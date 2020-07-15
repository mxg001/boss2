package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

import java.util.List;

/**
 * 超级银行家-红包领地
 * @author zxs
 * @date 2018-6-19
 */
public interface ManorService {

	 List<ManorMoney> selectManorMoneyPage(ManorMoney baseInfo, Page<ManorMoney> page);

	ManorMainSum selectManorMoneySum(ManorMoney baseInfo);

	List<ManorMoney> selectManorMoneyDetlPage(ManorMoney baseInfo, Page<ManorMoney> page);

	List<RedTerritoryBonusEveryday> selectDailyDividendPage(RedTerritoryBonusEveryday baseInfo, Page<RedTerritoryBonusEveryday> page);

	ManorMainSum selectOrderSum( RedTerritoryBonusEveryday baseInfo);

	List<RedTerritoryBonusDetail> selectDailyBusinessPage(RedTerritoryBonusEveryday baseInfo, Page<RedTerritoryBonusDetail> page);

	List<ManorTransactionRecore> selectManorTransactionRecorePage(ManorTransactionRecore baseInfo, Page<ManorTransactionRecore> page);

	ManorTransactionRecore selectManorTransactionRecorePageSum(ManorTransactionRecore baseInfo);

	ManorTransactionRecore selectManorTransactionRecorePageById(String id);

	List<ManorAdjustRecore> selectManorAdjustRecorePage(ManorAdjustRecore baseInfo, Page<ManorAdjustRecore> page);

	List<ManorQuery> selectManorQueryPage(ManorQuery baseInfo, Page<ManorQuery> page);

	ManorManager selectManorManager();

	void insertManorManager(ManorManager info);

	void updateManorManager(ManorManager info);
	
}
