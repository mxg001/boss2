package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ActivityDetailBackstageDao;
import cn.eeepay.framework.dao.ActivityDetailDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.daoAllAgent.AgentUserDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ActivityDetailBackstageService;
import cn.eeepay.framework.service.ActivityDetailService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.RandomNumber;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("activityDetailService")
@Transactional
public class ActivityDetailServiceImpl implements ActivityDetailService {

    private final static Logger log = LoggerFactory.getLogger(ActivityDetailServiceImpl.class);
    @Resource
    private ActivityDetailBackstageDao activityDetailBackstageDao;

    @Resource
    private ActivityDetailDao activityDetailDao;

    @Resource
    private SysDictDao sysDictDao;

    @Resource
    private ActivityDetailBackstageService activityDetailBackstageService;

    @Resource
    private TransInfoDao transInfoDao;

    @Resource
    private AgentUserDao agentUserDao;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private AgentInfoService agentInfoService;

    /**
     * 条件分页查询
     */
    @Override
    public List<ActivityDetail> selectActivityDetail(Page<ActivityDetail> page,ActivityDetail activityDetail) {
        return activityDetailDao.selectAllInfo(page,activityDetail);
    }


    /**
     * 导出
     * 导出满足查询条件的所有数据
     */
    @Override
    public void exportExcel(Page<ActivityDetail> page, ActivityDetail ad, HttpServletResponse response) throws IOException {
        List<ActivityDetail> list=null;
        if(ad.getCountAll()||ad.getPageAll()){
            if(ad.getPageAll()){//当前页
                activityDetailDao.selectAllInfo(page,ad);
                list=page.getResult();
            }
            if(ad.getCountAll()){//全部数据
                list=activityDetailDao.selectAllInfoAll(ad);
            }
        }else{//选中的数据
            list=activityDetailDao.selectAllInfoAll(ad);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "欢乐送"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;
        if(list!=null&&list.size()>0) {
            for (ActivityDetail item : list) {
                map = new HashMap<>();
                map.put("id", String.valueOf(item.getId()));
                map.put("activeOrder", item.getActiveOrder());
                map.put("cashOrder", item.getCashOrder());
                map.put("activeTime", item.getActiveTime() == null ? "" : sdfTime.format(item.getActiveTime()));
                map.put("merchantName", item.getMerchantName());
                map.put("enterTime", item.getEnterTime() == null ? "" : sdfTime.format(item.getEnterTime()));
                map.put("agentNo", item.getAgentNo());
                map.put("agentName", item.getAgentName());
                map.put("oneAgentNo", item.getOneAgentNo());
                map.put("oneAgentName", item.getOneAgentName());
                map.put("transTotal", StringUtil.filterNull(item.getTransTotal()));
                map.put("frozenAmout", StringUtil.filterNull(item.getFrozenAmout()));
                map.put("status", item.getStatusStr());
                map.put("targetAmout", StringUtil.filterNull(item.getTargetAmout()));
                map.put("checkStatus", item.getCheckStatusStr());
                map.put("dicountStatus", item.getDiscountStatus() != null
                        && item.getDiscountStatus() == 1 ? "已扣回" : "未扣回");
                map.put("acqEnname", item.getAcqEnname());
                map.put("cashTime", item.getCashTime() == null ? "" : sdfTime.format(item.getCashTime()));
                map.put("settleTransferId", item.getSettleTransferId());
                map.put("merchantFee", StringUtil.filterNull(item.getMerchantFee()));
                map.put("merchantFeeAmount", StringUtil.filterNull(item.getMerchantFeeAmount()));
                map.put("merchantOutAmount", StringUtil.filterNull(item.getMerchantOutAmount()));
                map.put("merchantSettleDate", item.getMerchantSettleDate() == null ? "" : sdfTime.format(item.getMerchantSettleDate()));
                map.put("checkOperatorName", item.getCheckOperatorName());
                map.put("checkTime", item.getCheckTime() == null ? "" : sdfTime.format(item.getCheckTime()));
                map.put("discountOperatorName", item.getDiscountOperatorName());
                map.put("discountTime", item.getDiscountTime() == null ? "" : sdfTime.format(item.getDiscountTime()));
                data.add(map);
            }
            ListDataExcelExport export = new ListDataExcelExport();
            String[] cols = new String[]{"id", "activeOrder", "cashOrder", "activeTime", "merchantName"
                    , "enterTime", "acqEnname", "transTotal", "merchantFee", "frozenAmout", "merchantFeeAmount",
                    "merchantOutAmount", "settleTransferId", "cashTime", "status",
                    "targetAmout", "agentName", "agentNo", "oneAgentName", "oneAgentNo", "checkStatus", "checkOperatorName", "checkTime",
                    "dicountStatus", "discountOperatorName", "discountTime"};
            String[] colsName = new String[]{"序号", "激活流水号", "提现流水号", "激活时间", "商户名称", "进件时间", "收单机构", "交易累计金额", "交易手续费",
                    "冻结金额", "商户提现费", "商户到账金额", "出款明细ID", "商户提现时间", "活动状态", "活动任务金额", "所属代理商名称", "所属代理商编号",
                    "一级代理商名称", "一级代理商编号", "核算状态", "核算操作人", "核算时间", "是否扣回", "扣回操作人", "扣回操作时间",};
            OutputStream ouputStream = null;
            try {
                ouputStream = response.getOutputStream();
                export.export(cols, colsName, data, response.getOutputStream());
            } catch (Exception e) {
                log.error("导出业务活动记录失败");
                e.printStackTrace();
            } finally {
                if (ouputStream != null) {
                    ouputStream.close();
                }
            }
        }
    }

    /**
     * 回盘导入
     * 改写activity_detail里面的扣回状态
     */
    @Override
    public Map<String, Object> importDiscount(MultipartFile file) throws EncryptedDocumentException, InvalidFormatException, IOException {
        Map<String, Object> msg = new HashMap<>();
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = String.valueOf(principal.getId());
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        ActivityDetail info = null;
        Sheet sheet = wb.getSheetAt(0);//读取第一个sheet
        // 遍历所有单元格，读取单元格
        int num = 0;
        int row_num = sheet.getLastRowNum();
        for (int i = 1; i <= row_num; i++) {
            info = new ActivityDetail();
            Row row = sheet.getRow(i);
            String activeOrder = getCellValue(row.getCell(1));//激活流水号
            String merchantNo = getCellValue(row.getCell(3));//商户编号
            String discountStatusStr = getCellValue(row.getCell(9));//扣回状态
            info.setActiveOrder(StringUtil.filterNull(activeOrder));
            info.setMerchantNo(StringUtil.filterNull(merchantNo));
            info.setDiscountStatus("已扣回".equals(discountStatusStr)?1:0);//"已扣回"返回1，否则返回0
            info.setDiscountOperator(userId);//扣回操作人
            int updateResult = activityDetailDao.updateDiscount(info);
            num += updateResult;
        }
        msg.put("status", true);
        msg.put("msg", "回盘导入成功,其中成功" + num + "条,失败"+ (row_num-num) + "条");
        return msg;
    }

    @Override
    public ActivityDetail getActivityDetailById(int id) {
        return activityDetailDao.selectActivityDetailById(id);
    }

    /**
     * @param ad
     * @param status 欢乐送核算状态
     * @return
     */
    @Override
    public Map<String, Object> updateAdjust(ActivityDetail ad,Page<ActivityDetail> page,String status) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = String.valueOf(principal.getId());
        Map<String, Object> msg = new HashMap<String, Object>();
        List<ActivityDetail> list=null;
        int sta=0;
        if("1".equals(ad.getBatchOrOne())){//未选中,直接点行按钮
            list=activityDetailDao.selectAllInfoAll(ad);
        }else if("2".equals(ad.getBatchOrOne())){
            if(ad.getCountAll()||ad.getPageAll()){
                if(ad.getPageAll()){//当前页
                    activityDetailDao.selectAllInfo(page,ad);
                    list=page.getResult();
                }
                if(ad.getCountAll()){//全部数据
                    list=activityDetailDao.selectAllInfoAll(ad);
                    sta=1;
                }
            }else{//选中数据
                list=activityDetailDao.selectAllInfoAll(ad);
            }
        }
        if(list!=null&&list.size()>0){
            if(sta==0){
                //及时访问接口
                int numTrue = 0;
                int numFalse = 0;
                SysDict merchantDict = sysDictDao.getByKey("HLS_MERNO_YS");
                for(int i=0;i<list.size();i++){
                    ActivityDetail item=list.get(i);
                    if( item.getCheckStatus()!=1&& item.getStatus()!=1){
                        if ("1".equals(status)) {//核算同意
                            try {
                                //活动结算商户
                                item.setMerchantNo(merchantDict.getSysValue());
                                String acc = ClientInterface.happySendAgentRecordAccountForCaiWu(item);
                                log.info("欢乐送核算接口返回[{}]", acc);
                                if (JSONObject.parseObject(acc).getBooleanValue("status")) {
                                    activityDetailDao.updateAdjustStatus(item.getId(), status, userId);
                                    numTrue++;
                                } else {
                                    log.info(item.getId() + "核算失败");
                                    numFalse++;
                                }
                            } catch (Exception e) {
                                log.error("核算异常", e);
                                numFalse++;
                            }
                        } else {//不同意，直接更新当前核算状态
                            int count = activityDetailDao.updateAdjustStatus(item.getId(), status, userId);
                            if (count > 0) {
                                numTrue++;
                            } else {
                                numFalse++;
                            }
                        }
                    }
                }
                String showMsg="核算操作完成!";
                if(numTrue>0){
                    showMsg=showMsg+"已成功核算:"+numTrue+"条;";
                }
                if(numFalse>0){
                    showMsg=showMsg+"核算失败:"+numFalse+"条;";
                }
                msg.put("msg",showMsg );
                msg.put("status", true);
                return msg;
            }else if(sta==1){
                //延时机制
                int numTrue = 0;
                int numFalse = 0;
                int numExist=0;
                for(int i=0;i<list.size();i++){
                    ActivityDetail item=list.get(i);
                    if( item.getCheckStatus()!=1&& item.getStatus()!=1){
                        if("1".equals(status)){//核算同意
                            List<ActivityDetailBackstage> actBackList=activityDetailBackstageService.getActivityDetailBackstage(item.getId(),"1");
                            if(actBackList!=null&&actBackList.size()>0){//已添加任务了,不重复添加
                                numExist++;
                            }else{//添加任务
                                ActivityDetailBackstage actBack=new ActivityDetailBackstage();
                                actBack.setActId(item.getId());
                                actBack.setActState("1");
                                actBack.setBatchNo(RandomNumber.mumberRandom("ACT002",6,2));
                                actBack.setUserId(principal.getId());
                                int num=activityDetailBackstageService.insertActivityDetailBackstage(actBack);
                                if(num>0){
                                    numTrue++;
                                }else{
                                    numFalse++;
                                }
                            }
                        }else{
                            int count=activityDetailDao.updateAdjustStatus(item.getId(),status,userId);
                            if(count>0){
                                numTrue++;
                            }else{
                                numFalse++;
                            }
                        }
                    }
                }
                if("1".equals(status)){
                    String showMsg="核算请求已提交,请稍后查询核算状态!";
                    if(numTrue>0){
                        showMsg=showMsg+"已成功提交:"+numTrue+"条;";
                    }
                    if(numFalse>0){
                        showMsg=showMsg+"提交失败:"+numFalse+"条;";
                    }
                    if(numExist>0){
                        showMsg=showMsg+"有"+numExist+"条已提交过;";
                    }
                    msg.put("msg",showMsg );
                    msg.put("status", true);
                    return msg;
                }else{
                    String showMsg="核算成功!";
                    if(numTrue>0){
                        showMsg=showMsg+"已成功核算:"+numTrue+"条;";
                    }
                    if(numFalse>0){
                        showMsg=showMsg+"核算失败:"+numFalse+"条;";
                    }
                    msg.put("msg",showMsg );
                    msg.put("status", true);
                    return msg;
                }
            }
        }else{
            msg.put("msg","没有符合核算的数据");
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 欢乐返商户条件查询
     */
    @Override
    public List<ActivityDetail>  selectHappyBackDetail(Page<ActivityDetail> page, ActivityDetail activityDetail) {
        return activityDetailDao.selectHappyBackDetail(page,activityDetail);
    }

    @Override
    public List<CashBackDetail>  selectAgentReturnCashDetailAll(Integer id,int amountType) {
        return activityDetailDao.selectAgentReturnCashDetailAll(id,amountType);
    }

    /**
     * 欢乐返商户查询by id
     */
    @Override
    public ActivityDetail  selectHappyBackDetailById(Integer id) {
        return activityDetailDao.selectHappyBackDetailById(id);
    }

    /**
     * 欢乐返商户提现开关
     */
    @Override
    public String  selectHappyTixianSwitch() {
        return sysDictDao.getValueByKey("HAPPY_TIXIAN_SWITCH");
    }

    /**
     * 欢乐返条件汇总金额
     */
    @Override
    public Map<String, Object> selectHappyBackTotalAmount(ActivityDetail activityDetail) {
        Map<String, Object> totalData=new HashMap<String, Object>();
        Map<String, Object> map1=activityDetailDao.selectHappyBackTotalAmount(activityDetail);
        if(map1!=null){
            totalData.putAll(map1);
        }
        Map<String, Object> map2=activityDetailDao.selectHappyBackTotalTransTotal(activityDetail);
        if(map2!=null){
            totalData.putAll(map2);
        }
        Map<String, Object> map3=activityDetailDao.selectHappyBackCashBackAmount(activityDetail);
        if(map3!=null){
            totalData.putAll(map3);
        }
        return totalData;
    }

    private static Map<String, String> activityCodeMap = new HashMap<>();
    private static Map<String, String> statusMap = new HashMap<>();
    private static Map<String, String> settleStatusMap = new HashMap<>();
    private static Map<String, String> isStandardMap = new HashMap<>();
    private static Map<String, String> activityTargetStatusMap = new HashMap<>();
//    private static Map<String, String> recommendedSourceMap = new HashMap<>();
    private static Map<String, String> repeatRegisterMap = new HashMap<>();
    private static Map<String, String> billingStatusMap = new HashMap<>();
    static {
        activityCodeMap.put("008", "欢乐返-循环送");
        activityCodeMap.put("009", "欢乐返");
        activityCodeMap.put("021", "欢乐返128");
        statusMap.put("1", "未激活");
        statusMap.put("2", "已激活");
        statusMap.put("6", "已返鼓励金");
        statusMap.put("7", "已扣款");
        statusMap.put("8", "预调账已发起");
        statusMap.put("9", "已奖励");
        settleStatusMap.put("1", "同意");
        settleStatusMap.put("2", "不同意");
        settleStatusMap.put("3", "未核算");
        isStandardMap.put("0", "未达标");
        isStandardMap.put("1", "已达标");
//        recommendedSourceMap.put("0", "正常注册");
//        recommendedSourceMap.put("1", "微创业");
//        recommendedSourceMap.put("2", "代理商分享");
//        recommendedSourceMap.put("3", "超级盟主");
        repeatRegisterMap.put("0", "否");
        repeatRegisterMap.put("1", "是");
        billingStatusMap.put("1","已入账");
        billingStatusMap.put("0","未入账");
        activityTargetStatusMap.put("0", "考核中");
        activityTargetStatusMap.put("1", "已达标");
        activityTargetStatusMap.put("2", "未达标");
    }
    public void exportHappyBack(Page<ActivityDetail> page, ActivityDetail ad, HttpServletResponse response) throws IOException {
        List<ActivityDetail> list=null;
        if(ad.getCountAll()||ad.getPageAll()){
            if(ad.getPageAll()){//当前页
                activityDetailDao.selectHappyBackDetail(page,ad);
                list=page.getResult();
            }
            if(ad.getCountAll()){//全部数据
                list=activityDetailDao.selectHappyBackDetailAll(ad);
            }
        }else{//选中数据
            list=activityDetailDao.selectHappyBackDetailAll(ad);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "欢乐返"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;
        if(list!=null&&list.size()>0){
            List<Map<String, String>> checkStatus = sysDictDao.getListByKey("CHECK_STATUS");
            Map<String,String> recommendedSourceMap = sysDictService.selectMapByKey("RECOMMENDED_SOURCES");
            for(ActivityDetail item: list){
                map = new HashMap<>();
                map.put("id", String.valueOf(item.getId()));
                map.put("activeOrder", item.getActiveOrder());
                map.put("activeTime", item.getActiveTime()==null?"":sdfTime.format(item.getActiveTime()));
                map.put("activityCode", StringUtils.trimToEmpty(activityCodeMap.get(item.getActivityCode())));
                map.put("activityTypeNo", item.getActivityTypeNo());
                map.put("activityTypeName", item.getActivityTypeName());
                map.put("merchantName", item.getMerchantName());
                map.put("merchantNo", item.getMerchantNo());
                map.put("teamName", item.getTeamName());
                map.put("teamEntryName", item.getTeamEntryName());
                map.put("hardId", item.getHardId()==null?"":item.getHardId().toString());
                map.put("recommendedSource", StringUtils.trimToEmpty(recommendedSourceMap.get(item.getRecommendedSource())));
                map.put("repeatRegister", StringUtils.trimToEmpty(repeatRegisterMap.get(item.getRepeatRegister()+"")));
                map.put("enterTime", item.getEnterTime()==null?"":sdfTime.format(item.getEnterTime()));
                map.put("transTotal", StringUtil.filterNull(item.getTransTotal()));
                map.put("acqEnname", item.getAcqEnname());
                map.put("acqMerchantFee",StringUtil.filterNull(item.getAcqMerchantFee()));
                map.put("cashBackAmount", StringUtil.filterNull(item.getCashBackAmount()));
                map.put("cumulateTransAmount", item.getCumulateTransAmount()==null?"":item.getCumulateTransAmount().toString());
                map.put("minOverdueTime", formatData(item.getMinOverdueTime()));
                map.put("overdueTime", formatData(item.getOverdueTime()));
                map.put("cumulateAmountMinus", item.getCumulateAmountMinus()==null?"":item.getCumulateAmountMinus().toString());
                map.put("cumulateAmountAdd", item.getCumulateAmountAdd()==null?"":item.getCumulateAmountAdd().toString());
                map.put("emptyAmount",StringUtil.filterNull(item.getEmptyAmount()));
                map.put("isStandard", StringUtils.trimToEmpty(isStandardMap.get(item.getIsStandard())));
                map.put("standardTime", formatData(item.getStandardTime()));
                map.put("minusAmountTime", formatData(item.getMinusAmountTime()));
                map.put("addAmountTime", formatData(item.getAddAmountTime()));
                map.put("fullAmount",StringUtil.filterNull(item.getFullAmount()));
                map.put("status", StringUtils.trimToEmpty(statusMap.get(item.getStatus() + "")));
                map.put("agentName", item.getAgentName());
                map.put("agentNo", item.getAgentNo());
                map.put("oneAgentName", item.getOneAgentName());
                map.put("oneAgentNo", item.getOneAgentNo());
                map.put("billingStatus", StringUtils.trimToEmpty(billingStatusMap.get(item.getBillingStatus() + "")));
                map.put("billingTime", formatData(item.getBillingTime()));
                map.put("billingMsg", item.getBillingMsg());
                map.put("isExclusion",item.getIsExclusion()==null? "" : item.getIsExclusion().equals(0)?"互斥":"不互斥");

                String liquidationStatus = item.getLiquidationStatus();
                String accountCheckStatus = item.getAccountCheckStatus();
                for (Map<String, String> map2 : checkStatus) {
                    if (liquidationStatus != null) {
                        if (map2.get("sys_value").equals(liquidationStatus)) {
                            map.put("liquidationStatus", map2.get("sys_name") == null ? "" : map2.get("sys_name"));
                        }
                    }
                    if (accountCheckStatus != null) {
                        if (map2.get("sys_value").equals(accountCheckStatus)) {
                            map.put("accountCheckStatus", map2.get("sys_name") == null ? "" : map2.get("sys_name"));
                        }
                    }
                }
                map.put("liquidationTime",item.getLiquidationTime()==null?"":sdfDate.format(item.getLiquidationTime()));
                map.put("accountCheckTime",item.getAccountCheckTime()==null?"":sdfDate.format(item.getAccountCheckTime()));
                map.put("liquidationOperator", item.getLiquidationOperator());
                map.put("accountCheckOperator", item.getAccountCheckOperator());
                data.add(map);
            }
            ListDataExcelExport export = new ListDataExcelExport();
            String[] cols = new String[]{"id","activeOrder","activeTime","activityCode","isExclusion","activityTypeNo",
                    "activityTypeName","merchantName","merchantNo","teamName","teamEntryName","hardId",
                    "recommendedSource","repeatRegister","enterTime","acqEnname","transTotal","acqMerchantFee",
                    "cumulateTransAmount","minOverdueTime","overdueTime","cumulateAmountMinus","cumulateAmountAdd",
                    "cashBackAmount","emptyAmount","fullAmount",
                    "status","isStandard","standardTime","minusAmountTime","addAmountTime",
                    "agentName","agentNo","oneAgentName",
                    "oneAgentNo","liquidationStatus","liquidationOperator","liquidationTime","accountCheckStatus",
                    "accountCheckOperator","accountCheckTime","billingStatus","billingTime","billingMsg"};
            String[] colsName = new String[]{"序号","激活订单号","激活时间","活动类型","互斥不补贴","欢乐返子类型编号",
                    "欢乐返子类型","商户名称", "商户编号","所属组织","所属子组织","硬件产品ID",
                    "推广来源","是否重复注册","进件时间","收单机构","交易金额","上游手续费",
                    "累计交易金额","交易奖励截止累计日期","交易扣款截止累计日期","累计交易（扣）","累计交易（奖）",
                    "返现金额","未满扣N元","满奖M元",
                    "活动状态","奖励是否达标","奖励达标时间","扣款时间","奖励时间",
                    "所属代理商名称","所属代理商编号","一级代理商名称",
                    "一级代理商编号", "清算核算状态","清算核算操作人","清算操作时间","账务核算状态",
                    "财务核算操作人","账务操作时间","入账状态","入账时间","入账信息"};

            OutputStream ouputStream = null;
            try {
                ouputStream = response.getOutputStream();
                export.export(cols, colsName, data, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(ouputStream!=null){
                    ouputStream.close();
                }
            }
        }
    }
    private String formatData(Date date){
        if (date == null){
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 欢乐返清算核算
     */
    @Override
    public Map<String, Object> updateLiquidation(ActivityDetail ad,Page<ActivityDetail> page,String liquidationStatus) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = principal.getId();
        Map<String, Object> msg = new HashMap<String, Object>();
        List<ActivityDetail> list=null;
        if("1".equals(ad.getBatchOrOne())){//未选中,直接点行按钮
            list=activityDetailDao.selectHappyBackDetailAll(ad);
        }else if("2".equals(ad.getBatchOrOne())){
            if(ad.getCountAll()||ad.getPageAll()){
                if(ad.getPageAll()){//当前页
                    activityDetailDao.selectHappyBackDetail(page,ad);
                    list=page.getResult();
                }
                if(ad.getCountAll()){//全部数据
                    list=activityDetailDao.selectHappyBackDetailAll(ad);
                }
            }else{//选中数据
                list=activityDetailDao.selectHappyBackDetailAll(ad);
            }
        }
        if(list!=null&&list.size()>0){
            int numTrue = 0;
            int numFalse = 0;
            int numExist=0;
            for(int i=0;i<list.size();i++){
                ActivityDetail item=list.get(i);
                if((!"1".equals(item.getLiquidationStatus()))&& item.getStatus()==2){
                    if("1".equals(liquidationStatus) && "008".equals(item.getActivityCode())){//同意
                        try {
                            activityDetailDao.updateAgreeLiquidationStatus(item.getId(),liquidationStatus,userId);
                            List<ActivityDetailBackstage> actBackList=activityDetailBackstageService.getActivityDetailBackstage(item.getId(),"3");
                            if(actBackList!=null&&actBackList.size()>0){//已添加任务了,不重复添加
                                numExist++;
                            }else{//添加任务
                                ActivityDetailBackstage actBack=new ActivityDetailBackstage();
                                actBack.setActId(item.getId());
                                actBack.setActState("3");
                                actBack.setBatchNo(RandomNumber.mumberRandom("ACT008",6,2));
                                actBack.setUserId(userId);
                                int num=activityDetailBackstageService.insertActivityDetailBackstage(actBack);
                                if(num>0){
                                    numTrue++;
                                }else{
                                    numFalse++;
                                }
                            }
                        }catch (Exception e) {
                            log.error("清算核算异常", e);
                            numFalse++;
                        }

                    }else{//不同意
                        int count = activityDetailDao.updatLiquidationStatus(item.getId(), liquidationStatus, userId);
                        if (count > 0) {
                            numTrue++;
                        } else {
                            numFalse++;
                        }
                    }
                }
            }
            if("1".equals(liquidationStatus)){
                String showMsg="清算核算请求已提交,请稍后查询清算核算状态!";
                if(numTrue>0){
                    showMsg=showMsg+"已成功提交:"+numTrue+"条;";
                }
                if(numFalse>0){
                    showMsg=showMsg+"提交失败:"+numFalse+"条;";
                }
                if(numExist>0){
                    showMsg=showMsg+"有"+numExist+"条已提交过;";
                }
                msg.put("msg",showMsg );
                msg.put("status", true);
                return msg;
            }else{
                String showMsg="核清算核算成功!";
                if(numTrue>0){
                    showMsg=showMsg+"已成功清算核算:"+numTrue+"条;";
                }
                if(numFalse>0){
                    showMsg=showMsg+"清算核算失败:"+numFalse+"条;";
                }
                msg.put("msg",showMsg );
                msg.put("status", true);
                return msg;
            }
        }else{
            msg.put("msg","没有符合清算核算的数据");
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 欢乐返财务核算
     */
    @Override
    public Map<String, Object> updateAccountCheck(ActivityDetail ad,Page<ActivityDetail> page,String accountCheckStatus) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = principal.getId();
        Map<String, Object> msg = new HashMap<String, Object>();
        List<ActivityDetail> list=null;
        if("1".equals(ad.getBatchOrOne())){//未选中,直接点行按钮
            list=activityDetailDao.selectHappyBackDetailAll(ad);
        }else if("2".equals(ad.getBatchOrOne())){
            if(ad.getCountAll()||ad.getPageAll()){
                if(ad.getPageAll()){//当前页
                    activityDetailDao.selectHappyBackDetail(page,ad);
                    list=page.getResult();
                }
                if(ad.getCountAll()){//全部数据
                    list=activityDetailDao.selectHappyBackDetailAll(ad);
                }
            }else{//选中数据
                list=activityDetailDao.selectHappyBackDetailAll(ad);
            }
        }
        if(list!=null&&list.size()>0){
            int numTrue = 0;
            int numFalse = 0;
            int numExist=0;
            for(int i=0;i<list.size();i++){
                ActivityDetail item=list.get(i);
                if("1".equals(item.getLiquidationStatus())&& item.getStatus()==2
                        &&"009".equals(item.getActivityCode())&&(!"1".equals(item.getAccountCheckStatus()))){
                    if("1".equals(accountCheckStatus)){//同意
                        try {
                            activityDetailDao.updateAgreeAccountCheckStatus(item.getId(), accountCheckStatus, userId);
                            List<ActivityDetailBackstage> actBackList=activityDetailBackstageService.getActivityDetailBackstage(item.getId(),"2");
                            if(actBackList!=null&&actBackList.size()>0){//已添加任务了,不重复添加
                                numExist++;
                            }else{//添加任务
                                ActivityDetailBackstage actBack=new ActivityDetailBackstage();
                                actBack.setActId(item.getId());
                                actBack.setActState("2");
                                actBack.setBatchNo(RandomNumber.mumberRandom("ACT009",6,2));
                                actBack.setUserId(userId);
                                int num=activityDetailBackstageService.insertActivityDetailBackstage(actBack);
                                if(num>0){
                                    numTrue++;
                                }else{
                                    numFalse++;
                                }
                            }
                        }catch (Exception e) {
                            log.error("财务核算异常", e);
                            numFalse++;
                        }

                    }else{//不同意
                        int count =activityDetailDao.updateAccountCheckStatus(item.getId(), accountCheckStatus, userId);
                        if (count > 0) {
                            numTrue++;
                        } else {
                            numFalse++;
                        }
                    }
                }
            }
            if("1".equals(accountCheckStatus)){
                String showMsg="财务核算请求已提交,请稍后查询财务核算状态!";
                if(numTrue>0){
                    showMsg=showMsg+"已成功提交:"+numTrue+"条;";
                }
                if(numFalse>0){
                    showMsg=showMsg+"提交失败:"+numFalse+"条;";
                }
                if(numExist>0){
                    showMsg=showMsg+"有"+numExist+"条已提交过;";
                }
                msg.put("msg",showMsg );
                msg.put("status", true);
                return msg;
            }else{
                String showMsg="财务核算算成功!";
                if(numTrue>0){
                    showMsg=showMsg+"已成功财务核算:"+numTrue+"条;";
                }
                if(numFalse>0){
                    showMsg=showMsg+"财务核算失败:"+numFalse+"条;";
                }
                msg.put("msg",showMsg );
                msg.put("status", true);
                return msg;
            }
        }else{
            msg.put("msg","没有符合财务核算的数据");
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 批量奖励入账
     * @return
     */
    @Override
    public Map<String, Object> rewardIsBooked(ActivityDetail ad,Page<ActivityDetail> page) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> msg = new HashMap<String, Object>();
        List<ActivityDetail> list=null;
        int sta=0;
        if("1".equals(ad.getBatchOrOne())){//未选中,直接点行按钮
            list=activityDetailDao.selectHappyBackDetailAll(ad);
        }else if("2".equals(ad.getBatchOrOne())){
            if(ad.getCountAll()||ad.getPageAll()){
                if(ad.getPageAll()){//当前页
                    activityDetailDao.selectHappyBackDetail(page,ad);
                    list=page.getResult();
                }
                if(ad.getCountAll()){//全部数据
                    list=activityDetailDao.selectHappyBackDetailAll(ad);
                    sta=1;
                }
            }else{//选中数据
                list=activityDetailDao.selectHappyBackDetailAll(ad);
            }
        }
        if(list!=null&&list.size()>0){
            if(sta==0){
                int numTrue = 0;
                int numFalse = 0;
                for(int i=0;i<list.size();i++){
                    ActivityDetail item=list.get(i);
                    if("1".equals(item.getIsStandard())&& (item.getStatus()==6||item.getStatus()==7||item.getStatus()==8)
                            && item.getAddAmountTime() == null && BigDecimal.ZERO.compareTo(item.getFullAmount())!=0) {
                        try {
                            ActivityDetail resultAd=activityDetailDao.getActivityDetailById(item.getId());
                            List<CashBackDetail> cashBackDetailList=activityDetailDao.getCashBackDetailById(resultAd.getId(),2);
                            //查询预调账
                            log.info("调用账户预调账接口的id----"+ item.getId());
                            String ACCOUNT_SERVICE_URL_VALUE = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
                            String findAgentPreUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/findAgentPreAdjustBalance.do";
                            String adjustBalanceMsg = ClientInterface.findAgentPreAdjustBalance(resultAd.getOneAgentNo(), findAgentPreUrl);
                            JSONObject adjustJson = JSONObject.parseObject(adjustBalanceMsg);
                            //执行入账
                            String msgCode="";
                            boolean status=false;
                            if(cashBackDetailList==null||cashBackDetailList.size()==0){
                                if(resultAd.getAgentType()!=null&&"11".equals(resultAd.getAgentType())){
                                    //超级盟主满奖
                                    String url = ACCOUNT_SERVICE_URL_VALUE+"/peragentController/peragentAccount.do";
                                    String returnMsg = ClientInterface.peragentAccountDemo(url,resultAd);
                                    Map<String, Object> result = JSON.parseObject(returnMsg);
                                    status = ((JSONObject) result).getBooleanValue("status");
                                }else {
                                    String returnMsg = ClientInterface.happyBackDaYuRecordAccount(resultAd);
                                    Map<String, Object> result = JSON.parseObject(returnMsg);
                                    msgCode = result.get("msgCode").toString();
                                }
                                if("000000".equals(msgCode)||status){
                                    //如果入账成功后有需要调账金额去调账
                                    /*if (adjustJson.getBooleanValue("status")) {
                                        BigDecimal freeAmount = adjustJson.getBigDecimal("freeAmount");
                                        BigDecimal preAdjustAmount = adjustJson.getBigDecimal("preAdjustAmount");
                                        if (freeAmount.compareTo(BigDecimal.ZERO) != 0 || preAdjustAmount.compareTo(BigDecimal.ZERO) != 0){
                                            String agentPreAdjustUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/agentActPreAdjustment.do";
                                            ClientInterface.agentPreAdjustBalance(resultAd.getOneAgentNo(), resultAd.getOneAgentName(), preAdjustAmount, "1", agentPreAdjustUrl);
                                        }
                                    }*/
                                    //更新数据库值
                                    ActivityDetail newAd=new ActivityDetail();
                                    newAd.setId(resultAd.getId());
                                    newAd.setStatus(9);
                                    activityDetailDao.updateRewardIsBooked(newAd);
                                    numTrue++;
                                }else{
                                    log.info(item.getId() + "奖励入账失败");
                                    numFalse++;
                                }
                            }else{
                                BigDecimal oldCashBackAmount=BigDecimal.ZERO;
                                for (int j=0;j<cashBackDetailList.size();j++){
                                    BigDecimal cashBackAmount=cashBackDetailList.get(j).getCashBackAmount();
                                    if(StringUtil.isBlank(cashBackAmount)){
                                        log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(j).getAgentNo()+",不满扣开金额为空不入账跳过!");
                                        break;
                                    }
                                    //'奖励入账开关 1-打开, 0-关闭' 不入账，可以直接跳过
                                    cashBackDetailList.get(j).setAgentCashBackSwitch(cashBackDetailList.get(j).getFullPrizeSwitch());
                                    if("0".equals(cashBackDetailList.get(j).getFullPrizeSwitch())){
                                        cashBackDetailList.get(j).setRemark("满奖开关关闭，不执行满奖");
                                        activityDetailDao.updateCashBackDetail(cashBackDetailList.get(j));
                                        log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(j).getAgentNo()+",满奖开关关闭，不执行满奖!");
                                        break;
                                    }
                                    if(cashBackAmount.compareTo(BigDecimal.ZERO)!=1){
                                        status=true;
                                        //返现金额为0 改为以入账
                                        cashBackDetailList.get(j).setEntryStatus("1");
                                        activityDetailDao.updateCashBackDetail(cashBackDetailList.get(j));
                                        log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(j).getAgentNo()+",满奖金额为0 改为以入账!");
                                        continue;
                                    }
                                    if(cashBackDetailList.get(j).getAgentLevel().equals("1")) {
                                        oldCashBackAmount = cashBackAmount;
                                    }
                                    if(cashBackAmount.compareTo(oldCashBackAmount)==1){
                                        cashBackDetailList.get(j).setRemark("上级倒挂,不需要满奖");
                                        activityDetailDao.updateCashBackDetail(cashBackDetailList.get(j));
                                        log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(j).getAgentNo()+",上级倒挂入账失败!");
                                        break;
                                    }else{
                                        oldCashBackAmount=cashBackAmount;
                                    }
                                    //查询预调账
                                    adjustBalanceMsg = ClientInterface.findAgentPreAdjustBalance(cashBackDetailList.get(j).getAgentNo(), findAgentPreUrl);
                                    adjustJson = JSONObject.parseObject(adjustBalanceMsg);
                                    BigDecimal freeAmount = adjustJson.getBigDecimal("freeAmount");
                                    BigDecimal preAdjustAmount = adjustJson.getBigDecimal("preAdjustAmount");
                                    //取差值
                                    if(cashBackDetailList.get(j).getAgentLevel().equals("1")){
                                        if((j+1)<cashBackDetailList.size()&&freeAmount.compareTo(BigDecimal.ZERO) == 0 && preAdjustAmount.compareTo(BigDecimal.ZERO) == 0){
                                            BigDecimal cashBackAmount2=cashBackDetailList.get(j+1).getCashBackAmount();
                                            cashBackAmount2=StringUtil.isBlank(cashBackAmount2)?BigDecimal.ZERO:cashBackAmount2;
                                            if((cashBackAmount.subtract(cashBackAmount2)).compareTo(BigDecimal.ZERO)!=-1&&"1".equals(cashBackDetailList.get(j+1).getFullPrizeSwitch())){
                                                cashBackDetailList.get(j).setCashBackAmount((cashBackAmount.subtract(cashBackAmount2)));
                                            }
                                        }
                                    }else{
                                        if((j+1)<cashBackDetailList.size()){
                                            BigDecimal cashBackAmount2=cashBackDetailList.get(j+1).getCashBackAmount();
                                            cashBackAmount2=StringUtil.isBlank(cashBackAmount2)?BigDecimal.ZERO:cashBackAmount2;
                                            if((cashBackAmount.subtract(cashBackAmount2)).compareTo(BigDecimal.ZERO)!=-1&&"1".equals(cashBackDetailList.get(j+1).getFullPrizeSwitch())){
                                                cashBackDetailList.get(j).setCashBackAmount((cashBackAmount.subtract(cashBackAmount2)));
                                            }
                                        }
                                    }
                                    //执行入账
                                    String returnMsg = ClientInterface.happyBackDaYuRecordAccount2(ACCOUNT_SERVICE_URL_VALUE,cashBackDetailList.get(j),resultAd.getMerchantNo());
                                    Map<String, Object> result = JSON.parseObject(returnMsg);
                                    msgCode = result.get("msgCode").toString();
                                    if("000000".equals(msgCode)){
                                        status=true;
                                        cashBackDetailList.get(j).setEntryStatus("1");
                                        activityDetailDao.updateCashBackDetail(cashBackDetailList.get(j));
                                        //如果入账成功后有需要调账金额去调账
                                        if (adjustJson.getBooleanValue("status")) {
                                            if (freeAmount.compareTo(BigDecimal.ZERO) != 0 || preAdjustAmount.compareTo(BigDecimal.ZERO) != 0){
                                                //String agentPreAdjustUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/agentActPreAdjustment.do";
                                                //ClientInterface.agentPreAdjustBalance(cashBackDetailList.get(j).getAgentNo(), cashBackDetailList.get(j).getAgentName(), preAdjustAmount, cashBackDetailList.get(j).getAgentLevel(), agentPreAdjustUrl);
                                                if(cashBackDetailList.get(j).getAgentLevel().equals("1")){
                                                    cashBackDetailList.get(j).setRemark("预冻结或者预调帐金额不为0，全部入账当前级别！");
                                                    cashBackDetailList.get(j).setEntryStatus("1");
                                                    activityDetailDao.updateCashBackDetail(cashBackDetailList.get(j));
                                                    break;
                                                }else{
                                                    cashBackDetailList.get(j).setEntryStatus("1");
                                                    activityDetailDao.updateCashBackDetail(cashBackDetailList.get(j));
                                                }
                                            }
                                        }
                                    }else{
                                        cashBackDetailList.get(j).setRemark(result.get("msg").toString());
                                        activityDetailDao.updateCashBackDetail(cashBackDetailList.get(j));
                                        log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(j).getAgentNo()+",满奖失败!");
                                    }
                                }
                                if(status){
                                    //更新数据库值
                                    ActivityDetail newAd=new ActivityDetail();
                                    newAd.setId(resultAd.getId());
                                    newAd.setStatus(9);
                                    activityDetailDao.updateRewardIsBooked(newAd);
                                    numTrue++;
                                }else{
                                    log.info(item.getId() + "奖励入账失败");
                                    numFalse++;
                                }
                            }
                        }catch (Exception e) {
                            log.error("奖励入账异常", e);
                            numFalse++;
                        }
                    }
                }
                String showMsg="奖励入账操作完成!";
                if(numTrue>0){
                    showMsg=showMsg+"已成功奖励入账:"+numTrue+"条;";
                }
                if(numFalse>0){
                    showMsg=showMsg+"奖励入账失败:"+numFalse+"条;";
                }
                msg.put("msg",showMsg);
                msg.put("status", true);
                return msg;
            }else if(sta==1){
                //延时机制
                int numTrue = 0;
                int numFalse = 0;
                int numExist=0;
                for(int i=0;i<list.size();i++){
                    ActivityDetail item=list.get(i);
                    if("1".equals(item.getIsStandard())&& (item.getStatus()==6||item.getStatus()==7||item.getStatus()==8)
                            && item.getAddAmountTime() == null && BigDecimal.ZERO.compareTo(item.getFullAmount())!=0) {
                        List<ActivityDetailBackstage> actBackList=activityDetailBackstageService.getActivityDetailBackstage(item.getId(),"4");
                        if(actBackList!=null&&actBackList.size()>0){//已添加任务了,不重复添加
                            numExist++;
                        }else{//添加任务
                            ActivityDetailBackstage actBack=new ActivityDetailBackstage();
                            actBack.setActId(item.getId());
                            actBack.setActState("4");
                            actBack.setBatchNo(RandomNumber.mumberRandom("ACT009",6,2));
                            actBack.setUserId(principal.getId());
                            int num=activityDetailBackstageService.insertActivityDetailBackstage(actBack);
                            if(num>0){
                                numTrue++;
                            }else{
                                numFalse++;
                            }
                        }
                    }
                }
                String showMsg="奖励入账请求已提交,请稍后查询奖励入账状态!";
                if(numTrue>0){
                    showMsg=showMsg+"已成功提交:"+numTrue+"条;";
                }
                if(numFalse>0){
                    showMsg=showMsg+"提交失败:"+numFalse+"条;";
                }
                if(numExist>0){
                    showMsg=showMsg+"有"+numExist+"条已提交过;";
                }
                msg.put("msg",showMsg );
                msg.put("status", true);
                return msg;
            }
        }else{
            msg.put("msg","没有符合奖励入账的数据");
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 奖励入账
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> oneRewardIsBooked(Integer id) {
        Map<String, Object> msg = new HashMap<String, Object>();
        ActivityDetail resultAd=activityDetailDao.getActivityDetailById(id);
        //状态为已返代理商 6  ,已达标，奖励时间为空
        if ((resultAd.getStatus() == 6||resultAd.getStatus() == 7||resultAd.getStatus() == 8) && "1".equals(resultAd.getIsStandard()) && resultAd.getAddAmountTime() == null) {
            if (BigDecimal.ZERO.equals(resultAd.getFullAmount())) {
                log.info("满奖M元的值为0，不需要奖励的id----"+ resultAd.getId());
                msg.put("status", false);
                msg.put("msg", "满奖M元的值为0，不需要奖励");
                return msg;
            } else {
                try{
                    List<CashBackDetail> cashBackDetailList=activityDetailDao.getCashBackDetailById(resultAd.getId(),2);
                    //查询预调账
                    String ACCOUNT_SERVICE_URL_VALUE = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
                    String findAgentPreUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/findAgentPreAdjustBalance.do";
                    String adjustBalanceMsg = ClientInterface.findAgentPreAdjustBalance(resultAd.getOneAgentNo(), findAgentPreUrl);
                    JSONObject adjustJson = JSONObject.parseObject(adjustBalanceMsg);
                    //执行入账
                    String msgCode="";
                    boolean status=false;
                    if(cashBackDetailList==null||cashBackDetailList.size()==0){
                        if(resultAd.getAgentType()!=null&&"11".equals(resultAd.getAgentType())){
                            //超级盟主满奖
                            String url = ACCOUNT_SERVICE_URL_VALUE+"/peragentController/peragentAccount.do";
                            String returnMsg = ClientInterface.peragentAccountDemo(url,resultAd);
                            Map<String, Object> result = JSON.parseObject(returnMsg);
                            status = ((JSONObject) result).getBooleanValue("status");
                        }else {
                            String returnMsg = ClientInterface.happyBackDaYuRecordAccount(resultAd);
                            Map<String, Object> result = JSON.parseObject(returnMsg);
                            msgCode = result.get("msgCode").toString();
                        }
                        if("000000".equals(msgCode)||status){
                            //如果入账成功后有需要调账金额去调账
                            /*if (adjustJson.getBooleanValue("status")) {
                                BigDecimal freeAmount = adjustJson.getBigDecimal("freeAmount");
                                BigDecimal preAdjustAmount = adjustJson.getBigDecimal("preAdjustAmount");
                                if (freeAmount.compareTo(BigDecimal.ZERO) != 0 || preAdjustAmount.compareTo(BigDecimal.ZERO) != 0){
                                    String agentPreAdjustUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/agentActPreAdjustment.do";
                                    ClientInterface.agentPreAdjustBalance(resultAd.getOneAgentNo(), resultAd.getOneAgentName(), preAdjustAmount, "1", agentPreAdjustUrl);
                                }
                            }*/
                            //更新数据库值
                            ActivityDetail newAd=new ActivityDetail();
                            newAd.setId(resultAd.getId());
                            newAd.setStatus(9);
                            activityDetailDao.updateRewardIsBooked(newAd);
                            msg.put("status", true);
                            msg.put("msg", "奖励入账成功!");
                            return msg;
                        }
                    }else{
                        BigDecimal oldCashBackAmount=BigDecimal.ZERO;
                        for (int i=0;i<cashBackDetailList.size();i++){
                            BigDecimal cashBackAmount=cashBackDetailList.get(i).getCashBackAmount();
                            if(StringUtil.isBlank(cashBackAmount)){
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",不满扣开金额为空不入账跳过!");
                                break;
                            }
                            //'奖励入账开关 1-打开, 0-关闭' 不入账，可以直接跳过
                            cashBackDetailList.get(i).setAgentCashBackSwitch(cashBackDetailList.get(i).getFullPrizeSwitch());
                            if("0".equals(cashBackDetailList.get(i).getFullPrizeSwitch())){
                                cashBackDetailList.get(i).setRemark("满奖开关关闭，不执行满奖");
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",满奖开关关闭，不执行满奖!");
                                break;
                            }
                            if(cashBackAmount.compareTo(BigDecimal.ZERO)!=1){
                                status=true;
                                //返现金额为0 改为以入账
                                cashBackDetailList.get(i).setEntryStatus("1");
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",满奖金额为0 改为以入账!");
                                continue;
                            }
                            if(cashBackDetailList.get(i).getAgentLevel().equals("1")) {
                                oldCashBackAmount = cashBackAmount;
                            }
                            if(cashBackAmount.compareTo(oldCashBackAmount)==1){
                                cashBackDetailList.get(i).setRemark("上级倒挂,不需要满奖");
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",上级倒挂入账失败!");
                                break;
                            }else{
                                oldCashBackAmount=cashBackAmount;
                            }
                            //查询预调账
                            adjustBalanceMsg = ClientInterface.findAgentPreAdjustBalance(cashBackDetailList.get(i).getAgentNo(), findAgentPreUrl);
                            adjustJson = JSONObject.parseObject(adjustBalanceMsg);
                            BigDecimal freeAmount = adjustJson.getBigDecimal("freeAmount");
                            BigDecimal preAdjustAmount = adjustJson.getBigDecimal("preAdjustAmount");
                            //取差值
                            if(cashBackDetailList.get(i).getAgentLevel().equals("1")){
                                if((i+1)<cashBackDetailList.size()&&freeAmount.compareTo(BigDecimal.ZERO) == 0 && preAdjustAmount.compareTo(BigDecimal.ZERO) == 0){
                                    BigDecimal cashBackAmount2=cashBackDetailList.get(i+1).getCashBackAmount();
                                    cashBackAmount2=StringUtil.isBlank(cashBackAmount2)?BigDecimal.ZERO:cashBackAmount2;
                                    if((cashBackAmount.subtract(cashBackAmount2)).compareTo(BigDecimal.ZERO)!=-1&&"1".equals(cashBackDetailList.get(i+1).getFullPrizeSwitch())){
                                        cashBackDetailList.get(i).setCashBackAmount((cashBackAmount.subtract(cashBackAmount2)));
                                    }
                                }
                            }else{
                                if((i+1)<cashBackDetailList.size()){
                                    BigDecimal cashBackAmount2=cashBackDetailList.get(i+1).getCashBackAmount();
                                    cashBackAmount2=StringUtil.isBlank(cashBackAmount2)?BigDecimal.ZERO:cashBackAmount2;
                                    if((cashBackAmount.subtract(cashBackAmount2)).compareTo(BigDecimal.ZERO)!=-1&&"1".equals(cashBackDetailList.get(i+1).getFullPrizeSwitch())){
                                        cashBackDetailList.get(i).setCashBackAmount((cashBackAmount.subtract(cashBackAmount2)));
                                    }
                                }
                            }
                            //执行入账
                            String returnMsg = ClientInterface.happyBackDaYuRecordAccount2(ACCOUNT_SERVICE_URL_VALUE,cashBackDetailList.get(i),resultAd.getMerchantNo());
                            Map<String, Object> result = JSON.parseObject(returnMsg);
                            msgCode = result.get("msgCode").toString();
                            if("000000".equals(msgCode)){
                                status=true;
                                cashBackDetailList.get(i).setEntryStatus("1");
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                //如果入账成功后有需要调账金额去调账
                                if (adjustJson.getBooleanValue("status")) {
                                    if (freeAmount.compareTo(BigDecimal.ZERO) != 0 || preAdjustAmount.compareTo(BigDecimal.ZERO) != 0){
                                        //String agentPreAdjustUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/agentActPreAdjustment.do";
                                        //ClientInterface.agentPreAdjustBalance(cashBackDetailList.get(i).getAgentNo(), cashBackDetailList.get(i).getAgentName(), preAdjustAmount, cashBackDetailList.get(i).getAgentLevel(), agentPreAdjustUrl);
                                        if(cashBackDetailList.get(i).getAgentLevel().equals("1")){
                                            cashBackDetailList.get(i).setRemark("预冻结或者预调帐金额不为0，全部入账当前级别！");
                                            cashBackDetailList.get(i).setEntryStatus("1");
                                            activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                            break;
                                        }else{
                                            cashBackDetailList.get(i).setEntryStatus("1");
                                            activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                        }
                                    }
                                }
                            }else{
                                cashBackDetailList.get(i).setRemark(result.get("msg").toString());
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",满奖失败!");
                            }
                        }
                        if(status){
                            //更新数据库值
                            ActivityDetail newAd=new ActivityDetail();
                            newAd.setId(resultAd.getId());
                            newAd.setStatus(9);
                            activityDetailDao.updateRewardIsBooked(newAd);
                            msg.put("status", true);
                            msg.put("msg", "奖励入账成功!");
                            return msg;
                        }
                    }
                }catch (Exception e) {
                    log.error("奖励入账异常", e);
                    msg.put("status", false);
                    msg.put("msg", "奖励入账异常!");
                }
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "奖励入账失败,请选择满足奖励条件的商户!");
        }
        return msg;
    }

    /**
     * 欢乐返批量奖励入账
     */
    @Override
    public Map<String, Object> joyToAccount(ActivityDetail activityDetail,Page<ActivityDetail> page) {
        Map<String, Object> msg = new HashMap<String, Object>();
        List<ActivityDetail> list = null;
        List<HappyBackBilling> billing_list = new ArrayList<>();
        int cashBackAmountHaveCount = 0;
        int cashBackAmountNotCount = 0;
        String message = "";
        boolean status = false;
        if(activityDetail.getCountAll() || activityDetail.getPageAll()){
            if(activityDetail.getPageAll()){//当前页
                activityDetailDao.selectHappyBackDetail(page,activityDetail);
                list = page.getResult();
            }
            if(activityDetail.getCountAll()){//全部数据
                list = activityDetailDao.selectHappyBackDetailAll(activityDetail);
            }
        }else{//选中数据
            list = activityDetailDao.selectHappyBackDetailAll(activityDetail);
        }

        if(list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ActivityDetail ad = activityDetailDao.getActivityDetail(list.get(i).getId());
                //初始化所有返回信息都是未入账状态
                String billingMsg = "未入账";
                int billingStatus = 0;
                HappyBackBilling happybackbilling = new HappyBackBilling();
                happybackbilling.setBillingMsg("未入账");
                happybackbilling.setBillingStatusText("未入账");
                happybackbilling.setActiveOrder(ad.getActiveOrder());
                //已经入账的就不入了
                if(ad.getBillingStatus() == 1) {
                    billingMsg = "已经入过账了，无需重复";
                    billingStatus = 1;
                    happybackbilling.setBillingMsg("已经入过账了，无需重复");
                    happybackbilling.setBillingStatusText("已入账");
                    cashBackAmountNotCount++;
                    billing_list.add(happybackbilling);
                    //activityDetailDao.updateBillStatus(ad.getId(), billingStatus, billingMsg);
                    continue;
                }
                List<CashBackDetail> cashBackDetailList = activityDetailDao.getCashBackDetailById(ad.getId(),1);
                String allAgentValue = sysDictDao.getValueByKey("ALLAGENT_SERVICE_URL");
                String allAgentUrl = allAgentValue + "/activity/accActOrder";
                //超级盟主入账
                if ("3".equals(ad.getRecommendedSource())) {
                    String result = ClientInterface.allAgentAccActOrder(ad, allAgentUrl);
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.getInteger("status") == 200) {
                        happybackbilling.setBillingMsg("超级盟主已入账");
                        billingMsg = "超级盟主已入账";
                        billingStatus = 1;
                        happybackbilling.setBillingStatusText("已入账");
                        cashBackAmountHaveCount++;
                    } else {
                        happybackbilling.setBillingMsg("超级盟主入账异常");
                        billingMsg = "超级盟主入账异常";
                        happybackbilling.setBillingStatusText("未入账");
                        cashBackAmountNotCount++;
                    }
                } else {
                    //欢乐返入账
                    if (cashBackDetailList != null && cashBackDetailList.size() > 0) {
                        for (int j = 0; j < cashBackDetailList.size(); j++){
                            CashBackDetail c = cashBackDetailList.get(j);
                            if(c.getEntryStatus().equals("1")){
                                continue;
                            }
                            if (c.getAgentLevel().equals("1")) {
                                //返现总金额大于配置金额，则不入账
                                BigDecimal maxAmount = activityDetailDao.getMaxAmount(ad.getId());
                                if (c.getCashBackAmount().compareTo(maxAmount) == 1) {
                                    happybackbilling.setBillingMsg("返现总金额大于配置金额");
                                    billingMsg = "返现总金额大于配置金额";
                                    happybackbilling.setBillingStatusText("未入账");
                                    if(c.getAgentLevel().equals("1")){
                                        cashBackAmountNotCount++;
                                    }
                                    break;
                                }
                            }
                            //'返现开关 1-打开, 0-关闭' 如果代理商返现开关关闭或者返现金额为0，则不入账，可以直接跳过
                            if ("0".equals(c.getAgentCashBackSwitch())) {
                                c.setRemark("返现开关关闭，不执行返现");
                                c.setCashBackSwitch(c.getAgentCashBackSwitch());
                                activityDetailDao.updateCashBackDetail(c);
                                if(c.getAgentLevel().equals("1")){
                                    cashBackAmountNotCount++;
                                    happybackbilling.setBillingMsg("返现开关关闭，不执行返现");
                                    billingMsg = "返现开关关闭，不执行返现";
                                    happybackbilling.setBillingStatusText("未入账");
                                }
                                log.info("欢乐返提现id:" + c.getId() + ",代理商" + c.getAgentNo() + ",返现开关关闭，不执行返现!");
                                break;
                            }
                            //返现金额为0 改为已入账
                            if (c.getCashBackAmount().compareTo(BigDecimal.ZERO) == 0) {
                                c.setEntryStatus("1");
                                c.setRemark("返现金额为0");
                                c.setCashBackSwitch(c.getAgentCashBackSwitch());
                                activityDetailDao.updateCashBackDetail(c);
                                if(c.getAgentLevel().equals("1")){
                                    cashBackAmountHaveCount++;
                                }
                                log.info("欢乐返提现id:" + c.getId()  + ",代理商" + c.getAgentNo() + ",返现金额为0改为已入账!");
                                continue;
                            }
                            //定义倒挂标志位
                            int less_than_flag = 0;
                            //记住原始金额
                            BigDecimal rawAmount = c.getCashBackAmount();
                            //最后一级直接入账
                            if (j == cashBackDetailList.size()-1){
                                c.setCashBackAmount(c.getCashBackAmount());
                            }else{
                                BigDecimal father_amount = c.getCashBackAmount();
                                BigDecimal son_amount = cashBackDetailList.get(j+1).getCashBackAmount();
                                //出现倒挂,只入当前代理商的
                                if(father_amount.compareTo(son_amount) == -1){
                                    less_than_flag = 1;
                                    c.setCashBackAmount(father_amount);
                                }else{
                                    c.setCashBackAmount(father_amount.subtract(son_amount));
                                }
                            }
                            String ACCOUNT_SERVICE_URL_VALUE = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
                            String findAgentPreUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/findAgentPreAdjustBalance.do";
                            String adjustBalanceMsg = ClientInterface.findAgentPreAdjustBalance(c.getAgentNo(), findAgentPreUrl);
                            JSONObject adjustJson = JSONObject.parseObject(adjustBalanceMsg);
                            if (adjustJson.getBooleanValue("status")) {
                                BigDecimal freeAmount = adjustJson.getBigDecimal("freeAmount");
                                BigDecimal preAdjustAmount = adjustJson.getBigDecimal("preAdjustAmount");
                                String happyBackAccountingUrl = ACCOUNT_SERVICE_URL_VALUE + "/happyBackController/happyBackAccounting.do";
                                //有预调账和冻结金额直接入当前级别全部的
                                if (freeAmount.compareTo(BigDecimal.ZERO) != 0 || preAdjustAmount.compareTo(BigDecimal.ZERO) != 0){
                                    c.setCashBackAmount(rawAmount);
                                }
                                //如果下级返现开关关闭，则当前入全部
                                if(j != cashBackDetailList.size()-1 && "0".equals(cashBackDetailList.get(j+1).getAgentCashBackSwitch())){
                                    c.setCashBackAmount(rawAmount);
                                }
                                String accountMsg = ClientInterface.happyBackAccounting(c, happyBackAccountingUrl,ad.getMerchantNo());
                                JSONObject json = JSONObject.parseObject(accountMsg);
                                if (json.getBooleanValue("status")) {
                                    activityDetailDao.updateAgreeAccountCheckStatusById(ad.getId());
                                    transInfoDao.updateReturnAgent(ad.getActiveOrder(), 6);
                                    c.setRemark(json.getString("msg"));
                                    c.setEntryStatus("1");
                                    //只要1级入账成功就将该id标记为已入账
                                    if(c.getAgentLevel().equals("1")){
                                        billingStatus = 1;
                                        billingMsg = json.getString("msg");
                                        cashBackAmountHaveCount++;
                                        happybackbilling.setBillingMsg(json.getString("msg"));
                                        happybackbilling.setBillingStatusText("已入账");
                                    }
                                }else {
                                    c.setRemark(json.getString("msg"));
                                    billingMsg = json.getString("msg");
                                    happybackbilling.setBillingStatusText("未入账");
                                    happybackbilling.setBillingMsg(json.getString("msg"));
                                    if(c.getAgentLevel().equals("1")){
                                        cashBackAmountNotCount++;
                                    }
                                    log.info("欢乐返提现id:" + ad.getId() + ",账户接口返回欢乐返财务核算失败!");
                                }
                                //不等于当前直接入账，并调用调账接口
                                if (freeAmount.compareTo(BigDecimal.ZERO) != 0 || preAdjustAmount.compareTo(BigDecimal.ZERO) != 0 || less_than_flag == 1){
                                    String agentPreAdjustUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/agentActPreAdjustment.do";
                                    ClientInterface.agentPreAdjustBalance(c.getAgentNo(), c.getAgentName(), preAdjustAmount, c.getAgentLevel(), agentPreAdjustUrl);
                                    c.setEntryStatus("1");
                                    if (less_than_flag == 1){
                                        c.setRemark("当前级别倒挂，全部入账当前级别！");
                                    }else {
                                        c.setRemark("预冻结或者预调帐金额不为0，全部入账当前级别！");
                                    }
                                    c.setCashBackSwitch(c.getAgentCashBackSwitch());
                                    activityDetailDao.updateCashBackDetail(c);
                                    break;
                                }
                            }else{
                                c.setRemark("预调账冻结金额接口异常未入账");
                                if(c.getAgentLevel().equals("1")){
                                    happybackbilling.setBillingMsg("接口异常未入账");
                                    if(c.getAgentLevel().equals("1")){
                                        cashBackAmountNotCount++;
                                    }
                                }
                                log.info("欢乐返提现id:" + c.getId() + ",账户活动补贴账户查询预调账冻结金额接口失败!");
                            }
                            c.setCashBackSwitch(c.getAgentCashBackSwitch());
                            activityDetailDao.updateCashBackDetail(c);
                        }
                    }else{
                        happybackbilling.setBillingStatusText("未入账");
                        happybackbilling.setBillingMsg("入账失败，没有返现明细");
                        billingMsg = "入账失败，没有返现明细";
                        cashBackAmountNotCount++;
                    }
                }
                billing_list.add(happybackbilling);
                //入账成功需要改变活动状态
                if(billingStatus == 1){
                    activityDetailDao.updateBillStatusOk(ad.getId(), billingStatus, billingMsg);
                }else{
                    activityDetailDao.updateBillStatus(ad.getId(), billingStatus, billingMsg);
                }
            }
            status = true;
            message = "已执行欢乐返入账操作";
        }else{
            message = "没有符合奖励入账的数据";
        }
        msg.put("msg", message);
        msg.put("status", status);
        msg.put("rewardResultList", billing_list);
        msg.put("cashBackAmountHaveCount", cashBackAmountHaveCount);
        msg.put("cashBackAmountNotCount", cashBackAmountNotCount);
        return msg;
    }

    public String getCellValue(Cell cell){
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getStringCellValue();
        }
        return null;
    }

    /**
     * 新欢乐送商户奖励条件查询
     */
    @Override
    public List<HappySendNewOrder>  selectHappySendOrderDetail(Page<HappySendNewOrder> page, HappySendNewOrder happySendNewOrder) {
        return activityDetailDao.selectHappySendOrderDetail(page,happySendNewOrder);
    }

    /**
     * 新欢乐送条件汇总金额
     */
    @Override
    public Map<String, Object> selectHappySendOrderTotalAmount(HappySendNewOrder happySendNewOrder) {
        Map<String, Object> totalData=new HashMap<String, Object>();
        Map<String, Object> map1=activityDetailDao.selectHappySendOrderTotalAmount(happySendNewOrder);
        if(map1!=null){
            totalData.putAll(map1);
        }
        return totalData;
    }

    public void exportHappySendOrder(HappySendNewOrder ad, HttpServletResponse response) throws IOException {
        List<HappySendNewOrder> list=null;
        list=activityDetailDao.selectHappySendOrderDetailAll(ad);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "新欢乐送"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;
        if(list!=null&&list.size()>0){
            for(HappySendNewOrder item: list){
                map = new HashMap<>();
                map.put("id", String.valueOf(item.getId()));
                map.put("activeOrder", item.getActiveOrder());
                map.put("activeTime", item.getActiveTime()==null?"":sdfTime.format(item.getActiveTime()));
                map.put("targetAmount", item.getTargetAmount()+"");
                map.put("rewardAmount", item.getRewardAmount()+"");
                map.put("activityTargetStatus",  StringUtils.trimToEmpty(activityTargetStatusMap.get(item.getRewardAccountStatus()+"")));
                map.put("activityTargetTime", item.getActivityTargetTime()==null?"":sdfTime.format(item.getActivityTargetTime()));
                map.put("rewardEndTime", item.getRewardEndTime()==null?"":sdfTime.format(item.getRewardEndTime()));
                map.put("rewardAccountStatus", StringUtils.trimToEmpty(billingStatusMap.get(item.getRewardAccountStatus()+"")));
                map.put("rewardAccountTime", item.getRewardAccountTime()==null?"":sdfTime.format(item.getRewardAccountTime()));

                map.put("activityTypeNo", item.getActivityTypeNo());
                map.put("merchantNo", item.getMerchantNo());
                map.put("teamName", item.getTeamName());
                map.put("teamEntryName", item.getTeamEntryName());
                map.put("hardId", item.getHardId()==null?"": item.getHardId().toString());

                map.put("agentName", item.getAgentName());
                map.put("agentNo", item.getAgentNo());
                map.put("oneAgentName", item.getOneAgentName());
                map.put("oneAgentNo", item.getOneAgentNo());
                data.add(map);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","activeOrder","activeTime","targetAmount","rewardAmount","activityTargetStatus",
                "activityTargetTime","rewardEndTime","rewardAccountStatus","rewardAccountTime",
                "activityTypeNo","merchantNo","teamName","teamEntryName","hardId",
                "agentName","agentNo","oneAgentName", "oneAgentNo"};
        String[] colsName = new String[]{"序号","激活订单号","激活日期","达标条件(元)","奖励金额(元)","达标状态",
                "达标日期","活动截止日期","奖励入账状态","入账日期",
                "欢乐返子类型编号", "商户编号", "所属组织", "所属子组织", "硬件产品ID",
                "所属代理商名称","所属代理商编号","一级代理商名称", "一级代理商编号"};

        OutputStream ouputStream = null;
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public NewHappyBackActivityResult newHappyBackCount(NewHappyBackActivityQo qo) {
        qo.init();
        Map<String, Object> totalMap = activityDetailDao.newHappyBackCount(qo);
        NewHappyBackActivityResult result = new NewHappyBackActivityResult();
        if(totalMap!=null){
            result.setTotalReward(totalMap.get("totalReward")+"");
            result.setTotalNoReward(totalMap.get("totalNoReward")+"");
        }

        return result;
    }

    @Override
    public NewHappyBackActivityResult newHappyBackQuery(NewHappyBackActivityQo qo) {
        qo.init();
        Page<NewHappyBackActivityVo> page = new Page<NewHappyBackActivityVo>(qo.getPageNo(), qo.getPageSize());
        List<NewHappyBackActivityVo> list = activityDetailDao.newHappyBackQuery(page,qo);
        NewHappyBackActivityResult result = new NewHappyBackActivityResult();
        result.setList(list);
        result.setTotalCount(page.getTotalCount()+0L);
        return result;
    }


    @Override
    public List<AgentAwardDetailVo> agentAwardDetail(Long id) {
        return activityDetailDao.agentAwardDetail(id);
    }


    @Override
    public List<XhlfActivityMerchantOrder> getXhlfMerOrderLists(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        return activityDetailDao.getXhlfMerOrderLists(idList);
    }

    @Override
    public List<XhlfActivityOrder> getXhlfAgentOrderLists(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        return activityDetailDao.getXhlfAgentOrderLists(idList);
    }
}












