package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;

import cn.eeepay.framework.model.exchange.MerchantFreeze;
import cn.eeepay.framework.model.exchange.SettlementCard;
import cn.eeepay.framework.model.exchange.UserManagementMember;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUser;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUserCard;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 超级兑用户Dao
 */
public interface ExchangeActivateUserDao {

    @SelectProvider(type=ExchangeActivateUserDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateUser.class)
    List<ExchangeActivateUser> selectAllList(@Param("user") ExchangeActivateUser userManagement, @Param("page") Page<ExchangeActivateUser> page);

    @SelectProvider(type=ExchangeActivateUserDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateUser.class)
    List<ExchangeActivateUser> importDetailSelect(@Param("user") ExchangeActivateUser user);

    @Select(
            " select mer.*,oem.oem_name,balance.total_balance, " +
                    " balance.total_balance,balance.freeze_amount freeze_amount_balance, "+
                    " receive.receive_merchant_no,receive.merchant_status,receive.success_time "+
                    " from yfb_merchant_info mer " +
                    " LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " LEFT JOIN yfb_balance balance ON balance.mer_no=mer.merchant_no " +
                    " LEFT JOIN act_receive_merchant_info receive ON receive.source_merchant_no=mer.merchant_no " +
                    " where mer.merchant_no=#{merchantNo}"
    )
    ExchangeActivateUser getUserManagementById(@Param("merchantNo") String merchantNo);

    @Update(
            "update yfb_merchant_info set mobile_username=#{mer.mobileUsername}" +
                    " where merchant_no=#{mer.merchantNo} "
    )
    int updateUserManagement(@Param("mer") ExchangeActivateUser userManagement);

    @Update(
            " update yfb_card_manage set account_no=#{card.cardNo},account_province=#{card.bankProvince}, " +
                    " account_city=#{card.bankCity},bank_name=#{card.bankName},mobile_no=#{card.accountPhone}," +
                    " zh_name=#{card.cnapsNo} where mer_no=#{card.merchantNo}"
    )
    int updateSettlementCard(@Param("card") SettlementCard settlementCard);

    @Update(
        "INSERT INTO yfb_card_his(mer_no,old_card_no,card_no,type,operator,create_time) " +
                " VALUES " +
                "  (#{card.merchantNo},#{card.oldCard},#{card.cardNo},'BOSS',#{card.operator},NOW())"
    )
    int updateSettlementCardHis(@Param("card") SettlementCard settlementCard);

    @SelectProvider(type=ExchangeActivateUserDao.SqlProvider.class,method="getUserManagementList")
    @ResultType(ExchangeActivateUser.class)
    List<ExchangeActivateUser> getUserManagementList(@Param("merchantNo") String merchantNo);

    @Update(
            "update yfb_merchant_info set" +
                    " freeze_amount=#{freeze.freezeAmount},freeze_remark=#{freeze.remark}" +
                    " where merchant_no=#{freeze.merchantNo}"
    )
    int userFreeze(@Param("freeze") MerchantFreeze freeze);

    @Update(
            "update yfb_balance set" +
                    " freeze_amount=#{freeze.freezeAmount} " +
                    " where mer_no=#{freeze.merchantNo}"
    )
    int userFreezeBalance(@Param("freeze") MerchantFreeze freeze);

    @Insert(
            "INSERT INTO yfb_merchant_info_his " +
                    "(merchant_no,freeze_oper,create_time,freeze_amount,remark,freeze_status) " +
                    " VALUES " +
                    "(#{freeze.merchantNo},#{freeze.freezeOper},NOW(),#{freeze.freezeAmount}," +
                    " #{freeze.remark},#{freeze.freezeStatus})"
    )
    int userFreezeHis(@Param("freeze") MerchantFreeze freeze);

    @Select(
            "select * from yfb_merchant_info_his " +
                    " where merchant_no=#{merchantNo} ORDER BY create_time desc limit 0,10 "
    )
    List<MerchantFreeze> getUserFreezeHis(@Param("merchantNo") String merchantNo);

    @Select(
            "select * from yfb_card_manage where mer_no=#{merchantNo} and is_settle=#{isSettle}"
    )
    List<ExchangeActivateUserCard> getUserCard(@Param("merchantNo")String merchantNo,@Param("isSettle")String isSettle);


    class SqlProvider {
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateUser user = (ExchangeActivateUser) param.get("user");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("      mer.*,oem.oem_name, ");
            sb.append("      balance.total_balance,balance.freeze_amount freeze_amount_balance, ");
            sb.append("      receive.receive_merchant_no,receive.merchant_status,receive.success_time ");
            sb.append(" from  yfb_merchant_info mer ");
            sb.append(" LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no ");
            sb.append(" LEFT JOIN yfb_balance balance ON balance.mer_no=mer.merchant_no ");
            sb.append(" LEFT JOIN act_receive_merchant_info receive ON receive.source_merchant_no=mer.merchant_no ");
            sb.append(" WHERE 1=1 ");
            if(StringUtils.isNotBlank(user.getMerchantNo())){
                sb.append(" and mer.merchant_no=#{user.merchantNo} ");
            }
            if(StringUtils.isNotBlank(user.getOemNo())){
                sb.append(" and oem.oem_no=#{user.oemNo} ");
            }
            if(StringUtils.isNotBlank(user.getStatus())){
                sb.append(" and mer.status=#{user.status} ");
            }
            if(StringUtils.isNotBlank(user.getMobileUsername())){
                sb.append(" and mer.mobile_username=#{user.mobileUsername} ");
            }
            if(StringUtils.isNotBlank(user.getRealName())){
                sb.append(" and mer.real_name like concat(#{user.realName},'%') ");
            }
            if(StringUtils.isNotBlank(user.getAgentNo())){
                sb.append(" and mer.agent_no=#{user.agentNo} ");
            }
            if(StringUtils.isNotBlank(user.getOneAgentNo())){
                sb.append(" and mer.one_agent_no=#{user.oneAgentNo} ");
            }
            if(user.getCreateTimeBegin()!= null){
                sb.append(" and mer.create_time>=#{user.createTimeBegin} ");
            }
            if(user.getCreateTimeEnd()!= null){
                sb.append(" and mer.create_time<=#{user.createTimeEnd} ");
            }
            if(StringUtils.isNotBlank(user.getFreezeAmountState())){
                sb.append(" and mer.freeze_amount>0 ");
            }
            if(StringUtils.isNotBlank(user.getReceiveMerchantNo())){
                sb.append(" and receive.receive_merchant_no=#{user.receiveMerchantNo} ");
            }
            if(StringUtils.isNotBlank(user.getRepayMerchantNo())){
                sb.append(" and mer.repay_merchant_no=#{user.repayMerchantNo} ");
            }
            if(StringUtils.isNotBlank(user.getMerchantStatus())){
                sb.append(" and receive.merchant_status=#{user.merchantStatus} ");
            }
            if(user.getSuccessTimeBegin()!= null){
                sb.append(" and receive.success_time>=#{user.successTimeBegin} ");
            }
            if(user.getSuccessTimeEnd()!= null){
                sb.append(" and receive.success_time<=#{user.successTimeEnd} ");
            }
            sb.append(" ORDER BY mer.create_time DESC");
            return sb.toString();
        }
        public String getUserManagementList(final Map<String, Object> param) {
            final String merchantNo = (String) param.get("merchantNo");
            StringBuffer sb=new StringBuffer();
            sb.append(" select mer.id,mer.merchant_no,mer.user_name ");
            sb.append("   from yfb_merchant_info mer ");
            if(StringUtils.isNotBlank(merchantNo)&&!"-1".equals(merchantNo)){
                sb.append(" where mer.merchant_no like concat(#{merchantNo},'%') ");
            }
            sb.append("limit 100");
            return sb.toString();
        }
    }
}
