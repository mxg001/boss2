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
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.service.AddRequireItemService;

@Controller
@RequestMapping(value="/require")
public class AddRequireItemAction {
	private static final Logger log = LoggerFactory.getLogger(AddRequireItemAction.class);

	@Resource
	AddRequireItemService addRequireItemService;

	/**
	 * 条件查询进件
	 * 
	 * @param requireName
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/queryRequireItem")
	public @ResponseBody Object queryRequireItem(String requireName,@ModelAttribute("page") Page<AddRequireItem> page)
		throws Exception{
		try{
			addRequireItemService.selectByName(page,requireName);
		} catch (Exception e){
			log.error("条件查询进件失败",e);
		}
		return page;
	}

	/**
	 * 根据进件ID，查询进件详情
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/requireDetailCtrl/{id}")
	@ResponseBody
	public AddRequireItem queryRequireById(@PathVariable("id") String id) throws Exception{
		AddRequireItem item = null;
		try{
			 item = addRequireItemService.selectById(id);
		} catch (Exception e) {
			log.error("查询进件详情失败",e);
		}
		return item;
	}

	/**
	 * 增加进件  || 保存修改
	 */
	@RequestMapping(value="/addRequire")
	@ResponseBody
	@SystemLog(description = "进件要求项保存|修改",operCode="require.insert")
	public Map<String, Object> addOrUpdate(@RequestBody String params) throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject json=JSON.parseObject(params);
			AddRequireItem item = json.getObject("baseInfo", AddRequireItem.class);
			int num = addRequireItemService.insertOrUpDate(item);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加进件 || 修改进件成功！");
			}
		} catch (Exception e) {
			log.error("添加服务失败！", e);
			msg.put("status", false);
			msg.put("msg", "添加进件 || 修改进件失败！");
		}
		return msg;
	}
	
}
