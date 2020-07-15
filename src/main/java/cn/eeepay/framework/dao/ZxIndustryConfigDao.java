package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ZxIndustryConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author tans
 * @date 2018/9/1 15:29
 */
public interface ZxIndustryConfigDao {

    @Select("select zic.*,ti.team_name from zx_industry_config zic" +
            " left join team_info ti on ti.team_id = zic.team_id" +
            " where activetiy_code = #{activetiyCode}")
    @ResultType(ZxIndustryConfig.class)
    List<ZxIndustryConfig> selectList(@Param("activetiyCode") String activetiyCode);

    @Update("update zx_industry_config set rate_type=#{rateType},fixed_amount=#{fixedAmount}," +
            "rate=#{rate},operator=#{operator},update_time=#{updateTime}" +
            " where activetiy_code = #{activetiyCode} and team_id = #{teamId} ")
    int update(ZxIndustryConfig baseInfo);

}
