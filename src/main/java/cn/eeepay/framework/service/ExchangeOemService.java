package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/13/013.
 * @author  liuks
 * oem service
 */
public interface ExchangeOemService {

    List<ExchangeOem> selectAllList(ExchangeOem oem, Page<ExchangeOem> page);

    List<ExchangeOem> importDetailSelect(ExchangeOem oem);

    int addExchangeOem(ExchangeOem oem, List<PropertyConfig> list,AgentOem agentOem);

    ExchangeOem getExchangeOem(long id);

    int updateExchangeOem(ExchangeOem oem, List<PropertyConfig> list, List<PropertyConfig> fileList,AgentOem agentOem);

    AgentOem getAgentOem(String oemNo, String agentLevel);

    List<ExchangeOem> getOemList();

    List<ProductOem> selectProductOemList(ProductOem proOem, Page<ProductOem> page);

    int updateProductOemShelve(long id, String state);

    int updateProductOemShelveBatch(String ids, String state,Map<String, Object> msg);

    int addProductOem(ProductOem proOem);

    ProductOem getProductOemDetail(long id);

    int updateProductOem(ProductOem proOem);

    boolean checkProductOem(ProductOem proOem);

    boolean checkProductOemShelve(long pId);

    int deleteProductOemShelve(long pId);

    ProductOem getProductOemOne(String oemNo,long pId);

    void synchronizationProductOem(long id, Map<String, Object> msg);

    boolean checkAgentOem(AgentOem agentOem);


    void importDetail(List<ExchangeOem> list, HttpServletResponse response) throws Exception;
}
