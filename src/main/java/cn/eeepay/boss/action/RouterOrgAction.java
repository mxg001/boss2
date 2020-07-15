package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TransRouteGroupAcqMerchant;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.service.RouterOrgService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 集群中收单商户action
 * 
 * @author junhu
 *
 */
@SuppressWarnings("all")
@Controller
@RequestMapping(value = "/routerOrg")
public class RouterOrgAction {

	private static final Logger log = LoggerFactory.getLogger(RouterOrgAction.class);

	@Resource
	private RouterOrgService routerOrgService;
	
	@Autowired
	private RedisService redisService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryRouterAcqMerchantList.do", method = RequestMethod.POST)
	@ResponseBody
	public Page<Map> queryRouterAcqMerchantList(@RequestParam("info") String param,
			@ModelAttribute("page") Page<Map> page) {
		try {
			Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
			routerOrgService.listRouterAcqMerchantByCon(jsonMap, page);
		} catch (Exception e) {
			log.error("查询集群收单商户列表失败！", e);
		}
		return page;
	}

	@RequestMapping(value = "/delRouterAcqMerchant.do", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(description = "删除收单商户",operCode="routerOrg.delete")
	public Map<String, Object> delRouterAcqMerchant(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			Long id = json.getObject("id", Long.class);
			int num = routerOrgService.deleteRouterAcqMerchantById(id);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "删除成功！");
			}
		} catch (Exception e) {
			log.error("删除失败！", e);
			msg.put("status", false);
			msg.put("msg", "删除失败！");
		}
		return msg;
	}

	@RequestMapping(value = "/updateAcqMerchantQuota.do", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(description = "集群中收单商户修改限额",operCode="routerOrg.updateQuota")
	public Map<String, Object> updateAcqMerchantQuota(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
			int num = routerOrgService.updateAcqMerchantQuota(jsonMap);
			List<String> keyList = new ArrayList<>();
			keyList.add("acqMerchantQuota:"+jsonMap.get("acq_merchant_no"));
			redisService.delete(keyList);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			log.error("修改失败！", e);
			msg.put("status", false);
			msg.put("msg", "修改失败！");
		}
		return msg;
	}
	//====集群中收单商户批量删除==sober========
	@RequestMapping(value = "/deleteBatch")
	@ResponseBody
	public Object deleteBatch(@RequestBody String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			List<TransRouteGroupAcqMerchant> list = JSON.parseArray(param, TransRouteGroupAcqMerchant.class);
			int num = 0;
			for (TransRouteGroupAcqMerchant transRouteGroupAcqMerchant : list) {
				num += routerOrgService.deleteRouterAcqMerchantById(transRouteGroupAcqMerchant.getId().longValue());
			}
			if (num > 0) {
				jsonMap.put("msg", "操作成功");
				jsonMap.put("bols", true);
			} else {
				jsonMap.put("bols", false);
				jsonMap.put("msg", "操作失败");
			}
		} catch (Exception e) {
			log.error("操作失败！", e);
			e.printStackTrace();
			jsonMap.put("msg", "操作失败");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	//收单商户导出  tgh
	@RequestMapping(value="/exportInfo.do")
	@ResponseBody
	@SystemLog(description = "收单商户导出",operCode="routerOrg.export")
	public void exportInfo(@RequestParam("info") String param,HttpServletResponse response) throws Exception{
		Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
		OutputStream ouputStream = null;
		try {
			List<Map<String,Object>> list=routerOrgService.selecrAllInfoRecordInfo(jsonMap);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
			String fileName = "集群中收单商户导出"+sdf.format(new Date())+".xls" ;
			String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);   
			List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
			if(list!=null){
				for (int i = 0; i < list.size(); i++) {
					Map<String,Object> map = list.get(i);
					Map<String,String> map1=new HashMap<>();
					map1.put("acqMerchantNo", StringUtil.filterNull(map.get("acq_merchant_no")));
					map1.put("acqMerchantName", StringUtil.filterNull(map.get("acq_merchant_name")));
					map1.put("acqMerchantTypeStr", StringUtil.filterNull(map.get("acq_merchant_type_str")));
					map1.put("groupCode", StringUtil.filterNull(map.get("group_code")));
					map1.put("groupName", StringUtil.filterNull(map.get("group_name")));
					map1.put("acqEnname", StringUtil.filterNull(map.get("acq_enname")));
					map1.put("agentName", StringUtil.filterNull(map.get("agent_name")));
					map1.put("province", StringUtil.filterNull(map.get("province")));
					map1.put("city", StringUtil.filterNull(map.get("city")));
					map1.put("j_rate", map.get("j_rate")==null?"":StringUtil.filterNull(map.get("j_rate"))+"%");
					map1.put("x_rate", map.get("x_rate")==null?"":StringUtil.filterNull(map.get("x_rate"))+"%");
					data.add(map1);
				}
			}
			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[]{"acqMerchantNo","acqMerchantName","acqMerchantTypeStr","groupCode","groupName","acqEnname","agentName","province","city","j_rate","x_rate"};
			String[] colsName = new String[]{"收单机构商户编号","商户名称","收单机构商户类别","集群编号","集群名称","收单机构","代理商名称","经营省份","经营市区","收单服务借记卡费率","收单服务贷记卡费率"};
			ouputStream = response.getOutputStream();    
			export.export(cols, colsName, data, ouputStream);
		} catch (Exception e) {
			log.error("收单机构导出异常:",e);
		} finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}
		
		
	}

	/**
	 * 集群中收单商户导入删除模板下载
	 */
	@RequestMapping("/routerOrgBatchDeleteTemplate")
	public String routerOrgBatchDeleteTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"routerOrgBatchDeleteTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath,"集群中收单商户导入删除模板.xlsx");
		return null;
	}

	/**
	 * 批量关闭导入
	 */
	@RequestMapping(value="/routerOrgBatchDelete")
	@ResponseBody
	@SystemLog(description = "集群中收单商户导入删除", operCode = "routerOrg.routerOrgBatchDelete")
	public Map<String, Object> routerOrgBatchDelete(@RequestParam("file") MultipartFile file){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "导入文件格式错误!");
					return msg;
				}
			}
			msg = routerOrgService.routerOrgBatchDelete(file);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "导入失败!");
			log.error("导入失败!",e);
		}
		return msg;
	}

	@RequestMapping(value = "/selectAcqMerCount", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectAcqMerCount(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSON.parseObject(param);
			Integer groupCode = json.getObject("groupCode", Integer.class);
			msg.put("acqMerCount",routerOrgService.selectAcqMerCount(groupCode));
		} catch (Exception e) {
			log.error("查询失败！", e);
		}
		return msg;
	}

	//====集群中收单商户批量删除==sober========
	@RequestMapping(value = "/deleteBatchCount")
	@ResponseBody
	public Object deleteBatchCount(@RequestBody String param) throws Exception {
		List<Map<String, Object>>  jsonlist = new ArrayList<Map<String, Object>>();
		try {
			List<Map> list = JSON.parseArray(param, Map.class);
			jsonlist=routerOrgService.deleteBatchCount(list);
		} catch (Exception e) {
			log.error("操作失败！", e);
			e.printStackTrace();
		}
		return jsonlist;
	}

	//===导入删除统计==sober========
	@RequestMapping(value = "/deleteImportDelCount")
	@ResponseBody
	public Object deleteImportDelCount(@RequestBody String delNo) throws Exception {
		List<Map<String, Object>>  jsonlist = new ArrayList<Map<String, Object>>();
		try {
			jsonlist=routerOrgService.deleteImportDelCount(delNo);
		} catch (Exception e) {
			log.error("操作失败！", e);
		}
		return jsonlist;
	}

	@RequestMapping(value = "/deleteImportDelBatch")
	@ResponseBody
	@SystemLog(description = "集群中收单商户导入删除", operCode = "routerOrg.deleteImportDelBatch")
	public Object deleteImportDelBatch(@RequestBody String delNo) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {

			int num = 0;
			num = routerOrgService.deleteImportDelBatch(delNo);
			if (num > 0) {
				jsonMap.put("msg", "操作成功");
				jsonMap.put("bols", true);
			} else {
				jsonMap.put("bols", false);
				jsonMap.put("msg", "操作失败");
			}
		} catch (Exception e) {
			log.error("操作失败！", e);
			jsonMap.put("msg", "操作失败");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	@RequestMapping(value = "/deleteAcqMerImportDel")
	@ResponseBody
	public Object deleteAcqMerImportDel(@RequestBody String delNo) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {

			int num = 0;
			num = routerOrgService.deleteAcqMerImportDel(delNo);
			if (num > 0) {
				jsonMap.put("msg", "操作成功");
				jsonMap.put("bols", true);
			} else {
				jsonMap.put("bols", false);
				jsonMap.put("msg", "操作失败");
			}
		} catch (Exception e) {
			log.error("操作失败！", e);
			e.printStackTrace();
			jsonMap.put("msg", "操作失败");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

}