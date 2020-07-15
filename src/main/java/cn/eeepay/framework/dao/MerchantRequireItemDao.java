package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.IndustryMcc;
import cn.eeepay.framework.model.MerchantRequireItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MerchantRequireItemDao {
    @Update("update merchant_require_item set status=#{status},audit_time=now() where id=#{id}")
    int updateBymriId(@Param("id")Long id,@Param("status")String status);
    
    @Update("update merchant_require_item set content=#{record.content} where id=#{record.id}")
    int updateByMbpId(@Param("record")MerchantRequireItem record);
    
    @Select("select mri.*,ari.item_name,ari.remark,ari.example_type,ari.photo,ari.check_status from merchant_require_item mri"
    		+ " left join add_require_item ari on ari.item_id=mri.mri_id "
    		+ "where mri.mri_id=#{mriId} and merchant_no=#{merId}")
    @ResultType(MerchantRequireItem.class)
    MerchantRequireItem selectByMriId(@Param("mriId")String mriId,@Param("merId")String merId);

    
    @Select("select * from merchant_require_item where merchant_no=#{merId} and mri_id=3")
    @ResultType(MerchantRequireItem.class)
    MerchantRequireItem selectByAccountNo(@Param("merId")String merId);

    @Select("select * from merchant_require_item where merchant_no=#{merId} and mri_id=6")
    @ResultType(MerchantRequireItem.class)
    MerchantRequireItem selectByIdCardNo(@Param("merId")String merId);

    @Select("SELECT mri.*,ari.item_name,ari.remark,ari.example_type,ari.photo,ari.check_status FROM `merchant_require_item` mri left join add_require_item ari on ari.item_id =mri.mri_id"
    		+ " where mri.merchant_no=#{merchantNo} ORDER BY ari.item_id;")
    @ResultType(MerchantRequireItem.class)
	List<MerchantRequireItem> getByMer(@Param("merchantNo")String merchantNo);

    @Select("select * from merchant_require_item where merchant_no=#{merchantNo}  and status='1';")
    @ResultType(MerchantRequireItem.class)
    List<MerchantRequireItem> getItemByMerId(@Param("merchantNo")String merchantNo);

    @Select("SELECT mri.* FROM business_require_item bri,merchant_require_item mri WHERE mri.mri_id = bri.br_id and bri.bp_id=#{bpId} and mri.merchant_no=#{merchantNo}")
    @ResultType(MerchantRequireItem.class)
    List<MerchantRequireItem> selectItemByBpIdAndMerNo(@Param("merchantNo")String merchantNo,@Param("bpId")String bpId);


    @Select("select *,im2.industry_name as industry_name1 from industry_mcc im1 left join industry_mcc im2 on im1.parent_id=im2.id where im1.mcc=#{mcc}" +
            " and im1.channel_code = im2.channel_code limit 1")
    @ResultType(IndustryMcc.class)
    IndustryMcc selectIndustryMccByMcc(@Param("mcc")String mcc);

    @Select("select * from industry_mcc where industry_level=#{industryLevel}")
    @ResultType(IndustryMcc.class)
    List<IndustryMcc> selectIndustryMccByLevel(@Param("industryLevel")String industryLevel);

    @Select("select * from industry_mcc where parent_id=#{parentId}")
    @ResultType(IndustryMcc.class)
    List<IndustryMcc> selectIndustryMccByParentId(@Param("parentId")String parentId);

    @Update("update merchant_require_item set content=#{mcc} where id=#{id}")
    int updateMccById(@Param("id")String id,@Param("mcc")String mcc);

    @Update("update merchant_info set industry_type=#{mcc} where merchant_no=#{merNo}")
    int updateMccByMerNo(@Param("merNo")String merNo,@Param("mcc")String mcc);


    /**
     * 获取该商户的进件项资料,过滤业务组织下包含有的
     * @param merchantNo
     * @param bpId
     * @return
     */
    @Select("SELECT mri.*,ari.item_name,ari.remark,ari.example_type,ari.photo,ari.check_status " +
            " FROM `merchant_require_item` mri " +
            "  INNER JOIN business_require_item brm ON (brm.br_id= mri.mri_id and brm.bp_id=#{bpId}) " +
            "  left join add_require_item ari on ari.item_id =mri.mri_id "+
            " where mri.merchant_no=#{merchantNo} " +
            "  ORDER BY ari.item_id;")
    @ResultType(MerchantRequireItem.class)
    List<MerchantRequireItem> getMerchantRequireItemList(@Param("merchantNo")String merchantNo,@Param("bpId")String bpId);

}