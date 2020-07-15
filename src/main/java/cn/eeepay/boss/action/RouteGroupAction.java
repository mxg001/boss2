package cn.eeepay.boss.action;

import java.io.File;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
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
import cn.eeepay.framework.service.RouteGroupService;
import cn.eeepay.framework.service.TransRouteGroupService;
import cn.eeepay.framework.service.impl.SeqService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 路由集群action
 * 
 * @author junhu
 *
 */
@Controller
@RequestMapping("/routeGroup")
public class RouteGroupAction {

	private static final Logger log = LoggerFactory.getLogger(RouteGroupAction.class);

	public static  final String SERVICE_TYPE_FOR_MERCHANT_TYPE="SERVICE_TYPE_FOR_MERCHANT_TYPE";//根据商户服务类型，查询包含的路由集群数据字典key
	@Resource
	private RouteGroupService routeGroupService;
	@Resource
	private SeqService seqService;

	@Resource
	private TransRouteGroupService transRouteGroupService;
	
	@Resource
	private SysDictService sysDictService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/acqOrgSelectBox.do", method = RequestMethod.POST)
	public @ResponseBody List<AcqOrg> acqOrgSelectBox() {
		return routeGroupService.acqOrgSelectBox();
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/acqServiceSelectBox.do", method = RequestMethod.POST)
	public @ResponseBody List<AcqService> acqServiceSelectBox(@RequestBody String param) {
		JSONObject jsonObject = JSON.parseObject(param);
		int acqId = jsonObject.getIntValue("acqId");
		return routeGroupService.acqServiceSelectBox(acqId);
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/oneLevelAgentSelectBox.do", method = RequestMethod.POST)
	public @ResponseBody List<AgentInfo> oneLevelAgentSelectBox() {
		return routeGroupService.oneLevelAgentSelectBox();
	}

	@RequestMapping(value = "/addRouteGroup.do", method = RequestMethod.POST)
	@SystemLog(description = "新增路由集群",operCode="managerRoute.insert")
	public @ResponseBody Map<String, Object> addRouteGroup(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			TransRouteGroup routeGroup = JSON.parseObject(param, TransRouteGroup.class);
			routeGroup.setGroupCode(Integer.valueOf(seqService.createKey("group_no")));
			int num = routeGroupService.insertRouteGroup(routeGroup);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "添加成功！");
			}
		} catch (Exception e) {
			log.error("添加失败！", e);
			if(e.getMessage().contains("\r\n") || e.getMessage().contains("\n")){
				msg.put("msg", "添加失败");
			}else{
				msg.put("msg", e.getMessage());
			}
			msg.put("status", false);
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryRouteGroupList.do", method = RequestMethod.POST)
	@ResponseBody
	public Page<TransRouteGroup> queryRouteGroupList(@RequestParam("info") String param,
			@ModelAttribute("page") Page<TransRouteGroup> page) {
		try {
			Map<String, Object> jsonMap = JSON.parseObject(param);
			routeGroupService.queryRouteGroupByCon(jsonMap, page);
		} catch (Exception e) {
			log.error("查询路由集群列表失败！", e);
		}
		return page;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getRouteGroup.do", method = RequestMethod.POST)
	@ResponseBody
	public TransRouteGroup getRouteGroup(@RequestBody String param) {
		try {
			Map<String, Object> jsonMap = JSON.parseObject(param);
			return routeGroupService.queryRouteGroupById(jsonMap);
		} catch (Exception e) {
			log.error("获取路由集群失败！", e);
		}
		return null;
	}

	@RequestMapping(value = "/updateRouteGroup.do", method = RequestMethod.POST)
	@SystemLog(description = "修改路由集群",operCode="managerRoute.update")
	public @ResponseBody Map<String, Object> updateRouteGroup(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			TransRouteGroup routeGroup = JSON.parseObject(param, TransRouteGroup.class);
			int num = routeGroupService.updateRouteGroup(routeGroup);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			log.error("修改失败！", e);
			if(e.getMessage().contains("\r\n") || e.getMessage().contains("\n")){
				msg.put("msg", "修改失败");
			}else{
				msg.put("msg", e.getMessage());
			}
			msg.put("status", false);
		}
		return msg;
	}

	@RequestMapping(value = "/addRouteGroupMerchant.do", method = RequestMethod.POST)
	@SystemLog(description = "增加普通商户",operCode="managerRoute.addMerchant")
	public @ResponseBody Map<String, Object> addRouteGroupMerchant(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			TransRouteGroupMerchant routeGroupMerchant = JSON.parseObject(param, TransRouteGroupMerchant.class);
			msg = routeGroupService.insertRouteGroupMerchant(routeGroupMerchant);
		} catch (Exception e) {
			log.error("添加失败！", e);
			msg.put("status", false);
			msg.put("msg", "添加异常！");
		}
		return msg;
	}

	@RequestMapping(value = "/addRouteGroupAcqMerchant.do", method = RequestMethod.POST)
	@SystemLog(description = "增加收单商户",operCode="managerRoute.addAcqMerchant")
	public @ResponseBody Map<String, Object> addRouteGroupAcqMerchant(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			TransRouteGroupAcqMerchant routeGroupAcqMerchant = JSON.parseObject(param, TransRouteGroupAcqMerchant.class);
			msg = routeGroupService.insertRouteGroupAcqMerchant(routeGroupAcqMerchant);
		} catch (Exception e) {
			log.error("添加失败！", e);
			msg.put("status", false);
			msg.put("msg", "添加失败！");
		}
		return msg;
	}
	
	
	
	/**
	 * 删除集群
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteByid")
	@ResponseBody
	@SystemLog(description = "删除集群",operCode="managerRoute.delete")
	public Object deleteById(@RequestParam("ids") String ids,@RequestParam("groupCode") String groupCode)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(ids,Integer.class);
			int code = JSON.parseObject(groupCode,Integer.class);
			List<TransRouteGroup> merGroupList = transRouteGroupService.selectMerNoByGroupCode(Integer.toString(code));
			if(merGroupList != null && merGroupList.size()>0){
				jsonMap.put("bols", false);
				jsonMap.put("msg", "集群已存在普通商户");
				return jsonMap;
			}
			List<TransRouteGroup> acqMerGroupList =transRouteGroupService.selectAcqMerNoByGroupCode(Integer.toString(code));
			if(acqMerGroupList != null && acqMerGroupList.size()>0){
				jsonMap.put("bols", false);
				jsonMap.put("msg", "集群已存在收单商户");
				return jsonMap;
			}
			
			int i = routeGroupService.deleteRouteGroup(id);
			if(i>0){
				jsonMap.put("bols", true);
			}else{
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("msg", "删除失败");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	@RequestMapping(value="/updateGroupStatus.do")
	@ResponseBody
	@SystemLog(description = "路由集群开关",operCode="managerRoute.switch")
	public Map<String, Object> updateGroupStatus(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			TransRouteGroup group = JSONObject.parseObject(param, TransRouteGroup.class);
			group.setStatus(group.getStatus()==0?1:0);
			int num = routeGroupService.updateGroupStatus(group);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "修改成功");
			}
		} catch (Exception e) {
			log.error("修改路由集群状态失败");
			e.printStackTrace();
			msg.put("status", false);
			msg.put("msg", "修改失败");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getGroupByCode")
	@ResponseBody
	public TransRouteGroup getGroupByCode(@RequestParam Map<String,String> param) throws Exception{
		TransRouteGroup info = null;
		try {
			String groupCode = param.get("groupCode");
			info = routeGroupService.getGroupByCode(groupCode);
		} catch (Exception e) {
			log.error("根据集群编号，查询路由集群失败");
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 根据商户服务类型，查询包含的路由集群
	 * @author tans
	 * @date 2017年3月15日 上午10:39:06
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getGroupByServiceType")
	@ResponseBody
	public Map<String, Object> getGroupByServiceType(@RequestParam(value = "group",required = false) String group){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		List<SysDict> list=sysDictService.selectByKey(RouteGroupAction.SERVICE_TYPE_FOR_MERCHANT_TYPE);
		String[] serviceTypes=null;
		if(list!=null&&list.size()>0){
			serviceTypes =new String[list.size()];
			for(int i=0;i<list.size();i++){
				SysDict sd=list.get(i);
				serviceTypes[i]=sd.getSysValue();
			}
		}else{
			serviceTypes=new String[]{"4","9","10004"};
		}
		try {
			List<TransRouteGroup> groupList = routeGroupService.getGroupByServiceType(serviceTypes,group);
			msg.put("groupList", groupList);
			msg.put("status", true);
		} catch (Exception e) {
			log.error("根据商户服务类型，查询包含的路由集群失败",e);
		}
		return msg;
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
		String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"routingClusterTemplate.xls";
		log.info(filePath);
		ResponseUtil.download(response, filePath,"批量导入收单商户号模板.xlsx");
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
	public Map<String, Object> importDiscount(@RequestParam("file") MultipartFile file){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "导入文件格式错误");
					return msg;
				}
			}
			msg = routeGroupService.importDiscount(file);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "导入失败");
			log.error("导入失败",e);
		}
		return msg;
	}
	
	@RequestMapping("/mapGroupSelect")
	@ResponseBody
	public Result mapGroupSelect(Integer groupCode){
		if (groupCode==null) {
			groupCode=-1;
		}
		Result result = new Result();
		result.setStatus(true);
		try {
			List<Map<String,String>> list = routeGroupService.getMapGroupSelect(groupCode);
			result.setData(list);
		} catch (Exception e) {
			result.setStatus(false);
			log.error("系统异常",e);
		}
		return result;
	}
}
