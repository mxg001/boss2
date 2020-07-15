package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantWarning;
import cn.eeepay.framework.util.StringUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface MerchantWarningDao {

    @SelectProvider(type=MerchantWarningDao.SqlProvider.class,method="selectMerchantWarningPage")
    @ResultType(MerchantWarning.class)
    List<MerchantWarning> selectMerchantWarningPage(@Param("info")MerchantWarning info, Page<MerchantWarning> page);

    @Select("SELECT * from merchant_warning_service where id=#{id}")
    @ResultType(MerchantWarning.class)
    MerchantWarning selectMerchantWarningDetail(@Param("id") Integer id);

    @Delete({"delete from merchant_warning_service where id = #{id}"})
    int deleteMerchantWarning(@Param("id") Integer id);

    @Update("update merchant_warning_service set is_used=#{isUsed} where id=#{id}")
    int updateIsUsedStatus(@Param("id")Integer id,@Param("isUsed")Integer isUsed);

    @Select("SELECT GROUP_CONCAT(team_name) teamName from team_info where team_id in (${teamId})")
    @ResultType(String.class)
    String selectTeamNameGroupConcat(@Param("teamId")String teamId);

    @Insert("INSERT INTO merchant_warning_service(warning_type ,warning_name ,team_id ,is_used ,warning_img ," +
            "warning_title ,warning_url ,no_tran_day ,tran_slide_rate ,create_time ,remark ) " +
            "values (#{m.warningType} ,#{m.warningName} ,#{m.teamId} ,0 ,#{m.warningImg} ," +
            "#{m.warningTitle} ,#{m.warningUrl} ,#{m.noTranDay} ,#{m.tranSlideRate} ,NOW() ,#{m.remark})")
    int insertMerWarning(@Param("m")MerchantWarning merchantWarning);

    @Update("update merchant_warning_service set warning_type=#{m.warningType},warning_name=#{m.warningName}," +
            "team_id=#{m.teamId},warning_img=#{m.warningImg},warning_title=#{m.warningTitle}," +
            "warning_url=#{m.warningUrl},no_tran_day=#{m.noTranDay},tran_slide_rate=#{m.tranSlideRate}" +
            " where id=#{m.id}")
    int updateMerWarning(@Param("m")MerchantWarning merchantWarning);

    public class SqlProvider {
        public String selectMerchantWarningPage(final Map<String, Object> param) {
            final MerchantWarning info = (MerchantWarning) param.get("info");
            return new SQL() {
                {
                    SELECT("mws.*,ti.team_name");
                    FROM("merchant_warning_service mws");
                    LEFT_OUTER_JOIN("team_info ti on mws.team_id=ti.team_id");
                    if (info.getId() != null && StringUtil.isNotBlank(info.getId())) {
                        WHERE("mws.id = #{info.id}");
                    }
                    if (StringUtil.isNotBlank(info.getWarningType())) {
                        WHERE("warning_type=#{info.warningType}");
                    }
                    ORDER_BY("create_time desc");
                }
            }.toString();
        }
    }

}
