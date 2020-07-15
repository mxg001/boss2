package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BonusConf;
import cn.eeepay.framework.model.CreditCardBonus;
import cn.eeepay.framework.model.CreditcardSource;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.ExchangeBonusService;
import cn.eeepay.framework.util.StringUtil;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 积分兑换总奖金管理
 * @author xieh
 * @date 2018/8/16
 */
@Controller
@RequestMapping("/superExchangeBonus")
public class SuperExchangeBonusAction {

    private Logger log = LoggerFactory.getLogger(SuperExchangeBonusAction.class);

    @Resource
    private ExchangeBonusService exchangeBonusService;


    @RequestMapping("/updateSuperExchange")
    @ResponseBody
    public Result updateSuperExchange(@RequestBody BonusConf bonusConf) {
        Result result = new Result();
        log.info("\n-----------传入参数----------\n" + JSON.toJSONString(bonusConf) + "\n-----------------\n");
        try {
        	
        	Result checkResult = checkExchangeBonusData("add",bonusConf);
            if(checkResult != null && !checkResult.isStatus()){
                result.setMsg(checkResult.getMsg());
                return result;
            }
            
            exchangeBonusService.updateBonus(bonusConf);
            result.setStatus(true);
            result.setMsg("修改成功");
        } catch (Exception e) {
            log.error("积分兑换总奖金", e);
            result.setStatus(false);
            result.setMsg("修改异常");
        }
        return result;
    }

    /**
     * 查询所有积分兑换
     * @author xieh
     * @date 2018/7/13
     */
    @RequestMapping("/getAllBonus")
    @ResponseBody
    public Result getByCompanyNo(){
        Result result = new Result();
        try {
            String type="1";
            List<BonusConf> allBonus = exchangeBonusService.getAllBonus(type);
            result.setData(allBonus);
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
     * 查询积分兑换
     * @author xieh
     * @date 2018/7/13
     */
    @RequestMapping("/getSuperExchanges")
    @ResponseBody
    public Result getSuperExchanges(@RequestBody BonusConf bonusConf,
                                    @RequestParam(defaultValue = "1") int pageNo,
                                    @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            log.info("\n-------------- " + JSON.toJSONString(bonusConf) + "-----------------\n");
            Page<BonusConf> page = new Page<>(pageNo, pageSize);
            List<BonusConf>  bonus = exchangeBonusService.getBonus(bonusConf, page);
            page.setResult(bonus);
            result.setData(page);
            result.setStatus(true);
            result.setMsg("查询成功");
        } catch (Exception e) {
            log.error("查询积分兑换异常", e);
            result.setStatus(false);
            result.setMsg("查询异常");
        }
        return result;
    }
    /**
     *
     * 积分兑换总奖金配置
     * @return
     */
    @RequestMapping("/addSuperExchange")
    @ResponseBody
    public Result addSuperExchange(@RequestBody BonusConf bonusConf){
        Result result = new Result();
        try {
            log.info("\n-------"+JSON.toJSONString(bonusConf)+"-------\n");
            if(bonusConf == null ){
                result.setStatus(false);
                result.setMsg("参数不能为空");
                return result;
            }
            Result checkResult = checkExchangeBonusData("add",bonusConf);
            if(checkResult != null && !checkResult.isStatus()){
                result.setMsg(checkResult.getMsg());
                return result;
            }
            if(StringUtil.isNotBlank(bonusConf.getOrgId())) {
	            boolean b = exchangeBonusService.checkOrgExist(bonusConf.getOrgId());
	            if(b){
	                result.setMsg("该组织依存在奖金配置");
	                return result;
	            }
            }
            exchangeBonusService.addBonus(bonusConf);
            result.setStatus(true);
            result.setMsg("新增成功");
        } catch (Exception e) {
            log.error("新增数据异常", e);
            result.setStatus(false);
            result.setMsg("新增失败,请检查数据");
        }
        return result;
    }
    
    
   
    public Result checkExchangeBonusData(String operType,BonusConf bonusConf) {
        Result result = new Result();
        if( null== bonusConf ){
            result.setMsg("参数不能为空");
            return result;
        }
        String totalBonus = bonusConf.getTotalBonus();
        String companyBonus = bonusConf.getCompanyBonus();
        String orgBonus = bonusConf.getOrgBonus();
        Long orgId = bonusConf.getOrgId();
        if("add".equals(operType)) {
        	 if( null== orgId ){
                 result.setMsg("组织不能为空");
                 return result;
             }
        }
        if(StringUtils.isBlank(totalBonus)){
            result.setMsg("积分兑换总奖金不能为空");
            return result;
        }
        if(StringUtils.isBlank(companyBonus)){
            result.setMsg("公司截留不能为空");
            return result;
        }
        if(StringUtils.isBlank(orgBonus)){
            result.setMsg("品牌截留不能为空");
            return result;
        }
        //根据总奖金的格式，判断现在填写的，格式是否一致，再判断大小
        if(!checkMoneyType(totalBonus)){
            result.setMsg("积分兑换总奖金输入不合法");
            return result;
        }
        if(!checkMoneyType(companyBonus)){
            result.setMsg("公司截留输入不合法");
            return result;
        }
        if(!checkMoneyType(orgBonus)){
            result.setMsg("品牌截留输入不合法");
            return result;
        }
        // 发卡公司截留+发卡品牌截留<=发卡奖金
        if((new BigDecimal(totalBonus)).compareTo(new BigDecimal(companyBonus).add(new BigDecimal(orgBonus))) <0) {
            result.setMsg("积分兑换总奖金应该大于等于公司截留和品牌截留的和");
            return result;
        }
     
        result.setStatus(true);
        result.setMsg("校验通过");
        return result;
    }
    
    /**
          * 格式要和总奖金的一致
     * @param type
     * @param bonus
     * @return
     */
    public boolean checkMoneyType(String bonus){
        Pattern pattern = null;
        Matcher matcherCompany = null;
        pattern = Pattern.compile("^[0-9]+(\\.[0-9]{0,2})?$");
        matcherCompany = pattern.matcher(bonus);
        if(matcherCompany.find()){
            return true;
        } else {
            return false;
        }
    }
}
