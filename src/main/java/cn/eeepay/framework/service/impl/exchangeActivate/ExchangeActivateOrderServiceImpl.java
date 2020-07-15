package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.WriteOffHis;
import cn.eeepay.framework.model.exchangeActivate.ExcActRouteGood;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateProduct;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateShare;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.exchangeActivate.*;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 兑换订单service
 */
@Service("exchangeActivateOrderService")
public class ExchangeActivateOrderServiceImpl implements ExchangeActivateOrderService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateOrderServiceImpl.class);

    @Resource
    private ExchangeActivateOrderDao exchangeActivateOrderDao;

    @Resource
    private ExchangeActivateShareService exchangeActivateShareService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private AgentInfoService agentInfoService;

    @Resource
    private HttpJfpdapiService httpJfpdapiService;

    @Resource
    private ExchangeActivateProductService exchangeActivateProductService;

    @Resource
    private ExcActRouteGoodService excActRouteGoodService;

    @Override
    public List<ExchangeActivateOrder> selectAllList(ExchangeActivateOrder order, Page<ExchangeActivateOrder> page) {
        List<ExchangeActivateOrder> list= exchangeActivateOrderDao.selectAllList(order,page);
        getAgentName(page.getResult());
        dataProcessingList(page.getResult());
        return list;
    }
    /**
     * 数据处理List
     */
    private void dataProcessingList(List<ExchangeActivateOrder> list){
        if(list!=null&&list.size()>0){
            for(ExchangeActivateOrder item:list){
                if(item!=null){
                    item.setMobileUsername(StringUtil.sensitiveInformationHandle(item.getMobileUsername(),0));
                }
            }
        }
    }
    @Override
    public TotalAmount selectSum(ExchangeActivateOrder order, Page<ExchangeActivateOrder> page) {
        return exchangeActivateOrderDao.selectSum(order,page);
    }

    @Override
    public ExchangeActivateOrder getExchangeOrder(long id) {
        ExchangeActivateOrder order=exchangeActivateOrderDao.getExchangeOrder(id);
        getExchangeOrderDetail(order);
        return order;
    }

    @Override
    public List<ExchangeActivateOrder> importDetailSelect(ExchangeActivateOrder order) {
        List<ExchangeActivateOrder> list=exchangeActivateOrderDao.importDetailSelect(order);
        getAgentName(list);
        dataProcessingList(list);
        return list;
    }

    @Override
    public void importDetail(List<ExchangeActivateOrder> list, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "兑换订单列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("oemNo",null);
            maps.put("orderStatus",null);
            maps.put("orgName",null);
            maps.put("typeName",null);
            maps.put("productName",null);
            maps.put("merNo",null);
            maps.put("userName",null);
            maps.put("mobileUsername",null);
            maps.put("price",null);
            maps.put("createTime",null);
            maps.put("oemName",null);
            maps.put("agentNo",null);
            maps.put("agentName",null);
            maps.put("oneAgentNo",null);
            maps.put("oneAgentName",null);
            maps.put("oemShare",null);
            maps.put("plateShare",null);
            maps.put("accStatus",null);
            maps.put("accTime",null);
            maps.put("channel",null);
            maps.put("channelCheckStatus",null);
            maps.put("channelCheckTime", null);
            maps.put("saleOrderNo",null);
            maps.put("writeOffPrice",null);
            maps.put("checkStatusOne",null);
            maps.put("checkStatus",null);
            maps.put("checkOper",null);
            maps.put("checkTime", null);
            data.add(maps);
        }else{
            Map<String, String> orderStatusMap=sysDictService.selectMapByKey("ORDER_STATUS");//订单状态
            Map<String, String> accStatusMap=sysDictService.selectMapByKey("ACC_STATUS");//记账状态
            Map<String, String> channelMap=sysDictService.selectMapByKey("ORDER_CHANNEL");//渠道

            Map<String, String> checkStatusMap=new HashMap<String, String>();
            checkStatusMap.put("0","核销中");
            checkStatusMap.put("1","核销成功");
            checkStatusMap.put("2","核销失败");

            for (ExchangeActivateOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                if(or.getOrderStatus()!=null){
                    maps.put("orderStatus",orderStatusMap.get(or.getOrderStatus()));
                }else{
                    maps.put("orderStatus","");
                }
                maps.put("orgName",or.getOrgName()==null?"":or.getOrgName());
                maps.put("typeName",or.getTypeName()==null?"":or.getTypeName());
                maps.put("productName",or.getProductName()==null?"":or.getProductName());
                maps.put("merNo",or.getMerNo()==null?"":or.getMerNo());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("mobileUsername",or.getMobileUsername()==null?"":or.getMobileUsername());
                maps.put("price",or.getPrice()==null?"":or.getPrice().toString());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("agentNo",or.getAgentNo()==null?"":or.getAgentNo());
                maps.put("agentName",or.getAgentName()==null?"":or.getAgentName());
                maps.put("oneAgentNo",or.getOneAgentNo()==null?"":or.getOneAgentNo());
                maps.put("oneAgentName",or.getOneAgentName()==null?"":or.getOneAgentName());
                maps.put("oemShare",or.getOemShare()==null?"":or.getOemShare().toString());
                maps.put("plateShare",or.getPlateShare()==null?"":or.getPlateShare().toString());

                maps.put("accStatus",accStatusMap.get(or.getAccStatus()));
                maps.put("accTime", or.getAccTime()==null?"":sdf1.format(or.getAccTime()));
                //渠道
                maps.put("channel",channelMap.get(or.getChannel()));
                maps.put("channelCheckStatus",checkStatusMap.get(or.getChannelCheckStatus()));
                maps.put("channelCheckTime", or.getChannelCheckTime()==null?"":sdf1.format(or.getChannelCheckTime()));

                maps.put("saleOrderNo",or.getSaleOrderNo()==null?"":or.getSaleOrderNo());
                maps.put("writeOffPrice",or.getWriteOffPrice()==null?"":or.getWriteOffPrice().toString());
                maps.put("checkStatusOne",checkStatusMap.get(or.getCheckStatusOne()));
                maps.put("checkStatus",checkStatusMap.get(or.getCheckStatus()));
                maps.put("checkOper",or.getCheckOper()==null?"":or.getCheckOper());
                maps.put("checkTime", or.getCheckTime()==null?"":sdf1.format(or.getCheckTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","oemNo","orderStatus","orgName","typeName","productName","merNo","userName",
                "mobileUsername","price","createTime","oemName",
                "agentNo","agentName","oneAgentNo","oneAgentName",
                "oemShare","plateShare","accStatus","accTime",
                "channel","channelCheckStatus","channelCheckTime","saleOrderNo","writeOffPrice",
                "checkStatusOne","checkStatus","checkOper","checkTime"
        };
        String[] colsName = new String[]{"订单ID","组织ID","订单状态","兑换机构","产品类别","产品名称","贡献人ID","贡献人名称",
                "贡献人手机号","兑换价格","创建时间","组织名称",
                "直属代理商ID","直属代理商名称","一级代理商ID","一级代理商名称",
                "品牌商分润","平台分润","记账状态","入账时间",
                "核销渠道","上游渠道核销状态","上游渠道核销时间","核销渠道订单ID","核销价格",
                "一次核销状态","二次核销状态","核销人","核销时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出兑换订单列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public List<ExchangeActivateOrder> selectAuditAll(ExchangeActivateOrder order, Page<ExchangeActivateOrder> page) {
        List<ExchangeActivateOrder> list= exchangeActivateOrderDao.selectAuditAll(order,page);
        getAgentName(page.getResult());
        return list;
    }

    private void getAgentName(List<ExchangeActivateOrder> list){
        if(list!=null&&list.size()>0) {
            for (ExchangeActivateOrder ord : list) {
                if (ord.getAgentNo() != null && !"".equals(ord.getAgentNo())) {
                    AgentInfo agentInfo = agentInfoService.getAgentByNo(ord.getAgentNo());
                    if(agentInfo!=null){
                        ord.setAgentName(agentInfo.getAgentName());
                    }
                }
                if (ord.getOneAgentNo() != null && !"".equals(ord.getOneAgentNo())) {
                    AgentInfo agentInfo = agentInfoService.getAgentByNo(ord.getOneAgentNo());
                    if(agentInfo!=null){
                        ord.setOneAgentName(agentInfo.getAgentName());
                    }
                }
            }
        }
    }

    private void getExchangeOrderDetail(ExchangeActivateOrder order){
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
                List<WriteOffHis> writeOffHisList=getWriteOffList(order.getOrderNo());
                order.setWriteOffHisList(writeOffHisList);

                List<ExchangeActivateShare> shareList=exchangeActivateShareService.getOrderShare("D",order.getOrderNo());
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
    @Override
    public TotalAmount selectAuditSum(ExchangeActivateOrder order, Page<ExchangeActivateOrder> page) {
        return exchangeActivateOrderDao.selectAuditSum(order,page);
    }

    @Override
    public List<ExchangeActivateOrder> importAuditDetailSelect(ExchangeActivateOrder order) {
        List<ExchangeActivateOrder> list=exchangeActivateOrderDao.importAuditDetailSelect(order);
        getAgentName(list);
        return list;
    }

    @Override
    public void importAuditDetail(List<ExchangeActivateOrder> list, HttpServletResponse response) throws Exception{
        if(list!=null&&list.size()>0){
            SysDict sysDict = sysDictService.getByKey("IMAGES_URL");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
            if(sysDict!=null){
                String baseUrl=sysDict.getSysValue();
                String str=RandomNumber.mumberRandom("img",20,4);
                String mkdirs=baseUrl+File.separator+str;//D:\image\ksdds
                File mkdirsFile = new File(mkdirs);
                if(!mkdirsFile.exists()){
                    mkdirsFile.mkdirs();
                }
                String imgs=mkdirs+File.separator+"核销管理";//D:\image\ksdds\核销管理
                File imgsFile = new File(imgs);
                if(!imgsFile.exists()){
                    imgsFile.mkdirs();
                }
                String zips=mkdirs+File.separator+"zip.zip";//D:\image\ksdds\zip.zip
                File zipsFile = new File(zips);
                try {
                    if(!zipsFile.exists()) {
                        zipsFile.createNewFile();
                    }
                    for (ExchangeActivateOrder or : list) {
                        if(or.getUploadImage()!=null&&!"".equals(or.getUploadImage())){
                            downloadImage(or.getUploadImage(),or.getOrderNo(),imgs);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //excel
                createExcel(list,imgs,1,null);
                //zip打包
                FileUtil.zipsFile(imgsFile,zipsFile);
                //导出
                FileInputStream in=null;
                OutputStream out=null;
                try {
                    in= new FileInputStream(zipsFile);
                    String aa = "积分兑换核销管理"+sdf.format(new Date())+".zip" ;
                    String fileNameFormataa = new String(aa.getBytes("GBK"),"ISO-8859-1");
                    response.setHeader("Content-disposition", "attachment;filename="+fileNameFormataa);
                    out=response.getOutputStream();
                    byte[] bs = new byte[1024]; // 1K的数据缓冲
                    int len;
                    // 开始读取
                    while ((len = in.read(bs)) != -1) {
                        out.write(bs, 0, len);
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(out!=null){
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(in!=null){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //删除临时文件
                FileUtil.delFolder(mkdirs);
            }else{
                log.error("导出积分兑换核销管理未配置数据字典图片目录!");
            }
        }
    }

    @Override
    public void importAuditDetailNoImg(List<ExchangeActivateOrder> list, HttpServletResponse response) throws Exception {
        createExcel(list,null,2,response);
    }

    private void createExcel(List<ExchangeActivateOrder> list, String excelUrl,int sta,HttpServletResponse response){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd") ;

        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("oemNo",null);
            maps.put("orderStatus",null);
            maps.put("orgName",null);
            maps.put("typeName",null);
            maps.put("productName",null);
            maps.put("redeemCode",null);
            maps.put("validityDateStart",null);
            maps.put("validityDateEnd",null);
            maps.put("productRemark",null);
            maps.put("merNo",null);
            maps.put("userName",null);
            maps.put("mobileUsername",null);
            maps.put("idCardNo",null);
            maps.put("price",null);
            maps.put("createTime",null);
            maps.put("oemName",null);
            maps.put("agentNo",null);
            maps.put("agentName",null);
            maps.put("oneAgentNo",null);
            maps.put("oneAgentName",null);
            maps.put("oemShare",null);
            maps.put("plateShare",null);
            maps.put("accStatus",null);
            maps.put("accTime",null);
            maps.put("channel",null);
            maps.put("channelCheckStatus",null);
            maps.put("channelCheckTime",null);
            maps.put("saleOrderNo",null);
            maps.put("writeOffPrice",null);
            maps.put("checkStatusOne",null);
            maps.put("checkStatus",null);
            maps.put("checkOper",null);
            maps.put("checkTime", null);
            data.add(maps);
        }else{
            Map<String, String> orderStatusMap=sysDictService.selectMapByKey("ORDER_STATUS");//订单状态
            Map<String, String> accStatusMap=sysDictService.selectMapByKey("ACC_STATUS");//记账状态
            Map<String, String> channelMap=sysDictService.selectMapByKey("ORDER_CHANNEL");//渠道

            Map<String, String> checkStatusMap=new HashMap<String, String>();
            checkStatusMap.put("0","核销中");
            checkStatusMap.put("1","核销成功");
            checkStatusMap.put("2","核销失败");
            for (ExchangeActivateOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                if(or.getOrderStatus()!=null){
                    maps.put("orderStatus",orderStatusMap.get(or.getOrderStatus()));
                }else{
                    maps.put("orderStatus","");
                }
                maps.put("orgName",or.getOrgName()==null?"":or.getOrgName());
                maps.put("typeName",or.getTypeName()==null?"":or.getTypeName());
                maps.put("productName",or.getProductName()==null?"":or.getProductName());
                maps.put("redeemCode",or.getRedeemCode()==null?"":or.getRedeemCode());
                maps.put("validityDateStart", or.getValidityDateStart()==null?"":sdf2.format(or.getValidityDateStart()));
                maps.put("validityDateEnd", or.getValidityDateEnd()==null?"":sdf2.format(or.getValidityDateEnd()));
                maps.put("productRemark",or.getProductRemark()==null?"":or.getProductRemark());
                maps.put("merNo",or.getMerNo()==null?"":or.getMerNo());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("mobileUsername",or.getMobileUsername()==null?"":or.getMobileUsername());
                maps.put("idCardNo",or.getIdCardNo()==null?"":or.getIdCardNo());
                maps.put("price",or.getPrice()==null?"":or.getPrice().toString());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("agentNo",or.getAgentNo()==null?"":or.getAgentNo());
                maps.put("agentName",or.getAgentName()==null?"":or.getAgentName());
                maps.put("oneAgentNo",or.getOneAgentNo()==null?"":or.getOneAgentNo());
                maps.put("oneAgentName",or.getOneAgentName()==null?"":or.getOneAgentName());
                maps.put("oemShare",or.getOemShare()==null?"":or.getOemShare().toString());
                maps.put("plateShare",or.getPlateShare()==null?"":or.getPlateShare().toString());
                //记账状态
                maps.put("accStatus",accStatusMap.get(or.getAccStatus()));
                maps.put("accTime", or.getAccTime()==null?"":sdf1.format(or.getAccTime()));

                //渠道
                maps.put("channel",channelMap.get(or.getChannel()));
                maps.put("channelCheckStatus",checkStatusMap.get(or.getChannelCheckStatus()));
                maps.put("channelCheckTime", or.getChannelCheckTime()==null?"":sdf1.format(or.getChannelCheckTime()));

                maps.put("saleOrderNo",or.getSaleOrderNo()==null?"":or.getSaleOrderNo());
                maps.put("writeOffPrice",or.getWriteOffPrice()==null?"":or.getWriteOffPrice().toString());
                if(or.getCheckStatusOne()!=null){
                    maps.put("checkStatusOne",checkStatusMap.get(or.getCheckStatusOne()));
                }else{
                    maps.put("checkStatusOne","");
                }
                if(or.getCheckStatus()!=null){
                    maps.put("checkStatus",checkStatusMap.get(or.getCheckStatus()));
                }else{
                    maps.put("checkStatus","");
                }
                maps.put("checkOper",or.getCheckOper()==null?"":or.getCheckOper());
                maps.put("checkTime", or.getCheckTime()==null?"":sdf1.format(or.getCheckTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","oemNo","orderStatus","orgName","typeName","productName","redeemCode",
                "validityDateStart","validityDateEnd","productRemark","merNo","userName","mobileUsername","idCardNo","price",
                "createTime","oemName", "agentNo","agentName","oneAgentNo","oneAgentName",
                "oemShare","plateShare","accStatus","accTime","channel","channelCheckStatus","channelCheckTime","saleOrderNo",
                "writeOffPrice","checkStatusOne","checkStatus","checkOper","checkTime"
        };

        String[] colsName = new String[]{"订单ID","组织ID","订单状态","兑换机构","产品类别","产品名称","兑换码",
                "有效期开始时间","有效期截止时间","产品备注","贡献人ID","贡献人名称","贡献人手机号","贡献人证件号","兑换价格",
                "创建时间","组织名称", "直属代理商ID","直属代理商名称","一级代理商ID","一级代理商名称",
                "品牌商分润","平台分润","记账状态","入账时间","核销渠道","上游渠道核销状态","上游渠道核销时间","核销渠道订单ID",
                "核销价格","一次核销状态","二次核销状态","核销人","核销时间"
        };
        OutputStream ouputStream =null;
        try {
            if(sta==1){
                String excel=excelUrl+File.separator+"积分兑换核销管理"+sdf.format(new Date())+".xlsx";
                File excelFile = new File(excel);
                if(!excelFile.exists()){
                    excelFile.createNewFile();
                }
                ouputStream = new FileOutputStream(excelFile);
                export.export(cols, colsName, data,ouputStream);
            }else if(sta==2){
                String fileName = "积分兑换核销管理"+sdf.format(new Date())+".xlsx" ;
                String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
                response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
                ouputStream=response.getOutputStream();
                export.export(cols, colsName, data, response.getOutputStream());
            }
        }catch (Exception e){
            log.error("导出积分兑换核销管理失败!",e);
        }finally {
            if(ouputStream!=null){
                try {
                    ouputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //从网络下载图片到本地
    private void downloadImage(String uploadImage,String orderNo,String imgs){
        String imgSuffix=uploadImage.substring(uploadImage.indexOf("."),uploadImage.length());
        String imgUrl=CommonUtil.getImgUrlAgent(uploadImage);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream outStream =null;
        InputStream inStream=null;
        try {
            URL url = new URL(imgUrl);// 构造URL
            URLConnection conn = url.openConnection(); // 打开连接
            conn.setConnectTimeout(5 * 1000);  //超时响应时间为5秒
            //通过输入流获取图片数据
            inStream = conn.getInputStream();
            String img=imgs+File.separator+orderNo+"_"+sdf.format(new Date())+imgSuffix;////D:\image\ksdds\核销管理\1233_2018.jpg
            File imgFile = new File(img);
            if(!imgFile.exists()){
                imgFile.createNewFile();
            }
            outStream = new FileOutputStream(imgFile);
            byte[] bs = new byte[1024]; //1K的数据缓冲
            int len;
            while ((len = inStream.read(bs)) != -1) {
                outStream.write(bs, 0, len);
            }
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(outStream!=null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inStream!=null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public ExchangeActivateOrder getAuditExchangeOrder(long id) {
        ExchangeActivateOrder order=exchangeActivateOrderDao.getAuditExchangeOrder(id);
        getExchangeOrderDetail(order);
        return order;
    }

    @Override
    public int updateWriteOff(WriteOffHis writeOff) {
        int num=0;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        writeOff.setCheckOper(principal.getUsername());
        writeOff.setCreateTime(new Date());
        if("1".equals(writeOff.getCheckMode())){
            //一次核销
            if("1".equals(writeOff.getCheckStatus())){ //核销成功
                num=exchangeActivateOrderDao.writeOffOneTrue(writeOff);
            }else if("2".equals(writeOff.getCheckStatus())){//核销失败
                num=exchangeActivateOrderDao.writeOffOneFalse(writeOff);
            }
        }else if("2".equals(writeOff.getCheckMode())){
//            //二次核销
//            if("1".equals(writeOff.getCheckStatus())) { //核销成功
//                num=exchangeActivateOrderDao.writeOffTwoTrue(writeOff);
//            }else if("2".equals(writeOff.getCheckStatus())){//核销失败
//                num=exchangeActivateOrderDao.writeOffTwoFalse(writeOff);
//            }
            num=exchangeActivateOrderDao.writeOffTwoTrue(writeOff);
        }
        if(num>0){
            exchangeActivateOrderDao.addwriteOffHis(writeOff);
        }
        return num;
    }

    @Override
    public List<WriteOffHis> getWriteOffList(String orderNo) {
        return exchangeActivateOrderDao.getWriteOffList(orderNo);
    }

    @Override
    public Map<String, Object> importDiscount(MultipartFile file) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 遍历所有单元格,读取单元格
        int row_num = sheet.getLastRowNum();
        List<WriteOffHis> list=new ArrayList<WriteOffHis>();
        List<String> orderNoList=new ArrayList<String>();
        Map<String, String> checkStatusMap=new HashMap<String, String>();
        checkStatusMap.put("核销成功","1");
        checkStatusMap.put("核销失败","2");
        Map<String, String> receiveStatusMap=new HashMap<String, String>();
        receiveStatusMap.put("未收到","0");
        receiveStatusMap.put("已收到","1");

        Date date=new Date();
        for (int i = 1; i <= row_num; i++) {
            Row row = sheet.getRow(i);
            String orderNo = CellUtil.getCellValue(row.getCell(0));//订单号
            String checkStatusStr = CellUtil.getCellValue(row.getCell(1));//核销状态
            String orgName = CellUtil.getCellValue(row.getCell(2));//兑换机构
            String typeName = CellUtil.getCellValue(row.getCell(3));//产品类别名称
            String productName = CellUtil.getCellValue(row.getCell(4));//产品名称
            String channel = CellUtil.getCellValue(row.getCell(5));//核销渠道
            String saleOrderNo = CellUtil.getCellValue(row.getCell(6));//核销渠道订单号
            String writeOffPrice = CellUtil.getCellValue(row.getCell(7));//核销价格
            String checkReason = CellUtil.getCellValue(row.getCell(8));//核销备注
            String receiveStatusStr = CellUtil.getCellValue(row.getCell(9));//收货状态

            if(orderNo==null||"".equals(orderNo)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,订单号不能为空!");
                return msg;
            }
            ExchangeActivateOrder oldOrder=exchangeActivateOrderDao.selectSaveExchangeOrder(orderNo);
            if(oldOrder==null){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,订单号("+orderNo+")不存在!");
                return msg;
            }
            if(orderNoList.contains(orderNo)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,订单号("+orderNo+")重复!");
                return msg;
            }else{
                orderNoList.add(orderNo);
            }
            if(!"SUCCESS".equals(oldOrder.getOrderStatus())){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,该订单("+orderNo+")不是成功状态的订单,不能核销!");
                return msg;
            }
            if("1".equals(oldOrder.getCheckStatus())){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,该订单("+orderNo+")已核销成功状态,不能核销!");
                return msg;
            }
            if(checkStatusStr==null||"".equals(checkStatusStr)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,核销状态不能为空!");
                return msg;
            }
            String checkStatus="0";
            if(checkStatusMap.containsKey(checkStatusStr)){
                checkStatus=checkStatusMap.get(checkStatusStr);
            }else{
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,核销状态数据异常!");
                return msg;
            }
            double writeOffPriceNum=-1;
            if("核销成功".equals(checkStatusStr)){
                if(channel==null||"".equals(channel)){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,核销渠道不能为空!");
                    return msg;
                }
                try {
                    Integer.valueOf(channel);
                }catch (Exception e){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,核销渠道格式错误!");
                    return msg;
                }
                if(writeOffPrice==null||"".equals(writeOffPrice)){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,核销价格不能为空!");
                    return msg;
                }
                try {
                    writeOffPriceNum=Double.valueOf(writeOffPrice);
                }catch (Exception e){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,核销价格格式错误!");
                    return msg;
                }
            }else{
                if(checkReason==null||"".equals(checkReason)){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,核销失败时,核销备注不能为空!");
                    return msg;
                }
            }
            if(checkReason!=null&&checkReason.length()>50){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,核销备注不能超过50个长度!");
                return msg;
            }

            String receiveStatus="0";
            if(oldOrder.getLogisticsInfo()!=null&&!"".equals(oldOrder.getLogisticsInfo())){
                if(receiveStatusStr==null||"".equals(receiveStatusStr)){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,该订单("+orderNo+")为预付卡,收货状态不能为空!");
                    return msg;
                }
                if(receiveStatusMap.containsKey(receiveStatusStr)){
                    receiveStatus=checkStatusMap.get(receiveStatusStr);
                }else{
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,收货状态数据异常!");
                    return msg;
                }
            }

            WriteOffHis writeOff=new WriteOffHis();
            writeOff.setOrderNo(orderNo);
            writeOff.setCheckStatus(checkStatus);
            writeOff.setChannel(channel);
            writeOff.setSaleOrderNo(saleOrderNo);
            writeOff.setWriteOffPrice(writeOffPriceNum==-1?null:BigDecimal.valueOf(writeOffPriceNum));
            writeOff.setRemark(checkReason);
            writeOff.setCheckOper(principal.getUsername());
            writeOff.setReceiveStatus(receiveStatus);
            writeOff.setCheckMode("3");
            writeOff.setCreateTime(date);
            list.add(writeOff);
        }
        if(list.size()>0){
            for(WriteOffHis writeOff:list){
                int num=exchangeActivateOrderDao.writeOffTwoImport(writeOff);
                if(num>0){
                    exchangeActivateOrderDao.addwriteOffHis(writeOff);
                }
            }
        }
        msg.put("list",list);
        msg.put("status", true);
        msg.put("msg", "导入成功");
        return msg;
    }

    @Override
    public int orderApiSelect(ExchangeActivateOrder order,Map<String, Object> msg) {
        if((HttpJfpdapiServiceImpl.ROUTE_NO).equals(order.getChannel())){
            if(order.getSaleOrderNo()!=null&&!"".equals(order.getSaleOrderNo())){
                String returnStr=httpJfpdapiService.httpOrder(order.getSaleOrderNo());
                //返回结果处理
                JSONObject json = JSON.parseObject(returnStr);
                if(json!=null&&json.get("code")!=null&&"200".equals(json.get("code").toString())
                        &&json.get("enmsg")!=null&&"ok".equals(json.get("enmsg").toString())
                        ){
                    if(json.get("data")!=null){
                        JSONObject dataJson = json.getJSONObject("data");
                        if(dataJson.get("order")!=null){
                            JSONObject orderJson = dataJson.getJSONObject("order");
                            if(orderJson.get("status")!=null){
                                if(orderJson.getIntValue("status")==2){
                                    BigDecimal price=orderJson.get("profit")==null?BigDecimal.ZERO:orderJson.getBigDecimal("profit");
                                    String checkReason="成功";
                                    String checkStatus="1";
                                    //成功
                                    ExchangeActivateOrder upOrder=new ExchangeActivateOrder();
                                    upOrder.setOrderNo(order.getOrderNo());
                                    upOrder.setChannelCheckStatus(checkStatus);
                                    upOrder.setChannelCheckReason(checkReason);
                                    upOrder.setCheckOper(HttpJfpdapiServiceImpl.ROUTE_NO);
                                    upOrder.setCheckStatus(checkStatus);
                                    upOrder.setCheckStatusOne(checkStatus);
                                    upOrder.setWriteOffPrice(price);
                                    if(order.getLogisticsInfo()!=null&&!"".equals(order.getLogisticsInfo())){
                                        upOrder.setReceiveStatus("1");
                                    }
                                    int num=exchangeActivateOrderDao.ckeckUpper(upOrder);
                                    if(num>0){
                                        //核销日志
                                        insertOrderHis(order,checkReason,checkStatus);
                                        //修改路由商品价格
                                        ExcActRouteGood good=new ExcActRouteGood();
                                        good.setChannelNo(order.getChannel());
                                        good.setChannelPrice(price);
                                        good.setpId(order.getProductId());
                                        excActRouteGoodService.checkRouteGood(good);
                                        msg.put("status", true);
                                        msg.put("msg", "上游查询订单成功,上游核销成功!");
                                        return 1;
                                    }
                                }else if(orderJson.getIntValue("status")==3){
                                    //失败
                                    ExchangeActivateProduct product= exchangeActivateProductService.getExchangeProduct(order.getProductId());
                                    if(product!=null){
                                        String failedReason=orderJson.get("failed_reason")==null?"失败,上游没有返回原因!":orderJson.getString("failed_reason");
                                        String checkStatus="2";
                                        ExchangeActivateOrder upOrder=new ExchangeActivateOrder();
                                        upOrder.setOrderNo(order.getOrderNo());
                                        upOrder.setChannelCheckStatus(checkStatus);
                                        upOrder.setChannelCheckReason(failedReason);
                                        upOrder.setCheckOper(HttpJfpdapiServiceImpl.ROUTE_NO);

                                        if("0".equals(product.getUnderlineWriteoff())){//不支持线下
                                            upOrder.setCheckStatus(checkStatus);
                                            upOrder.setCheckStatusOne(checkStatus);
                                            upOrder.setCheckReason(failedReason);
                                            if(order.getLogisticsInfo()!=null&&!"".equals(order.getLogisticsInfo())){
                                                upOrder.setReceiveStatus("0");
                                            }
                                            int num=exchangeActivateOrderDao.ckeckUpper(upOrder);
                                            if(num>0){//核销日志
                                                insertOrderHis(order,failedReason,checkStatus);
                                            }
                                        }else{//支持线下
                                            int num=exchangeActivateOrderDao.ckeckUpper(upOrder);
                                            if(num>0){//核销日志
                                                insertOrderHis(order,failedReason,checkStatus);
                                            }
                                        }
                                        msg.put("status", true);
                                        msg.put("msg", "上游查询订单成功,上游核销失败!");
                                        return 2;
                                    }
                                }else{
                                    msg.put("status", true);
                                    msg.put("msg", "上游查询订单成功,上游订单状态为待审核,请耐心等待!");
                                    return 3;
                                }
                            }
                        }
                    }
                    msg.put("status", false);
                    msg.put("msg", "上游查询订单失败,上游无数据返回!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "上游查询订单失败,上游无该订单信息!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "上游查询订单失败,核销订单号为空!");
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "上游查询订单失败,核销通道未开通API!");
        }
        return 0;
    }

    @Override
    public ExchangeActivateOrder getExchangeOrderLittle(long id) {
        ExchangeActivateOrder order=exchangeActivateOrderDao.getAuditExchangeOrder(id);
        return order;
    }

    /**
     * API核销日志
     */
    private  void insertOrderHis(ExchangeActivateOrder order,String checkReason,String checkStatus){
        WriteOffHis writeOff=new WriteOffHis();
        writeOff.setOrderNo(order.getOrderNo());
        writeOff.setCheckOper(HttpJfpdapiServiceImpl.ROUTE_NO);
        writeOff.setCreateTime(new Date());
        writeOff.setRemark(checkReason);
        writeOff.setCheckMode("4");
        writeOff.setCheckStatus(checkStatus);
        writeOff.setChannel(HttpJfpdapiServiceImpl.ROUTE_NO);
        writeOff.setSaleOrderNo(order.getSaleOrderNo());
        exchangeActivateOrderDao.addwriteOffHis(writeOff);
    }
}
