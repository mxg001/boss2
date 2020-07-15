package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ManorService;
import cn.eeepay.framework.service.RedService;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.*;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 超级银行家-红包
 * @author tans
 * @date 2018-1-17
 */
@Service("redService")
public class RedServiceImpl implements RedService{

    private Logger log = LoggerFactory.getLogger(RedServiceImpl.class);


    @Resource
    private RedOrgRowImpl redOrgRowImpl;

    @Resource
    private RedControlDao redControlDao;

    @Resource
    private RedOrgDao redOrgDao;

    @Resource
    private RedConfigDao redConfigDao;

    @Resource
    private RedUserConfDao redUserConfDao;

    @Resource
    private OrgInfoDao orgInfoDao;

    @Resource
    private RedLuckConfDao redLuckConfDao;

    @Resource
    private RedAccountInfoDao redAccountInfoDao;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SuperBankService superBankService;
    
    @Resource
    private SysIdDao sysIdDao;

    @Resource
    private ManorService manorService;
    
    @Resource
    private RedOrgSortDao redOrgSortDao;
    
    @Resource
    private RedOrgSortRowImpl redOrgSortRowImpl;
    @Override
    public Result redConfig(Map<String,Object> params,Page<Map<String,Object>> page) {
        Result result = new Result();
        redConfigDao.redConfigQuery(params,page);
        Map<Long, String> orgMap = getOrgNameMap();
        for (Map<String,Object> map : page.getResult()){
            if("7".equals((String)map.get("bus_type"))){
                map.put("red_amount" ,"还款金额*" + map.get("single_scale") + "%");
            } else if("12".equals((String)map.get("bus_type"))){
            	map.put("red_amount" ,"交易金额*" + map.get("single_scale") + "%");
            } else if("17".equals((String)map.get("bus_type"))){
                map.put("red_amount" ,"领地交易溢价*" + map.get("manormoney_traderpremium") + "%");
            }
            else {
                map.put("red_amount" ,map.get("min_amount") + "~" + map.get("max_amount"));
            }
            map.put("push_org_name", orgMap.get((Long)map.get("push_org_id")));
        }
        result.setData(page);
        return result;
    }

    /**
     * 将所有orgInfo的orgId和orgName组成map
     * @return
     */
    public Map<Long, String> getOrgNameMap(){
        Map<Long, String> map = new HashMap<>();
        List<OrgInfo> list = orgInfoDao.getOrgInfoList();
        if(list != null && list.size() > 0){
            map.put(0L, "平台");
            for(OrgInfo info: list){
                map.put( info.getOrgId(), info.getOrgName());
            }
        }
        return map;
    }

    @Override
    public int addRedConfig(Map<String, Object> params) {
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        params.put("operator", loginInfo.getId());
       return redConfigDao.insert(params);
    }

    @Override
    public Map<String, Object> redConfigInfo(String id ) {

        return redConfigDao.redConfigInfo(id);
    }

    @Override
    public int redConfigInfoUpdate(Map params) {
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        params.put("operator", loginInfo.getId());
        int num = redConfigDao.redConfigInfoUpdate(params);
        return num;
    }

    @Override
    public int updateRedStatus(String id, String status) {
        return redConfigDao.updateRedStatus(id,status);
    }

    /**
     * 红包业务管理
     * @param page
     * @return
     */
    @Override
    public List<RedControl> selectRedControl(Page<RedControl> page) {
        return redControlDao.selectRedControl(page);
    }

    /**
     * 修改红包业务开关状态
     * @param redControl
     * @return
     */
    @Override
    public Result updateRedOpen(RedControl redControl) {
        Result result=new Result();
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        redControl.setUpdateTime(new Date());
        redControl.setOperator(loginUser.getId());
        int num =  redControlDao.updateRedOpen(redControl);
        if(num>0) {
            if(redControl.getOpenStatus()==1){
                if("18".equals(redControl.getBusType())||"20".equals(redControl.getBusType())) {
                    List<OrgInfo> orgInfos = orgInfoDao.selectCjdOrgInfo();
                    //根据组织id刷新组织(当修改组织信息时调用)
                    SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
                     ClientInterface.superBankPushByOrgIdAndUrl("0",sysDict.getSysValue()+Constants.REFRESH_ORG_INFO,null,null);
//                    if(orgInfos==null||orgInfos.size()<1) {
//                        result = openOem();
//                        return result;
//                    }
                }
            }
            result.setStatus(true);
        }
        return result;
    }

    /**
     * 修改红包业务是否开启组织控制
     * @param redControl
     * @return
     */
    @Override
    public int updateRedOrg(RedControl redControl) {
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        redControl.setUpdateTime(new Date());
        redControl.setOperator(loginUser.getId());
        int num=redControlDao.updateRedOrg(redControl);
        return num;
    }

    /**
     * 分页查询红包组织
     * @param redOrg
     * @param page
     * @return
     */
    @Override
    public List<RedOrg> selectRedOrg(RedOrg redOrg, Page<RedOrg> page) {
        return redOrgDao.selectRedOrg(redOrg, page);
    }

    /**
     * 删除红包组织，单个、批量
     * @param id
     * @return
     */
    @Override
    public Result deleteRedOrg(String id) {
        Result result = new Result();
        if(StringUtils.isBlank(id)){
            result.setMsg("参数不能为空");
            return result;
        }
        String[] list = id.split(",");
        int num = 0;
        for(int i = 0; i < list.length; i++){
            if(StringUtils.isNotBlank(list[i])){
                num += redOrgDao.delete(list[i]);
            }
        }
        if(num > 0){
            result.setStatus(true);
            result.setMsg("操作成功");
        } else {
            result.setMsg("操作失败");
        }
        return result;
    }

    /**
     * 批量操作红包组织，新增、删除
     * @param file
     * @param busCode
     * @return
     */
    @Override
    public Result importRedOrg(MultipartFile file, String busCode) {
        Result result = new Result();
        if(StringUtils.isBlank(busCode)){
            result.setMsg("业务编号不能为空");
            return result;
        }
        redOrgRowImpl.setBusCode(busCode);
        //校验excel file的大小和格式
        if (!CommonUtil.checkExcelFile(file, result)){
            return result;
        }
        try {
            List<ExcelErrorMsgBean> errors = new ArrayList<>();
            List<RedOrg> list = ExcelUtils.parseWorkbook(file.getInputStream(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    errors, redOrgRowImpl );
            int successSum = list == null ? 0 : list.size();
            int failSum = errors == null ? 0 : errors.size();
            String msg = "";
            String errMsg = "";
            if(successSum > 0){
                msg = "导入成功";
            } else {
                msg = "导入失败";
            }
            if(failSum > 0){
                log.info("导入失败，原因:{}", JSONObject.toJSONString(errors));
                errMsg = "，第" + errors.get(0).getRow() + "列，" +
                        "第" + errors.get(0).getLine() + "行：" +errors.get(0).getMessage();
            }
            result.setStatus(true);
            result.setMsg(msg + "，成功条数：" + successSum + "，失败条数：" + failSum + errMsg);
        } catch (Exception e) {
            log.error("批量操作红包组织导入异常", e);
        }
        return result;
    }

    /**
     * 新增红包组织
     * @param redOrg
     * @return
     */
    @Override
    public Result addRedOrg(RedOrg redOrg) {
        Result result = new Result();
        if(redOrg == null || redOrg.getOrgId() == null){
            result.setMsg("参数不能为空");
            return result;
        }
        OrgInfo orgInfo = orgInfoDao.selectOrg(redOrg.getOrgId());
        if(orgInfo == null){
            result.setMsg("组织不存在");
            return result;
        }
        //是否已存在
        if(checkExists(redOrg)){
            result.setMsg("组织已存在");
            return result;
        }
        redOrg.setOrgName(orgInfo.getOrgName());
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        redOrg.setCreateTime(new Date());
        redOrg.setOperator(loginUser.getId());
        int num = redOrgDao.insert(redOrg);
        if(num > 0){
            result.setStatus(true);
            result.setMsg("操作成功");
        } else {
            result.setMsg("操作失败");
        }
        return result;
    }

    /**
     * 检查组织是否已存在
     * bus_code和org_id组合唯一
     * @param redOrg
     * @return
     */
    @Override
    public boolean checkExists(RedOrg redOrg) {
        boolean status = false;
        if(redOrg == null){
            return status;
        }
        int num = redOrgDao.checkExists(redOrg);
        if(num > 0){
            status = true;
        }
        return status;
    }

    /**
     * 修改个人发放红包配置
     * @param conf
     * @return
     */
    @Override
    public int updateRedUserConf(RedUserConf conf) {
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        conf.setOperator(loginUser.getId());
        conf.setUpdateTime(new Date());
        return redUserConfDao.update(conf);
    }

    /**
     * 获取个人发放红包配置
     * 取表里面createTime最新的那条数据
     * @return
     */
    @Override
    public RedUserConf getRedUserConf() {
        return redUserConfDao.getRedUserConf();
    }

    @Override
    public Map<String, Object> redProductInfo(String type) {
        //由于表与表之间的业务类型定的不一样，需要用map来转换
        Map<String, String> productTypeMap = getProductTypeMap();
        type = productTypeMap.get(type) == null ? type : productTypeMap.get(type);
        Map<String, Object> info =  redConfigDao.redProductInfo(type);
        if("1".equals(type)){
            Map<String, Object> userMap = sysIdDao.select("red_first_user");
            Map<String, Object> moneyMap = sysIdDao.select("red_first_money");
            if(userMap != null){
                info.put("red_first_user", userMap.get("key_value"));
            }
            if(moneyMap != null){
                info.put("red_first_money", moneyMap.get("key_value"));
            }
        }

        return info;
    }

    @Override
    public int redProductInfoUpdate(Map params) {
        Result result = new Result();
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        params.put("operator",loginUser.getId());
        int num= redConfigDao.redProductInfoUpdate(params);
        String confType = (String)params.get("conf_type");
        //如果是每日登录红包，需要更新大奖用户管理配置
        if("1".equals(confType)){
            String red_first_user = (String)params.get("red_first_user");
            String red_first_money = (String)params.get("red_first_money");
        
           // sysIdDao.update("red_first_user", red_first_user);
           // sysIdDao.update("red_first_money", red_first_money);
            
            //2018-7-9 维护新表     
            sysIdDao.updateSysPinfuChaju("red_first_user", red_first_user);
            sysIdDao.updateSysPinfuChaju("red_first_money", red_first_money);
        }
        return num;
    }

    /**
     *  业务类型(0个人发红包1新用户红包、2信用卡奖励红包、3贷款奖励红包、4登录红包、5敲门红包)
     *  管理类型(1每日登陆红包、2新用户红包、3信用卡奖励、4贷款奖励)
     * @return
     */
    public Map<String, String> getProductTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "2");
        map.put("2", "3");
        map.put("3", "4");
        map.put("4", "1");
        return map;
    }

    @Override
    public RedLuckConf getLuckConf() {
        RedLuckConf baseInfo = redLuckConfDao.getLuckConf();
        if(baseInfo != null){
            if(baseInfo.getSingleCommentValue() != null
                    && !baseInfo.getSingleCommentValue().startsWith("+")
                    && !baseInfo.getSingleCommentValue().startsWith("-")){
                baseInfo.setSingleCommentValue("+" + baseInfo.getSingleCommentValue());
            }
            if(baseInfo.getPushRedValue() != null
                    && !baseInfo.getPushRedValue().startsWith("+")
                    && !baseInfo.getPushRedValue().startsWith("-")){
                baseInfo.setPushRedValue("+" + baseInfo.getPushRedValue());
            }
            if(baseInfo.getFirstCommentValue() != null
                    && !baseInfo.getFirstCommentValue().startsWith("+")
                    && !baseInfo.getFirstCommentValue().startsWith("-")){
                baseInfo.setFirstCommentValue("+" + baseInfo.getFirstCommentValue());
            }
        }
        return baseInfo;
    }

    @Override
    public Result updateLuckConf(RedLuckConf baseInfo) {
        Result result = new Result();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        baseInfo.setOperator(loginInfo.getId());
        baseInfo.setUpdateTime(new Date());
        redLuckConfDao.updateLuckConf(baseInfo);
        result.setStatus(true);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 检查红包配置是否使用
     * @param id
     * @return
     */
    @Override
    public int configUseStatus(String id) {
        return redConfigDao.configUseStatus(id);
    }

    @Override
    public List<RedAccountDetail> selectAccountDetailPage(RedAccountDetail baseInfo, Page<RedAccountDetail> page) {
        redAccountInfoDao.selectAccountDetailPage(baseInfo, page);
        List<RedAccountDetail> list = page.getResult();
        if(list != null && list.size() > 0){
            Map<String, String> redType = sysDictService.selectMapByKey("RED_TYPE");
            Map<String, String> busType = sysDictService.selectMapByKey("RED_BUS_TYPE");
          //(0平台、1品牌商、2个人)
            Map<String, String> userType = new HashMap<>();
            userType.put("0", "平台");
            userType.put("1", "品牌商");
            userType.put("2", "个人");
            for(RedAccountDetail info: list){
                info.setAccountName("红包账户");
                info.setUserType(userType.get(baseInfo.getUserType()));
                info.setTypeInter(info.getType());
                info.setType(redType.get(info.getType()));
                info.setBusType(busType.get(info.getBusType()));
                info.setCreateDateStr(DateUtil.getLongFormatDate(info.getCreateDate()));
                //***************************************
                BigDecimal transAmount = info.getTransAmount().abs();
                if(info.getType().equals("0")){
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }
                else if(info.getType().equals("5")) {
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }else if(info.getType().equals("7")){
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }
                else if(info.getType().equals("9")){
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }
                else if(info.getType().equals("13")){
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }
                info.setTransAmountStr(""+transAmount);

                //*****************************************
            }
        }
        return list;
    }
    
    public List<RedAccountDetail> selectAccountDetailPageReload(RedAccountDetail baseInfo, Page<RedAccountDetail> page) {
        redAccountInfoDao.selectAccountDetailPage(baseInfo, page);
        List<RedAccountDetail> list = page.getResult();
        if(list != null && list.size() > 0){
            Map<String, String> redType = sysDictService.selectMapByKey("RED_TYPE");
            Map<String, String> busType = sysDictService.selectMapByKey("RED_BUS_TYPE");
            for(RedAccountDetail info: list){
                //***************************************
                BigDecimal transAmount = info.getTransAmount().abs();
                if(info.getType().equals("0")){
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }
               else if(info.getType().equals("5")) {
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }else if(info.getType().equals("7")){
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }
                else if(info.getType().equals("9")){
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }
                else if(info.getType().equals("13")){
                    transAmount = new BigDecimal("0").subtract(transAmount);
                }
                BigDecimal before =new BigDecimal("0");
                if(info.getRedmoney()!=null){
                    before=info.getRedmoney().subtract(transAmount);
                }
                info.setTransAmountStr(""+transAmount);
                info.setBeforetransredmoneytStr(before + "");

                //*****************************************
                info.setAccountName("红包账户");
                if (baseInfo != null && baseInfo.getMethodType() != null && baseInfo.getMethodType() == 1){

                }else{
                    info.setUserType(baseInfo.getUserType());
                }
                info.setTypeInter(info.getType());
                info.setType(redType.get(info.getType()));
                info.setBusType(busType.get(info.getBusType()));
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                info.setCreateDateStr(df.format(info.getCreateDate()));
            }
        }
        return list;
    }
    
    @Override
    public List<ManorAccountDetail> selectManorAccountDetailPage(ManorAccountDetail baseInfo, Page<ManorAccountDetail> page) {
        redAccountInfoDao.selectManorAccountDetailPage(baseInfo, page);
        
        List<OrgInfo> orgList = superBankService.getOrgInfoList();
        
        Map<Long,String> orgmap = new HashMap<>();
        for (OrgInfo orgInfo : orgList) {
        	orgmap.put(orgInfo.getOrgId(), orgInfo.getOrgName());
		}
        
        List<ManorAccountDetail> list = page.getResult();
        if(list != null && list.size() > 0){
        	for (ManorAccountDetail manorAccountDetail : list) {
        		manorAccountDetail.setAccountName("红包账户");
        		if ("1".equals(manorAccountDetail.getType())) {
        			manorAccountDetail.setUserName(orgmap.get(manorAccountDetail.getRelationId()));
				}
			}
        }
        return list;
    }

    @Override
    public RedAccountInfo plateAccountInfo() {
        return redAccountInfoDao.plateAccountInfo();
    }

    @Override
    public RedAccountInfo plateAccountInfoReload(Long id) {
        return redAccountInfoDao.plateAccountInfoReload(id);
    }

    
    @Override
    public void exportPlateAccountDetail(HttpServletResponse response, RedAccountDetail detail) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
      //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        OutputStream ouputStream = null;
        try {
            Page<RedAccountDetail> page = new Page<>(0, Integer.MAX_VALUE);
            List<RedAccountDetail> list = selectAccountDetailPage(detail, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "收支明细"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(RedAccountDetail item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("accountName", item.getAccountName());
                map.put("userType", item.getUserType());
                map.put("type", item.getType());
                map.put("redOrderId", item.getRedOrderId()==null?"":item.getRedOrderId().toString());
                if (StringUtils.isNotBlank(item.getTypeInter()) && Integer.valueOf(item.getTypeInter())>=8) {
                	map.put("busType", "领地开关");
				}else
                map.put("busType", item.getBusType());
                map.put("createDateStr", item.getCreateDateStr());
                map.put("transAmountStr", item.getTransAmountStr());
                map.put("redmoney", item.getRedmoney()==null?"":item.getRedmoney().toString());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            String[] cols = new String[]{"orderNo",
                    "accountName","userType","type","redOrderId","busType",
                    "createDateStr","transAmountStr","redmoney","remark"
                    };
            String[] colsName = new String[]{"订单编号",
                    "账户名称","用户类型","变动类型","对应红包ID","红包业务类型",
                    "记账时间","金额","红包账户金额","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出平台红包账户明细异常", e);
        } finally {
            try {
                if(ouputStream!=null){
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exportRedAccountDetail(HttpServletResponse response, RedAccountDetail detail) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        OutputStream ouputStream = null;
        try {
            Page<RedAccountDetail> page = new Page<>(0, Integer.MAX_VALUE);
            List<RedAccountDetail> list = selectAccountDetailPageReload(detail, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "收支明细"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List data = new ArrayList() ;
            Map map = null;
            for(RedAccountDetail item: list){
                map = new HashMap<>();
                map.put("id",item.getId());
                map.put("createDateStr", item.getCreateDateStr());

                map.put("transAmountStr", item.getTransAmountStr());
                map.put("beforetransredmoneytStr",item.getBeforetransredmoneytStr());

                map.put("redmoney", item.getRedmoney()==null?"":item.getRedmoney().toString());
                map.put("accountName", item.getAccountName());
                String userType=item.getUserType();
                //(0平台、1品牌商、2个人)
                if("0".equals(userType)){
                    userType="平台";
                }
                if("1".equals(userType)){
                    userType="品牌商";
                }
                if("2".equals(userType)){
                    userType="个人";
                }
                map.put("userType", userType);
                map.put("relationId",item.getRelationId());
               /* map.put("userOrgName", item.getUserOrgName());*/
                map.put("orderNo", item.getOrderNo());
                map.put("type", item.getType());
                map.put("redOrderId", item.getRedOrderId()==null?"":item.getRedOrderId().toString());
                if (StringUtils.isNotBlank(item.getTypeInter()) && Integer.valueOf(item.getTypeInter())>=8) {
                    map.put("busType", "领地开关");
                }else
                    map.put("busType", item.getBusType());
                data.add(map);
            }
            String[] cols = new String[]{"id",
                    "createDateStr","transAmountStr","beforetransredmoneytStr","redmoney","accountName",
                    "userType","relationId","orderNo","type","redOrderId","busType"
            };
            String[] colsName = new String[]{"记账流水号",
                    "记账时间","记账金额","记账前账户余额","记账后账户余额","账户名称",
                    "用户类型","用户ID/组织编号","关联订单号","变动类型","对应红包ID","红包业务类型"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出红包账户明细异常", e);
        } finally {
            try {
                if(ouputStream!=null){
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void exportPlateAccountDetail(HttpServletResponse response, ManorAccountDetail detail) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        OutputStream ouputStream = null;
        try {
            Page<ManorAccountDetail> page = new Page<>(0, Integer.MAX_VALUE);
            List<ManorAccountDetail> list = selectManorAccountDetailPage(detail, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "红包账户查询"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            
            Map<String, String> userType = new HashMap<>();
            userType.put("0", "平台");
            userType.put("1", "品牌商");
            userType.put("2", "个人");
            
            Map<String,String> map = null;
            
            
            for(ManorAccountDetail item: list){
                map = new HashMap<>();
                map.put("accountName", item.getAccountName());
                map.put("userType", userType.get(item.getType()));
                map.put("tempId", item.getRelationId()==null?"":item.getRelationId().toString());
                map.put("userName", item.getUserName());
                map.put("totalAmount", item.getTotalAmount()==null?"":item.getTotalAmount().toString());
                data.add(map);
            }
            String[] cols = new String[]{
                    "accountName","userType","tempId","userName","totalAmount"};
            String[] colsName = new String[]{
                    "账户名称","用户类型","用户ID／组织编号","用户/组织名称","可用金额"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出平台红包账户明细异常", e);
        } finally {
            try {
                if(ouputStream!=null){
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void exportManorTransactionRecore(HttpServletResponse response, ManorTransactionRecore detail) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        OutputStream ouputStream = null;
        try {
            Page<ManorTransactionRecore> page = new Page<>(0, Integer.MAX_VALUE);
            
            List<ManorTransactionRecore> list = manorService.selectManorTransactionRecorePage(detail, page);
            int size = 2;
            ListDataExcelExport export = new ListDataExcelExport(size);
            String fileName = "领地买卖交易记录"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            
            //{"text":"专员","value":"20"},{"text":"经理","value":"30"},{"text":"银行家","value":"40"}]
            Map<String, String> userType = new HashMap<>();
            userType.put("20", "专员");
            userType.put("30", "经理");
            userType.put("40", "银行家");
            ////'订单状态:1已创建；2待支付；3:待审核 4已授权 5订单成功 6订单失败 7已办理过  9已关闭'
            Map<String, String> statusList = new HashMap<>();
            statusList.put("1", "已创建");
            statusList.put("2", "待支付");
            statusList.put("3", "待审核");
            statusList.put("4", "已授权");
            statusList.put("5", "订单成功");
            statusList.put("6", "订单失败");
            statusList.put("7", "已办理过");
            statusList.put("9", "已关闭");
            statusList.put("18", "已支付");
            statusList.put("19", "已退款");
            //'记账状态;0待入账；1已记账；2记账失败',
            Map<String, String> accountStatusList = new HashMap<>();
          accountStatusList.put("0", "待入账");
          accountStatusList.put("1", "已记账");
          accountStatusList.put("2", "记账失败");
            //1 微信，2 支付宝，3 快捷，4红包账户，5分润账户',
            Map<String, String> payMethodList = new HashMap<>();
           payMethodList.put("1", "微信");
           payMethodList.put("2", "支付宝");
           payMethodList.put("3", "快捷支付");
           payMethodList.put("4", "红包账户");
           payMethodList.put("5", "分润账户");
            ////收款通道   {text:"微信官方",value:"wx"},{text:"支付宝官方",value:"ali"}]
            Map<String, String> payChannelStatusList = new HashMap<>();
           payChannelStatusList.put("wx", "微信官方");
           payChannelStatusList.put("ali", "支付宝官方");
           payChannelStatusList.put("kj", "中钢银通");
            Map<String,String> map = null;
            
            for(ManorTransactionRecore item: list){
                map = new HashMap<>();
                map.put("id", item.getOrderId()==null?"":item.getOrderId().toString());
                map.put("orderNo", item.getOrderNo());
                map.put("territoryId", item.getTerritoryId()==null?"":item.getTerritoryId().toString());
                map.put("payDate", item.getPayDate()==null?"":sdf2.format(item.getPayDate()));
                
                map.put("provinceName", item.getProvinceName());
                map.put("cityName", item.getCityName());
                map.put("regionName", item.getRegionName());
                
                map.put("status", statusList.get(item.getStatus()));
                map.put("accountStatus", accountStatusList.get(item.getAccountStatus()));
                map.put("oldLordsOrgName", item.getOldLordsOrgName());
                map.put("oldLordsUserCode", item.getOldLordsUserCode());
                map.put("userName", item.getUserName());
                map.put("nickName", item.getNickName());
                map.put("phone", item.getPhone());
                map.put("oldPayDate", item.getOldPayDate()==null?"":sdf2.format(item.getOldPayDate()));
                map.put("oldPayPrice", item.getOldPayPrice()==null?"":item.getOldPayPrice().toString());
                map.put("price", item.getPrice()==null?"":item.getPrice().toString());
                
                map.put("premiumPrice", item.getPremiumPrice()==null?"":item.getPremiumPrice().toString());
                map.put("oldTotalProfit", item.getOldTotalProfit()==null?"":item.getOldTotalProfit().toString());
                map.put("tradeProfit", item.getTradeProfit()==null?"":item.getTradeProfit().toString());
                
                map.put("newLordsOrgName", item.getNewLordsOrgName());
                map.put("newLordsUserCode", item.getNewLordsUserCode());
                map.put("newUserName", item.getNewUserName());
                map.put("newNickName", item.getNewNickName());
                map.put("newPhone", item.getNewPhone());
                map.put("tradeFeeConf", item.getTradeFeeConfStr());
                map.put("tradeFee",  String.valueOf(item.getTradeFee()));
               
                map.put("payMethod", payMethodList.get(item.getPayMethod()));
                map.put("payChannel", payChannelStatusList.get(item.getPayChannel()));
                map.put("payChannelNo", item.getPayChannelNo());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("totalBonus", item.getTotalBonus()==null?"":item.getTotalBonus().toString());
                
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", userType.get(item.getOneUserType()));
                map.put("oneUserProfit", item.getOneUserProfit());
                
                map.put("twoUserCode", item.getOneUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", userType.get(item.getTwoUserType()));
                map.put("twoUserProfit", item.getTwoUserProfit());
                
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", userType.get(item.getThrUserType()));
                map.put("thrUserProfit", item.getThrUserProfit());
                
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", userType.get(item.getFouUserType()));
                map.put("fouUserProfit", item.getFouUserProfit());
                
                map.put("orgProfit", item.getOrgProfit()==null?"":item.getOrgProfit().toString());
                map.put("plateProfit", item.getPlateProfit()==null?"":item.getPlateProfit().toString());
                
                data.add(map);
            }
            String[] cols = new String[]{"id",
                    "orderNo","territoryId","payDate","provinceName","cityName","regionName",
                    "status","accountStatus","oldLordsOrgName","oldLordsUserCode","userName","nickName",
                    "phone","oldPayDate","oldPayPrice","price","tradeFeeConf","tradeFee","premiumPrice","oldTotalProfit",
                    "tradeProfit","newLordsOrgName","newLordsUserCode","newUserName","newNickName","newPhone",
                    "payMethod","payChannel","payChannelNo","payOrderNo","totalBonus",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",                    
                    "twoUserCode","twoUserName","twoUserType","twoUserProfit",                    
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",                    
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",                    
                    "orgProfit","plateProfit"                    
            };
            String[] colsName = new String[]{"id",
                    "交易订单编号","领地ID","交易时间","省","市","区",
                    "订单状态","记账状态","原领主所属组织","原领主ID","原领主姓名","原领主昵称",
                    "原领主手机号","原领主买入时间","原领主买入价格","转让价格","领地交易手续费率","领地交易手续费","溢价","原领主累计收益",
                    "原领主转让收益","新领主所属组织","新领主ID","新领主姓名","新领主昵称","新领主手机号",
                    "支付方式","收款通道","收款通道商户号","收款通道流水号","发放总奖金",
                    "一级编号","一级姓名","一级身份","一级分润",
                    "二级编号","二级姓名","二级身份","二级分润",
                    "三级编号","三级姓名","三级身份","三级分润",
                    "四级编号","四级姓名","四级身份","四级分润",
                    "当前领主所属组织分润","平台分润"
            };
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出领地交易记录异常", e);
        } finally {
            try {
                if(ouputStream!=null){
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RedAccountDetail selectAccountDetailSum(RedAccountDetail baseInfo) {
        return redAccountInfoDao.selectAccountDetailSum(baseInfo);
    }

    @Override
    public RedAccountDetail findAccountDetailSum(RedAccountDetail baseInfo) {
        RedAccountDetail redAccountDetail=redAccountInfoDao.selectAccountDetailSum(baseInfo);
        if(redAccountDetail!=null) {
            redAccountDetail.setIncomeTransAmountSum(
                    redAccountDetail.getM1()
                            .add(redAccountDetail.getM2())
                            .add(redAccountDetail.getM3())
                            .add(redAccountDetail.getM4())
                            .add(redAccountDetail.getM8())
                            .add(redAccountDetail.getM10())
                            .add(redAccountDetail.getM11())
                            .add(redAccountDetail.getM12())
                            .add(redAccountDetail.getM6()));
            redAccountDetail.setLossTransAmountSum(
                    new BigDecimal(0)
                            .subtract(redAccountDetail.getM0())
                            .subtract(redAccountDetail.getM5())
                            .subtract(redAccountDetail.getM9())
                            .subtract(redAccountDetail.getM13())
                            .subtract(redAccountDetail.getM7())
            );
            redAccountDetail.setTransAmountSum(
                    redAccountDetail.getM1()
                            .add(redAccountDetail.getM2())
                            .add(redAccountDetail.getM3())
                            .add(redAccountDetail.getM4())
                            .add(redAccountDetail.getM8())
                            .add(redAccountDetail.getM10())
                            .add(redAccountDetail.getM11())
                            .add(redAccountDetail.getM12())
                            .add(redAccountDetail.getM6())
                            .subtract(redAccountDetail.getM0())
                            .subtract(redAccountDetail.getM5())
                            .subtract(redAccountDetail.getM9())
                            .subtract(redAccountDetail.getM13())
                            .subtract(redAccountDetail.getM7())
            );
        }
        return redAccountDetail;
    }
    
    @Override
    public ManorAccountDetail selectManorAccountDetailSum(ManorAccountDetail baseInfo) {
    	return redAccountInfoDao.selectManorAccountDetailSum(baseInfo);
    }

    public Result openOem() {
        Result result = new Result();
        RSA rsa = SecureUtil.rsa();
        String publicKey = rsa.getPublicKeyBase64();
        String privateKey = rsa.getPrivateKeyBase64();
        String oem_no = "";
        String app_key = "";
        String service_name = "";

        //接32位不唯一请求ID，相同请求ID会请求到缓存数据

        SysDict oem = sysDictService.getByKey("CREATE_EXCHANGE_OEM");
        String address = oem.getSysValue();
        oem = sysDictService.getByKey("SUPER_BANK_EXCHANGE");
        String tokenUrl = oem.getSysValue();
        String url = address.concat(RandomUtil.simpleUUID());
        oem = sysDictService.getByKey("SUPER_BANK_EXCHANGE_ORG_ID");
        String orgId=oem.getSysValue();
        oem = sysDictService.getByKey("POINT_EXCHANGE_RATIO");
        String pointExchangeRatio=oem.getSysValue();
        oem = sysDictService.getByKey("PLATFORM_COST");
        String platformCost=oem.getSysValue();
        Map params = new HashMap();
        {
            {//OEM注册,注册成功后,会响应oem_no,app_key
                service_name = "oemRegister";
                params.put("agent_no", orgId);
                params.put("public_key", publicKey);
                params.put("order_call_back", tokenUrl);
                params.put("company_name", "超级银行家");
                params.put("share_rate", pointExchangeRatio);
                params.put("oem_fee", platformCost);
                params.put("company_id", orgId);
            }

            oem_no = "OPEN10042";
            app_key = "6313f2850d09f4d3d80693152429b340";
        }
        String CjdOemOn;
        String CjdAppKey;
        try {
            //使用RSA私钥，对数据进行加密
            params.put("oem_no", oem_no);
            params.put("app_key", app_key);
            params.put("service_name", service_name);
            log.info("超级银行家开通oem请求参数========"+JSONObject.toJSONString(params));
            String res = HttpUtil.post(url, params);
            JSONObject json = JSONObject.parseObject(res);
            log.info("超级兑返回参数========"+json);
            String status = json.getString("status");
            if (status.equals("200")) {
                OrgInfo org=new OrgInfo();
                String data = json.getString("data");
                JSONObject jsonData = JSONObject.parseObject(data);
                CjdOemOn = jsonData.getString("oem_no");
                CjdAppKey = jsonData.getString("app_key");
                result.setStatus(true);
                org.setPointExchangeRatio(Integer.valueOf(pointExchangeRatio));
                org.setPlatformCost(new BigDecimal(platformCost));
                org.setPublicKey(publicKey);
                org.setPrivateKey(privateKey);
                org.setCjdOemOn(CjdOemOn);
                org.setCjdAppKey(CjdAppKey);
                org.setUpdateDate(new Date());
                orgInfoDao.updateExchange(org);
            } else {
                result.setMsg("开通OEM失败 请联系开发人员");
                return result;
            }
        } catch (Exception e) {
            result.setMsg("开通OEM失败 请联系开发人员");
            return result;
        }
        return result;
    }

	@Override
	public RedControl selectRedControlByPrimaryKey(Long id) {
		return redControlDao.selectByPrimaryKey(id);
	}

	@Override
	public String getSysPinfuChajuKey(String key) {
		return redControlDao.getSysPinfuChajuKey(key);
	}

	@Override
	public List<RedOrg> selectRedOrgListAll(RedOrg redOrg) {
		List<RedOrg> redOrgList= redOrgDao.selectRedOrgListAll(redOrg);
		return redOrgList;
	}

	@Override
	public List<RedOrgSort> selectRedOrgSort(RedOrgSort redOrgSort, Page<RedOrgSort> page) {
		return redOrgSortDao.selectRedOrgSort(redOrgSort, page);
	}

	@Override
	public int deleteRedOrgSort(RedOrgSort redOrgSort) {
		return redOrgSortDao.deleteRedOrgSort(redOrgSort);
	}

	@Override
	public int addRedOrgSort(RedOrgSort redOrgSort) {		
		UserLoginInfo loginUser = CommonUtil.getLoginUser();
		redOrgSort.setOperator(loginUser.getId());
		int count = redOrgSortDao.insertRedOrgSort(redOrgSort);
		
		Long orgId = redOrgSort.getOrgId();
		if(-1 != orgId) {
			RedOrg redOrg = new RedOrg();
    		redOrg.setBusCode(redOrgSort.getBusCode());
    		redOrg.setOrgId(orgId);
    		ignoreAddRedOrg(redOrg);
		}
		
		return count;
	}

	@Override
	public int editRedOrgSort(RedOrgSort redOrgSort) {		
		UserLoginInfo loginUser = CommonUtil.getLoginUser();
		redOrgSort.setOperator(loginUser.getId());
		int count = redOrgSortDao.updateRedOrgSort(redOrgSort);
		
		Long orgId = redOrgSort.getOrgId();
		if(-1 != orgId) {
			RedOrg redOrg = new RedOrg();
    		redOrg.setBusCode(redOrgSort.getBusCode());
    		redOrg.setOrgId(orgId);
    		ignoreAddRedOrg(redOrg);
		}
		return count;
	}

	@Override
	public int ignoreAddRedOrg(RedOrg redOrg) {
        OrgInfo orgInfo = orgInfoDao.selectOrg(redOrg.getOrgId());
        if(null == orgInfo) {
        	return 0;
        }
        redOrg.setOrgName(orgInfo.getOrgName());
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        redOrg.setCreateTime(new Date());
        redOrg.setOperator(loginUser.getId());
		return redOrgDao.insertIgnore(redOrg);
	}

	@Override
	public Result importRedOrgSort(MultipartFile file, String busCode,Integer orgStatus) {
		Result result = new Result();
        if(StringUtils.isBlank(busCode)){
            result.setMsg("业务编号不能为空");
            return result;
        }
        redOrgSortRowImpl.setBusCode(busCode);
        redOrgSortRowImpl.setOrgStatus(orgStatus);
        
        //校验excel file的大小和格式
        if (!CommonUtil.checkExcelFile(file, result)){
            return result;
        }
        try {
            List<ExcelErrorMsgBean> errors = new ArrayList<>();
            List<RedOrgSort> list = ExcelUtils.parseWorkbook(file.getInputStream(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    errors, redOrgSortRowImpl );
            int successSum = list == null ? 0 : list.size();
            int failSum = errors == null ? 0 : errors.size();
            String msg = "";
            String errMsg = "";
            if(successSum > 0){
                msg = "导入成功";
            } else {
                msg = "导入失败";
            }
            if(failSum > 0){
                log.info("导入失败，原因:{}", JSONObject.toJSONString(errors));
                errMsg = "，第" + errors.get(0).getLine()  + "行，" +
                        "第" + errors.get(0).getRow() + "列：" +errors.get(0).getMessage();
            }
            result.setStatus(true);
            result.setMsg(msg + "，成功条数：" + successSum + "，失败条数：" + failSum + errMsg);
        } catch (Exception e) {
            log.error("批量操作红包业务组织分类布局导入异常", e);
        }
        return result;
	}
}
