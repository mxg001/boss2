package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransInfoPreFrozenDao {

	Logger log = LoggerFactory.getLogger(TransInfoPreFrozenDao.class);

	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(TransInfoFreezeQueryCollection.class)
	List<TransInfoFreezeQueryCollection> queryAllInfo(@Param("transInfo") TransInfoFreezeQueryCollection transInfo, @Param("page") Page<TransInfoFreezeQueryCollection> page);

	@SelectProvider(type=SqlProvider.class,method="importAllInfo")
	@ResultType(TransInfoFreezeQueryCollection.class)
	List<TransInfoFreezeQueryCollection> importAllInfo(@Param("transInfo")TransInfoFreezeQueryCollection transInfo);


	public class SqlProvider{
		public String selectAllInfo(Map<String,Object> param){
			final TransInfoFreezeQueryCollection transInfo=(TransInfoFreezeQueryCollection)param.get("transInfo");
			StringBuilder sb = new StringBuilder();
			//区别查询方式  0-预冻结   1-冻结/解冻
			if(transInfo.getQueryMode().equals("0")) {
				sb.append(" select cto.merchant_no, mi.merchant_name, mi.mobilephone,cto.oper_log as operstr, cto.oper_time, cto.oper_name, '2' as operType, cto.oper_log, cto.pre_freeze_note as operReason, mi.status as merStatus, mi.risk_status from trans_info_pre_freeze_log cto left join merchant_info mi on cto.merchant_no =mi.merchant_no ");
				sb.append(" where 1=1 ");
				if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
					sb.append(" and cto.merchant_no=#{transInfo.merchantNo}");
				}
				if(StringUtils.isNotBlank(transInfo.getMobilephone())){
					sb.append(" and mi.mobilephone=#{transInfo.mobilephone}");
				}
				if(StringUtils.isNotBlank(transInfo.getMerStatus())){
					sb.append(" and mi.status=#{transInfo.merStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getRiskStatus())){
					sb.append(" and mi.risk_status=#{transInfo.riskStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getsTime())){
					sb.append(" and cto.oper_time>=#{transInfo.sTime}");
				}
				if(StringUtils.isNotBlank(transInfo.geteTime())){
					sb.append(" and cto.oper_time<=#{transInfo.eTime}");
				}
			} else{
				sb.append(" select coo.merchant_no, mi.merchant_name, mi.mobilephone, coo.trans_amount as operMoney, cto.oper_time, cto.oper_name, cto.oper_type, cto.oper_reason as oper_log, cto.order_no, mi.status as merStatus, mi.risk_status  from trans_info_freeze_new_log cto left join collective_trans_order coo on coo.order_no= cto.order_no left join merchant_info mi on coo.merchant_no =mi.merchant_no");
				sb.append(" where 1=1 ");
				if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
					sb.append(" and coo.merchant_no=#{transInfo.merchantNo}");
				}
				if(StringUtils.isNotBlank(transInfo.getMobilephone())){
					sb.append(" and mi.mobilephone=#{transInfo.mobilephone}");
				}
				if(StringUtils.isNotBlank(transInfo.getMerStatus())){
					sb.append(" and mi.status=#{transInfo.merStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getRiskStatus())){
					sb.append(" and mi.risk_status=#{transInfo.riskStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getsTime())){
					sb.append(" and cto.oper_time>=#{transInfo.sTime}");
				}
				if(StringUtils.isNotBlank(transInfo.geteTime())){
					sb.append(" and cto.oper_time<=#{transInfo.eTime}");
				}
			}
			sb.append(" order by cto.oper_time desc ");
			log.info("商户交易预冻结/解冻、冻结查询sql：" + sb.toString());
			return sb.toString();
		}


		public String importAllInfo(Map<String,Object> param){
			final TransInfoFreezeQueryCollection transInfo=(TransInfoFreezeQueryCollection)param.get("transInfo");
			StringBuilder sb = new StringBuilder();
			//区别查询方式  0-预冻结   1-冻结/解冻
			if(transInfo.getQueryMode().equals("0")) {
				sb.append(" select cto.merchant_no, mi.merchant_name, mi.mobilephone,cto.oper_log as operstr, cto.oper_time, cto.oper_name, '2' as operType, cto.oper_log, cto.pre_freeze_note as operReason, mi.status as merStatus, mi.risk_status from trans_info_pre_freeze_log cto left join merchant_info mi on cto.merchant_no =mi.merchant_no ");
				sb.append(" where 1=1 ");
				if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
					sb.append(" and cto.merchant_no=#{transInfo.merchantNo}");
				}
				if(StringUtils.isNotBlank(transInfo.getMobilephone())){
					sb.append(" and mi.mobilephone=#{transInfo.mobilephone}");
				}
				if(StringUtils.isNotBlank(transInfo.getMerStatus())){
					sb.append(" and mi.status=#{transInfo.merStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getRiskStatus())){
					sb.append(" and mi.risk_status=#{transInfo.riskStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getsTime())){
					sb.append(" and cto.oper_time>=#{transInfo.sTime}");
				}
				if(StringUtils.isNotBlank(transInfo.geteTime())){
					sb.append(" and cto.oper_time<=#{transInfo.eTime}");
				}
			} else{
				sb.append(" select coo.merchant_no, mi.merchant_name, mi.mobilephone, coo.trans_amount as operMoney, cto.oper_time, cto.oper_name, cto.oper_type, cto.oper_reason as oper_log, cto.order_no, mi.status as merStatus, mi.risk_status  from trans_info_freeze_new_log cto left join collective_trans_order coo on coo.order_no= cto.order_no left join merchant_info mi on coo.merchant_no =mi.merchant_no");
				sb.append(" where 1=1 ");
				if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
					sb.append(" and coo.merchant_no=#{transInfo.merchantNo}");
				}
				if(StringUtils.isNotBlank(transInfo.getMobilephone())){
					sb.append(" and mi.mobilephone=#{transInfo.mobilephone}");
				}
				if(StringUtils.isNotBlank(transInfo.getMerStatus())){
					sb.append(" and mi.status=#{transInfo.merStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getRiskStatus())){
					sb.append(" and mi.risk_status=#{transInfo.riskStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getsTime())){
					sb.append(" and cto.oper_time>=#{transInfo.sTime}");
				}
				if(StringUtils.isNotBlank(transInfo.geteTime())){
					sb.append(" and cto.oper_time<=#{transInfo.eTime}");
				}
			}
			sb.append(" order by cto.oper_time desc ");
			log.info("商户交易预冻结/解冻、冻结查询sql：" + sb.toString());
			return sb.toString();
		}
	}

}
