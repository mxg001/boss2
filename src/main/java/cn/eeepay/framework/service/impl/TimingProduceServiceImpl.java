package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.TimingProduceDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TimingProduceService;
import cn.eeepay.framework.service.WarningEventsService;
import cn.eeepay.framework.service.WarningPeopleService;
import cn.eeepay.framework.service.WarningSetService;
import cn.eeepay.framework.util.RandomNumber;
import cn.eeepay.framework.util.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/1/11/011.
 * @author  liuks
 * 定时预警service
 *
 */
@Service("timingProduceService")
public class TimingProduceServiceImpl  implements TimingProduceService {

    private final Logger log = LoggerFactory.getLogger(TimingProduceServiceImpl.class);

    public static final String TIMING_CYCLE_TRANSACTION="timing_cycle_transaction";//交易预警定时周期
    public static final String TIMING_DELAY_TRANSACTION="timing_delay_transaction";//交易预警查前几分的数据
    public static final String TIMING_PEN_NUMBER_TRANSACTION="timing_pen_number_transaction";//交易预警异常笔数
    public static final String TIMING_SMS_TEMPLATE_TRANSACTION="timing_sms_template_transaction";//交易预警短信模板


    public static final String TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENTING="timing_cycle_payment_service_settlementing";//出款结算中预警周期
    public static final String TIMING_DELAY_PAYMENT_SERVICE_SETTLEMENTING="timing_delay_payment_service_settlementing";//出款结算中预警查前几分的数据
    public static final String TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENTING="timing_pen_number_payment_service_settlementing";//出款结算中预警异常笔数
    public static final String TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_SETTLEMENTING="timing_sms_template_payment_service_settlementing";//出款结算中预警短信模板


    public static final String TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENT_FAILURE="timing_cycle_payment_service_settlement_failure";//出款结算失败预警周期
    public static final String TIMING_DELAY_PAYMENT_SERVICE_SETTLEMENT_FAILURE="timing_delay_payment_service_settlement_failure";//出款结算失败预警查前几分的数据
    public static final String TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENT_FAILURE="timing_pen_number_payment_service_settlement_failure";//出款结算失败预警异常笔数
    public static final String TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_SETTLEMENT_FAILURE="timing_sms_template_payment_service_settlement_failure";//出款结算失败预警短信模板


    public static final String TIMING_CYCLE_PAYMENT_SERVICE_QUOTA="timing_cycle_payment_service_quota";//出款服务额度预警周期
    public static final String TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_QUOTA="timing_sms_template_payment_service_quota";//出款服务额度预警短信模板

    public static final String TIMING_SMS_TEMPLATE_TASK="timing_sms_template_task";//定时任务监控预警短信模板
    public static final String TIMING_SMS_TEMPLATE_TASK_NOT_STARTING="timing_sms_template_task_not_starting";//定时任务监控预警短信模板
    @Resource
    private SysDictService sysDictService;

    @Resource
    private TimingProduceDao timingProduceDao;

    @Resource
    private WarningEventsService warningEventsService;

    @Resource
    private WarningPeopleService warningPeopleService;
    
    @Autowired
	private WarningSetService warningSetService;

    /**
     * 交易非成功
     */
    @Override
    public void timingTransaction(){
        SysDict cycleSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_TRANSACTION);
        SysDict delaySysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_DELAY_TRANSACTION);
        SysDict numberSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_TRANSACTION);
        int cycle=0;
        int delay=0;
        int number=0;

        try {
            cycle=Integer.valueOf(cycleSysDict.getSysValue());
            delay=Integer.valueOf(delaySysDict.getSysValue());
            number=Integer.valueOf(numberSysDict.getSysValue());
        }catch (Exception e){
            e.printStackTrace();
            log.info("交易预警定数据字典配置数据异常!");
            return;
        }
        if(cycle<=0||number<=0){
            log.info("交易预警定数据字典配置数据异常!");
            return;
        }
        TimingProduce conditionTP=getConditionTimingProduce(cycle,delay);//条件

        List<TimingProduce> sunTpList=timingProduceDao.getTimingTransaction(conditionTP);
        if(sunTpList!=null&&sunTpList.size()>0){
            Map<String,TimingProduceCount> map=new HashMap<String,TimingProduceCount>();
            for(TimingProduce tp:sunTpList){
                if(tp.getTotal()!=null&&tp.getTotal()>0){
                    String key=tp.getAcqOrgId()+"-"+tp.getAcqServiceId();
                    if(map.get(key)!=null){
                        //叠加
                        TimingProduceCount tpcMap=map.get(key);
                        tpcMap.setTotal(tpcMap.getTotal()+tp.getTotal());
                        tpcMap.getTimingProduceList().add(tp);
                        map.put(key,tpcMap);
                    }else{
                        //新增
                        TimingProduceCount tpc=new TimingProduceCount();
                        tpc.setAcqOrgId(tp.getAcqOrgId());
                        tpc.setAcqServiceId(tp.getAcqServiceId());
                        tpc.setTotal(tp.getTotal());
                        tpc.getTimingProduceList().add(tp);
                        map.put(key,tpc);
                    }
                }
            }
            //遍历统计完成的Map
            Iterator<Map.Entry<String, TimingProduceCount>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
            	Map.Entry<String, TimingProduceCount> entry = entries.next();
				TimingProduceCount tpc = entry.getValue();
				WarningSet warningSet = warningSetService.getWaringInfoByService(tpc.getAcqServiceId());
				if (tpc.getTotal() >= warningSet.getExceptionNumber()) {
				    if(warningSet.getId() != null){
                        log.info("预警触发配置，来自warning_set，warningSet.id:{},warningSet.warnTimeType:{}",warningSet.getId(),warningSet.getWarnTimeType() );
                    } else {
                        log.info("预警触发配置，来自数据字典");
                    }
					generateTransWarningEvents(tpc, TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_TRANSACTION);
				}
            }
        }
    }


    /**
     * 出款结算中
     */
    @Override
    public void timingPaymentServiceInTheSettlement(){
        SysDict cycleSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENTING);
        SysDict delaySysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_DELAY_PAYMENT_SERVICE_SETTLEMENTING);
        SysDict numberSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENTING);
        int cycle=0;
        int delay=0;
        int number=0;
        try {
            cycle=Integer.valueOf(cycleSysDict.getSysValue());
            delay=Integer.valueOf(delaySysDict.getSysValue());
            number=Integer.valueOf(numberSysDict.getSysValue());
        }catch (Exception e){
            e.printStackTrace();
            log.info("出款结算中预警定数据字典配置数据异常!");
            return;
        }
        if(cycle<=0||number<=0){
            log.info("出款结算中预警定数据字典配置数据异常!");
            return;
        }
        TimingProduce conditionTP=getConditionTimingProduce(cycle,delay);//条件
        conditionTP.setPaymentStatus("1");
        List<TimingProduce> sunTpList=timingProduceDao.getPaymentServiceInTheSettlement(conditionTP);
       
        List<WarningSet> exceptionNumber = warningPeopleService.getExceptionNumber();
		Map<Integer, Integer> idTotal = new HashMap<>();
		for (WarningSet warningSet : exceptionNumber) {
			idTotal.put(warningSet.getServiceId(), warningSet.getExceptionNumber());
		}
		
		if (sunTpList != null && sunTpList.size() > 0) {
			for (TimingProduce tp : sunTpList) {
				if (tp.getTotal() != null && tp.getTotal() > 0) {
//                    if (idTotal!=null && idTotal.containsKey(tp.getOutServiceId())) {
//                        if (tp.getTotal() >= idTotal.get(tp.getOutServiceId())) {
//                            // 预警事件
//                            generatePaymentServiceWarningEvents(tp,
//                                    TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_SETTLEMENTING);
//                        }
                    //update by tans 2018.11.22
				    //根据出款服务ID去warning_set表查找，有没有特殊配置
                    //优先取个性化配置，其次取全天配置，若没有，则跟数据字典比较
                    WarningSet warningSet = warningSetService.getWaringInfoByServiceAndStatus(tp.getOutServiceId(), WarningSet.warningSetTypeOut);
                    if(warningSet != null){
                        if( tp.getTotal() >= warningSet.getExceptionNumber()){
                            // 预警事件
                            log.info("预警触发配置，来自warning_set，warningSet.id:{},warningSet.warnTimeType:{}",warningSet.getId(),warningSet.getWarnTimeType() );
                            generatePaymentServiceWarningEvents(tp,
                                    TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_SETTLEMENTING);
                        }
					} else if (tp.getTotal() >= number) {
						// 预警事件
                        log.info("预警触发配置，来自数据字典");
						generatePaymentServiceWarningEvents(tp,
								TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_SETTLEMENTING);
					}
				}
			}
		}
    }

    /**
     * 出款结算失败
     */
    @Override
    public void timingPaymentServiceSettlementFailure(){
        SysDict cycleSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
        SysDict delaySysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_DELAY_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
        SysDict numberSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
        int cycle=0;
        int delay=0;
        int number=0;
        try {
            cycle=Integer.valueOf(cycleSysDict.getSysValue());
            delay=Integer.valueOf(delaySysDict.getSysValue());
            number=Integer.valueOf(numberSysDict.getSysValue());
        }catch (Exception e){
            e.printStackTrace();
            log.info("出款结算失败预警定数据字典配置数据异常!");
            return;
        }
        if(cycle<=0||number<=0){
            log.info("出款结算失败预警定数据字典配置数据异常!");
            return;
        }
        TimingProduce conditionTP=getConditionTimingProduce(cycle,delay);//条件
        conditionTP.setPaymentStatus("2");
        List<TimingProduce> sunTpList=timingProduceDao.getPaymentServiceInTheSettlement(conditionTP);
        List<WarningSet> exceptionNumber = warningPeopleService.getFailurExceptionNumber();
		
        Map<Integer, Integer> idTotal = new HashMap<>();
		for (WarningSet warningSet : exceptionNumber) {
			idTotal.put(warningSet.getServiceId(), warningSet.getFailurExceptionNumber());
		}

		if (sunTpList != null && sunTpList.size() > 0) {
			for (TimingProduce tp : sunTpList) {
				if (tp.getTotal() != null && tp.getTotal() > 0) {
//					if (idTotal!=null && idTotal.containsKey(tp.getOutServiceId())) {
//						if (tp.getTotal() >= idTotal.get(tp.getOutServiceId())) {
//							generatePaymentServiceWarningEvents(tp,
//									TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
//						}
                    //update by tans 2018.11.22
                    //根据出款服务ID去warning_set表查找，有没有特殊配置
                    //优先取个性化配置，其次取全天配置，若没有，则跟数据字典比较
                    WarningSet warningSet = warningSetService.getWaringInfoByServiceAndStatus(tp.getOutServiceId(), WarningSet.warningSetTypeOut);
                    if(warningSet != null ){
                        if(tp.getTotal() >= warningSet.getFailurExceptionNumber()){
                            // 预警事件
                            log.info("预警触发配置，来自warning_set，warningSet.id:{},warningSet.warnTimeType:{}",warningSet.getId(),warningSet.getWarnTimeType() );
                            generatePaymentServiceWarningEvents(tp,
                                    TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
                        }
					} else if (tp.getTotal() >= number) {
                        log.info("预警触发配置，来自数据字典");
						generatePaymentServiceWarningEvents(tp,
								TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
					}
				}
			}
		}
    }

    /**
     * 出款服务额度预警
     */
    @Override
    public void timingPaymentServiceQuota(){
        WarningEvents weCondition=new WarningEvents();
        Calendar c = Calendar.getInstance();
        Date date=c.getTime();
        //今天0是0分0秒
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        weCondition.setCreateTimeBegin(c.getTime());
        //今天23时59分59秒
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        weCondition.setCreateTimeEnd(c.getTime());

        List<OutAccountService> outServiceList=timingProduceDao.getOutAccountServiceList();
        if(outServiceList!=null&&outServiceList.size()>0){
            for(OutAccountService out:outServiceList) {
                if(out.getDayTotalAmount()!=null&&out.getOutAmountWarning()!=null&&out.getDayOutAccountAmount()!=null){
                    BigDecimal surplus=out.getDayOutAccountAmount().subtract(out.getDayTotalAmount());//剩余额度 50
                    if(out.getOutAmountWarning().compareTo(surplus)>=0){//a>=b 100
                        weCondition.setOutServiceId(out.getId());
                        List<WarningEvents> list=warningEventsService.selectWarningEvents(weCondition);
                        if(list==null||list.size()<=0){
                            generatePaymentServiceQuotaWarningEvents(date,out,TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_PAYMENT_SERVICE_QUOTA);
                        }
                    }
                }
            }
        }
    }

    /**
     * 交易生产预警事件
     * @param tpc
     */
    private void generateTransWarningEvents(TimingProduceCount tpc,String tem){

        WarningEvents we=new WarningEvents();
        we.setAcqId(tpc.getAcqOrgId());
        we.setAcqServiceId(tpc.getAcqServiceId());
        List<TimingProduce> tpList=tpc.getTimingProduceList();
        SysDict smsSysDict=sysDictService.getByKey(tem);
        String template=smsSysDict.getSysValue();
        //收单机构YS_ZQ,收单服务YS_ZQ_POS刷卡152，16:26:05交易返回为空[15笔]交易失败[5笔]，请处理
        //收单机构 #1,收单服务 #2 #3,#4交易返回为空[#5笔]交易失败[#6笔],请处理.
        if(tpList!=null&&tpList.size()>0){
            int sum1=0;//交易返回空
            int sum2=0;//交易失败
            String acqName=null;
            String acqEnname=null;
            String serviceName=null;
            String str1=null;
            String str2=null;
            for(int i=0;i<tpList.size();i++){
                TimingProduce tp=tpList.get(i);
                if(i==0){
                    acqName=tp.getAcqName();
                    acqEnname=tp.getAcqEnname();
                    serviceName=tp.getServiceName();
                    str1=tp.getTransStatus();
                    str2=""+tp.getTotal();
                }else{
                    str1=str1+","+tp.getTransStatus();
                    str2=str2+","+tp.getTotal();
                }
                if("FAILED".equals(tp.getTransStatus())){
                    sum2=sum2+tp.getTotal();
                }else{
                    sum1=sum1+tp.getTotal();
                }
            }
            Date date=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            String dateStr=sdf.format(date);
            String message=template.replaceAll("#1",acqEnname).replaceAll("#2",serviceName)
                    .replaceAll("#3",tpc.getAcqServiceId()+"").replaceAll("#4",dateStr).replaceAll("#5",sum1+"")
                    .replaceAll("#6",sum2+"");

            we.setAcqName(acqName);
            we.setAcqEnname(acqEnname);
            we.setServiceName(serviceName);
            we.setCreateTime(date);
            we.setSmsTime(date);
            we.setMessage(message);
            we.setTransStatus(str1);
            we.setTransStatusNumber(str2);
            we.setEventNumber(getEventNumber(date,"TRANS"));
            we.setStatus(1);//交易系统
            int num=warningEventsService.insertWarningEvents(we);
            //发送预警短信
            if(num>0){
                sendSms(we);
            }
        }
    }
    
    private void sendSms(WarningEvents we) {
		List<WarningPeople> list = warningPeopleService.getWarningPeopleAll(we.getStatus());
		if (list != null && list.size() > 0) {
			for (WarningPeople wp : list) {
				// 没有设置任何服务 或者包含该服务
				String sids = wp.getSids();
				String asId = we.getAcqServiceId().toString();
				boolean isSend = !StringUtils.hasLength(sids) || sids.indexOf(asId) != -1;
				// 发送短信
				if (StringUtils.hasLength(wp.getPhone()) && isSend) {
					try {
						Sms.sendMsg(wp.getPhone(), we.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						log.info("向手机号:" + wp.getPhone() + "短信发送失败,短信信息:" + we.getMessage());
					}
				}
			}
		} else {
			log.info("收单服务未设置预警人!");
		}
	}
    
    /**
     *出款生成预警事件
     */
    private void generatePaymentServiceWarningEvents(TimingProduce tp,String tem){
        WarningEvents we=new WarningEvents();
        SysDict smsSysDict=sysDictService.getByKey(tem);
        String template=smsSysDict.getSysValue();
        //收单机构YS_ZQ,出款服务YS_ZQT0单笔代付108，16:26:05结算中[200笔]，请处理。
        //收单机构 #1,出款服务 #2 #3,#4结算中[#5笔],请处理.

        //收单机构YS_ZQ,出款服务YS_ZQT0单笔代付108，16:26:05结算失败[20笔]，请处理。
        //收单机构 #1,出款服务 #2 #3,#4结算失败[#5笔],请处理.
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String dateStr=sdf.format(date);
        String message=template.replaceAll("#1",tp.getAcqEnname()).replaceAll("#2",tp.getOutServiceName())
                .replaceAll("#3",tp.getOutServiceId()+"").replaceAll("#4",dateStr).replaceAll("#5",tp.getTotal()+"");
        we.setAcqId(tp.getAcqOrgId());
        we.setAcqName(tp.getAcqName());
        we.setAcqEnname(tp.getAcqEnname());
        we.setOutServiceId(tp.getOutServiceId());
        we.setOutServiceName(tp.getOutServiceName());
        we.setCreateTime(date);
        we.setSmsTime(date);
        we.setMessage(message);
        we.setEventNumber(getEventNumber(date,"PAYMENT"));
        we.setStatus(2);//出款系统
        int num=warningEventsService.insertWarningEvents(we);
        //发送预警短信
        if(num>0){
        	sendMsgReloadForOut(we);
        }
    }

    /**
     *出款服务额度预警事件
     */
    private void generatePaymentServiceQuotaWarningEvents(Date date,OutAccountService out,String tem){
        WarningEvents we=new WarningEvents();
        SysDict smsSysDict=sysDictService.getByKey(tem);
        String template=smsSysDict.getSysValue();
        //收单机构YS_ZQ,出款服务YS_ZQT0单笔代付108，16:26:05出款余额低于预警值，请处理。
        //收单机构 #1,出款服务 #2 #3,#4出款余额低于预警值,请处理.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String dateStr=sdf.format(date);
        String message=template.replaceAll("#1",out.getAcqEnname()).replaceAll("#2",out.getServiceName())
                .replaceAll("#3",out.getId()+"").replaceAll("#4",dateStr);
        we.setAcqId(out.getAcqOrgId());
        we.setAcqName(out.getAcqOrgName());
        we.setAcqEnname(out.getAcqEnname());
        we.setOutServiceId(out.getId());
        we.setOutServiceName(out.getServiceName());
        we.setCreateTime(date);
        we.setSmsTime(date);
        we.setMessage(message);
        we.setEventNumber(getEventNumber(date,"PAYMENT"));
        we.setStatus(2);//出款系统
        int num=warningEventsService.insertWarningEvents(we);
        //发送预警短信
        if(num>0){
            sendMsg(we);
        }
    }

    /**
     *定时任务监控预警事件
     */
    @Override
    public void taskWarningEvents(Date startDate,Date endDate,TimedTask tim,int state){
        WarningEvents we=new WarningEvents();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        if(state==1){//超时预警
            SysDict smsSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_TASK);
            String template=smsSysDict.getSysValue();
            String startDateStr=sdf.format(startDate);
            String endDateStr=sdf.format(endDate);
            String taskStatus="";
            if("BLOCKED".equals(tim.getTaskStatus())){
                taskStatus="阻塞";
            }else{
                taskStatus="错误";
            }
            //[警告]定时任务 #1  #2  #3 从 #4 到 #5 一直处于#6状态,请处理.
            String message=template.replaceAll("#1",tim.getId()+"").replaceAll("#2",tim.getTaskName())
                    .replaceAll("#3",tim.getTaskGroup()).replaceAll("#4",startDateStr)
                    .replaceAll("#5",endDateStr).replaceAll("#6",taskStatus);
            we.setMessage(message);
            we.setTaskStatus(tim.getTaskStatus());
        }else{//未启动
            WarningEvents weCondition=new WarningEvents();
            Calendar c = Calendar.getInstance();
            //今天0是0分0秒
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            weCondition.setCreateTimeBegin(c.getTime());
            //今天23时59分59秒
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            weCondition.setCreateTimeEnd(c.getTime());
            weCondition.setTaskName(tim.getTaskName());
            weCondition.setTaskGroup(tim.getTaskGroup());
            List<WarningEvents> list=warningEventsService.selectWarningEventsTask(weCondition);
            if(list!=null&&list.size()>0){
                //已经发送过
                return;
            }
            SysDict smsSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_SMS_TEMPLATE_TASK_NOT_STARTING);
            String template=smsSysDict.getSysValue();
            String endDateStr=sdf.format(endDate);
            //[警告]定时任务 #1 #2 #3 未启动 #4,请处理.
            String message=template.replaceAll("#1",tim.getId()+"").replaceAll("#2",tim.getTaskName())
                    .replaceAll("#3",tim.getTaskGroup()).replaceAll("#4",endDateStr);
            we.setMessage(message);
            we.setTaskStatus("-1");//未启动存-1
        }
        we.setTaskName(tim.getTaskName());
        we.setTaskGroup(tim.getTaskGroup());
        we.setCreateTime(endDate);
        we.setSmsTime(endDate);
        we.setEventNumber(getEventNumber(endDate,"TASK"));
        we.setStatus(3);//出款系统
        int num=warningEventsService.insertWarningEvents(we);
        //发送预警短信
        if(num>0){
            sendMsgTask(we,tim);
        }
    }
    //定时任务监控发送短信
    private void sendMsgTask(WarningEvents we,TimedTask tim){
        List<WarningPeople> list= warningPeopleService.getWarningPeopleAll(we.getStatus());
        if(list!=null&&list.size()>0){
            for(WarningPeople wp:list){
                String str=wp.getAssignmentTask();
                if(str!=null){
                    String[] strs=str.split(",");
                    if(strs!=null&&strs.length>0){
                        boolean sta=false;
                        for(int i=0;i<strs.length;i++){
                            if((tim.getId()+"").equals(strs[i])){
                                sta=true;
                                break;
                            }
                        }
                        if(sta){
                            //发送短信
                            if(wp.getPhone()!=null&&!"".equals(wp.getPhone())){
                                try{
                                    Sms.sendMsg(wp.getPhone(),we.getMessage());
                                }catch (Exception e){
                                    e.printStackTrace();
                                    log.info("向手机号:"+wp.getPhone()+"短信发送失败,短信信息:"+we.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }else{
            if(we.getStatus()==3){
                log.info("定时任务监控未设置预警人!");
            }
        }
    }

    //发送短信
    private void sendMsg(WarningEvents we){
        List<WarningPeople> list= warningPeopleService.getWarningPeopleAll(we.getStatus());
        if(list!=null&&list.size()>0){
            for(WarningPeople wp:list){
                //发送短信
                if(wp.getPhone()!=null&&!"".equals(wp.getPhone())){
                    try{
                        Sms.sendMsg(wp.getPhone(),we.getMessage());
                    }catch (Exception e){
                        e.printStackTrace();
                        log.info("向手机号:"+wp.getPhone()+"短信发送失败,短信信息:"+we.getMessage());
                    }
                }
            }
        }else{
            if(we.getStatus()==1){
                log.info("收单服务未设置预警人!");
            }else if(we.getStatus()==2){
                log.info("出款服务未设置预警人!");
            }
        }
    }
    
    private void sendMsgReloadForOut(WarningEvents we){
        List<WarningPeople> list= warningPeopleService.getWarningPeopleAll(we.getStatus());
        if(list!=null&&list.size()>0){
        	String sids = null;
        	String temp = we.getOutServiceId().toString();
            for(WarningPeople wp:list){
                //发送短信
                if(wp.getPhone()!=null&&!"".equals(wp.getPhone())){
                	 sids = wp.getSids(); 
                	 if (sids==null || sids.trim().equals("") || sids.indexOf(temp)>=0) {
                		 try{
                             Sms.sendMsg(wp.getPhone(),we.getMessage());
                         }catch (Exception e){
                             e.printStackTrace();
                             log.info("向手机号:"+wp.getPhone()+"短信发送失败,短信信息:"+we.getMessage());
                         }
					 }
                }
            }
        }else{
            if(we.getStatus()==1){
                log.info("收单服务未设置预警人!");
            }else if(we.getStatus()==2){
                log.info("出款服务未设置预警人!");
            }
        }
    }
    
    
    private String getEventNumber(Date date,String head){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;
        String dateStr=sdf.format(date);
        String str=dateStr+RandomNumber.mumberRandom(head,5,0);
        return str;
    }
    /**
     * 获取开始结束时间条件
     * @param cycle
     * @param delay
     * @return
     */
    private TimingProduce getConditionTimingProduce(int cycle,int delay){
        TimingProduce conditionTP=new TimingProduce();
        //开始
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.MINUTE, -(cycle+delay));
        conditionTP.setStartTime(c1.getTime());
        //结束
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.MINUTE, -cycle);
        conditionTP.setEndTime(c2.getTime());
        return conditionTP;
    }
}
