package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrg;
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
public interface ExchangeActivateOrgDao {

    @SelectProvider(type=ExchangeActivateOrgDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateOrg.class)
    List<ExchangeActivateOrg> selectAllList(@Param("org") ExchangeActivateOrg org, @Param("page") Page<ExchangeActivateOrg> page);

    @SelectProvider(type=ExchangeActivateOrgDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateOrg.class)
    List<ExchangeActivateOrg> importDetailSelect(@Param("org")ExchangeActivateOrg org);

    @Update(
            " update yfb_org_info set org_status=#{state} where id=#{id}"
    )
    int updateOrgStatus(@Param("id") long id, @Param("state") int state);

    @Insert(
            "INSERT INTO yfb_org_info " +
                    " (org_name,org_code,org_status,org_logo,remark,create_time,sort,finance) " +
                    "VALUES " +
                    " (#{org.orgName},#{org.orgCode},'0',#{org.orgLogo},#{org.remark},NOW(),#{org.sort},#{org.finance})"
    )
    int addOrgManagement(@Param("org") ExchangeActivateOrg org);

    @Select(
            " select * from yfb_org_info "
    )
    List<ExchangeActivateOrg> getOrgManagementList();

    @Select(
            "select * from yfb_org_info where id=#{id}"
    )
    ExchangeActivateOrg getOrgManagementDetail(@Param("id") long id);

    @Update(
            "update yfb_org_info set org_name=#{org.orgName},org_logo=#{org.orgLogo},remark=#{org.remark}, " +
                    " sort=#{org.sort},finance=#{org.finance} " +
                    " where id=#{org.id} "
    )
    int updateOrgManagement(@Param("org") ExchangeActivateOrg org);

    @Update(
            "update yfb_org_info set org_name=#{org.orgName},remark=#{org.remark},sort=#{org.sort},finance=#{org.finance}" +
                    "  where id=#{org.id}"
    )
    int updateOrgManagementNoLogo(@Param("org") ExchangeActivateOrg org);

    @Select(
            "select * from yfb_org_info where org_name=#{orgName} "
    )
    List<ExchangeActivateOrg> checkOrgName(@Param("orgName") String orgName);

    @Select(
            "select * from yfb_org_info where org_name=#{orgName} and id!=#{id}"
    )
    List<ExchangeActivateOrg> checkOrgNameId(@Param("orgName") String orgName, @Param("id") long id);

    @Select(
            "select * from yfb_org_info where org_code=#{orgCode} "
    )
    List<ExchangeActivateOrg> getOrgManagementOrgCode(String orgCode);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateOrg org = (ExchangeActivateOrg) param.get("org");
            return new SQL(){{
                SELECT("org.*");
                FROM("yfb_org_info org");
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
