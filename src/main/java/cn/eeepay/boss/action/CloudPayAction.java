package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CloudPayInfo;
import cn.eeepay.framework.service.CloudPayService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
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
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 云闪付
 * @author mays
 * @date 2017年10月23日
 */
@Controller
@RequestMapping(value = "/cloudPay")
public class CloudPayAction {
	private Logger log = LoggerFactory.getLogger(CloudPayAction.class);

	@Resource
	private CloudPayService cloudPayService;

	/**
	 * 商户收益查询
	 * @author mays
	 * @date 2017年10月23日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectCloudPayByParam")
	@ResponseBody
	public Map<String, Object> selectCloudPayByParam(@RequestBody CloudPayInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			// 查询时间不能查询6个月以前的，跨度不超过三个月
			if (!processData(info, msg)) {
				return msg;
			}
			Page<CloudPayInfo> page = new Page<>(pageNo, pageSize);
			cloudPayService.selectCloudPayByParam(page, info);
			String merchantProfitCount=cloudPayService.selectMerchantProfitCount(info);
			msg.put("status", true);
			msg.put("page", page);
			msg.put("merchantProfitCount", merchantProfitCount);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("商户收益查询失败", e);
		}
		return msg;
	}

	/**
	 * 处理数据
	 * @author mays
	 * @date 2017年10月23日
	 * @return true 表示没问题，false 表示有问题
	 */
	private boolean processData(CloudPayInfo info, Map<String, Object> msg) throws ParseException {
		if (StringUtils.isBlank(info.getsTime())) {
			msg.put("status", false);
			msg.put("msg", "查询起始时间不能为空");
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isBlank(info.geteTime())) {	//没有结束时间就给它加上当前时间
			String now = sdf.format(new Date());
			info.seteTime(now);
		}
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sdf.parse(info.getsTime()));
		Calendar eCalendar = Calendar.getInstance();
		eCalendar.setTime(sdf.parse(info.geteTime()));
		// 起始时间加三个月和结束时间比较
		sCalendar.add(Calendar.MONTH, 3);
		if (sCalendar.compareTo(eCalendar) == -1) {
			msg.put("status", false);
			msg.put("msg", "查询时间跨度不能超过3个月");
			return false;
		}
		// 起始时间再加三个月和当前时间比较, 判断是否6个月以前的
		sCalendar.add(Calendar.MONTH, 3);
		if (sCalendar.compareTo(Calendar.getInstance()) == -1) {
			msg.put("status", false);
			msg.put("msg", "不能查询6个月以前的");
			return false;
		}
		return true;
	}

	/**
	 * 导出商户收益
	 * @author mays
	 * @date 2017年10月23日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/exportInfo")
	@ResponseBody
	public void exportInfo(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response) throws Exception {
		baseInfo = new String(baseInfo.getBytes("ISO8859-1"), "UTF-8");
		CloudPayInfo info = JSON.parseObject(baseInfo, CloudPayInfo.class);
		if (!processData(info, new HashMap<String, Object>())) {
			return;
		}
		List<CloudPayInfo> list = cloudPayService.importCloudPayByParam(info);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "商户收益记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("merchantName", null);
			maps.put("merchantNo", null);
			maps.put("mobilephone", null);
			maps.put("agentName", null);
			maps.put("oneAgentName", null);
			maps.put("transAmount", null);
			maps.put("transFee", null);
			maps.put("discountFee", null);
			maps.put("merchantProfit", null);
			maps.put("activityCode", null);
			maps.put("orderNo", null);
			maps.put("createTime", null);
			data.add(maps);
		} else {
			for (CloudPayInfo cpi : list) {
				if ("5".equals(cpi.getActivityCode())) {
					cpi.setActivityCode("云闪付");
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", null == cpi.getId() ? "" : cpi.getId() + "");
				maps.put("merchantName", null == cpi.getMerchantName() ? "" : cpi.getMerchantName());
				maps.put("merchantNo", null == cpi.getMerchantNo() ? "" : cpi.getMerchantNo());
				maps.put("mobilephone", null == cpi.getMobilephone() ? "" : cpi.getMobilephone());
				maps.put("agentName", null == cpi.getAgentName() ? "" : cpi.getAgentName());
				maps.put("oneAgentName", null == cpi.getOneAgentName() ? "" : cpi.getOneAgentName());
				maps.put("transAmount", null == cpi.getTransAmount() ? "" : cpi.getTransAmount().toString());
				maps.put("transFee", null == cpi.getTransFee() ? "" : cpi.getTransFee().toString());
				maps.put("discountFee", null == cpi.getDiscountFee() ? "" : cpi.getDiscountFee().toString());
				maps.put("merchantProfit", null == cpi.getMerchantProfit() ? "" : cpi.getMerchantProfit().toString());
				maps.put("activityCode", null == cpi.getActivityCode() ? "" : cpi.getActivityCode());
				maps.put("orderNo", null == cpi.getOrderNo() ? "" : cpi.getOrderNo());
				maps.put("createTime", null == cpi.getCreateTime() ? "" : sdf1.format(cpi.getCreateTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "id", "merchantName", "merchantNo", "mobilephone", "agentName", "oneAgentName",
				"transAmount", "transFee", "discountFee", "merchantProfit", "activityCode", "orderNo", "createTime"};
		String[] colsName = new String[] { "编号", "商户名称", "商户编号", "商户手机号", "直属代理商", "一级代理商",
				"交易金额(元)", "交易手续费(元)", "计算优惠后手续费(元)","收益金额(元)", "活动类型", "交易订单", "时间" };
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, response.getOutputStream());
		ouputStream.close();
	}

}
