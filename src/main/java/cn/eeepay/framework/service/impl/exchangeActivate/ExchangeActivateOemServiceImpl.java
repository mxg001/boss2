package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateOemDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.exchange.ExchangeConfig;
import cn.eeepay.framework.model.exchangeActivate.*;
import cn.eeepay.framework.service.OneAgentOemService;
import cn.eeepay.framework.service.RandomNumberService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateOemService;
import cn.eeepay.framework.service.exchangeActivate.PropertyConfigActivateService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/13/013.
 * @author  liuks
 * oem service实现类
 */
@Service("exchangeActivateOemService")
public class ExchangeActivateOemServiceImpl implements ExchangeActivateOemService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateOemServiceImpl.class);

    @Resource
    private ExchangeActivateOemDao exchangeActivateOemDao;

    @Resource
    private PropertyConfigActivateService propertyConfigActivateService;

    @Resource
    private RandomNumberService randomNumberService;

    @Resource
    private OneAgentOemService oneAgentOemService;

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<ExchangeActivateOem> selectAllList(ExchangeActivateOem oem, Page<ExchangeActivateOem> page) {
        List<ExchangeActivateOem> list= exchangeActivateOemDao.selectAllList(oem,page);
        fillData(page.getResult());
        return list;
    }

    @Override
    public List<ExchangeActivateOem> importDetailSelect(ExchangeActivateOem oem) {
        List<ExchangeActivateOem> list= exchangeActivateOemDao.importDetailSelect(oem);
        fillData(list);
        return list;
    }

    private void fillData(List<ExchangeActivateOem> list){
        if(list!=null&&list.size()>0){
            for(ExchangeActivateOem item:list){
                AgentOemActivate agentOem=exchangeActivateOemDao.getAgentOem(item.getOemNo(),"1");
                if(agentOem!=null){
                    item.setAgentNo(agentOem.getAgentNo());
                    item.setTeamId(agentOem.getTeamId());
                    item.setAgentAccount(agentOem.getAgentAccount());
                }
            }
        }
    }


    @Override
    public int addExchangeOem(ExchangeActivateOem oem, List<PropertyConfigActivate> list, AgentOemActivate agentOem) {
        int num=exchangeActivateOemDao.addExchangeOem(oem);
        if(num>0){
            String agentStr=null;
            String feeStr=null;
            String receiveStr=null;
            String repaymentStr=null;
            if(list!=null&&list.size()>0){
                for(PropertyConfigActivate pc:list){
                    pc.setConfigCode(oem.getOemNo());
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        //不为空时存值
                        propertyConfigActivateService.addOemConfig(pc);
                    }
                    if("one_agent_share".equals(pc.getPropertyCode())){
                        agentStr=pc.getConfigValue();
                    }
                    if("oem_fee".equals(pc.getPropertyCode())){
                        feeStr=pc.getConfigValue();
                    }
                    if("rec_agent1_share".equals(pc.getPropertyCode())){
                        receiveStr=pc.getConfigValue();
                    }
                    if("rep_agent1_share".equals(pc.getPropertyCode())){
                        repaymentStr=pc.getConfigValue();
                    }
                }
            }
            if(agentOem!=null){
                agentOem.setOemNo(oem.getOemNo());
                exchangeActivateOemDao.addAgentOem(agentOem);
                oneAgentOemService.insertOneAgentNo(agentOem.getAgentNo(),"10");
                if(agentStr!=null&&feeStr!=null&&receiveStr!=null&&repaymentStr!=null){
                    BigDecimal agentShare=new BigDecimal(agentStr);
                    BigDecimal fee=new BigDecimal(feeStr);
                    BigDecimal receiveShare=new BigDecimal(receiveStr);
                    BigDecimal repaymentShare=new BigDecimal(repaymentStr);
                    oneAgentOemService.insertOneAgentShare(agentOem.getAgentNo(),agentShare,fee,receiveShare,repaymentShare);
                    insertOneAgentShare(agentOem.getAgentNo(),agentShare,fee,receiveShare,repaymentShare);
                }
                //开户
                openAccount(oem.getOemNo(),agentOem.getAgentNo());

                //开通超级还/商户收款组织
                Map<String, Object> msg=new HashMap<String,Object>();
                updateRepayOem(oem,list,agentOem,msg);
            }
            //复制直营的
            ExchangeConfig ec=randomNumberService.getExchangeConfig("activate_oem_no");
            if(ec!=null&&ec.getSysValue()!=null&&!"".equals(ec.getSysValue())){
                List<ProductActivateOem> proOemList=exchangeActivateOemDao.getProductOemListDefault(ec.getSysValue());
                if(proOemList!=null&&proOemList.size()>0){
                    for(ProductActivateOem old:proOemList){
                        old.setOemNo(oem.getOemNo());
                        exchangeActivateOemDao.addProductOem(old);
                    }
                }
            }

        }
        return num;
    }

    public int insertOneAgentShare(String agentNo, BigDecimal share, BigDecimal fee,BigDecimal receiveShare,BigDecimal repaymentShare) {
        int num=0;
        List<Map<Object,Object>> list=exchangeActivateOemDao.checkAgent(agentNo);
        if(list==null||list.size()==0){
            num=exchangeActivateOemDao.insertOneAgentShare(agentNo,share,fee,receiveShare,repaymentShare);
        }else{
            num=exchangeActivateOemDao.updateOneAgentShare(agentNo,share,fee,receiveShare,repaymentShare);
        }
        return num;
    }

    private void openAccount(String oemNo,String agentNo){
        try {
            //代理商开户
            String returnStr =ClientInterface.createAccount("224123","A",agentNo);
            if(StringUtils.isNotBlank(returnStr)){
                JSONObject json = JSONObject.parseObject(returnStr);
                boolean status = json.getBoolean("status");
                if(status){
                    exchangeActivateOemDao.updateAgentOem(oemNo,agentNo);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public ExchangeActivateOem getExchangeOem(long id) {
        return  exchangeActivateOemDao.getExchangeOem(id);
    }

    @Override
    public int updateExchangeOem(ExchangeActivateOem oem, List<PropertyConfigActivate> list, List<PropertyConfigActivate> fileList,AgentOemActivate agentOem) {
        int num=exchangeActivateOemDao.updateExchangeOem(oem);
        if(num>0){
            if(list!=null&&list.size()>0){
                String agentStr=null;
                String feeStr=null;
                String receiveStr=null;
                String repaymentStr=null;
                for(PropertyConfigActivate pc:list){
                    PropertyConfigActivate oldpc=propertyConfigActivateService.updateOemConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            pc.setConfigCode(oem.getOemNo());
                            propertyConfigActivateService.addOemConfig(pc);
                        }else{
                            propertyConfigActivateService.updateOemConfig(pc);
                        }
                        if("one_agent_share".equals(pc.getPropertyCode())){
                            agentStr=pc.getConfigValue();
                        }
                        if("oem_fee".equals(pc.getPropertyCode())){
                            feeStr=pc.getConfigValue();
                        }
                        if("rec_agent1_share".equals(pc.getPropertyCode())){
                            receiveStr=pc.getConfigValue();
                        }
                        if("rep_agent1_share".equals(pc.getPropertyCode())){
                            repaymentStr=pc.getConfigValue();
                        }
                    }else{
                        if(oldpc!=null){
                            propertyConfigActivateService.deleteOemConfig(pc);
                        }
                    }
                }
                if(agentOem!=null) {
                    agentOem.setOemNo(oem.getOemNo());
                    if(agentStr!=null&&feeStr!=null){
                        BigDecimal agentShare = new BigDecimal(agentStr);
                        BigDecimal fee = new BigDecimal(feeStr);
                        BigDecimal receiveShare=new BigDecimal(receiveStr);
                        BigDecimal repaymentShare=new BigDecimal(repaymentStr);
                        oneAgentOemService.insertOneAgentShare(agentOem.getAgentNo(),agentShare,fee,receiveShare,repaymentShare);
                        insertOneAgentShare(agentOem.getAgentNo(),agentShare,fee,receiveShare,repaymentShare);
                    }
                }
            }
            if(fileList!=null&&fileList.size()>0){
                for(PropertyConfigActivate pc:fileList){
                    PropertyConfigActivate oldpc=propertyConfigActivateService.updateOemConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            pc.setConfigCode(oem.getOemNo());
                            propertyConfigActivateService.addOemConfig(pc);
                        }else{
                            propertyConfigActivateService.updateOemConfig(pc);
                        }
                    }
                }
            }
        }
        return num;
    }

    @Override
    public AgentOemActivate getAgentOem(String oemNo, String agentLevel) {
        return exchangeActivateOemDao.getAgentOem(oemNo,agentLevel);
    }

    @Override
    public List<ExchangeActivateOem> getOemList() {
        return exchangeActivateOemDao.getOemList();
    }

    @Override
    public List<ProductActivateOem> selectProductOemList(ProductActivateOem proOem, Page<ProductActivateOem> page) {
        return exchangeActivateOemDao.selectProductOemList(proOem,page);
    }

    @Override
    public int updateProductOemShelve(long id, String state) {
        return exchangeActivateOemDao.updateProductOemShelve(id,state);
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
                        ProductActivateOem old=exchangeActivateOemDao.getProductOem(Long.parseLong(id));
                        if("2".equals(old.getShelve())){
                            num++;
                            exchangeActivateOemDao.updateProductOemShelve(Long.parseLong(id),"1");
                        }
                    }
                    msg.put("status", true);
                    msg.put("msg", "批量上架成功,成功上架"+num+"条!");
                    return 1;
                }else if("2".equals(state)){
                    for(String id:strs){
                        //验证
                        ProductActivateOem old=exchangeActivateOemDao.getProductOem(Long.parseLong(id));
                        if("1".equals(old.getShelve())){
                            num++;
                            exchangeActivateOemDao.updateProductOemShelve(Long.parseLong(id),"2");
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
    public int addProductOem(ProductActivateOem proOem) {
        proOem.setShelve("2");
        return exchangeActivateOemDao.addProductOem(proOem);
    }

    @Override
    public ProductActivateOem getProductOemDetail(long id) {
        return exchangeActivateOemDao.getProductOemDetail(id);
    }

    @Override
    public int updateProductOem(ProductActivateOem proOem) {
        return exchangeActivateOemDao.updateProductOem(proOem);
    }

    @Override
    public boolean checkProductOem(ProductActivateOem proOem) {
        if(proOem.getId()==null){
            List<ProductActivateOem> list=exchangeActivateOemDao.checkProductOemSelect(proOem);
            if(list!=null&&list.size()>0){
                return true;
            }
        }else{
            List<ProductActivateOem> list=exchangeActivateOemDao.checkProductOemSelectId(proOem);
            if(list!=null&&list.size()>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkProductOemShelve(long pId) {
        List<ProductActivateOem> list=exchangeActivateOemDao.checkProductOemShelve(pId);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public int deleteProductOemShelve(long pId) {
        return exchangeActivateOemDao.deleteProductOemShelve(pId);
    }

    @Override
    public ProductActivateOem getProductOemOne(String oemNo, long pId) {
        return exchangeActivateOemDao.getProductOemOne(oemNo,pId);
    }

    @Override
    public void synchronizationProductOem(long id, Map<String, Object> msg) {
        ExchangeActivateOem oem=exchangeActivateOemDao.getExchangeOem(id);
        //复制直营的
        ExchangeConfig ec=randomNumberService.getExchangeConfig("activate_oem_no");
        int sum=0;
        if(ec!=null&&ec.getSysValue()!=null&&!"".equals(ec.getSysValue())){
            if(ec.getSysValue().equals(oem.getOemNo())){
                msg.put("status", false);
                msg.put("msg", "直营的oem不能执行同步!");
                return;
            }
            List<ProductActivateOem> proOemList=exchangeActivateOemDao.getProductOemListDefault(ec.getSysValue());
            if(proOemList!=null&&proOemList.size()>0){
                for(ProductActivateOem old:proOemList){
                    old.setOemNo(oem.getOemNo());
                    List<ProductActivateOem> list=exchangeActivateOemDao.checkProductOemSelect(old);
                    if(list==null||list.size()<=0){
                        int num=exchangeActivateOemDao.addProductOem(old);
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
    public boolean checkAgentOem(AgentOemActivate agentOem) {
        AgentOemActivate agent=exchangeActivateOemDao.checkAgentOem(agentOem.getAgentNo(),"1");
        if(agent!=null){
            return true;
        }
        return false;
    }

    @Override
    public int openUpOem(long id, Map<String, Object> msg) {
        ExchangeActivateOem oem=getExchangeOem(id);
        if(oem!=null){
            List<PropertyConfigActivate> list=propertyConfigActivateService.getOemConfigAndValue("share_config","oem_config",oem.getOemNo());
            AgentOemActivate agentOem=getAgentOem(oem.getOemNo(),"1");
            if(checkOem(oem,list,agentOem,msg)){
                return 1;
            }else{
                updateRepayOem(oem,list,agentOem,msg);
            }
        }
        return 0;
    }

    @Override
    public void importDetail(List<ExchangeActivateOem> list, HttpServletResponse response) throws Exception{

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
            maps.put("repaymentOemNo",null);
            maps.put("receiveOemNo",null);
            maps.put("agentAccount",null);
            maps.put("companyNo",null);
            maps.put("companyName",null);
            maps.put("remark",null);
            data.add(maps);
        }else{
            Map<String, String> agentAccountMap=new HashMap<String, String>();
            agentAccountMap.put("0","否");
            agentAccountMap.put("1","是");

            for (ExchangeActivateOem or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()+"");
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("publicAccountName",or.getPublicAccountName()==null?"":or.getPublicAccountName());
                maps.put("teamId",or.getTeamId()==null?"":or.getTeamId());
                maps.put("agentNo",or.getAgentNo()==null?"":or.getAgentNo());
                maps.put("repaymentOemNo",or.getRepaymentOemNo()==null?"":or.getRepaymentOemNo());
                maps.put("receiveOemNo",or.getReceiveOemNo()==null?"":or.getReceiveOemNo());
                maps.put("agentAccount",agentAccountMap.get(or.getAgentAccount()));
                maps.put("companyNo",or.getCompanyNo()==null?"":or.getCompanyNo());
                maps.put("companyName",or.getCompanyName()==null?"":or.getCompanyName());
                maps.put("remark",or.getRemark()==null?"":or.getRemark());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","oemNo","oemName","publicAccountName","teamId","agentNo",
                "repaymentOemNo","receiveOemNo","agentAccount","companyNo","companyName","remark"

        };
        String[] colsName = new String[]{"序号","oem编号","组织名称","微信公众号","V2组织编号","V2代理商编号",
                "对应超级还组织编号","对应V2商户收款组织编号","开户状态","公司编号","公司名称","备注"

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

    private boolean checkOem(ExchangeActivateOem oem,List<PropertyConfigActivate> list,AgentOemActivate agentOem,Map<String, Object> msg){
        if(!checkStr(oem.getReceiveOemNo())||!checkStr(oem.getRepaymentOemNo())){
            msg.put("status",false);
            msg.put("msg", "该组织已开通了,不能重复开通!");
            return true;
        }
        if(checkStr(oem.getOemName())){
            msg.put("status",false);
            msg.put("msg", "组织名称不能为空!");
            return true;
        }
        if(checkStr(agentOem.getAgentNo())){
            msg.put("status",false);
            msg.put("msg", "代理商编码不能为空!");
            return true;
        }
        if(checkStr(oem.getCompanyNo())){
            msg.put("status",false);
            msg.put("msg", "公司编号不能为空!");
            return true;
        }
        if(checkStr(oem.getCompanyName())){
            msg.put("status",false);
            msg.put("msg", "公司名称不能为空!");
            return true;
        }
        if(checkStr(oem.getServicePhone())){
            msg.put("status",false);
            msg.put("msg", "客服电话不能为空!");
            return true;
        }
        if(list!=null&&list.size()>0){
            for(PropertyConfigActivate pc:list){
                if("rep_mertrade_rate".equals(pc.getPropertyCode())){
                    if(checkStr(pc.getConfigValue())){
                        msg.put("status",false);
                        msg.put("msg", "超级还商户交易费率不能为空!");
                        return true;
                    }
                }
                if("rep_mertrade1_fee".equals(pc.getPropertyCode())){
                    if(checkStr(pc.getConfigValue())){
                        msg.put("status",false);
                        msg.put("msg", "超级还商户交易单笔手续费不能为空!");
                        return true;
                    }
                }
                if("rep_merwithdraw_fee".equals(pc.getPropertyCode())){
                    if(checkStr(pc.getConfigValue())){
                        msg.put("status",false);
                        msg.put("msg", "超级还商户提现单笔手续费不能为空!");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int updateRepayOem(ExchangeActivateOem oem,List<PropertyConfigActivate> list,AgentOemActivate agentOem,Map<String, Object> msg){
        String str= repayOemInterface(oem,list,agentOem);
        if(checkStr(str)){
            msg.put("status",false);
            msg.put("msg", "接口返回参数为空!");
            return 1;
        }else{
            JSONObject json = JSONObject.parseObject(str);
            if("200".equals(json.getString("status"))){
                JSONObject data = JSONObject.parseObject(json.getString("data"));
                oem.setRepaymentOemNo(data.getString("oem_no"));///对应超级还组织编号
                oem.setReceiveOemNo(data.getString("team_id"));//对应V2商户收款组织编号
                int num=exchangeActivateOemDao.updateRepayOem(oem);
                if(num>0){
                    msg.put("status",true);
                    msg.put("msg", "开通超级还/商户收款组织成功!");
                }
            }else{
                msg.put("status",false);
                msg.put("msg", "开通超级还/商户收款组织失败!");
                return 1;
            }

        }
        return 0;
    }
    private boolean checkStr(String str){
        if(str==null||"".equals(str)){
            return true;
        }
        return false;
    }

    private String repayOemInterface(ExchangeActivateOem oem,List<PropertyConfigActivate> list,AgentOemActivate agentOem){
        String returnStr=null;
        try {
            SysDict sysDict = sysDictService.getByKey("SUPER_REPAYMENT_URL");
            if(sysDict!=null){
                String url=sysDict.getSysValue()+"/oemCreate";
                final HashMap<String, String> claims = new HashMap<>();
                claims.put("oem_name",oem.getOemName());
                claims.put("agent_no", agentOem.getAgentNo());
                if(list!=null&&list.size()>0){
                    BigDecimal percent=new BigDecimal("0.01");
                    for(PropertyConfigActivate pc:list){
                        if("rep_mertrade_rate".equals(pc.getPropertyCode())){
                            claims.put("trade_fee_rate",new BigDecimal(pc.getConfigValue()).multiply(percent).toString());
                        }
                        if("rep_mertrade1_fee".equals(pc.getPropertyCode())){
                            claims.put("trade_single_fee",pc.getConfigValue());
                        }
                        if("rep_merwithdraw_fee".equals(pc.getPropertyCode())){
                            claims.put("withdraw_single_fee",new BigDecimal(pc.getConfigValue()).multiply(percent).toString());
                        }
                    }
                }
                claims.put("withdraw_fee_rate","0");
                claims.put("company_no", oem.getCompanyNo());
                claims.put("company_name", oem.getCompanyName());
                claims.put("service_phone", oem.getServicePhone());
                claims.put("oem_type", "redemption");
                claims.put("oem_prefix", "500");
                claims.put("team_prefix", "710");
                log.info("开通超级还组织接口,请求路径:{},参数：{}",url, JSONObject.toJSONString(claims));
                returnStr =ClientInterface.httpPost(url, claims);
                log.info("开通超级还组织接口,返回结果：{}", returnStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnStr;
    }
}
