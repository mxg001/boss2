package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OutAccountService;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.OutAccountServiceService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.WarningSetDao;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.WarningSet;
import cn.eeepay.framework.service.AcqServiceProService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.WarningSetService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

@Service
@Transactional
public class WarningSetServiceImpl implements WarningSetService {

	private static final Logger log = LoggerFactory.getLogger(WarningSetServiceImpl.class);

	@Autowired
	private WarningSetDao dao;

	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private AcqServiceProService acqServiceProService;

    @Autowired
    private OutAccountServiceService outAccountServiceService;

	@Override
	public WarningSet getWaringInfoByService(Integer serviceId) {
		// 1 根据Id去warning_set表中查询 没有取字典中统一设置的周期和笔数 （收单交易设置）
		//带上当前时间去找有没有个性化配置，有则返回
		//若没有，看有没有全天的
		Date nowDate = new Date();
		WarningSet warningSet = dao.findByServiceIdAndDate(serviceId,nowDate,WarningSet.warningSetTypeIn);
		if(warningSet == null ){
			warningSet = dao.findByServiceId(serviceId,WarningSet.warningSetTypeIn);
		}

		SysDict cycle = sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_TRANSACTION);
		if (warningSet != null) {
			// 从数据字典中拿到预警周期
			warningSet.setWarningCycle(Integer.valueOf(cycle.getSysValue()));
			return warningSet;
		}
		warningSet = new WarningSet();
		// 服务ID存在拿到服务名称
		if (acqServiceProService.isExsitByServiceId(serviceId)) {
			String serviceName = acqServiceProService.getServiceNameByServiceId(serviceId);
			warningSet.setServiceName(serviceName);
		}
		SysDict number = sysDictService.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_TRANSACTION);
		// 进行设置
		warningSet.setServiceId(serviceId);
		warningSet.setExceptionNumber(Integer.valueOf(number.getSysValue()));
		warningSet.setWarningCycle(Integer.valueOf(cycle.getSysValue()));
		return warningSet;
	}

	@Override
	public int updateWarningSet(WarningSet info) {
		// 1.根据服务ID去收款服务查询是否存在 不存在 抛出异常
        if(info.getStatus() == WarningSet.warningSetTypeIn){
            boolean isExsit = acqServiceProService.isExsitByServiceId(info.getServiceId());
            if (!isExsit) {
                throw new BossBaseException("根据收单服务ID没找到该服务");
            }
        } else {
            OutAccountService outAccountService = outAccountServiceService.findServiceId(info.getServiceId());
            if (outAccountService == null) {
                throw new BossBaseException("根据出款服务ID没找到该服务");
            }
        }

//		 2.根据服务ID去查询预警设置 存在更新 不存在新增
		//如果info ID不为空，则根据ID修改
		//新增和修改，都需要校验：针对同一服务（收单服务 | 出款服务），预警时间不能重叠
		int checkNum = checkWarnTime(info);
		if(checkNum > 0){
			throw new BossBaseException("预警时间不能重叠");
		}

		int num = 0;
		if(info.getId() != null){
			num = dao.update(info);
		} else {
			//如果info ID为空，则新增
			num = dao.insert(info);
		}
		if(num != 1){
			throw new BossBaseException("数据操作异常");
		}
		return num;
	}

	/**
	 * 校验同一个服务的预警时间
	 * @param info
	 * @return
	 */
	private int checkWarnTime(WarningSet info) {
		int checkNum;
//		//检查是否存在预警时间类型为全天的（除自己外），若存在，则预警时间不能重叠
//		checkNum = dao.checkWarnTimeType(info);
//		if(checkNum > 0){
//			return checkNum;
//		}
		//检查自己本身是不是全天，如果是，是否存在其他全天记录（除自己外），若存在，则预警时间不能重叠
		if(info.getWarnTimeType() == 1){
			checkNum = dao.checkOtherWarnTime(info);
		} else {
			//如果自己不是全天，，检查是否存在其他个性化时间重叠，若存在，则预警时间不能重叠
			if(info.getWarnStartTime() == null || info.getWarnEndTime() == null){
				throw new BossBaseException("预警时间不能空");
			}
			//比较时间大小的时候，如果是00:05，这样的，需要补充成00:05:00
			Matcher matcherHHmmStart = DateUtil.HH_mm_patter.matcher(info.getWarnStartTime());
			if(matcherHHmmStart.matches()){
				info.setWarnStartTime(info.getWarnStartTime() + ":00");
			}
			Matcher matcherHHmmEnd = DateUtil.HH_mm_patter.matcher(info.getWarnEndTime());
			if(matcherHHmmEnd.matches()){
				info.setWarnEndTime(info.getWarnEndTime() + ":00");
			}
			Date warnStartTime = DateUtil.parseFormatTime(info.getWarnStartTime());
			Date warnEndTime = DateUtil.parseFormatTime(info.getWarnEndTime());
			//如果起始时间大于结束时间，比如22:00:00~06:00:00
			if(warnStartTime.getTime() > warnEndTime.getTime()){
				checkNum = dao.checkWarnStartEndTime(info);
			} else {
				//如果起始时间小于等于结束时间，比如06:00:00~22:00:00
				checkNum = dao.checkWarnTime(info);
			}
		}
		return checkNum;
	}

	@Override
	public void selectPage(WarningSet baseInfo, Page<WarningSet> page) {
		dao.selectPage(baseInfo, page);
		List<WarningSet> list = page.getResult();
		if(list != null && list.size() > 0){
			SysDict cycleSysDict = sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_TRANSACTION);
			int cycle = Integer.valueOf(cycleSysDict.getSysValue());
			String warningCycleStr = cycle + "分钟";
			for(WarningSet info: list){
				info.setWarningCycleStr(warningCycleStr);
				if(info.getWarnTimeType() == 1){
					info.setWarnTimeStr("全天");
				} else {
					info.setWarnTimeStr(info.getWarnStartTime() + "-" + info.getWarnEndTime());
				}
			}
		}
		return;
	}

	@Override
	public int updateWarnStatus(WarningSet baseInfo) {
		return dao.updateWarnStatus(baseInfo);
	}

	@Override
	public int deleteWarning(WarningSet baseInfo) {
		baseInfo.setWarnStatus(2);//表示删除
		return dao.deleteWarning(baseInfo);
	}

	@Override
	public void exportInfo(HttpServletResponse response, WarningSet baseInfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		OutputStream ouputStream = null;
		try {
			Page<WarningSet> page = new Page<>(0, Integer.MAX_VALUE);
			selectPage(baseInfo, page);
			List<WarningSet> list = page.getResult();
			ListDataExcelExport export = new ListDataExcelExport();
			String fileName = "收单服务预警阀值设置列表"+sdf.format(new Date())+export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			List<Map<String, String>> data = new ArrayList<>();
			Map<String, String> serviceTypeMap = sysDictService.selectMapByKey("ACQ_SERVICE_TYPE");
			for(WarningSet item: list){
				Map<String,String> map = new HashMap<>();
				map.put("serviceId", String.valueOf(item.getServiceId()));
				map.put("serviceName", item.getServiceName());
				map.put("acqEnname", item.getAcqEnname());
				map.put("serviceType", serviceTypeMap.get(item.getServiceType()));
				map.put("warnTimeStr", item.getWarnTimeStr());
				map.put("warningCycleStr", item.getWarningCycleStr());
				map.put("exceptionNumber", String.valueOf(item.getExceptionNumber()));
				if(item.getWarnStatus() == 0){
					map.put("warnStatus", "关闭");
				} else if(item.getWarnStatus() == 1){
					map.put("warnStatus", "打开");
				}
				data.add(map);
			}
			String[] cols = new String[]{
					"serviceId","serviceName","acqEnname","serviceType","warnTimeStr",
					"warningCycleStr","exceptionNumber","warnStatus"};
			String[] colsName = new String[]{
					"收单服务ID","服务名称","收单机构","服务类型","预警时间",
					"预警周期","异常笔数","状态"};
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出预警阀值设置列表异常", e);
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void selectSettlePage(WarningSet baseInfo, Page<WarningSet> page) {
		dao.selectSettlePage(baseInfo, page);
		List<WarningSet> list = page.getResult();
		if(list != null && list.size() > 0){
			SysDict cycleDict =  sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENTING);
			SysDict failurCycleDict =  sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
			int cycle = Integer.valueOf(cycleDict.getSysValue());
			int failurCycle = Integer.valueOf(failurCycleDict.getSysValue());
			String warningCycleStr = cycle + "分钟";
			String failurWarningCycleStr = failurCycle + "分钟";
			for(WarningSet info: list){
				info.setWarningCycleStr(warningCycleStr);
				info.setFailurWarningCycleStr(failurWarningCycleStr);
				if(info.getWarnTimeType() == 1){
					info.setWarnTimeStr("全天");
				} else {
					info.setWarnTimeStr(info.getWarnStartTime() + "-" + info.getWarnEndTime());
				}
			}
		}
		return;
	}

	@Override
	public void exportSettleInfo(HttpServletResponse response, WarningSet warningSet) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		OutputStream ouputStream = null;
		try {
			Page<WarningSet> page = new Page<>(0, Integer.MAX_VALUE);
			selectSettlePage(warningSet, page);
			List<WarningSet> list = page.getResult();
			ListDataExcelExport export = new ListDataExcelExport();
			String fileName = "出款服务预警阀值设置列表"+sdf.format(new Date())+export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			List<Map<String, String>> data = new ArrayList<>();
			Map<String, String> serviceTypeMap = sysDictService.selectMapByKey("MONEY_SERVICE_TYPE");
			for(WarningSet item: list){
				Map<String,String> map = new HashMap<>();
				map.put("serviceId", String.valueOf(item.getServiceId()));
				map.put("serviceName", item.getServiceName());
				map.put("acqEnname", item.getAcqEnname());
				map.put("serviceType", serviceTypeMap.get(item.getServiceType()));
				map.put("warnTimeStr", item.getWarnTimeStr());
				map.put("warningCycleStr", item.getWarningCycleStr());
				map.put("exceptionNumber", String.valueOf(item.getExceptionNumber()));
				map.put("failurWarningCycleStr", item.getFailurWarningCycleStr());
				map.put("failurExceptionNumber", String.valueOf(item.getFailurExceptionNumber()));
				if(item.getWarnStatus() == 0){
					map.put("warnStatus", "关闭");
				} else if(item.getWarnStatus() == 1){
					map.put("warnStatus", "打开");
				}
				data.add(map);
			}
			String[] cols = new String[]{
					"serviceId","serviceName","acqEnname","serviceType","warnTimeStr",
					"warningCycleStr","exceptionNumber","failurWarningCycleStr","failurExceptionNumber","warnStatus"};
			String[] colsName = new String[]{
					"收单服务ID","服务名称","收单机构","服务类型","预警时间",
					"结算中预警周期","结算中异常笔数","结算失败预警周期","结算失败异常笔数","状态"};
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出出款服务预警阀值设置列表异常", e);
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public WarningSet getWaringInfoByServiceAndStatus(Integer serviceId, Integer warningSetTypeOut) {
		// 1 根据Id去warning_set表中查询 没有取字典中统一设置的周期和笔数 （收单交易设置）
		//带上当前时间去找有没有个性化配置，有则返回
		//若没有，看有没有全天的
		Date nowDate = new Date();
		WarningSet warningSet = dao.findByServiceIdAndDate(serviceId,nowDate, warningSetTypeOut);
		if(warningSet == null ){
			warningSet = dao.findByServiceId(serviceId,warningSetTypeOut);
		}
		return warningSet;
	}

}
