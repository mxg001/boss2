package cn.eeepay.boss.action;

import cn.eeepay.boss.action.MerchantBusinessProductAction.SelectParams;
import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.TerNoIsNullException;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.importLog.ImportLog;
import cn.eeepay.framework.model.TerActivityCheck;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.allAgent.AgentTerminalService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(value = "/terminalInfo")
public class TerminalInfoAction {

	public static final String CS_KEK="6F32F43CCF2DBE1204D5BEFBEBA20C72";

	public String url = "http://192.168.3.180:8099/boss2/terminalInfo/qrcod?qrCode=";

	private static final Logger log = LoggerFactory.getLogger(TerminalInfoAction.class);

	@Resource
	public TerminalInfoService terminalInfoService;

	@Resource
	public AgentInfoService agentInfoService;

	@Resource
	private HardwareProductService hardwareProductService;

	@Resource
	private SecretKeyService secretKeyService;

	@Resource
	private SysDictService sysDictService;

	@Resource
	private ActivityGroupService activityGroupService;

	// 商户业务产品
	@Resource
	private MerchantBusinessProductService merchantBusinessProductService;

	@Resource
	private AgentTerminalService agentTerminalService;

	@Resource
	private TerminalInfoUnService terminalInfoUnService;

	@Resource
	private AcqTerminalStoreService acqTerminalStoreService;
	@Resource
	private ActivityService activityService;

//	// 初始化查询
//	@DataSource(Constants.DATA_SOURCE_SLAVE)
//	@RequestMapping(value = "/selectAllInfo.do")
//	public @ResponseBody Object selectAllInfo(@ModelAttribute("page") Page<TerminalInfo> page) {
//		List<TerminalInfo> list = null;
//		try {
//			list = terminalInfoService.selectAllInfo(page);
//		} catch (Exception e) {
//			log.error("初始化失败-----", e);
//			return false;
//		}
//
//		return page;
//	}

	// 详情查询
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectObjInfo.do")
	public @ResponseBody Object selectObjInfo(String ids) {
		Long id = Long.valueOf(ids);
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "查询机具失败");
		try {
			TerminalInfo terminalInfo = terminalInfoService.selectObjInfo(id);
			if (terminalInfo.getType() != null) {
				terminalInfo.setTypeName(terminalInfo.getTypeName() + terminalInfo.getVersionNu());
			}
			msg = activityGroupService.getAllActivityGroup();
			msg.put("terminalInfo", terminalInfo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询机具失败", e);
			return false;
		}
		return msg;
	}

	// 条件查询
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByCondition.do")
	public @ResponseBody Object selectByCondition(@RequestParam("info") String param,
			@ModelAttribute("page") Page<TerminalInfo> page) {
		Map<String, Object> jsonMap = new HashMap<>();
		TerminalInfo terminalInfo = JSON.parseObject(param, TerminalInfo.class);
		try {
			conditionTransformation(terminalInfo);
			terminalInfoService.selectByParam(page, terminalInfo);
			jsonMap.put("list", page);
			jsonMap.put("result", true);
		} catch (Exception e) {
			log.error("机具条件查询出错-----", e);
			jsonMap.put("result", false);
			jsonMap.put("list", page);
		}

		return jsonMap;
	}

	/**
	 * 机具活动考核按条件分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	@RequestMapping("/selectWithTACByCondition")
	@ResponseBody
	public Map<String, Object> selectWithTACByCondition(@RequestParam("info") String param,
														@ModelAttribute("page") Page<TerminalInfo> page){
		Map<String, Object> map = new HashMap<>();
		try {
			TerminalInfo terminalInfo = JSON.parseObject(param, TerminalInfo.class);
			if(StringUtils.isNotBlank(terminalInfo.getAgentNo()) &&  StringUtils.isNotBlank(terminalInfo.getBool())){
				AgentInfo ai = agentInfoService.selectByagentNo(terminalInfo.getAgentNo());
				if("1".equals(terminalInfo.getBool())){
					terminalInfo.setAgentNo(ai.getAgentNode()+"%");
				}
			}
			terminalInfoService.selectWithTACByCondition(page, terminalInfo);
			map.put("status", true);
			map.put("page", page);
		} catch (Exception e) {
			map.put("status", false);
			map.put("msg", "查询失败");
			log.error("机具活动考核按条件分页查询失败！");
			e.printStackTrace();
		}
		return map;

	}


	/**
	 * 导出机具列表
	 */
	@RequestMapping(value="/importDetail")
	@ResponseBody
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> msg=new HashMap<>();
		try {
			//param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
			TerminalInfo terminalInfo = JSON.parseObject(param, TerminalInfo.class);
			conditionTransformation(terminalInfo);
			List<TerminalInfo> list=terminalInfoService.importDetailSelect(terminalInfo);
			terminalInfoService.importDetail(list,response);
		}catch (Exception e){
			log.error("导出机具列表异常!",e);
			msg.put("status", false);
			msg.put("msg", "导出机具列表异常!");
		}
		return msg;
	}

	/**
	 * 机具活动考核导出
	 * @param info
	 * @param response
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportTerminalWithTAC")
	public void exportTerminalWithTAC(String info, HttpServletResponse response){
		try {
			//info = URLDecoder.decode(info, "utf-8");
			TerminalInfo terminalInfo = JSONObject.parseObject(info, TerminalInfo.class);
			if(StringUtils.isNotBlank(terminalInfo.getAgentNo()) &&  StringUtils.isNotBlank(terminalInfo.getBool())){
				AgentInfo ai = agentInfoService.selectByagentNo(terminalInfo.getAgentNo());
				if("1".equals(terminalInfo.getBool())){
					terminalInfo.setAgentNo(ai.getAgentNode()+"%");
				}
			}
			terminalInfoService.exportTerminalWithTAC(response, terminalInfo);
		} catch (Exception e) {
			log.error("机具活动考核查询页面导出失败", e);
		}
	}

	/**
	 * 条件转换
	 */
	private  void conditionTransformation(TerminalInfo terminalInfo){
		if (!"".equals(terminalInfo.getAgentNo()) && !"-1".equals(terminalInfo.getAgentNo())) {
			AgentInfo ais = agentInfoService.selectByagentNo(terminalInfo.getAgentNo());
			if (ais != null) {
				if (terminalInfo.getBool().equals("0")) {
					terminalInfo.setBool(null);
				} else {
					terminalInfo.setAgentName(terminalInfo.getAgentName() + "%");
					terminalInfo.setBool(ais.getAgentNode() + "%");
				}
			} else {
				terminalInfo.setBool(null);
			}
		} else {
			terminalInfo.setBool(null);
		}
	}

	@RequestMapping(value = "/applyReturn.do")
	@ResponseBody
	@SystemLog(description = "申请回收机具", operCode = "terminal.applyRecover")
	public Object applyReturn(@RequestBody String params) {
		Map<String, Object> msg = new HashMap<>();
		List<String> ids = JSONObject.parseArray(params, java.lang.String.class);
		if (ids == null || ids.size() <=0) {
			msg.put("status", false);
			msg.put("msg", "没有可回收的机具");
		}
		try {
			String opentStatus = "0";
			int i = terminalInfoService.updateOpenStatusBatch(ids, opentStatus);
			if (i > 0) {
				msg.put("status", true);
				msg.put("msg", "操作成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "操作失败");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "操作失败");
			log.error("批量机具回归分配", e);
		}
		return msg;
	}

	// 新增
	@RequestMapping(value = "/addTerminalInfo.do")
	@SystemLog(description = "机具新增", operCode = "terminal.insert")
	public @ResponseBody Object addTerminalInfo(@RequestBody TerminalInfo terminalInfo) {
		Map<String, Object> jsonMap = new HashMap<>();
		List<TerminalInfo> terminalInfo1 = null;
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			//验证欢乐返子类型
			if(StringUtils.isNotBlank(terminalInfo.getActivityTypeNo())) {
				HlfHardware hlfHardware = new HlfHardware();
				hlfHardware.setHardId(Long.valueOf(terminalInfo.getType()));
				hlfHardware.setActivityTypeNo(terminalInfo.getActivityTypeNo());
				if (terminalInfo.getActivityType().indexOf("7") >= 0) {
					hlfHardware.setActivityCode("009");
				} else if (terminalInfo.getActivityType().indexOf("8") >= 0) {
					hlfHardware.setActivityCode("008");
				}
				HlfHardware hlf=activityService.selectHlfHardwareInfo(hlfHardware);
				if(hlf==null){
					jsonMap.put("result", false);
					jsonMap.put("message", "硬件种类已变更，请选择对应的新欢乐返活动类型");
					return jsonMap;
				}
			}
			HardwareProduct hp = hardwareProductService.selectHardwareName(terminalInfo.getType());
			terminalInfo.setPosType(hp.getPosType());
			terminalInfo.setModel(hp.getModel());
			// 查询是否有相同的机具
			terminalInfo1 = terminalInfoService.selectByAddParam(terminalInfo);
			if (terminalInfo1.size() != 0) {
				jsonMap.put("result", false);
				jsonMap.put("message", "新增内容已有，请重新输入!");
			} else {
				terminalInfo.setCashierNo(principal.getId().toString());
				terminalInfo.setCreateTime(new Date());
				terminalInfo.setOpenStatus("0");
				terminalInfoService.insert(terminalInfo);

				addTerActivityCheck(terminalInfo);

				jsonMap.put("result", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("机具新增出错-----", e);
			jsonMap.put("result", false);
		}

		return jsonMap;
	}

	// 修改
	@RequestMapping(value = "/updateTerminalInfo.do")
	@SystemLog(description = "机具修改", operCode = "terminal.update")
	public @ResponseBody Object updateTerminalInfo(@RequestBody TerminalInfo terminalInfo) {
		Map<String, Object> jsonMap = new HashMap<>();
		try {
			HlfHardware hlf=null;
			//验证欢乐返子类型
			if(StringUtils.isNotBlank(terminalInfo.getActivityTypeNo())) {
				HlfHardware hlfHardware = new HlfHardware();
				hlfHardware.setHardId(Long.valueOf(terminalInfo.getType()));
				hlfHardware.setActivityTypeNo(terminalInfo.getActivityTypeNo());
				if (terminalInfo.getActivityType().indexOf("7") >= 0) {
					hlfHardware.setActivityCode("009");
				} else if (terminalInfo.getActivityType().indexOf("8") >= 0) {
					hlfHardware.setActivityCode("008");
				}
				hlf=activityService.selectHlfHardwareInfo(hlfHardware);
				if(hlf==null){
					jsonMap.put("result", false);
					jsonMap.put("message", "硬件种类已变更，请选择对应的新欢乐返活动类型");
					return jsonMap;
				}
			}
//			HlfHardware oldHlf=hardwareProductService.isHlfHardware(terminalInfo.getSn());
//			if(oldHlf!=null){//原属于欢乐活动
//				if("3".equals(oldHlf.getSubType())){
//					if(hlf!=null&&!"3".equals(hlf.getSubType())){
//						jsonMap.put("result", false);
//						jsonMap.put("message", "机具互斥活动不能相互修改!");
//						return jsonMap;
//					}
//				}else{
//					if(hlf!=null&&"3".equals(hlf.getSubType())){
//						jsonMap.put("result", false);
//						jsonMap.put("message", "机具互斥活动不能相互修改!");
//						return jsonMap;
//					}
//				}
//			}

			HardwareProduct hp = hardwareProductService.selectHardwareName(terminalInfo.getType());
			terminalInfo.setPosType(hp.getPosType());
			terminalInfo.setType(hp.getHpId().toString());
			int i = terminalInfoService.updateByPrimaryKey(terminalInfo);
			if (i > 0) {
				jsonMap.put("result", true);
			} else {
				jsonMap.put("result", false);
				jsonMap.put("message", "修改失败！！！！！");
			}
		} catch (Exception e) {
			log.error("机具修改出错-----", e);
			jsonMap.put("result", false);
		}
		return jsonMap;
	}

	// 解分
	@RequestMapping(value = "/solutionById.do")
	@SystemLog(description = "机具回收", operCode = "terminal.recover")
	public @ResponseBody Object solutionById(@RequestParam("id") Long id,
			@ModelAttribute("page") Page<TerminalInfo> page) {
		Map<String, Object> jsonMap = new HashMap<>();
		TerminalInfo terminalInfo = new TerminalInfo();
		try {
			terminalInfo.setId(id);
			terminalInfo.setAgentNo(null);
			terminalInfo.setAgentNode(null);
			terminalInfo.setOpenStatus("0");
			int i = terminalInfoService.updateRecoverSolutionById(terminalInfo);
			if (i > 0) {
				jsonMap.put("result", true);
//				jsonMap.put("object", selectAllInfo(page));
			} else {
				jsonMap.put("result", false);
				jsonMap.put("message", "机具回收失败");
			}
		} catch (Exception e) {
			log.error("机具回收出错-----", e);
			jsonMap.put("result", false);
			String str=e.getMessage();
			if(str==null){
				jsonMap.put("message", "机具回收异常");
			}else{
				jsonMap.put("message", e.getMessage());
			}
		}

		return jsonMap;
	}

	// 解绑
	@RequestMapping(value = "/unbundlingById.do")
	@SystemLog(description = "机具解绑", operCode = "terminal.unbundling")
	public @ResponseBody Object unbundlingById(@RequestParam("id") Long id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg = terminalInfoService.updateUnbundlingById(id);
		} catch (Exception e) {
			log.error("机具解绑出错-----", e);
			msg.put("status", false);
			msg.put("msg", "解绑失败");
		}
		return msg;
	}
	
	// 批量解绑
	@RequestMapping(value = "/batchUnbundlingById.do")
	@SystemLog(description = "机具批量解绑", operCode = "terminal.batchUnbundling")
	public @ResponseBody Object batchUnbundlingById(@RequestParam("idList") ArrayList<Long> idList) {
		Map<String, Object> msg = new HashMap<>();
		try {
			StringBuilder errMsg = new StringBuilder();
			int sum = idList.size();
			int successSum = 0;
			int failSum = 0;
			for (Long id : idList) {
				Map<String, Object> map = terminalInfoService.updateUnbundlingById(id);
				if ((boolean) map.get("status")) {
					successSum++;
				} else {
					errMsg.append("机具ID:" + id + "解绑失败，失败原因:" + map.get("msg"));
				}
			}
			failSum = sum - successSum;
			msg.put("status", true);
			msg.put("msg", "操作成功,总条数:" + sum + "，成功条数:" + successSum + "，失败条数:" + failSum
					+ "," + errMsg);
		} catch (Exception e) {
			log.error("机具批量解绑异常", e);
			msg.put("status", false);
			msg.put("msg", "批量解绑失败");
		}
		return msg;
	}

	// 机具下发
	@RequestMapping(value = "/distributionTerminal.do")
	@SystemLog(description = "机具下发", operCode = "terminal.issued")
	public @ResponseBody Object distributionTerminal(@RequestParam("param") String param,
			@ModelAttribute("page") Page<TerminalInfo> page) {
		JSONObject json = JSON.parseObject(param);
		Map<String, Object> jsonMap = new HashMap<>();
		try {
			int num = 0;
			String agrntNo = json.getString("agentNo");
			AgentInfo ais = agentInfoService.selectByagentNo(agrntNo);
			List<TerminalInfo> terminalInfoList = JSON.parseArray(json.getJSONArray("list").toJSONString(),
					TerminalInfo.class);
			//根据机具的sn号，去acq_terminal_store表查找记录，看终端号ter_no是否存在，如果存在，则符合要求
			//如果不存在记录或者终端号为空，则返回，并提示：机具下发失败，请先输入终端号！
			for (TerminalInfo terminalInfo : terminalInfoList) {
				AcqTerminalStore acqTerminalStore = acqTerminalStoreService.selectBySn(terminalInfo.getSn());
				if(acqTerminalStore == null || StringUtil.isEmpty(acqTerminalStore.getTerNo())){
					jsonMap.put("result", false);
					jsonMap.put("msg", "机具(" + terminalInfo.getSn() + ")下发失败，请先输入终端号！");
					return jsonMap;
				}
			}
			for (TerminalInfo terminalInfo : terminalInfoList) {
				terminalInfo.setStartTime(new Date());
				terminalInfo.setOpenStatus("1");
				terminalInfo.setAgentNo(ais.getAgentNo());
				terminalInfo.setAgentNode(ais.getAgentNode());
				terminalInfo.setAgentLevel(ais.getAgentLevel()+"");
				num += terminalInfoService.updateSolutionById(terminalInfo);
				terminalInfoService.insertTerminalOperate(terminalInfo.getId(),1,1);
				String strAgentNo[]=ais.getAgentNode().split("-");
				for (int i=0;i<strAgentNo.length;i++){
					if(StringUtil.isNotBlank(strAgentNo[i])&&!strAgentNo[i].equals("0")){
						terminalInfoService.insertAgentRerminalOperate(strAgentNo[i],terminalInfo.getSn(),1,1);
						if(i<strAgentNo.length-1){
							terminalInfoService.insertAgentRerminalOperate(strAgentNo[i],terminalInfo.getSn(),1,2);
						}
					}
				}
				agentTerminalService.insertAgentTerminal(terminalInfo.getAgentNo(),terminalInfo.getSn(),terminalInfo.getAgentLevel());
				if(StringUtil.isNotBlank(terminalInfo.getActivityTypeNo())){
					ActivityHardwareType aht=activityService.queryByActivityHardwareType(terminalInfo.getActivityTypeNo());
					List<ActivityHardwareType> happyBackTypes=agentInfoService.selectHappyBackTypeByAgentNo(ais.getAgentNo());
					boolean isStatus=true;
					for (int i = 0; i < happyBackTypes.size(); i++) {
						if (aht.getActivityTypeNo().equals(happyBackTypes.get(i).getActivityTypeNo())) {
							String agentNoStr[]= ais.getAgentNode().split("-");
							String agentNoes="";
							for(String agentNo:agentNoStr){
								if(!StringUtil.isEmpty(agentNo)&&!agentNo.equals("0")){
									agentNoes+=agentNo+",";
								}
							}
							if(!StringUtil.isEmpty(agentNoes)){
								agentNoes=agentNoes.substring(0,agentNoes.length()-1);
								log.info("------------修改代理商参加的欢乐返子类型状态，agentNoes:{}",agentNoes);
								agentInfoService.updateHlfTerByAgentLevel(agentNoes,aht.getActivityTypeNo());
							}
							isStatus=false;
							break;
						}
					}
					if(isStatus&&ais!=null){
						List<ActivityHardwareType> ahts=new ArrayList<ActivityHardwareType>();
						String tpyes="";
						tpyes+="'"+aht.getActivityTypeNo()+"',";
						// ('00007','1448','0-1448-',127),('00009','1449','0-1449-',129)
						aht.setAgentNo(ais.getOneLevelId());
						aht.setAgentNode("0-"+ais.getOneLevelId()+"-");
						aht.setTaxRate(BigDecimal.valueOf(1));
						aht.setRepeatRegisterRatio(BigDecimal.valueOf(1));
						if(aht.getXhlfSmartConfigId()!=null){
							//智能版活动赋值这6个参数
							aht.setOneRewardAmount(aht.getOneRewardAgentAmount());
							aht.setOneRepeatRewardAmount(aht.getOneRepeatRewardAgentAmount());
							aht.setTwoRewardAmount(aht.getTwoRewardAgentAmount());
							aht.setTwoRepeatRewardAmount(aht.getTwoRepeatRewardAgentAmount());
							aht.setThreeRewardAmount(aht.getThreeRewardAgentAmount());
							aht.setThreeRepeatRewardAmount(aht.getThreeRepeatRewardAgentAmount());
						}
						ahts.add(aht);
						int res=0;
						log.info("------------修改代理商参加的欢乐返子类型新增，AgentNode:{}",ais.getAgentNode());
						try {
							res = agentInfoService.insertHappyBackType(ahts);
						}catch (Exception e){
							log.error("", e);
							log.error("------------修改代理商参加的欢乐返子类型新增失败，已存在配置，AgentNo:{}", ais.getOneLevelId());
						}
						if(!"".equals(tpyes)){
							tpyes=tpyes.substring(0,tpyes.length() - 1);
							String agentNoStr[]= ais.getAgentNode().split("-");
							String agentNoes="";
							for(String agentNo:agentNoStr){
								if(!StringUtil.isEmpty(agentNo)&&!agentNo.equals("0")){
									agentNoes+=agentNo+",";
									try {
										agentInfoService.insertHappyBackTypeByAgentLevel(agentNo,tpyes);
										log.info("------------修改代理商参加的欢乐返子类型新增成功，AgentNo:{}",agentNo);
									}catch (Exception e){
										log.error("", e);
										log.error("------------修改代理商参加的欢乐返子类型新增失败，已存在配置AgentNo:{}",agentNo);
									}
								}
							}
							if(!StringUtil.isEmpty(agentNoes)){
								agentNoes=agentNoes.substring(0,agentNoes.length()-1);
								log.info("------------修改代理商参加的欢乐返子类型状态，agentNoes:{}",agentNoes);
								agentInfoService.updateHappyBackTypeByAgentLevel(agentNoes,tpyes);
							}
						}
						if (res != ahts.size()) {
							log.error("修改代理商参加的欢乐返子类型失败");
						}
					}
				}
			}
			if (num > 0) {
				jsonMap.put("result", true);
//				jsonMap.put("object", selectAllInfo(page));
				jsonMap.put("msg", "机具下发成功！！！！！");
			} else {
				jsonMap.put("result", false);
				jsonMap.put("msg", "机具下发失败！！！！！");
			}
		} catch (Exception e) {
			log.error("机具下发出错-----", e);
			jsonMap.put("result", false);
			jsonMap.put("msg", "机具下发报错！！！！！");
		}
		return jsonMap;
	}

	// 绑定
	@RequestMapping(value = "/bindingTerminal.do")
	@ResponseBody
	@SystemLog(description = "机具绑定", operCode = "terminal.binding")
	public Result bindingTerminal(@RequestParam("param") String param) {
		JSONObject json = JSON.parseObject(param);
		Result result = new Result();
		try {
			List<TerminalInfo> terList = JSON.parseArray(json.getJSONArray("list").toJSONString(),
					TerminalInfo.class);
			String merNo = json.getString("merNo");
			String bpId = json.getString("bpId");
			if(StringUtils.isBlank(merNo) || StringUtils.isBlank(bpId) || terList==null || terList.size() < 1){
				result.setStatus(false);
				result.setMsg("参数不能为空");
				return result;
			}
			result = terminalInfoService.batchBindingTerminal(merNo, bpId, terList);
//			int num = 0;
//			for (TerminalInfo terminalInfo : terList) {
//				if (!merchantInfoService.selectByMerNo(merNo).getAgentNo().equals(terminalInfo.getAgentNo())) {
//					continue;
//				}
//				terminalInfo.setStartTime(new Date());
//				terminalInfo.setOpenStatus("2");
//				terminalInfo.setMerchantNo(merNo);
//				terminalInfo.setBpId(bpId);
//				num += terminalInfoService.updateBundlingById(terminalInfo);
//			}
		} catch (Exception e) {
			log.error("机具绑定异常", e);
			result.setStatus(false);
			result.setMsg("机具绑定异常");
		}
		return result;
	}

	// 机具导入
	@RequestMapping(value = "/importTerminal")
	@SystemLog(description = "机具导入", operCode = "terminal.import")
	public @ResponseBody Object importTerminal(@RequestParam("file") MultipartFile file,
			@RequestParam("agentNo") String agentNo, @RequestParam("termianlType") Long termianlType,
											   @RequestParam("activityType") String activityType,
											   @RequestParam("activityTypeNo") String activityTypeNo) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("result", false);
		jsonMap.put("msg", "机具导入失败");

		int errorCount=0;
		int successCount=0;
		List<Map> errorlist = new ArrayList<Map>();
		try {
			if (!file.isEmpty()) {
				String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if (!format.equals(".xls") && !format.equals(".xlxs")) {
					jsonMap.put("msg", "机具文件格式错误,请上传文件后缀为:.xls 或 .xlxs的Excel文件");
					return jsonMap;
				}
				long fileSize = file.getSize();
				if (((fileSize / 1024) / 1024) > 4) {
					jsonMap.put("msg", "上传文件过大,请上传4MB以内的文件");
					return jsonMap;
				}
				if(StringUtils.isBlank(agentNo)){
					jsonMap.put("msg", "代理商不能为空!");
					return jsonMap;
				}
				if(termianlType == null){
					jsonMap.put("msg", "硬件产品不能为空!");
					return jsonMap;
				}
				if(StringUtils.isNotBlank(activityType) && activityType.indexOf("7") >= 0 && StringUtils.isBlank(activityTypeNo)){
					jsonMap.put("msg", "请选择欢乐返子类型!");
					return jsonMap;
				}
				//验证欢乐返子类型
				if(StringUtils.isNotBlank(activityTypeNo)){
					HlfHardware hlfHardware = new HlfHardware();
					hlfHardware.setHardId(termianlType);
					hlfHardware.setActivityTypeNo(activityTypeNo);
					if (activityType.indexOf("7") >= 0) {
						hlfHardware.setActivityCode("009");
					} else if (activityType.indexOf("8") >= 0) {
						hlfHardware.setActivityCode("008");
					}
					HlfHardware hlf=activityService.selectHlfHardwareInfo(hlfHardware);
					if(hlf==null){
						jsonMap.put("result", false);
						jsonMap.put("msg", "硬件种类已变更，请选择对应的新欢乐返活动类型");
						return jsonMap;
					}
				}
				//获取代理商数据
				AgentInfo agentInfo = agentInfoService.selectLittleInfo(agentNo);
				//获取活动类型
				List<SysDict> activityTypeList = sysDictService.selectByKey("ACTIVITY_TYPE");
				Map<String, String> activityTypeListMap = new HashMap<>();
				if(activityTypeList!=null && activityTypeList.size()>0){
					for(SysDict item: activityTypeList){
						activityTypeListMap.put(item.getSysName(), item.getSysValue());
					}
				}
				//通道
				List<SysDict> channelList = sysDictService.selectByKey("JJTD");
				Map<String,Integer> channelMap = new HashMap<>();
				if(channelList!=null&&channelList.size()>0){
					channelMap.put(channelList.get(0).getSysName(),1);//YS_ZQ
				}else{
					jsonMap.put("msg", "数据字典key:JJTD,未配置通道");
					return jsonMap;
				}

				// 遍历所有单元格，读取单元格
				Workbook wb = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = wb.getSheetAt(0);
				int row_num = sheet.getLastRowNum();
				for (int i = 1; i <= row_num; i++) {
                    Map errorMap=new HashMap();
					Row row = sheet.getRow(i);
					String sn = CellUtil.getCellValue(row.getCell(0));
					String psam = CellUtil.getCellValue(row.getCell(1));
					String channel = CellUtil.getCellValue(row.getCell(2));

					TerminalInfo tis = new TerminalInfo();
					tis.setSn(sn);
					errorMap.put("sn",sn);

					int rowNum=i+1;
					if (StringUtils.isBlank(sn)) {
						errorCount++;
						errorMap.put("errorResult","机具文件有误,第" +rowNum+"行sn不能为空");
						errorlist.add(errorMap);
						continue;
					}else{
						if (terminalInfoService.selectBySameData(tis) != null) {
							errorCount++;
							errorMap.put("errorResult","第"+rowNum+ "行,机具已存在");
							errorlist.add(errorMap);
							continue;
						}
					}
					// 根据机具编号查询终端号 为空抛出异常
					long count = terminalInfoService.countTerNoBySn(tis.getSn());
					long countSn =  terminalInfoService.countBySn(tis.getSn());
					if (count > 0 || countSn < 1 ) {
						errorCount++;
						errorMap.put("errorResult","第"+rowNum+ "行,请先输入终端号");
						errorlist.add(errorMap);
						continue;
					}

					if (StringUtils.isBlank(psam)) {
						errorCount++;
						errorMap.put("errorResult","机具文件有误,第"+rowNum+ "行psam不能为空");
						errorlist.add(errorMap);
						continue;
					}
					if (StringUtils.isNotEmpty(channel)) {
						if(channelMap.get(channel)==null){
							errorMap.put("errorResult","机具通道有误，第"+ rowNum +"行数据字典无该值");
							errorlist.add(errorMap);
							continue;
						}
					}

					tis.setSn(sn);
					tis.setPsamNo(psam);
					tis.setType(termianlType+"");
				    tis.setChannel(channel);
				    tis.setActivityType(activityType);
					tis.setActivityTypeNo(activityTypeNo);
					if (agentInfo != null) {
						tis.setAgentNo(agentInfo.getAgentNo());
						tis.setAgentNode(agentInfo.getAgentNode());
						tis.setAgentLevel(agentInfo.getAgentLevel()+"");
						tis.setOpenStatus("1");
					} else {
						tis.setOpenStatus("0");
					}
					tis.setCreateTime(new Date());
					HardwareProduct hp = hardwareProductService.selectHardwareName(tis.getType());
					tis.setPosType(hp.getPosType());
					tis.setModel(hp.getModel());
					int num=terminalInfoService.insertSelective(tis);
					//同时ter_activity_check表新增记录
					addTerActivityCheck(tis);

					if(num>0){
						successCount++;
					}
				}
				if(successCount > 0){
					if(StringUtil.isNotBlank(activityTypeNo) && StringUtil.isNotBlank(agentInfo)){
						log.info("------------修改代理商参加的欢乐返子类型新增，AgentNode:{}",agentInfo.getAgentNode());
						try {
							agentInfoService.insertHlfTerByOneAgentLevel(agentInfo.getOneLevelId(), activityTypeNo);
						}catch (Exception e){

							log.error("------------修改代理商参加的欢乐返子类型新增失败，已存在配置，AgentNo:{}",agentInfo.getOneLevelId());
							log.error("修改代理商参加的欢乐返子类型新增失败",e);
						}
						String agentNoStr[]= agentInfo.getAgentNode().split("-");
						String agentNoes="";
						for(String agentStr:agentNoStr){
							if(!StringUtil.isEmpty(agentStr)&&!agentStr.equals("0")){
								agentNoes+=agentStr+",";
								try {
									agentInfoService.insertHlfTerByAgentLevel(agentStr, activityTypeNo);
									log.info("------------修改代理商参加的欢乐返子类型新增成功，AgentNo:{}",agentStr);
								}catch (Exception e){
									log.error("------------修改代理商参加的欢乐返子类型新增失败，已存在配置，AgentNo:{}",agentStr);
								}
							}
						}
						if(!StringUtil.isEmpty(agentNoes)){
							agentNoes=agentNoes.substring(0,agentNoes.length()-1);
							log.info("------------修改代理商参加的欢乐返子类型开关状态，agentNoes:{}",agentNoes);
							agentInfoService.updateHlfTerByAgentLevel(agentNoes, activityTypeNo);
						}
					}
				}
				jsonMap.put("errorCount", errorCount);
				jsonMap.put("successCount", successCount);
				jsonMap.put("errorlist", errorlist);
				jsonMap.put("result", true);
				jsonMap.put("msg", "机具导入成功");
			} else {
				jsonMap.put("result", false);
				jsonMap.put("msg", "机具文件格式错误");
			}
		} catch (Exception e) {
			jsonMap.put("result", false);
			if (e instanceof TerNoIsNullException) {
				jsonMap.put("msg", e.getMessage());
				return jsonMap;
			}
			
			String str = e.getMessage();
			if (e.getMessage() == null) {
				jsonMap.put("msg", "机具信息不完整");
				return jsonMap;
			}
			if (str.contains("\r\n") || str.contains("\n"))
				jsonMap.put("msg", "机具导入异常");
			else
				jsonMap.put("msg", str);
			log.error("机具导入异常,", e);
		}
		return jsonMap;
	}


	public void addTerActivityCheck(TerminalInfo ti){
		List<SysDict> sysDicts = sysDictService.selectByKey("TER_ACTIVITY_DEVICE_PN");
		List<HardwareProduct> list = hardwareProductService.selectAllHardwareProduct(sysDicts);
		for (HardwareProduct hardwareProduct : list) {
			if(ti.getType().equals(hardwareProduct.getHpId()+"")){
				SysDict sysDict = sysDictService.getByKey("TER_ACTIVITY_DUE_DAYS");
				TerActivityCheck tac = new TerActivityCheck(ti.getSn(), new Integer(sysDict.getSysValue()), ti.getCreateTime());
				terminalInfoService.insertOne(tac);
				break;
			}
		}
	}

	// 机具导入備份
	/*@RequestMapping(value = "/importTerminal")
	@SystemLog(description = "机具导入", operCode = "terminal.import")
	public @ResponseBody Object importTerminal(@RequestParam("file") MultipartFile file,
			@RequestParam("agentNo") String agentNo, @RequestParam("termianlType") String termianlType) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("result", false);
		jsonMap.put("msg", "机具导入失败");
		int errorCount=0;
		int successCount=0;
		List<Map> errorlist = new ArrayList<Map>();
		try {
			if (!file.isEmpty()) {
				String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if (!format.equals(".xls") && !format.equals(".xlxs")) {
					jsonMap.put("msg", "机具文件格式错误,请上传文件后缀为:.xls 或 .xlxs的Excel文件");
					return jsonMap;
				}
				long fileSize = file.getSize();
				if (((fileSize / 1024) / 1024) > 4) {
					jsonMap.put("msg", "上传文件过大,请上传4MB以内的文件");
					return jsonMap;
				}
				Workbook wb = WorkbookFactory.create(file.getInputStream());
				//List<TerminalInfo> tlist = new ArrayList<TerminalInfo>();
				Sheet sheet = wb.getSheetAt(0);
				// 遍历所有单元格，读取单元格
				int row_num = sheet.getLastRowNum();

				if(StringUtils.isBlank(agentNo)){
					jsonMap.put("msg", "代理商不能为空!");
					return jsonMap;
				}
				if(StringUtils.isBlank(termianlType)){
					jsonMap.put("msg", "硬件产品不能为空!");
					return jsonMap;
				}
				//获取代理商数据
				AgentInfo agentInfo = agentInfoService.selectLittleInfo(agentNo);
				//获取活动类型
				List<SysDict> activityTypeList = sysDictService.selectByKey("ACTIVITY_TYPE");
				Map<String, String> activityTypeListMap = new HashMap<>();
				if(activityTypeList!=null && activityTypeList.size()>0){
					for(SysDict item: activityTypeList){
						activityTypeListMap.put(item.getSysName(), item.getSysValue());
					}
				}
				//通道
				List<SysDict> channelList = sysDictService.selectByKey("JJTD");
				Map<String,Integer> channelMap = new HashMap<>();
				if(channelList!=null&&channelList.size()>0){
					channelMap.put(channelList.get(0).getSysName(),1);//YS_ZQ
				}else{
					jsonMap.put("msg", "数据字典key:JJTD,未配置通道");
					return jsonMap;
				}

				for (int i = 1; i <= row_num; i++) {
                    Map errorMap=new HashMap();
					Row row = sheet.getRow(i);
					String sn = CellUtil.getCellValue(row.getCell(0));
					String psam = CellUtil.getCellValue(row.getCell(1));
					String channel = CellUtil.getCellValue(row.getCell(2));
					String activityTypeNo = CellUtil.getCellValue(row.getCell(3));
					String activityTypeName = CellUtil.getCellValue(row.getCell(4));

					TerminalInfo tis = new TerminalInfo();
					tis.setSn(sn);
					errorMap.put("sn",sn);

					int rowNum=i+1;
					if (StringUtils.isBlank(sn)) {
						errorCount++;
						errorMap.put("errorResult","机具文件有误,第" +rowNum+"行sn不能为空");
						errorlist.add(errorMap);
						continue;
					}else{
						if (terminalInfoService.selectBySameData(tis) != null) {
							errorCount++;
							errorMap.put("errorResult","第"+rowNum+ "行,机具已存在");
							errorlist.add(errorMap);
							continue;
						}
					}
					// 根据机具编号查询终端号 为空抛出异常
					long count = terminalInfoService.countTerNoBySn(tis.getSn());
					long countSn =  terminalInfoService.countBySn(tis.getSn());
					if (count > 0 || countSn < 1 ) {
						errorCount++;
						errorMap.put("errorResult","第"+rowNum+ "行,请先输入终端号");
						errorlist.add(errorMap);
						continue;
					}

					if (StringUtils.isBlank(psam)) {
						errorCount++;
						errorMap.put("errorResult","机具文件有误,第"+rowNum+ "行psam不能为空");
						errorlist.add(errorMap);
						continue;
					}
					if (StringUtils.isNotEmpty(channel)) {
						if(channelMap.get(channel)==null){
							errorMap.put("errorResult","机具通道有误，第"+ rowNum +"行数据字典无该值");
							errorlist.add(errorMap);
							continue;
						}
					}
					// 机具活动最多只能填写五个
					String activityTypeStr="";
					Set<String> activityTypeSet = new HashSet<>();
					boolean isActivityType=false;
					for (int j = 5; j < 10; j++) {
						String activityName = CellUtil.getCellValue(row.getCell(j));
						if (StringUtils.isNotBlank(activityName)) {
							if(activityTypeListMap.get(activityName)!=null){
								activityTypeSet.add(activityTypeListMap.get(activityName));
							}else{
								if(j==9){
									activityTypeStr+="第"+(j+1) +"列";
								}else{
									activityTypeStr+="第"+(j+1) +"列,";
								}
								isActivityType=true;
							}
						}
					}
					if(isActivityType){
						errorCount++;
						errorMap.put("errorResult","第"+rowNum +"行活动配置有误" + activityTypeStr);
						errorlist.add(errorMap);
						continue;
					}
					if (row.getCell(10) != null && StringUtils.isNotBlank(CellUtil.getCellValue(row.getCell(10)))) {
						errorCount++;
						errorMap.put("errorResult","第"+ rowNum +"行活动配置有误,活动类型最多只能五个");
						errorlist.add(errorMap);
						continue;
					}
					// 如果活动类型不为空
					if (activityTypeSet != null && activityTypeSet.size() > 0) {
						// 检查activityTypeSet是否存在互斥
						List<String> itemList = new ArrayList<>(activityTypeSet);
						int checkNum = activityGroupService.checkMutex(itemList);
						if (checkNum > 0) {
							errorCount++;
							errorMap.put("errorResult","第"+ rowNum +"行活动配置有误,存在互斥");
							errorlist.add(errorMap);
							continue;
						}
						// 如果没有互斥，逗号隔开赋值给机具
						StringBuilder activityTypeSb = new StringBuilder();
						for (String str : activityTypeSet) {
							activityTypeSb.append(str+",");
						}
						tis.setActivityType(activityTypeSb.substring(0, activityTypeSb.length() - 1));
					}

					//验证欢乐返子类型
					HlfHardware hlfHardware = new HlfHardware();
					hlfHardware.setHardId(Long.valueOf(termianlType));
					hlfHardware.setActivityTypeNo(activityTypeNo);

					if(StringUtils.isNotBlank(activityTypeNo)){
						if(tis.getActivityType()!=null){
							if(tis.getActivityType().indexOf("7")>=0){
								hlfHardware.setActivityCode("009");
							}else if(tis.getActivityType().indexOf("8")>=0){
								hlfHardware.setActivityCode("008");
							}
							HlfHardware hlf=activityService.selectHlfHardwareInfo(hlfHardware);
							if(hlf==null){
								errorCount++;
								errorMap.put("errorResult","第"+ rowNum +"行该硬件产品还没有欢乐返子类型，请先新建后重试");
								errorlist.add(errorMap);
								continue;
							}
							if(!hlf.getActivityTypeName().equals(activityTypeName)){
								errorCount++;
								errorMap.put("errorResult","第"+ rowNum +"行欢乐返子类型编号与名称不一致");
								errorlist.add(errorMap);
								continue;
							}
						}else{
							errorCount++;
							errorMap.put("errorResult","第"+ rowNum +"行请添加活动");
							errorlist.add(errorMap);
							continue;
						}
					}

					tis.setSn(sn);
					tis.setPsamNo(psam);
					tis.setType(termianlType);
				    tis.setChannel(channel);
					tis.setActivityTypeNo(activityTypeNo);
					if (agentInfo != null) {
						tis.setAgentNo(agentInfo.getAgentNo());
						tis.setAgentNode(agentInfo.getAgentNode());
						tis.setAgentLevel(agentInfo.getAgentLevel()+"");
						tis.setOpenStatus("1");
					} else {
						tis.setOpenStatus("0");
					}
					tis.setCreateTime(new Date());
					HardwareProduct hp = hardwareProductService.selectHardwareName(tis.getType());
					tis.setPosType(hp.getPosType());
					tis.setModel(hp.getModel());
					int num=terminalInfoService.insertSelective(tis);
					if(StringUtil.isNotBlank(tis.getActivityTypeNo())&&StringUtil.isNotBlank(agentInfo)){
						log.info("------------修改代理商参加的欢乐返子类型新增，AgentNode:{}",agentInfo.getAgentNode());
						try {
							agentInfoService.insertHlfTerByOneAgentLevel(agentInfo.getOneLevelId(),tis.getActivityTypeNo());
						}catch (Exception e){
							log.error("------------修改代理商参加的欢乐返子类型新增失败，已存在配置，AgentNo:{}",agentInfo.getOneLevelId());
						}
						String agentNoStr[]= agentInfo.getAgentNode().split("-");
						String agentNoes="";
						for(String agentStr:agentNoStr){
							if(!StringUtil.isEmpty(agentStr)&&!agentStr.equals("0")){
								agentNoes+=agentStr+",";
								try {
									agentInfoService.insertHlfTerByAgentLevel(agentStr,tis.getActivityTypeNo());
									log.info("------------修改代理商参加的欢乐返子类型新增成功，AgentNo:{}",agentStr);
								}catch (Exception e){
									log.error("------------修改代理商参加的欢乐返子类型新增失败，已存在配置，AgentNo:{}",agentStr);
								}
							}
						}
						if(!StringUtil.isEmpty(agentNoes)){
							agentNoes=agentNoes.substring(0,agentNoes.length()-1);
							log.info("------------修改代理商参加的欢乐返子类型开关状态，agentNoes:{}",agentNoes);
							agentInfoService.updateHlfTerByAgentLevel(agentNoes,tis.getActivityTypeNo());
						}
					}
					if(num>0){
						successCount++;
					}
				}
				jsonMap.put("errorCount", errorCount);
				jsonMap.put("successCount", successCount);
				jsonMap.put("errorlist", errorlist);
				jsonMap.put("result", true);
				jsonMap.put("msg", "机具导入成功");
			} else {
				jsonMap.put("result", false);
				jsonMap.put("msg", "机具文件格式错误");
			}
		} catch (Exception e) {
			jsonMap.put("result", false);
			if (e instanceof TerNoIsNullException) {
				jsonMap.put("msg", e.getMessage());
				return jsonMap;
			}

			String str = e.getMessage();
			if (e.getMessage() == null) {
				jsonMap.put("msg", "机具信息不完整");
				return jsonMap;
			}
			if (str.contains("\r\n") || str.contains("\n"))
				jsonMap.put("msg", "机具导入异常");
			else
				jsonMap.put("msg", str);
			log.error("机具导入异常,", e);
		}
		return jsonMap;
	}*/


	/**
	 * 批量导入修改-机具硬件产品
	 */
	@RequestMapping(value="/importDiscount")
	@ResponseBody
	public Map<String, Object> importDiscount(@RequestParam("file") MultipartFile file,HttpServletRequest request){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "导入文件格式错误!");
					return msg;
				}
			}else{
				msg.put("status", false);
				msg.put("msg", "导入文件不能为空!");
				return msg;
			}
			msg = terminalInfoUnService.importDiscount(file,request);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "批量导入修改-机具硬件产品失败!");
			log.error("批量导入修改-机具硬件产品失败!",e);
		}
		return msg;
	}


	/**
	 * 批量导入修改-机具活动考核剩余天数
	 */
	@RequestMapping(value="/changeDueDays")
	@ResponseBody
	public Map<String, Object> changeDueDays(@RequestParam("file") MultipartFile file,HttpServletRequest request){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "导入文件格式错误!");
					return msg;
				}
			}else{
				msg.put("status", false);
				msg.put("msg", "导入文件不能为空!");
				return msg;
			}
			msg = terminalInfoService.changeDueDays(file,request);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "批量导入修改-机具活动考核剩余天数!");
			log.error("批量导入修改-机具活动考核剩余天数!",e);
		}
		return msg;
	}


	/**
	 * 获取异步导入结果
	 */
	@RequestMapping(value="/getImportResult")
	@ResponseBody
	public Map<String, Object> getImportResult(HttpServletRequest request){
		Map<String, Object> msg = new HashMap<>();
		if(request.getSession().getAttribute("batchNoTerminal")!=null){
			String batchNo=request.getSession().getAttribute("batchNoTerminal").toString();
			ImportLog log=terminalInfoUnService.getImportResult(batchNo);
			if(log!=null){
				msg.put("status", true);
				msg.put("msg", "导入成功!");
				msg.put("info", log);
			}else{
				msg.put("status", false);
				msg.put("msg", "数据还在导入中,请耐心等待!");
			}
		}else{
			msg.put("status", false);
			msg.put("msg", "无导入记录!");
		}
		return msg;
	}

	/**
	 * 导出批量修改导入处理结果
	 */
	@RequestMapping(value="/downloadResult")
	@ResponseBody
	public Map<String, Object> importDiscount(@RequestParam("batchNo") String batchNo,HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> msg = new HashMap<>();
        try {
		    terminalInfoService.downloadResult(batchNo,response,msg);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "导出批量修改导入处理结果失败!");
            log.error("导出批量修改导入处理结果失败!",e);
        }
		return msg;
	}
	/**
	 * 下载修改机具考核期限模板
	 */
	@RequestMapping("/downloadUpdateDueDaysTemplate")
	public String downloadUpdateDueDaysTemplate(HttpServletRequest request, HttpServletResponse response) {
		String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator
				+ "terminalUpdateDueDaysTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath, "批量修改机具考核期限模板.xlsx");
		return null;
	}

	/**
	 * 下载模板
	 */
	@RequestMapping("/downloadupdateTypeTemplate")
	public String downloadupdateTypeTemplate(HttpServletRequest request, HttpServletResponse response) {
		String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator
				+ "termianlTypeTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath, "批量修改机具硬件产品模板.xlsx");
		return null;
	}

	/**
	 * 下载模板
	 */
	@RequestMapping("/downloadTemplate")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response) {
		String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator
				+ "termianlTemplate.xls";
		log.info(filePath);
		ResponseUtil.download(response, filePath, "批量导入机具模板.xls");
		return null;
	}

	/**
	 * 密钥导入模板
	 * 
	 * @return
	 */
	@RequestMapping("/downloadKeyTemplate")
	public String downloadKeyTemplate(HttpServletRequest request, HttpServletResponse response) {
		String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator
				+ "termianlKeyTemplate.xls";
		log.info(filePath);
		ResponseUtil.download(response, filePath, "批量导入机具密钥模板.xls");
		return null;
	}

	// 机具密钥导入
	@RequestMapping(value = "/exportKey")
	@SystemLog(description = "机具密钥导入", operCode = "terminal.exportKey")
	public @ResponseBody Object exportKey(@RequestParam("file") MultipartFile file) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			if (!file.isEmpty()) {
				String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if (!format.equals(".xls") && !format.equals(".xlsx")) {
					jsonMap.put("status", false);
					jsonMap.put("msg", "机具文件格式错误");
					return jsonMap;
				}
				Workbook wb = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = wb.getSheetAt(0);
				// 遍历所有单元格，读取单元格
				int row_num = sheet.getLastRowNum();
				if (row_num < 2) {
					jsonMap.put("status", false);
					jsonMap.put("msg", "机具文件内容错误");
					return jsonMap;
				}
				// String no=agentName.substring(7);
				// String type=typeName.substring(7);
				List<SecretKey> list = new ArrayList<>();
				for (int i = 1; i <= row_num; i++) {
					Row row = sheet.getRow(i);
					String num1 = CellUtil.getCellValue(row.getCell(0));
					String num2 = CellUtil.getCellValue(row.getCell(1));
					String num3 = CellUtil.getCellValue(row.getCell(2));
					String num4 = CellUtil.getCellValue(row.getCell(3));
					String num5 = CellUtil.getCellValue(row.getCell(4));

					if (StringUtils.isBlank(num1))
						break;
					String[] str1 = num1.split("\\.");
					String[] str2 = num2.split("\\.");
					String[] str3 = num3.split("\\.");
					String[] str4 = num4.split("\\.");
					String[] str5 = num5.split("\\.");
					String device_id = str1[0];
					String key_type = str2[0];
					String key_content = str3[0];
					String device_type = str4[0];
					String check_value = str5[0];
					SecretKey sec = new SecretKey();
					sec.setDeviceId(device_id);
					sec.setKeyType(key_type);
					sec.setKeyContent(key_content);
					sec.setDeviceType(device_type);
					sec.setCheckValue(check_value);
					list.add(sec);
				}
				secretKeyService.insertAll((list));
				jsonMap.put("status", true);
				jsonMap.put("msg", "导入成功");
				return jsonMap;
			} else {
				jsonMap.put("status", false);
				jsonMap.put("msg", "机具密钥文件格式错误");
			}

		} catch (Exception e) {
			log.error("报错!!!", e);
			jsonMap.put("status", false);
		}
		return jsonMap;
	}

	/**
	 * 批量修改机具活动
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAllTerActivity")
	@SystemLog(description = "批量修改机具硬件种类及活动", operCode = "terminal.updateAllTerActivity")
	public @ResponseBody Map<String, Object> updateAllTerActivity(@RequestBody String param) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			resMap = terminalInfoService.updateAllTerActivity(param, true);
		} catch (Exception e) {
			log.error("==================>批量修改机具活动失败！", e);
			resMap.put("status", false);
			resMap.put("msg", "批量修改机具活动失败");
		}
		return resMap;
	}
	
	/**
	 * 批量修改机具活动（特殊）
	 * @author tans
	 * @date 2017年8月15日 上午11:31:19
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/updateTerminalSpecial")
	@SystemLog(description = "批量修改机具活动（特殊）", operCode = "terminal.updateTerminalSpecial")
	public @ResponseBody Map<String, Object> updateTerminalSpecial(@RequestBody String param) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			resMap = terminalInfoService.updateAllTerActivity(param, false);
		} catch (Exception e) {
			log.error("==================>批量修改机具活动失败！", e);
			resMap.put("status", false);
			resMap.put("msg", "批量修改机具活动失败");
		}
		return resMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getMbpByMerId")
	@ResponseBody
	public Object getMbpByMerId(String merId) {
		Map<String, Object> jsonMap = new HashMap<>();
		SelectParams selectParams = new SelectParams();
		selectParams.setMerchantNo(merId);
		Page<MerchantBusinessProduct> page1 = new Page<MerchantBusinessProduct>(1, 10000);
		List<MerchantBusinessProduct> list = merchantBusinessProductService.selectByParam(page1, selectParams);
		jsonMap.put("list", list);
		jsonMap.put("result", true);
		return jsonMap;
	}

	@RequestMapping(value = "/addSecret")
	@SystemLog(description = "机具管理生成密钥", operCode = "terminal.addkey")
	public void keyExport(@RequestParam String termianlType, @RequestParam String terminalCount,
			HttpServletResponse response, HttpServletRequest request) {
		try {
			// JSONObject params=JSON.parseObject(param);

			HardwareProduct hp = hardwareProductService.selectHardwareName(termianlType);

			String strDirPath = request.getSession().getServletContext().getRealPath("/");
			String temp = "/keysTemp/";
			String tmpPath = strDirPath + temp;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			int random = (int) (Math.random() * 1000);
			String fileName = sdf.format(new Date()) + random + ".txt";
			int num = Integer.parseInt(terminalCount);// 生成数量
			List<File> files = new ArrayList<File>();
			String file1Path = tmpPath + File.separator + "SECRET" + "_" + num + "_" + fileName;
			String file2Path = tmpPath + File.separator + "DIVNO" + "_" + num + "_" + fileName;
			/**
			 * 这个集合就是你想要打包的所有文件， 这里假设已经准备好了所要打包的文件
			 */

			File filet1 = new File(file1Path);
			File parentFile1 = filet1.getParentFile();
			// 如果路径不存在，则创建
			System.out.print("\n文件1的上一级路径： " + file1Path);
			if (!parentFile1.exists()) {
				parentFile1.mkdirs();
				System.out.println("\t不存在，开始新建");
			}
			File filet2 = new File(file2Path);
			File parentFile2 = filet2.getParentFile();
			// 如果路径不存在，则创建
			System.out.print("\n文件2的上一级路径： " + file2Path);
			if (!parentFile2.exists()) {
				parentFile2.mkdirs();
				System.out.println("\t不存在，开始新建");
			}

			files.add(filet1);
			files.add(filet2);
			List<String> list = new ArrayList<String>();
			List<String> listTow = new ArrayList<String>();
			List<Map<String, String>> secAndTerlist = new ArrayList<Map<String, String>>(); // 数据
			List<SecretKey> seclist = new ArrayList<SecretKey>(); // 数据

			/**
			 * 设备号，生成规则： 1、取机具类型首字母 （1位） 2、取 UUID 的 hashCode值 （10位，位数不够，高位补0）
			 * 3、取序列，从1开始，至生成数量最大数（5位，位数不够，高位补0）
			 *
			 */
			StringBuffer div_no_sb = new StringBuffer();
			DecimalFormat format2 = new DecimalFormat("00000");

			// String device = params.getString("device");//机具名称
			// String device_type = params.getString("device_type");//机具款式
			String mid = System.currentTimeMillis() + "";
			String type = "AE";// DictCache.getDictName(device)
			if (type.length() == 1) {
				mid = mid.substring(3, 13);
			} else if (type.length() == 2) {
				mid = mid.substring(4, 13);
			}
			String marker = hp.getFacturerCode();
			String device_type = hp.getModel();// 机具型号
			// 根据厂家获取传输密钥
			String maker_key = "";
			String key_type = "";
			String key_type2 = "";
			if ("ITRON".equals(marker)) {
				maker_key = Constants.ITRON_KEK;
				key_type = "TDK";
				if("M7".equals(device_type)) {
					key_type = "TMK";
				}
				type = "B1";
			} else if ("NEWLAND".equals(marker)) {
				maker_key = Constants.NEWLAND_KEK;
				key_type = "TMK";
				type = "D2";
			} else if ("BBPOS".equals(marker)) {
				maker_key = Constants.BBPOS_KEK;
				key_type = "TMK";
				type = "B2";
				/*
				 * if("SPOS".equals(device_type)){ key_type = "TDK"; }
				 */
			} else if ("TY".equals(marker)) {
				maker_key = Constants.TY_KEK;
				key_type = "TMK";
				type = "D1";
			} else if ("SHSF".equals(marker)) {
				maker_key = Constants.SHSF_KEK;
				key_type = "TMK";
				type = "D3";
			} else if ("SZAF".equals(marker)) {
				maker_key = Constants.SZAF_KEK;
				key_type = "TMK";
				type = "A1";
			}else{
				Map sf = secretKeyService.findSecretTsKey(marker);
				String sfKey = sf.get("key").toString();
				maker_key = JCEHandler.decryptData(sfKey,CS_KEK);
				key_type = "TMK";
				key_type2 = "TMK1";
				type = sf.get("sn").toString();

			}
			String end = null;

			try {
				for (int i = 0; i < num; i++) {
//				Map<String, String> secAndTerMap = new HashMap<String, String>();

					SecretKey sk = new SecretKey();
					Map<String, String> keyMap = getKeysCheckValue(maker_key);// 获取密钥
					String secretKey = keyMap.get("secretKey");// 密文
					String keys = keyMap.get("key");// 明文
					String check_value = keyMap.get("cv");// 检验值
					// 第一个密钥值start
					/*
					 * String keysStr =
					 * terminalService.getKeys1(maker_key);//得到密钥值和检验值 String[] key
					 * = keysStr.split("=="); String keys = key[0];//密钥值 String
					 * check_value = key[1].substring(0,8);//截取前8位,检验值
					 */ // 第一个密钥值end

					// 生成设备号 start
					String div_no = createDivNo(div_no_sb, format2, mid, type, i);
					// 生成设备号 end

					// String divs = div_no+"="+keys+" "+check_value;//明文密钥
					String divs_secret = div_no + "=" + secretKey + " " + check_value;// 密文密钥


//				secAndTerMap.put("device_id", div_no);
//				secAndTerMap.put("key_content", secretKey);
//				secAndTerMap.put("device_type", device_type);
//				secAndTerMap.put("check_value", check_value);
//				secAndTerMap.put("key_type", key_type);
					// secAndTerMap.put("pos_type", (String)
					// params.get("pos_type"));

					sk.setDeviceId(div_no);
					sk.setKeyContent(secretKey);
					sk.setDeviceType(device_type);
					sk.setCheckValue(check_value);
					sk.setKeyType(key_type);

					seclist.add(sk);

//				secAndTerlist.add(secAndTerMap);

					//如果硬件类型为双密钥，需要再生成一个
					if(hp.getSecretType() == 1){
						SecretKey sk2 = new SecretKey();
						Map<String, String> keyMap2 = getKeysCheckValue(maker_key);// 获取密钥
						String secretKey2 = keyMap2.get("secretKey");// 密文
						String keys2 = keyMap2.get("key");// 明文
						String check_value2 = keyMap2.get("cv");// 检验值

						divs_secret += " " + secretKey2 + " " + check_value2;// 密文密钥
//					list.add(divs_secret2);
//					listTow.add(div_no);

						sk2.setDeviceId(div_no);
						sk2.setKeyContent(secretKey2);
						sk2.setDeviceType(device_type);
						sk2.setCheckValue(check_value2);
						sk2.setKeyType(key_type2);

						seclist.add(sk2);
					}
					list.add(divs_secret);
					listTow.add(div_no);
				}
				writeToTxt2(file1Path, request, list);
				writeToTxt2(file2Path, request, listTow);
				// ===========================================================================================

				/**
				 * 创建一个临时压缩文件， 我们会把文件流全部注入到这个文件中 这里的文件你可以自定义是.rar还是.zip
				 */

				File file = new File(tmpPath + File.separator + "magazinePub.zip");
				File parentFile = file.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}

				response.reset();
				// response.getWriter()
				// 创建文件输出流
				FileOutputStream fous = new FileOutputStream(file);
				/**
				 * 打包的方法我们会用到ZipOutputStream这样一个输出流, 所以这里我们把输出流转换一下
				 */
				ZipOutputStream zipOut = new ZipOutputStream(fous);
				/**
				 * 这个方法接受的就是一个所要打包文件的集合， 还有一个ZipOutputStream
				 */
				zipFile(files, zipOut);
				zipOut.close();
				fous.close();
				downloadZip(file, response);
			} catch (Exception e) {
				log.info(e.getMessage());
			} finally {
				try {
					File f = new File(file1Path);
					File f2 = new File(file2Path);
					f.delete();
					f2.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 插入数据库secret_key and terminals
			// terminalService.addKeyAndTerminals(secAndTerlist);

			secretKeyService.insertAll(seclist);
		} catch (Exception e){
			log.error("生成密钥异常", e);
		}

	}

	private String createDivNo(StringBuffer div_no_sb, DecimalFormat format2, String mid, String type, int i) {
		String end;
		div_no_sb.setLength(0);
		// 取机具类型首字母
		div_no_sb.append(type);
		// 取 UUID 的 hashCode值 （10位，位数不够，高位补0）
		div_no_sb.append(mid);
		// 取序列，从1开始，至生成数量最大数（5位，位数不够，高位补0）
		end = format2.format(1 + i);
		div_no_sb.append(end);
		return div_no_sb.toString();
	}

	public void writeToTxt2(String filePath, HttpServletRequest request, List list) throws Exception {

		String enter = "\r\n";
		StringBuffer write;
		FileWriter fw = new FileWriter(filePath);
		try {
			for (int i = 0; i < list.size(); i++) {
				fw.write(list.get(i) + enter);
				fw.flush();
			}
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

			try {
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成密钥
	 * 
	 * @return
	 */
	public Map<String, String> getKeysCheckValue(String maker_key) {
		Map<String, String> keysMap = new HashMap<String, String>();
		UUID uuid = UUID.randomUUID();
		String key = uuid.toString().replace("-", "").toUpperCase();
		String cv = JCEHandler.encryptData("00000000000000000000000000000000", key);

		String secretKey = JCEHandler.encryptData(key, maker_key);

		keysMap.put("secretKey", secretKey);// 密文密钥
		keysMap.put("key", key);// 明文密钥
		keysMap.put("cv", cv.substring(0, 8));// 截取前8位,检验值
		return keysMap;
	}

	/**
	 * 把接受的全部文件打成压缩包
	 * 
	 * @param files
	 *            <File>;
	 * @param outputStream
	 *            .apache.tools.zip.ZipOutputStream
	 */
	public void zipFile(List files, ZipOutputStream outputStream) {
		int size = files.size();
		for (int i = 0; i < size; i++) {
			File file = (File) files.get(i);
			zipFile(file, outputStream);
		}
	}

	public HttpServletResponse downloadZip(File file, HttpServletResponse response) {
		try {
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();

			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				File f = new File(file.getPath());
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * 根据输入的文件与输出流对文件进行打包
	 * 
	 * @param inputFile
	 * @param ouputStream
	 *            .apache.tools.zip.ZipOutputStream
	 */
	public void zipFile(File inputFile, ZipOutputStream ouputStream) {
		try {
			if (inputFile.exists()) {
				/**
				 * 如果是目录的话这里是不采取操作的， 至于目录的打包正在研究中
				 */
				if (inputFile.isFile()) {
					FileInputStream IN = new FileInputStream(inputFile);
					BufferedInputStream bins = new BufferedInputStream(IN, 512);
					// org.apache.tools.zip.ZipEntry
					ZipEntry entry = new ZipEntry(inputFile.getName());
					ouputStream.putNextEntry(entry);
					// 向压缩文件中输出数据
					int nNumber;
					byte[] buffer = new byte[512];
					while ((nNumber = bins.read(buffer)) != -1) {
						ouputStream.write(buffer, 0, nNumber);
					}
					// 关闭创建的流对象
					bins.close();
					IN.close();
				} else {
					try {
						File[] files = inputFile.listFiles();
						for (int i = 0; i < files.length; i++) {
							zipFile(files[i], ouputStream);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/createActiveCode.do")
	@ResponseBody
	public Map<String, Object> createActiveCode(Integer codeNumber, String hpId) {
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = terminalInfoService.insertBatch(codeNumber, hpId);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "添加成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "添加失败");
			}
		} catch (Exception e) {
			log.error("批量添加收款码失败");
			e.printStackTrace();
			msg.put("status", false);
			msg.put("msg", "服务器异常");
		}
		return msg;
	}

	// 导出机具SN和激活码 excel格式，现在需求导出成.csv
	// @RequestMapping(value = "exportTerminalInfo.do")
	// @SystemLog(description = "导出机具SN和激活码",operCode="terminal.exportCode")
	// public void exportSubjectDayAmount(@RequestParam String
	// ids,HttpServletResponse response,HttpServletRequest request) throws
	// IOException {
	// List<TerminalInfo> list = new ArrayList<>();
	// if(StringUtils.isNotBlank(ids)){
	// String[] idList = ids.split(",");
	// list = terminalInfoService.getByIds(idList);
	// } else {
	// throw new RuntimeException("没有可导出的数据");
	// }
	// SysDict sysDict = sysDictService.getByKey("GATHER_CODE_IP");
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
	// String fileName = "机具激活码"+sdf.format(new Date())+".xls" ;
	// String fileNameFormat = new
	// String(fileName.getBytes("GBK"),"ISO-8859-1");
	// response.setHeader("Content-disposition",
	// "attachment;filename="+fileNameFormat);
	// List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
	// for(TerminalInfo info:list){
	// Map<String,String> map = new HashMap<String,String>() ;
	// if(sysDict!=null){
	// map.put("PSAM_NO",
	// sysDict.getSysValue()+"/gather/gatherProcess?source=3&settleMent=0&gatherCode="+info.getPsamNo());
	// }
	// map.put("SN", info.getSn());
	// data.add(map) ;
	// }
	//
	// ListDataExcelExport export = new ListDataExcelExport();
	// String[] cols = new String[]{"PSAM_NO","SN"};
	// String[] colsName = new String[]{"激活码","SN号"};
	// OutputStream ouputStream = response.getOutputStream();
	// export.export(cols, colsName, data, response.getOutputStream());
	// ouputStream.close();
	// }

	/**
	 * 导出激活码
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "exportTerminalInfo.do")
	@SystemLog(description = "导出机具SN和激活码", operCode = "terminal.exportCode")
	public void exportCsv(@RequestParam String ids, HttpServletResponse response) {
		BufferedWriter out = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "JH" + sdf.format(new Date()) + ".csv";
		try {
			List<TerminalInfo> list = new ArrayList<>();
			if (StringUtils.isNotBlank(ids)) {
				String[] idList = ids.split(",");
				list = terminalInfoService.getByIds(idList);
			} else {
				throw new RuntimeException("没有可导出的数据");
			}
			SysDict sysDict = sysDictService.getByKey("GATHER_CODE_IP");
			fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			out = new BufferedWriter(response.getWriter());
			if (list != null && !list.isEmpty()) {
				out.write("SN,PASM\r");
				for (TerminalInfo data : list) {
					out.append(data.getSn()).append(",").append("http://").append(sysDict.getSysValue())
							.append("/gather/gatherProcess?source=3&settleMent=0&gatherCode=").append(data.getPsamNo())
							.append("\r");
				}
			}
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/createQrcod")
	@ResponseBody
	public Map<String, Object> createQrcod(String qrCode) {
		Map<String, Object> msg = new HashMap<>();
		try {
			// qrCode = "jhbe08l5gz";
			// QRCodeUtil.encode("http://192.168.3.160:8080/boss2/terminalInfo/qrcod?qrCode=jhbe08l5gz","","d:/MyWorkDoc",true);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String fileName = sdf.format(new Date()) + ".jpg";
			QrCodeUtil.create("0000/core2/gather/getGatherCode?gatherCode=" + qrCode, null,
					"d:/boss2Qrcode/" + fileName, true);
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			e.printStackTrace();
		}
		return msg;
	}

	String pcUrl = "/terminalInfo/creatReceivableCode";

	@RequestMapping(value = "/creatReceivableCode")
	@ResponseBody
	public Map<String, Object> creatReceivableCode(String merchantNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			String receivableCode = terminalInfoService.getReceivableCodeByMerchant(merchantNo);
			if (StringUtils.isNotBlank(receivableCode)) {
				msg.put("msg", receivableCode);
				msg.put("status", true);
			} else {
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", false);
			log.error("根据商户查询收款码失败");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getActiveCode")
	@ResponseBody
	public Map<String, Object> getActiveCode(String sn) {
		Map<String, Object> msg = new HashMap<>();
		if (StringUtils.isBlank(sn)) {
			msg.put("status", false);
		}
		try {
			String activeCode = terminalInfoService.getActiveCodeBySn(sn);
			if (StringUtils.isNotBlank(activeCode)) {
				msg.put("activeCode", activeCode);
				msg.put("status", true);
			} else {
				msg.put("status", false);
				msg.put("msg", "未查到激活码");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "服务异常");
		}
		return msg;
	}

	// 229tgh查询所有机具活动
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllActivityType.do")
	public @ResponseBody List<Map<String, String>> selectAllActivityType() {
		List<Map<String, String>> map = null;
		try {
			map = terminalInfoService.selectAllActivityType();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询机具活动失败-----", e);
		}

		return map;
	}

	/**
	 * 机具导入模板下载
	 * 
	 * @author tans
	 * @date 2017年3月28日 下午5:46:49
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/downloadTerminalTemplate")
	public String downloadTerminalTemplate(HttpServletRequest request, HttpServletResponse response) {
		String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator
				+ "terminalBatchImport.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath, "导入机具模版.xlsx");
		return null;
	}

	/**
	 * 机具批量绑定
	 * 
	 * @author tans
	 * @date 2017年4月26日 下午2:43:33
	 * @param file
	 * @param agentNo
	 * @param merchantNo
	 * @param bpId
	 * @return
	 */
	@RequestMapping(value = "/terminalBindBatch")
	@ResponseBody
	public Map<String, Object> bindBatch(@RequestParam("file") MultipartFile file,
			@RequestParam("agentNo") String agentNo, @RequestParam("merchantNo") String merchantNo,
			@RequestParam("bpId") String bpId) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		if (file.isEmpty()) {
			msg.put("msg", "机具批量绑定失败,请上传需要导入的Excel文件");
			return msg;
		} else {
			String fileSuffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			long fileSize = file.getSize();
			if (!fileSuffixName.equals(".xls") && !fileSuffixName.equals(".xlsx")) {
				msg.put("msg", "机具批量绑定失败,请上传文件后缀为:.xls 或 .xlxs的Excel文件");
				return msg;
			}
			if (((fileSize / 1024) / 1024) > 4) {
				msg.put("msg", "上传文件过大,请上传4MB以内的文件");
				return msg;
			}
			try {
				msg = terminalInfoService.bindBatch(file.getInputStream(), agentNo, merchantNo, bpId);
				return msg;
			} catch (Exception e) {
				log.error("机具批量绑定失败,系统出现未知异常, ", e);
				msg.put("msg", "机具批量绑定失败,系统异常");
				return msg;
			}
		}
	}

}
