package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.TerminalBackDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.TerminalBack;
import cn.eeepay.framework.service.allAgent.TerminalBackService;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service("terminalBackService")
public class TerminalBackServiceImpl implements TerminalBackService {
    private static final Logger log = LoggerFactory.getLogger(TerminalBackServiceImpl.class);

    @Resource
    private TerminalBackDao terminalBackDao;

    public List<TerminalBack> queryTerminalBackList(TerminalBack info, Page<TerminalBack> page){
        List<TerminalBack> list=terminalBackDao.queryTerminalBackList(info,page);
        return list;
    }

    public List<Map<String, Object>> queryTerminalBackSN(String orderNo){
        return terminalBackDao.queryTerminalBackSN(orderNo);
    }

    public void exportTerminalBack(TerminalBack info, HttpServletResponse response)  throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;

        Page<TerminalBack> page = new Page<>();
        page.setPageSize(Integer.MAX_VALUE);
        List<TerminalBack> list = queryTerminalBackList(info,page);
        String fileName = "SN号回拨记录"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;

        Map<String, String> userTypeMap = new HashMap<>();
        userTypeMap.put("1", "机构");
        userTypeMap.put("2", "大盟主");
        userTypeMap.put("3", "盟主");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("0", "等待接收");
        statusMap.put("1", "回拨成功");
        statusMap.put("2", "拒绝接收");
        statusMap.put("3", "已取消");
        for(TerminalBack item: list){
            map = new HashMap<>();
            map.put("orderNo", item.getOrderNo());
            map.put("count", item.getCount()+"");
            map.put("userCode", item.getUserCode());
            map.put("receiveUserCode", item.getReceiveUserCode());
            map.put("receiveUserType", StringUtils.trimToEmpty(userTypeMap.get(item.getReceiveUserType()+"")));
            map.put("oneUserCode", item.getOneUserCode());
            map.put("status", StringUtils.trimToEmpty(statusMap.get(item.getStatus()+"")));
            map.put("createTime", item.getCreateTime()==null?"":sdfTime.format(item.getCreateTime()));
            map.put("lastUpdateTime", (item.getLastUpdateTime()==null||(item.getStatus()!=1&&item.getStatus()!=2&&item.getStatus()!=3))?"":sdfTime.format(item.getLastUpdateTime()));
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","count","userCode","receiveUserCode","receiveUserType"
                ,"oneUserCode","status","createTime","lastUpdateTime"};
        String[] colsName = new String[]{"回拨单号","回拨数量","回拨盟主编号","接收盟主编号","接收盟主类型"
                ,"所属机构编号","回拨状态","回拨日期","处理日期"};
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出SN号回拨记录失败,param:{}",JSONObject.toJSONString(info));
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
