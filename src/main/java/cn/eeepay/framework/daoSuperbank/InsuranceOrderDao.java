package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.OrderMainSum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface InsuranceOrderDao {

    @SelectProvider(type = InsuranceOrderDao.SqlProvider.class, method = "selectOrderPage")
    @ResultType(OrderMain.class)
    List<OrderMain> selectOrderPage(@Param("baseInfo") OrderMain baseInfo, @Param("page") Page<OrderMain> page);

    @SelectProvider(type = InsuranceOrderDao.SqlProvider.class, method = "selectOrderSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectOrderSum(@Param("baseInfo") OrderMain baseInfo);

    @SelectProvider(type = SqlProvider.class, method = "selectInsuranceOrderDetail")
    @ResultType(OrderMain.class)
    OrderMain selectInsuranceOrderDetail(String orderNo);

    class SqlProvider {
        public String selectOrderPage(Map<String, Object> param) {
            final OrderMain baseInfo = (OrderMain) param.get("baseInfo");
            SQL sql = new SQL() {{
                SELECT("om.order_no,om.org_id,om.org_name,om.order_type,om.user_code");
                SELECT("om.status,om.create_date,om.total_bonus,om.share_user_code");
                SELECT("om.one_user_code,om.one_user_type,om.one_user_profit,om.org_profit");
                SELECT("om.plate_profit,om.account_status,om.pay_method,om.pay_date");
                SELECT("om.pay_channel,om.pay_order_no,om.pay_channel_no,om.price");
                SELECT("om.two_user_code,om.two_user_type,om.two_user_profit");
                SELECT("om.thr_user_code,om.thr_user_type,om.thr_user_profit");
                SELECT("om.fou_user_code,om.fou_user_type,om.fou_user_profit");
                SELECT("om.bank_source_id,om.bank_name,om.order_name,om.order_phone,om.order_id_no");
                SELECT("om.loan_source_id,om.loan_name,om.loan_amount,om.loan_push_pro");
                SELECT("om.receive_agent_id,om.receive_amount,om.repayment_agent_id,om.repayment_amount");
                SELECT("om.creditcard_bank_bonus,om.loan_bank_rate,om.loan_org_rate,om.loan_org_bonus,om.trans_rate");
                SELECT("om.refund_status,om.refund_date,om.refund_msg,om.complete_date");
                SELECT("om.repay_trans_channel,om.repay_trans_card_no,om.repay_trans_status");
                SELECT("om.repay_transfee,om.repay_transfee_add");
                SELECT("om.creditcard_bank_bonus2,om.total_bonus2");
                SELECT("om.one_user_profit2,om.two_user_profit2,om.thr_user_profit2,om.fou_user_profit2");
                SELECT("om.org_profit2,om.plate_profit2,om.account_status2,om.profit_status2");
                SELECT("om.pay_date2,om.batch_no,om.batch_no2,om.loan_type,om.profit_type,om.bxzong_amount");
                SELECT("ui0.user_name,ui0.phone as shareUserPhone");
                SELECT("ui0.open_province,ui0.open_city,ui0.open_region,ui0.remark");
                SELECT("ui1.user_name as oneUserName,ui2.user_name as twoUserName");
                SELECT("ui3.user_name as thrUserName,ui4.user_name as fouUserName");
                SELECT("ic.company_nick_name,ip.product_type,ip.product_name,ip.bonus_settle_time");
                SELECT( "rtod.real_plat_profit,rtod.basic_bonus_amount,rtod.bonus_amount,rtod.rate,rtod.rate_type");
                SELECT( "rtbe.adjust_ratio,rtbe.user_code as redUserCode,redUi.user_name as redUserName");

            }};
            whereSql(sql, baseInfo);
            sql.ORDER_BY(" om.create_date desc");
            return sql.toString();
        }

        public String selectOrderSum(Map<String, Object> param) {
            final OrderMain baseInfo = (OrderMain) param.get("baseInfo");
            SQL sql = new SQL() {{
                SELECT("sum(om.total_bonus) as totalBonusSum");
                SELECT("sum(om.plate_profit) as plateProfitSum");
                SELECT("sum(om.org_profit) as orgProfitSum");
                SELECT("sum(rtod.real_plat_profit) as actualSum");
                SELECT("sum(rtod.bonus_amount) as territorySum");
                SELECT("count(1) as countSum");
            }};
            whereSql(sql, baseInfo);
            return sql.toString();
        }

        public void whereSql(SQL sql, OrderMain baseInfo) {
            sql.FROM("order_main om");
            sql.LEFT_OUTER_JOIN("  red_territory_order_detail rtod on rtod.order_no  = om.order_no");
            sql.LEFT_OUTER_JOIN("  red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id");
            sql.LEFT_OUTER_JOIN("  user_info redUi on redUi.user_code  = rtbe.user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui0 on ui0.user_code = om.user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui1 on ui1.user_code = om.one_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui2 on ui2.user_code = om.two_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui3 on ui3.user_code = om.thr_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui4 on ui4.user_code = om.fou_user_code");
            sql.LEFT_OUTER_JOIN("  insurance_product ip on om.upper_product_id = ip.upper_product_id");
            sql.LEFT_OUTER_JOIN("  insurance_company ic on ip.company_no = ic.company_no");
            if (StringUtils.isNotBlank(baseInfo.getOrderNo())) {
                sql.WHERE("om.order_no = #{baseInfo.orderNo}");
            }
            if (StringUtils.isNotBlank(baseInfo.getStatus())) {
                sql.WHERE("om.status = #{baseInfo.status}");
            }
            if (baseInfo.getCompanyNo() != null && baseInfo.getCompanyNo() != -1) {
                sql.WHERE("ip.company_no = #{baseInfo.companyNo}");
            }
            if (baseInfo.getOrgId() != null && -1 != baseInfo.getOrgId()) {
                sql.WHERE("om.org_id = #{baseInfo.orgId}");
            }
            if (StringUtils.isNotBlank(baseInfo.getProductType())) {
                sql.WHERE("ip.product_type = #{baseInfo.productType}");
            }
            if (baseInfo.getProductId() != null) {
                sql.WHERE("ip.product_id = #{baseInfo.productId}");
            }
            if (StringUtils.isNotBlank(baseInfo.getInsuranceName())) {
                sql.WHERE("om.insurance_name = #{baseInfo.InsuranceName}");
            }
            if (StringUtils.isNotBlank(baseInfo.getInsurancePhone())) {
                sql.WHERE("om.insurance_phone = #{baseInfo.insurancePhone}");
            }
            if (StringUtils.isNotBlank(baseInfo.getInsuranceIdNo())) {
                sql.WHERE("om.insurance_id_no = #{baseInfo.insuranceIdNo}");
            }
            if (StringUtils.isNotBlank(baseInfo.getCreateDateStart())) {
                sql.WHERE("om.create_date >= #{baseInfo.createDateStart}");
            }
            if (StringUtils.isNotBlank(baseInfo.getCreateDateEnd())) {
                sql.WHERE("om.create_date <= #{baseInfo.createDateEnd}");
            }
            if (StringUtils.isNotBlank(baseInfo.getUserName())) {
                sql.WHERE("om.user_name = #{baseInfo.userName}");
            }
            if (StringUtils.isNotBlank(baseInfo.getUserCode())) {
                sql.WHERE("om.user_code = #{baseInfo.userCode}");
            }
            if (StringUtils.isNotBlank(baseInfo.getShareUserPhone())) {
                sql.WHERE("om.share_user_phone = #{baseInfo.shareUserPhone}");
            }
            if (StringUtils.isNotBlank(baseInfo.getShareUserPhone())) {
                sql.WHERE("ui0.phone = #{baseInfo.shareUserPhone}");
            }
            if (StringUtils.isNotBlank(baseInfo.getUserName())) {
                sql.WHERE("ui0.user_name = #{baseInfo.userName}");
            }
            if (StringUtils.isNotBlank(baseInfo.getOneUserCode())) {
                sql.WHERE("om.one_user_code = #{baseInfo.oneUserCode}");
            }
            if (StringUtils.isNotBlank(baseInfo.getOneUserName())) {
                sql.WHERE("ui1.user_name = #{baseInfo.oneUserName}");
            }
            if (StringUtils.isNotBlank(baseInfo.getOneUserType())) {
                sql.WHERE("om.one_user_type = #{baseInfo.oneUserType}");
            }
            if (StringUtils.isNotBlank(baseInfo.getTwoUserCode())) {
                sql.WHERE("om.two_user_code = #{baseInfo.twoUserCode}");
            }
            if (StringUtils.isNotBlank(baseInfo.getTwoUserName())) {
                sql.WHERE("ui2.user_name = #{baseInfo.twoUserName}");
            }
            if (StringUtils.isNotBlank(baseInfo.getTwoUserType())) {
                sql.WHERE("om.two_user_type = #{baseInfo.twoUserType}");
            }
            if (StringUtils.isNotBlank(baseInfo.getThrUserCode())) {
                sql.WHERE("om.thr_user_code = #{baseInfo.thrUserCode}");
            }
            if (StringUtils.isNotBlank(baseInfo.getThrUserName())) {
                sql.WHERE("ui3.user_name = #{baseInfo.thrUserName}");
            }
            if (StringUtils.isNotBlank(baseInfo.getThrUserType())) {
                sql.WHERE("om.thr_user_type = #{baseInfo.thrUserType}");
            }
            if (StringUtils.isNotBlank(baseInfo.getFouUserCode())) {
                sql.WHERE("om.fou_user_code = #{baseInfo.fouUserCode}");
            }
            if (StringUtils.isNotBlank(baseInfo.getFouUserName())) {
                sql.WHERE("ui4.user_name = #{baseInfo.fouUserName}");
            }
            if (StringUtils.isNotBlank(baseInfo.getFouUserType())) {
                sql.WHERE("om.fou_user_type = #{baseInfo.fouUserType}");
            }
            if (StringUtils.isNotBlank(baseInfo.getRemark())) {
                baseInfo.setRemark(baseInfo.getRemark() + "%");
                sql.WHERE("ui0.remark like #{baseInfo.remark}");
            }
            if (StringUtils.isNotBlank(baseInfo.getOpenProvince()) && !"全部".equals(baseInfo.getOpenProvince())) {
                sql.WHERE("ui0.open_province = #{baseInfo.openProvince}");
            }
            if (StringUtils.isNotBlank(baseInfo.getOpenCity()) && !"全部".equals(baseInfo.getOpenCity())) {
                sql.WHERE("ui0.open_city = #{baseInfo.openCity}");
            }
            if (StringUtils.isNotBlank(baseInfo.getOpenRegion()) && !"全部".equals(baseInfo.getOpenRegion())) {
                sql.WHERE("ui0.open_region = #{baseInfo.openRegion}");
            }
            sql.WHERE("om.order_type = 15");
        }

        public String selectInsuranceOrderDetail(String orderNo) {
            SQL sql = new SQL() {{
                SELECT("om.order_no,om.org_id,om.org_name,om.order_type,om.user_code");
                SELECT("om.status,om.create_date,om.total_bonus,om.share_user_code");
                SELECT("om.one_user_code,om.one_user_type,om.one_user_profit,om.org_profit");
                SELECT("om.plate_profit,om.account_status,om.pay_method,om.pay_date");
                SELECT("om.pay_channel,om.pay_order_no,om.pay_channel_no,om.price");
                SELECT("om.two_user_code,om.two_user_type,om.two_user_profit");
                SELECT("om.thr_user_code,om.thr_user_type,om.thr_user_profit");
                SELECT("om.fou_user_code,om.fou_user_type,om.fou_user_profit");
                SELECT("om.bank_source_id,om.bank_name,om.order_name,om.order_phone,om.order_id_no");
                SELECT("om.loan_source_id,om.loan_name,om.loan_amount,om.loan_push_pro");
                SELECT("om.receive_agent_id,om.receive_amount,om.repayment_agent_id,om.repayment_amount");
                SELECT("om.creditcard_bank_bonus,om.loan_bank_rate,om.loan_org_rate,om.loan_org_bonus,om.trans_rate");
                SELECT("om.refund_status,om.refund_date,om.refund_msg,om.complete_date");
                SELECT("om.repay_trans_channel,om.repay_trans_card_no,om.repay_trans_status");
                SELECT("om.repay_transfee,om.repay_transfee_add");
                SELECT("om.creditcard_bank_bonus2,om.total_bonus2");
                SELECT("om.one_user_profit2,om.two_user_profit2,om.thr_user_profit2,om.fou_user_profit2");
                SELECT("om.org_profit2,om.plate_profit2,om.account_status2,om.profit_status2");
                SELECT("om.pay_date2,om.batch_no,om.batch_no2,om.loan_type,om.profit_type,om.bxzong_amount");
                SELECT("ui0.user_name,ui0.phone as shareUserPhone");
                SELECT("ui0.open_province,ui0.open_city,ui0.open_region,ui0.remark");
                SELECT("ui1.user_name as oneUserName,ui2.user_name as twoUserName");
                SELECT("ui3.user_name as thrUserName,ui4.user_name as fouUserName");
                SELECT("ic.company_nick_name,ip.product_type,ip.product_name");
                SELECT("  rtod.real_plat_profit,rtod.basic_bonus_amount,rtod.bonus_amount,rtbe.adjust_ratio");
                SELECT("  rtbe.user_code as redUserCode,redUi.user_name as redUserName ,rtbe.territory_price,rtbe.territory_avg_price,rtbe.receive_time ");
                FROM("order_main om ");
                LEFT_OUTER_JOIN("  red_territory_order_detail rtod on rtod.order_no  = om.order_no");
                LEFT_OUTER_JOIN("   red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id ");
                LEFT_OUTER_JOIN("   user_info redUi on redUi.user_code  = rtbe.user_code ");
                LEFT_OUTER_JOIN("  user_info ui0 on ui0.user_code = om.user_code");
                LEFT_OUTER_JOIN("  user_info ui1 on ui1.user_code = om.one_user_code");
                LEFT_OUTER_JOIN("  user_info ui2 on ui2.user_code = om.two_user_code");
                LEFT_OUTER_JOIN("  user_info ui3 on ui3.user_code = om.thr_user_code");
                LEFT_OUTER_JOIN("  user_info ui4 on ui4.user_code = om.fou_user_code");
                LEFT_OUTER_JOIN("  insurance_product ip on om.upper_product_id = ip.upper_product_id");
                LEFT_OUTER_JOIN("  insurance_company ic on ip.company_no = ic.company_no");

                WHERE("om.order_no = #{orderNo}");
            }};
            return sql.toString();
        }
    }
}
