package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.PropertyConfigDao;
import cn.eeepay.framework.model.exchange.PropertyConfig;
import cn.eeepay.framework.service.PropertyConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 查询页面所有字段
 */
@Service("propertyConfigService")
public class PropertyConfigServiceImpl implements PropertyConfigService {


    @Resource
    private PropertyConfigDao propertyConfigDao;

    @Override
    public List<PropertyConfig> getOrgConfig(String propertyType, String configType) {
        return propertyConfigDao.getOrgConfig(propertyType,configType);
    }

    @Override
    public int addOrgConfig(PropertyConfig pc) {
        return propertyConfigDao.addOemConfigBak(pc);
    }

    @Override
    public List<PropertyConfig> getOrgConfigAndValue(String propertyType, String configType,String configCode) {
        return propertyConfigDao.getOrgConfigAndValue(propertyType,configType,configCode);
    }

    @Override
    public int updateOrgConfig(PropertyConfig pc) {
        return propertyConfigDao.updateOemConfigBak(pc);
    }

    @Override
    public PropertyConfig updateOrgConfigSelect(PropertyConfig pc) {
        return propertyConfigDao.updateOemConfigSelectBak(pc);
    }

    @Override
    public int deleteOrgConfig(PropertyConfig pc) {
        return propertyConfigDao.deleteOemConfigBak(pc);
    }

    @Override
    public int addTypeConfig(PropertyConfig pc) {
        return propertyConfigDao.addTypeConfig(pc);
    }

    @Override
    public List<PropertyConfig> getTypeConfigAndValue(String propertyType, String configType, String configCode) {
        return propertyConfigDao.getTypeConfigAndValue(propertyType,configType,configCode);
    }

    @Override
    public int updateTypeConfig(PropertyConfig pc) {
        return propertyConfigDao.updateTypeConfig(pc);
    }

    @Override
    public PropertyConfig updateTypeConfigSelect(PropertyConfig pc) {
        return propertyConfigDao.updateTypeConfigSelect(pc);
    }

    @Override
    public int deleteTypeConfig(PropertyConfig pc) {
        return propertyConfigDao.deleteTypeConfig(pc);
    }

    @Override
    public List<PropertyConfig> getOemConfigAndValue(String propertyType, String configType, String oemNo) {
        return propertyConfigDao.getOemConfigAndValue(propertyType,configType,oemNo);
    }

    @Override
    public int addOemConfig(PropertyConfig pc) {
        return propertyConfigDao.addOemConfig(pc);
    }

    @Override
    public int updateOemConfig(PropertyConfig pc) {
        return propertyConfigDao.updateOemConfig(pc);
    }

    @Override
    public PropertyConfig updateOemConfigSelect(PropertyConfig pc) {
        return propertyConfigDao.updateOemConfigSelect(pc);
    }

    @Override
    public int deleteOemConfig(PropertyConfig pc) {
        return propertyConfigDao.deleteOemConfig(pc);
    }
}
