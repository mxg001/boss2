package cn.eeepay.boss.action.allAgent;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.CountSet;
import cn.eeepay.framework.model.allAgent.UserAllAgent;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.allAgent.UserAllAgentService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.StringUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/12/012.
 * @author  liuks
 * 超级盟主用户
 */
@Controller
@RequestMapping(value = "/userAllAgent")
public class UserAllAgentAction {

    private static final Logger log = LoggerFactory.getLogger(UserAllAgentAction.class);

    @Resource
    private UserAllAgentService userAllAgentService;
    @Resource
    private SysDictService sysDictService;

    /**
     * 用户查询
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<UserAllAgent> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            UserAllAgent user = JSONObject.parseObject(param, UserAllAgent.class);
            userAllAgentService.selectAllList(user, page);
            CountSet countSet=userAllAgentService.selectAllSum(user, page);
            msg.put("countSet",countSet);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("用户查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "用户查询异常!");
        }
        return msg;
    }

    /**
     * 导出超级盟主用户列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        UserAllAgent user = JSONObject.parseObject(param, UserAllAgent.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<UserAllAgent> list=userAllAgentService.importDetailSelect(user);
        try {
            userAllAgentService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出超级盟主用户列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出超级盟主用户列表异常!");
        }
        return msg;
    }

    /**
     * 获取用户详情
     */
    @RequestMapping(value = "/getUserAllAgent")
    @ResponseBody
    public Map<String,Object> getUserAllAgent(@RequestParam("id") int id) throws Exception{
        return getDetail(id,0);
    }
    /**
     * 敏感信息获取
     */
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    public Map<String,Object> getDataProcessing(@RequestParam("id") int id) throws Exception{
        return getDetail(id,3);
    }

    private Map<String,Object> getDetail(int id,int editState){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            UserAllAgent user=userAllAgentService.getUserAllAgent(id);
            if(0==editState){
                user.setMobile(StringUtil.sensitiveInformationHandle(user.getMobile(),0));
                user.setIdCardNo(StringUtil.sensitiveInformationHandle(user.getIdCardNo(),1));
                if(user.getCard()!=null){
                    user.getCard().setMobile(StringUtil.sensitiveInformationHandle(user.getCard().getMobile(),0));
                }
            }
            msg.put("user",user);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取用户详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取用户详情异常!");
        }
        return msg;
    }

    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/getOrgList")
    @ResponseBody
    public Map<String,Object> getOrgList(@RequestParam("brandCodes") String brandCodes,
                                              @RequestParam("userType") int userType) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<UserAllAgent> list=userAllAgentService.getOrgList(brandCodes,userType);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取用户列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取用户列表异常!");
        }
        return msg;
    }

    /**
     * 盟主编辑
     */
    @RequestMapping(value = "/saveUserAllAgent")
    @ResponseBody
    @SystemLog(description = "盟主编辑", operCode = "userAllAgent.saveUserAllAgent")
    public Map<String, Object> saveUserAllAgent(@RequestParam("info") String info) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            UserAllAgent user = JSONObject.parseObject(info, UserAllAgent.class);
            int num = userAllAgentService.saveUserAllAgent(user);
            if (num > 0) {
                msg.put("msg", "修改成功!");
                msg.put("status", true);
            } else {
                msg.put("msg", "修改失败!");
                msg.put("status", false);
            }
        } catch (Exception e) {
            log.error("盟主编辑异常!", e);
            msg.put("status", false);
            String str=e.getMessage();
            if(str==null){
                msg.put("msg", "盟主编辑异常!");
            }else{
                msg.put("msg", e.getMessage());
            }
        }
        return msg;
    }

    /**
     * 导出分润比例调整记录
     */
    @RequestMapping(value="/importDividedAdjustDetail")
    @ResponseBody
    public Map<String, Object> importDividedAdjustDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        UserAllAgent user = JSONObject.parseObject(param, UserAllAgent.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<UserAllAgent> list=userAllAgentService.selectDividedAdjustDetail(user);
        try {
            userAllAgentService.importDividedAdjustDetail(list,response);
        }catch (Exception e){
            log.error("导出分润比例调整记录异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出分润比例调整记录异常!");
        }
        return msg;
    }

    /**
     * 交易分润比例调整记录
     * @param param
     * @return
     */
    @RequestMapping(value="/selectDividedAdjustDetail")
    @ResponseBody
    public Map<String, Object> selectDividedAdjustDetail(@RequestParam("userCode") String param, @ModelAttribute("page")
            Page<UserAllAgent> page){
        UserAllAgent user = new UserAllAgent();
        user.setUserCode(param);
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            userAllAgentService.selectDividedAdjustDetailList(user,page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("交易分润比例调整记录异常!",e);
            msg.put("status", false);
            msg.put("msg", "交易分润比例调整记录异常!");
        }
        return msg;
    }

    @RequestMapping(value="/selectOneUserCodeList")
    @ResponseBody
    public Object selectOneUserCodeList(){
        List<UserAllAgent> list=null;
        try {
            list=userAllAgentService.selectOneUserCodeList();
        } catch (Exception e){
            log.error("交易分润比例调整记录异常!",e);
        }
        return list;
    }

    /**
     * 分润账户明细
     * 224105余额账户科目号  224124机具款项科目号
     * @return
     */
    @RequestMapping(value="/selectDivided")
    @ResponseBody
    public Map<String, Object> selectDivided(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            UserAllAgent userInfo = JSONObject.parseObject(param, UserAllAgent.class);
            UserAllAgent user=userAllAgentService.getUserAllAgent(userInfo.getId());
            userInfo.setUserCode(user.getUserCode());
            userInfo.setUserType(user.getUserType());
            String result=allAgentIncomeDetails(userInfo,"224105");
            JSONObject jsonObject= JSONObject.parseObject(result);
            if(jsonObject!=null&&jsonObject.getInteger("status")==200){
                msg.put("totalAmount",jsonObject.getJSONObject("data").getString("totalAmount"));
                msg.put("canDrawAmount",jsonObject.getJSONObject("data").getString("canDrawAmount"));
                msg.put("remainAmount",user.getUserType()!=1?0:jsonObject.getJSONObject("data").getString("remainAmount"));
                msg.put("withdrawAmount",jsonObject.getJSONObject("data").getString("withdrawAmount"));
                msg.put("incomeDetails",jsonObject.getJSONObject("data").getJSONArray("incomeDetails"));
                msg.put("status", true);
            }else{
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("获取分润账户明细异常!",e);
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 机具款项账户明细
     * 224105余额账户科目号  224124机具款项科目号
     * @return
     */
    @RequestMapping(value="/selectMachine")
    @ResponseBody
    public Map<String, Object> selectMachine(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            UserAllAgent userInfo = JSONObject.parseObject(param, UserAllAgent.class);
            UserAllAgent user=userAllAgentService.getUserAllAgent(userInfo.getId());
            userInfo.setUserCode(user.getUserCode());
            userInfo.setUserType(user.getUserType());
            String result=allAgentIncomeDetails(userInfo,"224124");
            JSONObject jsonObject= JSONObject.parseObject(result);
            if(jsonObject!=null&&jsonObject.getInteger("status")==200){
                msg.put("totalAmount",jsonObject.getJSONObject("data").getString("totalAmount"));
                msg.put("canDrawAmount",jsonObject.getJSONObject("data").getString("canDrawAmount"));
                msg.put("remainAmount",user.getUserType()!=1?0:jsonObject.getJSONObject("data").getString("remainAmount"));
                msg.put("withdrawAmount",jsonObject.getJSONObject("data").getString("withdrawAmount"));
                msg.put("incomeDetails",jsonObject.getJSONObject("data").getJSONArray("incomeDetails"));
                msg.put("status", true);
            }else{
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("机具款项账户明细异常!",e);
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 账户明细
     * @param userAllAgent
     * @param subNo 224105余额账户科目号  224124机具款项科目号
     *              接口响应：
     * {
     *     "status": 200,
     *     "msg": "查询成功",
     *     "data": {
     *         "totalAmount": "2.01",--总收入
     *         "canDrawAmount": "0.00",--可提现余额
     *         "remainAmount": "108.00",--留存金额
     *         "withdrawAmount": "5115.00",--已提现金额
     *         "incomeDetails": [
     *             {
     *                 "amount": "2.00",--收入金额
     *                 "incomeType": "000097",--收入类型
     *                 "incomeTime": "2018-09-12 16:18:15",--收入时间
     *                 "side": "credit",收入正负 credit代表+  debit代表-
     *                 "summaryInfo": null--收入备注
     *             },
     *             {
     *                 "amount": "0.01",
     *                 "incomeType": "000097",
     *                 "incomeTime": "2018-09-12 16:18:22",
     *                 "side": "credit",
     *                 "summaryInfo": null
     *             }
     *         ]
     *     }
     * }
     * @return
     */
    public String allAgentIncomeDetails(UserAllAgent userAllAgent,String subNo) {
        String url=sysDictService.getValueByKey("ALLAGENT_SERVICE_URL");
        url+="/income_details";
        Map<String, String> claims = new HashMap<>();
        claims.put("userCode", userAllAgent.getUserCode());//必传
        claims.put("subNo", subNo);//必传 224105余额账户科目号  224124机具款项科目号
        claims.put("userType", userAllAgent.getUserType()+"");// 用户类型 必传 1：机构，2：大盟主，3：盟主'
        claims.put("startDate", userAllAgent.getStartDate()==null?"":userAllAgent.getStartDate());//格式1970-02-02
        claims.put("endDate", userAllAgent.getEndDate()==null?"":userAllAgent.getEndDate());
        claims.put("transType", userAllAgent.getTransType()==null?"":userAllAgent.getTransType());//收支类别 如：000097
        String accountMsg = ClientInterface.httpPost(url, claims);
        log.info("url:{}，subNo:{},param:{}，response:{}",url,subNo,claims,accountMsg);
        return accountMsg;
    }

    /**
     * 根据条件获取用户列表
     */
    @RequestMapping(value = "/getUserByStr")
    @ResponseBody
    public Map<String,Object> getUserByStr(@RequestParam("str") String str,@RequestParam("level") String level) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<UserAllAgent> list =userAllAgentService.getUserByStr(str,level);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("根据条件获取用户列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "根据条件获取用户列表异常!");
        }
        return msg;
    }


    /**
     * 重置密码
     */
    @RequestMapping(value = "/resetPassword")
    @ResponseBody
    public Map<String,Object> resetPassword(@RequestParam("userCode") String userCode) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            userAllAgentService.resetPassword(userCode);
            msg.put("status", true);
            msg.put("msg", "重置密码成功!");
        } catch (Exception e){
            log.error("重置密码异常!",e);
            msg.put("status", false);
            msg.put("msg", "重置密码失败!");
        }
        return msg;
    }
}
