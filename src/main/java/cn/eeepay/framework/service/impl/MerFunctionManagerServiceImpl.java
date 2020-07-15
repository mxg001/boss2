package cn.eeepay.framework.service.impl;


import cn.eeepay.framework.dao.MerFunctionManagerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.FunctionMerchant;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.service.MerFunctionManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("merFunctionManagerService")
@Transactional
public class MerFunctionManagerServiceImpl implements MerFunctionManagerService {

	@Resource
	private MerFunctionManagerDao merFunctionManagerDao;

	@Override
	public void selectByParam(FunctionMerchant functionMerchant,
			Page<FunctionMerchant> page) {

		merFunctionManagerDao.selectByParam(functionMerchant, page);
		List<FunctionMerchant> resList=page.getResult();
		if(resList!=null&&!resList.isEmpty()){
			for(FunctionMerchant fm:resList){
				if(fm.getTeamEntryName()!=null){
					String newTeamName=fm.getTeamName()+"-"+fm.getTeamEntryName();
					fm.setTeamName(newTeamName);
				}
			}
		}
	}

	@Override
	public List<FunctionMerchant> exportConfig(FunctionMerchant functionMerchant) {
		return merFunctionManagerDao.exportConfig(functionMerchant);
	}

	@Override
	public int addFunctionMerchant(FunctionMerchant functionMerchant){
		return merFunctionManagerDao.addFunctionMerchant(functionMerchant);
	}


	@Override
	public MerchantInfo findMerInfoByMerNo(String merNo) {
		return merFunctionManagerDao.findMerInfoByMerNo(merNo);
	}

    @Override
    public int selectExists(FunctionMerchant functionMerchant) {
        return merFunctionManagerDao.selectExists(functionMerchant);
    }

	@Override
	public FunctionMerchant get(Integer id) {
		return merFunctionManagerDao.selectById(id);
	}

	@Override
	public int delete(Integer id) {
		return merFunctionManagerDao.delete(id);
	}


	@Override
	public void deleteInfo(String merchantNo, String functionNumber) {
		merFunctionManagerDao.deleteInfo(merchantNo,functionNumber);
	}


}
