package cn.eeepay.framework.daoCreditMgr;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBillDetail;
import cn.eeepay.framework.model.CmBillInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CmBillDao {

	/**
	 * 账单列表查询
	 * @author mays
	 * @date 2018年4月9日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectBillInfo")
	@ResultType(CmBillInfo.class)
	List<CmBillInfo> selectBillInfo(@Param("page") Page<CmBillInfo> page, @Param("info") CmBillInfo info);

	@SelectProvider(type = SqlProvider.class, method = "selectBillInfo")
	@ResultType(CmBillInfo.class)
	List<CmBillInfo> exportBillInfo(@Param("info") CmBillInfo info);

	/**
	 * 账单明细查询
	 * @author mays
	 * @date 2018年4月9日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectBillDetail")
	@ResultType(CmBillDetail.class)
	List<CmBillDetail> selectBillDetail(@Param("page") Page<CmBillDetail> page, @Param("info") CmBillDetail info);

	@SelectProvider(type = SqlProvider.class, method = "selectBillDetail")
	@ResultType(CmBillDetail.class)
	List<CmBillDetail> exportBillDetailInfo(@Param("info") CmBillDetail info);


	/**
	 * 查询评测概况
	 * @author	mays
	 * @date	2018年4月24日
	 */
	@Select("select * from (select reviews_id,review_result from cm_reviews_result "
			+ "where bill_id = #{billId} order by id desc ) as a group by a.reviews_id")
	List<Map<String, Integer>> queryReviewsReport(@Param("billId") String billId);

	/**
	 * 交易笔数
	 * @author	mays
	 * @date	2018年4月24日
	 */
	@Select("select count(0) from cm_bill_detail where trans_amt > 0 and ref_bill_id = #{billId}")
	Integer queryTransCount(@Param("billId") String billId);

	/**
	 * 商户个数
	 * @author	mays
	 * @date	2018年4月24日
	 */
	@Select("select count(0) from ( select count(0) from cm_bill_detail where trans_amt > 0 and ref_bill_id = #{billId} group by trans_desc ) t")
	Integer queryMerCount(@Param("billId") String billId);

	/**
	 * 交易金额分段统计
	 * @author	mays
	 * @date	2018年4月24日
	 */
	@Select("select sum(trans_amt > 0 and trans_amt <= 50000) t1,"
			+ "sum(trans_amt > 50000 and trans_amt <= 200000) t2,"
			+ "sum(trans_amt > 200000 and trans_amt <= 500000) t3,"
			+ "sum(trans_amt > 500000 and trans_amt <= 1000000) t4,"
			+ "sum(trans_amt > 1000000 and trans_amt <= 5000000) t5 "
			+ "from cm_bill_detail where ref_bill_id = #{billId}")
	Map<String, String> queryTransPartCount(@Param("billId") String billId);

	/**
	 * 交易时间分段统计
	 * @author	mays
	 * @date	2018年4月24日
	 */
	@Select("select IFNULL(sum(DATE_FORMAT(cbd.trans_date, '%H:%i') >= '00:00' and DATE_FORMAT(cbd.trans_date, '%H:%i') <= '04:00'), 0) t1,"
			+ "IFNULL(sum(DATE_FORMAT(cbd.trans_date, '%H:%i') > '04:00' and DATE_FORMAT(cbd.trans_date, '%H:%i') <= '08:00'), 0) t2,"
			+ "IFNULL(sum(DATE_FORMAT(cbd.trans_date, '%H:%i') > '08:00' and DATE_FORMAT(cbd.trans_date, '%H:%i') <= '12:00'), 0) t3,"
			+ "IFNULL(sum(DATE_FORMAT(cbd.trans_date, '%H:%i') > '12:00' and DATE_FORMAT(cbd.trans_date, '%H:%i') <= '16:00'), 0) t4,"
			+ "IFNULL(sum(DATE_FORMAT(cbd.trans_date, '%H:%i') > '16:00' and DATE_FORMAT(cbd.trans_date, '%H:%i') <= '20:00'), 0) t5,"
			+ "IFNULL(sum(DATE_FORMAT(cbd.trans_date, '%H:%i') > '20:00' and DATE_FORMAT(cbd.trans_date, '%H:%i') < '24:00'), 0) t6 "
			+ "from cm_bill_detail cbd join cm_card_info cci on cci.id = cbd.ref_card_id and cci.bank_name = '中国招商银行' "
			+ "where cbd.trans_amt > 0 and cbd.ref_bill_id = #{billId}")
	Map<String, String> queryTimePartCount(@Param("billId") String billId);

	public class SqlProvider {

		public String selectBillInfo(Map<String, Object> param) {
			final CmBillInfo info = (CmBillInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cb.*,cci.user_no,cci.bank_name,cci.user_name,cci.statement_date,cci.repayment_date");
					FROM("cm_bill cb");
					LEFT_OUTER_JOIN("cm_card_info cci ON cci.id = cb.ref_card_id");
					if (StringUtils.isNotBlank(info.getUserNo())) {
						WHERE("cci.user_no = #{info.userNo}");
					}
					if (StringUtils.isNotBlank(info.getCardNo())) {
						WHERE("cb.card_no = #{info.cardNo}");
					}
					if (info.getMethod() != null) {
						WHERE("cb.method = #{info.method}");
					}
					ORDER_BY("cb.create_time desc");
				}
			};
			return sql.toString();
		}

		public String selectBillDetail(Map<String, Object> param) {
			final CmBillDetail info = (CmBillDetail) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cbd.*,cb.card_no,cci.user_no");
					FROM("cm_bill_detail cbd");
					LEFT_OUTER_JOIN("cm_bill cb ON cb.id = cbd.ref_bill_id");
					LEFT_OUTER_JOIN("cm_card_info cci ON cci.id = cb.ref_card_id");
					WHERE("cbd.ref_bill_id = #{info.refBillId}");
					if ("0".equals(info.getTransType())) { //0--消费，1--还款
						WHERE("cbd.trans_amt > 0");
					} else if ("1".equals(info.getTransType())) { //0--消费，1--还款
						WHERE("cbd.trans_amt < 0");
					}
					if (StringUtils.isNotBlank(info.getsTransDate())) {
						WHERE("cbd.trans_date >= #{info.sTransDate}");
					}
					if (StringUtils.isNotBlank(info.geteTransDate())) {
						WHERE("cbd.trans_date <= #{info.eTransDate}");
					}
				}
			};
			return sql.toString();
		}

	}

}