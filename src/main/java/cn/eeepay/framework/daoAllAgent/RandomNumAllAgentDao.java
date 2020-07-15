package cn.eeepay.framework.daoAllAgent;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author liuks
 * 获取序列
 */
public interface RandomNumAllAgentDao {

    @Select(
            "select nextval(#{code})"
    )
    int getSequence(@Param("code") String code);
}
