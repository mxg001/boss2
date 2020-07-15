package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.RankConfigDao;
import cn.eeepay.framework.model.allAgent.RankConfig;
import cn.eeepay.framework.model.allAgent.RankReward;
import cn.eeepay.framework.service.allAgent.RankConfigService;
import cn.eeepay.framework.util.CommonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/12/5/005.
 * @author  liuks
 * 排行版设置service实现类
 */
@Service("rankConfigService")
public class RankConfigServiceImpl implements RankConfigService {

    @Resource
    private RankConfigDao rankConfigDao;

    @Override
    public int saveRankConfig(RankConfig info) {
        int num=0;
        if(info.getId()==null){
            //新增
            num=rankConfigDao.addRankConfig(info);
            if(num>0){
                saveRankRewardList(info);
            }
        }else{
            //修改
            num=rankConfigDao.updateRankConfig(info);
            if(num>0){
                saveRankRewardList(info);
            }
        }
        return num;
    }

    private int saveRankRewardList(RankConfig info){
        if(info.getId()!=null&&info.getRankRewardList()!=null&&info.getRankRewardList().size()>0){
            rankConfigDao.deleteRankRewardList(info.getId());
            int num=0;
            for(int i=0;i<info.getRankRewardList().size();i++){
                RankReward item=info.getRankRewardList().get(i);
                item.setRankConfigId(info.getId());
                num=num+rankConfigDao.addRankReward(item);
            }
            return num;
        }
        return 0;
    }

    @Override
    public RankConfig getRankConfig() {
        RankConfig info=rankConfigDao.getRankConfig();
        if(info!=null){
            if(StringUtils.isNotBlank(info.getBrandCodeSet())){
                String[] strs=info.getBrandCodeSet().split(",");
                info.setBrandCodeList(Arrays.asList(strs));
            }
            info.setRankRewardList(rankConfigDao.getRankRewardList(info.getId()));
            if(StringUtils.isNotBlank(info.getRankImg())){
                info.setRankImg(CommonUtil.getImgUrlAgent(info.getRankImg()));
            }
        }
        return info;
    }
}
