package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Msg;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface MsgDao {

    @SelectProvider(type = SqlProvider.class, method = "selectMsgByCondition")
    @ResultType(Msg.class)
    List<Msg> selectMsgByCondition(Page<Msg> page, @Param("msg") Msg msg);

    @Insert("insert into sys_msg_info(" +
            "msg_code,msg_type,module_name,user_msg,reason," +
            "solution,source_org,source_code,source_type,source_msg," +
            "source_remark,create_time,status) values ("
			+ "#{msg.msgCode},#{msg.msgType},#{msg.moduleName},#{msg.userMsg},#{msg.reason},#{msg.solution},"
			+ "#{msg.sourceOrg},#{msg.sourceCode},#{msg.sourceType},#{msg.sourceMsg},#{msg.sourceRemark}," +
            "#{msg.createTime},#{msg.status})")
    int insertMsg(@Param("msg") Msg msg);

    
    
    @Update("update sys_msg_info set msg_type=#{msg.msgType},module_name=#{msg.moduleName},user_msg=#{msg.userMsg},reason=#{msg.reason}"
    		+ ",solution=#{msg.solution},source_org=#{msg.sourceOrg},source_code=#{msg.sourceCode},source_type=#{msg.sourceType},source_msg=#{msg.sourceMsg} "
    		+ ",source_remark=#{msg.sourceRemark},last_update_time=#{msg.last_updateTime},status=#{msg.status}"
    		+ " where id=#{msg.id}")
	int updateMsg(@Param("msg")Msg msg);

    @Update("update sys_msg_info set status=#{status} where id=#{id}")
	int changeStatus(@Param("id")String id, @Param("status")String status);
    
    @Select("select * from sys_msg_info where id=#{id}")
    @ResultType(Msg.class)
    Msg msgDetail(@Param("id")String id);

    @SelectProvider(type = SqlProvider.class, method = "selectByReasonAndOrg")
    @ResultType(Integer.class)
	int selectByReasonAndOrg(@Param("reason")String reason,@Param("sourceOrg")String sourceOrg,@Param("id")Integer id);
    
    @SelectProvider(type = SqlProvider.class, method = "queryByResonAndAcqName")
    @ResultType(String.class)
    String queryByResonAndAcqName(@Param("reason")String resMsg, @Param("sourceOrg")String acqName);
    
    @Select("select user_msg from sys_msg_info where reason=#{reason} LIMIT 1 ")
    String queryMsgByReason(@Param("reason")String resMsg);
    
    @Select("select current_value from sequence where `name`= #{moduleName}")
    String getSeqByKey(@Param("moduleName")String moduleName);

    @Update("update sequence set current_value=#{value} where `name`= #{moduleName}")
	int updateSeq(@Param("value")int value,@Param("moduleName")String moduleName);

    @Select(" SELECT si.*,sd.sys_name as mdName,sd2.sys_name as mtName " +
            " FROM sys_msg_info si  " +
            " LEFT OUTER JOIN sys_dict sd on si.module_name=sd.sys_value AND sd.sys_key ='MODELTYPE'  " +
            " LEFT OUTER JOIN sys_dict sd2 on si.msg_type=sd2.sys_value AND sd2.sys_key ='MSGTYPE'  " +
            " WHERE (si.module_name = 'G' and si.status = 1) " +
            " ORDER BY si.create_time desc ")
    @ResultType(Msg.class)
    List<Msg> queryAgentTips();

    @Select(" SELECT si.*,sd.sys_name as mdName,sd2.sys_name as mtName " +
            " FROM sys_msg_info si " +
            " LEFT OUTER JOIN sys_dict sd on si.module_name=sd.sys_value AND sd.sys_key ='MODELTYPE' " +
            " LEFT OUTER JOIN sys_dict sd2 on si.msg_type=sd2.sys_value AND sd2.sys_key ='MSGTYPE'  " +
            " WHERE (si.msg_code = #{msgCode} ) " +
            " limit 1 ")
    @ResultType(Msg.class)
    Msg queryMsgByMsgCode(String msgCode);

    public class SqlProvider {

        public String selectMsgByCondition(Map<String, Object> param) {
            final Msg msg = (Msg) param.get("msg");
            return new SQL() {{
                SELECT("si.*,sd.sys_name as mdName,sd2.sys_name as mtName");
                FROM("sys_msg_info si ");
                LEFT_OUTER_JOIN("sys_dict sd on si.module_name=sd.sys_value AND sd.sys_key ='MODELTYPE' ");
                LEFT_OUTER_JOIN("sys_dict sd2 on si.msg_type=sd2.sys_value AND sd2.sys_key ='MSGTYPE' ");
                if (msg != null) {
                    if (StringUtils.isNotBlank(msg.getMsgCode())) {
                        msg.setMsgCode(msg.getMsgCode() + "%");
                        WHERE("si.msg_code like #{msg.msgCode}");
                    }
                    if (StringUtils.isNotBlank(msg.getReason())) {
                        msg.setReason("%"+msg.getReason() + "%");
                        WHERE("si.reason like #{msg.reason}");
                    }
                    if (StringUtils.isNotBlank(msg.getSourceOrg())) {
                        WHERE("si.source_org = #{msg.sourceOrg}");
                    }
                    if (StringUtils.isNotBlank(msg.getMsgType())) {
                        WHERE("si.msg_type = #{msg.msgType}");
                    }
                    if (StringUtils.isNotBlank(msg.getModuleName())) {
                        msg.setModuleName(msg.getModuleName());
                        WHERE("si.module_name = #{msg.moduleName}");
                    }
                    if (StringUtils.isNotBlank(msg.getStatus())) {
                        WHERE("si.status = #{msg.status}");
                    }
                }

                ORDER_BY("si.create_time desc");

            }}.toString();
        }
		
        public String selectByReasonAndOrg(Map<String, Object> param) {
            final String reason = (String) param.get("reason");
            final String sourceOrg = (String) param.get("sourceOrg");
            final Integer id = (Integer) param.get("id");
            return new SQL() {{
                SELECT("COUNT(1)");
                FROM("sys_msg_info");
               
                WHERE("reason = #{reason}");
                if(StringUtils.isNotBlank(sourceOrg)){
                	  WHERE("source_org = #{sourceOrg}");
                }else{
                	WHERE("source_org is null");
                }
                if(id!=null){
              	  WHERE("id != #{id}");
                }
                
            }}.toString();
        }
        
        public String queryByResonAndAcqName(Map<String, Object> param) {
        	final String reason = (String) param.get("reason");
        	final String sourceOrg = (String) param.get("sourceOrg");
        	return new SQL() {{
        		SELECT("user_msg");
        		FROM("sys_msg_info");
        		
        		WHERE("reason = #{reason}");
        		if(StringUtils.isNotBlank(sourceOrg)){
        			WHERE("source_org = #{sourceOrg}");
        		}else{
        			WHERE("source_org is null");
        		}
        	}}.toString();
        }
        
    }


}
