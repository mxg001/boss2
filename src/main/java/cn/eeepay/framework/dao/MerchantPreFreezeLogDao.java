package cn.eeepay.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.TransInfoPreFreezeLog;

public interface MerchantPreFreezeLogDao {
	@Insert("insert into trans_info_pre_freeze_log("
			+ "merchant_no,pre_freeze_note,oper_log,"
			+ "oper_time,oper_id,oper_name)"
			+ " values(#{record.merchantNo},#{record.preFreezeNote},#{record.operLog},"
			+ "#{record.operTime},#{record.operId},#{record.operName})")
	int insertPreFreezeLog(@Param("record")TransInfoPreFreezeLog record);
	
	@Select("select * from trans_info_pre_freeze_log where merchant_no=#{merchantNo} order by oper_time desc ")
    @ResultType(TransInfoPreFreezeLog.class)
	List<TransInfoPreFreezeLog> selectByMerchantNo(@Param("merchantNo")String merchantNo);
}
