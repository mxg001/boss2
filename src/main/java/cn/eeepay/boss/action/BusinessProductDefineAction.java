package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
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
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.AddRequireItemService;
import cn.eeepay.framework.service.BusinessProductDefineService;
import cn.eeepay.framework.service.BusinessRequireItemService;
import cn.eeepay.framework.service.MerchantBusinessProductService;

@Controller
@RequestMapping(value = "/businessProductDefine")
public class BusinessProductDefineAction {
	private static final Logger log = LoggerFactory.getLogger(ServiceAction.class);

	@Resource
	private BusinessProductDefineService businessProductDefineService;
	
	@Resource
	private AddRequireItemService addRequireItemService;
	
	@Resource
	private MerchantBusinessProductService  merchantBusinessProductService;
	
	@Resource
	private BusinessRequireItemService businessRequireItemService;
	
	
	
	/**
	 * 查询所有的业务产品以及群组号
	 * @author tans
	 * @date 2017年4月13日 上午9:34:08
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfo.do")
	@ResponseBody
	public Object selectAllInfo()  {
		List<BusinessProductDefine> list = null;
		try {
			list = businessProductDefineService.selectAllInfo();
		} catch (Exception e) {
			log.error("查询所有业务产品失败！");
		}
		return list;
	}
	/**
	 * 查询所有的业务产品以及群组号
	 * @author tans
	 * @date 2017年4月13日 上午9:34:08
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfoByTeamId.do")
	@ResponseBody
	public Object selectAllInfoByTeamId(@RequestParam(value="teamId",required=false)String teamId)  {
		List<BusinessProductDefine> list = null;
		try {
			list = businessProductDefineService.selectAllInfoByTeamId(teamId);
		} catch (Exception e) {
			log.error("查询所有业务产品失败！");
		}
		return list;
	}
	/**
	 * 查询所有的业务产品bpId
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfoByBpId")
	@ResponseBody
	public Object selectAllInfoByBpId(@RequestParam("bpId") String bpId)  {
		List<BusinessProductDefine> list = null;
		try {
			if(StringUtil.isBlank(bpId)){
				list = businessProductDefineService.selectAllInfo();
			}else {
				list = businessProductDefineService.selectAllInfoByBpId(Long.valueOf(bpId));
			}
		} catch (Exception e) {
			log.error("查询所有业务产品失败！");
		}
		return list;
	}

	/**
	 * 查询所有的业务产品bpIdand Name
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfoByName")
	@ResponseBody
	public Object selectAllInfoByName(@RequestParam("bpId") String bpId)  {
		List<BusinessProductDefine> list = null;
		try {
			if(StringUtil.isBlank(bpId)){
				list = businessProductDefineService.selectAllInfo();
			}else {
				list = businessProductDefineService.selectAllInfoByName(bpId);
			}
		} catch (Exception e) {
			log.error("查询所有业务产品失败！");
		}
		return list;
	}

	/**
	 * 条件查询业务产品 by tans
	 * 
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectProduct.do")
	public @ResponseBody Object selectProduct(@RequestParam("baseInfo") String param,
			@ModelAttribute("page") Page<BusinessProductDefine> page) throws Exception {
		try {
			BusinessProductDefine bpd = JSON.parseObject(param,BusinessProductDefine.class);
			businessProductDefineService.selectByCondition(page, bpd);
		} catch (Exception e) {
			log.error("条件查询业务产品！", e);
		}
		return page;
	}

	/**
	 * 查询业务产品详情 by tans
	 * 
	 * @param id
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/productDetailCtrl/{id}")
	@ResponseBody
	public Map<String, Object> productDetail(@PathVariable("id") String id) throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			msg = businessProductDefineService.selectDetailById(id);
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询业务产品详情失败！");
			log.error("查询业务产品详情失败！", e);
		}
		return msg;
	}

	/**
	 * 进入增加业务产品页面时，需要携带的数据
	 */
	@RequestMapping(value = "/addProductCtrl")
	@ResponseBody
	public Map<String, Object> addOrUpdate() {
		Map<String, Object> msg = new HashMap<>();
		try {
			String bpid = "";
			msg = businessProductDefineService.selectLinkInfo(bpid);
		} catch (Exception e) {
			log.error("加载增加业务产品页面时失败！", e);
		}
		return msg;
	}

	/**
	 * 进入业务产品的修改时，需要带入的数据
	 */
	@RequestMapping(value = "/editProductCtrl/{id}")
	@ResponseBody
	public Map<String, Object> editProduct(@PathVariable("id") String id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg = productDetail(id);
			Map<String, Object> linkInfo = businessProductDefineService.selectLinkInfo(id);
			List<ServiceInfo> services = (List<ServiceInfo>) msg.get("services");
			List<ServiceInfo> allService = (List<ServiceInfo>) linkInfo.get("allService");
			if(allService!=null){
				allService.addAll(0, services);
			}
			linkInfo.put("allService",allService);
			msg.put("linkInfo", linkInfo);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "进入新增业务产品页面时失败！");
		}

		return msg;
	}

	/**
	 * 保存业务产品的增加 || 修改
	 */
	@RequestMapping(value = "/saveProduct")
	@ResponseBody
	@SystemLog(description = "保存业务产品的增加 || 修改",operCode="businessProductDefine.insert")
	public Map<String, Object> saveProduct(@RequestBody String params) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			JSONObject json = JSON.parseObject(params);
			BusinessProductDefine product = json.getObject("baseInfo", BusinessProductDefine.class);
			if(product!=null){
				int productNameNum = businessProductDefineService.selectExistName(product.getBpId(),product.getBpName(), "productName");
				if(productNameNum > 0){
					msg.put("status", false);
					msg.put("msg", "该业务名称已存在");
					return msg;
				}

				int agentShowNameNum = businessProductDefineService.selectExistName(product.getBpId(),product.getAgentShowName(), "agentShowName");
				if(agentShowNameNum > 0){
					msg.put("status", false);
					msg.put("msg", "该代理商展示名称已存在");
					return msg;
				}

			}
			List<AddRequireItem> items = JSON.parseArray(json.getJSONArray("items").toJSONString(),
					AddRequireItem.class);
			String linkBpId = String.valueOf(product.getLinkProduct());	
			String rely_hardware = String.valueOf(product.getRelyHardware());
			if(!"null".equals(linkBpId)&& StringUtils.isNotBlank(linkBpId)){
				if("0".equals(rely_hardware)){
					msg.put("status", false);
					msg.put("msg", "新增业务产品必须为硬件型");
					return msg;
				}
				boolean isLink = true ;//可以通过
				List<String> listStr=businessRequireItemService.findByProduct(linkBpId);	
				if(listStr == null ||listStr.size()<1){
					msg.put("status", false);
					msg.put("msg", "添加业务产品失败==>所关联的业务产品无进件项！");
					return msg;
				}
				for(String item : listStr){
					boolean falg = false;
					if(items != null && items.size()>0){
						for(AddRequireItem ari :items){//遍历页面上的item
							if(item.equals(String.valueOf(ari.getItemId()))){
								falg = true;
								break;
							}
						}
					}else{
						isLink =false;
						break;
					}
					
					if(!falg){
						isLink =false;
						break;
					}
				}//FOR结束l
				if(!isLink){
					msg.put("status", false);
					msg.put("msg", "添加业务产品失败==>新增业务产品进件项不能少于关联业务产品进件项！");
					return msg;
				}
			}
			
//			List<AppInfo> apps = JSON.parseArray(json.getJSONArray("apps").toJSONString(), AppInfo.class);
			
			List<ServiceInfo> services = JSON.parseArray(json.getJSONArray("services").toJSONString(),
					ServiceInfo.class);
			
			List<HardwareProduct> hards = JSON.parseArray(json.getJSONArray("hards").toJSONString(),
					HardwareProduct.class);
			Map<String, Object> info = new HashMap<>();
			info.put("product", product);
			info.put("services", services);
			info.put("items", items);
			info.put("hards", hards);
//			info.put("apps", apps);
			String resultMsg = "添加";
			if(product.getBpId() != null){
				resultMsg = "修改";
			}
			log.info("action principal.getUsername() : "+principal.getUsername());
			info.put("createPerson",principal.getUsername());
			int num = businessProductDefineService.insertOrUpdate(info);

			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", resultMsg + "业务产品成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", resultMsg + "业务产品失败！");
			}
		} catch (Exception e) {
			log.error("添加 || 修改业务产品失败!", e);
			msg.put("status", false);
			msg.put("msg", "系统异常");
		}

		return msg;
	}

	/**
	 * 查询业务产品 返回业务产品的：id，名称bpName，所属组织名称teamName
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/selectBpTeam.do")
	@ResponseBody
	public List<BusinessProductDefine> selectBpTeam() throws Exception {
		List<BusinessProductDefine> list = null;
		try {
			list = businessProductDefineService.selectBpTeam();
		} catch (Exception e) {
			log.error("查询业务产品名称及组织名称失败", e);
		}
		return list;
	}
	
	/**
	 * 根据业务产品Id，查看能否修改
	 */
	@RequestMapping("/isUsed/{id}")
	@ResponseBody
	public Map<String, Object> isUsed(@PathVariable("id") Integer bpId) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			boolean flag = businessProductDefineService.selectRecord(bpId);
			msg.put("flag", flag);
		} catch (Exception e) {
			log.error("查询业务产品名称及组织名称失败", e);
		}
		return msg;
	}
	
	/**
	 * 修改业务产品的基础数据
	 * @param bpId
	 * @return
	 */
	@RequestMapping("/getProductBase/{id}")
	@ResponseBody
	public Map<String, Object> getProductBase(@PathVariable("id") Integer bpId){
		Map<String, Object> msg = new HashMap<>();
		try {
			msg = businessProductDefineService.getProductBase(bpId);
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询业务产品的基本信息失败");
			log.error("查询业务产品名称及组织名称失败", e);
		}
		return msg;
	}
	
	/**
	 * 保存修改的业务产品基础数据
	 * @param bpId
	 * @return
	 */
	@RequestMapping("/saveProductBase")
	@ResponseBody
	public Map<String, Object> saveProductBase(@RequestBody String params){
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSON.parseObject(params);
			BusinessProductDefine product = json.getObject("baseInfo", BusinessProductDefine.class);
			String bpId = product.getBpId().toString();	
			//BusinessProductDefine bpIdInfo = businessProductDefineService.selectBybpId(bpId);
			//Object linkBpId = bpIdInfo.getLinkProduct();//关联的业务产品ID
			String linkBpId = String.valueOf(product.getLinkProduct());	
			String rely_hardware = String.valueOf(product.getRelyHardware());
			if(!"null".equals(linkBpId)&& StringUtils.isNotBlank(linkBpId)){
				if("0".equals(rely_hardware)){
					msg.put("status", false);
					msg.put("msg", "新增业务产品必须为硬件型");
					return msg;
				}
				List<AddRequireItem> items = JSON.parseArray(json.getJSONArray("items").toJSONString(),AddRequireItem.class);
				boolean isLink = true ;//可以通过
				List<String> listStr=businessRequireItemService.findByProduct(linkBpId.toString());	
				if(listStr == null ||listStr.size()<1){
					msg.put("status", false);
					msg.put("msg", "添加业务产品失败==>所关联的业务产品无进件项！");
					return msg;
				}
				for(String item : listStr){
					boolean falg = false;
					if(items != null && items.size()>0){
						for(AddRequireItem ari :items){//遍历页面上的item
							if(item.equals(String.valueOf(ari.getItemId()))){
								falg = true;
								break;
							}
						}
					}else{
						isLink =false;
						break;
					}
					
					if(!falg){
						isLink =false;
						break;
					}
				}//FOR结束
				if(!isLink){
					msg.put("statue", false);
					msg.put("msg", "添加业务产品失败==>新增业务产品进件项不能少于关联业务产品进件项！");
					return msg;
				}
			}
//			BusinessProductDefine product = json.getObject("baseInfo", BusinessProductDefine.class);
			int num = businessProductDefineService.updateProductBase(params);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "修改成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "修改失败");
			log.error("修改失败", e);
		}
		return msg;
	}
	
	/**
	 * 根据商户服务类型获取对应的业务产品
	 * @author tans
	 * @date 2017年3月15日 上午9:53:47
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getProductByServiceType")
	@ResponseBody
	public Map<String, Object> getProductByServiceType(){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		String[] serviceTypes = {"4","9"};
		List<BusinessProductDefine> bpList = null;
		try {
			bpList = businessProductDefineService.getProductByServiceType(serviceTypes);
			msg.put("bpList", bpList);
			msg.put("status", true);
		} catch (Exception e) {
			log.error("根据服务类型获取对应的业务产品失败", e);
		}
		return msg;
	}

	/**
	 * 查询业务产品
	 * @author	mays
	 * @date	2017年12月19日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getProduct")
	@ResponseBody
	public Map<String, Object> getProduct(){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		List<BusinessProductDefine> bpList = null;
		try {
			bpList = businessProductDefineService.getProduct();
			msg.put("bpList", bpList);
			msg.put("status", true);
		} catch (Exception e) {
			log.error("根据服务类型获取对应的业务产品失败", e);
		}
		return msg;
	}
	
	/**
	 * 根据组织ID获取对应的业务产品以及群组号
	 * @author tans
	 * @date 2017年4月13日 下午2:31:49
	 * @param teamId
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getProductByTeam")
	@ResponseBody
	public Map<String, Object> getProductByTeam(@RequestParam("teamId")String teamId){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		List<BusinessProductDefine> bpList = null;
		try {
			bpList = businessProductDefineService.getProductByTeam(teamId);
			msg.put("bpList", bpList);
			msg.put("status", true);
		} catch (Exception e) {
			log.error("根据服务类型获取对应的业务产品失败", e);
		}
		return msg;
	}
	
	/**
	 * 获取组织内的其他业务产品
	 * @author tans
	 * @date 2017年8月24日 上午10:53:18
	 * @param teamId
	 * @param bpId
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getTeamOtherBp")
	@ResponseBody
	public List<BusinessProductDefine> getTeamOtherBp(@RequestParam("teamId")String teamId,
			@RequestParam(value="bpId",required=false ,defaultValue="")String bpId){
		List<BusinessProductDefine> list = new ArrayList<>();
		try {
			list = businessProductDefineService.getTeamOtherBp(teamId, bpId);
		} catch (Exception e) {
			log.error("获取组织内的其他业务产品失败");
			log.error(e.toString());
		}
		return list;
	}

	/**
	 * create by: tans 2018/7/31 14:18
	 * description:
	 * @return
	 */
	@RequestMapping(value="/updateEffectiveStatus")
	@ResponseBody
	@SystemLog(operCode = "businessProductDefine.updateEffectiveStatus", description = "修改业务产品生效状态")
	public Result updateEffectiveStatus(@RequestBody BusinessProductDefine baseInfo){
		Result result;
		try{
			result = businessProductDefineService.updateEffectiveStatus(baseInfo);
		} catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("修改业务产品生效状态异常", e);
		}
		return result;
	}



	@RequestMapping("/exportQueryProduct")
	@ResponseBody
	@SystemLog(description = "业务产品查询-导出",operCode = "businessProductDefine.exportQueryProduct")
	public List<AppInfo> exportQueryProduct(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response)throws Exception{
		Map<String, Object> msgMap = new HashMap<>();
		try {
			BusinessProductDefine bpd = JSON.parseObject(baseInfo,BusinessProductDefine.class);
			businessProductDefineService.exportQueryProduct(bpd,response,msgMap);
		} catch (Exception e) {
			log.error("业务产品查询-导出出错",e);
			msgMap.put("status", false);
			msgMap.put("msg", "业务产品查询-导出出错！");
		}
		return null;
	}


}
