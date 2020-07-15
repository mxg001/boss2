package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.FirewallDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.allAgent.FirewallService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/09/12.
 * @author  zja
 * 防火墙 service 实现
 */
@Service("firewallService")
public class FirewallServiceImpl implements FirewallService {

    @Resource
    private FirewallDao firewallDao;

    @Override
    public List<Map> selectSysConfig(Map params) {
        return firewallDao.selectSysConfig(params);
    }

    @Override
    public List<Map> selectRecordList(Map params, Page<Map> page) {
        return page==null ? firewallDao.selectRecordList2(params) : firewallDao.selectRecordList(params,page);
    }

    @Override
    public int deleteRecord(Map params) {
        return firewallDao.deleteRecord(params);
    }

    @Override
    public int updateRecord(Map params) {
        return firewallDao.updateRecord(params);
    }

    @Override
    public int addRecord(Map params) {
        return firewallDao.addRecord(params);
    }
}
