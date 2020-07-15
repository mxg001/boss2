package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.HappyBackActivityAgentDao;
import cn.eeepay.framework.dao.HappyBackActivityMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.HappyBackActivityAgent;
import cn.eeepay.framework.model.HappyBackActivityAgentDetail;
import cn.eeepay.framework.model.HappyBackActivityMerchant;
import cn.eeepay.framework.service.HappyBackActivityAgentService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author rpc
 * @description 欢乐返代理商奖励查询
 * @date 2019/11/7
 */
@Service
public class HappyBackActivityAgentServiceImpl implements HappyBackActivityAgentService {

    @Resource
    private HappyBackActivityAgentDao happyBackActivityAgentDao;

    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public void selectHappyBackActivityAgent(Page<HappyBackActivityAgent> page, HappyBackActivityAgent happyBackActivityAgent) {
        happyBackActivityAgentDao.selectHappyBackActivityAgent(page, happyBackActivityAgent);
    }

    @Override
    public Map<String, Object> countMoney(HappyBackActivityAgent happyBackActivityAgent) {
        Map<String, Object> map = happyBackActivityAgentDao.countRewardAmount(happyBackActivityAgent);
        return map;
    }

    @Override
    public void exportExcel(HappyBackActivityAgent happyBackActivityAgent, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<HappyBackActivityAgent> list = happyBackActivityAgentDao.exportExcel(happyBackActivityAgent);
        String fileName = "欢乐返代理商奖励活动" + sdf.format(new Date()) + ".xls";
        String fileNameFormat = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
        Map<String, String> targetStatusMap = new HashMap<>();
        targetStatusMap.put("1", "考核中");
        targetStatusMap.put("2", "已达标");
        targetStatusMap.put("3", "未达标");
        Map<String, String> accountStatusMap = new HashMap<>();
        accountStatusMap.put("0", "未入账");
        accountStatusMap.put("1", "已入账");
        List<Map<String, String>> data = new ArrayList<>();
        for (HappyBackActivityAgent item : list) {
            Map<String, String> map = new HashMap<>();
            map.put("activeOrder", item.getActiveOrder());
            map.put("activeTime", item.getActiveTime() == null ? "" : sdfTime.format(item.getActiveTime()));
            if (item.getScanActivityDays() != null && item.getScanActivityDays() != 0) {
                map.put("scanTargetAmount", "激活后" + (item.getScanActivityDays() == null ? "0" : item.getScanActivityDays()) + "天内，累计交易≥" + (item.getScanTargetAmount() == null ? "0" : item.getScanTargetAmount()) + "元");
                map.put("scanRewardAmount", item.getScanRewardAmount() == null ? "" : item.getScanRewardAmount().toString());
            } else {
                map.put("scanTargetAmount", "");
                map.put("scanRewardAmount", "");
            }

            map.put("scanTargetStatus", targetStatusMap.get(item.getScanTargetStatus()));
            map.put("scanTargetTime", item.getScanTargetTime() == null ? "" : sdfTime.format(item.getScanTargetTime()));
            map.put("scanRewardEndTime", item.getScanRewardEndTime() == null ? "" : sdfTime.format(item.getScanRewardEndTime()));
            map.put("scanAccountStatus", accountStatusMap.get(item.getScanAccountStatus()));
            map.put("scanAccountTime", item.getScanAccountTime() == null ? "" : sdfTime.format(item.getScanAccountTime()));
            if (item.getAllActivityDays() != null && item.getAllActivityDays() != 0) {
                map.put("allTargetAmount", "激活后" + (item.getAllActivityDays() == null ? "0" : item.getAllActivityDays()) + "天内，累计交易≥" + (item.getAllTargetAmount() == null ? "0" : item.getAllTargetAmount()) + "元");
                map.put("allRewardAmount", item.getAllRewardAmount() == null ? "" : item.getAllRewardAmount().toString());
            } else {
                map.put("allTargetAmount", "");
                map.put("allRewardAmount", "");

            }
            map.put("allTargetStatus", targetStatusMap.get(item.getAllTargetStatus()));
            map.put("allTargetTime", item.getAllTargetTime() == null ? "" : sdfTime.format(item.getAllTargetTime()));
            map.put("allRewardEndTime", item.getAllRewardEndTime() == null ? "" : sdfTime.format(item.getAllRewardEndTime()));
            map.put("allAccountStatus", accountStatusMap.get(item.getAllAccountStatus()));
            map.put("allAccountTime", item.getAllAccountTime() == null ? "" : sdfTime.format(item.getAllAccountTime()));

            map.put("activityTypeNo", item.getActivityTypeNo());
            map.put("merchantNo", item.getMerchantNo());
            map.put("teamName", item.getTeamName());
            map.put("teamEntryName", item.getTeamEntryName());
            map.put("hardId", item.getHardId()==null?"":item.getHardId().toString());

            map.put("agentName", item.getAgentName());
            map.put("agentNo", item.getAgentNo());
            map.put("oneAgentName", item.getOneAgentName());
            map.put("oneAgentNo", item.getOneAgentNo());
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"activeOrder", "activeTime",
                "scanTargetAmount", "scanRewardAmount", "scanTargetStatus", "scanTargetTime", "scanRewardEndTime", "scanAccountStatus", "scanAccountTime",
                "allTargetAmount", "allRewardAmount", "allTargetStatus", "allTargetTime", "allRewardEndTime", "allAccountStatus", "allAccountTime",
                "activityTypeNo","merchantNo","teamName","teamEntryName","hardId",
                "agentName", "agentNo", "oneAgentName", "oneAgentNo"};
        String[] colsName = new String[]{"激活订单号", "激活日期", "扫码交易满奖达标条件", "扫码交易满奖金额(元)", "扫码交易满奖达标状态", "扫码交易满奖达标日期", "扫码交易满奖活动截止日期",
                "扫码交易满奖入账状态", "扫码交易满奖入账日期", "全部交易满奖达标条件", "全部交易满奖金额(元)", "全部交易满奖达标状态", "全部交易满奖达标日期", "全部交易满奖活动截止日期",
                "全部交易满奖入账状态", "全部交易满奖入账日期",
                "欢乐返子类型编号","所属商户编号","所属组织","所属子组织","硬件产品ID",
                "所属代理商名称", "所属代理商编号", "一级代理商名称", "一级代理商编号"};
        OutputStream ouputStream = response.getOutputStream();
        export.export(cols, colsName, data, response.getOutputStream());
        ouputStream.close();
    }

    @Override
    public List<HappyBackActivityAgentDetail> agentAwardDetail(String id) {
        return happyBackActivityAgentDao.agentAwardDetail(id);
    }


    @Override
    public int updateHappyBackActivityAgentScan(HappyBackActivityAgent detail) {
        return happyBackActivityAgentDao.updateHappyBackActivityAgentScan(detail);
    }

    @Override
    public int updateHappyBackActivityAgentAll(HappyBackActivityAgent detail) {
        return happyBackActivityAgentDao.updateHappyBackActivityAgentAll(detail);
    }

    @Override
    public int updateHappyBackActivityAgentDetailScan(HappyBackActivityAgentDetail detail) {
        return happyBackActivityAgentDao.updateHappyBackActivityAgentDetailScan(detail);
    }

    @Override
    public int updateHappyBackActivityAgentDetailAll(HappyBackActivityAgentDetail detail) {
        return happyBackActivityAgentDao.updateHappyBackActivityAgentDetailAll(detail);
    }
}
