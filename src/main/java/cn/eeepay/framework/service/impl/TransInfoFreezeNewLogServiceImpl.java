package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.MerchantPreFreezeLogDao;
import cn.eeepay.framework.dao.TransInfoFreezeNewLogDao;
import cn.eeepay.framework.util.ClientInterface;

@Service("transInfoFreezeNewLogService")
@Transactional
public class TransInfoFreezeNewLogServiceImpl implements TransInfoFreezeNewLogService{
	private static final Logger log = LoggerFactory.getLogger(TransInfoFreezeNewLogServiceImpl.class);
	@Resource
	private TransInfoFreezeNewLogDao transInfoFreezeNewLogDao;
	
	@Resource
	private TransInfoService transInfoService;
	
	@Resource
	private AgentInfoService agentInfoService;
	
	@Resource
	private MerchantInfoService merchantInfoService;
	
	@Resource
	private MerchantPreFreezeLogDao merchantPreFreezeLogDao;
	
	@Resource
	public TransInfoFreezeNewLogService transInfoFreezeNewLogService;
	
	@Resource
	private MerchantInfoDao merchantInfoDao;
	@Resource
	private AutoTransferService autoTransferService;
	@Resource
	private SysDictService sysDictService;

	@Override
	public List<TransInfoFreezeNewLog> queryByOrderNo(String orderNo) {
		return transInfoFreezeNewLogDao.queryByOrderNo(orderNo);
	}
	@Override
	public Map<String, Object> insertInfo(TransInfoFreezeNewLog record,CollectiveTransOrder cto,String fd,String sts, String operId, String realName,BigDecimal originalPreFrozenMoney) {
		Map<String, Object> map=new HashMap<String, Object>();
		int num=0;
		try {
			TransInfo tt =null;
			ScanCodeTrans sct=null;
			String acqEnname = null;
			String directAgentNo = null;//直属代理商
			String fromSerialNo =null;
			if(cto.getPayMethod().equals("1")){
				tt = transInfoService.queryInfo(cto.getOrderNo());
				acqEnname=tt.getAcqEnname();
				directAgentNo=tt.getAgentNo();
				fromSerialNo=tt.getSerialNo();
			}else{
				sct=transInfoService.queryScanInfo(cto.getOrderNo());
				acqEnname=sct.getAcqEnname();
				fromSerialNo=sct.getTradeNo();
				MerchantInfo mis = merchantInfoService.selectMerExistByMerNo(cto.getMerchantNo());
				directAgentNo=mis.getAgentNo();
			}
			AgentInfo ais = agentInfoService.selectByagentNo(cto.getAgentNo());
			if(ais==null){
				map.put("msg", "代理商为空");
				map.put("bols", false);
				return map;
			}
			  BigDecimal agentShareAmount = new BigDecimal(0.00);
			CollectiveTransOrder cctt = transInfoService.selectByOrderNo(ais.getAgentLevel(), cto.getOrderNo());
			if(cctt!=null){
				agentShareAmount=cctt.getNum();
			}
	        final HashMap<String, Object> claims = new HashMap<String, Object>();
	        String merchantNo = cto.getMerchantNo();//商户编号
	        String oneAgentNo = cto.getOneAgentNo();//一级代理商
		    String acqServiceId = cto.getAcqServiceId().toString();
		    String serviceId = cto.getServiceId().toString();
	        BigDecimal transAmount = cto.getTransAmount();//交易金额
	        String acqOrgId = cto.getAcqOrgId().toString();

			//liuks 商户手续费,应该已表中的数据为准
			CollectiveTransOrder oldCto=transInfoService.queryCollectiveTransOrder(cto.getId().toString());

	        String merchantFee = oldCto.getMerchantFee().toString();
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	        Date transDate=cto.getTransTime();
	        String transTime = sdf.format(transDate);
	        claims.put("fromSystem", "boss");
	        claims.put("transDate", transTime.trim());
	        claims.put("transAmount", transAmount.toString().trim());
	        claims.put("agentShareAmount", agentShareAmount.toString().trim());
	        claims.put("fromSerialNo", fromSerialNo.trim());//流水号
	        claims.put("transOrderNo", cto.getOrderNo().trim());//订单号
	        claims.put("oneAgentNo", oneAgentNo.trim());
	        claims.put("directAgentNo", directAgentNo.trim());
	        claims.put("merchantNo", merchantNo.trim());
	        claims.put("transTypeCode", sts.trim()); //交易冻结 000017冻结  000018解冻
	        claims.put("serviceId", serviceId.trim());
	        claims.put("acqServiceId", acqServiceId.trim());
	        claims.put("cardNo", cto.getAccountNo());
	        claims.put("holidays", cto.getHolidaysMark().trim());
	        claims.put("acqOrgId", acqOrgId.trim());
	        claims.put("acqEnname", acqEnname.trim());
	        claims.put("merchantFee", merchantFee.trim());
	        String result = ClientInterface.transFrozenAccount(claims);
//	        {"msg":"冻结/解冻记账成功","name":"交易冻结/解冻","status":true,"timestamp":"1474527627988"}
			JSONObject json=JSON.parseObject(result);

			if(!(boolean)json.get("status")){
				map.put("msg", json.get("msg"));
				map.put("bols", false);
				return map;
			}else{
				cto.setFreezeStatus(fd);
				num=transInfoService.updateInfoByOrderNo(cto);
				if(num==0){
					map.put("msg", "操作失败");
					map.put("bols", false);
					return map;
				}
				num=transInfoFreezeNewLogDao.insertInfo(record);
				if(num==0){
					map.put("msg", "操作失败");
					map.put("bols", false);
					return map;
				}
				// 插入预冻结日志
				if (originalPreFrozenMoney.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal newPreFrozenMoney = BigDecimal.ZERO;// 冻结之后的预冻结金额
					newPreFrozenMoney = originalPreFrozenMoney.subtract(cto.getTransAmount());
					TransInfoPreFreezeLog preRecord = new TransInfoPreFreezeLog();
					preRecord.setOperId(operId);
					preRecord.setOperName(realName);
					preRecord.setPreFreezeNote("风险预冻结");
					preRecord.setMerchantNo(cto.getMerchantNo());
					preRecord.setOperTime(new Date());
					preRecord.setOperLog("预冻结金额触发自动冻结，从" + originalPreFrozenMoney + "更改为" + newPreFrozenMoney);
					// 插入预冻结金额操作日志
					merchantPreFreezeLogDao.insertPreFreezeLog(preRecord);
					// 更新商户表中的预冻结金额
					merchantInfoDao.updatePreFrozenAmountByMerId(newPreFrozenMoney, preRecord.getMerchantNo());
				}

				UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if("000017".equals(sts.trim())){
					//冻结时判断auto_transfer表中是否已存在该笔订单且状态为待执行，如果存在则将状态有待执行变更为再风控
					autoTransferService.updateByOrderNoAndStatus(cto.getOrderNo(), "1");
				}else {
					//解冻-插入自动出款任务表
					// 判断是否符合出款条件
					/*CollectiveTransOrder order = transInfoService.selectByOrderNo(cto.getOrderNo());

					if("SUCCESS".equals(order.getTransStatus()) && "0".equals(order.getFreezeStatus()) &&
							"PURCHASE".equals(order.getTransType()) && "记账成功".equals(order.getTransMsg()) &&
							(StringUtils.isBlank(order.getSettleStatus()) || "0".equals(order.getSettleStatus()) || "3".equals(order.getSettleStatus())) ){
						autoTransferService.insertOne(cto.getOrderNo(),cto.getMerchantNo(), "2",cto.getSettlementMethod(),"1", principal.getUsername());
					}*/
					int reSettleTaskCount = new Integer(sysDictService.getValueByKey("RE_SETTLE_TASK_COUNT")).intValue();
					int autoTransferLastDay = new Integer(sysDictService.getValueByKey("AUTO_TRANSFER_LAST_DAY")).intValue();
					String settleMethod = cto.getSettlementMethod();
					CollectiveTransOrder order = null;
					//T0转T1订单:当前时间8天前,结算状态为：未结算、结算失败
					if("0".equals(settleMethod) && "4".equals(cto.getSettleStatus())){
						order = autoTransferService.selectToT1ByOrderNoAndTransTime(cto.getOrderNo(), autoTransferLastDay);
					}
					// T0订单：当前时间-RE_SETTLE_TASK_COUNT 之前
					if("0".equals(settleMethod) &&  !"4".equals(cto.getSettleStatus())){
						order = autoTransferService.selectT0ByOrderNoAndTransTime(cto.getOrderNo(), reSettleTaskCount);
					}
					// T1订单：当前时间8天前，结算状态为：未结算、结算失败、转t1
					if("1".equals(settleMethod)){
						order = autoTransferService.selectT1ByOrderNoAndTransTime(cto.getOrderNo(), autoTransferLastDay);
					}
					if(null != order){
						autoTransferService.insertOne(cto.getOrderNo(),cto.getMerchantNo(), "2",cto.getSettlementMethod(),"1", principal.getUsername());
					}

				}
			}

			map.put("msg", json.get("msg"));
			map.put("bols", true);
		} catch (Exception e) {
			log.error("操作报错",e);
			System.out.println(e);
			map.put("msg", "操作失败");
			map.put("bols", false);
			throw new RuntimeException("冻结交易异常");
		}
		return map;
	}

}
