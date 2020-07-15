package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.ZqFileSync;
import cn.eeepay.framework.service.ZqFileSyncService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * @author tans
 * @date 2019-04-11
 */
@RestController
@RequestMapping("/zqFileSync")
public class ZqFileSyncAction {

    private static final Logger log = LoggerFactory.getLogger(ZqFileSyncAction.class);

    @Resource
    private ZqFileSyncService zqFileSyncService;

    /**
     * 条件查询报报备
     * @param zqFileSync
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestBody ZqFileSync zqFileSync,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<ZqFileSync> page = new Page<>(pageNo, pageSize);
            zqFileSyncService.selectPage(page, zqFileSync);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询报报备异常", e);
        }
        return result;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/export")
    @SystemLog(operCode = "zqFileSync.export", description = "导出")
    public void export(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            ZqFileSync zqServiceInfo = JSONObject.parseObject(baseInfo, ZqFileSync.class);
            zqFileSyncService.export(response, zqServiceInfo);
        } catch (Exception e){
            log.error("导出商户批量报备异常", e);
        }
    }

    @RequestMapping("/importData")
    @ResponseBody
    public Result importData(@RequestParam("file") MultipartFile file,
                             @RequestParam String channelCode){
        Result result = new Result();
        try {
            if(StringUtils.isBlank(channelCode)){
                result.setMsg("收单机构不能为空");
                return result;
            }
            if(!CommonUtil.checkExcelFile(file, result)){
                return result;
            }
            result = zqFileSyncService.importData(file, channelCode);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("导入直清商户批量报备异常", e);
        }
        return result;
    }

    @RequestMapping("/updateStatus")
    @ResponseBody
    @SystemLog(operCode = "zqFileSync.updateStatus", description = "修改状态")
    public Result updateStatus(@RequestBody ZqFileSync baseInfo){
        Result result = new Result();
        try {
            if(baseInfo == null || StringUtils.isBlank(baseInfo.getBatchNo())
                    || StringUtils.isBlank(baseInfo.getStatus())){
                return Result.fail("参数不能为空");
            }
            result = zqFileSyncService.updateStatus(baseInfo);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改状态异常", e);
        }
        return result;
    }

}
