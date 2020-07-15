package cn.eeepay.boss.action.cjt;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.cjt.CjtWhiteMer;
import cn.eeepay.framework.service.cjt.CjtWhiteMerService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tans
 * @date 2019-05-29
 */
@RestController
@RequestMapping("/cjtWhiteMer")
public class CjtWhiteMerAction {

    private static final Logger log = LoggerFactory.getLogger(CjtWhiteMerAction.class);

    @Resource
    private CjtWhiteMerService cjtWhiteMerService;

    /**
     * 条件查询超级推商户白名单（已下架的商品，白名单用户仍能看到）
     * @param baseInfo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestBody CjtWhiteMer baseInfo,
                             @RequestParam(defaultValue = "1") int pageNo,
                             @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<CjtWhiteMer> page = new Page<>(pageNo, pageSize);
            cjtWhiteMerService.selectPage(page, baseInfo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询超级推商户白名单（已下架的商品，白名单用户仍能看到）异常", e);
        }
        return result;
    }

    /**
     * 新增超级推商户白名单
     * @param baseInfo
     * @return
     */
    @RequestMapping("/insert")
    @SystemLog(operCode = "cjtWhiteMer.insert", description = "新增")
    public Result insert(@RequestBody CjtWhiteMer baseInfo){
        Result result = new Result();
        try {
            if(StringUtils.isEmpty(baseInfo.getMerchantNo())) {
                return Result.fail("商户号不能为空");
            }
            int num = cjtWhiteMerService.insert(baseInfo);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("新增超级推商户白名单异常", e);
        }
        return result;
    }

    /**
     * 修改超级推商户白名单
     * @param baseInfo
     * @return
     */
    @RequestMapping("/update")
    @SystemLog(operCode = "cjtWhiteMer.update", description = "修改")
    public Result update(@RequestBody CjtWhiteMer baseInfo){
        Result result = new Result();
        try {
            if(StringUtils.isEmpty(baseInfo.getMerchantNo())) {
                return Result.fail("商户号不能为空");
            }
            if(baseInfo.getId() == null) {
                return Result.fail("参数非法");
            }
            int num = cjtWhiteMerService.update(baseInfo);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改保存超级推商户白名单异常", e);
        }
        return result;
    }

    /**
     * updateStatus超级推商户白名单
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @SystemLog(operCode = "cjtWhiteMer.delete", description = "删除")
    public Result delete(Integer id){
        Result result = new Result();
        try {
            int num = cjtWhiteMerService.delete(id);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("删除超级推商户白名单异常", e);
        }
        return result;
    }

    /**
     * 修改超级推商户白名单状态
     * @param baseInfo
     * @return
     */
    @RequestMapping("/updateStatus")
    @SystemLog(operCode = "cjtWhiteMer.updateStatus", description = "修改状态")
    public Result updateStatus(@RequestBody CjtWhiteMer baseInfo){
        Result result = new Result();
        try {
            int num = cjtWhiteMerService.updateStatus(baseInfo);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改超级推商户白名单状态异常", e);
        }
        return result;
    }
}
