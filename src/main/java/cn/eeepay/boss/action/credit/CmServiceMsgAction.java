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
import cn.eeepay.framework.model.CmUserMessageInfo;
import cn.eeepay.framework.service.CmServiceMsgService;

@Controller
@RequestMapping(value = "/cmServiceMsg")
public class CmServiceMsgAction {
	private static final Logger log = LoggerFactory.getLogger(CmServiceMsgAction.class);

	@Resource
	public CmServiceMsgService cmServiceMsgService;

	/**
	 * 服务消息列表查询
	 * @author mays
	 * @date 2018年4月9日
	 */
	@RequestMapping(value = "/selectMsgInfo")
	@ResponseBody
	public Map<String, Object> selectMsgInfo(@RequestBody CmUserMessageInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmUserMessageInfo> page = new Page<>(pageNo, pageSize);
			cmServiceMsgService.selectMsgInfo(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-服务消息列表查询失败", e);
		}
		return msg;
	}

	/**
	 * 根据id查询消息详情
	 * @author	mays
	 * @date	2018年4月9日
	 */
	@RequestMapping(value = "/selectMsgInfoById")
	@ResponseBody
	public Map<String, Object> selectMsgInfoById(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			CmUserMessageInfo info = cmServiceMsgService.selectMsgInfoById(id);
			msg.put("status", true);
			msg.put("info", info);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-服务消息详情查询失败", e);
		}
		return msg;
	}

	/**
	 * 回收服务消息
	 * @author	mays
	 * @date	2018年4月9日
	 */
	@RequestMapping(value = "/updateMsgIsDelById")
	@ResponseBody
	@SystemLog(description = "回收服务消息",operCode="creditMgr.recoverMessage")
	public Map<String, Object> updateMsgIsDelById(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (1 == cmServiceMsgService.updateMsgIsDelById(id)) {
				msg.put("status", true);
				msg.put("msg", "操作成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "操作失败");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "操作失败");
			log.error("信用卡管家-修改消息is_del状态失败", e);
		}
		return msg;
	}

}
