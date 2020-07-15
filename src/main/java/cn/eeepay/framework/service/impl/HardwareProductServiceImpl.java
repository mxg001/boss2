package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.HardwareProductDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.HardwareProductService;
import cn.eeepay.framework.service.TeamInfoService;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("hardwareProductService")
@Transactional
public class HardwareProductServiceImpl implements HardwareProductService {
	
	private static final Logger log = LoggerFactory.getLogger(HardwareProductServiceImpl.class);
	
	@Resource
	public  HardwareProductDao hardwareProductDao;
	
	@Resource
	private TeamInfoService teamInfoService;
	
	@Override
	public List<HardwareProduct> selectAllInfo(String item) {
		if(StringUtil.isBlank(item)){
			return hardwareProductDao.selectAllInfo();
		}else{
			return hardwareProductDao.selectAllInfo2(item);
		}
	}

	@Override
	public HardwareProduct selectHardwareName(String hardWareId) {
		return hardwareProductDao.findHardwareName(hardWareId);
	}

	@Override
	public List<HardwareProduct> selectAllHardwareName() {
		return hardwareProductDao.findAllHardwareName();
	}

	@Override
	public int addHard(HardwareProduct hardProduct) {
		return hardwareProductDao.addHard(hardProduct);
	}
	
	@Override
	public int updateHard(HardwareProduct hardProduct) {
		return hardwareProductDao.updateHard(hardProduct);
	}

	@Override
	public List<HardwareProduct> selectByParams(String typeName, String versionNu) {
		return hardwareProductDao.selectByParams(typeName,versionNu);
	}

	public List<HardwareProduct> selectByParamsUpdate(String typeName, String versionNu,Long id) {
		return hardwareProductDao.selectByParamsUpdate(typeName,versionNu,id);
	}
	
	
	@Override
	public List<HardwareProduct> findHardWareBybpId(String bpId) {
		return hardwareProductDao.findHardWareBybpId(bpId);
	}

	@Override
	public List<HardwareProduct> selectByCondition(Page<HardwareProduct> page, HardwareProduct bpd) {
		return hardwareProductDao.selectByCondition(page,bpd);
	}

	@Override
	public Map<String, Object> queryHardById(String id) {
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("status", false);
		
		try {
			List<TeamInfo> allTeam = teamInfoService.selectTeamName();
			msg.put("teamInfo", allTeam);
			Map<String, List<SubTeam>> subTeamMap = teamInfoService.querySubTeams();
			msg.put("subTeamMap", subTeamMap);
		} catch (Exception e) {
			msg.put("msg", "查询组织异常");
			log.error(e.getMessage());
		}
		
		HardwareProduct hp = hardwareProductDao.queryHardById(id);
		if (hp!=null) {
			msg.put("status", true);
			msg.put("hp", hp);
		}
		return msg;
	}
	public List<HardwareProduct> getHardwareProduct() {
		List<HardwareProduct> list = hardwareProductDao.selectAllInfo();
		for (HardwareProduct hardwareProduct : list) {
			hardwareProduct.setTypeName(hardwareProduct.getTypeName() + hardwareProduct.getVersionNu());
		}
		return list;
	}

	@Override
	public Map<String, String> getHardwareProductMap() {
		Map<String, String> map=new HashMap<String, String>();
		List<HardwareProduct> list = hardwareProductDao.selectAllInfo();
		if(list!=null&&list.size()>0){
			for (HardwareProduct item : list) {
				map.put(item.getHpId().toString(),item.getTypeName() + item.getVersionNu());
			}
		}
		return map;
	}

	@Override
	public HardwareProduct getHardwareProductByBpId(Long bpId) {
		HardwareProduct info = hardwareProductDao.getHardwareProductByBpId(bpId);
		return info;
	}

	@Override
	public HardwareProduct getHardwareProductByBpName(String bpName) {
		HardwareProduct info = hardwareProductDao.getHardwareProductByBpName(bpName);
		return info;
	}

	@Override
	public List<HardwareProduct> selectAllHardwareProduct(List<SysDict> sysDicts) {
		StringBuilder pns = new StringBuilder("(");
		for (int i = 0; i < sysDicts.size(); i++) {
			if(i != sysDicts.size() - 1){
				pns.append("'" + sysDicts.get(i).getSysValue() + "',");
			}else {
				pns.append("'" + sysDicts.get(i).getSysValue() + "')");
			}
		}
		return hardwareProductDao.selectAllHardwareProduct(pns.toString());
	}


	@Override
	public List<HardwareProduct> selectAllInfoByTeamId(String teamId) {
		if(StringUtils.isNotBlank(teamId)){
			return hardwareProductDao.selectAllInfoByTeamId(teamId);
		}else{
			return hardwareProductDao.selectAllInfo();
		}
	}

	@Override
	public HlfHardware isHlfHardware(String sn) {
		List<HlfHardware> list= hardwareProductDao.isHlfHardware(sn);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
