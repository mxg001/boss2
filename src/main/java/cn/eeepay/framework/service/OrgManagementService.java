package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.OrgManagement;
import cn.eeepay.framework.model.exchange.PropertyConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 机构service
 */
public interface OrgManagementService {

    List<OrgManagement> selectAllList(OrgManagement org, Page<OrgManagement> page);

    List<OrgManagement> importDetailSelect(OrgManagement org);

    int updateOrgStatus(long id, int state);

    int addOrgManagement(OrgManagement org,List<PropertyConfig> list);

    List<OrgManagement> getOrgManagementList();

    OrgManagement getOrgManagementDetail(long id);

    int updateOrgManagement(OrgManagement org, List<PropertyConfig> list);

    boolean checkOrgName(OrgManagement org);

    OrgManagement getOrgOne(String orgCode);

    void importDetail(List<OrgManagement> list, HttpServletResponse response) throws Exception;
}
