package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedOrg;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface RedOrgDao {

    @SelectProvider(type=SqlProvider.class, method="selectRedOrg")
    @ResultType(RedOrg.class)
    List<RedOrg> selectRedOrg(@Param("redOrg")RedOrg redOrg, @Param("page")Page<RedOrg> page);

    @SelectProvider(type=SqlProvider.class, method="selectRedOrgListAll")
    @ResultType(RedOrg.class)
	List<RedOrg> selectRedOrgListAll(@Param("redOrg")RedOrg redOrg);
    
    @Delete("delete from red_org where id = #{id}")
    int delete(String id);

    @Insert("insert into red_org(bus_code,org_id,org_name,create_time,operator)" +
            " values(#{busCode},#{orgId},#{orgName},#{createTime},#{operator})")
    int insert(RedOrg redOrg);

    @Delete("delete from red_org where bus_code=#{busCode} and org_id=#{orgId}")
    int deleteRedOrg(RedOrg redOrg);

    @Select("select count(1) from red_org where bus_code=#{busCode} and org_id=#{orgId}")
    int checkExists(RedOrg redOrg);
    
    @Insert("insert ignore into red_org(bus_code,org_id,org_name,create_time,operator)" +
            " values(#{busCode},#{orgId},#{orgName},#{createTime},#{operator})")
    int insertIgnore(RedOrg redOrg);

    class SqlProvider{
        public String selectRedOrg(Map<String, Object> param){
            RedOrg redOrg = (RedOrg) param.get("redOrg");
            SQL sql = new SQL();
            sql.SELECT("id,org_id,org_name");
            sql.FROM("red_org");
            if(redOrg != null){
                if(StringUtils.isNotBlank(redOrg.getBusCode())){
                    sql.WHERE("bus_code=#{redOrg.busCode}");
                }
                if(redOrg.getOrgId() != -1){
                    sql.WHERE("org_id=#{redOrg.orgId}");
                }
            }
            sql.ORDER_BY("create_time desc");
            return sql.toString();
        }
        
        public String selectRedOrgListAll(Map<String, Object> param){
            RedOrg redOrg = (RedOrg) param.get("redOrg");
            SQL sql = new SQL();
            sql.SELECT("id,org_id,org_name");
            sql.FROM("red_org");
            if(redOrg != null){
                if(StringUtils.isNotBlank(redOrg.getBusCode())){
                    sql.WHERE("bus_code=#{redOrg.busCode}");
                }
                if(redOrg.getOrgId() !=null && redOrg.getOrgId() != -1){
                    sql.WHERE("org_id=#{redOrg.orgId}");
                }
            }
            sql.ORDER_BY("create_time desc");
            return sql.toString();
        }
    }

}
