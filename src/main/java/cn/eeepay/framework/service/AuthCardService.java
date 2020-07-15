package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AuthCard;

public interface AuthCardService {

	List<AuthCard> selectAllInfo(Page<AuthCard> page,AuthCard param);
	
	List<AuthCard> authCardExport(AuthCard rr);
	
	
	Map<String,Object> authCardTotal(AuthCard rr);
}
