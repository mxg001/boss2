package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.model.allAgent.RankConfig;
import cn.eeepay.framework.model.allAgent.RankReward;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/5/005.
 * @author  liuks
 *
 */
public interface RankConfigDao {

    /**
     * 首次新增
     * @param info
     * @return
     */
    @Insert(
            "INSERT INTO pa_rank_config " +
                    "(rank_max,brand_code_set,start_time,end_time,rank_img, " +
                    " one_join,one_show,two_join,two_show, " +
                    " rank_url,rank_rule,create_time) " +
                    " VALUES " +
                    " (#{info.rankMax},#{info.brandCodeSet},#{info.startTime},#{info.endTime},#{info.rankImg}, " +
                    "  #{info.oneJoin},#{info.oneShow},#{info.twoJoin},#{info.twoShow}, " +
                    "  #{info.rankUrl},#{info.rankRule},NOW()) "
    )
    @Options(useGeneratedKeys = true, keyProperty = "info.id")
    int addRankConfig(@Param("info") RankConfig info);


    //动态修改
    @UpdateProvider(type=RankConfigDao.SqlProvider.class,method="updateRankConfig")
    int updateRankConfig(@Param("info") RankConfig info);

    @Delete(
            "delete from pa_rank_reward where rank_config_id=#{id}"
    )
    int deleteRankRewardList(@Param("id")Long id);

    @Insert(
            "INSERT INTO pa_rank_reward (rank_config_id,rank,rank_amount,reach_num,reach_amount) " +
                    " VALUES(#{item.rankConfigId},#{item.rank},#{item.rankAmount},#{item.reachNum},#{item.reachAmount}) "
    )
    int addRankReward(@Param("item")RankReward item);

    @Select(
            "select * from pa_rank_reward where rank_config_id=#{id} order by rank asc"
    )
    List<RankReward> getRankRewardList(@Param("id")Long id);


    @Select(
            "select * from pa_rank_config order by id ASC LIMIT 1"
    )
    RankConfig getRankConfig();


    class SqlProvider{
        public String updateRankConfig(final Map<String, Object> param) {
            RankConfig info = (RankConfig) param.get("info");
            StringBuffer sb = new StringBuffer();
            sb.append("update pa_rank_config set ");
            sb.append(" rank_max=#{info.rankMax},brand_code_set=#{info.brandCodeSet}, ");
            sb.append(" start_time=#{info.startTime},end_time=#{info.endTime},");
            if (StringUtils.isNotBlank(info.getRankImg())) {
                sb.append(" rank_img=#{info.rankImg}, ");
            }
            sb.append(" one_join=#{info.oneJoin},one_show=#{info.oneShow}, ");
            sb.append(" two_join=#{info.twoJoin},two_show=#{info.twoShow}, ");
            sb.append(" rank_url=#{info.rankUrl},rank_rule=#{info.rankRule} ");
            sb.append(" where id=#{info.id} ");
            return sb.toString();
        }
    }
}
