package cn.eeepay.boss.action.cjt;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.cjt.CjtMerchantInfo;
import cn.eeepay.framework.service.MerchantBusinessProductService;
import cn.eeepay.framework.service.MerchantCardInfoService;
import cn.eeepay.framework.service.TerminalInfoService;
import cn.eeepay.framework.service.cjt.CjtMerchantInfoService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @date 2019-06-14
 */
@RestController
@RequestMapping("/cjtMerchantInfo")
public class CjtMerchantInfoAction {

    private static final Logger log = LoggerFactory.getLogger(CjtMerchantInfoAction.class);

    @Resource
    private CjtMerchantInfoService cjtMerchantInfoService;

    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;

    @Resource
    private TerminalInfoService terminalInfoService;

    @Resource
    private MerchantCardInfoService merchantCardInfoService;

    /**
     * 条件查询新版超级推商户
     * @param baseInfo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestBody CjtMerchantInfo baseInfo,
                             @RequestParam(defaultValue = "1") int pageNo,
                             @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            //如果商户号和手机号都为空，那就要限制查询时间,查询时间要在30天内
            if(StringUtils.isEmpty(baseInfo.getMerchantNo()) && StringUtils.isEmpty(baseInfo.getMobilephone())
                    &&StringUtils.isEmpty(baseInfo.getOneMerchantNo())&&StringUtils.isEmpty(baseInfo.getTwoMerchantNo())
            ) {
                String createTimeStart = baseInfo.getCreateTimeStart();
                String createTimeEnd = baseInfo.getCreateTimeEnd();
                DateUtil.checkOrderDate(createTimeStart, createTimeEnd, 30);
            }

            Page<CjtMerchantInfo> page = new Page<>(pageNo, pageSize);
            cjtMerchantInfoService.selectPage(page, baseInfo);
            Map<String, Object> totalMap = cjtMerchantInfoService.selectTotal(baseInfo);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("page", page);
            dataMap.put("totalMap", totalMap);

            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(dataMap);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询新版超级推商户异常", e);
        }
        return result;
    }

    /**
     * 查询超级推商户详情
     * @param merchantNo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/detail")
    public Result detail(String merchantNo){
        Result result = new Result();
        try {
            CjtMerchantInfo baseInfo = cjtMerchantInfoService.selectDetail(merchantNo);
            //业务产品
            List<MerchantBusinessProduct> bpList = merchantBusinessProductService.getByMer(merchantNo);
            //机具
            Page<TerminalInfo> tiPage = new Page<>();
            terminalInfoService.getPageByMerchant(merchantNo,tiPage);
            //结算信息
            MerchantCardInfo merchantCardInfo = merchantCardInfoService.selectByMertId(merchantNo);
            if(merchantCardInfo!=null){
                merchantCardInfo.setAccountArea(merchantCardInfo.getAccountProvince()
                        +"-"+ merchantCardInfo.getAccountCity()
                        +"-"+ merchantCardInfo.getAccountDistrict()
                );
            }
            Map<String, Object> map = new HashMap<>();
            map.put("baseInfo", baseInfo);
            map.put("bpList", bpList);
            map.put("tiPage", tiPage);
            map.put("merchantCardInfo", merchantCardInfo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询超级推商户详情异常", e);
        }
        return result;
    }

    /**
     * 查询超级推账户详情，提现详情
     * @param merchantNo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/accountDetail")
    public Result accountDetail(String merchantNo){
        Result result = new Result();
        try {
            //基本信息
            CjtMerchantInfo baseInfo = cjtMerchantInfoService.selectAccountDetail(merchantNo);

            //结算信息
            MerchantCardInfo merchantCardInfo = merchantCardInfoService.selectByMertId(merchantNo);
            if(merchantCardInfo!=null){
                merchantCardInfo.setAccountArea(merchantCardInfo.getAccountProvince()
                        +"-"+ merchantCardInfo.getAccountCity()
                        +"-"+ merchantCardInfo.getAccountDistrict()
                );
            }

            Map<String, Object> map = new HashMap<>();
            map.put("baseInfo", baseInfo);
            map.put("merchantCardInfo", merchantCardInfo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询超级推账户详情异常", e);
        }
        return result;
    }

    /**
     * 超级推商户提现流水
     * @param merchantNo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/getCashPage")
    public Result getCashPage(@RequestParam("merchantNo") String merchantNo,
                              @RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            SettleOrderInfo settleOrderInfo = new SettleOrderInfo();
            settleOrderInfo.setSubType(6);//超级推商户
            settleOrderInfo.setSettleUserNo(merchantNo);
            settleOrderInfo.setSettleUserType("M");
            settleOrderInfo.setSettleType("2");
            Page<SettleOrderInfo> page = new Page<>(pageNo, pageSize);
            cjtMerchantInfoService.selectCashPage(settleOrderInfo,page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e) {
            log.error("超级推商户提现流水,商户号：[{}]",merchantNo);
            log.error("超级推商户提现流水查询异常,",e);
        }
        return result;
    }


}
