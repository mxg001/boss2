package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.MobileVerInfo;
import cn.eeepay.framework.service.AppInfoService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping(value="/appInfoAction")
public class AppInfoAction {
	private static final Logger log = LoggerFactory.getLogger(AppInfoAction.class);
	
	@Resource
	private AppInfoService appInfoService;
	
	/**
	 * 初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<AppInfo> page,@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			AppInfo ais = JSON.parseObject(param,AppInfo.class);
			appInfoService.selectAllInfo(page, ais);
//			for (int i = 0; i < page.getResult().size(); i++) {
//				if(page.getResult().get(i).getParenName()==null||page.getResult().get(i).getParenName().equals("")){
//					page.getResult().remove(i);
//					i--;
//					continue;
//				}
//			}
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("二维码查询报错",e);
			System.out.println(e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "查询报错");
		}
		return jsonMap;
	}
	
	/**
	 * 子表初始化和分页
	 * @param page
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectChildAllInfo")
	@ResponseBody
	public Object selectChildAllInfo(@ModelAttribute("page")Page<MobileVerInfo> page,@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			String id=JSON.parseObject(ids,String.class);
			System.out.println("aaa========="+ids+"\tss===="+id);
			appInfoService.selectChildAllInfo(page, id);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("二维码历史查询报错",e);
			System.out.println(e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "二维码历史查询查询报错");
		}
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/findAppInfo")
	@ResponseBody
	public Object findAppInfo(String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			String id=JSON.parseObject(ids,String.class);
			AppInfo ais=appInfoService.findInfo(id);
			jsonMap.put("data", ais);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("查询二维码信息报错",e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "查询二维码信息报错");
		}
		return jsonMap;
	}
	
	/**
	 * 二维码历史记录详情
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/findChildDetailInfo")
	@ResponseBody
	public Object findChildDetailInfo(String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			String id=JSON.parseObject(ids,String.class);
			MobileVerInfo mvi=appInfoService.findChildDetailInfo(id);
			if(mvi.getUrl()!=null){
				mvi.setLogUrl(mvi.getUrl());
				Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
				String content = mvi.getUrl();
				String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
				mvi.setUrl(newContent);
			}
			if(mvi.getAppLogo()!=null){
				mvi.setLogAppLogo(mvi.getAppLogo());
				Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
				String content = mvi.getAppLogo();
				String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
				mvi.setAppLogo(newContent);
			}
			jsonMap.put("data", mvi);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("查询二维码信息报错",e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "查询二维码信息报错");
		}
		return jsonMap;
	}
	
	//新增和修改
	@RequestMapping(value="/addOrUpAppChildInfo")
	@ResponseBody
	@SystemLog(description = "新增或修改appInfo版本",operCode="appInfoAction.addOrUpAppChildInfo")
	public Object addOrUpAppChildInfo(@RequestBody String param) throws Exception{
		Map<String, Object> jsonMap=new HashMap<>();
		JSONObject json=JSON.parseObject(param);
		try{
			MobileVerInfo mvi=json.getObject("info", MobileVerInfo.class);
			String isn=json.getString("isn");
			int num=0;
			if(isn.equals("0")){//新增
				mvi.setCreateTime(new Date());
				num=appInfoService.insertMviInfo(mvi);
			}else{//修改
				num=appInfoService.updateMviInfo(mvi);
			}
			if(num>0){
				jsonMap.put("result", true);
			}else{
				jsonMap.put("result", false);
			}
		}catch(Exception ex){
			log.error("二维码新增出错------",ex);
			System.out.println(ex);
			jsonMap.put("result", false);
		}
		return jsonMap;
	}
	
	/**
	 * 详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByParam")
	@ResponseBody
	public Object selectByParam(String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(ids,Integer.class);
			AppInfo ais=appInfoService.selectDetail(id);
			if(ais.getCodeUrl()!=null){
				ais.setLogUrl(ais.getCodeUrl());
				Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
				String content = ais.getCodeUrl();
				String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
				ais.setCodeUrl(newContent);
			}
			if(ais.getParenName()==null||ais.getParenName().equals("")){
				ais.setParenName(ais.getAppName());
			}
			jsonMap.put("data", ais);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("二维码详情报错",e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "报错");
		}
		return jsonMap;
	}

	@RequestMapping(value="/selectInfoBox")
	@ResponseBody
	public Object selectInfoBox(String id)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			List<AppInfo> ais=null;
			if(id==null||id.equals("")){
				ais=appInfoService.selectInfoBox();
				jsonMap.put("d", "0");
			}else{
				ais=appInfoService.selectInfoBoxName(id);
				for (int i = 0; i < ais.size(); i++) {
					if(ais.get(i).getCodeUrl()!=null){
						ais.remove(i);
						i--;
					}
				}
				jsonMap.put("d", "1");
			}
			jsonMap.put("data", ais);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "报错");
		}
		return jsonMap;
	}
	
	//新增
	@RequestMapping(value="/addAppInfo")
	@ResponseBody
	@SystemLog(description = "新增APP二维码",operCode="appQrcode.insert")
	public Object addAppInfo(@RequestBody String param) throws Exception{
		Map<String, Object> jsonMap=new HashMap<>();
		JSONObject json=JSON.parseObject(param);
		try{
			AppInfo appInfo=json.getObject("info", AppInfo.class);
			AppInfo parentInfo=appInfoService.selectInfo(Integer.valueOf(appInfo.getParentId()));//上级
			AppInfo updateInfo=appInfoService.selectInfos(appInfo.getAppName(), parentInfo.getId().toString());
			int num=0;
			if(updateInfo==null){
				//appNo必须是数字
				if(!StringUtils.isNumeric(appInfo.getAppNo())){
					jsonMap.put("result", false);
					jsonMap.put("msg", "appNo必须是数字");
					return jsonMap;
				}
				//隐私协议版本号必須是数字
				if(!StringUtils.isNumeric(appInfo.getProtocolVer()+"")){
					jsonMap.put("result", false);
					jsonMap.put("msg", "隐私政策版本号必须是数字");
					return jsonMap;
				}
				//team_id和appNo组合唯一
				if(!appInfoService.checkUniqueApp(appInfo.getTeamId(), appInfo.getAppNo())){
					jsonMap.put("result", false);
					jsonMap.put("msg", "组织ID和appNo要组合唯一");
					return jsonMap;
				}
				appInfo.setTeamId(parentInfo.getTeamId());
				appInfo.setTeamName(parentInfo.getTeamName());
				appInfo.setParentId(parentInfo.getId().toString());
				num = appInfoService.insert(appInfo);
			}else{
				updateInfo.setCodeUrl(appInfo.getCodeUrl());
				num = appInfoService.update(updateInfo);
			}
			if(num>0){
				jsonMap.put("result", true);
				jsonMap.put("msg", "操作成功");
			}else{
				jsonMap.put("result", false);
				jsonMap.put("msg", "操作失败");
			}
		}catch(Exception ex){
			log.error("二维码新增出错------",ex);
			System.out.println(ex);
			jsonMap.put("result", false);
		}
		return jsonMap;
	}
	
	//修改
	@RequestMapping(value="/upAppInfo")
	@ResponseBody
	@SystemLog(description = "修改APP二维码",operCode="appQrcode.update")
	public Object upAppInfo(@RequestBody String param) throws Exception{
		Map<String, Object> jsonMap=new HashMap<>();
		JSONObject json=JSON.parseObject(param);
		try{
			AppInfo ais=json.getObject("info", AppInfo.class);
			String url=json.getString("img");
			AppInfo ais1=appInfoService.selectInfo(ais.getId());
			ais1.setAppName(ais.getAppName());
			ais1.setParentId(ais.getParentId());
			ais1.setCodeUrl(url);
			ais1.setProtocolVer(ais.getProtocolVer());
			int num=0;
			num = appInfoService.update(ais1);
			if(num>0){
				jsonMap.put("result", true);
			}else{
				jsonMap.put("result", false);
			}
		}catch(Exception ex){
			log.error("二维码新增出错------",ex);
			System.out.println(ex);
			jsonMap.put("result", false);
		}
		return jsonMap;
	}

	//获取app_info列表信息
	@RequestMapping(value="/getAllAppInfo")
	@ResponseBody
	public  Map<String,Object>  getAllAppInfo() {
		Map<String, Object> msg=new HashMap<>();
		try {
			List<AppInfo> list=appInfoService.getAllAppInfo();
			msg.put("status", true);
			msg.put("list", list);
		} catch (Exception e) {
			log.error("获取app_info列表信息异常!",e);
			msg.put("status", false);
			msg.put("msg", "获取app_info列表信息异常!");
		}
		return msg;
	}
}
