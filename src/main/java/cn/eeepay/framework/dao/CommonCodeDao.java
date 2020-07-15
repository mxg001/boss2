package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/3/18 11:25
 */
public interface CommonCodeDao {

    @Select("SELECT agent_no,common_code_url  FROM yfb_oem_service WHERE oem_type = 'nfc' and agent_no=#{info.agentNo}")
    @ResultType(CommonCode.class)
    List<CommonCode> query(@Param("info") CommonCode info);

    @Insert("insert into yfb_oem_service(`agent_no`,`oem_type`,`common_code_url`,`create_time`)values(#{info.agentNo},'nfc',#{info.commonCodeUrl},now())")
    @ResultType(Integer.class)
    int insert(@Param("info")CommonCode info);


    @Update("update yfb_oem_service set common_code_url=#{info.commonCodeUrl} where id = #{info.id}")
    @ResultType(Integer.class)
    int update(@Param("info") CommonCode info);


    @Update("update  yfb_oem_service set common_code_url = null where id = #{id}")
    @ResultType(Integer.class)
    int delById(@Param("id") Long id);



    @SelectProvider(type=CommonCodeDao.SqlProvider.class,method="queryAll")
    @ResultType(CommonCode.class)
    List<CommonCode> queryAll(@Param("page") Page<CommonCode> page,@Param("agentNo")String agentNo);

    @Select("SELECT yos.id,yos.agent_no,yos.common_code_url,ai.agent_name FROM yfb_oem_service yos left join agent_info ai on ai.agent_no = yos.agent_no  WHERE yos.oem_type = 'nfc' and yos.id = #{id}")
    @ResultType(CommonCode.class)
    CommonCode queryById(@Param("id") Long id);

    class SqlProvider{
        public String queryAll(final Map<String, Object> param){
            final String agentNo = (String) param.get("agentNo");
            String sql = "SELECT yos.id,yos.agent_no,yos.common_code_url,ai.agent_name FROM yfb_oem_service yos left join agent_info ai on ai.agent_no = yos.agent_no " +
            " WHERE yos.oem_type = 'nfc'  ";
            if(StringUtils.isNotBlank(agentNo)){
                sql += " and (yos.agent_no = #{agentNo} or ai.agent_name like concat(#{agentNo},'%')) ";
            }
            sql += "order by yos.agent_no ='default' desc,yos.create_time asc";
            return sql;
        }
    }

}
