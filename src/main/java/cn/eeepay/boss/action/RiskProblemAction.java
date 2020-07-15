package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskProblem;
import cn.eeepay.framework.model.RiskProblemAuditRecord;
import cn.eeepay.framework.model.ShiroUser;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.RiskProblemService;
import cn.eeepay.framework.service.RiskRulesService;

@Controller
@RequestMapping(value="/riskProblemAction")
public class RiskProblemAction {
	private static final Logger log = LoggerFactory.getLogger(RiskProblemAction.class);
	
	@Resource
	private RiskProblemService riskProblemService;
	
	@Resource
	private RiskRulesService riskRulesService;
	
	/**
	 * 初始化查询
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<RiskProblem> page,@RequestParam("info")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			RiskProblem rp=JSON.parseObject(param,RiskProblem.class);
			riskProblemService.selectAllInfo(page, rp);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("初始化查询报错~~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
		
	}
	
	/**
	 * 详情查询
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectInfo")
	@ResponseBody
	public Object selectInfo(@RequestParam("ids")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(param,Integer.class);
			RiskProblem rp = riskProblemService.selectInfo(id);
			if(rp==null){
				jsonMap.put("msg", "数据不存在");
				jsonMap.put("bols", false);
				return jsonMap;
			}
			List<RiskProblemAuditRecord> rlist = riskProblemService.selectRecordAllInfo(rp.getProblemId());
			jsonMap.put("record", rlist);
			jsonMap.put("result", rp);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("详情查询报错~~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 反馈处理查询
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectFeedbackInfo")
	@ResponseBody
	public Object selectFeedbackInfo(@RequestParam("ids")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(param,Integer.class);
			RiskProblem rp = riskProblemService.selectInfo(id);
			if(rp==null){
				jsonMap.put("msg", "数据不存在");
				jsonMap.put("bols", false);
				return jsonMap;
			}
			jsonMap.put("result", rp);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("反馈处理查询报错~~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 反馈处理措施
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/updateFeedback")
	@ResponseBody
	@SystemLog(description = "处理结果反馈",operCode="riskProblem.result")
	public Object updateFeedback(@RequestParam("info")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			RiskProblem rp=JSON.parseObject(param,RiskProblem.class);
			int i = riskProblemService.updateFeedback(rp);
			if(i>0){
				jsonMap.put("msg", "反馈处理措施成功~~~~");
				jsonMap.put("bols", true);
				
			}else{
				jsonMap.put("msg", "反馈处理措施失败~~~~");
				jsonMap.put("bols", false);
			}
			
		} catch (Exception e) {
			log.error("反馈处理措施报错~~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	
	/**
	 * 修改信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/updateInfo")
	@ResponseBody
	@SystemLog(description = "修改风控问题",operCode="riskProblem.update")
	public Object updateInfo(@RequestParam("info")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			System.out.println(param);
			RiskProblem rp=JSON.parseObject(param,RiskProblem.class);
			//查询风控规则ID是否存在
			if(rp==null){
				jsonMap.put("msg", "参数传输出错");
				jsonMap.put("bols", false);
				return jsonMap;
			}
			if(riskProblemService.selectInfoByRuleNo(rp.getRiskRulesNo())!=null){
				jsonMap.put("msg", "已经存在这个条风控问题");
				jsonMap.put("bols", false);
				return jsonMap;
			}
			if(riskRulesService.selectDetail(rp.getRiskRulesNo())==null){
				jsonMap.put("msg", "该条规则不存在,请检查规则");
				jsonMap.put("bols", false);
				return jsonMap;
			}
			int i = riskProblemService.updateInfo(rp);
			if(i>0){
				jsonMap.put("msg", "修改成功");
				jsonMap.put("bols", true);
				
			}else{
				jsonMap.put("msg", "修改失败");
				jsonMap.put("bols", false);
			}
			
		} catch (Exception e) {
			log.error("修改报错");
			jsonMap.put("msg", "修改报错");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 初始化审核查询
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAuditAllInfo")
	@ResponseBody
	public Object selectAuditAllInfo(@ModelAttribute("page")Page<RiskProblem> page,@RequestParam("info")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			RiskProblem rp=JSON.parseObject(param,RiskProblem.class);
			riskProblemService.selectAuditAllInfo(page, rp);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("初始化查询报错~~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
		
	}
	
	/**
	 * 审核
	 * @param page
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/addAuditInfo")
	@ResponseBody
	@SystemLog(description = "风控问题提交审核",operCode="riskProblem.insert")
	public Object addAuditInfo(@RequestParam("info")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			JSONObject json=JSON.parseObject(param);
			RiskProblem rp=json.getObject("info", RiskProblem.class);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			RiskProblemAuditRecord rpar=json.getObject("auditInfo", RiskProblemAuditRecord.class);
			rpar.setAuditPerson(principal.getId().toString());
			int i= riskProblemService.insertAuditRecord(rpar,rp);
			if(i>1){
				jsonMap.put("msg", "审核成功~~~~");
				jsonMap.put("bols", true);
				
			}else{
				jsonMap.put("msg", "审核出错异常~~~~");
				jsonMap.put("bols", false);
			}
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("审核异常~~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
		
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllUserInfo")
	@ResponseBody
	public Object selectAllUserInfo() throws Exception {
		List<ShiroUser> list = new ArrayList<ShiroUser>();
		try {
			list = riskProblemService.selectBossAllInfo();
		} catch (Exception e) {
			log.error("查询所有用户失败！");
		}
		return list;
	}
	
	/**
	 * 新增风控问题
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/addProblemInfo")
	@ResponseBody
	@SystemLog(description = "新增风控问题",operCode="riskProblem.insert")
	public Object addProblemInfo(@RequestParam("info")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			RiskProblem rp=JSON.parseObject(param, RiskProblem.class);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			rp.setStatus(1);
			rp.setCreatePerson(principal.getId().toString());
			if(riskProblemService.selectInfoByRuleNo(rp.getRiskRulesNo())==null){
				if(riskRulesService.selectDetail(rp.getRiskRulesNo())!=null){
					int i= riskProblemService.insertInfo(rp);
					if(i>0){
						jsonMap.put("msg", "新增成功");
						jsonMap.put("bols", true);
					}else{
						jsonMap.put("msg", "新增失败");
						jsonMap.put("bols", false);
					}
				}else{
					jsonMap.put("msg", "该条规则不存在");
					jsonMap.put("bols", false);
				}
			}else{
				jsonMap.put("msg", "已经存在这个条风控问题");
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("新增风控问题异常");
			jsonMap.put("bols", false);
			jsonMap.put("msg", "新增风控问题异常");
		}
		return jsonMap;
		
	}
	
}
