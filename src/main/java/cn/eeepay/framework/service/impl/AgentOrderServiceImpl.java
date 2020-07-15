package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.AgentOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.exchange.AgentOrder;
import cn.eeepay.framework.model.exchange.AgentShare;
import cn.eeepay.framework.model.exchange.ShareOrder;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.AgentOrderService;
import cn.eeepay.framework.service.ShareOrderService;
import cn.eeepay.framework.service.SysDictService;
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
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 代理费订单service
 */
@Service("agentOrderService")
public class AgentOrderServiceImpl implements AgentOrderService {

    private static final Logger log = LoggerFactory.getLogger(AgentOrderServiceImpl.class);

    @Resource
    private AgentOrderDao agentOrderDao;

    @Resource
    private ShareOrderService shareOrderService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private AgentInfoService agentInfoService;

    @Override
    public List<AgentOrder> selectAllList(AgentOrder order, Page<AgentOrder> page) {
        List<AgentOrder> list=agentOrderDao.selectAllList(order,page);
        getAgentShare(page.getResult());
        return list;
    }

    private void getAgentShare(List<AgentOrder> list) {
        if(list != null&&list.size()>0){
            for(AgentOrder order:list){
                List<AgentShare> shareList=agentOrderDao.getOrderShare(order.getOrderNo());
                if(shareList!=null&&shareList.size()>0){
                    for(AgentShare share:shareList){
                        if (share.getShareGrade() != null) {
                            if ("1".equals(share.getShareGrade())){
                                order.setAgentNoOne(share.getAgentNo());
                                order.setAgentOneAmout(share.getShareAmount());
                            } else if ("2".equals(share.getShareGrade())){
                                order.setAgentNoTwo(share.getAgentNo());
                                order.setAgentTwoAmout(share.getShareAmount());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public AgentOrder getAgentOrder(long id) {
        AgentOrder order=agentOrderDao.getAgentOrder(id);
        if(order!=null){
            List<ShareOrder> list=shareOrderService.getOrderShare("A",order.getOrderNo());
            if(list!=null&&list.size()>0){
                order.setShareOrderList(list);
            }
            List<AgentShare> list2=getOrderShare(order.getOrderNo());
            if(list2!=null&&list2.size()>0){
                order.setAgentShareList(list2);
            }
        }
        return order;
    }

    @Override
    public List<AgentShare> getOrderShare(String orderNo) {
        List<AgentShare> list=agentOrderDao.getOrderShare(orderNo);
        if(list!=null&&list.size()>0){
            for(AgentShare agent:list){
                AgentInfo agentInfo =agentInfoService.getAgentByNo(agent.getAgentNo());
                if(agentInfo!=null){
                    agent.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        return list;
    }

    @Override
    public List<AgentOrder> importDetailSelect(AgentOrder order) {
        List<AgentOrder> list=agentOrderDao.importDetailSelect(order);
        getAgentShare(list);
        return list;
    }

    @Override
    public void importDetail(List<AgentOrder> list, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "代理费订单列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("oemNo",null);
            maps.put("orderStatus",null);
            maps.put("merNo",null);
            maps.put("userName",null);
            maps.put("amount",null);
            maps.put("provideAmout",null);
            maps.put("createTime",null);
            maps.put("payTime",null);
            maps.put("payOrderNo",null);
            maps.put("oemName",null);
            maps.put("oemShare",null);
            maps.put("plateShare",null);
            maps.put("agentNoOne", null);
            maps.put("agentOneAmout", null);
            maps.put("agentNoTwo", null);
            maps.put("agentTwoAmout", null);
            maps.put("agentAmout",null);
            maps.put("merAmout",null);
            maps.put("accStatus",null);
            maps.put("accTime",null);
            data.add(maps);
        }else{
            Map<String, String> merCapaMap=sysDictService.selectMapByKey("MER_CAPA");//用户身份
            Map<String, String> orderStatusMap=sysDictService.selectMapByKey("ORDER_STATUS");//订单状态
            Map<String, String> accStatusMap=sysDictService.selectMapByKey("ACC_STATUS");//记账状态

            for (AgentOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                if(or.getOrderStatus()!=null){
                    maps.put("orderStatus",orderStatusMap.get(or.getOrderStatus()));
                }else{
                    maps.put("orderStatus","");
                }
                maps.put("merNo",or.getMerNo()==null?"":or.getMerNo());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("amount",or.getAmount()==null?"":or.getAmount().toString());
                maps.put("provideAmout",or.getProvideAmout()==null?"":or.getProvideAmout().toString());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("payTime", or.getPayTime()==null?"":sdf1.format(or.getPayTime()));
                maps.put("payOrderNo",or.getPayOrderNo()==null?"":or.getPayOrderNo());
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("oemShare",or.getOemShare()==null?"":or.getOemShare().toString());
                maps.put("plateShare",or.getPlateShare()==null?"":or.getPlateShare().toString());
                maps.put("agentNoOne", or.getAgentNoOne() == null ? "" : or.getAgentNoOne());
                maps.put("agentOneAmout", or.getAgentOneAmout() == null ? "" : or.getAgentOneAmout().toString());
                maps.put("agentNoTwo", or.getAgentNoTwo() == null ? "" : or.getAgentNoTwo());
                maps.put("agentTwoAmout", or.getAgentTwoAmout() == null ? "" : or.getAgentTwoAmout().toString());
                maps.put("agentAmout",or.getAgentAmout()==null?"":or.getAgentAmout().toString());
                maps.put("merAmout",or.getMerAmout()==null?"":or.getMerAmout().toString());
                if(or.getAccStatus()!=null){
                    maps.put("accStatus",accStatusMap.get(or.getAccStatus()));
                }else{
                    maps.put("accStatus","");
                }
                maps.put("accTime", or.getAccTime()==null?"":sdf1.format(or.getAccTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","oemNo","orderStatus","merNo","userName","amount",
                "provideAmout","createTime","payTime","payOrderNo","oemName","oemShare","plateShare",
                "agentNoOne", "agentOneAmout", "agentNoTwo", "agentTwoAmout",
                "agentAmout","merAmout","accStatus","accTime"
        };
        String[] colsName = new String[]{"订单ID","组织ID","订单状态","贡献人ID","贡献人名称","售价",
                "发放奖金","创建时间","支付时间","关联支付订单","组织名称","品牌商分润","平台分润",
                "一级分润代理商编号","一级分润代理商分润","二级分润代理商编号","二级分润代理商分润",
                "代理商总分润","用户总分润","记账状态","入账时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出代理费订单列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public TotalAmount selectSum(AgentOrder order, Page<AgentOrder> page) {
        return agentOrderDao.selectSum(order,page);
    }

}
