package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.SensitiveWordsDao;
import cn.eeepay.framework.daoSuperbank.SuperSensitiveWordsDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SensitiveWords;
import cn.eeepay.framework.service.SensitiveWordsService;
import cn.eeepay.framework.service.SuperSensitiveWordsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuks
 * 敏感词service实现类
 */
@Service("superSensitiveWordsService")
@Transactional
public class SuperSensitiveWordsServiceImpl implements SuperSensitiveWordsService {

    @Resource
    private SuperSensitiveWordsDao superSensitiveWordsDao ;

    @Override
    public List<SensitiveWords> selectAllList(SensitiveWords sensitiveWords, Page<SensitiveWords> page) {
        return superSensitiveWordsDao.selectAllList(sensitiveWords,page);
    }

    @Override
    public int insertSensitiveWords(SensitiveWords sensitiveWords) {
        return superSensitiveWordsDao.inserSensitiveWords(sensitiveWords);
    }

    @Override
    public int deleteSensitiveWords(SensitiveWords sensitiveWords) {
        return superSensitiveWordsDao.deleteSensitiveWords(sensitiveWords);
    }

    @Override
    public int batchDeleteSensitiveWords(List list) {
        return superSensitiveWordsDao.batchDeleteSensitiveWords(list);
    }

    @Override
    public int batchInsertSensitiveWords(List<SensitiveWords> list) {
        return superSensitiveWordsDao.batchInserSensitiveWords(list);
    }

    @Override
    public int updateSensitiveWords(SensitiveWords sensitiveWords) {
        return superSensitiveWordsDao.updateSensitiveWords(sensitiveWords);
    }

    @Override
    public SensitiveWords selectListbyKeyWord(String keyWord) {
        return superSensitiveWordsDao.selectListbyKeyWord(keyWord);
    }

}
