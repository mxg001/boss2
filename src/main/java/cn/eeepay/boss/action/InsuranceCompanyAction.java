package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceCompany;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.InsuranceCompanyService;
import cn.eeepay.framework.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 保险公司管理
 * @author xieh
 * @date 2018/7/12
 */
@Controller
@RequestMapping("/insuranceCompany")
public class InsuranceCompanyAction {

    private Logger log = LoggerFactory.getLogger(InsuranceCompanyAction.class);

    @Resource
    private InsuranceCompanyService insuranceCompanyService;
    

    /**
     * 保险公司管理
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Result selectList(@RequestBody InsuranceCompany baseInfo,
                             @RequestParam(defaultValue = "1")int pageNo,
                             @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try {
            Page<InsuranceCompany> page = new Page<>(pageNo, pageSize);
            insuranceCompanyService.selectList(baseInfo, page);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(page);
        } catch (Exception e){
            log.error("保险公司管理查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 保险公司详情
     * @param id
     * @return
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public Result companyDetail(@PathVariable("id")Long id){
        Result result = new Result();
        try {
            if(id == null){
                result.setMsg("参数不能为空");
                return result;
            }
            InsuranceCompany baseInfo = insuranceCompanyService.selectDetail(id);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(baseInfo);
        } catch (Exception e){
            log.error("保险公司详情查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 新增保险公司
     * @return
     */
    @RequestMapping("/addCompany")
    @ResponseBody
    @SystemLog(description = "新增保险公司", operCode = "insuranceCompany.addCompany")
    public Result addBank(@RequestBody InsuranceCompany info){
        Result result = new Result();
        try {
            boolean flag=insuranceCompanyService.selectNickExists(info.getCompanyNickName());
            if(flag){
                result.setMsg("保险公司别称已存在");
                return result;
            }
            flag=insuranceCompanyService.selectOrderExists(info.getShowOrder());
            if(flag){
                result.setMsg("顺序已存在");
                return result;
            }
            int num = insuranceCompanyService.addCompany(info);
            if(num>0) {
                result.setStatus(true);
                result.setMsg("操作成功");
            }
        } catch (Exception e){
            log.error("新增保险公司", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 修改保险公司
     * @return
     */
    @RequestMapping("/updateCompany")
    @ResponseBody
    @SystemLog(description = "修改保险公司", operCode = "insuranceCompany.updateCompany")
    public Result updateBank(@RequestBody InsuranceCompany info){
        Result result = new Result();
        try {
            boolean flag = insuranceCompanyService.selectNickIdExists(info.getCompanyNickName(), info.getCompanyNo().longValue());
            if(flag){
                result.setMsg("保险公司别称已存在");
                return result;
            }
            flag=insuranceCompanyService.selectOrderIdExists(info.getShowOrder(),info.getCompanyNo().longValue());
            if(flag){
                result.setMsg("顺序已存在");
                return result;
            }
            int num = insuranceCompanyService.updateCompany(info);
            if (num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            }
        } catch (Exception e){
            log.error("修改保险公司异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 修改上架状态
     * @param info
     * @return
     */
    @RequestMapping("/updateCompanyStatus")
    @ResponseBody
    @SystemLog(description = "修改保险公司上架状态", operCode = "creditcardSource.updateCompanyStatus")
	public Result updateBankStatus(@RequestBody InsuranceCompany info) {
		Result result = new Result();
		try {
			int num = insuranceCompanyService.updateCompanyStatus(info);
			if (num == 1) {
                result.setStatus(true);
                result.setMsg("操作成功");
            }
		} catch (Exception e) {
			log.error("修改保险公司异常", e);
			result = ResponseUtil.buildResult(e);
		}
		return result;
	}

    /**
     * 获取所有保险公司
     * @param
     * @return
     */
    @RequestMapping("/getCompanyList")
    @ResponseBody
    @SystemLog(description = "获取所有保险公司", operCode = "creditcardSource.getCompanyList")
    public Result getCompanyList() {
        Result result = new Result();
        try {
            List<InsuranceCompany> companyList = insuranceCompanyService.getCompanyList();
            result.setData(companyList);
            result.setStatus(true);
            result.setMsg("操作成功");
        } catch (Exception e) {
            log.error("修改保险公司异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }


}
