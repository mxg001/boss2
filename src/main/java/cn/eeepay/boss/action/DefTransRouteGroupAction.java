package cn.eeepay.boss.action;

import java.util.HashMap;
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
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.DefTransRouteGroup;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.TransRouteGroup;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.DefTransRouteGroupService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TransRouteGroupService;

/**
 * 默认路由集群管理
 */
@Controller
@RequestMapping(value="/defTransRouteGroupAction")
public class DefTransRouteGroupAction {

	private static final Logger log = LoggerFactory.getLogger(DefTransRouteGroupAction.class);
	
	@Resource
	private DefTransRouteGroupService defTransRouteGroupService;
	
	@Resource
	private TransRouteGroupService transRouteGroupService;
	
	@Resource
	private SysDictService sysDictService;
	
	/**
	 * 初始化数据和分页，模糊查询
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<DefTransRouteGroup> page,@RequestParam("info")String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			DefTransRouteGroup dtrg = JSON.parseObject(param,DefTransRouteGroup.class);
			defTransRouteGroupService.selectAllInfo(page, dtrg);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("msg", "查询出错~~~");
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
	@RequestMapping(value="/selectByParam")
	@ResponseBody
	public Object selectByParam(@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(ids,Integer.class);
			DefTransRouteGroup dtrg = defTransRouteGroupService.selectByPrimaryKey(id);
			jsonMap.put("result", dtrg);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("msg", "查询出错~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 根据集群ID查询名称
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/selectGroupNameByGroupId")
	@ResponseBody
	public Object selectGroupNameByGroupId(@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(ids,Integer.class);
			TransRouteGroup trg = transRouteGroupService.selectByPrimaryKey(id);
			if(trg==null){
				jsonMap.put("msg", "没有这个集群");
				jsonMap.put("bols", false);
			}else{
				jsonMap.put("result", trg);
				jsonMap.put("bols", true);
			}
			
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("msg", "查询出错~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 修改默认集群
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateBpCluster")
	@ResponseBody
	@SystemLog(description = "修改默认集群",operCode="bpCluster.update")
	public Object updateBpCluster(@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			DefTransRouteGroup dtrg=JSON.parseObject(param,DefTransRouteGroup.class);
			DefTransRouteGroup dtrg2 = defTransRouteGroupService.selectInfo(dtrg.getProductId().toString().trim(),dtrg.getServiceId().toString().trim());
			if(dtrg2==null || dtrg.getId().equals(dtrg2.getId())){
				TransRouteGroup trg = transRouteGroupService.selectByPrimaryKey(Integer.parseInt(dtrg.getDefGroupCode()));
				if(trg==null){
					jsonMap.put("msg", "默认集群编号不存在");
					jsonMap.put("bols", false);
					return jsonMap;
				}
				if(!trg.getServiceType().toString().equals(dtrg.getServiceType())){
					jsonMap.put("msg", "商户服务类型与默认集群的服务类型不一致");
					jsonMap.put("bols", false);
					return jsonMap;
				}
				if(dtrg.getAcqServiceType()!=null&&dtrg.getServiceType()!=null){
					SysDict sysDict = sysDictService.selectExistServiceLink(dtrg.getServiceType(),dtrg.getAcqServiceType().toString());
					if(sysDict==null){
						jsonMap.put("msg", "收单服务类型与商户服务类型不匹配");
						jsonMap.put("bols", false);
						return jsonMap;
					}
				}
				
				int i = defTransRouteGroupService.updateByPrimaryKey(dtrg);
				if(i>0){
					jsonMap.put("bols", true);
					jsonMap.put("msg","修改成功");
				}else{
					jsonMap.put("bols", false);
					jsonMap.put("msg","修改失败");
				}
			}else{
				jsonMap.put("bols", false);
				jsonMap.put("msg","已存在相同的业务产品/服务");
			}
		} catch (Exception e) {
			log.error("报错!!!",e);
			System.out.println(e);
			jsonMap.put("bols", false);
			jsonMap.put("msg","修改报错");
		}
		return jsonMap;
	}
	
	/**
	 * 新增默认集群
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addBpCluster")
	@ResponseBody
	@SystemLog(description = "新增默认集群",operCode="bpCluster.insert")
	public Object addBpCluster(@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			DefTransRouteGroup dtrg=JSON.parseObject(param,DefTransRouteGroup.class);
			if(dtrg != null && StringUtils.isNoneBlank(dtrg.getDefGroupCode())){
				TransRouteGroup trg = transRouteGroupService.selectByPrimaryKey(Integer.parseInt(dtrg.getDefGroupCode()));
				if(trg==null){
					jsonMap.put("msg", "默认集群编号不存在");
					jsonMap.put("bols", false);
					return jsonMap;
				}
				if(!trg.getServiceType().toString().equals(dtrg.getServiceType())){
					jsonMap.put("msg", "商户服务类型与默认集群的服务类型不一致");
					jsonMap.put("bols", false);
					return jsonMap;
				}
			}
			if(defTransRouteGroupService.selectInfo(dtrg.getProductId().toString().trim(),dtrg.getServiceId().toString().trim())!=null){
				jsonMap.put("msg", "已存在相同的业务产品/服务");
				jsonMap.put("bols", false);
				return jsonMap;
			}
			if(dtrg.getAcqServiceType()!=null&&dtrg.getServiceType()!=null){
				SysDict sysDict = sysDictService.selectExistServiceLink(dtrg.getServiceType(),dtrg.getAcqServiceType().toString());
				if(sysDict==null){
					jsonMap.put("msg", "收单服务类型与商户服务类型不匹配");
					jsonMap.put("bols", false);
					return jsonMap;
				}
			}
			dtrg.setCreatePerson(principal.getId().toString());
			dtrg.setDefType(1);
			int i = defTransRouteGroupService.insert(dtrg);
			if(i>0){
				jsonMap.put("bols", true);
				jsonMap.put("msg", "新增成功");
			}else{
				jsonMap.put("bols", false);
				jsonMap.put("msg", "新增失败");
			}
		} catch (Exception e) {
			log.error("报错!!!",e);
			System.out.println(e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "新增报错");
		}
		return jsonMap;
	}
	
}
