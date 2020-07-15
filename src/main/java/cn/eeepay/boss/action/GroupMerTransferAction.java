package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TransRouteGroup;
import cn.eeepay.framework.service.TransRouteGroupService;

@Controller
@RequestMapping(value="/groupMerTransferAction")
public class GroupMerTransferAction {

	private static final Logger log = LoggerFactory.getLogger(GroupMerTransferAction.class);
	
	@Resource
	private TransRouteGroupService transRouteGroupService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByParam")
	@ResponseBody
	public Result selectByParam(@RequestParam("info") String param, @ModelAttribute Page<TransRouteGroup> page) {
		Result result = new Result();
		try {
			Map<String, Object> info = JSONObject.parseObject(param);
			if(info.get("acqId") == null ){
				result.setMsg("请选择收单机构");
				return result;
			}
			if(info.get("acqServiceId") == null){
				result.setMsg("请选择收单机构服务");
				return result;
			}
			transRouteGroupService.selectByParam(page, info);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(page);
		} catch (Exception e) {
			log.error("条件查询集群中商户转移失败", e);
		}
		return result;
	}
	
	@RequestMapping(value="/transferMer")
	@ResponseBody
	@SystemLog(description = "集群中普通商户转移",operCode="groupMer.transfer")
	public Map<String, Object> transferMer(@RequestBody String params) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			JSONObject json = JSONObject.parseObject(params); 
			List<TransRouteGroup> list = JSON.parseArray(json.getJSONArray("merList").toString(),TransRouteGroup.class);
			String groupCode = json.getString("groupCode");
			int num = transRouteGroupService.transferMer(groupCode, list);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}
		} catch (Exception e) {
			String str = e.getMessage();
			if(str==null){
				msg.put("msg", "操作失败");
			} else if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "操作异常");
			else
				msg.put("msg", str);
			log.error("集群中普通商户转移失败");
			e.printStackTrace();
		}
		return msg;
	}
}
