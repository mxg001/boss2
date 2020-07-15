package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.BannerInfo;
import cn.eeepay.framework.service.BannerInfoService;

@Controller
@RequestMapping(value = "/banner")
public class BannerInfoAction {
	
	private static final Logger log = LoggerFactory.getLogger(BannerInfoAction.class);
	
	@Resource
	private BannerInfoService bannerInfoService; 
	
	/**
	 * 条件查询banner
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByCondition.do")
	public @ResponseBody Object selectBannerByCondition (@RequestParam("query") String param,@ModelAttribute("page")Page<BannerInfo> page) throws Exception{
		try{
			BannerInfo banner = JSON.parseObject(param,BannerInfo.class);
			bannerInfoService.selectByCondition(banner,page);
		} catch (Exception e){
			log.error("条件查询banner失败！",e);
		}
		return page;
	}
	
	/**
	 * 开启、关闭banner
	 * @param bannerId、bannerStatus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/switchStatus.do")
	@ResponseBody
	@SystemLog(description = "banner广告状态开关",operCode="banner.switch")
	public Map<String, Object> switchStatus (@RequestBody BannerInfo banner) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try{
			int num = bannerInfoService.updateStatus(banner);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "操作失败");
			}
		} catch (Exception e){
			msg.put("status", false);
			log.error("Switch banner失败！",e);
		}
		return msg;
	}
	
	/**
	 * 根据bannerId查询banner详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/bannerDetail/{id}")
	@ResponseBody
	public BannerInfo bannerDetail(@PathVariable("id") String id) throws Exception{
		BannerInfo banner = null;
		try{
			banner = bannerInfoService.selectDetailById(id);
		} catch(Exception e){
			log.error("查询详情失败",e);
		}
		return banner;
	}
	
	/**
	 * 新增banner时，需要带入的相关数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addBanner.do")
	@ResponseBody
	public Map<String, Object> addBanner() throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try{
			msg = bannerInfoService.selectLinkInfo("");
		} catch(Exception e){
			msg.put("status",false);
			log.error("查询详情失败",e);
		}
		return msg;
	}
	
	/**
	 * 获取app的相关信息
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getAppInfo.do")
	@ResponseBody
	public List<AppInfo> getAppInfo() throws Exception{
		List<AppInfo> list = new ArrayList<>();
		try{
			list = bannerInfoService.getAppInfo();
		} catch(Exception e){
			log.error("查询详情失败",e);
		}
		return list;
	}
	
	@RequestMapping(value = "/modifyById/{id}")
	@ResponseBody
	@SystemLog(description = "修改banner广告",operCode="banner.update")
	public Map<String, Object> modifyById(@PathVariable("id") String id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		if(StringUtils.isBlank(id)){
			msg.put("status",false);
			return msg;
		}
		try {
			msg = bannerInfoService.selectLinkInfo(id);
			msg.put("status", true);
		} catch (Exception e) {
			log.error("进入修改banner页面失败",e);
			msg.put("status",false);
		}
		return msg;
	}
	
	/**
	 * 保存新增 || 修改后的banner
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveBanner.do")
	@ResponseBody
	@SystemLog(description = "新增banner广告",operCode="banner.insert")
	public Map<String, Object> saveBanner(@RequestBody String param ) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try{
			JSONObject json = JSON.parseObject(param);
			BannerInfo banner = json.getObject("banner", BannerInfo.class);
			int num = bannerInfoService.insertOrUpdate(banner);
			if(num > 0){
				msg.put("status",true);
				msg.put("msg","保存banner成功");
			} else {
				msg.put("status",false);
				msg.put("msg","保存banner失败");
			}
		} catch(Exception e){
			msg.put("status",false);
			msg.put("msg","保存banner失败");
			log.error("保存banner失败",e);
		}
		return msg;
	}

	@RequestMapping(value = "/deleteBanner.do")
	@ResponseBody
	//@SystemLog(description = "删除banner",operCode="banner.recover")
	public Map<String,Object> deleteBanner(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = bannerInfoService.deleteBanner(id);
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
}
