package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.model.ExportManage;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.sysUser.ExportManageService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import cn.eeepay.framework.serviceImpl.exportBigData.ExportBase;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 定时删除导出文件
 *
 * @author tans
 * @date 2019/11/21 16:30
 */
@Component
@Scope("prototype")
public class DeleteFileJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(DeleteFileJob.class);

    @Resource
    private ExportManageService exportManageService;

    @Resource
    private SysDictService sysDictService;

    @Override
    protected void runTask(String runNo) {
        try {
            log.info("定时删除导出文件,DeleteFileJob.start");
            //查询出指定天数前的所有文件
            int effectiveDays = 7;//默认7天
            String effectiveDaysStr = sysDictService.getValueByKey("file_effective_days");
            if(StringUtil.isNotBlank(effectiveDaysStr) && StringUtil.isNumeric(effectiveDaysStr, false, false)) {
                int days = Integer.parseInt(effectiveDaysStr);
                if(days > 0 && days <= 365) {
                    effectiveDays = days;
                }
            }

            Date date = DateUtil.getBeforeDate2(DateUtil.getNowDateShort(), effectiveDays);
            //查询7天前的文件，最多查询10000条
            List<ExportManage> list = exportManageService.selectNeedDeleteList(date);
            //逐条删除
            if(list != null && list.size() > 0) {
                //文件根路径
                String baseUrl = sysDictService.getValueByKey(ExportBase.FILE_URL);
                baseUrl += File.separator + ExportBase.FILENAME;
                for(ExportManage item: list) {
                    String fileUrl = item.getFileUrl();
                    if(StringUtil.isEmpty(fileUrl)) {
                        continue;
                    }
                    File file = new File(baseUrl + File.separator + fileUrl);
                    if(file.exists()) {
                        if(file.delete()) {
                            exportManageService.deleteExportManage(item.getId());
                        }
                    }
                }
            }
        } catch (ParseException e) {
            log.error("定时删除导出文件异常", e);
        }
        log.info("定时删除导出文件,DeleteFileJob.end");

    }
}
