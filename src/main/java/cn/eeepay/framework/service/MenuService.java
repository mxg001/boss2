package cn.eeepay.framework.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MenuInfo;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.SysMenu;

public interface MenuService {

	List<MenuInfo> getAllMenuAndChildren();
	
	List<SysMenu> getSysMenuAndChildren();

	List<SysMenu> selectMenuByCondition(Page<SysMenu> page, SysMenu sysMenu);
	
	//新增菜单
	Map<String, Object> insertMenu(MenuInfo menu);
	//修改菜单
	Map<String, Object> updateMenu(MenuInfo menu);
	//删除删除
	int deleteMenus(Integer menuId);

	List<RightInfo> getRightsByMenu(Integer id);

	List<MenuInfo> getAllMenu();
	
	List<Integer> getMenuIdsByPermits(Collection<String> permits);

	List<MenuInfo> getAllPage();

	List<MenuInfo> findMenuPageList(Integer menuId);

	int updateStatus(MenuInfo info);

	List<MenuInfo> getMenuByParent(Integer parentId);
}
