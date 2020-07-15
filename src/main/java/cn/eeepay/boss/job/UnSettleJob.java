package cn.eeepay.boss.job;
/**
 * 未出款/出款失败 重新发起出款
 *
 */

import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class UnSettleJob implements Job {

	private final Logger log = LoggerFactory.getLogger(UnSettleJob.class);
	@Resource
	private TransInfoService transInfoService;
	@Resource
	private RedisService redisService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		/*try {
			log.info("未出款/出款失败 重新发起出款");
			String last=(String)redisService.select("RE_SETTLE_TASK_LAST");
			String count=(String)redisService.select("RE_SETTLE_TASK_COUNT");
			String channelNames = (String) redisService.select("RE_SETTLE_TASK_CHANNELS");
			if(!StringUtils.isNumeric(last)){
				last="900";
			}
			if(!StringUtils.isNumeric(count)){
				count="6000";
			}
			if(StringUtils.isBlank(channelNames)){
				channelNames="'YS_ZQ'";
			}
			Calendar calendar=Calendar.getInstance();
			calendar.add(Calendar.SECOND,Integer.parseInt(last)*-1);
			Date endDate = calendar.getTime();
			calendar.add(Calendar.SECOND,Integer.parseInt(count)*-1);
			Date startDate = calendar.getTime();
			List<CollectiveTransOrder> list = transInfoService.getUnSettle(channelNames, startDate, endDate);
			for (CollectiveTransOrder c:list){
				String url= Constants.SETTLE_TRANS+"?transferId="+c.getId()+"&userId=reSettle";
				String result= ClientInterface.baseNoClient(url,null);
				log.info("再次结算订单[{}]返回信息：{}",new String[]{c.getOrderNo(),result});
			}
		}catch (Exception e){
			log.error("未出款/出款失败 重新发起出款异常",e);
		}*/
	}

}
