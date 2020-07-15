package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.MerchantCardInfo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface MerchantCardInfoDao {

    @Insert("insert merchant_card_info"
    		+ "(merchant_no,card_type,quick_pay,def_quick_pay,def_settle_card,"
    		+ "account_type,bank_name,cnaps_no,account_name,account_no,status,create_time,account_province,account_city,account_district) "
    		+ "values(#{record.merchantNo},#{record.cardType},#{record.quickPay},#{record.defQuickPay},#{record.defSettleCard}"
    		+ ",#{record.accountType},#{record.bankName},#{record.cnapsNo},#{record.accountName},#{record.accountNo},#{record.status}"
    		+ ",#{record.createTime},#{record.accountProvince},#{record.accountCity},#{record.accountDistrict})")
    int insert(@Param("record")MerchantCardInfo record);

    @Update("update merchant_card_info set "
    		+ "card_type=#{record.cardType},quick_pay=#{record.quickPay},"
    		+ "def_quick_pay=#{record.defQuickPay},def_settle_card=#{record.defSettleCard},"
    		+ "account_type=#{record.accountType},bank_name=#{record.bankName},"
    		+ "cnaps_no=#{record.cnapsNo},account_name=#{record.accountName},"
    		+ "account_no=#{record.accountNo},status=#{record.status},"
    		+ "account_province=#{record.accountProvince},account_city=#{record.accountCity},"
    		+ "account_district=#{record.accountDistrict} where merchant_no=#{record.merchantNo} and account_no=#{record.logAccountNo}")
    int updateById(@Param("record")MerchantCardInfo record);
    
    @Select("SELECT * from merchant_card_info where merchant_no=#{mertId} and def_settle_card=1")
    @ResultType(MerchantCardInfo.class)
    MerchantCardInfo selectByMertId(@Param("mertId")String mertId);
    
    @Update("update merchant_card_info set "
    		+ "card_type=#{record.cardType},"
    		+ "account_type=#{record.accountType},bank_name=#{record.bankName},"
    		+ "cnaps_no=#{record.cnapsNo},account_name=#{record.accountName},"
    		+ "account_no=#{record.accountNo},account_province=#{record.accountProvince},account_city=#{record.accountCity},"
    		+ "account_district=#{record.accountDistrict}"
    		+ " where merchant_no=#{record.merchantNo} and account_no=#{record.logAccountNo}")
    int updateByMerId(@Param("record")MerchantCardInfo record);

    @Select("SELECT * from merchant_card_info where merchant_no=#{merchantCardInfo.merchantNo} and account_no=#{merchantCardInfo.logAccountNo}")
    @ResultType(MerchantCardInfo.class)
    MerchantCardInfo selectByMertIdAndAccountNo(@Param("merchantCardInfo")MerchantCardInfo merchantCardInfo);
    
}