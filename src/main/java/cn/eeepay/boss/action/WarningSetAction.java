package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.impl.TimingProduceServiceImpl;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.dubbo.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.util.Constants;

/**
 * Created by Administrator on 2018/1/8/008.
 * @author liuks
 * 预警人列表
 */
@Controller
@RequestMapping(value = "/warningSet")
public class WarningSetAction {

    private static final Logger log = LoggerFactory.getLogger(WarningSetAction.class);

    @Resource
    private WarningPeopleService warningPeopleService;
    @Autowired
	private WarningSetService warningSetService;

    @Resource
    private UserService userService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private AcqServiceProService acqServiceProService;

    //增加根据服务ID设置不同预警阀值提高系统预警的灵敏度
    //出款服务ID/结算中预警周期/结算中异常笔数/结算失败预警周期/结算失败异常笔数       修改完要丢弃之前的线程.重新开启
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/setJob")
    @ResponseBody
    public Map<String, Object> setJob(@ModelAttribute("page") Page<WarningPeople> page,@RequestParam("info") String param) throws Exception {
        //出款预警人员 2   前后台通了.现在写业务代码了
    	
    	// 获取前端的传入值,执行设置
    	
       // Map<String, Object> jsonMap=getWarningPeople(page,param,2);
        return null;
    }
    
    //不同的人设置不同的可管理任务  出款服务id------>不同的人

	@RequestMapping("/updateWaringInfo")
	@SystemLog(description = "修改收单服务预警阀值", operCode = "warningSet.updateWaringInfo")
	@ResponseBody
	public Result updateWaringInfo(String info) {
        Result result = new Result();
		try {
			WarningSet warningSet = JSONObject.parseObject(info, WarningSet.class);
            warningSet.setStatus(WarningSet.warningSetTypeIn);
			warningSetService.updateWarningSet(warningSet);
			result.setStatus(true);
			result.setMsg("操作成功");
		} catch (Exception e) {
			log.error("修改收单服务预警阀值异常", e);
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
            baseInfo.setStatus(WarningSet.warningSetTypeIn);
            warningSetService.selectPage(baseInfo, page);
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
    @SystemLog(description = "修改收单服务预警状态", operCode = "warningSet.updateWarnStatus")
    public Result updateWarnStatus(@RequestBody WarningSet baseInfo){
        Result result = new Result();
        try {
            warningSetService.updateWarnStatus(baseInfo);
            result.setStatus(true);
            result.setMsg("操作成功");
        } catch (Exception e){
            log.error("修改收单服务预警状态异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    @RequestMapping(value = "/deleteWarning")
    @ResponseBody
    @SystemLog(description = "删除收单服务预警", operCode = "warningSet.deleteWarning")
    public Result deleteWarning(@RequestBody WarningSet baseInfo){
        Result result = new Result();
        try {
            warningSetService.deleteWarning(baseInfo);
            result.setStatus(true);
            result.setMsg("操作成功");
        } catch (Exception e){
            log.error("删除收单服务预警异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 导出预警阀值
     * @param baseInfo
     * @return
     */
    @RequestMapping(value = "/exportInfo")
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @SystemLog(description = "导出预警阀值", operCode = "warningSet.exportInfo")
    public void exportInfo(@RequestParam String baseInfo, HttpServletResponse response){
        try {
            baseInfo = URL.decode(baseInfo);
            WarningSet warningSet = JSONObject.parseObject(baseInfo, WarningSet.class);
            warningSet.setStatus(WarningSet.warningSetTypeIn);
            warningSetService.exportInfo(response, warningSet);
        } catch (Exception e){
            log.error("导出预警阀值异常", e);
        }
        return;
    }

    /**
     * 查询交易预警周期
     * @return
     */
    @RequestMapping(value = "/getTransCycle")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result getTransCycle(){
        Result result = new Result();
        try {
            SysDict cycleDict =  sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_TRANSACTION);
            SysDict exceptionNumberDict =  sysDictService.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_TRANSACTION);
            Map<String, Object> map = new HashMap<>();
            if(cycleDict != null){
               map.put("cycle", cycleDict.getSysValue());
            } else {
                result.setMsg("数据字典：" + TimingProduceServiceImpl.TIMING_CYCLE_TRANSACTION +"为空");
                return result;
            }
            if(cycleDict != null){
                map.put("exceptionNumber", exceptionNumberDict.getSysValue());
            } else {
                result.setMsg("数据字典：" + TimingProduceServiceImpl.TIMING_PEN_NUMBER_TRANSACTION + "为空");
                return result;
            }
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("查询交易预警周期异常", e);
        }
        return result;
    }

    /**
     * 查询收单服务
     * @param serviceId
     * @return
     */
    @RequestMapping(value = "/getServiceInfo")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result getServiceInfo(Integer serviceId){
        Result result = new Result();
        try {
            AcqService acqService = acqServiceProService.findServiceId(serviceId);
            if(acqService != null){
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(acqService);
            } else {
                result.setMsg("找不到对应的收单服务");
            }
        } catch (Exception e){
            log.error("查询收单服务异常", e);
        }
        return result;
    }

}
