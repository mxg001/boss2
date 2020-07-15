package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.RedEnvelopesReceiveDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedEnvelopesReceive;
import cn.eeepay.framework.service.RedEnvelopesReceiveService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/1/18/018.
 * @author  liuks
 * 红包领取查询 service
 */
@Service("redEnvelopesReceiveService")
public class RedEnvelopesReceiveServiceImpl  implements RedEnvelopesReceiveService {

    private static final Logger log = LoggerFactory.getLogger(RedEnvelopesReceiveServiceImpl.class);
    @Resource
    private RedEnvelopesReceiveDao redEnvelopesReceiveDao;

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<RedEnvelopesReceive> selectAllByParam(RedEnvelopesReceive red, Page<RedEnvelopesReceive> page) {
        redEnvelopesReceiveDao.selectAllByParam(red,page);
        List<RedEnvelopesReceive> list = page.getResult();
        try {
            if(list != null && list.size() > 0){
                for(RedEnvelopesReceive item: list){
                    try {
                        if(StringUtils.isNotBlank(item.getPushUserName())){
                            item.setPushUserName(URLDecoder.decode((item.getPushUserName()), "utf-8"));
                        }
                    } catch (Exception e) {
                    }
//                    if(item.getRateType()==1){
//                        item.setRate(item.getRate()+"元");
//                    }if(item.getRateType()==2){
//                        item.setRate(item.getRate()+"%");
//                    }
                    try {
                        if(StringUtils.isNotBlank(item.getGetUserName())){
                            item.setGetUserName(URLDecoder.decode((item.getGetUserName()), "utf-8"));
                        }
                    } catch (Exception e) {
                    }

                    try {
                        if(StringUtils.isNotBlank(item.getTerritoryUserName())){
                            item.setTerritoryUserName(URLDecoder.decode((item.getTerritoryUserName()), "utf-8"));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public List<RedEnvelopesReceive> exportInfo(RedEnvelopesReceive order) {
        List<RedEnvelopesReceive> list = redEnvelopesReceiveDao.exportInfo(order);
        try {
            if(list != null && list.size() > 0){
                for(RedEnvelopesReceive item: list){
                    if(StringUtils.isNotBlank(item.getPushUserName())){
                        try {
                            item.setPushUserName(URLDecoder.decode((item.getPushUserName()), "utf-8"));
                        } catch (Exception e) {
                        }

                    }
                    if(StringUtils.isNotBlank(item.getGetUserName())){
                        try {
                            item.setGetUserName(URLDecoder.decode((item.getGetUserName()), "utf-8"));
                        } catch (Exception e) {
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public RedEnvelopesReceive sumCount(RedEnvelopesReceive order) {
        return redEnvelopesReceiveDao.sumCount(order);
    }

    @Override
    public List<RedEnvelopesReceive> selectRedEnvelopesReceive(Long id,Page<RedEnvelopesReceive> page) {
        redEnvelopesReceiveDao.selectRedEnvelopesReceive(id,page);
        List<RedEnvelopesReceive> list =  page.getResult();
        try {
            if(list != null && list.size() > 0){
                for(RedEnvelopesReceive item: list){
                    if(StringUtils.isNotBlank(item.getNickName())){
                        try {
                            item.setNickName(URLDecoder.decode(item.getNickName(), "utf-8"));
                        } catch (Exception e) {
                        }

                    }
                    if(StringUtils.isNotBlank(item.getGetUserName())){
                        try {
                            item.setGetUserName(URLDecoder.decode(item.getGetUserName(), "utf-8"));
                        } catch (Exception e) {
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void exportRedEnvelopesReceive(List<RedEnvelopesReceive> list, HttpServletResponse response) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "红包领取查询"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>();
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("redOrderId",null);
            maps.put("status",null);
            maps.put("pushType",null);
            maps.put("receiveType",null);
            maps.put("busType",null);
            maps.put("confId",null);
            maps.put("orderNo",null);
            maps.put("orgName",null);
            maps.put("pushUserCode",null);
            maps.put("pushUserName",null);
            maps.put("pushUserPhone",null);
            maps.put("getUserCode",null);
            maps.put("getUserName",null);
            maps.put("getUserPhone",null);
            maps.put("amount",null);
            maps.put("getDate", null);

            maps.put("territoryProvinceName", null);
            maps.put("territoryCityName", null);
            maps.put("territoryRegionName", null);

            maps.put("territoryUserCode", null);
            maps.put("territoryUserName", null);
            maps.put("territoryPhone", null);
            maps.put("territoryOrgName", null);
            maps.put("lordsProfit", null);

            maps.put("basicBonusAmount", null);
            maps.put("territoryAvgPrice", null);
            maps.put("territoryPrice", null);
            maps.put("adjustRatio", null);
            maps.put("bonusAmount", null);
            maps.put("dividendStatus", null);
            maps.put("dividendUserCode", null);
            maps.put("receiveTime", null);
            maps.put("rate", null);

            data.add(maps);
        }else{
            Map<String, String> busTypeMap=sysDictService.selectMapByKey("RED_BUS_TYPE");//业务类型
            Map<String, String> pushTypeMap=sysDictService.selectMapByKey("RED_PUSH_TYPE");//发放人类型
            Map<String, String> receiveTypeMap=sysDictService.selectMapByKey("RED_RECEIVE_TYPE");//接收人数类型

            for (RedEnvelopesReceive or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()==null?"":or.getId().toString());
                maps.put("redOrderId",or.getRedOrderId()==null?"":or.getRedOrderId().toString());
                //领取状态
                if(or.getStatus()!=null){
                    if("0".equals(or.getStatus())){
                        maps.put("status","待领取");
                    }else if("1".equals(or.getStatus())){
                        maps.put("status","已领取");
                    }else if("2".equals(or.getStatus())){
                        maps.put("status","平台回收");
                    }else if("3".equals(or.getStatus())){
                        maps.put("status","原路退回");
                    }
                }else{
                    maps.put("status","");
                }
                //发放人类型
                if(or.getPushType()!=null){
                    maps.put("pushType",pushTypeMap.get(or.getPushType()));
                }
                //接收人类型
                if(or.getReceiveType()!=null){
                    maps.put("receiveType",receiveTypeMap.get(or.getReceiveType()));
                }
                //业务类型
                if(or.getBusType()!=null){
                    maps.put("busType",busTypeMap.get(or.getBusType()));
                }

                maps.put("confId",or.getConfId()==null?"":or.getConfId().toString());
                maps.put("orderNo",or.getOrderNo()==null?"":or.getOrderNo().toString());
                maps.put("orgName",or.getOrgName()==null?"":or.getOrgName().toString());
                maps.put("pushUserCode",or.getPushUserCode()==null?"":or.getPushUserCode().toString());
                maps.put("pushUserName",or.getPushUserName()==null?"":or.getPushUserName().toString());
                maps.put("pushUserPhone",or.getPushUserPhone()==null?"":or.getPushUserPhone().toString());
                maps.put("getUserCode",or.getGetUserCode()==null?"":or.getGetUserCode().toString());
                maps.put("getUserName",or.getGetUserName()==null?"":or.getGetUserName().toString());
                maps.put("getUserPhone",or.getGetUserPhone()==null?"":or.getGetUserPhone().toString());
                maps.put("amount",or.getAmount()==null?"":or.getAmount().toString());
                maps.put("getDate", or.getGetDate()==null?"":sdf1.format(or.getGetDate()));
                maps.put("territoryProvinceName",or.getTerritoryProvinceName()==null?"":or.getTerritoryProvinceName().toString());
                maps.put("territoryCityName",or.getTerritoryCityName()==null?"":or.getTerritoryCityName().toString());
                maps.put("territoryRegionName",or.getTerritoryRegionName()==null?"":or.getTerritoryRegionName().toString());
                maps.put("territoryUserCode",or.getTerritoryUserCode()==null?"":or.getTerritoryUserCode().toString());
                maps.put("territoryUserName",or.getTerritoryUserName()==null?"":or.getTerritoryUserName().toString());
                maps.put("territoryPhone",or.getTerritoryPhone()==null?"":or.getTerritoryPhone().toString());
                maps.put("territoryOrgName",or.getTerritoryOrgName()==null?"":or.getTerritoryOrgName().toString());
                maps.put("lordsProfit",or.getLordsProfit()==null?"":or.getLordsProfit().toString());
//                maps.put("basicBonusAmount",or.getBasicBonusAmount()==null?"":or.getBasicBonusAmount().toString());
//                maps.put("territoryAvgPrice",or.getTerritoryAvgPrice()==null?"":or.getTerritoryAvgPrice().toString());
//                maps.put("territoryPrice",or.getTerritoryPrice()==null?"":or.getTerritoryPrice().toString());
//                maps.put("adjustRatio",or.getAdjustRatio()==null?"":or.getAdjustRatio().toString());
//                maps.put("bonusAmount",or.getBonusAmount()==null?"":or.getBonusAmount().toString());
//                if("0".equals(or.getDividendStatus())){
//                    or.setDividendStatus("未领取");
//                }if("1".equals(or.getDividendStatus())){
//                    or.setDividendStatus("已领取");
//                }
//                maps.put("rate",or.getRate());
//                maps.put("dividendUserCode",or.getDividendUserCode()==null?"":or.getDividendUserCode());
//                maps.put("receiveTime",or.getReceiveTime()==null?"":DateUtils.formatDateTime(or.getReceiveTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","redOrderId","status","pushType","receiveType","busType",
                "confId","orderNo","orgName","pushUserCode","pushUserName","pushUserPhone",
                "getUserCode","getUserName","getUserPhone","amount","getDate",
                "territoryProvinceName","territoryCityName","territoryRegionName",
                "territoryUserCode","territoryUserName","territoryPhone","territoryOrgName","lordsProfit"
//                "rate","basicBonusAmount","territoryAvgPrice","territoryPrice","adjustRatio","bonusAmount","dividendStatus","dividendUserCode","receiveTime"
        };

        String[] colsName = new String[]{"红包领取ID","红包ID","领取状态","发放人类型","接收人数类型","业务类型",
                "红包配置ID","关联业务订单ID","发放人组织名称","发红包用户ID","发红包用户姓名","发红包手机号",
                "领取用户ID","领取用户姓名","领取用户手机号","领取金额(元)","领取时间",
                "领取用户省","领取用户市","领取用户区","领主用户编号","领主姓名","领主手机号","领主所属组织","领主收益"
//                "领地业务基准分红配置","领地业务基准分红","领地均价","领地价格","调节系数","领地分红","领地领取状态","领地分红领取领主编号","领地分红领取时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("红包领取查询列表导出!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

}
