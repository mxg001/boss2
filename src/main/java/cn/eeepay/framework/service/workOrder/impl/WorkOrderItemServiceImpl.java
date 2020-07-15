package cn.eeepay.framework.service.workOrder.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.UserDao;
import cn.eeepay.framework.dao.workOrder.*;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SysCalendar;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.workOrder.*;
import cn.eeepay.framework.service.SysCalendarService;
import cn.eeepay.framework.service.workOrder.*;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.HolidayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/29 14:51
 */
@Transactional
@Service
public class WorkOrderItemServiceImpl implements WorkOrderItemService {

    @Resource
    private WorkOrderItemDao workOrderItemDao;

    @Resource
    private WorkFileInfoService workFileInfoService;

    @Resource
    private UserDao userDao;

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private WorkFlowNodeDao workFlowNodeDao;

    @Resource
    private WorkOrderUserService workOrderUserService;

    @Resource
    private AgentInfoDao agentInfoDao;

    @Resource
    private WorkRemarkRecordService workRemarkRecordService;

    @Resource
    private WorkFlowNodeService workFlowNodeService;

    @Resource
    private SysCalendarService sysCalendarService;

    @Resource
    private WorkOrderDao workOrderDao;



    @Override
    public void reply(WorkOrderItem item) {

        WorkOrder order = workOrderService.getWorkOrderByOrderNo(item.getOrderNo());
        if(order==null){
            throw new WorkOrderException("该工单不存在");
        }

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        WorkFlowNode node = workFlowNodeService.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());

        if(!workOrderService.replyStatusCheck(order,principal,workUser,node)){
            throw new WorkOrderException("你无权回复该工单！");
        }

        item.setSenderId(Long.valueOf(principal.getId()));
        item.setSenderDeptNo(Long.valueOf(userDao.getUserInfoById(principal.getId()).getDeptId()));
        item.setRejectStatus(0);
        item.setTransferStatus(0);
        item.setFlowNo(order.getCurrentFlowNo());
        workOrderItemDao.insert(item);
        if(item.getId()!=null){
            workFileInfoService.insertFiles(3,item.getId(),item.getWorkFileInfos());
        }

        //更新节点表 状态以及当前用户信息
        WorkFlowNode currNode = workFlowNodeDao.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());
        //如果是驳回 管理员处理了这份工单 工单的处理人还是属于第一次回复的人 节点表当前处理人不为空
        if(currNode.getCurrentUserId()==null){
            currNode.setCurrentUserId(item.getSenderId());
        }
        currNode.setCurrentStatus(2);
        int reply = workFlowNodeDao.reply(currNode);
        if(reply<=0){
            throw new WorkOrderException("回复失败！");
        }

        //如果下一个节点为空 流程个自动结束
        WorkFlowNode nextNode = workFlowNodeDao.getNodeByParentFlowNo(order.getCurrentFlowNo(), order.getOrderNo());
        if(nextNode==null){
            order.setStatus(2);//已处理
        }else{
            order.setCurrentDeptNo(nextNode.getDeptNo().intValue());
            order.setCurrentFlowNo(nextNode.getFlowNo());
            //更新下一个节点状态为待处理
            workFlowNodeDao.updateStatus(nextNode.getFlowNo(),order.getOrderNo(),0);

            //如果下一个节点为代理商 设置截止回复日期  截止日期优先级：工单上的截止日期 > 工单类型截止日期 并且跳过节假日
            if(nextNode.getDeptNo().intValue()==999){
                //判断nextNode是否为流程的最后一个节点  如果是 工单状态变为已处理
                WorkFlowNode lastNode = workFlowNodeDao.getNodeByParentFlowNo(nextNode.getFlowNo(), order.getOrderNo());
                if(lastNode==null){
                    order.setStatus(2);
                }else{
                    order.setAgentReplyStatus(0);
                }

                Integer endReplyDays = order.getEndReplyDays();
                if(endReplyDays==null){
                    endReplyDays = nextNode.getEndReplyDays();
                }

                if(endReplyDays!=null){
                    List<SysCalendar> holidays = sysCalendarService.getHolidayByYear(Calendar.getInstance().get(Calendar.YEAR));
                    List<String> holidayStr = new ArrayList<>();
                    for (SysCalendar holiday : holidays) {
                        holidayStr.add(DateUtil.getFormatDate("yyyy-MM-dd",holiday.getSysDate()));
                    }
                    Date endReplyTime = null;
                    try {
                        endReplyTime = HolidayUtils.getScheduleActiveDate(new Date(),holidayStr,endReplyDays);
                        endReplyTime = DateUtil.parseLongDateTime(DateUtil.getFormatDate("yyyy-MM-dd",endReplyTime)+" 23:59:59");

                        order.setEndReplyTime(endReplyTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        workOrderService.update(order);
    }

    /**
     * 驳回
     * @param newInfo
     */
    @Override
    public void reject(WorkOrderItem newInfo) {
        WorkOrder order = workOrderService.getWorkOrderByOrderNo(newInfo.getOrderNo());
        if(order==null){
            throw new WorkOrderException("该工单不存在");
        }

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        WorkFlowNode node = workFlowNodeService.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());

        if(!workOrderService.rejectStatusCheck(order,principal,workUser,node)){
            throw new WorkOrderException("你无权驳回该工单！");
        }

        //获取当前节点
        WorkFlowNode currNode = workFlowNodeDao.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());
        //只能驳回之前步骤的处理人  或者驳回给发起人    A-B-C-D-E 如：B节点的处理人是b,D节点驳回给B时 接收人必须为b
        Long receiverId = null;
        Long receiverDeptNo = null;

        //获取驳回步骤 当时的处理人
        WorkOrderItem oldItem = workOrderItemDao.getItemByOrderNoAndFlowNo(newInfo.getOrderNo(),newInfo.getReceiverFlowNo());
        receiverId = oldItem.getSenderId();
        receiverDeptNo = oldItem.getSenderDeptNo();


        newInfo.setRejectStatus(1);
        newInfo.setReceiverId(receiverId);
        newInfo.setReceiverDeptNo(receiverDeptNo);
        newInfo.setSenderId(Long.valueOf(principal.getId()));
        newInfo.setSenderDeptNo(Long.valueOf(principal.getDeptId()));
        newInfo.setTransferStatus(0);
        newInfo.setFlowNo(currNode.getFlowNo());
        workOrderItemDao.insert(newInfo);

        //更新当前节点信息
        currNode.setCurrentStatus(3);
        currNode.setCurrentUserId(Long.valueOf(principal.getId()));

        int reply = workFlowNodeDao.reply(currNode);
        if(reply<=0){
            throw new WorkOrderException("驳回失败！");
        }

        if(newInfo.getId()!=null){
            //更新驳回节点信息
            WorkFlowNode nextNode = workFlowNodeDao.getNodeByFlowNo(newInfo.getReceiverFlowNo(), order.getOrderNo());

            if(nextNode!=null){
                if(nextNode.getDeptNo().intValue()==999){
                    order.setAgentReplyStatus(0);
                }
                order.setCurrentDeptNo(nextNode.getDeptNo().intValue());
                order.setCurrentFlowNo(nextNode.getFlowNo());
                nextNode.setCurrentStatus(0);
                workFlowNodeDao.update(nextNode);
            }

        }
        workOrderService.update(order);
    }


    @Override
    public Map<String,Object> transfer(String[] orderNoArr,Integer receiverId) {

        if(orderNoArr==null || orderNoArr.length<=0){
            throw new WorkOrderException("参数不合法");
        }

        UserInfo receiver = userDao.getUserInfoById(receiverId);
        if(receiver==null){
            throw new WorkOrderException("参数不合法");
        }
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(receiver.getUserName());
        if(workUser==null){
            throw new WorkOrderException("该用户尚未配置工单类型");
        }

        //接收人未负责该类型工单
        List<HashMap<String,String>> orderList = new ArrayList<>();
        int failResult = 0;
        int successResult = 0;
        for (String orderNo : orderNoArr) {
            WorkOrder order = workOrderService.getWorkOrderByOrderNo(orderNo);
            HashMap<String,String> map = new HashMap<>();
            map.put("id",order.getId()+"");
            map.put("workTypeName",order.getWorkTypeName());

            if(order.getStatus()!=1){
                map.put("reason","待处理工单才能进行转单");
                orderList.add(map);
                failResult++;
                continue;
            }
            if(workUser.getDutyType()!=null){
                if(workUser.getDutyType()==2){//工单类型
                    String[] dataArr = workUser.getDutyData().split(",");
                    Set<String> dataSet = new HashSet<>(Arrays.asList(dataArr));
                    if(dataSet.add(order.getWorkTypeId()+"")){
                        map.put("reason","接收人未负责该类型工单");
                        orderList.add(map);
                        failResult++;
                        continue;
                    }
                }else if(workUser.getDutyType()==1){//销售
                    List<String> agentNodes = workOrderUserService.getdutyAgent(workUser);
                    Set<String> dataSet = new HashSet<>(agentNodes);
                    if(dataSet.add(order.getReceiveAgentNode())){
                        map.put("reason","接收人未负责该代理商的工单");
                        orderList.add(map);
                        failResult++;
                        continue;
                    }
                }
            }
            //获取当前节点
            WorkFlowNode node = workFlowNodeDao.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            WorkOrderUser currentWorkUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
            if(node!=null){
                //管理员之外  转单之后就是另外一个人的了，如果还需要再次转单就是另外一个人再转给别人
                if(!"2".equals(currentWorkUser.getRoleType())){
                    if(node.getCurrentUserId()!=null && node.getCurrentUserId().intValue()!=principal.getId().intValue()){
                        map.put("reason","您不是该工单的处理人，无法转单");
                        orderList.add(map);
                        failResult++;
                        continue;
                    }
                }
                if(node.getDeptNo().intValue()!=receiver.getDeptId().intValue()){
                    map.put("reason","非所在部门待回复工单");
                    orderList.add(map);
                    failResult++;
                    continue;
                }
                if(node.getCurrentStatus()!=null && node.getCurrentStatus().intValue()!=0){
                    map.put("reason","当前部门处理状态不是待处理");
                    orderList.add(map);
                    failResult++;
                    continue;
                }
            }


            //开始转单
            WorkOrderItem transferItem = new WorkOrderItem();
            transferItem.setOrderNo(orderNo);
            transferItem.setReceiverId(Long.valueOf(receiverId));
            transferItem.setReceiverDeptNo(Long.valueOf(receiver.getDeptId()));
            transferItem.setFlowNo(order.getCurrentFlowNo());
            transferItem.setRejectStatus(0);
            transferItem.setTransferStatus(1);
            transferItem.setSenderId(Long.valueOf(receiver.getId()));
            transferItem.setSenderDeptNo(Long.valueOf(receiver.getDeptId()));
            transferItem.setReceiverFlowNo(order.getCurrentFlowNo());

            //更新当前节点的处理人为接收人
            node.setCurrentUserId(Long.valueOf(receiverId));
            workFlowNodeService.update(node);

            workOrderItemDao.insert(transferItem);
        }

        successResult = orderNoArr.length-failResult;
        Map<String,Object> result= new HashMap<>();
        result.put("orders",orderList);
        result.put("success",successResult);
        result.put("fail",failResult);
        return result;
    }


    @Override
    public void remark(WorkRemarkRecord info) {
        WorkOrder order = workOrderDao.getWorkOrderById(info.getOrderId());
        if(order==null){
            throw new WorkOrderException("该工单不存在");
        }

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());

        WorkFlowNode node = workFlowNodeService.getNodeByOrderNoAndDeptNo(order.getOrderNo(), principal.getDeptId());
        if(!workOrderService.remarkStatusCheck(order,principal,workUser,node)){
            throw new WorkOrderException("你无权备注该工单！");
        }

        info.setOperator(principal.getUsername());
        if(order.getCreateUserId().intValue()==principal.getId().intValue()){//操作人是发起人
            info.setBelongType(1);
            info.setBelongId(order.getId());
        }else{
            //获取当前部门步骤item
            WorkOrderItem item = workOrderItemDao.getLastestItemByOrderNo(order.getOrderNo(),Long.valueOf(principal.getDeptId()));
            if(item!=null){
                info.setBelongType(2);
                info.setBelongId(item.getId());
            }
        }

        workRemarkRecordService.insert(info);
        if(info.getId()!=null){
            workFileInfoService.insertFiles(info.getBelongType()==2?2:4,info.getId(),info.getWorkFileInfos());
        }
    }

    @Override
    public List<WorkOrderItem> getItemsToExportByOrderNo(WorkOrder order) {
        List<WorkFlowNode> nodes = workFlowNodeService.getNodesByOrderNo(order.getOrderNo());
        if(nodes.size()>6){
            nodes = nodes.subList(nodes.size()-6,nodes.size());
        }

        List<WorkOrderItem> items = new ArrayList<>();
        if(nodes!=null && nodes.size()>0){
            for (WorkFlowNode node : nodes) {
                WorkOrderItem item = workOrderItemDao.getItemByOrderNoAndFlowNo(order.getOrderNo(), node.getFlowNo());
                if(item!=null){
                    item.setNode(node);
                    items.add(item);

                }
            }
        }
        return items;
    }

    @Override
    public List<WorkOrderItem> getItemsByOrderNo(String orderNo) {
        List<WorkOrderItem> items = workOrderItemDao.getItemsByOrderNo(orderNo);

        if(items!=null && items.size()>0){
            for (WorkOrderItem item : items) {
                WorkFlowNode node = workFlowNodeService.getNodeByFlowNo(item.getFlowNo(), orderNo);

                if(999==node.getDeptNo().intValue()){
                    AgentInfo agentInfo = agentInfoDao.select(item.getSenderId() + "");
                    if(agentInfo!=null){
                        item.setSenderName(agentInfo.getAgentName());
                    }
                }

                //获取附件
                List<WorkFileInfo> files = workFileInfoService.getFiles(3,item.getId());
                item.setFileInfos(files);
                //获取图片
                List<WorkFileInfo> imgs = workFileInfoService.getImgs(3,item.getId());
                item.setImgInfos(imgs);
                //获取备注
                List<WorkRemarkRecord> remarks = workRemarkRecordService.getRemarks(2, item.getId());
                if(remarks!=null && remarks.size()>0){
                    for (WorkRemarkRecord remark : remarks) {
                        //获取备注附件
                        List<WorkFileInfo> remarkFiles = workFileInfoService.getFiles(2,remark.getId());
                        remark.setFileInfos(remarkFiles);
                        ////获取备注图片
                        List<WorkFileInfo> remarkImgs = workFileInfoService.getImgs(2,remark.getId());
                        remark.setImgInfos(remarkImgs);
                    }
                }
                item.setRemarks(remarks);
                item.setCreateTimeStr(DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",item.getCreateTime()));

                //获取状态
                if(item.getRejectStatus()==1){//驳回
                    item.setStatus("已驳回至"+item.getReceiverDeptName()+"");
                }
                if(item.getTransferStatus()==1){
                    item.setStatus("已转单至"+item.getReceiverDeptName()+"");
                }
                if(item.getRejectStatus()==0 && item.getTransferStatus()==0){
                    item.setStatus("已回复");
                }
            }
        }
        return items;
    }

    @Override
    public WorkOrderItem getLastestItem(String orderNo,Integer deptNo,Integer userId) {
        return workOrderItemDao.getLastestItem(orderNo,deptNo,userId);
    }
}
