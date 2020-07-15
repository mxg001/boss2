package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantBusinessProductDao;
import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.RouteGroupDao;
import cn.eeepay.framework.dao.TerminalInfoDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.util.ClientInterface;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("merchantInfoService")
@Transactional
public class MerchantInfoServiceImpl implements MerchantInfoService {

	@Resource
	private MerchantInfoDao merchantInfoDao;
	
	@Resource
	private TerminalInfoDao terminalInfoDao;
	
	@Resource
	private RouteGroupDao routeGroupDao;
	
	
	@Resource
	private MerchantBusinessProductDao merchantBusinessProductDao;
	
	private static final Logger log = LoggerFactory.getLogger(MerchantInfoServiceImpl.class);

	
	@Override
	public MerchantInfo selectByPrimaryKey(Long id) {
		return merchantInfoDao.selectByPrimaryKey(id);
	}


	@Override
	public int updateByPrimaryKey(MerchantInfo record) {
		return merchantInfoDao.updateByPrimaryKey(record);
	}

	@Override
	public List<MerchantInfo> selectAllInfo() {
		return merchantInfoDao.selectAllInfo();
	}

	@Override
	public List<MerchantInfo> selectByMertId(String mertId) {
		return merchantInfoDao.selectByMertId(mertId);
	}

	@Override
	public int updateByMerId(MerchantInfo record) {
		return merchantInfoDao.updateByMerId(record);
	}

	@Override
	public List<MerchantInfo> selectByNameInfoByTermianl(String oneAgentNo) {
		return merchantInfoDao.selectByNameInfoByTermianl(oneAgentNo);
	}


	@Override
	public MerchantInfo selectByMerNo(String merchantNo) {
		return merchantInfoDao.selectByMerNo(merchantNo);
	}


	@Override
	public SysDict selectSysDictByKey(String key,String Pkey) {
		return merchantInfoDao.selectSysDictByKey(key,Pkey);
	}


	@Override
	public List<SysDict> selectTwoInfoByParentId(String ParentId) {
		return merchantInfoDao.selectTwoInfoByParentId(ParentId);
	}


	@Override
	public List<SysDict> selectOneInfo() {
		return merchantInfoDao.selectOneInfo();
	}


	@Override
	public MerchantInfo selectByMerIC(String card) {
		return merchantInfoDao.selectByMerIC(card);
	}


	@Override
	public MerchantInfo selectMerExistByMerNo(String merchantNo) {
		return merchantInfoDao.selectMerExistByMerNo(merchantNo);
	}


	@Override
	public int updateAddressByMerId(MerchantInfo record) {
		return merchantInfoDao.updateAddressByMerId(record);
	}


	@Override
	public int updateMerAcoount(String merNo) {
		return merchantInfoDao.updateMerAcoount(merNo);
	}


	@Override
	public List<MerchantInfo> selectByMerAccount() {
		return merchantInfoDao.selectByMerAccount();
	}


	@Override
	public int updateRiskStatus(String merId, String riskStatus) {
		int i =0 ;
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("subjectNo", "224101001");
		claims.put("accountType", "M");
		claims.put("currencyNo", "1");
		claims.put("accountOwner", "000001");
		if (riskStatus.equals("2"))
			claims.put("accountStatus", "3");
		else if (riskStatus.equals("3"))
			claims.put("accountStatus", "4");
		else
			claims.put("accountStatus", "1");
		claims.put("selectType", "2");
		claims.put("accountNo", null);
		claims.put("cardNo", null);
		claims.put("userId", merId);
		i = merchantInfoDao.updateRiskStatus(merId, riskStatus);
		if (i == 0) {
			return 0;
		}
		String result = ClientInterface.transFrozenAccountNo(claims);
		if (result == null) {
			if (riskStatus.equals("2") || riskStatus.equals("3"))
				throw new RuntimeException("账号冻结失败");
			else
				throw new RuntimeException("账号解冻失败");

		}
		JSONObject json = JSON.parseObject(result);
		if (!(boolean) json.get("status")) {
			if (riskStatus.equals("2") || riskStatus.equals("3"))
				throw new RuntimeException("账号冻结失败");
			else
				throw new RuntimeException("账号解冻失败");
		}
		return i;
	}
	@Override
	public int updateRiskStatusbyBlack(String merId, String riskStatus) {
		return merchantInfoDao.updateRiskStatus(merId, riskStatus);
	}

	@Override
	public MerchantInfo selectByMobilephone(String mobilephone) {
		return merchantInfoDao.selectByMobilephone(mobilephone);
	}

	@Override
	public MerchantInfo selectByMobilephoneAndTeam(String mobilephone, String teamId) {
		return merchantInfoDao.selectByMobilephoneAndTeam(mobilephone, teamId);
	}

	@Override
	public MerchantInfo selectStatusByMerNo(String merchantNo) {
		return merchantInfoDao.selectStatusByMerNo(merchantNo);
	}

	@Override
	public List<MerchantInfo> getMerchantFew(String item) {
		return merchantInfoDao.getMerchantFew(item);
	}

	@Override
	public List<ServiceRate> getServiceRateByServiceId(String oneAgentNo, String serviceId) {
		return merchantInfoDao.getServiceRateByServiceId(oneAgentNo, serviceId);
	}

	@Override
	public List<ServiceQuota> getServiceQuotaByServiceId(String oneAgentNo, String serviceId) {
		return merchantInfoDao.getServiceQuotaByServiceId(oneAgentNo, serviceId);
	}

	/**
	 * 开通无卡
	 * @param params
	 * @return
	 */
	@Override
	public boolean linkProduct(Map<String, String> params,UserLoginInfo principal) {
		int i = 0;
		String agentNo = params.get("agentNo");
		String parentNode = params.get("parentNode");
		String merchantNo = params.get("merchantNo");
		String linkBpId = params.get("linkBpId");
		String hpId = params.get("hpId");
		String psamNo = params.get("psamNo");
		String posType = params.get("posType");
		String oneAgentNo = params.get("oneAgentNo");
		String merbpId = params.get("merbpId");


		log.info("商户号"+merchantNo+"关联业务产品ID"+linkBpId);
		//插入机具信息
		TerminalInfo record = new TerminalInfo();
		record.setPsamNo(psamNo);
		record.setCollectionCode(psamNo);
		record.setCashierNo(principal.getId().toString());
		record.setType(hpId);
		record.setOpenStatus("2");
		record.setAgentNode(parentNode);
		record.setAgentNo(agentNo);
		record.setPosType(posType);
		record.setMerchantNo(merchantNo);
		record.setCreateTime(new Date());
		record.setBpId(linkBpId);
		i = terminalInfoDao.insertPsamInfo(record);
		if(i != 1){
			throw new RuntimeException("自动写入激活码失败");
		}
		//商户业务产品信息
		i = 0;
		MerchantBusinessProduct  mbp = new MerchantBusinessProduct();
		//获取进件ID初始次数复制到无卡上
		MerchantBusinessProduct mbpInfo=merchantBusinessProductDao.getMerchantBusinessProductInfo(Long.valueOf(merbpId));
		mbp.setAuditNum(mbpInfo.getAuditNum());

		mbp.setMerchantNo(merchantNo);
		mbp.setBpId(linkBpId);
		mbp.setStatus("4");
		mbp.setItemSource("4");

		i = merchantBusinessProductDao.insertMerProduct(mbp);
		if(i != 1){
			throw new RuntimeException("写入商户业务产品信息失败");
		}
		//
		List<ServiceInfo> listService = null;
		listService  = merchantInfoDao.getServiceInfoByParams(agentNo, linkBpId);	//查询服务
		if(listService == null ||listService.size()<1){
			throw new RuntimeException("无服务");
		}
		//写入商户服务
		for (ServiceInfo serviceInfo : listService){
			i = 0;
			MerchantService merService = new MerchantService();
			merService.setBpId(linkBpId);
			merService.setMerchantNo(merchantNo);
			merService.setServiceId(serviceInfo.getServiceId().toString());
			merService.setCreateDate(new Date());
		//	String status=String.valueOf(serviceInfo.getServiceStatus());
			merService.setStatus("1");
		 	i = merchantInfoDao.insertMerService(merService);
		 	if(i != 1){
				throw new RuntimeException("写入商户服务信息失败");
			}
		}
		
		for (ServiceInfo ser : listService) {
			List<ServiceQuota> listmsq = null;
			List<ServiceRate> listmr = null;
			String serviceId = ser.getServiceId().toString();
			log.info("拥有的服务有"+serviceId);
			if(ser.getFixedRate()==0){//不固定
					listmr = merchantInfoDao.getServiceRateByServiceId(oneAgentNo, serviceId);
				}
			if(ser.getFixedQuota()==0){
					listmsq = merchantInfoDao.getServiceQuotaByServiceId(oneAgentNo, serviceId);
				}		
			//写入商户服务限额
			if(listmsq != null && listmsq.size()>0){
			for (ServiceQuota serviceQuota : listmsq){
					i = 0;
					MerchantServiceQuota msqs=new MerchantServiceQuota();
					msqs.setMerchantNo(merchantNo);
					msqs.setServiceId(serviceQuota.getServiceId().toString());
					msqs.setCardType(serviceQuota.getCardType());
					msqs.setHolidaysMark(serviceQuota.getHolidaysMark());
					msqs.setSingleCountAmount(serviceQuota.getSingleCountAmount());
					msqs.setSingleDayAmount(serviceQuota.getSingleDayAmount());
					msqs.setSingleDaycardAmount(serviceQuota.getSingleDaycardAmount());
					msqs.setSingleDaycardCount(serviceQuota.getSingleDaycardCount());
					msqs.setSingleMinAmount(serviceQuota.getSingleMinAmount());
					msqs.setDisabledDate(new Date());
					msqs.setUseable("1");
					i = merchantInfoDao.insertMerQuota(msqs);
					if(i != 1){
						throw new RuntimeException("写入商户服务限额失败");
					}
				}
			}
			//写入商户服务费率
			if(listmr != null && listmr.size()>0){
			for (ServiceRate serviceRate : listmr){
					i =0;
					MerchantServiceRate msrs=new MerchantServiceRate();
					msrs.setMerchantNo(merchantNo);
					msrs.setServiceId(serviceRate.getServiceId().toString());
					msrs.setCardType(serviceRate.getCardType());
					msrs.setHolidaysMark(serviceRate.getHolidaysMark());
					msrs.setRateType(serviceRate.getRateType());
					msrs.setMerRate(serviceRate.getMerRate());		
					msrs.setCapping(serviceRate.getCapping());
					msrs.setRate(serviceRate.getRate());
					msrs.setSafeLine(serviceRate.getSafeLine());
					msrs.setSingleNumAmount(serviceRate.getSingleNumAmount());
					msrs.setLadder1Max(serviceRate.getLadder1Max());
					msrs.setLadder1Rate(serviceRate.getLadder1Rate());
					msrs.setLadder2Max(serviceRate.getLadder2Max());
					msrs.setLadder2Rate(serviceRate.getLadder2Rate());
					msrs.setLadder3Max(serviceRate.getLadder3Max());
					msrs.setLadder3Rate(serviceRate.getLadder3Rate());
					msrs.setLadder4Max(serviceRate.getLadder4Max());
					msrs.setLadder4Rate(serviceRate.getLadder4Rate());			
					i = merchantInfoDao.insertMerRate(msrs);
					if(i != 1){
						throw new RuntimeException("写入商户服务费率失败");
					}
				}
			}//写入商户服务费率结束
	
		}
		routeGroupDao.insertRouteGroupByMerchant(merchantNo);
		return true;
		
		
	}


	@Override
	public SysDict selectSysDict(String key) {
		// TODO Auto-generated method stub
		return merchantInfoDao.selectSysDict(key);
	}

	@Override
	public int updateMerAccountBatch(List<String> merchantNoList) {
		Set<String> setList = new HashSet<>();
		setList.addAll(merchantNoList);
		merchantNoList.clear();
		merchantNoList.addAll(setList);
		for (int i=merchantNoList.size()-1; i>-1; i--) {
			String acc=ClientInterface.createMerchantAccount(merchantNoList.get(i));
			log.info("商户批量开户,商户号:{},返回结果:{}", merchantNoList.get(i),acc);
			if(!JSONObject.parseObject(acc).getBooleanValue("status")){
				merchantNoList.remove(i);
			}
		}
		int num = 0;
		if(merchantNoList.size()>0){
			num = merchantInfoDao.updateMerAccountBatch(merchantNoList);
		}
		return num;
	}


}
