package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgSourceConf;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface OrgSourceConfDao {
	@Update("update org_source_conf set status=#{status} where id=#{id}")
	long updateStatus(@Param("status")String status,@Param("id")Long id);
	
	@Select("select max(show_order) from org_source_conf where org_id = #{orgId}")
	int selectMaxOrder(@Param("orgId")Long orgId,@Param("type")String type,@Param("application")String application);
	
	@Update("update org_source_conf set status=#{status} where source_id=#{sourceId}")
	long updateStatusBySourceId(@Param("status")String status,@Param("sourceId")String sourceId);
	
	@Update("update org_source_conf set show_order=#{showOrder},update_time=#{updateTime},is_recommend=#{isRecommend} where id=#{id}")
	long update(OrgSourceConf conf);
	
	@Select("select count(1) from org_source_conf where org_id =#{orgId} and show_order =#{showOrder}" +
			" and application = #{application} and type =#{type} and id =#{id}")
	Integer getOrgConfByParams(@Param("orgId")int orgId, @Param("showOrder")Integer showOrder,@Param("application")String application, 
			@Param("type")String type,@Param("id")long id);

    @Select("select * from org_source_conf where org_id =#{orgId} and is_recommend =#{isRecommend}" +
            " and application = #{application} and type =#{type}")
    List<OrgSourceConf> getCountByRecommend(@Param("orgId")int orgId, @Param("isRecommend")String isRecommend,@Param("application")String application,
                               @Param("type")String type);
	
	@SelectProvider(type = SqlProvider.class, method = "isExistOrder")
	@ResultType(Integer.class)
	Integer isExistOrder(@Param("order")Integer order,@Param("idList")LinkedList<Long> idList,@Param("orgId")int orgId,
			@Param("application")String application,@Param("type")String type);
	
    @Insert("insert into org_source_conf (org_id,source_id,type,application,remark,CREATE_DATE) " +
            "values(#{orgId},#{sourceId},#{type},#{application},#{remark},sysdate())")
    long saveOrgSourceConf(OrgSourceConf conf);

    @Insert("insert into org_source_conf (org_id,source_id,type,application,show_order,create_date,is_recommend,status) " +
            "values(#{orgId},#{sourceId},#{type},#{application},#{showOrder},#{createDate},#{isRecommend},#{status})")
    long InsertOrgSourceConf(OrgSourceConf conf);
    
    @Select("select * from org_source_conf where id = #{id}")
    OrgSourceConf getOrgSourceById(Long id);
    
    @Select("select os.* from org_source_conf os LEFT JOIN org_info  o on os.org_id = o.org_id where o.org_name = '超级银行家' and application = '1' and os.source_id = #{sourceId} and os.type = #{type}")
    OrgSourceConf getBankersOrgSource(@Param("sourceId")Long sourceId,@Param("type")String type);
    
    @SelectProvider(type = OrgSourceConfDao.SqlProvider.class, method = "getOrgSourceConfcount")
    @ResultType(Integer.class)
    int getOrgSourceConfcount(@Param("baseInfo")OrgSourceConf orgSourceConf);
    
    @SelectProvider(type = SqlProvider.class, method = "selectOrgSourceConfPage")
    @ResultType(OrgSourceConf.class)
    List<OrgSourceConf> getOrgSourceConfList(@Param("baseInfo")OrgSourceConf baseInfo, @Param("page") Page<OrgSourceConf> page);
    
    @SelectProvider(type = SqlProvider.class, method = "getOldConfList")
    @ResultType(OrgSourceConf.class)
    List<OrgSourceConf> getOldConfList(@Param("baseInfo")OrgSourceConf orgSourceConf);
    
    @SelectProvider(type = OrgSourceConfDao.SqlProvider.class, method = "getPageOrgSourceConfList")
    @ResultType(OrgSourceConf.class)
    List<OrgSourceConf> getPageOrgSourceConfList(@Param("baseInfo")OrgSourceConf orgSourcConf, @Param("page")Page<OrgSourceConf> page);

    @Delete("DELETE FROM org_source_conf WHERE ID=#{id}")
    void deleteOrgSourceConf(OrgSourceConf orgSourcConf);

    public class SqlProvider{
    	public String isExistOrder(Map<String, Object> params){
    		StringBuffer sql = new StringBuffer();
    		final Integer order = (Integer) params.get("order"); 
    		final List<Long> idList = (List<Long>) params.get("idList");
    		final String application = (String) params.get("application");
    		final String type = (String) params.get("type");
    		final int orgId = (int) params.get("orgId");
    		sql.append("select count(1) from org_source_conf where type =#{type} and application = #{application} and org_id = #{orgId}");
    		if(null != order){
    			sql.append(" and show_order = #{order}");
    		}
    		if(idList.size() > 0){
    			sql.append(" and id not in (");
    			for (int i = 0; i < idList.size(); i++) {
					if(i < idList.size() - 1){
						sql.append(idList.get(i)).append(",");
					}else {
						sql.append(idList.get(i)).append(")");
					}
				}
    		}
    		return sql.toString();
    	}
    	public String getOldConfList(Map<String, Object> params){
    		StringBuffer sqlBuffer = new StringBuffer();
    		final OrgSourceConf baseInfoConf = (OrgSourceConf) params.get("baseInfo");
    		sqlBuffer.append("Select * from org_source_conf osc where 1 = 1");
    		if(-1 != baseInfoConf.getOrgId()){
    			sqlBuffer.append(" and osc.org_id = #{baseInfo.orgId} ");
    		}
    		if(StringUtils.isNotBlank(baseInfoConf.getApplication())){
    			sqlBuffer.append(" and osc.application = #{baseInfo.application} ");
            }
            if(StringUtils.isNotBlank(baseInfoConf.getType())){
            	sqlBuffer.append(" and osc.type = #{baseInfo.type} ");
            }
            sqlBuffer.append("order by show_order desc");
    		return sqlBuffer.toString();
    	}
    	public String getOrgSourceConfcount(Map<String, Object> params) {
            StringBuffer sql = new StringBuffer();
            final OrgSourceConf baseInfo = (OrgSourceConf) params.get("baseInfo");
            sql.append("SELECT count(1)"
            		);
            sql.append(" FROM org_source_conf osc ");
            sql.append("where 1=1 ");
            if(baseInfo != null && baseInfo.getOrgId() != -1){
                sql.append(" and osc.org_id = #{baseInfo.orgId} ");
            }
            if(baseInfo.getSourceId()!=0){
                sql.append(" and osc.source_id = #{baseInfo.sourceId} ");
            }
            if(StringUtils.isNotBlank(baseInfo.getApplication())){
                sql.append(" and osc.application = #{baseInfo.application} ");
            }
            if(StringUtils.isNotBlank(baseInfo.getType())){
                sql.append(" and osc.type = #{baseInfo.type} ");
            }
            if("on".equals(baseInfo.getStatus())||"off".equals(baseInfo.getStatus())){
                sql.append(" and osc.status = #{baseInfo.status} ");
            }
            return sql.toString();
        }
    	
    	public String getPageOrgSourceConfList(Map<String, Object> params) {
            StringBuffer sql = new StringBuffer();
            final OrgSourceConf baseInfo = (OrgSourceConf) params.get("baseInfo");
            sql.append("SELECT osc.id,osc.source_id,oi.org_name,osc.type,osc.application,create_time" +
            		"osc.remark,osc.show_order,osc.status,osc.create_by,osc.update_time,osc.update_by,osc.is_recommend,");
            String type=baseInfo.getType();
            if("1".equals(type)){
                sql.append(" cs.bank_name as sourceName,cs.bank_nick_name as sourceNickName " );
            }
            if("2".equals(type)){
                sql.append(" ls.company_name as sourceName,ls.loan_alias as sourceNickName " );
            }
            sql.append(" FROM org_source_conf osc ");
            sql.append(" left join org_info oi ON osc.org_id=oi.org_id ");
            if("1".equals(type)){
                sql.append(" LEFT JOIN creditcard_source cs ON osc.source_id=cs.id " );
            }
            if("2".equals(type)){
                sql.append( " LEFT JOIN loan_source ls ON osc.source_id=ls.id " );
            }
            sql.append("where 1=1 ");
            if(baseInfo != null && baseInfo.getOrgId() != -1){
                sql.append(" and osc.org_id = #{baseInfo.orgId} ");
            }
            if(baseInfo.getSourceId()!=0){
                sql.append(" and osc.source_id = #{baseInfo.sourceId} ");
            }
            if(StringUtils.isNotBlank(baseInfo.getType())){
                sql.append(" and osc.type = #{baseInfo.type} ");
            }
            return sql.toString();
        }
    	
        public String selectOrgSourceConfPage(Map<String, Object> param) {
            StringBuffer sql = new StringBuffer();
            final OrgSourceConf baseInfo = (OrgSourceConf) param.get("baseInfo");
            sql.append("SELECT osc.id,osc.source_id,oi.org_name,osc.type,osc.application,osc.remark," +
            		"osc.create_date,osc.show_order,osc.is_recommend,osc.status,");
            String type=baseInfo.getType();
            if("1".equals(type)){
                sql.append("cs.code,cs.bank_name as sourceName,cs.bank_nick_name as sourceNickName,cs.special_image " );
            }
            if("2".equals(type)){
                sql.append("ls.id as code,ls.loan_product as sourceName,ls.loan_alias as sourceNickName,ls.special_image " );
            }
            sql.append(" FROM org_source_conf osc ");
            sql.append(" left join org_info oi ON osc.org_id=oi.org_id ");
            if("1".equals(type)){
                sql.append(" LEFT JOIN creditcard_source cs ON osc.source_id=cs.id " );
            }
            if("2".equals(type)){
                sql.append( " LEFT JOIN loan_source ls ON osc.source_id=ls.id " );
            }
            sql.append("where 1=1 ");
            if(baseInfo != null && baseInfo.getOrgId() != -1){
                sql.append(" and osc.org_id = #{baseInfo.orgId} ");
            }
            if(baseInfo.getSourceId()!=0){
                sql.append(" and osc.source_id = #{baseInfo.sourceId} ");
            }
            if(StringUtils.isNotBlank(baseInfo.getApplication())){
                sql.append(" and osc.application = #{baseInfo.application} ");
            }
            if(StringUtils.isNotBlank(baseInfo.getType())){
                sql.append(" and osc.type = #{baseInfo.type} ");
            }
            if(StringUtils.isNotBlank(baseInfo.getStatus())){
                sql.append(" and osc.status = #{baseInfo.status} ");
            }
            sql.append( " order by osc.show_order" );
            return sql.toString();
        }

    }
}
