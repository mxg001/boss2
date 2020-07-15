package cn.eeepay.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.TransInfoFreezeNewLog;

public interface TransInfoFreezeNewLogDao {

	@Select("select * from trans_info_freeze_new_log where order_no=#{orderNo} ORDER BY oper_time desc")
	@ResultType(TransInfoFreezeNewLog.class)
	List<TransInfoFreezeNewLog> queryByOrderNo(@Param("orderNo")String orderNo);
	
	@Insert("insert into trans_info_freeze_new_log(order_no,oper_type,oper_reason,oper_time,oper_id,oper_name)"
			+ " values(#{record.orderNo},#{record.operType},#{record.operReason},#{record.operTime},#{record.operId},#{record.operName})")
	int insertInfo(@Param("record")TransInfoFreezeNewLog record);
}
