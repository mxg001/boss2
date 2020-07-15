package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author liuks
 *
 */
public interface PropertyConfigActivateDao {


    @Select(
            "select * from yfb_property_config where property_type=#{pt} and config_type=#{ct} ORDER BY sort ASC"
    )
    List<PropertyConfigActivate> getOrgConfig(@Param("pt") String propertyType, @Param("ct") String configType);


    @Insert(
            "INSERT INTO yfb_oem_config (oem_no,oem_config_code,oem_config_value,create_time,bak) " +
                    " VALUES(#{pc.configCode},#{pc.propertyCode},#{pc.configValue},NOW(),#{pc.bak})"
    )
    int addOemConfigBak(@Param("pc") PropertyConfigActivate pc);

    @Select(
            "select config.*,oem.oem_config_value config_value,oem.bak,oem.oem_no config_code from yfb_property_config config" +
                    " LEFT JOIN yfb_oem_config oem " +
                    "  ON (oem.oem_config_code=config.property_code and oem.bak=#{code})" +
                    "where config.property_type=#{pt} and config.config_type=#{ct} ORDER BY config.sort ASC"
    )
    List<PropertyConfigActivate> getOrgConfigAndValue(@Param("pt") String propertyType, @Param("ct") String configType, @Param("code") String orgCode);

    @Update(
            "update yfb_oem_config set oem_config_value=#{pc.configValue} " +
                    " where oem_config_code=#{pc.propertyCode} and bak=#{pc.bak}"
    )
    int updateOemConfigBak(@Param("pc") PropertyConfigActivate pc);

    @Select(
            "select id,oem_no config_code,oem_config_code property_code,oem_config_value config_value,create_time,bak " +
                    " from yfb_oem_config " +
                    " where oem_config_code=#{pc.propertyCode} and bak=#{pc.bak} "
    )
    PropertyConfigActivate updateOemConfigSelectBak(@Param("pc") PropertyConfigActivate pc);

    @Delete(
            "delete from yfb_oem_config where oem_config_code=#{pc.propertyCode} and bak=#{pc.bak} "
    )
    int deleteOemConfigBak(@Param("pc") PropertyConfigActivate pc);

    @Insert(
            "INSERT INTO yfb_pro_type_config (type_code,config_code,config_value,create_time) " +
                    " VALUES(#{pc.configCode},#{pc.propertyCode},#{pc.configValue},NOW())"
    )
    int addTypeConfig(@Param("pc") PropertyConfigActivate pc);

    @Select(
            "select config.*,type.config_value config_value,type.type_code config_code " +
                    " from yfb_property_config config" +
                    " LEFT JOIN yfb_pro_type_config type " +
                    "  ON (type.config_code=config.property_code and type.type_code=#{code})" +
                    "where config.property_type=#{pt} and config.config_type=#{ct} ORDER BY config.sort ASC"
    )
    List<PropertyConfigActivate> getTypeConfigAndValue(@Param("pt") String propertyType, @Param("ct") String configType, @Param("code") String configCode);

    @Update(
            "update yfb_pro_type_config set config_value=#{pc.configValue} " +
                    " where config_code=#{pc.propertyCode} and type_code=#{pc.configCode}"
    )
    int updateTypeConfig(@Param("pc") PropertyConfigActivate pc);

    @Select(
            " select config_value config_value,type_code config_code from yfb_pro_type_config where config_code=#{pc.propertyCode} and type_code=#{pc.configCode} "
    )
    PropertyConfigActivate updateTypeConfigSelect(@Param("pc") PropertyConfigActivate pc);

    @Delete(
            " delete from yfb_pro_type_config where config_code=#{pc.propertyCode} and type_code=#{pc.configCode} "
    )
    int deleteTypeConfig(@Param("pc") PropertyConfigActivate pc);

    @Select(
            "select config.*,oem.oem_config_value config_value,oem.bak,oem.oem_no config_code from yfb_property_config config" +
                    " LEFT JOIN yfb_oem_config oem " +
                    "  ON (oem.oem_config_code=config.property_code and oem.oem_no=#{oemNo})" +
                    "where config.property_type=#{pt} and config.config_type=#{ct} ORDER BY config.sort ASC"
    )
    List<PropertyConfigActivate> getOemConfigAndValue(@Param("pt") String propertyType, @Param("ct") String configType, @Param("oemNo") String oemNo);


    @Insert(
            "INSERT INTO yfb_oem_config (oem_no,oem_config_code,oem_config_value,create_time) " +
                    " VALUES(#{pc.configCode},#{pc.propertyCode},#{pc.configValue},NOW())"
    )
    int addOemConfig(@Param("pc") PropertyConfigActivate pc);


    @Update(
            "update yfb_oem_config set oem_config_value=#{pc.configValue} " +
                    " where oem_config_code=#{pc.propertyCode} and oem_no=#{pc.configCode}"
    )
    int updateOemConfig(@Param("pc") PropertyConfigActivate pc);

    @Select(
            "select id,oem_no config_code,oem_config_code property_code,oem_config_value config_value,create_time,bak " +
                    " from yfb_oem_config " +
                    " where oem_config_code=#{pc.propertyCode} and oem_no=#{pc.configCode} "
    )
    PropertyConfigActivate updateOemConfigSelect(@Param("pc") PropertyConfigActivate pc);

    @Delete(
            "delete from yfb_oem_config " +
                    " where oem_config_code=#{pc.propertyCode} and oem_no=#{pc.configCode} "
    )
    int deleteOemConfig(@Param("pc") PropertyConfigActivate pc);


}
