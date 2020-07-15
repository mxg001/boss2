package cn.eeepay.framework.service.luckDraw;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CouponActivityEntity;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.luckDraw.Prize;
import cn.eeepay.framework.model.luckDraw.PrizeBlacklist;
import cn.eeepay.framework.model.luckDraw.PrizeConfigure;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/6/006.
 * @author  liuks
 * 奖项配置service
 */
public interface PrizeConfigureService {

    PrizeConfigure getPrizeConfigure(String funcCode);

    int updatePrizeConfigure(PrizeConfigure pc);

    List<Prize>  getPrizeList(String funcCode);

    Prize getPrize(int id);

    int addPrize(Prize prize, Map<String, Object> msg);

    int updatePrize(Prize prize, Map<String, Object> msg);

    List<CouponActivityEntity> getCouponList(String funcCode);

    int closeOpenPrize(int id, int status);

    List<PrizeBlacklist> getPrizeBlacklist(PrizeBlacklist blacklist, Page<PrizeBlacklist> page);

    int addPrizeBlacklist(PrizeBlacklist blacklist,Map<String, Object> msg);

    int deleteBlacklist(int id);

    MerchantInfo getMerchantInfo(String merchantNo);

    int initializationAwardsConfigCount();
}
