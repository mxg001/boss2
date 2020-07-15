package cn.eeepay.boss.action.workOrder;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.workOrder.WorkOrder;
import cn.eeepay.framework.service.workOrder.WorkOrderService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 11:43
 */
@RestController
@RequestMapping("/workOrder")
public class WorkOrderAction {
    private Logger log = LoggerFactory.getLogger(WorkOrderAction.class);

    @Resource
    private WorkOrderService workOrderService;

    @RequestMapping("/add")
    @SystemLog(operCode = "workOrder.add",description = "新增工单")
    public Result add(@RequestBody WorkOrder info){
        try {
            workOrderService.insert(info);
            return Result.success("新增成功");
        }catch (Exception e){
            log.error("新增失败",e);
            return Result.fail("新增失败");
        }
    }


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getWorkOrderById")
    public Result getWorkOrderById( @RequestParam("id") Long id){
        try {
            return Result.success("查询工单成功",workOrderService.getWorkOrderById(id));
        }catch (WorkOrderException we){
            log.error("查询工单失败 : ",we.getMessage());
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("查询工单失败",e);
            return Result.fail("查询工单失败");
        }
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getWorkOrderDetailById")
    public Result getWorkOrderDetailById( @RequestParam("id") Long id){
        try {
            return Result.success("查询工单详情成功",workOrderService.getWorkOrderDetailById(id));
        }catch (WorkOrderException we){
            log.error("查询工单详情失败 : ",we.getMessage());
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("查询工单详情失败",e);
            return Result.fail("查询工单详情失败");
        }
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getWorkOrderToRejectById")
    public Result getWorkOrderToRejectById( @RequestParam("id") Long id){
        try {
            return Result.success("查询工单成功",workOrderService.getWorkOrderToRejectById(id));
        }catch (WorkOrderException we){
            log.error("查询工单详情失败 : ",we.getMessage());
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("查询工单失败",e);
            return Result.fail("查询工单失败");
        }
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getWorkOrderToRemarkById")
    public Result getWorkOrderToRemarkById( @RequestParam("id") Long id){
        try {
            return Result.success("查询成功",workOrderService.showAgentShow(id));
        }catch (WorkOrderException we){
            log.error("查询失败: ",we.getMessage());
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("查询失败",e);
            return Result.fail("查询失败");
        }
    }

    @RequestMapping("/del")
    @SystemLog(operCode = "workOrder.del",description = "删除工单")
    public Result del(@RequestParam("id") Long id){
        try {
            workOrderService.del(id);
            return Result.success("删除工单成功");
        }catch (Exception e){
            log.error("删除工单失败",e);
            return Result.fail("删除工单员失败");
        }
    }


    @RequestMapping("/close")
    @SystemLog(operCode = "workOrder.close",description = "关闭工单")
    public Result close(@RequestParam("id") Long id){
        try {
            workOrderService.close(id);
            return Result.success("关闭工单成功");
        }catch (Exception e){
            log.error("关闭工单失败",e);
            return Result.fail("关闭工单失败");
        }
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/query")
    public Result query(@Param("page") Page<WorkOrder> page,@RequestParam("info") String info){
        try {
            workOrderService.query(page,JSON.parseObject(info, WorkOrder.class));
            return Result.success("查询工单成功",page);
        }catch (WorkOrderException we){
            log.error("查询工单失败",we);
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("查询工单失败",e);
            return Result.fail("查询工单失败");
        }
    }


    @RequestMapping("/getCurrentDeptUser")
    public Result getCurrentDeptUser(){
        try {
            return Result.success("获取当前部门用户成功",workOrderService.getCurrentDeptUser());
        }catch (Exception e){
            log.error("获取当前部门用户失败",e);
            return Result.fail("获取当前部门用户失败");
        }
    }

    @RequestMapping("/getCurrentWorkUser")
    public Result getCurrentWorkUser(){
        try {
            return Result.success("获取当前用户信息成功",workOrderService.getCurrentWorkUser());
        }catch (Exception e){
            log.error("获取当前用户信息失败",e);
            return Result.fail("获取当前用户信息失败");
        }
    }

    @RequestMapping("/getToDo")
    public Result getToDo(){
        try {
            return Result.success("获取待办事项成功",workOrderService.getToDo());
        }catch (WorkOrderException we){
            log.error("获取待办事项失败",we);
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("获取待办事项失败",e);
            return Result.fail("获取待办事项失败");
        }
    }

    @RequestMapping("/export")
    @SystemLog(operCode = "workOrder.export",description = "导出工单")
    public Result export(@RequestParam("info") String info,HttpServletResponse response, HttpServletRequest request){
        try {
            WorkOrder order = JSON.parseObject(info, WorkOrder.class);
            workOrderService.export(order,response,request);

            return Result.success("导出工单成功");
        }catch (Exception e){
            log.error("导出工单失败",e);
            return Result.fail("导出工单失败");
        }
    }






}
