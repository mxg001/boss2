package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.three.ImportAgentVo;
import cn.eeepay.framework.model.three.ImportReturnVo;
import cn.eeepay.framework.service.AgentFunctionManagerService;
import cn.eeepay.framework.service.TeamInfoService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import cn.hutool.core.util.IdUtil;
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
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/agentFunctionManager")
public class AgentFunctionManagerAction {

	@Resource
	private AgentFunctionManagerService agentFunctionManagerService;

	@Resource
	private TeamInfoService teamInfoService;

	private static final Logger log = LoggerFactory.getLogger(AgentFunctionManagerAction.class);

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByParam.do")
	@ResponseBody
	public Object selectByParam(@RequestParam("info") String param,
			@ModelAttribute("page") Page<AgentFunctionManager> page) throws Exception {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			AgentFunctionManager agentFunctionManager = JSONObject.parseObject(param, AgentFunctionManager.class);
			Integer blacklist = agentFunctionManager.getBlacklist();
			if (blacklist == null) {
				agentFunctionManagerService.selectByParam(agentFunctionManager, page);
			} else {
				agentFunctionManagerService.selectByParamBlacklist(agentFunctionManager, page);
			}
			jsonMap.put("page", page);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/exportConfig")
	@ResponseBody
	public Object exportConfig(@RequestParam("info") String param,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject object = JSONObject.parseObject(param);
			//每次请求都会上传随机数，如果出现重复，则不给予下载
			String random = String.valueOf(object.get("random"));
			Object has = request.getSession().getAttribute(random);
			//人工处理重复请求
			if(null != has){ return null; }
			request.getSession().setAttribute(random, random);
			AgentFunctionManager agentFunctionManager = object.toJavaObject(AgentFunctionManager.class);
			String  blacklist = object.get("blacklist").toString();
			List<AgentFunctionManager> list = new ArrayList<>();
			if ("1".equals(blacklist)) {
				 list = agentFunctionManagerService.exportConfigBlacklist(agentFunctionManager);
			} else {
				 list = agentFunctionManagerService.exportConfig(agentFunctionManager);
			}
			
			if(list!=null && list.size()>0){
				List<TeamInfo> teamInfos = teamInfoService.selectTeamName();
				Map<Object,String> teams = new HashMap();
				for (TeamInfo team : teamInfos) {
					teams.put(String.valueOf(team.getTeamId()),team.getTeamName());
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
				String fileName = "代理商管理导出-"+sdf.format(new Date())+".csv" ;
				String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
				response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);

				OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
				String utf_BOM = new String(new byte[]{( byte ) 0xEF ,( byte ) 0xBB ,( byte ) 0xBF });
				out.write(utf_BOM);
				if ("1".equals(blacklist)) {
					out.append("代理商编号,代理商名称,是否包含下级,属性\r");
					for (AgentFunctionManager item : list) {
						out.append(item.getAgentNo()).append(",");
						out.append(
								(item.getAgentName() + "").replaceAll("\n","").replaceAll("\r","")
								).append(",");
						out.append(
								(item.getContainsLower() == 1?"包含":"不包含").replaceAll("\n","").replaceAll("\r","")
								).append(",");
						out.append(teams.get(item.getTeamId())).append("\r");
					}
					
				}else {
					out.append("代理商编号,代理商名称,属性\r");
					for (AgentFunctionManager item : list) {
						out.append(item.getAgentNo()).append(",");
						out.append(
								(item.getAgentName() + "").replaceAll("\n","").replaceAll("\r","")
								).append(",");
						out.append(teams.get(item.getTeamId())).append("\r");
					}
				}
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/findAgentInfoByAgentNo.do")
	@ResponseBody
	public AgentInfo findAgentInfo(Integer blacklist,String agentNo) throws Exception {
		/*
		if (blacklist == null ) {
			// 只支持一级代理商
			return agentFunctionManagerService.findAgentInfoByAgentNoOneLevel(agentNo);
		}
		*/
		return agentFunctionManagerService.findAgentInfoByAgentNo(agentNo);
	}

	@RequestMapping(value = "/addAgentFunctionManager")
	@ResponseBody
	@SystemLog(description = "新增代理商管理",operCode="func.insert")
	public Object addAgentFunctionManager(@RequestBody String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(param+"=====");
		
		try {
			JSONObject json = JSON.parseObject(param);
			AgentFunctionManager agentFunctionManager = json.getObject("info", AgentFunctionManager.class);
			String functionNumber = json.getString("functionNumber");
//			AgentInfo agentInfo = JSON.parseObject(param, AgentInfo.class);
			AgentInfo agentInfo = new AgentInfo();
			String agentNo2 = agentFunctionManager.getAgentNo();
			agentInfo.setAgentNo(agentFunctionManager.getAgentNo());
			Integer blacklist = agentFunctionManager.getBlacklist();
			if (blacklist == null ) {
				// 只支持一级代理商
				agentInfo =  agentFunctionManagerService.findAgentInfoByAgentNoOneLevel(agentNo2);
			}else {
				agentInfo = agentFunctionManagerService.findAgentInfoByAgentNo(agentNo2);
			}
			if (agentInfo == null) {
				jsonMap.put("msg", "只支持一级代理商新增");
				jsonMap.put("bols", false);
			} else {
				int i = 0;
				// 存在就添加到agent_auto_check_manager表中,保存为AgentFunctionManager对象
				agentFunctionManager.setCreateTime(new Date());
				agentFunctionManager.setCreateUser(principal.getUsername());
				agentFunctionManager.setTeamId(agentInfo.getTeamId().toString());
				String agentNo = agentFunctionManager.getAgentNo();
				String functionNumber2 = agentFunctionManager.getFunctionNumber();
				// 判断该代理商是否被包含
				if (blacklist == null) {
					agentFunctionManager.setBlacklist(0);
					/*
					boolean isBlacklistNotContains = agentFunctionManagerService.isBlacklistNotContains(agentNo,functionNumber2);
					// 判断该代理商是否被加入黑名单
					boolean isAgentContains = agentFunctionManagerService.isAgentControlContains(agentNo,functionNumber2);
					if (isAgentContains || isBlacklistNotContains) {
						jsonMap.put("msg", "该代理商已存在");
						jsonMap.put("bols", false);
						return jsonMap;
					}
					*/
					int existsNum = agentFunctionManagerService.selectExists(agentFunctionManager);
					if (existsNum > 0) {
						jsonMap.put("msg", "该代理商已存在");
						jsonMap.put("bols", false);
						return jsonMap;
					}
				}else {
					boolean isBlacklistNotContains = agentFunctionManagerService.isBlacklistNotContains(agentNo,functionNumber2);
					boolean isAgentContains = agentFunctionManagerService.isAgentControlContains(agentNo,functionNumber2);
					// 删除该代理商 代理商控制配置
					//agentFunctionManagerService.deleteByAgentNoFunNum(agentNo,functionNumber2);
					if (isAgentContains || isBlacklistNotContains) {
						jsonMap.put("msg", "该代理商已存在");
						jsonMap.put("bols", false);
						return jsonMap;
					}
				}
				if (blacklist == null) {
					i = agentFunctionManagerService.addAgentFunctionManager(agentFunctionManager);
				}else {
					i = agentFunctionManagerService.addAgentFunctionManagerBlacklist(agentFunctionManager);
				}  
				
				if (i > 0) {
					if(FunctionManagerAction.FLUSHFUNCTIONNUMBER.equals(functionNumber)){
						ClientInterface.flushActivityCache(3);
					}
					if(FunctionManagerAction.FLUSHFUNCTIONNUMBER_BUYREWARD.equals(functionNumber)){
						ClientInterface.flushActivityCache(3);
					}
					jsonMap.put("msg", "添加成功");
					jsonMap.put("bols", true);
				} else if (i == 0) {
					jsonMap.put("msg", "添加失败");
					
					jsonMap.put("bols", false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("msg", "代理商已存在"); 
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	@RequestMapping(value = "/deleteInfo")
	@ResponseBody
	@SystemLog(description = "批量删除代理商管理",operCode="func.deleteBatch")
	public Object deleteInfo(@RequestBody String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			List<AgentFunctionManager> agentFunctionManagerList = JSON.parseArray(param, AgentFunctionManager.class);
			int num = 0;
			String functionNumber=null;
			for (AgentFunctionManager agentFunctionManager : agentFunctionManagerList) {
				Integer blacklist = agentFunctionManager.getBlacklist();
				if (blacklist == null) {
					num += agentFunctionManagerService.delete(agentFunctionManager.getId());
				}else {
					num += agentFunctionManagerService.deleteBlacklist(agentFunctionManager.getId());
				}
				if(functionNumber==null){
					functionNumber=agentFunctionManager.getFunctionNumber();
				}
			}
			if (num > 0) {
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER.equals(functionNumber)){
					ClientInterface.flushActivityCache(3);
				}
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER_BUYREWARD.equals(functionNumber)){
					ClientInterface.flushActivityCache(3);
				}
				jsonMap.put("msg", "操作成功");
				jsonMap.put("bols", true);
			} else {
				jsonMap.put("bols", false);
				jsonMap.put("msg", "操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("msg", "操作失败");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	@RequestMapping(value = "/deleteByAgentNo")
	@ResponseBody
	@SystemLog(description = "删除代理商管理",operCode="func.delete")
	public Object deleteById(@RequestParam("id") Integer id,@RequestParam("blacklist")Integer blacklist) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			AgentFunctionManager manager = null;
			if (blacklist == null) {
				 manager=  agentFunctionManagerService.get(id);
			}else {
				manager=  agentFunctionManagerService.getBlacklist(id);
			}
			int i = 0;
			if (blacklist == null) {
				i = agentFunctionManagerService.delete(id);
			}else {
				i= agentFunctionManagerService.deleteBlacklist(id);
			}
			String functionNumber = manager.getFunctionNumber();
			if (i > 0) {
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER.equals(functionNumber)){
					ClientInterface.flushActivityCache(3);
				}
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER_BUYREWARD.equals(functionNumber)){
					ClientInterface.flushActivityCache(3);
				}
				jsonMap.put("bols", true);
				jsonMap.put("msg", "删除成功");
			} else {
				jsonMap.put("bols", false);
				jsonMap.put("msg", "删除失败");
			}
		} catch (Exception e) {
			jsonMap.put("msg", "删除报错");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	// 根据操作中的"新增""删除"来执行批量添加,批量删除
	@RequestMapping(value = "/addButchAgentFunctionManager")
	@SystemLog(description = "批量新增代理商管理",operCode="func.insertBatch")
	public @ResponseBody Object importTerminal(@RequestParam("file") MultipartFile file,@RequestParam("functionNumber")String functionNumber,@RequestParam("blacklist")Integer blacklist) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(functionNumber+"=========*******=================");
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
				int state=0;
				ImportAgentVo importAgentVo = new ImportAgentVo();
				List<ImportReturnVo> voList = new ArrayList<>();
				for (int i = 1; i <= row_num; i++) {
					ImportReturnVo importReturnVo = new ImportReturnVo();
					Row row = sheet.getRow(i);
					String num1 = getCellValue(row.getCell(0));
					String num2 = getCellValue(row.getCell(1));
					String num3 = getCellValue(row.getCell(2));
					String num4=null;
					if (blacklist !=null) {
					  num4 = getCellValue(row.getCell(3));
					}

					if (StringUtils.isBlank(num1))
						break;
					String[] str1 = num1.split("\\.");
					String[] str2 = num2.split("\\.");
					String[] str3 = num3.split("\\.");
					String[] str4 = null ;
					if (blacklist !=null) {
						
						str4= num4.split("\\.");
					}

					String agentNo = str1[0];
					String agentName = str2[0];
					String handle = str3[0];//操作:新增,删除
					String containsLower = null;
					if (blacklist !=null) {
						 containsLower = str4[0];
					}
					importReturnVo.setHandle(handle);
					importReturnVo.setAgentNo(agentNo);
					importReturnVo.setContainsLower(containsLower==null?1:Integer.valueOf(containsLower));
					AgentInfo agentInfo = null;
					if (blacklist == null ) {
						agentInfo = agentFunctionManagerService.findAgentInfoByAgentNoOneLevel(agentNo);
					}else {
						agentInfo = agentFunctionManagerService.findAgentInfoByAgentNo(agentNo);
					}
					if(agentInfo != null){
						//把两张表都存在的,agent_function_manage表中全删除
//						agentFunctionManagerService.deleteInfo(agentInfo.getAgentNo(),null);
						AgentFunctionManager agentFunctionManager = new AgentFunctionManager();
						agentFunctionManager.setAgentNo(agentNo);
						agentFunctionManager.setAgentName(agentName);
						agentFunctionManager.setCreateTime(new Date());
						agentFunctionManager.setCreateUser(principal.getUsername());
						agentFunctionManager.setFunctionNumber(functionNumber);
						agentFunctionManager.setTeamId(agentInfo.getTeamId().toString());
						agentFunctionManager.setContainsLower(containsLower==null?1:Integer.valueOf(containsLower));
						agentFunctionManager.setBlacklist(blacklist==null? 0:blacklist);
						if("新增".equals(handle)){
									//检查代理编号+活动编号是否已存在
							if (blacklist == null) {
								int	existsNum = agentFunctionManagerService.selectExists(agentFunctionManager);
								if(existsNum > 0){
									importReturnVo.setErrMsg("该代理商已存在");
									voList.add(importReturnVo); 
									continue;
								}
							} else {
								boolean isBlacklistNotContains = agentFunctionManagerService.isBlacklistNotContains(agentFunctionManager.getAgentNo(),functionNumber);
								boolean isAgentContains = agentFunctionManagerService.isAgentControlContains(agentNo,functionNumber);
								// 删除该代理商 代理商控制配置
								//agentFunctionManagerService.deleteByAgentNoFunNum(agentNo,functionNumber2);
								if (isAgentContains || isBlacklistNotContains) {
									importReturnVo.setErrMsg("该代理商已存在");
									voList.add(importReturnVo); 
									continue;
								}
							}
							if (blacklist == null) {
								 agentFunctionManagerService.addAgentFunctionManager(agentFunctionManager);
							}else {
								agentFunctionManagerService.addAgentFunctionManagerBlacklist(agentFunctionManager);
							}  
							state++;
						}else if("删除".equals(handle)){
							if (blacklist == null) {
								agentFunctionManagerService.deleteInfo(agentInfo.getAgentNo(),functionNumber);
							}else {
								agentFunctionManagerService.deleteInfo(agentInfo.getAgentNo(),functionNumber,agentFunctionManager.getBlacklist());
							}
							state++;
						}
					}else {
						importReturnVo.setErrMsg("不存在该代理商");
						voList.add(importReturnVo);
					}
				}
				importAgentVo.setSuccessCount(state);
				importAgentVo.setErrCount(row_num-importAgentVo.getSuccessCount());
				importAgentVo.setList(voList);
				/*
				if(list.size()>0){
					if (blacklist == null) {
						agentFunctionManagerService.addButchAgentFunctionManager(list);
					}else {
						agentFunctionManagerService.addButchAgentFunctionManagerBlacklist(list);
					}
				}
				*/
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER.equals(functionNumber)){
					if(state>0){
						ClientInterface.flushActivityCache(3);
					}
				}
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER_BUYREWARD.equals(functionNumber)){
					if(state>0){
						ClientInterface.flushActivityCache(3);
					}
				}
				jsonMap.put("status", true);
				jsonMap.put("msg", "操作成功");
				jsonMap.put("data", importAgentVo);
				return jsonMap;
			} else {
				jsonMap.put("status", false);
				jsonMap.put("msg", "文件格式错误");
			}
		}catch (Exception e) {
			log.error("出错------>1111111111111111111111111111", e);
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
		String parameter = request.getParameter("blacklist");
		String filePath = "";
		if ("1".equals(parameter)) {
			 filePath = request.getServletContext().getRealPath("/")+File.separator+"template"+File.separator+"agentFunctionManagerTemplate.xls";
		}else {
			 filePath = request.getServletContext().getRealPath("/")+File.separator+"template"+File.separator+"agentFunctionManagerTemplate2.xls";
		}
		log.info(filePath);
		ResponseUtil.download(response, filePath,"批量新增模板.xlsx");
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
