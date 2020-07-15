package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.RouterOrgDao;
import cn.eeepay.framework.dao.TransRouteGroupDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TransRouteGroup;
import cn.eeepay.framework.service.RouterOrgService;
import cn.eeepay.framework.util.CellUtil;
import cn.eeepay.framework.util.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 集群中收单商户service实现
 * 
 * @author junhu
 *
 */
@Service
@Transactional
public class RouterOrgServiceImpl implements RouterOrgService {

	@Resource
	private RouterOrgDao routerOrgDao;
	@Resource
	private TransRouteGroupDao transRouteGroupDao;

	@Override
	public List<Map> listRouterAcqMerchantByCon(Map<String, Object> param, Page<Map> page) {

		return routerOrgDao.listRouterAcqMerchantByCon(param, page);
	}

	@Override
	public int deleteRouterAcqMerchantById(Long id) {

		return routerOrgDao.deleteRouterAcqMerchantById(id);
	}

	@Override
	public int updateAcqMerchantQuota(Map<String, Object> param) {

		return routerOrgDao.updateAcqMerchantQuota(param);
	}

	@Override
	public List<Map<String,Object>> selecrAllInfoRecordInfo(Map<String,Object> map) {
		return routerOrgDao.selecrAllInfoRecordInfoExport(map);
	}

	@Override
	public Map<String, Object> routerOrgBatchDelete(MultipartFile file) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		//读取第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		// 遍历所有单元格，读取单元格
		int row_num = sheet.getLastRowNum();

		List<Map<String,Object>> errorList=new ArrayList<Map<String,Object>>();
		List<Map<String,String>> checkList=new ArrayList<Map<String,String>>();//校验
		List<Map<String,String>> deleteList=new ArrayList<Map<String,String>>();//需要更新的

		for (int i = 1; i <= row_num; i++) {
			Row row = sheet.getRow(i);
			String acqMerchantNo1 = CellUtil.getCellValue(row.getCell(0));//收单机构商户编号
			String groupCode1 = CellUtil.getCellValue(row.getCell(2));//集群编号

			Map<String,Object> errorMap=new HashMap<String,Object>();
			int rowNum=i+1;
			if(acqMerchantNo1==null||"".equals(acqMerchantNo1)){
				errorMap.put("msg","第"+rowNum+"行,收单机构商户编号为空!");
				errorList.add(errorMap);
				continue;
			}
			String acqMerchantNo=acqMerchantNo1.split("\\.")[0];

			if(groupCode1==null||"".equals(groupCode1)){
				errorMap.put("msg","第"+rowNum+"行,集群编号为空!");
				errorList.add(errorMap);
				continue;
			}
			String groupCode=groupCode1.split("\\.")[0];


			//验证重复
			if(existList(checkList,acqMerchantNo,groupCode)){
				errorMap.put("msg","导入失败,第"+rowNum+"行,收单机构商户编号("+acqMerchantNo+"),集群编号("+groupCode+")重复!");
				errorList.add(errorMap);
				continue;
			}else{
				Map<String,String> map=new HashMap<String,String>();
				map.put("acqMerchantNo",acqMerchantNo);
				map.put("groupCode",groupCode);
				checkList.add(map);
			}

			List<Map<String,Object>> oldList = routerOrgDao.getTransRouteGroupAcqMerchant(acqMerchantNo,groupCode);
			if(oldList!=null&&oldList.size()>0){
				Map<String,String> map=new HashMap<String,String>();
				map.put("acqMerchantNo",acqMerchantNo);
				map.put("groupCode",groupCode);
				deleteList.add(map);
			}else{
				errorMap.put("msg", "第"+rowNum+"行,集群编号("+groupCode+")中的收单机构商户编号("+acqMerchantNo+")不存在!");
				errorList.add(errorMap);
				continue;
			}
		}
		int num=0;
		String delNo=null;
		if(deleteList!=null&&deleteList.size()>0){
			delNo= DateUtil.dateToUnixTimestamp()+"";
			for(Map<String,String> item:deleteList){
				item.put("deleteNo",delNo);
				routerOrgDao.insertAcqMerImportDel(item);
				//num=num+routerOrgDao.deleteAcqMer(item.get("acqMerchantNo"),item.get("groupCode"));
			}
		}
		msg.put("errorList", errorList);
		msg.put("status", true);
		msg.put("delNo", delNo);
		msg.put("msg", "总共"+row_num+"条数据,错误数据"+errorList.size()+"条!");
		return msg;
	}

	private boolean existList(List<Map<String,String>> list,String acqMerchantNo,String groupCode){
		if(list!=null&&list.size()>0){
			for(Map<String,String> map:list){
				if(map.get("acqMerchantNo").equals(acqMerchantNo)&&map.get("groupCode").equals(groupCode)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public  int selectAcqMerCount(Integer groupCode){
		return routerOrgDao.selectAcqMerCount(groupCode);
	}

	@Override
	public List<Map<String, Object>> deleteBatchCount(List<Map> list){
		List<Map<String, Object>>  jsonlist = new ArrayList<Map<String, Object>>();
		for (Map m : list) {
			Map<String,Object> map=new HashMap<String,Object>();
			boolean flag=true;
			for (Map jsonMap : jsonlist) {
				if(m.get("group_code").toString().equals(jsonMap.get("group_code").toString())){
					flag=false;
					jsonMap.put("acqMerCount",Integer.parseInt(jsonMap.get("acqMerCount").toString())-1);
					break;
				}
			}
			if(flag){
				TransRouteGroup transRouteGroup=transRouteGroupDao.selectByPrimaryKey(Integer.parseInt(m.get("group_code").toString()));
				map.put("group_code",m.get("group_code"));
				map.put("group_name",transRouteGroup.getGroupName());
				map.put("acqMerCount",routerOrgDao.selectAcqMerCount(Integer.parseInt(m.get("group_code").toString())));
				jsonlist.add(map);
			}
		}
		return jsonlist;
	}

	@Override
	public List<Map<String, Object>> deleteImportDelCount(String delNo){
		List<Map> list=routerOrgDao.selectAcqMerImportDel(delNo);
		return this.deleteBatchCount(list);
	}

	@Override
	public int deleteImportDelBatch(String delNo){
		int num=0;
		List<Map> list=routerOrgDao.selectAcqMerImportDel(delNo);
		for (Map m:list){
			num=num+routerOrgDao.deleteAcqMer(m.get("acq_merchant_no").toString(),m.get("group_code").toString());
		}
		routerOrgDao.deleteAcqMerImportDel(delNo);
		return num;
	}

	@Override
	public int deleteAcqMerImportDel(String delNo){
		return routerOrgDao.deleteAcqMerImportDel(delNo);
	}
}
