package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.model.AreaInfo;
import cn.eeepay.framework.service.AreaInfoService;

/**
 * 地区信息action
 * 
 * @author junhu
 *
 */
@Controller
@RequestMapping("/areaInfo")
public class AreaInfoAction {

	private static final Logger log = LoggerFactory.getLogger(AreaInfoAction.class);
	@Resource
	private AreaInfoService areaInfoService;

//	@RequestMapping(value = "/provinceSelectBox")
//	public @ResponseBody List<Map<String,Object>> provinceSelectBox() {
//		return areaInfoService.provinceSelectBox();
//	}
//
//	@RequestMapping(value = "/citySelectBox")
//	public @ResponseBody List<Map<String,Object>> citySelectBox(@RequestBody String param) {
//		JSONObject jsonObject = JSON.parseObject(param);
//		return areaInfoService.citySelectBox(jsonObject.getString("province"));
//	}
//
	//没用到此接口，备用
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getAreaByParentId")
	@ResponseBody
	public List<Map<String,Object>> getAreaByParentId(@RequestParam Integer id) {
		try{
			return areaInfoService.getItemByParentId(id);
		}catch(Exception e){
			log.error("获取城市列表异常",e);
		}
		return new ArrayList<Map<String,Object>>();
	}
	
//	@RequestMapping(value = "/getAreaByNames")
//	@ResponseBody
//	public Map<String,Object> getAreaByNames(@RequestParam String province,@RequestParam String city,
//			@RequestParam String area) {
//		try{
//			return areaInfoService.getAreaByNames(province,city,area);
//		}catch(Exception e){
//			log.error("获取城市列表异常",e);
//		}
//		return null;
//	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getAreaByName")
	@ResponseBody
	public List<Map<String,Object>> getAreaByName(@RequestParam("type") String type,@RequestParam("name") String name) {
		try{
			return areaInfoService.getAreaByName(type,name);
		}catch(Exception e){
			log.error("获取城市列表异常",e);
		}
		return new ArrayList<Map<String,Object>>();
	}

	//没用到此接口，备用
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getAreaCityByParentId")
	@ResponseBody
	public List<Map<String,Object>> getAreaCityByParentId(@RequestBody String params) {
		try{
			JSONObject json = JSON.parseObject(params);
			List<AreaInfo> provinceList = JSON.parseArray(json.getJSONArray("provinceList").toJSONString(),AreaInfo.class);
			if(provinceList!=null&&provinceList.size()>0){
				List<Long> listStr=new ArrayList<Long>();
				for(AreaInfo ai:provinceList){
					listStr.add(ai.getId());
				}
				List<Map<String,Object>> list=areaInfoService.getItemByParentIds(ListToString(listStr)) ;
				return list;
			}
		}catch(Exception e){
			log.error("获取市列表异常",e);
		}
		return new ArrayList<Map<String,Object>>();
	}

	private String ListToString (List<Long> list){
		StringBuffer sb=new StringBuffer();
		if(list!=null&&list.size()>0){
			for(Long ll:list){
				sb.append(ll.toString()+",");
			}
			sb.setLength(sb.length()-1);
		}
		return sb.toString();
	}
}
