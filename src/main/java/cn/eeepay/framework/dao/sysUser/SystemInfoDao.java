package cn.eeepay.framework.dao.sysUser;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.ShiroUser;
import cn.eeepay.framework.model.SystemInfo;
/**
 * 系统日切日终正常等状态表
 * @author Administrator
 *
 */
public interface SystemInfoDao {
	
	@Select( "select * from system_info where current_date = #{systemInfo.currentDate}")
	@ResultMap("cn.eeepay.framework.dao.SystemInfoMapper.BaseResultMap")
	SystemInfo findSystemInfo(@Param("systemInfo")SystemInfo systemInfo);
	
	@Update("update system_info set status = #{systemInfo.status},`current_date`= #{systemInfo.currentDate},parent_trans_date= #{systemInfo.parentTransDate},next_trans_date= #{systemInfo.nextTransDate} where id = #{systemInfo.id}")
	int updateSystemInfo(@Param("systemInfo")SystemInfo systemInfo);
}
