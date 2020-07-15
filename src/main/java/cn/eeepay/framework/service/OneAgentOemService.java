package cn.eeepay.framework.service;


import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/5/10/010.
 * @author  liuks
 * OEM下的一级代理关系表维护
 */
public interface OneAgentOemService {

    int insertOneAgentNo(String oneAgentNo,String oemType);

    int insertOneAgentShare(String agentNo, BigDecimal share, BigDecimal fee,BigDecimal receiveShare,BigDecimal repaymentShare);
}
