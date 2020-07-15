package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.UserProfit;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @Date 2017-12-4
 */
public interface UserProfitDao {


    @Select("select profit_formula,user_type from user_profit where order_no = #{orderNo}")
    @ResultType(UserProfit.class)
    List<UserProfit> selectByOrderNo(String orderNo);

    @SelectProvider(type = SqlProvider.class, method="selectProfitDetailPage")
    @ResultType(UserProfit.class)
    List<UserProfit> selectProfitDetailPage(@Param("baseInfo")UserProfit baseInfo, @Param("page")Page<UserProfit> page);

    @SelectProvider(type = SqlProvider.class, method="selectUserProfitSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectUserProfitSum(@Param("baseInfo")UserProfit baseInfo);

    @SelectProvider(type = SqlProvider.class, method="selectTotalProfitSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectTotalProfitSum(@Param("baseInfo")UserProfit baseInfo);

    @Update("update user_profit set account_status = #{accountStatus}" +
            ",update_by=#{updateBy}, update_date=#{updateDate} " +
            " where order_no = #{orderNo}")
    int updateAccountStatus(OrderMain orderMain);

    class SqlProvider{
        public String selectProfitDetailPage(Map<String, Object> param){
            UserProfit baseInfo = (UserProfit) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("up.id,up.order_type,up.status,up.order_no");
            sql.SELECT("up.org_id,up.share_user_code,up.user_code,up.user_type");
            sql.SELECT("up.total_profit,up.user_profit,up.create_date,up.account_status");
            sql.SELECT("up.profit_level,up.remark");
            sql.SELECT("share.nick_name shareNickName,share.user_name shareUserName,share.phone shareUserPhone");
            sql.SELECT("share.open_province,share.open_city,share.open_region,share.remark as shareUserRemark");
            sql.SELECT("user.user_name");
            sql.FROM("user_profit up");
            whereSql(baseInfo, sql);
            return sql.toString();
        }

        public String selectUserProfitSum(Map<String, Object> param){
            UserProfit baseInfo = (UserProfit) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("sum(up.user_profit) as profitSum");
            sql.FROM("user_profit up");
            whereSql(baseInfo, sql);
//            sql.WHERE("up.status = '5'");
            return sql.toString();
        }

        public String selectTotalProfitSum(Map<String, Object> param){
            UserProfit baseInfo = (UserProfit) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT_DISTINCT("up.order_no, up.total_profit");
//            sql.SELECT("sum(up.total_profit) as totalBonusSum");
            sql.FROM("user_profit up");
            whereSql(baseInfo, sql);
//            sql.WHERE("up.status = '5'");
            StringBuilder sb = new StringBuilder();
            sb.append("select sum(t.total_profit) as totalBonusSum");
            sb.append(" from (").append(sql.toString()).append(") as t");
            return sb.toString();
        }

        private void whereSql(UserProfit baseInfo, SQL sql) {
            sql.LEFT_OUTER_JOIN("user_info share on share.user_code = up.share_user_code");
            sql.LEFT_OUTER_JOIN("user_info user on user.user_code = up.user_code");
            if(StringUtils.isNotBlank(baseInfo.getOrderNo())){
                sql.WHERE("up.order_no = #{baseInfo.orderNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getStatus())){
                sql.WHERE("up.status = #{baseInfo.status}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderType())){
                sql.WHERE("up.order_type = #{baseInfo.orderType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                sql.WHERE("up.create_date >= #{baseInfo.createDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                sql.WHERE("up.create_date <= #{baseInfo.createDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserCode())){
                sql.WHERE("up.share_user_code = #{baseInfo.shareUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserPhone())){
                sql.WHERE("share.phone = #{baseInfo.shareUserPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserCode())){
                sql.WHERE("up.user_code = #{baseInfo.userCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserPhone())){
                sql.WHERE("user.phone = #{baseInfo.userPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getAccountStatus())){
                sql.WHERE("up.account_status = #{baseInfo.accountStatus}");
            }
            if(baseInfo.getOrgId() != null && -1 != baseInfo.getOrgId()){
                sql.WHERE("up.org_id = #{baseInfo.orgId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserType())){
                sql.WHERE("up.user_type = #{baseInfo.userType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserName())){
                baseInfo.setUserName(baseInfo.getUserName() + "%");
                sql.WHERE("user.user_name like #{baseInfo.userName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserName())){
                baseInfo.setShareUserName(baseInfo.getShareUserName() + "%");
                sql.WHERE("share.user_name like #{baseInfo.shareUserName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRemark())){
                baseInfo.setRemark(baseInfo.getRemark() + "%");
                sql.WHERE("up.remark like #{baseInfo.remark}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserRemark())){
                baseInfo.setShareUserRemark(baseInfo.getShareUserRemark() + "%");
                sql.WHERE("share.remark like #{baseInfo.shareUserRemark}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenProvince()) && !"全部".equals(baseInfo.getOpenProvince())){
                sql.WHERE("share.open_province = #{baseInfo.openProvince}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenCity()) && !"全部".equals(baseInfo.getOpenCity())){
                sql.WHERE("share.open_city = #{baseInfo.openCity}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenRegion()) && !"全部".equals(baseInfo.getOpenRegion())){
                sql.WHERE("share.open_region = #{baseInfo.openRegion}");
            }
            sql.ORDER_BY("up.create_date desc");
        }
    }

}
