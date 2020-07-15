package cn.eeepay.boss.action;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MenuInfo;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.RoleInfo;
import cn.eeepay.framework.model.StaticUserInfo;
import cn.eeepay.framework.model.SysMenu;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.MenuService;
import cn.eeepay.framework.service.RightService;
import cn.eeepay.framework.service.RoleService;
import cn.eeepay.framework.service.StaticUserInfoService;
import cn.eeepay.framework.service.UserGrantService;
import cn.eeepay.framework.service.UserService;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * 系统用户
 * @author 沙
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserAction {
	
	private static final Logger log = LoggerFactory.getLogger(UserAction.class);
	
	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;
	
	@Resource
	private RightService rightService;
	
	@Resource
	private MenuService menuService;
	
	@Resource
	private UserGrantService userGrantService;
	
	@Resource
	private StaticUserInfoService staticUserInfoService;

	/**
	 * 系统用户条件分页查询
	 * @param baseInfo
	 * @param page
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectUserByCondition.do")
	@ResponseBody
	public Page<UserInfo> selectUserByCondition(@RequestParam("baseInfo")String baseInfo, 
			@Param("page")Page<UserInfo> page){
		UserInfo user = JSON.parseObject(baseInfo, UserInfo.class);
		try {
			userService.selectUserByCondition(user, page);
		} catch (Exception e) {
			log.error("用户条件查询失败");
		}
		return page;
	}
	
	//新增系统用户
	@RequestMapping(value="/saveUser.do")
	@ResponseBody
	@SystemLog(description = "新增系统用户",operCode="user.insert")
	public Map<String, Object> saveUser(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSONObject.parseObject(param);
			UserInfo user = JSONObject.parseObject(json.getString("user"), UserInfo.class);
			int num = userService.insertUser(user);
			if(num == 1){
				msg.put("status", true);
				msg.put("msg", "添加成功！");
			}
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "添加失败！");
			log.error("新增用户失败");
		}
		return msg;
	}
	//修改用户
	@RequestMapping(value="/updateUser.do")
	@ResponseBody
	@SystemLog(description = "用户修改",operCode="user.update")
	public Map<String, Object> updateUser(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSONObject.parseObject(param);
			UserInfo user = JSONObject.parseObject(json.getString("user"), UserInfo.class);
			int num = userService.updateUser(user);
			if(num == 1){
				msg.put("status", true);
				msg.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "修改失败！");
			log.error("修改用户失败");
		}
		return msg;
	}
	/**
	 * 验证用户名是否唯一
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkNameUnique.do")
	@ResponseBody
	public Map<String, Object> checkNameUnique(@RequestParam("userName")String userName) throws Exception{
		Map<String, Object> msg = null;
		try {
			msg = userService.checkNameUnique(userName);
		} catch (Exception e) {
			log.error("验证用户名是否唯一，出现错误");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getRoles.do")
	@ResponseBody
	public Map<String, Object> getRoles(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			List<RoleInfo> userRoles = userService.getRolesByUser(id);
			List<RoleInfo> roleGroup = roleService.getAllRoles();
			msg.put("userRoles", userRoles);
			msg.put("roleGroup", roleGroup);
		} catch (Exception e) {
			log.error("查询相关角色信息失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editUserRole.do")
	@ResponseBody
	public Map<String, Object> addUserRole(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer userId=JSONObject.parseObject(json.getString("userId"), Integer.class);
			List<Integer> roleIds=JSONArray.parseArray(json.getString("roleIds"),Integer.class);
			int num = userGrantService.updateRoleToUser(userId, roleIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加用户角色失败");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getRights.do")
	@ResponseBody
	public Map<String, Object> getRights(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			List<RightInfo> userRights = userService.getRightsByUser(id);
			List<RightInfo> rightGroup = rightService.getAllRights();
			msg.put("userRights", userRights);
			msg.put("rightGroup", rightGroup);
		} catch (Exception e) {
			log.error("查询相关权限信息失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editUserRight.do")
	@ResponseBody
	@SystemLog(description = "添加用户权限",operCode="user.role")
	public Map<String, Object> addUserRight(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer userId=JSONObject.parseObject(json.getString("userId"), Integer.class);
			List<Integer> rightIds=JSONArray.parseArray(json.getString("rightIds"),Integer.class);
			int num = userGrantService.updateRightToUser(userId, rightIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加用户权限失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/deleteUser.do")
	@ResponseBody
	@SystemLog(description = "删除用户",operCode="user.delete")
	public Map<String, Object> deleteUser(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = userService.deleteUser(id);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "删除成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "删除失败");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "删除失败");
			log.error("删除用户失败");
		}
		return msg;
	}
	
	/**
	 * 重置密码
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/restPwd.do")
	@ResponseBody
	public Map<String, Object> restPwd(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int i =userService.restPwd(id);
			if(i>0){
				msg.put("msg", "重置密码成功:abc888888");
			}else{
				msg.put("msg", "重置密码失败");
			}
		} catch (Exception e) {
			log.error("重置密码失败");
			msg.put("msg", "重置密码失败");
		}
		return msg;
	}

	
	/**
	 * 修改密码
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updatePwd.do")
	@ResponseBody
	public Map<String, Object> updatePwd(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			JSONObject json=JSONObject.parseObject(param);
			String pwd=json.getString("pwd");
			String newPwd=json.getString("newPwd");
			Map<String, Object> maps=new HashMap<String, Object>();
			maps.put("id", principal.getId());
			System.out.println(principal.getUsername());
			maps.put("name", principal.getUsername());
			maps.put("pwd", pwd);
			maps.put("newPwd", newPwd);
			int i = userService.updateUserPwd(maps);
			if(i==1){
				msg.put("msg", "修改密码成功");
				msg.put("bols", true);
			}else if(i==2){
				msg.put("msg", "原密码不正确");
				msg.put("bols", false);
			}else{
				msg.put("msg", "修改密码失败");
				msg.put("bols", false);
			}
		} catch (Exception e) {
			log.error("修改密码失败");
			msg.put("msg", "修改密码失败");
		}
		return msg;
	}
	
	/**
	 * 根据用户获取菜单
	 * @param baseInfo
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectMenuByUser.do")
	@ResponseBody
	public Map<String, Object> selectMenuByUser(@RequestParam("baseInfo") String baseInfo,
			@Param("page")Page<SysMenu> page) throws Exception {
		Map<String, Object> msg = new HashMap<>(); 
		SysMenu sysMenu = JSON.parseObject(baseInfo, SysMenu.class);
		try {
			menuService.selectMenuByCondition(page, sysMenu);
			List<MenuInfo> userMenus = userService.getMenuByUser(sysMenu.getId());//这个id其实是用户的id，只是存在这个对象里
			msg.put("page", page);
			msg.put("rightMenus", userMenus);
		} catch (Exception e) {
			log.error("根据权限查询菜单，条件查询失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editUserMenu.do")
	@ResponseBody
	public Map<String, Object> editUserMenu(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer userId=JSONObject.parseObject(json.getString("userId"), Integer.class);
			List<Integer> menuIds=JSONArray.parseArray(json.getString("menuIds"),Integer.class);
			int num = userGrantService.updateMenuToUser(userId, menuIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加用户菜单失败");
		}
		return msg;
	}
	
	/**
	 * 查询拥有某一权限的所有用户
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectUserByMenuCode.do")
	@ResponseBody
	public List<UserInfo> selectUserByMenuCode(String menuCode) throws Exception{
		List<UserInfo> list = null;
		 try {
			list = userService.selectUserByMenuCode(menuCode);
		} catch (Exception e) {
			log.error("查询拥有某一权限的所有用户失败");
		}
		 return list;
	}
	
	
	/**
	 * 待完善商户信息查询
	 * @param info
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/perfectMerchantQuery.do")
	@ResponseBody
	public Map<String, Object> perfectMerchantQuery(@RequestParam("info") String info,
			@Param("page")Page<UserInfo> page) throws Exception {
		Map<String, Object> msg = new HashMap<>(); 
		UserInfo user = JSON.parseObject(info, UserInfo.class);
		try {
			userService.perfectMerchantQuery(user, page);
			msg.put("bols", true);
			msg.put("page", page);
		} catch (Exception e) {
			log.error("待完善商户信息查询异常");
			e.printStackTrace();
			msg.put("bols", false);
			msg.put("msg", "待完善商户信息查询异常");
		}
		return msg;
	}

	/**
	 * 待完善商户信息查询
	 * @param info
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delPerfectMerchant.do")
	@SystemLog(description = "待完善商户删除",operCode="merchant.delPerMer")
	@ResponseBody
	public Map<String, Object> delPerfectMerchant(@RequestParam("ids") String ids) throws Exception {
		Map<String, Object> msg = new HashMap<>();

		try {

				userService.delPerfectMerchant(Integer.parseInt(ids));
				msg.put("bols", true);
				msg.put("msg", "删除成功");


		} catch (Exception e) {
			log.error("待完善商户信息删除异常");
			e.printStackTrace();
			msg.put("bols", false);
			msg.put("msg", "待完善商户信息删除异常");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/findUserBox.do")
	@ResponseBody
	public Object findUserBox() throws Exception {
		List<UserInfo> list=null;
		try {
			list = userService.findUserBox();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * 用户统计初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<StaticUserInfo> page,@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			StaticUserInfo staticUserInfo=JSON.parseObject(param,StaticUserInfo.class);
			staticUserInfoService.selectAllInfo(page, staticUserInfo);
			jsonMap.put("page", page);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("用户统计初始化和模糊查询分页",e);
		}
		return page;
	}
	
	@RequestMapping(value="/addInfo")
	@ResponseBody
	@SystemLog(description = "新增用户统计",operCode="userTotal.insert")
	public Object addInfo(@RequestBody String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			StaticUserInfo staticUserInfo=JSON.parseObject(param,StaticUserInfo.class);
			if(staticUserInfoService.findInfo(staticUserInfo).size()>0){
				jsonMap.put("msg", "当前手机号已存在");
				jsonMap.put("bols", false);
			}else{
				int i =staticUserInfoService.addInfo(staticUserInfo);
				if(i>0){
					jsonMap.put("msg", "操作成功");
					jsonMap.put("bols", true);
				}else{
					jsonMap.put("msg", "操作失败");
					jsonMap.put("bols", false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("msg", "操作失败");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}


	@RequestMapping(value="/deleteInfo")
	@ResponseBody
	@SystemLog(description = "删除用户统计",operCode="userTotal.detele")
	public Object deleteInfo(@RequestBody String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			List<StaticUserInfo> staticUserInfo =JSON.parseArray(param,StaticUserInfo.class);
//			if(staticUserInfo.size()<2){
//				jsonMap.put("bols", false);
//				jsonMap.put("msg", "删除是选中的不能少于2条");
//			}
			int num =0;
			for (StaticUserInfo staticUserInfo2 : staticUserInfo) {
				num+=staticUserInfoService.deleteInfo(staticUserInfo2.getStaticUserId().toString());
			}
			if(num>0){
				jsonMap.put("msg", "操作成功");
				jsonMap.put("bols", true);
			}else{
				jsonMap.put("bols", false);
				jsonMap.put("msg", "操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("用户统计初始化和模糊查询分页",e);
			jsonMap.put("msg", "操作失败");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportInfo.do")
	@ResponseBody 
//	@SystemLog(description = "导出用户统计",operCode="userTotal.export")
	public void exportInfo(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		StaticUserInfo su = JSON.parseObject(info, StaticUserInfo.class);
		su.setUserName(new String(su.getUserName().getBytes("ISO-8859-1"),"UTF-8"));
		System.out.println(su.getUserName());
		List<StaticUserInfo> list=staticUserInfoService.selectAllInfoImprot(su);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "用户统计记录"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);   
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		  for(int i =0;i<list.size();i++){
			  StaticUserInfo sus=list.get(i);
			   Map<String,String> maps = new HashMap<String,String>() ;
			   maps.put("id",sus.getStaticUserId().toString());
			   maps.put("userName", sus.getUserName());
			   maps.put("mobilephone", sus.getMobilephone());
			   maps.put("department", sus.getDepartment());
			   maps.put("merchantNo", sus.getMerchantNo());
			   maps.put("netAmt", sus.getNetAmt());
			   maps.put("netCnt", sus.getNetCnt());
			   maps.put("totalAmt", sus.getTotalAmt());
			   maps.put("totalCnt", sus.getTotalCnt());
			   maps.put("remark", sus.getRemark());
			   data.add(maps) ;
		  }
			  
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"id","userName","mobilephone","department","merchantNo","netCnt","netAmt","totalCnt","totalAmt","remark"};
		  String[] colsName = new String[]{"编号","用户名称","用户手机号","部门","商户号","累计成功交易次数","累计成功交易金额","累计交易次数","累计交易金额","备注"};
		  OutputStream ouputStream = response.getOutputStream();    
		  export.export(cols, colsName, data, response.getOutputStream());
		  ouputStream.close(); 
	}

	//查询用户前50条
	@RequestMapping(value="/getUserlimit")
	@ResponseBody
	public Map<String, Object> getUserlimit(@RequestParam("info")String info) throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		UserInfo user = JSON.parseObject(info, UserInfo.class);
		try {
			List<UserInfo> list=userService.getUserlimit(user);
			map.put("userList",list);
			map.put("status",true);
		} catch (Exception e) {
			log.error("查询用户前100条失败!");
			map.put("msg","查询用户前100条失败!");
			map.put("status",true);
		}
		return map;
	}
}
