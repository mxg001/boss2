package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqInMerchant;
import cn.eeepay.framework.model.AcqMerchant;
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

public interface AcqMerchantDao {
	
	//根据收单机构商户编号判断是否存在
	@Select("SELECT COUNT(1) FROM acq_merchant where acq_merchant_no=#{acqMerchantNo}")
	int selectOrgMerExistByMerNo(@Param("acqMerchantNo")String acqMerchantNo);
	
	//根据普通商户编号判断是否存在
	@Select("SELECT * FROM acq_merchant WHERE merchant_no=#{merchantNo}")
	@ResultType(AcqMerchant.class)
	AcqMerchant selectInfoByMerNo(@Param("merchantNo")String merchantNo);
	
    @Insert("insert into acq_merchant(acq_org_id,acq_service_id,acq_merchant_no,acq_merchant_name,merchant_no,"
    		+ "agent_no,mcc,create_person,acq_merchant_type,acq_merchant_code," +
			"special,source,bind_general_merchant_time) "
    		+ "values(#{record.acqOrgId},#{record.acqServiceId},#{record.acqMerchantNo},#{record.acqMerchantName},#{record.merchantNo}"
    		+ ",#{record.agentNo},#{record.mcc},#{record.createPerson},#{record.acqMerchantType},#{record.acqMerchantCode}," +
			"#{record.special},#{record.source},NOW())")
    int insert(@Param("record")AcqMerchant record);

    @Select("SELECT acm.merchant_service_type,asi.service_name,acm.acq_merchant_no,acm.acq_org_id,acm.acq_service_id"
    		+ ",acm.acq_merchant_name,aco.acq_name,acm.agent_no,ais.agent_name,acm.merchant_no,acm.mcc,acm.acq_merchant_type," +
			"acm.acq_merchant_code,acm.special,acm.source "
    		+ "from acq_merchant acm "
    		+ "LEFT JOIN acq_org aco on aco.id=acm.acq_org_id "
    		+ "LEFT JOIN merchant_info mis on mis.merchant_no=acm.merchant_no "
    		+ "LEFT JOIN agent_info ais on ais.agent_no=acm.agent_no "
    		+ "LEFT JOIN acq_service asi on asi.id = acm.acq_service_id "
    		+ "where acm.id=#{id}")
    @ResultType(AcqMerchant.class)
    AcqMerchant selectByPrimaryKey(@Param("id")Integer id);
    
    @Update("update acq_merchant set acq_org_id=#{record.acqOrgId},acq_service_id=#{record.acqServiceId},"
    		+ "acq_merchant_name=#{record.acqMerchantName},"
    		+ "agent_no=#{record.agentNo},mcc=#{record.mcc},merchant_no=#{record.merchantNo}," +
			"acq_merchant_type=#{record.acqMerchantType},acq_merchant_code=#{record.acqMerchantCode},special=#{record.special}," +
			"source=#{record.source}"
    		+ " where id=#{record.id}")
    int updateByPrimaryKey(@Param("record")AcqMerchant record);
    
    @Update("update acq_merchant set quota=#{quota},day_quota=#{dayQuota} where acq_merchant_no=#{acqMerchantNo}")
    int updateAcqMerchantQuota(AcqMerchant recode);
    
    @Update("update acq_merchant set acq_status=#{record.acqStatus} where id=#{record.id}")
    int updateStatusByid(@Param("record")AcqMerchant record);
    
    @Select("select * from acq_merchant where acq_merchant_no=#{record.acqMerchantNo}")
    @ResultType(AcqMerchant.class)
    AcqMerchant selectInfoByAcqmerNo(@Param("record")AcqMerchant record);
    
    @SelectProvider(type=SqlProvider.class,method="selectAllInfo")
    @ResultType(AcqMerchant.class)
    List<AcqMerchant> selectAllInfo(@Param("page")Page<AcqMerchant> page,@Param("amc")AcqMerchant amc);

	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(AcqMerchant.class)
	List<AcqMerchant> importDetailSelect(@Param("amc")AcqMerchant amc);

    @Update("update acq_merchant set day_quota=quota")
   	int updateBatchAcqMerchantQuota();

	//根据普通商户编号判断是否存在
	@Select("SELECT * FROM acq_merchant WHERE acq_merchant_no=#{acqMerchantNo}")
	@ResultType(AcqMerchant.class)
	AcqMerchant getAcqMerchantInfo(@Param("acqMerchantNo")String acqMerchantNo);

	@Update("update acq_merchant set acq_status=#{acqStatus} where acq_merchant_no=#{acqMerchantNo}")
	int updateInfoStatus(@Param("acqStatus")int acqStatus,@Param("acqMerchantNo")String acqMerchantNo);

	@Update("update terminal_info set bp_id=#{newBpId}  where merchant_no=#{merchantNo} and bp_id =#{oldBpId} and open_status ='2' ")
    int updateMerBusTer(@Param("newBpId") String newBpId, @Param("merchantNo")String merchantNo, @Param("oldBpId")String oldBpId);

	@Update("UPDATE acq_merchant SET merchant_no = NULL WHERE id = #{id}")
	int clearMerchantNo(@Param("id")Integer id);

	@Select("SELECT * FROM acq_merchant WHERE merchant_no=#{merchantNo}")
    AcqMerchant selectByMerchantNo(@Param("merchantNo") String merchantNo);

    public class SqlProvider{
    	
    	public String selectAllInfo(Map<String,Object> param){
    		final AcqMerchant amc=(AcqMerchant)param.get("amc");
    		return new SQL(){{
    			SELECT("acm.id,acm.acq_merchant_no,acm.acq_merchant_name,acm.merchant_no,acm.acq_merchant_code,acm.source,"
    					+ "mis.merchant_name,aco.acq_name,acm.acq_org_id,ais.agent_name"
    					+ ",acm.locked,acm.rep_pay,acm.acq_status,acm.create_time,aso.service_name,acm.quota,"
    					+ "acm.day_quota as dayAmount,acm.acq_merchant_type ");
    			FROM("acq_merchant acm "
    					+ "LEFT JOIN acq_org aco on aco.id=acm.acq_org_id "
    					+ "LEFT JOIN acq_service aso on aso.id=acm.acq_service_id  "
    					+ "LEFT JOIN merchant_info mis on mis.merchant_no=acm.merchant_no "
    					+ "LEFT JOIN agent_info ais on ais.agent_no=acm.agent_no");
    			if(StringUtils.isNotBlank(amc.getAcqMerchantNo())){
    				WHERE(" (acm.acq_merchant_no=#{amc.acqMerchantNo} or acm.acq_merchant_name=#{amc.acqMerchantNo})");
    			}
    			if(StringUtils.isNotBlank(amc.getAcqMerchantCode())){
    				WHERE(" (acm.acq_merchant_code=#{amc.acqMerchantCode})");
    			}
    			if(StringUtils.isNotBlank(amc.getMerchantNo())){
    				WHERE(" (mis.merchant_no like concat(#{amc.merchantNo},'%') or mis.merchant_name like concat(#{amc.merchantNo},'%'))");
    			}
    			if(StringUtils.isNotBlank(amc.getAgentNo()) && !amc.getAgentNo().equals("-1")){
    				WHERE(" acm.agent_no=#{amc.agentNo}");
    			}
    			if(amc.getAcqOrgId()!=null && amc.getAcqOrgId()!=-1){
    				WHERE(" acm.acq_org_id=#{amc.acqOrgId}");
    			}
    			if(amc.getAcqStatus()!=-1){//gw
    				WHERE(" acm.acq_status=#{amc.acqStatus}");
    			}
    			if(amc.getAcqMerchantType()!=null && amc.getAcqMerchantType()!=-1){
    				WHERE(" acm.acq_merchant_type=#{amc.acqMerchantType}");
    			}
    			if(StringUtils.isNotBlank(amc.getStratRemaimAmount())){
    				WHERE(" acm.day_quota>=#{amc.stratRemaimAmount}");
    			}
    			if(StringUtils.isNotBlank(amc.getEndRemaimAmount())){
    				WHERE(" acm.day_quota<#{amc.endRemaimAmount}");
    			}
    			if(StringUtils.isNotBlank(amc.getSpecial())){
    				WHERE("acm.special=#{amc.special}");
				}
				if(StringUtils.isNotBlank(amc.getSource())){
					WHERE(" acm.source=#{amc.source}");
				}
    			ORDER_BY("acm.id");
    		}}.toString();
    	}
    	
    }

}
