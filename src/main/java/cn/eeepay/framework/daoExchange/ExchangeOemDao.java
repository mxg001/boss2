package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.AgentOem;
import cn.eeepay.framework.model.exchange.ExchangeOem;
import cn.eeepay.framework.model.exchange.ProductOem;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/13/013.
 * @author  liuks
 * oem dao
 */
public interface ExchangeOemDao {

    @SelectProvider(type=ExchangeOemDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeOem.class)
    List<ExchangeOem> selectAllList(@Param("oem")ExchangeOem oem, @Param("page")Page<ExchangeOem> page);

    @SelectProvider(type=ExchangeOemDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeOem.class)
    List<ExchangeOem> importDetailSelect(@Param("oem")ExchangeOem oem);

    @Insert(
            "INSERT INTO rdmp_oem_service (oem_no,oem_name,create_time,company_no,company_name,service_phone, " +
                    " android_app_key,android_master_secret,ios_app_key,ios_master_secret, " +
                    " public_account,public_account_name,appid,secret,encoding_aes_key, " +
                    " wx_token,wx_ticket,remark,mailbox,agreement,client_appid,client_secret )" +
                    " VALUES(#{oem.oemNo},#{oem.oemName},NOW(),#{oem.companyNo},#{oem.companyName},#{oem.servicePhone}, " +
                    " #{oem.androidAppKey},#{oem.androidMasterSecret},#{oem.iosAppKey},#{oem.iosMasterSecret}, " +
                    " #{oem.publicAccount},#{oem.publicAccountName},#{oem.appid},#{oem.secret},#{oem.encodingAesKey}," +
                    " #{oem.wxToken},#{oem.wxTicket},#{oem.remark},#{oem.mailbox},#{oem.agreement},#{oem.clientAppid}," +
                    " #{oem.clientSecret})"
    )
    int addExchangeOem(@Param("oem")ExchangeOem oem);

    @Select(
            "select oem.* from rdmp_oem_service oem where oem.id=#{id}"
    )
    ExchangeOem getExchangeOem(@Param("id")long id);

    @Update(
            "update rdmp_oem_service set oem_no=#{oem.oemNo},oem_name=#{oem.oemName},company_no=#{oem.companyNo}, " +
                    " company_name=#{oem.companyName},service_phone=#{oem.servicePhone},android_app_key=#{oem.androidAppKey}, " +
                    " android_master_secret=#{oem.androidMasterSecret},ios_app_key=#{oem.iosAppKey}," +
                    " ios_master_secret=#{oem.iosMasterSecret},public_account=#{oem.publicAccount}," +
                    " public_account_name=#{oem.publicAccountName},appid=#{oem.appid},secret=#{oem.secret}, " +
                    " encoding_aes_key=#{oem.encodingAesKey},wx_token=#{oem.wxToken},wx_ticket=#{oem.wxTicket}, " +
                    " remark=#{oem.remark},mailbox=#{oem.mailbox},agreement=#{oem.agreement},client_appid=#{oem.clientAppid}," +
                    " client_secret=#{oem.clientSecret} " +
                    " where id=#{oem.id}"
    )
    int updateExchangeOem(@Param("oem")ExchangeOem oem);

    @Select(
            "select * from rdmp_oem_agent where oem_no=#{oemNo} and agent_level=#{agentLevel}"
    )
    AgentOem getAgentOem(@Param("oemNo")String oemNo,@Param("agentLevel")String agentLevel);

    @Insert(
            "INSERT INTO rdmp_oem_agent (oem_no,agent_no,agent_level,agent_node,team_id,create_time) " +
                    "VALUES(#{agent.oemNo},#{agent.agentNo},'1',#{agent.agentNode},#{agent.teamId},NOW())"
    )
    void addAgentOem(@Param("agent")AgentOem agentOem);

    @Select(
            "select oem.* from rdmp_oem_service oem "
    )
    List<ExchangeOem> getOemList();

    @SelectProvider(type=ExchangeOemDao.SqlProvider.class,method="selectProductOemList")
    @ResultType(ProductOem.class)
    List<ProductOem> selectProductOemList(@Param("proOem")ProductOem proOem,@Param("page")Page<ProductOem> page);

    @Update(
            "update rdmp_oem_product_info set shelve=#{state} where id=#{id}"
    )
    int updateProductOemShelve(@Param("id")long id,@Param("state") String state);

    @Select(
            "select * from rdmp_oem_product_info where id=#{id}"
    )
    ProductOem getProductOem(@Param("id")Long id);

    @Insert(
            "INSERT INTO rdmp_oem_product_info(p_id,oem_no,shelve,brand_price,create_time) " +
                    " VALUES(#{proOem.pId},#{proOem.oemNo},#{proOem.shelve},#{proOem.brandPrice},NOW())"
    )
    int addProductOem(@Param("proOem")ProductOem proOem);

    @Select(
            "select proOem.*,oem.oem_name,pro.product_name,type.type_code,type.type_name,org.org_code,org.org_name " +
                    " from rdmp_oem_product_info proOem " +
                    " LEFT JOIN rdmp_oem_service oem ON oem.oem_no=proOem.oem_no "+
                    " LEFT JOIN rdmp_product_info pro ON pro.id=proOem.p_id " +
                    " LEFT JOIN rdmp_product_type type ON type.type_code=pro.type_code " +
                    " LEFT JOIN rdmp_org_info org ON org.org_code=type.org_code " +
                    " where proOem.id=#{id}"

    )
    ProductOem getProductOemDetail(@Param("id")long id);

    @Update(
            "update rdmp_oem_product_info " +
                    " set p_id=#{proOem.pId},oem_no=#{proOem.oemNo},brand_price=#{proOem.brandPrice} " +
                    " where id=#{proOem.id}"
    )
    int updateProductOem(@Param("proOem")ProductOem proOem);

    @Select(
            "select * from rdmp_oem_product_info where p_id=#{proOem.pId} and oem_no=#{proOem.oemNo} "
    )
    List<ProductOem> checkProductOemSelect(@Param("proOem")ProductOem proOem);

    @Select(
            "select * from rdmp_oem_product_info " +
                    "where p_id=#{proOem.pId} and oem_no=#{proOem.oemNo} and id!=#{proOem.id} "
    )
    List<ProductOem> checkProductOemSelectId(@Param("proOem")ProductOem proOem);

    @Select(
            "select * from rdmp_oem_product_info where p_id=#{pId} and shelve='1' "
    )
    List<ProductOem> checkProductOemShelve(@Param("pId")long pId);

    @Delete(
            " delete from rdmp_oem_product_info where p_id=#{pId} and shelve='2' "
    )
    int deleteProductOemShelve(@Param("pId")long pId);

    @Select(
            "select * from rdmp_oem_product_info where oem_no=#{oemNo}"
    )
    List<ProductOem> getProductOemListDefault(@Param("oemNo")String oemNo);

    @Select(
            " select  * from rdmp_oem_product_info where oem_no=#{oemNo} and p_id=#{pId}"
    )
    ProductOem getProductOemOne(@Param("oemNo")String oemNo,@Param("pId")long pId);

    @Update(
            "update rdmp_oem_agent set agent_account='1' " +
                    " where oem_no=#{oemNo} and agent_no=#{agentNo}"
    )
    int updateAgentOem(@Param("oemNo")String oemNo,@Param("agentNo") String agentNo);


    @Select(
            "select * from rdmp_oem_agent where agent_no=#{agentNo} and agent_level=#{agentLevel}"
    )
    AgentOem checkAgentOem(@Param("agentNo") String agentNo, @Param("agentLevel") String agentLevel);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeOem oem = (ExchangeOem) param.get("oem");
            return new SQL(){{
                SELECT("oem.*");
                FROM("rdmp_oem_service oem");
                if(StringUtils.isNotBlank(oem.getOemName())){
                    WHERE("oem.oem_name like concat(#{oem.oemName},'%') ");
                }
                ORDER_BY("oem.create_time DESC");
            }}.toString();
        }

        public String selectProductOemList(final Map<String, Object> param) {
            final ProductOem proOem = (ProductOem) param.get("proOem");
            return new SQL(){{
                SELECT("proOem.*,oem.oem_name,pro.product_name,type.type_code,type.type_name,org.org_code,org.org_name");
                FROM("rdmp_oem_product_info proOem");
                LEFT_OUTER_JOIN("rdmp_oem_service oem ON oem.oem_no=proOem.oem_no");
                LEFT_OUTER_JOIN(" rdmp_product_info pro ON pro.id=proOem.p_id");
                LEFT_OUTER_JOIN("rdmp_product_type type ON type.type_code=pro.type_code");
                LEFT_OUTER_JOIN("rdmp_org_info org ON org.org_code=type.org_code");
                if(StringUtils.isNotBlank(proOem.getOemNo())){
                   WHERE("oem.oem_no=#{proOem.oemNo}");
                }
                if(StringUtils.isNotBlank(proOem.getOrgCode())){
                    WHERE("org.org_code=#{proOem.orgCode}");
                }
                if(StringUtils.isNotBlank(proOem.getTypeCode())){
                    WHERE("type.type_code=#{proOem.typeCode}");
                }
                if(proOem.getpId()!=null){
                    WHERE("proOem.p_id=#{proOem.pId}");
                }
                if(StringUtils.isNotBlank(proOem.getShelve())){
                    WHERE("proOem.shelve=#{proOem.shelve}");
                }
            }}.toString();
        }
    }
}
