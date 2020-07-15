package cn.eeepay.boss.action.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.risk.BlackDataInfo;
import cn.eeepay.framework.model.risk.DealRecord;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.risk.BlackDataService;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 黑名单资料处理
 * @author MXG
 * create 2018/12/21
 */
@RequestMapping("blackData")
@Controller
public class BlackDataAction {

    @Resource
    private BlackDataService blackDataService;
    @Resource
    private MerchantInfoService merchantInfoService;

    private Logger log = LoggerFactory.getLogger(BlackDataAction.class);

    /**
     * 黑名单资料列表（分页条件查询）
     * @param blackDataInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectByParam")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestBody BlackDataInfo blackDataInfo,
                                             @RequestParam(defaultValue = "1") int pageNo,
                                             @RequestParam(defaultValue = "10") int pageSize){
        Map<String, Object> result = new HashMap<>();
        try {
            Page<BlackDataInfo> page = new Page<>(pageNo, pageSize);
            blackDataService.selectByParamWithPage(page, blackDataInfo);
            List<BlackDataInfo> list = page.getResult();
            for (BlackDataInfo dataInfo : list) {
                if(dataInfo!=null){
                    //敏感信息屏蔽
                    dataInfo.setMerchantPhone(StringUtil.sensitiveInformationHandle(dataInfo.getMerchantPhone(),0));
                    String mbpId = blackDataService.queryMbpId(dataInfo.getMerchantNo());
                    dataInfo.setMerBusinessProId(mbpId);
                }
            }
            result.put("status", true);
            result.put("page", page);
        } catch (Exception e) {
            result.put("status", false);
            result.put("msg", "查询失败");
            log.error("黑名单资料列表查询失败", e);
        }
        return result;
    }

    /**
     * 获取资料详情
     * @param id
     * @return
     */
    @RequestMapping("/getDetail")
    @ResponseBody
    public Map<String, Object> getDetail(@RequestParam String id){
        Map<String, Object> result = blackDataService.getDetail(id,1);
        return result;
    }

    /**
     * 获取资料详情
     * @param id
     * @return
     */
    @RequestMapping("/getDetailTwo")
    @ResponseBody
    public Map<String, Object> getDetailTwo(@RequestParam String id){
        Map<String, Object> result = blackDataService.getDetail(id,0);
        return result;
    }

    /**
     * 敏感信息获取
     */
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    public Map<String,Object> getDataProcessing(@RequestParam String id) throws Exception{
        Map<String, Object> result = blackDataService.getDetail(id,3);
        return result;
    }


    /**
     * 添加备注
     * @param blackDataInfo
     * @return
     */
    @RequestMapping("/addRemark")
    @ResponseBody
    public Map<String, Object> addRemark(@RequestBody BlackDataInfo blackDataInfo){
        Map<String, Object> result = new HashMap<>();
        try {
            blackDataService.addRemark(blackDataInfo.getId(), blackDataInfo.getRiskLastRemark());
            result.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取组织下拉列表
     * @return
     */
    @RequestMapping("/selectTeamList")
    @ResponseBody
    public Map<String, Object> selectTeamList(){
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map> teamList = blackDataService.selectTeamList();
            result.put("status", true);
            result.put("teamList", teamList);
        } catch (Exception e) {
            result.put("status", false);
            result.put("msg", "组织下拉列表获取失败");
            log.error("组织下拉列表获取失败", e);
        }
        return result;
    }

    /**
     * 导出
     * @param param
     * @param response
     * @param request
     */
    @RequestMapping("/export")
    @ResponseBody
    public void export(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        BlackDataInfo info = JSONObject.parseObject(param, BlackDataInfo.class);
        try {
            blackDataService.export(info, request, response);
        } catch (Exception e) {
            log.error("黑名单资料处理列表导出失败", e);
        }
    }


    /**
     * 提交风控处理
     * @param record
     * @return
     */
    @RequestMapping("/deal")
    @ResponseBody
    public Map<String, Object> deal(@RequestBody DealRecord record, @RequestParam int type){
        Map<String, Object> result = new HashMap<>();
        if(StringUtils.isBlank(record.getOrigOrderNo()) || StringUtils.isBlank(record.getMerchantNo())){
            result.put("status", false);
            result.put("msg", "必要参数不能为空");
            return result;
        }
        MerchantInfo merchant = merchantInfoService.selectByMerNo(record.getMerchantNo());
        String riskStatus = merchant.getRiskStatus();
        if("102".equals(record.getRiskDealTemplateNo()) && ("2".equals(riskStatus) || "3".equals(riskStatus))){
            result.put("status", false);
            result.put("msg", "请先解除户限制");
            return result;
        }

        try {
            blackDataService.deal(record, type, merchant.getMobilephone(), merchant.getMerchantNo());
            result.put("status", true);
        } catch (Exception e) {
            result.put("status", false);
            log.error("风控处理异常", e);
        }
        result.put("status", true);
        return result;
    }

    @RequestMapping("/queryMbpId")
    @ResponseBody
    public Map<String, Object> queryMbpId(String merchantNo){
        Map<String, Object> result = new HashMap<>();
        String mbpId = blackDataService.queryMbpId(merchantNo);
        result.put("mbpId", mbpId);
        return result;
    }

}
