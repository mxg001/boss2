package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.MobileVerInfo;

public interface AppInfoDao {

	@Insert("Insert into app_info (app_name,app_no,team_id,team_name,code_url,parent_id,protocol_ver)"
			+ " values(#{ais.appName},#{ais.appNo},#{ais.teamId},#{ais.teamName},#{ais.codeUrl},#{ais.parentId},#{ais.protocolVer})")
	int insert(@Param("ais")AppInfo ais);
	
	@Update("Update app_info set app_name=#{ais.appName},app_no=#{ais.appNo},team_id=#{ais.teamId},team_name=#{ais.teamName},"
			+ "code_url=#{ais.codeUrl},parent_id=#{ais.parentId},protocol_ver=#{ais.protocolVer} where id=#{ais.id}")
	int update(@Param("ais")AppInfo ais);
	
	@Select("select ais.id,ais.parent_id,ais.protocol_ver,ais.code_url,ais.app_name as appName,(select ss.app_name from app_info ss where ss.id=ais.parent_id)as parenName from "
			+ "app_info ais where id=#{id}")
	@ResultType(AppInfo.class)
	AppInfo selectDetail(@Param("id")int id);
	
	@Select("select ais.id,ais.app_name as appName,(select ss.app_name from app_info ss where ss.id=ais.parent_id)as parenName from "
			+ "app_info ais where ais.apply=1 and ais.parent_id=0")
	@ResultType(AppInfo.class)
	List<AppInfo> selectInfoBox();
	
	@Select("select ais.id,ais.parent_id,ais.code_url,ais.app_name as appName,(select ss.app_name from app_info ss where ss.id=ais.parent_id)as parenName from "
			+ "app_info ais where ais.parent_id=#{parentId}")
	@ResultType(AppInfo.class)
	List<AppInfo> selectInfoBoxName(String parentId);
	
	@Select("select * from app_info where id=#{id}")
	@ResultType(AppInfo.class)
	AppInfo selectInfo(@Param("id")int id);
	
	@Select("select * from app_info where app_name=#{name} and parent_id=#{parenId}")
	@ResultType(AppInfo.class)
	AppInfo selectInfos(@Param("name")String name,@Param("parenId")String parenId);
	
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(AppInfo.class)
	List<AppInfo> selectAllInfo(@Param("page")Page<AppInfo> page,@Param("ais")AppInfo ais);
	
	@Select("select * from app_info where app_no=#{appId}")
	@ResultType(AppInfo.class)
	AppInfo findInfo(@Param("appId")String appId);
	
	//历史记录
	@Select("select mvi.*,ais.app_name from mobile_ver_info mvi "
			+ "LEFT JOIN app_info ais on ais.app_no=mvi.app_type "
			+ "where mvi.APP_TYPE=#{appId}")
	@ResultType(MobileVerInfo.class)
	List<MobileVerInfo> selectChildAllInfo(@Param("page")Page<MobileVerInfo> page,@Param("appId")String appId);
	
	//历史记录详情
	@Select("select mvi.*,ais.app_name from mobile_ver_info mvi "
			+ "LEFT JOIN app_info ais on ais.app_no=mvi.app_type"
			+ " where mvi.id=#{id}")
	@ResultType(MobileVerInfo.class)
	MobileVerInfo findChildDetailInfo(@Param("id")String id);
	
	//历史记录修改
	@Update("update mobile_ver_info set PLATFORM=#{mvi.platform},VERSION=#{mvi.version},APP_URL=#{mvi.appUrl}"
			+ ",DOWN_FLAG=#{mvi.downFlag},VER_DESC=#{mvi.verDesc},"
			+ "lowest_version=#{mvi.lowestVersion},APP_LOGO=#{mvi.appLogo},url=#{mvi.url} "
			+ "where id=#{mvi.id}")
	int updateMviInfo(@Param("mvi")MobileVerInfo mvi);
	
	//历史记录新增
	@Insert("Insert into mobile_ver_info(PLATFORM,VERSION,APP_URL,DOWN_FLAG,VER_DESC,APP_TYPE,lowest_version,APP_LOGO,url,CREATE_TIME) "
			+ "values(#{mvi.platform},#{mvi.version},#{mvi.appUrl},#{mvi.downFlag},#{mvi.verDesc},"
			+ "#{mvi.appType},#{mvi.lowestVersion},#{mvi.appLogo},#{mvi.url},#{mvi.createTime})")
	int insertMviInfo(@Param("mvi")MobileVerInfo mvi);

	@Select("SELECT version,app_url,down_flag,ver_desc,lowest_version" +
			" FROM `mobile_ver_info` " +
			" WHERE app_type=#{appType} and platform=#{platform}" +
			" ORDER BY version desc limit 1")
	@ResultType(MobileVerInfo.class)
	MobileVerInfo getVersion(MobileVerInfo currentVer);

	@Select("select count(1) from app_info where team_id=#{teamId} and app_no=#{appNo}")
	int checkUniqueApp(@Param("teamId") String teamId, @Param("appNo") String appNo);

	@Select(
			"select * from app_info "
	)
	List<AppInfo> getAllAppInfo();

	public class SqlProvider{
    	public String selectAllInfo(Map<String,Object> param){
    		final AppInfo ais=(AppInfo)param.get("ais");
    		return new SQL(){{
    			SELECT("ais.apply,ais.code_url,ais.protocol_ver,ais.id,ais.app_no,ais.app_name as appName,ais.team_name,"
    					+ "(select ss.app_name from app_info ss where ss.id=ais.parent_id)as parenName");
    			FROM("app_info ais");
    			WHERE("ais.team_id is not null");
    			if(StringUtils.isNotBlank(ais.getAppName())){
    				WHERE("ais.app_name=#{ais.appName}");
    			}
    		}}.toString();
    	}

    }

}
