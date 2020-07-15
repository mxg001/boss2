package cn.eeepay.framework.service;

import cn.eeepay.boss.action.ZqMerInfoAction;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantsUpstream;
import cn.eeepay.framework.model.ZqMerchantInfo;
import cn.eeepay.framework.model.ZqMerchantLog;

import java.util.List;

public interface ZqMerchantInfoService {

    ZqMerchantInfo selectZqMerInfoBymbpIDAndChannel(String mbpId, String channelCode);

    int insertZqMerInfo(ZqMerchantInfo zqMerInfo);

    int updateCurrZqMerInfoEffStatus(String mbpId, String channelCode);

    int updateOtherZqMerInfoEffStatus(String  mbpId, String channelCode);

    int updateZqMerInfo(ZqMerchantInfo zqMerInfo);

    List<ZqMerchantInfo> selectAllZqMerInfo(ZqMerInfoAction.ZqMerParams zqMerParams);

    List<ZqMerchantInfo> selectAllZqMerInfoByPage(Page<ZqMerchantInfo> page, ZqMerInfoAction.ZqMerParams zqMerParams);

    List<ZqMerchantLog> selectZqMerLogsByMerAndMbpId(String merchantNo, String bpId);

    ZqMerchantInfo selectByUnionpayMerNo(String unionpayMerNo);

	ZqMerchantInfo selectByRegid(String regid);
	
	List<ZqMerchantInfo> selectYsSyncMer(String type);

    List<MerchantsUpstream> selectAllMerchantsUpstream(Page<MerchantsUpstream> page, List<String> list,int start);

    boolean selectMerchantsUpstreamByUnionpayMerNo(String unionpayMerNo);

    List<MerchantsUpstream> exportDetail(List<String> list,int start);

    ZqMerchantInfo selectByMerNoAndBpId(String  merNo, String  bpId, String channelCode);

}
