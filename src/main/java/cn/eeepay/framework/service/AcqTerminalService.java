package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqTerminal;

public interface AcqTerminalService {

	int deleteByPrimaryKey(Integer id);

    int insert(AcqTerminal record);

    AcqTerminal selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AcqTerminal record);
    
    List<AcqTerminal> selectAllInfo(Page<AcqTerminal> page,AcqTerminal act);
    
    int updateStatusByid(AcqTerminal record);
    
    AcqTerminal selectTerminalByTerNo(AcqTerminal record);
    
    int insertInfo(AcqTerminal record);
    
    int updateInfo(AcqTerminal record);
}
