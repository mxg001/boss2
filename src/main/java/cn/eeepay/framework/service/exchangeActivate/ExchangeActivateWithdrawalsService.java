package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateWithdrawals;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18/018.
 * @author  liuks
 * 用户提现service
 */
public interface ExchangeActivateWithdrawalsService {

    List<ExchangeActivateWithdrawals> selectAllList(ExchangeActivateWithdrawals wi, Page<ExchangeActivateWithdrawals> page);

    List<ExchangeActivateWithdrawals> importDetailSelect(ExchangeActivateWithdrawals wi);

    void importDetail(List<ExchangeActivateWithdrawals> list, HttpServletResponse response) throws Exception;

    TotalAmount selectSum(ExchangeActivateWithdrawals wi, Page<ExchangeActivateWithdrawals> page);
}
