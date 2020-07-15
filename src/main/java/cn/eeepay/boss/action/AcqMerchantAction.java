package cn.eeepay.boss.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;

@Controller
@RequestMapping(value = "/acqMerchantAction")
public class AcqMerchantAction {
    private static final Logger log = LoggerFactory.getLogger(AcqMerchantAction.class);

    //收单机构商户
    @Resource
    private AcqMerchantService acqMerchantService;

    //收单机构终端
    @Resource
    private AcqTerminalService acqTerminalService;

    //实体商户服务信息
    @Resource
    private MerchantServiceProService merchantServiceProService;

    @Resource
    private AgentInfoService agentInfoService;

    @Resource
    private MerchantInfoService merchantInfoService;

    @Resource
    private AcqServiceProService acqServiceProService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private TransRouteGroupService transRouteGroupService;
    @Resource
    private AcqInMerchantService acqInMerchantService;
    @Resource
    private BusinessProductDefineService businessProductDefineService;
    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;

    /**
     * 初始化和模糊查询分页
     *
     * @param page
     * @param param
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAllInfo")
    @ResponseBody
    public Object selectAllInfo(@ModelAttribute("page") Page<AcqMerchant> page, @RequestParam("info") String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            AcqMerchant amc = JSON.parseObject(param, AcqMerchant.class);
//			if(amc.getCheckDayAmount()==null){
//				amc.setCheckDayAmount("0");
//			}
            acqMerchantService.selectAllInfo(page, amc);
            jsonMap.put("bols", true);
            jsonMap.put("page", page);
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }

    /**
     * 详情查询
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam")
    @ResponseBody
    public Object selectByParam(@RequestParam("ids") String ids) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            int id = JSON.parseObject(ids, Integer.class);
            AcqMerchant acm = acqMerchantService.selectByPrimaryKey(id);
            AgentInfo ais = agentInfoService.selectByagentNo(acm.getAgentNo());
            if (ais != null) {
                acm.setAgentName(ais.getAgentName());
            }
            List<String> list = merchantServiceProService.selectServiceTypeByMerId(acm.getMerchantNo());
            jsonMap.put("result", acm);
            jsonMap.put("list", list);
            jsonMap.put("bols", true);
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }

    /**
     * 修改收单商户状态
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateAcqStatus")
    @ResponseBody
    @SystemLog(description = "收单商户状态开关", operCode = "orgMer.switch")
    public Object updateAcqStatus(@RequestParam("info") String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            JSONObject json = JSON.parseObject(param);
            if (json.getInteger("acqStatus") == 0) {
                String acqMerchantNo = json.getString("acqMerchantNo");
                List<TransRouteGroup> res = transRouteGroupService.selectGroupByAcqMerchantNo(acqMerchantNo);
                if (res != null && res.size() > 0) {
                    String acqMerchantName = res.get(0).getAcqMerchantName();
                    Object groupCode = res.get(0).getGroupCode();
                    jsonMap.put("msg", "收单机构商户" + acqMerchantName + "存在集群" + groupCode.toString() + "中,无法关闭");
                    jsonMap.put("bols", false);
                    return jsonMap;
                }
            }

            AcqMerchant acq = new AcqMerchant();
            acq.setId(json.getInteger("id"));
            acq.setAcqStatus(json.getInteger("acqStatus"));
            int i = acqMerchantService.updateStatusByid(acq);
            if (acq.getAcqStatus() == 1) {
                jsonMap.put("msg", "开通成功");
            } else {
                jsonMap.put("msg", "关闭成功");
            }
            if (i > 0) {
                jsonMap.put("bols", true);
            } else {
                jsonMap.put("bols", false);
                jsonMap.put("msg", "操作失败");
            }
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
            jsonMap.put("msg", "操作失败");
        }
        return jsonMap;
    }

    /**
     * 添加收单机构终端
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addTermianlInfo")
    @ResponseBody
    @SystemLog(description = "添加收单机构终端", operCode = "acqMer.addTermianl")
    public Object addTermianlInfo(@RequestParam("info") String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AcqTerminal atl = JSON.parseObject(param, AcqTerminal.class);
            if (atl.getAcqTerminalNo() == null ||"".equals(atl.getAcqTerminalNo())) {
                jsonMap.put("result", false);
                jsonMap.put("msg", "终端不能为空!!");
            }
            if (acqTerminalService.selectTerminalByTerNo(atl) != null) {
                jsonMap.put("result", false);
                jsonMap.put("msg", "终端已存在!!");
            } else {
                atl.setCreatePerson(principal.getId().toString());
                int i = acqTerminalService.insert(atl);
                if (i > 0) {
                    jsonMap.put("result", true);
                    jsonMap.put("msg", "新增终端成功！！");
                } else {
                    jsonMap.put("result", false);
                    jsonMap.put("msg", "新增终端失败！！");
                }
            }
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("result", false);
        }
        return jsonMap;
    }
//	selectByMerNo

    /**
     * 根据商户号查询一级代理商
     *
     * @param merNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectOneAgentNo")
    @ResponseBody
    public Object selectOneAgentNo(String type, @RequestParam("merNo") String merNo) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String merchantNo = JSON.parse(merNo).toString();
        if(StringUtils.isBlank(merchantNo)){
            jsonMap.put("bols", true);
            return jsonMap;
        }
        try {
            // 判断代理商是否已根据该普通商户申请特约商户
            AcqInMerchant acqInMerchant = acqInMerchantService.selectByMerchantNo(merchantNo);
            if("1".equals(type)){
                // 判断特约商户是否绑定普通商户
                AcqMerchant acqMerchant = acqMerchantService.selectInfoByMerNo(merchantNo);
                if(acqInMerchant == null){
                    jsonMap.put("bols", false);
                    jsonMap.put("msg", "代理商未根据该普通商户申请特约商户！");
                    return jsonMap;
                }
                if (acqMerchant != null && acqMerchant.getAcqStatus() != 0){ // 存在且未失效
                    jsonMap.put("bols", false);
                    jsonMap.put("msg", "该普通商户已经被特约商户绑定！");
                    return jsonMap;
                }
            }

            MerchantInfo mis = merchantInfoService.selectByMerNo(merchantNo);
            if (mis == null) {
                jsonMap.put("msg", "请输入合法的商户编号信息");
                jsonMap.put("bols", false);
            } else {
                AgentInfo ais = agentInfoService.selectByagentNo(mis.getOneAgentNo());
                if (ais == null) {
                    jsonMap.put("msg", "代理商不存在");
                    jsonMap.put("bols", false);
                } else {
                    if("1".equals(type)){
                        String changeMerBusinessInfo = acqInMerchant.getChangeMerBusinessInfo();
                        if(StringUtils.isNotBlank(changeMerBusinessInfo)){
                            JSONObject jsonObject = JSONObject.parseObject(changeMerBusinessInfo);
                            String info = jsonObject.get("changeBusinessInfo").toString();
                            List<Business> businesses = JSONArray.parseArray(info, Business.class);
                            for (Business business : businesses) {
                                business.setOldBpName(businessProductDefineService.selectBpNameByBpId(business.getOldBpId()));
                                business.setNewBpName(businessProductDefineService.selectBpNameByBpId(business.getNewBpId()));
                            }
                            jsonMap.put("businesses", businesses);
                        }
                        jsonMap.put("merchantName", mis.getMerchantName());
                    }
                    jsonMap.put("bols", true);
                    jsonMap.put("ais", ais);
                }
            }

        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
            jsonMap.put("msg", "运行时异常");
        }
        return jsonMap;
    }

    /**
     * 收单机构商户新增
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addAcqMerchant")
    @ResponseBody
    @SystemLog(description = "收单机构商户新增", operCode = "acqMer.insert")
    public Object addAcqMerchant(@RequestParam("info") String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            JSONObject json = JSON.parseObject(param);
            List<String> str = JSON.parseArray(json.getJSONArray("list").toJSONString(), String.class);
            AcqMerchant acq = json.getObject("info", AcqMerchant.class);
            acq.setCreatePerson(principal.getId().toString());
            AcqMerchant merCount = null;
            if (acq != null && StringUtils.isNotBlank(acq.getMerchantNo())) {
                MerchantInfo merchantInfo = merchantInfoService.selectMerExistByMerNo(acq.getMerchantNo());
                if (merchantInfo == null) {
                    jsonMap.put("msg", "普通商户不存在");
                    jsonMap.put("bols", false);
                    return jsonMap;
                }
                if (!merchantInfo.getOneAgentNo().equals(acq.getAgentNo())) {
                    jsonMap.put("msg", "该商户不属于该代理商");
                    jsonMap.put("bols", false);
                    return jsonMap;
                }
                merCount = acqMerchantService.selectInfoByMerNo(acq.getMerchantNo());
                if (merCount != null && merCount.getAcqStatus() != 0) {//存在且未失效
                    jsonMap.put("msg", "该普通商户已经有对应的收单商户了");
                    jsonMap.put("bols", false);
                    return jsonMap;
                }
            }

            String merchantNo = acq.getMerchantNo();
            if(StringUtils.isNotBlank(merchantNo)){
                AcqInMerchant acqInMerchant = acqInMerchantService.selectByMerchantNo(merchantNo);
                jsonMap = checkMerchantBusiness(merchantNo, acqInMerchant);
                if(!(Boolean) jsonMap.get("bols")){
                    return jsonMap;
                }
                acq.setAcqMerchantCode(acqInMerchant.getAcqIntoNo());//更新进件编号
            }


            if (acqMerchantService.selectInfoByAcqmerNo(acq) != null) {
                jsonMap.put("msg", "该收单商户已存在");
                jsonMap.put("bols", false);
            } else {
                int i = acqMerchantService.insert(acq, str, merCount);
                if (i > 0) {
                    jsonMap.put("bols", true);
                    jsonMap.put("msg", "新增成功");
                } else {
                    jsonMap.put("msg", "新增失败");
                    jsonMap.put("bols", false);
                }
            }
        } catch (Exception e) {
            log.error("新增收单商户失败", e);
            jsonMap.put("bols", false);
            String str = e.getMessage();
            if (e.getMessage() == null) {
                jsonMap.put("msg", "新增信息不完整");
                return jsonMap;
            }
            if (str.contains("\r\n") || str.contains("\n"))
                jsonMap.put("msg", "新增信息异常");
            else
                jsonMap.put("msg", str);
            log.error("新增异常！", e);
        }
        return jsonMap;

    }

    /**
     *  更换普通商户业务产品-业务产品合法性判断
     * @param merchantNo
     * @return
     */
    private Map<String, Object> checkMerchantBusiness(String merchantNo, AcqInMerchant acqInMerchant){
        log.info("更换普通商户业务产品-业务产品合法性判断");
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("bols", true);
        String changeMerBusinessInfo = acqInMerchant.getChangeMerBusinessInfo();
        if(StringUtils.isNotBlank(changeMerBusinessInfo)){
            JSONObject jsonObject = JSONObject.parseObject(changeMerBusinessInfo);
            String info = jsonObject.get("changeBusinessInfo").toString();
            List<Business> businesses = JSONArray.parseArray(info, Business.class);
            for (Business business : businesses) {
                business.setOldBpName(businessProductDefineService.selectBpNameByBpId(business.getOldBpId()));
                business.setNewBpName(businessProductDefineService.selectBpNameByBpId(business.getNewBpId()));
            }
            for (Business business : businesses) {
                String oldBpId = business.getOldBpId();
                String newBpId = business.getNewBpId();
                MerchantBusinessProduct oldMbp = merchantBusinessProductService.selectByMerchantNoAndBpId(merchantNo, oldBpId);
                if (oldMbp == null) {
                    jsonMap.put("bols", false);
                    jsonMap.put("msg", "原业务产品不存在,不能更换");
                    return jsonMap;
                }
                if(!"4".equals(oldMbp.getStatus())){
                    jsonMap.put("bols", false);
                    jsonMap.put("msg", "您的业务产品没有审核成功暂时,不能更换 原业务产品：" +  business.getOldBpName());
                    return jsonMap;
                }
                List<Map<String, Object>> merTerBpList = merchantBusinessProductService.selectTerBpInfo(merchantNo, oldBpId);
                if (merTerBpList == null || merTerBpList.size() < 1) {
                    jsonMap.put("bols", false);
                    jsonMap.put("msg", "该业务产品的机具已经解绑,不能更换 原业务产品：" + business.getOldBpName());
                    return jsonMap;
                }
                /*MerchantBusinessProduct newMbp = merchantBusinessProductService.selectByMerchantNoAndBpId(merchantNo, newBpId);
                if (newMbp != null) {
                    jsonMap.put("bols", false);
                    jsonMap.put("msg", "您已存在该业务产品,不能更换 新业务产品：" + business.getNewBpName());
                    return jsonMap;
                }*/

            }
        }
        return jsonMap;
    }



    /**
     * 收单机构商户修改
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateAcqMerchant")
    @ResponseBody
    @SystemLog(description = "收单机构商户修改", operCode = "acqMer.update")
    public Object updateAcqMerchant(@RequestParam("info") String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            JSONObject json = JSON.parseObject(param);
            AcqMerchant acq = json.getObject("info", AcqMerchant.class);
            acq.setId(json.getInteger("id"));
            if (acq != null && StringUtils.isNotBlank(acq.getMerchantNo())) {
                MerchantInfo merchantInfo = merchantInfoService.selectMerExistByMerNo(acq.getMerchantNo());
                if (merchantInfo == null) {
                    jsonMap.put("msg", "普通商户不存在");
                    jsonMap.put("bols", false);
                    return jsonMap;
                }
                if (!merchantInfo.getOneAgentNo().equals(acq.getAgentNo())) {
                    jsonMap.put("msg", "该商户不属于该代理商");
                    jsonMap.put("bols", false);
                    return jsonMap;
                }
                AcqMerchant merCount = acqMerchantService.selectInfoByMerNo(acq.getMerchantNo());
                if (merCount != null) {
                    if (!merCount.getId().equals(acq.getId())) {
                        jsonMap.put("msg", "该普通商户已经有对应的收单商户了");
                        jsonMap.put("bols", false);
                        return jsonMap;
                    }
                    AcqMerchant nums = acqMerchantService.selectInfoByAcqmerNo(acq);
                    if (nums != null && !nums.getId().equals(acq.getId())) {
                        jsonMap.put("msg", "该收单商户已存在");
                        jsonMap.put("bols", false);
                        return jsonMap;
                    }
                }
            }
            int i = acqMerchantService.updateByPrimaryKey(acq);
            if (i > 0) {
                jsonMap.put("bols", true);
            } else {
                jsonMap.put("bols", false);
            }
        } catch (Exception e) {
            log.error("报错!!!", e);
            System.out.println(e);
            jsonMap.put("msg", "异常");
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }

    /**
     * 查询实体商户对应的服务类型
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectServiceType")
    @ResponseBody
    public Object selectServiceType(@RequestParam("info") String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            JSONObject json = JSON.parseObject(param);
            String merId = json.getString("id");
            List<String> list = merchantServiceProService.selectServiceTypeByMerId(merId);
            jsonMap.put("bols", true);
            jsonMap.put("listStr", list);
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }


    @RequestMapping(value = "/importAcqMerchant")
    @ResponseBody
    @SystemLog(description = "收单机构批量导入", operCode = "acqMer.insertBatch")
    public Object importAcqMerchant(@RequestParam("file") MultipartFile file, @RequestParam("acqOrg") String acqOrg) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Workbook wb = null;
        try {
            int num = 0;
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!file.isEmpty()) {
                String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if (!format.equals(".xls") && !format.equals(".xlxs")) {
                    jsonMap.put("result", false);
                    jsonMap.put("msg", "文件格式错误");
                    return jsonMap;
                }
                wb = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = wb.getSheetAt(0);
                // 遍历所有单元格，读取单元格
                int row_num = sheet.getLastRowNum();
                SysDict sysDictParams = new SysDict();
                sysDictParams.setSysKey("ACQ_MERCHANT_TYPE");
                sysDictParams.setStatus(2);
                List<SysDict> listSysDict = sysDictService.selectDicByCondition(sysDictParams, new Page<SysDict>(0, 1000));


                Map<String, String> sourceMap=new HashMap<String, String>();
                List<SysDict> sourceList=sysDictService.selectByKey("acqMerSource");//收单商户来源途径
                if(sourceList!=null && sourceList.size()>0){
                    for(SysDict sysDict: sourceList){
                        sourceMap.put(sysDict.getSysName(),sysDict.getSysValue());
                    }
                }

                for (int i = 1; i <= row_num; i++) {
                    Row row = sheet.getRow(i);
                    String num1 = getCellValue(row.getCell(0));//收单机构商户编号
                    String num2 = getCellValue(row.getCell(1));//收单机构商户名称
                    String num3 = getCellValue(row.getCell(2));//MCC
                    String num4 = getCellValue(row.getCell(3));//收单服务ID
                    String num5 = getCellValue(row.getCell(4));//收单机构终端号
                    String num6 = "";
                    if (row.getCell(5) != null) {
                        num6 = getCellValue(row.getCell(5));//收单机构终端密钥（非必须）
                    }
                    String num7 = "";
                    if (row.getCell(6) != null) {
                        num7 = getCellValue(row.getCell(6));//普通商户编号（非必须）
                    }
                    
                    String num9 = "";
                    if (row.getCell(8) != null) {
                    	num9 = getCellValue(row.getCell(8)).split("\\.")[0];//收单机构对应收单商户进件编号（非必须）
                    }
                    String num10 = getCellValue(row.getCell(9)).trim();//是否特殊商户

                    String sourceStr1 = CellUtil.getCellValue(row.getCell(10));//来源途径
                    String source=null;
                    if(sourceStr1!=null){
                        String sourceStr=sourceStr1.split("\\.")[0];
                        if(!sourceMap.containsKey(sourceStr)){
                            jsonMap.put("msg", "第"+(i+1)+"行来源途径("+sourceStr+")填写错误!");
                            jsonMap.put("result", false);
                            return jsonMap;
                        }
                        source=sourceMap.get(sourceStr);
                    }

                    String[] str1 = num1.split("\\.");//收单机构商户编号
                    String[] str2 = num3.split("\\.");//MCC
                    String[] str3 = num4.split("\\.");//收单服务ID
                    String[] str4 = num5.split("\\.");//收单机构终端号

                    AcqMerchant am = new AcqMerchant();
                    am.setSource(source);
                    am.setAcqMerchantNo(str1[0]);
                    am.setAcqMerchantName(num2);
                    am.setMcc(str2[0]);
                    am.setAcqServiceId(Integer.valueOf(str3[0]));
                    am.setAcqOrgId(Integer.valueOf(acqOrg));
                    am.setAcqMerchantCode(num9);
                    am.setSpecial("是".equals(num10)?"1":"0");
                    num1 = getCellValue(row.getCell(7));
                    if (StringUtils.isNotBlank(num1)) {
                        for (SysDict d : listSysDict) {
                            if (d.getSysName().equals(num1) && StringUtils.isNotBlank(d.getSysValue())) {
                                am.setAcqMerchantType(Integer.parseInt(d.getSysValue()));
                                break;
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(num7)) {
                        String[] str6 = num7.split("\\.");//普通商户编号
                        am.setMerchantNo(str6[0]);
                        MerchantInfo mis = merchantInfoService.selectByMerNo(str6[0]);
                        if (mis == null) {
                            jsonMap.put("msg", "收单商户为：" + am.getAcqMerchantNo() + ",普通商户编号:" + str6[0] + "不存在");
                            jsonMap.put("result", false);
                            return jsonMap;
                        } else {
                            am.setAgentNo(mis.getAgentNo());
                        }
                    }
                    am.setCreatePerson(principal.getId().toString());

                    int nums = acqMerchantService.selectOrgMerExistByMerNo(str1[0]);
                    if (nums > 0) {//判断是否存在这个收单商户
                        jsonMap.put("msg", "收单商户为：" + am.getAcqMerchantNo() + "的收单商户已存在");
                        jsonMap.put("result", false);
                        return jsonMap;
                    }
                    AcqMerchant merCount = acqMerchantService.selectInfoByMerNo(str1[0]);
                    if (merCount != null) {
                        jsonMap.put("msg", "收单商户为：" + am.getAcqMerchantNo() + "的普通商户:" + str1[0] + "已存在在收单商户表中");
                        jsonMap.put("result", false);
                        return jsonMap;
                    }

                    AcqService as = acqServiceProService.findServiceId(am.getAcqServiceId());
                    if (as == null) {
                        jsonMap.put("msg", "收单商户为：" + am.getAcqMerchantNo() + "的服务ID:" + am.getAcqServiceId() + "不存在");
                        jsonMap.put("result", false);
                        return jsonMap;
                    }
                    AcqTerminal aa = new AcqTerminal();
                    if (StringUtils.isNotBlank(num6)) {
                        String[] str5 = num6.split("\\.");//收单机构终端密钥（非必须）
                        aa.setWorkKey(str5[0]);
                    }
                    aa.setAcqTerminalNo(str4[0]);
                    aa.setAcqMerchantNo(str1[0]);
                    aa.setAcqOrgId(acqOrg);
                    aa.setCreatePerson(principal.getId().toString());
                    num += acqMerchantService.insertInfo(am, aa);
                    if (num > 0) {
                        jsonMap.put("msg", "导入成功");
                        jsonMap.put("result", true);
                    } else {
                        jsonMap.put("result", false);
                        jsonMap.put("msg", "新增失败或者已存在，收单商户为：" + am.getAcqMerchantNo());
                    }
                }
            } else {
                jsonMap.put("result", false);
                jsonMap.put("msg", "文件格式错误");
            }
        } catch (Exception e) {
            log.error("报错", e);
            jsonMap.put("result", false);
            String str = e.getMessage();
            if (e.getMessage() == null) {
                jsonMap.put("msg", "导入信息有为空的数据，请检查必填项");
                return jsonMap;
            }
            if (str.contains("\r\n") || str.contains("\n"))
                jsonMap.put("msg", "导入信息异常，请检查");
            else
                jsonMap.put("msg", str);
        } finally {
            if (wb != null)
                wb.close();
        }
        return jsonMap;
    }

    public String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getStringCellValue();
        }
        return null;
    }

    /**
     * 下载模板
     */
    @RequestMapping("/downloadTemplate")
    public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response) {
        String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator + "acqMerchantTemplate.xls";
        log.info(filePath);
        ResponseUtil.download(response, filePath, "批量导入收单商户模板.xls");
        return null;
    }

    /**
     * 导出收单机构商户列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        AcqMerchant amc = JSON.parseObject(param, AcqMerchant.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<AcqMerchant> list=acqMerchantService.importDetailSelect(amc);
        try {
            acqMerchantService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出收单机构商户列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出收单机构商户列表异常!");
        }
        return msg;
    }

    /**
     * 收单机构商户批量关闭模板下载
     */
    @RequestMapping("/acqMerBatchColseTemplate")
    public String acqMerBatchColseTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"acqMerBatchColseTemplate.xlsx";
        log.info(filePath);
        ResponseUtil.download(response, filePath,"收单机构商户批量关闭模板.xlsx");
        return null;
    }

    /**
     * 批量关闭导入
     */
    @RequestMapping(value="/acqMerBatchColseimport")
    @ResponseBody
    @SystemLog(description = "收单机构批量关闭导入", operCode = "acqMerchantAction.acqMerBatchColseimport")
    public Map<String, Object> acqMerBatchColseimport(@RequestParam("file") MultipartFile file){
        Map<String, Object> msg = new HashMap<>();
        try {
            if (!file.isEmpty()) {
                String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if(!format.equals(".xls") && !format.equals(".xlsx")){
                    msg.put("status", false);
                    msg.put("msg", "导入文件格式错误!");
                    return msg;
                }
            }
            msg = acqMerchantService.acqMerBatchColseimport(file);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "导入失败!");
            log.error("导入失败!",e);
        }
        return msg;
    }



}
