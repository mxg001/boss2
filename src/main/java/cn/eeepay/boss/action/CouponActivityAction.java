package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.impl.SensorsService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * 优惠券活动管理（包括充值返、签到返、注册返、云闪付）
 * @author ivan
 * @date 2017/06/19
 * 
 */
@Controller
@RequestMapping(value="/couponActivity")
public class CouponActivityAction {
	public static final String  FLUSHACTIVETIYCODE="3";//活动编号3
	public static  final String FLUSHACTIVETIYCODE_BUYREWARD="6";//购买鼓励金功能编码

	private static final Logger log = LoggerFactory.getLogger(CouponActivityAction.class);
	@Resource
	private CouponActivityService couponActivityService;

	@Resource
	private MerchantInfoService merchantInfoService;

	@Resource
    private ZxIndustryConfigService zxIndustryConfigService;

	@Resource
	private CouponImportService couponImportService;

	@Resource
	private SensorsService sensorsService;
	@Resource
	private SysDictService sysDictService;

	/**
	 *优惠券活动列表信息
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getInfo")
	@ResponseBody
	public Object getInfo(){
		Map<String, Object> map = new HashMap<>();
		try {
			List<CouponActivityInfo> list = couponActivityService.getAllInfo();
			map.put("raList", list);
			map.put("status", true);
		} catch (Exception e) {
			log.error("获取活动列表信息失败,",e);
			map.put("msg", "活动列表数据读取失败");
			map.put("status", false);
		}
		return map;
	}
	/**
	 * 优惠券活动详情活动 one ---优惠券 one
	 * @param actId 活动ID
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/rewardDetail")
	@ResponseBody
	public Object rewardDetail(@RequestParam("actId") int actId) throws Exception{
		CouponActivityInfo couponActivity=null;
		Map<String, Object> map = new HashMap<>();
		try{
			couponActivity = couponActivityService.queryRewardDetail(actId);
			map.put("info", couponActivity);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询详情失败！",e);
			map.put("status", false);
		}
		return map;
	}

	/**
	 * 注册返活动详情
	 * @author	mays
	 * @date	2018年1月22日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/registeredDetail")
	@ResponseBody
	public Object registeredDetail() {
		Map<String, Object> map = new HashMap<>();
		try{
			CouponActivityInfo couponActivity = couponActivityService.queryRegisteredDetail();
			map.put("info", couponActivity);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询注册返活动详情失败！",e);
			map.put("status", false);
		}
		return map;
	}

	/**
	 * 注册返券查询
	 * @author	mays
	 * @date	2018年1月23日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectRegisteredCoupon")
	@ResponseBody
	public Object selectRegisteredCoupon() {
		Map<String, Object> map = new HashMap<>();
		try{
			List<CouponActivityInfo> couponList = couponActivityService.selectRegisteredCoupon();
			map.put("couponList", couponList);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询注册返券面值失败！",e);
			map.put("status", false);
		}
		return map;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectCardAndReward")
	@ResponseBody
	public Object selectCardAndReward(@RequestParam("activetiyCode") String activetiyCode) {
		Map<String, Object> map = new HashMap<>();
		try{
			List<CouponActivityInfo> couponList = couponActivityService.selectCardAndReward(activetiyCode);
			map.put("couponList", couponList);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询注册返券面值失败！",e);
			map.put("status", false);
		}
		return map;
	}
	
	
	/**
	 * 修改注册返券面值
	 * @author	mays
	 * @date	2018年1月23日
	 */
	@RequestMapping(value = "/updateRegisteredCoupon")
	@SystemLog(description = "修改注册返券面值", operCode = "couponActivity.updateRegisteredCoupon")
	@ResponseBody
	public Object updateRegisteredCoupon(@RequestBody String param) {
		Map<String, Object> map = new HashMap<>();
		try{
			CouponActivityInfo info = JSONObject.parseObject(param, CouponActivityInfo.class);
			if (info.getCouponName() == null || info.getCouponAmount() == null) {
				map.put("status", false);
				map.put("msg", "数据异常");
				return map;
			}
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			info.setOperator(principal.getUsername());
			int i = couponActivityService.updateRegisteredCoupon(info);
			if (i == 1) {
				map.put("status", true);
				map.put("msg", "修改成功");
			} else {
				map.put("status", false);
				map.put("msg", "修改失败");
			}
		}catch(Exception e){
			log.error("修改注册返券面值失败！",e);
			map.put("status", false);
			map.put("msg", "修改失败");
		}
		return map;
	}
	
	/**
	 * 优惠券活动详情(活动 one ---优惠券 many)
	 * @param actId 活动ID
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/couponRecharge")
	@ResponseBody
	public Object couponRecharge(@RequestParam("actId") int actId,@RequestParam("query") int query) throws Exception{
		CouponActivityInfo couponActivity=null;
		List<CouponActivityEntity> entity=null;
		Map<String, Object> map = new HashMap<>();
		try{
			couponActivity = couponActivityService.couponRecharge(actId);
			entity = couponActivityService.couponRechargeEntry(couponActivity.getActivetiyCode());
			map.put("info", couponActivity);
			map.put("entity", entity);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询详情失败！",e);
			map.put("status", false);
		}
		return map;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/couponEntityView")
	@ResponseBody
	public Object couponEntityView(@RequestParam("entityId") int entityId) throws Exception{
		CouponActivityEntity entity=null;
		Map<String, Object> map = new HashMap<>();
		try{
			entity = couponActivityService.couponEntityView(entityId);
			List<CouponActivityTime> couponTime=couponActivityService.getCouponActivityTime(entityId);
			map.put("entity", entity);
			map.put("couponTime", couponTime);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询详情失败！",e);
			map.put("status", false);
		}
		return map;
	}

	/**
	 * 活动优惠券保存
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveCouponEntity")
	@SystemLog(description = "充值返-新增券|修改券",operCode="couponEntity.save")
	@ResponseBody
	public Object saveCouponEntity(@RequestParam("info") String info,
								   @RequestParam("formatList") String formatList,
								   @RequestParam("deleteinfo") String deleteinfo) throws Exception{
		CouponActivityEntity entry = JSON.parseObject(info,CouponActivityEntity.class);
		List<CouponActivityTime> timeList = JSON.parseArray(formatList,CouponActivityTime.class);
		List<CouponActivityTime> deleteList = JSON.parseArray(deleteinfo,CouponActivityTime.class);
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.saveCouponEntity(entry,timeList,deleteList);
			if(i == 1){
				if(CouponActivityAction.FLUSHACTIVETIYCODE.equals(entry.getActivetiyCode())){
					ClientInterface.flushActivityCache(4);
				}
				if(CouponActivityAction.FLUSHACTIVETIYCODE_BUYREWARD.equals(entry.getActivetiyCode())){
					ClientInterface.flushActivityCache(4);
				}
				map.put("status", true);
				map.put("msg", "操作成功！");
			}else{
				map.put("status", false);
				map.put("msg", "操作失败！");
				log.error("操作失败！");
			}
		}catch(Exception e){
			log.error("操作失败！",e);
			map.put("status", false);
			map.put("msg", "操作失败！");
		}
		return map;
	}
	/**
	 * 活动保存
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveCouponActivity")
	@ResponseBody
	@SystemLog(description = "充值返活动修改",operCode="couponActivity.save")
	public Object saveCouponActivity(@RequestParam("info") String param) throws Exception{
		CouponActivityInfo couponActivity = JSON.parseObject(param,CouponActivityInfo.class);
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.saveCouponActivity(couponActivity);
			if(i == 1){
				map.put("status", true);
				map.put("msg", "修改成功！");
			}else{
				map.put("status", false);
				map.put("msg", "修改失败！");
				log.error("活动修改失败！");
			}
		}catch(Exception e){
			log.error("活动修改失败！",e);
			map.put("status", false);
			map.put("msg", "修改失败！");
		}
		return map;
	}
	
	/**
	 * 优惠券活动更新
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/rewardSave")
	@ResponseBody
	@SystemLog(description = "签到返活动修改",operCode="couponActivity.rewardSave")
	public Object rewardSave(@RequestParam("info") String param) throws Exception{
		CouponActivityInfo couponActivity = JSON.parseObject(param,CouponActivityInfo.class);
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.rewardSave(couponActivity);
			if(i == 1){
				map.put("status", true);
				map.put("msg", "修改成功！");
			}else{
				map.put("status", false);
				map.put("msg", "修改失败！");
				log.error("活动修改失败！");
			}
		}catch(Exception e){
			log.error("活动修改失败！",e);
			map.put("status", false);
			map.put("msg", "修改失败！");
		}
		return map;
	}

	
	/**
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanAndRewardSave")
	@ResponseBody
	@SystemLog(description = "贷款送奖励",operCode="couponActivity.loanAndRewardSave")
	public Object loanAndRewardSave(@RequestParam("info") String param) throws Exception{
		CouponActivityInfo couponActivity = JSON.parseObject(param,CouponActivityInfo.class);
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.cardAndRewardSave(couponActivity);
			if(i == 1){
				map.put("status", true);
				map.put("msg", "修改成功！");
			}else{
				map.put("status", false);
				map.put("msg", "修改失败！");
				log.error("活动修改失败！");
			}
		}catch(Exception e){
			log.error("活动修改失败！",e);
			map.put("status", false);
			map.put("msg", "修改失败！");
		}
		return map;
	}
	/**
	 * 优惠券活动更新
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cardAndRewardSave")
	@ResponseBody
	@SystemLog(description = "办卡送奖励",operCode="couponActivity.cardAndRewardSave")
	public Object cardAndRewardSave(@RequestParam("info") String param) throws Exception{
		CouponActivityInfo couponActivity = JSON.parseObject(param,CouponActivityInfo.class);
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.cardAndRewardSave(couponActivity);
			if(i == 1){
				map.put("status", true);
				map.put("msg", "修改成功！");
			}else{
				map.put("status", false);
				map.put("msg", "修改失败！");
				log.error("活动修改失败！");
			}
		}catch(Exception e){
			log.error("活动修改失败！",e);
			map.put("status", false);
			map.put("msg", "修改失败！");
		}
		return map;
	}
	/**
	 * 保存注册返活动配置
	 * @author	mays
	 * @date	2018年1月22日
	 */
	@RequestMapping(value = "/registeredSave")
	@ResponseBody
	@SystemLog(description = "注册返活动修改",operCode="couponActivity.registeredSave")
	public Object registeredSave(@RequestParam("info") String param) {
		Map<String, Object> map = new HashMap<>();
		try{
			CouponActivityInfo couponActivity = JSON.parseObject(param,CouponActivityInfo.class);
			int i = couponActivityService.registeredSave(couponActivity);
			if(i == 1){
				map.put("status", true);
				map.put("msg", "修改成功！");
			}else{
				map.put("status", false);
				map.put("msg", "修改失败！");
			}
		}catch(Exception e){
			log.error("注册返活动修改失败！", e);
			map.put("status", false);
			map.put("msg", "修改失败！");
		}
		return map;
	}

	/**
	 * 云闪付活动保存
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cloudPaySave")
	@ResponseBody
	@SystemLog(description = "云闪付返活动修改",operCode="couponActivity.cloudPaySave")
	public Object cloudPaySave(@RequestParam("info") String param) throws Exception{
		CouponActivityInfo couponActivity = JSON.parseObject(param,CouponActivityInfo.class);
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.cloudPaySave(couponActivity);
			if(i == 1){
				map.put("status", true);
				map.put("msg", "修改成功！");
			}else{
				map.put("status", false);
				map.put("msg", "修改失败！");
				log.error("活动修改失败！");
			}
		}catch(Exception e){
			log.error("活动修改失败！",e);
			map.put("status", false);
			map.put("msg", "修改失败！");
		}
		return map;
	}
	
	/**
	 * 数据初始化和分页查询
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getCouponAll")
	@ResponseBody
	public Object getCouponAll(@ModelAttribute("page") Page<CouponTotal> page,
			@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CouponTotal  coupon = JSON.parseObject(param, CouponTotal.class);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(coupon.getEndStartTime()!=null){
				Date endStartTime= formatter.parse((new SimpleDateFormat("yyyy-MM-dd").format(coupon.getEndStartTime()))+" 00:00:00");
				coupon.setEndStartTime(endStartTime);
			}
			if(coupon.getEndEndTime()!=null){
				Date endEndTime= formatter.parse((new SimpleDateFormat("yyyy-MM-dd").format(coupon.getEndEndTime()))+" 23:59:59");
				coupon.setEndEndTime(endEndTime);
			}
			couponActivityService.queryCouponAll(coupon, page);
			map.put("page", page);
			map.put("status", true);
		} catch (Exception e) {
			log.error("查询错误", e);
			map.put("status", false);
			map.put("msg", "查询错误");
		}
		return map;
	}

	/**
	 * 数据初始化和分页查询(统计)
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getCouponAllSum")
	@ResponseBody
	public Object getCouponAllSum(@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CouponTotal  coupon = JSON.parseObject(param, CouponTotal.class);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(coupon.getEndStartTime()!=null){
				Date endStartTime= formatter.parse((new SimpleDateFormat("yyyy-MM-dd").format(coupon.getEndStartTime()))+" 00:00:00");
				coupon.setEndStartTime(endStartTime);
			}
			if(coupon.getEndEndTime()!=null){
				Date endEndTime= formatter.parse((new SimpleDateFormat("yyyy-MM-dd").format(coupon.getEndEndTime()))+" 23:59:59");
				coupon.setEndEndTime(endEndTime);
			}
			//统计
			CouponTotal total = couponActivityService.queryCouponAllTotal(coupon);
			map.put("total", total);
			map.put("status", true);
		} catch (Exception e) {
			log.error("查询错误", e);
			map.put("status", false);
			map.put("msg", "查询错误");
		}
		return map;
	}
	/**
	 * 更新活动优惠券配置表状态
	 * @param id 数据ID
	 * @param col 更新字段
	 * @param val 更新值
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateStatus")
	@SystemLog(description = "充值返券开关控制修改",operCode="couponActivity.updateStatus")
	@ResponseBody
	public Object updateStatus(@RequestParam("id") int id,@RequestParam("col") String col,@RequestParam("val") int val) throws Exception{
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.updateStatus(id,col,val);
			if(i == 1){
				CouponActivityEntity entry = couponActivityService.couponEntityView(id);
				if(CouponActivityAction.FLUSHACTIVETIYCODE.equals(entry.getActivetiyCode())){
					ClientInterface.flushActivityCache(4);
				}
				if(CouponActivityAction.FLUSHACTIVETIYCODE_BUYREWARD.equals(entry.getActivetiyCode())){
					ClientInterface.flushActivityCache(4);
				}
				map.put("status", true);
				map.put("msg", "修改成功！");
			}else{
				map.put("status", false);
				map.put("msg", "修改失败！");
				log.error("券状态修改失败{}！",col);
			}
		}catch(Exception e){
			log.error("券状态修改失败！",e);
			map.put("status", false);
			map.put("msg", "修改失败！");
		}
		return map;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/exportCoupon")
	@ResponseBody
	public void exportCoupon(@RequestParam("info") String param, HttpServletResponse response) throws Exception {

		CouponTotal  coupon = JSON.parseObject(param, CouponTotal.class);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(coupon.getEndStartTime()!=null){
			Date endStartTime= formatter.parse((new SimpleDateFormat("yyyy-MM-dd").format(coupon.getEndStartTime()))+" 00:00:00");
			coupon.setEndStartTime(endStartTime);
		}
		if(coupon.getEndEndTime()!=null){
			Date endEndTime= formatter.parse((new SimpleDateFormat("yyyy-MM-dd").format(coupon.getEndEndTime()))+" 23:59:59");
			coupon.setEndEndTime(endEndTime);
		}
		String fileName = "券记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<>();
		log.info(JSONObject.toJSONString(coupon));
		List<CouponTotal> list = couponActivityService.queryCouponList(coupon);

		Map<String, String> couponTypeMap=sysDictService.selectMapByKey("COUPON_TYPE");//券类型

		for (CouponTotal couponTotal : list){
			String cou = JSONObject.toJSONString(couponTotal);
			Map<String,String> temp = JSONObject.parseObject(cou,Map.class);
			if(!StringUtil.isEmpty(StringUtil.filterNull(couponTotal.getStartTime()))){
				temp.put("startTime",sdf.format(couponTotal.getStartTime()));
			}
			if(!StringUtil.isEmpty(StringUtil.filterNull(couponTotal.getEndTime()))){
				temp.put("endTime",sdf.format(couponTotal.getEndTime()));
			}
			String payMethodValue = "";
			String payMethod = couponTotal.getPayMethod();
			if(StringUtils.isNotBlank(payMethod)){
				if("1".equals(payMethod)){
					payMethodValue = "POS";
				}
				if("2".equals(payMethod)){
					payMethodValue = "支付宝";
				}
				if("3".equals(payMethod)){
					payMethodValue = "微信";
				}
				if("4".equals(payMethod)){
					payMethodValue = "快捷";
				}
			}
			temp.put("payMethod", payMethodValue);
			temp.put("couponType", couponTypeMap.get(couponTotal.getCouponType()));
			data.add(temp);
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id", "merchantName", "merchantNo", "mobilephone", "couponCode", "agentName",
				"oneAgentName", "transAmount", "giftAmount", "faceValue", "balance", "useValue","couponType",
				"couponStatus","startTime","endTime","couponNo","payMethod","payOrderNo"};
		String[] colsName = new String[]{"序号", "商户名称", "商户编号", "商户手机号", "参与活动", "直属代理商",
				"一级代理商", "购买金额","赠送金额", "总价值金额", "可用金额","已使用金额","券类型",
				"使用状态","获得日期","失效日期","券编号","支付方式","支付订单号"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, ouputStream);
		ouputStream.close();
	}


	/**
	 * 新增券
	 * @param info
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addNewCoupon")
	@SystemLog(description = "新增活动券", operCode = "couponActivity.addActivityCoupon")
	public Object addNewCoupon(@RequestParam String info){
		Map<String,String> res = new HashMap<>();
		log.info("===新增券===");
		Map<String,String> params = JSONObject.parseObject(info,Map.class);
		if(params == null){
			res.put("msg","参数有误！");
			return res;
		}
		String orderNo = params.get("orderNo");
		String couponCode = StringUtil.filterNull(params.get("couponCode"));
		
		
		if ("13".equals(couponCode)|| "12".equals(couponCode)) {
			res.put("msg","该活动不允许添加,请前往对应模块操作！");
			return res;
		}
		String merchantNo = params.get("merchantNo");
		if(!StringUtil.isEmpty(orderNo)&&"3".equals(couponCode)){
			Map<String,Object> activtyOrderInfo = couponActivityService.activtyOrderInfo(orderNo);
			if(activtyOrderInfo==null){
				res.put("msg","当前充值返订单不存在！");
				return res;
			}
			if(activtyOrderInfo!=null&&activtyOrderInfo.get("coupon_no")!=null){
				res.put("msg","当前订单关联券已经存在！");
				return res;
			}
			if(activtyOrderInfo.get("merchant_no")!=null&&!activtyOrderInfo.get("merchant_no").toString().equals(merchantNo)){
				res.put("msg","当前订单关联券的商户号与赠送商户号不一致！");
				return res;
			}
		}else if("3".equals(couponCode)&&StringUtil.isEmpty(orderNo)) {
			res.put("msg","充值返请填写关联交易订单号！");
			return res;
		}
		if(!StringUtil.isEmpty(orderNo)&&"6".equals(couponCode)){
			Map<String,Object> activtyOrderInfo = couponActivityService.activtyOrderInfo(orderNo);
			if(activtyOrderInfo==null){
				res.put("msg","当前购买鼓励金订单不存在！");
				return res;
			}
			if(activtyOrderInfo!=null&&activtyOrderInfo.get("coupon_no")!=null){
				res.put("msg","当前购买鼓励金关联券已经存在！");
				return res;
			}
			if(activtyOrderInfo.get("merchant_no")!=null&&!activtyOrderInfo.get("merchant_no").toString().equals(merchantNo)){
				res.put("msg","当前购买鼓励金关联券的商户号与赠送商户号不一致！");
				return res;
			}
		}else if("6".equals(couponCode)&&StringUtil.isEmpty(orderNo)) {
			res.put("msg","购买鼓励金请填写关联交易订单号！");
			return res;
		}

		//判断赠送数量是否大于发行数量
		if("15".equals(couponCode)|| "16".equals(couponCode)){
			if(!couponActivityService.checkPurchaseCount(params,res)){
				return res;
			}
		}


		int num = Integer.parseInt(StringUtil.ifEmptyThen(params.get("addNum"),"0"));
		for (int i =0;i<num;i++){
			try {
				reqCoupon(params,res);
			} catch (Exception e) {
				log.info("",e);
				res.put("msg","生成券失败,请检查券配置信息！");
			}
		}
		return res;
	}




	private void reqCoupon(Map<String,String> params,Map<String,String> res) throws Exception {


		String merchantNo = params.get("merchantNo");
		String couponCode = StringUtil.filterNull(params.get("couponCode"));

		//调用券发放接口
		String couponNo = registerCoupon(params);

		log.info("====券编号[{}]",couponNo);
		if("1".equals(couponCode)){
			int merRes  = couponActivityService.updateMerActStatus(merchantNo);
			log.info("==修改商户是否存量,[{}]==[{}]",merchantNo,merRes);
		}
		if("6".equals(couponCode)) {
			String orderNo = params.get("orderNo");
			int adFlag = couponActivityService.addCouponOrderInfo(couponNo,orderNo);
			log.info("===购买鼓励金添加订单结果[{}]",adFlag);
			if(adFlag>0){
				res.put("msg","新增成功！");
			}else{
				res.put("msg","关联购买鼓励金订单失败！");
			}

		}else if("3".equals(couponCode)) {
			String orderNo = params.get("orderNo");
			int adFlag = couponActivityService.addCouponOrderInfo(couponNo,orderNo);
			log.info("===充值返添加订单结果[{}]",adFlag);
			if(adFlag>0){
				res.put("msg","新增成功！");
			}else{
				res.put("msg","关联充值返订单失败！");
			}

		}else{
			res.put("msg","新增成功！");
		}
		log.info("[{}]",res);
	}



	public String registerCoupon(Map<String, String> actCouponMap) throws Exception {

		String merchantNo = actCouponMap.get("merchantNo");
		String couponCode = StringUtil.filterNull(actCouponMap.get("couponCode"));
		String activityEntityIdStr = StringUtil.filterNull(actCouponMap.get("activityEntityId"));
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String couDate = sdf.format(new Date());
		String random = StringUtil.filterNull(Math.random());
		random = random.substring(random.indexOf(".")+1, 10);
		couDate = couDate+random;
		int activityEntityId = 0;
		Map<String, Object> activityMap = null;
		if("3".equals(couponCode)||"6".equals(couponCode)||"8".equals(couponCode)||"9".equals(couponCode)||"15".equals(couponCode)||"16".equals(couponCode) ||"17".equals(couponCode)){
			activityEntityId = Integer.parseInt(activityEntityIdStr);
			activityMap = couponActivityService.getActivityById(activityEntityId);
		} else if ("1".equals(couponCode)) {
			activityEntityId = Integer.parseInt(activityEntityIdStr);
			activityMap = couponActivityService.getActivityById2(couponCode,activityEntityId);
		} else {
			activityMap =couponActivityService.getActivityByCode(couponCode);
		}
		String token = getTokenStr(couDate,couponCode);
		if (activityMap != null && activityMap.size() > 0) {

			log.info("有效期时间"+activityMap.get("effective_days").toString());
			String currentDate = DateUtils.getCurrentDate();
			String couponStart = currentDate + " 00:00:00";
			Date couponStartDate = DateUtils.parseDateTime(couponStart);
			int effective_days = Integer.parseInt(activityMap.get("effective_days").toString());
			Date couponEndDate = DateUtils.addDate(couponStartDate, effective_days-1);
			String couponEnd = DateUtils.format(couponEndDate, "yyyy-MM-dd")+ " 23:59:59";
			Date endDate = DateUtils.parseDateTime(couponEnd);
			Map<String, Object> couponMap = new HashMap<String, Object>();
			//充值返金额加赠送金
			if ("3".equals(couponCode)||"6".equals(couponCode)||"8".equals(couponCode)||"9".equals(couponCode)) {
				BigDecimal couponAmount = new BigDecimal(activityMap.get("coupon_amount").toString());
				BigDecimal giftAmount = new BigDecimal(activityMap.get("gift_amount").toString());
				BigDecimal faceValue = couponAmount.add(giftAmount);
				couponMap.put("face_value", faceValue);
				couponMap.put("balance", faceValue);
				couponMap.put("coupon_amount", activityMap.get("coupon_amount"));
				couponMap.put("gift_amount", activityMap.get("gift_amount"));
			}else if("15".equals(couponCode)||"16".equals(couponCode)){
				BigDecimal couponAmount = new BigDecimal(activityMap.get("coupon_amount")==null?"0":activityMap.get("coupon_amount").toString());
				BigDecimal giftAmount = new BigDecimal(activityMap.get("gift_amount").toString());
				BigDecimal faceValue = couponAmount.add(giftAmount);
				couponMap.put("face_value", faceValue);
				couponMap.put("balance", faceValue);
				couponMap.put("coupon_amount", new BigDecimal(0));
				couponMap.put("gift_amount",  activityMap.get("gift_amount"));
			}else if("17".equals(couponCode)){
				BigDecimal couponAmount = new BigDecimal(activityMap.get("coupon_amount")==null?"0":activityMap.get("coupon_amount").toString());
				BigDecimal giftAmount = new BigDecimal(activityMap.get("gift_amount").toString());
				BigDecimal faceValue = couponAmount.add(giftAmount);
				couponMap.put("face_value", faceValue);
				couponMap.put("balance", faceValue);
				couponMap.put("coupon_amount", new BigDecimal(0));
				couponMap.put("gift_amount",  activityMap.get("gift_amount"));
				couponMap.put("coupon_standard",activityMap.get("coupon_standard"));
				couponMap.put("back_rate",activityMap.get("back_rate"));
			}else {
				couponMap.put("face_value", activityMap.get("coupon_amount").toString());
				couponMap.put("balance", activityMap.get("coupon_amount").toString());
				couponMap.put("coupon_amount", new BigDecimal(0));
				couponMap.put("gift_amount",  new BigDecimal(0));
			}
			couponMap.put("coupon_code", couponCode);
			couponMap.put("cancel_verification_code", activityMap.get("cancel_verification_code").toString());
			couponMap.put("coupon_status", "1");  // 1 未使用
			couponMap.put("start_time", couponStartDate);
			couponMap.put("end_time", endDate);
			couponMap.put("token", token);
			couponMap.put("merchant_no", merchantNo);
			couponMap.put("activity_first", activityMap.get("activity_first").toString());
			couponMap.put("activity_entity_id", activityEntityId);

			String couponNo="B"+couponActivityService.getNext("coupon_no_seq").get("t").toString();
			couponMap.put("coupon_type",activityMap.get("coupon_type"));
			couponMap.put("coupon_no", couponNo)	;
			couponActivityService.addUserCoupon(couponMap);

			sensorsService.buyCouponDetail(couponMap,couponNo,merchantNo,couponStartDate,endDate,couponCode);

			return couponNo;
		} else {
			return "";
		}
	}

	public String  getTokenStr(String tokenKey, String couponCode){

		String token = "";
		String currentDate = DateUtils.getMessageTextTime();
		//注册返
		if ("1".equals(couponCode)) {
			token =currentDate+"Q"+tokenKey;
		}
		//签到返
		if ("2".equals(couponCode)) {
			token =currentDate+"Z"+tokenKey;
		}
		//充值返
		if ("3".equals(couponCode)||"6".equals(couponCode)||"8".equals(couponCode)||"9".equals(couponCode)||"15".equals(couponCode)||"16".equals(couponCode)||"17".equals(couponCode)) {
			token =tokenKey;  //此时为订单号
		}
		return  token;
	}

	private synchronized String getNext(String seqName, String defValue) {

		Map<String, Object> map = couponActivityService.getNext(seqName);

		BigInteger v = new BigInteger(defValue);
		Object t = map.get("t");
		if (null != t) {
			String src = t.toString();
			v = new BigInteger(src);
			v=v.add(new BigInteger("1"));
		}
		return v.toString();
	}

	/**
	 * 废弃券
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/abandonedCoupon")
	@SystemLog(description = "废弃活动券", operCode = "couponActivity.abandonedActivityCoupon")
	public Object abandonedCoupon(@RequestParam String id){
		Map<String,String> res = new HashMap<>();
		log.info("======废弃券");

		Map<String,Object> couponInfo = couponActivityService.couponInfo(id);
		if(couponInfo!=null){
			String couponStatus = StringUtil.filterNull(couponInfo.get("coupon_status"));
			if(couponStatus.equals("4")||couponStatus.equals("5")){
				res.put("msg","当前状态不能废弃");
			}else {
				int rest = couponActivityService.updateCouponStatus(id);
				if(rest>0){
					res.put("msg","废弃成功！");
				}else {
					res.put("msg","操作失败！");
				}
			}
		}
		return res;
	}

	/**
	 * 查询商户名称
	 * @param merchantNo
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@ResponseBody
	@RequestMapping("/merInfo")
	public Object merInfo(@RequestParam String merchantNo){
		MerchantInfo merchantInfo = merchantInfoService.selectByMerNo(merchantNo);
		return merchantInfo;
	}

	/**
	 * 查询商户名称
	 * @param couponCode
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@ResponseBody
	@RequestMapping("/actEntityList")
	public Object actEntityList(@RequestParam String couponCode){
		List<Map<String,Object>> actEntityList = couponActivityService.actEntityList(couponCode);
		return actEntityList;
	}

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @ResponseBody
    @RequestMapping("/zxIndustryDetail")
    public Result zxIndustryDetail(@RequestParam String activetiyCode){
	    Result result = new Result();
	    if(StringUtils.isBlank(activetiyCode)){
            result.setMsg("参数非法");
            return result;
        }
        CouponActivityInfo baseInfo = couponActivityService.selectInfoDetail(activetiyCode);
        List<ZxIndustryConfig> list = zxIndustryConfigService.selectList(activetiyCode);
        Map<String, Object> map = new HashMap<>();
        map.put("baseInfo", baseInfo);
        map.put("config",list);
        result.setStatus(true);
        result.setMsg("查询成功");
        result.setData(map);
        return result;
    }

    @ResponseBody
    @RequestMapping("/zxIndustryUpdate")
    @SystemLog(operCode = "func.zxIndustryUpdate", description = "自选行业收费修改")
    public Result zxIndustryUpdate(@RequestBody Map<String, Object> map){
        Result result = new Result();
        if(map == null){
            result.setMsg("参数非法");
            return result;
        }
		try {
			result = couponActivityService.updateZxIndustry(map);
		} catch (Exception e){
        	log.error("自选行业收费修改失败", e);
        	result = ResponseUtil.buildResult(e);
		}
        return result;
    }

	/**
	 * vip优享详情
	 * @param actId 活动ID
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/couponActivityVip")
	@ResponseBody
	public Object couponActivityVip(@RequestParam("actId") int actId) throws Exception{
		CouponActivityInfo couponActivity=null;
		List<Map> entity=null;
		Map<String, Object> map = new HashMap<>();
		try{
			couponActivity = couponActivityService.couponRecharge(actId);
			entity = couponActivityService.couponActivityVip(couponActivity.getActivetiyCode());
			map.put("info", couponActivity);
			map.put("entity", entity);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询详情失败！",e);
			map.put("status", false);
		}
		return map;
	}

	/**
	 * VIP优享开关控制修改
	 * @param id 数据ID
	 * @param col 更新字段
	 * @param val 更新值
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateVipStatus")
	@SystemLog(description = "VIP优享开关控制修改",operCode="couponActivity.updateVipStatus")
	@ResponseBody
	public Object updateVipStatus(@RequestParam("id") int id,@RequestParam("col") String col,@RequestParam("val") int val,@RequestParam("teamId") String teamId) throws Exception{
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.updateVipStatus(id,col,val,teamId);
			if(i == 1){
				/*CouponActivityEntity entry = couponActivityService.couponEntityView(id);
				if(CouponActivityAction.FLUSHACTIVETIYCODE.equals(entry.getActivetiyCode())){
					ClientInterface.flushActivityCache(4);
				}
				if(CouponActivityAction.FLUSHACTIVETIYCODE_BUYREWARD.equals(entry.getActivetiyCode())){
					ClientInterface.flushActivityCache(4);
				}*/
				map.put("status", true);
				map.put("msg", "修改成功！");
			}else{
				map.put("status", false);
				map.put("msg", "修改失败！");
				log.error("VIP优享开关控制修改失败{}！",col);
			}
		}catch(Exception e){
			log.error("VIP优享开关控制修改失败！",e);
			map.put("status", false);
			map.put("msg", "修改失败！");
		}
		return map;
	}

	/**
	 * VIP优享新增
	 * @return
	 */
	@RequestMapping("/addActivityVip")
	@SystemLog(description = "VIP优享新增", operCode = "couponActivity.addActivityVip")
	public @ResponseBody Object addActivityVip(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			Map info = jsonObject.getObject("info", Map.class);
			// 数据安全判断
			if (StringUtil.isBlank(info.get("name"))
					|| StringUtil.isBlank(info.get("time")) || StringUtil.isBlank(info.get("original_price"))
					|| StringUtil.isBlank(info.get("sort_num"))) {
				msg.put("status", false);
				msg.put("msg", "非法参数,请重新输入");
				return msg;
			}
			info.put("discount_price",StringUtil.isEmpty(info.get("discount_price").toString())?null:info.get("discount_price"));
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			info.put("record_creator",principal.getUsername());
			if (couponActivityService.addActivityVip(info)>0) {
				msg.put("status", true);
				msg.put("msg", "VIP优享新增成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "VIP优享新增失败");
			}
		} catch (Exception e) {
			log.error("VIP优享新增失败！", e);
			msg.put("status", false);
			msg.put("msg", "VIP优享新增失败");
		}
		return msg;
	}

	/**
	 * VIP优享修改
	 * @return
	 */
	@RequestMapping("/updateActivityVip")
	@SystemLog(description = "VIP优享修改", operCode = "couponActivity.updateActivityVip")
	public @ResponseBody Object updateActivityVip(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			Map info = jsonObject.getObject("info", Map.class);
			// 数据安全判断
			if (StringUtil.isBlank(info.get("name"))
					|| StringUtil.isBlank(info.get("time")) || StringUtil.isBlank(info.get("original_price"))
					|| StringUtil.isBlank(info.get("sort_num"))) {
				msg.put("status", false);
				msg.put("msg", "非法参数,请重新输入");
				return msg;
			}
			info.put("discount_price",StringUtil.isBlank(info.get("discount_price"))?null:info.get("discount_price"));
			couponActivityService.updateActivityVip(info);
			msg.put("status", true);
			msg.put("msg", "VIP优享修改成功");
		} catch (Exception e) {
			log.error("VIP优享修改失败！", e);
			msg.put("status", false);
			msg.put("msg", "VIP优享修改失败");
		}
		return msg;
	}


	/**
	 * 发行券批量新增模板下载
	 */
	@RequestMapping("/couponImportTemplate")
	public String couponImportTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"couponImportTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath,"发行券批量新增模板.xlsx");
		return null;
	}

	/**
	 * 批量导入新增券
	 */
	@RequestMapping(value="/addCouponImport")
	@ResponseBody
	@SystemLog(description = "批量导入新增券", operCode = "couponActivity.addCouponImport")
	public Map<String, Object> addCouponImport(@RequestParam("file") MultipartFile file,HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "导入文件格式错误!");
					return msg;
				}
			}
			msg = couponImportService.addCouponImport(request.getParameter("info"),file);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "导入失败!");
			log.error("导入失败!",e);
		}
		return msg;
	}




	@RequestMapping(value = "/vipSysEntityAdd")
	@SystemLog(description = "会员系统-物品信息新增",operCode="couponActivity.vipSysEntityAdd")
	@ResponseBody
	@DataSource(value = Constants.DATA_SOURCE_MASTER)
	public Object vipSysEntityAdd(@RequestParam("info") String info){
		CouponActivityEntity entity = JSON.parseObject(info, CouponActivityEntity.class);
		Map<String,Object> msg = new HashMap<>();
		int result = couponActivityService.saveVipTicket(entity,msg);
		if(result==1){
			msg.put("msg","新增成功");
			msg.put("status",true);
		}else{
			msg.put("msg","新增失败");
			msg.put("status",false);
		}
		return msg;
	}



	@RequestMapping(value = "/updateVipTicket")
	@SystemLog(description = "会员系统-物品信息修改",operCode="couponActivity.updateVipTicket")
	@ResponseBody
	@DataSource(value = Constants.DATA_SOURCE_MASTER)
	public Object vipSysEntitySave(@RequestParam("info") String info){
		CouponActivityEntity entity = JSON.parseObject(info, CouponActivityEntity.class);
		Map<String,Object> msg = new HashMap<>();
		int result = couponActivityService.saveVipTicket(entity,msg);
		if(result==1){
			msg.put("msg","保存成功");
			msg.put("status",true);
		}else{
			msg.put("msg","保存失败");
			msg.put("status",false);
		}
		return msg;
	}


	@RequestMapping(value = "/delVipEntity")
	@SystemLog(description = "会员系统-物品信息删除",operCode="couponActivity.delVipEntity")
	@ResponseBody
	@DataSource(value = Constants.DATA_SOURCE_MASTER)
	public Object delVipEntity(@RequestParam("id") Integer id){
		Map<String,Object> msg = new HashMap<>();
		int result = couponActivityService.delVipEntity(id);
		if(result==1){
			msg.put("msg","删除成功");
			msg.put("status",true);
		}else{
			msg.put("msg","删除失败");
			msg.put("status",false);
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/vipSysEntityDetail")
	@ResponseBody
	public Object vipSysEntityDetail(@RequestParam("entityId") int entityId) throws Exception{
		CouponActivityEntity entity=null;
		Map<String, Object> map = new HashMap<>();
		try{
			entity = couponActivityService.couponEntityView(entityId);
			List<CouponActivityTime> couponTime=couponActivityService.getCouponActivityTime(entityId);
			map.put("entity", entity);
			map.put("couponTime", couponTime);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询详情失败！",e);
			map.put("status", false);
		}
		return map;
	}



	@RequestMapping(value = "/vipSysUpdate")
	@SystemLog(description = "会员系统-保存",operCode="couponActivity.vipSysUpdate")
	@ResponseBody
	@DataSource(value = Constants.DATA_SOURCE_MASTER)
	public Object vipSysUpdate(@RequestParam("info") String param){
		CouponActivityInfo couponActivity = JSON.parseObject(param,CouponActivityInfo.class);
		Map<String, Object> map = new HashMap<>();
		try{
			int i = couponActivityService.saveCouponActivity(couponActivity);
			if(i == 1){
				map.put("status", true);
				map.put("msg", "保存成功！");
			}else{
				map.put("status", false);
				map.put("msg", "保存失败！");
				log.error("保存失败！");
			}
		}catch(Exception e){
			log.error("保存失败！",e);
			map.put("status", false);
			map.put("msg", "保存失败！");
		}
		return map;
	}





	/**
	 * 会员活动详情
	 * @param actId 活动ID
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/vipSysDetail")
	@ResponseBody
	public Object vipSysDetail(@RequestParam("actId") int actId) throws Exception{
		CouponActivityInfo couponActivity=null;
		List<CouponActivityEntity> entity=null;
		Map<String, Object> map = new HashMap<>();
		try{
			couponActivity = couponActivityService.couponRecharge(actId);
			entity = couponActivityService.couponRechargeEntry(couponActivity.getActivetiyCode());
			map.put("info", couponActivity);
			map.put("entity", entity);
			map.put("status", true);
		}catch(Exception e){
			log.error("查询详情失败！",e);
			map.put("status", false);
		}
		return map;
	}
}
