package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SensitiveWords;

import java.util.List;

/**
 * @author liuks
 * 敏感词service接口
 */
public interface SensitiveWordsService {

    List<SensitiveWords> selectAllList(SensitiveWords sensitiveWords, Page<SensitiveWords> page);

    int insertSensitiveWords(SensitiveWords sensitiveWords);

    int deleteSensitiveWords(SensitiveWords sensitiveWords);

    int batchDeleteSensitiveWords(List list);

    int batchInsertSensitiveWords(List<SensitiveWords> list);

    int updateSensitiveWords(SensitiveWords sensitiveWords);

    SensitiveWords selectListbyKeyWord(String keyWord);

    boolean selectContain(String word);
}
