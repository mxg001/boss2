package cn.eeepay.framework.service.impl.capitalInsurance;

import cn.eeepay.framework.dao.capitalInsurance.SafeConfigDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.SafeConfig;
import cn.eeepay.framework.model.capitalInsurance.SafeLadder;
import cn.eeepay.framework.service.capitalInsurance.SafeConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 资金险service实现类
 */
@Service("safeConfigService")
public class SafeConfigServiceImpl implements SafeConfigService {


    @Resource
    private SafeConfigDao safeConfigDao;

    @Override
    public List<SafeConfig> selectAllList(SafeConfig safe, Page<SafeConfig> page) {
        return safeConfigDao.selectAllList(safe,page);
    }

    @Override
    public SafeConfig getSafeConfig(int id) {
        SafeConfig safe=safeConfigDao.getSafeConfig(id);
        if(safe!=null&&safe.getProCode()!=null){
            List<SafeLadder> list=safeConfigDao.getSafeLadderList(safe.getProCode());
            if(list!=null&&list.size()>0){
                safe.setSafeLadder(list);
            }
        }
        return safe;
    }

    @Override
    public int saveSafeConfig(SafeConfig safe) {
        return safeConfigDao.saveSafeConfig(safe);
    }

    @Override
    public int saveRouteScale(List<SafeConfig> safes) {
        int i=0;
        for(SafeConfig safe:safes){
            i+=safeConfigDao.saveRouteScale(safe);
        }
        return i;
    }

    @Override
    public int saveSafeLadder(SafeLadder ladder) {
        return safeConfigDao.saveSafeLadder(ladder);
    }

    @Override
    public List<SafeConfig> getSafeConfigList() {
        return safeConfigDao.getSafeConfigList();
    }

	@Override
	public SafeConfig getSafeConfigByProCode(String proCode) {
		 return safeConfigDao.getSafeConfigByProCode(proCode);
	}
}
