package cn.eeepay.framework.service.impl.cjt;

import cn.eeepay.framework.dao.AcqTerminalStoreDao;
import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.TerminalInfoDao;
import cn.eeepay.framework.dao.cjt.CjtGoodsDao;
import cn.eeepay.framework.dao.cjt.CjtOrderDao;
import cn.eeepay.framework.dao.cjt.CjtOrderSnDao;
import cn.eeepay.framework.dao.cjt.CjtTeamHardwareDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqTerminalStore;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.cjt.*;
import cn.eeepay.framework.service.cjt.CjtOrderService;
import cn.eeepay.framework.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商户购买订单 服务层实现
 * @author tans
 * @date 2019-05-30
 */
@Service
public class CjtOrderServiceImpl implements CjtOrderService {

    private static final Logger log = LoggerFactory.getLogger(CjtOrderServiceImpl.class);

    @Resource
    private CjtOrderDao cjtOrderDao;

    @Resource
    private CjtGoodsDao cjtGoodsDao;

    @Resource
    private TerminalInfoDao terminalInfoDao;

    @Resource
    private CjtTeamHardwareDao cjtTeamHardwareDao;

    @Resource
    private CjtOrderSnDao cjtOrderSnDao;

    @Resource
    private MerchantInfoDao merchantInfoDao;

    @Resource
    private AcqTerminalStoreDao acqTerminalStoreDao;

    /**
     * 条件查询商户购买订单
     * @param baseInfo
     * @return
     */
    @Override
    public void selectPage(Page<CjtOrder> page, CjtOrder baseInfo) {
        cjtOrderDao.selectPage(page, baseInfo);
        if(page == null || page.getResult() == null || page.getResult().size() < 1){
            return;
        }
        List<CjtOrder> orderList = page.getResult();

        Map<String, CjtGoods> goodsMap = getAllCjtGoodsMap();
        for(CjtOrder item: orderList) {
            filterDataFormat(goodsMap, item);
        }
        return;
    }

    /**
     * 过滤数据
     * @param goodsMap
     * @param item
     */
    private void filterDataFormat(Map<String, CjtGoods> goodsMap, CjtOrder item) {
        CjtGoods goods = goodsMap.get(item.getGoodsCode());
        item.setMainImgUrl1(goods != null ? goods.getMainImgUrl1() : "");
        item.setGoodsName(goods.getGoodsName());
        item.setCreateTimeStr(item.getCreateTime() != null ? DateUtil.getLongFormatDate(item.getCreateTime()) : "");
        item.setTransTimeStr(item.getTransTime() != null ? DateUtil.getLongFormatDate(item.getTransTime()): "");
        item.setLogisticsTimeStr(item.getLogisticsTime() != null ? DateUtil.getLongFormatDate(item.getLogisticsTime()) : "");
        item.setOrderStatusStr(CjtOrder.orderStatusMap.get(item.getOrderStatus()));
        item.setStatusStr(CjtAfterSale.statusMap.get(item.getStatus()));
        item.setTransStatusStr(CjtOrder.transStatusMap.get(item.getTransStatus()));
    }

    /**
     * 查询出所有商品的集合
     * @return
     */
    private Map<String, CjtGoods> getAllCjtGoodsMap() {

        List<CjtGoods> goodsList = cjtGoodsDao.selectList();
        Map<String, CjtGoods> goodsMap = new HashMap<>();
        if(goodsList != null && goodsList.size() > 0) {
            for(CjtGoods item: goodsList) {
                if(StringUtils.isNotEmpty(item.getMainImg())){
                    item.setMainImgUrl1(CommonUtil.getImgUrlAgent(item.getMainImg().split(",")[0]));
                }
                goodsMap.put(item.getGoodsCode(), item);
            }
        }
        return goodsMap;
    }

    /**
     * 查询商户购买订单
     * @param orderNo
     * @return
     */
    @Override
    public CjtOrder selectDetail(String orderNo) {
        return cjtOrderDao.selectDetail(orderNo);
    }

    @Override
    public Map<String, Object> selectTotalMap(CjtOrder baseInfo) {
        return cjtOrderDao.selectTotal(baseInfo);
    }

    @Override
    public void export(HttpServletResponse response, CjtOrder baseInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<CjtOrder> page = new Page<>(0, Integer.MAX_VALUE);
            selectPage(page, baseInfo);
            List<CjtOrder> list = page.getResult();
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "超级推机具申领订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<>();
            for(CjtOrder item: list){
                Map<String,String> map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("merchantNo", item.getMerchantNo());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("acqOrderNo", item.getAcqOrderNo());
//                map.put("mainImgUrl1", item.getMainImgUrl1());
                map.put("goodsName", item.getGoodsName());
                map.put("price", item.getPrice() != null ? item.getPrice().toString(): "");
                map.put("num", item.getNum() != null ? String.valueOf(item.getNum()): "");
                map.put("totalPrice", item.getTotalPrice()!= null ? item.getTotalPrice().toString(): "");
                map.put("orderStatusStr", item.getOrderStatusStr());
                map.put("statusStr", item.getStatusStr());
                String goodOrderType = item.getGoodOrderType()!= null ? item.getGoodOrderType().toString() : "";
                if(goodOrderType.equals("1")){
                        goodOrderType = "付费购买";
                }else if(goodOrderType.equals("2")){
                    goodOrderType = "免费申领";
                }else{
                    goodOrderType = "无";
                }
                map.put("goodOrderType", goodOrderType);
                map.put("receiver", item.getReceiver());
                map.put("receiverPhone", item.getReceiverPhone());
                map.put("receiveAddress", item.getReceiveAddress());
                map.put("remark", item.getRemark());
                map.put("transStatusStr", item.getTransStatusStr());
                map.put("transTimeStr", item.getTransTimeStr());
                map.put("createTimeStr", item.getCreateTimeStr());
                map.put("logisticsTimeStr", item.getLogisticsTimeStr());
                data.add(map);
            }
            String[] cols = new String[]{
                    "orderNo","merchantNo","payOrderNo","acqOrderNo","goodsName","price","num","totalPrice","orderStatusStr",
                    "statusStr","goodOrderType","receiver","receiverPhone","receiveAddress","remark",
                    "transStatusStr","transTimeStr","createTimeStr","logisticsTimeStr"
            };
            String[] colsName = new String[]{
                    "订单编号","商户编号","支付订单号","交易订单号","商品标题","商品销售价(元)","购买数量","订单金额(元)","订单状态",
                    "售后状态","申购类型","收件人","联系方式","收货地址","备注",
                    "支付状态","支付日期","下单日期","发货日期"
            };
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级推机具申领订单异常", e);
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

    /**
     * 机具发货
     * @param baseInfo
     * @return
     */
    @Override
    @Transactional
    public Result ship(CjtOrder baseInfo) {
        //1.校验机具发货的所有条件
        CjtOrder itemOrder = checkShip(baseInfo);
        //2.发货
        shipTer(itemOrder);
        return Result.success();
    }

    /**
     * 机具批量发货导入
     * @param file
     * @return
     */
    @Transactional
    @Override
    public Result importShip(MultipartFile file) {
        Workbook workBook = null;
        Result result = Result.fail();
        try {
            workBook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workBook.getSheetAt(0);
            if(sheet == null) {
                throw new BossBaseException("找不到对应的sheet，请使用模板进行上传");
            }
            int lastNum = sheet.getLastRowNum();
            if(lastNum <= 0){
                throw new BossBaseException("该sheet没有数据");
            }
            if(lastNum >= 10000){
                throw new BossBaseException("最多只能导入10000条数据,请分批导入");
            }
            int orderNoLine = 0;//订单数量所在列
            int numLine = 1;//购买数量所在列
            int logisticsCompanyLine = 2;//快递公司所在列
            int logisticsOrderNoLine = 3;//物流单号所在列
            int snStartLine = 4;//机具sn开始号所在列
            int snEndLine = 5;//机具sn结束号所在列

            List<CjtTeamHardware> cjtTeamHardwareList = cjtTeamHardwareDao.selectHpIdList();
            List<Integer> hpIdList = new ArrayList<>();
            for(CjtTeamHardware item: cjtTeamHardwareList) {
                hpIdList.add(item.getHpId());
            }

            List<CjtOrder> updateList = new ArrayList<>();
            List<CjtOrder> errorList = new ArrayList<>();
            for(int i = 1; i <= lastNum; i++){
                Row row = sheet.getRow(i);
                String orderNo = String.valueOf(row.getCell(orderNoLine));
                String numStr = String.valueOf(row.getCell(numLine));
                String logisticsCompany = String.valueOf(row.getCell(logisticsCompanyLine));
                String logisticsOrderNo = String.valueOf(row.getCell(logisticsOrderNoLine));
                String snStart = String.valueOf(row.getCell(snStartLine));
                String snEnd = String.valueOf(row.getCell(snEndLine));
                orderNo = StringUtil.getBeforePointStr(orderNo);
                numStr = StringUtil.getBeforePointStr(numStr);
                logisticsOrderNo = StringUtil.getBeforePointStr(logisticsOrderNo);
                snStart = StringUtil.getBeforePointStr(snStart);
                snEnd = StringUtil.getBeforePointStr(snEnd);
                //校验导入进来的数据
                CjtOrder itemOrder = new CjtOrder();
                String remark = checkImportData(hpIdList, i, orderNo, numStr, logisticsCompany,
                        logisticsOrderNo, snStart, snEnd, itemOrder);
                if(remark != null) {
                    CjtOrder errorOrder = new CjtOrder();
                    errorOrder.setOrderNo(orderNo);
                    errorOrder.setLogisticsOrderNo(logisticsOrderNo);
                    errorOrder.setLogisticsCompany(logisticsCompany);
                    errorOrder.setRemark(remark);
                    errorList.add(errorOrder);
                    continue;
                }

                itemOrder.setOrderNo(orderNo);
                itemOrder.setOrderStatus(CjtOrder.Order_Status_Shipped);
                itemOrder.setSnBegin(snStart);
                itemOrder.setSnEnd(snEnd);
                itemOrder.setLogisticsOrderNo(logisticsOrderNo);
                itemOrder.setLogisticsCompany(logisticsCompany);
                itemOrder.setLogisticsTime(new Date());
                itemOrder.setLogisticsOperator(CommonUtil.getLoginUserName());
                updateList.add(itemOrder);
            }
            //发货
            updateImportData(updateList);
            result = Result.success();
            Map<String, Object> map = new HashMap<>();
            map.put("errorList", errorList);
            map.put("failNum", errorList.size());
            map.put("successNum", updateList.size());
            result.setData(map);

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if(workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public void updateImportData(List<CjtOrder> updateList) {
        if(updateList.size() >0 ){
            for (CjtOrder itemOrder: updateList){
                //1.修改cjt_order，置为已发货
                cjtOrderDao.updateOrderStatus(itemOrder);

                //2.cjt_order_sn表插入记录
                insertBatchCjtOrderSn(itemOrder);

                //3.机具表，机具下发给商户对应的代理商
                updateBatchTerImport(itemOrder);
            }
        }
    }

    @Override
    public List<CjtOrderSn> selectCjtOrderSnList(String orderNo) {
        return cjtOrderSnDao.selectCjtOrderSnList(orderNo);
    }

    @Override
    public CjtOrder orderDetail(String orderNo) {
        Map<String, CjtGoods> goodsMap = getAllCjtGoodsMap();
        CjtOrder cjtOrder = cjtOrderDao.selectOrderDetail(orderNo);
        filterDataFormat(goodsMap, cjtOrder);
        return cjtOrder;
    }

    /**
     * 批量发货导入时，批量下发机具
     * @param itemOrder
     */
    private void updateBatchTerImport(CjtOrder itemOrder) {
        String merchantNo = itemOrder.getMerchantNo();
        MerchantInfo merchantInfo = merchantInfoDao.selectByMerchantNoAll(merchantNo);
        String agentNo = merchantInfo.getAgentNo();
        String agentNode = merchantInfo.getParentNode();
        terminalInfoDao.updateBatchIssuedSnRange(itemOrder.getSnBegin(), itemOrder.getSnEnd(), agentNo, agentNode, "1");
    }

    /**
     * 校验导入进来的数据
     * 校验机具发货的所有条件
     * 1.校验订单状态是否是待发货
     * 2.校验SN的硬件产品类型、活动类型：超级推，机具状态：已入库，机具未被购买（不在cjt_order_sn）
     * 3.符合条件的SN总数量，要和对应cjt_order表里面的num一致
     * @param hpIdList
     * @param i
     * @param orderNo
     * @param numStr
     * @param logisticsCompany
     * @param logisticsOrderNo
     * @param snStart
     * @param snEnd
     * @param baseInfo
     * @return
     */
    private String checkImportData(List<Integer> hpIdList, int i, String orderNo, String numStr,
                                   String logisticsCompany, String logisticsOrderNo, String snStart, String snEnd, CjtOrder baseInfo) {
        String remark = null;
        if(StringUtil.isEmpty(orderNo)) {
            remark = String.format("第%d行,订单号不能为空", i);
            return remark;
        }
        if(StringUtil.isEmpty(numStr)){
            remark = String.format("购买数量不能为空", i);
            return remark;
        }
        if(StringUtil.isEmpty(logisticsCompany)){
            remark = String.format("第%d行,快递公司不能为空", i);
            return remark;
        }
        if(StringUtil.isEmpty(logisticsOrderNo)){
            remark = String.format("第%d行,物流单号不能为空", i);
            return remark;
        }
        Integer num;
        if(StringUtil.isNumeric(numStr, false, 6,  0)){
            num = Integer.valueOf(numStr);
        } else {
            remark = String.format("第%d行,购买数量:%s,格式不正确", i, numStr);
            return remark;
        }

        //校验订单
        CjtOrder itemOrder = cjtOrderDao.selectDetail(orderNo);
        if(itemOrder == null){
            remark = String.format("第%d行,订单%s不存在", i, orderNo);
            return remark;
        }
        if(!CjtOrder.Order_Status_Not_Shipped.equals(itemOrder.getOrderStatus())){
            remark = String.format("第%d行,订单%s状态不是待发货", i, orderNo);
            return remark;
        }
        if(itemOrder.getNum() != num){
            remark = String.format("第%d行,购买数量和订单上购买数量不一致", i);
            return remark;
        }

        //校验SN的硬件产品类型、活动类型：超级推，机具状态：已入库，机具未被购买（不在cjt_order_sn）
        //1.机具未被购买，先去小表查询判断cjt_order_sn
        Integer countExists = cjtOrderSnDao.selectBySnStartAndEnd(snStart, snEnd);
        if(countExists != null && countExists > 0) {
            remark = String.format("第%d行,机具:%s~%s存在购买记录", i, snStart, snEnd);
            return remark;
        }

        //2.校验SN的硬件产品类型、活动类型：超级推，机具状态：已入库
        List<TerminalInfo> terList = terminalInfoDao.selectSnBy(snStart, snEnd, "0", hpIdList);
        if(terList == null || num != terList.size()) {
            remark = String.format("第%d行,符合条件的机具数和购买数量不一致", i);
            return remark;
        }

        //把需要的值，放在baseInfo里面，用baseInfo带回去
        List<String> snList = new ArrayList<>();
        for(TerminalInfo item: terList) {
            String sn = item.getSn();
            //3.机具终端号不能为空
            AcqTerminalStore acqTerminalStore = acqTerminalStoreDao.selectBySn(sn);
            if(acqTerminalStore == null || StringUtil.isEmpty(acqTerminalStore.getTerNo())) {
                remark = String.format("机具%s终端号不能为空", sn);
                return remark;
            }
            snList.add(sn);
        }
        baseInfo.setMerchantNo(itemOrder.getMerchantNo());
        baseInfo.setSnList(snList);
        return remark;
    }

    /**
     * 机具发货
     * 1.修改cjt_order，置为已发货
     * 2.cjt_order_sn表插入记录
     * 3.机具表，机具下发给商户对应的代理商
     * @param itemOrder
     */
    public void shipTer(CjtOrder itemOrder) {
        //1.修改cjt_order，置为已发货
        cjtOrderDao.updateOrderStatus(itemOrder);

        //2.cjt_order_sn表插入记录
        insertBatchCjtOrderSn(itemOrder);

        //3.机具表，机具下发给商户对应的代理商
        updateBatchTer(itemOrder);
    }

    /**
     * 批量下发机具
     * @param itemOrder
     */
    private void updateBatchTer(CjtOrder itemOrder) {
        List<String> snList = itemOrder.getSnList();
        String merchantNo = itemOrder.getMerchantNo();
        MerchantInfo merchantInfo = merchantInfoDao.selectByMerchantNoAll(merchantNo);
        if(merchantInfo == null) {
            throw new BossBaseException(String.format("商户号:%s,找不到对应的商户", merchantNo));
        }
        String agentNo = merchantInfo.getAgentNo();
        String agentNode = merchantInfo.getParentNode();

        if(snList.size() <= 300){
            terminalInfoDao.updateBatchIssued(snList, agentNo, agentNode, "1");
        } else {
            List<String> itemList = new ArrayList<>();
            for(String sn: snList){
                itemList.add(sn);
                if(itemList.size() % 300 == 0){
                    terminalInfoDao.updateBatchIssued(itemList, agentNo, agentNode, "1");
                    itemList.clear();
                }
            }
            if(itemList.size() > 0) {
                terminalInfoDao.updateBatchIssued(itemList, agentNo, agentNode, "1");
            }
        }
    }

    /**
     * 批量插入cjt_order_sn
     * @param itemOrder
     */
    private void insertBatchCjtOrderSn(CjtOrder itemOrder) {
        String orderNo = itemOrder.getOrderNo();
        String merchantNo = itemOrder.getMerchantNo();
        List<String> snList = itemOrder.getSnList();
        Date currentDate = new Date();
        if(snList.size() <= 300){
            cjtOrderSnDao.insertBatch(orderNo, merchantNo, currentDate, snList);
        } else {
            List<String> itemSnList = new ArrayList<>();
            for(String sn: snList) {
                itemSnList.add(sn);
                if(itemSnList.size() % 300 == 0){
                    cjtOrderSnDao.insertBatch(orderNo, merchantNo, currentDate, itemSnList);
                    itemSnList.clear();
                }
            }
            if(itemSnList.size() > 0){
                cjtOrderSnDao.insertBatch(orderNo, merchantNo, currentDate, itemSnList);
            }
        }

    }

    /**
     * 校验机具发货的所有条件
     * 1.校验订单状态是否是待发货
     * 2.符合条件的SN总数量，要和对应cjt_order表里面的num一致
     * 3.校验SN的硬件产品类型、活动类型：超级推，机具状态：已入库，机具未被购买（不在cjt_order_sn）
     *
     * @param baseInfo
     */
    private CjtOrder checkShip(CjtOrder baseInfo) {
        //1.校验订单状态是否是待发货
        String orderNo = baseInfo.getOrderNo();
        if(StringUtil.isEmpty(orderNo)) {
            throw new BossBaseException("订单号不能为空");
        }
        CjtOrder itemOrder = cjtOrderDao.selectDetail(orderNo);
        if(itemOrder == null){
            throw new BossBaseException(String.format("订单%s不存在", orderNo));
        }
        if(!CjtOrder.Order_Status_Not_Shipped.equals(itemOrder.getOrderStatus())){
            throw new BossBaseException(String.format("订单%s状态不是待发货", orderNo));
        }
        //2.符合条件的SN总数量，要和对应cjt_order表里面的num一致
        String sn = baseInfo.getSn();
        if(StringUtil.isEmpty(sn)) {
            throw new BossBaseException("sn不能为空");
        }
        String[] snArr = sn.split(",");
        if(snArr.length != itemOrder.getNum()) {
            throw new BossBaseException(
                    String.format("当前已选择%d台，订单购买数量为%d台，发货机具台数与订单订购数量不符，请确认选择的机具台数"
                    , snArr.length, itemOrder.getNum()));
        }
        //3.校验SN的硬件产品类型、活动类型：超级推，机具状态：已入库，机具未被购买（不在cjt_order_sn）
        List<CjtTeamHardware> cjtTeamHardwareList = cjtTeamHardwareDao.selectHpIdList();
        List<Integer> hpIdList = new ArrayList<>();
        for(CjtTeamHardware item: cjtTeamHardwareList) {
            hpIdList.add(item.getHpId());
        }
        for(String itemSn: snArr){
            checkShipSn(itemSn, hpIdList);
        }
        itemOrder.setOrderStatus(CjtOrder.Order_Status_Shipped);
        itemOrder.setSnList(Arrays.asList(snArr));
        itemOrder.setLogisticsOrderNo(baseInfo.getLogisticsOrderNo());
        itemOrder.setLogisticsCompany(baseInfo.getLogisticsCompany());
        itemOrder.setLogisticsTime(new Date());
        itemOrder.setLogisticsOperator(CommonUtil.getLoginUserName());
        return itemOrder;
    }

    /**
     * 校验SN的硬件产品类型、活动类型：超级推，机具状态：已入库，机具未被购买（不在cjt_order_sn）
     * @param sn
     * @param hpIdList
     */
    private void checkShipSn(String sn, List<Integer> hpIdList) {
        TerminalInfo terInfo = terminalInfoDao.getTerminalInfoBySn(sn);
        if(terInfo == null){
            throw new BossBaseException(String.format("找不到对应的机具,sn:%s", sn));
        }
        if(!"0".equals(terInfo.getOpenStatus())){
            throw new BossBaseException(String.format("机具%s状态不是已入库", sn));
        }
        String type = terInfo.getType();//机具硬件类型
        if(!hpIdList.contains(Integer.valueOf(type))) {
            throw new BossBaseException(String.format("机具%s硬件类型不是盛钱包超级推", sn));
        }
//        String activityType = terInfo.getActivityType();
//        if(activityType == null || !activityType.contains(CjtConstant.CJT_ACTIVITY_TYPE)) {
//            throw new BossBaseException(String.format("机具%s活动类型不是超级推", sn));
//        }
        CjtOrderSn cjtOrderSn = cjtOrderSnDao.selectBySn(sn);
        if(cjtOrderSn != null) {
            throw new BossBaseException(String.format("机具%s存在购买记录", sn));
        }
        AcqTerminalStore acqTerminalStore = acqTerminalStoreDao.selectBySn(sn);
        if(acqTerminalStore == null || StringUtil.isEmpty(acqTerminalStore.getTerNo())) {
            throw new BossBaseException(String.format("机具%s终端号不能为空", sn));
        }
    }




}
