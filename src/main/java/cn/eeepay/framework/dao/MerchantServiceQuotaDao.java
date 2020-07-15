package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.MerchantServiceQuota;

public interface MerchantServiceQuotaDao {
	
    @Update("update merchant_service_quota set single_day_amount=#{record.singleDayAmount},"
    		+ "single_count_amount=#{record.singleCountAmount},single_min_amount=#{record.singleMinAmount}"
    		+ ",single_daycard_amount=#{record.singleDaycardAmount},single_daycard_count=#{record.singleDaycardCount} "
    		+ "where id=#{record.id}")
    int updateByPrimaryKey(@Param("record")MerchantServiceQuota record);
    
    @Select("SELECT msq.*,sis.service_name FROM merchant_service_quota msq "
    		+ "LEFT JOIN service_info sis on sis.service_id=msq.service_id "
    		+ "where msq.merchant_no=#{merId} and msq.service_id=#{serId}")
    @ResultType(MerchantServiceQuota.class)
    List<MerchantServiceQuota> selectByMertIdAndServiceId(@Param("merId")String merId,@Param("serId")String serId);
    
    @SelectProvider(type=SqlProvider.class,method="findMsqList")
    @ResultType(MerchantServiceQuota.class)
    List<MerchantServiceQuota> selectByMertId(@Param("record")MerchantServiceQuota record);
    
    public class SqlProvider{
    	public String findMsqList(Map<String,Object> param){
			final MerchantServiceQuota record=(MerchantServiceQuota)param.get("record");
			return new SQL(){{
				SELECT("msq.*,sis.service_name");
				FROM("merchant_service_quota msq "
						+ "LEFT JOIN service_info sis on sis.service_id=msq.service_id ");
				if(StringUtils.isNotBlank(record.getMerchantNo())){
					WHERE(" msq.merchant_no=#{record.merchantNo}");
				}
			}}.toString();
		}
    	
    }
}