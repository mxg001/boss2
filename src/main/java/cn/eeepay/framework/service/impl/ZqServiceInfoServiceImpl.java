package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ZqServiceInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.ZqServiceInfo;
import cn.eeepay.framework.service.BusinessProductDefineService;
import cn.eeepay.framework.service.ServiceProService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.ZqServiceInfoService;
import cn.eeepay.framework.util.*;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 直清商户服务报件 服务层实现
 * @author tans
 * @date 2019-04-02
 */
@Service
public class ZqServiceInfoServiceImpl implements ZqServiceInfoService{

    public static final Map<String, String> statusMap = new HashMap<>();
    public static final Map<String, String> dealStatusMap = new HashMap<>();

    {
        statusMap.put("1", "失败");
        statusMap.put("2", "成功");
        statusMap.put("3", "同步审核中");
        statusMap.put("4", "禁用");

        dealStatusMap.put("0", "初始化");
        dealStatusMap.put("1", "未处理");
        dealStatusMap.put("2", "处理成功");
        dealStatusMap.put("3", "处理失败");
    }

    private static final Logger log = LoggerFactory.getLogger(ZqServiceInfoServiceImpl.class);

    @Resource
    private ServiceProService serviceProService;

    @Resource
    private BusinessProductDefineService businessProductDefineService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private ZqServiceInfoDao zqServiceInfoDao;

    /**
     * 条件查询直清商户服务报件
     * @param zqServiceInfo
     * @return
     */
    @Override
    public void selectZqServiceInfoPage(Page<ZqServiceInfo> page, ZqServiceInfo zqServiceInfo) {
        if(StringUtils.isBlank(zqServiceInfo.getMerchantNo()) && StringUtils.isBlank(zqServiceInfo.getMobilephone())){
            DateUtil.checkOrderDate(zqServiceInfo.getCreateTimeStart(), zqServiceInfo.getCreateTimeEnd(), 90);
        }
//        DateUtil.checkOrderDate(zqServiceInfo.getLastUpdateTimeStart(), zqServiceInfo.getLastUpdateTimeEnd(), 90);
        zqServiceInfoDao.selectZqServiceInfoPage(page, zqServiceInfo);
        List<ZqServiceInfo> list = page.getResult();
        if(list == null || list.isEmpty()){
            return;
        }
        Map<Long, String> serviceNameMap = serviceProService.selectServiceNameMap();
        Map<Long, String> bpNameMap = businessProductDefineService.selectBpNameMap();
        for(ZqServiceInfo item: list){
            item.setServiceName(serviceNameMap.get(item.getServiceId()));
            item.setBpName(bpNameMap.get(item.getBpId()));
            item.setStatusName(statusMap.get(item.getStatus()));
            if(StringUtils.isNotBlank(item.getDealStatus())){
                item.setDealStatusName(dealStatusMap.get(item.getDealStatus()));
            }
            item.setCreateTimeStr(DateUtil.getFormatDate(DateUtil.LONG_FROMATE, item.getCreateTime()));
            item.setLastUpdateTimeStr(DateUtil.getFormatDate(DateUtil.LONG_FROMATE, item.getLastUpdateTime()));
            item.setMobilephone(StrUtil.hide(item.getMobilephone(),3,7));
        }
        return;
    }

    /**
     * 查询直清商户服务报件
     * @param id
     * @return
     */
    @Override
    public ZqServiceInfo selectZqServiceInfoById(Long id) {
        return zqServiceInfoDao.selectZqServiceInfoById(id);
    }

    @Override
    public void export(HttpServletResponse response, ZqServiceInfo zqServiceInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<ZqServiceInfo> page = new Page<>(0, Integer.MAX_VALUE);
            selectZqServiceInfoPage(page, zqServiceInfo);
            List<ZqServiceInfo> list = page.getResult();
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "直清服务报件商户"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<>() ;
            Map<String,String> map = null;
            for(ZqServiceInfo item: list){
                map = new HashMap<>();
                map.put("createTimeStr", item.getCreateTimeStr());
                map.put("mbpId", String.valueOf(item.getMbpId()));
                map.put("merchantNo", item.getMerchantNo());
                map.put("merchantName", item.getMerchantName());
                map.put("mobilephone", item.getMobilephone());
                map.put("bpName", item.getBpName());
                map.put("serviceName", item.getServiceName());
                map.put("channelCode", item.getChannelCode());
                map.put("statusName", item.getStatusName());
                map.put("msg", item.getMsg());
                map.put("unionpayMerNo", item.getUnionpayMerNo());
                map.put("operator", item.getOperator());
                map.put("lastUpdateTimeStr", item.getLastUpdateTimeStr());
                map.put("acqServiceMerNo", item.getAcqServiceMerNo());
                map.put("merWxNo", item.getMerWxNo());
                map.put("merRealName", item.getMerRealName());
                map.put("dealStatusName", item.getDealStatusName());
                map.put("dealOperator", item.getDealOperator());
                data.add(map);
            }
            String[] cols = new String[]{
                    "createTimeStr","mbpId","merchantNo","merchantName","mobilephone","bpName","serviceName",
                    "channelCode","statusName","msg","unionpayMerNo","operator","lastUpdateTimeStr",
                    "acqServiceMerNo","merWxNo","merRealName","dealStatusName","dealOperator"
                    };
            String[] colsName = new String[]{
                    "创建时间","商户进件编号","商户编号","商户名称","手机号","业务产品","服务名称",
                    "通道名称","服务报备状态","备注","直清商户号","操作人","最后报件时间",
                    "上游商户号","微信号","真实姓名","业务处理状态","业务处理操作人"
                    };
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出直清服务报件商户异常", e);
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

    /**
     * 直清商户报件
     * @param list
     * @return
     */
    @Override
    public Result zqSyncSerBatch(List<ZqServiceInfo> list) {
        if(list == null || list.isEmpty()){
            return Result.fail("参数不能为空");
        }
        Result result = new Result();
        int successNum = 0;
        int failNum = 0;
        SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
        String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
        for(ZqServiceInfo zqServiceInfo: list){
            //商户报件
            Result singleResult = zqSyncSer(zqServiceInfo, accessUrl);
            if(singleResult != null && singleResult.isStatus()){
                successNum++;
                continue;
            }
            failNum++;
        }
        StringBuilder msg = new StringBuilder();
        if(successNum > 0){
            result.setStatus(true);
            msg.append("操作成功");
        } else {
            result.setStatus(false);
            msg.append("操作失败");
        }
        msg.append(",成功条数:").append(successNum).append(",失败条数:").append(failNum);
        result.setMsg(msg.toString());
        return result;
    }

    @Override
    public Result zqSyncSer(ZqServiceInfo zqServiceInfo, String accessUrl) {
        Result result = new Result();
        String merchantNo = zqServiceInfo.getMerchantNo();
        String channelCode = zqServiceInfo.getChannelCode();
        Long bpId = zqServiceInfo.getBpId();

        //校验商户是否需要报备，如果已报备成功，则返回
        ZqServiceInfo itemInfo = zqServiceInfoDao.selectStatus(zqServiceInfo);
        if(itemInfo == null){
            return Result.fail("找不到对应的记录");
        }
        if("2".equals(itemInfo.getStatus())){
            return Result.success("该商户报备状态是成功的,无需再次报备");
        }

        //商户报备
        Map<String, Object> marMap = new HashMap<String, Object>();
        List<String> channelList = new ArrayList<>();
        channelList.add(channelCode);
        marMap.put("merchantNo", merchantNo);
        marMap.put("bpId", bpId);
        marMap.put("operator", CommonUtil.getLoginUserName());
        marMap.put("changeSettleCard", "0");
        marMap.put("channelCode", channelList);
        marMap.put("newspaperReport", "1");//这次中钢需求新加的参数
        String paramStr = JSON.toJSONString(marMap);
        log.info("直清商户报件zqSyncSer start,url:{},param:{}", accessUrl, paramStr);
        String resultStr=new ClientInterface(accessUrl, null).postRequestBody(paramStr);
        log.info("直清商户报件zqSyncSer end,result:{}", resultStr);
        if(StringUtil.isEmpty(resultStr)){
            return Result.fail("直清商户报件失败,返回结果为空");
        }
        JSONObject resJson = JSONObject.parseObject(resultStr);
        resJson = resJson.getJSONObject("header");
        if(resJson.getBoolean("succeed")){
            result.setStatus(true);
            result.setMsg("同步成功");
        } else {
            result.setStatus(false);
            result.setMsg(resJson.getString("errMsg"));
        }
        return result;
    }

    @Override
    public Result updateDealStatus(ZqServiceInfo baseInfo) {
        Long id = baseInfo.getId();
        String dealStatus = baseInfo.getDealStatus();
        ZqServiceInfo item = zqServiceInfoDao.selectZqServiceInfoById(id);
        if(item == null){
            return Result.fail("找不到对应的ID[" + id + "]数据");
        }
        String loginName = CommonUtil.getLoginUserName();
        zqServiceInfoDao.updateDealStatus(id, dealStatus, loginName);
        return Result.success();
    }

}
