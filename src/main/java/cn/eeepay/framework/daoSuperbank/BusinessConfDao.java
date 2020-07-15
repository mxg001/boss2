package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.BusinessConf;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BusinessConfDao {

    @Select("select * from business_conf")
    List<BusinessConf> selectBusinessConfList();
}
