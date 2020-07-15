package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.ZxProductOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.ZxProductOrder;
import cn.eeepay.framework.provider.ZxApplyProductProvider;
import cn.eeepay.framework.rpc.entity.ZxApplyProductsEntity;
import cn.eeepay.framework.rpc.response.BaseResponse;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.ZxProductOrderService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZxProductOrderServiceImpl implements ZxProductOrderService {

    @Resource
    private ZxProductOrderDao zxProductOrderDao;

    @Resource
    private ZxApplyProductProvider zxApplyProductProvider;

    @Resource
    private SysDictService sysDictService;

    @Override
    public List selectByPage(ZxProductOrder record, Page<ZxProductOrder> page) {
        List<ZxProductOrder> zxProductOrders = zxProductOrderDao.selectByPage(record, page);
        for(ZxProductOrder zxProductOrder:zxProductOrders){
            copyProductOrder(zxProductOrder);
        }
        return zxProductOrders;
    }


    @Override
    public ZxProductOrder selectByOrderNo(String orderNo) {
        ZxProductOrder zxProductOrder = zxProductOrderDao.selectByOrderNo(orderNo);
        copyProductOrder(zxProductOrder);
        return zxProductOrder;
    }

    @Override
    public OrderMainSum selectOrderSum(ZxProductOrder record) {
        OrderMainSum orderMainSum= zxProductOrderDao.selectOrderSum(record) ;
        if(orderMainSum==null){
            orderMainSum=new OrderMainSum();
        }
        return orderMainSum;
    }

    @Override
    public List<ZxApplyProductsEntity> selectProductAll() {
        BaseResponse response = zxApplyProductProvider.selectAllPrice();
        return (List<ZxApplyProductsEntity>) response.getData();
    }

    void copyProductOrder(ZxProductOrder zxProductOrder){
        SysDict sysDict = sysDictService.getByKey("CREDIT_REPORT_URL");
        Map<String, String> userTypeMap = getUserTypeMap();
        zxProductOrder.setOneUserType(userTypeMap.get(zxProductOrder.getOneUserType()));
        zxProductOrder.setTwoUserType(userTypeMap.get(zxProductOrder.getTwoUserType()));
        zxProductOrder.setThrUserType(userTypeMap.get(zxProductOrder.getThrUserType()));
        zxProductOrder.setFouUserType(userTypeMap.get(zxProductOrder.getFouUserType()));
        if("1".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("待支付");
        }
        if("2".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("已支付");
        }
        if("3".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("已完成");
            String url=null;
            if("1".equals(zxProductOrder.getReportType())){
                url=sysDict.getSysValue()+Constants.DEPTH_REPORT_TYPE+zxProductOrder.getOrderNo();
            }
            if("2".equals(zxProductOrder.getReportType())){
                url=sysDict.getSysValue()+Constants.BLACKLIST_MULTIHEAD_TYPE+zxProductOrder.getOrderNo();
            }
            if("3".equals(zxProductOrder.getReportType())){
                url=sysDict.getSysValue()+Constants.CREDIT_RATING_TYPE+zxProductOrder.getOrderNo();
            }
            zxProductOrder.setReportUrl(url);
        }
        if("4".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("生成失败（退款处理中）");
        }
        if("5".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("退款成功");
        }
        if("6".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("退款失败");
        }
        if("7".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("已失效");
        }
        if("1".equals(zxProductOrder.getPayMethod())){
            zxProductOrder.setPayMethod("微信");
        }
        if("2".equals(zxProductOrder.getPayMethod())){
            zxProductOrder.setPayMethod("支付宝");
        }
        if("1".equals(zxProductOrder.getPayMethod())){
            zxProductOrder.setPayMethod("快捷");
        }
        if(zxProductOrder.getCreateTime()!=null){
            zxProductOrder.setCreateTimeStr(DateUtils.format(zxProductOrder.getCreateTime(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getPayDate()!=null){
            zxProductOrder.setPayDateStr(DateUtils.format(zxProductOrder.getPayDate(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getGenerationTime()!=null){
            zxProductOrder.setGenerationTimeStr(DateUtils.format(zxProductOrder.getGenerationTime(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getExpiryTime()!=null){
            zxProductOrder.setExpiryTimeStr(DateUtils.format(zxProductOrder.getExpiryTime(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getRefundTime()!=null){
            zxProductOrder.setRefundTimeStr(DateUtils.format(zxProductOrder.getRefundTime(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getProfitDate()!=null){
            zxProductOrder.setProfitDateStr(DateUtils.format(zxProductOrder.getProfitDate(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getReceiveTime()!=null){
            zxProductOrder.setReceiveTimeStr(DateUtils.format(zxProductOrder.getReceiveTime(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(1==zxProductOrder.getRateType()){
         zxProductOrder.setRate(zxProductOrder.getRate());
        }
        if(2==zxProductOrder.getRateType()){
          zxProductOrder.setRate(zxProductOrder.getRate()+"%");
        }
        if("0".equals(zxProductOrder.getAccountStatus())){
            zxProductOrder.setAccountStatus("待入账");
        }
        if("1".equals(zxProductOrder.getAccountStatus())){
            zxProductOrder.setAccountStatus("已记账");
        }
        if("2".equals(zxProductOrder.getAccountStatus())){
            zxProductOrder.setAccountStatus("记账失败");
        }
        if("1".equals(zxProductOrder.getReportType())){
            zxProductOrder.setReportTypeName("深度报告类型");
        }
        if("2".equals(zxProductOrder.getReportType())){
            zxProductOrder.setReportTypeName("黑名单多头类型");
        }
        if("3".equals(zxProductOrder.getReportType())){
            zxProductOrder.setReportTypeName("信用评测类型");
        }
        if(1==zxProductOrder.getRateType()){
            zxProductOrder.setRate(zxProductOrder.getRate()+"元");
        }if(2==zxProductOrder.getRateType()){
            zxProductOrder.setRate(zxProductOrder.getRate()+"%");
        }
    }

    public Map<String, String> getUserTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("10", "用户");
        map.put("20", "专员");
        map.put("30", "经理");
        map.put("40", "银行家");
        map.put("50", "OEM");
        map.put("60", "平台");
        return map;
    }
}
