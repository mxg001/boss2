package cn.eeepay.framework.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.MerchantBusinessProductService;
import cn.eeepay.framework.service.MerchantMigrateService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("merchantMigrateService")
@Transactional
public class MerchantMigrateServiceImpl implements MerchantMigrateService {
	
	private static final Logger log = LoggerFactory.getLogger(MerchantMigrateInfo.class);
	
	@Resource
	private MerchantMigrateDao merchantMigrateDao;
	@Resource
	private AgentInfoDao agentInfoDao;
	@Resource
	private MerchantInfoDao merchantInfoDao; 
	@Resource
	private SeqService seqService;
	
	@Resource
	public TerminalInfoDao terminalInfoDao;
	
	@Resource
	private MerchantBusinessProductDao merchantBusinessProductDao;
	
	@Resource
	private MerchantBusinessProductService merchantBusinessProductService;
	@Resource
	private SysDictDao sysDictDao;
	
	private int migrateMerchant(MerchantMigrate mm, MerchantMigrateInfo mmi, String agentParentNode){
		//迁移商户
		int mcount = merchantMigrateDao.modifyMerchant(mm.getNodeAgentNo(),mm.getAgentNo(),agentParentNode,mmi.getMerchantNo());
		if(mcount == 0){
			log.error("=====>迁移失败，商户[{}]修改商户代理商信息失败",mmi.getMerchantNo());
			throw new RuntimeException();
		}
		//迁移机具
		List<String> getMerchantTerminal = merchantMigrateDao.getMerchantTerminal(mmi.getMerchantNo());
		if(!getMerchantTerminal.isEmpty()){
			int tcount = merchantMigrateDao.updateMerchantTerminal(mmi.getMerchantNo(),mm.getNodeAgentNo(),agentParentNode);
			if(tcount == 0){
				log.error("=====>迁移失败，商户[{}]修改商户机具失败",mmi.getMerchantNo());
				throw new RuntimeException();
			}
		}
		//by ivan 2017/07/27 
		merchantMigrateDao.updateActivityAgentNode(mmi.getMerchantNo(),agentParentNode);
		//更新商户迁移状态
		int mmiCount = merchantMigrateDao.updateMerchantMigrateInfo("2", "跨一级代理商定时任务迁移", mmi.getId());
		if(mmiCount == 0){
			log.error("=====>迁移失败，商户[{}]修改商户迁移状态失败",mmi.getMerchantNo());
			throw new RuntimeException();
		}
		
		return mcount;
	}
	
	@Override
	public void migrate(List<String> merchantNos) {
		List<MerchantMigrate> mmList = merchantMigrateDao.migrate();//审核通过
		String agentApi2=sysDictDao.getValueByKey("agentApi2_url");
		String url =agentApi2+"/esDataMigrate/merchantMigrate";
		log.info("=====>转移一级代理商开始，本次转移共[{}]条主记录");
		if(!mmList.isEmpty()){
			for(MerchantMigrate mm: mmList){
				//是否修改一级代理商及未迁移
				List<MerchantMigrateInfo> mmInfoList = merchantMigrateDao.getMigrateInfoByStatus(String.valueOf(mm.getId()));
				if(mmInfoList.isEmpty()){
					continue;
				}
				AgentInfo agentInfo = merchantMigrateDao.getAgentInfo(mm.getNodeAgentNo());
				int migrateCount = 1;
				for(MerchantMigrateInfo mmi:mmInfoList){
					try{
						migrateMerchant(mm,mmi,agentInfo.getAgentNode());
						merchantNos.add(mmi.getMerchantNo());
						ClientInterface.merchantMigrate(url+"/"+mmi.getMerchantNo()+"/"+mmi.getBeforeNodeAgentNo());
					} catch (Exception e) {
						migrateCount++;
						log.error("=====>迁移商户[{}]出现错误：",mmi.getMerchantNo(),e.getMessage());
					}
				}
				if(migrateCount == 1){
					//迁移成功
					int ummCheckStatus = merchantMigrateDao.updateMerchantMirgateCheckStatus(4,mm.getId());
					if(ummCheckStatus == 0){
						log.error("=====>更新商户迁移主表[{}]状态失败",+mm.getId());
					}
					continue;
				}
				
				if(migrateCount > 1 && migrateCount<mmInfoList.size()){
					//部分成功
					int ummCheckStatus = merchantMigrateDao.updateMerchantMirgateCheckStatus(7,mm.getId());
					if(ummCheckStatus == 0){
						log.error("=====>更新商户迁移主表[{}]状态失败",+mm.getId());
					}
					continue;
				}
				if(migrateCount > 1 && migrateCount==mmInfoList.size()){
					//部分成功
					int ummCheckStatus = merchantMigrateDao.updateMerchantMirgateCheckStatus(6,mm.getId());
					if(ummCheckStatus == 0){
						log.error("=====>更新商户迁移主表[{}]状态失败",+mm.getId());
					}
					continue;
				}
				
			}
		}
	}
	
	@Override
	public List<MerchantMigrateInfo> getMerchantMigrateInfoAllList(String merchantMigrateId) {
		
		return merchantMigrateDao.getAllmerchantMigrateInfo(merchantMigrateId);
	}

	@Override
	public int cheXiaoMigrate(String mmId) {
		//add by tans 2017.4.6
		//撤销的同时，将子表的migrate_status状态置为已撤销
		int migrateStatus = 4;//子表的迁移状态：1.未迁移 2.已迁移 3.迁移失败 4.已撤销 5.审核失败 
		merchantMigrateDao.updateInfoBy(mmId, migrateStatus);
		return merchantMigrateDao.cheXiaoMigrate(mmId);
	}

	@Override
	public int merchantMigrateCheck(String check_status, String check_remark, String check_person,String id) {
		int count = 0;
		try {
			//add by tans 2017.4.6
			//审核失败的同时，将子表的migrate_status状态置为审核失败
			if("3".equals(check_status)){
				int migrateStatus = 5;//子表的迁移状态：1.未迁移 2.已迁移 3.迁移失败 4.已撤销 5.审核失败 
				merchantMigrateDao.updateInfoBy(id, migrateStatus);
			}
			count =merchantMigrateDao.merchantMigrateCheck(check_status, check_remark, check_person,id); 
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return count;
	}

	@Override
	public List<MerchantMigrateInfo> getMerchantMigrateInfoList(String merchantMigrateId, Page<MerchantMigrateInfo> page) {
		
		return merchantMigrateDao.getMerchantMigrateInfoList(merchantMigrateId,page);
	}

	@Override
	public MerchantMigrate getMerchantMigrateDetail(String id) {
		
		return merchantMigrateDao.getMerchantMigrateDetail(id);
	}

	@Override
	public int addMerchantMigrate(List<MerchantMigrateInfo> param, MerchantMigrate merchantMigrate,String agentParentNode) {
		merchantBusinessProductService.initSysConfigByKey();
		int count = 0;
		count = merchantMigrateDao.addMerchantMigrate(merchantMigrate);
		if(count == 0){
			throw new RuntimeException("提交失败，新增商户迁移失败");
		}
		String merchantNo = "";
		int mm_status = 0;
		StringBuffer metchantNos = new StringBuffer("");


		try {
			for (MerchantMigrateInfo mmi:param) {
				merchantNo = mmi.getMerchantNo();
				mmi.setMigrateId(merchantMigrate.getId());
				int countInfo = merchantMigrateDao.addMirgrateInfo(mmi);
				if(countInfo == 0){
					throw new RuntimeException("提交失败，新增商户迁移："+mmi.getMerchantNo()+" 失败");
				}
				
				if(mmi.getModifyAgentNo()==2){ //无需修改一级代理商
					// 修改商户所属代理商
					int mcount = merchantMigrateDao.modifyMerchant(merchantMigrate.getNodeAgentNo(), merchantMigrate.getAgentNo(), agentParentNode, mmi.getMerchantNo());
					if(mcount == 0){
						throw new RuntimeException("提交失败，迁移商户："+mmi.getMerchantNo()+" 失败");
					}
					List<String> getMerchantTerminal = merchantMigrateDao.getMerchantTerminal(mmi.getMerchantNo());
					if(!getMerchantTerminal.isEmpty()){
						int tcount = merchantMigrateDao.updateMerchantTerminal(mmi.getMerchantNo(),merchantMigrate.getNodeAgentNo(),agentParentNode);
						if(tcount == 0){
							throw new RuntimeException("提交失败，商户："+mmi.getMerchantNo()+" 修改机具失败");
						}
					}
					//agent_no=#{agentNo},agent_node=#{agentNode} 
					Map<String, Object> oneMap = new HashMap<>();
					Map<String, String> twoMap = new HashMap<>();
					oneMap.put("distinct_id", merchantBusinessProductDao.getUserIdByMerchantInfo(merchantNo));
					oneMap.put("time", new Date().getTime());
					oneMap.put("type", Constants.BEHAVIOUR_TYPE_PROFILE_SET);
					oneMap.put("project", Constants.BEHAVIOUR_SERVER_PROJECT);
					oneMap.put("properties", twoMap);
					twoMap.put("merchant_no", merchantNo);
					twoMap.put("agent_no", merchantMigrate.getNodeAgentNo());
					log.info("神策发送数据=======================>"+oneMap);
					merchantBusinessProductService.SendHttpSc(oneMap);
					
					
					//TODO 如有机具不随迁的需求 可在此处添加  清楚商户绑定机具
					int mmiCount = merchantMigrateDao.updateMerchantMigrateInfo("2", "未跨一级代理商，实时迁移", mmi.getId());
					if(mmiCount == 0){
						throw new RuntimeException("提交失败，修改商户："+mmi.getMerchantNo()+" 迁移状态失败");
					}
					//by ivan 2017/07/27 
					merchantMigrateDao.updateActivityAgentNode(mmi.getMerchantNo(),agentParentNode);

					String agentApi2=sysDictDao.getValueByKey("agentApi2_url");
					String url =agentApi2+"/esDataMigrate/merchantMigrate";
					url+="/"+merchantNo+"/"+mmi.getBeforeNodeAgentNo();
					ClientInterface.merchantMigrate(url);
				}else{
					mm_status++;
				}
				metchantNos.append("").append(merchantNo).append(",");
			}
			if(mm_status==0){ //更新迁移完成状态
				int ummCheckStatus = merchantMigrateDao.updateMerchantMirgateCheckStatus(4,merchantMigrate.getId());
				if(ummCheckStatus == 0){
					throw new RuntimeException("提交失败，更新商户迁移状态失败");
				}
			}
			//商户迁移迁移交易数据
			this.changeCollectiveTransOrder(StringUtil.delLastChar(metchantNos.toString()),agentParentNode);
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			throw new RuntimeException("提交失败，商户 "+merchantNo+" 重复或已存在");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("提交失败，系统出现未知错误，请联系管理员");
		}
		
		return count;
	}

	@Override
	public MerchantMigrateInfo findMerchantMigrateInfo(String merchantNo) {
		
		return merchantMigrateDao.findMerchantMigrateInfoByNo(merchantNo);
	}
	
	@Override
	public List<String> findMerchantBusProduct(String merchantNo) {
		// TODO Auto-generated method stub
		return merchantMigrateDao.findMerchantBusProduct(merchantNo);
	}

	@Override
	public List<String> findAgentBusProduct(String agentNo) {
		
		return merchantMigrateDao.findAgentBusProduct(agentNo);
	}

	@Override
	public MerchantInfo getMerchant(String merchantNo) {
		// TODO Auto-generated method stub
		return merchantInfoDao.selectByMerNo(merchantNo);
	}

	@Override
	public AgentInfo getAgentInfoByNo(String agentNo) {
		// TODO Auto-generated method stub
		return agentInfoDao.selectByAgentNo(agentNo);
	}

	@Override
	public List<MerchantMigrate> findMerchantMigrate(Page<MerchantMigrate> page, MerchantMigrate merchantMigrate) {
		// TODO Auto-generated method stub
		return merchantMigrateDao.findMerchantMigrate(page,merchantMigrate);
	}

	@Override
	public int modifyMerchantMigrate(Map<String, String> param) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public List<AgentInfo> getAgentInfo() {
		List<AgentInfo> map = new ArrayList<AgentInfo>();
		map = merchantMigrateDao.getAllAgentInfo();
		return map;
	}

	@Override
	public List<AgentInfo> findNodeAgent(String agentNo) {
		List<AgentInfo> map = new ArrayList<AgentInfo>();
		System.out.println(agentNo);
		try {
			map = merchantMigrateDao.getNodeAgent(agentNo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public void scBymerchantNos(List<String> merchantNos) {
		StringBuffer sb = new StringBuffer();
		for (String str : merchantNos) {
			sb.append(str);
		}
		
		log.info("神策商户迁移开始启动================>:"+sb.toString());
		for (String merchantNo : merchantNos) {
			try {
				this.sc(merchantNo);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	@Override
	public void changeCollectiveTransOrder(String merchantNos, String agentNode) throws Exception{
		if(StringUtils.isEmpty(merchantNos)){
			throw new RuntimeException("商户编号为空");
		}
		if(StringUtils.isEmpty(agentNode)){
			throw new RuntimeException("目标直属代理商为空");
		}
		merchantMigrateDao.changeCollectiveTransOrder(merchantNos,agentNode);
	}


	//
	private void sc(String merchantNo) throws Exception{
		
		merchantBusinessProductService.initSysConfigByKey();
		
		//商户
		MerchantInfo merchantInfo = merchantInfoDao.selectByMerchantNoAll(merchantNo);
		
		Map<String, Object> oneMap = new HashMap<>();
		Map<String, String> twoMap = new HashMap<>();

		oneMap.put("distinct_id", merchantBusinessProductDao.getUserIdByMerchantInfo(merchantInfo.getMerchantNo()));
		
		oneMap.put("time", new Date().getTime());
		oneMap.put("type", Constants.BEHAVIOUR_TYPE_PROFILE_SET);
		oneMap.put("project", Constants.BEHAVIOUR_SERVER_PROJECT);
		oneMap.put("properties", twoMap);

		twoMap.put("merchant_no", merchantInfo.getMerchantNo());
		
		//根据身份证号分析
		String idCardNo = merchantInfo.getIdCardNo();
		String sex = "";
		// 获取性别
		String id17 = idCardNo.substring(16, 17);
		if (Integer.parseInt(id17) % 2 != 0) {
			sex = "男";
		} else {
			sex = "女";
		}
		String birthday = idCardNo.substring(6, 14);
		Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		twoMap.put("gender", sex);
		twoMap.put("age", df.format(birthdate));
		
		//twoMap.put("registration_time", merchantInfo.getCreateTime().getTime()+"");
		//twoMap.put("check_time", "");
		twoMap.put("agent_no", merchantInfo.getAgentNo());
		
		//0：商户关闭；1：正常；2 冻结'
		twoMap.put("merchant_status", merchantInfo.getStatus());
		
		twoMap.put("sales", merchantInfo.getOneSaleName());
		twoMap.put("first_level_agent_no", merchantInfo.getOneAgentNo());
		
		//$scope.itemSources = [{text:"商户APP",value:"1"},{text:"代理商APP",value:"2"},{text:"代理商WEB",value:"3"},{text:"关联进件系统自动开通",value:"4"},{text:"还款系统",value:"5"}];
		
		List<MerchantBusinessProduct> merchantBusinessProductList = merchantBusinessProductDao.selectByMerchantNoAll(merchantNo);
		int j=1;
		for (int i = 0; i < merchantBusinessProductList.size(); i++) {
			twoMap.put("business_product1", merchantBusinessProductList.get(i).getBpName());
			
			Page<TerminalInfo> tiPage = new Page<TerminalInfo>();
			List<TerminalInfo> terminalInfos = terminalInfoDao.selectAllInfoBymerNoAndBpId(merchantBusinessProductList.get(i).getMerchantNo(), merchantBusinessProductList.get(i).getBpId(), tiPage);
			for (TerminalInfo terminalInfo : terminalInfos) {
				twoMap.put("product_type"+j, terminalInfo.getTypeName());
				j++;
			}
		}
	
		twoMap.put("orgnize_id", merchantInfo.getTeamId());
		twoMap.put("user_type", "商户");
		
		log.info("神策发送数据=======================>"+oneMap);
		merchantBusinessProductService.SendHttpSc(oneMap);
	}

}
