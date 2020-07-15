package cn.eeepay.boss.action.workOrder;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.workOrder.WorkOrderUser;
import cn.eeepay.framework.service.workOrder.WorkOrderUserService;
import cn.eeepay.framework.util.Constants;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 11:43
 */
@RestController
@RequestMapping("/workOrderUser")
public class WorkOrderUserAction {
    private Logger log = LoggerFactory.getLogger(WorkOrderUserAction.class);

    @Resource
    private WorkOrderUserService workOrderUserService;


    @RequestMapping("/add")
    @SystemLog(operCode = "workOrderUser.add",description = "新增工单处理人员")
    public Result add(@RequestBody WorkOrderUser info){
        try {
            workOrderUserService.insert(info);
            return Result.success("新增成功");
        }catch (WorkOrderException we){
            log.error("新增工单处理人员失败 : "+we.getMessage());
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("新增工单处理人员失败",e);
            return Result.fail("新增工单处理人员失败");
        }

    }

    @RequestMapping("/edit")
    @SystemLog(operCode = "workOrderUser.edit",description = "修改工单处理人员")
    public Result edit(@RequestBody WorkOrderUser info){
        try {
            workOrderUserService.update(info);
            return Result.success("修改成功");
        }catch (Exception e){
            log.error("修改工单处理人员失败",e);
            return Result.fail("修改工单处理人员失败");
        }
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getWorkUserById")
    public Result getWorkUserById( @RequestParam("id") Long id){
        try {
            return Result.success("查询负责类型成功",workOrderUserService.getWorkUserById(id));
        }catch (Exception e){
            log.error("查询负责类型失败",e);
            return Result.fail("查询负责类型失败");
        }
    }

    @RequestMapping("/updateStatus")
    @SystemLog(operCode = "workOrderUser.updateStatus",description = "修改工单处理人员状态")
    public Result updateStatus(@RequestBody WorkOrderUser info){
        try {
            workOrderUserService.updateStatus(info);
            return Result.success("修改工单处理人员状态成功");
        }catch (Exception e){
            log.error("修改工单处理人员状态失败",e);
            return Result.fail("修改工单处理人员状态失败");
        }

    }

    @RequestMapping("/del")
    @SystemLog(operCode = "workOrderUser.del",description = "删除工单处理人员")
    public Result del(@RequestParam("id") Long id){
        try {
            workOrderUserService.del(id);
            return Result.success("删除工单处理人员成功");
        }catch (Exception e){
            log.error("删除工单处理人员失败",e);
            return Result.fail("删除工单处理人员失败");
        }

    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/query")
    public Page<WorkOrderUser> query(@Param("page") Page<WorkOrderUser> page, @RequestBody WorkOrderUser info){
        try {
            workOrderUserService.query(page,info);
        }catch (Exception e){
            log.error("查询工单处理人员失败",e);
        }
        return page;
    }
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getDutyTypeDate")
    public Result getDutyTypeDate( @RequestParam("dutyType") Integer dutyType){
        try {
            return Result.success("查询负责类型成功",workOrderUserService.getDutyData(dutyType));
        }catch (Exception e){
            log.error("查询负责类型失败",e);
            return Result.fail("查询负责类型失败");
        }
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getAllUsers")
    public Result getAllUsers(){
        try {
            return Result.success("查询所有boss账号成功",workOrderUserService.getAllUsers());
        }catch (Exception e){
            log.error("查询所有boss账号失败",e);
            return Result.fail("查询所有boss账号失败");
        }
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getCurrentDeptUser")
    public Result getCurrentDeptUser(){
        try {
            return Result.success("查询所有boss账号成功",workOrderUserService.getCurrentDeptUser());
        }catch (Exception e){
            log.error("查询所有boss账号失败",e);
            return Result.fail("查询所有boss账号失败");
        }
    }


}
