package cn.eeepay.framework.daoCreditMgr;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBillInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CmRepayDao {

	@SelectProvider(type = SqlProvider.class, method = "selectRepayInfo")
	@ResultType(CmBillInfo.class)
	List<CmBillInfo> selectRepayInfo(@Param("page") Page<CmBillInfo> page, @Param("info") CmBillInfo info);

	@SelectProvider(type = SqlProvider.class, method = "selectRepayInfo")
	@ResultType(CmBillInfo.class)
	List<CmBillInfo> exportRepayInfo(@Param("info") CmBillInfo info);

	@Select("SELECT cb.id,cb.order_id,cci.user_no,cb.card_no,cci.mobile_no,cci.user_name,"
			+ "cb.order_money,cb.bill_status,cb.pay_way,cb.pay_date FROM cm_bill cb "
			+ "LEFT JOIN cm_card_info cci ON cci.id = cb.ref_card_id WHERE cb.id = #{id}")
	@ResultType(CmBillInfo.class)
	CmBillInfo selectRepayInfoById(@Param("id") String id);

	public class SqlProvider {

		public String selectRepayInfo(Map<String, Object> param) {
			final CmBillInfo info = (CmBillInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cb.id,cb.order_id,cci.user_no,cb.card_no,cci.mobile_no,cci.user_name,"
							+ "cb.order_money,cb.bill_status,cb.pay_way,cb.pay_date");
					FROM("cm_bill cb");
					LEFT_OUTER_JOIN("cm_card_info cci ON cci.id = cb.ref_card_id");
					WHERE("cb.bill_status = 1");
					if (StringUtils.isNotBlank(info.getUserNo())) {
						WHERE("cci.user_no = #{info.userNo}");
					}
					if (StringUtils.isNotBlank(info.getCardNo())) {
						WHERE("cb.card_no = #{info.cardNo}");
					}
					if (StringUtils.isNotBlank(info.getPayWay())) {
						WHERE("cb.pay_way = #{info.payWay}");
					}
					if (StringUtils.isNotBlank(info.getMobileNo())) {
						WHERE("cci.mobile_no = #{info.mobileNo}");
					}
					if (StringUtils.isNotBlank(info.getUserName())) {
						WHERE("cci.user_name = #{info.userName}");
					}
					if (StringUtils.isNotBlank(info.getsPayDate())) {
						WHERE("cb.pay_date >= #{info.sPayDate}");
					}
					if (StringUtils.isNotBlank(info.getePayDate())) {
						WHERE("cb.pay_date <= #{info.ePayDate}");
					}
				}
			};
			return sql.toString();
		}

	}

}