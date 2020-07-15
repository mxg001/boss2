package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Controller
@RequestMapping(value = "riskRollAction")
public class RiskRollAction {
	private static final Logger log = LoggerFactory.getLogger(RiskRollAction.class);

	@Resource
	private RiskRollService riskRollService;

	@Resource
	private MerchantInfoService merchantInfoService;

	@Resource
	private SysDictService sysDictService;

	@Resource
	private TransInfoService transInfoService;

	@Resource
	private AutoTransferService autoTransferService;
	
	@Resource
	private MsgService msgService;
	


	/**
	 * 名单初始化和模糊查询分页
	 * 
	 * @param page
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectRollAllInfo")
	@ResponseBody
	public Object selectRollAllInfo(@ModelAttribute("page") Page<RiskRoll> page, @RequestParam("info") String param)
			throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			RiskRoll rr = JSON.parseObject(param, RiskRoll.class);
			riskRollService.selectRollAllInfo(page, rr);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("名单查询报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/** 查黑名单的操作日志 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectLogsByRollNo")
	@ResponseBody
	public Object selectLogsByRollNo(@ModelAttribute("page") Page<BlackOperLog> page,
			@RequestParam("rollNo") String rollNo) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			riskRollService.selectBlackLogs(page, rollNo);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("黑名单的操作日志查询报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 名单明细模糊查询分页
	 * 
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectRollListAllInfo")
	@ResponseBody
	public Object selectRollListAllInfo(@ModelAttribute("page") Page<RiskRollList> page,
			@RequestParam("roll") String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			RiskRollList rrl = JSON.parseObject(param, RiskRollList.class);
			riskRollService.selectRollList(page, rrl);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("名单明细查询报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 名单详情查询
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectRollDetail")
	@ResponseBody
	public Object selectRollDetail(@RequestParam("ids") String ids) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			int id = JSON.parseObject(ids, Integer.class);
			RiskRoll rr = riskRollService.selectRollDetail(id);
			if (rr == null) {
				jsonMap.put("msg", "查询失败~~~~~");
				jsonMap.put("bols", false);
			} else {
				jsonMap.put("result", rr);
				jsonMap.put("bols", true);
			}
		} catch (Exception e) {
			log.error("名单详情查询报错!!!", e);
			jsonMap.put("msg", "查询报错~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 黑名单查询导出 by zouruijin zrj@eeepay.cn rjzou@qq.com
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportInfo.do")
	@ResponseBody
	@SystemLog(description = "黑名单查询导出", operCode = "riskRoll.export")
	public void exportInfo(HttpServletResponse response, HttpServletRequest request, @RequestParam("info") String param)
			throws Exception {
		RiskRoll rr = JSON.parseObject(param, RiskRoll.class);
		List<RiskRollExport> list = riskRollService.selectRollAllInfoExport(rr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "黑名单查询导出" + sdf.format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list == null || list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("rollNo", null);
			maps.put("rollTypeName", null);
			maps.put("rollStatus", null);
			maps.put("createTime", null);
			maps.put("remark", null);
			maps.put("userMsg", null);
			data.add(maps);
		} else {
			for (int i = 0; i < list.size(); i++) {
				RiskRollExport ami = list.get(i);
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", ami.getId()==null?"":ami.getId().toString());
				maps.put("rollNo", ami.getRollNo());
				maps.put("rollTypeName", ami.getRollTypeName());
				maps.put("rollStatus", ami.getRollStatus()==null?"":ami.getRollStatus().toString());
				maps.put("createTime", ami.getCreateTime()==null?"":DateUtil.getLongFormatDate(ami.getCreateTime()));
				maps.put("remark", ami.getRemark());
				maps.put("userMsg", ami.getUserMsg());
				data.add(maps);
			}
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "id", "rollNo", "rollTypeName", "rollStatus", "createTime", "remark","userMsg" };
		String[] colsName = new String[] { "ID", "商户编号/身份证号/银行卡号", "黑名单类型", "状态", "创建时间", "备注","代理商提示语" };
		double[] cellWidth = { 3000, 10000, 6000, 6000, 6000, 12000,12000 };
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, cellWidth, data, ouputStream);
		ouputStream.close();
	}

	/**
	 * 修改名单状态
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateRollStatus")
	@ResponseBody
	@SystemLog(description = "名单状态开关", operCode = "blackList.switch")
	public Object updateRollStatus(@RequestBody RiskRoll rr) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {
			if (rr.getRollStatus() == null) {
				jsonMap.put("msg", "信息不对~~~~");
			} else {
				int i = riskRollService.updateRollStatus(rr);
				if (i > 0) {
					jsonMap.put("bols", true);
					// 修改成功记录黑名单操作日志
					BlackOperLog b = new BlackOperLog();
					b.setRollNo(rr.getRollNo());
					b.setBlackType(String.valueOf(rr.getRollType()));
					b.setCreateTime(new Date());
					// 操作类型
					if (rr.getRollStatus() == 0) {
						b.setOperationType("关闭黑名单状态");
					} else {
						b.setOperationType("打开黑名单状态");
					}
					final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext()
							.getAuthentication().getPrincipal();
					b.setCreateBy(principal.getRealName());
					b.setRemark(rr.getRemark());
					riskRollService.insertBlackLog(b);
					//1.4 改为走风控定时任务
					if (rr.getRollStatus() == 0) {
						List<Map<String, Object>> transferList = transInfoService.queryResetTransfer(rr.getRollNo());
						/*if(transferList != null && transferList.size() >0){
							if("1".equals(rr.getRollType()+"")){
								for(Map<String, Object> map:transferList){
									ClientInterface.resetTransfer(map.get("id").toString(),"transfer_relieve_merchant_blacklist");
								}
							}else if("4".equals(rr.getRollType()+"")){
								for(Map<String, Object> map:transferList){
									ClientInterface.resetTransfer(map.get("id").toString(),"transfer_relieve_purse_blacklist");
								}
							}else if("7".equals(rr.getRollType()+"")){
								for(Map<String, Object> map:transferList){
									ClientInterface.resetTransfer(map.get("id").toString(),"transfer_relieve_quick_pass_blacklist");
								}
							}*/

							if("7".equals(rr.getRollType()+"")){
								for(Map<String, Object> map:transferList){
									ClientInterface.resetTransfer(map.get("id").toString(),"transfer_relieve_quick_pass_blacklist");
								}

							}
					}

					String rollType = rr.getRollType().toString();
					if("1".equals(rollType) || "4".equals(rollType)){
						if(rr.getRollStatus() == 0 ){
							// 解除黑名单，根据商户号检索出T0交易订单，并写入auto_transfer表
							SysDict sysDict = sysDictService.getByKey("RE_SETTLE_TASK_COUNT");
							int reSettleTaskCount = new Integer(sysDict.getSysValue()).intValue();
							// T0订单：当前时间-RE_SETTLE_TASK_COUNT 之前的T0订单
							List<CollectiveTransOrder> list = transInfoService.selectT0ByMerchantNoAndTransTime(rr.getRollNo(), reSettleTaskCount);
							// T1订单：当前时间8天前的所有订单，结算状态为：未结算、结算失败、转t1
							List<CollectiveTransOrder> t1s = transInfoService.selectT1ByMerchantNo(rr.getRollNo());
							//T0订单：当前时间8天前的所有订单，结算状态为：转t1
							List<CollectiveTransOrder> toT1s = transInfoService.selectToT1ByMerchantNo(rr.getRollNo());
							list.addAll(t1s);
							list.addAll(toT1s);
							if(null != list && list.size() > 0){
								for (CollectiveTransOrder cto : list) {
									// 判断auto_transfer是否已经存在
									String id = autoTransferService.selectByOrderNoAndStatus(cto.getOrderNo(), "1");
									if(StringUtils.isBlank(id)){
										String settleType = cto.getSettlementMethod();
										if("0".equals(cto.getSettlementMethod()) && "4".equals(cto.getSettleStatus())){ //T0转T1的存2
											settleType = "2";
										}
										autoTransferService.insertOne(cto.getOrderNo(), cto.getMerchantNo(), "1",settleType, "1", principal.getUsername());
									}
								}
							}
						}else {
							// 商户黑名单：检索auto_transfer表中是否存在该商户且状态为待执行的任务，将状态改为再风控
							autoTransferService.updateByMerchantNoAndStatus(rr.getRollNo(), "1");
						}
					}

					jsonMap.put("msg", "修改成功");
				} else {
					jsonMap.put("bols", false);
					jsonMap.put("msg", "修改失败");
				}
			}
		} catch (Exception e) {
			log.error("修改名单状态报错!!!", e);
			jsonMap.put("bols", false);
			String str = e.getMessage();
			if (e.getMessage() == null) {
				jsonMap.put("msg", "修改名单状态异常");
				return jsonMap;
			}
			if (str.contains("\r\n") || str.contains("\n"))
				jsonMap.put("msg", "修改名单状态异常");
			else
				jsonMap.put("msg", str);
		}
		return jsonMap;
	}

	/**
	 * 修改名单信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateRollInfo")
	@ResponseBody
	@SystemLog(description = "修改名单", operCode = "blackList.update")
	public Object updateRollInfo(@RequestParam("info") String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			RiskRoll rr = JSON.parseObject(param, RiskRoll.class);
			if (rr == null) {
				jsonMap.put("bols", false);
				jsonMap.put("msg", "信息出错~~~~");
			} else {
				int i = riskRollService.updateRollInfo(rr);
				if (i > 0) {
					jsonMap.put("bols", true);
					jsonMap.put("msg", "修改成功~~~~");
					BlackOperLog b = new BlackOperLog();
					b.setRollNo(rr.getRollNo());
					b.setBlackType(String.valueOf(rr.getRollType()));
					b.setCreateTime(new Date());
					b.setOperationType("修改黑名单");
					final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext()
							.getAuthentication().getPrincipal();
					b.setCreateBy(principal.getRealName());
					b.setRemark(rr.getRemark());
					riskRollService.insertBlackLog(b);
				} else {
					jsonMap.put("bols", false);
					jsonMap.put("msg", "修改失败~~~~");
				}
			}
		} catch (Exception e) {
			log.error("修改名单信息报错!!!", e);
			jsonMap.put("msg", "修改报错~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 新增名单信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addRollInfo")
	@ResponseBody
	@SystemLog(description = "新增名单信息", operCode = "blackList.insert")
	public Object addRollInfo(@RequestParam("info") String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			RiskRoll rr = JSON.parseObject(param, RiskRoll.class);
			if (rr == null) {
				jsonMap.put("bols", false);
				jsonMap.put("msg", "信息出错~~~~");
				return jsonMap;
			}
			if(rr.getRollType()!=null&&rr.getRollBelong()!=null){
				//--白名单 4 5 6,黑名单 1 4 7
				if (1==rr.getRollType().intValue()
						|| 4==rr.getRollType().intValue()
						|| 7==rr.getRollType().intValue()
						|| 5==rr.getRollType().intValue()
						|| 6==rr.getRollType().intValue()
						) {
					if (merchantInfoService.selectByMerNo(rr.getRollNo()) == null) {
						jsonMap.put("bols", false);
						jsonMap.put("msg", "该商户不存在");
						return jsonMap;
					}
				}
			}
			RiskRoll oldInfo = riskRollService.checkRollByRollNo(rr.getRollNo(),rr.getRollType(),rr.getRollBelong());
			if(oldInfo!=null){
				if (1==rr.getRollBelong().intValue()) {
					jsonMap.put("msg", "当前白名单已存在，类型为" + oldInfo.getRollTypeName());
				}else{
					jsonMap.put("msg", "当前黑名单已存在，类型为" + oldInfo.getRollTypeName());
				}

				jsonMap.put("bols", false);
				jsonMap.put("msg", "该商户已存在商户编号黑名单,不能导入");
				return jsonMap;
			}

			/*// 黑名单 商户编号 1 与 钱包出账 4 互斥
			if(2==rr.getRollBelong()){
				if(1==rr.getRollType()){
					RiskRoll mutexInfo = riskRollService.checkRollByRollNo(rr.getRollNo(), 4, 2);
					if (mutexInfo != null) {
						jsonMap.put("bols", false);
						jsonMap.put("msg", "该商户已存在钱包出账黑名单,不能导入");
						return jsonMap;
					}
				}else if (4==rr.getRollType()){
					RiskRoll mutexInfo = riskRollService.checkRollByRollNo(rr.getRollNo(), 1, 2);
					if (mutexInfo != null) {
						jsonMap.put("bols", false);
						jsonMap.put("msg", "该商户已存在商户编号黑名单,不能导入");
						return jsonMap;
					}
				}
			}*/

			rr.setCreatePerson(principal.getId().toString());
			rr.setRollStatus(0);//默认关闭
			int num=riskRollService.insertRoll(rr);
			if(num>0){
				if(2==rr.getRollBelong().intValue()){
					// 添加成功记录操作日志
					BlackOperLog b = new BlackOperLog();
					b.setRollNo(rr.getRollNo());
					b.setBlackType(String.valueOf(rr.getRollType()));
					b.setCreateTime(new Date());
					b.setOperationType("添加黑名单");
					b.setCreateBy(principal.getRealName());
					b.setRemark(rr.getRemark());
					riskRollService.insertBlackLog(b);
				}
				jsonMap.put("msg", "新增成功");
				jsonMap.put("bols", true);
			}else{
				jsonMap.put("bols", false);
				jsonMap.put("msg", "新增失败");
			}
		} catch (Exception e) {
			log.error("新增名单信息异常", e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "新增名单信息异常");
		}
		return jsonMap;
	}

	/**
	 * 删除名单
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteByid")
	@ResponseBody
	@SystemLog(description = "删除名单", operCode = "blackList.delete")
	public Object deleteById(@RequestParam("ids") String ids) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			int id = JSON.parseObject(ids, Integer.class);
			riskRollService.deleteRollListInfo(id,jsonMap);
		} catch (Exception e) {
			log.error("删除名单报错!!!", e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "删除名单异常!");
		}
		return jsonMap;
	}

	@RequestMapping(value = "/delBatch")
	@ResponseBody
	@SystemLog(description = "名单批量删除", operCode = "blackList.deleteBatch")
	public Object delBatch(@RequestParam("ids") String ids) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			String idStr = JSON.parseObject(ids, String.class);
			riskRollService.deleteBatch(idStr,jsonMap);
		} catch (Exception e) {
			log.error("删除名单异常", e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "删除名单异常");
		}
		return jsonMap;
	}

	@RequestMapping(value = "/openBatch")
	@ResponseBody
	@SystemLog(description = "名单批量打开", operCode = "blackList.openBatch")
	public Object openBatch(@RequestParam("ids") String ids) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			String idStr = JSON.parseObject(ids, String.class);
			int i = riskRollService.updateOpenBatch(idStr);
			if (i > 0) {
				jsonMap.put("bols", true);
				jsonMap.put("msg", "批量打开成功");
			} else {
				jsonMap.put("bols", false);
				jsonMap.put("msg", "批量打开失败");
			}
		} catch (Exception e) {
			log.error("批量打开名单异常", e);
			jsonMap.put("bols", false);
			String str = e.getMessage();
			if (e.getMessage() == null) {
				jsonMap.put("msg", "批量打开名单异常");
				return jsonMap;
			}
			if (str.contains("\r\n") || str.contains("\n"))
				jsonMap.put("msg", "打开名单异常");
			else
				jsonMap.put("msg", str);
		}
		return jsonMap;
	}

	/**
	 * 下载模板
	 */
	@RequestMapping("/downloadTemplate")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response) {
		String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator
				+ "blacklistTemplate.xls";
		log.info(filePath);
		ResponseUtil.download(response, filePath, "批量导入黑名单模板.xls");
		return null;
	}

	@RequestMapping(value = "/importBlacklist")
	@ResponseBody
	@SystemLog(description = "黑名单批量导入", operCode = "risk.blacklistimport")
	public Object blacklistimport(@RequestParam("file") MultipartFile file, @RequestParam("acqOrg") String acqOrg)
			throws Exception {
		Map<String, Object> jsonMap= new HashMap<>();
		Workbook wb=null;
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					jsonMap.put("result", false);
					jsonMap.put("msg", "导入文件格式错误!");
					return jsonMap;
				}
			}

			wb = WorkbookFactory.create(file.getInputStream());
			//读取第一个sheet
			Sheet sheet = wb.getSheetAt(0);
			// 遍历所有单元格，读取单元格
			int row_num = sheet.getLastRowNum();

			if(row_num>50000){
				jsonMap.put("result", false);
				jsonMap.put("msg", "批量导入,最大条数50000条!");
				return jsonMap;
			}

			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<String, String> riskRollTypeMap=sysDictService.selectMapKeyAndName("ROLL_TYPE",false);//黑名单类型
			List<Msg> msgs = msgService.queryAgentTips();
			Map<String,String> rollMsgMap= new HashMap<>();
			if(msgs!=null && msgs.size()>0){
				for (Msg msg : msgs) {
					rollMsgMap.put(msg.getUserMsg(),msg.getMsgCode());
				}
			}

			Map<String,Integer> statusMap = new HashMap<String,Integer>();
			statusMap.put("0", 0);
			statusMap.put("1", 1);

			int successCount = 0;
			int failCount = 0 ;
			List<String> msgList = new ArrayList<>();

			for (int i = 1; i <= row_num; i++) {

				Row row = sheet.getRow(i);
				String rollNo = CellUtil.getCellValue(row.getCell(0));//商户编号   roll_no
				String rollType = CellUtil.getCellValue(row.getCell(1));//黑名单类型   roll_type
				String rollStatus = CellUtil.getCellValue(row.getCell(2));//状态   roll_status
				String remark = CellUtil.getCellValue(row.getCell(3));//备注   remark
				String rollMsg = CellUtil.getCellValue(row.getCell(4));//代理商提示语   roll_msg

				if (StringUtils.isBlank(rollNo)) {
					failCount++;
					jsonMap.put("result", false);
					msgList.add("导入失败,第" + (i + 1) + "行,商户编号/身份证号/银行卡号为空!");
					continue;
				}
				if (StringUtils.isBlank(rollType)) {
					failCount++;
					jsonMap.put("result", false);
					msgList.add("导入失败,第" + (i + 1) + "行,黑名单类型为空!");
					continue;
				}
				if (StringUtils.isBlank(rollStatus)) {
					failCount++;
					jsonMap.put("result", false);
					msgList.add("导入失败,第" + (i + 1) + "行,状态为空!");
					continue;
				}

				if (riskRollTypeMap.get(rollType) == null) {
					failCount++;
					jsonMap.put("result", false);
					msgList.add( "导入失败,第" + (i + 1) + "行,黑名单类型不存在!");
					continue;
				}
				int rollTypeInt = Integer.parseInt(riskRollTypeMap.get(rollType));

				if(rollTypeInt==8 || rollTypeInt==9 || rollTypeInt==10){
					if(StringUtil.isBlank(rollMsg)){
						failCount++;
						jsonMap.put("result", false);
						msgList.add("导入失败,第" + (i + 1) + "行,代理商提示语为空!");
						continue;
					}
					String msgCode = rollMsgMap.get(rollMsg);
					if(StringUtil.isBlank(msgCode)){
						failCount++;
						jsonMap.put("result", false);
						msgList.add("导入失败,第" + (i + 1) + "行,代理商提示语不存在!");
						continue;
					}
				}

				if (statusMap.get(rollStatus) == null) {
					failCount++;
					jsonMap.put("result", false);
					msgList.add( "导入失败,第" + (i + 1) + "行,状态不存在!");
					continue;
				}
				int rollStatusInt = statusMap.get(rollStatus).intValue();

				if (1 == rollTypeInt || 4 == rollTypeInt || 7 == rollTypeInt) {
					if (merchantInfoService.selectByMerNo(rollNo) == null) {
						failCount++;
						jsonMap.put("result", false);
						msgList.add( "导入失败,第" + (i + 1) + "行,该商户不存在!");
						continue;
					}
				}
				RiskRoll oldInfo = riskRollService.checkRollByRollNo(rollNo, rollTypeInt, 2);
				if (oldInfo != null) {
					//jsonMap.put("result", false);
					//jsonMap.put("msg", "导入失败,第" + (i + 1) + "行,商户编号/身份证号/银行卡号已存在!");
					//return jsonMap;
					//2020-2-13 已经存在,下一行
					failCount++;
					jsonMap.put("result", false);
					msgList.add("导入失败,第" + (i + 1) + "行,"+rollNo+"商户编号/身份证号/银行卡号已存在!");
					continue;
				}

				/*// 商户编号 1 与 钱包出账 4 互斥
				if(1==rollTypeInt){
					RiskRoll mutexInfo = riskRollService.checkRollByRollNo(rollNo, 4, 2);
					if (mutexInfo != null) {
						failCount++;
						jsonMap.put("result", false);
						msgList.add("黑名单类型:钱包出账;导入失败,第" + (i + 1) + "行,"+rollNo+"商户编号/钱包出账黑名单已存在!");
						continue;
					}
				}else if (4==rollTypeInt){
					RiskRoll mutexInfo = riskRollService.checkRollByRollNo(rollNo, 1, 2);
					if (mutexInfo != null) {
						failCount++;
						jsonMap.put("result", false);
						msgList.add("黑名单类型:商户编号;导入失败,第" + (i + 1) + "行,"+rollNo+"商户编号/钱包出账黑名单已存在!");
						continue;
					}
				}*/


				RiskRoll addInfo = new RiskRoll();
				addInfo.setRollNo(rollNo);
				addInfo.setRollType(rollTypeInt);
				addInfo.setRollStatus(rollStatusInt);
				addInfo.setRollBelong(2);
				addInfo.setRemark(remark);
				addInfo.setCreatePerson(principal.getId().toString());
				if(rollTypeInt==8 || rollTypeInt==9 || rollTypeInt==10){
					addInfo.setRollMsg(rollMsgMap.get(rollMsg));
					addInfo.setRollUser("2");
				}else{
					addInfo.setRollUser("1");
				}


				if(1==addInfo.getRollStatus()) {
					MerchantInfo merInfo = merchantInfoService.selectMerExistByMerNo(addInfo.getRollNo());
					String finalRiskStatus = null;
					if (merInfo != null) {
						if (1 == addInfo.getRollType().intValue()) {//当商户黑名单
							finalRiskStatus = "3";
						} else if (4 == addInfo.getRollType().intValue()) {//钱包出账
							RiskRoll recordSH = riskRollService.checkRollByRollNo(addInfo.getRollNo(), 1, 2);
							if (recordSH != null && 1 == recordSH.getRollStatus().intValue()) {
								finalRiskStatus = "3";
							} else {
								finalRiskStatus = "2";
							}
						}
					}
					if(finalRiskStatus!=null&&!finalRiskStatus.equals(merInfo.getRiskStatus())){
						String accountStatus=null;
						if("1".equals(finalRiskStatus)){
							accountStatus="1";
						}else if("2".equals(finalRiskStatus)){
							accountStatus="3";
						}else if("3".equals(finalRiskStatus)){
							accountStatus="4";
						}
						final HashMap<String, Object> claims = new HashMap<String, Object>();
						claims.put("subjectNo", "224101001");
						claims.put("accountType", "M");
						claims.put("currencyNo", "1");
						claims.put("accountOwner", "000001");
						claims.put("selectType", "2");
						claims.put("accountNo", null);
						claims.put("cardNo", null);
						claims.put("userId", addInfo.getRollNo());
						claims.put("accountStatus", accountStatus);
						String result = ClientInterface.transFrozenAccountNo(claims);//锁定账户
						if(StringUtil.isBlank(result)){
							failCount++;
							jsonMap.put("result", false);
							msgList.add("导入失败,第" + (i + 1) + "行,锁定账户异常!");
							continue;
						}
						JSONObject json=JSON.parseObject(result);
						if((boolean)json.get("status")){
							merchantInfoService.updateRiskStatusbyBlack(addInfo.getRollNo(), finalRiskStatus);
						}else{
							failCount++;
							jsonMap.put("result", false);
							msgList.add ("导入失败,第" + (i + 1) + "行,锁定账户失败!");
							continue;
						}
					}
				}

				int num=riskRollService.insertRoll(addInfo);
				if(num>0){
					// 添加成功记录操作日志
					BlackOperLog b = new BlackOperLog();
					b.setRollNo(addInfo.getRollNo());
					b.setBlackType(String.valueOf(addInfo.getRollType()));
					b.setCreateTime(new Date());
					b.setOperationType("添加黑名单");
					b.setCreateBy(principal.getRealName());
					b.setRemark(addInfo.getRemark());
					riskRollService.insertBlackLog(b);
					successCount++;
				}else{
					msgList.add ("导入失败,第" + (i + 1) + "行,报存数据库出错!");
					failCount++;
				}

			}
			jsonMap.put("successCount", successCount);
			jsonMap.put("failCount", failCount);

			StringBuffer sb = new StringBuffer();

			for (String str: msgList) {
				sb.append(str).append("\\n");
			}

			jsonMap.put("msg", sb.toString());
			return jsonMap;
		} catch (Exception e) {
			log.error("报错", e);
			jsonMap.put("result", false);
			jsonMap.put("msg", "导入异常");
		} finally {
			if (wb != null){
				wb.close();
			}
		}
		return jsonMap;
	}

	private String getCellValue(Cell cell) {
		if (cell==null) {
			return null;
		}
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