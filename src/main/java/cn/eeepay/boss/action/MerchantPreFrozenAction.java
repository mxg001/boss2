package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TransInfoFreezeQueryCollection;
import cn.eeepay.framework.service.TransInfoPreFrozenService;
import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/merchantPreFrozenAction")
public class MerchantPreFrozenAction {
	private static final Logger log = LoggerFactory.getLogger(MerchantPreFrozenAction.class);
	@Resource
	private TransInfoPreFrozenService transInfoPreFrozenService;

	@Resource
	private TransInfoService transInfoService;

	//商户冻结查询初始化
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfo.do")
	@ResponseBody
	public Object getAllInfo(@ModelAttribute("page") Page<TransInfoFreezeQueryCollection> page,
							 @RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			TransInfoFreezeQueryCollection tis = JSON.parseObject(param, TransInfoFreezeQueryCollection.class);
			if (tis.getBool() == null || tis.getBool().equals("")) {
				tis.setBool("1");
			}
			//查询条件中商户编号、手机号、操作时间必填一项，若仅填写操作时间需限制只能查三个月内数据，若填写了商户编号或手机号任一项，可以查全部数据。
			if((tis.getMerchantNo()==null||tis.getMerchantNo().equals(""))&&(tis.getMobilephone()==null||tis.getMobilephone().equals(""))) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar sCalendar = Calendar.getInstance();
				sCalendar.setTime(sdf.parse(tis.getsTime()));
				Calendar eCalendar = Calendar.getInstance();
				eCalendar.setTime(sdf.parse(tis.geteTime()));
				// 起始时间加三个月和结束时间比较
				sCalendar.add(Calendar.MONTH, 3);
				if (sCalendar.compareTo(eCalendar) == -1) {
					map.put("bols", false);
					map.put("msg", "查询时间跨度不能超过3个月");
					return map;
				}
			}
			transInfoPreFrozenService.queryAllInfo(tis, page);
			//由于获取的预冻结金额是通过操作日志取的，所以要去解析，重新封装page
			if(tis.getQueryMode().equals("0")){
				List<TransInfoFreezeQueryCollection> rawResult =  page.getResult();
				for(TransInfoFreezeQueryCollection aa:rawResult){
					String operStr = aa.getOperStr();
					String [] operStrArray =operStr.split("从");
					String [] operStrArray2 =operStrArray[1].split("更改为");
					BigDecimal operMoneyFirst=new BigDecimal(operStrArray2[0]);
					BigDecimal operMoneySecond=new BigDecimal(operStrArray2[1]);
					aa.setOperMoney(operMoneySecond.subtract(operMoneyFirst).abs());
				}
				page.setResult(rawResult);
			}
			map.put("page", page);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/exportPreFrozenInfo")
	@ResponseBody
	public void exportInfo(@RequestParam("info") String info, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		TransInfoFreezeQueryCollection tis = JSON.parseObject(info, TransInfoFreezeQueryCollection.class);
		if (tis.getBool() == null || tis.getBool().equals("")) {
			tis.setBool("1");
		}
		List<TransInfoFreezeQueryCollection> list = transInfoPreFrozenService.importAllInfo(tis);
		//由于获取的预冻结金额是通过操作日志取的，所以要去解析，重新封装list
		if(tis.getQueryMode().equals("0")){
			for(TransInfoFreezeQueryCollection aa:list){
				String operStr = aa.getOperStr();
				String [] operStrArray =operStr.split("从");
				String [] operStrArray2 =operStrArray[1].split("更改为");
				BigDecimal operMoneyFirst=new BigDecimal(operStrArray2[0]);
				BigDecimal operMoneySecond=new BigDecimal(operStrArray2[1]);
				aa.setOperMoney(operMoneySecond.subtract(operMoneyFirst).abs());
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "商户冻结记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("merchantNo", null);
			maps.put("merchantName", null);
			maps.put("mobilephone", null);
			maps.put("operMoney", null);
			maps.put("operTime", null);
			maps.put("operName", null);
			maps.put("operType", null);
			maps.put("operLog", null);
			maps.put("orderNo", null);
			maps.put("operReason", null);
			maps.put("merStatus", null);
			maps.put("riskStatus", null);
			data.add(maps);
		} else {
			for (TransInfoFreezeQueryCollection collectiveTransOrder : list) {
				Map<String, String> maps = new HashMap<String, String>();
				// 操作类型
				String operType = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getOperType())) {
					if ("0".equals(collectiveTransOrder.getOperType())) {
						operType = "冻结";
					} else if("1".equals(collectiveTransOrder.getOperType())){
						operType = "解冻";
					}else if("2".equals(collectiveTransOrder.getOperType())){
						operType = "预冻结";
					}
					/*else if("3".equals(collectiveTransOrder.getOperType())){
						operType = "冻结/解冻";
					}*/
				}
				// 商户状态
				String merStatus = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getMerStatus())) {
					if ("0".equals(collectiveTransOrder.getMerStatus())) {
						merStatus = "关闭";
					} else if ("1".equals(collectiveTransOrder.getMerStatus())) {
						merStatus = "正常";
					} else if ("2".equals(collectiveTransOrder.getMerStatus())) {
						merStatus = "冻结";
					}
				}
				//商户冻结状态
				String riskStatus = "";
				if (StringUtils.isNotBlank(collectiveTransOrder.getRiskStatus())) {
					if ("3".equals(collectiveTransOrder.getRiskStatus())) {
						riskStatus = "不进不出";
					} else if ("1".equals(collectiveTransOrder.getRiskStatus())) {
						riskStatus = "正常";
					} else if ("2".equals(collectiveTransOrder.getRiskStatus())) {
						riskStatus = "只进不出";
					}
				}
				maps.put("merchantNo", collectiveTransOrder.getMerchantNo());
				maps.put("merchantName",null==collectiveTransOrder.getMerchantName()? "" : collectiveTransOrder.getMerchantName());
				maps.put("mobilephone", null==collectiveTransOrder.getMobilephone()? "" : collectiveTransOrder.getMobilephone());
				maps.put("operMoney",null == collectiveTransOrder.getOperMoney() ? "" : collectiveTransOrder.getOperMoney().toString());
				maps.put("operTime",null==collectiveTransOrder.getOperTime()?"":sdf1.format(collectiveTransOrder.getOperTime()));
				maps.put("operName", null == collectiveTransOrder.getOperName() ? "": collectiveTransOrder.getOperName());
				maps.put("operType",operType);
				maps.put("operLog", null == collectiveTransOrder.getOperLog() ? "": collectiveTransOrder.getOperLog());
				maps.put("orderNo", null == collectiveTransOrder.getOrderNo() ? "": collectiveTransOrder.getOrderNo());
				maps.put("operReason", null == collectiveTransOrder.getOperReason() ? "": collectiveTransOrder.getOperReason());
				maps.put("merStatus", merStatus);
				maps.put("riskStatus",riskStatus);
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "merchantNo", "merchantName", "mobilephone",
				"operMoney","operTime","operName","operType", "operLog", "orderNo",
				"operReason","merStatus","riskStatus"};
		String[] colsName = new String[] { "商户编号", "商户名称", "手机号",
				"操作金额", "操作时间", "操作人", "操作类型", "操作日志", "商户订单号",
				"备注/原因","商户状态","商户冻结状态" };
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}
}
