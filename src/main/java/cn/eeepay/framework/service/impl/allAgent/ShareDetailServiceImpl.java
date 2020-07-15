package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.ShareDetailDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.ShareDetail;
import cn.eeepay.framework.service.allAgent.ShareDetailService;
import cn.eeepay.framework.util.ListDataExcelExport;
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

@Service("shareDetailService")
public class ShareDetailServiceImpl implements ShareDetailService {
    private static final Logger log = LoggerFactory.getLogger(ShareDetailServiceImpl.class);

    @Resource
    private ShareDetailDao shareDetailDao;

    public List<ShareDetail> queryShareDetailList(ShareDetail info, Page<ShareDetail> page){
        List<ShareDetail> list=shareDetailDao.queryShareDetailList(info,page);
        return list;
    }

    public Map<String, Object> queryShareDetailCount(ShareDetail info){
        return shareDetailDao.queryShareDetailCount(info);
    }

    public void exportShareDetail(ShareDetail info, HttpServletResponse response)  throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;

        Page<ShareDetail> page = new Page<>();
        page.setPageSize(Integer.MAX_VALUE);
        List<ShareDetail> list = queryShareDetailList(info,page);
        String fileName = "盟主分润明细"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;

        Map<String, String> shareTypeMap = new HashMap<>();
        shareTypeMap.put("1", "固定收益");
        shareTypeMap.put("2", "交易分润(标准)");
        shareTypeMap.put("3", "管理津贴");
        shareTypeMap.put("4", "成长津贴");
        shareTypeMap.put("5", "王者奖金");
        shareTypeMap.put("6", "荣耀奖金");
        shareTypeMap.put("7", "机具分润");
        shareTypeMap.put("8", "交易分润(VIP)");
        Map<String, String> userTypeMap = new HashMap<>();
        userTypeMap.put("1", "机构");
        userTypeMap.put("2", "大盟主");
        userTypeMap.put("3", "盟主");
        Map<String, String> accStatusMap = new HashMap<>();
        accStatusMap.put("NOENTERACCOUNT", "未入账");
        accStatusMap.put("ENTERACCOUNTED", "已入账");

        for(ShareDetail item: list){
            map = new HashMap<>();
            map.put("shareAmount", item.getShareAmount()==null?"":item.getShareAmount().toString());
            map.put("shareType", StringUtils.trimToEmpty(shareTypeMap.get(item.getShareType())));
            map.put("teamTotalAmount", item.getTeamTotalAmount()==null?"":item.getTeamTotalAmount().toString());
            map.put("totalAmount", item.getTotalAmount()==null?"":item.getTotalAmount().toString());
            map.put("transAmount", item.getTransAmount()==null?"":item.getTransAmount().toString());
            map.put("transNo", item.getTransNo());
            map.put("userType", StringUtils.trimToEmpty(userTypeMap.get(item.getUserType())));
            map.put("realName", item.getRealName());
            map.put("userCode", item.getUserCode());
            map.put("shareLevel", item.getShareLevel()==null?"":"Lv."+item.getShareLevel());
            map.put("shareRatio", item.getShareType().equals("2")?item.getShareRatio()==null?"":item.getShareRatio().toString():"");
            map.put("vipShareRatio", item.getShareType().equals("8")?item.getShareRatio()==null?"":item.getShareRatio().toString():"");
            map.put("transShareRatio", item.getShareType().equals("6")?item.getShareRatio()==null?"":item.getShareRatio().toString():"");
            map.put("brandName", item.getBrandName());
            map.put("twoUserCode", item.getTwoUserCode());
            map.put("oneUserCode", item.getOneUserCode());
            map.put("shareMonth", item.getShareMonth());
            map.put("createTime", item.getCreateTime()==null?"":sdfTime.format(item.getCreateTime()));
            map.put("accStatus", StringUtils.trimToEmpty(accStatusMap.get(item.getAccStatus())));
            map.put("accMessage", item.getAccMessage());
            map.put("accTime", item.getAccTime()==null?"":sdfTime.format(item.getAccTime()));
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"shareAmount","shareType","teamTotalAmount","totalAmount","transAmount","transNo"
                ,"userType","realName","userCode","shareLevel","shareRatio","vipShareRatio","transShareRatio","brandName"
                ,"twoUserCode","oneUserCode","shareMonth","createTime","accStatus","accMessage","accTime"};
        String[] colsName = new String[]{"分润金额(元)","分润类别","当月团队总流水(元)","当月直营商户总流水(元)","交易金额","交易订单号"
                ,"用户类别","用户名称","盟主编号","标准分润等级","标准分润比例","VIP分润比例","荣耀奖金分润比例","所属品牌"
                ,"所属大盟主编号","所属机构编号","分润归属月份","分润创建时间","入账状态","入账信息","入账时间"};
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出盟主分润明细失败,param:{}",JSONObject.toJSONString(info));
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
