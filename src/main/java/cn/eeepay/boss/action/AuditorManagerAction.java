package cn.eeepay.boss.action;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.AuditorCountInfo;
import cn.eeepay.framework.util.Constants;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AuditorManagerInfo;
import cn.eeepay.framework.service.AuditorManagerService;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * 审核人员管理
 * @author tans
 */
@Controller
@RequestMapping(value="/auditorManager")
public class AuditorManagerAction {

	private static final Logger log = LoggerFactory.getLogger(AuditorManagerAction.class);  

	@Resource
	private AuditorManagerService auditorManagerService;
	/**
	 * 条件查询
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByCondition.do")
	public @ResponseBody Object selectByCondition(@Param("page")Page<AuditorManagerInfo> page,@RequestParam("baseInfo") String param) {
		try {
			AuditorManagerInfo baseInfo = JSON.parseObject(param, AuditorManagerInfo.class);
			auditorManagerService.selectByCondition(page,baseInfo);
		} catch (Exception e) {
			log.error("条件查询审核人员信息失败", e);
		}
		return page;
	}

	/**
	 * 根据审核人，查询他可以审核的业务产品
	 * @param auditorId
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getBpByAuditor.do")
	public @ResponseBody Object getBpByAuditor(String auditorId) {
		List<AuditorManagerInfo> list = null;
		try {
			list = auditorManagerService.getBpByAuditor(auditorId);
		} catch (Exception e) {
			log.error("根据审核人，查询他可以审核的业务产品，失败", e);
		}
		return list;
	}

	@RequestMapping(value="/saveBatch")
	@ResponseBody
	@SystemLog(description = "审核人管理设置规则",operCode="auditorManager.insert")
	public Object saveBatch(@RequestBody String param ) {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSON.parseObject(param);
			String auditorId = json.getString("auditorId");
			List<AuditorManagerInfo> list = JSON.parseArray(json.getJSONArray("auditorList").toJSONString(),
					AuditorManagerInfo.class);
			int i = auditorManagerService.insertBatch(auditorId,list);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "设置成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "设置失败");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "服务异常");
			log.error("设置规则失败", e);
		}
		return msg;
	}

	/**
	 * 根据审核人，查询他可以审核的业务产品
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateStatus.do")
	@SystemLog(description = "审核人管理状态开关",operCode="auditorManager.switch")
	public @ResponseBody Object updateStatus(@RequestBody String param ) {
		Map<String, Object> msg = new HashMap<>();
		try {
			AuditorManagerInfo info = JSON.parseObject(param, AuditorManagerInfo.class);
			int i = auditorManagerService.updateStatus(info);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "设置成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "设置失败");
			}
		} catch (Exception e) {
			msg.put("msg", "服务异常");
			msg.put("status", false);
			log.error("根据审核人，查询他可以审核的业务产品，失败", e);
		}
		return msg;
	}

	/**
	 * 根据审核人，查询他可以审核的业务产品
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteData.do")
	@SystemLog(description = "审核人管理删除",operCode="auditorManager.delete")
	public @ResponseBody Object deleteData(Integer id)throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int i = auditorManagerService.deleteData(id);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "删除成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "删除失败");
			}
		} catch (Exception e) {
			msg.put("msg", "服务异常");
			msg.put("status", false);
			log.error("删除审核人信息失败", e);
		}
		return msg;
	}

	/**
	 * 条件查询
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllRecord.do")
	@ResponseBody 
	public Object selectAllRecord(@Param("page")Page<AuditorCountInfo> page, @RequestParam("info") String param) {
		Map<String, Object> msg=new HashMap<String, Object>();
		try {
			AuditorCountInfo info = JSON.parseObject(param, AuditorCountInfo.class);
			auditorManagerService.selecrAllInfoRecord(page, info,msg);
		} catch (Exception e) {
			log.error("条件查询审核人员信息失败", e);
			msg.put("status", false);
			msg.put("msg", "系统异常!");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllRecordDetail.do")
	@ResponseBody 
	public Object selectAllRecordDetail(@Param("page")Page<AuditorCountInfo> page,@RequestParam("info") String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			AuditorCountInfo info = JSON.parseObject(param, AuditorCountInfo.class);
			auditorManagerService.selecrAllInfoRecordDetail(page,info,msg);
		} catch (Exception e) {
			log.error("查询审核人员详情1失败", e);
			msg.put("status", false);
			msg.put("msg", "系统异常!");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllRecordDetail2.do")
	@ResponseBody 
	public Object selectAllRecordDetail2(@Param("page")Page<AuditorManagerInfo> page,@RequestParam("info") String param) {
		List<AuditorManagerInfo> list=null;
		Map<String, Object> msg=new HashMap<String, Object>();
		try {
			AuditorCountInfo info = JSON.parseObject(param, AuditorCountInfo.class);
			auditorManagerService.selecrAllInfoRecordDetail2(page,info, msg);
		} catch (Exception e) {
			log.error("查询审核人员详情2失败", e);
			msg.put("status", false);
			msg.put("msg", "系统异常!");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportInfo.do")
	@ResponseBody
	@SystemLog(description = "审核统计详情2导出",operCode="auditorRecord.export")
	public Object exportInfo(@RequestParam("info") String param,HttpServletResponse response) throws Exception{
		Map<String, Object> msg=new HashMap<String,Object>();
		try {
			AuditorCountInfo info = JSON.parseObject(param, AuditorCountInfo.class);
			if(info.getAuditorId()==null){
				msg.put("status", false);
				msg.put("msg", "审核人为空!");
				return msg;
			}
			if (info.getsTime() != null && info.geteTime() != null) {
				// 判断起始时间是否大于结束时间
				long stime = info.getsTime().getTime();
				long etime = info.geteTime().getTime();
				if (stime > etime) {
					msg.put("status", false);
					msg.put("msg", "起始时间不能大于结束时间");
					return msg;
				}
				// 判断时间是否超过了30天
				int sum = (int) ((etime - stime) / (24 * 60 * 60 * 1000));
				if (sum > 31) {
					msg.put("status", false);
					msg.put("msg", "审核时间最长不超过一个月");
					return msg;
				}
			} else {
				msg.put("status", false);
				msg.put("msg", "审核时间必须输入");
				return msg;
			}
			List<AuditorManagerInfo> list = auditorManagerService.exportInfoList(info);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
			String fileName = "审核人员审核记录"+sdf.format(new Date())+".xlsx" ;
			String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
			if(list.size()<1){
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("userName", null);
				maps.put("itemNo", null);
				maps.put("merchantNo", null);
				maps.put("merchantName", null);
				maps.put("agentName", null);
				maps.put("bpName", null);
				maps.put("saleName", null);
				maps.put("createTime", null);
				maps.put("auditorStatus", null);
				maps.put("auditorTime", null);
				maps.put("describes", null);
				data.add(maps);
			}else{
				Map<String, String> auditorStatusMap=new HashMap<String, String>();
				auditorStatusMap.put("1","审核成功");
				auditorStatusMap.put("2","审核失败");
				auditorStatusMap.put("3","复审退件");

				for (AuditorManagerInfo item:list) {
					Map<String, String> maps = new HashMap<String, String>();
					maps.put("userName", null == item.getUserName() ? "" : item.getUserName());
					maps.put("itemNo", null == item.getItemNo() ? "" : item.getItemNo());
					maps.put("merchantNo", null == item.getMerchantNo() ? "" : item.getMerchantNo());
					maps.put("merchantName", null == item.getMerchantName() ? "" : item.getMerchantName());
					maps.put("agentName", null == item.getAgentName() ? "" : item.getAgentName());
					maps.put("bpName", null == item.getBpName() ? "" : item.getBpName());
					maps.put("saleName", null == item.getSaleName() ? "" : item.getSaleName());
					maps.put("createTime", null==item.getCreateTime()?"":sdf1.format(item.getCreateTime()));
					maps.put("auditorStatus",auditorStatusMap.get(item.getAuditorStatus()));
					maps.put("auditorTime", null==item.getAuditorTime()?"":sdf1.format(item.getAuditorTime()));
					maps.put("describes", null == item.getDescribes() ? "" : item.getDescribes());
					data.add(maps);
				}
			}
			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[]{
					"userName","itemNo","merchantNo","merchantName","agentName","bpName","saleName",
					"createTime","auditorStatus","auditorTime","describes"};
			String[] colsName = new String[]{
					"审核人","商户进件编号","商户编号","商户名称","所属代理商","业务产品","销售名称",
					"录入时间","审核状态","审核时间","描述"};
			OutputStream outputStream = null;
			try {
				outputStream=response.getOutputStream();
				export.export(cols, colsName, data, outputStream);
			}catch (Exception e){
				log.error("审核统计详情2导出失败!",e);
			}finally {
				if(outputStream!=null){
					outputStream.close();
				}
			}
		}catch (Exception e){
			log.error("审核统计详情2导出异常!",e);
			msg.put("status", false);
			msg.put("msg", "审核统计详情2导出异常!");
		}
		return msg;



	}	
	
	public static class AuditorRecord implements Cloneable{
		private String num;
		private String auditorId;
		private Date sTime;
		private Date eTime;
		private String bpName;
		private String isNull;
		private String bpId;
		private String url;
		private String status;
		private Date createTime;
		private String userName;
		private String openStatus;
		private String id;
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getAuditorId() {
			return auditorId;
		}
		public void setAuditorId(String auditorId) {
			this.auditorId = auditorId;
		}
		public Date getsTime() {
			return sTime;
		}
		public void setsTime(Date sTime) {
			this.sTime = sTime;
		}
		public Date geteTime() {
			return eTime;
		}
		public void seteTime(Date eTime) {
			this.eTime = eTime;
		}
		public String getBpName() {
			return bpName;
		}
		public void setBpName(String bpName) {
			this.bpName = bpName;
		}
		public String getIsNull() {
			return isNull;
		}
		public void setIsNull(String isNull) {
			this.isNull = isNull;
		}
		public String getBpId() {
			return bpId;
		}
		public void setBpId(String bpId) {
			this.bpId = bpId;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getOpenStatus() {
			return openStatus;
		}
		public void setOpenStatus(String openStatus) {
			this.openStatus = openStatus;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		@Override
		public AuditorRecord clone(){
			AuditorRecord o = null;
		        try{
		            o = (AuditorRecord)super.clone();
		        }catch(CloneNotSupportedException e){
		            e.printStackTrace();
		        }
		        return o;
		}
	}
}
