package cn.eeepay.framework.service.sysUser;

import java.util.List;

import cn.eeepay.framework.model.UserRight;

public interface UserRigthService {
//	int insertUserRigth(UserRigth userRigth) throws Exception;
	int insertUserRigth(Integer userId,Integer rigthId) throws Exception;
//	int deleteUserRigth(UserRigth userRigth) throws Exception;
	int deleteUserRigth(Integer userId,Integer rigthId) throws Exception;
	int saveUserRigth(Integer uId,Integer parentId, String[] rigthCodeArray) throws Exception;
	List<UserRight> findUserRigthByUserId(Integer userId) throws Exception;
	int deleteUserRigthByRigthId(Integer rigthId) throws Exception;
}
