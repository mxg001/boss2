package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.dao.cjt.CjtOrderSnDao;
import cn.eeepay.framework.dao.cjt.CjtTeamHardwareDao;
import cn.eeepay.framework.daoAllAgent.UserAllAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.allAgent.MerchantAllAgent;
import cn.eeepay.framework.model.allAgent.TerInfo;
import cn.eeepay.framework.model.allAgent.UserAllAgent;
import cn.eeepay.framework.model.cjt.CjtConstant;
import cn.eeepay.framework.model.cjt.CjtTeamHardware;
import cn.eeepay.framework.model.importLog.ImportLogEntry;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.allAgent.AgentTerminalService;
import cn.eeepay.framework.service.allAgent.MerchantAllAgentService;
import cn.eeepay.framework.service.importLog.ImportLogService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("TerminalInfoService")
@Transactional
public class TerminalInfoServiceImpl implements TerminalInfoService {

	private static final Logger log = LoggerFactory.getLogger(TerminalInfoServiceImpl.class);

	@Resource
	public TerminalInfoDao terminalInfoDao;

	@Resource
	private ActivityDetailDao activityDetailDao;

	@Resource
	private SysDictDao sysDictDao;

	@Resource
	private MerchantInfoDao merchantInfoDao;

	@Resource
	private AcqTterminalStoreDao acqTterminalStoreDao;

	@Resource ZqMerchantInfoDao zqMerchantInfoDao;

	@Resource MerchantBusinessProductDao merchantBusinessProductDao;

	@Resource
	private AgentTerminalService agentTerminalService;

	@Resource
	private SysDictService sysDictService;

	@Resource
	private UserAllAgentDao userAllAgentDao;
	@Resource
	private MerchantAllAgentService merchantAllAgentService;
	@Resource
	private ChangeLogService changeLogService;

	@Resource
	private HardwareProductService hardwareProductService;

	@Resource
	private ImportLogService importLogService;

	@Resource
	private HardwareProductDao hardwareProductDao;

	@Resource
	private CjtTeamHardwareDao cjtTeamHardwareDao;

	@Resource
	private BossSysConfigDao bossSysConfigDao;

	@Resource
	private CjtOrderSnDao cjtOrderSnDao;

	@Resource
	private MerchantRequireItemService merchantRequireItemService;

	@Override
	public int insert(TerminalInfo record) {
		return terminalInfoDao.insert(record);
	}

	@Override
	public int insertSelective(TerminalInfo terminalInfo) {
		int i = terminalInfoDao.insertSelective(terminalInfo);
		agentTerminalService.insertAgentTerminal(terminalInfo.getAgentNo(),terminalInfo.getSn(),terminalInfo.getAgentLevel());
		return i;
	}

	@Override
	public long countTerNoBySn(String sn) {
		return terminalInfoDao.countTerNoBySn(sn);
	}

	@Override
	public long countBySn(String sn) {
		return terminalInfoDao.countBySn(sn);
	}

	@Override
	public int updateByPrimaryKey(TerminalInfo record) {
		return terminalInfoDao.updateByPrimaryKey(record);
	}

//	@Override
//	public List<TerminalInfo> selectAllInfo(Page<TerminalInfo> page) {
//		return terminalInfoDao.selectAllInfo(page);
//	}

	@Override
	public TerminalInfo selectObjInfo(Long id) {
		return terminalInfoDao.selectObjInfo(id);
	}

	@Override
	public List<TerminalInfo> selectByParam(Page<TerminalInfo> page, TerminalInfo terminalInfo) {
		List<TerminalInfo> terminalInfoList = terminalInfoDao.selectByParam(page, terminalInfo);
		dataPostprocessingList(page.getResult());
		return terminalInfoList;
	}

	@Override
	public List<TerminalInfo> selectWithTACByCondition(Page<TerminalInfo> page, TerminalInfo info) {
		List<TerminalInfo> list = terminalInfoDao.selectWithTACByCondition(page, info, createPns());
		//如果已达标，考核剩余天数显示已达标
		for (TerminalInfo ter : list) {
			if(ter.getDueDays() > 0 && "1".equals(ter.getStatus())){
				ter.setDueDaysValue("已达标");
			}
		}
		return list;
	}

	@Override
	public List<TerminalInfo> importDetailSelect(TerminalInfo terminalInfo) {
		List<TerminalInfo> terminalInfoList = terminalInfoDao.importDetailSelect(terminalInfo);
		dataPostprocessingList(terminalInfoList);
		return terminalInfoList;
	}
	/**
	 * 后期处理数据List
	 * @param list
	 */
	private  void dataPostprocessingList(List<TerminalInfo> list){
		Map<String, String> activityTypeMap=sysDictService.selectMapByKey("ACTIVITY_TYPE");//活动类型
		if(list!=null&&list.size()>0){
			for(TerminalInfo item :list){
				dataPostprocessing(item,activityTypeMap);
			}
		}
	}
	private void dataPostprocessing(TerminalInfo info,Map<String, String> activityTypeMap){
		if(activityTypeMap==null){
			 activityTypeMap=sysDictService.selectMapByKey("ACTIVITY_TYPE");//活动类型
		}
		if(info!=null){
			if(StringUtils.isNotBlank(info.getActivityType())){
				String[] strs = info.getActivityType().split(",");
				if(strs!=null&&strs.length>0){
					StringBuffer sb=new StringBuffer();
					for(String str:strs){
						if(StringUtils.isNotBlank(str)){
							if(StringUtils.isNotBlank(activityTypeMap.get(str))){
								sb.append(activityTypeMap.get(str)+",");
							}
						}
					}
					if(!"".equals(sb.toString())){
						info.setActivityTypeName(sb.substring(0,sb.length()-1));
					}
				}
			}
			if(StringUtils.isNotBlank(info.getUserCode())){
				UserAllAgent userAllAgent=userAllAgentDao.selectUser(info.getUserCode());
				info.setRealName(userAllAgent.getRealName());
			}
		}
	}


	@Override
	public List<TerminalInfo> selectByAddParam(TerminalInfo terminalInfo) {
		return terminalInfoDao.selectByAddParam(terminalInfo);
	}

	@Override
	public int updateSolutionById(TerminalInfo record) {
		int row=terminalInfoDao.solutionById(record);
		return row;
	}

	@Override
	public int updateRecoverSolutionById(TerminalInfo record) {
		int row=0;
		terminalInfoDao.insertTerminalOperate(record.getId(),2,2);
		TerInfo terInfo=terminalInfoDao.selectAllAgentByTerminalId(record.getId());
		if(terInfo!=null){
			if(terInfo.getCallbackLock()==0) {
				row=terminalInfoDao.solutionById(record);
				terminalInfoDao.deleteAllAgentTerInfo(terInfo.getId());
				ChangeLog changeLog=new ChangeLog();
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				changeLog.setOperater(principal.getId().toString());
				changeLog.setOperMethod("updateRecoverSolutionById");
				changeLog.setRemark("删除");
				changeLog.setChangePre(JSONObject.toJSONString(terInfo));
				changeLogService.insertChangeLog(changeLog);
			}else{
				throw new RuntimeException("机具为回拨锁定状态，不能回收");
			}
		}else{
			row=terminalInfoDao.solutionById(record);
			//超级推的机具，回收的时候，需要删掉对应的购买记录cjt_order_sn
			TerminalInfo terminalInfo = terminalInfoDao.selectById(record.getId());
			if(terminalInfo != null && StringUtils.isNotEmpty(terminalInfo.getSn())) {
				cjtOrderSnDao.deleteBySn(terminalInfo.getSn());
			}
		}
		return row;
	}

	/**
	 * 解绑 1.若该机具标识为欢乐送，且该机具对应的商户欢乐送商户，且活动状态为未激活时候， 满足三个条件后在解绑时，从欢乐送商户池中删除对应的商户。
	 * 不符合该条件的机具解绑逻辑不变。 2.若该机具标识为欢乐返，同上
	 */
	@Override
	public Map<String, Object> updateUnbundlingById(Long id) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "解绑失败");
		// 检验是否欢乐送商户
		String[] activityTypeList = { "6", "7", "8" };
		String[] activityCodeList = { "002", "008", "009" };
		// String activityType = "6";
		// String activityCode = "002";
		TerminalInfo terminalInfo = terminalInfoDao.selectByIdAndActivity(id, activityTypeList);
		String acqEnname = "YS_ZQ";
		String type = "1";//解绑
        String coreUrl = "";
        SysDict coreUrlDict = sysDictDao.getByKey("CORE_URL");
        if(coreUrlDict != null){
            coreUrl = coreUrlDict.getSysValue() + Constants.BIND_OR_UNBIND;
        }
		if (terminalInfo != null && StringUtils.isNotBlank(terminalInfo.getMerchantNo())) {
			ActivityDetail activityDetail = new ActivityDetail();
			activityDetail.setActivityCodeList(activityCodeList);// 活动编号
			activityDetail.setMerchantNo(terminalInfo.getMerchantNo());
			// 查询条件：商户号、002、活动状态不等于1
			// 如果有数据，不能删除，该机具也不能解绑
			List<ActivityDetail> activityDetailList = activityDetailDao
					.selectListByParam(activityDetail.getMerchantNo(), activityDetail.getActivityCodeList());
			if (activityDetailList != null && activityDetailList.size() > 0) {
				msg.put("status", false);
				if ("002".equals(activityDetailList.get(0).getActivityCode())) {
					msg.put("msg", "机具已参加欢乐送且已激活,不能解绑");
				}
				if ("008".equals(activityDetailList.get(0).getActivityCode())) {
					msg.put("msg", "机具已参加欢乐返且已激活,不能解绑");
				}
				if ("009".equals(activityDetailList.get(0).getActivityCode())) {
					msg.put("msg", "机具已参加欢乐返且已激活,不能解绑");
				}
			} else {
				// 机具只能参加欢乐送、欢乐返-循环送、欢乐返的其中一个
				// 如果这个是欢乐送的机具
				// if(terminalInfo.getActivityType().contains("6")){
				// activityDetail.setActivityCode("002");
				// }
				// //如果这个是欢乐返的机具
				// if(terminalInfo.getActivityType().contains("7")){
				// activityDetail.setActivityCode("009");
				// }
				// //如果这个是欢乐返-循环送的机具
				// if(terminalInfo.getActivityType().contains("8")){
				// activityDetail.setActivityCode("008");
				// }
				// 2017.6.27 注释掉，不删除记录
				// activityDetailDao.deleteByCodeAndMer(activityDetail);
                unbundlingById(id, msg, acqEnname, type, coreUrl);
			}
		} else {
            unbundlingById(id, msg, acqEnname, type, coreUrl);
		}
		return msg;
	}

    /**
     * 解绑机具
     * @param id
     * @param msg
     * @param acqEnname
     * @param type
     * @param coreUrl
     * @return
     */
    private Map<String, Object> unbundlingById(Long id, Map<String, Object> msg, String acqEnname, String type, String coreUrl) {
        //2018.05.19,银盛商户直清
        //如果商户同步状态是同步成功时，需调用银盛解绑接口；
        // 若商户同步状态是审核中状态，则提示错误信息为“处于审核中状态，不能解绑”，不能解绑；
        // 若商户同步状态是初始化或者同步失败，不调用银盛解绑接口，内部直接解绑成功。
        TerminalInfo itemTer = terminalInfoDao.selectById(id);
        String sn = itemTer.getSn();
        String merNo = itemTer.getMerchantNo();
        String bpId = itemTer.getBpId();
        MerchantBusinessProduct mbp = merchantBusinessProductDao.selectMerBusPro(merNo, bpId);
        String mbpId = String.valueOf(mbp.getId());
        ZqMerchantInfo zqMerchantInfo = getZqMerchant(merNo, mbpId, acqEnname);
        if(zqMerchantInfo != null && "3".equals(zqMerchantInfo.getSyncStatus())){
            msg.put("msg", "该商户号" + merNo + "处于审核中状态，不能解绑");
            return msg;
        }
        if(zqMerchantInfo != null && "1".equals(zqMerchantInfo.getSyncStatus())){
        	//检查商户在科目224101001的余额，如果余额大于0，不能解绑，解绑提示‘商户账户余额大于0，不能解绑’；
			String subjectNo = "224101001";
			String balanceMsg = ClientInterface.getMerchantAccountBalance(merNo, subjectNo);
			if(StringUtils.isBlank(balanceMsg)){
				msg.put("msg", "sn:" + sn + ",merNo:" + merNo + ",调用账户接口查询余额失败");
				return msg;
			}
			JSONObject balanceJson = JSONObject.parseObject(balanceMsg);
			if(balanceJson.getBoolean("status")){
				AccountInfo accountInfo = JSONObject.parseObject(balanceMsg,AccountInfo.class);
				if(accountInfo != null && accountInfo.getBalance().compareTo(BigDecimal.ZERO) > 0){
					msg.put("msg", "sn:" + sn + ",merNo:" + merNo + ",商户账户余额大于0，不能解绑");
					return msg;
				}
			} else{
				msg.put("msg", "sn:" + sn + ",merNo:" + merNo + ",调用账户接口查询余额失败");
				return msg;
			}
			//调用接口进行解绑
            String returnStr = ClientInterface.bindingOrUnBindTerminal(coreUrl, sn, merNo, mbpId, type);
            if(StringUtils.isNotBlank(returnStr)){
                JSONObject json = JSONObject.parseObject(returnStr);
                JSONObject headJson = JSONObject.parseObject(json.getString("header"));
                if(!headJson.getBoolean("succeed")){
//                    msg.put("msg", "sn:" + sn + ",调用接口解绑失败，不能解绑");
                    msg.put("msg", "sn:" + sn + ",调用接口解绑失败," + headJson.getString("errMsg"));
                    log.info("调用接口绑定失败,失败提示:{}", headJson.getString("errMsg"));
                    return msg;
                }
            }else{
				msg.put("msg", "sn:" + sn + ",调用接口解绑接口异常");
				log.info("调用接口解绑接口异常");
				return msg;
			}
        }
		TerInfo terInfo=terminalInfoDao.selectAllAgentByTerminalId(id);
        if(terInfo!=null){
			if(terInfo.getCallbackLock()==1) {
				msg.put("msg","机具为回拨锁定状态，不能解绑");
				return msg;
			}
			UserAllAgent userAllAgent=userAllAgentDao.selectUser(terInfo.getUserCode());
			if(userAllAgent!=null){
				ChangeLog changeLog=new ChangeLog();
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				changeLog.setOperater(principal.getId().toString());
				changeLog.setOperMethod("unbundlingById");
				changeLog.setRemark("机具解绑");
				changeLog.setChangePre(JSONObject.toJSONString(terInfo));
				if(userAllAgent.getUserType()==1){//1：机构，2：大盟主，3：盟主',
					terminalInfoDao.updateAllAgentTerInfo(terInfo.getId(),1,null);//1:可销售
					terInfo.setStatus(1);
					terInfo.setMerchantNo(null);
					changeLog.setChangeAfter(JSONObject.toJSONString(terInfo));
				}else{
					terminalInfoDao.updateAllAgentTerInfo(terInfo.getId(),2,null);//2: 可下发
					terInfo.setStatus(2);
					terInfo.setMerchantNo(null);
					changeLog.setChangeAfter(JSONObject.toJSONString(terInfo));
				}
				changeLogService.insertChangeLog(changeLog);
			}else{
				msg.put("msg", "sn:" + sn + ",解绑失败,超级盟主用户"+terInfo.getUserCode()+"不存在。");
				return msg;
			}
		}
		terminalInfoDao.unbundlingById(id);

        //清除商户长token
		MerchantInfo merchantInfo = merchantInfoDao.selectByMerNo(merNo);
		if(merchantInfo!=null&&merchantInfo.getTeamId()!=null&&merchantInfo.getTeamId().equals("100060")){
			SysDict coreUrlDict = sysDictDao.getByKey("CORE_URL");
			ClientInterface.clearLongToken(coreUrlDict.getSysValue(),merNo,sn);
		}
        msg.put("status", true);
        msg.put("msg", "解绑成功");
        return msg;
    }

    /**
     * 获取直清商户
     * @param merNo
     * @param mbpId
     * @param acqEnname
     * @return
     */
    private ZqMerchantInfo getZqMerchant(String merNo, String mbpId, String acqEnname) {
        ZqMerchantInfo zqMerchantInfo = zqMerchantInfoDao.selectByMerMbpAcq(merNo, mbpId, acqEnname);
        return zqMerchantInfo;
    }

    @Override
	public int updateBundlingById(TerminalInfo record) {
		return terminalInfoDao.bundlingById(record);
	}

	@Override
	public TerminalInfo selectBySameData(TerminalInfo record) {
		return terminalInfoDao.selectBySameData(record);
	}

	@Override
	public List<TerminalInfo> selectAllInfoBymerNoAndBpId(String merNo, String bpId, Page<TerminalInfo> tiPage) {
		return terminalInfoDao.selectAllInfoBymerNoAndBpId(merNo, bpId, tiPage);
	}

	@Override
	public int insertBatch(Integer codeNumber, String hpId) {
		List<TerminalInfo> list = new ArrayList<>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		for (int i = 0; i < codeNumber; i++) {
			TerminalInfo info = new TerminalInfo();
			// info.setSn(seqService.createKey(Constants.TERMINAL_NO_SEQ));
			info.setPsamNo("jh" + getStringRandom(8));
			info.setCollectionCode(info.getPsamNo());
			info.setCashierNo(principal.getId().toString());
			info.setType(hpId);
			info.setOpenStatus("0");
			list.add(info);
			if (i % 300 == 0) {
				terminalInfoDao.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			terminalInfoDao.insertBatch(list);
		}
		return codeNumber;

	}

	@Override
	public List<TerminalInfo> getByIds(String[] idList) {
		return terminalInfoDao.selectByIds(idList);
	}

	@Override
	public String getReceivableCodeByMerchant(String merchantNo) {
		return terminalInfoDao.getReceivableCodeByMerchant(merchantNo);
	}

	@Override
	public String getActiveCodeBySn(String sn) {
		SysDict sysDict = sysDictDao.getByKey("GATHER_CODE_IP");
		TerminalInfo terminalInfo = terminalInfoDao.getBySn(sn);
		String gatherCode = "";
		if (sysDict != null && terminalInfo != null && StringUtils.isNoneBlank(terminalInfo.getPsamNo())) {
			gatherCode = "http://" + sysDict.getSysValue() + "/gather/gatherProcess?source=3&settleMent=0&gatherCode="
					+ terminalInfo.getPsamNo();
		}
		return gatherCode;
	}

	@Override
	public int updateOpenStatusBatch(List<String> ids, String opentStatus) {
    	int row=0;
		for(int i= ids.size()-1;i>=0;i--){
			terminalInfoDao.insertTerminalOperate(Long.valueOf(ids.get(i)),2,2);
			TerInfo terInfo=terminalInfoDao.selectAllAgentByTerminalId(Long.valueOf(ids.get(i)));
			if(terInfo!=null){
				if(terInfo.getCallbackLock()==0) {
					row += terminalInfoDao.updateOpenStatus(Long.valueOf(ids.get(i)), "0");
					terminalInfoDao.deleteAllAgentTerInfo(terInfo.getId());
					ChangeLog changeLog=new ChangeLog();
					final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					changeLog.setOperater(principal.getId().toString());
					changeLog.setOperMethod("updateOpenStatusBatch");
					changeLog.setRemark("删除");
					changeLog.setChangePre(JSONObject.toJSONString(terInfo));
					changeLogService.insertChangeLog(changeLog);
				}
				ids.remove(ids.get(i));
			}
			if(ids.size()==0){
				break;
			}
		}
		if(ids.size()>0){
			row+=terminalInfoDao.updateOpenStatusBatch(ids, opentStatus);
		}
		return row;
	}

	// 生成随机数字和字母,
	public String getStringRandom(int length) {

		String val = "";
		Random random = new Random();

		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				// int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + 97);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	@Override
	public List<Map<String, String>> selectAllActivityType() {
		return terminalInfoDao.selectAllActivityType();
	}

	@Override
	public int updateAllTerActivity(TerminalInfo record) {
		return terminalInfoDao.updateAllTerActivity(record);
	}

	@Override
	public List<TerminalInfo> selectAllTerActivity(TerminalInfo terInfo) {
		return terminalInfoDao.selectAllTerActivity(terInfo);
	}

	/**
	 * 绑定机具之前，对机具做相应的判断，是否符合绑定条件： 1.要属于当前的代理商 2.未被商户绑定
	 * 
	 * @author tans
	 * @date 2017年4月25日 下午2:02:20
	 * @param sn
	 * @param merchantInfo
	 * @return
	 */
	public Map<String, Object> checkBeforeBind(String sn, MerchantInfo merchantInfo) {
		String agentNo = merchantInfo.getAgentNo();
		String merchantNo = merchantInfo.getMerchantNo();
		String teamId = merchantInfo.getTeamId();
		String recommendedSource = merchantInfo.getRecommendedSource();
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		TerminalInfo oldInfo = terminalInfoDao.getBySn(sn);
		if (oldInfo == null) {
			msg.put("msg", "找不到对应的机具,SN:" + sn);
			return msg;
		}
		if (StringUtils.isBlank(oldInfo.getAgentNo())) {
			msg.put("msg", "机具未分配,SN:" + sn);
			return msg;
		}
		if (!oldInfo.getAgentNo().equals(agentNo)) {
			msg.put("msg", "商户" + merchantNo + "和机具" + sn + "不属于同一个代理商，不能绑定");
			return msg;
		}
		if (StringUtils.isNotBlank(oldInfo.getMerchantNo())) {
			msg.put("msg", "机具已绑定其他商户,SN:" + sn);
			return msg;
		}
		//	校验机具为回拨锁定状态，为是则不能绑定
		//	校验机具是否属于该盟主
		String checkCallBackLockMsg = checkTerCallBackLock(sn, merchantNo);
		if(StringUtils.isNotBlank(checkCallBackLockMsg)){
			msg.put("msg", "sn:" + sn + "," + checkCallBackLockMsg);
			return msg;
		}
        if(! checkTerAndMerTeam(sn, teamId)){
            msg.put("msg", "商户" + merchantNo + "和机具" + sn + "组织不一致，绑定失败");
            return msg;
        }
		String hpId = oldInfo.getType();
		CjtTeamHardware teamHardware = cjtTeamHardwareDao.selectByHpAndTeam(hpId, teamId);
        //超级盟主的商户不能绑定超级推的机具
		if("3".equals(recommendedSource) && teamHardware != null) {
			msg.put("msg", "超级盟主商户不能参与超级推活动，无法绑定超级推机具");
			return msg;
		}
		if("1".equals(recommendedSource) && teamHardware == null) {
			msg.put("msg", "超级推用户只能绑定超级推机具");
			return msg;
		}
		msg.put("status", true);
		return msg;
	}

	@Override
	public int updateBundlingBySn(TerminalInfo terminalInfo) {
		return terminalInfoDao.updateBundlingBySn(terminalInfo);
	}

	@Override
	public List<TerminalInfo> getPageByMerchant(String merchantNo, Page<TerminalInfo> page) {
		return terminalInfoDao.getPageByMerchant(merchantNo, page);
	}

	/**
	 * 根据活动名称获取活动类型
	 */
	@Override
	public String getActivityType(String activityName, List<SysDict> activityList) {
		String activityType = "";
		if (StringUtils.isBlank(activityName) || activityList == null || activityList.isEmpty()) {
			return activityType;
		}
		for (SysDict activity : activityList) {
			if (activityName.equals(activity.getSysName())) {
				activityType = activity.getSysValue();
				break;
			}
		}
		return activityType;
	}

//	/**
//	 * 批量解绑
//	 *
//	 * @throws Exception
//	 */
//	@Override
//	public Map<String, Object> batchUnbundlingById(ArrayList<Long> idList){
//        Map<String, Object> msg = new HashMap<>();
//        msg.put("status", false);
//        msg.put("msg", "操作失败");
//        StringBuilder errMsg = new StringBuilder();
//        int sum = idList.size();
//        int successSum = 0;
//        int failSum = 0;
//		for (Long id : idList) {
//			Map<String, Object> map = updateUnbundlingById(id);
//			if ((boolean) map.get("status")) {
//                successSum++;
//			} else {
//                errMsg.append("机具ID:" + id + "解绑失败，失败原因:" + map.get("msg"));
//            }
//		}
//        failSum = sum - successSum;
//        msg.put("status", true);
//        msg.put("msg", "操作成功,总条数:" + sum + "，成功条数:" + successSum + "，失败条数:" + failSum
//                + "," + errMsg);
//        return msg;
//    }

	/**
	 * checkStatus:是否检查
	 */
	@Override
	public Map<String, Object> updateAllTerActivity(String param, boolean checkStatus) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		JSONObject jsonObject = JSON.parseObject(param);
		String snStart = (String) jsonObject.get("snStart");
		String snEnd = (String) jsonObject.get("snEnd");
		String activityType = (String) jsonObject.get("activityType");
		Integer type = (Integer) jsonObject.get("type");
		String activityTypeNo = (String) jsonObject.get("activityTypeNo");
		if (snStart.length() != snEnd.length()) {
			resMap.put("status", false);
			resMap.put("msg", "输入SN号前后长度不匹配");
			return resMap;
		}
		String regex = "^[A-Za-z0-9]+$";
		if (!(snStart.matches(regex) && snEnd.matches(regex))) {// 只允许数字和英文
			resMap.put("status", false);
			resMap.put("msg", "SN号只能包含数字和英文");
			return resMap;
		}
		boolean isNum = snStart.matches("[0-9]+");
		if (!isNum) {// 不是数字进行字符串前缀匹配
			int i = snStart.length() - 1;
			for (; i >= 0; i--) {
				if ('A' < snStart.charAt(i) && snStart.charAt(i) < 'z') {
					break;
				}
			}
			int j = snEnd.length() - 1;
			for (; j >= 0; j--) {
				if ('A' < snEnd.charAt(j) && snEnd.charAt(j) < 'z') {
					break;
				}
			}
			if (i != j) {
				resMap.put("status", false);
				resMap.put("msg", "输入SN号前后带有英文字符不匹配");
				return resMap;
			}
			if (i != -1 || j != -1) {
				String startStr = snStart.substring(0, i + 1);
				String snEndStr = snEnd.substring(0, i + 1);
				log.info("SN开始截取开始字符串的英文字符=========>" + startStr);
				log.info("SN结尾截取结尾字符串的英文字符=========>" + snEndStr);
				if (!startStr.equals(snEndStr)) {
					resMap.put("status", false);
					resMap.put("msg", "SN号中以英文结束的前字符串必须一样,如1C*****和1C*****");
					return resMap;
				}
			}
		}

		// 进行批量更新机具活动操作
		TerminalInfo terInfo = new TerminalInfo();
		terInfo.setSnStart(snStart);
		terInfo.setSnEnd(snEnd);
		terInfo.setActivityType(activityType);
		terInfo.setType(type.toString());
		terInfo.setActivityTypeNo(activityTypeNo);
		// 查询是否有符合条件且已使用的机具，如果有，就返回
		if(checkStatus){
			List<TerminalInfo> list = terminalInfoDao.selectAllTerActivity(terInfo);
			if (list != null && list.size() > 0) {
				resMap.put("status", false);
				resMap.put("msg", "所输入的机具中包含已使用的机具，请重新输入");
				return resMap;
			}
		}
		int m = terminalInfoDao.updateAllTerActivity(terInfo);
		if (m > 0) {
			resMap.put("status", true);
			resMap.put("msg", "批量修改成功");
		} else {
			resMap.put("status", false);
			resMap.put("msg", "批量修改失败！");
		}
		return resMap;
	}

	/**
	 * 机具批量绑定
	 * @param merchantNo
	 * @param bpId
	 * @param terList
	 * @return
	 * 1）机具必须在acq_terminal_store有记录，才可以绑定，否则，给与提示并返回（status的判断与维护，由core那边处理）；
	 * 2）商户业务产品必须在zq_merchant_info.sync_status为“1同步成功”，report_status为“1已报备”，才可以调用绑定接口，
	 * 若接口返回成功，则进行内部绑定，否则，给与提示并返回；
	 */
	@Override
	public Result batchBindingTerminal(String merchantNo, String bpId, List<TerminalInfo> terList) {
		Result result = new Result();
		int successNum = 0;
		int failNum = 0;
		int sumNum = terList.size();
		Date nowDate = new Date();
		MerchantInfo merchantInfo;
		String sn;
		String acqEnname = "YS_ZQ";
		String coreUrl = "";
		SysDict coreUrlDict = sysDictDao.getByKey("CORE_URL");
		if(coreUrlDict != null){
            coreUrl = coreUrlDict.getSysValue() + Constants.BIND_OR_UNBIND;
        }
		//检查商户是否在银盛报备
		MerchantBusinessProduct mbp = merchantBusinessProductDao.selectMerBusPro(merchantNo, bpId);
		String mbpId = String.valueOf(mbp.getId());
		ZqMerchantInfo zqMerchantInfo = checkSyncMer(merchantNo, mbpId, acqEnname);
		if(zqMerchantInfo != null && "3".equals(zqMerchantInfo.getSyncStatus())){
			result.setMsg("商户同步状态为审核中，不能绑定");
			return result;
		}

        merchantInfo = merchantInfoDao.selectByMerNo(merchantNo);
//        String agentNo = merchantInfo.getAgentNo();
//        String teamId = merchantInfo.getTeamId();
		StringBuilder errMsg = new StringBuilder();
		String type = "0";//绑定
		List<TerminalInfo> updateList = new ArrayList<>();
		String risk130Key=bossSysConfigDao.selectValueByKey("RISK130_KEY");
		String settleCardNo=merchantRequireItemService.selectByAccountNo(merchantNo).getContent();
		String idCardNo=merchantRequireItemService.selectByIdCardNo(merchantNo).getContent();
		//风控规则130调用
		String responseMsg = ClientInterface.risk130(coreUrlDict.getSysValue(),idCardNo,settleCardNo,risk130Key);
		if(StringUtil.isNotBlank(responseMsg)){
			Map<String, Object> responseMap = JSON.parseObject(responseMsg).getJSONObject("header");
			Boolean succeed = (Boolean) responseMap.get("succeed");
			if(!succeed){
				String msg = (String) responseMap.get("errMsg");
				result.setMsg(msg);
				return result;
			}
		}
		for (TerminalInfo terminalInfo : terList) {
			sn = terminalInfo.getSn();
			//绑定机具前，进行的所有校验
			Map<String, Object> checkMap = checkBeforeBind(sn, merchantInfo);
			if (!(boolean) checkMap.get("status")) {
				// 如果check不符合，则下一条
				errMsg.append(String.valueOf(checkMap.get("msg")) + ";");
				continue;
			}

			//检查机具是否报备
            boolean syncTerStatus = checkExistsAcqTer(sn, acqEnname);
			if (zqMerchantInfo != null && "1".equals(zqMerchantInfo.getSyncStatus())){
                if(!syncTerStatus){
                    errMsg.append("机具" + sn + "在" + acqEnname +"找不到记录，不能绑定");
                    log.info("机具{}在{}找不到记录，不能绑定",sn, acqEnname);
                    continue;
                } else {
                    //调用core接口进行绑定
                    String returnStr = ClientInterface.bindingOrUnBindTerminal(coreUrl, sn, merchantNo, mbpId, type);
                    if(StringUtils.isNotBlank(returnStr)){
                        JSONObject json = JSONObject.parseObject(returnStr);
                        JSONObject headJson = JSONObject.parseObject(json.getString("header"));
                        if(!headJson.getBoolean("succeed")){
                            errMsg.append("sn:" + sn + ",调用接口绑定失败," + headJson.getString("errMsg") +";");
                            log.info("调用接口绑定失败,失败提示:{}", headJson.getString("errMsg"));
                            continue;
                        }
                    } else {
                        errMsg.append("sn:" + sn + ",调用接口失败;");
                        log.info("调用接口失败");
                        continue;
                    }
                }
			}
			//如果是超级推机具，需要调用core接口，新增超级推的相关逻辑
			String bindMsg = cjtTerBind(sn, merchantInfo);
//			if(StringUtils.isNotEmpty(bindMsg)) {
//				errMsg.append("sn:" + sn + ",调用超级推接口失败," + bindMsg + ";");
//				continue;
//			}

            terminalInfo.setMerchantNo(merchantNo);
            terminalInfo.setBpId(bpId);
            terminalInfo.setStartTime(nowDate);
            terminalInfo.setOpenStatus("2");
            updateList.add(terminalInfo);
		}
		if(updateList != null && updateList.size() > 0){
            successNum += batchBundlingBySn(updateList);
        }
        failNum = sumNum - successNum;
        result.setStatus(true);
        result.setMsg("操作成功,总条数:" + sumNum + "成功条数:" + successNum + ",失败条数:" + failNum + ";"
            + errMsg);
		return result;
	}

	/**
	 * 超级推机具绑定时，增加的逻辑
	 * @param sn
	 * @param merchantInfo
	 * @return
	 */
	private String cjtTerBind(String sn, MerchantInfo merchantInfo) {
		try {
			String merchantNo = merchantInfo.getMerchantNo();
			String teamId = merchantInfo.getTeamId();
			TerminalInfo terInfo = terminalInfoDao.getTerminalInfoBySn(sn);
			String hpId = terInfo.getType();
			CjtTeamHardware teamHardware = cjtTeamHardwareDao.selectByHpAndTeam(hpId, teamId);
			//不是超级推机具则返回
			if(teamHardware == null) {
				return null;
			}
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("sn", sn);
			paramMap.put("merchantNo", merchantNo);
			String key = bossSysConfigDao.selectValueByKey(Constants.CORE_KEY);
			String signData = CommonUtil.sortASCIISign(paramMap, key);
			paramMap.put("signData", signData);

			String coreUrl = sysDictDao.getFirstValueByKey(Constants.CORE_URL);
			String url = coreUrl + CjtConstant.CJT_BIND_MER;
			String returnMsg = ClientInterface.baseNoClient(url, paramMap);
//		if(StringUtils.isNotEmpty(returnMsg)) {
//			JSONObject json = JSONObject.parseObject(returnMsg);
//			JSONObject headJson = json.getJSONObject("header");
//			if(headJson.getBoolean("succeed")) {
//				return "";
//			} else {
//				return headJson.getString("errMsg");
//			}
//		}
		} catch (Exception e){
			log.error("调用绑定超级推接口失败", e);
		}

//		return "绑定超级推机具调用接口失败";
		return null;
	}

	/**
	 * 校验机具为回拨锁定状态，为是则不能绑定
	 * 校验机具是否属于该盟主
	 * @param sn
	 * @param merNo
	 * @return
	 */
	private String checkTerCallBackLock(String sn, String merNo) {
		String checkMsg = "";
		TerInfo terInfo=terminalInfoDao.selectAllAgentByTerminalSN(sn);
		if(terInfo!=null) {
			if (terInfo.getCallbackLock() == 1) {
				checkMsg = "机具为回拨锁定状态，不能绑定";
				log.info(checkMsg);
			} else {
				MerchantAllAgent merchantAllAgent = merchantAllAgentService.queryMerchantAllAgentByMerNo(merNo);
				if (!merchantAllAgent.getUserCode().equals(terInfo.getUserCode())) {
					checkMsg = "机具不属于该盟主，不能绑定";
					log.info(checkMsg);
				}
			}
		}
		return checkMsg;
	}

    /**
     * 比较商户和机具的组织ID
     * 相同则返回true
     * @param sn
     * @param teamId
     * @return
     */
    private boolean checkTerAndMerTeam(String sn, String teamId) {
        HardwareProduct hardwareProduct = hardwareProductDao.selectBySn(sn);
        if(hardwareProduct == null){
            return false;
        }
        if(teamId.equals(String.valueOf(hardwareProduct.getOrgId()))){
            return true;
        }
        return false;
    }

//            1.银盛商户绑定银盛机具，只能商户同步成功，调用接口，再内部绑定
//            3.银盛商户绑定其他机具，不能绑定
//            4.其他商户绑定银盛机具，内部绑定
//            5.其他商户绑定其他机具，内部绑定
    @Override
    public Map<String, Object> bindBatch(InputStream inputStream, String agentNo, String merchantNo, String bpId)
            throws IOException, InvalidFormatException {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        Workbook wb = null;
        wb = WorkbookFactory.create(inputStream);
        Sheet sheet = wb.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        if (rowNum > 5000) {
            msg.put("msg", "机具批量绑定失败,不能超过5000条记录");
            return msg;
        }

        String acqEnname = "YS_ZQ";
        List<TerminalInfo> updateList = new ArrayList<>();
        String coreUrl = "";
        SysDict coreUrlDict = sysDictDao.getByKey("CORE_URL");
        if(coreUrlDict != null){
            coreUrl = coreUrlDict.getSysValue() + Constants.BIND_OR_UNBIND;
        }
        String type = "0";//绑定
        Date nowDate = new Date();
        //检查商户是否在银盛报备
        MerchantBusinessProduct mbp = merchantBusinessProductDao.selectMerBusPro(merchantNo, bpId);
        String mbpId = String.valueOf(mbp.getId());
        ZqMerchantInfo zqMerchantInfo = checkSyncMer(merchantNo, mbpId, acqEnname);
        if(zqMerchantInfo != null && "3".equals(zqMerchantInfo.getSyncStatus())){
            msg.put("msg","商户同步状态为审核中，不能绑定");
            return msg;
        }
        MerchantInfo merchantInfo = merchantInfoDao.selectByMerNo(merchantNo);
		agentNo = merchantInfo.getAgentNo();//代理商取商户的，页面上传来的代理商编号不可信任不安全
        StringBuilder msgStr = new StringBuilder();
		String risk130Key=bossSysConfigDao.selectValueByKey("RISK130_KEY");
		String settleCardNo=merchantRequireItemService.selectByAccountNo(merchantNo).getContent();
		String idCardNo=merchantRequireItemService.selectByIdCardNo(merchantNo).getContent();
		//风控规则130调用
		String responseMsg = ClientInterface.risk130(coreUrlDict.getSysValue(),idCardNo,settleCardNo,risk130Key);
		if(StringUtil.isNotBlank(responseMsg)){
			Map<String, Object> responseMap = JSON.parseObject(responseMsg).getJSONObject("header");
			Boolean succeed = (Boolean) responseMap.get("succeed");
			if(!succeed){
				String errMsg = (String) responseMap.get("errMsg");
				msg.put("msg",errMsg);
				return msg;
			}
		}
        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            Cell cell1 = row.getCell(1);
            String sn = "";// 机具SN号
            if (cell1 != null) {
                sn = CellUtil.getCellValue(cell1);
            }
            if(StringUtils.isBlank(sn)){
                msgStr.append("第" + i+1 + "行sn不能为空;");
                continue;
            }
			//绑定机具前，进行的所有校验
            Map<String, Object> checkMap = checkBeforeBind(sn, merchantInfo);
            if (!(boolean) checkMap.get("status")) {
                // 如果check不符合，则下一条
                msgStr.append(String.valueOf(checkMap.get("msg")) + ";");
                continue;
            }
            boolean syncTerStatus = checkExistsAcqTer(sn, acqEnname);
            //1.银盛商户绑定银盛机具，只能商户同步成功，调用接口，再内部绑定
			//否则可以直接绑定，为存量机具考虑的
            if(zqMerchantInfo != null && "1".equals(zqMerchantInfo.getSyncStatus()) ){
                if(!syncTerStatus){
                    msgStr.append("机具" + sn + "在" + acqEnname +"找不到记录，不能绑定;");
                    log.info("机具{}在{}找不到记录，不能绑定",sn, acqEnname);
                    continue;
                } else {
                    //调用core接口进行绑定
                    String returnStr = ClientInterface.bindingOrUnBindTerminal(coreUrl, sn, merchantNo, mbpId, type);
                    if(StringUtils.isNotBlank(returnStr)){
                        JSONObject json = JSONObject.parseObject(returnStr);
                        JSONObject headJson = JSONObject.parseObject(json.getString("header"));
                        if(!headJson.getBoolean("succeed")){
                            msgStr.append("sn:" + sn + ",调用接口绑定失败," + headJson.getString("errMsg") +";");
                            log.info("调用接口绑定失败,失败提示:{}", headJson.getString("errMsg"));
                            continue;
                        }
                    } else {
                        msgStr.append("sn:" + sn + ",调用接口失败;");
                        log.info("调用接口绑定失败");
                        continue;
                    }
                }
            }
			//如果是超级推机具，需要调用core接口，新增超级推的相关逻辑
			String bindMsg = cjtTerBind(sn, merchantInfo);
//			if(StringUtils.isNotEmpty(bindMsg)) {
//				msgStr.append("sn:" + sn + ",调用超级推接口失败," + bindMsg + ";");
//				continue;
//			}
			TerminalInfo terminalInfo = new TerminalInfo();
			terminalInfo.setSn(sn);
			terminalInfo.setAgentNo(agentNo);
			terminalInfo.setMerchantNo(merchantNo);
            terminalInfo.setBpId(bpId);
            terminalInfo.setStartTime(nowDate);
            terminalInfo.setOpenStatus("2");
            updateList.add(terminalInfo);
        }
        int successSum = 0;
        int failSum = 0;
        successSum += batchBundlingBySn(updateList);
        failSum = rowNum - successSum;
        wb.close();
        msg.put("status", true);
        msg.put("msg", "总条数:" + rowNum +",成功条数:" + successSum + ",失败条数:" + failSum
                + ";" + msgStr);
        return msg;
    }

    /**
     * 批量绑定机具
     * @param updateList
     * @return
     */
    private int batchBundlingBySn(List<TerminalInfo> updateList) {
        int num = 0;
        if(updateList == null || updateList.size() < 1){
            return num;
        }
        //由于批量update 的sql有问题，改成单挑upate
//        List<TerminalInfo> itemList = new ArrayList<>();
        for (int i=0; i<updateList.size(); i++){
//            itemList.add(updateList.get(i));
//            if(itemList.size() >= 200){
//                num += terminalInfoDao.batchBundlingBySn(itemList);
//                itemList.clear();
//            }
			TerInfo terInfo=terminalInfoDao.selectAllAgentByTerminalSN(updateList.get(i).getSn());
			if(terInfo!=null){
				ChangeLog changeLog=new ChangeLog();
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				changeLog.setOperater(principal.getId().toString());
				changeLog.setOperMethod("batchBundlingBySn");
				changeLog.setRemark("批量绑定机具");
				changeLog.setChangePre(JSONObject.toJSONString(terInfo));
				terminalInfoDao.updateAllAgentTerInfo(terInfo.getId(),3,updateList.get(i).getMerchantNo());
				terInfo.setStatus(3);
				terInfo.setMerchantNo(updateList.get(i).getMerchantNo());
				changeLog.setChangeAfter(JSONObject.toJSONString(terInfo));
				changeLogService.insertChangeLog(changeLog);
				int num1=terminalInfoDao.updateBundlingBySn(updateList.get(i));
				if(num1>0){
					terminalInfoDao.insertPushRecord(updateList.get(i));
				}
				num +=num1;
			}else{
				int num1=terminalInfoDao.updateBundlingBySn(updateList.get(i));
				if(num1>0){
					terminalInfoDao.insertPushRecord(updateList.get(i));
				}
				num +=num1;
			}
        }
//        if(itemList.size() > 0){
//            num += terminalInfoDao.batchBundlingBySn(itemList);
//        }
        return num;
    }

    /**
	 * 检查商户是否同步成功
	 * @param merNo
	 * @param mbpId
	 * @param acqEnname
	 * @return
	 */
	private ZqMerchantInfo checkSyncMer(String merNo, String mbpId, String acqEnname) {
        ZqMerchantInfo zqMerchantInfo = zqMerchantInfoDao.selectByMerMbpAcq(merNo, mbpId, acqEnname);
		return zqMerchantInfo;
	}

	/**
	 * 检查机具是否报备
	 * @param sn
	 * @param acqEnname
	 * @return
	 */
	private boolean checkExistsAcqTer(String sn, String acqEnname) {
		AcqTerminalStore acqTer = acqTterminalStoreDao.selectBySnAcq(sn, acqEnname);
		if(acqTer != null){
			return true;
        }
		return false;
	}

	public List<TerInfo> querySNList(TerInfo info,Page<TerInfo> page){
		List<TerInfo> terInfolist=terminalInfoDao.querySNList(info,page);
		Map<String, String> activityTypeMap=sysDictService.selectMapByKey("ACTIVITY_TYPE");
		String[] strArr = {};
		if (terInfolist != null) {
			for (TerInfo t : terInfolist) {
				String activityTypeNameStr = "";
				if (t != null && StringUtils.isNotBlank(t.getActivityType())) {
					strArr = t.getActivityType().split(",");
					String activityTypeName="";
					for (int i = 0; i < strArr.length; i++) {
						activityTypeNameStr = activityTypeMap.get(strArr[i]);
						if(activityTypeNameStr!=null&&!"".equals(activityTypeNameStr)){
							activityTypeName+=activityTypeNameStr+",";
						}
					}
					// 如果有逗号，去掉最后那个逗号
					if (activityTypeName!=null&&!"".equals(activityTypeName)) {
						activityTypeNameStr = activityTypeName.substring(0, activityTypeName.length() - 1);
					}
					t.setActivityTypeName(activityTypeNameStr);
				}
			}
		}
		return terInfolist;
	}

	public List<TerInfo> querySNByTerInfo(TerInfo info){
		return terminalInfoDao.querySNByTerInfo(info);
	}

	public List<TerInfo> queryShipMachineDetail(String orderNo, Page<TerInfo> page){
		return terminalInfoDao.queryShipMachineDetail(orderNo,page);
	}

	@Override
	public void importDetail(List<TerminalInfo> list, HttpServletResponse response) throws Exception{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "机具列表"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
			maps.put("sn",null);
			maps.put("terNo",null);
			maps.put("unionMerNo",null);
			maps.put("type",null);
			maps.put("merchantNo",null);
			maps.put("merchantName",null);
			maps.put("bpName",null);
			maps.put("userCode",null);
			maps.put("realName",null);
			maps.put("oneAgentName",null);
			maps.put("agentName",null);
			maps.put("terminalId",null);
			maps.put("openStatus",null);
			maps.put("hasKey",null);
			maps.put("channel",null);
			maps.put("startTime", null);
			maps.put("activityTypeName",null);
			maps.put("activityTypeNoName",null);
			data.add(maps);
		}else{
			Map<String,String> openStatusMap = getOpenStatusMap();

			for (TerminalInfo or : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id",or.getId()+"");
				maps.put("sn",or.getSn()==null?"":or.getSn());
				maps.put("terNo",or.getTerNo()==null?"":or.getTerNo());
				maps.put("unionMerNo",or.getUnionMerNo()==null?"":or.getUnionMerNo());
				if(or.getType()!=null){
					HardwareProduct info=hardwareProductService.getHardwareProductByBpId(Long.parseLong(or.getType()));
					maps.put("type",info==null?"":info.getTypeName()+info.getVersionNu());
				}else{
					maps.put("type","");
				}
				maps.put("merchantNo",or.getMerchantNo()==null?"":or.getMerchantNo());
				maps.put("merchantName",or.getMerchantName()==null?"":or.getMerchantName());
				maps.put("bpName",or.getBpName()==null?"":or.getBpName());
				maps.put("userCode",or.getUserCode()==null?"":or.getUserCode());
				maps.put("realName",or.getRealName()==null?"":or.getRealName());
				maps.put("oneAgentName",or.getOneAgentName()==null?"":or.getOneAgentName());
				maps.put("agentName",or.getAgentName()==null?"":or.getAgentName());
				maps.put("terminalId",or.getTerminalId()==null?"":or.getTerminalId());
				maps.put("openStatus",openStatusMap.get(or.getOpenStatus()));
				if(or.getHasKey()!=null&&"1".equals(or.getHasKey().toString())){
					maps.put("hasKey","是");
				}else{
					maps.put("hasKey","否");
				}
				maps.put("channel",or.getChannel()==null?"":or.getChannel());
				maps.put("startTime", or.getStartTime()==null?"":sdf1.format(or.getStartTime()));
				maps.put("activityTypeName",or.getActivityTypeName()==null?"":or.getActivityTypeName());
				maps.put("activityTypeNoName",or.getActivityTypeNoName()==null?"":or.getActivityTypeNoName());


				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","sn","terNo","unionMerNo","type","merchantNo",
				"merchantName","bpName","userCode","realName","oneAgentName","agentName","terminalId",
				"openStatus","hasKey","channel","startTime","activityTypeName","activityTypeNoName"
		};
		String[] colsName = new String[]{"序号","SN号","银联终端号","银联报备商户号","硬件产品种类","商户编号",
				"商户简称","业务产品","所属盟主编号","所属盟主姓名","一级代理商","所属代理商","终端号",
				"机具状态","是否有密钥","机具通道","启用时间","机具活动类型","欢乐返子类型"
		};
		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("导出机具列表失败!",e);
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}
	}


	public Map<String, String> getOpenStatusMap(){
		Map<String,String> map = new HashMap<>();
		map.put("0","已入库");
		map.put("1","已分配");
		map.put("2","已使用");
		map.put("3","申请中");
		return map;
	}


	public String createPns(){
		List<SysDict> sysDicts = sysDictService.selectByKey("TER_ACTIVITY_DEVICE_PN");
		StringBuilder pns = new StringBuilder("(");
		for (int i = 0; i < sysDicts.size(); i++) {
			if(i != sysDicts.size() - 1){
				pns.append("'" + sysDicts.get(i).getSysValue() + "',");
			}else {
				pns.append("'" + sysDicts.get(i).getSysValue() + "')");
			}
		}
		return pns.toString();
	}

	@Override
	public void exportTerminalWithTAC(HttpServletResponse response, TerminalInfo terminalInfo) throws Exception{

		List<TerminalInfo> list = terminalInfoDao.exportTerminalWithTAC(terminalInfo, createPns());
		//如果已达标，考核剩余天数显示已达标
		for (TerminalInfo ter : list) {
			if(ter.getDueDays() > 0 && "1".equals(ter.getStatus())){
				ter.setDueDaysValue("已达标");
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "机具活动考核列表"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("sn",null);
			maps.put("typeName",null);
			maps.put("bpName",null);
			maps.put("oneAgentNo",null);
			maps.put("oneAgentName",null);
			maps.put("agentNo",null);
			maps.put("agentName",null);
			maps.put("checkTime",null);
			maps.put("dueDaysValue",null);
			maps.put("status",null);
			maps.put("openStatus",null);
			maps.put("activityTypeNoName",null);
			data.add(maps);
		}else{
			Map<String,String> openStatusMap = getOpenStatusMap();

			for (TerminalInfo info : list) {
				Map<String, String> maps = new HashMap<>();
				maps.put("sn", info.getSn());
				maps.put("typeName", info.getTypeName());
				maps.put("bpName", info.getBpName());
				maps.put("oneAgentNo", info.getOneAgentNo());
				maps.put("oneAgentName", info.getOneAgentName());
				maps.put("agentNo", info.getAgentNo());
				maps.put("agentName", info.getAgentName());
				maps.put("checkTime", sdf1.format(info.getCheckTime()));
				maps.put("dueDaysValue", info.getDueDaysValue());
				maps.put("status", "1".equals(info.getStatus())?"已激活":"未激活");
				maps.put("openStatus", openStatusMap.get(info.getOpenStatus()));
				maps.put("activityTypeNoName", info.getActivityTypeNoName());

				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"sn","typeName","bpName","oneAgentNo","oneAgentName",
				"agentNo","agentName","checkTime","dueDaysValue","status","openStatus","activityTypeNoName"
		};
		String[] colsName = new String[]{"SN号","硬件产品种类","业务产品","一级代理商编号","一级代理商名称","所属代理编号",
				"所属代理商名称","考核日期","考核剩余天数","激活状态","机具状态","欢乐返子类型"
		};
		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("导出机具活动考核列表失败!",e);
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}
	}



	@Override
	public Map<String, Object> downloadResult(String batchNo, HttpServletResponse response,Map<String, Object> msg) throws Exception{
		List<ImportLogEntry> list=importLogService.getImportLogEntryList(batchNo);
		if(list==null||list.size()<=0){
			msg.put("status", false);
			msg.put("msg", "无下载结果文件!");
			return msg;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "机具批量导入修改结果"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;

		for (ImportLogEntry or : list) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("data1",or.getData1()==null?"":or.getData1());
			maps.put("data2",or.getData2()==null?"":or.getData2());
			maps.put("result",or.getResult()==null?"":or.getResult());
			data.add(maps);
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"data1","data2","result"};
		String[] colsName = new String[]{"机具SN号","硬件产品类型","处理结果"};
		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("机具批量导入修改结果失败!",e);
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}
		return msg;
	}

	@Override
	public List<TerInfo> queryHlfBindSn(String activityTypeNo){
		return terminalInfoDao.queryHlfBindSn(activityTypeNo);
	}

	@Override
	public int insertTerminalOperate(Long id, Integer oper_detail_type,Integer oper_type){
		return terminalInfoDao.insertTerminalOperate(id,oper_detail_type,oper_type);
	}

	@Override
	public int insertAgentRerminalOperate(String agentNo,String sn, Integer oper_detail_type, Integer oper_type){
		return terminalInfoDao.insertAgentRerminalOperate(agentNo,sn,oper_detail_type,oper_type);
	}

	@Override
	public void selectCjtTerPage(TerminalInfo baseInfo, Page<TerminalInfo> page) {
		if(StringUtils.isEmpty(baseInfo.getType())){
			StringBuilder typeSb = new StringBuilder();
			List<CjtTeamHardware> cjtTeamHardwareList = cjtTeamHardwareDao.selectHpIdList();
			for(CjtTeamHardware item: cjtTeamHardwareList) {
				typeSb.append(item.getHpId()).append(",");
			}
			baseInfo.setType(typeSb.toString());
		}
		terminalInfoDao.selectCjtTerPage(baseInfo, page);
		if(page != null && page.getResult() != null && page.getResult().size() >0){
			dataPostprocessingList(page.getResult());
		}
		return;
	}

	@Override
	public int insertOne(TerActivityCheck tac) {
		return terminalInfoDao.insertOne(tac);
	}


	@Override
	public Map<String, Object> changeDueDays(MultipartFile file, HttpServletRequest request) throws Exception {
		log.info("修改机具活动考核文件导入------------------------------------");
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("result", false);
		jsonMap.put("msg", "文件导入失败！");

		int errorCount=0;
		int successCount=0;
		List<Map> errorlist = new ArrayList<Map>();
		try {
			if (!file.isEmpty()) {
				// 遍历所有单元格，读取单元格
				Workbook wb = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = wb.getSheetAt(0);
				int row_num = sheet.getLastRowNum();
				for (int i = 1; i <= row_num; i++) {
					Map errorMap=new HashMap();
					Row row = sheet.getRow(i);
					String sn = CellUtil.getCellValue(row.getCell(0));
					String days = CellUtil.getCellValue(row.getCell(1));

					TerActivityCheck tac = new TerActivityCheck();
					errorMap.put("sn",sn);

					int rowNum=i+1;
					if (StringUtils.isBlank(sn)) {
						errorCount++;
						errorMap.put("errorResult","第" + rowNum + "行，sn不能为空");
						errorlist.add(errorMap);
						continue;
					}
					if (StringUtils.isBlank(days)) {
						errorCount++;
						errorMap.put("errorResult","第" + rowNum + "行，延期天数不能为空");
						errorlist.add(errorMap);
						continue;
					}else if(days.contains(".")){
						int index = days.indexOf(".");
						days = days.substring(0, index);
					}
					if(!days.matches("^[-\\+]?[0-9]{0,3}")){
						errorCount++;
						errorMap.put("errorResult","第" + rowNum + "行，延期天数不符合要求");
						errorlist.add(errorMap);
						continue;
					}

					TerminalInfo info = terminalInfoDao.selectBySn(sn);
					if(null == info){
						errorCount++;
						errorMap.put("errorResult","第" + rowNum + "行，机具不存在");
						errorlist.add(errorMap);
						continue;
					}

					TerActivityCheck hasTac = terminalInfoDao.selectTACBySn(sn);
					if(null == hasTac){
						errorCount++;
						errorMap.put("errorResult","第" + rowNum + "行，机具未参与活动考核");
						errorlist.add(errorMap);
						continue;
					}

					tac.setSn(sn);//机具号
					int dueDays = hasTac.getDueDays();
					// 查询机具达标状态
					if(dueDays > 0 && 1==hasTac.getStatus()){
						errorCount++;
						errorMap.put("errorResult","第" + rowNum + "行，考核机具已达标，无法更改考核剩余天数");
						errorlist.add(errorMap);
						continue;
					}
					tac.setDueDays(new Integer(dueDays) + new Integer(days));//剩余考核天数

					int num = terminalInfoDao.updateDueDays(tac);

					if(num>0){
						successCount++;
					}

				}
				jsonMap.put("errorCount", errorCount);
				jsonMap.put("successCount", successCount);
				jsonMap.put("errorlist", errorlist);
				jsonMap.put("result", true);
				jsonMap.put("msg", "机具导入成功");
			} else {
				jsonMap.put("result", false);
				jsonMap.put("msg", "机具文件格式错误");
			}
		} catch (Exception e) {
			jsonMap.put("result", false);
			jsonMap.put("msg", "文件导入异常！");
			log.error("修改机具活动考核剩余天数文件导入异常,", e);
		}
		return jsonMap;
	}
}
