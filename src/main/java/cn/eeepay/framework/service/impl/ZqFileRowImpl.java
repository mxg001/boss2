package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantBusinessProductDao;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.model.ZqServiceInfo;
import cn.eeepay.framework.service.TranslateRow;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ExcelErrorMsgBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ZqFileRowImpl implements TranslateRow<ZqServiceInfo> {

    private ThreadLocal<String> channelThreadLocal = new ThreadLocal<>();//批次号

//    @Resource
//    private MerchantInfoDao merchantInfoDao;
//
//    @Resource
//    private BusinessProductDefineDao businessProductDefineDao;

    @Resource
    private MerchantBusinessProductDao merchantBusinessProductDao;

    /**
     * 校验商户号是否存在
     * 校验业务产品是否存在
     * 校验商户是否代理该业务产品
     * @param row    Excel 行
     * @param index  第几行
     * @param errors 错误信息集合
     * @return
     */
    @Override
    public ZqServiceInfo translate(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int merchantNoLine = 0;//商户号
        int bpIdLine = 1;//业务产品ID

        String merchantNo = String.valueOf(row.getCell(merchantNoLine));
        String bpIdStr = String.valueOf(row.getCell(bpIdLine));

        if(StringUtils.isBlank(merchantNo) || "null".equals(merchantNo)){
            errors.add(new ExcelErrorMsgBean(index, merchantNoLine+1, "商户号不能为空"));
            return null;
        }
        if(StringUtils.isBlank(bpIdStr) || "null".equals(bpIdStr)){
            errors.add(new ExcelErrorMsgBean(index, bpIdLine+1, "业务产品ID不能为空"));
            return null;
        }
        if(merchantNo.endsWith(".0")){
            merchantNo = merchantNo.substring(0, merchantNo.length() - 2);
        }
        if(bpIdStr.endsWith(".0")){
            bpIdStr = bpIdStr.substring(0, bpIdStr.length() - 2);
        }

        //检查商户号是否存在
//        MerchantInfo merchantInfo = merchantInfoDao.selectByMerNo(merchantNo);
//        if(merchantInfo == null){
//            errors.add(new ExcelErrorMsgBean(index, merchantNoLine+1, "商户号[" + merchantNo + "]不存在"));
//            return null;
//        }
//
//        //检查业务产品ID是否存在
//        BusinessProductDefine bpd = businessProductDefineDao.selectBybpId(bpIdStr);
//        if(bpd == null){
//            errors.add(new ExcelErrorMsgBean(index, bpIdLine+1, "业务产品[" + bpIdStr + "]不存在"));
//            return null;
//        }

        //检查商户是否代理该业务产品
        MerchantBusinessProduct mbp = merchantBusinessProductDao.selectMerBusPro(merchantNo, bpIdStr);
        if(mbp == null){
            errors.add(new ExcelErrorMsgBean(index, bpIdLine+1, "商户[" + merchantNo   +"]-业务产品[" + bpIdStr +"]不存在"));
            return null;
        }

        //如果符合规则
        ZqServiceInfo zqServiceInfo = new ZqServiceInfo();
        zqServiceInfo.setMerchantNo(merchantNo);
        zqServiceInfo.setBpId(Long.valueOf(bpIdStr));
        zqServiceInfo.setChannelCode(channelThreadLocal.get());
        zqServiceInfo.setOperator(CommonUtil.getLoginUserName());
        return zqServiceInfo;
    }

    public void setChannel(String channelCode) {
        channelThreadLocal.set(channelCode);
    }

}
