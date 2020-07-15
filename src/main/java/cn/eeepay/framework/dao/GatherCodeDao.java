package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.GatherCode;

public interface GatherCodeDao {

	@SelectProvider(type=SqlProvider.class, method="selectByParams")
	@ResultType(GatherCode.class)
	List<GatherCode> selectByParams(Page<GatherCode> page, @Param("info")GatherCode gatherCode);
	
	@Select("select g.*,m.merchant_name from gather_code g left join merchant_info m on g.merchant_no=m.merchant_no where g.id=#{id}")
	@ResultType(GatherCode.class)
	GatherCode gatherCodeDetail(String id);
	
	@InsertProvider(type=SqlProvider.class, method="insertBatch")
	int insertBatch(@Param("list")List<GatherCode> list);
	
	@UpdateProvider(type=SqlProvider.class, method="updateStatusBatch")
	int updateStatusBatch(@Param("status")int status,@Param("list")List<GatherCode> list, @Param("recordId")Integer recordId);
	
	public class SqlProvider{
		public String selectByParams(Map<String, Object> params){
			final GatherCode info = (GatherCode) params.get("info");
			String str =  new SQL(){{
				SELECT("g.*,m.merchant_name");
				FROM("gather_code g");
				LEFT_OUTER_JOIN("merchant_info m on g.merchant_no=m.merchant_no");
				if(info!=null && StringUtils.isNoneBlank(info.getSn())){
					WHERE("g.SN like concat('%',#{info.sn},'%')");
				}
				if(info!=null && StringUtils.isNoneBlank(info.getMerchantNo())){
					WHERE("g.merchant_no like concat('%',#{info.merchantNo},'%')");
				}
				if(info!=null && info.getStatus()!=-1){
					WHERE("g.status =#{info.status}");
				}
				if(info!=null && info.getMaterialType()!=-1){
					WHERE("g.material_type = #{info.materialType}");
				}
			}}.toString();
			return str;
		}
		
		public String insertBatch(Map<String, Object> param){
			List<GatherCode> list = (List<GatherCode>) param.get("list");
			StringBuilder sb = new StringBuilder("insert into gather_code(SN,gather_code,status,material_type,create_time) values");
			MessageFormat message = new MessageFormat("(nextval('''gather_code_seq'''),#'{'list[{0}].gatherCode},0,#'{'list[{0}].materialType},now()),");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		
		public String updateStatusBatch(Map<String, Object> param){
			List<GatherCode> list = (List<GatherCode>) param.get("list");
			StringBuilder sb = new StringBuilder("update gather_code set status=#{status},record_id=#{recordId} where id in(");
			MessageFormat message = new MessageFormat("#'{'list[{0}].id},");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			sb.append(")");
			System.out.println(sb.toString());
			return sb.toString();
		}
	}


	
}
