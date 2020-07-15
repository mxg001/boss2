package cn.eeepay.framework.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.daoSuperbank.LoanInstitutionManagementDao;
import cn.eeepay.framework.daoSuperbank.OrgSourceConfDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LoanSource;
import cn.eeepay.framework.model.OrgSourceConf;
import cn.eeepay.framework.service.LoanInstitutionManagementService;
import cn.eeepay.framework.service.OrgSourceConfigService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.CommonUtil;

@Service("loanInstitutionManagementService")
public class LoanInstitutionManagementServiceImpl implements LoanInstitutionManagementService {

	@Resource
	private OrgSourceConfigService orgSourceConfigService;
	@Resource
	private LoanInstitutionManagementDao loanInstitutionManagementDao;
	@Resource
	private OrgSourceConfDao orgSourceConfDao;
	
	@Override
	public List<LoanSource> selectList(LoanSource baseInfo, Page<LoanSource> page) {
		List<LoanSource> selectList = loanInstitutionManagementDao.selectList(baseInfo, page);
		for (LoanSource info: selectList){
			/*OrgSourceConf conf = orgSourceConfDao.getBankersOrgSource(info.getId(),"2");
			if(null!=conf&&!"".equals(conf.getShowOrder())){
				info.setShowOrder(Integer.parseInt(conf.getShowOrder()));
			}*/
			 if(StringUtils.isNotBlank(info.getShowLogo())){
                 info.setShowLogoUrl(CommonUtil.getImgUrlAgent(info.getShowLogo()));
             }
            if(StringUtils.isNotBlank(info.getStatus()) && "on".equals(info.getStatus())){
                info.setStatusInt(1);
            } else {
                info.setStatusInt(0);
            }
            String showLoanBonus="";
            if(StringUtils.isNotBlank(info.getProfitType()) && "1".equals(info.getProfitType())){
            	showLoanBonus=info.getLoanBonus().toString();
            }else if(StringUtils.isNotBlank(info.getProfitType()) && "2".equals(info.getProfitType())){
            	showLoanBonus="N*"+info.getLoanBonus().toString()+"%";
            }
            info.setShowLoanBonus(showLoanBonus);
        }
		return selectList;
	}

	@Override
	public LoanSource selectDetail(Long id) {
		LoanSource loanSource = loanInstitutionManagementDao.selectDetail(id);
		if(null!=loanSource && !StringUtils.isBlank(loanSource.getPropaganda())){
			String [] propagandas=loanSource.getPropaganda().split("&&;");
			if(propagandas.length==1){
				loanSource.setPropaganda(propagandas[0]);
			}
			if(propagandas.length==2){
				loanSource.setPropaganda(propagandas[0]);
				loanSource.setPropaganda2(propagandas[1]);
			}
			if(propagandas.length==3){
				loanSource.setPropaganda(propagandas[0]);
				loanSource.setPropaganda2(propagandas[1]);
				loanSource.setPropaganda3(propagandas[2]);
				}
			 if(StringUtils.isNotBlank(loanSource.getShowLogo())){
				 loanSource.setShowLogoUrl(CommonUtil.getImgUrlAgent(loanSource.getShowLogo()));
             }
			 if(StringUtils.isNotBlank(loanSource.getH5Link())){
				 loanSource.setH5LinkUrl(CommonUtil.getImgUrlAgent(loanSource.getH5Link()));
             }
			 if(StringUtils.isNotBlank(loanSource.getSpecialImage())){
				 loanSource.setSpecialImageUrl(CommonUtil.getImgUrlAgent(loanSource.getSpecialImage()));
			 }
		}
		return loanSource;
	}

	@Override
	public int addLoan(LoanSource info) {
	        if(StringUtils.isBlank( info.getLoanProduct())){
	            throw new BossBaseException("贷款机构名称为空");
	        }
	        if(StringUtils.isBlank( info.getLoanProduct())){
	            throw new BossBaseException("贷款机构别称为空");
	        }
	        LoanSource loanSource = loanInstitutionManagementDao.selectLoanAliasExists(info.getLoanAlias());
	        if(loanSource !=null){
	            throw new BossBaseException("贷款机构别称已存在");
	        }
	        if(StringUtils.isBlank( info.getSource())){
	            throw new BossBaseException("来源为空");
	        }
	        if(info.getShowOrder()==null){
	            throw new BossBaseException("显示顺序为空");
	        }
	        loanSource = loanInstitutionManagementDao.selectOrderExists(info.getShowOrder().toString());
	        if(loanSource !=null){
	            throw new BossBaseException("显示顺序已存在");
	        }
	        if(StringUtils.isBlank(info.getPropaganda())){
	            throw new BossBaseException("宣传语1为空");
	        }
	        if(StringUtils.isBlank(info.getPropaganda2())){
	            throw new BossBaseException("宣传语2为空");
	        }
	        if(StringUtils.isBlank(info.getPropaganda3())){
	            throw new BossBaseException("宣传语3为空");
	        }
	        if(StringUtils.isBlank(info.getProfitType())){
	            throw new BossBaseException("奖金方式为空");
	        }
	        if(StringUtils.isBlank(info.getRewardRequirements())){
	            throw new BossBaseException("奖励要求为空");
	        }
			if(info.getAccessWay()==1){
	        if(StringUtils.isBlank(info.getLoanLink())){
	            throw new BossBaseException("申请h5链接为空");
	        } }
	        if(info.getAccessWay()==2){
			if(StringUtils.isBlank(info.getApiUrl())){
				throw new BossBaseException("申请API链接为空");
			}
	        }
	        if(StringUtils.isBlank(info.getSettlementCycle())){
	            throw new BossBaseException("结算周期为空");
	        }
	        if(StringUtils.isBlank(info.getSettlementRules())){
	            throw new BossBaseException("结算规则为空");
	        }
			if(info.getAccessWay()==0){
			throw new BossBaseException("接入方式为空");
			}
	        if(StringUtils.isNotBlank(info.getRuleCode())){
	         loanSource = loanInstitutionManagementDao.selectRuleCodeIdExists(info.getRuleCode());
	         if(loanSource !=null){
		            throw new BossBaseException("导入匹配规则编码已存在");
		        }
	        }
		info.setCreateTime(new Date());
		StringBuffer propaganda=new StringBuffer();
		propaganda.append(info.getPropaganda()).append("&&;").append(info.getPropaganda2()).append("&&;").append(info.getPropaganda3());
		info.setPropaganda(propaganda.toString());
		info.setStatus("off");
		loanInstitutionManagementDao.insert(info);
		LoanSource loanSource2 = loanInstitutionManagementDao.selectLoanAliasExists( info.getLoanAlias());
		orgSourceConfigService.addOrgConf(loanSource2.getId(),String.valueOf(info.getShowOrder()),"2");
		return info.getId().intValue();
	}

	@Override
	public int updateLoan(LoanSource info) {
		if(StringUtils.isBlank( info.getLoanProduct())){
            throw new BossBaseException("贷款机构名称为空");
        }
        if(StringUtils.isBlank( info.getLoanProduct())){
            throw new BossBaseException("贷款机构别称为空");
        }
        LoanSource loanSource = loanInstitutionManagementDao.selectLoanAliasExists(info.getLoanAlias());
        if(loanSource !=null){
        	if(loanSource.getId().longValue()!=info.getId().longValue()){
            throw new BossBaseException("贷款机构别称已存在");
        	}
        }
        if(StringUtils.isBlank( info.getSource())){
            throw new BossBaseException("来源为空");
        }
        if(info.getShowOrder()==null){
            throw new BossBaseException("显示顺序为空");
        }
        if(StringUtils.isNotBlank(info.getRuleCode())){
         loanSource = loanInstitutionManagementDao.selectRuleCodeIdExists(info.getRuleCode());
         if(loanSource !=null){
        	if(loanSource.getId().longValue()!=info.getId().longValue()){
            throw new BossBaseException("导入匹配规则编码已存在");
        	}
        }}
         loanSource = loanInstitutionManagementDao.selectOrderExists(info.getShowOrder().toString());
         if(loanSource !=null){
        	if(loanSource.getId().longValue()!=info.getId().longValue()){
            throw new BossBaseException("显示顺序已存在");
        	}
        }
        if(StringUtils.isBlank(info.getPropaganda())){
            throw new BossBaseException("宣传语1为空");
        }
        if(StringUtils.isBlank(info.getPropaganda2())){
            throw new BossBaseException("宣传语2为空");
        }
        if(StringUtils.isBlank(info.getPropaganda3())){
            throw new BossBaseException("宣传语3为空");
        }
        if(StringUtils.isBlank(info.getProfitType())){
            throw new BossBaseException("奖金方式为空");
        }
        if(StringUtils.isBlank(info.getRewardRequirements())){
            throw new BossBaseException("奖励要求为空");
        }
        if(StringUtils.isBlank(info.getLoanLink())){
            throw new BossBaseException("申请h5链接为空");
        }
        if(StringUtils.isBlank(info.getSettlementCycle())){
            throw new BossBaseException("结算周期为空");
        }
        if(StringUtils.isBlank(info.getSettlementRules())){
            throw new BossBaseException("结算规则为空");
        }
		if(info.getAccessWay()==1){
			if(StringUtils.isBlank(info.getLoanLink())){
				throw new BossBaseException("申请h5链接为空");
			} }
		if(info.getAccessWay()==2) {
			if (StringUtils.isBlank(info.getApiUrl())) {
				throw new BossBaseException("申请API链接为空");
			}
		}
		StringBuffer propaganda=new StringBuffer();
		propaganda.append(info.getPropaganda()).append("&&;").append(info.getPropaganda2()).append("&&;").append(info.getPropaganda3());
		info.setPropaganda(propaganda.toString());
		info.setUpdateTime(new Date());
		return loanInstitutionManagementDao.updateLoan(info);
	}

	@Override
	public int updateLoanStatus(LoanSource info) {
		int num =  loanInstitutionManagementDao.updateLoanStatus(info);
		/*if("off".equals(info.getStatus())){
        	orgSourceConfDao.updateStatusBySourceId("off", String.valueOf(info.getId()));
        }*/
		return num;
	}

}
