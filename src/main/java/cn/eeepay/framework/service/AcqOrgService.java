package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcpWhitelist;
import cn.eeepay.framework.model.AcqOrg;

public interface AcqOrgService {
	
    AcqOrg selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AcqOrg record);
    
    //收单机构白名单删除
    int deleteByWlid(int id);
    
    //收单机构白名单新增
    int insertWl(AcpWhitelist record);
    
    //收单机构白名单查询
    List<AcpWhitelist> selectAllWlInfo(int acqId);
    
    //查询收单机构白名单是否存在
    AcpWhitelist selectWlInfo(AcpWhitelist record);
    
    List<AcqOrg> selectAllInfo(Page<AcqOrg> page,AcqOrg ao);
    
    //下拉框
    List<AcqOrg> selectBoxAllInfo();
    
    Object selectAccountDate();
    
    int updateStatusByid(AcqOrg record);

	int addAcqOrg(AcqOrg acqOrg);

	int updateChannelStatusByid(AcqOrg acqOrg);

    //获取所有直清收单机构
    List<AcqOrg> selectAllZqOrg();

    //通过收单机构名称获取id
    AcqOrg selectInfoByName(String acqName);
}
