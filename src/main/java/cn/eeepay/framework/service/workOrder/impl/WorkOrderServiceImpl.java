package cn.eeepay.framework.service.workOrder.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.UserDao;
import cn.eeepay.framework.dao.workOrder.WorkFlowNodeDao;
import cn.eeepay.framework.dao.workOrder.WorkOrderDao;
import cn.eeepay.framework.dao.workOrder.WorkOrderItemDao;
import cn.eeepay.framework.dao.workOrder.WorkTypeDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.workOrder.*;
import cn.eeepay.framework.service.impl.SeqService;
import cn.eeepay.framework.service.workOrder.*;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.auth0.jwt.internal.org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 10:35
 */
@Transactional
@Service
public class WorkOrderServiceImpl implements WorkOrderService {
    private Logger log = LoggerFactory.getLogger(WorkOrderServiceImpl.class);

    @Resource
    private WorkOrderDao workOrderDao;

    @Resource
    private UserDao userDao;

    @Resource
    private WorkFileInfoService workFileInfoService;


    @Resource
    private WorkTypeService workTypeService;


    @Resource
    private WorkFlowNodeService workFlowNodeService;


    @Resource
    private SysDictDao sysDictDao;


    @Resource
    private AgentInfoDao agentInfoDao;


    @Resource
    private WorkOrderUserService workOrderUserService;

    @Resource
    private WorkOrderItemService workOrderItemService;


    @Resource
    private WorkRemarkRecordService workRemarkRecordService;



    @Override
    public int del(Long id) {
        return workOrderDao.del(id);
    }

    @Override
    public void insert(WorkOrder order) {

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setCreateUserId(Long.valueOf(principal.getId()));
        order.setCreateType("P");//发起人类型 "P"平台 "A"代理商'
        //冗余工单类型信息
        WorkType workType = workTypeService.getWorkTypeById(order.getWorkTypeId());
        order.setDealProcess(workType.getDealProcess());
        order.setReplyType(workType.getReplyType());
        order.setWorkTypeName(workType.getName());
        order.setAgentShow(workType.getAgentShow());
        order.setCurrentFlowNo("0");
        order.setAgentReplyStatus(0);


        order.setOrderNo(getWorkOrderNo("P"));
        order.setStatus(1);//'工单状态：1:处理中,2:已处理,3:已驳回,4:已关闭'
        //获取部门编号
        UserInfo userInfo = userDao.getUserInfoByUserName(principal.getUsername());
        if(userInfo.getDeptId()!=null){
            order.setCreateDeptNo(Long.valueOf(userInfo.getDeptId()));
        }

        List<WorkFlowNode> nodes = workFlowNodeService.getNodesByWorkTypeID(order.getWorkTypeId());

        Long insert = workOrderDao.insert(order);
        if(insert<=0){
            throw new WorkOrderException("新增工单失败");
        }

        if(order.getId()!=null){
            //保存图片
            workFileInfoService.insertFiles(4,order.getId(),order.getWorkFileInfos());
            //保存工单流程信息
            workFlowNodeService.saveWorkFlowNode(nodes,order.getOrderNo(),principal.getUsername());
            order.setCurrentFlowNo(nodes.get(0).getFlowNo());
            order.setCurrentDeptNo(nodes.get(0).getDeptNo().intValue());
            int update = workOrderDao.update(order);
            if(update<=0){
                throw new WorkOrderException("更新工单当前节点和部门失败");
            }
        }
    }

    private String getWorkOrderNo(String createType) {
        if("A".equals(createType)) {
            return "A" + getOrderNo();
        } else {
            return "P" + getOrderNo();
        }
    }

    private String getOrderNo() {
        return getCurrentTimeMillisStr() + String.format("%04d", RandomUtils.nextInt(0,9999));
    }

    private String getCurrentTimeMillisStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(date);
    }

    @Override
    public void update(WorkOrder info) {
        workOrderDao.update(info);
    }

    @Override
    public List<WorkOrder> query(Page<WorkOrder> page, WorkOrder info) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        if(workUser==null){
            throw new WorkOrderException("当前用户无权查看工单记录");
        }

        //默认查询本部门
        info.setDeptNo(principal.getDeptId());
        //默认查询待处理
        if(info.getCurrentStatus()==null){
            info.setCurrentStatus(0);
        }

        List<WorkOrder> orderList = null;
        Integer queryType = info.getQueryType();
        if(queryType!=null){
            if(queryType==1){
                //我发起的工单
                info.setCreateUserId(Long.valueOf(principal.getId()));
                info.setCreateDeptNo(Long.valueOf(principal.getDeptId()));
                orderList = workOrderDao.queryMySelf(page, info,workUser);
            }else if(queryType==3){
                //获取负责的所有代理商节点
                List<String> agentNodes = workOrderUserService.getdutyAgent(workUser);
                if(agentNodes!=null && agentNodes.size()>0){
                    orderList = workOrderDao.queryMyAgent(page, info,agentNodes);
                }
            }else if(queryType==2){
                //已处理 已回复
                orderList = workOrderDao.query(page, info,workUser);
            }else if(queryType==0){
                //待处理
                orderList = workOrderDao.query(page, info,workUser);
            }else if(queryType==4){
                //我部门发起的工单
                if(!"2".equals(workUser.getRoleType())){
                    throw new WorkOrderException("你无权查看本部门工单记录！");
                }
                info.setCreateDeptNo(Long.valueOf(principal.getDeptId()));
                orderList = workOrderDao.queryMyDept(page, info,workUser);
            }
        }else{
            throw new WorkOrderException("参数有误!");
        }

        if(page.getResult()!=null){
            for (WorkOrder order : page.getResult()) {
                //权限判断
                WorkFlowNode node = workFlowNodeService.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());
                if(!orderCanOperate(order,workUser)){
                    order.setReplyStatus(false);
                    order.setRemarkStatus(false);
                    order.setRejectStatus(false);
                    order.setCloseStatus(false);
                }else{
                    order.setReplyStatus(replyStatusCheck(order,principal,workUser,node));
                    order.setCloseStatus(closeStatusCheck(order,principal,workUser));
                    order.setRejectStatus(rejectStatusCheck(order,principal,workUser,node));
                    order.setRemarkStatus(remarkStatusCheck(order,principal,workUser,workFlowNodeService.getNodeByOrderNoAndDeptNo(order.getOrderNo(), principal.getDeptId())));
                }

                if(StringUtil.isNotBlank(order.getDealProcess())){
                    //回显流程信息
                    StringBuilder sb = new StringBuilder();
                    String[] deptIdArr = order.getDealProcess().split("-");
                    for (String deptId : deptIdArr) {
                        String deptName = sysDictDao.getSysNameByKV("DEPT_LIST", deptId);
                        if(StringUtil.isNotBlank(deptName)){
                            sb.append(deptName).append("一〉");
                        }
                    }
                    sb.setLength(sb.length()-2);
                    order.setDealProcessName(sb.toString());
                }

                //回显创建人信息
                if("P".equals(order.getCreateType())){
                    UserInfo userInfo = userDao.getUserInfoById(Integer.valueOf(order.getCreateUserId() + ""));
                    if(userInfo!=null){
                        order.setCreateUserName(userInfo.getRealName());
                    }
                }else if("A".equals(order.getCreateType())){
                    AgentInfo agentInfo = agentInfoDao.select(order.getCreateUserId() + "");
                    if(agentInfo!=null){
                        order.setCreateUserName(agentInfo.getAgentName());
                    }
                }

                //回显当前步骤信息
                if(node!=null){
                    if(StringUtil.isBlank(order.getCurrentDeptName())){
                        AgentInfo agentInfo = agentInfoDao.select(node.getDeptNo() + "");
                        if(agentInfo!=null){
                            order.setCurrentDeptName(agentInfo.getAgentName());
                        }
                    }
                    if(StringUtil.isBlank(order.getCurrentUserName())){
                        AgentInfo agentInfo = agentInfoDao.select(node.getCurrentUserId()+"");
                        if(agentInfo!=null){
                            order.setCurrentUserName(agentInfo.getAgentName());
                        }
                    }
                }
            }
        }
        return orderList;
    }



    @Override
    public boolean remarkStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo,WorkOrderUser workUser,WorkFlowNode node) {
        /**
         * 备注
         * 2、工单状态为处理中
         * 3、部门处理状态为 已处理
         * 4、仅工单发起人和部门工单管理员
         * */
        if(order==null || userLoginInfo==null || workUser==null || node==null){
            return false;
        }

        if(order.getStatus().intValue()==4){
            return false;
        }

        if(order.getCreateUserId().intValue()==userLoginInfo.getId().intValue()){
            return true;
        }

        if("2".equals(workUser.getRoleType()) && node.getCurrentStatus()!=null && node.getCurrentStatus().intValue()==2){
            return true;
        }

        if(node.getCurrentUserId()!=null){
            if(node.getCurrentUserId().intValue()!=userLoginInfo.getId().intValue()){
                return false;
            }else{
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean rejectStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo,WorkOrderUser workUser,WorkFlowNode node) {
        /**
         * 驳回
         * 1、未关闭
         * 2、为待回复
         * 3、登录账户所属部门与当前处理部门一致
         * */
        if(order==null || userLoginInfo==null || workUser==null || node==null){
            return false;
        }
        if("0".equals(node.getParentFlowNo())){
            return false;
        }
        if(order.getStatus().intValue()!=1){
            return false;
        }
        if(node.getCurrentStatus()==null || node.getCurrentStatus()!=0){
            return false;
        }
        //当前处理部门不是当前登录人部门
        if(node.getDeptNo().intValue()!=userLoginInfo.getDeptId()){
            return false;
        }
        //如果是管理员
        if("2".equals(workUser.getRoleType())){
            return true;
        }
        //驳回 或者转单
        if(node.getCurrentUserId()!=null){
            if(node.getCurrentUserId().intValue()!=userLoginInfo.getId().intValue()){
                return false;
            }else{
                return true;
            }
        }

        return true;
    }



    @Override
    public boolean closeStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo,WorkOrderUser workUser) {
        /**
         * 未关闭
         * boss 发起人或者  发起人所在部门的管理员
         * */
        if(order==null || userLoginInfo==null || workUser==null){
            return false;
        }

        if(order.getStatus().intValue()==4 || order.getStatus().intValue()==2){
            return false;
        }
        if(userLoginInfo.getDeptId().intValue()!=order.getCreateDeptNo().intValue()){
            return false;
        }

        if("2".equals(workUser.getRoleType()) ){
            return true;
        }

        if(order.getCreateUserId().intValue()==userLoginInfo.getId().intValue()){
            return true;
        }
        return false;
    }

    @Override
    public boolean replyStatusCheck(WorkOrder order,UserLoginInfo userLoginInfo,WorkOrderUser workUser,WorkFlowNode node) {
        /**
         * 回复
         * 1、未关闭
         * 2、待处理
         * 3、部门管理员
         * 4、驳回之后 当时操作人 或者 管理员
         * 5、如果是驳回给发起人 发起人可进行回复操作
         * */

        if(order==null || userLoginInfo==null || workUser==null || node==null){
            return false;
        }

        //工单为已关闭
        if(order.getStatus().intValue()==4){
            return false;
        }

        //不是待处理状态
        if(node.getCurrentStatus()==null || node.getCurrentStatus()!=0){
            return false;
        }

        //当前处理部门不是当前登录人部门
        if(node.getDeptNo().intValue()!=userLoginInfo.getDeptId()){
            return false;
        }

        //如果是管理员 可以回复
        if("2".equals(workUser.getRoleType())){
            return true;
        }
        //驳回 或者转单
        if(node.getCurrentUserId()!=null){
            if(node.getCurrentUserId().intValue()!=userLoginInfo.getId().intValue()){
                return false;
            }else{
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean detailStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo,WorkOrderUser workUser) {
        /**
         * 该流程包含当前部门
         *
         * */
        if(order==null || userLoginInfo==null ||workUser==null){
            return false;
        }

        //我发起的工单
        if(order.getCreateUserId().intValue()==userLoginInfo.getId().intValue() && order.getCreateDeptNo().intValue()==userLoginInfo.getDeptId().intValue()){
            if("1".equals(workUser.getRoleType()) ){
                if(workUser.getDutyType()!=null){
                    if(workUser.getDutyType().intValue()==2) {
                        String[] dutyArr = workUser.getDutyData().split(",");
                        for (String duty : dutyArr) {
                            if (order.getWorkTypeId().toString().equals(duty)) {
                                return true;
                            }
                        }
                    }else{
                        return true;
                    }
                }
            }
        }

        if("2".equals(workUser.getRoleType()) && order.getCreateDeptNo().intValue()==userLoginInfo.getDeptId().intValue()){
            return true;
        }


        //我负责的代理
        List<String> agentNodes = workOrderUserService.getdutyAgent(workUser);
        WorkOrder agentOrder = workOrderDao.queryMyAgentByOrderNo(order,agentNodes);
        if(agentOrder!=null){
            return true;
        }

        //当前部门已经存在处理记录
        List<WorkFlowNode> nodes = workFlowNodeService.getNodesByOrderNo(order.getOrderNo());
        if(nodes!=null && nodes.size()>0){
            for (WorkFlowNode nodeFlow : nodes) {
                if(nodeFlow.getDeptNo().intValue()==userLoginInfo.getDeptId().intValue()){
                    if(nodeFlow.getCurrentStatus()!=null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean orderCanOperate(WorkOrder order, WorkOrderUser workUser) {
        if("2".equals(workUser.getRoleType()) ){
            return true;
        }
        if(workUser.getDutyType()!=null){
            if(workUser.getDutyType().intValue()==2) {
                String[] dutyArr = workUser.getDutyData().split(",");
                for (String duty : dutyArr) {
                    if (order.getWorkTypeId().toString().equals(duty)) {
                        return true;
                    }
                }
            }else{
                return true;
            }
        }

        return false;
    }


    @Override
    public WorkOrder getWorkOrderById(Long id) {

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        WorkOrder order = workOrderDao.getWorkOrderById(id);
        if(order==null){
            throw new WorkOrderException("该工单不存在！");
        }
        if(!replyStatusCheck(order,principal,workUser,workFlowNodeService.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo()))){
            throw new WorkOrderException("你无权回复该工单！");
        }

        List<WorkFlowNode> nodes =  workFlowNodeService.getNodesByOrderNo(order.getOrderNo());
        StringBuilder sb = new StringBuilder();
        //回显流程信息
        WorkFlowNode nextNode = workFlowNodeService.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());
        for (WorkFlowNode node : nodes) {
            if(nextNode.getFlowNo().equals(node.getFlowNo())){
                sb.append("<font color='red'>").append(node.getDeptName()).append("</font>一〉");
            }else{
                sb.append(node.getDeptName()).append("一〉");
            }
        }
        sb.setLength(sb.length()-2);
        order.setDealProcessName(sb.toString());
        return workOrderDao.getWorkOrderById(id);
    }



    @Override
    public WorkOrder getWorkOrderToExport(Long id) {
        WorkOrder order = workOrderDao.getWorkOrderById(id);
        //获取工单流转记录
        List<WorkOrderItem> items = workOrderItemService.getItemsToExportByOrderNo(order);
        order.setItems(items);
        return order;
    }

    @Override
    public void export(WorkOrder info, HttpServletResponse response, HttpServletRequest request) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        if(workUser==null){
            throw new WorkOrderException("当前用户无权查看工单记录");
        }

        //默认查询本部门
        info.setDeptNo(principal.getDeptId());
        //默认查询待处理
        if(info.getCurrentStatus()==null){
            info.setCurrentStatus(0);
        }

        List<WorkOrder> orderList = null;
        Integer queryType = info.getQueryType();
        if(queryType!=null){
            if(queryType==1){
                info.setCreateUserId(Long.valueOf(principal.getId()));
                info.setCreateDeptNo(Long.valueOf(principal.getDeptId()));
                orderList = workOrderDao.exportMySelf(info,workUser);
            }else if(queryType==3){
                //获取负责的所有代理商节点
                List<String> agentNodes = workOrderUserService.getdutyAgent(workUser);
                if(agentNodes!=null && agentNodes.size()>0){
                    orderList = workOrderDao.exportMyAgent(info,agentNodes);
                }
            }else if(queryType==2){
                orderList = workOrderDao.export(info,workUser);
            }else if(queryType==0){
                orderList = workOrderDao.export(info,workUser);
            }else if(queryType==4){
                //我部门发起的工单
                if(!"2".equals(workUser.getRoleType())){
                    throw new WorkOrderException("你无权查看本部门工单记录！");
                }
                info.setCreateDeptNo(Long.valueOf(principal.getDeptId()));
                orderList = workOrderDao.exportMyDept(info,workUser);
            }
        }else{
            throw new WorkOrderException("参数有误!");
        }

        try {

            String fileName = "工单记录.xlsx";
            String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            if(orderList!=null && orderList.size()>0){
                for (WorkOrder order : orderList) {
                    order = getWorkOrderToExport(order.getId());
                    Map<String,String> map = new HashMap<>();
                    map.put("orderNo",order.getOrderNo());
                    map.put("workTypeName",order.getWorkTypeName());

                    //回显创建人信息
                    if("P".equals(order.getCreateType())){
                        UserInfo userInfo = userDao.getUserInfoById(Integer.valueOf(order.getCreateUserId() + ""));
                        if(userInfo!=null){
                            order.setCreateUserName(userInfo.getRealName());
                        }
                    }else if("A".equals(order.getCreateType())){
                        AgentInfo agentInfo = agentInfoDao.select(order.getCreateUserId() + "");
                        if(agentInfo!=null){
                            order.setCreateUserName(agentInfo.getAgentName());
                        }
                    }
                    map.put("createUserName",order.getCreateUserName());
                    map.put("receiverAgentName",order.getReceiverAgentName());
                    map.put("oneAgentName",order.getOneAgentName());
                    map.put("urgeStatus",getUrgeStatus(order.getOverDueReply()));
                    map.put("createTime",DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",order.getCreateTime()));
                    if(order.getItems()!=null && order.getItems().size()>0){
                        int i = 0;
                        for (WorkOrderItem item : order.getItems()) {
                            i++;
                            map.put("dept"+i,item.getSenderDeptName());
                            if(StringUtil.isBlank(item.getSenderName()) && item.getSenderDeptNo().intValue()==999){
                                //获取代理商名称
                                map.put("user"+i,agentInfoDao.select(item.getSenderId().toString()).getAgentName());
                            }else{
                                map.put("user"+i,item.getSenderName());
                            }
                            if(item.getNode()!=null){
                                map.put("status"+i,getStatus(item.getNode().getCurrentStatus()));
                            }

                            map.put("date"+i,DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",item.getCreateTime()));
                        }
                    }
                    data.add(map);
                }
            }

                ListDataExcelExport export = new ListDataExcelExport();
                String[] cols = new String[] { "orderNo", "workTypeName", "createUserName", "receiverAgentName",
                        "oneAgentName","urgeStatus","createTime",
                        "dept1","user1","status1","date1",
                        "dept2","user2","status2","date2",
                        "dept3","user3","status3","date3",
                        "dept4","user4","status4","date4",
                        "dept5","user5","status5","date5",
                        "dept6","user6","status6","date6"
                };
                String[] colsName = new String[] { "工单编号", "工单类型", "发起人", "工单所属代理商",
                        "工单一级代理商","逾期回复状态","创建时间",
                        "第1处理部门","处理人","第1处理部门处理状态","第1处理部门处理日期",
                        "第2处理部门","处理人","第2处理部门处理状态","第2处理部门处理日期",
                        "第3处理部门","处理人","第3处理部门处理状态","第3处理部门处理日期",
                        "第4处理部门","处理人","第4处理部门处理状态","第4处理部门处理日期",
                        "第5处理部门","处理人","第5处理部门处理状态","第5处理部门处理日期",
                        "第6处理部门","处理人","第6处理部门处理状态","第6处理部门处理日期"
                };
                OutputStream outputStream = response.getOutputStream();
                export.export(cols, colsName, data, outputStream);
                outputStream.close();


        }catch (Exception e){
            log.error("导出工单记录失败",e);
        }



    }

    private String getUrgeStatus(Integer overDueReply) {
        if(overDueReply==null){
            return "";
        }
        switch (overDueReply){
            case 0:
                return "未逾期";
            case 1:
                return "代理商逾期回复";
            default:
                return "";
        }
    }

    private String getStatus(Integer status) {
        if(status==null){
            return "";
        }
        switch (status){
            case 0:
                return "待处理";
            case 2:
                return "已处理";
            case 3:
                return "已驳回";
            default:
                return "";
        }
    }

    @Override
    public WorkOrder getWorkOrderDetailById(Long id) {

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        WorkOrder order = workOrderDao.getWorkOrderById(id);

        if(!detailStatusCheck(order,principal,workUser)){
            throw new WorkOrderException("当前用户无权查看该工单！");
        }

        List<WorkFlowNode> nodes =  workFlowNodeService.getNodesByOrderNo(order.getOrderNo());
        StringBuilder deptNameSB = new StringBuilder();
        StringBuilder flowDescSB = new StringBuilder();

        //回显流程信息
        for (WorkFlowNode node : nodes) {
            deptNameSB.append(node.getDeptName()).append("一〉");
            flowDescSB.append(StringUtil.filterNull(node.getFlowDesc())).append("一〉");
        }
        deptNameSB.setLength(deptNameSB.length()-2);
        flowDescSB.setLength(flowDescSB.length()-2);
        order.setDealProcessName(deptNameSB.toString());
        order.setFlowDesc(flowDescSB.toString());

        //获取工单流转记录
        List<WorkOrderItem> items = workOrderItemService.getItemsByOrderNo(order.getOrderNo());
        order.setItems(items);
        //获取工单备注
        List<WorkRemarkRecord> remarks = workRemarkRecordService.getRemarks(1, order.getId());
        order.setRemarks(remarks);
        order.setCreateTimeStr(DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",order.getCreateTime()));

        //设置相关权限
        WorkFlowNode node = workFlowNodeService.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());
        if(!orderCanOperate(order,workUser) || node==null){
            order.setReplyStatus(false);
            order.setRejectStatus(false);
        }else{
            order.setReplyStatus(replyStatusCheck(order,principal,workUser,node));
            order.setRejectStatus(rejectStatusCheck(order,principal,workUser,node));
        }
        return order;
    }

    @Override
    public boolean showAgentShow(Long id) {

        WorkOrder order = workOrderDao.getWorkOrderById(id);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(order==null){
            throw new WorkOrderException("该工单不存在");
        }

        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        WorkFlowNode node = workFlowNodeService.getNodeByOrderNoAndDeptNo(order.getOrderNo(), principal.getDeptId());

        if(!remarkStatusCheck(order,principal,workUser,node)){
            throw new WorkOrderException("你无权备注该工单！");
        }

        WorkOrderItem item = workOrderItemService.getLastestItem(order.getOrderNo(),principal.getDeptId(),principal.getId());
        if(item!=null){
            //如果是驳回给代理商
            if(item.getReceiverDeptNo()!=null && item.getReceiverDeptNo().intValue()==999){
                return true;
            }else{
                WorkFlowNode lastNode = workFlowNodeService.getNodeByParentFlowNo(item.getFlowNo(), order.getOrderNo());
                //如果是最后一个节点
                if(lastNode==null){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public WorkOrder getWorkOrderToRejectById(Long id) {

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        WorkOrder order = workOrderDao.getWorkOrderById(id);
        if(order==null){
            throw new WorkOrderException("该工单不存在");
        }

        WorkFlowNode node = workFlowNodeService.getNodeByFlowNo(order.getCurrentFlowNo(), order.getOrderNo());
        if(!rejectStatusCheck(order,principal,workUser,node)){
            throw new WorkOrderException("你无权驳回该工单！");
        }

        String[] flowNoArr = node.getFlowNode().split("-");

        StringBuilder sb = new StringBuilder();
        for (String flowNo : flowNoArr) {
            if(!flowNo.equals("0")){
                sb.append("'").append(flowNo).append("',");
            }
        }
        sb.setLength(sb.length()-1);
        List<WorkFlowNode> preNodes = workFlowNodeService.getNodesByFlowNos(sb.toString(), order.getOrderNo());
        preNodes.remove(preNodes.size()-1);

        //只有指定部门才能退回给代理商 如果不是 屏蔽代理商节点
        String rejectToAgent = sysDictDao.getSysNameByKV("REJECT_TO_AGENT", principal.getDeptId().toString());

        if(StringUtil.isBlank(rejectToAgent)){
            Iterator<WorkFlowNode> it = preNodes.iterator();
            while(it.hasNext()){
                if(it.next().getDeptNo()==999){
                    it.remove();
                }
            }
        }

        order.setPreNodes(preNodes);
        return order;
    }

    @Override
    public WorkOrder getWorkOrderByOrderNo(String orderNo) {
        return workOrderDao.getWorkOrderByOrderNo(orderNo);
    }

    @Override
    public List<UserInfo> getCurrentDeptUser() {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDao.getUsersByDeptNo(principal.getDeptId());
    }


    @Override
    public void close(Long id) {
        WorkOrder order = workOrderDao.getWorkOrderById(id);
        if(order==null){
            throw new WorkOrderException("该工单不存在");
        }

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());

        if(!closeStatusCheck(order,principal,workUser)){
            throw new WorkOrderException("你无权关闭该工单！");
        }

        int close = workOrderDao.close(id);
        if(close<=0){
            throw new WorkOrderException("关闭工单失败");
        }
        workFlowNodeService.updateStatus(order.getOrderNo(),order.getCurrentFlowNo(),2);

    }



    @Override
    public int getToDo() {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        if(workUser==null){
            throw new WorkOrderException("当前用户无权查看工单记录");
        }
        return workOrderDao.getToDo(principal.getDeptId(),workUser);
    }


    @Override
    public Boolean getCurrentWorkUser() {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        return "2".equals(workUser.getRoleType());
    }
}
