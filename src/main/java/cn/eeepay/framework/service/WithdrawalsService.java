package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.Withdrawals;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18/018.
 * @author  liuks
 * 用户提现service
 */
public interface WithdrawalsService {

    List<Withdrawals> selectAllList(Withdrawals wi, Page<Withdrawals> page);

    List<Withdrawals> importDetailSelect(Withdrawals wi);

    void importDetail(List<Withdrawals> list, HttpServletResponse response) throws Exception;

    TotalAmount selectSum(Withdrawals wi, Page<Withdrawals> page);
}
