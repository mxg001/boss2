package cn.eeepay.framework.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AreaInfoDao;
import cn.eeepay.framework.model.AreaInfo;
import cn.eeepay.framework.service.AreaInfoService;

@Service
@Transactional
public class AreaInfoServiceImpl implements AreaInfoService {

	@Resource
	private AreaInfoDao areaInfoDao;

	@Override
	public List<Map<String,Object>> provinceSelectBox() {

		return areaInfoDao.provinceSelectBox();
	}

	@Override
	public List<Map<String,Object>> citySelectBox(String province) {

		return areaInfoDao.citySelectBox(province);
	}
	
	@Override
	public List<Map<String,Object>> getItemByParentId(Integer pid){
		return areaInfoDao.getItemsByParentId(pid);
	}

	@Override
	public List<Map<String,Object>> getItemByParentIds(String pids){
		return areaInfoDao.getItemsByParentIds(pids);
	}

	@Override
	public Map<String, Object> getAreaByNames(String province, String city, String area) {
		return areaInfoDao.getAreaByNames(province,city,area);
	}

	@Override
	public List<Map<String, Object>> getAreaByName(String type, String name) {
		if("p".equals(type)){
			return areaInfoDao.getItemsByParentId(0);
		}else if(StringUtils.isNotBlank(name)){
			return areaInfoDao.getItemsByName(name);
		}
		return null;
	}

	@Override
	public Map<String,Object> getProvincebyId(int id){
		return areaInfoDao.getProvincebyId(id);
	}
}
