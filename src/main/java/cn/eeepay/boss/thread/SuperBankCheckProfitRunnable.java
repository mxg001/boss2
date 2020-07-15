package cn.eeepay.boss.thread;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.util.ClientInterface;
import org.jpos.q2.cli.SLEEP;

/**
 * 超级银行家
 * 根据根据批次号，计算订单的分润
 * @author tans
 * @data 2017-12-14
 */
public class SuperBankCheckProfitRunnable implements Runnable{
	
	private String batchNo;//订单号
	private SuperBankService superBankService;

	public SuperBankCheckProfitRunnable(String batchNo, SuperBankService superBankService) {
		super();
		this.batchNo = batchNo;
		this.superBankService = superBankService;
	}
	
	@Override
	public void run(){
		if(StringUtils.isBlank(batchNo)){
			return;
		}
		//计算分润状态置为未计算（入账状态给接口维护）
//		superBankService.updateProfitStatus(batchNo, "0");
//		调用分润计算接口
		ClientInterface.checkOrderProfit(batchNo);
//		JSONObject json = JSONObject.parseObject(checkStr);
//		String checkStatus = json.getString("code");
//		if(!"success".equals(checkStatus)){
//			return;
//		}
		//如果计算成功，进行分润入账
//		superBankService.profitAccount(batchNo);
	}

}
