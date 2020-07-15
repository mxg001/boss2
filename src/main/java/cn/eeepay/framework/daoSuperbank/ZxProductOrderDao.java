package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.ZxProductOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface ZxProductOrderDao {
    @SelectProvider(type= ZxProductOrderDao.SqlProvider.class, method="selectByPage")
    @ResultType(ZxProductOrder.class)
    List<ZxProductOrder> selectByPage(@Param("record") ZxProductOrder record, Page<ZxProductOrder> page);

    @SelectProvider(type= ZxProductOrderDao.SqlProvider.class, method="selectOrderSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectOrderSum(@Param("record") ZxProductOrder record);

    @Select(" SELECT  zpo.* ,om.org_name, om.one_user_code,om.total_bonus,om.account_status,om.zx_cost_price,om.plate_profit,om.one_user_type,om.one_user_profit,om.org_profit,om.two_user_code,om.two_user_type,om.two_user_profit," +
            "om.thr_user_code,om.thr_user_type,om.thr_user_profit,om.fou_user_code,om.fou_user_type,om.fou_user_profit,ui0.user_name,ui0.phone AS shareUserPhone,ui0.open_province,ui0.open_city,ui0.open_region,ui0.remark," +
            "ui1.user_name AS oneUserName,ui2.user_name AS twoUserName,ui3.user_name AS thrUserName,ui4.user_name AS fouUserName ,rtod.real_plat_profit,rtod.basic_bonus_amount,rtod.bonus_amount,rtbe.adjust_ratio," +
            "rtbe.user_code as redUserCode,redUi.user_name as redUserName ,rtbe.territory_price,rtbe.territory_avg_price,rtbe.receive_time,rtod.rate,rtod.rate_type " +
            "FROM zx_product_order zpo " +
            "LEFT JOIN  order_main om ON zpo.yhj_order_no  = om.order_no " +
            "LEFT JOIN user_info ui0 ON ui0.user_code = om.user_code " +
            "LEFT JOIN user_info ui1 ON ui1.user_code = om.one_user_code " +
            "LEFT JOIN user_info ui2 ON ui2.user_code = om.two_user_code " +
            "LEFT JOIN  user_info ui3 ON ui3.user_code = om.thr_user_code " +
            "LEFT JOIN red_territory_order_detail rtod on rtod.order_no  = om.order_no " +
            "LEFT JOIN red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id " +
            "LEFT JOIN  user_info redUi on redUi.user_code  = rtbe.user_code " +
            "LEFT JOIN  user_info ui4 ON ui4.user_code = om.fou_user_code where zpo.order_no = #{orderNo} ")
    @ResultType(ZxProductOrder.class)
    ZxProductOrder selectByOrderNo(String orderNo);
    public class SqlProvider {
        public String selectByPage(Map<String, Object> param) {
            final ZxProductOrder record = (ZxProductOrder) param.get("record");

            SQL sql = new SQL(){{
                SELECT("zpo.*");
                SELECT("om.org_id,om.org_name,om.one_user_code,om.one_user_type,om.one_user_profit,om.org_profit");
                SELECT("om.total_bonus,om.account_status,om.zx_cost_price,om.plate_profit");
                SELECT("om.two_user_code,om.two_user_type,om.two_user_profit");
                SELECT("om.thr_user_code,om.thr_user_type,om.thr_user_profit");
                SELECT("om.fou_user_code,om.fou_user_type,om.fou_user_profit");
                SELECT("ui0.user_name,ui0.phone as shareUserPhone");
                SELECT("ui0.open_province,ui0.open_city,ui0.open_region,ui0.remark");
                SELECT("ui1.user_name as oneUserName,ui2.user_name as twoUserName");
                SELECT( "ui3.user_name as thrUserName,ui4.user_name as fouUserName");
                SELECT( "rtod.real_plat_profit,rtod.basic_bonus_amount,rtod.bonus_amount,rtod.rate,rtod.rate_type");
                SELECT( "rtbe.adjust_ratio,rtbe.user_code as redUserCode,redUi.user_name as redUserName,rtod.rate,rtod.rate_type");
            }};
            whereSql(sql, record);
            sql.ORDER_BY(" zpo.create_time desc");
            return sql.toString();
    }
        public String selectOrderSum(Map<String, Object> param){
            final ZxProductOrder record = (ZxProductOrder) param.get("record");
            SQL sql = new SQL(){{
                SELECT("sum(om.total_bonus) as totalBonusSum");
                SELECT("sum(om.plate_profit) as plateProfitSum");
                SELECT("sum(om.org_profit) as orgProfitSum");
                SELECT("sum(rtod.real_plat_profit) as actualSum");
                SELECT("sum(rtod .bonus_amount) as territorySum");
            }};
            whereSql(sql, record);
            return sql.toString();
        }
        public void whereSql(SQL sql, ZxProductOrder record){
            sql.FROM("zx_product_order zpo");
            sql.LEFT_OUTER_JOIN("  order_main om on zpo.yhj_order_no  = om.order_no");
            sql.LEFT_OUTER_JOIN("  red_territory_order_detail rtod on rtod.order_no  = om.order_no");
            sql.LEFT_OUTER_JOIN("  red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id");
            sql.LEFT_OUTER_JOIN("  user_info redUi on redUi.user_code  = rtbe.user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui0 on ui0.user_code = om.user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui1 on ui1.user_code = om.one_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui2 on ui2.user_code = om.two_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui3 on ui3.user_code = om.thr_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui4 on ui4.user_code = om.fou_user_code");
            if(record.getOrgId()!=null){
                sql.WHERE("om.org_id = #{record.orgId}");
            }
            if(StringUtils.isNotBlank(record.getOrderNo())){
                sql.WHERE("zpo.order_no = #{record.orderNo}");
            }
            if(StringUtils.isNotBlank(record.getYhjOrderNo())){
                sql.WHERE("zpo.yhj_order_no = #{record.yhjOrderNo}");
            }
            if(StringUtils.isNotBlank(record.getApplyNo())){
                sql.WHERE("zpo.apply_no = #{record.applyNo}");
            }

            if(StringUtils.isNotBlank(record.getReportType())){
                sql.WHERE("zpo.report_type = #{record.reportType}");
            }

            if(StringUtils.isNotBlank(record.getStatus())){
                sql.WHERE("zpo.status = #{record.status}");
            }
            if(StringUtils.isNotBlank(record.getApplyNo())){
                sql.WHERE("zpo.apply_no = #{record.applyNo}");
            }
            if(StringUtils.isNotBlank(record.getPayNo())){
                sql.WHERE("zpo.pay_No = #{record.payNo}");
            }
            if(StringUtils.isNotBlank(record.getOrderNo())){
                sql.WHERE("zpo.order_no = #{record.orderNo}");
            }
            if(StringUtils.isNotBlank(record.getReportNo())){
                sql.WHERE("zpo.report_no = #{record.reportNo}");
            }
            if(StringUtils.isNotBlank(record.getRecordName())){
                sql.WHERE("zpo.record_name = #{record.recordName}");
            }
            if(StringUtils.isNotBlank(record.getRecordPhone())){
                sql.WHERE("zpo.record_phone = #{record.recordPhone}");
            }
            if(StringUtils.isNotBlank(record.getRecordIdNo())){
                sql.WHERE("zpo.record_id_no = #{record.recordIdNo}");
            }
            if(StringUtils.isNotBlank(record.getContactPhone())){
                sql.WHERE("zpo.contact_phone = #{record.contactPhone}");
            }
            if(StringUtils.isNotBlank(record.getPayMethod())){
                sql.WHERE("zpo.pay_method = #{record.payMethod}");
            }

            if(StringUtils.isNotBlank(record.getPayMethod())){
                sql.WHERE("zpo.pay_method = #{record.payMethod}");
            }

            if(StringUtils.isNotBlank(record.getGenerationTimeBegin())&&StringUtils.isNotBlank(record.getGenerationTimeEnd())){
                sql.WHERE("zpo.generation_time between #{record.generationTimeBegin} and #{record.generationTimeEnd}");
            }
            if(StringUtils.isNotBlank(record.getRefundTimeBegin())&&StringUtils.isNotBlank(record.getRefundTimeEnd())){
                sql.WHERE("zpo.refund_time between #{record.refundTimeBegin} and #{record.refundTimeEnd}");
            }

            if(StringUtils.isNotBlank(record.getShareUserPhone())){
                sql.WHERE("ui0.phone = #{record.shareUserPhone}");
            }
            if(StringUtils.isNotBlank(record.getUserName())){
                sql.WHERE("ui0.user_name = #{record.userName}");
            }
            if(StringUtils.isNotBlank(record.getUserCode())){
                sql.WHERE("om.user_code = #{record.userCode}");
            }

            if(StringUtils.isNotBlank(record.getOneUserCode())){
                sql.WHERE("om.one_user_code = #{record.oneUserCode}");
            }
            if(StringUtils.isNotBlank(record.getOneUserName())){
                sql.WHERE("ui1.user_name = #{record.oneUserName}");
            }
            if(StringUtils.isNotBlank(record.getOneUserType())){
                sql.WHERE("om.one_user_type = #{record.oneUserType}");
            }
            if(StringUtils.isNotBlank(record.getTwoUserCode())){
                sql.WHERE("om.two_user_code = #{record.twoUserCode}");
            }
            if(StringUtils.isNotBlank(record.getTwoUserName())){
                sql.WHERE("ui2.user_name = #{record.twoUserName}");
            }
            if(StringUtils.isNotBlank(record.getTwoUserType())){
                sql.WHERE("om.two_user_type = #{record.twoUserType}");
            }
            if(StringUtils.isNotBlank(record.getThrUserCode())){
                sql.WHERE("om.thr_user_code = #{record.thrUserCode}");
            }
            if(StringUtils.isNotBlank(record.getThrUserName())){
                sql.WHERE("ui3.user_name = #{record.thrUserName}");
            }
            if(StringUtils.isNotBlank(record.getThrUserType())){
                sql.WHERE("om.thr_user_type = #{record.thrUserType}");
            }
            if(StringUtils.isNotBlank(record.getFouUserCode())){
                sql.WHERE("om.fou_user_code = #{record.fouUserCode}");
            }
            if(StringUtils.isNotBlank(record.getFouUserName())){
                sql.WHERE("ui4.user_name = #{record.fouUserName}");
            }
            if(StringUtils.isNotBlank(record.getFouUserType())){
                sql.WHERE("om.fou_user_type = #{record.fouUserType}");
            }

            if(StringUtils.isNotBlank(record.getOpenProvince()) && !"全部".equals(record.getOpenProvince())){
                sql.WHERE("ui0.open_province = #{record.openProvince}");
            }
            if(StringUtils.isNotBlank(record.getOpenCity()) && !"全部".equals(record.getOpenCity())){
                sql.WHERE("ui0.open_city = #{record.openCity}");
            }
            if(StringUtils.isNotBlank(record.getOpenRegion()) && !"全部".equals(record.getOpenRegion())){
                sql.WHERE("ui0.open_region = #{record.openRegion}");
            }

        }
    }
}
