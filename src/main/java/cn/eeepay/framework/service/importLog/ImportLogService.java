package cn.eeepay.framework.service.importLog;

import cn.eeepay.framework.model.importLog.ImportLog;
import cn.eeepay.framework.model.importLog.ImportLogEntry;

import java.util.List;

/**
 * Created by Administrator on 2018/11/13/013.
 * @author liuks
 * 导入日志记录
 */
public interface ImportLogService {

    int insertLog(ImportLog log);

    int insertLogEntry(List<ImportLogEntry> entryList);

    List<ImportLogEntry> getImportLogEntryList(String batchNo);

    ImportLog getImportLog(String batchNo);

    int updateLog(String batchNo);
}
