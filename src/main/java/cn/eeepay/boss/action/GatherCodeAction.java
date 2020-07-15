package cn.eeepay.boss.action;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.GatherCode;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.service.GatherCodeService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * 收款码管理
 * @author tans
 */
@Controller
@RequestMapping(value="/gatherCode")
public class GatherCodeAction {
	
	private static final Logger log = LoggerFactory.getLogger(GatherCodeAction.class);
	
	@Resource
	private GatherCodeService gatherCodeService;
	
	@Resource
	private SysDictService sysDictService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByParams")
	@ResponseBody
	public Page<GatherCode> selectByParams(@RequestParam("baseInfo") String param, @Param("page") Page<GatherCode> page)throws Exception{
		try {
			GatherCode gatherCode = JSONObject.parseObject(param, GatherCode.class);
			gatherCodeService.selectByParams(page,gatherCode);
		} catch (Exception e) {
			log.error("收款码管理：条件查询异常");
			e.printStackTrace();
		}
		return page;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/gatherCodeDetail/{id}")
	@ResponseBody
	public GatherCode gatherCodeDetail(@PathVariable("id") String id)throws Exception{
		GatherCode info = null;
		try {
			info = gatherCodeService.gatherCodeDetail(id);
		} catch (Exception e) {
			log.error("收款码管理：条件查询异常");
			e.printStackTrace();
		}
		return info;
	}
	
	/**
	 * 生成收款码
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/createGatherCode")
	@ResponseBody
	@SystemLog(description = "生成收款码",operCode="gatherCode.insert")
	public Map<String, Object> createGatherCode(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			GatherCode info = JSONObject.parseObject(param, GatherCode.class);
			if(info!=null && info.getNumber()>100000){
				msg.put("status", false);
				msg.put("msg", "每次最多能生成10万个");
				return msg;
			}
			int num = gatherCodeService.insertBatch(info); 
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}else {
				msg.put("status", false);
				msg.put("msg", "收款码导出失败");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "服务异常");
			log.error("生成收款码失败");
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 检验收款码剩余数量
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkGatherNumber")
	@ResponseBody
	public Map<String, Object> checkGatherNumber(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			GatherCode info = JSONObject.parseObject(param, GatherCode.class);
			List<GatherCode> list = gatherCodeService.selectByParams(new Page<GatherCode>(0,info.getNumber()), info); 
			if(list==null || list.size()<info.getNumber()){
				msg.put("status", false);
				msg.put("msg", list.size());
			}else{
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "服务异常");
			log.error("检验收款码剩余数量失败");
			e.printStackTrace();
		}
		return msg;
	}
	
	
	/**
	 * 导出收款码和SN，并添加批次号
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/exportGatherCode")
	@SystemLog(description = "导出收款码",operCode="garherCode.export")
	public void exportGatherCode(@RequestParam Map<String, Object> param,HttpServletResponse response) throws IOException {
		GatherCode info = new GatherCode();
		String materialType = (String) param.get("materialType");
		String number = (String) param.get("number");
		info.setMaterialType(Integer.parseInt(materialType));
		info.setNumber(Integer.parseInt(number));
		info.setStatus(0);	//状态为：未导出
		//找出需要导出的数据
		List<GatherCode> list = gatherCodeService.exportGatherCode(info);
		
		SysDict sysDict = sysDictService.getByKey("GATHER_CODE_IP");
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
	    String fileName = "SK"+sdf.format(new Date())+".csv" ;
	    String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
	    response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
	    BufferedWriter out = null;
	    out = new BufferedWriter(response.getWriter());
	    out.append("SN,PASM\r");
	    for(GatherCode item:list){
		    if(sysDict!=null){
		    	out.append("NO.")
		    	.append(item.getSn()).append(",")
		    	.append("http://")
		    	.append(sysDict.getSysValue())
    			.append("/gather/gatherProcess?source=3&settleMent=0&gatherCode=")
    			.append(item.getGatherCode())
		    	.append("\r");
		    }
	    }
	    out.close();
        out=null;
	}
	
	/**
	 * 弃用收款码
	 */
	@RequestMapping(value="/gatherCodeAbandon/{id}")
	@ResponseBody
	@SystemLog(description = "弃用收款码",operCode="gatherCode.abandon")
	public Map<String, Object> gatherCodeAbandon(@PathVariable("id") String id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = gatherCodeService.updateGatherStatus(id,3);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "操作失败");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "服务异常");
			log.error("弃用收款码失败");
		}
		return msg;
	}
	
	/**
	 * 查看收款码url
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/gatherCodeUrl/{id}")
	@ResponseBody
	public Map<String, Object> gatherCodeUrl(@PathVariable("id") String id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			String url = gatherCodeService.gatherCodeUrl(id);
			msg.put("msg", url);
		} catch (Exception e) {
			log.error("查看收款码url失败");
		}
		return msg;
	}
}
