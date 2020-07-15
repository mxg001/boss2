package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantBusinessProductDao;
import cn.eeepay.framework.dao.SysWarningDao;
import cn.eeepay.framework.service.SysWarningService;
import cn.eeepay.framework.util.Sms;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

@Service("sysWarningService")
@Transactional
public class SysWarningServiceImpl implements SysWarningService {
	private static final Logger log = LoggerFactory.getLogger(SysWarningServiceImpl.class);
	@Resource
	private SysWarningDao sysWarningDao;
	@Resource
	private MerchantBusinessProductDao merchantBusinessProductDao;

	@Override
	public Map getByType(String type){
		return sysWarningDao.getByType(type);
	}

	@Override
	public int updateSysWarning(Map<String, Object> map) {
		return sysWarningDao.updateSysWarning(map);
	}

	@Override
	public List<Map> getListByType(String type){
		return sysWarningDao.getListByType(type);
	}

	@Override
	public int deleteWarningIds(List<Integer> ids){
		return sysWarningDao.deleteWarningIds(ids);
	}

	@Override
	public int updateSysWarningById(Map map){
		return sysWarningDao.updateSysWarningById(map);
	}

	@Override
	public int intsertSysWarning(Map map){
		return sysWarningDao.intsertSysWarning(map);
	}

	@Override
	public void merExamineWarningTask(){
		Map map=sysWarningDao.getByType("8");
		//1个月前的 00:00:00
		final Calendar startTime = new GregorianCalendar();
		startTime.set(Calendar.HOUR_OF_DAY,0);
		startTime.set(Calendar.MINUTE,0);
		startTime.set(Calendar.SECOND,0);
		startTime.set(Calendar.MILLISECOND,0);
		startTime.add(Calendar.MONTH, -1);
		//当前时间
		final Calendar endTime = new GregorianCalendar();
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
		endTime.set(Calendar.MILLISECOND, 0);
		Integer count=merchantBusinessProductDao.selectMerExamineWarningCount(startTime.getTime(),endTime.getTime());
		if (count!=null&&count>=Integer.parseInt(map.get("num").toString())) {
			try {
				//发短信
				String content = StringUtil.filterNull(map.get("content"));
				String phones = StringUtil.filterNull(map.get("phones"));
				content = content.replaceAll("num", String.valueOf(count));
				//发送短信
				smsByMobiles(phones, content);
			} catch (Exception e) {
				log.error("商户审核预警异常",e);
			}
		}
	}

	/*
	发送短信
	 */
	public void smsByMobiles(String mobiles,String content){
		try {
			String[] arr = mobiles.split(",");
			for (String s : arr) {
				Sms.sendMsg(s, content);
			}
		} catch (Exception e) {
			log.error("发送多个电话短信异常",e);
		}
	}
}
