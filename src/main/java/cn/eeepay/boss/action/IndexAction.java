package cn.eeepay.boss.action;

import cn.eeepay.boss.system.CommonConst;
import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.BossOperLog;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.SysMenu;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.BossOperLogService;
import cn.eeepay.framework.service.MenuService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
@Controller
public class IndexAction {
	
	@Resource
	MenuService menuService;
	
	@Resource
	SysDictService sysDictService;

	@Resource
	BossOperLogService bossOperLogService;

	/**
	 * 保存登录或拿出日志
	 * @param request
	 * @param userInfo 当前登录用户信息
	 * @param login    true 登录,false 登出
	 */
	private void saveBossLogInfo(HttpServletRequest request, UserLoginInfo userInfo, Boolean login) {
		BossOperLog log =new BossOperLog();
		log.setOper_time(new Date());
		log.setUser_name(userInfo.getRealName());
		log.setUser_id(userInfo.getId());
		log.setOper_status(CommonConst.ONE);
		log.setOper_ip(ServletUtil.getClientIP(request));
		log.setRequest_params(userInfo.getRealName());
		log.setMethod_desc(login ? "BOSS用户登录" : "BOSS用户登出");
		log.setOper_code(login ? "BOSS_USER_LOGIN" : "BOSS_USER_LOGOUT");
		bossOperLogService.insert(log); //mj
	}

	@RequestMapping(value = "/loginSuccess")
	public String loginSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		saveBossLogInfo(request, userInfo, true);
		response.sendRedirect("welcome.do");
		return null;
	}

	@RequestMapping(value = "/logout.do")
	public String logoutSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserLoginInfo userInfo = (UserLoginInfo) auth.getPrincipal();
		saveBossLogInfo(request, userInfo, false);
		new SecurityContextLogoutHandler().logout(request, response, auth);
		response.sendRedirect("login.do?logout");
		return null;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/welcome.do")
	public String welcome(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		System.out.println("welcome");
		Object principalObj = null;
		String verNo = "2.0.001";
		try {
			principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//获取js的版本号
			SysDict sysDict = sysDictService.getByKey("VER_NUM");
			if(sysDict!=null){
				verNo = sysDict.getSysValue();
			}
			model.addAttribute("verNo", verNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(principalObj instanceof UserLoginInfo){
			final UserLoginInfo principal = (UserLoginInfo) principalObj;
			Set<String> permits = new HashSet<String>();
			for(GrantedAuthority item : principal.getAuthorities()){
				permits.add(item.getAuthority());
			}
			model.addAttribute("permits", permits);
			model.addAttribute("permitsJSON", JSON.toJSONString(permits));
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
			final Set<String> excludes = filter.getExcludes();
			excludes.add("authorities");
			excludes.add("password");
			excludes.add("accountNonExpired");
			excludes.add("accountNonLocked");
			excludes.add("credentialsNonExpired");
			excludes.add("enabled");
			model.addAttribute("principalJSON", JSON.toJSONString(principal,filter));
		}else{
			model.addAttribute("permits", Collections.EMPTY_SET);
			model.addAttribute("permitsJSON", "[]");
			model.addAttribute("principalJSON", "{}");
		}
		return "index";
	}
	// 权限控制相关页面

    @RequestMapping(value = "/login.do")
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/denied.do")
    public String deniedPage() {
        return "denied";
    }

	@DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping({"/views/common/navigation.html","/navigation.do"})
    public String navigation(ModelMap model, HttpServletRequest request, HttpServletResponse response){
    	try {
    		Object principalObj = null;
    		try {
    			principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		if(principalObj instanceof UserLoginInfo){
				final UserLoginInfo principal = (UserLoginInfo) principalObj;
	    		Set<String> permits = new HashSet<String>();
	    		for(GrantedAuthority item : principal.getAuthorities()){
	    			permits.add(item.getAuthority());
	    		}
//	    		System.out.println(permits);
				List<SysMenu> list = menuService.getSysMenuAndChildren();
//				List<Integer> menuIds = menuService.getMenuIdsByPermits(permits);

				boolean isSalesperson = principal.getExtended(CommonConst.isSalesperson);
				request.getSession().setAttribute(CommonConst.isSalesperson, isSalesperson);
				
				filterMenu(list, permits);
				model.addAttribute("menus", list);
    		}else{
    			model.addAttribute("menus", null);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "navigation";
    }
    
	private void filterMenu(Collection<SysMenu> list, Collection<String> menuIds) {
		Iterator<SysMenu> it = list.iterator();
		while(it.hasNext()){
			SysMenu menu = it.next();
//			if(!menuIds.contains(menu.getId().toString())){
			if(!menuIds.contains(menu.getMenuCode().toString())){
				it.remove();
				continue;
			}
			final List<SysMenu> children = menu.getChildren();
			if(children !=null && !children.isEmpty())
				filterMenu(children, menuIds);
		}
	}
}
