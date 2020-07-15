package cn.eeepay.framework.service.workOrder.impl;

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.workOrder.WorkFileInfoDao;
import cn.eeepay.framework.dao.workOrder.WorkFlowNodeDao;
import cn.eeepay.framework.dao.workOrder.WorkTypeDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.workOrder.WorkFileInfo;
import cn.eeepay.framework.model.workOrder.WorkFlowNode;
import cn.eeepay.framework.model.workOrder.WorkOrderUser;
import cn.eeepay.framework.model.workOrder.WorkType;
import cn.eeepay.framework.service.impl.SeqService;
import cn.eeepay.framework.service.workOrder.WorkFileInfoService;
import cn.eeepay.framework.service.workOrder.WorkFlowNodeService;
import cn.eeepay.framework.service.workOrder.WorkOrderUserService;
import cn.eeepay.framework.service.workOrder.WorkTypeService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/26 10:31
 */
@Transactional
@Service("workTypeServiceImpl")
public class WorkTypeServiceImpl implements WorkTypeService {

    @Resource
    private WorkTypeDao workTypeDao;


    @Resource
    private WorkFlowNodeDao workFlowNodeDao;

    @Resource
    private WorkFileInfoDao workFileInfoDao;

    @Resource
    private SysDictDao sysDictDao;

    @Resource
    private WorkFileInfoService workFileInfoService;

    @Resource
    private WorkOrderUserService workOrderUserService;


    @Resource
    private WorkFlowNodeService workFlowNodeService;

    @Override
    public void insert(WorkType info) {
        //判断类型名称是否已存在  存在则不能添加
        checkWorkTypeName(info.getName());
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        workTypeDao.insert(info);

        if(info.getId()!=null){
            //保存流程节点
            workFlowNodeService.saveWorkFlowNode(info.getWorkFlowNodeList(),principal.getUsername(),info.getId());
            //保存类型图片
            workFileInfoService.insertFiles(1,info.getId(),info.getWorkFileInfos());
        }
    }



    @Override
    public void update(WorkType info) {
        //判断类型名称是否已存在  存在则不能添加
        //先判断名称是否已发生变更
        if(!workTypeDao.getWorkTypeById(info.getId()).getName().equals(info.getName())){
            checkWorkTypeName(info.getName());
        }
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        workTypeDao.update(info);
        //保存流程节点  只做新增操作 保存前先将原来的流程记录删除  在做新增
        //删除相关流程信息
        workFlowNodeDao.delByWorkTypeId(info.getId());
        workFlowNodeService.saveWorkFlowNode(info.getWorkFlowNodeList(),principal.getUsername(),info.getId());
        //保存类型图片 只做新增操作  保存前先将原来的图片记录删除  在做新增
        workFileInfoDao.delByWorkTypeId(1,info.getId());
        workFileInfoService.insertFiles(1,info.getId(),info.getWorkFileInfos());
    }

    private void checkWorkTypeName(String name) {
        WorkType info = new WorkType();
        info.setName(name);
        List<WorkType> workTypes = workTypeDao.query(new Page<WorkType>(), info);
        if(workTypes!=null && workTypes.size()>0){
            throw new WorkOrderException("类型名称已存在");
        }
    }



    @Override
    public List<WorkType> getAll() {
        List<WorkType> workTypes = workTypeDao.getAll();
        if(workTypes!=null && workTypes.size()>0){
            for (WorkType workType : workTypes) {
                String dealProcess = workType.getDealProcess();
                if(StringUtil.isNotBlank(dealProcess)){
                    StringBuilder sb = new StringBuilder();
                    String[] dealProcessArr = dealProcess.split("-");
                    for (String deptId : dealProcessArr) {
                        //获取部门名称
                        sb.append(sysDictDao.getSysNameByKV("DEPT_LIST",deptId)).append("一〉");
                    }
                    sb.setLength(sb.length()-2);
                    workType.setDealProcessName(sb.toString());
                }

            }
        }
        return workTypes;
    }

    @Override
    public List<WorkType> getMyDutyType() {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        List<WorkType> workTypes = workTypeDao.getAll();
        if("2".equals(workUser.getRoleType()) ){
            return this.getAll();
        }
        List<WorkType> result = new ArrayList<>();
        if(workUser.getDutyType()!=null && workUser.getDutyType().intValue()==2){
            String[] dutyArr = workUser.getDutyData().split(",");
            for (String duty : dutyArr) {
                for (WorkType workType : workTypes) {
                    if(workType.getId().toString().equals(duty)){
                        StringBuilder sb = new StringBuilder();
                        String dealProcess = workType.getDealProcess();
                        String[] dealProcessArr = dealProcess.split("-");
                        for (String deptId : dealProcessArr) {
                            //获取部门名称
                            sb.append(sysDictDao.getSysNameByKV("DEPT_LIST",deptId)).append("一〉");
                        }
                        sb.setLength(sb.length()-2);
                        workType.setDealProcessName(sb.toString());
                        result.add(workType);

                    }
                }
            }
        }else{
            return this.getAll();
        }
        return result;
    }

    @Override
    public List<WorkType> query(Page<WorkType> page,WorkType info) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkOrderUser workUser = workOrderUserService.getWorkUserByBossUserName(principal.getUsername());
        if(workUser==null){
            throw new WorkOrderException("当前用户无权查看工单类型");
        }
        List<WorkType> query = workTypeDao.query(page, info);
        //回显流程部门
        if(page.getResult()!=null && page.getResult().size()>0){
            for (WorkType workType : page.getResult()) {
                String dealProcess = workType.getDealProcess();
                if(StringUtil.isNotBlank(dealProcess)){
                    StringBuilder sb = new StringBuilder();
                    String[] dealProcessArr = dealProcess.split("-");
                    for (String deptId : dealProcessArr) {
                        //获取部门名称
                        sb.append(sysDictDao.getSysNameByKV("DEPT_LIST",deptId)).append("一〉");
                    }
                    sb.setLength(sb.length()-2);
                    workType.setDealProcessName(sb.toString());
                }

            }
        }
        return query;
    }

    @Override
    public void del(Long id) {
        int del = workTypeDao.del(id);
        if(del>0){
            //删除相关流程信息
            workFlowNodeDao.delByWorkTypeId(id);
            //删除图片信息
            workFileInfoDao.delByWorkTypeId(1,id);
        }
    }

    @Override
    public WorkType getWorkTypeById(Long id) {
        WorkType workType = workTypeDao.getWorkTypeById(id);
        if(workType!=null){
            //获取图片
            List<WorkFileInfo> files = workFileInfoService.getImgs(1, id);
            if(files!=null && files.size()>0){
                for (WorkFileInfo file : files) {
                    file.setRealUrl(CommonUtil.getImgUrlAgent(file.getFileUrl()));
                }
            }
            workType.setWorkFileInfos(files);
            //获取流程
            workType.setWorkFlowNodeList(workFlowNodeDao.getNodesByWorkTypeID(id));
        }
        return workType;
    }

    @Override
    public List<Map<String, String>> getAllWorkTypes() {
        return workTypeDao.getAllWorkTypes();
    }
}
