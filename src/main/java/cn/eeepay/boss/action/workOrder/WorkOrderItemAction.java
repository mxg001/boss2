package cn.eeepay.boss.action.workOrder;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.workOrder.WorkOrderTransfer;
import cn.eeepay.framework.model.workOrder.WorkOrderItem;
import cn.eeepay.framework.model.workOrder.WorkRemarkRecord;
import cn.eeepay.framework.service.workOrder.WorkOrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 11:43
 */
@RestController
@RequestMapping("/workOrderItem")
public class WorkOrderItemAction {
    private Logger log = LoggerFactory.getLogger(WorkOrderItemAction.class);

    @Resource
    private WorkOrderItemService workOrderItemService;

    @RequestMapping("/reply")
    @SystemLog(operCode = "workOrder.reply",description = "回复工单")
    public Result reply(@RequestBody WorkOrderItem info){
        try {
            workOrderItemService.reply(info);
            return Result.success("回复成功");
        }catch (WorkOrderException we){
            log.error("回复失败 : ",we.getMessage());
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("回复失败",e);
            return Result.fail("回复失败");
        }
    }


    @RequestMapping("/reject")
    @SystemLog(operCode = "workOrder.reject",description = "驳回工单")
    public Result reject(@RequestBody WorkOrderItem info){
        try {
            workOrderItemService.reject(info);
            return Result.success("驳回成功");
        }catch (WorkOrderException we){
            log.error("驳回失败 : ",we.getMessage());
            return Result.fail(we.getMessage());
        }catch (Exception e){
            log.error("驳回失败",e);
            return Result.fail("驳回失败");
        }
    }

    @RequestMapping("/transfer")
    @SystemLog(operCode = "workOrder.transfer",description = "转单")
    public Result transfer(@RequestBody WorkOrderTransfer ot){
        try {
            return Result.success("转单成功",workOrderItemService.transfer(ot.getOrderNoArr(),ot.getReceiverId()));
        }catch (Exception e){
            log.error("转单失败",e);
            return Result.fail("转单失败");
        }
    }

    @RequestMapping("/remark")
    @SystemLog(operCode = "workOrder.remark",description = "备注工单")
    public Result remark(@RequestBody WorkRemarkRecord info){
        try {
            workOrderItemService.remark(info);
            return Result.success("备注成功");
        }catch (Exception e){
            log.error("备注失败",e);
            return Result.fail("备注失败");
        }
    }

}
