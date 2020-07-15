package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.BusinessProductInfo;

public interface BusinessProductInfoService {

	List<BusinessProductInfo> selectInfoByBpId(String bpId);

	List<BusinessProductInfo> getByBpId(String bpId);
}
