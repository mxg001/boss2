package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.dao.RepayMerchantDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.OperateLogService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.RepayMerchantService;

/**
 * 超级还款用户管理
 * @author mays
 * @date 2017年10月31日
 */
@Controller
@RequestMapping(value = "/repayMerchant")
public class RepayMerchantAction {

	private Logger log = LoggerFactory.getLogger(RepayMerchantAction.class);

	@Resource
	private RepayMerchantService repayMerchantService;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private OperateLogService operateLogService;

	private static final String OPERATE_CODE_CHANGE_MER_STATUS = "changeMerStatus";
	/**
	 * 修改用户状态
	 * @return
	 */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public Map<String, Object> changeStatus(@RequestBody String params){
		Map<String, Object> map = new HashMap<>();
		UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JSONObject jsonObject = JSONObject.parseObject(params);
		String merchantNo = jsonObject.getString("merchantNo");
		String operateDetail = jsonObject.getString("operateDetail");
		if(!StringUtils.isNotBlank(merchantNo)){
			map.put("status", "false");
			map.put("msg", "商户号必传");
		}
		RepayMerchantInfo merchantInfo = repayMerchantService.selectByMerchantNo(merchantNo);
		String oldStatus = merchantInfo.getStatus();
		String status = oldStatus.equals("0") ? "1" : "0";
		int i = repayMerchantService.updateRepayMerchantStatus(merchantNo, status);
		//记录操作到日志
		OperateLog operateLog = new OperateLog();
		operateLog.setOperator(principal.getUsername());
		operateLog.setOperateCode(OPERATE_CODE_CHANGE_MER_STATUS);
		operateLog.setOperateTable("yfb_repay_merchant_info");
		operateLog.setPreValue(oldStatus);
		operateLog.setAfterValue(status);
		operateLog.setOperateDetail(operateDetail);
		operateLog.setOperateTime(new Date());
		operateLog.setBeOperator(merchantNo);
		operateLog.setOperateFrom("repay");
		int j = operateLogService.saveMerStatusChangeToLog(operateLog);

		if (i > 0){
			map.put("msg","修改成功");
			map.put("status","true");
		}else {
			map.put("msg","修改失败");
			map.put("status","false");
		}
		return map;
	}

	/**
	 * 获取用户状态修改日志（只返回前两条）
	 * @param merchantNo
	 * @return
	 */
	@RequestMapping("/queryMerStatusChangeLog")
	@ResponseBody
	public Map<String, Object> queryMerStatusChangeLog(String merchantNo){
		Map<String, Object> map = new HashMap<>();
		if(!StringUtils.isNotBlank(merchantNo)){
			map.put("status", "false");
			map.put("msg", "商户号必传");
		}
		List<OperateLog> operateLogs = operateLogService.queryByOperateCodeAndBeOperator(OPERATE_CODE_CHANGE_MER_STATUS, merchantNo);
		map.put("operateLogs", operateLogs);
		map.put("status", true);
		return map;
	}

	/**
	 * 获取单个用户的所有状态修改日志
	 * @return
	 */
	@RequestMapping("/queryAllMerStatusLog")
	@ResponseBody
	public Map<String, Object> queryAllMerStatusLog(String merchantNo,
													@RequestParam(defaultValue = "1") int pageNo,
													@RequestParam(defaultValue = "10") int pageSize){
		Map<String, Object> map = new HashMap<>();
		if(!StringUtils.isNotBlank(merchantNo)){
			map.put("status", "false");
			map.put("msg", "商户号必传");
		}
		Page<OperateLog> page = new Page<>(pageNo, pageSize);
		operateLogService.queryAllMerStatusLog(page, OPERATE_CODE_CHANGE_MER_STATUS, merchantNo);
		map.put("status", true);
		map.put("page", page);
		return map;
	}

	/**
	 * 用户查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectRepayMerchantByParam")
	@ResponseBody
	public Map<String, Object> selectRepayMerchantByParam(@RequestBody RepayMerchantInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<RepayMerchantInfo> page = new Page<>(pageNo, pageSize);
			repayMerchantService.selectRepayMerchantByParam(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡还款用户查询失败", e);
		}
		return msg;
	}

	/**
	 * 用户开户
	 * @author mays
	 * @date 2017年10月31日
	 */
	@RequestMapping(value = "/batchOpenAccount")
	@ResponseBody
	@SystemLog(description = "信用卡用户开户", operCode = "repayMerchant.batchOpenAccount")
	public Map<String, Object> batchOpenAccount(@RequestBody List<String> merchantNos) {
		Map<String, Object> msg = new HashMap<>();
		if (merchantNos == null || merchantNos.size() < 1) {
			msg.put("status", false);
			msg.put("msg", "参数非法");
			return msg;
		}
		int successNum = 0;
		int failNum = 0;
		for (String merchantNo : merchantNos) {
			try {
				String returnMsg = ClientInterface.creditMerchantOpenAccount(merchantNo);
				if (returnMsg == null || returnMsg.isEmpty()) {
					failNum++;
					continue;
				}
				Map<String, Object> result = JSON.parseObject(returnMsg);
				if ((Boolean)result.get("status") || "外部账号已经存在".equals(result.get("msg").toString())) {
					//返回 成功 或 已经开户，更新状态
					if (1 == repayMerchantService.updateRepayMerchantAccountStatus(merchantNo)) {
						successNum++;
						continue;
					}
				}
				failNum++;
			} catch (Exception e) {
				log.error("信用卡还款用户开户失败", e);
				failNum++;
			}
		}
		msg.put("status", true);
		msg.put("msg", "批量开户,成功条数：" + successNum + "，失败条数：" + failNum);
		return msg;
	}

	/**
	 * 用户详情查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryMerchantDetailByMerchantNo")
	@ResponseBody
	public Map<String, Object> queryMerchantDetailByMerchantNo(String merchantNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (merchantNo == null || "".equals(merchantNo)) {
				msg.put("status", false);
				msg.put("msg", "商户号为空");
				return msg;
			}
			//获取用户基本资料
			RepayMerchantInfo info = repayMerchantService.queryRepayMerchantByMerchantNo(merchantNo);
			//为了方便页面展示图片,把图片信息封装到一个集合
			ArrayList<Object> imageList = processImage(info);
			//获取用户绑定的贷记卡和借记卡
			List<YfbCardManage> debitCardList = new ArrayList<>();
			List<YfbCardManage> creditCardList = new ArrayList<>();
			queryCardInfo(info, debitCardList, creditCardList,1);
			msg.put("status", true);
			msg.put("info", info);	//基本资料
			msg.put("imageList", imageList);	//基本资料的图片
			msg.put("debitCardList", debitCardList);	//借记卡
			msg.put("creditCardList", creditCardList);	//贷记卡
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("用户详情查询失败", e);
		}
		return msg;
	}

	/**
	 * 获取敏感数据
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getDataProcessing")
	@ResponseBody
	public Map<String, Object> getDataProcessing(String merchantNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (merchantNo == null || "".equals(merchantNo)) {
				msg.put("status", false);
				msg.put("msg", "商户号为空");
				return msg;
			}
			RepayMerchantInfo info = repayMerchantService.getDataProcessing(merchantNo);

			//获取用户绑定的贷记卡和借记卡
			List<YfbCardManage> debitCardList = new ArrayList<>();
			List<YfbCardManage> creditCardList = new ArrayList<>();
			queryCardInfo(info, debitCardList, creditCardList,0);

			msg.put("status", true);
			msg.put("info", info);	//基本资料
			msg.put("debitCardList", debitCardList);	//借记卡
			msg.put("creditCardList", creditCardList);	//贷记卡
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "获取敏感数据失败");
			log.error("获取敏感数据失败", e);
		}
		return msg;
	}
	/**
	 * 查询借记卡和贷记卡信息并分到两个集合里
	 * @param info
	 * @param debitCardList 借记卡
	 * @param creditCardList 贷记卡
	 * @param  sta 0 不屏蔽 1屏蔽（敏感信息）
	 */
	private void queryCardInfo(RepayMerchantInfo info, List<YfbCardManage> debitCardList,
			List<YfbCardManage> creditCardList,int sta) {
		if (StringUtils.isNotBlank(info.getIdCardNo())) {
			List<YfbCardManage> cardList = repayMerchantService.queryCardByMerchantNo(info.getMerchantNo());
			String[] num = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二",
					"十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十"};	//超过二十算我输
			int debitNum = 0;
			int creaditNum = 0;
			for (YfbCardManage card : cardList) {
				//获取照片url
				if (StringUtils.isNotBlank(card.getYhkzmUrl())) {
					Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
					String newUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, card.getYhkzmUrl(), expiresDate);
					card.setYhkzmUrl(newUrl);
				}
				// 获取绑卡记录
				List<YfbBindCardRecord> bindCardRecords = repayMerchantService.queryBindCardRecord(card.getCardNo());
				card.setBindCardRecords(bindCardRecords);

				//屏蔽手机号
				if(1==sta){
					card.setMobileNo(StringUtil.sensitiveInformationHandle(card.getMobileNo(),0));
				}

				//分类,借记卡DEBIT/贷记卡CREDIT
				if (StringUtils.isNotBlank(card.getCardType())) {
					if ("DEBIT".equals(card.getCardType())) {
						//判断是否为结算卡
						if (StringUtils.isNotBlank(info.getCardNo()) && card.getCardNo().equals(info.getCardNo())) {
							card.setIsSettleCard("是");
						} else {
							card.setIsSettleCard("否");
						}
						card.setTitle("卡片" + (debitNum > 20 ? "" : num[++debitNum]));
						debitCardList.add(card);
					} else if ("CREDIT".equals(card.getCardType())) {
						card.setTitle("卡片" + (creaditNum > 20 ? "" : num[++creaditNum]));
						creditCardList.add(card);
					}
				}
			}
		}
	}

	/**
	 * 为了方便页面展示图片,把图片信息封装到一个集合
	 * @param info
	 * @return 图片信息的集合
	 */
	private ArrayList<Object> processImage(RepayMerchantInfo info) {
		ArrayList<Object> imageList = new ArrayList<>();
		if (StringUtils.isNotBlank(info.getSfzzmUrl())) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("itemName", "身份证正面");
			map.put("url", info.getSfzzmUrl());
			imageList.add(map);
		}
		if (StringUtils.isNotBlank(info.getSfzfmUrl())) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("itemName", "身份证反面");
			map.put("url", info.getSfzfmUrl());
			imageList.add(map);
		}
		if (StringUtils.isNotBlank(info.getScsfzUrl())) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("itemName", "手持身份证");
			map.put("url", info.getScsfzUrl());
			imageList.add(map);
		}
		if (StringUtils.isNotBlank(info.getHeadimgurl())) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("itemName", "微信头像");
			map.put("url", info.getHeadimgurl());
			imageList.add(map);
		}
		return imageList;
	}

	/**
	 * 用户金额信息查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryAccountAmount")
	@ResponseBody
	public Map<String, Object> queryAccountAmount(String merchantNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (merchantNo == null || "".equals(merchantNo)) {
				msg.put("status", false);
				msg.put("msg", "商户号为空");
				return msg;
			}
			List<YfbBalance> accountInfo = repayMerchantService.queryBalanceByMerchantNo(merchantNo);
			msg.put("status", true);
			msg.put("accountInfo", accountInfo);
//			String returnMsg = ClientInterface.repayAccountAmountInfo(merchantNo);
//			Map<String, Object> result = JSON.parseObject(returnMsg);
//			if (result != null && "200".equals(result.get("status").toString())) {
//				List<AccountInfo> list = JSON.parseArray(result.get("data").toString(), AccountInfo.class);
//				// 那边写死返回两条，把失败的数据移除
//				for (int i = 0; i < list.size(); i++) {
//					if (!list.get(i).getStatus()) {
//						list.remove(i--);
//					}
//				}
//				msg.put("status", true);
//				msg.put("accountInfo", list);	
//			} else {
//				msg.put("status", false);
//				msg.put("msg", "账户余额查询失败");
//			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "账户余额查询失败");
			log.error("用户详情账户余额查询失败", e);
		}
		return msg;
	}


	/**
	 * 用户通道同步查询
	 * @author rpc
	 * @date 2018年04月20日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/querychannelSyn")
	@ResponseBody
	public Map<String, Object> querychannelSyn(String merchantNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (merchantNo == null || "".equals(merchantNo)) {
				msg.put("status", false);
				msg.put("msg", "商户号为空");
				return msg;
			}
			List<YfbChannelSyn> channelSynInfo = repayMerchantService.queryChannelSynByMerchantNo(merchantNo);
			msg.put("status", true);
			msg.put("channelSynInfo", channelSynInfo);

		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "通道同步查询失败");
			log.error("用户详情通道同步查询失败", e);
		}
		return msg;
	}

	/**
	 * 用户通道同步日志记录查询
	 * @author rpc
	 * @date 2018年04月20日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/querychannelSynLog")
	@ResponseBody
	public Map<String, Object> querychannelSynLog(String merchantNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (merchantNo == null || "".equals(merchantNo)) {
				msg.put("status", false);
				msg.put("msg", "商户号为空");
				return msg;
			}
			List<YfbChannelSynLog> channelSynLogInfo = repayMerchantService.queryChannelSynLogByMerchantNo(merchantNo);
			msg.put("status", true);
			msg.put("channelSynLogInfo", channelSynLogInfo);

		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "通道同步日志查询失败");
			log.error("用户详情通道同步日志查询失败", e);
		}
		return msg;
	}

	/**
	 * 用户通道同步日志记录查询
	 * @author rpc
	 * @date 2018年04月20日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/toChannelSyn")
	@ResponseBody
	public Map<String, Object> toChannelSyn(String merchantNo,String channelCode) {
		Map<String, Object> msg = new HashMap<>();
		boolean status=false;
		String warmMsg="未知错误";
		try {

			if (merchantNo == null || "".equals(merchantNo)) {

				warmMsg="商户号为空";

			}else if (channelCode == null || "".equals(channelCode)) {

				warmMsg="同步通道为空";

			}else{
				SysDict sysDict = sysDictService.getByKey("REPAY_ACCESS_URL");
				String repayAccessUrl = "http://repay.olvip.vip/";
				if(sysDict != null){
					repayAccessUrl = sysDict.getSysValue();
				}
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("merchantNo",merchantNo);
				params.put("channelCode",channelCode);
				params.put("repayAccessUrl",repayAccessUrl);

				String result = ClientInterface.toChannelSyn(params);

				if (result == null) {

					warmMsg= "同步失败";

				}else{
					JSONObject json = JSON.parseObject(result);
					if (json!=null){
						if ("200".equals(json.getString("status"))) {
							status=true;
							warmMsg= "同步成功";
						} else {
							warmMsg= json.getString("msg");
						}
					}else{
						warmMsg= "同步失败";
					}

				}


			}


		} catch (Exception e) {

			warmMsg="同步失败";
			log.error("用户详情通道同步失败", e);
		}
		msg.put("status", status);
		msg.put("msg", warmMsg);
		return msg;
	}

}
