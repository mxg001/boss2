package cn.eeepay.boss.action;

import cn.eeepay.boss.system.CommonConst;
import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.impl.SensorsService;
import cn.eeepay.framework.util.*;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/merchantBusinessProduct")
public class MerchantBusinessProductAction {

    private static final Logger log = LoggerFactory.getLogger(MerchantBusinessProductAction.class);

    private static final String merAudit ="merAudit";

    // 商户业务产品
    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;
    // 代理商
    @Resource
    private AgentInfoService agentInfoService;
    // 业务产品（服务套餐）定义
    @Resource
    private BusinessProductDefineService businessProductDefineService;
    // 商户信息
    @Resource
    private MerchantInfoService merchantInfoService;

    // 商户服务限额信息
    @Resource
    private MerchantServiceQuotaService merchantServiceQuotaService;
    // 商户服务签约费率
    @Resource
    private MerchantServiceRateService merchantServiceRateService;
    // 商户进件条件表(明细)
    @Resource
    private MerchantRequireItemService merchantRequireItemService;
    // 审核记录
    @Resource
    private ExaminationsLogService examinationsLogService;
    // 进件提交信息
    @Resource
    private AddRequireItemService addRequireItemService;
    // 服务费率拼接
    @Resource
    private ServiceProService serviceProService;
    // 商户服务表
    @Resource
    private MerchantServiceProService merchantServiceProService;
    // 业务进件资料表
    @Resource
    private BusinessRequireItemService businessRequireItemService;
    @Resource
    private TerminalInfoService terminalInfoService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private HardwareProductService hardwareProductService;
    @Resource
    private MerchantPreFreezeLogService merchantPreFreezeLogService;
    @Resource
    private ZqMerchantInfoService zqMerInfoService;
    @Resource
    private RiskRollService riskRollService;

    @Resource
    private MerchantRequireHistoryService merchantRequireHistoryService;
    @Resource
    private RedisService redisService;
    @Resource
    private SysWarningService sysWarningService;
    @Resource
    private PyIdentificationService pyIdentificationService;
    @Resource
    private OpenPlatformService openPlatformService;

    @Resource
    private SensorsService sensorsService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TransInfoService transInfoService;

    @Resource
    private AddCreaditcardLogService addCreaditcardLogService;

    @Resource
    private BossSysConfigService bossSysConfigService;

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private  CompanyInfoCompareService companyInfoCompareService;


    // 商户初始化
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAllInfo.do")
    @ResponseBody
    public Object selectAllInfo(@ModelAttribute("page") Page<MerchantBusinessProduct> page) {
        List<MerchantBusinessProduct> listMerbp = null;
        List<MerchantBusinessProduct> listMerbpResult = new ArrayList<>();
        try {
            listMerbp = merchantBusinessProductService.selectAllInfo(page);
            for (MerchantBusinessProduct mbp : listMerbp) {
                String merNo = mbp.getMerchantNo();

                try {
                    String str = ClientInterface.getMerchantAccountBalance(merNo);
                    JSONObject json1 = JSON.parseObject(str);
                    if ((boolean) json1.get("status")) {// 返回成功
                        AccountInfo ainfo = JSON.parseObject(str, AccountInfo.class);
                        mbp.setControlAmount(ainfo.getControlAmount());
                    } else {
                        mbp.setControlAmount(BigDecimal.ZERO);
                    }
                } catch (Exception e) {
                    log.error("商户预冻结金额查询报错 : " + merNo);
                    log.error("预冻结金额查询报错", e);
                    mbp.setControlAmount(null);
                }
                listMerbpResult.add(mbp);
            }

            page.setResult(listMerbpResult);
        } catch (Exception e) {
            log.error("初始化失败----", e);
        }
        return page;
    }

    // 商户初始化销售
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAllInfoSale.do")
    @ResponseBody
    public Object selectAllInfoSale(@ModelAttribute("page") Page<MerchantBusinessProduct> page) {
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            merchantBusinessProductService.selectAllInfoSale(page, principal.getRealName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化失败----", e);
        }
        return page;
    }

    // 商户模糊查询销售
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParamSale.do")
    @ResponseBody
    public Object selectByParamSale(@RequestParam("info") String param,
                                    @ModelAttribute("page") Page<MerchantBusinessProduct> page, HttpServletRequest request) {
        SelectParams selectParams = JSON.parseObject(param, SelectParams.class);
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                if (ais != null) {
                    if (selectParams.getAgentNode().equals("0")) {
                        selectParams.setAgentNode(ais.getAgentNode());
                    } else {
                        selectParams.setAgentNode(ais.getAgentNode() + "%");
                        selectParams.setAgentName("");
                    }
                } else {
                    return page;
                }
            } else {
                selectParams.setAgentNode(null);
            }
            merchantBusinessProductService.selectByParamSale(page, selectParams, principal.getRealName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模糊查询失败失败----", e);
        }
        boolean isSale = (boolean) request.getSession().getAttribute(CommonConst.isSalesperson);
        if (isSale) {
            List<MerchantBusinessProduct> list = page.getResult();
            String tmp;
            for (MerchantBusinessProduct item : list) {
                tmp = item.getMobilePhone();
                tmp = StrUtil.hide(tmp, 3, 7);
                item.setMobilePhone(tmp);
            }
        }
        List<MerchantBusinessProduct> list = page.getResult();
        if (list != null && list.size() > 0) {
            for (MerchantBusinessProduct item : list) {
                // 敏感信息屏蔽
                item.setMobilePhone(StringUtil.sensitiveInformationHandle(item.getMobilePhone(), 0));
            }
        }
        return page;
    }

    // 商户审核初始化
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAllByStatusInfo")
    @ResponseBody
    public Object selectAllByStatusInfo(@ModelAttribute("page") Page<MerchantBusinessProduct> page) {
        List<MerchantBusinessProduct> listMerbp = null;
        try {
            listMerbp = merchantBusinessProductService.selectAllByStatusInfo(page);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化失败----", e);
        }
        return page;
    }

    // 商户模糊查询
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam.do")
    @ResponseBody
    public Object selectByParam(@RequestParam("info") String param,
                                @ModelAttribute("page") Page<MerchantBusinessProduct> page) {
        Map<String, Object> maps = new HashMap<>();
        SelectParams selectParams = JSON.parseObject(param, SelectParams.class);
        List<MerchantBusinessProduct> listMerbp = null;
        List<MerchantBusinessProduct> listMerbpResult = new ArrayList<>();
        try {

            if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                if (ais != null) {
                    if (selectParams.getAgentNode().equals("0")) {
                        selectParams.setAgentNode(ais.getAgentNode());
                    } else {
                        selectParams.setAgentNode(ais.getAgentNode() + "%");
                        selectParams.setAgentName("");
                    }
                } else {
                    maps.put("page", page);
                    maps.put("status", true);
                    return maps;
                }
            } else {
                selectParams.setAgentNode(null);
            }

            listMerbp = merchantBusinessProductService.selectByParam(page, selectParams);

            for (MerchantBusinessProduct mbp : listMerbp) {
                // 敏感信息屏蔽
                mbp.setMobilePhone(StringUtil.sensitiveInformationHandle(mbp.getMobilePhone(), 0));

                if("V2-agent".equals(mbp.getSourceSys())){
                    mbp.setSourceSysSta(1);
                }else{
                    mbp.setSourceSysSta(2);
                }
                String merNo = mbp.getMerchantNo();
                mbp.setMerCreditCardStatus(merchantBusinessProductService.getMerCreditCard(merNo));
                try {
                    String str = ClientInterface.getMerchantAccountBalance(merNo);
                    JSONObject json1 = JSON.parseObject(str);
                    if ((boolean) json1.get("status")) {// 返回成功
                        AccountInfo ainfo = JSON.parseObject(str, AccountInfo.class);
                        mbp.setControlAmount(ainfo.getControlAmount());
                    } else {
                        mbp.setControlAmount(BigDecimal.ZERO);
                    }
                } catch (Exception e) {
                    log.error("商户预冻结金额查询报错 : " + merNo);
                    log.error("预冻结金额查询报错 ", e);
                    mbp.setControlAmount(null);
                }
                listMerbpResult.add(mbp);
            }

            page.setResult(listMerbpResult);
            maps.put("page", page);
            maps.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模糊查询失败失败----", e);
        }
        return maps;
    }

    // 商户审核模糊查询
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByStatusParam")
    @ResponseBody
    public Object  selectByStatusParam(@RequestParam("info") String param,
                                      @ModelAttribute("page") Page<MerchantBusinessProduct> page) {
        Map<String, Object> maps = new HashMap<>();
        try {
            SelectParams selectParams = JSON.parseObject(param, SelectParams.class);
            if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                if (ais != null) {
                    if (selectParams.getAgentNode().equals("0")) {
                        selectParams.setAgentNode(ais.getAgentNode());
                    } else {
                        selectParams.setAgentNode(ais.getAgentNode() + "%");
                        selectParams.setAgentName("");
                    }
                } else {
                    maps.put("msg", "找不到对应的代理商");
                    maps.put("status", false);
                    return maps;
                }
            } else {
                selectParams.setAgentNode(null);
            }
            merchantBusinessProductService.selectByStatusParam(page, selectParams);
            maps.put("page", page);
            maps.put("status", true);
        } catch (Exception e) {
            log.error("商户审核模糊查询失败----", e);
            maps.put("msg", "查询失败");
            maps.put("status", false);
        }
        return maps;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectMerBpHistoryList")
    @ResponseBody
    public Object selectMerBpHistoryList(@RequestParam("info") String param,
                                         @ModelAttribute("page") Page<MerchantBusinessProductHistory> page) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> params = JSONObject.parseObject(param, Map.class);
            merchantBusinessProductService.selectMerBpHistoryList(params, page);
        } catch (Exception e) {
            log.error("模糊查询失败失败----", e);
        }
        return page;
    }

    // 商户详情和审核查询
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectDetailInfoTwo.do")
    @ResponseBody
    public Object selectDetailInfoTwo(String ids, HttpServletRequest request) {
        return getDetail(ids, request, 0);
    }

    // 商户详情和审核查询
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectDetailInfo.do")
    @ResponseBody
    public Object selectDetailInfo(String ids, HttpServletRequest request) {
        return getDetail(ids, request, 1);
    }

    /**
     * 敏感信息获取
     */
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> getDataProcessing(String ids, HttpServletRequest request) throws Exception {
        return getDetail(ids, request, 3);
    }

    /**
     * 敏感信息获取(销售)
     */
    @RequestMapping(value = "/dataProcessingTwo")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> dataProcessingTwo(String ids, HttpServletRequest request) throws Exception {
        return getDetail(ids, request, 4);
    }


    /**
     * 商户审核详情 剥离其他查询详情，去除不必要的查询数据
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/merchantExamineDetail")
    @ResponseBody
    public Object merchantExamineDetail(String ids, HttpServletRequest request) {
        Map<String, Object> maps = new HashMap<>();
        try {
            if(!checkUserAudit(ids,true)){
                maps.put("msg", "该商户正在审核中,请重新选择!");
                maps.put("bols", false);
                return maps;
            }
            // 商户业务产品
            MerchantBusinessProduct mbp = merchantBusinessProductService.getMbpInfoDetail(Long.valueOf(ids));
            if(mbp==null){
                maps.put("msg", "详情查询失败");
                maps.put("bols", false);
                return maps;
            }
            maps.put("mbp", mbp);

            List<MerchantRequireItem> listmri = new ArrayList<MerchantRequireItem>();//进件资料
            List<MerchantRequireItem> listmris = new ArrayList<MerchantRequireItem>();//进件图片
            //获取进件项
            List<MerchantRequireItem> mriLists = null;
            if ("1".equals(mbp.getMerchantType())){//商户类型:1-个人
                 mriLists = merchantRequireItemService.getMerchantRequireItemList(mbp.getMerchantNo(),mbp.getBpId());
            }else{//商户类型:2-个体商户，3-企业商户',
                 mriLists = merchantRequireItemService.getByMer(mbp.getMerchantNo());
            }
            if (mriLists != null && mriLists.size()>0) {
                for (MerchantRequireItem mri : mriLists) {
                    if(mri!=null){
                        mri.setMerBpId(mbp.getId().toString());

                        //针对不同的进件项,处理
                        if("3".equals(mri.getMriId())){
                            mri.setLogContent(mri.getContent());
                        }else if("37".equals(mri.getMriId())){
                            IndustryMcc industryMcc = merchantRequireItemService.selectIndustryMccByMcc(mri.getContent());
                            if (industryMcc != null) {
                                mri.setIndustryMcc(industryMcc);
                                mri.setContent(industryMcc.getIndustryName1() + "-" + industryMcc.getIndustryName());
                            }
                        }

                        //图片,文件类型转换 阿里云地址
                        if ("1".equals(mri.getExampleType())) {
                            if(mri.getContent()!=null){
                                mri.setContent( CommonUtil.getImgUrlAgent(mri.getContent()));
                            }
                            listmris.add(mri);
                            continue;// 图片类型不加入 进件项审核表
                        }else if("2".equals(mri.getExampleType())){
                            if(mri.getContent()!=null){
                                mri.setContent( CommonUtil.getImgUrlAgent(mri.getContent()));
                            }
                        }
                        listmri.add(mri);
                    }
                }
            }
            String str1 = "";
            String str2 = "";
            String str3 = "";
            String companyName="";
            String licenseSocialCode="";

            for (MerchantRequireItem list : listmri) {
                if (list.getMriId().equals("2") || list.getMriId().equals("6")) {
                    str1 += list.getItemName() + ":" + list.getContent() + ",";
                }
                if (list.getMriId().equals("3") || list.getMriId().equals("4")) {
                    str2 += list.getItemName() + ":" + list.getContent() + ",";
                }
                if(list.getMriId().equals("34")){//营业执照号
                    str3 += list.getItemName() + ":" + list.getContent() + ",";
                    licenseSocialCode=list.getContent();

                }else if(list.getMriId().equals("38")){//营业执照全称
                    str3 += list.getItemName() + ":" + list.getContent() + ",";
                    companyName=list.getContent();
                }

            }
            if (listmris.size() > 0) {
                for (MerchantRequireItem lists : listmris) {
                    if (lists.getMriId().equals("9")) {
                        lists.setRemark(str1);
                        continue;
                    }
                    if (lists.getMriId().equals("11")) {
                        lists.setRemark(str2);
                        continue;
                    }
                    if (lists.getMriId().equals("12")) {
                        lists.setRemark(str3);
                        continue;
                    }
                    lists.setRemark("");
                }
            }
            maps.put("listmri", listmri);
            maps.put("listmris", listmris);

            String legalName="";//商户法人姓名
            //获取商户信息
            List<MerchantInfo> mi = merchantInfoService.selectByMertId(mbp.getMerchantNo());
            if(mi!=null&&mi.size()>0){
                if (mi.get(0).getBusinessType() != null) {
                    if ("1".equals(mi.get(0).getMerchantType())) {
                        mi.get(0).setSysName(merchantInfoService.selectSysDictByKey(mi.get(0).getBusinessType(), "-1").getSysName());
                        if (mi.get(0).getIndustryType() != null) {
                            mi.get(0).setTwoSysName(merchantInfoService.selectSysDictByKey(mi.get(0).getIndustryType(), mi.get(0).getBusinessType()).getSysName());
                        }
                    } else {
                        IndustryMcc industryMcc = merchantRequireItemService.selectIndustryMccByMcc(mi.get(0).getIndustryType());
                        if (industryMcc != null) {
                            mi.get(0).setSysName(industryMcc.getIndustryName());
                            mi.get(0).setTwoSysName(industryMcc.getIndustryName1());
                        }
                    }
                }
                legalName=mi.get(0).getLawyer();
                maps.put("mi",mi.get(0));
                maps.put("agent", agentInfoService.selectByagentNo(mi.get(0).getOneAgentNo()));//一级代理商
                maps.put("merAgent", agentInfoService.selectByagentNo(mi.get(0).getAgentNo()));//直属代理商
            }
            //企业对比信息
            List<CompanyInfoCompare> comparList=companyInfoCompareService.getCompanyInfoCompareOne(companyName,licenseSocialCode,legalName);
            maps.put("comparList",comparList);

            List<ExaminationsLog> listel = new ArrayList<>();//审核记录
            List<ExaminationsLog> exlistel = new ArrayList<>();// 复审记录
            // 审核记录
            List<ExaminationsLog> exList = examinationsLogService.selectByMerchantId(mbp.getId().toString());
            for (ExaminationsLog elog : exList) {
                // 初审
                if (elog.getExamineType() == 1) {
                    listel.add(elog);
                } else if (elog.getExamineType() == 2) {
                    // 复审
                    exlistel.add(elog);
                }
            }
            maps.put("listel", listel);
            maps.put("exlistel", exlistel);


            Page<TerminalInfo> tiPage = new Page<>();
            //获取机具信息
            terminalInfoService.selectAllInfoBymerNoAndBpId(mbp.getMerchantNo(), mbp.getBpId(), tiPage);
            maps.put("tiPage", tiPage);


            maps.put("bols", true);
        } catch (Exception e) {
            log.error("详情查询报错", e);
            maps.put("msg", "详情查询报错");
            maps.put("bols", false);
        }
        return maps;
    }

    /**
     * 判断redis中，该进件是商户审核否被锁定
     * @param mbpId 商户进件ID
     * @param storage 是否需要存储涮新缓存CD
     * @return
     */
    private boolean checkUserAudit(String mbpId,boolean storage){
        String mbpIdKey=MerchantBusinessProductAction.merAudit+mbpId;
        //获取当前操作人
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName=principal.getUsername();

        if(redisTemplate.hasKey(mbpIdKey)){
            String oldUserName=redisTemplate.opsForValue().get(mbpIdKey)==null?"":redisTemplate.opsForValue().get(mbpIdKey).toString();
            if(!userName.equals(oldUserName)){
                return false;
            }
        }
        if(storage){
            int timeNum = Integer.parseInt(sysDictService.getByKey("MER_AUDIT_TIME").getSysValue());
            redisTemplate.opsForValue().set(mbpIdKey,userName,timeNum, TimeUnit.MINUTES);
        }
        return true;
    }

    /**
     * 商户审核跳转先判断锁定用户
     */
    @RequestMapping(value = "/merchantExamineDetailBefor")
    @ResponseBody
    public Object merchantExamineDetailBefor(@RequestParam("mbpId")String mbpId) {
        Map<String, Object> maps = new HashMap<>();
        if(checkUserAudit(mbpId,true)){
            maps.put("bols", true);
        }else{
            maps.put("msg", "该商户正在审核中,请重新选择!");
            maps.put("bols", false);
        }
        return maps;
    }

    /**
     * 商户审核详情 剥离其他查询详情，去除不必要的查询数据
     */
    @RequestMapping(value = "/deleteUserAuditKeyDetail")
    @ResponseBody
    public void deleteUserAuditKeyDetail(@RequestParam("mbpId")String mbpId) {
        deleteUserAuditKey(mbpId);
    }

    /**
     * 清除 商户审核锁定
     * @param mbpId
     * @return
     */
    private boolean deleteUserAuditKey(String mbpId){
        String mbpIdKey=MerchantBusinessProductAction.merAudit+mbpId;
        //获取当前操作人
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName=principal.getUsername();
        if(redisTemplate.hasKey(mbpIdKey)){
            redisTemplate.delete(mbpIdKey);
            return true;
        }
        return false;
    }

    /**
     * 获取所有的商户审核锁定key
     * 返回 进件项ID字符串， 逗号间隔
     * @return
     */
    private String getALLUserAuditKeys(){
        Set keys=redisTemplate.keys(MerchantBusinessProductAction.merAudit+"*");
        StringBuffer sb=new StringBuffer();
        for (Object key:keys) {
            String keyStr=String.valueOf(key);
            keyStr=keyStr.substring(MerchantBusinessProductAction.merAudit.length(),keyStr.length());
            sb.append(keyStr).append(",");
        }
        String str=sb.toString();
        if(!"".equals(str)){
            str=str.substring(0,str.length()-1);
        }
        return str;
    }

    private Map<String, Object> getDetail(String ids, HttpServletRequest request, int editState) {
        Map<String, Object> maps = new HashMap<>();

        // 商户业务产品
        MerchantBusinessProduct mbp = merchantBusinessProductService.selectByPrimaryKey(Long.valueOf(ids));
        if(StringUtils.isNotBlank(mbp.getAcqMerchantNo())){
            mbp.setSpecialMerchant("是");
        }else{
            mbp.setSpecialMerchant("否");
        }
        
        // 获取自动审件通道
        String autoMbpChannel = mbp.getAutoMbpChannel();
        if (org.springframework.util.StringUtils.hasLength(autoMbpChannel)) {
        	mbp.setAutoMbpChannelName(merchantBusinessProductService.getByCodeAndType(autoMbpChannel,1));
		}
        String orc = mbp.getOcr();
        if (org.springframework.util.StringUtils.hasLength(orc)) {
        	mbp.setOcrName(merchantBusinessProductService.getByCodeAndType(orc,2));
        }
        String face = mbp.getFace();
        if (org.springframework.util.StringUtils.hasLength(face)) {
        	mbp.setFaceName(merchantBusinessProductService.getByCodeAndType(face,3));
        }
        
        List<MerchantServiceRate> listmr = new ArrayList<MerchantServiceRate>();
        List<MerchantServiceQuota> listmsq = new ArrayList<MerchantServiceQuota>();
        List<MerchantRequireItem> listmri = new ArrayList<MerchantRequireItem>();
        List<MerchantRequireItem> listmris = new ArrayList<MerchantRequireItem>();
        List<AutoCheckResult> listacr = new ArrayList<AutoCheckResult>();
        List<MerchantInfo> mi = null;
        Page<TerminalInfo> tiPage = new Page<>();
        List<MerchantService> listms = null;
        MerchantServiceRate msr = new MerchantServiceRate();
        MerchantServiceQuota msq = new MerchantServiceQuota();
        List<ExaminationsLog> listel = new ArrayList<>();
        List<ExaminationsLog> exlistel = new ArrayList<>();// 复审记录
        List<ZqMerchantLog> zqMerLogs = null;
        List<TransInfoPreFreezeLog> preFreezeList = new ArrayList<TransInfoPreFreezeLog>();
        try {
            if (mbp.getMerchantNo() != null) {
                listacr = merchantBusinessProductService.selectAutoCheckResult(mbp.getMerchantNo(), mbp.getBpId());
                maps.put("listacr", listacr);
            }
            if (mbp.getAutoCheckTimes() > 0) {
                maps.put("isCheck", "是");
                ExaminationsLog elx = examinationsLogService.selectByitemNo(mbp.getId().toString());
                if (elx == null) {
                    if (mbp.getStatus().equals("1")) {
                        maps.put("checkStatus", "待一审");
                    } else if (mbp.getStatus().equals("2")) {
                        maps.put("checkStatus", "待平台审核");
                    } else if (mbp.getStatus().equals("3")) {
                        maps.put("checkStatus", "不通过");
                    } else if (mbp.getStatus().equals("4")) {
                        maps.put("checkStatus", "通过");
                    } else {
                        maps.put("checkStatus", "关闭");
                    }
                } else {
                    if (elx.getOpenStatus().equals("1")) {
                        maps.put("checkStatus", "通过");
                    } else {
                        maps.put("checkStatus", "不通过");
                    }
                }
            } else {
                maps.put("isCheck", "否");
                maps.put("checkStatus", "");
            }
            List<String> listStr = merchantBusinessProductService.querySerivceId(mbp.getBpId());
            if (mbp.getMerchantNo() != null) {
                terminalInfoService.selectAllInfoBymerNoAndBpId(mbp.getMerchantNo(), mbp.getBpId(), tiPage);
                mi = merchantInfoService.selectByMertId(mbp.getMerchantNo());
                if (mi.size() == 0) {
                    maps.put("msg", "没有该商户~");
                    maps.put("bols", false);
                    return maps;
                }
                if (mi.get(0).getBusinessType() != null) {
                    if ("1".equals(mi.get(0).getMerchantType())) {
                        mi.get(0).setSysName(
                                merchantInfoService.selectSysDictByKey(mi.get(0).getBusinessType(), "-1").getSysName());
                        if (mi.get(0).getIndustryType() != null) {
                            mi.get(0)
                                    .setTwoSysName(merchantInfoService
                                            .selectSysDictByKey(mi.get(0).getIndustryType(), mi.get(0).getBusinessType())
                                            .getSysName());
                        }
                    } else {
                        IndustryMcc industryMcc = merchantRequireItemService.selectIndustryMccByMcc(mi.get(0).getIndustryType());
                        if (industryMcc != null) {
                            mi.get(0).setSysName(industryMcc.getIndustryName());
                            mi.get(0).setTwoSysName(industryMcc.getIndustryName1());
                        }

                    }

                }
            }
            if (mbp.getMerchantNo() != null) {
                msr.setMerchantNo(mbp.getMerchantNo());
                msr.setUseable("1");
                for (String str : listStr) {
                    ServiceInfo sis = serviceProService.queryServiceInfo(Long.valueOf(str));
                    if (sis.getFixedRate() == 1) {
                        List<ServiceRate> srlist = serviceProService.getServiceAllRate(sis.getServiceId(),
                                mi.get(0).getOneAgentNo());
                        for (ServiceRate serviceRate : srlist) {
                            String oneRate = serviceProService.profitExpression(serviceRate);
                            MerchantServiceRate msrs = new MerchantServiceRate();
                            msrs.setServiceId(sis.getServiceId().toString());
                            msrs.setServiceName(sis.getServiceName());
                            msrs.setCardType(serviceRate.getCardType());
                            msrs.setHolidaysMark(serviceRate.getHolidaysMark());
                            msrs.setRateType(serviceRate.getRateType());
                            msrs.setCapping(serviceRate.getCapping());
                            msrs.setRate(serviceRate.getRate());
                            msrs.setSafeLine(serviceRate.getSafeLine());
                            msrs.setSingleNumAmount(serviceRate.getSingleNumAmount());
                            msrs.setLadder1Max(serviceRate.getLadder1Max());
                            msrs.setLadder1Rate(serviceRate.getLadder1Rate());
                            msrs.setLadder2Max(serviceRate.getLadder2Max());
                            msrs.setLadder2Rate(serviceRate.getLadder2Rate());
                            msrs.setLadder3Max(serviceRate.getLadder3Max());
                            msrs.setLadder3Rate(serviceRate.getLadder3Rate());
                            msrs.setLadder4Max(serviceRate.getLadder4Max());
                            msrs.setLadder4Rate(serviceRate.getLadder4Rate());
                            msrs.setOneRate(oneRate);
                            msrs.setMerRate(merchantServiceRateService.profitExpression(msrs));
                            String ss = String.valueOf(sis.getFixedRate());
                            msrs.setFixedMark(ss);
                            listmr.add(msrs);
                        }
                    } else {
                        List<MerchantServiceRate> listmrs = merchantServiceRateService
                                .selectByMertIdAndSerivceId(mbp.getMerchantNo(), str);
                        for (MerchantServiceRate merchantServiceRate : listmrs) {
                            // 查询服务管控表的费率
                            ServiceRate sr = new ServiceRate();
                            sr.setAgentNo(mi.get(0).getOneAgentNo());
                            sr.setServiceId(Long.valueOf(merchantServiceRate.getServiceId()));
                            sr.setCardType(merchantServiceRate.getCardType());
                            sr.setHolidaysMark(merchantServiceRate.getHolidaysMark());
                            sr.setRateType(merchantServiceRate.getRateType());

                            // 查询出一级代理商的费率
                            String oneRate = serviceProService.profitExpression(serviceProService.queryServiceRate(sr));
                            // 判断是否固定
                            if (sis.getFixedRate() == 0) {
                                merchantServiceRate
                                        .setMerRate(merchantServiceRateService.profitExpression(merchantServiceRate));
                                merchantServiceRate.setFixedMark("0");
                                merchantServiceRate.setOneRate(oneRate);
                            } else {
                                merchantServiceRate.setFixedMark("1");
                                merchantServiceRate.setOneRate(oneRate);
                                merchantServiceRate.setMerRate(oneRate);
                            }
                            listmr.add(merchantServiceRate);
                        }
                    }
                }

            }
            List<ServiceQuota> sqlist1 = new ArrayList<ServiceQuota>();
            if (mbp.getMerchantNo() != null) {
                msq.setMerchantNo(mbp.getMerchantNo());
                for (String str : listStr) {
                    ServiceInfo sis = serviceProService.queryServiceInfo(Long.valueOf(str));
                    if (sis.getFixedQuota() == 1) {
                        List<ServiceQuota> sqlist = serviceProService.getServiceAllQuota(sis.getServiceId(),
                                mi.get(0).getOneAgentNo());
                        for (ServiceQuota serviceQuota : sqlist) {
                            MerchantServiceQuota msqs = new MerchantServiceQuota();
                            msqs.setServiceId(sis.getServiceId().toString());
                            msqs.setServiceName(sis.getServiceName());
                            msqs.setCardType(serviceQuota.getCardType());
                            msqs.setHolidaysMark(serviceQuota.getHolidaysMark());
                            msqs.setSingleCountAmount(serviceQuota.getSingleCountAmount());
                            msqs.setSingleMinAmount(serviceQuota.getSingleMinAmount());
                            msqs.setSingleDayAmount(serviceQuota.getSingleDayAmount());
                            msqs.setSingleDaycardAmount(serviceQuota.getSingleDaycardAmount());
                            msqs.setSingleDaycardCount(serviceQuota.getSingleDaycardCount());
                            String ss = String.valueOf(sis.getFixedQuota());
                            msqs.setFixedMark(ss);
                            listmsq.add(msqs);
                        }
                    } else {
                        List<MerchantServiceQuota> listmsqs = merchantServiceQuotaService
                                .selectByMertIdAndServiceId(mbp.getMerchantNo(), str);
                        for (MerchantServiceQuota merchantServiceQuota : listmsqs) {
                            // 查询服务限额表的费率
                            ServiceQuota sq = new ServiceQuota();
                            sq.setAgentNo(mi.get(0).getOneAgentNo());
                            sq.setServiceId(Long.valueOf(merchantServiceQuota.getServiceId()));
                            sq.setCardType(merchantServiceQuota.getCardType());
                            sq.setHolidaysMark(merchantServiceQuota.getHolidaysMark());

                            // 查询出一级代理商的限额
                            sq = serviceProService.queryServiceQuota(sq);
                            sqlist1.add(sq);
                            // 判断是否固定
                            if (sis.getFixedQuota() == 0) {
                                merchantServiceQuota.setFixedMark("0");
                            } else {
                                merchantServiceQuota.setFixedMark("1");
                            }
                            listmsq.add(merchantServiceQuota);
                        }
                    }
                }

            }
            if (mbp.getMerchantNo() != null) {
                // listms =
                // merchantServiceProService.selectByMerId(mbp.getMerchantNo());
                listms = merchantServiceProService.selectByMerAndMbpId(mbp.getMerchantNo(), mbp.getBpId());
                zqMerLogs = zqMerInfoService.selectZqMerLogsByMerAndMbpId(mbp.getMerchantNo(), mbp.getBpId());
            }

            if (mbp.getMerchantNo() != null) {
                if ("1".equals(mbp.getMerchantType())) {//商户类型:1-个人
                    List<String> listStrs = businessRequireItemService.findByProduct(mbp.getBpId());
                    // 判断1图片、2文件
                    for (String string : listStrs) {
                        Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                        MerchantRequireItem mri = merchantRequireItemService.selectByMriId(string, mbp.getMerchantNo());
                        if (mri == null) {
                            continue;
                        }
                        mri.setMerBpId(mbp.getId().toString());
                        if (mri.getMriId().equals("3")) {
                            mri.setLogContent(mri.getContent());
                        }
                        if (mri.getExampleType() != null) {
                            if (mri.getExampleType().equals("1")) {
                                if (mri.getContent() != null) {
                                    String content = mri.getContent();
                                    String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content,
                                            expiresDate);
                                    mri.setContent(newContent);
                                    listmris.add(mri);
                                    continue;
                                }
                            }
                            if (mri.getExampleType().equals("2")) {
                                String content = mri.getContent();
                                String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content,
                                        expiresDate);
                                mri.setContent(newContent);
                            }
                        }
                        // 敏感信息屏蔽
                        if (0 == editState && mri.getMriId().equals("6")) {
                            mri.setContent(StringUtil.sensitiveInformationHandle(mri.getContent(), 1));
                        }
                        listmri.add(mri);
                    }
                } else {//商户类型:2-个体商户，3-企业商户',

                    List<MerchantRequireItem> mriLists = merchantRequireItemService.getByMer(mbp.getMerchantNo());
                    if (mriLists != null && !mriLists.isEmpty()) {
                        for (MerchantRequireItem mri : mriLists) {
                            Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                            if (mri == null) {
                                continue;
                            }
                            mri.setMerBpId(mbp.getId().toString());
                            if (mri.getMriId().equals("3")) {
                                mri.setLogContent(mri.getContent());
                            }
                            if (mri.getExampleType() != null) {
                                if (mri.getExampleType().equals("1")) {
                                    if (mri.getContent() != null) {
                                        String content = mri.getContent();
                                        String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content,
                                                expiresDate);
                                        mri.setContent(newContent);
                                        listmris.add(mri);
                                        continue;
                                    }
                                }
                                if (mri.getExampleType().equals("2")) {
                                    String content = mri.getContent();
                                    String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content,
                                            expiresDate);
                                    mri.setContent(newContent);
                                }
                            }
                            // 敏感信息屏蔽
                            if (0 == editState && mri.getMriId().equals("6")) {
                                mri.setContent(StringUtil.sensitiveInformationHandle(mri.getContent(), 1));
                            }


                            if ("37".equals(mri.getMriId())) {
                                IndustryMcc industryMcc = merchantRequireItemService.selectIndustryMccByMcc(mri.getContent());
                                if (industryMcc != null) {
                                    mri.setIndustryMcc(industryMcc);
                                    mri.setContent(industryMcc.getIndustryName1() + "-" + industryMcc.getIndustryName());
                                }


                            }


                            listmri.add(mri);
                        }
                    }


                }


            }
            String str1 = "";
            String str2 = "";
            for (MerchantRequireItem list : listmri) {
                if (list.getMriId().equals("2") || list.getMriId().equals("6")) {
                    str1 += list.getItemName() + ":" + list.getContent() + ",";
                }
                if (list.getMriId().equals("3") || list.getMriId().equals("4")) {
                    str2 += list.getItemName() + ":" + list.getContent() + ",";
                }
            }
            if (listmris.size() > 0) {
                for (MerchantRequireItem lists : listmris) {
                    if (lists.getMriId().equals("9")) {
                        lists.setRemark(str1);
                        continue;
                    }
                    if (lists.getMriId().equals("11")) {
                        lists.setRemark(str2);
                        continue;
                    }
                    lists.setRemark("");
                }
            }
            // 审核记录
            List<ExaminationsLog> exList = examinationsLogService.selectByMerchantId(mbp.getId().toString());
            for (ExaminationsLog elog : exList) {
                // 初审
                if (elog.getExamineType() == 1) {
                    listel.add(elog);
                } else if (elog.getExamineType() == 2) {
                    // 复审
                    exlistel.add(elog);
                }
            }
            // 2.2.5基本信息中 冻结金额要从账户接口中获得
            if (mbp.getMerchantNo() != null) {
                String merNo = mbp.getMerchantNo();
                try {
                    String str = ClientInterface.getMerchantAccountBalance(merNo);
                    JSONObject json1 = JSON.parseObject(str);
                    if ((boolean) json1.get("status")) {// 返回成功
                        AccountInfo ainfo = JSON.parseObject(str, AccountInfo.class);
                        mbp.setControlAmount(ainfo.getControlAmount());
                    } else {
                        mbp.setControlAmount(BigDecimal.ZERO);
                    }
                } catch (Exception e) {
                    log.error("商户预冻结金额查询报错 : " + merNo);
                    log.error("预冻结金额查询报错", e);
                    mbp.setControlAmount(null);
                }
            }
            // 2.2.5 最近预冻结操作
            preFreezeList = merchantPreFreezeLogService.selectByMerchantNo(mbp.getMerchantNo());

            // 最近修改结算卡信息
            List<MerchantRequireHistory> merchantRequireHistoryList = merchantRequireHistoryService
                    .getMerchantRequireHistoryByMriId(mbp.getMerchantNo(), "3");
            maps.put("agent", agentInfoService.selectByagentNo(mi.get(0).getOneAgentNo()));
            maps.put("merAgent", agentInfoService.selectByagentNo(mi.get(0).getAgentNo()));
            maps.put("mbp", mbp);
            MerchantInfo mer0 = mi.get(0);
            boolean isSale = (boolean) request.getSession().getAttribute(CommonConst.isSalesperson);
            if (isSale) {
                String tmp = mer0.getMobilephone();
                tmp = StrUtil.hide(tmp, 3, 7);
                mer0.setMobilephone(tmp);
                tmp = mer0.getIdCardNo();
                tmp = StrUtil.hide(tmp, tmp.length() - 4, tmp.length());
                mer0.setIdCardNo(tmp);
                tmp = mer0.getLawyer();
                tmp = StrUtil.hide(tmp, 1, 2);
                mer0.setLawyer(tmp);
            }
            // 敏感信息屏蔽
            if (0 == editState && mer0 != null) {
                mer0.setMobilephone(StringUtil.sensitiveInformationHandle(mer0.getMobilephone(), 0));
                mer0.setIdCardNo(StringUtil.sensitiveInformationHandle(mer0.getIdCardNo(), 1));
            }
            maps.put("mi", mer0);
            maps.put("listmr", listmr);
            maps.put("tiPage", tiPage);
            maps.put("listmsq", listmsq);
            maps.put("sqlist", sqlist1);
            maps.put("listel", listel);
            maps.put("exlistel", exlistel);
            maps.put("preFreezeList", preFreezeList);// 2.2.5 预冻结操作
            maps.put("merchantRequireHistoryList", merchantRequireHistoryList);
            if (isSale) {
                String tmp;
                for (MerchantRequireItem item : listmri) {
                    tmp = item.getMriId();
                    if ("2".equals(tmp)) {
                        tmp = item.getContent();
                        tmp = StrUtil.hide(tmp, 1, 2);
                        item.setContent(tmp);
                    }
                    if ("3".equals(tmp)) {
                        tmp = item.getContent();
                        tmp = StrUtil.hide(tmp, 0, tmp.length() - 4);
                        item.setContent(tmp);
                    } else if ("6".equals(tmp)) {
                        tmp = item.getContent();
                        tmp = StrUtil.hide(tmp, tmp.length() - 4, tmp.length());
                        item.setContent(tmp);
                    }
                }
            }
            maps.put("listmri", listmri);
            maps.put("listmris", listmris);
            maps.put("serviceMgr", listms);
            maps.put("zqMerLogs", zqMerLogs);
            // 黑名单操作日志
            List<BlackOperLog> blacklistDateList = riskRollService.selectBlackLogsByMerNo(mbp.getMerchantNo());
            if (null == blacklistDateList) {
                blacklistDateList = new ArrayList<BlackOperLog>();
            } else {
                for (BlackOperLog b : blacklistDateList) {
                    String type = b.getBlackType();
                    if ("1".equals(type)) {
                        b.setBlackType("商户黑名单");
                    } else if ("4".equals(type)) {
                        b.setBlackType("钱包出账黑名单");
                    } else {
                        b.setBlackType("");
                    }
                }
            }
            maps.put("blacklistDateList", blacklistDateList);
            maps.put("bols", true);
        } catch (Exception e) {
            log.error("详情查询报错", e);
            maps.put("msg", "详情查询报错");
            maps.put("bols", false);
        }
        return maps;
    }

    /**
     * 实名认证预警
     *
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectVerifiedWarning")
    @ResponseBody
    public Object selectVerifiedWarning() {
        Map<String, Object> maps = new HashMap<>();
        try {
            maps.put("authWarning", sysWarningService.getByType("1"));
            maps.put("hackWarning", sysWarningService.getByType("2"));
            maps.put("overtimeWarning",sysWarningService.getByType("5"));
            maps.put("enterpriseWarning",sysWarningService.getByType("6"));
            maps.put("toCheck",sysWarningService.getByType("8"));

            maps.put("status", true);
        } catch (Exception e) {
            log.error("实名认证预警查询报错", e);
            maps.put("msg", "实名认证预警查询报错");
            maps.put("status", false);
        }
        return maps;
    }

    /**
     * 实名认证预警修改
     *
     * @return
     */
    @RequestMapping(value = "/updateVerifiedWarning")
    @ResponseBody
    @SystemLog(description = "实名认证预警修改", operCode = "merchantBusinessProduct.updateVerifiedWarning")
    public Object updateVerifiedWarning(@RequestBody String param) {
        Map<String, Object> maps = new HashMap<>();
        JSONObject json = JSON.parseObject(param);
        Map<String, Object> authWarning = json.getObject("authWarning", Map.class);
        Map<String, Object> hackWarning = json.getObject("hackWarning", Map.class);
        Map<String, Object> overtimeWarning = json.getObject("overtimeWarning", Map.class);
        Map<String, Object> enterpriseWarning = json.getObject("enterpriseWarning", Map.class);
        Map<String, Object> toCheck = json.getObject("toCheck", Map.class);

        try {
            sysWarningService.updateSysWarning(authWarning);
            sysWarningService.updateSysWarning(hackWarning);
            sysWarningService.updateSysWarning(overtimeWarning);
            sysWarningService.updateSysWarning(enterpriseWarning);
            sysWarningService.updateSysWarning(toCheck);
            maps.put("status", true);
        } catch (Exception e) {
            log.error("实名认证预警修改报错", e);
            maps.put("msg", "实名认证预警修改报错");
            maps.put("status", false);
        }
        return maps;
    }

    // 审核意见查询
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectMriremark.do")
    @ResponseBody
    public Object selectMriremark(String ids) {
        AddRequireItem ari = new AddRequireItem();
        try {
            ari = addRequireItemService.selectByMeriId(ids);
        } catch (Exception e) {
            log.error("审核意见查询失败------", e);
        }
        return ari;
    }

    public boolean linkProduct(Map<String, String> params,UserLoginInfo principal) {
        try {
            String merchantNo = (String) params.get("merchantNo");
            String bpId = (String) params.get("bpId");
            String merbpId = (String) params.get("merbpId");

            MerchantInfo merInfo = merchantInfoService.selectByMerNo(merchantNo);
            params.put("agentNo", merInfo.getAgentNo());
            params.put("oneAgentNo", merInfo.getOneAgentNo());
            params.put("parentNode", merInfo.getParentNode());
            params.put("merbpId", merbpId);

            String agentNo = merInfo.getAgentNo();
            BusinessProductDefine bpIdInfo = businessProductDefineService.selectBybpId(bpId);
            String bpIdName = bpIdInfo.getBpName();
            Object linkBpId = bpIdInfo.getLinkProduct();// 关联的业务产品ID
            if (linkBpId == null || !StringUtils.isNotBlank(linkBpId.toString())) {
                log.info("=====>>关联的业务产品不开通,业务产品[{}]没有关联的业务产品", new Object[]{bpId});
                return false;
            }

            BusinessProductDefine linkBpIdInfo = businessProductDefineService
                    .selectBybpIdAndAgentNo(linkBpId.toString(), agentNo);// 该关联的业务产品是否被代理商代理。
            if (linkBpIdInfo == null) {
                log.info("=====>>关联的业务产品不开通,代理商编号[{}]没有代理关联业务产品,关联业务产品ID是[{}]",
                        new Object[]{agentNo, linkBpId.toString()});
                return false;
            }
            MerchantBusinessProduct merLinkBpIdInfo = merchantBusinessProductService.selectMerBusPro(merchantNo,
                    linkBpId.toString());// 校验商户未开通该关联业务产品；
            if (merLinkBpIdInfo != null) {
                log.info("=====>>关联的业务产品不开通,商户编号[{}]已开通关联业务产品ID,关联业务产品ID是[{}]",
                        new Object[]{merchantNo, linkBpId.toString()});
                return false;
            }
            SysDict proLimitMap = merchantInfoService.selectSysDict("products_limit");// 校验商户已开通的业务产品数量小于系统配置参数中商户能同时开通的业务产品数量；
            String proLimit = proLimitMap.getSysValue();
            Integer merProLimit = merchantBusinessProductService.selectMerProLimit(merchantNo);
            // String merProLimit = countMap.get("products_limit").toString();
            if (merProLimit.intValue() >= Integer.parseInt(proLimit)) {
                log.info("=====>>关联的业务产品不开通,商户编号[{}]的业务产品个数[{}]大于等于系统配置业务产品个数[{}]",
                        new Object[]{merchantNo, merProLimit.intValue(), Integer.parseInt(proLimit)});
                return false;
            }

            MerchantBusinessProduct barcodeMap = merchantBusinessProductService.findCollectionCodeMbp(merchantNo);// 校验商户未开通不依赖硬件的业务产品（一个商户只能拥有一个不依赖硬件的业务产品）；
            if (barcodeMap != null) {
                log.info("=====>>关联的业务产品不开通,商户编号[{}]已开通不不依赖硬件的业务产品", new Object[]{merchantNo});
                return false;
            }

            String hpId = null;
            if ("170".equals(linkBpId.toString())) {
                SysDict hpDict = sysDictService.getByKey("WKZF_HARDWARE_ID");
                hpId = hpDict.getSysValue();
            } else {
                List<HardwareProduct> list = hardwareProductService.findHardWareBybpId(linkBpId.toString());
                if (list.size() > 1) {
                    log.error("关联多个硬件..");
                    return false;
                }
                if (list.get(0) == null) {
                    log.error("无硬件..");
                    return false;
                }
                hpId = String.valueOf(list.get(0).getHpId());
            }
            String pos_type = "";
            String psamNo = "jh" + StringUtil.getStringRandom(8);
            params.put("posType", pos_type.toString());
            params.put("hpId", hpId);
            params.put("psamNo", psamNo);
            params.put("linkBpId", linkBpId.toString());
            log.info("关联的业务产品的状态为开始");
            boolean falg = merchantInfoService.linkProduct(params,principal);
            log.info("=====>>关联的业务产品的状态为" + falg);
            if (falg) {
                // 无卡自动审件通过
                // 调用商户进件接口

                String paramStr = "merchantNo=" + merchantNo + "&bpId=" + linkBpId.toString() + "&operator="
                        + principal.getId().toString();

                SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
                String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerAdd";

                HttpUtils.sendPost(accessUrl, paramStr, "UTF-8");
            }
            return falg;
        } catch (Exception e) {
            log.error("=====>>关联的业务产品异常", e);
            return false;
        }

    }

    // 审核
    @RequestMapping(value = "/examineRcored.do")
    @ResponseBody
    @SystemLog(description = "商户审核", operCode = "merchant.examineRcored")
    public Object examineRcored(@RequestBody String param) {
        JSONObject jsonMap = new JSONObject();
        try {

            JSONObject json = JSON.parseObject(param);

            // 判断是那种状态提交 1审核成功 2审核失败 3 审核成功下一条 4 审核失败下一条
            int val = json.getIntValue("val");
            // 商户进件ID
            String mbpId = json.getString("merbpId");

            //条件状态
            MbpStatusCondition mbpStatus=new MbpStatusCondition();
            mbpStatus.setVal(val);
            if("isReexamine".equals(json.get("isReexamine"))){
                mbpStatus.setReexamineStatus(true);
            }else{
                mbpStatus.setReexamineStatus(false);
            }

            // 商户信息
            MerchantInfo merchantInfo = json.getObject("info", MerchantInfo.class);

            if (!mbpStatus.getReexamineStatus()) {
                if(!checkUserAudit(mbpId,false)){
                    jsonMap.put("msg", "该商户正在审核中,请重新选择!");
                    jsonMap.put("result", false);
                    return jsonMap;
                }
            }
            //校验进件项表的状态
            MerchantBusinessProduct oldMbpInfo = merchantBusinessProductService.getMerchantBusinessProductInfo(Long.valueOf(mbpId));
            String status=oldMbpInfo.getStatus();

            // 过滤掉不能审核的单
            if ("0".equals(status) || "1".equals(status)) {
                jsonMap.put("result", false);
                jsonMap.put("msg", "该商户非待审核状态");
                return jsonMap;
            }
            if (!mbpStatus.getReexamineStatus()) {
                //审核成功的单不能再审核
                if("4".equals(status)){
                    jsonMap.put("result", false);
                    jsonMap.put("msg", "该商户非待审核状态");
                    return jsonMap;
                }
                //审核失败,人工审核的单不能再审核
                if ("3".equals(status)&&(!"-1".equals(oldMbpInfo.getAuditorId())&&!"-2".equals(oldMbpInfo.getAuditorId()))) {
                    jsonMap.put("result", false);
                    jsonMap.put("msg", "该商户非待审核状态");
                    return jsonMap;
                }
            }

            //合并进件项审核处理结果
            List<MerchantRequireItem> mri = JSON.parseArray(json.getJSONArray("listMri").toJSONString(), MerchantRequireItem.class);// 文字
            List<MerchantRequireItem> mris = JSON.parseArray(json.getJSONArray("listMris").toJSONString(), MerchantRequireItem.class);// 图片
            for (MerchantRequireItem merchantRequireItem : mris) {
                mri.add(merchantRequireItem);
            }

            // 意见
            String opinion = json.getString("opinion");
            if (opinion == null || opinion.equals("")) {
                opinion = "审核成功";
            }

            MerchantInfo oldMerInfo = merchantInfoService.selectByMerNo(merchantInfo.getMerchantNo());
            // 获取当前登录的人
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // 审核记录
            ExaminationsLog el = new ExaminationsLog();
            el.setCreateTime(new Date());
            el.setExaminationOpinions(opinion);
            el.setItemNo(mbpId);
            el.setBpId(oldMbpInfo.getBpId());
            el.setExamineType(1);
            el.setMerchantType(oldMerInfo.getMerchantType());
            el.setOperator(principal.getId().toString());

            //商户银行卡表
            MerchantCardInfo mci = new MerchantCardInfo();
            mci.setMerchantNo(merchantInfo.getMerchantNo());
            mci.setCardType("1");
            mci.setStatus("1");
            mci.setDefQuickPay("2");
            mci.setQuickPay("2");
            mci.setCreateTime(new Date());

            //银行卡状态
            boolean bankCardStatus=false;
            //开户行全称，开户地址
            boolean addressStatus=false;

            //APP计数3次直接只走人工
            boolean appStatus=false;

            if(mri!=null&&mri.size()>0){
                for(MerchantRequireItem item:mri){

                    if("1".equals(item.getMriId())){// 账户类型
                        if ("对公".equals(item.getContent())) {
                            mci.setAccountType("1");
                        } else {
                            mci.setAccountType("2");
                        }

                    }else if("2".equals(item.getMriId())){ // 开户名
                        if("不通过".equals(item.getaStatus())){
                            appStatus=true;
                        }
                        mci.setAccountName(item.getContent());

                    }else if("3".equals(item.getMriId())){//银行卡
                        if("不通过".equals(item.getaStatus())){
                            bankCardStatus=true;
                            appStatus=true;
                        }
                        mci.setAccountNo(item.getContent());
                        mci.setLogAccountNo(item.getLogContent());

                    }else if("4".equals(item.getMriId())){//开户行全称
                        if("不通过".equals(item.getaStatus())){
                            addressStatus=true;
                        }
                        mci.setBankName(item.getContent());

                    }else if("5".equals(item.getMriId())){//联行行号
                        mci.setCnapsNo(item.getContent());

                    }else if("6".equals(item.getMriId())){//开户身份证
                        if("不通过".equals(item.getaStatus())){
                            appStatus=true;
                        }

                    }else if("7".equals(item.getMriId())){//经营地址拆分
                        String content = item.getContent();
                        String[] str = content.split("-");
                        if (str.length <= 3) {
                            jsonMap.put("result", false);
                            jsonMap.put("msg", "经营地址格式不正确");
                            return jsonMap;
                        }
                        merchantInfo.setProvince(str[0]);
                        merchantInfo.setCity(str[1]);
                        merchantInfo.setDistrict(str[2]);
                        merchantInfo.setAddress(str[0] + str[1] + str[2] + str[3]);

                    }else if("15".equals(item.getMriId())){//开户地址
                        if("不通过".equals(item.getaStatus())){
                            addressStatus=true;
                        }
                        String content = item.getContent();
                        String[] str = content.split("-");
                        if (str.length < 3) {
                            jsonMap.put("result", false);
                            jsonMap.put("msg", "开户行地址格式不正确");
                            return jsonMap;
                        }
                        mci.setAccountProvince(str[0]);
                        mci.setAccountCity(str[1]);
                        mci.setAccountDistrict(str[2]);
                    }
                }
            }
            mbpStatus.setBankCardStatus(bankCardStatus);
            mbpStatus.setAddressStatus(addressStatus);
            mbpStatus.setAppStatus(appStatus);

            //更新审核数据
            int num=merchantBusinessProductService.updateMbpExamineInfo(mbpStatus,oldMbpInfo,mri,merchantInfo,mci,el);

            if(num>0){
                // 审核过后清除进件ID锁定,复审退件跳过
                if (!mbpStatus.getReexamineStatus()) {
                    deleteUserAuditKey(mbpId);
                }

                //开启异步线程处理后续数据
                asynchronousImplement(merchantInfo,val,oldMbpInfo,principal,opinion);

                // 跳下一条
                if (val == 3 || val == 4) {
                    MerchantBusinessProduct conditionInfo=new MerchantBusinessProduct();
                    conditionInfo.setDayNum(bossSysConfigService.selectValueByKey("MER_AUDIT_DAY"));
                    conditionInfo.setAuditorId(principal.getId().toString());
                    conditionInfo.setStatus("2");
                    //获取所有锁定的用户ID
                    conditionInfo.setAuditIds(getALLUserAuditKeys());

                    List<MerchantBusinessProduct> list = merchantBusinessProductService.selectNextMbpInfo(conditionInfo);
                    if (list.size() < 1) {
                        conditionInfo.setAuditorId(null);
                        List<MerchantBusinessProduct> list12 = merchantBusinessProductService.selectNextMbpInfo(conditionInfo);
                        if (list12.size() < 1) {
                            jsonMap.put("msg", "没有可处理的商户");
                            jsonMap.put("result", true);
                            jsonMap.put("infoState", true);
                        } else {
                            jsonMap.put("result", true);
                            jsonMap.put("msg", "操作成功");
                            jsonMap.put("infos", list12.get(0).getId().toString());
                            if (!mbpStatus.getReexamineStatus()) {
                                checkUserAudit(list12.get(0).getId().toString(),true);
                            }
                        }
                    } else {
                        jsonMap.put("result", true);
                        jsonMap.put("msg", "操作成功");
                        jsonMap.put("infos", list.get(0).getId().toString());
                        if (!mbpStatus.getReexamineStatus()) {
                            checkUserAudit(list.get(0).getId().toString(),true);
                        }

                    }
                } else {
                    jsonMap.put("result", true);
                    jsonMap.put("msg", "操作成功");
                }
            }else{
                jsonMap.put("result", false);
                jsonMap.put("msg", "操作失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("审核异常!", e);
            jsonMap.put("msg", "审核信息异常");
            jsonMap.put("result", false);
        }
        return jsonMap;
    }

    private void asynchronousImplement(final MerchantInfo merchantInfo,final int val,final MerchantBusinessProduct oldMbpInfo,final UserLoginInfo principal,final String opinion){

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if (val == 4 || val == 2) {//审核失败
                    // 审核失败发送短信提醒
                    String teamName =merchantBusinessProductService.getTeamName(merchantInfo.getTeamId());
                    if (StringUtils.isNotBlank(teamName)) {
                        Sms.sendMsg(merchantInfo.getMobilephone(), "感谢您使用" + teamName.trim() + "，您提交的商户" + merchantInfo.getMerchantName() + "信息审核失败，失败原因为" + opinion + "");
                    }
                    //调用神策
                    sensorsService.checkUser(merchantInfo, oldMbpInfo.getBpId(), principal.getUsername(), opinion, false);
                }
                if (val == 1 || val == 3) { // 审核成功发送短信提醒（审核成功不再发送短信，由core统一来发）
                    //调用core进件同步接口
                    zfMerAddInterface(merchantInfo.getMerchantNo(),oldMbpInfo.getBpId(),principal.getId().toString(), oldMbpInfo.getId().toString());

                    //开通无卡服务
                    Map<String, String> map = new HashMap<>();
                    map.put("merchantNo", merchantInfo.getMerchantNo());
                    map.put("bpId", oldMbpInfo.getBpId());
                    map.put("merbpId", oldMbpInfo.getId().toString());

                    map.put("merbpId", oldMbpInfo.getId().toString());

                    linkProduct(map,principal);


                    //校验商户是否有过复审退件再次进件
                    if ("3".equals(oldMbpInfo.getReexamineStatus())) {
                        //审核通过的时候，需要把merchant_business_product对应记录的 reexamine_status置为未复审， reexamine_tip_end_time清空
                        List<MerchantBusinessProduct> mbpList = merchantBusinessProductService.getByMer(oldMbpInfo.getMerchantNo());
                        for (MerchantBusinessProduct mbp : mbpList) {
                            merchantBusinessProductService.resetReexamineBymbpId(mbp.getId(), "0", null);
                        }
                    }else{

                        //进件审核通过时推送“已绑定机具”信息到会员系统
                        //进件审核通过时推送“已绑定身份证”信息到会员系统
                        String vipScoreUrl=sysDictService.getValueByKey("VIP_SCORE_URL");
                        String teamId=merchantInfo.getTeamId();
                        String key=bossSysConfigService.selectValueByKey("VIP_SCORE_SIGN_KEY_"+teamId);
                        String businessNo=bossSysConfigService.selectValueByKey("VIP_SCORE_BUS_NO_"+teamId);
                        String resultMsg=ClientInterface.handlerVipTask(null,vipScoreUrl,merchantInfo,businessNo,teamId,key);

                    }

                    //调用神策
                    sensorsService.checkUser(merchantInfo, oldMbpInfo.getBpId(), principal.getUsername(), opinion, true);

                }
            }
        });
    }

    //调用商户进件接口
    private void zfMerAddInterface(String merchantNo,String bpId,String principalId,String mbpId){
        // 调用商户进件接口
        String autoLinkBpId = "0";
        String params = "merchantNo=" + merchantNo + "&bpId=" + bpId + "&operator="
                + principalId + "&autoLinkBpId=" + autoLinkBpId;

        SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
        String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerAdd";

        HttpUtils.sendPost(accessUrl, params, "UTF-8");

    }

    // 图片修改
    @RequestMapping(value = "/updateItemImg.do")
    @ResponseBody
    public Object updateItemImg(@RequestBody String param) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject json = JSON.parseObject(param);
        try {
            Long id = json.getLong("id");
            String img = json.getString("img");
            MerchantRequireItem mri = new MerchantRequireItem();
            mri.setId(id);
            mri.setContent(img);
            int num = 0;
            num = merchantRequireItemService.updateByMbpId(mri);
            if (num > 0) {
                Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, img, expiresDate);
                jsonMap.put("result", true);
                jsonMap.put("datas", newContent);
            } else {
                jsonMap.put("result", false);
            }
        } catch (Exception ex) {
            log.error("商户修改出错------", ex);
            jsonMap.put("result", false);
        }

        return jsonMap;
    }

    // 商户信息修改
    @RequestMapping(value = "/updateMerchantInfo.do")
    @ResponseBody
    @SystemLog(description = "商户信息修改", operCode = "merchant.update")
    public Object updateMerchantInfo(@RequestBody String param) {

        merchantBusinessProductService.initSysConfigByKey();

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("result", false);
        JSONObject json = JSON.parseObject(param);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        try {
            UserModel userModel = new UserModel();

            // 商户业务产品
            MerchantBusinessProduct merchantBusinessProduct = json.getObject("mbp", MerchantBusinessProduct.class);
            // 商户信息
            MerchantInfo merchantInfo = json.getObject("info", MerchantInfo.class);
            // 审核明细表
            List<MerchantRequireItem> mris = JSON.parseArray(json.getJSONArray("listMri").toJSONString(),
                    MerchantRequireItem.class);

            // 商户费率
            List<MerchantServiceRate> msr = JSON.parseArray(json.getJSONArray("listMsr").toJSONString(),
                    MerchantServiceRate.class);

            // 商户限额
            List<MerchantServiceQuota> msq = JSON.parseArray(json.getJSONArray("listMsq").toJSONString(),
                    MerchantServiceQuota.class);
            jsonMap = merchantBusinessProductService.updateByItemAndMbpId(merchantBusinessProduct, merchantInfo, mris,
                    msr, msq, principal.getId(), userModel);
            if ((boolean) jsonMap.get("result")) {
                if (jsonMap.get("mobile") != null) {
                    if (jsonMap.get("mobile").toString().equals("yes")) {
                        Sms.sendMsg(merchantInfo.getMobilephone(), "您的手机号修改成功,登录密码为初始密码:"
                                + sysDictService.selectRestPwd().getSysValue() + ",请重新去设置您的支付密码,移动支付，快乐生活！");
                    }
                }
            } else {
                return jsonMap;
            }
            // 商户业务产品服务
            List<String> channelList = new ArrayList<>();
            List<MerchantService> merServices = JSON.parseArray(json.getJSONArray("serviceData").toJSONString(),
                    MerchantService.class);
            String saveMerServiceRes = null;
            for (MerchantService merService : merServices) {
                // 校验商户服务通道是否发生了变化
                MerchantService oldService = merchantServiceProService.getMerchantServiceByID(merService.getId());
                String newChannelCode = merService.getChannelCode();
                if (newChannelCode != null && !newChannelCode.equals(oldService.getChannelCode())) {
                    if(!channelList.contains(newChannelCode)){
                        channelList.add(newChannelCode);
                    }
                }
                saveMerServiceRes = saveZqMerService(merService);
                if (saveMerServiceRes != null) {
                    break;
                }
            }
            if (saveMerServiceRes != null) {
                jsonMap.put("msg", saveMerServiceRes);
                return jsonMap;
            }

            // 调用银盛商户同步接口
            // 如果商户已报备银盛，需要调用商户修改接口
            String merchantNo = merchantInfo.getMerchantNo();
            String mbpId = String.valueOf(merchantBusinessProduct.getId());
            String bpId = merchantBusinessProduct.getBpId();
            String acqEnname = "YS_ZQ";
            Result result = merchantBusinessProductService.ysUpdateMer(merchantNo, mbpId, bpId, acqEnname);
            if (!result.isStatus()) {
                jsonMap.put("msg", result.getMsg());
            }

            // 调用商户修改接口
            String paramStr = null;
            SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
            String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
            Map<String, Object> marMap = new HashMap<String, Object>();

            marMap.put("merchantNo", merchantBusinessProduct.getMerchantNo());
            marMap.put("bpId", merchantBusinessProduct.getBpId());
            marMap.put("operator", principal.getId().toString());
            marMap.put("changeSettleCard",
                    jsonMap.get("changeSettleCard") == null ? "0" : String.valueOf(jsonMap.get("changeSettleCard")));
            marMap.put("channelCode", channelList);
            paramStr = JSON.toJSONString(marMap);
            log.info("ZFZQ_ACCESS_URL:" + accessUrl + "\n paramStr:" + paramStr);
            new ClientInterface(accessUrl, null).postRequestBody(paramStr);

            //修改结算卡调出款
            if(jsonMap.get("changeSettleCard") != null){
                List<Map<String, Object>> transferList = transInfoService.queryResetTransfer(merchantBusinessProduct.getMerchantNo());
                if(transferList != null && transferList.size() >0){
                    for(int i = 0; i < transferList.size(); i ++){
                        ClientInterface.resetTransfer(transferList.get(i).get("id").toString(),"transfer_modify_settle_card");
                    }
                }
            }

            // jsonMap.put("msg", "修改成功");
            // jsonMap.put("result", true);

            // 发送神策
            //String project=bEHAVIOUR_SERVER_URL.substring(bEHAVIOUR_SERVER_URL.indexOf("project=")+8, bEHAVIOUR_SERVER_URL.length());

            Map<String, Object> oneMap = new HashMap<>();
            Map<String, String> twoMap = new HashMap<>();

            oneMap.put("distinct_id", merchantBusinessProductService.getUserIdByMerchantInfo(merchantInfo.getMerchantNo()));

            oneMap.put("time", new Date().getTime());
            oneMap.put("type", Constants.BEHAVIOUR_TYPE_PROFILE_SET);
            oneMap.put("project", Constants.BEHAVIOUR_SERVER_PROJECT);
            oneMap.put("properties", twoMap);

            twoMap.put("$city", merchantInfo.getCity());
            twoMap.put("$province", merchantInfo.getProvince());


            //merchant_push_device
            twoMap.put("device_id", merchantBusinessProductService.getDeviceIdByPhone(merchantInfo.getMobilephone()));

            twoMap.put("merchant_no", merchantInfo.getMerchantNo());

            //根据身份证号分析
            String idCardNo = merchantInfo.getIdCardNo();
            String sex = "";
            // 获取性别
            String id17 = idCardNo.substring(16, 17);
            if (Integer.parseInt(id17) % 2 != 0) {
                sex = "男";
            } else {
                sex = "女";
            }
            String birthday = idCardNo.substring(6, 14);
            Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
            DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
            twoMap.put("gender", sex);
            twoMap.put("age", df.format(birthdate));
            twoMap.put("bank_name", userModel.getBankName());
            twoMap.put("registration_time", DateUtil.getLongFormatDate(merchantInfo.getCreateTime()));
            twoMap.put("agent_no", merchantInfo.getAgentNo());

            //0：商户关闭；1：正常；2 冻结'
            twoMap.put("merchant_status", merchantInfo.getStatus());

            //(0:正常,1:为超级推，2：代理商推荐的商户，3人人代理推荐)

            twoMap.put("recmand_source", merchantInfo.getRecommendedSource());

            twoMap.put("sales", merchantInfo.getOneSaleName());
            twoMap.put("first_level_agent_no", merchantInfo.getOneAgentNo());


            twoMap.put("sign_source", merchantInfo.getRegisterSource());


            List<MerchantBusinessProduct> merchantBusinessProductList = merchantBusinessProductService.selectByMerchantNoAll(merchantNo);
            int j = 1;
            for (int i = 0; i < merchantBusinessProductList.size(); i++) {
                twoMap.put("business_product1", merchantBusinessProductList.get(i).getBpName());

                Page<TerminalInfo> tiPage = new Page<TerminalInfo>();
                List<TerminalInfo> terminalInfos = terminalInfoService.selectAllInfoBymerNoAndBpId(merchantBusinessProductList.get(i).getMerchantNo(), merchantBusinessProductList.get(i).getBpId(), tiPage);
                for (TerminalInfo terminalInfo : terminalInfos) {
                    twoMap.put("product_type" + j, terminalInfo.getTypeName());
                    j++;
                }
            }
            twoMap.put("orgnize_id", merchantInfo.getTeamId());
            twoMap.put("user_type", "商户");

            merchantBusinessProductService.SendHttpSc(oneMap);

        } catch (Exception ex) {
            log.error("商户修改出错------", ex);
            jsonMap.put("result", false);
            String str = ex.getMessage();
            if (ex.getMessage() == null) {
                jsonMap.put("msg", "修改失败");
                return jsonMap;
            }
            if (str.contains("\r\n") || str.contains("\n"))
                jsonMap.put("msg", "修改信息异常");
            else
                jsonMap.put("msg", str);
        }
        return jsonMap;
    }

    // 保存商户交易模式和通道
    public String saveZqMerService(MerchantService merService) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        try {
            Long merServiceId = merService.getId();
            String tradeType = merService.getTradeType();
            String channelCode = merService.getChannelCode();
            if (merServiceId == null) {
                return merService.getServiceName() + " 商户服务不存在";
            }
            if (StringUtils.isBlank(tradeType)) {
                return merService.getServiceName() + " 商户交易模式不能为空";
            }
            if ("1".equals(tradeType) && StringUtils.isBlank(channelCode)) {
                return merService.getServiceName() + " 商户直清通道不能为空";
            }
            // 设置商户服务的交易模式
            merchantServiceProService.updateTradeTypeByPrimaryKey(merServiceId, tradeType, channelCode);
            // 根据服务Id查询该商户业务产品对象
            MerchantBusinessProduct merBusPro = merchantBusinessProductService.selectByServiceId(merServiceId);

            // 之前自己实现的直清业务功能，先直接调用高伟那边的接口，在那边实现具体的逻辑判断和业务场景

            // 修改服务的交易模式之后，更新对应商户进件对象的交易模式，只要有一条服务是直清，那么该进件就是直清
            merchantBusinessProductService.updateTradeTypeByServices(merBusPro);

        } catch (Exception e) {
            log.error(merService.getServiceName() + " 保存商户交易模式和通道------", e);
            return merService.getServiceName() + " 保存失败，请稍后再试";
        }
        return null;
    }

    // 商户服务表修改
    @RequestMapping(value = "updateMerchantService.do")
    @ResponseBody
    public Object updateMerchantService(@RequestBody String param) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject json = JSON.parseObject(param);
        MerchantBusinessProduct mbp = json.getObject("mbp", MerchantBusinessProduct.class);
        try {
            int i = 0;
            Long id = json.getLong("id");
            String status = json.getString("status");
            MerchantService msi = new MerchantService();
            msi.setId(id);
            msi.setStatus(status);
            i = merchantServiceProService.updateByPrimaryKey(msi);
            if (i > 0) {
                jsonMap.put("result", true);
                if (mbp.getMerchantNo() != null) {
                    jsonMap.put("serviceMgr", merchantServiceProService.selectByMerId(mbp.getMerchantNo()));
                }
            } else {
                jsonMap.put("result", false);

            }
        } catch (Exception e) {
            log.error("商户修改出错------", e);
            jsonMap.put("result", false);
        }
        // 商户服务

        return jsonMap;
    }

    // 更改预冻结金额之前，需判断该商户是否有未出款的交易
    @RequestMapping(value = "judgeIsExistTrade.do")
    @ResponseBody
    public Object judgeIsExistTrade(@RequestBody String param) {

        log.info("更改预冻结金额，判断该商户是否有未出款的交易参数： " + param);
        Map<String, Object> maps = new HashMap<String, Object>();
        JSONObject json = JSON.parseObject(param);
        String merchantNo = json.getString("merchantNo");

        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal controlAmount = BigDecimal.ZERO;

        BigDecimal compareAmount = BigDecimal.ZERO;

        if (StringUtils.isNotBlank(merchantNo)) {
            String str = "";
            try {
                str = ClientInterface.getMerchantAccountBalance(merchantNo);
            } catch (Exception e) {
                log.error("账户查询商户金额接口异常：" + e);
                maps.put("bols", false);
                maps.put("msg", "账户查询商户金额接口异常!");
                return maps;
            }
            JSONObject json1 = JSON.parseObject(str);
            if ((boolean) json1.get("status")) {// 返回成功
                AccountInfo ainfo = JSON.parseObject(str, AccountInfo.class);
                balance = balance.add(ainfo.getBalance());
                controlAmount = controlAmount.add(ainfo.getControlAmount());
                compareAmount = balance.subtract(controlAmount);
                if (compareAmount.compareTo(BigDecimal.ZERO) > 0) {
                    maps.put("bols", true);
                    return maps;
                } else {
                    maps.put("bols", false);
                    return maps;
                }
            } else {
                maps.put("bols", false);
                maps.put("msg", json1.get("msg"));
                return maps;
            }
        }
        return maps;
    }

    /**
     * 2.2.5 预冻结金额修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "preFrozen.do")
    @ResponseBody
    public Object preFrozen(@RequestBody String param) {

        log.info("预冻结金额修改参数： " + param);
        Map<String, Object> maps = new HashMap<String, Object>();
        JSONObject json = JSON.parseObject(param);
        String preFrozenMoney = json.getString("preFrozenAmount");
        if (StringUtils.isNotBlank(preFrozenMoney)) {
            try {
                String merchantNo = json.getString("merchantNo");
                String preFrozenNote = json.getString("preFrozenNote");
                BigDecimal preFrozenAmount = new BigDecimal(preFrozenMoney);
                // 插入预冻结日志
                final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                TransInfoPreFreezeLog record = new TransInfoPreFreezeLog();
                record.setOperId(principal.getId().toString());
                record.setOperName(principal.getRealName());
                record.setPreFreezeNote(preFrozenNote);
                record.setPreFreezeAmount(preFrozenAmount);
                record.setMerchantNo(merchantNo);
                record.setOperTime(new Date());
                merchantPreFreezeLogService.insertLogAndUpdateMerchantInfoAmount(record);

            } catch (Exception e) {
                log.error("预冻结金额修改出错------", e);
                maps.put("bols", false);
                maps.put("msg", "预冻结金额修改异常");
            }
        } else {
            maps.put("bols", false);
            maps.put("msg", "预冻结金额不能为空！");
            return maps;
        }
        maps.put("bols", true);
        maps.put("msg", "预冻结金额修改成功");
        return maps;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getMerchantFew")
    @ResponseBody
    public Object getMerchantFew(@RequestParam(value = "item", required = false) String item) throws Exception {
        List<MerchantInfo> list = null;
        try {
            list = merchantInfoService.getMerchantFew(item);
        } catch (Exception e) {
            log.error("查询少量商户失败！", e);
        }
        return list;
    }

    // 批量转自动审件
    @RequestMapping(value = "autoCheckBatch.do")
    @ResponseBody
    @SystemLog(description = "批量转自动审件", operCode = "merchant.autoCheckBatch")
    public Object autoCheckBatch(@RequestBody String param) {
        Map<String, Object> maps = new HashMap<String, Object>();
        try {
            List<String> idList = JSONObject.parseArray(param, String.class);
            log.info("批量转自动审件参数： " + idList.toString());
            if (idList.size() <= 0) {
                maps.put("bols", false);
                maps.put("msg", "选择数据为空");
                return maps;
            }
            List<MerchantBusinessProduct> mbpLists = merchantBusinessProductService.selectMerBusProByIds(idList);
            if (mbpLists == null || mbpLists.isEmpty()) {
                maps.put("bols", false);
                maps.put("msg", "选择数据为空");
                return maps;
            }
            int errorCount = 0;
            int successCount = 0;
            String merNoes = "";
            Iterator<MerchantBusinessProduct> it = mbpLists.iterator();
            while (it.hasNext()) {
                MerchantBusinessProduct mbp = it.next();
                final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();

                List<MerchantRequireItem> listMri = merchantRequireItemService.getByMer(mbp.getMerchantNo());
                String accountNo = "";
                String name = "";
                String card = "";
                for (MerchantRequireItem m : listMri) {
                    if (m.getMriId().equals("3")) {
                        accountNo = m.getContent();
                    }
                    if (m.getMriId().equals("2")) {
                        name = m.getContent();
                    }
                    if (m.getMriId().equals("6")) {
                        card = m.getContent();
                    }
                }
                PyIdentification pyp = new PyIdentification();
                pyp.setCreatePerson(principal.getId().toString());
                pyp.setBySystem(1);
                pyp.setAccountNo(accountNo);
                pyp.setIdCard(card);
                pyp.setIdentName(name);
                if (StringUtils.isEmpty(accountNo) || StringUtils.isEmpty(name) || StringUtils.isEmpty(card)) {
                    errorCount++;
                    merNoes += mbp.getMerchantNo() + ",";
                    it.remove();
                } else {

                    PyIdentification ppp = pyIdentificationService.queryByCheckInfo(name, card, accountNo);
                    if (ppp == null) {// 去走检查
                        Map<String, String> strMap = openPlatformService.doAuthen(accountNo, name, card, null);
                        String errCode = strMap.get("errCode");
                        String errMsg_ = strMap.get("errMsg");
                        boolean flag = "00".equalsIgnoreCase(errCode);
                        // log.info("身份证验证结果：是否成功:{};开户名:{};银行卡号:{};身份证:{};验证结果:{};错误信息:{};异常信息:{};",new
                        // Object[]
                        // {flag,account_name,account_no,id_card_no,errCode,errMsg_,exceptionMsg});
                        // 如果身份验证失败，刚不再自动审核，按旧有的注册流程走。

                        pyp.setErrorMsg(errMsg_);
                        if (!flag) {
                            log.info("身份证验证失败,开户名、身份证、银行卡号不匹配");
                            errorCount++;
                            merNoes += mbp.getMerchantNo() + ",";
                            it.remove();
                        } else {
                            // 修改状态为已转自动审件 5
                            merchantBusinessProductService.updateStatusById("5", mbp.getId() + "");
                            successCount++;
                        }
                    } else {// 查看是否通过
                        if (ppp.getIdentiStatus() == 1) {
                            // 修改状态为已转自动审件 5
                            merchantBusinessProductService.updateStatusById("5", mbp.getId() + "");
                            successCount++;
                        } else {// 去走检查
                            Map<String, String> strMap = openPlatformService.doAuthen(accountNo, name, card, null);
                            String errCode = strMap.get("errCode");
                            String errMsg_ = strMap.get("errMsg");
                            boolean flag = "00".equalsIgnoreCase(errCode);
                            // log.info("身份证验证结果：是否成功:{};开户名:{};银行卡号:{};身份证:{};验证结果:{};错误信息:{};异常信息:{};",new
                            // Object[]
                            // {flag,account_name,account_no,id_card_no,errCode,errMsg_,exceptionMsg});
                            // 如果身份验证失败，刚不再自动审核，按旧有的注册流程走。
                            pyp.setErrorMsg(errMsg_);
                            if (!flag) {
                                log.info("身份证验证失败,开户名、身份证、银行卡号不匹配");
                                errorCount++;
                                merNoes += mbp.getMerchantNo() + ",";
                                it.remove();
                            } else {
                                // 修改状态为已转自动审件 5
                                merchantBusinessProductService.updateStatusById("5", mbp.getId() + "");
                                successCount++;
                            }
                        }
                    }
                }
            }
            merNoes = merNoes.length() > 0 ? merNoes.substring(0, merNoes.length() - 1) : merNoes;
            final String coreUrl = merchantBusinessProductService.getCoreUrl() + Constants.MER_AUTO_CHECK_URL;
            for (final MerchantBusinessProduct mbp : mbpLists) {
                try {
                    new Thread(new Runnable() {// 给core发请求，单独起一个线程
                        @Override
                        public void run() {
                            String res = ClientInterface.toAutoCheck(coreUrl, mbp.getMerchantNo(), mbp.getBpId(),
                                    mbp.getContent());
                            log.info("\n批量转自动审件(id=" + mbp.getId() + ")返回： " + res);
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("\n批量转自动审件(id=" + mbp.getId() + ")返回：请求失败 ");
                }
            }
            // 暂时不做，以后要做这里写方法
            maps.put("bols", true);
            maps.put("msg", "批量转自动审件成功,成功" + successCount + "条,失败" + errorCount + "条,失败商户号" + merNoes);
        } catch (Exception e) {
            log.error("批量转自动审件异常", e);
            maps.put("bols", false);
            maps.put("msg", "批量转自动审件异常");
        }
        return maps;
    }

    // 商户复审查询
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/examineQueryByParam.do")
    @ResponseBody
    public Object examineQueryByParam(@RequestParam("info") String param,
                                      @ModelAttribute("page") Page<MerchantBusinessProduct> page, HttpServletRequest request) {
        Map<String, Object> maps = (Map<String, Object>) this.selectByParam(param, page);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String redisKey = getIpAddr(request) + "secret" + principal.getId().toString();
        try {
            log.info("缓存复审查询条件：{}", redisKey);
            redisService.insertString(redisKey, param);
        } catch (Exception e) {
            log.info("缓存查询条件失败");
        }
        return maps;
    }

    // 商户复审统计
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/examineTotalByParam.do")
    @ResponseBody
    public Object examineTotalByParam(@RequestParam("info") String param) {
        Map<String, Object> maps = new HashMap<>();
        SelectParams selectParams = JSON.parseObject(param, SelectParams.class);
        if (StringUtils.isNotBlank(selectParams.getAgentName())) {
            AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
            if (ais != null) {
                if (selectParams.getAgentNode().equals("0")) {
                    selectParams.setAgentNode(ais.getAgentNode());
                } else {
                    selectParams.setAgentNode(ais.getAgentNode() + "%");
                    selectParams.setAgentName("");
                }
            }
        } else {
            selectParams.setAgentNode(null);
        }
        List<Map<String, Object>> totalMap = merchantBusinessProductService.examineTotalByParam(selectParams);
        maps.put("result", totalMap.get(0));
        maps.put("status", true);
        return maps;
    }

    /**
     * 商户复审
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/reexamineRcored.do")
    @ResponseBody
    @SystemLog(description = "商户复审", operCode = "merchant.reexamineRcored")
    public Object reexamineRcored(@RequestBody String param, HttpServletRequest request) {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("result", false);
        jsonMap.put("msg", "操作失败");
        Map parMap = JSON.parseObject(param, Map.class);
        String val = String.valueOf(parMap.get("val"));
        String reexamine_status = val;
        if (Integer.valueOf(val) > 3) {
            reexamine_status = String.valueOf(Integer.valueOf(val) - 3);
        }
        ExaminationsLog el = new ExaminationsLog();
        el.setExamineType(2);
        el.setOpenStatus(reexamine_status);
        el.setCreateTime(new Date());
        el.setExaminationOpinions(String.valueOf(parMap.get("opinion")));
        el.setItemNo(String.valueOf(parMap.get("merbpId")));
        el.setBpId(String.valueOf(parMap.get("bpId")));
        // 获取当前登录的人
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        el.setOperator(principal.getId().toString());
        int count = examinationsLogService.insert(el);
        if (count > 0) {
            count = merchantBusinessProductService.reexamineBymbpId(Long.valueOf(String.valueOf(parMap.get("merbpId"))),
                    reexamine_status, principal.getUsername());
            if (count > 0) {
                if (parMap.get("listMris") != null) {
                    JSONArray jsAry = JSONObject.parseArray(String.valueOf(parMap.get("listMris")));
                    if (jsAry != null && !jsAry.isEmpty()) {
                        for (int i = 0; i < jsAry.size(); i++) {
                            JSONObject jsObj = jsAry.getJSONObject(i);
                            if (jsObj != null) {
                                if ("2".equals(reexamine_status)) {//不通过
                                    if ("不通过".equals(jsObj.getString("aStatus"))) {
                                        if (!"2".equals(jsObj.getString("status"))) {
                                            merchantRequireItemService.updateBymriId(jsObj.getLong("id"), "2");
                                        }

                                    }
                                } else if ("1".equals(reexamine_status)) {//通过
                                    if (!"1".equals(jsObj.getString("status"))) {
                                        merchantRequireItemService.updateBymriId(jsObj.getLong("id"), "1");
                                    }
                                }
                            }
                        }
                    }

                }


                if (parMap.get("listMri") != null) {
                    JSONArray jsAry = JSONObject.parseArray(String.valueOf(parMap.get("listMri")));
                    if (jsAry != null && !jsAry.isEmpty()) {
                        boolean isUniUpd = false;
                        for (int i = 0; i < jsAry.size(); i++) {
                            JSONObject jsObj = jsAry.getJSONObject(i);
                            if (jsObj != null) {
                                if ("2".equals(reexamine_status)) {//不通过
                                    //3-4-5-15联动不通过
                                    if ("3".equals(jsObj.getString("mriId"))
                                            || "4".equals(jsObj.getString("mriId"))
                                            || "5".equals(jsObj.getString("mriId"))
                                            || "15".equals(jsObj.getString("mriId"))) {
                                        if ("不通过".equals(jsObj.getString("aStatus"))) {
                                            isUniUpd = true;
                                            break;
                                        }

                                    }
                                }
                            }
                        }
                        for (int i = 0; i < jsAry.size(); i++) {
                            JSONObject jsObj = jsAry.getJSONObject(i);
                            if (jsObj != null) {
                                if ("2".equals(reexamine_status)) {//不通过
                                    boolean isUpd = false;
                                    if (isUniUpd) {//3-4-5-15联动不通过
                                        if ("3".equals(jsObj.getString("mriId"))
                                                || "4".equals(jsObj.getString("mriId"))
                                                || "5".equals(jsObj.getString("mriId"))
                                                || "15".equals(jsObj.getString("mriId"))) {
                                            merchantRequireItemService.updateBymriId(jsObj.getLong("id"), "2");
                                            isUpd = true;
                                        }
                                    }
                                    if (!isUpd && "不通过".equals(jsObj.getString("aStatus"))) {
                                        if (!"2".equals(jsObj.getString("status"))) {
                                            merchantRequireItemService.updateBymriId(jsObj.getLong("id"), "2");
                                        }

                                    }
                                } else if ("1".equals(reexamine_status)) {//通过
                                    if (!"1".equals(jsObj.getString("status"))) {
                                        merchantRequireItemService.updateBymriId(jsObj.getLong("id"), "1");
                                    }
                                }
                            }
                        }
                    }

                }

                if ("2".equals(reexamine_status)) {//不通过
                    if (StringUtils.isNotBlank(parMap.get("teamId") + "") && StringUtils.isNotBlank(parMap.get("mobilePhone") + "")) {
                        String teamName = merchantBusinessProductService.getTeamName(parMap.get("teamId") + "");
                        if (StringUtils.isNotBlank(teamName)) {
                            Sms.sendMsg(parMap.get("mobilePhone") + "", "感谢您使用" + teamName.trim() + "，您提交的商户"
                                    + parMap.get("merchantName") + "信息复审不通过，不通过原因为" + parMap.get("opinion") + "" + "请尽快重新修改资料！");
                        }
                    }

                }

                if ("1".equals(reexamine_status) || "2".equals(reexamine_status)) {//修改关联业务产品复审状态
                    MerchantBusinessProduct thisMbp = merchantBusinessProductService.selectByPrimaryKey(Long.valueOf(String.valueOf(parMap.get("merbpId"))));
                    if (thisMbp != null) {
                        if ("4".equals(thisMbp.getStatus())) {
                            List<MerchantBusinessProduct> proLists = merchantBusinessProductService.getByMerAndBpId(thisMbp.getMerchantNo());
                            if (proLists != null && !proLists.isEmpty()) {

                                for (MerchantBusinessProduct mbp : proLists) {
                                    String mbpId = mbp.getId() + "";
                                    if (!mbpId.equals(String.valueOf(parMap.get("merbpId")))) {

                                        List<MerchantRequireItem> mriLists = merchantRequireItemService.selectItemByBpIdAndMerNo(thisMbp.getMerchantNo(), mbp.getBpId());

                                        boolean canUpd = true;
                                        String opinion = "";
                                        if ("1".equals(reexamine_status)) {
                                            canUpd = true;
                                        } else if ("2".equals(reexamine_status)) {
                                            canUpd = false;
                                        }
                                        for (MerchantRequireItem mri : mriLists) {

                                            if ("1".equals(reexamine_status)) {
                                                if ("2".equals(mri.getStatus())) {
                                                    canUpd = false;
                                                }
                                            } else if ("2".equals(reexamine_status)) {
                                                if ("2".equals(mri.getStatus())) {
                                                    AddRequireItem ari = addRequireItemService.selectById(mri.getMriId());
                                                    canUpd = true;
                                                    opinion = opinion + ari.getCheckMsg();
                                                }
                                            }


                                        }

                                        if (canUpd) {
                                            el = new ExaminationsLog();
                                            el.setExamineType(2);
                                            el.setOpenStatus(reexamine_status);
                                            el.setCreateTime(new Date());
                                            el.setExaminationOpinions(opinion);
                                            el.setItemNo(String.valueOf(mbpId));
                                            el.setBpId(mbp.getBpId());
                                            el.setOperator(principal.getId().toString());
                                            examinationsLogService.insert(el);

                                            merchantBusinessProductService.reexamineBymbpId(mbp.getId(),
                                                    reexamine_status, principal.getUsername());

                                        }


                                    }
                                }
                            }

                        }
                    }

                }

                jsonMap.put("result", true);
                jsonMap.put("msg", "操作成功");
            } else {
                log.error("复审状态修改失败");
                return jsonMap;
            }
        } else {
            log.error("复审日志记录失败");
            return jsonMap;
        }

        // 复审退件，调用退件处理
        if ("3".equals(val) || "6".equals(val)) {
            // 复审退件
            parMap.put("val", 2);
            parMap.put("isReexamine", "isReexamine");
            jsonMap = (JSONObject) examineRcored(JSON.toJSONString(parMap));
            if ("false".equals(String.valueOf(jsonMap.get("result")))) {
                return jsonMap;
            }
        }
        // 跳下一条
        if ("4".equals(val) || "5".equals(val) || "6".equals(val)) {
            Page<MerchantBusinessProduct> page = new Page<MerchantBusinessProduct>();
            page.setPageSize(1);
            String redisKey = getIpAddr(request) + "secret" + principal.getId().toString();
            SelectParams selectParams = new SelectParams();
            try {
                log.info("获取缓存复审查询条件：{}", redisKey);
                if (redisService.exists(redisKey)) {
                    String redisVal = StringUtil.filterNull(redisService.select(redisKey));
                    log.info("缓存条件值:{}", redisVal);
                    selectParams = JSON.parseObject(redisVal, SelectParams.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                if (ais != null) {
                    if (selectParams.getAgentNode().equals("0")) {
                        selectParams.setAgentNode(ais.getAgentNode());
                    } else {
                        selectParams.setAgentNode(ais.getAgentNode() + "%");
                        selectParams.setAgentName("");
                    }
                }
            } else {
                selectParams.setAgentNode(null);
            }
            selectParams.setNotMbpId(String.valueOf(parMap.get("merbpId")));
            List<MerchantBusinessProduct> list = merchantBusinessProductService.selectByParam(page, selectParams);
            if (list.size() < 1) {
                jsonMap.put("msg", "没有可处理的商户");
                jsonMap.put("result", true);
                jsonMap.put("bols", false);
            } else {
                jsonMap.put("result", true);
                jsonMap.put("msg", "操作成功");
                jsonMap.put("infos", list.get(0).getId().toString());
            }
        } else {
            jsonMap.put("result", true);
            jsonMap.put("msg", "操作成功");
        }
        return jsonMap;
    }

    // 商户复审导出
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/exportExamine.do")
    @ResponseBody
    public void exportExamine(@RequestParam("info") String param, HttpServletResponse response,
                              HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("utf-8");
        log.info("商户复审导出：{}", param);
        SelectParams selectParams = JSON.parseObject(param, SelectParams.class);
        if (StringUtils.isNotBlank(selectParams.getAgentName())) {
            AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
            if (ais != null) {
                if (selectParams.getAgentNode().equals("0")) {
                    selectParams.setAgentNode(ais.getAgentNode());
                } else {
                    selectParams.setAgentNode(ais.getAgentNode() + "%");
                    selectParams.setAgentName("");
                }
            }
        } else {
            selectParams.setAgentNode(null);
        }
        merchantBusinessProductService.exportExamine(selectParams, response, request);

    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getIndustryMcc")
    @ResponseBody
    public List<IndustryMcc> getIndustryMcc(@RequestParam("parentId") String parentId) {
        try {
            System.out.println("parentId=" + parentId);
            return merchantRequireItemService.selectIndustryMccByParentId(parentId);
        } catch (Exception e) {
            log.error("获取MCC列表异常", e);
        }
        return new ArrayList<IndustryMcc>();
    }

    /**
     * 设置所属行业MCC
     *
     * @return
     */
    @RequestMapping(value = "saveIndustryMcc")
    @ResponseBody
    public Object saveIndustryMcc(@RequestParam("id") String id, @RequestParam("mcc") String mcc, @RequestParam("merNo") String merNo) {

        log.info("设置所属行业参数：id= " + id + ",mcc=" + mcc + ",merNo=" + merNo);
        Map<String, Object> maps = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(mcc) && StringUtils.isNotBlank(merNo)) {
            try {
                int sucCount = merchantRequireItemService.updateMccById(id, mcc);
                if (sucCount > 0) {
                    sucCount = merchantRequireItemService.updateMccByMerNo(merNo, mcc);
                    if (sucCount > 0) {
                        IndustryMcc industryMcc = merchantRequireItemService.selectIndustryMccByMcc(mcc);
                        maps.put("industryMcc", industryMcc);
                        maps.put("bols", true);
                        maps.put("msg", "设置所属行业成功");
                        return maps;
                    } else {
                        maps.put("bols", false);
                        maps.put("msg", "所属行业未改变");
                        return maps;
                    }

                } else {
                    maps.put("bols", false);
                    maps.put("msg", "所属行业未改变");
                    return maps;
                }
            } catch (Exception e) {
                log.error("设置所属行业出错------", e);
                maps.put("bols", false);
                maps.put("msg", "设置所属行业异常");
                return maps;
            }
        } else {
            maps.put("bols", false);
            maps.put("msg", "id、mcc、merNo不能为空！");
            return maps;
        }
    }

    //获取商户账号信息
    @RequestMapping(value = "/getZGAccount")
    @ResponseBody
    public Object getAccountInfo(@RequestParam("merNo") String merNo, @RequestParam("bpId") String bpId) {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("bols", false);
        maps.put("msg", "获取中钢余额异常");
        try {
            String channelCode = "ZG_ZQ";
            ZqMerchantInfo zqMer = zqMerInfoService.selectByMerNoAndBpId(merNo, bpId, channelCode);
            if (zqMer != null) {
                String accountNo = zqMer.getUnionpayMerNo();
                if (accountNo != null) {
                    SysDict sysDict = sysDictService.getByKey("FLOWMONEY_SERVICE_URL");
                    String accessUrl = sysDict.getSysValue() + "other/ZGZQAccount";
                    String str = ClientInterface.getMerchantZGBalance(accessUrl, accountNo);
                    if (StringUtils.isNotBlank(str)) {
			/*	{"errmsg":"SUCCESS","account":"0"}
				{"errmsg":"验证签名错误"}
				{"errmsg":"查询余额验签失败"}*/
                        JSONObject jsons = JSON.parseObject(str);
                        if (jsons != null) {
                            if ("SUCCESS".equals(jsons.getString("errmsg"))) {
                                maps.put("account", jsons.get("account"));
                                maps.put("bols", true);
                                maps.put("msg", "获取中钢余额成功");
                                return maps;
                            } else {

                                maps.put("bols", false);
                                maps.put("msg", jsons.getString("errmsg"));
                                return maps;
                            }

                        } else {
                            maps.put("bols", false);
                            maps.put("msg", "查询中钢余额异常");
                            return maps;
                        }

                    }

                } else {
                    maps.put("msg", "当前业务产品无直清报备信息");
                }
            } else {
                maps.put("msg", "当前业务产品无直清报备信息");

            }

        } catch (Exception e) {
            log.error("获取中钢余额异常", e);
            maps.put("bols", false);
            maps.put("msg", "获取中钢余额异常");
        }
        return maps;
    }


    //获取易票联商户账号信息
    @RequestMapping(value = "/getYPLAccount")
    @ResponseBody
    public Object getYPLAccount(@RequestParam("merNo") String merNo, @RequestParam("bpId") String bpId) {
        Map<String, Object> maps = new HashMap<>();
        String channelCode = "YPL_ZQ";
        try {
            ZqMerchantInfo zqMer = zqMerInfoService.selectByMerNoAndBpId(merNo, bpId, channelCode);
            if (zqMer != null) {
                String accountNo = zqMer.getUnionpayMerNo();
                if (accountNo != null) {
                    SysDict sysDict = sysDictService.getByKey("FLOWMONEY_SERVICE_URL");
                    String accessUrl = sysDict.getSysValue() + "other/ZQAccount";
                    String str = ClientInterface.getMerchantYPLBalance(accessUrl, accountNo);
                    if (StringUtils.isNotBlank(str)) {
                        JSONObject jsons = JSON.parseObject(str);
                        if (jsons != null) {
                            if ("SUCCESS".equals(jsons.getString("errmsg"))) {
                                maps.put("account", jsons.get("account"));
                                maps.put("bols", true);
                                maps.put("msg", "获取易票联余额成功");
                                return maps;
                            } else {

                                maps.put("bols", false);
                                maps.put("msg", jsons.getString("errmsg"));
                                return maps;
                            }
                        } else {
                            maps.put("bols", false);
                            maps.put("msg", "查询易票联余额异常");
                            return maps;
                        }

                    }

                } else {
                    maps.put("msg", "当前业务产品无直清报备信息");
                }
            } else {
                maps.put("msg", "当前业务产品无直清报备信息");

            }

        } catch (Exception e) {
            log.error("获取易票联余额异常", e);
            maps.put("bols", false);
            maps.put("msg", "获取易票联余额异常");
        }
        return maps;
    }

    /**
     * 获取商户绑定信用卡信息
     * @param merchantNo
     * @return
     */
    @RequestMapping("/selectMerchantCreditcard")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result selectMerchantCreditcard(String merchantNo){
        if(StringUtil.isEmpty(merchantNo)) {
            return Result.fail("参数不能为空");
        }
        try {
            List<AddCreaditcardLog> list = addCreaditcardLogService.selectMerchantCreditcard(merchantNo);
            Result result = new Result();
            if(list != null && list.size() > 0) {
                result.setStatus(true);
                result.setData(list);
                result.setMsg("查询成功");
            } else {
                result.setStatus(false);
                result.setMsg("商户没有绑定信用卡记录");
            }
            return result;
        } catch (Exception e) {
            log.error("查询商户绑定信用卡信息异常", e);
            return Result.fail("查询异常");
        }
    }

    @RequestMapping("/selectMerchantCreditcardDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result selectMerchantCreditcardDetail(Long id){
        if(id == null) {
            return Result.fail("参数不能为空");
        }
        try {
            AddCreaditcardLog baseInfo = addCreaditcardLogService.selectMerchantCreditcardDetail(id);
            if(baseInfo != null) {
                Result result = Result.success("查询成功");
                result.setData(baseInfo);
                return result;
            } else {
                return Result.fail("查询失败");
            }
        } catch (Exception e) {
            log.error("查询商户绑定信用卡信息异常", e);
            return Result.fail("查询异常");
        }
    }


    // 对于需要转换为Date类型的属性，使用DateEditor进行处理
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        // 对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    /**
     * 返回客户端IP地址
     *
     * @param request
     * @return
     */
    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static class SelectParams {
        private Date eTime;
        private Date sTime;
        private String termianlType;
        private String productType;
        private String agentName;
        private String agentNode;
        private String merchantExamineState;
        private String merchantNo;
        private String merchantType;
        private String mbpId;
        private String mobilephone;
        private String cardId;
        private String merAccount;
        private String acqOrgMerNo;
        private String riskStatus;
        private String auditorId;
        private String openStatus;
        private String autoCheckTimes;
        private String accountName;
        private String accountNo;
        private String merStatus;
        private String saleName;
        private String recommendedSource; // 推广来源
        private String itemSource;// 进件来源
        private String status;
        private String teamId;
        private String teamEntryId;
        private String province;
        private String city;
        private String reexamineStatus;// 复审状态
        private Date startReexamineTime;// 复审时间
        private Date endReexamineTime;// 复审时间
        private String reexamineOperator;// 复审人
        private String notMbpId;
        private String autoMbpChannel;//自动审核活体通道
        private String authChannel;//鉴权通道
        private Integer auditNum;//初审次数
        private String specialMerchant;//特约商户 1是，0否
        private String acqMerchantNo;//特约商户编号
        private String acqMerchantName;// 特约商户名称
        private Date bindTimeBegin;
        private Date bindTimeEnd;
        private Date bindGeneralMerchantTime;

        private Integer sourceSysSta;
        private String sourceSys;//是否是代理商推广

        public Integer getAuditNum() {
            return auditNum;
        }

        public void setAuditNum(Integer auditNum) {
            this.auditNum = auditNum;
        }

        public String getAuthChannel() {
            return authChannel;
        }

        public void setAuthChannel(String authChannel) {
            this.authChannel = authChannel;
        }

        public String getAutoMbpChannel() {
            return autoMbpChannel;
        }

        public void setAutoMbpChannel(String autoMbpChannel) {
            this.autoMbpChannel = autoMbpChannel;
        }

        public Date getStartReexamineTime() {
            return startReexamineTime;
        }

        public void setStartReexamineTime(Date startReexamineTime) {
            this.startReexamineTime = startReexamineTime;
        }

        public Date getEndReexamineTime() {
            return endReexamineTime;
        }

        public void setEndReexamineTime(Date endReexamineTime) {
            this.endReexamineTime = endReexamineTime;
        }

        public String getReexamineOperator() {
            return reexamineOperator;
        }

        public void setReexamineOperator(String reexamineOperator) {
            this.reexamineOperator = reexamineOperator;
        }

        public String getReexamineStatus() {
            return reexamineStatus;
        }

        public void setReexamineStatus(String reexamineStatus) {
            this.reexamineStatus = reexamineStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRecommendedSource() {
            return recommendedSource;
        }

        public void setRecommendedSource(String recommendedSource) {
            this.recommendedSource = recommendedSource;
        }

        public String getSyncStatus() {
            return syncStatus;
        }

        public void setSyncStatus(String syncStatus) {
            this.syncStatus = syncStatus;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }

        private String tradeType;
        private String syncStatus;

        public String getMerchantType() {
            return merchantType;
        }

        public void setMerchantType(String merchantType) {
            this.merchantType = merchantType;
        }

        public String getMerStatus() {
            return merStatus;
        }

        public void setMerStatus(String merStatus) {
            this.merStatus = merStatus;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        // 2.2.5 增加预冻结金额
        private String preFrozenMoney1;
        private String preFrozenMoney2;

        public String getPreFrozenMoney1() {
            return preFrozenMoney1;
        }

        public void setPreFrozenMoney1(String preFrozenMoney1) {
            this.preFrozenMoney1 = preFrozenMoney1;
        }

        public String getPreFrozenMoney2() {
            return preFrozenMoney2;
        }

        public void setPreFrozenMoney2(String preFrozenMoney2) {
            this.preFrozenMoney2 = preFrozenMoney2;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        private String activityType;

        public Date geteTime() {
            return eTime;
        }

        public void seteTime(Date eTime) {
            this.eTime = eTime;
        }

        public Date getsTime() {
            return sTime;
        }

        public void setsTime(Date sTime) {
            this.sTime = sTime;
        }

        public String getTermianlType() {
            return termianlType;
        }

        public void setTermianlType(String termianlType) {
            this.termianlType = termianlType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getMerchantExamineState() {
            return merchantExamineState;
        }

        public void setMerchantExamineState(String merchantExamineState) {
            this.merchantExamineState = merchantExamineState;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public String getMbpId() {
            return mbpId;
        }

        public void setMbpId(String mbpId) {
            this.mbpId = mbpId;
        }

        public String getAgentNode() {
            return agentNode;
        }

        public void setAgentNode(String agentNode) {
            this.agentNode = agentNode;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getMerAccount() {
            return merAccount;
        }

        public void setMerAccount(String merAccount) {
            this.merAccount = merAccount;
        }

        public String getAcqOrgMerNo() {
            return acqOrgMerNo;
        }

        public void setAcqOrgMerNo(String acqOrgMerNo) {
            this.acqOrgMerNo = acqOrgMerNo;
        }

        public String getRiskStatus() {
            return riskStatus;
        }

        public void setRiskStatus(String riskStatus) {
            this.riskStatus = riskStatus;
        }

        public String getAuditorId() {
            return auditorId;
        }

        public void setAuditorId(String auditorId) {
            this.auditorId = auditorId;
        }

        public String getOpenStatus() {
            return openStatus;
        }

        public void setOpenStatus(String openStatus) {
            this.openStatus = openStatus;
        }

        public String getAutoCheckTimes() {
            return autoCheckTimes;
        }

        public void setAutoCheckTimes(String autoCheckTimes) {
            this.autoCheckTimes = autoCheckTimes;
        }

        public String getSaleName() {
            return saleName;
        }

        public void setSaleName(String saleName) {
            this.saleName = saleName;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getTeamEntryId() {
            return teamEntryId;
        }

        public void setTeamEntryId(String teamEntryId) {
            this.teamEntryId = teamEntryId;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getNotMbpId() {
            return notMbpId;
        }

        public void setNotMbpId(String notMbpId) {
            this.notMbpId = notMbpId;
        }

        public String getItemSource() {
            return itemSource;
        }

        public void setItemSource(String itemSource) {
            this.itemSource = itemSource;
        }

        public String getSpecialMerchant() {
            return specialMerchant;
        }

        public void setSpecialMerchant(String specialMerchant) {
            this.specialMerchant = specialMerchant;
        }

        public String getAcqMerchantNo() {
            return acqMerchantNo;
        }

        public void setAcqMerchantNo(String acqMerchantNo) {
            this.acqMerchantNo = acqMerchantNo;
        }

        public String getAcqMerchantName() {
            return acqMerchantName;
        }

        public void setAcqMerchantName(String acqMerchantName) {
            this.acqMerchantName = acqMerchantName;
        }

        public Date getBindTimeBegin() {
            return bindTimeBegin;
        }

        public void setBindTimeBegin(Date bindTimeBegin) {
            this.bindTimeBegin = bindTimeBegin;
        }

        public Date getBindTimeEnd() {
            return bindTimeEnd;
        }

        public void setBindTimeEnd(Date bindTimeEnd) {
            this.bindTimeEnd = bindTimeEnd;
        }

        public Date getBindGeneralMerchantTime() {
            return bindGeneralMerchantTime;
        }

        public void setBindGeneralMerchantTime(Date bindGeneralMerchantTime) {
            this.bindGeneralMerchantTime = bindGeneralMerchantTime;
        }
        public Integer getSourceSysSta() {
            return sourceSysSta;
        }

        public void setSourceSysSta(Integer sourceSysSta) {
            this.sourceSysSta = sourceSysSta;
        }

        public String getSourceSys() {
            return sourceSys;
        }

        public void setSourceSys(String sourceSys) {
            this.sourceSys = sourceSys;
        }
    }

    //获取活体检测通道
    @RequestMapping("/queryHuoTiChannels")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public List<Map> queryHuoTiChannels(){

        try {
            return merchantBusinessProductService.queryHuoTiChannels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //获取鉴权通道
    @RequestMapping("/queryAuthChannels")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map queryAuthChannels(){
        Map map = new HashMap<String,Object>();

        try {
            map.put("data",merchantBusinessProductService.queryAuthChannels());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg","获取鉴权通道失败");
        }

        return map;
    }

}
