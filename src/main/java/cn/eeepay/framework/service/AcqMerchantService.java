package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqMerchant;
import cn.eeepay.framework.model.AcqTerminal;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface AcqMerchantService {

    public int insert(AcqMerchant record,List<String> list, AcqMerchant acqMerchant);

    public int updateByPrimaryKey(AcqMerchant record);
    
    AcqMerchant selectByPrimaryKey(Integer id);
    
    List<AcqMerchant> selectAllInfo(Page<AcqMerchant> page,AcqMerchant amc);
    
    int updateStatusByid(AcqMerchant record);
    
    AcqMerchant selectInfoByAcqmerNo(AcqMerchant record);

	public AcqMerchant selectInfoByMerNo(String merchantNo);
	
	int selectOrgMerExistByMerNo(String acqMerchantNo);
	
	int insertInfo(AcqMerchant record,AcqTerminal aa);

    List<AcqMerchant> importDetailSelect(AcqMerchant amc);

    void importDetail(List<AcqMerchant> list, HttpServletResponse response) throws Exception;

    Map<String,Object> acqMerBatchColseimport(MultipartFile file) throws Exception;

    AcqMerchant selectByMerchantNo(String merchantNo);
}
