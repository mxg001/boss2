package cn.eeepay.framework.service.workOrder;

import cn.eeepay.framework.model.workOrder.WorkFileInfo;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 15:55
 */
public interface WorkFileInfoService {
    void insertFiles(Integer belongType, Long belongId, List<WorkFileInfo> files);

    List<WorkFileInfo> getFiles(int belongType, Long belongId);
    List<WorkFileInfo> getImgs(int belongType, Long belongId);
}
