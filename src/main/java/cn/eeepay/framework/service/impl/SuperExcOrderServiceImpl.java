package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.SuperExcOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.service.SuperExcOrderService;
import cn.eeepay.framework.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("superExcOrderService")
public class SuperExcOrderServiceImpl implements SuperExcOrderService {

    @Resource
    private SuperExcOrderDao superExcOrderDao;

    @Override
    public List<OrderMain> selectOrderPage(OrderMain baseInfo, Page<OrderMain> page) {
        List<OrderMain> list=superExcOrderDao.selectOrderPage(baseInfo,page);
        for(OrderMain orderMain:list) {
            filteModel(orderMain);
        }
        return list;
    }

    @Override
    public OrderMainSum selectOrderSum(OrderMain baseInfo) {
        return superExcOrderDao.selectOrderSum(baseInfo);
    }

    @Override
    public OrderMain selectExcOrderDetail(String orderNo) {
        OrderMain orderMain = superExcOrderDao.selectSuperExcOrderDetail(orderNo);
        filteModel(orderMain);
        return orderMain;
    }


    @Override
    public String selectByOrgId(long orgId) {
        return superExcOrderDao.selectByOrgId(orgId);
    }

    public void filteModel(OrderMain orderMain) {
        Map<String, String> userTypeMap = getUserTypeMap();
        Map<String, String> bonusSettleTime = getBonusSettleTimeMap();
        orderMain.setOneUserType(userTypeMap.get(orderMain.getOneUserType()));
        orderMain.setTwoUserType(userTypeMap.get(orderMain.getTwoUserType()));
        orderMain.setThrUserType(userTypeMap.get(orderMain.getThrUserType()));
        orderMain.setFouUserType(userTypeMap.get(orderMain.getFouUserType()));
        orderMain.setStatus(getOrderStatusMap().get(orderMain.getStatus()));
        orderMain.setBonusSettleTime(bonusSettleTime.get(orderMain.getBonusSettleTime()));
        orderMain.setAccountStatus(getAccountStatusMap().get(orderMain.getAccountStatus()));
        orderMain.setCreateDateStr(DateUtils.format(orderMain.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
    }
    /**
     * 订单状态集合转换为map
     * @return
     */
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


    /**
     * 入账状态集合转换为map
     * @return
     */
    public Map<String, String> getAccountStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put(null, "待入账");
        map.put("", "待入账");
        map.put("0", "待入账");
        map.put("1", "已记账");
        map.put("2", "记账失败");
        return map;
    }

    /**
     * 订单状态集合转换为map
     * @return
     */
    public Map<String, String> getOrderStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "已创建");
        map.put("2", "待支付");
        map.put("3", "待审核");
        map.put("4", "已授权");
        map.put("5", "已完成");
        map.put("6", "审核不通过");
        map.put("7", "已办理过");
        map.put("9", "回收站");
        return map;
    }
    /**
     * 入账状态集合转换为map
     * @return
     */
    public Map<String, String> getBonusSettleTimeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "实时");
        map.put("2", "周结");
        map.put("3", "月结");
        return map;
    }

}
