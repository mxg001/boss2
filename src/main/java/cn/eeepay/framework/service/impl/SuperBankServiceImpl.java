package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.thread.AgentAccountRunnable;
import cn.eeepay.boss.thread.LotteryGenOrderAndProfitRunnable;
import cn.eeepay.boss.thread.SuperBankCheckProfitRunnable;
import cn.eeepay.boss.thread.SuperBankPushRunnable;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.AgentOemInfoDao;
import cn.eeepay.framework.dao.MerchantBusinessProductDao;
import cn.eeepay.framework.dao.PosCardBinDao;
import cn.eeepay.framework.daoSuperbank.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.enums.OperType;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.ListUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("superBankService")
@Transactional
public class SuperBankServiceImpl implements SuperBankService {

    private final static Logger log = LoggerFactory.getLogger(SuperBankServiceImpl.class);

    private final static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

    @Resource
    private SysDictService sysDictService;

    @Resource
    private PyIdentificationService pyIdentificationService;

    @Resource
	private OrgSourceConfigService orgSourceConfigService;

    @Resource
    private TranslateRowImpl translateRowImpl;

    @Resource
    private CreditRowImpl creditRowImpl;

    @Resource
    private LotteryRowImpl lotteryRowImpl;

    @Resource
    private SportLotteryRowImpl sportLotteryRowImpl;

    @Resource
    private OrgInfoDao orgInfoDao;

    @Resource
    private CreditCardBonusDao creditCardDao;

    @Resource
    private OrderMainDao orderMainDao;

    @Resource
    private SuperBankUserInfoDao superBankUserInfoDao;

    @Resource
    private UserProfitDao userProfitDao;

    @Resource
    private LoanSourceDao loanSourceDao;

    @Resource
    private LoanCompanyDao loanCompanyDao;

    @Resource
    private LotteryBonusConfDao lotteryBonusConfDao;

    @Resource
    private LotteryImportRecordsDao lotteryImportRecordsDao;

    @Resource
    private UserObtainRecordDao userObtainRecordDao;

    @Resource
    private OrgProfitConfDao orgProfitConfDao;

    @Resource
    private MerchantBusinessProductDao merchantBusinessProductDao;

    @Resource
    private PosCardBinDao posCardBinDao;

    @Resource
    private NoticeDao noticeDao;

    @Resource
    private AdDao adDao;

    @Resource
    private SequenceDao sequenceDao;

    @Resource
    private AgentInfoDao agentInfoDao;

    @Resource
    private OpenPlatformService openPlatformService;

    @Resource
    private RechargeRecordDao rechargeRecordDao;

    @Resource
    private CreditcardApplyRecordDao creditcardApplyRecordDao;

    @Resource
    private SysOptionDao sysOptionDao;

    @Resource
    private AgentOemInfoDao agentOemInfoDao;


    @Resource
    private LotteryOrderDao lotteryOrderDao;

    @Resource
    private OrgInfoOpenConfDao orgInfoOpenConfDao;

    @Resource
    private OpenFunctionConfDao openFunctionConfDao;

    @Resource
    private ZxProductOrderService zxProductOrderService;

    @Resource
    private RankingPushRecordDao rankingPushRecordDao;

    @Resource
    private RankingRecordDao rankingRecordDao;

    @Resource
    private RankingRuleDao rankingRuleDao;

    @Resource
    private RankingRecordDetailDao rankingRecordDetailDao;
    @Resource
    private RankingRuleLevelDao rankingRuleLevelDao;

    @Resource
    private RankingRuleHistoryDao rankingRuleHistoryDao;

    @Resource
    private CarOrderDao carOrderDao;

    @Resource
    private CarOrderProfitConfDao carOrderProfitConfDao;

    @Resource
    private InsuranceOrderService insuranceOrderService;

    @Resource
    private SuperExcOrderService superExcOrderService;

    @Resource
    private PageViewRecordDao pageViewRecordDao;

    @Resource
    private DataAnalysisRecordDao dataAnalysisRecordDao;

    @Resource
    private UserDistributionRecordDao userDistributionRecordDao;

    @Resource
    private CreditloanEnterDataDao creditloanEnterDataDao;

    @Resource
    private BusinessPageViewDataDao businessPageViewDataDao;

    @Resource
    private OrgWxTemplateDao orgWxTemplateDao;

    @Resource
    private TSysOptionDao tSysOptionDao;

    @Resource
    private OrgSourceConfDao orgSourceConfDao;

    @Resource
    private BusinessConfDao businessConfDao;

    @Resource
    private CreditcardSourceDao creditcardSourceDao;

    @Resource
    private OrgBusinessConfDao orgBusinessConfDao;

    @Resource
    private  YsService ysService;

    @Resource
    private ModulesNewStyleDao modulesNewStyleDao;

    /**
     * 用户管理
     * @param baseInfo
     * @param page
     * @return
     */
    @Override
    public List<SuperBankUserInfo> selectUserInfoPage(SuperBankUserInfo baseInfo, Page<SuperBankUserInfo> page) {
        superBankUserInfoDao.selectUserInfoPage(baseInfo, page);
        List<SuperBankUserInfo> list = page.getResult();
        filterUserInfo(list);
        return list;
    }

    /**
     * 转换用户list的部分信息
     * @param list
     */
    public void filterUserInfo(List<SuperBankUserInfo> list) {
        if (list != null && list.size() > 0) {
            Map<Long, String> orgNameMap = getOrgNameMap();
            Map<String, String> userTypeMap = getUserTypeMap();
            Map<String, String> payBackMap = getPayBackMap();
            Map<String, String> openAccountMap = getOpenAccountMap();
            Map<String, String> payMoneyMap = getPayMoneyStatusMap();
            Map<String, String> mentorMap = getMentorMap();
            for (SuperBankUserInfo userInfo : list) {
                if (StringUtils.isNotBlank(userInfo.getUserType())) {
                    if ("10".equals(userInfo.getUserType())) {
                        userInfo.setPayMoneyStatus("未缴费");
                    } else {
                        userInfo.setPayMoneyStatus("已缴费");
                    }
                }
                userInfo.setOrgName(orgNameMap.get(userInfo.getOrgId()));
                userInfo.setUserType(userTypeMap.get(userInfo.getUserType()));
                userInfo.setPayBack(payBackMap.get(userInfo.getPayBack()));
                userInfo.setAccountStatus(openAccountMap.get(userInfo.getAccountStatus()));
                userInfo.setStatusMentor(mentorMap.get(userInfo.getStatusMentor()));
                String userCode = userInfo.getUserCode();
                if (StringUtils.isNotBlank(userCode)) {
                    String userAmount = superBankUserInfoDao.findUserAmount(userCode);
                    userAmount = userAmount == null ? "0.00" : userAmount;
                    userInfo.setTotalProfit(new BigDecimal(userAmount));
                }
                if (StringUtils.isNotBlank(userInfo.getNickName())) {
                    try {
                        userInfo.setNickName(URLDecoder.decode(userInfo.getNickName(), "utf-8"));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 用户详情
     * @param userCode
     * @return
     */
    @Override
    public Result selectUserDetail(String userCode) {
        Result result = new Result();
        Map<String, Object> map = new HashMap<>();

        SuperBankUserInfo userInfo = getSuperBankUserInfo(userCode);
        //1.设置用户的基本信息
        setUserBaseInfo(userCode, userInfo);
        //2.设置用户的结算卡信息
        SuperBankUserCard userCard = getUserCard(userCode);
        //3.还款业务，userInfo表本身有超级还商户号，页面设置跳转链接即可
        //4.收款业务，找到进件ID，页面设置跳转链接即可
        setUserItemNo(userInfo);
        map.put("userInfo", userInfo);
        map.put("userCard", userCard);
        result.setStatus(true);
        result.setMsg("查询成功");
        result.setData(map);
        return result;
    }

    /**
     * 用户账户信息
     * @param userCode
     * @return
     */
    @Override
    public Result getUserAccountInfo(String userCode) {
        Result result = new Result();
        result.setMsg("获取用户账户信息失败");
        String subjectNo = Constants.SUPER_BANK_SUBJECT_NO;//超级银行家用户账户的科目号
        String merAccountStr = ClientInterface.getMerchantAccountBalance(userCode, subjectNo);
        if (StringUtils.isNotBlank(merAccountStr)) {
            JSONObject merAccountJson = JSON.parseObject(merAccountStr);
            if (merAccountJson != null && merAccountJson.getBoolean("status")) {
                AccountInfo accountInfo = JSONObject.parseObject(merAccountStr, AccountInfo.class);
                List<AccountInfo> list = new ArrayList<>();
                list.add(accountInfo);
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(list);
            }
        }
        return result;
    }

    /**
     * 组织账户信息
     */
    @Override
    public Result getOrgAccountInfo(String userCode) {
        Result result = new Result();
        result.setMsg("获取用户账户信息失败");
        String subjectNo = Constants.SUPER_BANK_AGENT_SUBJECT_NO;//超级银行家用户账户的科目号
        String merAccountStr = ClientInterface.getSuperBankAccountBalance(userCode, subjectNo);
        if (StringUtils.isNotBlank(merAccountStr)) {
            JSONObject merAccountJson = JSON.parseObject(merAccountStr);
            if (merAccountJson != null && merAccountJson.getBoolean("status")) {
                AccountInfo accountInfo = JSONObject.parseObject(merAccountStr, AccountInfo.class);
                List<AccountInfo> list = new ArrayList<>();
                list.add(accountInfo);
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(list);
            }
        }
        return result;
    }

    /**
     * 用户账户明细
     * @param record
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Result getAccountTranInfo(AccountInfoRecord record, int pageNo, int pageSize) {
        Result result = new Result();
        result.setMsg("获取用户账户明细失败");
        String merchantNo = record.getUserId();
        String recordTimeStart = record.getRecordTimeStart();
        String recordTimeEnd = record.getRecordTimeEnd();
        String debitCreditSide = record.getDebitCreditSide();
        String subjectNo = Constants.SUPER_BANK_SUBJECT_NO;
        String accountDetailStr = ClientInterface.selectMerchantAccountTransInfoList(
                merchantNo, subjectNo, recordTimeStart, recordTimeEnd, debitCreditSide, pageNo, pageSize);
        if (StringUtils.isNotBlank(accountDetailStr)) {
            JSONObject accountDetailJson = JSON.parseObject(accountDetailStr);
            if (accountDetailJson != null && accountDetailJson.getBoolean("status")) {
                JSONObject json = JSONObject.parseObject(accountDetailJson.getString("data"));
                List<AccountInfoRecord> list = JSONObject.parseArray(json.getString("list"), AccountInfoRecord.class);
                String total = json.getString("total");
                Map<String, Object> map = new HashMap<>();
                map.put("list", list);
                map.put("total", total);
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(map);
            }
        }
        return result;
    }

    /**
     * 修改超级银行家用户
     * @param baseInfo
     * @return
     */
    @Override
    public Result updateUserInfo(SuperBankUserInfo baseInfo) {
        Result result = new Result();
        //1.校验用户参数是否为空
        if (checkUserInfo(baseInfo, result)) return result;
        //2.update用户表
        int i = superBankUserInfoDao.updateUserInfo(baseInfo);
        if (i != 1) {
            throw new BossBaseException("修改超级银行家用户异常");
        }
        result.setStatus(true);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 获取结算卡信息
     * @param cardNo
     * @return
     */
    @Override
    public Result getCardInfo(String cardNo) {
        Result result = new Result();
        PosCardBin cardBin = getPosCardBin(cardNo);
        result.setStatus(true);
        result.setMsg("查询成功");
        result.setData(cardBin);
        return result;
    }

    /**
     * 获取支行信息
     * @param bankName
     * @param cityName
     * @return
     */
    @Override
    public Result getPosCnaps(String bankName, String cityName) {
        Result result = new Result();
        if (StringUtils.isBlank(bankName)
                || StringUtils.isBlank(cityName)) {
            result.setMsg("参数不能为空");
            return result;
        }
        List<PosCnaps> cardBin = getPosCnapsList(bankName, cityName);
        result.setStatus(true);
        result.setMsg("查询成功");
        result.setData(cardBin);
        return result;
    }

    /**
     * 根据卡号和城市名称，获取支行列表
     * @param bankName
     * @param cityName
     * @return
     */
    private List<PosCnaps> getPosCnapsList(String bankName, String cityName) {
        if (StringUtils.isBlank(bankName)
                || StringUtils.isBlank(cityName)) {
            throw new BossBaseException("参数不能为空");
        }
        if (cityName.endsWith("市")) {
            cityName = cityName.substring(0, cityName.length() - 1);
        }
        String percent = "%";
        bankName = percent.concat(bankName).concat(percent);
        cityName = percent.concat(cityName).concat(percent);
        return posCardBinDao.getPosCnapsList(bankName, cityName);
    }

    /**
     * 获取结算卡信息
     * @param cardNo
     * @return
     */
    private PosCardBin getPosCardBin(String cardNo) {
        PosCardBin cardBin = null;
        if (StringUtils.isBlank(cardNo) || !StringUtils.isNumeric(cardNo)) {
            throw new BossBaseException("请输入正确的卡号");
        }
        cardBin = posCardBinDao.queryInfo(cardNo);
        if (cardBin == null || "贷记卡".equals(cardBin.getCardType())) {
            throw new BossBaseException("请输入正确的卡号");
        }
        return cardBin;
    }

    /**
     * 修改超级银行家结算卡
     * @param baseInfo
     * @return
     */
    @Override
    public Result updateUserCard(SuperBankUserCard baseInfo) {
        Result result = new Result();
        //0.校验baseInfo参数是否为空
        if (checkUserCard(baseInfo, result)) return result;
        SuperBankUserCard oldUserCard = superBankUserInfoDao.selectSuperBankUserCard(String.valueOf(baseInfo.getUserCode()));
        //0.5实名认证
        if (checkBank_Name_IDCard(baseInfo, result, oldUserCard)) return result;
        //1.向用户结算卡变更记录表插入一条记录
        insertUserCardRecord(oldUserCard);
        //2.修改用户结算卡信息，user_card
        int i = superBankUserInfoDao.updateUserCard(baseInfo);
        if(i != 1){
            throw new BossBaseException("修改超级银行家用户结算卡异常");
        }
        result.setStatus(true);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 修改结算卡时，调用实名认证接口
     * @param baseInfo
     * @param result
     * @param oldUserCard
     * @return
     */
    private boolean checkBank_Name_IDCard(SuperBankUserCard baseInfo, Result result, SuperBankUserCard oldUserCard) {
        String identityId = oldUserCard.getAccountIdNo();//身份证号
        String accountNo = baseInfo.getCardNo();//新的卡号
        String accountName = oldUserCard.getAccountName();//开户名
        String cnapsNo = baseInfo.getCnapsNo();//联行行号
        if(StringUtils.isBlank(identityId)){
            result.setMsg("身份证号不能为空");
            return true;
        }
        if(StringUtils.isBlank(accountNo)){
            result.setMsg("结算卡号不能为空");
            return true;
        }
        if(StringUtils.isBlank(accountName)){
            result.setMsg("开户名不能为空");
            return true;
        }
        //实名认证，如果没有校验历史，要进行实名认证
        PyIdentification pyIdentification = pyIdentificationService.queryByCheckInfo(accountName, identityId, accountNo);
        if(pyIdentification == null || !"1".equals(pyIdentification.getIdentiStatus().toString())){
//            Map<String, String> checkParamMap = new HashMap<String, String>();
//            checkParamMap.put("identityId", identityId);
//            checkParamMap.put("accountNo", accountNo);
//            checkParamMap.put("accountName", accountName);
//            checkParamMap.put("cnapsNo", cnapsNo);
            Map<String, String> checkResultMap=openPlatformService.doAuthen(accountNo,accountName,identityId,null);

            String errCode = checkResultMap.get("errCode");
//            String errMsg_ = checkResultMap.get("errMsg");
            boolean flag = "00".equalsIgnoreCase(errCode);
            if (!flag) {
                result.setMsg("开户名、身份证、银行卡号不匹配");
                return true;
            }
        }
        return false;
    }

//    /**
//     * 实名认证
//     * @param params
//     * identityId 身份证号
//     * accountNo 卡号
//     * accountName 开户名
//     * cnapsNo 联行行号
//     * @return
//     */
//    @Override
//    public Map<String, String> checkBank_Name_IDCard(Map<String, String> params) {
//        log.info("验证身份证、开户名、银行卡：" + params);
//        String ip ="http://www.yfbpay.cn/boss/api/checkMain";
//        StringBuffer url = new StringBuffer(ip);
//        url.append("?");
//        String responseBody = null;
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("errCode", "faild");
//        map.put("errMsg", "开户名+账号+证件号码,校验失败");
//        map.put("exceptionMsg", "--");
//        try {
//            if(params == null){
//                map.put("errMsg", "参数不能为空");
//                return map;
//            }
//
//            String identityId = params.get("identityId");//身份证号码
//            String accNo = params.get("accountNo");//银行卡号
//            String accName = params.get("accountName"); //开户名称
//            if(StringUtils.isBlank(identityId)
//                    || StringUtils.isBlank(accNo)
//                    || StringUtils.isBlank(accName)){
//                map.put("errMsg", "开户名|账号|证件号码，不能为空");
//                return map;
//            }
//            //(1:验证账号+户名  2：验证账号+户名+证件号)
//            String verifyType = "2";
//            verifyType = (StringUtils.isEmpty(verifyType)) ? "2" : verifyType;
//            url.append("verifyType=").append(verifyType);
//
//            //标识为AGENT 代理商系统访问接口
//            String channel = params.get("channel");
//            channel = (StringUtils.isEmpty(channel)) ? "AGENT" : channel;
//            url.append("&channel=").append(channel);
//
//            url.append("&identityId=").append(identityId);
//            url.append("&accNo=").append(URLEncoder.encode(accNo, "UTF-8"));
//            url.append("&accName=").append(URLEncoder.encode(accName, "UTF-8"));
//
//            //清算联行号
//            String cnapsNo = params.get("cnapsNo");
//            if(StringUtils.isEmpty(cnapsNo)){ //如果没有传入清算联行号，则从 CardBin 中取
//                try {
//                    String bankNoTemp = posCardBinService.queryBankNo(accNo);
//                    if(bankNoTemp.equals("0")||bankNoTemp.equals("1")){
//                        cnapsNo = null;
//                    }else{
//                        cnapsNo = bankNoTemp;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            url.append("&bankNo=").append(cnapsNo);
//            String finalUrl = url.toString();
//            log.info("验证身份证、开户名、银行卡，最终URL："+finalUrl);
//
//            responseBody = ClientInterface.postRequest(finalUrl);
//            String errCode = responseBody.substring(responseBody.indexOf("<errCode>")+9, responseBody.indexOf("</errCode>"));
//            String errMsg = responseBody.substring(responseBody.indexOf("<errMsg>")+8, responseBody.indexOf("</errMsg>"));
//            map.put("errCode", errCode);
//            map.put("errMsg", errMsg);
//        } catch (IOException e) {
//            log.info("验证身份证、开户名、银行卡：其他异常<IOException>");
//            e.printStackTrace();
//        }
//        return map;
//    }

    /**
     * 获取直营的初始化信息
     * 费率信息、代理升级方案、代理退费方案
     * 普通用户奖励方案，分润账户留存金额
     * @return
     */
    @Override
    public OrgInfo getBaseOrgInfo() {
        String v2Orgcode = "610010";//直营组织的组织ID
        OrgInfo orgInfo = orgInfoDao.selectBaseDetail(v2Orgcode);
        return orgInfo;
    }

    /**
     * 向用户结算卡变更记录表插入一条记录
     * @param oldUserCard
     */
    private void insertUserCardRecord(SuperBankUserCard oldUserCard) {
        //1.查询修改之前的用户结算卡信息
        if(oldUserCard == null){
            throw new BossBaseException("修改超级银行家用户结算卡异常");
        }
        //2.向用户结算卡变更记录表插入一条记录，user_card_record
        SuperBankUserCardRecord record = new SuperBankUserCardRecord();
        record.setUserCode(oldUserCard.getUserCode());
        BeanUtils.copyProperties(oldUserCard, record);//复制userCode、cardNo、bankName、bankBranchName、bankAdress
        record.setRecordDate(new Date());//记录时间
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        record.setUpdateBy(String.valueOf(loginUser.getId()));
        record.setUpdateSource("1");//修改来源，1：boss，2：h5
        superBankUserInfoDao.insertUserCardRecord(record);
    }

    /**
     * 修改用户结算卡之前，校验参数是否为空
     * 校验属性：userCode、cardNo
     * @param baseInfo
     * @param result
     * @return
     */
    private boolean checkUserCard(SuperBankUserCard baseInfo, Result result) {
        if(baseInfo == null ){
            result.setMsg("参数不能为空");
            return true;
        }
        if(baseInfo.getId() == null){
            result.setMsg("结算卡编号不能为空");
            return true;
        }
        if(baseInfo.getUserCode() == null){
            result.setMsg("用户编号不能为空");
            return true;
        }
        if(StringUtils.isBlank(baseInfo.getCardNo())){
            result.setMsg("结算卡号不能为空");
            return true;
        }
        return false;
    }

    /**
     * 检查用户参数是否为空
     * 校验属性：userCode、姓名、手机号、微信号
     * @param baseInfo
     * @param result
     * @return
     */
    private boolean checkUserInfo(SuperBankUserInfo baseInfo, Result result) {
        if(baseInfo == null){
            result.setMsg("参数不能为空");
            return true;
        }
        if(StringUtils.isBlank(baseInfo.getUserCode())){
            result.setMsg("用户编号不能为空");
            return true;
        }
        if(StringUtils.isBlank(baseInfo.getUserName())){
            result.setMsg("姓名不能为空");
            return true;
        }
        if(StringUtils.isBlank(baseInfo.getPhone())){
            result.setMsg("手机号不能为空");
            return true;
        }
        if(StringUtils.isBlank(baseInfo.getWeixinCode())){
            result.setMsg("微信号不能为空");
            return true;
        }
        return false;
    }

    /**
     * 设置用户的基本信息
     * @param userCode
     * @param userInfo
     */
    private void setUserBaseInfo(String userCode, SuperBankUserInfo userInfo) {
        if(userInfo != null){
            //1.设置组织名称
            setUserOrgName(userInfo);
            //2.一、二、三级用户信息
            setSuperUser(userInfo);
            //3.用户、一、二、三级身份
            filterUserType(userInfo);
            //4.代理状态、退款状态，收款状态、还款状态
            filterUserStatus(userInfo);
            //5.微信头像、微信二维码
            setUserImg(userInfo);
            //6.用户下级统计
            setUserSubCount(userCode, userInfo);
            //7.获取用户总收益
            BigDecimal totalAmount = superBankUserInfoDao.getTotalAmount(userCode);
            //8设置导师
            setMentor(userInfo);
            if(totalAmount != null){
                userInfo.setTotalProfit(totalAmount);
            } else {
                userInfo.setTotalProfit(BigDecimal.ZERO);
            }
            //过滤昵称乱码
            if(StringUtils.isNotBlank(userInfo.getNickName())){
                try {
                    userInfo.setNickName(URLDecoder.decode(userInfo.getNickName(), "utf-8"));
                } catch (Exception e) {
                }
            }
            if(StringUtils.isNotBlank(userInfo.getTopOneNickName())){
                try {
                    userInfo.setTopOneNickName(URLDecoder.decode(userInfo.getTopOneNickName(), "utf-8"));
                } catch (Exception e) {
                }
            }
            if(StringUtils.isNotBlank(userInfo.getTopTwoNickName())){
                try {
                    userInfo.setTopTwoNickName(URLDecoder.decode(userInfo.getTopTwoNickName(), "utf-8"));
                } catch (Exception e) {
                }
            }
            if(StringUtils.isNotBlank(userInfo.getTopThreeNickName())){
                try {
                    userInfo.setTopThreeNickName(URLDecoder.decode(userInfo.getTopThreeNickName(), "utf-8"));
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 获取用户
     * @param userCode
     * @return
     */
    private SuperBankUserInfo getSuperBankUserInfo(String userCode) {
        if(StringUtils.isBlank(userCode)){
            throw new BossBaseException("参数不能为空");
        }
        SuperBankUserInfo userInfo = superBankUserInfoDao.selectDetail(userCode);
        if(userInfo == null){
            throw new BossBaseException("找不到对应的用户");
        }
        return userInfo;
    }

    /**
     * 设置用户的V2进件ID
     * @param userInfo
     */
    private void setUserItemNo(SuperBankUserInfo userInfo) {
        String bpId = "";
        SysDict bpSysDcit = sysDictService.getByKey("SUPER_BANK_BP_ID");
        if(bpSysDcit == null || StringUtils.isBlank(bpSysDcit.getSysValue())){
            throw new BossBaseException("超级银行家业务产品的数据字典未配置");
        }
        bpId = bpSysDcit.getSysValue();
        MerchantBusinessProduct merchantBusinessProduct = merchantBusinessProductDao.selectMerBusPro(userInfo.getReceiveUserNo(),bpId);
        if (merchantBusinessProduct != null) {
            userInfo.setItemId(String.valueOf(merchantBusinessProduct.getId()));
        }
    }

    /**
     * 获取用户的结算卡信息
     * @param userCode
     * @return
     */
    private SuperBankUserCard getUserCard(String userCode) {
        SuperBankUserCard userCard = superBankUserInfoDao.selectSuperBankUserCard(userCode);
        if(userCard != null && StringUtils.isNotBlank(userCard.getPositivePhoto())){
            userCard.setPositivePhotoUrl(CommonUtil.getImgUrl(userCard.getPositivePhoto()));
        }
        return userCard;
    }

    /**
     * 设置用户的下级统计信息
     * @param userCode
     * @param userInfo
     */
    private void setUserSubCount(String userCode, SuperBankUserInfo userInfo) {
        Integer subCount = superBankUserInfoDao.countSub(userCode);//直属下级总数
        Integer subUserCount = superBankUserInfoDao.countSubUser(userCode);//直属用户数量
        Integer subAgentCount = subCount - subUserCount;//直属代理数量 = 直属下级总数 - 直属用户数量
        userInfo.setSubUserCount(subUserCount);
        userInfo.setSubAgentCount(subAgentCount);
    }

    /**
     * 设置用户图片的url
     * 根据图片的地址，去阿里云获取对应的url
     * @param userInfo
     */
    private void setMentor(SuperBankUserInfo userInfo) {
        String mentor = userInfo.getStatusMentor();
        if(StringUtils.isNotBlank(mentor)){
            userInfo.setStatusMentorName(getMentorMap().get(mentor));
        }
    }
    /**
     * 设置用户图片的url
     * 根据图片的地址，去阿里云获取对应的url
     * @param userInfo
     */
    private void setUserImg(SuperBankUserInfo userInfo) {
        String userLog = userInfo.getUserLogo();
        String qrCode = userInfo.getQrCode();
        if(StringUtils.isNotBlank(userLog)){
//            userInfo.setUserLogoUrl(CommonUtil.getImgUrl(userLog));
            userInfo.setUserLogoUrl(userLog);
        }
        if(StringUtils.isNotBlank(qrCode)){
            userInfo.setQrCodeUrl(CommonUtil.getImgUrl(qrCode));
//            userInfo.setQrCodeUrl(qrCode);
        }
    }


    /**
     * 过滤用户的相关状态
     * 1.代理开通状态，2.退款状态，3.还款开通状态，4.收款开通状态
     * @param userInfo
     */
    private void filterUserStatus(SuperBankUserInfo userInfo) {
        Map<String, String> agentStatusMap = getAgentStatusMap();
        Map<String, String> payBackMap = getPayBackMap();
        Map<String, String> repayStatusMap = getRepayStatusMap();
        Map<String, String> receiveStatusMap = getReceiveStatusMap();
        userInfo.setStatusAgent(agentStatusMap.get(userInfo.getStatusAgent()));
        userInfo.setPayBackStr(payBackMap.get(userInfo.getPayBack()));
        userInfo.setStatusRepayment(repayStatusMap.get(userInfo.getStatusRepayment()));
        userInfo.setStatusReceive(receiveStatusMap.get(userInfo.getStatusReceive()));
    }

    /**
     * 过滤用户和上级的身份
     * @param userInfo
     */
    private void filterUserType(SuperBankUserInfo userInfo) {
        Map<String, String> userTypeMap = getUserTypeMap();
        userInfo.setUserType(userTypeMap.get(userInfo.getUserType()));
        userInfo.setTopOneUserType(userTypeMap.get(userInfo.getTopOneUserType()));
        userInfo.setTopTwoUserType(userTypeMap.get(userInfo.getTopTwoUserType()));
        userInfo.setTopThreeUserType(userTypeMap.get(userInfo.getTopThreeUserType()));
    }

    /**
     * 设置上级相关信息，一、二、三级
     * @param userInfo
     */
    private void setSuperUser(SuperBankUserInfo userInfo) {
        String userCode = userInfo.getUserCode();
        String topOneCode = userInfo.getTopOneCode();
        String topTwoCode = userInfo.getTopTwoCode();
        String topThrCode = userInfo.getTopThreeCode();
        if(StringUtils.isNotBlank(userCode)){
            SuperBankUserInfo tempUser = superBankUserInfoDao.selectUser(userCode);
            if(tempUser != null){
                userInfo.setUserName(tempUser.getUserName());
                userInfo.setNickName(tempUser.getNickName());
                userInfo.setUserType(tempUser.getUserType());
            }
        }
        if(StringUtils.isNotBlank(topOneCode)){
            SuperBankUserInfo tempUser = superBankUserInfoDao.selectUser(topOneCode);
            if(tempUser != null){
                userInfo.setTopOneUserName(tempUser.getUserName());
                userInfo.setTopOneNickName(tempUser.getNickName());
                userInfo.setTopOneUserType(tempUser.getUserType());
            }
        }
        if(StringUtils.isNotBlank(topTwoCode)){
            SuperBankUserInfo tempUser = superBankUserInfoDao.selectUser(topTwoCode);
            if(tempUser != null){
                userInfo.setTopTwoUserName(tempUser.getUserName());
                userInfo.setTopTwoNickName(tempUser.getNickName());
                userInfo.setTopTwoUserType(tempUser.getUserType());
            }
        }
        if(StringUtils.isNotBlank(topThrCode)){
            SuperBankUserInfo tempUser = superBankUserInfoDao.selectUser(topThrCode);
            if(tempUser != null){
                userInfo.setTopThreeUserName(tempUser.getUserName());
                userInfo.setTopThreeNickName(tempUser.getNickName());
                userInfo.setTopThreeUserType(tempUser.getUserType());
            }
        }
    }

    /**
     * 设置组织名称
     * @param userInfo
     */
    private void setUserOrgName(SuperBankUserInfo userInfo) {
        Long orgId = userInfo.getOrgId();
        if(orgId != null){
            OrgInfo orgInfo = orgInfoDao.selectOrg(orgId);
            if(orgInfo != null){
                userInfo.setOrgName(orgInfo.getOrgName());
                if(orgInfo.getIsOpen()==null){
                    userInfo.setIsOpen("否");
                }else {
                    if("1".equals(orgInfo.getIsOpen())){
                        userInfo.setIsOpen("是");
                    }else{
                        userInfo.setIsOpen("否");
                    }
                }
            }
        }
    }

    /**
     * 新增超级银行组织
     * @param orgInfo
     * @return
     */
    @Override
    public Result addOrgInfo(OrgInfo orgInfo) {
        Result result = new Result();
        if (orgInfo == null) {
            result.setMsg("参数不能为空");
            return result;
        }
        if (orgInfo.getOrgId() == null) {
            result.setMsg("orgId不能为空");
            return result;
        }
        if (checkExistsOrgId(orgInfo.getOrgId())) {
            result.setMsg("orgId已存在");
            return result;
        }
        //新增的组织，加上直营组织的相关配置
        addDefaultCreditCardConf(orgInfo.getOrgId());
        //需要调用超级还的接口，开通超级还oem，且回写超级还组织编号、V2对应组织编号
        createRepayOem(orgInfo);
        if ("1".equals(orgInfo.getIsOpen())) {
            if (StringUtils.isNotBlank(orgInfo.getOpenMerchantKey())) {
                String openMerchantKey = RandomStringUtils.randomAlphanumeric(32);
                orgInfo.setOpenMerchantKey(openMerchantKey);
                AgentInfo agentInfo = agentInfoDao.select(orgInfo.getV2AgentNumber());
                StringBuffer context = new StringBuffer();
                context.append(agentInfo.getAgentName()).append(",您已开通综合服务平台权限，请前往代理商平台获取开发资料");
                Sms.sendMsg(agentInfo.getMobilephone(), context.toString());
            } else {
                result.setMsg("请点击随机生成商户应用秘钥");
                return result;
            }
        }
        //插入信息到org_info_open_conf
        if (orgInfo.getIsOpen() != null && "1".equals(orgInfo.getIsOpen())) {
            insertOrgInfoOpenConf(orgInfo);
        }
        List<OrgInfo> orgInfos = orgInfoDao.selectCjdOrgInfo();
        if (orgInfos != null && orgInfos.size() > 0) {
            OrgInfo record = orgInfos.get(0);
            orgInfo.setCjdAppKey(record.getCjdAppKey());
            orgInfo.setCjdOemOn(record.getCjdOemOn());
            orgInfo.setPrivateKey(record.getPrivateKey());
            orgInfo.setPublicKey(record.getPublicKey());
        }
        int num = orgInfoDao.insert(orgInfo);
        if (num == 1) {
            new Thread(new AgentAccountRunnable(orgInfo.getV2AgentNumber(),
                    Constants.SUPER_BANK_AGENT_SUBJECT_NO, this)).start();
        }
        //添加组织成功后，需要把组织加到agent_oem_info表，防止接收到我们的公告
        //先检查该组织是否已存在OEM，如果不存在，则加入
        int count = agentOemInfoDao.checkExists(orgInfo.getOrgId().toString());
        if (count == 0) {
            agentOemInfoDao.insert(orgInfo.getOrgId().toString(), "7");
        }
        //初始化默认组织信用卡贷款机构配置
        createDefultOrgSourceConf(orgInfo.getOrgId());
        result.setStatus(true);
        result.setMsg("添加组织成功，请到信用卡总奖金管理页面，编辑修改信用卡奖励设置");
        return result;
    }

    private void createDefultOrgSourceConf(Long orgId){
        List<CreditcardSource> creditcardSourceList = creditcardSourceDao.getAllBanks();
        List<LoanSource> loanSourceList = loanSourceDao.getLoanList();
        OrgSourceConf conf = new OrgSourceConf();
        conf.setOrgId(orgId.intValue());
        conf.setCreateDate(new Date());
        conf.setIsRecommend("0");
        for (CreditcardSource creditcardSource:creditcardSourceList) {
            conf.setType("1");
            conf.setSourceId(creditcardSource.getId().intValue());
            conf.setShowOrder(StringUtils.isBlank(creditcardSource.getShowOrder())?"0":creditcardSource.getShowOrder());
            conf.setStatus("on");
            conf.setApplication("1");
            orgSourceConfDao.InsertOrgSourceConf(conf);
            conf.setApplication("2");
            orgSourceConfDao.InsertOrgSourceConf(conf);
        }
        for (LoanSource loanSource:loanSourceList) {
            conf.setType("2");
            conf.setSourceId(loanSource.getId().intValue());
            conf.setShowOrder(loanSource.getShowOrder()==null?"0":String.valueOf(loanSource.getShowOrder()));
            conf.setStatus("on");
            conf.setApplication("1");
            orgSourceConfDao.InsertOrgSourceConf(conf);
            conf.setApplication("2");
            orgSourceConfDao.InsertOrgSourceConf(conf);
        }

    }

    /**
     * 开通超级还OEM，获取OEM编号
     * @param orgInfo
     * @return
     */
    private void createRepayOem(OrgInfo orgInfo) {
        String returnStr = ClientInterface.createRepayOem(orgInfo);
        String returnMsg = "操作失败,开通超级还失败";
        if (StringUtils.isBlank(returnStr)) {
            throw new BossBaseException(returnMsg);
        }
        JSONObject json = JSONObject.parseObject(returnStr);
        if (!"200".equals(json.getString("status"))) {
            returnMsg = StringUtils.isNotBlank(json.getString("msg")) ? json.getString("msg") : returnMsg;
            throw new BossBaseException(returnMsg);
        }
        JSONObject data = JSONObject.parseObject(json.getString("data"));
        orgInfo.setV2Orgcode(data.getString("team_id"));
        orgInfo.setSuperOrgcode(data.getString("oem_no"));
    }

    /**
     * 修改超级银行组织
     * @param orgInfo
     * @return
     */
    @Override
    public int updateOrgInfo(OrgInfo orgInfo) {
        // 封装一个方法，校验银行家组织的其他属性格式
        // 校验完毕之后
        Date date = new Date();
        String openMerchantKey = orgInfoDao.checkOpenMerchantKey(orgInfo.getOrgId());
        orgInfo.setUpdateDate(date);
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        orgInfo.setUpdateBy(Long.valueOf(loginUser.getId()));
        if (orgInfo.getIsOpen() != null && "1".equals(orgInfo.getIsOpen())) {
            if (openMerchantKey == null) {
                openMerchantKey = "";
            }
            if (!openMerchantKey.equals(orgInfo.getOpenMerchantKey() == null ? "" : orgInfo.getOpenMerchantKey())) {
                AgentInfo agentInfo = agentInfoDao.select(orgInfo.getV2AgentNumber());
                StringBuffer context = new StringBuffer();
                context.append(agentInfo.getAgentName()).append(",综合服务平台已更新应用密钥，请前往代理商平台同步更新。");
                Sms.sendMsg(agentInfo.getMobilephone(), context.toString());
            }
            List<OrgInfoOpenConf> orgInfoList = orgInfoOpenConfDao.getOrgInfoList(orgInfo.getOrgId());
            if (null == orgInfoList || orgInfoList.size() == 0) {
                insertOrgInfoOpenConf(orgInfo);
            } else {
                List<OpenFunctionConf> listAll = orgInfo.getOpenFunctionConfList();
                long orgId = orgInfo.getOrgId();
                long updateBy = orgInfo.getUpdateBy();
                if (listAll != null && listAll.size() > 0) {
                    List<OrgInfoOpenConf> listUpdate0 = new ArrayList<>();
                    List<OrgInfoOpenConf> listUpdate1 = new ArrayList<>();
                    List<OrgInfoOpenConf> listInsert0 = new ArrayList<>();
                    for (OpenFunctionConf openFunctionConf : listAll) {
                    	//存在更新
                     if(orgInfoOpenConfDao.checkExistsOrgIdAndFuncId(openFunctionConf.getId(),orgId)> 0) {
                        OrgInfoOpenConf record = new OrgInfoOpenConf();
                        record.setOrgId(orgId);
                        record.setUpdateBy(updateBy + "");
                        record.setFunctionId(openFunctionConf.getId() + "");
                        if (1 == openFunctionConf.getIsEnable()) {
                            record.setIsEnable("1");
                            listUpdate1.add(record);
                        } else {
                            record.setIsEnable("0");
                            listUpdate0.add(record);
                        }
                	 }else {
                		 //不存在直接新增
                		 OrgInfoOpenConf insertRecord=new OrgInfoOpenConf();
                		 insertRecord.setFunctionId(openFunctionConf.getId()+"");
                		 insertRecord.setOrgId(orgId);
                		 insertRecord.setCreateBy(updateBy + "");
                         if(1==openFunctionConf.getIsEnable()){
                        	 insertRecord.setIsEnable("1");
                         }else{
                        	 insertRecord.setIsEnable("0");
                         }
                         insertRecord.setCreateDate(new Date());
                         listInsert0.add(insertRecord);
                	 }
                    }
                    List<OrgInfoOpenConf> list = new ArrayList<>();
                    if (listUpdate0.size() > 0) {
                        list.addAll(listUpdate0);
                        orgInfoOpenConfDao.updateAll(list);
                    }
                    if (listUpdate1.size() > 0) {
                        list.clear();
                        list.addAll(listUpdate1);
                        orgInfoOpenConfDao.updateAll(list);
                    }
                    if (listInsert0.size() > 0) {
                        orgInfoOpenConfDao.insertBatch(listInsert0);
                    }

                }
            }
        }
        return orgInfoDao.update(orgInfo);
    }

    /**
     * 银行家组织分页查询
     * @param baseInfo
     * @param page
     * @return
     */
    @Override
    public List<OrgInfo> selectOrgInfoPage(OrgInfo baseInfo, Page<OrgInfo> page) {
        List<OrgInfo> list = orgInfoDao.selectOrgInfoPage(baseInfo, page);
        List<OrgInfo> pageList = page.getResult();
        if (pageList != null && pageList.size() > 0) {
            for (OrgInfo item : pageList) {
                if (item.getReceiveKjCost() != null && !item.getReceiveKjCost().endsWith("%")) {
                    item.setReceiveKjCost(item.getReceiveKjCost() + "%");
                }
                if(item.getReceivePushCost() != null && !item.getReceivePushCost().endsWith("%")){
                    item.setReceivePushCost(item.getReceivePushCost() + "%");
                }
                if(item.getRepaymentCost() != null && !item.getRepaymentCost().endsWith("%")){
                    item.setRepaymentCost(item.getRepaymentCost() + "%");
                }
                if(item.getRepaymentPushCost() != null && !item.getRepaymentPushCost().endsWith("%")){
                    item.setRepaymentPushCost(item.getRepaymentPushCost() + "%");
                }
            }
        }
        return list;
    }

    /**
     * 获取所有的银行组织
     * @return
     */
    @Override
    public List<OrgInfo> getOrgInfoList() {
        return orgInfoDao.getOrgInfoList();
    }

    /**
     * 校验orgId
     * 1.校验orgId在超级银行家组织是否已存在，已存在则返回false
     * 2.从nposp库获取V2组织ID、超级还组织ID
     * @param orgId
     * @return
     */
    @Override
    public Result checkOrgId(String orgId) {
        Result result = new Result();
        boolean checkStatus = checkExistsOrgId(Long.valueOf(orgId));
        if(checkStatus){
            result.setMsg("orgId已存在");
            return result;
        }
        if(!checkExistsAgent(orgId)){
            result.setMsg("代理商不存在");
            return result;
        }
        result.setStatus(true);
        result.setMsg("校验成功");
        return result;
    }

    /**
     * @param agentNo
     * @return
     */
    public boolean checkExistsAgent(String agentNo) {
        if (StringUtils.isBlank(agentNo)) {
            return false;
        }
        AgentInfo info = agentInfoDao.selectByAgentNo(agentNo);
        if(info != null){
            return true;
        }
        return false;
    }

    /**
     * 校验orgId在org_info是否已存在
     * @param orgId
     * @return
     */
    public boolean checkExistsOrgId(Long orgId){
        boolean checkStatus = false;
        Long num = orgInfoDao.checkExistsOrgId(orgId);
        if(num != null && num > 0){
            checkStatus = true;
        }
        return checkStatus;
    }

    @Override
    public LoanSource selectLoanDetail(Long aLong) {
        return loanSourceDao.selectDetail(aLong);
    }
    @Override
    public List<RankingPushRecordInfo> selectRankingPushRecordPage(Map<String, Object> params, Page<RankingPushRecordInfo> page) {
        rankingPushRecordDao.selectRankingPushRecordPage(params, page);
        List<RankingPushRecordInfo> list = page.getResult();
        return list;
    }

    @Override
    public void exportRankingPushRecord(HttpServletResponse response, Map<String, Object> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream outputStream = null;
        try {
            Page<RankingPushRecordInfo> page = new Page<>(0, Integer.MAX_VALUE);
            List<RankingPushRecordInfo> list = selectRankingPushRecordPage(params, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "用户奖金发放记录" + sdf.format(new Date()) + export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            Map<String, String> map = null;
            for (RankingPushRecordInfo item : list) {
                map = new HashMap<>();
                String rankingType = "";
                String pushStatus = "";
                String accountStatus = "";
                String dataType = "";
                if(item.getRankingType()!=null){
                    if(item.getRankingType()==0){
                        rankingType = "周榜";
                    }else if (item.getRankingType()==1){
                        rankingType = "月榜";
                    }else if (item.getRankingType()==2){
                        rankingType = "年榜";
                    }
                }
                if(item.getPushStatus()!=null){
                    if(item.getPushStatus()==0){
                        pushStatus = "未发放";
                    }else if (item.getPushStatus()==1){
                        pushStatus = "已发放";
                    }else if (item.getPushStatus()==2){
                        pushStatus = "已移除";
                    }
                }
                if(item.getAccountStatus()!=null){
                    if(item.getAccountStatus()==0){
                        accountStatus = "未记账";
                    }else if (item.getAccountStatus()==1){
                        accountStatus = "已记账";
                    }else if (item.getAccountStatus()==2){
                        accountStatus = "记账失败";
                    }
                }
                //0收益金额 1会员数 2用户数
                if(item.getDataType()!=null){
                    if("0".equals(item.getDataType())){
                        dataType = "收益金额";
                    }else if("1".equals(item.getDataType())){
                        dataType = "会员数";
                    }else if("2".equals(item.getDataType())){
                        dataType = "用户数";
                    }
                }

                map.put("orderNo", item.getOrderNo());         //获奖订单号
                map.put("rankingNo", item.getRankingNo());       //排行榜编号
                map.put("batchNo", item.getBatchNo());         //期号
                map.put("ruleNo", item.getRuleNo());          //规则编号
                map.put("rankingName", item.getRankingName());     //排行榜名称
                map.put("rankingType", rankingType);     //统计周期
                map.put("orgId", item.getOrgName());           //所属组织
                map.put("showNo", item.getShowNo());          //排名
                map.put("userName", item.getUserName());        //用户姓名
                try {
                    map.put("nickName", StringUtils.isNotEmpty(item.getNickName()) ? URLDecoder.decode(item.getNickName(), "utf-8") : "");        //微信昵称
                } catch (Exception e) {
                }

                map.put("userCode", item.getUserCode());        //用户ID
                map.put("phone", item.getPhone());           //手机号
                map.put("rankingData", item.getRankingData());     //统计总额
                map.put("rankingLevel", item.getRankingLevel());    //获奖等级
                map.put("rankingMoney", item.getRankingMoney() == null ? "0.00" : item.getRankingMoney().toString());//获奖金额
                map.put("pushStatus", pushStatus);      //用户发放状态
                map.put("accountStatus", accountStatus);   //记账状态
                map.put("dataType", dataType);
                map.put("startTime", item.getStartTime() == null ? "" : sdf2.format(item.getStartTime()));
                map.put("endTime", item.getEndTime() == null ? "" : sdf2.format(item.getEndTime()));
                //map.put("removeRemark", item.getRemoveRemark());    //移除说明
                map.put("pushTime", item.getPushTime() == null ? "" : sdf2.format(item.getPushTime()));          //发放时间
                //map.put("removeTime", item.getRemoveTime()==null?"":sdf2.format(item.getRemoveTime()));        //移除时间
                data.add(map);
            }

            String[] cols = new String[]{
                    "orderNo", "rankingNo", "batchNo", "ruleNo", "rankingName", "rankingType", "dataType",
                    "orgId", "showNo", "userName", "nickName", "userCode", "phone", "rankingData",
                    "rankingLevel", "rankingMoney", "pushStatus", "accountStatus",
                    "pushTime", "startTime", "endTime"};
            String[] colsName = new String[]{
                    "获奖订单号", "排行榜编号", "期号", "规则编号",
                    "排行榜名称", "统计周期", "统计数据", "所属组织", "排名", "用户姓名", "微信昵称",
                    "用户ID", "手机号", "统计总额", "获奖等级", "获奖金额", "用户发放状态", "记账状态",
                    "发放时间", "统计开始时间", "统计结束时间"};
            outputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家用户奖金发放记录异常", e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String selectRankingPushRecordTotalMoneySum(Map<String, Object> params) {
        return rankingPushRecordDao.selectRankingPushRecordTotalMoneySum(params);
    }

    @Override
    public String selectRankingPushRecordPushTotalMoneySum(Map<String, Object> params) {
        return rankingPushRecordDao.selectRankingPushRecordTotalMoneySum(params);
    }

    public String getUserName(){
        String userName = "";
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null){
            userName = principal.getRealName().toString();
        } else {
            throw new BossBaseException("用户未登录");
        }
        return userName;
    }

    public void insert111(Long orgId){
        OrgInfo org = new OrgInfo();
        org.setOrgId(orgId);
        int i = orgInfoDao.insert(org);
        if (i == 1){
            throw  new RuntimeException("测试回滚");
        }
        return;
    }

    /**
     * 超级银行家详情
     * @param orgId
     * @return
     */
    @Override
    public Result orgInfoDetail(Long orgId) {
        Result result = new Result();
        if(orgId == null){
            result.setMsg("orgId不能为空");
            return result;
        }
        OrgInfo orgInfo = orgInfoDao.selectDetail(orgId);
        List<ModulesNewStyles> tutorModelList = modulesNewStyleDao.selectTutorModelByOrgId(orgId);
        List<ModulesNewStyles> bankModelList = modulesNewStyleDao.selectBankModelByOrgId(orgId);

        orgInfo.setTutorModelList(tutorModelList);
        orgInfo.setBankModelList(bankModelList);

        for(int i=0; i<tutorModelList.size(); i++){
            String tutor = CommonUtil.getImgUrlAgent(tutorModelList.get(i).getModulesImages());
            tutorModelList.get(i).setModulesImagesUrl(tutor);
        }
        for(int i=0; i<bankModelList.size(); i++){
            String bank = CommonUtil.getImgUrlAgent(bankModelList.get(i).getModulesImages());
            bankModelList.get(i).setModulesImagesUrl(bank);
        }

        if (orgInfo != null) {
            String orgLogo = CommonUtil.getImgUrlAgent(orgInfo.getOrgLogo());
            String sealUrl = CommonUtil.getImgUrlAgent(orgInfo.getAuthorizedUnitSeal());
            String memberCenterUrl = CommonUtil.getImgUrlAgent(orgInfo.getMemberCenterLogo());
            String startPageUrl = CommonUtil.getImgUrlAgent(orgInfo.getStartPage());
            String appLogoUrl = CommonUtil.getImgUrlAgent(orgInfo.getAppLogo());
            String publicQrUrl = CommonUtil.getImgUrlAgent(orgInfo.getPublicQrCode());
            String shareMessageUrl = CommonUtil.getImgUrlAgent(orgInfo.getShareMessageLogo());
            String shareTempalte1Url = CommonUtil.getImgUrlAgent(orgInfo.getShareTemplateImage1());
            String shareTempalte2Url = CommonUtil.getImgUrlAgent(orgInfo.getShareTemplateImage2());
            String shareTempalte3Url = CommonUtil.getImgUrlAgent(orgInfo.getShareTemplateImage3());
            String homeBackgroundUrl = CommonUtil.getImgUrlAgent(orgInfo.getHomeBackground());
            String appQrCodeUrl = CommonUtil.getImgUrlAgent(orgInfo.getAppQrCode());

            orgInfo.setOrgLogoUrl(orgLogo);
            orgInfo.setAuthorizedUnitSealUrl(sealUrl);
            orgInfo.setMemberCenterLogoUrl(memberCenterUrl);
            orgInfo.setStartPageUrl(startPageUrl);
            orgInfo.setAppLogoUrl(appLogoUrl);
            orgInfo.setPublicQrCodeUrl(publicQrUrl);
            orgInfo.setShareMessageLogoUrl(shareMessageUrl);
            orgInfo.setShareTemplateImage1Url(shareTempalte1Url);
            orgInfo.setShareTemplateImage2Url(shareTempalte2Url);
            orgInfo.setShareTemplateImage3Url(shareTempalte3Url);
            orgInfo.setAppQrCodeUrl(appQrCodeUrl);
            if (orgInfo.getIsOpen() == null) {
                orgInfo.setIsOpen("0");
            }
            orgInfo.setHomeBackgroundUrl(homeBackgroundUrl);
            if (orgInfo.getTradeFeeRate() != null) {
                orgInfo.setTradeFeeRateStr(orgInfo.getTradeFeeRate() + "%");
            }
            if (orgInfo.getTradeSingleFee() != null) {
                orgInfo.setTradeSingleFeeStr(orgInfo.getTradeSingleFee() + "元");
            }
            if (orgInfo.getTradeFeeRateWmjh() != null) {
                orgInfo.setTradeFeeRateWmjhStr(orgInfo.getTradeFeeRateWmjh() + "%");
            }
            if (orgInfo.getTradeSingleFeeWmjh() != null) {
                orgInfo.setTradeSingleFeeWmjhStr(orgInfo.getTradeSingleFeeWmjh() + "元");
            }
            if (orgInfo.getWithdrawFeeRate() != null) {
                orgInfo.setWithdrawFeeRateStr(orgInfo.getWithdrawFeeRate() + "%");
            }
            if (orgInfo.getWithdrawSingleFee() != null) {
                orgInfo.setWithdrawSingleFeeStr(orgInfo.getWithdrawSingleFee() + "元");
            }
            List<OpenFunctionConf> openFunctionConfs = openFunctionConfDao.getList();
            List<OrgInfoOpenConf> list = orgInfoOpenConfDao.getOrgInfoList(orgInfo.getOrgId());
            if (list != null && list.size() > 0) {
                for (OrgInfoOpenConf orgInfoOpenConf : list) {
                    for (OpenFunctionConf openFunctionConf : openFunctionConfs) {
                        if (orgInfoOpenConf.getFunctionId().equals(openFunctionConf.getId() + "")) {
                            openFunctionConf.setIsEnable(Integer.valueOf(orgInfoOpenConf.getIsEnable()));
                        }
                    }
                }
            }
            orgInfo.setOpenFunctionConfList(openFunctionConfs);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(orgInfo);
        } else {
            result.setMsg(orgId + "找不到对应的超级银行家组织");
        }
        return result;
    }

    /**
     * 分页条件查询订单数据
     * @param baseInfo
     * @param page
     * @return
     */
    @Override
    public List<OrderMain> selectOrderPage(OrderMain baseInfo, Page<OrderMain> page) {
        orderMainDao.selectOrderPage(baseInfo, page);
        List<OrderMain> list = page.getResult();
        filterOrderParam(list);
        return list;
    }

    @Override
    public List<UserProfit> selectProfitDetailPage(UserProfit baseInfo, Page<UserProfit> page) {
        userProfitDao.selectProfitDetailPage(baseInfo, page);
        List<UserProfit> list = page.getResult();
        filterProfitDetailParam(list);
        return list;
    }

    @Override
    public List<UserObtainRecord> selectObtainRecordPage(UserObtainRecord baseInfo, Page<UserObtainRecord> page) {
        userObtainRecordDao.selectObtainRecordPage(baseInfo, page);
        List<UserObtainRecord> list = page.getResult();
        filterObtainRecordParam(list);
        return list;
    }

    /**
     * 汇总订单数据
     */
    @Override
    public OrderMainSum selectOrderSum(OrderMain baseInfo) {
        OrderMainSum orderMainSum = orderMainDao.selectOrderSum(baseInfo);
        if(orderMainSum == null ){
            orderMainSum = new OrderMainSum();
        }
        return orderMainSum;
    }

    /**
     * 订单分润明细汇总
     * @param baseInfo
     * @return
     */
    @Override
    public OrderMainSum selectProfitDetailSum(UserProfit baseInfo) {
        OrderMainSum orderMainSum = new OrderMainSum();
        OrderMainSum totalSum = userProfitDao.selectTotalProfitSum(baseInfo);//奖励总金额
        OrderMainSum userSum = userProfitDao.selectUserProfitSum(baseInfo);//收益汇总
        orderMainSum.setTotalBonusSum(totalSum != null ? totalSum.getTotalBonusSum() : new BigDecimal(0));
        orderMainSum.setProfitSum(userSum != null ? userSum.getProfitSum() : new BigDecimal(0));
        return orderMainSum;
    }

    @Override
    public OrderMainSum selectObtainRecordSum(UserObtainRecord baseInfo) {
        OrderMainSum orderMainSum = userObtainRecordDao.selectObtainRecordSum(baseInfo);
        if(orderMainSum == null){
            orderMainSum = new OrderMainSum();
        }
        return orderMainSum;
    }

    /**
     * 模糊查询SuperBankUserInfo
     * @param userCode
     * @return
     */
    @Override
    public List<SuperBankUserInfo> selectUserInfoList(String userCode) {
        if(StringUtils.isNotBlank(userCode)){
            userCode = userCode + "%";
        }
        return superBankUserInfoDao.selectUserInfoList(userCode);
    }

    /**
     * 导出用户管理
     * @param response
     * @param userInfo
     */
    @Override
    public void exportUserInfo(HttpServletResponse response, SuperBankUserInfo userInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<SuperBankUserInfo> page = new Page<>(0, Integer.MAX_VALUE);
            List<SuperBankUserInfo> list = selectUserInfoPage(userInfo, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "超级银行家用户管理" + sdf.format(new Date()) + export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            Map<String, String> map = null;
            for (SuperBankUserInfo item : list) {
                map = new HashMap<>();
                map.put("userCode", item.getUserCode());
                map.put("orgName", String.valueOf(item.getOrgName()));
                map.put("userName", item.getUserName());
                map.put("niceName", item.getNickName());
                map.put("phone", item.getPhone());
                if (StringUtils.isNotBlank(item.getIsOpen())) {
                    if ("1".equals(item.getIsOpen())) {
                        map.put("isOpen", "是");
                    } else {
                        map.put("isOpen", "否");
                    }

                } else {
                    map.put("isOpen", "否");
                }
                map.put("weinxinCode", item.getWeixinCode());
                map.put("totalProfit", String.valueOf(item.getTotalProfit()));
                map.put("userType", item.getUserType());
                map.put("status", "1".equals(item.getStatus()) ? "正常" : "冻结");
                map.put("accountStatus", item.getAccountStatus());
                map.put("createDateStr", item.getCreateDateStr());
                map.put("repaymentUserNo", item.getRepaymentUserNo());
                map.put("receiveUserNo", item.getReceiveUserNo());
                map.put("topOneCode", item.getTopOneCode());
                map.put("topTwoCode", item.getTopTwoCode());
                map.put("topThreeCode", item.getTopThreeCode());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                map.put("payMoneyStatus", item.getPayMoneyStatus());
                map.put("toagentDate", item.getToagentDateStr());
                data.add(map);
            }
            String[] cols = new String[]{
                    "userCode", "orgName", "isOpen", "userName", "niceName",
                    "phone", "weinxinCode",
                    "totalProfit", "userType",
                    "status", "accountStatus", "createDateStr",
                    "repaymentUserNo", "receiveUserNo",
                    "topOneCode", "topTwoCode", "topThreeCode",
                    "openProvince", "openCity", "openRegion",
                    "remark", "payMoneyStatus", "toagentDate"};
            String[] colsName = new String[]{
                    "用户ID", "所属组织", "是否外放", "姓名", "昵称",
                    "手机号", "微信号",
                    "总收益", "代理身份",
                    "状态", "是否开户", "入驻时间",
                    "超级还用户编号", "收款商户编号",
                    "一级代理ID", "二级代理ID", "三级代理ID",
                    "省", "市", "区",
                    "备注", "是否缴费", "支付时间"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家用户管理异常", e);
        } finally {
            try {
                if (ouputStream != null) {
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出代理授权订单
     * @param order
     */
    @Override
    public void exportAgentOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "代理授权订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("status", item.getStatus());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("shareUserPhone", item.getShareUserPhone());
                map.put("price", String.valueOf(item.getPrice()));
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDate", item.getCreateDateStr());
                map.put("payDate", item.getPayDateStr());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("refundStatus", item.getRefundStatus());
                map.put("refundDate", item.getRefundDateStr());
                map.put("refundMsg", item.getRefundMsg());
                map.put("payChannel", item.getPayChannel());
                map.put("payChannelNo", item.getPayChannelNo());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("realPlatProfit", item.getRealPlatProfit()==null?"":item.getRealPlatProfit().toString());
                map.put("adjustRatio",item.getAdjustRatio()==null?"":item.getAdjustRatio().toString());
                map.put("basicBonusAmount",item.getBasicBonusAmount()==null?"":item.getBasicBonusAmount().toString());
                map.put("bonusAmount",item.getBonusAmount()==null?"":item.getBonusAmount().toString());
                map.put("redUserCode",item.getRedUserCode()==null?"":item.getRedUserCode());
                map.put("rate",item.getRate());
                map.put("redUserName",item.getRedUserName()==null?"":item.getRedUserName());
                map.put("refundOrderNo", item.getRefundOrderNo());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            addOrderSumMap(order, data);

            String[] cols = new String[]{
                    "orderNo","orgId","orgName","status",
                    "payChannel","payChannelNo","payOrderNo",
                    "refundStatus","refundDate","refundMsg","refundOrderNo",
                    "userCode","userName","shareUserPhone",
                    "price","totalBonus",
                    "createDate","payDate","payOrderNo",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgProfit","plateProfit","realPlatProfit","adjustRatio","rate",
                    "basicBonusAmount","bonusAmount","redUserCode","redUserName","accountStatus",
                    "openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","组织名称","订单状态",
                    "收款通道","收款通道商户号","收款通道流水号",
                    "已达标退款","退款时间","退款原由","退款订单号",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "售价","发放总奖金",
                    "创建时间","支付时间","关联支付订单",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商分润","平台分润" ,"平台实际分润","调节系数","领地业务基准配置",
                    "领地业务基准分红","领地分红","领地分红领取用户编号","领地分红用户姓名","记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家代理授权订单异常", e);
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
    public void exportOpenCredit(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "开通办理信用卡订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("orgName", String.valueOf(item.getOrgId()));
                map.put("status", item.getStatus());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("shareUserPhone", item.getShareUserPhone());
                map.put("price", String.valueOf(item.getPrice()));
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDate", item.getCreateDateStr());
                map.put("payDate", item.getPayDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit()));
                map.put("orgProfit", String.valueOf(item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("payChannel", item.getPayChannel());
                map.put("payChannelNo", item.getPayChannelNo());
                map.put("payOrderNo", item.getPayOrderNo());
                data.add(map);
            }
            addOrderSumMap2(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orgId","orgName","status",
                    "payChannel","payChannelNo","payOrderNo",
                    "userCode","userName","shareUserPhone",
                    "price","totalBonus",
                    "createDate","payDate",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgProfit","plateProfit","accountStatus"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","组织名称","订单状态",
                    "收款通道","收款通道商户号","收款通道流水号",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "售价","品牌发放总奖金",
                    "创建时间","支付时间","关联支付订单",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商分润","平台分润","记账状态"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家办理信用卡订单异常", e);
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

    /**
     * 导出办理信用卡订单
     * @param order
     */
    @Override
    public void exportCreditOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "办理信用卡订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("status", item.getStatus());
                map.put("bankCode", item.getBankCode());
                map.put("bankName", item.getBankName());
                map.put("bankNickName", item.getBankNickName());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("orderName", item.getOrderName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("orderIdNo", item.getOrderIdNo());
                map.put("creditcardBankBonus", String.valueOf(item.getCreditcardBankBonus()));
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDate", item.getCreateDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit()));
                map.put("oneUserProfit2", String.valueOf(item.getOneUserProfit2()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit()));
                map.put("twoUserProfit2", String.valueOf(item.getTwoUserProfit2()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit()));
                map.put("thrUserProfit2", String.valueOf(item.getThrUserProfit2()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit()));
                map.put("fouUserProfit2", String.valueOf(item.getFouUserProfit2()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit()));
                map.put("orgProfit2", String.valueOf(item.getOrgProfit2()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit()));
                map.put("plateProfit2", String.valueOf(item.getPlateProfit2()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("accountStatus2", item.getAccountStatus2());
                map.put("profitStatus2", item.getProfitStatus2());
                map.put("shareUserPhone", item.getShareUserPhone());
                map.put("creditcardBankBonus2", String.valueOf(item.getCreditcardBankBonus2()));
                map.put("totalBonus2", String.valueOf(item.getTotalBonus2()));
                map.put("payDate", item.getPayDateStr());
                map.put("payDate2", item.getPayDate2Str());
                map.put("proofreadingMethod", item.getProofreadingMethod());
                map.put("proofreadingResult", item.getProofreadingResult());
                map.put("realPlatProfit", item.getRealPlatProfit()==null?"":item.getRealPlatProfit().toString());
                map.put("adjustRatio",item.getAdjustRatio()==null?"":item.getAdjustRatio().toString());
                map.put("basicBonusAmount",item.getBasicBonusAmount()==null?"":item.getBasicBonusAmount().toString());
                map.put("bonusAmount",item.getBonusAmount()==null?"":item.getBonusAmount().toString());
                map.put("redUserCode",item.getRedUserCode()==null?"":item.getRedUserCode());
                map.put("rate",item.getRate());
                map.put("redUserName",item.getRedUserName()==null?"":item.getRedUserName());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("batchNo", item.getBatchNo());
                map.put("batchNo2", item.getBatchNo2());
                map.put("remark", item.getRemark());
                map.put("loanOrderNo", item.getLoanOrderNo());
                map.put("totalBonusReal",String.valueOf(item.getOneUserProfit().add(item.getTwoUserProfit())
                        .add(item.getThrUserProfit()).add(item.getFouUserProfit())));
                map.put("totalBonus2Real",String.valueOf(item.getOneUserProfit2().add(item.getTwoUserProfit2())
                        .add(item.getThrUserProfit2()).add(item.getFouUserProfit2())));
//                map.put("cardType", item.getCardType());
                data.add(map);
            }
            addOrderSumMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orgId","orgName","status","profitStatus2",
                    "bankCode","bankName","bankNickName",
                    "userCode","userName","shareUserPhone",
                    "orderName","orderPhone", "orderIdNo",
                    "creditcardBankBonus","creditcardBankBonus2",
                    "totalBonus","totalBonusReal",
                    "totalBonus2","totalBonus2Real",
                    "createDate","payDate","payDate2",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit","oneUserProfit2",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit","twoUserProfit2",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit","thrUserProfit2",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit","fouUserProfit2",
                    "orgProfit","orgProfit2","plateProfit","plateProfit2","accountStatus"
                    ,"realPlatProfit","proofreadingMethod","proofreadingResult","loanOrderNo","adjustRatio","rate","basicBonusAmount","bonusAmount","redUserCode","redUserName","accountStatus2",
                    "openProvince","openCity","openRegion","batchNo","batchNo2","remark"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","组织名称","订单状态","首刷分润状态",
                    "发卡银行编码","发卡银行名称","发卡银行别称",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "订单姓名","订单手机号","订单证件号",
                    "银行发卡奖金","银行首刷奖金",
                    "发卡品牌发放总奖金","发卡品牌实发总奖金",
                    "首刷品牌发放总奖金","首刷品牌实发总奖金",
                    "创建时间","发卡分润时间","首刷分润时间",
                    "一级编号","一级名称","一级身份","发卡一级分润","首刷一级分润",
                    "二级编号","二级名称","二级身份","发卡二级分润","首刷二级分润",
                    "三级编号","三级名称","三级身份","发卡三级分润","首刷三级分润",
                    "四级编号","四级名称","四级身份","发卡四级分润","首刷四级分润",
                    "品牌商分润发卡","品牌商分润首刷","平台分润发卡","平台分润首刷","发卡记账状态"
                    ,"平台实际分润","发卡分润发放方式","校对结果","上游订单号","调节系数","领地业务基准配置","领地业务基准分红","领地分红","领地分红领取用户编号","领地分红用户姓名","首刷记账状态",
                    "省","市","区","办卡批次号","首刷批次号","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家办理信用卡订单异常", e);
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

    /**
     * 导出贷款订单
     * @param order
     */
    @Override
    public void exportLoanOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "贷款订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("orderType", item.getOrderType());
                map.put("status", item.getStatus());
                map.put("loanName", item.getLoanName());
                map.put("loanAlias", item.getLoanAlias());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("shareUserPhone", item.getShareUserPhone());
                map.put("orderName", item.getOrderName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("orderIdNo", item.getOrderIdNo());
                map.put("loanAmount", String.valueOf(item.getLoanAmount()));
                map.put("loanTypeStr",item.getLoanTypeStr());
                int ppStatus=item.getPpStatus();
                if(ppStatus==1||ppStatus==2){
                    map.put("ppStatus","已授信");
                }else{
                    map.put("ppStatus","未授信");
                }
                int accessWay=item.getAccessWay();
                if(accessWay==1){
                    map.put("accessWay","H5");
                }else{
                    map.put("accessWay","API");
                }
                map.put("profitTypeStr",item.getProfitTypeStr());
                map.put("loanBankRate", item.getLoanBankRate());
                map.put("price", String.valueOf(item.getPrice()));
                map.put("loanOrgBonus", item.getLoanOrgBonus() != null ? item.getLoanOrgBonus() :"");
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDate", item.getCreateDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit()));
                map.put("realPlatProfit", item.getRealPlatProfit()==null?"":item.getRealPlatProfit().toString());
                map.put("adjustRatio",item.getAdjustRatio()==null?"":item.getAdjustRatio().toString());
                map.put("basicBonusAmount",item.getBasicBonusAmount()==null?"":item.getBasicBonusAmount().toString());
                map.put("bonusAmount",item.getBonusAmount()==null?"":item.getBonusAmount().toString());
                map.put("redUserCode",item.getRedUserCode()==null?"":item.getRedUserCode().toString());
                map.put("redUserName",item.getRedUserName()==null?"":item.getRedUserName().toString());
                map.put("rate",item.getRate());
                map.put("accountStatus", item.getAccountStatus());
                map.put("completeDate", item.getCompleteDateStr());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            addOrderSumMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orgId","orgName","orderType","ppStatus","status","accessWay","loanAlias",
                    "userCode","userName","shareUserPhone",
                    "orderName","orderPhone", "orderIdNo",
                    "loanAmount","loanTypeStr","profitTypeStr",
                    "loanBankRate", "price","loanOrgBonus","totalBonus",
                    "createDate","completeDate",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgProfit","plateProfit","realPlatProfit","adjustRatio","rate","basicBonusAmount","bonusAmount","redUserCode","redUserName","accountStatus",
                    "openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","组织名称","订单类型","授信状态","订单状态","接入方式","贷款机构别称",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "订单姓名","订单手机号","订单证件号",
                    "贷款金额","奖金方式","奖励要求",
                    "贷款机构总奖金扣率","贷款机构总奖金","品牌发放总资金扣率","品牌发放总奖金",
                    "创建时间","订单完成时间",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商分润","平台分润","平台实际分润","调节系数","领地业务基准分红配置","领地业务基准分红","领地分红","领地分红领取用户编号","领地分红用户姓名","记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家贷款订单异常", e);
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
    public void exportInsuranceOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = insuranceOrderService.selectOrderPage(order, page);
            int size = 1;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "保险订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgName",item.getOrgName());
                map.put("status",item.getStatus());
                map.put("productName",item.getProductName());
                map.put("productType",item.getProductType());
                map.put("bonusSettleTime",item.getBonusSettleTime());
                map.put("productPrice",item.getProductPrice()==null?"":String.valueOf(item.getProductPrice()));
                map.put("companyNickName",item.getCompanyNickName());
                map.put("createDate",item.getCreateDateStr());
                map.put("price",String.valueOf(item.getPrice()==null ?"":item.getPrice()));
                map.put("userCode",item.getUserCode());
                map.put("userName",item.getUserName());
                map.put("sharePhone",item.getShareUserPhone());
                map.put("plateProfit",item.getPlateProfit()==null ?"":item.getPlateProfit()+"");
                map.put("totalBonus",String.valueOf(item.getTotalBonus()));
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
                map.put("realPlatProfit", item.getRealPlatProfit()==null?"":item.getRealPlatProfit().toString());
                map.put("adjustRatio",item.getAdjustRatio()==null?"":item.getAdjustRatio().toString());
                map.put("basicBonusAmount",item.getBasicBonusAmount()==null?"":item.getBasicBonusAmount().toString());
                map.put("bonusAmount",item.getBonusAmount()==null?"":item.getBonusAmount().toString());
                map.put("redUserCode",item.getRedUserCode()==null?"":item.getRedUserCode().toString());
                map.put("rate",item.getRate());
                map.put("redUserName",item.getRedUserName()==null?"":item.getRedUserName().toString());
                map.put("accountStatus", item.getAccountStatus());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            String[] cols = new String[]{
                    "orderNo", "orgName", "status", "productName",
                    "productType", "companyNickName", "createDate", "price", "bonusSettleTime", "productPrice",
                    "userCode", "userName", "sharePhone", "totalBonus",
                    "oneUserCode", "oneUserName", "oneUserType", "oneUserProfit",
                    "twoUserCode", "twoUserName", "twoUserType", "twoUserProfit",
                    "thrUserCode", "thrUserName", "thrUserType", "thrUserProfit",
                    "fouUserCode", "fouUserName", "fouUserType", "fouUserProfit",
                    "orgName", "orgProfit","plateProfit","realPlatProfit","adjustRatio","rate","basicBonusAmount","bonusAmount","redUserCode","redUserName", "accountStatus",
                    "openProvince", "openCity", "openRegion", "remark"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","订单状态","产品名称",
                    "保险种类","保险公司别称","订单创建时间", "保单金额", "奖金结算时间", "保险总奖金",
                    "贡献人ID","贡献人名称", "贡献人手机号", "品牌发放总奖金",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商名称","品牌商分润","平台分润","平台实际分润","调节系数","领地业务基准配置","领地业务基准分红","领地分红","领地分红领取用户编号","领地分红用户姓名", "记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家保险订单异常", e);
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
    public void exportSuperExcOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = superExcOrderService.selectOrderPage(order, page);
            int size = 1;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "积分兑换订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("orgId",String.valueOf(item.getOrgId()));
                map.put("orgName",item.getOrgName());
                map.put("status",item.getStatus());
                map.put("loanType",item.getLoanType());
                map.put("orderName",item.getOrderName());
                map.put("loanAmount",item.getLoanAmount()==null?"":String.valueOf(item.getLoanAmount()));
                map.put("price",String.valueOf(item.getPrice()==null ?"":item.getPrice()));
                map.put("createDate",item.getCreateDateStr());
                map.put("userCode",item.getUserCode());
                map.put("userName",item.getUserName());
                map.put("sharePhone",item.getShareUserPhone());
                map.put("plateProfit",item.getPlateProfit()==null ?"":item.getPlateProfit()+"");
                map.put("totalBonus",String.valueOf(item.getTotalBonus()));
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            String[] cols = new String[]{
                    "orderNo", "payOrderNo", "orgId", "orgName",
                    "status", "loanType", "orderName", "loanAmount", "price", "createDate",
                    "userCode", "userName", "sharePhone", "totalBonus",
                    "oneUserCode", "oneUserName", "oneUserType", "oneUserProfit",
                    "twoUserCode", "twoUserName", "twoUserType", "twoUserProfit",
                    "thrUserCode", "thrUserName", "thrUserType", "thrUserProfit",
                    "fouUserCode", "fouUserName", "fouUserType", "fouUserProfit",
                    "orgName", "orgProfit","plateProfit", "accountStatus",
                    "openProvince", "openCity", "openRegion", "remark"};
            String[] colsName = new String[]{
                    "订单ID","关联订单ID","所属组织","组织名称",
                    "订单状态","兑换产品类别", "兑换产品名称", "兑换积分数", "兑换价格", "订单创建时间",
                    "贡献人ID","贡献人名称", "贡献人手机号", "品牌发放总奖金",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商名称","品牌商分润","平台分润", "记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家积分兑换异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 导出收款订单
     * @param order
     */
    @Override
    public void exportReceiveOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "收款订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("status", item.getStatus());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("orderName", item.getOrderName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("orderIdNo", item.getOrderIdNo());
                map.put("receiveAgentId", item.getReceiveAgentId());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("payMethod", item.getPayMethod());
                map.put("receiveAmount", String.valueOf(item.getReceiveAmount()));
                map.put("transRate", item.getTransRate());
                map.put("loanOrgRate", item.getLoanOrgRate());
                map.put("loanOrgBonus", item.getLoanOrgBonus());
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDate", item.getCreateDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit()));
                map.put("realPlatProfit", item.getRealPlatProfit()==null?"":String.valueOf(item.getRealPlatProfit()));
                map.put("adjustRatio",item.getAdjustRatio()==null?"":String.valueOf(item.getAdjustRatio()));
                map.put("basicBonusAmount",item.getBasicBonusAmount()==null?"":String.valueOf(item.getBasicBonusAmount()));
                map.put("bonusAmount",item.getBonusAmount()==null?"":String.valueOf(item.getBonusAmount()));
                map.put("rate",item.getRate());
                map.put("redUserCode",item.getRedUserCode()==null?"":item.getRedUserCode());
                map.put("redUserName",item.getRedUserName()==null?"":item.getRedUserName());
                map.put("accountStatus", item.getAccountStatus());
                data.add(map);
            }
            addOrderSumMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orgId","status","loanName",
                    "userCode","userName",
                    "orderName","orderPhone", "orderIdNo",
                    "receiveAgentId","receiveAmount","payOrderNo", "payMethod","receiveAmount","transRate",
                    "loanOrgRate","loanOrgBonus","totalBonus",
                    "createDate",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgName","orgProfit","plateProfit","realPlatProfit","adjustRatio","rate","basicBonusAmount","bonusAmount","redUserCode","redUserName","accountStatus"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","订单状态","贷款机构",
                    "贡献人ID","贡献人名称",
                    "订单姓名","订单手机号","订单证件号",
                    "收款商户ID","收款金额","关联订单号","支付方式","收款金额","商户费率",
                    "品牌代理成本扣率","品牌发放总奖金扣率","发放奖金",
                    "创建时间",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商名称","品牌商分润","平台分润","平台实际分润","调节系数","领地业务基准分红配置","领地业务基准分红","领地分红","领地分红领取用户编号","领地分红用户姓名","记账状态"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家收款订单异常", e);
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

    /**
     * 导出还款订单
     * @param order
     */
    @Override
    public void exportRepayOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "还款订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orderType",item.getOrderType());
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("status", item.getStatus());
                map.put("repayTransStatus", item.getRepayTransStatus());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("shareUserPhone", item.getShareUserPhone());
                map.put("orderName", item.getOrderName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("orderIdNo", item.getOrderIdNo());
                map.put("repaymentAgentId", item.getRepaymentAgentId());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("receiveAmount", String.valueOf(item.getReceiveAmount()));
                map.put("repaymentAmount", String.valueOf(item.getRepaymentAmount()));
                map.put("repayTransscucons", String.valueOf(item.getRepayTransscucons()));
                map.put("repayTransCardNo", item.getRepayTransCardNo());
                map.put("transRate", item.getTransRate());
                map.put("loanOrgRate", item.getLoanOrgRate());
                map.put("loanOrgBonus", item.getLoanOrgBonus());
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("repayTransChannel", item.getRepayTransChannel());
                map.put("createDate", item.getCreateDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit()));
                map.put("realPlatProfit", item.getRealPlatProfit()==null?"":item.getRealPlatProfit().toString());
                map.put("adjustRatio",item.getAdjustRatio()==null?"":item.getAdjustRatio().toString());
                map.put("basicBonusAmount",item.getBasicBonusAmount()==null?"":item.getBasicBonusAmount().toString());
                map.put("bonusAmount",item.getBonusAmount()==null?"":item.getBonusAmount().toString());
                map.put("redUserCode",item.getRedUserCode()==null?"":item.getRedUserCode().toString());
                map.put("redUserName",item.getRedUserName()==null?"":item.getRedUserName().toString());
                map.put("accountStatus", item.getAccountStatus());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("rate", item.getRate());
                map.put("remark", item.getRemark());
                map.put("repayTransfee", String.valueOf(item.getRepayTransfee()));
                map.put("companyCostRate", item.getCompanyCostRate());
                data.add(map);
            }
            addOrderSumMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orderType","orgId","orgName","status","repayTransStatus",
                    "userCode","userName","shareUserPhone",
                    "repaymentAgentId","payOrderNo","receiveAmount","repaymentAmount","repayTransscucons","repayTransCardNo",
                    "transRate","repayTransfee","loanOrgRate","loanOrgBonus","totalBonus",
                    "repayTransChannel","companyCostRate",
                    "createDate",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgProfit","plateProfit","realPlatProfit","adjustRatio","rate","basicBonusAmount","bonusAmount","redUserCode","redUserName","accountStatus",
                    "openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{
                    "订单ID","类型","所属组织", "组织名称","订单状态","还款状态",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "还款商户ID","关联还款订单号","目标还款金额","实际还款金额","实际消费金额","还款卡号",
                    "用户还款费率","用户还款手续费","品牌还款代理费率","品牌发放总奖金扣率","品牌发放总奖金",
                    "还款通道","公司成本扣率",
                    "创建时间",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商分润","平台分润","平台实际分润","调节系数","领地业务基准分红配置","领地业务基准分红","领地分红","领地分红领取用户编号","领地分红用户姓名","记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家还款订单异常", e);
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

    /**
     * 超级银行家订单分润详情导出
     * @param response
     * @param order
     */
    @Override
    public void exportProfitDetail(HttpServletResponse response, UserProfit order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<UserProfit> page = new Page<>(0, Integer.MAX_VALUE);
            List<UserProfit> list = selectProfitDetailPage(order, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "订单分润详情"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(UserProfit item: list){
                map = new HashMap<>();
                map.put("id", String.valueOf(item.getId()));
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("orgName", item.getOrgName());
                map.put("orderType", item.getOrderType());
                map.put("status", item.getStatus());
                map.put("orderNo", item.getOrderNo());
                map.put("shareUserCode", item.getShareUserCode());
                map.put("shareNickName", item.getShareNickName());
                map.put("userName", item.getUserName());
                map.put("shareUserPhone", item.getShareUserPhone());
                map.put("totalProfit", String.valueOf(item.getTotalProfit()));
                map.put("userName", item.getUserName());
                map.put("userCode", item.getUserCode());
                map.put("userType", item.getUserType());
                map.put("userProfit", String.valueOf(item.getUserProfit()));
                map.put("createDate", item.getCreateDateStr());
                map.put("accountStatus", item.getAccountStatus());
                map.put("profitLevel", String.valueOf(item.getProfitLevel()));
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                map.put("shareUserRemark", item.getShareUserRemark());
                data.add(map);
            }
            //添加合计
            OrderMainSum orderMainSum = selectProfitDetailSum(order);
            Map<String,String> sumMap = new HashMap<>();
//            sumMap.put("id", "奖励总金额：" + orderMainSum.getTotalBonusSum() + "元");
            sumMap.put("orgId", "分润汇总：" + orderMainSum.getProfitSum() + "元");
            data.add(sumMap);
            String[] cols = new String[]{
                    "id","orgId","orgName","orderType",
                    "status","orderNo",
                    "shareUserCode","shareNickName","userName","shareUserPhone",
                    "totalProfit",
                    "userName","userCode", "userType","userProfit","profitLevel",
                    "createDate","accountStatus",
                    "openProvince","openCity","openRegion","remark","shareUserRemark"};
            String[] colsName = new String[]{
                    "分润明细ID","所属组织","品牌商名称","订单类型",
                    "订单状态","订单编号",
                    "贡献人ID","贡献人昵称","贡献人名称","贡献人手机号",
                    "总奖金",
                    "收益人姓名","收益人ID","收益人身份","收益人分润","当前分润层级",
                    "创建时间","记账状态",
                    "省","市","区","备注","贡献人备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家分润明细异常", e);
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

    /**
     * 导出用户提现记录
     * @param response
     * @param order
     */
    @Override
    public void exportObtainRecord(HttpServletResponse response, UserObtainRecord order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<UserObtainRecord> page = new Page<>(0, Integer.MAX_VALUE);
            List<UserObtainRecord> list = selectObtainRecordPage(order, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "用户提现记录"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(UserObtainRecord item: list){
                map = new HashMap<>();
                map.put("obtainNo", item.getObtainNo());
                map.put("userCode", String.valueOf(item.getUserCode()));
                map.put("nickName", item.getNickName());
                map.put("userName", item.getUserName());
                map.put("phone", item.getPhone());
                map.put("status", item.getStatus());
                map.put("orderType", item.getOrderType());
                map.put("obtainAmount", String.valueOf(item.getObtainAmount()));
                map.put("realObtainAmount", String.valueOf(item.getRealObtainAmount()));
                map.put("obtainFee", String.valueOf(item.getObtainFee()));
                map.put("createDate", item.getCreateDateStr());
                map.put("userType", item.getUserType());
                map.put("orgName", item.getOrgName());
                data.add(map);
            }
            //添加合计
            OrderMainSum orderMainSum = selectObtainRecordSum(order);
            Map<String,String> sumMap = new HashMap<>();
            sumMap.put("obtainNo", "出款总金额：" + orderMainSum.getObtainAmountSum() + "元");
            sumMap.put("userCode", "实际出款总金额：" + orderMainSum.getRealObtainAmountSum() + "元");
            sumMap.put("nickName", "出款手续费总金额：" + orderMainSum.getObtainFeeSum() + "元");
            data.add(sumMap);
            String[] cols = new String[]{
                    "obtainNo","userCode","nickName","userName",
                    "phone","status","orderType","obtainAmount","realObtainAmount","obtainFee",
                    "createDate","userType","orgName"};
            String[] colsName = new String[]{
                    "订单ID","用户ID","昵称","姓名",
                    "手机号","结算状态","订单类型","出款金额","实际出款金额","出款手续费",
                    "创建时间","用户身份","所属组织"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家用户提现记录异常", e);
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
    public void exportInquiryOrder(HttpServletResponse response, ZxProductOrder order) {
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
            OutputStream ouputStream = null;
            try {
                Page<ZxProductOrder> page = new Page<>(0, Integer.MAX_VALUE);
                if("-1".equals(order.getOrgId().toString())){
                    order.setOrgId(null);
                }
                List<ZxProductOrder> zxProductOrders = zxProductOrderService.selectByPage(order, page);
                int size = 2;
                ListDataExcelExport export = new ListDataExcelExport(size);
                String fileName = "超级银行家征信订单"+sdf.format(new Date())+export.getFileSuffix(size);
                String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
                response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
                Map<String,String> map = null;
                for(ZxProductOrder item: zxProductOrders){
                    map = new HashMap<>();
                    map.put("yhjOrderNo", item.getYhjOrderNo());
                    map.put("orderNo",item.getOrderNo());
                    map.put("status",item.getStatus());
                    map.put("payMethod", item.getPayMethod());
                    map.put("payNo", item.getPayNo());
                    map.put("orgName", item.getOrgName());
                    map.put("generationTimeStr",item.getGenerationTimeStr() );
                    map.put("reportNo", item.getReportNo());
                    map.put("reportType", item.getReportTypeName());
                    map.put("productName", item.getProductName());
                    map.put("price", String.valueOf(item.getPrice()));
                    map.put("recordName", item.getRecordName());
                    map.put("recordPhone", item.getRecordPhone());
                    map.put("recordIdNo", item.getRecordIdNo());
                    map.put("contactPhone", item.getContactPhone());
                    map.put("userCode", item.getUserCode());
                    map.put("userName", item.getUserName());
                    map.put("shareUserPhone",item.getShareUserPhone());
                    map.put("totalBonus", String.valueOf(item.getTotalBonus()==null?"":item.getTotalBonus()));
                    map.put("accountStatus", item.getAccountStatus());
                    map.put("createTimeStr", item.getCreateTimeStr());
                    map.put("expiryTimeStr", item.getExpiryTimeStr());
                    map.put("refundTimeStr", item.getRefundTimeStr());
                    map.put("oneUserCode", item.getOneUserCode());
                    map.put("oneUserName", item.getOneUserName());
                    map.put("oneUserType", item.getOneUserType());
                    map.put("oneUserProfit", String.valueOf(item.getOneUserProfit()==null?"":item.getOneUserProfit()));
                    map.put("twoUserCode", item.getTwoUserCode());
                    map.put("twoUserType", item.getTwoUserType());
                    map.put("twoUserProfit",String.valueOf( item.getTwoUserProfit()==null?"":item.getTwoUserProfit()));
                    map.put("thrUserCode", item.getThrUserCode());
                    map.put("thrUserName", item.getThrUserName());
                    map.put("thrUserType", item.getThrUserType());
                    map.put("thrUserProfit", String.valueOf(item.getThrUserProfit()==null?"":item.getThrUserProfit()));
                    map.put("fouUserCode", item.getFouUserCode());
                    map.put("fouUserName", item.getFouUserName());
                    map.put("fouUserType", item.getFouUserType());
                    map.put("fouUserProfit", String.valueOf(item.getFouUserProfit()));
                    map.put("orgProfit", String.valueOf(item.getOrgProfit()));
                    map.put("realPlatProfit", item.getRealPlatProfit()==null?"":item.getRealPlatProfit().toString());
                    map.put("adjustRatio",item.getAdjustRatio()==null?"":item.getAdjustRatio().toString());
                    map.put("rate",item.getRate());
                    map.put("basicBonusAmount",item.getBasicBonusAmount()==null?"":item.getBasicBonusAmount().toString());
                    map.put("bonusAmount",item.getBonusAmount()==null?"":item.getBonusAmount().toString());
                    map.put("redUserCode",item.getRedUserCode()==null?"":item.getRedUserCode().toString());
                    map.put("redUserName",item.getRedUserName()==null?"":item.getRedUserName().toString());
                    map.put("zxCostPrice", String.valueOf(item.getZxCostPrice()==null?"":item.getZxCostPrice()));
                    map.put("plateProfit", String.valueOf(item.getPlateProfit()==null?"":item.getPlateProfit()));
                    map.put("openProvince", item.getOpenProvince());
                    map.put("openCity", item.getOpenCity());
                    map.put("openRegion", item.getOpenRegion());
                    map.put("remark", item.getRemark());
                    data.add(map);
                }

                String[] cols = new String[]{
                        "yhjOrderNo","orderNo","status","payMethod","payNo","orgName","generationTimeStr",
                        "reportNo","reportType","productName",
                        "price","recordName","recordPhone","recordIdNo",
                        "contactPhone","userCode",
                        "userName","shareUserPhone","totalBonus","accountStatus","createTimeStr","expiryTimeStr","refundTimeStr",
                        "oneUserCode","oneUserName","oneUserType",
                        "oneUserProfit","twoUserCode","twoUserName","twoUserType",
                        "twoUserProfit","thrUserCode",
                        "thrUserName","thrUserType","thrUserProfit","fouUserCode","fouUserName",
                        "fouUserType","fouUserProfit","orgProfit","realPlatProfit","rate","adjustRatio","basicBonusAmount","bonusAmount","redUserCode","redUserName","zxCostPrice",
                        "plateProfit","openProvince","openCity","openRegion",
                        "remark"
                };
                String[] colsName = new String[]{
                        "订单ID ","征信订单ID","订单状态","支付方式","收款流水号","所属组织","报告生成时间",
                        "报告编号","报告类型名称","报告名称",
                        "报告价格","订单姓名","订单手机号","订单身份证号",
                        "联系人手机号","贡献人ID",
                        "贡献人名称 ","贡献人手机号","发放总奖金额","记账状态","订单生成时间","订单失效时间","退款时间",
                        "一级编号","一级名称","一级身份",
                        "一级分润","二级编号","二级名称","二级身份",
                        "二级分润","三级编号",
                        "三级名称 ","三级身份","三级分润","四级编号","四级名称",
                        "四级身份","四级分润","品牌商分润","平台实际分润","调节系数","领地业务基准分红配置","领地业务基准分红","领地分红","领地分红领取用户编号","领地分红用户姓名","征信系统给银行家成本",
                        "平台分润","省","市","区",
                        "备注"
                };
                ouputStream = response.getOutputStream();
                export.export(cols, colsName, data, response.getOutputStream());
            } catch (Exception e) {
                log.error("导出超级银行家用户管理异常", e);

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
    }


    /**
     * 贷款订单导入
     * @param file
     * @param loanSourceId
     * @return
     */
    @Override
    public Result importLoanOrder(MultipartFile file, String loanSourceId) {
        //1.校验导入文件格式、大小，最多1万条数据
        //2.从序列表取一个订单的批次号
        //3.循环校验每笔订单的数据是否正确
        //4.若数据正确，且订单状态为已通过，更新订单状态为已完成，
        //  并插入批次号字段，条件加上account_status不等于1
        //5.1 return 处理结果
        //5.2 异步调用计算分润接口，参数为批次号
        //6.根据批次号，循环订单，调用记账接口，根据处理结果，更新记账状态
        //7.调用推送接口，参数为批次号
        Result result = new Result();
        //校验excel file的大小和格式
        if (!checkExcelFile(file, result)){
            return result;
        }
        String batchNo = sequenceDao.getValue(Constants.SUPER_BANK_BATCH_NO);
        if(StringUtils.isBlank(batchNo)){
            result.setMsg("批次号未配置");
            return result;
        }
        LoanSource loanSource = loanSourceDao.selectDetail(Long.valueOf(loanSourceId));
        if(loanSource == null || StringUtils.isBlank(loanSource.getRuleCode())){
            result.setMsg("loanSourceId:" + loanSourceId + "找不到贷款机构或者导入编码不能为空");
            return result;
        }
        String ruleCode = loanSource.getRuleCode();
        translateRowImpl.setBatchNo(batchNo);
        translateRowImpl.setLoanSourceId(loanSourceId);
        translateRowImpl.setRuleCode(ruleCode);
        try {
            List<ExcelErrorMsgBean> errors = new ArrayList<>();
            List<OrderMain> list = ExcelUtils.parseWorkbook(file.getInputStream(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    errors, translateRowImpl );
            int successSum = list.size();
            int failSum = errors.size();
            String msg = "";
            StringBuilder errMsg = new StringBuilder();
            if(successSum > 0){
                msg = "导入成功";
            } else {
                msg = "导入失败";
            }
            if(failSum > 0){
                log.info("贷款导入失败，原因:{}", JSONObject.toJSONString(errors));
                for (ExcelErrorMsgBean bean: errors){
                    errMsg.append("\n第" + bean.getRow() + "行，" +
                            "第" + bean.getLine() + "列：" +bean.getMessage() + "；");
                }
//                errMsg =
//                        "，第" + errors.get(0).getRow() + "行，" +
//                        "第" + errors.get(0).getLine() + "列：" +errors.get(0).getMessage();
            }
            if(successSum > 0){
                //批量更新贷款订单
                HashSet<String> orderSet = new HashSet<>();
                List<OrderMain> updateList = new ArrayList<>();
                for (OrderMain item: list){
                    if(StringUtils.isNotBlank(item.getOrderNo())
                            && !orderSet.contains(item.getOrderNo())){
                        orderSet.add(item.getOrderNo());
                        updateList.add(item);
                    }
                }
                successSum = updateList.size();
                if(!(ruleCode.equals(LoanSourceCode.QMG)&&"0".equals(loanSource.getLoanStatus()))){
                    updateOrderStatusBatch(updateList);
                }
                //起一个异步线程，调用计算分润接口，计算完成，调用记账接口，记账成功，调用推送接口
                new Thread(new SuperBankCheckProfitRunnable(batchNo, this)).start();
            }
            result.setStatus(true);
            result.setMsg(msg + "，成功条数：" + successSum + "，失败条数：" + failSum + "；" +  errMsg);

        } catch (Exception e) {
            log.error("贷款订单导入异常", e);
        }
        return result;
    }

    private int updateOrderStatusBatch(List<OrderMain> orderList) {
        int num = 0;
        if(orderList.size() > 0){
            List<OrderMain> itemList = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++){
                itemList.add(orderList.get(i));
                if(i % 200 > 0){
                    num += orderMainDao.updateLoanOrderBatch(itemList);
                    itemList.clear();
                }
            }
            if(itemList.size() > 0){
                num += orderMainDao.updateLoanOrderBatch(itemList);
            }
        }
        return num;
    }

    /**
     * 获取所有的贷款机构
     * @return
     */
    @Override
    public List<LoanSource> getLoanList() {
        return loanSourceDao.getLoanList();
    }

    @Override
    public List<LoanSource> changeSource(String type) {
        List<LoanSource> loanList=new ArrayList<>();
        if("2".equals(type)){
             loanList = this.getLoanList();
        }else{
            List<CreditcardSource> creditcardSources = this.banksList();
            for(CreditcardSource record:creditcardSources){
                LoanSource loanSource=new LoanSource();
                loanSource.setId(record.getId());
                loanSource.setLoanAlias(record.getBankNickName());
                loanList.add(loanSource);
            }
        }
        return loanList;
    }

    /**
     * 校验excel file的大小和格式
     * @param file
     * @param result
     * @return
     */
    private boolean checkExcelFile(MultipartFile file, Result result) {
        if(file == null){
            result.setMsg("请选择上传文件");
            return false;
        }
        if (!file.isEmpty()) {
            if(((file.getSize()/1024)/1024) > 4){
                result.setMsg("上传文件过大,请上传4MB以内的文件");
                return false;
            }
            String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(!format.equals(".xls") && !format.equals(".xlsx")){
                result.setMsg("请选择.xls或.xlsx文件");
                return false;
            }
        }
        return true;
    }

    /**
     * 添加合计
     * @param order
     * @param data
     */
    private void addOrderSumMap(OrderMain order, List<Map<String, String>> data) {
        OrderMainSum orderMainSum = selectOrderSum(order);
        if(orderMainSum != null){
            data.add(new HashMap<String, String>());
            Map<String ,String> sumMap = new HashMap<>();
            //如果是分期还款订单
            if("7".equals(order.getOrderType())||"7".equals(order.getOrderType())||"7,17".equals(order.getOrderType())){
                sumMap.put("orderNo", "目标还款金额汇总:" + String.valueOf(orderMainSum.getReceiveAmountSum()));
                sumMap.put("orderType", "实际消费金额汇总" + String.valueOf(orderMainSum.getRepayTransscuconsSum()));
                sumMap.put("orgId", "实际还款金额汇总:" + String.valueOf(orderMainSum.getRepayAmountSum()));
                sumMap.put("status", "平台分润汇总:" + String.valueOf(orderMainSum.getPlateProfitSum()));
                sumMap.put("userCode", "品牌分润汇总:" + String.valueOf(orderMainSum.getOrgProfitSum()));
                sumMap.put("userName", "平台实际分润汇总:" + String.valueOf(orderMainSum.getActualSum()));
            } else if("2".equals(order.getOrderType())){
                //如果是办理信用卡的订单
                sumMap.put("orderNo", "银行发卡奖金汇总:" + String.valueOf(orderMainSum.getCreditcardBankBonusSum()));
                sumMap.put("orgId", "银行首刷奖金汇总:" + String.valueOf(orderMainSum.getCreditcardBankBonus2Sum()));
                sumMap.put("orgName", "发卡平台分润汇总:" + String.valueOf(orderMainSum.getPlateProfitSum()));
                sumMap.put("status", "首刷平台分润汇总:" + String.valueOf(orderMainSum.getPlateProfit2Sum()));
                sumMap.put("profitStatus2", "发卡品牌分润汇总:" + String.valueOf(orderMainSum.getOrgProfitSum()));
                sumMap.put("bankCode", "首刷品牌分润汇总:" + String.valueOf(orderMainSum.getOrgProfit2Sum()));
            } else if("5,6".equals(order.getOrderType())){
                //如果是贷款的订单
                sumMap.put("orderNo", "贷款金额汇总:" + String.valueOf(orderMainSum.getLoanAmountSum()));
                sumMap.put("orgId", "贷款机构发放总奖金汇总:" + String.valueOf(orderMainSum.getPriceSum()));
                sumMap.put("orderType", "平台分润汇总:" + String.valueOf(orderMainSum.getPlateProfitSum()));
                sumMap.put("status", "品牌分润汇总:" + String.valueOf(orderMainSum.getOrgProfitSum()));
                sumMap.put("loanName", "平台实际分润汇总:" + String.valueOf(orderMainSum.getActualSum()));
                sumMap.put("loanAlias", "领地业务分红汇总:" + String.valueOf(orderMainSum.getTerritorySum()));
            } else if("3".equals(order.getOrderType())){
                //如果是还款的订单
                sumMap.put("orderNo", "目标还款金额汇总:" + String.valueOf(orderMainSum.getReceiveAmountSum()));
                sumMap.put("orgId", "实际还款金额汇总:" + String.valueOf(orderMainSum.getRepayAmountSum()));
                sumMap.put("status", "平台分润汇总:" + String.valueOf(orderMainSum.getPlateProfitSum()));
                sumMap.put("repayTransStatus", "品牌分润汇总:" + String.valueOf(orderMainSum.getOrgProfitSum()));
                sumMap.put("userCode", "平台实际分润汇总:" + String.valueOf(orderMainSum.getActualSum()));
                sumMap.put("userName", "领地业务分红汇总:" + String.valueOf(orderMainSum.getTerritorySum()));
            }else {
                sumMap.put("orderNo", "奖励总金额:" + String.valueOf(orderMainSum.getTotalBonusSum()));
                sumMap.put("orgId", "平台分润汇总:" + String.valueOf(orderMainSum.getPlateProfitSum()));
                sumMap.put("status", "品牌分润汇总:" + String.valueOf(orderMainSum.getOrgProfitSum()));
            }
            data.add(sumMap);
        }
    }

    /**
     * 添加合计
     * @param order
     * @param data
     */
    private void addOrderSumMap2(OrderMain order, List<Map<String, String>> data) {
        OrderMainSum orderMainSum = selectOrderSum(order);
        if(orderMainSum != null){
            data.add(new HashMap<String, String>());
            Map<String ,String> sumMap = new HashMap<>();
            sumMap.put("orgId", "平台分润汇总:" + String.valueOf(orderMainSum.getPlateProfitSum()));
            sumMap.put("status", "品牌分润汇总:" + String.valueOf(orderMainSum.getOrgProfitSum()));
            data.add(sumMap);
        }
    }

    @Override
    public Result getCreditBonusConf(CreditCardBonus creditCardconf,
                                     Page<CreditCardBonus> page) {
        Result result = new Result();
        creditCardDao.getCreditCardConf(creditCardconf, page);
        List<CreditCardBonus> list = page.getResult();
        for(CreditCardBonus item:list){
            if(null!= item.getOrgId() && item.getOrgId()==0){
            	item.setOrgName("默认");
            }
        }
        result.setData(page);
        return result;
    }

    @Override
    public List<CreditcardSource> banksList() {
        List<CreditcardSource>  list = creditCardDao.allBanksList();
        return list;
    }

    //插入信息到org_info_open_conf
    public void insertOrgInfoOpenConf(OrgInfo orgInfo){
        Date date = new Date();
        orgInfo.setCreateDate(date);
        orgInfo.setUpdateDate(date);
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        orgInfo.setUpdateBy(Long.valueOf(loginUser.getId()));
        long orgId=orgInfo.getOrgId();
        String createBy=loginUser.getId().toString();
        Date createDate=new Date();
        List<OpenFunctionConf> list = orgInfo.getOpenFunctionConfList();
        List<OrgInfoOpenConf> orgInfoOpenConfs = new ArrayList<>();
        if(list!=null&&list.size()>0){
            for(OpenFunctionConf openFunctionConf :list){
                OrgInfoOpenConf orgInfoOpenConf=new OrgInfoOpenConf();
                orgInfoOpenConf.setFunctionId(openFunctionConf.getId()+"");
                orgInfoOpenConf.setOrgId(orgId);
                orgInfoOpenConf.setCreateBy(createBy);
                if(1==openFunctionConf.getIsEnable()){
                    orgInfoOpenConf.setIsEnable("1");
                }else{
                    orgInfoOpenConf.setIsEnable("0");
                }
                orgInfoOpenConf.setCreateDate(createDate);
                orgInfoOpenConfs.add(orgInfoOpenConf);
            }
            orgInfoOpenConfDao.insertBatch(orgInfoOpenConfs);
        }
    }
    //默认信用卡配置
    public void addDefaultCreditCardConf(Long orgId){
        String code = "610010";//直营组织编号
        if(orgId != null && String.valueOf(orgId).equals(code)){
            return;
        }
        List<CreditcardSource>  list = creditCardDao.allBanksList();
        List<CreditCardBonus> directSales = creditCardDao.getDirectSalesConf(code);//直营配置

        String userName = getUserName();

        for(CreditcardSource cs : list){//银行
            for(CreditCardBonus cb : directSales){//配置
                if(cs.getId().equals(cb.getSourceId())){
                    CreditCardBonus conf = new CreditCardBonus();
                    conf.setOrgId(orgId);
                    conf.setSourceId(cs.getId());
                    conf.setOrgCost(cb.getOrgCost());
                    conf.setOrgPushCost(cb.getOrgPushCost());
                    conf.setUpdateBy(userName);
                    conf.setIsOnlyone(cb.getIsOnlyone());
                    conf.setCardCompanyBonus(cb.getCardCompanyBonus());
                    conf.setCardOemBonus(cb.getCardOemBonus());
                    conf.setFirstCompanyBonus(cb.getFirstCompanyBonus());
                    conf.setFirstOemBonus(cb.getFirstOemBonus());
                    creditCardDao.saveCreditCardConf(conf);
                }
            }
        }
    }

    //默认贷款配置
    public void addDefaultLoanConf(Long orgId){
        String code = "610010";//直营组织编号
        if(orgId != null && String.valueOf(orgId).equals(code)){
            return;
        }

//        List<LoanSource> loanList = loanSourceDao.getLoanList();//所有贷款机构
        List<LoanBonusConf> directSales = loanCompanyDao.getDirectSalesConf(code);//直营配置

        String userName = getUserName();

//		for(LoanSource ls : loanList){
//			for(LoanBonusConf lbc : directSales){//配置
//				if(ls.getId().equals(lbc.getSourceId())){
//					LoanBonusConf conf = new LoanBonusConf();
//					conf.setOrgId(orgId);
//					conf.setSourceId(ls.getId());
//					conf.setOrgCostLoan(lbc.getOrgCostLoan());
//					conf.setOrgCostReg(lbc.getOrgCostReg());
//					conf.setOrgPushLoan(lbc.getOrgPushLoan());
//					conf.setOrgPushReg(lbc.getOrgPushReg());
//					conf.setUpdateBy(userName);
//					conf.setProfitType(lbc.getProfitType());
//					loanCompanyDao.saveLoanConf(conf);
//				}
//			}
//		}
        for(LoanBonusConf lbc : directSales){//配置
            LoanBonusConf conf = new LoanBonusConf();
            conf.setOrgId(orgId);
            conf.setSourceId(lbc.getSourceId());
            conf.setOrgCostLoan(lbc.getOrgCostLoan());
            conf.setOrgCostReg(lbc.getOrgCostReg());
            conf.setOrgPushLoan(lbc.getOrgPushLoan());
            conf.setOrgPushReg(lbc.getOrgPushReg());
            conf.setUpdateBy(userName);
            //conf.setProfitType(lbc.getProfitType());
            //conf.setBusiType(lbc.getBusiType());
            //conf.setTotalCost(lbc.getTotalCost());
            loanCompanyDao.saveLoanConf(conf);
        }
    }

    /**
     * 修改条件      信用卡配置ID，银行ID
     * 修改内容      品牌组织成本，品牌组织发放奖金，银行发放奖金
     */
    @Override
    public int updCreditCardConf(CreditCardBonus creditCardConf) {
        if(creditCardConf == null){
            return 0;
        }

        String userName = getUserName();
        creditCardConf.setUpdateBy(userName);

//		int cs,cf ;
//
//		cs = creditCardDao.updBankConf(creditCardConf);
//		cf = creditCardDao.updCreditCardConf(creditCardConf);
//
//		if(cs > 0 && cf > 0){
//			return 1;
//		}else{
//			return 0;
//		}
        return creditCardDao.updCreditCardConf(creditCardConf);
    }

    @Override
    public int addBank(CreditCardBonus creditCardConf) {
    	 String updBy = getUserName();
    	 creditCardConf.setUpdateBy(updBy);
                creditCardDao.saveCreditCardConf(creditCardConf);
        return 1;
    }

    /**
     * 超级银行家订单详情
     * @param orderNo
     * @return
     */
    @Override
    public OrderMain selectOrderDetail(String orderNo) {
        if(StringUtils.isBlank(orderNo)){
            return null;
        }
        OrderMain orderMain = orderMainDao.selectOrderDetail(orderNo);

        List<OrderMain> list = new ArrayList<>();
        list.add(orderMain);
        filterOrderParam(list);//过滤orderMain里面的日期、身份、订单状态、记账状态等
//        filterProfitFormula(orderNo, orderMain);//给orderMain一二三四级分润规则赋值
        //订单表本身就有，要取当时的记录
        //银行发放总奖金
//        getBankGiveAmount(orderMain);
        return list.get(0);
    }

    /**
     * 超级银行家订单详情
     * @param orderNo
     * @return
     */
    @Override
    public OrderMain selectRepayOrderDetail(String orderNo) {
        if(StringUtils.isBlank(orderNo)){
            return null;
        }
        OrderMain orderMain = orderMainDao.selectRepayOrderDetail(orderNo);

        List<OrderMain> list = new ArrayList<>();
        list.add(orderMain);
        filterOrderParam(list);//过滤orderMain里面的日期、身份、订单状态、记账状态等
//        filterProfitFormula(orderNo, orderMain);//给orderMain一二三四级分润规则赋值
        //订单表本身就有，要取当时的记录
        //银行发放总奖金
//        getBankGiveAmount(orderMain);
        return list.get(0);
    }


    //银行发放总奖金，实时查
    private void getBankGiveAmount(OrderMain orderMain){

        Long sourceId = orderMain.getBankSourceId();
        String totalAmount = orderMainDao.getBankPushAmount(sourceId);

        if(totalAmount==null){
            totalAmount = "0.00";
        }

        System.out.println("---------"+sourceId+"-------------"+totalAmount+"---------");

        orderMain.setLoanAmount(new BigDecimal(totalAmount));
    }

    /**
     * 给orderMain平台、组织、一、二、三、四级分润规则赋值
     * @param orderNo
     * @param orderMain
     */
    private void filterProfitFormula(String orderNo, OrderMain orderMain) {
        List<UserProfit> userProfitList = userProfitDao.selectByOrderNo(orderNo);
        if(userProfitList != null && userProfitList.size() > 0){
            for (UserProfit userProfit : userProfitList){
                //用户分润
                if(userProfit.getUserType() != null){
                    switch (userProfit.getUserType()){
                        case "10":
                            orderMain.setOneProfitFormula(userProfit.getProfitFormula());
                            break;
                        case "20":
                            orderMain.setTwoProfitFormula(userProfit.getProfitFormula());
                            break;
                        case "30":
                            orderMain.setThrProfitFormula(userProfit.getProfitFormula());
                            break;
                        case "40":
                            orderMain.setFouProfitFormula(userProfit.getProfitFormula());
                            break;
                        case "50":
                            orderMain.setOrgProfitFormula(userProfit.getProfitFormula());
                            break;
                        case "60":
                            orderMain.setPlateProfitFormula(userProfit.getProfitFormula());
                            break;
                    }
                }
            }
        }
    }

    /**
     * 过滤订单里面的数据
     * @param list
     */
    public void filterOrderParam(List<OrderMain> list){
        Map<String, String> orderStatusMap = getOrderStatusMap();
        Map<String, String> accountStatusMap = getAccountStatusMap();
        Map<String, String> userTypeMap = getUserTypeMap();
        Map<String, String> payMethodMap = getPayMethodMap();
        Map<String, String> orderTypeMap = getOrderTypeMap();
        Map<String, String> packBackMap = getPayBackMap();
        Map<String, String> profitTypeMap = getProfitTypeMap();
        Map<String, String> loanTypeMap = getLoanTypeMap();
        Map<String, String> proofreadingMethodMap = getProofreadingMethodMap();
        Map<String, String> proofreadingResultMap = getProofreadingResultMap();
//        Map<String, String> cardTypeMap = getCardTypeMap();
//        Map<String, Object> bankMap = getBankMap();//银行名称和ID对应的map
        Map<String, String> repayTransStatusMap = getRepayTransStatusMap();//银行名称和ID对应的map
        Map<String, String> completeStatusMap = getCompleteStatusMap();
        SysOption sysOption = sysOptionDao.selectByCode("REPAYREDWEPROFIT");
        String companyCostRate = null;//公司成本扣率
        if(sysOption != null){
            companyCostRate = sysOption.getName();//公司成本扣率
            if(!companyCostRate.endsWith("%")){
                companyCostRate = companyCostRate + "%";
            }
        }
        if(list != null && list.size() > 0){
            for(OrderMain order: list){
                order.setOneUserType(userTypeMap.get(order.getOneUserType()));
                order.setTwoUserType(userTypeMap.get(order.getTwoUserType()));
                order.setThrUserType(userTypeMap.get(order.getThrUserType()));
                order.setFouUserType(userTypeMap.get(order.getFouUserType()));
                order.setStatus(orderStatusMap.get(order.getStatus()));
                order.setAccountStatus(accountStatusMap.get(order.getAccountStatus()));
                order.setPayMethod(payMethodMap.get(order.getPayMethod()));
                order.setProofreadingResult(proofreadingResultMap.get(order.getProofreadingResult()));
                order.setProofreadingMethod(proofreadingMethodMap.get(order.getProofreadingMethod()));
                if(order.getTransRate() != null && !"".equals(order.getTransRate()) && !order.getTransRate().endsWith("%")){
                	BigDecimal transRateDecimal = null ;
                	try {
                		transRateDecimal = new BigDecimal(order.getTransRate()).setScale(2,BigDecimal.ROUND_DOWN);
					} catch (Exception e) {
					}
                    order.setTransRate(transRateDecimal + "%");
                    //如果是还款订单，需要加上repay_transfee_add
                    if("7".equals(order.getOrderType()) && order.getRepayTransfeeAdd() != null||
                    		"17".equals(order.getOrderType()) && order.getRepayTransfeeAdd() != null){
                        order.setTransRate(order.getTransRate() + "+" +  order.getRepayTransfeeAdd());
                    }
                }
                if(order.getAgentInputRate() != null && !"".equals(order.getAgentInputRate()) && !order.getAgentInputRate().endsWith("%")){
                    order.setAgentInputRate(order.getAgentInputRate() + "%");
                }
                if(order.getLoanOrgRate() != null && !"".equals(order.getLoanOrgRate()) && !order.getLoanOrgRate().endsWith("%")){
                    order.setLoanOrgRate(order.getLoanOrgRate() + "%");
                }
                if(order.getLoanOrgBonus() != null && !"".equals(order.getLoanOrgBonus()) && !order.getLoanOrgBonus().endsWith("%")
                        && !"1".equals(order.getProfitType()) ){
                    order.setLoanOrgBonus(order.getLoanOrgBonus() + "%");
                }
                if(order.getReceiveOrgRate() != null && !"".equals(order.getReceiveOrgRate()) && !order.getReceiveOrgRate().endsWith("%")){
                    order.setReceiveOrgRate(order.getReceiveOrgRate() + "%");
                }
                if(order.getReceiveOrgBonus() != null && !"".equals(order.getReceiveOrgBonus()) && !order.getReceiveOrgBonus().endsWith("%")){
                    order.setReceiveOrgBonus(order.getReceiveOrgBonus() + "%");
                }
                if(StringUtils.isNotBlank(order.getLoanBankRate()) && !"1".equals(order.getProfitType()) && !order.getLoanBankRate().endsWith("%")){
                    order.setLoanBankRate(order.getLoanBankRate() + "%");
                }
                if(order.getReceiveTime()!=null){
                    order.setReceiveTimeStr(DateUtils.format(order.getReceiveTime(),"yyyy-MM-dd HH:mm:ss"));
                }
                if(StringUtils.isNotBlank(order.getPayChannel())){
                    if("wx".equals(order.getPayChannel())){
                        order.setPayChannel("微信官方");
                    }if("ali".equals(order.getPayChannel())){
                        order.setPayChannel("支付宝官方");
                    }
                }
                if(1==order.getRateType()){
                    order.setRate(order.getRate()+"元");
                }if(2==order.getRateType()){
                    order.setRate(order.getRate()+"%");
                }
                order.setOrderType(orderTypeMap.get(order.getOrderType()));
                order.setRefundStatus(packBackMap.get(order.getRefundStatus()));
//                if(order.getBankSourceId() != null){
//                    CreditcardSource bankInfo = (CreditcardSource) bankMap.get(order.getBankSourceId());
//                    order.setBankName(bankInfo.getBankName());
//                    order.setBankNickName(bankInfo.getBankNickName());
//                }
                if(StringUtils.isNotBlank(order.getPayChannel())){
                    if("wx".equals(order.getPayChannel())){
                        order.setPayChannel("微信官方");
                    }
                    if("ali".equals(order.getPayChannel())){
                        order.setPayChannel("支付宝官方");
                    }
                }
                order.setCompanyCostRate(companyCostRate);
                order.setRepayTransStatus(repayTransStatusMap.get(order.getRepayTransStatus()));
                order.setAccountStatus2(accountStatusMap.get(order.getAccountStatus2()));
                order.setProfitStatus2(completeStatusMap.get(order.getProfitStatus2()));
                order.setProfitTypeStr(profitTypeMap.get(order.getProfitType()));
                order.setLoanTypeStr(loanTypeMap.get(order.getLoanType()));
//                order.setCardType(cardTypeMap.get(order.getCardType()));
            }
        }
    }

    /**
     * 过滤分润详情列表里面的数据
     * @param list
     */
    private void filterProfitDetailParam(List<UserProfit> list) {
        Map<String, String> orderStatusMap = getOrderStatusMap();
        Map<String, String> orderTypeMap = getOrderTypeMap();
        Map<String, String> accountStatusMap = getAccountStatusMap();
        Map<Long, String> orgNameMap = getOrgNameMap();
        Map<String, String> userTypeMap = getUserTypeMap();
        if(list != null && list.size() > 0){
            for(UserProfit order: list){
                order.setStatus(orderStatusMap.get(order.getStatus()));
                order.setOrderType(orderTypeMap.get(order.getOrderType()));
                order.setAccountStatus(accountStatusMap.get(order.getAccountStatus()));
                order.setOrgName(orgNameMap.get(order.getOrgId()));
                if("50".equals(order.getUserType())){
                    order.setUserName(order.getOrgName());
                }
                if("60".equals(order.getUserType())){
                    order.setUserName("默认");
                }
                order.setUserType(userTypeMap.get(order.getUserType()));
                if(StringUtils.isNotBlank(order.getShareNickName())){
                    try {
                        order.setShareNickName(URLDecoder.decode(order.getShareNickName(), "utf-8"));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 过滤用户提现记录
     * @param list
     */
    private void filterObtainRecordParam(List<UserObtainRecord> list){
        Map<String, String> orderStatusMap = getObtainStatusMap();
        Map<String, String> orderTypeMap = getObtainTypeMap();
        Map<String, String> userTypeMap = getUserTypeMap();
        Map<Long, String> orgInfoMap = getOrgNameMap();
        if(list != null && list.size() > 0){
            for(UserObtainRecord order: list){
                order.setStatus(orderStatusMap.get(order.getStatus()));
                order.setOrderType(orderTypeMap.get(order.getOrderType()));
                if(StringUtils.isNotBlank(order.getNickName())){
                    try {
                        order.setNickName(URLDecoder.decode(order.getNickName(), "utf-8"));
                    } catch (Exception e) {
                    }
                }
                if(StringUtils.isNotBlank(order.getUserType())){
                    order.setUserType(userTypeMap.get(order.getUserType()));
                }
                if(order.getOrgId() != null){
                    order.setOrgName(orgInfoMap.get(order.getOrgId()));
                }
            }
        }
    }

    /**
     * 获取支付方式
     * @return
     */
    private Map<String, String> getPayMethodMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "微信");
        map.put("2", "支付宝");
        map.put("3", "快捷");
        return map;
    }

    /**
     * 获取代理开通状态
     * @return
     */
    private Map<String, String> getAgentStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("0", "未开通");
        map.put("1", "已开通");
        return map;
    }

    /**
     * 获取还款状态
     * @return
     */
    private Map<String, String> getRepayStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put(null, "未开通");
        map.put("", "未开通");
        map.put("0", "未开通");
        map.put("1", "已开通");
        return map;
    }

    /**
     * 获取还款状态
     * 还款状态:3成功，4失败，6终止
     * @return
     */
    private Map<String, String> getRepayTransStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("3", "成功");
        map.put("4", "失败");
        map.put("6", "终止");
        return map;
    }

    /**
     * 获取收款状态
     * @return
     */
    private Map<String, String> getReceiveStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put(null, "未开通");
        map.put("", "未开通");
        map.put("0", "未开通");
        map.put("1", "待审核");
        map.put("2", "审核失败");
        map.put("3", "已开通");
        return map;
    }

    /**
     * 结算状态
     * @return
     */
    public Map<String, String> getObtainStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "未提交");
        map.put("2", "结算中");
        map.put("3", "已结算");
        map.put("4", "结算失败");
        return map;
    }

    /**
     * 结算订单类型
     * @return
     */
    public Map<String, String> getObtainTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "用户分润提现");
        return map;
    }

    /**
     * 订单状态集合转换为map
     * @return
     */
    public Map<String, String> getUserTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("10", "用户");
        map.put("20", "专员");
        map.put("30", "经理");
        map.put("40", "银行家");
        map.put("50", "OEM");
        map.put("60", "平台");
        return map;
    }

    /**
     * 入账状态集合转换为map
     * @return
     */
    public Map<String, String> getAccountStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put(null, "待入账");
        map.put("", "待入账");
        map.put("0", "待入账");
        map.put("1", "已记账");
        map.put("2", "记账失败");
        return map;
    }

    /**
     * 订单状态集合转换为map
     * @return
     */
    public Map<String, String> getOrderStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "已创建");
        map.put("2", "待支付");
        map.put("3", "待审核");
        map.put("4", "已授权");
        map.put("5", "已完成");
        map.put("6", "审核不通过");
        map.put("7", "已办理过");
        map.put("9", "回收站");
        map.put("10", "已删除");
        return map;
    }

    /**
     * 订单类型:1代理授权；2信用卡申请 3收款 4信用卡还款 5贷款
     * @return
     */
    public Map<String, String> getOrderTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "代理授权");
        map.put("2", "信用卡申请");
        map.put("3", "收款");
        map.put("4", "信用卡还款");
        map.put("5", "贷款注册");
        map.put("6", "贷款批贷");
        map.put("7", "分期计划");
        map.put("8", "彩票代购");
        map.put("10", "征信");
        map.put("11", "红包领地");
        map.put("14", "违章代缴");
        map.put("15", "保险");
        map.put("16", "积分商城");
        map.put("17", "完美计划");
        return map;
    }

    /**
     * 退款状态集合转换为map
     * @return
     */
    public Map<String, String> getPayBackMap(){
        Map<String, String> map = new HashMap<>();
        map.put("0", "未退款");
        map.put("1", "已退款");
        return map;
    }

    /**
     * 退款状态集合转换为map
     * @return
     */
    public Map<String, String> getMentorMap(){
        Map<String, String> map = new HashMap<>();
        map.put("0", "否");
        map.put("1", "是");
        return map;
    }

    /**
     * 退款状态集合转换为map
     * @return
     */
    public Map<String, String> getProfitTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "固定金额");
        map.put("2", "按借款金额比例");
        return map;
    }

    /**
     * 贷款订单奖励类型集合转换为map
     * @return
     */
    public Map<String, String> getLoanTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "有效注册");
        map.put("2", "有效借款");
        map.put("3", "授信成功");
        return map;
    }

    /**
     * 卡类型
     * @return
     */
//    public Map<String, String> getCardTypeMap(){
//        Map<String, String> map = new HashMap<>();
//        map.put("1", "普通卡");
//        map.put("2", "校园卡");
//        return map;
//    }

    /**
     * 开户状态集合转换为map
     * @return
     */
    public Map<String, String> getOpenAccountMap(){
        Map<String, String> map = new HashMap<>();
        map.put("0", "未开户");
        map.put("1", "已开户");
        return map;
    }

    /**
     * 用户状态集合转换为map
     * @return
     */
    public Map<String, String> getUserStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "正常");
        map.put("2", "冻结");
        return map;
    }

    /**
     * 用户状态集合转换为map
     * @return
     */
    public Map<String, String> getPayMoneyStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("0", "未缴费");
        map.put("1", "已缴费");
        return map;
    }

    /**
     * 将所有orgInfo的orgId和orgName组成map
     * @return
     */
    public Map<Long, String> getOrgNameMap(){
        Map<Long, String> map = new HashMap<>();
        List<OrgInfo> list = orgInfoDao.getOrgInfoList();
        if(list != null && list.size() > 0){
            for(OrgInfo info: list){
                map.put(
                        info.getOrgId() == null? -1L : info.getOrgId(),
                        info.getOrgName() == null?"" : info.getOrgName());
            }
        }
        return map;
    }

    /**
     * 完成状态集合转换为map
     * @return
     */
    public Map<String, String> getCompleteStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("0", "未完成");
        map.put("1", "已完成");
        return map;
    }

    /**
     * 秒结状态集合转换为map
     * @return
     */
    public Map<String, String> getProofreadingMethodMap(){
        Map<String, String> map = new HashMap<>();
        map.put("", "未发放");
        map.put("0", "未发放");
        map.put("1", "查询秒结");
        map.put("2", "自动结算");
        return map;
    }

    /**
     * 校对结果态集合转换为map
     * @return
     */
    public Map<String, String> getProofreadingResultMap(){
        Map<String, String> map = new HashMap<>();
        map.put("", "未校验");
        map.put("0", "未校验");
        map.put("1", "校验成功");
        return map;
    }

    /**获取所有贷款机构*/
    @Override
    public List<LoanSource> loanCompanyList() {

        return loanCompanyDao.getLoanCompanies();
    }

    /**分页查询彩票配置**/
    @Override
    public List<LotteryBonusConf> getLotteryBonusConfList(LotteryBonusConf conf,
                                                          Page<LotteryBonusConf> page) {

        return lotteryBonusConfDao.getLotteryBonusConf(conf, page);
    }

    /**新增彩票奖励配置*/
    @Override
    public int saveLotteryBonusconf(LotteryBonusConf conf) {
        String updBy = getUserName();
        conf.setUpdateBy(updBy);
        int count = lotteryBonusConfDao.saveLotteryConf(conf);
        if(count == 0){
            return 0;
        }
        return 1;
    }

    /**修改组织彩票配置*/
    @Override
    public int updLotteryBonusconf(LotteryBonusConf conf) {
        String updBy = getUserName();
        conf.setUpdateBy(updBy);

        int confCount = lotteryBonusConfDao.updLotteryConf(conf);
        if(confCount==0){
            return 0;
        }
        return 1;
    }

    /**修改组织彩票配置*/
    @Override
    public int checkLotteryBonusconfExist(Long orgId) {

        int confCount = lotteryBonusConfDao.checkExists(orgId);
        if(confCount==0){
            return 0;
        }
        return 1;
    }

    /**分页查询彩票导入记录**/
    @Override
    public List<LotteryImportRecords> getLotteryImportRecordsList(LotteryImportRecords record,
                                                                  Page<LotteryImportRecords> page) {

        return lotteryImportRecordsDao.getLotteryImportRecords(record, page);
    }

    /**新增彩票导入记录*/
    @Override
    public int saveLotteryImportRecord(LotteryImportRecords record) {
        int count = lotteryImportRecordsDao.saveLotteryImportRecords(record);
        if(count == 0){
            return 0;
        }
        return 1;
    }

    /**根据id删除彩票导入记录*/
    @Override
    public int deleteLotteryImportRecord(Long id){
        return lotteryImportRecordsDao.deleteLotteryImportRecord(id);
    }

    /**根据id获取彩票导入记录*/
    @Override
    public LotteryImportRecords getLotteryImportRecordById(String id){
        return lotteryImportRecordsDao.getLotteryImportRecordById(id);
    }

    /**彩票导入文件匹配定时任务*/
    @Override
    public void lotteryMatchTask() {
        //获取未匹配的导入记录
        List<LotteryImportRecords> records = lotteryImportRecordsDao.getLotteryListByStatus();
        if(records == null || records.size() == 0){
            log.info("~~~INFO:没有状态为未匹配的彩票导入记录~~~~~~~~~~");
            return;
        }
        SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
        if(sysDict == null){
            log.info("~~~~~~~~~~~~~~~~~超级银行家订单接口数字字典SUPER_BANK_INTEFACE_URL没有配置，请配置好~~~~~~~~~~~~~~~~~~~~~~");
            return;
        }
        int totalValidCount = 0; //总有效条数
        for(LotteryImportRecords record : records){
            if(record.getFileUrl() == null || "".equals(record.getFileUrl())){
                log.info("~~~ERROR:导入记录批次号:" + record.getBatchNo() + "的记录不存在File URL 不执行匹配~~~~~~~~~~~~");
                continue;
            }
            log.info("~~~INFO:执行批次号为：" + record.getBatchNo()+"的彩票导入记录匹配~~~~~~~");
            java.net.URLConnection conn = null;
            java.io.InputStream is = null;
            try {
                java.net.URL url = new java.net.URL(record.getFileUrl());
                conn = url.openConnection();
                is = conn.getInputStream();
                List<ExcelErrorMsgBean> errors = new ArrayList<>();
                List<LotteryOrder> list = null;
                if("1".equals(record.getLotteryType())){
                    list = ExcelUtils.parseWorkbook(is,
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            errors, lotteryRowImpl );
                }else if("2".equals(record.getLotteryType())){
                    list = ExcelUtils.parseWorkbook(is,
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            errors, sportLotteryRowImpl );
                }


                //1.该订单购买状态不是出票成功;2.用户不存在;3记录已存在
                int failNum = 0; //出票失败条数
                int unkownUser = 0;//用户不存在条数
                int existRecord = 0;//已存在条数
                int totalCount = 0; //总条数
                int validCount = 0; //有效条数
                int failCount = 0;  //异常条数
                int valiDateCount = 0;//非法的时间格式
                int emptyCount =0;  //设备号或者设备流水号为空
                if(errors != null){
                    failCount = errors.size();
                    for(ExcelErrorMsgBean errorBean: errors){
                        if("1".equals(errorBean.getMessage())){
                            ++failNum;
                        }else if("2".equals(errorBean.getMessage())){
                            ++unkownUser;
                        }else if("3".equals(errorBean.getMessage())){
                            ++existRecord;
                        }else if("4".equals(errorBean.getMessage())){
                            ++valiDateCount;
                        }else if("5".equals(errorBean.getMessage())){
                            ++ emptyCount;
                        }
                    }
                }
                if(list == null || list.size() == 0){
                    log.info("~~~~~~INFO:批次号为" + record.getBatchNo()+"的彩票导入记录不存在需要导入的彩票记录~~~~");
                }else{
                    validCount = list.size();
                }

                totalCount = failCount + validCount;
                String remark = "总记录数： "+totalCount+" ;匹配成功："+validCount+" 匹配失败： "+failCount+" (其中：出票失败:"+failNum+";用户不存在："+unkownUser+";已存在记录："+existRecord+";非法时间："+valiDateCount+";其他："+emptyCount+")";

                log.info("~~~~~~~"+remark);
                log.info("~~~~~~~批量修改彩票表的批次号");
                if("1".equals(record.getLotteryType())){
                    updateLotteryBatchNoBatch(list, record.getBatchNo());
                }else if("2".equals(record.getLotteryType())){
                    batchUpdSportLottery(list, record.getBatchNo());
                }

                log.info("~~~~~~~~~更新导入记录状态为匹配中~~~~~~~~~");
                Map<String,String> param = new HashMap<String,String>();
                param.put("status", "2"); //1.待匹配，2.已匹配
                param.put("batchNo", record.getBatchNo());
                param.put("remark", remark);

                lotteryImportRecordsDao.updateLotteryListByBatchNo(param);


                totalValidCount += list.size();

            } catch (Exception e) {
                log.info("~~~EXCEPTION:执行批次号为：" + record.getBatchNo()+"的彩票导入记录异常~~~~~~~");
                e.printStackTrace();
            }finally{
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(totalValidCount == 0){
            log.info("~~~~~有效条数为0，不用调用超级银行家接口生成主订单及进行分润操作~~~~~~~");
            return;
        }
        if(sysDict != null){
            new Thread(new LotteryGenOrderAndProfitRunnable(sysDict.getSysValue())).start();
        }
    }

    public void updateLotteryBatchNoBatch(List<LotteryOrder> orderList,String batchNo) {
        if(orderList.size() > 0){
            List<LotteryOrder> itemList = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++){
                LotteryOrder lottery = orderList.get(i);
                lottery.setBatchNo(batchNo);
                itemList.add(lottery);
                if(i % 200 > 0){
                    lotteryOrderDao.updateLotteryBatchNoBatch(itemList);
                    itemList.clear();
                }
            }
            if(itemList.size() > 0){
                lotteryOrderDao.updateLotteryBatchNoBatch(itemList);
            }
        }
    }

    public void batchUpdSportLottery(List<LotteryOrder> orderList,String batchNo) {
        if(orderList.size() > 0){
            List<LotteryOrder> itemList = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++){
                LotteryOrder lottery = orderList.get(i);
                lottery.setBatchNo(batchNo);
                itemList.add(lottery);
                if(i % 200 > 0){
                    lotteryOrderDao.batchUpdbatchNo(itemList);
                    itemList.clear();
                }
            }
            if(itemList.size() > 0){
                lotteryOrderDao.batchUpdbatchNo(itemList);
            }
        }
    }

    /**
     * 生成订单主表记录
     * @param
     * @return
     *
     **/
    public OrderMain orderMainGenerator(LotteryOrder lottery,String batchNo){
        if(lottery == null){
            return null;
        }
        OrderMain order = new OrderMain();
        order.setOrderNo(lottery.getOrderNo()); //订单编号
        order.setUserCode(lottery.getUserCode());//用户编号
        if(lottery.getOrgId() != null && !"".equals(lottery.getOrgId())){
            Long orgId = Long.valueOf(String.valueOf(lottery.getOrgId()));
            order.setOrgId(orgId);//组织编号
            /**获取组织名称*/
            OrgInfo orgInfo = orgInfoDao.selectOrg(orgId);
            if(orgInfo != null){
                order.setOrgName(orgInfo.getOrgName());//组织名称
            }
        }
        order.setOrderType("8");//8 彩票
        order.setStatus("5");   // 5订单成功
        order.setBatchNo(batchNo); //导入批次号
        return order;

    }


    /**
     * 生成Order No
     * @param memberId
     * @return
     *
     **/
    public static String getOrderNo(String memberId){
        String orderNo = "SU";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Random ne=new Random();
        int ram=ne.nextInt(9999-1000+1)+1000;
        if(memberId != null){
            orderNo += sdf.format(new Date()) + String.valueOf(ram) + memberId;
        }else{
            orderNo += sdf.format(new Date()) + String.valueOf(ram);
        }
        return orderNo;
    }


    /**
     * 信用卡办理记录导入
     * @param file
     * @return
     */
    @Override
    public Result importLottery(MultipartFile file) {
        Result result = new Result();
        //校验excel file的大小和格式
        if (!checkExcelFile(file, result)){
            return result;
        }
        try {
            List<ExcelErrorMsgBean> errors = new ArrayList<>();
            List<CreditcardApplyRecord> list = ExcelUtils.parseWorkbook(file.getInputStream(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    errors, creditRowImpl );
            int successSum = insertBatchCreditRecord(list);
            int failSum = errors.size();
            String msg = "";
            String errMsg = "";
            if(successSum > 0){
                msg = "导入成功";
            } else {
                msg = "导入失败";
            }
            if(failSum > 0){
                log.info("导入失败，原因:{}", JSONObject.toJSONString(errors));
                errMsg = "，第" + errors.get(0).getRow() + "行，" +
                        "第" + errors.get(0).getLine() + "列：" +errors.get(0).getMessage();
            }
            result.setStatus(true);
            result.setMsg(msg + "，成功条数：" + successSum + "，失败条数：" + failSum + errMsg);
        } catch (Exception e) {
            log.error("信用卡办理记录导入异常", e);
        }
        return result;
    }


    /**分页查询贷款配置**/
    @Override
    public List<LoanBonusConf> getLoanBonusConfList(LoanBonusConf conf,
                                                    Page<LoanBonusConf> page) {
        loanCompanyDao.getLoanBonusConf(conf, page);
        List<LoanBonusConf> list = page.getResult();
        if(list != null && list.size() > 0){
            Map<Long, String> orgMap =  getOrgNameMap();
            orgMap.put(0L, "默认");
            for(LoanBonusConf item : list){
                item.setOrgName(orgMap.get(item.getOrgId()));
            }
        }
        return list;
    }

    /**修改配置信息**/
    @Override
    public int updLoanBonusconf(LoanBonusConf conf) {
        String updBy = getUserName();
        conf.setUpdateBy(updBy);
        int num = loanCompanyDao.updLoanConf(conf);
        return num;
    }

    /**新增贷款机构，默认新增奖励配置*/
    @Override
    public int saveLoanBonusconf(LoanBonusConf conf) {
        String updBy = getUserName();
        conf.setUpdateBy(updBy);
        int num = loanCompanyDao.saveLoanConf(conf);
        return num;
    }

    public int isExistLoanCompany(long sourceId, Long orgId){
        int cout = loanCompanyDao.isExist(sourceId, orgId);
        return cout;
    }

    @Override
    public List<OrgProfitConf> getOrgProfitConfList(OrgProfitConf conf,
                                                    Page<OrgProfitConf> page) {

        return orgProfitConfDao.getByPager(conf, page);
    }

    @Override
    public int updOrgProfitConf(OrgProfitConf conf) {
        return (int)orgProfitConfDao.updOrgProfitConf(conf);
    }

    @Override
    public int saveOrgProfitConf(List<OrgProfitConf> confs) {

        String userName = getUserName();
        for (OrgProfitConf conf : confs) {
            conf.setUpdateBy(userName);
            orgProfitConfDao.saveOrgProfitConf(conf);
        }
        return 0;
    }

    @Override
    public List<Notice> findNotice(Notice searchCondi, Page<Notice> pager) {
        noticeDao.find(searchCondi, pager);
        List<Notice> list = pager.getResult();
        Map<Long, String> orgMap = getOrgNameMap();
        if(list != null && list.size() > 0){
            String orgId = "";
            for(Notice notice: list){
                orgId = notice.getOrgId();
                if(StringUtils.isBlank(orgId)){
                    continue;
                }
                if("-1".equals(orgId)){
                    notice.setOrgId("全部");
                } else {
                    String[] orgIdArr = orgId.split(",");
                    StringBuilder orgName = new StringBuilder();
                    for(String item: orgIdArr){
                        orgName.append(orgMap.get(Long.valueOf(item)));
                        orgName.append(",");
                    }
                    notice.setOrgId(orgName.substring(0, orgName.length() - 1));
                }
            }
        }
        return list;
    }

    /**
     *
     * @param notice
     * @return
     */
    @Override
    public long updNotice(Notice notice) {
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        //如果是立即发布
        if(notice.getSubmitType() != null && notice.getSubmitType() == 1){
            notice.setStatus("1");//已发布
            if(StringUtils.isNotBlank(notice.getSendTimeStr())){
                notice.setSendTime(DateUtil.parseLongDateTime(notice.getSendTimeStr()));
            } else {
                notice.setSendTime(nowDate);
            }
            notice.setSendById(loginInfo.getId());
            notice.setSendByName(loginInfo.getUsername());
        }else{
            if(StringUtils.isNotBlank(notice.getSendTimeStr())){
                notice.setSendTime(DateUtil.parseLongDateTime(notice.getSendTimeStr()));
            }
            notice.setStatus("0");//待发布
        }
        notice.setUpdateBy(loginInfo.getUsername());
        notice.setUpdateDate(nowDate);
        return  noticeDao.upd(notice);
    }

    @Override
    public long addNotice(Notice notice) {
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        //如果是立即发布
        if((notice.getSubmitType() != null && notice.getSubmitType() == 1)
                || notice.getSendTime() != null){
            notice.setStatus("1");//已发布
            if(StringUtils.isNotBlank(notice.getSendTimeStr())){
                notice.setSendTime(DateUtil.parseLongDateTime(notice.getSendTimeStr()));
            } else {
                notice.setSendTime(nowDate);
            }
            notice.setSendById(loginInfo.getId());
            notice.setSendByName(loginInfo.getUsername());
        }else{
            if(StringUtils.isNotBlank(notice.getSendTimeStr())){
                notice.setSendTime(DateUtil.parseLongDateTime(notice.getSendTimeStr()));
            }
            notice.setStatus("0");//待发布
        }
        notice.setNoticeType("1");//系统公告
        notice.setPopSwitch(0);//弹窗提示开关,默认0关闭
        notice.setCreateBy(loginInfo.getUsername());
        notice.setCreateDate(nowDate);
        long num = noticeDao.add(notice);
        return num;
    }

    @Override
    public Notice noticeDetail(String id) {
        Notice notice = noticeDao.detail(id);
        if(notice != null){
            notice.setNewsImageUrl(CommonUtil.getImgUrlAgent(notice.getNewsImage()));
            if(notice.getSendTime() != null){
                notice.setSendTimeStr(DateUtil.getLongFormatDate(notice.getSendTime()));
            }
        }
        return notice;
    }

    @Override
    public int sendNotice(Notice notice) {
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        if("1".equals(notice.getStatus())){
            notice.setSendTime(new Date());
            notice.setSendById(loginInfo.getId());
            notice.setSendByName(loginInfo.getUsername());
        }
        return noticeDao.sendNotice(notice);
    }

    /**banner管理*/
    @Override
    public List<Ad> findAds(Ad searchCondi, Page<Ad> pager) {
        adDao.find(searchCondi, pager);
        List<Ad> list = pager.getResult();
        if(list != null && list.size() > 0){
            for (Ad ad: list){
                if(ad.getOrgId() == -1L){
                    ad.setOrgName("所有组织");
                }
            }
        }
        return list;
    }

    /**更新信息或状态开关，1-更新，2-开关*/
    @Override
    public long updAd(Ad ad,String updType) {
        long num =0;
        if("1".equals(updType)){
            num= adDao.upd(ad);
        }else if("2".equals(updType)){
            num=adDao.on_off(ad);
        }

        return num;
    }

    @Override
    public long addAd(Ad ad) {

        long oldId = adDao.getSysId();
        long newId = oldId + 1;
        int cout = adDao.updSysId(newId, oldId);

        if(cout==0){
            return 0;
        }

        ad.setId(newId);

        return adDao.add(ad);
    }

    @Override
    public Ad adDetail(Ad ad) {
        ad = adDao.detail(ad);
        if(ad != null){
            ad.setImgUrlStr(CommonUtil.getImgUrlAgent(ad.getImgUrl()));
            if(ad.getOrgId() == -1L){
                ad.setOrgName("所有组织");
            }
        }
        return ad;
    }

    @Override
    public long delAd(String id) {

        return adDao.del(id);
    }

    /**
     * 分润入账
     * @param batchNo:批次号
     */
    @Override
    public void profitAccount(String batchNo) {
        List<OrderMain> list = orderMainDao.selectOrder(batchNo);
        if(list == null || list.size() <1) {
            return;
        }
        String accountStatus = "2";
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        String userId = userInfo.getId().toString();
        Date date = new Date();
        for(OrderMain order: list){
            try {
                String resultStr = ClientInterface.profitAccount(order);
                if(StringUtils.isNotBlank(resultStr)) {
                    JSONObject json = JSONObject.parseObject(resultStr);
                    if (json.getBooleanValue("status")) {
                        accountStatus = "1";
                        fixedThreadPool.execute(new SuperBankPushRunnable(order.getOrderNo()));
                    }
                }
            } catch (Exception e){
                log.error("超级银行家贷款导入入账异常", e);
            }
            order.setAccountStatus(accountStatus);
            order.setUpdateBy(accountStatus);
            order.setUpdateDate(date);
            orderMainDao.updateAccountStatus(order);//更新订单入账状态
            userProfitDao.updateAccountStatus(order);//更新分润明细入账状态
            //更新用户|组织的收益总额
            updateAccountInfo(order);
        }
    }

    /**
     * 对订单里面生成的分润，在汇总表汇总
     * @param order
     */
    private void updateAccountInfo(OrderMain order) {
        String accountType = "0";
        //组织分润不需要累计
//        if(order.getOrgId() != null){
//            updateAccountInfo(order.getOrgId().toString(), order.getOrgProfit(), accountType);
//        }
//        accountType = "0";
        if(order.getOneUserCode() != null){
            updateAccountInfo(order.getOneUserCode(), order.getOneUserProfit(), accountType);
        }
        if(order.getTwoUserCode() != null){
            updateAccountInfo(order.getTwoUserCode(), order.getTwoUserProfit(), accountType);
        }
        if(order.getThrUserCode() != null){
            updateAccountInfo(order.getThrUserCode(), order.getThrUserProfit(), accountType);
        }
        if(order.getFouUserCode() != null){
            updateAccountInfo(order.getFouUserCode(), order.getFouUserProfit(), accountType);
        }
    }

    /**
     * 更新用户|组织的收益总额
     * @param userCode
     * @param profit
     * @param accountType
     */
    private void updateAccountInfo(String userCode, BigDecimal profit, String accountType) {
        if(StringUtils.isBlank(userCode) || profit == null
                || profit.compareTo(BigDecimal.ZERO) <= 0){
            return;
        }
        superBankUserInfoDao.updateAccountInfo(userCode, profit, accountType);
    }

    /**
     * 超级银行家用户开户
     * 未开户、待审核或者已开通
     * 代理业务状态;0未开通；1已开通；2待审核
     */
    @Override
    public void createMerAccount() {
//        String statusAgent = "1";
        List<SuperBankUserInfo> list = superBankUserInfoDao.selectNotAccountList();
//        List<SuperBankUserInfo> openedList = superBankUserInfoDao.selectByStatusAgent(statusAgent);
//        statusAgent = "2";
//        List<SuperBankUserInfo> waitList = superBankUserInfoDao.selectByStatusAgent(statusAgent);
//        if(openedList != null && openedList.size() > 0){
//            list.addAll(openedList);
//        }
//        if(waitList != null && waitList.size() > 0){
//            list.addAll(waitList);
//        }
        if(list == null || list.size() < 1){
            return;
        }
        //科目号、用户类型、商户号 | 代理商编号
        String subjectNo = Constants.SUPER_BANK_SUBJECT_NO;
        String userType = "M";
        String userId = "";
        for(SuperBankUserInfo userInfo: list){
            if(userInfo == null || StringUtils.isBlank(userInfo.getUserCode())){
                continue;
            }
            userId = userInfo.getUserCode();
            String returnStr = ClientInterface.createAccount(subjectNo, userType, userId);
            if(StringUtils.isNotBlank(returnStr)){
                JSONObject json = JSONObject.parseObject(returnStr);
                boolean status = json.getBoolean("status");
                if(status){
                    userInfo.setAccountStatus("1");
                    superBankUserInfoDao.updateAccount(userInfo);
                }
            }
        }
    }

    /**
     * 校验用户的商户号是否为空
     * @param userInfo
     * @return
     */
    private boolean checkUserAccount(SuperBankUserInfo userInfo) {
        if(userInfo == null|| StringUtils.isBlank(userInfo.getReceiveUserNo())){
            return true;
        }
        return false;
    }

    @Override
    public int isExistOrgConf(String orgId) {

        return orgProfitConfDao.isExist(orgId);
    }

    @Override
    public int isExistBankConf(long sourceId) {
        return creditCardDao.isExist(sourceId);
    }

    /**
     * 根据批次号更新分润计算状态
     * @param batchNo
     * @param profitStatus
     * @return
     */
    @Override
    public int updateProfitStatus(String batchNo, String profitStatus) {
        return orderMainDao.updateProfitStatus(batchNo, profitStatus);
    }

    @Override
    public int isExistBankConfWithSiAndOrgId(long sourceId,Long orgId) {
        return creditCardDao.isExistBankConfWithSiAndOrgId(sourceId,orgId);
    }
    /**
     * 获取出款预警的基本信息
     * @return
     */
    @Override
    public Map<String, Object> getOutWarn() {
        Map<String, Object> warnMap = new HashMap<>();
        SysDict warnPhone = sysDictService.getByKey("SUPER_BANK_WARN_PHONE");
        SysDict warnAmount = sysDictService.getByKey("SUPER_BANK_WARN_AMOUNT");
        if(warnPhone != null){
            warnMap.put("warnPhone", warnPhone.getSysValue());
        } else {
            warnMap.put("warnPhone", null);
        }
        if(warnAmount != null){
            warnMap.put("warnAmount", warnAmount.getSysValue());
        } else {
            warnMap.put("warnAmount", null);
        }
        return warnMap;
    }

    /**
     * 修改超级银行家出款预警的基本信息
     * @param param
     * @return
     */
    @Override
    public Result updateOutWarn(Map<String, Object> param) {
        Result result = new Result();
        String warnPhone = (String)param.get("warnPhone");//预警手机号
        String warnAmount = (String)param.get("warnAmount");//预警金额
        SysDict warnPhoneDict = new SysDict();
        warnPhoneDict.setSysKey("SUPER_BANK_WARN_PHONE");
        warnPhoneDict.setSysValue(warnPhone);
        SysDict warnAmountDict = new SysDict();
        warnAmountDict.setSysKey("SUPER_BANK_WARN_AMOUNT");
        warnAmountDict.setSysValue(warnAmount);
        sysDictService.updateSysValue(warnPhoneDict);
        sysDictService.updateSysValue(warnAmountDict);
        result.setStatus(true);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 获取超级银行家账户余额
     * @return
     */
    @Override
    public String getOutWarnAccount() {
        String warnAccount = "";
        SysDict sysDcit = sysDictService.getByKey("SUPER_BANK_ACCOUNT_BALANCE");
        if(sysDcit != null){
            warnAccount = sysDcit.getSysValue();
            warnAccount = new BigDecimal(warnAccount).setScale(2, BigDecimal.ROUND_DOWN).toString();
        }
        return warnAccount;
    }

    /**
     * 获取充值记录
     * @param baseInfo
     * @param page
     * @return
     */
    @Override
    public List<RechargeRecord> getRechargeList(RechargeRecord baseInfo, Page<RechargeRecord> page) {
        List<RechargeRecord> list = rechargeRecordDao.getRechargeList(baseInfo, page);
        return list;
    }

    /**
     * 超级银行家账户充值
     * 1.给账户加钱
     * 2.插入充值记录
     * @param amount
     * @return
     */
    @Override
    public int updateRecharge(String amount) {
        //1.插入充值记录
        RechargeRecord record = new RechargeRecord();
        BigDecimal accountBalance = BigDecimal.ZERO;//账户余额
        BigDecimal rechargeAmount = new BigDecimal(amount);//充值金额
        String accountBalanceStr = getOutWarnAccount();
        if(StringUtils.isNotBlank(accountBalanceStr)){
            accountBalance = new BigDecimal(accountBalanceStr);
        }
        record.setAmountBalance(accountBalance);
        record.setRechargeAmount(rechargeAmount);
        UserLoginInfo userInfo =  CommonUtil.getLoginUser();
        record.setUserId(userInfo.getId());
        record.setUserName(userInfo.getUsername());
        record.setCreateDate(new Date());
        rechargeRecordDao.insert(record);

        //2.给账户加钱
        accountBalance = accountBalance.add(rechargeAmount);
        SysDict sysDict = new SysDict();
        sysDict.setSysKey("SUPER_BANK_ACCOUNT_BALANCE");
        sysDict.setSysValue(accountBalance.toString());
        int num = sysDictService.updateSysValue(sysDict);
        return num;
    }

    /**
     * 修改组织开户状态
     * @param agentNo
     * @return
     */
    @Override
    public int updateOrgAccount(String agentNo) {
        return orgInfoDao.updateAccount(agentNo);
    }

    /**
     * 超级银行家组织开户
     * @param agentNo
     * @return
     */
    @Override
    public Result openAccount(String agentNo) {
        Result result = new Result();
        if(StringUtils.isBlank(agentNo)){
            result.setMsg("参数不能为空");
            return result;
        }
        String returnMsg = ClientInterface.createAccountByAcc(agentNo, Constants.SUPER_BANK_AGENT_SUBJECT_NO);
        if(StringUtils.isBlank(returnMsg)){
            result.setMsg("开户失败，请稍后再试");
            return result;
        }
        JSONObject json = JSONObject.parseObject(returnMsg);
        if(json.getBoolean("status")){
            updateOrgAccount(agentNo);
            result.setStatus(true);
            result.setMsg("开户成功");
        }
        return result;
    }

    /**
     * 信用卡办理记录导入
     * @param file
     * @param bankSourceId
     * @return
     */
    @Override
    public Result importCreditRecord(MultipartFile file, String bankSourceId) {
        Result result = new Result();
        if(StringUtils.isBlank(bankSourceId)){
            result.setMsg("指定银行不能为空");
            return result;
        }
        creditRowImpl.setBankSourceId(bankSourceId);
        //校验excel file的大小和格式
        if (!checkExcelFile(file, result)){
            return result;
        }
        try {
            List<ExcelErrorMsgBean> errors = new ArrayList<>();
            List<CreditcardApplyRecord> list = ExcelUtils.parseWorkbook(file.getInputStream(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    errors, creditRowImpl );
            int successSum = insertBatchCreditRecord(list);
            int failSum = errors.size();
            String msg = "";
            String errMsg = "";
            if(successSum > 0){
                msg = "导入成功";
            } else {
                msg = "导入失败";
            }
            if(failSum > 0){
                log.info("导入失败，原因:{}", JSONObject.toJSONString(errors));
                errMsg = "，第" + errors.get(0).getRow() + "行，" +
                        "第" + errors.get(0).getLine() + "列：" +errors.get(0).getMessage();
            }
            result.setStatus(true);
            result.setMsg(msg + "，成功条数：" + successSum + "，失败条数：" + failSum + errMsg);
        } catch (Exception e) {
            log.error("信用卡办理记录导入异常", e);
        }
        return result;
    }

    /**
     *
     * @param info
     * @return
     */
    @Override
    public Map<String, Object> getUserTotal(SuperBankUserInfo info) {
        Map<String, Object> map = superBankUserInfoDao.getUserTotal(info);
        if(map != null){
            BigDecimal commSum = (BigDecimal)map.get("commSum");//专员数量
            BigDecimal managerSum = (BigDecimal)map.get("managerSum");//经理数量
            BigDecimal bankerSum = (BigDecimal)map.get("bankerSum");//银行家数量
            BigDecimal agentSum = commSum.add(managerSum).add(bankerSum);//代理数量
            map.put("agentSum", agentSum);
        } else {
            map = new HashMap<>();
            map.put("userSum", 0);
            map.put("commSum", 0);
            map.put("managerSum", 0);
            map.put("bankerSum", 0);
            map.put("agentSum", 0);
        }
        return map;
    }

    @Override
    public int deleteNotice(Long id) {
        return noticeDao.deleteNotice(id);
    }

    @Override
    public int updateNoticePop(Long id, Integer popSwitch) {
        return noticeDao.updateNoticePop(id, popSwitch);
    }

    @Override
    public int updateUserStatus(SuperBankUserInfo userInfo) {
        return superBankUserInfoDao.updateUserStatus(userInfo);
    }

    @Override
    public Result checkLoanData(LoanBonusConf loan) {
        Result result = new Result();
        String companyBonus = loan.getCompanyBonus();
        String orgBonus = loan.getOrgBonus();
        if(StringUtils.isBlank(companyBonus)){
            result.setMsg("公司截留奖金不能为空");
            return result;
        }
        if(StringUtils.isBlank(orgBonus)){
            result.setMsg("组织截留奖金不能为空");
            return result;
        }
        if(loan.getSourceId() == null){
            result.setMsg("贷款机构不能为空");
            return result;
        }
        LoanSource loanSource = loanSourceDao.selectDetail(loan.getSourceId());
        if(loanSource == null){
            result.setMsg("找不到对应的贷款机构");
            return result;
        }
        //根据贷款总奖金的格式，判断现在填写的，格式是否一致，再判断大小
        String profitType = loanSource.getProfitType();
        loan.setProfitType(profitType);
        if(StringUtils.isBlank(profitType)) {
            result.setMsg("贷款机构的分润方式不能为空");
            return result;
        }
        if(!checkLoanBonusType(profitType, companyBonus)){
            result.setMsg("公司截留奖金格式不正确");
            return result;
        }
        if(!checkLoanBonusType(profitType, orgBonus)){
            result.setMsg("品牌截留奖金格式不正确");
            return result;
        }
        //贷款总奖金 >= 公司截留 + 品牌截留
        BigDecimal loanBonus = loanSource.getLoanBonus();
        if(!compareLoanBonus(profitType, loanBonus, companyBonus, orgBonus) ){
            result.setMsg("公司截留奖金+品牌截留奖金不能大于贷款总奖金");
            return result;
        }
        result.setStatus(true);
        result.setMsg("校验通过");
        return result;
    }

    /**
     * 贷款总奖金 >= 公司截留 + 品牌截留
     * @param type
     * @param loanBonus
     * @param companyBonusStr
     * @param orgBonusStr
     * @return
     */
    private boolean compareLoanBonus(String type, BigDecimal loanBonus, String companyBonusStr, String orgBonusStr) {
        BigDecimal companyBonus = null;
        BigDecimal orgBonus = null;
        companyBonus = new BigDecimal(companyBonusStr);
        orgBonus = new BigDecimal(orgBonusStr);
        if(loanBonus.compareTo(companyBonus.add(orgBonus)) >= 0){
            return true;
        } else {
            return false;
        }
    }



    @Override
    public List<LotteryOrder> qryLotteryOrder(LotteryOrder info, Page<LotteryOrder> page) {

        lotteryOrderDao.selectLotteryOrder(info, page);

        List<LotteryOrder> list = page.getResult();
        for (LotteryOrder order : list) {
            formatData(order);
        }
        page.setResult(list);
        return list;
    }

    @Override
    public LotteryOrder qrySumOrder(LotteryOrder info) {
        return lotteryOrderDao.selectOrderSum(info);
    }

    /**
     * 公司截留奖金和品牌截留奖金的格式要和贷款总奖金的一致
     * @param type
     * @param bonus
     * @return
     */
    public boolean checkLoanBonusType(String type, String bonus){
        Pattern pattern = null;
        Matcher matcherCompany = null;
        pattern = Pattern.compile("^[0-9]+(\\.[0-9]{0,2})?$");
        matcherCompany = pattern.matcher(bonus);
        if(matcherCompany.find()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 批量插入信用卡导入记录
     * @param list
     * @return
     */
    private int insertBatchCreditRecord(List<CreditcardApplyRecord> list) {
        int num = 0;
        if(list == null || list.size() < 1){
            return num;
        }
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        Date date = new Date();
        List<CreditcardApplyRecord> itemList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            list.get(i).setCreateTime(date);
            list.get(i).setOperator(loginUser.getId());
            itemList.add(list.get(i));
            if((i+1) % 300 == 0){
                //插入数据
                num += creditcardApplyRecordDao.insertBatch(itemList);
                //清空itemList
                itemList.clear();
            }
        }
        //插入最后不足300的数据
        if(itemList.size() > 0){
            num += creditcardApplyRecordDao.insertBatch(itemList);
        }
        return num;
    }

    public Map<String, Object> getBankMap(){
        Map<String, Object> bankMap = new HashMap<>();
        List<CreditcardSource> list = creditCardDao.allBanksList();
        if(list != null && list.size() > 0){
            for(CreditcardSource item: list){
                bankMap.put(item.getId().toString(), item);
            }
        }
        return bankMap;
    }

    @Override
    public LoanBonusConf selectLoanBonusConfByPrimaryKey(Long id) {
        return loanCompanyDao.selectByPrimaryKey(id);
    }

    @Override
    public OrgProfitConf selectOrgProfitConfByPrimaryKey(Long id) {
        return orgProfitConfDao.selectOrgProfit(id);
    }

    @Override
    public Ad selectAdByPrimaryKey(Long id) {
        Ad ad=new Ad();
        ad.setId(id);
        return adDao.detail(ad);
    }

    public void exportLotteryOrder(HttpServletResponse response, LotteryOrder baseInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<LotteryOrder> page = new Page<>(0, Integer.MAX_VALUE);
            List<LotteryOrder> list = lotteryOrderDao.selectLotteryOrder(baseInfo, page);

            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);

            String fileName = "彩票代购订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(LotteryOrder item: list){
                map = new HashMap<>();
                formatData(item);
                map.put("orderNo",item.getOrderNo());
                map.put("deviceJno",item.getDeviceJno());
                map.put("orgId",item.getOrgId());
                map.put("orgName",item.getOrgName());
                map.put("orderStatus",item.getOrderStatus());
                map.put("userCode",item.getUserCode());
                map.put("userName",item.getUserName());
                map.put("phone",item.getPhone());
                map.put("price",item.getPrice());
                map.put("profitType",item.getProfitType());
                map.put("awardRequire",item.getAwardRequire());
                map.put("loanBankRate",item.getLoanBankRate());
                map.put("orgProfit",item.getOrgProfit());
                map.put("loanOrgRate",item.getLoanOrgRate());
                map.put("loanOrgBonus",item.getLoanOrgBonus());
                map.put("betTime",item.getBetTime());
                map.put("lotteryType",item.getLotteryType());
                map.put("issue",item.getIssue());
                map.put("redeemTime",item.getRedeemTime());
                map.put("awardAmount",item.getAwardAmount());
                map.put("isBigPrize",item.getIsBigPrize());
                map.put("redeemFlag",item.getRedeemFlag());
                map.put("oneCode",item.getOneCode());
                map.put("oneName",item.getOneName());
                map.put("oneRole",item.getOneRole());
                map.put("oneProfit",item.getOneProfit());
                map.put("twoCode", item.getTwoCode());
                map.put("twoName",item.getTwoName());
                map.put("twoRole",item.getTwoRole());
                map.put("twoProfit",item.getTwoProfit());
                map.put("threeCode",item.getThreeCode());
                map.put("threeName",item.getThreeName());
                map.put("threeRole",item.getThreeRole());
                map.put("threeProfit",item.getThreeProfit());
                map.put("fourCode",item.getFourCode());
                map.put("fourName",item.getFourName());
                map.put("fourRole",item.getFourRole());
                map.put("fourProfit",item.getFourProfit());
                map.put("orgName",item.getOrgName());
                map.put("orgProfit",item.getOrgProfit());
                map.put("plateProfit",item.getPlateProfit());
                map.put("accountStatus",item.getAccountStatus());
                map.put("openProvince",item.getOpenProvince());
                map.put("openCity",item.getOpenCity());
                map.put("openRegion",item.getOpenRegion());
                map.put("remark",item.getRemark());
                map.put("loanAmount",item.getLoanAmount());
                map.put("totalBonus", item.getTotalBonus());
                map.put("createDate", item.getCreateDate());
                data.add(map);
            }
            //addOrderSumMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","deviceJno","orgId","orgName","orderStatus",
                    "userCode","userName","phone",
                    "loanAmount","profitType","awardRequire","loanBankRate","price",
                    "loanOrgRate","totalBonus","betTime","lotteryType","issue",
                    "redeemTime","awardAmount",
                    "isBigPrize",
                    "redeemFlag","oneCode","oneName","oneRole",
                    "oneProfit","twoCode", "twoName","twoRole",
                    "twoProfit","threeCode","threeName","threeRole",
                    "threeProfit","fourCode","fourName","fourRole","fourProfit",
                    "orgName","orgProfit","plateProfit","accountStatus",
                    "openProvince","openCity","openRegion","createDate","remark"};
            String[] colsName = new String[]{
                    "订单ID","投注设备流水号","所属组织","组织名称","订单状态","贡献人ID",
                    "贡献人名称","贡献人手机号","付款金额","奖金方式","奖励要求","彩票机构总奖金扣率",
                    "彩票机构总奖金","品牌发放总资金扣率","品牌发放总奖金","投注时间","彩种","投注期号",
                    "兑奖时间","中奖总金额","大奖标志","兑奖状态","一级编号","一级名称","一级身份",
                    "一级分润","二级编号","二级名称","二级身份","二级分润","三级编号","三级名称",
                    "三级身份","三级分润","四级编号","四级名称","四级身份","四级分润","品牌商名称",
                    "品牌商分润","平台分润","记账状态","省","市","区","创建时间","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出彩票代购订单异常", e);
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

    public void formatData(LotteryOrder order){
        if("0".equals(order.getAccountStatus())){//0待入账；1已记账；2记账失败
            order.setAccountStatus("待入账");
        }else if("1".equals(order.getAccountStatus())){
            order.setAccountStatus("已记账");
        }else if("2".equals(order.getAccountStatus())){
            order.setAccountStatus("记账失败");
        }

        //1已创建；2待支付；3:待审核 4已授权 5订单成功 6订单失败 7已办理过  9已关闭
        if("1".equals(order.getOrderStatus())){
            order.setOrderStatus("已创建");
        }else if("5".equals(order.getOrderStatus())){
            order.setOrderStatus("已完成");
        }else if("6".equals(order.getOrderStatus())){
            order.setOrderStatus("订单失败");
        }

        //1-固定奖金，2-按比例发放
        if("1".equals(order.getProfitType())){
            order.setProfitType("固定奖金");
        }else if("2".equals(order.getProfitType())){
            order.setProfitType("按比例发放");
        }

        String normal = "普通用户";
        String twoZero 	  = "专员";
        String threeZero  = "经理";
        String fourZero = "银行家";

        if("10".equals(order.getOneRole())){
            order.setOneRole(normal);
        }else if("20".equals(order.getOneRole())){
            order.setOneRole(twoZero);
        }else if("30".equals(order.getOneRole())){
            order.setOneRole(threeZero);
        }else if("40".equals(order.getOneRole())){
            order.setOneRole(fourZero);
        }

        if("10".equals(order.getTwoRole())){
            order.setTwoRole(normal);
        }else if("20".equals(order.getTwoRole())){
            order.setTwoRole(twoZero);
        }else if("30".equals(order.getTwoRole())){
            order.setTwoRole(threeZero);
        }else if("40".equals(order.getTwoRole())){
            order.setTwoRole(fourZero);
        }

        if("10".equals(order.getThreeRole())){
            order.setThreeRole(normal);
        }else if("20".equals(order.getThreeRole())){
            order.setThreeRole(twoZero);
        }else if("30".equals(order.getThreeRole())){
            order.setThreeRole(threeZero);
        }else if("40".equals(order.getThreeRole())){
            order.setThreeRole(fourZero);
        }

        if("10".equals(order.getFourRole())){
            order.setFourRole(normal);
        }else if("20".equals(order.getFourRole())){
            order.setFourRole(twoZero);
        }else if("30".equals(order.getFourRole())){
            order.setFourRole(threeZero);
        }else if("40".equals(order.getFourRole())){
            order.setFourRole(fourZero);
        }
    }

    public List<OpenFunctionConf> getOpenFunctionConfList() {
        return openFunctionConfDao.getList();
    }


    @Override
    public List<RankingRecord> qryRankingRecord(RankingRecord info,
                                                Page<RankingRecord> page) {
        List<RankingRecord> list = rankingRecordDao.selectRankingRecord(info, page);
        for (RankingRecord base : list) {
            if("0".equals(base.getStatus())){
                base.setStatus("未生成");
            }else if("1".equals(base.getStatus())){
                base.setStatus("未发放");
            }else if("2".equals(base.getStatus())){
                base.setStatus("已发放");
            }else if("3".equals(base.getStatus())){
                base.setStatus("发放完成");
            }

            if("0".equals(base.getRankingType())){
                base.setRankingType("周榜");
            }else if("1".equals(base.getRankingType())){
                base.setRankingType("月榜");
            }else if("2".equals(base.getRankingType())){
                base.setRankingType("年榜");
            }

            if("0".equals(base.getDataType())){
                base.setDataType("收益金额");
            }else if("1".equals(base.getDataType())){
                base.setDataType("会员数");
            }else if("2".equals(base.getDataType())){
                base.setDataType("用户数");
            }else if("3".equals(base.getDataType())){
                base.setDataType("订单数");
            }
        }
        page.setResult(list);
        return list;
    }

    @Override
    public RankingRecord getRankingRecordTotalInfo(RankingRecord info) {
        return rankingRecordDao.getTotalInfo(info);
    }

    @Override
    public List<RankingRule> getRankingRuleList(RankingRule baseInfo,
                                                Page<RankingRule> page) {

        return rankingRuleDao.getRankingRuleList(baseInfo, page);
    }

    @Override
    public int updateBankingRuleStatus(RankingRule info) {
        if(info != null){
            int openTime = info.getOpenTime();
            if("1".equals(info.getStatus())){
                info.setOpenTime(openTime + 1);
            }else{
                info.setOpenTime(openTime);
            }
        }
        return rankingRuleDao.updateRankingRuleStatus(info);
    }

    @Override
    public RankingRecordDetail qryRankingDetail(String recordId,
                                                Page<RankingRecordDetail> page,boolean isSort) {

        List<RankingRecordDetail> list = rankingRecordDetailDao.selectRankingRecordDetail(recordId, page);

        /**排名*/
/*
        int index = 0;
        int flagIndex = 0;
        boolean flag = false;

        List<RankingRecordDetail> notRemove = new ArrayList<RankingRecordDetail>();
        List<RankingRecordDetail> hadRemove = new ArrayList<RankingRecordDetail>();

        for(int i=0;i < list.size();i++){
            RankingRecordDetail rd = list.get(i);

            if("2".equals(rd.getStatus())){//已移除
                hadRemove.add(rd);
            }else{
                notRemove.add(rd);
            }

            rd.setRankingIndex(index+1+"");   //排名
            if("2".equals(rd.getStatus()) && !flag){//已移除
                flagIndex = index;
                flag = true;
            }

            if(!"2".equals(rd.getStatus()) && flag){//非移除
                index = flagIndex;
                flag = false;
                rd.setRankingIndex(index+1+"");   //重新设置排名
            }

            index++;
        }
        if(notRemove.size()>0){
            Collections.sort(notRemove,notRemove.get(0));//未移除的重新排序
        }

        //已移除的  再对比成为专员时间 重新排名   在已有rankingIndex上排名
        if(hadRemove.size()>1){
            for (int i = 0; i < hadRemove.size(); i++) {
                for (int k = 0; k < hadRemove.size()-i; k++) {
                    if(k < hadRemove.size()-i - 1){
                        RankingRecordDetail first = hadRemove.get(k);
                        RankingRecordDetail second = hadRemove.get(k+1);
                        if(new BigDecimal(second.getUserTotalAmount()).compareTo(new BigDecimal(first.getUserTotalAmount()))==1){//后一个比前一个大
                            String temp = hadRemove.get(k).getRankingIndex();
                            hadRemove.get(k).setRankingIndex(hadRemove.get(k+1).getRankingIndex());
                            hadRemove.get(k+1).setRankingIndex(temp);
                        }else if(new BigDecimal(second.getUserTotalAmount()).compareTo(new BigDecimal(first.getUserTotalAmount()))==0){
                            if(second.getToagentDate()!=null&&first.getToagentDate()!=null){
                                if(second.getToagentDate().before(first.getToagentDate())){//后一个比前一个早
                                    String temp = hadRemove.get(k).getRankingIndex();
                                    hadRemove.get(k).setRankingIndex(hadRemove.get(k+1).getRankingIndex());
                                    hadRemove.get(k+1).setRankingIndex(temp);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (RankingRecordDetail detailInfo : notRemove) {
            rankingRecordDetailDao.updSort(detailInfo);
        }
        for(RankingRecordDetail detailInfo : hadRemove) {
            rankingRecordDetailDao.updSort(detailInfo);
        }

        list = rankingRecordDetailDao.selectRankingRecordDetail(recordId, page);
*/
        /**排名----end----*/

        for (RankingRecordDetail rd : list) {
            //0未发放 1已发放 2已移除
            if("0".equals(rd.getStatus())){
                rd.setStatus("未发放");
            }else if("1".equals(rd.getStatus())){
                rd.setStatus("已发放");
            }else if("2".equals(rd.getStatus())){
                rd.setStatus("已移除");
            }

            //0未获奖 1获奖
            if("0".equals(rd.getIsRank())){
                rd.setIsRank("未获奖");
            }else if("1".equals(rd.getIsRank())){
                rd.setIsRank("获奖");
            }

            if(rd.getNickName()!=null){
                try {
                    rd.setNickName(URLDecoder.decode(rd.getNickName(),"utf-8"));
                } catch (Exception e) {
                    //
                }
            }
        }

        page.setResult(list);
        RankingRecordDetail rrd = new RankingRecordDetail();
        if(list.size()>0){
            rrd = list.get(0);
            //0未生成 1未发放  2已经发放  3发放完成
            if("0".equals(rrd.getRankingStatus())){
                rrd.setRankingStatus("未生成");
            }else if("1".equals(rrd.getRankingStatus())){
                rrd.setRankingStatus("未发放");
            }else if("2".equals(rrd.getRankingStatus())){
                rrd.setRankingStatus("已发放");
            }else if("3".equals(rrd.getRankingStatus())){
                rrd.setRankingStatus("发放完成");
            }

            //0周榜 1月榜 2年榜
            if("0".equals(rrd.getRankingType())){
                rrd.setRankingType("周榜");
            }else if("1".equals(rrd.getRankingType())){
                rrd.setRankingType("月榜");
            }else if("2".equals(rrd.getRankingType())){
                rrd.setRankingType("年榜");
            }
        }
        return rrd;
    }

    public RankingRule getRankingRuleById(Long id) {
        RankingRule rankingRule = rankingRuleDao.selectRankingRuleByRuleId(id);
        if(rankingRule != null){
            String openOrg = rankingRule.getOpenOrg();
            List<OrgInfo> allOrgList = orgInfoDao.getOrgInfoList(); //所有组织
            if(openOrg != null){
                if(!"-1".equals(openOrg)){
                    List<OrgInfo> selectOrgList = new ArrayList<OrgInfo>();//选择有的组织
                    List<OrgInfo> orgList = new ArrayList<OrgInfo>();//未选择的组织
                    String[] orgs = openOrg.split(",");
                    List<String> selectOrgs = Arrays.asList(orgs);
                    for(int i = 0;i < allOrgList.size();i++){
                        boolean isContains = selectOrgs.contains(String.valueOf(allOrgList.get(i).getOrgId()));
                        if(isContains){
                            selectOrgList.add(allOrgList.get(i));
                        }else{
                            orgList.add(allOrgList.get(i));
                        }
                    }
                    rankingRule.setOrgList(orgList);
                    rankingRule.setSelectOrgs(selectOrgList);
                }else{
                    rankingRule.setOrgList(null);
                    rankingRule.setSelectOrgs(allOrgList);
                }

            }
        }
        return rankingRule;
    }

    @Override
    public List<RankingRuleLevel> getRankingRuleLevelListByRuleId(Long id) {
        return rankingRuleLevelDao.selectRankingRuleLevelByRuleId(id);
    }

    @Override
    public Result insertRankingRule(RankingRule rankingRule) {
        Result result = checkParamsOfRankingRule(rankingRule);
        if(result != null){
            return result;
        }
        if("1".equals(rankingRule.getStatus())){ //假如是开启状态则open_time开启数为1
            rankingRule.setOpenTime(1);
        }
        int count = rankingRuleDao.insertRankingRule(rankingRule);
        if(count == 0){
            Result rs = new Result();
            rs.setStatus(false);
            rs.setMsg("保存失败");
            return rs;
        }
        return null;
    }

    @Override
    public Result updateRankingRule(RankingRule rankingRule) {
        Result result = checkParamsOfRankingRule(rankingRule);
        if(result != null){
            return result;
        }
        int count = rankingRuleDao.updateRankingRule(rankingRule);
        Result rs = new Result();
        if(count == 0){
            rs.setStatus(false);
            rs.setMsg("保存失败");
        }
        rs.setStatus(true);
        rs.setMsg("保存成功");
        return rs;
    }

    private Result checkParamsOfRankingRule(RankingRule rankingRule){
        Result result = new Result();
        result.setStatus(false);
        if(StringUtils.isBlank(rankingRule.getRuleName())){
            result.setMsg("排行榜名称不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getDataType())){
            result.setMsg("统计数据类型不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getRuleType())){
            result.setMsg("统计周期不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getLevelTotalMoney())){
            result.setMsg("各级奖金设置不能为空！");
            return result;
        }
        if(rankingRule.getLevelList() == null || rankingRule.getLevelList().size() == 0){
            result.setMsg("每个组织中各级奖金设置不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getOrgType())){
            result.setMsg("参入活动组织不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getOpenOrg())){
            result.setMsg("参入活动组织不能为空！");
            return result;
        }
        if(rankingRule.getStartTime() == null){
            result.setMsg("活动开始时间不能为空！");
            return result;
        }
        if(rankingRule.getEndTime() == null){
            result.setMsg("活动结束不能为空！");
            return result;
        }
		/*if(StringUtils.isBlank(rankingRule.getTotalAmount())){
			result.setMsg("活动总奖金不能为空！");
			return result;
		}*/
        if(StringUtils.isBlank(rankingRule.getIntroduction())){
            result.setMsg("排行榜活动介绍不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getBonusNote())){
            result.setMsg("规则及奖金说明不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getShowOrderNo())){
            result.setMsg("显示顺序不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getAdvertUrl())){
            result.setMsg("广告位图片不能为空！");
            return result;
        }
        if(StringUtils.isBlank(rankingRule.getStatus())){
            result.setMsg("开启活动状态不能为空！");
            return result;
        }
        //校验顺序是否重复
        List<RankingRule> rankingList = rankingRuleDao.selectRankingRuleByShowOrderNo(rankingRule.getShowOrderNo());
        boolean isExist = false;
        if("1".equals(rankingRule.getOperate())){
            for(RankingRule rank :rankingList){
                if(rank.getId().equals(rankingRule.getId())){
                    continue;
                }else{
                    isExist = true;
                }
            }
            if(isExist){
                result.setMsg("'显示顺序'"+rankingRule.getShowOrderNo()+"已存在，请重新输入！");
                return result;
            }
        }else{
            if(rankingList != null && rankingList.size() > 0){
                result.setMsg("'显示顺序'"+rankingRule.getShowOrderNo()+"已存在，请重新输入！");
                return result;
            }

        }

        return null;
    }

    @Override
    public RankingRule getRankingRuleByCode(String code) {
        return rankingRuleDao.selectRankingRuleByRuleCode(code);
    }

    @Override
    public int insertRankingRuleLevelBatch(List<RankingRuleLevel> list,
                                           Long ruleId) {
        return rankingRuleLevelDao.insertBatch(list, ruleId);
    }

    @Override
    public int deleteRankingRuleLevelByRuleId(Long id) {
        return rankingRuleLevelDao.deleteRankingRuleLevelByRuleId(id);
    }

    private final int NEED_ORDERS = 100;  //订单排行榜 一等奖需要完成订单数
    @Override
    public int updRankingDetail(RankingRecordDetail baseInfo){
        rankingRecordDetailDao.updRankingDetail(baseInfo);//先更新单条记录
        //再重新分配奖金

        RankingRecordDetail one = rankingRecordDetailDao.selectRankingById(baseInfo.getId());

        RankingRecord rr = new RankingRecord();
        rr.setId(one.getRecordId());
        Page<RankingRecord> rrPage = new Page<RankingRecord>(1,1);
        List<RankingRecord> rrList = rankingRecordDao.selectRankingRecord(rr,rrPage);
        RankingRule rule = rankingRuleDao.selectRankingRuleByRuleCode(rrList.get(0).getRuleNo());

        List<RankingRuleLevel>  levels = rankingRuleLevelDao.selectRankingRuleLevelByRuleId(rule.getId());
        Page<RankingRecordDetail> rrdPage = new Page<RankingRecordDetail>(1,Integer.MAX_VALUE);
        List<RankingRecordDetail> rrdList = rankingRecordDetailDao.selectRankingRecordDetail(one.getRecordId(),rrdPage);//一条排行榜的明细  (包括移除的)

        List<RankingRecordDetail> notRemove = new ArrayList<RankingRecordDetail>();

        for (RankingRecordDetail detail : rrdList) {
            if("0".equals(detail.getStatus())){
                notRemove.add(detail);
            }
        }



        int index = -1;   //修改到的数据的索引



        int giveCount = 0 ;   //获奖人数 ，重新分配后   重新统计人数  如果全部人都获奖然后移除或取消人数会变
        if(notRemove.size() > 0){
        	
        	
        	//-----------------------------订单排行榜-----------------
        	if("3".equals(rule.getDataType())){
        		if(Double.parseDouble(notRemove.get(0).getUserTotalAmount()) < NEED_ORDERS){  //不满足规定订单数 则不发一等奖
        			RankingRuleLevel no1 = levels.get(0);//一等奖
        			int count = no1.getPrizePeopleCount();//人数
        			RankingRuleLevel theLast = levels.get(levels.size()-1);
        			theLast.setPrizePeopleCount(theLast.getPrizePeopleCount() + count);
        			levels.remove(0);
        		}
        	}
        	//-----------------------------订单排行榜-----------------
        	
        	
            for (int i=0;i<levels.size();i++) {
                RankingRuleLevel level = levels.get(i);
                String levFix = level.getLevel();//中文 等级
                int personCount = level.getPrizePeopleCount();//该等级的获奖人数
                if(personCount==0){
                    continue;
                }
                int startIndex = index + 1;
                int endIndex = index + personCount;
                index = index + personCount;
                List<RankingRecordDetail> subList = null;
                if(endIndex+1 <= notRemove.size()){
                    subList = notRemove.subList(startIndex, endIndex+1);   //根据等级人数截取排好序的list，然后分别设置奖励
                }else{
                    subList = notRemove.subList(startIndex, notRemove.size());
                }

                if(subList.size()==0){
                    break ;
                }

                List<Long> ids = new ArrayList<Long>();
                for(RankingRecordDetail d : subList){
                    ids.add(Long.parseLong(d.getId()));
                }

                giveCount += ids.size();

                Map<String,Object> params = new HashMap<String,Object>();
                params.put("isRank", 1);
                params.put("levelName", levFix+"等奖");
                params.put("rankingAmount", level.getSinglePrize());
                params.put("ids", ids);

                int count = rankingRecordDetailDao.updateBatch(params);
                log.info(levFix+"等奖"+"   "+ level.getSinglePrize()+"   原   {}  条" + subList.size() +"  更新成功  {} 条  " + count);
            }
        }

        rr.setPushNum(String.valueOf(giveCount));
        rankingRecordDao.updRankingRecordPushNum(rr);


        //兼容移除，取消移除
        if(index < notRemove.size()-1){ //把下一条记录设置成未中奖的(因为后台每次只能操作一条,暂时不考虑并发)   没操作到最后一条的情况下
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("isRank", "0");
            params.put("levelName", "");
            params.put("rankingAmount", "0.00");
            List<Long> ids = new ArrayList<Long>();
            ids.add(Long.parseLong(notRemove.get(index+1).getId()));
            params.put("ids", ids);
            int count = rankingRecordDetailDao.updateBatch(params);
        }

        return 1;
    }

    @Override
    public List<RankingRule> selectRankingRuleByOrgId(String orgId) {
        return rankingRuleDao.selectRankingRuleByOrgId(orgId);
    }

    @Override
    public List<RankingRecordDetail> getPushRankingDetail(RankingRecordDetail baseInfo){
        return rankingRecordDetailDao.selectPushRecord(baseInfo);
    }

    @Override
    public Result pushRankingBonus(RankingRecord data,List<RankingRecordDetail> detailList,
                                   JSONObject accoutInfo){

        Result result = new Result();

        synchronized (SuperBankServiceImpl.class) {
            int count = detailList.size();//实发人数
            BigDecimal realAmount = new BigDecimal("0.00");//实发金额
            for (RankingRecordDetail base : detailList) {
                String amount = base.getRankingAmount()==null?"0.00":base.getRankingAmount();//获奖奖金
                realAmount = realAmount.add(new BigDecimal(amount));
            }
            Collections.sort(detailList, detailList.get(0));//倒序排序
            if(realAmount.compareTo(accoutInfo.getBigDecimal("avaliBalance"))==1){  //实发 大于 可用金额
                result.setMsg("要发放的奖金大于组织账户余额！");
                result.setStatus(false);
                return result;
            }else{
                List<RankingPushRecordInfo> pushList = new ArrayList<RankingPushRecordInfo>();
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

                for (int i=0;i < detailList.size();i++) {
                    RankingRecordDetail base = detailList.get(i);
                    RankingPushRecordInfo pushInfo = new RankingPushRecordInfo();
                    pushInfo.setAccountStatus(0);
                    pushInfo.setBatchNo(data.getBatchNo());
                    pushInfo.setNickName(base.getNickName());
                    String rand = "";
                    for(int k=0;k<4;k++){
                        double temp = Math.random();
                        int code = (int)(temp*9);
                        rand += code;
                    }

                    String orderNo = "SYJ" + sdf.format(new Date()) + rand + (i+1);
                    pushInfo.setOrderNo(orderNo);
                    pushInfo.setOrgId(data.getOrgId());

                    SuperBankUserInfo sbui = superBankUserInfoDao.selectDetail(base.getUserCode());
                    if(sbui==null){
                        sbui = new SuperBankUserInfo();
                        sbui.setPhone("");
                    }
                    pushInfo.setPhone(sbui.getPhone()==null?"":sbui.getPhone());

                    pushInfo.setPushStatus(0);//0未发放 1已经发放
                    pushInfo.setPushTime(now);
                    pushInfo.setRankingData(base.getUserTotalAmount());//统计总额
                    pushInfo.setRankingLevel(base.getRankingLevel());
                    pushInfo.setRankingMoney(new BigDecimal(base.getRankingAmount()));
                    pushInfo.setRankingName(data.getRankingName());
                    pushInfo.setRankingNo(data.getRankingNo());
                    pushInfo.setStartTime(data.getStartDate());
                    pushInfo.setEndTime(data.getEndDate());
                    String dataType = "";
                    if("收益金额".equals(data.getDataType())){
                        dataType = "0";
                    }else if("会员数".equals(data.getDataType())){
                        dataType = "1";
                    }else if("用户数".equals(data.getDataType())){
                        dataType = "2";
                    }else if("订单数".equals(data.getDataType())){
                    	dataType = "3";
                    }
                    pushInfo.setDataType(dataType);
                    String rtype = data.getRankingType();
                    pushInfo.setRankingType(0);
                    if("周榜".equals(rtype)){
                        pushInfo.setRankingType(0);
                    }else if("月榜".equals(rtype)){
                        pushInfo.setRankingType(1);
                    }else if("年榜榜".equals(rtype)){
                        pushInfo.setRankingType(2);
                    }
                    pushInfo.setRemoveRemark(null);
                    pushInfo.setRemoveTime(null);
                    pushInfo.setRuleNo(data.getRuleNo());
                    pushInfo.setShowNo(base.getRankingIndex()==null?"":base.getRankingIndex());
                    pushInfo.setUserCode(base.getUserCode());
                    pushInfo.setUserName(base.getUserName());

                    pushList.add(pushInfo);
                }

                int succCount = rankingPushRecordDao.insertRankingPushRecords(pushList);
                if(succCount!=count){
                    int delCount = rankingPushRecordDao.delRankingPushRecords(data.getRankingNo());
                    log.info("------删除成功条数------" + delCount);
                    result.setMsg("发放失败 ！有记录存在格式问题");
                    result.setStatus(false);
                    return result;
                }
                data.setPushRealNum(count+"");
                data.setPushRealAmount(realAmount.toString());
                data.setStatus("2");
                int updrr = rankingRecordDao.updRankingRecord(data);//更新排行榜
                log.info("-------更新排行榜------" + updrr);
                for (RankingRecordDetail detail : detailList) {
                    int updrrd = rankingRecordDetailDao.updPushedRecord(detail);//更新排行榜明细
                    log.info("----------更新排行榜明细---------" + updrrd);
                }

                result.setMsg("发放成功！");
                result.setStatus(true);
            }
        }

        return result;
    }

    public void invokeInterface(RankingRecord data){

        String recordId = data.getId();
        Map<String,String> params = new HashMap<String,String>();
        params.put("recordId", recordId);
        String sign = Md5.md5Str(recordId + "&" + Constants.SUPER_BANK_SECRET);
        params.put("sign", sign);
        SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
        String profitUrl= sysDict.getSysValue();
        if(sysDict == null || StringUtils.isBlank(profitUrl)){
            throw new BossBaseException("超级银行家业务产品分润计算的数据字典未配置");
        }

        String httpResult = ClientInterface.httpPost2(profitUrl+Constants.SUPER_BANK_PROFIT_RANKING,params);//计算分润

        log.info("计算分润返回-----"+httpResult);

        String freshURL = "/superbank-mobile/refresh/refreshRankingPushRecord";

        String md5Str = data.getOrgId()+"&" + Constants.SUPER_BANK_SECRET;

        Map<String,String> freshMap = new HashMap<String,String>();
        freshMap.put("orgId", data.getOrgId());
        freshMap.put("ruleNo", data.getRuleNo());
        freshMap.put("sign", Md5.md5Str(md5Str));

        String refResult = ClientInterface.httpPost2(profitUrl+freshURL,freshMap);//刷新缓存
        log.info("刷新缓存结果-----"+refResult);
    }


    @Override
    public List<RankingRuleHistory> selectRankingRuleHistory(Long ruleId) {
        return rankingRuleHistoryDao.selectRankingRuleHistory(ruleId);
    }

    @Override
    public int insertRankingRuleHistory(RankingRuleHistory hisRecord) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = "";
        if (principal != null){
            userName = principal.getUsername();
        }
        hisRecord.setUpdateBy(userName);
        return rankingRuleHistoryDao.insertRankingRuleHistory(hisRecord);
    }

    @Override
    public CarOrder getCarOrders(CarOrder order, Page<CarOrder> page) {
        List<CarOrder> carOrderByPage = carOrderDao.findCarOrderByPage(order, page);
        for(CarOrder carOrder:carOrderByPage){
            if(1==carOrder.getRateType()){
                carOrder.setRate(carOrder.getRate()+"元");
            } if(2==carOrder.getRateType()){
                carOrder.setRate(carOrder.getRate()+"%");
            }
            if(carOrder.getReceiveTime()!=null){
                carOrder.setReceiveTimeStr(DateUtils.format(carOrder.getReceiveTime(), " yyyy-MM-dd HH:mm:ss "));
            }
        }
        page.setResult(carOrderByPage);
        return carOrderDao.orderSum(order);
    }

    @Override
    public int addCarOrderProfitConf(CarOrderProfitConf conf) {
        String userName = getUserName();
        conf.setCreateBy(userName);
        conf.setUpdateBy(userName);
        return carOrderProfitConfDao.insert(conf);
    }

    @Override
    public CarOrderProfitConf findCarOrderProfitConf() {
        return carOrderProfitConfDao.select();
    }

    @Override
    public int updCarOrderProfitConf(CarOrderProfitConf conf) {
        String userName = getUserName();
        conf.setUpdateBy(userName);
        return carOrderProfitConfDao.update(conf);
    }

    @Override
    public Result carOrderDetail(String orderNo) {
        Result result = new Result();
        CarOrder carOrder = carOrderDao.carOrderDetail(orderNo);
        if(carOrder == null){
            result.setStatus(false);
            result.setMsg("查询失败");
            return result;
        }
        if(carOrder.getReceiveTime()!=null){
            carOrder.setReceiveTimeStr(DateUtils.format(carOrder.getReceiveTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        if(carOrder.getRateType()==1){
            carOrder.setRate(carOrder.getRate()+"元");
        }
        if(carOrder.getRateType()==2){
            carOrder.setRate(carOrder.getRate()+"%");
        }
        result.setData(carOrder);
        result.setMsg("查询成功");
        result.setStatus(true);
        return result;
    }

    @Override
    public void exportCarOrder(HttpServletResponse response, CarOrder order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream outputStream = null;
        try {
            Page<CarOrder> page = new Page<CarOrder>(0, Integer.MAX_VALUE);
            List<CarOrder> carList = carOrderDao.findCarOrderByPage(order, page);

            ListDataExcelExport export = new ListDataExcelExport(2);
            String fileName = "违章代缴订单" + sdf.format(new Date()) + export.getFileSuffix(2);

            String fileNameFormat = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            Map<String, String> map = null;

            String wzType = "";
            String oneUserType = "";
            String twoUserType = "";
            String thrUserType = "";
            String fourUserType = "";

            for (CarOrder carOrder : carList) {
                map = new HashMap<>();

                map.put("orderNo", carOrder.getOrderNo());
                if("1".equals(carOrder.getViolationType())){
                    wzType = "扣分单";
                }else{
                    wzType = "非扣分单";
                }
                map.put("violationType", wzType);
                map.put("outOrderNo", carOrder.getOutOrderNo());
                map.put("score", carOrder.getScore());
                map.put("violationTime", carOrder.getViolationTime()==null?"":sdf2.format(carOrder.getViolationTime()));
                map.put("violationCity", carOrder.getViolationCity());
                map.put("carNum", carOrder.getCarNum());
                map.put("receiveAmount", carOrder.getReceiveAmount());
                map.put("price", carOrder.getPrice());
                map.put("totalBonus", carOrder.getTotalBonus());
                map.put("orgName", carOrder.getOrgName());
                map.put("status", "订单成功");
                map.put("userCode", carOrder.getUserCode());
                map.put("userName", carOrder.getUserName());
                map.put("phone", carOrder.getPhone());
                map.put("createDate", carOrder.getCreateDate()==null?"":sdf2.format(carOrder.getCreateDate()));
                map.put("completeDate", carOrder.getCompleteDate()==null?"":sdf2.format(carOrder.getCompleteDate()));
                map.put("oneUserCode", carOrder.getOneUserCode());
                map.put("oneUserName", carOrder.getOneUserName());
                if("20".equals(carOrder.getOneUserType())){
                    oneUserType = "专员";
                }else if("30".equals(carOrder.getOneUserType())){
                    oneUserType = "经理";
                }else if("40".equals(carOrder.getOneUserType())){
                    oneUserType = "银行家";
                }
                map.put("oneUserType", oneUserType);
                map.put("oneUserProfit", carOrder.getOneUserProfit());
                map.put("twoUserCode", carOrder.getTwoUserCode());
                map.put("twoUserName", carOrder.getTwoUserName());
                if("20".equals(carOrder.getTwoUserType())){
                    twoUserType = "专员";
                }else if("30".equals(carOrder.getTwoUserType())){
                    twoUserType = "经理";
                }else if("40".equals(carOrder.getTwoUserType())){
                    twoUserType = "银行家";
                }
                map.put("twoUserType", twoUserType);
                map.put("twoUserProfit", carOrder.getTwoUserProfit());
                map.put("thrUserCode", carOrder.getThrUserCode());
                map.put("thrUserName", carOrder.getThrUserName());
                if("20".equals(carOrder.getThrUserType())){
                    thrUserType = "专员";
                }else if("30".equals(carOrder.getThrUserType())){
                    thrUserType = "经理";
                }else if("40".equals(carOrder.getThrUserType())){
                    thrUserType = "银行家";
                }
                map.put("thrUserType", thrUserType);
                map.put("thrUserProfit", carOrder.getThrUserProfit());
                map.put("fouUserCode", carOrder.getFouUserCode());
                map.put("fouUserName", carOrder.getFouUserName());
                if("20".equals(carOrder.getFouUserType())){
                    fourUserType = "专员";
                }else if("30".equals(carOrder.getFouUserType())){
                    fourUserType = "经理";
                }else if("40".equals(carOrder.getFouUserType())){
                    fourUserType = "银行家";
                }
                map.put("fouUserType", fourUserType);
                map.put("fouUserProfit", carOrder.getFouUserProfit());
                map.put("orgProfit", carOrder.getOrgProfit());
                map.put("plateProfit", carOrder.getPlateProfit());
                map.put("realPlatProfit", carOrder.getRealPlatProfit()==null?"":carOrder.getRealPlatProfit().toString());
                map.put("adjustRatio",carOrder.getAdjustRatio()==null?"":carOrder.getAdjustRatio().toString());
                map.put("basicBonusAmount",carOrder.getBasicBonusAmount()==null?"":carOrder.getBasicBonusAmount().toString());
                map.put("bonusAmount",carOrder.getBonusAmount()==null?"":carOrder.getBonusAmount().toString());
                map.put("redUserCode",carOrder.getRedUserCode()==null?"":carOrder.getRedUserCode().toString());
                map.put("redUserName",carOrder.getRedUserName()==null?"":carOrder.getRedUserName().toString());
                if("0".equals(carOrder.getAccountStatus())){
                    map.put("accountStatus", "未记账");
                }else if("1".equals(carOrder.getAccountStatus())){
                    map.put("accountStatus", "记账成功");
                }else{
                    map.put("accountStatus", "记账失败");
                }

                map.put("openProvince", carOrder.getOpenProvince());
                map.put("openCity", carOrder.getOpenCity());
                map.put("openRegion", carOrder.getOpenRegion());
                map.put("remark", carOrder.getRemark());

                data.add(map);
            }

            String[] cols = new String[]{"orderNo","outOrderNo","violationType","score","violationTime","violationCity","carNum","receiveAmount","price","totalBonus","orgName","status","userCode","userName","phone","createDate","completeDate","oneUserCode","oneUserName","oneUserType","oneUserProfit","twoUserCode","twoUserName","twoUserType","twoUserProfit","thrUserCode","thrUserName","thrUserType","thrUserProfit","fouUserCode","fouUserName","fouUserType","fouUserProfit","orgProfit","plateProfit","realPlatProfit","adjustRatio","basicBonusAmount","bonusAmount","redUserCode","redUserName","accountStatus","openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{"订单ID","外部订单号","违章类型","扣分","违章时间","违章城市","车牌号","订单总额","银行家项目总分润","发放总奖金","所属组织","订单状态","贡献人ID","贡献人名称","贡献人手机号","创建时间","订单完成时间","一级编号","一级名称","一级身份","一级分润","二级编号","二级名称","二级身份","二级分润","三级编号","三级名称","三级身份","三级分润","四级编号","四级名称","四级身份","四级分润","品牌商分润","平台分润","平台实际分润","调节系数","领地基准分红","领地分红","领地分红领取用户编号","领地分红用户姓名","记账状态","省","市","区","备注"};
            outputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出违章代缴订单记录异常", e);
        } finally {
            try {
                if(outputStream!=null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void analysisDataTask() {

        /**
         * loginSource:设备访问来源 1.安卓；2.苹果,
         * businessSource:业务来源:1.办信用卡、2.贷款申请,
         * openType:页面类型1.APP,2.H5,
         * viewType ：1或空.Page view;2.Unique Visitor
         * viewNum：页面1.每日,7.周,30.月的访问量
         * loginType:登录类型：0.访问；1.登录'
         **/
        //获取开启了数据分析定时任务的组织
        List<OrgInfo> orgInfoList = dataAnalysisRecordDao.getAnalysisOrgInfoList();
        if(orgInfoList == null || orgInfoList.size() == 0){
            log.info("~~~~~~~~组织为空，不需要进行数据分析~~~~~~~~~");
            return;
        }
        for(OrgInfo orgInfo : orgInfoList){
            DataAnalysisRecord dataAnalysisRecord = new DataAnalysisRecord();
            String orgId = String.valueOf(orgInfo.getOrgId());
            log.info("********************当前解析的组织为，OrgId: " + orgId + ";组织名称: " + orgInfo.getOrgName() + "Start ******************");
            dataAnalysisRecord.setOrgId(String.valueOf(orgInfo.getOrgId()));
            //生成数据分析编号，格式：ANALYSISyyyyMMdd_orgId
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String analysisCode = "ANALYSIS" + sf.format(new Date()) + "_"+ orgId;
            log.info("此次数据分析编号：" + analysisCode);
            dataAnalysisRecord.setAnalysisCode(analysisCode);
            //数据分析开始时间
            Date lineDate = new Date();
            dataAnalysisRecord.setCreateDate(lineDate);
            log.info("数据分析开始时间：" + sff.format(lineDate));

            //启动频率：每天APP和公众号登录次数
            //公众号登陆次数
            Long h5LoginNum = pageViewRecordDao.getPageViewOrUniqueVisitorBySource(null, null, "2", "1", "1", "1", orgId, null, null);
            log.info("公众号登陆次数: " + h5LoginNum);

            //公众号android登陆次数
            Long dayH5AndroidLoginNum = pageViewRecordDao.getPageViewOrUniqueVisitorBySource("1", null, "2", "1", "1", "1", orgId, null, null);
            dataAnalysisRecord.setDayH5AndroidLoginNum(String.valueOf(dayH5AndroidLoginNum));
            log.info("公众号android登陆次数: " + dayH5AndroidLoginNum);
            //公众号ios登陆次数
            Long dayH5IosLoginNum = pageViewRecordDao.getPageViewOrUniqueVisitorBySource("2", null, "2", "1", "1", "1", orgId, null, null);
            dataAnalysisRecord.setDayH5IosLoginNum(String.valueOf(dayH5IosLoginNum));
            log.info("公众号ios登陆次数: " + dayH5IosLoginNum);

            //android登陆次数
            Long androidLoginNum = pageViewRecordDao.getPageViewOrUniqueVisitorBySource("1", null, "1", "1", "1", "1", orgId, null, null);
            log.info("android登陆次数: " + androidLoginNum);

            //ios登陆次数
            Long iosLoginNum = pageViewRecordDao.getPageViewOrUniqueVisitorBySource("2", null, "1", "1", "1", "1", orgId, null, null);
            log.info("ios登陆次数: " + iosLoginNum);

            Long totalLoginNum = dayH5AndroidLoginNum + dayH5IosLoginNum + androidLoginNum + iosLoginNum;
            log.info("总登陆次数: " + totalLoginNum);

            dataAnalysisRecord.setDayH5LoginNum(String.valueOf(h5LoginNum));
            dataAnalysisRecord.setDayAndroidLoginNum(String.valueOf(androidLoginNum));
            dataAnalysisRecord.setDayIosLoginNum(String.valueOf(iosLoginNum));
            dataAnalysisRecord.setDayLoginNum(String.valueOf(totalLoginNum));

            //统计男性用户总数
            Long maleCount = pageViewRecordDao.getUserInfoNumber("1",orgId);
            dataAnalysisRecord.setMaleNum(String.valueOf(maleCount));
            log.info("统计男性用户总数: " + maleCount);

            //女性用户总数
            Long femaleCount = pageViewRecordDao.getUserInfoNumber("2",orgId);
            dataAnalysisRecord.setFemaleNum(String.valueOf(femaleCount));
            log.info("女性用户总数: " + femaleCount);

            //已完善资料用户数
            Long perfectUserCount = maleCount + femaleCount;
            //Long perfectUserCount = pageViewRecordDao.getUserInfoNumber("3",orgId);
            log.info("已完善资料用户数: " + perfectUserCount);

            //总用户数
            Long allCount = pageViewRecordDao.getUserInfoNumber(null,orgId);
            BigDecimal allNum = new BigDecimal(allCount);
            dataAnalysisRecord.setAllNum(String.valueOf(allCount));
            log.info("总用户数: " + allCount);

            //完善资料用户
            Long registerUserCount = userDistributionRecordDao.getConsummateDataUserCount(orgId);
            log.info("完善资料用户: " + registerUserCount);

            //男性，女性占总用户数的百分比
            if(perfectUserCount == 0){
                dataAnalysisRecord.setMaleRate("0.00");
                dataAnalysisRecord.setFemaleRate("0.00");
            }else{
                BigDecimal maleNum = new BigDecimal(maleCount);
                BigDecimal femaleNum = new BigDecimal(femaleCount);
                BigDecimal perfectUserNum = new BigDecimal(perfectUserCount);
                BigDecimal maleRate = maleNum.divide(perfectUserNum,2, BigDecimal.ROUND_HALF_UP);
                BigDecimal femaleRate = femaleNum.divide(perfectUserNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setMaleRate(maleRate.toString());
                dataAnalysisRecord.setFemaleRate(femaleRate.toString());
            }
            log.info("男性占总用户数的百分比: " + dataAnalysisRecord.getMaleRate());
            log.info("女性占总用户数的百分比: " + dataAnalysisRecord.getFemaleRate());

            //统计用户分布的在某省 市 人数
            log.info("-------------------统计用户分布的在某'省'人数------------------------");
            List<UserDistributionRecord> provinceList = userDistributionRecordDao.getProvinceDistributionCount(orgId);
            List<UserDistributionRecord> cityList = userDistributionRecordDao.getCityDistributionCount(orgId);
            if(provinceList != null && provinceList.size() > 0){
                for(UserDistributionRecord udRecord : provinceList){
                    udRecord.setIsProvince("1");
                    udRecord.setAnalysisCode(analysisCode);
                    udRecord.setOrgId(orgId);
                    log.info(udRecord.getOpenProvince() + ":" + udRecord.getUserCount());
                    userDistributionRecordDao.saveUserDistributionRecord(udRecord);
                }
            }
            log.info("-------------------统计用户分布的在某 '市 '人数------------------------");
            if(cityList != null && cityList.size() > 0){
                for(UserDistributionRecord udRecord : cityList){
                    udRecord.setIsProvince("0");
                    udRecord.setOrgId(orgId);
                    udRecord.setAnalysisCode(analysisCode);
                    log.info(udRecord.getOpenProvince() + "-" + udRecord.getOpenCity() + ":" + udRecord.getUserCount());

                    userDistributionRecordDao.saveUserDistributionRecord(udRecord);
                }
            }
            log.info("-------------------------------------------------------------");
            //小白用户：没有创建过订单的用户数
            Long notOrderUserCount = userDistributionRecordDao.getOrderUserCount("1",orgId);
            dataAnalysisRecord.setWhiteUser(String.valueOf(notOrderUserCount));
            log.info("小白用户：" + notOrderUserCount);


            //注册转化率：注册用户数/总用户数*100%
            dataAnalysisRecord.setRegisterUser(String.valueOf(registerUserCount));
            if(allCount == 0){
                dataAnalysisRecord.setRegisterRate("0.00");
            }else{
                BigDecimal registerNum = new BigDecimal(registerUserCount);
                BigDecimal registerRate = registerNum.divide(allNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setRegisterRate(registerRate.toString());
            }
            log.info("注册转化率：" + dataAnalysisRecord.getRegisterRate());

            //激活用户：已创建订单用户
            Long orderUserCount = userDistributionRecordDao.getOrderUserCount("2",orgId);
            dataAnalysisRecord.setActivateUser(String.valueOf(orderUserCount));
            log.info("激活用户: " + orderUserCount);


            //产品使用成功率=已完成订单/已创建订单数（信用卡、贷款、所有 三个维度）
            //已创建的订单
            Long totalOrderCount = userDistributionRecordDao.getOrderByStatusAndOrderTypeCount(null,null,orgId);
            dataAnalysisRecord.setOrderNum(String.valueOf(totalOrderCount));
            log.info("已创建的订单量： " + totalOrderCount);
            //已完成订单
            Long successOrderCount = userDistributionRecordDao.getOrderByStatusAndOrderTypeCount("99",null,orgId);
            dataAnalysisRecord.setFinishOrderNum(String.valueOf(successOrderCount));
            if(totalOrderCount == 0){
                dataAnalysisRecord.setProductSuccessRate("0.00");
            }else{
                BigDecimal totalOrderNum = new BigDecimal(totalOrderCount);
                BigDecimal successOrderNum = new BigDecimal(successOrderCount);
                BigDecimal successRate = successOrderNum.divide(totalOrderNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setProductSuccessRate(successRate.toString());
            }
            log.info("已完成订单: " + successOrderCount);
            log.info("已完成订单率: " + dataAnalysisRecord.getProductSuccessRate());

            //推荐用户：已分享应用的用户  （已分享，分享成功用户数）
            Long sharedNum = userDistributionRecordDao.getSharedSuccessUserCount(orgId);
            dataAnalysisRecord.setSharedUser(String.valueOf(sharedNum));
            log.info("推荐用户: " + sharedNum);

            //推荐用户率= 推荐用户数/总用户数
            BigDecimal sharedCount = new BigDecimal(sharedNum);
            if(sharedCount == null || allCount == null || allCount == 0){
                dataAnalysisRecord.setSharedRate("0.00");
            }else{
                BigDecimal sharedRate = sharedCount.divide(allNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setSharedRate(String.valueOf(sharedRate));
            }
            log.info("推荐用户率 : " + dataAnalysisRecord.getSharedRate());
            //公众号用户数
            Long h5UserNum = userDistributionRecordDao.getUserCountBySource(orgId, "2", null);
            dataAnalysisRecord.setH5UserNum(String.valueOf(h5UserNum));
            log.info("公众号用户数 : " + h5UserNum);

            //android用户数
            Long androidUserNum = userDistributionRecordDao.getUserCountBySource(orgId, "1", "1");
            dataAnalysisRecord.setAndroidUserNum(String.valueOf(androidUserNum));
            log.info("android用户数 : " + androidUserNum);

            //ios用户数
            Long iosUserNum = userDistributionRecordDao.getUserCountBySource(orgId, "1", "2");
            dataAnalysisRecord.setIosUserNum(String.valueOf(iosUserNum));
            log.info("ios用户数 : " + iosUserNum);

            //公众号安卓用户数
            Long h5AndroidUserNum = userDistributionRecordDao.getUserCountBySource(orgId, "2", "1");
            dataAnalysisRecord.setH5AndroidUserNum(String.valueOf(h5AndroidUserNum));
            log.info("公众号安卓用户数 : " + h5AndroidUserNum);

            //公众号Ios用户数
            Long h5IosUserNum = userDistributionRecordDao.getUserCountBySource(orgId, "2", "2");
            dataAnalysisRecord.setH5IosUserNum(String.valueOf(h5IosUserNum));
            log.info("公众号Ios用户数 : " + h5IosUserNum);


            //付费用户：已付费交钱的用户
            Long paidUserCount = userDistributionRecordDao.getPaidUserCount("2",orgId);
            dataAnalysisRecord.setPaidUser(String.valueOf(paidUserCount));
            if(allCount == 0){
                dataAnalysisRecord.setPaidRate("0.00");
            }else{
                BigDecimal paidUserNum = new BigDecimal(paidUserCount);
                BigDecimal totalNum = new BigDecimal(allCount);
                BigDecimal paidUserRate = paidUserNum.divide(totalNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setPaidRate(paidUserRate.toString());
            }
            log.info("付费用户量： " + paidUserCount);
            log.info("付费用户率： " + dataAnalysisRecord.getPaidRate());

            //计算昨天次日留存率：（当天新增的用户中，在注册的第2天还登录的用户数）/第一天新增总用户数；
            //(1)前第2天新增总用户数
            Long morrowNewUserCount = userDistributionRecordDao.getNewIncreaseUserByDay(2,orgId);
            //(2) 次日新用户登录数
            Long morrowUserLoginCount = userDistributionRecordDao.getloginUserByDay(2,orgId);
            if(morrowNewUserCount == 0){
                dataAnalysisRecord.setMorrowRetentionRate("0.00");
            }else{
                BigDecimal newUserNum = new BigDecimal(morrowNewUserCount);
                BigDecimal userLoginNum = new BigDecimal(morrowUserLoginCount);
                BigDecimal retentionRate = userLoginNum.divide(newUserNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setMorrowRetentionRate(retentionRate.toString());
            }
            log.info("次日留存率计算：");
            log.info("     首日新增量：" + morrowNewUserCount);
            log.info("     次日登录数：" + morrowUserLoginCount);
            log.info("     次日留存率：" + dataAnalysisRecord.getMorrowRetentionRate());

            //计算3日留存率：（当天新增的用户中，在注册的第2天还登录的用户数）/第一天新增总用户数；
            //(1)前第3天新增总用户数
            Long threeNewUserCount = userDistributionRecordDao.getNewIncreaseUserByDay(3,orgId);
            //(2) 在注册的第3天还有登录的用户数
            Long threeUserLoginCount = userDistributionRecordDao.getloginUserByDay(3,orgId);
            if(threeNewUserCount == 0 || threeUserLoginCount == 0){
                dataAnalysisRecord.setThreeRetentionRate("0.00");
            }else{
                BigDecimal newUserNum = new BigDecimal(threeNewUserCount);
                BigDecimal userLoginNum = new BigDecimal(threeUserLoginCount);
                BigDecimal retentionRate = userLoginNum.divide(newUserNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setThreeRetentionRate(retentionRate.toString());
            }
            log.info("3日留存率计算：");
            log.info("     首日新增量：" + threeNewUserCount);
            log.info("     3日登录数：" + threeUserLoginCount);
            log.info("     3日留存率：" + dataAnalysisRecord.getThreeRetentionRate());

            //计算7留存率：（当天新增的用户中，在注册的第2天还登录的用户数）/第一天新增总用户数；
            //(1)前第7天新增总用户数
            Long sevenNewUserCount = userDistributionRecordDao.getNewIncreaseUserByDay(7,orgId);
            //(2) 在注册的第7天还有登录的用户数
            Long sevenUserLoginCount = userDistributionRecordDao.getloginUserByDay(7,orgId);
            if(sevenNewUserCount == 0 || sevenUserLoginCount == 0){
                dataAnalysisRecord.setSevenRetentionRate("0.00");
            }else{
                BigDecimal newUserNum = new BigDecimal(sevenNewUserCount);
                BigDecimal userLoginNum = new BigDecimal(sevenUserLoginCount);
                BigDecimal retentionRate = userLoginNum.divide(newUserNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setSevenRetentionRate(retentionRate.toString());
            }
            log.info("7日留存率计算：");
            log.info("     首日新增量：" + sevenNewUserCount);
            log.info("     7日登录数：" + sevenUserLoginCount);
            log.info("     7日留存率：" + dataAnalysisRecord.getSevenRetentionRate());

            //计算15留存率：（当天新增的用户中，在注册的第2天还登录的用户数）/第一天新增总用户数；
            //(1)前第15天新增总用户数
            Long fifteenNewUserCount = userDistributionRecordDao.getNewIncreaseUserByDay(15,orgId);
            //(2) 在注册的第15天还有登录的用户数
            Long fifteenUserLoginCount = userDistributionRecordDao.getloginUserByDay(15,orgId);
            if(fifteenNewUserCount == 0 || fifteenUserLoginCount == 0){
                dataAnalysisRecord.setFifteenRetentionRate("0.00");
            }else{
                BigDecimal newUserNum = new BigDecimal(fifteenNewUserCount);
                BigDecimal userLoginNum = new BigDecimal(fifteenUserLoginCount);
                BigDecimal retentionRate = userLoginNum.divide(newUserNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setFifteenRetentionRate(retentionRate.toString());
            }
            log.info("15日留存率计算：");
            log.info("     首日新增量：" + fifteenNewUserCount);
            log.info("     15日登录数：" + fifteenUserLoginCount);
            log.info("     15日留存率：" + dataAnalysisRecord.getFifteenRetentionRate());

            //计算30留存率：（当天新增的用户中，在注册的第2天还登录的用户数）/第一天新增总用户数；
            //(1)前第30天新增总用户数
            Long thityNewUserCount = userDistributionRecordDao.getNewIncreaseUserByDay(30,orgId);
            //(2) 在注册的第30天还有登录的用户数
            Long thityUserLoginCount = userDistributionRecordDao.getloginUserByDay(30,orgId);
            if(thityNewUserCount == 0 || thityUserLoginCount == 0){
                dataAnalysisRecord.setThityRetentionRate("0.00");
            }else{
                BigDecimal newUserNum = new BigDecimal(thityNewUserCount);
                BigDecimal userLoginNum = new BigDecimal(thityUserLoginCount);
                BigDecimal retentionRate = userLoginNum.divide(newUserNum,2,BigDecimal.ROUND_HALF_UP);
                dataAnalysisRecord.setThityRetentionRate(retentionRate.toString());
            }
            log.info("30日留存率计算：");
            log.info("     首日新增量：" + fifteenNewUserCount);
            log.info("     30日登录数：" + fifteenUserLoginCount);
            log.info("     30日留存率：" + dataAnalysisRecord.getFifteenRetentionRate());

            //信用卡创建订单数
            Long creditOrderCount = userDistributionRecordDao.getOrderByStatusAndOrderTypeCount(null,"2",orgId);
            dataAnalysisRecord.setCreditOrderNum(String.valueOf(creditOrderCount));
            log.info("信用卡创建订单数: " + creditOrderCount);

            //信用卡已完成订单数
            Long successCreditOrderCount = userDistributionRecordDao.getOrderByStatusAndOrderTypeCount("5","2",orgId);
            dataAnalysisRecord.setCreditFinishOrder(String.valueOf(successCreditOrderCount));
            log.info("信用卡已完成订单数 : " + successCreditOrderCount);

            log.info("---------------------------------------------业务访问统计：------------------------------------------------------------");
            int businessCount = 13;  //11个业务：业务来源:1.办信用卡、2.贷款申请、3.商户收款、4.保险、5.智能信用查询、6.违章代缴、7.办卡查询、8.信用卡还款、9.抢红包、10.排行榜、11.领地、12.小鹿租机
            int creditCount = 14;    //13个信用卡银行：1.平安银行、2.交通银行、3.兴业银行、4.浦发银行、5.光大银行、6.广州银行、7.招商银行、8.广发银行、9.上海银行、10.温州银行、11.温州银行全卡种、12.华夏银行、13.建设银行（深圳）
            int loanCount = 16;      //15个贷款机构： 1.全民购、2.宜人贷、3.钱隆柜、4.你我贷借款、5.快易花、6.众安杏仁派、7.万达普惠、8.豆豆钱、9.360借条、10.卡卡贷、11.苏宁消费金融、12.小树时代、13.功夫贷、14.人品贷、15.微贷'


            // 业务访问量汇总
            for(int i = 1;i < businessCount;i++){
                log.info("个业务(11个)：业务来源:1.办信用卡、2.贷款申请、3.商户收款、4.保险、5.智能信用查询、6.违章代缴、7.办卡查询、8.信用卡还款、9.抢红包、10.排行榜、11.领地、12.小鹿租机");
                log.info("当前业务编号：" + i);
                for(int openType = 1;openType < 3 ;openType++){ //打开位置 1-APP 2-微信H5；
                    log.info("开始计算~~~");
                    log.info("打开位置 1-APP 2-微信H5 --- 当前设Open Type：" + openType);
                    for(int loginSource = 1;loginSource < 3; loginSource++){
                        log.info("设备访问来源 1.安卓；2.IOS --- " + loginSource);

                        BusinessPageViewData pageViewData = new BusinessPageViewData();
                        Long dayViewPage = pageViewRecordDao.getPageViewOrUniqueVisitorBySource(String.valueOf(loginSource), String.valueOf(i), String.valueOf(openType), "1", "1",null,orgId,null,null);
                        Long weekPvNum = pageViewRecordDao.selectBusinessPageViewData(String.valueOf(i), String.valueOf(openType), "pv", "week", orgId,String.valueOf(loginSource));
                        Long monthPvNum = pageViewRecordDao.selectBusinessPageViewData(String.valueOf(i), String.valueOf(openType), "pv", "month", orgId,String.valueOf(loginSource));

                        Long dayUniqueVisitor = pageViewRecordDao.getPageViewOrUniqueVisitorBySource(String.valueOf(loginSource), String.valueOf(i), String.valueOf(openType), "4", "1",null,orgId,null,null);
                        Long weekUvNum = pageViewRecordDao.selectBusinessPageViewData(String.valueOf(i), String.valueOf(openType), "uv", "week", orgId,String.valueOf(loginSource));
                        Long monthUvNum = pageViewRecordDao.selectBusinessPageViewData(String.valueOf(i), String.valueOf(openType), "uv", "month", orgId,String.valueOf(loginSource));


                        weekPvNum = (weekPvNum==null?0:weekPvNum) + (dayViewPage==null?0:dayViewPage);
                        monthPvNum = (monthPvNum==null?0:monthPvNum) + (dayViewPage==null?0:dayViewPage);

                        weekUvNum = (weekUvNum==null?0:weekUvNum) + (dayUniqueVisitor==null?0:dayUniqueVisitor);
                        monthUvNum = (monthUvNum==null?0:monthUvNum) + (dayUniqueVisitor==null?0:dayUniqueVisitor);

                        log.info("日VP量：" + dayViewPage + ";周PV量：" + weekPvNum + ";月PV量：" + monthPvNum);
                        log.info("日UV量：" + dayUniqueVisitor + ";周UV量：" + weekUvNum + ";月UV量：" + monthUvNum);
                        pageViewData.setOpenType(String.valueOf(openType));
                        pageViewData.setDayPvNum(String.valueOf(dayViewPage));
                        pageViewData.setDayUvNum(String.valueOf(dayUniqueVisitor));
                        pageViewData.setWeekPvNum(String.valueOf(weekPvNum));
                        pageViewData.setMonthPvNum(String.valueOf(monthPvNum));
                        pageViewData.setWeekUvNum(String.valueOf(weekUvNum));
                        pageViewData.setMonthUvNum(String.valueOf(monthUvNum));
                        pageViewData.setAnalysisCode(analysisCode);
                        pageViewData.setOrgId(orgId);
                        pageViewData.setLoginSource(String.valueOf(loginSource));
                        pageViewData.setBusinessSource(String.valueOf(i));
                        businessPageViewDataDao.saveBusinessPageViewData(pageViewData);
                    }
                }

            }
            log.info("---------------------------------------------信用卡访问量统计：------------------------------------------------------------");

            //保存信用卡申请入口pv量
            for(int i = 1;i < creditCount;i++){
                log.info("信用卡银行(13个)：个信用卡银行：1.平安银行、2.交通银行、3.兴业银行、4.浦发银行、5.光大银行、6.广州银行、7.招商银行、8.广发银行、9.上海银行、10.温州银行、11.温州银行全卡种、12.华夏银行、13.建设银行（深圳）");
                log.info("当前业务编号：" + i);
                for(int openType = 1;openType < 3 ;openType++){ //打开位置 1-APP 2-微信H5；
                    log.info("开始计算~~~");
                    log.info("打开位置 1-APP 2-微信H5 --- 当前设Open Type：" + openType);
                    for(int loginSource = 1;loginSource < 3; loginSource++){
                        log.info("设备访问来源 1.安卓；2.IOS --- " + loginSource);
                        CreditloanEnterData pageViewData = new CreditloanEnterData();
                        Long dayViewPage = pageViewRecordDao.getPageViewOrUniqueVisitorBySource(String.valueOf(loginSource), "1", String.valueOf(openType), "1", "1",null,orgId,String.valueOf(i),null);
                        //获取周pv量
                        Long creditWeekPvNum = pageViewRecordDao.selectCleditOrLoanPvOrUvNumber("1", String.valueOf(openType), "pv", "week", orgId,String.valueOf(i),null,String.valueOf(loginSource));
                        //获取月pv量
                        Long creditMonthPvNum = pageViewRecordDao.selectCleditOrLoanPvOrUvNumber("1", String.valueOf(openType), "pv", "month", orgId,String.valueOf(i),null,String.valueOf(loginSource));
                        //creditWeekPvNum = creditWeekPvNum + dayViewPage;
                        //creditMonthPvNum = creditMonthPvNum + dayViewPage;

                        creditWeekPvNum = (creditWeekPvNum==null?0:creditWeekPvNum) + (dayViewPage==null?0:dayViewPage);
                        creditMonthPvNum = (creditMonthPvNum==null?0:creditMonthPvNum) + (dayViewPage==null?0:dayViewPage);



                        Long dayUniqueVisitor = pageViewRecordDao.getPageViewOrUniqueVisitorBySource(String.valueOf(loginSource), "1", String.valueOf(openType), "4", "1",null,orgId,String.valueOf(i),null);
                        //获取周uv量
                        Long creditWeekUvNum = pageViewRecordDao.selectCleditOrLoanPvOrUvNumber("1", String.valueOf(openType), "uv", "week", orgId,String.valueOf(i),null,String.valueOf(loginSource));
                        //获取月uv量
                        Long creditMonthUvNum = pageViewRecordDao.selectCleditOrLoanPvOrUvNumber("1", String.valueOf(openType), "uv", "month", orgId,String.valueOf(i),null,String.valueOf(loginSource));

                        creditWeekUvNum = (creditWeekUvNum==null?0:creditWeekUvNum) + (dayUniqueVisitor==null?0:dayUniqueVisitor);
                        creditMonthUvNum = (creditMonthUvNum==null?0:creditMonthUvNum) + (dayUniqueVisitor==null?0:dayUniqueVisitor);

                        log.info("日VP量：" + dayViewPage + ";周PV量：" + creditWeekPvNum + ";月PV量：" + creditMonthPvNum);
                        log.info("日UV量：" + dayUniqueVisitor + ";周UV量：" + creditWeekUvNum + ";月UV量：" + creditMonthUvNum);

                        pageViewData.setOpenType(String.valueOf(openType));
                        pageViewData.setDayPvNum(String.valueOf(dayViewPage));
                        pageViewData.setDayUvNum(String.valueOf(dayUniqueVisitor));
                        pageViewData.setWeekPvNum(String.valueOf(creditWeekPvNum));
                        pageViewData.setMonthPvNum(String.valueOf(creditMonthPvNum));
                        pageViewData.setWeekUvNum(String.valueOf(creditWeekUvNum));
                        pageViewData.setMonthUvNum(String.valueOf(creditMonthUvNum));
                        pageViewData.setAnalysisCode(analysisCode);
                        pageViewData.setOrgId(orgId);
                        pageViewData.setBusinessSource("1");
                        pageViewData.setLoginSource(String.valueOf(loginSource));
                        pageViewData.setCreditBank(String.valueOf(i));
                        creditloanEnterDataDao.saveCreditloanEnterData(pageViewData);
                    }
                }

            }

            log.info("---------------------------------------------贷款访问量统计：------------------------------------------------------------");
            //保存贷款申请入口pv量
            for(int i = 1;i < loanCount;i++){
                log.info("贷款机构(15个)： 1.全民购、2.宜人贷、3.钱隆柜、4.你我贷借款、5.快易花、6.众安杏仁派、7.万达普惠、8.豆豆钱、9.360借条、10.卡卡贷、11.苏宁消费金融、12.小树时代、13.功夫贷、14.人品贷、15.微贷'");
                log.info("当前业务编号：" + i);
                for(int openType = 1;openType < 3 ;openType++){ //打开位置 1-APP 2-微信H5；
                    log.info("开始计算~~~");
                    log.info("打开位置 1-APP 2-微信H5 --- 当前设Open Type：" + openType);
                    for(int loginSource = 1;loginSource < 3; loginSource++){
                        log.info("设备访问来源 1.安卓；2.IOS --- " + loginSource);
                        CreditloanEnterData pageViewData = new CreditloanEnterData();
                        Long dayViewPage = pageViewRecordDao.getPageViewOrUniqueVisitorBySource(String.valueOf(loginSource), "2", String.valueOf(openType), "1", "1",null,orgId,null,String.valueOf(i));
                        //获取周pv量
                        Long loanWeekPvNum = pageViewRecordDao.selectCleditOrLoanPvOrUvNumber("2", String.valueOf(openType), "pv", "week", orgId,null,String.valueOf(i),String.valueOf(loginSource));
                        //获取月pv量
                        Long loanMonthPvNum = pageViewRecordDao.selectCleditOrLoanPvOrUvNumber("2", String.valueOf(openType), "pv", "month", orgId,null,String.valueOf(i),String.valueOf(loginSource));

                        loanWeekPvNum = (loanWeekPvNum==null?0:loanWeekPvNum) + (dayViewPage==null?0:dayViewPage);
                        loanMonthPvNum = (loanMonthPvNum==null?0:loanMonthPvNum) + (dayViewPage==null?0:dayViewPage);

                        Long dayUniqueVisitor = pageViewRecordDao.getPageViewOrUniqueVisitorBySource(String.valueOf(loginSource), "2", String.valueOf(openType), "4", "1",null,orgId,null,String.valueOf(i));
                        //获取周uv量
                        Long loanWeekUvNum = pageViewRecordDao.selectCleditOrLoanPvOrUvNumber("2", String.valueOf(openType), "uv", "week", orgId,null,String.valueOf(i),String.valueOf(loginSource));
                        //获取月uv量
                        Long loanMonthUvNum = pageViewRecordDao.selectCleditOrLoanPvOrUvNumber("2", String.valueOf(openType), "uv", "month", orgId,null,String.valueOf(i),String.valueOf(loginSource));

                        loanWeekUvNum = (loanWeekUvNum==null?0:loanWeekUvNum) + (dayUniqueVisitor==null?0:dayUniqueVisitor);
                        loanMonthUvNum = (loanMonthUvNum==null?0:loanMonthUvNum) + (dayUniqueVisitor==null?0:dayUniqueVisitor);

                        log.info("日VP量：" + dayViewPage + ";周PV量：" + loanWeekPvNum + ";月PV量：" + loanMonthPvNum);
                        log.info("日UV量：" + dayUniqueVisitor + ";周UV量：" + loanWeekUvNum + ";月UV量：" + loanMonthUvNum);
                        pageViewData.setOpenType(String.valueOf(openType));
                        pageViewData.setDayPvNum(String.valueOf(dayViewPage));
                        pageViewData.setDayUvNum(String.valueOf(dayUniqueVisitor));
                        pageViewData.setWeekPvNum(String.valueOf(loanWeekPvNum));
                        pageViewData.setMonthPvNum(String.valueOf(loanMonthPvNum));
                        pageViewData.setWeekUvNum(String.valueOf(loanWeekUvNum));
                        pageViewData.setMonthUvNum(String.valueOf(loanMonthUvNum));
                        pageViewData.setAnalysisCode(analysisCode);
                        pageViewData.setOrgId(orgId);
                        pageViewData.setBusinessSource("2");
                        pageViewData.setLoginSource(String.valueOf(loginSource));
                        pageViewData.setLoanSource(String.valueOf(i));
                        creditloanEnterDataDao.saveCreditloanEnterData(pageViewData);
                    }
                }
            }
            log.info("---------------------------------------------业务访问量统计 END：------------------------------------------------------------");

            //我要申请页面宣传海报点击数，“我要申请”按钮点击数
            Long posterApplyCount = pageViewRecordDao.checkOperateCount("1",orgId);
            dataAnalysisRecord.setPosterApplyCount(String.valueOf(posterApplyCount));
            log.info("我要申请页面宣传海报点击数，“我要申请”按钮点击数 :" + posterApplyCount);

            //我要申请点击数(办卡)
            Long confirmApplyCount = pageViewRecordDao.checkOperateCount("2",orgId);
            dataAnalysisRecord.setConfirmApplyCount(String.valueOf(confirmApplyCount));
            log.info("我要申请点击数(办卡) : " + confirmApplyCount);

            //前往申请或注册点击数(贷款)
            Long gotoApplyLoanCount = pageViewRecordDao.checkOperateCount("3",orgId);
            dataAnalysisRecord.setGotoApplyLoanCount(String.valueOf(gotoApplyLoanCount));
            log.info("前往申请或注册点击数(贷款): " + gotoApplyLoanCount);

            //贷款宣传海报滑动查看数
            Long loanPosterCount = pageViewRecordDao.checkOperateCount("4",orgId);
            dataAnalysisRecord.setLoanPosterCount(String.valueOf(loanPosterCount));
            log.info("贷款宣传海报滑动查看数: " + loanPosterCount);

            //结束时间
            dataAnalysisRecord.setFinishDate(new Date());
            log.info(dataAnalysisRecord.toString());
            log.info("****************** OrgId: " + orgId + ";组织名称: " + orgInfo.getOrgName() + " 分析 END   ******************* ");
            dataAnalysisRecordDao.saveDataAnalysisRecord(dataAnalysisRecord);


        }
        //清空查询记录表今天前的数据
        //int delCount = pageViewRecordDao.deletePageViewHistoryRecord();
        //log.info("Delete View Record: " + delCount);

    }

    @Override
    public long saveOrgWxTemplate(OrgWxTemplate orgWxTemplate) {
        orgWxTemplate.setCreateDate(new Date());
        orgWxTemplate.setCreateBy(CommonUtil.getLoginUser().getUsername());
        return orgWxTemplateDao.saveOrgWxTemplate(orgWxTemplate);
    }

    @Override
    public long updOrgWxTemplate(OrgWxTemplate orgWxTemplate) {
        orgWxTemplate.setUpdateDate(new Date());
        orgWxTemplate.setUpdateBy(CommonUtil.getLoginUser().getUsername());
        return orgWxTemplateDao.updOrgWxTemplate(orgWxTemplate);
    }

    @Override
    public List<OrgWxTemplate> getByPager(OrgWxTemplate conf, Page<OrgWxTemplate> page) {
        return orgWxTemplateDao.getByPager(conf,page);
    }

    @Override
    public List<TSysOption> querySysOptionList() {
        return tSysOptionDao.querySysOptionList();
    }

    @Override
    public long updsysOption(TSysOption tSysOption) {
        tSysOption.setDateUpdated(new Date());
        tSysOption.setUpdateBy(CommonUtil.getLoginUser().getUsername());
        return tSysOptionDao.updTSysOption(tSysOption);
    }

    @Override
    public List<OrgSourceConf> queryOrgSourcConfList(OrgSourceConf orgSourcConf,Page<OrgSourceConf> page) {
        List<OrgSourceConf> list=new ArrayList<>();
       if(StringUtils.isBlank(orgSourcConf.getType())){
           orgSourcConf.setType("1");
           List<OrgSourceConf> orgSourceConfList = orgSourceConfDao.getOrgSourceConfList(orgSourcConf, page);
           list.addAll(orgSourceConfList);
           orgSourcConf.setType("2");
           List<OrgSourceConf> confList = orgSourceConfDao.getOrgSourceConfList(orgSourcConf, page);
           list.addAll(confList);
       }else {
           list = orgSourceConfDao.getOrgSourceConfList(orgSourcConf, page);
       }
        return list;
    }

    @Override
    public long saveOrgSourceConf(OrgSourceConf orgSourceConf) {
        return orgSourceConfDao.saveOrgSourceConf(orgSourceConf);
    }

    @Override
    public void deleteOrgSourceConf(OrgSourceConf orgSourcConf) {
        orgSourceConfDao.deleteOrgSourceConf(orgSourcConf);
    }

	
	@Override
	public List<BlackList> selectBlacks(BlackList baseinfo, Page<BlackList> page) {
		return superBankUserInfoDao.selectBlacks(baseinfo, page);
	}

	
	@Override
	public Result addUserBlack(BlackList baseinfo) {
		Result result = new Result();
	 if(baseinfo!=null) {
		Integer type = baseinfo.getType();
		if(type==null) {
			result.setMsg("黑名单类型不能为空");
			return result;
		}
		if(StringUtils.isBlank(baseinfo.getUserCode()) && StringUtils.isBlank(baseinfo.getUserPhone())
				&& StringUtils.isBlank(baseinfo.getUserIdCard())) {
			result.setMsg("用户ID/身份证、手机号至少填一项");
			return result;
		}
		if(StringUtils.isNotBlank(baseinfo.getUserCode())) {
			int i = superBankUserInfoDao.selectblackByUserId(type, baseinfo.getUserCode());
			if(i>0) {
				result.setMsg("同一个类型，同一个用户ID只能添加一次");
				return result;
			}
		}
		if(StringUtils.isNotBlank(baseinfo.getUserPhone())) {
			int i = superBankUserInfoDao.selectblackByPhone(type,baseinfo.getUserPhone());
			if(i>0) {
				result.setMsg("同一种类型，同一个手机号只能添加一次");
				return result;
			}
		}
		if(StringUtils.isNotBlank(baseinfo.getUserIdCard())) {
			int i = superBankUserInfoDao.selectblackByIdCard(type, baseinfo.getUserIdCard());
			if(i>0) {
				result.setMsg("同一种类型,同一个身份证号只能添加一次");
				return result;
			}
		}
	   UserLoginInfo loginInfo = CommonUtil.getLoginUser();
	   baseinfo.setCreateBy(loginInfo.getUsername());
	   baseinfo.setCreateDate(new Date());
	   baseinfo.setStatus(1);
	   baseinfo.setUserCode(StringUtils.isNotBlank(baseinfo.getUserCode()) ? baseinfo.getUserCode():"");
	   int black = superBankUserInfoDao.insertBlack(baseinfo);
	   if(black>0) {
		   BlackListLog log=new BlackListLog();
		   addLogData(log,baseinfo);
		   log.setType(OperType.ADDBLACK.getOperType());
		   superBankUserInfoDao.insertBlackLog(log);
		   result.setMsg("保存成功");
		   result.setStatus(true);
	   }
	  }
		return result;
	}


	@Override
	public int deleteBlackById(Long id) {
		BlackList blackList = superBankUserInfoDao.selectBlackDetail(id);
		if(blackList!=null) {
			if(blackList.getStatus()==1) {
				throw new RuntimeException("\"黑名单\"状态为打开，不可以删除");
			}
		}else {
			throw new RuntimeException("该黑名单不存在");
		}
		return superBankUserInfoDao.deleteBlackById(id);
	}


	@Override
	public int updateBlackById(BlackList baseInfo) {
		int i = superBankUserInfoDao.updateblackStatus(baseInfo);
		if(i>0) {
			   BlackListLog log=new BlackListLog();
			   addLogData(log,baseInfo);
			   if(baseInfo.getStatus()==0) {
				   log.setType(OperType.CLOSEBLACK.getOperType());
			   }else if(baseInfo.getStatus()==1) {
				   log.setType(OperType.OPENBLACK.getOperType());
			   }
			   superBankUserInfoDao.insertBlackLog(log);
		}
		return i;
	}

	private void addLogData(BlackListLog log,BlackList baseInfo) {
		   log.setBlackListId(baseInfo.getId());
		   log.setCreateBy(CommonUtil.getLoginUser().getUsername());
		   log.setCreateDate(new Date());
		   log.setRemark(baseInfo.getRemark());
		   log.setStatus(baseInfo.getStatus());
	}

	
	@Override
	public List<BlackListLog> selectBlackLogs(Page<BlackListLog> baseInfo, Long blackId) {
		return superBankUserInfoDao.selectBlackLogs(baseInfo, blackId);
	}

    @Override
    public List<BusinessConf> selectBusinessConfList() {
        return businessConfDao.selectBusinessConfList();
    }

    @Override
    public List<OrgBusinessConf> selectOrgBusinessConfByOrgId(Long orgId) {
        return orgBusinessConfDao.selectOrgBusinessConfByOrgId(orgId);
    }

    @Override
    public void saveOrUpdateOrgBusinessConfList(List<OrgBusinessConf> orgBusinessConfList) {
        if(orgBusinessConfList!=null && orgBusinessConfList.size()>0){
            for (OrgBusinessConf orgBusinessConf:orgBusinessConfList){
                saveOrUpdateOrgBusinessConf(orgBusinessConf);
            }
        }
    }

    @Override
    public void saveOrUpdateOrgBusinessConf(OrgBusinessConf orgBusinessConf) {
        if(OrgBusinessConf.Opear.INSERT.equals(orgBusinessConf.getOpear())){    //新增操作
            orgBusinessConfDao.insert(orgBusinessConf);
        }
        if(OrgBusinessConf.Opear.UPDATE.equals(orgBusinessConf.getOpear())){    //更新操作
            orgBusinessConfDao.update(orgBusinessConf);
        }
        if(OrgBusinessConf.Opear.NONE.equals(orgBusinessConf.getOpear())){      //不做操作

        }
    }

    @Override
    public Result checkCreditBonusData(String operType,CreditCardBonus creditCardBonus) {
        Result result = new Result();
        if( null== creditCardBonus ){
            result.setMsg("入参不能为空");
            return result;
        }
        Long sourceId = creditCardBonus.getSourceId();
        BigDecimal cardCompanyBonus = creditCardBonus.getCardCompanyBonus();
        BigDecimal cardOemBonus = creditCardBonus.getCardOemBonus();
        BigDecimal firstCompanyBonus = creditCardBonus.getFirstCompanyBonus();
        BigDecimal firstOemBonus = creditCardBonus.getFirstOemBonus();
        Long orgId = creditCardBonus.getOrgId();
        if("add".equals(operType)) {
            if( null== orgId ){
                result.setMsg("组织不能为空");
                return result;
            }
        }

        if( null== sourceId ){
            result.setMsg("银行不能为空");
            return result;
        }
        if(null== cardCompanyBonus){
            result.setMsg("发卡公司截留不能为空");
            return result;
        }
        if(null== cardOemBonus){
            result.setMsg("发卡OEM截留不能为空");
            return result;
        }
        if(null==firstCompanyBonus){
            result.setMsg("首刷公司截留不能为空");
            return result;
        }
        if(null== firstOemBonus){
            result.setMsg("首刷OEM截留不能为空");
            return result;
        }

        //根据总奖金的格式，判断现在填写的，格式是否一致，再判断大小
        if(!checkLoanBonusType(null, cardCompanyBonus.toString())){
            result.setMsg("发卡公司截留格式不正确");
            return result;
        }
        if(!checkLoanBonusType(null, cardOemBonus.toString())){
            result.setMsg("发卡OEM截留格式不正确");
            return result;
        }
        if(!checkLoanBonusType(null, firstCompanyBonus.toString())){
            result.setMsg("首刷公司截留格式不正确");
            return result;
        }
        if(!checkLoanBonusType(null, firstOemBonus.toString())){
            result.setMsg("首刷OEM截留格式不正确");
            return result;
        }
        CreditcardSource creditSource = creditcardSourceDao.selectDetail((long)sourceId);
        if(null== creditSource ) {
            result.setMsg("找不到对应的银行");
            return result;
        }
        BigDecimal cardBonus = creditSource.getCardBonus();
        BigDecimal firstBrushBonus = creditSource.getFirstBrushBonus();

        // 发卡公司截留+发卡品牌截留<=发卡奖金
        if(cardBonus.compareTo(cardCompanyBonus.add(cardOemBonus)) < 0) {
            result.setMsg("发卡公司截留奖金+发卡品牌截留奖金必须<=发卡奖金");
            return result;
        }
        // 首刷公司截留+首刷品牌截留<=首刷奖金
        if(firstBrushBonus.compareTo(firstCompanyBonus.add(firstOemBonus)) < 0) {
            result.setMsg("首刷公司截留奖金+首刷品牌截留奖金必须<=首刷奖金");
            return result;
        }
        result.setStatus(true);
        result.setMsg("校验通过");
        return result;
    }

    public void insertModuleBatch(List<ModulesNewStyles> list, Long orgId) {
        modulesNewStyleDao.insertModuleBatch(list, orgId);
    }

    public List<ModulesNewStyles> selectTutorModelByOrgId(Long orgId) {
        return modulesNewStyleDao.selectTutorModelByOrgId(orgId);
    }

    public List<ModulesNewStyles> selectBankModelByOrgId(Long orgId) {
        return modulesNewStyleDao.selectBankModelByOrgId(orgId);
    }

    public void deleteModuleByOrgId(Long orgId) {

        modulesNewStyleDao.deleteModuleByOrgId(orgId);
    }
}
