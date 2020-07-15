package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOem;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrg;
import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 机构service
 */
public interface ExchangeActivateOrgService {

    List<ExchangeActivateOrg> selectAllList(ExchangeActivateOrg org, Page<ExchangeActivateOrg> page);

    List<ExchangeActivateOrg> importDetailSelect(ExchangeActivateOrg org);

    int updateOrgStatus(long id, int state);

    int addOrgManagement(ExchangeActivateOrg org, List<PropertyConfigActivate> list);

    List<ExchangeActivateOrg> getOrgManagementList();

    ExchangeActivateOrg getOrgManagementDetail(long id);

    int updateOrgManagement(ExchangeActivateOrg org, List<PropertyConfigActivate> list);

    boolean checkOrgName(ExchangeActivateOrg org);

    ExchangeActivateOrg getOrgOne(String orgCode);

    void importDetail(List<ExchangeActivateOrg> list, HttpServletResponse response) throws Exception;
}
