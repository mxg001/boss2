package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.action.SuperCollectionAction;
import cn.eeepay.framework.dao.SuperCollectionDao;
import cn.eeepay.framework.model.SuperCollection;
import cn.eeepay.framework.service.SuperCollectionService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/12/18/018.
 * @author liuks
 * 超级还款设置service实现类
 */
@Service("superCollectionService")
@Transactional
public class SuperCollectionServiceImpl implements SuperCollectionService {

    @Resource
    private SuperCollectionDao superCollectionDao;

    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public SuperCollection selectByNumber(String number) {
        return superCollectionDao.selectByNumber(number);
    }

    @Override
    public int saveSuperCollection(SuperCollection sc) {
        int ret=superCollectionDao.saveSuperCollection(sc);
        if (ret > 0) {
            SuperCollection resultSc=selectByNumber(SuperCollectionAction.NUMBER);
            setRedisFlush(resultSc);
        }
        return ret;
    }

    @Override
    public void setRedisFlush(SuperCollection sc) {
        if(redisTemplate.opsForHash().hasKey(SuperCollectionAction.SUPERPOS,SuperCollectionAction.STARTTIME)){
            if(sc.getStartTime()!=null){
                redisTemplate.opsForHash().put(SuperCollectionAction.SUPERPOS,SuperCollectionAction.STARTTIME,sc.getStartTime().replaceAll(":",""));
            }
        }
        if(redisTemplate.opsForHash().hasKey(SuperCollectionAction.SUPERPOS,SuperCollectionAction.ENDTIME)){
            if(sc.getEndTime()!=null){
                redisTemplate.opsForHash().put(SuperCollectionAction.SUPERPOS,SuperCollectionAction.ENDTIME,sc.getEndTime().replaceAll(":",""));
            }
        }
        if(redisTemplate.opsForHash().hasKey(SuperCollectionAction.SUPERPOS,SuperCollectionAction.TOTALQOUTA)){
            if(sc.getDayLines()!=null){
                redisTemplate.opsForHash().put(SuperCollectionAction.SUPERPOS,SuperCollectionAction.TOTALQOUTA,sc.getDayLines().toString());
            }
        }
    }
}
