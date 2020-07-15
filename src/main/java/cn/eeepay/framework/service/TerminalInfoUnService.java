package cn.eeepay.framework.service;

import cn.eeepay.framework.model.importLog.ImportLog;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/16/016.
 */
public interface TerminalInfoUnService {

    Map<String, Object> importDiscount(MultipartFile file,HttpServletRequest request) throws Exception;

    ImportLog getImportResult(String batchNo);

}
