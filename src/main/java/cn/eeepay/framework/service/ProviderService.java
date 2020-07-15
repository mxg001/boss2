package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;
import cn.eeepay.framework.model.RepayProfitDetailBean;

import java.util.List;

/**
 * Created by 666666 on 2017/10/27.
 */
public interface ProviderService {
    /**
     * 查询服务商信息(一级代理商)
     * @param providerBean 查询条件
     * @param page 分页条件
     * @return
     */
    List<ProviderBean> listProvider(ProviderBean providerBean, Page<AgentInfo> page);

    /**
     * 开通信用卡超级还款功能
     * @param agentNoList
     * @return
     */
    boolean openSuperRepayment(List<String> agentNoList);

    /**
     * 修改服务成本
     * @param bean
     * @return
     */
	boolean updateServiceCost(ProviderBean bean);

	/**
	 * 超级还款分润
	 * @author	mays
	 * @date	2017年11月21日
	 */
	List<RepayProfitDetailBean> listRepayProfitDetail(RepayProfitDetailBean bean, Page<RepayProfitDetailBean> page);

	/**
	 * 汇总超级还款分润
	 */
	RepayProfitDetailBean sumRepayProfitDetail(RepayProfitDetailBean bean);

	/**
	 * 导出超级还款分润
	 */
	List<RepayProfitDetailBean> exportRepayProfitDetail(RepayProfitDetailBean bean);

	/**
	 * 根据agentNo查询agentNode
	 * @author	mays
	 * @date	2018年1月16日
	 */
	String queryAgentNode(String profitMerNo);

}
