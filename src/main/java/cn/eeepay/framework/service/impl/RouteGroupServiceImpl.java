package cn.eeepay.framework.service.impl;

import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.CellUtil;
import cn.eeepay.framework.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.eeepay.framework.dao.AcqMerchantDao;
import cn.eeepay.framework.dao.AcqOrgDao;
import cn.eeepay.framework.dao.AcqServiceDao;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.RouteGroupDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.TransRouteGroupDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.RouteGroupService;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class RouteGroupServiceImpl implements RouteGroupService {

	@Resource
	private RouteGroupDao routeGroupDao;
	@Resource
	private AgentInfoDao agentInfoDao;
	@Resource
	private AcqOrgDao acqOrgDao;
	@Resource
	private AcqServiceDao acqServiceDao;
	@Resource
	private MerchantInfoDao merchantInfoDao;
	@Resource
	private AcqMerchantDao acqMerchantDao;
	@Resource
	private TransRouteGroupDao transRouteGroupDao;
	

	@Override
	public List<AcqOrg> acqOrgSelectBox() {
		return routeGroupDao.acqOrgSelectBox();
	}

	@Override
	public List<AcqService> acqServiceSelectBox(int acqId) {
		if (acqId == -1) {
			return acqServiceDao.selectAllAcqService();
		}
		return routeGroupDao.acqServiceSelectBox(acqId);
	}

	@Resource
	private SysDictDao sysDictDao;
	
	@Override
	public int insertRouteGroup(TransRouteGroup routeGroup) {
		//新增路由集群,判断收单服务类型和商户服务类型是否一致
		Integer serviceType = routeGroup.getServiceType();
		String acqServiceType = routeGroup.getAcqServiceType();
		SysDict sysDict = sysDictDao.selectExistServiceLink(serviceType.toString(),acqServiceType);
		if(sysDict==null){
			throw new RuntimeException("收单服务类型与商户服务类型不一致");
		}
		String groupNameStr = this.generateGroupName(routeGroup);
		routeGroup.setGroupName(groupNameStr);
		routeGroup.setStatus(0);
		routeGroup.setCreateTime(new Date());
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		routeGroup.setCreatePerson(principal.getId().toString());
		return routeGroupDao.insertRouteGroup(routeGroup);
	}

	private String generateGroupName(TransRouteGroup routeGroup) {
		StringBuilder groupName = new StringBuilder();
		if (routeGroup.getAcqId() != null) {
			AcqOrg acqOrg = acqOrgDao.selectByPrimaryKey(routeGroup.getAcqId());
			if (acqOrg != null) {
				groupName.append(acqOrg.getAcqName() + "-");
			}
		}
		if (routeGroup.getServiceName() != null) {
			groupName.append(routeGroup.getServiceName() + "-");
		}
		if (!StringUtils.isEmpty(routeGroup.getGroupProvince()) || !StringUtils.isEmpty(routeGroup.getGroupCity())) {
			groupName.append(routeGroup.getGroupProvince() + routeGroup.getGroupCity());
		}
		if (!StringUtils.isEmpty(routeGroup.getRouteLast())) {
			groupName.append("(" + routeGroup.getRouteLast() + ")");
		}
		String groupNameStr = groupName.toString();
		if ((groupNameStr.substring(groupNameStr.length() - 1)).equals("-")) {
			groupNameStr = groupNameStr.substring(0, groupNameStr.length() - 1);
		}
		return groupNameStr;
	}

	@Override
	public List<AgentInfo> oneLevelAgentSelectBox() {
		return agentInfoDao.selectByLevelOne();
	}

	@Override
	public List<TransRouteGroup> queryRouteGroupByCon(Map<String, Object> param, Page<TransRouteGroup> page) {
		List<TransRouteGroup> list = routeGroupDao.queryRouteGroupByCon(param, page);
		TransRouteGroup t = new TransRouteGroup();
		for(TransRouteGroup group: list){
			group.setStatus(group.getStatus()==0?1:0);
		}
		return list;
	}

	@Override
	public TransRouteGroup queryRouteGroupById(Map<String, Object> param) {
		TransRouteGroup info = routeGroupDao.queryRouteGroupById(param);
		if (info!=null) {
			if (info.getAcqId() != null) {
				AcqOrg acqOrg = acqOrgDao.selectByPrimaryKey(info.getAcqId());
				if (acqOrg != null) {
					info.setAcqName(acqOrg.getAcqName());
				}
			}
			if (info.getAcqServiceId() != null) {
				AcqService service = routeGroupDao.queryAcqServiceById(info.getAcqServiceId());
				if (service != null) {
					info.setServiceName(service.getServiceName());
				}
			}
			if (info.getAgentNo() != null) {
				AgentInfo agentInfo = agentInfoDao.selectByAgentNo(info.getAgentNo() + "");
				if (agentInfo != null) {
					info.setAgentName(agentInfo.getAgentName());
				}
			}
		}
		return info;
	}

	@Override
	public int updateRouteGroup(TransRouteGroup routeGroup) {
		//新增路由集群,判断收单服务类型和商户服务类型是否一致
		Integer serviceType = routeGroup.getServiceType();
		String acqServiceType = routeGroup.getAcqServiceType();
		SysDict sysDict = sysDictDao.selectExistServiceLink(serviceType.toString(),acqServiceType);
		if(sysDict==null){
			throw new RuntimeException("收单服务类型与商户服务类型不一致");
		}
		routeGroup.setGroupName(this.generateGroupName(routeGroup));
		return routeGroupDao.updateRouteGroup(routeGroup);
	}

	@Override
	public Map<String, Object> insertRouteGroupMerchant(TransRouteGroupMerchant routeGroupMerchant) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			MerchantInfo merchantInfo = merchantInfoDao.selectMerExistByMerNo(routeGroupMerchant.getPosMerchantNo());
			if(merchantInfo==null){
				msg.put("status", false);
				msg.put("msg", "商户不存在");
				return msg;
			}
			TransRouteGroup trg = transRouteGroupDao.selectByPrimaryKey(routeGroupMerchant.getGroupCode());
			if(!trg.getServiceType().toString().equals(routeGroupMerchant.getServiceType())){
				msg.put("msg", "该商户的服务类型与集群的服务类型不一致");
				msg.put("status", false);
				return msg;
			}
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			routeGroupMerchant.setCreateTime(new Date());
			routeGroupMerchant.setCreatePerson(principal.getId().toString());
			if(routeGroupDao.selectInfo(routeGroupMerchant)==null){
				int num = routeGroupDao.insertRouteGroupMerchant(routeGroupMerchant);
				if(num==1){
					msg.put("status", true);
					msg.put("msg", "添加成功");
				}else{
					msg.put("status", false);
					msg.put("msg", "添加失败");
				}
			}else{
				msg.put("status", false);
				msg.put("msg", "商户已存在不能添加");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public Map<String, Object> insertRouteGroupAcqMerchant(TransRouteGroupAcqMerchant routeGroupAcqMerchant) {
		Map<String, Object> msg = new HashMap<String, Object>();
		int count = acqMerchantDao.selectOrgMerExistByMerNo(routeGroupAcqMerchant.getAcqMerchantNo());
		if(count==0){
			msg.put("status", false);
			msg.put("msg", "收单商户不存在");
			return msg;
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		routeGroupAcqMerchant.setCreateTime(new Date());
		routeGroupAcqMerchant.setCreatePerson(principal.getId().toString());
		//将收单商户加到路由集群，然后更新收单商户表，将路由集群的限额填到该收单商户里面
		int num = routeGroupDao.insertRouteGroupAcqMerchant(routeGroupAcqMerchant);
		Map<String, Object> param = new HashMap<>();
		param.put("groupCode", routeGroupAcqMerchant.getGroupCode());
		TransRouteGroup routeGroup = routeGroupDao.queryRouteGroupById(param);
		AcqMerchant acqMerchant = new AcqMerchant();
		acqMerchant.setAcqMerchantNo(routeGroupAcqMerchant.getAcqMerchantNo());
		acqMerchant.setQuota(routeGroup.getDefAcqDayAmount());
//		acqMerchantDao.updateByPrimaryKey(acqMerchant);
		if(acqMerchant.getDayQuota()==null){
			acqMerchant.setDayQuota(acqMerchant.getQuota());
		}
		acqMerchantDao.updateAcqMerchantQuota(acqMerchant);
		if(num==1){
			msg.put("status", true);
			msg.put("msg", "添加成功");
		}
		return msg;
	}

	@Override
	public int updateGroupStatus(TransRouteGroup group) {
		return routeGroupDao.updateGroupStatus(group);
	}

	@Override
	public TransRouteGroup getGroupByCode(String groupCode) {
		TransRouteGroup info = routeGroupDao.getGroupByCode(groupCode);
		return info;
	}

	@Override
	public int deleteRouteGroup(int id){
		return routeGroupDao.deleteRouteGroup(id);
	}

	@Override
	public List<TransRouteGroup> getGroupByServiceType(String[] serviceTypes,String group) {
		return routeGroupDao.getGroupByServiceType(serviceTypes,group);
	}

	@Override
	public Map<String, Object> importDiscount(MultipartFile file) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = String.valueOf(principal.getId());
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		//读取第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		// 遍历所有单元格，读取单元格
		int row_num = sheet.getLastRowNum();
		List<TransRouteGroupAcqMerchant> list=new ArrayList<TransRouteGroupAcqMerchant>();
		List<String> acqMerchantNoList=new ArrayList<String>();
		Date da=new Date();
		for (int i = 1; i <= row_num; i++) {
			Row row = sheet.getRow(i);
			String acqMerchantNo1 = CellUtil.getCellValue(row.getCell(0));//收单机构商户编号
			String acqMerchantName = CellUtil.getCellValue(row.getCell(1));//收单机构商户名称
			String groupCode1 = CellUtil.getCellValue(row.getCell(2));//集群编号
			String acqEnname = CellUtil.getCellValue(row.getCell(3));//收单机构名称
			if(acqMerchantNo1==null||"".equals(acqMerchantNo1)){
				msg.put("status", false);
				msg.put("msg","导入失败,第"+(i+1)+"行,收单机构商户编号为空!");
				return msg;
			}
			String acqMerchantNo=acqMerchantNo1.split("\\.")[0];

			if(acqMerchantNoList.contains(acqMerchantNo)){
				msg.put("status", false);
				msg.put("msg","导入失败,第"+(i+1)+"行,收单机构商户编号"+acqMerchantNo+"重复!");
				return msg;
			}
			acqMerchantNoList.add(acqMerchantNo);
			int count = acqMerchantDao.selectOrgMerExistByMerNo(acqMerchantNo);
			if(count==0){
				msg.put("status", false);
				msg.put("msg", "导入失败,第"+(i+1)+"行,收单机构商户不存在!");
				return msg;
			}
			TransRouteGroupMerchant rouMer=routeGroupDao.getTransRouteGroupMerchant(acqMerchantNo);
			if(rouMer!=null){
				msg.put("status", false);
				msg.put("msg", "导入失败,第"+(i+1)+"行,收单机构商户已存在集群"+rouMer.getGroupCode()+"中!");
				return msg;
			}
			if(groupCode1==null||"".equals(groupCode1)){
				msg.put("status", false);
				msg.put("msg","导入失败,第"+(i+1)+"行,集群编号为空!");
				return msg;
			}
			String groupCode =groupCode1.split("\\.")[0];
			int groupCodeNum=0;
			try {
				groupCodeNum=Integer.valueOf(groupCode);
			}catch (Exception e){
				msg.put("status", false);
				msg.put("msg","导入失败,第"+(i+1)+"行,集群编号格式错误!");
				return msg;
			}
			TransRouteGroup routeGroup = routeGroupDao.getGroupByCode(groupCode);
			if(routeGroup==null){
				msg.put("status", false);
				msg.put("msg","导入失败,第"+(i+1)+"行,集群"+groupCode+"不存在!");
				return msg;
			}
			if(acqEnname==null||"".equals(acqEnname)){
				msg.put("status", false);
				msg.put("msg","导入失败,第"+(i+1)+"行,收单机构名称为空!");
				return msg;
			}
			TransRouteGroupAcqMerchant routeGroupAcqMerchant=new TransRouteGroupAcqMerchant();
			routeGroupAcqMerchant.setAcqMerchantNo(acqMerchantNo);
			routeGroupAcqMerchant.setGroupCode(groupCodeNum);
			routeGroupAcqMerchant.setCreateTime(da);
			routeGroupAcqMerchant.setCreatePerson(userId);
			list.add(routeGroupAcqMerchant);
		}
		for(TransRouteGroupAcqMerchant rou:list){
			routeGroupDao.insertRouteGroupAcqMerchant(rou);
			Map<String, Object> param = new HashMap<>();
			param.put("groupCode", rou.getGroupCode());
			TransRouteGroup routeGroup = routeGroupDao.queryRouteGroupById(param);
			AcqMerchant acqMerchant = new AcqMerchant();
			acqMerchant.setAcqMerchantNo(rou.getAcqMerchantNo());
			acqMerchant.setQuota(routeGroup.getDefAcqDayAmount());
			acqMerchant.setDayQuota(acqMerchant.getQuota());
			acqMerchantDao.updateAcqMerchantQuota(acqMerchant);
		}
		msg.put("status", true);
		msg.put("msg", "导入成功");
		return msg;
	}

	@Override
	public List<Map<String, String>> getMapGroupSelect(Integer groupCode) {
		return routeGroupDao.findAll(groupCode);
	}
}
