package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.dao.AcqOrgDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcpWhitelist;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.service.AcqOrgService;
import cn.eeepay.framework.util.ClientInterface;

@Service("acqOrgService")
@Transactional
public class AcqOrgServiceImpl implements AcqOrgService{
	private static final Logger log = LoggerFactory.getLogger(AcqOrgServiceImpl.class);
	@Resource
	private AcqOrgDao acqOrgDao;
	
	@Resource
	private AcqOrgService acqOrgService;

	@Override
	public AcqOrg selectByPrimaryKey(Integer id) {
		return acqOrgDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(AcqOrg record) {
		return acqOrgDao.updateByPrimaryKey(record);
	}

	@Override
	public List<AcqOrg> selectAllInfo(Page<AcqOrg> page,AcqOrg ao) {
		return acqOrgDao.selectAllInfo(page,ao);
	}

	@Override
	public List<AcqOrg> selectBoxAllInfo(){
		List<AcqOrg> list = acqOrgDao.selectBoxAllInfo();
		return list;
	}
	@Override
	public Object selectAccountDate(){
		Object accountData = null;
		try {
			String response = ClientInterface.findAllBankAccountInfoList();
			accountData = JSON.parse(response);
		} catch (Exception e) {
			log.info("调用账户接口查询分润结算账户失败");
		}
		return accountData;
	}

	@Override
	public int updateStatusByid(AcqOrg record) {
		return acqOrgDao.updateStatusByid(record);
	}

	@Override
	public int deleteByWlid(int id) {
		return acqOrgDao.deleteByWlid(id);
	}

	@Override
	public int insertWl(AcpWhitelist record) {
		return acqOrgDao.insertWl(record);
	}

	@Override
	public List<AcpWhitelist> selectAllWlInfo(int acqId) {
		return acqOrgDao.selectAllWlInfo(acqId);
	}

	@Override
	public AcpWhitelist selectWlInfo(AcpWhitelist record) {
		return acqOrgDao.selectWlInfo(record);
	}

	@Override
	public int addAcqOrg(AcqOrg acqOrg) {
		return acqOrgDao.addAcqOrg(acqOrg);
	}

	@Override
	public int updateChannelStatusByid(AcqOrg acqOrg) {
		return acqOrgDao.updateChannelStatusByid(acqOrg);
	}

	@Override
	public List<AcqOrg> selectAllZqOrg() {
		return acqOrgDao.selectAllZqOrg();
	}

	@Override
	public AcqOrg selectInfoByName(String acqName){
		return acqOrgDao.selectInfoByName(acqName);
	}


}
