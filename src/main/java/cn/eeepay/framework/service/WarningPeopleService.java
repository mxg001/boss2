package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WarningPeople;
import cn.eeepay.framework.model.WarningSet;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/8/008.
 * @author liuks
 * 预警人service
 */
public interface WarningPeopleService {
    List<WarningPeople> getWarningPeople(Page<WarningPeople> page,WarningPeople wp);

    int deleteWarningPeople(int id);

    int addWarningPeople(WarningPeople wp);

    WarningPeople findWarningPeoplebyUserId(int userId,int status);

    int sumWarningPeople(WarningPeople wp);

    List<WarningPeople> getWarningPeopleAll(int status);

    int updateWarningPeople(WarningPeople wp);

    Map<String, Object> synchronous(List<WarningPeople> list);

    WarningPeople getWarningPeopleById(int id);

    int updateWarningPeopleByAssignmentTask(String assignmentTask, int id);

	int saveOrUpdateWarningSet(WarningSet wp);

	WarningSet getWarningSet(Integer serviceId);

	List<WarningSet> getExceptionNumber();

	List<WarningSet> getFailurExceptionNumber();

	WarningPeople getWarningPeopleByIdAndStatus(Integer id, int i);

	int updateWarningSids(String param, Integer id, int i);

	/**
	 * 根据预警人员的Id获取预警信息
	 * 
	 * @return
	 */
	WarningPeople getCsWarningPeople(Integer id);

	/**
	 * 更新用户的任务
	 * 
	 * @param sids
	 * @param id
	 * @return
	 */
	int updateSidsById(String sids, Integer id);
}
