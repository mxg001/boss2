package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SettleTransfer;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.historyquery.SettleOrderInfoHistoryService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value="/settleOrderInfoHistoryAction")
public class SettleOrderInfoHistoryAction {
	private static final Logger log = LoggerFactory.getLogger(SettleOrderInfoHistoryAction.class);

	@Resource
	private SettleOrderInfoHistoryService settleOrderInfoHistoryService;

	@Resource
	private SysDictService sysDictService;

	/**
	 * 初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<SettleOrderInfo> page,@RequestParam("info") String param) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = JSON.parseObject(param, SettleOrderInfo.class);
			settleOrderInfoHistoryService.selectAllInfo(page, soi);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 查询总金额
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping("/getTotalNumAndTotalMoney")
	@ResponseBody
	public Object getTotalNumAndTotalMoney(@RequestParam("info") String param) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = JSON.parseObject(param, SettleOrderInfo.class);
			String money = settleOrderInfoHistoryService.getTotalNumAndTotalMoney(soi);
			if(money==null){
				map.put("totalMoney", "0");
			}else{
				map.put("totalMoney", money);
			}
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错",e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}

	/**
	 * 详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="/selectInfoDetail")
	@ResponseBody
	public Object selectInfoDetail(@RequestParam("ids") String ids) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = settleOrderInfoHistoryService.selectInfo(ids);
			if(soi==null){
				jsonMap.put("bols", false);
				jsonMap.put("msg","查询为空");
				return jsonMap;
			}
			List<SettleTransfer> slist=settleOrderInfoHistoryService.selectSettleInfo(ids);

			jsonMap.put("bols", true);
			jsonMap.put("soi",soi);
			jsonMap.put("slist",slist);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("msg","查询详情报错");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	@RequestMapping(value="/exportAllInfo")
	@ResponseBody 
	public void exportAllInfo(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		//20171011,mys,优化
		HashMap<String, String> settleNameMap = getSysDictMap(sysDictService.selectByKey("SETTLE_TYPE"));//出款类型
		HashMap<String, String> settleStatusNameMap = getSysDictMap(sysDictService.selectByKey("SETTLE_STATUS"));//结算状态

		SettleOrderInfo soi=JSON.parseObject(info,SettleOrderInfo.class);
		List<SettleOrderInfo> list=settleOrderInfoHistoryService.importAllInfo(soi);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "出款记录"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);   
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("settleOrder",null);
			maps.put("settleType", null);
			maps.put("settleUserType", null);
			maps.put("settleUserName", null);
			maps.put("settleUserNo", null);
			maps.put("settleAmount", null);
			maps.put("outAmount", null);
			maps.put("feeAmount", null);
			maps.put("settleStatus", null);
			maps.put("sourceSystem", null);
			maps.put("sourceBatchNo", null);
			maps.put("sourceOrderNo", null);
			maps.put("createTime", null);
			data.add(maps);
		}else{
			Map<String, String> sourceSystemMap = sysDictService.selectMapByKey("SOURCE_SYSTEM");
			for (SettleOrderInfo settleOrderInfo : list) {
				if (settleOrderInfo.getSettleType() != null) {
					settleOrderInfo.setSettleName(settleNameMap.get(settleOrderInfo.getSettleType()));
				}
				if (settleOrderInfo.getSettleStatus() != null) {
					settleOrderInfo.setSettleStatusName(settleStatusNameMap.get(settleOrderInfo.getSettleStatus()));
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("settleOrder",settleOrderInfo.getSettleOrder());
				maps.put("settleType", null==settleOrderInfo.getSettleName()?"":settleOrderInfo.getSettleName());
				maps.put("settleUserType", null==settleOrderInfo.getSettleUserType()?"":settleOrderInfo.getSettleUserType());
				maps.put("settleUserName", null==settleOrderInfo.getSettleUserName()?"":settleOrderInfo.getSettleUserName());
				maps.put("settleUserNo", null==settleOrderInfo.getSettleUserNo()?"":settleOrderInfo.getSettleUserNo());
				maps.put("settleAmount", null==settleOrderInfo.getSettleAmount()?"":settleOrderInfo.getSettleAmount().toString());
				maps.put("outAmount", null==settleOrderInfo.getOutAmount() ? "" : settleOrderInfo.getOutAmount().toString());
				maps.put("feeAmount", null==settleOrderInfo.getFeeAmount() ? "" : settleOrderInfo.getFeeAmount().toString());
				maps.put("settleStatus", null==settleOrderInfo.getSettleStatusName()?"":settleOrderInfo.getSettleStatusName());
				String sourceSystem = settleOrderInfo.getSourceSystem();
				if(sourceSystemMap != null){
					sourceSystem = sourceSystemMap.get(sourceSystem);
				}
				maps.put("sourceSystem", sourceSystem);
				maps.put("sourceBatchNo", null==settleOrderInfo.getSourceBatchNo()?"":settleOrderInfo.getSourceBatchNo());
				maps.put("sourceOrderNo", null==settleOrderInfo.getSourceOrderNo()?"":settleOrderInfo.getSourceOrderNo());
				maps.put("createTime", null==settleOrderInfo.getCreateTime()?"":sdf1.format(settleOrderInfo.getCreateTime()));
				maps.put("errMsg", StringUtils.hasLength(settleOrderInfo.getErrMsg())?settleOrderInfo.getErrMsg():settleOrderInfo.getSettleMsg());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"settleOrder","settleType","settleUserType","settleUserName","settleUserNo","settleAmount",
				"outAmount","feeAmount","settleStatus","sourceSystem","sourceBatchNo","sourceOrderNo","createTime","errMsg"};
		String[] colsName = new String[]{"出款记录ID","来源类型","用户类型","用户简称","用户编号","出款金额(元)","到账金额（元）",
				"出款手续费（元）","结算状态","来源系统","来源系统批次号","来源订单号","创建时间","结算错误信息"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, response.getOutputStream());
		ouputStream.close();
	}

	/**
	 * 出款明细初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	
	@RequestMapping(value="/selectOutDetailAllInfo")
	@ResponseBody
	public Object selectOutDetailAllInfo(@ModelAttribute("page")Page<SettleOrderInfo> page,@RequestParam("info") String param) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = JSON.parseObject(param, SettleOrderInfo.class);
			settleOrderInfoHistoryService.selectOutDetailAllInfo(page, soi);
			if(page.getResult()!=null&&page.getResult().size()>0){
				for (SettleOrderInfo item :page.getResult()){
					//屏蔽敏感信息
					item.setMobile(StringUtil.sensitiveInformationHandle(item.getMobile(),0));
				}
			}
			Map<String, String> count = settleOrderInfoHistoryService.getOutDetailTotalMoney(soi);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
			jsonMap.put("count", count);
		} catch (Exception e) {
			log.error("查询出款明细报错",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}


	/**
	 * 出款明细详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="/selectOutInfoDetail")
	@ResponseBody
	public Object selectOutInfoDetail(@RequestParam("ids") String ids) {
		return getDetail(ids,0);
	}

	@RequestMapping(value="/getDetailShow")
	@ResponseBody
	public Object getDetailShow(@RequestParam("ids") String ids) {
		return getDetail(ids,1);
	}

	private Map<String,Object> getDetail(String ids,int editState){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleTransfer st=settleOrderInfoHistoryService.selectOutSettleInfo(ids);
			if(st!=null){
				SettleOrderInfo soi=new SettleOrderInfo();
				if(st.getOrderNo()==null||st.getOrderNo().equals("")){
					soi = settleOrderInfoHistoryService.selectInfo(st.getTransId());
				}else{
					soi = settleOrderInfoHistoryService.selectOrderNoInfo(st.getOrderNo());
				}
				//脱敏处理
				if(0==editState){
					st.setInAccNo(StringUtil.sensitiveInformationHandle(st.getInAccNo(),2));
				}
				jsonMap.put("bols", true);
				jsonMap.put("soi",soi);
				jsonMap.put("st",st);
			}else{
				jsonMap.put("bols", false);
				jsonMap.put("msg", "没找出到对应数据");
			}
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("msg","查询详情报错");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 把数据字典查询的结果转成map
	 */
	private HashMap<String, String> getSysDictMap(List<SysDict> list) {
		HashMap<String, String> map = new HashMap<>();
		for (SysDict sysDict : list) {
			map.put(sysDict.getSysValue(), sysDict.getSysName());
		}
		return map;
	}

	
	@RequestMapping(value="/exportInfo")
	@ResponseBody 
	public void exportInfo(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		//20171011,mys,优化
		HashMap<String, String> statusNameMap = getSysDictMap(sysDictService.selectByKey("OUT_STATUS"));//出款状态
		HashMap<String, String> settleNameMap = getSysDictMap(sysDictService.selectByKey("SETTLE_TYPE"));//出款类型
		HashMap<String, String> settleStatusNameMap = getSysDictMap(sysDictService.selectByKey("SETTLE_STATUS"));//结算状态

		SettleOrderInfo soi=JSON.parseObject(info,SettleOrderInfo.class);
		List<SettleOrderInfo> list=settleOrderInfoHistoryService.exportOutDetailAllInfo(soi);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "出款明细记录"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);   
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
			maps.put("accountSerialNo", null);
			maps.put("transId", null);
			maps.put("orderNo", null);
			maps.put("settleStatusName", null);
			maps.put("statusName", null);
			maps.put("settleName", null);
			maps.put("settleUserNo", null);
			maps.put("settleUserName", null);
			maps.put("unionpayMerNo", null);
			maps.put("mobile", null);
			maps.put("amount", null);
			maps.put("feeAmount", null);
			maps.put("deductionFee", null);
			maps.put("outAmount", null);
			maps.put("actualFee", null);
			maps.put("acqEnname", null);
			maps.put("outServiceName", null);
			maps.put("sourceBatchNo", null);
			maps.put("createTime", null);
			maps.put("inAccName", null);
			maps.put("inAccNo", null);
			maps.put("inBankName", null);
			data.add(maps);
		}else{
			for (SettleOrderInfo settleOrderInfo : list) {
				//屏蔽敏感信息
				if(soi.getEditState()==0){
					settleOrderInfo.setMobile(StringUtil.sensitiveInformationHandle(settleOrderInfo.getMobile(),0));
					settleOrderInfo.setInAccNo(StringUtil.sensitiveInformationHandle(settleOrderInfo.getInAccNo(),2));
				}

				if (settleOrderInfo.getStatus() != null) {
					settleOrderInfo.setStatusName(statusNameMap.get(settleOrderInfo.getStatus()));
				}
				if (settleOrderInfo.getSettleType() != null) {
					settleOrderInfo.setSettleName(settleNameMap.get(settleOrderInfo.getSettleType()));
				}
				if (settleOrderInfo.getSettleStatus() != null) {
					settleOrderInfo.setSettleStatusName(settleStatusNameMap.get(settleOrderInfo.getSettleStatus()));
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id",settleOrderInfo.getId());
				maps.put("accountSerialNo", null==settleOrderInfo.getAccountSerialNo()?"":settleOrderInfo.getAccountSerialNo());
				maps.put("transId", null==settleOrderInfo.getTransId()?"":settleOrderInfo.getTransId());
				maps.put("orderNo", null==settleOrderInfo.getOrderNo()?"":settleOrderInfo.getOrderNo());
				maps.put("settleStatusName", null==settleOrderInfo.getSettleStatusName()?"":settleOrderInfo.getSettleStatusName());
				maps.put("statusName", null==settleOrderInfo.getStatusName()?"":settleOrderInfo.getStatusName());
				maps.put("settleName", null==settleOrderInfo.getSettleName()?"":settleOrderInfo.getSettleName());
				maps.put("settleUserNo", null==settleOrderInfo.getSettleUserNo()?"":settleOrderInfo.getSettleUserNo());
				maps.put("settleUserName", null==settleOrderInfo.getSettleUserName()?"":settleOrderInfo.getSettleUserName());
				maps.put("unionpayMerNo", null==settleOrderInfo.getUnionpayMerNo()?"":settleOrderInfo.getUnionpayMerNo());
				maps.put("mobile", null==settleOrderInfo.getMobile()?"":settleOrderInfo.getMobile());
				maps.put("amount", null==settleOrderInfo.getAmount()?"0.00":settleOrderInfo.getAmount().toString());
				maps.put("feeAmount", null==settleOrderInfo.getFeeAmount()?"0.00":settleOrderInfo.getFeeAmount().toString());
				maps.put("deductionFee", null==settleOrderInfo.getDeductionFee()?"0.00":settleOrderInfo.getDeductionFee().toString());
				maps.put("outAmount", null==settleOrderInfo.getOutAmount()?"0.00":settleOrderInfo.getOutAmount().toString());
				maps.put("actualFee", null==settleOrderInfo.getActualFee()?"0.00":settleOrderInfo.getActualFee().toString());
				maps.put("deductionFee", StringUtil.filterNull(settleOrderInfo.getDeductionFee()));
				maps.put("actualFee",StringUtil.filterNull(settleOrderInfo.getActualFee()));
				maps.put("acqEnname", null==settleOrderInfo.getAcqEnname()?"":settleOrderInfo.getAcqEnname());
				maps.put("outServiceName", null==settleOrderInfo.getOutServiceName()?"":settleOrderInfo.getOutServiceName());
				maps.put("sourceBatchNo", null==settleOrderInfo.getSourceBatchNo()?"":settleOrderInfo.getSourceBatchNo());
				maps.put("createTime", null==settleOrderInfo.getCreateTime()?"":sdf1.format(settleOrderInfo.getCreateTime()));
				maps.put("inAccName", null==settleOrderInfo.getInAccName()?"":settleOrderInfo.getInAccName());
				maps.put("inAccNo", null==settleOrderInfo.getInAccNo()?"":settleOrderInfo.getInAccNo());
				maps.put("inBankName", null==settleOrderInfo.getInBankName()?"":settleOrderInfo.getInBankName());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","accountSerialNo","transId","orderNo","settleStatusName","statusName",
				"settleName","settleUserNo","settleUserName","unionpayMerNo","mobile","amount","feeAmount","deductionFee","outAmount","actualFee","acqEnname",
				"outServiceName","sourceBatchNo","createTime","inAccName","inAccNo","inBankName"};
		String[] colsName = new String[]{"出款记录ID","出款流水号","来源订单号","交易订单号","结算状态","出款明细状态","出款类型","结算用户编号",
				"结算用户简称","银联报备商户编号","用户手机号","出款金额（元）","出款手续费（元）","抵扣手续费（元）","实际出款金额（元）","实际出款手续费（元）",
				"出款通道","出款服务名称","来源批次号","出款时间","收款账户名称","结算卡号","结算银行名称"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}
}
