package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.ExchangeOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.exchange.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.exchange.ExcRouteGoodService;
import cn.eeepay.framework.service.exchangeActivate.HttpJfpdapiService;
import cn.eeepay.framework.service.impl.exchangeActivate.HttpJfpdapiServiceImpl;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 兑换订单service
 */
@Service("exchangeOrderService")
public class ExchangeOrderServiceImpl  implements ExchangeOrderService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeOrderServiceImpl.class);

    @Resource
    private ExchangeOrderDao exchangeOrderDao;

    @Resource
    private ShareOrderService shareOrderService;

    @Resource
    private ProductTypeService productTypeService;

    @Resource
    private UserManagementService userManagementService;

    @Resource
    private ExchangeOemService  exchangeOemService;

    @Resource
    private PropertyConfigService propertyConfigService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private ExchangeProductService exchangeProductService;

    @Resource
    private AgentOrderService agentOrderService;

    @Resource
    private HttpJfpdapiService httpJfpdapiService;

    @Resource
    private ExcRouteGoodService excRouteGoodService;

    @Override
    public List<ExchangeOrder> selectAllList(ExchangeOrder order, Page<ExchangeOrder> page) {
        List<ExchangeOrder> list=exchangeOrderDao.selectAllList(order,page);
        getAgentShare(page.getResult());
        dataProcessingList(page.getResult());
        return list;
    }
    /**
     * 数据处理List
     */
    private void dataProcessingList(List<ExchangeOrder> list){
        if(list!=null&&list.size()>0){
            for(ExchangeOrder item:list){
                if(item!=null){
                    item.setMobileUsername(StringUtil.sensitiveInformationHandle(item.getMobileUsername(),0));
                }
            }
        }
    }
    private void getAgentShare(List<ExchangeOrder> list) {
        if(list != null&&list.size()>0){
            for(ExchangeOrder order:list){
                List<AgentShare> shareList=agentOrderService.getOrderShare(order.getOrderNo());
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
    public TotalAmount selectSum(ExchangeOrder order, Page<ExchangeOrder> page) {
        return exchangeOrderDao.selectSum(order,page);
    }

    @Override
    public ExchangeOrder getExchangeOrder(long id) {
        ExchangeOrder order=exchangeOrderDao.getExchangeOrder(id);
        getExchangeOrderDetail(order);
        return order;
    }

    private void getExchangeOrderDetail(ExchangeOrder order){
        if(order!=null){
            List<ShareOrder> list=shareOrderService.getOrderShare("D",order.getOrderNo());
            if(list!=null&&list.size()>0){
                order.setShareOrderList(list);
            }
            List<AgentShare> list2=agentOrderService.getOrderShare(order.getOrderNo());
            if(list2!=null&&list2.size()>0){
                order.setAgentShareList(list2);
            }
            ProductOem product=exchangeOemService.getProductOemOne(order.getOemNo(),order.getProductId());
            if(product!=null){
                order.setBrandPrice(product.getBrandPrice());
            }
        }
    }

    @Override
    public List<ExchangeOrder> importDetailSelect(ExchangeOrder order) {
        List<ExchangeOrder> list=exchangeOrderDao.importDetailSelect(order);
        getAgentShare(list);
        dataProcessingList(list);
        return list;
    }

    @Override
    public void importDetail(List<ExchangeOrder> list, HttpServletResponse response) throws Exception{
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
            maps.put("createTime", null);
            maps.put("oemName",null);
            maps.put("oemShare",null);
            maps.put("plateShare",null);
            maps.put("agentNoOne",null);
            maps.put("agentOneAmout", null);
            maps.put("agentNoTwo", null);
            maps.put("agentTwoAmout",null);
            maps.put("agentAmout",null);
            maps.put("merAmout",null);
            maps.put("accStatus",null);
            maps.put("accTime", null);
            maps.put("channel",null);
            maps.put("channelCheckStatus",null);
            maps.put("channelCheckTime", null);
            maps.put("saleOrderNo",null);
            maps.put("writeOffPrice",null);
            maps.put("checkStatusOne",null);
            maps.put("checkStatus",null);
            maps.put("checkOper",null);
            maps.put("checkTime",null);
            data.add(maps);
        }else{
            Map<String, String> orderStatusMap=sysDictService.selectMapByKey("ORDER_STATUS");//订单状态
            Map<String, String> accStatusMap=sysDictService.selectMapByKey("ACC_STATUS");//记账状态
            Map<String, String> channelMap=sysDictService.selectMapByKey("ORDER_CHANNEL");//渠道

            Map<String, String> checkStatusMap=new HashMap<String, String>();
            checkStatusMap.put("0","核销中");
            checkStatusMap.put("1","核销成功");
            checkStatusMap.put("2","核销失败");

            for (ExchangeOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                maps.put("orderStatus",orderStatusMap.get(or.getOrderStatus()));
                maps.put("orgName",or.getOrgName()==null?"":or.getOrgName());
                maps.put("typeName",or.getTypeName()==null?"":or.getTypeName());
                maps.put("productName",or.getProductName()==null?"":or.getProductName());
                maps.put("merNo",or.getMerNo()==null?"":or.getMerNo());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("mobileUsername",or.getMobileUsername()==null?"":or.getMobileUsername());
                maps.put("price",or.getPrice()==null?"":or.getPrice().toString());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("oemShare",or.getOemShare()==null?"":or.getOemShare().toString());
                maps.put("plateShare",or.getPlateShare()==null?"":or.getPlateShare().toString());
                maps.put("agentNoOne", or.getAgentNoOne() == null ? "" : or.getAgentNoOne());
                maps.put("agentOneAmout", or.getAgentOneAmout() == null ? "" : or.getAgentOneAmout().toString());
                maps.put("agentNoTwo", or.getAgentNoTwo() == null ? "" : or.getAgentNoTwo());
                maps.put("agentTwoAmout", or.getAgentTwoAmout() == null ? "" : or.getAgentTwoAmout().toString());
                maps.put("agentAmout",or.getAgentAmout()==null?"":or.getAgentAmout().toString());
                maps.put("merAmout",or.getMerAmout()==null?"":or.getMerAmout().toString());
                maps.put("accStatus",accStatusMap.get(or.getAccStatus()));
                maps.put("accTime", or.getAccTime()==null?"":sdf1.format(or.getAccTime()));
                maps.put("channel",channelMap.get(or.getChannel()));  //渠道
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
                "mobileUsername","price","createTime","oemName","oemShare","plateShare",
                "agentNoOne", "agentOneAmout", "agentNoTwo", "agentTwoAmout",
                "agentAmout","merAmout","accStatus","accTime",
                "channel","channelCheckStatus","channelCheckTime","saleOrderNo",
                "writeOffPrice","checkStatusOne","checkStatus","checkOper","checkTime"
        };
        String[] colsName = new String[]{"订单ID","组织ID","订单状态","兑换机构","产品类别","产品名称","贡献人ID","贡献人名称",
                "贡献人手机号","兑换价格","创建时间","组织名称","品牌商分润","平台分润",
                "一级分润代理商编号","一级分润代理商分润","二级分润代理商编号","二级分润代理商分润",
                "代理商总分润","用户总分润","记账状态","入账时间",
                "核销渠道","上游渠道核销状态","上游渠道核销时间","核销渠道订单ID",
                "核销价格","一次核销状态","二次核销状态","核销人","核销时间"
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
    public List<ExchangeOrder> selectAuditAll(ExchangeOrder order, Page<ExchangeOrder> page) {
        List<ExchangeOrder> list=exchangeOrderDao.selectAuditAll(order,page);
        getAgentShare(page.getResult());
        return list;
    }

    @Override
    public TotalAmount selectAuditSum(ExchangeOrder order, Page<ExchangeOrder> page) {
        return exchangeOrderDao.selectAuditSum(order,page);
    }

    @Override
    public List<ExchangeOrder> importAuditDetailSelect(ExchangeOrder order) {
        List<ExchangeOrder> list=exchangeOrderDao.importAuditDetailSelect(order);
        getAgentShare(list);
        return list;
    }

    @Override
    public void importAuditDetail(List<ExchangeOrder> list, HttpServletResponse response) throws Exception{
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
                    for (ExchangeOrder or : list) {
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
    public void importAuditDetailNoImg(List<ExchangeOrder> list, HttpServletResponse response) throws Exception {
        createExcel(list,null,2,response);
    }

    private void createExcel(List<ExchangeOrder> list, String excelUrl,int sta,HttpServletResponse response){
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
            maps.put("businessCode",null);
            maps.put("price",null);
            maps.put("createTime",null);
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
            for (ExchangeOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                maps.put("oemNo",or.getOemNo()==null?"":or.getOemNo());
                maps.put("orderStatus",orderStatusMap.get(or.getOrderStatus()));
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
                maps.put("businessCode",or.getBusinessCode()==null?"":or.getBusinessCode());
                maps.put("price",or.getPrice()==null?"":or.getPrice().toString());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("oemName",or.getOemName()==null?"":or.getOemName());
                maps.put("oemShare",or.getOemShare()==null?"":or.getOemShare().toString());
                maps.put("plateShare",or.getPlateShare()==null?"":or.getPlateShare().toString());
                maps.put("agentNoOne", or.getAgentNoOne() == null ? "" : or.getAgentNoOne());
                maps.put("agentOneAmout", or.getAgentOneAmout() == null ? "" : or.getAgentOneAmout().toString());
                maps.put("agentNoTwo", or.getAgentNoTwo() == null ? "" : or.getAgentNoTwo());
                maps.put("agentTwoAmout", or.getAgentTwoAmout() == null ? "" : or.getAgentTwoAmout().toString());
                maps.put("agentAmout",or.getAgentAmout()==null?"":or.getAgentAmout().toString());
                maps.put("merAmout",or.getMerAmout()==null?"":or.getMerAmout().toString());
                maps.put("accStatus",accStatusMap.get(or.getAccStatus()));//记账状态
                maps.put("accTime", or.getAccTime()==null?"":sdf1.format(or.getAccTime()));
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
        String[] cols = new String[]{"orderNo","oemNo","orderStatus","orgName","typeName","productName","redeemCode",
                "validityDateStart","validityDateEnd","productRemark","merNo","userName","mobileUsername","businessCode","price",
                "createTime","oemName","oemShare","plateShare",
                "agentNoOne", "agentOneAmout", "agentNoTwo", "agentTwoAmout",
                "agentAmout","merAmout","accStatus","accTime","channel",
                "channelCheckStatus","channelCheckTime","saleOrderNo",
                "writeOffPrice","checkStatusOne","checkStatus","checkOper","checkTime"
        };
        String[] colsName = new String[]{"订单ID","组织ID","订单状态","兑换机构","产品类别","产品名称","兑换码",
                "有效期开始时间","有效期截止时间","产品备注","贡献人ID","贡献人名称","贡献人手机号","贡献人证件号","兑换价格",
                "创建时间","组织名称","品牌商分润","平台分润",
                "一级分润代理商编号","一级分润代理商分润","二级分润代理商编号","二级分润代理商分润",
                "代理商总分润","用户总分润","记账状态","入账时间","核销渠道",
                "上游渠道核销状态","上游渠道核销时间","核销渠道订单ID",
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
    public ExchangeOrder getAuditExchangeOrder(long id) {
        ExchangeOrder order=exchangeOrderDao.getAuditExchangeOrder(id);
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
                num=exchangeOrderDao.writeOffOneTrue(writeOff);
            }else if("2".equals(writeOff.getCheckStatus())){//核销失败
                num=exchangeOrderDao.writeOffOneFalse(writeOff);
            }
        }else if("2".equals(writeOff.getCheckMode())){
            //二次核销
//            if("1".equals(writeOff.getCheckStatus())) { //核销成功
//                num=exchangeOrderDao.writeOffTwoTrue(writeOff);
//            }else if("2".equals(writeOff.getCheckStatus())){//核销失败
//                num=exchangeOrderDao.writeOffTwoFalse(writeOff);
//            }
            num=exchangeOrderDao.writeOffTwoTrue(writeOff);

        }
        if(num>0){
            exchangeOrderDao.addwriteOffHis(writeOff);
        }
        return num;
    }

    @Override
    public int addExchangeOrder(ExchangeOrder order, Map<String,Object> msg) {
        if(checkExchangeOrder(order,msg,1)==1){
            return -1;
        }
        //交易单的有效
        if(checkProduct(order,msg,1)==1){
            return -1;
        }
        if(completionExchangeOrder(order,msg,1)==1){
            return -1;
        }
        int num =exchangeOrderDao.addDeclareOrder(order);
        if(num>0){
            exchangeOrderDao.addOrder(order);
        }
        return num;
    }

    private int checkProduct(ExchangeOrder order,Map<String,Object> msg,int state){
        ExchangeProduct product=exchangeProductService.getExchangeProduct(order.getProductId());
        int minDay=product.getMinDay();
        Date now=new Date();
        if(order.getValidityDateStart()!=null&&order.getValidityDateEnd()!=null){
            if(order.getValidityDateEnd().getTime()-now.getTime()<=0){
                msg.put("status", false);
                msg.put("msg", "有效期截止时间不能小于当前时间!");
                return 1;
            }
            if(minDay>0) {
                try {
                    int num = DateUtil.daysBetween(now, order.getValidityDateEnd());
                    if (num < minDay) {
                        msg.put("status", false);
                        msg.put("msg", "当前时间到有效期截止时间不能小于" + minDay + "天!");
                        return 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(state==1){
            int minNum=product.getExcNum();
            if(minNum>0){
                Date startDay=DateUtil.getFirstDayOfMonthZero(now);
                Date endDay=DateUtil.getLastDay(now);
                List<ExchangeOrder> list=exchangeOrderDao.ckeckExchangeOrderNum(order.getMerNo(),startDay,endDay,order.getProductId());
                if(list!=null&&list.size()>0){
                    if((list.size()+1)>minNum){
                        msg.put("status", false);
                        msg.put("msg", "该用户报单次数已到上限!");
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public ExchangeOrder getExchangeOrerEdit(long id) {
        return exchangeOrderDao.getExchangeOrerEdit(id);
    }

    @Override
    public int saveExchangeOrder(ExchangeOrder order, Map<String, Object> msg) {
        if(checkExchangeOrder(order,msg,2)==1){
            return -1;
        }
        //交易单的有效
        if(checkProduct(order,msg,2)==1){
            return -1;
        }
        if(completionExchangeOrder(order,msg,2)==1){
            return -1;
        }
        int num=-1;
        if(checkString(order.getUploadImage())){
            num =exchangeOrderDao.updateDeclareOrderNoImage(order);
        }else{
            num =exchangeOrderDao.updateDeclareOrder(order);
        }
        if(num>0){
            exchangeOrderDao.updateOrder(order);
        }
        return num;
    }

    @Override
    public List<WriteOffHis> getWriteOffList(String orderNo) {
        return exchangeOrderDao.getWriteOffList(orderNo);
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
            ExchangeOrder oldOrder=exchangeOrderDao.selectSaveExchangeOrder(orderNo);
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
                int num=exchangeOrderDao.writeOffTwoImport(writeOff);
                if(num>0){
                    exchangeOrderDao.addwriteOffHis(writeOff);
                }
            }
        }
        msg.put("list",list);
        msg.put("status", true);
        msg.put("msg", "导入成功");
        return msg;
    }

    @Override
    public ExchangeOrder getExchangeOrderLittle(long id) {
        ExchangeOrder order=exchangeOrderDao.getAuditExchangeOrder(id);
        return order;
    }

    @Override
    public int orderApiSelect(ExchangeOrder order, Map<String, Object> msg) {
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
                                    ExchangeOrder upOrder=new ExchangeOrder();
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
                                    int num=exchangeOrderDao.ckeckUpper(upOrder);
                                    if(num>0){
                                        //核销日志
                                        insertOrderHis(order,checkReason,checkStatus);
                                        //修改路由商品价格
                                        ExcRouteGood good=new ExcRouteGood();
                                        good.setChannelNo(order.getChannel());
                                        good.setChannelPrice(price);
                                        good.setpId(order.getProductId());
                                        excRouteGoodService.checkRouteGood(good);
                                        msg.put("status", true);
                                        msg.put("msg", "上游查询订单成功,上游核销成功!");
                                        return 1;
                                    }
                                }else if(orderJson.getIntValue("status")==3){
                                    //失败
                                    ExchangeProduct product= exchangeProductService.getExchangeProduct(order.getProductId());
                                    if(product!=null){
                                        String failedReason=orderJson.get("failed_reason")==null?"失败,上游没有返回原因!":orderJson.getString("failed_reason");
                                        String checkStatus="2";
                                        ExchangeOrder upOrder=new ExchangeOrder();
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
                                            int num=exchangeOrderDao.ckeckUpper(upOrder);
                                            if(num>0){//核销日志
                                                insertOrderHis(order,failedReason,checkStatus);
                                            }
                                        }else{//支持线下
                                            int num=exchangeOrderDao.ckeckUpper(upOrder);
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

    /**
     * API核销日志
     */
    private  void insertOrderHis(ExchangeOrder order,String checkReason,String checkStatus){
        WriteOffHis writeOff=new WriteOffHis();
        writeOff.setOrderNo(order.getOrderNo());
        writeOff.setCheckOper(HttpJfpdapiServiceImpl.ROUTE_NO);
        writeOff.setCreateTime(new Date());
        writeOff.setRemark(checkReason);
        writeOff.setCheckMode("4");
        writeOff.setCheckStatus(checkStatus);
        writeOff.setChannel(HttpJfpdapiServiceImpl.ROUTE_NO);
        writeOff.setSaleOrderNo(order.getSaleOrderNo());
        exchangeOrderDao.addwriteOffHis(writeOff);
    }

    private int completionExchangeOrder(ExchangeOrder order,Map<String, Object> msg,int state){
        if(state==1){
            //初始化化数据
            String oemNo=getOrderNo("DC",20);
            order.setOrderNo(oemNo);
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            order.setDeclarePeople(principal.getId()+"");
        }
        if(!checkString(order.getMerNo())){
            UserManagement user =userManagementService.getUserManagementOne(order.getMerNo());
            if(user!=null){
                order.setParMerNo(user.getParMerNo());
                order.setMerCapa(user.getMerCapa());
                ProductOem productOem=exchangeOemService.getProductOemOne(user.getOemNo(),order.getProductId());
                if(productOem!=null){
                    if(!"1".equals(productOem.getShelve())){
                        msg.put("status", false);
                        msg.put("msg", "该oem下该产品未上架!");
                        return 1;
                    }
                }else{
                    msg.put("status", false);
                    msg.put("msg", "该oem下该产品未上架!");
                    return 1;
                }
                List<PropertyConfig> list=propertyConfigService.getOemConfigAndValue("product_config","oem_config",user.getOemNo());
                BigDecimal splitRatio=BigDecimal.ZERO;
                for(PropertyConfig pc:list){
                    if("1".equals(user.getMerCapa())){//普通会员
                        if("ordmem_pro_share".equals(pc.getPropertyCode())){
                            splitRatio=new BigDecimal(pc.getConfigValue());
                            break;
                        }
                    }else if("2".equals(user.getMerCapa())){//超级会员
                        if("supermem_pro_share".equals(pc.getPropertyCode())){
                            splitRatio=new BigDecimal(pc.getConfigValue());
                            break;
                        }
                    }else if("3".equals(user.getMerCapa())){
                        if("ordpar_pro_share".equals(pc.getPropertyCode())){
                            splitRatio=new BigDecimal(pc.getConfigValue());
                            break;
                        }
                    }else if("4".equals(user.getMerCapa())){
                        if("goldpar_pro_share".equals(pc.getPropertyCode())){
                            splitRatio=new BigDecimal(pc.getConfigValue());
                            break;
                        }
                    }else if("5".equals(user.getMerCapa())){
                        if("diampar_pro_share".equals(pc.getPropertyCode())){
                            splitRatio=new BigDecimal(pc.getConfigValue());
                            break;
                        }
                    }
                }
                BigDecimal  price=productOem.getBrandPrice().multiply(splitRatio).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                order.setPrice(price);
            }
        }
        return 0;
    }
    private int checkExchangeOrder(ExchangeOrder order,Map<String,Object> msg,int imageState){
        if(order!=null&&order.getTypeCode()!=null&&!"".equals(order.getTypeCode())){
            //校验
            Map<String,String> map=productTypeService.getAddProductType(order.getTypeCode());
            //物流信息
            if("1".equals(map.get("need_logistics_info"))){
                if(checkString(order.getLogisticsInfo())){
                    msg.put("status", false);
                    msg.put("msg", "物流信息不能为空!");
                    return 1;
                }
            }else{
                order.setLogisticsInfo(null);
            }
            //兑换数量
            if("1".equals(map.get("need_exec_num"))){
                if(checkString(order.getExecNum())){
                    msg.put("status", false);
                    msg.put("msg", "兑换数量不能为空!");
                    return 1;
                }
            }else{
                order.setExecNum(null);
            }
            //兑换码
            if("1".equals(map.get("need_redeem_code"))){
                if(checkString(order.getRedeemCode())){
                    msg.put("status", false);
                    msg.put("msg", "兑换码不能为空!");
                    return 1;
                }
            }else{
                order.setRedeemCode(null);
            }
            //有效期
            if("1".equals(map.get("need_express_date"))){
                if(order.getValidityDateStart()==null||order.getValidityDateEnd()==null){
                    msg.put("status", false);
                    msg.put("msg", "有效期不能为空!");
                    return 1;
                }
            }else{
                order.setValidityDateStart(null);
                order.setValidityDateEnd(null);
            }
            //备注
            if("1".equals(map.get("need_remak"))){

            }else{
                order.setProductRemark(null);
            }
            if(imageState==1){
                //上传截图
                if("1".equals(map.get("need_upload_screen"))){
                    if(checkString(order.getUploadImage())){
                        msg.put("status", false);
                        msg.put("msg", "上传截图不能为空!");
                        return 1;
                    }
                }else{
                    order.setUploadImage(null);
                }
            }else if(imageState==2){
                ExchangeOrder old= exchangeOrderDao.selectSaveExchangeOrder(order.getOrderNo());
                if(old.getUploadImage()==null){
                    //上传截图
                    if("1".equals(map.get("need_upload_screen"))){
                        if(checkString(order.getUploadImage())){
                            msg.put("status", false);
                            msg.put("msg", "上传截图不能为空!");
                            return 1;
                        }
                    }
                }
            }
        }
        return 0;
    }
    private boolean checkString(String str){
        if(str!=null&&!"".equals(str)){
            return false;
        }
        return true;
    }

    /**
     * 生产订单号
     * @param strNo
     * @param max
     * @return
     */
    private String getOrderNo(String strNo,int max){
        Long  id=exchangeOrderDao.getOrderNo();
            String seqNo=null;
        if((strNo.length()+id.toString().length())>=max){
            seqNo=(strNo+id.toString()).substring(0,max);
        }else{
            seqNo= RandomNumber.mumberRandom(strNo+id.toString(),max-(strNo+id.toString()).length(),0);
        }
        return seqNo;
    }
}
