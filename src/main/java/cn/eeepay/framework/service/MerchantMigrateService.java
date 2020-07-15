package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.MerchantMigrate;
import cn.eeepay.framework.model.MerchantMigrateInfo;

/**
 * @see 商户迁移，所有商户迁移的接口都将出现在这里
 * @author ws
 * @date 2017年3月4日12:34:38
 *
 */
public interface MerchantMigrateService {
	
	
	/**
	 * 商户迁移转移一级代理商
	 * @return
	 */
	void migrate(List<String> merchantNos);
	
	/**
	 * 获取商户业务产品
	 * @param merchantNo 商户编号
	 * @return 业务产品编号集合
	 */
	List<String> findMerchantBusProduct(String merchantNo);
	
	/**
	 * 
	 * @param merchantMigrateId
	 * @return
	 */
	List<MerchantMigrateInfo> getMerchantMigrateInfoAllList(String merchantMigrateId);
	/**
	 * 撤销商户迁移
	 * @param mmId 商户迁移ID
	 * @return 受影响的行数
	 */
	int cheXiaoMigrate(String mmId);
	
	/**
	 * 商户迁移审核
	 * @param check_status 状态
	 * @param check_remark 审核意见
	 * @param check_person 审核人
	 * @param id 审核ID编号
	 * @return 受影响的行数
	 */
	int merchantMigrateCheck(String check_status, String check_remark, String check_person,String id);
	
	/**
	 * 根据商户迁移主表ID 获取商户迁移子表记录
	 * @return 商户迁移子信息集合
	 */
	List<MerchantMigrateInfo> getMerchantMigrateInfoList(String merchantMigrateId, Page<MerchantMigrateInfo> page);
	
	/**
	 * 获取商户迁移主信息详情
	 * @param id 商户迁移主表ID
	 * @return
	 */
	MerchantMigrate getMerchantMigrateDetail(String id);
	
	/**
	 * 获取商户迁移子信息
	 * @param merchantNo
	 * @return
	 */
	MerchantMigrateInfo findMerchantMigrateInfo(String merchantNo);
	
	/**
	 * 获取代理商所代理商的产品
	 * @param agentNo 代理商编号
	 * @return String 集合
	 */
	List<String> findAgentBusProduct(String agentNo);
	
	
	/**
	 * 获取商户信息
	 * @param merchantNo 商户编号
	 * @return 商户信息
	 */
	MerchantInfo getMerchant(String merchantNo);
	
	/**
	 * 查询所有商户迁移信息
	 * @param param 多条件查询参数
	 * @return 商户迁移集合
	 */
	List<MerchantMigrate> findMerchantMigrate(Page<MerchantMigrate> page, MerchantMigrate merchantMigrate);
	
	/**
	 * 新增商户迁移记录
	 * @param param 商户信息
	 * @param merchantMigrate 商户迁移信息
	 * @param agentParentNode 所属代理商节点
	 * @return 受影响的行数
	 */
	int addMerchantMigrate(List<MerchantMigrateInfo> param, MerchantMigrate merchantMigrate, String agentParentNode);
	
	/**
	 * 修改商户迁移信息
	 * @param param 修改参数
	 * @return 受影响的行数
	 */
	int modifyMerchantMigrate(Map<String, String> param);
	
	/**
	 * 根据代理商编号获取代理商信息
	 * @param agentNo 代理商编号
	 * @return
	 */
	AgentInfo getAgentInfoByNo(String agentNo);
	
	/**
	 * @see 一级代理商
	 * @return 代理商集合
	 */
	List<AgentInfo> getAgentInfo();
	
	/**
	 * @see 加载一级代理商下子代理商
	 * @param agentNo 一级代理商编号
	 * @return Map集合
	 */
	List<AgentInfo> findNodeAgent(String agentNo);

	void scBymerchantNos(List<String> merchantNos);

	/***
	 * 商户迁移 迁移交易数据
	 * @param merchantNos 商户编号
	 * @param agentNode 目标直属代理商
	 */
    void changeCollectiveTransOrder(String merchantNos, String agentNode)throws Exception;
}
