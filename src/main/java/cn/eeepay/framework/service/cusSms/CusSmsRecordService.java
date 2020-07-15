package cn.eeepay.framework.service.cusSms;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cusSms.CusSmsRecord;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CusSmsRecordService {

    List<CusSmsRecord> selectAllList(CusSmsRecord info, Page<CusSmsRecord> page);

    CusSmsRecord getCusSmsRecordInfo(int id, int sta);

    void exportInfo(CusSmsRecord info, HttpServletResponse response) throws Exception;

    int insertCusSmsRecordList(List<CusSmsRecord> list);
}
