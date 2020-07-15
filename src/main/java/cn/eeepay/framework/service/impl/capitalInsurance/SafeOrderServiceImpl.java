package cn.eeepay.framework.service.impl.capitalInsurance;

import cn.eeepay.framework.dao.capitalInsurance.SafeOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.SafeOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.capitalInsurance.SafeOrderService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 保险订单service 实现类
 */
@Service("safeOrderService")
public class SafeOrderServiceImpl implements SafeOrderService {

    private static final Logger log = LoggerFactory.getLogger(SafeOrderServiceImpl.class);

    @Resource
    private SafeOrderDao safeOrderDao;

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<SafeOrder> selectAllList(SafeOrder order, Page<SafeOrder> page) {
        return safeOrderDao.selectAllList(order,page);
    }

    @Override
    public SafeOrder getSafeOrderDetail(int id) {
        return safeOrderDao.getSafeOrderDetail(id);
    }

    @Override
    public OrderTotal selectSum(SafeOrder order, Page<SafeOrder> page) {
        return safeOrderDao.selectSum(order,page);
    }

    @Override
    public void retreatsSafe(String ids, Map<String, Object> msg) {
        if(ids!=null&&!"".equals(ids)){
            String[] sts=ids.split(",");
            int num=0;//符合条件有效的
            int trueNum=0;//退保成功
            int falseNum=0;//退保失败
            Date newDate=new Date();
            for(int i=0;i<sts.length;i++){
                SafeOrder order=safeOrderDao.getSafeOrderDetail(Integer.parseInt(sts[i]));
                if(order!=null
                        &&"FAILED".equals(order.getTransStatus())
                        &&"SUCCESS".equals(order.getBxType())
                        &&(order.gettTime().getTime()<newDate.getTime())){
                    //有效的
                    num++;
                    String returnStr=retreatsSafeInterface(order.getBxOrderNo());
                    if(returnStr!=null){
                        if("true".equals(returnStr)){
                            trueNum++;
                        }else{
                            falseNum++;
                        }
                    }else{
                        falseNum++;
                    }
                }
            }
            msg.put("status", true);
            msg.put("msg", "符合的数据有"+num+"笔,退保成功:"+trueNum+"笔,退保失败:"+falseNum+"笔!");
        }else{
            msg.put("status", false);
            msg.put("msg", "符合的数据为空!");
        }
    }

    @Override
    public List<SafeOrder> importDetailSelect(SafeOrder order) {
        return safeOrderDao.importDetailSelect(order);
    }

    @Override
    public void importDetail(List<SafeOrder> list, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "保险订单列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("bxOrderNo",null);
            maps.put("tTime",null);
            maps.put("orderNo",null);
            maps.put("merchantNo",null);
            maps.put("oneAgentNo",null);
            maps.put("settlementMethod",null);
            maps.put("transAmount",null);
            maps.put("nFee",null);
            maps.put("nPrm",null);
            maps.put("nAmt",null);
            maps.put("transStatus",null);
            maps.put("bxType",null);
            maps.put("thirdOrderNo",null);
            maps.put("tBeginTime",null);
            maps.put("tEndTime",null);
            data.add(maps);
        }else{
            Map<String, String> transStatusMap=sysDictService.selectMapByKey("TRANS_STATUS");//订单状态

            Map<String, String> settlementMethodMap=new HashMap<String, String>();
            settlementMethodMap.put("0","T0");
            settlementMethodMap.put("1","T1");

            Map<String, String> bxTypeMap=new HashMap<String, String>();
            bxTypeMap.put("INIT","初始化");
            bxTypeMap.put("SUCCESS","投保成功");
            bxTypeMap.put("FAILED","投保失败");
            bxTypeMap.put("OVERLIMIT","已退保");

            for (SafeOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("bxOrderNo",or.getBxOrderNo()==null?null:or.getBxOrderNo());
                maps.put("tTime", or.gettTime()==null?"":sdf1.format(or.gettTime()));
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                maps.put("merchantNo",or.getMerchantNo()==null?null:or.getMerchantNo());
                maps.put("oneAgentNo",or.getOneAgentNo()==null?null:or.getOneAgentNo());
                maps.put("settlementMethod",settlementMethodMap.get(or.getSettlementMethod()));
                maps.put("transAmount",or.getTransAmount()==null?"":or.getTransAmount().toString());
                maps.put("nFee",or.getnFee()==null?"":or.getnFee().toString());
                maps.put("nPrm",or.getnPrm()==null?"":or.getnPrm().toString());
                maps.put("nAmt",or.getnAmt()==null?"":or.getnAmt().toString());
                maps.put("transStatus",transStatusMap.get(or.getTransStatus()));
                maps.put("bxType",bxTypeMap.get(or.getBxType()));
                maps.put("thirdOrderNo",or.getThirdOrderNo()==null?null:or.getThirdOrderNo());
                maps.put("tBeginTime", or.gettBeginTime()==null?"":sdf1.format(or.gettBeginTime()));
                maps.put("tEndTime", or.gettEndTime()==null?"":sdf1.format(or.gettEndTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"bxOrderNo","tTime","orderNo","merchantNo","oneAgentNo",
                "settlementMethod","transAmount","nFee","nPrm","nAmt","transStatus","bxType","thirdOrderNo",
                "tBeginTime","tEndTime"
        };
        String[] colsName = new String[]{"保险订单号","投保时间","交易订单号","商户编号","一级代理商编号",
                "结算周期","交易金额","保费-成本价","保费-售价","保额","支付状态","投保状态","保单号",
                "保险起期","保险止期"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出保险订单列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    private String retreatsSafeInterface(String bxOrderNo){
        String returnStr=null;
        try {
            SysDict sysDict = sysDictService.getByKey("CORE_URL");
            if(sysDict!=null){
                String url=sysDict.getSysValue()+"/mer/bxBack";
                final HashMap<String, String> claims = new HashMap<>();
                claims.put("bxOrderNo", bxOrderNo);
                log.info("手工退保,请求路径：{},参数：{}",url, JSONObject.toJSONString(claims));
                returnStr = new ClientInterface(url, claims).postRequest();
                log.info("手工退保,返回结果：{}", returnStr);
            }else{
                log.info("数据字典未配置CORE_URL!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnStr;

    }
}
