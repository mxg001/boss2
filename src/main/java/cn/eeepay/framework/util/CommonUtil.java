package cn.eeepay.framework.util;

import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.UserLoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    private static final Pattern patternYMD = Pattern.compile("^(\\d){4}-(\\d){1,2}-(\\d){1,2}$");
    private static final Pattern patternYMDHMS = Pattern.compile("^(\\d){4}-(\\d){1,2}-(\\d){1,2} (\\d){1,2}:(\\d){1,2}:(\\d){1,2}$");
    private static final Pattern patternYYYYMMDD = Pattern.compile("^(\\d){8}$");


    /**
     * 获取当前登录的UserLoginInfo
     * @return
     */
    public static UserLoginInfo getLoginUser(){
        UserLoginInfo userInfo = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null){
            userInfo = principal;
        } else {
            throw new BossBaseException("用户未登录");
        }
        return userInfo;
    }

    /**
     * 获取当前登录的UserLoginInfo
     * @return
     */
    public static String getLoginUserName(){
        UserLoginInfo userInfo = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null){
            userInfo = principal;
        } else {
            throw new BossBaseException("用户未登录");
        }
        return userInfo.getRealName();
    }

    /**
     * 将图片的地址转换为阿里云的地址，boss-temp
     * @param imgUrl
     * @return
     */
    public static String getImgUrl(String imgUrl){
        if(StringUtils.isBlank(imgUrl)){
            return imgUrl;
        }
        if(imgUrl.startsWith("http")){
            return imgUrl;
        }
        Long dateTime = new Date().getTime() + 60*60*1000L;
        Date date = new Date(dateTime);
        imgUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_TEMP_TUCKET, imgUrl, date);
        return imgUrl;
    }

    /**
     * 将图片的地址转换为阿里云的地址，agent-attch
     * @param imgUrl
     * @return
     */
    public static String getImgUrlAgent(String imgUrl){
        if(StringUtils.isBlank(imgUrl)){
            return imgUrl;
        }
        if(imgUrl.startsWith("http")){
            return imgUrl;
        }
        Long dateTime = new Date().getTime() + 10*60*1000L;
        Date date = new Date(dateTime);
        imgUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, imgUrl, date);
        return imgUrl;
    }
    /**
     * 将图片的地址转换为阿里云的地址，agent-attch
     * 超长时间
     */
    public static String getImgUrlAgentLongTime(String imgUrl){
        if(StringUtils.isBlank(imgUrl)){
            return imgUrl;
        }
        if(imgUrl.startsWith("http")){
            return imgUrl;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 99);
        Date date=cal.getTime();
        imgUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, imgUrl, date);
        return imgUrl;
    }

    /**
     * 校验excel file的大小和格式
     * @param file
     * @param result
     * @return
     */
    public static boolean checkExcelFile(MultipartFile file, Result result) {
        if(file == null){
            result.setMsg("请选择上传文件");
            return false;
        }
        if (!file.isEmpty()) {
            if(((file.getSize()/1024)/1024) > 4){
                result.setMsg("上传文件过大,请上传4MB以内的文件");
                return false;
            }
            String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(!format.equals(".xls") && !format.equals(".xlsx")){
                result.setMsg("请选择.xls或.xlsx文件");
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param params
     *            参数集合
     * @param signKey
     *            参与签名秘钥
     * @return sign
     */
    public final static String sortASCIISign(Map<String, String> params, String signKey) {
        StringBuffer signStr = new StringBuffer();
        // 所有参与传参的参数按照ASCII排序（升序）
        TreeMap<String, Object> tempMap = new TreeMap<String, Object>();
        for (String  key : params.keySet()) {
            if (StringUtils.isNotBlank(String.valueOf(params.get(key)))) {
                tempMap.put(key, params.get(key));
            }
        }
        for (String key : tempMap.keySet()) {
            signStr.append(key).append("=").append(tempMap.get(key)).append("&");
        }
        signStr = signStr.append("key=").append(signKey);
        // MD5加密,结果转换为大写字符
        String sign = Md5.md5Str(signStr.toString()).toUpperCase();
        return sign;
    }

    public static Map<String, Object> getMonthMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("一月", "01");
        map.put("二月", "02");
        map.put("三月", "03");
        map.put("四月", "04");
        map.put("五月", "05");
        map.put("六月", "06");
        map.put("七月", "07");
        map.put("八月", "08");
        map.put("九月", "09");
        map.put("十月", "10");
        map.put("十一月", "11");
        map.put("十二月", "12");
        map.put("1", "01");
        map.put("2", "02");
        map.put("3", "03");
        map.put("4", "04");
        map.put("5", "05");
        map.put("6", "06");
        map.put("7", "07");
        map.put("8", "08");
        map.put("9", "09");
        map.put("01", "01");
        map.put("02", "02");
        map.put("03", "03");
        map.put("04", "04");
        map.put("05", "05");
        map.put("06", "06");
        map.put("07", "07");
        map.put("08", "08");
        map.put("09", "09");
        map.put("10", "10");
        map.put("11", "11");
        map.put("12", "12");
        map.put("Jan", "01");
        map.put("Feb", "02");
        map.put("Mar", "03");
        map.put("Apr", "04");
        map.put("May", "05");
        map.put("Jun", "06");
        map.put("Jul", "07");
        map.put("Aug", "08");
        map.put("Sep", "09");
        map.put("Oct", "10");
        map.put("Nov", "11");
        map.put("Dec", "12");
        return map;
    }

    public static Date getFormatDate(String dateStr) {
        if(dateStr == null){
            return null;
        }
        Date date = null;
        Matcher matcherYMDHMS = patternYMDHMS.matcher(dateStr);
        if(matcherYMDHMS.matches()){
            date = DateUtil.parseLongDateTime(dateStr);
            return date;
        }
        Matcher matcherYMD = patternYMD.matcher(dateStr);
        if(matcherYMD.matches()){
            date = DateUtil.parseDateTime(dateStr);
            return date;
        }
        Matcher matcherYYYYMMDD = patternYYYYMMDD.matcher(dateStr);
        if(matcherYYYYMMDD.matches()){
            try {
                date = DateUtil.convertShortDate(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
        String[] issueDateArr = dateStr.split("-");
        if (issueDateArr.length < 3){
            return null;
        }
        Map<String, Object> monthMap = getMonthMap();

        dateStr = issueDateArr[2] + "-" + monthMap.get(issueDateArr[1]) + "-" + issueDateArr[0];
        date = DateUtil.parseDateTime(dateStr);
        return date;
    }
}
