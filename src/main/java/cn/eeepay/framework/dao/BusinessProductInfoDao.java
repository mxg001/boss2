package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.BusinessProductInfo;

public interface BusinessProductInfoDao {
	
	@Select("select * from business_product_define where bp_id=#{bpId}")
    @ResultType(BusinessProductDefine.class)
	public BusinessProductDefine getProductBase(Integer bpId);
	
	@Select("select bp_id,bp_name from business_product_define where rely_hardware=0 and team_id=#{teamId} and bp_id<>#{bpId} and effective_status='1'")
    @ResultType(BusinessProductDefine.class)
	public List<BusinessProductDefine> getOtherProduct(BusinessProductDefine product);

    @Select("SELECT service_id FROM business_product_info WHERE bp_id=#{bpId}")
    @ResultType(java.lang.String.class)
	List<String> findByProduct(@Param("bpId")String bpId);
    
    @Select("SELECT bpi.service_id,si.service_type,si.service_name FROM business_product_info bpi left join service_info si on si.service_id=bpi.service_id "
    		+ " WHERE bp_id=#{bpId} order by CASE WHEN si.link_service IS NULL THEN si.service_id ELSE si.link_service END,"
    		+ " CASE WHEN si.service_type in ('10000', '10001') THEN '1' ELSE '0' END;")
    @ResultType(BusinessProductInfo.class)
    List<BusinessProductInfo> getByBpId(@Param("bpId")String bpId);
    
    
    @Insert("INSERT INTO business_product_info(bp_id,service_id) values (#{map.bpId},#{map.serviceId})")
    int insert(@Param("map")Map<String, Object> map);
    
    @InsertProvider(type=SqlProvider.class, method="insertBatchService")
    int insertBatchService(@Param("bpId")String bpId, @Param("serviceIds")Object[] serviceIds);

    @Delete("DELETE FROM business_product_info WHERE bp_id=#{bpId}")
	int deleteProductByPid(@Param("bpId")String id);
    
    @SelectProvider(type=SqlProvider.class,method="selectInfoByBpId")
    @ResultType(BusinessProductInfo.class)
	List<BusinessProductInfo> selectInfoByBpId(@Param("bpId")String bpId);
    
    @SelectProvider(type=SqlProvider.class, method="selectExistName")
	int selectExistName(@Param("bpId")Long bpId, @Param("name")String name, @Param("type") String type);
    
    @Select("select bp_id from business_product_info where service_id=#{serviceId}")
    @ResultType(List.class)
	List<Long> findByService(@Param("serviceId")Long serviceId);
    
    public class SqlProvider{
    	public String selectInfoByBpId(Map<String,Object> param){
    		final String bpId=(String)param.get("bpId");
    		return new SQL(){{
    			SELECT("bpi.id,bpi.bp_id,bpi.service_id,sis.service_name,sis.service_type");
    			FROM("business_product_info bpi,service_info sis ");
    			WHERE("sis.service_id=bpi.service_id");
    			if(StringUtils.isNotBlank(bpId) && !bpId.equals("-1")){
    				WHERE(" bpi.bp_id=#{bpId}");
    			}
    			WHERE("sis.service_type<>\"10000\"");
    			WHERE("sis.service_type<>\"10001\"");
    		}}.toString();
    	}
    	
    	public String insertBatchService(Map<String,Object> param){
    		Object[] list = (Object[]) param.get("serviceIds");
    		final String bpId=(String)param.get("bpId");
			StringBuilder sb = new StringBuilder();
			sb.append("insert into business_product_info (bp_id, service_id) values");
			MessageFormat messageFormat = new MessageFormat("(#'{'bpId},#'{'serviceIds[{0}]})");
            for (int i = 0; i < list.length; i++) {
                sb.append(messageFormat.format(new Integer[]{i}));
                sb.append(",");
            }
            sb.setLength(sb.length() - 1);
            System.out.println(sb.toString());
			return sb.toString();
    	}
    	
    	public String selectExistName(Map<String,Object> param){
    		final Long bpId = (Long) param.get("bpId");
    		final String type = (String)param.get("type");
    		return new SQL(){{
    			SELECT("count(1)");
    			FROM("business_product_define");
				if("productName".equals(type)){
					WHERE("bp_name=#{name}");
				}
				if("agentShowName".equals(type)){
					WHERE("agent_show_name=#{name}");
				}
    			if(null != bpId && 0 != bpId){
					WHERE("bp_id!=#{bpId}");
    			}
    		}}.toString();
    	}
    	
    }

	
}