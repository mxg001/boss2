package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.cjt.CjtGoods;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 超级推商品 数据层
 * 
 * @author tans
 * @date 2019-05-28
 */
public interface CjtGoodsDao {
	/**
     * 查询超级推商品信息
     * 
     * @param goodsCode 超级推商品编号
     * @return 超级推商品信息
     */
	@Select("select * from cjt_goods where goods_code = #{goodsCode}")
	@ResultType(CjtGoods.class)
	public CjtGoods selectCjtGoodsDetail(String goodsCode);
	
	/**
     * 查询超级推商品列表
     * 
     * @param baseInfo 超级推商品信息
     * @return 超级推商品集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectCjtGoodsPage")
	@ResultType(CjtGoods.class)
	public List<CjtGoods> selectCjtGoodsPage(@Param("page") Page<CjtGoods> page,
                                             @Param("baseInfo") CjtGoods baseInfo);
	
	/**
     * 新增超级推商品
     * 
     * @param cjtGoods 超级推商品信息
     * @return 结果
     */
	@Insert("insert into cjt_goods(goods_code,status,white_status,goods_name,goods_desc," +
			" main_img,desc_img,price,size,color,min_num,hp_id,creater,good_order_type)" +
			" values(#{goodsCode},#{status},#{whiteStatus},#{goodsName},#{goodsDesc}," +
			" #{mainImg},#{descImg},#{price},#{size},#{color},#{minNum},#{hpId},#{creater},#{goodOrderType})")
	public int insertCjtGoods(CjtGoods cjtGoods);
	
	/**
     * 修改超级推商品
     * 
     * @param cjtGoods 超级推商品信息
     * @return 结果
     */
	@Update("update cjt_goods set goods_name = #{goodsName},goods_desc=#{goodsDesc}," +
			"main_img=#{mainImg},desc_img=#{descImg},price=#{price},size=#{size}," +
			"color=#{color},min_num=#{minNum},hp_id=#{hpId}" +
			" where goods_code=#{goodsCode}")
	public int updateCjtGoods(CjtGoods cjtGoods);

	@Update("update cjt_goods set status = #{status} where goods_code = #{goodsCode}")
	int updateStatus(CjtGoods cjtGoods);

	@Update("update cjt_goods set white_status = #{whiteStatus} where goods_code = #{goodsCode}")
	int updateWhiteStatus(CjtGoods cjtGoods);

	@Select("select goods_code,goods_name,main_img from cjt_goods")
	@ResultType(CjtGoods.class)
	List<CjtGoods> selectList();

    class SqlProvider {
		public String selectCjtGoodsPage(Map<String, Object> param) {
			CjtGoods baseInfo  = (CjtGoods) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("cg.*");
			sql.SELECT("hp.type_name");
			sql.FROM("cjt_goods cg");
			sql.LEFT_OUTER_JOIN("hardware_product hp on hp.hp_id = cg.hp_id");
			if(StringUtils.isNotEmpty(baseInfo.getGoodsCode())){
				sql.WHERE("cg.goods_code = #{baseInfo.goodsCode}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getGoodsName())){
				baseInfo.setGoodsName("%" + baseInfo.getGoodsName() + "%");
				sql.WHERE("cg.goods_name like #{baseInfo.goodsName}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getStatus())){
				sql.WHERE("cg.status = #{baseInfo.status}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getWhiteStatus())){
				sql.WHERE("cg.white_status = #{baseInfo.whiteStatus}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeStart())){
				sql.WHERE("cg.create_time >= #{baseInfo.createTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeEnd())){
				sql.WHERE("cg.create_time <= #{baseInfo.createTimeEnd}");
			}
			if(baseInfo.getHpId() != null){
				sql.WHERE("cg.hp_id = #{baseInfo.hpId}");
			}
			if(baseInfo.getGoodOrderType() != null){
				sql.WHERE("cg.good_order_type = #{baseInfo.goodOrderType}");
			}
			sql.ORDER_BY("cg.create_time desc");
			return sql.toString();
		}
	}

	
}