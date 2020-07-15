package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.model.exchange.PropertyConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author liuks
 *
 */
public interface PropertyConfigDao {


    @Select(
            "select * from rdmp_property_config where property_type=#{pt} and config_type=#{ct} ORDER BY sort ASC"
    )
    List<PropertyConfig> getOrgConfig(@Param("pt")String propertyType,@Param("ct")String configType);


    @Insert(
            "INSERT INTO rdmp_oem_config (oem_no,oem_config_code,oem_config_value,create_time,bak) " +
                    " VALUES(#{pc.configCode},#{pc.propertyCode},#{pc.configValue},NOW(),#{pc.bak})"
    )
    int addOemConfigBak(@Param("pc")PropertyConfig pc);

    @Select(
            "select config.*,oem.oem_config_value config_value,oem.bak,oem.oem_no config_code from rdmp_property_config config" +
                    " LEFT JOIN rdmp_oem_config oem " +
                    "  ON (oem.oem_config_code=config.property_code and oem.bak=#{code})" +
                    "where config.property_type=#{pt} and config.config_type=#{ct} ORDER BY config.sort ASC"
    )
    List<PropertyConfig> getOrgConfigAndValue(@Param("pt")String propertyType,@Param("ct")String configType,@Param("code")String orgCode);

    @Update(
            "update rdmp_oem_config set oem_config_value=#{pc.configValue} " +
                    " where oem_config_code=#{pc.propertyCode} and bak=#{pc.bak}"
    )
    int updateOemConfigBak(@Param("pc")PropertyConfig pc);

    @Select(
            "select id,oem_no config_code,oem_config_code property_code,oem_config_value config_value,create_time,bak " +
                    " from rdmp_oem_config " +
                    " where oem_config_code=#{pc.propertyCode} and bak=#{pc.bak} "
    )
    PropertyConfig updateOemConfigSelectBak(@Param("pc")PropertyConfig pc);

    @Delete(
            "delete from rdmp_oem_config where oem_config_code=#{pc.propertyCode} and bak=#{pc.bak} "
    )
    int deleteOemConfigBak(@Param("pc")PropertyConfig pc);

    @Insert(
            "INSERT INTO rdmp_pro_type_config (type_code,config_code,config_value,create_time) " +
                    " VALUES(#{pc.configCode},#{pc.propertyCode},#{pc.configValue},NOW())"
    )
    int addTypeConfig(@Param("pc")PropertyConfig pc);

    @Select(
            "select config.*,type.config_value config_value,type.type_code config_code " +
                    " from rdmp_property_config config" +
                    " LEFT JOIN rdmp_pro_type_config type " +
                    "  ON (type.config_code=config.property_code and type.type_code=#{code})" +
                    "where config.property_type=#{pt} and config.config_type=#{ct} ORDER BY config.sort ASC"
    )
    List<PropertyConfig> getTypeConfigAndValue(@Param("pt")String propertyType,@Param("ct")String configType,@Param("code")String configCode);

    @Update(
            "update rdmp_pro_type_config set config_value=#{pc.configValue} " +
                    " where config_code=#{pc.propertyCode} and type_code=#{pc.configCode}"
    )
    int updateTypeConfig(@Param("pc")PropertyConfig pc);

    @Select(
            " select config_value config_value,type_code config_code from rdmp_pro_type_config where config_code=#{pc.propertyCode} and type_code=#{pc.configCode} "
    )
    PropertyConfig updateTypeConfigSelect(@Param("pc")PropertyConfig pc);

    @Delete(
            " delete from rdmp_pro_type_config where config_code=#{pc.propertyCode} and type_code=#{pc.configCode} "
    )
    int deleteTypeConfig(@Param("pc")PropertyConfig pc);

    @Select(
            "select config.*,oem.oem_config_value config_value,oem.bak,oem.oem_no config_code from rdmp_property_config config" +
                    " LEFT JOIN rdmp_oem_config oem " +
                    "  ON (oem.oem_config_code=config.property_code and oem.oem_no=#{oemNo})" +
                    "where config.property_type=#{pt} and config.config_type=#{ct} ORDER BY config.sort ASC"
    )
    List<PropertyConfig> getOemConfigAndValue(@Param("pt")String propertyType,@Param("ct") String configType, @Param("oemNo")String oemNo);


    @Insert(
            "INSERT INTO rdmp_oem_config (oem_no,oem_config_code,oem_config_value,create_time) " +
                    " VALUES(#{pc.configCode},#{pc.propertyCode},#{pc.configValue},NOW())"
    )
    int addOemConfig(@Param("pc")PropertyConfig pc);


    @Update(
            "update rdmp_oem_config set oem_config_value=#{pc.configValue} " +
                    " where oem_config_code=#{pc.propertyCode} and oem_no=#{pc.configCode}"
    )
    int updateOemConfig(@Param("pc")PropertyConfig pc);

    @Select(
            "select id,oem_no config_code,oem_config_code property_code,oem_config_value config_value,create_time,bak " +
                    " from rdmp_oem_config " +
                    " where oem_config_code=#{pc.propertyCode} and oem_no=#{pc.configCode} "
    )
    PropertyConfig updateOemConfigSelect(@Param("pc")PropertyConfig pc);

    @Delete(
            "delete from rdmp_oem_config " +
                    " where oem_config_code=#{pc.propertyCode} and oem_no=#{pc.configCode} "
    )
    int deleteOemConfig(@Param("pc")PropertyConfig pc);


}
