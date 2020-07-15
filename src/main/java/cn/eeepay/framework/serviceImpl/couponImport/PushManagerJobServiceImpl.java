package cn.eeepay.framework.serviceImpl.couponImport;

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.pushManager.PushManagerDao;
import cn.eeepay.framework.model.pushManager.PushManager;
import cn.eeepay.framework.service.pushManager.impl.PushManagerServiceImpl;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.Md5;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PushManagerJobServiceImpl  {



    private static final Logger log = LoggerFactory.getLogger(PushManagerServiceImpl.class);
    @Resource
    private PushManagerDao pushManagerDao;

    @Resource
    private SysDictDao sysDictDao;


    public void PushManagerJob() {
        // 定时任务扫描到push_manager timer_time字段小于当前时间并且 推送状态为未推送 的时候 进行推送

        try {
            PushManager pmCondition = new PushManager();
            pmCondition.setTimerTime(new Date());
            pmCondition.setPushTimes(0);
            List<PushManager> pms = pushManagerDao.getPushManagerByParam(pmCondition);

            for (PushManager pm : pms) {
                //如果目标用户是全部 根据推送对象获取商户信息  否则从推送明细表获取商户信息  每1000条进行推送一次
                if (pm.getTargetUser() == 0) {//全部
                    if (StringUtils.isNotEmpty(pm.getPushObj())) {
                        String[] pushObjArr = pm.getPushObj().split(",");
                        for (String pushObj : pushObjArr) {
                            remotePush(null,2,pushObj,pm);
                        }
                    }
                } else if (pm.getTargetUser()== 1) {//部分
                    int merchantNoCounts = pushManagerDao.getMerchantInfoCountsByPushId(pm.getId());
                    //如果部分推送 没有导入商户信息 即push_manager_detail没有相应的商户信息数据 返回推送失败标志
                    if (merchantNoCounts <= 0) {
                        pm.setPushStatus(2);
                        pm.setErrMsg("没有导入商户信息");
                        pushManagerDao.updatePushManager(pm);
                        continue;
                    }else {
                        toRemotePush(pm);
                    }
                }
                pm.setActualTime(new Date());//更新实际推送时间
                pm.setPushStatus(1);//将推送状态更改为 已推送
                pm.setPushTimes(1);//设置推送次数
                pushManagerDao.updatePushManager(pm);
            }
        } catch (Exception e) {
            log.error("定时推送失败", e);
        }
    }


    private void toRemotePush(PushManager pm ) throws Exception{

        if(pm!=null && pm.getId()!=null){

            String pushObj = pm.getPushObj();
            if(StringUtils.isNotBlank(pushObj)){
                String[] pushObjArr = pushObj.split(",");
                if(pushObjArr.length>0){
                    //每次推送的商户必须为同一个对象下面的
                    for (String obj : pushObjArr) {
                        int first = 0;
                        int pageSize = 10000;//每次从数据库去取10000条 为了性能
                        int sendSize = 1000;//每次只推1000条
                        List<String> prePushMerchantNo = new ArrayList<>();
                        while(true){
                            List<String> merchantNolistResult = pushManagerDao.getMerchantInfoPageByPushId(first, pageSize, pm.getId(),obj);
                            if(merchantNolistResult!=null && merchantNolistResult.size()>0){
                                Iterator<String> it = merchantNolistResult.iterator();
                                while (it.hasNext()) {
                                    String merchantNo = it.next();
                                    //获取当前推送商户的设备类型 如果不是当前推送的设备类型 remove掉该商户信息
                                    String currMobileType = pushManagerDao.getMobileTypeByMerchantNo(merchantNo);
                                    if (StringUtils.isNotEmpty(currMobileType)) {
                                        if(pm.getMobileTerminalType()!=null){
                                            if (pm.getMobileTerminalType() == Integer.parseInt(currMobileType)) {
                                                prePushMerchantNo.add(merchantNo);
                                                //达到推送上限
                                                if(prePushMerchantNo.size()==sendSize){
                                                    //直接推送
                                                    remotePush(prePushMerchantNo,1,obj,pm);
                                                    prePushMerchantNo.clear();
                                                }
                                            }
                                        }else{
                                            prePushMerchantNo.add(merchantNo);
                                        }
                                    }
                                }
                            }else{
                                break;
                            }
                            first = (first +1 )*pageSize;
                        }
                        if(prePushMerchantNo.size()>0){
                            remotePush(prePushMerchantNo,1, obj,pm);
                        }
                    }
                }
            }
        }
    }

    /***
     *  调用远程极光推送
     * @param list 商户编号
     * @param pushMode
     * @param pm
     */
    private void remotePush(List<String> list,Integer pushMode,String appNo,PushManager pm) throws Exception{

        String checkMerchantNo = "";
        if(list!=null && list.size()>0){
            checkMerchantNo = list.get(0);
        }

        String Token = "a185faf4c55807bca4bcfdaae06b58fa";
        String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
        String PATH = "/jPushHandle/controlJPush";

        Map<String, String> requestMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        if(pushMode!=null){
            if( pushMode==1){
                if(list!=null){
                    for (String merchantNo : list) {
                        sb.append(merchantNo).append(",");
                    }
                }
                requestMap.put("merchantNoArray", StringUtil.delLastChar(sb.toString()));
            }
        }
        requestMap.put("token", Token);
        String authCode = Md5.md5Str(USER_INFO_ID +checkMerchantNo+ Token).toUpperCase();
        String url = sysDictDao.getByKey("CORE_URL").getSysValue() + PATH;
        requestMap.put("authCode", authCode);
        requestMap.put("osType", pm.getMobileTerminalType()==null?"3":pm.getMobileTerminalType()+"");
        requestMap.put("pushMode", pushMode+"");//推送方式 1：根据商户号(数组)推 2：根据app_no全推
        requestMap.put("title", pm.getPushTitle());
        requestMap.put("content", pm.getPushContent());
        requestMap.put("appNo", appNo);
        requestMap.put("merchantNo", checkMerchantNo);
        //String ext = "{\"link\":\""+pm.getJumpUrl()+"\" ,\"type\":\"control\"}";
        JSONObject extJson = new JSONObject();
        extJson.put("link",pm.getJumpUrl());
        extJson.put("type","control");
        requestMap.put("ext",extJson.toJSONString());

        log.info("定时推送 调用极光推送接口参数 ：{}"+JSONObject.toJSONString(requestMap));

        String result = HttpUtils.sendPostRequest(url, requestMap);

        log.info("定时推送 调用极光推送接口结果 ："+result);

        JSONObject json = JSON.parseObject(result);
        String body = json.getString("body");
        String msgId = null;
        if(StringUtils.isNotEmpty(body)){
            JSONObject headerObj = JSON.parseObject(body);
            msgId= headerObj.getString("msg_id");
        }
        JSONObject header = JSON.parseObject(result).getJSONObject("header");
        String errMsg = null;
        if(header!=null){
            errMsg = header.getString("errMsg");
        }
        if(list!=null){
            for (String merchantNo : list) {
                String mobileId = pushManagerDao.getDeviceIdByAppNo(merchantNo);
                pushManagerDao.updatePushManagerDetail(merchantNo,pm.getId(),msgId,errMsg,1,mobileId);
            }
        }
    }
}
