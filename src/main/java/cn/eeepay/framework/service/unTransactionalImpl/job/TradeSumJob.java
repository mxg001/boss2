package cn.eeepay.framework.service.unTransactionalImpl.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.eeepay.framework.dao.TradeSumInfoMapper;
import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.ServiceManageRate;
import cn.eeepay.framework.model.TradeSumInfo;
import cn.eeepay.framework.model.three.AgentNameInfo;
import cn.eeepay.framework.model.three.SumDo;
import cn.eeepay.framework.model.three.TeamInfoEntry;
import cn.eeepay.framework.model.three.TransServiceIdDo;
import cn.eeepay.framework.service.ServiceProService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.impl.TradeSumInfoService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import cn.eeepay.framework.util.DateUtil;

@Component
@Scope("prototype")
public class TradeSumJob extends ScheduleJob {

	private static final Logger log = LoggerFactory.getLogger(TradeSumJob.class);

	public static final String THREE_INCOME_CALC_OEM = "THREE_INCOME_CALC_OEM";
	public static final String THREE_CALC_START_DATE = "THREE_CALC_START_DATE";
	public static final String THREE_CALC_END_DATE = "THREE_CALC_END_DATE";

	public static final String THREE_INCOME_HISTORY_CALC_SWITCH = "THREE_INCOME_HISTORY_CALC_SWITCH";
	public static final String THREE_INCOME_HISTORY_CALC_START_DATE = "THREE_INCOME_HISTORY_CALC_START_DATE";
	public static final String THREE_INCOME_HISTORY_CALC_END_DATE = "THREE_INCOME_HISTORY_CALC_END_DATE";

	public static final String THREE_WITHDRAW_INCOME_IGNORE_TEAM = "THREE_WITHDRAW_INCOME_IGNORE_TEAM";

	@Autowired
	private TradeSumInfoMapper tradeSumInfoMapper;

	@Autowired
	private TradeSumInfoService tradeSumInfoService;

	@Autowired
	private SysDictService sysDictService;

	@Resource
	private ServiceProService serviceProService;

	@Qualifier("taskExecutor")
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Override
	public void runTask(String runNo) {
		log.info("==>三方交易汇总报表跑批开始-定时任务运行编号[{}]", runNo);
		// 三方链条所有代理商编号
		final List<String> agentList = new ArrayList<>();
		List<String> list = tradeSumInfoMapper.findAllTopAgentNo();
		for (String str : list) {
			if (StringUtils.hasLength(str)) {
				agentList.add(str);
				tradeSumInfoService.getAllChildAgentNo(agentList, str);
			}
		}

		// 超过多少天未激活 到期未激活专用
		final String days = sysDictService.getByKey(TradeSumInfoService.EXPIRED_NOT_ACTIVATED_DAYS).getSysValue();

		// 拿到数据字典配置的所属组织
		String teamConfigStr = sysDictService.getValueByKey(THREE_INCOME_CALC_OEM);
		String[] teamArray = teamConfigStr.split("-");
		final List<String> teamList = new ArrayList<>();
		for (int i = 0; i < teamArray.length; i++) {
			teamList.add(teamArray[i]);
		}
		final List<String> teamEntryIdList = new ArrayList<>();

		// 有子组织的所属组织和没有子组织的所属组织分开计算
		for (Iterator<String> iterator = teamList.iterator(); iterator.hasNext();) {
			String teamId = (String) iterator.next();
			// 查询teamId下的子组织
			List<TeamInfoEntry> teamInfoEntryList = tradeSumInfoMapper.findTeamEntryByTeamId(teamId);
			if (teamInfoEntryList.size() > 0) {
				for (TeamInfoEntry teamInfoEntry : teamInfoEntryList) {
					teamEntryIdList.add(teamInfoEntry.getTeamEntryId());
				}
				iterator.remove();
			}
		}
		final boolean hasteamEntryId = teamEntryIdList.size() > 0;

		// 三方收益计算开关
		final String incomeCalcSwitch = sysDictService.getByKey(TradeSumInfoService.THREE_INCOME_CALC_SWITCH)
				.getSysValue();
		final String incomeHistoryCalcSwitch = sysDictService.getByKey(THREE_INCOME_HISTORY_CALC_SWITCH).getSysValue();
		if ("1".equals(incomeHistoryCalcSwitch) && "1".equals(incomeCalcSwitch)) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					String startTimeStr = sysDictService.getValueByKey(THREE_INCOME_HISTORY_CALC_START_DATE)
							+ " 00:00:00";
					String endTimeStr = sysDictService.getValueByKey(THREE_INCOME_HISTORY_CALC_END_DATE) + " 23:59:59";
					log.info("三方历史收益计算开始，开始时间[{}]", startTimeStr);
					updateThreeIncome(startTimeStr, endTimeStr);
					log.info("三方历史收益计算完成，结束时间[{}]", endTimeStr);
				}
			});
		}

		// 统计指定时间区间的三方数据
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {
				String startDateStr = sysDictService.getValueByKey(THREE_CALC_START_DATE);
				String endDateStr = sysDictService.getValueByKey(THREE_CALC_END_DATE);
				Date startDate = DateUtils.addDays(DateUtil.parseDateTime(startDateStr), 1);
				Date endDate = DateUtils.addDays(DateUtil.parseDateTime(endDateStr), 1);
				// 两个时间区间对应的天数
				List<Date> timeList = getIntervalDateList(startDate, endDate, 1);

				log.info("历史报表统计日期[{}]开始", startDateStr);
				// 三方报表
				for (Date date : timeList) {
					Date endTimeDate = DateUtils.addDays(date, -Integer.valueOf(days));
					threeCalcByTeamId(teamList, agentList, date, endTimeDate);
					if (hasteamEntryId) {
						threeCalcByTeamEntryId(teamEntryIdList, agentList, date, endTimeDate);
					}
				}
				// 三方收益
				if ("1".equals(incomeCalcSwitch) && "1".equals(incomeHistoryCalcSwitch)) {
					String startTimeStr = startDateStr + " 00:00:00";
					String endTimeStr = endDateStr + " 23:59:59";
					updateThreeIncome(startTimeStr, endTimeStr);
				}
				log.info("历史报表统计日期[{}]结束", endDateStr);
			}
		});

		// 当天统计昨天的数据
		Date date = new Date();
		String defaultFormatDate = DateUtil.getDefaultFormatDate(DateUtils.addDays(date, -1));
		Date endTimeDate = DateUtils.addDays(date, -Integer.valueOf(days));
		log.info("统计日期[{}]开始", DateUtil.getDefaultFormatDate(DateUtils.addDays(date, -1)));
		this.threeCalcByTeamId(teamList, agentList, date, endTimeDate);
		// 子组织统计
		if (hasteamEntryId) {
			this.threeCalcByTeamEntryId(teamEntryIdList, agentList, date, endTimeDate);
		}
		if ("1".equals(incomeCalcSwitch)) {
			String startTimeStr = defaultFormatDate + " 00:00:00";
			String endTimeStr = defaultFormatDate + " 23:59:59";
			this.updateThreeIncome(startTimeStr, endTimeStr);

		}
		log.info("统计日期[{}]结束", defaultFormatDate);
		log.info("<==三方交易汇总报表跑批结束[{}]", runNo);
	}

	private void updateThreeIncome(String startTimeStr, String endTimeStr) {
		// 拿到报表列表
		List<TradeSumInfo> infoList = tradeSumInfoMapper.findByCreateTime(startTimeStr, endTimeStr);
		int size = infoList.size();

		int num = size / 5;
		for (int i = 0; i < 6; i++) {

			final List<TradeSumInfo> subList = infoList.subList(i * num, (i + 1) * num > size ? size : (i + 1) * num);
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					for (TradeSumInfo tradeSumInfo : subList) {
						String agentNoStr = tradeSumInfo.getAgentNo();
						String teamId = tradeSumInfo.getTeamId();
						Date createTime = tradeSumInfo.getCreateTime();
						String formatDate = DateUtil.getFormatDate("yyyy-MM-dd", createTime);
						String startTime = formatDate + " 00:00:00";
						String endTime = formatDate + " 23:59:59";
						Integer incomeCalc = tradeSumInfo.getIncomeCalc();
						if (incomeCalc == 1) {
							continue;
						}
						log.info("计算 开始===>>代理商[{}],组织ID[{}]", agentNoStr, teamId);

						threeIncomeCalc(agentNoStr, startTime, endTime, teamId, tradeSumInfo);
						tradeSumInfo.setIncomeCalc(1);
						log.info("计算 结束<<===代理商[{}],组织ID[{}]", agentNoStr, teamId);
						// 更新三方收益
						tradeSumInfoMapper.updateThreeIncome(tradeSumInfo);
					}
				}
			});
		}

	}

	private void threeCalcByTeamId(List<String> teamList, List<String> list, Date date, Date endTimeDate) {
		if (teamList == null || teamList.size() == 0) {
			return;
		}

		Date yesterday = DateUtils.addDays(date, -1);
		String formatDate = DateUtil.getFormatDate("yyyy-MM-dd", yesterday);
		String startTime = formatDate + " 00:00:00";
		String endTime = formatDate + " 23:59:59";
		if (list == null) {
			return;
		}
		List<String> sumAgentList = tradeSumInfoMapper.findSumAgentNo(startTime, endTime, list, teamList);
		List<String> agentList = new ArrayList<>();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String agentNo = iterator.next();
			if (!sumAgentList.contains(agentNo)) {
				agentList.add(agentNo);
			}
		}
		if (agentList.size() < 1) {
			return;
		}
		List<SumDo> tradeSumList = tradeSumInfoMapper.tradeSumByList(agentList, startTime, endTime, teamList);
		List<SumDo> merSumList = tradeSumInfoMapper.merSumByList(agentList, startTime, endTime, teamList);
		List<SumDo> activateSumList = tradeSumInfoMapper.activateSumByList(agentList, startTime, endTime, teamList);
		List<SumDo> machinesStockList = tradeSumInfoMapper.machinesStockByList(agentList, startTime, endTime, teamList);
		List<SumDo> unusedMachinesList = tradeSumInfoMapper.unusedMachinesByList(agentList, startTime, endTime,
				teamList);
		List<SumDo> expiredNotActivatedList = tradeSumInfoMapper.expiredNotActivatedByList(agentList, endTimeDate);
		// 1-5级代理商名称信息
		List<AgentNameInfo> agentInfoList = tradeSumInfoMapper.findAgentInfoByNoList(agentList);
		// 顶层代理商名称信息
		List<AgentNameInfo> topAgentInfoList = tradeSumInfoMapper.findTopAgentInfoByNoList(agentList);

		for (String teamId : teamList) {
			for (String agentNoStr : agentList) {
				TradeSumInfo tradeSumInfo = new TradeSumInfo();
				tradeSumInfo.setCreateTime(yesterday);
				tradeSumInfo.setTeamId(teamId);
				for (SumDo sumDo : tradeSumList) {
					if (sumDo.getTeamId().equals(teamId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getTransAmount())) {
							tradeSumInfo.setTradeSum(new BigDecimal(sumDo.getTransAmount()));
						}
					}
				}
				if (tradeSumInfo.getTradeSum() == null) {
					tradeSumInfo.setTradeSum(BigDecimal.ZERO);
				}
				for (SumDo sumDo : merSumList) {
					if (sumDo.getTeamId().equals(teamId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getMerSum())) {
							tradeSumInfo.setMerSum(Integer.valueOf(sumDo.getMerSum()));
						}
					}
				}
				if (tradeSumInfo.getMerSum() == null) {
					tradeSumInfo.setMerSum(0);
				}
				for (SumDo sumDo : activateSumList) {
					if (sumDo.getTeamId().equals(teamId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getActivateSum())) {
							tradeSumInfo.setActivateSum(Integer.valueOf(sumDo.getActivateSum()));
						}
					}
				}
				if (tradeSumInfo.getActivateSum() == null) {
					tradeSumInfo.setActivateSum(0);
				}
				for (SumDo sumDo : machinesStockList) {
					if (sumDo.getTeamId().equals(teamId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getMachinesStock())) {
							tradeSumInfo.setMachinesStock(Integer.valueOf(sumDo.getMachinesStock()));
						}
					}
				}
				if (tradeSumInfo.getMachinesStock() == null) {
					tradeSumInfo.setMachinesStock(0);
				}
				for (SumDo sumDo : unusedMachinesList) {
					if (sumDo.getTeamId().equals(teamId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getUnusedMachines())) {
							tradeSumInfo.setUnusedMachines(Integer.valueOf(sumDo.getUnusedMachines()));
						}
					}
				}
				if (tradeSumInfo.getUnusedMachines() == null) {
					tradeSumInfo.setUnusedMachines(0);
				}
				if ("100070".equals(teamId)) {
					for (SumDo sumDo : expiredNotActivatedList) {
						if (sumDo.getTeamId().equals(teamId) && sumDo.getAgentNo().equals(agentNoStr)) {
							if (StringUtils.hasLength(sumDo.getExpiredNotActivated())) {
								tradeSumInfo.setExpiredNotActivated(Integer.valueOf(sumDo.getExpiredNotActivated()));
							}
						}
					}
				} else {
					tradeSumInfo.setExpiredNotActivated(0);
				}
				if (tradeSumInfo.getExpiredNotActivated() == null) {
					tradeSumInfo.setExpiredNotActivated(0);
				}

				// 查询代理商名称
				for (AgentNameInfo info : agentInfoList) {
					if (info != null && info.getAgentNo().equals(agentNoStr)) {
						String agentName = info.getAgentName();
						Integer linkLevel = info.getLinkLevel();
						String agentInfo = agentName + "(" + agentNoStr + ")";
						switch (linkLevel) {
						case 1:
							tradeSumInfo.setOneLevel(agentInfo);
							break;
						case 2:
							tradeSumInfo.setTwoLevel(agentInfo);
							break;
						case 3:
							tradeSumInfo.setThreeLevel(agentInfo);
							break;
						case 4:
							tradeSumInfo.setFourLevel(agentInfo);
							break;
						default:
							tradeSumInfo.setFiveLevel(agentInfo);
							break;
						}
					}
				}
				if (tradeSumInfo.getOneLevel() == null && tradeSumInfo.getTwoLevel() == null
						&& tradeSumInfo.getThreeLevel() == null && tradeSumInfo.getFourLevel() == null
						&& tradeSumInfo.getFiveLevel() == null) {
					for (AgentNameInfo info : topAgentInfoList) {
						if (info != null && info.getAgentNo().equals(agentNoStr)) {
							String agentName = info.getAgentName();
							String agentInfo = agentName + "(" + agentNoStr + ")";
							tradeSumInfo.setBranch(agentInfo);
						}
					}
				}

				tradeSumInfo.setAgentNo(agentNoStr);
				tradeSumInfo.setUpdateTime(new Date());
				tradeSumInfo.setRecordedStatus(0);
				tradeSumInfo.setThreeIncome(BigDecimal.ZERO);
				tradeSumInfo.setIncomeCalc(0);
				// 保存
				tradeSumInfoMapper.insertSelective(tradeSumInfo);
			}
		}
	}

	private void threeCalcByTeamEntryId(List<String> teamEntryIdList, List<String> list, Date date, Date endTimeDate) {
		Date yesterday = DateUtils.addDays(date, -1);
		String formatDate = DateUtil.getFormatDate("yyyy-MM-dd", yesterday);
		String startTime = formatDate + " 00:00:00";
		String endTime = formatDate + " 23:59:59";
		if (list == null) {
			return;
		}
		List<String> sumAgentList = tradeSumInfoMapper.findSumAgentNo(startTime, endTime, list, teamEntryIdList);
		List<String> agentList = new ArrayList<>();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String agentNo = iterator.next();
			if (!sumAgentList.contains(agentNo)) {
				agentList.add(agentNo);
			}
		}
		if (agentList.size() < 1) {
			return;
		}
		List<SumDo> tradeSumList = tradeSumInfoMapper.tradeSumByListTeamEntry(agentList, startTime, endTime,
				teamEntryIdList);
		List<SumDo> merSumList = tradeSumInfoMapper.merSumByListTeamEntry(agentList, startTime, endTime,
				teamEntryIdList);
		List<SumDo> activateSumList = tradeSumInfoMapper.activateSumByListTeamEntry(agentList, startTime, endTime,
				teamEntryIdList);
		List<SumDo> machinesStockList = tradeSumInfoMapper.machinesStockByListTeamEntry(agentList, startTime, endTime,
				teamEntryIdList);
		List<SumDo> unusedMachinesList = tradeSumInfoMapper.unusedMachinesByListTeamEntry(agentList, startTime, endTime,
				teamEntryIdList);
		List<SumDo> expiredNotActivatedList = tradeSumInfoMapper.expiredNotActivatedByListTeamEntry(agentList,
				endTimeDate);
		// 1-5级代理商名称信息
		List<AgentNameInfo> agentInfoList = tradeSumInfoMapper.findAgentInfoByNoList(agentList);
		// 顶层代理商名称信息
		List<AgentNameInfo> topAgentInfoList = tradeSumInfoMapper.findTopAgentInfoByNoList(agentList);

		for (String teamEntryId : teamEntryIdList) {
			for (String agentNoStr : agentList) {
				TradeSumInfo tradeSumInfo = new TradeSumInfo();
				tradeSumInfo.setCreateTime(yesterday);
				tradeSumInfo.setTeamId(teamEntryId);
				for (SumDo sumDo : tradeSumList) {
					if (sumDo.getTeamId().equals(teamEntryId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getTransAmount())) {
							tradeSumInfo.setTradeSum(new BigDecimal(sumDo.getTransAmount()));
						}
					}
				}
				if (tradeSumInfo.getTradeSum() == null) {
					tradeSumInfo.setTradeSum(BigDecimal.ZERO);
				}
				for (SumDo sumDo : merSumList) {
					if (sumDo.getTeamId().equals(teamEntryId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getMerSum())) {
							tradeSumInfo.setMerSum(Integer.valueOf(sumDo.getMerSum()));
						}
					}
				}
				if (tradeSumInfo.getMerSum() == null) {
					tradeSumInfo.setMerSum(0);
				}
				for (SumDo sumDo : activateSumList) {
					if (sumDo.getTeamId().equals(teamEntryId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getActivateSum())) {
							tradeSumInfo.setActivateSum(Integer.valueOf(sumDo.getActivateSum()));
						}
					}
				}
				if (tradeSumInfo.getActivateSum() == null) {
					tradeSumInfo.setActivateSum(0);
				}
				for (SumDo sumDo : machinesStockList) {
					if (sumDo.getTeamId().equals(teamEntryId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getMachinesStock())) {
							tradeSumInfo.setMachinesStock(Integer.valueOf(sumDo.getMachinesStock()));
						}
					}
				}
				if (tradeSumInfo.getMachinesStock() == null) {
					tradeSumInfo.setMachinesStock(0);
				}
				for (SumDo sumDo : unusedMachinesList) {
					if (sumDo.getTeamId().equals(teamEntryId) && sumDo.getAgentNo().equals(agentNoStr)) {
						if (StringUtils.hasLength(sumDo.getUnusedMachines())) {
							tradeSumInfo.setUnusedMachines(Integer.valueOf(sumDo.getUnusedMachines()));
						}
					}
				}
				if (tradeSumInfo.getUnusedMachines() == null) {
					tradeSumInfo.setUnusedMachines(0);
				}
				if (teamEntryId.contains("100070")) {
					for (SumDo sumDo : expiredNotActivatedList) {
						if (sumDo.getTeamId().equals(teamEntryId) && sumDo.getAgentNo().equals(agentNoStr)) {
							if (StringUtils.hasLength(sumDo.getExpiredNotActivated())) {
								tradeSumInfo.setExpiredNotActivated(Integer.valueOf(sumDo.getExpiredNotActivated()));
							}
						}
					}
				} else {
					tradeSumInfo.setExpiredNotActivated(0);
				}
				if (tradeSumInfo.getExpiredNotActivated() == null) {
					tradeSumInfo.setExpiredNotActivated(0);
				}

				// 查询代理商名称
				for (AgentNameInfo info : agentInfoList) {
					if (info != null && info.getAgentNo().equals(agentNoStr)) {
						String agentName = info.getAgentName();
						Integer linkLevel = info.getLinkLevel();
						String agentInfo = agentName + "(" + agentNoStr + ")";
						switch (linkLevel) {
						case 1:
							tradeSumInfo.setOneLevel(agentInfo);
							break;
						case 2:
							tradeSumInfo.setTwoLevel(agentInfo);
							break;
						case 3:
							tradeSumInfo.setThreeLevel(agentInfo);
							break;
						case 4:
							tradeSumInfo.setFourLevel(agentInfo);
							break;
						default:
							tradeSumInfo.setFiveLevel(agentInfo);
							break;
						}
					}
				}
				if (tradeSumInfo.getOneLevel() == null && tradeSumInfo.getTwoLevel() == null
						&& tradeSumInfo.getThreeLevel() == null && tradeSumInfo.getFourLevel() == null
						&& tradeSumInfo.getFiveLevel() == null) {
					for (AgentNameInfo info : topAgentInfoList) {
						if (info != null && info.getAgentNo().equals(agentNoStr)) {
							String agentName = info.getAgentName();
							String agentInfo = agentName + "(" + agentNoStr + ")";
							tradeSumInfo.setBranch(agentInfo);
						}
					}
				}

				tradeSumInfo.setAgentNo(agentNoStr);
				tradeSumInfo.setUpdateTime(new Date());
				tradeSumInfo.setRecordedStatus(0);
				tradeSumInfo.setThreeIncome(BigDecimal.ZERO);
				tradeSumInfo.setIncomeCalc(0);
				// 保存
				tradeSumInfoMapper.insertSelective(tradeSumInfo);
			}
		}
	}

	/*
	 * 三方收益计算：
	 */
	private void threeIncomeCalc(String agentNoStr, String startTime, String endTime, String teamId,
			TradeSumInfo tradeSumInfo) {
		BigDecimal threeIncome = BigDecimal.ZERO;
		String agentNode = tradeSumInfoMapper.findAuthAgentNode(agentNoStr);
		// 交易
		List<TransServiceIdDo> listDo = tradeSumInfoMapper.findAllTransServiceIdByTeamId(teamId, agentNoStr, agentNode,
				startTime, endTime);

		log.info("代理商[{}],组织ID[{}],交易查询结束", agentNoStr, teamId);
		String ignoreTeam = sysDictService.getValueByKey(THREE_WITHDRAW_INCOME_IGNORE_TEAM);
		// 1计算机构直属下级的收益
		for (TransServiceIdDo transServiceIdDo : listDo) {
			if (transServiceIdDo == null) {
				continue;
			}
			BigDecimal transAmountSum = transServiceIdDo.getTransAmountSum();
			if (transAmountSum == null) {
				transAmountSum = BigDecimal.ZERO;
			}
			String serviceId = transServiceIdDo.getServiceId();
			String cardType = transServiceIdDo.getCardType();
			String agentNode2 = transServiceIdDo.getAgentNode();

			// 拿到机构分润比例 机构成本
			AgentShareRule rule = tradeSumInfoMapper.findRuleByAgentNoAndServiceId(agentNoStr, serviceId, cardType);
			if (rule == null) {
				log.info("代理商[{}],服务id[{}],卡类型[{}]的分润规则为空", agentNoStr, serviceId, cardType);
				continue;
			} else {
				log.info("收益计算代理商[{}]分润规则-->服务id:[{}],代理商:[{}],卡类型:[{}],交易金额:[{}]", agentNoStr, serviceId, agentNoStr,
						cardType, transAmountSum);
			}

			// 拿到下级成本 分润比例 下级商户费率
			AgentShareRule chlidRule = null;
			agentNode2 = agentNode2.substring(agentNode2.indexOf(agentNoStr), agentNode2.length());
			agentNode2 = agentNode2.replace(agentNoStr, "");
			String[] agentNoList = agentNode2.split("-");
			for (String noStr : agentNoList) {
				if (StringUtils.hasLength(noStr)) {
					if (chlidRule == null) {
						chlidRule = tradeSumInfoMapper.findRuleByAgentNoAndServiceId(noStr, serviceId, cardType);
						if (chlidRule != null) {
							log.info("分润规则-->服务id:[{}],代理商:[{}],卡类型:[{}],交易金额:[{}]", serviceId, noStr, cardType,
									transAmountSum);
						}
					} else {
						break;
					}
				}
			}
			if (chlidRule == null) {
				continue;
			}
			ServiceManageRate rate = null;
			for (String noStr : agentNoList) {
				if (StringUtils.hasLength(noStr)) {
					if (rate == null) {
						rate = tradeSumInfoMapper.findManageRateByAgentNoAndServiceId(noStr, serviceId, cardType);
						if (rate != null) {
							log.info("管控费率-->服务id:[{}],代理商:[{}],卡类型:[{}],交易金额:[{}]", serviceId, noStr, cardType,
									transAmountSum);
						}
					} else {
						break;
					}
				}
			}
			if (rate == null) {
				continue;
			}

			// 上级机构封顶手续费＞下级的 不计算
			if ("6".equals(rate.getRateType())) {
				BigDecimal capping = rule.getCostCapping();
				BigDecimal childCostCapping = chlidRule.getCostCapping();
				if (capping.compareTo(childCostCapping) == 1) {
					continue;
				}

				// {（直属下级商户封顶-机构封顶成本）* 机构分润比例-（直属下级商户封顶-直属下级封顶成本）*
				// 直属下级分润比例）}*直属下级及整个三方链条下级交易封顶笔数
				BigDecimal multiply = rate.getCapping().subtract(rule.getCostCapping())
						.multiply(rule.getShareProfitPercent().divide(new BigDecimal("100")));
				BigDecimal multiply2 = rate.getCapping().subtract(chlidRule.getCostCapping())
						.multiply(chlidRule.getShareProfitPercent().divide(new BigDecimal("100")));
				BigDecimal tradeIncome = multiply.subtract(multiply2)
						.multiply(new BigDecimal(transServiceIdDo.getNum()));
				threeIncome = threeIncome.add(tradeIncome);
			} else {
				// 商户费率 默认扣率 {（直属下级商户费率-机构成本费率）* 机构分润比例-（直属下级商户费率-直属下级成本费率）* 直属下级分润比例 } *
				// 直属下级交易量
				BigDecimal multiply = rate.getRate().subtract(rule.getCostRate())
						.multiply(rule.getShareProfitPercent().divide(new BigDecimal("10000")));
				BigDecimal multiply2 = rate.getRate().subtract(chlidRule.getCostRate())
						.multiply(chlidRule.getShareProfitPercent().divide(new BigDecimal("10000")));
				BigDecimal tradeIncome = multiply.subtract(multiply2).multiply(transAmountSum);
				threeIncome = threeIncome.add(tradeIncome);
			}
			log.info("代理商[{}]交易收益-->[{}]", agentNoStr, threeIncome);
		}
		log.info("代理商[{}],组织ID[{}],交易收益计算结束", agentNoStr, teamId);

		// 提现 分秒到 和 手工提现
		if (ignoreTeam == null || !ignoreTeam.contains(teamId)) {
			List<TransServiceIdDo> listSettleDo = tradeSumInfoMapper.findAllSettleServiceIdByTeamId(teamId, agentNoStr,
					agentNode, startTime, endTime);
			log.info("代理商[{}],组织ID[{}],提现查询结束", agentNoStr, teamId);
			for (TransServiceIdDo transServiceIdDo : listSettleDo) {
				if (transServiceIdDo == null) {
					continue;
				}
				BigDecimal transAmountSum = transServiceIdDo.getTransAmountSum();
				Long num = transServiceIdDo.getNum();
				if (transAmountSum == null) {
					transAmountSum = BigDecimal.ZERO;
				}
				String serviceId = transServiceIdDo.getServiceId();
				String cardType = transServiceIdDo.getCardType();
				// 拿到机构分润比例 机构成本
				AgentShareRule rule = tradeSumInfoMapper.findRuleByLinkService(agentNoStr, serviceId, cardType);
				if (rule == null) {
					log.info("代理商[{}],服务[{}],卡类型[{}]的分润规则为空", agentNoStr, serviceId, cardType);
					continue;
				} else {
					log.info("收益计算代理商[{}]分润规则-->服务id:[{}],代理商:[{}],卡类型:[{}],提现笔数:[{}]", agentNoStr, serviceId,
							agentNoStr, cardType, num);
				}
				String agentNode2 = transServiceIdDo.getAgentNode();

				// 拿到下级成本 分润比例 下级商户费率
				AgentShareRule chlidRule = null;
				agentNode2 = agentNode2.substring(agentNode2.indexOf(agentNoStr), agentNode2.length());
				agentNode2 = agentNode2.replace(agentNoStr, "");
				String[] agentNoList = agentNode2.split("-");
				for (String noStr : agentNoList) {
					if (StringUtils.hasLength(noStr)) {
						if (chlidRule == null) {
							chlidRule = tradeSumInfoMapper.findRuleByLinkService(noStr, serviceId, cardType);
							if (chlidRule != null) {
								log.info("分润规则-->服务id:[{}],代理商:[{}],卡类型:[{}],提现笔数:[{}]", serviceId, noStr, cardType,
										num);
							}
						} else {
							break;
						}
					}
				}
				if (chlidRule == null) {
					continue;
				}

				ServiceManageRate rate = null;
				for (String noStr : agentNoList) {
					if (StringUtils.hasLength(noStr)) {
						if (rate == null) {
							rate = tradeSumInfoMapper.findManageRateByLinkService(noStr, serviceId, cardType);
							if (rate != null) {
								log.info("管控费率-->服务id:[{}],代理商:[{}],卡类型:[{}],提现笔数:[{}]", serviceId, noStr, cardType,
										num);
							}
						} else {
							break;
						}
					}
				}
				if (rate == null) {
					continue;
				}

				// 上级机构封顶手续费＞下级的 不计算
				if ("6".equals(rate.getRateType())) {
					BigDecimal capping = rule.getCostCapping();
					BigDecimal childCostCapping = chlidRule.getCostCapping();
					if (capping.compareTo(childCostCapping) == 1) {
						continue;
					}

					// {（直属下级商户封顶-机构封顶成本）* 机构分润比例-（直属下级商户封顶-直属下级封顶成本）*
					// 直属下级分润比例）}*直属下级及整个三方链条下级交易封顶笔数
					BigDecimal multiply = rate.getCapping().subtract(rule.getCostCapping())
							.multiply(rule.getShareProfitPercent().divide(new BigDecimal("100")));
					BigDecimal multiply2 = rate.getCapping().subtract(chlidRule.getCostCapping())
							.multiply(chlidRule.getShareProfitPercent().divide(new BigDecimal("100")));
					BigDecimal tradeIncome = multiply.subtract(multiply2)
							.multiply(new BigDecimal(transServiceIdDo.getNum()));
					threeIncome = threeIncome.add(tradeIncome);
				} else {
					// 如是固定金额提现手续费的计算如下：
					// {（直属下级商户提现手续费-机构代理商提现费成本）-（直属下级商户提现手续费-下级代理提现费成本）}*直属下级商户提现笔数
					if ("1".equals(rule.getCostRateType())) {
						BigDecimal subtract = rate.getSingleNumAmount().subtract(rule.getPerFixCost());
						BigDecimal subtract2 = rate.getSingleNumAmount().subtract(chlidRule.getPerFixCost());
						BigDecimal tradeIncome = subtract.subtract(subtract2).multiply(new BigDecimal(num));
						threeIncome = threeIncome.add(tradeIncome);
					} else {
						// 商户费率 默认扣率 {（直属下级商户费率-机构成本费率）* 机构分润比例-（直属下级商户费率-直属下级成本费率）* 直属下级分润比例 } *
						// 直属下级提现金额
						BigDecimal multiply = rate.getRate().subtract(rule.getCostRate())
								.multiply(rule.getShareProfitPercent().divide(new BigDecimal("10000")));
						BigDecimal multiply2 = rate.getRate().subtract(chlidRule.getCostRate())
								.multiply(chlidRule.getShareProfitPercent().divide(new BigDecimal("10000")));
						BigDecimal tradeIncome = multiply.subtract(multiply2).multiply(transAmountSum);
						threeIncome = threeIncome.add(tradeIncome);
					}
				}

				log.info("代理商[{}]非手工提现收益+交易收益-->[{}]", agentNoStr, threeIncome);
			}
		}

		log.info("代理商[{}],组织ID[{}],提现收益计算结束", agentNoStr, teamId);
		tradeSumInfo.setThreeIncome(threeIncome.setScale(2, BigDecimal.ROUND_HALF_UP));
	}

	private List<Date> getIntervalDateList(Date startDate, Date endDate, int dateNum) {
		List<Date> list = new ArrayList<>();
		while (startDate.getTime() <= endDate.getTime()) {
			list.add(startDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			// 开始时间加1
			calendar.add(Calendar.DAY_OF_YEAR, dateNum);
			if (calendar.getTime().getTime() > endDate.getTime()) {
				if (!startDate.equals(endDate)) {
					list.add(startDate);
				}
				startDate = calendar.getTime();
			} else {
				startDate = calendar.getTime();
			}
		}
		return list;
	}

}
