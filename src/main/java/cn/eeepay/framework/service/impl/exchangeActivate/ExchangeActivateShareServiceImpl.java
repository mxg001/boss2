package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateShareDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateShare;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SysDictService;
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
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 订单分润查询service
 */
@Service("exchangeActivateShareService")
public class ExchangeActivateShareServiceImpl implements ExchangeActivateShareService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateShareServiceImpl.class);

    @Resource
    private ExchangeActivateShareDao exchangeActivateShareDao;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private AgentInfoService agentInfoService;

    @Override
    public List<ExchangeActivateShare> getOrderShare(String shareType, String orderId) {
        return exchangeActivateShareDao.getOrderShare(shareType,orderId);
    }

    @Override
    public List<ExchangeActivateShare> selectAllList(ExchangeActivateShare order, Page<ExchangeActivateShare> page) {
        List<ExchangeActivateShare> list=exchangeActivateShareDao.selectAllList(order,page);
        getAgentName(page.getResult());
        dataProcessingList(page.getResult());
        return list;
    }
    /**
     * 数据处理List
     */
    private void dataProcessingList(List<ExchangeActivateShare> list){
        if(list!=null&&list.size()>0){
            for(ExchangeActivateShare item:list){
                if(item!=null){
                    item.setMobileUsername(StringUtil.sensitiveInformationHandle(item.getMobileUsername(),0));
                }
            }
        }
    }
    @Override
    public List<ExchangeActivateShare> importDetailSelect(ExchangeActivateShare order) {
        List<ExchangeActivateShare> list=exchangeActivateShareDao.importDetailSelect(order);
        getAgentName(list);
        dataProcessingList(list);
        return list;
    }

    private void getAgentName(List<ExchangeActivateShare> list){
        if(list!=null&&list.size()>0) {
            for (ExchangeActivateShare ord : list) {
                if (ord.getAgentNo() != null && !"".equals(ord.getAgentNo())) {
                    AgentInfo agentInfo = agentInfoService.getAgentByNo(ord.getAgentNo());
                    if(agentInfo!=null){
                        ord.setAgentName(agentInfo.getAgentName());
                    }
                }
            }
        }
    }

    @Override
    public void importDetail(List<ExchangeActivateShare> list, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "订单分润列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("oemNo",null);
            maps.put("oemName",null);
            maps.put("orderNo",null);
            maps.put("shareType",null);
            maps.put("orderStatus",null);
            maps.put("merNo",null);
            maps.put("userName",null);
            maps.put("mobileUsername",null);
            maps.put("amount",null);
            maps.put("shareAmount",null);
            maps.put("agentNo",null);
            maps.put("agentName",null);
            maps.put("createTime",null);
            maps.put("accStatus",null);
            maps.put("accTime",null);
            data.add(maps);
        }else{
            Map<String, String> orderStatusMap=sysDictService.selectMapByKey("ORDER_STATUS");//订单状态
            Map<String, String> accStatusMap=sysDictService.selectMapByKey("ACC_STATUS");//记账状态

            Map<String, String> shareTypeMap=new HashMap<String, String>();
            shareTypeMap.put("D","积分兑换");
            shareTypeMap.put("O","收款");
            shareTypeMap.put("R","还款");

            for (ExchangeActivateShare or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()<0?"":or.getId()+"");
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                if(or.getShareType()!=null){
                    maps.put("shareType",shareTypeMap.get(or.getShareType()));
                }else{
                    maps.put("shareType","");
                }
                if(or.getOrderStatus()!=null){
                    maps.put("orderStatus",orderStatusMap.get(or.getOrderStatus()));
                }else{
                    maps.put("orderStatus","");
                }
                maps.put("merNo",or.getMerNo()==null?"":or.getMerNo());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("mobileUsername",or.getMobileUsername()==null?"":or.getMobileUsername());
                maps.put("amount",or.getAmount()==null?"":or.getAmount().toString());
                maps.put("shareAmount",or.getShareAmount()==null?"":or.getShareAmount().toString());
                maps.put("agentNo",or.getAgentNo()==null?"":or.getAgentNo());
                maps.put("agentName",or.getAgentName()==null?"":or.getAgentName());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
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
        String[] cols = new String[]{"id","oemNo","oemName","orderNo","shareType","orderStatus",
                "merNo","userName","mobileUsername","amount","shareAmount",
                "agentNo","agentName","createTime","accStatus","accTime"
        };
        String[] colsName = new String[]{"分润明细ID","组织ID","组织名称","订单ID","订单类型","订单状态",
                "贡献人ID","贡献人名称","贡献人手机号","交易金额","分润金额",
                "服务商ID","服务商名称","创建时间","记账状态","入账时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出订单分润列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public TotalAmount selectSum(ExchangeActivateShare order, Page<ExchangeActivateShare> page) {
        return exchangeActivateShareDao.selectSum(order,page);
    }
}
