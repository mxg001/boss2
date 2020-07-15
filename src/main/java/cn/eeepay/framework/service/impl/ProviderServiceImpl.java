package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.eeepay.framework.dao.ProviderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;
import cn.eeepay.framework.model.RepayOemServiceCostBean;
import cn.eeepay.framework.model.RepayProfitDetailBean;
import cn.eeepay.framework.service.ProviderService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.ClientInterface;

/**
 * Created by 666666 on 2017/10/27.
 */
@Service
public class ProviderServiceImpl implements ProviderService {

	private Logger log = LoggerFactory.getLogger(ProviderServiceImpl.class);

    @Resource
    private ProviderDao providerDao;

    @Override
    public List<ProviderBean> listProvider(ProviderBean providerBean, Page<AgentInfo> page) {
        return providerDao.listProvider(providerBean, page);
    }

    @Override
    public boolean openSuperRepayment(List<String> agentNoList) {
        if (CollectionUtils.isEmpty(agentNoList)){
            throw new BossBaseException("请选择需要开通的服务商.");
        }
        int count = providerDao.chechAgentNoIsLevelOne(agentNoList);
        if (count != agentNoList.size()){
            throw new BossBaseException("选中服务商已经开通此功能或不是一级代理商.");
        }
        RepayOemServiceCostBean defaultOemServiceCost = providerDao.queryRepayOemServiceCost("default");
        if (defaultOemServiceCost == null){
            throw new BossBaseException("请配置默认的交易服务费率.");
        }
        List<RepayOemServiceCostBean> wantAddAgent = new ArrayList<>();
        for (String agentNo : agentNoList){
            RepayOemServiceCostBean oemServiceCost = providerDao.queryRepayOemServiceCost(agentNo);
			if (oemServiceCost == null) {
				wantAddAgent.add(new RepayOemServiceCostBean(agentNo, defaultOemServiceCost.getRate(),
						defaultOemServiceCost.getSingleAmount(), defaultOemServiceCost.getFullRepayRate(),
						defaultOemServiceCost.getFullRepaySingleAmount(), defaultOemServiceCost.getPerfectRate(),
						defaultOemServiceCost.getPerfectSingleAmount()));
			} else {
				wantAddAgent.add(oemServiceCost);
			}
            try {
            	ClientInterface.createAccountByAcc(agentNo, "224114");
			} catch (Exception e) {
				log.error("超级还-服务商: " + agentNo + ", 开户失败", e);
			}
        }
        providerDao.openSuperRepayment(wantAddAgent);
        //根据代理商编号插入oem信息表
        for (RepayOemServiceCostBean agentinfo : wantAddAgent) {
            try {
                providerDao.insertoneAgentNo(agentinfo.getAgentNo());
            }catch (Exception e){
                log.info("超级还-服务商: " + agentinfo.getAgentNo() + ", 添加agent_oem_info表信息失败", e);
            }
        }
        return true;
    }

	@Override
	public boolean updateServiceCost(ProviderBean bean) {
		RepayOemServiceCostBean oemServiceCost = providerDao.queryRepayOemServiceCost(bean.getAgentNo());
		if (oemServiceCost == null) {
			oemServiceCost = providerDao.queryRepayOemServiceCost("default");
			if (oemServiceCost == null) {
				throw new BossBaseException("请配置默认的交易服务费率.");
			}
		}
		// 校验成本是否大于费率-start
		if (bean.getRate().compareTo(oemServiceCost.getRate()) > 0
				|| bean.getSingleAmount().compareTo(oemServiceCost.getSingleAmount()) > 0)
			throw new BossBaseException("修改后的成本不能大于服务商费率");
		if (bean.getFullRepayRate().compareTo(oemServiceCost.getFullRepayRate()) > 0
				|| bean.getFullRepaySingleAmount().compareTo(oemServiceCost.getFullRepaySingleAmount()) > 0)
			throw new BossBaseException("修改后的全额还款成本不能大于服务商全额还款费率");
		if (bean.getPerfectRepayRate().compareTo(oemServiceCost.getPerfectRate()) > 0
				|| bean.getPerfectRepaySingleAmount().compareTo(oemServiceCost.getPerfectSingleAmount()) > 0)
			throw new BossBaseException("修改后的完美还款成本不能大于服务商完美还款费率");
		// 校验成本是否大于费率-end

		// 校验是否大于下级成本-start
		ProviderBean serviceMinCost = providerDao.queryServiceMinCost(bean.getAgentNo());
		if (serviceMinCost != null) { //没有下级成本则不校验
			//在校验前做非空判断
			if ((serviceMinCost.getRate() != null && bean.getRate().compareTo(serviceMinCost.getRate()) > 0) ||
					(serviceMinCost.getSingleAmount() != null && bean.getSingleAmount().compareTo(serviceMinCost.getSingleAmount()) > 0)) {
				throw new BossBaseException("修改后的成本不能大于下级代理商成本");
			}
			if ((serviceMinCost.getFullRepayRate() != null && bean.getFullRepayRate().compareTo(serviceMinCost.getFullRepayRate()) > 0) ||
					(serviceMinCost.getFullRepaySingleAmount() != null && bean.getFullRepaySingleAmount().compareTo(serviceMinCost.getFullRepaySingleAmount()) > 0)) {
				throw new BossBaseException("修改后的全额还款成本不能大于下级代理商全额还款成本");
			}
			if ((serviceMinCost.getPerfectRepayRate() != null && bean.getPerfectRepayRate().compareTo(serviceMinCost.getPerfectRepayRate()) > 0) ||
					(serviceMinCost.getPerfectRepaySingleAmount() != null && bean.getPerfectRepaySingleAmount().compareTo(serviceMinCost.getPerfectRepaySingleAmount()) > 0)) {
				throw new BossBaseException("修改后的完美还款成本不能大于下级代理商完美还款成本");
			}
		}
		// 校验是否大于下级成本-end
		return providerDao.updateServiceCost(bean) == 1;
	}

	@Override
	public List<RepayProfitDetailBean> listRepayProfitDetail(RepayProfitDetailBean bean,
			Page<RepayProfitDetailBean> page) {
		return providerDao.listRepayProfitDetail(bean, page);
	}

	@Override
	public RepayProfitDetailBean sumRepayProfitDetail(RepayProfitDetailBean bean) {
		return providerDao.sumRepayProfitDetail(bean);
	}

	@Override
	public List<RepayProfitDetailBean> exportRepayProfitDetail(RepayProfitDetailBean bean) {
		return providerDao.exportRepayProfitDetail(bean);
	}

	@Override
	public String queryAgentNode(String profitMerNo) {
		return providerDao.queryAgentNode(profitMerNo);
	}

}
