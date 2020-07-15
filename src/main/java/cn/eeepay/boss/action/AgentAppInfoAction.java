package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentAppInfo;
import cn.eeepay.framework.service.AgentAppInfoService;

@Controller
@RequestMapping(value="/appInfo")
public class AgentAppInfoAction {

	private static final Logger log = LoggerFactory.getLogger(AgentAppInfoAction.class);
	
	@Resource
	private AgentAppInfoService agentAppInfoService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByCondition.do")
	@ResponseBody
	public Page<AgentAppInfo> selectByCondition(@RequestBody String param,@ModelAttribute("page") Page<AgentAppInfo> page)
		throws Exception{
		try{
			JSONObject json = JSON.parseObject(param);  
			Map<String, Object> map = json.getJSONObject("query");
			if(map != null){
				int pageSize = (int) map.get("pageSize") ;
				int pageNo = (int) map.get("pageNo") ;
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);
			}
			agentAppInfoService.selectByCondition(page,map);
		} catch (Exception e){
			log.error("条件查询AgentInfo失败");
		}
		return page;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectDetail/{id}")
	@ResponseBody
	public AgentAppInfo selectDetailById(@PathVariable("id")Long id) throws Exception{
		AgentAppInfo appInfo = null;
		try{
			appInfo = agentAppInfoService.selectDetailById(id);
		} catch (Exception e){
			log.error("查询代理商App软件设置详情失败");
		}
		return appInfo;
	}
	
	@RequestMapping(value="/modifyAppInfo/{id}")
	@ResponseBody
	public Map<String, Object> modifyAppInfo(@PathVariable("id")Long id) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg = agentAppInfoService.modifyAppInfo(id);
		} catch (Exception e){
			log.error("进入修改代理商App软件设置页面失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/saveAppInfo.do")
	@ResponseBody
	@SystemLog(description = "新增|修改代理商App软件设置",operCode="appInfo.insert")
	public Map<String, Object> saveAppInfo(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			JSONObject json = JSON.parseObject(param);
			AgentAppInfo appInfo = json.getObject("appInfo", AgentAppInfo.class);
			int num = agentAppInfoService.insertOrUpdate(appInfo);
			if(num>0){
				msg.put("status",true);
				msg.put("msg", "保存 || 修改成功");
			}
		} catch (Exception e){
			msg.put("status",false);
			msg.put("msg", "保存 || 修改失败");
			log.error("进入修改代理商App软件设置页面失败");
		}
		return msg;
	}
}
