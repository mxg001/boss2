
package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Msg;
import cn.eeepay.framework.service.MsgService;
import cn.eeepay.framework.util.Constants;

//import cn.eeepay.framework.service.sysUser.UserRigthService;
@Controller
@RequestMapping(value = "/msg")
public class MsgAction {
	private static final Logger log = LoggerFactory.getLogger(MsgAction.class);
	@Resource
	public MsgService msgService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/selectMsgByCondition.do")
	@ResponseBody
	public Page<Msg> selectMsgByCondition(@RequestParam("baseInfo") String baseInfo,
			@Param("page") Page<Msg> page) throws Exception{
		Msg msg = JSON.parseObject(baseInfo, Msg.class);
		try{
			msgService.selectMsgByCondition(page, msg);
		} catch(Exception e) {
			log.error("查询所有提示语失败",e);
		}
		return page;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/queryAgentTips")
	@ResponseBody
	public Result queryAgentTips(){
		try{
			return Result.success("查询代理商提示语成功",msgService.queryAgentTips());
		} catch(Exception e) {
			log.error("查询代理商提示语失败",e);
		}
		return Result.fail("查询代理商提示语失败");
	}

	@RequestMapping(value="/addMsg")
	@ResponseBody
	@SystemLog(description = "新增提示语信息",operCode="sys.msgAdd")
	public Object addMsg(@RequestParam("info") String param)throws Exception {
		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("status", false);
		msgMap.put("msg", "添加失败！");

		try {
			Msg msg = JSON.parseObject(param, Msg.class);
			if(msg==null || StringUtils.isBlank(msg.getModuleName())){
				msgMap.put("msg", "参数异常！");
				return msgMap;
			}
			
			msgService.addMsg(msg,msgMap);
		}catch (Exception e){
			log.error("新增提示语失败",e);
			msgMap.put("status", false);
			msgMap.put("msg", "添加失败！");
		}

		return msgMap;
	}
	
	@RequestMapping(value="/updateMsg")
	@ResponseBody
	@SystemLog(description = "修改提示语信息",operCode="sys.msgUpdate")
	public Object updateMsg(@RequestParam("info") String param)throws Exception {
		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("status", false);
		msgMap.put("msg", "修改失败！");

		try {
			Msg msg = JSON.parseObject(param, Msg.class);
			if(msg==null || StringUtils.isBlank(msg.getModuleName())){
				msgMap.put("msg", "参数异常！");
				return msgMap;
			}
			msgService.updateMsg(msg,msgMap);
			
		}catch (Exception e){
			log.error("修改提示语失败",e);
			msgMap.put("status", false);
			msgMap.put("msg", "添加失败！");
		}

		return msgMap;
	}
	

	@RequestMapping("/msgDetail")
	@ResponseBody
	public Object findDetail(@RequestParam("id") String id){

		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("status", false);
		msgMap.put("msg", "查询失败！");
		
		try {
			Msg msg = msgService.msgDetail(id);
			msgMap.put("info", msg);
			msgMap.put("status", true);
			msgMap.put("msg", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常\n",e);
			msgMap.put("status", false);
			msgMap.put("msg", "查询异常！");
		}

		return msgMap ;
	}

	
	@RequestMapping("/refresh")
	@ResponseBody
	public Object refresh(@RequestParam("id") String id){

		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("status", false);
		msgMap.put("msg", "查询失败！");
		System.out.println(id);
		try {
			//保留接口
			msgMap.put("status", true);
			msgMap.put("msg", "刷新成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常\n",e);
			msgMap.put("status", false);
			msgMap.put("msg", "刷新异常！");
		}

		return msgMap ;
	}
	
	@RequestMapping("/changeStatus")
	@ResponseBody
	public Object changeStatus(@RequestParam("data") String param){
		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("status", false);
		msgMap.put("msg", "更改失败！");
		try {
			
			JSONObject json=JSON.parseObject(param);
			
			String id = json.getString("id");
			String status = json.getString("status");
			
			int i =msgService.changeStatus(id,status);
			if(i==1){
				msgMap.put("status", true);
				msgMap.put("msg", "更改成功！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常\n",e);
			msgMap.put("status", false);
			msgMap.put("msg", "刷新异常！");
		}

		return msgMap ;
	}
}
