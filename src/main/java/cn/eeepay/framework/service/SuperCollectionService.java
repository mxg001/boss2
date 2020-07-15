package cn.eeepay.framework.service;

import cn.eeepay.framework.model.SuperCollection;

/**
 * Created by Administrator on 2017/12/18/018.
 * @author liuks
 * 超级还款设置service
 */
public interface SuperCollectionService {

    SuperCollection selectByNumber(String number);

    int saveSuperCollection(SuperCollection sc);

    void setRedisFlush(SuperCollection sc);
}
