package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.MachineBuyOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.MachineBuyOrder;
import cn.eeepay.framework.model.allAgent.TerInfo;
import cn.eeepay.framework.service.TerminalInfoService;
import cn.eeepay.framework.service.allAgent.MachineBuyOrderService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
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

@Service("machineBuyOrderService")
public class MachineBuyOrderServiceImpl implements MachineBuyOrderService {
    private static final Logger log = LoggerFactory.getLogger(MachineBuyOrderServiceImpl.class);

    @Resource
    private MachineBuyOrderDao machineBuyOrderDao;
    @Resource
    public TerminalInfoService terminalInfoService;

    @Override
    public List<MachineBuyOrder> queryMachineBuyOrderList(MachineBuyOrder info, Page<MachineBuyOrder> page) {
        List<MachineBuyOrder> list=machineBuyOrderDao.queryMachineBuyOrderList(info,page);
        for(MachineBuyOrder m:list){
            if(m.getImg()!=null&&!"".equals(m.getImg())){
                m.setImg(CommonUtil.getImgUrlAgent(m.getImg()));
            }
        }
        return list;
    }

    @Override
    public MachineBuyOrder queryMachineBuyOrderByOrderNo(String orderNo){
        return machineBuyOrderDao.queryMachineBuyOrderByOrderNo(orderNo);
    }

    @Override
    public List<TerInfo> querySNList(TerInfo info, Page<TerInfo> page){
        MachineBuyOrder machineBuyOrder=machineBuyOrderDao.queryMachineBuyOrderByOrderNo(info.getOrderNo());
        info.setOneUserCode(machineBuyOrder.getOneUserCode());
        return terminalInfoService.querySNList(info,page);
    }

    @Override
    public Map<String,Object> queryMachineBuyOrderCount(MachineBuyOrder info){
        return machineBuyOrderDao.queryMachineBuyOrderCount(info);
    }

    @Override
    public List<MachineBuyOrder> queryMachineBuyOrderByTime(String time){
        return machineBuyOrderDao.queryMachineBuyOrderByTime(time);
    }

    @Override
    public int updateMachineBuyOrderAccountEntry(MachineBuyOrder info){
        return machineBuyOrderDao.updateMachineBuyOrderAccountEntry(info);
    }

    /**
     * 导出机具申购订单
     * @throws IOException
     */
    @Override
    public void exportMachineBuyOrder(MachineBuyOrder info, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;

        Page<MachineBuyOrder> page = new Page<>();
        page.setPageSize(Integer.MAX_VALUE);
        List<MachineBuyOrder> list = machineBuyOrderDao.queryMachineBuyOrderList(info,page);
        String fileName = "机具申购订单"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;

        Map<String, String> orderStatusMap = new HashMap<>();
        orderStatusMap.put("0", "待发货");
        orderStatusMap.put("1", "待付款");
        orderStatusMap.put("2", "已发货");
        orderStatusMap.put("4", "已关闭");
        Map<String, String> afterSaleStatusMap = new HashMap<>();
        afterSaleStatusMap.put("0", "待机构处理");
        afterSaleStatusMap.put("1", "待平台处理");
        afterSaleStatusMap.put("2", "已处理");
        afterSaleStatusMap.put("3", "已取消");
        Map<String, String> transChannelMap = new HashMap<>();
        transChannelMap.put("wx", "微信");
        transChannelMap.put("zfb", "支付宝");
        transChannelMap.put("kj", "快捷支付");
        Map<String, String> transStatusMap = new HashMap<>();
        transStatusMap.put("0", "未支付");
        transStatusMap.put("1", "交易成功");
        transStatusMap.put("2", "交易失败");
        Map<String, String> entryStatusMap = new HashMap<>();
        entryStatusMap.put("0", "未入账");
        entryStatusMap.put("1", "已入账");
        Map<String, String> accStatusMap = new HashMap<>();
        accStatusMap.put("NOENTERACCOUNT", "未入账");
        accStatusMap.put("ENTERACCOUNTED", "已入账");
        Map<String, String> isPlatformMap = new HashMap<>();
        isPlatformMap.put("0", "机构发货");
        isPlatformMap.put("1", "平台发货");
        isPlatformMap.put("2", "大盟主发货");
        isPlatformMap.put("3", "盟主发货");
        Map<String, String> sendTypeMap = new HashMap<>();
        sendTypeMap.put("1", "快递配送");
        sendTypeMap.put("2", "线下自提");
        Map<String, String> shipWayMap = new HashMap<>();
        shipWayMap.put("1", "机具类");
        shipWayMap.put("2", "物料类");

        for(MachineBuyOrder item: list){
            map = new HashMap<>();
            map.put("orderNo", item.getOrderNo());
            if(item.getImg()!=null&&!"".equals(item.getImg())){
                item.setImg(CommonUtil.getImgUrlAgent(item.getImg()));
            }
            map.put("img", item.getImg());
            map.put("gName", item.getgName());
            map.put("color", item.getColor());
            map.put("size", item.getSize());
            map.put("price", StringUtil.filterNull(item.getPrice()));
            map.put("goodsCost", StringUtil.filterNull(item.getGoodsCost()));
            map.put("num", StringUtil.filterNull(item.getNum()));
            map.put("fare", "包邮");
            map.put("isPlatform", StringUtils.trimToEmpty(isPlatformMap.get(item.getIsPlatform()+"")));
            map.put("shipWay", StringUtils.trimToEmpty(shipWayMap.get(item.getShipWay()+"")));
            map.put("totalAmount", StringUtil.filterNull(item.getTotalAmount()));
            map.put("entryStatus", StringUtils.trimToEmpty(entryStatusMap.get(item.getEntryStatus())));
            map.put("entryAmount", StringUtil.filterNull(item.getEntryAmount()));
            map.put("entryTime", item.getEntryTime()==null?"":sdfTime.format(item.getEntryTime()));
            map.put("accStatus", StringUtils.trimToEmpty(accStatusMap.get(item.getAccStatus())));
            map.put("shareAmount", StringUtil.filterNull(item.getShareAmount()));
            map.put("accTime", item.getAccTime()==null?"":sdfTime.format(item.getAccTime()));
            map.put("realName", item.getRealName());
            map.put("userCode", item.getUserCode());
            map.put("parentId", item.getParentId());
            map.put("orderStatus", StringUtils.trimToEmpty(orderStatusMap.get(item.getOrderStatus()+"")));
            map.put("afterSaleStatus", StringUtils.trimToEmpty(afterSaleStatusMap.get(item.getAfterSaleStatus()+"")));
            map.put("sendType", StringUtils.trimToEmpty(sendTypeMap.get(item.getSendType()+"")));
            map.put("receiver", item.getReceiver());
            map.put("receiverMobile", item.getReceiverMobile());
            map.put("receiverAddress", item.getReceiverAddress());
            map.put("remark", item.getRemark());
            map.put("brandName", item.getBrandName());
            map.put("agentNo", item.getAgentNo());
            map.put("oneUserCode", item.getOneUserCode());
            map.put("oneUserName", item.getOneUserName());
            map.put("transChannel", StringUtils.trimToEmpty(transChannelMap.get(item.getTransChannel())));
            map.put("transStatus", StringUtils.trimToEmpty(transStatusMap.get(item.getTransStatus()+"")));
            map.put("transNo", item.getTransNo());
            map.put("transTime", item.getTransTime()==null?"":sdfTime.format(item.getTransTime()));
            map.put("createTime", item.getCreateTime()==null?"":sdfTime.format(item.getCreateTime()));
            map.put("sendTime", item.getSendTime()==null?"":sdfTime.format(item.getSendTime()));
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","img","gName","color","size","price","goodsCost","num","fare","isPlatform","shipWay","totalAmount"
                ,"entryStatus","entryAmount","entryTime","accStatus","shareAmount","accTime","realName","userCode"
                ,"parentId","orderStatus","afterSaleStatus","sendType","receiver","receiverMobile","receiverAddress","remark"
                ,"brandName","agentNo","oneUserCode","oneUserName","transChannel","transStatus","transNo","transTime","createTime","sendTime"};
        String[] colsName = new String[]{"订单编号","商品图片","商品标题","颜色","尺码","商品销售价(元)","机构成本价(元)","购买数量","运费","发货方","商品类型","订单金额(元)"
                ,"机具款项入账状态","机具款项入账金额(元)","机具款项入账时间","机具分润入账状态","机具分润入账金额(元)","机具分润入账时间","申购盟主姓名","申购盟主编号"
                ,"申购盟主推荐人编号","订单状态","售后状态","订单类型","收件人","联系方式","收货地址","备注"
                ,"所属品牌","代理商编号","机构编号","机构名称","支付方式","支付状态","支付订单号","支付日期","下单日期","发货日期"};
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出机具申购订单失败,param:{}",JSONObject.toJSONString(info));
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

    public List<TerInfo> queryShipMachineDetail(String orderNo, Page<TerInfo> page){
        return terminalInfoService.queryShipMachineDetail(orderNo,page);
    }
}
