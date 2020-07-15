package cn.eeepay.framework.daoSuperbank;

import org.apache.ibatis.annotations.Insert;

import cn.eeepay.framework.model.BusinessPageViewData;



public interface BusinessPageViewDataDao {

	/**新增数据分析记录*/
	@Insert("insert into business_page_view_data" +
			"(analysis_code,login_source,business_source,open_type,day_pv_num,week_pv_num,month_pv_num,day_uv_num,week_uv_num,month_uv_num,org_id,analysis_date) " +
			" values " +
			" (#{analysisCode},#{loginSource},#{businessSource},#{openType},#{dayPvNum},#{weekPvNum},#{monthPvNum},#{dayUvNum},#{weekUvNum},#{monthUvNum},#{orgId},DATE_SUB(curdate(),INTERVAL 1 DAY))")
	int saveBusinessPageViewData(BusinessPageViewData record);
	
	
	
	
	
}
