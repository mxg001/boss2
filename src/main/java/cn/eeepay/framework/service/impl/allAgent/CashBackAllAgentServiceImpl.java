package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.CashBackAllAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityHardwareType;
import cn.eeepay.framework.model.allAgent.CashBackAllAgent;
import cn.eeepay.framework.service.ActivityService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.allAgent.CashBackAllAgentService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("cashBackAllAgentService")
public class CashBackAllAgentServiceImpl implements CashBackAllAgentService {
    private static final Logger log = LoggerFactory.getLogger(CashBackAllAgentServiceImpl.class);
    @Resource
    private CashBackAllAgentDao cashBackAllAgentDao;
    @Resource
    private ActivityService activityService;
    @Resource
    private SysDictService sysDictService;

    public List<CashBackAllAgent> queryCashBackDetailAllAgentList(CashBackAllAgent info, Page<CashBackAllAgent> page){
        return cashBackAllAgentDao.queryCashBackDetailAllAgentList(info,page);
    }

    /**
     * 导出盟主活动返现明细
     * @throws IOException
     */
    @Override
    public void exportCashBackAllAgent(CashBackAllAgent info, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;

        Page<CashBackAllAgent> page = new Page<>();
        page.setPageSize(Integer.MAX_VALUE);
        List<CashBackAllAgent> list = cashBackAllAgentDao.queryCashBackDetailAllAgentList(info,page);
        String fileName = "盟主活动返现明细"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;

        Map<String, String> types = new HashMap<>();
        types.put("008", "欢乐返-循环送");
        types.put("009", "欢乐返");
        List<ActivityHardwareType> activityHardwareTypeList=activityService.queryByactivityTypeNoList("008");
        activityHardwareTypeList.addAll(activityService.queryByactivityTypeNoList("009"));
        Map<String, String> activityTypeNames = new HashMap<>();
        for (ActivityHardwareType a:activityHardwareTypeList){
            activityTypeNames.put(a.getActivityTypeNo(),a.getActivityTypeName());
        }
        Map<String,String> sysDicts=sysDictService.selectMapByKey("AGENT_OEM");
        Map<String, String> entryStatuses = new HashMap<>();
        entryStatuses.put("0", "未入账");
        entryStatuses.put("1", "已入账");
        entryStatuses.put("2", "入账失败");

        for(CashBackAllAgent item: list){
            map = new HashMap<>();
            map.put("activeOrder", item.getActiveOrder());
            map.put("merchantNo", item.getMerchantNo());
            map.put("userName", item.getUserName());
            map.put("userCode", item.getUserCode());
            map.put("brandCode", StringUtils.trimToEmpty(sysDicts.get(item.getBrandCode())));
            map.put("oneAgentNo", item.getOneAgentNo());
            map.put("oneUserName", item.getOneUserName());
            map.put("oneUserCode", item.getOneUserCode());
            map.put("activityCode", StringUtils.trimToEmpty(types.get(item.getActivityCode())));
            map.put("activityTypeNo", StringUtils.trimToEmpty(activityTypeNames.get(item.getActivityTypeNo())));
            map.put("transAmount", StringUtil.filterNull(item.getTransAmount()));
            map.put("cashBackAmount", StringUtil.filterNull(item.getCashBackAmount()));
            map.put("entryStatus", StringUtils.trimToEmpty(entryStatuses.get(item.getEntryStatus())));
            map.put("activityTime", item.getActivityTime()==null?"":sdfTime.format(item.getActivityTime()));
            map.put("entryTime", item.getEntryTime()==null?"":sdfTime.format(item.getEntryTime()));
            map.put("remark", item.getRemark());
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"activeOrder","merchantNo","userName","userCode","brandCode","oneAgentNo","oneUserName","oneUserCode"
                ,"activityCode","activityTypeNo","transAmount","cashBackAmount","entryStatus","activityTime","entryTime","remark"};
        String[] colsName = new String[]{"激活订单号","商户编号","盟主姓名","盟主编号","所属品牌","一级代理商编号","所属机构名称","所属机构编号",
                "欢乐返类型","欢乐返子类型","交易金额(元)","返盟主金额(元)","返现入账状态","激活日期","入账日期","备注"};
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出盟主活动返现明细失败,param:{}",JSONObject.toJSONString(info));
            e.printStackTrace();
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
