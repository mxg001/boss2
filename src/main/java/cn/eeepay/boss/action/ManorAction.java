package cn.eeepay.boss.action;


import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ManorService;
import cn.eeepay.framework.service.RedService;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 红包领地功能
 * @author zxs
 * @date 2018-6-19
 */
@Controller
@RequestMapping("/manor")
public class ManorAction {

	private Logger log = LoggerFactory.getLogger(ManorAction.class);

	@Resource
	private RedService redService;
	@Resource
	private ManorService manorService;


	@RequestMapping("/manorMoney")
	@ResponseBody
	public Result manorMoney(@RequestParam(defaultValue = "1") int pageNo,
							 @RequestParam(defaultValue = "10") int pageSize,
							 @RequestParam("baseInfo") String param ){
		Result result = new Result();
		try{

			ManorMoney baseInfo = JSONObject.parseObject(param, ManorMoney.class);

			Page<ManorMoney> page = new Page<>(pageNo, pageSize);
			manorService.selectManorMoneyPage(baseInfo, page);
			ManorMainSum manorMainSum = manorService.selectManorMoneySum(baseInfo);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("manorMainSum", manorMainSum);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询领主领地收益表异常", e);
		}
		return result;
	}

	@RequestMapping("/dailyDividend")
	@ResponseBody
	public Result dailyDividend(@RequestParam(defaultValue = "1") int pageNo,
								@RequestParam(defaultValue = "10") int pageSize,
								@RequestBody RedTerritoryBonusEveryday redTerritoryBonusEveryday ){
		Result result = new Result();
		try{
			Page<RedTerritoryBonusEveryday> page = new Page<>(pageNo, pageSize);
			manorService.selectDailyDividendPage(redTerritoryBonusEveryday, page);
			ManorMainSum manorMainSum = manorService.selectOrderSum(redTerritoryBonusEveryday);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("manorMainSum", manorMainSum);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询领主领地收益表异常", e);
		}
		return result;
	}
	@RequestMapping("/dailyBusinessDividend")
	@ResponseBody
	public Result dailyBusinessDividend(@RequestParam(defaultValue = "1") int pageNo,
								@RequestParam(defaultValue = "10") int pageSize,
								@RequestBody RedTerritoryBonusEveryday redTerritoryBonusEveryday ){
		Result result = new Result();
		try{
			Page<RedTerritoryBonusDetail> page = new Page<>(pageNo, pageSize);
			manorService.selectDailyBusinessPage(redTerritoryBonusEveryday, page);
			ManorMainSum manorMainSum = manorService.selectOrderSum(redTerritoryBonusEveryday);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("manorMainSum", manorMainSum);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询领主领地收益表异常", e);
		}
		return result;
	}


	@RequestMapping("/manorMoneyDetl")
	@ResponseBody
	public Result manorMoneyDetl(@RequestParam(defaultValue = "1") int pageNo,
								 @RequestParam(defaultValue = "10") int pageSize,
								 @RequestParam("baseInfo") String param){
		Result result = new Result();
		try{
			ManorMoney baseInfo = JSONObject.parseObject(param, ManorMoney.class);
			Page<ManorMoney> page = new Page<>(pageNo, pageSize);
			manorService.selectManorMoneyDetlPage(baseInfo, page);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询转入红包账户明细异常", e);
		}
		return result;
	}

	@RequestMapping("/transactionRecore")
	@ResponseBody
	public Result transactionRecore(@RequestParam(defaultValue = "1") int pageNo,
									@RequestParam(defaultValue = "10") int pageSize,
									@RequestParam("baseInfo") String param){
		Result result = new Result();
		try{
			ManorTransactionRecore baseInfo = JSONObject.parseObject(param, ManorTransactionRecore.class);
			Page<ManorTransactionRecore> page = new Page<>(pageNo, pageSize);
			baseInfo.setType(0);
			manorService.selectManorTransactionRecorePage(baseInfo, page);
			baseInfo.setType(1);
			ManorTransactionRecore recoresum= manorService.selectManorTransactionRecorePageSum(baseInfo);

			if (recoresum == null) {
				recoresum = new ManorTransactionRecore();
				recoresum.setSumMoney(new BigDecimal(0));
				recoresum.setSumPlatMoney(new BigDecimal(0));
			}

			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("recoresum", recoresum);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("领地买卖记录异常", e);
		}
		return result;
	}

	@RequestMapping("/transactionRecoreOne")
	@ResponseBody
	public Result transactionRecoreById(@RequestParam("orderId") String orderId){
		Result result = new Result();
		try{
			ManorTransactionRecore infoDetail= manorService.selectManorTransactionRecorePageById(orderId);
			Map<String, Object> map = new HashMap<>();
			map.put("infoDetail", infoDetail);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("领地买卖记录详情异常", e);
		}
		return result;
	}


	@RequestMapping("/manorAdjustRecore")
	@ResponseBody
	public Result manorAdjustRecore(@RequestParam(defaultValue = "1") int pageNo,
									@RequestParam(defaultValue = "10") int pageSize,
									@RequestParam("baseInfo") String param){
		Result result = new Result();
		try{
			ManorAdjustRecore baseInfo = JSONObject.parseObject(param, ManorAdjustRecore.class);

			Page<ManorAdjustRecore> page = new Page<>(pageNo, pageSize);
			manorService.selectManorAdjustRecorePage(baseInfo, page);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("领地买卖记录异常", e);
		}
		return result;
	}

	@RequestMapping("/manorquery")
	@ResponseBody
	public Result manorquery(@RequestParam(defaultValue = "1") int pageNo,
							 @RequestParam(defaultValue = "10") int pageSize,
							 @RequestParam("baseInfo")String param ){
		Result result = new Result();
		try{
			ManorQuery baseInfo = JSONObject.parseObject(param, ManorQuery.class);
			Page<ManorQuery> page = new Page<>(pageNo, pageSize);
			manorService.selectManorQueryPage(baseInfo, page);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("领地查询异常", e);
		}
		return result;
	}

	@RequestMapping("/manormanager")
	@ResponseBody
	public Result manormanager(){
		Result result = new Result();
		try{
			ManorManager manager = manorService.selectManorManager();
			Map<String, Object> map = new HashMap<>();
			map.put("info", manager);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("领地业务管理查询", e);
		}
		return result;
	}

	@RequestMapping("/savemanormanager")
	@ResponseBody
	@SystemLog(description="保存或更新红包领地业务管理", operCode="manor.savemanormanager")
	public Result savemanormanager(@RequestParam("info") String param){
		Result result = new Result();
		try{
			ManorManager parseObject = JSON.parseObject(param, ManorManager.class);
			ManorManager manager = manorService.selectManorManager();
			if (manager==null) {
				manorService.insertManorManager(parseObject);
			}else{
				manorService.updateManorManager(parseObject);
			}

			Map<String, Object> map = new HashMap<>();
			map.put("info", manager);
			result.setStatus(true);
			result.setMsg("保存成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("领地业务管理查询", e);
		}
		return result;
	}

	/**
	 * 查询红包账户余额明细
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/redAccountDetail")
	@ResponseBody
	public Result redAccountDetail(@RequestParam(defaultValue = "1") int pageNo,
								   @RequestParam(defaultValue = "10") int pageSize,
								   @RequestParam("baseInfo") String param){
		Result result = new Result();
		try{
			ManorAccountDetail baseInfo = JSONObject.parseObject(param, ManorAccountDetail.class);
			Page<ManorAccountDetail> page = new Page<>(pageNo, pageSize);
			redService.selectManorAccountDetailPage(baseInfo, page);
			baseInfo.setSelectType(1);
			ManorAccountDetail totalInfo = redService.selectManorAccountDetailSum(baseInfo);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("totalInfo", totalInfo);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询红包账户余额异常", e);
		}
		return result;


	}


	/**
	 * 查询平台红包账户余额
	 * @return
	 */
	@RequestMapping("/plateAccountInfo")
	@ResponseBody
	public Result plateAccountInfo(@RequestParam("id") Long id){
		Result result = new Result();
		try{
			RedAccountInfo accountInfo = redService.plateAccountInfoReload(id);
			if(accountInfo != null){
				if (accountInfo.getType()!=null && accountInfo.getType().equals("2") && !StringUtil.isBlank(accountInfo.getUserCode())) {
					accountInfo.setRelationId(Long.valueOf(accountInfo.getUserCode()));
				}
				result.setStatus(true);
				result.setMsg("查询成功");
				result.setData(accountInfo);
			}
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询平台红包账户余额异常", e);
		}
		return result;
	}

	/**
	 * 查询平台红包账户余额明细
	 * @param pageNo
	 * @param pageSize
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping("/plateAccountDetail")
	@ResponseBody
	public Result plateAccountDetail(@RequestParam(defaultValue = "1") int pageNo,
									 @RequestParam(defaultValue = "10") int pageSize,
									 @RequestBody RedAccountDetail baseInfo){
		Result result = new Result();
		try{

			Page<RedAccountDetail> page = new Page<>(pageNo, pageSize);

			RedAccountInfo accountInfo = redService.plateAccountInfoReload(baseInfo.getId());

			baseInfo.setRedAccountId(accountInfo.getId());
			baseInfo.setUserType(accountInfo.getType());
			redService.selectAccountDetailPageReload(baseInfo, page);
			baseInfo.setSelectType(1);
			RedAccountDetail totalInfo = redService.selectAccountDetailSum(baseInfo);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("totalInfo", totalInfo);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询平台红包账户余额明细异常", e);
		}
		return result;
	}

	/**
	 * 查询红包账户明细
	 * @param pageNo
	 * @anthor ouyuhui
	 * @param pageSize
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping("/redAccountDetailList")
	@ResponseBody
	public Result redAccountDetail(@RequestParam(defaultValue = "1") int pageNo,
									 @RequestParam(defaultValue = "10") int pageSize,
									 @RequestBody RedAccountDetail baseInfo){
		Result result = new Result();
		try{

			Page<RedAccountDetail> page = new Page<>(pageNo, pageSize);
			baseInfo.setMethodType(1);
			redService.selectAccountDetailPageReload(baseInfo, page);
			baseInfo.setSelectType(1);
			RedAccountDetail totalInfo = redService.findAccountDetailSum(baseInfo);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("totalInfo", totalInfo);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		}catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询红包账户明细异常", e);
		}
		return result;
	}

	/**
	 * 导出平台红包账户明细
	 * @param baseInfo
	 * @param response
	 */
	@RequestMapping("/exportPlateAccountDetail")
	public void exportPlateAccountDetail(@Param("baseInfo")String baseInfo, HttpServletResponse response){
		try {
			RedAccountDetail detail = JSONObject.parseObject(baseInfo, RedAccountDetail.class);

			RedAccountInfo red = redService.plateAccountInfoReload(detail.getId());

			detail.setRedAccountId(red.getId());
			detail.setUserType(red.getType());

			redService.exportPlateAccountDetail(response, detail);
		} catch (Exception e){
			log.error("导出平台红包账户明细异常", e);
		}
	}
	/**
	 * 导出红包账户明细
	 * @param baseInfo
	 * @param response
	 */
	@RequestMapping("/exportRedAccountDetail")
	public void exportRedAccountDetail(@Param("baseInfo")String baseInfo, HttpServletResponse response){
		try {
			RedAccountDetail detail = JSONObject.parseObject(baseInfo, RedAccountDetail.class);
			detail.setMethodType(1);
			redService.exportRedAccountDetail(response, detail);
		} catch (Exception e){
			log.error("导出平台红包账户明细异常", e);
		}
	}
	/**
	 * 导出平台红包账户
	 * @param baseInfo
	 * @param response
	 */
	@RequestMapping("/exportAccountQuery")
	public void exportAccountQuery(@Param("baseInfo")String baseInfo, HttpServletResponse response){
		try {

			ManorAccountDetail detail = JSONObject.parseObject(baseInfo, ManorAccountDetail.class);


			redService.exportPlateAccountDetail(response, detail);
		} catch (Exception e){
			log.error("导出红包账户异常", e);
		}
	}

	/**
	 * 领地买卖记录异常
	 * @param baseInfo
	 * @param response
	 */
	@RequestMapping("/exportManorTransactionRecore")
	public void exportManorTransactionRecore(@Param("baseInfo")String baseInfo, HttpServletResponse response){
		try {

			baseInfo = URLDecoder.decode(baseInfo,"UTF-8");

			ManorTransactionRecore detail = JSONObject.parseObject(baseInfo, ManorTransactionRecore.class);


			redService.exportManorTransactionRecore(response, detail);
		} catch (Exception e){
			log.error("导出领地买卖记录异常", e);
		}
	}
}
