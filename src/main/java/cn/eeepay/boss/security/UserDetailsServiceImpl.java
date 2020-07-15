package cn.eeepay.boss.security;

import cn.eeepay.boss.system.CommonConst;
import cn.eeepay.framework.model.MenuInfo;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.MenuService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UserGrantService;
import cn.eeepay.framework.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 用户详细信息类
 * <p>
 * 负责以{@link UserDetails}方式提供用户信息
 * <p>
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    public UserService userService;
    @Resource
    public UserGrantService userGrantService;
    @Resource
    public SysDictService sysDictService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//    	String ip = request.getRemoteAddr();
//    	 String key = "blocked:"+ip;
//        if (userService.isBlocked(key)) {
//            throw new RuntimeException("blocked");
//        }

        UserLoginInfo userInfo = null;
//    	UserInfo user = new UserInfo();
//    	user.setUserName(userName);
//		Page<UserInfo> page = new Page<>(1, 1);
//		UserInfo shiroUser = page.getResult().get(0);
        UserInfo shiroUser = userService.selectUserByUserName(userName);

        try {
            if (shiroUser != null) {
                Integer uId = shiroUser.getId();
                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                List<MenuInfo> userRigths = userGrantService.getUserAllMenu(uId);//用户拥有的所有菜单ID
                for (MenuInfo sr : userRigths) {
                    if (sr != null)
//        				authorities.add(new SimpleGrantedAuthority(sr.getId().toString()));
                        authorities.add(new SimpleGrantedAuthority(sr.getMenuCode()));
                }

                userInfo = new UserLoginInfo(shiroUser.getUserName(), shiroUser.getPassword(), authorities);
                userInfo.setId(shiroUser.getId());
                userInfo.setRealName(shiroUser.getRealName());
                userInfo.setTelNo(shiroUser.getTelNo());
                userInfo.setEmail(shiroUser.getEmail());
                userInfo.setStatus(shiroUser.getStatus());
                userInfo.setTheme(shiroUser.getTheme());
                userInfo.setExtended(CommonConst.isSalesperson, false);
                userInfo.setDeptId(shiroUser.getDeptId());

                String sale_role_code = sysDictService.getByKey("SALESPERSON_ROLE").getSysValue();
                List<RightInfo> userRoles = userService.getRightsByUser(userInfo.getId());
                String right_code;
                for (RightInfo rightInfo: userRoles) {
                    right_code = rightInfo.getRightCode();
                    if(sale_role_code.contains("["+right_code+"]")){
                        //标记是否是销售
                        userInfo.setExtended(CommonConst.isSalesperson, true);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }
}
