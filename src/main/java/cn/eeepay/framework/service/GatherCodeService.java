package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.GatherCode;

public interface GatherCodeService {

	List<GatherCode> selectByParams(Page<GatherCode> page, GatherCode gatherCode);

	GatherCode gatherCodeDetail(String id);

	int insertBatch(GatherCode info);

	List<GatherCode> exportGatherCode(GatherCode info);

	String gatherCodeUrl(String id);

	int updateGatherStatus(String id, int i);

}
