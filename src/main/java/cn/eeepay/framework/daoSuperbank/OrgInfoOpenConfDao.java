package cn.eeepay.framework.daoSuperbank;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.eeepay.framework.model.OrgInfoOpenConf;
import cn.eeepay.framework.util.DateUtil;

public interface OrgInfoOpenConfDao {

    @Insert("insert into org_info_open_conf (org_id,function_id,is_enable,create_by,create_date,update_by,update_date)"
    		+ "values( #{orgid}, #{functionId}, #{isEnable}, #{create_by}, #{createdate}, #{updateby},#{updatedate})")
    int insert(OrgInfoOpenConf orgInfoOpenConf);

    @InsertProvider(type = SqlProvider.class, method = "insertBatch")  
    void insertBatch(@Param("list") List<OrgInfoOpenConf> orgInfoOpenConfs);  
    
    @Update("update org_info_open_conf set org_id=#{orgId},function_id=#{functionId},is_enable=#{isEnable}," +
            "create_by=#{createBy},create_date=#{createDate},update_by=#{updateBy},update_date=#{updateDate}" +
            " where id = #{id}")
    int update(OrgInfoOpenConf orgInfoOpenConf);
    
    @Update("update org_info_open_conf set org_id=#{orgId},function_id=#{functionId},is_enable=#{isEnable}," +
            "create_by=#{createBy},create_date=#{createDate},update_by=#{updateBy},update_date=#{updateDate}" +
            " where org_id=#{orgId} and function_id=#{functionId}")
    int updateByOrgIdAndFunctionId(OrgInfoOpenConf orgInfoOpenConf);


    @Select("select org_id,function_id,is_enable,create_by,create_date,update_by,update_date from org_info_open_conf where org_id=#{orgId}")
    @ResultType(OrgInfoOpenConf.class)
    List<OrgInfoOpenConf> getOrgInfoList(long orgId);
    
    
    @UpdateProvider(type = SqlProvider.class, method = "updateAll")
    void updateAll(@Param("list") Collection<OrgInfoOpenConf> OrgInfoOpenConfS);
    
    @Select("select count(id) from org_info_open_conf where function_id=#{functionId} and org_id=#{orgId}")
	int checkExistsOrgIdAndFuncId(@Param("functionId") long functionId,@Param("orgId") Long orgId);
    
    public class SqlProvider{
        public String updateAll(Map<String, Object> param){
        	List<OrgInfoOpenConf> orgInfoOpenConfs = (List<OrgInfoOpenConf>) param.get("list");
        	OrgInfoOpenConf orgInfoOpenConf=orgInfoOpenConfs.get(0);
            StringBuilder sb = new StringBuilder();
            Date dateNow=new Date();
            String now=DateUtil.getLongFormatDate(dateNow);
            sb.append("update org_info_open_conf ");
            sb.append("set IS_ENABLE= '"+ orgInfoOpenConf.getIsEnable()+ "' , UPDATE_BY='"+orgInfoOpenConf.getUpdateBy()+"',UPDATE_DATE='"+now+"'");
            sb.append("where FUNCTION_ID in(");
            for(OrgInfoOpenConf record:orgInfoOpenConfs){
            	sb.append(record.getFunctionId()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(") and ORG_ID ='"+orgInfoOpenConf.getOrgId()+"'");
            return sb.toString();
    }
        public String insertBatch(Map map) {  
            List<OrgInfoOpenConf> orgInfoOpenConfs = (List<OrgInfoOpenConf>) map.get("list");  
            StringBuilder sb = new StringBuilder();  
            sb.append("INSERT INTO org_info_open_conf ");  
            sb.append("(org_id,function_id,is_enable,create_by,create_date,update_by,update_date) ");  
            sb.append("VALUES ");  
            MessageFormat mf = new MessageFormat("( #'{'list[{0}].orgId},#'{'list[{0}].functionId},#'{'list[{0}].isEnable},#'{'list[{0}].createBy},#'{'list[{0}].createDate},#'{'list[{0}].updateBy},#'{'list[{0}].updateDate})");  
            for (int i = 0; i < orgInfoOpenConfs.size(); i++) {  
                sb.append(mf.format(new Object[]{i}));  
                if (i < orgInfoOpenConfs.size() - 1) {  
                    sb.append(",");  
                }  
            }  
            return sb.toString();  
        }  
    }

}
