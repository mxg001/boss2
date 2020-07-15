package cn.eeepay.framework.service.impl.cjt;

import cn.eeepay.framework.dao.cjt.CjtAfterSaleDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtAfterSale;
import cn.eeepay.framework.model.cjt.CjtOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.cjt.CjtAfterSaleService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
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
 * 售后订单 服务层实现
 * @author tans
 * @date 2019-06-06
 */
@Service
public class CjtAfterSaleServiceImpl implements CjtAfterSaleService {

    private static Logger log = LoggerFactory.getLogger(CjtAfterSaleServiceImpl.class);

    @Resource
    private CjtAfterSaleDao cjtAfterSaleDao;

    @Resource
    private SysDictService sysDictService;

    /**
     * 条件查询售后订单
     * @param cjtAfterSale
     * @return
     */
    @Override
    public void selectPage(Page<CjtAfterSale> page, CjtAfterSale cjtAfterSale) {
        cjtAfterSaleDao.selectPage(page, cjtAfterSale);
        if(page != null && page.getResult() != null && page.getResult().size() > 0) {
            Map<String, String> afterSaleTypeMap = sysDictService.selectMapByKey(CjtAfterSale.AFTER_SALE_TYPE);
            List<CjtAfterSale> list = page.getResult();
            for(CjtAfterSale item: list) {
                transFormatData(afterSaleTypeMap, item);
            }
        }
        return;
    }

    /**
     * 转换数据格式
     * @param afterSaleTypeMap
     * @param item
     */
    private void transFormatData(Map<String, String> afterSaleTypeMap, CjtAfterSale item) {
        if(StringUtils.isNotEmpty(item.getApplyImg())){
            String[] applyArr = item.getApplyImg().split(",");
            if(applyArr.length >= 1) {
                item.setApplyImgUrl1(CommonUtil.getImgUrlAgent(applyArr[0]));
            }
            if(applyArr.length >= 2) {
                item.setApplyImgUrl2(CommonUtil.getImgUrlAgent(applyArr[1]));
            }
            if(applyArr.length >= 3) {
                item.setApplyImgUrl3(CommonUtil.getImgUrlAgent(applyArr[2]));
            }
        }
        if(StringUtils.isNotEmpty(item.getDealImg())){
            String[] dealArr = item.getDealImg().split(",");
            if(dealArr.length >= 1) {
                item.setDealImgUrl1(CommonUtil.getImgUrlAgent(dealArr[0]));
            }
            if(dealArr.length >= 2) {
                item.setDealImgUrl2(CommonUtil.getImgUrlAgent(dealArr[1]));
            }
            if(dealArr.length >= 3) {
                item.setDealImgUrl3(CommonUtil.getImgUrlAgent(dealArr[2]));
            }
        }
        if(item.getCreateTime() != null){
            item.setCreateTimeStr(DateUtil.getLongFormatDate(item.getCreateTime()));
        }
        if(item.getDealTime() != null){
            item.setDealTimeStr(DateUtil.getLongFormatDate(item.getDealTime()));
        }
        if(StringUtils.isNotEmpty(item.getStatus())) {
            item.setStatusStr(CjtAfterSale.statusMap.get(item.getStatus()));
        }
        if(StringUtils.isNotEmpty(item.getAfterSaleType()) && afterSaleTypeMap != null) {
            item.setAfterSaleTypeStr(afterSaleTypeMap.get(item.getAfterSaleType()));
        }
    }

    /**
     * 导出超级推申领售后订单
     * @param response
     * @param baseInfo
     */
    @Override
    public void export(HttpServletResponse response, CjtAfterSale baseInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<CjtAfterSale> page = new Page<>(0, Integer.MAX_VALUE);
            selectPage(page, baseInfo);
            List<CjtAfterSale> list = page.getResult();
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "超级推申领售后订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<>() ;
            for(CjtAfterSale item: list){
                Map<String,String> map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("serviceOrderNo", item.getServiceOrderNo());
                map.put("afterSaleTypeStr", item.getAfterSaleTypeStr());
                map.put("applyRemark", item.getApplyRemark());
                map.put("statusStr", item.getStatusStr());
                map.put("dealRemark", item.getDealRemark());
                map.put("createTimeStr", item.getCreateTimeStr());
                map.put("dealPerson", item.getDealPerson());
                map.put("dealTimeStr", item.getDealTimeStr());

//                String applyImg = "";
//                if(StringUtils.isNotEmpty(item.getApplyImgUrl1())) {
//                    applyImg += item.getApplyImgUrl1() + ";";
//                }
//                if(StringUtils.isNotEmpty(item.getApplyImgUrl2())) {
//                    applyImg += item.getApplyImgUrl2() + ";";
//                }
//                if(StringUtils.isNotEmpty(item.getApplyImgUrl3())) {
//                    applyImg += item.getApplyImgUrl3() + ";";
//                }
//                map.put("applyImg", applyImg);

//                String dealImg = "";
//                if(StringUtils.isNotEmpty(item.getDealImgUrl1())) {
//                    dealImg += item.getDealImgUrl1() + ";";
//                }
//                if(StringUtils.isNotEmpty(item.getDealImgUrl2())) {
//                    dealImg += item.getDealImgUrl2() + ";";
//                }
//                if(StringUtils.isNotEmpty(item.getDealImgUrl3())) {
//                    dealImg += item.getDealImgUrl3() + ";";
//                }
//                map.put("dealImg", dealImg);


                data.add(map);
            }
            String[] cols = new String[]{
                    "orderNo","serviceOrderNo","afterSaleTypeStr","applyRemark","statusStr",
                    "dealRemark", "createTimeStr","dealPerson","dealTimeStr"
            };
            String[] colsName = new String[]{
                    "售后编号","关联订单编号","售后类型","售后说明","售后状态",
                    "平台处理结果","提交日期","处理人","处理日期"
            };
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级推申领售后订单异常", e);
        } finally {
            if(ouputStream!=null){
                try {
                    ouputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Map<String, Object> selectTotal(CjtAfterSale baseInfo) {
        baseInfo.setNoDealTimeStr(DateUtil.getBeforeDate(new Date(), 7));
        return cjtAfterSaleDao.selectTotal(baseInfo);
    }

    @Override
    public int updateStatus(String orderNo, String status) {
        return cjtAfterSaleDao.updateStatus(orderNo, status);
    }

    @Override
    public int deal(CjtAfterSale baseInfo) {
        baseInfo.setDealTime(new Date());
        baseInfo.setDealPerson(CommonUtil.getLoginUserName());
        baseInfo.setStatus(CjtAfterSale.Status_Success);
        return cjtAfterSaleDao.update(baseInfo);
    }

    /**
     * 校验订单是否已取消，如果已取消，则无需进行售后
     * @param orderNo
     */
    @Override
    public void checkStatus(String orderNo) {
        CjtAfterSale baseInfo = cjtAfterSaleDao.select(orderNo);
        if(baseInfo == null) {
            throw new BossBaseException("找不到对应的订单,售后订单编号:" + orderNo);
        }
        if(CjtAfterSale.Status_Closed.equals(baseInfo.getStatus())) {
            throw new BossBaseException("该用户已经取消售后,您无需再处理此售后");
        }
    }


}
