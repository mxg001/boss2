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

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.ys.YsClient;
import cn.eeepay.framework.util.zf.ZfCilent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AcqOrgDao;
import cn.eeepay.framework.dao.OutAccountServiceDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.OutAccountServiceService;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

/**
 * 出款服务service实现
 * 
 * @author junhu
 *
 */
@Service
@Transactional
public class OutAccountServiceServiceImpl implements OutAccountServiceService {

	private final Logger log = LoggerFactory.getLogger(OutAccountServiceServiceImpl.class);

	private static final Pattern pattern = Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)$");

	private static final DecimalFormat format = new java.text.DecimalFormat("0.00");

	@Resource
	private OutAccountServiceDao outAccountServiceDao;
	
	@Resource
	private AcqOrgDao acqOrgDao;

	@Resource
    private SysDictDao sysDictDao;

	@Override
	public int saveOutAccountServiceFunction(OutAccountServiceFunction function) {
		if (function.getId() == null) {
			outAccountServiceDao.insertOutAccountServiceFunction(function);
		} else {
			outAccountServiceDao.updateOutAccountServiceFunction(function);
		}
		return function.getId();
	}

	@Override
	public OutAccountServiceFunction queryOutAccountServiceFunction() {
		return outAccountServiceDao.queryOutAccountServiceFunction();
	}

	@Override
	public int insertOutAccountService(OutAccountService service, List<OutAccountServiceRate> serviceRates) {
		if (service != null) {
			service.setOutAccountStatus(1);
			AcqOrg acq = acqOrgDao.selectByPrimaryKey(service.getAcqOrgId());
			if(acq!= null && StringUtils.isNoneBlank(acq.getAcqEnname())){
				service.setAcqEnname(acq.getAcqEnname());
			}
			outAccountServiceDao.insertOutAccountService(service);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (serviceRates != null) {
				for (OutAccountServiceRate serviceRate : serviceRates) {
					serviceRate.setOutAccountServiceId(service.getId());
					serviceRate.setEffectiveDate(new Date());
					serviceRate.setEffectiveStatus(1);
					serviceRate.setCreateTime(new Date());
					serviceRate.setCreatePerson(principal.getId().toString());
					if (serviceRate.getAgentRateType() != null) {
						if(serviceRate.getAgentRateType()==5){
							outAccountServiceDao.insertOutAccountServiceRate(serviceRate);
							continue;
						}
						this.setAgentServiceRate(serviceRate);
					}
					if (serviceRate.getCostRateType() != null) {
						if(serviceRate.getCostRateType()==5){
							outAccountServiceDao.insertOutAccountServiceRate(serviceRate);
							continue;
						}
						this.setCostServiceRate(serviceRate);
					}
					outAccountServiceDao.insertOutAccountServiceRate(serviceRate);
				}
			}
		}
		return service==null?-1:service.getId();
	}

	public void setAgentServiceRate(OutAccountServiceRate serviceRate) {
		if (serviceRate.getAgentRateType() == 5 || serviceRate.getAgentRateType() == 6) {
			if (!serviceRate.getServiceRate().matches("^(\\d+(\\.\\d+)?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d+)?)%)+"))
				throw new RuntimeException("服务费率设置错误！");
			String[] strs = serviceRate.getServiceRate().split("<");
			serviceRate.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
			serviceRate.setLadder1Max(new BigDecimal(strs[1]));
			serviceRate.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
			if (strs.length > 3) {
				serviceRate.setLadder2Max(new BigDecimal(strs[3]));
				serviceRate.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
				if (serviceRate.getLadder2Max().compareTo(serviceRate.getLadder1Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if (strs.length > 5) {
				serviceRate.setLadder3Max(new BigDecimal(strs[5]));
				serviceRate.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
				if (serviceRate.getLadder3Max().compareTo(serviceRate.getLadder2Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if (strs.length > 7) {
				serviceRate.setLadder4Max(new BigDecimal(strs[7]));
				if (serviceRate.getLadder4Max().compareTo(serviceRate.getLadder3Max()) <= 0)
					throw new RuntimeException("服务费率设置错误！");
			}
		} else if (serviceRate.getAgentRateType() == 4) {
			if (!serviceRate.getServiceRate().matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
				throw new RuntimeException("服务费率设置错误！");
			String[] temp = serviceRate.getServiceRate().split("%+");
			serviceRate.setRate(new BigDecimal(temp[0]));
			serviceRate.setSingleAmount(new BigDecimal(temp[1]));
		} else if (serviceRate.getAgentRateType() == 3) {
			Matcher m = pattern.matcher(serviceRate.getServiceRate());
			while (m.find()) {
				serviceRate.setSafeLine(new BigDecimal(m.group(1)));
				serviceRate.setRate(new BigDecimal(m.group(3)));
				serviceRate.setCapping(new BigDecimal(m.group(5)));
			}
			if(serviceRate.getSafeLine()==null||serviceRate.getCapping().compareTo(serviceRate.getSafeLine())<0)
				throw new RuntimeException("服务费率设置错误！");
		} else if (serviceRate.getAgentRateType() == 2) {
			String str_ = serviceRate.getServiceRate().substring(0, serviceRate.getServiceRate().indexOf("%"));
			if (str_.matches("\\d+(\\.\\d+)?")) {
				serviceRate.setRate(new BigDecimal(str_));
			} else {
				throw new RuntimeException("服务费率设置错误！");
			}
		} else {
			if (serviceRate.getAgentRateType() == 1) {
				serviceRate.setSingleAmount(new BigDecimal(serviceRate.getServiceRate()));
			} else {
				throw new RuntimeException("服务费率设置错误！");
			}
		}
	}

	public void setCostServiceRate(OutAccountServiceRate serviceRate) {
		if (serviceRate.getCostRateType() == 4) {
			if (!serviceRate.getServiceRate().matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
				throw new RuntimeException("服务费率设置错误！");
			String[] temp = serviceRate.getServiceRate().split("%+");
			serviceRate.setRate(new BigDecimal(temp[0]));
			serviceRate.setSingleAmount(new BigDecimal(temp[1]));
		} else if (serviceRate.getCostRateType() == 3) {
			Matcher m = pattern.matcher(serviceRate.getServiceRate());
			while (m.find()) {
				serviceRate.setSafeLine(new BigDecimal(m.group(1)));
				serviceRate.setRate(new BigDecimal(m.group(3)));
				serviceRate.setCapping(new BigDecimal(m.group(5)));
			}
			if(serviceRate.getSafeLine()==null||serviceRate.getCapping().compareTo(serviceRate.getSafeLine())<0)
				throw new RuntimeException("服务费率设置错误！");
		} else if (serviceRate.getCostRateType() == 2 || serviceRate.getCostRateType() == 5) {
			String str_ = serviceRate.getServiceRate().substring(0, serviceRate.getServiceRate().indexOf("%"));
			if (str_.matches("\\d+(\\.\\d+)?")) {
				serviceRate.setRate(new BigDecimal(str_));
			} else {
				throw new RuntimeException("服务费率设置错误！");
			}
		} else {
			if (serviceRate.getCostRateType() == 1) {
				serviceRate.setSingleAmount(new BigDecimal(serviceRate.getServiceRate()));
			} else {
				throw new RuntimeException("服务费率设置错误！");
			}
		}
	}

	public String getAgentServiceRate(OutAccountServiceRate rule) {
		if (rule == null)
			return "";
		String profitExp = null;
		switch (rule.getAgentRateType().intValue()) {
		case 1:
			profitExp = format.format(rule.getSingleAmount()) == null ? "" : format.format(rule.getSingleAmount());
			break;
		case 2:
			profitExp = format.format(rule.getRate()) == null ? "" : format.format(rule.getRate())+"%";
			break;
		case 3:
			profitExp = format.format(rule.getSafeLine()) + "~" + format.format(rule.getRate()) + "%~" + format.format(rule.getCapping());
			break;
		case 4:
			profitExp = format.format(rule.getRate()) + "%+" + format.format(rule.getSingleAmount());
			break;
		case 5:
			StringBuffer sb = new StringBuffer();
			sb.append(format.format(rule.getLadder1SafeLine())).append("元~")
			        .append(format.format(rule.getLadder1Rate())).append("%").append("<=")
					.append(format.format(rule.getLadder1Max())).append("万元<")
					.append(format.format(rule.getLadder2SafeLine())).append("元~")
					.append(format.format(rule.getLadder2Rate())).append("%");
			if (rule.getLadder2Max() != null) {
				sb.append("<=").append(format.format(rule.getLadder2Max())).append("万元<")
						.append(format.format(rule.getLadder3SafeLine())).append("元~")
						.append(format.format(rule.getLadder3Rate())).append("%");
				if (rule.getLadder3Max() != null) {
					sb.append("<=").append(format.format(rule.getLadder3Max())).append("万元<")
							.append(format.format(rule.getLadder4SafeLine())).append("元~")
							.append(format.format(rule.getLadder4Rate())).append("%");
					if (rule.getLadder4Max() != null) {
						sb.append("<=").append(format.format(rule.getLadder4Max())).append("万元");
					}
				}
			}
			profitExp = sb.toString();
			break;
		default:
			;
		}
		return profitExp;
	}

	public String getCostServiceRate(OutAccountServiceRate rule) {
		if (rule == null)
			return "";
		String profitExp = null;
		switch (rule.getCostRateType().intValue()) {
		case 1:
			profitExp = format.format(rule.getSingleAmount()) == null ? "" : format.format(rule.getSingleAmount());
			break;
		case 2:
			profitExp = format.format(rule.getRate()) == null ? "" : format.format(rule.getRate()) + "%";
			break;
		case 3:
			profitExp = format.format(rule.getSafeLine()) + "~" + format.format(rule.getRate()) + "%~" + format.format(rule.getCapping());
			break;
		case 4:
			profitExp = format.format(rule.getRate()) + "%+" + format.format(rule.getSingleAmount());
			break;
		case 5:
			StringBuffer sb = new StringBuffer();
			sb.append(format.format(rule.getLadder1SafeLine())).append("元~")
			        .append(format.format(rule.getLadder1Rate())).append("%").append("<=")
					.append(format.format(rule.getLadder1Max())).append("万元<")
					.append(format.format(rule.getLadder2SafeLine())).append("元~")
					.append(format.format(rule.getLadder2Rate())).append("%");
			if (rule.getLadder2Max() != null) {
				sb.append("<=").append(format.format(rule.getLadder2Max())).append("万元<")
						.append(format.format(rule.getLadder3SafeLine())).append("元~")
						.append(format.format(rule.getLadder3Rate())).append("%");
				if (rule.getLadder3Max() != null) {
					sb.append("<=").append(format.format(rule.getLadder3Max())).append("万元<")
							.append(format.format(rule.getLadder4SafeLine())).append("元~")
							.append(format.format(rule.getLadder4Rate())).append("%");
					if (rule.getLadder4Max() != null) {
						sb.append("<=").append(format.format(rule.getLadder4Max())).append("万元");
					}
				}
			}
			profitExp = sb.toString();
			break;
		default:
			;
		}
		return profitExp;
	}

	@Override
	public List<OutAccountService> queryOutAccountService(Map<String, Object> param, Page<OutAccountService> page) {

		return outAccountServiceDao.queryOutAccountService(param, page);
	}

	@Override
	public int updateOutAccountServiceStatus(Integer id, Integer outAccountStatus) {

		return outAccountServiceDao.updateOutAccountServiceStatus(id, outAccountStatus);
	}

	@Override
	public Map<String, Object> getOutAccountServiceDetail(Integer serviceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("serviceId", serviceId);
		OutAccountService outAccountService = outAccountServiceDao.queryOutAccountServiceById(param);
		List<OutAccountServiceRate> rates = outAccountServiceDao.queryOutAccountServiceRate(serviceId);
		List<OutAccountServiceRate> agentServiceRateList = new ArrayList<OutAccountServiceRate>();
		List<OutAccountServiceRate> costServiceRateList = new ArrayList<OutAccountServiceRate>();
		if (rates != null) {
			for (OutAccountServiceRate outAccountServiceRate : rates) {
				if (outAccountServiceRate.getAgentRateType() != null) {
					outAccountServiceRate.setServiceRate(this.getAgentServiceRate(outAccountServiceRate));
					agentServiceRateList.add(outAccountServiceRate);
				}
				if (outAccountServiceRate.getCostRateType() != null) {
					outAccountServiceRate.setServiceRate(this.getCostServiceRate(outAccountServiceRate));
					costServiceRateList.add(outAccountServiceRate);
				}
			}
		}
		map.put("serviceBaseInfo", outAccountService);
		map.put("agentServiceRateList", agentServiceRateList);
		map.put("costServiceRateList", costServiceRateList);
		return map;
	}

	@Override
	public List<OutAccountServiceRateTask> queryOutAccountServiceRateLog(Integer serviceRateId) {
		List<OutAccountServiceRateTask> rateTasks = outAccountServiceDao.queryOutAccountServiceRateLog(serviceRateId);
		for (OutAccountServiceRateTask rateTask : rateTasks) {
			OutAccountServiceRate serviceRate = new OutAccountServiceRate();
			BeanUtils.copyProperties(rateTask, serviceRate);
			if (rateTask.getAgentRateType() != null) {
				rateTask.setServiceRate(this.getAgentServiceRate(serviceRate));
			}
			if (rateTask.getCostRateType() != null) {
				rateTask.setServiceRate(this.getCostServiceRate(serviceRate));
			}
		}
		return rateTasks;
	}

	@Override
	public int updateOutAccountService(OutAccountService service) {

		return outAccountServiceDao.updateOutAccountService(service);
	}

	@Override
	public int insertOutAccountServiceRateTask(OutAccountServiceRateTask serviceRateTask) {
		int i=0;
		if (serviceRateTask != null) {
			serviceRateTask.setEffectiveStatus(2);
			serviceRateTask.setCreateTime(new Date());
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			serviceRateTask.setCreatePerson(principal.getId().toString());
			
			OutAccountServiceRate serviceRate = new OutAccountServiceRate();
			BeanUtils.copyProperties(serviceRateTask, serviceRate);
			if (serviceRateTask.getAgentRateType() != null) {
				if(serviceRateTask.getAgentRateType()==5){
					i=outAccountServiceDao.insertOutAccountServiceRateTask(serviceRateTask);
					BeanUtils.copyProperties(serviceRate, serviceRateTask);
					return i;
				}else{
					this.setAgentServiceRate(serviceRate);
				}
				
			}
			if (serviceRateTask.getCostRateType() != null) {
				if(serviceRateTask.getCostRateType()==5){
					i=outAccountServiceDao.insertOutAccountServiceRateTask(serviceRateTask);
					BeanUtils.copyProperties(serviceRate, serviceRateTask);
					return i;
				}else{
					this.setCostServiceRate(serviceRate);
				}
			}
			BeanUtils.copyProperties(serviceRate, serviceRateTask);
			i = outAccountServiceDao.insertOutAccountServiceRateTask(serviceRateTask);
		}
		return i;
	}

	@Override
	public int deleteOutAccountServiceRateTask(Integer id) {

		return outAccountServiceDao.deleteOutAccountServiceRateTask(id);
	}

	@Override
	public OutAccountServiceRate getById(Integer id) {
		return outAccountServiceDao.getRateById(id);
	}

	@Override
	public List<OutAccountServiceRateTask> findByEffective() {
		return outAccountServiceDao.findByEffective();
	}

	@Override
	public int updateByTaskBatch(List<OutAccountServiceRateTask> taskList) {
		return outAccountServiceDao.updateRateByTaskBatch(taskList);
	}

	@Override
	public int updateByRateBatch(List<OutAccountServiceRate> rateList,
			List<Integer> taskIdList) {
		return outAccountServiceDao.updateTaskByRateBatch(rateList, taskIdList);
	}

	@Override
	public int updateResetDayTotalAmount(Integer acq_org_id) {
		return outAccountServiceDao.updateResetDayTotalAmount(acq_org_id);
	}

	@Override
	public List<OutAccountService> queryBoxAllInfo() {
		return outAccountServiceDao.queryBoxAllInfo();
	}

	@Override
	public OutAccountService getOutAccountServiceById(Integer serviceId) {
		return outAccountServiceDao.getOutAccountServiceById(serviceId);
	}

	@Override
	public List<OutAccountService> queryOutAccountServiceNoPage() {
		return outAccountServiceDao.queryOutAccountServiceNoPage();
	}

	//更新上游余额
	@Override
	public void updateUserBalance() {

		//1.找到需要更新上游余额的出款服务
		//条件1：出款服务类型为8,10,11,12
		//条件2：平台用户编号不为空
		List<OutAccountService> serviceList = outAccountServiceDao.selectNeedUpdateBalance();
		int totalNum = 0;
		int successNum = 0;
		if(serviceList == null || serviceList.size() < 1) {
			log.error("找不到需要更新上游余额的出款服务");
			return;
		}
		totalNum = serviceList.size();
		//2.调用上游接口，若成功，更新对应出款服务余额
		//2.1如果acqEnname=neweptok，调用银盛接口
		//2.1如果acqEnname=ZF_DF，调用中付接口
		String ysAcqEnname = "neweptok";
		String zfAcqEnname = "ZF_DF";
		List<OutAccountService> needUpdateList = new ArrayList<>();
		for(OutAccountService service: serviceList){
			try {
				Result result = new Result();
				if(service.getAcqEnname().equals(ysAcqEnname)){
					result = YsClient.pureAcctInfo(service.getUserCode());
				} else if(service.getAcqEnname().equals(zfAcqEnname)){
					result = ZfCilent.pureAcctInfo(service.getUserCode());
				}
				if(result != null && result.isStatus() && result.getData() != null){
					service.setUserBalance(new BigDecimal(String.valueOf(result.getData())));
					needUpdateList.add(service);
				} else if(result != null && !result.isStatus()){
                    log.error("更新出款服务上游余额异常,msg:{}", result.getMsg());
                }
			} catch (Exception e){
				log.error("更新出款服务上游余额异常", e);
			}
		}
		//更新出款服务表余额
		for(OutAccountService service: needUpdateList){
			int num = outAccountServiceDao.updateBalance(service);
			if(num == 1){
				successNum++;
			}
		}
		//3.打印本次更新结果
		log.info("更新出款服务上游余额完成,总共需要更新:" + totalNum + "条,成功:"
				+ successNum + "条,失败:" + (totalNum - successNum) +"条.");
		return;
	}

    @Override
    public Result selectAgentWithdraw() {
	    Result result = new Result();
	    Map<String, Object> map = new HashMap<>();
	    SysDict agentWithdrawSwitch = sysDictDao.getByKey("AGENT_WITHDRAW_SWITCH");
	    if(agentWithdrawSwitch != null && agentWithdrawSwitch.getSysValue() != null){
            map.put("agentWithdrawSwitch", Integer.valueOf(agentWithdrawSwitch.getSysValue().trim()));
        }
	    SysDict agentWithdrawBalance = sysDictDao.getByKey("AGENT_WITHDRAW_BALANCE");
        if(agentWithdrawBalance != null && agentWithdrawBalance.getSysValue() != null){
            map.put("agentWithdrawBalance", agentWithdrawBalance.getSysValue().trim());
        }
        result.setStatus(true);
        result.setMsg("查询成功");
        result.setData(map);
        return result;
    }

    @Override
    public Result saveAgentWithdraw(Map<String, Object> map) {
	    Result result = new Result();
	    if(map.get("agentWithdrawSwitch") == null ||
                map.get("agentWithdrawBalance") == null){
            result.setMsg("参数非法");
            return result;
        }
	    String agentWithdrawSwitchStr = String.valueOf(map.get("agentWithdrawSwitch")).trim();
	    if(agentWithdrawSwitchStr.equals("1") || agentWithdrawSwitchStr.equals("true") ){
			agentWithdrawSwitchStr = "1";
        } else {
			agentWithdrawSwitchStr = "0";
		}
        String agentWithdrawBalanceStr = String.valueOf(map.get("agentWithdrawBalance")).trim();
        Pattern pattern = Pattern.compile("^(0|[1-9]\\d+)|(0|[1-9]\\d+).(\\d){1,2}$");
        Matcher mather = pattern.matcher(agentWithdrawBalanceStr);
        if(!mather.matches()){
            result.setMsg("上游账户剩余额度输入格式有误,请输入只有两位小数的正数");
            return result;
        }
        SysDict agentWithdrawSwitch = new SysDict();
        agentWithdrawSwitch.setSysKey("AGENT_WITHDRAW_SWITCH");
        agentWithdrawSwitch.setSysValue(agentWithdrawSwitchStr);
        int num = sysDictDao.updateSysValue(agentWithdrawSwitch);
        if(num != 1){
            throw new BossBaseException("更新代理商提现开关失败");
        }

        SysDict agentWithdrawBalance = new SysDict();
        agentWithdrawBalance.setSysKey("AGENT_WITHDRAW_BALANCE");
        agentWithdrawBalance.setSysValue(agentWithdrawBalanceStr);
        num = sysDictDao.updateSysValue(agentWithdrawBalance);
        if(num != 1){
            throw new BossBaseException("更新代理商提现剩余额度失败");
        }
        result.setStatus(true);
        result.setMsg("操作成功");
        return result;
    }

	@Override
	public OutAccountService findServiceId(Integer serviceId) {
		return outAccountServiceDao.findServiceId(serviceId);
	}

}
