package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.historyquery.TransInfoHistoryService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

@Controller
@RequestMapping(value = "/transInfoHistoryAction")
public class TransInfoHistoryAction {
	private static final Logger log = LoggerFactory.getLogger(TransInfoHistoryAction.class);

	@Resource
	private SysDictService sysDictService;

	@Resource
	private TransInfoHistoryService transInfoHistoryService;

	@Resource
	public AgentInfoService agentInfoService;

	@Resource
	public PosCardBinService posCardBinService;

	@Resource
	public TransInfoFreezeNewLogService transInfoFreezeNewLogService;

	@Resource
	private MerchantRequireItemService merchantRequireItemService;

	@Resource
	private RiskRollService riskRollService;

	@Resource
	private UserCouponService userCouponService;

	/**
	 * 数据初始化和分页查询
	 * 
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping("/getAllInfo")
	@ResponseBody
	public Object getAllInfo(@ModelAttribute("page") Page<CollectiveTransOrder> page,
			@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CollectiveTransOrder tis = JSON.parseObject(param, CollectiveTransOrder.class);
			if (tis.getBool() == null || tis.getBool().equals("")) {
				tis.setBool("1");
			}
			transInfoHistoryService.queryAllInfo(tis, page);
			for (int i = 0; i < page.getResult().size(); i++) {
				CollectiveTransOrder cto = page.getResult().get(i);
				String temp = cto.getAmount();
				if (StringUtils.isNotBlank(temp)) {
					String[] temps = temp.split(",");
					cto.setAmount(temps[0]);
					cto.setFeeAmount(temps[1]);
					cto.setOutAmount(temps[2]);
					cto.setOutActualFee(temps[3]);
				}
				cto.setActualFee(cto.getMerchantFee().toString());//实际交易手续费取向上取整的商户手续费，原表字段不用了
				if(cto.getDeductionFee()!=null){
					cto.setMerchantFee(cto.getMerchantFee().add(new BigDecimal(cto.getDeductionFee()))
							.subtract(cto.getMerchantPrice())
							.add(cto.getDeductionMerFee())
							.subtract(cto.getnPrm()==null?BigDecimal.ZERO:cto.getnPrm()));
				}

				/**
				 * T0, 当天(去掉) 交易成功 交易记账:记账成功 冻结状态:正常 结算状态:结算失败\未结算 没有T1结算过
				 * 陈达娟提供可结算的条件
				 */
				if (cto.getTransTime() != null && "0".equals(cto.getSettlementMethod())
						&& "SUCCESS".equals(cto.getTransStatus()) && "1".equals(cto.getAccount())
						&& "0".equals(cto.getFreezeStatus()) && cto.getSettleType() == null
						&& cto.getSettleOrder() == null
						&& ("0".equals(cto.getSettleStatus()) || "3".equals(cto.getSettleStatus()))) {
					page.getResult().get(i).setIsSettleMethod("1");
				} else {
					page.getResult().get(i).setIsSettleMethod("0");
				}
				cto.setAccountNo(StringUtil.sensitiveInformationHandle(cto.getAccountNo(),4));
			}
			map.put("page", page);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}

	/**
	 * 查询总笔数和总金额
	 * 
	 * @return
	 * @throws Exception
	 */
	
	@ResponseBody
	@RequestMapping("/getTotalNumAndTotalMoney")
	public Object getTotalNumAndTotalMoney(@RequestParam("info") String param) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CollectiveTransOrder cto = JSON.parseObject(param, CollectiveTransOrder.class);
			if (cto.getBool() == null || cto.getBool().equals("")) {
				cto.setBool("1");
			}
			CollectiveTransOrder totalInfo = transInfoHistoryService.queryNumAndMoney(cto);
			if(totalInfo!=null && totalInfo.getTotalDeductionFee() != null){
				if(totalInfo.getTotalMerchantPrice()==null){
					totalInfo.setTotalMerchantPrice(BigDecimal.ZERO);
				}
				if(totalInfo.getTotalDeductionMerFee()==null){
					totalInfo.setTotalDeductionMerFee(BigDecimal.ZERO);
				}
				if(totalInfo.getTotalNPrm()==null){
					totalInfo.setTotalNPrm(BigDecimal.ZERO);
				}
				totalInfo.setTotalMerchantFee(totalInfo.getTotalMerchantFee()
						.add(totalInfo.getTotalDeductionFee())
						.subtract(totalInfo.getTotalMerchantPrice())
						.add(totalInfo.getTotalDeductionMerFee())
						.subtract(totalInfo.getTotalNPrm()));
			}
			map.put("totalInfo", totalInfo);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}

	/**
	 * 详情查询
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping("/queryInfoDetail")
	@ResponseBody
	public Object queryInfoDetail(String ids)throws Exception{
		return getDetail(ids,0);
	}

	@RequestMapping("/getDetailShow")
	@ResponseBody
	public Object getDetailShow(String ids)throws Exception{
		return getDetail(ids,1);
	}

	private Map<String,Object> getDetail(String ids,int editState){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("msg", "查询失败");
		map.put("bols", false);
		try {
			CollectiveTransOrder tt = transInfoHistoryService.queryInfoDetail(ids);
			List<SettleTransfer> slist = transInfoHistoryService.selectSettleInfo(tt.getId().toString(), tt.getOrderNo());
			map.put("slist", slist);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
			if(tt!=null){
				if(tt.getTransTime()!=null){
					String orderId = tt.getTisId() + "";
					orderId = StringUtils.leftPad(orderId, 8, "0");
					String d = sdf.format(tt.getTransTime());
					String content = d + orderId + ".png";
					String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_SIGN_TUCKET, content, expiresDate);
					tt.setSignImg(newContent);
				}
				List<TransInfoFreezeNewLog> list = transInfoFreezeNewLogService.queryByOrderNo(tt.getOrderNo());
				map.put("list", list);
				AgentInfo aa = agentInfoService.selectByagentNo(tt.getOneAgentNo());
				PosCardBin pcb = posCardBinService.queryInfo(tt.getAccountNo());
				map.put("pcb", pcb);
				if (aa != null) {
					tt.setAgentName(aa.getAgentName());
				}
				MerchantRequireItem mri = merchantRequireItemService.selectByAccountNo(tt.getMerchantNo());
				if (mri == null) {
					map.put("msg", "没有查询到结算账号");
					map.put("bols", false);
					return map;
				}
				PosCardBin pcb1=posCardBinService.queryInfo(mri.getContent());
				if(pcb1==null){
					pcb1 = new PosCardBin();
				}
				tt.setActualFee(tt.getMerchantFee()==null?"0":tt.getMerchantFee().toString());//实际交易手续费取向上取整的商户手续费，原表字段不用了
				if(tt.getDeductionFee()!=null){
					tt.setMerchantFee(tt.getMerchantFee().add(new BigDecimal(tt.getDeductionFee()))
							.subtract(tt.getMerchantPrice()).add(tt.getDeductionMerFee())
							.subtract(tt.getnPrm()==null?BigDecimal.ZERO:tt.getnPrm()));
				}
				String orderNo = tt.getOrderNo();

				List<Map<String,Object>> coupList = userCouponService.couponList(orderNo,"2");
				String coupNo = "";
				for (Map<String,Object> temp : coupList){
					coupNo += StringUtil.filterNull(temp.get("cno"));
					coupNo += ",";
				}
				if(coupNo.endsWith(",")) {
					coupNo = coupNo.substring(0,coupNo.length() -1);
				}

				//20180202,mays,小票优化,银盛的生成小票加字段'终端号'
				String ysTerminalNo = "";
				if ("YS_ZQ".equals(tt.getAcqEnname())) {
					ysTerminalNo = transInfoHistoryService.queryAcqTerminalNo(tt.getAcqMerchantNo());
				}
				pcb1.setBankNo(mri.getContent());
				//脱敏处理
				if(0==editState){
					tt.setMobilephone(StringUtil.sensitiveInformationHandle(tt.getMobilephone(),0));
					tt.setAccountNo(StringUtil.sensitiveInformationHandle(tt.getAccountNo(),4));
					pcb1.setBankNo(StringUtil.sensitiveInformationHandle(pcb1.getBankNo(),2));
				}
				map.put("pcb1", pcb1);
				map.put("tt", tt);
				map.put("bols", true);
				map.put("coupNo", coupNo);
				map.put("ysTerminalNo", ysTerminalNo);
			}
		} catch (Exception e) {
			log.error("查询详情报错",e);
			map.put("bols", false);
			map.put("msg", "查询详情报错");
		}
		return map;
	}

	/**
	 * 冻结
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/tradeFrozenInfo")
	@ResponseBody
	@SystemLog(description = "交易冻结", operCode = "trade.frozen")
	public Object tradeFrozenInfo(@RequestBody String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			TransInfoFreezeNewLog tfnl = new TransInfoFreezeNewLog();
			String operId = principal.getId().toString();
			String realName = principal.getRealName();
			tfnl.setOperId(operId);
			tfnl.setOperName(realName);
			CollectiveTransOrder orderNo = json.getObject("info", CollectiveTransOrder.class);
			String fd = "1";
			tfnl.setOperReason(json.getString("result"));
			tfnl.setOperType("0");
			tfnl.setOrderNo(orderNo.getOrderNo());
			tfnl.setOperTime(new Date());
			BigDecimal originalPreFrozenMoney = BigDecimal.ZERO;// 构建参数而已
			map = transInfoFreezeNewLogService.insertInfo(tfnl, orderNo, fd, "000017", operId, realName,
					originalPreFrozenMoney);
		} catch (Exception e) {
			log.error("冻结报错", e);
			map.put("bols", false);
			map.put("msg", "冻结报错");
		}
		return map;
	}

	/**
	 * 解冻
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/tradeUnfreezeInfo")
	@ResponseBody
	@SystemLog(description = "交易解冻", operCode = "trade.thaw")
	public Object tradeUnfreezeInfo(@RequestBody String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			TransInfoFreezeNewLog tfnl = new TransInfoFreezeNewLog();
			String operId = principal.getId().toString();
			String realName = principal.getRealName();
			tfnl.setOperId(operId);
			tfnl.setOperName(realName);
			CollectiveTransOrder orderNo = json.getObject("info", CollectiveTransOrder.class);
			String fd = "0";
			tfnl.setOperType("1");
			tfnl.setOrderNo(orderNo.getOrderNo());
			tfnl.setOperTime(new Date());
			BigDecimal originalPreFrozenMoney = BigDecimal.ZERO;// 构建参数而已
			map = transInfoFreezeNewLogService.insertInfo(tfnl, orderNo, fd, "000018", operId, realName,
					originalPreFrozenMoney);
		} catch (Exception e) {
			log.error("解冻报错", e);
			map.put("bols", false);
			map.put("msg", "解冻报错");
		}
		return map;
	}

	/**
	 * 结算
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/settleTransInfo")
	@ResponseBody
	@SystemLog(description = "交易结算", operCode = "trade.settleTransInfo")
	public Object settleTransInfo(String id) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			// String card = transInfoService.findCardById(Integer.valueOf(id));
			// String num = riskRollService.findBlacklist(card, "3", "2");
			// if(num!=null){
			// map.put("bols", "0");
			// map.put("msg", "该结算卡是黑名单，不能结算");
			// }
			msg = transInfoHistoryService.settleTransInfo(id);
		} catch (Exception e) {
			log.error("结算异常");
			System.out.println(e);
			msg.put("bols", false);
			msg.put("msg", "结算异常");
		}
		return msg;
	}

	// ========================================

	// 销售查询
	/**
	 * 数据初始化和分页查询
	 * 
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping("/getAllInfoSale")
	@ResponseBody
	public Object getAllInfoSale(@ModelAttribute("page") Page<CollectiveTransOrder> page,
			@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CollectiveTransOrder tis = JSON.parseObject(param, CollectiveTransOrder.class);
			if (tis.getBool() == null || tis.getBool().equals("")) {
				tis.setBool("1");
			}
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			List<CollectiveTransOrder> clist = transInfoHistoryService.queryAllInfoSale(principal.getRealName(), tis, page);
			if (clist == null) {
				map.put("money", "0");
			} else {
				if (clist.get(0).getTotalMoney() == null) {
					map.put("money", "0");
				} else {
					map.put("money", clist.get(0).getTotalMoney());
				}
			}
			map.put("page", page);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			System.out.println(e);
			System.out.println(e.getMessage());
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}

	/**
	 * 外部账户调用交易查询
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 * @author transInfoAction/accountTranQuery.do
	 */
	
	@RequestMapping("/accountTranQuery.do")
	@ResponseBody
	public Map<String, Object> accountTranQuery(@RequestParam String token) throws Exception {
		log.info("accountTranQuery token:" + token);
		Map<String, Object> msg = new HashMap<>();
		msg.put("name", "交易查询");
		final String secret = Constants.ACCOUNT_API_SECURITY;
		String orderNo = null;
		try {
			final JWTVerifier verifier = new JWTVerifier(secret);
			final Map<String, Object> claims = verifier.verify(token);
			orderNo = (String) claims.get("transOrderNo");
			// String jti = (String) claims.get("jti");
			// long exp = Long.valueOf(claims.get("exp").toString());
			// long iat = Long.valueOf(claims.get("iat").toString());
			// String key = accountType+":"+userId+":"+jti;
			// long expireTime = exp - iat;
			// if(redisService.exists(key))
			// {
			// //返回 401
			// throw new JWTVerifyException("Invalid Token");
			// }
			// else{
			// redisService.insertSet(key, "1", expireTime);
			// }
			for (Map.Entry<String, Object> entry : claims.entrySet()) {
				log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
			}
		} catch (JWTVerifyException | InvalidKeyException | NoSuchAlgorithmException | IllegalStateException
				| SignatureException | IOException e) {
			log.debug(e.getMessage());
			msg.put("status", false);
			msg.put("msg", e.getMessage());
			return msg;
		}
		try {
			AccountCollectiveTransOrder list = transInfoHistoryService.queryInfoAccount(orderNo);
			if (list == null) {
				msg.put("status", false);
				msg.put("msg", "没查到数据");
			} else {
				msg.put("status", true);
				msg.put("msg", list);
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", e.getMessage());
		}
		return msg;
	}

	
	@RequestMapping(value = "/exportTransInfo")
	@ResponseBody
	public void exportInfo(@RequestParam("info") String info, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		info = URLDecoder.decode(info, "UTF-8");
		CollectiveTransOrder tis = JSON.parseObject(info, CollectiveTransOrder.class);
		if (tis.getBool() == null || tis.getBool().equals("")) {
			tis.setBool("1");
		}
		List<CollectiveTransOrder> list = transInfoHistoryService.importAllInfo(tis);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "交易记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("orderNo", null);
			maps.put("settlementMethod", null);
			maps.put("merchantName", null);
			maps.put("merchantNo", null);
			maps.put("acqReferenceNo", null);
			maps.put("unionpayMerNo", null);
			maps.put("payMethod", null);
			maps.put("orderType", null);
			maps.put("zxRate", null);
			maps.put("serviceType", null);
			maps.put("cardType", null);
			maps.put("accountNo", null);
			maps.put("iccardType", null);
			maps.put("transAmount", null);
			maps.put("nPrm", null);
			maps.put("merchantRate", null);
			maps.put("merchantFee", null);
			maps.put("deductionFee", null);
			maps.put("actualFee", null);
			maps.put("amount", null);
			maps.put("outAmount", null);
			maps.put("feeAmount", null);
			maps.put("outActualFee", null);
			maps.put("merchantPrice", null);
			maps.put("deductionMerFee", null);
			maps.put("actualMerchantPrice", null);
			maps.put("quickRate", null);
			maps.put("quickFee", null);
			maps.put("acqEnname",null);
			maps.put("transStatus", null);
			maps.put("resMsg", null);
			maps.put("freezeStatus", null);
			maps.put("settleStatus", null);
			maps.put("account", null);
			maps.put("settleType", null);
			maps.put("settleOrder", null);
			maps.put("createTime", null);
			maps.put("transTime", null);
			data.add(maps);
		} else {
			Map<String, String> orderTypeMap = sysDictService.selectMapByKey("ORDER_TYPE");
			Map<String, String> serviceTypeMap = sysDictService.selectMapByKey("SERVICE_TYPE");
			for (CollectiveTransOrder collectiveTransOrder : list) {
				collectiveTransOrder.setActualFee(collectiveTransOrder.getMerchantFee()==null?"0":collectiveTransOrder.getMerchantFee().toString());//实际交易手续费取向上取整的商户手续费，原表字段不用了

				if(collectiveTransOrder.getDeductionFee()!=null){
					collectiveTransOrder.setMerchantFee(collectiveTransOrder.getMerchantFee().add(new BigDecimal(collectiveTransOrder.getDeductionFee()))
							.subtract(collectiveTransOrder.getMerchantPrice())
							.add(collectiveTransOrder.getDeductionMerFee())
							.subtract(collectiveTransOrder.getnPrm()==null?BigDecimal.ZERO:collectiveTransOrder.getnPrm()));
				}

				String temp = collectiveTransOrder.getAmount();
				if (StringUtils.isNotBlank(temp)) {
					String[] temps = temp.split(",");
					collectiveTransOrder.setAmount(temps[0]);
					collectiveTransOrder.setFeeAmount(temps[1]);
					collectiveTransOrder.setOutAmount(temps[2]);
					collectiveTransOrder.setOutActualFee(temps[3]);
				}
				Map<String, String> maps = new HashMap<String, String>();
				// 结算方式
				String settlementMethod = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getSettlementMethod())) {
					if ("0".equals(collectiveTransOrder.getSettlementMethod())) {
						settlementMethod = "T0";
					} else {
						settlementMethod = "T1";
					}
				}
				// 支付方式
				String payMethod = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getPayMethod())) {
					if ("1".equals(collectiveTransOrder.getPayMethod())) {
						payMethod = "POS";
					} else if ("2".equals(collectiveTransOrder.getPayMethod())) {
						payMethod = "支付宝";
					} else if ("3".equals(collectiveTransOrder.getPayMethod())) {
						payMethod = "微信";
					} else if ("4".equals(collectiveTransOrder.getPayMethod())) {
						payMethod = "快捷支付";
					}
				}
				//判断订单是否微创业
				String orderType = "普通订单";//默认不是
				if(orderTypeMap!=null){
					orderType = orderTypeMap.get(String.valueOf(collectiveTransOrder.getOrderType()));
				}
				// 卡种类型
				String cardType = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getCardType())) {
					if ("0".equals(collectiveTransOrder.getCardType())) {
						cardType = "不限";
					} else if ("1".equals(collectiveTransOrder.getCardType())) {
						cardType = "贷记卡";
					} else if ("2".equals(collectiveTransOrder.getCardType())) {
						cardType = "借记卡";
					}
				}
				// 冻结状态
				String freezeStatus = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getFreezeStatus())) {
					if ("0".equals(collectiveTransOrder.getFreezeStatus())) {
						freezeStatus = "正常";
					} else if ("1".equals(collectiveTransOrder.getFreezeStatus())) {
						freezeStatus = "风控冻结";
					} else if ("2".equals(collectiveTransOrder.getFreezeStatus())) {
						freezeStatus = "活动冻结";
					} else if ("3".equals(collectiveTransOrder.getFreezeStatus())) {
						freezeStatus = "财务冻结";
					}
				}
				// 结算状态
				String settleStatus = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getSettleStatus())) {
					if ("0".equals(collectiveTransOrder.getSettleStatus())) {
						settleStatus = "未结算";
					} else if ("1".equals(collectiveTransOrder.getSettleStatus())) {
						settleStatus = "已结算";
					} else if ("2".equals(collectiveTransOrder.getSettleStatus())) {
						settleStatus = "结算中";
					} else if ("3".equals(collectiveTransOrder.getSettleStatus())) {
						settleStatus = "结算失败";
					} else if ("4".equals(collectiveTransOrder.getSettleStatus())) {
						settleStatus = "转T1结算";
					} else if ("5".equals(collectiveTransOrder.getSettleStatus())) {
						settleStatus = "不结算";
					} else if ("6".equals(collectiveTransOrder.getSettleStatus())) {
						settleStatus = "已返鼓励金";
					}
				}
				// 交易记账
				String account = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getAccount())) {
					if ("0".equals(collectiveTransOrder.getAccount())) {
						account = "未记账";
					} else if ("1".equals(collectiveTransOrder.getAccount())) {
						account = "记账成功";
					} else if ("2".equals(collectiveTransOrder.getAccount())) {
						account = "记账失败";
					}
				}
				String settleType="";
				if (StringUtils.isNotBlank(collectiveTransOrder.getSettleType())) {
					if ("1".equals(collectiveTransOrder.getSettleType())) {
						settleType = "T0交易";
					} else if ("2".equals(collectiveTransOrder.getSettleType())) {
						settleType = "手工提现";
					} else if ("3".equals(collectiveTransOrder.getSettleType())) {
						settleType = "T1线上代付";
					}else if ("4".equals(collectiveTransOrder.getSettleType())) {
						settleType = "T1线下代付";
					}
				}
				// 卡类型 I:芯片卡; C:非接芯片卡; S:磁条卡
				String iccardType = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getAccountNo())) {
					if ("quickPass".equals(collectiveTransOrder.getReadCard())) {
						iccardType = "C";
					}
					if (collectiveTransOrder.getIsIccard() != null && collectiveTransOrder.getIsIccard() == 1) {
						iccardType += "I";
					} else {
						iccardType += "S";
					}
				}
				// 收款类型
				if (collectiveTransOrder.getServiceType() != null && !collectiveTransOrder.getServiceType().isEmpty()) {
					collectiveTransOrder.setServiceType(serviceTypeMap.get(collectiveTransOrder.getServiceType()));
				}

				maps.put("id", collectiveTransOrder.getId().toString());
				maps.put("orderNo", collectiveTransOrder.getOrderNo());
				maps.put("settlementMethod", settlementMethod);
				maps.put("merchantName",null == collectiveTransOrder.getMerchantName() ? "" : collectiveTransOrder.getMerchantName());
				maps.put("merchantNo",null == collectiveTransOrder.getMerchantNo() ? "" : collectiveTransOrder.getMerchantNo());
				maps.put("acqReferenceNo", null == collectiveTransOrder.getAcqReferenceNo() ? "": collectiveTransOrder.getAcqReferenceNo());
				maps.put("unionpayMerNo",collectiveTransOrder.getUnionpayMerNo()==null?"":collectiveTransOrder.getUnionpayMerNo());
				maps.put("payMethod", payMethod);
				maps.put("orderType", orderType);
				maps.put("zxRate", collectiveTransOrder.getZxRate());
				maps.put("serviceType", null == collectiveTransOrder.getServiceType() ? "": collectiveTransOrder.getServiceType());
				maps.put("cardType", cardType);
				maps.put("accountNo",null == collectiveTransOrder.getAccountNo() ? "" : tis.getEditState()==0?
						StringUtil.sensitiveInformationHandle(collectiveTransOrder.getAccountNo(),4):collectiveTransOrder.getAccountNo());
				maps.put("iccardType", iccardType);
				maps.put("transAmount", null == collectiveTransOrder.getTransAmount() ? "" : collectiveTransOrder.getTransAmount().toString());
				maps.put("nPrm", null == collectiveTransOrder.getnPrm() ? "" :collectiveTransOrder.getnPrm().toString());
				maps.put("merchantRate",collectiveTransOrder.getMerchantRate()==null?"":collectiveTransOrder.getMerchantRate().toString());
				maps.put("merchantFee", null == collectiveTransOrder.getMerchantFee() ? "" : collectiveTransOrder.getMerchantFee().toString());
				maps.put("deductionFee", StringUtil.filterNull(collectiveTransOrder.getDeductionFee()));
				maps.put("actualFee",StringUtil.filterNull(collectiveTransOrder.getActualFee()));
				maps.put("amount",null == collectiveTransOrder.getAmount() ? "" : collectiveTransOrder.getAmount().toString());
				maps.put("outAmount", null == collectiveTransOrder.getOutAmount() ? "" : collectiveTransOrder.getOutAmount().toString());
				maps.put("feeAmount", null == collectiveTransOrder.getFeeAmount() ? "" : collectiveTransOrder.getFeeAmount().toString());
				maps.put("outActualFee", null == collectiveTransOrder.getOutActualFee() ? "" : collectiveTransOrder.getOutActualFee().toString());
				maps.put("merchantPrice",collectiveTransOrder.getMerchantPrice()==null?"":collectiveTransOrder.getMerchantPrice().toString());
				maps.put("deductionMerFee",collectiveTransOrder.getDeductionMerFee()==null?"":collectiveTransOrder.getDeductionMerFee().toString());
				maps.put("actualMerchantPrice",collectiveTransOrder.getActualMerchantPrice()==null?"":collectiveTransOrder.getActualMerchantPrice().toString());
				maps.put("quickRate", null == collectiveTransOrder.getQuickRate() ? "" : collectiveTransOrder.getQuickRate().toString());
				maps.put("quickFee", null == collectiveTransOrder.getQuickFee() ? "" : collectiveTransOrder.getQuickFee().toString());
				maps.put("acqEnname", null == collectiveTransOrder.getAcqEnname() ? "" : collectiveTransOrder.getAcqEnname());
				maps.put("transStatus", null == collectiveTransOrder.getTransStatus() ? "" : collectiveTransOrder.getTransStatus());
				maps.put("resMsg", null == collectiveTransOrder.getResMsg() ? "" : collectiveTransOrder.getResMsg());
				maps.put("freezeStatus", freezeStatus);
				maps.put("settleStatus", settleStatus);
				maps.put("account", account);
				maps.put("settleType",settleType);
				maps.put("settleOrder", collectiveTransOrder.getSettleOrder()==null?"":collectiveTransOrder.getSettleOrder());
				maps.put("createTime", collectiveTransOrder.getCreateTime()==null?"":sdf1.format(collectiveTransOrder.getCreateTime()));
				maps.put("transTime", null == collectiveTransOrder.getTransTime() ? "" : sdf1.format(collectiveTransOrder.getTransTime()));
				maps.put("settleMsg", collectiveTransOrder.getSettleMsg());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "id", "orderNo", "settlementMethod", "merchantName",
				"merchantNo","acqReferenceNo","unionpayMerNo","payMethod", "orderType","zxRate", "serviceType", "cardType",
				"accountNo","iccardType","transAmount","nPrm","merchantRate", "merchantFee", "deductionFee","actualFee",
				"amount", "outAmount", "feeAmount", "outActualFee", "merchantPrice",
				"deductionMerFee","actualMerchantPrice","quickRate","quickFee","acqEnname","transStatus","resMsg","freezeStatus", "settleStatus",
				"account","acqEnname","settleType","settleOrder","createTime", "transTime" ,"settleMsg"};
		String[] colsName = new String[] { "交易流水", "订单号", "结算周期", "商户简称",
				"商户编号","参考号", "银联报备商户编号", "交易方式", "订单类型","是否优享(一)收费", "收款类型", "卡种",
				"交易卡号","卡类型(I:芯片卡, CI:非接芯片卡, S:磁条卡)","金额（元）","保费","商户费率", "交易手续费（元）", "抵扣交易手续费（元）","实际交易手续费（元）",
				"出款金额（元）", "到账金额（元）", "出款手续费（元）","实际出款手续费（元）","优享(二)手续费（元）",
				"抵扣优享(二)手续费（元）","实际优享(二)手续费（元）", "云闪付费率（%）", "云闪付手续费（元）", "出款通道","交易状态","响应码","冻结状态", "结算状态",
				"交易记账","收单机构","出款类型","出款订单ID", "创建时间", "交易时间" ,"结算错误信息"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

	@RequestMapping("download")
	@ResponseBody
	public void downloadReceipt(@RequestParam("info") String info, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		System.out.println("b--info:"+info);
		log.info("b--info:"+info);
		info = request.getParameter("info");
		System.out.println("e--info:"+info);
		log.info("e--info:"+info);
		Map<String, String> map = JSON.parseObject(info, Map.class);
		// 生成图片
		int imageWidth = 750;// 图片的宽度
		int imageHeight = 1490;// 图片的高度
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, imageWidth, imageHeight);

		String bgPath = request.getSession().getServletContext().getRealPath("/") + "img" + File.separator + "receipt"
				+ File.separator + "beijing_img.png";
		BufferedImage bgImg = ImageIO.read(new FileInputStream(bgPath));
		graphics.drawImage(bgImg, 0, 0, imageWidth, imageHeight, null);

		String logoPath = request.getSession().getServletContext().getRealPath("/") + "img" + File.separator
				+ "receipt" + File.separator + "yinlian.png";
		BufferedImage logoImg = ImageIO.read(new FileInputStream(logoPath));
		graphics.drawImage(logoImg, 32, 50, logoImg.getWidth(), logoImg.getHeight(), null);

		graphics.setColor(new Color(0x45, 0x45, 0x45));
		graphics.setFont(new Font("微软雅黑", Font.PLAIN, 43));
		FontMetrics fm = graphics.getFontMetrics();
		String str = "签购单";
		int strWidth = fm.stringWidth(str);
		graphics.drawString(str, (imageWidth - strWidth) / 2, 70 + fm.getAscent());

		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		int strHeight = fm.getAscent();

		str = "商户存根";
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), 137 - strHeight - 10);

		str = "MERCHANT COPY";
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), 137);

		int y = 172;

		// 商户名称
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += fm.getAscent();
		str = "商户名称(MERCHANT)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		str = map.get("acqMerchantName");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 交易金额
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "交易金额(AMOUNT)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 37));
		fm = graphics.getFontMetrics();
		str = map.get("transAmount");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 操作员
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "操作员(OPERATOR)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		str = map.get("operatorNo");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 交易类型
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "交易类型(TRANS.TYPE)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		str = "消费";
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 卡号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "卡号(CARD NUMBER)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 27));
		fm = graphics.getFontMetrics();

		str = map.get("card_no");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 发卡行
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "发卡行(CARD ISSURE)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		str = map.get("bankName");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 收单机构
		if ("1".equals(map.get("myCheck"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 30 + fm.getAscent();
			str = "收单机构(ACQ NO)";
			graphics.drawString(str, 32, y);

			graphics.setColor(new Color(0x00, 0x9a, 0xe2));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			str = map.get("acqEnname");
			if (StringUtils.isNotEmpty(str))
				graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);
		}

		y += 30;
		String linePath = request.getSession().getServletContext().getRealPath("/") + "img" + File.separator
				+ "receipt" + File.separator + "xian_img.png";
		BufferedImage lineImg = ImageIO.read(new FileInputStream(linePath));
		graphics.drawImage(lineImg, 32, y, imageWidth - 64, 3, null);

		// 商户编号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 33 + fm.getAscent();
		str = "商户编号(MERCHANT NO.)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		str = map.get("acqMerchantNo");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 终端编号
		if ("1".equals(map.get("showAcqTerminalNo"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 30 + fm.getAscent();
			str = "终端编号(TERMINAL ID.)";
			graphics.drawString(str, 32, y);
	
			graphics.setColor(new Color(0x00, 0x9a, 0xe2));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			str = map.get("acqTerminalNo");
			graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);
		}

		// 终端号
		if ("1".equals(map.get("showYsTerminalNo"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 30 + fm.getAscent();
			str = "终端号(TERMINAL ID.)";
			graphics.drawString(str, 32, y);
			
			graphics.setColor(new Color(0x00, 0x9a, 0xe2));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			str = map.get("ysTerminalNo");
			graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);
		}

		// 授权号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "授权号(AUTH CODE)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		str = map.get("acqAuthNo");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 参考号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "参考号(REF.NO.)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		str = map.get("acqReferenceNo");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 凭证号
		if ("1".equals(map.get("acqSerialNoCheck"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 30 + fm.getAscent();
			str = "凭证号(VOICHER NO.)";
			graphics.drawString(str, 32, y);

			graphics.setColor(new Color(0x00, 0x9a, 0xe2));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			str = map.get("acqSerialNo");
			graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);
		}
		// 批次号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "批次号(BATCH NO.)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		str =map.get("batchNo");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);

		// 交易时间
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 30 + fm.getAscent();
		str = "交易时间(TRANS.TIME)";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0x00, 0x9a, 0xe2));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		str = map.get("transTime");
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);
		if ("1".equals(map.get("merchantAddress"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 30 + fm.getAscent();
			str = "交易地址(LOCATION)";
			graphics.drawString(str, 32, y);

			graphics.setColor(new Color(0x00, 0x9a, 0xe2));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			str = map.get("address");
			if (fm.stringWidth(str) < 400) {
				graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);
			} else {
				boolean isPln = false;
				String[] strArr = str.split("");
				String currentLine = strArr[0];
				for (int j = 1; j < strArr.length; j++) {
					if (fm.stringWidth(currentLine + strArr[j]) < 400) {
						currentLine += strArr[j];
					} else {
						if(isPln){
							y += 30 + fm.getAscent();
						}
						graphics.drawString(currentLine, imageWidth - 32 - fm.stringWidth(currentLine), y);
						currentLine = strArr[j];
						isPln = true;
					}
				}
				if (currentLine.trim().length() > 0) {
					y += 30 + fm.getAscent();
					graphics.drawString(currentLine, imageWidth - 32 - fm.stringWidth(currentLine), y);
				}
			}
		}

		y += 30;
		graphics.drawImage(lineImg, 32, y, imageWidth - 64, 3, null);

		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 23 + fm.getAscent();
		str = "签名(SIGNATURE)";
		graphics.drawString(str, 32, y);
		int height = 348;
		if ("1".equals(map.get("issignImg"))) {
			y += 10;
			try{
				BufferedImage signImgUrl = ALiYunOssUtil.fmtTransparentImg(map.get("signImg"));
				//图片等比处理
				int img_Height = signImgUrl.getHeight();//取得图片的长和宽
		         int img_Width = signImgUrl.getWidth();
		         double pre =Double.valueOf(img_Height)/img_Width;
		         height = (int) ((imageWidth - 64)*pre);
				graphics.drawImage(signImgUrl, 32, y, imageWidth - 64, height, null);
			}catch(Exception e){
				
			}
		}
		graphics.setColor(new Color(0xac, 0xad, 0xb5));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 10+height + fm.getAscent();
		str = "本人确认以上交易，同意将其计入本卡账户";
		graphics.drawString(str, (imageWidth - fm.stringWidth(str)) / 2, y);

		graphics.setFont(new Font("宋体", Font.PLAIN, 16));
		fm = graphics.getFontMetrics();
		y += 10 + fm.getAscent();
		str = "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES";
		graphics.drawString(str, (imageWidth - fm.stringWidth(str)) / 2, y);

		createImage(response, image);
		graphics.dispose();
	}

	@RequestMapping("download_one")
	public void downloadReceiptOne(@RequestParam("info") String info, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		System.out.println("b--info:"+info);
		log.info("b--info:"+info);
		info = request.getParameter("info");
		System.out.println("e--info:"+info);
		log.info("e--info:"+info);
		Map<String, String> map = JSON.parseObject(info, Map.class);
		// 生成图片
		int imageWidth = 450;// 图片的宽度
		int imageHeight = 1200;// 图片的高度
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, imageWidth, imageHeight);

		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 36));
		FontMetrics fm = graphics.getFontMetrics();
		String str = "POS签购单";
		int strWidth = fm.stringWidth(str);
		graphics.drawString(str, (imageWidth - strWidth) / 2, 70 + fm.getAscent());

		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();

		int y = 90;
		String linePath = request.getSession().getServletContext().getRealPath("/") + "img" + File.separator
				+ "receipt" + File.separator + "xian_img_1.png";
		BufferedImage lineImg = ImageIO.read(new FileInputStream(linePath));
		y += 30;
		graphics.drawImage(lineImg, 32, y, imageWidth - 64, 1, null);
		
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 10 + fm.getAscent();
		str = "商户存根";
		graphics.drawString(str, 32, y);

		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		str = "MERCHANT COPY";
		graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);
		y += 10;
		graphics.drawImage(lineImg, 32, y, imageWidth - 64, 1, null);
		// 商户名称
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 23 + fm.getAscent();
		str = "商户名称: " + map.get("acqMerchantName");
		graphics.drawString(str, 32, y);

		// 商户编号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "商户编号: " + map.get("acqMerchantNo");
		graphics.drawString(str, 32, y);

		// 终端编号
		if ("1".equals(map.get("showAcqTerminalNo"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 15 + fm.getAscent();
			str = "终端编号：" + map.get("acqTerminalNo");
			graphics.drawString(str, 32, y);
		}

		// 终端号
		if ("1".equals(map.get("showYsTerminalNo"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 15 + fm.getAscent();
			str = "终端号：" + map.get("ysTerminalNo");
			graphics.drawString(str, 32, y);
		}

		// 操作员
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "操作员： "+map.get("operatorNo");
		graphics.drawString(str, 32, y);
		// 卡号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "卡号： " + map.get("card_no");
		graphics.drawString(str, 32, y);

		// 发卡行
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "发卡行: " + map.get("bankName");
		graphics.drawString(str, 32, y);

		// 收单机构
		if ("1".equals(map.get("myCheck"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 15 + fm.getAscent();
			str = "收单行： " + map.get("acqEnname");
			graphics.drawString(str, 32, y);
		}
		// 交易类型
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "交易类型：消费";
		graphics.drawString(str, 32, y);
		// 凭证号
		if ("1".equals(map.get("acqSerialNoCheck"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 15 + fm.getAscent();
			str = "凭证号： " + map.get("acqSerialNo");
			graphics.drawString(str, 32, y);
		}
		// 批次号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "批次号： " + map.get("batchNo");
		graphics.drawString(str, 32, y);

		// 参考号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "检索参考号： " + map.get("acqReferenceNo");
		graphics.drawString(str, 32, y);

		// 授权号
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "授权号： " + map.get("acqAuthNo");
		graphics.drawString(str, 32, y);

		// 交易时间
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "时间： " + map.get("transTime");
		graphics.drawString(str, 32, y);
		// 交易金额
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "金额： " + map.get("transAmount");
		graphics.drawString(str, 32, y);

		if ("1".equals(map.get("merchantAddress"))) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 15 + fm.getAscent();
			str = "交易地址： " + map.get("address");
			//str = map.get("address");
			if (fm.stringWidth(str) < 400) {
				graphics.drawString(str, 32, y);
			} else {
				String[] strArr = str.split("");
				String currentLine = strArr[0];
				boolean isPln = false;
				for (int j = 1; j < strArr.length; j++) {
					if (fm.stringWidth(currentLine + strArr[j]) < 400) {
						currentLine += strArr[j];
					} else {
						if(isPln){
							y += 15 + fm.getAscent();
						}
						graphics.drawString(currentLine, 32, y);
						currentLine = strArr[j];
						isPln = true;
					}
				}
				if (currentLine.trim().length() > 0) {
					y += 10 + fm.getAscent();
					graphics.drawString(currentLine, 32, y);
				}
			}
		}
		y += 10;
		graphics.drawImage(lineImg, 32, y, imageWidth - 64, 1, null);
		graphics.setColor(new Color(0, 0, 0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "持卡人签名";
		int height = 348;
		graphics.drawString(str, 32, y);
		if ("1".equals(map.get("issignImg"))) {
			y += 15;
			try {
				BufferedImage signImgUrl = ALiYunOssUtil.fmtTransparentImg(map.get("signImg"));
				 int img_Height = signImgUrl.getHeight();//取得图片的长和宽
		         int img_Width = signImgUrl.getWidth();
		         double pre =Double.valueOf(img_Height)/img_Width;
		         height = (int) ((imageWidth - 64)*pre);
				graphics.drawImage(signImgUrl, 32, y, imageWidth - 64, height, null);
			} catch (Exception e) {
			}
		}
		graphics.setColor(new Color(0xac, 0xad, 0xb5));
		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 10+height + fm.getAscent();
		str = "本人确认以上交易，同意将其计入本卡账户";
		graphics.drawString(str, (imageWidth - fm.stringWidth(str)) / 2, y);

		graphics.setFont(new Font("宋体", Font.PLAIN, 16));
		fm = graphics.getFontMetrics();
		y += 15 + fm.getAscent();
		str = "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES";
		if (fm.stringWidth(str) < 400) {
			graphics.drawString(str, imageWidth - 32 - fm.stringWidth(str), y);
		} else {
			String[] strArr = str.split("");
			String currentLine = strArr[0];
			for (int j = 1; j < strArr.length; j++) {
				if (fm.stringWidth(currentLine + strArr[j]) < 400) {
					currentLine += strArr[j];
				} else {
					graphics.drawString(currentLine, 32, y);
					currentLine = strArr[j];
				}
			}
			if (currentLine.trim().length() > 0) {
				y += 10 + fm.getAscent();
				graphics.drawString(currentLine, 32, y);
			}
		}
		y += 10;
		graphics.drawImage(lineImg, 32, y, imageWidth - 64, 1, null);
		
		graphics.setColor(new Color(0,0,0));
		graphics.setFont(new Font("宋体", Font.PLAIN, 27));
		fm = graphics.getFontMetrics();
		y += 20 + fm.getAscent();
		str = "支付随心   畅想随行";
		graphics.drawString(str, (imageWidth - fm.stringWidth(str)) / 2, y);
		createImage(response, image);
		graphics.dispose();
	}

	private static void createImage(HttpServletResponse response, BufferedImage image) {
		try {

			String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
			OutputStream os = response.getOutputStream();
			response.reset(); // 清空输出流
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/png");
			response.setHeader("Content-disposition",
					"attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			BufferedOutputStream bos = new BufferedOutputStream(os);

			ImageIO.write(image, "png", bos);
			os.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 数据初始化和分页查询
	 * 
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping("/getShareSettleInfo")
	@ResponseBody
	public Object getShareSettleInfo(@ModelAttribute("page") Page<ShareSettleInfo> page,
			@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ShareSettleInfo tis = JSON.parseObject(param, ShareSettleInfo.class);
			transInfoHistoryService.queryShareSettleInfo(tis, page);
			map.put("page", page);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}


	/**
	 * 下载模板
	 */
	@RequestMapping("/downloadTemplate")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response) {
		String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator + "acqSingleShareSettle.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath, "长款分润处理excel模板.xlsx");
		return null;
	}
    /**
     * 长款分润导入
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/acqSingleImport")
    @ResponseBody
    public Object acqSingleImport(@RequestParam("file") MultipartFile file) {
			Map<String,Object> rsp = new HashMap<>();
			log.info("=====分润代付");
        try{
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String realName = principal.getUsername();
            Workbook wb  = WorkbookFactory.create(file.getInputStream());
			Sheet sheet = wb.getSheetAt(0);
			int row_num = sheet.getLastRowNum();
			int failCout = 0;
			int succCount = 0;
			for (int i = 1; i <= row_num; i++) {
				Row row = sheet.getRow(i);
				String  orderNo = row.getCell(0).getStringCellValue();
				if(StringUtil.isEmpty(orderNo)){
					continue;
				}
				ShareSettleInfo shareSettleInfo = transInfoHistoryService.shareSettleInfo(orderNo);
				if(shareSettleInfo!= null){
					failCout++;
					continue;
				}
				CollectiveTransOrder cto = transInfoHistoryService.queryCtoInfo(orderNo);
				if(cto==null){
					failCout++;
					continue;
				}
				ShareSettleInfo ssInfo = new ShareSettleInfo();
				ssInfo.setAcqEnname(cto.getAcqEnname());
				ssInfo.setAmount(cto.getMerchantFee());
				String shareSettleNo = "AS"+ DateUtil.dateToUnixTimestamp();
				String r = StringUtil.filterNull(Math.random());
				shareSettleNo = shareSettleNo + r.substring(r.indexOf(".")+1,7);
				ssInfo.setShareSettleNo(shareSettleNo);
				ssInfo.setOrderNo(cto.getOrderNo());
				ssInfo.setMerchantNo(cto.getMerchantNo());
				ssInfo.setType("1");
				ssInfo.setZqMerchantNo(cto.getUnionpayMerNo());
				ssInfo.setStatus("2");
				ssInfo.setOpertor(realName);
				transInfoHistoryService.insertShareSettleInfo(ssInfo);
				succCount++;
			}
            rsp.put("msg", "导入成功"+succCount+"条，导入失败"+failCout+"条");
        }catch (Exception e){
            log.error("",e);
            rsp.put("msg", "格式有误，导入失败");
        }
        return rsp;
    }

}