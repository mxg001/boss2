package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CarOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CarOrderDao {

	@SelectProvider(type=SqlProvider.class,method="findCarOrder")
	@ResultType(CarOrder.class)
	List<CarOrder> findCarOrderByPage(@Param("order") CarOrder order,@Param("page") Page<CarOrder> page);
	
	@SelectProvider(type=SqlProvider.class,method="oderSum")
	@ResultType(CarOrder.class)
	CarOrder orderSum(@Param("order") CarOrder order);
	
	class SqlProvider{
        public String findCarOrder(Map<String, Object> param){
            CarOrder order = (CarOrder) param.get("order");
            
            SQL sql = new SQL();
            sql.SELECT("om.order_no,tvi.out_order_no,tvi.violation_type,tvi.score,tvi.violation_time,tvi.violation_city,tvi.car_num,om.receive_amount,om.total_bonus");
            sql.SELECT("om.price,om.org_id,oi.org_name,om.status,om.user_code,ui.user_name,ui.phone,om.create_date,om.complete_date,ui.user_code");
            sql.SELECT("ui1.user_code oneUserCode,ui1.user_type oneUserType,ui1.user_name oneUserName, ui2.user_code twoUserCode");
            sql.SELECT("ui2.user_type twoUserType,ui2.user_name twoUserName,ui3.user_code thrUserCode,ui3.user_type thrUserType,ui3.user_name thrUserName");
            sql.SELECT("ui4.user_code fouUserCode,ui4.user_type fouUserType,ui4.user_name fouUserName,om.one_user_profit oneUserProfit,om.two_user_profit twoUserProfit ");
            sql.SELECT("om.thr_user_profit thrUserProfit,om.fou_user_profit fouUserProfit,om.org_profit,om.plate_profit,om.account_status");
            sql.SELECT("ui.open_province,ui.open_city,ui.open_region,ui.remark");
            sql.SELECT( "rtod.real_plat_profit,rtod.basic_bonus_amount,rtod.bonus_amount,rtod.rate,rtod.rate_type");
            sql.SELECT( "rtbe.adjust_ratio,rtbe.receive_time,rtbe.user_code as redUserCode,redUi.user_name as redUserName");
            
            sql.FROM("order_main om");
            sql.INNER_JOIN(" traffic_violation_info tvi on om.order_no=tvi.order_no ");
            sql.LEFT_OUTER_JOIN("  red_territory_order_detail rtod on rtod.order_no  = om.order_no");
            sql.LEFT_OUTER_JOIN("  red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id");
            sql.LEFT_OUTER_JOIN("  user_info redUi on redUi.user_code  = rtbe.user_code");
            sql.LEFT_OUTER_JOIN(" user_info ui on om.user_code=ui.user_code ");
            sql.LEFT_OUTER_JOIN(" org_info oi on om.org_id=oi.org_id ");
            sql.LEFT_OUTER_JOIN(" user_info ui1 on om.one_user_code=ui1.user_code ");
            sql.LEFT_OUTER_JOIN(" user_info ui2 on om.two_user_code=ui2.user_code ");
            sql.LEFT_OUTER_JOIN(" user_info ui3 on om.thr_user_code=ui3.user_code ");
            sql.LEFT_OUTER_JOIN(" user_info ui4 on om.fou_user_code=ui4.user_code ");
            
            whereSql(sql,order);
          
            sql.ORDER_BY(" om.create_date desc ");
            
            return sql.toString();
        }
        
        public String oderSum(Map<String, Object> param){
        	CarOrder order = (CarOrder) param.get("order");
            
            SQL sql = new SQL();
            sql.SELECT("sum(org_profit) as orgProfit");
            sql.SELECT("sum(plate_profit) as plateProfit");
            sql.SELECT("sum(rtod.real_plat_profit) as actualSum");
            sql.SELECT("sum(rtod.bonus_amount) as territorySum");
            sql.FROM("order_main om");
            sql.INNER_JOIN(" traffic_violation_info tvi on om.order_no=tvi.order_no ");
            sql.LEFT_OUTER_JOIN("  red_territory_order_detail rtod on rtod.order_no  = om.order_no");
            sql.LEFT_OUTER_JOIN("  red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id");
            sql.LEFT_OUTER_JOIN("  user_info redUi on redUi.user_code  = rtbe.user_code");
            sql.LEFT_OUTER_JOIN(" user_info ui on om.user_code=ui.user_code ");
            whereSql(sql,order);
            
            return sql.toString();
        }
        
        private String whereSql(SQL sql,CarOrder order){
        	if(order != null){
                 if(StringUtils.isNotBlank(order.getOrderNo())){
                     sql.WHERE("om.order_no=#{order.orderNo}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getStatus())){
                     sql.WHERE("om.status=#{order.status}");
                 }
                 //记账状态
                 if(StringUtils.isNotBlank(order.getAccountStatus())){
                     sql.WHERE("om.account_status=#{order.accountStatus}");
                 }
                 if(StringUtils.isNotBlank(order.getOrgId()) && !"-1".equals(order.getOrgId())){
                     sql.WHERE("om.org_id=#{order.orgId}");
                 }
                 //违章类型
                 if(StringUtils.isNotBlank(order.getViolationType())){
                     sql.WHERE("tvi.violation_type=#{order.violationType}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getCarNum())){
                     sql.WHERE("tvi.car_num=#{order.carNum}");
                 }
                 //违章城市
                 if(StringUtils.isNotBlank(order.getViolationCity())){
                     sql.WHERE("tvi.violation_city=#{order.violationCity}");
                 }
                 //创建时间
                 if(StringUtils.isNotBlank(order.getCreateSTime())){
                     sql.WHERE("om.create_date >= #{order.createSTime}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getCreateETime())){
                     sql.WHERE("om.create_date < #{order.createETime}");
                 }
                 //订单完成时间
                 if(StringUtils.isNotBlank(order.getFinishSTime())){
                     sql.WHERE("om.complete_date >= #{order.finishSTime}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getFinishETime())){
                     sql.WHERE("om.complete_date < #{order.finishETime}");
                 }
                 //违章时间
                 if(StringUtils.isNotBlank(order.getViolationSTime())){
                     sql.WHERE("tvi.violation_time >= #{order.violationSTime}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getViolationETime())){
                     sql.WHERE("tvi.violation_time < #{order.violationETime}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getUserCode())){
                     sql.WHERE("om.user_code = #{order.userCode}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getUserName())){
                     sql.WHERE("ui.user_name = #{order.userName}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getPhone())){
                     sql.WHERE("ui.phone = #{order.phone}");
                 }
                 //一级编号、一级名称、一级身份
                 if(StringUtils.isNotBlank(order.getOneUserCode())){
                     sql.WHERE("om.one_user_code = #{order.oneUserCode}");
                 }
                 if(StringUtils.isNotBlank(order.getOneUserName())){
                     sql.WHERE("om.one_user_nme = #{order.oneUserName}");
                 }
                 if(StringUtils.isNotBlank(order.getOneUserType())){
                     sql.WHERE("om.one_user_type = #{order.oneUserType}");
                 }
                 //2级编号、2级名称、2级身份
                 if(StringUtils.isNotBlank(order.getTwoUserCode())){
                     sql.WHERE("om.two_user_code = #{order.twoUserCode}");
                 }
                 if(StringUtils.isNotBlank(order.getTwoUserName())){
                     sql.WHERE("om.two_user_nme = #{order.twoUserNme}");
                 }
                 if(StringUtils.isNotBlank(order.getTwoUserType())){
                     sql.WHERE("om.two_user_type = #{order.twoUserType}");
                 }
                 //3级编号、3级名称、3级身份
                 if(StringUtils.isNotBlank(order.getThrUserCode())){
                     sql.WHERE("om.thr_user_code = #{order.thrUserCode}");
                 }
                 if(StringUtils.isNotBlank(order.getThrUserName())){
                     sql.WHERE("om.thr_user_nme = #{order.thrUserNme}");
                 }
                 if(StringUtils.isNotBlank(order.getThrUserType())){
                     sql.WHERE("om.thr_user_type = #{order.thrUserType}");
                 }
                 //4级编号、4级名称、4级身份
                 if(StringUtils.isNotBlank(order.getFouUserCode())){
                     sql.WHERE("om.fou_user_code = #{order.fouUserCode}");
                 }
                 if(StringUtils.isNotBlank(order.getFouUserName())){
                     sql.WHERE("om.fou_user_nme = #{order.fouUserNme}");
                 }
                 if(StringUtils.isNotBlank(order.getFouUserType())){
                     sql.WHERE("om.fou_user_type = #{order.fouUserType}");
                 }
                 
                 if(!"全部".equals(order.getOpenProvince())&&StringUtils.isNotBlank(order.getOpenProvince())){
                     sql.WHERE("ui.open_province = #{order.openProvince}");
                 }
                 
                 if(!"全部".equals(order.getOpenCity())&&StringUtils.isNotBlank(order.getOpenCity())){
                     sql.WHERE("ui.open_city = #{order.openCity}");
                 }
                 
                 if(!"全部".equals(order.getOpenRegion())&&StringUtils.isNotBlank(order.getOpenRegion())){
                     sql.WHERE("ui.open_region = #{order.openRegion}");
                 }
                 
                 if(StringUtils.isNotBlank(order.getRemark())){
                     sql.WHERE("ui.remark = #{order.remark}");
                 }
             }
        	return sql.toString();
        }
    }
	
	  /**根据orderNo获取违章订单详情*/
    @Select("select  om.order_no,tvi.violation_type,tvi.score,tvi.violation_time,tvi.violation_city,tvi.car_num,om.receive_amount,"
			+"om.price,om.org_id,oi.org_name,om.status,om.user_code,ui.user_name,ui.phone,om.create_date,om.complete_date,ui.user_code,"
			+"ui1.user_code oneUserCode,ui1.user_type oneUserType,ui1.user_name oneUserName, ui2.user_code twoUserCode,"
			+"ui2.user_type twoUserType,ui2.user_name twoUserName,ui3.user_code thrUserCode,ui3.user_type thrUserType,ui3.user_name thrUserName,"
			+"ui4.user_code fouUserCode,ui4.user_type fouUserType,ui4.user_name fouUserName,om.one_user_profit oneUserProfit,om.two_user_profit twoUserProfit,"
			+"om.thr_user_profit thrUserProfit,om.fou_user_profit fouUserProfit,om.org_profit,om.plate_profit,om.account_status,"
			+"ui.open_province,ui.open_city,ui.open_region,ui.remark,tvi.order_id_card_no,"+
            " rtod.real_plat_profit,rtod.basic_bonus_amount,rtod.bonus_amount,rtbe.adjust_ratio,rtod.rate,rtod.rate_type,"+
            " rtbe.user_code as redUserCode,redUi.user_name as redUserName ,rtbe.territory_price,rtbe.territory_avg_price,rtbe.receive_time "+
            "from order_main om "+
            "  LEFT OUTER JOIN red_territory_order_detail rtod on rtod.order_no  = om.order_no"+
            "  LEFT OUTER JOIN red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id "+
            "  LEFT OUTER JOIN user_info redUi on redUi.user_code  = rtbe.user_code "
			+"inner JOIN traffic_violation_info tvi on om.order_no=tvi.order_no "
			+"LEFT OUTER JOIN user_info ui on om.user_code=ui.user_code "
			+"LEFT OUTER JOIN org_info oi on om.org_id=oi.org_id "
			+"LEFT OUTER JOIN user_info ui1 on om.one_user_code=ui1.user_code "
			+"LEFT OUTER JOIN user_info ui2 on om.two_user_code=ui2.user_code "
			+"LEFT OUTER JOIN user_info ui3 on om.thr_user_code=ui3.user_code "
			+"LEFT OUTER JOIN user_info ui4 on om.fou_user_code=ui4.user_code "
			+"where om.order_no = #{orderNo} ")
    @ResultType(CarOrder.class)
    CarOrder carOrderDetail(@Param("orderNo") String orderNo);
}
