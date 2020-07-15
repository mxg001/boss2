package cn.eeepay.framework.service.impl;

import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.action.UserFeedbackProblemAction;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.UserFeedbackProblemDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ProblemType;
import cn.eeepay.framework.model.UserFeedbackProblem;
import cn.eeepay.framework.service.UserFeedbackProblemService;

@Service("userFeedbackProblemService")
@Transactional
public class UserFeedbackProblemServiceImpl implements UserFeedbackProblemService{
	private static final Logger log = LoggerFactory.getLogger(UserFeedbackProblemServiceImpl.class);

	@Resource
	private UserFeedbackProblemDao userFeedbackProblemDao;

	@Override
	public List<UserFeedbackProblem> selectAllInfo(Page<UserFeedbackProblem> page, UserFeedbackProblem ufp) {
		return userFeedbackProblemDao.selectAllInfo(page, ufp);
	}

	@Override
	public UserFeedbackProblem selectDetailById(int id) {
		return userFeedbackProblemDao.selectDetailById(id);
	}

	@Override
	public List<ProblemType> selectAllProblemInfo() {
		return userFeedbackProblemDao.selectAllProblemInfo();
	}

	@Override
	public void saveDealResult(UserFeedbackProblem ufp) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ufp.setStatus(1);//已处理
		ufp.setDealUserId(Long.valueOf(principal.getId()));
		userFeedbackProblemDao.saveDealResult(ufp);
	}


	@Override
	public void export(UserFeedbackProblem ufp, HttpServletResponse response) {
		List<UserFeedbackProblem> infos = userFeedbackProblemDao.export(ufp);
		try {
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			String fileName = "用户反馈问题.xlsx";
			String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);

			for (UserFeedbackProblem info : infos) {
				Map<String,String> map = new HashMap<>();
				map.put("id",info.getId()+"");
				map.put("userType", convertUserType(info.getUserType()));
				map.put("appName", info.getAppName());
				map.put("userName", info.getUserName());
				map.put("mobilephone", info.getMobilephone());
				map.put("typeName", info.getTypeName());
				map.put("title", info.getTitle());
				if(info.getSubmitTime()!=null){
					map.put("submitTime", DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",info.getSubmitTime()));
				}
				map.put("status", info.getStatus()==null?"":info.getStatus()==1?"已处理":info.getStatus()==0?"待处理":"");
				map.put("dealUserName", info.getDealUserName());
				if(info.getDealTime()!=null){
					map.put("dealTime", DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",info.getDealTime()));
				}
				map.put("dealResult", info.getDealResult());
				data.add(map);
			}

			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[] { "id", "userType", "appName", "userName",
					"mobilephone","typeName","title","submitTime","status","dealUserName","dealTime","dealResult"};
			String[] colsName = new String[] { "ID", "反馈用户分类", "问题来源", "反馈用户",
					"反馈用户手机号","问题类别","标题","提交时间", "状态","处理人员","处理时间","处理结果"};
			OutputStream outputStream = response.getOutputStream();
			export.export(cols, colsName, data, outputStream);
			outputStream.close();
		} catch (Exception e) {
			log.error("用户反馈问题导出出错",e);
		}
	}

	private String convertUserType(String str){
		if(StringUtil.isBlank(str)){
			return "";
		}
		switch (str){
			case "1":
				return "代理商";
			case "2":
				return "商户";
			default:
				return "";
		}
	}

	private Map<String, String> convertProblemTypes() {
		List<ProblemType> problemTypes = userFeedbackProblemDao.selectAllProblemInfo();
		Map<String,String> map = new HashMap<>();
		for (ProblemType problemType : problemTypes) {
			map.put(problemType.getProblemType(),problemType.getTypeName());
		}
		return map;
	}


}
