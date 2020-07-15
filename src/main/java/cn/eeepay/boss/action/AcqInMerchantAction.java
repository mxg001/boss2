package cn.eeepay.boss.action;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqInMerchant;
import cn.eeepay.framework.model.AcqInMerchantFileInfo;
import cn.eeepay.framework.model.AcqInMerchantRecord;
import cn.eeepay.framework.service.AcqInMerchantService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;

@Controller
@RequestMapping(value = "/acqInMerchantAction")
public class AcqInMerchantAction {
	private static final Logger log = LoggerFactory.getLogger(AcqInMerchantAction.class);

	// 收单机构商户
	@Resource
	private AcqInMerchantService acqInMerchantService;

	/**
	 * 初始化和模糊查询分页
	 *
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page") Page<AcqInMerchant> page, @RequestParam("info") String param)
			throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			AcqInMerchant amc = JSON.parseObject(param, AcqInMerchant.class);
			acqInMerchantService.selectAllInfo(page, amc);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	@RequestMapping(value = "/updateMcc")
	@ResponseBody
	@SystemLog(description = "修改MCC码", operCode = "acqIn.mcc")
	public Object updateMcc(@RequestParam("id") String id, @ModelAttribute("mcc") String mcc) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			
			if(StringUtils.isNotBlank(acqInMerchantService.queryStatusByAcqId(Long.valueOf(id)).getMcc())){
				jsonMap.put("msg", "已存在MCC");
				jsonMap.put("status", false);
			}else{
				int num = acqInMerchantService.updateMcc(id, mcc);
				if (num == 1) {
					jsonMap.put("msg", "添加成功");
					jsonMap.put("status", true);
				}
			};
		
		} catch (Exception e) {
			log.error("报错!!!", e);
			jsonMap.put("msg", false);
			jsonMap.put("status", false);
		}
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)

	@RequestMapping(value = "/selectDetailByParam")
	@ResponseBody
	public Object selectDetailByParam(@RequestParam("ids") String ids) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			long id = JSON.parseObject(ids, Long.class);
			AcqInMerchant acm = acqInMerchantService.selectByPrimaryKey(id);
			List<AcqInMerchantRecord> rd = acqInMerchantService.selectRecordByPrimaryKey(id);

            List<AcqInMerchantFileInfo> listStrs = acqInMerchantService.findByAcqIntoNo(acm.getAcqIntoNo());

            //图片地址
			for (AcqInMerchantFileInfo af: listStrs) {
				changeItemName(acm,af);
				if (af.getFileUrl()!=null) {
                    Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                    af.setContent(ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, af.getFileUrl(), expiresDate));
                    continue;
				}

			}
			
			jsonMap.put("result", acm);
			jsonMap.put("listStrs", listStrs);
			jsonMap.put("record", rd);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	public void changeItemName(AcqInMerchant acm,AcqInMerchantFileInfo af){
		if("特定行业许可证书".equals(af.getItemName())){
			if("5812".equals(acm.getTwoScopeX())) {
				af.setItemName("食品卫生许可证");
			}else if("7911".equals(acm.getTwoScopeX())) {
				af.setItemName("文化经营许可证");
			}else if("5813".equals(acm.getTwoScopeX())) {
				af.setItemName("文化经营许可证");
			}else if("7011".equals(acm.getTwoScopeX())) {
				af.setItemName("特种行业许可证");
			}else if("5072".equals(acm.getTwoScopeX())) {
				af.setItemName("加工或生产合同");
			}else if("5111".equals(acm.getTwoScopeX())) {
				af.setItemName("加工或生产合同");
			}else if("5137".equals(acm.getTwoScopeX())) {
				af.setItemName("加工或生产合同");
			}else if("5998".equals(acm.getTwoScopeX())) {
				af.setItemName("加工或生产合同");
			}else if("5541".equals(acm.getTwoScopeX())) {
				af.setItemName("成品油零售经营许可证");
			}
		}
	}

	@RequestMapping(value = "/exportInfo")
	@ResponseBody
	public void exportInfo(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		try {
			param = URLDecoder.decode(param, "UTF-8");

			AcqInMerchant am = JSONObject.parseObject(param, AcqInMerchant.class);
			List<AcqInMerchant> list = acqInMerchantService.exportInfo(am);
			acqInMerchantService.exportAcqInMerchant(list, response);
		} catch (Exception e) {
			log.error("收单商户进件导出异常", e);
		}
	}

	// 审核
	@RequestMapping(value = "/audit.do")
	@ResponseBody
	@SystemLog(description = "收单机构审核", operCode = "acqIn.audit")
	public Object audit(@RequestBody String param) {
		
		JSONObject json = JSON.parseObject(param);
		// 参数
		long id = json.getLongValue("id");
		int val = json.getIntValue("val");
		String notAllowIds=json.getString("notAllowIds");
		List<String> ids = StringUtil.strToList(notAllowIds,",");
		String examinationOpinions = json.getString("examinationOpinions");
	
		JSONObject jsonMap =acqInMerchantService.audit(id,val,ids,examinationOpinions);
		return jsonMap;
	}
}
