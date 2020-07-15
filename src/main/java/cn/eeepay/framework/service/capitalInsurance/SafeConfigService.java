package cn.eeepay.framework.service.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.SafeConfig;
import cn.eeepay.framework.model.capitalInsurance.SafeLadder;

import java.util.List;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 资金险service
 */
public interface SafeConfigService {


    List<SafeConfig> selectAllList(SafeConfig safe, Page<SafeConfig> page);

    SafeConfig getSafeConfig(int id);

    int saveSafeConfig(SafeConfig safe);

    int saveRouteScale(List<SafeConfig> safes);

    int saveSafeLadder(SafeLadder ladder);

    List<SafeConfig> getSafeConfigList();

	SafeConfig getSafeConfigByProCode(String proCode);
}
