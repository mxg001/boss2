package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 超级兑用户Dao
 */
public interface UserManagementDao {

    @SelectProvider(type=UserManagementDao.SqlProvider.class,method="selectAllList")
    @ResultType(UserManagement.class)
    List<UserManagement> selectAllList(@Param("user")UserManagement userManagement, @Param("page")Page<UserManagement> page);

    @SelectProvider(type=UserManagementDao.SqlProvider.class,method="selectSum")
    @ResultType(MerInfoTotal.class)
    MerInfoTotal selectSum(@Param("user")UserManagement userManagement, @Param("page")Page<UserManagement> page);


    @SelectProvider(type=UserManagementDao.SqlProvider.class,method="selectAllList")
    @ResultType(UserManagement.class)
    List<UserManagement> importDetailSelect(@Param("user")UserManagement user);

    @Select(
            " select mer.*,oem.oem_no,oem.oem_name,wechat.nickname,wechat.weixinhao,balance.total_balance," +
                    " card.account_name,card.business_code,card.mobile_no,card.account_no,card.yhkzm_url " +
                    " from rdmp_merchant_info mer " +
                    " LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " LEFT JOIN rdmp_wechat_info wechat ON wechat.unionid=mer.unionid " +
                    " LEFT JOIN rdmp_balance balance ON balance.mer_no=mer.merchant_no " +
                    " LEFT JOIN rdmp_card_manage card ON card.mer_no=mer.merchant_no " +
                    " where mer.merchant_no=#{merchantNo}"
    )
    UserManagement getUserManagementById(@Param("merchantNo")String merchantNo);

    @Select(
            "select * from rdmp_merchant_act_info where merchant_no=#{merchantNo} "
    )
    List<UserManagementMember> getUserManagementMember(@Param("merchantNo")String merchantNo);

    @Select(
            " select sum(IF(mer_act_status='1',1,0)) subordinate,sum(IF(mer_act_status='2',1,0)) subordinateMoney from rdmp_merchant_info where par_mer_no=#{merchantNo} "
    )
    Subordinate getSubordinate(@Param("merchantNo")String merchantNo);

    @Update(
            "update rdmp_merchant_info set mobile_username=#{mer.mobileUsername}" +
                    " where merchant_no=#{mer.merchantNo} "
    )
    int updateUserManagement(@Param("mer")UserManagement userManagement);

    @Update(
            " update rdmp_card_manage set account_no=#{card.cardNo},account_province=#{card.bankProvince}, " +
                    " account_city=#{card.bankCity},bank_name=#{card.bankName},mobile_no=#{card.accountPhone}," +
                    " zh_name=#{card.cnapsNo} where mer_no=#{card.merchantNo}"
    )
    int updateSettlementCard(@Param("card")SettlementCard settlementCard);

    @Update(
        "INSERT INTO rdmp_card_his(mer_no,old_card_no,card_no,type,operator,create_time) " +
                " VALUES " +
                "  (#{card.merchantNo},#{card.oldCard},#{card.cardNo},'BOSS',#{card.operator},NOW())"
    )
    int updateSettlementCardHis(@Param("card")SettlementCard settlementCard);

    @SelectProvider(type=UserManagementDao.SqlProvider.class,method="getUserManagementList")
    @ResultType(UserManagement.class)
    List<UserManagement> getUserManagementList(@Param("merchantNo")String merchantNo);


    @Select(
            "select * from rdmp_merchant_info where merchant_no=#{merchantNo} "
    )
    UserManagement getUserManagementOne(@Param("merchantNo")String merchantNo);


    @Update(
            "update rdmp_merchant_info set" +
                    " freeze_amount=#{freeze.freezeAmount},freeze_remark=#{freeze.remark}" +
                    " where merchant_no=#{freeze.merchantNo}"
    )
    int userFreeze(@Param("freeze")MerchantFreeze freeze);

    @Update(
            "update rdmp_balance set" +
                    " freeze_amount=#{freeze.freezeAmount} " +
                    " where mer_no=#{freeze.merchantNo}"
    )
    int userFreezeBalance(@Param("freeze")MerchantFreeze freeze);

    @Insert(
            "INSERT INTO rdmp_merchant_info_his " +
                    "(merchant_no,freeze_oper,create_time,freeze_amount,remark,freeze_status) " +
                    " VALUES " +
                    "(#{freeze.merchantNo},#{freeze.freezeOper},NOW(),#{freeze.freezeAmount}," +
                    " #{freeze.remark},#{freeze.freezeStatus})"
    )
    int userFreezeHis(@Param("freeze")MerchantFreeze freeze);

    @Select(
            "select * from rdmp_merchant_info_his " +
                    " where merchant_no=#{merchantNo} ORDER BY create_time desc limit 0,10 "
    )
    List<MerchantFreeze> getUserFreezeHis(@Param("merchantNo")String merchantNo);

    class SqlProvider {
        public String selectAllList(final Map<String, Object> param) {
            final UserManagement user = (UserManagement) param.get("user");
            String str=getSelectSql(user,1);
            return str;
        }
        public String selectSum(final Map<String, Object> param) {
            final UserManagement user = (UserManagement) param.get("user");
            String str=getSelectSql(user,2);
            return str;
        }
        public String getSelectSql(UserManagement user,int state){
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            if(state==1){
                sb.append("      mer.*,oem.oem_name,wechat.nickname,balance.total_balance, ");
                sb.append("      act.create_time paymentTime, ");
                sb.append("      balance.freeze_amount freeze_amount_balance,card.account_name ");
            }else if(state==2){
                sb.append(" count(*) merTotal,");
                sb.append(" sum(IF(mer.mer_capa='1',1,0)) ordmemTotal, ");
                sb.append(" sum(IF(mer.mer_capa='2',1,0)) supermemTotal, ");
                sb.append(" sum(IF(mer.mer_capa='3',1,0)) ordparTotal, ");
                sb.append(" sum(IF(mer.mer_capa='4',1,0)) goldparTotal, ");
                sb.append(" sum(IF(mer.mer_capa='5',1,0)) diamparTotal, ");
                sb.append(" sum(IF(mer.mer_act_status='2',1,0)) merActTotal ");
            }
            sb.append(" from  rdmp_merchant_info mer ");
            sb.append(" LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer.oem_no ");
            sb.append(" LEFT JOIN rdmp_wechat_info wechat ON wechat.unionid=mer.unionid ");
            sb.append(" LEFT JOIN rdmp_balance balance ON balance.mer_no=mer.merchant_no ");
            sb.append(" LEFT JOIN rdmp_card_manage card ON card.mer_no=mer.merchant_no ");
            sb.append(" LEFT JOIN (");
            sb.append("           select act1.* from rdmp_merchant_act_info act1 inner join ");
            sb.append("             (select merchant_no, max(mer_capa) as max_score from  rdmp_merchant_act_info where mer_capa>=3 group by merchant_no) act2");
            sb.append("              ON (act1.merchant_no=act2.merchant_no and act1.mer_capa=act2.max_score) ");
            sb.append("           ) act ON act.merchant_no=mer.merchant_no ");
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
            if(StringUtils.isNotBlank(user.getAccountName())){
                sb.append(" and card.account_name like concat(#{user.accountName},'%') ");
            }
            if(StringUtils.isNotBlank(user.getMerCapa())){
                sb.append(" and mer.mer_capa=#{user.merCapa} ");
            }
            if(StringUtils.isNotBlank(user.getParMerNo())){
                sb.append(" and mer.par_mer_no=#{user.parMerNo} ");
            }
            if(user.getCreateTimeBegin()!= null){
                sb.append(" and mer.create_time>=#{user.createTimeBegin} ");
            }
            if(user.getCreateTimeEnd()!= null){
                sb.append(" and mer.create_time<=#{user.createTimeEnd} ");
            }
            if(user.getPaymentTimeBegin()!= null){
                sb.append(" and act.create_time>=#{user.paymentTimeBegin} ");
            }
            if(user.getPaymentTimeEnd()!= null){
                sb.append(" and act.create_time<=#{user.paymentTimeEnd} ");
            }
            if(StringUtils.isNotBlank(user.getFreezeAmountState())){
                sb.append(" and mer.freeze_amount>0 ");
            }
            sb.append(" ORDER BY mer.create_time DESC");
            return sb.toString();
        }


        public String getUserManagementList(final Map<String, Object> param) {
            final String merchantNo = (String) param.get("merchantNo");
            StringBuffer sb=new StringBuffer();
            sb.append(" select mer.id,mer.merchant_no,mer.user_name ");
            sb.append("   from rdmp_merchant_info mer ");
            if(StringUtils.isNotBlank(merchantNo)&&!"-1".equals(merchantNo)){
                sb.append(" where mer.merchant_no like concat(#{merchantNo},'%') ");
            }
            sb.append("limit 100");
            return sb.toString();
        }
    }
}
