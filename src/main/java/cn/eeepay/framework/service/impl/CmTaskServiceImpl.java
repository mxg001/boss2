package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoCreditMgr.CmTaskDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmTaskInfo;
import cn.eeepay.framework.service.CmTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cmTaskService")
@Transactional
public class CmTaskServiceImpl implements CmTaskService {

	@Resource
	private CmTaskDao cmTaskDao;

	/**
	 * 查询limit_add_bank_strategy里的所有银行名称
	 */
	public List<Map<String,String>> selectBankName() {
		return cmTaskDao.selectBankName();
	}

	/**
	 * 提额任务查询
	 */
	public List<CmTaskInfo> selectTaskInfo(Page<CmTaskInfo> page, CmTaskInfo info) {
		List<CmTaskInfo> list = cmTaskDao.selectTaskInfo(page, info);
		for (CmTaskInfo cti : list) {
			if (StringUtils.isNotBlank(cti.getIncreasePossible())) {	//四舍五入-提额指数
				cti.setIncreasePossible(new BigDecimal(cti.getIncreasePossible()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			}
			if (StringUtils.isNotBlank(cti.getCardHealth())) {	//四舍五入-用卡健康度
				cti.setCardHealth(new BigDecimal(cti.getCardHealth()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			}
			if (StringUtils.isNotBlank(cti.getTaskHaveComplete())) {	//四舍五入-计划完成度
				cti.setTaskHaveComplete(new BigDecimal(cti.getTaskHaveComplete()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			}
		}
		return list;
	}

	/**
	 * 提额任务详情
	 */
	public Map<String, Object> queryTaskDetail(Map<String, Object> msg, String id) {
		List<CmTaskInfo> list = cmTaskDao.queryTaskDetail(id);
		Map<String, String> report = new HashMap<>();//存放任务概况
		for (CmTaskInfo info : list) {
			//处理数据
			if ("2".equals(info.getDataType())) {
				if (StringUtils.isNotBlank(info.getTaskValue())) {//百分数要四舍五入
					info.setTaskValue(new BigDecimal(info.getTaskValue()).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "%");
				}
			} else if ("3".equals(info.getDataType())) {
				info.setTaskValue("1".equals(info.getTaskValue()) ? "是" : "否");//处理完成值的显示
			}
			if ("1007".equals(info.getTargetId())) {//逾期指数
				report.put("cardHealth", new BigDecimal(info.getCardHealth()).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "分");
				report.put("increasePossible", new BigDecimal(info.getIncreasePossible()).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "%");
				report.put("startTime", info.getStartTime());
				report.put("endTime", info.getEndTime());
				report.put("yuQi", info.getTaskValue());
			} else if ("1008".equals(info.getTargetId())) {//用卡安全指数
				report.put("anQuan", info.getTaskValue());
			} else if ("1009".equals(info.getTargetId())) {//提额加分
				info.setTaskValue(new BigDecimal(info.getTaskValue()).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "分");
				report.put("jiaFen", info.getTaskValue());
			}
		}
		msg.put("info", list);
		msg.put("report", report);
		msg.put("status", true);
		return msg;
	}

	public List<CmTaskInfo> exportTaskInfo(CmTaskInfo info){
		List<CmTaskInfo> list = cmTaskDao.exportTaskInfo(info);
		for (CmTaskInfo cti : list) {
			if (StringUtils.isNotBlank(cti.getIncreasePossible())) {	//四舍五入-提额指数
				cti.setIncreasePossible(new BigDecimal(cti.getIncreasePossible()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			}
			if (StringUtils.isNotBlank(cti.getCardHealth())) {	//四舍五入-用卡健康度
				cti.setCardHealth(new BigDecimal(cti.getCardHealth()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			}
			if (StringUtils.isNotBlank(cti.getTaskHaveComplete())) {	//四舍五入-计划完成度
				cti.setTaskHaveComplete(new BigDecimal(cti.getTaskHaveComplete()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			}
		}
		return list;
	}

}
