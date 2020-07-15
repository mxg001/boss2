package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.model.cjt.CjtPushTemplate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author tans
 * @date 2019/5/27 11:32
 */
public interface CjtPushTemplateDao {

    @Select("select * from cjt_push_template where type = #{type}")
    @ResultType(CjtPushTemplate.class)
    CjtPushTemplate selectFirstByType(@Param("type") String type);

    @Update("update cjt_push_template set content = #{content} where id = #{id}")
    int update(CjtPushTemplate cjtPushTemplate);
}
