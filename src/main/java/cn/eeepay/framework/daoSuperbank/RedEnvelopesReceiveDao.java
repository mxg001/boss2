package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedEnvelopesReceive;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/18/018.
 * 红包领取查询dao
 */
public interface RedEnvelopesReceiveDao {

    /**
     *多条件分页查询
     */
    @SelectProvider(type=RedEnvelopesReceiveDao.SqlProvider.class,method="selectAllList")
    @ResultType(RedEnvelopesReceive.class)
    List<RedEnvelopesReceive> selectAllByParam(@Param("red")RedEnvelopesReceive red, @Param("page")Page<RedEnvelopesReceive> page);

    @SelectProvider(type=RedEnvelopesReceiveDao.SqlProvider.class,method="selectAllList")
    @ResultType(RedEnvelopesReceive.class)
    List<RedEnvelopesReceive> exportInfo(@Param("red")RedEnvelopesReceive order);


    @SelectProvider(type=RedEnvelopesReceiveDao.SqlProvider.class,method="selectAllList")
    @ResultType(RedEnvelopesReceive.class)
    RedEnvelopesReceive sumCount(@Param("red")RedEnvelopesReceive order);

    @Select(
            " select ror.*,ui.nick_name " +
                    " from red_orders_receive ror " +
                    " LEFT JOIN user_info ui ON ui.user_code=ror.get_user_code " +
                    " where ror.red_order_id=#{id} and ror.status=1 "
    )
    @ResultType(RedEnvelopesReceive.class)
    List<RedEnvelopesReceive> selectRedEnvelopesReceive(@Param("id")Long id,@Param("page")Page<RedEnvelopesReceive> page);

    @Update("update red_orders_receive set status=#{status}, get_date=#{getDate} where red_order_id=#{redOrderId} " +
            " and status=#{oldStatus}")
    int updateStatus(RedEnvelopesReceive receive);

    @Select("select sum(amount) from red_orders_receive where red_order_id=#{redOrderId} and status=#{status}")
    BigDecimal totalReceiveAmount(@Param("redOrderId") Long redOrderId, @Param("status") String status);

    class SqlProvider {
        public String selectAllList(final Map<String, Object> param){
            final RedEnvelopesReceive red = (RedEnvelopesReceive) param.get("red");
            String str=new SQL() {{
                if(red.getSumState()==0){
                    SELECT(" red.*,ui.user_name territoryUserName,ui.phone territoryPhone,oi.org_name territoryOrgName") ;
//                            "rtbe.user_code as dividendUserCode,rtod.basic_bonus_amount,rtbe.territory_price,rtbe.territory_avg_price," +
//                            "rtbe.adjust_ratio,rtod.bonus_amount,rtbe.status as dividendStatus,rtbe.user_code as dividendUserCode,rtbe.receive_time," +
//                            "rtod.rate,rtod.rate_type");
                }else if(red.getSumState()==1){
                    SELECT(" sum(red.amount) amount_count,sum(red.lords_profit) totalLordsProfit");
                }
                FROM("red_orders_receive red");
//                LEFT_OUTER_JOIN("red_territory_order_detail rtod on rtod.recId = red.id");
//                LEFT_OUTER_JOIN("red_territory_bonus_everyday rtbe on rtbe.id = rtod.bonus_everyday_id");
                LEFT_OUTER_JOIN("user_info ui on ui.user_code = red.territory_user_code");
                LEFT_OUTER_JOIN("org_info oi on ui.org_id = oi.org_id");
                
                if(red.getId()!=null){
                    WHERE(" red.id=#{red.id}");
                }
                if(red.getRedOrderId()!=null){
                    WHERE(" red.red_order_id=#{red.redOrderId}");
                }
                if(StringUtils.isNotBlank(red.getStatus())){
                    WHERE(" red.status=#{red.status}");
                }
                if(StringUtils.isNotBlank(red.getOrderNo())){
                    WHERE(" red.order_no=#{red.orderNo}");
                }
                if(StringUtils.isNotBlank(red.getPushUserCode())){
                    WHERE(" red.push_user_code=#{red.pushUserCode}");
                }
                if(StringUtils.isNotBlank(red.getPushUserName())){
                    WHERE(" red.push_user_name like concat(#{red.pushUserName},'%')");
                }
                if(StringUtils.isNotBlank(red.getPushUserPhone())){
                    WHERE(" red.push_user_phone=#{red.pushUserPhone}");
                }
                if(StringUtils.isNotBlank(red.getGetUserCode())){
                    WHERE(" red.get_user_code=#{red.getUserCode}");
                }
                if(StringUtils.isNotBlank(red.getGetUserName())){
                    WHERE(" red.get_user_name like concat(#{red.getUserName},'%')");
                }
                if(StringUtils.isNotBlank(red.getGetUserPhone())){
                    WHERE(" red.get_user_phone=#{red.getUserPhone}");
                }
                if(StringUtils.isNotBlank(red.getPushType())){
                    WHERE(" red.push_type=#{red.pushType}");
                }
                if(StringUtils.isNotBlank(red.getReceiveType())){
                    WHERE(" red.receive_type=#{red.receiveType}");
                }
                if(StringUtils.isNotBlank(red.getBusType())){
                    WHERE(" red.bus_type=#{red.busType}");
                }
                if(red.getOrgId()!=null){
                    WHERE(" red.org_id=#{red.orgId}");
                }
                if(red.getGetDateMin()!=null){
                    WHERE(" red.get_date>=#{red.getDateMin}");
                }
                if(red.getGetDateMax()!=null){
                    WHERE(" red.get_date<=#{red.getDateMax}");
                }
//                if(StringUtils.isNotBlank(red.getDividendStatus())){
//                    WHERE(" rtbe.status=#{red.getDividendStatus}");
//                }

                //地区
            	if(StringUtils.isNotBlank(red.getTerritoryProvinceName())){
            		WHERE("red.territory_province_name = #{red.territoryProvinceName}");
            	}
            	if(StringUtils.isNotBlank(red.getTerritoryCityName())){
            		WHERE("red.territory_city_name = #{red.territoryCityName}");
            	}
            	if(StringUtils.isNotBlank(red.getTerritoryRegionName())){
            		WHERE("red.territory_region_name = #{red.territoryRegionName}");
            	}
                
                ORDER_BY("red.get_date desc");
            }}.toString();
            
            
            return str;
        }
    }
}
