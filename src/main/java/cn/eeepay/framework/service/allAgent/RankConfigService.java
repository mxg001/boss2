package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.model.allAgent.RankConfig;

/**
 * Created by Administrator on 2018/12/5/005.
 * @author  liuks
 * 排行版设置service
 */
public interface RankConfigService {

    int saveRankConfig(RankConfig info);

    RankConfig getRankConfig();
}
