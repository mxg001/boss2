package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayNotice;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/26/026.
 * @author liuks
 * 信用卡还款公告service接口
 */
public interface CreditRepayNoticeService {

    /**
     *动态分页查询
     */
    List<CreditRepayNotice> selectAllList(CreditRepayNotice notice, Page<CreditRepayNotice> page);

    /**
     *根据公告id 查询公告
     * @param id
     * @return
     */
    Map<String, Object> selectById(int id);

    /**
     * 新增公告
     */
    int insertCreditRepayNotice(CreditRepayNotice notice);

    /**
     * 修改公告
     */
    int updateCreditRepayNotice(CreditRepayNotice notice);

    /**
     * 下发公告
     */
    int issueCreditRepayNotice(CreditRepayNotice notice);

    /**
     * 删除公告
     */
    int deleteCreditRepayNotice(CreditRepayNotice notice);

    /**
     * 收回公告
     */
    int reclaimCreditRepayNotice(CreditRepayNotice notice);
}
