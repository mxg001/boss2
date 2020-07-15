package cn.eeepay.boss.action;

import cn.eeepay.boss.system.CommonConst;
import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.exportBigData.TransactionExoprtService;
import cn.eeepay.framework.service.sysUser.ExportManageService;
import cn.eeepay.framework.util.*;
import cn.hutool.core.util.StrUtil;
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
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping(value = "/transInfoAction")
public class TransInfoAction {
	private static final Logger log = LoggerFactory.getLogger(TransInfoAction.class);

	@Resource
	private SysDictService sysDictService;

	@Resource
	private TransInfoService transInfoService;

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
	
	@Resource
	private MsgService msgService;

	@Resource
	private SettleOrderInfoService settleOrderInfoService;

	@Resource
	private TransactionExoprtService transactionExoprtService;
	@Resource
	private ExportManageService exportManageService;
	@Resource
	private TaskExecutor taskExecutor;
	@Resource
	private SysWarningService sysWarningService;
	@Resource
	private OrderEventService orderEventService;
	/***
	 *	批量变更订单状态
	 * @param currSettleStatus 变更后的状态
	 * @param oldSettleStatus 变更前的状态
	 * @param changeSettleStatusReason 变更原因
	 * @param ids 变更订单号
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_MASTER)
	@RequestMapping(value="/changeSettleStatus")
	@ResponseBody
	@SystemLog(description = "交易查询订单状态变更",operCode = "transInfoAction.changeSettleStatus")
	public Object changeSettleStatus(@RequestParam("currSettleStatus") String currSettleStatus,
									 @RequestParam("oldSettleStatus") String oldSettleStatus,
									 @RequestParam("changeSettleStatusReason") String changeSettleStatusReason,
									 @RequestParam("ids") String[] ids,
									 @RequestParam("orderOrigin") Integer orderOrigin) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String msg = "";

		try {
			if(StringUtil.isEmpty(changeSettleStatusReason)){
				jsonMap.put("msg","变更原因为空");
				jsonMap.put("status", false);
				return jsonMap;
			}
			msg = settleOrderInfoService.changeSettleStatus(currSettleStatus, oldSettleStatus, changeSettleStatusReason, ids, principal.getUsername(),orderOrigin);
			jsonMap.put("msg", msg);
		} catch (Exception e) {
			log.error("变更出款订单明细状态出错",e);
			jsonMap.put("msg","变更出款订单明细状态出错");
			jsonMap.put("status", false);
		}
		return jsonMap;
	}
	
	/**
	 * 数据初始化和分页查询
	 * 
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
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
			String orderNo = format(tis.getOrderNo());
			String[] orderNos = null;
			if(StringUtils.isNotBlank(orderNo)){
				orderNos = orderNo.split(",");
			}
			if(null != orderNos && orderNos.length > 1){
				transInfoService.selectByOrderNos(orderNos, page);
			}else {
				transInfoService.queryAllInfo(tis, page);
			}
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
				if("V2-agent".equals(cto.getSourceSys())){
					cto.setSourceSysSta(1);
				}else{
					cto.setSourceSysSta(2);
				}
				cto.setActualFee(cto.getMerchantFee()==null?"0":cto.getMerchantFee().toString());//实际交易手续费取向上取整的商户手续费，原表字段不用了
				if(cto.getDeductionFee()!=null){
					cto.setMerchantFee(cto.getMerchantFee().add(new BigDecimal(cto.getDeductionFee()))
							.subtract(cto.getMerchantPrice())
							.add(cto.getDeductionMerFee())
							.subtract(cto.getnPrm()==null?BigDecimal.ZERO:cto.getnPrm()));
				}

				/**
				 * T0, 当天(去掉) 交易成功 交易记账:记账成功 冻结状态:正常 结算状态:结算失败\未结算 没有T1结算过
				 * 陈达娟提供可结算的条件
				 * 转T1
				 */
				if (cto.getTransTime() != null && "0".equals(cto.getSettlementMethod())
						&& "SUCCESS".equals(cto.getTransStatus()) && "1".equals(cto.getAccount())
						&& "0".equals(cto.getFreezeStatus()) && cto.getSettleType() == null
						&& cto.getSettleOrder() == null
						&& ("0".equals(cto.getSettleStatus()) || "3".equals(cto.getSettleStatus()) || "4".equals(cto.getSettleStatus()))) {
					page.getResult().get(i).setIsSettleMethod("1");
					//T1:1. 当天结算的订单，不能手动发起结算  2. 昨天及昨天之前结算状态为未结算、结算失败和转T1结算的T1订单，显示结算按钮。
				}else if("1".equals(cto.getSettlementMethod()) && cto.getDays().intValue() >= 1
						&& "SUCCESS".equals(cto.getTransStatus()) && "1".equals(cto.getAccount())
						&& "0".equals(cto.getFreezeStatus())
						&& (cto.getSettleOrder() == null||(cto.getSettleOrder() != null && "YS_ZQ".equals(cto.getAcqEnname())))
						&& ("0".equals(cto.getSettleStatus()) || "3".equals(cto.getSettleStatus()) || "4".equals(cto.getSettleStatus()))){
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
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getTotalNumAndTotalMoney")
	@ResponseBody
	public Object getTotalNumAndTotalMoney(@RequestParam("info") String param) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CollectiveTransOrder cto = JSON.parseObject(param, CollectiveTransOrder.class);
			if (cto.getBool() == null || cto.getBool().equals("")) {
				cto.setBool("1");
			}

			String orderNo = format(cto.getOrderNo());
			String[] orderNos = null;
			if(StringUtils.isNotBlank(orderNo)){
				orderNos = orderNo.split(",");
			}
			CollectiveTransOrder totalInfo = null;
			if(null != orderNos && orderNos.length > 1){
				totalInfo = transInfoService.queryNumAndMoneyByOrderNos(orderNos);
			}else {
				totalInfo = transInfoService.queryNumAndMoney(cto);
			}

			if(totalInfo!=null && totalInfo.getTotalDeductionFee() != null &&  1 == cto.getQueryTotalStatus()){
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
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/queryInfoDetail")
	@ResponseBody
	public Object queryInfoDetail(String ids,HttpServletRequest request)throws Exception{
		return getDetail(ids,request,0);
	}

	/**
	 * 脱敏处理显示
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getDetailShow")
	@ResponseBody
	public Object getDetailShow(String ids,HttpServletRequest request)throws Exception{
		return getDetail(ids,request,1);
	}

	private Map<String,Object> getDetail(String ids,HttpServletRequest request,int editState){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("msg", "查询失败");
		map.put("bols", false);
		try {
			CollectiveTransOrder tt = transInfoService.queryInfoDetail(ids);
			List<SettleTransfer> slist = transInfoService.selectSettleInfo(tt.getId().toString(), tt.getOrderNo());
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
					ysTerminalNo = transInfoService.queryAcqTerminalNo(tt.getAcqMerchantNo());
				}

				pcb1.setBankNo(mri.getContent());
				//脱敏处理
				if(0==editState){
					tt.setMobilephone(StringUtil.sensitiveInformationHandle(tt.getMobilephone(),0));
					tt.setAccountNo(StringUtil.sensitiveInformationHandle(tt.getAccountNo(),4));
					pcb1.setBankNo(StringUtil.sensitiveInformationHandle(pcb1.getBankNo(),2));
				}
				map.put("pcb1", pcb1);
				boolean isSalesperson = (boolean) request.getSession().getAttribute(CommonConst.isSalesperson);
				if(isSalesperson){
					String tmp = tt.getAccountNo();
					tmp = StrUtil.hide(tmp,0,tmp.length()-4);
					tt.setAccountNo(tmp);
				}

				//2018-12-21 修改响应码
				String resMsg = tt.getResMsg();
				String acqName = tt.getAcqEnname();
				if (StringUtils.isNotBlank(resMsg)) {
					//“错误原因”+“收单机构”，有合适的记录则返回“提示信息”；如果没查到记录，则单独按“错误原因”再查一次，如果还没查到，返回原始错误信息。

					String msgResonAcq = msgService.queryByResonAndAcqName(resMsg,acqName);
					if (StringUtils.isBlank(msgResonAcq)) {
						String msgReson = msgService.queryMsgByReason(resMsg);
						if(StringUtils.isNotBlank(msgReson)){
							tt.setResMsg(msgReson);
						}
					}else{
						tt.setResMsg(msgResonAcq);
					}


				}
				//查询订单事件
				List<OrderEventInfo> orderEventList1=orderEventService.getOrderEventListJY(tt.getOrderNo());
				List<OrderEventInfo> orderEventList2=orderEventService.getOrderEventListDF(tt.getOrderNo());
				List<OrderEventInfo> orderEventList=new ArrayList<OrderEventInfo>();
				if(orderEventList1!=null){
					orderEventList.addAll(orderEventList1);
				}
				if(orderEventList2!=null){
					orderEventList.addAll(orderEventList2);
				}
				map.put("orderEventList", orderEventList);
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
			msg = transInfoService.settleTransInfo(id);
		} catch (Exception e) {
			log.error("结算异常");
			System.out.println(e);
			msg.put("bols", false);
			msg.put("msg", "提交异常");
		}
		return msg;
	}

	// ========================================
	/**
	 * 批量手工结算 by sober
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/settleButch")
	@ResponseBody
	@SystemLog(description = "批量手工结算", operCode = "trade.settleBatch")
	public Object settleButch(@RequestBody String param, @RequestParam("type") String type) throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			List<CollectiveTransOrder> collectiveTransOrders = JSON.parseArray(param, CollectiveTransOrder.class);
			msg = transInfoService.settleButch(collectiveTransOrders, type);
		} catch (Exception e) {
			log.error("提交异常");
			System.out.println(e);
			e.printStackTrace();
			msg.put("bols", false);
			msg.put("msg", "提交异常");
		}
		return msg;
	}
	
	
	@RequestMapping("/syncOrder")
	@ResponseBody
	@SystemLog(description = "批量同步订单", operCode = "trade.syncOrder")
	public Object syncOrder(@RequestBody String param) throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			List<CollectiveTransOrder> collectiveTransOrders = JSON.parseArray(param, CollectiveTransOrder.class);
			msg = transInfoService.syncOrder(collectiveTransOrders);
		} catch (Exception e) {
			log.error("同步异常");
			System.out.println(e);
			e.printStackTrace();
			msg.put("bols", false);
			msg.put("msg", "同步异常");
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
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getAllInfoSale")
	@ResponseBody
	public Object getAllInfoSale(@ModelAttribute("page") Page<CollectiveTransOrder> page,
			@RequestParam("info") String param,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CollectiveTransOrder tis = JSON.parseObject(param, CollectiveTransOrder.class);
			if (tis.getBool() == null || tis.getBool().equals("")) {
				tis.setBool("1");
			}
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<CollectiveTransOrder> clist = transInfoService.queryAllInfoSale(principal.getRealName(), tis, page);
			if (clist == null) {
				map.put("money", "0");
			} else {
				if (clist.get(0).getTotalMoney() == null) {
					map.put("money", "0");
				} else {
					map.put("money", clist.get(0).getTotalMoney());
				}
			}
			boolean isSalesperson = (boolean) request.getSession().getAttribute(CommonConst.isSalesperson);
			if(isSalesperson){
				List<CollectiveTransOrder> items = page.getResult();
				String tmp;
				for (CollectiveTransOrder item: items) {
					tmp = item.getAccountNo();
					tmp = StrUtil.hide(tmp,0,tmp.length()-4);
					item.setAccountNo(tmp);
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
	@DataSource(Constants.DATA_SOURCE_SLAVE)
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
			AccountCollectiveTransOrder list = transInfoService.queryInfoAccount(orderNo);
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

	/**
	 * 异步执行导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/exportTransInfo")
	@ResponseBody
	public Map<String, Object> exportTransInfo(@RequestParam("info") final String param, HttpServletRequest request) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//个性标识,防止不同菜单用同一个实体
		final String md5Key= Md5.md5Str(principal.getUsername() +"_tran_"+ param);
		//校验key值
		if(exportManageService.checkExportManageInfo(md5Key,msg)){
			return msg;
		}
		final HttpSession session = request.getSession();
		//异步执行导出
		taskExecutor.execute(
				new Runnable() {
					@Override
					public void run() {
						transactionExoprtService.export(principal.getUsername(),md5Key,param, session);
					}
				}
		);
		msg.put("status", true);
		msg.put("msg", "导出操作成功,请稍后查询结果!");
		return msg;
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
		if(map.get("isDisplay").equals("1")){
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 10+height + fm.getAscent();
			if("quickPass".equals(map.get("readCard"))&&"5".equals(map.get("orderType"))){
				//只有是云闪付的时候才显示免密免签
				str = "交易金额未超1000.00元，免密免签";
			}else if("ic".equals(map.get("readCard"))||"track".equals(map.get("readCard"))){
				str = "交易金额未超1000.00元，免签";
			}

			graphics.drawString(str, (imageWidth - fm.stringWidth(str)) / 2, y);
		}

		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 10 + fm.getAscent();
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
		int imageHeight = 1220;// 图片的高度
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
		if(map.get("isDisplay").equals("1")){
			graphics.setFont(new Font("宋体", Font.PLAIN, 20));
			fm = graphics.getFontMetrics();
			y += 10+height + fm.getAscent();
			if("quickPass".equals(map.get("readCard"))&&"5".equals(map.get("orderType"))){
				//只有是云闪付的时候才显示免密免签
				str = "交易金额未超1000.00元，免密免签";
			}else if("ic".equals(map.get("readCard"))||"track".equals(map.get("readCard"))){
				str = "交易金额未超1000.00元，免签";
			}
			graphics.drawString(str, (imageWidth - fm.stringWidth(str)) / 2, y);
		}

		graphics.setFont(new Font("宋体", Font.PLAIN, 20));
		fm = graphics.getFontMetrics();
		y += 10 + fm.getAscent();
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
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getShareSettleInfo")
	@ResponseBody
	public Object getShareSettleInfo(@ModelAttribute("page") Page<ShareSettleInfo> page,
			@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ShareSettleInfo tis = JSON.parseObject(param, ShareSettleInfo.class);
			String orderNo = format(tis.getOrderNo());
			String[] orderNos = null;
			if(StringUtils.isNotBlank(orderNo)){
				orderNos = orderNo.split(",");
			}
			if(null != orderNos && orderNos.length > 1){
				transInfoService.selectShareSettleInfoByOrderNosWithPage(orderNos, page);
			}else {
				transInfoService.queryShareSettleInfo(tis, page);
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
				ShareSettleInfo shareSettleInfo = transInfoService.shareSettleInfo(orderNo);
				if(shareSettleInfo!= null){
					failCout++;
					continue;
				}
				CollectiveTransOrder cto = transInfoService.queryCtoInfo(orderNo);
				if(cto==null){
					failCout++;
					continue;
				}
				String uniNo = cto.getUnionpayMerNo();

				String terminalNo = "";
				AcqTerminalStore acqTerminalStore = transInfoService.getAcqTer(uniNo);
				if(acqTerminalStore == null ){
					ZqMerchantInfo zqMerchantInfo = transInfoService.getAcqMerchant(uniNo);
					if(zqMerchantInfo!= null)
					    terminalNo = zqMerchantInfo.getTerminalNo();
				}else {
					terminalNo = acqTerminalStore.getTerNo();
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
				ssInfo.setTerminalNo(terminalNo);
				transInfoService.insertShareSettleInfo(ssInfo);
				succCount++;
			}
            rsp.put("msg", "导入成功"+succCount+"条，导入失败"+failCout+"条");
        }catch (Exception e){
            log.error("",e);
            rsp.put("msg", "格式有误，导入失败");
        }
        return rsp;
    }



	@RequestMapping("/shareSettleButch")
	@ResponseBody
	@SystemLog(description = "批量手工结算", operCode = "trade.settleBatch")
	public Object shareSettleButch(@RequestBody String param) throws Exception {
		List<ShareSettleInfo> ssiList = JSON.parseArray(param, ShareSettleInfo.class);
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			List<CollectiveTransOrder> collectiveTransOrders = new ArrayList<>();
			for (ShareSettleInfo ssi: ssiList) {
				CollectiveTransOrder cto = new CollectiveTransOrder();
				cto.setOrderNo(ssi.getOrderNo());
				cto.setTransAmount(ssi.getAmount());
				collectiveTransOrders.add(cto);
			}
			msg = transInfoService.settleButch(collectiveTransOrders, "0");
		} catch (Exception e) {
			log.error("结算异常");
			System.out.println(e);
			e.printStackTrace();
			msg.put("bols", false);
			msg.put("msg", "结算异常");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/exportShareSettleInfo")
//	@SystemLog(description = "代付订单导出", operCode = "trade.tradeSettleExport")
	public void exportShareSettleInfo(@RequestParam("info") String info, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		ShareSettleInfo tis = JSON.parseObject(info, ShareSettleInfo.class);
		List<ShareSettleInfo> list = new ArrayList<>();
		String orderNo = format(tis.getOrderNo());
		String[] orderNos = null;
		if(StringUtils.isNotBlank(orderNo)){
			orderNos = orderNo.split(",");
		}
		if(null != orderNos && orderNos.length > 1){
			list = transInfoService.selectShareSettleInfoByOrderNos(orderNos);
		}else {
			list = transInfoService.exportShareSettleInfo(tis);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "代付订单记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
			maps.put("shareSettleNo",null);
			maps.put("orderNo",null);
			maps.put("merchantNo",null);
			maps.put("type",null);
			maps.put("acqEnname",null);
			maps.put("zqMerchantNo",null);
			maps.put("amount",null);
			maps.put("nPrm",null);
			maps.put("status",null);
			maps.put("errMsg",null);
			maps.put("startTime",null);
			maps.put("endTime",null);
			maps.put("orderType",null);
			data.add(maps);
		} else {
			Map<String, String> orderTypeMap = sysDictService.selectMapByKey("ORDER_TYPE");
			for (ShareSettleInfo ssi : list) {
				Map<String, String> maps = new HashMap<String, String>();
				String type = "";
				String status = "";
				String orderType = "";
				if(ssi.getType() != null){
					switch (ssi.getType()) {
					case "1":
						type = "交易";
						break;
					case "2":
						type = "出款";
						break;
					default:
						break;
					}
				}
				if(ssi.getStatus() != null){
					switch (ssi.getStatus()) {
					case "0":
						status="成功";
						break;
					case "1":
						status="失败";			
						break;
					case "2":
						status="待处理";
						break;
					case "3":
						status="处理中";
						break;
					case "4":
						status="需要重出";
						break;
					default:
						break;
					}
				}
				if(ssi.getOrderType() != null){
					orderType = orderTypeMap.get(String.valueOf(ssi.getOrderType()));
				}
				maps.put("id",String.valueOf(ssi.getId()));
				maps.put("shareSettleNo",ssi.getShareSettleNo());
				maps.put("orderNo",ssi.getOrderNo());
				maps.put("merchantNo",ssi.getMerchantNo());
				maps.put("type",type);
				maps.put("acqEnname",ssi.getAcqEnname());
				maps.put("zqMerchantNo",ssi.getZqMerchantNo());
				maps.put("amount",String.valueOf(ssi.getAmount()==null ? "0" : ssi.getAmount()));
				maps.put("nPrm",ssi.getnPrm()==null ? "" : ssi.getnPrm().toString());
				maps.put("status",status);
				maps.put("errMsg",ssi.getErrMsg()==null?"":ssi.getErrMsg());
				if(ssi.getStartTime() != null){
					maps.put("startTime",sdf1.format(ssi.getStartTime()));
				}else{
					maps.put("startTime",null);
				}
				if(ssi.getEndTime() != null){
					maps.put("endTime",sdf1.format(ssi.getEndTime()));
				}else{
					maps.put("endTime",null);
				}
				maps.put("orderType",orderType);
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "id", "shareSettleNo", "orderNo", "merchantNo","type", "acqEnname",
				"zqMerchantNo", "amount","nPrm","orderType","status","errMsg","startTime", "endTime"};
		String[] colsName = new String[] { "交易流水", "代付结算订单号", "来源单号", "商户号", "来源类型", "收单结构",
				"收单商户号", "金额","保费","活动类型", "状态","原因","创建时间","完成时间"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

	/**
	 * 同步交易状态
	 * @author	mays
	 * @date	2017年12月11日
	 */
	@RequestMapping("/syncTransStatus")
	@ResponseBody
	public Map<String, Object> syncTransStatus() {
		return transInfoService.syncTransStatus();
	}

	/**
	 * 批量记账
	 */
	@RequestMapping(value = "/accountRecordBatch")
	@ResponseBody
	@SystemLog(description = "批量记账",operCode="transInfoAction.accountRecordBatch")
	public Map<String,Object> accountRecordBatch(@RequestParam("ids") String ids) throws Exception{
		Map<String, Object> msg=new HashMap<String,Object>();
		try{
			transInfoService.accountRecordBatch(ids,msg);
		} catch (Exception e){
			log.error("批量记账失败!",e);
			msg.put("status", false);
			msg.put("msg", "批量记账失败!");
		}
		return msg;
	}

	/**
	 * 交易预警
	 *
	 * @return
	 */
	@RequestMapping(value = "/selectTradeWarning")
	@ResponseBody
	public Object selectTradeWarning() {
		Map<String, Object> maps = new HashMap<>();
		try {
			List<Map> list=sysWarningService.getListByType("7");
			Map<String, Object> info = new HashMap<>();
			info.put("content",list.get(0).get("content"));
			info.put("cd_time",list.get(0).get("cd_time"));
			info.put("phones",list.get(0).get("phones"));
			maps.put("list",list);
			maps.put("info",info);
			maps.put("status", true);
		} catch (Exception e) {
			log.error("实名认证预警查询报错", e);
			maps.put("msg", "实名认证预警查询报错");
			maps.put("status", false);
		}
		return maps;
	}

	/**
	 * 交易预警修改
	 *
	 * @return
	 */
	@RequestMapping(value = "/updateTradeWarning")
	@ResponseBody
	@SystemLog(description = "交易预警修改", operCode = "transInfoAction.updateTradeWarning")
	public Object updateTradeWarning(@RequestBody String param) {
		Map<String, Object> maps = new HashMap<>();
		try {
			JSONObject json = JSON.parseObject(param);
			Map<String, Object> info = json.getObject("info", Map.class);
			List<Map> tradeWarning = json.getJSONArray("tradeWarning").toJavaList(Map.class);
			List<Integer> delIds = json.getJSONArray("delIds").toJavaList(Integer.class);
			maps=transInfoService.updateTradeWarning(info,tradeWarning,delIds);
		} catch (Exception e) {
			log.error("交易预警修改报错", e);
			maps.put("msg", "交易预警修改报错");
			maps.put("status", false);
		}
		return maps;
	}

	private String format(String orderNo){
		return orderNo.replace("\n","")
				.replace(" ", "")
				.replace("，", ","); // 中文逗号转英文
	}
}