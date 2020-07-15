package cn.eeepay.framework.service;

import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgSourceConf;
import cn.eeepay.framework.model.OrgSourceConfigSumInfo;

public interface OrgSourceConfigService {

	public List<OrgSourceConf> getOrgSourceConfigPage(OrgSourceConf orgSourceConf,Page<OrgSourceConf> page);

	public OrgSourceConfigSumInfo getOrgSourceConfigSumInfo(OrgSourceConf orgSourceConf);

	public OrgSourceConf getOrgSourceById(Long id);

	public Integer findExist(int orgId, Integer showOrder, String application,
			String type,long id);

	public int updateOrgSourceConfig(JSONArray jsons);

	public void batchUpd(String[] idArry, String status);
	
	public void UpdateStatusById(OrgSourceConf baseInfo);

	public boolean checkIsCanPutOn(OrgSourceConf conf);

	public boolean checkIsCanBatchPutOn(String[] idArry);

	public void initConfig(OrgSourceConf orgSourceConf);
	
	public void addOrgSourceConf(Long orgId);

	public void addOrgConf(Long sourceId,String order,String type);
	
	public Integer isExistOrder(int orgId, Integer order, String application,
			String type, LinkedList<Long> idList);

	List<OrgSourceConf> getCountByRecommend(int orgId,String isRecommend,String application, String type);
}
