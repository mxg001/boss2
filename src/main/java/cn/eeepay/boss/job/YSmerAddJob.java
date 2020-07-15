package cn.eeepay.boss.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jpos.util.Log;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.ZqMerchantInfo;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.ZqMerchantInfoService;
import cn.eeepay.framework.service.impl.RegularTasks;
import cn.eeepay.framework.util.ClientInterface;

/** 
*YS同步商户定时任务
* @version 创建时间：2017年9月15日 下午2:52:50
*/
@DisallowConcurrentExecution
public class YSmerAddJob  implements Job{

	@Resource
	private ZqMerchantInfoService zqMerchantInfoService;
	
	@Resource
	private SysDictService sysDictService;
	private final Logger log = LoggerFactory.getLogger(YSmerAddJob.class);

	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
        /*log.info("=============YS同步商户定时任务开始=========");
		String paramStr;  
        String merchantNo = null;
		SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
		String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
		SysDict type = sysDictService.getByKey("YS_MERADD_TIME");
		List<ZqMerchantInfo> zqMerList = zqMerchantInfoService.selectYsSyncMer(String.valueOf(type.getSysValue()));
		if(zqMerList != null && zqMerList.size()>0){
            for (ZqMerchantInfo zqMerInfo : zqMerList) {
            	try{
            		 merchantNo = zqMerInfo.getMerchantNo();
                     String bpId = zqMerInfo.getBpId().toString();
                     Map<String, Object> marMap = new HashMap<String, Object>();
                     List<String> channelList = new ArrayList<>();
                     channelList.add("YS_ZQ");
                     marMap.put("merchantNo", merchantNo);
                     marMap.put("bpId", bpId);
                     marMap.put("operator", merchantNo);
                     marMap.put("changeSettleCard", "0");
                     marMap.put("channelCode", channelList);
                     paramStr = JSON.toJSONString(marMap);
                     new ClientInterface(accessUrl, null).postRequestBody(paramStr);
            	}catch(Exception e){
					log.error("YS商户定时任务同步异常,商户号:{}",new Object[]{merchantNo});
            	}
                          
            }
		}*/
	}
	

}
