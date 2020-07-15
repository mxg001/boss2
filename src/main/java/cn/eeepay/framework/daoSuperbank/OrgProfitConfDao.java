package cn.eeepay.framework.daoSuperbank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgProfitConf;

/**
 * 品牌组织分润配置
 * @author Administrator
 *
 */
public interface OrgProfitConfDao {

	@SelectProvider(type = SqlProvider.class, method = "selectOrgProfitPage")
    @ResultType(OrgProfitConf.class)
	List<OrgProfitConf> getByPager(@Param("orgConf") OrgProfitConf conf, @Param("page")Page<OrgProfitConf> page);
	
	@Insert("insert into org_profit_conf (org_id,product_type,profit_type,selt_member,selt_manager,selt_banker,update_by,update_date) " +
			"values(#{orgId},#{productType},#{profitType},#{seltMember},#{seltManager},#{seltBanker},#{updateBy},sysdate())")
	long saveOrgProfitConf(OrgProfitConf conf);
	
	@Update("update org_profit_conf set selt_member=#{seltMember},selt_manager=#{seltManager},selt_banker=#{seltBanker} where id=#{id}")
	long updOrgProfitConf(OrgProfitConf conf);
	
	@Select("select count(org_id) from org_profit_conf where org_id=#{orgId}")
	int isExist(@Param("orgId") String orgId);
	
	@Select("select org_id,product_type,profit_type,selt_member,selt_manager,selt_banker,update_by,update_date from org_profit_conf where id=#{id}")
	OrgProfitConf selectOrgProfit(@Param("id") Long id);
	
	public class SqlProvider{
        public String selectOrgProfitPage(Map<String, Object> param){
            StringBuffer sql = new StringBuffer();
            final OrgProfitConf baseInfo = (OrgProfitConf) param.get("orgConf");
        	sql.append("SELECT of.org_id,oi.org_name,of.product_type,of.profit_type,of.selt_member,of.selt_manager,of.selt_banker,of.id ");
        	sql.append(" FROM org_profit_conf of LEFT JOIN  org_info oi on of.org_id = oi.org_id where 1=1 ");
            if(baseInfo != null && baseInfo.getOrgId() != null && baseInfo.getOrgId() != -1){
            	sql.append(" and of.org_id = #{orgConf.orgId} ");
            }
        	sql.append(" order by of.id desc");
            
            return sql.toString();
        }
    }
}