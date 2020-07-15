package cn.eeepay.framework.serviceImpl.exportBigData;

import cn.eeepay.boss.system.websocket.SpringWebSocketHandler;
import cn.eeepay.framework.serviceImpl.sysUser.DataSourceSwitch;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.dao.sysUser.ExportManageDao;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.ExportManage;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.exportBigData.TransactionExoprtService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("transactionExoprtService")
public class TransactionExoprtServiceImpl implements TransactionExoprtService {

    private static final Logger log = LoggerFactory.getLogger(TransactionExoprtServiceImpl.class);

    @Resource
    private SysDictService sysDictService;
    @Resource
    private TransInfoDao transInfoDao;
    @Resource
    private ExportManageDao exportManageDao;
    @Resource
    private DataSourceSwitch dataSourceSwitch;
    @Bean
    public SpringWebSocketHandler infoHandler() {
        return new SpringWebSocketHandler();
    }

    @Override
    public void export(String userName,String md5Key,String param, HttpSession session) {
        try {
            dataSourceSwitch.switchSlave();
            CollectiveTransOrder info = JSON.parseObject(param, CollectiveTransOrder.class);
            //条件转换
            if (info.getBool() == null || info.getBool().equals("")) {
                info.setBool("1");
            }

            //存储基础数据
            ExportManage addInfo=new ExportManage();
            addInfo.setMd5Key(md5Key);
            addInfo.setOperator(userName);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            addInfo.setFilterRemark("创建时间:"+(info.getSdate()==null?"无":sdf1.format(info.getSdate())) +"-"+(info.getEdate()==null?"无":sdf1.format(info.getEdate())));
            addInfo.setFilterStr(param);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String zipName= "交易记录"+sdf.format(new Date())+"_"+new Random().nextInt(100000) +".zip";
            addInfo.setFileName(zipName);
            dataSourceSwitch.switchMaster();//切换数据源
            exportManageDao.insertExport(addInfo);
            dataSourceSwitch.switchSlave();
            //校验数据是否过百万
            String orderNo = format(info.getOrderNo());
            String[] orderNos = null;
            if(StringUtils.isNotBlank(orderNo)){
                orderNos = orderNo.split(",");
            }
            if(null == orderNos || orderNos.length == 1){
                CollectiveTransOrder countInfo=getCount(info);
                int maxNum=1000000;
                if(countInfo!=null) {
                    int num=countInfo.getTradeCountNum().intValue();
                    if (num <= 0) {
                        addInfo.setStatus(2);
                        addInfo.setMsg("导出失败,导出数据为空!");
                        dataSourceSwitch.switchMaster();//切换数据源
                        exportManageDao.updateExportFaile(addInfo);
                        dataSourceSwitch.switchSlave();
                        return;
                    } else if (num > maxNum) {
                        addInfo.setStatus(2);
                        addInfo.setMsg("导出失败,数据超过" + maxNum + "条!");
                        dataSourceSwitch.switchMaster();//切换数据源
                        exportManageDao.updateExportFaile(addInfo);
                        dataSourceSwitch.switchSlave();
                        return;
                    }
                }
            }

            SysDict sysDict = sysDictService.getByKey(ExportBase.FILE_URL);
            if(sysDict!=null){
                //根目录
                String baseFolder=sysDict.getSysValue()+File.separator+ExportBase.FILENAME;// D:\image\exportFile
                //临时文件目录
                String exportFolder=ExportBase.fileBase(baseFolder);
                //导出文件
                exportData(info,exportFolder);
                //zip打包
                String identificationStr="tranFile";
                ExportBase.zipDelete(baseFolder,exportFolder,identificationStr,zipName);
                //存储数据
                addInfo.setStatus(1);
                addInfo.setFileUrl(identificationStr+File.separator+zipName);
                dataSourceSwitch.switchMaster();//切换数据源
                exportManageDao.updateExportStatus(addInfo);
                dataSourceSwitch.switchSlave();

                //给前端发送信息表示下载成功
                infoHandler().sendMessageToUser(session, new TextMessage("有新的下载任务已完成"));
            }else{
                log.error("导出存储的文件根目录不存在!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("交易查询异步导出异常!");
        }finally {
            dataSourceSwitch.clearDbType();
        }
    }

    private String format(String orderNo){
        return orderNo.replace("\n","")
                .replace(" ", "")
                .replace("，", ","); // 中文逗号转英文
    }

    private CollectiveTransOrder getCount(CollectiveTransOrder info){
        //dataSourceSwitch.switchMaster();
        info.setQueryTotalStatus(0);

        CollectiveTransOrder countInfo=transInfoDao.queryNumAndMoney(info);
        //dataSourceSwitch.switchSlave();
        return countInfo;
    }

    /**
     * 导出excel
     * @param info
     * @param exportFolder
     * @throws IOException
     */
    private void exportData(CollectiveTransOrder info,String exportFolder) throws IOException {
        //key 加密
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("Sheet1");
        double[] cellWidth=null;

        String[] cols = new String[] { "id", "orderNo", "groupCode", "settlementMethod", "merchantName",
                "merchantNo","merchantType","recommendedSource","sourceSysSta","acqReferenceNo","unionpayMerNo","payMethod","delivery", "orderType","zxRate", "serviceType", "cardType",
                "accountNo","iccardType","transAmount","nPrm","merchantRate","vasRate", "merchantFee", "deductionFee","actualFee",
                "amount", "outAmount", "feeAmount", "outActualFee", "merchantPrice",
                "deductionMerFee","actualMerchantPrice","quickRate","quickFee","acqEnname","transStatus","profitType","resMsg","freezeStatus", "settleStatus",
                "account","acqEnname","settleType","settleOrder","createTime", "transTime" ,"address","mobilephone","saleName","settleMsg"};
        String[] colsName = new String[] { "交易流水", "订单号", "集群编号","结算周期", "商户简称",
                "商户编号","商户类型","推广来源","代理商推广","参考号", "银联报备商户编号", "交易方式","交易模式", "订单类型","是否优享(一)收费", "收款类型", "卡种",
                "交易卡号","卡类型(I:芯片卡, CI:非接芯片卡, S:磁条卡)","金额（元）","保费","商户费率","服务费" ,"交易手续费（元）", "抵扣交易手续费（元）","实际交易手续费（元）",
                "出款金额（元）", "到账金额（元）", "出款手续费（元）","实际出款手续费（元）","优享(二)手续费（元）",
                "抵扣优享(二)手续费（元）","实际优享(二)手续费（元）", "云闪付费率（%）", "云闪付手续费（元）", "出款通道","交易状态","分润类型","响应码","冻结状态", "结算状态",
                "交易记账","收单机构","出款类型","出款订单ID", "创建时间", "交易时间" ,"经营地址","商户手机号","一级代理商所属销售","结算错误信息"};

        //创建头文件
        int rowIdx=0;
        ExportBase.builTitle(wb,sheet,cellWidth,rowIdx,colsName);

        //分页出来数据
        int pageNo=1;
        int pageSize=50000;
        SysDict sysDict=sysDictService.getByKey("EXOPRT_PAGESIZE");
        if(sysDict!=null){
            pageSize=Integer.parseInt(sysDict.getSysValue());
        }

        String orderNo = format(info.getOrderNo());
        String[] orderNos = null;
        if(StringUtils.isNotBlank(orderNo)){
            orderNos = orderNo.split(",");
        }
        List<Map<String, String>> list = new ArrayList<>();
        if(null != orderNos && orderNos.length > 1){//批量查询
            list = getDataList(info,pageNo,pageSize,"2", orderNos);
            rowIdx= ExportBase.writeData(wb,sheet,rowIdx,list,cols);
        }else {
            while (true){
                list = getDataList(info,pageNo,pageSize,"1", null);
                if(list!=null&&list.size()>0){
                    rowIdx= ExportBase.writeData(wb,sheet,rowIdx,list,cols);
                    pageNo=pageNo+1;
                }else{
                    break;
                }
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String excelFolder = exportFolder+File.separator+"交易记录" + sdf.format(new Date()) + ".xlsx";

        FileOutputStream fileOut = null;
        try {
            File excelFile = new File(excelFolder);
            FileUtil.createFolder(excelFile,1);
            fileOut = new FileOutputStream(excelFile);
            wb.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            fileOut.close();
        }

    }

    /**
     * 分页查询数据，组装导出excel数据
     * @param info
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Map<String, String>> getDataList(CollectiveTransOrder info,int pageNo,int pageSize,String type, String[] orderNos) {
        info.setPageSize(pageSize);
        info.setPageFirst( (pageNo - 1) * pageSize);

        List<CollectiveTransOrder> list = new ArrayList<>();
        if("2".equals(type)){ // type==2 批量查询
            list = transInfoDao.exportByOrderNos(orderNos);
        }else {
            list = transInfoDao.importAllInfo(info);
        }

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        if(list!=null&&list.size()>0){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<SysDict> sysDictList=sysDictService.selectByKey("MERCHANT_TYPE_LIST");
            Map<String, String> orderTypeMap = sysDictService.selectMapByKey("ORDER_TYPE");
            Map<String, String> serviceTypeMap = sysDictService.selectMapByKey("SERVICE_TYPE");
            Map<String, String> settleTypeMap=sysDictService.selectMapByKey("SETTLE_TYPE");
            Map<String, String> recommendedSourcesMap=sysDictService.selectMapByKey("RECOMMENDED_SOURCES");

            Map<String, String> profitTypeMap=new HashMap<String, String>();
            profitTypeMap.put("0","普通交易分润");
            profitTypeMap.put("1","封顶交易分润");

            for (CollectiveTransOrder collectiveTransOrder : list) {
                collectiveTransOrder.setActualFee(collectiveTransOrder.getMerchantFee()==null?"0":collectiveTransOrder.getMerchantFee().toString());//实际交易手续费取向上取整的商户手续费，原表字段不用了
                if(collectiveTransOrder.getDeductionFee()!=null){
                    collectiveTransOrder.setMerchantFee(collectiveTransOrder.getMerchantFee().add(new BigDecimal(collectiveTransOrder.getDeductionFee()))
                            .subtract(collectiveTransOrder.getMerchantPrice())
                            .add(collectiveTransOrder.getDeductionMerFee())
                            .subtract(collectiveTransOrder.getnPrm()==null?BigDecimal.ZERO:collectiveTransOrder.getnPrm()));
                }

                String temp = collectiveTransOrder.getAmount();
                if (StringUtils.isNotBlank(temp)) {
                    String[] temps = temp.split(",");
                    collectiveTransOrder.setAmount(temps[0]);
                    collectiveTransOrder.setFeeAmount(temps[1]);
                    collectiveTransOrder.setOutAmount(temps[2]);
                    collectiveTransOrder.setOutActualFee(temps[3]);
                }
                Map<String, String> maps = new HashMap<String, String>();
                // 结算方式
                String settlementMethod = "";
                if (StringUtils.isNotBlank(collectiveTransOrder.getSettlementMethod())) {
                    if ("0".equals(collectiveTransOrder.getSettlementMethod())) {
                        settlementMethod = "T0";
                    } else {
                        settlementMethod = "T1";
                    }
                }
                // 支付方式
                String payMethod = "";
                if (StringUtils.isNotBlank(collectiveTransOrder.getPayMethod())) {
                    if ("1".equals(collectiveTransOrder.getPayMethod())) {
                        payMethod = "POS";
                    } else if ("2".equals(collectiveTransOrder.getPayMethod())) {
                        payMethod = "支付宝";
                    } else if ("3".equals(collectiveTransOrder.getPayMethod())) {
                        payMethod = "微信";
                    } else if ("4".equals(collectiveTransOrder.getPayMethod())) {
                        payMethod = "快捷支付";
                    }
                }
                //交易模式
                String delivery = "旧模式";
                if(StringUtils.isNotBlank(collectiveTransOrder.getDelivery()) && "1".equals(collectiveTransOrder.getDelivery())){
                    delivery = "新模式";
                }
                //判断订单是否微创业
                String orderType = "普通订单";//默认不是
                if(orderTypeMap!=null){
                    orderType = orderTypeMap.get(String.valueOf(collectiveTransOrder.getOrderType()));
                }
                // 卡种类型
                String cardType = "";
                if (StringUtils.isNotBlank(collectiveTransOrder.getCardType())) {
                    if ("0".equals(collectiveTransOrder.getCardType())) {
                        cardType = "不限";
                    } else if ("1".equals(collectiveTransOrder.getCardType())) {
                        cardType = "贷记卡";
                    } else if ("2".equals(collectiveTransOrder.getCardType())) {
                        cardType = "借记卡";
                    }
                }
                // 冻结状态
                String freezeStatus = "";
                if (StringUtils.isNotBlank(collectiveTransOrder.getFreezeStatus())) {
                    if ("0".equals(collectiveTransOrder.getFreezeStatus())) {
                        freezeStatus = "正常";
                    } else if ("1".equals(collectiveTransOrder.getFreezeStatus())) {
                        freezeStatus = "风控冻结";
                    } else if ("2".equals(collectiveTransOrder.getFreezeStatus())) {
                        freezeStatus = "活动冻结";
                    } else if ("3".equals(collectiveTransOrder.getFreezeStatus())) {
                        freezeStatus = "财务冻结";
                    }
                }
                // 结算状态
                String settleStatus = "";
                if (StringUtils.isNotBlank(collectiveTransOrder.getSettleStatus())) {
                    if ("0".equals(collectiveTransOrder.getSettleStatus())) {
                        settleStatus = "未结算";
                    } else if ("1".equals(collectiveTransOrder.getSettleStatus())) {
                        settleStatus = "已结算";
                    } else if ("2".equals(collectiveTransOrder.getSettleStatus())) {
                        settleStatus = "结算中";
                    } else if ("3".equals(collectiveTransOrder.getSettleStatus())) {
                        settleStatus = "结算失败";
                    } else if ("4".equals(collectiveTransOrder.getSettleStatus())) {
                        settleStatus = "转T1结算";
                    } else if ("5".equals(collectiveTransOrder.getSettleStatus())) {
                        settleStatus = "不结算";
                    } else if ("6".equals(collectiveTransOrder.getSettleStatus())) {
                        settleStatus = "已返鼓励金";
                    }
                }
                // 交易记账
                String account = "";
                if (StringUtils.isNotBlank(collectiveTransOrder.getAccount())) {
                    if ("0".equals(collectiveTransOrder.getAccount())) {
                        account = "未记账";
                    } else if ("1".equals(collectiveTransOrder.getAccount())) {
                        account = "记账成功";
                    } else if ("2".equals(collectiveTransOrder.getAccount())) {
                        account = "记账失败";
                    }
                }

                String settleType = settleTypeMap.get(collectiveTransOrder.getSettleType());

                // 卡类型 I:芯片卡; C:非接芯片卡; S:磁条卡
                String iccardType = "";
                if (StringUtils.isNotBlank(collectiveTransOrder.getAccountNo())) {
                    if ("quickPass".equals(collectiveTransOrder.getReadCard())) {
                        iccardType = "C";
                    }
                    if (collectiveTransOrder.getIsIccard() != null && collectiveTransOrder.getIsIccard() == 1) {
                        iccardType += "I";
                    } else {
                        iccardType += "S";
                    }
                }

                String recommendedSource="";
                if (StringUtils.isNotBlank(collectiveTransOrder.getRecommendedSource())) {
                    recommendedSource = recommendedSourcesMap.get(collectiveTransOrder.getRecommendedSource());
                }
                // 收款类型
                if (collectiveTransOrder.getServiceType() != null && !collectiveTransOrder.getServiceType().isEmpty()) {
                    collectiveTransOrder.setServiceType(serviceTypeMap.get(collectiveTransOrder.getServiceType()));
                }

                maps.put("id", collectiveTransOrder.getId().toString());
                maps.put("orderNo", collectiveTransOrder.getOrderNo());
                maps.put("groupCode", collectiveTransOrder.getGroupCode());
                maps.put("settlementMethod", settlementMethod);
                maps.put("merchantName",null == collectiveTransOrder.getMerchantName() ? "" : collectiveTransOrder.getMerchantName());
                maps.put("merchantNo",null == collectiveTransOrder.getMerchantNo() ? "" : collectiveTransOrder.getMerchantNo());
                maps.put("merchantType",null == collectiveTransOrder.getMerchantType() ? "" : SysDictUtil.getSysNameByValue(sysDictList,collectiveTransOrder.getMerchantType()));
                maps.put("recommendedSource", recommendedSource);
                if("V2-agent".equals(collectiveTransOrder.getSourceSys())){
                    maps.put("sourceSysSta", "是");
                }else{
                    maps.put("sourceSysSta", "否");
                }
                maps.put("acqReferenceNo", null == collectiveTransOrder.getAcqReferenceNo() ? "": collectiveTransOrder.getAcqReferenceNo());
                maps.put("unionpayMerNo",collectiveTransOrder.getUnionpayMerNo()==null?"":collectiveTransOrder.getUnionpayMerNo());
                maps.put("payMethod", payMethod);
                maps.put("delivery", delivery);
                maps.put("orderType", orderType);
                maps.put("zxRate", collectiveTransOrder.getZxRate());
                maps.put("serviceType", null == collectiveTransOrder.getServiceType() ? "": collectiveTransOrder.getServiceType());
                maps.put("cardType", cardType);

                maps.put("accountNo",null == collectiveTransOrder.getAccountNo() ? "" :
                        info.getEditState()==0?StringUtil.sensitiveInformationHandle(collectiveTransOrder.getAccountNo(),4):collectiveTransOrder.getAccountNo());
                maps.put("iccardType", iccardType);
                maps.put("transAmount", null == collectiveTransOrder.getTransAmount() ? "" : collectiveTransOrder.getTransAmount().toString());
                maps.put("nPrm", null == collectiveTransOrder.getnPrm() ? "" :collectiveTransOrder.getnPrm().toString());
                maps.put("merchantRate",collectiveTransOrder.getMerchantRate()==null?"":collectiveTransOrder.getMerchantRate().toString());
                maps.put("vasRate", null == collectiveTransOrder.getVasRate() ? "" : collectiveTransOrder.getVasRate().toString());
                maps.put("merchantFee", null == collectiveTransOrder.getMerchantFee() ? "" : collectiveTransOrder.getMerchantFee().toString());
                maps.put("deductionFee", StringUtil.filterNull(collectiveTransOrder.getDeductionFee()));
                maps.put("actualFee",StringUtil.filterNull(collectiveTransOrder.getActualFee()));
                maps.put("amount",null == collectiveTransOrder.getAmount() ? "" : collectiveTransOrder.getAmount().toString());
                maps.put("outAmount", null == collectiveTransOrder.getOutAmount() ? "" : collectiveTransOrder.getOutAmount().toString());
                maps.put("feeAmount", null == collectiveTransOrder.getFeeAmount() ? "" : collectiveTransOrder.getFeeAmount().toString());
                maps.put("outActualFee", null == collectiveTransOrder.getOutActualFee() ? "" : collectiveTransOrder.getOutActualFee().toString());
                maps.put("merchantPrice",collectiveTransOrder.getMerchantPrice()==null?"":collectiveTransOrder.getMerchantPrice().toString());
                maps.put("deductionMerFee",collectiveTransOrder.getDeductionMerFee()==null?"":collectiveTransOrder.getDeductionMerFee().toString());
                maps.put("actualMerchantPrice",collectiveTransOrder.getActualMerchantPrice()==null?"":collectiveTransOrder.getActualMerchantPrice().toString());
                maps.put("quickRate", null == collectiveTransOrder.getQuickRate() ? "" : collectiveTransOrder.getQuickRate().toString());
                maps.put("quickFee", null == collectiveTransOrder.getQuickFee() ? "" : collectiveTransOrder.getQuickFee().toString());
                maps.put("acqEnname", null == collectiveTransOrder.getAcqEnname() ? "" : collectiveTransOrder.getAcqEnname());
                maps.put("transStatus", null == collectiveTransOrder.getTransStatus() ? "" : collectiveTransOrder.getTransStatus());
                maps.put("profitType",collectiveTransOrder.getProfitType()==null?"":profitTypeMap.get(collectiveTransOrder.getProfitType()));
                maps.put("resMsg", null == collectiveTransOrder.getResMsg() ? "" : collectiveTransOrder.getResMsg());
                maps.put("freezeStatus", freezeStatus);
                maps.put("settleStatus", settleStatus);
                maps.put("account", account);
                maps.put("settleType",settleType);
                maps.put("settleOrder", collectiveTransOrder.getSettleOrder()==null?"":collectiveTransOrder.getSettleOrder());
                maps.put("createTime", collectiveTransOrder.getCreateTime()==null?"":sdf1.format(collectiveTransOrder.getCreateTime()));
                maps.put("transTime", null == collectiveTransOrder.getTransTime() ? "" : sdf1.format(collectiveTransOrder.getTransTime()));
                maps.put("address", null == collectiveTransOrder.getAddress() ? "" : collectiveTransOrder.getAddress());
                maps.put("mobilephone", null == collectiveTransOrder.getMobilephone() ? "" :
                        info.getEditState()==0?StringUtil.sensitiveInformationHandle(collectiveTransOrder.getMobilephone(),0):collectiveTransOrder.getMobilephone());
                maps.put("saleName", null == collectiveTransOrder.getSaleName() ? "" : collectiveTransOrder.getSaleName());
                maps.put("settleMsg", collectiveTransOrder.getSettleMsg());

//                if(isSalesperson){
//                    String accountNo = maps.get("accountNo");
//                    account = accountNo.length()>4 ? StrUtil.hide(account,account.length()-4,account.length()) : accountNo;
//                    maps.put("accountNo", account);
//                }

                data.add(maps);
            }
        }
        return data;
    }
}
