package cn.eeepay.framework.service.workOrder.impl;

import cn.eeepay.framework.dao.workOrder.WorkRemarkRecordDao;
import cn.eeepay.framework.model.workOrder.WorkFileInfo;
import cn.eeepay.framework.model.workOrder.WorkRemarkRecord;
import cn.eeepay.framework.service.workOrder.WorkFileInfoService;
import cn.eeepay.framework.service.workOrder.WorkRemarkRecordService;
import cn.eeepay.framework.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/5/6 14:54
 */
@Service
public class WorkRemarkRecordServiceImpl implements WorkRemarkRecordService {


    @Resource
    private WorkRemarkRecordDao workRemarkRecordDao;

    @Resource
    private WorkFileInfoService workFileInfoService;


    @Override
    public Long insert(WorkRemarkRecord info) {
        return workRemarkRecordDao.insert(info);
    }

    @Override
    public List<WorkRemarkRecord> getRemarks(int belongType, Long belongId) {
        List<WorkRemarkRecord> remarks = workRemarkRecordDao.getRemarks(belongType, belongId);
        if(remarks!=null && remarks.size()>0){
            for (WorkRemarkRecord remark : remarks) {
                //获取备注附件
                List<WorkFileInfo> remarkFiles = workFileInfoService.getFiles(4,remark.getId());
                remark.setFileInfos(remarkFiles);
                ////获取备注图片
                List<WorkFileInfo> remarkImgs = workFileInfoService.getImgs(4,remark.getId());
                remark.setImgInfos(remarkImgs);
                remark.setCreateTimeStr(DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",remark.getCreateTime()));
            }
        }
        return remarks;
    }
}
