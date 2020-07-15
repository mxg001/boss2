package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.ShareDetail;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface ShareDetailDao {

    @SelectProvider(type=SqlProvider.class,method="queryShareDetailList")
    @ResultType(ShareDetail.class)
    List<ShareDetail> queryShareDetailList(@Param("info") ShareDetail info, @Param("page") Page<ShareDetail> page);

    @SelectProvider(type=SqlProvider.class,method="queryShareDetailCount")
    @ResultType(Map.class)
    Map<String, Object> queryShareDetailCount(@Param("info") ShareDetail info);

    class SqlProvider{

        public String queryShareDetailList(final Map<String, Object> param) {
            final ShareDetail info = (ShareDetail) param.get("info");
            SQL sql = new SQL(){{
                SELECT("psd.*,pui.user_type,pui.real_name,pui.real_name,pui.one_user_code,pui.two_user_code,oem.brand_name");
                FROM("pa_share_detail psd");
                LEFT_OUTER_JOIN("pa_user_info pui on pui.user_code=psd.user_code");
                LEFT_OUTER_JOIN("pa_brand oem ON oem.brand_code=pui.brand_code");
            }};
            where(sql, info);
            sql.ORDER_BY("psd.create_time DESC");
            return sql.toString();
        }

        public String queryShareDetailCount(final Map<String, Object> param) {
            final ShareDetail info = (ShareDetail) param.get("info");
            SQL sql = new SQL(){{
                SELECT("sum(psd.share_amount) shareAmountCount,sum(if(psd.acc_status='ENTERACCOUNTED',psd.share_amount,0.00)) accYesCount," +
                        "sum(if(psd.acc_status='NOENTERACCOUNT',psd.share_amount,0.00)) accNoCount");
                FROM("pa_share_detail psd");
                LEFT_OUTER_JOIN("pa_user_info pui on pui.user_code=psd.user_code");
                LEFT_OUTER_JOIN("pa_brand oem ON oem.brand_code=pui.brand_code");
            }};
            where(sql, info);
            return sql.toString();
        }

        public void where(SQL sql, ShareDetail info) {
            if(StringUtils.isNotBlank(info.getRealName())){
                sql.WHERE("pui.real_name = #{info.realName} ");
            }
            if(StringUtils.isNotBlank(info.getUserType())){
                sql.WHERE("pui.user_type = #{info.userType} ");
            }
            if(StringUtils.isNotBlank(info.getUserCode())){
                sql.WHERE("pui.user_code = #{info.userCode} ");
            }
            if(StringUtils.isNotBlank(info.getShareType())){
                sql.WHERE("psd.share_type = #{info.shareType} ");
            }
            if(StringUtils.isNotBlank(info.getAccStatus())){
                sql.WHERE("psd.acc_status = #{info.accStatus} ");
            }
            if(StringUtils.isNotBlank(info.getBrandCode())){
                sql.WHERE("pui.brand_code = #{info.brandCode} ");
            }
            if(StringUtils.isNotBlank(info.getTwoUserCode())){
                sql.WHERE("pui.two_user_code = #{info.twoUserCode} ");
            }
            if(StringUtils.isNotBlank(info.getOneUserCode())){
                sql.WHERE("pui.one_user_code = #{info.oneUserCode} ");
            }
            if(StringUtils.isNotBlank(info.getShareMonth())){
                sql.WHERE("psd.share_month = #{info.shareMonth} ");
            }
            if(StringUtils.isNotBlank(info.getAccStartTime())){
                sql.WHERE("psd.acc_time  >= #{info.accStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getAccEndTime())){
                sql.WHERE("psd.acc_time  <= #{info.accEndTime} ");
            }
            if(StringUtils.isNotBlank(info.getStartTime())){
                sql.WHERE("psd.create_time  >= #{info.startTime} ");
            }
            if(StringUtils.isNotBlank(info.getEndTime())){
                sql.WHERE("psd.create_time  <= #{info.endTime} ");
            }
        }
    }
}
