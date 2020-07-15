package cn.eeepay.framework.service.exchangeActivate;


import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11/011.
 * @@author  liuks
 */
public interface PropertyConfigActivateService {

    List<PropertyConfigActivate>  getOrgConfig(String propertyType, String configType);

    int addOrgConfig(PropertyConfigActivate pc);

    List<PropertyConfigActivate>  getOrgConfigAndValue(String propertyType, String configType, String configCode);

    int updateOrgConfig(PropertyConfigActivate pc);

    PropertyConfigActivate updateOrgConfigSelect(PropertyConfigActivate pc);

    int deleteOrgConfig(PropertyConfigActivate pc);

    int addTypeConfig(PropertyConfigActivate pc);

    List<PropertyConfigActivate>  getTypeConfigAndValue(String propertyType, String configType, String configCode);

    int updateTypeConfig(PropertyConfigActivate pc);

    PropertyConfigActivate updateTypeConfigSelect(PropertyConfigActivate pc);

    int deleteTypeConfig(PropertyConfigActivate pc);

    List<PropertyConfigActivate>  getOemConfigAndValue(String propertyType, String configType, String oemNo);

    int addOemConfig(PropertyConfigActivate pc);

    int updateOemConfig(PropertyConfigActivate pc);

    PropertyConfigActivate updateOemConfigSelect(PropertyConfigActivate pc);

    int deleteOemConfig(PropertyConfigActivate pc);

}
