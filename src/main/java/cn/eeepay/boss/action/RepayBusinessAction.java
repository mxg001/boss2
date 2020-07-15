package cn.eeepay.boss.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayOemServiceCostBean;
import cn.eeepay.framework.service.RepayBusinessService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;

/**
 * Created by 666666 on 2017/10/31.
 */
@RestController
@RequestMapping("repayBusinessAction")
public class RepayBusinessAction {

    @Resource
    private SysDictService sysDictService;
    @Resource
    private RepayBusinessService repayBusinessService;

    private final static Pattern costPattern = Pattern.compile("^(\\d+(?:\\.\\d{0,6})?)%\\+(\\d+(?:\\.\\d{0,2})?)$");

//    @RequestMapping("/listRepayBusiness")
//    public Map<String, Object> listRepayBusiness(){
//        try {
//            SysDict ratio1 = sysDictService.getByKey("YFB_REPAYMENT_RATIO1");
//            SysDict ratio2 = sysDictService.getByKey("YFB_REPAYMENT_RATIO2");
//            SysDict rate = sysDictService.getByKey("YFB_PROVIDER_COST_RATE");
//            SysDict singleAmount = sysDictService.getByKey("YFB_PROVIDER_COST_SINGLE_AMOUNT");
//            Map<String, String> resultMap = new HashedMap();
//            resultMap.put("ratio1", ratio1 == null ? "" : ratio1.getSysValue());
//            resultMap.put("ratio2", ratio2 == null ? "" : ratio2.getSysValue());
//            resultMap.put("rate", rate == null ? "" : rate.getSysValue());
//            resultMap.put("singleAmount", singleAmount == null ? "" : singleAmount.getSysValue());
//            return ResponseUtil.buildResponseMap(resultMap);
//        }catch (Exception e){
//            return ResponseUtil.buildResponseMap(e);
//        }
//    }

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/listRepayBusiness")
	public Map<String, Object> listRepayBusiness(@RequestBody RepayOemServiceCostBean info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		try {
			Page<RepayOemServiceCostBean> page = new Page<>(pageNo, pageSize);
			List<RepayOemServiceCostBean> repayOemServiceCostBeans = repayBusinessService.listRepayBusiness(page, info);
			if (repayOemServiceCostBeans != null && !repayOemServiceCostBeans.isEmpty()) {
				for (RepayOemServiceCostBean bean : repayOemServiceCostBeans) {
					if (bean.getRate() != null) {
						bean.setRate(bean.getRate().multiply(new BigDecimal(100)));
					}
					if (bean.getFullRepayRate() != null) {
						bean.setFullRepayRate(bean.getFullRepayRate().multiply(new BigDecimal(100)));
					}
					if (bean.getPerfectRate() != null) {
						bean.setPerfectRate(bean.getPerfectRate().multiply(new BigDecimal(100)));
					}
				}
			}
			return ResponseUtil.buildResponseMap(repayOemServiceCostBeans, page.getTotalCount());
		} catch (Exception e) {
			return ResponseUtil.buildResponseMap(e);
		}
	}

//    @SystemLog(description = "信用卡还款修改费率", operCode = "business.updateCost")
//    @RequestMapping("/updateRepayBusiness")
//    public Map<String, Object> updateRepayBusiness(@RequestBody Map<String,String> param){
//        try {
//            String ratio1Param = param.get("ratio1");
//            String ratio2Param = param.get("ratio2");
//            String rateAndSingleAmountParam = param.get("rateAndSingleAmount");
//            SysDict ratio1 = sysDictService.getByKey("YFB_REPAYMENT_RATIO1");
//            SysDict ratio2 = sysDictService.getByKey("YFB_REPAYMENT_RATIO2");
//            try {
//                int radio1Int = Integer.valueOf(ratio1Param);
//                if (radio1Int <= 0 || radio1Int > 100){
//                    throw new BossBaseException("修改比例1值必须在0-100之间");
//                }
//                ratio1.setSysValue(ratio1Param);
//                int radio2Int = Integer.valueOf(ratio2Param);
//                if (radio2Int <= 0 || radio2Int > 100){
//                    throw new BossBaseException("修改比例2值必须在0-100之间");
//                }
//                ratio2.setSysValue(ratio2Param);
//            }catch (NumberFormatException e){
//                throw new BossBaseException("修改比例必须是数字");
//            }
//            Matcher matcher = costPattern.matcher(rateAndSingleAmountParam);
//            if (!matcher.matches()){
//                throw new BossBaseException("修改的费率格式不合法(例:0.63%+2)");
//            }
//            SysDict rate = sysDictService.getByKey("YFB_PROVIDER_COST_RATE");
//            SysDict singleAmount = sysDictService.getByKey("YFB_PROVIDER_COST_SINGLE_AMOUNT");
//            rate.setSysValue(matcher.group(1));
//            singleAmount.setSysValue(matcher.group(2));
//            sysDictService.update(ratio1);
//            sysDictService.update(ratio2);
//            sysDictService.update(rate);
//            sysDictService.update(singleAmount);
//            return ResponseUtil.buildResponseMap(true);
//        }catch (Exception e){
//            return ResponseUtil.buildResponseMap(e);
//        }
//    }

    @SystemLog(description = "信用卡还款修改费率", operCode = "business.updateCost")
    @RequestMapping("/updateRepayBusiness")
    public Map<String, Object> updateRepayBusiness(@RequestBody RepayOemServiceCostBean bean){
        try {
        	if (StringUtils.isEmpty(bean.getAgentNo())) {
        		throw new BossBaseException("服务商编号为空");
			}
            Matcher matcher1 = costPattern.matcher(bean.getRateAndSingleAmount());
            Matcher matcher2 = costPattern.matcher(bean.getFullRepayRateAndSingleAmount());
            Matcher matcher3 = costPattern.matcher(bean.getPerfectRateAndSingleAmount());
            if (!matcher1.matches()){
                throw new BossBaseException("修改的费率格式不合法(例:0.63%+2)");
            }
            if (!matcher2.matches()){
            	throw new BossBaseException("修改的全额还款费率格式不合法(例:0.63%+2)");
            }
            if (!matcher3.matches()){
            	throw new BossBaseException("修改的完美还款费率格式不合法(例:0.63%+2)");
            }
            bean.setRate(new BigDecimal(matcher1.group(1)).divide(new BigDecimal(100)));
            bean.setSingleAmount(new BigDecimal(matcher1.group(2)));
            bean.setFullRepayRate(new BigDecimal(matcher2.group(1)).divide(new BigDecimal(100)));
            bean.setFullRepaySingleAmount(new BigDecimal(matcher2.group(2)));
            bean.setPerfectRate(new BigDecimal(matcher3.group(1)).divide(new BigDecimal(100)));
            bean.setPerfectSingleAmount(new BigDecimal(matcher3.group(2)));
            // 判断是否倒挂，即 费率要大于成本，20180124，mays
            if (!"default".equals(bean.getAgentNo())) {
				RepayOemServiceCostBean costBean = repayBusinessService.queryRepayServiceCost(bean.getAgentNo());
				if (costBean != null) {	//如果查不到成本，直接修改，不需判断
					if (bean.getRate().compareTo(costBean.getRate()) < 0
							|| bean.getSingleAmount().compareTo(costBean.getSingleAmount()) < 0) {
						throw new BossBaseException("修改后的费率不能小于服务商成本");
					}
					if (bean.getFullRepayRate().compareTo(costBean.getFullRepayRate()) < 0
							|| bean.getFullRepaySingleAmount().compareTo(costBean.getFullRepaySingleAmount()) < 0) {
						throw new BossBaseException("修改后的全额还款费率不能小于服务商全额还款成本");
					}
					if (bean.getPerfectRate().compareTo(costBean.getPerfectRate()) < 0
							|| bean.getPerfectSingleAmount().compareTo(costBean.getPerfectSingleAmount()) < 0) {
						throw new BossBaseException("修改后的完美还款费率不能小于服务商完美还款成本");
					}
				}
			}
            return ResponseUtil.buildResponseMap(repayBusinessService.updateRepayBusiness(bean));
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }
}
