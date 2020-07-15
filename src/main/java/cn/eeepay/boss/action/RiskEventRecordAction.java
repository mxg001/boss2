package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.RiskEventRecordService;
import cn.eeepay.framework.service.RiskRollService;
import cn.eeepay.framework.service.RiskRulesService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value="/riskEventRecordAction")
public class RiskEventRecordAction {
	
	private static final Logger log = LoggerFactory.getLogger(RiskEventRecordAction.class);

	@Resource
	private RiskRulesService riskRulesService;
	@Resource
	private RiskEventRecordService riskEventRecordService;
	
	@Resource
	private RiskRollService riskRollService;
	@Resource
	private SysDictService sysDictService;
	
	/**
	 * 风控事件记录查询
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/queryEventRecordList")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<RiskEventRecord> page,@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			RiskEventRecord riskEventRecord=JSON.parseObject(param,RiskEventRecord.class);
			riskEventRecordService.queryEventRecordList(page, riskEventRecord);
			List<RiskEventRecord> riskEventRecordList = page.getResult();
			for (RiskEventRecord record : riskEventRecordList) {
				RiskRules riskRules = riskRulesService.selectByRulesNo(record.getRulesNo());
				if(StringUtils.isBlank(riskRules.getRulesValues())){
					continue;
				}
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> valuesMap = om.readValue(riskRules.getRulesValues(), Map.class);
				String rulesEngine = riskRules.getRulesEngine();
				for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
					log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
					String key = "\\{"+entry.getKey()+"\\}";
					String val = String.valueOf(entry.getValue());
					rulesEngine = rulesEngine.replaceAll(key, val);
				}
				record.setRulesEngine(rulesEngine);
			}
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 风控事件记录处理
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/updateHandleStatus")
	@ResponseBody
	public Object updateHandleStatus(@RequestBody String param) {
			log.info("风控事件记录处理参数： " + param);
			String id = "";
			String handleResults = "";
			String handleRemark = "";
			Map<String, Object> maps = new HashMap<String, Object>();
			JSONObject json = JSON.parseObject(param);
				try {
					id = json.getString("id");
					handleResults = json.getString("handleResults");
					handleRemark = json.getString("handleRemark");
					// 更新处理状态
					final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
							.getPrincipal();
					RiskEventRecord riskEventRecord = new RiskEventRecord();
					riskEventRecord.setId(Integer.valueOf(id));
					//riskEventRecord.setHandlePersonId(principal.getId().toString());
					riskEventRecord.setHandlePerson(principal.getRealName());
					riskEventRecord.setHandleResults(Integer.valueOf(handleResults));
					riskEventRecord.setHandleTime(new Date());
					riskEventRecord.setHandleRemark(handleRemark);
					riskEventRecordService.updateHandleStatus(riskEventRecord);
				} catch (Exception e) {
					log.error("风控事件记录处理出错------", e);
					maps.put("bols", false);
					maps.put("msg", "风控事件记录处理异常");
				}
			
			maps.put("bols", true);
			maps.put("msg", "完成处理！");
			return maps;
		}
	
	/**
	 * 风控事件记录详情
	 * @param ids
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/findRiskEventRecordById")
	@ResponseBody
	public Object findRiskEventRecordById(@RequestParam("ids") String ids) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			int id = JSON.parseObject(ids, Integer.class);
			log.info(id + "========");
			RiskEventRecord riskEventRecord = riskEventRecordService.findRiskEventRecordById(id);
			if (riskEventRecord == null) {
				jsonMap.put("msg", "查询失败~~~~~");
				jsonMap.put("bols", false);
			} else {
				RiskRules riskRules = riskRulesService.selectByRulesNo(riskEventRecord.getRulesNo());
				if (!StringUtils.isBlank(riskRules.getRulesValues())) {

					ObjectMapper om = new ObjectMapper();
					Map<String, Object> valuesMap = om.readValue(riskRules.getRulesValues(), Map.class);
					String rulesEngine = riskRules.getRulesEngine();
					for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
						log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
						String key = "\\{" + entry.getKey() + "\\}";
						String val = String.valueOf(entry.getValue());
						rulesEngine = rulesEngine.replaceAll(key, val);
					}
					riskEventRecord.setRulesEngine(rulesEngine);
				}
				jsonMap.put("result", riskEventRecord);
				jsonMap.put("bols", true);
				//查询更多时间
				List<BlackOperLog> myDate = riskRollService.selectMoreTime2(String.valueOf(id));
				jsonMap.put("myDate", myDate);
			}
		} catch (Exception e) {
			log.error("风控事件记录详情报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 修改名单状态
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateRollStatus")
	@ResponseBody
	@SystemLog(description = "名单状态开关",operCode="blackList.switch")
	public Object updateRollStatus(@RequestBody RiskEventRecord rer)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		RiskRoll rr = new RiskRoll();
		rr = riskRollService.findRiskRollByRollNoAndRollType(rer.getRollNo(), rer.getRulesInstruction());
		try {
			if(rr != null){
				rr.setRollStatus(rer.getRollStatus());
				log.info("事件记录 黑名单状态："+ rer.getRollStatus());
			}
			if(rr.getRollStatus()==null){
				jsonMap.put("msg", "信息不对~~~~");
			}else{
				int i = riskRollService.updateRollStatus(rr);
				if(i>0){
					jsonMap.put("bols", true);
				}else{
					jsonMap.put("bols", false);
				}
			}
		} catch (Exception e) {
			log.error("修改名单状态报错!!!",e);
			jsonMap.put("bols", false);
			String str=e.getMessage();
			if(e.getMessage()==null){
				jsonMap.put("msg", "修改名单状态异常");
				return jsonMap;
			}
			if(str.contains("\r\n")||str.contains("\n"))
				jsonMap.put("msg", "修改名单状态异常");
			else
				jsonMap.put("msg", str);
		}
		return jsonMap;
	}

	@RequestMapping(value="/getRollInfo")
	@ResponseBody
	@SystemLog(description = "名单状态开关",operCode="blackList.switch")
	public Object getRollInfo(@RequestBody RiskEventRecord rer)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		RiskRoll rr = riskRollService.findRiskRollByRollNoAndRollType(rer.getRollNo(), rer.getRulesInstruction());
		try {
			if(rr != null){
				rr.setRollStatus(rer.getRollStatus());
				jsonMap.put("bols", true);
				jsonMap.put("rr", rr);
			}else{
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("修改名单状态报错!!!",e);
		}
		return jsonMap;
	}

	/**
	 * 风控事件记录导出
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/eventRecordExport.do")
	@ResponseBody
	public void exportInfo(HttpServletResponse response,HttpServletRequest request,@RequestParam("info") String param) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "风控事件记录查询导出"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);

		if(StringUtils.isNotBlank(param)){
			param = URLDecoder.decode(param, "utf-8");
		}
		RiskEventRecord rr = JSON.parseObject(param,RiskEventRecord.class);
		List<RiskEventRecord> list = riskEventRecordService.riskEventRecordExport(rr);

		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		Map<String, String> rulesInstructionMap=sysDictService.selectMapByKey("RULES_INSTRUCTIONS");//风控规则指令

		for (RiskEventRecord record : list) {
			RiskRules riskRules = riskRulesService.selectByRulesNo(record.getRulesNo());
			if(StringUtils.isBlank(riskRules.getRulesValues())){
				continue;
			}
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> valuesMap = om.readValue(riskRules.getRulesValues(), Map.class);
			String rulesEngine = riskRules.getRulesEngine();
			for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
				log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
				String key = "\\{"+entry.getKey()+"\\}";
				String val = String.valueOf(entry.getValue());
				rulesEngine = rulesEngine.replaceAll(key, val);
			}
			record.setRulesEngine(rulesEngine);
		}
		
		
		if(list==null||list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
			maps.put("createTime", null);
			maps.put("rollNo", null);
			maps.put("merchantName", null);
			maps.put("agentNo", null);
			maps.put("rulesNo", null);
			maps.put("rulesEngine", null);
			maps.put("rulesInstruction", null);
			maps.put("orderNo",null);
			maps.put("gnb",null);
			maps.put("lbs",null);
			maps.put("ip",null);
			maps.put("ipArea",null);
			maps.put("decision",null);
			maps.put("handleStatus", null);
			maps.put("handleResults", null);
			maps.put("handleTime", null);
			maps.put("handlePerson", null);
			maps.put("handleRemark", null);
			maps.put("rollStatus", null);
			data.add(maps);
		}else{
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> maps = new HashMap<String, String>();
				
				//操作状态
				String handleStatus = "";
				if(list.get(i).getHandleStatus() != null){
					if(list.get(i).getHandleStatus().equals(0)){
						handleStatus = "未处理";
					}else if(list.get(i).getHandleStatus().equals(1)){
						handleStatus = "已处理";
					}	
				}
				
				//操作结果
				String handleResults = "";
				if(list.get(i).getHandleResults() != null){
					if(list.get(i).getHandleResults().equals(1)){
						handleResults = "安全";
					}else if(list.get(i).getHandleResults().equals(2)){
						handleResults = "可疑";
					}else if(list.get(i).getHandleResults().equals(3)){
						handleResults = "风险";
					}
				}
				
				
				//黑名单状态
				String rollStatus = "无";//1：开启 0：关闭'
				if(list.get(i).getRollStatus() != null){
					if(list.get(i).getRollStatus().equals(0)){
						rollStatus = "关闭";
					}else if(list.get(i).getRollStatus().equals(1)){
						rollStatus = "开启";
					}
				}
				maps.put("id",list.get(i).getId().toString());
				maps.put("createTime", DateUtil.getLongFormatDate(list.get(i).getCreateTime()));
				maps.put("rollNo",list.get(i).getRollNo());
				maps.put("merchantName",list.get(i).getMerchantName());
				maps.put("agentNo",list.get(i).getAgentNo());
				maps.put("rulesNo",list.get(i).getRulesNo().toString());
				maps.put("rulesEngine",list.get(i).getRulesEngine());
				maps.put("rulesInstruction",StringUtils.trimToEmpty(rulesInstructionMap.get(list.get(i).getRulesInstruction()+"")));
				maps.put("orderNo",list.get(i).getOrderNo()!=null?list.get(i).getOrderNo():"");
				maps.put("gnb",list.get(i).getGnb()!=null?list.get(i).getGnb():"");
				maps.put("lbs",list.get(i).getLbs()!=null?list.get(i).getLbs():"");
				maps.put("ip",list.get(i).getIp()!=null?list.get(i).getIp():"");
				maps.put("ipArea",list.get(i).getIpArea()!=null?list.get(i).getIpArea():"");
				maps.put("decision",list.get(i).getDecision()!=null?list.get(i).getDecision():"");
				maps.put("handleStatus",handleStatus);
				maps.put("handleResults",handleResults);
				if(list.get(i).getHandleTime() != null){
					maps.put("handleTime",DateUtil.getLongFormatDate(list.get(i).getHandleTime()));
				}else{
					maps.put("handleTime","");
				}
				maps.put("handlePerson",list.get(i).getHandlePerson());
				maps.put("handleRemark",list.get(i).getHandleRemark());
				maps.put("rollStatus",rollStatus);
				data.add(maps);
			}
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{
				"id","createTime","rollNo","merchantName","agentNo","rulesNo","rulesEngine",
				"rulesInstruction","orderNo","gnb","lbs","ip","ipArea","decision",
				"handleStatus","handleResults","handleTime","handlePerson","handleRemark","rollStatus"};
		String[] colsName = new String[]{
				"序号","创建时间","商户编号/身份证号/银行卡号","商户名称","代理商编号","规则编号","规则引擎",
				"规则指令","交易订单号","基站信息","经纬度","IP","IP解析地址","位置判断结果",
				"处理状态","处理结果","处理时间","处理人","处理备注","黑名单状态"};
		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("导出收单机构商户列表失败!",e);
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}
	}

	/**更多触发时间*/
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/showMoreTime")
	@ResponseBody
	public Object showMoreTime(@ModelAttribute("page")Page<BlackOperLog> page, @RequestParam("rollNo") String rollNo)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			riskRollService.selectMoreTime(page,rollNo);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("黑名单的操作日志查询报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

}
