package cn.eeepay.framework.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.dao.SuperPushDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AccountInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SuperPushShare;
import cn.eeepay.framework.model.SuperPushUser;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SuperPushService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;

/** 
 * @author tans 
 * @version ：2017年5月10日 上午11:29:21 
 * 
 */
@Service("superPushService")
@Transactional
public class SuperPushServiceImpl implements SuperPushService {
	
	private static final Logger log = LoggerFactory.getLogger(SuperPushServiceImpl.class);

	@Resource
	private SuperPushDao superPushDao;
	
	@Resource
	private SysDictDao sysDictDao;
	
	@Override
	public List<SuperPushUser> getByParam(SuperPushUser baseInfo, Page<SuperPushUser> page) {
		SysDict sysDict = sysDictDao.getByKey("SUPER_PUSH_BP_ID");
		if(sysDict!=null && StringUtils.isNotBlank(sysDict.getSysValue())){
			baseInfo.setBpId(sysDict.getSysValue());
		}
		List<SuperPushUser> list = superPushDao.getByParam(baseInfo,page);
//		List<SuperPushUser> list = page.getResult();
//		if(list!=null && list.size()>0){
//			for(SuperPushUser item: list){
//				item.setAvaliBalance(getSuperPushUserBalance(item.getMerchantNo()));
//			}
//		}
		return list;
	}

	@Override
	public MerchantInfo getSuperPushMerchantDetail(String merchantNo) {
		return superPushDao.getSuperPushMerchantDetail(merchantNo);
	}

	@Override
	public List<SuperPushShare> getSuperPushShareDetail(SuperPushShare baseInfo, Page<SuperPushShare> page) {
		return superPushDao.getSuperPushShareDetail(baseInfo,page);
	}

	@Override
	public void exportExcel(Page<SuperPushShare> page, SuperPushShare baseInfo, HttpServletResponse response) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;
        try {
        	List<SuperPushShare> list = superPushDao.getSuperPushShareDetail(baseInfo,page);
        	String fileName = "微创业商户分润详情"+sdf.format(new Date())+".xls" ;
     	    String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		    response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);    
		    List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		    Map<String,String> map = null;
		    for(SuperPushShare item: list){
		    	map = new HashMap<>();
		    	map.put("index", data.size()+1+"");
		    	map.put("transTime", item.getTransTime()==null?"":sdfTime.format(item.getTransTime()));
		    	map.put("merchantNo", item.getMerchantNo());
		    	map.put("merchantName", item.getMerchantName());
		    	map.put("level", item.getLevel());
		    	map.put("share", StringUtil.filterNull(item.getShare()));
		    	map.put("rule", item.getRule());
		    	map.put("transAmount", StringUtil.filterNull(item.getTransAmount()));
		    	data.add(map);
		    }
		    ListDataExcelExport export = new ListDataExcelExport();
		    String[] cols = new String[]{"index","transTime","merchantNo","merchantName","level"
		    		,"share","rule","transAmount"};
		    String[] colsName = new String[]{"序号","统计时间","下级商户号","下级商户名称","下级商户级别",
		    		"贡献分润金额","分润比例","交易金额"};
		    ouputStream = response.getOutputStream();
	        export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
        	log.error("导出业务活动记录失败");
        	e.printStackTrace();
        } finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public SuperPushUser getCashMerchantDetail(String merchantNo) {
		return superPushDao.getCashMerchantDetail(merchantNo);
	}

	@Override
	public BigDecimal getTotalAmount(String merchantNo) {
		return superPushDao.getTotalAmount(merchantNo);
	}

	
	/**
	 * 获取商户账号余额
	 * @param merNo
	 * @return
	 */
	public BigDecimal getSuperPushUserBalance(String merNo) {
		BigDecimal avaliBalance = null;
		String str = ClientInterface.getSuperPushUserBalance(merNo);
		log.info("查询微创业商户余额，商户号：{}，返回结果[{}]",merNo,str);
		JSONObject json = JSON.parseObject(str);
		if ((boolean) json.get("status")) {// 返回成功
			AccountInfo ainfo = JSON.parseObject(str, AccountInfo.class);
			avaliBalance = ainfo.getAvaliBalance();
		}
		return avaliBalance;
	}

	@Override
	public List<SettleOrderInfo> getCashPage(SettleOrderInfo settleOrderInfo, Page<SuperPushUser> page) {
		return superPushDao.getCashPage(settleOrderInfo, page);
	}

	@Override
	public List<SuperPushShare> getShareByParam(SuperPushShare baseInfo, Page<SuperPushShare> page) {
		List<SuperPushShare> list = superPushDao.getShareByParam(baseInfo,page);
		return list;
	}

	@Override
	public Map<String, Object> getTotalShareByParam(SuperPushShare baseInfo) {
		return superPushDao.getTotalShareByParam(baseInfo);
	}

	@Override
	public Map<String, Object> getTotalTransAmountByParam(SuperPushShare baseInfo) {
		return superPushDao.getTotalTransAmountByParam(baseInfo);
	}

	/**
	 * 导出微创业分润明细
	 * @throws IOException 
	 */
	@Override
	public void exportShare(SuperPushShare baseInfo, HttpServletResponse response) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;
        try {
        	Page<SuperPushShare> page = new Page<>();
        	page.setPageSize(Integer.MAX_VALUE);
        	List<SuperPushShare> list = superPushDao.getShareByParam(baseInfo,page);
        	String fileName = "微创业收益明细"+sdf.format(new Date())+".xls" ;
     	    String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		    response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);    
		    List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		    Map<String,String> map = null;
		    Map<String, String> shareTypeMap = new HashMap<>();
		    shareTypeMap.put("0", "一级代理商");
		    shareTypeMap.put("1", "直属代理商");
		    shareTypeMap.put("2", "一级商户");
		    shareTypeMap.put("3", "二级商户");
		    shareTypeMap.put("4", "三级商户");
		    Map<String, String> shareStatusMap = new HashMap<>();
		    shareStatusMap.put("0", "未入账");
		    shareStatusMap.put("1", "已入账");
		    shareStatusMap.put("2", "入账失败");
		    for(SuperPushShare item: list){
		    	map = new HashMap<>();
		    	map.put("index", data.size()+1+"");
		    	map.put("createTime", item.getTransTime()==null?"":sdfTime.format(item.getCreateTime()));
		    	map.put("shareAmount", StringUtil.filterNull(item.getShareAmount()));
		    	map.put("shareType", StringUtils.trimToEmpty(shareTypeMap.get(item.getShareType())));
		    	map.put("shareRate", "".equals(StringUtil.filterNull(item.getShareRate()))?"":StringUtil.filterNull(item.getShareRate())+"%");
		    	map.put("shareName", item.getShareName());
		    	map.put("shareNo", item.getShareNo());
		    	map.put("transAmount", StringUtil.filterNull(item.getTransAmount()));
		    	map.put("orderNo", item.getOrderNo());
		    	map.put("merchantNo", item.getMerchantNo());
		    	map.put("shareStatus", StringUtils.trimToEmpty(shareStatusMap.get(item.getShareStatus())));
		    	map.put("shareTime", item.getShareTime()==null?"":sdfTime.format(item.getShareTime()));
		    	data.add(map);
		    }
		    ListDataExcelExport export = new ListDataExcelExport();
		    String[] cols = new String[]{"index","createTime","shareAmount","shareType","shareRate"
		    		,"shareName","shareNo","transAmount","orderNo","merchantNo","shareStatus","shareTime"};
		    String[] colsName = new String[]{"序号","分润创建时间","分润金额","分润级别","分润百分比",
		    		"商户/代理商名称","商户/代理商编号","交易金额","交易订单号","交易商户编号","入账状态","入账时间"};
		    ouputStream = response.getOutputStream();
	        export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
        	log.error("导出微创业收益明细失败,param:{}",JSONObject.toJSONString(baseInfo));
			e.printStackTrace();
        } finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
	}
	

}
