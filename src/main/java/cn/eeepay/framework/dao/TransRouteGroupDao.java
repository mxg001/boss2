package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TransRouteGroup;

public interface TransRouteGroupDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TransRouteGroup record);

    int insertSelective(TransRouteGroup record);

    @Select("select * from trans_route_group where group_code=#{id}")
    @ResultType(TransRouteGroup.class)
    TransRouteGroup selectByPrimaryKey(@Param("id")Integer id);

    int updateByPrimaryKeySelective(TransRouteGroup record);

    int updateByPrimaryKey(TransRouteGroup record);

    
    @Select("select g.group_name,am.acq_merchant_name,gam.group_code FROM  trans_route_group_acq_merchant gam "
			+ " LEFT JOIN acq_merchant am ON gam.acq_merchant_no = am.acq_merchant_no"
			+ " LEFT JOIN trans_route_group g ON gam.group_code = g.group_code "
			+" WHERE  gam.acq_merchant_no =#{acqMerchantNo}")
    @ResultType(TransRouteGroup.class)
    List<TransRouteGroup> selectGroupByAcqMerchantNo(@Param("acqMerchantNo")String acqMerchantNo);
   
    @Select("select group_code,pos_merchant_no from trans_route_group_merchant where  group_code =#{groupGode}")
    List<TransRouteGroup> selectMerNoByGroupCode(@Param("groupGode")String groupGode);
    

    @Select("select group_code,acq_merchant_no from trans_route_group_acq_merchant where group_code =#{groupGode}")
    List<TransRouteGroup> selectAcqMerNoByGroupCode(@Param("groupGode")String groupGode);

    
    @SelectProvider(type=SqlProvider.class, method="selectByParam")
    @ResultType(TransRouteGroup.class)
	List<TransRouteGroup> selectByParam(Page<TransRouteGroup> page, Map<String, Object> param);
    
    @InsertProvider(type=SqlProvider.class, method="transferMer")
	int transferMer(@Param("groupCode")String groupCode, @Param("list")List<TransRouteGroup> list);

	@DeleteProvider(type=SqlProvider.class, method="deleteBatchByMer")
	int deleteBatchByMer(@Param("list")List<TransRouteGroup> groupList);
    
    public class SqlProvider{
    	public String selectByParam(final Map<String, Object> param){
    		String sql = new SQL(){{
    			SELECT_DISTINCT (" trg.group_code,trg.group_name,trg.service_type,trg.group_province");
    			SELECT("trg.group_city,mi.merchant_no,mi.merchant_name,mi.province,mi.city,ai.agent_name");
    			FROM("trans_route_group_merchant trgm");
    			LEFT_OUTER_JOIN("trans_route_group trg ON trgm.group_code = trg.group_code");
    			LEFT_OUTER_JOIN("merchant_info mi ON trgm.pos_merchant_no=mi.merchant_no");
//    			LEFT_OUTER_JOIN("agent_info ai on mi.one_agent_no=ai.agent_no");
    			//add by tans，查詢一級代理商名稱，改为查询直属代理商名称
    			LEFT_OUTER_JOIN("agent_info ai on mi.agent_no=ai.agent_no");
    			if(param.get("groupCode")!=null&&!"".equals(param.get("groupCode").toString())){
    				WHERE("trg.group_code like concat(#{groupCode},'%')");
    			}
				if(param.get("merchantNo")!=null&&!"".equals(param.get("merchantNo").toString())){
					WHERE("trgm.pos_merchant_no = #{merchantNo}");
				}
				if(param.get("status")!=null&&!"".equals(param.get("status").toString())){
					WHERE("trg.status=#{status}");
				}
				if(param.get("acqId")!=null&&!"".equals(param.get("acqId").toString())){
					WHERE("trg.acq_id=#{acqId}");
				}
				if(param.get("acqServiceId")!=null&&!"".equals(param.get("acqServiceId").toString())){
					WHERE("trg.acq_service_id=#{acqServiceId}");
				}
				if(param.get("groupProvince")!=null&&!"全部".equals(param.get("groupProvince").toString())){
					WHERE("mi.province=#{groupProvince}");
				}
				if(param.get("groupCity")!=null&&!"全部".equals(param.get("groupCity").toString())){
					WHERE("mi.city=#{groupCity}");
				}
				if(param.get("agentNode")!=null&&!"".equals(param.get("agentNode").toString())){
//					WHERE("mi.agent_no=#{agentNo}");
					WHERE("mi.parent_node like concat (#{agentNode},'%')");
				}
				if(param.get("serviceType")!=null&&!"".equals(param.get("serviceType").toString())){
					WHERE("trg.service_type=#{serviceType}");
				}
				WHERE("mi.merchant_no > 0");
    		}}.toString();
    		return sql;
    	}
    	
    	public String deleteBatchByMer(Map<String, Object> param){
    		List<TransRouteGroup> list = (List<TransRouteGroup>) param.get("list");
    		StringBuffer sb = new StringBuffer("delete from trans_route_group_merchant where service_type=#{list[0].serviceType} and pos_merchant_no in(");
    		MessageFormat message = new MessageFormat("#'{'list[{0}].merchantNo},");
    		for(int i=0; i<list.size(); i++){
    			sb.append(message.format(new Integer[]{i}));
    		}
    		sb.setLength(sb.length()-1);
    		sb.append(")");
    		return sb.toString();
    	}
    	
    	public String transferMer(Map<String, Object> param){
    		List<TransRouteGroup> list = (List<TransRouteGroup>) param.get("list");
    		StringBuffer sb = new StringBuffer("insert into trans_route_group_merchant(group_code,pos_merchant_no,service_type,create_time,create_person) values");
    		MessageFormat message = new MessageFormat("(#'{'groupCode},#'{'list[{0}].merchantNo},#'{'list[{0}].serviceType},now(),#'{'list[{0}].createPerson}),");
    		for(int i=0; i<list.size(); i++){
    			sb.append(message.format(new Integer[]{i}));
    		}
    		sb.setLength(sb.length()-1);
    		return sb.toString();
    	}
    }

}