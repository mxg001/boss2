package cn.eeepay.boss.action.credit;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmTaskInfo;
import cn.eeepay.framework.service.CmTaskService;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSON;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 提额任务
 * @author	mays
 * @date	2018年5月7日
 */
@Controller
@RequestMapping(value = "/cmTask")
public class CmTaskAction {
	private static final Logger log = LoggerFactory.getLogger(CmTaskAction.class);

	@Resource
	public CmTaskService cmTaskService;

	/**
	 * 查询limit_add_bank_strategy里的所有银行名称
	 * @author	mays
	 * @date	2018年5月7日
	 */
	@RequestMapping(value = "/selectBankName")
	@ResponseBody
	public Map<String, Object> selectBankName() {
		Map<String, Object> msg = new HashMap<>();
		try {
			List<Map<String,String>> list = cmTaskService.selectBankName();
			msg.put("status", true);
			msg.put("bankNames", list);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询银行名称失败");
			log.error("信用卡管家-查询银行名称失败", e);
		}
		return msg;
	}

	/**
	 * 提额任务查询
	 * @author	mays
	 * @date	2018年5月7日
	 */
	@RequestMapping(value = "/selectTaskInfo")
	@ResponseBody
	public Map<String, Object> selectTaskInfo(@RequestBody CmTaskInfo info, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmTaskInfo> page = new Page<>(pageNo, pageSize);
			cmTaskService.selectTaskInfo(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-提额任务查询失败", e);
		}
		return msg;
	}

	/**
	 * 提额任务详情
	 * @author	mays
	 * @date	2018年5月8日
	 */
	@RequestMapping(value = "/queryTaskDetail")
	@ResponseBody
	public Map<String, Object> queryTaskDetail(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			cmTaskService.queryTaskDetail(msg, id);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-提额任务详情查询失败", e);
		}
		return msg;
	}

	/**
	 * 信用卡管家提额任务-导出
	 * @param info
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportTaskInfo")
	@ResponseBody
	public void exportTaskInfo(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CmTaskInfo bean = JSON.parseObject(info, CmTaskInfo.class);
		List<CmTaskInfo> list = cmTaskService.exportTaskInfo(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "信用卡管家提额任务" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("cardNo", null);
			maps.put("bankName", null);
			maps.put("userNo", null);
			maps.put("userName", null);
			maps.put("cardHealth", null);
			maps.put("increasePossible", null);
			maps.put("taskHaveComplete", null);
			maps.put("time", null);
			data.add(maps);
		} else {
			for (CmTaskInfo i : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", null == i.getId() ? "" : i.getId());
				maps.put("cardNo", null == i.getCardNo() ? "" : i.getCardNo());
				maps.put("bankName", null == i.getBankName() ? "" : i.getBankName());
				maps.put("userNo", null == i.getUserNo() ? "" : i.getUserNo());
				maps.put("userName", null == i.getUserName() ? "" : i.getUserName());
				maps.put("cardHealth", null == i.getCardHealth() ? "" : i.getCardHealth()+"分");
				maps.put("increasePossible", null == i.getIncreasePossible() ? "" : i.getIncreasePossible()+"%");
				maps.put("taskHaveComplete", null == i.getTaskHaveComplete() ? "" : i.getTaskHaveComplete()+"%");
				maps.put("time", (null == i.getStartTime() ? "" : i.getStartTime())+"-"+(null == i.getEndTime() ? "" : i.getEndTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = { "id", "cardNo", "bankName", "userNo", "userName", "cardHealth",
				"increasePossible", "taskHaveComplete", "time"};
		String[] colsName = { "提额任务ID", "银行卡号", "银行名称", "用户ID", "姓名", "卡健康度",
				"提额指数", "计划完成度", "提额任务时间"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

}
