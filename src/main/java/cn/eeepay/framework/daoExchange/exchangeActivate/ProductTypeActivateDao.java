package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ProductTypeActivate;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 超级兑，产品类别
 */
public interface ProductTypeActivateDao {

    @SelectProvider(type=ProductTypeActivateDao.SqlProvider.class,method="selectAllList")
    @ResultType(ProductTypeActivate.class)
    List<ProductTypeActivate> selectAllList(@Param("pt") ProductTypeActivate pt, @Param("page") Page<ProductTypeActivate> page);

    @SelectProvider(type=ProductTypeActivateDao.SqlProvider.class,method="selectAllList")
    @ResultType(ProductTypeActivate.class)
    List<ProductTypeActivate> importDetailSelect(@Param("pt") ProductTypeActivate pt);

    @Insert(
            "INSERT INTO yfb_product_type " +
                    " (type_name,type_code,type_status,declara_type,create_time,org_code,video_url," +
                    "  course_url,bank_url,hint) " +
                    " VALUES " +
                    " (#{pt.typeName},#{pt.typeCode},'1',#{pt.declaraType},NOW(),#{pt.orgCode}," +
                    " #{pt.videoUrl},#{pt.courseUrl},#{pt.bankUrl},#{pt.hint}) "
    )
    int addProductType(@Param("pt") ProductTypeActivate pt);

    /**
     *逻辑删除
     */
    @Update(
            " update yfb_product_type set type_status='0' where id=#{id}"
    )
    int deleteProductType(@Param("id") long id);

    @Update(
            " update yfb_product_type set " +
                    " type_name=#{pt.typeName},declara_type=#{pt.declaraType},org_code=#{pt.orgCode}," +
                    " video_url=#{pt.videoUrl},course_url=#{pt.courseUrl},bank_url=#{pt.bankUrl}," +
                    " hint=#{pt.hint} " +
                    " where id=#{pt.id}"
    )
    int editProductType(@Param("pt") ProductTypeActivate pt);

    @Select(
            " select * from yfb_product_type where id=#{id}"
    )
    ProductTypeActivate getProductType(@Param("id") long id);


    @SelectProvider(type=ProductTypeActivateDao.SqlProvider.class,method="getProductTypeList")
    @ResultType(ProductTypeActivate.class)
    List<ProductTypeActivate> getProductTypeList(@Param("orgCode") String orgCode);

    @Select(
            " select * from yfb_product_type where type_name=#{pt.typeName} and org_code=#{pt.orgCode} and type_status='1'"
    )
    List<ProductTypeActivate> checkOrgName(@Param("pt") ProductTypeActivate pt);

    @Select(
            " select * from yfb_product_type " +
                    "where type_name=#{pt.typeName} and org_code=#{pt.orgCode} and id!=#{pt.id} and type_status='1' "
    )
    List<ProductTypeActivate> checkOrgNameId(@Param("pt") ProductTypeActivate pt);

    @Select(
            "select * from yfb_product_type where  type_code=#{typeCode}"
    )
    List<ProductTypeActivate> getProductTypeCode(@Param("typeCode") String typeCode);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ProductTypeActivate pt = (ProductTypeActivate) param.get("pt");
            return new SQL(){{
                SELECT("pt.*,org.org_name ");
                FROM("yfb_product_type pt");
                LEFT_OUTER_JOIN("yfb_org_info org ON org.org_code=pt.org_code ");
                WHERE("pt.type_status='1'");
                if(StringUtils.isNotBlank(pt.getTypeName())){
                    WHERE("pt.type_name like concat(#{pt.typeName},'%') ");
                }
                if(StringUtils.isNotBlank(pt.getOrgCode())){
                    WHERE("pt.org_code = #{pt.orgCode} ");
                }
                ORDER_BY("pt.create_time DESC");
            }}.toString();
        }

        public String getProductTypeList(final Map<String, Object> param) {
            final String orgCode = (String) param.get("orgCode");
            return new SQL(){{
                SELECT("pt.*");
                FROM("yfb_product_type pt");
                WHERE("pt.type_status='1'");
                if(StringUtils.isNotBlank(orgCode)){
                    WHERE("pt.org_code =#{orgCode} ");
                }
                ORDER_BY("pt.create_time DESC");
            }}.toString();
        }
    }
}
