package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.impl.TimingProduceServiceImpl;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.dubbo.common.URL;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 预警人列表
 * 出款服务的
 */
@Controller
@RequestMapping(value = "/warningSetSettle")
public class WarningSetSettleAction {

    private static final Logger log = LoggerFactory.getLogger(WarningSetSettleAction.class);

    @Autowired
	private WarningSetService warningSetService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private OutAccountServiceService outAccountServiceService;

    
    //不同的人设置不同的可管理任务  出款服务id------>不同的人

	@RequestMapping("/updateWaringInfo")
	@SystemLog(description = "修改出款服务预警阀值", operCode = "warningSetSettle.updateWaringInfo")
	@ResponseBody
	public Result updateWaringInfo(String info) {
        Result result = new Result();
		try {
			WarningSet warningSet = JSONObject.parseObject(info, WarningSet.class);
            warningSet.setStatus(WarningSet.warningSetTypeOut);
			warningSetService.updateWarningSet(warningSet);
			result.setStatus(true);
			result.setMsg("操作成功");
		} catch (Exception e) {
			log.error("修改出款服务预警阀值异常", e);
            result = ResponseUtil.buildResult(e);
		}
		return result;
	}

	/**
	 * 条件分页查询预警阀值
	 * @param baseInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/selectPage")
	@ResponseBody
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	public Result selectPage(@RequestBody WarningSet baseInfo,
							 @RequestParam(defaultValue = "1") int pageNo,
							 @RequestParam(defaultValue = "10") int pageSize){
		Result result = new Result();
		try {
		    Page<WarningSet> page = new Page<>(pageNo, pageSize);
            baseInfo.setStatus(WarningSet.warningSetTypeOut);
            warningSetService.selectSettlePage(baseInfo, page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
		} catch (Exception e){
			log.error("条件查询预警阀值异常", e);
		}
		return result;
	}

    @RequestMapping(value = "/updateWarnStatus")
    @ResponseBody
    @SystemLog(description = "修改出款服务预警状态", operCode = "warningSetSettle.updateWarnStatus")
    public Result updateWarnStatus(@RequestBody WarningSet baseInfo){
        Result result = new Result();
        try {
            warningSetService.updateWarnStatus(baseInfo);
            result.setStatus(true);
            result.setMsg("操作成功");
        } catch (Exception e){
            log.error("修改出款服务预警状态异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    @RequestMapping(value = "/deleteWarning")
    @ResponseBody
    @SystemLog(description = "删除出款服务预警", operCode = "warningSetSettle.deleteWarning")
    public Result deleteWarning(@RequestBody WarningSet baseInfo){
        Result result = new Result();
        try {
            warningSetService.deleteWarning(baseInfo);
            result.setStatus(true);
            result.setMsg("操作成功");
        } catch (Exception e){
            log.error("删除出款服务预警异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 导出出款服务预警阀值
     * @param baseInfo
     * @return
     */
    @RequestMapping(value = "/exportInfo")
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @SystemLog(description = "导出出款服务预警阀值", operCode = "warningSetSettle.exportInfo")
    public void exportInfo(@RequestParam String baseInfo, HttpServletResponse response){
        try {
            baseInfo = URL.decode(baseInfo);
            WarningSet warningSet = JSONObject.parseObject(baseInfo, WarningSet.class);
            warningSet.setStatus(WarningSet.warningSetTypeOut);
            warningSetService.exportSettleInfo(response, warningSet);
        } catch (Exception e){
            log.error("导出出款服务预警阀值异常", e);
        }
        return;
    }

    /**
     * 查询出款结算中、失败预警周期
     * @return
     */
    @RequestMapping(value = "/getTransCycle")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result getTransCycle(){
        Result result = new Result();
        try {
            SysDict cycleDict =  sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENTING);
            SysDict excetionNumberDict =  sysDictService.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENTING);
            SysDict failurCycleDict =  sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
            SysDict failueExcetionNumberDict =  sysDictService.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
            Map<String, Object> map = new HashMap<>();
            if(cycleDict != null){
                map.put("cycle", cycleDict.getSysValue());
            } else {
                result.setMsg("数据字典：" + TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENTING + "为空");
                return result;
            }
            if(excetionNumberDict != null){
                map.put("exceptionNumber", excetionNumberDict.getSysValue());
            } else {
                result.setMsg("数据字典：" + TimingProduceServiceImpl.TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENTING +"为空");
                return result;
            }
            if(failurCycleDict != null){
                map.put("failurCycle", failurCycleDict.getSysValue());
            } else {
                result.setMsg("数据字典：" + TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENT_FAILURE + "为空");
                return result;
            }
            if(failueExcetionNumberDict != null){
                map.put("failurExceptionNumber", failueExcetionNumberDict.getSysValue());
            } else {
                result.setMsg("数据字典：" + TimingProduceServiceImpl.TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENT_FAILURE + "为空");
                return result;
            }
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("查询出款预警周期异常", e);
        }
        return result;
    }

    /**
     * 查询出款服务
     * @param serviceId
     * @return
     */
    @RequestMapping(value = "/getServiceInfo")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result getServiceInfo(Integer serviceId){
        Result result = new Result();
        try {
            OutAccountService outAccountService = outAccountServiceService.findServiceId(serviceId);
            if(outAccountService != null){
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(outAccountService);
            } else {
                result.setMsg("找不到对应的出款服务");
            }
        } catch (Exception e){
            log.error("查询出款服务异常", e);
        }
        return result;
    }

}
