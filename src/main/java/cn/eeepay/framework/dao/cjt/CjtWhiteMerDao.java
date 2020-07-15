package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtWhiteMer;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 超级推商户白名单 数据层
 * 
 * @author tans
 * @date 2019-05-29
 */
public interface CjtWhiteMerDao {
	
	/**
     * 查询超级推商户白名单列表
     * 
     * @param baseInfo 超级推商户白名单信息
     * @return 超级推商户白名单集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectPage")
	public List<CjtWhiteMer> selectPage(@Param("page") Page<CjtWhiteMer> page,
										@Param("baseInfo") CjtWhiteMer baseInfo);
	
	/**
     * 新增超级推商户白名单
     * 
     * @param cjtWhiteMer 超级推商户白名单信息
     * @return 结果
     */
	@Insert("insert into cjt_white_mer(merchant_no,status,remark,create_time,creater)" +
			" values (#{merchantNo},#{status},#{remark},#{createTime},#{creater})")
	public int insert(CjtWhiteMer cjtWhiteMer);
	
	/**
     * 修改超级推商户白名单
     * 
     * @param cjtWhiteMer 超级推商户白名单信息
     * @return 结果
     */
	@Update("update cjt_white_mer set merchant_no=#{merchantNo},remark=#{remark}" +
			" where id=#{id} ")
	public int update(CjtWhiteMer cjtWhiteMer);

	@Delete("delete from cjt_white_mer where id = #{id}")
	int delete(int id);

	@Select("select * from cjt_white_mer where merchant_no = #{merchantNo}")
	@ResultType(CjtWhiteMer.class)
	CjtWhiteMer selectDetailByMer(String merchantNo);

	@Update("update cjt_white_mer set status = #{status} where id = #{id}")
	int updateStatus(CjtWhiteMer baseInfo);

	class SqlProvider{
		public String selectPage(Map<String, Object> param){
			CjtWhiteMer baseInfo = (CjtWhiteMer) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("cwm.*, mi.merchant_name");
			sql.FROM("cjt_white_mer cwm");
			sql.LEFT_OUTER_JOIN("merchant_info mi on mi.merchant_no = cwm.merchant_no");
			if(StringUtils.isNotEmpty(baseInfo.getMerchantNo())) {
				sql.WHERE("cwm.merchant_no = #{baseInfo.merchantNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getMerchantName())) {
				baseInfo.setMerchantName("%" + baseInfo.getMerchantName() + "%");
				sql.WHERE("mi.merchant_name like #{baseInfo.merchantName}");
			}
			if(baseInfo.getStatus() != null) {
				sql.WHERE("cwm.status = #{baseInfo.status}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeStart())){
				sql.WHERE("cwm.create_time >= #{baseInfo.createTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeEnd())){
				sql.WHERE("cwm.create_time <= #{baseInfo.createTimeEnd}");
			}
			sql.ORDER_BY("cwm.create_time desc");
			return sql.toString();
		}
	}
}