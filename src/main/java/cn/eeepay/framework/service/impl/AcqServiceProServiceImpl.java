package cn.eeepay.framework.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.boss.system.CommonConst;
import cn.eeepay.framework.dao.AcqServiceDao;
import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.model.BossOperLog;
import cn.eeepay.framework.service.AcqServiceProService;
import cn.eeepay.framework.service.BossOperLogService;
import cn.eeepay.framework.service.GroupServiceService;
import cn.eeepay.framework.util.DateUtil;

@Service("acqServiceProService")
@Transactional
public class AcqServiceProServiceImpl implements AcqServiceProService {
	
	private final Logger log = LoggerFactory.getLogger(AcqServiceProServiceImpl.class);

	@Resource
	private AcqServiceDao acqServiceDao;
	@Autowired
	private GroupServiceService groupServiceService;
	@Autowired
	private BossOperLogService bossOperLogService ;
	
	@Override
	public List<AcqService> selectBoxAllInfo(String acqId) {
		return acqServiceDao.selectBoxAllInfo(acqId);
	}

	@Override
	public List<AcqService> selectBox(String acqId) {
		return acqServiceDao.selectBox(acqId);
	}

	@Override
	public AcqService findServiceId(Integer id) {
		return acqServiceDao.selectInfo(id);
	}
	
	@Override
	public void periodicityClose() {
		// 获取有设置周期性关闭的收单服务列表
		List<AcqService> list = acqServiceDao.selectByPeriodicityTime();
		if (list.size() < 1) {
			return;
		}

		// 判断当前时间是否在关闭期间 ，在进行关闭 不在判断是否指定区间 在关闭 不在开启
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// 当前时间的后一天 为了使用到系统的日期格式化工具
		Date dateAfter = DateUtils.addDays(date, 1);

		for (AcqService acqService : list) {
			String startTimeStr = DateUtil.getBeforeDateString(dateAfter) + " " + acqService.getPeriodicityStartTime();
			String endTimeStr = DateUtil.getBeforeDateString(dateAfter) + " " + acqService.getPeriodicityEndTime();
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = format.parse(startTimeStr);
				endDate = format.parse(endTimeStr);
			} catch (ParseException e) {
				log.error("日期解析异常", e);
			}

			// 判断系统当前时间是否在周期性关闭时间之间
			boolean isBetween = date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime();
			// 判断时间是否为空
			boolean isNull = acqService.getTimeEndTime() == null && acqService.getTimeStartTime() == null;
			// 判断当前时间是否在指定时间之外
			boolean isOut = isNull || (date.getTime() > acqService.getTimeEndTime().getTime()
					|| date.getTime() < acqService.getTimeStartTime().getTime());

			if (isBetween && acqService.getServiceStatus() == 1) {
				acqService.setServiceStatus(0);
				groupServiceService.setAcqServiceStatus(acqService);
				BossOperLog operLog = new BossOperLog();
				operLog.setUser_name("超级管理员");
				operLog.setOper_code("acqService.periodicityClose");
				operLog.setMethod_desc("周期性定时关闭收单服务");
				operLog.setOper_status(CommonConst.ONE);
				operLog.setOper_time(date);
				operLog.setRequest_params("服务ID:"+acqService.getId()+"");
				bossOperLogService.insert(operLog);
				log.info("周期性关闭成功-------------------------------------------------------");
			} else if (!isBetween && isOut && acqService.getServiceStatus() == 0) {
				// 开启
				acqService.setServiceStatus(1);
				groupServiceService.setAcqServiceStatus(acqService);
				BossOperLog operLog = new BossOperLog();
				operLog.setUser_name("超级管理员");
				operLog.setOper_code("acqService.periodicityOpen");
				operLog.setMethod_desc("周期性定时关闭收单服务时间到自动开启");
				operLog.setOper_status(CommonConst.ONE);
				operLog.setOper_time(date);
				operLog.setRequest_params("服务ID:"+acqService.getId()+"");
				bossOperLogService.insert(operLog);
				log.info("周期性定时关闭收单服务时间到自动开启成功-------------------------------------------------------");
			}
		}
	}

	@Override
	public boolean isExsitByServiceId(Integer serviceId) {
		return acqServiceDao.countByServiceId(serviceId) == 1;
	}

	@Override
	public String getServiceNameByServiceId(Integer serviceId) {
		return acqServiceDao.findServiceNameByServiceId(serviceId);
	}
	
}
