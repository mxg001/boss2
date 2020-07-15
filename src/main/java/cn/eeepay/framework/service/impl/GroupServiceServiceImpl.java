package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AcqOrgDao;
import cn.eeepay.framework.dao.GroupServiceDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.model.AcqServiceRate;
import cn.eeepay.framework.model.AcqServiceRateTask;
import cn.eeepay.framework.model.AcqServiceTransRules;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.GroupServiceService;

/**
 * 收单服务service实现
 * 
 * @author junhu
 *
 */
@Service
@Transactional
public class GroupServiceServiceImpl implements GroupServiceService {

	private static final Pattern pattern = Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d{0,3})?)%~(\\d+(\\.\\d+)?)$");

	private static final DecimalFormat format = new java.text.DecimalFormat("0.000");

	private static final DecimalFormat double_format = new java.text.DecimalFormat("0.00");

	@Resource
	private GroupServiceDao groupServiceDao;
	
	@Resource
	private AcqOrgDao acqOrgDao;

	@Override
	public List<AcqOrg> acqOrgSelectBox() {
		return groupServiceDao.acqOrgSelectBox();
	}

	@Override
	public List<AcqService> listAcqServiceByCon(Map<String, Object> param, Page<AcqService> page) {
		return groupServiceDao.listAcqServiceByCon(param, page);
	}

	@Override
	public Integer insertService(AcqService acqService, List<AcqServiceRate> acqServiceRates,
			AcqServiceTransRules acqServiceTransRules) {
		Integer acqServiceId = null;
		if (acqService != null) {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			acqService.setServiceStatus(1);
			acqService.setCreatePerson(principal.getId().toString());
			AcqOrg acq = acqOrgDao.selectByPrimaryKey(acqService.getAcqId());
			if(acq != null){
				acqService.setAcqEnname(acq.getAcqEnname());
			}
			groupServiceDao.insertAcqService(acqService);
			acqServiceId = acqService.getId();
			if (acqServiceRates != null) {
				for (AcqServiceRate acqServiceRate : acqServiceRates) {
					acqServiceRate.setAcqServiceId(acqServiceId);
					this.setServiceRate(acqServiceRate);
					acqServiceRate.setEffectiveDate(new Date());
					acqServiceRate.setEffectiveStatus(1);
					acqServiceRate.setCreatePerson(principal.getId().toString());
					groupServiceDao.insertAcqServiceRate(acqServiceRate);
				}
			}
			if (acqServiceTransRules != null) {
				acqServiceTransRules.setAcqServiceId(acqServiceId);
				acqServiceTransRules.setCreatePerson(principal.getId().toString());
				groupServiceDao.insertAcqServiceTransRules(acqServiceTransRules);
			}
		}
		return acqServiceId;
	}

	public void setServiceRate(AcqServiceRate acqServiceRate) {
		if (acqServiceRate.getRateType() == 5 || acqServiceRate.getRateType() == 6) {
			if (!acqServiceRate.getServiceRate().matches("^(\\d+(\\.\\d{0,3})?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d{0,3})?)%)+"))
				throw new RuntimeException("服务费率设置错误！");
			String[] strs = acqServiceRate.getServiceRate().split("<");
			acqServiceRate.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
			acqServiceRate.setLadder1Max(new BigDecimal(strs[1]));
			acqServiceRate.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
			if (strs.length > 3) {
				acqServiceRate.setLadder2Max(new BigDecimal(strs[3]));
				acqServiceRate.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
				if (acqServiceRate.getLadder2Max().compareTo(acqServiceRate.getLadder1Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if (strs.length > 5) {
				acqServiceRate.setLadder3Max(new BigDecimal(strs[5]));
				acqServiceRate.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
				if (acqServiceRate.getLadder3Max().compareTo(acqServiceRate.getLadder2Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if (strs.length > 7) {
				acqServiceRate.setLadder4Max(new BigDecimal(strs[7]));
				if (acqServiceRate.getLadder4Max().compareTo(acqServiceRate.getLadder3Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
		} else if (acqServiceRate.getRateType() == 4) {
			if (!acqServiceRate.getServiceRate().matches("^(\\d+(\\.\\d{0,3})?)%\\+(\\d+(\\.\\d+)?)"))
				throw new RuntimeException("服务费率设置错误！");
			String[] temp = acqServiceRate.getServiceRate().split("%+");
			acqServiceRate.setRate(new BigDecimal(temp[0]));
			acqServiceRate.setSingleAmount(new BigDecimal(temp[1]));
		} else if (acqServiceRate.getRateType() == 3) {
			Matcher m = pattern.matcher(acqServiceRate.getServiceRate());
			while (m.find()) {
				acqServiceRate.setSafeLine(new BigDecimal(m.group(1)));
				acqServiceRate.setRate(new BigDecimal(m.group(3)));
				acqServiceRate.setCapping(new BigDecimal(m.group(5)));
			}
		} else if (acqServiceRate.getRateType() == 2) {
			String str_ = acqServiceRate.getServiceRate().substring(0, acqServiceRate.getServiceRate().indexOf("%"));
			if (str_.matches("\\d+(\\.\\d{0,3})?")) {
				acqServiceRate.setRate(new BigDecimal(str_));
			} else {
				throw new RuntimeException("服务费率设置错误！");
			}
		} else {
			if (acqServiceRate.getRateType() == 1) {
				acqServiceRate.setSingleAmount(new BigDecimal(acqServiceRate.getServiceRate()));
			} else {
				throw new RuntimeException("服务费率设置错误！");
			}
		}
	}

	public String profitExpression(AcqServiceRate rule) {
		if (rule == null)
			return "";
		String profitExp = null;
		switch (rule.getRateType().intValue()) {
		case 1:
			profitExp = rule.getSingleAmount() == null ? "" : double_format.format(rule.getSingleAmount());
			break;
		case 2:
			profitExp = rule.getRate() == null ? "" : format.format(rule.getRate()) + "%";
			break;
		case 3:
			profitExp = double_format.format(rule.getSafeLine()) + "~" + format.format(rule.getRate()) + "%~" + double_format.format(rule.getCapping());
			break;
		case 4:
			profitExp = format.format(rule.getRate()) + "%+" + double_format.format(rule.getSingleAmount());
			break;
		case 5:
		case 6:
			StringBuffer sb = new StringBuffer();
			sb.append(format.format(rule.getLadder1Rate())).append("%").append("<")
					.append(double_format.format(rule.getLadder1Max())).append("<")
					.append(format.format(rule.getLadder2Rate())).append("%");
			if (rule.getLadder2Max() != null) {
				sb.append("<").append(double_format.format(rule.getLadder2Max())).append("<")
						.append(format.format(rule.getLadder3Rate())).append("%");
				if (rule.getLadder3Max() != null) {
					sb.append("<").append(double_format.format(rule.getLadder3Max())).append("<")
							.append(format.format(rule.getLadder4Rate())).append("%");
					if (rule.getLadder4Max() != null) {
						sb.append("<").append(double_format.format(rule.getLadder4Max()));
					}
				}
			}
			profitExp = sb.toString();
		default:
			;
		}
		return profitExp;
	}

	@Override
	public int updateAcqServiceStatus(AcqService acq) {
		if(acq.getServiceStatus()==1){
			//关闭--》开启
			acq.setClosePrompt(null);
		}
		int num=groupServiceDao.updateAcqServiceStatus(acq);
		return num;
	}

	@Override
	public Map<String, Object> serviceDetail(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (id != null) {
			map.put("serviceBaseInfo", groupServiceDao.queryServiceById(id));
			List<AcqServiceRate> list = groupServiceDao.queryServiceRateByServiceId(id);
			if (list != null){
				for (AcqServiceRate acqServiceRate : list) {
					acqServiceRate.setServiceRate(this.profitExpression(acqServiceRate));
					acqServiceRate.setEffectiveStatus(new Date().compareTo(acqServiceRate.getEffectiveDate()) >=0?1:2);
				}
			}
			map.put("serviceRateInfos", list);
			map.put("serviceQuotaInfo", groupServiceDao.queryServiceTransRulesByServiceId(id));
		}
		return map;
	}

	@Override
	public List<AcqServiceRate> listEffectiveServiceRateByServiceId(Long id) {
		List<AcqServiceRate> dataList = new ArrayList<AcqServiceRate>();
		List<AcqServiceRate> list = groupServiceDao.queryServiceRateByServiceId(id);
		if (list != null) {
			for (AcqServiceRate acqServiceRate : list) {
				acqServiceRate.setServiceRate(this.profitExpression(acqServiceRate));
				// acqServiceRate.setEffectiveStatus(new Date().compareTo(acqServiceRate.getEffectiveDate()) >= 0 ? 1 : 2);
				if (acqServiceRate.getEffectiveStatus() == 1) {
					// 生效
					dataList.add(acqServiceRate);
				}
			}
		}
		return dataList;
	}

	@Override
	public List<AcqServiceRate> listServiceRateLogByRateIdAndCardType(Long rateId, Integer cardType) {
		List<AcqServiceRate> list = groupServiceDao.queryServiceRateByRateId(rateId, cardType);
		if (list != null) {
			for (AcqServiceRate acqServiceRate : list) {
				acqServiceRate.setServiceRate(this.profitExpression(acqServiceRate));
			}
		}
		return list;
	}

	@Override
	public int deleteServiceRateTask(Long id) {
		return groupServiceDao.deleteServiceRateTask(id);
	}

	@Override
	public int insertAcqServiceRateTask(AcqServiceRateTask acqServiceRateTask) {
		if (acqServiceRateTask != null) {
//			acqServiceRateTask.setEffectiveStatus(new Date().compareTo(acqServiceRateTask.getEffectiveDate()) >= 0 ? 1 : 2);
			acqServiceRateTask.setEffectiveStatus(2);
			this.setServiceRate(acqServiceRateTask);
			return groupServiceDao.insertAcqServiceRateTask(acqServiceRateTask);
		}
		return 0;
	}

	public void setServiceRate(AcqServiceRateTask serviceRateTask) {
		if (serviceRateTask.getRateType() == 5 || serviceRateTask.getRateType() == 6) {
			if (!serviceRateTask.getServiceRate().matches("^(\\d+(\\.\\d{0,3})?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d{0,3})?)%)+"))
				throw new RuntimeException("服务费率设置错误！");
			String[] strs = serviceRateTask.getServiceRate().split("<");
			serviceRateTask.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
			serviceRateTask.setLadder1Max(new BigDecimal(strs[1]));
			serviceRateTask.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
			if (strs.length > 3) {
				serviceRateTask.setLadder2Max(new BigDecimal(strs[3]));
				serviceRateTask.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
				if (serviceRateTask.getLadder2Max().compareTo(serviceRateTask.getLadder1Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if (strs.length > 5) {
				serviceRateTask.setLadder3Max(new BigDecimal(strs[5]));
				serviceRateTask.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
				if (serviceRateTask.getLadder3Max().compareTo(serviceRateTask.getLadder2Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if (strs.length > 7) {
				serviceRateTask.setLadder4Max(new BigDecimal(strs[7]));
				if (serviceRateTask.getLadder4Max().compareTo(serviceRateTask.getLadder3Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
		} else if (serviceRateTask.getRateType() == 4) {
			if (!serviceRateTask.getServiceRate().matches("^(\\d+(\\.\\d{0,3})?)%\\+(\\d+(\\.\\d+)?)"))
				throw new RuntimeException("服务费率设置错误！");
			String[] temp = serviceRateTask.getServiceRate().split("%+");
			serviceRateTask.setRate(new BigDecimal(temp[0]));
			serviceRateTask.setSingleAmount(new BigDecimal(temp[1]));
		} else if (serviceRateTask.getRateType() == 3) {
			Matcher m = pattern.matcher(serviceRateTask.getServiceRate());
			while (m.find()) {
				serviceRateTask.setSafeLine(new BigDecimal(m.group(1)));
				serviceRateTask.setRate(new BigDecimal(m.group(3)));
				serviceRateTask.setCapping(new BigDecimal(m.group(5)));
			}
			if(serviceRateTask.getSafeLine()==null||serviceRateTask.getCapping().compareTo(serviceRateTask.getSafeLine())<0)
				throw new RuntimeException("服务费率设置错误！");
		} else if (serviceRateTask.getRateType() == 2) {
			String str_ = serviceRateTask.getServiceRate().substring(0, serviceRateTask.getServiceRate().indexOf("%"));
			if (str_.matches("\\d+(\\.\\d{0,3})?")) {
				serviceRateTask.setRate(new BigDecimal(str_));
			} else {
				throw new RuntimeException("服务费率设置错误！");
			}
		} else {
			if (serviceRateTask.getRateType() == 1) {
				serviceRateTask.setSingleAmount(new BigDecimal(serviceRateTask.getServiceRate()));
			} else {
				throw new RuntimeException("服务费率设置错误！");
			}
		}
	}

	@Override
	public int updateAcqServiceTransRules(AcqServiceTransRules acqServiceTransRules) {
		return groupServiceDao.updateAcqServiceTransRulesByServiceId(acqServiceTransRules);
	}

	@Override
	public AcqServiceTransRules getAcqServiceTransRule(Long acqServiceId) {
		if (acqServiceId == null) {
			return null;
		}
		return groupServiceDao.queryServiceTransRuleByServiceId(acqServiceId);
	}

	@Override
	public int updateTimeSwitch(AcqService acq) {
		if (acq.getTimeSwitch() == 0) {
			acq.setTimeStartTime(null);
			acq.setTimeEndTime(null);
			acq.setClosePrompt(null);
			acq.setPeriodicityStartTime(null);
			acq.setPeriodicityEndTime(null);
		}
		return groupServiceDao.updateTimeSwitch(acq);
	}
	
	@Override
	public void setAcqServiceStatus(AcqService acqService) {
		groupServiceDao.updateServiceStatus(acqService);
	}

	@Override
	public Page<Map<String, Object>> queryServiceInfoList() {
		List<Map<String, Object>> list = groupServiceDao.findAllServiceInfo();
		Page<Map<String, Object>> page = new Page<>();
		page.setResult(list);
		page.setTotalCount(list.size());
		return page;
	}
	
}
