package cn.eeepay.boss.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.SysMenu;
import cn.eeepay.framework.service.MenuService;
import cn.eeepay.framework.util.AntUrlPathMatcher;
import cn.eeepay.framework.util.UrlMatcher;

/**
 * 资源元数据类
 *
 * 负责提供资源数据信息
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private final Logger logger = LoggerFactory.getLogger(SecurityMetadataSource.class);

    private UrlMatcher urlMatcher = new AntUrlPathMatcher();
    @Resource
	public MenuService sysMenuService;
    //private ResourceDao resourceDao = new ResourceDao();
    private static Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

    public SecurityMetadataSource() {
        //resourceMap = resourceDao.getResourceMap();
       
    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws  IllegalArgumentException {
		try {
			List<SysMenu> sysMenus = sysMenuService.selectMenuByCondition(new Page<SysMenu>(1,Integer.MAX_VALUE), null);
	     	for (final SysMenu sysMenu : sysMenus) {
	     		String url = sysMenu.getMenuUrl();
	     		if (StringUtils.isNotBlank(url) && !url.startsWith("#")) {
//					url = "/"+ url + "**";
					url = "/"+ url;
				}else{
					continue;
				}
	     		final ArrayList<ConfigAttribute> value = new ArrayList<ConfigAttribute>();
//	     		for(RightInfo item : sysMenuService.getRightsByMenu(sysMenu.getId())){
//	     			value.add(new SecurityConfig(item.getRightCode()));
//	     		}
//	     		value.add(new SecurityConfig(sysMenu.getId().toString()));
	     		value.add(new SecurityConfig(sysMenu.getMenuCode()));
				resourceMap.put(url, value);
	 		}
		
	    	String url = ((FilterInvocation)object).getRequestUrl();
	     	if(!StringUtil.isEmpty() && url.indexOf("?") > -1) {
				url = url.split("\\?")[0];
			}
			if(url.contains("export") && !url.contains("exportManage")){
				UserLoginInfo loginUser = CommonUtil.getLoginUser();
				logger.info("current_user_export,当前用户导出报表{}",url);
				logger.info("当前用户{},{}",loginUser.getId(),loginUser.getRealName());
			}
	        for(String resURL : resourceMap.keySet()) {
	            if (resURL != null && urlMatcher.pathMatchesUrl(resURL, url)) {
	                return resourceMap.get(resURL);
	            }
	        }
			} catch (Exception e) {
				logger.error("权限数据异常", e);
			}
	        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
