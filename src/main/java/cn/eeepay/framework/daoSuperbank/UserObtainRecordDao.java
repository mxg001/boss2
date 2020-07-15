package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.UserObtainRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @data 2017-12-6
 * super.user_obtain_record
 * 分润提现记录表
 */
public interface UserObtainRecordDao {

    @SelectProvider(type = SqlProvider.class, method = "selectObtainRecordPage")
    @ResultType(UserObtainRecord.class)
    List<UserObtainRecord> selectObtainRecordPage(@Param("baseInfo") UserObtainRecord baseInfo, @Param("page")Page<UserObtainRecord> page);

    @SelectProvider(type = SqlProvider.class, method = "selectObtainRecordSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectObtainRecordSum(@Param("baseInfo")UserObtainRecord baseInfo);

    class SqlProvider{
        public String selectObtainRecordPage(Map<String, Object> param){
            UserObtainRecord baseInfo = (UserObtainRecord)param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("uor.obtain_no,uor.user_code");
            sql.SELECT("uor.status,uor.order_type");
            sql.SELECT("uor.obtain_amount,uor.obtain_fee,uor.real_obtain_amount,uor.create_date");
            sql.SELECT("ui.nick_name,ui.user_name,ui.phone,ui.user_type,ui.org_id");
            whereSql(baseInfo, sql);
            sql.ORDER_BY("uor.create_date desc");
            return  sql.toString();
        }

        public String selectObtainRecordSum(Map<String, Object> param){
            UserObtainRecord baseInfo = (UserObtainRecord)param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("sum(uor.obtain_amount) as obtainAmountSum");
            sql.SELECT("sum(uor.real_obtain_amount) as realObtainAmountSum");
            sql.SELECT("sum(uor.obtain_fee) as obtainFeeSum");
            whereSql(baseInfo, sql);
//            sql.WHERE("uor.status = '3'");
            return  sql.toString();
        }

        private void whereSql(UserObtainRecord baseInfo, SQL sql) {
            sql.FROM("user_obtain_record uor");
            sql.INNER_JOIN("user_info ui on ui.user_code = uor.user_code");
            if(StringUtils.isNotBlank(baseInfo.getObtainNo())){
                sql.WHERE("uor.obtain_no = #{baseInfo.obtainNo}");
            }
            if(baseInfo.getUserCode() != null){
                sql.WHERE("uor.user_code = #{baseInfo.userCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPhone())){
                sql.WHERE("ui.phone = #{baseInfo.phone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getStatus())){
                sql.WHERE("uor.status = #{baseInfo.status}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderType())){
                sql.WHERE("uor.order_type = #{baseInfo.orderType}");
            }
            if(baseInfo.getObtainAmountStart() != null){
                sql.WHERE("uor.obtain_amount >= #{baseInfo.obtainAmountStart}");
            }
            if(baseInfo.getObtainAmountEnd() != null){
                sql.WHERE("uor.obtain_amount <= #{baseInfo.obtainAmountEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                sql.WHERE("uor.create_date >= #{baseInfo.createDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                sql.WHERE("uor.create_date <= #{baseInfo.createDateEnd}");
            }
        }

    }
}
