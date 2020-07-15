package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.UserManagementDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.exchange.*;
import cn.eeepay.framework.service.OpenPlatformService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UserManagementService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 超级兑用户 service实现类
 */
@Service("userManagementService")
public class UserManagementServiceImpl implements UserManagementService {
    //超级兑商户科目号
    public static final String EXCHANGE_SUBJECT_NO = "224118";

    private static final Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    @Resource
    private UserManagementDao userManagementDao;

    @Resource
    private OpenPlatformService openPlatformService;

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<UserManagement> selectAllList(UserManagement userManagement, Page<UserManagement> page) {
        List<UserManagement> list=userManagementDao.selectAllList(userManagement,page);
        dataProcessingList(page.getResult());
        return list;
    }

    /**
     * 数据处理List
     */
    private void dataProcessingList(List<UserManagement> list){
        if(list!=null&&list.size()>0){
            for(UserManagement item:list){
                if(item!=null){
                    item.setMobileUsername(StringUtil.sensitiveInformationHandle(item.getMobileUsername(),0));
                }
            }
        }
    }

    @Override
    public MerInfoTotal selectSum(UserManagement userManagement, Page<UserManagement> page) {
        return userManagementDao.selectSum(userManagement,page);
    }

    @Override
    public UserManagement getUserManagement(String merchantNo) {
        return userManagementDao.getUserManagementById(merchantNo);
    }

    /**
     * 查询超级兑账户
     * @param merchantNo
     * @return
     */
    @Override
    public List<AccountInfo> getUserBalance(String merchantNo) {
        String subjectNo = UserManagementServiceImpl.EXCHANGE_SUBJECT_NO;//超级兑用户账户的科目号
        String merAccountStr = ClientInterface.getMerchantAccountBalance(merchantNo,subjectNo);
        List<AccountInfo> list = new ArrayList<>();
        if(StringUtils.isNotBlank(merAccountStr)){
            JSONObject merAccountJson = JSON.parseObject(merAccountStr);
            if(merAccountJson != null && merAccountJson.getBoolean("status")){
                AccountInfo accountInfo = JSONObject.parseObject(merAccountStr,AccountInfo.class);
                list.add(accountInfo);
            }
        }
        return list;
    }

    /**
     * 查询超级兑账户明细
     * @param record
     * @param pageNo
     * @param pageSize
     * @return
     */

    @Override
    public Result getAccountTranInfo(AccountInfoRecord record, int pageNo, int pageSize) {
        Result result = new Result();
        result.setMsg("获取用户账户明细失败");
        String merchantNo = record.getUserId();
        String recordTimeStart = record.getRecordTimeStart();
        String recordTimeEnd = record.getRecordTimeEnd();
        String debitCreditSide = record.getDebitCreditSide();
        String subjectNo = UserManagementServiceImpl.EXCHANGE_SUBJECT_NO;
        String accountDetailStr = ClientInterface.selectMerchantAccountTransInfoList(
                merchantNo, subjectNo,recordTimeStart, recordTimeEnd, debitCreditSide, pageNo, pageSize);
        if(StringUtils.isNotBlank(accountDetailStr)){
            JSONObject accountDetailJson = JSON.parseObject(accountDetailStr);
            if(accountDetailJson != null){
                if(accountDetailJson.getBoolean("status")){
                    JSONObject json = JSONObject.parseObject(accountDetailJson.getString("data"));
                    List<AccountInfoRecord> list = JSONObject.parseArray(json.getString("list"),AccountInfoRecord.class);
                    String total = json.getString("total");
                    Map<String, Object> map = new HashMap<>();
                    map.put("list", list);
                    map.put("total", total);
                    result.setStatus(true);
                    result.setMsg("获取用户账户明细成功");
                    result.setData(map);
                }else{
                    result.setMsg(accountDetailJson.getString("msg"));
                }
            }
        }
        return result;
    }

    @Override
    public Map<String,Date> getUserManagementMember(String merchantNo) {
        List<UserManagementMember> list=userManagementDao.getUserManagementMember(merchantNo);
        Map<String,Date> map=new HashMap<String,Date>();
        if(list!=null&&list.size()>0){
            for(UserManagementMember mem:list){
                map.put("vip"+mem.getMerCapa(),mem.getCreateTime());
            }
        }
        return map;
    }

    @Override
    public Subordinate getSubordinate(String merchantNo) {
        return userManagementDao.getSubordinate(merchantNo);
    }

    @Override
    public int updateUserManagement(UserManagement userManagement) {
        return userManagementDao.updateUserManagement(userManagement);
    }

    @Override
    public void updateSettlementCard(SettlementCard settlementCard, Map<String, Object> msg) {
        if(checkSettlementCard(settlementCard,msg)==1){
            msg.put("status", false);
            return;
        }
        UserManagement oldUser=userManagementDao.getUserManagementById(settlementCard.getMerchantNo());
        if(checkBank_Name_IDCard(oldUser,msg,settlementCard)==1){
            msg.put("status", false);
            return;
        }
        int num=userManagementDao.updateSettlementCard(settlementCard);
        if(num>0){
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            settlementCard.setOldCard(oldUser.getAccountNo());
            settlementCard.setOperator(principal.getId()+"");
            userManagementDao.updateSettlementCardHis(settlementCard);
        }
        msg.put("status", true);
        msg.put("msg", "修改结算卡成功!");
    }

    @Override
    public List<UserManagement> importDetailSelect(UserManagement user) {
        List<UserManagement> list=userManagementDao.importDetailSelect(user);
        dataProcessingList(list);
        return list;
    }

    @Override
    public void importDetail(List<UserManagement> list,HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "超级兑用户列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("merchantNo",null);
            maps.put("oemName",null);
            maps.put("accountName",null);
            maps.put("userName",null);
            maps.put("mobileUsername",null);
            maps.put("totalBalance",null);
            maps.put("merCapa",null);
            maps.put("merAccount",null);
            maps.put("createTime",null);
            maps.put("vip3",null);
            maps.put("vip4",null);
            maps.put("vip5",null);
            maps.put("parMerNo",null);
            maps.put("freezeAmountBalance",null);
            maps.put("freezeAmount",null);
            data.add(maps);
        }else{
            Map<String, String> merCapaMap=sysDictService.selectMapByKey("MER_CAPA");//用户身份

            for (UserManagement or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("merchantNo",or.getMerchantNo()==null?"":or.getMerchantNo());
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("accountName",or.getAccountName()==null?null:or.getAccountName());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("mobileUsername",or.getMobileUsername()==null?"":or.getMobileUsername());
                maps.put("totalBalance",or.getTotalBalance()==null?"":or.getTotalBalance().toString());
                if(or.getMerCapa()!=null){
                    maps.put("merCapa",merCapaMap.get(or.getMerCapa()));
                }else{
                    maps.put("merCapa","");
                }
                if(or.getMerAccount()>=0){
                    if(or.getMerAccount()==0){
                        maps.put("merAccount","否");
                    }else if(or.getMerAccount()==1){
                        maps.put("merAccount","否");
                    }
                }else{
                    maps.put("merAccount","");
                }
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));

                Map<String,Date> dateMap=getUserManagementMember(or.getMerchantNo());
                if(dateMap!=null){
                    maps.put("vip3",dateMap.get("vip3")==null?"":sdf1.format(dateMap.get("vip3")));
                    maps.put("vip4",dateMap.get("vip4")==null?"":sdf1.format(dateMap.get("vip4")));
                    maps.put("vip5",dateMap.get("vip5")==null?"":sdf1.format(dateMap.get("vip5")));
                }else{
                    maps.put("vip3",null);
                    maps.put("vip4",null);
                    maps.put("vip5",null);
                }
                maps.put("paymentTime", or.getPaymentTime()==null?"":sdf1.format(or.getPaymentTime()));
                maps.put("parMerNo",or.getParMerNo()==null?"":or.getParMerNo());
                maps.put("freezeAmountBalance", or.getFreezeAmountBalance()==null?"":or.getFreezeAmountBalance().toString());
                maps.put("freezeAmount", or.getFreezeAmount()==null?"":or.getFreezeAmount().toString());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"merchantNo","oemName","accountName","userName","mobileUsername","totalBalance",
                "merCapa","merAccount","createTime","vip3","vip4","vip5",
                "parMerNo","freezeAmountBalance","freezeAmount"
        };
        String[] colsName = new String[]{"用户ID","组织名称","姓名","昵称","手机号","总收益(元)",
                "代理身份","是否开户","入驻时间","成为会员时间","成为黄金会员时间","成为钻石会员时间",
                "上级代理ID","冻结金额","预冻结金额"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出超级兑用户列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public List<UserManagement> getUserManagementList(String merchantNo) {
        return userManagementDao.getUserManagementList(merchantNo);
    }

    @Override
    public UserManagement getUserManagementOne(String merchantNo) {
        return userManagementDao.getUserManagementOne(merchantNo);
    }

    @Override
    public int userFreeze(MerchantFreeze freeze) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        freeze.setFreezeOper(principal.getUsername());
        int num=userManagementDao.userFreeze(freeze);
        if(num>0){
            userManagementDao.userFreezeBalance(freeze);
            userManagementDao.userFreezeHis(freeze);
        }
        return num;
    }

    @Override
    public List<MerchantFreeze> getUserFreezeHis(String merchantNo) {
        return userManagementDao.getUserFreezeHis(merchantNo);
    }


    /**
     * 修改结算卡时，调用实名认证接口
     */
    private int checkBank_Name_IDCard(UserManagement oldUser,Map<String, Object> msg,SettlementCard settlementCard) {
        String identityId = oldUser.getBusinessCode();//身份证号
        String accountNo = settlementCard.getCardNo();//新的卡号
        String accountName = oldUser.getAccountName();//开户名
        if(StringUtils.isBlank(identityId)){
            msg.put("msg", "身份证号不能为空!");
            return 1;
        }
        if(StringUtils.isBlank(accountName)){
            msg.put("msg", "开户名不能为空!");
            return 1;
        }
        Map<String, String> checkResultMap=openPlatformService.doAuthen(accountNo,accountName,identityId,null);
        String errCode = checkResultMap.get("errCode");
        boolean flag = "00".equalsIgnoreCase(errCode);
        if (!flag) {
            msg.put("msg", "开户名、身份证、银行卡号不匹配!");
            return 1;
        }
        return 0;
    }

    private boolean isStringNull(String str){
        if(str==null||"".equals(str)){
            return  true;
        }
        return  false;
    }
    private int checkSettlementCard(SettlementCard settlementCard,Map<String, Object> msg){
        if(isStringNull(settlementCard.getCardNo())){
            msg.put("msg", "银行卡号不能为空!");
            return 1;
        }
        if(isStringNull(settlementCard.getBankName())){
            msg.put("msg", "银行名称不能为空!");
            return 1;
        }
        if(isStringNull(settlementCard.getAccountPhone())){
            msg.put("msg", "手机号不能为空!");
            return 1;
        }
        if(isStringNull(settlementCard.getBankProvince())||isStringNull(settlementCard.getBankCity())){
            msg.put("msg", "开户地址不能为空!");
            return 1;
        }
        if(isStringNull(settlementCard.getCnapsNo())){
            msg.put("msg", "分行不能为空!");
            return 1;
        }
        return 0;
    }
}
