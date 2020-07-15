package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import cn.eeepay.framework.model.WarningSet;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预警数据设置DAO
 * 
 * @author Qiujian
 *
 */
public interface WarningSetDao {
	
	@Select("SELECT\r\n" + 
			"	w.*\r\n" + 
			"FROM\r\n" + 
			"	warning_set w\r\n" + 
			"WHERE\r\n" + 
			"	w.service_id = #{serviceId}"
			+ " and  w.status = #{status}"
			+ " and  w.warn_status = 1"
			+ "   and w.warn_time_type = 1 limit 1 ")
	@ResultType(WarningSet.class)
	WarningSet findByServiceId(@Param("serviceId")Integer serviceId,@Param("status")Integer status);

	@Select("select * from warning_set where service_id = #{serviceId} and  status = #{status}" +
			" and  warn_status = 1 and warn_time_type = 2 and" +
			" case when warn_end_time > warn_start_time then #{nowDate} >= warn_start_time" +
			" and #{nowDate} < warn_end_time" +
			" else (#{nowDate} >= warn_start_time or #{nowDate} < warn_end_time) end " +
			" limit 1")
	@ResultType(WarningSet.class)
	WarningSet findByServiceIdAndDate(@Param("serviceId") Integer serviceId,@Param("nowDate") Date nowDate,@Param("status") Integer status);
	
	@Select("SELECT count(*) FROM warning_set w WHERE w.service_id = #{serviceId} and  w.status = #{status}")
	@ResultType(Integer.class)
	Integer countByServiceId(@Param("serviceId")Integer serviceId,@Param("status")Integer status);
	
	@Insert("INSERT INTO warning_set ( service_id,service_name, exception_number,failur_exception_number, status,warn_time_type," +
            "warn_start_time,warn_end_time,warn_status ) " +
            "VALUES" +
            " ( #{serviceId},#{serviceName},#{exceptionNumber},#{failurExceptionNumber},#{status},#{warnTimeType}" +
            ",#{warnStartTime},#{warnEndTime},#{warnStatus})")
	int insert(WarningSet info);
	
	@Update("UPDATE warning_set w_s SET w_s.exception_number = #{exceptionNumber},w_s.failur_exception_number = #{failurExceptionNumber}," +
            "w_s.warn_time_type = #{warnTimeType}, w_s.warn_start_time = #{warnStartTime}," +
            "w_s.warn_end_time = #{warnEndTime}, w_s.warn_status = #{warnStatus} WHERE w_s.id = #{id} ")
    int update(WarningSet info);

	@SelectProvider(type = SqlProvider.class, method = "selectPage")
	@ResultType(WarningSet.class)
	List<WarningSet> selectPage(@Param("baseInfo") WarningSet baseInfo, Page<WarningSet> page);

//    @SelectProvider(type = SqlProvider.class, method = "checkWarnTimeType")
//    @ResultType(Integer.class)
//    int checkWarnTimeType(@Param("info") WarningSet info);

    @SelectProvider(type = SqlProvider.class, method = "checkOtherWarnTime")
    @ResultType(Integer.class)
    int checkOtherWarnTime(@Param("info") WarningSet info);

    @SelectProvider(type = SqlProvider.class, method = "checkWarnTime")
    @ResultType(Integer.class)
    int checkWarnTime(@Param("info") WarningSet info);

    @Update("update warning_set set warn_status = #{warnStatus} where id = #{id} and warn_status <> 2")
	int updateWarnStatus(WarningSet baseInfo);

	@Update("update warning_set set warn_status = #{warnStatus} where id = #{id}")
	int deleteWarning(WarningSet baseInfo);

	@SelectProvider(type = SqlProvider.class, method = "selectSettlePage")
	@ResultType(WarningSet.class)
	List<WarningSet> selectSettlePage(@Param("baseInfo")WarningSet baseInfo, Page<WarningSet> page);

	@SelectProvider(type = SqlProvider.class, method = "checkWarnStartEndTime")
	@ResultType(Integer.class)
	int checkWarnStartEndTime(@Param("info") WarningSet info);


	class SqlProvider{

		public String selectPage(Map<String, Object> param){
			WarningSet baseInfo = (WarningSet) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("ws.id,ws.service_id,ws.warn_time_type");
			sql.SELECT("ws.warn_start_time,ws.warn_end_time");
			sql.SELECT("ws.exception_number,ws.warn_status,ws.status");
			sql.SELECT("acqs.service_name,acqs.acq_enname,acqs.service_type");
			sql.FROM("warning_set ws");
			sql.LEFT_OUTER_JOIN("acq_service acqs on acqs.id = ws.service_id");
			if(baseInfo.getServiceId() != null){
				sql.WHERE("ws.service_id = #{baseInfo.serviceId}");
			}
			if(baseInfo.getAcqId() != null){
				sql.WHERE("acqs.acq_id = #{baseInfo.acqId}");
			}
			if(StringUtils.isNotBlank(baseInfo.getServiceType())){
				sql.WHERE("acqs.service_type = #{baseInfo.serviceType}");
			}
			if(StringUtils.isNotBlank(baseInfo.getServiceName())){
				baseInfo.setServiceName(baseInfo.getServiceName() + "%");
				sql.WHERE("acqs.service_name like #{baseInfo.serviceName}");
			}
			if(baseInfo.getWarnStatus() != null){
				sql.WHERE("ws.warn_status = #{baseInfo.warnStatus}");
			}
			if(baseInfo.getStatus() != null){
				sql.WHERE("ws.status = #{baseInfo.status}");
			} else {
				sql.WHERE("ws.status = 1");
			}
			sql.WHERE("ws.warn_status <> 2");
//			sql.ORDER_BY("ws.service_id desc");
			return sql.toString();
		}

		public String selectSettlePage(Map<String, Object> param){
			WarningSet baseInfo = (WarningSet) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("ws.id,ws.service_id,ws.warn_time_type");
			sql.SELECT("ws.warn_start_time,ws.warn_end_time");
			sql.SELECT("ws.exception_number,ws.failur_exception_number,ws.warn_status,ws.status");
			sql.SELECT("acqs.service_name,acqs.acq_enname,acqs.service_type");
			sql.FROM("warning_set ws");
			sql.LEFT_OUTER_JOIN("out_account_service acqs on acqs.id = ws.service_id");
			if(baseInfo.getServiceId() != null){
				sql.WHERE("ws.service_id = #{baseInfo.serviceId}");
			}
			if(baseInfo.getAcqId() != null){
				sql.WHERE("acqs.acq_org_id = #{baseInfo.acqId}");
			}
			if(StringUtils.isNotBlank(baseInfo.getServiceType())){
				sql.WHERE("acqs.service_type = #{baseInfo.serviceType}");
			}
			if(StringUtils.isNotBlank(baseInfo.getServiceName())){
				baseInfo.setServiceName(baseInfo.getServiceName() + "%");
				sql.WHERE("acqs.service_name like #{baseInfo.serviceName}");
			}
			if(baseInfo.getWarnStatus() != null){
				sql.WHERE("ws.warn_status = #{baseInfo.warnStatus}");
			}
			if(baseInfo.getStatus() != null){
				sql.WHERE("ws.status = #{baseInfo.status}");
			} else {
				sql.WHERE("ws.status = 1");
			}
			sql.WHERE("ws.warn_status <> 2");
//			sql.ORDER_BY("ws.service_id desc");
			return sql.toString();
		}

//        public String checkWarnTimeType(Map<String, Object> param){
//            WarningSet info = (WarningSet) param.get("info");
//            SQL sql = new SQL();
//            sql.SELECT("count(*)");
//            sql.FROM("warning_set");
//            sql.WHERE("service_id = #{info.serviceId}");
//            sql.WHERE("warn_time_type = 1");
//            sql.WHERE("status = #{info.status}");
//            sql.WHERE("warn_status <> 2");
//            if(info.getId() != null){
//                sql.WHERE("id <> #{info.id}");
//            }
//            System.out.println(sql);
//            return sql.toString();
//        }

        public String checkOtherWarnTime(Map<String, Object> param){
            WarningSet info = (WarningSet) param.get("info");
            SQL sql = new SQL();
            sql.SELECT("count(*)");
            sql.FROM("warning_set");
            sql.WHERE("service_id = #{info.serviceId}");
            sql.WHERE("status = #{info.status}");
            sql.WHERE("warn_time_type = 1");
            sql.WHERE("warn_status <> 2");
            if(info.getId() != null){
                sql.WHERE("id <> #{info.id}");
            }
            System.out.println(sql);
            return sql.toString();
        }

		public String checkWarnTime(Map<String, Object> param){
		    WarningSet info = (WarningSet) param.get("info");
		    SQL sql = new SQL();
		    sql.SELECT("count(*)");
		    sql.FROM("warning_set");
		    sql.WHERE("service_id = #{info.serviceId}");
            sql.WHERE("status = #{info.status}");
            sql.WHERE("warn_status <> 2");
            sql.WHERE("warn_time_type = 2");
			//如果数据库存在22:00:00 ~ 06:00:00
			//如果数据库不存在22:00:00 ~ 06:00:00
			//分两种情况进行查询判断
			sql.WHERE("(case when warn_end_time > warn_start_time then" +
					" !(#{info.warnStartTime} >= warn_end_time or #{info.warnEndTime} <= warn_start_time) " +
					" ELSE !(#{info.warnEndTime} <= warn_start_time and #{info.warnStartTime} >= warn_end_time)END)");
		    if(info.getId() != null){
		        sql.WHERE("id <> #{info.id}");
            }
            System.out.println(sql);
		    return sql.toString();
        }

        public String checkWarnStartEndTime(Map<String, Object> param){
			WarningSet info = (WarningSet) param.get("info");
			SQL sql = new SQL();
			sql.SELECT("count(*)");
			sql.FROM("warning_set");
			sql.WHERE("service_id = #{info.serviceId}");
			sql.WHERE("status = #{info.status}");
			sql.WHERE("warn_status <> 2");
			sql.WHERE("warn_time_type = 2");
			sql.WHERE("case when warn_start_time > warn_end_time then 1=1" +
			" else !(#{info.warnEndTime} <= warn_start_time and #{info.warnStartTime} >= warn_end_time) end");
			if(info.getId() != null){
				sql.WHERE("id <> #{info.id}");
			}
			System.out.println(sql);
			return sql.toString();
		}
	}
}
