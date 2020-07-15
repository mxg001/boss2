package cn.eeepay.framework.dao;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.MerchantService;
import cn.eeepay.framework.model.MerchantServiceQuota;
import cn.eeepay.framework.model.MerchantServiceRate;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;
import cn.eeepay.framework.model.SysDict;
import org.apache.ibatis.jdbc.SQL;

public interface MerchantInfoDao {

    @Select("SELECT mis.merchant_no,tis.SN,mis.merchant_name,mis.mobilephone,mis.create_time,"
    		+ "ai.agent_name,mis.`status` from merchant_info mis "
    		+ "LEFT JOIN terminal_info tis on tis.merchant_no=mis.merchant_no "
    		+ "LEFT JOIN agent_info ai on ai.agent_no=mis.agent_no "
    		+ "where mis.id=#{id}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByPrimaryKey(@Param("id")Long id);
    
    @Select("SELECT * FROM merchant_info where merchant_no=#{posMerchantNo}")
    MerchantInfo selectMerExistByMerNo(@Param("posMerchantNo")String posMerchantNo);
    
    //用于黑白名单添加查询
    @Select("SELECT * from merchant_info where merchant_no=#{merchantNo}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByMerNo(@Param("merchantNo")String merchantNo);

    @Select("SELECT info.*,product.status AS product_status FROM merchant_info info LEFT JOIN merchant_business_product product ON info.merchant_no = product.merchant_no where info.merchant_no=#{merchantNo} LIMIT 0,1")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectStatusByMerNo(@Param("merchantNo")String merchantNo);

    @Select("SELECT * from merchant_info where id_card_no=#{card}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByMerIC(@Param("card")String card);
    
    @Select("SELECT * from merchant_info where mobilephone=#{mobilephone}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByMobilephone(@Param("mobilephone")String mobilephone);
    
    @Select("SELECT * from merchant_info where team_id=#{teamId} and mobilephone=#{mobilephone}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByMobilephoneAndTeam(@Param("mobilephone")String mobilephone, @Param("teamId")String teamId);
    
    //商户进件中的修改
    @Update("update merchant_info set status=#{record.status},lawyer=#{record.lawyer},merchant_name=#{record.merchantName},remark=#{record.remark},mobilephone=#{record.mobilephone},province=#{record.province}"
    		+ ",city=#{record.city},address=#{record.address},district=#{record.district},id_card_no=#{record.idCardNo} "
    		+ "where merchant_no=#{record.merchantNo}")
    int updateByMerId(@Param("record")MerchantInfo record);
    
    //商户进件中的修改1
    @Update("update merchant_info set province=#{record.province}"
    		+ ",city=#{record.city},address=#{record.address},district=#{record.district} "
    		+ "where merchant_no=#{record.merchantNo}")
    int updateAddressByMerId(@Param("record")MerchantInfo record);
    
    //根据商户ID修改信息审核信息中
    @Update("update merchant_info SET id_card_no=#{record.idCardNo},province=#{record.province}"
    		+ ",city=#{record.city},address=#{record.address},district=#{record.district} where merchant_no=#{record.merchantNo}")
    int updateByPrimaryKey(@Param("record")MerchantInfo record);
    
    @Select("SELECT mis.id,mis.merchant_no,mis.merchant_name,mis.mobilephone,mis.create_time,"
    		+ "ai.agent_name,mis.`status` from merchant_info mis "
    		+ "LEFT JOIN agent_info ai on ai.agent_no=mis.agent_no")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectAllInfo();
    
    /*
     * 2016-09-02 徐永锋 将下面的one_agent_no改为agent_no
     */
    @Select("select mi.merchant_no,mi.merchant_name from merchant_info mi where mi.status=1 and mi.agent_no=#{agentNo}")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectByNameInfoByTermianl(@Param("agentNo")String agentNo);
    
    @Select("SELECT mis.*,tti.team_name,ai.agent_name from merchant_info mis "
    		+ "LEFT JOIN terminal_info tis on tis.merchant_no=mis.merchant_no "
    		+ "LEFT JOIN agent_info ai on ai.agent_no=mis.agent_no "
    		+ "LEFT JOIN team_info tti on tti.team_id=mis.team_id "
    		+ " where mis.merchant_no=#{mertId}")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectByMertId(@Param("mertId")String mertId);
  
    @Select("select * from sys_dict where sys_key=#{key} ")
    @ResultType(SysDict.class)
    SysDict selectSysDict(@Param("key")String key);
    
    
    @Select("select * from sys_dict where sys_value=#{key} and parent_id=#{Pkey}")
    @ResultType(SysDict.class)
    SysDict selectSysDictByKey(@Param("key")String key,@Param("Pkey")String Pkey);
    
    @Select("select * from sys_dict where parent_id=#{ParentId}")
    @ResultType(SysDict.class)
    List<SysDict> selectTwoInfoByParentId(@Param("ParentId")String ParentId);
    
    @Select("select * from sys_dict where parent_id=-1")
    @ResultType(SysDict.class)
    List<SysDict> selectOneInfo();

    @Update("update merchant_info set mer_account=1 where merchant_no=#{merNo}")
    int updateMerAcoount(@Param("merNo")String merNo);
    
    @Select("select * from merchant_info where mer_account='0'")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> selectByMerAccount();
    
    @Update("update merchant_info set risk_status=#{riskStatus} where merchant_no=#{merId}")
    int updateRiskStatus(@Param("merId")String merId,@Param("riskStatus")String riskStatus);
    
    
    @Select("select distinct si.service_id,si.* from service_info si"
			+ " inner join business_product_info  bpi on si.service_id = bpi.service_id"
			+ " inner join agent_business_product abp on abp.bp_id = bpi.bp_id"
			+ " where abp.agent_no=#{agent_no} and bpi.bp_id=#{bp_id}"
			)
	public List<ServiceInfo> getServiceInfoByParams(@Param("agent_no") String agent_no,@Param("bp_id") String bp_id);
	
  
    
    @Select("SELECT smq.* ,si.fixed_quota,si.service_name FROM business_product_info bpi "
			+ "INNER JOIN service_manage_quota smq ON bpi.service_id= smq.service_id "
			+ "INNER JOIN service_info si ON si.service_id = bpi.service_id "
			+ " WHERE bpi.bp_id=#{bp_id} AND smq.agent_no=#{one_agent_no}")
	public List<ServiceQuota> getServiceQuotaByParams(@Param("one_agent_no") String one_agent_no,@Param("bp_id") String bp_id);
    
    
    @Select("SELECT smr.*,si.fixed_rate,si.service_name FROM business_product_info bpi "
			+ "INNER JOIN service_manage_rate smr ON bpi.service_id= smr.service_id "
			+ "INNER JOIN service_info si ON si.service_id = bpi.service_id "
			+ " WHERE bpi.bp_id=#{bp_id} AND smr.agent_no=#{one_agent_no}")
	public List<ServiceRate> getServiceRatedByParams(@Param("one_agent_no") String one_agent_no,@Param("bp_id") String bp_id);
	
    
    
    
    
    
    
    @Select("select smr.service_id,si.service_name, smr.holidays_mark, smr.card_type, smr.rate_type, smr.single_num_amount, smr.rate, smr.capping,smr.safe_line,smr.ladder1_rate,smr.ladder1_max, smr.ladder2_rate, smr.ladder2_max, smr.ladder3_rate, smr.ladder3_max, smr.ladder4_rate,smr.ladder4_max  from service_manage_rate smr"
    		+ " inner join service_info si on si.service_id = smr.service_id where  smr.agent_no=#{one_agent_no} and smr.service_id=#{serviceId}")
	public List<ServiceRate> getServiceRateByServiceId(@Param("one_agent_no") String one_agent_no,@Param("serviceId") String serviceId);

  
    @Select(" select smq.* ,si.service_name from service_manage_quota smq inner join service_info si on si.service_id = smq.service_id"
    		+ " where  smq.agent_no=#{one_agent_no} and smq.service_id=#{serviceId}")
	public List<ServiceQuota> getServiceQuotaByServiceId(@Param("one_agent_no") String one_agent_no,@Param("serviceId") String serviceId);

    
    @Insert("insert merchant_service(bp_id,merchant_no,service_id,create_date,disabled_date,status) "
			+"values(#{merService.bpId},#{merService.merchantNo},#{merService.serviceId},#{merService.createDate},#{merService.disabledDate},#{merService.status})")
	int insertMerService(@Param("merService")MerchantService merService);
    
    
    @Insert("insert merchant_service_quota(service_id,card_type,holidays_mark,merchant_no,single_day_amount,"
			+ "single_count_amount,single_daycard_amount,single_daycard_count,efficient_date,useable,single_min_amount)"
			+" values(#{merquota.serviceId},#{merquota.cardType},#{merquota.holidaysMark},#{merquota.merchantNo}"
			+ ",#{merquota.singleDayAmount},#{merquota.singleCountAmount},#{merquota.singleDaycardAmount},"
			+ "#{merquota.singleDaycardCount},#{merquota.efficientDate},#{merquota.useable},#{merquota.singleMinAmount})")
	int insertMerQuota(@Param("merquota")MerchantServiceQuota merQuota);
    
    
    @Insert("insert merchant_service_rate(service_id,merchant_no,holidays_mark,card_type,rate_type,single_num_amount,rate,capping,safe_line,efficient_date,disabled_date,ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,ladder3_max,ladder4_rate,ladder4_max,useable)"
			+ " values(#{merRate.serviceId},#{merRate.merchantNo},#{merRate.holidaysMark},#{merRate.cardType},#{merRate.rateType},#{merRate.singleNumAmount},#{merRate.rate},#{merRate.capping},#{merRate.safeLine},#{merRate.efficientDate},#{merRate.disabledDate},#{merRate.ladder1Rate},#{merRate.ladder1Max},#{merRate.ladder2Rate},#{merRate.ladder2Max},#{merRate.ladder3Rate},#{merRate.ladder3Max},#{merRate.ladder4Rate},#{merRate.ladder4Max},#{merRate.useable})")
	int insertMerRate(@Param("merRate")MerchantServiceRate merRate);
	
    //2.2.5 商户预冻结金额的update
    @Update("update merchant_info set pre_frozen_amount=#{preFrozenAmount} "
    		+ " where merchant_no=#{merchantNo}")
    int updatePreFrozenAmountByMerId(@Param("preFrozenAmount") BigDecimal preFrozenAmount,@Param("merchantNo") String merchantNo);
    
    //2.2.5 获取商户预冻结金额
    @Select("SELECT pre_frozen_amount from merchant_info "
    		+ " where merchant_no=#{merchantNo}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByMerchantNo(@Param("merchantNo") String merchantNo);
    
    
    @Select("SELECT * from merchant_info "
    		+ " where merchant_no=#{merchantNo}")
    @ResultType(MerchantInfo.class)
    MerchantInfo selectByMerchantNoAll(@Param("merchantNo") String merchantNo);
    
    @UpdateProvider(type=SqlProvider.class, method="updateMerAccountBatch")
    int updateMerAccountBatch(@Param("merchantNoList")List<String> merchantNoList);

    //模糊查询少量的商户
    @SelectProvider(type=MerchantInfoDao.SqlProvider.class,method="getMerchantFew")
    @ResultType(MerchantInfo.class)
    List<MerchantInfo> getMerchantFew(@Param("item")String item);

    public class SqlProvider{
    	public String updateMerAccountBatch(Map<String, Object> param){
    		List<String> merchantNoList = (List<String>) param.get("merchantNoList");
    		StringBuilder sql = new StringBuilder();
    		sql.append("update merchant_info set mer_account=1 where merchant_no in(");
    		MessageFormat message = new MessageFormat("#'{'merchantNoList[{0}]}");
    		for(int i=0; i<merchantNoList.size(); i++){
    			sql.append(message.format(new Integer[]{i}));
    			sql.append(",");
    		}
    		sql.replace(sql.length()-1, sql.length(), ");");
    		System.out.println(sql);
    		return sql.toString();
    	}
    	public String getMerchantFew (Map<String, Object> param){
            final String item=(String)param.get("item");
            SQL sql = new SQL();
            sql.SELECT("merchant_no,merchant_name");
            sql.FROM("merchant_info");
            if(StringUtils.isNotBlank(item)){
                if(StringUtils.isNumeric(item)){
                    sql.WHERE("merchant_no like concat(#{item}, '%')");
                }else{
                    sql.WHERE("merchant_name like concat(#{item}, '%')");
                }
            }
            sql.ORDER_BY(" merchant_no limit 100");
            return sql.toString();
        }
    }

	
}