package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.Sequence;
import org.apache.ibatis.annotations.*;

/**
 * 超级银行家序列dao
 */
public interface SequenceDao {

    @Select("select nextval(#{key})")
    @ResultType(String.class)
    String getValue(@Param("key")String key);

    @Insert("insert into sequence(name,current_value,increment) "
            + "values(#{seq.name},#{seq.currentValue},#{seq.increment})")
    int insertSeq(@Param("seq") Sequence seq);
}
