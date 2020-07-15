package cn.eeepay.boss.action.capitalInsurance;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.capitalInsurance.BillEneryService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;

/**
 * 对账详情
 * @author ivan
 *
 */
@Controller
@RequestMapping(value = "/billEneryAction")
public class BillEneryAction {
	private static final Logger log = LoggerFactory.getLogger(BillEneryAction.class);
	@Autowired
	private BillEneryService billEneryService;
	@Autowired
	private SysDictService sysDictService;
	
	/**
	 * 
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getEntryAllInfo")
	@ResponseBody
	public Object getBillAllInfo(@ModelAttribute("page") Page<BillEntry> page,
			@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BillEntry tis = JSON.parseObject(param, BillEntry.class);
			billEneryService.queryAllInfo(tis, page);
			map.put("page", page);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/billEneryDetail")
	@ResponseBody
	public Object billEneryDetail(@RequestParam("id") String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BillEntry be = billEneryService.getEneryDetail(id);
			map.put("info", be);
			map.put("status", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("status", false);
			map.put("msg", "查询报错");
		}
		return map;
	}
	
	/**
	 * 
	 * @param info
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportTransInfo")
	@ResponseBody
	public void exportInfo(@RequestParam("info") String info, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		BillEntry tis = JSON.parseObject(info, BillEntry.class);
		List<BillEntry> list = billEneryService.importAllInfo(tis);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "对账详情" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> exlData = installExlData(list);
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "id","batchNo", "insurer","orderType","acqOrderNo","acqAmount",
				"holder","oneAgentNo","oneAgentName","sysOrderNo","sysBillNo","sysAmount","billAmount","insureStatus","insureTime","effectiveStime",
				"effectiveEtime","checkStatus","reportStatus",};
		String[] colsName = new String[] { "ID","对账批次号","上游渠道","订单类型","上游保险订单号","上游渠道含税保费",
                "被保险人","一级代理商编号","一级代理商名称","平台保险订单号","保单号","平台含税保费","平台售价","平台投保状态","投保时间","保险起期",
                "保险止期","对账状态","汇总状态"	
		};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, exlData, outputStream);
		outputStream.close();
	}
	/**
	 * 组装exl数据
	 * @param list 数据集
	 * @return
	 */
	private List<Map<String, String>>  installExlData(List<BillEntry> list){
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
            maps.put("batchNo",null);
            maps.put("insurer",null);
            maps.put("orderType",null);
            maps.put("acqOrderNo",null);
            maps.put("acqAmount",null);
            maps.put("holder",null);
			maps.put("oneAgentNo",null);
			maps.put("oneAgentName",null);
            maps.put("sysOrderNo",null);
            maps.put("sysBillNo",null);
            maps.put("sysAmount",null);
            maps.put("insureStatus",null);
            maps.put("insureTime",null);
            maps.put("effectiveStime",null);
            maps.put("effectiveEtime",null);
            maps.put("checkStatus",null);
            maps.put("reportStatus",null);
			data.add(maps);
		} else {
			Map<Integer,String> orderTypeList = new HashMap<Integer,String>();
			orderTypeList.put(1, "投保订单");
			orderTypeList.put(2, "退保订单");
			   
			Map<Integer,String> checkStatusList = new HashMap<Integer,String>();
			checkStatusList.put(1, "核对成功");
			checkStatusList.put(2, "上游单边");
			checkStatusList.put(3, "平台单边");
			checkStatusList.put(4, "金额不符");
			checkStatusList.put(5, "未核对");
			 
			Map<Integer,String> reportStatusList = new HashMap<Integer,String>();
			reportStatusList.put(1, "已汇总");
			reportStatusList.put(2, "未汇总");
			 
			Map<String, String> insurerStr = sysDictService.selectMapByKey("insurer_number");

			Map<String,String> insureStatusList = new HashMap<String,String>();
			insureStatusList.put("SUCCESS", "投保成功");
			insureStatusList.put("FAILED", "投保失败");
			insureStatusList.put("INIT", "初始化");
			insureStatusList.put("OVERLIMIT", "已退保");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (BillEntry  be : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id",StringUtil.filterNull(be.getId()));
	            maps.put("batchNo",be.getBatchNo());
	            maps.put("insurer",insurerStr.get(be.getInsurer()));
	            maps.put("orderType",orderTypeList.get(be.getOrderType()));
	            maps.put("acqOrderNo",be.getAcqOrderNo());
	            maps.put("acqAmount",be.getAcqAmount().toString());
	            maps.put("holder",be.getHolder());
				maps.put("oneAgentNo",be.getOneAgentNo());
				maps.put("oneAgentName",be.getOneAgentName());
	            maps.put("sysOrderNo",be.getSysOrderNo());
	            maps.put("sysBillNo",be.getSysBillNo());
	            maps.put("sysAmount",be.getSysAmount().toString());
	            maps.put("insureStatus",insureStatusList.get(be.getInsureStatus()));
	            maps.put("insureTime",sdf.format(be.getInsureTime()));
	            maps.put("effectiveStime",sdf.format(be.getEffectiveStime()));
	            maps.put("effectiveEtime",sdf.format(be.getEffectiveEtime()));
	            maps.put("checkStatus",checkStatusList.get(be.getCheckStatus()));
	            maps.put("reportStatus",reportStatusList.get(be.getReportStatus()));
				data.add(maps);
			}
		}
		return data;
	}
}
