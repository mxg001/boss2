package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.AwardParam;
import cn.eeepay.framework.model.allAgent.AwardParamDiamonds;
import cn.eeepay.framework.model.allAgent.AwardParamLadder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/6/006.
 * @author  liuks
 */
public interface AwardParamDao {

    @SelectProvider(type=AwardParamDao.SqlProvider.class,method="selectAllList")
    @ResultType(AwardParam.class)
    List<AwardParam> selectAllList(@Param("oem") AwardParam oem, @Param("page") Page<AwardParam> page);

    @Insert(
            "INSERT INTO pa_brand " +
                    "(brand_code,brand_name,cost,out_service_id,create_time,last_update," +
                    " acc_fee,one_agent_status,two_agent_status,user_status ) " +
                    "VALUES " +
                    "(#{oem.brandCode},#{oem.brandName},#{oem.cost},#{oem.outServiceId},NOW(),NOW()," +
                    " #{oem.accFee},#{oem.oneAgentStatus},#{oem.twoAgentStatus},#{oem.userStatus})"
    )
    int addAwardParam(@Param("oem")AwardParam oem);

    @Insert(
            "INSERT INTO pa_level_share_config " +
                    "(brand_code," +
                    " gold_num,gold_amount,gold_award," +
                    " pt_num,pt_amount,pt_award," +
                    " bgold_num,bgold_amount,bgold_award," +
                    " diamo_num,diamo_amount,diamo_award," +
                    " ado_num,ado_amount,ado_rate,ado_limit," +
                    " create_time) " +
                    "VALUES " +
                    "(#{diamonds.brandCode}," +
                    " #{diamonds.goldNum},#{diamonds.goldAmount},#{diamonds.goldAward}," +
                    " #{diamonds.ptNum},#{diamonds.ptAmount},#{diamonds.ptAward}," +
                    " #{diamonds.bgoldNum},#{diamonds.bgoldAmount},#{diamonds.bgoldAward}," +
                    " #{diamonds.diamoNum},#{diamonds.diamoAmount},#{diamonds.diamoAward}," +
                    " #{diamonds.adoNum},#{diamonds.adoAmount},#{diamonds.adoRate},#{diamonds.adoLimit}," +
                    " NOW() )"
    )
    int addDiamonds(@Param("diamonds")AwardParamDiamonds awardParamDiamonds);

    @Insert(
            "INSERT INTO pa_ladder_setting " +
                    "(brand_code,min_num,max_num,val,type,ladder_grade )" +
                    "VALUES " +
                    "(#{brandCode},#{item.minNum},#{item.maxNum},#{item.val},#{type},#{item.ladderGrade})"
    )
    int addLadder(@Param("item")AwardParamLadder item, @Param("brandCode")String brandCode,@Param("type")int type);

    @Select(
            "select * from pa_brand where id=#{id}"
    )
    AwardParam getAwardParam(@Param("id")int id);

    @Select(
            "select id,brand_code,brand_name,cost,out_service_id,acc_fee,one_agent_status," +
                    " two_agent_status,create_time,last_update,user_status " +
                    " from pa_brand where id=#{id}"
    )
    AwardParam getAwardParamLittle(@Param("id")int id);
    @Select(
            "select * from pa_level_share_config where brand_code=#{brandCode}"
    )
    AwardParamDiamonds getDiamonds(@Param("brandCode")String brandCode);

    @Select(
            "select * from pa_ladder_setting where brand_code=#{brandCode} and type=#{type} order by ladder_grade"
    )
    List<AwardParamLadder> getLadder(@Param("brandCode")String brandCode,@Param("type")int type);

    @Update(
            "update pa_brand set brand_code=#{oem.brandCode}," +
                    " brand_name=#{oem.brandName},cost=#{oem.cost},out_service_id=#{oem.outServiceId}," +
                    " acc_fee=#{oem.accFee},one_agent_status=#{oem.oneAgentStatus},two_agent_status=#{oem.twoAgentStatus}," +
                    " user_status=#{oem.userStatus} " +
                    " where id=#{oem.id}"
    )
    int updateAwardParam(@Param("oem")AwardParam oem);

    @UpdateProvider(type=SqlProvider.class,method="updateAwardParamOem")
    int updateAwardParamOem(@Param("oem")AwardParam oem);

    @Update(
            "update pa_level_share_config " +
                    " set " +
                    " brand_code=#{diamonds.brandCode}," +
                    " gold_num=#{diamonds.goldNum},gold_amount=#{diamonds.goldAmount},gold_award=#{diamonds.goldAward}," +
                    " pt_num=#{diamonds.ptNum},pt_amount=#{diamonds.ptAmount},pt_award=#{diamonds.ptAward}," +
                    " bgold_num=#{diamonds.bgoldNum},bgold_amount=#{diamonds.bgoldAmount},bgold_award=#{diamonds.bgoldAward}," +
                    " diamo_num=#{diamonds.diamoNum},diamo_amount=#{diamonds.diamoAmount},diamo_award=#{diamonds.diamoAward}," +
                    " ado_num=#{diamonds.adoNum},ado_amount=#{diamonds.adoAmount},ado_rate=#{diamonds.adoRate},ado_limit=#{diamonds.adoLimit} " +
                    " where id=#{diamonds.id}"
    )
    void updateDiamonds(@Param("diamonds")AwardParamDiamonds diamonds);

    @Delete(
            "delete from pa_ladder_setting where brand_code=#{brandCode} and type=#{type}"
    )
    void deleteLadder(@Param("brandCode")String brandCode,@Param("type")int type);

    @Select(
            " select * from pa_brand where brand_code=#{brandCode}"
    )
    List<AwardParam> checkAwardParam(@Param("brandCode")String brandCode);

    @Select(
            " select * from pa_brand where brand_code=#{brandCode} and id!=#{id} "
    )
    List<AwardParam> checkAwardParamId(@Param("brandCode")String brandCode,@Param("id")int id);

    @Select(
            "select * from pa_brand "
    )
    List<AwardParam> getOemList();

    @Update("update pa_brand set owner_imgs=#{ownerImgs} where id=${id}")
    int deleteOwnerImgs(@Param("id")int id,@Param("ownerImgs")String ownerImgs);

    @Update("update pa_brand set mer_imgs=#{merImgs} where id=${id}")
    int deleteMerImgs(@Param("id")int id,@Param("merImgs")String merImgs);

    @Update("update pa_brand set leaderboard_bgi=#{leaderboardBgi} where id=${id}")
    int deleteLeaImgs(@Param("id")int id,@Param("leaderboardBgi")String leaderboardBgi);

    class SqlProvider{

        public String selectAllList(final Map<String, Object> param) {
            final AwardParam oem = (AwardParam) param.get("oem");
            return new SQL(){{
                SELECT("oem.*");
                FROM("pa_brand oem");
                if(StringUtils.isNotBlank(oem.getBrandCode())){
                    WHERE("oem.brand_code =#{oem.brandCode} ");
                }
                if(StringUtils.isNotBlank(oem.getBrandName())){
                    WHERE("oem.brand_name like concat(#{oem.brandName},'%') ");
                }
            }}.toString();
        }

        public String updateAwardParamOem(final Map<String, Object> param) {
            final AwardParam oem = (AwardParam) param.get("oem");
            StringBuffer sb = new StringBuffer();
            sb.append("update pa_brand set ");
            if (StringUtils.isNotBlank(oem.getAgentBgi())) {
                sb.append(" agent_bgi=#{oem.agentBgi}, ");
            }
            if (StringUtils.isNotBlank(oem.getMerBgi())) {
                sb.append(" mer_bgi=#{oem.merBgi}, ");
            }
            if (StringUtils.isNotBlank(oem.getAboutUs())) {
                sb.append(" about_us=#{oem.aboutUs}, ");
            }
            if (StringUtils.isNotBlank(oem.getOwnerImgs())) {
                sb.append(" owner_imgs=#{oem.ownerImgs}, ");
            }
            if (StringUtils.isNotBlank(oem.getOwnerImg())) {
                sb.append(" owner_img=#{oem.ownerImg}, ");
            }
            if (StringUtils.isNotBlank(oem.getMerImgs())) {
                sb.append(" mer_imgs=#{oem.merImgs}, ");
            }
            if (StringUtils.isNotBlank(oem.getMerImg())) {
                sb.append(" mer_img=#{oem.merImg}, ");
            }
            if (StringUtils.isNotBlank(oem.getMerContent())) {
                sb.append(" mer_content=#{oem.merContent}, ");
            }
            if (StringUtils.isNotBlank(oem.getLeaderboardBgi())) {
                sb.append(" leaderboard_bgi=#{oem.leaderboardBgi}, ");
            }
            sb.append(" mer_app=#{oem.merApp} ");
            sb.append(" where brand_code=#{oem.brandCode} ");
            return sb.toString();
        }
    }
}
