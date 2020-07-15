package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AuthCard;
import cn.eeepay.framework.util.StringUtil;

public interface AuthCardDao {
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(AuthCard.class)
	List<AuthCard> selectAllInfo(@Param("page")Page<AuthCard> page,@Param("rr")AuthCard param);
	
	
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(AuthCard.class)
	List<AuthCard> authExport(@Param("rr")AuthCard param);
	
	@SelectProvider(type=SqlProvider.class,method="authCardTotal")
	@ResultType(Map.class)
	Map<String, Object> authCardTotal(@Param("rr")AuthCard param);
	
	
	 public class SqlProvider{
	    	
	    	public String selectAllInfo(Map<String,Object> param){
	    		final AuthCard rr=(AuthCard)param.get("rr");
	    		return new SQL(){{
	    			SELECT("rr.*,lawyer,m.merchant_name");
	    			FROM(" real_auth rr " +
	    					"left join merchant_info m on rr.merchant_no = m.merchant_no");
	    			if(!StringUtil.isBlank(rr.getMerchantNo())){
	    				WHERE(" rr.merchant_no=#{rr.merchantNo}");
	    			}
	    			if(!StringUtil.isBlank(rr.getMerchantName())){
	    				WHERE(" m.merchant_name=#{rr.merchantName}");
	    			}
	    			if(!StringUtil.isBlank(rr.getUserName())){
	    				WHERE(" rr.user_name=#{rr.userName}");
	    			}
	    			if(!StringUtil.isBlank(rr.getCardNo())){
	    				WHERE(" rr.card_no=#{rr.cardNo}");
	    			}
	    			if(rr.getSdate()!=null){
						WHERE("rr.create_time>=#{rr.sdate}");
					}
					if(rr.getEdate()!=null){
						WHERE("rr.create_time<#{rr.edate}");
					}
					if(!StringUtil.isBlank(rr.getStatus())){
						WHERE(" rr.status=#{rr.status}");
					}
	    		}}.toString();
	    	}
	    	
	    	
	    	public String authCardTotal(Map<String,Object> param){
	    		final AuthCard rr=(AuthCard)param.get("rr");
	    		return new SQL(){{
	    			SELECT("count(rr.id) count,sum(case when rr.status=1 then 1 else 0 end ) sucCount ");
	    			FROM("real_auth rr " +
	    					"left join merchant_info m on rr.merchant_no = m.merchant_no");
	    			if(!StringUtil.isBlank(rr.getMerchantNo())){
	    				WHERE(" rr.merchant_no=#{rr.merchantNo}");
	    			}
	    			if(!StringUtil.isBlank(rr.getMerchantName())){
	    				WHERE(" m.merchant_name=#{rr.merchantName}");
	    			}
	    			if(!StringUtil.isBlank(rr.getUserName())){
	    				WHERE(" rr.user_name=#{rr.userName}");
	    			}
	    			if(!StringUtil.isBlank(rr.getCardNo())){
	    				WHERE(" rr.card_no=#{rr.cardNo}");
	    			}
	    			if(rr.getSdate()!=null){
						WHERE("rr.create_time>=#{rr.sdate}");
					}
					if(rr.getEdate()!=null){
						WHERE("rr.create_time<#{rr.edate}");
					}
	    		}}.toString();
	    	}
	    	
	    	
	    }


}
