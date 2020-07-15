package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TerminalApply;
import cn.eeepay.framework.service.TerminalApplyService;

@Controller
@RequestMapping(value="/terminalApplyAction")
public class TerminalApplyAction {
	private static final Logger log = LoggerFactory.getLogger(TerminalApplyAction.class);
	
	@Resource
	private TerminalApplyService terminalApplyService;
	
	/**
	 * 初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<TerminalApply> page,@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			TerminalApply ta = JSON.parseObject(param,TerminalApply.class);
			terminalApplyService.queryAllInfo(page, ta);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("机具申请查询报错",e);
			System.out.println(e.getMessage());
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}	
	
	/**
	 * 详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectDetail")
	@ResponseBody
	public Object selectDetail(@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			TerminalApply ta = terminalApplyService.queryInfoDetail(ids);
			jsonMap.put("result", ta);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("机具申请详情查询报错",e);
			System.out.println(e.getMessage());
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 修改状态
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateInfo")
	@ResponseBody
	public Object updateInfo(@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int num=0;
			num = terminalApplyService.updateInfo(ids);
			if(num>0){
				jsonMap.put("msg", "操作成功");
				jsonMap.put("bols", true);
			}else{
				jsonMap.put("msg", "操作失败");
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("机具申请详情查询报错",e);
			System.out.println(e.getMessage());
			jsonMap.put("msg", "操作失败");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
}
