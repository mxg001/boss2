package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.rpc.entity.ZhengBonusConfEntity;
import cn.eeepay.framework.rpc.entity.ZxApplyProductsEntity;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UserFreezeOperLogService;
import cn.eeepay.framework.service.ZhengBonusConfService;
import cn.eeepay.framework.service.ZxProductOrderService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;


/**
 * @author tans
 * @date 2017-11-29
 * @desc 超级银行家
 */
@Controller
@RequestMapping("/superBank")
public class SuperBankAction {

    private final static Logger log = LoggerFactory.getLogger(SuperBankAction.class);

    @Resource
    private SuperBankService superBankService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private ZhengBonusConfService zhengBonusConfService;

    @Resource
    private ZxProductOrderService zxProductOrderService;
    
    @Resource
	private UserFreezeOperLogService userFreezeOperLogService;
    /**
     * 用户管理查询
     * @param info
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/userInfoManager")
    @ResponseBody
    public Result userInfoManager(@RequestBody SuperBankUserInfo info,
                                  @RequestParam( defaultValue = "1")int pageNo,
                                  @RequestParam( defaultValue = "10")int pageSize) {
        Result result = new Result();
        try{
            Page<SuperBankUserInfo> page = new Page<>(pageNo, pageSize);
            if(info == null){
                result.setMsg("参数不能为空");
                return result;
            }
            //如果有索引的参数为空，则校验查询时间区间在一年内
            if(StringUtils.isBlank(info.getUserCode()) && info.getOrgId()==null){
                if(checkOrderDate(info.getCreateDateStart(), info.getCreateDateEnd(),result, 365)){
                    return result;
                };
            }
            superBankService.selectUserInfoPage(info,page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        }catch(Exception e){
            log.error("用户管理查询异常!",e);
        }
        return result;
    }

    /**
     * 用户管理查询
     * @param info
     * @return
     */
    @RequestMapping(value = "/getUserTotal")
    @ResponseBody
    public Result getUserTotal(@RequestBody SuperBankUserInfo info) {
        Result result = new Result();
        try{
            //如果有索引的参数为空，则校验查询时间区间在一年内
            if(StringUtils.isBlank(info.getUserCode()) && info.getOrgId()==null){
                if(checkOrderDate(info.getCreateDateStart(), info.getCreateDateEnd(),result,365)){
                    return result;
                };
            }
            Map<String, Object> map = superBankService.getUserTotal(info);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        }catch(Exception e){
            log.error("用户管理查询异常!",e);
        }
        return result;
    }

    /**
     * 用户管理详情
     * @param userCode
     * @return
     */
    @RequestMapping(value = "/selectUserDetail")
    @ResponseBody
    public Result selectUserDetail(@RequestParam("userCode")String userCode) {
        Result result = new Result();
        try{
            result = superBankService.selectUserDetail(userCode);
        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家用户详情异常",e);
        }
        return result;
    }

    /**
     * 获取用户账户信息
     * @param userCode
     * @return
     */
    @RequestMapping(value="/getUserAccountInfo")
    @ResponseBody
    public Result getUserAccountInfo(@RequestParam("userCode") String userCode) {
        Result result = new Result();
        try{
            result = superBankService.getUserAccountInfo(userCode);
        }catch(Exception e){
            log.error("获取超级银行家用户账户信息异常",e);
        }
        return result;
    }

    /**
     * 获取商户账户交易记录
     * @param record
     * @param pageNo
     * @param pageSize
     * @return
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
            result = superBankService.getAccountTranInfo(record, pageNo, pageSize);
        }catch(Exception e){
            log.error("超级银行家账户明细异常",e);
        }
        return result;
    }

    /**
	 * 获取用户冻结解冻日志记录
	 * 
	 * @param record
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/getUserFreezeOperLog")
	@ResponseBody
	public Result getUserFreezeOperLog(@RequestParam("userCode") String userCode,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Result result = new Result();
		try {
			Page<UserFreezeOperLog> page = new Page<>(pageNo, pageSize);
			if(StringUtils.isBlank(userCode)) {
				result.setMsg("参数不能为空");
				return result;
			}
			
			userFreezeOperLogService.getUserFreezeOperLog(userCode, page);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(page);
		} catch (Exception e) {
			log.error("超级银行家用户冻结解冻异常!", e);
		}
		return result;
	}
	
    /**
     *
     * @return
     */
    @RequestMapping(value="/changeSource")
    @ResponseBody
    public Result changeSource(String type) {
        Result result = new Result();
        try{
            List<LoanSource> list = superBankService.changeSource(type);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(list);
        }catch(Exception e){
            log.error("超级银行家账户明细异常",e);
        }
        return result;
    }

    /**
     * 修改超级银行家用户
     * @param baseInfo
     * @return
     */
    @RequestMapping("/updateUserInfo")
    @ResponseBody
    @SystemLog(operCode = "superBank.updateUserInfo", description = "修改超级银行家用户")
    public Result updateUserInfo(@RequestBody SuperBankUserInfo baseInfo){
        Result result = new Result();
        try {
            result = superBankService.updateUserInfo(baseInfo);
        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改超级银行家用户异常", e);
        }
        return result;
    }

    @RequestMapping("/lotteryJob")
    @ResponseBody
    public Result lotteryJob(){
        Result result = new Result();
        superBankService.lotteryMatchTask();
        result.setStatus(true);
        result.setMsg("完成");
        return result;
    }

    /**
     * 通过卡号获取结算卡相关信息
     * @param cardNo
     * @return
     */
    @RequestMapping("/getCardInfo")
    @ResponseBody
    public Result getCardInfo(String cardNo){
        Result result = new Result();
        try {
            result = superBankService.getCardInfo(cardNo);
        } catch (Exception e){
            result = result = ResponseUtil.buildResult(e);
            log.error("获取结算卡信息异常", e);
        }
        return result;
    }

    /**
     * 获取支行信息
     * @param posCnaps
     * @return
     */
    @RequestMapping("/getPosCnaps")
    @ResponseBody
    public Result getPosCnaps(@RequestBody PosCnaps posCnaps){
        Result result = new Result();
        try {
            String bankName = posCnaps.getBankName();
            String cityName = posCnaps.getAddress();
            result = superBankService.getPosCnaps(bankName, cityName);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("获取获取支行信息异常", e);
        }
        return result;
    }

    /**
     * 修改超级银行家结算卡
     * @param baseInfo
     * @return
     */
    @RequestMapping("/updateUserCard")
    @ResponseBody
    @SystemLog(operCode = "superBank.updateUserCard", description = "修改超级银行家结算卡")
    public Result updateUserCard(@RequestBody SuperBankUserCard baseInfo){
        Result result = new Result();
        try {
            result = superBankService.updateUserCard(baseInfo);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改超级银行家结算卡信息异常",e);
        }
        return result;
    }

    /**
     * 用户查询导出
     * @param info
     * @param response
     * @param request
     */
    @RequestMapping(value="/exportUserInfo")
    public void exportUserInfo(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request){
        try {
            if(StringUtils.isNotBlank(info)){
                info = URLDecoder.decode(info, "utf-8");
            }
            SuperBankUserInfo userInfo = JSONObject.parseObject(info, SuperBankUserInfo.class);
            if(userInfo == null){
                return;
            }
            //如果有索引的参数为空，则校验查询时间区间在一年内
            if(StringUtils.isBlank(userInfo.getUserCode()) && userInfo.getOrgId()==null){
                if(checkOrderDate(userInfo.getCreateDateStart(), userInfo.getCreateDateEnd(),new Result(), 365)){
                    return;
                };
            }

            superBankService.exportUserInfo(response, userInfo);
        } catch (Exception e){
            log.error("导出超级银行家用户管理异常", e);
        }
    }

    /**
     * 银行家组织分页查询
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/orgInfoManager")
    @ResponseBody
    public Result orgInfoManager(@RequestBody OrgInfo baseInfo,
                                 @RequestParam(defaultValue = "1") int pageNo,
                                 @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            Page<OrgInfo> page = new Page<>(pageNo, pageSize);
            superBankService.selectOrgInfoPage(baseInfo, page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e) {
            log.error("超级银行家组织管理异常", e);
            result.setMsg("查询异常");
        }
        return result;
    }

    /**
     * 用户管理详情
     * @return
     */
    @RequestMapping(value = "/getOrgWxTemplate")
    @ResponseBody
    public Result getOrgWxTemplate(@RequestBody OrgWxTemplate orgWxTemplat,@RequestParam(defaultValue = "1") int pageNo,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        Page<OrgWxTemplate> page = new Page<>(pageNo, pageSize);
        try{
            superBankService.getByPager(orgWxTemplat,page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家微信模板异常",e);
        }
        return result;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/querySysOptionList")
    @ResponseBody
    public Result getOrgWxTemplate() {
        Result result = new Result();
        try{
            List<TSysOption> tSysOptions = superBankService.querySysOptionList();
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(tSysOptions);
        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家微信模板异常",e);
        }
        return result;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/queryOrgSourcConfList")
    @ResponseBody
    public Result queryOrgSourcConfList(@RequestBody OrgSourceConf orgSourcConf,@RequestParam(defaultValue = "1") int pageNo,
                                        @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        Page<OrgSourceConf> page = new Page<>(pageNo, pageSize);
        try{
            List<OrgSourceConf> list = superBankService.queryOrgSourcConfList(orgSourcConf,page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(list);
        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家组织屏蔽功能异常",e);
        }
        return result;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/deleteOrgSourceConf")
    @ResponseBody
    public Result deleteOrgSourceConf(@RequestBody OrgSourceConf orgSourcConf) {
        Result result = new Result();
        try{
            superBankService.deleteOrgSourceConf(orgSourcConf);
            result.setStatus(true);
            result.setMsg("删除成功");
        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家组织屏蔽功能异常",e);
        }
        return result;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/addOrgWxTemplate")
    @ResponseBody
    public Result addOrgWxTemplate(@RequestBody OrgWxTemplate orgWxTemplat) {
        Result result = new Result();
        try{
            superBankService.saveOrgWxTemplate(orgWxTemplat);
            result.setStatus(true);
            result.setMsg("保存成功");
        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家微信模板保存异常",e);
        }
        return result;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/saveOrgSourceConf")
    @ResponseBody
    public Result saveOrgSourceConf(@RequestBody OrgSourceConf orgSourceConf) {
        Result result = new Result();
        try{
            superBankService.saveOrgSourceConf(orgSourceConf);
            result.setStatus(true);
            result.setMsg("保存成功");
        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家组织功能屏蔽保存异常",e);
        }
        return result;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/updateOrgWxTemplate")
    @ResponseBody
    public Result updateOrgWxTemplate(@RequestBody OrgWxTemplate orgWxTemplat) {
        Result result = new Result();
        try{
            superBankService.updOrgWxTemplate(orgWxTemplat);
            result.setStatus(true);
            result.setMsg("修改成功");
        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家微信模板改异常",e);
        }
        return result;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/sysOptionUpd")
    @ResponseBody
    public Result sysOptionUpd(@RequestBody TSysOption tSysOption) {
        Result result = new Result();
        try{
            superBankService.updsysOption(tSysOption);
            SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            String back = ClientInterface.superBankSysOption(sysDict.getSysValue()+Constants.REFRESH_SYS_OPTION);
            if(!StringUtils.isBlank(back)){
                JSONObject jsonObject = JSONObject.parseObject(back);
                if(jsonObject.containsValue("success")){
                    result.setStatus(true);
                    result.setMsg("修改成功");
                }else{
                    result.setStatus(false);
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setStatus(false);
                result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            }

        }catch(Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("超级银行家字典修改异常",e);
        }
        return result;
    }

    /**
     * 获取直营组织的费率信息、代理升级方案、代理退费方案、普通用户奖励方案，分润账户留存金额
     * @return
     */
    @RequestMapping("/getBaseOrgInfo")
    @ResponseBody
    public Result getBaseOrgInfo(){
        Result result = new Result();
        try {
            OrgInfo orgInfo = superBankService.getBaseOrgInfo();
            if(orgInfo != null){
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(orgInfo);
            } else {
                result.setMsg("未查询到直营信息");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("获取直营组织信息异常", e);
        }
        return result;
    }


    /**
     * 获取开放平台功能点配置
     * @return
     */
    @RequestMapping("/getOpenFunctionConfList")
    @ResponseBody
    public Result getOpenFunctionConfList(){
        Result result = new Result();
        try {
            List<OpenFunctionConf> list = superBankService.getOpenFunctionConfList();
            if(list != null&&list.size()>0){
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(list);
            } else {
                result.setMsg("开放平台功能点配置信息");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("开放平台功能点配置信息信息异常", e);
        }
        return result;
    }

    /**
     * 新增超级银行家组织
     * @param orgInfo
     * @return
     */
    @RequestMapping("/addOrgInfo")
    @ResponseBody
    @SystemLog(operCode = "superBank.addOrgInfo", description = "新增超级银行家组织")
    public Result addOrgInfo(@RequestBody OrgInfo orgInfo){
        Result result = new Result();
        result.setMsg("操作失败");
        if(!"1".equals(orgInfo.getUpMemberNeedpay()) && !"1".equals(orgInfo.getUpMemberNeedperfect()) && !"1".equals(orgInfo.getUpMemberNeedlock()) ){
            result.setMsg("升级专员条件必须至少填写一项");
            result.setStatus(false);
            return result;
        }else{

            if("1".equals(orgInfo.getUpMemberNeedlock()) && orgInfo.getUpMemberLocknum()==null){
                result.setMsg("升级专员条件 勾选锁粉人数后需填写人数数量");
                result.setStatus(false);
                return result;
            }
            if("1".equals(orgInfo.getUpMemberNeedlock()) && orgInfo.getUpMemberLocknum()<=0){
                result.setMsg("升级专员条件 勾选锁粉人数后需填写人数数量 请检查");
                result.setStatus(false);
                return result;
            }

        }
        if(orgInfo.getUpMemberMposnum()!=null && orgInfo.getUpMemberMposnum()<=0){
            result.setMsg("升级专员条件 采购mpos机数量请填写大于零的值");
            result.setStatus(false);
            return result;
        }
        if(orgInfo.getUpManagerNum()==null && orgInfo.getUpManagerCardnum()==null && orgInfo.getUpManagerLocknum()==null ){
            result.setMsg("升级经理条件必须至少填写一项");
            result.setStatus(false);
            return result;
        }
        if(orgInfo.getUpManagerNum()!=null && orgInfo.getUpManagerNum()<=0){         //升级经理条件 第一项小于0  判断其他两项
            if((orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0)||orgInfo.getUpManagerCardnum()==null){
                if((orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()<=0)||orgInfo.getUpManagerLocknum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
            if((orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()<=0)||orgInfo.getUpManagerLocknum()==null){
                if((orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0)||orgInfo.getUpManagerCardnum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
        }
        if(orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0) {     //升级经理条件 第二项等于-1  判断其他两项
            if((orgInfo.getUpManagerNum()!=null &&orgInfo.getUpManagerNum()<=0)||orgInfo.getUpManagerNum()==null){
                if((orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()==-1)||orgInfo.getUpManagerLocknum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
            if((orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()<=0)||orgInfo.getUpManagerLocknum()==null){
                if((orgInfo.getUpManagerNum()!=null &&orgInfo.getUpManagerNum()<=0)||orgInfo.getUpManagerNum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

        }
        if(orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()<=0) {    //升级经理条件 第三项等于-1  判断其他两项
            if((orgInfo.getUpManagerNum()!=null && orgInfo.getUpManagerNum()<=0)||orgInfo.getUpManagerNum()==null){
                if((orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0)||orgInfo.getUpManagerCardnum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
            if((orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0)||orgInfo.getUpManagerCardnum()==null){
                if((orgInfo.getUpManagerNum()!=null && orgInfo.getUpManagerNum()<=0)||orgInfo.getUpManagerNum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

        }
        if(orgInfo.getUpManagerMposnum()!=null && orgInfo.getUpManagerMposnum()<=0){
            result.setMsg("升级经理条件 采购mpos机数量请填写大于零的值");
            result.setStatus(false);
            return result;
        }

        if(orgInfo.getUpBankerNum()==null && orgInfo.getUpBankerCardnum()==null && orgInfo.getUpBankerLocknum()==null ){
            result.setMsg("升级银行家条件必须至少填写一项");
            result.setStatus(false);
            return result;
        }
        if(orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0){       //升级银行家条件第一项 判断其他两项
            if((orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0)||orgInfo.getUpBankerCardnum()==null){
                if((orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0)||orgInfo.getUpBankerLocknum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

            if((orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0)||orgInfo.getUpBankerLocknum()==null){
                if((orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0)||orgInfo.getUpBankerCardnum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
        }
        if(orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0){      //升级银行家条件第二项 判断其他两项
            if((orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0)||orgInfo.getUpBankerNum()==null){
                if((orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0)||orgInfo.getUpBankerLocknum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

            if((orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0)||orgInfo.getUpBankerLocknum()==null){
                if((orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0)||orgInfo.getUpBankerNum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

        }
        if(orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0){      //升级银行家条件第三项 判断其他两项
            if((orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0)||orgInfo.getUpBankerNum()==null){
                if((orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0)||orgInfo.getUpBankerCardnum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

            if((orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0)||orgInfo.getUpBankerCardnum()==null){
                if((orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0)||orgInfo.getUpBankerNum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
        }
        if(orgInfo.getUpBankerMposnum()!=null && orgInfo.getUpBankerMposnum()<=0){
            result.setMsg("升级银行家条件 采购mpos机数量请填写大于零的值");
            result.setStatus(false);
            return result;
        }
        try {
            result = superBankService.addOrgInfo(orgInfo);
            if("3".equals(orgInfo.getIndexStyle()) || "4".equals(orgInfo.getIndexStyle())){
                Long orgId = orgInfo.getOrgId();
                //插入优秀导师模块
                if(orgInfo.getTutorModelList()!= null&&orgInfo.getTutorModelList().size()>0){
                    superBankService.insertModuleBatch(orgInfo.getTutorModelList(), orgId);
                }
                //插入银行家大学-宣传展示模块
                if(orgInfo.getBankModelList()!= null&&orgInfo.getBankModelList().size()>0){
                    superBankService.insertModuleBatch(orgInfo.getBankModelList(), orgId);
                }

            }
            List<OrgBusinessConf> orgBusinessConfList = superBankService.selectOrgBusinessConfByOrgId(orgInfo.getOrgId());
            String[] businessIdArray=null;
            if (StringUtils.isNotEmpty(orgInfo.getCheckIds())) {
                businessIdArray = orgInfo.getCheckIds().split(",");

            }
            List<OrgBusinessConf> updateOrgBusinessConfList = getUpdateOrgBusinessConfList(businessIdArray,orgBusinessConfList,orgInfo.getOrgId());
            superBankService.saveOrUpdateOrgBusinessConfList(updateOrgBusinessConfList);    //更新业务

            if(result.isStatus()){
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                String back = ClientInterface.superBankPushByOrgIdAndUrl(orgInfo.getOrgId().toString(),sysDict.getSysValue()+Constants.REFRESH_ORG_INFO,null,null);
                if(!StringUtils.isBlank(back)){
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if(jsonObject.containsValue("success")){
                    }else{
                        result.setStatus(false);
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                }else{
                    result.setStatus(false);
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("新增超级银行家组织管理异常", e);
        }
        return result;
    }

    /**
     * 修改超级银行家组织
     * @param orgInfo
     * @return
     */
    @RequestMapping("/updateOrgInfo")
    @ResponseBody
    @SystemLog(operCode = "superBank.updateOrgInfo", description = "修改超级银行家组织")
    public Result updateOrgInfo(@RequestBody OrgInfo orgInfo){
        Result result = new Result();
        if(orgInfo == null){
            result.setMsg("参数不能为空");
            return result;
        }
        if(orgInfo.getOrgId() == null){
            result.setMsg("orgId不能为空");
            return result;
        }
        if(!superBankService.checkExistsOrgId(orgInfo.getOrgId())){
            result.setMsg("orgId不存在");
            return result;
        }
        if(!"1".equals(orgInfo.getUpMemberNeedpay()) && !"1".equals(orgInfo.getUpMemberNeedperfect()) && !"1".equals(orgInfo.getUpMemberNeedlock()) ){
            result.setMsg("升级专员条件必须至少填写一项");
            result.setStatus(false);
            return result;
        }else{

            if("1".equals(orgInfo.getUpMemberNeedlock()) && orgInfo.getUpMemberLocknum()==null){
                result.setMsg("升级专员条件 勾选锁粉人数后需填写人数数量");
                result.setStatus(false);
                return result;
            }
            if("1".equals(orgInfo.getUpMemberNeedlock()) && orgInfo.getUpMemberLocknum()<=0){
                result.setMsg("升级专员条件 勾选锁粉人数后需填写人数数量 请检查");
                result.setStatus(false);
                return result;
            }

        }
        if(orgInfo.getUpMemberMposnum()!=null && orgInfo.getUpMemberMposnum()<=0){
            result.setMsg("升级专员条件 采购mpos机数量请填写大于零的值");
            result.setStatus(false);
            return result;
        }
        if(orgInfo.getUpManagerNum()==null && orgInfo.getUpManagerCardnum()==null && orgInfo.getUpManagerLocknum()==null ){
            result.setMsg("升级经理条件必须至少填写一项");
            result.setStatus(false);
            return result;
        }
        if(orgInfo.getUpManagerNum()!=null && orgInfo.getUpManagerNum()<=0){         //升级经理条件 第一项小于0  判断其他两项
            if((orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0)||orgInfo.getUpManagerCardnum()==null){
                if((orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()<=0)||orgInfo.getUpManagerLocknum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
            if((orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()<=0)||orgInfo.getUpManagerLocknum()==null){
                if((orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0)||orgInfo.getUpManagerCardnum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
        }
        if(orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0) {     //升级经理条件 第二项等于-1  判断其他两项
            if((orgInfo.getUpManagerNum()!=null &&orgInfo.getUpManagerNum()<=0)||orgInfo.getUpManagerNum()==null){
                if((orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()==-1)||orgInfo.getUpManagerLocknum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
            if((orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()<=0)||orgInfo.getUpManagerLocknum()==null){
                if((orgInfo.getUpManagerNum()!=null &&orgInfo.getUpManagerNum()<=0)||orgInfo.getUpManagerNum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

        }
        if(orgInfo.getUpManagerLocknum()!=null && orgInfo.getUpManagerLocknum()<=0) {    //升级经理条件 第三项等于-1  判断其他两项
            if((orgInfo.getUpManagerNum()!=null && orgInfo.getUpManagerNum()<=0)||orgInfo.getUpManagerNum()==null){
                if((orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0)||orgInfo.getUpManagerCardnum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
            if((orgInfo.getUpManagerCardnum()!=null && orgInfo.getUpManagerCardnum()<=0)||orgInfo.getUpManagerCardnum()==null){
                if((orgInfo.getUpManagerNum()!=null && orgInfo.getUpManagerNum()<=0)||orgInfo.getUpManagerNum()==null){
                    result.setMsg("升级条件不能为空，请检查你的经理升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

        }
        if(orgInfo.getUpManagerMposnum()!=null && orgInfo.getUpManagerMposnum()<=0){
            result.setMsg("升级经理条件 采购mpos机数量请填写大于零的值");
            result.setStatus(false);
            return result;
        }

        if(orgInfo.getUpBankerNum()==null && orgInfo.getUpBankerCardnum()==null && orgInfo.getUpBankerLocknum()==null ){
            result.setMsg("升级银行家条件必须至少填写一项");
            result.setStatus(false);
            return result;
        }
        if(orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0){       //升级银行家条件第一项 判断其他两项
            if((orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0)||orgInfo.getUpBankerCardnum()==null){
                if((orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0)||orgInfo.getUpBankerLocknum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

            if((orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0)||orgInfo.getUpBankerLocknum()==null){
                if((orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0)||orgInfo.getUpBankerCardnum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
        }
        if(orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0){      //升级银行家条件第二项 判断其他两项
            if((orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0)||orgInfo.getUpBankerNum()==null){
                if((orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0)||orgInfo.getUpBankerLocknum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

            if((orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0)||orgInfo.getUpBankerLocknum()==null){
                if((orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0)||orgInfo.getUpBankerNum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

        }
        if(orgInfo.getUpBankerLocknum()!=null && orgInfo.getUpBankerLocknum()<=0){      //升级银行家条件第三项 判断其他两项
            if((orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0)||orgInfo.getUpBankerNum()==null){
                if((orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0)||orgInfo.getUpBankerCardnum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }

            if((orgInfo.getUpBankerCardnum()!=null && orgInfo.getUpBankerCardnum()<=0)||orgInfo.getUpBankerCardnum()==null){
                if((orgInfo.getUpBankerNum()!=null && orgInfo.getUpBankerNum()<=0)||orgInfo.getUpBankerNum()==null){
                    result.setMsg("升级条件不能为空，请检查你的银行家升级条件 ！");
                    result.setStatus(false);
                    return result;
                }
            }
        }
        if(orgInfo.getUpBankerMposnum()!=null && orgInfo.getUpBankerMposnum()<=0){
            result.setMsg("升级银行家条件 采购mpos机数量请填写大于零的值");
            result.setStatus(false);
            return result;
        }


        try {
            List<OrgBusinessConf> orgBusinessConfList = superBankService.selectOrgBusinessConfByOrgId(orgInfo.getOrgId());
            String[] businessIdArray=null;
            if (StringUtils.isNotEmpty(orgInfo.getCheckIds())) {
                businessIdArray = orgInfo.getCheckIds().split(",");
            }
            List<OrgBusinessConf> updateOrgBusinessConfList = getUpdateOrgBusinessConfList(businessIdArray,orgBusinessConfList,orgInfo.getOrgId());
            superBankService.saveOrUpdateOrgBusinessConfList(updateOrgBusinessConfList);    //更新业务

            int num = superBankService.updateOrgInfo(orgInfo);

            if("3".equals(orgInfo.getIndexStyle()) || "4".equals(orgInfo.getIndexStyle())){
                //更新三大模块新样式(先删除再插入)
                superBankService.deleteModuleByOrgId(orgInfo.getOrgId());
                //插入信用卡活动模块
                Long orgId = orgInfo.getOrgId();
                //插入优秀导师模块
                if(orgInfo.getTutorModelList()!= null&&orgInfo.getTutorModelList().size()>0){
                    superBankService.insertModuleBatch(orgInfo.getTutorModelList(), orgId);
                }
                //插入银行家大学-宣传展示模块
                if(orgInfo.getBankModelList()!= null&&orgInfo.getBankModelList().size()>0){
                    superBankService.insertModuleBatch(orgInfo.getBankModelList(), orgId);
                }

            }

            if(num == 1){
                //根据组织id刷新组织(当修改组织信息时调用)
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                String back = ClientInterface.superBankPushByOrgIdAndUrl(orgInfo.getOrgId().toString(),sysDict.getSysValue()+Constants.REFRESH_ORG_INFO,null,null);
                if(!StringUtils.isBlank(back)){
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if(jsonObject.containsValue("success")){
                        result.setStatus(true);
                        result.setMsg("操作成功");
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setStatus(false);
                result.setMsg("操作失敗");
            }
        } catch (Exception e){
            log.error("修改超级银行家组织管理异常", e);
            result.setStatus(false);
            result.setMsg("异常");
        }
        return result;
    }

    public List<OrgBusinessConf> getUpdateOrgBusinessConfList(String[] businessIdArray, List<OrgBusinessConf> orgBusinessConfList,Long orgId){
        List<OrgBusinessConf> updateOrgBusinessConfList = new ArrayList<>();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();

        List<String> checkList = new ArrayList<>();     //前端选择业务的集
        if (businessIdArray != null && businessIdArray.length > 0) {
            for (int i = 0; i < businessIdArray.length; i++) {
                String businessId = businessIdArray[i];
                if(StringUtils.isNotEmpty(businessId)) {
                    checkList.add(businessId);
                }
            }
        }
        List<String> saveCheckList = new ArrayList<>();     //已保存的业务的集
        if (businessIdArray != null && businessIdArray.length > 0) {
            for (int i = 0; i < orgBusinessConfList.size(); i++) {
                OrgBusinessConf orgBusinessConf = orgBusinessConfList.get(i);
                saveCheckList.add(String.valueOf(orgBusinessConf.getBusinessId()));
            }
        }

        List<String> checkList2 = new ArrayList<>();
        checkList2.addAll(checkList);

        List<String> saveCheckList2 = new ArrayList<>();
        saveCheckList2.addAll(saveCheckList);


        checkList2.retainAll(saveCheckList2);   //取交集

        checkList.removeAll(checkList2);        //取差集
        saveCheckList.removeAll(checkList2);    //取差集

        if(checkList!=null && checkList.size()>0){          //前端选择业务的集的差集
            for (int i = 0; i < checkList.size(); i++) {
                String businessId = checkList.get(i);
                OrgBusinessConf obc = new OrgBusinessConf();
                obc.setOrgId(orgId);
                obc.setBusinessId(Long.valueOf(businessId));
                obc.setIsEnable("1");
                obc.setCreateBy(loginInfo.getUsername());
                obc.setCreateDate(new Date());
                obc.setUpdateBy(loginInfo.getUsername());
                obc.setUpdateDate(new Date());
                obc.setOpear(OrgBusinessConf.Opear.INSERT);
                updateOrgBusinessConfList.add(obc);
            }
        }

        if (orgBusinessConfList != null && orgBusinessConfList.size() > 0) {

            for (int i = 0; i < orgBusinessConfList.size(); i++) {
                OrgBusinessConf orgBusinessConf = orgBusinessConfList.get(i);
                orgBusinessConf.setId(orgBusinessConf.getId());
                orgBusinessConf.setOrgId(orgBusinessConf.getOrgId());
                orgBusinessConf.setBusinessId(orgBusinessConf.getBusinessId());
                orgBusinessConf.setUpdateBy(loginInfo.getUsername());
                orgBusinessConf.setUpdateDate(new Date());
                String businessId =String.valueOf(orgBusinessConf.getBusinessId());
                if(saveCheckList.contains(businessId)){    //如果已保存的业务的差集包含Id
                    orgBusinessConf.setIsEnable("0");
                    orgBusinessConf.setOpear(OrgBusinessConf.Opear.UPDATE);
                }
                if(checkList2.contains(businessId)){
                    if ("0".equals(orgBusinessConf.getIsEnable())) {
                        orgBusinessConf.setIsEnable("1");
                        orgBusinessConf.setOpear(OrgBusinessConf.Opear.UPDATE);
                    } else {
                        orgBusinessConf.setOpear(OrgBusinessConf.Opear.NONE);
                    }
                }
                updateOrgBusinessConfList.add(orgBusinessConf);


            }

        }


        return updateOrgBusinessConfList;
    }


    /**
     * 超级银行家组织详情
     * @param orgId
     * @return
     */
    @RequestMapping("/orgInfoDetail")
    @ResponseBody
    public Result orgInfoDetail(Long orgId){
        Result result = new Result();
        result.setMsg("详情查询失败");
        try {
            result = superBankService.orgInfoDetail(orgId);
        } catch (Exception e){
            log.error("查看超级银行家组织详情异常", e);
        }
        return result;
    }

    /**
     * 校验orgId
     * 1.校验orgId在超级银行家组织是否已存在，已存在则返回false
     * 2.从nposp库获取V2组织ID、超级还组织ID
     * @param orgId
     * @return
     */
    @RequestMapping("/checkOrgId")
    @ResponseBody
    public Result checkOrgId(@RequestParam("orgId") String orgId){
        Result result = new Result();
        result.setMsg("校验orgId失败");
        try {
            if(StringUtils.isBlank(orgId)){
                result.setMsg("orgId不能为空");
                return result;
            }
            result = superBankService.checkOrgId(orgId);
        } catch (Exception e){
            log.error("校验orgId异常", e);
        }
        return result;
    }

    /**
     * 获取所有的银行家组织
     * @return
     */
    @RequestMapping("/getOrgInfoList")
    @ResponseBody
    public Result getOrgInfoList(){
        Result result = new Result();
        try {
            List<OrgInfo> list = superBankService.getOrgInfoList();
            if(list != null && list.size() > 0){
                for(OrgInfo org: list){
                    org.setOrgName(org.getOrgId() + " " + org.getOrgName());
                }
            }
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e){
            log.error("获取所有的银行家组织异常", e);
        }
        return result;
    }
    /**
     * 查询所有组织升级业务
     * @return
     */
    @RequestMapping("/getBusinessConfList")
    @ResponseBody
    public Result getBusinessConfList(String orgId){
        Result result = new Result();
        try {
            List<BusinessConf> list = superBankService.selectBusinessConfList();
            if(StringUtils.isNotEmpty(orgId)) {
                List<OrgBusinessConf> orgBusinessConfList = superBankService.selectOrgBusinessConfByOrgId(Long.valueOf(orgId));

                if (list != null && orgBusinessConfList != null) {
                    for (int i = 0; i < list.size(); i++) {
                        BusinessConf businessConf = list.get(i);
                        for (int j = 0; j < orgBusinessConfList.size(); j++) {
                            OrgBusinessConf orgBusinessConf = orgBusinessConfList.get(j);
                            if (orgBusinessConf.getBusinessId().equals(businessConf.getId())) {
                                businessConf.setIsCheck("1".equals(orgBusinessConf.getIsEnable()) ? 1 : 0);
                                continue;
                            }
                        }
                        list.set(i, businessConf);
                    }
                }
            }
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e){
            log.error("获取所有组织升级业务异常", e);
        }
        return result;
    }

    /**
     * 开户
     * @param agentNo
     * @return
     */
    @RequestMapping("/openAccount")
    @ResponseBody
    public Result openAccount(@RequestBody String agentNo){
        Result result = new Result();
        try {
            result = superBankService.openAccount(agentNo);
        } catch (Exception e){
            log.error("超级银行家组织开户", e);
        }
        return result;
    }

    /**
     * 获取所有的贷款机构
     * @return
     */
    @RequestMapping("/getLoanList")
    @ResponseBody
    public Result getLoanList(){
        Result result = new Result();
        try {
            List<LoanSource> list = superBankService.getLoanList();
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e){
            log.error("获取所有的贷款机构异常", e);
        }
        return result;
    }

    /**
     * 征信订单分页条件查询
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectOrderInquiryPage")
    @ResponseBody
    public Result selectOrderInquiryPage(@RequestBody ZxProductOrder baseInfo,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            if("-1".equals(baseInfo.getOrgId().toString())){
                baseInfo.setOrgId(null);
            }
            Page<ZxProductOrder> page = new Page<>(pageNo, pageSize);
            zxProductOrderService.selectByPage(baseInfo, page);
            OrderMainSum orderMainSum = zxProductOrderService.selectOrderSum(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("orderMainSum", orderMainSum);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }

    /**
     * 征信订单分页条件查询
     * @return
     */
    @RequestMapping("/selectProductAll")
    @ResponseBody
    public Result selectProductAll() {
        Result result = new Result();
        try {
            List<ZxApplyProductsEntity> zxApplyProductsEntitys = zxProductOrderService.selectProductAll();
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(zxApplyProductsEntitys);
        } catch (Exception e){
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }


    @RequestMapping("/orderInquiryDetail")
    @ResponseBody
    public Result orderInquiryDetail(String orderNo){
        Result result = new Result();
        try {
            if(StringUtil.isBlank(orderNo)){
                result.setMsg("订单号不能为空");
                return result;
            }
            ZxProductOrder zxProductOrder = zxProductOrderService.selectByOrderNo(orderNo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(zxProductOrder);
        } catch (Exception e){
            log.error("模糊查询所有的用户异常", e);
        }
        return result;
    }
    @RequestMapping("/updateCreditTotal")
    @ResponseBody
    public Result updateCreditTotal(@RequestBody ZhengBonusConfEntity record){
        Result result = new Result();
        log.info("\n-----------传入参数----------\n"+JSON.toJSONString(record)+"\n-----------------\n");
        try {
            BigDecimal pirceAll=record.getOemBonus().add(record.getOemTotalBonus()).add(record.getPrice());
            if(pirceAll.compareTo(record.getProductPrice())>0){
                result.setMsg("征信系统给银行家的成本+公司截留+品牌截留应小于等于报告售价");
                result.setStatus(false);
                return result;
            }
            result= zhengBonusConfService.updateBonusConfById(record);
        } catch (Exception e) {
            log.error("征信奖金配置修改异常", e);
            result.setStatus(false);
            result.setMsg("修改异常");
        }
        return result;
    }
    /**
     * 订单分页条件查询
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/orderManager")
    @ResponseBody
    public Result orderManager(@RequestBody OrderMain baseInfo,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            if(baseInfo == null){
                result.setMsg("参数不能为空");
                return result;
            }
            String createTimeStart = baseInfo.getCreateDateStart();
            String createTimeEnd = baseInfo.getCreateDateEnd();
//            订单类型:1代理授权 2信用卡申请 3收款 4还款申请 5贷款注册 6贷款批贷 7分期还款 8彩票代购 9排行榜 10征信 11红包领地 14违章代缴  15保险 16超级兑 17 完美还款
            String orderType = baseInfo.getOrderType();
            System.out.println((orderType.indexOf("5")!=-1 ||orderType.indexOf("6")!=-1
                    ||orderType.indexOf("3")!=-1
                    ||orderType.indexOf("7")!=-1 ||orderType.indexOf("17")!=-1
                    ||orderType.indexOf("1")!=-1
            ));
            if(!(orderType.indexOf("5")!=-1 ||orderType.indexOf("6")!=-1         //贷款
                    ||orderType.indexOf("3")!=-1                                //收款
                    ||orderType.indexOf("7")!=-1 ||orderType.indexOf("17")!=-1  //还款
                    ||orderType.indexOf("1")!=-1                                //代理授权
            )) {

                if (checkOrderDate(createTimeStart, createTimeEnd, result)) {
                    return result;
                }
            }
            Page<OrderMain> page = new Page<>(pageNo, pageSize);
            superBankService.selectOrderPage(baseInfo, page);
            OrderMainSum orderMainSum = superBankService.selectOrderSum(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("orderMainSum", orderMainSum);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }

    /**
     * 分润明细订单查询
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/profitDetailOrder")
    @ResponseBody
    public Result profitDetailManager(@RequestBody UserProfit baseInfo,
                                      @RequestParam(defaultValue = "1") int pageNo,
                                      @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            if(baseInfo == null){
                result.setMsg("参数不能为空");
                return result;
            }
            String createTimeStart = baseInfo.getCreateDateStart();
            String createTimeEnd = baseInfo.getCreateDateEnd();
            if (checkOrderDate(createTimeStart,createTimeEnd, result)) return result;

            Page<UserProfit> page = new Page<>(pageNo, pageSize);
            superBankService.selectProfitDetailPage(baseInfo, page);

            OrderMainSum orderMainSum = superBankService.selectProfitDetailSum(baseInfo);

            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("orderMainSum", orderMainSum);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询分润明细订单异常", e);
        }
        return result;
    }

    /**
     * 用户提现记录
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/obtainRecord")
    @ResponseBody
    public Result obtainRecord(@RequestBody UserObtainRecord baseInfo,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            if(baseInfo == null){
                result.setMsg("参数不能为空");
                return result;
            }
            String createTimeStart = baseInfo.getCreateDateStart();
            String createTimeEnd = baseInfo.getCreateDateEnd();
            if (checkOrderDate(createTimeStart,createTimeEnd, result)) return result;
            Page<UserObtainRecord> page = new Page<>(pageNo, pageSize);
            superBankService.selectObtainRecordPage(baseInfo, page);
            OrderMainSum orderMainSum = superBankService.selectObtainRecordSum(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("orderMainSum", orderMainSum);
            result.setStatus(true);
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询用户提现记录异常", e);
        }
        return result;
    }

    /**
     * 查询用户奖金发放记录
     * @param data
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/getRankingPushRecord")
    @ResponseBody
    public Result getRankingPushRecord(@RequestParam("baseInfo") String data,
                                       @RequestParam(defaultValue = "1") int pageNo,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        Map<String, Object> params = JSONObject.parseObject(data, Map.class);

        try {
            if(params == null || params.size()<=0){
                result.setMsg("参数不能为空");
                return result;
            }

            Page<RankingPushRecordInfo> page = new Page<>(pageNo, pageSize);
            superBankService.selectRankingPushRecordPage(params, page);

            List<RankingPushRecordInfo> rankingPushRecordInfoList = page.getResult();
            if (!CollectionUtils.isEmpty(rankingPushRecordInfoList)){

                for (int i = 0; i < rankingPushRecordInfoList.size(); i++) {
                    RankingPushRecordInfo rankingPushRecordInfo = rankingPushRecordInfoList.get(i);
                    if(StringUtils.isNotEmpty(rankingPushRecordInfo.getNickName())){
                        String deNickName = null;
                        try {
                            deNickName = URLDecoder.decode(rankingPushRecordInfo.getNickName(), "utf-8");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        rankingPushRecordInfo.setDeNickName(deNickName);
                    }
                    rankingPushRecordInfoList.set(i,rankingPushRecordInfo);
                }

            }
            String totalMoneyCount = superBankService.selectRankingPushRecordTotalMoneySum(params);
            String pushTotalMoneyCount = superBankService.selectRankingPushRecordPushTotalMoneySum(params);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("rankingPushRecordInfoList", rankingPushRecordInfoList);
            map.put("totalMoneyCount", totalMoneyCount);
            map.put("pushTotalMoneyCount", pushTotalMoneyCount);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询用户提现记录异常", e);
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
            result.setMsg("时间不能为空");
            return true;
        }
        Date createTimeStart = DateUtils.parseDateTime(createTimeStartStr);
        Date createTimeEnd = DateUtils.parseDateTime(createTimeEndStr);
        if(createTimeEnd.getTime() < createTimeStart.getTime()){
            result.setMsg("结束时间时间不能小于起始时间");
            return true;
        }
        if(createTimeEnd.getTime() - createTimeStart.getTime() > 3*30*24*60*60*1000L){
            result.setMsg("时间间隔不能超过三个月");
            return true;
        }
        return false;
    }


    @RequestMapping("/addCreditTotalBonus")
    @ResponseBody
    public Result addCreditTotalBonus(@RequestBody ZhengBonusConfEntity record ) {
        Result result=new Result();
        if(record.getProductId()==null){
            result.setMsg("请选择报告名称");
            return result;
        }
        BigDecimal pirceAll=record.getOemBonus().add(record.getOemTotalBonus()).add(record.getPrice());
        if(pirceAll.compareTo(record.getProductPrice())>0){
            result.setMsg("征信系统给银行家的成本+公司截留+品牌截留应小于等于报告售价");
            result.setStatus(false);
            return result;
        }
        int i  = zhengBonusConfService.saveZhengBonusConf(record);
        if(1>0) {
            result.setMsg("操作成功");
            result.setStatus(true);
        }
        return result;

    }



    @RequestMapping("/getCreditTotalList")
    @ResponseBody
    public Result getCreditTotalList(@RequestBody ZhengBonusConfEntity baseInfo,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            Page<ZhengBonusConfEntity> page = new Page<>(pageNo, pageSize);
            zhengBonusConfService.selectByOrgId(baseInfo, page);
            result.setStatus(true);
            result.setData(page);
        } catch (Exception e){
            log.error("分页条件查询记录异常", e);
        }
        return result;
    }
    private boolean checkOrderDate(String createTimeStartStr,String createTimeEndStr, Result result, int day) {
        if(StringUtils.isBlank(createTimeStartStr)
                || StringUtils.isBlank(createTimeEndStr)) {
            result.setMsg("时间不能为空");
            return true;
        }
        Date createTimeStart = DateUtils.parseDateTime(createTimeStartStr);
        Date createTimeEnd = DateUtils.parseDateTime(createTimeEndStr);
        if(createTimeEnd.getTime() < createTimeStart.getTime()){
            result.setMsg("结束时间时间不能小于起始时间");
            return true;
        }
        if(createTimeEnd.getTime() - createTimeStart.getTime() > day*30*24*60*60*1000L){
            result.setMsg("时间间隔不能超过三个月");
            return true;
        }
        return false;
    }

    /**
     * 模糊查询所有的用户
     * @param userCode
     * @return
     */
    @RequestMapping("/selectUserInfoList")
    @ResponseBody
    public Result selectUserInfoList(String userCode){
        Result result = new Result();
        try {
            List<SuperBankUserInfo> userInfoList = superBankService.selectUserInfoList(userCode);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(userInfoList);
        } catch (Exception e){
            log.error("模糊查询所有的用户异常", e);
        }
        return result;
    }

    /**
     * 导出超级银行家代理授权订单
     * @param baseInfo
     */
    @RequestMapping("/exportAgentOrder")
    @SystemLog(operCode = "superBank.exportAgentOrder", description = "导出超级银行家代理授权订单")
    public void exportAgentOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportAgentOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家代理授权订单异常", e);
        }
    }

    /**
     * 导出超级银行家开通办理信用卡订单
     * @param baseInfo
     */
    @RequestMapping("/exportOpenCredit")
    @SystemLog(operCode = "superBank.exportOpenCredit", description = "导出超级银行家开通办理信用卡订单")
    public void exportOpenCredit(String baseInfo, HttpServletResponse response){
        try {
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportOpenCredit(response, order);
        } catch (Exception e){
            log.error("导出超级银行家开通办理信用卡订单", e);
        }
    }

    /**
     * 导出超级银行家办理信用卡订单
     * @param baseInfo
     */
    @RequestMapping("/exportCreditOrder")
    @SystemLog(operCode = "superBank.exportAgentOrder", description = "导出超级银行家办理信用卡订单")
    public void exportCreditOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportCreditOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家办理信用卡订单异常", e);
        }
    }

    /**
     * 导出超级银行家贷款订单
     * @param baseInfo
     */
    @RequestMapping("/exportLoanOrder")
    @SystemLog(operCode = "superBank.exportLoanOrder", description = "导出超级银行家贷款订单")
    public void exportLoanOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportLoanOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家贷款订单异常", e);
        }
    }

    /**
     * 导出超级银行家征信订单
     * @param baseInfo
     */
    @RequestMapping("/exportInquiryOrder")
    @SystemLog(operCode = "superBank.exportInquiryOrder", description = "导出超级银行家贷款订单")
    public void exportInquiryOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            ZxProductOrder order = JSONObject.parseObject(baseInfo, ZxProductOrder.class);
            superBankService.exportInquiryOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家贷款订单异常", e);
        }
    }

    /**
     * 导出超级银行家收款订单
     * @param baseInfo
     */
    @RequestMapping("/exportReceiveOrder")
    @SystemLog(operCode = "superBank.exportLoanOrder", description = "导出超级银行家收款订单")
    public void exportReceiveOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportReceiveOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家收款订单异常", e);
        }
    }

    /**
     * 导出超级银行家还款订单
     * @param baseInfo
     */
    @RequestMapping("/exportRepayOrder")
    @SystemLog(operCode = "superBank.exportRepayOrder", description = "导出超级银行家还款订单")
    public void exportRepayOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportRepayOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家还款订单异常", e);
        }
    }

    /**
     * 导出超级银行家订单分润明细
     * @param baseInfo
     */
    @RequestMapping("/exportProfitDetail")
    @SystemLog(operCode = "superBank.exportProfitDetail", description = "导出超级银行家订单分润明细")
    public void exportProfitDetail(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "UTF-8");
            }
            UserProfit order = JSONObject.parseObject(baseInfo, UserProfit.class);
            superBankService.exportProfitDetail(response, order);
        } catch (Exception e){
            log.error("导出超级银行家还款订单异常", e);
        }
    }

    /**
     * 导出超级银行家用户提现记录
     * @param baseInfo
     */
    @RequestMapping("/exportObtainRecord")
    @SystemLog(operCode = "superBank.exportObtainRecord", description = "导出超级银行家用户提现记录")
    public void exportObtainRecord(String baseInfo, HttpServletResponse response){
        try {
            UserObtainRecord order = JSONObject.parseObject(baseInfo, UserObtainRecord.class);
            superBankService.exportObtainRecord(response, order);
        } catch (Exception e){
            log.error("导出超级银行家用户提现记录异常", e);
        }
    }
    /**
     * 导出超级银行家 用户奖金发放记录
     * @param baseInfo
     */
    @RequestMapping("/exportRankingPushRecord")
    @SystemLog(operCode = "superBank.exportRankingPushRecord", description = "导出超级银行家 用户奖金发放记录")
    public void exportRankingPushRecord(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response) throws Exception{
        if(StringUtils.isNotBlank(baseInfo)){
            baseInfo = URLDecoder.decode(baseInfo, "utf-8");
        }
        try {
            Map<String, Object> params = JSONObject.parseObject(baseInfo, Map.class);
            superBankService.exportRankingPushRecord(response, params);
        } catch (Exception e){
            log.error("导出超级银行家用户奖金发放记录异常", e);
        }
    }

    /**
     * 贷款订单模板下载
     * @author tans
     * @date 2017年12月04日
     * @param request
     * @param respon
     * se
     * @return
     */
    @RequestMapping("/loanOrderTemplate")
    public void loanOrderTemplate(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "12") String loanSourceId){
        String template = "";
        String fileName = "";
        String ruleCode;
        LoanSource loanSource = superBankService.selectLoanDetail(Long.valueOf(loanSourceId));
        ruleCode = loanSource.getRuleCode();
        if(StringUtils.isBlank(ruleCode)){
            try {
                String url = request.getRequestURI();
                if(url.contains("boss")){
                    String[] urlArr = url.split("/");
                    response.sendRedirect("/" + urlArr[1] + "/welcome.do#/superBank/superBank/loanOrder");
                } else {
                    response.sendRedirect("/welcome.do#/superBank/superBank/loanOrder");
                }
            } catch (IOException e) {
                log.error("重定向异常", e);
            }
        } else {
            if(ruleCode.equals(LoanSourceCode.YRD)){
                template = "loanOrderTemplate.xlsx";
                fileName = "宜人贷订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.KKD)){
                template = "kkdTemplate.xlsx";
                fileName = "卡卡贷订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.XSPH)){
                template = "xsphTemplate.xlsx";
                fileName = "小树普惠订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.JKD)){
                template = "jkdTemplate.xls";
                fileName = "嘉卡贷订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.CRK)){
                template = "crkTemplate.xlsx";
                fileName = "超人卡订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.WDPH)){
                template = "wdphTemplate.xlsx";
                fileName = "万达普惠订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.HKD)){
                template = "hkdTemplate.xlsx";
                fileName = "恒快贷订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.QLG)){
                template = "qlgTemplate.xlsx";
                fileName = "钱隆柜订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.RPD)){
                template = "rpdTemplate.xlsx";
                fileName = "人品贷订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.GFD)){
                template = "gfdTemplate.xlsx";
                fileName = "功夫贷订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.GT360)){
                template = "gt360Template.xlsx";
                fileName = "360借条订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.XEF)){
                template = "xefTemplate.xlsx";
                fileName = "信而富订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.WD)){
                template = "wdTemplate.xlsx";
                fileName = "微贷订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.ZAXY)){
                template = "zaxyTemplate.xlsx";
                fileName = "中安信业订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.KYH)){
                template = "kyhTemplate.xlsx";
                fileName = "快易花订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.DDQ)){
                template = "ddqTemplate.xlsx";
                fileName = "豆豆钱订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.ZA)){
                template = "zaTemplate.xlsx";
                fileName = "众安杏仁派订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.SNJR)){
                template = "snjrTemplate.xlsx";
                fileName = "苏宁金融订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.QMG)){
                template = "qmgTemplate.xlsx";
                fileName = "全民购订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.NWD)){
                template = "nwdTemplate.xlsx";
                fileName = "你我贷订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.QHFQ)){
                template = "qhfqTemplate.xlsx";
                fileName = "趣花分期订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.TQY)){
                template = "tqyTemplate.xlsx";
                fileName = "提钱游订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.JFXLK)){
                template = "jfxlkTemplate.xlsx";
                fileName = "玖富小蓝卡订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.LFQ)){
                template = "lfqTemplate.xlsx";
                fileName = "龙分期订单导入模板.xlsx";
            } else if(ruleCode.equals(LoanSourceCode.PAPH)){
                template = "paphTemplate.xlsx";
                fileName = "平安普惠i贷订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.DW)){
                template = "dwTemplate.xlsx";
                fileName = "大王贷款订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.MB)){
                template = "mbTemplate.xlsx";
                fileName = "秒贝订单导入模板.xlsx";
            }else if(ruleCode.equals(LoanSourceCode.JDBT)){
                template = "jdbtTemplate.xlsx";
                fileName = "京东白条订单导入模板.xlsx";
            }

//        switch (loanSourceId){
//            case "12":
//                template = "loanOrderTemplate.xlsx";
//                fileName = "宜人贷订单导入模板.xlsx";
//                break;
//            case "13":
//                template = "kkdTemplate.xlsx";
//                fileName = "卡卡贷订单导入模板.xlsx";
//                break;
//            case "14":
//                template = "xsphTemplate.xlsx";
//                fileName = "小树普惠订单导入模板.xlsx";
//                break;
//            case "15":
//                template = "jkdTemplate.xls";
//                fileName = "嘉卡贷订单导入模板.xlsx";
//                break;
//            case "16":
//                template = "crkTemplate.xlsx";
//                fileName = "超人卡订单导入模板.xlsx";
//                break;
//            case "17":
//                template = "wdphTemplate.xlsx";
//                fileName = "万达普惠订单导入模板.xlsx";
//                break;
//            case "18":
//                template = "hkdTemplate.xlsx";
//                fileName = "恒快贷订单导入模板.xlsx";
//                break;
//        }
            String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+template;
            ResponseUtil.download(response, filePath,fileName);
        }

    }

    /**
     * 信用卡申请记录导入模板下载
     * @author tans
     * @date 2018年1月22日
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/creditApplyTemplate")
    public void creditApplyTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"creditApplyTemplate.xlsx";
        ResponseUtil.download(response, filePath,"超级银行家信用卡申请记录导入模板.xlsx");
    }

    /**
     * 贷款订单导入
     * @author tans
     * @date 2017年12月04日
     * @return
     */
    @RequestMapping(value="/importLoanOrder")
    @ResponseBody
    @SystemLog(description = "贷款订单导入",operCode="superBank.importLoanOrder")
    public Result importLoanOrder(@RequestParam("file") MultipartFile file,
                                  @RequestParam String loanSourceId){
        Result result = new Result();
        try {
            if(StringUtils.isBlank(loanSourceId)){
                result.setMsg("贷款机构不能为空");
                return result;
            }
            result = superBankService.importLoanOrder(file, loanSourceId);
        } catch (Exception e) {
            log.error("导入失败",e);
        }
        return result;
    }

    @RequestMapping(value="/importCreditRecord")
    @ResponseBody
//    @SystemLog(description = "信用卡申请记录导入",operCode="superBank.importCreditRecord")
    public Result importCreditRecord(@RequestParam("file") MultipartFile file
            , @RequestParam("bankSourceId") String bankSourceId){
        Result result = new Result();
        try {
            result = superBankService.importCreditRecord(file, bankSourceId);
        } catch (Exception e) {
            log.error("导入失败",e);
        }
        return result;
    }

    /**
     * 超级银行家订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/orderDetail")
    @ResponseBody
    public Result orderDetail(String orderNo){
        Result result = new Result();
        try {
            if(StringUtil.isBlank(orderNo)){
                result.setMsg("订单号不能为空");
                return result;
            }
            OrderMain orderMain = superBankService.selectOrderDetail(orderNo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(orderMain);
        } catch (Exception e){
            log.error("模糊查询所有的用户异常", e);
        }
        return result;
    }

    /**
     * 超级银行家订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/repayOrderDetail")
    @ResponseBody
    public Result repayOrderManager(String orderNo){
        Result result = new Result();
        try {
            if(StringUtil.isBlank(orderNo)){
                result.setMsg("订单号不能为空");
                return result;
            }
            OrderMain orderMain = superBankService.selectRepayOrderDetail(orderNo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(orderMain);
        } catch (Exception e){
            log.error("模糊查询所有的用户异常", e);
        }
        return result;
    }

    /**
     * 测试新的数据源回滚机制
     * @param orgId
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    public Result insert(Long orgId){
        Result result = new Result();
        try {
            superBankService.insert111(orgId);
            result.setStatus(true);
        } catch (Exception e){
            log.error("新增银行家异常", e);
        }
        return result;
    }

    /**
     * 信用卡奖励查询
     * @param creditCardConf
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/getCreditCardConf")
    @ResponseBody
    public Result getCreditCardConfDatas(@RequestBody CreditCardBonus creditCardConf,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            log.info("\n-------------- " + JSON.toJSONString(creditCardConf) + "-----------------\n");

            Page<CreditCardBonus> page = new Page<>(pageNo, pageSize);

            result = superBankService.getCreditBonusConf(creditCardConf, page);
            result.setStatus(true);
            result.setMsg("查询成功");
        } catch (Exception e) {
            log.error("信用卡奖金配置查询异常", e);
            result.setStatus(false);
            result.setMsg("查询异常");
        }
        return result;
    }
    /**
     * 新增信用卡奖励配置
     * @param creditCardConf
     * @return
     */
    @RequestMapping("/addCredtiCardConf")
    @ResponseBody
    public Result addCredtiCardConf(@RequestBody CreditCardBonus creditCardConf){
        Result result = new Result();
        try {

            log.info("\n-------"+JSON.toJSONString(creditCardConf)+"-------\n");

//    		 boolean flag = checkData(creditCardConf,result);
//		 	 if(!flag){
//		 		 return result;
//		 	 }
            if(creditCardConf == null || creditCardConf.getSourceId() == null){
                result.setStatus(false);
                result.setMsg("参数不能为空");
                return result;
            }
            //int isExist = superBankService.isExistBankConf(creditCardConf.getSourceId());
            //旧数据只有银行所以只用sourceId判断，新的改造有组织和银行，所以联合判断是否已经存在(orgId和sourceid的组合)
            int isExist = superBankService.isExistBankConfWithSiAndOrgId(creditCardConf.getSourceId(),creditCardConf.getOrgId());//旧数据只有银行所以只用sourceId判断，新的改造有组织和银行，所以联合判断是否已经存在(orgId和sourceid的组合)

            if(isExist > 0){
                result.setStatus(false);
                result.setMsg("已存在该银行的配置");
                return result;
            }


            if("true".equals(creditCardConf.getIsOnlyone())){
                creditCardConf.setIsOnlyone("1");
            }else{
                creditCardConf.setIsOnlyone("0");
            }

            int cout = superBankService.addBank(creditCardConf);
            if(cout==0){
                result.setStatus(false);
                result.setMsg("新增失败，请重试");
                return result;
            }
            SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            //新增信用卡奖金配置
            String back=ClientInterface.superBankPushByOrgIdAndUrl(creditCardConf.getOrgId().toString(),sysDict.getSysValue()+Constants.REFRESH_CREDIT_CONF,null,creditCardConf.getSourceId().toString());
            if(!StringUtils.isBlank(back)){
                JSONObject jsonObject = JSONObject.parseObject(back);
                if(jsonObject.containsValue("success")){
                    result.setStatus(true);
                    result.setMsg("修改成功");
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            }
        } catch (Exception e) {
            log.error("新增数据异常", e);
            result.setStatus(false);
            result.setMsg("新增失败,请检查数据");
        }
        return result;
    }
    /**
     * 修改信用卡奖励配置
     * @param creditCardConf
     * @return
     */
    @RequestMapping("/updateCreditCardConf")
    @ResponseBody
    public Result updateCreditCardConf(@RequestBody CreditCardBonus creditCardConf){
        Result result = new Result();
        log.info("\n-----------传入参数----------\n"+JSON.toJSONString(creditCardConf)+"\n-----------------\n");
        try {

//   		 	boolean flag = checkData(creditCardConf,result);
//   		 	if(!flag){
//   		 		return result;
//   		 	}

        	   Result checkResult = superBankService.checkCreditBonusData("update",creditCardConf);
               if(checkResult != null && !checkResult.isStatus()){
                   result.setMsg(checkResult.getMsg());
                   return result;
               }

            int upds = superBankService.updCreditCardConf(creditCardConf);
            if(upds==0){
                result.setStatus(false);
                result.setMsg("修改失败，请检查数据");
            }else if(upds > 0){
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                //刷新信用卡奖金配置(当配置更改时调用)
                String back=ClientInterface.superBankPushByOrgIdAndUrl(creditCardConf.getOrgId().toString(),sysDict.getSysValue()+Constants.REFRESH_CREDIT_CONF,null,creditCardConf.getSourceId().toString());
                if(!StringUtils.isBlank(back)){
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if(jsonObject.containsValue("success")){
                        result.setStatus(true);
                        result.setMsg("修改成功");
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }

        } catch (Exception e) {
            log.error("信用卡配置修改异常", e);
            result.setStatus(false);
            result.setMsg("修改异常");
        }
        return result;
    }


    public boolean checkData(CreditCardBonus ccb,Result result){
        String bankBonus = ccb.getBankBonus();//银行总奖金
        String orgCost = ccb.getOrgCost();//品牌成本
        String orgPushCost = ccb.getOrgPushCost();//品牌发放成本

        if(bankBonus==null || orgCost==null || orgPushCost==null){
            result.setStatus(false);
            result.setMsg("请输入完整信息");
            return false;
        }
        Double bbd = 0d;
        Double ocd = 0d;
        Double opcd = 0d;
        try{
            bbd = Double.valueOf(bankBonus);
            ocd = Double.valueOf(orgCost);
            opcd = Double.valueOf(orgPushCost);
        }catch(java.lang.NumberFormatException e){
            result.setStatus(false);
            result.setMsg("请输入数字");
            return false;
        }

        if((ocd+opcd) > bbd){
            result.setStatus(false);
            result.setMsg("品牌成本加发放成本不能大于总奖金");
            return false;
        }
        return true;
    }

    /**
     * 查询所有支持的银行
     * @return
     */
    @RequestMapping("/banksList")
    @ResponseBody
    public Result credtiCardBanks(){
        Result result = new Result();
        try{
            List<CreditcardSource> list = superBankService.banksList();
            result.setData(list);
            result.setStatus(true);
            result.setMsg("查询成功");
        }catch(Exception e){
            log.info("获取银行列表失败",e);
        }

        return result;
    }

    /***
     * 获取所有机构信息
     * @return
     */
    @RequestMapping("/loanCompanies")
    @ResponseBody
    public Result loanCompanys(){
        Result result = new Result();
        try{
            List<LoanSource> list = superBankService.loanCompanyList();

            result.setData(list);
            result.setStatus(true);
            result.setMsg("查询成功");
        }catch(Exception e){
            log.info("获取贷款机构列表失败",e);
        }

        return result;
    }

    /**
     * 根据条件查询彩票总奖金管理配置
     * @param conf
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/lotteryConfList")
    @ResponseBody
    public Result getLotteryConfByPager(@RequestBody LotteryBonusConf conf,
                                        @RequestParam(defaultValue = "1") int pageNo,
                                        @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        log.info("组织ID--------------:"+conf.getOrgId());
        try{
            Page<LotteryBonusConf> page = new Page<>(pageNo, pageSize);
            List<LotteryBonusConf> list = superBankService.getLotteryBonusConfList(conf,page);
            page.setResult(list);
            result.setData(page);
            result.setStatus(true);
            result.setMsg("查询成功");
        }catch(Exception e){
            log.info("获取彩票奖金配置列表失败",e);
        }

        return result;
    }

    /**新增彩票组织奖金配置**/
    @RequestMapping("/addOrgLottery")
    @ResponseBody
    public Result addOrgLottery(@RequestBody LotteryBonusConf lottery){
        Result result = new Result();
        log.info("\n------------"+JSON.toJSONString(lottery)+"-----------------\n");

        boolean flag = checkLotteryData(lottery,result);
        if(!flag){
            return result;
        }
        /**校验组织是否存在*/
        if(lottery == null || lottery.getOrgId() == null ){
            result.setStatus(false);
            result.setMsg("新增失败，请重试");
            return result;
        }
        int orgCount = superBankService.checkLotteryBonusconfExist(lottery.getOrgId());
        if(orgCount > 0){
            result.setStatus(false);
            result.setMsg("该组织已经存在彩票配置，不能重复配置");
            return result;
        }
        try{
            int cout = superBankService.saveLotteryBonusconf(lottery);
            if(cout==0){
                result.setStatus(false);
                result.setMsg("新增失败，请重试");
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("异常",e);
            result.setStatus(false);
            result.setMsg("新增异常");
        }
        result.setStatus(true);
        result.setMsg("新增成功");
        return result ;
    }

    /**
     * 修改彩票配置
     * @param
     * @return
     */
    @RequestMapping("/updateLotteryConf")
    @ResponseBody
    public Result updateLotteryBonusConf(@RequestBody LotteryBonusConf lotteryConf) {
        Result result = new Result();

        log.info("\n------------"+JSON.toJSONString(lotteryConf)+"-----------------\n");

        boolean flag = checkLotteryData(lotteryConf,result);
        if(!flag){
            return result;
        }

        try {
            int upds = superBankService.updLotteryBonusconf(lotteryConf);
            if(upds==0){
                result.setStatus(false);
                result.setMsg("修改失败，请稍后再试");
                return result;
            }
        }catch (Exception e) {
            log.error("贷款配置修改异常", e);
            result.setStatus(false);
            result.setMsg("修改异常");
        }
        result.setStatus(true);
        result.setMsg("修改成功");
        return result;
    }


    //校验彩票新增参数
    private boolean checkLotteryData(LotteryBonusConf lottery,Result result){

        if(org.springframework.util.StringUtils.isEmpty(lottery.getLotteryOrgTotalBonus())
                && org.springframework.util.StringUtils.isEmpty(lottery.getCompanyBonus())
                && org.springframework.util.StringUtils.isEmpty(lottery.getOrgBonus())){
            result.setStatus(false);
            result.setMsg("请随便输入一个配置数据");
            return false;
        }

        Double lotteryOrgTotalBonus = 0d;
        Double companyBonus = 0d;
        Double orgBonus = 0d;

        try {
            if(!org.springframework.util.StringUtils.isEmpty(lottery.getLotteryOrgTotalBonus())){
                lotteryOrgTotalBonus = Double.valueOf(lottery.getLotteryOrgTotalBonus());
            }
            if(!org.springframework.util.StringUtils.isEmpty(lottery.getCompanyBonus())){
                companyBonus = Double.valueOf(lottery.getCompanyBonus());
            }
            if(!org.springframework.util.StringUtils.isEmpty(lottery.getOrgBonus())){
                orgBonus = Double.valueOf(lottery.getOrgBonus());
            }
        } catch (java.lang.NumberFormatException e) {
            result.setStatus(false);
            result.setMsg("请输入数字");
            return false;
        }
        if(companyBonus < 0 || lotteryOrgTotalBonus < 0 || orgBonus < 0){
            result.setStatus(false);
            result.setMsg("请输入大于或者等于0的参数");
            return false;
        }
        if(companyBonus > lotteryOrgTotalBonus){
            result.setStatus(false);
            result.setMsg("公司截留≤彩票机构给的总奖金");
            return false;
        }

        if(orgBonus > lotteryOrgTotalBonus){
            result.setStatus(false);
            result.setMsg("品牌截留≤彩票机构给的总奖金");
            return false;
        }

        if((companyBonus + orgBonus) > lotteryOrgTotalBonus){
            result.setStatus(false);
            result.setMsg("公司截留+品牌截留≤彩票机构给的总奖金");
            return false;
        }

        return true;
    }

    /**
     * 根据条件查询彩票总奖金管理配置
     * @param
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/lotteryImportList")
    @ResponseBody
    public Result lotteryImportList(@RequestBody LotteryImportRecords record,
                                    @RequestParam(defaultValue = "1") int pageNo,
                                    @RequestParam(defaultValue = "10") int pageSize){
        log.info("\n------------"+JSON.toJSONString(record)+"-----------------\n");
        Result result = new Result();
        try{
            Page<LotteryImportRecords> page = new Page<>(pageNo, pageSize);
            List<LotteryImportRecords> list = superBankService.getLotteryImportRecordsList(record, page);
            page.setResult(list);
            result.setData(page);
            result.setStatus(true);
            result.setMsg("查询成功");
        }catch(Exception e){
            log.info("获取彩票导入记录列表失败",e);
        }

        return result;
    }

    /**
     * 彩票订单记录导入
     * @author dxy
     * @date 2018年05月14日
     * @return
     */
    @RequestMapping(value="/importLottery")
    @ResponseBody
    @SystemLog(description = "彩票订单记录导入",operCode="superBank.importLottery")
    public Result importLottery(@RequestParam("file") CommonsMultipartFile file,@RequestParam("lotteryType") String lotteryType){
        Result result = new Result();
        try {

            String nowStr = DateUtil.getMessageTextTime();
            String fileName = getFileNamePrefxiByFileName(file.getOriginalFilename()) + "_"+ nowStr + ".xlsx";
            ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_ATTCH_TUCKET,fileName,file.getInputStream());
            Date now = new Date();
            Date exip = new Date(now.getTime() + 3600l * 1000 * 24 * 365 * 50);//设置过期时间为50年
            String aliUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET,fileName,exip);
            UserLoginInfo loginInfo = CommonUtil.getLoginUser();
            //3.增加上传记录
            LotteryImportRecords info = new LotteryImportRecords();
            info.setStatus("1");//状态，0无需操作，1待匹配，2已匹配
            info.setBatchNo("CP_" + nowStr);
            info.setFileName(fileName);
            info.setFileUrl(aliUrl);
            info.setLotteryType(lotteryType);//彩票类型

            info.setUpdateBy(loginInfo.getUsername());
            Integer count = superBankService.saveLotteryImportRecord(info);
            if(count != null && count != 0){
                result.setStatus(true);
                result.setMsg("导入成功");
            }
        } catch (Exception e) {
            log.error("导入失败",e);
        }
        return result;
    }

    private String getFileNamePrefxiByFileName(String fileName){
        String fileNamePrefix = "";
        if(fileName.contains(".xlsx")){
            fileNamePrefix = fileName.substring(0,fileName.length()-".xlsx".length());
        }else if(fileName.contains(".xls")){
            fileNamePrefix = fileName.substring(0,fileName.length()-".xls".length());
        }
        return fileNamePrefix;
    }

    @RequestMapping("/deleteLotteryImport")
    @ResponseBody
    public Result deleteLotteryImport(@RequestBody LotteryImportRecords record){
        Result result = new Result();
        int count = 0;
        if(record == null || record.getId() == null){
            result.setStatus(false);
            result.setMsg("参数不能为空");
            return result;
        }
        try {
            count = superBankService.deleteLotteryImportRecord(record.getId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            count = 0;
        }
        if(count > 0) {
            result.setStatus(true);
            result.setMsg("删除成功");
        } else {
            result.setStatus(false);
            result.setMsg("删除失败");
        }
        return result;
    }


    /**新增彩票组织奖金配置**/
    @RequestMapping("/addLotteryImportRecord")
    @ResponseBody
    public Result addLotteryImportRecord(@RequestBody LotteryImportRecords record){
        Result result = new Result();

        try{
            int cout = superBankService.saveLotteryImportRecord(record);
            if(cout==0){
                result.setStatus(false);
                result.setMsg("新增失败，请重试");
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("异常",e);
            result.setStatus(false);
            result.setMsg("新增异常");
        }
        result.setStatus(true);
        result.setMsg("新增成功");
        return result ;
    }

    /**
     * 根据条件查询贷款奖励配置
     * @param conf
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/loanConfList")
    @ResponseBody
    public Result getLoanConfByPager(@RequestBody LoanBonusConf conf,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        log.info("贷款机构ID--------------:"+conf.getSourceId());
        try{
            Page<LoanBonusConf> page = new Page<>(pageNo, pageSize);
            List<LoanBonusConf> list = superBankService.getLoanBonusConfList(conf,page);
            page.setResult(list);
            result.setData(page);
            result.setStatus(true);
            result.setMsg("查询成功");
        }catch(Exception e){
            log.info("获取贷款机构列表失败",e);
        }

        return result;
    }

    /**
     * 修改贷奖励款配置
     * @param loanConf
     * @return
     */
    @RequestMapping("/updateLoanConf")
    @ResponseBody
    public Result updateLoanBonusConf(@RequestBody LoanBonusConf loanConf) {
        Result result = new Result();
        log.info("\n------------"+JSON.toJSONString(loanConf)+"-----------------\n");
        try {
            Result checkResult = superBankService.checkLoanData(loanConf);
            if(checkResult != null && !checkResult.isStatus()){
                result.setMsg(checkResult.getMsg());
                return result;
            }
            int upds = superBankService.updLoanBonusconf(loanConf);
            if(upds==0){
                result.setStatus(false);
                result.setMsg("修改失败，请稍后再试");
                return result;
            }
            //刷新贷款奖金配置(当贷款奖金配置更改时调用)
            loanConf= superBankService.selectLoanBonusConfByPrimaryKey(loanConf.getId());
            SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            String back = ClientInterface.superBankPushByOrgIdAndUrl(loanConf.getOrgId().toString(), sysDict.getSysValue()+Constants.REFRESH_LOAN_CONF ,null,loanConf.getSourceId().toString());
            if(!StringUtils.isBlank(back)){
                JSONObject jsonObject = JSONObject.parseObject(back);
                if(jsonObject.containsValue("success")){
                    result.setStatus(true);
                    result.setMsg("修改成功");
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            }
            /*result.setStatus(true);
            result.setMsg("修改成功");*/
        } catch (Exception e) {
            log.error("贷款配置修改异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**新增贷款机构**/
    @RequestMapping("/addLoanCompany")
    @ResponseBody
    public Result addLoanCompany(@RequestBody LoanBonusConf loan){
        Result result = new Result();
        log.info("\n------------"+JSON.toJSONString(loan)+"-----------------\n");
        try{
            Result checkResult = superBankService.checkLoanData(loan);
            if(checkResult != null && !checkResult.isStatus()){
                result.setMsg(checkResult.getMsg());
                return result;
            }
            int isExist = superBankService.isExistLoanCompany(loan.getSourceId(), loan.getOrgId());
            if(isExist > 0){
                result.setStatus(false);
                result.setMsg("已存在该贷款机构的配置");
                return result;
            }
            int cout = superBankService.saveLoanBonusconf(loan);
            if(cout==0){
                result.setStatus(false);
                result.setMsg("新增失败，请重试");
                return result;
            }
            SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            String back = ClientInterface.superBankPushByOrgIdAndUrl(loan.getOrgId().toString(), sysDict.getSysValue()+Constants.REFRESH_LOAN_CONF ,null,loan.getSourceId().toString());
            if(!StringUtils.isBlank(back)){
                JSONObject jsonObject = JSONObject.parseObject(back);
                if(jsonObject.containsValue("success")){
                    result.setStatus(true);
                    result.setMsg("修改成功");
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("异常",e);
            result.setStatus(false);
            result.setMsg("新增异常");
        }
        return result ;
    }

    /**分页查询分润配置*/
    @RequestMapping("/getOrgProfitConf")
    @ResponseBody
    public Result getOrgProfitConf(@RequestBody OrgProfitConf orgProfit, @RequestParam(defaultValue = "1") int pageNo,
                                   @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        Page<OrgProfitConf> page = new Page<OrgProfitConf>(pageNo,pageSize);
        try {

            log.info("\n组织ID--------------："+orgProfit.getOrgId()+"------\n");

            List<OrgProfitConf> list = superBankService.getOrgProfitConfList(orgProfit,page);
            page.setResult(list);
            result.setData(page);
            result.setMsg("查询成功");
            result.setStatus(true);
        } catch (Exception e) {
            log.info("查询异常",e);
            e.printStackTrace();
            result.setMsg("查询失败");
            result.setStatus(false);
        }
        return result;
    }

    /**新增组织分润配置*/
    @RequestMapping("/addOrgPofitConf")
    @ResponseBody
    public Result addOrgProfit(@RequestBody OrgProfitConfToTradeType data){
        Result result = new Result();
        try {

            int isExist = superBankService.isExistOrgConf(data.getOrgId()+"");
            if(isExist > 0){
                result.setStatus(false);
                result.setMsg("已存在该品牌的配置");
                return result;
            }

            log.info("\n-------传入参数-----" + JSON.toJSONString(data)+"------------\n");

//            String profitType1 = "1";//固定
            String profitType2 = "2";//比例

            OrgProfitConf conf1 = new OrgProfitConf();
//            OrgProfitConf conf2 = new OrgProfitConf();
//            OrgProfitConf conf3= new OrgProfitConf();
//            OrgProfitConf conf4 = new OrgProfitConf();
//            OrgProfitConf conf5 = new OrgProfitConf();
//            OrgProfitConf conf6 = new OrgProfitConf();
//            OrgProfitConf conf7 = new OrgProfitConf();
//            OrgProfitConf conf8 = new OrgProfitConf();
//            OrgProfitConf conf9 = new OrgProfitConf();
//            OrgProfitConf conf10 = new OrgProfitConf();
//            OrgProfitConf conf11 = new OrgProfitConf();
//            OrgProfitConf conf12 = new OrgProfitConf();
//            OrgProfitConf conf15 = new OrgProfitConf();
//            OrgProfitConf conf17 = new OrgProfitConf();

            conf1.setOrgId(data.getOrgId());
//            conf2.setOrgId(data.getOrgId());
//            conf3.setOrgId(data.getOrgId());
//            conf4.setOrgId(data.getOrgId());
//            conf5.setOrgId(data.getOrgId());
//            conf6.setOrgId(data.getOrgId());
//            conf7.setOrgId(data.getOrgId());
//            conf8.setOrgId(data.getOrgId());
//            conf9.setOrgId(data.getOrgId());
//            conf10.setOrgId(data.getOrgId());
//            conf11.setOrgId(data.getOrgId());
//            conf12.setOrgId(data.getOrgId());
//            conf15.setOrgId(data.getOrgId());
//            conf17.setOrgId(data.getOrgId());

            List<OrgProfitConf> confs = new ArrayList<OrgProfitConf>();

            //除了 1 和 4 固定金额，其余按比例

            //1代理授权 2信用卡申请 3收款(快捷) 4还款申请(激活还款) 5贷款注册  6贷款批贷   7还款(订单)  8 收款(支付宝) 9 收款(微信) 10彩票
            conf1.setProfitType(profitType2);
            conf1.setProductType(null);
            conf1.setSeltMember(data.getSeltMember1());
            conf1.setSeltManager(data.getSeltManager1());
            conf1.setSeltBanker(data.getSeltBanker1());

//            conf2.setProfitType(profitType2);
//            conf2.setProductType("2");
//            conf2.setSeltMember(data.getSeltMember2());
//            conf2.setSeltManager(data.getSeltManager2());
//            conf2.setSeltBanker(data.getSeltBanker2());
//
//            conf3.setProfitType(profitType2);
//            conf3.setProductType("3");
//            conf3.setSeltMember(data.getSeltMember3());
//            conf3.setSeltManager(data.getSeltManager3());
//            conf3.setSeltBanker(data.getSeltBanker3());
//
//            conf4.setProfitType(profitType1);
//            conf4.setProductType("4");
//            conf4.setSeltMember(data.getSeltMember4());
//            conf4.setSeltManager(data.getSeltManager4());
//            conf4.setSeltBanker(data.getSeltBanker4());
//
//            conf5.setProfitType(profitType2);
//            conf5.setProductType("5");
//            conf5.setSeltMember(data.getSeltMember5());
//            conf5.setSeltManager(data.getSeltManager5());
//            conf5.setSeltBanker(data.getSeltBanker5());
//
//            conf6.setProfitType(profitType2);
//            conf6.setProductType("6");
//            conf6.setSeltMember(data.getSeltMember6());
//            conf6.setSeltManager(data.getSeltManager6());
//            conf6.setSeltBanker(data.getSeltBanker6());
//
//            conf7.setProfitType(profitType2);
//            conf7.setProductType("7");
//            conf7.setSeltMember(data.getSeltMember7());
//            conf7.setSeltManager(data.getSeltManager7());
//            conf7.setSeltBanker(data.getSeltBanker7());
//
//            conf8.setProfitType(profitType2);
//            conf8.setProductType("8");
//            conf8.setSeltMember(data.getSeltMember8());
//            conf8.setSeltManager(data.getSeltManager8());
//            conf8.setSeltBanker(data.getSeltBanker8());
//
//            conf9.setProfitType(profitType2);
//            conf9.setProductType("9");
//            conf9.setSeltMember(data.getSeltMember9());
//            conf9.setSeltManager(data.getSeltManager9());
//            conf9.setSeltBanker(data.getSeltBanker9());
//
//            conf10.setProfitType(profitType2);
//            conf10.setProductType("10");
//            conf10.setSeltMember(data.getSeltMember10());
//            conf10.setSeltManager(data.getSeltManager10());
//            conf10.setSeltBanker(data.getSeltBanker10());
//
//            conf11.setProfitType(profitType2);
//            conf11.setProductType("11");
//            conf11.setSeltMember(data.getSeltMember11());
//            conf11.setSeltManager(data.getSeltManager11());
//            conf11.setSeltBanker(data.getSeltBanker11());
//
//            conf12.setProfitType(profitType2);
//            conf12.setProductType("14");
//            conf12.setSeltMember(data.getSeltMember12());
//            conf12.setSeltManager(data.getSeltManager12());
//            conf12.setSeltBanker(data.getSeltBanker12());
//
//            conf15.setProfitType(profitType2);
//            conf15.setProductType("15");
//            conf15.setSeltMember(data.getSeltMember15());
//            conf15.setSeltManager(data.getSeltManager15());
//            conf15.setSeltBanker(data.getSeltBanker15());
//
//            conf17.setProfitType(profitType2);
//            conf17.setProductType("17");
//            conf17.setSeltMember(data.getSeltMember17());
//            conf17.setSeltManager(data.getSeltManager17());
//            conf17.setSeltBanker(data.getSeltBanker17());

            confs.add(conf1);
//            confs.add(conf2);
//            confs.add(conf3);
//            confs.add(conf4);
//            confs.add(conf5);
//            confs.add(conf6);
//            confs.add(conf7);
//            confs.add(conf8);
//            confs.add(conf9);
//            confs.add(conf10);
//            confs.add(conf11);
//            confs.add(conf12);
//            confs.add(conf15);
//            confs.add(conf17);

            //新增组织分润配置
            superBankService.saveOrgProfitConf(confs);
            SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            String back = ClientInterface.superBankPushByOrgIdAndUrl(confs.get(0).getOrgId().toString(), sysDict.getSysValue()+Constants.REFRESH_ORG_CONF_ALL ,null,null);
            if(!StringUtils.isBlank(back)){
                JSONObject jsonObject = JSONObject.parseObject(back);
                if(jsonObject.containsValue("success")){
                    result.setStatus(true);
                    result.setMsg("修改成功");
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("新增异常",e);
            result.setStatus(false);
            result.setMsg("新增分润配置失败");
        }

        return result;
    }

    /**修改组织分润配置*/
    @RequestMapping("/updOrgProfit")
    @ResponseBody
    public Result updOrgProfit(@RequestBody OrgProfitConf orgProfit){
        Result result = new Result();
        try {
            int num = superBankService.updOrgProfitConf(orgProfit);
            if(num == 1){
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                //刷新组织分润配置(当组织分配配置修改时调用)
                orgProfit=superBankService.selectOrgProfitConfByPrimaryKey(orgProfit.getId());
                String back=ClientInterface.superBankPushByOrgIdAndUrl(orgProfit.getOrgId().toString(),sysDict.getSysValue()+Constants.REFRESH_ORG_CONF,orgProfit.getProductType(),null);
                if(!StringUtils.isBlank(back)){
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if(jsonObject.containsValue("success")){
                        result.setStatus(true);
                        result.setMsg("修改成功");
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setMsg("修改失敗");
                result.setStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("修改分润失败",e);
            result.setMsg("修改失败");
            result.setStatus(false);
        }
        return result;
    }

    /**查询公告列表*/
    @RequestMapping("/noticeList")
    @ResponseBody
    public Result findNotice(@RequestBody Notice notice,@RequestParam(defaultValue = "1") int pageNo,
                             @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();

        log.info("\n查询参数--------------------"+JSON.toJSONString(notice));
        try {
            Page<Notice> page = new Page<>(pageNo,pageSize);
            List<Notice> list = superBankService.findNotice(notice, page);
            page.setResult(list);
            result.setData(page);
            result.setMsg("查询成功");
            result.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("查询异常");
            result.setStatus(false);
        }

        return result ;
    }

    /**公告详情*/
    @RequestMapping("/noticeDetail/{id}")
    @ResponseBody
    public Result findDetail(@PathVariable("id") String id){
        Result result = new Result();

        log.info("\n查询详情的ID---------------"+id+"--------------------\n");

        try {
            Notice notice = superBankService.noticeDetail(id);
            result.setData(notice);
            result.setStatus(true);
            result.setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常\n",e);
            result.setMsg("查询异常");
            result.setStatus(false);
        }

        return result ;
    }

    /**新增公告*/
    @RequestMapping("/addNotice")
    @ResponseBody
    @SystemLog(operCode = "superBank.addNotice", description = "新增公告")
    public Result addNotice(@RequestBody Notice notice){
        Result result = new Result();
        log.info("\n传入参数-----------"+JSON.toJSONString(notice));

        try {
            superBankService.addNotice(notice);
            SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            //根据组织id刷新公告(新增公告刷新)
            String back=ClientInterface.superBankPushByOrgIdAndUrl(notice.getOrgId(),sysDict.getSysValue()+Constants.REFRESH_NOTICE_INFO,null,null);
            if(!StringUtils.isBlank(back)){
                JSONObject jsonObject = JSONObject.parseObject(back);
                if(jsonObject.containsValue("success")){
                    result.setStatus(true);
                    result.setMsg("修改成功");
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("新增异常");
            result.setStatus(false);
        }
        return result ;
    }

    /**修改公告*/
    @RequestMapping("/updNotice")
    @ResponseBody
    @SystemLog(operCode = "superBank.updNotice", description = "修改公告")
    public Result updNotice(@RequestBody Notice notice){
        Result result = new Result();
        try {
            long num = superBankService.updNotice(notice);
            if(num == 1){
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                //根据组织id刷新公告(当公告配置、修改、删除时都要调用)
                String back=ClientInterface.superBankPushByOrgIdAndUrl(notice.getOrgId(),sysDict.getSysValue()+Constants.REFRESH_NOTICE_INFO,null,null);
                if(!StringUtils.isBlank(back)){
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if(jsonObject.containsValue("success")){
                        result.setStatus(true);
                        result.setMsg("修改成功");
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setMsg("修改失敗");
                result.setStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("修改异常");
            result.setStatus(false);
        }
        return result ;
    }
    /**下发公告*/
    @RequestMapping("/sendNotice")
    @ResponseBody
    @SystemLog(operCode = "superBank.sendNotice", description = "下发公告")
    public Result sendNotice(@RequestBody Notice notice){
        Result result = new Result();
        try {
            if(notice != null && notice.getId() != null && notice.getStatus() != null){
                int num = superBankService.sendNotice(notice);
                notice=superBankService.noticeDetail(notice.getId().toString());
                if(num == 1){
                    SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                    //根据组织id刷新公告(当公告配置、修改、删除时都要调用)
                    String back=ClientInterface.superBankPushByOrgIdAndUrl(notice.getOrgId(),sysDict.getSysValue()+Constants.REFRESH_NOTICE_INFO,null,null);
                    if(!StringUtils.isBlank(back)){
                        JSONObject jsonObject = JSONObject.parseObject(back);
                        if(jsonObject.containsValue("success")){
                            result.setStatus(true);
                            result.setMsg("修改成功");
                        }else{
                            result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                        }
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                } else {
                    result.setMsg("操作失败");
                }
            } else {
                result.setMsg("参数不能为空");
            }

        } catch (Exception e) {
            log.error("下发公告异常", e);
            result.setMsg("操作失败");
            result.setStatus(false);
        }
        return result ;
    }

    /**删除公告*/
    @RequestMapping("/deleteNotice")
    @ResponseBody
    @SystemLog(operCode = "superBank.deleteNotice", description = "删除公告")
    public Result deleteNotice(@RequestBody Notice notice){
        Result result = new Result();
        try {
            if(notice != null && notice.getId() != null){
                notice=superBankService.noticeDetail(notice.getId().toString());
                int num = superBankService.deleteNotice(notice.getId());
                if(num == 1){
                    SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                    //根据组织id刷新公告(当公告配置、修改、删除时都要调用)
                    String back=ClientInterface.superBankPushByOrgIdAndUrl(notice.getOrgId(),sysDict.getSysValue()+Constants.REFRESH_NOTICE_INFO,null,null);
                    if(!StringUtils.isBlank(back)){
                        JSONObject jsonObject = JSONObject.parseObject(back);
                        if(jsonObject.containsValue("success")){
                            result.setStatus(true);
                            result.setMsg("修改成功");
                        }else{
                            result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                        }
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                } else {
                    result.setMsg("操作失败");
                }
            } else {
                result.setMsg("参数不能为空");
            }
        } catch (Exception e) {
            log.error("删除公告异常", e);
            result.setMsg("操作异常");
            result.setStatus(false);
        }
        return result ;
    }

    /**修改公告弹窗提示开关*/
    @RequestMapping("/updateNoticePop")
    @ResponseBody
    @SystemLog(operCode = "superBank.updateNoticePop", description = "修改公告弹窗提示开关")
    public Result updateNoticePop(@RequestBody Notice notice){
        Result result = new Result();
        try {
            if(notice != null && notice.getId() != null && notice.getPopSwitch() != null){
                int num = superBankService.updateNoticePop(notice.getId(), notice.getPopSwitch());
                if(num == 1){
                    notice = superBankService.noticeDetail(notice.getId().toString());
                    SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                    //根据组织id刷新公告(新增公告刷新)
                    String back=ClientInterface.superBankPushByOrgIdAndUrl(notice.getOrgId(),sysDict.getSysValue()+Constants.REFRESH_NOTICE_INFO,null,null);
                    if(!StringUtils.isBlank(back)){
                        JSONObject jsonObject = JSONObject.parseObject(back);
                        if(jsonObject.containsValue("success")){
                            result.setStatus(true);
                            result.setMsg("修改成功");
                        }else{
                            result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                        }
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                } else {
                    result.setMsg("操作失败");
                }
            } else {
                result.setMsg("参数不能为空");
            }
        } catch (Exception e) {
            log.error("修改公告弹窗提示开关异常", e);
            result.setMsg("操作异常");
            result.setStatus(false);
        }
        return result ;
    }

    /**banner管理*/
    @RequestMapping("/adManage")
    @ResponseBody
    public Result findAds(@RequestBody Ad ad,@RequestParam(defaultValue = "1") int pageNo,
                          @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            log.info("\n传入参数----------"+JSON.toJSONString(ad)+"\n");

            Page<Ad> page = new Page<Ad>(pageNo,pageSize);
            superBankService.findAds(ad,page);
            result.setData(page);
            result.setMsg("查询成功");
            result.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("查询异常");
            result.setStatus(false);
        }
        return result;
    }

    /**新增banner*/
    @RequestMapping("/addAd")
    @ResponseBody
    public Result addAd(@RequestBody Ad ad){
        Result result = new Result();
        try {
            log.info("\n传入参数----------"+JSON.toJSONString(ad)+"----------\n");
            if(ad==null){
                result.setMsg("新增异常,数据为空");
                result.setStatus(false);
                return result;
            }

            //改成临时获取图片URL，数据库只保存图片名
            //String realUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, ad.getImgUrl(), new Date(new Date().getTime()+365*24*60*60*1000));
            //ad.setImgUrl(realUrl);
            ad.setStatus("off");

            String orgId=null;
            if(-1L == ad.getOrgId()){//所有组织，用于设置默认配置
                orgId="all";
                List<OrgInfo> list = superBankService.getOrgInfoList();
                for (OrgInfo org : list) {
                    ad.setOrgId(org.getOrgId());
                    long count = superBankService.addAd(ad);
                }
            }else{
                orgId=ad.getOrgId().toString();
                long count = superBankService.addAd(ad);
                if(count==0){
                    result.setMsg("新增失败，请重试");
                    result.setStatus(false);
                    return result;
                }
            }

            SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            String back = ClientInterface.superBankPushByOrgIdAndUrl(orgId,sysDict.getSysValue()+Constants.REFRESH_BANNER_INFO,null,null);
            if(!StringUtils.isBlank(back)){
                JSONObject jsonObject = JSONObject.parseObject(back);
                if(jsonObject.containsValue("success")){
                    result.setMsg("更新成功");
                    result.setStatus(true);
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("新增异常");
            result.setStatus(false);
        }
        return result;
    }

    /**修改banner信息*/
    @RequestMapping("/updAd")
    @ResponseBody
    public Result updAd(@RequestBody Ad ad){
        Result result = new Result();
        try {
            log.info("\n传入参数----------"+JSON.toJSONString(ad)+"----------------\n");

    		/*-----改成存图片名，临时获取URL
    		if(ad.getImgUrl()!=null && !"".equals(ad.getImgUrl())){
    			String realUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, ad.getImgUrl(), new Date(new Date().getTime()+365*24*60*60*1000));
    			ad.setImgUrl(realUrl);
    		}
    		*/
            long count = superBankService.updAd(ad, "1");
            if(count == 1){
                //根据组织id刷新组织(当修改组织信息时调用)
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                String back = ClientInterface.superBankPushByOrgIdAndUrl(ad.getOrgId().toString(),sysDict.getSysValue()+Constants.REFRESH_BANNER_INFO,null,null);
                if(!StringUtils.isBlank(back)){
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if(jsonObject.containsValue("success")){
                        result.setMsg("更新成功");
                        result.setStatus(true);
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setStatus(false);
                result.setMsg("操作失敗");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("修改异常");
            result.setStatus(false);
        }
        return result;
    }

    /**删除banner*/
    @RequestMapping("/delAd")
    @ResponseBody
    public Result delAd(@RequestParam("id") String id){
        Result result = new Result();
        try {
            log.info("\n传入参数----------"+id+"\n");
            Ad ad = superBankService.selectAdByPrimaryKey(Long.valueOf(id));
            long count = superBankService.delAd(id);
            if(count == 1){
                //根据组织id刷新组织(当修改组织信息时调用)
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                String back = ClientInterface.superBankPushByOrgIdAndUrl(ad.getOrgId().toString(),sysDict.getSysValue()+Constants.REFRESH_BANNER_INFO,null,null);
                if(!StringUtils.isBlank(back)){
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if(jsonObject.containsValue("success")){
                        result.setMsg("更新成功");
                        result.setStatus(true);
                    }else{
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                }else{
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }else{
                result.setStatus(false);
                result.setMsg("操作失敗");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("删除异常");
            result.setStatus(false);
        }
        return result;
    }

    /**banner详情*/
    @RequestMapping("/adDetail")
    @ResponseBody
    public Result adDetail(@RequestParam("id") String id,@RequestParam("isUpd") String isUpd){
        Result result = new Result();
        try {
            log.info("\n传入参数----------"+JSON.toJSONString(id)+"----------------------\n");
            Ad ad = new Ad();
            if(id==null){
                result.setMsg("查询异常");
                result.setStatus(false);
                return result;
            }
            ad.setId(Long.parseLong(id));

            ad = superBankService.adDetail(ad);

            if("on".equals(ad.getStatus())){
                ad.setStatus("打开");
            }else{
                ad.setStatus("关闭");
            }
            result.setMsg("查询成功");
            result.setData(ad);
            result.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("查询异常");
            result.setStatus(false);
        }
        return result;
    }

    /**banner上线下线开关*/
    @RequestMapping("/adOnOff")
    @ResponseBody
    public Result on_offAd(@RequestBody Ad ad){
        Result result = new Result();
        try {
            log.info("\n传入参数----------"+JSON.toJSONString(ad)+"\n");
            long count = superBankService.updAd(ad, "2");
            if(count == 1){
                ad = superBankService.selectAdByPrimaryKey(Long.valueOf(ad.getId()));
                //根据组织id刷新组织(当修改组织信息时调用)
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                String back = ClientInterface.superBankPushByOrgIdAndUrl(ad.getOrgId().toString(),sysDict.getSysValue()+Constants.REFRESH_BANNER_INFO,null,null);
                if(!StringUtils.isBlank(back)){
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if(jsonObject.containsValue("success")){
                        result.setMsg("更新成功");
                        result.setStatus(true);
                    }else{
                        result.setStatus(false);
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                }else{
                    result.setStatus(false);
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("修改异常");
            result.setStatus(false);
        }
        return result;
    }

    /**
     * 获取出款预警的基本信息设置
     * @return
     */
    @RequestMapping("/getOutWarnInfo")
    @ResponseBody
    public Result getOutWarn(){
        Result result = new Result();
        try {
            Map<String, Object> warnInfo = superBankService.getOutWarn();
            if(warnInfo != null){
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(warnInfo);
            }
        }catch (Exception e){
            log.info("获取出款预警信息异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 修改出款预警信息
     * @param param
     * @return
     */
    @RequestMapping("/updateOutWarn")
    @ResponseBody
    @SystemLog(operCode = "superbank.updateOutWarn", description = "修改出款预警信息")
    public Result updateOutWarn(@RequestBody Map<String, Object> param){
        Result result = new Result();
        try {
            result = superBankService.updateOutWarn(param);
        }catch (Exception e){
            log.info("获取出款预警信息异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 获取出款预警的余额
     * @return
     */
    @RequestMapping("/getOutWarnAccount")
    @ResponseBody
    public Result getOutWarnAccount(){
        Result result = new Result();
        try {
            String warnAccount = superBankService.getOutWarnAccount();
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(warnAccount);
        }catch (Exception e){
            log.info("获取出款预警余额异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 获取出款预警的充值记录
     * @return
     */
    @RequestMapping("/getRechargeList")
    @ResponseBody
    public Result getRechargeList(@RequestBody RechargeRecord record,
                                  @RequestParam(defaultValue = "1") int pageNo,
                                  @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<RechargeRecord> page = new Page<>(pageNo, pageSize);
            superBankService.getRechargeList(record, page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        }catch (Exception e){
            log.info("获取出款预警充值记录异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 超级银行家重置密钥
     * @return
     */
    @RequestMapping("/resetSecretKey")
    @ResponseBody
    @SystemLog(operCode = "superbank.resetSecretKey" ,description = "超级银行家重置密钥")
    public Result resetSecretKey(){
        Result result = new Result();
        String openMerchantKey = RandomStringUtils.randomAlphanumeric(32);
        result.setData(openMerchantKey);
        result.setStatus(true);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 修改超级银行家用户状态
     * @param userInfo
     * @return
     */
    @RequestMapping("/updateUserStatus")
    @ResponseBody
    @SystemLog(operCode = "superBank.updateUserStatus", description = "修改超级银行家用户状态")
    public Result updateUserStatus(@RequestBody SuperBankUserInfo userInfo){
        Result result = new Result();
        try {
            if(userInfo == null || StringUtils.isBlank(userInfo.getUserCode())
                    || StringUtils.isBlank(userInfo.getStatus())){
                result.setMsg("参数不能为空");
                return result;
            }
            int num = superBankService.updateUserStatus(userInfo);
            if(num == 1){
            	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				 
				UserFreezeOperLog userFreezeOperLog = new UserFreezeOperLog();
				userFreezeOperLog.setOperId(principal.getId());//操作人id
				userFreezeOperLog.setOperName(principal.getRealName());//操作人姓名
				userFreezeOperLog.setOperType(userInfo.getStatus());//操作类型：1，解冻；2，冻结
				userFreezeOperLog.setOperReason(userInfo.getOperReason());//冻结解冻原因：允许为空
				userFreezeOperLog.setUserCode(userInfo.getUserCode());//被解冻冻结的用户编码
				int insLog =userFreezeOperLogService.insertUserFreezeOperLog(userFreezeOperLog);//记录冻结解冻日志

				if(insLog>0) {
					result.setStatus(true);
					result.setMsg("操作成功");
				}else {
					result.setMsg("操作失败");
				}
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            log.error("修改超级银行家用户状态异常", e);
            result.setMsg("修改异常");
        }
        return result;
    }

    @RequestMapping("/lotteryOrder")
    @ResponseBody
    public Result lotteryOrder(@RequestBody LotteryOrder baseInfo,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            if(baseInfo == null){
                result.setMsg("参数不能为空");
                return result;
            }
            Page<LotteryOrder> page = new Page<>(pageNo, pageSize);

            System.out.println("-----参数-------"+JSON.toJSON(baseInfo) + "--------------");

            superBankService.qryLotteryOrder(baseInfo, page);
            LotteryOrder sumOrder = superBankService.qrySumOrder(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("sumOrder", sumOrder);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }

    @RequestMapping("/lotteryOrderDetail")
    @ResponseBody
    public Result lotteryOrderDetail(@Param("orderNo")String orderNo) {
        Result result = new Result();
        try {

            System.out.println("-------查询订单--------" + orderNo);

            LotteryOrder info = new LotteryOrder();
            info.setOrderNo(orderNo);
            Page<LotteryOrder> page = new Page<>(1, 10);
            List<LotteryOrder> list = superBankService.qryLotteryOrder(info, page);

            LotteryOrder order = list.size() > 0 ? list.get(0):null;

            System.out.println("---------------查询结果----------" + JSON.toJSONString(order));

            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(order);
        } catch (Exception e){
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }

    @RequestMapping("/exportLotteryOrder")
    @SystemLog(operCode = "superBank.exportLotteryOrder", description = "导出彩票代购订单")
    public void exportLotteryOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            LotteryOrder info = JSONObject.parseObject(baseInfo, LotteryOrder.class);
            superBankService.exportLotteryOrder(response, info);
        } catch (Exception e){
            log.error("导出彩票代购订单异常", e);
        }
    }

    /**
     * 出款预警余额的充值
     * @return
     */
    @RequestMapping("/recharge")
    @ResponseBody
    @SystemLog(operCode = "superbank.recharge" ,description = "超级银行家出款预警账户充值")
    public Result recharge(@RequestBody Map<String, Object> param){
        Result result = new Result();
        try {
            String amount = param.get("amount").toString();
            if(StringUtils.isBlank(amount)){
                result.setMsg("充值金额不能为空");
                return result;
            }
            superBankService.updateRecharge(amount);
            result.setStatus(true);
            result.setMsg("操作成功");
        } catch (NumberFormatException e){
            log.info("金额格式有误", e);
            result.setMsg("金额格式有误");
        } catch (Exception e){
            log.info("出款预警余额的充值异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 超级银行家排行榜
     * @return
     */
    @RequestMapping("/rankingRecord")
    @ResponseBody
    @SystemLog(operCode = "superbank.rankingRecord" ,description = "超级银行家排行榜")
    public Result rankingRecord(@RequestBody RankingRecord baseInfo,
                                @RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<RankingRecord> page = new Page<>(pageNo, pageSize);

            log.info("--------------传入参数----------------" + JSON.toJSONString(baseInfo) + "-----------------------");
            if("-1".equals(baseInfo.getOrgId())){
                baseInfo.setOrgId(null);
            }
            superBankService.qryRankingRecord(baseInfo,page);
            RankingRecord record = superBankService.getRankingRecordTotalInfo(baseInfo);

            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("page", page);
            map.put("orderMainSum", record);
            result.setStatus(true);
            result.setMsg("操作成功");
            result.setData(map);
        } catch (Exception e){
            log.info("排行榜查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 根据条件查询排行榜规则管理
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/rankingRuleList")
    @ResponseBody
    @SystemLog(operCode = "superbank.rankingRuleList" ,description = "超级银行家排行榜")
    public Result rankingRuleList(@RequestBody RankingRule baseInfo,
                                  @RequestParam(defaultValue = "1") int pageNo,
                                  @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try{
            Page<RankingRule> page = new Page<>(pageNo, pageSize);
            List<RankingRule> list = superBankService.getRankingRuleList(baseInfo, page);
            if(list != null && list.size() > 0){
                for (RankingRule info: list){
                    if(StringUtils.isNotBlank(info.getAdvertUrl())){
                        info.setAdvertUrl(CommonUtil.getImgUrlAgent(info.getAdvertUrl()));
                    }
                    if(StringUtils.isNotBlank(info.getStatus())){
                        if("1".equals(info.getStatus())){
                            info.setStatusInt(1);
                        }else{
                            info.setStatusInt(0);
                        }
                    }
                }
            }
            page.setResult(list);
            result.setData(page);
            result.setStatus(true);
            result.setMsg("查询成功");
        }catch(Exception e){
            log.info("获取排行榜管理列表失败",e);
        }

        return result;
    }
    /**
     * 根据条件查询排行榜规则管理
     * @return
     */
    @RequestMapping("/updateRankingRuleStatus")
    @ResponseBody
    @SystemLog(description = "修改排行榜活动开关", operCode = "superbank.updateRankingRuleStatus")
    public Result updateRankingRuleStatus(@RequestBody RankingRule info) {
        Result result = new Result();
        try {
            if (info.getId() == null || StringUtils.isBlank(info.getStatus())) {
                result.setStatus(false);
                result.setMsg("参数不能为空");
                return result;
            }

            int num = superBankService.updateBankingRuleStatus(info);
            if (num == 1) {
                SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                String back = ClientInterface.superBankPushRankingRule(info.getId().toString(),
                        sysDict.getSysValue() + Constants.REFRESH_RANKING_RULE);
                if (!StringUtils.isBlank(back)) {
                    JSONObject jsonObject = JSONObject.parseObject(back);
                    if (jsonObject.containsValue("success")) {
                        result.setStatus(true);
                        result.setMsg("操作成功");
                    } else {
                        result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                    }
                } else {
                    result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
                }
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e) {
            log.error("修改银行上架状态异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 排行榜组织各级奖金设置详情
     * @return
     */
    @RequestMapping(value = "/rankingRuleLevel")
    @ResponseBody
    public Result rankingRuleLevel(@RequestParam("id") Long id,@RequestParam("operate")String operate) {
        Result result = new Result();
        if(id == null){
            result.setStatus(false);
            result.setMsg("id不能为空");
            return result;
        }
        if(operate == null){
            result.setStatus(false);
            result.setMsg("operate不能为空");
            return result;
        }
        RankingRule rankingRule = null;


        if("0".equals(operate)){ //新增操作
            rankingRule = new RankingRule();
            List<OrgInfo> orgList = superBankService.getOrgInfoList(); //所有组织
            rankingRule.setOrgList(orgList);

        }else if("1".equals(operate) || "2".equals(operate)){ //1.修改操作 ,2.显示操作
            rankingRule = superBankService.getRankingRuleById(id);
            if(rankingRule != null){
                if(StringUtils.isNotBlank(rankingRule.getAdvertUrl())){
                    rankingRule.setAdvertUrl(CommonUtil.getImgUrlAgent(rankingRule.getAdvertUrl()));
                }
            }else {
                result.setStatus(false);
                result.setMsg("Id为："+id+"的排行榜配置不存在");
                return result;
            }

            List<RankingRuleLevel> levelList = superBankService.getRankingRuleLevelListByRuleId(id);
            if(levelList != null){
                rankingRule.setLevelList(levelList);
            }
            if("2".equals(operate)){
                List<RankingRuleHistory> hisList = superBankService.selectRankingRuleHistory(id);
                if(hisList != null){
                    rankingRule.setHisList(hisList);
                }
            }

        }else{
            result.setStatus(false);
            result.setMsg("非法操作");
            return result;
        }

        rankingRule.setOperate(operate);
        result.setStatus(true);
        result.setMsg("查询成功");
        result.setData(rankingRule);

        return result;
    }

    @RequestMapping("/saveRankingRuleLevel")
    @ResponseBody
    public Result saveRankingRuleLevel(@RequestBody RankingRule baseInfo) {
        Result result = new Result();
        if(baseInfo == null || StringUtil.isBlank(baseInfo.getOperate())
                || (!"0".equals(baseInfo.getOperate()) && !"1".equals(baseInfo.getOperate()))){
            result.setStatus(false);
            result.setMsg("参数错误");
            return result;
        }
        Long ruleId = baseInfo.getId();
        if("0".equals(baseInfo.getOperate())){ //新增
            String nowStr = DateUtil.getMessageTextTime();
            baseInfo.setRuleCode("PH_" + nowStr);
            log.info("~~~~~~生成排行榜记录 ruleCode:"+baseInfo.getRuleCode());
            result = superBankService.insertRankingRule(baseInfo);
            if(result != null){
                return result;
            }
            log.info("~~~~~排行榜 ruleCode:"+baseInfo.getRuleCode()+"生成成功~~~~~~");
            RankingRule rankingRule = superBankService.getRankingRuleByCode(baseInfo.getRuleCode());
            if(rankingRule != null &&baseInfo.getLevelList() != null){
                superBankService.insertRankingRuleLevelBatch(baseInfo.getLevelList(), rankingRule.getId());
            }
            ruleId = rankingRule.getId();
        }else if("1".equals(baseInfo.getOperate())){//修改
            //获取修改前的对象获取组织编号
            RankingRule rankingRule = superBankService.getRankingRuleById(baseInfo.getId());
            String orgAfter = baseInfo.getOpenOrgInfo();
            if(rankingRule != null){
                //校验：通过最新修改时间判断是否修改过
                if(!baseInfo.getUpdateTime().equals(rankingRule.getUpdateTime())){
                    result.setStatus(false);
                    result.setMsg("配置已被修改，请刷新页面后再进行修改！");
                    return result;
                }
                String status = rankingRule.getStatus(); //判断修改前的状态跟修改后的状态，假如从0->1则打开数量+1
                int openTime = rankingRule.getOpenTime();
                if("0".equals(status)){
                    if("1".equals(baseInfo.getStatus())){
                        baseInfo.setOpenTime(openTime + 1);
                    }
                }
                if(baseInfo.getAdvertUrl().contains("http:")){ //假如图片未修改则取原来没有拼接的图片名称
                    baseInfo.setAdvertUrl(rankingRule.getAdvertUrl());
                }
            }

            result = superBankService.updateRankingRule(baseInfo);
            if(result == null || !result.isStatus()){
                return result;
            }

            /**假如组织修改，则生成修改记录*/
            if(!orgAfter.equals(rankingRule.getOpenOrgInfo())){
                //insertRankingRuleHistory
                RankingRuleHistory hisRecord = new RankingRuleHistory();
                hisRecord.setRuleId(baseInfo.getId());
                hisRecord.setOrgBefore(rankingRule.getOpenOrgInfo());
                hisRecord.setOrgAfter(orgAfter);
                superBankService.insertRankingRuleHistory(hisRecord);
            }

            /**排行榜等级设置先删除后插入新的*/
            superBankService.deleteRankingRuleLevelByRuleId(baseInfo.getId());
            if(baseInfo.getLevelList() != null && baseInfo.getLevelList().size() > 0){
                superBankService.insertRankingRuleLevelBatch(baseInfo.getLevelList(), baseInfo.getId());
            }
            ruleId = baseInfo.getId();
        }
        if(result == null){
            result = new Result();
        }
        /**刷新缓存*/
        SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
        String back = ClientInterface.superBankPushRankingRule(ruleId.toString(),
                sysDict.getSysValue() + Constants.REFRESH_RANKING_RULE);
        if (!StringUtils.isBlank(back)) {
            JSONObject jsonObject = JSONObject.parseObject(back);
            if (jsonObject.containsValue("success")) {
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            }
        } else {
            result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
        }

        return result;
    }


    /**榜单明细*/
    @RequestMapping("/rankingRecordDetail")
    @ResponseBody
    @SystemLog(operCode = "superbank.rankingRecordDetail" ,description = "排行榜榜单明细")
    public Result rankingRecordDetail(@Param("recordId")String recordId){
        Result result = new Result();
        try{
            Page<RankingRecordDetail> page = new Page<>(1, Integer.MAX_VALUE);

            log.info("---------------------编号---------------" +  recordId + "---------------------------");

            RankingRecordDetail record = superBankService.qryRankingDetail(recordId, page,true);

            Map<String,Object> map = new HashMap<String,Object>();

            System.out.println(JSON.toJSONString(record) + "------size -----"+page.getResult().size());

            map.put("page", page);
            map.put("sumOrder", record);
            result.setData(map);
            result.setStatus(true);
            result.setMsg("查询成功");
        }catch(Exception e){
            log.info("获取榜单明细失败",e);
        }

        return result;
    }


    /**榜单明细查询*/
    @RequestMapping("/findRankingRecordDetail")
    @ResponseBody
    public Result findRankingRecordDetail(@Param("recordId")String recordId){
        Result result = new Result();
        try{
            Page<RankingRecordDetail> page = new Page<>(1, Integer.MAX_VALUE);

            log.info("---------------------编号---------------" +  recordId + "---------------------------");

            RankingRecordDetail record = superBankService.qryRankingDetail(recordId, page,false);

            Map<String,Object> map = new HashMap<String,Object>();

            System.out.println(JSON.toJSONString(record) + "------size -----"+page.getResult().size());

            map.put("page", page);
            map.put("sumOrder", record);
            result.setData(map);
            result.setStatus(true);
            result.setMsg("查询成功");
        }catch(Exception e){
            log.info("获取榜单明细失败",e);
        }

        return result;
    }

    /**移除明细记录*/
    @RequestMapping("/removeRankingDetail")
    @ResponseBody
    public Result removeRankingDetail(@RequestBody RankingRecordDetail baseInfo){
        Result result = new Result();
        try{
            baseInfo.setStatus("2");

            baseInfo.setRemoveTime(new Date());

            log.info("---------------" + JSON.toJSONString(baseInfo) + "------------------");

            int count = superBankService.updRankingDetail(baseInfo);
            if(count>0){
                result.setMsg("移除成功");
                result.setStatus(true);
            }else{
                result.setMsg("移除失败");
                result.setStatus(false);
            }
        }catch(Exception e){
            log.info("移除榜单排名",e);
        }
        return result;
    }

    /**取消移除排名*/
    @RequestMapping("/cancelRemoveRankingDetail")
    @ResponseBody
    public Result cancelRemoveRankingDetail(@Param("detailId") String detailId){
        Result result = new Result();
        try{
            RankingRecordDetail baseInfo = new RankingRecordDetail();
            baseInfo.setStatus("0");

            baseInfo.setRemoveTime(null);
            baseInfo.setRemark(null);
            baseInfo.setId(detailId);

            int count = superBankService.updRankingDetail(baseInfo);
            if(count>0){
                result.setMsg("取消移除成功");
                result.setStatus(true);
            }else{
                result.setMsg("取消移除失败");
                result.setStatus(false);
            }
        }catch(Exception e){
            log.info("取消移除榜单排名",e);
        }
        return result;
    }

    /**发放奖金*/
    @RequestMapping("/pushRankingBonus")
    @ResponseBody
    public Result pushRankingBonus(@Param("recordId") String recordId){
        Result result = new Result();
        if(recordId==null || "null".equals(recordId)){
            result.setMsg("非法调用");
            result.setStatus(false);
            return result;
        }

        Page<RankingRecord> page = new Page<>(1, 10);
        RankingRecord baseInfo = new RankingRecord();
        baseInfo.setId(recordId);
        superBankService.qryRankingRecord(baseInfo,page);
        RankingRecord data = null;
        if(page.getResult().size()>0){
            data = page.getResult().get(0);
        }else{
            result.setMsg("参数非法");
            result.setStatus(false);
            return result;
        }

        if("已发放".equals(data.getStatus())){
            result.setMsg("该榜单已发放，不能重复发放！");
            result.setStatus(false);
            return result;
        }

        String orgId = data.getOrgId();
        if(orgId==null || "".equals(orgId)){
            result.setMsg("排行榜记录未关联组织");
            result.setStatus(false);
            return result;
        }
        OrgInfo org = new OrgInfo();
        org.setOrgId(Long.parseLong(orgId));
        Page<OrgInfo> pageOrg = new Page<OrgInfo>();
        List<OrgInfo> orgInfos = superBankService.selectOrgInfoPage(org,pageOrg);
        try{
            if(orgInfos.size()>0){
                result = superBankService.getOrgAccountInfo(orgInfos.get(0).getV2AgentNumber());
                log.info("---查询账户返回----" + JSON.toJSONString(result));


                JSONObject accoutInfo = null;

                if(result.getData()!=null){
                    JSONArray jarry = JSON.parseArray(JSON.toJSONString(result.getData()));
                    accoutInfo = jarry.getJSONObject(000);
                }

                if(accoutInfo==null){
                    result.setMsg("没有找到账户余额");
                    result.setStatus(false);
                    return result;
                }

                RankingRecordDetail rankParams = new RankingRecordDetail();
                rankParams.setRecordId(recordId);
                rankParams.setIsRank("1");
                rankParams.setStatus("0");
                List<RankingRecordDetail> detailList = superBankService.getPushRankingDetail(rankParams);

                if(detailList.size() > 0){
                    Page<RankingRecordDetail> rrdPage = new Page<RankingRecordDetail>(1,Integer.MAX_VALUE);
                    //RankingRecordDetail record = superBankService.qryRankingDetail(recordId, rrdPage,true);//实现一次排名(防止不审核直接发放奖金报错)
                    //detailList = superBankService.getPushRankingDetail(rankParams);//重新查询一次,获取排名结果
                    result = superBankService.pushRankingBonus(data, detailList, accoutInfo);
                    if(result.isStatus())
                        superBankService.invokeInterface(data);

                }else{
                    result.setMsg("没有要发放的数据,发放失败！");
                    result.setStatus(false);
                    return result;
                }

            }else{
                result.setMsg("排行榜未找到组织");
                result.setStatus(false);
                return result;
            }
        }catch(Exception e){
            log.info("发放奖金失败",e);
            result.setMsg("发放奖金失败！");
            result.setStatus(false);
        }
        return result;
    }

    /**违章代缴订单查询*/
    @RequestMapping("/findCarOrder")
    @ResponseBody
    public Result findCarOrder(@RequestBody CarOrder order,@RequestParam( defaultValue = "1")int pageNo,
                               @RequestParam( defaultValue = "10")int pageSize){
        Result result = new Result();
        try{
            Page<CarOrder> page = new Page<CarOrder>(pageNo,pageSize);

            order = superBankService.getCarOrders(order, page);

            Map<String,Object> data = new HashMap<String,Object>();
            data.put("orderSum", order);
            data.put("page", page);

            result.setStatus(true);
            result.setData(data);
            result.setMsg("查询成功");
        }catch(Exception e){
            e.printStackTrace();
            result.setStatus(false);
            result.setMsg("查询异常");
        }


        return result;
    }

    /**
     * 违章代缴订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/carOrderDetail")
    @ResponseBody
    public Result carOrderDetail(String orderNo){
        Result result = new Result();
        result.setMsg("详情查询失败");
        try {
            result = superBankService.carOrderDetail(orderNo);
            CarOrder order = (CarOrder)result.getData();
            if(order!=null){
                if("20".equals(order.getOneUserType())){
                    order.setOneUserType("专员");
                }else if("30".equals(order.getOneUserType())){
                    order.setOneUserType("经理");
                }else if("40".equals(order.getOneUserType())){
                    order.setOneUserType("银行家");
                }else{
                    order.setOneUserType(null);
                }

                if("20".equals(order.getTwoUserType())){
                    order.setTwoUserType("专员");
                }else if("30".equals(order.getTwoUserType())){
                    order.setTwoUserType("经理");
                }else if("40".equals(order.getTwoUserType())){
                    order.setTwoUserType("银行家");
                }else{
                    order.setTwoUserType(null);
                }

                if("20".equals(order.getThrUserType())){
                    order.setThrUserType("专员");
                }else if("30".equals(order.getThrUserType())){
                    order.setThrUserType("经理");
                }else if("40".equals(order.getThrUserType())){
                    order.setThrUserType("银行家");
                }else{
                    order.setThrUserType(null);
                }

                if("20".equals(order.getFouUserType())){
                    order.setFouUserType("专员");
                }else if("30".equals(order.getFouUserType())){
                    order.setFouUserType("经理");
                }else if("40".equals(order.getFouUserType())){
                    order.setFouUserType("银行家");
                }else{
                    order.setFouUserType(null);
                }

                result.setData(order);
            }
        } catch (Exception e){
            log.error("查看超级银行家组织详情异常", e);
        }
        return result;
    }

    /**导出违章订单*/
    @RequestMapping("/exportCarOrder")
    @ResponseBody
    public Result exportCarOrder(@RequestParam("baseInfo") String baseInfo,HttpServletResponse response,HttpServletRequest request){
        Result result = new Result();
        result.setMsg("导出异常");
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
                CarOrder order = JSON.parseObject(baseInfo, CarOrder.class);
                superBankService.exportCarOrder(response,order);
            }
        } catch (Exception e){
            log.error("导出异常", e);
        }
        return result;
    }

    /**违章代缴订单奖金设置*/
    @RequestMapping("/carOrderProfitConf")
    @ResponseBody
    public Result carOrderProfitConf(){
        Result result = new Result();
        try{

            CarOrderProfitConf data = superBankService.findCarOrderProfitConf();

            if(data == null) {
                data = new CarOrderProfitConf();
            }

            result.setStatus(true);
            result.setData(data);
            result.setMsg("查询成功");
        }catch(Exception e){
            e.printStackTrace();
            result.setStatus(false);
            result.setMsg("查询异常");
        }

        return result;
    }

    /**更新违章代缴奖金配置*/
    @RequestMapping("/updCarOrderProfitConf")
    @ResponseBody
    public Result updCarOrderProfitConf(@RequestBody CarOrderProfitConf conf){
        Result result = new Result();
        try{

            int count = 0;
            if(conf.getId() != null){
                count = superBankService.updCarOrderProfitConf(conf);
            }else{
                count = superBankService.addCarOrderProfitConf(conf);
            }

            if(count > 0){
                result.setStatus(true);
                result.setMsg("修改成功");
            }else {
                result.setStatus(false);
                result.setMsg("修改出错，请重试");
            }

        }catch(Exception e){
            e.printStackTrace();
            result.setStatus(false);
            result.setMsg("修改异常");
        }

        return result;
    }
    
    /**
     * 查询黑名单
     * @param Result
     */
    @RequestMapping("/selectBlackList")
    @ResponseBody
    public Result blackManager(@RequestBody BlackList baseInfo,
                                 @RequestParam(defaultValue = "1") int pageNo,
                                 @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            Page<BlackList> page = new Page<>(pageNo, pageSize);
            superBankService.selectBlacks(baseInfo, page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e) {
            log.error("查询黑名单异常", e);
            result.setMsg("查询异常");
        }
        return result;
    }
    
    /**
     *黑名单日志列表 
     * @param Result
     */
    @RequestMapping("/selectBlackLogList")
    @ResponseBody
    public Result selectBlackLogList(@ModelAttribute("page")Page<BlackListLog> page, @RequestParam("blackId") String blackId) {
    	 Result result = new Result();
    	try {
    	       superBankService.selectBlackLogs(page, Long.valueOf(blackId));
    		   result.setStatus(true);
               result.setMsg("查询成功");
               result.setData(page);
		} catch (Exception e) {
			   log.error("查询黑名单异常", e);
	            result.setMsg("查询异常");
		}
    	 return result;
    }
    
    /**
     * 新增黑名单
     * @param Result
     */
    @RequestMapping("/addUserBlack")
    @ResponseBody
    public Result addUserBlack(@RequestBody(required=false) BlackList baseInfo){
    	return superBankService.addUserBlack(baseInfo);
    }
    
    /**
     * 删除黑名单
     * @param Result
     */
    @RequestMapping("/deleteBlack")
    @ResponseBody
    public Result deleteBlack(@RequestParam("id")String id) {
    	   Result result = new Result();
    	   try {
    		   Long _id = JSON.parseObject(id, Long.class);
    		   int i = superBankService.deleteBlackById(_id);
    		   if(i>0) {
    			   result.setMsg("删除成功");
    			   result.setStatus(true);
    		   }else {
    			   result.setMsg("删除失败");
    			   result.setStatus(false);
    		   }
		} catch (Exception e) {
			String str=e.getMessage();
			if(e.getMessage()==null){
				result.setMsg("删除名单异常");
				return result;
			}
			if(str.contains("\r\n")||str.contains("\n"))
			    result.setMsg("删除名单异常");
			else
			    result.setMsg(str);
		}
    	   return result;
    }
    
    /**
     *更新黑名单状态 
     * @param Result
     */
    @RequestMapping("/updateBlackStatus")
    @ResponseBody
    public Result deleteBlack(@RequestBody BlackList baseInfo) {
    	 Result result = new Result();
    	   int i = superBankService.updateBlackById(baseInfo);
    	   if(i>0) {
    		   result.setMsg("更新成功");
    		   result.setStatus(true);
    	   }else {
    		   result.setMsg("更新失败");
    		   result.setStatus(false);
    	   }
    	return result;
    }
    
    
    
}
