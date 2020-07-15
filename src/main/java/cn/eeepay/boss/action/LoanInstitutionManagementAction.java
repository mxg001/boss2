package cn.eeepay.boss.action;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LoanSource;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.LoanInstitutionManagementService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;


/**
 * 贷款机构管理
 * @author tans
 * @date 2018/3/26
 */
@Controller
@RequestMapping("/loanInstitutionManagement")
public class LoanInstitutionManagementAction {

    private Logger log = LoggerFactory.getLogger(LoanInstitutionManagementAction.class);

    @Resource
    private LoanInstitutionManagementService loanInstitutionManagementService;
    
    @Resource
    private SysDictService sysDictService;

    /**
     * 贷款机构管理
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Result selectList(@RequestBody LoanSource baseInfo,
                             @RequestParam(defaultValue = "1")int pageNo,
                             @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try {
            Page<LoanSource> page = new Page<>(pageNo, pageSize);
            loanInstitutionManagementService.selectList(baseInfo, page);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(page);
        } catch (Exception e){
            log.error("贷款管理详情查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 贷款机构详情
     * @param id
     * @return
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public Result loanDetail(@PathVariable("id")Long id){
        Result result = new Result();
        try {
            if(id == null){
                result.setMsg("参数不能为空");
                return result;
            }
            LoanSource baseInfo = loanInstitutionManagementService.selectDetail(id);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(baseInfo);
        } catch (Exception e){
            log.error("贷款机构详情查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 新增修改贷款机构
     * @return
     */
    @RequestMapping("/addLoan")
    @ResponseBody
    @SystemLog(description = "新增修改贷款机构", operCode = "loanInstitutionManagement.addLoan")
    public Result addLoan(@RequestBody LoanSource info){
        Result result = new Result();
        try {
            int num = loanInstitutionManagementService.addLoan(info);
            if (num > 0){
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.superBankPushLoanSource(num+"",sysDict.getSysValue()+Constants.REFRESH_LOAN_SOURCE );
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
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            log.error("新增修改贷款机构异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 修改贷款机构
     * @return
     */
    @RequestMapping("/updateLoan")
    @ResponseBody
    @SystemLog(description = "修改贷款机构", operCode = "loanInstitution.updateLoan")
    public Result updateLoan(@RequestBody LoanSource info){
        Result result = new Result();
        try {
            int num = loanInstitutionManagementService.updateLoan(info);
            if (num == 1){
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.superBankPushLoanSource(info.getId().toString(),sysDict.getSysValue()+Constants.REFRESH_LOAN_SOURCE );
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
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            log.error("修改贷款机构异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     *修改贷款管理状态
     * @param info
     * @return
     */
    @RequestMapping("/updateLoanStatus")
    @ResponseBody
    @SystemLog(description = "修改贷款管理状态", operCode = "loanInstitutionManagement.updateLoanStatus")
    public Result updateLoanStatus(@RequestBody LoanSource info){
        Result result = new Result();
        try {
            if(info.getId() == null || StringUtils.isBlank(info.getStatus())){
                result.setMsg("参数不能为空");
                return result;
            }
            int num = loanInstitutionManagementService.updateLoanStatus(info);
            if (num == 1){
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.superBankPushLoanSource(info.getId().toString(),sysDict.getSysValue()+Constants.REFRESH_LOAN_SOURCE );
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
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            log.error("修改修改贷款管理状态异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

}
