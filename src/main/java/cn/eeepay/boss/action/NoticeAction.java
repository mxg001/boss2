package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.NoticeInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.allAgent.AwardParam;
import cn.eeepay.framework.service.NoticeInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.allAgent.AwardParamService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateEditor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/notice")
public class NoticeAction {

	private static final Logger log = LoggerFactory.getLogger(NoticeAction.class);

	public static final String ORMKEY="OEM_TYPE";
	@Resource
	private NoticeInfoService noticeInfoService;

	@Resource
	private SysDictService sysDictService;//数据字典service

	@Resource
	private AwardParamService awardParamService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByParam.do")
	public @ResponseBody Object selectByParam(@RequestParam("baseInfo") String param,@ModelAttribute("page") 
		Page<NoticeInfo> page) throws Exception{
		try{
			NoticeInfo notice = JSONObject.parseObject(param, NoticeInfo.class);
			noticeInfoService.selectByParam(notice, page);
		} catch (Exception e){
			log.error("条件查询公告失败",e);
		}
		return page;
	}

	@RequestMapping(value = "/addNotice.do")
	public Map<String, Object> addNotice() throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg = noticeInfoService.selectLinkInfo();
			//查询数据字典中的组织
			List<SysDict> dictList=sysDictService.selectByKey(ORMKEY);
			msg.put("oemTypes",dictList);
		} catch (Exception e) {
			log.error("添加 || 修改公告失败！", e);
			msg.put("status", false);
			msg.put("msg", "添加 || 修改公告失败！");
		}
		return msg;
	}
	
	@RequestMapping(value = "/saveNotice.do")
	@SystemLog(description = "公告新增|修改",operCode="notice.insert")
	public Map<String, Object> saveNotice(@RequestBody String param) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSON.parseObject(param);
			log.info("公告新增|修改参数="+json.toJSONString());
			NoticeInfo notice = json.getObject("notice", NoticeInfo.class);
			if("0".equals(notice.getShowStatus())){
				notice.setValidBeginTime(null);
				notice.setValidEndTime(null);
			}
			JSONObject baseInfo = (JSONObject) json.get("baseInfo");
			Map<String, Object> data = new HashMap<>();
			if(json.getJSONArray("products") != null){
				List<BusinessProductDefine> products = JSON.parseArray(json.getJSONArray("products").toJSONString(),BusinessProductDefine.class);
				data.put("products", products);
			}
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			notice.setLoginUser(principal.getId().toString());
			data.put("notice", notice);
			data.put("isAll", baseInfo.get("isAll"));
			data.put("agentBusiness", baseInfo.get("agentBusiness"));
			
			int ret = 0;
			if(notice.getNtId() == null){
				 ret = noticeInfoService.insert(data);
				 if (ret > 0) {
						msg.put("status", true);
						msg.put("msg", "添加成功");
					}
			}else{
				ret= noticeInfoService.update(data);
				if (ret > 0) {
					msg.put("status", true);
					msg.put("msg", "修改成功");
				}
			}

		} catch (Exception e) {
			log.error("添加 || 修改公告失败！", e);
			msg.put("status", false);
			msg.put("msg", "添加 || 修改公告失败！");
		}
		return msg;
	}

	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectById/{id}")
	@ResponseBody
	public Map<String,Object> selectById(@PathVariable("id") String id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		System.out.println("根据公告ID查询详情，ID = " + id);
		try{
			msg = noticeInfoService.selectById(id);
			screeningSysDictList(msg,0);
		} catch (Exception e){
			log.error("查询公告详情失败！",e);
			msg.put("status", false);
			msg.put("msg", "查询公告详情失败！");
		}
		return msg;
	}

	/**
	 * 筛选选中的指定组织
	 * @param state 0 详情，1修改
	 */
	private Map<String, Object>  screeningSysDictList(Map<String, Object> msg,int state){
		if(msg.get("notice")!=null){
			NoticeInfo notice=(NoticeInfo)msg.get("notice");
			List<SysDict> dictList=sysDictService.selectByKey(ORMKEY);
			List<AwardParam> oemListes=awardParamService.getOemList();
			if(notice.getSysType()!=null&&"2".equals(notice.getSysType())){
				if(notice.getOemType()!=null){
					List<String> list=Arrays.asList(notice.getOemType().split(","));
					if(list!=null&&list.size()>0){
						Iterator<SysDict> ite=dictList.iterator();
						if(state==0){
							while (ite.hasNext()){
								SysDict syd=ite.next();
								if(list.contains(syd.getSysValue())){
									syd.setCheckedState("1");
								}else{
									ite.remove();
								}
							}
						}else if(state==1){
							while (ite.hasNext()){
								SysDict syd=ite.next();
								if(list.contains(syd.getSysValue())){
									syd.setCheckedState("1");
								}
							}
						}
					}
				}
				if(notice.getOemList()!=null){
					List<String> list=Arrays.asList(notice.getOemList().split(","));
					if(list!=null&&list.size()>0){
						for (AwardParam a:oemListes){
							if(list.contains(a.getBrandCode())){
								a.setCheckedState("1");
							}
						}
					}

				}
			}
			msg.put("oemTypes",dictList);
			msg.put("oemListes",oemListes);
		}
		return msg;
	}
	/**
	 * 修改页面需要携带的数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectInfoById/{id}")
	@ResponseBody
	public Map<String,Object> selectInfoById(@PathVariable("id") String id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try{
			msg = noticeInfoService.selectInfoById(id);
			screeningSysDictList(msg,1);
		} catch (Exception e){
			log.error("修改公告详情失败！",e);
			msg.put("status", false);
			msg.put("msg", "修改公告详情失败！");
		}
		return msg;
	}

	@RequestMapping(value = "/deliverNotice/{id}")
	@ResponseBody
	@SystemLog(description = "公告下发",operCode="notice.deliver")
	public Map<String,Object> deliverNotice(@PathVariable("id") long id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int sum = noticeInfoService.deliverNotice(id);
			if(sum > 0){
				msg.put("status", true);
				msg.put("msg", "下发成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "下发失败");
		}
		return msg;
	}
	
	@RequestMapping(value = "/recoverNotice.do")
	@ResponseBody
	@SystemLog(description = "公告回收",operCode="notice.recover")
	public Map<String,Object> recoverNotice(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = noticeInfoService.updateRecoverNotice(id);
			if(num == 1){
				noticeInfoService.strongNotice(id,0);			//0取消置顶
				msg.put("state", true);
				msg.put("msg", "收回成功");
			}
		} catch (Exception e) {
			msg.put("state", false);
			msg.put("msg", "收回失败");
		}
		return msg;
	}

	@RequestMapping(value = "/deleteNotice.do")
	@ResponseBody
	//@SystemLog(description = "公告删除",operCode="notice.recover")
	public Map<String,Object> deleteNotice(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = noticeInfoService.deleteRecoverNotice(id);
			if(num == 1){
				msg.put("state", true);
				msg.put("msg", "删除成功");
			}
		} catch (Exception e) {
			msg.put("state", false);
			msg.put("msg", "删除失败");
		}
		return msg;
	}

	@RequestMapping(value = "/optStrong.do")
	@ResponseBody
	//@SystemLog(description = "公告置顶",operCode="notice.recover")
	public Map<String,Object> optStrong(@RequestParam("id")Integer id,@RequestParam("type")Integer type) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = 0;
			if(type==1){		//置顶
				Map<String, Object> obj = noticeInfoService.selectInfoById(id+"");
				if(obj==null || obj.size()==0){
					msg.put("state", false);
					msg.put("msg", "操作失败");
					return msg;
				}
				NoticeInfo noticeInfo = (NoticeInfo) obj.get("notice");
				String sysType = noticeInfo.getSysType();
				//可以无限置顶，不需要清除
				//noticeInfoService.clearStrongNotice(sysType);
				num = noticeInfoService.strongNotice(id,1);			//1置顶
			}else{
				num = noticeInfoService.strongNotice(id,0);			//0取消置顶
			}
			if(num == 1){
				msg.put("state", true);
				msg.put("msg", "操作成功");
			}
		} catch (Exception e) {
			msg.put("state", false);
			msg.put("msg", "操作失败");
		}
		return msg;
	}
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

}
