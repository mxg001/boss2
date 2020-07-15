package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.SensitiveWordsDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SensitiveWords;
import cn.eeepay.framework.service.SensitiveWordsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuks
 * 敏感词service实现类
 */
@Service("sensitiveWordsService")
@Transactional
public class SensitiveWordsServiceImpl implements SensitiveWordsService {

    @Resource
    private SensitiveWordsDao sensitiveWordsDao ;

    @Override
    public List<SensitiveWords> selectAllList(SensitiveWords sensitiveWords, Page<SensitiveWords> page) {
        return sensitiveWordsDao.selectAllList(sensitiveWords,page);
    }

    @Override
    public int insertSensitiveWords(SensitiveWords sensitiveWords) {
        return sensitiveWordsDao.inserSensitiveWords(sensitiveWords);
    }

    @Override
    public int deleteSensitiveWords(SensitiveWords sensitiveWords) {
        return sensitiveWordsDao.deleteSensitiveWords(sensitiveWords);
    }

    @Override
    public int batchDeleteSensitiveWords(List list) {
        return sensitiveWordsDao.batchDeleteSensitiveWords(list);
    }

    @Override
    public int batchInsertSensitiveWords(List<SensitiveWords> list) {
        return sensitiveWordsDao.batchInserSensitiveWords(list);
    }

    @Override
    public int updateSensitiveWords(SensitiveWords sensitiveWords) {
        return sensitiveWordsDao.updateSensitiveWords(sensitiveWords);
    }

    @Override
    public SensitiveWords selectListbyKeyWord(String keyWord) {
        return sensitiveWordsDao.selectListbyKeyWord(keyWord);
    }

    @Override
    public boolean selectContain(String word) {
        if(StringUtils.isBlank(word)){
            return false;
        }
        int num = sensitiveWordsDao.selectContain(word);
        if(num > 0){
            return true;
        }
        return false;
    }

}
