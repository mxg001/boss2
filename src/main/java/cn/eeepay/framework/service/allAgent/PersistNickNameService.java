package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.PersistNickName;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PersistNickNameService {

    List<PersistNickName> selectPersistNickNameList(PersistNickName info, Page<PersistNickName> page);

    int insertPersistNickName(PersistNickName info);

    int insertPersistNickNameAll(PersistNickName info);

    PersistNickName selectPersistNickNameById(Integer id);

    int updatePersistNickName(PersistNickName info);

    int deletePersistNickName(Integer id);

    void exportPersistNickName(PersistNickName info, HttpServletResponse response)  throws Exception;
}
