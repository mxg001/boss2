package cn.eeepay.boss.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.MerchantMigrate;
import cn.eeepay.framework.model.MerchantMigrateInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.MerchantMigrateService;
import cn.eeepay.framework.util.StringUtil;


/**
 * 
 * @author ws
 *  商户迁移
 * @date 2017年2月27日15:15:42
 */
@Controller
@RequestMapping(value="/merchantMigrate")
public class MerchantMigrateAction {
	
	private static final Logger log = LoggerFactory.getLogger(MerchantMigrateAction.class);
	@Resource
	private MerchantMigrateService merchantMigrateService;
	@Resource
	private MerchantInfoService merchantInfoService;
	
	/**
	 * 查询商户迁移记录
	 * @param param 查询组合条件
	 * @return 商户迁移集合
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/findMirate")
	@ResponseBody
	public Object findMirate(@ModelAttribute("page") Page<MerchantMigrate> page,@RequestBody String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			
			JSONObject jsonObject1 = JSONObject.parseObject(param);
			page.setPageNo(Integer.valueOf(jsonObject1.getString("pageNo")));
			page.setPageSize(Integer.valueOf(jsonObject1.getString("pageSize")));
			JSONObject jsonObject = JSONObject.parseObject(jsonObject1.get("param").toString());
			MerchantMigrate mm = new MerchantMigrate();
			mm.setOaNo(jsonObject.getString("oaNo"));
			mm.setCheckStatus(jsonObject.getString("checkStatus"));
			mm.setAgentNo(jsonObject.getString("agentNo"));
			if(null != jsonObject.get("eTime")&&!"".equals(jsonObject.get("eTime").toString())){
				mm.seteTime(Timestamp.valueOf(jsonObject.get("eTime").toString()));
			}
			if(null != jsonObject.get("sTime")&&!"".equals(jsonObject.get("sTime").toString())){
				mm.setsTime(Timestamp.valueOf(jsonObject.get("sTime").toString()));
			}
			mm.setCreatePerson(jsonObject.getString("createPerson"));
			mm.setNodeAgentNo(jsonObject.getString("nodeAgentNo"));
			merchantMigrateService.findMerchantMigrate(page,mm);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!",e);
			e.printStackTrace();
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 加载一级代理商
	 * @return 所有一级代理商
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getAllAgentInfo")
	@ResponseBody
	public List<AgentInfo> getAllAgentInfo(){
		List<AgentInfo> map = new ArrayList<AgentInfo>();
		map = merchantMigrateService.getAgentInfo();
		return map;
	}
	
	/**
	 *  加载一级代理商下所有子代理商
	 * @param agentNo 一级代理商编号
	 * @return 该一级代理商下所有子代理商
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/findNodeAgent")
	@ResponseBody
	public List<AgentInfo> findNodeAgent(@RequestParam String agentNo){
		List<AgentInfo> map = new ArrayList<AgentInfo>();
		map = merchantMigrateService.findNodeAgent(agentNo);
		return map;
	}
	
	
	@RequestMapping(value="/addMigrate")
	@ResponseBody
	public  Object addMerchantMigrate(@RequestParam("file") MultipartFile file, @RequestParam("agentNo") String agentNo, 
			@RequestParam("agentNode")String agentNode, @RequestParam("goSn") String goSn, @RequestParam("oaNo") String oaNo){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //当前用户信息
		if(file.isEmpty()){
			jsonMap.put("addStatus", "2");
			jsonMap.put("addMsg", "新增商户迁移失败,请上传需要迁移的Excel文件");
			return jsonMap;
		}else{
			String fileSuffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			long fileSize = file.getSize();
			String fileName = file.getOriginalFilename();
			if(!fileSuffixName.equals(".xls") && !fileSuffixName.equals(".xlsx")){
				jsonMap.put("addStatus", "2");
				jsonMap.put("addMsg", "新增商户迁移失败,请上传文件后缀为:.xls 或 .xlxs的Excel文件");
				return jsonMap;
			}
			if(((fileSize/1024)/1024) > 4){
				jsonMap.put("addStatus", "2");
				jsonMap.put("addMsg", "上传文件过大,请上传4MB以内的文件");
				return jsonMap;
			}
			Workbook wb=null;
			try {
				wb = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = wb.getSheetAt(0);
				int row_num = sheet.getLastRowNum();
				MerchantMigrate mm = new MerchantMigrate();
				List<MerchantMigrateInfo> mmiList = new ArrayList<MerchantMigrateInfo>();
				String agentParentNode = "";
				String nowAgentNo = "";
				for (int i = 0; i <= row_num; i++) {
					MerchantMigrateInfo mmInfo = new MerchantMigrateInfo();
					int isModifyOneAgentNo = 2; //是否修改一级代理商1.是 2.否 
					Row row = sheet.getRow(i);
					if(null == row || null == row.getCell(0)){
						continue;
					}
					String merchantNo = getCellValue(row.getCell(0));//商户编号
					if(!StringUtil.isEmpty(merchantNo) && !"".equals(merchantNo.trim())){
						AgentInfo agentInfo = merchantMigrateService.getAgentInfoByNo(agentNode);
						if(null == agentInfo || "".equals(agentInfo.getAgentNode())){
							jsonMap.put("addStatus", "2");
							jsonMap.put("addMsg", "目标所属代理商 "+agentNode+" 不存在或该代理商节点不明!");
							return jsonMap;
						}
						if(agentInfo.getAgentType()!=null&&"11".equals(agentInfo.getAgentType())){
							jsonMap.put("addStatus", "2");
							jsonMap.put("addMsg", "超级盟主模式的商户暂时不支持迁移");
							return jsonMap;
						}
						agentParentNode = agentInfo.getAgentNode(); //目标所属代理商节点
						MerchantMigrateInfo mmi = merchantMigrateService.findMerchantMigrateInfo(merchantNo.trim());
						if(null != mmi){
							MerchantMigrate merchantMigrate = merchantMigrateService.getMerchantMigrateDetail(String.valueOf(mmi.getMigrateId()));
							if(!"3".equals(merchantMigrate.getCheckStatus()) && 
									!"5".equals(merchantMigrate.getCheckStatus()) && !"7".equals(merchantMigrate.getCheckStatus())){
								jsonMap.put("addStatus", "2");
								jsonMap.put("addMsg", "提交失败，商户编号 "+merchantNo+" 已在申请单号 "+mmi.getMigrateId()+" 中等待迁移!");
								return jsonMap;
							}
						}
						MerchantInfo merchantInfo = merchantMigrateService.getMerchant(merchantNo.trim());
						if(null == merchantInfo){
							jsonMap.put("addStatus", "2");
							jsonMap.put("addMsg", "提交失败， "+merchantNo+" 不是一个有效商户编号!");
							return jsonMap;
						}
						if("".equals(nowAgentNo)){
							nowAgentNo = merchantInfo.getOneAgentNo();
						}
						
						if(!nowAgentNo.equals(merchantInfo.getOneAgentNo())){
							jsonMap.put("addStatus", "2");
							jsonMap.put("addMsg", "提交失败， 商户 "+merchantNo+" 与附件中其他商户一级代理商不一致!");
							return jsonMap;
						}
						
						if(agentNode.equals(merchantInfo.getAgentNo())){
							jsonMap.put("addStatus", "2");
							jsonMap.put("addMsg", "提交失败， 商户 "+merchantNo+" 已在目标所属代理商名下，无需再迁移!");
							return jsonMap;
						}
						if(!agentNo.equals(merchantInfo.getOneAgentNo())){//涉及变更一级代理商
							isModifyOneAgentNo = 1;
							/*List<String> newOneAgentProduct = merchantMigrateService.findAgentBusProduct(agentNo);
							List<String> oldOneAgentBusProduct = merchantMigrateService.findAgentBusProduct(merchantInfo.getOneAgentNo());
							if(!newOneAgentProduct.containsAll(oldOneAgentBusProduct)){ //校验目标代理商是否代理商当前代理商业务产品
								jsonMap.put("addStatus", "2");
								jsonMap.put("addMsg", "提交失败，商户 "+merchantNo+" 一级代理商未代理目标一级代理商业务产品!");
								return jsonMap;
							}*/
						}
						
						List<String> newAgentProduct = merchantMigrateService.findAgentBusProduct(agentNode);
						List<String> merchantBusProduct = merchantMigrateService.findMerchantBusProduct(merchantInfo.getMerchantNo());
						if(!newAgentProduct.containsAll(merchantBusProduct)){ //校验目标所属代理商是否代理当前商户所属代理商业务产品
							jsonMap.put("addStatus", "2");
							jsonMap.put("addMsg", "提交失败，目标直属代理商未代理,商户 "+merchantNo+" 业务产品!");
							return jsonMap;
						}
						mmInfo.setBeforeAgentNo(merchantInfo.getOneAgentNo());
						mmInfo.setBeforeNodeAgentNo(merchantInfo.getAgentNo());
						mmInfo.setMerchantNo(merchantNo.trim());
						mmInfo.setModifyAgentNo(isModifyOneAgentNo);
						mmiList.add(mmInfo);
					}
				}
				if(mmiList.isEmpty()){
					jsonMap.put("addStatus", "2");
					jsonMap.put("addMsg", "提交失败，未发现需要迁移的商户编号!");
					return jsonMap;
				}
				mm.setAgentNo(agentNo);
				mm.setCreatePerson(principal.getRealName());
				mm.setFileName(fileName);
				mm.setGoSn(Integer.valueOf(goSn));
				mm.setNodeAgentNo(agentNode);
				mm.setOaNo(oaNo);
				merchantMigrateService.addMerchantMigrate(mmiList, mm, agentParentNode);
				jsonMap.put("addStatus", "1");
				jsonMap.put("addMsg", "提交成功!");
				return jsonMap;
			} catch (Exception e) {
				log.error("新增商户迁移失败，系统出现未知异常 = ", e.getMessage());
				jsonMap.put("addStatus", "2");
				jsonMap.put("addMsg", e.getMessage());
				return jsonMap;
			} finally {
				try {
					if(null != wb){
						wb.close();
					}
				} catch (Exception e2) {
					log.error("新增商户迁移关闭工作空间出现异常 = ",e2.getMessage());
					e2.getStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/cheXiaoMigrate")
	@ResponseBody
	public Object cheXiaoMigrate(@RequestParam("id") String id){
		Map<String, String> retMap = new HashMap<String, String>();
		if("".equals(id)){
			retMap.put("flag", "false");
			retMap.put("msg", "非法请求!");
			return retMap;
		}
		MerchantMigrate merchantMigrate = merchantMigrateService.getMerchantMigrateDetail(id);
		if(null == merchantMigrate){
			retMap.put("flag", "false");
			retMap.put("msg", "信息不存在!");
			return retMap;
		}
		if(!"1".equals(merchantMigrate.getCheckStatus()) && !"2".equals(merchantMigrate.getCheckStatus())){ // 
			retMap.put("flag", "false");
			retMap.put("msg", "商户迁移状态不支持撤销!");
			return retMap;
		}
		try {
			merchantMigrateService.cheXiaoMigrate(id);
			retMap.put("flag", "true");
			retMap.put("msg", "撤销成功!");
		} catch (Exception e) {
			log.error("商户迁移出现未知异常 = " + e.getMessage());
			retMap.put("flag", "false");
			retMap.put("msg", "商户迁移出现未知错误!");
			return retMap;
		}
		return retMap;
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkMigrateModify")
	@ResponseBody
	public Object checkMigrate(@RequestBody String param ) throws Exception{
		Map<String, Object> retM = new HashMap<>();
		retM.put("msg", "非法请求");
		if(!"".equals(param)){
			try {
				JSONObject json = JSONObject.parseObject(param);
				String id = json.getString("id");
				String checkStatus = json.getString("checkStatus");
				String remark = "";
				if(null != json.getString("remark")){
					remark = json.getString("remark");
				}
				if(checkStatus.equals("3") && "".equals(remark)){
					retM.put("msg", "请输入审核意见");
					return retM;
				}
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //当前用户信息
				int count = merchantMigrateService.merchantMigrateCheck(checkStatus, remark, principal.getRealName(), id);
				retM.put("msg", "审核成功");
				retM.put("status", count);
				return retM;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("审核出现异常="+e.getMessage());
				return "审核出现异常";
			}
		}
		return retM;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/migrateCheck/{id}")
	@ResponseBody
	public Map<String, Object> migrateCheck(@ModelAttribute("page") Page<MerchantMigrateInfo> page,@PathVariable("id") String id){
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		MerchantMigrate merchantMigrate = merchantMigrateService.getMerchantMigrateDetail(id);
		List<MerchantMigrateInfo> infoList = new ArrayList<MerchantMigrateInfo>();
		if(null != merchantMigrate){
			infoList = merchantMigrateService.getMerchantMigrateInfoList(String.valueOf(merchantMigrate.getId()), page);
		}
		merchantMap.put("merchantMigrate", merchantMigrate);
		merchantMap.put("list", infoList);
		merchantMap.put("page", page);
		return merchantMap;
	}
	
	
	
	public String getCellValue(Cell cell){
		 switch (cell.getCellType()) {
	  		case Cell.CELL_TYPE_NUMERIC:
	  			return String.valueOf(cell.getNumericCellValue());
	  		case Cell.CELL_TYPE_STRING:
	  			return cell.getStringCellValue();
	  		case Cell.CELL_TYPE_BLANK:
	  			return "";
	  		case Cell.CELL_TYPE_BOOLEAN:
	  			return String.valueOf(cell.getBooleanCellValue());
	  		case Cell.CELL_TYPE_FORMULA:
	  			return cell.getStringCellValue();
		 }
		return null;
	}

}
