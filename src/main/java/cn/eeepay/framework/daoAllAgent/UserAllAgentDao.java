package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.CountSet;
import cn.eeepay.framework.model.allAgent.UserAllAgent;
import cn.eeepay.framework.model.allAgent.UserAllAgentCard;
import cn.eeepay.framework.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/12/012.
 */
public interface UserAllAgentDao {

    @SelectProvider(type=SqlProvider.class,method="selectAllList")
    @ResultType(UserAllAgent.class)
    List<UserAllAgent> selectAllList(@Param("user")UserAllAgent user,@Param("page") Page<UserAllAgent> page);

    @SelectProvider(type=SqlProvider.class,method="selectAllSum")
    @ResultType(CountSet.class)
    CountSet selectAllSum(@Param("user")UserAllAgent user, @Param("page") Page<UserAllAgent> page);

    @SelectProvider(type=SqlProvider.class,method="selectAllList")
    @ResultType(UserAllAgent.class)
    List<UserAllAgent> importDetailSelect(@Param("user")UserAllAgent user);

    @Select(
            " select count(*) from pa_mer_info where user_code=#{userCode} "
    )
    int sumMer(@Param("userCode")String userCode);

    @Select(
            " select count(*) from pa_user_info where parent_id=#{userCode} "
    )
    int sumUser(@Param("userCode")String userCode);

    @Select(
            " select * from pa_user_info where user_code=#{userCode} "
    )
    UserAllAgent selectUser(@Param("userCode")String userCode);

    @Select(
            " select pauser.*, IF(pauser.id_card_no is null,0,1) idCardNoState," +
                    " oem.brand_name,agent1.agent_no oneAgentNo ,agent2.agent_no twoAgentNo, " +
                    " (select CONCAT('Lv.',ladder.ladder_grade,'(万分之',ladder.val,')') from pa_ladder_setting ladder "+
                    "    where ladder.type='1' and ladder.brand_code=pauser.brand_code and ladder.ladder_grade=pauser.share_level) gradeStr,vip.val vipShareRatio "+
                    " from pa_user_info pauser " +
                    " LEFT JOIN pa_brand oem ON oem.brand_code=pauser.brand_code " +
                    " LEFT JOIN pa_agent_user agent1 ON agent1.user_code=pauser.one_user_code " +
                    " LEFT JOIN pa_agent_user agent2 ON agent2.user_code=pauser.two_user_code " +
                    " LEFT JOIN pa_ladder_setting vip ON vip.ladder_grade=pauser.vip_share_level and vip.type=3 and vip.brand_code=pauser.brand_code" +
                    " where pauser.id=#{id} "
    )
    UserAllAgent getUserAllAgent(@Param("id")int id);

    @Select(
            "select * from pa_user_card where user_code=#{userCode} and is_settle=#{isSettle} "
    )
    List<UserAllAgentCard> getUserCard(@Param("userCode")String userCode,@Param("isSettle")int isSettle);

    @SelectProvider(type=SqlProvider.class,method="getOrgList")
    @ResultType(UserAllAgent.class)
    List<UserAllAgent> getOrgList(@Param("brandCodes")String brandCodes,@Param("userType")int userType);

    @Update("UPDATE pa_user_info SET pwd=#{u.pwd},mobile=#{u.mobile},real_name=#{u.realName},id_card_no=#{u.idCardNo} WHERE user_code=#{u.userCode}")
    int updateUserAllAgent(@Param("u")UserAllAgent userAllAgent);

    @UpdateProvider(type=SqlProvider.class,method="updateUserAllAgentBySave")
    int updateUserAllAgentBySave(@Param("u")UserAllAgent userAllAgent);

    @Select("select user_code from pa_agent_user where agent_no=#{agentNo}")
    @ResultType(String.class)
    String getUserAllAgentByAgentNo(@Param("agentNo")String agentNo);

    @Select("select * from pa_user_card where user_code=#{userCode} and is_settle=1")
    @ResultType(UserAllAgentCard.class)
    UserAllAgentCard getUserCardAllAgentByUserCode(@Param("userCode")String userCode);

    @Select("select count(*) from pa_user_info where user_code!=#{u.userCode} and (mobile=#{u.mobile} or id_card_no=#{u.idCardNo})")
    int getUserCardAllAgentCount(@Param("u")UserAllAgent userAllAgent);

    @Insert("INSERT INTO pa_user_card ( user_code, bank_name, bank_branch_name, account, mobile, cnaps, card_type, is_settle, create_time, status) VALUES " +
            "( #{uc.userCode}, #{uc.bankName}, #{uc.bankBranchName}, #{uc.account}, #{uc.mobile}, #{uc.cnaps}, '借记卡', '1', NOW(), NULL)")
    int insertUserCardAllAgent(@Param("uc")UserAllAgentCard userCard);

    @Insert("INSERT INTO pa_user_card ( user_code, bank_name, bank_branch_name, account, mobile, address, card_type, is_settle, create_time, status) VALUES " +
            "( #{uc.userCode}, #{uc.bankName}, #{uc.bankName}, #{uc.account}, #{uc.mobile}, #{uc.address}, '借记卡', '1', NOW(), NULL)")
    int insertUserCardAllAgentBySave(@Param("uc")UserAllAgentCard userCard);

    @Update("UPDATE pa_user_card SET bank_name=#{uc.bankName},bank_branch_name=#{uc.bankName}, account=#{uc.account}, mobile=#{uc.mobile}, address=#{uc.address}, cnaps=#{uc.cnaps},last_update=NOW() WHERE id=#{uc.id}")
    int updateUserCardAllAgent(@Param("uc")UserAllAgentCard userCard);

    @SelectProvider(type=SqlProvider.class,method="selectDividedAdjustDetail")
    @ResultType(UserAllAgent.class)
    List<UserAllAgent> selectDividedAdjustDetail(@Param("user")UserAllAgent user);

    @SelectProvider(type=SqlProvider.class,method="selectDividedAdjustDetail")
    @ResultType(UserAllAgent.class)
    List<UserAllAgent> selectDividedAdjustDetailList(@Param("user") UserAllAgent user,@Param("page") Page<UserAllAgent> page);

    @Select("select * from pa_user_info where user_type=1")
    @ResultType(UserAllAgent.class)
    List<UserAllAgent> selectOneUserCodeList();

    @SelectProvider(type=SqlProvider.class,method="selectSumMerUserTransShare")
    @ResultType(String.class)
    String selectSumMerUserTransShare(@Param("user") UserAllAgent user);

    @SelectProvider(type=SqlProvider.class,method="selectSumMerTransShare")
    @ResultType(String.class)
    String selectSumMerTransShare(@Param("user") UserAllAgent user);

    @Select("select count(*) from pa_mer_info pai " +
            "where pai.is_act=1 and pai.user_node like concat(#{userNode},'%') and pai.user_node != #{userNode}")
    @ResultType(String.class)
    String selectSumActivationMer(@Param("userNode") String userNode);

    /**
     * 根据userCode查询用户
     * @param userCode
     * @return
     */
    @Select(
            "select * from pa_user_info where user_code = #{userCode} "
    )
    UserAllAgent getUserOne(@Param("userCode")String userCode);

    @SelectProvider(type=UserAllAgentDao.SqlProvider.class,method="getUserByCondition")
    @ResultType(UserAllAgent.class)
    List<UserAllAgent> getUserByStr(@Param("str")String str,@Param("level")String level);

    class SqlProvider{
        public String getOrgList(final Map<String, Object> param) {
            final String brandCodes = (String) param.get("brandCodes");
            StringBuffer sb=new StringBuffer();
            sb.append(" select pauser.*,agent.agent_no oneAgentNo ");
            sb.append("  from pa_user_info pauser ");
            sb.append("   LEFT JOIN pa_agent_user agent ON agent.user_code=pauser.user_code");
            sb.append(" where 1=1");
            sb.append(" and pauser.user_type=#{userType}" );
            if(StringUtils.isNotBlank(brandCodes)){
                sb.append(" and pauser.brand_code in ("+brandCodes+") " );
            }
            return sb.toString();
        }
        public String selectAllList(final Map<String, Object> param) {
            return selectAllSql(param,1);
        }
        public String selectAllSum(final Map<String, Object> param) {
            return selectAllSql(param,2);
        }

        public String selectAllSql(final Map<String, Object> param,int sta) {
            final UserAllAgent user = (UserAllAgent) param.get("user");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            if(sta==1){
                sb.append("    pauser.*,oem.brand_name,agent.agent_no as select_agent_no, ");
                sb.append("    IF(pauser.id_card_no is null,0,1) idCardNoState,");
                sb.append("    t3.real_name parentRealName,t1.cou1 sumUser,t2.cou2 sumMer, ");
                sb.append("    (select CONCAT('万分之',ladder.val) from pa_ladder_setting ladder ");
                sb.append("      where ladder.type='1' and ladder.brand_code=pauser.brand_code and ladder.ladder_grade=pauser.share_level) gradeStr,CONCAT('万分之',vip.val) vipShareRatio, ");
                sb.append("    IF(pauser.user_type=1," +
                        "(SELECT MAX(pls.val) from pa_ladder_setting pls where pls.brand_code=pauser.brand_code and pls.type=2)," +
                        "(select glory_rate from pa_month_count pmc where pmc.user_code=pauser.user_code ORDER BY pmc.count_month desc limit 1)) shareRatio ");
            }else if(sta==2){
                sb.append(" sum(IF(pauser.user_type=1,1,0)) allyOneCount, ");
                sb.append(" sum(IF(pauser.user_type=2,1,0)) allyTwoCount, ");
                sb.append(" sum(IF(pauser.user_type=3,1,0)) allyCount, ");
                sb.append(" sum(IF(pauser.reg_type='merchant' and pauser.user_type=3,1,0)) businessCount ");
            }
            sb.append("   from pa_user_info pauser ");
            sb.append("     LEFT JOIN pa_brand oem ON oem.brand_code=pauser.brand_code");
            sb.append("     LEFT JOIN pa_user_info t3 ON t3.user_code=pauser.parent_id");
            sb.append("     LEFT JOIN (select parent_id par1,count(*) cou1 from pa_user_info GROUP BY parent_id ) t1 ON t1.par1=pauser.user_code");
            sb.append("     LEFT JOIN (select user_code par2,count(*) cou2 from pa_mer_info  GROUP BY user_code ) t2 ON t2.par2=pauser.user_code");
            sb.append("     LEFT JOIN pa_agent_user agent ON agent.user_code = pauser.user_code ");
            sb.append("     LEFT JOIN pa_ladder_setting vip ON vip.ladder_grade=pauser.vip_share_level and vip.type=3 and vip.brand_code=pauser.brand_code");
            sb.append("   where 1=1 ");
            where(sb,user);
            sb.append("   ORDER BY pauser.id ");
            return sb.toString();
        }

        public void where(StringBuffer sb, UserAllAgent user) {
            if(StringUtils.isNotBlank(user.getUserCode())){
                sb.append("  and pauser.user_code =#{user.userCode} ");
            }
            if(StringUtils.isNotBlank(user.getRealName())){
                sb.append("  and pauser.real_name like concat(#{user.realName},'%') ");
            }
            if(StringUtils.isNotBlank(user.getNickName())){
                sb.append("  and pauser.nick_name like concat('%',#{user.nickName},'%') ");
            }
            if(StringUtils.isNotBlank(user.getMobile())){
                sb.append("  and pauser.mobile =#{user.mobile} ");
            }
            if(StringUtils.isNotBlank(user.getBrandCode())){
                sb.append("  and pauser.brand_code =#{user.brandCode} ");
            }
            if(user.getUserType()!=null){
                sb.append("  and pauser.user_type =#{user.userType} ");
            }
            if(StringUtils.isNotBlank(user.getParentId())){
                sb.append("  and pauser.parent_id =#{user.parentId} ");
            }
            if(StringUtils.isNotBlank(user.getTwoUserCode())){
                sb.append("  and pauser.two_user_code =#{user.twoUserCode} ");
            }
            if(StringUtils.isNotBlank(user.getOneUserCode())){
                sb.append("  and pauser.one_user_code =#{user.oneUserCode} ");
            }
            if(user.getIdCardNoState()!=null){
                if(user.getIdCardNoState().intValue()==0){
                    sb.append(" and pauser.id_card_no is null ");
                }else if(user.getIdCardNoState().intValue()==1){
                    sb.append(" and pauser.id_card_no is not null ");
                }
            }
            if(user.getCreateTimeBegin() != null){
                sb.append("  and pauser.create_time >= #{user.createTimeBegin}");
            }
            if(user.getCreateTimeEnd() != null){
                sb.append("  and pauser.create_time <= #{user.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(user.getIdCardNo())){
                sb.append("  and pauser.id_card_no =#{user.idCardNo} ");
            }
            if(StringUtils.isNotBlank(user.getGrade())){
                sb.append("  and pauser.grade =#{user.grade} ");
            }
            if(StringUtil.isNotBlank(user.getAgentNo())){
            	 sb.append("  and agent.agent_no =#{user.agentNo} ");
            }
        }

        public String updateUserAllAgentBySave(final Map<String, Object> param) {
            final UserAllAgent u = (UserAllAgent) param.get("u");
            SQL sql = new SQL(){{
                UPDATE("pa_user_info");
                SET("real_name=#{u.realName},mobile=#{u.mobile},id_card_no=#{u.idCardNo}");
                if(StringUtils.isNotBlank(u.getPwd())){
                    SET("pwd=#{u.pwd}");
                }
                if(StringUtils.isNotBlank(u.getNickName())){
                    SET("nick_name=#{u.nickName}");
                }
                WHERE("user_code=#{u.userCode}");
            }};
            return sql.toString();
        }

        public String selectDividedAdjustDetail(final Map<String, Object> param) {
            final UserAllAgent user = (UserAllAgent) param.get("user");
            StringBuffer sb=new StringBuffer();
            sb.append(" SELECT pauser.user_code,pauser.real_name,pauser.one_user_code,pauser.two_user_code, ");
            sb.append("   ladder.ladder_grade gradeRate,ladder.val shareRatio,");
            sb.append("   plc.pre_rate,plc.after_rate,plc.create_time from  pa_level_change plc ");
            sb.append("   LEFT JOIN pa_user_info pauser ON plc.user_code=pauser.user_code ");
            sb.append("   LEFT JOIN pa_ladder_setting ladder on ladder.type='1' and ladder.brand_code=pauser.brand_code and ladder.ladder_grade=pauser.share_level ");
            sb.append("   where 1=1 and plc.status='1' ");
            where(sb,user);
            sb.append("   ORDER BY plc.create_time desc ");
            return sb.toString();
        }

        public String getUserByCondition(Map<String, Object> param) {
            String str =(String) param.get("str");
            String level =(String) param.get("level");
            StringBuffer sb=new StringBuffer();
            sb.append("select * from pa_user_info");
            sb.append(" where  1=1 ");
            if(StringUtils.isNotBlank(str)){
                if(str.indexOf("M")>=0||str.indexOf("m")>=0){
                    sb.append("  and user_code =#{str} ");
                }else{
                    sb.append("  and real_name like concat(#{str},'%') ");
                }
            }
            if(StringUtils.isNotBlank(level)){
                if("1".equals(level)){
                    sb.append("  and user_type =1 ");
                }else if("2".equals(level)){
                    sb.append("  and user_type =2 ");
                }else if("3".equals(level)){
                    sb.append("  and user_type =3 ");
                }else{

                }
            }
            sb.append(" LIMIT 200 ");
            return sb.toString();
        }

        public String selectSumMerUserTransShare(final Map<String, Object> param) {
            final UserAllAgent info = (UserAllAgent) param.get("user");
            SQL sql = new SQL(){{
                SELECT("sum(trans_amount) sum ");
                FROM("pa_trans_info");
                WHERE("user_node like concat(#{user.userNode},'%')");
            }};
            sumShere(sql, info);
            return sql.toString();
        }

        public String selectSumMerTransShare(final Map<String, Object> param) {
            final UserAllAgent info = (UserAllAgent) param.get("user");
            SQL sql = new SQL(){{
                SELECT("sum(trans_amount) sum");
                FROM("pa_trans_info");
                WHERE("user_code=#{user.userCode}");
            }};
            sumShere(sql, info);
            return sql.toString();
        }

        public void sumShere(SQL sql, UserAllAgent info) {
            if(StringUtils.isNotBlank(info.getTransStartTime())){
                sql.WHERE("trans_time  >= #{user.transStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getTransEndTime())){
                sql.WHERE("trans_time  <= #{user.transEndTime} ");
            }
        }
    }
}
