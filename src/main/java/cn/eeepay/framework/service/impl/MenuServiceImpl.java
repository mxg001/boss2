package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.dao.MenuDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MenuInfo;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.SysMenu;
import cn.eeepay.framework.service.MenuService;

@Service("menuService")
@Transactional
public class MenuServiceImpl implements MenuService {

	@Resource
	private MenuDao menuDao;

	@Override
	public List<MenuInfo> getAllMenuAndChildren() {
		return menuDao.getAllMenuAndChildren();
	}
	
	@Override
	public List<SysMenu> getSysMenuAndChildren() {
		return menuDao.getSysMenuAndChildren();
	}

	@Override
	public List<SysMenu> selectMenuByCondition(Page<SysMenu> page, SysMenu sysMenu) {
		return menuDao.selectMenuByCondition(page, sysMenu);
	}

	@Override
	public Map<String, Object> insertMenu(MenuInfo menu) {
		Map<String, Object> msg = new HashMap<>();
		//检查menu_code的唯一性
		if(menu!=null){
			MenuInfo result = menuDao.checkMenuCode(menu.getMenuCode());
			if(result!=null){
				msg.put("status", false);
				msg.put("msg", "菜单编码已存在");
				return msg;
			}
		}
		int num = menuDao.insertMenu(menu);
		if(num==1){
			msg.put("status", true);
			msg.put("msg", "添加成功！");
		}
		return msg;
	}

	@Override
	public Map<String, Object> updateMenu(MenuInfo menu) {
		Map<String, Object> msg = new HashMap<>();
		//设置排序
//		if(menu!=null){
//			MenuInfo parent = null;
//			if(menu.getParentId()!=null && menu.getParentId()!=0){
//				parent = menuDao.getParent(menu.getParentId());
//				if(parent == null){
//					msg.put("status", false);
//					msg.put("msg", "上级不存在");
//					return msg;
//				}
//			}
//			if(menu.getMenuLevel()!=0 && parent!=null){
//				menu.setOrderNo(menu.getOrderNo()+parent.getOrderNo()*100);
//			}
//			if(menu.getMenuLevel()==0){
//				menu.setOrderNo(menu.getOrderNo()*1000000);
//			}
//			else if(menu.getMenuLevel()==1){
//				menu.setOrderNo(parent.getOrderNo() + menu.getOrderNo()*1000);
//			} else if(menu.getMenuLevel()==2){
//				menu.setOrderNo(parent.getOrderNo() + menu.getOrderNo());
//			}
//		}
		int num = menuDao.updateMenu(menu);
		if(num==1){
			msg.put("status", true);
			msg.put("msg", "修改成功！");
		}
		return msg;
	}

	@Override
	public int deleteMenus(Integer menuId) {
//		menuDao.deleteUserRightByMenuId(menuId);
//		menuDao.deleteRoleRightByMenuId(menuId);
		menuDao.deleteRightByMenuId(menuId);
		return menuDao.deleteMenus(menuId);
	}

	@Override
	public List<RightInfo> getRightsByMenu(Integer id) {
		return menuDao.getRightsByMenu(id);
	}

	@Override
	public List<MenuInfo> getAllMenu() {
		return menuDao.getAllMenu();
	}

	@Override
	public List<Integer> getMenuIdsByPermits(Collection<String> permits) {
		return menuDao.getMenuIdsByPermits('\''+StringUtils.join(permits, "','")+'\'');
	}

	@Override
	public List<MenuInfo> getAllPage() {
		return menuDao.getAllPage();
	}

	@Override
	public List<MenuInfo> findMenuPageList(Integer menuId) {
		return menuDao.findMenuPageList(menuId);
	}

	@Override
	public int updateStatus(MenuInfo info) {
		return menuDao.updateStatus(info);
	}

	@Override
	public List<MenuInfo> getMenuByParent(Integer parentId) {
		return menuDao.getMenuByParent(parentId);
	}

}
