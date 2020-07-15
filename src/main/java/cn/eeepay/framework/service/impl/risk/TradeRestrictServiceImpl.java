package cn.eeepay.framework.service.impl.risk;

import cn.eeepay.framework.dao.risk.TradeRestrictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.risk.TradeRestrict;
import cn.eeepay.framework.service.risk.TradeRestrictService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2019/5/5/005.
 * @author  liuks
 * 交易限制记录实现service
 */
@Service("tradeRestrictService")
public class TradeRestrictServiceImpl implements TradeRestrictService {

    private static final Logger log = LoggerFactory.getLogger(TradeRestrictServiceImpl.class);

    @Resource
    private TradeRestrictDao tradeRestrictDao;

    @Override
    public List<TradeRestrict> selectAllList(TradeRestrict info, Page<TradeRestrict> page) {
        List<TradeRestrict> list=tradeRestrictDao.selectAllList(info,page);
        if(page.getResult()!=null&&page.getResult().size()>0){
            for(TradeRestrict itme:page.getResult()){
                if(1==itme.getStatus().intValue()){
                    itme.setButtonStatus(1);
                }else{
                    itme.setButtonStatus(0);
                }
            }
        }
        return list;
    }

    @Override
    public List<TradeRestrict> importDetailSelect(TradeRestrict info) {
        return tradeRestrictDao.importDetailSelect(info);
    }

    @Override
    public void importDetail(List<TradeRestrict> list, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "交易限制记录列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("merchantNo",null);
            maps.put("limitNumber",null);
            maps.put("status",null);
            maps.put("merchantName",null);
            maps.put("orderNo",null);
            maps.put("merchantPhone",null);
            maps.put("rollNo",null);
            maps.put("triggerNumber",null);
            maps.put("agentNo",null);
            maps.put("resultNo",null);
            maps.put("createPerson",null);
            maps.put("operationTime", null);
            maps.put("merchantTime", null);
            maps.put("createTime", null);
            maps.put("invalidTime", null);
            data.add(maps);
        }else{
            Map<String, String> statusMap=new HashMap<String, String>();
            statusMap.put("0","初始化");
            statusMap.put("1","开启");
            statusMap.put("2","关闭");
            statusMap.put("3","失效");

            for (TradeRestrict or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("limitNumber",or.getLimitNumber()==null?"":or.getLimitNumber());
                maps.put("status",statusMap.get(or.getStatus().toString()));
                maps.put("merchantName",or.getMerchantName()==null?"":or.getMerchantName());
                maps.put("orderNo",or.getOrderNo()==null?"":or.getOrderNo());
                maps.put("merchantPhone",or.getMerchantPhone()==null?"":or.getMerchantPhone());
                maps.put("rollNo",or.getRollNo()==null?"":or.getRollNo());
                maps.put("triggerNumber",or.getTriggerNumber()==null?"":or.getTriggerNumber().toString());
                maps.put("agentNo",or.getAgentNo()==null?null:or.getAgentNo());
                maps.put("resultNo",or.getResultNo()==null?"":or.getResultNo());
                maps.put("createPerson",or.getCreatePerson()==null?"":or.getCreatePerson());
                maps.put("operationTime", or.getOperationTime()==null?"":sdf1.format(or.getOperationTime()));
                maps.put("merchantTime", or.getMerchantTime()==null?"":sdf1.format(or.getMerchantTime()));
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("invalidTime", or.getInvalidTime()==null?"":sdf1.format(or.getInvalidTime()));

                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"limitNumber","status","merchantName","orderNo","merchantPhone","rollNo",
                "triggerNumber","agentNo","resultNo","createPerson","operationTime","merchantTime","createTime","invalidTime"
        };
        String[] colsName = new String[]{"受限卡号/商户号","限制状态","商户名称","交易订单号","商户手机号","规则编号",
                "限制次数","代理商编号","失败返回码","操作人","操作时间","商户注册时间","创建时间","失效时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出交易限制记录列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public int riskStatusChangeBatch(String ids, int status,Map<String, Object> msg) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName=principal.getUsername();
        if(ids!=null&&!"".equals(ids)){
            String[] strs=ids.split(",");
            if(strs!=null&&strs.length>0){
                int length=strs.length;
                int successNum=0;
                for(String id:strs){
                    TradeRestrict old=tradeRestrictDao.getInfo(Integer.parseInt(id));
                    if(1==status){
                        //开启
                        if(old.getStatus().intValue()==2){
                            int num=tradeRestrictDao.updateInfoStatus(Integer.parseInt(id),status,userName);
                            if(num>0){
                                successNum++;
                            }
                        }
                    }else{
                        //关闭
                        if(old.getStatus().intValue()==1){
                            int num=tradeRestrictDao.updateInfoStatus(Integer.parseInt(id),status,userName);
                            if(num>0){
                                successNum++;
                            }
                        }
                    }
                }
                msg.put("status", true);
                msg.put("msg", "批量修改交易限制记录状态成功,选中"+length+"条数据,成功操作"+successNum+"条!");
                return 1;
            }
        }
        msg.put("status", false);
        msg.put("msg", "批量修改交易限制记录状态失败!");
        return 0;
    }

    @Override
    public int changeStatus(int id, int status, Map<String, Object> msg) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName=principal.getUsername();
        TradeRestrict old=tradeRestrictDao.getInfo(id);
        if(1==status){
            //开启
            if(old.getStatus().intValue()==2){
                int num=tradeRestrictDao.updateInfoStatus(id,status,userName);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "修改交易限制记录状态成功!");
                    return 1;
                }
            }
        }else{
            //关闭
            if(old.getStatus().intValue()==1){
                int num=tradeRestrictDao.updateInfoStatus(id,status,userName);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "修改交易限制记录状态成功!");
                    return 1;
                }
            }
        }
        msg.put("status", false);
        msg.put("msg", "修改交易限制记录状态失败!");
        return 0;
    }

    @Override
    public int updateFailureTime() {
        return tradeRestrictDao.updateFailureTime();
    }
}
