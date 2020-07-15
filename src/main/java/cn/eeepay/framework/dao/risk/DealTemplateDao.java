package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.DealTemplate;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author MXG
 * create 2018/12/20
 */
public interface DealTemplateDao {

    @Select("select * from black_data_deal_template where id=#{id} and status=1")
    @ResultType(DealTemplate.class)
    DealTemplate selectById(@Param("id") String id);

    @Select("select * from black_data_deal_template where template_no=#{templateNo} and status=1")
    @ResultType(DealTemplate.class)
    List<DealTemplate> selectByTemplateNoWithPage(@Param("page")Page<DealTemplate> page, @Param("templateNo")String templateNo);

    @Select("select * from black_data_deal_template where status=1 order by template_no")
    @ResultType(DealTemplate.class)
    List<DealTemplate> selectAllWithPage(@Param("page")Page<DealTemplate> page);

    @Insert("INSERT INTO black_data_deal_template(template_no,template_content,create_time,creator) " +
            "VALUES(#{template.templateNo},#{template.templateContent},NOW(),#{template.creator})")
    int add(@Param("template") DealTemplate template);

    @Update("update black_data_deal_template " +
            "set template_content=#{template.templateContent}," +
            "creator=#{template.creator} where id=#{template.id}")
    int update(@Param("template") DealTemplate template);

    //@Update("update black_data_deal_template set status=0 where id=#{id}")
    @Delete("DELETE FROM black_data_deal_template WHERE id=#{id}")
    int delete(@Param("id")String id);

    @Select("select * from black_data_deal_template where template_no <> '101'")
    @ResultType(DealTemplate.class)
    List<DealTemplate> selectAll();
}
