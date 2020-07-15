package cn.eeepay.framework.service.workOrder.impl;

import cn.eeepay.framework.dao.workOrder.WorkFileInfoDao;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.workOrder.WorkFileInfo;
import cn.eeepay.framework.service.workOrder.WorkFileInfoService;
import cn.eeepay.framework.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 15:55
 */
@Service
public class WorkFileInfoServiceImpl implements WorkFileInfoService {

    @Resource
    private WorkFileInfoDao workFileInfoDao;

    @Override
    public void insertFiles(Integer belongType, Long belongId, List<WorkFileInfo> files) {
        if(files!=null && files.size()>0){
            int result = 0;
            for (WorkFileInfo file : files) {
                file.setBelongId(belongId);
                file.setBelongType(belongType);
                int insert = workFileInfoDao.insert(file);
                result +=insert;
            }
            if(files.size()!=result){
                throw new WorkOrderException("批量保存文件失败！");
            }
        }
    }

    @Override
    public List<WorkFileInfo> getFiles(int belongType, Long belongId) {
        List<WorkFileInfo> files = workFileInfoDao.getFiles(belongType, belongId);
        if(files!=null && files.size()>0){
            for (WorkFileInfo file : files) {
                file.setRealUrl(CommonUtil.getImgUrlAgent(file.getFileUrl()));
            }
        }
        return files;
    }
    @Override
    public List<WorkFileInfo> getImgs(int belongType, Long belongId) {
        List<WorkFileInfo> imgs = workFileInfoDao.getImgs(belongType, belongId);
        if(imgs!=null && imgs.size()>0){
            for (WorkFileInfo img : imgs) {
                img.setRealUrl(CommonUtil.getImgUrlAgent(img.getFileUrl()));
            }
        }
        return imgs;
    }
}
