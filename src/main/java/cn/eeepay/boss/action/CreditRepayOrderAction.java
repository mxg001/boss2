package cn.eeepay.boss.action;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayOrder;
import cn.eeepay.framework.model.CreditRepayOrderDetail;
import cn.eeepay.framework.service.CreditRepayOrderDetailService;
import cn.eeepay.framework.service.CreditRepayOrderService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * 信用卡还款订单
 * @author liuks
 */
@Controller
@RequestMapping(value = "/creditRepayOrder")
public class CreditRepayOrderAction {
    private static final Logger log = LoggerFactory.getLogger(CreditRepayOrderAction.class);

    @Resource
    private CreditRepayOrderService creditRepayOrderService;
    @Resource
    private CreditRepayOrderDetailService creditRepayOrderDetailService;

    /**
     * 查询订单详情
     * @param batchNo 批次号
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectById/{batchNo}")
    @ResponseBody
    public Map<String,Object> selectById(@PathVariable("batchNo") String batchNo,@RequestParam("tallyOrderNo") String tallyOrderNo) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CreditRepayOrder order = creditRepayOrderService.selectById(batchNo,tallyOrderNo,1);
            List<CreditRepayOrderDetail> detailList=creditRepayOrderDetailService.selectDetailList(batchNo);
            msg.put("order",order);
            msg.put("detailList",detailList);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询订单详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询订单详情失败!");
        }
        return msg;
    }

    /**
     *查询敏感信息
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getDataProcessing/{batchNo}")
    @ResponseBody
    public Map<String,Object> getDataProcessing(@PathVariable("batchNo") String batchNo,@RequestParam("tallyOrderNo") String tallyOrderNo) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CreditRepayOrder info = creditRepayOrderService.selectById(batchNo,tallyOrderNo,0);
            msg.put("info",info);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询敏感信息失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询敏感信息失败!");
        }
        return msg;
    }

    /**
     * 查询信用卡还款订单列表
     * @param param
     * @param page
     * @return
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam.do")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<CreditRepayOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CreditRepayOrder order = JSONObject.parseObject(param, CreditRepayOrder.class);
            creditRepayOrderService.selectAllList(order, page);
            msg.put("page",page);
            CreditRepayOrder  orderAll=creditRepayOrderService.selectAllListSum(order);
            msg.put("orderAll",orderAll);
            msg.put("status",true);
        } catch (Exception e){
            log.error("查询信用卡还款订单列表失败!",e);
            msg.put("msg","查询信用卡还款订单列表失败!");
            msg.put("status",false);
        }
        return msg;
    }


    /**
     * 导出数据表
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/exportAllInfo")
    @ResponseBody
    public void exportAllInfo(@RequestParam("baseInfo") String param, HttpServletResponse response, HttpServletRequest request) throws Exception{
        CreditRepayOrder order = JSONObject.parseObject(param, CreditRepayOrder.class);
        List<CreditRepayOrder> list=creditRepayOrderService.importSelectAllList(order);
        export(list,response,request,true);
    }


    /**
     * 查询信用卡还款订单列表
     * @param param
     * @param page
     * @return
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectDetail.do")
    @ResponseBody
    public Map<String, Object> selectDetailByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<CreditRepayOrderDetail> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CreditRepayOrderDetail orderDetail = JSONObject.parseObject(param, CreditRepayOrderDetail.class);
            creditRepayOrderDetailService.selectDetailAllList(orderDetail,page);
            msg.put("page",page);
            BigDecimal planAmountTotal = creditRepayOrderDetailService.selectDetailAllListSum(orderDetail);
            msg.put("planAmountTotal", planAmountTotal);
            msg.put("status",true);
        } catch (Exception e){
            log.error("查询信用卡还款订单列表失败!",e);
            msg.put("msg","查询信用卡还款订单列表失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 导出还款订单处理流水
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/exportDetailAllInfo")
    @ResponseBody
    public void exportDetailAllInfo(@RequestParam("baseInfo") String param, HttpServletResponse response, HttpServletRequest request) throws Exception{
        CreditRepayOrderDetail orderDetail = JSONObject.parseObject(param, CreditRepayOrderDetail.class);

        List<CreditRepayOrderDetail> list=creditRepayOrderDetailService.importDetailAllList(orderDetail);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "还款订单处理流水"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("planNo",null);
            maps.put("planAmount",null);
            maps.put("accountNo",null);
            maps.put("batchNo",null);
            maps.put("merchantNo",null);
            maps.put("planType",null);
            maps.put("planStatus",null);
            maps.put("resMsg",null);
            maps.put("createTime",null);
            maps.put("bak1",null);
            data.add(maps);
        }else{
            for (CreditRepayOrderDetail or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("planNo",or.getPlanNo()==null?"":or.getPlanNo());
                maps.put("planAmount",or.getPlanAmount()==null?null:or.getPlanAmount().toString());
                maps.put("accountNo",or.getAccountNo()==null?"":or.getAccountNo());
                maps.put("batchNo",or.getBatchNo()==null?"":or.getBatchNo());
                maps.put("merchantNo",or.getMerchantNo()==null?"":or.getMerchantNo());
                if(or.getPlanType()!=null){
                    if("IN".equals(or.getPlanType())){
                        maps.put("planType","给用户还款");
                    }else if("OUT".equals(or.getPlanType())){
                        maps.put("planType","用户消费");
                    }else{
                        maps.put("planType","");
                    }
                }else{
                    maps.put("planType","");
                }
                maps.put("acqCode", or.getAcqCode());
                if(or.getPlanStatus()!=null){
                    if("0".equals(or.getPlanStatus())){
                        maps.put("planStatus","未执行");
                    }else if("1".equals(or.getPlanStatus())){
                        maps.put("planStatus","执行中");
                    }else if("2".equals(or.getPlanStatus())){
                        maps.put("planStatus","执行成功");
                    }else if("3".equals(or.getPlanStatus())){
                        maps.put("planStatus","执行失败");
                    }else{
                        maps.put("planStatus","");
                    }
                }else{
                    maps.put("planStatus","");
                }
                maps.put("resMsg",or.getResMsg()==null?"":or.getResMsg());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("bak1",or.getBak1()==null?"":or.getBak1());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"planNo","planAmount","accountNo","batchNo","merchantNo","planType","acqCode",
                "planStatus","resMsg","createTime","bak1"
        };

        String[] colsName = new String[]{"任务流水号","金额(元)","还款卡号","来源订单号","用户编号","类型","上游通道",
                "状态","错误信息","创建时间","备注"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出还款订单处理流水失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }


    /**
     * 查询异常信用卡还款订单列表
     * @param param
     * @param page
     * @return
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAbnormalByParam.do")
    @ResponseBody
    public Map<String, Object> selectAbnormalByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<CreditRepayOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CreditRepayOrder order = JSONObject.parseObject(param, CreditRepayOrder.class);
            order.setStatus("5");
            creditRepayOrderService.selectAllList(order, page);
            msg.put("page",page);
            CreditRepayOrder  orderAll=creditRepayOrderService.selectAllListSum(order);
            msg.put("orderAll",orderAll);
            msg.put("status",true);
        } catch (Exception e){
            log.error("查询异常信用卡还款订单查询失败!",e);
            msg.put("msg","查询异常信用卡还款订单查询失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 导出数据表
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/exportAbnormalAllInfo")
    @ResponseBody
    public void exportAbnormalAllInfo(@RequestParam("baseInfo") String param, HttpServletResponse response, HttpServletRequest request) throws Exception{
        CreditRepayOrder order = JSONObject.parseObject(param, CreditRepayOrder.class);
        order.setStatus("5");
        List<CreditRepayOrder> list=creditRepayOrderService.importSelectAllList(order);
        export(list,response,request,false);
    }

    /**
     * 模板导出
     * @param list
     * @param response
     * @param request
     * @throws Exception
     */
    private void export(List<CreditRepayOrder> list,HttpServletResponse response, HttpServletRequest request,boolean state) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName=null;
        if(state){
            fileName = "信用卡还款订单"+sdf.format(new Date())+".xlsx" ;
        }else{
            fileName = "还款异常订单查询"+sdf.format(new Date())+".xlsx" ;
        }
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("batchNo",null);
            maps.put("merchantNo", null);
            maps.put("nickname", null);
            maps.put("userName", null);
            maps.put("mobileNo", null);
            maps.put("repayType", null);
            maps.put("status", null);
            maps.put("repayAmount", null);
            maps.put("ensureAmount", null);
            maps.put("repayFee", null);
            maps.put("successPayAmount", null);
            maps.put("successRepayAmount", null);
            maps.put("actualPayFee", null);
            maps.put("actualWithdrawFee", null);
            maps.put("oneAgentName", null);
            maps.put("agentName", null);
            maps.put("repayNum", null);
            maps.put("accountNo", null);
            maps.put("bankName", null);
            maps.put("acqCode", null);
            maps.put("createTime", null);
            maps.put("repayBeginTime", null);
            maps.put("repayEndTime", null);
            maps.put("mission", null);
            maps.put("billingStatus", null);
            maps.put("completeTime", null);
            data.add(maps);
        }else{
            for (CreditRepayOrder or : list) {
            	if ("1".equals(or.getRepayType())) {
            		or.setRepayType("分期还款");
        			//分期还款的 服务费 = 0.006 * repay_amount + 2 * repay_num (保留两位小数并向上取整，即 3.141 取 3.15)
        			String[] arr = or.getRepayFeeRate().split("\\+");
        			BigDecimal repayFee = new BigDecimal(arr[0]).multiply(or.getRepayAmount())
        					.add(new BigDecimal(arr[1]).multiply(new BigDecimal(or.getRepayNum())))
        					.setScale(2, RoundingMode.UP);
        			or.setRepayFee(repayFee);
				} else if ("2".equals(or.getRepayType())) {
					or.setRepayType("全额还款");
				} else if ("3".equals(or.getRepayType())) {
					or.setRepayType("完美还款");
				}
            	if ("0".equals(or.getStatus())) {
            		or.setStatus("初始化");
            	} else if ("1".equals(or.getStatus())) {
            		or.setStatus("未执行");
            	} else if ("2".equals(or.getStatus())) {
            		or.setStatus("还款中");
            	} else if ("3".equals(or.getStatus())) {
            		or.setStatus("还款成功");
            	} else if ("4".equals(or.getStatus())) {
            		or.setStatus("还款失败");
            	} else if ("5".equals(or.getStatus())) {
            		or.setStatus("挂起");
            	} else if ("6".equals(or.getStatus())) {
            		or.setStatus("终止");
            	} else if ("7".equals(or.getStatus())) {
            		or.setStatus("逾期待还");
            	}
            	if ("0".equals(or.getBillingStatus())) {
            		or.setBillingStatus("未记账");
				} else if ("1".equals(or.getBillingStatus())) {
					or.setBillingStatus("发起记账失败");
				} else if ("2".equals(or.getBillingStatus())) {
					or.setBillingStatus("记账成功");
				} else if ("3".equals(or.getBillingStatus())) {
					or.setBillingStatus("记账失败");
				}
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("batchNo",or.getBatchNo()==null?"":or.getBatchNo());
                maps.put("merchantNo", or.getMerchantNo()==null?"":or.getMerchantNo());
                maps.put("nickname", or.getNickname()==null?"":or.getNickname());
                maps.put("userName", or.getUserName()==null?"":or.getUserName());
                maps.put("mobileNo", or.getMobileNo()==null?"":or.getMobileNo());
                maps.put("repayType", or.getRepayType()==null?"":or.getRepayType());
                maps.put("status", or.getStatus()==null?"":or.getStatus());
                maps.put("repayAmount", or.getRepayAmount()==null?"":or.getRepayAmount().toString());
                maps.put("ensureAmount", or.getEnsureAmount()==null?"":or.getEnsureAmount().toString());
                maps.put("repayFee", or.getRepayFee()==null?"":or.getRepayFee().toString());
                maps.put("successPayAmount", or.getSuccessPayAmount()==null?"":or.getSuccessPayAmount().toString());
                maps.put("successRepayAmount", or.getSuccessRepayAmount()==null?"":or.getSuccessRepayAmount().toString());
                maps.put("actualPayFee", or.getActualPayFee()==null?"":or.getActualPayFee().toString());
                maps.put("actualWithdrawFee", or.getActualWithdrawFee()==null?"":or.getActualWithdrawFee().toString());
				maps.put("oneAgentName", or.getOneAgentName() == null ? "" : or.getOneAgentName());
				maps.put("agentName", or.getAgentName() == null ? "" : or.getAgentName());
                maps.put("repayNum", or.getRepayNum()<0?"":or.getRepayNum()+"");
                maps.put("accountNo", or.getAccountNo()==null?"":or.getAccountNo());
                maps.put("bankName", or.getBankName()==null?"":or.getBankName());
                maps.put("acqCode", or.getAcqCode()==null?"":or.getAcqCode());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("repayBeginTime", or.getRepayBeginTime()==null?"":sdf1.format(or.getRepayBeginTime()));
                maps.put("repayEndTime", or.getRepayEndTime()==null?"":sdf1.format(or.getRepayEndTime()));
                maps.put("mission", or.getMission()==null?"":or.getMission());
                maps.put("billingStatus", or.getBillingStatus()==null?"":or.getBillingStatus());
                maps.put("completeTime", or.getCompleteTime()==null?"":sdf1.format(or.getCompleteTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "batchNo", "merchantNo", "nickname", "userName", "mobileNo", "repayType", "status",
				"repayAmount", "ensureAmount", "repayFee", "successPayAmount", "successRepayAmount", "actualPayFee",
				"actualWithdrawFee", "oneAgentName", "agentName", "repayNum", "accountNo", "bankName", "acqCode",
				"createTime", "repayBeginTime", "repayEndTime", "mission", "billingStatus", "completeTime" };
		String[] colsName = new String[] { "订单ID", "用户编号", "昵称", "姓名", "手机号", "订单类型", "订单状态", "任务金额(元)",
				"保证金(元)", "服务费(元)", "已消费总额(元)", "已还款总额(元)", "实际交易手续费(元)", "实际代付手续费(元)",
				"一级代理商", "直属代理商", "还款期数", "还款卡号", "还款银行", "交易通道", "创建时间", "开始时间",
				"结束时间", "任务", "记账状态", "终态时间" };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            if(state){
                log.error("导出信用卡还款订单失败!",e);
            }else{
                log.error("导出还款异常订单查询失败!",e);
            }
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    /**
     * 把'挂起'的订单改为'未执行'
     * @author	mays
     * @date	2018年5月10日
     */
    @RequestMapping(value = "/execute")
	@ResponseBody
	@SystemLog(description = "执行", operCode = "creditRepayOrder.resumePlan")
	public Map<String, Object> execute(@RequestBody String batchNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (batchNo == null || batchNo.isEmpty()) {
				msg.put("status", false);
				msg.put("msg", "订单号为空");
				return msg;
			}
			if (!"5".equals(creditRepayOrderService.selectStatusByBatchNo(batchNo))) {
				msg.put("status", false);
				msg.put("msg", "该订单的状态不为'挂起'");
				return msg;
			}
			String returnMsg = ClientInterface.wakePerfectRepayPlan(batchNo, CommonUtil.getLoginUser().getUsername());
			if (returnMsg == null || returnMsg.isEmpty()) {
				msg.put("status", false);
				msg.put("msg", "操作失败，返回为空");
				return msg;
			}
			Map<String, Object> result = JSON.parseObject(returnMsg);
			if ("200".equals(result.get("status"))) {
				msg.put("status", true);
			} else {
				msg.put("status", false);
			}
			msg.put("msg", result.get("msg"));
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "操作失败");
			log.error("信用卡还款-执行异常订单失败", e);
		}
		return msg;
	}

    /**
     * 批量执行
     * @return
     */
    @RequestMapping(value = "/batchExecute")
    @ResponseBody
    @SystemLog(description = "执行", operCode = "creditRepayOrder.batchResumePlan")
    public Map<String, Object> batchExecute(@RequestParam("baseInfo") String param) {
        Map<String, Object> msg = new HashMap<>();
        CreditRepayOrder condition = JSONObject.parseObject(param, CreditRepayOrder.class);
        List<CreditRepayOrder> orders = creditRepayOrderService.selectAbnormalByParam(condition);
        try {
            StringBuilder sbBatchNo = new StringBuilder();
            for (int i = 0; i < orders.size(); i++) {
                sbBatchNo.append(i == orders.size()-1?orders.get(i).getBatchNo():orders.get(i).getBatchNo()+",");
            }
            String returnMsg = ClientInterface.wakePerfectRepayPlan(sbBatchNo.toString(), CommonUtil.getLoginUser().getUsername());
            if (returnMsg == null || returnMsg.isEmpty()) {
                msg.put("status", false);
                msg.put("msg", "操作失败，返回为空");
                return msg;
            }
            Map<String, Object> result = JSON.parseObject(returnMsg);
            if ("200".equals(result.get("status"))) {
                msg.put("status", true);
                msg.put("msg", "操作成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "操作失败");
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "操作失败");
            log.error("信用卡还款-执行异常订单失败", e);
        }
        return msg;
    }



}
