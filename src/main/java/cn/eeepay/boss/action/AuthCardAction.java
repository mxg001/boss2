package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AuthCard;
import cn.eeepay.framework.service.AuthCardService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value="/authCardAction")
public class AuthCardAction {
	
	private static final Logger log = LoggerFactory.getLogger(AuthCardAction.class);

	
	@Resource
	private AuthCardService authCardService;
	/**
	 * 查询
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/queryAuthCardList")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<AuthCard> page,@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			AuthCard rr=JSON.parseObject(param,AuthCard.class);
			authCardService.selectAllInfo(page, rr);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	/**
	 * 实名认证查询导出
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/authCardExport")
	@ResponseBody
	public void exportInfo(HttpServletResponse response,HttpServletRequest request,@RequestParam("info") String param) throws Exception{
		AuthCard rr = JSON.parseObject(param,AuthCard.class);
		List<AuthCard> list = authCardService.authCardExport(rr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "实名认证查询导出"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);   
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list==null||list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
			maps.put("cardNo", null);
			maps.put("userName", null);
			maps.put("merchantNo", null);
			maps.put("merchantName", null);
			
			maps.put("idCard", null);
			maps.put("mobile", null);
			maps.put("status", null);
			
			maps.put("createTime", null);
			data.add(maps);
		}else{
			for (int i = 0; i < list.size(); i++) {
				AuthCard ami = list.get(i);
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", ami.getId().toString());
				maps.put("cardNo", ami.getCardNo());
				maps.put("userName", ami.getUserName());
				maps.put("merchantNo", ami.getMerchantNo());
				maps.put("merchantName", ami.getMerchantName());
				maps.put("idCard", ami.getIdCard());
				maps.put("mobile", ami.getMobile());
				String status = "";
				if(StringUtils.isNotBlank(ami.getStatus())){
					if("0".equals(ami.getStatus())){
						status = "失败";
					}else if("1".equals(ami.getStatus())){
						status = "成功";
					}else if("2".equals(ami.getStatus())){
						status = "失效";
					}
				}
				maps.put("status", status);
				maps.put("createTime", DateUtil.getLongFormatDate(ami.getCreateTime()));
				data.add(maps);
			}
		}
			  
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"id","cardNo","userName","merchantNo","merchantName","idCard","mobile","status","createTime"};
		  String[] colsName = new String[]{"ID","银行卡号","持卡人","商户编号","商户名称","身份证","手机号","认证状态","创建时间"};
		  double[] cellWidth = {3000,6000,6000,6000,6000,6000,6000,6000,10000};
		  OutputStream ouputStream = response.getOutputStream();    
		  export.export(cols, colsName,cellWidth, data, ouputStream);
		  ouputStream.close(); 
	}

	/**
	 * 查询
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/authCardTotal")
	@ResponseBody
	public Object authCardTotal(@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			AuthCard rr=JSON.parseObject(param,AuthCard.class);
			Map<String,Object> s = authCardService.authCardTotal(rr);
			jsonMap.put("bols", true);
			jsonMap.put("page", s);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
}
