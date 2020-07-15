package cn.eeepay.boss.action.credit;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmCardInfo;
import cn.eeepay.framework.model.CmOrgInfo;
import cn.eeepay.framework.model.CmUserInfo;
import cn.eeepay.framework.service.CmUserManageService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/cmUserManage")
public class CmUserManageAction {
	private static final Logger log = LoggerFactory.getLogger(CmUserManageAction.class);

	@Resource
	public CmUserManageService cmUserManageService;

	/**
	 * 查询所有组织
	 * @author mays
	 * @date 2018年3月29日
	 */
	@RequestMapping(value = "/selectOrgAllInfo")
	public @ResponseBody Object selectOrgAllInfo() {
		List<CmOrgInfo> list = null;
		try {
			list = cmUserManageService.selectOrgAllInfo();
		} catch (Exception e) {
			log.error("信用卡管家-查询组织信息失败！", e);
		}
		return list;
	}

	/**
	 * 查询用户信息
	 * @author	mays
	 * @date	2018年4月3日
	 */
	@RequestMapping(value = "/selectUserInfo")
	@ResponseBody
	public Map<String, Object> selectUserInfo(@RequestBody CmUserInfo info, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmUserInfo> page = new Page<>(pageNo, pageSize);
			cmUserManageService.selectUserInfo(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-用户列表查询失败", e);
		}
		return msg;
	}

	/**
	 * 根据userNo查询用户详情(详情)
	 */
	@RequestMapping(value = "/selectUserDetailByUserNo")
	@ResponseBody
	public Map<String, Object> selectUserDetailByUserNo(@RequestParam("userNo")String userNo) {
		return getUserDetail(userNo,0);
	}


	/**
	 * 根据userNo查询用户详情(编辑)
	 */
	@RequestMapping(value = "/selectUserDetailEdit")
	@ResponseBody
	public Map<String, Object> selectUserDetailEdit(@RequestParam("userNo")String userNo) {
		return getUserDetail(userNo,1);
	}
	/**
	 * 获取敏感信息
	 */
	@RequestMapping(value = "/getDataProcessing")
	@ResponseBody
	public Map<String, Object> getDataProcessing(@RequestParam("userNo")String userNo) {
		return getUserDetail(userNo,1);
	}

	private Map<String, Object> getUserDetail(String userNo,int editState){
		Map<String, Object> msg = new HashMap<>();
		try {
			CmUserInfo info =cmUserManageService.selectUserInfoByUserNo(userNo);
			if(0==editState){
				info.setMobileNo(StringUtil.sensitiveInformationHandle(info.getMobileNo(),0));
			}
			List<CmCardInfo> cardList = cmUserManageService.selectCardInfoByUserNo(userNo);
			for (CmCardInfo cci : cardList) {
				if (cci.getTotalAmount() != null) {
					cci.setTotalAmount(cci.getTotalAmount().divide(new BigDecimal(100)));
				}
				if(0==editState){
					cci.setMobileNo(StringUtil.sensitiveInformationHandle(cci.getMobileNo(),0));
				}

			}
			msg.put("status", true);
			msg.put("info", info);
			msg.put("cardList", cardList);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-用户详情查询失败", e);
		}
		return msg;
	}

	/**
	 * 修改用户信息-目前只有修改手机号
	 * @author	mays
	 * @date	2018年4月6日
	 */
	@RequestMapping(value = "/updateUserInfo")
	@ResponseBody
	@SystemLog(description = "修改用户",operCode="creditMgr.updateUser")
	public Map<String, Object> updateUserInfo(String userNo, String mobileNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (1 == cmUserManageService.updateUserInfo(userNo, mobileNo)) {
				msg.put("msg", "修改成功");
				msg.put("status", true);
			} else {
				msg.put("msg", "修改失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "修改失败");
			log.error("信用卡管家-修改用户信息失败", e);
		}
		return msg;
	}

	/**
	 * 信用卡管家-用户管理-导出
	 * @author	mays
	 * @date	2018年5月22日
	 */
	@RequestMapping(value = "/exportCmUser")
	@ResponseBody
	public void exportCmUser(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CmUserInfo bean = JSON.parseObject(info, CmUserInfo.class);
		List<CmUserInfo> list = cmUserManageService.exportCmUser(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "信用卡管家用户信息" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("userNo", null);
			maps.put("userName", null);
			maps.put("mobileNo", null);
			maps.put("srcOrgId", null);
			maps.put("srcUserId", null);
			maps.put("orgName", null);
			maps.put("agentName", null);
			maps.put("oneAgentName", null);
			maps.put("createTime", null);
			maps.put("userType", null);
			maps.put("memberExpire", null);
			data.add(maps);
		} else {
			for (CmUserInfo i : list) {
				if ("1".equals(i.getIsVip()) && i.getUserType() != null && 1 == i.getUserType()) {
					i.setIsVip("月付费");
				} else {
					i.setIsVip("普通");
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("userNo", null == i.getUserNo() ? "" : i.getUserNo());
				maps.put("userName", null == i.getUserName() ? "" : i.getUserName());
				maps.put("mobileNo", null == i.getMobileNo() ? "" : i.getMobileNo());
				maps.put("srcOrgId", null == i.getSrcOrgId() ? "" : i.getSrcOrgId());
				maps.put("srcUserId", null == i.getSrcUserId() ? "" : i.getSrcUserId());
				maps.put("orgName", null == i.getOrgName() ? "" : i.getOrgName());
				maps.put("agentName", null == i.getAgentName() ? "" : i.getAgentName());
				maps.put("oneAgentName", null == i.getOneAgentName() ? "" : i.getOneAgentName());
				maps.put("createTime", null == i.getCreateTime() ? "" : sdf1.format(i.getCreateTime()));
				maps.put("userType", i.getIsVip());
				maps.put("memberExpire", null == i.getMemberExpire() ? "" : sdf1.format(i.getMemberExpire()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = { "userNo", "userName", "mobileNo", "srcOrgId", "srcUserId", "orgName", "agentNo",
				"oneAgentNo","createTime", "userType", "memberExpire" };
		String[] colsName = { "用户ID", "姓名", "手机号", "来源组织ID", "来源用户/商户编号", "来源组织名称",
				"所属代理商名称", "一级代理商名称", "入驻时间", "会员类型", "会员到期时间" };
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

}
