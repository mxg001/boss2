package cn.eeepay.framework.serviceImpl;

import cn.eeepay.framework.dao.AutoTransferDao;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.service.AutoTransferService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;


@Service("autoTransferService")
public class AutoTransferServiceImpl implements AutoTransferService {

    private static final Logger log = LoggerFactory.getLogger(AutoTransferServiceImpl.class);

    @Resource
    private AutoTransferDao autoTransferDao;
    @Resource
    private SysDictService sysDictService;

    private static int count = 0;
    /**
     * T1自动出款定时任务
     */
    @Override
    public void t1AutoTransfer() {

        // 根据日历表判断是否为假期
        int flag = autoTransferDao.getHolidayFlag(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if(flag > 0){
            log.info("今天是节假日，T1自动出款定时任务不执行！" );
            return;
        }

        // 扫描交易表查询符合要求的交易订单
        int autoTransferLastDay = new Integer(sysDictService.getValueByKey("AUTO_TRANSFER_LAST_DAY")).intValue();
        List<String> orders =  autoTransferDao.selectT1Orders(autoTransferLastDay);
        List<String> toT1Orders = autoTransferDao.selectToT1Orders(autoTransferLastDay);
        orders.addAll(toT1Orders);
        if(null == orders || orders.size() <= 0){
            log.info("T1自动出款定时任务：没有需要出款的交易订单！" );
            return;
        }

        //调用账户系统查询对账结果
        List<String> successOrderNos = checkAccount(orders);
        if(successOrderNos.size() < 1){
            log.info("T1自动出款定时任务差错帐没有成功的订单！" );
            return;
        }
        log.info("T1自动出款定时任务差错帐成功的订单数：" + successOrderNos.size() );
        orders.clear();
        orders = null;

        // 将对账无误的订单提交到出款系统进行出款：10-11个线程同时进行出款
        final String flowmoneyUrl = sysDictService.getValueByKey("FLOWMONEY_SERVICE_URL") + "transfer/T1Transfer";

        if(successOrderNos.size() < 600){
            for (String orderNo : successOrderNos) {
                String url = flowmoneyUrl + "?orderId=" + orderNo + "&settleType=3&userType=1";
                log.info("T1定时任务调用出款系统，订单号：" + orderNo);
                String result = ClientInterface.baseNoClient(url, null);
                log.info("T1定时任务调用出款系统结果：" + result);
            }
        }else {
            List<List<String>> listGroup = gourpList(successOrderNos, successOrderNos.size()/10);

            final CountDownLatch countDownLatch = new CountDownLatch(listGroup.size());
            for (final List<String> list : listGroup) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Iterator<String> iterator = list.iterator();
                        while (iterator.hasNext()){
                            String orderNo = iterator.next();
                            String url = flowmoneyUrl+"?orderId="+orderNo+"&settleType=3&userType=1";
                            log.info("T1定时任务调用出款系统，订单号：" + orderNo);
                            String result= ClientInterface.baseNoClient(url,null);
                            log.info("T1定时任务调用出款系统结果：" + result);
                            count++;
                        }
                        countDownLatch.countDown();
                    }
                }).start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("T1自动出款定时任务：所有符合条件的订单出款完毕！请求出款次数；" + count );
            count = 0;
        }

    }

    /**
     * 将订单批量发送至账户查询对账结果
     * @param orders
     * @return
     */
    public List<String> checkAccount(List<String> orders){
        String accountUrl = sysDictService.getValueByKey("ACCOUNT_WEB_URL") + "taskApi/queryOrderCheckStatus";
        List<String> success = new ArrayList<>();
        List<List<String>> listGroup = gourpList(orders, 1000);
        for (List<String> list : listGroup) {
            Map<String, Object> map = new HashMap<>();
            map.put("ids", list);
            try {
                log.info("T1定时任务对账url：" + accountUrl);
                String msg = HttpUtils.postWithjson(accountUrl, JSONObject.toJSONString(map));
                log.info("T1定时任务对账结果：" + msg);
                if(StringUtils.isBlank(msg)){
                    log.info("T1定时任务查询对账结果返回空!");
                    return success;
                }
                JSONObject jsonObject = JSONObject.parseObject(msg);
                int code = (int)jsonObject.get("code");
                if(code == 1){
                    Map<String, Object> data = jsonObject.getJSONObject("data");
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        if((int)entry.getValue() ==1){
                            success.add(entry.getKey());
                        }
                    }
                }
            } catch (IOException e) {
                log.error("T1定时任务调用账户系统查询对账结果异常!" );
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * list集合分割
     * @param list
     * @param toIndex
     * @return
     */
    public List<List<String>> gourpList(List<String> list, int toIndex){
        List<List<String>> listGroup = new ArrayList<List<String>>();

        int size = list.size();
        for (int i = 0; i < size; i+=toIndex) {
            if(i+toIndex > size){
                toIndex = size - i;
            }
            List<String> newList = list.subList(i, i + toIndex);
            listGroup.add(newList);
        }
        return listGroup;
    }

    @Override
    public int updateByOrderNoAndStatus(String orderNo, String status) {
        return autoTransferDao.updateByOrderNoAndStatus(orderNo, status);
    }

    @Override
    public int insertOne(String orderNo, String merchantNo, String busiType, String SettleType, String status, String createPerson) {
        return autoTransferDao.insertOne(orderNo, merchantNo, busiType, SettleType, status, createPerson);
    }

    @Override
    public String selectByOrderNoAndStatus(String orderNo, String status) {
        return autoTransferDao.selectByOrderNoAndStatus(orderNo, status);
    }

    @Override
    public int updateByMerchantNoAndStatus(String merchantNo, String status) {
        return autoTransferDao.updateByMerchantNoAndStatus(merchantNo, status);
    }

    @Override
    public CollectiveTransOrder selectT0ByOrderNoAndTransTime(String orderNo, int reSettleTaskCount) {
        return autoTransferDao.selectT0ByOrderNoAndTransTime(orderNo, reSettleTaskCount);
    }

    @Override
    public CollectiveTransOrder selectToT1ByOrderNoAndTransTime(String orderNo, int autoTransferLastDay) {
        return autoTransferDao.selectToT1ByOrderNoAndTransTime(orderNo, autoTransferLastDay);
    }

    @Override
    public CollectiveTransOrder selectT1ByOrderNoAndTransTime(String orderNo, int autoTransferLastDay) {
        return autoTransferDao.selectT1ByOrderNoAndTransTime(orderNo, autoTransferLastDay);
    }
}
