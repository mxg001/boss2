package cn.eeepay.framework.service.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.cjt.CjtProfitDetail;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 超级推收益明细 服务层
 *
 * @author tans
 * @date 2019-06-14
 */
public interface CjtProfitDetailService {

    /**
     * 条件查询超级推收益明细
     * @param cjtProfitDetail
     * @return
     */
    void selectPage(Page<CjtProfitDetail> page, CjtProfitDetail cjtProfitDetail);

    Map<String,Object> selectTotal(CjtProfitDetail baseInfo);

    Map<String,Object> selectTotalTrans(CjtProfitDetail baseInfo);

    void export(HttpServletResponse response, CjtProfitDetail item);

    Result rechargeBatch(List<String> orderNoList,String userType);
}
