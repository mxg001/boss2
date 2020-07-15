package cn.eeepay.framework.dao;

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

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqTerminal;

public interface AcqTerminalDao {

	@Delete("delete from acq_terminal where id=#{id}")
    int deleteByPrimaryKey(@Param("id")Integer id);

    @Insert("INSERT INTO acq_terminal(acq_org_id,acq_merchant_no,acq_terminal_no,create_person) "
    		+ "values (#{record.acqOrgId},#{record.acqMerchantNo},#{record.acqTerminalNo},#{record.createPerson})")
    int insert(@Param("record")AcqTerminal record);
    
    @Insert("INSERT INTO acq_terminal(acq_org_id,acq_merchant_no,acq_terminal_no,create_person,work_key) "
    		+ "values (#{record.acqOrgId},#{record.acqMerchantNo},#{record.acqTerminalNo},#{record.createPerson},#{record.workKey})")
    int insertInfo(@Param("record")AcqTerminal record);
    
    @Update("update acq_terminal set acq_merchant_no=#{record.acqMerchantNo},work_key=#{record.workKey} where acq_terminal_no=#{record.acqTerminalNo}")
    int updateInfo(@Param("record")AcqTerminal record);
    
    @Select("select act.acq_merchant_no,act.acq_org_id,amt.acq_merchant_name,act.acq_terminal_no,act.locked,"
    		+ "amt.merchant_no,mis.merchant_name,ais.agent_name,aos.acq_name "
    		+ "from acq_terminal act "
    		+ "LEFT JOIN acq_merchant amt on amt.acq_merchant_no=act.acq_merchant_no "
    		+ "LEFT JOIN merchant_info mis on mis.merchant_no=amt.merchant_no "
    		+ "LEFT JOIN acq_org aos on aos.id=act.acq_org_id "
    		+ "LEFT JOIN agent_info ais on ais.agent_no=amt.agent_no "
    		+ "where act.id=#{id}")
    @ResultType(AcqTerminal.class)
    AcqTerminal selectByPrimaryKey(@Param("id")Integer id);

    @Select("SELECT * from acq_terminal where acq_terminal_no=#{record.acqTerminalNo}")
    @ResultType(AcqTerminal.class)
    AcqTerminal selectTerminalByTerNo(@Param("record")AcqTerminal record);
    
    @Update("update acq_terminal set locked=#{record.locked} where id=#{record.id}")
    int updateByPrimaryKey(@Param("record")AcqTerminal record);
    
    @Update("update acq_terminal set `status`=#{record.status} where id=#{record.id}")
    int updateStatusByid(@Param("record")AcqTerminal record);
    
    @SelectProvider(type=SqlProvider.class,method="selectAllInfo")
    @ResultType(AcqTerminal.class)
    List<AcqTerminal> selectAllInfo(@Param("page")Page<AcqTerminal> page,@Param("act")AcqTerminal act);
    
    public class SqlProvider{
    	public String selectAllInfo(Map<String,Object> param){
    		final AcqTerminal act=(AcqTerminal)param.get("act");
    		return new SQL(){{
    			SELECT("act.id,act.acq_org_id,aos.acq_name,act.acq_merchant_no,amt.acq_merchant_name,act.acq_terminal_no,"
    					+ "act.batch_no,act.serial_no,act.locked,act.`status`,act.create_time,amt.large_small_flag");
    			FROM("acq_terminal act "
    					+ "LEFT JOIN acq_merchant amt on amt.acq_merchant_no=act.acq_merchant_no "
    					+ "LEFT JOIN acq_org aos on aos.id=act.acq_org_id "
    					+ "LEFT JOIN agent_info ais on ais.agent_no=amt.agent_no");
    			if(StringUtils.isNotBlank(act.getAcqMerchantNo())){
    				WHERE(" (act.acq_merchant_no=#{act.acqMerchantNo} or amt.acq_merchant_name=#{act.acqMerchantNo})");
    			}
    			if(StringUtils.isNotBlank(act.getAcqTerminalNo())){
    				WHERE(" act.acq_terminal_no=#{act.acqTerminalNo}");
    			}
    			if(StringUtils.isNotBlank(act.getAgentNo()) && !act.getAgentNo().equals("-1")){
    				WHERE(" amt.agent_no=#{act.agentNo}");
    			}
    			if(StringUtils.isNotBlank(act.getAcqOrgId()) && !act.getAcqOrgId().equals("-1")){
    				WHERE(" act.acq_org_id=#{act.acqOrgId}");
    			}
    			if(StringUtils.isNotBlank(act.getLargeSmallFlag()) && !act.getLargeSmallFlag().equals("-1")){
    				WHERE(" amt.large_small_flag=#{act.largeSmallFlag}");
    			}
    			if(act.getLocked()!=null && act.getLocked()!=-1){
    				WHERE(" act.locked=#{act.locked}");
    			}
    		}}.toString();
    	}
    }
}