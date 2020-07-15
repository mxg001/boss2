package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.service.ReSettleService;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author tans
 * @date 2019/1/10 15:07
 */
@Service("reSettleService")
public class ReSettleServiceImpl implements ReSettleService {

    private static final Logger log = LoggerFactory.getLogger(ReSettleServiceImpl.class);

    @Resource
    private RedisService redisService;

    @Resource
    private TransInfoDao transInfoDao;

    @Override
    public void reSettle() {
        try {
            log.info("未出款/出款失败 重新发起出款, reSettle_start");
            String last=(String)redisService.select("RE_SETTLE_TASK_LAST");
            String otherLast=(String)redisService.select("RE_SETTLE_TASK_OTHER_LAST");
            String count=(String)redisService.select("RE_SETTLE_TASK_COUNT");
            String channelNames = (String) redisService.select("RE_SETTLE_TASK_CHANNELS");
            String otherChannelNames = (String) redisService.select("RE_SETTLE_TASK_OTHER_CHANNELS");
            String limitNumbersStr = (String) redisService.select("RE_SETTLE_TASK_LIMIT");
            String excludeChannel = (String) redisService.select("RE_SETTLE_TASK_EXCLUDE_CHANNEL");
            String excludeTime = (String) redisService.select("RE_SETTLE_TASK_EXCLUDE_TIME");
            Integer limitNumbers = 0;
            if(!StringUtils.isNumeric(last)){
                last="900";
            }
            if(!StringUtils.isNumeric(count)){
                count="6000";
            }
            if(StringUtils.isBlank(channelNames)){
                channelNames="YS_ZQ";
            }
            if(!StringUtils.isNumeric(limitNumbersStr)){
                limitNumbers = 1000;
            } else {
                limitNumbers = Integer.parseInt(limitNumbersStr);
            }

            List<CollectiveTransOrder> otherList = getOtherCollectiveTransOrders(otherLast, count, otherChannelNames, excludeChannel, excludeTime, limitNumbers);

            List<CollectiveTransOrder> list = getCollectiveTransOrders(last, count, channelNames, limitNumbers);
            if(otherList != null && otherList.size() > 0) {
                list.addAll(otherList);
            }

            for (CollectiveTransOrder c:list){
                String url= Constants.SETTLE_TRANS+"?transferId="+c.getId()+"&userId=reSettle";
                log.info("再次结算订单reSettle_transferId:{}",c.getId());
                String result= ClientInterface.baseNoClient(url,null);
                log.info("再次结算订单[{}]返回信息：{}",new String[]{c.getOrderNo(),result});

                // 异步方案暂时不可取
//                threadpool.execute(new ReSettleRunnable(c.getId(), c.getOrderNo()));
            }
            log.info("未出款/出款失败 重新发起出款, reSettle_end");
        }catch (Exception e){
            log.error("未出款/出款失败 重新发起出款异常",e);
        }
    }

    public static void main(String[] args) {
        String last = "3600";
        String count = "3600";
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,Integer.parseInt(last)*-1);
        Date endDate = calendar.getTime();
        calendar.add(Calendar.SECOND,Integer.parseInt(count)*-1);
        Date startDate = calendar.getTime();

        System.out.println("startDate:" +  DateUtil.getLongFormatDate(startDate));
        System.out.println("endDate:" +  DateUtil.getLongFormatDate(endDate));
    }

    /**
     * 获取需要结算的订单
     * @param last
     * @param count
     * @param channelNames
     * @param limitNumbers
     * @return
     */
    private List<CollectiveTransOrder> getCollectiveTransOrders(String last, String count, String channelNames, Integer limitNumbers) {
        List<String> channelNameList = Arrays.asList(channelNames.split(","));
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,Integer.parseInt(last)*-1);
        Date endDate = calendar.getTime();
        calendar.add(Calendar.SECOND,Integer.parseInt(count)*-1);
        Date startDate = calendar.getTime();
        return getUnSettle(channelNameList, startDate, endDate,limitNumbers);
    }

    /**
     * 获取其他需要结算的订单
     * 比如：中钢的，在某些时段不能结算，查找订单的时间区间和之前普通的不一样，换一个时间区间，不影响之前的普通订单
     *
     * @param otherLast
     * @param count
     * @param otherChannelNames
     * @param excludeChannel
     * @param excludeTime
     * @param limitNumbers
     * @return
     */
    private List<CollectiveTransOrder> getOtherCollectiveTransOrders(String otherLast, String count, String otherChannelNames, String excludeChannel, String excludeTime, Integer limitNumbers) {
        List<CollectiveTransOrder> otherList = null;
        if(StringUtils.isNotEmpty(otherLast) && StringUtils.isNotEmpty(otherChannelNames)) {
            List<String> otherChannelNameList = Arrays.asList(otherChannelNames.split(","));
            //判断，如果在排除的时间段内，去掉需要排除的渠道
            otherChannelNameList = delExcludeChannel(otherChannelNameList, excludeChannel, excludeTime);
            if(otherChannelNameList != null && otherChannelNameList.size() > 0) {
                Calendar otherCalendar = Calendar.getInstance();
                otherCalendar.add(Calendar.SECOND,Integer.parseInt(otherLast)*-1);
                Date otherEndDate = otherCalendar.getTime();
                otherCalendar.add(Calendar.SECOND,Integer.parseInt(count)*-1);
                Date otherStartDate = otherCalendar.getTime();
                otherList = getUnSettle(otherChannelNameList, otherStartDate, otherEndDate,limitNumbers);
            }
        }
        return otherList;
    }

    /**
     * 去掉排除时间段内的需要排除的渠道
     * @param channelNames
     * @param excludeChannel
     * @param excludeTime
     * @return
     */
    private static List<String> delExcludeChannel(List<String> channelNames, String excludeChannel, String excludeTime) {
        if(channelNames == null || channelNames.isEmpty()) {
            return channelNames;
        }
        //如果在排除的时间段内
        String[] excludeTimeArr = excludeTime.split("-");
        if(excludeTimeArr != null && excludeTimeArr.length > 1){
            Date nowDate = new Date();
            String nowDateStr = DateUtil.getFormatDate(DateUtil.LONG_FROMATE, nowDate);
            String[] nowDateArr = nowDateStr.split(" ");
            String startTimeStr = nowDateArr[0] + " " + excludeTimeArr[0] + ":00";
            String endTimeStr = nowDateArr[0] + " " + excludeTimeArr[1] + ":00";
            Date startTime = DateUtil.parseLongDateTime(startTimeStr);
            Date endTime = DateUtil.parseLongDateTime(endTimeStr);
            if(DateUtil.compare(endTime, startTime)){
                //如果开始时间大于结束时间
                //比如 22:00-01:00
                endTime =new Date(endTime.getTime() + 24 * 60 * 60 * 1000L);
            }
            //当前时间如果在排除时间段内
            if(DateUtil.compare(startTime, nowDate) && DateUtil.compare(nowDate, endTime)){
                //将excludeChannel排除后，放到一个新的list里面
                List<String> newChannelNameList = new ArrayList<>();
                List<String> excludeChannelArr = Arrays.asList(excludeChannel.split(","));
                for(String item: channelNames){
                    if(!excludeChannelArr.contains(item) && StringUtils.isNotBlank(item)){
                        newChannelNameList.add(item);
                    }
                }
                return newChannelNameList;
            }
        }
        return channelNames;
    }

    @Override
    public List<CollectiveTransOrder> getUnSettle(List<String> channelNames, Date startDate, Date endDate, Integer limitNumbers) {
        return transInfoDao.getUnSettele(channelNames,startDate,endDate,limitNumbers);
    }
}
