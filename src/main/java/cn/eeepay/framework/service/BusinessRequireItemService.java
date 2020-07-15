package cn.eeepay.framework.service;

import java.util.List;


public interface BusinessRequireItemService {

	List<String> findByProduct(String bpId);
}
