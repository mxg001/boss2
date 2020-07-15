package cn.eeepay.boss.thread;

import cn.eeepay.framework.dao.ZqFileSyncRecordDao;
import cn.eeepay.framework.model.ZqFileSyncRecord;
import cn.eeepay.framework.model.ZqServiceInfo;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

/**
 * @author tans
 * @date 2019/4/11 17:49
 */
public class ZqMerSyncTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ZqMerSyncTask.class);

    private Queue<ZqServiceInfo> queue;
    private CyclicBarrier cyclicBarrier;
    private String accessUrl;
    private String batchNo;
    private ZqFileSyncRecordDao zqFileSyncRecordDao;
    public ZqMerSyncTask(Queue<ZqServiceInfo> queue, CyclicBarrier cyclicBarrier, String url, String batchNo,
                         ZqFileSyncRecordDao zqFileSyncRecordDao) {
        this.queue = queue;
        this.cyclicBarrier = cyclicBarrier;
        this.accessUrl = url;
        this.batchNo = batchNo;
        this.zqFileSyncRecordDao = zqFileSyncRecordDao;
    }

    @Override
    public void run() {
        try {
            do {
                ZqServiceInfo poll = this.queue.poll();
                if (poll == null) {
                    return;
                }
                dealWith(poll);
            } while (true);
        } finally {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 存量商户报备
     * @param zqServiceInfo
     */
    private void dealWith(ZqServiceInfo zqServiceInfo) {
        String merchantNo = zqServiceInfo.getMerchantNo();
        Long bpId = zqServiceInfo.getBpId();
        String channelCode = zqServiceInfo.getChannelCode();
        String operator = zqServiceInfo.getOperator();

        Map<String, Object> marMap = new HashMap<String, Object>();
        List<String> channelList = new ArrayList<>();
        channelList.add(channelCode);
        marMap.put("merchantNo", merchantNo);
        marMap.put("bpId", bpId);
        marMap.put("operator", operator);
        marMap.put("channelCode", channelList);
        marMap.put("stockMerFlag", "1");//存量商户报件，需要新增的参数
        String paramStr = JSON.toJSONString(marMap);
        log.info("直清存量商户报件zqSyncSer start,url:{},param:{}", accessUrl, paramStr);
        String resultStr=new ClientInterface(accessUrl, null).postRequestBody(paramStr);
        log.info("直清存量商户报件zqSyncSer end,result:{}", resultStr);
        if(StringUtil.isEmpty(resultStr)){
            log.error("直清商户报件失败,返回结果为空");
        } else {
            JSONObject resJson = JSONObject.parseObject(resultStr);
            resJson = resJson.getJSONObject("header");
            String status = "2";//默认失败
            if(resJson.getBoolean("succeed")){
                status = "1";
            }
            ZqFileSyncRecord record =new ZqFileSyncRecord();
            record.setBatchNo(batchNo);
            record.setStatus(status);
            record.setChannelCode(channelCode);
            record.setMerchantNo(merchantNo);
            record.setBpId(bpId);
            record.setOperator(operator);
            record.setCreateTime(new Date());
            record.setResultMsg(resultStr);
            zqFileSyncRecordDao.insert(record);
        }
    }
}
