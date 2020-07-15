package cn.eeepay.framework.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.InvitePrizesConfig;
import cn.eeepay.framework.model.InvitePrizesMerchantInfo;

/**
 * 邀请有奖
 * @author tans
 * @date 2017年8月19日 上午10:59:46
 */
public interface InvitePrizesService {

	Map<String, Object> getInfo();

	List<InvitePrizesConfig> getAgentListByParam(InvitePrizesConfig baseInfo, Page<InvitePrizesConfig> page);

	Map<String, Object> updateInfo(Map<String, String> baseInfo);

	Map<String, Object> updateAgentActivityDate(InvitePrizesConfig baseInfo);

	Map<String, Object> deleteBatch(List<String> agentNoList);

	Map<String, Object> insertAgent(InvitePrizesConfig baseInfo);

	AgentInfo getAgent(String agentNo, Integer agentLevel);

	/**
	 * 邀请有奖查询
	 */
	List<InvitePrizesMerchantInfo> selectInvitePrizesByParam(Page<InvitePrizesMerchantInfo> page, InvitePrizesMerchantInfo info);

	/**
	 * 邀请有奖导出
	 * mys,20171017
	 */
	List<InvitePrizesMerchantInfo> exportInvitePrizesByParam(InvitePrizesMerchantInfo info);

	Map<String, Object> insertBatchAgent(MultipartFile file, Map<String, Object> msg) throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException;

	Map<String, String> countInvitePrizesMerchant(InvitePrizesMerchantInfo info);

	InvitePrizesMerchantInfo queryInvitePrizesMerchantInfo(int id);

	Map<String, Object> updateRecordAccount(Integer id, Map<String, Object> msg);

}
