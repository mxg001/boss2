package cn.eeepay.framework.service.sysUser;

import java.util.List;

import cn.eeepay.framework.model.SysMenu;

public interface SysMenuService {
		int insertSysMenu(SysMenu sysMenu);
		int updateSysMenu(SysMenu sysMenu);
		int deleteSysMenu(Integer id);
		SysMenu findSysMenuById(Integer id) throws Exception;
		List<SysMenu> findAllSysMenu() throws Exception;
		List<SysMenu> findAllNotBlankUrlSysMenu() throws Exception;
		List<SysMenu> findAllPageSysMenuByParentId(Integer parentId)  throws Exception;
		List<SysMenu> findUserRolePrivilegeSysMenu(Integer userId) throws Exception;
		SysMenu findSysMenuByMenuCode(String menuCode) throws Exception;
		int deleteMenuAndChildren(Integer menuId);
		List<SysMenu> selectAllSysMenuAndChildrenList();
		List<SysMenu> findMenuPageList(Integer menuId);
}
