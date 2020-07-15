package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.common.json.JSON;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.internal.org.apache.commons.lang3.time.DateUtils;

import cn.eeepay.framework.dao.ThreeRecordedFlowMapper;
import cn.eeepay.framework.dao.TradeSumInfoMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ThreeRecordedFlow;
import cn.eeepay.framework.model.TradeSumInfo;
import cn.eeepay.framework.model.TradeSumInfoQo;
import cn.eeepay.framework.model.three.AccountReturn;
import cn.eeepay.framework.model.three.TeamInfoEntry;
import cn.eeepay.framework.model.three.ThreeIncomeVo;
import cn.eeepay.framework.model.three.ThreeSumVo;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpUtils;
import cn.hutool.core.lang.UUID;

@Service
public class TradeSumInfoService {

	public static final String UPDATE_AGENT_AUTHORIZED_LINK_COUNT = "UPDATE_AGENT_AUTHORIZED_LINK_COUNT";
	public static final String EXPIRED_NOT_ACTIVATED_DAYS = "EXPIRED_NOT_ACTIVATED_DAYS";

	public static final String ZERO = "0";
	public static final String THREE_INCOME_CALC_SWITCH = "THREE_INCOME_CALC_SWITCH";

	@Autowired
	private TradeSumInfoMapper tradeSumInfoMapper;

	@Autowired
	private ThreeRecordedFlowMapper threeRecordedFlowMapper;

	@Autowired
	private SysDictService sysDictService;

	public void query(Page<TradeSumInfo> page, TradeSumInfoQo qo) {
		setQo(qo);
		tradeSumInfoMapper.page(page, qo);
	}

	public List<Map<String, String>> queryConfigInfo() {
		return tradeSumInfoMapper.findConfigInfo();
	}

	private void setQo(TradeSumInfoQo qo) {
		// 拿到所有的顶级代理商
		List<String> agentList = new ArrayList<>();
		List<String> topAgentNoList = new ArrayList<>();
		if (StringUtils.hasLength(qo.getAgentNo())) {
			agentList.add(qo.getAgentNo());
			getAllLookAgentNo(agentList, qo.getAgentNo());
		} else {
			topAgentNoList = tradeSumInfoMapper.findAllTopAgentNo();
			for (String str : topAgentNoList) {
				agentList.add(str);
				getAllLookAgentNo(agentList, str);
			}
		}

		if (2 == qo.getShowLower()) {
			// 不包含下级
			if (StringUtils.hasLength(qo.getAgentNo())) {
				topAgentNoList.clear();
				topAgentNoList.add(qo.getAgentNo());
			}
			qo.setAgentNoList(topAgentNoList);
		} else {
			qo.setAgentNoList(agentList);
		}
	}

	private void getAllLookAgentNo(List<String> agentList, String agentNo) {
		List<String> findConfigAgentNo = tradeSumInfoMapper.findLookAgentNo(agentNo);
		for (String str : findConfigAgentNo) {
			if (agentList.contains(str)) {
				continue;
			}
			agentList.add(str);
			getAllLookAgentNo(agentList, str);
		}
	}

	public void getAllChildAgentNo(List<String> agentList, String agentNo) {
		List<String> findConfigAgentNo = tradeSumInfoMapper.findConfigAgentNo(agentNo);
		for (String str : findConfigAgentNo) {
			if (agentList.contains(str)) {
				continue;

			}
			agentList.add(str);
			getAllChildAgentNo(agentList, str);
		}
	}

	public ThreeSumVo tradeSum(TradeSumInfoQo qo) {
		setQo(qo);
		// 交易总量
		ThreeSumVo sum = tradeSumInfoMapper.sum(qo);
		if (sum == null) {
			sum = new ThreeSumVo();
			sum.setActiveSum(ZERO);
			sum.setMerSum(ZERO);
			sum.setThreeIncomeSum(ZERO);
			sum.setTradeSum(ZERO);
		}
		ThreeSumVo sumRecorded = tradeSumInfoMapper.sumRecorded(qo);
		if (sumRecorded == null) {
			sum.setRecordedSum(ZERO);
		} else {
			sum.setRecordedSum(sumRecorded.getRecordedSum());
		}
		return sum;
	}

	public List<TradeSumInfo> list(TradeSumInfoQo qo) {
		setQo(qo);
		return tradeSumInfoMapper.list(qo);
	}

	/**
	 * 涉及入账 防止恶意刷 加同步锁
	 * 
	 * @param recordedTime
	 * @throws ParseException
	 * @throws com.alibaba.dubbo.common.json.ParseException
	 */
	public synchronized void recorded(String recordedTime)
			throws ParseException, com.alibaba.dubbo.common.json.ParseException {
		// 统计所有代理商该月未入账的收益金额

		// 根据入账时间拿到月末
		Date parseDate = DateUtils.parseDate(recordedTime, "yyyy-MM");
		Date addMonths = DateUtils.addMonths(parseDate, 1);
		addMonths = DateUtils.addSeconds(addMonths, -1);
		List<ThreeIncomeVo> list = tradeSumInfoMapper.findThreeIncomeVoList(parseDate, addMonths);
		// 调用账户接口
		for (ThreeIncomeVo threeIncomeVo : list) {
			final String secret = "zouruijin";
			final long iat = System.currentTimeMillis() / 1000l; // issued at claim
			final long exp = iat + 60L; // expires claim. In this case the token
										// expires in 60 seconds
			final String jti = UUID.randomUUID().toString();
			final JWTSigner signer = new JWTSigner(secret);
			final HashMap<String, Object> claims = new HashMap<String, Object>();
			claims.put("exp", exp);
			claims.put("iat", iat);
			claims.put("jti", jti);
			String agentNo = threeIncomeVo.getAgentNo();
			String threeIncomeSum = threeIncomeVo.getThreeIncomeSum();
			BigDecimal threeIncomeSumBig = new BigDecimal(threeIncomeSum);
			// 时间加随机数
			Random random = new Random();
			String orderNo = DateUtil.getFormatDate(DateUtil.YEARMONTHFORMATE2, new Date()) + random.nextInt(999999);
			// 小于0 不调账户接口 直接修改状态
			if (threeIncomeSumBig.compareTo(BigDecimal.ZERO) <= 0) {
				Date date = new Date();
				// 成功 修改代理商该月的入账状态为已入账
				tradeSumInfoMapper.updateRecorded(date, agentNo, parseDate, addMonths);
				// 记录流水
				ThreeRecordedFlow flow = new ThreeRecordedFlow();
				flow.setRecordedStatus(1);

				// 拿到所有入账成功记录统计金额
				BigDecimal sum = threeRecordedFlowMapper.sumRecordedAmountByAgentNo(agentNo);

				if (sum == null) {
					sum = BigDecimal.ZERO;
				}

				flow.setRecordedSum(sum.add(new BigDecimal(threeIncomeSum)).setScale(2, BigDecimal.ROUND_HALF_UP));
				flow.setTransOrderNo(orderNo);
				flow.setFromSerialNo("直属下级成本小于机构成本,没入账。请检查关联关系是否正确。");
				flow.setAgentNo(agentNo);
				flow.setActivteAmount(new BigDecimal(threeIncomeSum));
				flow.setCreateTime(date);
				threeRecordedFlowMapper.insertSelective(flow);
				continue;
			}
			// 以下参数都为字符串
			claims.put("agentNo", agentNo);// 代理商商编号
			claims.put("amount", threeIncomeSum);// 分润金额
			claims.put("transTypeCode", "000128");// 交易码固定 三方收益入账

			claims.put("fromSerialNo", orderNo);// 来源系统流水号
			claims.put("transOrderNo", orderNo);// 发生交易订单号
			claims.put("fromSystem", "boss");// 来源系统固定
			claims.put("transDate", DateUtil.getLongCurrentDate());// 记账日期

			final String token = signer.sign(claims);
			String valueByKey = sysDictService.getValueByKey("ACCOUNT_SERVICE_URL");
			String url = valueByKey + Constants.THREE_RECORDED_URL;
			String response = HttpUtils.sendPost(url, "token=" + token, "utf-8");
			AccountReturn accountReturn = JSON.parse(response, AccountReturn.class);
			if (accountReturn.isStatus()) {
				Date date = new Date();
				// 成功 修改代理商该月的入账状态为已入账
				tradeSumInfoMapper.updateRecorded(date, agentNo, parseDate, addMonths);
				// 记录流水
				ThreeRecordedFlow flow = new ThreeRecordedFlow();
				flow.setRecordedStatus(1);

				// 拿到所有入账成功记录统计金额
				BigDecimal sum = threeRecordedFlowMapper.sumRecordedAmountByAgentNo(agentNo);

				if (sum == null) {
					sum = BigDecimal.ZERO;
				}

				flow.setRecordedSum(sum.add(new BigDecimal(threeIncomeSum)).setScale(2, BigDecimal.ROUND_HALF_UP));
				flow.setTransOrderNo(orderNo);
				flow.setFromSerialNo("入账成功");
				flow.setAgentNo(agentNo);
				flow.setActivteAmount(new BigDecimal(threeIncomeSum));
				flow.setCreateTime(date);
				threeRecordedFlowMapper.insertSelective(flow);
			} else {
				System.out.println("response:" + response);
				throw new BossBaseException("调用账户入账异常");
			}
		}
	}

	public List<TeamInfoEntry> listByTeamId(String merTeamId) {
		return tradeSumInfoMapper.findTeamEntryByTeamId(merTeamId);
	}

}
