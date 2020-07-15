package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.AccountRecordService;
import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.util.ClientInterface;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @date 2019/2/27 17:15
 */
@Service("accountRecordService")
public class AccountRecordServiceImpl implements AccountRecordService {
    @Resource
    private TransInfoService transInfoService;

    @Resource
    private SysDictDao sysDictDao;
    @Resource
    private TransInfoDao transInfoDao;


    private static final Logger log = LoggerFactory.getLogger(AccountRecordServiceImpl.class);

    @Override
    public void accountRecordTask(){
        log.info("定时记账accountRecordTask start");
        int accountRecordDay = 1;
        SysDict sysDict = sysDictDao.getByKey("ACCOUNT_RECORD_DAY");
        if(sysDict != null && StringUtils.isNotBlank(sysDict.getSysValue())){
            String sysValue = sysDict.getSysValue();
            if(StringUtils.isNumeric(sysValue)){
                accountRecordDay = Integer.parseInt(sysValue);
                if(accountRecordDay > 365){
                    accountRecordDay = 365;
                }
                if(accountRecordDay < 0){
                    accountRecordDay = 1;
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        Long nowTime = nowDate.getTime() - accountRecordDay*24*60*60*1000L;
        Date yesDate = new Date(nowTime);
        log.info("处理订单的创建时间条件： create_time >= {}", sdf.format(yesDate));
        List<CollectiveTransOrder> transList = transInfoDao.selectRecordAccountFail(sdf.format(yesDate));

        for(CollectiveTransOrder info: transList){
            accountRecordInfo(info);
        }
        log.info("定时记账accountRecordTask end");
    }

    @Override
    public int accountRecordInfo(CollectiveTransOrder info){
        try {
            if("5".equals(info.getSettleStatus())&&"欢乐返首笔激活".equals(info.getRemark())){
                String msg= ClientInterface.hlfRecode(info);
                Map<String, Object> result = JSON.parseObject(msg);
                if(result!=null&&(Boolean)result.get("status")){
                    info.setAccount("1");
                    info.setTransMsg(String.valueOf(result.get("msg")));
                    transInfoDao.updateAccount(info);
                    return 1;
                }else{
                    info.setTransMsg(String.valueOf(result.get("msg")));
                    transInfoDao.updateAccount(info);
                    return 0;
                }
            }
            if(info.getAgentNode()!=null){
                String[] agentArr = info.getAgentNode().split("-");
                info.setOneAgentNo(agentArr[1]);
                info.setAgentNo(agentArr[agentArr.length-1]);
            }
            String msg = ClientInterface.httpRecordAccount(info);
            System.out.println(msg);
            Map<String, Object> result = JSON.parseObject(msg);
            if(result!=null){
                boolean status=(Boolean)result.get("status");
                if(status){
                    info.setAccount("1");
                    transInfoDao.updateAccount(info);
                    //2.2.5 添加预冻结金额判断开始
                    Map<String, Object> map = transInfoService.judgePreFreezeaMountAngFreezaTrans(info);
                    log.info("预冻结返回结果： " + map.toString());
                    //2.2.5 添加预冻结金额判断结束
                    return 2;
                } else {
                    info.setAccount("2");
                }
            }
        } catch (Exception e) {
            log.info("我们的订单号：" + info.getOrderNo()+ ";调用记账接口异常");
            log.error("定时记账异常", e);
        }
        return 0;
    }
}
