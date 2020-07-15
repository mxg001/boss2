package cn.eeepay.framework.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.cjt.CjtOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.dao.JumpRouteDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.JumpRouteService;

@Service("jumpRouteService")
@Transactional
public class JumpRouteServiceImpl implements JumpRouteService {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	private SysDictService sysDictService;

	@Resource
	private JumpRouteDao jumpRouteDao;

	@Override
	public List<JumpRouteConfig> list(JumpRouteConfig baseInfo, Page<JumpRouteConfig> page) {
		jumpRouteDao.list(baseInfo, page);
		List<JumpRouteConfig> list = page.getResult();
		if(list != null && list.size() > 0){
			for(JumpRouteConfig jumpRouteConfig: list){
				if(jumpRouteConfig.getEffectiveDateType() == null || jumpRouteConfig.getEffectiveDateType() != 4){
					jumpRouteConfig.setStartDate(null);
					jumpRouteConfig.setEndDate(null);
				}
			}
		}
		return list;
	}


	@Override
	public int save(String params) {
		JSONObject json = JSON.parseObject(params);
		JumpRouteConfig baseInfo = json.getObject("baseInfo", JumpRouteConfig.class);
		if(baseInfo == null ){
			throw new BossBaseException("请求参数非法");
		}
		List<AreaInfo> provinceList = JSON.parseArray(json.getJSONArray("provinceList").toJSONString(),
				AreaInfo.class);
		List<BusinessProductDefine> bpList = JSON.parseArray(json.getJSONArray("bpList").toJSONString(),
				BusinessProductDefine.class);
		List<Map> cardBinList = JSON.parseArray(json.getJSONArray("cardBinList").toJSONString(),
				Map.class);

		List<SysDict> acqMerchantList = JSON.parseArray(json.getJSONArray("acqMerchantList").toJSONString(),
				SysDict.class);
		List<AreaInfo> cityList = JSON.parseArray(json.getJSONArray("cityList").toJSONString(),
				AreaInfo.class);
		List<String> serviceTypeList = JSON.parseArray(json.getJSONArray("serviceTypes").toJSONString(),
				String.class);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < serviceTypeList.size(); i++) {
			builder.append(serviceTypeList.get(i));
			if (i!=serviceTypeList.size()-1) {
				builder.append(",");
			}
		}
		baseInfo.setServiceTypes(org.springframework.util.StringUtils.hasLength(builder.toString())?builder.toString():"0");
		if(baseInfo.getAcqMerchantShowState()==1){
			if(baseInfo.getAcqMerchantState()!=null){
				if(baseInfo.getAcqMerchantState()==0){
					baseInfo.setAcqMerchantType("0");
				}
				if(baseInfo.getAcqMerchantState()==1){
					StringBuilder sb = new StringBuilder();
					for(SysDict p: acqMerchantList){
						sb.append(p.getSysValue()).append(",");
					}
					baseInfo.setAcqMerchantType(sb.substring(0, sb.length()-1));
				}
			}
		}else{
			baseInfo.setAcqMerchantType(null);
		}

		//如果业务产品选择不限，则设置bpIds为“0”
		//如果选择指定业务产品，则设置bpIds为选中业务产品id的拼接
		if(baseInfo.getBpType()!=null&&baseInfo.getBpType()==0){
			baseInfo.setBpIds("0");
		}
		if(baseInfo.getBpType()!=null&&baseInfo.getBpType()==1){
			StringBuilder sb = new StringBuilder();
			for(BusinessProductDefine bp: bpList){
				sb.append(bp.getBpId()).append(",");
			}
			baseInfo.setBpIds(sb.substring(0, sb.length()-1));
		}
		if(baseInfo.getBpIds().length() > 200) {
			throw new BossBaseException("勾选的业务产品数量超出范围，不允许该操作。");
		}
		//如果省份选择不限，则设置provinceIds为“0”
		//如果选择指定省份，则设置provinceIds为选中省份的名称拼接
		if(baseInfo.getProvinceType()!=null&&baseInfo.getProvinceType()==0){
			baseInfo.setMerchantProvinces("0");
			baseInfo.setMerchantCity("0");
		}
		if(baseInfo.getProvinceType()!=null&&baseInfo.getProvinceType()==1){
			StringBuilder sb = new StringBuilder();
			for(AreaInfo province: provinceList){
				sb.append(province.getName()).append(",");
			}
			baseInfo.setMerchantProvinces(sb.substring(0, sb.length()-1));
			//设置市
			if(cityList!=null&&cityList.size()>0){
				StringBuilder ss = new StringBuilder();
				for(AreaInfo p: cityList){
					ss.append(p.getName()).append(",");
				}
				baseInfo.setMerchantCity(ss.substring(0, ss.length()-1));
			}else{
				baseInfo.setMerchantCity("0");
			}
		}
		if(baseInfo.getCardBinType()!=null&&baseInfo.getCardBinType()==0){
			baseInfo.setCardBinIds("0");
		}
		
		if(baseInfo.getCardBinType()!=null&&baseInfo.getCardBinType()==1){
			StringBuilder binSb = new StringBuilder();
			for(Map cardBin: cardBinList){
				binSb.append(cardBin.get("id")).append(",");
			}
			baseInfo.setCardBinIds(binSb.substring(0, binSb.length()-1));
		}

		//如果生效时间类型为自定义，生效时间不能为空
		if(baseInfo.getEffectiveDateType() == null){
			throw new BossBaseException("生效日期类型不能为空");
		}
		if(baseInfo.getEffectiveDateType() == 4){
			Date startDate = baseInfo.getStartDate();
			Date endDate = baseInfo.getEndDate();
			if(startDate == null || endDate == null){
				throw new BossBaseException("开始生效日期或截止生效日期不能为空");
			}
			if(!DateUtil.compare(startDate, endDate)){
				throw new BossBaseException("开始生效日期不能大于截止生效日期");
			}
		}
		//目标金额可以为空，但是不能为负
		if(baseInfo.getTargetAmount() != null){
			if(baseInfo.getTargetAmount().compareTo(BigDecimal.ZERO) < 0){
				throw new BossBaseException("目标金额不可小于0");
			}
		}
		int num = 0;
		if(baseInfo.getId()!=null){
			num = jumpRouteDao.update(baseInfo);
		} else {
			baseInfo.setTotalAmount(BigDecimal.ZERO);
			num = jumpRouteDao.insert(baseInfo);
		}
		return num;
	}

	@Override
	public JumpRouteConfig getById(Integer id) {
		JumpRouteConfig baseInfo = jumpRouteDao.getById(id);
		if(baseInfo!=null){
			//指定行业
			if(baseInfo.getAcqMerchantType()==null){
				baseInfo.setAcqMerchantShowState(0);
				baseInfo.setAcqMerchantState(0);
			}else if("0".equals(baseInfo.getAcqMerchantType())){
				baseInfo.setAcqMerchantShowState(1);
				baseInfo.setAcqMerchantState(0);
			}else{
				baseInfo.setAcqMerchantShowState(1);
				baseInfo.setAcqMerchantState(1);
				baseInfo.setAcqMerchantList(baseInfo.getAcqMerchantType().split(","));
			}

			//业务产品“0”表示不限
			//如果不是不限，需要转换成数组传到前台
			if(StringUtils.isNoneBlank(baseInfo.getBpIds()) 
					&& !"0".equals(baseInfo.getBpIds())){
				baseInfo.setBpType(1);
				String[] strList = baseInfo.getBpIds().split(",");
				Long[] bpIdList = new Long[strList.length];
				for(int i = 0; i < strList.length; i++) {
					bpIdList[i] = Long.parseLong(strList[i]);
				}
				baseInfo.setBpList(bpIdList);
			}
			if("0".equals(baseInfo.getBpIds())){
				baseInfo.setBpType(0);
			}
			//商户省份“0”表示不限
			//如果不是不限，需要转换成数组传到前台
			if(StringUtils.isNoneBlank(baseInfo.getMerchantProvinces()) 
					&& !"0".equals(baseInfo.getMerchantProvinces())){
				baseInfo.setProvinceType(1);
				baseInfo.setProvinceList(baseInfo.getMerchantProvinces().split(",")); 
			}
			if("0".equals(baseInfo.getMerchantProvinces())){
				baseInfo.setProvinceType(0);
			}
			if(StringUtils.isNoneBlank(baseInfo.getMerchantCity())
					&& !"0".equals(baseInfo.getMerchantCity())){
				baseInfo.setCityList(baseInfo.getMerchantCity().split(","));
			}
			
			if(StringUtils.isNoneBlank(baseInfo.getCardBinIds())&& !"0".equals(baseInfo.getCardBinIds())){
				baseInfo.setCardBinType(1);
				baseInfo.setCardBinList(baseInfo.getCardBinIds().split(","));
			}
			if("0".equals(baseInfo.getCardBinIds())){
				baseInfo.setCardBinType(0);
			}
		}
		return baseInfo;
	}
	
	public int delete(String id){
		return jumpRouteDao.delete(id);
	}

	@Override
	public List<AcpWhitelist> selectAllWlInfo() {
		return jumpRouteDao.selectAllWlInfo();
	}

	@Override
	public int deleteByWlid(int id) {
		return jumpRouteDao.deleteByWlid(id);
	}

	@Override
	public int insertWl(AcpWhitelist record) {
		
		return jumpRouteDao.insertWl(record);
	}

	@Override
	public AcpWhitelist getWlInfoByMerchantNo(String merchantNo) {
		return jumpRouteDao.getWlInfoByMerchantNo(merchantNo);
	}


	@Override
	public List<SysDict> getServiceTypeSelectByBqIds(Integer[] ids) {
		return jumpRouteDao.findServiceTypeSelectByBqIds(ids);
	}

	@Override
	public void export(HttpServletResponse response, JumpRouteConfig baseInfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		OutputStream ouputStream = null;
		try {
			Page<JumpRouteConfig> page = new Page<>(0, Integer.MAX_VALUE);
			list(baseInfo, page);
			List<JumpRouteConfig> configList = page.getResult();
			int size = 2;
			ListDataExcelExport export = new ListDataExcelExport(size);
			String fileName = "跳转集群规则" + sdf.format(new Date()) + export.getFileSuffix(size);
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			List<Map<String, String>> data = new ArrayList<>();

			Map<Integer, String> effectiveDateType = new HashMap<>();
			effectiveDateType.put(1, "每天");
			effectiveDateType.put(2, "周一至周五");
			effectiveDateType.put(3, "法定工作日");
			effectiveDateType.put(4, "自定义");

			Map<String, String> cardTypeMap = sysDictService.selectMapByKey("CARD_TYPE");

			for(JumpRouteConfig item: configList){
				Map<String,String> map = new HashMap<>();
				map.put("id", String.valueOf(item.getId()));
				map.put("status", item.getStatus() != null && item.getStatus() != 1 ? "失效" : "生效");
				map.put("effectiveDateType", effectiveDateType.get(item.getEffectiveDateType()));
				map.put("startDate", item.getStartDate() != null ? DateUtil.getLongFormatDate(item.getStartDate()) : "");
				map.put("endDate", item.getEndDate() != null ? DateUtil.getLongFormatDate(item.getEndDate()) : "");
				map.put("cardType", cardTypeMap.get(String.valueOf(item.getCardType())));
				map.put("startTime", item.getStartTime());
				map.put("endTime", item.getEndTime());
				map.put("minTransAmount", item.getMinTransAmount() != null ? String.valueOf(item.getMinTransAmount()) : "");
				map.put("maxTransAmount", item.getMaxTransAmount() != null ? String.valueOf(item.getMaxTransAmount()) : "");
				map.put("groupCode", String.valueOf(item.getGroupCode()));
				map.put("groupName", item.getGroupName());
				map.put("acqEnname", item.getAcqEnname());
				map.put("remark", item.getRemark());
				data.add(map);
			}
			String[] cols = new String[]{
					"id","status","effectiveDateType","startDate","endDate","cardType","startTime","endTime","minTransAmount",
					"maxTransAmount","groupCode","groupName","acqEnname","remark",
			};
			String[] colsName = new String[]{
					"id","状态","生效日期","开始生效日期","截止生效日期","卡类型","每天生效时间","每天截止时间","最小交易金额",
					"最大交易金额","可用集群","跳转目标路由集群","收单机构","备注",
			};
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("跳转路由集群导出异常", e);
		} finally {
			if(ouputStream!=null){
				try {
					ouputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
