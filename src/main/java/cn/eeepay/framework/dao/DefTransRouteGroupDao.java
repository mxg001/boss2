package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.DefTransRouteGroup;

public interface DefTransRouteGroupDao {
    @Insert("insert into def_trans_route_group(product_id,acq_org_id,acq_service_type,service_id,start_pc,def_group_code,create_person,def_type,service_model,liquidation_channel) "
    		+ "values(#{record.productId},#{record.acqOrgId},#{record.acqServiceType},#{record.serviceId},"
    		+ "#{record.startPc},#{record.defGroupCode},#{record.createPerson},#{record.defType},#{record.serviceModel},#{record.liquidationChannel})")
    int insert(@Param("record")DefTransRouteGroup record);
    

    @Select("select d.*,s.service_type from def_trans_route_group d left join service_info s on d.service_id=s.service_id where d.product_id=#{id} and d.service_id=#{serviceId}")
    @ResultType(DefTransRouteGroup.class)
    DefTransRouteGroup selectInfo(@Param("id")String bpId,@Param("serviceId")String serviceId);
    
    
    @Select("select dtrg.*,aos.acq_name,trg.group_name,bpd.bp_name,sis.service_name "
    		+ "from def_trans_route_group dtrg "
    		+ "LEFT JOIN trans_route_group trg on trg.group_code=dtrg.def_group_code "
    		+ "LEFT JOIN acq_org aos on aos.id=dtrg.acq_org_id "
    		+ "LEFT JOIN business_product_define bpd on bpd.bp_id=dtrg.product_id "
    		+ "LEFT JOIN service_info sis on sis.service_id=dtrg.service_id "
    		+ "where dtrg.id=#{id}")
    @ResultType(DefTransRouteGroup.class)
    DefTransRouteGroup selectByPrimaryKey(@Param("id")Integer id);

    @Update("update def_trans_route_group set product_id=#{record.productId},"
    		+ "acq_org_id=#{record.acqOrgId},acq_service_type=#{record.acqServiceType}"
    		+ ",service_id=#{record.serviceId},start_pc=#{record.startPc},"
    		+ "def_group_code=#{record.defGroupCode},create_person=#{record.createPerson},service_model=#{record.serviceModel},liquidation_channel=#{record.liquidationChannel}"
    		+ " where id=#{record.id}")
    int updateByPrimaryKey(@Param("record")DefTransRouteGroup record);
    
    @SelectProvider(type=SqlProvider.class,method="selectAllInfo")
    @ResultType(DefTransRouteGroup.class)
    List<DefTransRouteGroup> selectAllInfo(@Param("page")Page<DefTransRouteGroup> page,@Param("drg")DefTransRouteGroup drg);
    
    @SelectProvider(type=SqlProvider.class,method="selectExistByParam")
    int selectExistByParam(@Param("drg")DefTransRouteGroup dtrg);
    public class SqlProvider{
    	
    	public String selectAllInfo(Map<String,Object> param){
    		final DefTransRouteGroup drg=(DefTransRouteGroup)param.get("drg");
    		return new SQL(){{
    			SELECT("dtrg.id,dtrg.def_group_code,trg.group_name,bpd.bp_name,sis.service_name");
    			FROM("def_trans_route_group dtrg "
    					+ "LEFT JOIN trans_route_group trg on trg.group_code=dtrg.def_group_code "
    					+ "LEFT JOIN business_product_define bpd on bpd.bp_id=dtrg.product_id "
    					+ "LEFT JOIN service_info sis on sis.service_id=dtrg.service_id");
    			if(StringUtils.isNotBlank(drg.getDefGroupCode())){
    				WHERE(" dtrg.def_group_code=#{drg.defGroupCode}");
    			}
    			if(StringUtils.isNotBlank(drg.getGroupName())){
    				WHERE(" trg.group_name=#{drg.groupName}");
    			}
    			if(drg.getProductId()!=null && drg.getProductId()!=-1){
    				WHERE(" dtrg.product_id=#{drg.productId}");
    			}
    			if(drg.getServiceId()!=null && drg.getServiceId()!=-1){
    				WHERE(" dtrg.service_id=#{drg.serviceId}");
    			}
    			ORDER_BY("dtrg.id");
    		}}.toString();
    	}
    	
    	public String selectExistByParam(Map<String,Object> param){
    		final DefTransRouteGroup drg=(DefTransRouteGroup)param.get("drg");
    		return new SQL(){{
    			SELECT("COUNT(1)");
    			FROM("def_trans_route_group");
    			if(StringUtils.isNotBlank(drg.getDefGroupCode())){
    				WHERE("def_group_code=#{drg.defGroupCode}");
    			}
    			if(drg.getAcqOrgId()!=null){
    				WHERE("acq_org_id=#{drg.acqOrgId}");
    			}
    			if(drg.getProductId()!=null && drg.getProductId()!=-1){
    				WHERE("product_id=#{drg.productId}");
    			}
    			if(drg.getServiceId()!=null && drg.getServiceId()!=-1){
    				WHERE("service_id=#{drg.serviceId}");
    			}
    		}}.toString();
    	}
    	
    }

    
}