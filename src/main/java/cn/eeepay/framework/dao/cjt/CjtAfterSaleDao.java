package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtAfterSale;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 售后订单 数据层
 * 
 * @author tans
 * @date 2019-06-06
 */
public interface CjtAfterSaleDao {
	/**
     * 查询售后订单信息
     * 
     * @param id 售后订单ID
     * @return 售后订单信息
     */
	public CjtAfterSale selectCjtAfterSaleById(Integer id);
	
	/**
     * 查询售后订单列表
     * 
     * @param baseInfo 售后订单信息
     * @return 售后订单集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectPage")
	@ResultType(CjtAfterSale.class)
	List<CjtAfterSale> selectPage(@Param("page") Page<CjtAfterSale> page,
								  @Param("baseInfo") CjtAfterSale baseInfo);

	@SelectProvider(type = SqlProvider.class, method = "selectTotal")
	@ResultType(Map.class)
	Map<String, Object> selectTotal(@Param("baseInfo") CjtAfterSale baseInfo);

	@Update("update cjt_after_sale set status = #{status} where order_no = #{orderNo} and status = '0'")
	int updateStatus(@Param("orderNo") String orderNo, @Param("status") String status);

	@Update("update cjt_after_sale set status = #{status}, deal_remark = #{dealRemark}, deal_img = #{dealImg}," +
			" deal_person = #{dealPerson}, deal_time = #{dealTime}" +
			" where order_no = #{orderNo} ")
	int update(CjtAfterSale baseInfo);

	@Select("select * from cjt_after_sale where order_no = #{orderNo}")
	@ResultType(CjtAfterSale.class)
	CjtAfterSale select(@Param("orderNo") String orderNo);

	class SqlProvider {
		public String selectPage(Map<String, Object> param) {
			CjtAfterSale baseInfo = (CjtAfterSale) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("cas.*");
			fromSql(sql);
			whereSql(baseInfo, sql);
			sql.ORDER_BY("cas.create_time desc");
			return sql.toString();
		}

		public String selectTotal(Map<String, Object> param) {
			CjtAfterSale baseInfo = (CjtAfterSale) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("sum(if(cas.status=0,1,0)) as noDealNum");
			sql.SELECT("sum(if(cas.status=0&&cas.create_time<=#{baseInfo.noDealTimeStr},1,0)) as sevenDaysNoDealNum");
			fromSql(sql);
			whereSql(baseInfo, sql);
			return sql.toString();
		}

		private void whereSql(CjtAfterSale baseInfo, SQL sql) {
			if(StringUtils.isNotEmpty(baseInfo.getOrderNo())) {
				sql.WHERE("cas.order_no = #{baseInfo.orderNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getServiceOrderNo())) {
				sql.WHERE("cas.service_order_no = #{baseInfo.serviceOrderNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getAfterSaleType())) {
				sql.WHERE("cas.after_sale_type = #{baseInfo.afterSaleType}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getStatus())) {
				sql.WHERE("cas.status = #{baseInfo.status}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeStart())) {
				sql.WHERE("cas.create_time >= #{baseInfo.createTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeEnd())) {
				sql.WHERE("cas.create_time <= #{baseInfo.createTimeEnd}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getDealTimeStart())) {
				sql.WHERE("cas.deal_time >= #{baseInfo.dealTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getDealTimeEnd())) {
				sql.WHERE("cas.deal_time <= #{baseInfo.dealTimeEnd}");
			}
		}

		private void fromSql(SQL sql) {
			sql.FROM("cjt_after_sale cas");
		}
	}
}











