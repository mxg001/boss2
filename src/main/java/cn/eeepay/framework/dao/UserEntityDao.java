package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.eeepay.framework.model.UserEntityInfo;

public interface UserEntityDao {

	@Select("select * from user_entity_info where entity_id=#{agentNo}")
	@ResultType(UserEntityInfo.class)
	List<UserEntityInfo> getByAgent(String agentNo);

	@Select("select user_id from user_entity_info where entity_id=#{agentNo} and user_type='1'" +
			" and apply='1' and manage='1' and is_agent='1' and status='1' order by login_time desc limit 1")
	@ResultType(UserEntityInfo.class)
	UserEntityInfo getUserId(String agentNo);

	@UpdateProvider(type=SqlProvider.class, method="updateStatusBatch")
	int updateStatusBatch(@Param("list")List<UserEntityInfo> userEntityInfoList, @Param("status")String status);
	
	public class SqlProvider{
		public String updateStatusBatch(Map<String, Object>param){
			StringBuilder sb = new StringBuilder("update user_entity_info set status=#{status} where user_id in(");
			List<UserEntityInfo> list = (List<UserEntityInfo>) param.get("list");
			MessageFormat message = new MessageFormat("#'{'list[{0}].userId},");
			for(int i=0;i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			sb.append(")");
			return sb.toString();
		}
	}

}
