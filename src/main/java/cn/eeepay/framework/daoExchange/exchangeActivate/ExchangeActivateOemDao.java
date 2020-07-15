package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.AgentOemActivate;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOem;
import cn.eeepay.framework.model.exchangeActivate.ProductActivateOem;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/13/013.
 * @author  liuks
 * oem dao
 */
public interface ExchangeActivateOemDao {

    @SelectProvider(type=ExchangeActivateOemDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateOem.class)
    List<ExchangeActivateOem> selectAllList(@Param("oem") ExchangeActivateOem oem, @Param("page") Page<ExchangeActivateOem> page);


    @SelectProvider(type=ExchangeActivateOemDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateOem.class)
    List<ExchangeActivateOem> importDetailSelect(@Param("oem") ExchangeActivateOem oem);

    @Insert(
            "INSERT INTO yfb_oem_service (oem_no,oem_name,create_time,company_no,company_name,service_phone, " +
                    " android_app_key,android_master_secret,ios_app_key,ios_master_secret, " +
                    " public_account,public_account_name,appid,secret,encoding_aes_key, " +
                    " wx_token,wx_ticket,remark,mailbox,agreement,client_appid,client_secret )" +
                    " VALUES(#{oem.oemNo},#{oem.oemName},NOW(),#{oem.companyNo},#{oem.companyName},#{oem.servicePhone}, " +
                    " #{oem.androidAppKey},#{oem.androidMasterSecret},#{oem.iosAppKey},#{oem.iosMasterSecret}, " +
                    " #{oem.publicAccount},#{oem.publicAccountName},#{oem.appid},#{oem.secret},#{oem.encodingAesKey}," +
                    " #{oem.wxToken},#{oem.wxTicket},#{oem.remark},#{oem.mailbox},#{oem.agreement},#{oem.clientAppid}," +
                    " #{oem.clientSecret})"
    )
    int addExchangeOem(@Param("oem") ExchangeActivateOem oem);

    @Select(
            "select oem.* from yfb_oem_service oem where oem.id=#{id}"
    )
    ExchangeActivateOem getExchangeOem(@Param("id") long id);

    @Update(
            "update yfb_oem_service set oem_no=#{oem.oemNo},oem_name=#{oem.oemName},company_no=#{oem.companyNo}, " +
                    " company_name=#{oem.companyName},service_phone=#{oem.servicePhone},android_app_key=#{oem.androidAppKey}, " +
                    " android_master_secret=#{oem.androidMasterSecret},ios_app_key=#{oem.iosAppKey}," +
                    " ios_master_secret=#{oem.iosMasterSecret},public_account=#{oem.publicAccount}," +
                    " public_account_name=#{oem.publicAccountName},appid=#{oem.appid},secret=#{oem.secret}, " +
                    " encoding_aes_key=#{oem.encodingAesKey},wx_token=#{oem.wxToken},wx_ticket=#{oem.wxTicket}, " +
                    " remark=#{oem.remark},mailbox=#{oem.mailbox},agreement=#{oem.agreement},client_appid=#{oem.clientAppid}," +
                    " client_secret=#{oem.clientSecret} " +
                    " where id=#{oem.id}"
    )
    int updateExchangeOem(@Param("oem") ExchangeActivateOem oem);

    @Select(
            "select * from yfb_oem_agent where oem_no=#{oemNo} and agent_level=#{agentLevel}"
    )
    AgentOemActivate getAgentOem(@Param("oemNo") String oemNo, @Param("agentLevel") String agentLevel);

    @Select(
            "select * from yfb_oem_agent where agent_no=#{agentNo} and agent_level=#{agentLevel}"
    )
    AgentOemActivate checkAgentOem(@Param("agentNo") String agentNo, @Param("agentLevel") String agentLevel);

    @Insert(
            "INSERT INTO yfb_oem_agent (oem_no,agent_no,agent_level,agent_node,team_id,create_time) " +
                    "VALUES(#{agent.oemNo},#{agent.agentNo},'1',#{agent.agentNode},#{agent.teamId},NOW())"
    )
    void addAgentOem(@Param("agent") AgentOemActivate agentOem);

    @Select(
            "select oem.* from yfb_oem_service oem "
    )
    List<ExchangeActivateOem> getOemList();

    @SelectProvider(type=ExchangeActivateOemDao.SqlProvider.class,method="selectProductOemList")
    @ResultType(ProductActivateOem.class)
    List<ProductActivateOem> selectProductOemList(@Param("proOem") ProductActivateOem proOem, @Param("page") Page<ProductActivateOem> page);

    @Update(
            "update yfb_oem_product_info set shelve=#{state} where id=#{id}"
    )
    int updateProductOemShelve(@Param("id") long id, @Param("state") String state);

    @Select(
            "select * from yfb_oem_product_info where id=#{id}"
    )
    ProductActivateOem getProductOem(@Param("id") Long id);

    @Insert(
            "INSERT INTO yfb_oem_product_info(p_id,oem_no,shelve,brand_price,create_time) " +
                    " VALUES(#{proOem.pId},#{proOem.oemNo},#{proOem.shelve},#{proOem.brandPrice},NOW())"
    )
    int addProductOem(@Param("proOem") ProductActivateOem proOem);

    @Select(
            "select proOem.*,oem.oem_name,pro.product_name,type.type_code,type.type_name,org.org_code,org.org_name " +
                    " from yfb_oem_product_info proOem " +
                    " LEFT JOIN yfb_oem_service oem ON oem.oem_no=proOem.oem_no "+
                    " LEFT JOIN yfb_product_info pro ON pro.id=proOem.p_id " +
                    " LEFT JOIN yfb_product_type type ON type.type_code=pro.type_code " +
                    " LEFT JOIN yfb_org_info org ON org.org_code=type.org_code " +
                    " where proOem.id=#{id}"

    )
    ProductActivateOem getProductOemDetail(@Param("id") long id);

    @Update(
            "update yfb_oem_product_info " +
                    " set p_id=#{proOem.pId},oem_no=#{proOem.oemNo},brand_price=#{proOem.brandPrice} " +
                    " where id=#{proOem.id}"
    )
    int updateProductOem(@Param("proOem") ProductActivateOem proOem);

    @Select(
            "select * from yfb_oem_product_info where p_id=#{proOem.pId} and oem_no=#{proOem.oemNo} "
    )
    List<ProductActivateOem> checkProductOemSelect(@Param("proOem") ProductActivateOem proOem);

    @Select(
            "select * from yfb_oem_product_info " +
                    "where p_id=#{proOem.pId} and oem_no=#{proOem.oemNo} and id!=#{proOem.id} "
    )
    List<ProductActivateOem> checkProductOemSelectId(@Param("proOem") ProductActivateOem proOem);

    @Select(
            "select * from yfb_oem_product_info where p_id=#{pId} and shelve='1' "
    )
    List<ProductActivateOem> checkProductOemShelve(@Param("pId") long pId);

    @Delete(
            " delete from yfb_oem_product_info where p_id=#{pId} and shelve='2' "
    )
    int deleteProductOemShelve(@Param("pId") long pId);

    @Select(
            "select * from yfb_oem_product_info where oem_no=#{oemNo}"
    )
    List<ProductActivateOem> getProductOemListDefault(@Param("oemNo") String oemNo);

    @Select(
            " select  * from yfb_oem_product_info where oem_no=#{oemNo} and p_id=#{pId}"
    )
    ProductActivateOem getProductOemOne(@Param("oemNo") String oemNo, @Param("pId") long pId);

    @Update(
            "update yfb_oem_agent set agent_account='1' " +
                    " where oem_no=#{oemNo} and agent_no=#{agentNo}"
    )
    int updateAgentOem(@Param("oemNo") String oemNo, @Param("agentNo") String agentNo);


    @Select(
            "select * from yfb_agent_share_config where agent_no=#{agentNo}"
    )
    List<Map<Object,Object>> checkAgent(@Param("agentNo")String agentNo);

    @Insert(
            "INSERT INTO yfb_agent_share_config" +
                    " (agent_no,share_rate,oem_fee,receive_share_rate,repayment_share_rate,share_type, " +
                    " create_time) " +
                    " VALUES " +
                    "(#{agentNo},#{share},#{fee},#{receiveShare},#{repaymentShare},'D',NOW())"
    )
    int insertOneAgentShare(@Param("agentNo")String agentNo,
                            @Param("share") BigDecimal share,
                            @Param("fee") BigDecimal fee,
                            @Param("receiveShare") BigDecimal receiveShare,
                            @Param("repaymentShare") BigDecimal repaymentShare
    );

    @Update(
            "update yfb_agent_share_config " +
                    "set share_rate=#{share},oem_fee=#{fee},receive_share_rate=#{receiveShare}, " +
                    "   repayment_share_rate=#{repaymentShare} " +
                    " where agent_no=#{agentNo} "
    )
    int updateOneAgentShare(@Param("agentNo")String agentNo,
                            @Param("share") BigDecimal share,
                            @Param("fee") BigDecimal fee,
                            @Param("receiveShare") BigDecimal receiveShare,
                            @Param("repaymentShare") BigDecimal repaymentShare
    );


    @Update(
            "update yfb_oem_service " +
                    " set repayment_oem_no=#{oem.repaymentOemNo},receive_oem_no=#{oem.receiveOemNo} " +
                    " where id=#{oem.id}"
    )
    int updateRepayOem(@Param("oem") ExchangeActivateOem oem);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateOem oem = (ExchangeActivateOem) param.get("oem");
            return new SQL(){{
                SELECT("oem.*");
                FROM("yfb_oem_service oem");
                if(StringUtils.isNotBlank(oem.getOemName())){
                    WHERE("oem.oem_name like concat(#{oem.oemName},'%') ");
                }
                if(StringUtils.isNotBlank(oem.getOpenOemState())){
                    if("1".equals(oem.getOpenOemState())){
                        WHERE(" oem.repayment_oem_no is not null ");
                    }else if("2".equals(oem.getOpenOemState())){
                        WHERE(" oem.repayment_oem_no is null ");
                    }
                }
                ORDER_BY("oem.create_time DESC");
            }}.toString();
        }

        public String selectProductOemList(final Map<String, Object> param) {
            final ProductActivateOem proOem = (ProductActivateOem) param.get("proOem");
            return new SQL(){{
                SELECT("proOem.*,oem.oem_name,pro.product_name,type.type_code,type.type_name,org.org_code,org.org_name");
                FROM("yfb_oem_product_info proOem");
                LEFT_OUTER_JOIN("yfb_oem_service oem ON oem.oem_no=proOem.oem_no");
                LEFT_OUTER_JOIN(" yfb_product_info pro ON pro.id=proOem.p_id");
                LEFT_OUTER_JOIN("yfb_product_type type ON type.type_code=pro.type_code");
                LEFT_OUTER_JOIN("yfb_org_info org ON org.org_code=type.org_code");
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
