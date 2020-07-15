package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayChannel;
import cn.eeepay.framework.model.RepayPlanInfo;

import java.util.List;

/**
 * @author MXG
 * create 2018/08/29
 */
public interface RepayTypeManagerService {

    /**
     * 查询所有的订单类型
     * @param page
     * @param planType
     * @return
     */
    List<RepayPlanInfo> selectRepayTypeList(Page<RepayPlanInfo> page, String planType);

    /**
     * 根据id查询类型
     * @param id
     * @return
     */
    RepayPlanInfo queryTypeDetailById(String id);

    /**
     * 查找订单类型所具有的通道
     * @param type
     * @return
     */
    List<RepayChannel> selectChannelsByRepayType(String type);

    /**
     * 查找所有未绑定订单类型的通道
     * @return
     */
    List<RepayChannel> selectChannelsWithoutType();

    /**
     * 修改订单类型数据
     * @return
     */
    int updateRepayPlanInfo(RepayPlanInfo info);

    /**
     * 解除通道
     * @param id
     * @return
     */
    int relieve(String id);

    int updateChannel(RepayChannel channel);
}
