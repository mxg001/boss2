package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.thread.ZqMerSyncTask;
import cn.eeepay.framework.dao.AcqOrgDao;
import cn.eeepay.framework.dao.ZqFileSyncDao;
import cn.eeepay.framework.dao.ZqFileSyncRecordDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.ZqFileSyncService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * 报报备 服务层实现
 * @author tans
 * @date 2019-04-11
 */
@Service
public class ZqFileSyncServiceImpl implements ZqFileSyncService{

    private static final Logger log = LoggerFactory.getLogger(ZqFileSyncServiceImpl.class);

    public static final String ZQ_FILE_BATCH_NO = "ZQ_FILE_BATCH_NO";

    public static final Map<String, String> statusMap = new HashMap<>();

    {
        statusMap.put("1", "报备中");
        statusMap.put("2", "报备完成");
    }

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SeqService seqService;

    @Resource
    private ZqFileRowImpl zqFileRowImpl;

    @Resource
    private ZqFileSyncDao zqFileSyncDao;

    @Resource
    private AcqOrgDao acqOrgDao;

    @Resource
    private ZqFileSyncRecordDao zqFileSyncRecordDao;

    /**
     * 条件查询报报备
     * @param zqFileSync
     * @return
     */
    @Override
    public void selectPage(Page<ZqFileSync> page, ZqFileSync zqFileSync) {
        zqFileSyncDao.selectPage(page, zqFileSync);
        List<ZqFileSync> list = page.getResult();
        if(list == null || list.isEmpty()){
            return;
        }
        for(ZqFileSync item: list){
            item.setStatusName(statusMap.get(item.getStatus()));
            item.setCreateTimeStr(DateUtil.getFormatDate(DateUtil.LONG_FROMATE, item.getCreateTime()));
            if(item.getLastUpdateTime()!=null){
                item.setLastUpdateTimeStr(DateUtil.getFormatDate(DateUtil.LONG_FROMATE, item.getLastUpdateTime()));
            }
            if(StringUtils.isNotBlank(item.getFileUrl())){
                item.setFileUrl(CommonUtil.getImgUrlAgent(item.getFileUrl()));
            }
        }
        return;
    }

    @Override
    public void export(HttpServletResponse response, ZqFileSync zqFileSync) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<ZqFileSync> page = new Page<>(0, Integer.MAX_VALUE);
            selectPage(page, zqFileSync);
            List<ZqFileSync> list = page.getResult();
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "直清商户批量报备"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<>() ;
            Map<String,String> map = null;
            for(ZqFileSync item: list){
                map = new HashMap<>();
                map.put("batchNo", item.getBatchNo());
                map.put("createTimeStr", item.getCreateTimeStr());
                map.put("channelCode", item.getChannelCode());
                map.put("statusName", item.getStatusName());
                map.put("fileName", item.getFileName());
                map.put("operator", item.getOperator());
                map.put("lastUpdateTimeStr", item.getLastUpdateTimeStr());
                data.add(map);
            }
            String[] cols = new String[]{
                    "batchNo","createTimeStr","channelCode","statusName","fileName",
                    "operator","lastUpdateTimeStr"
            };
            String[] colsName = new String[]{
                    "批次号","创建时间","收单机构","状态","文件名称",
                    "操作人","最后操作时间"
            };
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出直清商户批量报备异常", e);
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

    @Override
    public Result importData(MultipartFile file, String channelCode) {
        Result result = new Result();
        AcqOrg channel = acqOrgDao.selectInfoByName(channelCode);
        if(channel == null){
            result.setMsg("找不到对应的收单机构");
            return result;
        }
        try {
            zqFileRowImpl.setChannel(channelCode);
            List<ExcelErrorMsgBean> errors = new ArrayList<>();
            List<ZqServiceInfo> list = ExcelUtils.parseWorkbook(file.getInputStream(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    errors, zqFileRowImpl);
            int successSum = 0;
            if(list != null){
                successSum = list.size();
            }
            int failSum = 0;
            if(errors != null){
                failSum = errors.size();
            }
            String msg = "";
            //如果不符合条件的数据，将提示信息返回
            //只有全部成功，就插入导入的数据记录,并插入导入记录
            if (failSum > 0) {
                log.info("导入失败，原因:{}", JSONObject.toJSONString(errors));
                msg = "导入失败，第" + (errors.get(0).getRow()+1) + "行，" +
                                "第" + errors.get(0).getLine() + "列：" + errors.get(0).getMessage();
            } else {
                //校验，同一个渠道，有且只能有一个文件在报备中
                String status = "1";//报备中
                List<ZqFileSync> zqFileSyncList = zqFileSyncDao.selectListByStatus(status);
                if(zqFileSyncList != null && zqFileSyncList.size() > 0){
                    msg = "存在报备中的报表,不能继续导入";
                    return Result.fail(msg);
                }
                //上传文件、插入上传记录
                String batchNo = seqService.createKey(ZQ_FILE_BATCH_NO);
                uploadZqFile(file, batchNo, channelCode, successSum);
                //调用同步接口，批量同步
                syncZqMerBatch(list, batchNo);
                msg = "导入成功,请稍后查询同步结果,批次号:" + batchNo;
            }
            result.setStatus(true);
            result.setMsg(msg);
        } catch (Exception e){
            log.error("导入直清商户数据异常", e);
            throw new BossBaseException("导入直清商户数据异常");
        }
        return result;
    }

    @Override
    public Result updateStatus(ZqFileSync baseInfo) {
        String batchNo = baseInfo.getBatchNo();
        String status = baseInfo.getStatus();
        ZqFileSync item = zqFileSyncDao.select(batchNo);
        if(item == null){
            return Result.fail("找不到对应的批次号[" + batchNo + "]数据");
        }
        zqFileSyncDao.updateStatus(batchNo, status);
        return Result.success();
    }

    private void syncZqMerBatch(List<ZqServiceInfo> list, final String batchNo) {
        log.info("直清商户批量报件异步开始,批次号batchNo[{}]", batchNo);
        Queue<ZqServiceInfo> queue = new ArrayBlockingQueue<>(list.size());
        queue.addAll(list);

        SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
        String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
        Integer zqSyncThreadNum = 3;
        SysDict zqSyncThreadNumDict = sysDictService.getByKey("zq_sync_thread_num");
        if(zqSyncThreadNumDict != null){
            Integer sysDictNum = Integer.valueOf(zqSyncThreadNumDict.getSysValue());
            if(sysDictNum > 0 && sysDictNum <= 30) {
                zqSyncThreadNum = sysDictNum;
            }
        }

        List<Thread> threads = new ArrayList<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(zqSyncThreadNum, new Runnable() {
            @Override
            public void run() {
                zqFileSyncDao.updateStatus(batchNo, "2");
                log.info("直清商户批量报件异步结束,批次号batchNo[{}]", batchNo);
            }
        });
        for (int i = 0; i < zqSyncThreadNum; i ++) {
            threads.add(new Thread(new ZqMerSyncTask(queue, cyclicBarrier, accessUrl, batchNo, zqFileSyncRecordDao)));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    /**
     * 导入数据
     * 并插入导入记录
     * @param file
     * @param batchNo
     * @param channelCode
     * @param successSum
     * @return
     * @throws IOException
     */
    private int uploadZqFile(MultipartFile file, String batchNo, String channelCode, int successSum) throws IOException {
        String nowStr = DateUtil.getMessageTextTime();
        String fileName = file.getOriginalFilename();
        String fileUrl = fileName + nowStr + ".xlsx";
        ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_ATTCH_TUCKET,fileUrl,file.getInputStream());
        Date nowTime = new Date();

        ZqFileSync info = new ZqFileSync();
        info.setStatus("1");//状态,1:报备中,2:报备完成，默认报备中，导入进来之后，立马调用报备接口
        info.setBatchNo(batchNo);
        info.setChannelCode(channelCode);
        info.setFileName(fileName);
        info.setFileUrl(fileUrl);
        info.setInnerNum(successSum);
        info.setCreateTime(nowTime);
        info.setOperator(CommonUtil.getLoginUserName());
        info.setMessage("导入完成");
        return zqFileSyncDao.insert(info);
    }
}
