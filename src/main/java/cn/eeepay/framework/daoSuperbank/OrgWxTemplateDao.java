package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgWxTemplate;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface OrgWxTemplateDao {

    @Insert("insert into org_wx_template (ORG_ID,TYPE,TEMPLATE_ID,CREATE_DATE,CREATE_BY) " +
            "values(#{orgId},#{type},#{templateId},#{createDate},#{createBy})")
    long saveOrgWxTemplate(OrgWxTemplate orgWxTemplate);

    @Update("update org_wx_template set TEMPLATE_ID=#{templateId},UPDATE_DATE=#{updateDate},UPDATE_BY=#{updateBy} where id=#{id}")
    long updOrgWxTemplate(OrgWxTemplate orgWxTemplate);

    @SelectProvider(type = OrgWxTemplateDao.SqlProvider.class, method = "selectOrgWxTemplatePage")
    @ResultType(OrgWxTemplate.class)
    List<OrgWxTemplate> getByPager(@Param("baseInfo") OrgWxTemplate conf, @Param("page")Page<OrgWxTemplate> page);

    public class SqlProvider{
        public String selectOrgWxTemplatePage(Map<String, Object> param){
            StringBuffer sql = new StringBuffer();
            final OrgWxTemplate baseInfo = (OrgWxTemplate) param.get("baseInfo");
            sql.append("select owt.id,owt.org_id,oi.org_name,owt.TYPE,owt.TEMPLATE_ID ");
            sql.append(" from org_wx_template owt inner join org_info oi on  owt.ORG_ID=oi.ORG_ID ");
            if( baseInfo.getOrgId() != -1){
                sql.append(" and oi.ORG_ID = #{baseInfo.orgId} ");
            }
            sql.append(" order by owt.id desc");

            return sql.toString();
        }
    }
}
