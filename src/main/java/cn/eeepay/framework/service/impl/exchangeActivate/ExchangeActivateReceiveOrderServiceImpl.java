package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.boss.action.exchangeActivate.ExchangeActivateOemAction;
import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateReceiveOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.WriteOffHis;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateReceiveOrder;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateShare;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUser;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateReceiveOrderService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateShareService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/6/7/007.
 * @author liuks
 * 商户收款订单Dao
 */
@Service("exchangeActivateReceiveOrderService")
public class ExchangeActivateReceiveOrderServiceImpl implements ExchangeActivateReceiveOrderService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateReceiveOrderServiceImpl.class);

    @Resource
    private ExchangeActivateReceiveOrderDao exchangeActivateReceiveOrderDao;

    @Resource
    private AgentInfoService agentInfoService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private ExchangeActivateShareService exchangeActivateShareService;

    @Override
    public List<ExchangeActivateReceiveOrder> selectAllList(ExchangeActivateReceiveOrder order, Page<ExchangeActivateReceiveOrder> page) {
        List<ExchangeActivateReceiveOrder> list=exchangeActivateReceiveOrderDao.selectAllList(order,page);
        setAgentName(page.getResult());
        dataProcessingList(page.getResult());
        return list;
    }
    /**
     * 数据处理List
     */
    private void dataProcessingList(List<ExchangeActivateReceiveOrder> list){
        if(list!=null&&list.size()>0){
            for(ExchangeActivateReceiveOrder item:list){
                if(item!=null){
                    item.setMobileUsername(StringUtil.sensitiveInformationHandle(item.getMobileUsername(),0));
                }
            }
        }
    }

    private  void setAgentName(List<ExchangeActivateReceiveOrder> list){
        if(list!=null&&list.size()>0){
            for(ExchangeActivateReceiveOrder order:list){
                if(order.getAgentNo()!=null&&!"".equals(order.getAgentNo())){
                    AgentInfo agentInfo =agentInfoService.getAgentByNo(order.getAgentNo());
                    if(agentInfo!=null){
                        order.setAgentName(agentInfo.getAgentName());
                    }
                }
                if(order.getOneAgentNo()!=null&&!"".equals(order.getOneAgentNo())){
                    AgentInfo agentInfo =agentInfoService.getAgentByNo(order.getOneAgentNo());
                    if(agentInfo!=null){
                        order.setOneAgentName(agentInfo.getAgentName());
                    }
                }
            }
        }
    }

    @Override
    public TotalAmount selectSum(ExchangeActivateReceiveOrder order, Page<ExchangeActivateReceiveOrder> page) {
        return exchangeActivateReceiveOrderDao.selectSum(order,page);
    }

    @Override
    public ExchangeActivateReceiveOrder getReceiveOrder(long id) {
        ExchangeActivateReceiveOrder order=exchangeActivateReceiveOrderDao.getReceiveOrder(id);
        getReceiveOrderDetail(order);
        return order;
    }

    @Override
    public List<ExchangeActivateReceiveOrder> importDetailSelect(ExchangeActivateReceiveOrder order) {
        List<ExchangeActivateReceiveOrder> list=exchangeActivateReceiveOrderDao.importDetailSelect(order);
        setAgentName(list);
        dataProcessingList(list);
        return list;
    }

    @Override
    public void importDetail(List<ExchangeActivateReceiveOrder> list, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "收款订单列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("oemNo",null);
            maps.put("oemName",null);
            maps.put("orderStatus",null);
            maps.put("merNo",null);
            maps.put("userName",null);
            maps.put("mobileUsername",null);
            maps.put("receiveMerchantNo",null);
            maps.put("sourceOrderNo",null);
            maps.put("payMethod",null);
            maps.put("amount",null);
            maps.put("rate",null);
            maps.put("provideAmout",null);
            maps.put("createTime",null);
            maps.put("agentNo",null);
            maps.put("agentName",null);
            maps.put("oneAgentNo",null);
            maps.put("oneAgentName",null);
            maps.put("oemShare",null);
            maps.put("plateShare",null);
            maps.put("accStatus",null);
            maps.put("accTime",null);
            data.add(maps);
        }else{
            Map<String, String> orderStatusMap=sysDictService.selectMapByKey("ORDER_STATUS");//订单状态
            Map<String, String> accStatusMap=sysDictService.selectMapByKey("ACC_STATUS");//记账状态

            Map<String, String> payMethodMap=new HashMap<String, String>();//支付方式
            payMethodMap.put("1","POS");
            payMethodMap.put("2","支付宝");
            payMethodMap.put("3","微信");
            payMethodMap.put("4","快捷");

            for (ExchangeActivateReceiveOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("orderStatus",orderStatusMap.get(or.getOrderStatus()));
                maps.put("merNo",or.getMerNo()==null?"":or.getMerNo());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("mobileUsername",or.getMobileUsername()==null?"":or.getMobileUsername());
                maps.put("receiveMerchantNo",or.getReceiveMerchantNo()==null?"":or.getReceiveMerchantNo());
                maps.put("sourceOrderNo",or.getSourceOrderNo()==null?"":or.getSourceOrderNo());
                maps.put("payMethod",payMethodMap.get(or.getPayMethod()));
                maps.put("amount",or.getAmount()==null?"":or.getAmount().toString());
                maps.put("rate",or.getRate()==null?"":or.getRate().toString());
                maps.put("provideAmout",or.getProvideAmout()==null?"":or.getProvideAmout().toString());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("agentNo",or.getAgentNo()==null?"":or.getAgentNo());
                maps.put("agentName",or.getAgentName()==null?"":or.getAgentName());
                maps.put("oneAgentNo",or.getOneAgentNo()==null?"":or.getOneAgentNo());
                maps.put("oneAgentName",or.getOneAgentName()==null?"":or.getOneAgentName());
                maps.put("oemShare",or.getOemShare()==null?"":or.getOemShare().toString());
                maps.put("plateShare",or.getPlateShare()==null?"":or.getPlateShare().toString());
                maps.put("accStatus",accStatusMap.get(or.getAccStatus()));
                maps.put("accTime", or.getAccTime()==null?"":sdf1.format(or.getAccTime()));

                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","oemNo","oemName","orderStatus","merNo","userName",
                "mobileUsername","receiveMerchantNo","sourceOrderNo","payMethod","amount","rate",
                "provideAmout","createTime","agentNo","agentName","oneAgentNo","oneAgentName",
                "oemShare","plateShare","accStatus","accTime"
        };
        String[] colsName = new String[]{"订单ID","组织ID","组织名称","订单状态","贡献人ID","贡献人名称",
                "贡献人手机号","收款商户ID","关联订单号","支付方式","收款金额","品牌发放总奖金扣率(%)",
                "品牌发放总奖金","创建时间","直属代理商ID","直属代理商名称","一级代理商ID","一级代理商名称",
                "品牌商分润","平台分润","记账状态","入账时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("收款订单列表异常!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    private void getReceiveOrderDetail(ExchangeActivateReceiveOrder order){
        if(order!=null){
            if(order.getAgentNo()!=null&&!"".equals(order.getAgentNo())){
                AgentInfo agentInfo =agentInfoService.getAgentByNo(order.getAgentNo());
                if(agentInfo!=null){
                    order.setAgentName(agentInfo.getAgentName());
                }
            }
            if(order.getOneAgentNo()!=null&&!"".equals(order.getOneAgentNo())){
                AgentInfo agentInfo =agentInfoService.getAgentByNo(order.getOneAgentNo());
                if(agentInfo!=null){
                    order.setOneAgentName(agentInfo.getAgentName());
                }
            }
            if(order.getOrderNo()!=null&&!"".equals(order.getOrderNo())){
                List<ExchangeActivateShare> shareList=exchangeActivateShareService.getOrderShare("O",order.getOrderNo());
                if(shareList!=null&&shareList.size()>0){
                    for(ExchangeActivateShare share:shareList){
                        AgentInfo agentInfo =agentInfoService.getAgentByNo(share.getAgentNo());
                        if(agentInfo!=null){
                            share.setAgentName(agentInfo.getAgentName());
                        }
                    }
                }
                order.setShareList(shareList);
            }
        }
    }
}
