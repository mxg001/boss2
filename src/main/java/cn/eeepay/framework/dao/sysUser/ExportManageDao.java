package cn.eeepay.framework.dao.sysUser;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExportManage;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExportManageDao {

    @Insert(
            " INSERT INTO export_manage" +
                    " (md5_key,create_time,operator,remark,filter_remark,filter_str,status,read_status,download_num,file_name) " +
                    " VALUES " +
                    " (#{info.md5Key},now(),#{info.operator},#{info.remark},#{info.filterRemark},#{info.filterStr},0,0,0,#{info.fileName}) "
    )
    @Options(useGeneratedKeys = true, keyProperty = "info.id")
    int insertExport(@Param("info") ExportManage info);

    @Update(
        "update export_manage set status=#{info.status},file_url=#{info.fileUrl} where id=#{info.id} "
    )
    int updateExportStatus(@Param("info") ExportManage info);

    @Select(
            "select * from export_manage where md5_key = #{md5Key} and status!=3 "
    )
    ExportManage checkExportManageInfo(@Param("md5Key") String md5Key);

    @Update(
            "update export_manage set status=#{info.status},msg=#{info.msg} where id=#{info.id} "
    )
    int updateExportFaile(@Param("info") ExportManage info);

    @SelectProvider(type=ExportManageDao.SqlProvider.class, method="selectAllList")
    @ResultType(ExportManage.class)
    List<ExportManage> selectAllList(@Param("info")ExportManage info,@Param("page") Page<ExportManage> page);

    @Update(
            "update export_manage set read_status=1 where id=#{id} "
    )
    int updateReadStatus(@Param("id")int id);

    @Update(
            "update export_manage set download_num=download_num+1,read_status=1 where id=#{id} "
    )
    int updateDownloadNum(@Param("id")int id);

    @Select(
            "select * from export_manage where id = #{id} "
    )
    ExportManage getExportManageInfoByID(@Param("id")int id);


    @Select(
            "select * from export_manage where read_status=0 and status in (1,2) and operator = #{operator} order by id desc limit 100 "
    )
    @ResultType(ExportManage.class)
    List<ExportManage> getReadInfoList(@Param("operator")String operator);

    @Update(
            "update export_manage set status=3 where id=#{id} "
    )
    int deleteExportManage(@Param("id")int id);

    @Select("select * from export_manage where create_time < #{date} limit 10000")
    List<ExportManage> selectNeedDeleteList(Date date);

    class SqlProvider{
        public String selectAllList(Map<String, Object> param){
            final ExportManage info = (ExportManage) param.get("info");
            String sql = new SQL(){{
                SELECT(" * ");
                FROM(" export_manage ");
                if(StringUtils.isNotBlank(info.getOperator())){
                    WHERE(" operator = #{info.operator} ");
                }
                if(info.getId()!=null&&info.getId().intValue()>=0){
                    WHERE(" id = #{info.id} ");
                }
                if(info.getReadStatus()!=null&&info.getReadStatus().intValue()>=0){
                    WHERE(" read_status = #{info.readStatus} ");
                }
                if(info.getStatus()!=null&&info.getStatus().intValue()>=0){
                    WHERE(" status = #{info.status} ");
                }else{
                    WHERE(" status != 3 ");
                }
                if(StringUtils.isNotBlank(info.getFileName())){
                    WHERE(" file_name like concat('%',#{info.fileName},'%') ");
                }
                ORDER_BY(" id desc ");
            }}.toString();
            return sql;
        }
    }
}
