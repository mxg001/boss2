package cn.eeepay.framework.dao.capitalInsurance;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.model.capitalInsurance.ShareReportTotal;
import cn.eeepay.framework.util.StringUtil;

public interface ShareReporDao {
	/**
	 * log
	 */
	Logger log = LoggerFactory.getLogger(ShareReporDao.class);
	
	/**
	 * 
	 * @param tis
	 * @param page
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(ShareReport.class)
	List<ShareReport>  queryAllInfo(@Param("tis") ShareReport tis, @Param("page") Page<ShareReport> page);

	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(ShareReport.class)
	List<ShareReport> importDetailSelect(@Param("tis")ShareReport tis);

	/**
	 * 
	 * @param tis
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectMoneyInfo")
	@ResultType(ShareReportTotal.class)
	ShareReportTotal queryAmount(@Param("tis") ShareReport tis);
	
	/**
	 * 
	 * @param reportMonth
	 * @return
	 */
	@Select("select count(0) from zjx_share_report  where bill_month=#{reportMonth}")
	@ResultType(Integer.class)
	Integer isInitShareReport(@Param("reportMonth") String reportMonth);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	@Insert("insert into zjx_share_report (batch_no,bill_month,one_agent_no,total_amount,total_count,share_rate,share_amount,create_person,create_time)"
			+ " values(#{report.batchNo},#{report.billMonth},#{report.oneAgentNo},#{report.totalAmount},#{report.totalCount},#{report.shareRate},#{report.shareAmount},#{report.createPerson},now())")
	@ResultType(Integer.class)
	Integer initShareReport(@Param("report") ShareReport report);

	/**
	 * 
	 * SqlProvider
	 *
	 */
	public class SqlProvider{
		public String selectAllInfo(Map<String,Object> param){
    		final ShareReport tis = (ShareReport)param.get("tis");
    			StringBuilder sb = new StringBuilder();
    			sb.append(" SELECT sr.*,agent.agent_name as one_agent_name,agent.agent_type as agent_type  from zjx_share_report sr left join agent_info agent on agent.agent_no = sr.one_agent_no ");
    			whereSQL(sb,tis);
    			sb.append(" order by sr.create_time desc ");
				return sb.toString();
    	}
		public String selectMoneyInfo(Map<String,Object> param){
			final ShareReport tis = (ShareReport)param.get("tis");
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT sum(share_amount) as total_amount,"
					+ " sum(case when account_status ='1' then share_amount else 0 end) as account_amount,"
					+ " sum(case when account_status in('3','2') then share_amount else 0 end ) as unAccount_amount"
					+ " from zjx_share_report sr left join agent_info agent on agent.agent_no = sr.one_agent_no ");
			whereSQL(sb,tis);
			return sb.toString();
    	}
		
		private void whereSQL(StringBuilder sb, ShareReport tis) {
			sb.append(" where 1=1");
			if(StringUtils.isNotBlank(tis.getBatchNo())){
				sb.append(" and sr.batch_no=#{tis.batchNo}");
			}
			if(StringUtils.isNotBlank(tis.getOneAgentNo())){
				sb.append(" and sr.one_agent_no=#{tis.oneAgentNo}");
			}
			if(StringUtils.isNotBlank(tis.getOneAgentName())){
				sb.append(" and agent.agent_name=#{tis.oneAgentName}");
			}
			if(tis.getsShareAmount()!=null && tis.getsShareAmount().doubleValue()!=0){
				sb.append(" and sr.share_amount>=#{tis.sShareAmount}");
			}
			if(tis.geteShareAmount()!=null && tis.geteShareAmount().doubleValue()!=0){
				sb.append(" and sr.share_amount<=#{tis.eShareAmount}");
			}
			if(StringUtils.isNotBlank(tis.getBillMonth())){
				sb.append(" and sr.bill_month=#{tis.billMonth}");
			}
			if(tis.getsAccountTime()!=null){
				sb.append(" and sr.account_time>=#{tis.sAccountTime}");
			}
			if(tis.geteAccountTime()!=null ){
				sb.append(" and sr.account_time<=#{tis.eAccountTime}");
			}
			if(tis.getAccountStatus()!=null && tis.getAccountStatus()!=0){
				sb.append(" and sr.account_status=#{tis.accountStatus}");
			}
			if(!StringUtil.isBlank(tis.getAgentType())){
				sb.append(" and agent.agent_type=#{tis.agentType} ");
			}
		}
	}
	@Select("select batch_no,one_agent_no,share_amount from zjx_share_report  where id=#{id}")
	@ResultType(ShareReport.class)
	ShareReport getShareReport(@Param("id")Integer id);
	
	@Update("update zjx_share_report set account_status =#{status},account_time=now() where id=#{id}")
	@ResultType(Integer.class)
	int updateAccountStatus(@Param("id")Integer id,@Param("status")Integer status);
}
