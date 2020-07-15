package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface OrgInfoDao {
    @Options(useGeneratedKeys=true, keyProperty="org_id")
    @Insert("insert into org_info(org_id,org_name,org_logo,weixin_sign,super_orgcode,v2_agent_number" +
            ",v2_orgcode,agent_price,agent_cost,agent_push_cost,receive_wx_cost,receive_zfb_cost," +
            "receive_kj_cost,receive_push_cost" +
            ",repayment_cost,repayment_cost_wmjh,repayment_push_cost,repayment_push_cost_wmjh,up_manager_num,up_manager_cardnum,up_banker_num" +
            ",up_banker_cardnum,remark,update_by,update_date,create_date,public_account,public_account_name" +
            ",appid,secret,encoding_aes_key,open_repay_price,open_repay_cost,open_repay_push_cost,service_phone" +
            ",authorized_unit,authorized_unit_seal,is_support_pay,public_qr_code" +
            ",red_money_status,red_money_profit_status,business_email,ui_status" +
            ",member_center_logo,start_page,app_logo,share_message_logo" +
            ",share_template_image1,share_template_image2,share_template_image3,day_start,day_end" +
            ",month_card_num,year_card_num,red_money_min,red_money_max,withdraw_money_min,company_name,company_no" +
            ",trade_fee_rate,trade_fee_rate_wmjh,trade_single_fee,trade_single_fee_wmjh,withdraw_fee_rate,withdraw_single_fee,push_app_key,push_master_secret" +
            ",index_style,home_background,is_open,batch_order_notify_url,open_app_url,open_app_name,open_app_intro" +
            ",open_merchant_key,key_version,get_userinfo_url,ali_p_id,ali_app_id,ali_private_key,ali_public_key,public_key,private_key,cjd_app_key,cjd_oem_on,point_exchange_ratio" +
            ",is_user_sendprofit,user_alias,member_alias,manager_alias,banker_alias,is_user_share,is_profit_banker_banker" +
            ",up_member_needperfect,up_member_needpay,up_member_needlock,up_member_locknum" +
            ",up_member_mposnum,up_manager_locknum,up_manager_mposnum,up_banker_locknum,up_banker_mposnum,app_qr_code)" +
            " values(#{orgId},#{orgName},#{orgLogo},#{weixinSign},#{superOrgcode},#{v2AgentNumber}" +
            ",#{v2Orgcode},#{agentPrice},#{agentCost},#{agentPushCost},#{receiveWxCost},#{receiveZfbCost}" +
            ",#{receiveKjCost},#{receivePushCost}" +
            ",#{repaymentCost},#{repaymentCostWmjh},#{repaymentPushCost},#{repaymentPushCostWmjh},#{upManagerNum},#{upManagerCardnum},#{upBankerNum}" +
            ",#{upBankerCardnum},#{remark},#{updateBy},#{updateDate},#{createDate},#{publicAccount},#{publicAccountName}" +
            ",#{appid},#{secret},#{encodingAesKey},#{openRepayPrice},#{openRepayCost},#{openRepayPushCost}" +
            ",#{servicePhone},#{authorizedUnit},#{authorizedUnitSeal},#{isSupportPay},#{publicQrCode}"+
            ",#{redMoneyStatus},#{redMoneyProfitStatus},#{businessEmail},#{uiStatus}"+
            ",#{memberCenterLogo},#{startPage},#{appLogo},#{shareMessageLogo}"+
            ",#{shareTemplateImage1},#{shareTemplateImage2},#{shareTemplateImage3},#{dayStart},#{dayEnd}"+
            ",#{monthCardNum},#{yearCardNum},#{redMoneyMin},#{redMoneyMax},#{withdrawMoneyMin},#{companyName},#{companyNo}" +
            ",#{tradeFeeRate},#{tradeFeeRateWmjh},#{tradeSingleFee},#{tradeSingleFeeWmjh},#{withdrawFeeRate},#{withdrawSingleFee},#{pushAppKey},#{pushMasterSecret}" +
            ",#{indexStyle},#{homeBackground},#{isOpen},#{batchOrderNotifyUrl},#{openAppUrl},#{openAppName},#{openAppIntro}" +
            ",#{openMerchantKey},#{keyVersion},#{getUserinfoUrl},#{aliPId},#{aliAppId},#{aliPrivateKey},#{aliPublicKey},#{publicKey},#{privateKey},#{cjdAppKey},#{cjdOemOn},#{pointExchangeRatio}"+
            ",#{isUserSendprofit},#{userAlias},#{memberAlias},#{managerAlias},#{bankerAlias},#{isUserShare},#{isProfitBankerBanker}" +
            ",#{upMemberNeedperfect},#{upMemberNeedpay},#{upMemberNeedlock},#{upMemberLocknum}" +
            ",#{upMemberMposnum},#{upManagerLocknum},#{upManagerMposnum},#{upBankerLocknum},#{upBankerMposnum},#{appQrCode})")
    int insert(OrgInfo orgInfo);

    @Update("update org_info set org_name=#{orgName},weixin_sign=#{weixinSign},agent_price=#{agentPrice}," +
            "agent_cost=#{agentCost},agent_push_cost=#{agentPushCost},receive_wx_cost=#{receiveWxCost},receive_zfb_cost=#{receiveZfbCost}" +
            ",receive_kj_cost=#{receiveKjCost},trade_fee_rate=#{tradeFeeRate},trade_fee_rate_wmjh=#{tradeFeeRateWmjh},trade_single_fee=#{tradeSingleFee},trade_single_fee_wmjh=#{tradeSingleFeeWmjh}," +
            "receive_push_cost=#{receivePushCost},repayment_cost=#{repaymentCost},repayment_cost_wmjh=#{repaymentCostWmjh}," +
            "repayment_push_cost=#{repaymentPushCost},repayment_push_cost_wmjh=#{repaymentPushCostWmjh},up_manager_num=#{upManagerNum}," +
            "up_manager_cardnum=#{upManagerCardnum},up_banker_num=#{upBankerNum}," +
            "up_banker_cardnum=#{upBankerCardnum},remark=#{remark},update_by=#{updateBy}," +
            "update_date=#{updateDate},org_logo=#{orgLogo},public_account=#{publicAccount},public_account_name=#{publicAccountName}" +
            ",appid=#{appid},secret=#{secret},encoding_aes_key=#{encodingAesKey}" +
            ",open_repay_price=#{openRepayPrice},open_repay_cost=#{openRepayCost},open_repay_push_cost=#{openRepayPushCost}" +
            ",service_phone=#{servicePhone},authorized_unit=#{authorizedUnit},authorized_unit_seal=#{authorizedUnitSeal}" +
            ",is_support_pay=#{isSupportPay},public_qr_code=#{publicQrCode}" +
            ",red_money_status=#{redMoneyStatus},red_money_profit_status=#{redMoneyProfitStatus}" +
            ",business_email=#{businessEmail},ui_status=#{uiStatus}"+
            ",member_center_logo=#{memberCenterLogo},start_page=#{startPage},app_logo=#{appLogo},share_message_logo=#{shareMessageLogo}"+
            ",share_template_image1=#{shareTemplateImage1},share_template_image2=#{shareTemplateImage2},share_template_image3=#{shareTemplateImage3}" +
            ",day_start=#{dayStart},day_end=#{dayEnd},month_card_num=#{monthCardNum},year_card_num=#{yearCardNum}" +
            ",red_money_min=#{redMoneyMin},red_money_max=#{redMoneyMax},withdraw_money_min=#{withdrawMoneyMin},index_style=#{indexStyle}" +
            ",push_app_key=#{pushAppKey},push_master_secret=#{pushMasterSecret},is_open=#{isOpen},batch_order_notify_url=#{batchOrderNotifyUrl}"+ 
            ",open_app_url=#{openAppUrl},open_app_name=#{openAppName},open_app_intro=#{openAppIntro},open_merchant_key=#{openMerchantKey},key_version=#{keyVersion}"+
            ",home_background=#{homeBackground},get_userinfo_url=#{getUserinfoUrl},ali_p_id=#{aliPId},ali_app_id=#{aliAppId}" +
            ",ali_private_key=#{aliPrivateKey},ali_public_key=#{aliPublicKey},point_exchange_ratio=#{pointExchangeRatio}" +
            ",is_user_sendprofit=#{isUserSendprofit},user_alias=#{userAlias},member_alias=#{memberAlias},manager_alias=#{managerAlias},banker_alias=#{bankerAlias},is_user_share=#{isUserShare},is_profit_banker_banker=#{isProfitBankerBanker}" +
            ",up_member_needperfect=#{upMemberNeedperfect},up_member_needpay=#{upMemberNeedpay},up_member_needlock=#{upMemberNeedlock},up_member_locknum=#{upMemberLocknum}" +
            ",up_member_mposnum=#{upMemberMposnum},up_manager_locknum=#{upManagerLocknum},up_manager_mposnum=#{upManagerMposnum},up_banker_locknum=#{upBankerLocknum},up_banker_mposnum=#{upBankerMposnum},app_qr_code=#{appQrCode}"+
            " where org_id = #{orgId}")
    int update(OrgInfo orgInfo);

    @Update("update org_info set public_key=#{publicKey},private_key=#{privateKey},cjd_app_key=#{cjdAppKey},cjd_oem_on=#{cjdOemOn},update_by=#{updateBy}," +
            "update_date=#{updateDate},point_exchange_ratio=#{pointExchangeRatio},platform_cost=#{platformCost}")
    int updateExchange(OrgInfo orgInfo);

    @SelectProvider(type = SqlProvider.class, method = "selectOrgInfoPage")
    @ResultType(OrgInfo.class)
    List<OrgInfo> selectOrgInfoPage(@Param("baseInfo") OrgInfo baseInfo2, @Param("page")Page<OrgInfo> page);

    @Select("select org_id, org_name,point_exchange_ratio,platform_cost from org_info order by org_id")
    @ResultType(OrgInfo.class)
    List<OrgInfo> getOrgInfoList();

    @Select("select open_merchant_key from org_info where org_id = #{orgId}")
    String checkOpenMerchantKey(@Param("orgId")Long orgId);
    
    @Select("select count(1) from org_info where org_id = #{orgId}")
    Long checkExistsOrgId(@Param("orgId")Long orgId);

    @Select("select * from org_info where org_id = #{orgId}")
    @ResultType(OrgInfo.class)
    OrgInfo selectDetail(@Param("orgId") Long orgId);

    @Select("select org_id, org_name,is_open from org_info where org_id = #{orgId}")
    @ResultType(OrgInfo.class)
    OrgInfo selectOrg(Long orgId);

    @Select("select agent_price,agent_cost,agent_push_cost,open_repay_price,open_repay_cost,open_repay_push_cost" +
            ",receive_wx_cost,receive_zfb_cost,receive_kj_cost,receive_push_cost,repayment_cost,repayment_cost_wmjh," +
            "repayment_push_cost,repayment_push_cost_wmjh,trade_fee_rate,trade_fee_rate_wmjh," +
            "trade_single_fee,trade_single_fee_wmjh,withdraw_fee_rate,withdraw_single_fee" +
            ",up_manager_num,up_manager_cardnum,up_banker_num,up_banker_cardnum" +
            ",day_start,day_end,month_card_num,year_card_num,red_money_min,red_money_max,withdraw_money_min,index_style,"+
            "home_background" +
            " from org_info where v2_orgcode = #{v2Orgcode}")
    @ResultType(OrgInfo.class)
    OrgInfo selectBaseDetail(String v2Orgcode);

    @Update("update org_info set account_status = '1' where v2_agent_number = #{agentNo}")
    int updateAccount(@Param("agentNo")String agentNo);

    @Select("select * from org_info where public_key is not null")
    List<OrgInfo> selectCjdOrgInfo();

    public class SqlProvider{

        public String selectOrgInfoPage(Map<String, Object> param){
            String sql = "";
            final OrgInfo baseInfo = (OrgInfo) param.get("baseInfo");
            sql = new SQL(){{
                SELECT("oi.org_id, oi.org_name,oi.org_logo, oi.red_money_status,oi.red_money_profit_status, oi.weixin_sign, oi.super_orgcode");
                SELECT("oi.v2_agent_number, oi.v2_orgcode, oi.agent_price, oi.agent_cost");
                SELECT("oi.agent_push_cost, oi.receive_wx_cost,oi.receive_zfb_cost,oi.receive_kj_cost, oi.receive_push_cost");
                SELECT("oi.repayment_cost, oi.repayment_push_cost, oi.up_manager_num");
                SELECT("oi.up_manager_cardnum, oi.up_banker_num, oi.up_banker_cardnum");
                SELECT("oi.open_repay_price, oi.open_repay_cost, oi.open_repay_push_cost, oi.public_account");
                SELECT("oi.remark,oi.account_status,oi.is_open");
                FROM("org_info oi");
                if(baseInfo != null){
                    if(baseInfo.getOrgId() != -1){
                        WHERE("oi.org_id = #{baseInfo.orgId}");
                    }
                    if(StringUtils.isNotBlank(baseInfo.getAccountStatus())){
                        WHERE("oi.account_status = #{baseInfo.accountStatus}");
                    }
                }
                ORDER_BY("oi.create_date desc");
            }}.toString();
            return sql;
        }
    }
}
