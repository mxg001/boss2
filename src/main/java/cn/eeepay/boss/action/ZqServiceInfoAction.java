package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.ZqFileSync;
import cn.eeepay.framework.model.ZqServiceInfo;
import cn.eeepay.framework.service.ZqServiceInfoService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author tans
 * @date 2019-04-02
 */
@RestController
@RequestMapping("/zqServiceInfo")
public class ZqServiceInfoAction {

    private static final Logger log = LoggerFactory.getLogger(ZqServiceInfoAction.class);

    @Resource
    private ZqServiceInfoService zqServiceInfoService;

    /**
     * 条件查询直清商户服务报件
     * @param zqServiceInfo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectZqServiceInfoPage")
    public Result selectZqServiceInfoPage(@RequestBody ZqServiceInfo zqServiceInfo,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<ZqServiceInfo> page = new Page<>(pageNo, pageSize);
            zqServiceInfoService.selectZqServiceInfoPage(page, zqServiceInfo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询直清商户服务报件异常", e);
        }
        return result;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/export")
    @SystemLog(operCode = "zqServiceInfo.export", description = "导出")
    public void export(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            ZqServiceInfo zqServiceInfo = JSONObject.parseObject(baseInfo, ZqServiceInfo.class);
            zqServiceInfoService.export(response, zqServiceInfo);
        } catch (Exception e){
            log.error("导出直清服务报件商户异常", e);
        }
    }

    /**
     * 直清商户报件
     * @param list
     * @return
     */
    @RequestMapping("/zqSyncSerBatch")
    @SystemLog(operCode = "zqServiceInfo.zqSyncSerBatch", description = "直清商户报件")
    public Result zqSyncSerBatch(@RequestBody List<ZqServiceInfo> list){
        Result result;
        try {
            result = zqServiceInfoService.zqSyncSerBatch(list);
        } catch (Exception e) {
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 查询直清商户服务报件
     * @param id
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectZqServiceInfo/{id}")
    public Result selectZqServiceInfoById(@PathVariable Long id){
        Result result = new Result();
        try {
            ZqServiceInfo zqServiceInfo = zqServiceInfoService.selectZqServiceInfoById(id);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(zqServiceInfo);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询直清商户服务报件异常", e);
        }
        return result;
    }

    @RequestMapping("/updateDealStatus")
    @ResponseBody
    @SystemLog(operCode = "zqServiceInfo.updateDealStatus", description = "修改业务处理状态")
    public Result updateDealStatus(@RequestBody ZqServiceInfo baseInfo){
        Result result = new Result();
        try {
            if(baseInfo == null || baseInfo.getId() == null
                    || StringUtils.isBlank(baseInfo.getDealStatus())){
                return Result.fail("参数不能为空");
            }
            result = zqServiceInfoService.updateDealStatus(baseInfo);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改业务处理状态异常", e);
        }
        return result;
    }

}
