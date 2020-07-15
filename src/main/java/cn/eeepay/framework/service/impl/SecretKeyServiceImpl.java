package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.SecretKeyDao;
import cn.eeepay.framework.model.SecretKey;
import cn.eeepay.framework.service.SecretKeyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("SecretKeyService")
@Transactional
public class SecretKeyServiceImpl implements SecretKeyService{

	@Resource
	public  SecretKeyDao secretKeyDao;
	
	@Override
	public int insertAll(List<SecretKey> sks) {
		int i=0;
		for (SecretKey sk : sks) {
			secretKeyDao.insertSelective(sk);
		}
		return i;
	}
	@Override
	public Map findSecretTsKey(String factoryCode){
		return secretKeyDao.findSecretTsKey(factoryCode);
	}
}
