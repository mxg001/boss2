package cn.eeepay.framework.service.impl.cusSms;

import cn.eeepay.framework.dao.cusSms.CusSmsTemplateDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.cusSms.CusSmsRecord;
import cn.eeepay.framework.model.cusSms.CusSmsTemplate;
import cn.eeepay.framework.model.cusSms.SendResult;
import cn.eeepay.framework.service.cusSms.CusSmsRecordService;
import cn.eeepay.framework.service.cusSms.CusSmsTemplateService;
import cn.eeepay.framework.service.sysUser.BossSmsService;
import cn.eeepay.framework.util.RandomNumber;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service("cusSmsTemplateService")
public class CusSmsTemplateServiceImpl implements CusSmsTemplateService {

    @Resource
    private CusSmsTemplateDao cusSmsTemplateDao;
    @Resource
    private CusSmsRecordService cusSmsRecordService;
    @Resource
    private BossSmsService bossSmsService;

    @Override
    public List<CusSmsTemplate> selectAllList(CusSmsTemplate info, Page<CusSmsTemplate> page) {
        return cusSmsTemplateDao.selectAllList(info,page);
    }

    @Override
    public int addSmsTemplate(CusSmsTemplate info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setCreateOperator(principal.getUsername());
        info.setLastUpdateOperator(principal.getUsername());
        return cusSmsTemplateDao.addSmsTemplate(info);
    }

    @Override
    public int editSmsTemplate(CusSmsTemplate info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setLastUpdateOperator(principal.getUsername());
        return cusSmsTemplateDao.editSmsTemplate(info);
    }

    @Override
    public int deleteSmsTemplate(int id) {
        return cusSmsTemplateDao.deleteSmsTemplate(id);
    }

    @Override
    public CusSmsTemplate getSmsTemplateInfo(int id) {
        return cusSmsTemplateDao.getSmsTemplateInfo(id);
    }

    @Override
    public int sendTemplate(SendResult info, Map<String, Object> msg) {
        CusSmsTemplate oldInfo=cusSmsTemplateDao.getSmsTemplateInfo(info.getTemplateId());//短信模板信息
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String operator=principal.getUsername();
        int errorNum=0;
        int checkNum=0;
        int successNum=0;
        int countNum=0;
        List<CusSmsRecord> addList=new ArrayList<CusSmsRecord>();//需要保存的日志记录
        StringBuffer resultStr=new StringBuffer();
        List<String> checkList=new ArrayList<String>();//校验是否重复手机号
        Map<String,String> checkMap=new HashMap<String,String>();//存储手机号-商户号对应关系
        if(info.getSendStr()!=null){
            String[] strs=info.getSendStr().replaceAll("\\n","").split(",");
            if(strs!=null&&strs.length>0){
                countNum=strs.length;
                for(String str:strs){
                    if("1".equals(info.getSendType())) {//手机号码
                        //验证手机号格式
                        if(!checkMobilePhone(str)){
                            checkNum++;
                            resultStr.append(str+":手机号码格式不正确;<br>");
                            continue;
                        }
                        if(checkList.contains(str)){
                            checkNum++;
                            resultStr.append(str+":手机号码重复;<br>");
                            continue;
                        }else{
                            checkList.add(str);
                        }
                        List<MerchantInfo> list = cusSmsTemplateDao.getMerchantInfoByPhone(str);
                        if("1".equals(oldInfo.getType())){//验证码模式下，查不到商户号不发送
                            if(list==null||list.size()<=0){
                                checkNum++;
                                resultStr.append(str+":商户不存在;<br>");
                                continue;
                            }
                        }
                        //发送短信
                        Map<String,Object> map =sendSmsTemplate(str,oldInfo);
                        if("200".equals(map.get("status"))) {//发送成功了
                            if(list!=null&&list.size()>0){
                                for(MerchantInfo item:list){
                                    CusSmsRecord addInfo=new CusSmsRecord();
                                    saveCusSmsRecord(addInfo,oldInfo,map,str,item.getMerchantNo(),operator);
                                    addList.add(addInfo);
                                }
                            }else{
                                CusSmsRecord addInfo=new CusSmsRecord();
                                saveCusSmsRecord(addInfo,oldInfo,map,str,null,operator);
                                addList.add(addInfo);
                            }
                            successNum++;
                        }else{
                            errorNum++;
                            resultStr.append(str+":发送失败;<br>");
                        }

                    }else{//商户编码
                        MerchantInfo merInfo=cusSmsTemplateDao.getMerchantInfoByNo(str);
                        if(merInfo==null){
                            checkNum++;
                            resultStr.append(str+":商户编号不存在;<br>");
                            continue;
                        }
                        if(checkList.contains(merInfo.getMobilephone())){
                            checkNum++;
                            resultStr.append(str+":与商户编号("+checkMap.get(merInfo.getMobilephone())+")的手机号码相同;<br>");
                            continue;
                        }else{
                            checkList.add(merInfo.getMobilephone());
                            checkMap.put(merInfo.getMobilephone(),str);
                        }
                        //发送短信
                        Map<String,Object> map =sendSmsTemplate(merInfo.getMobilephone(),oldInfo);
                        if("200".equals(map.get("status"))) {//发送成功了
                            CusSmsRecord addInfo=new CusSmsRecord();
                            saveCusSmsRecord(addInfo,oldInfo,map,merInfo.getMobilephone(),str,operator);
                            addList.add(addInfo);
                            successNum++;
                        }else{
                            errorNum++;
                            resultStr.append(str+":发送失败;<br>");
                        }
                    }

                }
            }
        }
        if(addList.size()>0){
            cusSmsRecordService.insertCusSmsRecordList(addList);
        }
        String msgStr="发送成功,总共"+countNum+"条,发送成功"+successNum+"条,发送失败"+(checkNum+errorNum)+"条.";
        msg.put("status",true);
        msg.put("msg",msgStr);
        msg.put("errorMsg",msgStr+"<br>"+resultStr.toString());
        return 1;
    }
    //验证手机号格式
    private boolean checkMobilePhone(String mobilePhone){
        String regex = "^1\\d{10}$";
        if(Pattern.matches(regex, mobilePhone)){
            return true;
        }
        return false;
    }
    //封装新增实体
    private void saveCusSmsRecord(CusSmsRecord addInfo,CusSmsTemplate oldInfo,Map<String,Object> map,String mobilePhone,String merchantNo,String operator){
        addInfo.setMobilePhone(mobilePhone);
        addInfo.setMerchantNo(merchantNo);
        addInfo.setOperator(operator);
        addInfo.setTemplateId(oldInfo.getId());
        addInfo.setType(oldInfo.getType());
        addInfo.setContent(map.get("context").toString());
        if("1".equals(oldInfo.getType())){
            addInfo.setCode(map.get("code").toString());
        }
    }

    private Map<String,Object> sendSmsTemplate(String mobile,CusSmsTemplate oldInfo){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("status","0");
        String context="";
        if("1".equals(oldInfo.getType())){//验证码模式
            String code=RandomNumber.mumberRandom(null,6,0);
            context=oldInfo.getTemplate().replaceAll("code",code);
            int sendStatus=bossSmsService.sendCusSmsTemplate(mobile,context);
            if(sendStatus==200){
                map.put("status","200");
                map.put("code",code);
                map.put("context",context);
                return map;
            }
        }else{
            context=oldInfo.getTemplate();
            int sendStatus=bossSmsService.sendCusSmsTemplate(mobile,context);
            if(sendStatus==200){
                map.put("status","200");
                map.put("code",null);
                map.put("context",context);
                return map;
            }
        }
        return map;
    }
}
