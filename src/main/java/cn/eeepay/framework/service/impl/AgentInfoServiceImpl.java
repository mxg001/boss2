package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.allAgent.UserAllAgentService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("agentInfoService")
@Transactional
public class AgentInfoServiceImpl implements AgentInfoService{
	private static final Logger log = LoggerFactory.getLogger(AgentInfoServiceImpl.class);
	private static final Pattern pattern=Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)$");
	//	private static final DecimalFormat format=new java.text.DecimalFormat("0.00");
	@Resource
	private AgentInfoDao agentInfoDao;
	@Resource
	private AgentBusinessProductDao agentBusinessProductDao;
	@Resource
	private BusinessProductDefineDao businessProductDefineDao;
	@Resource
	private ServiceDao serviceDao;
	@Resource
	private UserDao userDao;
	@Resource
	private ServiceProService serviceProService;
	@Resource
	private SeqService seqService;
	@Resource
	private AgentShareDao agentShareDao;
	@Resource
	private AgentInfoService agentInfoService;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private OneAgentOemService oneAgentOemService;
	@Resource
	private UserAllAgentService userAllAgentService;
	@Resource
	private OpenPlatformService openPlatformService;
	@Resource
	private BusinessProductDefineService businessProductDefineService;
	@Resource
	private VasInfoService vasInfoService;

	@Resource
	private MsgService msgService;

	@Resource
	private RiskRollService riskRollService;

	@Override
	public AgentInfo selectByName(String name) {
		return agentInfoDao.selectByName(name);
	}

	@Override
	public List<AgentInfo> selectAllAgentInfo(String item, int agentLevel) {
		return agentInfoDao.selectAllAgentInfo(item, agentLevel);
	}
	@Override
	public List<AgentInfo> selectAllAgentInfo(String item,String sale_name, int agentLevel) {
		return agentInfoDao.selectAllAgentInfo2(item, sale_name, agentLevel);
	}

	@Override
	public List<AgentInfo> getAllAgentListByParent(String item, String parentAgentNo) {
		AgentInfo parentAgentInfo=agentInfoDao.selectByAgentNo(parentAgentNo);
		return agentInfoDao.getAllAgentListByParent(item, parentAgentInfo.getAgentNode(),0);
	}

	@Override
	public List<AgentInfo> selectByLevelOne() {
		return agentInfoDao.selectByLevelOne();
	}

	@Override
	public List<AgentInfo> selectLevelOneAgentWithNoAccount() {
		return agentInfoDao.selectLevelOneAgentWithNoAccount();
	}

	@Override
	public AgentInfo selectByagentNo(String agentNo) {
		AgentInfo agent=agentInfoDao.selectByAgentNo(agentNo);
		if(agent!=null&&StringUtils.isNotBlank(agent.getClientLogo())){
			String logo=ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, agent.getClientLogo(),new Date(new Date().getTime()+3600000));
			agent.setClientLogoLink(logo);
		}
		if(agent!=null&&StringUtils.isNotBlank(agent.getManagerLogo())){
			String logo=ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, agent.getManagerLogo(),new Date(new Date().getTime()+3600000));
			agent.setManagerLogoLink(logo);
		}
		return agent;
	}

	@Override
	public AgentInfo getAgentByNo(String agentNo) {
		AgentInfo agent=agentInfoDao.selectByAgentNo(agentNo);
		return agent;
	}

	/**
	 * 根据代理商名称
	 * 模糊查询出一级代理商的名称及编号
	 */
	public List<AgentInfo> selectOneAgentByName(String agentName){
		agentName = StringUtils.isBlank(agentName)?"":agentName+"%";
		return agentInfoDao.selectOneAgentByName(agentName);
	}

	/**
	 * 根据代理商编号查代理商信息和组织信息
	 * @param agentNo
	 * @return
	 */
	public Map<String, Object> selectAgentTeamByAgentNo(String agentNo){
		return agentInfoDao.selectAgentTeamByAgentNo(agentNo);
	}
	
	/**
	 * 根据代理商ID，查询相关的业务产品
	 */
	public List<BusinessProductDefine> selectProductById(String id) {
		List<BusinessProductDefine> list = null;
		if(id == null || "null".equals(id) || "0".equals(id)){
			list = businessProductDefineDao.selectBpTeam();
			return list;
		}
		list =  businessProductDefineDao.getProductsByAgent(id);
		return list;
	}

	/**
	 * 通过组织过滤出业务产品
	 * @param agentNo
	 */
	@Override
	public List<JoinTable> selectProductByTeamId(Integer id, String agentNo) {
		Map<String,Object> map = new HashMap<>();
		map.put("teamId", id);
		map.put("agentNo", agentNo);
		return businessProductDefineDao.getProducesByCondition(map);
	}

	@Override
	public List<JoinTable> selectProductByAgentNoBpId(String bpId, String agentNo) {
		Map<String,Object> map = new HashMap<>();
		map.put("bpId", bpId);
		map.put("agentNo", agentNo);
		return businessProductDefineDao.getProducesByCondition(map);
	}

	@Override
	public Map<String,Object> getAgentServices(Map<String, Object> json) {
		Map<String,Object> result=new HashMap<>();
		try {
			String agentId=(String)json.get("agentId");
			@SuppressWarnings("unchecked")
			List<Integer> ids=(List<Integer>)json.get("bpIds");
			if(StringUtils.isNotBlank(agentId)){
				result.put("profits",this.getProfits(ids, agentId));
				result.put("rates",this.getServiceRate(ids, agentId));
				result.put("quotas",this.getServiceQuota(ids, agentId));
			}

		} catch (Exception e) {
			log.error("查询代理商的代理业务产品对应的所有的服务费率和服务额度异常！",e);
		}
		return result;
	}

	/**
	 * 查询出代理商的费率
	 */
	@Override
	public List<ServiceRate> getServiceRate(List<Integer> bpIds, String agentId) {
		List<ServiceRate> list=agentInfoDao.getServiceRate(bpIds,agentId);
		for(ServiceRate r:list){
			r.setMerRate(serviceProService.profitExpression(r));
		}
		return list;
	}

	/**
	 * 查询出代理商的额度
	 */
	@Override
	public List<ServiceQuota> getServiceQuota(List<Integer> bpIds, String agentId) {
		return agentInfoDao.getServiceQuota(bpIds,agentId);
	}

	/**
	 * 保存一级
	 */
	@Override
	public AgentInfo saveAgentInfo(JSONObject json) {
//		String msg=null;
		AgentInfo agent=JSONObject.parseObject(json.getString("agentInfo"), AgentInfo.class);
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		agent.setCreator(principal.getId().toString());
		String agentNo=seqService.createKey("agent_no");
		agent.setAgentNo(agentNo);
		agent.setAgentNode("0-" + agentNo + "-");
		agent.setAgentLevel(1);
		agent.setParentId("0");
		agent.setOneLevelId(agentNo);
		agent.setStatus("1");
		agent.setProfitSwitch(1);//默认分润开关为开
		agent.setCashBackSwitch(1);//一级代理商的返现开关都为开
		agent.setFullPrizeSwitch(1);
		agent.setNotFullDeductSwitch(1);
		//edit by tans	2017-4-13
		//代理商默认组织为999
		agent.setTeamId(Constants.AGENT_TEAM_ID);
//		if(agent != null){
//			if(agent.getTeamId()==100010){
//				agent.setIsOem(0);
//			} else if(agent.getTeamId()==200010)
//				agent.setIsOem(1);
//		}
		if(agent.getAgentName()!=null&&StringUtil.isSpecialChar(agent.getAgentName())){
			throw new RuntimeException("名称不能包含以下字符+/?\\$&';#=:");
		}
		if(agent.getAgentType()==null||"".equals(agent.getAgentType())){
			throw new RuntimeException("代理商类型不能为空!");
		}
		if("11".equals(agent.getAgentType())){
			agent.setTeamId(998);
			if(agent.getAgentOem()==null||"".equals(agent.getAgentOem())){
				throw new RuntimeException("超级盟主所属品牌不能为空!");
			}
			if(agent.getPhone()==null||"".equals(agent.getPhone())){
				throw new RuntimeException("超级盟主联系电话不能为空!");
			}
			if(agent.getIdCardNo()==null||"".equals(agent.getIdCardNo())){
				throw new RuntimeException("超级盟主身份证号不能为空!");
			}
			if(agent.getAgentShareLevel()==null||"".equals(agent.getAgentShareLevel())){
				throw new RuntimeException("超级盟主交易分润等级不能为空!");
			}
			if(StringUtils.isBlank(agent.getAgentOem())){
				throw new RuntimeException("所属品牌不能为空!");
			}
		}

		//判断身份证号 开户账号 手机号是否在黑名单中
		String riskMsg = "";
		RiskRoll riskRoll3 = riskRollService.checkRollByRollNo(agent.getMobilephone(), 10, 2);
		if(riskRoll3!=null && riskRoll3.getRollStatus()!=null && riskRoll3.getRollStatus().intValue()==1){
			Msg msg = msgService.queryMsgByMsgCode(riskRoll3.getRollMsg());
			if(msg!=null){
				riskMsg += msg.getUserMsg()+"\r\n";
			}
		}
		RiskRoll riskRoll1 = riskRollService.checkRollByRollNo(agent.getIdCardNo(), 8, 2);
		if(riskRoll1!=null && riskRoll1.getRollStatus()!=null && riskRoll1.getRollStatus().intValue()==1){
			Msg msg = msgService.queryMsgByMsgCode(riskRoll1.getRollMsg());
			if(msg!=null){
				riskMsg += msg.getUserMsg()+"\r\n";
			}
		}
		RiskRoll riskRoll2 = riskRollService.checkRollByRollNo(agent.getAccountNo(), 9, 2);
		if(riskRoll2!=null && riskRoll2.getRollStatus()!=null && riskRoll2.getRollStatus().intValue()==1){
			Msg msg = msgService.queryMsgByMsgCode(riskRoll2.getRollMsg());
			if(msg!=null){
				riskMsg += msg.getUserMsg()+"\r\n";
			}
		}

		if(StringUtil.isNotBlank(riskMsg)){
			throw new RuntimeException(riskMsg);
		}

		if(agent.getAccountType()==2){
			Map<String, String> maps=openPlatformService.doAuthen(agent.getAccountNo(),agent.getAccountName(),agent.getIdCardNo(),null);
			boolean flag = "00".equalsIgnoreCase(maps.get("errCode"));
			if (!flag) {
				log.info("身份证验证失败");
				throw new RuntimeException("开户名、身份证、银行卡号不匹配!");
			}
		}
		//支付收单时，agentOem也不能为空
		if("101".equals(agent.getAgentType())){
			if(StringUtils.isBlank(agent.getAgentOem())){
				throw new RuntimeException("所属品牌不能为空!");
			}
		}



		int count = agentInfoDao.existAgentByMobilephoneAndTeamId(agent);
		if(count>0)
			throw new RuntimeException("该组织下的手机号码或者邮箱或者代理商名称已存在!");
		int num=agentInfoDao.insertAgentInfo(agent);
		if(num<1)
			throw new RuntimeException("保存一级代理商失败！");
		if(json.getJSONArray("bpData").isEmpty()){
			throw new RuntimeException("请选择业务产品！");
		}

		List<Integer> bps=JSONArray.parseArray(json.getString("bpData"),Integer.class);
		List<JoinTable> bpList=new ArrayList<>();
		for(Integer id:bps){
			JoinTable product=new JoinTable();
			product.setKey1(id);
			product.setKey2(1);
			product.setKey3(agentNo);
			bpList.add(product);
		}
		agentInfoDao.insertAgentProductList(bpList);

		//设置代理商业务产品的默认值
		//1.如果是自定义业务产品，队长就是默认的(一个群组内，只会有一个可以单独申请的业务产品，称之为队长)
		//2.如果是非自定义业务产品，本身就是默认的
		agentBusinessProductDao.updateDefaultBp(bps, agentNo);

		//保存费率
		//当勾选“与公司管控费率相同时”，根据那些选中的行从数据库取值并存入（从页面获取ID，再从数据库取值）
		//没有勾选的行，将其保存（从页面获取所有数据）
		//限额同理
		List<ServiceRate> rateList=JSONArray.parseArray(json.getString("rateData"), ServiceRate.class);
		List<ServiceRate> rateList2 = new ArrayList<>();
		List<ServiceRate> rateList3 = new ArrayList<>();
		for(ServiceRate rate:rateList){
			if(rate.getIsGlobal()==1){
				rateList3.add(rate);
			}else {
				rate.setAgentNo(agent.getAgentNo());
				rate.setCheckStatus(0);
				rate.setLockStatus(0);
				serviceProService.setServiceRate(rate,false);
				rateList2.add(rate);
			}
		}

		if(rateList3.size()>0){
			for(ServiceRate rate: rateList3){
                ServiceRate itemRate = serviceDao.getServiceRateByRate(rate);
                itemRate.setAgentNo(agent.getAgentNo());
                itemRate.setCheckStatus(0);
                itemRate.setLockStatus(0);
                rateList2.add(itemRate);
//				rateList4.add(serviceDao.getServiceRateByRate(rate));
			}
		}
		serviceProService.insertServiceRateList(rateList2);
		List<ServiceQuota> quotaList=JSONArray.parseArray(json.getString("quotaData"), ServiceQuota.class);
		List<ServiceQuota> quotaList2 = new ArrayList<>();
		List<ServiceQuota> quotaList3 = new ArrayList<>();
		for(ServiceQuota quota:quotaList){
			if(quota.getIsGlobal()==1){
				quotaList3.add(quota);
			} else {
				quota.setCheckStatus(0);
				quota.setLockStatus(0);
				quota.setAgentNo(agent.getAgentNo());
				quotaList2.add(quota);
			}
		}
		if(quotaList3.size()>0){
			for(ServiceQuota quota: quotaList3){
                ServiceQuota itemRate = serviceDao.getServiceQuotaByQuota(quota);
                itemRate.setCheckStatus(0);
                itemRate.setLockStatus(0);
                itemRate.setAgentNo(agent.getAgentNo());
                quotaList2.add(itemRate);
//				quotaList4.add(serviceDao.getServiceQuotaByQuota(quota));
			}

		}
		serviceProService.insertServiceQuotaList(quotaList2);

		List<AgentShareRule> shareList=JSONArray.parseArray(json.getString("shareData"), AgentShareRule.class);
		List<AgentShareRule> totalShareList = new ArrayList<>();
		for(AgentShareRule rule:shareList){
			//不是提现服务的，代理商成本需要加上%
			if(rule.getServiceType()!=null && rule.getServiceType()!=10000 && rule.getServiceType()!=10001 ){
				if(rule.getCost()!=null && !rule.getCost().contains("%")){
					rule.setCost(rule.getCost()+"%");
				}
			}
			setShareRuleAdd(rule);
			//找到同组的最低费率
			rule.setAgentNo(agentNo);
			ServiceRate minRate = queryMinServiceRate(rule);
			//比较rule和rate，rule不能大于rate
			compareRuleRate(rule, minRate);
			rule.setAgentNo(agentNo);
			rule.setCheckStatus(0);
			rule.setLockStatus(0);
			totalShareList.add(rule);
			//找到同组的业务产品的服务集合
			List<ServiceRate> serviceRateList = new ArrayList<>();
			//不是提现服务的查询方法
			if(rule.getServiceType()!=null && rule.getServiceType()!=10000 && rule.getServiceType()!=10001 ){
				serviceRateList = agentInfoDao.getOtherServiceRate(rule);
			} else {
				//如果是提现服务，需要从主服务开始查，主服务可以确定唯一（同一业务产品只能关联服务类型不一样的服务，关联提现服务除外）
				serviceRateList = agentInfoDao.getOtherTXServiceRate(rule);
			}

			log.info("当前的分润：serviceId:{},serviceType:{},cardType:{},holidayMark:{}",
					rule.getServiceId(),rule.getServiceType(),rule.getCardType(),rule.getHolidaysMark());
			log.info("SQL查询出对应的分润：{}",JSONObject.toJSONString(serviceRateList));
			if(serviceRateList!=null && !serviceRateList.isEmpty()){
				for(ServiceRate serviceRate: serviceRateList){
					AgentShareRule copyRule = new AgentShareRule();
					BeanUtils.copyProperties(rule, copyRule);
					copyRule.setServiceId(String.valueOf(serviceRate.getServiceId()));
					totalShareList.add(copyRule);
				}
			}
		}
		log.info("新增代理商时,需要保存的全部分润数据:{}",JSONObject.toJSONString(totalShareList));
		agentInfoDao.insertAgentShareList(totalShareList);

		//add by tans 2017.4.13
		//验证user_info表，组织与手机号唯一，或者组织与邮箱唯一
		AgentUserInfo userInfo=new AgentUserInfo();
		userInfo.setUserName(agent.getAgentName());
		userInfo.setMobilephone(agent.getMobilephone());
		userInfo.setEmail(agent.getEmail());
		int countAgentUser = agentInfoDao.existUserInfo(userInfo);
		if(countAgentUser>0){
			throw new RuntimeException("该组织下手机号或者邮箱已存在");
		}
		//创建代理商管理员
		AgentUserInfo agentUser=agentInfoDao.selectAgentUser(agent.getMobilephone(),agent.getEmail(),agent.getTeamId().toString());
		if(agentUser==null){
			agentUser=new AgentUserInfo();
			agentUser.setUserName(agent.getAgentName());
			String userId=seqService.createKey(Constants.AGENT_USER_SEQ, new BigInteger("1000000000000000000"));
			agentUser.setUserId(userId);
			agentUser.setEmail(agent.getEmail());
			agentUser.setTeamId(agent.getTeamId().toString());
			agentUser.setMobilephone(agent.getMobilephone());
			agentUser.setPassword(new Md5PasswordEncoder().encodePassword("123456",agent.getMobilephone()));
			agentInfoDao.insertAgentUser(agentUser);
		} else {
			throw new RuntimeException("该组织下的手机号码或者邮箱已存在!");
		}
		AgentUserEntity entity=agentInfoDao.selectAgentUserEntity(agentUser.getUserId(),agent.getAgentNo());
		if(entity==null){
			Map params=new HashMap();
			params.put("agent_type",agent.getAgentType());
			params.put("agent_oem",agent.getAgentOem());
			Map map=agentInfoDao.selectAgentRoleOem(params);
			if(map==null){//默认支付收单-盛钱包
				params.put("agent_type","101");
				params.put("agent_oem","200010");
				map=agentInfoDao.selectAgentRoleOem(params);
			}
			entity=new AgentUserEntity();
			entity.setEntityId(agent.getAgentNo());
			entity.setUserId(agentUser.getUserId());
			entity.setIsAgent("1");
			agentInfoDao.insertAgentEntity(entity);
			agentInfoDao.insertAgentRole(entity.getId(),Long.valueOf(map.get("role_id").toString()));
		}else{
			throw new RuntimeException("代理商手机号已注册");
		}
		//保存代理商参加的欢乐返子类型-mays-2018.5.15-start
		List<ActivityHardwareType> happyBackTypes = JSONArray.parseArray(json.getString("happyBackTypes"), ActivityHardwareType.class);
		List<ActivityHardwareType> newHappyBackTypes = JSONArray.parseArray(json.getString("newHappyBackTypes"), ActivityHardwareType.class);
		List<ActivityHardwareType> superHappyBackTypes = JSONArray.parseArray(json.getString("superHappyBackTypes"), ActivityHardwareType.class);

		saveHlfTypes(agent, happyBackTypes, newHappyBackTypes,superHappyBackTypes);
		//保存代理商参加的欢乐返子类型-mays-2018.5.15-end

		if(agent.getAgentType()!=null&&"11".equals(agent.getAgentType())){
			//超级盟主注册机构代理商
			String url=sysDictService.getValueByKey("ALLAGENT_SERVICE_URL");
			url+="/user/toReg";
			String accountMsg = ClientInterface.allAgentUserToReg(agent,url);
			JSONObject jsonObject = JSONObject.parseObject(accountMsg);
			if (jsonObject.getInteger("status")!=200) {
				throw new RuntimeException(jsonObject.getString("msg"));
			}
			userAllAgentService.saveUserCardAllAgent(agent);
			oneAgentOemService.insertOneAgentNo(agent.getAgentNo(),"11");
		}

		//处理增值分润相关数据
		vasInfoService.checkVasShareRule(agentNo);



		//开设代理商账户
		try{
			String acc=ClientInterface.createAgentAccount(agent.getAgentNo());
			if(JSONObject.parseObject(acc).getBooleanValue("status")){
				agentInfoDao.updateAgentAccount(agentNo, 1);
			}else
				log.info("开立代理商账户失败：{}" ,acc);
		}catch(Exception e){
			log.error("开立代理商账户异常",e);
		}
//		msg = "添加代理商成功";
		return agent;
	}

	/**
	 * 保存欢乐返子类型
	 * @param agent
	 * @param happyBackTypes
	 * @param newHappyBackTypes
	 */
	private void saveHlfTypes(AgentInfo agent, List<ActivityHardwareType> happyBackTypes, List<ActivityHardwareType> newHappyBackTypes, List<ActivityHardwareType> superHappyBackTypes) {
		List<ActivityHardwareType> happyBackTypesAllList = new ArrayList<>();
		happyBackTypesAllList.addAll(happyBackTypes);
		happyBackTypesAllList.addAll(newHappyBackTypes);
		happyBackTypesAllList.addAll(superHappyBackTypes);
		if(happyBackTypesAllList !=null && happyBackTypesAllList.size()>0){
			List<ActivityHardwareType> insertList = new ArrayList<>();
			Map<String, String> map = new HashMap<>();
			// 新增代理商欢乐返活动按组织分组需求 去重
			for (ActivityHardwareType aht : happyBackTypesAllList) {
				String activityTypeNo = aht.getActivityTypeNo();
				if(StringUtils.isNotBlank(map.get(activityTypeNo))){
					continue;
				}
				aht.setAgentNo(agent.getAgentNo());
				aht.setAgentNode(agent.getAgentNode());
				aht.setTaxRate(BigDecimal.valueOf(1));
				aht.setRepeatRegisterRatio(BigDecimal.valueOf(1));
				aht.setOneSubWhenIsNull();
				insertList.add(aht);
				map.put(activityTypeNo, activityTypeNo);
			}

			int res = agentInfoDao.insertHappyBackType(insertList);
			if (res != insertList.size()) {
				throw new RuntimeException("保存代理商参加的欢乐返子类型失败");
			}
		}
	}

	public void compareRuleRate(AgentShareRule rule, ServiceRate rate) {
		//如果不是提现服务
		if(rate.getIsTx()==0){
			//如果分润大于
			if(rule.getCostRate() == null){
				throw new RuntimeException(rate.getBpName()+"-"+rate.getServiceName()
						+"分润必须是百分比");
			}
			if(rate.getRate() == null){
				throw new RuntimeException(rate.getBpName()+"-"+rate.getServiceName()
						+"费率必须是百分比");
			}
			if(rule.getCostRate().compareTo(rate.getRate())>0){
				throw new RuntimeException(rate.getBpName()+"-"+rate.getServiceName()
						+"分润底价不能大于商户手续费" + rate.getRate() + "%");
			}
		}
		//如果是提现服务
		if(rate.getIsTx()==1){
			//如果分润大于
			if(rule.getPerFixCost() == null){
				throw new RuntimeException(rate.getBpName()+"-"+rate.getServiceName()
						+"分润必须是固定金额");
			}
			if(rate.getSingleNumAmount() == null){
				throw new RuntimeException(rate.getBpName()+"-"+rate.getServiceName()
						+"费率必须是固定金额");
			}
			if(rule.getPerFixCost().compareTo(rate.getSingleNumAmount())>0){
				throw new RuntimeException(rate.getBpName()+"-"+rate.getServiceName()
						+"分润底价不能大于商户手续费" + rate.getSingleNumAmount());
			}
		}
		if(rule.getCostRateType().equals("5")){
			String temp=rule.getCostCapping().setScale(2).toString();
			log.info("-------------封顶手续费"+ temp);
			if(StringUtils.isBlank(temp)||!temp.matches("\\d+(\\.\\d{1,2})?")){
				throw new RuntimeException("封顶手续费仅可设置为≥0，小数点最多仅能输入两位小数点");
			}
			if(rule.getCostCapping().compareTo(rate.getCapping())>0){
				throw new RuntimeException("封顶手续费需≤"+rate.getCapping()+"元；其中"+rate.getCapping()+"元为商户封顶手续费金额");
			}
		}
	}

	@Override
	public Page<AgentInfo> queryAgentInfoList(Map<String, Object> params, Page<AgentInfo> page) {
		if(StringUtils.isNotBlank((String)params.get("agentNo")) && "1".equals(params.get("hasSub").toString())){
			AgentInfo info=this.selectByagentNo((String)params.get("agentNo"));
			if(info!=null&&StringUtils.isNotBlank(info.getAgentNode())){
				params.put("subAgentNo", info.getAgentNode()+"%");
			} else {
				return page;
			}
		}
		agentInfoDao.queryAgentInfoList(params,page);
		List<AgentInfo> result = page.getResult();
		for (AgentInfo agentInfo : result) {
			agentInfo.setIdCardNo(StringUtil.sensitiveInformationHandle(agentInfo.getIdCardNo(),1));
			agentInfo.setAccountNo(StringUtil.sensitiveInformationHandle(agentInfo.getAccountNo(),4));
		}
		return page;
	}

	@Override
	public Map<String,Object> queryAgentProducts(String agentNo,Integer teamId) {
		AgentInfo info=this.selectByagentNo(agentNo);
		Map<String,Object> map=new HashMap<>();
		if(info!=null){
			if(1==info.getAgentLevel()){
				//一级代理商
				map.put("parentProducts", this.selectProductByTeamId(info.getTeamId(),agentNo));
			}else{
				//查询父级代理商业务产品
				map.put("parentProducts",businessProductDefineDao.getAgentProducts(info.getParentId()));
			}
			if(info.getTeamId().compareTo(teamId)==0)//组织是否变更
				map.put("agentProducts",businessProductDefineDao.getAgentProducts(info.getAgentNo()));
		}
		return map;
	}

	@Override
	public Map<String, Object> queryAgentInfoDetail(String agentNo, Integer teamId,boolean isAllProducts) {
		Map<String,Object> map=new HashMap<>();
		AgentInfo info=this.selectByagentNo(agentNo);
		if(info!=null){
			map.put("agentInfo", info);
//			查询业务产品
			List<JoinTable> bps=businessProductDefineDao.getAgentProducts(info.getAgentNo());
			map.put("agentProducts",bps);
//			查询上级的所有的业务产品
			if(isAllProducts)
				map.put("parentProducts", this.selectProductByTeamId(null,agentNo));
//			查询代理商的分润信息
			map.put("agentShare",this.getAgentShareInfos(agentNo));
//			查询代理商的费率
			List<Integer> bpIds=new ArrayList<>();
			for(JoinTable t:bps){
				bpIds.add(t.getKey1());
			}
			map.put("agentRate",this.getServiceRate(bpIds, agentNo));
//			查询代理商的额度
			map.put("agentQuota", agentInfoDao.getServiceQuota(bpIds, agentNo));
			//查询代理商参加的欢乐返子类型
			//map.put("hbTypes", agentInfoDao.selectHappyBackTypeByAgentNo(agentNo));

			// 代理商管理优化-按组织分组
			List<String> ids = new ArrayList<>();
			for (JoinTable bp : bps) {
				ids.add(bp.getKey1()+"");
			}
			List<String> teamIds = businessProductDefineService.selectTeamIdsWithBpIds(ids);
			List<ActivityHardwareType> hbTypes = agentInfoDao.selectHappyBackWithTeamIdAndAgentNo(teamIds, agentNo);
			List<ActivityHardwareType> hlfList = new ArrayList<>();
			if(hbTypes != null && hbTypes.size() >0) {
				for(ActivityHardwareType item: hbTypes) {
					if(StringUtils.isNotEmpty(item.getSubType()) && "1".equals(item.getSubType())) {
                        hlfList.add(item);
					}
				}
			}
            List<ActivityHardwareType> xhlfList = agentInfoDao.selectHappyBackTypeByAgentNoCodeSubType(agentNo,"009","2");
			List<ActivityHardwareType> superList = agentInfoDao.selectHappyBackTypeByAgentNoCodeSubType(agentNo,"009","3");

			map.put("hbTypes", hlfList);
			map.put("xhlfList", xhlfList);
			map.put("superList", superList);

		}
		return map;
	}

	@Override
	public Map<String, Object> queryAgentInfoAudit(String agentNo, Integer teamId,boolean isAllProducts) {
		Map<String,Object> map=new HashMap<>();
		AgentInfo info=this.selectByagentNo(agentNo);
		if(info!=null){
			map.put("agentInfo", info);
//			查询业务产品
			List<JoinTable> bps=businessProductDefineDao.getAgentProducts(info.getAgentNo());
			map.put("agentProducts",bps);
//			查询上级的所有的业务产品
			if(isAllProducts)
				map.put("parentProducts", this.selectProductByTeamId(info.getTeamId(),agentNo));
//			查询代理商的分润信息
			List<AgentShareRule> list3 = new ArrayList<>();
			List<AgentShareRule> list=agentInfoDao.getAgentShareInfos(agentNo);
			for(AgentShareRule rule: list){
				if(rule.getCheckStatus()==1){
					rule.setEffectiveStatus(1);
				}else {
					rule.setEffectiveStatus(0);
				}
				if(rule.getCheckStatus()==0){
					list3.add(rule);
				}
				List<AgentShareRule> list2 = agentInfoDao.getAgentShareRuleTaskByRule(rule);
				for(AgentShareRule rule2: list2){
					rule2.setServiceName(rule.getServiceName());
					rule2.setCardType(rule.getCardType());
					rule2.setHolidaysMark(rule.getHolidaysMark());
				}
				list3.addAll(list2);
			}
			for(AgentShareRule r:list3)
				this.profitExpression(r);
			map.put("agentShare",list3);
//			查询代理商的费率
			List<Integer> bpIds=new ArrayList<>();
			for(JoinTable t:bps){
				bpIds.add(t.getKey1());
			}
			map.put("agentRate",this.getServiceRate(bpIds, agentNo));
//			查询代理商的额度
			map.put("agentQuota", agentInfoDao.getServiceQuota(bpIds, agentNo));

		}
		return map;
	}

	@Override
	public Map<String, Object> getNewAgentServices(Map<String, Object> map) {
		Map<String,Object> result=new HashMap<String,Object>();
		try {
			String agentId=(String)map.get("agentNo");
			@SuppressWarnings("unchecked")
			List<Integer> ids=(List<Integer>)map.get("bpIds");
			if(StringUtils.isNotBlank(agentId)){
				List<ServiceRate> list=agentInfoDao.getNewServiceRate(ids, agentId);
				for(ServiceRate r:list){
					r.setMerRate(serviceProService.profitExpression(r));
				}
				result.put("rates",list);
				result.put("quotas",agentInfoDao.getNewServiceQuota(ids, agentId));
			}
		} catch (Exception e) {
			log.error("查询代理商的代理业务产品对应的所有的服务费率和服务额度异常！",e);
		}
		return result;
	}

	/**
	 * 通过所有的代理商查询出所有分润信息
	 */
	@Override
	public List<AgentShareRule> getAgentShareInfos(String agentNo) {
		List<AgentShareRule> list=agentInfoDao.getAgentShareInfos(agentNo);
		for(AgentShareRule r:list){
			this.profitExpression(r);
			String str = r.getUncheckStatus();
			String[] strArr = str.split(",");
			r.setUncheckStatus("待审核："+strArr[0]+","+"审核不通过："+strArr[1]);

			//判断是否是从默认分润带过来的,是就1,否就0
			//r.setIsGlobal(1);
		}
		return list;
	}

	@Override
	public Map<String, Object> updateAgentProStatus(Map<String, Object> map) {
		Map<String,Object> msg = new HashMap<String,Object>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		//如果是打开业务产品
		//判断这个业务产品下的所有服务的分润和费率，费率要大于等于分润
		//根据代理商业务产品查询对应分润
		//根据分润查询对应的费率
		//比较分润和费率的大小
		//如果分润>费率，报错
		//如果分润子表有待审核或者审核不通过或者审核通过但是即将生效的分润，不让打开
		String agentNo = String.valueOf(map.get("agentNo"));
		String bpId = String.valueOf(map.get("bpId"));
		Integer status=(Integer) map.get("status");
		if(status!=null && status==1){
			List<AgentShareRule> shareList = agentShareDao.getByAgentNoBpId(agentNo, bpId);
			for(AgentShareRule share: shareList){
				//查询审核
				ServiceRate rate = agentShareDao.queryRateByShare(share);
				if(rate==null){
					throw new RuntimeException("找不到对应的费率");
				}
				if(rate.getServiceType()==10000 || rate.getServiceType()==10001){
					rate.setIsTx(1);
				} else {
					rate.setIsTx(0);
				}
				this.compareRuleRate(share, rate);
				if(agentShareDao.countHasNotCheck(Long.valueOf(bpId), agentNo) > 0 ){
					throw new RuntimeException("业务产品" + bpId + "不能有待审核或者审核不通过，"
							+ "或者审核通过但是还未生效的分润");
				}
			}
		}
//		JoinTable agentPro=agentInfoDao.getAgentPro(map.get("agentNo").toString(), (Integer)map.get("bpId"));
//		if(status==agentPro.getKey1())
//			return msg;
		AgentInfo info = agentInfoDao.select(agentNo);
		map.put("agentNode",info.getAgentNode());
		agentInfoDao.updateAgentProStatus(map);
		msg.put("status", true);
		msg.put("msg", "操作成功");
		return msg;
	}

	@Override
	public String delAgent(String agentNo, Integer teamId) {
		//1.判断一级代理商是否有下级代理商和直接商户
		int count=agentInfoDao.getAgentAndMerchantCount(agentNo);
		if(count>0){
			return "当前代理商已经存在下级代理商或商户，不能删除！";
		}
//		AgentInfo agent=this.selectByagentNo(agentNo);
		Map<String,Object> map=agentInfoDao.getAgentEntity(agentNo, teamId);
		String userId=(String)map.get("user_id");
		//删除代理商角色
		agentInfoDao.delAgentRole(userId);
		//删除代理商用户
		agentInfoDao.delAgentUser(userId);
		//删除代理商实体
		agentInfoDao.delAgentEntity(userId);
		//删除额度
		agentInfoDao.deleteAgentQuotas(agentNo);
		//删除费率
		agentInfoDao.deleteAgentRates(agentNo);
		//删除分润task
		agentInfoDao.deleteAgentShareTasks(agentNo);
		//删除分润
		agentInfoDao.deleteAgentShares(agentNo);
		//删除业务产品
		agentInfoDao.deleteAgentProducts(agentNo);
		//删除代理商
		agentInfoDao.deleteAgent(agentNo);
		return "删除代理商成功！";
	}

	@Override
	public String updateAgent(String data) {
		JSONObject json = JSONObject.parseObject(data);
		//更新代理商基本信息
		AgentInfo agent=JSONObject.parseObject(json.getString("agentInfo"), AgentInfo.class);
		log.info("代理商编号为{}，代理商手机号为{}",agent.getAgentNo(),agent.getMobilephone());
		if(agent.getId()==null||StringUtils.isBlank(agent.getAgentNo())){
			throw new RuntimeException("代理商信息无效，请刷新重新填写！");
		}
		if(agent.getAgentName()!=null&&StringUtil.isSpecialChar(agent.getAgentName())){
			throw new RuntimeException("名称不能包含以下字符+/?\\$&';#=:");
		}
		if(agent.getAgentType()==null||"".equals(agent.getAgentType())){
			throw new RuntimeException("代理商类型不能为空!");
		}
		if("11".equals(agent.getAgentType())){
			agent.setTeamId(998);
			if(agent.getAgentOem()==null||"".equals(agent.getAgentOem())){
				throw new RuntimeException("超级盟主所属品牌不能为空!");
			}
			if(agent.getPhone()==null||"".equals(agent.getPhone())){
				throw new RuntimeException("超级盟主联系电话不能为空!");
			}
			if(agent.getIdCardNo()==null||"".equals(agent.getIdCardNo())){
				throw new RuntimeException("超级盟主身份证号不能为空!");
			}
			if(agent.getAgentShareLevel()==null||"".equals(agent.getAgentShareLevel())){
				throw new RuntimeException("超级盟主交易分润等级不能为空!");
			}
			if(StringUtils.isBlank(agent.getAgentOem())){
				throw new RuntimeException("所属品牌不能为空!");
			}
		}

		//判断身份证号 开户账号 手机号是否在黑名单中
		String riskMsg = "";
		RiskRoll riskRoll3 = riskRollService.checkRollByRollNo(agent.getMobilephone(), 10, 2);
		if(riskRoll3!=null && riskRoll3.getRollStatus()!=null && riskRoll3.getRollStatus().intValue()==1){
			Msg msg = msgService.queryMsgByMsgCode(riskRoll3.getRollMsg());
			if(msg!=null){
				riskMsg += msg.getUserMsg()+"\r\n";
			}
		}
		RiskRoll riskRoll1 = riskRollService.checkRollByRollNo(agent.getIdCardNo(), 8, 2);
		if(riskRoll1!=null && riskRoll1.getRollStatus()!=null && riskRoll1.getRollStatus().intValue()==1){
			Msg msg = msgService.queryMsgByMsgCode(riskRoll1.getRollMsg());
			if(msg!=null){
				riskMsg += msg.getUserMsg()+"\r\n";
			}
		}
		RiskRoll riskRoll2 = riskRollService.checkRollByRollNo(agent.getAccountNo(), 9, 2);
		if(riskRoll2!=null && riskRoll2.getRollStatus()!=null && riskRoll2.getRollStatus().intValue()==1){
			Msg msg = msgService.queryMsgByMsgCode(riskRoll2.getRollMsg());
			if(msg!=null){
				riskMsg += msg.getUserMsg()+"\r\n";
			}
		}

		if(StringUtil.isNotBlank(riskMsg)){
			throw new RuntimeException(riskMsg);
		}

		if(agent.getAccountType()==2){
			AgentInfo info = agentInfoService.selectByagentNo(agent.getAgentNo());
			if ((info != null && (!agent.getIdCardNo().equals(info.getIdCardNo()) || (!agent.getAccountNo().equals(info.getAccountNo()))
					|| (!agent.getAccountName().equals(info.getAccountName()))))
					|| info == null) {//修改时,修改了身份证,银行卡号,开户名就作校验,新增时作校验
				Map<String, String> maps=openPlatformService.doAuthen(agent.getAccountNo(),agent.getAccountName(),agent.getIdCardNo(),null);
				boolean flag = "00".equalsIgnoreCase(maps.get("errCode"));
				if (!flag) {
					log.info("身份证验证失败");
					throw new RuntimeException("开户名、身份证、银行卡号不匹配!");
				}
			}
		}


		//支付收单时，agentOem也不能为空
		if("101".equals(agent.getAgentType())){
			if(StringUtils.isBlank(agent.getAgentOem())){
				throw new RuntimeException("所属品牌不能为空!");
			}
		}
		int count = agentInfoDao.existAgentByMobilephoneAndTeamId(agent);
		if(count>0)
			throw new RuntimeException("该组织下的手机号码或者邮箱或者代理商名称已存在!");
		AgentInfo oldAgent = agentInfoDao.selectByAgentNo(agent.getAgentNo());
		String oldMobilephone = oldAgent.getMobilephone();//以前的手机号，通过以前的手机号和teamId找到唯一的user_info
		int num=agentInfoDao.updateAgent(agent);
		agentInfoDao.updateNotOneAgentOem(agent.getAgentNode(),agent.getAgentOem());
		if(num<1){
			throw new RuntimeException("代理商信息更新失败，请刷新重新填写!");
		}
		//业务产品
		if(json.getString("bpData")!=null&&json.getJSONArray("bpData").size()>0){
			List<Integer> bps=JSONArray.parseArray(json.getString("bpData"),Integer.class);
			List<JoinTable> bpList=new ArrayList<>();
			for(Integer id:bps){
				JoinTable product=new JoinTable();
				product.setKey1(id);
				product.setKey2(1);
				product.setKey3(agent.getAgentNo());
				bpList.add(product);
			}
			agentInfoDao.insertAgentProductList(bpList);

			//设置代理商业务产品的默认值
			//1.如果是自定义业务产品，队长就是默认的(一个群组内，只会有一个可以单独申请的业务产品，称之为队长)
			//2.如果是非自定义业务产品，本身就是默认的
			agentBusinessProductDao.updateDefaultBp(bps, agent.getAgentNo());
		}
		//费率信息
		agentInfoDao.deleteAgentRates(agent.getAgentNo());
		List<ServiceRate> rateList=JSONArray.parseArray(json.getString("rateData"), ServiceRate.class);
		List<ServiceRate> rateList2 = new ArrayList<>();
		List<ServiceRate> rateList3 = new ArrayList<>();
		for(ServiceRate rate:rateList){
			if(rate.getIsGlobal()==1){
				rateList3.add(rate);
			}else {
				rate.setAgentNo(agent.getAgentNo());
				rate.setCheckStatus(0);
				rate.setLockStatus(0);
				serviceProService.setServiceRate(rate,false);
				rateList2.add(rate);
			}
		}

		if(rateList3.size()>0){
			List<ServiceRate> rateList4 = new ArrayList<>();
			for(ServiceRate rate: rateList3){
				rateList4.add(serviceDao.getServiceRateByRate(rate));
			}
			for(ServiceRate rate:rateList4){
				rate.setAgentNo(agent.getAgentNo());
				rate.setCheckStatus(0);
				rate.setLockStatus(0);
				rateList2.add(rate);
			}
		}

		serviceProService.insertServiceRateList(rateList2);
		//限额
		agentInfoDao.deleteAgentQuotas(agent.getAgentNo());
		List<ServiceQuota> quotaList=JSONArray.parseArray(json.getString("quotaData"), ServiceQuota.class);
		List<ServiceQuota> quotaList2 = new ArrayList<>();
		List<ServiceQuota> quotaList3 = new ArrayList<>();
		for(ServiceQuota quota:quotaList){
			if(quota.getIsGlobal()==1){
				quotaList3.add(quota);
			} else {
				quota.setCheckStatus(0);
				quota.setLockStatus(0);
				quota.setAgentNo(agent.getAgentNo());
				quotaList2.add(quota);
			}
		}
		if(quotaList3.size()>0){
			List<ServiceQuota> quotaList4 = new ArrayList<>();
			for(ServiceQuota quota: quotaList3){
				quotaList4.add(serviceDao.getServiceQuotaByQuota(quota));
			}
			for(ServiceQuota quota:quotaList4){
				quota.setCheckStatus(0);
				quota.setLockStatus(0);
				quota.setAgentNo(agent.getAgentNo());
				quotaList2.add(quota);
			}
		}
		serviceProService.insertServiceQuotaList(quotaList2);
		//分润信息
		//如果新增了业务产品，且分润信息为空，则抛异常
		//如果新增的业务产品没有群组号，或者有群组号且是可以进件的，它们对应的分润信息为空，则抛异常
		if(json.getString("bpData")!=null&&json.getJSONArray("bpData").size()>0){
			//1.根据代理商编号查询，找出组长已经代理，现在要代理组员的业务产品集合
			List<Map<Long, Long>> bpIdMapList = agentShareDao.selectLeaderAndOtherMember(agent.getAgentNo());
			List<Map<Long, Long>> onlyMemberMapList = new ArrayList<>();
			List<Integer> bps=JSONArray.parseArray(json.getString("bpData"),Integer.class);
			for(Map<Long, Long> bpIdMap: bpIdMapList){
				//新增的业务产品，不包含组长，但是包括组员，提取出来，单独处理
				if(!bps.contains((bpIdMap.get("leader").intValue()))
						&& bps.contains(bpIdMap.get("member").intValue())){
					onlyMemberMapList.add(bpIdMap);
				}
			}
			//如果页面勾选了已经代理的组长，再勾选组员（bpIdList）时不需要填写分润
			//但是onlyMemberMapList为空，那就必须填写分润，否则抛异常
			if(onlyMemberMapList.isEmpty() && (json.getString("shareData")==null || json.getString("shareData").isEmpty())){
				throw new RuntimeException("新增业务产品数据中没有填写分润信息！");
			}
//
			if(onlyMemberMapList!=null && onlyMemberMapList.size()>0){
//				2.找出这些业务产品对应的分润
				List<AgentShareRule> memberTotalShareList = new ArrayList<>();
				for(Map<Long, Long> bpIdMap: onlyMemberMapList){
					//如果组长有待审核或者审核不通过，或者审核通过但是还未生效的，不能添加组员
					if(agentShareDao.countHasNotCheck(bpIdMap.get("leader"), agent.getAgentNo()) > 0){
						throw new RuntimeException("业务产品" + bpIdMap.get("leader") + "不能有待审核或者审核不通过，或者审核通过但是还未生效的分润，"
								+ "不能添加自定义业务产品" + bpIdMap.get("member"));
					}
					List<AgentShareRule> memberRuleList =  agentShareDao.getShareByLeaderAndMember(agent.getAgentNo(), bpIdMap);
					for(AgentShareRule memberRule: memberRuleList){
						//3.找出最低费率
						memberRule.setAgentNo(agent.getAgentNo());
						ServiceRate minRate = queryMinServiceRate(memberRule);

						//4..然后和对应的组的最低费率比较大小，分润要比费率小
						compareRuleRate(memberRule, minRate);
						memberTotalShareList.add(memberRule);
					}
				}
				//4.如果符合条件，插入分润数据
				if(memberTotalShareList.size() > 0){
					agentInfoDao.insertAgentShareList(memberTotalShareList);
				}

			}
			//注：现在代理组长和组员的或者没有群组信息的，走之前的逻辑

			List<AgentShareRule> shareList=JSONArray.parseArray(json.getString("shareData"), AgentShareRule.class);
			if(shareList!=null && shareList.size()>0){
				List<AgentShareRule> totalShareList = new ArrayList<>();
				for(AgentShareRule rule:shareList){
					//不是提现服务的，代理商成本需要加上%
					if(rule.getServiceType()!=null && rule.getServiceType()!=10000 && rule.getServiceType()!=10001 ){
						if(rule.getCost()!=null && !rule.getCost().contains("%")){
							rule.setCost(rule.getCost()+"%");
						}
					}
					setShareRuleAdd(rule);
					//分润需要小于商户费率
					//找到同组的最低费率
                    rule.setAgentNo(agent.getAgentNo());
					ServiceRate minRate = queryMinServiceRate( rule);
					if(minRate==null){
						throw new RuntimeException("找不到对应的费率");
					}
					//比较rule和rate，rule不能大于rate
					compareRuleRate(rule, minRate);
					rule.setAgentNo(agent.getAgentNo());
					rule.setCheckStatus(0);
					rule.setLockStatus(0);
					log.info("当前的分润：serviceId:{},serviceType:{},cardType:{},holidayMark:{}",
							rule.getServiceId(),rule.getServiceType(),rule.getCardType(),rule.getHolidaysMark());
					totalShareList.add(rule);
					//找到同组的业务产品的服务集合
					List<ServiceRate> serviceRateList = new ArrayList<>();
					//不是提现服务的查询方法
					if(rule.getServiceType()!=null && rule.getServiceType()!=10000 && rule.getServiceType()!=10001 ){
						serviceRateList = agentInfoDao.getOtherServiceRate(rule);
					} else {
						//如果是提现服务，需要从主服务开始查，主服务可以确定唯一（同一业务产品只能关联服务类型不一样的服务，关联提现服务除外）
						serviceRateList = agentInfoDao.getOtherTXServiceRate(rule);
					}
					log.info("SQL查询出对应的分润：{}",JSONObject.toJSONString(serviceRateList));
					if(serviceRateList!=null && !serviceRateList.isEmpty()){
						for(ServiceRate serviceRate: serviceRateList){
							AgentShareRule copyRule = new AgentShareRule();
							BeanUtils.copyProperties(rule, copyRule);
							copyRule.setServiceId(String.valueOf(serviceRate.getServiceId()));
							totalShareList.add(copyRule);
						}
					}
				}
				log.info("新增代理商时,需要保存的全部分润数据:{}",JSONObject.toJSONString(totalShareList));
				agentInfoDao.insertAgentShareList(totalShareList);
			}
		}
		//更新代理商系统的用户信息
		//如果手机号或者邮箱有更改，就需要更新user_info的信息
		if(!oldAgent.getMobilephone().equals(agent.getMobilephone())
				|| !oldAgent.getEmail().equals(agent.getEmail())
				|| !oldAgent.getAgentName().equals(agent.getAgentName())){
			AgentUserInfo oldUserInfo=agentInfoDao.getUserInfo(oldMobilephone, agent.getTeamId());
			AgentUserInfo agentUser=new AgentUserInfo();
			agentUser.setUserName(agent.getAgentName());
			agentUser.setMobilephone(agent.getMobilephone());
			agentUser.setEmail(agent.getEmail());
			agentUser.setUserId(oldUserInfo.getUserId());

			//add by tans 2017.4.13
			//验证user_info表，组织与手机号唯一，或者组织与邮箱唯一
			int countAgentUser = agentInfoDao.existUserInfo(agentUser);
			if(countAgentUser>0){
				throw new RuntimeException("该组织下手机号或者邮箱已存在");
			}
//			Map<String,Object> userInfo = agentInfoDao.getUserInfoById(userId);
			if(!oldUserInfo.getMobilephone().equals(agent.getMobilephone())){
				agentUser.setPassword(new Md5PasswordEncoder().encodePassword("123456",agent.getMobilephone()));
			}
			agentInfoDao.updateAgentUser(agentUser);
		}

		//修改代理商参加的欢乐返子类型，只增不删start
		List<ActivityHardwareType> happyBackTypes = JSONArray.parseArray(json.getString("happyBackTypes"), ActivityHardwareType.class);
		List<ActivityHardwareType> newHappyBackTypes = JSONArray.parseArray(json.getString("newHappyBackTypes"), ActivityHardwareType.class);
		List<ActivityHardwareType> superHappyBackTypes = JSONArray.parseArray(json.getString("superHappyBackTypes"), ActivityHardwareType.class);

		updateHlfTypes(agent, happyBackTypes, newHappyBackTypes,superHappyBackTypes);
		//修改代理商参加的欢乐返子类型，只增不删end

		//处理增值分润相关数据
		vasInfoService.checkVasShareRule(agent.getAgentNo());

		if(agent.getAgentType()!=null&&"11".equals(agent.getAgentType())){
			userAllAgentService.saveUserCardAllAgent(agent);
		}

		return "修改代理商成功";
	}

	/**
	 * 代理商修改，参加欢乐返子类型
	 * @param agent
	 * @param happyBackTypes
	 * @param newHappyBackTypes
	 */
	private void updateHlfTypes(AgentInfo agent, List<ActivityHardwareType> happyBackTypes, List<ActivityHardwareType> newHappyBackTypes,List<ActivityHardwareType> superHappyBackTypes) {
		List<ActivityHardwareType> list = new ArrayList<>();
		list.addAll(happyBackTypes);
		list.addAll(newHappyBackTypes);
		list.addAll(superHappyBackTypes);
		if(list == null || list.isEmpty()) {
			return;
		}
		List<String> agentActivityList = agentInfoDao.selectHbTypeByAgentNo(agent.getAgentNo());//查询已参加的子类型
		if(agentActivityList != null && !agentActivityList.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {//去掉已参加的子类型
				if (agentActivityList.contains(list.get(i).getActivityTypeNo())) {
					list.remove(i--);
				}
			}
		}
		if (list.size() > 0) {
			String tpyes="";
			for (ActivityHardwareType aht:list) {
				tpyes+="'"+aht.getActivityTypeNo()+"',";
				// ('00007','1448','0-1448-',127),('00009','1449','0-1449-',129)
				aht.setAgentNo(agent.getAgentNo());
				aht.setAgentNode(agent.getAgentNode());
				aht.setTaxRate(BigDecimal.valueOf(1));
				aht.setRepeatRegisterRatio(BigDecimal.valueOf(1));

				aht.setOneSubWhenIsNull();
			}
			int res = agentInfoDao.insertHappyBackType(list);
			if(!"".equals(tpyes)){
				agentInfoDao.insertHappyBackTypeByAgentLevel(agent.getAgentNode()+"%",tpyes.substring(0,tpyes.length() - 1));
			}
			if (res != list.size()) {
				throw new RuntimeException("修改代理商参加的欢乐返子类型失败");
			}
		}
	}

	public ServiceRate queryMinServiceRate(AgentShareRule rule) {
		//根据服务ID、卡类型、节假日标志，获取对应的费率
		ServiceRate rate = new ServiceRate();
		rate.setAgentNo(rule.getAgentNo());
		rate.setServiceId(Long.valueOf(rule.getServiceId()));
		rate.setCardType(rule.getCardType().toString());
		rate.setHolidaysMark(rule.getHolidaysMark().toString());
		rate.setServiceType(rule.getServiceType());
		rate.setBpId(rule.getBpId());

		ServiceRate minRate;
		//找到同组的最低费率
		//校验服务有没有群组，如果没有，直接查询service_manager_rate
		int num = serviceDao.hasGroup(rule.getServiceId());
		if(num == 0){
			minRate = serviceDao.querySysServiceRate(rate);
		} else {
			//如果有群组，需要查询群组内的最低费率
			//如果是交易服务
			if(rate.getServiceType()!=null && rate.getServiceType()!=10000 && rate.getServiceType()!=10001 ){
				minRate = serviceDao.queryMinJYServiceRate(rate);
			} else {
				//如果是提现服务，需要从主服务开始查，主服务可以确定唯一（同一业务产品只能关联服务类型不一样的服务，关联提现服务除外）
				//先查出提现服务对应交易服务的服务类型
				ServiceInfo jyService = serviceDao.queryJyServiceByTx(rate.getServiceId());
				rate.setServiceType2(jyService.getServiceType());//serviceType2表示其对应的交易服务类型
				minRate = serviceDao.queryMinTXServiceRate(rate);
			}
		}
        if(minRate==null){
            throw new RuntimeException("找不到对应的费率");
        }
        if(rate.getServiceType()!=null && rate.getServiceType()!=10000 && rate.getServiceType()!=10001 ){
            minRate.setIsTx(0);
        } else {
            minRate.setIsTx(1);
        }
		//之前的旧版查询同组最低的服务费率
//		minRate = serviceDao.queryMinServiceRate(rate);
		return minRate;
	}

	@Override
	public int updateAgentShare(String data) {
		JSONObject json = JSONObject.parseObject(data);
		List<AgentShareRule> list = JSONObject.parseArray(json.getString("shareData"), AgentShareRule.class);
		int num = 0;
		for(AgentShareRule rule: list){
			if(rule == null ||rule.getCheckStatus()==null || rule.getCheckStatus()==0){
				continue;
			}
			if(rule.getShareId()!=null){
				//更新子表记录
				num = agentShareDao.updateShareTaskStatus(rule);
				//查到当前分润的主分润的所属代理商、业务产品ID、服务类型、卡类型、节假日标志
				Map<String, Object> resultMap = agentShareDao.getAgentBpServiceType(rule.getShareId().intValue());
				//获取同组的其他分润
				List<AgentShareRule> otherShareList = agentShareDao.getOtherGroupShareId(resultMap);
				//根据shareId和生效时间，确定同组的其他分润，再去修改
				if(otherShareList!=null && otherShareList.size()>0){
					for(AgentShareRule otherShare: otherShareList){
						log.info("修改同组子表分润，otherShare=【{}】",JSONObject.toJSONString(otherShare));
						rule.setShareId(otherShare.getId());
						agentShareDao.updateShareTaskStatus(rule);
					}
				}
			} else {
				//更新主表记录
				num = agentShareDao.updateShareStatus(rule);
				//查到当前分润的主分润的所属代理商、业务产品ID、服务类型、卡类型、节假日标志
				Map<String, Object> resultMap = agentShareDao.getAgentBpServiceType(rule.getId().intValue());
				//获取同组的其他分润
				List<AgentShareRule> otherShareList = agentShareDao.getOtherGroupShareId(resultMap);
				//根据shareId和生效时间，确定同组的其他分润，再去修改
				if(otherShareList!=null && otherShareList.size()>0){
					for(AgentShareRule otherShare: otherShareList){
						log.info("修改同组主表分润，otherShare=【{}】",JSONObject.toJSONString(otherShare));
						rule.setId(otherShare.getId());
						agentShareDao.updateShareStatus(rule);
					}
				}
			}
		}
		return num;
	}

	@Override
	public void setShareRule(AgentShareRule share){
		String temp;
		switch (share.getProfitType()) {
			case 1:
				temp=share.getIncome();
				if(StringUtils.isBlank(temp)||!temp.matches("\\d+(\\.\\d+)?"))
					throw new RuntimeException("分润信息中类型为【每笔固定收益金额】：填写不合法！");
				share.setPerFixIncome(new BigDecimal(temp));
				break;
			case 2:
				temp=share.getIncome();
				if(StringUtils.isBlank(temp)||!temp.matches("\\d+(\\.\\d+)?%"))
					throw new RuntimeException("分润信息中类型为【每笔收益率】：填写不合法！");
				share.setPerFixInrate(new BigDecimal(temp.substring(0,temp.indexOf("%"))));
				break;
			case 3:
				temp=share.getIncome();
				Matcher m = pattern.matcher(temp);
				if(StringUtils.isBlank(temp)||!m.matches())
					throw new RuntimeException("分润信息中类型为【每笔收益率带保底封顶】：填写不合法！");
				m.reset();
				while(m.find()){
					share.setSafeLine(new BigDecimal(m.group(1)));
					share.setPerFixInrate(new BigDecimal(m.group(3)));
					share.setCapping(new BigDecimal(m.group(5)));
					if(share.getSafeLine().compareTo(share.getCapping())>0){
						throw new RuntimeException("分润信息中类型为【每笔收益率带保底封顶】：保底不能大于封顶！");
					}
				}
				break;
			case 4:
				temp=share.getIncome();
				if(StringUtils.isBlank(temp)||!temp.matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
					throw new RuntimeException("分润信息中类型为【每笔收益率+每笔固定收益金额】：填写不合法！");
				String[] arr=temp.split("%\\+");
				share.setPerFixInrate(new BigDecimal(arr[0]));
				share.setPerFixIncome(new BigDecimal(arr[1]));
				break;
			case 5:
				temp=setShareCose(share);
				if(share.getShareProfitPercent()==null||StringUtils.isNotBlank(temp))
					throw new RuntimeException("分润信息中类型为【商户签约费率与代理商成本费率差额百分比分润】商户成本填写不合法！");
				break;
			case 6:
				temp=setShareCose(share);
				if(StringUtils.isNotBlank(temp))
					throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】商户成本填写不合法！");
				temp=share.getLadderRate();
				if(!temp.matches("^(\\d+(\\.\\d+)?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d+)?)%){3}"))
					throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例：填写不合法！");
				String[] strs=temp.split("<");
				share.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
				share.setLadder1Max(new BigDecimal(strs[1]));
				share.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
				share.setLadder2Max(new BigDecimal(strs[3]));
				share.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
				share.setLadder3Max(new BigDecimal(strs[5]));
				share.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
				if(share.getLadder2Max().compareTo(share.getLadder1Max())<=0)
					throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例第二组上限额度要大于第一组上限额度！");
				if(share.getLadder3Max().compareTo(share.getLadder2Max())<=0)
					throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例第三组上限额度要大于第二组上限额度！");
				break;
		}
	}

	public void setShareRuleAdd(AgentShareRule share){
		String temp;
		switch (share.getProfitType()) {
			case 5:
				temp=setShareCoseAdd(share);
				if(share.getShareProfitPercent()==null||StringUtils.isNotBlank(temp))
					throw new RuntimeException("分润信息中类型为【商户签约费率与代理商成本费率差额百分比分润】商户成本填写不合法！");
				break;
			case 6:
				temp=setShareCoseAdd(share);
				if(StringUtils.isNotBlank(temp))
					throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】商户成本填写不合法！");
				temp=share.getLadderRate();
				if(!temp.matches("^(\\d+(\\.\\d+)?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d+)?)%){3}"))
					throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例：填写不合法！");
				String[] strs=temp.split("<");
				share.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
				share.setLadder1Max(new BigDecimal(strs[1]));
				share.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
				share.setLadder2Max(new BigDecimal(strs[3]));
				share.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
				share.setLadder3Max(new BigDecimal(strs[5]));
				share.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
				if(share.getLadder2Max().compareTo(share.getLadder1Max())<=0)
					throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例第二组上限额度要大于第一组上限额度！");
				if(share.getLadder3Max().compareTo(share.getLadder2Max())<=0)
					throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例第三组上限额度要大于第二组上限额度！");
				break;
			default:
				throw new RuntimeException("分润方式错误，请刷新页面后重试");
		}
	}

	/**
	 * 设置成本
	 * @param share
	 * @return
	 */
	private String setShareCose(AgentShareRule share){
		String temp=share.getCost();
		if(temp.indexOf("+")!=-1){
			if(!temp.matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			String[] arr=temp.split("%+");
			share.setCostRate(new BigDecimal(arr[0]));
			share.setPerFixCost(new BigDecimal(arr[1]));
			share.setCostRateType("4");
		}else if(temp.indexOf("~")!=-1){
			Matcher m = pattern.matcher(temp);
			while(m.find()){
				share.setCostSafeline(new BigDecimal(m.group(1)));
				share.setCostRate(new BigDecimal(m.group(3)));
				share.setCostCapping(new BigDecimal(m.group(5)));
			}
			if(share.getCostCapping()==null||share.getCostCapping().compareTo(share.getCostSafeline())<0)
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			share.setCostRateType("3");
		}else if(temp.indexOf("%")!=-1){
			String str_=temp.substring(0, temp.indexOf("%"));
			if(str_.matches("\\d+(\\.\\d+)?")){
				share.setCostRate(new BigDecimal(str_));
			}else{
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			}
			share.setCostRateType("2");
		}else {
			if(temp.matches("\\d+(\\.\\d+)?")){
				share.setPerFixCost(new BigDecimal(temp));
			}else{
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			}
			share.setCostRateType("1");
		}
		if(share.getRateType()!=null&&share.getRateType().equals("6")){
			share.setCostRateType("5");
		}
		return null;
	}

	/**
	 * 新增代理商的时候，设置成本，此时成本只有两种1-每笔固定金额，2-扣率
	 * @param share
	 * @return
	 */
	private String setShareCoseAdd(AgentShareRule share){
		String temp=share.getCost();
		if(temp.indexOf("%")!=-1){
			String str_=temp.substring(0, temp.indexOf("%"));
			if(str_.matches("\\d+(\\.\\d+)?")){
				share.setCostRate(new BigDecimal(str_));
			}else{
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			}
			share.setCostRateType("2");
		}else {
			if(temp.matches("\\d+(\\.\\d+)?")){
				share.setPerFixCost(new BigDecimal(temp));
			}else{
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			}
			share.setCostRateType("1");
		}
		if(share.getRateType()!=null&&share.getRateType().equals("6")) {
			share.setCostRateType("5");
		}
		return null;
	}

	//设置成本
	@Override
	public void profitExpression(AgentShareRule rule){
		if(rule.getProfitType()==null) return;
		switch(rule.getProfitType()){
			case 1:
				rule.setIncome(rule.getPerFixIncome().setScale(2, RoundingMode.HALF_UP).toString());
				rule.setShareProfitPercent(null);
				break;
			case 2:
				rule.setIncome(rule.getPerFixInrate().setScale(3, RoundingMode.HALF_UP).toString()+"%");
				rule.setShareProfitPercent(null);
				break;
			case 3:
				rule.setIncome(rule.getSafeLine().setScale(2, RoundingMode.HALF_UP).toString()+"~"+
						rule.getPerFixInrate().setScale(3, RoundingMode.HALF_UP).toString()+"%~"+rule.getCapping().setScale(2, RoundingMode.HALF_UP).toString());
				rule.setShareProfitPercent(null);
				break;
			case 4:
				rule.setIncome(rule.getPerFixInrate().setScale(3, RoundingMode.HALF_UP).toString()+"%+"+rule.getPerFixIncome().setScale(2, RoundingMode.HALF_UP).toString());
				rule.setShareProfitPercent(null);
				break;
			case 5:
				switch (rule.getCostRateType()) {
					case "1":
						rule.setCost(rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
						rule.setShareProfitPercent(rule.getShareProfitPercent().setScale(3, RoundingMode.HALF_UP));
						break;
					case "2":
						rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString()+"%");
						rule.setShareProfitPercent(rule.getShareProfitPercent().setScale(3, RoundingMode.HALF_UP));
						break;
					case "3":
						rule.setCost(rule.getCostSafeline().setScale(2, RoundingMode.HALF_UP).toString()+"~"+rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString()+"%~"+rule.getCostCapping().setScale(2, RoundingMode.HALF_UP).toString());
						rule.setShareProfitPercent(rule.getShareProfitPercent().setScale(3, RoundingMode.HALF_UP));
						break;
					case "4":
						rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString()+"%+"+rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
						rule.setShareProfitPercent(rule.getShareProfitPercent().setScale(3, RoundingMode.HALF_UP));
						break;
					case "5":
						if(rule.getServiceType().equals("10000")||rule.getServiceType().equals("10001")){
							rule.setCost(rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
						}else{
							rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString()+"%");
						}
						rule.setShareProfitPercent(rule.getShareProfitPercent().setScale(3, RoundingMode.HALF_UP));
						break;
				}
				break;
			case 6:
				StringBuffer sb=new StringBuffer();
				sb.append(rule.getLadder1Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%<").append(rule.getLadder1Max().setScale(2, RoundingMode.HALF_UP).toString())
						.append("<").append(rule.getLadder2Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%<").append(rule.getLadder2Max().setScale(2, RoundingMode.HALF_UP).toString())
						.append("<").append(rule.getLadder3Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%<").append(rule.getLadder3Max().setScale(2, RoundingMode.HALF_UP).toString())
						.append("<").append(rule.getLadder4Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%");
				rule.setLadderRate(sb.toString());
				switch (rule.getCostRateType()) {
					case "1":
						rule.setCost(rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
						break;
					case "2":
						rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString()+"%");
						break;
					case "3":
						rule.setCost(rule.getCostSafeline().setScale(2, RoundingMode.HALF_UP).toString()+"~"+rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString()+"%~"+rule.getCostCapping().setScale(2, RoundingMode.HALF_UP).toString());
						break;
					case "4":
						rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString()+"%+"+rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
						break;
				}

		}
	}

	@Override
	public int updateAgentAccount(String agentNo) {
		int num = 0;
		//开设代理商账户
		try{
			String acc=ClientInterface.createAgentAccount(agentNo);
			System.out.println(acc);
			if(JSONObject.parseObject(acc).getBooleanValue("status")){
				num = agentInfoDao.updateAgentAccount(agentNo, 1);
			} else if(JSONObject.parseObject(acc).getString("msg").equals("外部账号已经存在")){
				num = agentInfoDao.updateAgentAccount(agentNo, 1);
			}else
				log.info("开立代理商账户失败：{}" ,acc);
		}catch(Exception e){
			log.error("开立代理商账户异常",e);
		}
		return num;
	}

	@Resource
	private UserEntityDao userEntityDao;
	/**
	 * 更新代理商状态，同时也要改变代理商用户的状态
	 */
	@Override
	public int updateStatus(AgentInfo agent) {
		int num = 0;
		num = agentInfoDao.updateStatus(agent);
		List<UserEntityInfo> userEntityInfoList = userEntityDao.getByAgent(agent.getAgentNo());
		userEntityDao.updateStatusBatch(userEntityInfoList,agent.getStatus());
		userDao.updateStatusBatch(userEntityInfoList,agent.getStatus());
		return num;
	}

	@Override
	public List<AgentInfo> selectAllInfoSale(String name, boolean onlyOneNode) {
		List<AgentInfo> list=new ArrayList<AgentInfo>();
		List<String> list1 = agentInfoDao.selectAllNodeSale(name);
		for (String node : list1) {
			node = onlyOneNode ? node.replaceAll("%","") : node;
			List<AgentInfo> list2 = agentInfoDao.selectAllInfoSale(node);
			for (AgentInfo agentInfo2 : list2) {
				list.add(agentInfo2);
			}
		}
		return list;
	}

	@Override
	public List<AgentInfo> selectAllInfoSale(String name) {
		return selectAllInfoSale(name,false);
	}


	@Override
	public List<AgentInfo> selectAllInfoSale1(String node) {
		return agentInfoDao.selectAllInfoSale(node);
	}

	@Override
	public List<String> selectAllNodeSale(String node) {
		return agentInfoDao.selectAllNodeSale(node);
	}

	@Override
	public List<String> queryAllOneAgentBySale(String saler) {
		return agentInfoDao.queryAllOneAgentBySale(saler);
	}

	@Override
	public Page<AgentInfo> queryAgentInfoListSale(String realName, Map<String, Object> params, Page<AgentInfo> page) {

//		List<String> list=agentInfoDao.selectAllNodeSale(realName);
		String agentNo = (String) params.get("agentNo");
		//查询条件如果代理商编号不为空
		if(StringUtils.isNotBlank(agentNo)){
			AgentInfo oneAgent = agentInfoDao.findOneAgentByAgentNoAndRealName(realName, agentNo);
			if(oneAgent==null){
				return page;
			}
			//包含下级
			if((Integer)params.get("hasSub")==1){
				params.put("subAgentNo", oneAgent.getAgentNode());
			}
		} else {
			params.put("saleName", realName);
		}
		agentInfoDao.queryAgentInfoList(params, page);
		return page;

//		List<String> list=agentInfoDao.selectAllNodeSale(str);
//		if(list.size()==0){
//			return null;
//		}
//		String str1="";
//		for (String agentInfo : list) {
//			List<AgentInfo> list2 = agentInfoDao.selectAllInfoSale(agentInfo);
//			for (AgentInfo agentInfo2 : list2) {
//				str1+=" or info.agent_no="+agentInfo2.getAgentNo();
//			}
//		}
//		if(str1.equals("")){
//			return null;
//		}
//		if(StringUtils.isNotBlank((String)params.get("agentNo"))&&"1".equals(params.get("hasSub").toString())){
//			AgentInfo info=this.selectByagentNo((String)params.get("agentNo"));
//			if(info!=null&&StringUtils.isNotBlank(info.getAgentNode())){
//				params.put("subAgentNo", info.getAgentNode()+"%");
//			}else
//			return page;
//		}
//		agentInfoDao.queryAgentInfoListSale(str1.substring(3, str1.length()),params,page);
//		return page;
	}

	@Override
	public List<AgentShareRule> getAllShare(String agentNo) {
		List<AgentShareRule> list3 = new ArrayList<>();
		List<AgentShareRule> list=agentInfoDao.getAgentShareInfos(agentNo);
		for(AgentShareRule rule: list){
//			if(rule.getCheckStatus()==0){
			rule.setEffectiveStatus(rule.getCheckStatus());
			list3.add(rule);
//			}else{
			List<AgentShareRule> list2 = agentInfoDao.getAllAgentShareRuleTaskByRule(rule);
			for(AgentShareRule rule2: list2){
				rule2.setServiceName(rule.getServiceName());
				rule2.setCardType(rule.getCardType());
				rule2.setHolidaysMark(rule.getHolidaysMark());
			}
			list3.addAll(list2);
//			}
		}
		for(AgentShareRule r:list3)
			this.profitExpression(r);
		return list3;
	}

	/**
	 * 查询出代理商的分润设置
	 */
	private List<AgentShareRule> getProfits(List<Integer> bpIds, String agentId) {
		List<AgentShareRule> list = agentInfoDao.getProfits(bpIds,agentId);
		for (AgentShareRule rule : list) {
			agentInfoService.profitExpression(rule);
//			StringBuffer sb=new StringBuffer();
//			if (rule.getLadder1Rate()!=null||rule.getLadder1Max()!=null||rule.getLadder1Max()!=null||rule.getLadder2Rate()!=null||
//					rule.getLadder2Max()!=null||rule.getLadder3Rate()!=null||rule.getLadder3Max()!=null||rule.getLadder4Rate()!=null) {
//				sb.append(rule.getLadder1Rate()).append("%<").append(rule.getLadder1Max())
//				  .append("<").append(rule.getLadder2Rate()).append("%<").append(rule.getLadder2Max())
//				  .append("<").append(rule.getLadder3Rate()).append("%<").append(rule.getLadder3Max())
//				  .append("<").append(rule.getLadder4Rate()).append("%");
//				rule.setLadderRate(sb.toString());
//			}
		}
		return list;
	}

	//========sober==================================================
	/*
	 * 开设存量代理商欢乐送账户
	 * by Ivan
	 * (non-Javadoc)
	 * @see cn.eeepay.framework.service.AgentInfoService#levelOneCreateAcc(java.lang.String)
	 */
	@Override
	public int levelOneCreateAcc(String agentNo) {
		int num = 0;
		//开设存量代理商欢乐送账户
		try{
			String acc=ClientInterface.levelOneCreateAcc(agentNo);
			log.info("开户返回：" + acc);
			if(JSONObject.parseObject(acc).getBooleanValue("status")){
				num = agentInfoDao.updateNotOneAgentAccount(agentNo, 1);
			} else if(JSONObject.parseObject(acc).getString("msg").equals("外部账号已经存在")){
				num = agentInfoDao.updateNotOneAgentAccount(agentNo, 1);
			}else
				log.info("开立代理商账户失败：{}" ,acc);
		}catch(Exception e){
			log.error("开立代理商账户异常",e);
		}
		return num;
	}

	@Override
	public Map<String, Object> updateProfitStatus(AgentInfo agent) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		if(agent!=null ){
			if(agent.getProfitSwitch()==1){
				AgentInfo parentAgent = agentInfoDao.getParentProfitsSwitch(agent.getAgentNode());
				if(parentAgent!=null && parentAgent.getProfitSwitch()!=1){
					msg.put("msg", "上级或本级代理商未打开分润日结 ,不能给下级打开分润日结");
					return msg;
				}
			}
			int num = agentInfoDao.updateProfitStatus(agent.getAgentNode(),agent.getProfitSwitch());
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}
		}
		return msg;
	}

	@Override
	public int updateProgitSwitchBatch(List<String> agentNodeList, Integer switchStatus) {
		//关闭分润开关时，需要将代理商的所有下级都关闭
		int num = 0;
		for(String agentNode: agentNodeList){
			if(1==switchStatus){
				AgentInfo parentAgent = agentInfoDao.getParentProfitsSwitch(agentNode);
				if(parentAgent!=null && parentAgent.getProfitSwitch()!=1){
					continue;
				}
			}
			num += agentInfoDao.updateProfitStatus(agentNode,switchStatus);
		}
		return num;
	}

	@Override
	public Map<String, Object> updatePromotionStatus(AgentInfo agent) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		String agentNode = agent.getAgentNode();
		if (agentNode == null ||"".equals(agentNode)) {
			return msg;
		}
		int num = agentInfoDao.updatePromotionStatus(agentNode, agent.getPromotionSwitch());
		if (num > 0) {
			msg.put("status", true);
			msg.put("msg", "操作成功");
		}
		return msg;
	}

	@Override
	public Map<String, Object> updateCashBackStatus(AgentInfo agent) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		String agentNode = agent.getAgentNode();
		if (agentNode == null ||"".equals(agentNode)) {
			return msg;
		}
		int num = agentInfoDao.updateCashBackStatus(agentNode, agent.getCashBackSwitch());
		if (num > 0) {
			msg.put("status", true);
			msg.put("msg", "操作成功");
		}
		return msg;
	}

	@Override
	public int updatePromotionSwitchBatch(List<String> agentNodeList, Integer switchStatus) {
		// 关闭推广开关时，需要将代理商的所有下级推广开关都关闭
		int num = 0;
		for (String agentNode : agentNodeList) {
			num += agentInfoDao.updatePromotionStatus(agentNode, switchStatus);
		}
		return num;
	}

	/**
	 * 代理商根据科目号开户
	 */
	@Override
	public Map<String, Object> openOldAccount(List<String> agentNoList, String subjectNo) {
		Map<String,Object> msg=new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		int successTimes = 0;
		int failTimes = 0;
		for(String agentNo: agentNoList){
			try{
				String acc=ClientInterface.createAccountByAcc(agentNo,subjectNo);
				log.info("开户返回：" + acc);
				if(JSONObject.parseObject(acc).getBooleanValue("status")){
					successTimes++;
					agentInfoDao.updateNotOneAgentAccount(agentNo, 1);
				} else if(JSONObject.parseObject(acc).getString("msg").equals("外部账号已经存在")){
					successTimes++;
					agentInfoDao.updateNotOneAgentAccount(agentNo, 1);
				}else
					log.info("开立代理商账户失败：{}" ,acc);
			}catch(Exception e){
				failTimes++;
				log.error("开立代理商账户异常",e);
			}
		}
		msg.put("status", true);
		msg.put("msg", "成功：" + successTimes + "条，失败：" + failTimes + "条");
		return msg;
	}

	@Override
	public List<ActivityHardwareType> selectHappyBackType() {
		return agentInfoDao.selectHappyBackType();
	}

	@Override
	public List<ActivityHardwareType> selectHappyBackTypeBySubType(String activityCode,String subType) {

		List<ActivityHardwareType> lists=agentInfoDao.selectHappyBackTypeBySubType(activityCode,subType);
		if(lists!=null&&!lists.isEmpty()){
			for(ActivityHardwareType aht:lists){
				Integer xhlfSmartConfigId=aht.getXhlfSmartConfigId();
				//判断是新欢乐送智能版活动的时候 给这6个字段插入配置的数据吧，显示用
				if(xhlfSmartConfigId!=null){
					XhlfSmartConfig  xsc=agentInfoDao.selectXhlfSmart(xhlfSmartConfigId);
					if(xsc!=null){
						aht.setOneRewardAmount(xsc.getOneRewardAgentAmount());
						aht.setTwoRewardAmount(xsc.getTwoRewardAgentAmount());
						aht.setThreeRewardAmount(xsc.getThreeRewardAgentAmount());
						aht.setOneRepeatRewardAmount(xsc.getOneRepeatRewardAgentAmount());
						aht.setTwoRepeatRewardAmount(xsc.getTwoRepeatRewardAgentAmount());
						aht.setThreeRepeatRewardAmount(xsc.getThreeRepeatRewardAgentAmount());

					}
				}
			}
		}
		return lists;
	}

	@Override
	public List<ActivityHardwareType> selectHappyBackTypeByAgentNo(String agentNo){
		return agentInfoDao.selectHappyBackTypeByAgentNo(agentNo);
	}

	@Override
	public List<Map> selectAgentShareRule(){
		return agentInfoDao.selectAgentShareRule();
	}

	@Override
	public List<Map> queryAgentRoleOemList(Map<String, Object> params, Page<Map> page){
		return agentInfoDao.queryAgentRoleOemList(params,page);
	}

	@Override
	public Map selectAgentRoleOem(Map<String, Object> params){
		return agentInfoDao.selectAgentRoleOem(params);
	}

	@Override
	public int insertAgentRoleOem(Map<String, Object> params){
		return agentInfoDao.insertAgentRoleOem(params);
	}

	@Override
	public int updateAgentRoleOem(Map<String, Object> params){
		return agentInfoDao.updateAgentRoleOem(params);
	}

	@Override
	public int deleteAgentRoleOem(Integer id){
		return agentInfoDao.deleteAgentRoleOem(id);
	}

	@Override
	public List<Map> selectUserAgentRoleOem(Map<String, Object> params){
		return agentInfoDao.selectUserAgentRoleOem(params);
	}

	@Override
	public List<AgentInfo> selectAgentShareCheckList(Page<AgentInfo> page, AgentInfo agentInfo) {
		return agentInfoDao.selectAgentShareCheckList(page, agentInfo);
	}

	public void exportAgentShareCheck(AgentInfo info, HttpServletResponse response)  throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OutputStream ouputStream = null;

		Page<AgentInfo> page = new Page<>();
		page.setPageSize(Integer.MAX_VALUE);
		List<AgentInfo> list = selectAgentShareCheckList(page,info);
		String fileName = "代理商审核"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		Map<String,String> map = null;
		Map<String, String> statusMap = new HashMap<>();
		statusMap.put("0", "关闭进件");
		statusMap.put("1", "正常");
		statusMap.put("2", "冻结");

		for(AgentInfo item: list){
			map = new HashMap<>();
			map.put("agentNo", item.getAgentNo());
			map.put("agentName", item.getAgentName());
			map.put("status", StringUtils.trimToEmpty(statusMap.get(item.getStatus()+"")));
			map.put("createDate", item.getCreateDate()==null?"":sdfTime.format(item.getCreateDate()));
			map.put("teamName", item.getTeamName());
			data.add(map);
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"agentNo","agentName","status","createDate","teamName"};
		String[] colsName = new String[]{"代理商编号","代理商名称","状态","创建时间","所属组织"};
		try {
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出代理商审核失败,param:{}",JSONObject.toJSONString(info));
			e.printStackTrace();
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int insertHappyBackTypeByAgentLevel(String agentNo, String types){
		return agentInfoDao.insertHappyBackTypeByAgentLevel(agentNo, types);
	}

	@Override
	public int updateHappyBackTypeByAgentLevel(String agentNoes, String types){
		return agentInfoDao.updateHappyBackTypeByAgentLevel(agentNoes, types);
	}

	@Override
	public int insertHlfTerByAgentLevel(String agentNo, String activityTypeNo){
		return agentInfoDao.insertHlfTerByAgentLevel(agentNo, activityTypeNo);
	}

	@Override
	public int updateHlfTerByAgentLevel(String agentNoes, String activityTypeNo){
		return agentInfoDao.updateHlfTerByAgentLevel(agentNoes, activityTypeNo);
	}

	@Override
	public int insertHlfTerByOneAgentLevel(String agentNo, String activityTypeNo){
		return agentInfoDao.insertHlfTerByOneAgentLevel(agentNo, activityTypeNo);
	}

	@Override
	public int insertHappyBackType(List<ActivityHardwareType> happyBackTypes){
		return agentInfoDao.insertHappyBackType(happyBackTypes);
	}

	@Override
	public AgentInfo selectLittleInfo(String agentNo) {
		return agentInfoDao.selectLittleInfo(agentNo);
	}


	@Override
	public List<JoinTable> getAgentProducts(String agentNo){
		return businessProductDefineDao.getAgentProducts(agentNo);
	}

	@Override
	public List<ActivityHardwareType> selectHappyBackTypeWithTeamId(List<String> teamIds) {
		return agentInfoDao.selectHappyBackTypeWithTeamId(teamIds);
	}

	@Override
	public String getOneAgentNo(String agentNo) {
		return agentInfoDao.getOneAgentNo(agentNo);
	}

}
