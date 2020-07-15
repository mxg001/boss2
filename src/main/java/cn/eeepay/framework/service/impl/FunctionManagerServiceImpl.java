package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.dao.VasInfoDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.function.AppShowConfig;
import cn.eeepay.framework.model.function.FunctionTeam;
import cn.eeepay.framework.service.VasInfoService;
import cn.eeepay.framework.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.FunctionManagerDao;
import cn.eeepay.framework.service.FunctionManagerService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.BossBaseException;

@Service("functionManagerService")
@Transactional
public class FunctionManagerServiceImpl implements FunctionManagerService {
    @Resource
    private FunctionManagerDao functionManagerDao;

    @Resource
    private VasInfoService vasInfoService;

    @Resource
    private VasInfoDao vasInfoDao;

    @Autowired
    private SysDictService sysDictService;

    @Resource
    private SeqService seqService;


    @Override
    public List<FunctionManager> selectFunctionManagers() {
        return functionManagerDao.selectFunctionManagers();
    }

    @Override
    public int updateFunctionSwitch(FunctionManager info) {
        return functionManagerDao.updateFunctionSwitch(info);
    }

    @Override
    public int updateVasRateStatus(VasRate info) {
        return functionManagerDao.updateVasRateStatus(info);
    }

    @Override
    public int updateFunctionManageConfigStatus(FunctionTeam info) {
        return functionManagerDao.updateFunctionManageConfigStatus(info);
    }

    @Override
    public int updateAgentControl(FunctionManager info) {
        return functionManagerDao.updateAgentControl(info);
    }

    @Override
    public FunctionManager getFunctionManager(int id) {
        return functionManagerDao.getFunctionManager(id);
    }

    @Override
    public FunctionManager getFunctionManagerByNum(String funcNum) {
        return functionManagerDao.getFunctionManagerByNum(funcNum);
    }

    @Override
    public VasRate getVasRateById(int id) {
        return functionManagerDao.getVasRateById(id);
    }

    @Override
    public IndustrySwitchInfo getIndustrySwitchInfo() {
        IndustrySwitchInfo info = new IndustrySwitchInfo();
        // 获取总开关
        String value = sysDictService.getValueByKey("INDUSTRY_SWITCH");
        info.setIndustrySwitch(Integer.valueOf(value));
        // 获取行业切换信息
        List<IndustrySwitch> list = functionManagerDao.findAll();
        // 获取所有商户类别
        List<SysDict> merchantList = sysDictService.getAcqMerchantList("ACQ_MERCHANT_TYPE");
        for (IndustrySwitch industrySwitch : list) {
            industrySwitch.setStartTime(
                    industrySwitch.getStartTime().substring(0, industrySwitch.getStartTime().lastIndexOf(":")));
            industrySwitch
                    .setEndTime(industrySwitch.getEndTime().substring(0, industrySwitch.getEndTime().lastIndexOf(":")));

            for (SysDict sysDict : merchantList) {
                String acqMerchantType = industrySwitch.getAcqMerchantType();
                if (acqMerchantType.equals(sysDict.getSysValue())) {
                    industrySwitch.setAcqMerchantTypeName(sysDict.getSysName());
                }
            }
        }
        info.setIndustrySwitchList(list);
        info.setSysDicts(merchantList);
        return info;
    }

    @Override
    public void industrySwitchSave(IndustrySwitch data) {
        String startTime = data.getStartTime();
        String endTime = data.getEndTime();
        // 修改
        if (data.getId() != null) {
            // 先删除
            this.industrySwitchDelete(data.getId());
            this.containTimeSection(startTime, endTime);
            if ("23:59".equals(endTime)) {
                endTime = endTime + ":59";
                data.setEndTime(endTime);
                functionManagerDao.industrySwitchSaveAllData(data);
            } else {
                functionManagerDao.industrySwitchSaveAll(data);
            }

        } else {
            data.setCreateTime(new Date());
            this.containTimeSection(startTime, endTime);
            if ("23:59".equals(endTime)) {
                endTime = endTime + ":59";
                data.setEndTime(endTime);
                functionManagerDao.industrySwitchSaveData(data);
            } else {
                functionManagerDao.industrySwitchSave(data);
            }

        }
    }


    private void containTimeSection(String startTime, String endTime) {
		/*
		String formatDate = null;
		try {
			Date parseDate = DateUtils.parseDate(startTime+":00", DateUtil.TIME_FORMAT);
			parseDate= DateUtils.addSeconds(parseDate, 1);
			formatDate = DateUtil.getFormatDate(DateUtil.TIME_FORMAT, parseDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		*/
        // 判断时间是否在其他区间
        long count = functionManagerDao.industrySwitchCount(startTime, endTime);
        if (count > 0) {
            throw new BossBaseException("");
        }
    }

    @Override
    public void industrySwitchDelete(Long id) {
        functionManagerDao.industrySwitchDelete(id);
    }

    @Override
    public void industrySwitchUpdate(Integer industrySwitch) {
        functionManagerDao.updateSysDictValue("INDUSTRY_SWITCH", industrySwitch);
    }

    @Override
    public int updateBaseInfo(FunctionManager baseInfo) {
        return functionManagerDao.updateBaseInfo(baseInfo);
    }

    @Override
    public List<FunctionTeam> getFunctionTeamList(String functionNumber) {
        List<FunctionTeam> list = functionManagerDao.getFunctionTeamList(functionNumber);
        if (list != null && list.size() > 0) {
            for (FunctionTeam item : list) {
                JSONObject jsonObject = JSONObject.parseObject(item.getBakJson());
                if ("066".equals(functionNumber)) {
                    item.setActivity_days(jsonObject.getInteger("activity_days"));
                    item.setActivity_trans_amount(jsonObject.getInteger("activity_trans_amount"));
                } else if ("069".equals(functionNumber)) {
                    item.setActivity_type_nos(jsonObject.getString("activity_type_nos"));
                    item.setCycle(jsonObject.getInteger("cycle"));
                    item.setRemarks(jsonObject.getString("remarks"));
                } else if ("070".equals(functionNumber) || "071".equals(functionNumber)) {
                    item.setActivity_days(jsonObject.getInteger("day_number"));
                    item.setBeginTime(jsonObject.getString("begin_time"));
                    item.setEndTime(jsonObject.getString("end_time"));
                } else if ("072".equals(functionNumber)) {
                    item.setTxFee(jsonObject.getBigDecimal("tx_fee"));
                } else if ("074".equals(functionNumber)) {
                    item.setEffectiveTime(jsonObject.getString("effective_time"));
                    item.setInvalidTime(jsonObject.getString("invalid_time"));
                    item.setActLimitDay(jsonObject.getInteger("act_limit_day"));

                }
            }
        }
        return list;
    }

    @Override
    public FunctionTeam getFunctionTeamById(String functionNumber, int id) {
        FunctionTeam item = functionManagerDao.getFunctionTeamById(functionNumber, id);
        if (item != null) {
            JSONObject jsonObject = JSONObject.parseObject(item.getBakJson());
            if ("072".equals(functionNumber)) {
                item.setTxFee(jsonObject.getBigDecimal("tx_fee"));
            } else if ("074".equals(functionNumber)) {
                item.setEffectiveTime(jsonObject.getString("effective_time"));
                item.setInvalidTime(jsonObject.getString("invalid_time"));
                item.setActLimitDay(jsonObject.getInteger("act_limit_day"));
            }
        }
        return item;
    }

    @Override
    public List<VasRate> getVasRateList(String functionNumber) {
        List<VasRate> list = null;
        VasServiceInfo vasInfo = vasInfoDao.getVasServiceInfo(functionNumber, "1");
        if (vasInfo != null) {
            List<SysDict> orderTypes = sysDictService.selectByKey("ORDER_TYPE");
            List<SysDict> serviceTypes = sysDictService.selectByKey("SERVICE_TYPE");
            List<SysDict> merchantTypes = sysDictService.selectByKey("MERCHANT_TYPE_LIST");
            list = functionManagerDao.getVasRateList(vasInfo.getVasServiceNo());
            if (list != null && list.size() > 0) {
                for (VasRate item : list) {
                    String rateType = item.getRateType();
                    if ("1".equals(rateType)) {
                        item.setShowRate(item.getSingleNumAmount() + "(元)");
                    } else if ("2".equals(rateType)) {
                        item.setShowRate(item.getRate() + "(%)");
                    }
                    String orderType = item.getOrderType();
                    if (orderType != null) {
                        if (orderType.startsWith("-1")) {
                            item.setOrderTypeName("全部");
                        } else {

                            String[] orders = orderType.split(",");
                            StringBuffer orderTypeNameSB = new StringBuffer();
                            for (String order : orders) {
                                for (SysDict sd : orderTypes) {
                                    if (order.equals(sd.getSysValue())) {
                                        orderTypeNameSB.append(sd.getSysName() + ",");
                                        break;
                                    }
                                }
                            }
                            String resultName = orderTypeNameSB.toString();
                            if (resultName.endsWith(",")) {
                                resultName = resultName.substring(0, resultName.length() - 1);
                            }
                            item.setOrderTypeName(resultName);
                        }
                    }
                    String merchantType = item.getMerchantType();
                    if (merchantType != null) {
                        if (merchantType.startsWith("-1")) {
                            item.setMerchantTypeName("全部");
                        } else {

                            String[] types = merchantType.split(",");
                            StringBuffer typeNameSB = new StringBuffer();
                            for (String type : types) {
                                for (SysDict sd : merchantTypes) {
                                    if (type.equals(sd.getSysValue())) {
                                        typeNameSB.append(sd.getSysName() + ",");
                                        break;
                                    }
                                }
                            }
                            String resultName = typeNameSB.toString();
                            if (resultName.endsWith(",")) {
                                resultName = resultName.substring(0, resultName.length() - 1);
                            }
                            item.setMerchantTypeName(resultName);
                        }
                    }
                    String serviceType = item.getServiceType();
                    if (serviceType != null) {
                        if (serviceType.startsWith("-1")) {
                            item.setServiceTypeName("全部");
                        } else {

                            String[] services = serviceType.split(",");
                            StringBuffer serviceTypeNameSB = new StringBuffer();
                            for (String service : services) {
                                for (SysDict sd : serviceTypes) {
                                    if (service.equals(sd.getSysValue())) {
                                        serviceTypeNameSB.append(sd.getSysName() + ",");
                                        break;
                                    }
                                }
                            }
                            String resultName = serviceTypeNameSB.toString();
                            if (resultName.endsWith(",")) {
                                resultName = resultName.substring(0, resultName.length() - 1);
                            }
                            item.setServiceTypeName(resultName);
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public int saveFunctionTeam(FunctionTeam info, Map<String, Object> msg) {
        //校验
        System.out.println(info);
        if (!functionTeamCheck(info)) {
            msg.put("status", false);
            msg.put("msg", "新增的组织配置数据已存在!");
            return 0;
        }
        //数据转换
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        JSONObject jsonObject = new JSONObject();
        if ("066".equals(info.getFunctionNumber())) {
            jsonObject.put("activity_days", info.getActivity_days());
            jsonObject.put("activity_trans_amount", info.getActivity_trans_amount());
        } else if ("070".equals(info.getFunctionNumber()) || "071".equals(info.getFunctionNumber())) {
            jsonObject.put("day_number", info.getActivity_days());
            jsonObject.put("begin_time", info.getBeginTime());
            jsonObject.put("end_time", info.getEndTime());
        } else if ("072".equals(info.getFunctionNumber())) {
            jsonObject.put("tx_fee", info.getTxFee());
            info.setStatus(0);
        } else if ("073".equals(info.getFunctionNumber())) {
            int num = 0;
            VasServiceInfo vasInfo = vasInfoDao.getVasServiceInfo(info.getFunctionNumber(), "1");
            if (vasInfo != null) {
                VasRate vasRate = new VasRate();
                vasRate.setVasServiceNo(vasInfo.getVasServiceNo());
                vasRate.setOperator(principal.getUsername());
                vasRate.setMerchantType(info.getMerchantType());
                vasRate.setOrderType(info.getOrderType());
                vasRate.setServiceType(info.getServiceType());
                vasRate.setRateType(info.getRateType());
                vasRate.setRate(info.getRate());
                vasRate.setSingleNumAmount(info.getSingleNumAmount());
                vasRate.setTeamId(info.getTeamId());
                vasRate.setTeamEntryId(info.getTeamEntryId());
                num = vasInfoDao.insertVasRate(vasRate);

                VasShareRule vasShareRule = new VasShareRule();
                vasShareRule.setAgentNo("0");
                vasShareRule.setVasServiceNo(vasInfo.getVasServiceNo());
                vasShareRule.setOperator(principal.getUsername());
                vasShareRule.setTeamId(info.getTeamId());
                vasShareRule.setTeamEntryId(info.getTeamEntryId());
                vasShareRule.setRateType(info.getRateType());
                vasShareRule.setProfitSwitch(0);
                num += vasInfoDao.insertVasShareRule(vasShareRule);


            }

            if (num > 0) {
                msg.put("status", true);
                msg.put("msg", "新增成功!");
            } else {
                msg.put("status", false);
                msg.put("msg", "新增失败!");
            }
            return 0;
        } else if ("074".equals(info.getFunctionNumber())) {
            jsonObject.put("effective_time", info.getEffectiveTime());
            jsonObject.put("invalid_time", info.getInvalidTime());
            jsonObject.put("act_limit_day", info.getActLimitDay());
        }

        info.setBakJson(jsonObject.toString());
        int num = functionManagerDao.insertFunctionTeam(info);
        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "新增成功!");
        } else {
            msg.put("status", false);
            msg.put("msg", "新增失败!");
        }
        return 0;
    }

    @Override
    public int updateFunctionTeamVas(FunctionTeam info, Map<String, Object> msg) {
        int num = 0;
        //数据转换
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        if ("072".equals(info.getFunctionNumber())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tx_fee", info.getTxFee());
            info.setBakJson(jsonObject.toString());
            num = functionManagerDao.updateFunctionManageConfigBak(info);
        } else if ("073".equals(info.getFunctionNumber())) {
            VasServiceInfo vsi = vasInfoDao.getVasServiceInfo(info.getFunctionNumber(), "1");
            VasRate vasRate = new VasRate();
            vasRate.setId(info.getId());
            vasRate.setTeamId(info.getTeamId());
            vasRate.setTeamEntryId(info.getTeamEntryId());
            vasRate.setVasServiceNo(vsi.getVasServiceNo());
            vasRate.setOperator(principal.getUsername());
            vasRate.setMerchantType(info.getMerchantType());
            vasRate.setOrderType(info.getOrderType());
            vasRate.setServiceType(info.getServiceType());
            vasRate.setRate(info.getRate());
            vasRate.setSingleNumAmount(info.getSingleNumAmount());
            num = vasInfoDao.updateVasRate(vasRate);

            //boolean isOk = vasInfoService.compareVasRate(vasRate);
            //if (isOk) {
            //num = vasInfoDao.updateVasRate(vasRate);
            // } else {
            // msg.put("status", false);
            // msg.put("msg", "修改失败-需符合条件：服务费>= 默认分润+直属分润!");
            // return 0;
            //}
        } else if ("074".equals(info.getFunctionNumber())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("effective_time", info.getEffectiveTime());
            jsonObject.put("invalid_time", info.getInvalidTime());
            jsonObject.put("act_limit_day", info.getActLimitDay());
            info.setBakJson(jsonObject.toString());
            num = functionManagerDao.updateFunctionManageConfigBak(info);
        }
        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "修改成功!");
        } else {
            msg.put("status", false);
            msg.put("msg", "修改失败!");
        }
        return 0;
    }

    @Override
    public int saveFunctionConfigure(FunctionTeam info, Map<String, Object> msg) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        JSONObject jsonObject = new JSONObject();
        if ("069".equals(info.getFunctionNumber())) {
            jsonObject.put("activity_type_nos", info.getActivity_type_nos());
            jsonObject.put("cycle", info.getCycle());
            jsonObject.put("remarks", info.getRemarks());
        }
        info.setBakJson(jsonObject.toString());
        int num = 0;
        List<FunctionTeam> list = functionManagerDao.getFunctionTeamList(info.getFunctionNumber());
        if (list != null && list.size() > 0) {
            num = functionManagerDao.saveFunctionConfigure(info);
        } else {
            num = functionManagerDao.insertFunctionTeam(info);
        }

        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "新增成功!");
        } else {
            msg.put("status", false);
            msg.put("msg", "新增失败!");
        }
        return 0;
    }

    @Override
    public int deleteFunctionTeam(int id) {
        int num = 0;
        int num1 = functionManagerDao.deleteFunctionTeam(id);
        if (num1 > 0) {
            num = 1;
            //删除组织下的App显示配置
            List<AppShowConfig> oldList = functionManagerDao.getAppShowList(id);
            if (oldList != null && oldList.size() > 0) {
                functionManagerDao.deleteAppShowConfig(id);
                num = 2;
            }
        }
        return num;
    }

    @Override
    public int deleteSpecialFunctionTeam(String functionNumber, int id) {
        int num = 0;
        if ("073".equals(functionNumber)) {
            num = functionManagerDao.deleteVasRate(id);
        }
        return num;
    }

    @Override
    public List<AppShowConfig> getAppShowList(int fmcId) {
        List<AppShowConfig> list = functionManagerDao.getAppShowList(fmcId);
        //加载模板数据
        List<AppShowConfig> oldList = functionManagerDao.getAppShowList(0);

        if (list != null && list.size() > 0) {
            List<String> checkList = new ArrayList<String>();
            for (AppShowConfig item : list) {
                item.setImgUrl(CommonUtil.getImgUrlAgent(item.getRecommendIcon()));
                checkList.add(item.getFunctionCode());
            }
            //遍历模板数据，增加后追加模板
            if (oldList != null && oldList.size() > 0) {
                for (AppShowConfig oldItem : oldList) {
                    if (!checkList.contains(oldItem.getFunctionCode())) {
                        list.add(oldItem);
                    }
                }
            }
        } else {
            //如果查询不到配置数据，加载模板数据
            list = functionManagerDao.getAppShowList(0);
        }
        return list;
    }

    /**
     * 保存APP首页显示配置
     *
     * @param fmcId
     * @param addList
     * @return
     */
    @Override
    public int saveAppShowList(int fmcId, List<AppShowConfig> addList) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = principal.getUsername();
        int num = 0;
        if (addList != null && addList.size() > 0) {
            for (AppShowConfig item : addList) {
                item.setOperator(userName);
                item.setFmcId(fmcId);
                AppShowConfig oldInfo = functionManagerDao.checkEdit(item);
                if (oldInfo != null) {
                    num = num + functionManagerDao.updateAppShowList(item);
                } else {
                    num = num + functionManagerDao.insertAppShowList(item);
                }
            }
        }
        return num;
    }

    private boolean functionTeamCheck(FunctionTeam info) {
        if (info != null) {
            if ("073".equals(info.getFunctionNumber())) {
                VasServiceInfo vasServiceInfo = vasInfoDao.getVasServiceInfo(info.getFunctionNumber(), "1");
                if (vasServiceInfo != null) {
                    info.setVasServiceNo(vasServiceInfo.getVasServiceNo());
                    VasRate oldInfo = functionManagerDao.vasRateCheck(info);
                    if (oldInfo == null) {
                        return true;
                    }
                }
            } else {
                FunctionTeam oldInfo = functionManagerDao.functionTeamCheck(info);
                if (oldInfo == null) {
                    return true;
                }
            }

        }
        return false;
    }


    @Override
    public FunctionTeam getFunctionTeam(int id) {
        return functionManagerDao.getFunctionTeam(id);
    }

    @Override
    public int updateFunctionTeam(FunctionTeam info, Map<String, Object> msg) {
        //校验是否已存在
        FunctionTeam oldInfo = functionManagerDao.getFunctionTeamExit(info);

        if (oldInfo != null) {
            msg.put("status", false);
            msg.put("msg", "该组织已存在!");
            return 0;
        }

        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("day_number", info.getActivity_days());
        jsonObject.put("begin_time", info.getBeginTime());
        jsonObject.put("end_time", info.getEndTime());
        info.setBakJson(jsonObject.toString());
        int num = functionManagerDao.updateFunctionTeam(info);
        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "更新成功!");
        } else {
            msg.put("status", false);
            msg.put("msg", "更新失败!");
        }
        return num;
    }

}
