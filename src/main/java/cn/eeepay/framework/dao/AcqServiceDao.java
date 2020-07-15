package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.model.AcqServiceRate;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface AcqServiceDao {
    
    @Select("select DISTINCT service_type from acq_service where acq_id=#{acqId}")
    @ResultType(AcqService.class)
    List<AcqService> selectBoxAllInfo(@Param("acqId")String acqId);
    
    @Select("select * from acq_service where acq_id=#{acqId}")
    @ResultType(AcqService.class)
    List<AcqService> selectBox(@Param("acqId")String acqId);
    
    @Select("select * from acq_service where id=#{id}")
    @ResultType(AcqService.class)
    AcqService selectInfo(@Param("id")Integer id);
    
    @Select("select * from acq_service_rate where id=#{id} and card_rate_type=#{cardType}")
    @ResultType(AcqServiceRate.class)
    AcqServiceRate selectAsrInfo(@Param("id")Integer id,@Param("cardType")Integer cardType);
    
    @Select("select * from acq_service")
    @ResultType(AcqService.class)
    List<AcqService> selectAllAcqService();
    
    @Select("SELECT\n" + 
    		"	a.*\n" + 
    		"FROM\n" + 
    		"	acq_service a\n" + 
    		"WHERE\n" + 
    		"	a.periodicity_start_time IS NOT NULL\n" + 
    		"AND a.periodicity_start_time IS NOT NULL")
    @ResultType(AcqService.class)
	List<AcqService> selectByPeriodicityTime();
    
    @Select("SELECT\n" + 
    		"	count(*)\n" + 
    		"FROM\n" + 
    		"	acq_service a\n" + 
    		"WHERE\n" + 
    		"	a.id = #{serviceId}")
    @ResultType(Integer.class)
    Integer countByServiceId(Integer serviceId);
    
    @Select("SELECT a.service_name FROM acq_service a WHERE a.id = #{serviceId}")
    @ResultType(String.class)
	String findServiceNameByServiceId(Integer serviceId);
}