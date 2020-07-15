package cn.eeepay.framework.service;

import java.util.Map;

public interface ScheduleService {

    int insertTask(String runNo,String interfaceName);

    int updateTask(String runNo,String status);

    Map<String,Object> queryTask(String runNo);

    //异步处理
    Map<String,Object> process(String runNo,String interfaceType);
}
