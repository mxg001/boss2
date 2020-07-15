package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceProduct;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface InsuranceProductDao {

    @SelectProvider(type = SqlProvider.class, method = "selectList")
    @ResultType(InsuranceProduct.class)
    List<InsuranceProduct> selectList(@Param("baseInfo") InsuranceProduct baseInfo, @Param("page") Page<InsuranceProduct> page);

    @Select("select ip.* from insurance_product ip where ip.product_id = #{productId}")
    @ResultType(InsuranceProduct.class)
    InsuranceProduct selectDetail(@Param("productId") Long productId);

    @Insert("insert into insurance_product (product_id,upper_product_id, product_name, company_no, " +
            "      product_type, recommend_status,status, show_order," +
            "      product_price, bonus_type, bonus_settle_time, " +
            "      title1, title2, title3, " +
            "      h5_link, product_image, create_by, " +
            "      update_by, create_date, update_date" +
            "      )" +
            "    values (#{productId}, #{upperProductId},#{productName}, #{companyNo}, " +
            "      #{productType}, #{recommendStatus}, #{status}, #{showOrder}, " +
            "      #{productPrice}, #{bonusType}, #{bonusSettleTime}, " +
            "      #{title1}, #{title2}, #{title3}, " +
            "      #{h5Link}, #{productImage}, #{createBy}, " +
            "      #{updateBy}, #{createDate}, #{updateDate}" +
            "      )")
    int insert(InsuranceProduct info);

    @Update("update insurance_product " +
            "    set product_name = #{productName }, " +
            "      company_no = #{companyNo}, " +
            "      product_type = #{productType}, " +
            "      upper_product_id = #{upperProductId}, " +
            "      recommend_status = #{recommendStatus}, " +
            "      status = #{status}, " +
            "      show_order = #{showOrder}, " +
            "      product_price = #{productPrice}, " +
            "      bonus_type = #{bonusType}, " +
            "      bonus_settle_time = #{bonusSettleTime}, " +
            "      title1 = #{title1}, " +
            "      title2 = #{title2}, " +
            "      title3 = #{title3}, " +
            "      h5_link = #{h5Link}, " +
            "      product_image = #{productImage}, " +
            "      create_by = #{createBy}, " +
            "      update_by = #{updateBy}, " +
            "      create_date = #{createDate}, " +
            "      update_date = #{updateDate} " +
            "    where product_id = #{productId}")
    int updateProduct(InsuranceProduct info);

    @Update("update insurance_product set status = #{status},update_by=#{updateBy},update_date=#{updateDate} where product_id = #{productId}")
    int updateProductStatus(InsuranceProduct info);

    @Select("select count(1) from insurance_product where show_order = #{showOrder}")
    int selectOrderExists(@Param("showOrder") Integer showOrder);

    @Select("select count(1) from insurance_product where show_order = #{showOrder} and product_id <> #{productId}")
    int selectOrderIdExists(@Param("showOrder") Integer showOrder, @Param("productId") Long productId);


    @Select("select count(1) from insurance_product where upper_product_id = #{upperProductId}")
    int selectProductIdExists(@Param("upperProductId") String upperProductId);

    @Select("select count(1) from insurance_product where upper_product_id = #{upperProductId} and product_id <> #{productId}")
    int selectProductAndIdExists(@Param("upperProductId") String upperProductId, @Param("productId") Long productId);

    @Select("select count(1) from insurance_product where product_name = #{productName}")
    int selectProductNameExists(@Param("productName") String productName);

    @Select("select count(1) from insurance_product where product_name = #{productName} and product_id <> #{productId}")
    int selectProductNameIdExists(@Param("productName") String productName, @Param("productId") Long productId);

    @Select("select * from insurance_product")
    @ResultType(InsuranceProduct.class)
    List<InsuranceProduct> getListAll();


    @Select("select * from insurance_product where company_no=#{companyNo} ")
    @ResultType(InsuranceProduct.class)
    List<InsuranceProduct> getByCompanyNo(int companyNo);

    @Delete("delete from insurance_product where product_id =#{productId}")
    int deleteProduct(int productId);

    class SqlProvider{
        public String selectList(Map<String, Object> param){
            InsuranceProduct baseInfo = (InsuranceProduct) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("ip.*");
            sql.FROM("insurance_product ip");
            sql.LEFT_OUTER_JOIN("insurance_company ic on ic.company_no = ip.company_no");
            if(baseInfo != null){
                if(baseInfo.getCompanyNo()!=null){
                    sql.WHERE("ip.company_no = #{baseInfo.companyNo}");
                }
                if(baseInfo.getProductType()!=null){
                    sql.WHERE("ip.product_type = #{baseInfo.productType}");
                }
                if(StringUtils.isNotBlank(baseInfo.getProductName())){
                    baseInfo.setProductName(baseInfo.getProductName() + "%");
                    sql.WHERE("ip.product_name like #{baseInfo.productName}");
                }
            }
            sql.ORDER_BY("ip.create_date desc");
            return sql.toString();
        }
    }
    
}
