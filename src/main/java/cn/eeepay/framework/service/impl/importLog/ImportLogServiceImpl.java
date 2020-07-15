package cn.eeepay.framework.service.impl.importLog;

import cn.eeepay.framework.dao.importLog.ImportLogDao;
import cn.eeepay.framework.model.importLog.ImportLog;
import cn.eeepay.framework.model.importLog.ImportLogEntry;
import cn.eeepay.framework.service.importLog.ImportLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/11/13/013.
 * @author  liuks
 * 导入处理结果日志service实现类
 */
@Service("importLogService")
public class ImportLogServiceImpl implements ImportLogService {

    @Resource
    private ImportLogDao importLogDao;


    @Override
    public int insertLog(ImportLog log) {
        int num =importLogDao.insertLog(log);
        return num;
    }

    @Override
    public int insertLogEntry(List<ImportLogEntry> entryList) {
        int num =importLogDao.insertLogEntry(entryList);
        return num;
    }

    @Override
    public List<ImportLogEntry> getImportLogEntryList(String batchNo) {
        return importLogDao.getImportLogEntryList(batchNo);
    }

    @Override
    public ImportLog getImportLog(String batchNo) {
        return importLogDao.getImportLog(batchNo);
    }

    @Override
    public int updateLog(String batchNo) {
        return importLogDao.updateLog(batchNo);
    }
}
