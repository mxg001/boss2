//package cn.eeepay.boss.action;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.collections.ListUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.eeepay.framework.db.pagination.Page;
//import cn.eeepay.framework.db.pagination.Sort;
//import cn.eeepay.framework.enums.MenuType;
//import cn.eeepay.framework.model.Node;
//import cn.eeepay.framework.model.RoleRigth;
//import cn.eeepay.framework.model.ShiroRigth;
//import cn.eeepay.framework.model.ShiroRole;
//import cn.eeepay.framework.model.ShiroUser;
//import cn.eeepay.framework.model.SysDept;
//import cn.eeepay.framework.model.SysMenu;
//import cn.eeepay.framework.model.UserInfo;
//import cn.eeepay.framework.model.UserRigth;
//import cn.eeepay.framework.model.UserRole;
//import cn.eeepay.framework.service.sysUser.RoleRigthService;
//import cn.eeepay.framework.service.sysUser.ShiroRigthService;
//import cn.eeepay.framework.service.sysUser.ShiroRoleService;
//import cn.eeepay.framework.service.sysUser.SysConfigService;
//import cn.eeepay.framework.service.sysUser.SysDeptService;
//import cn.eeepay.framework.service.sysUser.SysMenuService;
//import cn.eeepay.framework.service.sysUser.UserRigthService;
//import cn.eeepay.framework.service.sysUser.UserRoleService;
//import cn.eeepay.framework.service.sysUser.UserService;
//
///**
// * 
// * by zouruijin
// * email rjzou@qq.com zrj@eeepay.cn
// * 2016年4月12日13:45:54
// *
// */
//
////@Controller
////@RequestMapping(value = "/sysAction")
//public class SysAction {
//	@Resource
//	public UserService userService;
//	@Resource
//	public SysDeptService sysDeptService;
//	@Resource
//	public SysMenuService sysMenuService;
//	@Resource
//	public ShiroRigthService shiroRigthService;
//	@Resource
//	public ShiroRoleService shiroRoleService;
//	@Resource
//	public UserRigthService userRigthService;
//	@Resource
//	public RoleRigthService roleRigthService;
//	@Resource
//	public UserRoleService userRoleService;
//	@Resource
//	public SysConfigService sysConfigService;
//	
//	private static final Logger log = LoggerFactory.getLogger(SysAction.class);
//	//返回视图
//	//用户登录
////	@RequestMapping(value = "/login.do")
////	public String login(@RequestParam Map<String, String> params){
////		return "index";
////	}
//	@RequestMapping(value = "/menu.do")
//	public String menu(ModelMap model, @RequestParam Map<String, String> params){
//		List<String> list = new ArrayList<String>();
//		String str = "";
//		try {
////			List<SysConfig> menuTypes = sysConfigService.findSysConfigGroup("sys_menu_type");
////			model.put("menuTypes", menuTypes);
//			
//			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
//			list.add("{ id:'root', pId:0, name:'系统菜单', open:true}");
//			for (SysMenu sysMenu : sysMenus) {
//				String pId = "root"; 
//				if (sysMenu.getParentId() != null) {
//					pId = sysMenu.getParentId().toString();
//				}
//				list.add("{ id:'"+sysMenu.getId()+"', pId:'"+pId+"', name:'"+sysMenu.getMenuName()+"', open:true}");
//			}
//			str = StringUtils.join(list,",\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.put("menuTree", str);
//		return "sys/menu";
//	}
//	@RequestMapping(value = "/findMenuById.do")
//	@ResponseBody
//	public SysMenu findMenuById(Integer menuId) throws Exception{
////		String[] params = userId.split(":");
////		Integer uId = Integer.valueOf(params[1]);
//		SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
//		return sysMenu;
//	}
//	@RequestMapping(value = "findMenuPageList.do")
//	@ResponseBody
//	public List<SysMenu> findMenuPageList(Integer menuId){
//		List<SysMenu> sysMenus = null;
//		try {
//			//subjectService.findSubject(subject,sort,page);
//			sysMenus = sysMenuService.findAllPageSysMenuByParentId(menuId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return sysMenus;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "updateFunction.do")
//	@ResponseBody
//	public Map<String,Object> updateFunction(Integer id,String menuName,String menuCode,String menuUrl,String orderNo) throws Exception {
//		//TODO updateFunction验证操作session 是否 有权限操作 
//		Map<String,Object> msg=new HashMap<>();
//		boolean isReturn = false;
//		if (StringUtils.isBlank(menuName)) {
//			msg.put("msg","功能名称不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if(StringUtils.isBlank(menuCode) ){
//			msg.put("msg","功能编码不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if(StringUtils.isBlank(menuUrl) ){
//			msg.put("msg","功能路径不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if(StringUtils.isBlank(orderNo) ){
//			msg.put("msg","排序号不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			SysMenu sysMenu = sysMenuService.findSysMenuById(id);
//			sysMenu.setMenuName(menuName);
//			sysMenu.setMenuCode(menuCode);
//			sysMenu.setMenuUrl(menuUrl);
//			sysMenu.setOrderNo(orderNo);
//			try {
//				int i = 0;
//				i = sysMenuService.updateSysMenu(sysMenu);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","修改成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "addFunction.do")
//	@ResponseBody
//	public Map<String,Object> addFunction(Integer menuId,String menuName,String menuCode,String menuUrl,String orderNo) throws Exception {
//		//TODO addFunction验证操作session 是否 有权限操作 
//		Map<String,Object> msg=new HashMap<>();
//		
//		boolean isReturn = false;
//		if (StringUtils.isBlank(menuName)) {
//			msg.put("msg","功能名称不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if(StringUtils.isBlank(menuCode) ){
//			msg.put("msg","功能编码不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if(StringUtils.isBlank(menuUrl) ){
//			msg.put("msg","功能路径不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		else if(StringUtils.isBlank(orderNo) ){
//			msg.put("msg","排序号不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			int i = 0;
//			SysMenu sysMenu = new SysMenu();
//			sysMenu.setMenuName(menuName);
//			sysMenu.setMenuCode(menuCode);
//			sysMenu.setRigthCode(menuCode);
//			sysMenu.setMenuUrl(menuUrl);
//			sysMenu.setOrderNo(orderNo);
//			SysMenu sysMenu1 = sysMenuService.findSysMenuById(menuId);
//			sysMenu.setMenuLevel(sysMenu1.getMenuLevel()+1);
//			sysMenu.setMenuType(MenuType.PAGE.toString());
//			sysMenu.setParentId(menuId);
//			try {
//				i = sysMenuService.insertSysMenu(sysMenu);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","保存成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "deleteFunction.do")
//	@ResponseBody
//	public Map<String,Object> deleteFunction(Integer menuId) throws Exception {
//		//TODO deleteFunction验证操作session 是否 有权限操作 
//		Map<String,Object> msg=new HashMap<>();
//		
//		boolean isReturn = false;
//		if (menuId == null) {
//			msg.put("msg","功能ID不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			int i = 0;
//			try {
//				i = sysMenuService.deleteSysMenu(menuId);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","删除成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "deleteMenu.do")
//	@ResponseBody
//	public Map<String,Object> deleteMenu(Integer menuId) throws Exception {
//		//TODO deleteMenu验证操作session 是否 有权限操作 
//		Map<String,Object> msg=new HashMap<>();
//		
//		boolean isReturn = false;
//		if (menuId == null) {
//			msg.put("msg","功能ID不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			int i = 0;
//			try {
//				sysMenuService.deleteMenuAndChildren(menuId);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","删除成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "updateMenu.do")
//	@ResponseBody
//	public Map<String,Object> updateMenu(Integer menuId,String menuName,String menuCode,Integer parentId,String menuUrl,String orderNo) throws Exception {
//		//TODO updateMenu验证操作session 是否 有权限操作 
//		Map<String,Object> msg=new HashMap<>();
//		
//		boolean isReturn = false;
//		if (menuId == null) {
//			msg.put("msg","功能ID不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(menuName)) {
//			msg.put("msg","菜单名称不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(menuCode)) {
//			msg.put("msg","菜单编码不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (parentId == null) {
//			msg.put("msg","父菜单不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		else if (StringUtils.isBlank(orderNo)) {
//			msg.put("msg","排序号不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			int i = 0;
//			try {
//				SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
//				
//				sysMenu.setMenuName(menuName);
//				sysMenu.setMenuCode(menuCode);
//				sysMenu.setMenuUrl(menuUrl);
//				sysMenu.setParentId(parentId);
//				sysMenu.setOrderNo(orderNo);
//			
//				i = sysMenuService.updateSysMenu(sysMenu);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","修改菜单成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "addMenu.do")
//	@ResponseBody
//	public Map<String,Object> addMenu(String menuName,String menuCode,Integer parentId,String menuUrl,String orderNo) throws Exception {
//		//TODO addMenu验证操作session 是否 有权限操作 
//		Map<String,Object> msg=new HashMap<>();
//		
//		boolean isReturn = false;
//		if (StringUtils.isBlank(menuName)) {
//			msg.put("msg","菜单名称不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(menuCode)) {
//			msg.put("msg","菜单编码不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (parentId == null) {
//			msg.put("msg","父菜单不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		else if (StringUtils.isBlank(orderNo)) {
//			msg.put("msg","排序号不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			int i = 0;
//			try {
//				SysMenu sysMenu = new SysMenu();
//				sysMenu.setMenuName(menuName);
//				sysMenu.setMenuCode(menuCode);
//				sysMenu.setMenuUrl(menuUrl);
//				sysMenu.setParentId(parentId);
//				sysMenu.setOrderNo(orderNo);
//			
//				i = sysMenuService.insertSysMenu(sysMenu);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","增加菜单成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		
//		return msg;
//	}
//	
//	@RequestMapping(value = "/user.do")
//	public String user(ModelMap model, @RequestParam Map<String, String> params){
//		List<String> list = new ArrayList<String>();
//		String str = "";
//		try {
//			List<SysDept> sysDepts= sysDeptService.findAllSysDeptList();
//			list.add("{ id:'root', pId:0, name:'部门用户', open:true}");
//			for (SysDept sysDept : sysDepts) {
//				list.add("{ id:'dept:"+sysDept.getId()+"', pId:'root', name:'"+sysDept.getDeptName()+"'}");
//				for (ShiroUser user : sysDept.getUsers()) {
//					list.add("{ id:'user:"+user.getId()+"', pId:'dept:"+sysDept.getId()+"', name:'"+user.getRealName()+"'}");
//				}
//			}
//			str = StringUtils.join(list,",\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.put("deptUserTree", str);
//		return "sys/user";
//	}
//	
//	@RequestMapping(value = "findUserRolePrivilege.do")
//	@ResponseBody
//	public List<Node> findUserRolePrivilege(String userId) throws Exception {
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		System.out.println(" findUserRolePrivilege Json");
//		Node node = new Node();
//		List<Node> nodes = new ArrayList<>();
//		
//		try {
//			//List<ShiroRigth> shiroRigths = shiroRigthService.findAllShiroRigth();
//			//List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
//			List<ShiroRole> shiroRoles= shiroRoleService.findAllShiroRole();
//			List<UserRole> userRoles = userRoleService.findUserRoleByUserId(uId);//获取用户对应的角色
//			
//			
//			List<ShiroRole> srs = new ArrayList<>();//用户对应角色实体
//			for (UserRole userRole : userRoles) {
//				srs.add(userRole.getShiroRole());
//			}
//			
//			node = new Node("root","0","true","用户角色","true","true");
//			nodes.add(node);
//			for (ShiroRole shiroRole : shiroRoles) {
//				node = new Node();
//				node.setId(shiroRole.getId().toString());
//				String pId = "root";
////				if(sysMenu.getParentId() != null){
////					pId = sysMenu.getParentId().toString();
////				}
//				node.setpId(pId);
//				node.setIsParent("false");
//				node.setOpen("true");
//				node.setName(shiroRole.getRoleName());
//				node.setChecked("false");
//				for (ShiroRole sr : srs) {
//					if (shiroRole.getRoleCode().equals(sr.getRoleCode())) {
//						node.setChecked("true");
//						break;
//					}
//				}
//				nodes.add(node);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return nodes;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "saveUserRole.do")
//	@ResponseBody
//	public int saveUserRole(String userId,String roleId) throws Exception {
//		String[] roleIdArray = roleId.split(",");
//		
//		//TODO insertUserRigth验证操作session 是否 有权限操作 
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		int i = 0;
//		try {
//			i = userRoleService.saveUserRole(uId, roleIdArray);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return i;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "updateUser.do")
//	@ResponseBody
//	public Map<String,Object> updateUser(String userId,String userName,String realName,String email,String telNo) throws Exception {
//		Map<String,Object> msg=new HashMap<>();
//		boolean isReturn = false;
//		if (StringUtils.isBlank(userName)) {
//			msg.put("msg","用户名不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(realName)) {
//			msg.put("msg","真实名称不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//		//TODO saveRole验证操作session 是否 有权限操作 
//			String[] params = userId.split(":");
//			Integer uId = Integer.valueOf(params[1]);
//			int i = 0;
//			ShiroUser shiroUser = new ShiroUser();
//			shiroUser.setId(uId);
//			shiroUser.setUserName(userName);
//			shiroUser.setRealName(realName);
//			shiroUser.setEmail(email);
//			shiroUser.setTelNo(telNo);
//			try {
//				i = userService.updateUser(shiroUser);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","增加用户成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "addUser.do")
//	@ResponseBody
//	public Map<String,Object> addUser(String userName,String realName,String email,String telNo) throws Exception {
//		Map<String,Object> msg=new HashMap<>();
//		boolean isReturn = false;
//		if (StringUtils.isBlank(userName)) {
//			msg.put("msg","用户名不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(realName)) {
//			msg.put("msg","真实名称不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
//				    .getAuthentication()
//				    .getPrincipal();
//			String newPassMd5 = DigestUtils.md5Hex("88888888{"+userName+"}");
//			//TODO addRole验证操作session 是否 有权限操作 
//			int i = 0;
//			ShiroUser shiroUser = new ShiroUser();
//			shiroUser.setUserName(userName);
//			shiroUser.setPassword(newPassMd5);
//			shiroUser.setRealName(realName);
//			shiroUser.setEmail(email);
//			shiroUser.setTelNo(telNo);
////			shiroUser.setCreateOperator(userInfo.getUsername());
//			shiroUser.setCreateTime(new Date());
//			try {
//				i = userService.insertUser(shiroUser);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","增加用户成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		return msg;
//	}
//	
//	@RequestMapping(value = "/findUserById.do")
//	@ResponseBody
//	public ShiroUser findUserById(String userId) throws Exception{
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		ShiroUser shiroUser = userService.findUserById(uId);
//		return shiroUser;
//	}
//	@RequestMapping(value = "findUserMenuPrivilege.do")
//	@ResponseBody
//	public List<Node> findUserMenuPrivilege(String userId) throws Exception {
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		System.out.println(" findUserMenuPrivilege Json");
//		Node node = new Node();
//		List<Node> nodes = new ArrayList<>();
//		
//		try {
//			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
//			List<ShiroRigth> shiroRigths = shiroRigthService.findUserRolePrivilegeRigth(uId);//角色对应的权限
//			List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
//			List<ShiroRigth> shiroRigths2 = new ArrayList<>();
//			for (UserRigth userRigth : userRigths) {
//				shiroRigths2.add(userRigth.getShiroRigth());
//			}
//			List<ShiroRigth> srs1 = ListUtils.intersection(shiroRigths, shiroRigths2);//交集
//			List<ShiroRigth> srs2 = ListUtils.subtract(shiroRigths2, srs1);//相减
//			
//			shiroRigths = ListUtils.subtract(shiroRigths, srs1);//相减
//			List<ShiroRigth> srs = ListUtils.union(shiroRigths, srs2);
//			srs = new ArrayList(new HashSet(srs));//去掉重复的
//			
//			node = new Node("root","0","true","用户权限","true","true");
//			nodes.add(node);
//			for (SysMenu sysMenu : sysMenus) {
//				node = new Node();
//				node.setId(sysMenu.getId().toString());
//				String pId = "root";
//				if(sysMenu.getParentId() != null){
//					pId = sysMenu.getParentId().toString();
//				}
//				node.setpId(pId);
//				node.setIsParent("false");
//				node.setOpen("true");
//				node.setName(sysMenu.getMenuName());
//				node.setChecked("false");
//				for (ShiroRigth shiroRigth : srs) {
//					if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
//						node.setChecked("true");
//						break;
//					}
//					
//				}
//				
//				nodes.add(node);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return nodes;
//	}
//	@RequestMapping(value = "findUserPrivilege.do")
//	@ResponseBody
//	public List<Node> findUserPrivilege(Integer parentId,String userId) throws Exception {
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		System.out.println(" findUserPrivilege Json");
//		Node node = new Node();
//		List<Node> nodes = new ArrayList<>();
//		
//		try {
//			List<SysMenu> sysMenus = sysMenuService.findAllPageSysMenuByParentId(parentId);
//			List<ShiroRigth> shiroRigths = shiroRigthService.findUserWithRolesPrivilegeRigthByParentId(uId,parentId);//角色对应的权限
//			List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
//			List<ShiroRigth> shiroRigths2 = new ArrayList<>();
//			for (UserRigth userRigth : userRigths) {
//				shiroRigths2.add(userRigth.getShiroRigth());
//			}
//			List<ShiroRigth> srs1 = ListUtils.intersection(shiroRigths, shiroRigths2);//交集
//			List<ShiroRigth> srs2 = ListUtils.subtract(shiroRigths2, srs1);//相减
//			
//			shiroRigths = ListUtils.subtract(shiroRigths, srs1);//相减
//			List<ShiroRigth> srs = ListUtils.union(shiroRigths, srs2);
//			srs = new ArrayList(new HashSet(srs));//去掉重复的
//			
//			
//			for (SysMenu sysMenu : sysMenus) {
//				node = new Node();
//				node.setId(sysMenu.getId().toString());
//				node.setpId(parentId.toString());
//				node.setName(sysMenu.getMenuName());
//				node.setChecked("false");
//				node.setRigthCode(sysMenu.getMenuCode());
//				for (ShiroRigth shiroRigth : srs) {
//					if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
//						node.setChecked("true");
//						break;
//					}
//				}
//				
//				nodes.add(node);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return nodes;
//	}
////	@RequestMapping(value = "deleteUserRigth.do")
////	@ResponseBody
////	public int deleteUserRigth(String userId,String rigthCode) throws Exception {
////		
////		//TODO deleteUserRigth验证操作session 是否 有权限操作 
////		
////		String[] params = userId.split(":");
////		Integer uId = Integer.valueOf(params[1]);
////		int i = 0;
////		try {
////			ShiroRigth shiroRigth = shiroRigthService.findShiroRigthByRigthCode(rigthCode);
////			i = userRigthService.deleteUserRigth(uId, shiroRigth.getId());
////		} catch (Exception e) {
////			e.printStackTrace();
////		}	
////		return i;
////	}
////	
////	@RequestMapping(value = "insertUserRigth.do")
////	@ResponseBody
////	public int insertUserRigth(String userId,String rigthCode) throws Exception {
////		
////		//TODO insertUserRigth验证操作session 是否 有权限操作 
////		String[] params = userId.split(":");
////		Integer uId = Integer.valueOf(params[1]);
////		int i = 0;
////		try {
////			ShiroRigth shiroRigth = shiroRigthService.findShiroRigthByRigthCode(rigthCode);
////			i = userRigthService.insertUserRigth(uId, shiroRigth.getId());
////		} catch (Exception e) {
////			e.printStackTrace();
////		}	
////		return i;
////	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "saveUserRigth.do")
//	@ResponseBody
//	public Map<String,Object> saveUserRigth(String userId,Integer parentId,String rigthCode) throws Exception {
//		String[] rigthCodeArray = rigthCode.split(",");
//		
//		//TODO insertUserRigth验证操作session 是否 有权限操作 
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		int i = 0;
//		Map<String,Object> msg=new HashMap<>();
//		try {
//			i = userRigthService.saveUserRigth(uId,parentId,rigthCodeArray);
//			if (i>0) {
//				msg.put("state",true);
//				msg.put("msg","保存成功");
//			}else{
//				msg.put("state",true);
//				msg.put("msg","没有保存任何数据");
//			}
//		} catch (Exception e) {
//			//e.printStackTrace();
//			msg.put("state",false);
//			msg.put("msg",e.getMessage());
//		}	
//		return msg;
//	}
//	
//	@RequestMapping(value = "/role.do")
//	public String role(ModelMap model, @RequestParam Map<String, String> params){
//		List<String> list = new ArrayList<String>();
//		String str = "";
//		try {
//			List<ShiroRole> shiroRoles= shiroRoleService.findAllShiroRole();
//			list.add("{ id:'root', pId:0, name:'角色', open:true}");
//			for (ShiroRole shiroRole : shiroRoles) {
//				list.add("{ id:'role:"+shiroRole.getId()+"', pId:'root', name:'"+shiroRole.getRoleName()+"'}");
//			}
//			str = StringUtils.join(list,",\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.put("deptUserTree", str);
//		return "sys/role";
//	}
//	@RequestMapping(value = "/findRoleById.do")
//	@ResponseBody
//	public ShiroRole findRoleById(String roleId) throws Exception{
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		ShiroRole shiroRole = shiroRoleService.findShiroRoleById(rId);
//		return shiroRole;
//	}
//	@RequestMapping(value = "findShiroRolePrivilege.do")
//	@ResponseBody
//	public List<Node> findShiroRolePrivilege(String roleId) throws Exception {
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		System.out.println(" findShiroRolePrivilege Json");
//		Node node = new Node();
//		List<Node> nodes = new ArrayList<>();
//		
//		try {
//			//List<ShiroRigth> shiroRigths = shiroRigthService.findAllShiroRigth();
//			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
//			List<RoleRigth> roleRigths = roleRigthService.findRoleRigthByRoleId(rId);//获取角色对应的权限
//			
//			List<ShiroRigth> srs = new ArrayList<>();
//			for (RoleRigth roleRigth : roleRigths) {
//				srs.add(roleRigth.getShiroRigth());
//			}
//			
//			node = new Node("root","0","true","角色权限","true","true");
//			nodes.add(node);
//			for (SysMenu sysMenu : sysMenus) {
//				node = new Node();
//				node.setId(sysMenu.getId().toString());
//				String pId = "root";
//				if(sysMenu.getParentId() != null){
//					pId = sysMenu.getParentId().toString();
//				}
//				node.setpId(pId);
//				node.setIsParent("false");
//				node.setOpen("true");
//				node.setName(sysMenu.getMenuName());
//				node.setChecked("false");
//				for (ShiroRigth sr : srs) {
//					if (sysMenu.getMenuCode().equals(sr.getRigthCode())) {
//						node.setChecked("true");
//						break;
//					}
//				}
//				nodes.add(node);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return nodes;
//	}
//	
//	@RequestMapping(value = "findRolePrivilege.do")
//	@ResponseBody
//	public List<Node> findRolePrivilege(String roleId,Integer menuId,String parentId) throws Exception {
//		//TODO parentId 暂时未用
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		System.out.println(" findUserPrivilege Json");
//		Node node = new Node();
//		List<Node> nodes = new ArrayList<>();
//		
//		try {
//			List<SysMenu> sysMenus = sysMenuService.findAllPageSysMenuByParentId(menuId);
//			List<ShiroRigth> srs = shiroRigthService.findRolePrivilegeRigthByParentId(rId,menuId);//单个角色对应的权限
//			
////			if ("root".equals(parentId)) {
////				sysMenus.clear();
////				SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
////				sysMenus.add(sysMenu);
////			}
//			
//			for (SysMenu sysMenu : sysMenus) {
//				node = new Node();
//				node.setId(sysMenu.getId().toString());
//				node.setpId(menuId.toString());
//				node.setName(sysMenu.getMenuName());
//				node.setChecked("false");
//				node.setRigthCode(sysMenu.getMenuCode());
//				for (ShiroRigth shiroRigth : srs) {
//					if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
//						node.setChecked("true");
//						break;
//					}
//				}
//				
//				nodes.add(node);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return nodes;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "saveRoleRigth.do")
//	@ResponseBody
//	public Map<String,Object> saveRoleRigth(String roleId,Integer menuId,String rigthCode) throws Exception {
//		String[] rigthCodeArray = rigthCode.split(",");
//		
//		//TODO saveRoleRigth验证操作session 是否 有权限操作 
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		int i = 0;
//		Map<String,Object> msg=new HashMap<>();
//		try {
//			i = roleRigthService.saveRoleRigth(rId, menuId, rigthCodeArray);
//			if (i>0) {
//				msg.put("state",true);
//				msg.put("msg","保存角色权限成功");
//			}
//		} catch (Exception e) {
//			//e.printStackTrace();
//			msg.put("state",false);
//			msg.put("msg",e.getMessage());
//		}	
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "updateRole.do")
//	@ResponseBody
//	public Map<String,Object> updateRole(String roleId,String roleCode,String roleName,String roleRemake) throws Exception {
//		
//		//TODO saveRole验证操作session 是否 有权限操作 
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		int i = 0;
//		Map<String,Object> msg=new HashMap<>();
//		ShiroRole shiroRole = new ShiroRole();
//		shiroRole.setId(rId);
//		shiroRole.setRoleCode(roleCode);
//		shiroRole.setRoleName(roleName);
//		shiroRole.setRoleRemake(roleRemake);
//		shiroRole.setUpdateTime(new Date());
//		try {
//			i = shiroRoleService.updateShiroRole(shiroRole);
//			if (i>0) {
//				msg.put("state",true);
//				msg.put("msg","修改角色成功");
//			}
//		} catch (Exception e) {
//			//e.printStackTrace();
//			msg.put("state",false);
//			msg.put("msg",e.getMessage());
//		}	
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "addRole.do")
//	@ResponseBody
//	public Map<String,Object> addRole(String roleCode,String roleName,String roleRemake) throws Exception {
//		
//		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
//			    .getAuthentication()
//			    .getPrincipal();
//		
//		//TODO addRole验证操作session 是否 有权限操作 
////		String[] params = roleId.split(":");
////		Integer rId = Integer.valueOf(params[1]);
//		int i = 0;
//		Map<String,Object> msg=new HashMap<>();
//		ShiroRole shiroRole = new ShiroRole();
////		shiroRole.setId(rId);
//		shiroRole.setRoleCode(roleCode);
//		shiroRole.setRoleName(roleName);
//		shiroRole.setRoleRemake(roleRemake);
////		shiroRole.setCreateOperator(userInfo.getUsername());
//		shiroRole.setCreateTime(new Date());
//		try {
//			i = shiroRoleService.insertShiroRole(shiroRole);
//			if (i>0) {
//				msg.put("state",true);
//				msg.put("msg","修改角色成功");
//			}
//		} catch (Exception e) {
//			//e.printStackTrace();
//			msg.put("state",false);
//			msg.put("msg",e.getMessage());
//		}	
//		return msg;
//	}
//	
//	@RequestMapping(value = "/userRigth.do")
//	public String userRigth(ModelMap model, @RequestParam Map<String, String> params){
//		List<String> list = new ArrayList<String>();
//		String str = "";
//		try {
//			List<SysDept> sysDepts= sysDeptService.findAllSysDeptList();
//			list.add("{ id:'root', pId:0, name:'部门用户', open:true}");
//			for (SysDept sysDept : sysDepts) {
//				list.add("{ id:'dept:"+sysDept.getId()+"', pId:'root', name:'"+sysDept.getDeptName()+"'}");
//				for (ShiroUser user : sysDept.getUsers()) {
//					list.add("{ id:'user:"+user.getId()+"', pId:'dept:"+sysDept.getId()+"', name:'"+user.getRealName()+"'}");
//				}
//			}
//			str = StringUtils.join(list,",\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.put("deptUserTree", str);
//		return "sys/userRigth";
//	}
//	
//	//返回数据
//	
//	@RequestMapping("sysUserInfoList")
//	@ResponseBody
//	public Page<ShiroUser> getAllUser(@ModelAttribute("shiroUser")ShiroUser user,@ModelAttribute("sort")Sort sort,
//			@ModelAttribute("page")Page<ShiroUser> page) throws Exception{
//		userService.getUsers(user,sort,page);
//		return page;
//	}
//	@RequestMapping(value = "/sysRoleInfoList.do")
//	@ResponseBody
//	public Page<ShiroUser> sysRoleInfoList(@RequestParam Map<String, String> params){
//		return null;
//	}
//	@RequestMapping(value = "/toUpdatePwd.do")
//	public String toUpdatePwd(ModelMap model, @RequestParam Map<String, String> params){
//		return "sys/updatePwd";
//	}
//	
//	@RequestMapping(value = "/updatePwd.do")
//	@ResponseBody
//	public Map<String,Object> updatePwd(ModelMap model, @RequestParam Map<String, String> params){
//		Map<String,Object> msg=new HashMap<>();
//		String oldPass = params.get("oldPass");
//		String password = params.get("password");
//		String password2 = params.get("password2");
//		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
//			    .getAuthentication()
//			    .getPrincipal();
//		
//		ShiroUser shiroUser = userService.findUserById(userInfo.getId());
//		
//		String oldPassMd5 = DigestUtils.md5Hex(oldPass+"{"+shiroUser.getUserName()+"}");
//		boolean isReturn = false;
//		if (StringUtils.isBlank(oldPass)) {
//			msg.put("msg","旧密码不能为空");
//			isReturn = true;
//		}else if(StringUtils.isBlank(password) ||StringUtils.isBlank(password2) ){
//			msg.put("msg","新密码不能为空");
//			isReturn = true;
//		}
//		else if (!password.equals(password2)) {
//			msg.put("msg","两次输入的密码不相同");
//			isReturn = true;
//		}
//		else if (!shiroUser.getPassword().equals(oldPassMd5)) {
//			msg.put("msg","旧密码不正确");
//			isReturn = true;
//		}
//		 
//		if (!isReturn) {
//			int i = 0;
//			String newPassMd5 = DigestUtils.md5Hex(password+"{"+shiroUser.getUserName()+"}");
//			try {
//				i = userService.updateUserPwd(shiroUser.getId(), newPassMd5);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","修改密码成功");
//				}
//				else{
//					msg.put("msg","修改密码失败");
//				}
//			} catch (Exception e) {
//				log.error("修改密码失败",e);
//			}
//		}
//		return msg;
//	}
//	
//}
