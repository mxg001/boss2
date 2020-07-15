package cn.eeepay.framework.dao;
import cn.eeepay.framework.model.MerchantServiceRate;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

public interface MerchantServiceRateDao {
	
	@Delete("delete from merchant_service_rate where id=#{id}")
    int deleteByPrimaryKey(@Param("id")Long id);

	@Insert("insert merchant_service_rate set service_id=#{record.serviceId},card_type=#{record.cardType}"
			+ ",holidays_mark=#{record.holidaysMark},merchant_id=#{record.merchantId}"
			+ ",efficient_date=#{record.efficientDate},useable=#{record.useable},fixed_mark=#{record.fixedMark}"
			+ ",rate_type=#{record.rateType},single_num_amount=#{record.singleNumAmount},rate=#{record.rate},capping=#{record.capping}"
			+ ",safe_line=#{record.safeLine},ladder1_rate=#{record.ladder1Rate},ladder1_max=#{record.ladder1Max}"
			+ ",ladder2_rate=#{record.ladder2Rate},ladder2_max=#{record.ladder2Max}"
			+ ",ladder3_rate=#{record.ladder3Rate},ladder3_max=#{record.ladder3Max}"
			+ ",ladder4_rate=#{record.ladder4Rate},ladder4_max=#{record.ladder4Max}")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(@Param("record")MerchantServiceRate record);

    //用于商户费率新增查询
    @Select("select * from merchant_service_rate "
    		+ "where merchant_no=#{record.merchantNo} and service_id=#{record.serviceId} and card_type=#{record.cardType} "
    		+ "and holidays_mark=#{record.holidaysMark} and efficient_date=#{record.efficientDate}")
    MerchantServiceRate addSelectInfo(@Param("record")MerchantServiceRate record);
    
    @Update("update merchant_service_rate set rate_type=#{record.rateType},single_num_amount=#{record.singleNumAmount}"
    		+ ",rate=#{record.rate},"
    		+ "capping=#{record.capping},safe_line=#{record.safeLine},ladder1_rate=#{record.ladder1Rate}"
    		+ ",ladder1_max=#{record.ladder1Max},"
    		+ "ladder2_rate=#{record.ladder2Rate},ladder2_max=#{record.ladder2Max}"
    		+ ",ladder3_rate=#{record.ladder3Rate},ladder3_max=#{record.ladder3Max}"
    		+ ",ladder4_rate=#{record.ladder4Rate},ladder4_max=#{record.ladder4Max} where id=#{record.id}")
    int updateByPrimaryKey(@Param("record")MerchantServiceRate record);
    
    @Select("SELECT msr.*,sis.service_name FROM merchant_service_rate msr "
    		+ "LEFT JOIN service_info sis on sis.service_id=msr.service_id "
    		+ "where msr.merchant_no=#{merId} and msr.service_id=#{serId}")
    @ResultType(MerchantServiceRate.class)
    List<MerchantServiceRate> selectByMertIdAndSerivceId(@Param("merId")String merId,@Param("serId")String serId);
    
    @SelectProvider(type=SqlProvider.class,method="findMsrList")
    @ResultType(MerchantServiceRate.class)
    List<MerchantServiceRate> selectByMertId(@Param("record")MerchantServiceRate record);
    
    public class SqlProvider{
    	public String findMsrList(Map<String,Object> param){
			final MerchantServiceRate record=(MerchantServiceRate)param.get("record");
			return new SQL(){{
				SELECT("msr.*,sis.service_name");
				FROM("merchant_service_rate msr "
						+ "LEFT JOIN service_info sis on sis.service_id=msr.service_id ");
				if(StringUtils.isNotBlank(record.getMerchantNo())){
					WHERE(" msr.merchant_no=#{record.merchantNo}");
				}
			}}.toString();
		}
    	
    }
}