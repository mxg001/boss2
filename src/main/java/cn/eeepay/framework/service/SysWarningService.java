package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

public interface SysWarningService {

    Map getByType(String type);

    int updateSysWarning(Map<String,Object> map);

    List<Map> getListByType(String type);

    int deleteWarningIds(List<Integer> ids);

    int updateSysWarningById(Map map);

    int intsertSysWarning(Map map);

    void merExamineWarningTask();
}
