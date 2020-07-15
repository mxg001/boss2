package cn.eeepay.framework.service.impl.risk;

import cn.eeepay.framework.dao.risk.SurveyDeductDetailDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.risk.DeductAddInfo;
import cn.eeepay.framework.model.risk.SurveyDeductDetail;
import cn.eeepay.framework.model.risk.SurveyOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.risk.SurveyDeductDetailService;
import cn.eeepay.framework.service.risk.SurveyOrderLogService;
import cn.eeepay.framework.service.risk.SurveyOrderService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/9/12/012.
 * @author  liuks
 * 调单扣款明细
 */
@Service("surveyDeductDetailService")
public class SurveyDeductDetailServiceImpl implements SurveyDeductDetailService {

    private static final Logger log = LoggerFactory.getLogger(SurveyDeductDetailServiceImpl.class);

    @Resource
    private SurveyDeductDetailDao surveyDeductDao;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private SurveyOrderService surveyOrderService;
    @Resource
    private SurveyOrderLogService surveyOrderLogService;


    @Override
    public int addDeductDetail(SurveyOrder order) {
        return surveyDeductDao.addDeductDetail(order);
    }

    @Override
    public List<SurveyDeductDetail> selectAllList(SurveyDeductDetail detail, Page<SurveyDeductDetail> page) {
        return surveyDeductDao.selectAllList(detail,page);
    }

    @Override
    public List<SurveyDeductDetail> importDetailSelect(SurveyDeductDetail detail) {
        return surveyDeductDao.importDetailSelect(detail);
    }

    @Override
    public void importDetail(List<SurveyDeductDetail> list, HttpServletResponse response,int sta) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName=null;
        if(sta==1){
            fileName = "调单扣款列表"+sdf.format(new Date())+".xlsx" ;
        }else{
            fileName = "调单扣款明细列表"+sdf.format(new Date())+".xlsx" ;
        }

        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo", null);
            maps.put("transOrderNo",null);
            maps.put("acqReferenceNo",null);
            maps.put("orderTypeCode",null);
            maps.put("merchantNo",null);
            maps.put("transAmount",null);

            maps.put("acqDeductAmount",null);
            maps.put("acqDeductTime",null);
            maps.put("acqDeductRemark",null);

            maps.put("merDeductAmount",null);
            maps.put("merDeductTime",null);
            maps.put("agentRemainDeductAmount",null);
            maps.put("merDeductRemark",null);

            maps.put("agentHaveDeductAmount",null);
            maps.put("agentDeductTime",null);
            maps.put("agentNeedDeductAmount",null);
            maps.put("agentDeductRemark",null);
            if(sta==1){
                maps.put("agentDeductDealStatus",null);
                maps.put("agentDeductDealRemark",null);
            }
            maps.put("acqIssueAmount",null);
            maps.put("acqIssueTime",null);
            maps.put("acqIssueRemark",null);

            maps.put("merIssueAmount",null);
            maps.put("merIssueTime",null);
            maps.put("agentRemainIssueAmount",null);
            maps.put("merIssueRemark",null);

            maps.put("agentHaveIssueAmount",null);
            maps.put("agentIssueTime",null);
            maps.put("agentNeedIssueAmount",null);
            maps.put("agentIssueRemark",null);
            if(sta==1){
                maps.put("agentIssueDealStatus",null);
                maps.put("agentIssueDealRemark",null);
            }else{
                maps.put("operator",null);
                maps.put("operateTime",null);
            }
            data.add(maps);
        }else{
            Map<String, String> orderTypeCodeMap=sysDictService.selectMapByKey("ORDER_TYPE_CODE");//调单类型

            Map<String, String> agentDeductDealStatusMap=new HashMap<String, String>();
            agentDeductDealStatusMap.put("0","未处理");
            agentDeductDealStatusMap.put("1","已处理");

            Map<String, String> agentIssueDealStatusMap=new HashMap<String, String>();
            agentIssueDealStatusMap.put("0","未处理");
            agentIssueDealStatusMap.put("1","已处理");

            for (SurveyDeductDetail or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?"":or.getOrderNo());
                maps.put("transOrderNo",or.getTransOrderNo()==null?"":or.getTransOrderNo());
                maps.put("acqReferenceNo",or.getAcqReferenceNo()==null?"":or.getAcqReferenceNo());
                maps.put("orderTypeCode",orderTypeCodeMap.get(or.getOrderTypeCode()==null?"":or.getOrderTypeCode()));
                maps.put("merchantNo",or.getMerchantNo()==null?"":or.getMerchantNo());
                maps.put("transAmount",or.getTransAmount()==null?"":or.getTransAmount().toString());

                maps.put("acqDeductAmount",or.getAcqDeductAmount()==null?"":or.getAcqDeductAmount().toString());
                maps.put("acqDeductTime",or.getAcqDeductTime()==null?"":sdf1.format(or.getAcqDeductTime()));
                maps.put("acqDeductRemark",or.getAcqDeductRemark()==null?"":or.getAcqDeductRemark());

                maps.put("merDeductAmount",or.getMerDeductAmount()==null?"":or.getMerDeductAmount().toString());
                maps.put("merDeductTime",or.getMerDeductTime()==null?"":sdf1.format(or.getMerDeductTime()));
                maps.put("agentRemainDeductAmount",or.getAgentRemainDeductAmount()==null?"":or.getAgentRemainDeductAmount().toString());
                maps.put("merDeductRemark",or.getMerDeductRemark()==null?"":or.getMerDeductRemark());

                maps.put("agentHaveDeductAmount",or.getAgentHaveDeductAmount()==null?"":or.getAgentHaveDeductAmount().toString());
                maps.put("agentDeductTime",or.getAgentDeductTime()==null?"":sdf1.format(or.getAgentDeductTime()));
                maps.put("agentNeedDeductAmount",or.getAgentNeedDeductAmount()==null?"":or.getAgentNeedDeductAmount().toString());
                maps.put("agentDeductRemark",or.getAgentDeductRemark()==null?"":or.getAgentDeductRemark());
                if(sta==1){
                    maps.put("agentDeductDealStatus",agentDeductDealStatusMap.get(or.getAgentDeductDealStatus()));
                    maps.put("agentDeductDealRemark",or.getAgentDeductDealRemark()==null?"":or.getAgentDeductDealRemark());
                }
                maps.put("acqIssueAmount",or.getAcqIssueAmount()==null?"":or.getAcqIssueAmount().toString());
                maps.put("acqIssueTime",or.getAcqIssueTime()==null?"":sdf1.format(or.getAcqIssueTime()));
                maps.put("acqIssueRemark",or.getAcqIssueRemark()==null?"":or.getAcqIssueRemark());

                maps.put("merIssueAmount",or.getMerIssueAmount()==null?"":or.getMerIssueAmount().toString());
                maps.put("merIssueTime",or.getMerIssueTime()==null?"":sdf1.format(or.getMerIssueTime()));
                maps.put("agentRemainIssueAmount",or.getAgentRemainIssueAmount()==null?"":or.getAgentRemainIssueAmount().toString());
                maps.put("merIssueRemark",or.getMerIssueRemark()==null?"":or.getMerIssueRemark());

                maps.put("agentHaveIssueAmount",or.getAgentHaveIssueAmount()==null?"":or.getAgentHaveIssueAmount().toString());
                maps.put("agentIssueTime",or.getMerIssueTime()==null?"":sdf1.format(or.getMerIssueTime()));
                maps.put("agentNeedIssueAmount",or.getAgentNeedIssueAmount()==null?"":or.getAgentNeedIssueAmount().toString());
                maps.put("agentIssueRemark",or.getMerIssueRemark()==null?"":or.getMerIssueRemark());
                if(sta==1){
                    maps.put("agentIssueDealStatus",agentIssueDealStatusMap.get(or.getAgentIssueDealStatus()));
                    maps.put("agentIssueDealRemark",or.getAgentIssueDealRemark()==null?"":or.getAgentIssueDealRemark());
                }else{
                    maps.put("operator",or.getOperator()==null?"":or.getOperator());
                    maps.put("operateTime",or.getOperateTime()==null?"":sdf1.format(or.getOperateTime()));
                }
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols=null;
        String[] colsName=null;
        if(sta==1){
            cols= new String[]{"orderNo","transOrderNo","acqReferenceNo","orderTypeCode","merchantNo","transAmount",
                    "acqDeductAmount","acqDeductTime","acqDeductRemark",
                    "merDeductAmount","merDeductTime","agentRemainDeductAmount","merDeductRemark",
                    "agentHaveDeductAmount","agentDeductTime","agentNeedDeductAmount","agentDeductRemark",
                    "agentDeductDealStatus","agentDeductDealRemark",
                    "acqIssueAmount","acqIssueTime","acqIssueRemark",
                    "merIssueAmount","merIssueTime","agentRemainIssueAmount","merIssueRemark",
                    "agentHaveIssueAmount","agentIssueTime","agentNeedIssueAmount","agentIssueRemark",
                    "agentIssueDealStatus","agentIssueDealRemark"
            };
            colsName = new String[]{"调单号","订单编号","系统参考号","调单类型","商户编号","交易金额",
                    "上游扣款金额","上游扣款时间","上游扣款备注",
                    "商户已扣款金额","商户已扣款时间","代理商待扣款金额","商户已扣款备注",
                    "代理商已扣款金额","代理商已扣款时间","代理商需扣款金额","代理商已扣款备注",
                    "扣款处理状态","财务扣款处理备注",
                    "上游下发金额","上游下发时间","上游下发备注",
                    "商户已下发金额","商户已下发时间","代理商待下发金额","商户已下发备注",
                    "代理商已下发金额","代理商已下发时间","代理商需下发金额","代理商已下发备注",
                    "下发处理状态","财务处理下发备注"
            };
        }else{
            cols= new String[]{"orderNo","transOrderNo","acqReferenceNo","orderTypeCode","merchantNo","transAmount",
                    "acqDeductAmount","acqDeductTime","acqDeductRemark",
                    "merDeductAmount","merDeductTime","agentRemainDeductAmount","merDeductRemark",
                    "agentHaveDeductAmount","agentDeductTime","agentNeedDeductAmount","agentDeductRemark",
                    "acqIssueAmount","acqIssueTime","acqIssueRemark",
                    "merIssueAmount","merIssueTime","agentRemainIssueAmount","merIssueRemark",
                    "agentHaveIssueAmount","agentIssueTime","agentNeedIssueAmount","agentIssueRemark",
                    "operator","operateTime"
            };
            colsName = new String[]{"调单号","订单编号","系统参考号","调单类型","商户编号","交易金额",
                    "上游扣款金额","上游扣款时间","上游扣款备注",
                    "商户已扣款金额","商户已扣款时间","代理商待扣款金额","商户已扣款备注",
                    "代理商已扣款金额","代理商已扣款时间","代理商需扣款金额","代理商已扣款备注",
                    "上游下发金额","上游下发时间","上游下发备注",
                    "商户已下发金额","商户已下发时间","代理商待下发金额","商户已下发备注",
                    "代理商已下发金额","代理商已下发时间","代理商需下发金额","代理商已下发备注",
                    "操作人","操作时间"
            };
        }

        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出调单扣款明细列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }


    @Override
    public List<SurveyDeductDetail> selectGroup(SurveyDeductDetail detail, Page<SurveyDeductDetail> page) {
        return surveyDeductDao.selectGroup(detail,page);
    }

    /**
     * 标注
     */
    @Override
    public int tagging(DeductAddInfo info, Map<String, Object> msg) {
        if(info!=null){
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            info.setOperator(principal.getUsername());
            if(checkState(info)){
                int num=0;
                if("6".equals(info.getOperatorType())||"7".equals(info.getOperatorType())){
                    num=surveyOrderService.updateAgentState(info);
                }else{
                    num= surveyDeductDao.saveDeductDetail(info);
                }
                if(num>0){
                    surveyOrderLogService.insertSurveyOrderLog(info.getOrderNo(),info.getLogSta(),info.getRemark());
                    msg.put("status", true);
                    msg.put("msg", info.getMsg()+"成功!");
                    return num;
                }
            }

        }
        msg.put("status", false);
        msg.put("msg", "操作失败!");
        return 0;
    }

    @Override
    public List<SurveyDeductDetail> importSelectGroup(SurveyDeductDetail detail) {
        return surveyDeductDao.importSelectGroup(detail);
    }

    private boolean checkState(DeductAddInfo info){
        boolean status=false;
        if(info.getStaAll().intValue()==1) {
            if(info.getSta().intValue()==0){
                info.setOperatorType("0");
                info.setLogSta("6");
                info.setMsg("标注上游扣款信息");
                status=true;
            }else{
                info.setOperatorType("1");
                info.setLogSta("7");
                info.setMsg("标注上游下发信息");
                status=true;
            }
        }else if(info.getStaAll().intValue()==2){
            if(info.getSta().intValue()==0){
                info.setOperatorType("2");
                info.setLogSta("8");
                info.setMsg("标注商户扣款信息");
                status=true;
            }else{
                info.setOperatorType("3");
                info.setLogSta("9");
                info.setMsg("标注商户下发信息");
                status=true;
            }
        }else if(info.getStaAll().intValue()==3){
            if(info.getSta().intValue()==0){
                info.setOperatorType("4");
                info.setLogSta("10");
                info.setMsg("标注代理商扣款信息");
                status=true;
            }else{
                info.setOperatorType("5");
                info.setLogSta("11");
                info.setMsg("标注代理商下发信息");
                status=true;
            }
        }else if(info.getStaAll().intValue()==4){
            if(info.getSta().intValue()==0){
                info.setOperatorType("6");
                info.setLogSta("12");
                info.setMsg("标注扣款处理状态");
                status=true;
            }else{
                info.setOperatorType("7");
                info.setLogSta("13");
                info.setMsg("标注下发处理状态");
                status=true;
            }
        }
        return status;
    }
}
