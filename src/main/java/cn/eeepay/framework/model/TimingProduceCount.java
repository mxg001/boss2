package cn.eeepay.framework.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/15/015.
 * @author  liuks
 * 交易集合合集
 */
public class TimingProduceCount {
    private Integer total;//笔数

    private Integer acqOrgId;//收单机构ID

    private Integer acqServiceId;//收单服务id

    private List<TimingProduce> timingProduceList=new ArrayList<TimingProduce>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAcqOrgId() {
        return acqOrgId;
    }

    public void setAcqOrgId(Integer acqOrgId) {
        this.acqOrgId = acqOrgId;
    }

    public Integer getAcqServiceId() {
        return acqServiceId;
    }

    public void setAcqServiceId(Integer acqServiceId) {
        this.acqServiceId = acqServiceId;
    }

    public List<TimingProduce> getTimingProduceList() {
        return timingProduceList;
    }

    public void setTimingProduceList(List<TimingProduce> timingProduceList) {
        this.timingProduceList = timingProduceList;
    }
}
