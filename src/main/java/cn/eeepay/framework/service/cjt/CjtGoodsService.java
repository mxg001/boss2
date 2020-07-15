package cn.eeepay.framework.service.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.cjt.CjtGoods;

import java.util.List;

/**
 * 超级推商品 服务层
 *
 * @author tans
 * @date 2019-05-28
 */
public interface CjtGoodsService {

    /**
     * 条件查询超级推商品
     * @param cjtGoods
     * @return
     */
    void selectCjtGoodsPage(Page<CjtGoods> page, CjtGoods cjtGoods);

    /**
     * 查询超级推商品
     * @param goodsCode
     * @return
     */
    CjtGoods selectCjtGoodsDetail(String goodsCode);

    /**
     * 新增超级推商品
     * @param cjtGoods
     * @return
     */
    int insertCjtGoods(CjtGoods cjtGoods);

    /**
     * 修改超级推商品
     * @param cjtGoods
     * @return
     */
    int updateCjtGoods(CjtGoods cjtGoods);

    int updateStatus(CjtGoods cjtGoods);

    int updateWhiteStatus(CjtGoods cjtGoods);

    List<HardwareProduct> selectCjtHpList();
}
