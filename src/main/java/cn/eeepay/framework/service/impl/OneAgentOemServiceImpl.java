package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.OneAgentOemDao;
import cn.eeepay.framework.service.OneAgentOemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/10/010.
 * @author  liuks
 * OEM下的一级代理关系表维护
 */
@Service("oneAgentOemService")
public class OneAgentOemServiceImpl implements OneAgentOemService {

    @Resource
    private OneAgentOemDao oneAgentOemDao;

    @Override
    public int insertOneAgentNo(String oneAgentNo, String oemType) {
        int num=0;
        try {
            List<Map<Object,Object>> list= oneAgentOemDao.getOneAgentNo(oneAgentNo);
            if(list==null||list.size()==0){
                num=oneAgentOemDao.insertOneAgentNo(oneAgentNo,oemType);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public int insertOneAgentShare(String agentNo, BigDecimal share, BigDecimal fee,BigDecimal receiveShare,BigDecimal repaymentShare) {
        int num=0;
        List<Map<Object,Object>> list=oneAgentOemDao.checkAgent(agentNo);
        if(list==null||list.size()==0){
            num=oneAgentOemDao.insertOneAgentShare(agentNo,share,fee,receiveShare,repaymentShare);
        }else{
            num=oneAgentOemDao.updateOneAgentShare(agentNo,share,fee,receiveShare,repaymentShare);
        }
        return num;
    }
}
