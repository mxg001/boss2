package cn.eeepay.boss.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.eeepay.framework.util.ClientInterface;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

/**
 * 超级银行家
 * 生成彩票主订单并分润
 * @author dxy
 * @data 2018-05-30
 */
public class LotteryGenOrderAndProfitRunnable implements Runnable{

	private String sysDict;//订单号

	public LotteryGenOrderAndProfitRunnable(String sysDict) {
		super();
		this.sysDict = sysDict;
	}
	
	@Override
	public void run(){
		if(StringUtils.isBlank(sysDict)){
			return;
		}
//		调用彩票主订单和分润接口
		String genUrl = sysDict+"/superbank-mobile/order/lotteryOrderAndProfit";
	    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
	    String batchNo =  sf.format(new Date());
	    System.out.println("异步生成彩票主订单和分润接口 url:"+genUrl);
	    ClientInterface.lotteryGenOrderAndProfit(batchNo, genUrl);
	    System.out.println("异步生成彩票主订单和分润接口结束");
	}

}
