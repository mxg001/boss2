package cn.eeepay.framework.serviceImpl.exportBigData;

import cn.eeepay.boss.system.websocket.SpringWebSocketHandler;
import cn.eeepay.framework.dao.SettleOrderInfoDao;
import cn.eeepay.framework.dao.sysUser.ExportManageDao;
import cn.eeepay.framework.model.ExportManage;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.exportBigData.PaymentOutExoprtService;
import cn.eeepay.framework.serviceImpl.sysUser.DataSourceSwitch;
import cn.eeepay.framework.util.FileUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service("paymentOutExoprtService")
public class PaymentOutExoprtServiceImpl  implements PaymentOutExoprtService {
    private static final Logger log = LoggerFactory.getLogger(PaymentOutExoprtServiceImpl.class);

    @Resource
    private SysDictService sysDictService;
    @Resource
    private SettleOrderInfoDao settleOrderInfoDao;
    @Resource
    private ExportManageDao exportManageDao;
    @Resource
    private DataSourceSwitch dataSourceSwitch;
    @Bean
    public SpringWebSocketHandler infoHandler() {
        return new SpringWebSocketHandler();
    }

    @Override
    public void export(String userName, String md5Key, String param, HttpSession session) {
        try {
            dataSourceSwitch.switchSlave();//切换数据源
            SettleOrderInfo info = JSONObject.parseObject(param,SettleOrderInfo.class);

            //存储基础数据
            ExportManage addInfo=new ExportManage();
            addInfo.setMd5Key(md5Key);
            addInfo.setOperator(userName);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            addInfo.setFilterRemark("创建时间:"+(info.getSdate()==null?"无":sdf1.format(info.getSdate())) +"-"+(info.getEdate()==null?"无":sdf1.format(info.getEdate())));
            addInfo.setFilterStr(param);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String zipName= "出款明细记录"+sdf.format(new Date())+"_"+new Random().nextInt(100000) +".zip";
            addInfo.setFileName(zipName);
            dataSourceSwitch.switchMaster();//切换数据源
            exportManageDao.insertExport(addInfo);
            dataSourceSwitch.switchSlave();//切换数据源

            //校验数据是否过百万
            String accountNo = format(info.getAccountSerialNo());
            String[] accountNos = null;
            if(org.apache.commons.lang3.StringUtils.isNotBlank(accountNo)){
                accountNos = accountNo.split(",");
            }
            if(null == accountNos || accountNos.length == 1){
                Map map=getCount(info);
                int maxNum=1000000;
                if(map!=null) {
                    int num=Integer.parseInt(map.get("countNum").toString());
                    if (num <= 0) {
                        addInfo.setStatus(2);
                        addInfo.setMsg("导出失败,导出数据为空!");
                        dataSourceSwitch.switchMaster();//切换数据源
                        exportManageDao.updateExportFaile(addInfo);
                        dataSourceSwitch.switchSlave();//切换数据源
                        return;
                    } else if (num > maxNum) {
                        addInfo.setStatus(2);
                        addInfo.setMsg("导出失败,数据超过" + maxNum + "条!");
                        dataSourceSwitch.switchMaster();//切换数据源
                        exportManageDao.updateExportFaile(addInfo);
                        dataSourceSwitch.switchSlave();//切换数据源
                        return;
                    }
                }
            }

            SysDict sysDict = sysDictService.getByKey(ExportBase.FILE_URL);
            if(sysDict!=null){
                String baseFolder=sysDict.getSysValue()+File.separator+ExportBase.FILENAME;// D:\image\exportFile
                //初始文件目录创建
                String exportFolder=ExportBase.fileBase(baseFolder);
                //导出文件
                exportData(info,exportFolder);
                //zip打包
                String identificationStr="payOutFile";
                ExportBase.zipDelete(baseFolder,exportFolder,identificationStr,zipName);
                //存储数据
                addInfo.setStatus(1);
                addInfo.setFileUrl(identificationStr+File.separator+zipName);
                dataSourceSwitch.switchMaster();//切换数据源
                exportManageDao.updateExportStatus(addInfo);
                dataSourceSwitch.switchSlave();//切换数据源

                //给前端发送信息表示下载成功
                infoHandler().sendMessageToUser(session, new TextMessage("有新的下载任务已完成"));
            }else{
                log.error("导出存储的文件根目录不存在!");
            }
        } catch (Exception e) {
            log.error("出款明细记录异步导出异常!", e);
        }finally {
            dataSourceSwitch.clearDbType();
        }
    }

    private String format(String accountNos){
        return accountNos.replace("，", ",").replace("\n","")
                .replace(" ", ""); // 中文逗号转英文
    }

    private Map getCount(SettleOrderInfo info){
        info.setQueryTotalStatus(0);
        Map map=settleOrderInfoDao.getOutDetailTotalMoney(info);
        return map;
    }

    /**
     * 导出excel
     * @param info
     * @param exportFolder
     * @throws IOException
     */
    private void exportData(SettleOrderInfo info,String exportFolder) throws IOException {
        //key 加密
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("Sheet1");
        double[] cellWidth=null;

        String[] cols = new String[]{"id","accountSerialNo","transId","orderNo","delivery","settleStatusName","statusName",
                "settleName","agentNoOne","agentNameOne","settleUserNo","settleUserName","unionpayMerNo","mobile","amount","feeAmount","deductionFee","perAgentFee","outAmount","actualFee","acqEnname",
                "outServiceName","sourceBatchNo","createTime","inAccName","inAccNo","inBankName"};
        String[] colsName = new String[]{"出款记录ID","出款流水号","来源订单号","交易订单号","出款模式","结算状态","出款明细状态","出款类型","一级代理商编号","一级代理商名称","结算用户编号",
                "结算用户简称","银联报备商户编号","用户手机号","出款金额（元）","出款手续费（元）","抵扣手续费（元）","税费（元）","实际出款金额（元）","实际出款手续费（元）",
                "出款通道","出款服务名称","来源批次号","出款时间","收款账户名称","结算卡号","结算银行名称"};

        int rowIdx=0;
        ExportBase.builTitle(wb,sheet,cellWidth,rowIdx,colsName);
        int pageNo=1;
        int pageSize=50000;
        SysDict sysDict=sysDictService.getByKey("EXOPRT_PAGESIZE");
        if(sysDict!=null){
            pageSize=Integer.parseInt(sysDict.getSysValue());
        }

        String accountNo = format(info.getAccountSerialNo());
        String[] accountNos = null;
        if(org.apache.commons.lang3.StringUtils.isNotBlank(accountNo)){
            accountNos = accountNo.split(",");
        }
        List<Map<String, String>> list = new ArrayList<>();
        if(null != accountNos && accountNos.length > 1){//批量查询
            list = getDataList(info,pageNo,pageSize, "2", accountNos);
            rowIdx= ExportBase.writeData(wb,sheet,rowIdx,list,cols);
        }else {
            while (true){
                list = getDataList(info,pageNo,pageSize, "1", null);
                if(list != null && list.size()>0){
                    rowIdx= ExportBase.writeData(wb,sheet,rowIdx,list,cols);
                    pageNo=pageNo+1;
                }else{
                    break;
                }
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String excelFolder = exportFolder+File.separator+"出款明细记录" + sdf.format(new Date()) + ".xlsx";
        File excelFile = new File(excelFolder);
        FileUtil.createFolder(excelFile,1);

        FileOutputStream fileOut = null;
        try {
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
    public List<Map<String, String>> getDataList(SettleOrderInfo info,int pageNo,int pageSize, String type, String[] accountNos) {
        info.setPageSize(pageSize);
        info.setPageFirst( (pageNo - 1) * pageSize);

        List<SettleOrderInfo> list = new ArrayList<>();
        if("2".equals(type)){
            list = settleOrderInfoDao.exportByAccountNos(accountNos);
        }else {
            list=settleOrderInfoDao.exportOutDetailAllInfo(info);
        }

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        if(list!=null&&list.size()>0){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> statusNameMap = sysDictService.selectMapByKey("OUT_STATUS");//出款状态
            Map<String, String> settleNameMap = sysDictService.selectMapByKey("SETTLE_TYPE");//出款类型
            Map<String, String> settleStatusNameMap = sysDictService.selectMapByKey("SETTLE_STATUS");//结算状态

            for (SettleOrderInfo settleOrderInfo : list) {
                //屏蔽敏感信息
                if(info.getEditState()==0){
                    settleOrderInfo.setMobile(StringUtil.sensitiveInformationHandle(settleOrderInfo.getMobile(),0));
                    settleOrderInfo.setInAccNo(StringUtil.sensitiveInformationHandle(settleOrderInfo.getInAccNo(),2));
                }

                //reLoadOrderInfo(settleOrderInfo);
                if (settleOrderInfo.getStatus() != null) {
                    settleOrderInfo.setStatusName(statusNameMap.get(settleOrderInfo.getStatus()));
                }
                if (settleOrderInfo.getSettleType() != null) {
                    settleOrderInfo.setSettleName(settleNameMap.get(settleOrderInfo.getSettleType()));
                }
                if (settleOrderInfo.getSettleStatus() != null) {
                    settleOrderInfo.setSettleStatusName(settleStatusNameMap.get(settleOrderInfo.getSettleStatus()));
                }
                String delivery = "旧模式";
                if(StringUtils.isNotBlank(settleOrderInfo.getDelivery()) && "1".equals(settleOrderInfo.getDelivery())){
                    delivery = "新模式";
                }
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",settleOrderInfo.getId());
                maps.put("accountSerialNo", null==settleOrderInfo.getAccountSerialNo()?"":settleOrderInfo.getAccountSerialNo());
                maps.put("transId", null==settleOrderInfo.getTransId()?"":settleOrderInfo.getTransId());
                maps.put("orderNo", null==settleOrderInfo.getOrderNo()?"":settleOrderInfo.getOrderNo());
                maps.put("delivery", delivery);
                maps.put("settleStatusName", null==settleOrderInfo.getSettleStatusName()?"":settleOrderInfo.getSettleStatusName());
                maps.put("statusName", null==settleOrderInfo.getStatusName()?"":settleOrderInfo.getStatusName());
                maps.put("settleName", null==settleOrderInfo.getSettleName()?"":settleOrderInfo.getSettleName());
                maps.put("agentNoOne", null==settleOrderInfo.getAgentNoOne()?"":settleOrderInfo.getAgentNoOne());
                maps.put("agentNameOne", null==settleOrderInfo.getAgentNameOne()?"":settleOrderInfo.getAgentNameOne());
                maps.put("settleUserNo", null==settleOrderInfo.getSettleUserNo()?"":settleOrderInfo.getSettleUserNo());
                maps.put("settleUserName", null==settleOrderInfo.getSettleUserName()?"":settleOrderInfo.getSettleUserName());
                maps.put("unionpayMerNo", null==settleOrderInfo.getUnionpayMerNo()?"":settleOrderInfo.getUnionpayMerNo());
                maps.put("mobile", null==settleOrderInfo.getMobile()?"":settleOrderInfo.getMobile());
                maps.put("amount", null==settleOrderInfo.getAmount()?"0.00":settleOrderInfo.getAmount().toString());
                maps.put("feeAmount", null==settleOrderInfo.getFeeAmount()?"0.00":settleOrderInfo.getFeeAmount().toString());
                maps.put("deductionFee", null==settleOrderInfo.getDeductionFee()?"0.00":settleOrderInfo.getDeductionFee().toString());
                maps.put("perAgentFee", null==settleOrderInfo.getPerAgentFee()?"0.00":settleOrderInfo.getPerAgentFee().toString());
                maps.put("outAmount", null==settleOrderInfo.getOutAmount()?"0.00":settleOrderInfo.getOutAmount().toString());
                maps.put("actualFee", null==settleOrderInfo.getActualFee()?"0.00":settleOrderInfo.getActualFee().toString());
                maps.put("acqEnname", null==settleOrderInfo.getAcqEnname()?"":settleOrderInfo.getAcqEnname());
                maps.put("outServiceName", null==settleOrderInfo.getOutServiceName()?"":settleOrderInfo.getOutServiceName());
                maps.put("sourceBatchNo", null==settleOrderInfo.getSourceBatchNo()?"":settleOrderInfo.getSourceBatchNo());
                maps.put("createTime", null==settleOrderInfo.getCreateTime()?"":sdf1.format(settleOrderInfo.getCreateTime()));
                maps.put("inAccName", null==settleOrderInfo.getInAccName()?"":settleOrderInfo.getInAccName());
                maps.put("inAccNo", null==settleOrderInfo.getInAccNo()?"":settleOrderInfo.getInAccNo());
                maps.put("inBankName", null==settleOrderInfo.getInBankName()?"":settleOrderInfo.getInBankName());
                data.add(maps);
            }
        }
        return data;
    }

}
