package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface SuperBankService {

    /**
     * 用户管理查询
     * @param SuperBankUserInfo
     * @param page
     * @return
     */
    List<SuperBankUserInfo> selectUserInfoPage(SuperBankUserInfo SuperBankUserInfo, Page<SuperBankUserInfo> page);

    /**
     * 用户管理详情
     * @param userCode
     * @return
     */
    Result selectUserDetail(String userCode);

    /**
     * 获取用户账户信息
     * @param userCode
     * @return
     */
    Result getUserAccountInfo(String userCode);

    /**
     * 超级银行家账户明细
     * @param record
     * @param pageNo
     * @param pageSize
     * @return
     */
    Result getAccountTranInfo(AccountInfoRecord record, int pageNo, int pageSize);

    /**
     * 修改超级银行家用户
     * @param baseInfo
     * @return
     */
    Result updateUserInfo(SuperBankUserInfo baseInfo);

    /**
     * 获取结算卡信息
     * @param cardNo
     * @return
     */
    Result getCardInfo(String cardNo);

    /**
     * 获取支行信息异常
     * @param bankName
     * @param cityName
     * @return
     */
    Result getPosCnaps(String bankName, String cityName);

    /**
     * 修改超级银行家结算卡
     * @param baseInfo
     * @return
     */
    Result updateUserCard(SuperBankUserCard baseInfo);

    /**
     * 获取直营组织的初始化信息
     * @return
     */
    OrgInfo getBaseOrgInfo();

    /**
     * 新增超级银行组织
     * @param orgInfo
     * @return
     */
    Result addOrgInfo(OrgInfo orgInfo);

    /**
     * 银行家组织分页查询
     * @param baseInfo
     * @param page
     * @return
     */
    List<OrgInfo> selectOrgInfoPage(OrgInfo baseInfo, Page<OrgInfo> page);

    /**
     * 获取所有的银行组织
     * @return
     */
    List<OrgInfo> getOrgInfoList();

    /**
     * 校验orgId
     * 1.校验orgId在超级银行家组织是否已存在，已存在则返回false
     * 2.从nposp库获取V2组织ID、超级还组织ID
     * @param orgId
     * @return
     */
    Result checkOrgId(String orgId);

    /**
     * 修改超级银行家组织
     * @param orgInfo
     * @return
     */
    int updateOrgInfo(OrgInfo orgInfo);

    void insert111(Long orgId);

    /**
     * 超级银行家详情
     * @param orgId
     * @return
     */
    Result orgInfoDetail(Long orgId);

    /**
     * 条件分页查询订单
     * @param baseInfo
     * @param page
     * @return
     */
    List<OrderMain> selectOrderPage(OrderMain baseInfo, Page<OrderMain> page);

    /**
     * 分润明细订单查询
     * @param baseInfo
     * @param page
     * @return
     */
    List<UserProfit> selectProfitDetailPage(UserProfit baseInfo, Page<UserProfit> page);

    /**
     * 用户提现记录
     * @param baseInfo
     * @param page
     * @return
     */
    List<UserObtainRecord> selectObtainRecordPage(UserObtainRecord baseInfo, Page<UserObtainRecord> page);

    /**
     * 订单数据汇总
     * @param baseInfo
     * @return
     */
    OrderMainSum selectOrderSum(OrderMain baseInfo);

    /**
     * 分润明细汇总
     * @param baseInfo
     * @return
     */
    OrderMainSum selectProfitDetailSum(UserProfit baseInfo);

    /**
     * 用户提现记录汇总
     * @param baseInfo
     * @return
     */
    OrderMainSum selectObtainRecordSum(UserObtainRecord baseInfo);

    /**
     * 模糊查询SuperBankUserInfo
     * @param userCode
     * @return
     */
    List<SuperBankUserInfo> selectUserInfoList(String userCode);

    /**
     * 导出用户管理
     * @param response
     * @param userInfo
     */
    void exportUserInfo(HttpServletResponse response, SuperBankUserInfo userInfo);

    /**
     * 导出超级银行家代理授权订单
     * @param order
     */
    void exportAgentOrder(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级银行家开通办理信用卡订单
     * @param order
     */
    void exportOpenCredit(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级银行家办理信用卡订单
     * @param order
     */
    void exportCreditOrder(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级银行家贷款订单
     * @param order
     */
    void exportLoanOrder(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级银行家保险订单
     * @param response
     * @param order
     */
    void exportInsuranceOrder(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级兑订单
     * @param response
     * @param order
     */
   void exportSuperExcOrder(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级银行家收款订单
     * @param order
     */
    void exportReceiveOrder(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级银行家还款订单
     * @param order
     */
    void exportRepayOrder(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级银行家订单分润详情
     * @param order
     */
    void exportProfitDetail(HttpServletResponse response, UserProfit order);

    /**
     * 导出超级银行家用户提现记录
     * @param order
     */
    void exportObtainRecord(HttpServletResponse response, UserObtainRecord order);

    /**
     * 导出超级银行家征信记录
     * @param order
     */
    void exportInquiryOrder(HttpServletResponse response, ZxProductOrder order);
    /**
     * 贷款订单导入
     * @param file
     * @param loanSourceId
     * @return
     */
    Result importLoanOrder(MultipartFile file, String loanSourceId);

    /**
     * 获取所有的贷款机构
     * @return
     */
    List<LoanSource> getLoanList();

    List<LoanSource> changeSource(String type);

    /**
     * 获取开放平台功能点配置
     * @return
     */
    List<OpenFunctionConf> getOpenFunctionConfList();

    /**
     * 超级银行家订单详情
     * @param orderNo
     * @return
     */
    OrderMain selectOrderDetail(String orderNo);

    /**
     * 超级银行家还款订单详情
     * @param orderNo
     * @return
     */
    OrderMain selectRepayOrderDetail(String orderNo);
    /**
     * 信用卡奖金查询
     * @param creditCardconf
     * @param page
     * @return
     */
    Result getCreditBonusConf(CreditCardBonus creditCardconf,Page<CreditCardBonus> page);

    /**
     * 获取银行列表
     * @return
     */
    List<CreditcardSource> banksList();
    
    /**
     * 贷款机构列表
     * @return
     */
    List<LoanSource> loanCompanyList();
    /**修改*/
    int updCreditCardConf(CreditCardBonus creditCardConf);
    
    /***新增银行*/
    int addBank(CreditCardBonus creditCardConf);

    /**查询组织彩票配置是否存在*/
    int checkLotteryBonusconfExist(Long orgId);


    /**查询彩票奖金配置*/
    List<LotteryBonusConf> getLotteryBonusConfList(LotteryBonusConf conf,Page<LotteryBonusConf> page);

    /**新增组织彩票配置*/
    int saveLotteryBonusconf(LotteryBonusConf conf);
    /**修改组织彩票配置*/
    int updLotteryBonusconf(LotteryBonusConf conf);

    /**查询彩票导入记录*/
    List<LotteryImportRecords> getLotteryImportRecordsList(LotteryImportRecords record,Page<LotteryImportRecords> page);

    /**新增彩票导入记录*/
    int saveLotteryImportRecord(LotteryImportRecords record);

    /**
     * 根据id删除彩票订单导入记录*/
    int deleteLotteryImportRecord(Long id);

    /**
     * 彩票订单导入*/
    Result importLottery(MultipartFile file);

    /**彩票导入记录文件匹配定时任务*/
    void lotteryMatchTask();

    /**根据Id获取导入记录*/
    LotteryImportRecords getLotteryImportRecordById(String id);


    /**查询贷款奖金配置*/
    List<LoanBonusConf> getLoanBonusConfList(LoanBonusConf conf,Page<LoanBonusConf> page);
    
    int updLoanBonusconf(LoanBonusConf conf);
    
    int saveLoanBonusconf(LoanBonusConf conf);
    
    /**分润配置查询*/
    List<OrgProfitConf> getOrgProfitConfList(OrgProfitConf conf,Page<OrgProfitConf> page);
    /**修改分润配置*/
    int updOrgProfitConf(OrgProfitConf conf);
    /**新增分润配置*/
    int saveOrgProfitConf(List<OrgProfitConf> confs);
    
    int isExistOrgConf(String orgId);

    /**查询公告列表*/
    List<Notice> findNotice(Notice searchCondi,Page<Notice> pager);
    /**修改公告*/
    long updNotice(Notice notice);
    /**新增公告*/
    long addNotice(Notice notice);
    /**公告详情*/
    Notice noticeDetail(String id);
    int sendNotice(Notice notice);
    
    /**查询广告列表*/
    List<Ad> findAds(Ad searchCondi,Page<Ad> pager);
    /**修改广告,updType=1 普通修改      updType=2  状态开关修改*/
    long updAd(Ad ad,String updType);
    /**新增广告*/
    long addAd(Ad ad);
    /**广告详情*/
    Ad adDetail(Ad ad);
    /**删除广告*/
    long delAd(String id);

    /**
     * 分润入账
     * @param batchNo
     */
	void profitAccount(String batchNo);

    /**
     * 超级银行家用户开户
     */
    void createMerAccount();
    
    int isExistLoanCompany(long companyId, Long orgId);

    int isExistBankConf(long sourceId);
    int isExistBankConfWithSiAndOrgId(long sourceId, Long orgId);

    /**
     *
     * @param batchNo
     * @param profitStatus
     */
    int updateProfitStatus(String batchNo, String profitStatus);

    /**
     * 获取出款预警的基本信息
     * @return
     */
    Map<String,Object> getOutWarn();

    /**
     * 修改出款预警信息
     * @param param
     * @return
     */
    Result updateOutWarn(Map<String, Object> param);

    /**
     * 获取超级银行家账户余额
     * @return
     */
    String getOutWarnAccount();

    /**
     * 获取充值记录
     * @param baseInfo
     * @param page
     * @return
     */
    List<RechargeRecord> getRechargeList(RechargeRecord baseInfo, Page<RechargeRecord> page);

    /**
     * 超级银行家账户充值
     * @param amount
     * @return
     */
    int updateRecharge(String amount);

    /**
     * 修改组织开户状态
     * @param agentNo
     * @return
     */
    int updateOrgAccount(String agentNo);

    /**
     * 超级银行组织开户
     * @param agentNo
     * @return
     */
    Result openAccount(String agentNo);

    /**
     * 信用卡申请记录
     * @param file
     * @param bankSourceId
     * @return
     */
    Result importCreditRecord(MultipartFile file, String bankSourceId);


    /**
     * 查询彩票订单
     * @param info
     * @param page
     * @return
     */
   	public List<LotteryOrder> qryLotteryOrder(LotteryOrder info, Page<LotteryOrder> page);

   	/**
   	 * 汇总信息
   	 * @param info
   	 * @return
   	 */
   	public LotteryOrder qrySumOrder(LotteryOrder info);

    /**
     * 用户统计查询
     * @param info
     * @return
     */
    Map<String,Object> getUserTotal(SuperBankUserInfo info);

    LoanBonusConf selectLoanBonusConfByPrimaryKey(Long id);

    OrgProfitConf selectOrgProfitConfByPrimaryKey(Long id);

    Ad selectAdByPrimaryKey(Long id);

    int deleteNotice(Long id);

    int updateNoticePop(Long id, Integer popSwitch);

    int updateUserStatus(SuperBankUserInfo userInfo);

    Result checkLoanData(LoanBonusConf loan);

    boolean checkExistsOrgId(Long orgId);

    LoanSource selectLoanDetail(Long aLong);

    /**导出彩票代购订单*/
    public void exportLotteryOrder(HttpServletResponse response, LotteryOrder baseInfo);

    public List<RankingRecord> qryRankingRecord(RankingRecord info, Page<RankingRecord> page);

    public RankingRecord getRankingRecordTotalInfo(RankingRecord info);

    /**根据条件查询排行榜规则管理*/
    List<RankingRule> getRankingRuleList(RankingRule baseInfo,Page<RankingRule> page);
    /**修改排行榜开关*/
    int updateBankingRuleStatus(RankingRule info);
    /**根据id获取排行榜配置详情*/
    RankingRule getRankingRuleById(Long id);
    /**根据排行榜配置id获取等级配置*/
    List<RankingRuleLevel> getRankingRuleLevelListByRuleId(Long id);
    /**生成新的排行榜记录*/
    Result insertRankingRule(RankingRule rankingRule);
    /**修改排行榜记录*/
    Result updateRankingRule(RankingRule rankingRule);
    /**根据Rule Code获取排行榜记录*/
    RankingRule getRankingRuleByCode(String code);
    /**批量添加排行榜等级配置*/
    int insertRankingRuleLevelBatch(List<RankingRuleLevel> list,Long ruleId);
    /**删除排行榜等级设置*/
    int deleteRankingRuleLevelByRuleId(Long id);

    /**根据组织id模糊查找所有的排行榜设置*/
    List<RankingRule> selectRankingRuleByOrgId(String orgId);
    

    public RankingRecordDetail qryRankingDetail(String recordId, Page<RankingRecordDetail> page,boolean isSort);

    /**移除或取消移除一条榜单排名*/
    public int updRankingDetail(RankingRecordDetail baseInfo);
    
    public List<RankingRecordDetail> getPushRankingDetail(RankingRecordDetail baseInfo);


    List<RankingPushRecordInfo> selectRankingPushRecordPage(Map<String,Object> params, Page<RankingPushRecordInfo> page);

    void exportRankingPushRecord(HttpServletResponse response, Map<String,Object> params);

    String selectRankingPushRecordTotalMoneySum(Map<String,Object> params);

    String selectRankingPushRecordPushTotalMoneySum(Map<String,Object> params);
    
    public Result pushRankingBonus(RankingRecord data,List<RankingRecordDetail> detailList,
    		com.alibaba.fastjson.JSONObject accoutInfo);
    /**根据ruleId获取修改排行榜修改记录*/
    List<RankingRuleHistory> selectRankingRuleHistory(Long ruleId);
    /**新增排行榜修改记录*/
    int insertRankingRuleHistory(RankingRuleHistory hisRecord);

    public Result getOrgAccountInfo(String userCode);

    public void invokeInterface(RankingRecord data);
    
    public CarOrder getCarOrders(CarOrder order,Page<CarOrder> page);
    
    public Result carOrderDetail(String orderNo);

    public int addCarOrderProfitConf(CarOrderProfitConf conf);
    
    public CarOrderProfitConf findCarOrderProfitConf();
    
    public int updCarOrderProfitConf(CarOrderProfitConf conf);
    
    public void exportCarOrder(HttpServletResponse response,CarOrder order);
    
    /**超级银行家数据分析定时任务*/
    void analysisDataTask();

    long saveOrgWxTemplate(OrgWxTemplate orgWxTemplate);

    long updOrgWxTemplate(OrgWxTemplate orgWxTemplate);

    List<OrgWxTemplate> getByPager( OrgWxTemplate conf, Page<OrgWxTemplate> page);

    List <TSysOption> querySysOptionList();

    long updsysOption(TSysOption tSysOption);

    List<OrgSourceConf> queryOrgSourcConfList(OrgSourceConf orgSourcConf,Page<OrgSourceConf> page);

    long saveOrgSourceConf(OrgSourceConf orgSourceConf);

    void deleteOrgSourceConf(OrgSourceConf orgSourcConf);
    
    /**
     * 黑名单查询接口
     * @param List<BlackList>
     */
    List<BlackList> selectBlacks(BlackList baseinfo, Page<BlackList> page);
    /**
     * 新增黑名单
     * @param Result
     */
    Result addUserBlack(BlackList baseinfo); 
    /**
     * 删除黑名单
     * @param int
     */
    int deleteBlackById(Long id);
    /**
     *更新黑名单状态 
     * @param int
     */
    int updateBlackById(BlackList baseInfo);
 
    /**
     *查询黑名单日志 
     * @param List<BlackListLog>
     */
    List<BlackListLog> selectBlackLogs(@Param("baseInfo")Page<BlackListLog> baseInfo,@Param("blackId")Long blackId);

    /**
     * 查询所有组织升级业务
     * @return
     */
    List<BusinessConf> selectBusinessConfList();


    /**
     * 根据组织ID获取升级业务配置
     * @param orgId
     * @return
     */
    List<OrgBusinessConf> selectOrgBusinessConfByOrgId(@Param("orgId")Long orgId);

    /**
     * 保存或修改组织升级业务配置
     * @param orgBusinessConfList
     */
    void saveOrUpdateOrgBusinessConfList(List<OrgBusinessConf> orgBusinessConfList);


    /**
     * 保存或修改组织升级业务配置
     * @param orgBusinessConf
     */
    void saveOrUpdateOrgBusinessConf(OrgBusinessConf orgBusinessConf);

    Result checkCreditBonusData(String operType,CreditCardBonus creditCardBonus) ;
    /**根据orgId获取三大模块新样式列表*/
    List<ModulesNewStyles> selectTutorModelByOrgId(Long orgId);

    /**根据orgId获取三大模块新样式列表*/
    List<ModulesNewStyles> selectBankModelByOrgId(Long orgId);

    /**新增三大模块*/
    void insertModuleBatch(List<ModulesNewStyles> list, Long orgId);

    /**删除三大模块*/
    void deleteModuleByOrgId(Long orgId);

}
