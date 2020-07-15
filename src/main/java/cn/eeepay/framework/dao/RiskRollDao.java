package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.BlackOperLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskRoll;
import cn.eeepay.framework.model.RiskRollExport;
import cn.eeepay.framework.model.RiskRollList;

public interface RiskRollDao {

	@SelectProvider(type=SqlProvider.class,method="selectRollAllInfo")
	@ResultType(RiskRoll.class)
	List<RiskRoll> selectRollAllInfo(@Param("page")Page<RiskRoll> page,@Param("record")RiskRoll record);

	@SelectProvider(type=SqlProvider.class,method="selectBlackLogs")
	@ResultType(BlackOperLog.class)
	List<BlackOperLog> selectBlackLogs(@Param("page")Page<BlackOperLog> page,@Param("rollNo")String rollNo);

	@Insert("insert into black_oper_log(roll_no,black_type,create_time,operation_type,create_by,remark) values(#{record.rollNo},#{record.blackType}," +
			"#{record.createTime},#{record.operationType},#{record.createBy},#{record.remark})")
	int insertBlackLog(@Param("record")BlackOperLog record);

	@Select("SELECT  a.black_type as blackType,a.create_time as createTime,a.operation_type as operationType,a.create_by as createBy,a.remark  " +
			"FROM  black_oper_log a WHERE a.black_type in (1,4) and a.roll_no = #{merchantNo} order by a.create_time desc")
	@ResultType(BlackOperLog.class)
	List<BlackOperLog> selectBlackLogsByMerNo(@Param("merchantNo")String merchantNo);

	@SelectProvider(type=SqlProvider.class,method="selectMoreTime")
	@ResultType(BlackOperLog.class)
	List<BlackOperLog> selectMoreTime(@Param("page")Page<BlackOperLog> page,@Param("rId")String rId);

	@Select("select a.id,a.create_time as createTime from risk_event_detail a where a.r_id = #{rId} order by a.create_time asc")
	@ResultType(BlackOperLog.class)
	List<BlackOperLog> selectMoreTime2(@Param("rId")String rId);
	
	@SelectProvider(type=SqlProvider.class,method="selectRollAllInfoExport")
	@ResultType(RiskRollExport.class)
	List<RiskRollExport> selectRollAllInfoExport(@Param("record")RiskRoll record);
	
	@Select("select * from risk_roll where roll_belong=#{typeId}")
	@ResultType(RiskRoll.class)
	List<RiskRoll> selectRollInfo(@Param("typeId")int typeId);
	
	@SelectProvider(type=SqlProvider.class,method="selectRollList")
	@ResultType(RiskRollList.class)
	List<RiskRollList> selectRollList(@Param("page")Page<RiskRollList> page,@Param("record")RiskRollList record);
	
	@Insert("insert into risk_roll(roll_no,roll_name,roll_type,create_person,roll_belong,remark,roll_status,create_time,roll_user,roll_msg)"
			+ " values(#{record.rollNo},#{record.rollName},#{record.rollType},#{record.createPerson},#{record.rollBelong},#{record.remark},#{record.rollStatus},NOW(),#{record.rollUser},#{record.rollMsg })")
	int insertRoll(@Param("record")RiskRoll record);
	
	@Insert("insert into risk_roll_list(roll_no,roll_number,create_person) values(#{record.rollNo},#{record.rollNumber},#{record.createPerson})")
	int insertRollList(@Param("record")RiskRollList record);
	
	@Select("select * from risk_roll_list where roll_no=#{record.rollNo} and roll_number=#{record.rollNumber}")
	@ResultType(RiskRollList.class)
	RiskRollList selectInfoByrollNoAndMerNo(@Param("record")RiskRollList record);
	
	@Update("update risk_roll set roll_status=#{record.rollStatus},remark=#{record.remark} where id=#{record.id}")
	int updateRollStatus(@Param("record")RiskRoll record);
	
	@Update("update risk_roll set remark=#{record.remark} , roll_msg=#{record.rollMsg} where id=#{record.id}")
	int updateRollInfo(@Param("record")RiskRoll record);
	
	@Select("select * from risk_roll where id=#{id}")
	@ResultType(RiskRoll.class)
	RiskRoll selectRollDetail(@Param("id")int id);

	@Select("select * from risk_roll")
	@ResultType(RiskRoll.class)
	List<RiskRoll> selectRollAll();

	@Select("select * from risk_roll where roll_no=#{rollNo}")
	@ResultType(RiskRoll.class)
	List<RiskRoll> selectRollByRollNo(@Param("rollNo")String rollNo);
	
	@Select("select * from risk_roll where roll_no=#{rollNo} and roll_type = #{rulesInstruction}")
	@ResultType(RiskRoll.class)
	RiskRoll findRiskRollByRollNoAndRollType(@Param("rollNo")String rollNo, @Param("rulesInstruction")Integer rulesInstruction);

	@Select("select * from risk_roll where roll_no=#{rollNo} and roll_type = #{rulesInstruction} and roll_belong=#{rollBelong}")
	@ResultType(RiskRoll.class)
	RiskRoll findRiskRollByRollNoAndRollTypeAndBelongType(@Param("rollNo")String rollNo, @Param("rulesInstruction")Integer rulesInstruction, @Param("rollBelong")Integer rollBelong);
	
	@Select("select rr.*,sd.sys_name as rollTypeName from risk_roll rr left join sys_dict sd on sd.sys_value=rr.roll_type and sd.status='1'"
			+ " and sd.sys_key='RISK_ROLL_TYPE' where rr.roll_no=#{rollNo} and rr.roll_type=#{rollType} and rr.roll_belong=#{rollBelong}")
	@ResultType(RiskRoll.class)
	RiskRoll selectRollByRollNoAndType(@Param("rollNo")String rollNo, @Param("rollType")Integer rollType, @Param("rollBelong")Integer rollBelong);
	
	@Select("select * from risk_roll where roll_name=#{rollName}")
	@ResultType(RiskRoll.class)
	RiskRoll selectRollByRollName(@Param("rollName")String rollName);
	
	@Delete("delete from risk_roll where id=#{id}")
	int deleteRollListInfo(@Param("id")int id);
	
	@DeleteProvider(type = SqlProvider.class, method = "deleteBatch")
	int deleteBatch(@Param("ids")String ids);
	
	@Select("select id from risk_roll where roll_no=#{rollNo} and roll_type=#{rollType} and roll_belong=#{rollBelong} and roll_status='1'")
	@ResultType(String.class)
	String findBlacklist(@Param("rollNo")String rollNo,@Param("rollType")String rollType,@Param("rollBelong")String rollBelong);

	@Select(
			" select * from risk_roll where roll_no=#{rollNo} and roll_type=#{rollType} and roll_belong=#{rollBelong} "
	)
	@ResultType(RiskRoll.class)
	RiskRoll checkRollByRollNo(@Param("rollNo")String rollNo,@Param("rollType") Integer rollType, @Param("rollBelong")Integer rollBelong);


	 public class SqlProvider{
		 public String deleteBatch(Map<String,Object> param){
			 final String ids = (String) param.get("ids");
			 String sb = "";
			 sb = new SQL() {{
				 DELETE_FROM("risk_roll");
				 WHERE("id in ("+ids+")");
			 }}.toString();
			 System.out.println(sb);
			 return sb.toString();
		 }
		 
		 public String selectRollAllInfo(Map<String,Object> param){
	    		final RiskRoll rr=(RiskRoll)param.get("record");
	    		return new SQL(){{
	    			SELECT("rr.*,sd.sys_name as rollTypeName,bsu.user_name,smi.user_msg");
	    			FROM(" risk_roll rr left join sys_dict sd "
	    					+ "on sd.sys_value=rr.roll_type and sd.status='1' and sd.sys_key='RISK_ROLL_TYPE'"
	    					+ "left join boss_shiro_user bsu on rr.create_person = bsu.id " +
							" left join sys_msg_info smi on smi.msg_code = rr.roll_msg ");
	    			WHERE(" rr.roll_belong=#{record.rollBelong}");
	    			if(StringUtils.isNotBlank(rr.getRollNo())){
	    				rr.setRollNo(rr.getRollNo()+"%");
	    				WHERE(" rr.roll_no like #{record.rollNo}");
	    			}
	    			if (rr.getRollStatus() != null && !rr.getRollStatus().equals(-1)) {
	    				WHERE(" rr.roll_status=#{record.rollStatus}");
	    			}
	    			if(StringUtils.isNotBlank(rr.getRollName())){
	    				rr.setRollName(rr.getRollName()+"%");
	    				WHERE(" rr.roll_name like #{record.rollName}");
	    			}
	    			if(rr.getRollType()!=null && !rr.getRollType().equals(-1)){
	    				WHERE(" rr.roll_type=#{record.rollType}");
	    			}
	    			if (rr.getSdate() != null) {
	    				WHERE(" rr.create_time>=#{record.sdate}");
	    			}
	    			if (rr.getEdate() != null) {
	    				WHERE(" rr.create_time<=#{record.edate}");
	    			}
					if (rr.getUserName() != null) {
						rr.setUserName("%"+rr.getUserName()+"%");
						WHERE(" bsu.user_name like #{record.userName} ");
					}
	    			ORDER_BY(" rr.create_time desc ");
	    		}}.toString();
	    		
		 }

		 public String selectBlackLogs(Map<String,Object> param){
			 return new SQL(){{
				 SELECT(" a.black_type as blackType,a.create_time as createTime,a.operation_type as operationType,a.create_by as createBy,a.remark ");
				 FROM("black_oper_log a ");
				 WHERE(" a.roll_no = #{rollNo}");
				 ORDER_BY(" a.create_time desc ");
			 }}.toString();
		 }

		 public String selectMoreTime(Map<String,Object> param){
			 return new SQL(){{
				 SELECT(" a.id,a.create_time as createTime ");
				 FROM("risk_event_detail a ");
				 WHERE(" a.r_id = #{rId}");
				 ORDER_BY(" a.create_time desc ");
			 }}.toString();
		 }
		 
		 public String selectRollAllInfoExport(Map<String,Object> param){
	    		final RiskRoll rr=(RiskRoll)param.get("record");
	    		return new SQL(){{
	    			SELECT("rr.*,sd.sys_name as rollTypeName,smi.user_msg ");
	    			FROM(" risk_roll rr "
	    					+ " left join sys_dict sd on sd.sys_value=rr.roll_type and sd.status='1' and sd.sys_key='RISK_ROLL_TYPE'"
							+ " left join boss_shiro_user bsu on rr.create_person = bsu.id " +
							" left join sys_msg_info smi on smi.msg_code = rr.roll_msg ");

	    			WHERE(" rr.roll_belong=#{record.rollBelong}");
	    			if(StringUtils.isNotBlank(rr.getRollNo())){
	    				rr.setRollNo(rr.getRollNo()+"%");
	    				WHERE(" rr.roll_no like #{record.rollNo}");
	    			}
	    			if (rr.getRollStatus() != null && !rr.getRollStatus().equals(-1)) {
	    				WHERE(" rr.roll_status=#{record.rollStatus}");
	    			}
	    			if(StringUtils.isNotBlank(rr.getRollName())){
	    				rr.setRollName(rr.getRollName()+"%");
	    				WHERE(" rr.roll_name like #{record.rollName}");
	    			}
	    			if(rr.getRollType()!=null && !rr.getRollType().equals(-1)){
	    				WHERE(" rr.roll_type=#{record.rollType}");
	    			}
	    			if (rr.getSdate() != null) {
	    				WHERE(" rr.create_time>=#{record.sdate}");
	    			}
	    			if (rr.getEdate() != null) {
	    				WHERE(" rr.create_time<=#{record.edate}");
	    			}
					if (rr.getUserName() != null) {
						rr.setUserName("%"+rr.getUserName()+"%");
						WHERE(" bsu.user_name like #{record.userName} ");
					}

	    			ORDER_BY(" rr.create_time desc ");
	    		}}.toString();
	    		
		 }
		 
		 public String selectRollList(Map<String,Object> param){
	    		final RiskRollList rr=(RiskRollList)param.get("record");
	    		return new SQL(){{
	    			SELECT("*");
	    			FROM("risk_roll_list");
	    			WHERE(" roll_no=#{record.rollNo}");
	    			if(StringUtils.isNotBlank(rr.getRollNumber())){
	    				WHERE(" roll_number=#{record.rollNumber}");
	    			}
	    		}}.toString();
	    		
		 }
		 
	 }

	
}
