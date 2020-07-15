package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 外放给贷款的接口
 * Created by Administrator on 2017/10/24.
 */

@Controller
@RequestMapping(value = "/loanAction")
public class LoanAction {
    private static final String LOAN_KEK = "2AZE1LQZ70QKHWZ0DQRSPJB5DHMCXV3A";//固定密钥
    private final static Pattern pattern = Pattern.compile("\\d+");
    private final static Map<String,String> limitMap = new HashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private MerchantInfoService merchantInfoService; //商户信息
    @Resource
    private MerchantRequireItemService merchantRequireItemService;//身份信息
    @Resource
    private MerchantCardInfoService merchantCardInfoService;//卡信息
    @Resource
    private AgentInfoService agentInfoService;//代理商
    @Resource
    private AgentInfoDao agentInfoDao;
    @Resource
    private TransInfoService transInfoService;
    @Resource
    private RiskRollService riskRollService;


    /**
     * 根据商户号查询商户信息
     * @param merchantNo 商户号
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/getMerItems",method = RequestMethod.GET)
    @ResponseBody
    public String getMerItems(String merchantNo){
        Map<String, Object> msgMap= new HashMap<>();
        MerchantInfo mer=merchantInfoService.selectStatusByMerNo(merchantNo);
        AgentInfo agent = agentInfoService.selectByagentNo(mer.getAgentNo());
        List<MerchantRequireItem> itemsList = merchantRequireItemService.getItemByMerId(merchantNo);
        MerchantCardInfo merchantCardInfo = merchantCardInfoService.selectByMertId(merchantNo);

        if (null !=mer){
            msgMap.put("mobilephone",mer.getMobilephone());
            msgMap.put("merchantNo",mer.getMerchantNo());
            msgMap.put("merchantName",mer.getMerchantName());
            msgMap.put("lawyer",mer.getLawyer());
            msgMap.put("merchantType",mer.getMerchantType());

            msgMap.put("status",mer.getProductStatus());
            msgMap.put("createTime",mer.getCreateTime());
            if (null !=agent){
                String OneLevelName = agentInfoDao.selectNameById(agent.getOneLevelId());
                msgMap.put("agentNo",agent.getAgentNo());
                msgMap.put("agentName",agent.getAgentName());
                msgMap.put("oneLevelId",agent.getOneLevelId());
                msgMap.put("oneLevelName",OneLevelName);
            }
            if (null !=itemsList){
                for (MerchantRequireItem item:itemsList) {
                    if (null !=item.getContent()) {
                        String content = item.getContent();
                        Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                        if ("6".equals(item.getMriId())) {
                            msgMap.put("idCardNo", content); //身份证
                            continue;
                        } else if ("9".equals(item.getMriId())) { //身份证下面照
                            msgMap.put("idFrontImg", ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate));
                            continue;
                        } else if ("10".equals(item.getMriId())) { //身份证反面照
                            msgMap.put("idBackImg", ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate));
                            continue;
                        } else if ("8".equals(item.getMriId())) { //手持身份证
                            msgMap.put("idHandImg", ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate));
                            continue;
                        } else if ("16".equals(item.getMriId())) {//活体照片
                            msgMap.put("idLiveImg", ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate));
                            continue;
                        }else if ("11".equals(item.getMriId())) { //银行卡正面
                            msgMap.put("idAccountImg", ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate));
                            continue;
                        }
                    }
                }
            }
            if (null !=merchantCardInfo){
                msgMap.put("accountType",merchantCardInfo.getAccountType());
                msgMap.put("cardType",merchantCardInfo.getCardType());
                msgMap.put("bankName",merchantCardInfo.getBankName());
                msgMap.put("accountName",merchantCardInfo.getAccountName());
                msgMap.put("accountNo",merchantCardInfo.getAccountNo());
                msgMap.put("cnapsNo",merchantCardInfo.getCnapsNo());
                msgMap.put("province",merchantCardInfo.getAccountProvince());
                msgMap.put("city",merchantCardInfo.getAccountCity());
                msgMap.put("district",merchantCardInfo.getAccountDistrict());
            }
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(msgMap);
        //return jsonObject.toString();
        return encode(jsonObject.toString(), LOAN_KEK);
    }

    /**
     * 查询交易总数
     * @param merchantNo 商户号
     * @param startTime 时间段
     * @param endTime 时间段
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/getAmountAndNum",method = RequestMethod.GET)
    @ResponseBody
    public String getAmountAndNum(String merchantNo , String startTime ,String endTime){
        Map<String, Object> msgMap = new HashMap<>();
        msgMap = transInfoService.getAmountAndNum(merchantNo,startTime,endTime);
        msgMap.put("merchantNo", merchantNo);
        if (null == msgMap){
            msgMap.put("amountSum","0");
            msgMap.put("countNum","0");
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(msgMap);
        return encode(jsonObject.toString(), LOAN_KEK);
        //return jsonObject.toString();
    }

    /**
     * 查询所有黑名单
     * @return
     * @throws IOException
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/getBlackList",method = RequestMethod.GET)
    @ResponseBody
    public String getBlackList() throws IOException {
        Map<String, Object> msgMap = new HashMap<>();
        if (limitMap.size()>0){
            try {
                boolean flag = sdf.parse(limitMap.get("time")).before(new Date());
                if (flag == false){
                    limitMap.put("time",sdf.format(new Date()));
                    limitMap.put("limit","1");
                }else {
                    if (Integer.parseInt(limitMap.get("limit"))<3){
                        int limit = Integer.parseInt(limitMap.get("limit"));
                        limitMap.put("limit",String.valueOf(limit + 1));
                    }else {
                        return null;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            limitMap.put("time",sdf.format(new Date()));
            limitMap.put("limit","1");
        }
        List<RiskRoll> list = riskRollService.selectRollAll();
        ObjectMapper mapper = new ObjectMapper();
        StringWriter w = new StringWriter();
        try {
            mapper.writeValue(w, list);
            System.out.println("===========" + w.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encode(w.toString(), LOAN_KEK);
        //return w.toString();
    }

    /**
     * 加密
     * @param src 参数
     * @param key 密钥
     * @return
     */
    public static String encode(String src,String key) {
        try {
            byte[] data = src.getBytes("utf-8");
            byte[] keys = key.getBytes();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                //结合key和相应的数据进行加密操作,ofxx的作用是补码，byte是8bits，而int是32bits
                int n = (0xff & data[i]) + (0xff & keys[i % keys.length]);
                sb.append("&"+n);
            }
            return sb.toString();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return src;
    }

    /**
     * 解密
     * @param src 参数
     * @param key 密钥
     * @return
     */
    public static String decode(String src,String key) {
        if(src == null || src.length() == 0){
            return src;
        }
        //正则表达式字符串匹配
        Matcher m = pattern.matcher(src);

        List<Integer> list = new ArrayList<Integer>();
        //find方法(部分匹配):尝试去发现输入串中是否匹配相应的子串
        while (m.find()) {
            try {
                //返回匹配到的子字符串
                String group = m.group();
                list.add(Integer.valueOf(group));
            } catch (Exception e) {
                e.printStackTrace();
                return src;
            }
        }
        //如果有匹配的字符串
        if (list.size() > 0) {
            try {
                byte[] data = new byte[list.size()];
                byte[] keys = key.getBytes();
                //相对于加密过程的解密过程
                for (int i = 0; i < data.length; i++) {
                    data[i] = (byte) (list.get(i) - (0xff & keys[i % keys.length]));
                }
                return new String(data, "utf-8");
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            return src;
        } else {
            return src;
        }
    }
}
