package cn.eeepay.framework.service.impl.cjt;

import cn.eeepay.framework.dao.cjt.CjtProfitDetailDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.cjt.CjtOrder;
import cn.eeepay.framework.model.cjt.CjtProfitDetail;
import cn.eeepay.framework.model.cjt.CjtProfitRule;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.cjt.CjtProfitDetailService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 超级推收益明细 服务层实现
 * @author tans
 * @date 2019-06-14
 */
@Service
public class CjtProfitDetailServiceImpl implements CjtProfitDetailService {

    private static final Logger log = LoggerFactory.getLogger(CjtProfitDetailServiceImpl.class);

    @Resource
    private CjtProfitDetailDao cjtProfitDetailDao;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private AgentInfoService agentInfoService;

    /**
     * 条件查询超级推收益明细
     * @param cjtProfitDetail
     * @return
     */
    @Override
    public void selectPage(Page<CjtProfitDetail> page, CjtProfitDetail cjtProfitDetail) {
        cjtProfitDetailDao.selectPage(page, cjtProfitDetail);
        List<CjtProfitDetail> list = page.getResult();
        if(list != null && list.size() > 0) {
            formatData(list);
        }
        return;
    }

    private void formatData(List<CjtProfitDetail> list) {
        BigDecimal oneHundred = new BigDecimal(100);
        for(CjtProfitDetail item: list) {
            item.setCreateTimeStr(DateUtil.getLongFormatDate(item.getCreateTime()));
            item.setRechargeTimeStr(item.getRechargeTime() != null ? DateUtil.getLongFormatDate(item.getRechargeTime()) : "");
            item.setFromLevelStr(CjtProfitDetail.levelMap.get(item.getFromLevel()));
            item.setTransTypeStr(CjtProfitDetail.transTypeMap.get(item.getProfitType()));
            item.setProfitTypeStr(CjtProfitDetail.profitTypeMap.get(item.getProfitType()));
            item.setRechargeStatusStr(CjtProfitDetail.rechargeStatusMap.get(item.getRechargeStatus()));
            if(item.getProfitRate() != null) {
                item.setProfitRateStr(item.getProfitRate().multiply(oneHundred).setScale(3, BigDecimal.ROUND_DOWN) + "%");//保留3位小数
            }
        }
    }

    @Override
    public Map<String, Object> selectTotal(CjtProfitDetail baseInfo) {
        return cjtProfitDetailDao.selectTotal(baseInfo);
    }

    @Override
    public Map<String, Object> selectTotalTrans(CjtProfitDetail baseInfo) {
        return cjtProfitDetailDao.selectTotalTrans(baseInfo);
    }

    @Override
    public void export(HttpServletResponse response, CjtProfitDetail baseInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<CjtProfitDetail> page = new Page<>(0, Integer.MAX_VALUE);
            selectPage(page, baseInfo);
            List<CjtProfitDetail> list = page.getResult();
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            boolean userTypeSta=false;//是否是代理商
            if("A".equals(baseInfo.getUserType())){
                userTypeSta=true;
            }
            String fileName = (userTypeSta?"代理商":"商户")+"-超级推收益明细"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<>() ;
            for(CjtProfitDetail item: list){
                Map<String,String> map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("merchantName", item.getMerchantName());
                map.put("merchantNo", item.getMerchantNo());
                map.put("profitAmount", item.getProfitAmount() != null ? String.valueOf(item.getProfitAmount()): "");
                if(!userTypeSta){
                    map.put("profitRateStr", item.getProfitRateStr());
                    map.put("fromLevelStr", item.getFromLevelStr());
                    map.put("transTypeStr", item.getTransTypeStr());
                }
                map.put("profitTypeStr", item.getProfitTypeStr());
                map.put("profitFromAmount", item.getProfitFromAmount() != null ? String.valueOf(item.getProfitFromAmount()): "");
                map.put("profitFromOrderNo", item.getProfitFromOrderNo());
                map.put("fromMerchantNo", item.getFromMerchantNo());
                map.put("createTimeStr", item.getCreateTimeStr());
                map.put("rechargeStatusStr", item.getRechargeStatusStr());
                map.put("rechargeTimeStr", item.getRechargeTimeStr());
                data.add(map);
            }

            String[] cols=null;
            String[] colsName=null;
            if(userTypeSta){//代理商
                 cols = new String[]{
                        "orderNo","merchantName","merchantNo","profitAmount",
                        "profitTypeStr","profitFromAmount","profitFromOrderNo","fromMerchantNo",
                        "createTimeStr","rechargeStatusStr","rechargeTimeStr"
                };
                colsName = new String[]{
                        "序号","收益代理商名称","收益代理编号","收益奖金",
                        "收益类型","交易金额","交易订单号","交易商户编号",
                        "收益创建时间","收益入账状态","收益入账时间"
                };
            }else{
                cols = new String[]{
                        "orderNo","merchantName","merchantNo","profitAmount","profitRateStr","fromLevelStr",
                        "transTypeStr","profitTypeStr","profitFromAmount","profitFromOrderNo","fromMerchantNo",
                        "createTimeStr","rechargeStatusStr","rechargeTimeStr"
                };
                colsName = new String[]{
                        "序号","收益商户名称","收益商户编号","收益奖金","收益百分比","收益级别",
                        "交易类型","收益类型","交易金额","交易订单号","交易商户编号",
                        "收益创建时间","收益入账状态","收益入账时间"
                };
            }
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级推收益明细异常", e);
        } finally {
            if(ouputStream!=null){
                try {
                    ouputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Result rechargeBatch(List<String> orderNoList,String userType) {
        String accountUrl=null;
        if("A".equals(userType)){//代理商
            accountUrl = sysDictService.getValueByKey("ACCOUNT_SERVICE_URL") + "/superPushPlusController/supPushAgentProfitRecordAccount.do";
        }else{
            accountUrl = sysDictService.getValueByKey("ACCOUNT_SERVICE_URL") + "/superPushPlusController/supPushMerProfitRecordAccount.do";
        }
        int successNum = 0;
        int failNum = 0;
        for(String orderNo: orderNoList) {
            boolean rechargeStatus = false;
            try {
                //入账
                rechargeStatus = recharge(orderNo, accountUrl,userType);
            } catch (Exception e) {
                log.error("批量入账失败", e);
            }
            if(rechargeStatus) {
                successNum++;
            } else {
                failNum++;
            }
        }
        return Result.success("批量入账完成！成功" + successNum + "条，失败" + failNum + "条。");
    }

    /**
     * 分润入账
     * @param orderNo
     * @param url
     * @return
     */
    private boolean recharge(String orderNo, String url,String userType) {
        CjtProfitDetail baseInfo = cjtProfitDetailDao.select(orderNo);
        if(baseInfo == null){
            log.error("订单[{}]不存在", orderNo);
            return false;
        }
        if("1".equals(baseInfo.getRechargeStatus())){
            log.error("订单[{}]已入账,无需入账", orderNo);
            return false;
        }
        String returnMsg =null;
        if("A".equals(userType)){//代理商
            AgentInfo agentInfo= agentInfoService.getAgentByNo(baseInfo.getMerchantNo());
            returnMsg = ClientInterface.cjtRechargeAgent(baseInfo,agentInfo, url);
        }else{
            returnMsg = ClientInterface.cjtRecharge(baseInfo, url);
        }
        if(StringUtils.isNotEmpty(returnMsg)) {
            JSONObject returnJson = JSONObject.parseObject(returnMsg);
            if(returnJson.getBoolean("status")) {
                cjtProfitDetailDao.updateRechargeStatus(orderNo, new Date(), "1");
                return true;
            }
        }
        return false;
    }
}
