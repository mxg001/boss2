package cn.eeepay.framework.daoSuperbank;

import org.apache.ibatis.annotations.Insert;

import cn.eeepay.framework.model.CreditloanEnterData;



public interface CreditloanEnterDataDao {

	/**新增数据分析记录*/
	@Insert("insert into creditloan_enter_data" +
			"(analysis_code,login_source,business_source,credit_bank,loan_source,open_type,day_pv_num,week_pv_num,month_pv_num,day_uv_num,week_uv_num,month_uv_num,org_id,analysis_date) " +
			" values " +
			" (#{analysisCode},#{loginSource},#{businessSource},#{creditBank},#{loanSource},#{openType},#{dayPvNum},#{weekPvNum},#{monthPvNum},#{dayUvNum},#{weekUvNum},#{monthUvNum},#{orgId},DATE_SUB(curdate(),INTERVAL 1 DAY))")
	int saveCreditloanEnterData(CreditloanEnterData record);
	
	
	
	
	
}
