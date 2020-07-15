package cn.eeepay.boss.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CustomerServiceProblem;
import cn.eeepay.framework.model.CustomerServiceQo;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.CustomerServiceProblemService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ListDataExcelExport;

@Controller
public class CustomerServiceProblemController {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceProblemController.class);

	@Autowired
	private CustomerServiceProblemService customerServiceProblemService;
	@Autowired
	private SysDictService sysDictService;
	
	@RequestMapping("customService/problemAdd")
	@ResponseBody
	public Result add(@RequestBody CustomerServiceProblem data) {
		Result result = new Result();
		try {
			customerServiceProblemService.save(data);
			result.setStatus(true);
		} catch (Exception e) {
			log.info("保存失败",e);
			result.setStatus(false);
		}
		return result;
	}
	
	@RequestMapping("customService/problemRemove")
	@ResponseBody
	public Result remove(String id) {
		Result result = new Result();
		try {
			customerServiceProblemService.removeById(id);
			result.setStatus(true);
		} catch (Exception e) {
			log.info("删除失败",e);
			result.setStatus(false);
		}
		return result;
	}

	@RequestMapping("customService/problemQuery")
	@ResponseBody
	public Page<CustomerServiceProblem> query(@RequestBody CustomerServiceQo qo,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Page<CustomerServiceProblem> page = new Page<>(pageNo, pageSize);
		try {
			customerServiceProblemService.queryByQo(page, qo);
			List<CustomerServiceProblem> csProblemList = page.getResult();
			List<SysDict> types = sysDictService.selectByKey("CUSTOM_SERVICE_PROBLEM_TYPE");
			List<SysDict> agentOemList = sysDictService.selectByKey("AGENT_OEM");
			for (CustomerServiceProblem problem : csProblemList) {
				for (SysDict sysDict : types) {
					if (sysDict.getSysValue().equals(problem.getProblemType().toString())) {
						problem.setProblemTypeName(sysDict.getSysName());
					}
				}
				String appScope = problem.getAppScope();
				if (StringUtils.hasLength(appScope)) {
					StringBuilder appScopeNameSb = new StringBuilder();
					String[] appArray = appScope.split("-");
					if (appArray.length > 0) {
						for (int i = 0; i < appArray.length; i++) {
							for (SysDict sysDict : agentOemList) {
								if (sysDict.getSysValue().equals(appArray[i])) {
									appScopeNameSb.append(sysDict.getSysName());
									appScopeNameSb.append(",");
								}
							}
						}
						if (appScopeNameSb.length() > 0) {
							appScopeNameSb.deleteCharAt(appScopeNameSb.length() - 1);
						}
					}
					problem.setAppScopeName(appScopeNameSb.toString());
				} else {
					problem.setAppScopeName("全部");
				}
			}
		} catch (Exception e) {
			log.error("查询出错", e);
		}
		return page;
	}

	@RequestMapping("customService/problemExport")
	@ResponseBody
	public void export(String qoStr, HttpServletResponse response) {
		CustomerServiceQo qo = JSON.parseObject(qoStr, CustomerServiceQo.class);
		List<CustomerServiceProblem> csProblemList = customerServiceProblemService.listByQo(qo);
		String fileName = "客服问题列表" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xls";
		String fileNameFormat;
		try {
			fileNameFormat = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		List<SysDict> types = sysDictService.selectByKey("CUSTOM_SERVICE_PROBLEM_TYPE");
		List<SysDict> agentOemList = sysDictService.selectByKey("AGENT_OEM");
		List<Map<String, String>> data = new ArrayList<>();
		for (CustomerServiceProblem problem : csProblemList) {
			Map<String, String> map = new HashMap<>();
			map.put("problemId", problem.getProblemId().toString());
			for (SysDict sysDict : types) {
				if (sysDict.getSysValue().equals(problem.getProblemType().toString())) {
					problem.setProblemTypeName(sysDict.getSysName());
				}
			}
			map.put("problemTypeName", problem.getProblemTypeName());
			map.put("clicks", problem.getClicks().toString());
			map.put("solveNum",problem.getSolveNum()==null?"": problem.getSolveNum().toString());
			map.put("noSolveNum", problem.getNoSolveNum()==null?"":problem.getNoSolveNum().toString());
			String appScope = problem.getAppScope();
			if (StringUtils.hasLength(appScope)) {
				StringBuilder appScopeNameSb = new StringBuilder();
				String[] appArray = appScope.split("-");
				if (appArray.length > 0) {
					for (int i = 0; i < appArray.length; i++) {
						for (SysDict sysDict : agentOemList) {
							if (sysDict.getSysValue().equals(appArray[i])) {
								appScopeNameSb.append(sysDict.getSysName());
								appScopeNameSb.append(",");
							}
						}
					}
					if (appScopeNameSb.length() > 0) {
						appScopeNameSb.deleteCharAt(appScopeNameSb.length() - 1);
					}
				}

				map.put("appScopeName", appScopeNameSb.toString());
			} else {
				map.put("appScopeName", "全部");
			}
			map.put("problemName", problem.getProblemName());
			data.add(map);
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "problemId", "problemTypeName", "problemName", "appScopeName",
				"clicks","solveNum","noSolveNum" };
		String[] colsName = new String[] { "序列", "类型", "问题名称","适用范围", "点击率", "已解决", "未解决" };
		OutputStream ouputStream;
		try {
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, ouputStream);
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
