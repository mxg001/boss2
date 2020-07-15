package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.WarningPeopleDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.WarningPeople;
import cn.eeepay.framework.model.WarningSet;
import cn.eeepay.framework.service.UserService;
import cn.eeepay.framework.service.WarningPeopleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/8/008.
 *  @author liuks
 * 预警人service实现类
 */
@Service("warningPeopleService")
@Transactional
public class WarningPeopleServiceImpl implements WarningPeopleService {

    @Resource
    private WarningPeopleDao warningPeopleDao;

    @Resource
    private UserService userService;

    @Override
    public List<WarningPeople> getWarningPeople(Page<WarningPeople> page,WarningPeople wp) {
        return warningPeopleDao.getWarningPeople(page,wp);
    }

    @Override
    public int deleteWarningPeople(int id) {
        return warningPeopleDao.deleteWarningPeople(id);
    }

    @Override
    public int addWarningPeople(WarningPeople wp) {
        return warningPeopleDao.addaddWarningPeople(wp);
    }

    @Override
    public WarningPeople findWarningPeoplebyUserId(int userId,int status) {
        return warningPeopleDao.findWarningPeoplebyUserId(userId,status);
    }

    @Override
    public int sumWarningPeople(WarningPeople wp) {
        return warningPeopleDao.sumWarningPeople(wp);
    }

    @Override
    public List<WarningPeople> getWarningPeopleAll(int status) {
        return warningPeopleDao.getWarningPeopleAll(status);
    }

    @Override
    public int updateWarningPeople(WarningPeople wp) {
        return warningPeopleDao.updateWarningPeople(wp);
    }

    @Override
    public Map<String, Object> synchronous(List<WarningPeople> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(list!=null&&list.size()>0){
            List<String> updateList=new ArrayList<String>();
            List<String> removeList=new ArrayList<String>();
            for(WarningPeople wp:list){
                if(wp.getUserId()!=null){
                    UserInfo us=userService.getUserInfoById(wp.getUserId());
                    if(us!=null){
                        if(us.getTelNo()==null||"".equals(us.getTelNo())){
                            //电话号为空，删除
                            deleteWarningPeople(wp.getId());
                            removeList.add(wp.getUserName());
                        }else{
                            if(wp.getName()!=null){
                                if(!wp.getName().equals(us.getRealName())||!wp.getPhone().equals(us.getTelNo())){
                                    //用户名或者电话号不一样，更新
                                    wp.setName(us.getRealName());
                                    wp.setPhone(us.getTelNo());
                                    updateWarningPeople(wp);
                                    updateList.add(wp.getUserName());
                                }
                            }else{
                                if(us.getRealName()!=null||!wp.getPhone().equals(us.getTelNo())){
                                    //用户名或者电话号不一样，更新
                                    wp.setName(us.getRealName());
                                    wp.setPhone(us.getTelNo());
                                    updateWarningPeople(wp);
                                    updateList.add(wp.getUserName());
                                }
                            }
                        }
                    }else{
                        //用户不在删除
                        deleteWarningPeople(wp.getId());
                        removeList.add(wp.getUserName());
                    }
                }
            }
            map.put("status",true);
            map.put("updateList",updateList);
            map.put("removeList",removeList);
            return map;
        }
        map.put("status",false);
        map.put("msg","没有记录可以更新!");
        return map;
    }

    @Override
    public WarningPeople getWarningPeopleById(int id) {
        return warningPeopleDao.getWarningPeopleById(id);
    }

    @Override
    public int updateWarningPeopleByAssignmentTask(String assignmentTask, int id) {
        return warningPeopleDao.updateWarningPeopleByAssignmentTask(assignmentTask,id);
    }

	@Override
	public int saveOrUpdateWarningSet(WarningSet ws) {
		WarningSet wsOld = warningPeopleDao.getWarnningSet(ws);
		if (wsOld!=null) {
			return warningPeopleDao.updateWarningSet(ws);
		}else
		return warningPeopleDao.saveWarningSet(ws);
	}

	@Override
	public WarningSet getWarningSet(Integer serviceId) {
		return warningPeopleDao.getWarningSet(serviceId,WarningSet.warningSetTypeOut);
	}

	@Override
	public List<WarningSet> getExceptionNumber() {
		return warningPeopleDao.getExceptionNumber();
	}

	@Override
	public List<WarningSet> getFailurExceptionNumber() {
		return warningPeopleDao.getFailurExceptionNumber();
	}

	@Override
	public WarningPeople getWarningPeopleByIdAndStatus(Integer id, int status) {
		return warningPeopleDao.getWarningPeopleByIdAndStatus(id,status);
	}

	@Override
	public int updateWarningSids(String param, Integer id, int i) {
		return warningPeopleDao.updateWarningSids(param,id,i);
	}
	
	@Override
	public WarningPeople getCsWarningPeople(Integer id ) {
		return warningPeopleDao.getWarningPeopleById(id);
	}

	@Override
	public int updateSidsById(String sids, Integer id) {
		return warningPeopleDao.updateSidsById(sids,id);
	}
}
