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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.OrgRouteMerService;

/**
 * 集群中普通商户控制器
 * 
 * @author junhu
 *
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/orgRouteMer")
public class OrgRouteMerAction {
	
	private static final Logger log = LoggerFactory.getLogger(OrgRouteMerAction.class);
	
	@Resource
	private OrgRouteMerService orgRouteMerService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryOrgRouteMerList.do", method = RequestMethod.POST)
	@ResponseBody
	public Page<Map> queryOrgRouteMerList(@RequestParam("info") String param,
			@ModelAttribute("page") Page<Map> page) {
		try {
			Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
			orgRouteMerService.listOrgRouteMerByCon(jsonMap, page);
		} catch (Exception e) {
			log.error("查询集群普通商户列表失败！", e);
		}
		return page;
	}
	
	@RequestMapping(value = "/delOrgRouteMer.do", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(description = "普通商户删除",operCode="orgRouteMer.delete")
	public Map<String, Object> delOrgRouteMer(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			Long id = json.getObject("id", Long.class);
			int num = orgRouteMerService.deleteOrgRouteMerById(id);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "删除成功！");
			}
		} catch (Exception e) {
			log.error("删除失败！", e);
			msg.put("status", false);
			msg.put("msg", "删除失败！");
		}
		return msg;
	}
}
