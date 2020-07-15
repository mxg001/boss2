package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.ExchangeOemDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.*;
import cn.eeepay.framework.service.ExchangeOemService;
import cn.eeepay.framework.service.OneAgentOemService;
import cn.eeepay.framework.service.PropertyConfigService;
import cn.eeepay.framework.service.RandomNumberService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/13/013.
 * @author  liuks
 * oem service实现类
 */
@Service("exchangeOemService")
public class ExchangeOemServiceImpl  implements ExchangeOemService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeOemServiceImpl.class);

    @Resource
    private ExchangeOemDao exchangeOemDao;

    @Resource
    private PropertyConfigService propertyConfigService;

    @Resource
    private RandomNumberService randomNumberService;

    @Resource
    private OneAgentOemService oneAgentOemService;

    @Override
    public List<ExchangeOem> selectAllList(ExchangeOem oem, Page<ExchangeOem> page) {
        List<ExchangeOem> list= exchangeOemDao.selectAllList(oem,page);
        fillData(page.getResult());
        return list;
    }

    @Override
    public List<ExchangeOem> importDetailSelect(ExchangeOem oem) {
        List<ExchangeOem> list= exchangeOemDao.importDetailSelect(oem);
        fillData(list);
        return  list;
    }

    private void fillData(List<ExchangeOem> list){
        if(list!=null&&list.size()>0){
            for(ExchangeOem item:list){
                AgentOem agentOem=exchangeOemDao.getAgentOem(item.getOemNo(),"1");
                if(agentOem!=null){
                    item.setAgentNo(agentOem.getAgentNo());
                    item.setTeamId(agentOem.getTeamId());
                }
            }
        }
    }
    @Override
    public int addExchangeOem(ExchangeOem oem, List<PropertyConfig> list,AgentOem agentOem) {
        int num=exchangeOemDao.addExchangeOem(oem);
        if(num>0){
            if(list!=null&&list.size()>0){
                for(PropertyConfig pc:list){
                    pc.setConfigCode(oem.getOemNo());
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        //不为空时存值
                        propertyConfigService.addOemConfig(pc);
                    }
                }
            }
            if(agentOem!=null){
                agentOem.setOemNo(oem.getOemNo());
                exchangeOemDao.addAgentOem(agentOem);
                oneAgentOemService.insertOneAgentNo(agentOem.getAgentNo(),"9");
                //开户
                openAccount(oem.getOemNo(),agentOem.getAgentNo());
            }
            //复制直营的
            ExchangeConfig ec=randomNumberService.getExchangeConfig("default_oem_no");
            if(ec!=null&&ec.getSysValue()!=null&&!"".equals(ec.getSysValue())){
                List<ProductOem> proOemList=exchangeOemDao.getProductOemListDefault(ec.getSysValue());
                if(proOemList!=null&&proOemList.size()>0){
                    for(ProductOem old:proOemList){
                        old.setOemNo(oem.getOemNo());
                        exchangeOemDao.addProductOem(old);
                    }
                }
            }

        }
        return num;
    }

    private void openAccount(String oemNo,String agentNo){
        try {
            //代理商开户
            String returnStr =ClientInterface.createAccount("224120","A",agentNo);
            if(StringUtils.isNotBlank(returnStr)){
                JSONObject json = JSONObject.parseObject(returnStr);
                boolean status = json.getBoolean("status");
                if(status){
                    exchangeOemDao.updateAgentOem(oemNo,agentNo);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public ExchangeOem getExchangeOem(long id) {
        return  exchangeOemDao.getExchangeOem(id);
    }

    @Override
    public int updateExchangeOem(ExchangeOem oem, List<PropertyConfig> list, List<PropertyConfig> fileList,AgentOem agentOem) {
        int num=exchangeOemDao.updateExchangeOem(oem);
        if(num>0){
            if(list!=null&&list.size()>0){
                for(PropertyConfig pc:list){
                    PropertyConfig oldpc=propertyConfigService.updateOemConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            pc.setConfigCode(oem.getOemNo());
                            propertyConfigService.addOemConfig(pc);
                        }else{
                            propertyConfigService.updateOemConfig(pc);
                        }
                    }else{
                        if(oldpc!=null){
                            propertyConfigService.deleteOemConfig(pc);
                        }
                    }
                }
            }
            if(fileList!=null&&fileList.size()>0){
                for(PropertyConfig pc:fileList){
                    PropertyConfig oldpc=propertyConfigService.updateOemConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            pc.setConfigCode(oem.getOemNo());
                            propertyConfigService.addOemConfig(pc);
                        }else{
                            propertyConfigService.updateOemConfig(pc);
                        }
                    }
                }
            }
        }
        return num;
    }

    @Override
    public AgentOem getAgentOem(String oemNo, String agentLevel) {
        return exchangeOemDao.getAgentOem(oemNo,agentLevel);
    }

    @Override
    public List<ExchangeOem> getOemList() {
        return exchangeOemDao.getOemList();
    }

    @Override
    public List<ProductOem> selectProductOemList(ProductOem proOem, Page<ProductOem> page) {
        return exchangeOemDao.selectProductOemList(proOem,page);
    }

    @Override
    public int updateProductOemShelve(long id, String state) {
        return exchangeOemDao.updateProductOemShelve(id,state);
    }

    @Override
    public int updateProductOemShelveBatch(String ids,String state, Map<String, Object> msg) {
        if(ids!=null&&!"".equals(ids)){
            String[] strs=ids.split(",");
            if(strs!=null&&strs.length>0){
                int num=0;
                if("1".equals(state)){
                    for(String id:strs){
                        //验证
                        ProductOem old=exchangeOemDao.getProductOem(Long.parseLong(id));
                        if("2".equals(old.getShelve())){
                            num++;
                            exchangeOemDao.updateProductOemShelve(Long.parseLong(id),"1");
                        }
                    }
                    msg.put("status", true);
                    msg.put("msg", "批量上架成功,成功上架"+num+"条!");
                    return 1;
                }else if("2".equals(state)){
                    for(String id:strs){
                        //验证
                        ProductOem old=exchangeOemDao.getProductOem(Long.parseLong(id));
                        if("1".equals(old.getShelve())){
                            num++;
                            exchangeOemDao.updateProductOemShelve(Long.parseLong(id),"2");
                        }
                    }
                    msg.put("status", true);
                    msg.put("msg", "批量下架成功,成功下架"+num+"条!");
                    return 1;
                }

            }
        }
        if("1".equals(state)){
            msg.put("status", false);
            msg.put("msg", "批量上架失败!");
        }else if("2".equals(state)){
            msg.put("status", false);
            msg.put("msg", "批量下架失败!");
        }
        return 0;
    }

    @Override
    public int addProductOem(ProductOem proOem) {
        proOem.setShelve("2");
        return exchangeOemDao.addProductOem(proOem);
    }

    @Override
    public ProductOem getProductOemDetail(long id) {
        return exchangeOemDao.getProductOemDetail(id);
    }

    @Override
    public int updateProductOem(ProductOem proOem) {
        return exchangeOemDao.updateProductOem(proOem);
    }

    @Override
    public boolean checkProductOem(ProductOem proOem) {
        if(proOem.getId()==null){
            List<ProductOem> list=exchangeOemDao.checkProductOemSelect(proOem);
            if(list!=null&&list.size()>0){
                return true;
            }
        }else{
            List<ProductOem> list=exchangeOemDao.checkProductOemSelectId(proOem);
            if(list!=null&&list.size()>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkProductOemShelve(long pId) {
        List<ProductOem> list=exchangeOemDao.checkProductOemShelve(pId);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public int deleteProductOemShelve(long pId) {
        return exchangeOemDao.deleteProductOemShelve(pId);
    }

    @Override
    public ProductOem getProductOemOne(String oemNo, long pId) {
        return exchangeOemDao.getProductOemOne(oemNo,pId);
    }

    @Override
    public void synchronizationProductOem(long id, Map<String, Object> msg) {
        ExchangeOem oem=exchangeOemDao.getExchangeOem(id);
        //复制直营的
        ExchangeConfig ec=randomNumberService.getExchangeConfig("default_oem_no");
        int sum=0;
        if(ec!=null&&ec.getSysValue()!=null&&!"".equals(ec.getSysValue())){
            if(ec.getSysValue().equals(oem.getOemNo())){
                msg.put("status", false);
                msg.put("msg", "直营的oem不能执行同步!");
                return;
            }
            List<ProductOem> proOemList=exchangeOemDao.getProductOemListDefault(ec.getSysValue());
            if(proOemList!=null&&proOemList.size()>0){
                for(ProductOem old:proOemList){
                    old.setOemNo(oem.getOemNo());
                    List<ProductOem> list=exchangeOemDao.checkProductOemSelect(old);
                    if(list==null||list.size()<=0){
                        int num=exchangeOemDao.addProductOem(old);
                        if(num>0){
                            sum=sum+num;
                        }
                    }
                }
            }
        }
        msg.put("status", true);
        msg.put("msg", "同步成功,同步了"+sum+"条记录!");
    }

    @Override
    public boolean checkAgentOem(AgentOem agentOem) {
        AgentOem agent=exchangeOemDao.checkAgentOem(agentOem.getAgentNo(),"1");
        if(agent!=null){
            return true;
        }
        return false;
    }



    @Override
    public void importDetail(List<ExchangeOem> list, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "积分兑换组织列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("oemNo",null);
            maps.put("oemName",null);
            maps.put("publicAccountName",null);
            maps.put("teamId",null);
            maps.put("agentNo",null);
            maps.put("companyNo",null);
            maps.put("companyName",null);
            maps.put("remark",null);
            data.add(maps);
        }else{
//            Map<String, String> agentAccountMap=new HashMap<String, String>();
//            agentAccountMap.put("0","否");
//            agentAccountMap.put("1","是");

            for (ExchangeOem or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()+"");
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("publicAccountName",or.getPublicAccountName()==null?"":or.getPublicAccountName());
                maps.put("teamId",or.getTeamId()==null?"":or.getTeamId());
                maps.put("agentNo",or.getAgentNo()==null?"":or.getAgentNo());
                maps.put("companyNo",or.getCompanyNo()==null?"":or.getCompanyNo());
                maps.put("companyName",or.getCompanyName()==null?"":or.getCompanyName());
                maps.put("remark",or.getRemark()==null?"":or.getRemark());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","oemNo","oemName","publicAccountName","teamId","agentNo",
                "companyNo","companyName","remark"

        };
        String[] colsName = new String[]{"序号","oem编号","组织名称","微信公众号","V2组织编号","V2代理商编号",
                "公司编号","公司名称","备注"

        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出积分兑换组织列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}
