package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CouponActivityDao;
import cn.eeepay.framework.dao.RedemptionDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CouponActivityEntity;
import cn.eeepay.framework.model.ExchangeAwardsConfig;
import cn.eeepay.framework.model.ExchangeAwardsRecode;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.RedemptionService;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("redemptionService")
public class RedemptionServiceImpl implements RedemptionService {
    private static final Logger log = LoggerFactory.getLogger(RedemptionServiceImpl.class);

    @Resource
    private RedemptionDao redemptionDao;

    @Resource
    private CouponActivityDao rewardActivityDao;


    private static Map<String,String> statusMap=new HashMap<String, String>();
    private static Map<String,String> awardTypeMap=new HashMap<String, String>();
    static {
        statusMap.put("0","未兑奖");
        statusMap.put("1","已兑奖");
        statusMap.put("2","已过期");
        statusMap.put("3","已作废");
        awardTypeMap.put("1","鼓励金");
        awardTypeMap.put("2","超级积分");
        awardTypeMap.put("3","现金券");
    }

    @Override
    public List<ExchangeAwardsRecode> queryRedemptionList(ExchangeAwardsRecode info, Page<ExchangeAwardsRecode> page) {
        List<ExchangeAwardsRecode> list=redemptionDao.queryRedemptionList(info,page);
        return list;
    }

    @Override
    public List<ExchangeAwardsConfig> queryRedemptionManageList(ExchangeAwardsConfig info) {
        List<ExchangeAwardsConfig> list=redemptionDao.queryRedemptionManageList(info);
        return list;
    }

    @Override
    public int addRedemptionManage(ExchangeAwardsConfig info){
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        CouponActivityEntity entry=new CouponActivityEntity();
        if(info.getAwardType()==1){
            entry.setActivetiyCode("9");
            entry.setCouponCode("6");
            entry.setCouponName(info.getAwardName());
            entry.setCouponAmount(info.getMoney());
            entry.setCouponCount(-1);
            entry.setCancelVerificationCode("1");
            entry.setIsshelves(1);
            entry.setIntegralScale(0);
            entry.setGiftAmount(BigDecimal.ZERO);
            entry.setOperator(principal.getUsername());
            entry.setActivityFirst(info.getActivityFirst());
            entry.setEffectiveDays(info.getEffectDays());
            rewardActivityDao.insertCouponEntity(entry);
            info.setCouponId(entry.getId());
        }else if(info.getAwardType()==2){
            entry.setActivetiyCode("9");
            entry.setCouponCode("3");
            entry.setCouponName(info.getAwardName());
            entry.setCouponAmount(info.getMoney());
            entry.setCouponCount(-1);
            entry.setCancelVerificationCode("2");
            entry.setIsshelves(1);
            entry.setIntegralScale(100);
            entry.setGiftAmount(BigDecimal.ZERO);
            entry.setOperator(principal.getUsername());
            entry.setActivityFirst(info.getActivityFirst());
            entry.setEffectiveDays(info.getEffectDays());
            rewardActivityDao.insertCouponEntity(entry);
            info.setCouponId(entry.getId());
        }
        info.setFuncCode("9");
        return redemptionDao.addRedemptionManage(info);
    }

    @Override
    public int updateRedemptionManage(ExchangeAwardsConfig info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        CouponActivityEntity entry=rewardActivityDao.selectCouponEntityById(info.getCouponId());
        if(info.getAwardType()==1||info.getAwardType()==2){
            entry.setCouponName(info.getAwardName());
            entry.setCouponAmount(info.getMoney());
            entry.setOperator(principal.getUsername());
            entry.setActivityFirst(info.getActivityFirst());
            entry.setEffectiveDays(info.getEffectDays());
            rewardActivityDao.saveCouponEntity(entry);
        }
        return redemptionDao.updateRedemptionManage(info);
    }

    public int deleteRedemptionManage(Integer id){
        return redemptionDao.deleteRedemptionManage(id);
    }

    @Override
    public int addRedemption(ExchangeAwardsRecode info) {
        return redemptionDao.addRedemption(info);
    }

    @Override
    public ExchangeAwardsConfig queryRedemptionManageById(Integer id) {
        return redemptionDao.queryRedemptionManageById(id);
    }

    @Override
    public int updateRedemptionStatus(Integer id){
        return redemptionDao.updateRedemptionStatus(id);
    }

    @Override
    public void exportRedemption(ExchangeAwardsRecode info, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;

        Page<ExchangeAwardsRecode> page = new Page<>();
        page.setPageSize(Integer.MAX_VALUE);
        List<ExchangeAwardsRecode> list = redemptionDao.queryRedemptionList(info,page);
        String fileName = "兑奖活动"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;

        for(ExchangeAwardsRecode item: list){
            map = new HashMap<>();
            map.put("id", item.getId()+"");
            map.put("excCode", item.getExcCode());
            map.put("status", StringUtils.trimToEmpty(statusMap.get(item.getStatus()+"")));
            map.put("awardName", item.getAwardName());
            map.put("awardType", StringUtils.trimToEmpty(awardTypeMap.get(item.getAwardType()+"")));
            map.put("offBeginTime", item.getOffBeginTime()==null?"":sdfTime.format(item.getOffBeginTime()));
            map.put("offEndTime", item.getOffEndTime()==null?"":sdfTime.format(item.getOffEndTime()));
            map.put("awardTime", item.getAwardTime()==null?"":sdfTime.format(item.getAwardTime()));
            map.put("createTime", item.getCreateTime()==null?"":sdfTime.format(item.getCreateTime()));
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","excCode","status","awardName","awardType","offBeginTime"
                ,"offEndTime","awardTime","createTime"};
        String[] colsName = new String[]{"序列","编号","状态","奖券名称","类型","有效期开始时间"
                ,"有效期结束时间","使用时间","生成时间"};
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出兑奖活动失败,param:{}",JSONObject.toJSONString(info));
            e.printStackTrace();
        } finally {
            try {
                if(ouputStream!=null){
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateRedemptionExpired() {
        int num=redemptionDao.updateRedemptionExpired();
        log.error("兑奖奖券过期数:{}",num);
    }
}
