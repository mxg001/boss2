package cn.eeepay.boss.action;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;
import cn.eeepay.framework.model.RepayProfitDetailBean;
import cn.eeepay.framework.service.ProviderService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.ResponseUtil;

/**
 * 信用卡还款服务商管理
 * Created by 666666 on 2017/10/27.
 */
@RestController
@RequestMapping("/providerAction")
public class ProviderAction {

	private Logger log = LoggerFactory.getLogger(ProviderAction.class);

    @Resource
    private ProviderService providerService;
    private final static Pattern costPattern = Pattern.compile("^(\\d+(?:\\.\\d{0,6})?)%\\+(\\d+(?:\\.\\d{0,2})?)$");

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/listProvider")
	public Map<String, Object> listProvider(@RequestBody ProviderBean providerBean, int pageNo, int pageSize) {
        try {
            Page<AgentInfo> page = new Page<>(pageNo, pageSize);
            List<ProviderBean> listProvider = providerService.listProvider(providerBean, page);
            if (!CollectionUtils.isEmpty(listProvider)){
                for (ProviderBean bean : listProvider){
                    if (bean.getRate() != null){
                        bean.setRate(bean.getRate().multiply(new BigDecimal(100)));
                    }
                    if (bean.getFullRepayRate() != null){
                    	bean.setFullRepayRate(bean.getFullRepayRate().multiply(new BigDecimal(100)));
                    }
                    if (bean.getPerfectRepayRate() != null){
                    	bean.setPerfectRepayRate(bean.getPerfectRepayRate().multiply(new BigDecimal(100)));
                    }
                }
            }
            return ResponseUtil.buildResponseMap(listProvider, page.getTotalCount());
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "开通信用卡超级还款",operCode="provider.open")
    @RequestMapping("/openSuperRepayment")
    public Map<String, Object> openSuperRepayment(@RequestBody List<String> agentNoList){
        try {
            return ResponseUtil.buildResponseMap(providerService.openSuperRepayment(agentNoList));
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

	@SystemLog(description = "信用卡超级还款修改成本",operCode="provider.updateCost")
	@RequestMapping("/updateServiceCost")
	public Map<String, Object> updateServiceCost(@RequestBody ProviderBean bean) {
		try {
			Matcher matcherCost = costPattern.matcher(bean.getCost());
			Matcher matcherFullRepayCost = costPattern.matcher(bean.getFullRepayCost());
			Matcher matcherPerfectRepayCost = costPattern.matcher(bean.getPerfectRepayCost());
			if (!matcherCost.matches()) {
				throw new BossBaseException("修改的成本格式不合法(例:0.63%+2)");
			}
			if (!matcherFullRepayCost.matches()) {
				throw new BossBaseException("修改的全额还款成本格式不合法(例:0.63%+2)");
			}
			if (!matcherPerfectRepayCost.matches()) {
				throw new BossBaseException("修改的完美还款成本格式不合法(例:0.63%+2)");
			}
			if (!"1".equals(bean.getAgentLevel())) {
				throw new BossBaseException("只有一级代理商才允许修改");
			}
			if (StringUtils.isBlank(bean.getAccountRatio()) || !bean.getAccountRatio().matches("^([1-9])|([1-9]\\d)|(100)$")) {
				throw new BossBaseException("修改后的分润入账比例必须为正整数且在1~100之间");
			}
			bean.setRate(new BigDecimal(matcherCost.group(1)).divide(new BigDecimal(100)));
			bean.setSingleAmount(new BigDecimal(matcherCost.group(2)));
			bean.setFullRepayRate(new BigDecimal(matcherFullRepayCost.group(1)).divide(new BigDecimal(100)));
			bean.setFullRepaySingleAmount(new BigDecimal(matcherFullRepayCost.group(2)));
			bean.setPerfectRepayRate(new BigDecimal(matcherPerfectRepayCost.group(1)).divide(new BigDecimal(100)));
			bean.setPerfectRepaySingleAmount(new BigDecimal(matcherPerfectRepayCost.group(2)));
			return ResponseUtil.buildResponseMap(providerService.updateServiceCost(bean));
		} catch (Exception e) {
			return ResponseUtil.buildResponseMap(e);
		}
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/listRepayProfitDetail")
	@ResponseBody
	public Map<String, Object> listRepayProfitDetail(@RequestBody RepayProfitDetailBean bean,
			Page<RepayProfitDetailBean> page) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (bean.getProfitMerNo() != null && !bean.getProfitMerNo().isEmpty()) {
				String agentNode = providerService.queryAgentNode(bean.getProfitMerNo());
				if (agentNode == null || agentNode.isEmpty()) {
					msg.put("status", false);
					msg.put("msg", "查询失败");
					return msg;
				} else {
					bean.setAgentNode(agentNode);
				}
			}
			providerService.listRepayProfitDetail(bean, page);
			RepayProfitDetailBean sum = providerService.sumRepayProfitDetail(bean);
			msg.put("status", true);
			msg.put("page", page);
			msg.put("sunProfitDate", sum);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("服务商分润查询失败", e);
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/exportRepayProfitDetail")
	public void exportRepayProfitDetail(@RequestParam("info") String param  , HttpServletResponse response) throws Exception {

		RepayProfitDetailBean bean = JSONObject.parseObject(param, RepayProfitDetailBean.class);
		if(bean!=null){
			if (bean.getProfitMerNo() != null && !bean.getProfitMerNo().isEmpty()) {
				String agentNode = providerService.queryAgentNode(bean.getProfitMerNo());
				if (agentNode == null || agentNode.isEmpty()) {
					return;
				} else {
					bean.setAgentNode(agentNode);
				}
			}
		}else{
			return;
		}
		List<RepayProfitDetailBean> list = providerService.exportRepayProfitDetail(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "服务商分润订单记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("profitNo", null);
			maps.put("profitMerNo", null);
			maps.put("agentName", null);
			maps.put("profitType", null);
			maps.put("shareAmount", null);
			maps.put("toProfitAmount", null);
			maps.put("repayAmount", null);
			maps.put("ensureAmount", null);
			maps.put("repayFee", null);
			maps.put("successPayAmount", null);
			maps.put("successRepayAmount", null);
			maps.put("actualPayFee", null);
			maps.put("actualWithdrawFee", null);
			maps.put("orderNo", null);
			maps.put("merchantNo", null);
			maps.put("status", null);
			maps.put("transTime", null);
			data.add(maps);
		} else {
			for (RepayProfitDetailBean info : list) {
				if (info.getStatus() == null || info.getStatus().isEmpty()) {
				} else if ("0".equals(info.getStatus())) {
					info.setStatus("初始化");
				} else if ("1".equals(info.getStatus())) {
					info.setStatus("未执行");
				} else if ("2".equals(info.getStatus())) {
					info.setStatus("还款中");
				} else if ("3".equals(info.getStatus())) {
					info.setStatus("还款成功");
				} else if ("4".equals(info.getStatus())) {
					info.setStatus("还款失败");
				} else if ("5".equals(info.getStatus())) {
					info.setStatus("挂起");
				} else if ("6".equals(info.getStatus())) {
					info.setStatus("终止");
				} else if ("7".equals(info.getStatus())) {
					info.setStatus("逾期待还");
				}
				if ("1".equals(info.getProfitType())) {
					info.setProfitType("分期还款");
				} else if ("2".equals(info.getProfitType())) {
					info.setProfitType("全额还款");
				} else if ("3".equals(info.getProfitType())){
					info.setProfitType("保证金");
				} else if ("4".equals(info.getProfitType())){
					info.setProfitType("完美还款");
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("profitNo", null == info.getProfitNo() ? "" : info.getProfitNo());
				maps.put("profitMerNo", null == info.getProfitMerNo() ? "" : info.getProfitMerNo());
				maps.put("agentName", null == info.getAgentName() ? "" : info.getAgentName());
				maps.put("profitType", null == info.getProfitType() ? "" : info.getProfitType());
				maps.put("shareAmount", null == info.getShareAmount() ? "" : info.getShareAmount());
				maps.put("toProfitAmount", null == info.getToProfitAmount() ? "" : info.getToProfitAmount());
				maps.put("repayAmount", null == info.getRepayAmount() ? "" : info.getRepayAmount());
				maps.put("ensureAmount", null == info.getEnsureAmount() ? "" : info.getEnsureAmount());
				maps.put("repayFee", null == info.getRepayFee() ? "" : info.getRepayFee());
				maps.put("successPayAmount", null == info.getSuccessPayAmount() ? "" : info.getSuccessPayAmount());
				maps.put("successRepayAmount", null == info.getSuccessRepayAmount() ? "" : info.getSuccessRepayAmount());
				maps.put("actualPayFee", null == info.getActualPayFee() ? "" : info.getActualPayFee());
				maps.put("actualWithdrawFee", null == info.getActualWithdrawFee() ? "" : info.getActualWithdrawFee());
				maps.put("orderNo", null == info.getOrderNo() ? "" : info.getOrderNo());
				maps.put("merchantNo", null == info.getMerchantNo() ? "" : info.getMerchantNo());
				maps.put("status", null == info.getStatus() ? "" : info.getStatus());
				maps.put("transTime", null == info.getTransTime() ? "" : sdf1.format(info.getTransTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = {"profitNo", "profitMerNo", "agentName", "profitType", "shareAmount","toProfitAmount","repayAmount", "ensureAmount",
				"repayFee", "successPayAmount", "successRepayAmount", "actualPayFee", "actualWithdrawFee", "orderNo",
				"merchantNo", "status", "transTime"};
		String[] colsName = {"分润流水号","服务商编号","服务商名称","订单类型","分润","产生分润金额","任务金额","保证金", "服务费","已消费总额","已还款总额",
				"实际消费手续费","实际还款手续费","关联还款订单", "订单用户", "订单状态", "终态时间"};

		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("导出服务商分润订单记录失败!",e);
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}
	}

}
