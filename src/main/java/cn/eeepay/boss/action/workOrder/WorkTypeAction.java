package cn.eeepay.boss.action.workOrder;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.workOrder.WorkType;
import cn.eeepay.framework.service.workOrder.WorkTypeService;
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
@RequestMapping("/workType")
public class WorkTypeAction {
    private Logger log = LoggerFactory.getLogger(WorkTypeAction.class);

    @Resource
    private WorkTypeService workTypeService;


    @RequestMapping("/add")
    @SystemLog(operCode = "workType.add",description = "新增工单类型")
    public Result add(@RequestBody WorkType info){
        try {
            workTypeService.insert(info);
            return Result.success("新增成功");
        }catch (WorkOrderException we){
            log.error("新增工单类型失败",we);
            return Result.fail("新增工单类型失败 : "+we.getMessage());
        }catch (Exception e){
            log.error("新增工单类型失败",e);
            return Result.fail("新增工单类型失败");
        }

    }

    @RequestMapping("/edit")
    @SystemLog(operCode = "workType.edit",description = "修改工单类型")
    public Result edit(@RequestBody WorkType info){
        try {
            workTypeService.update(info);
            return Result.success("修改成功");
        }catch (Exception e){
            log.error("修改工单类型失败",e);
            return Result.fail("修改工单类型失败");
        }
    }



    @RequestMapping("/getWorkTypeById")
    public Result getWorkTypeById(@RequestParam("id") Long id){
        try {
            WorkType workType = workTypeService.getWorkTypeById(id);
            return Result.success("获取工单类型成功",workType);
        }catch (Exception e){
            log.error("获取工单类型失败",e);
            return Result.fail("获取工单类型失败");
        }
    }




    @RequestMapping("/del")
    @SystemLog(operCode = "workType.del",description = "删除工单类型")
    public Result del(@RequestParam("id") Long id){
        try {
            workTypeService.del(id);
            return Result.success("删除工单类型成功");
        }catch (Exception e){
            log.error("删除工单类型失败",e);
            return Result.fail("删除工单类型失败");
        }

    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/query")
    public Result query(@Param("page") Page<WorkType> page, @RequestBody WorkType info){
        try {
            workTypeService.query(page,info);
            return Result.success("查询工单类型",page);
        }catch (WorkOrderException we){
            log.error("查询工单类型失败: ",we);
            return Result.fail("查询工单类型失败: "+we.getMessage());
        }catch (Exception e){
            log.error("查询工单类型失败: ",e);
            return Result.fail("查询工单类型失败");
        }
    }


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getAll")
    public Result getAll(){
        try {
            return Result.success("查询所有工单类型成功",workTypeService.getAll());
        }catch (Exception e){
            log.error("查询所有工单类型失败",e);
            return Result.fail("查询所有工单类型失败");
        }
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getMyDutyType")
    public Result getMyDutyType(){
        try {
            return Result.success("获取本人负责的工单类型成功",workTypeService.getMyDutyType());
        }catch (Exception e){
            log.error("获取本人负责的工单类型失败",e);
            return Result.fail("获取本人负责的工单类型失败");
        }
    }


}
