package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ProblemType;
import cn.eeepay.framework.model.UserFeedbackProblem;
import cn.eeepay.framework.service.UserFeedbackProblemService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;

@Controller
@RequestMapping(value="/userFeedbackProblemAction")
public class UserFeedbackProblemAction {
	private static final Logger log = LoggerFactory.getLogger(UserFeedbackProblemAction.class);
	
	@Resource
	private UserFeedbackProblemService userFeedbackProblemService;
	
	/**
	 * 初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<UserFeedbackProblem> page,@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			UserFeedbackProblem ufp = JSON.parseObject(param,UserFeedbackProblem.class);
			userFeedbackProblemService.selectAllInfo(page, ufp);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllProblemInfo")
	public @ResponseBody Object selectAllProblemInfo() throws Exception {
		List<ProblemType> list = null;
		try {
			list=userFeedbackProblemService.selectAllProblemInfo();
		} catch (Exception e) {
			log.error("查询所有问题类型报错");
		}
		return list;
	}

	@DataSource
	@RequestMapping(value="/dealResult")
	@ResponseBody
	@SystemLog(operCode = "userFeedback.dealResult",description = "提交用户反馈问题处理")
	public Object saveDealResult(@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			UserFeedbackProblem ufp = JSON.parseObject(param,UserFeedbackProblem.class);
			userFeedbackProblemService.saveDealResult(ufp);
			jsonMap.put("status", true);
			jsonMap.put("msg", "提交处理成功");
		} catch (Exception e) {
			log.error("提交处理出错",e);
			jsonMap.put("status", false);
		}
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/export")
	@ResponseBody
	@SystemLog(operCode = "userFeedback.export",description = "导出用户反馈问题")
	public Object export(@RequestParam("info") String param, HttpServletResponse response)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			UserFeedbackProblem ufp = JSON.parseObject(param,UserFeedbackProblem.class);
			userFeedbackProblemService.export(ufp,response);
			jsonMap.put("status", true);
			jsonMap.put("msg", "导出用户反馈问题成功");
		} catch (Exception e) {
			log.error("导出用户反馈问题出错",e);
			jsonMap.put("status", false);
		}
		return jsonMap;
	}
	/**
	 * 问题反馈详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectDetail")
	@ResponseBody
	public Object selectDetail(@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(ids,Integer.class);
			UserFeedbackProblem ufp = userFeedbackProblemService.selectDetailById(id);
			List<String> lists=new ArrayList<String>();
			if(ufp==null){
				jsonMap.put("msg", "查询失败~~~~~");
				jsonMap.put("bols", false);
			}else{
				Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
				String content = ufp.getPrintscreen();
				if(content==null||content.equals("")){
				}else{
					String[] cc=content.split(",");
					for (int i = 0; i < cc.length; i++) {
						String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, cc[i], expiresDate);
						lists.add(newContent);
					}
				}
				jsonMap.put("tt", lists);
				jsonMap.put("result", ufp);
				jsonMap.put("bols", true);
			}
		} catch (Exception e) {
			log.error("问题反馈详情查询报错!!!",e);
			jsonMap.put("msg", "查询报错~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
}
