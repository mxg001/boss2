package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.cjt.CjtTeamHardware;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author tans
 * @date 2019/5/31 16:10
 */
public interface CjtTeamHardwareDao {

    @Select("select DISTINCT hp.hp_id, hp.type_name from hardware_product hp " +
            "INNER JOIN cjt_team_hardware cth on cth.hp_id = hp.hp_id")
    @ResultType(HardwareProduct.class)
    List<HardwareProduct> selectCjtHpList();

    @Select("select * from cjt_team_hardware group by hp_id")
    @ResultType(CjtTeamHardware.class)
    List<CjtTeamHardware> selectHpIdList();

    @Select("select * from cjt_team_hardware where hp_id = #{hpId} and team_id = #{teamId}")
    @ResultType(CjtTeamHardware.class)
    CjtTeamHardware selectByHpAndTeam(@Param("hpId") String hpId, @Param("teamId")  String teamId);
}
