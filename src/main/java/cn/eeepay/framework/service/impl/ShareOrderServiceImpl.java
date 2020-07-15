package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.ShareOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ShareOrder;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.service.ShareOrderService;
import cn.eeepay.framework.service.SysDictService;
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
@Service("shareOrderService")
public class ShareOrderServiceImpl implements ShareOrderService {

    private static final Logger log = LoggerFactory.getLogger(ShareOrderServiceImpl.class);

    @Resource
    private ShareOrderDao shareOrderDao;

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<ShareOrder> getOrderShare(String shareType,String orderId) {
        return shareOrderDao.getOrderShare(shareType,orderId);
    }

    @Override
    public List<ShareOrder> selectAllList(ShareOrder order, Page<ShareOrder> page) {
        List<ShareOrder> list=shareOrderDao.selectAllList(order,page);
        dataProcessingList(list);
        return list;
    }
    /**
     * 数据处理List
     */
    private void dataProcessingList(List<ShareOrder> list){
        if(list!=null&&list.size()>0){
            for(ShareOrder item:list){
                if(item!=null){
                    item.setMobileUsername(StringUtil.sensitiveInformationHandle(item.getMobileUsername(),0));
                    item.setShareMobile(StringUtil.sensitiveInformationHandle(item.getShareMobile(),0));
                }
            }
        }
    }
    @Override
    public List<ShareOrder> importDetailSelect(ShareOrder order) {
        List<ShareOrder> list= shareOrderDao.importDetailSelect(order);
        dataProcessingList(list);
        return list;
    }

    @Override
    public void importDetail(List<ShareOrder> list, HttpServletResponse response) throws Exception {
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
            maps.put("accountName",null);
            maps.put("mobileUsername",null);
            maps.put("totalShareAmount",null);
            maps.put("shareMerNo",null);
            maps.put("shareName",null);
            maps.put("shareMobile",null);
            maps.put("shareMerCapa",null);
            maps.put("shareAmount",null);
            maps.put("shareGrade",null);
            maps.put("createTime",null);
            maps.put("accStatus",null);
            maps.put("accTime",null);
            data.add(maps);
        }else{
            Map<String, String> merCapaMap=sysDictService.selectMapByKey("MER_CAPA");//用户身份
            Map<String, String> orderStatusMap=sysDictService.selectMapByKey("ORDER_STATUS");//订单状态
            Map<String, String> accStatusMap=sysDictService.selectMapByKey("ACC_STATUS");//记账状态

            Map<String, String> shareTypeMap=new HashMap<String, String>();
            shareTypeMap.put("A","代理订单");
            shareTypeMap.put("D","积分兑换");

            for (ShareOrder or : list) {
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
                maps.put("accountName",or.getAccountName()==null?"":or.getAccountName());
                maps.put("mobileUsername",or.getMobileUsername()==null?"":or.getMobileUsername());
                maps.put("totalShareAmount",or.getTotalShareAmount()==null?"":or.getTotalShareAmount().toString());
                maps.put("shareMerNo",or.getShareMerNo()==null?"":or.getShareMerNo());
                maps.put("shareName",or.getShareName()==null?"":or.getShareName());

                maps.put("shareMobile",or.getShareMobile()==null?"":or.getShareMobile());
                if(or.getShareMerCapa()!=null){
                    maps.put("shareMerCapa",merCapaMap.get(or.getShareMerCapa()));
                }else{
                    maps.put("shareMerCapa","");
                }
                maps.put("shareAmount",or.getShareAmount()==null?"":or.getShareAmount().toString());
                maps.put("shareGrade",or.getShareGrade()==null?"":or.getShareGrade());
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
                "merNo","accountName","mobileUsername","totalShareAmount","shareMerNo","shareName",
                "shareMobile","shareMerCapa","shareAmount","shareGrade","createTime","accStatus","accTime"
        };
        String[] colsName = new String[]{"分润明细ID","组织ID","组织名称","订单ID","订单类型","订单状态",
                "贡献人ID","贡献人姓名","贡献人手机号","总分润金额","收益人ID","收益人姓名",
                "收益人手机号","收益人身份","收益人分润","当前分润层级","创建时间","记账状态","入账时间"
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
    public TotalAmount selectSum(ShareOrder order, Page<ShareOrder> page) {
        return shareOrderDao.selectSum(order,page);
    }
}
