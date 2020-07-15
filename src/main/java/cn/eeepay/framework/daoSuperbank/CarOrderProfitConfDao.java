package cn.eeepay.framework.daoSuperbank;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.CarOrderProfitConf;

public interface CarOrderProfitConfDao {

	@Insert("insert into carorder_profit_conf (org_push_amount,org_gain_amount,create_time,update_time,create_by,update_by) values(#{baseInfo.orgPushAmount},#{baseInfo.orgGainAmount},NOW(),NOW(),#{baseInfo.createBy},#{baseInfo.updateBy})")
    int insert(@Param("baseInfo") CarOrderProfitConf baseInfo);

    @Select("select * from carorder_profit_conf")
    CarOrderProfitConf select();

    @Update("update carorder_profit_conf set org_push_amount=#{baseInfo.orgPushAmount},org_gain_amount=#{baseInfo.orgGainAmount},update_time=NOW(),update_by=#{baseInfo.updateBy} where id = #{baseInfo.id}")
    int update(@Param("baseInfo") CarOrderProfitConf baseInfo);

}
