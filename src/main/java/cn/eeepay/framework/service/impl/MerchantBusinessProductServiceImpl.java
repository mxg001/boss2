package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.action.MerchantBusinessProductAction.SelectParams;
import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.allAgent.MerchantAllAgent;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.allAgent.MerchantAllAgentService;
import cn.eeepay.framework.util.*;
import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("merchantBusinessProductService")
@Transactional
public class MerchantBusinessProductServiceImpl implements MerchantBusinessProductService {

    private Logger log = LoggerFactory.getLogger(MerchantBusinessProductServiceImpl.class);

	@Resource
	private MerchantBusinessProductDao merchantBusinessProductDao;

	//商户银行卡
	@Resource
	private MerchantCardInfoService merchantCardInfoService;

	//审核记录
	@Resource
	private ExaminationsLogService examinationsLogService;

	//商户信息
	@Resource
	private MerchantInfoService merchantInfoService;

	//商户服务限额信息
	@Resource
	private MerchantServiceQuotaService merchantServiceQuotaService;

	//商户服务签约费率
	@Resource
	private MerchantServiceRateService merchantServiceRateService;

	//商户进件条件表(明细)
	@Resource
	private MerchantRequireItemService merchantRequireItemService;

	@Resource
	private RouteGroupDao routeGroupDao;

	@Resource
	private UserService userService;

	@Resource
	private PosCardBinService posCardBinService;

	@Resource
	private BusinessProductInfoDao businessProductInfoDao;

	@Resource
	private AgentInfoService agentInfoService;

	@Resource
	private SysDictService sysDictService;
	@Resource
	private BossSysConfigService bossSysConfigService;

	@Resource
	private AreaInfoService areaInfoService;

	@Resource
	private MerchantRequireHistoryService merchantRequireHistoryService;

	@Resource
	private SysDictDao sysDictDao;

	@Resource
	private SensitiveWordsService sensitiveWordsService;

	@Resource
    private  ZqMerchantInfoDao zqMerchantInfoDao;

	@Resource
	private MerchantAllAgentService merchantAllAgentService;
	@Resource
	private ChangeLogService changeLogService;

	@Resource
	private TeamInfoDao teamInfoDao;

	@Resource
	private YfbOemServiceDao yfbOemServiceDao;

	@Override
	public MerchantBusinessProduct selectByPrimaryKey(Long id) {
 		return merchantBusinessProductDao.selectByPrimaryKey(id);
	}


	@Override
	public List<MerchantBusinessProduct> selectAllByStatusInfo(Page<MerchantBusinessProduct> page) {
		return merchantBusinessProductDao.selectAllByStatusInfo(page);
	}

	@Override
	public List<MerchantBusinessProduct> selectMerBusProByIds(List<String> ids) {
		return merchantBusinessProductDao.selectMerBusProByIds(ids);
	}
	@Override
	public int updateStatusById(String status,String id) {
		return merchantBusinessProductDao.updateStatusById(status,id);
	}

	@Override
	public List<MerchantBusinessProduct> selectByParam(Page<MerchantBusinessProduct> page, SelectParams selectParams) {
		if(selectParams.getProvince()!=null&&!"".equals(selectParams.getProvince())){
			Map<String,Object> map=areaInfoService.getProvincebyId(Integer.valueOf(selectParams.getProvince()));
			selectParams.setProvince(map.get("name").toString());
		}
		merchantBusinessProductDao.selectByParam(page, selectParams);
		List<MerchantBusinessProduct> list = page.getResult();
		getTeamName(list);
		page.setResult(list);
		return list;
	}

	/**
	 * 获取商户的组织名称
	 * @param list
	 */
	private void getTeamName(List<MerchantBusinessProduct> list) {
		//商户的组织编号是根据oem来的，对这些商户的组织名称进行处理
		//本次处理6开头的，比如超级还600010、超级银行家610010
		//数据字典配置60开头是超级还，61开头是超级银行家 71开头是超级兑分销版
		if(list == null || list.size() < 1) {
			return;
		}
		for(MerchantBusinessProduct merchant: list){
			if(merchant.getTeamId() != null && merchant.getTeamId().length() > 2){
				if(merchant.getTeamId().startsWith("6")||merchant.getTeamId().startsWith("71")){
					List<Map<String,Object>> teamInfoList=merchantBusinessProductDao.getTeamInfo(merchant.getTeamId().substring(0,2));
					if(teamInfoList!=null&&teamInfoList.size()>0){
						merchant.setTeamName(teamInfoList.get(0).get("team_name").toString());
					}
				}
			}
		}
	}

	@Override
	public int updateBymbpId(Long mbpId, String status, String auditor) {
		return merchantBusinessProductDao.updateBymbpId(mbpId, status, auditor);
	}

	@Override
	public int reexamineBymbpId(Long mbpId, String status, String auditor) {
		return merchantBusinessProductDao.reexamineBymbpId(mbpId, status, auditor);
	}

	@Override
	public int resetReexamineBymbpId(Long mbpId, String status, Date time) {
		return merchantBusinessProductDao.resetReexamineBymbpId(mbpId, status, time);
	}

	@Override
	public Map<String, Object> updateByItemAndMbpId(MerchantBusinessProduct merchantBusinessProduct,
			MerchantInfo merchantInfo, List<MerchantRequireItem> merchantRequireItemList,
			List<MerchantServiceRate> merchantServiceRateList, List<MerchantServiceQuota> merchantServiceQuotaList,Integer userId,UserModel userModel) {
		int i = 0;
		Map<String, Object> jsonMap = new HashMap<>();
		//校验商户名称和商户经营地址，是否包含敏感词
		if(sensitiveWordsService.selectContain(merchantInfo.getMerchantName()) ){
			throw new RuntimeException("修改失败，商户名称包含敏感词");
		}
		i = merchantBusinessProductDao.updateByItemAndMbpId(merchantBusinessProduct);
		if (i != 1) {
			throw new RuntimeException("商户业务产品信息修改失败");
		}
		int nu = 0;
		String logAccount = "";
		String account = "";
		for (MerchantRequireItem merchantRequireItem : merchantRequireItemList) {
			if (merchantRequireItem.getMriId().equals("3")) {
				logAccount = merchantRequireItem.getLogContent();
				account = merchantRequireItem.getContent();
				break;
			}
		}
		MerchantCardInfo merchantCardInfo = new MerchantCardInfo();
		merchantCardInfo.setMerchantNo(merchantInfo.getMerchantNo());
		merchantCardInfo.setLogAccountNo(logAccount);
		MerchantCardInfo mci = merchantCardInfoService.selectByMertIdAndAccountNo(merchantCardInfo);
		if(mci==null){
			throw new RuntimeException("商户开户账号和默认结算卡不匹配");
		}
		if(!logAccount.equals(account)){//新旧结算卡不一致，修改过结算卡
			jsonMap.put("changeSettleCard", 1);
		}
		String idCardNo="";
		String settleCardNo="";
		String coreUrl=sysDictService.getValueByKey("CORE_URL");
		String risk130Key=bossSysConfigService.selectValueByKey("RISK130_KEY");
		for (MerchantRequireItem merchantRequireItem : merchantRequireItemList) {
			if (merchantRequireItem.getMriId().equals("37")){//临时解决方法经营范围不作修改
				continue;
			}

			if (!merchantBusinessProduct.getStatus().equals("1") && !merchantBusinessProduct.getStatus().equals("2") && !merchantBusinessProduct.getStatus().equals("0")) {
				//开户身份证
				if (merchantRequireItem.getMriId().equals("6") && merchantRequireItem.getStatus().equals("1") ){
					if(checkIcCardNoTitle(merchantRequireItem.getContent())){
						merchantInfo.setIdCardNo(merchantRequireItem.getContent());
					}else{
						throw new RuntimeException("暂不支持进件!");
					}
				}

				//开户行全称
				if (merchantRequireItem.getMriId().equals("4") && merchantRequireItem.getStatus().equals("1")) {
					mci.setBankName(merchantRequireItem.getContent());
					userModel.setBankName(merchantRequireItem.getContent());
				}
				//卡号
				if (merchantRequireItem.getMriId().equals("3") && merchantRequireItem.getStatus().equals("1")) {
					settleCardNo=merchantRequireItem.getContent();
					mci.setAccountNo(merchantRequireItem.getContent());
				}
				//开户名
				if (merchantRequireItem.getMriId().equals("2") && merchantRequireItem.getStatus().equals("1")) {
					mci.setAccountName(merchantRequireItem.getContent());
				}
				//账户类型
				if (merchantRequireItem.getMriId().equals("1") && merchantRequireItem.getStatus().equals("1")) {
					if (merchantRequireItem.getContent().equals("对公")) {
						mci.setAccountType("1");
					} else {
						mci.setAccountType("2");
					}
				}
				//联行行号
				if (merchantRequireItem.getMriId().equals("5") && merchantRequireItem.getStatus().equals("1"))
					mci.setCnapsNo(merchantRequireItem.getContent());
				//经营地址拆分
				if (merchantRequireItem.getMriId().equals("7") && merchantRequireItem.getStatus().equals("1")) {
					String content = merchantRequireItem.getContent();
					String[] str = content.split("-");
					if (str.length <= 3) {
						throw new RuntimeException("经营地址有误");
					}
					if(sensitiveWordsService.selectContain(str[3]) ){
						throw new RuntimeException("修改失败，商户经营详细地址包含敏感词");
					}
					nu = 1;
					merchantInfo.setProvince(str[0]);
					merchantInfo.setCity(str[1]);
					merchantInfo.setDistrict(str[2]);
					merchantInfo.setAddress(str[0] + str[1] + str[2] + str[3]);
				}
				//开户地址拆分
				if (merchantRequireItem.getMriId().equals("15") && merchantRequireItem.getStatus().equals("1")) {
					String content = merchantRequireItem.getContent();
					String[] str = content.split("-");
					if (str.length <= 2) {
						throw new RuntimeException("开户地址有误");
					}
					mci.setAccountProvince(str[0]);
					mci.setAccountCity(str[1]);
					mci.setAccountDistrict(str[2]);
				}
			}

			if (merchantRequireItem.getExampleType().equals("1"))
				continue;
			MerchantRequireItem mrit = merchantRequireItemService.selectByMriId(merchantRequireItem.getMriId(), merchantRequireItem.getMerchantNo());

			if (merchantRequireItem.getMriId().equals("6") ){
				idCardNo=merchantRequireItem.getContent();
			}
			if (!mrit.getContent().equals(merchantRequireItem.getContent())) {
				//风控规则130调用
				if (merchantRequireItem.getMriId().equals("3")||merchantRequireItem.getMriId().equals("6")){
					String responseMsg = ClientInterface.risk130(coreUrl,idCardNo,settleCardNo,risk130Key);
					if(StringUtil.isNotBlank(responseMsg)){
						Map<String, Object> responseMap = JSON.parseObject(responseMsg).getJSONObject("header");
						Boolean succeed = (Boolean) responseMap.get("succeed");
						if(!succeed){
							String msg = (String) responseMap.get("errMsg");
							throw new RuntimeException(msg);
						}
					}
				}
				i = merchantRequireItemService.updateByMbpId(merchantRequireItem);
				if (i != 1) {
					throw new RuntimeException("商户进件项修改失败");
				}
			}
		}

		if (!merchantBusinessProduct.getStatus().equals("1") && !merchantBusinessProduct.getStatus().equals("2") && !merchantBusinessProduct.getStatus().equals("0")) {
			if (merchantCardInfoService.selectByMertIdAndAccountNo(merchantCardInfo) != null) {
				PosCardBin pdd = posCardBinService.queryInfo(mci.getAccountNo());
				mci.setLogAccountNo(logAccount);
				if (pdd != null) {
					if (pdd.getCardType().equals("借记卡")) {
						mci.setCardType("1");
					} else {
						mci.setCardType("2");
					}
					i = merchantCardInfoService.updateByMerId(mci);
					if (i != 1) {
						throw new RuntimeException("商户账号信息修改失败");
					}
					MerchantRequireHistory mer=new MerchantRequireHistory();
					mer.setMerchantNo(mci.getMerchantNo());
					mer.setHistoryContent(mci.getLogAccountNo());
					mer.setNewContent(mci.getAccountNo());
					mer.setCreator(userId+"");
					int num=merchantRequireHistoryService.insertBankCard(mer);
					if(num<0){
						throw new RuntimeException("商户账号信息修改日志记录失败");
					}
				} else {
					throw new RuntimeException("银行账号匹配不上");
				}
			}
			if (nu == 1) {
				i = merchantInfoService.updateAddressByMerId(merchantInfo);
				if (i != 1) {
					throw new RuntimeException("商户地址信息修改失败");
				}
			}
		}
		//用商户编号查询这个商户的手机号（旧），与现在的手机号做比较
		MerchantInfo misd1 = merchantInfoService.selectByMobilephoneAndTeam(merchantInfo.getMobilephone(),
				merchantInfo.getTeamId());
		MerchantInfo misd = merchantInfoService.selectByMerNo(merchantBusinessProduct.getMerchantNo());
		if (misd1 != null) {
			if (!misd1.getMerchantNo().equals(merchantInfo.getMerchantNo())) {
				throw new RuntimeException("手机号已存在，请重试");
			}
		} else {
			UserInfo uis = userService.selectInfoByTelNo(misd.getMobilephone(), misd.getTeamId());
			if (uis == null) {
				throw new RuntimeException("手机号不存在，请重试");
			} else {
				int ppm = userService.updateInfoByMp(misd.getMobilephone(), merchantInfo.getMobilephone(), misd.getTeamId());
				if (ppm < 1) {
					throw new RuntimeException("手机号修改失败，请重试");
				} else {
					jsonMap.put("mobile", "yes");
				}
			}
		}
		if (merchantInfo.getRemark() == null) {
			merchantInfo.setRemark("");
		}
		if (misd.getRemark() == null) {
			misd.setRemark("");
		}
		if (!misd.getMobilephone().equals(merchantInfo.getMobilephone()) || !misd.getMerchantName().equals(merchantInfo.getMerchantName())
				|| !merchantInfo.getRemark().equals(misd.getRemark()) || !merchantInfo.getLawyer().equals(misd.getLawyer())
				|| !merchantInfo.getStatus().equals(misd.getStatus()) || !merchantInfo.getIdCardNo().equals(misd.getIdCardNo()) ) {
			i = merchantInfoService.updateByMerId(merchantInfo);
			//判断是不是超级盟主的商户
			MerchantAllAgent merchantAllAgent=merchantAllAgentService.queryMerchantAllAgentByMerNo(merchantInfo.getMerchantNo());
			if(merchantAllAgent!=null){
				ChangeLog changeLog=new ChangeLog();
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				changeLog.setOperater(principal.getId().toString());
				changeLog.setOperMethod("updateByItemAndMbpId");
				changeLog.setRemark("修改超级盟主商户");
				changeLog.setChangePre(JSONObject.toJSONString(merchantAllAgent));
				merchantAllAgent.setStatus(merchantInfo.getStatus());
				merchantAllAgent.setMobilePhone(merchantInfo.getMobilephone());
				merchantAllAgentService.updateMerchantAllAgentByMerNo(merchantAllAgent);
				changeLog.setChangeAfter(JSONObject.toJSONString(merchantAllAgent));
				changeLogService.insertChangeLog(changeLog);
				log.info("修改超级盟主商户成功...........");
			}
			if (i != 1) {
				throw new RuntimeException("商户信息修改失败");
			}
		}

		for (MerchantServiceRate merchantServiceRate : merchantServiceRateList) {
			if (merchantServiceRate.getFixedMark().equals("0")) {
				MerchantServiceRate m = new MerchantServiceRate();
				m = merchantServiceRateService.setMerchantServiceRate(merchantServiceRate);
				m.setId(merchantServiceRate.getId());
				m.setServiceId(merchantServiceRate.getServiceId());
				m.setMerchantNo(merchantServiceRate.getMerchantNo());
				m.setHolidaysMark(merchantServiceRate.getHolidaysMark());
				m.setCardType(merchantServiceRate.getCardType());
				m.setRateType(merchantServiceRate.getRateType());
				i = merchantServiceRateService.updateByPrimaryKey(m);
				if (i != 1) {
					throw new RuntimeException("商户费率改失败");
				}
			}
		}

		for (MerchantServiceQuota merchantServiceQuota : merchantServiceQuotaList) {
			if (merchantServiceQuota.getFixedMark().equals("0")) {
				i = merchantServiceQuotaService.updateByPrimaryKey(merchantServiceQuota);
				if (i != 1) {
					throw new RuntimeException("商户限额修改失败");
				}
			}
		}

		// 修改商户手机号时调用积分系统修改手机号
		String vipScoreUrl=sysDictService.getValueByKey("VIP_SCORE_URL");
		String teamId=misd.getTeamId();
		String key=bossSysConfigService.selectValueByKey("VIP_SCORE_SIGN_KEY_"+teamId);
		String businessNo=bossSysConfigService.selectValueByKey("VIP_SCORE_BUS_NO_"+teamId);
		String resultMsg=ClientInterface.updateVipInfo(vipScoreUrl,merchantInfo,misd.getMobilephone(),businessNo,teamId,key);

        jsonMap.put("result", true);
        jsonMap.put("msg", "修改成功");
		return jsonMap;
	}

	/**
	 * 校验身份证号是否为禁用开头
	 * @param idCardNo 身份证号
	 * @return false 表示禁用，true表示可以用
	 */
	private boolean checkIcCardNoTitle(String idCardNo){
		//多个,号间隔
		String checkStr=sysDictDao.getValueByKey("ID_CARD_PREFIX_OF_FORBID_ADD_MERCHANT");
		if(StringUtils.isNotBlank(checkStr)&&idCardNo.length()>=6){
			String str=","+idCardNo.substring(0,6)+",";
			String strs=","+checkStr+",";
			//如果过滤字符包含 身份证前6位，则返回false
			if(strs.indexOf(str)>=0){
				return false;
			}
		}
		return true;
	}

    public Result ysUpdateMer(String merchantNo, String mbpId, String bpId, String acqEnname) {
		Result result = new Result();
		result.setMsg("修改成功");
		ZqMerchantInfo zqMerchantInfo = zqMerchantInfoDao.selectByMerMbpAcq(merchantNo, mbpId, acqEnname);
		if(zqMerchantInfo != null && "1".equals(zqMerchantInfo.getReportStatus())){
            String coreUrl = "";
            SysDict coreUrlDict = sysDictDao.getByKey("CORE_URL");
            if(coreUrlDict != null){
                coreUrl = coreUrlDict.getSysValue() + Constants.YS_UPDATE_MERCHANT_INFO;
            }
            try {
                String returnStr = ClientInterface.ysUpdateMerInfo(coreUrl, merchantNo, mbpId, bpId);
                if(StringUtils.isNotBlank(returnStr)){
                    JSONObject json = JSONObject.parseObject(returnStr);
                    JSONObject headJson = JSONObject.parseObject(json.getString("header"));
                    if(headJson.getBoolean("succeed")){
						result.setStatus(true);
						result.setMsg("操作成功，且同步商户成功");
                    } else {
						result.setMsg("操作成功，且同步商户失败，失败原因:" + headJson.getString("errMsg"));
                    }
                } else {
					result.setMsg("操作成功，同步商户失败");
                }
            } catch (Exception e){
                log.info("商户修改，同步银盛失败", e);
            }
        }
		return result;
	}

	@Override
	public List<Map<String, Object>> examineTotalByParam(SelectParams selectParams) {
		if(selectParams.getProvince()!=null&&!"".equals(selectParams.getProvince())){
			Map<String,Object> map=areaInfoService.getProvincebyId(Integer.valueOf(selectParams.getProvince()));
			selectParams.setProvince(map.get("name").toString());
		}
		return merchantBusinessProductDao.examineTotalByParam(selectParams);
	}

	@Override
	public void exportExamine(SelectParams selectParams, HttpServletResponse response, HttpServletRequest request) {
		log.info("开始执行导出");
		if(selectParams.getProvince()!=null&&!"".equals(selectParams.getProvince())){
			Map<String,Object> map=areaInfoService.getProvincebyId(Integer.valueOf(selectParams.getProvince()));
			selectParams.setProvince(map.get("name").toString());
		}
		List<MerchantBusinessProduct>  list = merchantBusinessProductDao.exportExamine(selectParams);
		log.info("准备执行导出数据条数：{}",list.size());
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,String> syncStatusMap = new HashMap<>();
		syncStatusMap.put("0","初始化");
		syncStatusMap.put("1","同步成功");
		syncStatusMap.put("2","同步失败");
		syncStatusMap.put("3","审核中");
		Map<String,String> statusMap = new HashMap<>();
		statusMap.put("1","待一审");
		statusMap.put("2","待平台审核");
		statusMap.put("3","审核失败");
		statusMap.put("4","正常");
		Map<String,String> riskStatus = new HashMap<>();
		riskStatus.put("1","正常");
		riskStatus.put("2","只进不出");
		riskStatus.put("3","不进不出");
		Map<String,String> recommendedSource = sysDictService.selectMapByKey("RECOMMENDED_SOURCES");
//		recommendedSource.put("0","正常注册");
//		recommendedSource.put("1","微创业");
//		recommendedSource.put("2","代理商分");
//		recommendedSource.put("3","超级盟主");
		Map<String,String> reexamineStatus = new HashMap<>();
		reexamineStatus.put("0","未复审");
		reexamineStatus.put("1","复审通过");
		reexamineStatus.put("2","复审不通过");
		reexamineStatus.put("3","复审退件");
		List<SysDict> sysDictList=sysDictService.selectByKey("MERCHANT_TYPE_LIST");
		String fileName = "复审商户导出" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = null;
		try {
			fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		log.info("开始exl数据组装");
			for (MerchantBusinessProduct mbp: list) {

				//敏感信息屏蔽
				mbp.setMobilePhone(StringUtil.sensitiveInformationHandle(mbp.getMobilePhone(),0));

				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id",mbp.getId().toString());
				maps.put("teamName",mbp.getTeamName());//displayName:'所属组织',width:150
				maps.put("merchantNo",mbp.getMerchantNo());
				maps.put("merchantType",mbp.getMerchantType()==null?"": SysDictUtil.getSysNameByValue(sysDictList,mbp.getMerchantType()));
				maps.put("tradeType","1".equals(mbp.getTradeType())?"直清模式":"集群模式");
				maps.put("merchantName",mbp.getMerchantName());
				maps.put("mobilePhone",mbp.getMobilePhone());
				maps.put("bpName",mbp.getBpName());
				maps.put("agentName",mbp.getAgentName());
				maps.put("activityCode","1".equals(mbp.getActivityCode())? "是":"否");
				maps.put("merAccount","1".equals(mbp.getMerAccount())? "是":"否");
				maps.put("syncStatus", syncStatusMap.get(mbp.getSyncStatus()));
				maps.put("controlAmount", StringUtil.filterNull(mbp.getControlAmount()));
				maps.put("preFrozenAmount",StringUtil.filterNull(mbp.getPreFrozenAmount()));
				maps.put("merCreateTime",sdf.format(mbp.getMerCreateTime()));
				maps.put("reexamineTime",mbp.getReexamineTime()==null?"":sdf.format(mbp.getReexamineTime()));
				maps.put("merStatus","1".equals(mbp.getMerStatus())? "正常":"关闭");
				maps.put("reexamineStatus",reexamineStatus.get(mbp.getReexamineStatus()));
				maps.put("reexamineOperator",mbp.getReexamineOperator());
				maps.put("status",statusMap.get(mbp.getStatus()));
				maps.put("riskStatus",riskStatus.get(mbp.getRiskStatus()));
				maps.put("recommendedSource",recommendedSource.get(mbp.getRecommendedSource()));
				data.add(maps);
			}
		log.info("exl数据组装完成");
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "id", "teamName", "merchantNo","merchantType", "tradeType", "merchantName", "mobilePhone", "bpName", "agentName", "activityCode",
				"merAccount", "syncStatus", "controlAmount", "preFrozenAmount", "merCreateTime", "reexamineTime", "merStatus", "reexamineStatus", "reexamineOperator",
				"status", "riskStatus", "recommendedSource"};
		String[] colsName = new String[] {"商户进件编号", "所属组织", "商户编号","商户类型", "交易模式", "商户名称", "商户手机号", "业务产品", "代理商名称", "是否参加欢乐送",
				"已开户", "直清同步状态", "冻结金额", "预冻结金额", "创建时间", "复审时间", "商户状态", "复审状态", "复审人",
				"业务产品状态", "商户冻结状态", "推广来源"};

		OutputStream outputStream = null;
		log.info("exl数据导出");
		try {
			outputStream = response.getOutputStream();
			export.export(cols, colsName, data, outputStream);
		} catch (IOException e) {
			log.error("导出异常",e);
			e.printStackTrace();
		} finally {
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	public String getTeamName(String teamId) {
		String teamName = "";
		if(StringUtils.isBlank(teamId)){
			return teamName;
		}
		if(!StringUtil.isNumeric(teamId)){
			return teamName;
		}
		Integer teamIdInt = Integer.parseInt(teamId);
		TeamInfo teamInfo = teamInfoDao.selectTeamInfo(teamIdInt);
		if(teamInfo != null){
			teamName = teamInfo.getTeamName();
		} else {
			teamName = yfbOemServiceDao.selectTeamName(teamId);
		}
		return teamName;
	}

	@Override
	public String getCoreUrl(){
		String coreUrl = "";
		SysDict coreUrlDict = sysDictDao.getByKey("CORE_URL");
		if(coreUrlDict != null){
			coreUrl = coreUrlDict.getSysValue();
		}
		return coreUrl;
	}


    @Override
    public int updateMbpExamineInfo(MbpStatusCondition mbpStatus,MerchantBusinessProduct mbpInfo,List<MerchantRequireItem> mri, MerchantInfo merchantInfo, MerchantCardInfo mci,ExaminationsLog el) {
       	int val=mbpStatus.getVal();
        //银行卡状态
        boolean bankCardStatus=mbpStatus.getBankCardStatus();
        //开户行全称，开户地址
        boolean addressStatus=mbpStatus.getBankCardStatus();

		boolean reexamineStatus=mbpStatus.getReexamineStatus();

		boolean appStatus=mbpStatus.getAppStatus();

        int num=0;
        if(val == 1 || val == 3){//审核成功
			el.setOpenStatus("1");
			//更新进件表
			num = updateBymbpId(mbpInfo.getId(), "4", el.getOperator());
			if(num>0){
				//更新子进件项
				updateMerchantRequireItem(val,mri,bankCardStatus,addressStatus);

				//审核过,增加初始统计次数
				merchantBusinessProductDao.updateMbpAuditNum(mbpInfo.getId());

				//商户信息
				merchantInfoService.updateByPrimaryKey(merchantInfo);

				//商户银行卡信息merchant_id
				mci.setDefSettleCard("1");
				if (merchantCardInfoService.selectByMertIdAndAccountNo(mci) == null) {
					 merchantCardInfoService.insert(mci);
				} else {
					merchantCardInfoService.updateById(mci);
				}

				//路由
				routeGroupDao.insertRouteGroupByMerchant(merchantInfo.getMerchantNo());

				//审核日志
				//校验商户是否有过复审退件再次进件
				if("3".equals(mbpInfo.getReexamineStatus())){
					//被复审退件的商户
					el.setExamineType(2);
					int count=examinationsLogService.insert(el);

					//商户曾被复审退件，再次进件，审核成功，其余件全部成功
					List<MerchantBusinessProduct> mbpList= getByMer(merchantInfo.getMerchantNo());
					for (MerchantBusinessProduct mbp:mbpList) {
						Long otherMbpId = mbp.getId();
						if(!mbpInfo.getId().toString().equals(otherMbpId.toString())){
							updateBymbpId(otherMbpId, "4", el.getOperator());
							el.setBpId(mbp.getBpId());
							el.setItemNo(String.valueOf(otherMbpId));
							el.setExamineType(2);
							examinationsLogService.insert(el);
						}
					}
				}else{
					int count = examinationsLogService.insert(el);
					if(count>0){//增加审核记录统计数据
						examinationsLogService.insertLogExt(el);
					}
				}
			}


        }else if(val == 2 || val == 4){//不通过
			el.setOpenStatus("2");
			if(!reexamineStatus){
				//正常审件
				num = updateBymbpId(mbpInfo.getId(), "3", el.getOperator());
				if(num>0){
					//更新子进件项
					updateMerchantRequireItem(val,mri,bankCardStatus,addressStatus);

					//开户名,银行卡,开户身份证,更新APP自动审件次数为3
					if(appStatus){
						updateCheckNum(merchantInfo.getMobilephone(),merchantInfo.getTeamId());
					}

					//审核过,增加初始统计次数
					merchantBusinessProductDao.updateMbpAuditNum(mbpInfo.getId());

					if("3".equals(mbpInfo.getReexamineStatus()) ){
						//被复审退件的商户
						el.setExamineType(2);
						examinationsLogService.insert(el);

						//属风控复审退件，继续退其他进件...
						List<MerchantBusinessProduct> mbpList= getByMer(merchantInfo.getMerchantNo());
						for (MerchantBusinessProduct mbp:mbpList) {
							Long otherMbpId = mbp.getId();
							if(!mbpInfo.getId().toString().equals(otherMbpId.toString())){
								updateBymbpId(otherMbpId, "3", el.getOperator());
								el.setExamineType(2);
								el.setBpId(mbp.getBpId());
								el.setItemNo(String.valueOf(otherMbpId));
								el.setOpenStatus("2");
								examinationsLogService.insert(el);
							}
						}
					}else{
						int count=examinationsLogService.insert(el);
						if(count>0){//增加审核记录统计数据
							examinationsLogService.insertLogExt(el);
						}
					}
				}

			}else{
				//复审退件
				num = updateBymbpId(mbpInfo.getId(), "3", el.getOperator());
				if(num>0){

					//更新子进件项
					updateMerchantRequireItem(val,mri,bankCardStatus,addressStatus);

					//开户名,银行卡,开户身份证,更新APP自动审件次数为3
					if(appStatus){
						updateCheckNum(merchantInfo.getMobilephone(),merchantInfo.getTeamId());
					}

					//审核日志
					el.setOpenStatus("2");
					examinationsLogService.insert(el);

					//复审退件,获取当前商户其余进件，全部退件
					List<MerchantBusinessProduct> mbpList= getByMer(merchantInfo.getMerchantNo());
					final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					for (MerchantBusinessProduct mbp:mbpList) {
						Long otherMbpId = mbp.getId();
						if(!mbpInfo.getId().toString().equals(otherMbpId.toString())){
							updateBymbpId(otherMbpId, "3", el.getOperator());
							reexamineBymbpId(otherMbpId,"3",principal.getUsername());
							el.setBpId(mbp.getBpId());
							el.setItemNo(String.valueOf(otherMbpId));
							el.setExamineType(2);
							el.setOpenStatus("3");
							examinationsLogService.insert(el);
						}
					}
				}
			}
        }
		return num;
    }




	/**
	 * 更新子进件项
	 * @param val
	 * @param mri
	 * @param bankCardStatus 银行卡状态
	 * @param addressStatus 开户行全称，开户地址
	 * @return
	 */
    private void updateMerchantRequireItem(int val,List<MerchantRequireItem> mri,boolean bankCardStatus,boolean addressStatus){
		if(mri!=null&&mri.size()>0){
			for(MerchantRequireItem item:mri){
				if(val == 1 || val == 3){
					merchantRequireItemService.updateBymriId(item.getId(), "1");
				}else if(val == 2 || val == 4){

					if ("不通过".equals(item.getaStatus())) {
						merchantRequireItemService.updateBymriId(item.getId(), "2");
					}else{
						if(bankCardStatus && ("4".equals(item.getMriId())||"5".equals(item.getMriId())||"15".equals(item.getMriId()))) {
							merchantRequireItemService.updateBymriId(item.getId(), "2");
							continue;
						}
						if(addressStatus && ("4".equals(item.getMriId())||"15".equals(item.getMriId()))){
							merchantRequireItemService.updateBymriId(item.getId(), "2");
							continue;
						}
						merchantRequireItemService.updateBymriId(item.getId(), "1");
					}
				}
			}
		}
	}

	@Override
	public List<MerchantBusinessProduct> selectAllInfo(Page<MerchantBusinessProduct> page) {
		return merchantBusinessProductDao.selectAllInfo(page);
	}

	@Override
	public List<String> querySerivceId(String bpId) {
		return businessProductInfoDao.findByProduct(bpId);
	}

	@Override
	public List<MerchantBusinessProduct> selectByStatusParam(Page<MerchantBusinessProduct> page,
															 SelectParams selectParams) {
		List<MerchantBusinessProduct> list=merchantBusinessProductDao.selectByStatusParam(page, selectParams);
		getTeamName(page.getResult());
		return list;
	}

	@Override
	public List<MerchantBusinessProduct> selectAllInfoSale(Page<MerchantBusinessProduct> page, String name) {
		List<String> list = agentInfoService.selectAllNodeSale(name);
		if (list.size() == 0) {
			return null;
		}
		String str = "";
		for (String agentInfo : list) {
			str += " or ais.agent_node LIKE " + '"' + agentInfo + '"';
		}
		return merchantBusinessProductDao.selectAllInfoSale(page, str.substring(3, str.length()));
	}

	@Override
	public List<MerchantBusinessProduct> selectByParamSale(Page<MerchantBusinessProduct> page,
														   SelectParams selectParams, String name) {
		List<String> list = agentInfoService.queryAllOneAgentBySale(name);
		if (list.size() == 0) {
			return null;
		}
		//xy328tgh
//		StringBuilder str=new StringBuilder("(");
//		for (String agentNo : list) {
//			str.append("'").append(agentNo).append("'").append(",");
//		}
//		str.deleteCharAt(str.length()-1);
//		str.append(")");
		selectParams.setSaleName(name);
		return merchantBusinessProductDao.selectByParamSale(page, selectParams);
	}

	@Override
	public List<AutoCheckResult> selectAutoCheckResult(String merchantNo, String bpId) {
		return merchantBusinessProductDao.selectAutoCheckResult(merchantNo, bpId);
	}

	@Override
	public int updateCheckNum(String mobile, String teamId) {
		return merchantBusinessProductDao.updateCheckNum(mobile, teamId);
	}

	@Override
	public MerchantBusinessProduct selectMerBusPro(String merchantNo, String bpId) {
		return merchantBusinessProductDao.selectMerBusPro(merchantNo, bpId);
	}

	@Override
	public Integer selectMerProLimit(String merchantNo) {
		return merchantBusinessProductDao.selectMerProLimit(merchantNo);
	}

	@Override
	public MerchantBusinessProduct findCollectionCodeMbp(String merchantNo) {
		return merchantBusinessProductDao.findCollectionCodeMbp(merchantNo);
	}

	@Override
	public List<BusinessProductDefine> selectSourceBpInfo() {
		return merchantBusinessProductDao.selectSourceBpInfo();
	}

	@Override
	public List<BusinessProductDefine> selectNewBpInfo() {
		return merchantBusinessProductDao.selectNewBpInfo();
	}

	@Override
	public Page<MerchantBusinessProductHistory> selectMerBpHistoryList(Map<String, Object> params,
																	   Page<MerchantBusinessProductHistory> page) {
		merchantBusinessProductDao.selectMerBpHistoryList(params, page);
		return page;
	}

	@Override
	public List<MerchantBusinessProduct> getByMer(String merchantNo) {
		return merchantBusinessProductDao.getByMer(merchantNo);
	}

	@Override
	public int updateTradeTypeById(Long primaryKey, String tradeType) {
		return merchantBusinessProductDao.updateTradeTypeById(primaryKey, tradeType);
	}

	@Override
	public MerchantBusinessProduct selectByServiceId(Long merServiceId) {
		return merchantBusinessProductDao.selectByServiceId(merServiceId);
	}

	@Override
	public int updateTradeTypeByServices(MerchantBusinessProduct merBusPro) {
		return merchantBusinessProductDao.updateTradeTypeByServices(merBusPro);
	}

	@Override
	public String queryExamineStatus(String bpId, String merchantNo) {
		return merchantBusinessProductDao.queryExamineStatus(bpId, merchantNo);
	}

	@Override
	public List<Map<String, Object>> findDefRouteGroupAdd(String merchantNo, String bpId, String channelCode) {
		return merchantBusinessProductDao.findDefRouteGroupAdd(merchantNo, bpId,channelCode);
	}


	@Override
	public String getUserIdByMerchantInfo(String merchantNo) {
		return merchantBusinessProductDao.getUserIdByMerchantInfo(merchantNo);
	}


	@Override
	public MerchantBusinessProduct selectByMerchantNo(String merchantNo) {
		return merchantBusinessProductDao.selectByMerchantNo(merchantNo);
	}


	@Override
	public String getDeviceIdByPhone(String mobilephone) {
		return merchantBusinessProductDao.getDeviceIdByPhone(mobilephone);
	}


	@Override
	public List<MerchantBusinessProduct> selectByMerchantNoAll(String merchantNo) {
		return merchantBusinessProductDao.selectByMerchantNoAll(merchantNo);
	}


	public void initSysConfigByKey() {
		Constants.BEHAVIOUR_SERVER_PROJECT= merchantBusinessProductDao.getSysConfigByKey("SENSORS_PROJECT");
		Constants.BEHAVIOUR_SERVER_URL = merchantBusinessProductDao.getSysConfigByKey("SENSORS_URL");	
		Constants.BEHAVIOUR_SERVER_TOKEN = merchantBusinessProductDao.getSysConfigByKey("SENSORS_TOKEN");
	}

	public void SendHttpSc(Map<String, Object> oneMap) {
		
		String valueByKey = sysDictDao.getValueByKey("SENSORS_STATUS");
		
		if("0".equals(valueByKey)){
			return ;
		}
		
		try {
		//JSON BASE64 URLEncoder
		JSONObject jsonObject = new JSONObject(oneMap);
		String encode = Base64.encode(jsonObject.toString().getBytes());
		String data = URLEncoder.encode(encode,"UTF-8");
		String paramSC = "data=" + data + "&zip=0";
		//String sendPost = HttpUtils.sendPost("https://shenceapi.sqianbao.cn/sa?project=default&token=saf7c5e114", paramSC, "UTF-8");
			HttpUtils.sendPost(Constants.BEHAVIOUR_SERVER_URL+"?project="+Constants.BEHAVIOUR_SERVER_PROJECT+"&token="+ Constants.BEHAVIOUR_SERVER_TOKEN, paramSC, "UTF-8");
			log.info("神策发送数据=========>"+jsonObject);
		} catch (Exception e) {
			log.error("神策出错------", e);
		}
	}

	@Override
	public int getMerCreditCard(String merNo) {
		Map<String, Object> map=merchantBusinessProductDao.getMerCreditCard(merNo);
		if(map!=null){
			return 1;
		}
		return 0;
	}

	@Override
	public List<MerchantBusinessProduct> getByMerAndBpId(String merchantNo) {
		return merchantBusinessProductDao.getByMerAndBpId(merchantNo);
	}


	@Override
	public String getByCodeAndType(String autoMbpChannel, int routeType) {
		return merchantBusinessProductDao.findByCodeAndType(autoMbpChannel,routeType);
	}
	
	@Override
	public List<MerchantBusinessProduct> selectNextMbpInfo(MerchantBusinessProduct info) {
		return merchantBusinessProductDao.selectNextMbpInfo(info);
	}

	@Override
	public MerchantBusinessProduct getMerchantBusinessProductInfo(Long mbpId) {
		return merchantBusinessProductDao.getMerchantBusinessProductInfo(mbpId);
	}

	@Override
	public MerchantBusinessProduct getMbpInfoDetail(Long mbpId) {
		return merchantBusinessProductDao.getMbpInfoDetail(mbpId);
	}


	@Override
	public List<Map> queryHuoTiChannels()throws Exception {
		return merchantBusinessProductDao.queryHuoTiChannels();
	}


	@Override
	public List<Map<String, String>> queryAuthChannels() {
		return sysDictDao.getListByKey("authChannel");
	}

	@Override
	public List<String> selectBpIdsByMerchantNo(String merchantNo) {
		return merchantBusinessProductDao.selectBpIdsByMerchantNo(merchantNo);
	}

	@Override
	public MerchantBusinessProduct selectByMerchantNoAndBpId(String merchantNo, String bpId) {
		return merchantBusinessProductDao.selectByMerchantNoAndBpId(merchantNo, bpId);
	}

	@Override
	public List<Map<String, Object>> selectTerBpInfo(String merchantNo, String bpId) {
		return merchantBusinessProductDao.selectTerBpInfo(merchantNo, bpId);
	}

	@Override
	public int updateMerchantBusinessProduct(String merchantNo, String oldBpId, String newBpId) {
		return merchantBusinessProductDao.updateMerchantBusinessProduct(merchantNo, oldBpId, newBpId);
	}

	@Override
	public int insertMerBusProHis(MerchantBusinessProductHistory hisMbp) {
		return merchantBusinessProductDao.insertMerBusProHis(hisMbp);
	}

	@Override
	public int deleteMerBusItem(String merchantNo) {
		return merchantBusinessProductDao.deleteMerBusItem(merchantNo);
	}

	@Override
	public List<ServiceInfoBean> listServiceInfoByBpId(String bpId) {
		return merchantBusinessProductDao.listServiceInfoByBpId(bpId);
	}

	@Override
	public int deleteMerRate(String bpId, String merchantNo) {
		return merchantBusinessProductDao.deleteMerRate(bpId, merchantNo);
	}

	@Override
	public int deleteMerQuota(String bpId, String merchantNo) {
		return merchantBusinessProductDao.deleteMerQuota(bpId, merchantNo);
	}

	@Override
	public int updateMerchantService(String merchantNo, String oldBpId, String newBpId, String oldServiceId, String newServiceId) {
		return merchantBusinessProductDao.updateMerchantService(merchantNo, oldBpId, newBpId, oldServiceId, newServiceId);
	}

	@Override
	public void bacthInsertServiceRate(List<ServiceRate> newServiceRateList, String merchantNo) {
		merchantBusinessProductDao.bacthInsertServiceRate(newServiceRateList, merchantNo);
	}

	@Override
	public int bacthInsertServiceQuota(List<ServiceQuota> newServiceQuotaList, String merchantNo) {
		return merchantBusinessProductDao.bacthInsertServiceQuota(newServiceQuotaList, merchantNo);
	}

	@Override
	public int countZF_ZQAndSyncSuccess(String merchantNo, String bpId) {
		return merchantBusinessProductDao.countZF_ZQAndSyncSuccess(merchantNo, bpId);
	}
}
