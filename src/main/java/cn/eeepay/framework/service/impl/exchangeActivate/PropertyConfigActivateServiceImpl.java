package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.PropertyConfigActivateDao;
import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;
import cn.eeepay.framework.service.exchangeActivate.PropertyConfigActivateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 查询页面所有字段
 */
@Service("propertyConfigActivateService")
public class PropertyConfigActivateServiceImpl implements PropertyConfigActivateService {


    @Resource
    private PropertyConfigActivateDao propertyConfigActivateDao;

    @Override
    public List<PropertyConfigActivate> getOrgConfig(String propertyType, String configType) {
        return propertyConfigActivateDao.getOrgConfig(propertyType,configType);
    }

    @Override
    public int addOrgConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.addOemConfigBak(pc);
    }

    @Override
    public List<PropertyConfigActivate> getOrgConfigAndValue(String propertyType, String configType,String configCode) {
        return propertyConfigActivateDao.getOrgConfigAndValue(propertyType,configType,configCode);
    }

    @Override
    public int updateOrgConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.updateOemConfigBak(pc);
    }

    @Override
    public PropertyConfigActivate updateOrgConfigSelect(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.updateOemConfigSelectBak(pc);
    }

    @Override
    public int deleteOrgConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.deleteOemConfigBak(pc);
    }

    @Override
    public int addTypeConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.addTypeConfig(pc);
    }

    @Override
    public List<PropertyConfigActivate> getTypeConfigAndValue(String propertyType, String configType, String configCode) {
        return propertyConfigActivateDao.getTypeConfigAndValue(propertyType,configType,configCode);
    }

    @Override
    public int updateTypeConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.updateTypeConfig(pc);
    }

    @Override
    public PropertyConfigActivate updateTypeConfigSelect(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.updateTypeConfigSelect(pc);
    }

    @Override
    public int deleteTypeConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.deleteTypeConfig(pc);
    }

    @Override
    public List<PropertyConfigActivate> getOemConfigAndValue(String propertyType, String configType, String oemNo) {
        return propertyConfigActivateDao.getOemConfigAndValue(propertyType,configType,oemNo);
    }

    @Override
    public int addOemConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.addOemConfig(pc);
    }

    @Override
    public int updateOemConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.updateOemConfig(pc);
    }

    @Override
    public PropertyConfigActivate updateOemConfigSelect(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.updateOemConfigSelect(pc);
    }

    @Override
    public int deleteOemConfig(PropertyConfigActivate pc) {
        return propertyConfigActivateDao.deleteOemConfig(pc);
    }
}
