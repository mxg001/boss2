package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.AfterSaleOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.AfterSaleOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.allAgent.AfterSaleOrderService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("afterSaleOrderService")
public class AfterSaleOrderServiceImpl implements AfterSaleOrderService {
    private static final Logger log = LoggerFactory.getLogger(AfterSaleOrderServiceImpl.class);

    @Resource
    private AfterSaleOrderDao afterSaleOrderDao;

    @Resource
    private SysDictService sysDictService;

    public List<AfterSaleOrder> queryAfterSaleOrderList(AfterSaleOrder info, Page<AfterSaleOrder> page){
        List<AfterSaleOrder> list=afterSaleOrderDao.queryAfterSaleOrderList(info,page);
        for(AfterSaleOrder a:list){
            if(a.getApplyImg()!=null&&!"".equals(a.getApplyImg())){
                String applyImgs[]=a.getApplyImg().split(",");
                for(int i=0;i<applyImgs.length;i++){
                    applyImgs[i]=CommonUtil.getImgUrlAgent(applyImgs[i]);
                }
                a.setApplyImg(StringUtils.join(applyImgs,","));
            }
            if(a.getDealImg()!=null&&!"".equals(a.getDealImg())){
                String dealImgs[]=a.getDealImg().split(",");
                for(int i=0;i<dealImgs.length;i++){
                    dealImgs[i]=CommonUtil.getImgUrlAgent(dealImgs[i]);
                }
                a.setDealImg(StringUtils.join(dealImgs,","));
            }
        }
        return list;
    }

    public Map<String, Object> queryAfterSaleOrderCount(AfterSaleOrder info){
        return afterSaleOrderDao.queryAfterSaleOrderCount(info);
    }

    public int updateProcessAfterSaleOrder(AfterSaleOrder info){
        return afterSaleOrderDao.updateProcessAfterSaleOrder(info);
    }

    public void exportAfterSaleOrder(AfterSaleOrder info, HttpServletResponse response)  throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;

        Page<AfterSaleOrder> page = new Page<>();
        page.setPageSize(Integer.MAX_VALUE);
        List<AfterSaleOrder> list = queryAfterSaleOrderList(info,page);
        String fileName = "申购售后订单"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;

        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("0", "待机构处理");
        statusMap.put("1", "待平台处理");
        statusMap.put("2", "已处理");
        statusMap.put("3", "已取消");
        Map<String, String> shipWayMap = new HashMap<>();
        shipWayMap.put("1", "机具类");
        shipWayMap.put("2", "物料类");
        Map<String, String> ascriptionMap = new HashMap<>();
        ascriptionMap.put("1", "机构");
        ascriptionMap.put("2", "平台");
        Map<String, String> handlerMap = new HashMap<>();
        handlerMap.put("1", "机构处理");
        handlerMap.put("2", "平台处理");
        Map<String, String> saleTypeMap=sysDictService.selectMapByKey("RRDL_SALE_TYPE");

        for(AfterSaleOrder item: list){
            map = new HashMap<>();
            map.put("orderNo", item.getOrderNo());
            map.put("payOrder", item.getPayOrder());
            map.put("shipWay", StringUtils.trimToEmpty(shipWayMap.get(item.getShipWay()+"")));
            map.put("saleType", StringUtils.trimToEmpty(saleTypeMap.get(item.getSaleType())));
            map.put("applyDesc", item.getApplyDesc());
            map.put("applyImg", item.getApplyImg());
            map.put("status", StringUtils.trimToEmpty(statusMap.get(item.getStatus()+"")));
            map.put("ascription", StringUtils.trimToEmpty(ascriptionMap.get(item.getAscription()+"")));
            map.put("handler", StringUtils.trimToEmpty(handlerMap.get(item.getHandler()+"")));
            map.put("dealDesc1", (item.getHandler()!=null&&item.getHandler()==1&&item.getStatus()==2)?item.getDealDesc():"");
            map.put("dealImg1", (item.getHandler()!=null&&item.getHandler()==1&&item.getStatus()==2)?item.getDealImg():"");
            map.put("dealDesc2", (item.getHandler()!=null&&item.getHandler()==2&&item.getStatus()==2)?item.getDealDesc():"");
            map.put("dealImg2", (item.getHandler()!=null&&item.getHandler()==2&&item.getStatus()==2)?item.getDealImg():"");
            map.put("applyTime", item.getApplyTime()==null?"":sdfTime.format(item.getApplyTime()));
            map.put("dealTime", item.getDealTime()==null?"":sdfTime.format(item.getDealTime()));
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","payOrder","shipWay","saleType","applyDesc","applyImg","status"
                ,"ascription","handler","dealDesc1","dealImg1","dealDesc2","dealImg2","applyTime","dealTime"};
        String[] colsName = new String[]{"售后编号","关联订单编号","商品类型","售后类型","售后说明","图片凭证","售后状态"
                ,"售后归属","处理人","机构处理结果","机构处理图片凭证","平台处理结果","平台处理图片凭证","提交日期","处理日期"};
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出申购售后订单失败,param:{}",JSONObject.toJSONString(info));
            e.printStackTrace();
        } finally {
            try {
                if(ouputStream!=null){
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
