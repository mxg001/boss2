package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.OrgBusinessConf;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrgBusinessConfDao {

    @Select("select * from org_business_conf where org_id = #{orgId}")
    List<OrgBusinessConf> selectOrgBusinessConfByOrgId(@Param("orgId") Long orgId);

    @Insert("insert into org_business_conf " +
            "(`org_id`, `business_id`, `is_enable`,`create_by`, `create_date`, `update_by`, `update_date`) values (" +
            " #{orgId},#{businessId},#{isEnable},#{createBy},#{createDate},#{updateBy},#{updateDate} )")
    int insert(OrgBusinessConf orgBusinessConf);

    @Update("UPDATE `org_business_conf` SET  `org_id`=#{orgId}, `business_id`=#{businessId}, `is_enable`=#{isEnable}, `create_by`=#{createBy}, `create_date`=#{createDate}, `update_by`=#{updateBy}, `update_date`=#{updateDate} where `id`=#{id}")
    int update(OrgBusinessConf orgBusinessConf);

}
