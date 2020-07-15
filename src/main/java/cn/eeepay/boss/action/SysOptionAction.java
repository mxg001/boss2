package cn.eeepay.boss.action;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SysOption;
import cn.eeepay.framework.service.SysOptionService;
import cn.eeepay.framework.util.ResponseUtil;

@Controller
@RequestMapping(value="/sysOption")
public class SysOptionAction {

	private static final Logger log = LoggerFactory.getLogger(SysOptionAction.class);
	
	@Resource
    private SysOptionService sysOptionService;
	
    @RequestMapping("/selectSysOption")
    @ResponseBody
    public Result redOrgListAll(@RequestBody SysOption sysOption){
        Result result = new Result();
        try {
            List<SysOption> redOrgList =  sysOptionService.selectSysOption(sysOption);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(redOrgList);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询系统字典选项异常!", e);
        }
        return result;
    }
}
