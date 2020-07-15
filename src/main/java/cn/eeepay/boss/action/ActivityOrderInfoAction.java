package cn.eeepay.boss.action;


import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.ActivityOrderInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/activityOrder")
public class ActivityOrderInfoAction {

    private static final Logger log = LoggerFactory.getLogger(ActivityOrderInfoAction.class);

    @Resource
    private ActivityOrderInfoService activityOrderInfoService;

    @Resource
    private SysDictService sysDictService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @ResponseBody
    @RequestMapping("/actOrderInfoQuery")
    public Map<String, Object> actOrderInfoQuery(@RequestParam String info, @Param("page") Page<Map<String, Object>> page) {
        Map<String, Object> res = new HashMap<>();
        JSONObject json = JSONObject.parseObject(info);
        Map params = json;
        activityOrderInfoService.actOrderInfoQuery(params, page);
        //如果为大POS，交易金额及交易状态值取自activity_order_info
        List<Map<String,Object>> list = page.getResult();
        for (Map<String, Object> map : list) {
            if(map.get("couponCode").equals("11")){
                map.put("transAmount",map.get("aoiTransAmount"));
                map.put("transStatus",map.get("aoiTransStatus"));
            }
        }
        res.put("page", page);
        return res;
    }


    @ResponseBody
    @RequestMapping("/actOrderInfo")
    public Map<String, Object> actOrderInfo(@RequestParam String id) {
        Map<String, Object> res = new HashMap<>();
        Map<String,Object> infoDetail = activityOrderInfoService.actOrderInfo(id);

        String couponNo = "";
        String payOrderNo = "";
        if(infoDetail !=null){
            couponNo = StringUtil.filterNull(infoDetail.get("couponNo"));
            payOrderNo = StringUtil.filterNull(infoDetail.get("payOrderNo"));

            infoDetail.put("merInfo",StringUtil.filterNull(infoDetail.get("merchantName"))
                    +"("+StringUtil.filterNull(infoDetail.get("merchantNo"))+")");
            if(infoDetail.get("couponCode").equals("11")){
                log.info("大POS购买鼓励金");
                infoDetail.put("transAmount",infoDetail.get("aoiTransAmount"));
                infoDetail.put("transStatus",infoDetail.get("aoiTransStatus"));
            }
        }

        Map<String,Object> couponInfo = activityOrderInfoService.queryCouponInfo(couponNo);
        if(couponInfo!=null){
            String startTime = StringUtil.filterNull(couponInfo.get("startTime"));
            String endTime = StringUtil.filterNull(couponInfo.get("endTime"));
            String startEndTime  = startTime.substring(0,10)+"~"+endTime.substring(0,10);
            couponInfo.put("startEndTime",startEndTime);
        }

        Map<String,Object> settleInfo = activityOrderInfoService.actOrderSettleInfoQuery(payOrderNo);

        res.put("infoDetail", infoDetail);
        res.put("couponInfo", couponInfo);
        res.put("settleInfo", settleInfo);

        return res;
    }

    @ResponseBody
    @RequestMapping("/actOrderInfoCount")
    public Map<String, Object> actOrderInfoCount(@RequestParam String info, @Param("page") Page<Map<String, Object>> page) {
        Map<String, Object> res = new HashMap<>();
        JSONObject json = JSONObject.parseObject(info);
        Map params = json;
        Map<String, Object> total = activityOrderInfoService.actOrderInfoCount(params);
        res.put("total", total);
        return res;
    }
    @ResponseBody
    @RequestMapping("/actOrderRemarkUpdate")
    public int  actOrderRemarkUpdate(@RequestParam Map<String,String> params, @Param("page") Page<Map<String, Object>> page) {
        int result = activityOrderInfoService.actOrderRemarkUpdate(params);
        return result;
    }

    @RequestMapping("/actOrderInfoExport")
    @ResponseBody
    public void actOrderInfoExport(@RequestParam String info, HttpServletResponse response) {

        JSONObject json = JSONObject.parseObject(info);
        Map params = json;
        log.info("[{}]",params);
        List<Map<String, Object>> listData = activityOrderInfoService.actOrderInfoExport(params);
        for (Map<String, Object> map : listData) {
            if(map.get("couponCode").equals("11")){
                map.put("transAmount",map.get("aoiTransAmount"));
                map.put("transStatus",map.get("aoiTransStatus"));
            }
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileName = "充值返购买记录" + sdf.format(new Date()) + ".xls";
            String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
            List<Map<String, String>> data = new ArrayList<>();
            if (listData.size() < 1) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id", null);
                maps.put("orderNo", null);
                maps.put("merchantNo", null);
                maps.put("merchantName", null);
                maps.put("agentName", null);
                maps.put("oneAgentName", null);
                maps.put("mobileNo", null);
                maps.put("transAmount", null);
                maps.put("couponCode", null);
                maps.put("transStatus", null);
                maps.put("merAccNo", null);
                maps.put("transTime", null);
                maps.put("payMethod", null);
                maps.put("payOrderNo", null);
                maps.put("remark", null);
                data.add(maps);
            } else {
                for (Map<String,Object> i : listData) {
                    String couponCode = "";
                    String transStatus = "";
                    if (i.get("couponCode") != null) {
                        SysDict sysDict = activityOrderInfoService.sysDict("COUPON_CODE",i.get("couponCode").toString());
                        couponCode = sysDict.getSysName();
                    }
                    if (i.get("transStatus") != null) {
                        SysDict sysDict = activityOrderInfoService.sysDict("TRANS_STATUS",i.get("transStatus").toString());
                        transStatus = sysDict.getSysName();
                    }
                    Map<String, String> maps = new HashMap<String, String>();
                    maps.put("id", null == i.get("id") ? "" :i.get("id").toString());
                    maps.put("orderNo", null == i.get("orderNo") ? "" :i.get("orderNo").toString());
                    maps.put("merchantNo", null == i.get("merchantNo") ? "" :i.get("merchantNo").toString());
                    maps.put("merchantName", null == i.get("merchantName") ? "" :i.get("merchantName").toString());
                    maps.put("agentName", null == i.get("agentName") ? "" :i.get("agentName").toString());
                    maps.put("oneAgentName", null == i.get("oneAgentName") ? "" :i.get("oneAgentName").toString());
                    maps.put("mobileNo", null == i.get("mobileNo") ? "" :i.get("mobileNo").toString());
                    maps.put("transAmount", null == i.get("transAmount") ? "0" :i.get("transAmount").toString());
                    maps.put("couponCode", couponCode);
                    maps.put("transStatus", transStatus);
                    maps.put("merAccNo", null == i.get("merAccNo") ? "" :i.get("merAccNo").toString());
                    maps.put("transTime", null == i.get("transTime") ? "" : sdf1.format(i.get("transTime")));
                    String payMethodValue = "";
                    String payMethod = (String)i.get("payMethod");
                    if(StringUtils.isNotBlank(payMethod)){
                        if("1".equals(payMethod)){
                            payMethodValue = "POS";
                        }
                        if("2".equals(payMethod)){
                            payMethodValue = "支付宝";
                        }
                        if("3".equals(payMethod)){
                            payMethodValue = "微信";
                        }
                        if("4".equals(payMethod)){
                            payMethodValue = "快捷";
                        }
                    }
                    maps.put("payMethod",payMethodValue);
                    maps.put("payOrderNo", null == i.get("payOrderNo") ? "" :i.get("payOrderNo").toString());
                    maps.put("remark", null == i.get("remark") ? "" :i.get("remark").toString());
                    data.add(maps);
                }
            }
            ListDataExcelExport export = new ListDataExcelExport();
            String[] cols = new String[]{"id", "orderNo", "merchantNo", "merchantName","agentName","oneAgentName", "mobileNo", "transAmount",
                    "couponCode", "transStatus", "merAccNo", "transTime", "payMethod","payOrderNo", "remark"};
            String[] colsName = new String[]{"序号", "业务订单编号", "商户编号", "商户名称","所属代理商","一级代理商","商户手机号", "交易金额",
                    "订单类型", "订单状态","收款商户", "交易时间", "支付方式","支付订单号", "备注"};
            OutputStream ouputStream = response.getOutputStream();
            export.export(cols, colsName, data,ouputStream );
            ouputStream.close();
        }catch (Exception e){
            log.error("",e);
        }


    }

}
