package cn.eeepay.boss.action.cjt;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.cjt.CjtGoods;
import cn.eeepay.framework.service.cjt.CjtGoodsService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tans
 * @date 2019-05-28
 */
@RestController
@RequestMapping("/cjtGoods")
public class CjtGoodsAction {

    private static final Logger log = LoggerFactory.getLogger(CjtGoodsAction.class);

    @Resource
    private CjtGoodsService cjtGoodsService;

    /**
     * 条件查询超级推商品
     * @param cjtGoods
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectCjtGoodsPage")
    public Result selectCjtGoodsPage(@RequestBody CjtGoods cjtGoods,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<CjtGoods> page = new Page<>(pageNo, pageSize);
            cjtGoodsService.selectCjtGoodsPage(page, cjtGoods);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询超级推商品异常", e);
        }
        return result;
    }

    /**
     * 修改上下架状态
     * @param cjtGoods
     * @return
     */
    @RequestMapping("/updateStatus")
    @SystemLog(operCode = "cjtGoods.updateStatus", description = "修改上下架状态")
    public Result updateStatus(@RequestBody CjtGoods cjtGoods){
        Result result = new Result();
        try {
            int num = cjtGoodsService.updateStatus(cjtGoods);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改上下架状态异常", e);
        }
        return result;
    }

    /**
     * 修改白名单状态
     * @param cjtGoods
     * @return
     */
    @RequestMapping("/updateWhiteStatus")
    @SystemLog(operCode = "cjtGoods.updateWhiteStatus", description = "修改白名单状态")
    public Result updateWhiteStatus(@RequestBody CjtGoods cjtGoods){
        Result result = new Result();
        try {
            int num = cjtGoodsService.updateWhiteStatus(cjtGoods);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改白名单状态异常", e);
        }
        return result;
    }

    /**
     * 查询超级推商品
     * @param goodsCode
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectCjtGoods/{goodsCode}")
    public Result selectCjtGoodsDetail(@PathVariable String goodsCode){
        Result result = new Result();
        try {
            CjtGoods cjtGoods = cjtGoodsService.selectCjtGoodsDetail(goodsCode);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(cjtGoods);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询超级推商品异常", e);
        }
        return result;
    }

    /**
     * 新增超级推商品
     * @param cjtGoods
     * @return
     */
    @RequestMapping("/addCjtGoods")
    @SystemLog(operCode = "cjtGoods.addCjtGoods", description = "新增")
    public Result addCjtGoods(@RequestBody CjtGoods cjtGoods){
        Result result = new Result();
        try {
            int num = cjtGoodsService.insertCjtGoods(cjtGoods);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("新增超级推商品异常", e);
        }
        return result;
    }

    /**
     * 修改超级推商品
     * @param cjtGoods
     * @return
     */
    @RequestMapping("/updateCjtGoods")
    @SystemLog(operCode = "cjtGoods.updateCjtGoods", description = "修改")
    public Result updateCjtGoods(@RequestBody CjtGoods cjtGoods){
        Result result = new Result();
        try {
            int num = cjtGoodsService.updateCjtGoods(cjtGoods);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改保存超级推商品异常", e);
        }
        return result;
    }

    /**
     * 查询超级推硬件产品集合
     * @return
     */
    @RequestMapping("/selectCjtHpList")
    public Result selectCjtHpList(){
        Result result = new Result();
        try {
            List<HardwareProduct> list = cjtGoodsService.selectCjtHpList();
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询超级推硬件产品集合异常", e);
        }
        return result;
    }

}
