package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.OrgManagement;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 *
 */
public interface OrgManagementDao {

    @SelectProvider(type=OrgManagementDao.SqlProvider.class,method="selectAllList")
    @ResultType(OrgManagement.class)
    List<OrgManagement> selectAllList(@Param("org")OrgManagement org, @Param("page")Page<OrgManagement> page);

    @SelectProvider(type=OrgManagementDao.SqlProvider.class,method="selectAllList")
    @ResultType(OrgManagement.class)
    List<OrgManagement> importDetailSelect(@Param("org")OrgManagement org);

    @Update(
            " update rdmp_org_info set org_status=#{state} where id=#{id}"
    )
    int updateOrgStatus(@Param("id")long id, @Param("state")int state);

    @Insert(
            "INSERT INTO rdmp_org_info " +
                    " (org_name,org_code,org_status,org_logo,remark,create_time,sort,finance) " +
                    "VALUES " +
                    " (#{org.orgName},#{org.orgCode},'0',#{org.orgLogo},#{org.remark},NOW(),#{org.sort},#{org.finance})"
    )
    int addOrgManagement(@Param("org")OrgManagement org);

    @Select(
            " select * from rdmp_org_info "
    )
    List<OrgManagement> getOrgManagementList();

    @Select(
            "select * from rdmp_org_info where id=#{id}"
    )
    OrgManagement getOrgManagementDetail(@Param("id")long id);

    @Update(
            "update rdmp_org_info set org_name=#{org.orgName},org_logo=#{org.orgLogo},remark=#{org.remark}," +
                    " sort=#{org.sort},finance=#{org.finance} " +
                    " where id=#{org.id} "
    )
    int updateOrgManagement(@Param("org")OrgManagement org);

    @Update(
            "update rdmp_org_info set org_name=#{org.orgName},remark=#{org.remark},sort=#{org.sort},finance=#{org.finance}" +
                    " where id=#{org.id}"
    )
    int updateOrgManagementNoLogo(@Param("org")OrgManagement org);

    @Select(
            "select * from rdmp_org_info where org_name=#{orgName} "
    )
    List<OrgManagement> checkOrgName(@Param("orgName")String orgName);

    @Select(
            "select * from rdmp_org_info where org_name=#{orgName} and id!=#{id}"
    )
    List<OrgManagement> checkOrgNameId(@Param("orgName")String orgName,@Param("id")long id);

    @Select(
            "select * from rdmp_org_info where org_code=#{orgCode} "
    )
    List<OrgManagement> getOrgManagementOrgCode(String orgCode);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final OrgManagement org = (OrgManagement) param.get("org");
            return new SQL(){{
                SELECT("org.*");
                FROM("rdmp_org_info org");
                if(StringUtils.isNotBlank(org.getOrgName())){
                    WHERE("org.org_name like concat(#{org.orgName},'%') ");
                }
                if(StringUtils.isNotBlank(org.getFinance())){
                    WHERE("org.finance = #{org.finance} ");
                }
                ORDER_BY("org.sort ASC");
                ORDER_BY("org.create_time DESC");
            }}.toString();
        }
    }
}
