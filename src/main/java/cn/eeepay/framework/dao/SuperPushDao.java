package cn.eeepay.framework.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SuperPushShare;
import cn.eeepay.framework.model.SuperPushUser;

/** 
 * @author tans 
 * @version ：2017年5月10日 上午11:30:29 
 * 
 */
public interface SuperPushDao {
	
	Logger log = LoggerFactory.getLogger(SuperPushDao.class);

	@SelectProvider(type=SqlProvider.class, method="getByParam")
	@ResultType(SuperPushUser.class)
	List<SuperPushUser> getByParam(@Param("baseInfo")SuperPushUser baseInfo, @Param("page")Page<SuperPushUser> page);
	
	@Select("SELECT ai.agent_name agentName,ai1.agent_name oneAgentName,ai1.sale_name oneSaleName,"
			+ "sd.sys_name businessTypeStr,mi1.merchant_name oneMerchantName,"
			+ "mi2.merchant_name twoMerchantName,mi3.merchant_name threeMerchantName, mi.*"
			+ " FROM `merchant_info` mi "
			+ " LEFT JOIN agent_info ai on ai.agent_no=mi.agent_no"
			+ " LEFT JOIN agent_info ai1 on ai1.agent_no=mi.one_agent_no"
			+ " INNER JOIN super_push_user spu on spu.merchant_no=mi.merchant_no"
			+ " LEFT JOIN merchant_info mi1 on mi1.merchant_no=spu.one_merchant_no"
			+ " LEFT JOIN merchant_info mi2 on mi2.merchant_no=spu.two_merchant_no"
			+ " LEFT JOIN merchant_info mi3 on mi3.merchant_no=spu.three_merchant_no"
			+ " LEFT JOIN sys_dict sd on sd.sys_value=mi.business_type and sd.sys_key='sys_mcc'"
			+ " where mi.merchant_no=#{merchantNo};")
	@ResultType(MerchantInfo.class)
	MerchantInfo getSuperPushMerchantDetail(@Param("merchantNo")String merchantNo);
	
	@SelectProvider(type=SqlProvider.class,method="getSuperPushShareDetail")
	@ResultType(SuperPushShare.class)
	List<SuperPushShare> getSuperPushShareDetail(@Param("baseInfo")SuperPushShare baseInfo,
			@Param("page")Page<SuperPushShare> page);

	@Select("select mi.merchant_no,mi.merchant_name,ai.agent_no,ai.agent_name from merchant_info mi left join agent_info ai on ai.agent_no=mi.agent_no "
			+ " where mi.merchant_no=#{merchantNo}")
	@ResultType(SuperPushUser.class)
	SuperPushUser getCashMerchantDetail(@Param("merchantNo")String merchantNo);
	
//	@Select("select sum(result.sumShare) from "
//			+" (select sum(sps.self_share)sumShare from super_push_user spu"
//			+" LEFT JOIN super_push_share sps on sps.merchant_no=spu.merchant_no"
//			+" where spu.merchant_no=#{merchantNo}"
//			+" UNION ALL select sum(sps.one_share) sumShare from super_push_user spu"
//			+" LEFT JOIN super_push_share sps ON sps.merchant_no = spu.merchant_no WHERE spu.one_merchant_no = #{merchantNo} "
//			+" UNION ALL select sum(sps.two_share) sumShare from super_push_user spu"
//			+" LEFT JOIN super_push_share sps on sps.merchant_no=spu.merchant_no where spu.two_merchant_no=#{merchantNo} "
//			+" UNION ALL select sum(sps.three_share) sumShare from super_push_user spu"
//			+" LEFT JOIN super_push_share sps on sps.merchant_no=spu.merchant_no where spu.three_merchant_no=#{merchantNo}) result;")
	@Select("select SUM(share_amount) from super_push_share where share_no=#{merchantNo};")
	@ResultType(BigDecimal.class)
	BigDecimal getTotalAmount(@Param("merchantNo")String merchantNo);

	@Select("select soi.settle_order,soi.create_time,soi.settle_amount,soi.settle_status from settle_order_info soi "
			+ " where soi.settle_type=#{soi.settleType} and soi.sub_type=#{soi.subType}"
			+ " and soi.settle_user_type=#{soi.settleUserType}"
			+ " and soi.settle_user_no=#{soi.settleUserNo}")
	@ResultType(SettleOrderInfo.class)
	List<SettleOrderInfo> getCashPage(@Param("soi")SettleOrderInfo settleOrderInfo, @Param("page")Page<SuperPushUser> page);
	
	@SelectProvider(type=SqlProvider.class, method="getShareByParam")
	@ResultType(SuperPushShare.class)
	List<SuperPushShare> getShareByParam(@Param("baseInfo")SuperPushShare baseInfo, Page<SuperPushShare> page);
	
	@SelectProvider(type=SqlProvider.class, method="getTotalShareByParam")
	Map<String, Object> getTotalShareByParam(@Param("baseInfo")SuperPushShare baseInfo);
	
	@SelectProvider(type=SqlProvider.class, method="getTotalTransAmountByParam")
	Map<String, Object> getTotalTransAmountByParam(@Param("baseInfo")SuperPushShare baseInfo);
	
	
	public class SqlProvider{
		public String getByParam(Map<String, Object> param){
			final SuperPushUser baseInfo = (SuperPushUser) param.get("baseInfo");
			String sql = new SQL(){{
				SELECT("mi0.merchant_name,ui.mobilephone,ai.agent_name,ai.agent_no,ai.one_level_id oneAgentNo,ifnull(mbp.status,'-1') status,spu.*");
				FROM("`super_push_user` spu");
				INNER_JOIN("user_info ui on ui.user_id = spu.user_id");
				LEFT_OUTER_JOIN("merchant_info mi0 on mi0.merchant_no=spu.merchant_no");
				INNER_JOIN("agent_info ai on spu.agent_node = ai.agent_node");
				LEFT_OUTER_JOIN("merchant_business_product mbp on mbp.merchant_no=spu.merchant_no and mbp.bp_id=#{baseInfo.bpId}");
//				LEFT_OUTER_JOIN("merchant_info mi1 on spu.one_merchant_no=mi1.merchant_no");
//				LEFT_OUTER_JOIN("merchant_info mi2 on spu.two_merchant_no=mi2.merchant_no");
//				LEFT_OUTER_JOIN("merchant_info mi3 on spu.three_merchant_no=mi3.merchant_no");
//				LEFT_OUTER_JOIN("(select SUM(self_share) total_amount,merchant_no from super_push_share GROUP BY merchant_no) sps on sps.merchant_no=spu.merchant_no");
				//必须是商户链接进来的商户，才是微创业的商户
				WHERE(" spu.recommended_source=1");
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getAgentNo())){
//					WHERE("ai.agent_node like concat(#{baseInfo.agentNo}, '%')");
					WHERE("spu.agent_node = #{baseInfo.agentNo}");
				}
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getOneAgentNo())){
//					WHERE("ai.agent_node like concat(#{baseInfo.oneAgentNo},'%')");
					WHERE("ai.one_level_id = #{baseInfo.oneAgentNo}");
				}
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getMerchantNo())){
					WHERE("spu.merchant_no=#{baseInfo.merchantNo}");
				}
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getOneMerchantNo())){
					WHERE(" spu.one_merchant_no=#{baseInfo.oneMerchantNo}");
				}
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getTwoMerchantNo())){
					WHERE(" spu.two_merchant_no=#{baseInfo.twoMerchantNo}");
				}
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getThreeMerchantNo())){
					WHERE(" spu.three_merchant_no=#{baseInfo.threeMerchantNo}");
				}
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getMobilephone())){
					WHERE("mi0.mobilephone=#{baseInfo.mobilephone}");
				}
				if(baseInfo!=null && baseInfo.getCreateTimeStart()!=null){
					WHERE("spu.create_time>=#{baseInfo.createTimeStart}");
				}
				if(baseInfo!=null && baseInfo.getCreateTimeEnd()!=null){
					WHERE("spu.create_time<=#{baseInfo.createTimeEnd}");
				}
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getStatus())){
					if("-1".equals(baseInfo.getStatus())){
						WHERE("mbp.merchant_no is null");
					} else {
						WHERE("mbp.status=#{baseInfo.status}");
					}
				}
				ORDER_BY("spu.create_time desc");
			}}.toString();
			return sql;
		}
		
		public String getSuperPushShareDetail(Map<String, Object> param){
			SuperPushShare baseInfo = (SuperPushShare) param.get("baseInfo");
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT m.merchant_no,m.merchant_name,t.trans_time,t.level,t.share,t.rule,t.amount FROM (");
			sql.append(" SELECT u.merchant_no,s.trans_time,0 as level,s.self_share share,s.self_rule rule,s.amount");
			sql.append(" FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.merchant_no=#{baseInfo.merchantNo} ");
			sql.append(" UNION ALL");
			sql.append(" SELECT u.merchant_no,s.trans_time,1 as level,s.one_share share,s.one_rule rule,s.amount");
			sql.append(" FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.one_merchant_no=#{baseInfo.merchantNo}");
			sql.append(" UNION ALL");
			sql.append(" SELECT u.merchant_no,s.trans_time,2 as level,s.two_share share,s.two_rule rule,s.amount");
			sql.append(" FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.two_merchant_no=#{baseInfo.merchantNo}");
			sql.append(" UNION ALL");
			sql.append(" SELECT u.merchant_no,s.trans_time,3 as level,s.three_share share,s.three_rule rule,s.amount");
			sql.append(" FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.three_merchant_no=#{baseInfo.merchantNo}) t");
			sql.append(" LEFT JOIN merchant_info m ON m.merchant_no = t.merchant_no where 1=1 ");
			if(baseInfo!=null && baseInfo.getTransTimeStart()!=null){
				sql.append(" and t.trans_time>=#{baseInfo.transTimeStart}");
			}
			if(baseInfo!=null && baseInfo.getTransTimeEnd()!=null){
				sql.append(" and t.trans_time<=#{baseInfo.transTimeEnd}");
			}
			return sql.toString();
		}
		
		public String getShareByParam(Map<String, Object> param){
			final SuperPushShare baseInfo = (SuperPushShare) param.get("baseInfo");
			SQL sql = new SQL(){{
				SELECT("IFNULL(mi.merchant_name,ai.agent_name) share_name,sps.*");
				FROM("`super_push_share` sps");
				LEFT_OUTER_JOIN("merchant_info mi on mi.merchant_no=sps.share_no and sps.share_type>=2");
				LEFT_OUTER_JOIN("agent_info ai on ai.agent_no = sps.share_no and sps.share_type<=1");
			}};
			return sql.toString() + shareWhereSql(baseInfo);
		}
		
		public String getTotalShareByParam(Map<String, Object> param){
			final SuperPushShare baseInfo = (SuperPushShare) param.get("baseInfo");
			SQL sql = new SQL(){{
				SELECT("sum( sps.share_amount) as sumShareAmount");
				SELECT("sum(if(sps.share_status='1', sps.share_amount,0)) as recordSumShareAmount");
				SELECT("sum(if(sps.share_status='0',sps.share_amount,0)) as noRecordSumShareAmount");
				FROM("`super_push_share` sps");
			}};
			log.info("统计微创业入账金额sql:{}", sql.toString() + shareWhereSql(baseInfo));
			return sql.toString() + shareWhereSql(baseInfo);
		}
		
		public String getTotalTransAmountByParam(Map<String, Object> param){
			final SuperPushShare baseInfo = (SuperPushShare) param.get("baseInfo");
			SQL sql = new SQL(){{
				SELECT("sum(total.trans_amount) sumTransAmount,count(1) sumTransOrder "
				+ " from (select distinct sps.order_no, sps.trans_amount from super_push_share sps"
						+ shareWhereSql(baseInfo).toString()
						+ ") total");
			}};
			log.info("统计微创业交易总金额sql:{}", sql.toString());
			return sql.toString();
		}
			
		public String shareWhereSql(final SuperPushShare baseInfo) {
			StringBuilder sql = new StringBuilder();
			sql.append(" where 1=1");
			if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getShareNo())){
				sql.append(" and sps.share_no = #{baseInfo.shareNo}");
			}
			if(baseInfo!=null && baseInfo.getCreateTimeStart()!=null){
				sql.append(" and sps.create_time>=#{baseInfo.createTimeStart}");
			}
			if(baseInfo!=null && baseInfo.getCreateTimeEnd()!=null){
				sql.append(" and sps.create_time<=#{baseInfo.createTimeEnd}");
			}
			if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getShareType())){
				sql.append(" and sps.share_type=#{baseInfo.shareType}");
			}
			if(baseInfo!=null && baseInfo.getShareTimeStart()!=null){
				sql.append(" and sps.share_time>=#{baseInfo.shareTimeStart}");
			}
			if(baseInfo!=null && baseInfo.getShareTimeEnd()!=null){
				sql.append(" and sps.share_time<=#{baseInfo.shareTimeEnd}");
			}
			if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getShareStatus())){
				sql.append(" and sps.share_status=#{baseInfo.shareStatus}");
			}
			if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getOrderNo())){
				sql.append(" and sps.order_no=#{baseInfo.orderNo}");
			}
			if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getMerchantNo())){
				sql.append(" and sps.merchant_no=#{baseInfo.merchantNo}");
			}
			sql.append(" order by sps.create_time desc");
			return sql.toString();
		}
	}




}
