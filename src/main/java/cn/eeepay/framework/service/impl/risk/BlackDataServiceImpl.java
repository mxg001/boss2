package cn.eeepay.framework.service.impl.risk;

import cn.eeepay.framework.dao.risk.BlackDataDao;
import cn.eeepay.framework.dao.risk.BlackDataDealRecordDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.PosCardBin;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.risk.*;
import cn.eeepay.framework.service.PosCardBinService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.risk.BlackDataService;
import cn.eeepay.framework.service.risk.SurveyReplyService;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.Md5;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MXG
 * create 2018/12/21
 */
@Service
public class BlackDataServiceImpl implements BlackDataService {

    @Resource
    private SurveyReplyService surveyReplyService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private PosCardBinService posCardBinService;
    @Resource
    private BlackDataDao blackDataDao;
    @Resource
    private BlackDataDealRecordDao dealRecordDao;

    private final String Token = "a185faf4c55807bca4bcfdaae06b58fa";
    private final String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
    private final String PATH = "/riskhandle/answerjpush";

    private Logger log = LoggerFactory.getLogger(BlackDataServiceImpl.class);

    @Override
    public Map<String, Object> getDetail(String id,int editState) {
        //基本信息
        Map<String, Object> result = new HashMap<>();
        BlackDataInfo info = blackDataDao.selectById(id);
        //敏感信息屏蔽
        if(0==editState&&info!=null){
            info.setMerchantPhone(StringUtil.sensitiveInformationHandle(info.getMerchantPhone(),0));
            info.setMerchantIdCard(StringUtil.sensitiveInformationHandle(info.getMerchantIdCard(),1));
        }
        String accountNO = info.getAccountNo();
        if(StringUtils.isNotBlank(accountNO)){
            PosCardBin posCardBin = posCardBinService.queryInfo(accountNO);
            info.setBankName(posCardBin.getBankName());
        }
        result.put("info", info);
        //商户回复内容
        ReplyRecord replyRecord = blackDataDao.selectReplyRecord(info.getOrderNo());
        if (replyRecord != null) {
            if (StringUtils.isNotBlank(replyRecord.getReplyFilesName())) {
                replyRecord.setFilesList(surveyReplyService.getFileList(replyRecord.getReplyFilesName()));
            }
            result.put("replyRecord", replyRecord);
            //风控处理内容
            DealRecord dealRecord = dealRecordDao.selectByOrder(replyRecord.getDealRecordOrderNo());
            result.put("dealRecord", dealRecord);
        }
        //黑名单资料处理日志
        List<BlackDataDealLog> logs = blackDataDao.selectLogByOrderNo(info.getOrderNo());
        result.put("logs", logs);
        //往来记录
        List<DealReplyRecord> dealReplyRecords = blackDataDao.selectDealReplyRecord(info.getOrderNo());
        for (DealReplyRecord record : dealReplyRecords) {
            if (StringUtils.isNotBlank(record.getFilesName())) {
                record.setFilesList(surveyReplyService.getFileList(record.getFilesName()));
            }
        }
        result.put("dealReplyRecords", dealReplyRecords);
        //风控处理回显
        DealRecord deal = dealRecordDao.selectByOrigOrderNoAndStatus(info.getOrderNo(), "1");
        result.put("deal", deal);
        return result;
    }

    @Override
    public void deal(DealRecord record, int type, String mobileNo, String merchantNo) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String lastOrderNo = record.getOrderNo();
        DealRecord lastDealRecord = null;

        //解冻
        if ("102".equals(record.getRiskDealTemplateNo())) {
            //将处理状态改为 解冻
            blackDataDao.updateRiskDealStatus(record.getOrigOrderNo(), "2", principal.getUsername());
            record.setOrderNo(getIndex());
            record.setStatus("1");
            record.setCreater(principal.getUsername());
            dealRecordDao.insert(record);
            //极光推送
            String authCode = Md5.md5Str(USER_INFO_ID + merchantNo + Token).toUpperCase();
            String paramStr = "token=" + Token + "&merchantNo=" + merchantNo + "&authCode=" + authCode + "&recordType=FINISH";
            String url = sysDictService.getByKey("CORE_URL").getSysValue() + PATH;
            String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
            log.info("极光推送结果：" + str);

        }else {
            record.setOrderNo(getIndex());
            record.setStatus("1");
            record.setCreater(principal.getUsername());
            dealRecordDao.insert(record);
            //改变黑名单资料状态 risk_last_deal_status=1(已处理)，mer_last_deal_status=1(未回复)
            blackDataDao.updateDealStatus(record.getOrigOrderNo(), "1", "1", principal.getUsername());

            //极光推送
            String authCode = Md5.md5Str(USER_INFO_ID + merchantNo + Token).toUpperCase();
            String paramStr = "token=" + Token + "&merchantNo=" + merchantNo + "&authCode=" + authCode + "&recordType=HANDLE";
            String url = sysDictService.getByKey("CORE_URL").getSysValue() + PATH;
            String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
            log.info("极光推送结果：" + str);
        }


        //如果为修改，将上条记录置为失效 status=0
        if (2 == type && StringUtils.isNotBlank(lastOrderNo)) {
            dealRecordDao.updateStatusByOrderNo(lastOrderNo, "0");
            lastDealRecord = dealRecordDao.selectByOrder(lastOrderNo);
        }

        //添加日志
        BlackDataDealLog dealLog = new BlackDataDealLog(record.getOrigOrderNo(),
                "1", principal.getUsername(), "black_data_deal_record",
                lastDealRecord == null ? "" : JSONObject.toJSONString(lastDealRecord), JSONObject.toJSONString(record));
        blackDataDao.addLog(dealLog);
    }

    @Override
    public void addRemark(String id, String remark) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        blackDataDao.addRemark(id, remark);
        BlackDataInfo info = blackDataDao.selectById(id);
        BlackDataDealLog blackDataDealLog = new BlackDataDealLog(info.getOrderNo(), "2",
                principal.getUsername(), "black_data_info", info.getRiskLastRemark(), remark);
        blackDataDao.addLog(blackDataDealLog);
    }


    public static AtomicInteger index = new AtomicInteger(5000);

    public String getIndex() {
        index.getAndIncrement();
        long now = System.currentTimeMillis();
        // 获取2位年份数字
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
        // 获取时间戳
        String time = dateFormat.format(now);
        String info = now + "";
        index.weakCompareAndSet(9999, 5000);
        return time + info.substring(2, info.length()) + index;
    }


    @Override
    public void export(BlackDataInfo info, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<BlackDataInfo> list = blackDataDao.selectByParam(info);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "调单查询" + sdf.format(new Date()) + ".xls";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        if (list.size() < 1) {
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("merchantNo", null);
            maps.put("merchantName", null);
            maps.put("lawyer", null);
            maps.put("merchantPhone", null);
            maps.put("agentNo", null);
            maps.put("agentName", null);
            maps.put("merRiskRulesNo", null);
            maps.put("blackCreator", null);
            maps.put("teamName", null);
            maps.put("recommendedSource", null);
            maps.put("merLastDealStatus", null);
            maps.put("merLastDealTime", null);
            maps.put("riskLastDealStatus", null);
            maps.put("riskLastDealOperator", null);
            maps.put("riskLastDealTime", null);
            maps.put("haveTriggerHis", null);
            maps.put("blackCreateRemark", null);
            maps.put("createTime", null);
            maps.put("riskLastRemark", null);
            data.add(maps);
        } else {
            for (BlackDataInfo black : list) {
                //屏蔽敏感信息
                black.setMerchantPhone(StringUtil.sensitiveInformationHandle(black.getMerchantPhone(),0));

                Map<String, String> maps = new HashMap<String, String>();
                maps.put("merchantNo", black.getMerchantNo() == null ? "" : black.getMerchantNo());
                maps.put("merchantName", black.getMerchantName() == null ? "" : black.getMerchantName());
                maps.put("lawyer", black.getLawyer() == null ? "" : black.getLawyer());
                maps.put("merchantPhone", black.getMerchantPhone() == null ? "" : black.getMerchantPhone());
                maps.put("agentNo", black.getAgentNo() == null ? "" : black.getAgentNo());
                maps.put("agentName", black.getAgentName() == null ? "" : black.getAgentName());
                maps.put("merRiskRulesNo", black.getMerRiskRulesNo() == null ? "" : black.getMerRiskRulesNo());
                maps.put("blackCreator", black.getBlackCreator() == null ? "" : black.getBlackCreator());
                maps.put("teamName", black.getTeamName() == null ? "" : black.getTeamName());

                String reSource = black.getRecommendedSource();//推广来源
                String reSourceValue = "";
                if ("0".equals(reSource)) {
                    reSourceValue = "正常";
                }
                if ("1".equals(reSource)) {
                    reSourceValue = "超级推";
                }
                if ("2".equals(reSource)) {
                    reSourceValue = "代理商";
                }
                if ("3".equals(reSource)) {
                    reSourceValue = "人人代理";
                }
                maps.put("recommendedSource", reSourceValue);

                String merdealStatus = black.getMerLastDealStatus();//商户回复状态
                String merDealStatusValue = "";
                if ("0".equals(merdealStatus)) {
                    merDealStatusValue = "初始化";
                }
                if ("1".equals(merdealStatus)) {
                    merDealStatusValue = "未回复";
                }
                if ("2".equals(merdealStatus)) {
                    merDealStatusValue = "已回复";
                }
                maps.put("merLastDealStatus", merDealStatusValue);

                maps.put("merLastDealTime", black.getMerLastDealTime());

                String riskDealStatus = black.getRiskLastDealStatus();//风控处理状态
                String riskDealStatusValue = "";
                if ("0".equals(riskDealStatus)) {
                    riskDealStatusValue = "未处理";
                }
                if ("1".equals(riskDealStatus)) {
                    riskDealStatusValue = "已处理";
                }
                if ("2".equals(riskDealStatus)) {
                    riskDealStatusValue = "解冻";
                }
                maps.put("riskLastDealStatus", riskDealStatusValue);
                maps.put("riskLastDealOperator", black.getRiskLastDealOperator() == null ? "" : black.getRiskLastDealOperator());
                maps.put("riskLastDealTime", black.getRiskLastDealTime() == null ? "" : black.getRiskLastDealTime());

                String haveHis = black.getHaveTriggerHis();//商户是否有历史触发记录
                maps.put("haveTriggerHis", "1".equals(haveHis) ? "是" : "否");
                maps.put("blackCreateRemark", black.getBlackCreateRemark() == null ? "" : black.getBlackCreateRemark());
                maps.put("createTime", black.getCreateTime() == null ? "" : black.getCreateTime());
                maps.put("riskLastRemark", black.getRiskLastRemark() == null ? "" : black.getRiskLastRemark());
                data.add(maps);
            }
        }

        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"merchantNo", "merchantName", "lawyer", "merchantPhone", "agentNo",
                "agentName", "merRiskRulesNo", "blackCreator", "teamName", "recommendedSource", "merLastDealStatus", "merLastDealTime", "riskLastDealStatus",
                "riskLastDealOperator", "riskLastDealTime", "haveTriggerHis", "blackCreateRemark", "createTime", "riskLastRemark"};
        String[] colsName = new String[]{"商户编号", "商户名称", "商户姓名", "商户手机号", "代理商编号", "代理商名称", "触发规则编号", "冻结记录创建人",
                "组织名称", "推广来源", "商户回复状态", "商户回复时间", "处理状态", "处理人", "处理时间", "历史触发记录", "黑名单备注", "创建时间", "备注"};
        OutputStream ouputStream = null;
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("黑名单资料导出失败!", e);
        } finally {
            if (ouputStream != null) {
                ouputStream.close();
            }
        }

    }

    @Override
    public List<BlackDataInfo> selectByParamWithPage(Page<BlackDataInfo> page, BlackDataInfo blackDataInfo) {
        return blackDataDao.selectByParamWithPage(page, blackDataInfo);
    }

    @Override
    public List<Map> selectTeamList() {
        return blackDataDao.selectTeamList();
    }

    @Override
    public String queryMbpId(String merchantNo) {
        return blackDataDao.queryMbpId(merchantNo);
    }
}
