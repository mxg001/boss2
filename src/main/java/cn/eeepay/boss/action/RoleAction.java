package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.RoleInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.service.RightService;
import cn.eeepay.framework.service.RoleService;
import cn.eeepay.framework.service.UserGrantService;
import cn.eeepay.framework.service.UserService;

@Controller
@RequestMapping(value = "/role")
public class RoleAction {

	private static final Logger log = LoggerFactory.getLogger(RoleAction.class);

	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;
	
	@Resource
	private RightService rightService;
	
	@Resource
	private UserGrantService userGrantService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectRoleByCondition.do")
	@ResponseBody
	public Page<RoleInfo> selectRoleByCondition(@RequestParam("baseInfo") String baseInfo,
			@Param("page") Page<RoleInfo> page) throws Exception {
		RoleInfo role = JSON.parseObject(baseInfo, RoleInfo.class);
		try {
			roleService.selectRoleByCondition(role, page);
		} catch (Exception e) {
			log.error("角色条件查询失败");
		}
		return page;
	}
	
	@RequestMapping(value="/saveRole.do")
	@ResponseBody
	public Map<String, Object> saveRole(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSONObject.parseObject(param);
			RoleInfo Role = JSONObject.parseObject(json.getString("role"), RoleInfo.class);
			int num = roleService.insertRole(Role);
			if(num == 1){
				msg.put("status", true);
				msg.put("msg", "添加成功！");
			} else {
				msg.put("status", true);
				msg.put("msg", "添加失败！");
			}
		} catch (Exception e) {
			log.error("新增用户失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/updateRole.do")
	@ResponseBody
	public Map<String, Object> updateRole(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSONObject.parseObject(param);
			RoleInfo Role = JSONObject.parseObject(json.getString("role"), RoleInfo.class);
			int num = roleService.updateRole(Role);
			if(num == 1){
				msg.put("status", true);
				msg.put("msg", "修改角色成功！");
			} else {
				msg.put("status", true);
				msg.put("msg", "修改角色失败！");
			}
		} catch (Exception e) {
			log.error("修改角色失败");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getUsers.do")
	@ResponseBody
	public Map<String, Object> getUsers(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			List<UserInfo> roleUsers = roleService.getUsersByRole(id);
			List<UserInfo> userGroup = userService.getAllUsers();
			msg.put("roleUsers", roleUsers);
			msg.put("userGroup", userGroup);
		} catch (Exception e) {
			log.error("查询相关权限信息失败");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getRights.do")
	@ResponseBody
	public Map<String, Object> getRights(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			List<RightInfo> roleRights = roleService.getRightsByRole(id);
			List<RightInfo> rightGroup = rightService.getAllRights();
			msg.put("roleRights", roleRights);
			msg.put("rightGroup", rightGroup);
		} catch (Exception e) {
			log.error("查询相关权限信息失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editRoleUser.do")
	@ResponseBody
	public Map<String, Object> editRoleUser(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer roleId=JSONObject.parseObject(json.getString("roleId"), Integer.class);
			List<Integer> userIds=JSONArray.parseArray(json.getString("userIds"),Integer.class);
			int num = userGrantService.updateRoleAddUser(roleId, userIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加角色用户失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editRoleRight.do")
	@ResponseBody
	public Map<String, Object> editRoleRight(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer roleId=JSONObject.parseObject(json.getString("roleId"), Integer.class);
			List<Integer> rightIds=JSONArray.parseArray(json.getString("rightIds"),Integer.class);
			int num = userGrantService.updateRightToRole(roleId, rightIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加角色权限失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/deleteRole.do")
	@ResponseBody
	public Map<String, Object> deleteRole(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = roleService.deleteRoles(id);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "删除成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "删除失败");
			log.error("删除角色失败");
		}
		return msg;
	}
}
