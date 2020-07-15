package cn.eeepay.framework.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.daoSuperbank.RedAccountInfoDao;
import cn.eeepay.framework.daoSuperbank.RedConfigDao;
import cn.eeepay.framework.daoSuperbank.RedEnvelopesGrantDao;
import cn.eeepay.framework.daoSuperbank.RedEnvelopesReceiveDao;
import cn.eeepay.framework.daoSuperbank.RedOrdersOptionDao;
import cn.eeepay.framework.daoSuperbank.RedUserConfDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedAccountDetail;
import cn.eeepay.framework.model.RedAccountInfo;
import cn.eeepay.framework.model.RedEnvelopesGrant;
import cn.eeepay.framework.model.RedEnvelopesGrantDiscuss;
import cn.eeepay.framework.model.RedEnvelopesGrantImage;
import cn.eeepay.framework.model.RedEnvelopesGrantOption;
import cn.eeepay.framework.model.RedEnvelopesReceive;
import cn.eeepay.framework.model.RedOrdersOption;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.RedEnvelopesGrantService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * Created by Administrator on 2018/1/19/019.
 * 红包发放查询 serviceImpl
 * @author  liuks
 */
@Service("redEnvelopesGrantService")
public class RedEnvelopesGrantServiceImpl  implements RedEnvelopesGrantService {

    private static final Logger log = LoggerFactory.getLogger(RedEnvelopesGrantServiceImpl.class);

    @Resource
    private RedEnvelopesGrantDao redEnvelopesGrantDao;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private RedOrdersOptionDao redOrdersOptionDao;

    @Resource
    protected RedEnvelopesReceiveDao redEnvelopesReceiveDao;

    @Resource
    private RedAccountInfoDao redAccountInfoDao;

    @Resource
    private RedUserConfDao redUserConfDao;

    @Resource
    private RedConfigDao redConfigDao;

    @Override
    public List<RedEnvelopesGrant> selectAllByParam(RedEnvelopesGrant reg, Page<RedEnvelopesGrant> page) {
        redEnvelopesGrantDao.selectAllByParam(reg,page);
        List<RedEnvelopesGrant> list = page.getResult();
        try {
            for (RedEnvelopesGrant info: list){
                if(StringUtils.isNotBlank(info.getPushUserName())){
                	try {
                		info.setPushUserName(URLDecoder.decode(info.getPushUserName(), "utf-8"));
					} catch (Exception e) {
					}
                    
                }
                if(StringUtils.isNotBlank(info.getDxUserName())){
                    try {
                    	info.setDxUserName(URLDecoder.decode(info.getDxUserName(), "utf-8"));
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
    public List<RedEnvelopesGrant> exportInfo(RedEnvelopesGrant reg) {
        return redEnvelopesGrantDao.exportInfo(reg);
    }

    @Override
    public RedEnvelopesGrant sumCount(RedEnvelopesGrant reg) {
        return redEnvelopesGrantDao.sumCount(reg);
    }

    @Override
    public RedEnvelopesGrant selectRedEnvelopesGrantById(Long id) {
        RedEnvelopesGrant info = redEnvelopesGrantDao.selectRedEnvelopesGrantById(id);
        if(info != null){
                if(StringUtils.isNotBlank(info.getPushUserName())){
                	try {
                		info.setPushUserName(URLDecoder.decode(info.getPushUserName(), "utf-8"));
					} catch (Exception e) {
					}
                }
                if(StringUtils.isNotBlank(info.getDxUserName())){
                    try {
						info.setDxUserName(URLDecoder.decode(info.getDxUserName(), "utf-8"));
					} catch (Exception e) {
					}
                }
                if(StringUtils.isNotBlank(info.getRemark())){
                    try {
						info.setRemark(URLDecoder.decode(info.getRemark(), "utf-8"));
					} catch (Exception e) {
					}
                }
        }
        return info;
    }

    @Override
    public List<RedEnvelopesGrantImage> getImages(Long id) {
        return redEnvelopesGrantDao.getImages(id);
    }

    @Override
    public List<RedEnvelopesGrantDiscuss> getRedEnvelopesGrantDiscuss(Long id,Page<RedEnvelopesGrantDiscuss> page) {
        redEnvelopesGrantDao.getRedEnvelopesGrantDiscuss(id,page);
        List<RedEnvelopesGrantDiscuss> list = page.getResult();
            for(RedEnvelopesGrantDiscuss info: list){
                if(StringUtils.isNotBlank(info.getUserNickName())){
                    try {
						info.setUserNickName(URLDecoder.decode(info.getUserNickName(), "utf-8"));
					} catch (Exception e) {
					}
                }
                if(StringUtils.isNotBlank(info.getContent())){
                    try {
						info.setContent(URLDecoder.decode(info.getContent(), "utf-8"));
					} catch (Exception e) {
					}
                }
            }
        return list;
    }

    @Override
    public List<RedEnvelopesGrantOption> selectRedEnvelopesGrantOption(Long id,Page<RedEnvelopesGrantOption> page) {
        return redEnvelopesGrantDao.selectRedEnvelopesGrantOption(id,page);
    }

    @Override
    public void exportRedEnvelopesGrant(List<RedEnvelopesGrant> list, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "红包发放查询"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>();
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("confId",null);
            maps.put("busType",null);
            maps.put("pushType",null);
            maps.put("receiveType",null);
            maps.put("orgName",null);
            maps.put("hasProfit",null);
            maps.put("status",null);
            maps.put("statusRisk",null);
            maps.put("recoveryType",null);
            maps.put("statusRecovery",null);
            maps.put("pushArea",null);
            maps.put("pushAmount",null);
            maps.put("pushNum",null);
            maps.put("pushEachAmount",null);
            maps.put("pushUserCode",null);
            maps.put("pushRealName",null);
            maps.put("pushUserName",null);
            maps.put("pushUserPhone",null);
            maps.put("dxUserCode",null);
            maps.put("dxUserName",null);
            maps.put("dxUserPhone",null);
            maps.put("payType",null);
            maps.put("orderNo",null);
            maps.put("payOrderNo",null);
            maps.put("pushFee",null);
            maps.put("oneUserProfit",null);
            maps.put("oneUserCode",null);
            maps.put("twoUserProfit",null);
            maps.put("twoUserCode",null);
            maps.put("thrUserProfit",null);
            maps.put("thrUserCode",null);
            maps.put("fouUserProfit",null);
            maps.put("fouUserCode",null);
            maps.put("plateProfit",null);
            maps.put("orgProfit",null);
            maps.put("createDate", null);
            maps.put("expDate",null);
            data.add(maps);
        }else{
            Map<String, String> busTypeMap=sysDictService.selectMapByKey("RED_BUS_TYPE");//业务类型
            Map<String, String> pushTypeMap=sysDictService.selectMapByKey("RED_PUSH_TYPE");//发放人类型
            Map<String, String> receiveTypeMap=sysDictService.selectMapByKey("RED_RECEIVE_TYPE");//接收人数类型
            Map<String, String> pushAreaMap=sysDictService.selectMapByKey("RED_PUSH_AREA");//发放人类型

            for (RedEnvelopesGrant or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()==null?"":or.getId().toString());
                maps.put("confId",or.getConfId()==null?"":or.getConfId().toString());
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
                //发放范围
                if(or.getBusType()!=null){
                    maps.put("pushArea",pushAreaMap.get(or.getPushArea()));
                }
                maps.put("orgName",or.getOrgName()==null?"":or.getOrgName().toString());
                //是否收取佣金
                if(or.getHasProfit()!=null){
                    if("0".equals(or.getHasProfit())){
                        maps.put("hasProfit","是");
                    }else if("1".equals(or.getHasProfit())){
                        maps.put("hasProfit","否");
                    }
                }else{
                    maps.put("hasProfit","");
                }
                //红包状态
                if(or.getStatus()!=null){
                    if("-1".equals(or.getStatus())){
                        maps.put("status","初始化");
                    }else if("0".equals(or.getStatus())){
                        maps.put("status","发放中");
                    }else if("1".equals(or.getStatus())){
                        maps.put("status","已领完");
                    }else if("2".equals(or.getStatus())){
                        maps.put("status","已到期");
                    }
                }else{
                    maps.put("status","");
                }
                //风控状态
                if(or.getStatusRisk()!=null){
                    if("0".equals(or.getStatusRisk())){
                        maps.put("statusRisk","正常");
                    }else if("1".equals(or.getStatusRisk())){
                        maps.put("statusRisk","已屏蔽");
                    }
                }else{
                    maps.put("statusRisk","");
                }
                //剩余金额处理方式
                if(or.getRecoveryType()!=null){
                    if("0".equals(or.getRecoveryType())){
                        maps.put("recoveryType","原路退回");
                    }else if("1".equals(or.getRecoveryType())){
                        maps.put("recoveryType","归平台所有");
                    }else if("2".equals(or.getRecoveryType())){
                        maps.put("recoveryType","无需处理");
                    }
                }else{
                    maps.put("recoveryType","");
                }
                //剩余金额处理状态
                if(or.getStatusRecovery()!=null){
                    if("0".equals(or.getStatusRecovery())){
                        maps.put("statusRecovery","待处理");
                    }else if("1".equals(or.getStatusRecovery())){
                        maps.put("statusRecovery","处理成功");
                    }else if("2".equals(or.getStatusRecovery())){
                        maps.put("statusRecovery","处理失败");
                    }else if("3".equals(or.getStatusRecovery())){
                        maps.put("statusRecovery","处理中");
                    }
                }else{
                    maps.put("statusRecovery","");
                }
                maps.put("pushAmount",or.getPushAmount()==null?"":or.getPushAmount().toString());
                maps.put("pushNum",or.getPushNum()==null?"":or.getPushNum().toString());
                maps.put("pushEachAmount",or.getPushEachAmount()==null?"":or.getPushEachAmount().toString());
                maps.put("pushUserCode",or.getPushUserCode()==null?"":or.getPushUserCode().toString());
                maps.put("pushRealName",or.getPushRealName()==null?"":or.getPushRealName().toString());
                if(StringUtils.isNotBlank(or.getPushUserName())){
                	try {
                		or.setPushUserName(URLDecoder.decode(or.getPushUserName(), "utf-8"));
					} catch (Exception e) {
					}
                    
                }
                maps.put("pushUserName",or.getPushUserName()==null?"":or.getPushUserName().toString());
                maps.put("pushUserPhone",or.getPushUserPhone()==null?"":or.getPushUserPhone().toString());
                maps.put("dxUserCode",or.getDxUserCode()==null?"":or.getDxUserCode().toString());
                if(StringUtils.isNotBlank(or.getDxUserName())){
                    try {
                        or.setDxUserName(URLDecoder.decode(or.getDxUserName(), "utf-8"));
                    } catch (Exception e){
                    }
                }
                maps.put("dxUserName",or.getDxUserName()==null?"":or.getDxUserName().toString());
                maps.put("dxUserPhone",or.getDxUserPhone()==null?"":or.getDxUserPhone().toString());
                //支付方式
                if(or.getPayType()!=null){
                    if("0".equals(or.getPayType())){
                        maps.put("payType","分润账户余额");
                    }else if("1".equals(or.getPayType())){
                        maps.put("payType","微信支付");
                    }else if("2".equals(or.getPayType())){
                        maps.put("payType","红包账户余额");
                    }else if("3".equals(or.getPayType())){
                        maps.put("payType","内部账户");
                    }else if("4".equals(or.getPayType())){
                        maps.put("payType","支付宝支付");
                    }
                }else{
                    maps.put("payType","");
                }
                maps.put("orderNo",or.getOrderNo()==null?"":or.getOrderNo().toString());
                maps.put("payOrderNo",or.getPayOrderNo()==null?"":or.getPayOrderNo().toString());
                maps.put("pushFee",or.getPushFee()==null?"":or.getPushFee().toString());
                maps.put("oneUserProfit",or.getOneUserProfit()==null?"":or.getOneUserProfit().toString());
                maps.put("oneUserCode",or.getOneUserCode()==null?"":or.getOneUserCode().toString());
                maps.put("twoUserProfit",or.getTwoUserProfit()==null?"":or.getTwoUserProfit().toString());
                maps.put("twoUserCode",or.getTwoUserCode()==null?"":or.getTwoUserCode().toString());
                maps.put("thrUserProfit",or.getThrUserProfit()==null?"":or.getThrUserProfit().toString());
                maps.put("thrUserCode",or.getThrUserCode()==null?"":or.getThrUserCode().toString());
                maps.put("fouUserProfit",or.getFouUserProfit()==null?"":or.getFouUserProfit().toString());
                maps.put("fouUserCode",or.getFouUserCode()==null?"":or.getFouUserCode().toString());
                maps.put("plateProfit",or.getPlateProfit()==null?"":or.getPlateProfit().toString());
                maps.put("orgProfit",or.getOrgProfit()==null?"":or.getOrgProfit().toString());
                maps.put("createDate", or.getCreateDate()==null?"":sdf1.format(or.getCreateDate()));
                maps.put("expDate", or.getExpDate()==null?"":sdf1.format(or.getExpDate()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","confId","busType","pushType","receiveType","orgName",
                "hasProfit","status","statusRisk","recoveryType","statusRecovery","pushArea",
                "pushAmount","pushNum","pushUserCode","pushRealName","pushUserName","pushUserPhone",
                "dxUserCode","dxUserName","dxUserPhone","payType","orderNo",
                "payOrderNo","pushFee","oneUserProfit","oneUserCode","twoUserProfit","twoUserCode",
                "thrUserProfit","thrUserCode","fouUserProfit","fouUserCode","plateProfit","orgProfit",
                "createDate","expDate"
        };

        String[] colsName = new String[]{"红包ID","红包配置ID","业务类型","发放人类型","接收人类型","发放人组织名称",
                "是否收取佣金","红包状态","风控状态","剩余金额处理方式","剩余金额处理状态","发放范围",
                "红包金额(元)","个数","发红包用户ID","发红包用户姓名","发红包用户昵称","发红包手机号",
                "单个定向接收用户ID","单个定向接收用户昵称","单个定向接收用户手机号","支付方式","关联业务订单ID",
                "关联支付订单ID","服务费(元)","一级分润(元)","一级编号","二级分润(元)","二级编号",
                "三级分润(元)","三级编号","四级分润(元)","四级编号","平台分润(元)","OEM品牌分润(元)",
                "红包创建时间","红包失效时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("红包发放查询列表导出失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public int updateRedEnvelopesGrantImage(RedEnvelopesGrantImage img) {
        return redEnvelopesGrantDao.updateRedEnvelopesGrantImage(img);
    }

    @Override
    public int updateRedEnvelopesGrantImageAll(RedEnvelopesGrantImage img) {
        return redEnvelopesGrantDao.updateRedEnvelopesGrantImageAll(img);
    }

    @Override
    public int updateRemark(Long id) {
        return redEnvelopesGrantDao.updateRemark(id);
    }

    /**
     * 风控开启关闭红包
     * @param baseInfo
     * @return
     */
    @Override
    public int updateStatusRisk(RedOrdersOption baseInfo) {
        RedEnvelopesGrant red = new RedEnvelopesGrant();
        red.setId(baseInfo.getRedOrderId());
        red.setStatusRisk(baseInfo.getStatus());
        int num = 0;
        //插入操作记录
        Date nowDate = new Date();
        baseInfo.setCreateDate(nowDate);
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        baseInfo.setOptUserName(loginInfo.getUsername());
        //如果是风控屏蔽
        if("1".equals(baseInfo.getStatus())){
//          1.对红包账户余额进行加减，并记录
            //将剩余金额累加到平台账户
            BigDecimal notReceiveAmount = redEnvelopesReceiveDao.totalReceiveAmount(baseInfo.getRedOrderId()
                    , "0");
            notReceiveAmount = notReceiveAmount == null ? BigDecimal.ZERO: notReceiveAmount;
            Long relationId = 0L;//平台ID
            redAccountInfoDao.updateAmount(relationId, notReceiveAmount);
            //插入收支明细
            RedAccountInfo account = redAccountInfoDao.selectByRelationId(relationId);
            RedAccountDetail accountDetail = new RedAccountDetail();
            accountDetail.setRedAccountId(account.getId());
            accountDetail.setAccountCode(account.getAccountCode());
            accountDetail.setCreateDate(nowDate);
            accountDetail.setType("6");//风控关闭
            accountDetail.setTransAmount(notReceiveAmount);
            accountDetail.setRedOrderId(baseInfo.getRedOrderId());
            accountDetail.setRedmoney(account.getTotalAmount());
            accountDetail.setOrderNo(baseInfo.getRedOrderId()==null?"":baseInfo.getRedOrderId().toString());
            redAccountInfoDao.insertAccountDetail(accountDetail);
//          2.将红包发放记录表中对应的红包发放记录更改如下字段：
//          剩余金额处理方式 ：【平台回收】
            red.setRecoveryType("1");
//            剩余金额处理状态 ：【处理成功】
            red.setStatusRecovery("1");
            num = redEnvelopesGrantDao.updateStatusRisk(red);

//          3.将红包领取记录表中该红包剩余未领取记录更改如下字段：
            RedEnvelopesReceive receive = new RedEnvelopesReceive();
            receive.setRedOrderId(baseInfo.getRedOrderId());
//          领取状态：平台回收
            receive.setStatus("2");
            receive.setOldStatus("0");
//          领取时间：当前系统时间
            receive.setGetDate(nowDate);
            redEnvelopesReceiveDao.updateStatus(receive);

            Map<String, String> reasonMap = sysDictService.selectMapByKey("RISK_CLOSE_REASON");
            baseInfo.setReason(reasonMap.get(baseInfo.getReason()));
            baseInfo.setOptContent("风控关闭");
        }
        //风控开启红包
        if("0".equals(baseInfo.getStatus())){
//          1.对红包账户余额进行加减，并记录
            //将剩余金额累加到平台账户
            BigDecimal notReceiveAmount = redEnvelopesReceiveDao.totalReceiveAmount(baseInfo.getRedOrderId()
                    , "2");
            if(notReceiveAmount == null){
                notReceiveAmount = BigDecimal.ZERO;
            } else {
                notReceiveAmount = BigDecimal.ZERO.subtract(notReceiveAmount);
            }
            Long relationId = 0L;//平台ID
            redAccountInfoDao.updateAmount(relationId, notReceiveAmount);
            //插入收支明细
            RedAccountInfo account = redAccountInfoDao.selectByRelationId(relationId);
            RedAccountDetail accountDetail = new RedAccountDetail();
            accountDetail.setRedAccountId(account.getId());
            accountDetail.setAccountCode(account.getAccountCode());
            accountDetail.setCreateDate(nowDate);
            accountDetail.setType("7");//风控开启红包
            accountDetail.setTransAmount(notReceiveAmount);
            accountDetail.setRedOrderId(baseInfo.getRedOrderId());
            accountDetail.setRedmoney(account.getTotalAmount());
            accountDetail.setOrderNo(baseInfo.getRedOrderId()==null?"":baseInfo.getRedOrderId().toString());
            redAccountInfoDao.insertAccountDetail(accountDetail);
//          2.将红包发放记录表中对应的红包发放记录更改如下字段：
//          剩余金额处理方式 ：根据配置来
            //找出bus_type，根据业务类型，去对应的配置表，找到红包到期处理方式
            String recoveryType = "";
            String busType = redEnvelopesGrantDao.getBusType(baseInfo.getRedOrderId());
            //如果是个人红包
            if("0".equals(busType)){
                recoveryType = redUserConfDao.getRecoveryType();
            } else {
                recoveryType = redConfigDao.getRecoveryType(busType);
            }
            red.setRecoveryType(recoveryType);
//            剩余金额处理状态 ：【待处理】
            red.setStatusRecovery("0");
            num = redEnvelopesGrantDao.updateStatusRisk(red);

//          3.将红包领取记录表中该红包剩余未领取记录更改如下字段：
            RedEnvelopesReceive receive = new RedEnvelopesReceive();
            receive.setRedOrderId(baseInfo.getRedOrderId());
//          领取状态：待领取
            receive.setStatus("0");
            receive.setOldStatus("2");
//          领取时间：当前系统时间
            receive.setGetDate(null);
            redEnvelopesReceiveDao.updateStatus(receive);
            baseInfo.setOptContent("风控开启");
        }
        //4.插入风控操作记录
        redOrdersOptionDao.insert(baseInfo);
        return num;
    }


    @Override
    public int deleteRedEnvelopesGrantDiscuss(RedEnvelopesGrantDiscuss baseInfo) {
        int num =  redEnvelopesGrantDao.deleteRedEnvelopesGrantDiscuss(baseInfo.getId());
        //插入操作记录
        RedOrdersOption  redOrdersOption = new RedOrdersOption();
        redOrdersOption.setRedOrderId(baseInfo.getRedOrderId());
        redOrdersOption.setCreateDate(new Date());
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        redOrdersOption.setOptUserName(loginInfo.getUsername());
        redOrdersOption.setOptContent("删除评论ID" + baseInfo.getId());
        Map<String, String> reasonMap = sysDictService.selectMapByKey("DISCUSS_DELETE_REASON");
        redOrdersOption.setReason(reasonMap.get(baseInfo.getReason()));
        redOrdersOption.setRemark(baseInfo.getReamrk());
        redOrdersOptionDao.insert(redOrdersOption);
        return num;
    }
}
