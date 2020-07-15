package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.PosCardBin;
import cn.eeepay.framework.model.PosCnaps;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface PosCardBinDao {

	@Select("select * from pos_card_bin c where c.card_length = length(#{accountNo}) AND c.verify_code = left(#{accountNo},c.verify_length)"
			+ " order by c.verify_length desc limit 1")
	@ResultType(PosCardBin.class)
	PosCardBin queryInfo(@Param("accountNo")String accountNo);

	@Select("select * from pos_card_bin c where c.card_length = length(#{accountNo}) AND c.verify_code = left(#{accountNo},c.verify_length)")
	@ResultType(PosCardBin.class)
	List<PosCardBin> queryAllInfo(@Param("accountNo")String accountNo);
	         
	@Select("select bank_no from pos_card_bin c  where  c.card_length = length(#{accountNo}) AND c.verify_code = left(#{accountNo},  c.verify_length)")
	@ResultType(PosCardBin.class)
	String queryBankNo(@Param("accountNo")String accountNo);

	@SelectProvider(type=SqlProvider.class,method="getCarBinList")
	@ResultType(Map.class)
	List<Map<String, Object>> getCarBinList(@Param("cardType") int cardType);

	@Select("select id, cnaps_no ,bank_name  from pos_cnaps" +
			" where bank_name like #{bankName} and bank_name like #{cityName}")
	@ResultType(PosCnaps.class)
	List<PosCnaps> getPosCnapsList(@Param("bankName")String bankName ,@Param("cityName")String cityName);

	@Select("select cnaps_no from pos_cnaps where bank_name = #{bankName}")
	@ResultType(String.class)
	String getPoscnapsNoByBankName(@Param("bankName")String bankName);
	
	public class SqlProvider{
    	public String getCarBinList(Map<String,Object> param){
    		final int cardType = Integer.valueOf(param.get("cardType").toString());
    		return new SQL(){{
    			SELECT(" bank_no as id ,bank_name ");
    			FROM("pos_card_bin ");
    			if(cardType != 0){
    				if(cardType == 1){
    					WHERE("card_type ='贷记卡' ");
    				}else{
    					WHERE("card_type ='借记卡' ");
    				}
    				
    				WHERE("bank_no is not null and  bank_no <> '--'");
    			}
    			GROUP_BY(" bank_no");
    			ORDER_BY(" bank_name");
    		}}.toString();
    	}
    	
    }
}
