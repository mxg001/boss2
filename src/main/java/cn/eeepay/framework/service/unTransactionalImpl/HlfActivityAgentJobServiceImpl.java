package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.HappyBackActivityAgentDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.XhlfActivityOrderDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.HappyBackActivityAgentService;
import cn.eeepay.framework.service.HlfActivityAgentJobService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.DateUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("hlfActivityAgentJobService")
public class HlfActivityAgentJobServiceImpl implements HlfActivityAgentJobService {

    private static final Logger log = LoggerFactory.getLogger(HlfActivityAgentJobServiceImpl.class);
    @Resource
    private HappyBackActivityAgentService happyBackActivityAgentService;
    @Resource
    private HappyBackActivityAgentDao happyBackActivityAgentDao;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private SysDictDao sysDictDao;
    @Resource
    private XhlfActivityOrderDao xhlfActivityOrderDao;

    @Override
    public void hlfActivityAgentJob() {
        try {

            //获取当前日期，比如：2019-09-27
            Date nowShortDate = DateUtil.getNowDateShort();
            //获取nowShortDate前一天日期
            Date startTime = DateUtil.getBeforeDate(nowShortDate);
            //当天结束时间，2019-09-26 23:59:59
            Date endTime = new Date(nowShortDate.getTime() - 1000L);
            //进行统计达标处理

            //扫码交易
            log.info("扫码交易进行统计达标处理----countScanList.start");
            countScanList(startTime, endTime);
            log.info("扫码交易进行统计达标处理----countScanList.end");
            //查询今天达标，但是未入账的所有订单
            log.info("查询今天扫码交易达标，但是未入账的所有订单进行入账----scanAccountTask.start");
            List<HappyBackActivityAgent> scanOrderList = happyBackActivityAgentDao.queryScanNeedAccountList(nowShortDate);
            //进行入账

            scanAccountTask(scanOrderList);
            log.info("查询今天扫码交易达标，但是未入账的所有订单进行入账----scanAccountTask.end");

            //全部交易
            log.info("全部交易进行统计达标处理----countAllList.start");
            countAllList(startTime, endTime);
            log.info("全部交易进行统计达标处理----countAllList.end");
            //查询今天达标，但是未入账的所有订单
            log.info("查询今天全部交易达标，但是未入账的所有订单进行入账----allAccountTask.start");
            List<HappyBackActivityAgent> allOrderList = happyBackActivityAgentDao.queryAllNeedAccountList(nowShortDate);
            //进行入账
            allAccountTask(allOrderList);
            log.info("查询今天全部交易达标，但是未入账的所有订单进行入账----allAccountTask.end");

        } catch (Exception e) {
            log.error("定时任务统计代理商奖励异常", e);
        }

    }

    /**
     * 对hlf_agent_reward_order进行扫码交易统计达标处理
     *
     * @param startTime
     * @param endTime
     * @throws ParseException
     */
    private void countScanList(Date startTime, Date endTime) throws ParseException {
        //以前一天日期为基准，查询该日期在考核期间内，考核中的订单
        HappyBackActivityAgent queryOrder = new HappyBackActivityAgent();
        queryOrder.setScanTargetStatus("1");//考核中
        queryOrder.setScanRewardEndTime(endTime);
        List<HappyBackActivityAgent> orderList = happyBackActivityAgentDao.getScanNeedCountList(queryOrder);
        if (orderList == null || orderList.isEmpty()) {
            return;
        }
        for (HappyBackActivityAgent itemOrder : orderList) {
            String merchantNo = itemOrder.getMerchantNo();
            //判断今天是否是考核第一天，如果是，startTime的时分秒要用激活时的时分秒
            if (!judgeLastDay(itemOrder.getActiveTime())) {

                ///startTime = itemOrder.getActiveTime();
                itemOrder.setTransStartTime(startTime);
            }


            itemOrder.setTransEndTime(endTime);
            String type = "3";//统计交易表的金额,
            BigDecimal merchantTodayTrans = queryMerchantTransTotal(itemOrder, type);
            //添加商户累计金额记录
            //判断一下是否存在累计记录merchant_no_type_total_day_uni
            //如果存在，更新当前记录
            insertMerchantTransTotalRecord(merchantNo, startTime, type, merchantTodayTrans);

            BigDecimal totalAmount = getMerchantTotalAmount(itemOrder.getMerchantNo(), type, itemOrder.getActiveTime(), endTime);//统计xhlf_merchant_trans_total_day，商户交易总金额
            BigDecimal targetAmount = itemOrder.getScanTargetAmount();
//            itemOrder.setTotalAmount(totalAmount);
            //判断考核累计金额是否达标
            if (totalAmount.compareTo(targetAmount) < 0) {
                //未达标
                //判断当前日期是否是考核最后一天
                if (judgeLastDay(itemOrder.getScanRewardEndTime())) {
                    //当前日期是考核最后一天
                    itemOrder.setScanTargetStatus("3");
                    //需要更新商户所有周期的达标状态为未达标
                    happyBackActivityAgentDao.updateHappyBackActivityAgentScan(itemOrder);
                }
            } else {
                //已达标
                //更新累计金额，当前达标状态为已达标，入账状态置为未入账
                Date nowDate = new Date();
                itemOrder.setScanTargetStatus("2");
                itemOrder.setScanTargetTime(nowDate);
                itemOrder.setScanAccountStatus("0");
                happyBackActivityAgentDao.updateHappyBackActivityAgentScan(itemOrder);


                //将代理商入账明细置为未入账
                HappyBackActivityAgentDetail accountDetail = new HappyBackActivityAgentDetail();
                accountDetail.setScanAccountStatus("0");
                accountDetail.setHlfAgentRewardOrderId(itemOrder.getId());
                happyBackActivityAgentDao.updateAgentScanDetailByOrderId(accountDetail);

            }
        }

    }


    /**
     * 对hlf_agent_reward_order进行全部交易统计达标处理
     *
     * @param startTime
     * @param endTime
     * @throws ParseException
     */
    private void countAllList(Date startTime, Date endTime) throws ParseException {
        //以前一天日期为基准，查询该日期在考核期间内，考核中的订单
        HappyBackActivityAgent queryOrder = new HappyBackActivityAgent();
        queryOrder.setAllTargetStatus("1");//考核中
        queryOrder.setAllRewardEndTime(endTime);
        List<HappyBackActivityAgent> orderList = happyBackActivityAgentDao.getAllNeedCountList(queryOrder);
        if (orderList == null || orderList.isEmpty()) {
            return;
        }
        for (HappyBackActivityAgent itemOrder : orderList) {
            String merchantNo = itemOrder.getMerchantNo();
            //判断今天是否是考核第一天，如果是，startTime的时分秒要用激活时的时分秒
            if (!judgeLastDay(itemOrder.getActiveTime())) {
                //startTime = itemOrder.getActiveTime();
                itemOrder.setTransStartTime(startTime);
            }

            itemOrder.setTransEndTime(endTime);
            String type = "4";//统计交易表的金额,
            BigDecimal merchantTodayTrans = queryMerchantTransTotal(itemOrder, type);
            //添加商户累计金额记录
            //判断一下是否存在累计记录merchant_no_type_total_day_uni
            //如果存在，更新当前记录
            insertMerchantTransTotalRecord(merchantNo, startTime, type, merchantTodayTrans);

            BigDecimal totalAmount = getMerchantTotalAmount(itemOrder.getMerchantNo(), type, itemOrder.getActiveTime(), endTime);//统计xhlf_merchant_trans_total_day，商户交易总金额
            BigDecimal targetAmount = itemOrder.getAllTargetAmount();
//            itemOrder.setTotalAmount(totalAmount);
            //判断考核累计金额是否达标
            if (totalAmount.compareTo(targetAmount) < 0) {
                //未达标
                //判断当前日期是否是考核最后一天
                if (judgeLastDay(itemOrder.getAllRewardEndTime())) {
                    //当前日期是考核最后一天
                    itemOrder.setAllTargetStatus("3");
                    //需要更新商户所有周期的达标状态为未达标
                    happyBackActivityAgentDao.updateHappyBackActivityAgentAll(itemOrder);
                }
            } else {
                //已达标
                //更新累计金额，当前达标状态为已达标，入账状态置为未入账
                Date nowDate = new Date();
                itemOrder.setAllTargetStatus("2");
                itemOrder.setAllTargetTime(nowDate);
                itemOrder.setAllAccountStatus("0");
                happyBackActivityAgentDao.updateHappyBackActivityAgentAll(itemOrder);
                //将代理商入账明细置为未入账
                HappyBackActivityAgentDetail accountDetail = new HappyBackActivityAgentDetail();
                accountDetail.setAllAccountStatus("0");
                accountDetail.setHlfAgentRewardOrderId(itemOrder.getId());
                happyBackActivityAgentDao.updateAgentAllDetailByOrderId(accountDetail);

            }
        }

    }

    /**
     * 统计商户今日交易金额
     *
     * @param order
     * @param type  统计交易表的金额
     * @return
     */
    private BigDecimal queryMerchantTransTotal(HappyBackActivityAgent order, String type) throws ParseException {
        BigDecimal merchantTodayTrans = null;
        if ("3".equals(type)) {
            merchantTodayTrans = happyBackActivityAgentDao.queryScanTransTotal(order);
        } else if ("4".equals(type)) {
            merchantTodayTrans = happyBackActivityAgentDao.queryAllTransTotal(order);
        }
        if (merchantTodayTrans == null) {
            merchantTodayTrans = BigDecimal.ZERO;
        }
        return merchantTodayTrans;

    }

    /**
     * 获取商户的交易总金额
     * 统计xhlf_merchant_trans_total_day
     *
     * @param merchantNo
     * @param type
     * @param endTime
     * @return
     */
    private BigDecimal getMerchantTotalAmount(String merchantNo, String type, Date startTime, Date endTime) {
        BigDecimal sumAmount = null;
        XhlfMerchantTransTotalDay info = new XhlfMerchantTransTotalDay();
        info.setMerchantNo(merchantNo);
        info.setType(type);
        info.setStartDay(startTime);
        info.setEndDay(endTime);
        sumAmount = xhlfActivityOrderDao.selectMerchantSumAount(info);
        if (sumAmount == null) {
            sumAmount = BigDecimal.ZERO;
        }
        return sumAmount;
    }

    /**
     * 添加商户累计金额记录
     *
     * @param merchantNo
     * @param startTime
     * @param type
     * @param totalAmount
     */
    private void insertMerchantTransTotalRecord(String merchantNo, Date startTime, String type, BigDecimal totalAmount) {
        XhlfMerchantTransTotalDay xhlfMerchantTransTotalDay = new XhlfMerchantTransTotalDay();
        xhlfMerchantTransTotalDay.setMerchantNo(merchantNo);
        xhlfMerchantTransTotalDay.setTotalDay(startTime);
        xhlfMerchantTransTotalDay.setType(type);
        xhlfMerchantTransTotalDay.setTotalAmount(totalAmount);
        if(xhlfMerchantTransTotalDay.getTotalAmount().compareTo(BigDecimal.ZERO)>0){
            xhlfActivityOrderDao.insertMerchantTransTotalRecord(xhlfMerchantTransTotalDay);
        }
    }


    /**
     * 判断当前时间是否是考核结束最后一天
     * 定时任务一般凌晨执行，所以需要取当前的前一天时间
     * 当前时间的前一天与结束时间，日期相等，则返回true
     *
     * @param rewardEndTime
     * @return
     */
    private boolean judgeLastDay(Date rewardEndTime) throws ParseException {
        Date nowDate = DateUtil.getNowDateShort();
        Date yesterdayTime = new Date(nowDate.getTime() - 1000L);
        String yesterdayTimeStr = DateUtil.getDefaultFormatDate(yesterdayTime);
        String rewardEndTimeStr = DateUtil.getDefaultFormatDate(rewardEndTime);
        return yesterdayTimeStr.equals(rewardEndTimeStr);
    }


    /**
     * 代理商奖励活动-奖励入账
     */
    public static String happyBackAgentRecordAccount(String url, HappyBackActivityAgentDetail info, String transTypeCode) {
        url += "/newHappyBackController/agentProfitRecordAccount.do";
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        String response = "";
        claims.put("transOrderNo", info.getActiveOrder());// 当前订单编号
        claims.put("agentNo", info.getAgentNo()); //当前代理商
        claims.put("agentLevel", info.getAgentLevel()); //
        claims.put("agentNode", info.getAgentNode()); //
        claims.put("merchantNo", info.getMerchantNo()); //
        claims.put("amount", String.valueOf(info.getRealAccountAmount())); //入账金额
        claims.put("configAmount", String.valueOf(info.getConfigAmount())); //配置金额
        claims.put("fromSerialNo", "DLSJL-" + String.valueOf(info.getId())); //来源系统流水号，每笔唯一，发起端需保存
        claims.put("fromSystem", "boss"); //来源系统固定
        claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); //交易时间
        claims.put("transTypeCode", transTypeCode); //交易码固定

        log.info("代理商奖励活动-奖励入账,url:{},HappyBackActivityAgent:{}", url, info);
        response = ClientInterface.baseClient(url, claims);
        log.info("代理商奖励活动-奖励入账，returnMsg:{}", response);
        return response;
    }


    @Override
    public Map<String, Object> agentRewardAccountStatus(String ids) {
        log.info("------------------代理商奖励活动手动批量入账开始");
        Map<String, Object> msg = new HashMap<>();

        List<HappyBackActivityAgent> lists = happyBackActivityAgentDao.selectAgentRewardAccountStatusByIds(ids);
        if (lists.size() == 0) {
            msg.put("msg", "没有符合奖励入账的数据");
            msg.put("status", false);
        } else {

           /* StringBuffer allResultSB=new StringBuffer();
            StringBuffer scanResultSB=new StringBuffer();
            int allSucCount=0;
            int allFailCount=0;
            int scanSucCount=0;
            int scanFailCount=0;
*/

            //扫码交易入账
            scanAccountTask(lists);
            //全部交易入账
            allAccountTask(lists);


           /* msg.put("msg","奖励入账操作完成\n"
                    +"全部交易成功条数:"+allSucCount+"\r\n"+"全部交易失败条数:"+allFailCount+"\r\n"+allResultSB.toString()+"\r\n"
                    +"扫码交易成功条数:"+scanSucCount+"\r\n"+"扫码交易失败条数:"+scanFailCount+"\r\n"+scanResultSB.toString()
            );*/
            msg.put("msg", "奖励入账操作完成");
            msg.put("status", true);

        }

        return msg;
    }


    public void scanAccountTask(List<HappyBackActivityAgent> lists) {
        String url = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
        for (HappyBackActivityAgent agentReward : lists) {
            try {
                //扫码交易已达标2，未入账非1
                if ("2".equals(agentReward.getScanTargetStatus())
                        && !"1".equals(agentReward.getScanAccountStatus())) {

                    BigDecimal amount = null;//实际入账
                    BigDecimal amount1 = null;//上级入账
                    BigDecimal amount2 = null;//下级入账
                    List<HappyBackActivityAgentDetail> agentRewardDetails = happyBackActivityAgentService.agentAwardDetail(agentReward.getId() + "");
                    //代理商奖励明细
                    for (int i = 0; i < agentRewardDetails.size(); i++) {
                        HappyBackActivityAgentDetail agentRewardDetail = agentRewardDetails.get(i);
                        if ("1".equals(agentRewardDetail.getScanAccountStatus())) {
                            continue;
                        }
                        StringBuffer remark = new StringBuffer();
                        HappyBackActivityAgentDetail nextAgentRewardDetail = null;
                        boolean noNext = false;
                        if (i < agentRewardDetails.size() - 1) {
                            nextAgentRewardDetail = agentRewardDetails.get(i + 1);
                            //不是连续的代理商层级就当没有下级，说明数据有问题
                            if (Integer.parseInt(nextAgentRewardDetail.getAgentLevel()) - Integer.parseInt(agentRewardDetail.getAgentLevel()) != 1) {
                                log.info("不是连续的代理商层级就当没有下级，说明数据有问题，不再继续入账");
                                nextAgentRewardDetail = null;
                                noNext = true;
                            }
                        }
                        if (nextAgentRewardDetail != null && nextAgentRewardDetail.getScanAmount() != null) {
                            amount1 = agentRewardDetail.getScanAmount();
                            amount2 = nextAgentRewardDetail.getScanAmount();
                            if (amount1 != null && amount2 != null) {
                                if (amount1.compareTo(amount2) == -1) {//小于0//倒挂了 全部入账到当前级别，不继续往下
                                    log.info("入账金额倒挂，全部入账到当前级别，不继续往下入账");
                                    remark.append("入账金额倒挂，全部入账到当前级别。");
                                    amount = agentRewardDetail.getScanAmount();
                                    noNext = true;
                                } else {
                                    amount = amount1.subtract(amount2);
                                }
                            }

                        } else {
                            amount = agentRewardDetail.getScanAmount();
                        }

                        if (agentRewardDetail.getScanAmount() == null || agentRewardDetail.getScanAmount().compareTo(new BigDecimal("0.00")) == 0) {
                            log.info("配置金额为空或者0,不再往下入账。");
                            remark.append("配置金额为0,不再往下入账。");
                            noNext = true;
                        }

                        if (amount != null && amount.compareTo(new BigDecimal("0.00")) >= 0) {

                            if (amount.compareTo(new BigDecimal("0.00")) == 0) {
                                //等于0 直接改为已入账，不调用账户入账接口
                                agentRewardDetail.setScanAccountStatus("1");
                                agentRewardDetail.setScanAccountTime(new Date());
                                agentRewardDetail.setScanRemark(remark.toString());
                                happyBackActivityAgentService.updateHappyBackActivityAgentDetailScan(agentRewardDetail);
                                if ("1".equals(agentRewardDetail.getAgentLevel())) {
                                    agentReward.setScanAccountStatus("1");
                                    agentReward.setScanAccountTime(new Date());
                                    happyBackActivityAgentService.updateHappyBackActivityAgentScan(agentReward);
                                    //scanSucCount++;
                                }
                            } else {
                                agentRewardDetail.setRealAccountAmount(amount);
                                agentRewardDetail.setConfigAmount(agentRewardDetail.getScanAmount());
                                agentRewardDetail.setMerchantNo(agentReward.getMerchantNo());
                                //开始调入账
                                String returnMsg = happyBackAgentRecordAccount(url, agentRewardDetail, "000160");
                                Map<String, Object> result = JSON.parseObject(returnMsg);
                                if (result != null) {
                                    boolean status = Boolean.valueOf(result.get("status").toString());
                                    if (status) {
                                        //已奖励
                                        //存在预调账不再继续入账
                                        if ("1".equals(result.get("isPreAdjustCode").toString())) {
                                            log.info("存在预调账不再继续入账");
                                            remark.append("存在预调账或者预冻结不再往下入账。");
                                            noNext = true;
                                        }
                                        Date nowDate = new Date();
                                        agentRewardDetail.setScanAccountStatus("1");
                                        agentRewardDetail.setScanAccountTime(nowDate);
                                        agentRewardDetail.setScanRemark(remark.toString());
                                        happyBackActivityAgentService.updateHappyBackActivityAgentDetailScan(agentRewardDetail);
                                        if ("1".equals(agentRewardDetail.getAgentLevel())) {
                                            agentReward.setScanAccountStatus("1");
                                            agentReward.setScanAccountTime(nowDate);
                                            happyBackActivityAgentService.updateHappyBackActivityAgentScan(agentReward);
                                            //scanSucCount++;
                                        }


                                    } else {
                                        if ("1".equals(agentRewardDetail.getAgentLevel())) {
                                            //scanFailCount++;
                                            //scanResultSB.append("["+agentReward.getActiveOrder()+"]"+result.get("msg")+"\n");
                                        }
                                    }
                                }

                            }

                        }
                        if (noNext) {
                            //不是连续的代理商层级就当没有下级，说明数据有问题,不再继续
                            break;
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void allAccountTask(List<HappyBackActivityAgent> lists) {
        String url = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
        for (HappyBackActivityAgent agentReward : lists) {
            try {
                if ("2".equals(agentReward.getAllTargetStatus())
                        && !"1".equals(agentReward.getAllAccountStatus())) {

                    BigDecimal amount = null;//实际入账
                    BigDecimal amount1 = null;//上级入账
                    BigDecimal amount2 = null;//下级入账
                    List<HappyBackActivityAgentDetail> agentRewardDetails = happyBackActivityAgentService.agentAwardDetail(agentReward.getId() + "");
                    //代理商奖励明细
                    for (int i = 0; i < agentRewardDetails.size(); i++) {
                        HappyBackActivityAgentDetail agentRewardDetail = agentRewardDetails.get(i);
                        if ("1".equals(agentRewardDetail.getAllAccountStatus())) {
                            continue;
                        }
                        StringBuffer remark = new StringBuffer();
                        HappyBackActivityAgentDetail nextAgentRewardDetail = null;
                        boolean noNext = false;//不再往下入账标志
                        if (i < agentRewardDetails.size() - 1) {
                            nextAgentRewardDetail = agentRewardDetails.get(i + 1);
                            //不是连续的代理商层级就当没有下级，说明数据有问题
                            if (Integer.parseInt(nextAgentRewardDetail.getAgentLevel()) - Integer.parseInt(agentRewardDetail.getAgentLevel()) != 1) {
                                log.info("不是连续的代理商层级就当没有下级，说明数据有问题，不再继续入账");
                                nextAgentRewardDetail = null;
                                noNext = true;
                            }
                        }
                        if (nextAgentRewardDetail != null && nextAgentRewardDetail.getAllAmount() != null) {
                            amount1 = agentRewardDetail.getAllAmount();
                            amount2 = nextAgentRewardDetail.getAllAmount();
                            if (amount1 != null && amount2 != null) {
                                if (amount1.compareTo(amount2) == -1) {//小于0//倒挂了 全部入账到当前级别，不继续往下
                                    log.info("入账金额倒挂，全部入账到当前级别，不继续往下入账。");
                                    remark.append("入账金额倒挂，全部入账到当前级别。");
                                    amount = agentRewardDetail.getAllAmount();
                                    noNext = true;
                                } else {
                                    amount = amount1.subtract(amount2);
                                }
                            }
                        } else {
                            amount = agentRewardDetail.getAllAmount();
                        }
                        if (agentRewardDetail.getAllAmount() == null || agentRewardDetail.getAllAmount().compareTo(new BigDecimal("0.00")) == 0) {
                            log.info("配置金额为空或者0,不再往下入账。");
                            remark.append("配置金额为0,不再往下入账。");
                            noNext = true;
                        }

                        if (amount != null && amount.compareTo(new BigDecimal("0.00")) >= 0) {


                            if (amount.compareTo(new BigDecimal("0.00")) == 0) {

                                //等于0 直接改为已入账，不调用账户入账接口
                                agentRewardDetail.setAllAccountStatus("1");
                                agentRewardDetail.setAllAccountTime(new Date());
                                agentRewardDetail.setAllRemark(remark.toString());
                                happyBackActivityAgentService.updateHappyBackActivityAgentDetailAll(agentRewardDetail);
                                if ("1".equals(agentRewardDetail.getAgentLevel())) {
                                    agentReward.setAllAccountStatus("1");
                                    agentReward.setAllAccountTime(new Date());
                                    happyBackActivityAgentService.updateHappyBackActivityAgentAll(agentReward);
                                    //allSucCount++;
                                }
                            } else {
                                agentRewardDetail.setMerchantNo(agentReward.getMerchantNo());
                                agentRewardDetail.setRealAccountAmount(amount);
                                agentRewardDetail.setConfigAmount(agentRewardDetail.getAllAmount());
                                //开始调入账
                                String returnMsg = happyBackAgentRecordAccount(url, agentRewardDetail, "000161");
                                Map<String, Object> result = JSON.parseObject(returnMsg);
                                if (result != null) {
                                    boolean status = Boolean.valueOf(result.get("status").toString());
                                    if (status) {
                                        //已奖励

                                        //存在预调账不再继续入账
                                        if ("1".equals(result.get("isPreAdjustCode").toString())) {
                                            remark.append("存在预调账或者预冻结不再往下入账。");
                                            log.info("存在预调账不再继续入账");
                                            noNext = true;
                                        }
                                        Date nowDate = new Date();
                                        agentRewardDetail.setAllAccountStatus("1");
                                        agentRewardDetail.setAllAccountTime(nowDate);
                                        agentRewardDetail.setAllRemark(remark.toString());
                                        happyBackActivityAgentService.updateHappyBackActivityAgentDetailAll(agentRewardDetail);
                                        if ("1".equals(agentRewardDetail.getAgentLevel())) {
                                            agentReward.setAllAccountStatus("1");
                                            agentReward.setAllAccountTime(nowDate);
                                            happyBackActivityAgentService.updateHappyBackActivityAgentAll(agentReward);
                                            //allSucCount++;
                                        }

                                    } else {
                                        if ("1".equals(agentRewardDetail.getAgentLevel())) {
                                            //allFailCount++;
                                            //allResultSB.append("["+agentReward.getActiveOrder()+"]"+result.get("msg")+"\n");
                                        }
                                    }
                                }
                            }


                        }
                        if (noNext) {
                            //不再往下入账
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
