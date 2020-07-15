package cn.eeepay.boss.action;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;


import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.HardwareProductService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/hardwareProduct")
public class HardwareProductAction {

	private static final Logger log = LoggerFactory.getLogger(HardwareProductAction.class);
	
	@Resource
	private HardwareProductService hardwareProductServiceImpl;
	@Resource
	private SysDictService sysDictService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfo.do")
	public @ResponseBody Object selectAllInfo(@RequestParam(value="item",required=false)String item) {
		List<HardwareProduct> list = hardwareProductServiceImpl.selectAllInfo(item);
		for (HardwareProduct hardwareProduct : list) {
			hardwareProduct.setTypeName(hardwareProduct.getTypeName() + hardwareProduct.getVersionNu());
		}
		return list;
	}
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfoByTeamId.do")
	public @ResponseBody Object selectAllInfoByTeamId(@RequestParam(value="teamId",required=false)String teamId) {
		List<HardwareProduct> list = hardwareProductServiceImpl.selectAllInfoByTeamId(teamId);
		for (HardwareProduct hardwareProduct : list) {
			hardwareProduct.setTypeName(hardwareProduct.getTypeName() + hardwareProduct.getVersionNu());
		}
		return list;
	}

	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectHard.do")
	public @ResponseBody Object selectProduct(@RequestParam("baseInfo") String param,
			@ModelAttribute("page") Page<HardwareProduct> page) throws Exception {
		try {
			HardwareProduct bpd = JSON.parseObject(param,HardwareProduct.class);
			hardwareProductServiceImpl.selectByCondition(page, bpd);
		} catch (Exception e) {
			log.error("条件查询硬件产品！", e);
		}
		return page;
	}
	
	
	
	
	/**
	 * 硬件产品新增 by sober
	 */
	@RequestMapping(value = "/addHard")
	@ResponseBody
	@SystemLog(description = "硬件产品新增",operCode="service.insertHard")
	public Object addHard(@RequestBody String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			HardwareProduct hardwareProduct = JSON.parseObject(param,HardwareProduct.class);
			List<HardwareProduct> hps = hardwareProductServiceImpl.selectByParams(hardwareProduct.getTypeName(),hardwareProduct.getVersionNu());
			if (hps.size() > 0) {
				jsonMap.put("msg", "所添加硬件产品已存在");
				jsonMap.put("bols", false);

			}else {
				int i = 0;
				if (hardwareProduct.getTypeName() != null && hardwareProduct.getVersionNu() != null
						&& hardwareProduct.getFacturerCode() != null && hardwareProduct.getManufacturer() != null
						&&hardwareProduct.getModel()!=null
						) {
					HardwareProduct hardProduct = new HardwareProduct();
					hardProduct.setTypeName(hardwareProduct.getTypeName());
					hardProduct.setVersionNu(hardwareProduct.getVersionNu());
					hardProduct.setFacturerCode(hardwareProduct.getFacturerCode());
					hardProduct.setManufacturer(hardwareProduct.getManufacturer());
					hardProduct.setModel(hardwareProduct.getModel());
					hardProduct.setOrgId(hardwareProduct.getOrgId());
					hardProduct.setSecretType(hardwareProduct.getSecretType());
					if(StringUtils.isNotBlank(hardwareProduct.getTeamEntryId())){
						hardProduct.setTeamEntryId(hardwareProduct.getTeamEntryId());
					}
					if(principal!=null){
						hardProduct.setCreatePerson(principal.getUsername());
					}
					hardProduct.setCreateTime(new Date());
					hardProduct.setDevicePn(hardwareProduct.getDevicePn());

					i = hardwareProductServiceImpl.addHard(hardProduct);
				}
				if (i > 0) {
					jsonMap.put("msg", "添加成功");
					jsonMap.put("bols", true);
				} else {
					jsonMap.put("msg", "添加失败");
					jsonMap.put("bols", false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("msg", "系统异常");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	@RequestMapping(value = "/updateHard")
	@ResponseBody
	@SystemLog(description = "硬件产品修改",operCode="service.updateHard")
	public Object updateHard(@RequestBody String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			HardwareProduct hardwareProduct = JSON.parseObject(param,HardwareProduct.class);
			List<HardwareProduct> hps = hardwareProductServiceImpl.selectByParamsUpdate(hardwareProduct.getTypeName(),hardwareProduct.getVersionNu(),hardwareProduct.getHpId());
			if (hps.size() > 0) {
				jsonMap.put("msg", "所添加硬件产品已存在");
				jsonMap.put("bols", false);

			}else {
				int i = 0;
				if (hardwareProduct.getTypeName() != null && hardwareProduct.getVersionNu() != null
						&& hardwareProduct.getFacturerCode() != null && hardwareProduct.getManufacturer() != null&&hardwareProduct.getModel()!=null) {
					if(!StringUtils.isNotBlank(hardwareProduct.getTeamEntryId())){
						hardwareProduct.setTeamEntryId(null);
					}
					i = hardwareProductServiceImpl.updateHard(hardwareProduct);
				}
				if (i > 0) {
					jsonMap.put("msg", "更新成功");
					jsonMap.put("bols", true);
				} else {
					jsonMap.put("msg", "更新失败");
					jsonMap.put("bols", false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("msg", "更新异常");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	
	
	@RequestMapping(value = "/queryHardById/{id}")
	@ResponseBody
	public Map<String, Object> queryHardById(@PathVariable("id") String id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		if (StringUtils.isBlank(id)) {
			msg.put("status", false);
			return msg;
		}
		try {
			msg = hardwareProductServiceImpl.queryHardById(id);
			msg.put("status", true);
		} catch (Exception e) {
			log.error("进入修改广告详情页面失败", e);
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 机具活动考核查询页面硬件产品下拉框数据
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/selectAllHardwareProduct")
	@ResponseBody
	public Map<String, Object> selectAllHardwareProduct(){
		Map<String, Object> map = new HashMap<>();
		try {
			List<SysDict> sysDicts = sysDictService.selectByKey("TER_ACTIVITY_DEVICE_PN");
			List<HardwareProduct> list = hardwareProductServiceImpl.selectAllHardwareProduct(sysDicts);
			for (HardwareProduct hardwareProduct : list) {
				hardwareProduct.setTypeName(hardwareProduct.getTypeName() + hardwareProduct.getVersionNu());
			}
			map.put("status", true);
			map.put("list", list);
		} catch (Exception e) {
			map.put("status", false);
			log.error("机具活动考核查询页面硬件产品下拉框数据获取失败 ");
			e.printStackTrace();
		}
		return map;
	}
}
