package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.TradeRestrict;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/5/005.
 * @author  liuks
 * 交易限制查询 dao
 */
public interface TradeRestrictDao {

    @SelectProvider(type=TradeRestrictDao.SqlProvider.class,method="selectAllList")
    @ResultType(TradeRestrict.class)
    List<TradeRestrict> selectAllList(@Param("info") TradeRestrict info,@Param("page") Page<TradeRestrict> page);

    @SelectProvider(type=TradeRestrictDao.SqlProvider.class,method="selectAllList")
    @ResultType(TradeRestrict.class)
    List<TradeRestrict> importDetailSelect(@Param("info")TradeRestrict info);

    @Select(
            "select * from risk_trans_limit where id=#{id}"
    )
    TradeRestrict getInfo(@Param("id")int id);

    @Update(
            "update risk_trans_limit " +
                    " set status=#{status},create_person=#{userName},operation_time=NOW() " +
                    " where id=#{id}"
    )
    int updateInfoStatus(@Param("id")int id,@Param("status")int status,@Param("userName")String userName);

    @Update(
            "update risk_trans_limit" +
                    " set status=3,operation_time=NOW() " +
                    "where invalid_time < NOW() and status!=3 "
    )
    int updateFailureTime();


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final TradeRestrict info = (TradeRestrict) param.get("info");
            return new SQL(){{
                SELECT("risk.*,mer.merchant_name,mer.mobilephone merchantPhone,mer.create_time merchantTime ");
                FROM("risk_trans_limit risk");
                LEFT_OUTER_JOIN("merchant_info mer ON mer.merchant_no=risk.merchant_no");
                if(StringUtils.isNotBlank(info.getLimitNumber())){
                    WHERE("risk.limit_number = #{info.limitNumber} ");
                }
                if(StringUtils.isNotBlank(info.getMerchantName())){
                    WHERE("mer.merchant_name like concat(#{info.merchantName},'%') ");
                }
                if(StringUtils.isNotBlank(info.getAgentNo())){
                    WHERE("risk.agent_no = #{info.agentNo} ");
                }
                if(StringUtils.isNotBlank(info.getOrderNo())){
                    WHERE("risk.order_no = #{info.orderNo} ");
                }
                if(StringUtils.isNotBlank(info.getMerchantPhone())){
                    WHERE("mer.mobilephone = #{info.merchantPhone} ");
                }
                if(StringUtils.isNotBlank(info.getRollNo())){
                    WHERE("risk.roll_no = #{info.rollNo} ");
                }
                if(info.getTriggerNumber()!=null){
                    WHERE("risk.trigger_number = #{info.triggerNumber} ");
                }
                if(info.getStatus()!=null&&info.getStatus().intValue()>-1){
                    WHERE("risk.status = #{info.status} ");
                }else{//全部查询开启关闭
                    WHERE("risk.status !=0");
                }
                if(StringUtils.isNotBlank(info.getResultNo())){
                    WHERE("risk.result_no = #{info.resultNo} ");
                }
                if(StringUtils.isNotBlank(info.getCreatePerson())){
                    WHERE("risk.create_person = #{info.createPerson} ");
                }
                if(info.getMerchantTimeBegin()!= null){
                    WHERE("mer.create_time >= #{info.merchantTimeBegin} ");
                }
                if(info.getMerchantTimeEnd()!= null){
                    WHERE("mer.create_time <= #{info.merchantTimeEnd} ");
                }
                if(info.getCreateTimeBegin()!= null){
                    WHERE("risk.create_time >= #{info.createTimeBegin} ");
                }
                if(info.getCreateTimeEnd()!= null){
                    WHERE("risk.create_time <= #{info.createTimeEnd} ");
                }
                if(info.getOperationTimeBegin()!= null){
                    WHERE("risk.operation_time >= #{info.operationTimeBegin} ");
                }
                if(info.getOperationTimeEnd()!= null){
                    WHERE("risk.operation_time <= #{info.operationTimeEnd} ");
                }
                ORDER_BY("risk.create_time desc");
            }}.toString();
        }
    }
}
