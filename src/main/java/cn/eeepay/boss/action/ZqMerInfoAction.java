package cn.eeepay.boss.action;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.BusinessRequireItemService;
import cn.eeepay.framework.service.ExaminationsLogService;
import cn.eeepay.framework.service.MerchantBusinessProductService;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.MerchantPreFreezeLogService;
import cn.eeepay.framework.service.MerchantRequireItemService;
import cn.eeepay.framework.service.MerchantServiceProService;
import cn.eeepay.framework.service.MerchantServiceQuotaService;
import cn.eeepay.framework.service.MerchantServiceRateService;
import cn.eeepay.framework.service.ServiceProService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TerminalInfoService;
import cn.eeepay.framework.service.ZqMerchantInfoService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;

/**
 * Created by Administrator on 2017/5/22.
 */

@Controller
@RequestMapping(value = "/zqMerInfoMgr")
public class ZqMerInfoAction {

    private static final Logger log = LoggerFactory.getLogger(ZqMerInfoAction.class);


    //商户业务产品服务类
    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;
    //直清商户服务类
    @Resource
    private ZqMerchantInfoService zqMerInfoService;
    //代理商服务类
    @Resource
    private AgentInfoService agentInfoService;
    //商户服务服务类
    @Resource
    private MerchantServiceProService merSerProService;
    //审核记录
    @Resource
    private ExaminationsLogService examinationsLogService;
    @Resource
    private MerchantPreFreezeLogService merchantPreFreezeLogService;
    //业务进件资料表
    @Resource
    private BusinessRequireItemService businessRequireItemService;
    @Resource
    private TerminalInfoService terminalInfoService;
    //商户进件条件表(明细)
    @Resource
    private MerchantRequireItemService merchantRequireItemService;
    //商户服务限额信息
    @Resource
    private MerchantServiceQuotaService merchantServiceQuotaService;
    //服务费率拼接
    @Resource
    private ServiceProService serviceProService;
    //商户服务签约费率
    @Resource
    private MerchantServiceRateService merchantServiceRateService;
    //商户信息
    @Resource
    private MerchantInfoService merchantInfoService;
    //数据字典服务
    @Resource
    private SysDictService sysDictService;

    /**
     * 直清商户管理模糊查询
     *
     * @param param
     * @param page
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAllZqMerInfo.do")
    @ResponseBody
    public Object selectAllZqMerInfo(@RequestParam("info") String param, @ModelAttribute("page") Page<ZqMerchantInfo> page) {
        ZqMerParams zqMerParams = JSON.parseObject(param, ZqMerParams.class);
        List<ZqMerchantInfo> zqMerInfos = null;
        try {
            zqMerInfos = zqMerInfoService.selectAllZqMerInfoByPage(page, zqMerParams);
            page.setResult(zqMerInfos);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模糊查询失败失败----", e);
        }
        return page;
    }

    /**
     * 导入直清商户
     *
     * @return
     */
    @RequestMapping(value = "/impZqMerInfo.do")
    @ResponseBody
    public Object impZqMerInfo(@RequestParam("file") MultipartFile file, @RequestParam("impChannelCode") String channelCode, HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        log.info("导入直清商户......................................channelCode=" + channelCode);
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (StringUtil.isBlank(channelCode)) {
                jsonMap.put("result", false);
                jsonMap.put("msg", "直清通道不能为空");
                return jsonMap;
            }
            if (file.isEmpty()) {
                jsonMap.put("result", false);
                jsonMap.put("msg", "导入文件不存在");
                return jsonMap;
            }
            int index = file.getOriginalFilename().lastIndexOf(".");
            if (index < 0) {
                jsonMap.put("result", false);
                jsonMap.put("msg", "导入文件文件格式有误");
                return jsonMap;
            }
            String format = file.getOriginalFilename().substring(index);
            if (!".xls".equals(format) && !".xlxs".equals(format) && !".xlsx".equals(format)) {
                jsonMap.put("result", false);
                jsonMap.put("msg", "导入文件文件格式有误");
                return jsonMap;
            }
            Workbook wb = WorkbookFactory.create(file.getInputStream());
//            List<ZqMerchantInfo> tlist = new ArrayList<>();
            Sheet sheet = wb.getSheetAt(0);
            int row_num = sheet.getLastRowNum();
            Row row;
            String unionpayMerNo, terminalNo,syncStatus;
            Long mbpId, merServiceId;
            ZqMerchantInfo zqMerchantInfo;
            MerchantService merchantService;
            String regid = null;
            List<String> resMsg = new ArrayList<>();
            Cell cell;
            for (int i = 1; i <= row_num; i++) {
                row = sheet.getRow(i);

                if (null == row) {
                    resMsg.add("第" + (i + 1) + "行数据不能为空");
                    continue;
                }

                cell = row.getCell(0);
                if (null == cell) {
                    resMsg.add("第" + (i + 1) + "行银联报备商户编号不能为空");
                    continue;
                }
                unionpayMerNo = getCellValue(cell).trim();
                if (StringUtils.isBlank(unionpayMerNo)) {
                    resMsg.add("第" + (i + 1) + "行银联报备商户编号不能为空");
                    continue;
                }
                cell = row.getCell(1);
                terminalNo = null;
                // 'WF_ZQ'允许终端号为空
                if(!"WF_ZQ".equals(channelCode)) {
                	if (null == cell) {
                        resMsg.add("第" + (i + 1) + "行终端号不能为空");
                        continue;
                    }
                	terminalNo = getCellValue(cell).trim();
                    if (StringUtils.isBlank(terminalNo)) {
                        resMsg.add("第" + (i + 1) + "行终端号不能为空");
                        continue;
                    }
                }
                cell = row.getCell(2);
                if (null == cell) {
                    resMsg.add("第" + (i + 1) + "行商户进价编号不能为空");
                    continue;
                }
                if (StringUtils.isBlank(getCellValue(cell))) {
                    resMsg.add("第" + (i + 1) + "行商户进价编号不能为空");
                    continue;
                }
                mbpId = Long.parseLong(getCellValue(cell).trim());

                if("YS_ZQ".equals(channelCode)){//银盛直清，第列为统一社会信用代码
                	
                	cell = row.getCell(3);
                    if (null == cell) {
                        resMsg.add("第" + (i + 1) + "行统一社会信用代码不能为空");
                        continue;
                    }
                    regid = getCellValue(cell).trim();
                    if (StringUtils.isBlank(regid)) {
                        resMsg.add("第" + (i + 1) + "行统一社会信用代码不能为空");
                        continue;
                    }
                	
                	cell = row.getCell(4);
                    if (null == cell) {
                        resMsg.add("第" + (i + 1) + "行同步状态不能为空");
                        continue;
                    }
                    syncStatus = getCellValue(cell).trim();
                    if (StringUtils.isBlank(syncStatus)) {
                        resMsg.add("第" + (i + 1) + "行同步状态不能为空");
                        continue;
                    }
                	
                }else{
                	 cell = row.getCell(3);
                     if (null == cell) {
                         resMsg.add("第" + (i + 1) + "行同步状态不能为空");
                         continue;
                     }
                     syncStatus = getCellValue(cell).trim();
                     if (StringUtils.isBlank(syncStatus)) {
                         resMsg.add("第" + (i + 1) + "行同步状态不能为空");
                         continue;
                     }
                }
                
                //根据商户进件编号获取商户业务产品对象
                MerchantBusinessProduct merBusPro = merchantBusinessProductService.selectByPrimaryKey(mbpId);
                if ("WF_ZQ".equals(channelCode)) {
                	merchantService = merSerProService.selectQuickOrNoCardSerByMbpId(mbpId.toString());
                	if (null == merchantService) {
                		resMsg.add("第" + (i + 1) + "行商户进件编号" + mbpId + "不存在 或 没开通快捷支付或无卡支付");
                		continue;
                	}
				} else {
					//根据商户进件编号获取该商户唯一的POS服务
					merchantService = merSerProService.selectPosSerByMbpId(mbpId.toString());
					if (null == merchantService) {
						resMsg.add("第" + (i + 1) + "行商户进件编号" + mbpId + "不存在或没开通POS服务");
						continue;
					}
				}
                //获取商户服务Id
                merServiceId = merchantService.getId();
                //根据商户服务Id和通道编码获取直清商户对象
                zqMerchantInfo = zqMerInfoService.selectZqMerInfoBymbpIDAndChannel(mbpId.toString(), channelCode);
                //根据商户银联编号查询出直清商户对象
                ZqMerchantInfo unionZqMerInfo = zqMerInfoService.selectByUnionpayMerNo(unionpayMerNo);
                //校验信用代码是否重复
                ZqMerchantInfo regidZqMerInfo = zqMerInfoService.selectByRegid(regid);
                if (zqMerchantInfo != null) {
                    if (null != unionZqMerInfo && !merServiceId.equals(unionZqMerInfo.getMerServiceId())) {
                        resMsg.add("第" + (i + 1) + "行银联报备商户编号" + unionpayMerNo + "已存在");
                        continue;
                    }
                    if(regidZqMerInfo != null){
                    	resMsg.add("第" + (i + 1) + "行统一社会信用代码" + regid + "已存在");
                        continue;
                    }
                    //当存在时直接覆盖该商户的同步状态，并设置为已生效的状态
                    zqMerchantInfo.setUnionpayMerNo(unionpayMerNo);
                    zqMerchantInfo.setTerminalNo(terminalNo);
                    zqMerchantInfo.setSyncStatus(syncStatus);
                    zqMerchantInfo.setEffectiveStatus("1");
                    zqMerchantInfo.setUpdateTime(new Date());
                    zqMerchantInfo.setOperator(principal.getId().toString());
                    zqMerchantInfo.setRegid(regid);
                    zqMerchantInfo.setMbpId(mbpId);

                    zqMerInfoService.updateZqMerInfo(zqMerchantInfo);
                } else {
                    if (null != unionZqMerInfo) {
                        resMsg.add("第" + (i + 1) + "行银联报备商户编号" + unionpayMerNo + "已存在");
                        continue;
                    }
                    if(regidZqMerInfo != null){
                    	resMsg.add("第" + (i + 1) + "行统一社会信用代码" + regid + "已存在");
                        continue;
                    }
                    //当商户在直清商户中不存在时创建该直清商户
                    zqMerchantInfo = new ZqMerchantInfo(merchantService.getMerchantNo(), merServiceId, syncStatus, channelCode, unionpayMerNo, terminalNo);
                    zqMerchantInfo.setEffectiveStatus("1");
                    zqMerchantInfo.setReportStatus("1");
                    zqMerchantInfo.setCreateTime(new Date());
                    zqMerchantInfo.setOperator(principal.getId().toString());
                    zqMerchantInfo.setRegid(regid);
                    zqMerchantInfo.setMbpId(mbpId);
                    zqMerInfoService.insertZqMerInfo(zqMerchantInfo);
                }
                //设置当前服务的其余通道为失效状态
                zqMerInfoService.updateOtherZqMerInfoEffStatus(mbpId.toString(), channelCode);
                //更新服务的交易模式
                merSerProService.updateTradeTypeByPrimaryKey(merServiceId, "1",channelCode);
                //修改服务的交易模式之后，更新对应商户进件对象的交易模式，只要有一条服务是直清，那么该进件就是直清
                merchantBusinessProductService.updateTradeTypeByServices(merBusPro);
            }
            if (resMsg.size() > 0) {
                request.getSession().setAttribute("impWarnData", resMsg);
                jsonMap.put("hasWarnData", "hasWarnData");
            } else {
                request.getSession().removeAttribute("impWarnData");
            }
            jsonMap.put("result", true);
        } catch (Exception e) {
            log.error("直清商户导入------", e);
            jsonMap.put("result", false);
            jsonMap.put("msg", "保存失败，请稍后再试");
        }
        return jsonMap;
    }

    /**
     * 下载导入异常信息
     */
    @RequestMapping(value = "/downHandleRes.do")
    @ResponseBody
    public void downHandleRes(HttpServletRequest request, HttpServletResponse response) {
        try {

            HttpSession session = request.getSession();
            String fileName = "直清商户导入异常.txt";

            request.setCharacterEncoding("UTF-8");
            response.reset(); // 清空输出流
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            response.setContentType("application/msexcel;charset=UTF-8");// 定义输出类型
            OutputStream os = response.getOutputStream(); // 取得输出流
            StringBuilder sb = new StringBuilder();

            List<String> impWarnData = (List<String>) session.getAttribute("impWarnData");
            session.removeAttribute("impWarnData");

            for (String warn : impWarnData) {
                sb.append(warn + "\n");
            }
            os.write(sb.toString().getBytes("GBK"));
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("直清商户导入异常");
        }
    }
    /**
     * 商户详情和审核查询
     *
     * @param merServiceId
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/showZqMerInfoDetail.do")
    @ResponseBody
    public Object showZqMerInfoDetail(@RequestParam("merServiceId") String merServiceId) {
        return getDetail(merServiceId,0);
    }
    /**
     * 敏感信息获取
     */
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getDataProcessing(@RequestParam("merServiceId") String merServiceId) throws Exception{
        return getDetail(merServiceId,3);
    }

    private Map<String,Object> getDetail(String merServiceId,int editState){
        Map<String, Object> maps = new HashMap<>();
        //商户业务产品
        MerchantBusinessProduct mbp = merchantBusinessProductService.selectByPrimaryKey(Long.valueOf(merServiceId));
        List<MerchantServiceRate> listmr = new ArrayList<MerchantServiceRate>();
        List<MerchantServiceQuota> listmsq = new ArrayList<MerchantServiceQuota>();
        List<MerchantRequireItem> listmri = new ArrayList<MerchantRequireItem>();
        List<MerchantRequireItem> listmris = new ArrayList<MerchantRequireItem>();
        List<AutoCheckResult> listacr = new ArrayList<AutoCheckResult>();
        List<MerchantInfo> mi = null;
        Page<TerminalInfo> tiPage = new Page<>();
        List<MerchantService> listms = null;
        List<ZqMerchantLog> zqMerLogs = null;
        MerchantServiceRate msr = new MerchantServiceRate();
        MerchantServiceQuota msq = new MerchantServiceQuota();
        List<ExaminationsLog> listel = new ArrayList<>();
        List<ExaminationsLog> exlistel = new ArrayList<>();
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

                    if("1".equals(mi.get(0).getMerchantType())){
                        mi.get(0).setSysName(
                                merchantInfoService.selectSysDictByKey(mi.get(0).getBusinessType(), "-1").getSysName());
                        if (mi.get(0).getIndustryType() != null) {
                            mi.get(0)
                                    .setTwoSysName(merchantInfoService
                                            .selectSysDictByKey(mi.get(0).getIndustryType(), mi.get(0).getBusinessType())
                                            .getSysName());
                        }
                    }else{
                        IndustryMcc industryMcc=merchantRequireItemService.selectIndustryMccByMcc(mi.get(0).getIndustryType());
                        if(industryMcc!=null){
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
                        List<ServiceRate> srlist = serviceProService.getServiceAllRate(sis.getServiceId(), mi.get(0).getOneAgentNo());
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
                        List<MerchantServiceRate> listmrs = merchantServiceRateService.selectByMertIdAndSerivceId(mbp.getMerchantNo(), str);
                        for (MerchantServiceRate merchantServiceRate : listmrs) {
                            //查询服务管控表的费率
                            ServiceRate sr = new ServiceRate();
                            sr.setAgentNo(mi.get(0).getOneAgentNo());
                            sr.setServiceId(Long.valueOf(merchantServiceRate.getServiceId()));
                            sr.setCardType(merchantServiceRate.getCardType());
                            sr.setHolidaysMark(merchantServiceRate.getHolidaysMark());
                            sr.setRateType(merchantServiceRate.getRateType());

                            //查询出一级代理商的费率
                            String oneRate = serviceProService.profitExpression(serviceProService.queryServiceRate(sr));
                            //判断是否固定
                            if (sis.getFixedRate() == 0) {
                                merchantServiceRate.setMerRate(merchantServiceRateService.profitExpression(merchantServiceRate));
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
                        List<ServiceQuota> sqlist = serviceProService.getServiceAllQuota(sis.getServiceId(), mi.get(0).getOneAgentNo());
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
                        List<MerchantServiceQuota> listmsqs = merchantServiceQuotaService.selectByMertIdAndServiceId(mbp.getMerchantNo(), str);
                        for (MerchantServiceQuota merchantServiceQuota : listmsqs) {
                            //查询服务限额表的费率
                            ServiceQuota sq = new ServiceQuota();
                            sq.setAgentNo(mi.get(0).getOneAgentNo());
                            sq.setServiceId(Long.valueOf(merchantServiceQuota.getServiceId()));
                            sq.setCardType(merchantServiceQuota.getCardType());
                            sq.setHolidaysMark(merchantServiceQuota.getHolidaysMark());

                            //查询出一级代理商的限额
                            sq = serviceProService.queryServiceQuota(sq);
                            sqlist1.add(sq);
                            //判断是否固定
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
                listms = merSerProService.selectByMerAndMbpId(mbp.getMerchantNo(), mbp.getBpId());
                zqMerLogs = zqMerInfoService.selectZqMerLogsByMerAndMbpId(mbp.getMerchantNo(), mbp.getBpId());
            }

            if (mbp.getMerchantNo() != null) {
                if("1".equals(mbp.getMerchantType())) {//商户类型:1-个人
                    List<String> listStrs = businessRequireItemService.findByProduct(mbp.getBpId());
                    //判断1图片、2文件
                    for (String string : listStrs) {
                        Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                        MerchantRequireItem mri = new MerchantRequireItem();
                        mri = merchantRequireItemService.selectByMriId(string, mbp.getMerchantNo());
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
                                    String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
                                    mri.setContent(newContent);
                                    listmris.add(mri);
                                    continue;
                                }
                            }
                            if (mri.getExampleType().equals("2")) {
                                String content = mri.getContent();
                                String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
                                mri.setContent(newContent);
                            }
                        }
                        //敏感信息屏蔽
                        if(0==editState&&mri.getMriId().equals("6")){
                            mri.setContent(StringUtil.sensitiveInformationHandle(mri.getContent(),1));
                        }
                        listmri.add(mri);
                    }
                }else{//商户类型:2-个体商户，3-企业商户',

                    List<MerchantRequireItem> mriLists=merchantRequireItemService.getByMer(mbp.getMerchantNo());
                    if(mriLists!=null&&!mriLists.isEmpty()){
                        for(MerchantRequireItem mri:mriLists){
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


                            if("37".equals(mri.getMriId())){
                                IndustryMcc industryMcc=merchantRequireItemService.selectIndustryMccByMcc(mri.getContent());
                                if(industryMcc!=null){
                                    mri.setIndustryMcc(industryMcc);
                                    mri.setContent(industryMcc.getIndustryName1()+"-"+industryMcc.getIndustryName());
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
            //审核记录
            List<ExaminationsLog> exList = examinationsLogService.selectByMerchantId(mbp.getId().toString());
            for (ExaminationsLog elog : exList) {
                //初审
                if(elog.getExamineType()==1){
                    listel.add(elog);
                }else if(elog.getExamineType()==2){
                    //复审
                    exlistel.add(elog);
                }
            }

            //基本信息中 冻结金额要从账户接口中获得
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
            //最近预冻结操作
            preFreezeList = merchantPreFreezeLogService.selectByMerchantNo(mbp.getMerchantNo());


            //敏感信息屏蔽
            if(0==editState&&mi.get(0)!=null){
                mi.get(0).setMobilephone(StringUtil.sensitiveInformationHandle(mi.get(0).getMobilephone(),0));
                mi.get(0).setIdCardNo(StringUtil.sensitiveInformationHandle(mi.get(0).getIdCardNo(),1));
            }

            maps.put("agent", agentInfoService.selectByagentNo(mi.get(0).getOneAgentNo()));
            maps.put("merAgent", agentInfoService.selectByagentNo(mi.get(0).getAgentNo()));
            maps.put("mbp", mbp);
            maps.put("mi", mi.get(0));
            maps.put("listmr", listmr);
            maps.put("tiPage", tiPage);
            maps.put("listmsq", listmsq);
            maps.put("sqlist", sqlist1);
            maps.put("listel", listel);
            maps.put("exlistel", exlistel);
            maps.put("preFreezeList", preFreezeList);//2.2.5 预冻结操作
            maps.put("listmri", listmri);
            maps.put("listmris", listmris);
            maps.put("serviceMgr", listms);
            maps.put("zqMerLogs", zqMerLogs);
            maps.put("bols", true);
        } catch (Exception e) {
            log.error("详情查询报错", e);
            maps.put("msg", "详情查询报错");
            maps.put("bols", false);
        }
        return maps;
    }


    /**
     * 下载商户导入模板
     *
     * @return
     */
    @RequestMapping("/downloadZqMerTemplate.do")
    public String downloadZqMerTemplate(HttpServletRequest request, HttpServletResponse response) {
    	String channelCode = request.getParameter("channelCode");
    	String filePath = null;
    	if("YS_ZQ".equals(channelCode)){//银盛直清模板
    		filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator + "YSzqMerchantTemplate.xls";
    	}else{
    		filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator + "zqMerchantTemplate.xls";
    	}
        log.info(filePath);
        ResponseUtil.download(response, filePath, "直清商户批量导入模板.xls");
        return null;
    }

    /**
     * 直清商户导出
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/exportZqMerInfo.do")
    @ResponseBody
    public void exportZqMerInfo(@RequestParam("info") String param, HttpServletRequest request, HttpServletResponse response) {
        ZqMerParams zqMerParams = JSON.parseObject(param, ZqMerParams.class);
        List<ZqMerchantInfo> zqMerInfos = null;
        try {
            zqMerInfos = zqMerInfoService.selectAllZqMerInfo(zqMerParams);

            String fileName = "直清商户查询导出.xls";
            OutputStream os = getDownStream(fileName, request, response);
            // 1.创建一个 workbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 2.创建一个 worksheet
            HSSFSheet worksheet = workbook.createSheet("Sheet1");
            // 单元格样式
            HSSFFont font = workbook.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 14);
            HSSFCellStyle cellStyle = worksheet.getWorkbook().createCellStyle();
            cellStyle.setFont(font);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            // 4.创建报表
            String[] titles = new String[]{"银联报备商户编号", "商户进件编号", "商户编号", "交易模式", "同步状态", "手机号", "商户名称"
                    , "代理商名称", "业务产品", "通道名称", "同步备注", "创建时间", "创建人"};
            buildReportHead(worksheet, titles, cellStyle);

            HSSFRow row;
            // 5.定义表体起始行
            int startRowIndex = 1;
            //如果查不到数据就是只有标题的空excel
            for (ZqMerchantInfo zqMerInfo : zqMerInfos) {
                //创建数据行
                row = worksheet.createRow(startRowIndex);
                fillReportBody(row, 0, cellStyle, zqMerInfo.getUnionpayMerNo());
                fillReportBody(row, 1, cellStyle, zqMerInfo.getMbpId() == null ? "" : zqMerInfo.getMbpId().toString());
                fillReportBody(row, 2, cellStyle, zqMerInfo.getMerchantNo());
                fillReportBody(row, 3, cellStyle, getTradeTypeZH(zqMerInfo.getTradeType()));
                fillReportBody(row, 4, cellStyle, getSyncStatusZH(zqMerInfo.getSyncStatus()));
                fillReportBody(row, 5, cellStyle, zqMerInfo.getMiMobilephone());
                fillReportBody(row, 6, cellStyle, zqMerInfo.getMerchantName());
                fillReportBody(row, 7, cellStyle, zqMerInfo.getAgentName());
                fillReportBody(row, 8, cellStyle, zqMerInfo.getBpName());
                fillReportBody(row, 9, cellStyle, zqMerInfo.getChannelCode());
                fillReportBody(row, 10, cellStyle, zqMerInfo.getSyncRemark());
                fillReportBody(row, 11, cellStyle, getCreateTimeStr(zqMerInfo.getCreateTime()));
                fillReportBody(row, 12, cellStyle, zqMerInfo.getOperatorName());

                startRowIndex++;
            }
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
        }
    }

    /**
     * 直清商户批量同步
     */
    @RequestMapping(value = "/batchSyncZqMer.do")
    @ResponseBody
    public Object batchSyncZqMer(@RequestParam("param") String param) {
        JSONObject json = JSON.parseObject(param);
        Map<String, Object> jsonMap = new HashMap<>();
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            List<ZqMerchantInfo> zqMerInfos = JSON.parseArray(json.getJSONArray("list").toJSONString(), ZqMerchantInfo.class);
            //循环遍历直清商户，调用商户修改接口
            SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
            String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
            String paramStr, merchantNo, bpId;   
            String res;
            JSONObject resJson;
            int successNum = 0, failNum = 0;
            for (ZqMerchantInfo zqMerInfo : zqMerInfos) {

                merchantNo = zqMerInfo.getMerchantNo();
                bpId = zqMerInfo.getBpId() == null ? "" : zqMerInfo.getBpId().toString();

                Map<String, Object> marMap = new HashMap<String, Object>();
                List<String> channelList = new ArrayList<>();
                channelList.add(zqMerInfo.getChannelCode());
                marMap.put("merchantNo", merchantNo);
                marMap.put("bpId", bpId);
                marMap.put("operator", principal.getId().toString());
                marMap.put("changeSettleCard", "0");
                marMap.put("channelCode", channelList);
                paramStr = JSON.toJSONString(marMap);
                res=new ClientInterface(accessUrl, null).postRequestBody(paramStr);
                resJson = JSONObject.parseObject(res);
                resJson = resJson.getJSONObject("header");
                if ("true".equals(resJson.getString("succeed"))) {
                    successNum++;
                } else {
                    failNum++;
                }
            }
            jsonMap.put("resMsg", "同步成功：" + successNum + "条\n同步失败：" + failNum + "条");
            jsonMap.put("result", true);
        } catch (Exception e) {
            log.error("直清商户批量导入失败-----", e);
            jsonMap.put("result", false);
            jsonMap.put("msg", "操作失败，请稍后再试");
        }
        return jsonMap;
    }

    public String getTradeTypeZH(String tradeType) {
        return "1".equals(tradeType) ? "直清模式" : "集群模式";
    }

    public String getSyncStatusZH(String syncStatus) {
    	switch (syncStatus) {
	    	case "0":
	             return "初始化";
	        case "1":
	            return "同步成功";
	        case "2":
	            return "同步失败";
	        case "3":
	            return "审核中";
	        default:
	        return "";
    	}
    }

    public String getCreateTimeStr(Date createTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return createTime == null ? "" : sdf.format(createTime);
    }

    public String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getStringCellValue();
        }
        return null;
    }

    /**
     * 获取文件导出流
     *
     * @return
     */
    protected OutputStream getDownStream(String fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.reset(); // 清空输出流
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            response.setContentType("application/msexcel;charset=UTF-8");// 定义输出类型
            OutputStream os = response.getOutputStream(); // 取得输出流
            return os;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建Excel表头
     *
     * @param worksheet
     */
    private static void buildReportHead(HSSFSheet worksheet, String[] titles, HSSFCellStyle cellStyle) {
        // 创建字段标题
        HSSFRow rowHeader = worksheet.createRow(0);
        int index = 0;
        HSSFCell cell;
        for (String title : titles) {
            worksheet.setColumnWidth(index, 8000);
            cell = rowHeader.createCell(index);
            cell.setCellValue(title);
            cell.setCellStyle(cellStyle);
            index++;
        }
    }

    /**
     * 填充Excel表体数据
     *
     * @param row
     * @param colIndex
     * @param bodyCellStyle
     * @param value
     */
    private static void fillReportBody(HSSFRow row, int colIndex, HSSFCellStyle bodyCellStyle, String value) {
        HSSFCell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(bodyCellStyle);
    }

    public static class ZqMerParams {

        private String agentNo;
        private String unionpayMerNo;
        private String mbpId;
        private String merchantNo;
        private String channelCode;
        private String productType;
        private String mobilephone;
        private String accountName;
        private String cardId;
        private String terminalNo;
        private String tradeType;
        private String syncStatus;
        private Date eTime;
        private Date sTime;

        public String getAgentNo() {
            return agentNo;
        }

        public void setAgentNo(String agentNo) {
            this.agentNo = agentNo;
        }

        public String getUnionpayMerNo() {
            return unionpayMerNo;
        }

        public void setUnionpayMerNo(String unionpayMerNo) {
            this.unionpayMerNo = unionpayMerNo;
        }

        public String getMbpId() {
            return mbpId;
        }

        public void setMbpId(String mbpId) {
            this.mbpId = mbpId;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public String getChannelCode() {
            return channelCode;
        }

        public void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getTerminalNo() {
            return terminalNo;
        }

        public void setTerminalNo(String terminalNo) {
            this.terminalNo = terminalNo;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }

        public String getSyncStatus() {
            return syncStatus;
        }

        public void setSyncStatus(String syncStatus) {
            this.syncStatus = syncStatus;
        }

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
    }

}
