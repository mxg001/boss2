package cn.eeepay.framework.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.TransInfoFreezeNewLog;

public interface TransInfoFreezeNewLogService {

	List<TransInfoFreezeNewLog> queryByOrderNo(String orderNo);
	
	Map<String, Object> insertInfo(TransInfoFreezeNewLog record,CollectiveTransOrder cto,String fd,String sts, String operId, String realName, BigDecimal originalPreFrozenMoney);
}
