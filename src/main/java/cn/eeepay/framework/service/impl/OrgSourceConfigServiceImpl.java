package cn.eeepay.framework.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.daoSuperbank.CreditcardSourceDao;
import cn.eeepay.framework.daoSuperbank.LoanSourceDao;
import cn.eeepay.framework.daoSuperbank.OpenFunctionConfDao;
import cn.eeepay.framework.daoSuperbank.OrgInfoDao;
import cn.eeepay.framework.daoSuperbank.OrgSourceConfDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditcardSource;
import cn.eeepay.framework.model.LoanSource;
import cn.eeepay.framework.model.OpenFunctionConf;
import cn.eeepay.framework.model.OrgInfo;
import cn.eeepay.framework.model.OrgSourceConf;
import cn.eeepay.framework.model.OrgSourceConfigSumInfo;
import cn.eeepay.framework.service.OrgSourceConfigService;
import cn.eeepay.framework.util.CommonUtil;

@Transactional
@Service("OrgSourceConfigService")
public class OrgSourceConfigServiceImpl implements OrgSourceConfigService{

	@Resource
	private OrgSourceConfDao orgSourceConfDao;
	@Resource
	private OrgInfoDao orgInfoDao;
	@Resource
	private CreditcardSourceDao creditcardSourceDao;
	@Resource
	private LoanSourceDao loanSourceDao;
	@Resource
	private OpenFunctionConfDao openFunctionConfDao;

	@Override
	public List<OrgSourceConf> getCountByRecommend(int orgId, String isRecommend, String application, String type) {
		return orgSourceConfDao.getCountByRecommend(orgId,isRecommend,application,type);
	}

	@Override
	public List<OrgSourceConf> getOrgSourceConfigPage(OrgSourceConf orgSourceConf,Page<OrgSourceConf> page) {
       orgSourceConfDao.getOrgSourceConfList(orgSourceConf, page);
       List<OrgSourceConf> list = page.getResult();
       OpenFunctionConf openFunctionConf = new OpenFunctionConf();
       if("1".equals(orgSourceConf.getType())){
    	   openFunctionConf = openFunctionConfDao.getOpenFunctionConfById((long)15);
    	   for (OrgSourceConf conf: list) {
    		   if("0".equals(conf.getIsRecommend())){
    			   conf.setSpecialImage("");
    		   }
    		   /*if(StringUtils.isNotBlank(conf.getSpecialImage())){
                   conf.setSpecialImageUrl(CommonUtil.getImgUrlAgent(conf.getSpecialImage()));
               }*/
    		   String application = "1".equals(conf.getApplication())?"h5":("2".equals(conf.getApplication())?"app":"");
        	   conf.setOpenUrl(openFunctionConf.getFunctionUrl()+"?creditBankId="+conf.getSourceId()+"&orgId="+orgSourceConf.getOrgId()+"&source="+application);
    	   }
       }else if("2".equals(orgSourceConf.getType())){
    	   openFunctionConf = openFunctionConfDao.getOpenFunctionConfById((long)16);
    	   for (OrgSourceConf conf: list) {
    		   if("0".equals(conf.getIsRecommend())){
    			   conf.setSpecialImage("");
    		   }
    		   /*if(StringUtils.isNotBlank(conf.getSpecialImage())){
                   conf.setSpecialImageUrl(CommonUtil.getImgUrlAgent(conf.getSpecialImage()));
               }*/
    		   String application = "1".equals(conf.getApplication())?"h5":("2".equals(conf.getApplication())?"app":"");;
        	   conf.setOpenUrl(openFunctionConf.getFunctionUrl()+"?loanSourceId="+conf.getSourceId()+"&orgId="+orgSourceConf.getOrgId()+"&source="+application);
    	   }
       }
       page.setResult(list);
       return list;
    }

	@Override
	public OrgSourceConfigSumInfo getOrgSourceConfigSumInfo(OrgSourceConf orgSourceConf) {
		OrgSourceConfigSumInfo sumInfo = new OrgSourceConfigSumInfo();
		orgSourceConf.setStatus("");
		sumInfo.setTotalCount(orgSourceConfDao.getOrgSourceConfcount(orgSourceConf));
		orgSourceConf.setStatus("on");
		sumInfo.setOpenCount(orgSourceConfDao.getOrgSourceConfcount(orgSourceConf));
		orgSourceConf.setStatus("off");
		sumInfo.setOffCount(orgSourceConfDao.getOrgSourceConfcount(orgSourceConf));
		OrgInfo orgInfo = orgInfoDao.selectOrg((long)orgSourceConf.getOrgId());
		sumInfo.setSourceName(orgInfo.getOrgName());
		return sumInfo;
    }

	@Override
	public OrgSourceConf getOrgSourceById(Long id) {
		return orgSourceConfDao.getOrgSourceById(id);
	}

	@Override
	public Integer findExist(int orgId, Integer showOrder, String application,
			String type,long id) {
		return orgSourceConfDao.getOrgConfByParams(orgId,showOrder,application,type,id);
	}

	@Override
	public int updateOrgSourceConfig(JSONArray jsons) {
		int orgId = -1;
		if(jsons!=null && jsons.size()>0){
			for (int i=0;i<jsons.size();i++) {
				JSONObject  obj = jsons.getJSONObject(i);
				String showOrder = obj.getString("showOrder");
				String isRecommend = obj.getString("isRecommend");
				Long id = obj.getLong("id");
				
				OrgSourceConf conf = orgSourceConfDao.getOrgSourceById(id);
				orgId = conf.getOrgId();
				if(conf!=null){
					conf.setShowOrder(showOrder);
					conf.setIsRecommend(isRecommend);
					conf.setUpdateTime(new Date());
					orgSourceConfDao.update(conf);
				}
			}
		}
		return orgId;
	}

	@Override
	public void batchUpd(String[] idArry, String status) {
		for (String id : idArry) {
			orgSourceConfDao.updateStatus(status,Long.parseLong(id));
		}
	}

	@Override
	public void UpdateStatusById(OrgSourceConf baseInfo) {
		Long id = (long)baseInfo.getId();
		String status = baseInfo.getStatus();
		orgSourceConfDao.updateStatus( status,id);
	}

	@Override
	public boolean checkIsCanPutOn(OrgSourceConf conf) {
		if("1".equals(conf.getType())){
			CreditcardSource source = creditcardSourceDao.selectDetail((long)conf.getSourceId());
			if("off".equals(source.getStatus())){
				return false;
			}
		}else if("2".equals(conf.getType())){
			LoanSource source = loanSourceDao.selectDetail((long)conf.getSourceId());
			if("off".equals(source.getStatus())){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean checkIsCanBatchPutOn(String[] idArry) {
		for (String id : idArry) {
			OrgSourceConf conf = orgSourceConfDao.getOrgSourceById(Long.parseLong(id));
			if("1".equals(conf.getType())){
				CreditcardSource source = creditcardSourceDao.selectDetail((long)conf.getSourceId());
				if("off".equals(source.getStatus())){
					return false;
				}
			}else if("2".equals(conf.getType())){
				LoanSource source = loanSourceDao.selectDetail((long)conf.getSourceId());
				if("off".equals(source.getStatus())){
					return false;
				}
			
			}
		}
		return true;
	}

	@Override
	public void initConfig(OrgSourceConf orgSourceConf) {
		List<OrgSourceConf> oldList = orgSourceConfDao.getOldConfList(orgSourceConf);
		if(oldList.size() == 0){
			initOrgAllSourceConf(orgSourceConf);
		}else{
			initOrgConfigService(orgSourceConf, oldList);
		}
	}

	private void initOrgConfigService(OrgSourceConf orgSourceConf,
			List<OrgSourceConf> oldList) {
		if("1".equals(orgSourceConf.getType())){
			List<CreditcardSource> bankList = creditcardSourceDao.getAllBanks();
			if(bankList.size()>oldList.size()){
				OrgSourceConf orgConf = new OrgSourceConf();
				orgConf.setOrgId(orgSourceConf.getOrgId());
				orgConf.setType(orgSourceConf.getType());
				orgConf.setApplication(orgSourceConf.getApplication());
				orgConf.setCreateDate(new Date());
				orgConf.setIsRecommend("1");
				orgConf.setStatus("on");
				for (CreditcardSource creditcardSource : bankList) {
					int order = oldList.get(0).getId()+1;
					boolean flag = true;
					for (OrgSourceConf orgSourceConf1 : oldList) {
						if(orgSourceConf1.getSourceId()==creditcardSource.getId()){
							flag = false;
						}
					}
					if(flag){
						orgConf.setShowOrder(String.valueOf(order++));
						orgConf.setSourceId(creditcardSource.getId().intValue());
						orgSourceConfDao.InsertOrgSourceConf(orgConf);
					}
				}
			}
		}else if("2".equals(orgSourceConf.getType())){
			List<LoanSource> loanList = loanSourceDao.getLoanList();
			if(loanList.size()>oldList.size()){
				OrgSourceConf orgConf = new OrgSourceConf();
				orgConf.setOrgId(orgSourceConf.getOrgId());
				orgConf.setType(orgSourceConf.getType());
				orgConf.setApplication(orgSourceConf.getApplication());
				orgConf.setCreateDate(new Date());
				orgConf.setIsRecommend("1");
				orgConf.setStatus("on");
				for (LoanSource loanSource : loanList) {
					int order = oldList.get(0).getId()+1;
					boolean flag = true;
					for (OrgSourceConf orgSourceConf1 : oldList) {
						if(orgSourceConf1.getSourceId()==loanSource.getId()){
							flag = false;
						}
					}
					if(flag){
						orgConf.setShowOrder(String.valueOf(order++));
						orgConf.setSourceId(loanSource.getId().intValue());
						orgSourceConfDao.InsertOrgSourceConf(orgConf);
					}
				}
			}

		}
	}

	private void initOrgAllSourceConf(OrgSourceConf orgSourceConf) {
		OrgSourceConf orgConf = new OrgSourceConf();
		orgConf.setOrgId(orgSourceConf.getOrgId());
		orgConf.setType(orgSourceConf.getType());
		orgConf.setApplication(orgSourceConf.getApplication());
		orgConf.setCreateDate(new Date());
		orgConf.setIsRecommend("1");
		orgConf.setStatus("on");
		if("1".equals(orgSourceConf.getType())){
			List<CreditcardSource> bankList = creditcardSourceDao.getAllBanks();
			for (int i=0;i<bankList.size();i++) {
				orgConf.setSourceId(bankList.get(i).getId().intValue());
				orgConf.setShowOrder(String.valueOf(i+1));
				orgSourceConfDao.InsertOrgSourceConf(orgConf);
			}	
		}else if("2".equals(orgSourceConf.getType())){
			List<LoanSource> loanList = loanSourceDao.getLoanList();
			for (int i=0;i<loanList.size();i++) {
				orgConf.setSourceId(loanList.get(i).getId().intValue());
				orgConf.setShowOrder(String.valueOf(i+1));
				orgSourceConfDao.InsertOrgSourceConf(orgConf);
			}
		}
	}

	
	@Override
	public Integer isExistOrder(int orgId, Integer order, String application,
			String type, LinkedList<Long> idList) {
		return orgSourceConfDao.isExistOrder(order, idList,orgId,application,type);
	}

	@Override
	public void addOrgSourceConf(Long orgId) {
		OrgSourceConf orgConf = new OrgSourceConf();
		orgConf.setOrgId(orgId.intValue());
		orgConf.setCreateDate(new Date());
		orgConf.setIsRecommend("1");
		orgConf.setStatus("on");

		List<CreditcardSource> bankList = creditcardSourceDao.getAllBanks();
		//添加组织银行卡配置
		for (int i=0;i<bankList.size();i++) {
			orgConf.setSourceId(bankList.get(i).getId().intValue());
			orgConf.setShowOrder(String.valueOf(i+1));
			orgConf.setType("1");
			orgConf.setApplication("1");
			orgSourceConfDao.InsertOrgSourceConf(orgConf);
			orgConf.setApplication("2");
			orgSourceConfDao.InsertOrgSourceConf(orgConf);
		}	
		//添加组织贷款机构配置
		List<LoanSource> loanList = loanSourceDao.getLoanList();
		for (int i=0;i<loanList.size();i++) {
			orgConf.setSourceId(loanList.get(i).getId().intValue());
			orgConf.setShowOrder(String.valueOf(i+1));
			orgConf.setType("2");
			orgConf.setApplication("1");
			orgSourceConfDao.InsertOrgSourceConf(orgConf);
			orgConf.setApplication("2");
			orgSourceConfDao.InsertOrgSourceConf(orgConf);
		}
	}

	@Override
	public void addOrgConf(Long sourceId,String order,String type) {
		OrgSourceConf orgConf = new OrgSourceConf();
		orgConf.setSourceId(sourceId.intValue());
		orgConf.setCreateDate(new Date());
		orgConf.setIsRecommend("0");
		orgConf.setStatus("on");
		orgConf.setType(type);
		orgConf.setShowOrder(order);
		List<OrgInfo> orgInfoList = orgInfoDao.getOrgInfoList();
		for (int i=0;i<orgInfoList.size();i++) {
			orgConf.setOrgId(orgInfoList.get(i).getOrgId().intValue());
			orgConf.setApplication("1");
			orgSourceConfDao.InsertOrgSourceConf(orgConf);
			orgConf.setApplication("2");
			orgSourceConfDao.InsertOrgSourceConf(orgConf);
		}
	}
}
