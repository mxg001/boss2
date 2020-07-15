package cn.eeepay.framework.dao.importLog;

import cn.eeepay.framework.model.importLog.ImportLog;
import cn.eeepay.framework.model.importLog.ImportLogEntry;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/13/013.
 * @author  批量导入处理日志类
 */
public interface ImportLogDao {

    @Insert(
           "INSERT INTO import_log " +
                   " (batch_no,log_source,msg,operator,create_time,status)" +
                   " VALUES " +
                   "(#{log.batchNo},#{log.logSource},#{log.msg},#{log.operator},NOW(),'0')"
    )
    @Options(useGeneratedKeys = true, keyProperty = "log.id")
    int insertLog(@Param("log") ImportLog log);

    @Insert(
            "<script> INSERT INTO import_log_entry" +
                    " (batch_no,data1,data2,data3,data4,data5,data6,data7,data8,data9,result) " +
                    " VALUES "+
                    "<foreach  collection=\"list\" item=\"info\" index=\"index\" separator=\",\" > " +
                    "(#{info.batchNo},#{info.data1},#{info.data2},#{info.data3},#{info.data4},#{info.data5}," +
                    " #{info.data6},#{info.data7},#{info.data8},#{info.data9},#{info.result})"+
                    " </foreach ></script>"
    )
    int insertLogEntry(@Param("list")List<ImportLogEntry> entryList);

    @Select(
            "select * from  import_log_entry where batch_no=#{batchNo}"
    )
    List<ImportLogEntry> getImportLogEntryList(@Param("batchNo")String batchNo);

    @Select(
          "select * from import_log where batch_no=#{batchNo} "
    )
    ImportLog getImportLog(@Param("batchNo")String batchNo);

    @Update(
            "update import_log set status='1' where batch_no=#{batchNo} "
    )
    int updateLog(@Param("batchNo")String batchNo);
}
