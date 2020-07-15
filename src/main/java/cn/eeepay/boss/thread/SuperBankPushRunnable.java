package cn.eeepay.boss.thread;

import org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.util.ClientInterface;

/**
 * 超级银行家
 * 根据订单号调用银行家后台进行推送
 * @author tans
 *
 */
public class SuperBankPushRunnable implements Runnable{
	
	private String orderNo;//订单号

	public SuperBankPushRunnable(String orderNo) {
		super();
		this.orderNo = orderNo;
	}
	
	@Override
	public void run(){
		if(StringUtils.isBlank(orderNo)){
			return;
		}
		ClientInterface.superBankPush(orderNo);
	}
	

}
