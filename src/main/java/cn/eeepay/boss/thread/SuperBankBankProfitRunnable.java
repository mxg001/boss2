package cn.eeepay.boss.thread;

import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.util.ClientInterface;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

/**
 * 超级银行家
 * 根据根据批次号，计算办卡的分润
 * @author tans
 * @data 2017-12-14
 */
public class SuperBankBankProfitRunnable implements Runnable{

	private String batchNo;//订单号
	private String bonusType;//类型

	public SuperBankBankProfitRunnable(String batchNo, String bonusType) {
		super();
		this.batchNo = batchNo;
		this.bonusType = bonusType;
	}
	
	@Override
	public void run(){
		if(StringUtils.isBlank(batchNo)){
			return;
		}
//		调用分润计算接口
		ClientInterface.checkBankProfit(batchNo, bonusType);
	}

}
