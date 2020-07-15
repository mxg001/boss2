package cn.eeepay.framework.dao.pushManager;


import cn.eeepay.framework.dao.BusinessProductDefineDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.pushManager.PushManager;
import cn.eeepay.framework.model.pushManager.PushManagerDetail;
import cn.eeepay.framework.util.StringUtil;
import com.sun.corba.se.spi.orb.ORBData;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/***
 *  table push_manager
 */
public interface PushManagerDao {


    @Update("update push_manager set push_title=#{pushTitle},push_content=#{pushContent}" +
            ",jump_url=#{jumpUrl},push_obj=#{pushObj}" +
            ",mobile_terminal_type=#{mobileTerminalType},target_user=#{targetUser}" +
            ",push_time=#{pushTime},timer_time=#{timerTime}" +
            ",actual_time=#{actualTime},push_status=#{pushStatus}" +
            ",create_person=#{createPerson},push_t" +
            "itle=#{pushTitle},dingshi_or_now=#{dingshiOrNow}" +
            ",create_time=#{createTime},push_times = #{pushTimes},err_msg=#{errMsg} where id = #{id}")
    public int updatePushManager(PushManager pm);

    @Delete("delete from push_manager where id=#{id}")
    public int delPushManager(@Param("id") Long id);

    @Insert("INSERT INTO push_manager(id, push_title, push_content, jump_url, push_obj, mobile_terminal_type, target_user, push_time, " +
            "timer_time, actual_time, push_status, create_person, create_time,dingshi_or_now) " +
            "VALUES (#{pm.id}, #{pm.pushTitle}, #{pm.pushContent}, #{pm.jumpUrl}, #{pm.pushObj}, #{pm.mobileTerminalType}, #{pm.targetUser}, " +
            "#{pm.pushTime}, #{pm.timerTime}, #{pm.actualTime}, #{pm.pushStatus}, #{pm.createPerson}, #{pm.createTime},#{pm.dingshiOrNow})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "pm.id", before = false, resultType = Long.class)
    //@Options(useGeneratedKeys = true,keyProperty = "pm.id" ,keyColumn = "id")
    public int savePushManager(@Param("pm")PushManager pm);

    @SelectProvider(type = SqlProvider.class, method = "selectPushManagerByParam")
    @ResultType(PushManager.class)
    List<PushManager> selectPushManagerByParam(@Param("page")Page<PushManager> page, @Param("pm") PushManager pm);

    @SelectProvider(type = SqlProvider.class, method = "selectPushManagerByParam")
    @ResultType(PushManager.class)
    List<PushManager> getPushManagerByParam(@Param("pm") PushManager pm);

    @SelectProvider(type = PushManagerDao.SqlProvider.class, method = "getAppInfo")
    //@Select("select * from app_info where apply = '2'")
    @ResultType(AppInfo.class)
    List<AppInfo> getAppInfo(@Param("apply") String apply);

    @Select("select * from push_manager where id=#{id}")
    @ResultType(PushManager.class)
    PushManager getByPushManagerId(Long id);

    @Select("select * from merchant_info where merchant_no = #{merchantNo}")
    @ResultType(MerchantInfo.class)
    MerchantInfo validMerchantNo(String merchantNo);

    @Select("select count(1) from merchant_info where team_id in( " +
            "select team_id from app_info where app_no in(${appNos}) " +
            ") and merchant_no =#{merchantNo}")
    @ResultType(Integer.class)
    Integer validMerchantNoAndAppNo(@Param("appNos")String appNos, @Param("merchantNo") String merchantNo);

    @Insert("INSERT INTO push_manager_detail(id, merchant_no, push_status, mobile_id, push_id, push_all, msg_id, msg_result, push_obj)" +
            " VALUES (id, #{merchantNo}, #{pushStatus}, #{mobileId}, #{pushId}, #{pushAll}, #{msgId}, #{msgResult}, #{pushObj})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    Integer savePushManagerDetail(PushManagerDetail pmd);


    @Delete("delete from push_manager_detail where push_id=#{pushId}")
    @ResultType(Integer.class)
    Integer delPushManagerDetailByPushId(Long pushId);

    @Select("select merchant_no from merchant_info " +
            "where team_id in ( " +
            "select team_id from app_info where app_no =#{pushObj}) ")
    @ResultType(String.class)
    List<String> getMerChantInfoByPushObj(@Param("pushObj") String pushObj);

    @Select("select merchant_no from merchant_info where merchant_no in( " +
            "select merchant_no from push_manager_detail where push_id=#{pushId} )")
    @ResultType(String.class)
    List<String> getMerchantInfoPushId(@Param("pushId") Long id);

    @Select("select group_concat(app_name,'') from app_info where FIND_IN_SET(app_no,#{pushObj} )")
    @ResultType(String.class)
    String getPushObjName(@Param("pushObj") String pushObj);

    @Select("select * from push_manager_detail where push_id=#{pushId}")
    @ResultType(PushManagerDetail.class)
    List<PushManagerDetail> getByPushManagerDetailByPushId(@Param("pushId") Long pushId);

    @Select("select device_id from jpush_device where app_no = #{appNo}")
    @ResultType(String.class)
    String getDeviceIdByAppNo(@Param("appNo")String appNo);

    @Update("update push_manager_detail set msg_id=#{msgId},msg_result=#{errMsg},push_status=#{pushStatus},mobile_id=#{mobileId} where push_id=#{pushId} and merchant_no=#{merchantNo}")
    int updatePushManagerDetail(@Param("merchantNo") String merchantNo, @Param("pushId") Long pushId,
                                @Param("msgId") String msgId, @Param("errMsg") String errMsg,
                                @Param("pushStatus") int pushStatus,
                                @Param("mobileId")String mobileId);

    @Select("select count(1) from merchant_info " +
            "where team_id in ( " +
            "select team_id from app_info where app_no =#{pushObj}) ")
    @ResultType(Integer.class)
    int getMerChantInfoCountsByPushObj(@Param("pushObj") String pushObj);

    @Select("select merchant_no from merchant_info " +
            "where team_id in ( " +
            "select team_id from app_info where app_no =#{pushObj}) limit #{first} , #{pageSize}")
    @ResultType(String.class)
    List<String> getMerchantInfoPageByPushObj(@Param("first") Integer first,@Param("pageSize") Integer pageSize,@Param("pushObj") String pushObj);

    @Select("select count(1) from merchant_info where merchant_no in( " +
            "select merchant_no from push_manager_detail where push_id=#{pushId} )")
    @ResultType(Integer.class)
    int getMerchantInfoCountsByPushId(@Param("pushId") Long pushId);

    @Select("select m1.merchant_no from merchant_info m1 where m1.merchant_no in( " +
            "select merchant_no from push_manager_detail where push_id=#{pushId} ) and exists (select 1 from  app_info where app_no=#{obj} and team_id=m1.team_id and apply='2') limit #{first} , #{pageSize}")
    @ResultType(String.class)
    List<String> getMerchantInfoPageByPushId(@Param("first")Integer first,@Param("pageSize")Integer pageSize, @Param("pushId") Long pushId,@Param("obj")String obj);

    @Select("select device_type from jpush_device where app_no = #{pushObj}")
    @ResultType(String.class)
    String getMobileTypeByPushObj(@Param("pushObj") String pushObj);

    @Select("select device_type from jpush_device where user_no = #{merchantNo} and device_id is not null")
    @ResultType(String.class)
    String getMobileTypeByMerchantNo(@Param("merchantNo")String merchantNo);

    @SelectProvider(type = SqlProvider.class, method = "selectPushManagerByParam")
    @ResultType(PushManager.class)
    List<PushManager> getPushManager(@Param("pm")PushManager pm);

    @Select("select count(1) from push_manager_detail where push_id=#{pushId}")
    @ResultType(Integer.class)
    int checkCanPush(@Param("pushId") String pushId);


    @Select("select team_name from team_info where team_id=#{teamId}")
    @ResultType(String.class)
    String getAppNameByMerchantNo(@Param("teamId") String teamId);

    @Select("select app_no  from jpush_device where user_no=#{merchantNo}")
    String getAppNoByMerchantNo(@Param("merchantNo") String merchantNo);


    public class SqlProvider {
        public String getAppInfo(Map<String, Object> param){
            final String  apply = (String) param.get("apply");
            return new SQL(){
                {
                    SELECT(" * ");
                    FROM("app_info");
                    if(StringUtils.isNotEmpty(apply)){
                        WHERE("apply = #{apply}");
                    }
                }
            }.toString();
        }

        public String selectPushManagerByParam(Map<String, Object> param){
            final PushManager  pm = (PushManager) param.get("pm");
            return new SQL(){
                {
                    SELECT(" * ");
                    FROM("push_manager");
                    if(StringUtils.isNotEmpty(pm.getPushContent())){
                        WHERE(" push_content like CONCAT('%',#{pm.pushContent},'%')");
                    }
                    if(StringUtils.isNotEmpty(pm.getJumpUrl())){
                        WHERE(" jump_url = #{pm.jumpUrl}");
                    }
                    if(pm.getMobileTerminalType()!=null){
                        WHERE(" mobile_terminal_type = #{pm.mobileTerminalType}");
                    }
                    if(StringUtils.isNotEmpty(pm.getPushObj())){
                        WHERE(" FIND_IN_SET(#{pm.pushObj},push_obj)");

                    }
                    if(pm.getPushStatus()!=null){
                        WHERE("push_status = #{pm.pushStatus}");
                    }
                    if(pm.getPushTimeBegin()!=null){
                        WHERE("push_time >= #{pm.pushTimeBegin}");
                    }
                    if(pm.getPushTimeEnd()!=null){
                        WHERE("push_time <= #{pm.pushTimeEnd}");
                    }
                    if(pm.getCreateTimeBegin()!=null){
                        WHERE("create_time >= #{pm.createTimeBegin}");
                    }
                    if(pm.getCreateTimeEnd()!=null){
                        WHERE("create_time <= #{pm.createTimeEnd}");
                    }
                    if(pm.getTimerTime()!=null){
                        WHERE("timer_time <= #{pm.timerTime}");
                    }
                    if(pm.getPushTimes()!=null){
                        WHERE(" push_times = #{pm.pushTimes}");
                    }
                    ORDER_BY("create_time desc");


                }
            }.toString();
        }
    }
}
