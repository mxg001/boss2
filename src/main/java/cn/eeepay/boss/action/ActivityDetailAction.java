package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ActivityDetailBackstageService;
import cn.eeepay.framework.service.ActivityDetailService;
import cn.eeepay.framework.service.XhlfActivityOrderJobService;
import cn.eeepay.framework.service.unTransactionalImpl.HappyReturnJobServiceImpl;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/activityDetail")
public class ActivityDetailAction {
	private static final Logger log = LoggerFactory.getLogger(ActivityDetailAction.class);

    @Resource
    private HappyReturnJobServiceImpl happyReturnJobService;

	@Resource
	private ActivityDetailService activityDetailService;

	@Resource
	private ActivityDetailBackstageService activityDetailBackstageService;

	@Resource
	private XhlfActivityOrderJobService xhlfActivityOrderJobService;

	@Resource
	private SysDictDao sysDictDao;

	// 业务活动明细
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectActivityDetail.do")
	@ResponseBody
	public Object selectActivityDetail(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page) {
		Map<String, Object> msg = new HashMap<>();
		ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
		try {
			String orderNoStr = activityDetail.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				activityDetail.setActiveOrder(newOrderNo);
			}
			activityDetailService.selectActivityDetail(page,activityDetail);
			int accountCheckTotal=activityDetailBackstageService.countActivityDetailBackstage("1");
			msg.put("page",page);
			msg.put("accountCheckTotal",accountCheckTotal);
			msg.put("status",true);
		} catch (Exception e) {
			log.error("初始化失败----", e);
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 
	 * @author tans
	 * @date 2017年3月28日 下午5:46:41
	 * @param param
	 * @param page
	 * @param response
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportExcel.do")
	@SystemLog(description = "导出业务活动记录",operCode="activity.exportExcel")
	public void exportExcel(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
		 HttpServletResponse response){
		try {
			ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
			String orderNoStr = ad.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				ad.setActiveOrder(newOrderNo);
			}
			activityDetailService.exportExcel(page,ad,response);
		} catch (Exception e) {
			log.error("导出业务活动记录失败");
		    e.printStackTrace();
	    }
	}
	
	/**
	 * 回盘文件模板下载
	 * @author tans
	 * @date 2017年3月28日 下午5:46:49
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/downloadTemplate")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getServletContext().getRealPath("/")+File.separator+"template"+File.separator+"activityDiscountTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath,"欢乐送补贴扣回回盘模板.xlsx");
		return null;
	}
	
	/**
	 * 回盘导入
	 * 改写activity_detail里面的扣回状态
	 * @author tans
	 * @date 2017年3月28日 下午7:43:59
	 * @return
	 */
	@RequestMapping(value="/importDiscount")
	@ResponseBody
	@SystemLog(description = "欢乐送商户查询回盘导入",operCode="activity.importDiscount")
	public Map<String, Object> importDiscount(@RequestParam("file") MultipartFile file){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "回盘导入文件格式错误");
					return msg;
				}
			}
			msg = activityDetailService.importDiscount(file);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "回盘导入失败");
			log.error("回盘导入失败",e);
		}
		return msg;
	}
	
	/**
	 * 欢乐送业务批量核算
	 * @author Ivan
	 * @date   2017/03/28
	 * @return
	 */
	@RequestMapping(value = "/happySendActivityAdjust")
	@SystemLog(description = "欢乐送商户查询核算",operCode="activity.happySendActivityAdjust")
	public @ResponseBody Map<String, Object> happySendActivityAdjust(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
			@RequestParam("status") String status) {

		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",","','")+"'";
			ad.setActiveOrder(newOrderNo);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg=activityDetailService.updateAdjust(ad,page,status);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","核算操作异常!");
			msg.put("status", false);
		}
		return msg;

	}
	
	/**
	 * 欢乐返活动查询
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectHappyBackDetail.do")
	@ResponseBody
	public Object selectHappyBackDetail(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page) {
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("status", false);
		msg.put("msg", "查询失败");
		try {
			ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
			String orderNoStr = activityDetail.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				activityDetail.setActiveOrder(newOrderNo);
			}
			activityDetailService.selectHappyBackDetail(page,activityDetail);
			Map<String, Object> totalData = activityDetailService.selectHappyBackTotalAmount(activityDetail);
			int accountCheckTotal=activityDetailBackstageService.countActivityDetailBackstage("2");
			int liquidationTotal=activityDetailBackstageService.countActivityDetailBackstage("3");
			int rewardIsBookedTotal=activityDetailBackstageService.countActivityDetailBackstage("4");
			msg.put("accountCheckTotal", accountCheckTotal);//财务核算
			msg.put("liquidationTotal", liquidationTotal);//清算
			msg.put("rewardIsBookedTotal", rewardIsBookedTotal);//奖励入账

			msg.put("status", true);
			msg.put("msg", "查询成功");
			msg.put("page", page);
			msg.put("totalData", totalData);
		} catch (Exception e) {
			log.error("查询失败----", e);
		}
		return msg;
	}


	/**
	 * 获取欢乐返商户详情
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectHappyBackDetailInfo")
	@ResponseBody
	public Map<String, Object> selectHappyBackDetailInfo(String ids) throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {

			ActivityDetail activityDetail =activityDetailService.selectHappyBackDetailById(Integer.parseInt(ids));
			if(activityDetail!=null){
				List<CashBackDetail> list =activityDetailService.selectAgentReturnCashDetailAll(activityDetail.getId(),1);
				List<CashBackDetail> acbfpList =activityDetailService.selectAgentReturnCashDetailAll(activityDetail.getId(),2);
				List<CashBackDetail> acbnfbList =activityDetailService.selectAgentReturnCashDetailAll(activityDetail.getId(),3);
				msg.put("acbList", list);
				msg.put("acbfpList", acbfpList);
				msg.put("acbnfbList", acbnfbList);
				msg.put("mbp", activityDetail);
				msg.put("status", true);
				msg.put("msg", "查询成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "查询失败");
			}

		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("ids----", e);
		}
		return msg;
	}

	/**
	 * 新欢乐送商户奖励查询
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectHappySendOrderDetail.do")
	@ResponseBody
	public Object selectHappySendOrderDetail(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<HappySendNewOrder> page) {
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("status", false);
		msg.put("msg", "查询失败");

		try {
			HappySendNewOrder happySendNewOrder = JSON.parseObject(param,HappySendNewOrder.class);

			String orderNoStr = happySendNewOrder.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				//newOrderNo=""+orderNoStr.replaceAll(",", "','")+"";
				List<String> orderNoList= StringUtil.strToList(orderNoStr,",");
				happySendNewOrder.setOrderNoList(orderNoList);
				//happySendNewOrder.setActiveOrder(newOrderNo);
			}
			activityDetailService.selectHappySendOrderDetail(page,happySendNewOrder);
			Map<String, Object> totalData = activityDetailService.selectHappySendOrderTotalAmount(happySendNewOrder);
			msg.put("status", true);
			msg.put("msg", "查询成功");
			msg.put("page", page);
			msg.put("totalData", totalData);
		} catch (Exception e) {
			log.error("查询失败----", e);
		}
		return msg;
	}

	/**
	 * 获取欢乐返商户详情
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectHappyTixianSwitch")
	@ResponseBody
	public Map<String, Object> selectHappyTixianSwitch() throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			String happyTixianSwitch = activityDetailService.selectHappyTixianSwitch();
			if(happyTixianSwitch!=null){
				msg.put("happyTixianSwitch", happyTixianSwitch);
			}else{
				msg.put("happyTixianSwitch", "0");
			}
			msg.put("status", true);
			msg.put("msg", "查询成功");
			log.info("\nhappyTixianSwitch="+happyTixianSwitch);

		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("ids----", e);
		}
		return msg;
	}
	
	//导出欢乐返活动明细
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportHappyBack.do")
	@SystemLog(description = "导出欢乐返活动明细",operCode="activity.exportHappyBack")
	public String exportHappyBack(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,HttpServletResponse response){
        try {
        	ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
        	String orderNoStr = activityDetail.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				activityDetail.setActiveOrder(newOrderNo);
			}
        	activityDetailService.exportHappyBack(page,activityDetail,response);
        } catch (Exception e) {
        	log.error("导出欢乐返失败",e);
        	return "error";
        }
        return null;
    }

	//导出欢乐返活动明细
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportHappySendOrder.do")
//	@SystemLog(description = "新欢乐送商户奖励导出",operCode="activity.exportHappySendOrder")
	public String exportHappySendOrder(@RequestParam("baseInfo")String param,HttpServletResponse response){
		try {
			HappySendNewOrder happySendNewOrder = JSON.parseObject(param,HappySendNewOrder.class);
			String orderNoStr = happySendNewOrder.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){

				List<String> orderNoList= StringUtil.strToList(orderNoStr,",");
				happySendNewOrder.setOrderNoList(orderNoList);
				//newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				//happySendNewOrder.setActiveOrder(newOrderNo);
			}
			activityDetailService.exportHappySendOrder(happySendNewOrder,response);
		} catch (Exception e) {
			log.error("导出欢乐返失败",e);
			return "error";
		}
		return null;
	}
	
	/**
	 * 欢乐返清算核算
	 * @author tans
	 * @date 2017年6月27日 上午9:51:02
	 * @return
	 */
	@RequestMapping(value="/liquidation.do")
	@ResponseBody
	@SystemLog(description = "欢乐返清算核算",operCode="activity.liquidation")
	public Map<String, Object> liquidation(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
			@RequestParam String liquidationStatus){

		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
			ad.setActiveOrder(newOrderNo);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			 msg = activityDetailService.updateLiquidation(ad,page,liquidationStatus);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","欢乐返清算核算操作异常!");
			msg.put("status", false);
		}
		return msg;
	}
	
	/**
	 * 欢乐返财务核算
	 * @author tans
	 * @date 2017年6月27日 上午9:51:02
	 * @param
	 * @return
	 */
	@RequestMapping(value="/accountCheck.do")
	@ResponseBody
	@SystemLog(description = "欢乐返财务核算",operCode="activity.accountCheck")
	public Map<String, Object> accountCheck(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
			@RequestParam String accountCheckStatus){
		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
			ad.setActiveOrder(newOrderNo);
		}

		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg = activityDetailService.updateAccountCheck(ad,page,accountCheckStatus);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","欢乐返财务核算操作异常!");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 批量奖励入账
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/rewardIsBooked")
	@ResponseBody
	@SystemLog(description = "批量奖励入账",operCode="activity.rewardIsBooked")
	public Map<String, Object> rewardIsBooked(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page){
		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
			ad.setActiveOrder(newOrderNo);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg = activityDetailService.rewardIsBooked(ad,page);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","批量奖励入账操作异常!");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 奖励入账
	 * @param
	 * @return
	 */
	@RequestMapping(value="/oneRewardIsBooked")
	@ResponseBody
	@SystemLog(description = "奖励入账",operCode="activity.oneRewardIsBooked")
	public Map<String, Object> oneRewardIsBooked(@RequestParam Integer id){
		Map<String, Object> msg=null;
		try {
			msg=activityDetailService.oneRewardIsBooked(id);
		}catch (Exception e){
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 欢乐返批量奖励入账
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/joyToAccount")
	@ResponseBody
	@SystemLog(description = "欢乐返批量奖励入账",operCode="activity.joyToAccount")
	public Map<String, Object> joyToAccount(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page){
		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
			ad.setActiveOrder(newOrderNo);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg = activityDetailService.joyToAccount(ad,page);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","欢乐返批量奖励入账异常!");
			msg.put("status", false);
		}
		return msg;
	}

	@RequestMapping("/newHappyBackCount")
	@ResponseBody
	public Result newHappyBackCount(@RequestBody NewHappyBackActivityQo qo) {
		Result result = new Result();
		result.setStatus(true);
		try {
			NewHappyBackActivityResult data = activityDetailService.newHappyBackCount(qo);
			result.setData(data);
		} catch (Exception e) {
			result.setStatus(false);
			log.error(e.getMessage(),e);
		}
		return result;
	}

	@RequestMapping("/newHappyBackQuery")
	@ResponseBody
	public Result newHappyBackQuery(@RequestBody NewHappyBackActivityQo qo) {
		Result result = new Result();
		result.setStatus(true);
		try {
			NewHappyBackActivityResult data = activityDetailService.newHappyBackQuery(qo);
			result.setData(data);
		} catch (Exception e) {
			result.setStatus(false);
			log.error(e.getMessage(),e);
		}
		return result;
	}

	@RequestMapping("/agentAwardDetail")
	@ResponseBody
	public Result agentAwardDetail(Long id) {
		Result result = new Result();
		result.setStatus(true);
		try {
			List<AgentAwardDetailVo> data = activityDetailService.agentAwardDetail(id);
			result.setData(data);
		} catch (Exception e) {
			result.setStatus(false);
			log.error(e.getMessage(),e);
		}
		return result;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/exportHappySendNew")
	@ResponseBody
//	@SystemLog(description = "新欢乐送活动查询导出",operCode="activity.exportHappySendNew")
	public void exportHappySendNew(@RequestParam("baseInfo")String param,HttpServletResponse response){
		try {
			NewHappyBackActivityQo qo = JSON.parseObject(param,NewHappyBackActivityQo.class);
			List<NewHappyBackActivityVo> list = activityDetailService.newHappyBackQuery(qo).getList();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fileName = null;
			if("3".equals(qo.getSubType())){
				fileName = "超级返活动"+sdf.format(new Date())+".xls" ;
			}else{
				fileName = "新欢乐送活动"+sdf.format(new Date())+".xls" ;
			}
			String fileNameFormat = new String(fileName.getBytes("utf-8"),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
			Map<String, String> agentTransTotalTypeMap = new HashMap<>();
			List<SysDict> selectListByKey = sysDictDao.selectListByKey("XHLS_TRANS_TOTAL_TYPES");
			for (SysDict sysDict : selectListByKey) {
				agentTransTotalTypeMap.put(sysDict.getSysValue(), sysDict.getSysName());
			}
			Map<String, String> currentCycleMap=new HashMap<String, String>();
			currentCycleMap.put("1","第一次考核奖励");
			currentCycleMap.put("1.1","第一次奖励考核子考核");
			currentCycleMap.put("2","第二次考核奖励");
			currentCycleMap.put("3","第三次考核奖励");
			currentCycleMap.put("4","第四次考核奖励");

			Map<String, String> currentTargetStatusMap=new HashMap<String, String>();
			currentTargetStatusMap.put("0","未开始");
			currentTargetStatusMap.put("1","考核中");
			currentTargetStatusMap.put("2","已达标");
			currentTargetStatusMap.put("3","未达标");
			currentTargetStatusMap.put("4","未达标返代理商");
			currentTargetStatusMap.put("5","已达标奖励商户");

			Map<String, String> activityTargetStatusMap=new HashMap<String, String>();
			activityTargetStatusMap.put("0","考核中");
			activityTargetStatusMap.put("1","已达标");
			activityTargetStatusMap.put("2","未达标");

			for(NewHappyBackActivityVo item: list){
				Map<String,String>  map = new HashMap<>();
				map.put("activeOrder", item.getActiveOrder());
				map.put("activeTime", item.getActiveTime() == null ? "" : sdfTime.format(item.getActiveTime()));
				map.put("targetAmount", item.getTargetAmount() == null ? "累计交易≥"+"0.00" : "累计交易≥"+item.getTargetAmount().toString());
				map.put("rewardAmount", item.getRewardAmount() == null ? "0.00" : item.getRewardAmount().toString());

				map.put("agentTransTotalType", agentTransTotalTypeMap.get(item.getAgentTransTotalType()+""));
				map.put("currentCycle", currentCycleMap.get(item.getCurrentCycle()));
				map.put("rewardStartTime", item.getRewardStartTime() == null ? "" : sdfTime.format(item.getRewardStartTime()));
				map.put("rewardEndTime", item.getRewardEndTime() == null ? "" : sdfTime.format(item.getRewardEndTime()));

				map.put("currentTargetStatus", currentTargetStatusMap.get(item.getCurrentTargetStatus()));
				map.put("currentTargetTime", item.getCurrentTargetTime() == null ? "" : sdfTime.format(item.getCurrentTargetTime()));
				map.put("rewardAccountStatus", "0".equals(item.getRewardAccountStatus())?"未入账":"已入账");
				map.put("rewardAccountTime", item.getRewardAccountTime() == null ? "" : sdfTime.format(item.getRewardAccountTime()));

				map.put("activityTargetStatus", activityTargetStatusMap.get(item.getActivityTargetStatus()));
				map.put("activityTargetTime", item.getActivityTargetTime() == null ? "" : sdfTime.format(item.getActivityTargetTime()));
				map.put("activityTypeNo", item.getActivityTypeNo());
				map.put("merchantNo", item.getMerchantNo());
				map.put("teamName", item.getTeamName());
				map.put("teamEntryName", item.getTeamEntryName());
				map.put("hardId", item.getHardId()==null?"": item.getHardId().toString());
				map.put("agentName", item.getAgentName());
				map.put("agentNo", item.getAgentNo());
				map.put("oneAgentName", item.getOneAgentName());
				map.put("oneAgentNo", item.getOneAgentNo());
				data.add(map);
			}
			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[]{"activeOrder","activeTime","targetAmount"
					,"agentTransTotalType","rewardAmount","currentCycle","rewardStartTime","rewardEndTime","currentTargetStatus","currentTargetTime",
					"rewardAccountStatus","rewardAccountTime","activityTargetStatus","activityTargetTime",
					"activityTypeNo","merchantNo","teamName","teamEntryName","hardId",
					"agentName", "agentNo","oneAgentName","oneAgentNo"};
			String[] colsName = new String[]{"激活流水号","激活时间","达标条件(元)","累计交易量统计方式","达标奖励金额(元)","考核周期","考核开始日期",
					"考核结束日期","当前考核达标状态","当前考核达标日期","奖励入账状态","奖励入账日期","活动达标状态", "活动达标日期",
					"欢乐返子类型编号","所属商户编号","所属组织","所属子组织","硬件产品ID",
					"所属代理商名称","所属代理商编号","一级代理商名称","一级代理商编号"};

			OutputStream ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
			ouputStream.close();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}


	/**
	 * 新欢乐送代理商奖励批量入账
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/happySendNewAgentRewardIsBooked")
	@ResponseBody
	@SystemLog(description = "新欢乐送代理商奖励批量入账",operCode="activity.happySendNewAgentRewardIsBooked")
	public Map<String, Object> happySendNewAgentRewardIsBooked(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<XhlfActivityOrder> page){
		XhlfActivityOrder XhlfActivityOrder = JSON.parseObject(param,XhlfActivityOrder.class);
		log.info(XhlfActivityOrder.toString());
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			if(StringUtil.isNotBlank(XhlfActivityOrder.getCheckIds())){
				List<XhlfActivityOrder> orderLists = activityDetailService.getXhlfAgentOrderLists(XhlfActivityOrder.getCheckIds());
				if(orderLists != null && !orderLists.isEmpty()){
					int sucNum = xhlfActivityOrderJobService.accountList(orderLists);
					msg.put("msg","成功入账条数为:"+sucNum);
				}else{
					msg.put("msg","没有符合入账的记录");
				}
			}else{
				msg.put("msg","请选择需要奖励入账的记录");
			}
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","新欢乐送代理商奖励批量入账操作异常!");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 新欢乐送商户奖励批量入账
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/happySendNewMerRewardIsBooked")
	@ResponseBody
	@SystemLog(description = "新欢乐送商户奖励批量入账",operCode="activity.happySendNewMerRewardIsBooked")
	public Map<String, Object> happySendNewMerRewardIsBooked(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<HappySendNewOrder> page){
		HappySendNewOrder happySendNewOrder = JSON.parseObject(param,HappySendNewOrder.class);
		log.info(happySendNewOrder.toString());
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			if(StringUtil.isNotBlank(happySendNewOrder.getCheckIds()) ){
				List<XhlfActivityMerchantOrder> orderLists=activityDetailService.getXhlfMerOrderLists(happySendNewOrder.getCheckIds());
				if(orderLists != null && !orderLists.isEmpty()){
					int sucNum=xhlfActivityOrderJobService.accountMerchantList(orderLists);
					msg.put("msg","成功入账条数为:"+sucNum);
				}else{
					msg.put("msg","没有符合入账的记录");
				}
			}else{
				msg.put("msg","请选择需要奖励入账的记录");
			}
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","新欢乐送商户奖励批量入账操作异常!");
			msg.put("status", false);
		}
		return msg;
	}




}
