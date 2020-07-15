package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.NoticeInfo;

public interface NoticeInfoService {

	int insert(Map<String, Object> data);

	Map<String, Object> selectById(String id);

	int update(Map<String, Object> data);

	List<NoticeInfo> selectByParam( NoticeInfo notice, Page<NoticeInfo> page);

	int deliverNotice(long id);

	Map<String, Object> selectInfoById(String id);

	Map<String, Object> selectLinkInfo();

	int updateRecoverNotice(Integer id);

	int deleteRecoverNotice(Integer id);

	int clearStrongNotice(String sysType);

	int strongNotice(Integer id ,Integer strong);


}
