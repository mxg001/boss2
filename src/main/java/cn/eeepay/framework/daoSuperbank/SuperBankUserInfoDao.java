package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.daoSuperbank.CreditcardSourceDao.SqlProvider;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BlackList;
import cn.eeepay.framework.model.BlackListLog;
import cn.eeepay.framework.model.BlackOperLog;
import cn.eeepay.framework.model.CreditcardSource;
import cn.eeepay.framework.model.RiskRoll;
import cn.eeepay.framework.model.SuperBankUserCard;
import cn.eeepay.framework.model.SuperBankUserCardRecord;
import cn.eeepay.framework.model.SuperBankUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SuperBankUserInfoDao {

    /**
     * 用户银行卡信息
     * @param userCode
     * @return
     */
    @Select("select id,user_code,account_name,account_phone,account_id_no," +
            "bank_province,bank_city,bank_district,bank_name,bank_branch_name," +
            " card_no,positive_photo from user_card" +
            " where user_code = #{userCode} and status = '1'" +
            " order by create_date desc limit 1")
    @ResultType(SuperBankUserInfo.class)
    SuperBankUserCard selectSuperBankUserCard(@Param("userCode")String userCode);

    /**
     * 用户管理详情
     * @param userCode
     * @return
     */
    @Select("select ui.* from user_info ui where ui.user_code = #{userCode}")
    @ResultType(SuperBankUserInfo.class)
    SuperBankUserInfo selectDetail(@Param("userCode")String userCode);

    @Select("select ui.user_id, ui.user_code, ui.user_name, ui.user_type, ui.nick_name"
    		+ " ,ui.receive_user_no, ui.account_status " +
            " from user_info ui where ui.user_code = #{userCode}")
    @ResultType(SuperBankUserInfo.class)
    SuperBankUserInfo selectUser(@Param("userCode")String userCode);

    /**
     * 用户的直接用户
     * @param userCode
     * @return
     */
    @Select("select count(1) from user_info where top_one_code = #{userCode} and user_type = '10'")
    @ResultType(Integer.class)
    Integer countSubUser(@Param("userCode")String userCode);

    /**
     * 用户的所有下级
     * @param userCode
     * @return
     */
    @Select("select count(1) from user_info where top_one_code = #{userCode}")
    @ResultType(Integer.class)
    Integer countSub(@Param("userCode")String userCode);

    /**
     * 用户管理
     * @param baseInfo
     * @param page
     * @return
     */
    @SelectProvider(type=SqlProvider.class,method="selectUserInfoPage")
    @ResultType(SuperBankUserInfo.class)
    List<SuperBankUserInfo> selectUserInfoPage(@Param("baseInfo")SuperBankUserInfo baseInfo, @Param("page")Page<SuperBankUserInfo> page);

    @Select("select user_id, user_code, user_name from user_info")
    @ResultType(SuperBankUserInfo.class)
    List<SuperBankUserInfo> getAllList();

    @SelectProvider(type = SqlProvider.class, method = "selectUserInfoList")
    @ResultType(SuperBankUserInfo.class)
    List<SuperBankUserInfo> selectUserInfoList(@Param("userCode") String userCode);

    @Update("update user_info set user_name=#{userName}, nick_name=#{nickName}," +
            " phone=#{phone},weixin_code=#{weixinCode},pay_back=#{payBack},status_mentor=#{statusMentor} where user_code=#{userCode}")
    int updateUserInfo(SuperBankUserInfo baseInfo);

    @Insert("insert into user_card_record(user_code,record_date,account_phone,card_no,bank_name," +
            "bank_branch_name,bank_adress,update_source,update_by)" +
            " values (#{userCode},#{recordDate},#{accountPhone},#{cardNo},#{bankName}," +
            "#{bankBranchName},#{bankAdress},#{updateSource},#{updateBy})")
    int insertUserCardRecord(SuperBankUserCardRecord record);

    @Update("update user_card set card_no=#{cardNo},bank_name=#{bankName}," +
            "cnaps_no=#{cnapsNo},bank_branch_name=#{bankBranchName}," +
            "bank_province=#{bankProvince},bank_city=#{bankCity}," +
            "account_phone=#{accountPhone} where user_code = #{userCode} and id = #{id}")
    int updateUserCard(SuperBankUserCard baseInfo);

    //开通收款功能，且未开户
    @Select("select user_code,receive_user_no from user_info where account_status = '0' and status_agent = #{statusAgent}")
    List<SuperBankUserInfo> selectByStatusAgent(@Param("statusAgent")String statusAgent);

    @Select("select user_code,receive_user_no from user_info where account_status = '0' and phone is not null")
    @ResultType(SuperBankUserInfo.class)
    List<SuperBankUserInfo> selectNotAccountList();

    @Update("update user_info set account_status=#{accountStatus} where user_code=#{userCode}")
    int updateAccount(SuperBankUserInfo userInfo);

    @Select("select total_amount from account_info where user_code=#{userCode}")
    String findUserAmount(@Param("userCode") String userCode);

    @Select("select total_amount from account_info where user_code = #{userCode}")
    BigDecimal getTotalAmount(String userCode);

    @Update("update account_info set total_amount = total_amount + #{profit} " +
            " where user_code = #{userCode} and account_type = #{accountType}")
    int updateAccountInfo(String userCode, BigDecimal profit, String accountType);

    @SelectProvider(type=SqlProvider.class, method="getUserTotal")
    @ResultType(Map.class)
    Map<String,Object> getUserTotal(@Param("baseInfo") SuperBankUserInfo info);

    @Update("update user_info set status = #{status} where user_code = #{userCode}")
    int updateUserStatus(SuperBankUserInfo userInfo);

    class SqlProvider{
        public String selectUserInfoPage(Map<String,Object> param){
            final SuperBankUserInfo info = (SuperBankUserInfo)param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT(" ui.user_code,ui.org_id,oi.is_open,ui.user_name,ui.nick_name,ui.phone");
            sql.SELECT(" ui.weixin_code,ui.total_profit,ui.user_type,ui.pay_back");
            sql.SELECT(" ui.status,ui.account_status,ui.create_date,ui.repayment_user_no,ui.receive_user_no");
            sql.SELECT(" ui.top_one_code,ui.top_two_code,ui.top_three_code");
            sql.SELECT(" ui.open_province,ui.open_city,ui.open_region");
            sql.SELECT(" ui.remark,ui.toagent_date,ui.status_mentor");
            whereSql(info, sql);
            sql.ORDER_BY("ui.create_date desc,ui.user_id desc");
            return sql.toString();
        }

        public String getUserTotal(Map<String,Object> param){
            final SuperBankUserInfo info = (SuperBankUserInfo)param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT(" sum(if(ui.user_type=10, 1, 0)) userSum");
            sql.SELECT(" sum(if(ui.user_type=20, 1, 0)) commSum");
            sql.SELECT(" sum(if(ui.user_type=30, 1, 0)) managerSum");
            sql.SELECT(" sum(if(ui.user_type=40, 1, 0)) bankerSum");
            whereSql(info, sql);
            return sql.toString();
        }

        private void whereSql(SuperBankUserInfo info, SQL sql) {
            sql.FROM(" user_info ui ");
            sql.LEFT_OUTER_JOIN(" org_info oi on ui.org_id=oi.org_id");
            if(StringUtils.isNotBlank(info.getTopOnePhone())){
                sql.INNER_JOIN("user_info oneUi on ui.top_one_code = oneUi.user_code");
            }
            if(StringUtils.isNotBlank(info.getTopTwoPhone())){
                sql.INNER_JOIN("user_info twoUi on ui.top_two_code = twoUi.user_code");
            }
            if(StringUtils.isNotBlank(info.getTopThreePhone())){
                sql.INNER_JOIN("user_info threeUi on ui.top_three_code = threeUi.user_code");
            }
            if (StringUtils.isNotBlank(info.getUserCode())) {
                sql.WHERE("ui.user_code = #{baseInfo.userCode}");
            }
            if (StringUtils.isNotBlank(info.getStatusMentor())) {
                sql.WHERE("ui.status_mentor = #{baseInfo.statusMentor}");
            }
            if (info.getOrgId() != null) {
                sql.WHERE("ui.org_id = #{baseInfo.orgId}");
            }
            if (StringUtils.isNotBlank(info.getPhone())) {
                sql.WHERE("ui.phone = #{baseInfo.phone}");
            }
            if (StringUtils.isNotBlank(info.getUserType())) {
                sql.WHERE("ui.user_type = #{baseInfo.userType}");
            }
            if (StringUtils.isNotBlank(info.getTopOneCode())) {
                sql.WHERE("ui.top_one_code = #{baseInfo.topOneCode}");
            }
            if (StringUtils.isNotBlank(info.getTopTwoCode())) {
                sql.WHERE("ui.top_two_code = #{baseInfo.topTwoCode}");
            }
            if (StringUtils.isNotBlank(info.getTopThreeCode())) {
                sql.WHERE("ui.top_three_code = #{baseInfo.topThreeCode}");
            }
            if (StringUtils.isNotBlank(info.getReceiveUserNo())) {
                sql.WHERE("ui.receive_user_no = #{baseInfo.receiveUserNo}");
            }
            if (StringUtils.isNotBlank(info.getRepaymentUserNo())) {
                sql.WHERE("ui.repayment_user_no = #{baseInfo.repaymentUserNo}");
            }
            if (info.getCreateDateStart() != null) {
                sql.WHERE("ui.create_date >= #{baseInfo.createDateStart}");
            }
            if (info.getCreateDateEnd() != null) {
                sql.WHERE("ui.create_date <= #{baseInfo.createDateEnd}");
            }
            if (StringUtils.isNotBlank(info.getPayBack())) {
                sql.WHERE("ui.pay_back = #{baseInfo.payBack}");
            }
            if (StringUtils.isNotBlank(info.getAccountStatus())) {
                sql.WHERE("ui.account_status = #{baseInfo.accountStatus}");
            }
            if (StringUtils.isNotBlank(info.getUserName())) {
                sql.WHERE("ui.user_name = #{baseInfo.userName}");
            }
            if(StringUtils.isNotBlank(info.getTopOnePhone())){
                sql.WHERE("oneUi.phone=#{baseInfo.topOnePhone}");
            }
            if(StringUtils.isNotBlank(info.getTopTwoPhone())){
                sql.WHERE("twoUi.phone = #{baseInfo.topTwoPhone}");
            }
            if(StringUtils.isNotBlank(info.getTopThreePhone())){
                sql.WHERE("threeUi.phone = #{baseInfo.topThreePhone}");
            }
            if(StringUtils.isNotBlank(info.getStatus())){
                sql.WHERE("ui.status = #{baseInfo.status}");
            }
            if(StringUtils.isNotBlank(info.getPayMoneyStatus())){
                if("0".equals(info.getPayMoneyStatus())){
                    sql.WHERE("ui.user_type = '10'");
                } else {
                    sql.WHERE("ui.user_type <> '10'");
                }
            }
            if (info.getToagentDateStart() != null) {
                sql.WHERE("ui.toagent_date >= #{baseInfo.toagentDateStart}");
            }
            if (info.getToagentDateEnd() != null) {
                sql.WHERE("ui.toagent_date <= #{baseInfo.toagentDateEnd}");
            }
            if(StringUtils.isNotBlank(info.getRemark())){
                info.setRemark(info.getRemark() + "%");
                sql.WHERE("ui.remark like #{baseInfo.remark}");
            }
            if(StringUtils.isNotBlank(info.getOpenProvince()) && !"全部".equals(info.getOpenProvince())){
                sql.WHERE("ui.open_province = #{baseInfo.openProvince}");
            }
            if(StringUtils.isNotBlank(info.getOpenCity()) && !"全部".equals(info.getOpenCity())){
                sql.WHERE("ui.open_city = #{baseInfo.openCity}");
            }
            if(StringUtils.isNotBlank(info.getOpenRegion()) && !"全部".equals(info.getOpenRegion())){
                sql.WHERE("ui.open_region = #{baseInfo.openRegion}");
            }
        }

        public String selectUserInfoList(Map<String, Object> param){
            final String userCode = (String) param.get("userCode");
            SQL sql = new SQL(){{
                SELECT("user_code, user_name");
                FROM("user_info");
                if(StringUtils.isNotBlank(userCode)){
                    WHERE("(user_code like #{userCode} or user_name like #{userCode})");
                }
                ORDER_BY("user_code limit 0,50");
            }};
            return sql.toString();
        }
        
        public String selectBlackList(Map<String,Object> param) {
          final	BlackList blackList = (BlackList)param.get("baseInfo");	
          SQL sql = new SQL();
          sql.SELECT("bk.*");
          sql.FROM("blacklist bk");
          if(blackList!=null) {
               if(StringUtils.isNotBlank(blackList.getUserCode())) {
            	   sql.WHERE("bk.user_code = #{baseInfo.userCode}");
               }
               if(StringUtils.isNotBlank(blackList.getUserPhone())) {
            	   blackList.setUserPhone(blackList.getUserPhone()+"%");
            	   sql.WHERE("bk.user_phone like #{baseInfo.userPhone}");
               }
               if(StringUtils.isNotBlank(blackList.getUserIdCard())) {
            	   blackList.setUserIdCard(blackList.getUserIdCard()+"%");
            	   sql.WHERE("bk.user_id_card like #{baseInfo.userIdCard}");
               }
               if(blackList.getStatus()!=null) {
            	   sql.WHERE("bk.status = #{baseInfo.status}");
               }
               if(blackList.getType()!=null) {
            	   sql.WHERE("bk.type = #{baseInfo.type}");
               }
               if(StringUtils.isNotBlank(blackList.getCreateBy())) {
            	   blackList.setCreateBy("%"+blackList.getCreateBy()+"%");
            	   sql.WHERE("bk.create_by like #{baseInfo.createBy}");
               }
               if (blackList.getCreateDateStart() != null) {
                   sql.WHERE("bk.create_date >= #{baseInfo.createDateStart}");
               }
               if (blackList.getCreateDateEnd() != null) {
                   sql.WHERE("bk.create_date <= #{baseInfo.createDateEnd}");
               }
          }
          sql.ORDER_BY("bk.create_date desc");
          return sql.toString();
        }
        
        public String selectBlackLogs(Map<String,Object> param){
			 return new SQL(){{
				 SELECT(" a.create_date as createDate,a.type,a.create_by as createBy,a.remark ");
				 FROM("blacklist_log a ");
				 WHERE(" a.blacklist_id = #{blackListId}");
				 ORDER_BY(" a.create_date desc ");
			 }}.toString();
		 }
        
    }
    
	
    /**
     * 是否存在该用户
     * @param userCode
     * @return
     */
    @Select("select count(1) from user_info where user_code = #{userCode} ")
    @ResultType(Integer.class)
    Integer selectUserInfoByUserCode(@Param("userCode")String userCode);


    /**
     * 查询黑名单列表
     * @param List<BlackList>
     */
    @SelectProvider(type = SqlProvider.class, method = "selectBlackList")
    @ResultType(BlackList.class)
    List<BlackList> selectBlacks(@Param("baseInfo") BlackList baseInfo, @Param("page") Page<BlackList> page);
    
    /**
     *查询黑名单日志 
     * @param List<BlackListLog>
     */
	@SelectProvider(type=SqlProvider.class,method="selectBlackLogs")
	@ResultType(BlackListLog.class)
	List<BlackListLog> selectBlackLogs(@Param("baseInfo")Page<BlackListLog> baseInfo,@Param("blackListId")Long blackId);
    
    /**
     * 新增黑名单
     * @param int
     */
    @Options(useGeneratedKeys=true, keyProperty="id")
    @Insert({"insert into blacklist(type,user_code,user_phone,user_id_card,status,create_date,create_by,remark) values(#{type},#{userCode},#{userPhone},#{userIdCard},#{status},#{createDate,jdbcType=TIMESTAMP},#{createBy},#{remark})"})
    int insertBlack(BlackList baseInfo);
    /**
     *新增黑名单日志 
     * @param int
     */
    @Options(useGeneratedKeys=true,keyProperty="id")
    @Insert("insert into blacklist_log(type,blacklist_id,status,create_date,create_by,remark) values(#{type},#{blackListId},#{status},#{createDate},#{createBy},#{remark})")
    int insertBlackLog(BlackListLog blackListLog);
    
    /**
     *同一个类型，同一个用户ID 查询是否存在
     * @param int
     */
    @Select("select count(1) from blacklist where type = #{type} and user_code=#{userCode}")
    int selectblackByUserId(@Param("type")Integer type,@Param("userCode")String userCode);
    /**
     * 
     * 同一个类型，同一个手机号 查询是否存在
     * @param int
     */
    @Select("select count(1) from blacklist where type = #{type} and user_phone=#{userPhone}")
    int selectblackByPhone(@Param("type")Integer type,@Param("userPhone")String userPhone);
    /**
     * 同一个类型，同一个身份证号 查询是否存在
     * @param int
     */
    @Select("select count(1) from blacklist where type = #{type} and user_id_card=#{userIdCard}")
    int selectblackByIdCard(@Param("type")Integer type,@Param("userIdCard")String userIdCard);
    /**
     * 删除黑名单
     * @param int
     */
	@Delete("delete from blacklist where id=#{id}")
	int deleteBlackById(@Param("id")Long id);
    /**
     * 查询
     * @param BlackList
     */
	@Select("select b.* from blacklist b where b.id=#{id}")
	@ResultType(BlackList.class)
	BlackList selectBlackDetail(@Param("id")Long id);
	
	@Update("update blacklist b set b.status=#{baseInfo.status},b.remark=#{baseInfo.remark} where b.id=#{baseInfo.id}")
	int updateblackStatus(@Param("baseInfo")BlackList baseInfo) ;
	
}
