package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface ServiceDao {
	@SelectProvider(type=SqlProvider.class, method="getServiceInfo")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> getServiceInfo(@Param("info")Map<String,Object> info,Page<ServiceInfo> page);
	
	//用于业务产品默认路由集群
	@Select("select * from service_info")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> selectServiceInfo();
	
	@Select("select s1.*,s2.service_name linkServiceName from service_info s1 left join service_info s2 on s1.link_service=s2.service_id where s1.service_id = #{serviceId}")
	@ResultType(ServiceInfo.class)
	ServiceInfo queryServiceInfo(Long serviceId);
	
	/*//查询系统费率 //此SQL有问题，会查出多个结果
	@Select("select smr.*, si.service_name, bpd.bp_name from service_manage_rate smr" +
			" left join service_info si on si.service_id = smr.service_id" +
			" left join business_product_info bpi on bpi.service_id = smr.service_id" +
			" left join business_product_define bpd on bpd.bp_id = bpi.bp_id" +
			" where "
			+ " smr.service_id=#{srs.serviceId} and smr.agent_no='0' "
			+ " and smr.holidays_mark=#{srs.holidaysMark} and smr.card_type=#{srs.cardType}")
	@ResultType(ServiceRate.class)
	ServiceRate querySysServiceRate(@Param("srs")ServiceRate sr);*/

	//查询系统费率
	@Select("select smr.*, si.service_name, bpd.bp_name from service_manage_rate smr" +
			" left join service_info si on si.service_id = smr.service_id" +
			//" left join business_product_info bpi on bpi.service_id = smr.service_id" +
			" left join business_product_define bpd on bpd.bp_id = #{srs.bpId}" +
			" where "
			+ " smr.service_id=#{srs.serviceId} and smr.agent_no='0' "
			+ " and smr.holidays_mark=#{srs.holidaysMark} and smr.card_type=#{srs.cardType}")
	@ResultType(ServiceRate.class)
	ServiceRate querySysServiceRate(@Param("srs")ServiceRate sr);

	//用于商户费率
	@Select("select * from service_manage_rate where "
			+ "service_id=#{srs.serviceId} and agent_no=#{srs.agentNo} "
			+ "and holidays_mark=#{srs.holidaysMark} and card_type=#{srs.cardType} and rate_type=#{srs.rateType}")
	@ResultType(ServiceRate.class)
	ServiceRate queryServiceRate(@Param("srs")ServiceRate sr);
	
	//用于商户
	@Select("select * from service_manage_rate where service_id=#{serviceId} and agent_no=#{agentId}")
	@ResultType(ServiceRate.class)
	List<ServiceRate> getServiceAllRate(@Param("serviceId") Long serviceId,@Param("agentId")String agentId);
	//用于商户
	@Select("select * from service_manage_quota where service_id=#{serviceId} and agent_no=#{agentId}")
	@ResultType(ServiceQuota.class)
	List<ServiceQuota> getServiceAllQuota(@Param("serviceId") Long serviceId,@Param("agentId")String agentId);
	
	//用于商户限额
	@Select("select * from service_manage_quota where "
			+ "service_id=#{srs.serviceId} and agent_no=#{srs.agentNo} "
			+ "and holidays_mark=#{srs.holidaysMark} and card_type=#{srs.cardType}")
	@ResultType(ServiceRate.class)
	ServiceQuota queryServiceQuota(@Param("srs")ServiceQuota sq);
	
	@SelectProvider(type=SqlProvider.class, method="getServiceInfoWithDetail")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> getServiceInfoWithDetail(@Param("info")ServiceInfo info);
	
	@Select("select * from service_manage_rate where service_id=#{serviceId} and agent_no=#{agentNo}")
	@ResultType(ServiceRate.class)
	List<ServiceRate> getServiceRate(@Param("serviceId") Long serviceId,@Param("agentNo") String agentNo);
	
	@Select("select * from service_manage_quota where service_id=#{serviceId} and agent_no=#{agentNo}")
	@ResultType(ServiceQuota.class)
	List<ServiceQuota> getServiceQuota(@Param("serviceId") Long serviceId,@Param("agentNo") String agentNo);
	
	@Select("SELECT sd.sys_name serviceTypeName,s.rate_holidays,s.rate_card,s.service_id,s.service_name ,s.service_type,s.t_flag,s.link_service FROM " +
			" service_info s " +
			" left join sys_dict sd on sd.sys_value = s.service_type and sd.sys_key='SERVICE_TYPE' "+
 			" WHERE service_id=#{id}")
	@ResultType(ServiceInfo.class)
	ServiceInfo findServiceName(@Param("id")String id);


	@Select("SELECT sd.sys_name serviceTypeName,s.rate_holidays,s.rate_card,s.service_id,s.service_name ,s.service_type,s.t_flag,s.link_service FROM " +
			" service_info s " +
			" left join sys_dict sd on sd.sys_value = s.service_type and sd.sys_key='SERVICE_TYPE' "+
			" WHERE service_id in (SELECT service_id FROM business_product_info WHERE bp_id=#{bpId})")
	@ResultType(List.class)
	List<ServiceInfo> getBusinessServiceInfoToExport(@Param("bpId")Long bpId);



	@Select("select s.service_id,s.service_name,s.service_type,s.t_flag,s.link_service from service_info s where s.service_type<>'10001' "
			+ " and not EXISTS(select 1 from business_product_info bpi where s.service_id=bpi.service_id)"
			+" ORDER BY s.service_type+0,s.service_name")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> findAllServiceName();
	
	@SelectProvider(type=SqlProvider.class, method="existServiceName")
	int existServiceName(@Param("info")ServiceInfo serviceInfo);

	@SelectProvider(type=SqlProvider.class, method="existAgentShowName")
	int existAgentShowName(@Param("info") ServiceInfo serviceInfo);

	@Update("update service_info set service_status=#{status} where service_id=#{serviceId}")
	int updateServiceStatus(@Param("serviceId")String id,@Param("status") String status);

	@Select("SELECT s1.service_id,s1.service_name FROM service_info s1 where s1.service_type='10001'" +
			" and s1.effective_status = '1'"
			+ " and not EXISTS(SELECT 1 from service_info s2 where s2.link_service = s1.service_id);")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> getLinkServices();
	
	@Insert("insert into service_info(service_name,agent_show_name,service_type,hardware_is,bank_card,exclusive,business,sale_starttime,sale_endtime,use_starttime,use_endtime,proxy,"
			+ "getcash_id,rate_card,rate_holidays,quota_holidays,quota_card,oem_id,remark,t_flag,cash_subject,fixed_rate,fixed_quota,trad_start,trad_end,link_service,t0_turn_t1,create_person,create_time,loan_merge_show)"
			+ " values(#{s.serviceName},#{s.agentShowName},#{s.serviceType},#{s.hardwareIs},#{s.bankCard},#{s.exclusive},#{s.business},#{s.saleStarttime},#{s.saleEndtime},"
			+ "#{s.useStarttime},#{s.useEndtime},#{s.proxy},#{s.getcashId},#{s.rateCard},#{s.rateHolidays},#{s.quotaHolidays},#{s.quotaCard},#{s.oemId},#{s.remark},#{s.tFlag},#{s.cashSubject}"
			+ ",#{s.fixedRate},#{s.fixedQuota},#{s.tradStart},#{s.tradEnd},#{s.linkService},#{s.t0TurnT1},#{s.createPerson},#{s.createTime},#{s.loanMergeShow})")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="s.serviceId", before=false, resultType=Long.class)  
	int insertServiceInfo(@Param("s")ServiceInfo serviceInfo);

	@InsertProvider(type=SqlProvider.class,method="insertServiceRateList")
	int insertServiceRateList(@Param("list")List<ServiceRate> list);

	@InsertProvider(type=SqlProvider.class,method="insertServiceQuotaList")
	int insertServiceQuotaList(@Param("list")List<ServiceQuota> list);
	
	@Delete("delete from service_manage_rate where service_id = #{serviceId} and agent_no=#{agentNo}")
	int deleteServiceRateByFK(@Param("serviceId")Long id,@Param("agentNo")String agentNo);

	@Delete("delete from service_manage_quota where service_id = #{serviceId} and agent_no=#{agentNo}")
	int deleteServiceQuotaByFK(@Param("serviceId")Long id,@Param("agentNo")String agent);
	
	@Update("update service_info set service_name=#{info.serviceName}, agent_show_name=#{info.agentShowName},service_type=#{info.serviceType}, hardware_is=#{info.hardwareIs}, bank_card=#{info.bankCard}, exclusive=#{info.exclusive},fixed_rate=#{info.fixedRate},fixed_quota=#{info.fixedQuota},"
			+ "service_status=#{info.serviceStatus},trad_start=#{info.tradStart},trad_end=#{info.tradEnd},link_service=#{info.linkService},"
			+ " business=#{info.business}, sale_starttime=#{info.saleStarttime}, sale_endtime=#{info.saleEndtime}, use_starttime=#{info.useStarttime}, use_endtime=#{info.useEndtime}, proxy=#{info.proxy}, t_flag=#{info.tFlag},cash_subject=#{info.cashSubject},"
			+ "getcash_id=#{info.getcashId}, rate_card=#{info.rateCard}, rate_holidays=#{info.rateHolidays}, quota_holidays=#{info.quotaHolidays}, quota_card=#{info.quotaCard}, oem_id=#{info.oemId}, remark=#{info.remark},t0_turn_t1=#{info.t0TurnT1},"
			+ "loan_merge_show=#{info.loanMergeShow} where service_id = #{info.serviceId}")
	int updateServiceInfo(@Param("info")ServiceInfo serviceInfo);

	@Delete("delete from service_info where service_id = #{serviceId}")
	int deleteServiceInfo(@Param("serviceId")Long id);
	
	@Update("update service_manage_rate set single_num_amount=#{singleNumAmount},"
			+ "rate=#{rate},capping=#{capping},safe_line=#{safeLine},"
			+ "check_status=#{checkStatus},lock_status=#{lockStatus},"
			+ "ladder1_rate=#{ladder1Rate},ladder1_max=#{ladder1Max},ladder2_rate=#{ladder2Rate},"
			+ "ladder2_max=#{ladder2Max},ladder3_rate=#{ladder3Rate},ladder3_max=#{ladder3Max},ladder4_rate=#{ladder4Rate},"
			+ "ladder4_max=#{ladder4Max}"
			+ " where holidays_mark=#{holidaysMark} and card_type=#{cardType} and is_global=1 and service_id = #{serviceId}")
	int updateAgentServiceRateList(ServiceRate rate);

	@Update("update service_manage_quota set single_day_amount=#{singleDayAmount},single_min_amount=#{singleMinAmount},single_count_amount=#{singleCountAmount},"
			+ "single_daycard_amount=#{singleDaycardAmount},single_daycard_count=#{singleDaycardCount},"
			+ "check_status=#{checkStatus},lock_status=#{lockStatus}"
			+ " where holidays_mark=#{holidaysMark} and card_type=#{cardType} and is_global=1 and service_id = #{serviceId}")
	int updateAgentServiceQuotaList(ServiceQuota quota);
	
//	@SelectProvider(type=SqlProvider.class,method="getServiceQuotaByIds")
//	@ResultType(ServiceQuota.class)
//	List<ServiceQuota> getServiceQuotaByIds(@Param("list")List<Long> list);
//	
//	@SelectProvider(type=SqlProvider.class,method="getServiceRateByIds")
//	@ResultType(ServiceRate.class)
//	List<ServiceRate> getServiceRateByIds(@Param("list")List<Long> list);
	
	@Select("select * from service_manage_rate where holidays_mark=#{holidaysMark} and card_type=#{cardType} and"
			+ " agent_no='0' and service_id=#{serviceId}")
	@ResultType(ServiceRate.class)
	ServiceRate getServiceRateByRate(ServiceRate rate);

	@Select("select * from service_manage_quota where holidays_mark=#{holidaysMark} and card_type=#{cardType} and"
			+ " agent_no='0' and service_id=#{serviceId}")
	@ResultType(ServiceQuota.class)
	ServiceQuota getServiceQuotaByQuota(ServiceQuota quota);
	
	/**
	 * 查询同组业务产品的最小费率
	 * @author tans
	 * @date 2017年6月8日 上午10:19:41
	 * @param
	 * @return
	 */
//	@Select("SELECT smr.rate,smr.`single_num_amount`,temp.isTx,temp.bp_name,temp.service_name"
//			+" FROM service_manage_rate smr"
//			+" JOIN ("
//			+" SELECT v2.`service_id`,v2.isTx,v2.`bp_name`,v2.`service_name` "
//			+" FROM ("
//			+" SELECT "
//			+" si1.`service_id`,"
//			+" si1.`service_name`,"
//			+" CONCAT(si1.`service_type`, '-', IFNULL(si2.`service_type`, '')) AS service_type,"
//			+" bpi.`bp_id`,"
//			+" IFNULL(bpg.`group_no`, concat('G', si1.`service_id`)) AS group_no,"
//			+" bpd.bp_name,"
//			+" IF(si2.service_type IS NULL, 0, 1) AS isTx"
//			+" FROM service_info si1"
//			+" LEFT JOIN service_info si2 ON si2.`link_service` = si1.`service_id`"
//			+" LEFT JOIN business_product_info bpi ON bpi.`service_id` = si1.`service_id`"
//			+" LEFT JOIN business_product_group bpg ON bpg.`bp_id` = bpi.`bp_id`"
//			+" LEFT JOIN business_product_define bpd ON bpd.`bp_id` = bpi.`bp_id`"
//			+" 	) v1"
//			+" LEFT JOIN ("
//			+" SELECT "
//			+" si1.`service_id`,"
//			+" si1.`service_name`,"
//			+" CONCAT(si1.`service_type`, '-', IFNULL(si2.`service_type`, '')) AS service_type,"
//			+" bpi.`bp_id`,"
//			+" IFNULL(bpg.`group_no`, concat('G', si1.`service_id`)) AS group_no,"
//			+" bpd.bp_name,"
//			+" IF(si2.service_type IS NULL, 0, 1) AS isTx"
//			+" FROM service_info si1"
//			+" LEFT JOIN service_info si2 ON si2.`link_service` = si1.`service_id`"
//			+" LEFT JOIN business_product_info bpi ON bpi.`service_id` = si1.`service_id`"
//			+" LEFT JOIN business_product_group bpg ON bpg.`bp_id` = bpi.`bp_id`"
//			+" LEFT JOIN business_product_define bpd ON bpd.`bp_id` = bpi.`bp_id`"
//			+" where exists(select 1 from agent_business_product where status=1 and bp_id=bpi.bp_id and agent_no=#{agentNo})"
//			+" ) v2 ON v1.service_type = v2.service_type AND v1.group_no = v2.group_no"
//			+" WHERE v1.service_id = #{serviceId}"
//			+" )temp ON smr.`service_id` = temp.service_id"
//			+" AND smr.`agent_no` = #{agentNo}"
//			+" AND smr.`card_type` = #{cardType}"
//			+" AND smr.`holidays_mark` = #{holidaysMark}"
//			+" ORDER BY IF(temp.isTx = 1,smr.single_num_amount, smr.rate )"
//			+" LIMIT 1;")
//	@ResultType(ServiceRate.class)
//	ServiceRate queryMinServiceRate(ServiceRate rate);

	@Select("select bpd.bp_id,bpd.bp_name,bpd.allow_individual_apply\n" +
            " from business_product_define bpd\n" +
            " INNER JOIN business_product_info bpi on bpi.bp_id = bpd.bp_id\n" +
            " where bpi.service_id = #{serviceId}")
    @ResultType(BusinessProductDefine.class)
    BusinessProductDefine getProductByService(@Param("serviceId") Long serviceId);

	@Select("select si.service_id,si.service_name\n" +
            " from business_product_group bpg1\n" +
            " INNER JOIN business_product_group bpg2 on bpg2.group_no = bpg1.group_no\n" +
            " INNER JOIN business_product_info bpi on bpi.bp_id = bpg1.bp_id\n" +
            " INNER JOIN service_info si on si.service_id = bpi.service_id\n" +
            " where bpg2.bp_id = #{bpId} and bpg1.bp_id <> #{bpId} \n" +
            " and si.service_type = #{serviceType} and si.effective_status = '1'")
	@ResultType(ServiceInfo.class)
    List<ServiceInfo> getMemberServiceByProduct(@Param("bpId") Long bpId, @Param("serviceType") Integer serviceType);

	@Update("update service_info set effective_status = #{effectiveStatus} where service_id = #{serviceId}")
    int updateEffectiveStatus(ServiceInfo baseInfo);

	@Select("select count(1) from business_product_group bpg\n" +
			"INNER JOIN business_product_info bpi on bpi.bp_id = bpg.bp_id\n" +
			"where bpi.service_id = #{serviceId}")
	@ResultType(Integer.class)
	int hasGroup(@Param("serviceId") String serviceId);

	@Select("SELECT smr.rate,smr.rate_type,smr.capping,si.service_name,bpd.bp_name\n" +
			" from business_product_info bpi\n" +
			"LEFT JOIN business_product_group bpg on bpg.bp_id = bpi.bp_id\n" +
			"LEFT JOIN business_product_group bpg2 on bpg2.group_no = bpg.group_no\n" +
			"LEFT JOIN business_product_info bpi2 on bpi2.bp_id = bpg2.bp_id\n" +
			"LEFT JOIN service_info si on si.service_id = bpi2.service_id\n" +
			"LEFT JOIN service_manage_rate smr on smr.service_id = si.service_id\n" +
			"LEFT JOIN business_product_define bpd on bpd.bp_id = bpg2.bp_id\n" +
			"left join agent_business_product abp on abp.bp_id = bpg2.bp_id " +
			"where\n" +
			"bpi.service_id = #{serviceId}\n" +
			"and si.effective_status = '1'\n" +
			"and smr.card_type = #{cardType}\n" +
			"and smr.holidays_mark = #{holidaysMark}\n" +
			"and smr.agent_no = '0'\n" +
			"and si.service_type = #{serviceType}\n" +
			"and abp.agent_no = #{agentNo}" +
			"ORDER BY smr.rate\n" +
			"limit 1")
	@ResultType(ServiceRate.class)
	ServiceRate queryMinJYServiceRate(ServiceRate rate);

	@Select("SELECT smr.single_num_amount,smr.rate_type,smr.capping,si.service_name,bpd.bp_name from business_product_info bpi\n" +
			"LEFT JOIN business_product_group bpg on bpg.bp_id = bpi.bp_id\n" +
			"LEFT JOIN business_product_group bpg2 on bpg2.group_no = bpg.group_no\n" +
			"LEFT JOIN business_product_info bpi2 on bpi2.bp_id = bpg2.bp_id\n" +
			"LEFT JOIN service_info si on si.service_id = bpi2.service_id\n" +
			"LEFT JOIN service_info si2 on si2.link_service = si.service_id\n" +
			"LEFT JOIN service_manage_rate smr on smr.service_id = si.service_id\n" +
			"LEFT JOIN business_product_define bpd on bpd.bp_id = bpg2.bp_id " +
			"left join agent_business_product abp on abp.bp_id = bpg2.bp_id " +
			"where\n" +
			"bpi.service_id = #{serviceId}\n" +
			"and si.effective_status = '1'\n" +
			"and smr.card_type = #{cardType}\n" +
			"and smr.holidays_mark = #{holidaysMark}\n" +
			"and smr.agent_no = '0'\n" +
			"and si2.service_type = #{serviceType2}\n" +
			"and abp.agent_no = #{agentNo}" +
			"ORDER BY smr.single_num_amount\n" +
			"limit 1")
	@ResultType(ServiceRate.class)
	ServiceRate queryMinTXServiceRate(ServiceRate rate);

	@Select("select service_id,service_type from service_info where link_service = #{serviceId}")
	@ResultType(ServiceInfo.class)
	ServiceInfo queryJyServiceByTx(@Param("serviceId") Long serviceId);

	@Select("select service_id,service_name from service_info where effective_status = 1 order by convert(service_name using gbk)")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> selectServiceName();

	@Select("select single_num_amount from service_manage_rate where service_id=#{serviceId} and agent_no='0' and card_type=#{cardType} and holidays_mark=#{holidayMark}")
    @ResultType(BigDecimal.class)
    BigDecimal getRateByCardType(@Param("cardType") Integer cardType,@Param("serviceId")Long serviceId,@Param("holidayMark") String holidayMark);

    @SelectProvider(type=SqlProvider.class, method="getServiceInfoToExport")
    @ResultType(ServiceInfo.class)
    List<ServiceInfo> getServiceInfoToExport(@Param("info")Map<String, Object> jsonMap);

	/*@Select("SELECT smr.* " +
			"FROM service_manage_rate smr " +
			"WHERE smr.service_id=#{rate.serviceId} " +
			"AND smr.agent_no=#{rate.agentNo} " +
			"AND smr.holidays_mark=#{rate.holidaysMark} " +
			"AND smr.card_type=#{rate.cardType}")
	@ResultType(ServiceRate.class)
	ServiceRate selectServiceManageRate(@Param("rate") ServiceRate rate);*/


	public class SqlProvider{
		public String getServiceInfoToExport(final Map<String, Object> map){
			final Map<String,Object> param = (Map<String,Object>)map.get("info");
			return new SQL(){{
				SELECT("s.*,sd.sys_name serviceTypeName,s1.service_name linkServiceName");
				FROM("service_info s");
				LEFT_OUTER_JOIN(" sys_dict sd on sd.sys_value = s.service_type and sd.sys_key='SERVICE_TYPE' ");
				LEFT_OUTER_JOIN("service_info s1 on s1.service_id = s.link_service");
				//LEFT_OUTER_JOIN(" service_manage_rate sr on sr.service_id=s.service_id");
				if(param.get("serviceName")!=null&&StringUtils.isNotBlank(param.get("serviceName").toString())){
					param.put("serviceName","%" + param.get("serviceName") + "%");
					WHERE("s.service_name like #{info.serviceName}");
				}
				if(param.get("serviceId")!=null&&StringUtils.isNotBlank(param.get("serviceId").toString())){
					param.put("serviceId", param.get("serviceId") );
					WHERE("s.service_id = #{info.serviceId}");
				}
				if(param.get("tFlag")!=null&&StringUtils.isNotBlank(param.get("tFlag").toString())){
					param.put("tFlag", param.get("tFlag") );
					WHERE("s.t_flag = #{info.tFlag}");
				}

				if(param.get("agentShowName")!=null&&StringUtils.isNotBlank(param.get("agentShowName").toString())){
					param.put("agentShowName","%" + param.get("agentShowName") + "%");
					WHERE("s.agent_show_name like #{info.agentShowName}");
				}
				if(param.get("serviceType")!=null&&StringUtils.isNotBlank(param.get("serviceType").toString()))
					WHERE("s.service_type=#{info.serviceType}");
				if(param.get("rateCard")!=null&&StringUtils.isNotBlank(param.get("rateCard").toString()))
					WHERE("s.rate_card=#{info.rateCard}");
				if(param.get("rateHolidays")!=null&&StringUtils.isNotBlank(param.get("rateHolidays").toString()))
					WHERE("s.rate_holidays=#{info.rateHolidays}");
				if(param.get("quotaCard")!=null&&StringUtils.isNotBlank(param.get("quotaCard").toString()))
					WHERE("s.quota_card=#{info.quotaCard}");
				if(param.get("quotaHolidays")!=null&&StringUtils.isNotBlank(param.get("quotaHolidays").toString()))
					WHERE("s.quota_holidays=#{info.quotaHolidays}");
				if(param.get("fixedRate")!=null&&StringUtils.isNotBlank(param.get("fixedRate").toString()))
					WHERE("s.fixed_rate=#{info.fixedRate}");
				if(param.get("fixedQuota")!=null&&StringUtils.isNotBlank(param.get("fixedQuota").toString()))
					WHERE("s.fixed_quota=#{info.fixedQuota}");
				if(param.get("createTimeBegin")!=null&&StringUtils.isNotBlank(param.get("createTimeBegin").toString())){
					WHERE("s.create_time >= #{info.createTimeBegin}");
				}
				if(param.get("createTimeEnd")!=null&&StringUtils.isNotBlank(param.get("createTimeEnd").toString()))
					WHERE("s.create_time <= #{info.createTimeEnd}");
				if(param.get("createPerson")!=null&&StringUtils.isNotBlank(param.get("createPerson").toString()))
					WHERE("s.create_person like  CONCAT('%',#{info.createPerson},'%')");

				String str="";
				if(param.get("rateCheckStatus")!=null&&StringUtils.isNotBlank(param.get("rateCheckStatus").toString())){
					str=" and sr.check_status=#{info.rateCheckStatus} ";
				}
				if(param.get("rateLockStatus")!=null&&StringUtils.isNotBlank(param.get("rateLockStatus").toString())){
					str+=" and sr.lock_status=#{info.rateLockStatus} ";
				}
				if(StringUtils.isNotBlank(str)){
					WHERE("s.service_id in (SELECT sr.service_id FROM service_manage_rate sr WHERE s.service_id =sr.service_id "+str+")");
				}
				str="";
				if(param.get("quotaCheckStatus")!=null&&StringUtils.isNotBlank(param.get("quotaCheckStatus").toString())){
					str=" and sq.check_status=#{info.quotaCheckStatus} ";
				}
				if(param.get("quotaLockStatus")!=null&&StringUtils.isNotBlank(param.get("quotaLockStatus").toString())){
					str+=" and sq.lock_status=#{info.quotaLockStatus} ";
				}
				if(StringUtils.isNotBlank(str)){
					WHERE("s.service_id in (SELECT sq.service_id FROM service_manage_quota sq WHERE s.service_id =sq.service_id "+str+")");
				}
				if(param.get("effectiveStatus")!=null&&StringUtils.isNotBlank(param.get("effectiveStatus").toString()))
					WHERE("s.effective_status=#{info.effectiveStatus}");
			}}.toString();
		}

		public String getServiceInfo(final Map<String, Object> map){
			final Map<String,Object> param = (Map<String,Object>)map.get("info");
			return new SQL(){{
				SELECT("s.*,sd.sys_name serviceTypeName");
				FROM("service_info s");
				LEFT_OUTER_JOIN(" sys_dict sd on sd.sys_value = s.service_type and sd.sys_key='SERVICE_TYPE' ");
				//LEFT_OUTER_JOIN(" service_manage_rate sr on sr.service_id=s.service_id");
				if(param.get("serviceName")!=null&&StringUtils.isNotBlank(param.get("serviceName").toString())){
					param.put("serviceName","%" + param.get("serviceName") + "%");
					WHERE("s.service_name like #{info.serviceName}");
				}
				if(param.get("serviceId")!=null&&StringUtils.isNotBlank(param.get("serviceId").toString())){
					param.put("serviceId", param.get("serviceId") );
					WHERE("s.service_id = #{info.serviceId}");
				}
				if(param.get("tFlag")!=null&&StringUtils.isNotBlank(param.get("tFlag").toString())){
					param.put("tFlag", param.get("tFlag") );
					WHERE("s.t_flag = #{info.tFlag}");
				}

				if(param.get("agentShowName")!=null&&StringUtils.isNotBlank(param.get("agentShowName").toString())){
					param.put("agentShowName","%" + param.get("agentShowName") + "%");
					WHERE("s.agent_show_name like #{info.agentShowName}");
				}
				if(param.get("serviceType")!=null&&StringUtils.isNotBlank(param.get("serviceType").toString()))
					WHERE("s.service_type=#{info.serviceType}");
				if(param.get("rateCard")!=null&&StringUtils.isNotBlank(param.get("rateCard").toString()))
					WHERE("s.rate_card=#{info.rateCard}");
				if(param.get("rateHolidays")!=null&&StringUtils.isNotBlank(param.get("rateHolidays").toString()))
					WHERE("s.rate_holidays=#{info.rateHolidays}");
				if(param.get("quotaCard")!=null&&StringUtils.isNotBlank(param.get("quotaCard").toString()))
					WHERE("s.quota_card=#{info.quotaCard}");
				if(param.get("quotaHolidays")!=null&&StringUtils.isNotBlank(param.get("quotaHolidays").toString()))
					WHERE("s.quota_holidays=#{info.quotaHolidays}");
				if(param.get("fixedRate")!=null&&StringUtils.isNotBlank(param.get("fixedRate").toString()))
					WHERE("s.fixed_rate=#{info.fixedRate}");
				if(param.get("fixedQuota")!=null&&StringUtils.isNotBlank(param.get("fixedQuota").toString()))
					WHERE("s.fixed_quota=#{info.fixedQuota}");
				if(param.get("createTimeBegin")!=null&&StringUtils.isNotBlank(param.get("createTimeBegin").toString())){
					WHERE("s.create_time >= #{info.createTimeBegin}");
				}
				if(param.get("createTimeEnd")!=null&&StringUtils.isNotBlank(param.get("createTimeEnd").toString()))
					WHERE("s.create_time <= #{info.createTimeEnd}");
				if(param.get("createPerson")!=null&&StringUtils.isNotBlank(param.get("createPerson").toString()))
					WHERE("s.create_person like  CONCAT('%',#{info.createPerson},'%')");

				String str="";
				if(param.get("rateCheckStatus")!=null&&StringUtils.isNotBlank(param.get("rateCheckStatus").toString())){
					str=" and sr.check_status=#{info.rateCheckStatus} ";
				}
				if(param.get("rateLockStatus")!=null&&StringUtils.isNotBlank(param.get("rateLockStatus").toString())){
					str+=" and sr.lock_status=#{info.rateLockStatus} ";
				}
				if(StringUtils.isNotBlank(str)){
					WHERE("s.service_id in (SELECT sr.service_id FROM service_manage_rate sr WHERE s.service_id =sr.service_id "+str+")");
				}
				str="";
				if(param.get("quotaCheckStatus")!=null&&StringUtils.isNotBlank(param.get("quotaCheckStatus").toString())){
					str=" and sq.check_status=#{info.quotaCheckStatus} ";
				}
				if(param.get("quotaLockStatus")!=null&&StringUtils.isNotBlank(param.get("quotaLockStatus").toString())){
					str+=" and sq.lock_status=#{info.quotaLockStatus} ";
				}
				if(StringUtils.isNotBlank(str)){
					WHERE("s.service_id in (SELECT sq.service_id FROM service_manage_quota sq WHERE s.service_id =sq.service_id "+str+")");
				}
				if(param.get("effectiveStatus")!=null&&StringUtils.isNotBlank(param.get("effectiveStatus").toString()))
					WHERE("s.effective_status=#{info.effectiveStatus}");
			}}.toString();
		}
		
		public String getServiceInfoWithDetail(Map<String,Object> param){
			final ServiceInfo info=(ServiceInfo)param.get("info");
			return new SQL(){{
				SELECT("id,service_id,service_name,service_type,hardware_is,bank_card,exclusive,business,sale_starttime,sale_endtime,use_starttime,"
						+ "use_endtime,proxy,getcash_id,rate_card,rate_holidays,quota_holidays,quota_card,oem_id,remark,t_flag,cash_subject");
				FROM("service_info");
				if(StringUtils.isNotBlank(info.getServiceName())){
					WHERE("service_name=#{info.serviceName}");
				}
				if(info.getServiceType()==null){
					WHERE("service_type=#{info.serviceType}");
				}
			}}.toString();
		}
		public String insertServiceRateList(Map<String, List<ServiceRate>> param){
			List<ServiceRate> list = param.get("list");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert into service_manage_rate(service_id,holidays_mark,card_type,quota_level,agent_no,rate_type,single_num_amount,rate,capping,safe_line,is_global,check_status,lock_status,ladder1_rate,"
            		+ "ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,ladder3_max,ladder4_rate,ladder4_max) values");
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].serviceId},#'{'list[{0}].holidaysMark},#'{'list[{0}].cardType},#'{'list[{0}].quotaLevel},#'{'list[{0}].agentNo},#'{'list[{0}].rateType},"
            		+ "#'{'list[{0}].singleNumAmount},#'{'list[{0}].rate},#'{'list[{0}].capping},#'{'list[{0}].safeLine},#'{'list[{0}].isGlobal},#'{'list[{0}].checkStatus},#'{'list[{0}].lockStatus},#'{'list[{0}].ladder1Rate},"
            		+ "#'{'list[{0}].ladder1Max},#'{'list[{0}].ladder2Rate},#'{'list[{0}].ladder2Max},#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
		}
		
		public String insertServiceQuotaList(Map<String, List<ServiceQuota>> param){
			List<ServiceQuota> list = param.get("list");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert into service_manage_quota(service_id,holidays_mark,card_type,quota_level,agent_no,single_day_amount,single_min_amount,single_count_amount,single_daycard_amount,single_daycard_count,check_status,lock_status,is_global) values");
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].serviceId},#'{'list[{0}].holidaysMark},#'{'list[{0}].cardType},#'{'list[{0}].quotaLevel},#'{'list[{0}].agentNo},#'{'list[{0}].singleDayAmount}"
            		+ ",#'{'list[{0}].singleMinAmount},#'{'list[{0}].singleCountAmount},#'{'list[{0}].singleDaycardAmount},#'{'list[{0}].singleDaycardCount},#'{'list[{0}].checkStatus},#'{'list[{0}].lockStatus},#'{'list[{0}].isGlobal})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
		}
		
		public String existServiceName(Map<String, Object> param){
			final ServiceInfo serviceInfo = (ServiceInfo) param.get("info");
            return new SQL(){{
            	SELECT("count(1)");
            	FROM("service_info");
            	WHERE("service_name=#{info.serviceName}");
            	if(serviceInfo.getServiceId()!=null && serviceInfo.getServiceId()!=0){
            		WHERE("service_id<>#{info.serviceId}");
            	}
            }}.toString();
		}

		public String existAgentShowName(Map<String, Object> param){
			final ServiceInfo serviceInfo = (ServiceInfo) param.get("info");
            return new SQL(){{
            	SELECT("count(1)");
            	FROM("service_info");
            	WHERE("agent_show_name=#{info.agentShowName}");
            	if(serviceInfo.getServiceId()!=null && serviceInfo.getServiceId()!=0){
            		WHERE("service_id<>#{info.serviceId}");
            	}
            }}.toString();
		}

//		public String getServiceQuotaByIds(Map<String, Object> param){
//			final List<Long> sqIds = (List<Long>) param.get("list");
//			StringBuilder sb = new StringBuilder();
//			sb.append("select * from service_manage_quota where id in (");
//			MessageFormat messageFormat = new MessageFormat("#'{'list[{0}]},");
//			for(int i=0; i<sqIds.size();i++){
//				sb.append(messageFormat.format(new Integer[]{i}));
//			}
//			sb.replace(sb.length()-1, sb.length(), ")");
//			return sb.toString();
//		}
		
//		public String getServiceRateByIds(Map<String, Object> param){
//			final List<Long> sqIds = (List<Long>) param.get("list");
//			StringBuilder sb = new StringBuilder();
//			sb.append("select * from service_manage_rate where id in (");
//			MessageFormat messageFormat = new MessageFormat("#'{'list[{0}]},");
//			for(int i=0; i<sqIds.size();i++){
//				sb.append(messageFormat.format(new Integer[]{i}));
//			}
//			sb.replace(sb.length()-1, sb.length(), ")");
//			System.out.println(sb.toString());
//			return sb.toString();
//		}
	}

	//=========代理商分润设置 =====================
	@Select("SELECT si.service_id,si.service_name,smr.card_type,smr.holidays_mark FROM service_manage_rate smr "
			+ "LEFT JOIN service_info si ON smr.service_id = si.service_id WHERE smr.agent_no=0 AND si.service_id = #{serviceId}")
	@ResultType(AgentShareRule.class)
	List<AgentShareRule> queryAgentProfit(Long serviceId);
	
	@Select("SELECT asr.*, si.service_name,si.service_type FROM agent_share_rule asr "
			+ "LEFT JOIN service_info si ON asr.service_id=si.service_id WHERE asr.agent_no=0 AND asr.service_id = #{serviceId}")
	@ResultType(AgentShareRule.class)
	List<AgentShareRule> queryByParams(Long serviceId);
	
	@Select("SELECT * FROM agent_share_rule where agent_no = 0 and service_id = #{serviceId}")
	@ResultType(AgentShareRule.class)
	List<AgentShareRule> queryByAgentNo(@Param("serviceId")Long serviceId);
	//=======================================

	

}
