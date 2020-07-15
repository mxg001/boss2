package cn.eeepay.boss.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.model.MerchantCardInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SuperPushShare;
import cn.eeepay.framework.model.SuperPushUser;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.service.MerchantBusinessProductService;
import cn.eeepay.framework.service.MerchantCardInfoService;
import cn.eeepay.framework.service.MerchantRequireItemService;
import cn.eeepay.framework.service.SuperPushService;
import cn.eeepay.framework.service.TerminalInfoService;

/** 
 * 微创业商户查询
 * @author tans 
 * @version ：2017年5月10日 上午11:13:29 
 * 
 */
@Controller
@RequestMapping(value="/superPush")
public class SuperPushAction {
	
	private static final Logger log = LoggerFactory.getLogger(SuperPushAction.class);
	
	@Resource
	private SuperPushService superPushService;
	
	@Resource
	private MerchantBusinessProductService merchantBusinessProductService;
	
	@Resource
	private TerminalInfoService terminalInfoService;
	
	@Resource
	private MerchantRequireItemService merchantRequireItemService;
	
	@Resource
	private MerchantCardInfoService merchantCardInfoService;

	/**
	 * 条件查询微创业商户表
	 * @author tans
	 * @version 创建时间：2017年5月12日 上午11:20:33
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getByParam")
	@ResponseBody
	public Map<String,Object> getByParam(@RequestParam("baseInfo") String param,
			@Param("page") Page<SuperPushUser> page){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			SuperPushUser baseInfo = JSON.parseObject(param,SuperPushUser.class);
			superPushService.getByParam(baseInfo,page);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			log.error("条件查询微创业商户失败,参数：[{}]",param);
			log.error("条件查询微创业商户失败,",e);
		}
		return msg;
	}
	
	/**
	 * 微创业商户详情
	 * @author tans
	 * @version 创建时间：2017年5月12日 上午11:21:29
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/superPushMerchantDetail")
	@ResponseBody
	public Map<String,Object> superPushMerchantDetail(@RequestParam("merchantNo") String merchantNo)
			{
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			//基本信息
			MerchantInfo baseInfo = superPushService.getSuperPushMerchantDetail(merchantNo);
			//业务产品
			List<MerchantBusinessProduct> bpList = merchantBusinessProductService.getByMer(merchantNo);
			//机具
			Page<TerminalInfo> tiPage = new Page<>();
			terminalInfoService.getPageByMerchant(merchantNo,tiPage);
			//结算信息
			MerchantCardInfo merchantCardInfo = merchantCardInfoService.selectByMertId(merchantNo);
			if(merchantCardInfo!=null){
				merchantCardInfo.setAccountArea(merchantCardInfo.getAccountProvince()
						+"-"+ merchantCardInfo.getAccountCity()
						+"-"+ merchantCardInfo.getAccountDistrict()
						);
			}
			msg.put("status", true);
			msg.put("baseInfo", baseInfo);
			msg.put("bpList", bpList);
			msg.put("tiPage", tiPage);
			msg.put("merchantCardInfo", merchantCardInfo);
		} catch (Exception e) {
			log.error("微创业商户详情失败,参数：[{}]",merchantNo);
			log.error("微创业商户详情失败,",e);
		}
		return msg;
	}
	
	/**
	 * 微创业商户分润详情
	 * @author tans
	 * @version 创建时间：2017年5月15日 下午4:01:25
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/superPushShareDetail")
	@ResponseBody
	public Map<String,Object> superPushShareDetail(@RequestParam("baseInfo") String param,
			@Param("page")Page<SuperPushShare> page)
			{
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			SuperPushShare baseInfo = JSONObject.parseObject(param, SuperPushShare.class);
			superPushService.getSuperPushShareDetail(baseInfo,page);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			log.error("微创业商户分润详情失败,参数：[{}]",param);
			log.error("微创业商户分润详情失败,",e);
		}
		return msg;
	}
	
	/**
	 * 微创业分润详情导出
	 * @author tans
	 * @version 创建时间：2017年5月15日 下午4:01:56
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportExcel.do")
	public void exportExcel(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<SuperPushShare> page,
			 HttpServletResponse response){
       try {
    	   SuperPushShare baseInfo = JSON.parseObject(param,SuperPushShare.class);
    	   superPushService.exportExcel(page,baseInfo,response);
       } catch (Exception e) {
    	   log.error("导出微创业分润失败");
    	   log.error("导出微创业分润失败",e);
       }
   }
	
	/**
	 * 微创业体现详情
	 * @author tans
	 * @version 创建时间：2017年5月16日 下午2:28:05
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/superPushCashDetail")
	@ResponseBody
	public Map<String,Object> superPushCashDetail(@RequestParam("merchantNo") String merchantNo)
			{
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			//基本信息
			SuperPushUser baseInfo = superPushService.getCashMerchantDetail(merchantNo);
			//收益总额
			BigDecimal totalAmount = superPushService.getTotalAmount(merchantNo);
			//可用余额
			BigDecimal avaliBalance = superPushService.getSuperPushUserBalance(merchantNo);
			baseInfo.setTotalAmount(totalAmount);
			baseInfo.setAvaliBalance(avaliBalance);
			//结算信息
			MerchantCardInfo merchantCardInfo = merchantCardInfoService.selectByMertId(merchantNo);
			if(merchantCardInfo!=null){
				merchantCardInfo.setAccountArea(merchantCardInfo.getAccountProvince()
						+"-"+ merchantCardInfo.getAccountCity()
						+"-"+ merchantCardInfo.getAccountDistrict()
						);
			}
			msg.put("status", true);
			msg.put("baseInfo", baseInfo);
			msg.put("merchantCardInfo", merchantCardInfo);
		} catch (Exception e) {
			log.error("微创业商户详情失败,参数：[{}]",merchantNo);
			log.error("微创业商户详情失败,",e);
		}
		return msg;
	}
	
	/**
	 * 微创业商户提现详情-提现流水
	 * @author tans
	 * @version 创建时间：2017年5月16日 下午2:28:48
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getCashPage")
	@ResponseBody
	public Map<String,Object> getCashPage(@RequestParam("merchantNo") String merchantNo,
			@Param("page") Page<SuperPushUser> page){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			SettleOrderInfo settleOrderInfo = new SettleOrderInfo();
			settleOrderInfo.setSubType(6);//微创业商户
			settleOrderInfo.setSettleUserNo(merchantNo);
			settleOrderInfo.setSettleUserType("M");
			settleOrderInfo.setSettleType("2");
			superPushService.getCashPage(settleOrderInfo,page);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			log.error("微创业商户提现详情-提现流水,商户号：[{}]",merchantNo);
			log.error("微创业商户提现详情-提现流水,",e);
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getShareByParam")
	@ResponseBody
	public Map<String,Object> getShareByParam(@RequestParam("baseInfo") String param,
			@Param("page") Page<SuperPushShare> page){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			SuperPushShare baseInfo = JSON.parseObject(param,SuperPushShare.class);
			superPushService.getShareByParam(baseInfo,page);
			Map<String, Object> totalShareMap = superPushService.getTotalShareByParam(baseInfo);
			Map<String, Object> totalAmountMap = superPushService.getTotalTransAmountByParam(baseInfo);
			msg.put("status", true);
			msg.put("page", page);
			msg.put("totalShareMap", totalShareMap);
			msg.put("totalAmountMap", totalAmountMap);
		} catch (Exception e) {
			log.error("条件查询微创业收益失败,参数：[{}]",param);
			log.error("条件查询微创业收益失败,",e);
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/exportShare")
	public void exportShare(@RequestParam String param, HttpServletResponse response){
		try {
			SuperPushShare baseInfo = JSON.parseObject(param,SuperPushShare.class);
			superPushService.exportShare(baseInfo, response);
		} catch (Exception e) {
			log.info("导出微创业收益明细失败,参数:{}");
			log.info(e.toString());
		}
	}
}
