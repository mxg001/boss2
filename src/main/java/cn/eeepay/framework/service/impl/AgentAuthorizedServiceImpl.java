package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentAuthorizedDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.agentAuth.AgentAuth;
import cn.eeepay.framework.service.AgentAuthorizedService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.util.CellUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("agentAuthorizedService")
@Transactional
public class AgentAuthorizedServiceImpl implements AgentAuthorizedService {
	
	private static final Logger log = LoggerFactory.getLogger(AgentAuthorizedServiceImpl.class);

	@Resource
	private AgentAuthorizedDao agentAuthorizedDao;
	@Resource
	private AgentInfoService agentInfoService;

	@Override
	public Map<String, Object> getData(String record_code) {
		return agentAuthorizedDao.getData(record_code);
	}

	@Override
	public int upRecord(Map params) {
		return agentAuthorizedDao.upRecord(params);
	}

	@Override
	public int upCloseStatus(Map params) {
		return agentAuthorizedDao.upCloseStatus(params);
	}

	@Override
	public int upIsLook(Map params) {
		return agentAuthorizedDao.upIsLook(params);
	}

	@Override
	public int delRecord(String record_code) {
		return agentAuthorizedDao.deRecord(record_code);
	}

	@Override
	public int addRecord(Map params) {
		return agentAuthorizedDao.addRecord(params);
	}

	@Override
	public List<Map> getDatas(Map params, Page<Map> page) {
		if(StringUtil.isNotBlank(params.get("agent_authorized"))){
			List<Map<String, Object>> list=agentAuthorizedDao.getAgentAuthorized(params.get("agent_authorized").toString());
			if(list.size()>0){
				params.put("top_agent",list.get(0).get("top_agent"));
			}
		}
		return agentAuthorizedDao.getDatas(params,page);
	}

	/**
	 *获取代理商下的管理数据
	 */
	private String getLowerLevel(String agentNo){
		if(StringUtils.isNotBlank(agentNo)){
			//初始化数据,封装代理商数据
			List<String> agentNoList=new ArrayList<String>();//所有下级关联代理商编号
			List<Map<String,String>> agentList=new ArrayList<Map<String,String>>();//编号名称
			agentNoList.add(agentNo);

			Map<String,String> map=new HashMap<String, String>();
			map.put("agentNo",agentNo);
			AgentInfo agentInfo = agentInfoService.getAgentByNo(agentNo);
			if(agentInfo!=null){
				map.put("agentName",agentInfo.getAgentName());
			}
			agentList.add(map);

			//调用获取有关联授权的
			getEachLevel(agentNo,agentNoList,agentList);

			if(agentNoList.size()>0){
				StringBuffer sb=new StringBuffer();
				for(String item:agentNoList){
					sb.append(item).append(",");
				}
				String str=sb.toString().substring(0,sb.toString().length()-1);
				return str;
			}
		}
		return null;
	}

	/**
	 * 每级代理商获取
	 */
	private void getEachLevel(String agentNoParent,List<String> agentNoList,List<Map<String,String>> agentList){
		if(StringUtils.isNotBlank(agentNoParent)){
			List<Map<String,String>> list=agentAuthorizedDao.getCurrentLevel(agentNoParent);
			if(list!=null&&list.size()>0){
				for(Map<String,String> item:list){
					String agentNo=item.get("agentNo");
					//String agentName=item.get("agentName");
					if(agentNoList.contains(agentNo)){
						continue;//重复了跳过
					}else{
						agentNoList.add(agentNo);
						agentList.add(item);
						//持续回调下一层
						getEachLevel(agentNo,agentNoList,agentList);
					}
				}
			}
		}
	}

	@Override
	public List<Map<String, Object>> importDetailSelect(Map<String, Object> info) {
		if(StringUtil.isNotBlank(info.get("agent_authorized"))){
			List<Map<String, Object>> list=agentAuthorizedDao.getAgentAuthorized(info.get("agent_authorized").toString());
			if(list.size()>0){
				info.put("top_agent",list.get(0).get("top_agent"));
			}
		}
		return agentAuthorizedDao.importDetailSelect(info);
	}

	@Override
	public void importDetail(List<Map<String, Object>> list, HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "三方授权查询列表"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agent_authorized",null);
			maps.put("agent_authorized_name",null);
			maps.put("agent_link",null);
			maps.put("agent_link_name",null);
			maps.put("record_status",null);
			maps.put("is_look",null);
			maps.put("record_check",null);
			maps.put("record_creator",null);
			maps.put("check_user",null);
			maps.put("createTime",null);
			data.add(maps);
		}else{
			Map<String, String> recordStatusMap=new HashMap<String, String>();
			recordStatusMap.put("0","关闭");
			recordStatusMap.put("1","开启");

			Map<String, String> recordCheckMap=new HashMap<String, String>();
			recordCheckMap.put("0","未通过");
			recordCheckMap.put("1","通过");

			Map<String, String> isTopMap=new HashMap<String, String>();
			isTopMap.put("1","是");
			isTopMap.put("2","否");

			for (Map<String, Object> or : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("agent_authorized",or.get("agent_authorized")==null?"":or.get("agent_authorized").toString());
				maps.put("agent_authorized_name",or.get("agent_authorized_name")==null?"":or.get("agent_authorized_name").toString());
				maps.put("agent_link",or.get("agent_link")==null?"":or.get("agent_link").toString());
				maps.put("agent_link_name",or.get("agent_link_name")==null?"":or.get("agent_link_name").toString());
				maps.put("record_status",recordStatusMap.get(or.get("record_status")==null?null:or.get("record_status").toString()));
				maps.put("is_look",recordStatusMap.get(or.get("is_look")==null?null:or.get("is_look").toString()));
				maps.put("record_check",recordCheckMap.get(or.get("record_check")==null?null:or.get("record_check").toString()));
				maps.put("is_top",isTopMap.get(or.get("is_top")==null?null:or.get("is_top").toString()));
				maps.put("record_creator",or.get("record_creator")==null?"":or.get("record_creator").toString());
				maps.put("check_user",or.get("check_user")==null?"":or.get("check_user").toString());
				maps.put("create_time", or.get("create_time")==null?"":or.get("create_time").toString().substring(0,or.get("create_time").toString().length()-2));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"agent_authorized","agent_authorized_name","agent_link","agent_link_name",
				"record_status","is_look","record_check","is_top","record_creator","check_user","create_time"
		};
		String[] colsName = new String[]{"代理商编号","代理商名称","授权查询代理商编号","下级代理商名称",
				"关联关系开关","数据查询开关","审核状态","是否为顶层代理商","创建人员","审核人员","创建时间"
		};
		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("导出三方授权查询列表失败!",e);
		}finally {
			if (ouputStream != null) {
				ouputStream.close();
			}
		}
	}

	@Override
	public Map<String, Object> importDiscount(MultipartFile file) throws Exception {
		UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Map<String, Object> msg = new HashMap<>();
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		//读取第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		// 遍历所有单元格，读取单元格
		int row_num = sheet.getLastRowNum();

		List<AgentAuth> checkList=new ArrayList<AgentAuth>();//校验
		int num=0;
		for (int i = 1; i <= row_num; i++) {
			Row row = sheet.getRow(i);
			String agentAuthorized = CellUtil.getCellValue(row.getCell(0));//代理商编号(agent_authorized可以访问agent_link)
			String agentAuthorizedName = CellUtil.getCellValue(row.getCell(1));
			String agentLink = CellUtil.getCellValue(row.getCell(2));//授权查询代理商编号
			String agentLinkName = CellUtil.getCellValue(row.getCell(3));

			//代理商编号
			if(agentAuthorized==null||"".equals(agentAuthorized)){
				msg.put("status", false);
				msg.put("msg","导入失败,第"+(i+1)+"行,代理商编号为空!");
				return msg;
			}
			AgentInfo agentInfo = agentInfoService.getAgentByNo(agentAuthorized);
			if(agentInfo==null){
				msg.put("status", false);
				msg.put("msg", "导入失败,第"+(i+1)+"行,代理商编号不存在!");
				return msg;
			}else{
				if(!"1".equals(agentInfo.getAgentLevel().toString())){
					msg.put("status", false);
					msg.put("msg", "导入失败,第"+(i+1)+"行,代理商编号不是一级代理商!");
					return msg;
				}
			}

			//授权查询代理商编号
			if(agentLink==null||"".equals(agentLink)){
				msg.put("status", false);
				msg.put("msg","导入失败,第"+(i+1)+"行,授权查询代理商编号为空!");
				return msg;
			}
			AgentInfo agentInfo1 = agentInfoService.getAgentByNo(agentLink);
			if(agentInfo1==null){
				msg.put("status", false);
				msg.put("msg", "导入失败,第"+(i+1)+"行,授权查询代理商编号不存在!");
				return msg;
			}else{
				if(!"1".equals(agentInfo1.getAgentLevel().toString())){
					msg.put("status", false);
					msg.put("msg", "导入失败,第"+(i+1)+"行,授权查询代理商编号不是一级代理商!");
					return msg;
				}
			}
			AgentAuth info=new AgentAuth();
			info.setAgentAuthorized(agentAuthorized);
			info.setAgentLink(agentLink);
			//校验是否存在
			List<AgentAuth> agentAuthList=agentAuthorizedDao.checkSelectAgentAuth(info);
			if(agentAuthList!=null&&agentAuthList.size()>0){
				msg.put("status", false);
				msg.put("msg", "导入失败,第"+(i+1)+"行,代理商编号:"+agentAuthorized+" 授权查询代理商编号:"+agentLink+"已经存在相同配置!");
				return msg;
			}

			//检验导入文件是否存在重复记录
			if(checkList.size()>0){
				int sta=0;
				for(AgentAuth item:checkList){
					if(item.getAgentAuthorized().equals(agentAuthorized)
							&&item.getAgentLink().equals(agentLink)){
						sta=1;
						break;
					}
				}
				if(1==sta){
					msg.put("status", false);
					msg.put("msg", "导入失败,第"+(i+1)+"行,代理商编号:"+agentAuthorized+" 授权查询代理商编号:"+agentLink+"在导入文件中重复!");
					return msg;
				}else{
					checkList.add(info);
				}
			}else{
				checkList.add(info);
			}
			Map<String,Object> mapInfo=new HashMap<String,Object>();
			mapInfo.put("agent_authorized",agentAuthorized);
			mapInfo.put("agent_link",agentLink);
			mapInfo.put("record_code",IdUtil.simpleUUID());
			mapInfo.put("record_creator",principal.getUsername());
			Map agentLinkMap=agentAuthorizedDao.selectAgentAuthorizedAgentLink(agentLink);
			if(agentLinkMap!=null){
				msg.put("status", false);
				msg.put("msg", "导入失败,第"+(i+1)+"行,代理商已经有直属上级，不可关联多个上级");
				return msg;
			}
			List<Map<String,Object>> agentLinkList=agentAuthorizedDao.getAgentAuthorized(agentLink);
			if(agentLinkList.size()>0&&"1".equals(agentLinkList.get(0).get("is_top").toString())){
				msg.put("status", false);
				msg.put("msg", "导入失败,第"+(i+1)+"行,授权代理商不能为顶级代理商");
				return msg;
			}
			List<Map<String,Object>> list=agentAuthorizedDao.getAgentAuthorized(agentAuthorized);
			if(list.size()>0&&"1".equals(list.get(0).get("is_top").toString())){
				mapInfo.put("agent_node", agentAuthorized+"-"+agentLink);
				if(StringUtil.isBlank(list.get(0).get("agent_link"))){
					list.get(0).put("agent_link", agentLink);
					list.get(0).put("agent_node", agentAuthorized+"-"+agentLink);
					num += agentAuthorizedDao.upRecord(list.get(0));
				}else{
					mapInfo.put("top_agent",list.get(0).get("top_agent"));
					mapInfo.put("link_level",list.get(0).get("link_level"));
					mapInfo.put("is_top",list.get(0).get("is_top"));
					num += agentAuthorizedDao.addRecord(mapInfo);
				}
			}else{
				Map map=agentAuthorizedDao.selectAgentAuthorizedAgentLink(agentAuthorized);
				if(map==null){
					msg.put("status", false);
					msg.put("msg", "导入失败,第"+(i+1)+"行,该代理商未与上级代理商关联，请先与上级关联");
					return msg;
				}
				if("0".equals(map.get("record_status").toString())&&"1".equals(map.get("record_check").toString())){
					msg.put("status", false);
					msg.put("msg", "导入失败,第"+(i+1)+"行,请打开该代理商上级关联开关");
					return msg;
				}
				if (Integer.parseInt(map.get("link_level").toString())+1>5){
					msg.put("status", false);
					msg.put("msg", "导入失败,第"+(i+1)+"行,三方关联层级不能大于5级");
					return msg;
				}
				mapInfo.put("agent_node", map.get("agent_node").toString()+"-"+agentLink);
				mapInfo.put("top_agent",map.get("top_agent"));
				mapInfo.put("link_level",Integer.parseInt(map.get("link_level").toString())+1);
				mapInfo.put("is_top",2);
				num += agentAuthorizedDao.addRecord(mapInfo);
			}
		}
		msg.put("status", true);
		msg.put("msg", "导入成功,总共"+row_num+"条数据,成功导入"+num+"条");
		return msg;
	}

	@Override
	public List<Map<String, Object>> selectTopAgentManagement(Map<String, Object> map, Page page) {
		return agentAuthorizedDao.selectTopAgentManagement(map,page);
	}

	@Override
	public Map<String, Object> selectAgentAuthorizedAgentLink(String agent_link) {
		return agentAuthorizedDao.selectAgentAuthorizedAgentLink(agent_link);
	}

	@Override
	public List<Map<String, Object>> getAgentAuthorized(String agent_authorized) {
		return agentAuthorizedDao.getAgentAuthorized(agent_authorized);
	}

	@Override
	public int updateTopAgentManagement(String agent_authorized) {
		return agentAuthorizedDao.updateTopAgentManagement(agent_authorized);
	}

	@Override
	public int addTopAgentManagement(String agent_authorized) {
		UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return agentAuthorizedDao.addTopAgentManagement(agent_authorized,IdUtil.simpleUUID(),principal.getUsername());
	}

	@Override
	public int deleteTopAgentManagement(String agent_authorized) {
		return agentAuthorizedDao.deleteTopAgentManagement(agent_authorized);
	}

	@Override
	public boolean isCorrectChain(String recordCode) {
		return agentAuthorizedDao.countCorrectChain(recordCode) > 0;
	}

	@Override
	public boolean isTop(String recordCode) {
		return agentAuthorizedDao.countTop(recordCode)>0;
	}
}
