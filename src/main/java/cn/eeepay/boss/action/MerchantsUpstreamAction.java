package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayOrderDetail;
import cn.eeepay.framework.model.MerchantsUpstream;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.ZqMerchantInfoService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author liuks
 * @date 2017-11-15
 * 商户上游账户查询
 */
@Controller
@RequestMapping(value="/merchantsUpstream")
public class MerchantsUpstreamAction {

    public static final String ZQSFT="SFT_ZQ";//直清盛付通通道编号

    public static final String ZFZQURL="ZFZQ_ACCESS_URL";//接口请求根路径

    private static final Logger log = LoggerFactory.getLogger(MerchantsUpstreamAction.class);

    @Resource
    private ZqMerchantInfoService zqMerInfoService;

    @Resource
    private SysDictService sysDictService;

    /**
     * 条件商户上游账户查询
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam.do")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<MerchantsUpstream> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            MerchantsUpstream me = JSONObject.parseObject(param, MerchantsUpstream.class);
            List<String> list=parseString(me.getUnionpayMerNo());
            List<String> inlist=new ArrayList<String>();//直清表存在
            List<String> unlist=new ArrayList<String>();//直清表不存在
            if(list!=null&&list.size()>0){
                if(list.size()>50){
                    msg.put("msg","银联报备商户编号不能超过50个!");
                    msg.put("status",false);
                    return msg;
                }
                for(String str:list){
                    boolean istrue=zqMerInfoService.selectMerchantsUpstreamByUnionpayMerNo(str);
                    if(istrue){
                        inlist.add(str);
                    }else{
                        unlist.add(str);
                    }
                }

            }
            if(!(unlist.size()>0&&inlist.size()<=0)){
                //获取数据
                getDataList(page,inlist,page.getFirst(),true);
            }

            msg.put("page",page);
            msg.put("unlist",listToString(unlist));
            msg.put("status",true);
        } catch (Exception e){
            log.error("商户上游账户查询失败!",e);
            msg.put("msg","商户上游账户查询失败!");
            msg.put("status",false);
        }
        return msg;
    }


    /**
     * 导出商户上游账户查询
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/exportDetail")
    @ResponseBody
    public Map<String, Object> exportDetail(@RequestParam("baseInfo") String param, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        MerchantsUpstream me = JSONObject.parseObject(param, MerchantsUpstream.class);
        List<String> list=parseString(me.getUnionpayMerNo());
        List<String> inlist=new ArrayList<String>();//直清表存在
        List<String> unlist=new ArrayList<String>();//直清表不存在
        try {
            if(list!=null&&list.size()>0){
                if(list.size()>50){
                    msg.put("msg","银联报备商户编号不能超过50个!");
                    msg.put("status",false);
                    return msg;
                }
                for(String str:list){
                    boolean istrue=zqMerInfoService.selectMerchantsUpstreamByUnionpayMerNo(str);
                    if(istrue){
                        inlist.add(str);
                    }else{
                        unlist.add(str);
                    }
                }
            }
            List<MerchantsUpstream> dataList=null;
            if(!(unlist.size()>0&&inlist.size()<=0)){
                //获取数据
                dataList= getDataList(null, inlist, 0, false);
            }
            exportDetail(dataList, response, request);
            msg.put("status", true);

        } catch (Exception e){
            log.error("导出商户上游账户查询失败!",e);
            msg.put("msg","导出商户上游账户查询失败!");
            msg.put("status",false);
        }
        return msg;
    }

    private void exportDetail(List<MerchantsUpstream> list,HttpServletResponse response, HttpServletRequest request) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        String fileName = "商户上游账户查询"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list==null||list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("rowNo",null);
            maps.put("channelCode",null);
            maps.put("unionpayMerNo",null);
            maps.put("mbpId",null);
            maps.put("merchantNo",null);
            maps.put("merchantName",null);
            maps.put("mobilephone",null);
            maps.put("balance",null);
            maps.put("bankAccount",null);
            maps.put("bankCode",null);
            maps.put("bankName",null);
            maps.put("wxRates",null);
            maps.put("zfbRates",null);
            maps.put("result",null);
            maps.put("errorMsg",null);
            data.add(maps);
        }else{
            for (MerchantsUpstream or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("rowNo",or.getRowNo()<=0?"":or.getRowNo()+"");
                maps.put("channelCode",or.getChannelCode()==null?"":or.getChannelCode());
                maps.put("unionpayMerNo",or.getUnionpayMerNo()==null?"":or.getUnionpayMerNo());
                maps.put("mbpId",or.getMbpId()==null?"":or.getMbpId());
                maps.put("merchantNo",or.getMerchantNo()==null?"":or.getMerchantNo());
                maps.put("merchantName",or.getMerchantName()==null?"":or.getMerchantName());
                maps.put("mobilephone",or.getMobilephone()==null?"":or.getMobilephone());
                maps.put("balance",or.getBalance()==null?"":or.getBalance());
                maps.put("bankAccount",or.getBankAccount()==null?"":or.getBankAccount());
                maps.put("bankCode",or.getBankCode()==null?"":or.getBankCode());
                maps.put("bankName",or.getBankName()==null?"":or.getBankName());
                maps.put("wxRates",or.getWxRates()==null?"":or.getWxRates());
                maps.put("zfbRates",or.getZfbRates()==null?"":or.getZfbRates());
                maps.put("result",or.getResult()==null?"":or.getResult());
                maps.put("errorMsg",or.getErrorMsg()==null?"":or.getErrorMsg());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"rowNo","channelCode","unionpayMerNo","mbpId","merchantNo","merchantName",
                "mobilephone","balance","bankAccount","bankCode","bankName","wxRates","zfbRates","result","errorMsg"
        };

        String[] colsName = new String[]{"序号","通道名称","银联报备商户编号","商户进件编号","商户编号","商户名称",
                "手机号","账户余额","银行账号","银行编码","银行名称","微信费率档","支付宝费率档","查询结果","错误描述"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出商户上游账户查询!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }


    /**
     * 查询数据，对当前页数据分页处理
     * @param page 分页
     * @param inlist 条件list
     * @param start 开始的记录数
     * @throws Exception
     */
    private List<MerchantsUpstream> getDataList(Page<MerchantsUpstream> page,List<String> inlist,int start,boolean state) throws Exception{
        List<MerchantsUpstream> dataList=null;
        if(state){
            dataList=zqMerInfoService.selectAllMerchantsUpstream(page,inlist,start);
        }else{
            dataList=zqMerInfoService.exportDetail(inlist,start);
        }
        if(dataList!=null&&dataList.size()>0){
            SysDict sysDict = sysDictService.getByKey(MerchantsUpstreamAction.ZFZQURL);
            for(MerchantsUpstream mu:dataList){
                if(MerchantsUpstreamAction.ZQSFT.equals(mu.getChannelCode())){
                    //请求接口 传入 数据字典的基础url和银联报备商户号
                    String returnMsg = ClientInterface.sftAccountSearch(sysDict.getSysValue(),mu.getUnionpayMerNo());
                    //数据返回接口demo   93748092
                    //成功:{ "body" : "{\"balance\":\"0.00\",\"bankAccount\":\"623094*********4977\",\"bankCode\":\"CCB\",\"bankName\":\"建设银行\",\"channelFeelevel\":\"{\\\"TX01\\\":\\\"\\\",\\\"ZFB01\\\":\\\"\\\"}\",\"channelID\":\"93746552\",\"mcc\":\"9999\",\"merchantId\":\"93748092\"}","header" :{ "errMsg" : "请求成功","error" : "温馨提示","succeed":TRUE}}
                    //失败:{"header":{"errMsg":"系统异常","error":"温馨提示","succeed":false}}
                    Map<String, Object> json = JSON.parseObject(returnMsg);
                    Map<String, Object> header = JSON.parseObject(String.valueOf(json.get("header")));
                    if(json.get("header")!=null){
                        boolean succeed=Boolean.valueOf(String.valueOf(header.get("succeed")));
                        Map<String, Object> body = JSON.parseObject(String.valueOf(json.get("body")));
                        if(succeed&&body!=null){
                            mu.setBalance(String.valueOf(body.get("balance")));
                            mu.setBankAccount(String.valueOf(body.get("bankAccount")));
                            mu.setBankCode(String.valueOf(body.get("bankCode")));
                            mu.setBankName(String.valueOf(body.get("bankName")));
                            Map<String, Object> rates = JSON.parseObject(String.valueOf(body.get("channelFeelevel")));
                            if(rates!=null){
                                mu.setWxRates(String.valueOf(rates.get("TX01")));
                                mu.setZfbRates(String.valueOf(rates.get("ZFB01")));
                            }
                            mu.setResult("成功");
                            mu.setErrorMsg(String.valueOf(header.get("errMsg")));
                        }else{
                            mu.setResult("失败");
                            mu.setErrorMsg(String.valueOf(header.get("errMsg")));
                        }
                    }else{
                        mu.setResult("失败");
                        mu.setErrorMsg("接口请求失败");
                    }
                }else{
                    mu.setResult("失败");
                    mu.setErrorMsg("功能未开通");
                }
            }
            if(page!=null){
                page.setResult(dataList);
            }
        }
        return dataList;
    }

    private String listToString(List<String> list){
        if(list!=null&&list.size()>0){
            StringBuilder sb = new StringBuilder();
            for(String str:list){
                sb.append(str+",");
            }
            sb.setLength(sb.length()-1);
            return sb.toString();
        }
        return null;
    }
    /**
     * 解析xxx1,xxx2,xxx3 字符转换成集合list
     * @param str
     * @return
     */
    private List<String> parseString(String str) throws Exception{
        if(str!=null&&!"".equals(str)){
            String[] strs=str.trim().split(",");
            List<String> list=Arrays.asList(strs);
            return list;
        }
        return null;
    }
}
