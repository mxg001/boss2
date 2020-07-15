package cn.eeepay.framework.serviceImpl.couponImport;

import cn.eeepay.framework.model.CardAndReward;
import cn.eeepay.framework.model.CouponActivityEntity;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.couponImport.CouponImport;
import cn.eeepay.framework.model.couponImport.CouponImportCard;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.impl.SensorsService;
import cn.eeepay.framework.util.CellUtil;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2019/6/5/005.
 * @author liuks
 * 新增券导入
 */
@Service("couponImportService")
public class CouponImportServiceImpl implements CouponImportService {

    private static final Logger log = LoggerFactory.getLogger(CouponImportServiceImpl.class);

    //商户信息
    @Resource
    private MerchantInfoService merchantInfoService;

    @Resource
    private CouponActivityService couponActivityService;

    @Resource
    private CardAndRewardManageService cardAndRewardManageService;
    // 商户业务产品
    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;
    @Resource
    private SensorsService sensorsService;

    @Override
    public Map<String, Object> addCouponImport(String param,MultipartFile file) throws Exception{
        Map<String,Object> jsonMap = new HashMap<>();
        Map<String,Object> params = JSONObject.parseObject(param,Map.class);
        if(params == null){
            jsonMap.put("msg","参数有误！");
            jsonMap.put("status", false);
            return jsonMap;
        }
        if(params.get("couponCode")==null||Integer.parseInt(params.get("couponCode").toString())<=0){
            jsonMap.put("msg","参与活动不能为空！");
            jsonMap.put("status", false);
            return jsonMap;
        }
        if(params.get("activityEntityId")==null||Integer.parseInt(params.get("activityEntityId").toString())<=0){
            jsonMap.put("msg","券类型不能为空！");
            jsonMap.put("status", false);
            return jsonMap;
        }
        if(params.get("addNum")==null||Integer.parseInt(params.get("addNum").toString())<=0){
            jsonMap.put("msg","赠送数量不能为空！");
            jsonMap.put("status", false);
            return jsonMap;
        }
        int couponCode = Integer.parseInt(params.get("couponCode").toString());
        int activityEntityId = Integer.parseInt(params.get("activityEntityId").toString());
        int addNum = Integer.parseInt(params.get("addNum").toString());

        if(couponCode>=3 && addNum>1 && couponCode!=15 && couponCode!=16 && couponCode!=17){
            jsonMap.put("msg","该活动类型下,赠送数量只能为1!");
            jsonMap.put("status", false);
            return jsonMap;
        }

        if(1==couponCode||2==couponCode||3==couponCode||6==couponCode||15==couponCode||16==couponCode||17==couponCode){
            excelHandle(couponCode,activityEntityId,addNum,file,jsonMap);
        }else{
            jsonMap.put("msg","该活动类型暂不支持!");
            jsonMap.put("status", false);
        }
        return jsonMap;

    }

    private void excelHandle(int couponCode,int activityEntityId,int addNum,MultipartFile file,Map<String,Object> jsonMap) throws Exception{

        Workbook wb = WorkbookFactory.create(file.getInputStream());
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 遍历所有单元格，读取单元格
        int row_num = sheet.getLastRowNum();

        List<Map<String,Object>> errorList=new ArrayList<Map<String,Object>>();
        List<String> merNoList=new ArrayList<>();//商户号校验
        List<Map<String,String>> checkList=new ArrayList<Map<String,String>>();// 商户号+订单号 校验

        List<CouponImport> addList=new ArrayList<CouponImport>();//需要更新的

        for (int i = 1; i <= row_num; i++) {
            Row row = sheet.getRow(i);
            String merchantNo1 = CellUtil.getCellValue(row.getCell(0));//赠送商户编号
            String merchantName1 = CellUtil.getCellValue(row.getCell(1));//赠送商户名称
            String orderNo1 = CellUtil.getCellValue(row.getCell(2));//关联交易订单号

            Map<String,Object> errorMap=new HashMap<String,Object>();
            int rowNum=i+1;
            CouponImport addItem=new CouponImport();
            addItem.setCouponCode(couponCode);
            addItem.setActivityEntityId(activityEntityId);
            addItem.setAddNum(addNum);
            addItem.setMerchantName(merchantName1);

            if(merchantNo1==null||"".equals(merchantNo1)){
                errorMap.put("msg","第"+rowNum+"行,赠送商户编号为空!");
                errorList.add(errorMap);
                continue;
            }
            String merchantNo=merchantNo1.split("\\.")[0];
            addItem.setMerchantNo(merchantNo);

            if(1==couponCode||2==couponCode){
                if(merNoList.contains(merchantNo)){
                    errorMap.put("msg","第"+rowNum+"行,赠送商户编号("+merchantNo+")重复!");
                    errorList.add(errorMap);
                    continue;
                }else{
                    merNoList.add(merchantNo);
                }
            }

            //商户是否存在
            MerchantInfo oldMer=merchantInfoService.selectByMerNo(merchantNo);
            if(oldMer==null){
                errorMap.put("msg", "第"+rowNum+"行,赠送商户编号("+merchantNo+")不存在!");
                errorList.add(errorMap);
                continue;
            }

            String orderNo="";
            if(couponCode>=3){

                if(couponCode!=15 && couponCode!=16){
                    if(orderNo1==null||"".equals(orderNo1)){
                        errorMap.put("msg","第"+rowNum+"行,关联交易订单号为空!");
                        errorList.add(errorMap);
                        continue;
                    }
                    orderNo=orderNo1.split("\\.")[0];
                    addItem.setOrderNo(orderNo);
                }


                //验证重复
                if(existList(checkList,merchantNo,orderNo)){
                    errorMap.put("msg","导入失败,第"+rowNum+"行,赠送商户编号("+merchantNo+"),关联交易订单号("+orderNo+")重复!");
                    errorList.add(errorMap);
                    continue;
                }else{
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("merchantNo",merchantNo);
                    map.put("orderNo",orderNo);
                    checkList.add(map);
                }

                if(couponCode!=15 && couponCode!=16 && couponCode!=17) {
                    Map<String, Object> activtyOrderInfo = couponActivityService.activtyOrderInfo(orderNo);
                    if (activtyOrderInfo == null) {
                        errorMap.put("msg", "第" + rowNum + "行,关联交易订单号不存在!");
                        errorList.add(errorMap);
                        continue;
                    } else {
                        if (activtyOrderInfo.get("coupon_no") != null) {
                            errorMap.put("msg", "第" + rowNum + "行,该关联交易订单号(" + orderNo + ")关联券已经存在,赠送失败!");
                            errorList.add(errorMap);
                            continue;
                        }
                        if (activtyOrderInfo.get("coupon_code") != null && couponCode != Integer.parseInt(activtyOrderInfo.get("coupon_code").toString())) {
                            errorMap.put("msg", "第" + rowNum + "行,该关联交易订单号(" + orderNo + ")的活动类型和选择的活动类型不一致,赠送失败!");
                            errorList.add(errorMap);
                            continue;
                        }
                        if (activtyOrderInfo.get("merchant_no") != null && !activtyOrderInfo.get("merchant_no").toString().equals(merchantNo)) {
                            errorMap.put("msg", "第" + rowNum + "行,该关联交易订单号(" + orderNo + ")的商户编号和赠送的商户编号" +
                                    "(" + merchantNo + ")不一致,赠送失败!");
                            errorList.add(errorMap);
                            continue;
                        }
                    }
                }else if(couponCode==15 || couponCode==16){
                    Map<String,String> checkMap = new HashMap<>();
                    Map<String,String> checkMapMsg = new HashMap<>();
                    checkMap.put("addNum",addItem.getAddNum()+"");
                    checkMap.put("activityEntityId",addItem.getActivityEntityId()+"");
                    checkMap.put("couponCode",addItem.getCouponCode()+"");
                    checkMap.put("merchantNo",addItem.getMerchantNo());
                    if(!couponActivityService.checkPurchaseCount(checkMap,checkMapMsg)){
                        errorMap.put("msg", "第" + rowNum + "行,"+checkMapMsg.get("msg"));
                        errorList.add(errorMap);
                        continue;
                    }

                }

            }
            //处理需要生成的券
            if(couponCode==15 || couponCode==16 || couponCode==17) {
                for (int j =0;j<addItem.getAddNum();j++){
                    registerCoupon(addItem);
                }
            }
            addList.add(addItem);
        }

        //处理需要生成的券
        if(couponCode!=15 && couponCode!=16 && couponCode!=17) {
            if (addList != null && addList.size() > 0) {
                for (CouponImport item : addList) {
                    for (int i = 0; i < item.getAddNum(); i++) {
                        registerCoupon(item);
                    }
                }
            }

        }

        jsonMap.put("errorList", errorList);
        jsonMap.put("status", true);
        jsonMap.put("msg", "导入成功,总共"+row_num+"条数据,成功导入新增"+addList.size()+" * "+addNum+"张券,失败"+errorList.size()+"条数据!");

    }
    //生成券
    public void registerCoupon(CouponImport addItem) throws Exception {
        String merchantNo = addItem.getMerchantNo();
        int couponCode = addItem.getCouponCode();
        int activityEntityId =addItem.getActivityEntityId();

        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String couDate = sdf.format(new Date());
        String random = StringUtil.filterNull(Math.random());
        random = random.substring(random.indexOf(".")+1, 10);
        couDate = couDate+random;
        Map<String, Object> activityMap = null;
        if(3==couponCode||6==couponCode || 15==couponCode || 16==couponCode || 17==couponCode){
            activityMap = couponActivityService.getActivityById(activityEntityId);
        } else if (1==couponCode) {
            activityMap = couponActivityService.getActivityById2(couponCode+"",activityEntityId);
        } else {
            activityMap =couponActivityService.getActivityByCode(couponCode+"");
        }
        String token = getTokenStr(couDate,couponCode);
        if (activityMap != null && activityMap.size() > 0) {

            String currentDate = DateUtils.getCurrentDate();
            String couponStart = currentDate + " 00:00:00";
            Date couponStartDate = DateUtils.parseDateTime(couponStart);
            int effective_days = Integer.parseInt(activityMap.get("effective_days").toString());
            Date couponEndDate = DateUtils.addDate(couponStartDate, effective_days-1);
            String couponEnd = DateUtils.format(couponEndDate, "yyyy-MM-dd")+ " 23:59:59";
            Date endDate = DateUtils.parseDateTime(couponEnd);

            Map<String, Object> couponMap = new HashMap<String, Object>();
            //充值返金额加赠送金
            if (3==couponCode||6==couponCode) {
                BigDecimal couponAmount = new BigDecimal(activityMap.get("coupon_amount").toString());
                BigDecimal giftAmount = new BigDecimal(activityMap.get("gift_amount").toString());
                BigDecimal faceValue = couponAmount.add(giftAmount);
                couponMap.put("face_value", faceValue);
                couponMap.put("balance", faceValue);
                couponMap.put("coupon_amount", activityMap.get("coupon_amount"));
                couponMap.put("gift_amount", activityMap.get("gift_amount"));
            }else if (15==couponCode || 16==couponCode) {
                BigDecimal couponAmount = new BigDecimal(activityMap.get("coupon_amount")==null?"0":activityMap.get("coupon_amount").toString());
                BigDecimal giftAmount = new BigDecimal(activityMap.get("gift_amount").toString());
                BigDecimal faceValue = couponAmount.add(giftAmount);
                couponMap.put("face_value", faceValue);
                couponMap.put("balance", faceValue);
                couponMap.put("coupon_amount", new BigDecimal(0));
                couponMap.put("gift_amount", activityMap.get("gift_amount"));
            }else if(17==couponCode){
                BigDecimal couponAmount = new BigDecimal(activityMap.get("coupon_amount")==null?"0":activityMap.get("coupon_amount").toString());
                BigDecimal giftAmount = new BigDecimal(activityMap.get("gift_amount").toString());
                BigDecimal faceValue = couponAmount.add(giftAmount);
                couponMap.put("face_value", faceValue);
                couponMap.put("balance", faceValue);
                couponMap.put("coupon_amount", new BigDecimal(0));
                couponMap.put("gift_amount",  activityMap.get("gift_amount"));
                couponMap.put("coupon_standard",activityMap.get("coupon_standard"));
                couponMap.put("back_rate",activityMap.get("back_rate"));
            }else {
                couponMap.put("face_value", activityMap.get("coupon_amount").toString());
                couponMap.put("balance", activityMap.get("coupon_amount").toString());
                couponMap.put("coupon_amount", new BigDecimal(0));
                couponMap.put("gift_amount",  new BigDecimal(0));
            }
            couponMap.put("coupon_code", couponCode);
            couponMap.put("cancel_verification_code", activityMap.get("cancel_verification_code").toString());
            couponMap.put("coupon_status", "1");  // 1 未使用
            couponMap.put("start_time", couponStartDate);
            couponMap.put("end_time", endDate);
            couponMap.put("token", token);
            couponMap.put("merchant_no", merchantNo);
            couponMap.put("activity_first", activityMap.get("activity_first").toString());
            couponMap.put("activity_entity_id", activityEntityId);

            String couponNo = "B"+ couponActivityService.getNext("coupon_no_seq").get("t").toString();

            couponMap.put("coupon_type",activityMap.get("coupon_type"));
            couponMap.put("coupon_no",couponNo)	;
            couponActivityService.addUserCoupon(couponMap);
            if(1==couponCode){
                couponActivityService.updateMerActStatus(merchantNo);
            }else if(3==couponCode||6==couponCode){
                couponActivityService.addCouponOrderInfo(couponNo,addItem.getOrderNo());
            }

            sensorsService.buyCouponDetail(couponMap,couponNo,merchantNo,couponStartDate,endDate,couponCode+"");
        }
    }
    private boolean existList(List<Map<String,String>> list,String merchantNo,String orderNo){
        if(list!=null&&list.size()>0){
            for(Map<String,String> map:list){
                if(map.get("merchantNo").equals(merchantNo)&&map.get("orderNo").equals(orderNo)){
                    return true;
                }
            }
        }
        return false;
    }
    public String  getTokenStr(String tokenKey, int couponCode){
        String token = "";
        String currentDate = DateUtils.getMessageTextTime();
        //注册返
        if (1==couponCode) {
            token =currentDate+"Q"+tokenKey;
        }
        //签到返
        if (2==couponCode) {
            token =currentDate+"Z"+tokenKey;
        }
        //充值返
        if (3==couponCode||6==couponCode || 15==couponCode || 16==couponCode || 17==couponCode) {
            token =tokenKey;  //此时为订单号
        }
        return  token;
    }


    @Override
    public Map<String, Object> couponImportCard(String param, MultipartFile file) throws Exception{
        Map<String,Object> jsonMap = new HashMap<>();
        Map<String,Object> params = JSONObject.parseObject(param,Map.class);
        if(params == null){
            jsonMap.put("msg","参数有误！");
            jsonMap.put("status", false);
            return jsonMap;
        }
        if(params.get("couponCode")==null||Integer.parseInt(params.get("couponCode").toString())<=0){
            jsonMap.put("msg","参与活动不能为空！");
            jsonMap.put("status", false);
            return jsonMap;
        }
        if(params.get("sendChannel")==null||"".equals(params.get("sendChannel").toString())){
            jsonMap.put("msg","赠送渠道不能为空！");
            jsonMap.put("status", false);
            return jsonMap;
        }
        if(params.get("sendTypeId")==null||"".equals(params.get("sendTypeId").toString())){
            jsonMap.put("msg","券类型不能为空！");
            jsonMap.put("status", false);
            return jsonMap;
        }
        int couponCode = Integer.parseInt(params.get("couponCode").toString());
        String sendChannel = params.get("sendChannel").toString();
        String sendTypeId = params.get("sendTypeId").toString();

        if(12==couponCode||13==couponCode){
            excelHandleCard(couponCode,sendChannel,sendTypeId,file,jsonMap);
        }else{
            jsonMap.put("msg","该活动类型暂不支持!");
            jsonMap.put("status", false);
        }
        return jsonMap;
    }

    private void excelHandleCard(int couponCode,String sendChannel,String sendTypeId,MultipartFile file,Map<String,Object> jsonMap) throws Exception{

        Workbook wb = WorkbookFactory.create(file.getInputStream());
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 遍历所有单元格，读取单元格
        int row_num = sheet.getLastRowNum();

        List<Map<String,Object>> errorList=new ArrayList<Map<String,Object>>();
        List<Map<String,String>> checkList=new ArrayList<Map<String,String>>();// 商户号+订单号 校验
        List<CouponImportCard> addList=new ArrayList<CouponImportCard>();//需要更新的

        for (int i = 1; i <= row_num; i++) {
            Row row = sheet.getRow(i);
            String merchantNo1 = CellUtil.getCellValue(row.getCell(0));//赠送商户编号
            String merchantName1 = CellUtil.getCellValue(row.getCell(1));//赠送商户名称
            String orderNo1 = CellUtil.getCellValue(row.getCell(2));//关联交易订单号

            Map<String,Object> errorMap=new HashMap<String,Object>();
            int rowNum=i+1;
            CouponImportCard addItem=new CouponImportCard();
            addItem.setCouponCode(couponCode);
            addItem.setSendChannel(sendChannel);
            addItem.setSendTypeId(sendTypeId);
            addItem.setMerchantName(merchantName1);

            if(merchantNo1==null||"".equals(merchantNo1)){
                errorMap.put("msg","第"+rowNum+"行,赠送商户编号为空!");
                errorList.add(errorMap);
                continue;
            }
            String merchantNo=merchantNo1.split("\\.")[0];
            addItem.setMerchantNo(merchantNo);

            String orderNo="";
            if(12==couponCode||13==couponCode){
                if(orderNo1==null||"".equals(orderNo1)){
                    errorMap.put("msg","第"+rowNum+"行,关联交易订单号为空!");
                    errorList.add(errorMap);
                    continue;
                }
                orderNo=orderNo1.split("\\.")[0];
                addItem.setOrderNo(orderNo);

                //验证重复
                if(existList(checkList,merchantNo,orderNo)){
                    errorMap.put("msg","导入失败,第"+rowNum+"行,赠送商户编号("+merchantNo+"),关联交易订单号("+orderNo+")重复!");
                    errorList.add(errorMap);
                    continue;
                }else{
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("merchantNo",merchantNo);
                    map.put("orderNo",orderNo);
                    checkList.add(map);
                }

                CardAndReward card = cardAndRewardManageService.getCardLoanHearten(merchantNo,orderNo);

                if(card==null){
                    errorMap.put("msg","第"+rowNum+"行,赠送商户编号("+merchantNo+"),关联交易订单号("+orderNo+")不存在!");
                    errorList.add(errorMap);
                    continue;
                }else{
                    if(card.getGivenStatus()!=null&&1==card.getGivenStatus().intValue()){
                        errorMap.put("msg","第"+rowNum+"行,该关联交易订单号("+orderNo+")您已成功赠送该用户,不能再次赠送!");
                        errorList.add(errorMap);
                        continue;
                    }
                    if(12==couponCode){
                        if(card.getOrderType().intValue()!=2){
                            errorMap.put("msg","第"+rowNum+"行,该关联交易订单号("+orderNo+")的订单类型和选择的活动类型不一致,赠送失败!");
                            errorList.add(errorMap);
                            continue;
                        }
                    }else if(13==couponCode){
                        if(card.getOrderType().intValue()!=6){
                            errorMap.put("msg","第"+rowNum+"行,该关联交易订单号("+orderNo+")的订单类型和选择的活动类型不一致,赠送失败!");
                            errorList.add(errorMap);
                            continue;
                        }
                    }
                    addItem.setCard(card);
                }
            }
            addList.add(addItem);
        }

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //处理需要生成的券
        if(addList!=null&&addList.size()>0){
            for(CouponImportCard item:addList){
                registerCouponCard(item,principal.getUsername());
            }
        }
        jsonMap.put("errorList", errorList);
        jsonMap.put("status", true);
        jsonMap.put("msg", "导入成功,总共"+row_num+"条数据,成功导入赠送"+addList.size()+"条数据,失败"+errorList.size()+"条数据!");
    }
    //生成券
    public void registerCouponCard(CouponImportCard addItem,String username) throws Exception {
        String merchantNo = addItem.getMerchantNo();
        int couponCode = addItem.getCouponCode();
        String sendTypeId =addItem.getSendTypeId();

        CouponActivityEntity couponActivityEntity = cardAndRewardManageService.selectCouponActivityInfoById(sendTypeId);

        CardAndReward card=addItem.getCard();
        card.setEffectiveDays(couponActivityEntity.getEffectiveDays());
        card.setTicketId(couponActivityEntity.getId());
        card.setCouponAmount(couponActivityEntity.getCouponAmount());
        card.setGivenChannel(addItem.getSendChannel());


        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String couDate = sdf.format(new Date());
        String random = StringUtil.filterNull(Math.random());
        random = random.substring(random.indexOf(".") + 1, 10);
        couDate = couDate + random;

        String token = "";
        String currentDate = DateUtils.getMessageTextTime();

        if (12==couponCode) {
            token = currentDate + "BK" + couDate;
        } else {
            token = currentDate + "DK" + couDate;
        }
        Date couponStartDate = new Date();
        Date endDate = DateUtils.addDate(couponStartDate, couponActivityEntity.getEffectiveDays());
        Map<String, Object> couponMap = new HashMap<String, Object>();
        couponMap.put("face_value", couponActivityEntity.getCouponAmount());
        couponMap.put("balance", couponActivityEntity.getCouponAmount());
        couponMap.put("coupon_amount", new BigDecimal(0));
        couponMap.put("gift_amount", new BigDecimal(0));
        couponMap.put("coupon_code", couponCode);
        couponMap.put("cancel_verification_code", couponActivityEntity.getCancelVerificationCode());
        couponMap.put("coupon_status", "1"); // 1 未使用
        couponMap.put("start_time", couponStartDate);
        couponMap.put("end_time", endDate);
        couponMap.put("token", token);
        couponMap.put("merchant_no", merchantNo);
        couponMap.put("activity_first", couponActivityEntity.getActivityFirst());
        couponMap.put("activity_entity_id", couponActivityEntity.getId());
        couponMap.put("given_status_id", addItem.getCard().getId());

        couponMap.put("coupon_type", couponActivityEntity.getCouponType());//走购买鼓励金逻辑

        String couponNo="B"+couponActivityService.getNext("coupon_no_seq").get("t").toString();
        couponMap.put("coupon_no",couponNo)	;

        card.setUpdateTime(couponStartDate);
        card.setSuccessTime(couponStartDate);
        card.setOperTime(endDate);
        card.setOperUsername(username);
        card.setGivenType("1");
        // 防并发
        int addUserCoupon = couponActivityService.addUserCouponReload(couponMap);
        if (addUserCoupon == 0) {
            return;
        }
        if (addUserCoupon == 1) {
            card.setGivenStatus(1);
            // 赠送成功更改状态
            cardAndRewardManageService.updateCardLoanHearten(card);
        } else {
            card.setSuccessTime(null);
            card.setOperTime(null);
            card.setGivenStatus(2);
            cardAndRewardManageService.updateCardLoanHearten(card);
        }
        // 赠送日志
        cardAndRewardManageService.insertCardLoanHeartenLog(card);
        try {
            cardAndRewardManageService.sendJG(merchantNo,couponCode+"");

            sensorsService.buyCouponDetail(couponMap,couponNo,merchantNo,couponStartDate,endDate,couponCode+"");
        } catch (Exception e) {
            log.error("赠送下发出现异常",e);
        }
    }
}
