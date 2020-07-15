package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.SecretKey;

public interface SecretKeyService {

	  	public int insertAll(List<SecretKey> sk);

		public Map findSecretTsKey(String factoryCode);
}
