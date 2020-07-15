package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CloudPayDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CloudPayInfo;
import cn.eeepay.framework.service.CloudPayService;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("cloudPayService")
@Transactional
public class CloudPayServiceImpl implements CloudPayService {

	@Resource
	private CloudPayDao cloudPayDao;

	/**
	 * 商户收益查询
	 * @author mays
	 * @date 2017年10月23日
	 */
	@Override
	public List<CloudPayInfo> selectCloudPayByParam(Page<CloudPayInfo> page, CloudPayInfo info) {
		return cloudPayDao.selectCloudPayByParam(page, info);
	}

	@Override
	public String selectMerchantProfitCount(CloudPayInfo info) {
		String  merchantProfitCount=cloudPayDao.selectMerchantProfitCount(info);
		return StringUtil.isEmpty(merchantProfitCount)?"0":merchantProfitCount;
	}

	@Override
	public List<CloudPayInfo> importCloudPayByParam(CloudPayInfo info) {
		return cloudPayDao.importCloudPayByParam(info);
	}

}
