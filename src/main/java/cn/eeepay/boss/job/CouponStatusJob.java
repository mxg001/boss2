package cn.eeepay.boss.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.model.UserCoupon;
import cn.eeepay.framework.service.impl.RegularTasks;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
@DisallowConcurrentExecution
public class CouponStatusJob implements Job {

	private final Logger log = LoggerFactory.getLogger(CouponStatusJob.class);
	@Resource
	private RegularTasks regularTasks;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("定时更新优惠券状态,执行时间:" + new Date());
		regularTasks.updateCouponStatus();
		//获取过期充值返数据写入核销表
		List<UserCoupon> expCoupons = regularTasks.queryExpCoupons();
		if(expCoupons != null && expCoupons.size() > 0){
			String order = DateUtil.getMessageTextTime().toString();
			for(int i = 0; i < expCoupons.size(); i ++){
				expCoupons.get(i).setToken("EXP"+order+i);
				expCoupons.get(i).setCouponStatus("0");
				expCoupons.get(i).setCancelVerificationType("3");
			}
			regularTasks.batchInsertexpCoupon(expCoupons);
		}
		//获取过期的充值返商户、金额、提示消息
		List<Map<String, Object>> expCoupon = regularTasks.queryExpCoupon();
		if(expCoupons != null && expCoupon.size() > 0){
			for(int i = 0; i < expCoupon.size(); i ++){
				try {
					String point = String.valueOf(new BigDecimal(expCoupon.get(i).get("balance").toString()).multiply(new BigDecimal("100")).intValueExact());
					String msm = expCoupon.get(i).get("activity_notice").toString()+"过期"+point+"积分";
					List<NameValuePair> list=new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("title", "充值券过期提醒"));//标题
					list.add(new BasicNameValuePair("notice", msm));//消息内容
					list.add(new BasicNameValuePair("mer_no", expCoupon.get(i).get("merchant_no").toString()));//商户号
					//如果不.returnContent()，request不会释放，顶多100个，超过100个将不会发出请求
					Content content = Request.Post(Constants.EXPCOUPON_MSM_URL).body(new UrlEncodedFormEntity(list, "utf-8")).execute().returnContent();
					if(content != null){
						log.info("returnMsg:{}", content.asString());
					}
					log.info(Constants.EXPCOUPON_MSM_URL+";"+expCoupon.get(i));
					//successCount++;
				} catch (Exception e) {
					log.error("充值卷过期提醒异常:{}",expCoupon.get(i));
					log.error(e.toString());
					break;
				}
			}
		}
	}

}