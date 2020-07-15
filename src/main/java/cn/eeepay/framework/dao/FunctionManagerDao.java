package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.function.AppShowConfig;
import cn.eeepay.framework.model.function.FunctionTeam;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface FunctionManagerDao {

	@Select(" select * from function_manage where show_status='1' ")
	@ResultType(FunctionManager.class)
	List<FunctionManager> selectFunctionManagers();

	@Update("update function_manage set function_switch=#{functionSwitch} where id=#{id}")
	int updateFunctionSwitch(FunctionManager functionManager);

	@Update("update vas_rate set status=#{status} where id=#{id}")
	int updateVasRateStatus(VasRate info);

	@Update("update function_manage_config set status=#{status} where id=#{id}")
	int updateFunctionManageConfigStatus(FunctionTeam info);

	@Update("update function_manage_config set bak_json=#{bakJson} where id=#{id}")
	int updateFunctionManageConfigBak(FunctionTeam info);

	@Update("update function_manage set agent_control=#{agentControl} where id=#{id}")
	int updateAgentControl(FunctionManager functionManager);

	@Select(" select * from function_manage where id=#{id}")
	@ResultType(FunctionManager.class)
	FunctionManager getFunctionManager(int id);

	@Select(" select * from function_manage where function_number=#{funcNum}")
	@ResultType(FunctionManager.class)
	FunctionManager getFunctionManagerByNum(@Param("funcNum") String funcNum);

	@Select(" select * from vas_rate where id=#{id}")
	@ResultType(VasRate.class)
	VasRate getVasRateById(@Param("id") int id);

	@Select("select * from industry_switch order by start_time")
	@ResultType(IndustrySwitch.class)
	List<IndustrySwitch> findAll();
	
	@Insert("INSERT INTO `industry_switch`( `acq_merchant_type`, `start_time`, `end_time`, `create_time`) VALUES ( #{acqMerchantType},STR_TO_DATE(#{startTime}, '%H:%i') ,STR_TO_DATE(#{endTime}, '%H:%i'),#{createTime})")
	int industrySwitchSave(IndustrySwitch data);
	
	@Insert("INSERT INTO `industry_switch`( `acq_merchant_type`, `start_time`, `end_time`, `create_time`) VALUES ( #{acqMerchantType},STR_TO_DATE(#{startTime}, '%H:%i') ,STR_TO_DATE(#{endTime}, '%H:%i:%s'),#{createTime})")
	int industrySwitchSaveData(IndustrySwitch data);

	@Delete("delete from industry_switch where id=#{id}")
	int industrySwitchDelete(Long id);

	@Select("SELECT " + 
			"	count(*)  " + 
			"FROM " + 
			"	industry_switch  " + 
			"WHERE " + 
			"	( STR_TO_DATE( #{startTime}, '%H:%i' ) < end_time AND STR_TO_DATE(  #{startTime}, '%H:%i' ) >= start_time )  " + 
			"	OR ( STR_TO_DATE( #{endTime}, '%H:%i' ) > start_time AND STR_TO_DATE( #{endTime}, '%H:%i' ) <= end_time ) OR (STR_TO_DATE( #{startTime}, '%H:%i' )<=start_time and STR_TO_DATE( #{endTime}, '%H:%i' ) >end_time )")
	long industrySwitchCount(@Param("startTime")String startTime, @Param("endTime")String endTime);

	@Insert("INSERT INTO `industry_switch`(`id`, `acq_merchant_type`, `start_time`, `end_time`, `create_time`) VALUES ( #{id},#{acqMerchantType},STR_TO_DATE(#{startTime}, '%H:%i') ,STR_TO_DATE(#{endTime}, '%H:%i'),#{createTime})")
	int industrySwitchSaveAll(IndustrySwitch data);

	@Update("update sys_dict set sys_value=#{sysValue} where sys_key=#{sysKey}")
	int updateSysDictValue(@Param("sysKey")String string, @Param("sysValue")Integer industrySwitch);

	@Insert("INSERT INTO `industry_switch`(`id`, `acq_merchant_type`, `start_time`, `end_time`, `create_time`) VALUES ( #{id},#{acqMerchantType},STR_TO_DATE(#{startTime}, '%H:%i') ,STR_TO_DATE(#{endTime}, '%H:%i:%s'),#{createTime})")
	int industrySwitchSaveAllData(IndustrySwitch data);

	@Update("update function_manage set limit_sys= #{limitSys},function_name = #{functionName}, remark = #{remark} where id = #{id}")
    int updateBaseInfo(FunctionManager baseInfo);


	//查询开关组织配置信息
	@Select(
			"select * from function_manage_config where function_number=#{functionNumber} ORDER BY team_id "
	)
	List<FunctionTeam> getFunctionTeamList(@Param("functionNumber")String functionNumber);

	//查询开关组织配置信息
	@Select(
			"select * from function_manage_config where function_number=#{functionNumber} and id=#{id} "
	)
	FunctionTeam getFunctionTeamById(@Param("functionNumber")String functionNumber,@Param("id")int id);

	//查询服务信息
	@Select(
			"select vr.*,ti.team_name,tie.team_entry_name " +
					"from vas_rate vr " +
					"left join team_info ti on vr.team_id=ti.team_id " +
					"left join team_info_entry tie on vr.team_entry_id=tie.team_entry_id " +
					"where vr.vas_service_no=#{vasServiceNo} ORDER BY vr.id desc "
	)
	List<VasRate> getVasRateList(@Param("vasServiceNo")String vasServiceNo);

	@Insert(
			" INSERT INTO function_manage_config " +
					" (function_number,team_id,team_name,team_entry_id,team_entry_name," +
					"  app_no,app_name,bak_json,create_time,operator,status) " +
					" VALUES " +
					" (#{info.functionNumber},#{info.teamId},#{info.teamName},#{info.teamEntryId},#{info.teamEntryName}," +
					"  #{info.appNo},#{info.appName},#{info.bakJson},now(),#{info.operator},#{info.status}) "
	)
	int insertFunctionTeam(@Param("info")FunctionTeam info);

	@Insert(
			" INSERT INTO vas_service_info " +
					" (vas_service_no,vas_service_name,source_service_no,service_source," +
					"  create_time,operator) " +
					" VALUES " +
					" (#{info.vasServiceNo},#{info.vasServiceName},#{info.sourceServiceNo},#{info.serviceSource}," +
					"  now(),#{info.operator}) "
	)
	int insertVasServiceInfo(@Param("info")VasServiceInfo info);





	@Update(
			" update function_manage_config set " +
					" bak_json=#{info.bakJson},operator=#{info.operator} " +
					" where function_number=#{info.functionNumber}"
	)
	int saveFunctionConfigure(@Param("info")FunctionTeam info);

	//校验唯一
	@SelectProvider(type=FunctionManagerDao.SqlProvider.class,method="vasRateCheck")
	@ResultType(VasRate.class)
	VasRate vasRateCheck(@Param("info")FunctionTeam info);

	//校验唯一
	@SelectProvider(type=FunctionManagerDao.SqlProvider.class,method="functionTeamCheck")
	@ResultType(FunctionTeam.class)
	FunctionTeam functionTeamCheck(@Param("info")FunctionTeam info);

	@Delete(
			" delete from function_manage_config where id=#{id}"
	)
	int deleteFunctionTeam(@Param("id")int id);

	@Delete(
			" delete from vas_rate where id=#{id}"
	)
	int deleteVasRate(@Param("id")int id);

	@Select(
			"select * from app_function_config where fmc_id=#{info.fmcId} and function_code=#{info.functionCode} "
	)
	AppShowConfig checkEdit(@Param("info")AppShowConfig info);
	//获取app前端显示设置
	@Select(
			"select * from app_function_config where fmc_id=#{fmcId} ORDER BY function_modular asc,sort asc,id asc "
	)
	List<AppShowConfig> getAppShowList(@Param("fmcId")int fmcId);

	@Insert(
			"INSERT INTO app_function_config " +
					"(fmc_id,function_code,function_name,show_name,function_modular,sort," +
					" is_show,is_recommend,recommend_icon,create_time,operator) " +
					" VALUES " +
					" (#{info.fmcId},#{info.functionCode},#{info.functionName},#{info.showName},#{info.functionModular},#{info.sort}," +
					"  #{info.isShow},#{info.isRecommend},#{info.recommendIcon},now(),#{info.operator})"
	)
	int insertAppShowList(@Param("info")AppShowConfig info);

	@Update(
			"update app_function_config set " +
					" show_name=#{info.showName},sort=#{info.sort},is_show=#{info.isShow},is_recommend=#{info.isRecommend}," +
					" recommend_icon=#{info.recommendIcon},operator=#{info.operator} " +
					" where fmc_id=#{info.fmcId} and function_code=#{info.functionCode} "
	)
	int updateAppShowList(@Param("info")AppShowConfig item);

	@Delete(
			"delete from app_function_config where fmc_id=#{fmcId}"
	)
	int deleteAppShowConfig(@Param("fmcId")int fmcId);

	@Select("select * from function_manage_config where id=#{id}")
	@ResultType(FunctionTeam.class)
	FunctionTeam getFunctionTeam(int id);

	@Select("select * from function_manage_config where id!=#{info.id} AND function_number=#{info.functionNumber} AND team_id=#{info.teamId}")
	@ResultType(FunctionTeam.class)
	FunctionTeam getFunctionTeamExit(@Param("info")FunctionTeam info);

	@Update("update function_manage_config set team_id=#{info.teamId},team_name=#{info.teamName},bak_json=#{info.bakJson} where id=#{info.id}")
	int updateFunctionTeam(@Param("info")FunctionTeam info);

	class SqlProvider {
		public String functionTeamCheck(Map<String, Object> param){
			final FunctionTeam  info = (FunctionTeam) param.get("info");
			return new SQL(){
				{
					SELECT(" * ");
					FROM("function_manage_config");
					WHERE("function_number=#{info.functionNumber}");
					WHERE("team_id=#{info.teamId}");

					if(StringUtils.isNotBlank(info.getTeamEntryId())){
						WHERE("team_entry_id=#{info.teamEntryId}");
					}else{
						WHERE("team_entry_id is NULL");
					}
					if(StringUtils.isNotBlank(info.getAppNo())){
						WHERE("app_no=#{info.appNo}");
					}
				}
			}.toString();
		}

		public String vasRateCheck(Map<String, Object> param){
			final FunctionTeam  info = (FunctionTeam) param.get("info");
			return new SQL(){
				{
					SELECT(" * ");
					FROM("vas_rate");
					WHERE("vas_service_no=#{info.vasServiceNo}");
					WHERE("team_id=#{info.teamId}");

					if(StringUtils.isNotBlank(info.getTeamEntryId())){
						WHERE("team_entry_id=#{info.teamEntryId}");
					}else{
						WHERE("team_entry_id is NULL");
					}
				}
			}.toString();
		}
	}
}
