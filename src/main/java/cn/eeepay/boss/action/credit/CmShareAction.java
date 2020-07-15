package cn.eeepay.boss.action.credit;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmShare;
import cn.eeepay.framework.service.CmShareService;
import cn.eeepay.framework.util.ListDataExcelExport;

@Controller
@RequestMapping(value = "/cmShare")
public class CmShareAction {
	private static final Logger log = LoggerFactory.getLogger(CmShareAction.class);

	@Resource
	public CmShareService cmShareService;

	/**
	 * 分润查询
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/selectShareInfo")
	@ResponseBody
	public Map<String, Object> selectShareInfo(@RequestBody CmShare info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmShare> page = new Page<>(pageNo, pageSize);
			cmShareService.selectShareInfo(page, info);
			msg.put("page", page);
			msg.put("sumAmount", cmShareService.sumShareInfo(info));
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-分润查询失败", e);
		}
		return msg;
	}

	/**
	 * 信用卡管家-分润查询-导出
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/exportShareInfo")
	@ResponseBody
	public void exportShareInfo(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CmShare bean = JSON.parseObject(info, CmShare.class);
		List<CmShare> list = cmShareService.exportShareInfo(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "信用卡管家分润信息" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("createDate", null);
			maps.put("shareCash", null);
			maps.put("sharePercentage", null);
			maps.put("enterStatus", null);
			maps.put("shareAgentName", null);
			maps.put("shareAgentNo", null);
			maps.put("relatedOrderNo", null);
			maps.put("orderCash", null);
			maps.put("orderType", null);
			maps.put("userId", null);
			maps.put("belongAgentNo", null);
			maps.put("belongAgentName", null);
			maps.put("enterDate", null);
			data.add(maps);
		} else {
			for (CmShare i : list) {
				String orderType = "";
				if (i.getOrderType() == null) {
				} else if (i.getOrderType() == 1) {
					orderType = "会员服务费";
				} else if (i.getOrderType() == 2) {
					orderType = "其他";
				}
				String enterStatus = "";
				if (i.getEnterStatus() == null) {
				} else if (i.getEnterStatus() == 1) {
					enterStatus = "未入账";
				} else if (i.getEnterStatus() == 2) {
					enterStatus = "已入账";
				} else if (i.getEnterStatus() == 3) {
					enterStatus = "入账失败";
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("createDate", null == i.getCreateDate() ? "" : sdf1.format(i.getCreateDate()));
				maps.put("shareCash", null == i.getShareCash() ? "" : i.getShareCash().toString());
				maps.put("sharePercentage", null == i.getSharePercentage() ? "" : i.getSharePercentage() + "%");
				maps.put("enterStatus", orderType);
				maps.put("shareAgentName", null == i.getShareAgentName() ? "" : i.getShareAgentName());
				maps.put("shareAgentNo", null == i.getShareAgentNo() ? "" : i.getShareAgentNo());
				maps.put("relatedOrderNo", null == i.getRelatedOrderNo() ? "" : i.getRelatedOrderNo());
				maps.put("orderCash", null == i.getOrderCash() ? "" : i.getOrderCash().toString());
				maps.put("orderType", enterStatus);
				maps.put("userId", null == i.getUserId() ? "" : i.getUserId());
				maps.put("belongAgentNo", null == i.getBelongAgentNo() ? "" : i.getBelongAgentNo());
				maps.put("belongAgentName", null == i.getBelongAgentName() ? "" : i.getBelongAgentName());
				maps.put("enterDate", null == i.getEnterDate() ? "" : sdf1.format(i.getEnterDate()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = {"createDate", "shareCash", "sharePercentage", "enterStatus", "shareAgentName", "shareAgentNo",
				"relatedOrderNo", "orderCash", "orderType", "userId", "belongAgentNo", "belongAgentName", "enterDate"};
		String[] colsName = {"分润创建时间", "分润金额", "分润百分比", "入账状态", "分润代理商名称", "分润代理商编号",
				"关联订单号", "订单金额", "订单类型", "用户ID", "所属代理商编号", "所属代理商名称", "入账时间"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

}
