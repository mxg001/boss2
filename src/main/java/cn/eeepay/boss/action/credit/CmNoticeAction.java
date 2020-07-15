package cn.eeepay.boss.action.credit;

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
import cn.eeepay.framework.model.CmNoticeInfo;
import cn.eeepay.framework.service.CmNoticeService;

/**
 * 信用卡管家-系统公告
 * @author mays
 * @date 2018年4月2日
 */
@Controller
@RequestMapping(value = "/cmNotice")
public class CmNoticeAction {
	private static final Logger log = LoggerFactory.getLogger(CmNoticeAction.class);

	@Resource
	public CmNoticeService cmNoticeService;

	/**
	 * 公告查询
	 * @author mays
	 * @date 2018年4月2日
	 */
	@RequestMapping(value = "/selectInfo")
	@ResponseBody
	public Map<String, Object> selectInfo(@RequestBody CmNoticeInfo info, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmNoticeInfo> page = new Page<>(pageNo, pageSize);
			cmNoticeService.selectInfo(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-公告查询失败", e);
		}
		return msg;
	}

	/**
	 * 根据id查询公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	@RequestMapping(value = "/queryNoticeById")
	@ResponseBody
	public Map<String, Object> queryNoticeById(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			CmNoticeInfo info = cmNoticeService.queryNoticeById(id);
			msg.put("status", true);
			msg.put("info", info);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-公告查询失败", e);
		}
		return msg;
	}

	/**
	 * 新增Notice
	 * @author mays
	 * @date 2018年4月2日
	 */
	@RequestMapping("/addNotice")
	@ResponseBody
	@SystemLog(description = "新增公告",operCode="creditMgr.addNotice")
	public Map<String, Object> addNotice(@RequestBody CmNoticeInfo info) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (info == null) {
				msg.put("msg", "新增异常，数据为空");
				msg.put("status", false);
				return msg;
			}
			if (1 == cmNoticeService.addNotice(info)) {
				msg.put("msg", "新增成功");
				msg.put("status", true);
			} else {
				msg.put("msg", "新增失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			log.error("信用卡管家-新增Notice异常", e);
			msg.put("msg", "新增失败");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 修改公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	@RequestMapping("/updateNotice")
	@ResponseBody
	@SystemLog(description = "修改公告",operCode="creditMgr.updateNotice")
	public Map<String, Object> updateNotice(@RequestBody CmNoticeInfo info) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (info == null) {
				msg.put("msg", "数据为空");
				msg.put("status", false);
				return msg;
			}
			if (1 == cmNoticeService.updateNotice(info)) {
				msg.put("msg", "修改成功");
				msg.put("status", true);
			} else {
				msg.put("msg", "修改失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			log.error("信用卡管家-修改公告异常", e);
			msg.put("msg", "修改失败");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 根据id删除公告，置为'已废弃'
	 * @author mays
	 * @date 2018年4月3日
	 */
	@RequestMapping(value = "/delNoticeById")
	@ResponseBody
	@SystemLog(description = "删除公告",operCode="creditMgr.delNotice")
	public Map<String, Object> delNoticeById(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (1 == cmNoticeService.delNoticeById(id)) {
				msg.put("status", true);
				msg.put("msg", "删除成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "删除失败");
			}
		} catch (Exception e) {
			log.error("信用卡管家-删除公告失败", e);
			msg.put("status", false);
			msg.put("msg", "删除失败");
		}
		return msg;
	}

	/**
	 * 修改'弹窗提示开关'
	 * @author mays
	 * @date 2018年4月3日
	 */
	@RequestMapping(value = "/updateNoticePop")
	@ResponseBody
	@SystemLog(description = "修改弹窗提示开关",operCode="creditMgr.updateNotice")
	public Map<String, Object> updateNoticePop(String id, String popSwitch) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if ("0".equals(popSwitch) || "1".equals(popSwitch)) {
				if (1 == cmNoticeService.updateNoticePop(id, popSwitch)) {
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
			log.error("信用卡管家-修改'弹窗提示开关'失败", e);
			msg.put("status", false);
			msg.put("msg", "修改失败");
		}
		return msg;
	}

	/**
	 * 下发或收回公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	@RequestMapping("/sendOrRecoverNotice")
	@ResponseBody
	@SystemLog(description = "下发或收回公告",operCode="creditMgr.updateNotice")
	public Map<String, Object> sendOrRecoverNotice(@RequestBody CmNoticeInfo info) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (info == null || info.getId() == null || info.getStatus() == null) {
				msg.put("msg", "数据为空");
				msg.put("status", false);
				return msg;
			}
			if (1 == cmNoticeService.sendOrRecoverNotice(info)) {
				msg.put("msg", "操作成功");
				msg.put("status", true);
			} else {
				msg.put("msg", "操作失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			log.error("信用卡管家-下发或收回公告异常", e);
			msg.put("msg", "操作失败");
			msg.put("status", false);
		}
		return msg;
	}

}
