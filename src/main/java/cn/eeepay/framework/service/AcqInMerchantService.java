package cn.eeepay.framework.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqInMerchant;
import cn.eeepay.framework.model.AcqInMerchantFileInfo;
import cn.eeepay.framework.model.AcqInMerchantRecord;

public interface AcqInMerchantService {


    AcqInMerchant selectByPrimaryKey(Long id);
    
    List<AcqInMerchant> selectAllInfo(Page<AcqInMerchant> page,AcqInMerchant amc);
    
	//更新mcc码
	public int updateMcc(String id, String mcc);

	AcqInMerchant queryStatusByAcqId(long id);
	/**
	 * 查询导出结果
	 * @param am
	 * @return
	 */
	List<AcqInMerchant> exportInfo(AcqInMerchant am);

	void exportAcqInMerchant(List<AcqInMerchant> list, HttpServletResponse response) throws Exception;

	List<AcqInMerchantRecord> selectRecordByPrimaryKey(Long id);

	JSONObject  audit(long id, int val,List<String> notAllowIds,String examinationOpinions);

	List<AcqInMerchantFileInfo> findByAcqIntoNo(String acqIntoNo);

    AcqInMerchant selectByMerchantNo(String merNo);

    int updateMerchantNo(String merchantNo);
}
