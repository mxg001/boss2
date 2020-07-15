package cn.eeepay.framework.service.sysUser;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExportManage;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExportManageService {

    boolean checkExportManageInfo(String md5Key,Map<String, Object> msg);

    List<ExportManage> selectAllList(ExportManage info, Page<ExportManage> page);

    int updateReadStatus(int id);

    ExportManage getExportManageInfoByID(int id);

    void downloadFile(ExportManage info, HttpServletResponse response, Map<String, Object> msg);

    List<ExportManage> getReadInfoList(String username);

    int deleteExportManage(int id);

    List<ExportManage> selectNeedDeleteList(Date date);

}
