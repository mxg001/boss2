package cn.eeepay.framework.service.impl.risk;

import cn.eeepay.framework.dao.risk.SurveyOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.YfbPayOrder;
import cn.eeepay.framework.model.allAgent.MerchantAllAgent;
import cn.eeepay.framework.model.risk.DeductAddInfo;
import cn.eeepay.framework.model.risk.Reminder;
import cn.eeepay.framework.model.risk.SurveyOrder;
import cn.eeepay.framework.service.PosCardBinService;
import cn.eeepay.framework.service.RepaySettleOrderService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.service.allAgent.MerchantAllAgentService;
import cn.eeepay.framework.service.allAgent.RandomNumAllAgentService;
import cn.eeepay.framework.service.historyquery.TransInfoHistoryService;
import cn.eeepay.framework.service.risk.SurveyDeductDetailService;
import cn.eeepay.framework.service.risk.SurveyOrderLogService;
import cn.eeepay.framework.service.risk.SurveyOrderService;
import cn.eeepay.framework.service.risk.SurveyReplyService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.Md5;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/9/7/007.
 * @author  liuks
 * 调单管理
 */
@Service("surveyOrderService")
public class SurveyOrderServiceImpl implements SurveyOrderService {

    private static final Logger log = LoggerFactory.getLogger(SurveyOrderServiceImpl.class);

    @Resource
    private SurveyOrderDao surveyOrderDao;
    @Resource
    private TransInfoService transInfoService;
    @Resource
    private TransInfoHistoryService transInfoHistoryService;
    @Resource
    private RandomNumAllAgentService randomNumAllAgentService;
    @Resource
    private SurveyOrderLogService surveyOrderLogService;
    @Resource
    private SurveyDeductDetailService surveyDeductDetailService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private SurveyReplyService surveyReplyService;
    @Resource
    private PosCardBinService posCardBinService;
    @Resource
    private RepaySettleOrderService repaySettleOrderService;
    @Resource
    private MerchantAllAgentService merchantAllAgentService;

    private final String Token = "a185faf4c55807bca4bcfdaae06b58fa";
    private final String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
    private final String PATH = "/riskhandle/commonJPush";

    @Override
    public List<SurveyOrder> selectAllList(SurveyOrder order, Page<SurveyOrder> page) {
        return surveyOrderDao.selectAllList(order,page);
    }

    @Override
    public int addSurveyOrder(SurveyOrder order,Map<String, Object> msg) {
        if(order!=null){
            if(order.getDataSta()!=null){
                if(order.getTransOrderNo()!=null&&!"".equals(order.getTransOrderNo())){
                    if(checkOrder(order,msg)){
                        return 0;
                    }
                    //当前登入用户
                    UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    String userName=principal.getUsername();

                    String[] ordstr=order.getTransOrderNo().split(",");
                    if(ordstr!=null&&ordstr.length>0){
                        StringBuffer errorMsg=new StringBuffer();//数据库是否含有校验
                        StringBuffer paErrorMsg=new StringBuffer();//超级盟主订单校验
                        List<SurveyOrder> list=new ArrayList<SurveyOrder>();
                        int sta=0;
                        String remark="";
                        List<String> checkList=new ArrayList<String>();
                        for(String ord:ordstr){
                            if(checkList.contains(ord)){
                                msg.put("msg","新增调单失败,添加的订单号:"+ord+"重复");
                                msg.put("status", false);
                                msg.put("addsta", "2");
                                return 0;
                            }
                            checkList.add(ord);

                            if("".equals(remark)){
                                remark=order.getOrderRemark();
                            }

                            if("3".equals(order.getOrderServiceCode())){//超级还款的订单
                                YfbPayOrder  repayOrder=null;
                                if(order.getDataSta().intValue()==0){
                                    repayOrder =repaySettleOrderService.getYfbPayOrder(ord);
                                }
                                if(repayOrder!=null){
                                    list.add(getTransInfoToSurveyOrder(order,null,repayOrder,userName));
                                }else {
                                    errorMsg.append(ord).append(",");
                                    sta=1;
                                }
                            }else{
                                CollectiveTransOrder transOrder=null;
                                if(order.getDataSta().intValue()==0){ //近期数据库
                                    transOrder=transInfoService.getCtoBySurvey(ord);
                                }else{//历史数据库
                                    transOrder=transInfoHistoryService.getCtoBySurvey(ord);
                                }
                                //超级盟主
                                if(transOrder!=null){
                                    if("2".equals(order.getOrderServiceCode())){
                                        if("3".equals(transOrder.getRecommendedSource())){//超级盟主商户
                                            MerchantAllAgent merchantAllAgent=merchantAllAgentService.queryMerchantAllAgentByMerNo(transOrder.getMerchantNo());
                                            if(merchantAllAgent!=null){
                                                transOrder.setPaUserNo(merchantAllAgent.getUserCode());
                                                transOrder.setUserNode(merchantAllAgent.getUserNode());
                                                list.add(getTransInfoToSurveyOrder(order,transOrder,null,userName));
                                            }else{
                                                paErrorMsg.append(ord).append(",");
                                                sta=2;
                                            }
                                        }else{
                                            paErrorMsg.append(ord).append(",");
                                            sta=2;
                                        }
                                    }else{
                                        list.add(getTransInfoToSurveyOrder(order,transOrder,null,userName));
                                    }
                                }else{
                                    errorMsg.append(ord).append(",");
                                    sta=1;
                                }
                            }
                        }
                        if(sta==0){
                            int num=surveyOrderDao.addSurveyOrder(list);
                            if(num>0){
                                for(SurveyOrder so:list){
                                    surveyOrderLogService.insertSurveyOrderLog(so.getOrderNo(),"0",remark);
                                }
                                msg.put("msg","新增调单成功!");
                                msg.put("status", true);
                                msg.put("addsta", "1");

                                // 进行极光推送
                                sendJPush(list);

                                return num;
                            }else{
                                msg.put("msg","新增调单失败!");
                                msg.put("status", false);
                                msg.put("addsta", "0");
                            }
                        }else if(sta==1){
                            String error=errorMsg.toString().substring(0,errorMsg.toString().length()-1);
                            String title=order.getDataSta().intValue()==0?"近期数据库":"历史数据库";
                            msg.put("msg","新增调单失败,"+title+"中不存在的订单编号:"+error);
                            msg.put("status", false);
                            msg.put("addsta", "2");
                        }else if(sta==2){
                            String error=paErrorMsg.toString().substring(0,paErrorMsg.toString().length()-1);
                            msg.put("msg","新增调单失败,订单编号:"+error+"不是超级盟主的订单!");
                            msg.put("status", false);
                            msg.put("addsta", "2");
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 极光推送
     * @param list
     */
    private void sendJPush(List<SurveyOrder> list){
        String tittle = "新增调单";
        String content = "您有1条调单未处理，请及时处理！";
        for (SurveyOrder surveyOrder : list) {
            if(!"6".equals(surveyOrder.getReplyStatus())){ //状态6为无需处理
                // 一级代理商
                if(surveyOrder.getOneAgentNo().equals(surveyOrder.getAgentNo())){
                    sendJPush(surveyOrder.getOneAgentNo(), tittle, content, surveyOrder.getOrderNo(), "1");
                }else{
                    // 一级代理商
                    sendJPush(surveyOrder.getOneAgentNo(), tittle, content, surveyOrder.getOrderNo(), "1");
                    // 直属代理商
                    sendJPush(surveyOrder.getAgentNo(), tittle, content, surveyOrder.getOrderNo(),"2");
                }
                // 3. 推送给商户
                sendJPush(surveyOrder.getMerchantNo(), tittle, content, surveyOrder.getOrderNo(),"0");
            }
        }

    }

    /**
     * 极光推送
     * @param merchantNo
     * @param tittle
     * @param content
     */
    @Override
    public void sendJPush(String merchantNo, String tittle, String content, String orderNo, String ownStatus){

        String authCode = Md5.md5Str(USER_INFO_ID + merchantNo + Token).toUpperCase();
        String url = sysDictService.getByKey("CORE_URL").getSysValue() + PATH;

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("token", Token);
        requestMap.put("merchantNo", merchantNo);
        requestMap.put("authCode", authCode);
        requestMap.put("tittle", tittle);
        requestMap.put("content", content);
        String ext = "{\"type\":\"surveyOrder\",\"orderNo\":\""+orderNo+"\",\"ownStatus\":\"" + ownStatus + "\"}";
        requestMap.put("ext", ext);
        String str = HttpUtils.sendPostRequest(url, requestMap);
        log.info("调单" + orderNo + "极光推送给" + merchantNo + "的结果：" + str);
    }


    @Override
    public int deleteSurveyOrder(int id) {
        SurveyOrder old=surveyOrderDao.getSurveyOrder(id);
        if(("0".equals(old.getReplyStatus())&&"0".equals(old.getDealStatus()))
                ||"9".equals(old.getDealStatus())){
            int num=surveyOrderDao.deleteSurveyOrder(id);
            if(num>0){
                surveyOrderDao.regressesUpdateReminderLog(old.getOrderNo());
                surveyOrderLogService.insertSurveyOrderLog(old.getOrderNo(),"4",null);
            }
            return num;
        }
        return 0;
    }

    @Override
    public void deleteOrderBatch(String ids, Map<String, Object> msg) {
        if(ids!=null&&!"".equals(ids)){
            String[] strs=ids.split(",");
            if(strs!=null&&strs.length>0){
                int num=0;
                for(String id:strs){
                    SurveyOrder old=surveyOrderDao.getSurveyOrder(Integer.parseInt(id));
                    if(("0".equals(old.getReplyStatus())&&"0".equals(old.getDealStatus()))
                            ||"9".equals(old.getDealStatus())){
                        int num1=surveyOrderDao.deleteSurveyOrder(Integer.parseInt(id));
                        if(num1>0){
                            surveyOrderDao.regressesUpdateReminderLog(old.getOrderNo());
                            surveyOrderLogService.insertSurveyOrderLog(old.getOrderNo(),"4",null);
                        }
                        num=num+num1;
                    }
                }
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "批量删除调单成功,符合的数据"+strs.length+"条,成功删除"+num+"条!");
                    return;
                }
            }
        }
        msg.put("status", false);
        msg.put("msg", "批量删除调单失败!");
    }

    @Override
    public int reminder(int id,List<String> list) {
        SurveyOrder old=surveyOrderDao.getSurveyOrder(id);
        if(!"9".equals(old.getDealStatus())){
            int num=surveyOrderDao.reminder(id);
            if(num>0){
                reminderDetail(old,list);
            }
            return num;
        }
        return 0;
    }

    @Override
    public void reminderBatch(String ids, Map<String, Object> msg,List<String> list) {
        if(ids!=null&&!"".equals(ids)){
            String[] strs=ids.split(",");
            if(strs!=null&&strs.length>0){
                int allnum=0;
                for(String id:strs){
                    SurveyOrder old=surveyOrderDao.getSurveyOrder(Integer.parseInt(id));
                    if(!"9".equals(old.getDealStatus())){
                        int num=surveyOrderDao.reminder(Integer.parseInt(id));
                        if(num>0){
                            reminderDetail(old,list);
                        }
                        allnum=allnum+num;
                    }
                }
                if(allnum>0){
                    msg.put("status", true);
                    msg.put("msg", "批量催单成功,符合的数据"+strs.length+"条,成功催单"+allnum+"条!");
                    return;
                }
            }
        }
        msg.put("status", false);
        msg.put("msg", "批量催单失败!");
    }

    /**
     * 催单修改完成后，更新相关信息
     */
    private void reminderDetail(SurveyOrder old,List<String> list){
        if(old!=null){
            Reminder re=new Reminder();
            re.setOrderNo(old.getOrderNo());
            re.setAgentNode(old.getAgentNode());
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            re.setOperator(principal.getUsername());
            surveyOrderDao.insertReminderLog(re);
            surveyOrderLogService.insertSurveyOrderLog(old.getOrderNo(),"3",null);

            if("2".equals(old.getOrderServiceCode())){
                list.add(old.getOrderNo());
            }
        }
    }
    @Override
    public int regresses(SurveyOrder order) {
        if("8".equals(order.getDealStatus())){//回退
            SurveyOrder old=surveyOrderDao.getSurveyOrder(order.getId());
            if(!"9".equals(old.getDealStatus())&&("2".equals(old.getReplyStatus())||"5".equals(old.getReplyStatus()))){
                int num=surveyOrderDao.regresses(order);
                if(num>0){
                    surveyOrderDao.regressesUpdateReminderLog(old.getOrderNo());
                    surveyOrderLogService.insertSurveyOrderLog(old.getOrderNo(),"1",order.getDealRemark());
                    surveyReplyService.updateNowSurveyReply(old.getOrderNo(),order.getDealStatus(),order.getDealRemark());
                }
                return num;
            }
        }
        return 0;
    }

    @Override
    public int handle(SurveyOrder order) {
        SurveyOrder old=surveyOrderDao.getSurveyOrder(order.getId());
        if("9".equals(old.getDealStatus())||"2".equals(old.getReplyStatus())||"5".equals(old.getReplyStatus())){
            int num=surveyOrderDao.handle(order);
            if(num>0){
                surveyOrderLogService.insertSurveyOrderLog(old.getOrderNo(),"2",order.getDealRemark());
                surveyReplyService.updateNowSurveyReply(old.getOrderNo(),order.getDealStatus(),order.getDealRemark());
            }
            return num;
        }
        return 0;
    }

    @Override
    public void handleBatch(SurveyOrder order, Map<String, Object> msg) {
        if(order.getIds()!=null&&!"".equals(order.getIds())){
            String[] strs=order.getIds().split(",");
            if(strs!=null&&strs.length>0){
                int allnum=0;
                for(String id:strs){
                    SurveyOrder old=surveyOrderDao.getSurveyOrder(Integer.parseInt(id));
                    if(!"9".equals(old.getDealStatus())&&("2".equals(old.getReplyStatus())||"5".equals(old.getReplyStatus()))){
                        order.setId(Integer.parseInt(id));
                        int num=surveyOrderDao.handle(order);
                        if(num>0){
                            Reminder re=new Reminder();
                            re.setOrderNo(old.getOrderNo());
                            re.setAgentNode(old.getAgentNode());
                            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                            re.setOperator(principal.getUsername());
                            surveyOrderLogService.insertSurveyOrderLog(old.getOrderNo(),"2",order.getDealRemark());
                            surveyReplyService.updateNowSurveyReply(old.getOrderNo(),order.getDealStatus(),order.getDealRemark());

                            //如果处理结果为非终态则给一级代理商推送提醒
                            String dealStatus = order.getDealStatus();
                            if(!"2".equals(dealStatus) && !"3".equals(dealStatus) && !"6".equals(dealStatus)){
                                //如果处理结果为非终态则给一级代理商推送提醒
                                SurveyOrder surveyOrder = selectById(order.getId());
                                sendJPush(surveyOrder.getOneAgentNo(), "调单更新极光推送", "您有1条调单有更新，请及时处理！", surveyOrder.getOrderNo(),"1");
                            }
                        }
                        allnum=allnum+num;
                    }
                }
                if(allnum>0){
                    msg.put("status", true);
                    msg.put("msg", "批量处理成功,符合的数据"+strs.length+"条,成功处理"+allnum+"条!");
                    return;
                }
            }
        }
        msg.put("status", false);
        msg.put("msg", "批量处理失败!");
    }

    @Override
    public int deduct(SurveyOrder order, Map<String, Object> msg) {
        SurveyOrder old=surveyOrderDao.getSurveyOrder(order.getId());
        if("0".equals(old.getHaveAddDeduct())){
            int num=surveyOrderDao.updateDeduct(order.getId());
            if(num>0){
                order.setOrderNo(old.getOrderNo());
                UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                order.setOperator(principal.getUsername());
                surveyDeductDetailService.addDeductDetail(order);
                surveyOrderLogService.insertSurveyOrderLog(old.getOrderNo(),"5",order.getAcqDeductRemark());
            }
            return num;
        }
        return 0;
    }

    @Override
    public List<SurveyOrder> importDetailSelect(SurveyOrder order) {
        return surveyOrderDao.importDetailSelect(order);
    }

    @Override
    public void importDetail(List<SurveyOrder> list, HttpServletResponse response) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "调单管理列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("orderTypeCode",null);
            maps.put("transOrderNo",null);
            maps.put("orderServiceCode",null);
            maps.put("acqReferenceNo",null);
            maps.put("createTime",null);
            maps.put("replyEndTime",null);
            maps.put("replyStatus",null);
            maps.put("dealStatus",null);
            maps.put("replyTypeName",null);
            maps.put("haveAddDeduct",null);
            maps.put("urgeNum",null);
            maps.put("merchantNo",null);
            maps.put("merchantName",null);
            maps.put("acqMerchantNo",null);
            maps.put("acqMerchantName",null);
            maps.put("paUserNo",null);
            maps.put("unionpayMerNo",null);
            maps.put("cardType",null);
            maps.put("cardName",null);
            maps.put("transStatus",null);
            maps.put("transAccountNo",null);
            maps.put("transAmount",null);
            maps.put("transTime",null);
            maps.put("agentName",null);
            maps.put("oneAgentName",null);
            maps.put("saleName",null);
            maps.put("lastReplyAgent",null);
            maps.put("dealRemark",null);
            maps.put("acqReplyStatus",null);
            maps.put("lastReplyTime",null);
            maps.put("acqReplyRemark",null);
            maps.put("acqCode",null);
            maps.put("payMethod",null);
            maps.put("creator",null);
            data.add(maps);
        }else{
            Map<String, String> orderTypeCodeMap=sysDictService.selectMapByKey("ORDER_TYPE_CODE");//调单类型
            Map<String, String> orderServiceCodeMap=sysDictService.selectMapByKey("ORDER_SERVICE_CODE");//业务类型
            Map<String, String> payMethodMap=sysDictService.selectMapByKey("PAY_METHOD_TYPE");//支付方式
            Map<String, String> transStatusMap=sysDictService.selectMapByKey("TRANS_STATUS");//交易状态

            Map<String, String> replyStatusMap=new HashMap<String, String>();
            replyStatusMap.put("0","未回复");
            replyStatusMap.put("1","未回复(下级已提交)");
            replyStatusMap.put("2","已回复");
            replyStatusMap.put("3","逾期未回复");
            replyStatusMap.put("4","逾期未回复(下级已提交)");
            replyStatusMap.put("5","逾期已回复");
            replyStatusMap.put("6","无需处理");

            Map<String, String> dealStatusMap=new HashMap<String, String>();
            dealStatusMap.put("0","未处理");
            dealStatusMap.put("1","部分提供");
            dealStatusMap.put("2","持卡人承认交易");
            dealStatusMap.put("3","全部提供");
            dealStatusMap.put("4","无法提供");
            dealStatusMap.put("5","逾期部分提供");
            dealStatusMap.put("6","逾期全部提供");
            dealStatusMap.put("7","逾期未回");
            dealStatusMap.put("8","已回退");
            dealStatusMap.put("9","无需提交资料");

            Map<String, String> haveAddDeductMap=new HashMap<String, String>();
            haveAddDeductMap.put("0","未添加");
            haveAddDeductMap.put("1","已添加");

            Map<String, String> acqReplyStatusMap=new HashMap<String, String>();
            acqReplyStatusMap.put("0","未回复");
            acqReplyStatusMap.put("1","已回复");

            for (SurveyOrder or : list) {
                //获取详细信息
                surveyOrderDetail(or,false);

                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?"":or.getOrderNo());
                maps.put("orderTypeCode",orderTypeCodeMap.get(or.getOrderTypeCode()==null?"":or.getOrderTypeCode()));
                maps.put("transOrderNo",or.getTransOrderNo()==null?"":or.getTransOrderNo());
                maps.put("orderServiceCode",orderServiceCodeMap.get(or.getOrderServiceCode()==null?"":or.getOrderServiceCode()));
                maps.put("acqReferenceNo",or.getAcqReferenceNo()==null?"":or.getAcqReferenceNo());
                maps.put("createTime",or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("replyEndTime",or.getReplyEndTime()==null?"":sdf1.format(or.getReplyEndTime()));
                maps.put("replyStatus",replyStatusMap.get(or.getReplyStatus()==null?"":or.getReplyStatus()));
                maps.put("dealStatus",dealStatusMap.get(or.getDealStatus()==null?"":or.getDealStatus()));
                maps.put("replyTypeName",or.getReplyTypeName()==null?"":or.getReplyTypeName());
                maps.put("haveAddDeduct",haveAddDeductMap.get(or.getHaveAddDeduct()==null?"":or.getHaveAddDeduct()));
                maps.put("urgeNum",or.getUrgeNum()==null?"":or.getUrgeNum().toString());
                maps.put("merchantNo",or.getMerchantNo()==null?"":or.getMerchantNo());
                maps.put("merchantName",or.getMerchantName()==null?"":or.getMerchantName());
                maps.put("acqMerchantNo",or.getAcqMerchantNo()==null?"":or.getAcqMerchantNo());
                maps.put("acqMerchantName",(or.getTransOrder()==null||or.getTransOrder().getAcqMerchantName()==null)?"":or.getTransOrder().getAcqMerchantName());
                maps.put("paUserNo",or.getPaUserNo()==null?"":or.getPaUserNo());
                maps.put("unionpayMerNo",(or.getTransOrder()==null||or.getTransOrder().getUnionpayMerNo()==null)?"":or.getTransOrder().getUnionpayMerNo());
                maps.put("cardType",(or.getCard()==null||or.getCard().getCardType()==null)?"":or.getCard().getCardType());
                maps.put("transAccountBank",(or.getCard()==null||or.getCard().getBankName()==null)?"":or.getCard().getBankName());
                maps.put("cardName",(or.getCard()==null||or.getCard().getCardName()==null)?"":or.getCard().getCardName());
                maps.put("transStatus",transStatusMap.get(or.getTransStatus()==null?"":or.getTransStatus()));
                maps.put("transAccountNo",or.getTransAccountNo()==null?"":or.getTransAccountNo());
                maps.put("transAmount",or.getTransAmount()==null?"":or.getTransAmount().toString());
                maps.put("transTime",or.getTransTime()==null?"":sdf1.format(or.getTransTime()));
                maps.put("agentName",or.getAgentName()==null?"":or.getAgentName());
                maps.put("oneAgentName",or.getOneAgentName()==null?"":or.getOneAgentName());
                maps.put("saleName",or.getSaleName()==null?"":or.getSaleName());
                maps.put("lastReplyAgent",(or.getReply()==null||or.getReply().getReplyRemark()==null)?"":or.getReply().getReplyRemark());
                maps.put("dealRemark",or.getDealRemark()==null?"":or.getDealRemark());
                maps.put("acqReplyStatus",acqReplyStatusMap.get(or.getAcqReplyStatus()==null?"":or.getAcqReplyStatus()));
                maps.put("lastReplyTime",or.getLastReplyTime()==null?"":sdf1.format(or.getLastReplyTime()));
                maps.put("acqReplyRemark",or.getAcqReplyRemark()==null?"":or.getAcqReplyRemark());
                maps.put("acqCode",or.getAcqCode()==null?"":or.getAcqCode());
                maps.put("payMethod",payMethodMap.get(or.getPayMethod()==null?"":or.getPayMethod()));
                maps.put("creator",or.getCreator()==null?"":or.getCreator());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","orderTypeCode","transOrderNo","orderServiceCode",
                "acqReferenceNo","createTime","replyEndTime","replyStatus","dealStatus","replyTypeName","haveAddDeduct",
                "urgeNum","merchantNo","merchantName","acqMerchantNo","acqMerchantName","paUserNo","unionpayMerNo",
                "cardType","transAccountBank","cardName","transStatus","transAccountNo","transAmount",
                "transTime","agentName","oneAgentName","saleName",
                "lastReplyAgent","dealRemark","acqReplyStatus","lastReplyTime","acqReplyRemark",
                "acqCode","payMethod","creator"
        };
        String[] colsName = new String[]{"调单号","调单类型","订单编号","业务类型",
                "系统参考号","发起时间","截止回复时间","回复状态","处理状态","提交回复角色","是否添加扣款",
                "催单次数","商户编号","商户名称","收单商户编号","收单商户名称","所属盟主编号","银联报备商户编号",
                "卡种","发卡行","卡类型","交易状态","交易卡号","交易金额",
                "交易时间","所属代理商","一级代理商","一级代理商所属销售",
                "代理商回复备注","处理备注","上游回复状态","代理商回复时间","上游回复备注",
                "收单机构","交易方式","创建人"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出调单管理列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public SurveyOrder getSurveyOrderDetail(int id) {
        SurveyOrder order=surveyOrderDao.getSurveyOrderDetail(id);
        surveyOrderDetail(order,true);
        return order;
    }

    /**
     * 获取详情数据
     * @param order
     * @param sta true 获取更多
     */
    private void surveyOrderDetail(SurveyOrder order,boolean sta){
        if(order!=null){
            if("3".equals(order.getOrderServiceCode())){
                if("now".equals(order.getTransOrderDatabase())) {
                    order.setTransOrder(repayToCto(repaySettleOrderService.getYfbPayOrder(order.getTransOrderNo())));
                }
            }else{
                if("now".equals(order.getTransOrderDatabase())){ //近期数据库
                    order.setTransOrder(transInfoService.getCtoBySurvey(order.getTransOrderNo()));
                }else if("old".equals(order.getTransOrderDatabase())){//历史数据库
                    order.setTransOrder(transInfoHistoryService.getCtoBySurvey(order.getTransOrderNo()));
                }
            }
            order.setReply(surveyReplyService.getMaxReply(order.getOrderNo()));
            order.setCard(posCardBinService.queryInfo(order.getTransAccountNo()));
            if(sta){
                order.setReplyList(surveyReplyService.getReplyList(order.getOrderNo()));
                order.setLogList(surveyOrderLogService.getSurveyOrderLogAll(order.getOrderNo()));
                //初始化调单模板
                order.setTemplateList(surveyReplyService.getFileList(order.getTemplateFilesName()));
            }
        }
    }

    //将超级还订单信息转换成订单实体
    private  CollectiveTransOrder repayToCto(YfbPayOrder repayOrder){
        if(repayOrder!=null){
            CollectiveTransOrder transOrder=new CollectiveTransOrder();
            transOrder.setAcqMerchantName(repayOrder.getAcqMerchantNo());
            transOrder.setUnionpayMerNo(repayOrder.getZqMerchantNo());
            transOrder.setId(repayOrder.getId());
            transOrder.setMerchantFee(repayOrder.getTransFee());
            return transOrder;
        }
        return null;
    }

    @Override
    public SurveyOrder selectSurveyOrder(String orderNo) {
        SurveyOrder order=surveyOrderDao.selectSurveyOrder(orderNo);
        return  order;
    }

    @Override
    public int updateAgentState(DeductAddInfo info) {
        if(info!=null){
            if("6".equals(info.getOperatorType())){
                return surveyOrderDao.updateAgentState1(info);
            }else if("7".equals(info.getOperatorType())){
                return surveyOrderDao.updateAgentState2(info);
            }
        }
        return 0;
    }

    @Override
    public int updateOrderStateOverdue() {
       return surveyOrderDao.updateOrderStateOverdue();
    }

    @Override
    public List<Reminder> getRecordList(String orderNo) {
        return surveyOrderDao.getRecordList(orderNo);
    }

    @Override
    public int upstream(SurveyOrder order) {
        int num=surveyOrderDao.upstream(order);
        if(num>0){
            SurveyOrder old=surveyOrderDao.getSurveyOrder(order.getId());
            surveyOrderLogService.insertSurveyOrderLog(old.getOrderNo(),"14",order.getAcqReplyRemark());
        }
        return num;
    }

    private boolean checkOrder(SurveyOrder order,Map<String, Object> msg){
        List<SurveyOrder> list=surveyOrderDao.checkOrder(order);
        if(list!=null&&list.size()>0){
            StringBuffer errorMsg=new StringBuffer();
            for(SurveyOrder so:list){
                errorMsg.append(so.getTransOrderNo()).append(",");
            }
            String error=errorMsg.toString().substring(0,errorMsg.toString().length()-1);
            msg.put("msg","新增调单失败,该类型下,订单编号:"+error+"已存在的!");
            msg.put("status", false);
            msg.put("addsta", "2");
            return true;
        }
        return false;
    }
    //封装提交的数据
    public SurveyOrder getTransInfoToSurveyOrder(SurveyOrder oldOrd,CollectiveTransOrder transOrder,YfbPayOrder repayOrder,String userName){

        SurveyOrder newOrd=new SurveyOrder();
        newOrd.setOrderNo(randomNumAllAgentService.getRandomNumberByData("TQ","nposp"));
        newOrd.setTransOrderDatabase(oldOrd.getDataSta().intValue()==0?"now":"old");
        newOrd.setOrderServiceCode(oldOrd.getOrderServiceCode());
        newOrd.setOrderTypeCode(oldOrd.getOrderTypeCode());
        newOrd.setReplyEndTime(oldOrd.getReplyEndTime());
        newOrd.setTemplateFilesName(oldOrd.getTemplateFilesName());
        newOrd.setOrderRemark(oldOrd.getOrderRemark());
        newOrd.setReplyStatus(oldOrd.getOrderSta()==0?"0":"6");
        newOrd.setDealStatus(oldOrd.getOrderSta()==0?"0":"9");
        newOrd.setCreator(userName);
        if(transOrder!=null){
            newOrd.setMerchantNo(transOrder.getMerchantNo());
            newOrd.setTransOrderNo(transOrder.getOrderNo());
            newOrd.setAcqReferenceNo(transOrder.getAcqReferenceNo());
            newOrd.setAgentNode(transOrder.getMerAgentNode());
            newOrd.setTransAmount(transOrder.getTransAmount());
            newOrd.setTransAccountNo(transOrder.getAccountNo());
            newOrd.setTransTime(transOrder.getTransTime());
            newOrd.setAcqCode(transOrder.getAcqEnname());
            newOrd.setAcqMerchantNo(transOrder.getAcqMerchantNo());
            newOrd.setPayMethod(transOrder.getPayMethod());
            newOrd.setTransStatus(transOrder.getTransStatus());
            newOrd.setAgentNo(transOrder.getAgentNo());
            newOrd.setOneAgentNo(transOrder.getOneAgentNo());
            newOrd.setPaUserNo(transOrder.getPaUserNo());//超级盟主用户编号
            newOrd.setUserNode(transOrder.getUserNode());//超级盟主用户节点
        }
        if(repayOrder!=null){
            newOrd.setMerchantNo(repayOrder.getMerchantNo());
            newOrd.setTransOrderNo(repayOrder.getOrderNo());
            newOrd.setAcqReferenceNo(null);
            newOrd.setAgentNode(repayOrder.getMerAgentNode());
            newOrd.setTransAmount(repayOrder.getTransAmount());
            newOrd.setTransAccountNo(repayOrder.getAccountNo());
            newOrd.setTransTime(repayOrder.getTransTime());
            newOrd.setAcqCode(repayOrder.getAcqCode());
            newOrd.setAcqMerchantNo(repayOrder.getAcqMerchantNo());
            newOrd.setPayMethod("4");
            newOrd.setAgentNo(repayOrder.getAgentNo());
            newOrd.setOneAgentNo(repayOrder.getOneAgentNo());
            //交易状态转换
            Map<String, String> transStatusMap=new HashMap<String, String>();
            transStatusMap.put("0","INIT");
            transStatusMap.put("1","TRADING");
            transStatusMap.put("2","SUCCESS");
            transStatusMap.put("3","FAILED");
            transStatusMap.put("4","UNKNOWN");
            newOrd.setTransStatus(transStatusMap.get(repayOrder.getTransStatus()));
        }
        return newOrd;
    }

    @Override
    public SurveyOrder selectById(Integer id) {
        return surveyOrderDao.selectById(id);
    }
}
