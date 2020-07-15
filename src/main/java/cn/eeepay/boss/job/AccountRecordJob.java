package cn.eeepay.boss.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.util.ClientInterface;

/**
 * 定时记账，交易成功且记账失败的
 * @author tans
 */
@DisallowConcurrentExecution
public class AccountRecordJob implements Job{

	private static final Logger log = LoggerFactory.getLogger(AccountRecordJob.class);

	@Resource
	private TransInfoService transInfoService;

	@Resource
	private SysDictService sysDictService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------  调用账户的接口，定时任务开始:--------------");
		int accountRecordDay = 1;
		SysDict sysDict = sysDictService.getByKey("ACCOUNT_RECORD_DAY");
		if(sysDict != null && StringUtils.isNotBlank(sysDict.getSysValue())){
			String sysValue = sysDict.getSysValue();
			if(StringUtils.isNumeric(sysValue)){
				accountRecordDay = Integer.parseInt(sysValue);
				if(accountRecordDay > 365){
					accountRecordDay = 365;
				}
				if(accountRecordDay < 0){
					accountRecordDay = 1;
				}
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date();
		Long nowTime = nowDate.getTime() - accountRecordDay*24*60*60*1000L;
		Date yesDate = new Date(nowTime);
		log.info("处理订单的创建时间条件： create_time >= {}", sdf.format(yesDate));
//		List<CollectiveTransOrder> transList = transInfoService.selectAllRecordAccountFail();
		List<CollectiveTransOrder> transList = transInfoService.selectRecordAccountFail(sdf.format(yesDate));

		Map<String, Object> map = new HashMap<String, Object>();
		for(CollectiveTransOrder info: transList){
			try {
				if("5".equals(info.getSettleStatus())&&"欢乐返首笔激活".equals(info.getRemark())){
					String msg=ClientInterface.hlfRecode(info);
					Map<String, Object> result = JSON.parseObject(msg);
					if(result!=null&&(Boolean)result.get("status"))
						info.setAccount("1");
						info.setTransMsg(String.valueOf(result.get("msg")));
						transInfoService.updateAccount(info);
					continue;
				}
//				ScanCodeTrans scanCodeTrans = transInfoService.getScanCodeTransByOrder(info.getOrderNo());
//				if(scanCodeTrans==null){
//					continue;
//				}
				if(info.getAgentNode()!=null){
					String[] agentArr = info.getAgentNode().split("-");
					info.setOneAgentNo(agentArr[1]);
					info.setAgentNo(agentArr[agentArr.length-1]);
				}
				String msg = ClientInterface.httpRecordAccount(info);
				System.out.println(msg);
				Map<String, Object> result = JSON.parseObject(msg);
				if(result!=null){
					boolean status=(Boolean)result.get("status");
					if(status){
						info.setAccount("1");
						transInfoService.updateAccount(info);
						//2.2.5 添加预冻结金额判断开始
						map = transInfoService.judgePreFreezeaMountAngFreezaTrans(info);
						System.out.println("预冻结返回结果： " + map.toString());
						//2.2.5 添加预冻结金额判断结束
					} else {
						info.setAccount("2");
					}
				}
			} catch (Exception e) {
				System.out.println("我们的订单号：" + info.getOrderNo()+ ";调用记账接口异常");
				e.printStackTrace();
			}
			
		}
		log.info("--------------  调用账户的接口，定时任务结束--------------");

	}
}
