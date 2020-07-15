package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.HardwareProduct;

public interface BusinessProductHardwareDao {

	@Select("select * from hardware_product where hp_id in(select hp_id from business_product_hardware bph where bp_id=#{bpId})")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> findByProduct(@Param("bpId") String bpId);

	@Insert("INSERT INTO business_product_hardware(bp_id,hp_id) values (#{bpId},#{hpId})")
	int insert(@Param("hpId")String hpId, @Param("bpId")String bpId);

	@Delete("DELETE FROM business_product_hardware WHERE bp_id=#{bpId}")
	int deleteProductByPid(@Param("bpId") String id);

	@InsertProvider(type=SqlProvider.class, method="insertBatch")
	int insertBatch(@Param("list")List<HardwareProduct> list, @Param("bpId")String bpId);
	
	public class SqlProvider{
		public String insertBatch(Map<String, Object> param){
			StringBuilder sb = new StringBuilder();
			List<HardwareProduct> list = (List<HardwareProduct>) param.get("list");
			sb.append("insert into business_product_hardware(bp_id,hp_id) values ");
			MessageFormat message = new MessageFormat("(#'{'bpId},(#'{'list[{0}].hpId}+\"\")),");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			System.out.println(sb.toString());
			return sb.toString();
		}
	}
}