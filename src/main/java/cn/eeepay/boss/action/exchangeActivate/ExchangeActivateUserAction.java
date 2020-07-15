package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AccountInfo;
import cn.eeepay.framework.model.AccountInfoRecord;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.exchange.MerchantFreeze;
import cn.eeepay.framework.model.exchange.SettlementCard;
import cn.eeepay.framework.model.exchange.Subordinate;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUser;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUserCard;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateUserService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 超级兑用户 Action
 */
@Controller
@RequestMapping(value = "/exchangeActivateUser")
public class ExchangeActivateUserAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateUserAction.class);

    @Resource
    private ExchangeActivateUserService exchangeActivateUserService;

    /**
     * 查询用户列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateUser> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateUser userManagement = JSONObject.parseObject(param, ExchangeActivateUser.class);
            exchangeActivateUserService.selectAllList(userManagement, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询用户列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询用户列表失败!");
        }
        return msg;
    }

    /**
     * 查询用户详情
     */
    @RequestMapping(value = "/getUserManagement")
    @ResponseBody
    public Map<String,Object> getUserManagement(@RequestParam("merchantNo") String merchantNo) throws Exception{
        return getUserDetail(merchantNo,0);
    }

    /**
     * 查询用户详情(修改)
     */
    @RequestMapping(value = "/getUserManagementEdit")
    @ResponseBody
    public Map<String,Object> getUserManagementEdit(@RequestParam("merchantNo") String merchantNo) throws Exception{
        return getUserDetail(merchantNo,1);
    }
    /**
     * 敏感信息获取
     */
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    public Map<String,Object> getDataProcessing(@RequestParam("merchantNo") String merchantNo) throws Exception{
        return getUserDetail(merchantNo,3);
    }

    private Map<String,Object> getUserDetail(String merchantNo,int editState){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            if(merchantNo!=null&&!"".equals(merchantNo)){
                ExchangeActivateUser user = exchangeActivateUserService.getUserManagement(merchantNo);
                if(0==editState){
                    user.setMobileUsername(StringUtil.sensitiveInformationHandle(user.getMobileUsername(),0));
                    user.setIdCardNo(StringUtil.sensitiveInformationHandle(user.getIdCardNo(),1));
                }
                //账户余额
                List<AccountInfo> balance=exchangeActivateUserService.getUserBalance(merchantNo);
                if(user!=null){
                    if(StringUtils.isNotBlank(user.getWechatQrcode()) ){
                        user.setWechatQrcode(CommonUtil.getImgUrlAgent(user.getWechatQrcode()));
                    }
                    List<MerchantFreeze> freezeList=exchangeActivateUserService.getUserFreezeHis(user.getMerchantNo());
                    msg.put("freezeList",freezeList);
                    List<ExchangeActivateUserCard> cardList=exchangeActivateUserService.getUserCard(user.getMerchantNo(),"1");
                    if(cardList!=null&&cardList.size()>0){
                        ExchangeActivateUserCard card=cardList.get(0);
                        if(StringUtils.isNotBlank(card.getYhkzmUrl())){
                            card.setYhkzmUrl(CommonUtil.getImgUrlAgent(card.getYhkzmUrl()));
                        }
                        if(0==editState) {
                            card.setMobileNo(StringUtil.sensitiveInformationHandle(card.getMobileNo(), 0));
                        }
                        msg.put("card",card);
                    }
                }
                msg.put("user",user);
                msg.put("balance",balance);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "查询用户详情失败!");
            }
        } catch (Exception e){
            log.error("查询用户详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询用户详情失败!");
        }
        return msg;
    }
    /**
     * 查询用户账户交易记录
     */
    @RequestMapping(value="/getUserAccountDetail")
    @ResponseBody
    public Result getAccountTranInfo(@RequestBody AccountInfoRecord record,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try{
            if(checkOrderDate(record.getRecordTimeStart(), record.getRecordTimeEnd(), result)){
                return result;
            }
            result = exchangeActivateUserService.getAccountTranInfo(record, pageNo, pageSize);
        }catch(Exception e){
            log.error("超级银行家账户明细异常",e);
        }
        return result;
    }
    /**
     * 校验查询时间间隔不能超过三个月
     * @param createTimeStartStr
     * @param createTimeEndStr
     * @param result
     * @return
     */
    private boolean checkOrderDate(String createTimeStartStr,String createTimeEndStr, Result result) {
        if(StringUtils.isBlank(createTimeStartStr)
                || StringUtils.isBlank(createTimeEndStr)) {
            result.setMsg("交易时间不能为空");
            return true;
        }
        Date createTimeStart = DateUtils.parseDateTime(createTimeStartStr);
        Date createTimeEnd = DateUtils.parseDateTime(createTimeEndStr);
        if(createTimeEnd.getTime() < createTimeStart.getTime()){
            result.setMsg("交易结束时间时间不能小于起始时间");
            return true;
        }
        if(createTimeEnd.getTime() - createTimeStart.getTime() > 3*30*24*60*60*1000L){
            result.setMsg("交易时间间隔不能超过三个月");
            return true;
        }
        return false;
    }

    /**
     * 修改用户信息
     */
    @RequestMapping(value = "/updateUserManagement")
    @ResponseBody
    @SystemLog(description = "修改用户信息",operCode="exchangeActivateUser.updateUserManagement")
    public Map<String,Object> updateUserManagement(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateUser userManagement = JSONObject.parseObject(param, ExchangeActivateUser.class);
            int num =exchangeActivateUserService.updateUserManagement(userManagement);
            if(num>0){
                msg.put("msg","修改成功!");
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "修改用户信息失败!");
            }
        } catch (Exception e){
            log.error("修改用户信息失败!",e);
            msg.put("status", false);
            msg.put("msg", "修改用户信息失败!");
        }
        return msg;
    }

    /**
     * 修改用户结算卡信息
     */
    @RequestMapping(value = "/updateSettlementCard")
    @ResponseBody
    @SystemLog(description = "修改用户结算卡信息",operCode="exchangeActivateUser.updateSettlementCard")
    public Map<String,Object> updateSettlementCard(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SettlementCard settlementCard = JSONObject.parseObject(param, SettlementCard.class);
            exchangeActivateUserService.updateSettlementCard(settlementCard,msg);
        } catch (Exception e){
            log.error("修改超级兑用户信息失败!",e);
            msg.put("status", false);
            msg.put("msg", "修改超级兑用户信息失败!");
        }
        return msg;
    }


    /**
     * 导出超级兑用户列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        ExchangeActivateUser user = JSONObject.parseObject(param, ExchangeActivateUser.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<ExchangeActivateUser> list=exchangeActivateUserService.importDetailSelect(user);
        try {
            exchangeActivateUserService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出超级兑用户异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出超级兑用户异常!");
        }
        return msg;
    }

    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/getUserManagementList")
    @ResponseBody
    public Map<String,Object> getUserManagementList(@RequestParam("merchantNo") String merchantNo) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<ExchangeActivateUser> list=exchangeActivateUserService.getUserManagementList(merchantNo);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("修改超级兑用户信息失败!",e);
            msg.put("status", false);
            msg.put("msg", "修改超级兑用户信息失败!");
        }
        return msg;
    }

    /**
     * 用户预冻结
     */
    @RequestMapping(value = "/userFreeze")
    @ResponseBody
    @SystemLog(description = "用户预冻结",operCode="exchangeActivateUser.userFreeze")
    public Map<String,Object> userFreeze(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            MerchantFreeze freeze = JSONObject.parseObject(param, MerchantFreeze.class);
            int num=exchangeActivateUserService.userFreeze(freeze);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("用户预冻结异常!",e);
            msg.put("status", false);
            msg.put("msg", "用户预冻结异常!");
        }
        return msg;
    }
}
