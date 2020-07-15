package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.ActivityDao;
import cn.eeepay.framework.dao.CouponActivityDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.model.CouponActivityInfo;
import cn.eeepay.framework.service.SubscribeVipPushJobService;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.Md5;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("subscribeVipPushJobService")
public class SubscribeVipPushJobServiceImpl implements SubscribeVipPushJobService {

    private final static Logger log = LoggerFactory.getLogger(SubscribeVipPushJobServiceImpl.class);

    @Resource
    private CouponActivityDao couponActivityDao;
    @Resource
    private ActivityDao activityDao;
    @Resource
    private SysDictDao sysDictDao;

    private static final String Token = "a185faf4c55807bca4bcfdaae06b58fa";
    private static final String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
    private static final String PATH = "/riskhandle/commonJPush";

    @Override
    public void subscribeVipPushJob() {
        try {
            String subscribeVipPushTime=sysDictDao.getByKey("subscribeVipPushTime").getSysValue();
            String url = sysDictDao.getByKey("CORE_URL").getSysValue() + PATH;
            List<Map> list = activityDao.queryActivityVipPush(subscribeVipPushTime);
            
            CouponActivityInfo info = couponActivityDao.selectInfoDetail("10");
            CouponActivityInfo infoDf = couponActivityDao.selectInfoDetail("14");
            for(Map map:list){
            	//根据商户号获取所属组织
                String teamId = couponActivityDao.selectTeamIdByMerchantNo(map.get("merchant_no").toString());
                if(StringUtils.isNotBlank(teamId)){
                	if("200010".equals(teamId)){
                		log.info("VIP优享到期极光推送merchantNo" +map.get("merchant_no").toString());
                        subscribeVipPush(map.get("merchant_no").toString(),"VIP优享到期提醒",info.getMaturityGivePush(),url);
                	}else if("300020".equals(teamId)){
                		log.info("点付VIP优享到期极光推送merchantNo" +map.get("merchant_no").toString());
                        subscribeVipPush(map.get("merchant_no").toString(),"点付VIP优享到期提醒",infoDf.getMaturityGivePush(),url);
                	}
                }else{
                	log.error("VIP优享到期极光推送异常,存在未知所属组织:"+map.get("merchant_no").toString());
                }
            }
        }catch (Exception e){
            log.error("VIP优享到期极光推送异常",e);
        }
    }

    /**
     * 极光推送
     * @param merchantNo
     * @param tittle
     * @param content
     */
    public static void subscribeVipPush(String merchantNo, String tittle, String content ,String url){
        String authCode = Md5.md5Str(USER_INFO_ID + merchantNo + Token).toUpperCase();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("token", Token);
        requestMap.put("merchantNo", merchantNo);
        requestMap.put("authCode", authCode);
        requestMap.put("tittle", tittle);
        requestMap.put("content", content);
        String str = HttpUtils.sendPostRequest(url, requestMap);
        log.info("VIP优享到期极光推送给" + merchantNo + "的结果：" + str+",content："+content);
    }

}
