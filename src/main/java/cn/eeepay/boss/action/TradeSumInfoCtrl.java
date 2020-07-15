package cn.eeepay.boss.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.TradeSumInfo;
import cn.eeepay.framework.model.TradeSumInfoQo;
import cn.eeepay.framework.model.three.TeamInfoEntry;
import cn.eeepay.framework.model.three.ThreeSumVo;
import cn.eeepay.framework.service.impl.TradeSumInfoService;
import cn.eeepay.framework.service.unTransactionalImpl.job.TradeSumHistoryJob;
import cn.eeepay.framework.service.unTransactionalImpl.job.TradeSumJob;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;

@RestController
public class TradeSumInfoCtrl {

	private static final Logger log = LoggerFactory.getLogger(TradeSumInfoCtrl.class);

	@Autowired
	private TradeSumInfoService tradeSumInfoService;

	@Autowired
	private TradeSumHistoryJob tradeSumHistoryJob;
	
	@Autowired
	private TradeSumJob tradeSumJob;

	@RequestMapping("TradeSumInfo/query")
	public Page<TradeSumInfo> query(@RequestBody TradeSumInfoQo qo, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		Page<TradeSumInfo> page = new Page<>(pageNo, pageSize);
		try {
			// tradeSumJob.runTask("1");
			tradeSumInfoService.query(page, qo);
		} catch (Exception e) {
			log.error("三方交易量汇总查询异常", e);
		}
		return page;
	}

	@RequestMapping("TradeSumInfo/recorded")
	@SystemLog(description = "三方收益入账", operCode = "TradeSumInfo.recorded")
	public Result recorded(String recordedTime) {
		Result result = new Result();
		try {
			tradeSumInfoService.recorded(recordedTime);
			result.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
	}

	@RequestMapping("TradeSumInfo/tradeSum")
	public ThreeSumVo tradeSum(@RequestBody TradeSumInfoQo qo) {
		return tradeSumInfoService.tradeSum(qo);
	}

	@RequestMapping("TradeSumInfo/selectConfigInfo")
	public List<Map<String, String>> selectConfigInfo() {
		return tradeSumInfoService.queryConfigInfo();
	}

	@RequestMapping("TradeSumInfo/export")
	public void export(String info, HttpServletResponse response) {
		OutputStream ouputStream = null;
		try {
			TradeSumInfoQo qo = JSON.parseObject(info, TradeSumInfoQo.class);
			List<TradeSumInfo> tradeSumInfoList = tradeSumInfoService.list(qo);
			String fileName = "交易汇总报表" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xls";
			String fileNameFormat;
			fileNameFormat = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);

			List<Map<String, String>> data = new ArrayList<>();
			for (TradeSumInfo sumInfo : tradeSumInfoList) {
				Map<String, String> map = new HashMap<>();
				map.put("createTime", DateUtil.getFormatDate("yyyy-MM-dd", sumInfo.getCreateTime()));

				map.put("branch", sumInfo.getBranch());
				map.put("oneLevel", sumInfo.getOneLevel());
				map.put("twoLevel", sumInfo.getTwoLevel());
				map.put("threeLevel", sumInfo.getThreeLevel());
				map.put("fourLevel", sumInfo.getFourLevel());
				map.put("fiveLevel", sumInfo.getFiveLevel());
				map.put("tradeSum", sumInfo.getTradeSum() == null ? "" : sumInfo.getTradeSum().toString());
				map.put("merSum", sumInfo.getMerSum() == null ? "" : Integer.toString(sumInfo.getMerSum()));
				map.put("activateSum",
						sumInfo.getActivateSum() == null ? "" : Integer.toString(sumInfo.getActivateSum()));
				map.put("machinesStock",
						sumInfo.getMachinesStock() == null ? "" : Integer.toString(sumInfo.getMachinesStock()));
				map.put("unusedMachines",
						sumInfo.getUnusedMachines() == null ? "" : Integer.toString(sumInfo.getUnusedMachines()));
				map.put("expiredNotActivated", sumInfo.getExpiredNotActivated() == null ? ""
						: Integer.toString(sumInfo.getExpiredNotActivated()));
				map.put("threeIncome", sumInfo.getThreeIncome() == null ? "" : sumInfo.getThreeIncome().toString());
				if (sumInfo.getRecordedStatus() == null) {
					map.put("recordedStatus", "");
				} else {
					map.put("recordedStatus", sumInfo.getRecordedStatus() == 0 ? "未入账" : "已入账");
				}
				map.put("recordedDate", sumInfo.getRecordedDate() == null ? ""
						: DateUtil.getFormatDate(DateUtil.LONG_FROMATE, sumInfo.getRecordedDate()));
				data.add(map);
			}

			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[] { "createTime", "branch", "oneLevel", "twoLevel", "threeLevel", "fourLevel",
					"fiveLevel", "tradeSum", "merSum", "activateSum", "machinesStock", "unusedMachines",
					"expiredNotActivated", "threeIncome", "recordedStatus", "recordedDate" };
			String[] colsName = new String[] { "统计日期", "机构", "三方1级", "三方2级", "三方3级", "三方4级", "三方5级", "交易量(元)", "商户总数",
					"激活总数", "机具库存", "未使用机具", "到期未激活机具", "三方收益", "入账状态", "入账时间" };
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, ouputStream);
		} catch (Exception e) {
			log.error("导出异常",e);
		}finally {
			try {
				if (ouputStream != null) {
					ouputStream.close();
				}
			} catch (IOException e) {
				log.error("关闭流异常",e);
			}
		}
	}

	@RequestMapping("TradeSumInfo/getTeamEntryIdList")
	public Result getTeamEntryIdList(String merTeamId) {
		Result result = new Result();
		try {
			List<TeamInfoEntry> list = tradeSumInfoService.listByTeamId(merTeamId);
			result.setData(list);
			result.setStatus(true);
		} catch (Exception e) {
			log.error("查询异常", e);
			result.setStatus(false);
		}
		return result;
	}

}
