package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SensitiveWords;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author liuks
 * 敏感词dao
 */
public interface SensitiveWordsDao {
    @Insert(
            " INSERT INTO sensitive_words(sensitive_no,key_word,status,create_time,modify_time) " +
            "VALUES(#{info.sensitiveNo},#{info.keyWord},#{info.status},now(),now()); "
    )
    int inserSensitiveWords(@Param("info")SensitiveWords sensitiveWords);

    @Insert(
            "<script> INSERT INTO sensitive_words(sensitive_no,key_word,status,create_time,modify_time) VALUES " +
                    "<foreach  collection=\"list\" item=\"info\" index=\"index\" separator=\",\" > " +
                    "(#{info.sensitiveNo},#{info.keyWord},#{info.status},now(),now()) " +
                    " </foreach ></script>"
    )
    int batchInserSensitiveWords(@Param("list")List<SensitiveWords> list);


    @Update(
            "update sensitive_words set modify_time=now() where id=#{info.id};"
    )
    int updateSensitiveWords(@Param("info")SensitiveWords sensitiveWords);


    @Delete(
            "delete from sensitive_words where id=#{info.id};"
    )
    int deleteSensitiveWords(@Param("info")SensitiveWords sensitiveWords);



    @Delete(
            "<script> delete from sensitive_words where id in " +
                    "  <foreach  collection=\"list\" item=\"swItem\" open=\"(\" separator=\",\" close=\")\">" +
                    "   #{swItem} " +
                    "  </foreach> </script>"
    )
    int batchDeleteSensitiveWords(@Param("list")List list);


    @Select(" select id from sensitive_words where key_word=#{keyWord}")
    @ResultType(SensitiveWords.class)
    SensitiveWords selectListbyKeyWord(@Param("keyWord")String keyWord);

    /**
     *根据条件动态查询列表
     */
    @SelectProvider(type=SensitiveWordsDao.SqlProvider.class,method="selectAllList")
    @ResultType(SensitiveWords.class)
    List<SensitiveWords> selectAllList(@Param("info") SensitiveWords sensitiveWords, Page<SensitiveWords> page);

    @Select("select count(1) from sensitive_words where #{word} like concat('%',key_word,'%') and status = '1'")
    @ResultType(Integer.class)
    int selectContain(String word);


    class SqlProvider {
        public String selectAllList(final Map<String, Object> param) {
            final SensitiveWords info = (SensitiveWords) param.get("info");
            return new SQL() {
                {
                    SELECT("id,sensitive_no,key_word,status,create_time,modify_time");
                    FROM("sensitive_words");
                    if (StringUtils.isNotBlank(info.getSensitiveNo())) {
                        WHERE("sensitive_no=#{info.sensitiveNo}");
                    }
                    if(StringUtils.isNotBlank(info.getKeyWord())){
                        info.setKeyWord(info.getKeyWord()+"%");
                        WHERE("key_word like #{info.keyWord}");
                    }
                    ORDER_BY("modify_time DESC");
                    ORDER_BY("id DESC");
                }
            }.toString();
        }
    }
}
