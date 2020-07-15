package cn.eeepay.framework.service.pushManager.impl;

import cn.eeepay.boss.action.pushManager.PushManagerAction;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.pushManager.PushManagerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.pushManager.PushManager;
import cn.eeepay.framework.model.pushManager.PushManagerDetail;
import cn.eeepay.framework.service.pushManager.PushManagerService;
import cn.eeepay.framework.service.unTransactionalImpl.SubscribeVipPushJobServiceImpl;
import cn.eeepay.framework.serviceImpl.couponImport.PushManagerJobServiceImpl;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.Md5;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class PushManagerServiceImpl implements PushManagerService {
    private static final Logger log = LoggerFactory.getLogger(PushManagerServiceImpl.class);
    @Resource
    private PushManagerDao pushManagerDao;
    @Resource
    private SysDictDao sysDictDao;

    @Resource
    private PushManagerJobServiceImpl pushManagerJobService;

    private static final String Token = "a185faf4c55807bca4bcfdaae06b58fa";
    private static final String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
    private static final String PATH = "/riskhandle/controlJPush";


    @Override
    public void checkCanPush(Map<String, Object> msgMap, String pushId) {
        int result = pushManagerDao.checkCanPush(pushId);
        if(result<=0){
            msgMap.put("status",false);
            msgMap.put("msg","部分商户未导入，不可推送！");
        }
    }

    @Override
    public String getPushObjName(String pushObj) {

        try {
            return pushManagerDao.getPushObjName(pushObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PushManager> getPushManager(PushManager pm) {

        try {
            return pushManagerDao.getPushManager(pm);
        } catch (Exception e) {
            log.error("获取到处数据出错",e);
        }
        return null;
    }

    @Override
    public void tuPush(Long id, Map<String, Object> msgMap) {

        PushManager pm = pushManagerDao.getByPushManagerId(id);
        if(pm==null){
            msgMap.put("status",false);
            msgMap.put("msg","推送内容不存在! ");
            return;
        }

        if(pm.getPushStatus()==1){//已推送
            msgMap.put("status",false);
            msgMap.put("msg","请勿重新推送! ");
            return;
        }
        if(pm.getDingshiOrNow()!=0){//只有实时推送才能进行手动推送
            msgMap.put("status",false);
            msgMap.put("msg","只有实时推送才能进行手动推送！");
            return;
        }
        //更新推送时间
        Date now = new Date();
        pm.setPushTime(now);
        pm.setTimerTime(now);
        pm.setPushStatus(1);
        pushManagerDao.updatePushManager(pm);
    }
    @Override
    public void previewPushManager(String merchantNos, Long pushId, Map<String, Object> msgMap) {
        try {
            if(StringUtil.isEmpty(merchantNos)){
                msgMap.put("status",false);
                msgMap.put("msg","预览商户号为空");
                return;
            }
            PushManager pm = pushManagerDao.getByPushManagerId(pushId);
            if(pm==null){
                msgMap.put("status",false);
                msgMap.put("msg","推送内容不存在");
                return;
            }
            String url = sysDictDao.getByKey("CORE_URL").getSysValue() + "/riskhandle/commonJPush";
            ArrayList<String> errMerchantNo = new ArrayList<>();
            for (String merchantNo : merchantNos.split(",")) {
                //判断商户编号是否存在
                MerchantInfo mi = pushManagerDao.validMerchantNo(merchantNo);
                if(mi==null){
                    errMerchantNo.add(merchantNo);
                    continue;
                }
                //判断所选商户是不是在推送范围
                int validMerchantNoAndAppNoResult = pushManagerDao.validMerchantNoAndAppNo(pm.getPushObj(),merchantNo);

                if(validMerchantNoAndAppNoResult!=1){
                    errMerchantNo.add(merchantNo);
                    continue;
                }

                String currMobileType = pushManagerDao.getMobileTypeByMerchantNo(merchantNo);
                if (StringUtils.isNotEmpty(currMobileType)) {
                    if(pm.getMobileTerminalType()!=null){
                        if (pm.getMobileTerminalType() != Integer.parseInt(currMobileType)) {
                            errMerchantNo.add(merchantNo);
                            continue;
                        }
                    }
                }else{
                    errMerchantNo.add(merchantNo);
                    continue;
                }

                String result = previewRemotePush(merchantNo,1,pm);
                try {
                    JSONObject header = JSON.parseObject(result).getJSONObject("header");
                    if(header!=null){
                        if(header.getBoolean("succeed")!=null && !header.getBoolean("succeed")){
                            errMerchantNo.add(merchantNo);
                        }
                    }
                } catch (Exception e) {
                    errMerchantNo.add(merchantNo);
                    log.error("推送预览解析返回结果失败",e);
                    msgMap.put("status", false);
                    msgMap.put("msg", "预览失败！");
                }
            }
            StringBuilder sb = new StringBuilder();
            if(errMerchantNo.size()>5){
                for (String str : errMerchantNo.subList(0, 5)) {
                    sb.append(str).append(",");
                }
                msgMap.put("msg",StringUtil.delLastChar(sb.toString())+"...... 推送失败！");
            }else{
                if(errMerchantNo.size()>0){
                    for (String str : errMerchantNo) {
                        sb.append(str).append(",");
                    }
                    msgMap.put("msg",StringUtil.delLastChar(sb.toString())+" 推送失败！");
                }else{
                    msgMap.put("status", true);
                    msgMap.put("msg", "预览成功");
                }
            }

        } catch (Exception e) {
            log.error("推送预览失败",e);
            msgMap.put("status", false);
            msgMap.put("msg", "预览失败！");
            e.printStackTrace();
        }
    }


    /***
     *  调用远程极光推送
     */
    private String previewRemotePush(String merchantNo,Integer pushMode,PushManager pm){
        String Token = "a185faf4c55807bca4bcfdaae06b58fa";
        String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
        String PATH = "/jPushHandle/controlJPush";
        try {

            String appNo = pushManagerDao.getAppNoByMerchantNo(merchantNo);

            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("token", Token);
            String authCode = Md5.md5Str(USER_INFO_ID +merchantNo+ Token).toUpperCase();
            String url = sysDictDao.getByKey("CORE_URL").getSysValue() + PATH;
            requestMap.put("merchantNoArray", merchantNo);
            requestMap.put("authCode", authCode);
            requestMap.put("osType", pm.getMobileTerminalType()==null?"3":pm.getMobileTerminalType()+"");
            requestMap.put("pushMode", pushMode+"");//推送方式 1：根据商户号(数组)推 2：根据app_no全推
            requestMap.put("title", pm.getPushTitle());
            requestMap.put("content", pm.getPushContent());
            requestMap.put("appNo", appNo);
            requestMap.put("merchantNo", merchantNo);
            JSONObject extJson = new JSONObject();
            extJson.put("link",pm.getJumpUrl());
            extJson.put("type","control");
            requestMap.put("ext",extJson.toJSONString());
            log.info("预览推送 调用极光推送接口参数 ：{}"+JSONObject.toJSONString(requestMap));

            String result = HttpUtils.sendPostRequest(url, requestMap);

            log.info("预览推送 调用极光推送接口结果 ：{}"+result);

            return result;
        } catch (Exception e) {
            log.error("调用远程极光推送失败",e);
        }
        return null;
    }



    @Override
    public List<Map<String, String>> getAppInfo(String apply) {

        try {
            return sysDictDao.getListByKey("pushManager_appNo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public void selectPushManagerByCondition(Page<PushManager> page, PushManager pm) {

        try {
            pushManagerDao.selectPushManagerByParam(page,pm);
            List<PushManager> result = page.getResult();
            for (PushManager pushManager : result) {
                String pushObj = pushManager.getPushObj();
                String pushObjName = pushManagerDao.getPushObjName(pushObj);
                pushManager.setPushObjName(pushObjName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveOrUpdatePushManager(PushManager pm,Map<String,Object> msgMap) {
        int result = -1;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(pm!=null){
                String[] pushObjArr = pm.getPushObj().split(",");
                //修改
                if(pm.getId()!=null){

                    PushManager tmpPM = pushManagerDao.getByPushManagerId(pm.getId());

                    if(tmpPM.getPushStatus()!=0){
                        msgMap.put("status", false);
                        msgMap.put("msg", "修改失败，当前状态不是未推送!");
                        return;
                    }

                    if(pm.getDingshiOrNow()==0){
                        pm.setPushTime(null);
                        pm.setTimerTime(null);
                    }else if(pm.getDingshiOrNow()==1){
                        pm.setTimerTime(pm.getPushTime());
                    }
                    result = pushManagerDao.updatePushManager(pm);
                    if(result==1){
                        //删除原来明细表的记录 重新生成
                        pushManagerDao.delPushManagerDetailByPushId(pm.getId());
                        //如果是全部用户 向明细表每个推送对象保存一条默认信息
                        if(pm.getTargetUser()==0){
                            for (String pushObj : pushObjArr) {
                                PushManagerDetail pmd = new PushManagerDetail();
                                pmd.setPushObj(pushObj);
                                pmd.setPushId(pm.getId());
                                //未推送
                                pmd.setPushStatus(0);
                                pmd.setPushAll(pm.getTargetUser());
                                pushManagerDao.savePushManagerDetail(pmd);
                            }
                        }
                        msgMap.put("status", true);
                        msgMap.put("msg", "更新成功！");
                    }else{
                        msgMap.put("status", false);
                        msgMap.put("msg", "更新失败！");
                    }
                }else{
                    //新增
                    //初始化信息
                    Date now =new Date();
                    //未推送
                    pm.setPushStatus(0);
                    pm.setCreateTime(now);
                    pm.setCreatePerson(principal.getUsername());
                    //如果是定时推送
                    if(pm.getDingshiOrNow()==1){
                        pm.setTimerTime(pm.getPushTime());
                    }
                    result = pushManagerDao.savePushManager(pm);
                    if(result==1){
                        //如果是全部用户 向明细表每个推送对象保存一条默认信息//如果是全部用户 向明细表每个推送对象保存一条默认信息
                        if(pm.getTargetUser()==0){
                            for (String pushObj : pushObjArr) {
                                PushManagerDetail pmd = new PushManagerDetail();
                                pmd.setPushObj(pushObj);
                                pmd.setPushId(pm.getId());
                                //未推送
                                pmd.setPushStatus(0);
                                pmd.setPushAll(pm.getTargetUser());
                                pushManagerDao.savePushManagerDetail(pmd);
                            }
                        }
                        msgMap.put("status", true);
                        msgMap.put("msg", "添加成功！");
                    }else{
                        msgMap.put("status", false);
                        msgMap.put("msg", "添加失败！");
                    }
                }
            }

    }


    @Override
    public void delPushManagerById(Long id,Map<String, Object> msgMap) {

        try {
            if(id!=null){
                int result = pushManagerDao.delPushManager(id);
                if(result!=1){
                    msgMap.put("status",false);
                    msgMap.put("msg","删除失败!");
                }
                //删除关联推送明细表
                pushManagerDao.delPushManagerDetailByPushId(id);

            }else {
                msgMap.put("status",false);
                msgMap.put("msg","参数为空!");
            }
        } catch (Exception e) {
            log.debug("删除推送管理内容失败",e);
            msgMap.put("status",false);
            msgMap.put("msg","删除失败!");
            throw new RuntimeException();
        }
    }

    @Override
    public PushManager getByPushManagerId(Long id, Map<String, Object> msgMap) {
        if(id==null){
            msgMap.put("status",false);
            msgMap.put("msg","参数为空");
            return null;
        }
        return pushManagerDao.getByPushManagerId(id);
    }


    @Override
    public Map<String, Object> importPushManagerFromExcel(MultipartFile file,Long pushId) throws EncryptedDocumentException, InvalidFormatException, IOException {
        Map<String, Object> msg = new HashMap<>();
        Workbook wb = WorkbookFactory.create(file.getInputStream());

        PushManager pm = this.getByPushManagerId(pushId,new HashMap<String, Object>());

        String[] pushObj ;
        // 遍历所有单元格，读取单元格
        int num = 0;
        Sheet sheet = wb.getSheetAt(0);//读取第一个sheet
        int row_num = sheet.getLastRowNum();

        Set<String> merchantSet = new HashSet<>();

        try {
            if(pm!=null){
                if( pm.getTargetUser()==1){//如果不是全部推送 才需要校验excel里面的商户编号
                    pushObj = pm.getPushObj().split(",");
                    for (int i = 1; i <= row_num; i++) {
                        Row row = sheet.getRow(i);
                        String merchantNo = getCellValue(row.getCell(1));//推送对象
                        String appName = getCellValue(row.getCell(0));//推送对象
                        if(StringUtils.isNotBlank(merchantNo)){
                            if(!merchantSet.add(merchantNo)){//校验重复商户编号
                                msg.put("msg","第"+i+"行数据校验错误，错误原因: 已存在重复的商户编号");
                                msg.put("status",false);
                                return msg;
                            }
                            if(!validCheck(merchantNo,pushObj,msg,appName)){
                                msg.put("msg","第"+i+"行数据校验错误，错误原因: "+msg.get("msg"));
                                msg.put("status",false);
                                return msg;
                            }


                        }else{
                            msg.put("msg","第"+i+"行数据校验错误，错误原因: 该行商户号为空");
                            msg.put("status",false);
                            return msg;
                        }
                        num++;
                    }


                    if(row_num==num){
                        //保存之前先删除上次的推送明细
                        pushManagerDao.delPushManagerDetailByPushId(pm.getId());
                        num = 0;
                        PushManagerDetail pmd = null;
                        for (String s : merchantSet) {
                            pmd = new PushManagerDetail();
                            pmd.setPushAll(pm.getTargetUser());
                            pmd.setMerchantNo(s);
                            pmd.setPushId(pm.getId());
                            pmd.setPushStatus(0);//未推送
                            int tmp = pushManagerDao.savePushManagerDetail(pmd);
                            num +=tmp;
                        }
                    }
                    msg.put("status", true);
                    msg.put("msg", "商户信息导入成功,其中成功" + num + "条,失败"+ (row_num-num) + "条");
                }
            }
        } catch (Exception e) {
            msg.put("msg","导入商户信息失败");
            msg.put("status",false);
            log.error("导入商户信息失败",e);
        }


        return msg;
    }

    /***
     *
     * @param merchantNo
     * @param pushObj
     * @return
     */
    private boolean validCheck(String merchantNo,String[] pushObj,Map<String, Object> msg,String appName) {
        if(pushObj==null || pushObj.length==0){
            msg.put("status",false);
            msg.put("msg","推送对象为空");
            return false;
        }
        if(StringUtils.isEmpty(merchantNo)){
            msg.put("status",false);
            msg.put("msg","商户编号为空");
            return false;
        }
        //判断商户编号是否存在
        MerchantInfo mi = pushManagerDao.validMerchantNo(merchantNo);

        if(mi==null){
            msg.put("status",false);
            msg.put("msg","商户编号不存在");
            return false;
        }else{
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pushObj.length; i++) {
                sb.append(pushObj[i]).append(",");
            }

            int result = pushManagerDao.validMerchantNoAndAppNo(StringUtil.delLastChar(sb.toString()),merchantNo);//判断所选商户是不是在推送范围

            if(result!=1){
                msg.put("status",false);
                msg.put("msg","商户编号不在推送对象范围内");
                return false;
            }
            String resultAppName = pushManagerDao.getAppNameByMerchantNo(mi.getTeamId());
            if(StringUtils.isNotBlank(resultAppName)){
                if(!resultAppName.equals(appName)){
                    msg.put("status",false);
                    msg.put("msg","推送对象和商户编号不匹配");
                    return false;
                }
            }else{
                msg.put("status",false);
                msg.put("msg","推送对象为空");
                return false;
            }
        }
        return true;
    }


    public String getCellValue(Cell cell){
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getStringCellValue();
        }
        return null;
    }
}
