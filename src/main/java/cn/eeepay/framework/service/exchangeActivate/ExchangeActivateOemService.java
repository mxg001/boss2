package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.AgentOemActivate;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOem;
import cn.eeepay.framework.model.exchangeActivate.ProductActivateOem;
import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/13/013.
 * @author  liuks
 * oem service
 */
public interface ExchangeActivateOemService {

    List<ExchangeActivateOem> selectAllList(ExchangeActivateOem oem, Page<ExchangeActivateOem> page);

    List<ExchangeActivateOem> importDetailSelect(ExchangeActivateOem oem);

    int addExchangeOem(ExchangeActivateOem oem, List<PropertyConfigActivate> list, AgentOemActivate agentOem);

    ExchangeActivateOem getExchangeOem(long id);

    int updateExchangeOem(ExchangeActivateOem oem, List<PropertyConfigActivate> list, List<PropertyConfigActivate> fileList, AgentOemActivate agentOem);

    AgentOemActivate getAgentOem(String oemNo, String agentLevel);

    List<ExchangeActivateOem> getOemList();

    List<ProductActivateOem> selectProductOemList(ProductActivateOem proOem, Page<ProductActivateOem> page);

    int updateProductOemShelve(long id, String state);

    int updateProductOemShelveBatch(String ids,String state,Map<String, Object> msg);

    int addProductOem(ProductActivateOem proOem);

    ProductActivateOem getProductOemDetail(long id);

    int updateProductOem(ProductActivateOem proOem);

    boolean checkProductOem(ProductActivateOem proOem);

    boolean checkProductOemShelve(long pId);

    int deleteProductOemShelve(long pId);

    ProductActivateOem getProductOemOne(String oemNo, long pId);

    void synchronizationProductOem(long id, Map<String, Object> msg);

    boolean checkAgentOem(AgentOemActivate agentOem);

    int openUpOem(long id, Map<String, Object> msg);

    void importDetail(List<ExchangeActivateOem> list, HttpServletResponse response) throws Exception;
}
