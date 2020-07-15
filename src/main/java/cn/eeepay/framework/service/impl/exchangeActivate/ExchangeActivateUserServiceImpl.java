package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateUserDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.exchange.MerchantFreeze;
import cn.eeepay.framework.model.exchange.SettlementCard;
import cn.eeepay.framework.model.exchange.Subordinate;
import cn.eeepay.framework.model.exchange.UserManagementMember;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrg;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUser;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUserCard;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.OpenPlatformService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateUserService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.CommonUtil;
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
@Service("exchangeActivateUserService")
public class ExchangeActivateUserServiceImpl implements ExchangeActivateUserService {

    //超级兑商户科目号
    public static final String EXCHANGE_SUBJECT_NO = "224122";

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateUserServiceImpl.class);

    @Resource
    private ExchangeActivateUserDao exchangeActivateUserDao;

    @Resource
    private OpenPlatformService openPlatformService;

    @Resource
    private AgentInfoService agentInfoService;

    @Override
    public List<ExchangeActivateUser> selectAllList(ExchangeActivateUser userManagement, Page<ExchangeActivateUser> page) {
        List<ExchangeActivateUser> list=exchangeActivateUserDao.selectAllList(userManagement,page);
        if(page.getResult().size()>0){
            for(ExchangeActivateUser user:page.getResult()){
                if(user.getAgentNo()!=null&&!"".equals(user.getAgentNo())){
                    AgentInfo agentInfo =agentInfoService.getAgentByNo(user.getAgentNo());
                    if(agentInfo!=null){
                        user.setAgentName(agentInfo.getAgentName());
                    }
                }
                if(user.getOneAgentNo()!=null&&!"".equals(user.getOneAgentNo())){
                    AgentInfo agentInfo =agentInfoService.getAgentByNo(user.getOneAgentNo());
                    if(agentInfo!=null){
                        user.setOneAgentName(agentInfo.getAgentName());
                    }
                }
            }
        }
        dataProcessingList(page.getResult());
        return list;
    }
    /**
     * 数据处理List
     */
    private void dataProcessingList(List<ExchangeActivateUser> list){
        if(list!=null&&list.size()>0){
            for(ExchangeActivateUser item:list){
                if(item!=null){
                    item.setMobileUsername(StringUtil.sensitiveInformationHandle(item.getMobileUsername(),0));
                }
            }
        }
    }
    @Override
    public ExchangeActivateUser getUserManagement(String merchantNo) {
        ExchangeActivateUser user=exchangeActivateUserDao.getUserManagementById(merchantNo);

        if(user.getAgentNo()!=null&&!"".equals(user.getAgentNo())){
            AgentInfo agentInfo =agentInfoService.getAgentByNo(user.getAgentNo());
            if(agentInfo!=null){
                user.setAgentName(agentInfo.getAgentName());
            }
        }
        if(user.getOneAgentNo()!=null&&!"".equals(user.getOneAgentNo())){
            AgentInfo agentInfo =agentInfoService.getAgentByNo(user.getOneAgentNo());
            if(agentInfo!=null){
                user.setOneAgentName(agentInfo.getAgentName());
            }
        }
        return user;
    }

    /**
     * 查询超级兑账户
     * @param merchantNo
     * @return
     */
    @Override
    public List<AccountInfo> getUserBalance(String merchantNo) {
        String subjectNo = ExchangeActivateUserServiceImpl.EXCHANGE_SUBJECT_NO;//超级兑用户账户的科目号
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
        String subjectNo = ExchangeActivateUserServiceImpl.EXCHANGE_SUBJECT_NO;
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
    public int updateUserManagement(ExchangeActivateUser userManagement) {
        return exchangeActivateUserDao.updateUserManagement(userManagement);
    }

    @Override
    public void updateSettlementCard(SettlementCard settlementCard, Map<String, Object> msg) {
        if(checkSettlementCard(settlementCard,msg)==1){
            msg.put("status", false);
            return;
        }
        ExchangeActivateUser oldUser=exchangeActivateUserDao.getUserManagementById(settlementCard.getMerchantNo());
        if(checkBank_Name_IDCard(oldUser,msg,settlementCard)==1){
            msg.put("status", false);
            return;
        }
        int num=exchangeActivateUserDao.updateSettlementCard(settlementCard);
        if(num>0){
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            settlementCard.setOldCard(oldUser.getIdCardNo());
            settlementCard.setOperator(principal.getId()+"");
            exchangeActivateUserDao.updateSettlementCardHis(settlementCard);
        }
        msg.put("status", true);
        msg.put("msg", "修改结算卡成功!");
    }

    @Override
    public List<ExchangeActivateUser> importDetailSelect(ExchangeActivateUser user) {
        List<ExchangeActivateUser> list =exchangeActivateUserDao.importDetailSelect(user);
        dataProcessingList(list);
        return list;
    }

    @Override
    public void importDetail(List<ExchangeActivateUser> list,HttpServletResponse response) throws Exception {
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
            maps.put("userName",null);
            maps.put("realName",null);
            maps.put("mobileUsername",null);
            maps.put("totalBalance",null);
            maps.put("merAccount",null);
            maps.put("createTime",null);
            maps.put("receiveMerchantNo",null);
            maps.put("receiveMerchantName",null);
            maps.put("agentNo",null);
            maps.put("agentName",null);
            maps.put("oneAgentNo",null);
            maps.put("oneAgentName",null);
            maps.put("merchantStatus",null);
            maps.put("successTime",null);
            maps.put("freezeAmountBalance",null);
            maps.put("freezeAmount",null);
            data.add(maps);
        }else{
            Map<String, String> statusMap=new HashMap<String, String>();
            statusMap.put("1","正常");
            statusMap.put("2","不进不出");
            statusMap.put("3","只进不出");

            Map<String, String> merchantStatusMap=new HashMap<String, String>();
            merchantStatusMap.put("1","待进件");
            merchantStatusMap.put("2","审核中");
            merchantStatusMap.put("3","审核失败");
            merchantStatusMap.put("4","审核通过");


            for (ExchangeActivateUser or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("merchantNo",or.getMerchantNo()==null?"":or.getMerchantNo());
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("realName",or.getRealName()==null?null:or.getRealName());
                maps.put("mobileUsername",or.getMobileUsername()==null?"":or.getMobileUsername());
                maps.put("totalBalance",or.getTotalBalance()==null?"":or.getTotalBalance().toString());
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
                maps.put("receiveMerchantNo",or.getReceiveMerchantNo()==null?"":or.getReceiveMerchantNo());
                maps.put("repayMerchantNo",or.getRepayMerchantNo()==null?"":or.getRepayMerchantNo());
                maps.put("agentNo",or.getAgentNo()==null?null:or.getAgentNo());
                if(or.getAgentNo()!=null&&!"".equals(or.getAgentNo())){
                    AgentInfo agentInfo =agentInfoService.getAgentByNo(or.getAgentNo());
                    maps.put("agentName",agentInfo==null?null:agentInfo.getAgentName());
                }else{
                    maps.put("agentName",null);
                }

                maps.put("oneAgentNo",or.getOneAgentNo()==null?null:or.getOneAgentNo());
                if(or.getOneAgentNo()!=null&&!"".equals(or.getOneAgentNo())){
                    AgentInfo agentInfo =agentInfoService.getAgentByNo(or.getOneAgentNo());
                    maps.put("oneAgentName",agentInfo==null?null:agentInfo.getAgentName());
                }else{
                    maps.put("oneAgentName",null);
                }
                maps.put("merchantStatus",merchantStatusMap.get(or.getMerchantStatus()));
                maps.put("successTime",or.getSuccessTime()==null?"":sdf1.format(or.getSuccessTime()));
                maps.put("freezeAmountBalance", or.getFreezeAmountBalance()==null?"":or.getFreezeAmountBalance().toString());
                maps.put("freezeAmount", or.getFreezeAmount()==null?"":or.getFreezeAmount().toString());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"merchantNo","oemName","userName","realName","mobileUsername","totalBalance",
                "merAccount","createTime","receiveMerchantNo","repayMerchantNo","agentNo","agentName","oneAgentNo","oneAgentName",
                "merchantStatus","successTime","freezeAmountBalance","freezeAmount"
        };
        String[] colsName = new String[]{"用户ID","组织名称","昵称","姓名","手机号","总收益(元)",
                "是否开户","激活时间","收款商户ID","还款商户ID","直属代理商ID","直属代理商名称","一级代理商ID","一级代理商名称",
                "进件状态","进件审核通过时间","冻结金额","预冻结金额"
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
    public List<ExchangeActivateUser> getUserManagementList(String merchantNo) {
        return exchangeActivateUserDao.getUserManagementList(merchantNo);
    }

    @Override
    public int userFreeze(MerchantFreeze freeze) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        freeze.setFreezeOper(principal.getUsername());
        int num=exchangeActivateUserDao.userFreeze(freeze);
        if(num>0){
            exchangeActivateUserDao.userFreezeBalance(freeze);
            exchangeActivateUserDao.userFreezeHis(freeze);
        }
        return num;
    }

    @Override
    public List<MerchantFreeze> getUserFreezeHis(String merchantNo) {
        return exchangeActivateUserDao.getUserFreezeHis(merchantNo);
    }

    @Override
    public List<ExchangeActivateUserCard> getUserCard(String merchantNo,String isSettle) {
        return exchangeActivateUserDao.getUserCard(merchantNo,isSettle);
    }


    /**
     * 修改结算卡时，调用实名认证接口
     */
    private int checkBank_Name_IDCard(ExchangeActivateUser oldUser,Map<String, Object> msg,SettlementCard settlementCard) {
        String identityId = oldUser.getIdCardNo();//身份证号
        String accountNo = settlementCard.getCardNo();//新的卡号
        String accountName = oldUser.getRealName();//开户名
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
