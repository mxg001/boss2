package cn.eeepay.boss.action.credit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBannerInfo;
import cn.eeepay.framework.service.CmBannerService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;

/**
 * 信用卡管家-Banner管理
 * @author mays
 * @date 2018年3月29日
 */
@Controller
@RequestMapping(value = "/cmBanner")
public class CmBannerAction {
	private static final Logger log = LoggerFactory.getLogger(CmBannerAction.class);

	@Resource
	public CmBannerService cmBannerService;

	/**
	 * Banner查询
	 * @author mays
	 * @date 2018年3月29日
	 */
	@RequestMapping(value = "/selectInfo")
	@ResponseBody
	public Map<String, Object> selectInfo(@RequestBody CmBannerInfo ad, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmBannerInfo> page = new Page<>(pageNo, pageSize);
			cmBannerService.selectInfo(page, ad);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-Banner查询失败", e);
		}
		return msg;
	}

	/**
	 * 根据id查询Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	@RequestMapping(value = "/queryBannerById")
	@ResponseBody
	public Map<String, Object> queryBannerById(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			CmBannerInfo info = cmBannerService.queryBannerById(id);
			msg.put("status", true);
			msg.put("info", info);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-Banner查询失败", e);
		}
		return msg;
	}

	/**
	 * 新增Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	@RequestMapping("/addBanner")
	@ResponseBody
	@SystemLog(description = "新增Banner",operCode="creditMgr.addBanner")
	public Map<String, Object> addBanner(@RequestBody CmBannerInfo info) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (info == null) {
				msg.put("msg", "新增异常，数据为空");
				msg.put("status", false);
				return msg;
			}
			info.setStatus(1);
			Date expiresDate = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
			String picUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, info.getPicName(), expiresDate);
    		info.setPicUrl(picUrl);
			if (1 == cmBannerService.addBanner(info)) {
				msg.put("msg", "新增Banner成功");
				msg.put("status", true);
			} else {
				msg.put("msg", "新增Banner失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			log.error("信用卡管家-新增Banner异常", e);
			msg.put("msg", "新增异常，数据为空");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 修改Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	@RequestMapping("/updateBanner")
	@ResponseBody
	@SystemLog(description = "修改Banner",operCode="creditMgr.updateBanner")
	public Map<String, Object> updateBanner(@RequestBody CmBannerInfo info) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (info == null) {
				msg.put("msg", "数据为空");
				msg.put("status", false);
				return msg;
			}
			if (info.getPicName() != null || !"".equals(info.getPicName())) {
				//有修改图片
//				Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
				Date expiresDate = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
				String picUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, info.getPicName(), expiresDate);
				info.setPicUrl(picUrl);
			}
			if (1 == cmBannerService.updateBanner(info)) {
				msg.put("msg", "修改成功");
				msg.put("status", true);
			} else {
				msg.put("msg", "修改失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			log.error("信用卡管家-修改Banner异常", e);
			msg.put("msg", "修改失败");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 根据id删除Banner
	 * @author	mays
	 * @date	2018年4月2日
	 */
	@RequestMapping(value = "/delBannerById")
	@ResponseBody
	@SystemLog(description = "删除Banner",operCode="creditMgr.delBanner")
	public Map<String, Object> delBannerById(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (1 == cmBannerService.delBannerById(id)) {
				msg.put("status", true);
				msg.put("msg", "删除成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "删除失败");
			}
		} catch (Exception e) {
			log.error("信用卡管家-Banner查询失败", e);
			msg.put("status", false);
			msg.put("msg", "删除失败");
		}
		return msg;
	}

	/**
	 * 修改Banner状态
	 * @author	mays
	 * @date	2018年4月2日
	 */
	@RequestMapping(value = "/updateBannerStatus")
	@ResponseBody
	@SystemLog(description = "修改Banner状态",operCode="creditMgr.updateBanner")
	public Map<String, Object> updateBannerStatus(String id, String status) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if ("0".equals(status) || "1".equals(status)) {
				if (1 == cmBannerService.updateBannerStatus(id, status)) {
					msg.put("status", true);
					msg.put("msg", "修改成功");
				} else {
					msg.put("status", false);
					msg.put("msg", "修改失败");
				}
			} else {
				msg.put("status", false);
				msg.put("msg", "数据异常");
			}
		} catch (Exception e) {
			log.error("信用卡管家-修改Banner状态失败", e);
			msg.put("status", false);
			msg.put("msg", "修改失败");
		}
		return msg;
	}

}
