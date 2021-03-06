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
import cn.eeepay.framework.model.CreditcardSource;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.CreditcardSourceService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;


/**
 * 信用卡银行管理
 * @author tans
 * @date 2018/3/26
 */
@Controller
@RequestMapping("/creditcardSource")
public class CreditcardSourceAction {

    private Logger log = LoggerFactory.getLogger(CreditcardSourceAction.class);

    @Resource
    private CreditcardSourceService creditcardSourceService;
    
    @Resource
    private SysDictService sysDictService;

    /**
     * 信用卡银行管理
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Result selectList(@RequestBody CreditcardSource baseInfo,
                             @RequestParam(defaultValue = "1")int pageNo,
                             @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try {
            Page<CreditcardSource> page = new Page<>(pageNo, pageSize);
            creditcardSourceService.selectList(baseInfo, page);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(page);
        } catch (Exception e){
            log.error("信用卡银行管理查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 银行详情
     * @param id
     * @return
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public Result bankDetail(@PathVariable("id")Long id){
        Result result = new Result();
        try {
            if(id == null){
                result.setMsg("参数不能为空");
                return result;
            }
            CreditcardSource baseInfo = creditcardSourceService.selectDetail(id);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(baseInfo);
        } catch (Exception e){
            log.error("信用卡银行详情查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 新增银行
     * @return
     */
    @RequestMapping("/addBank")
    @ResponseBody
    @SystemLog(description = "新增银行", operCode = "creditcardSource.addBank")
    public Result addBank(@RequestBody CreditcardSource info){
        Result result = new Result();
        try {
            int num = creditcardSourceService.addBank(info);
            if (num > 0){
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.superBankPushLoanSource(info.getId().toString(),sysDict.getSysValue()+Constants.REFRESH_CREDIT_SOURCE );
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
            log.error("新增银行异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 修改银行
     * @return
     */
    @RequestMapping("/updateBank")
    @ResponseBody
    @SystemLog(description = "修改银行", operCode = "creditcardSource.updateBank")
    public Result updateBank(@RequestBody CreditcardSource info){
        Result result = new Result();
        try {
            int num = creditcardSourceService.updateBank(info);
            if (num == 1){
            	//刷新信用卡银行配置(当信用卡银行更改时调用)
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.superBankPushLoanSource(info.getId().toString(),sysDict.getSysValue()+Constants.REFRESH_CREDIT_SOURCE );
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
            log.error("修改银行异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 修改银行上架状态
     * @param info
     * @return
     */
    @RequestMapping("/updateBankStatus")
    @ResponseBody
    @SystemLog(description = "修改银行上架状态", operCode = "creditcardSource.updateBankStatus")
	public Result updateBankStatus(@RequestBody CreditcardSource info) {
		Result result = new Result();
		try {
			if (info.getId() == null || StringUtils.isBlank(info.getStatus())) {
				result.setMsg("参数不能为空");
				return result;
			}
			int num = creditcardSourceService.updateBankStatus(info);
			if (num == 1) {
				SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
				String back = ClientInterface.superBankPushLoanSource(info.getId().toString(),
						sysDict.getSysValue() + Constants.REFRESH_CREDIT_SOURCE);
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

}
