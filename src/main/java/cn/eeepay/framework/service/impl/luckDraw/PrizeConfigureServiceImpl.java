package cn.eeepay.framework.service.impl.luckDraw;

import cn.eeepay.framework.dao.luckDraw.PrizeConfigureDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CouponActivityEntity;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.luckDraw.*;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.luckDraw.LuckDrawOrderService;
import cn.eeepay.framework.service.luckDraw.PrizeConfigureService;
import cn.eeepay.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/6/006.
 * @author  liuks
 * 奖项配置 service实现类
 */
@Service("prizeConfigureService")
public class PrizeConfigureServiceImpl implements PrizeConfigureService {

    @Resource
    private PrizeConfigureDao prizeConfigureDao;

    @Resource
    private LuckDrawOrderService luckDrawOrderService;

    @Resource
    private MerchantInfoService merchantInfoService;

    @Override
    public PrizeConfigure getPrizeConfigure(String funcCode) {
        return prizeConfigureDao.getPrizeConfigure(funcCode);
    }

    @Override
    public int updatePrizeConfigure(PrizeConfigure pc) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        pc.setOperator(principal.getUsername());
        int num=prizeConfigureDao.updatePrizeConfigure(pc);
        if(num>0){
            prizeConfigureDao.updateCouponActivityInfo(pc);
        }
        return num;
    }

    @Override
    public List<Prize> getPrizeList(String funcCode) {
        List<Prize> list=prizeConfigureDao.getPrizeList(funcCode);
        getCount(list);
        return list;
    }

    //查询统计值 今日已经派发奖品次数
    private  void getCount(List<Prize> list){
        if(list!=null&&list.size()>0){
            //今天 0是0分0秒0毫秒
            Calendar c1 = new GregorianCalendar();
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            c1.set(Calendar.MILLISECOND, 0);

            //今天23时59分59秒0毫秒
            Calendar c2 = new GregorianCalendar();
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            c2.set(Calendar.MILLISECOND,0);

            for(Prize item:list){
                if(item.getAwardType()!=null&&item.getAwardType().intValue()!=4){
                    LuckDrawOrder order=new LuckDrawOrder();
                    order.setPlayTimeBegin(c1.getTime());
                    order.setPlayTimeEnd(c2.getTime());
                    order.setAwardsConfigId(item.getId());
                    Integer sum=luckDrawOrderService.sumAwardsConfigId(order);
                    if(sum!=null){
                        item.setDayCount1(sum);//今日已经发放数量
                        item.setDayCount2(item.getDayCount().intValue()-sum.intValue());//今日剩余发放数量
                    }else{
                        item.setDayCount1(0);//今日已经发放数量
                        item.setDayCount2(item.getDayCount());//今日剩余发放数量
                    }
                }
            }

        }
    }
    @Override
    public Prize getPrize(int id) {
        Prize pr=prizeConfigureDao.getPrize(id);
        if(pr!=null&&pr.getId()!=null){
            if(pr.getAwardPic()!=null){
                pr.setAwardPic(CommonUtil.getImgUrlAgent(pr.getAwardPic()));
            }
            List<PrizeDetail> list=prizeConfigureDao.getPrizeDetailList(pr.getId());
            if(list!=null&&list.size()>0){
                pr.setPrizeDetailList(list);
            }
        }
        return pr;
    }

    @Override
    public int addPrize(Prize prize, Map<String, Object> msg) {
        if(!checkPrize(prize)){
            msg.put("status", false);
            msg.put("msg", "新增失败,所属奖项概率超出100%!");
            return 2;
        }else{
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            prize.setOperator(principal.getUsername());
            int num=prizeConfigureDao.addPrize(prize);
            if(num>0){
                savePrizeDetail(prize,false);
                msg.put("status", true);
                msg.put("msg", "新增奖项成功!");
                return 1;
            }else{
                msg.put("status", false);
                msg.put("msg", "新增奖项失败!");
            }
        }
        return 0;
    }

    @Override
    public int updatePrize(Prize prize, Map<String, Object> msg) {
        if(!checkPrize(prize)){
            msg.put("status", false);
            msg.put("msg", "修改失败,所属奖项概率超出100%!");
            return 2;
        }else{
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            prize.setOperator(principal.getUsername());
            int num=prizeConfigureDao.updatePrize(prize);
            if(num>0){
                savePrizeDetail(prize,true);
                msg.put("status", true);
                msg.put("msg", "修改奖项成功!");
                return 1;
            }else{
                msg.put("status", false);
                msg.put("msg", "修改奖项成功!");
            }
        }
        return 0;
    }

    /**
     * @param isEdit 是否是编辑状态
     */
    private void savePrizeDetail(Prize prize,boolean isEdit){
        if(isEdit){
            prizeConfigureDao.deletePrizeDetail(prize);
        }
        if(prize.getId()!=null){
            if(prize.getAwardType().intValue()==1||prize.getAwardType().intValue()==2){
                if(prize.getPrizeDetailList()!=null&&prize.getPrizeDetailList().size()>0){
                    for(PrizeDetail item:prize.getPrizeDetailList()){
                        item.setMoney(null);
                        prizeConfigureDao.insertPrizeDetail(prize,item);
                    }
                }
            }else if(prize.getAwardType().intValue()==3){
                PrizeDetail item=new PrizeDetail();
                item.setMoney(prize.getMoney());
                prizeConfigureDao.insertPrizeDetail(prize,item);
            }
        }
    }
    @Override
    public List<CouponActivityEntity> getCouponList(String funcCode) {
        return  prizeConfigureDao.getCouponList(funcCode);
    }

    @Override
    public int closeOpenPrize(int id, int status) {
        return prizeConfigureDao.closeOpenPrize(id,status);
    }

    @Override
    public List<PrizeBlacklist> getPrizeBlacklist(PrizeBlacklist blacklist, Page<PrizeBlacklist> page) {
        return prizeConfigureDao.getPrizeBlacklist(blacklist,page);
    }

    @Override
    public int addPrizeBlacklist(PrizeBlacklist blacklist,Map<String, Object> msg) {
        if(StringUtils.isNotBlank(blacklist.getMerchantNo())){
            MerchantInfo info=getMerchantInfo(blacklist.getMerchantNo());
            if(info==null){
                msg.put("status", false);
                msg.put("msg","新增黑名单失败,商户号不存在!");
                return 0;
            }
            if(checkBlacklist(blacklist)){
                msg.put("status", false);
                msg.put("msg","新增黑名单失败,商户号已经在黑名单列表中!");
                return 0;
            }
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            blacklist.setCreater(principal.getUsername());
            int num=prizeConfigureDao.addPrizeBlacklist(blacklist);
            if(num>0){
                msg.put("status", true);
                msg.put("msg","新增黑名单成功!");
                return num;
            }
        }
        msg.put("status", false);
        msg.put("msg","新增黑名单失败!");
        return 0;
    }
    private boolean checkBlacklist(PrizeBlacklist blacklist){
        PrizeBlacklist info=prizeConfigureDao.checkBlacklist(blacklist);
        if(info!=null){
            return  true;
        }
        return false;
    }

    @Override
    public int deleteBlacklist(int id) {
        return prizeConfigureDao.deleteBlacklist(id);
    }

    @Override
    public MerchantInfo getMerchantInfo(String merchantNo) {
        return merchantInfoService.selectByMerNo(merchantNo);
    }

    /**
     * 初始化抽奖次数
     * @return
     */
    @Override
    public int initializationAwardsConfigCount() {
        return prizeConfigureDao.initializationAwardsConfigCount();
    }

    private boolean checkPrize(Prize prize){
        BigDecimal count=prizeConfigureDao.checkPrize(prize);
        if(count!=null){
            if(count.add(prize.getProb()).compareTo(new BigDecimal("100"))>0){
                return false;
            }
        }else{
            if(prize.getProb().compareTo(new BigDecimal("100"))>0){
                return false;
            }
        }
        return true;
    }
}
