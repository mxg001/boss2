package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

public interface ActivityGroupDao {

	@SelectProvider(type=SqlProvider.class, method="checkMutex")
	int checkMutex(@Param("list")List<String> activityTypeList);
	
	public class SqlProvider{
		
		public String checkMutex(Map<String, Object> param){
			final List<String> list = (List<String>) param.get("list");
			String sql = new SQL(){{
				SELECT(" count(1) from activity_group ag1"); 
				INNER_JOIN("activity_group ag2 on ag1.id<>ag2.id and ag2.group_no=ag1.group_no and ag2.relation_type=ag1.relation_type");
				WHERE("ag1.relation_type=0");
				if(list!=null && list.size()>0){
					StringBuilder sb = new StringBuilder();
					MessageFormat message = new MessageFormat("#'{'list[{0}]}");
					for (int i = 0; i < list.size(); i++) {
						sb.append(message.format(new Integer[]{i}));
						sb.append(",");
					}
					sb.setLength(sb.length()-1);
					WHERE("ag1.activity_type in (" + sb + ")");
					WHERE("ag2.activity_type in (" + sb + ")");
				}
			}}.toString();
			System.out.println(sql);
			return sql;
		}
	}

}
