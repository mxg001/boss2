package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceBonusConf;
import cn.eeepay.framework.model.InsuranceProduct;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.InsuranceBonusService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


/**
 * 保险产品管理
 * @author xieh
 * @date 2018/7/13
 */
@Controller
@RequestMapping("/insuranceBonus")
public class InsuranceBonusAction {

    private Logger log = LoggerFactory.getLogger(InsuranceBonusAction.class);

    @Resource
    private InsuranceBonusService insuranceBonusService;


    @RequestMapping("/updateInsuranceBonus")
    @ResponseBody
    public Result updateInsuranceBonus(@RequestBody InsuranceBonusConf insuranceBonusConf) {
        Result result = new Result();
        log.info("\n-----------传入参数----------\n" + JSON.toJSONString(insuranceBonusConf) + "\n-----------------\n");
        try {
            insuranceBonusService.updateInsuranceBonus(insuranceBonusConf);
            result.setStatus(true);
            result.setMsg("修改成功");
        } catch (Exception e) {
            log.error("保单总奖金修改异常", e);
            result.setStatus(false);
            result.setMsg("修改异常");
        }
        return result;
    }

    /**
     * 查询保险产品
     * @author xieh
     * @date 2018/7/13
     */
    @RequestMapping("/getByCompanyNo")
    @ResponseBody
    public Result getByCompanyNo(@RequestBody int companyNo){
        Result result = new Result();
        try {
            List<InsuranceProduct> insuranceBonus = insuranceBonusService.getByCompanyNo(companyNo);
            result.setData(insuranceBonus);
            result.setStatus(true);
            result.setMsg("查询成功");
        } catch (Exception e) {
            log.error("查询异常", e);
            result.setStatus(false);
            result.setMsg("查询异常");
        }
        return result;
    }


    /**
     * 查询保险产品
     * @author xieh
     * @date 2018/7/13
     */
    @RequestMapping("/getInsuranceBonus")
    @ResponseBody
    public Result getInsuranceBonus(@RequestBody InsuranceBonusConf insuranceBonusConf,
                                    @RequestParam(defaultValue = "1") int pageNo,
                                    @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            log.info("\n-------------- " + JSON.toJSONString(insuranceBonusConf) + "-----------------\n");

            Page<InsuranceBonusConf> page = new Page<>(pageNo, pageSize);
            List<InsuranceBonusConf> insuranceBonus = insuranceBonusService.getInsuranceBonus(insuranceBonusConf, page);
            page.setResult(insuranceBonus);
            result.setData(page);
            result.setStatus(true);
            result.setMsg("查询成功");
        } catch (Exception e) {
            log.error("查询保险产品异常", e);
            result.setStatus(false);
            result.setMsg("查询异常");
        }
        return result;
    }
    /**
     * 保单总奖金配置
     * @return
     */
    @RequestMapping("/addInsuranceConf")
    @ResponseBody
    public Result addInsuranceConf(@RequestBody InsuranceBonusConf insuranceBonusConf){
        Result result = new Result();
        try {
            log.info("\n-------"+JSON.toJSONString(insuranceBonusConf)+"-------\n");
            if(insuranceBonusConf == null ){
                result.setStatus(false);
                result.setMsg("参数不能为空");
                return result;
            }
            if(insuranceBonusConf.getCompanyNo()==-1 ){
                result.setStatus(false);
                result.setMsg("请选择保险公司别称");
                return result;
            }
            if(insuranceBonusConf.getProductId()==-1 ){
                result.setStatus(false);
                result.setMsg("请选择产品名称");
                return result;
            }
            boolean b = insuranceBonusService.checkProductIdExist(insuranceBonusConf.getProductId());
            if(b){
                result.setStatus(false);
                result.setMsg("该产品已存在奖金配置");
                return result;
            }
            insuranceBonusService.addInsuranceBonus(insuranceBonusConf);
            result.setStatus(true);
            result.setMsg("新增成功");
        } catch (Exception e) {
            log.error("新增数据异常", e);
            result.setStatus(false);
            result.setMsg("新增失败,请检查数据");
        }
        return result;
    }
}
