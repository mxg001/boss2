package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateOrgDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeConfig;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrg;
import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;
import cn.eeepay.framework.service.RandomNumberService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateOrgService;
import cn.eeepay.framework.service.exchangeActivate.PropertyConfigActivateService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 机构service实现类
 */
@Service("exchangeActivateOrgService")
public class ExchangeActivateOrgServiceImpl implements ExchangeActivateOrgService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateOrgServiceImpl.class);

    @Resource
    private ExchangeActivateOrgDao exchangeActivateOrgDao;

    @Resource
    private PropertyConfigActivateService propertyConfigActivateService;

    @Resource
    private RandomNumberService randomNumberService;

    @Override
    public List<ExchangeActivateOrg> selectAllList(ExchangeActivateOrg org, Page<ExchangeActivateOrg> page) {
        return  exchangeActivateOrgDao.selectAllList(org,page);
    }

    @Override
    public List<ExchangeActivateOrg> importDetailSelect(ExchangeActivateOrg org) {
        return  exchangeActivateOrgDao.importDetailSelect(org);
    }

    @Override
    public int updateOrgStatus(long id, int state) {
        return  exchangeActivateOrgDao.updateOrgStatus(id,state);
    }

    @Override
    public int addOrgManagement(ExchangeActivateOrg org,List<PropertyConfigActivate> list) {
        int num=exchangeActivateOrgDao.addOrgManagement(org);
        if(num>0){
            if(list!=null&&list.size()>0){
                ExchangeConfig ec=randomNumberService.getExchangeConfig("activate_oem_no");
                for(PropertyConfigActivate pc:list){
                    pc.setConfigCode(ec.getSysValue());
                    pc.setBak(org.getOrgCode());
                    pc.setConfigValue(pc.getPropertyDefaultValue());
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        //不为空时存值
                        propertyConfigActivateService.addOrgConfig(pc);
                    }
                }
            }
        }
        return num;
    }

    @Override
    public List<ExchangeActivateOrg> getOrgManagementList() {
        return  exchangeActivateOrgDao.getOrgManagementList();
    }

    @Override
    public ExchangeActivateOrg getOrgManagementDetail(long id) {
        return  exchangeActivateOrgDao.getOrgManagementDetail(id);
    }

    @Override
    public int updateOrgManagement(ExchangeActivateOrg org, List<PropertyConfigActivate> list) {
        int num=0;
        if(org.getOrgLogo()==null||"".equals(org.getOrgLogo())){
            num=exchangeActivateOrgDao.updateOrgManagementNoLogo(org);
        }else{
            num=exchangeActivateOrgDao.updateOrgManagement(org);
        }
        if(num>0){
            if(list!=null&&list.size()>0){
                for(PropertyConfigActivate pc:list){
                    PropertyConfigActivate oldpc=propertyConfigActivateService.updateOrgConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            ExchangeConfig ec=randomNumberService.getExchangeConfig("activate_oem_no");
                            pc.setConfigCode(ec.getSysValue());
                            pc.setBak(org.getOrgCode());
                            propertyConfigActivateService.addOrgConfig(pc);
                        }else{
                            propertyConfigActivateService.updateOrgConfig(pc);
                        }
                    }else{
                        if(oldpc!=null){
                            propertyConfigActivateService.deleteOrgConfig(pc);
                        }
                    }
                }
            }
        }
        return num;
    }

    @Override
    public boolean checkOrgName(ExchangeActivateOrg org) {
        if(org.getId()==null){//新增
            List<ExchangeActivateOrg> oldList=exchangeActivateOrgDao.checkOrgName(org.getOrgName());
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }else{
            List<ExchangeActivateOrg> oldList=exchangeActivateOrgDao.checkOrgNameId(org.getOrgName(),org.getId());
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public ExchangeActivateOrg getOrgOne(String orgCode) {
        List<ExchangeActivateOrg> list=exchangeActivateOrgDao.getOrgManagementOrgCode(orgCode);
        if(list!=null&&list.size()>0){
            return  list.get(0);
        }
        return null;
    }

    @Override
    public void importDetail(List<ExchangeActivateOrg> list, HttpServletResponse response) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "积分机构列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("orgCode",null);
            maps.put("orgName",null);
            maps.put("orgStatus",null);
            maps.put("sort",null);
            maps.put("finance",null);
            maps.put("remark",null);
            data.add(maps);
        }else{
            Map<String, String> orgStatusMap=new HashMap<String, String>();
            orgStatusMap.put("0","关闭");
            orgStatusMap.put("1","开启");

            Map<String, String> financeMap=new HashMap<String, String>();
            financeMap.put("0","否");
            financeMap.put("1","是");

            for (ExchangeActivateOrg or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()+"");
                maps.put("orgCode",or.getOrgCode()==null?"":or.getOrgCode());
                maps.put("orgName",or.getOrgName()==null?"":or.getOrgName());
                maps.put("orgStatus",orgStatusMap.get(or.getOrgStatus()));
                maps.put("sort",or.getSort()==null?"":or.getSort().toString());
                maps.put("finance",financeMap.get(or.getFinance()));
                maps.put("remark",or.getRemark()==null?"":or.getRemark());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","orgCode","orgName","orgStatus","sort","finance",
                "remark"
        };
        String[] colsName = new String[]{"序号","编码","机构名称","状态","顺序","是否具备金融属性",
                "积分查询方式"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出积分机构列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}
