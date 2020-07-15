package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 商户购买订单 数据层
 * 
 * @author tans
 * @date 2019-05-30
 */
public interface CjtOrderDao {
	/**
     * 查询商户购买订单信息
     * 
     * @param orderNo
     * @return 商户购买订单信息
     */
	@Select("select * from cjt_order where order_no = #{orderNo}")
	@ResultType(CjtOrder.class)
	CjtOrder selectDetail(String orderNo);
	
	/**
     * 查询商户购买订单列表
     * 
     * @param baseInfo 商户购买订单信息
     * @return 商户购买订单集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectPage")
	@ResultType(CjtOrder.class)
	List<CjtOrder> selectPage(@Param("page") Page<CjtOrder> page,
                                             @Param("baseInfo") CjtOrder baseInfo);

	@SelectProvider(type = SqlProvider.class, method = "selectTotal")
	@ResultType(Map.class)
	Map<String, Object> selectTotal(@Param("baseInfo") CjtOrder baseInfo);

	@Update("update cjt_order set order_status = #{orderStatus},logistics_company=#{logisticsCompany}," +
			" logistics_order_no=#{logisticsOrderNo},logistics_time=#{logisticsTime},logistics_operator=#{logisticsOperator}" +
			" where order_no = #{orderNo}")
    int updateOrderStatus(CjtOrder baseInfo);

	@Select("select co.*," +
			" cto.order_no as transOrderNo,cto.trans_status,cto.trans_time,cto.trans_type,cto.acq_code,cto.acq_order_no as acqOrderNo, co.good_order_type," +
			" cas.status " +
			" from cjt_order co" +
			" left join cjt_trans_order cto on cto.service_order_no = co.order_no and cto.service = 'goodsOrder'" +
			" left join cjt_after_sale cas on cas.order_no = co.last_after_sale_no " +
			" where co.order_no = #{orderNo} ")
	CjtOrder selectOrderDetail(String orderNo);

	class SqlProvider{
		public String selectPage(Map<String, Object> param){
			CjtOrder baseInfo = (CjtOrder) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("co.*");
			sql.SELECT("cto.trans_status, cto.trans_time,cto.order_no payOrderNo, cto.acq_order_no acqOrderNo, co.good_order_type ");
			sql.SELECT("cas.status");
			fromSql(sql);
			whereSql(sql, baseInfo);
			sql.ORDER_BY("co.id desc");
			return sql.toString();
		}

		public String selectTotal(Map<String, Object> param){
			CjtOrder baseInfo = (CjtOrder) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("sum(if(co.order_status='2',1,0)) as shippedTotal");//已发货总笔数
			sql.SELECT("sum(if(co.order_status='2',co.total_price,0)) as shippedTotalPrice");//已发货总金额
			sql.SELECT("sum(if(co.order_status='1',1,0)) as notShippedTotal");//待发货总笔数
			sql.SELECT("sum(if(co.order_status='1',co.total_price,0)) as notShippedTotalPrice");//待发货总金额
			sql.SELECT("sum(if(co.order_status='2',co.num,0)) as shippedTotalNum");//已发货总台数
			sql.SELECT("sum(if(cto.trans_status='2',1,0)) as transTotalNum");//支付成功总笔数
			sql.SELECT("sum(if(cto.trans_status='2',cto.trans_amount,0)) as transTotalAmount");//支付成功总金额

			fromSql(sql);
			whereSql(sql, baseInfo);
			return sql.toString();
		}

		private void fromSql(SQL sql) {
			sql.FROM("cjt_order co");
			sql.LEFT_OUTER_JOIN("cjt_trans_order cto on cto.service_order_no = co.order_no and cto.service='goodsOrder'");
			sql.LEFT_OUTER_JOIN("cjt_after_sale cas on cas.order_no = co.last_after_sale_no ");
		}

		private void whereSql(SQL sql, CjtOrder baseInfo) {
			if(StringUtils.isNotEmpty(baseInfo.getOrderNo())) {
				sql.WHERE("co.order_no = #{baseInfo.orderNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getMerchantNo())) {
				sql.WHERE("co.merchant_no = #{baseInfo.merchantNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getOrderStatus())) {
				sql.WHERE("co.order_status = #{baseInfo.orderStatus}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getTransType())) {
				sql.WHERE("cto.trans_type = #{baseInfo.transType}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getTransOrderNo())) {
				sql.WHERE("cto.order_no = #{baseInfo.transOrderNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getAcqOrderNo())) {
				sql.WHERE("cto.acq_order_no = #{baseInfo.acqOrderNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getTransStatus())) {
				sql.WHERE("cto.trans_status = #{baseInfo.transStatus}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getStatus())) {
				sql.WHERE("cas.status = #{baseInfo.status}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getAcqCode())) {
				sql.WHERE("cto.acq_code = #{baseInfo.acqCode}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeStart())){
				sql.WHERE("co.create_time >= #{baseInfo.createTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeEnd())){
				sql.WHERE("co.create_time <= #{baseInfo.createTimeEnd}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getTransTimeStart())){
				sql.WHERE("cto.trans_time >= #{baseInfo.transTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getTransTimeEnd())){
				sql.WHERE("cto.trans_time <= #{baseInfo.transTimeEnd}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getLogisticsTimeStart())){
				sql.WHERE("co.logistics_time >= #{baseInfo.logisticsTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getLogisticsTimeEnd())){
				sql.WHERE("co.logistics_time <= #{baseInfo.logisticsTimeEnd}");
			}
			if(baseInfo.getGoodOrderType() != null){
				sql.WHERE("co.good_order_type = #{baseInfo.goodOrderType}");
			}

		}
	}

	
}