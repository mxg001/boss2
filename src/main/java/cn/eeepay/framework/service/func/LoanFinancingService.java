package cn.eeepay.framework.service.func;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.func.LoanFinancing;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LoanFinancingService {

    List<LoanFinancing> selectAllList(LoanFinancing info, Page<LoanFinancing> page);

    int addLoanFinancing(LoanFinancing info);

    int updateLoanFinancing(LoanFinancing info);

    int updateLoanFinancingStatus(int id, int status);

    LoanFinancing getLoanFinancing(int id);

    void importDetail(LoanFinancing info, HttpServletResponse response) throws Exception;
}
