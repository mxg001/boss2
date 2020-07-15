package cn.eeepay.framework.serviceImpl.sysUser;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.DynamicDataSourceHolder;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DataSourceSwitch {

    private final static Logger log = LoggerFactory.getLogger(DataSourceSwitch.class);

    public void switchSlave() {
        DynamicDataSourceHolder.putDataSource(Constants.DATA_SOURCE_SLAVE);
        log.info("切换到slave数据源");
    }

    public void switchMaster() {
        DynamicDataSourceHolder.putDataSource(Constants.DATA_SOURCE_MASTER);
        log.info("切换到master数据源");
    }

    public void clearDbType(){
        DynamicDataSourceHolder.clearDbType();
        log.info("清除数据源");
    }

}
