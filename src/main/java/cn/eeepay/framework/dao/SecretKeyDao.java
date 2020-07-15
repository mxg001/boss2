package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.SecretKey;
import org.apache.ibatis.annotations.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface SecretKeyDao {


	@Insert("insert into secret_key(device_id,key_type,key_content,device_type,check_value,CREATE_TIME)"
			+ "values(#{record.deviceId},#{record.keyType},#{record.keyContent},#{record.deviceType},#{record.checkValue},now())")
	int insertSelective(@Param("record")SecretKey record);
    
    
    @InsertProvider(type=SqlProvider.class, method="insertBitch")
	int insertBitch(@Param("list")List<SecretKey> list);



	@Select("select * from secret_ts_key where  factory_code = #{factoryCode}")
	@ResultType(Map.class)
	Map<String,Object> findSecretTsKey(@Param("factoryCode") String factoryCode);

	public class SqlProvider{
		
//		public String insertBitch(Map<String, Object> param){
//			final List<SecretKey> list = (List<SecretKey>) param.get("list");
//			StringBuilder sb = new StringBuilder();
//			sb.append("insert into secret_key(device_id,key_type,key_content,device_type,check_value,CREATE_TIME) values ");
//			MessageFormat message = new MessageFormat("(#'{'list[{0}].deviceId},#'{'list[{0}].keyType},#'{'list[{0}].keyContent},"
//					+ "#'{'list[{0}].deviceType},#'{'list[{0}].checkValue},now()),");
//			for(int i=0; i<list.size();i++){
//				sb.append(message.format(new Integer[]{i}));
//			}
//			sb.setLength(sb.length()-1);
////			System.out.println(sb.toString());
//			return sb.toString();
//		}
		public String insertBitch(Map<String, Object> param){
			final List<SecretKey> list = (List<SecretKey>) param.get("list");
			StringBuilder sb = new StringBuilder();
			sb.append("insert into secret_key(device_id,key_type,key_content,device_type,check_value,CREATE_TIME)");
			MessageFormat message = new MessageFormat(" select #'{'list[{0}].deviceId},#'{'list[{0}].keyType},#'{'list[{0}].keyContent},#'{'list[{0}].deviceType},#'{'list[{0}].checkValue},now() from dual "
					+ "where not exists(select 1 from secret_key where device_id = #'{'list[{0}].deviceId}) union all");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-9);
   		    System.out.println(sb.toString());
			return sb.toString();
		}
	}
}