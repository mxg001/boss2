package cn.eeepay.boss.action.credit;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmVipConfig;
import cn.eeepay.framework.model.CmVipConfigAgent;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.CmSettingService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/cmSetting")
public class CmSettingAction {
	private static final Logger log = LoggerFactory.getLogger(CmSettingAction.class);

	@Resource
	public CmSettingService cmSettingService;

	@Resource
	public AgentInfoService agentInfoService;

	/**
	 * 根据type查询会员配置信息
	 * @author	mays
	 * @date	2018年5月19日
	 */
	@RequestMapping(value = "/selectVipConfigByType")
	@ResponseBody
	public Map<String, Object> selectVipConfigByType() {
		Map<String, Object> msg = new HashMap<>();
		try {
			CmVipConfig info = cmSettingService.selectVipConfigByType(1);//目前只有月付费,所以写死1
			msg.put("status", true);
			msg.put("info", info);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-查询会员配置信息失败", e);
		}
		return msg;
	}

	/**
	 * 修改会员配置信息
	 * @author	mays
	 * @date	2018年5月19日
	 */
	@RequestMapping(value = "/updateVipConfig")
	@ResponseBody
	@SystemLog(description = "修改会员配置",operCode="creditMgr.cmSetting")
	public Map<String, Object> updateVipConfig(@RequestBody CmVipConfig info) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if(info.getVipCharge()==null||info.getAgentComfig()==null||info.getCardLimit()==null){
				msg.put("msg", "参数错误");
				msg.put("status", false);
				return msg;
			}
			if(info.getCardLimit()==1){
				if(info.getCardLimitNum()==null){
					msg.put("msg", "数据不能为空");
					msg.put("status", false);
					return msg;
				}else if(info.getCardLimitNum()<1||info.getCardLimitNum()>20){
					msg.put("msg", "添卡数量上限必须大于0，小于20的整数");
					msg.put("status", false);
					return msg;
				}
			}
			if (info.getVipFee() == null || info.getValidPeriod() == null || info.getAgentShare() == null) {
				msg.put("msg", "数据不能为空");
				msg.put("status", false);
				return msg;
			}
			if (info.getVipFee().compareTo(new BigDecimal(0)) != 1 || info.getVipFee().compareTo(new BigDecimal(360)) == 1) {
				msg.put("msg", "会员服务费必须为正数,且大于0小于等于360");
				msg.put("status", false);
				return msg;
			}
			if (info.getValidPeriod() < 1 || info.getValidPeriod()>360) {
				msg.put("msg", "有效期必须为正整数,且大于0小于等于360");
				msg.put("status", false);
				return msg;
			}
			if (info.getAgentShare().compareTo(new BigDecimal(0)) == -1 || info.getAgentShare().compareTo(new BigDecimal(100))==1) {
				msg.put("msg", "代理商分润必须为正数,且大于0小于等于100");
				msg.put("status", false);
				return msg;
			}
			info.setType(1);//目前只有月付费,所以写死1
			if (1 == cmSettingService.updateVipConfig(info)) {
				msg.put("msg", "修改成功");
				msg.put("status", true);
			} else {
				msg.put("msg", "修改失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "修改失败");
			log.error("信用卡管家-修改会员配置信息失败", e);
		}
		return msg;
	}

	/**
	 * 组织会员费设置查询
	 * @author	yyao
	 * @date	2018年6月15日
	 */
	@RequestMapping(value = "/selectVipConfigAgent")
	@ResponseBody
	public Map<String, Object> selectVipConfigAgent(@RequestParam("info") String param, @ModelAttribute("page") Page<CmVipConfigAgent> page) {
		Map<String, Object> msg = new HashMap<>();
		try {
			CmVipConfigAgent cmVipConfigAgent = JSONObject.parseObject(param, CmVipConfigAgent.class);
			cmSettingService.selectVipConfigAgent(page, cmVipConfigAgent);
			msg.put("page", page);
			msg.put("numCount", cmSettingService.selectVipConfigAgentCount(cmVipConfigAgent));
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-组织会员费设置查询", e);
		}
		return msg;
	}

	@RequestMapping(value = "/selectVipConfigAgentByAgentNo")
	@ResponseBody
	public Map<String, Object> selectVipConfigAgentByAgentNo(@RequestBody String agentNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			CmVipConfigAgent param=cmSettingService.selectVipConfigAgentByAgentNo(agentNo);
			msg.put("param", param);
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			log.error("查询失败", e);
		}
		return msg;
	}

	/**
	 * 组织会员费设置新增
	 * @param cmVipConfigAgent
	 * @return
	 */
	@RequestMapping(value = "/saveCmSettingAgent")
	@ResponseBody
	public Map<String, Object> saveCmSettingAgent(@RequestBody CmVipConfigAgent cmVipConfigAgent) {
		Map<String, Object> msg = new HashMap<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(cmVipConfigAgent.getAgentNo()==null||cmVipConfigAgent.getAgentName()==null
					||cmVipConfigAgent.getAgentLevel()==null||cmVipConfigAgent.getSrcOrgPrduct()==null
					||cmVipConfigAgent.getSrcOrgId()==null||cmVipConfigAgent.getVipFee()==null
					||cmVipConfigAgent.getValidPeriod()==null||cmVipConfigAgent.getAgentShare()==null){
				msg.put("msg", "参数错误");
				msg.put("status", false);
				return msg;
			}
			if(cmSettingService.selectVipConfigAgentByAgentNoCount(cmVipConfigAgent.getAgentNo())>0){
				msg.put("msg", "不可以重复添加代理商");
				msg.put("status", false);
				return msg;
			}
			Map<String, Object> agentMap= agentInfoService.selectAgentTeamByAgentNo(cmVipConfigAgent.getAgentNo());
			if(agentMap==null){
				msg.put("msg", "代理商不存在");
				msg.put("status", false);
				return msg;
			}else if(!"1".equals(agentMap.get("agent_level").toString())){
				msg.put("msg", "代理商必须为一级代理商");
				msg.put("status", false);
				return msg;
			}
			if (cmVipConfigAgent.getVipFee().compareTo(new BigDecimal(0)) != 1 || cmVipConfigAgent.getVipFee().compareTo(new BigDecimal(360)) == 1) {
				msg.put("msg", "会员服务费必须为正数,且大于0小于等于360");
				msg.put("status", false);
				return msg;
			}
			if (cmVipConfigAgent.getValidPeriod() < 1 || cmVipConfigAgent.getValidPeriod()>360) {
				msg.put("msg", "有效期必须为正整数,且大于0小于等于360");
				msg.put("status", false);
				return msg;
			}
			if (cmVipConfigAgent.getAgentShare().compareTo(new BigDecimal(0)) == -1 || cmVipConfigAgent.getAgentShare().compareTo(new BigDecimal(100))==1) {
				msg.put("msg", "代理商分润必须为正数,且大于0小于等于100");
				msg.put("status", false);
				return msg;
			}
			cmVipConfigAgent.setOperator(principal.getUsername());
			if(cmSettingService.saveCmSettingAgent(cmVipConfigAgent)>0){
				msg.put("msg", "新增成功");
				msg.put("status", true);
			}else{
				msg.put("msg", "新增失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "新增失败");
			log.error("信用卡管家-组织会员费设置新增", e);
		}
		return msg;
	}

	/**
	 * 组织会员费设置新增
	 * @param cmVipConfigAgent
	 * @return
	 */
	@RequestMapping(value = "/editCmSettingAgent")
	@ResponseBody
	public Map<String, Object> editCmSettingAgent(@RequestBody CmVipConfigAgent cmVipConfigAgent) {
		Map<String, Object> msg = new HashMap<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(cmVipConfigAgent.getAgentNo()==null||cmVipConfigAgent.getVipFee()==null
					||cmVipConfigAgent.getValidPeriod()==null||cmVipConfigAgent.getAgentShare()==null){
				msg.put("msg", "参数错误");
				msg.put("status", false);
				return msg;
			}
			if (cmVipConfigAgent.getVipFee().compareTo(new BigDecimal(0)) != 1 || cmVipConfigAgent.getVipFee().compareTo(new BigDecimal(360)) == 1) {
				msg.put("msg", "会员服务费必须为正数,且大于0小于等于360");
				msg.put("status", false);
				return msg;
			}
			if (cmVipConfigAgent.getValidPeriod() < 1 || cmVipConfigAgent.getValidPeriod()>360) {
				msg.put("msg", "有效期必须为正整数,且大于0小于等于360");
				msg.put("status", false);
				return msg;
			}
			if (cmVipConfigAgent.getAgentShare().compareTo(new BigDecimal(0)) == -1 || cmVipConfigAgent.getAgentShare().compareTo(new BigDecimal(100))==1) {
				msg.put("msg", "代理商分润必须为正数,且大于0小于等于100");
				msg.put("status", false);
				return msg;
			}
			cmVipConfigAgent.setOperator(principal.getUsername());
			if(cmSettingService.updateCmSettingAgent(cmVipConfigAgent)>0){
				msg.put("msg", "修改成功");
				msg.put("status", true);
			}else{
				msg.put("msg", "修改失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "修改失败");
			log.error("信用卡管家-组织会员费设置修改", e);
		}
		return msg;
	}


	@RequestMapping(value = "/deleteCmSettingByAgentNo")
	@ResponseBody
	public Map<String, Object> deleteCmSettingByAgentNo(@RequestParam("agentNo") String agentNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if(cmSettingService.deleteCmSettingAgentByAgentNo(agentNo)>0){
				msg.put("msg", "删除成功");
				msg.put("status", true);
			}else{
				msg.put("msg", "删除失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "删除失败");
			log.error("信用卡管家-组织会员费设置删除", e);
		}
		return msg;
	}

	@RequestMapping(value = "/deleteCmSettingInfo")
	@ResponseBody
	public Object deleteCmSettingInfo(@RequestBody String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			List<CmVipConfigAgent> cmVipConfigAgent = JSON.parseArray(param, CmVipConfigAgent.class);
			int num = 0;
			for (CmVipConfigAgent c : cmVipConfigAgent) {
				num += cmSettingService.deleteCmSettingAgentByAgentNo(c.getAgentNo());
			}
			if (num > 0) {
				jsonMap.put("msg", "操作成功");
				jsonMap.put("status", true);
			} else {
				jsonMap.put("status", false);
				jsonMap.put("msg", "操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("msg", "操作失败");
			jsonMap.put("status", false);
		}
		return jsonMap;
	}

	// 根据操作中的"新增""删除"来执行批量添加,批量删除
	@RequestMapping(value = "/addButchCmSettingAgent")
	public @ResponseBody Object addButchCmSettingAgent(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int errorCount=0;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			if (!file.isEmpty()) {
				String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if (!format.equals(".xls") && !format.equals(".xlsx")) {
					jsonMap.put("status", false);
					jsonMap.put("msg", "文件格式错误");
					return jsonMap;
				}
				Workbook wb = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = wb.getSheetAt(0);
				// 遍历所有单元格，读取单元格
				int row_num = sheet.getLastRowNum();
				if (row_num < 1) {
					jsonMap.put("status", false);
					jsonMap.put("msg", "文件内容错误");
					return jsonMap;
				}
				List<CmVipConfigAgent> errorlist = new ArrayList<CmVipConfigAgent>();
				List<CmVipConfigAgent> list = new ArrayList<CmVipConfigAgent>();
				for (int i = 1; i <= row_num; i++) {
					Row row = sheet.getRow(i);
					String num1 = getCellValue(row.getCell(0));
					String num2 = getCellValue(row.getCell(1));
					String num3 = getCellValue(row.getCell(2));
					String num4 = getCellValue(row.getCell(3));
					String num5 = getCellValue(row.getCell(4));
					String num6 = getCellValue(row.getCell(5));
					String num7 = getCellValue(row.getCell(6));
					String num8 = getCellValue(row.getCell(7));
					String num9 = getCellValue(row.getCell(8));

					if (StringUtils.isBlank(num1))
						break;
					String agentNo = num1;
					String agentName = num2;
					String agentLevel = num3;
					String srcOrgPrduct = num4;
					String srcOrgId = num5;
					String vipFee = num6;
					String validPeriod = num7;
					String agentShare = num8;
					String handle = num9;//操作:新增,删除

					Map<String, Object> agentMap= agentInfoService.selectAgentTeamByAgentNo(agentNo);
					CmVipConfigAgent cmVipConfigAgent = new CmVipConfigAgent();
					cmVipConfigAgent.setAgentNo(agentNo);
					cmVipConfigAgent.setAgentName(agentName);
					cmVipConfigAgent.setAgentLevel(agentLevel);
					cmVipConfigAgent.setSrcOrgPrduct(srcOrgPrduct);
					cmVipConfigAgent.setSrcOrgId(srcOrgId);
					cmVipConfigAgent.setVipFee(new BigDecimal(vipFee));
					cmVipConfigAgent.setValidPeriod(Integer.parseInt(validPeriod));
					cmVipConfigAgent.setAgentShare(new BigDecimal(agentShare));
					cmVipConfigAgent.setOperator(principal.getUsername());
					cmVipConfigAgent.setHandle(handle);
					if("新增".equals(handle)){
						if(agentMap==null||!"1".equals(agentMap.get("agent_level").toString())){
							errorCount++;
							cmVipConfigAgent.setErrorResult("代理商不存在或者不是一级代理商");
							errorlist.add(cmVipConfigAgent);
							continue;
						}
						if(vipFee==null||"".equals(vipFee)){
							errorCount++;
							cmVipConfigAgent.setErrorResult("会员服务费不能为空");
							errorlist.add(cmVipConfigAgent);
							continue;
						}
						if(validPeriod==null||"".equals(validPeriod)){
							errorCount++;
							cmVipConfigAgent.setErrorResult("会员有效期不能为空");
							errorlist.add(cmVipConfigAgent);
							continue;
						}
						if(agentShare==null||"".equals(agentShare)){
							errorCount++;
							cmVipConfigAgent.setErrorResult("代理商分润比不能为空");
							errorlist.add(cmVipConfigAgent);
							continue;
						}
					}
					if(agentMap!=null&&"1".equals(agentMap.get("agent_level").toString())){
						cmVipConfigAgent.setAgentName(agentMap.get("agent_name").toString());
						cmVipConfigAgent.setAgentLevel(agentMap.get("agent_level").toString());
						cmVipConfigAgent.setSrcOrgPrduct(agentMap.get("team_id").toString());
						cmVipConfigAgent.setSrcOrgId(agentMap.get("team_name").toString());
						cmSettingService.deleteCmSettingAgentByAgentNo(agentNo);
						if("新增".equals(handle)){
							list.add(cmVipConfigAgent);
						}else if("删除".equals(handle)){
							cmSettingService.deleteCmSettingAgentByAgentNo(cmVipConfigAgent.getAgentNo());
						}
					}
				}

				if(list.size()>0){
					cmSettingService.addButchCmSettingAgent(list);
				}
				request.getSession().setAttribute("errorCmSettingAgent"+principal.getRealName(),errorlist);
				jsonMap.put("errorCount", errorCount);
				jsonMap.put("status", true);
				jsonMap.put("msg", "操作成功");
				return jsonMap;
			} else {
				jsonMap.put("status", false);
				jsonMap.put("msg", "文件格式错误");
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("status", false);
			jsonMap.put("msg", "数据异常");
		}
		return jsonMap;
	}

	/**
	 * 批量新增模板
	 */
	@RequestMapping("/downloadTemplate")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getServletContext().getRealPath("/")+File.separator+"template"+File.separator+"cmVipConfigAgentTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath,"批量新增模板.xlsx");
		return null;
	}

	@RequestMapping("/downloadErrorDate")
	public String downloadErrorDate(HttpServletRequest request, HttpServletResponse response)throws Exception{
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<CmVipConfigAgent> errorList =  (List<CmVipConfigAgent>) request.getSession().getAttribute("errorCmSettingAgent"+principal.getRealName());
		if(errorList!=null && errorList.size()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String fileName = "批量添加【会员和分润设置】-异常数据" + sdf.format(new Date()) + ".xlsx";
			String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			for(CmVipConfigAgent a:errorList){
				Map<String, String> map=new HashMap<String, String>();
				map.put("agentNo",a.getAgentNo());
				map.put("agentName", a.getAgentName());
				map.put("agentLevel", a.getAgentLevel());
				map.put("srcOrgPrduct", a.getSrcOrgPrduct());
				map.put("srcOrgId", a.getSrcOrgId());
				map.put("vipFee", a.getVipFee().toString());
				map.put("validPeriod", a.getValidPeriod().toString());
				map.put("agentShare", a.getAgentShare().toString());
				map.put("handle", a.getHandle());
				map.put("errorResult", a.getErrorResult());
				data.add(map);
			}
			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[] { "agentNo", "agentName", "agentLevel", "srcOrgPrduct", "srcOrgId", "vipFee",
					"validPeriod", "agentShare", "handle", "errorResult"};
			String[] colsName = new String[] { "代理商编号", "代理商名称", "代理商级别", "所属组织", "组织ID", "会员服务费",
					"会员有效期", "代理商分润比", "操作","错误信息"};
			OutputStream ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
			ouputStream.close();
		}
		request.getSession().removeAttribute("errorCmSettingAgent"+principal.getRealName());
		return null;
	}

	public String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				return String.valueOf(cell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_FORMULA:
				return cell.getStringCellValue();
		}
		return null;
	}
}
