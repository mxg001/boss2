package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.XhlfActivityOrderJobService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author tans
 * @date 2019/9/26 10:08
 */
@Service
public class XhlfActivityOrderJobServiceImpl implements XhlfActivityOrderJobService {

    private static final Logger log = LoggerFactory.getLogger(XhlfActivityOrderJobServiceImpl.class);


    @Resource
    private XhlfActivityOrderDao xhlfActivityOrderDao;

    @Resource
    private XhlfActivityMerchantOrderDao xhlfActivityMerchantOrderDao;

    @Resource
    private SysDictDao sysDictDao;

    @Resource
    private AgentInfoDao agentInfoDao;

    @Resource
    private AddCreaditcardLogDao addCreaditcardLogDao;

    public void countOrder() {
        countOrder(null);
    }

    /**
     * 定时任务统计xhlf_activity_order达标情况，并批量入账
     * 任务：按天统计商户MPOS刷卡交易
     * 筛选条件，商户在考核日期内，且状态为考核中
     * 统计商户MPOS刷卡交易，
     * 统计条件：交易时间为前一天内，交易成功，且卡号在add_creaditcard_log
     * 交易方式 = POS刷卡 and 订单类型 in（普通订单，微创业订单，云闪付，境外卡交易）
     * 保存累计金额记录，当前周期累计金额 = 当天累计金额 + 当前周期累计金额
     * 1.累计金额大于等目标金额
     * 1.1更新累计金额，当前达标状态为已达标，入账状态置为未入账，入账明细置为未入账
     * 1.2判断当前周期是否是最后一个周期
     * 1.2.1如果是，则更新所有周期的达标状态为已达标，记录达标时间
     * <p>
     * 2.累计金额不足目标金额
     * 2.1判断当前日期是否是考核最后一天
     * 2.1.1如果不是，更新累计金额
     * 2.1.2如果是，则更新当前达标状态为未达标，且更新所有周期的达标状态为未达标
     * <p>
     * 3.判断是否有订单需要从未开始置为考核中
     * <p>
     * 4.对xhlf_activity_merchant_order进行统计达标处理
     * <p>
     * 5.查询今天达标，但是未入账的所有订单（代理商），进行入账
     * <p>
     * 6.查询今天达标，但是未入账的所有订单（商户），进行入账
     */
    @Override
    public void countOrder(Date startShortDate) {
        try {
            //获取当前日期，比如：2019-09-27
            Date nowShortDate = startShortDate == null ? DateUtil.getNowDateShort() : startShortDate;

            //获取nowShortDate前一天日期
            Date startTime = DateUtil.getBeforeDate(nowShortDate);

            //当天结束时间，2019-09-26 23:59:59
            Date endTime = new Date(nowShortDate.getTime() - 1000L);

            //对xhlf_activity_merchant_order进行统计达标处理
            countMerchantList(startTime, endTime);

            //对xhlf_activity_order进行统计达标处理
            countList(startTime, endTime);

            //判断是否有订单需要从未开始置为考核中
            xhlfActivityOrderDao.updateCurrentOrderChecking(new Date());

            //查询今天达标，但是未入账的所有订单
            List<XhlfActivityOrder> orderList = xhlfActivityOrderDao.queryNeedAccountList(nowShortDate);
            //对xhlf_activity_order进行入账
            accountList(orderList);

            //查询今天达标，但是未入账的所有订单
            List<XhlfActivityMerchantOrder> merchantOrderList = xhlfActivityMerchantOrderDao.queryMerchantNeedAccountList(nowShortDate);
            //对xhlf_activity_merchant_order进行入账
            accountMerchantList(merchantOrderList);
        } catch (Exception e) {
            log.error("定时任务统计新欢乐返异常", e);
        }
    }

    /**
     * 临时：手工统计xhlf_activity_order达标情况
     *
     */
    @Override
    public int countDateAgentOrder(Date nowShortDate) {
        try {
            //获取当前日期，比如：2019-09-27
//            Date nowShortDate = DateUtil.getNowDateShort();
            //获取nowShortDate前一天日期
            Date startTime = DateUtil.getBeforeDate(nowShortDate);

            //当天结束时间，2019-09-26 23:59:59
            Date endTime = new Date(nowShortDate.getTime() - 1000L);

            //对xhlf_activity_order进行统计达标处理
            countListByInterface(startTime, endTime);

            //判断是否有订单需要从未开始置为考核中
            xhlfActivityOrderDao.updateCurrentOrderChecking(nowShortDate);

            return 1;

        } catch (Exception e) {
            log.error("定时任务统计新欢乐返异常", e);
            return 0;
        }
    }

    /**
     * 手工统计代理商奖励达标情况
     * 对xhlf_activity_order进行统计达标处理
     *
     * @param startTime
     * @param endTime
     * @throws ParseException
     */
    private void countListByInterface(Date startTime, Date endTime) throws ParseException {
        //以前一天日期为基准，查询该日期在考核期间内，考核中的订单
        XhlfActivityOrder queryOrder = new XhlfActivityOrder();
        queryOrder.setCurrentTargetStatus("1");//考核中
        queryOrder.setRewardTime(endTime);
        List<XhlfActivityOrder> orderList = xhlfActivityOrderDao.queryNeedCountList(queryOrder);
        if (orderList == null || orderList.isEmpty()) {
            return;
        }
        for (XhlfActivityOrder itemOrder : orderList) {
            //如果是智能版第二周期的订单,只有对应的商户订单达标状态为未达标时,才能开始统计考核
            if("2".equals(itemOrder.getXhlfActivityType()) && "2".equals(itemOrder.getCurrentCycle())){
                XhlfActivityMerchantOrder merchantOrder = xhlfActivityMerchantOrderDao.queryMerchantOrder(itemOrder.getMerchantNo());
                //如果商户订单不为空，且是考核中或者已达标，则不对代理商进行考核
                if(merchantOrder != null && ("0".equals(merchantOrder.getActivityTargetStatus()) || "1".equals(merchantOrder.getActivityTargetStatus()))){
                    continue;
                }
            }
            itemOrder.setTransEndTime(endTime);
            //如果激活时间大于奖励开始时间,则使用激活时间作为开始时间统计今天的交易
            if (judgeSameDay(itemOrder.getActiveTime())) {
                itemOrder.setTransStartTime(itemOrder.getActiveTime());
            }else{
                itemOrder.setTransStartTime(startTime);
            }
            //统计交易表的金额,目的是给:1.xhlf_activity_order累计金额,2.xhlf_activity_merchant_order累计金额
            String type = itemOrder.getAgentTransTotalType() == null ? "1" : String.valueOf(itemOrder.getAgentTransTotalType());
            XhlfMerchantTransTotalDay xhlfMerchantTransTotalDay = xhlfActivityOrderDao.queryMerchantTransTotalDay(itemOrder.getMerchantNo(), type, endTime);
            if (xhlfMerchantTransTotalDay == null) {//上面可能统计了，有统计就不再统计
                String merchantNo = itemOrder.getMerchantNo();
                BigDecimal merchantTodayTrans = queryMerchantTransTotal(itemOrder, type);
                //添加商户累计金额记录
                //判断一下是否存在累计记录merchant_no_type_total_day_uni
                //如果存在，更新当前记录
                insertMerchantTransTotalRecord(merchantNo, startTime, type, merchantTodayTrans);
            }

            BigDecimal totalAmount = getMerchantTotalAmount(itemOrder.getMerchantNo(), type, itemOrder.getRewardStartTime(), endTime);//统计xhlf_merchant_trans_total_day，商户交易总金额
            BigDecimal targetAmount = itemOrder.getTargetAmount();
//            itemOrder.setTotalAmount(totalAmount);
            Date nowDate = new Date();
            //判断考核累计金额是否达标
            if (totalAmount.compareTo(targetAmount) < 0) {
                //未达标
                //判断是否超过奖励截止时间
//                if (DateUtil.compare(itemOrder.getRewardEndTime(), nowDate)) {
                if (DateUtil.compare(itemOrder.getRewardEndTime(), endTime)) {
                    //当前日期是考核最后一天
                    //需要更新商户所有周期的达标状态为未达标,将当前周期以及

                    log.info("xhlf_activity_order未达标更新："+itemOrder.toString());

                    //需要更新商户所有周期的达标状态为未达标
                    //判断xhlf_activity_type=1才更新
                    if("1".equals(itemOrder.getXhlfActivityType())){
                        itemOrder.setCurrentTargetStatus("3");
                        xhlfActivityOrderDao.updateCurrenAndAfterOrder(itemOrder);
                        itemOrder.setActivityTargetStatus("2");
                        xhlfActivityOrderDao.updateOrder(itemOrder);
                    }else{
                        itemOrder.setCurrentTargetStatus("3");
                        xhlfActivityOrderDao.updateCurrenOrder(itemOrder);
                    }

                }
            } else {
                //已达标
                //更新累计金额，当前达标状态为已达标，入账状态置为未入账
                itemOrder.setCurrentTargetStatus("2");
                itemOrder.setCurrentTargetTime(nowDate);
                itemOrder.setRewardAccountStatus("0");
                xhlfActivityOrderDao.updateCurrentOrder(itemOrder);


                //将入账明细置为未入账
                XhlfAgentAccountDetail accountDetail = new XhlfAgentAccountDetail();
                accountDetail.setAccountStatus("0");
                accountDetail.setOldAccountStatus("-1");
                accountDetail.setXhlfActivityOrderId(itemOrder.getId());
                xhlfActivityOrderDao.updateAccountDetailByActivityOrderId(accountDetail);
                //判断当前周期是否是最后一个周期
                if (judgeLastCycle(itemOrder)) {
                    //如果是最后一个周期
                    //更新所有活动达标状态为已达标
                    if("1".equals(itemOrder.getXhlfActivityType())){
                        itemOrder.setActivityTargetStatus("1");
                        itemOrder.setActivityTargetTime(nowDate);
                        xhlfActivityOrderDao.updateActivityOrder(itemOrder);
                    }

                }
            }
        }
    }

    /**
     * 对xhlf_activity_merchant_order进行入账
     *
     * @param orderList
     */
    @Override
    public int accountMerchantList(List<XhlfActivityMerchantOrder> orderList) {
        int successNum = 0;
        if (orderList == null || orderList.isEmpty()) {
            return successNum;
        }
        String accountUrl = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL") + Constants.XHLF_MERCHANT_ACCOUNT;
        for (XhlfActivityMerchantOrder itemOrder : orderList) {
            try {
                //新欢乐送商户入账
                String responseMsg = ClientInterface.xhlfMerchantAccount(itemOrder, accountUrl);
                //处理商户入账接口回调
                boolean accoutnStatus = dealMerchantAccountResponse(itemOrder, responseMsg);
                if (accoutnStatus) {
                    successNum++;
                }
            } catch (Exception e) {
                log.error("新欢乐送商户入账异常", e);
            }
        }
        return successNum;
    }

    /**
     * 处理商户入账接口回调
     *
     * @param order
     * @param responseMsg
     */
    private boolean dealMerchantAccountResponse(XhlfActivityMerchantOrder order, String responseMsg) {
        boolean dealStatus = false;
        if (StringUtil.isEmpty(responseMsg)) {
            return dealStatus;
        }
        JSONObject responseJson = JSONObject.parseObject(responseMsg);
        String accountStatus = "0";//入账成功,奖励入账状态,0:未入账,1:已入账,-1:未开始,还没进到入账逻辑
        if (responseJson.getBoolean("status")) {
            accountStatus = "1";
        }
        String operator = getCurrentUser();
        order.setRewardAccountStatus(accountStatus);
        order.setOperator(operator);
        order.setRewardAccountTime(new Date());
        xhlfActivityMerchantOrderDao.updateAccount(order);
        dealStatus = true;
        return dealStatus;
    }

    /**
     * 对xhlf_activity_order进行入账
     *
     * @param orderList
     */
    @Override
    public int accountList(List<XhlfActivityOrder> orderList) {
        int successNum = 0;
        if (orderList == null || orderList.isEmpty()) {
            return successNum;
        }
        String operator = getCurrentUser();
        String accountUrl = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
        for (XhlfActivityOrder itemOrder : orderList) {
            try {
                //如果奖励金额小于等于0，则不调入账，直接置为已入账
                if (itemOrder.getRewardAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    itemOrder.setRewardAccountStatus("1");//已入账
                    itemOrder.setRewardAccountTime(new Date());
                    itemOrder.setOperator(operator);
                    xhlfActivityOrderDao.updateOrderAccount(itemOrder);
                    xhlfActivityOrderDao.updateAccountDetailByXhlfActivityOrder(itemOrder);
                    continue;
                }
                List<XhlfAgentAccountDetail> accountDetailList = xhlfActivityOrderDao.queryNeedAccountDetail(itemOrder.getId());
                if (accountDetailList == null || accountDetailList.isEmpty()) {
                    continue;
                }
                //入账代理商明细
                boolean orderAccountStatus = accountDetailList(accountDetailList, accountUrl);
                if (orderAccountStatus) {
                    //如果一级代理商入账成功，需要更新订单入账状态为成功
                    itemOrder.setRewardAccountStatus("1");//已入账
                    itemOrder.setRewardAccountTime(new Date());
                    itemOrder.setOperator(operator);
                    xhlfActivityOrderDao.updateOrderAccount(itemOrder);
                    successNum++;
                }
            } catch (Exception e) {
                log.error("新欢乐送代理商入账异常", e);
            }

        }
        return successNum;
    }

    /**
     * 获取当前用户，没有则是定时任务
     *
     * @return
     */
    private String getCurrentUser() {
        String operator = "定时任务";
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null) {
                operator = principal.getRealName();
            }
        } catch (Exception e) {

        }
        return operator;
    }


    /**
     * 入账代理商明细
     *
     * @param accountDetailList
     * @param accountUrl
     * @return 一级代理商入账状态
     */
    private boolean accountDetailList(List<XhlfAgentAccountDetail> accountDetailList, String accountUrl) {
        boolean orderAccountStatus = false;
        for (XhlfAgentAccountDetail accountDetail : accountDetailList) {
            //对单笔明细进行入账
            AgentInfo agentInfo = agentInfoDao.select(accountDetail.getAgentNo());
            accountDetail.setAgentNode(agentInfo.getAgentNode());
            Map<String, Object> accountMap = accountDetail(accountDetail, accountUrl);
            String accountStatus = (String) accountMap.get("accountStatus");//入账状态
            String isPreAdjust = (String) accountMap.get("isPreAdjust");//是否存在预调账，0：不存在，1：存在
            //如果一级代理商记账成功，则订单记账成功
            if ("1".equals(accountStatus) && "1".equals(accountDetail.getAgentLevel())) {
                orderAccountStatus = true;
            }
            //如果入账失败或者存在预调账，则下级不再入账
            if (!"1".equals(accountStatus) || "1".equals(isPreAdjust)) {
                break;
            }
        }
        return orderAccountStatus;
    }

    /**
     * 对单笔明细进行入账
     *
     * @param accountDetail
     * @param accountUrl
     */
    private Map<String, Object> accountDetail(XhlfAgentAccountDetail accountDetail, String accountUrl) {
        if ("1".equals(accountDetail.getAgentLevel())) {
            //一级代理商入账
            return oneAgentAccount(accountDetail, accountUrl + Constants.XHLF_ONE_AGENT_ACCOUNT);
        } else {
            //一级以下代理商入账
            return agentAccount(accountDetail, accountUrl + Constants.XHLF_AGENT_ACCOUNT);
        }
    }

    /**
     * 一级以下代理商入账
     *
     * @param accountDetail
     * @param accountUrl
     * @return
     * @throws Exception
     */
    private Map<String, Object> agentAccount(XhlfAgentAccountDetail accountDetail, String accountUrl) {
        Map<String, Object> result = new HashMap<>();
        String accountStatus = "0";//入账成功,奖励入账状态,0:未入账,1:已入账,-1:未开始,还没进到入账逻辑
        String isPreAdjust = "0";//默认不存在预调账
        result.put("accountStatus", accountStatus);
        result.put("isPreAdjust", isPreAdjust);
        //判断奖励金额是否倒挂，倒挂则不调用账户接口，需要更新备注信息
        String parentAgentNo = accountDetail.getParentAgentNo();
        Integer xhlfActivityOrderId = accountDetail.getXhlfActivityOrderId();
        BigDecimal parentAmount = xhlfActivityOrderDao.queryParentAount(parentAgentNo, xhlfActivityOrderId);
        if (parentAmount == null) {
            //上级奖励金额为空
            accountDetail.setRemark(String.format("上级奖励金额为空，本级代理商编号：%s，上级代理商编号：%s，奖励金额：%s"
                    , accountDetail.getAgentNo(), parentAgentNo, parentAmount));
            xhlfActivityOrderDao.updateAccountDetailRemark(accountDetail);
            return result;
        }
        BigDecimal amount = accountDetail.getAmount();
        if (amount.compareTo(parentAmount) > 0) {
            //奖励金额倒挂
            accountDetail.setRemark(String.format("奖励金额倒挂，本级代理商编号：%s，奖励金额：%s，上级代理商编号：%s，奖励金额：%s"
                    , accountDetail.getAgentNo(), amount, parentAgentNo, parentAmount));
            xhlfActivityOrderDao.updateAccountDetailRemark(accountDetail);
            return result;
        }
        //调用一级以下入账接口
        String responseMsg = ClientInterface.xhlfAgentAccount(accountDetail, accountUrl);
        return dealAccountResponse(accountDetail, responseMsg);
    }

    /**
     * 一级代理商入账
     *
     * @param accountDetail
     * @param accountUrl
     * @return
     * @throws Exception
     */
    private Map<String, Object> oneAgentAccount(XhlfAgentAccountDetail accountDetail, String accountUrl) {
        String responseMsg = ClientInterface.xhlfOneAgentAccount(accountDetail, accountUrl);
        return dealAccountResponse(accountDetail, responseMsg);
    }

    /**
     * 处理账户回调信息
     * 只有账户记账成功，且不存在预调账，则返回true
     *
     * @param accountDetail
     * @param responseMsg
     * @return
     */
    private Map<String, Object> dealAccountResponse(XhlfAgentAccountDetail accountDetail, String responseMsg) {
        Map<String, Object> result = new HashMap<>();
        String accountStatus = "0";//入账成功,奖励入账状态,0:未入账,1:已入账,-1:未开始,还没进到入账逻辑
        String isPreAdjust = "0";//默认不存在预调账
        result.put("accountStatus", accountStatus);
        result.put("isPreAdjust", isPreAdjust);
        if (StringUtil.isEmpty(responseMsg)) {
            return result;
        }
        JSONObject responseJson = JSONObject.parseObject(responseMsg);
        String remark = "";
        if (responseJson.getBoolean("status")) {
            accountStatus = "1";//记账成功
            String isPreAdjustCode = responseJson.getString("isPreAdjustCode");//当前代理商是否有预调账，1：是，0：否
            if ("1".equals(isPreAdjustCode)) {
                isPreAdjust = "1";//存在预调账
                remark = "记账成功，当前代理商存在预调账或预冻结";
            } else {
                remark = "记账成功";
            }
        } else {
            //入账失败
            remark = responseJson.getString("msg");
        }
        String operator = getCurrentUser();
        accountDetail.setRemark(remark);
        accountDetail.setAccountStatus(accountStatus);
        accountDetail.setOperator(operator);
        accountDetail.setAccountTime(new Date());
        xhlfActivityOrderDao.updateAccountDetail(accountDetail);
        result.put("isPreAdjust", isPreAdjust);
        result.put("accountStatus", accountStatus);
        return result;
    }

    /**
     * 对xhlf_activity_merchant_order进行统计达标处理
     *
     * @param startTime
     * @param endTime
     * @throws ParseException
     */
    private void countMerchantList(Date startTime, Date endTime) throws ParseException {
        //查询xhlf_activity_merchant_order，统计需要统计的订单
        XhlfActivityMerchantOrder queryMerchantOrder = new XhlfActivityMerchantOrder();
        queryMerchantOrder.setActivityTargetStatus("0");//考核中
        queryMerchantOrder.setRewardTime(endTime);
        List<XhlfActivityMerchantOrder> merchantOrderList = xhlfActivityMerchantOrderDao.queryMerchantNeedCountList(queryMerchantOrder);
        if (merchantOrderList == null || merchantOrderList.isEmpty()) {
            return;
        }
        for (XhlfActivityMerchantOrder itemOrder : merchantOrderList) {
            String type =null;
            if("1".equals(itemOrder.getXhlfActivityType())){
                type = "2";//统计交易表的金额,目的是给:1.xhlf_activity_order累计金额,2.xhlf_activity_merchant_order累计金额
            }else if("2".equals(itemOrder.getXhlfActivityType())){
                type = "8";
            }

            XhlfMerchantTransTotalDay xhlfMerchantTransTotalDay = xhlfActivityOrderDao.queryMerchantTransTotalDay(itemOrder.getMerchantNo(), type, endTime);
            if (xhlfMerchantTransTotalDay == null) {//上面可能统计了，有统计就不再统计
                //查询统计金额
                XhlfActivityOrder queryCto = new XhlfActivityOrder();
                //如果激活时间大于奖励开始时间,则使用激活时间作为开始时间统计今天的交易
                if (judgeSameDay(itemOrder.getActiveTime())) {
                    queryCto.setTransStartTime(itemOrder.getActiveTime());
                }else{
                    queryCto.setTransStartTime(startTime);
                }
                queryCto.setMerchantNo(itemOrder.getMerchantNo());
                queryCto.setTransEndTime(endTime);

                BigDecimal merchantTodayTrans = queryMerchantTransTotal(queryCto, type);//统计商户昨天交易的总金额
                //添加商户累计金额记录
                insertMerchantTransTotalRecord(itemOrder.getMerchantNo(), startTime, type, merchantTodayTrans);
            }

            BigDecimal totalAmount = getMerchantTotalAmount(itemOrder.getMerchantNo(), type, itemOrder.getRewardStartTime(), endTime);//统计xhlf_merchant_trans_total_day，商户交易总金额
            BigDecimal targetAmount = itemOrder.getTargetAmount();
//            itemOrder.setTotalAmount(totalAmount);
            //判断考核累计金额是否达标
            Date nowDate = new Date();
            if (totalAmount.compareTo(targetAmount) < 0) {
                //未达标
                //判断当前日期是否是考核最后一天
                //不是最后一天,不做任何操作
                if (DateUtil.compare(nowDate, itemOrder.getRewardEndTime())) {
                    continue;
                }
                //当前日期是考核最后一天
                //更新活动达标状态置为未达标
                itemOrder.setActivityTargetStatus("2");
                xhlfActivityMerchantOrderDao.updateActivityOrder(itemOrder);

                //智能版,需要对代理商订单进行处理
                if ("2".equals(itemOrder.getXhlfActivityType())) {
                    dealActivitySmartOrder(itemOrder, totalAmount, nowDate, false);
                    continue;
                }


            } else {
                //已达标
                //更新累计金额，当前达标状态为已达标，入账状态置为未入账

                itemOrder.setActivityTargetStatus("1");
                itemOrder.setActivityTargetTime(nowDate);
                itemOrder.setRewardAccountStatus("0");
                xhlfActivityMerchantOrderDao.updateActivityOrder(itemOrder);
                //当类型为智能版这种订单达标时，
                if ("2".equals(itemOrder.getXhlfActivityType())) {
                    dealActivitySmartOrder(itemOrder, totalAmount, nowDate, true);
                }
            }
        }
    }

    /**
     * 根据商户订单处理智能版订单
     * @param itemOrder
     * @param totalAmount
     * @param nowDate
     * @throws ParseException
     */
    private void dealActivitySmartOrder(XhlfActivityMerchantOrder itemOrder, BigDecimal totalAmount, Date nowDate, boolean merchantTargetStatus) throws ParseException {
        if(merchantTargetStatus) {
            //商户达标时
            //更新当前周期达标状态为"已达标返商户":5,从考核中改为已达标返商户
            xhlfActivityOrderDao.updateActivityOrderCurrentTargetStatus(itemOrder.getMerchantNo(), nowDate,"5","1","1");
            xhlfActivityOrderDao.updateActivityOrderCurrentTargetStatus(itemOrder.getMerchantNo(), nowDate,"5","2","1");
            return;
        }
        //商户不达标时
        //更新代理商第一周期订单,当前周期达标状态为"未达标返代理商":4,从考核中改为已返代理商
        xhlfActivityOrderDao.updateActivityOrderCurrentTargetStatus(itemOrder.getMerchantNo(), nowDate,"4","1","1");

        //判断代理商第二周期订单,是否达标,达标则更新当前周期达标状态为已达标,不达标则判断考核时间是否已过截止时间,若已截止,则当前周期达标状态则置为未达标
        String agentCycle = "2";
        XhlfActivityOrder secondOrder = xhlfActivityOrderDao.queryActivityOrder(itemOrder.getMerchantNo(), agentCycle, "1");
        if(secondOrder == null || secondOrder.getTargetAmount() == null) {
            return;
        }
        //如果已达标
        if (totalAmount.compareTo(secondOrder.getTargetAmount()) >= 0) {
            xhlfActivityOrderDao.updateActivityOrderCurrentTargetStatus(itemOrder.getMerchantNo(), nowDate,"2",agentCycle,"1");
        } else {
            //如果未达标
            //且已超过奖励截止日期
            if (DateUtil.compare(secondOrder.getRewardEndTime(), nowDate)) {
                xhlfActivityOrderDao.updateActivityOrderCurrentTargetStatus(itemOrder.getMerchantNo(), null,"3",agentCycle,"1");
            }
        }
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
        if (xhlfMerchantTransTotalDay.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
            xhlfActivityOrderDao.insertMerchantTransTotalRecord(xhlfMerchantTransTotalDay);
        }
    }

    /**
     * 对xhlf_activity_order进行统计达标处理
     *
     * @param startTime
     * @param endTime
     * @throws ParseException
     */
    private void countList(Date startTime, Date endTime) throws ParseException {
        //以前一天日期为基准，查询该日期在考核期间内，考核中的订单
        XhlfActivityOrder queryOrder = new XhlfActivityOrder();
        queryOrder.setCurrentTargetStatus("1");//考核中
        queryOrder.setRewardTime(endTime);
        List<XhlfActivityOrder> orderList = xhlfActivityOrderDao.queryNeedCountList(queryOrder);
        if (orderList == null || orderList.isEmpty()) {
            return;
        }
        for (XhlfActivityOrder itemOrder : orderList) {
            //如果是智能版第二周期的订单,只有对应的商户订单达标状态为未达标时,才能开始统计考核
            if("2".equals(itemOrder.getXhlfActivityType()) && "2".equals(itemOrder.getCurrentCycle())){
                XhlfActivityMerchantOrder merchantOrder = xhlfActivityMerchantOrderDao.queryMerchantOrder(itemOrder.getMerchantNo());
                //如果商户订单不为空，且是考核中或者已达标，则不对代理商进行考核
                if(merchantOrder != null && ("0".equals(merchantOrder.getActivityTargetStatus()) || "1".equals(merchantOrder.getActivityTargetStatus()))){
                    continue;
                }
            }
            itemOrder.setTransEndTime(endTime);
            //如果激活时间大于奖励开始时间,则使用激活时间作为开始时间统计今天的交易
            if (judgeSameDay(itemOrder.getActiveTime())) {
                itemOrder.setTransStartTime(itemOrder.getActiveTime());
            }else{
                itemOrder.setTransStartTime(startTime);
            }
            //统计交易表的金额,目的是给:1.xhlf_activity_order累计金额,2.xhlf_activity_merchant_order累计金额
            String type = itemOrder.getAgentTransTotalType() == null ? "1" : String.valueOf(itemOrder.getAgentTransTotalType());
            XhlfMerchantTransTotalDay xhlfMerchantTransTotalDay = xhlfActivityOrderDao.queryMerchantTransTotalDay(itemOrder.getMerchantNo(), type, endTime);
            if (xhlfMerchantTransTotalDay == null) {//上面可能统计了，有统计就不再统计
                String merchantNo = itemOrder.getMerchantNo();
                BigDecimal merchantTodayTrans = queryMerchantTransTotal(itemOrder, type);
                //添加商户累计金额记录
                //判断一下是否存在累计记录merchant_no_type_total_day_uni
                //如果存在，更新当前记录
                insertMerchantTransTotalRecord(merchantNo, startTime, type, merchantTodayTrans);
            }

            BigDecimal totalAmount = getMerchantTotalAmount(itemOrder.getMerchantNo(), type, itemOrder.getRewardStartTime(), endTime);//统计xhlf_merchant_trans_total_day，商户交易总金额
            BigDecimal targetAmount = itemOrder.getTargetAmount();
//            itemOrder.setTotalAmount(totalAmount);
            Date nowDate = new Date();
            //判断考核累计金额是否达标
            if (totalAmount.compareTo(targetAmount) < 0) {
                //如果传入的截止时间 + 1 天等于当前日期，则认定为定时任务；反之则是通过接口跑的指定日期的任务
                boolean isTask =  DateUtils.addDays(startTime,1).compareTo(DateUtil.getNowDateShort())==0;
                //如果是定时任务，则取当前日期；如果不是定时任务，则取传入的截止时间，该时间为统计交易的截止时间，将传入的截止时间 + 1天，当作比较日期
                Date compareDate = isTask ? nowDate : DateUtils.addDays(startTime,1);
                //判断订单对应的审核截止日期与 compareDate 关系。
                //未达标
                //判断是否超过奖励截止时间
                log.info("rewardEndTime[{}]endTime[{}]nowDate[{}]compareDate[{}]", itemOrder.getRewardEndTime(), endTime, nowDate, compareDate);
                if (DateUtil.compare(itemOrder.getRewardEndTime(), compareDate)) {
                    //当前日期是考核最后一天
                    //需要更新商户所有周期的达标状态为未达标,将当前周期以及
                    log.info("xhlf_activity_order未达标更新："+itemOrder.toString());
                    //需要更新商户所有周期的达标状态为未达标
                    //判断xhlf_activity_type=1才更新
                    if("1".equals(itemOrder.getXhlfActivityType())){
                        itemOrder.setCurrentTargetStatus("3");
                        xhlfActivityOrderDao.updateCurrenAndAfterOrder(itemOrder);
                        itemOrder.setActivityTargetStatus("2");
                        xhlfActivityOrderDao.updateOrder(itemOrder);
                    }else{
                        itemOrder.setCurrentTargetStatus("3");
                        xhlfActivityOrderDao.updateCurrenOrder(itemOrder);
                    }
                }
            } else {
                //已达标
                //更新累计金额，当前达标状态为已达标，入账状态置为未入账
                itemOrder.setCurrentTargetStatus("2");
                itemOrder.setCurrentTargetTime(nowDate);
                itemOrder.setRewardAccountStatus("0");
                xhlfActivityOrderDao.updateCurrentOrder(itemOrder);

                //将入账明细置为未入账
                XhlfAgentAccountDetail accountDetail = new XhlfAgentAccountDetail();
                accountDetail.setAccountStatus("0");
                accountDetail.setOldAccountStatus("-1");
                accountDetail.setXhlfActivityOrderId(itemOrder.getId());
                xhlfActivityOrderDao.updateAccountDetailByActivityOrderId(accountDetail);
                //判断当前周期是否是最后一个周期
                if (judgeLastCycle(itemOrder)) {
                    //如果是最后一个周期
                    //更新所有活动达标状态为已达标
                    if("1".equals(itemOrder.getXhlfActivityType())){
                        itemOrder.setActivityTargetStatus("1");
                        itemOrder.setActivityTargetTime(nowDate);
                        xhlfActivityOrderDao.updateActivityOrder(itemOrder);
                    }

                }
            }
        }
    }

    /**
     * 判断当前订单是否是最后一个周期
     * 存在下一个周期订单(时间区间，商户号)，则返回false
     * 不存在返回true
     *
     * @param itemOrder
     * @return
     */
    private boolean judgeLastCycle(XhlfActivityOrder itemOrder) {
        XhlfActivityOrder lastOrder = xhlfActivityOrderDao.queryLastOrder(itemOrder);
        if (lastOrder == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断当前时间是否是考核结束最后一天
     * 定时任务一般凌晨执行，所以需要取当前的前一天时间
     * 当前时间的前一天与结束时间，日期相等，则返回true
     * 当前时间 >= 截止时间，则返回true，表示已截止
     *
     * @param rewardEndTime
     * @return
     */
    private boolean judgeSameDay(Date rewardEndTime) throws ParseException {
        Date nowDate = DateUtil.getNowDateShort();
        Date yesterdayTime = new Date(nowDate.getTime() - 1000L);
        String yesterdayTimeStr = DateUtil.getDefaultFormatDate(yesterdayTime);
        String rewardEndTimeStr = DateUtil.getDefaultFormatDate(rewardEndTime);
        return yesterdayTimeStr.equals(rewardEndTimeStr);
    }

    /**
     * 统计商户今日交易金额
     *
     * @param order
     * @param type  统计交易表的金额,目的是给:1.xhlf_activity_order累计金额,2.xhlf_activity_merchant_order累计金额
     * @return
     */
    private BigDecimal queryMerchantTransTotal(XhlfActivityOrder order, String type) throws ParseException {
        BigDecimal merchantTodayTrans = null;
        AddCreaditcardLog addCreaditcardLog;
        log.info("交易统计类型[{}]考核周期[{}]",type,order.getCurrentCycle());
        switch (type) {
            case "1":
                merchantTodayTrans = xhlfActivityOrderDao.queryTransTotal(order);
                break;
            case "2":
                merchantTodayTrans = xhlfActivityOrderDao.queryMerchantTransTotal(order);
                break;
            case "7":
                //第一周期绑定信用卡后的所有信用卡刷卡交易,且交易卡未在被其他活动商户交易
                //xhlf_merchant_trans_card：新欢乐送商户交易卡信息表
                //not exists (select 1 from xhlf_merchant_trans_card card where card.card_no = '交易卡号' and card.merchant_no <> '商户号')

                if("1".equals(order.getCurrentCycle()) || "1.1".equals(order.getCurrentCycle())){
                    //先查询出第一次绑卡时间
                    addCreaditcardLog = addCreaditcardLogDao.selectFirstMerchantCreditcard(order.getMerchantNo());
                    if(addCreaditcardLog == null || addCreaditcardLog.getCreateTime() == null) {
                        break;
                    }
                    //如果绑卡时间大于交易统计开始时间，则以绑卡时间为交易统计开始时间
                    //绑卡时间2019-11-25 06:00:00 ，交易统计开始时间2019-11-25 06:00:00，则交易统计开始时间为2019-11-25 06:00:00
                    if(!DateUtil.compare(addCreaditcardLog.getCreateTime(), order.getTransStartTime())) {
                        order.setTransStartTime(addCreaditcardLog.getCreateTime());
                    }
                    //根据商户号查询今天的交易list，且交易卡不在别人的xhlf_merchant_trans_card表里
                    List<CollectiveTransOrder> transList = xhlfActivityOrderDao.queryMerchantTransListType7(order);
                    if(transList == null || transList.isEmpty()) {
                        break;
                    }
                    merchantTodayTrans = BigDecimal.ZERO;
                    List<XhlfMerchantTransCard> xhlfMerchantTransCardList = new ArrayList<>();
                    Set<String> bankIdList = new HashSet<>();
                    for(CollectiveTransOrder itemTrans: transList) {
                        merchantTodayTrans = merchantTodayTrans.add(itemTrans.getTransAmount());
                        if(StringUtil.isNotBlank(itemTrans.getAccountNo()) && !bankIdList.contains(itemTrans.getAccountNo())) {
                            //根据bankCardId查询是否已存在，不存在则要保存
                            XhlfMerchantTransCard itemCardInfo = xhlfActivityOrderDao.queryMerchantTransCard(order, itemTrans.getAccountNo());
                            if(itemCardInfo != null) {
                                continue;
                            }
                            bankIdList.add(itemTrans.getAccountNo());

                            XhlfMerchantTransCard xhlfMerchantTransCard = new XhlfMerchantTransCard();
                            xhlfMerchantTransCard.setAccountNo(itemTrans.getAccountNo());
                            xhlfMerchantTransCardList.add(xhlfMerchantTransCard);
                        }
                    }
                    if (xhlfMerchantTransCardList != null && xhlfMerchantTransCardList.size() > 0) {
                        xhlfActivityOrderDao.insertxhlfMerchantTransCard(order, xhlfMerchantTransCardList);
                    }

                } else {
                    //后续所有信用卡pos刷卡
                    merchantTodayTrans = xhlfActivityOrderDao.queryMerchantCreditTransTotal(order);
                }
                break;
            case "8":
                merchantTodayTrans = xhlfActivityOrderDao.querySmartTransTotal(order);
                break;
            case "9":
                //TODO 新欢乐送活动新增交易量统计方式 （9）
                //第一周期绑定信用卡后的所有信用卡刷卡交易,且交易卡未在被其他活动商户交易
                //xhlf_merchant_trans_card：新欢乐送商户交易卡信息表
                //not exists (select 1 from xhlf_merchant_trans_card card where card.card_no = '交易卡号' and card.merchant_no <> '商户号')
                if("1".equals(order.getCurrentCycle()) || "1.1".equals(order.getCurrentCycle())){
                    //先查询出第一次绑卡时间
                    addCreaditcardLog = addCreaditcardLogDao.selectFirstMerchantCreditcard(order.getMerchantNo());
                    if(addCreaditcardLog == null || addCreaditcardLog.getCreateTime() == null) {
                        break;
                    }
                    //如果绑卡时间大于交易统计开始时间，则以绑卡时间为交易统计开始时间
                    //绑卡时间2019-11-25 06:00:00 ，交易统计开始时间2019-11-25 06:00:00，则交易统计开始时间为2019-11-25 06:00:00
                    if(!DateUtil.compare(addCreaditcardLog.getCreateTime(), order.getTransStartTime())) {
                        order.setTransStartTime(addCreaditcardLog.getCreateTime());
                    }
                    //根据商户号查询今天的交易list，且交易卡不在别人的xhlf_merchant_trans_card表里
                    List<CollectiveTransOrder> transList = xhlfActivityOrderDao.queryMerchantTransListType9(order);
                    log.info("满足统计要求的交易量[{}]",transList.size());
                    if(transList == null || transList.isEmpty()) {
                        break;
                    }
                    merchantTodayTrans = BigDecimal.ZERO;
                    List<XhlfMerchantTransCard> xhlfMerchantTransCardList = new ArrayList<>();
                    Set<String> bankIdList = new HashSet<>();
                    for(CollectiveTransOrder itemTrans: transList) {
                        merchantTodayTrans = merchantTodayTrans.add(itemTrans.getTransAmount());
                        if(StringUtil.isNotBlank(itemTrans.getAccountNo()) && !bankIdList.contains(itemTrans.getAccountNo())) {
                            //根据bankCardId查询是否已存在，不存在则要保存
                            XhlfMerchantTransCard itemCardInfo = xhlfActivityOrderDao.queryMerchantTransCard(order, itemTrans.getAccountNo());
                            if(itemCardInfo != null) {
                                continue;
                            }
                            bankIdList.add(itemTrans.getAccountNo());

                            XhlfMerchantTransCard xhlfMerchantTransCard = new XhlfMerchantTransCard();
                            xhlfMerchantTransCard.setAccountNo(itemTrans.getAccountNo());
                            xhlfMerchantTransCardList.add(xhlfMerchantTransCard);
                        }
                    }
                    if (xhlfMerchantTransCardList != null && xhlfMerchantTransCardList.size() > 0) {
                        xhlfActivityOrderDao.insertxhlfMerchantTransCard(order, xhlfMerchantTransCardList);
                    }

                } else {
                    //后续所有信用卡pos刷卡
                    merchantTodayTrans = xhlfActivityOrderDao.queryMerchantCreditTransTotal(order);
                }
                break;
        }
        if (merchantTodayTrans == null) {
            merchantTodayTrans = BigDecimal.ZERO;
        }
        return merchantTodayTrans;
    }

}
