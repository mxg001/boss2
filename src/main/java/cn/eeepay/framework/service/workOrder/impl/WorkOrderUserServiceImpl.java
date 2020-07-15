package cn.eeepay.framework.service.workOrder.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.UserDao;
import cn.eeepay.framework.dao.workOrder.WorkOrderUserDao;
import cn.eeepay.framework.dao.workOrder.WorkTypeDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.workOrder.WorkOrder;
import cn.eeepay.framework.model.workOrder.WorkOrderUser;
import cn.eeepay.framework.model.workOrder.WorkType;
import cn.eeepay.framework.service.workOrder.WorkOrderUserService;
import cn.eeepay.framework.service.workOrder.WorkTypeService;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 11:41
 */
@Service
@Transactional
public class WorkOrderUserServiceImpl implements WorkOrderUserService {

    @Resource
    private WorkOrderUserDao workOrderUserDao;

    @Resource
    private UserDao userDao;

    @Resource
    private WorkTypeService workTypeService;


    @Resource
    private AgentInfoDao agentInfoDao;


    @Override
    public boolean auth(WorkOrderUser workUser, WorkOrder order){
        if(order==null || workUser==null){
            return false;
        }




        return false;
    }



    @Override
    public WorkOrderUser getWorkUserById(Long id) {
        return workOrderUserDao.getWorkUserById(id);
    }

    @Override
    public int insert(WorkOrderUser info) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        info.setStatus(1);//状态默认开启
        //判断授权唯一性
        checkUeserExisted(info);
        return workOrderUserDao.insert(info);
    }

    private void checkUeserExisted(WorkOrderUser info){
        if(info.getId()!=null){
            WorkOrderUser user = workOrderUserDao.getWorkUserById(info.getId());
            //如果boss账号没有变更 就是修改操作 不需要判断账号是否已经存在
            if(user.getBossUserName().equals(info.getBossUserName())){
                return;
            }
        }
        if(workOrderUserDao.getAdminByName(info.getBossUserName())!=null){
            throw new WorkOrderException("该boss账号已经存在");
        }
    }

    @Override
    public List<WorkOrderUser> getCurrentDeptUser() {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserInfo> users = userDao.getUsersByDeptNo(principal.getDeptId());
        if(users!=null && users.size()>0){
            StringBuffer sb = new StringBuffer();
            for (UserInfo user : users) {
                if(!user.getUserName().equals(principal.getUsername())){
                    sb.append("'").append(user.getUserName()).append("',");
                }
            }
            sb.setLength(sb.length()-1);
            return workOrderUserDao.getWorkUserByUserNames(sb.toString());
        }
        return null;
    }

    @Override
    public int update(WorkOrderUser info) {

        checkUeserExisted(info);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        if(info.getRoleType()!=null && info.getRoleType().equals("2")){
            info.setDutyData(null);
            info.setDutyType(null);
        }
        return workOrderUserDao.update(info);
    }

    @Override
    public int updateStatusById(WorkOrderUser info) {
        return workOrderUserDao.updateStatusById(info);
    }

    @Override
    public int del(Long id) {
        return workOrderUserDao.del(id);
    }

    @Override
    public void query(Page<WorkOrderUser> page, WorkOrderUser info) {
        List<WorkOrderUser> users = workOrderUserDao.query(page, info);
        if(page.getResult()!=null){
            List<WorkOrderUser> result = page.getResult();
            for (WorkOrderUser user : result) {
                if(user.getDutyType()!=null){
                    if(user.getDutyType()==1){//销售名称转换
                        if(user.getDutyData()!=null){
                            String[] sales= user.getDutyData().split(",");
                            StringBuilder sb = new StringBuilder();
                            for (String sale : sales) {
                                sb.append(userDao.getUserInfoByUserName(sale).getRealName()).append(",");
                            }
                            if(sb.length()>1){
                                sb.setLength(sb.length()-1);
                            }
                            user.setDutySale(sb.toString());
                            user.setDutyWorkType("全部");
                        }
                    }if(user.getDutyType()==2){//工单类型名称转换
                        if(user.getDutyData()!=null){
                            String[] workTypes = user.getDutyData().split(",");
                            StringBuilder sb = new StringBuilder();
                            for (String workTypeId : workTypes) {
                                WorkType workType = workTypeService.getWorkTypeById(Long.valueOf(workTypeId));
                                if(workType!=null){
                                    sb.append(workType.getName()).append(",");
                                }
                            }
                            if(sb.length()>1){
                                sb.setLength(sb.length()-1);
                            }
                            user.setDutyWorkType(sb.toString());
                        }
                    }
                }
            }
        }
    }

    @Override
    public int updateStatus(WorkOrderUser info) {
        return workOrderUserDao.updateStatusById(info);
    }

    @Override
    public List<UserInfo> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public List<Map<String,String>> getDutyData(Integer dutyType) {
        if(dutyType==null){
            return null;
        }
        if(dutyType==1){//获取销售人员
            return getSaleUsers();
        }else if(dutyType==2){//获取所有工单
            return getAllWorkTypes();
        }
        return null;
    }


    private List<Map<String,String>> getSaleUsers(){
        return workOrderUserDao.getSaleUsers();
    }
    private List<Map<String,String>> getAllWorkTypes(){
        return workTypeService.getAllWorkTypes();
    }


    @Override
    public WorkOrderUser getWorkUserByBossUserName(String bossUserName) {
        WorkOrderUser workUser = workOrderUserDao.getWorkUserByBossUserName(bossUserName);

        return workUser;
    }


    @Override
    public List<String> getdutyAgent(WorkOrderUser workUser) {
        if(workUser.getDutyType()!=null && workUser.getDutyType()==1){
            if(StringUtil.isNotBlank(workUser.getDutyData())){
                String[] sales = workUser.getDutyData().split(",");
                StringBuilder sb = new StringBuilder();
                for (String sale : sales) {
                    sb.append("'").append(sale).append("'").append(",");
                }

                if(sb.length()>0){
                    sb.setLength(sb.length()-1);
                    List<String> realNames = userDao.getRealNameByUserName(sb.toString());
                    if(realNames!=null && realNames.size()>0){
                        StringBuilder sb1 = new StringBuilder();
                        for (String realName : realNames) {
                            sb1.append("'").append(realName).append("'").append(",");
                        }
                        if(sb1.length()>0){
                            sb1.setLength(sb1.length()-1);
                        }
                        return agentInfoDao.getAgentInfoBySaleName(sb1.toString());
                    }
                }
            }
        }
        return null;
    }
}
