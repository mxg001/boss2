package cn.eeepay.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.MerchantService;

public interface MerchantServiceDao {


    @Update("update merchant_service set status=#{status} where id=#{id}")
    int updateByPrimaryKey(MerchantService record);

    @Select("select msi.*,sis.service_name,sis.service_type,msi.trade_type,IFNULL(zmi.channel_code, '') AS channel_code,zmi.sync_remark from merchant_service msi "
            + "left join service_info sis on sis.service_id=msi.service_id "
            + "left join zq_merchant_info zmi on (zmi.merchant_no=msi.merchant_no and msi.channel_code= zmi.channel_code and msi.trade_type='1' and zmi.effective_status = '1' and zmi.sync_status <> '0') "
            + "where msi.merchant_no=#{merId}")
    @ResultType(MerchantService.class)
    List<MerchantService> selectByMerId(@Param("merId")String merId);

    @Select("SELECT DISTINCT sis.service_type from merchant_service msi "
            + "LEFT JOIN service_info sis on sis.service_id=msi.service_id "
            + "WHERE merchant_no=#{merId}")
    @ResultType(String.class)
    List<String> selectServiceTypeByMerId(@Param("merId")String merId);

    @Update("update merchant_service set trade_type=#{tradeType},channel_code=#{channelCode} where id=#{primaryKey}")
    int updateTradeTypeByPrimaryKey(@Param("primaryKey")Long primaryKey, @Param("tradeType")String tradeType,  @Param("channelCode")String channelCode);

    @Select("select msi.*,sis.service_name,sis.service_type,msi.trade_type,msi.channel_code AS channel_code," +
            " zmi.sync_remark,zmi.unionpay_mer_no,zmi.terminal_no,(CASE WHEN msi.trade_type = '1' THEN IFNULL(zmi.sync_status, '0') ELSE '' END) as sync_status" +
            " from merchant_service msi "
            + "left join service_info sis on sis.service_id=msi.service_id "
            + "left join zq_merchant_info zmi on (zmi.merchant_no=msi.merchant_no and msi.channel_code= zmi.channel_code and msi.trade_type='1' and zmi.effective_status = '1' and zmi.sync_status <> '0') "
            + "where msi.merchant_no=#{merchantNo} and msi.bp_id=#{bpId}")
    @ResultType(MerchantService.class)
    List<MerchantService> selectByMerAndMbpId(@Param("merchantNo")String merchantNo, @Param("bpId")String bpId);

    @Select("select msi.* from merchant_service msi left join merchant_business_product mbp on" +
            " (msi.bp_id=mbp.bp_id and msi.merchant_no=mbp.merchant_no) " +
            " right join service_info si on (si.service_id = msi.service_id and si.service_type is not NULL and si.service_type <> '' and si.service_type < 10000) " +
            " where mbp.id=#{mbpId} limit 1")
    @ResultType(MerchantService.class)
    MerchantService selectPosSerByMbpId(String mbpId);

    @Select("select msi.* from merchant_service msi left join merchant_business_product mbp on" +
    		" (msi.bp_id=mbp.bp_id and msi.merchant_no=mbp.merchant_no) " +
    		" right join service_info si on (si.service_id = msi.service_id and si.service_type is not NULL and si.service_type <> '' and si.service_type > 10000) " +
    		" where mbp.id=#{mbpId} limit 1")
    @ResultType(MerchantService.class)
    MerchantService selectQuickOrNoCardSerByMbpId(String mbpId);
    
    @Select("select * from merchant_service where id=#{id}")
    @ResultType(MerchantService.class)
	MerchantService getMerchantServiceByID(@Param("id") Long id);
}