package cn.eeepay.framework.service;

import cn.eeepay.framework.model.exchange.PropertyConfig;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11/011.
 * @@author  liuks
 */
public interface PropertyConfigService {

    List<PropertyConfig>  getOrgConfig(String propertyType,String configType);

    int addOrgConfig(PropertyConfig pc);

    List<PropertyConfig>  getOrgConfigAndValue(String propertyType,String configType,String configCode);

    int updateOrgConfig(PropertyConfig pc);

    PropertyConfig updateOrgConfigSelect(PropertyConfig pc);

    int deleteOrgConfig(PropertyConfig pc);

    int addTypeConfig(PropertyConfig pc);

    List<PropertyConfig>  getTypeConfigAndValue(String propertyType,String configType,String configCode);

    int updateTypeConfig(PropertyConfig pc);

    PropertyConfig updateTypeConfigSelect(PropertyConfig pc);

    int deleteTypeConfig(PropertyConfig pc);

    List<PropertyConfig>  getOemConfigAndValue(String propertyType,String configType,String oemNo);

    int addOemConfig(PropertyConfig pc);

    int updateOemConfig(PropertyConfig pc);

    PropertyConfig updateOemConfigSelect(PropertyConfig pc);

    int deleteOemConfig(PropertyConfig pc);

}
