package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RechargeRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 充值记录
 */
public interface RechargeRecordDao {

    @SelectProvider(type = SqlProvider.class, method = "getRechargeList")
    List<RechargeRecord> getRechargeList(@Param("baseInfo")RechargeRecord baseInfo,
                                         @Param("page")Page<RechargeRecord> page);

    @Insert("insert into recharge_record(create_date,amount_balance,recharge_amount" +
            ",user_id,user_name)" +
            " values(#{createDate},#{amountBalance},#{rechargeAmount}" +
            ",#{userId},#{userName})")
    int insert(RechargeRecord record);

    class SqlProvider{
        public String getRechargeList(Map<String, Object> params){
            RechargeRecord baseInfo = (RechargeRecord)params.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("id, create_date, amount_balance,recharge_amount, user_id, user_name");
            sql.FROM("recharge_record");
            if(baseInfo != null){
                if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                    sql.WHERE("create_date >= #{baseInfo.createDateStart}");
                }
                if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                    sql.WHERE("create_date <= #{baseInfo.createDateEnd}");
                }
            }
            sql.ORDER_BY("create_date desc, id desc");
            return sql.toString();
        }
    }
}
