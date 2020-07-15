package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.CountSet;
import cn.eeepay.framework.model.allAgent.GoodAllAgent;
import cn.eeepay.framework.model.allAgent.GoodsPrice;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author liuks
 * 商品查询service
 */
public interface GoodAllAgentService {

    List<GoodAllAgent> selectAllList(GoodAllAgent good, Page<GoodAllAgent> page);

    List<Map> goodsGroupQuery(Map group, Page<Map> page);

    int addGood(GoodAllAgent good,List<GoodsPrice> pricesList);

    GoodAllAgent getGood(int id);

    GoodAllAgent getGoodsCode(String goodsCode);

    int saveGood(GoodAllAgent good,List<GoodsPrice> pricesList,List<GoodsPrice> deleteList);

    List<GoodsPrice> getGoodsPrice(String  goodsCode);

    int updateGood(int id, int status);

    CountSet selectAllSum(GoodAllAgent good, Page<GoodAllAgent> page);

    int deleteGoodImg(int id, int status);

    int addGoodsGroup(Map group);

    int updateGoodsGroup(Map group);

    int updateListType(Map group);

    int deleteGoodsGroup(Map params);
}
