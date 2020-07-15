package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.CountSet;
import cn.eeepay.framework.model.allAgent.GoodAllAgent;
import cn.eeepay.framework.model.allAgent.GoodsPrice;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author  liuks
 * 商品dao
 */
public interface GoodAllAgentDao {

    @SelectProvider(type=GoodAllAgentDao.SqlProvider.class,method="selectAllList")
    @ResultType(GoodAllAgent.class)
    List<GoodAllAgent> selectAllList(@Param("good")GoodAllAgent good, @Param("page") Page<GoodAllAgent> page);

    @SelectProvider(type=GoodAllAgentDao.SqlProvider.class,method="selectAllSum")
    @ResultType(CountSet.class)
    CountSet selectAllSum(@Param("good")GoodAllAgent good, @Param("page") Page<GoodAllAgent> page);

    @Insert(
            " INSERT INTO pa_goods " +
                    "(goods_name,goods_code,goods_desc,img," +
                    " img2,img3,price,cost,minimum," +
                    " is_multi,status,brand_code,create_time,creater,group_code,detail_imgs,shipper,ship_way)" +
                    "VALUES " +
                    " (#{good.goodsName},#{good.goodsCode},#{good.goodsDesc},#{good.img}" +
                    ",#{good.img2},#{good.img3},#{good.price},#{good.cost},#{good.minimum}" +
                    ",#{good.isMulti},0,#{good.brandCode},NOW(),#{good.creater}"+
                    ",#{good.groupCode},#{good.detailImgs},#{good.shipper},#{good.shipWay}"+
                    ")"
    )
    int addGood(@Param("good")GoodAllAgent good);

    @Insert("INSERT INTO pa_goods_price (goods_code,price,cost,agio,color,size) VALUES " +
            "(#{gp.goodsCode},#{gp.price},#{gp.cost},#{gp.agio},#{gp.color},#{gp.size})")
    int addGoodsPrice(@Param("gp") GoodsPrice goodsPrice);

    @Update("update pa_goods_price set price=#{gp.price},cost=#{gp.cost},agio=#{gp.agio},color=#{gp.color},size=#{gp.size} where id=#{gp.id}")
    int updateGoodsPrice(@Param("gp") GoodsPrice goodsPrice);

    @Delete("delete from pa_goods_price where id = #{id}")
    int deleteGoodsPrice(@Param("id") Integer id);

    @Select("select * from pa_goods_price where goods_code = #{goodsCode}")
    @ResultType(GoodsPrice.class)
    List<GoodsPrice> getGoodsPrice(@Param("goodsCode") String  goodsCode);

    @Select(
            "select good.* from pa_goods good where good.id=#{id}"
    )
    GoodAllAgent getGood(@Param("id")int id);

    @Select("select good.* from pa_goods good where good.goods_code=#{goodsCode}")
    GoodAllAgent getGoodsCode(@Param("goodsCode") String  goodsCode);

    @UpdateProvider(type=GoodAllAgentDao.SqlProvider.class,method="saveGood")
    int saveGood(@Param("good")GoodAllAgent good);

    @Update(
            "update pa_goods set status=#{status} where id=#{id}"
    )
    int updateGood(@Param("id")int id,@Param("status")int status);

    @DeleteProvider(type=GoodAllAgentDao.SqlProvider.class,method="deleteGoodImg")
    int deleteGoodImg(@Param("id")int id,@Param("status")int status);

    @UpdateProvider(type=SqlProvider.class,method="updateListType")
    int updateListType(Map params);

    class SqlProvider {

        public String selectAllList(final Map<String, Object> param) {
            return selectAllSql(param,1);
        }
        public String selectAllSum(final Map<String, Object> param) {
            return selectAllSql(param,2);
        }

        private String selectAllSql(final Map<String, Object> param,int sta) {
            GoodAllAgent good = (GoodAllAgent) param.get("good");
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(sta==1){
                sb.append("   good.id,good.goods_name,good.goods_code,good.goods_desc,good.img,good.img2,good.img3," +
                        "   good.minimum,good.is_multi,good.status,good.brand_code," +
                        "   good.produte_type,good.create_time,good.creater,good.update_time,good.group_code," +
                        "   good.list_type,good.detail_imgs,good.shipper,good.ship_way,oem.brand_name," +
                        "   pgg.group_name," +
                        " (SELECT IF(min(pgp.price)=MAX(pgp.price),min(pgp.price),CONCAT(min(pgp.price),'-',MAX(pgp.price))) from pa_goods_price pgp where pgp.goods_code=good.goods_code) price ");
            }else if(sta==2){

            }
            sb.append("  From pa_goods good ");
            sb.append("   LEFT JOIN pa_goods_group pgg on pgg.group_code=good.group_code ");
            sb.append("   LEFT JOIN pa_brand oem ON oem.brand_code=good.brand_code ");
            sb.append("  where 1=1 ");
            if (StringUtils.isNotBlank(good.getGoodsCode())) {
                sb.append("  and good.goods_code =#{good.goodsCode} ");
            }
            if (StringUtils.isNotBlank(good.getGoodsName())) {
                sb.append("  and good.goods_name like concat(#{good.goodsName},'%') ");
            }
            if (good.getStatus() != null) {
                sb.append("  and good.status =#{good.status} ");
            }
            if (StringUtils.isNotBlank(good.getBrandCode())) {
                sb.append("  and good.brand_code =#{good.brandCode} ");
            }
            if (StringUtils.isNotBlank(good.getGroupCode())) {
                sb.append("  and good.group_code =#{good.groupCode} ");
            }
            if (StringUtils.isNotBlank(good.getListType())) {
                sb.append("  and good.list_type =#{good.listType} ");
            }
            if (good.getShipWay()!=null) {
                sb.append("  and good.ship_way =#{good.shipWay} ");
            }
            if (good.getCreateTimeBegin() != null) {
                sb.append("  and good.create_time >= #{good.createTimeBegin}");
            }
            if (good.getCreateTimeEnd() != null) {
                sb.append("  and good.create_time <= #{good.createTimeEnd}");
            }
            sb.append("  order by good.update_time desc ");
            return sb.toString();
        }

        public String saveGood(final Map<String, Object> param) {
            final GoodAllAgent good = (GoodAllAgent) param.get("good");
            StringBuffer sb = new StringBuffer();
            sb.append("update pa_goods set ");
            sb.append(" goods_name=#{good.goodsName},goods_desc=#{good.goodsDesc}, ");
            sb.append(" minimum=#{good.minimum},is_multi=#{good.isMulti} ");
            if (StringUtils.isNotBlank(good.getImg())) {
                sb.append(" ,img=#{good.img} ");
            }
            if (StringUtils.isNotBlank(good.getImg2())) {
                sb.append(" ,img2=#{good.img2} ");
            }
            if (StringUtils.isNotBlank(good.getImg3())) {
                sb.append(" ,img3=#{good.img3} ");
            }
            sb.append(" ,detail_imgs=#{good.detailImgs}");
            sb.append(" ,group_code=#{good.groupCode}");
            sb.append(" ,brand_code=#{good.brandCode}");
            sb.append(" ,ship_way=#{good.shipWay}");
            sb.append(" ,shipper=#{good.shipper}");
            sb.append(" where id=#{good.id} ");
            return sb.toString();
        }

        public String deleteGoodImg(final Map<String, Object> param) {
            int status = Integer.parseInt(param.get("status").toString());
            StringBuffer sb = new StringBuffer();
            sb.append("update pa_goods set ");
            if (status==2) {
                sb.append(" img2=null ");
            }else if(status==3){
                sb.append(" img3=null ");
            }
            sb.append(" where id=#{id} ");
            return sb.toString();
        }

        public String updateListType(final Map<String, Object> params){
            StringBuffer sb = new StringBuffer("UPDATE pa_goods SET list_type=#{list_type} WHERE goods_code=#{goods_code} and id=#{id}");
            return sb.toString();
        }
    }
}
