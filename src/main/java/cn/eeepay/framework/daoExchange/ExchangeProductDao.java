package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeProduct;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 产品dao
 */
public interface ExchangeProductDao {

    @SelectProvider(type=ExchangeProductDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeProduct.class)
    List<ExchangeProduct> selectAllList(@Param("pro")ExchangeProduct pro,@Param("page")Page<ExchangeProduct> page);

    @SelectProvider(type=ExchangeProductDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeProduct.class)
    List<ExchangeProduct> importDetailSelect(@Param("pro")ExchangeProduct pro);

    @Insert(
            "INSERT INTO rdmp_product_info" +
                    "(product_name,status,type_code,exc_point,exc_price,exc_num," +
                    " settle_day,create_time,min_day,product_shorthand,original_price,underline_writeoff) " +
                    "VALUES " +
                    " (#{pro.productName},'1',#{pro.typeCode},#{pro.excPoint},#{pro.excPrice},#{pro.excNum}," +
                    "  #{pro.settleDay},NOW(),#{pro.minDay},#{pro.productShorthand},#{pro.originalPrice},#{pro.underlineWriteoff})"
    )
    int addExchangeProduct(@Param("pro")ExchangeProduct pro);

    @Select(
            "select * from rdmp_product_info where type_code=#{typeCode}"
    )
    List<ExchangeProduct> getExchangeProductList(@Param("typeCode")String typeCode);

    @Select(
            "select pro.*,type.type_name,type.org_code,org.org_name " +
                    " from rdmp_product_info pro " +
                    " LEFT JOIN rdmp_product_type type ON type.type_code=pro.type_code " +
                    " LEFT JOIN rdmp_org_info org ON org.org_code=type.org_code" +
                    " where pro.id=#{id} "
    )
    ExchangeProduct getExchangeProduct(@Param("id")long id);

    @Update(
            " update rdmp_product_info set product_name=#{pro.productName},type_code=#{pro.typeCode}," +
                    " exc_point=#{pro.excPoint},exc_price=#{pro.excPrice},exc_num=#{pro.excNum}, " +
                    " settle_day=#{pro.settleDay},min_day=#{pro.minDay},product_shorthand=#{pro.productShorthand}," +
                    " original_price=#{pro.originalPrice},underline_writeoff=#{pro.underlineWriteoff} " +
                    " where id=#{pro.id} "
    )
    int updateExchangeProduct(@Param("pro")ExchangeProduct pro);

    @Update(
            "update rdmp_product_info set status='0' where id=#{id} "
    )
    int deleteExchangeProduct(@Param("id")long id);

    @Select(
            "select * from rdmp_product_info " +
                    " where product_name=#{pro.productName} and type_code=#{pro.typeCode} and status='1' "
    )
    List<ExchangeProduct> checkproductName(@Param("pro")ExchangeProduct pro);

    @Select(
            "select * from rdmp_product_info " +
                    "where product_name=#{pro.productName} and type_code=#{pro.typeCode} and id!=#{pro.id} and status='1' "
    )
    List<ExchangeProduct> checkproductNameId(@Param("pro")ExchangeProduct pro);

    @SelectProvider(type=ExchangeProductDao.SqlProvider.class,method="getProductList")
    @ResultType(ExchangeProduct.class)
    List<ExchangeProduct> getProductList(@Param("name")String name,@Param("typeCode")String typeCode);

    @SelectProvider(type=ExchangeProductDao.SqlProvider.class,method="productListSelect")
    @ResultType(ExchangeProduct.class)
    List<ExchangeProduct> productListSelect(@Param("val")String val);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeProduct pro = (ExchangeProduct) param.get("pro");
            return new SQL(){{
                SELECT("pro.*,type.type_name,type.org_code,org.org_name ");
                FROM("rdmp_product_info pro");
                LEFT_OUTER_JOIN("rdmp_product_type type ON type.type_code=pro.type_code");
                LEFT_OUTER_JOIN("rdmp_org_info org ON org.org_code=type.org_code");
                WHERE("pro.status='1' ");
                if(StringUtils.isNotBlank(pro.getProductName())){
                    WHERE("pro.product_name like concat(#{pro.productName},'%') ");
                }
                ORDER_BY("pro.create_time DESC");
            }}.toString();
        }

        public String getProductList(final Map<String, Object> param) {
            final String name = (String) param.get("name");
            final String typeCode = (String) param.get("typeCode");
            StringBuffer sb=new StringBuffer();
            sb.append("select pro.* from rdmp_product_info pro ");
            sb.append("where pro.status='1' ");
            if(StringUtils.isNotBlank(name)){
                sb.append("and pro.product_name like concat(#{name},'%') ");
            }
            if(typeCode!=null&&!"".equals(typeCode)&&!"-1".equals(typeCode)){
                sb.append("and pro.type_code=#{typeCode} ");
            }
            sb.append("limit 100");
            return  sb.toString();
        }

        public String productListSelect(final Map<String, Object> param) {
            String val = (String) param.get("val");
            StringBuffer sb=new StringBuffer();
            sb.append("select pro.* from rdmp_product_info pro ");
            sb.append("where pro.status='1' ");
            if(StringUtils.isNotBlank(val)){
                if(StringUtils.isNumeric(val)){
                    sb.append("and pro.id = #{val} ");
                }else{
                    sb.append("and pro.product_name like concat(#{val},'%') ");
                }
            }
            sb.append("limit 100");
            return  sb.toString();
        }
    }
}
