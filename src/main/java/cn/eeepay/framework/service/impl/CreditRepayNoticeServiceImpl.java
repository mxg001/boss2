package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CreditRepayNoticeDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayNotice;
import cn.eeepay.framework.service.CreditRepayNoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/26/026.\
 * @author liuks
 * 信用卡还款公告service实现类
 */
@Service("creditRepayNoticeService")
@Transactional
public  class CreditRepayNoticeServiceImpl implements CreditRepayNoticeService {

    @Resource
    private CreditRepayNoticeDao creditRepayNoticeDao;

    /**
     * 通过id查询通告
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> selectById(int id) {
        Map<String, Object> map = new HashMap<>();
        CreditRepayNotice notice = creditRepayNoticeDao.selectById(id);
        map.put("notice",notice);
        map.put("status", true);
        return map;
    }

    /**
     * 新增通告
     * @param notice
     * @return
     */
    @Override
    public int insertCreditRepayNotice(CreditRepayNotice notice) {
        return creditRepayNoticeDao.insertCreditRepayNotice(notice);
    }

    /**
     * 修改通告
     * @param notice
     * @return
     */
    @Override
    public int updateCreditRepayNotice(CreditRepayNotice notice) {
        return creditRepayNoticeDao.updateCreditRepayNotice(notice);
    }

    /**
     * 下发通告
     * @param notice
     * @return
     */
    @Override
    public int issueCreditRepayNotice(CreditRepayNotice notice) {
        notice.setStatus(2);//设置公告为下发状态
        return creditRepayNoticeDao.issueCreditRepayNoticeStatus(notice);
    }

    /**
     * 删除通告
     * @param notice
     * @return
     */
    @Override
    public int deleteCreditRepayNotice(CreditRepayNotice notice) {
        notice.setStatus(3);//设置公告为删除状态
        return creditRepayNoticeDao.deleteCreditRepayNoticeStatus(notice);
    }

    /**
     * 收回通告
     * @param notice
     * @return
     */
    @Override
    public int reclaimCreditRepayNotice(CreditRepayNotice notice) {
        notice.setStatus(1);//设置公告为待下发状态
        return creditRepayNoticeDao.reclaimCreditRepayNoticeStatus(notice);
    }

    /**
     * 查询通告
     * @param notice
     * @param page
     * @return
     */
    @Override
    public List<CreditRepayNotice> selectAllList(CreditRepayNotice notice, Page<CreditRepayNotice> page) {
        return creditRepayNoticeDao.selectAllList(notice,page);
    }
}

