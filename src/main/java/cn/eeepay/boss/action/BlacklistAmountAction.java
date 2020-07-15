package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.BlacklistAmount;
import cn.eeepay.framework.service.BlacklistAmountService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tans
 * @date 2019-08-09
 */
@RestController
@RequestMapping("/blacklistAmount")
public class BlacklistAmountAction {

    private static final Logger log = LoggerFactory.getLogger(BlacklistAmountAction.class);

    @Resource
    private BlacklistAmountService blacklistAmountService;

    /**
     * 条件查询金额黑名单
     * @param baseInfo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestBody BlacklistAmount baseInfo,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<BlacklistAmount> page = new Page<>(pageNo, pageSize);
            blacklistAmountService.selectPage(page, baseInfo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询金额黑名单异常", e);
        }
        return result;
    }

    /**
     * 新增金额黑名单
     * @param baseInfo
     * @return
     */
    @RequestMapping("/insert")
    @SystemLog(operCode = "blacklistAmount.insert", description = "新增")
    public Result insert(@RequestBody BlacklistAmount baseInfo){
        Result result = new Result();
        if(baseInfo == null || baseInfo.getJumpRuleId() == null ||
                baseInfo.getAmount() == null) {
            return Result.fail("参数不能为空");
        }
        try {
            int num = blacklistAmountService.insert(baseInfo);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("新增金额黑名单异常", e);
        }
        return result;
    }

    /**
     * 修改金额黑名单
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @SystemLog(operCode = "blacklistAmount.delete", description = "删除")
    public Result delete(Integer id){
        Result result = new Result();
        if(id == null) {
            return Result.fail("参数不能为空");
        }
        try {
            int num = blacklistAmountService.delete(id);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("删除保存金额黑名单异常", e);
        }
        return result;
    }

}
