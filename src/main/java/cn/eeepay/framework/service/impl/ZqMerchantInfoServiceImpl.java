package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.action.ZqMerInfoAction;
import cn.eeepay.framework.dao.ZqMerchantInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantsUpstream;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.ZqMerchantInfo;
import cn.eeepay.framework.model.ZqMerchantLog;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.ZqMerchantInfoService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/19.
 */
@Service("zqMerchantInfoService")
@Transactional
public class ZqMerchantInfoServiceImpl implements ZqMerchantInfoService {

    @Resource
    private ZqMerchantInfoDao zqMerInfoDao;
    @Resource
    private SysDictService sysDictService;

    private final Logger log = LoggerFactory.getLogger(ZqMerchantInfoServiceImpl.class);

    @Override
    @Transactional(readOnly = true , propagation = Propagation.NOT_SUPPORTED)
    public ZqMerchantInfo selectZqMerInfoBymbpIDAndChannel(String mbpId, String channelCode) {
        return zqMerInfoDao.selectZqMerInfoBymbpIDAndChannel(mbpId, channelCode);
    }

    @Override
    public int insertZqMerInfo(ZqMerchantInfo zqMerInfo) {
        return zqMerInfoDao.insertZqMerInfo(zqMerInfo);
    }

    @Override
    public int updateCurrZqMerInfoEffStatus(String mbpId, String channelCode) {
        return zqMerInfoDao.updateCurrZqMerInfoEffStatus(mbpId, channelCode);
    }

    @Override
    public int updateOtherZqMerInfoEffStatus(String mbpId, String channelCode) {
        return zqMerInfoDao.updateOtherZqMerInfoEffStatus(mbpId, channelCode);
    }

    @Override
    public int updateZqMerInfo(ZqMerchantInfo zqMerInfo) {
        return zqMerInfoDao.updateZqMerInfo(zqMerInfo);
    }

    @Override
    public List<ZqMerchantInfo> selectAllZqMerInfo(ZqMerInfoAction.ZqMerParams zqMerParams) {
        List<ZqMerchantInfo> reList=zqMerInfoDao.selectAllZqMerInfo(zqMerParams);
        if(reList!=null&&reList.size()>0){
            for(ZqMerchantInfo item:reList){
                if(item!=null){
                    item.setMiMobilephone(StringUtil.sensitiveInformationHandle(item.getMiMobilephone(),0));
                }
            }
        }
        return reList;
    }

    @Override
    public List<ZqMerchantInfo> selectAllZqMerInfoByPage(Page<ZqMerchantInfo> page, ZqMerInfoAction.ZqMerParams zqMerParams) {
        List<ZqMerchantInfo> list=zqMerInfoDao.selectAllZqMerInfoByPage(page, zqMerParams);
        if(page.getResult()!=null&&page.getResult().size()>0){
            for(ZqMerchantInfo item:list){
                if(item!=null){
                    item.setMiMobilephone(StringUtil.sensitiveInformationHandle(item.getMiMobilephone(),0));
                }
            }
        }
        return list;
    }

    @Override
    public List<ZqMerchantLog> selectZqMerLogsByMerAndMbpId(String merchantNo, String bpId) {
    	List<ZqMerchantLog> list = zqMerInfoDao.selectZqMerLogsByMerAndMbpId(merchantNo, bpId);
    	List<ZqMerchantLog> wfzq = zqMerInfoDao.selectZqMerLogsInWFZQ(merchantNo);
    	wfzq.addAll(list);
        return wfzq;
    }

    @Override
    public ZqMerchantInfo selectByUnionpayMerNo(String unionpayMerNo) {
        return zqMerInfoDao.selectByUnionpayMerNo(unionpayMerNo);
    }
    
    @Override
    public ZqMerchantInfo selectByRegid(String regid) {
        return zqMerInfoDao.selectByRegid(regid);
    }

	@Override
	public List<ZqMerchantInfo> selectYsSyncMer(String type) {
		return zqMerInfoDao.selectYsSyncMer(type);
	}

    @Override
    public List<MerchantsUpstream> selectAllMerchantsUpstream(Page<MerchantsUpstream> page, List<String> list,int start) {
        return zqMerInfoDao.selectAllMerchantsUpstream(page, list,start);
    }

    @Override
    public boolean selectMerchantsUpstreamByUnionpayMerNo(String unionpayMerNo) {
        List<MerchantsUpstream> list= zqMerInfoDao.selectMerchantsUpstreamByUnionpayMerNo(unionpayMerNo);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public List<MerchantsUpstream> exportDetail(List<String> list, int start) {
        List<MerchantsUpstream> reList=zqMerInfoDao.exportDetail(list,start);
        return reList;
    }

    @Override
    public ZqMerchantInfo selectByMerNoAndBpId(String  merNo, String  bpId, String channelCode) {
        return zqMerInfoDao.selectByMerNoAndBpId(merNo,bpId, channelCode);
    }

}
