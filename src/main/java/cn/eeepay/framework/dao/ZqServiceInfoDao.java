package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ZqServiceInfo;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 直清商户服务报件 数据层
 * 
 * @author tans
 * @date 2019-04-02
 */
public interface ZqServiceInfoDao {
	/**
     * 查询直清商户服务报件信息
     * 
     * @param id 直清商户服务报件ID
     * @return 直清商户服务报件信息
     */
	@Select("select * from zq_service_info where id = #{id}")
	@ResultType(ZqServiceInfo.class)
	ZqServiceInfo selectZqServiceInfoById(@Param("id") Long id);
	
	/**
     * 查询直清商户服务报件列表
     * 
     * @param zqServiceInfo 直清商户服务报件信息
     * @return 直清商户服务报件集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectZqServiceInfoPage")
	@ResultType(ZqServiceInfo.class)
	List<ZqServiceInfo> selectZqServiceInfoPage(@Param("page") Page<ZqServiceInfo> page,
                                                       @Param("zqServiceInfo") ZqServiceInfo zqServiceInfo);

	@Select("select status from zq_service_info where merchant_no = #{merchantNo} and service_id = #{serviceId}" +
			" and channel_code = #{channelCode}")
	@ResultType(ZqServiceInfo.class)
    ZqServiceInfo selectStatus(ZqServiceInfo zqServiceInfo);

	@Update("update zq_service_info set deal_status = #{dealStatus}, deal_operator = #{loginName} where id = #{id}")
	int updateDealStatus(@Param("id") Long id, @Param("dealStatus") String dealStatus, @Param("loginName") String loginName);

	class SqlProvider{
		public String selectZqServiceInfoPage(Map<String, Object> param){
			ZqServiceInfo zqServiceInfo = (ZqServiceInfo) param.get("zqServiceInfo");
			SQL sql = new SQL();
			sql.SELECT("zsi.*");
			sql.SELECT("mi.merchant_name, mi.mobilephone");
			sql.SELECT("zmi.unionpay_mer_no");
			sql.FROM("zq_service_info zsi");
			sql.INNER_JOIN("merchant_info mi on mi.merchant_no = zsi.merchant_no");
			sql.LEFT_OUTER_JOIN("zq_merchant_info zmi on zmi.merchant_no = zsi.merchant_no" +
					" and(zmi.mbp_id+0) = zsi.mbp_id" +	//字符串转换为数字后再进行关联，保证两边类型一致
					" and zmi.channel_code = zsi.channel_code");
			if(zqServiceInfo.getMbpId() != null){
				sql.WHERE("zsi.mbp_id = #{zqServiceInfo.mbpId}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getMerchantNo())){
				sql.WHERE("zsi.merchant_no = #{zqServiceInfo.merchantNo}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getMobilephone())){
				sql.WHERE("mi.mobilephone = #{zqServiceInfo.mobilephone}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getMerchantName())){
				zqServiceInfo.setMerchantName("%" + zqServiceInfo.getMerchantName() + "%");
				sql.WHERE("mi.merchant_name like #{zqServiceInfo.merchantName}");
			}
			if(zqServiceInfo.getBpId() != null){
				sql.WHERE("zsi.bp_id = #{zqServiceInfo.bpId}");
			}
			if(zqServiceInfo.getBpId() != null){
				sql.WHERE("zsi.bp_id = #{zqServiceInfo.bpId}");
			}
			if(zqServiceInfo.getServiceId() != null){
				sql.WHERE("zsi.service_id = #{zqServiceInfo.serviceId}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getStatus())){
				sql.WHERE("zsi.status = #{zqServiceInfo.status}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getChannelCode())){
				sql.WHERE("zsi.channel_code = #{zqServiceInfo.channelCode}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getUnionpayMerNo())){
				sql.WHERE("zmi.unionpay_mer_no = #{zqServiceInfo.unionpayMerNo}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getCreateTimeStart())){
				sql.WHERE("zsi.create_time >= #{zqServiceInfo.createTimeStart}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getCreateTimeEnd())){
				sql.WHERE("zsi.create_time <= #{zqServiceInfo.createTimeEnd}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getLastUpdateTimeStart())){
				sql.WHERE("zsi.last_update_time >= #{zqServiceInfo.lastUpdateTimeStart}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getLastUpdateTimeEnd())){
				sql.WHERE("zsi.last_update_time <= #{zqServiceInfo.lastUpdateTimeEnd}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getAcqServiceMerNo())){
				sql.WHERE("zsi.acq_service_mer_no = #{zqServiceInfo.acqServiceMerNo}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getDealStatus())){
				sql.WHERE("zsi.deal_status = #{zqServiceInfo.dealStatus}");
			}
			if(StringUtils.isNotBlank(zqServiceInfo.getDealOperator())){
				zqServiceInfo.setDealOperator("%" + zqServiceInfo.getDealOperator() + "%");
				sql.WHERE("zsi.deal_operator like #{zqServiceInfo.dealOperator}");
			}
			return sql.toString();
		}
	}

	
}