package test;

import cn.eeepay.framework.util.DateUtil;
import org.junit.Test;

import java.util.Date;

/**
 * @author tans
 * @date 2019/1/9 10:22
 */
public class TansTest {

    @Test
    public void createTestOrder(){
        String orderSqlDemo = "INSERT INTO `collective_trans_order` (`order_no`,`mobile_no`, `merchant_no`, `trans_amount`," +
                " `pay_method`, `trans_status`, `trans_type`, `trans_time`, `create_time`, `account_no`," +
                " `card_type`, `holidays_mark`, `business_product_id`, `service_id`, `acq_org_id`, `acq_name`," +
                " `acq_enname`, `acq_service_id`, `merchant_fee`, `merchant_rate`, `merchant_rate_type`, " +
                "`acq_merchant_no`, `acq_merchant_fee`, `acq_merchant_rate`, `acq_rate_type`, `agent_node`," +
                " `settlement_method`, `settle_status`, `settle_msg`, `settle_type`, `settle_order`, `syn_status`," +
                " `freeze_status`, `device_sn`, `hardware_product`, `account`, `trans_msg`, `res_msg`, `profits_1`," +
                " `profits_2`, `profits_3`, `profits_4`, `profits_5`, `profits_6`, `profits_7`, `profits_8`," +
                " `profits_9`, `profits_10`, `profits_11`, `profits_12`, `profits_13`, `profits_14`, `profits_15`, " +
                "`profits_16`, `profits_17`, `profits_18`, `profits_19`, `profits_20`, `gather_code`," +
                " `last_update_time`, `remark`, `deduction_fee`, `actual_fee`, `activity_source`," +
                " `order_type`, `unionpay_mer_no`, `mbp_id`, `origin_fee`, `quick_rate`, `quick_fee`," +
                " `merchant_price`, `deduction_mer_fee`, `isZJX`, `n_prm`, `zx_rate`) " +
                "VALUES (nextval('test_order_no'), '17777777650', '258121000031254', '12.00', '1'," +
                " 'SUCCESS', 'PURCHASE', now(), now(), " +
                "'12345678901234567', '2', '1', '404', '1206', '41', NULL, 'YS_ZQ', '198', " +
                "'1.00', '0.55%', NULL, '826440379918056', '0.01', '0.10%', NULL, '0-22302-', '0', '3'," +
                " 'yfb:交易已冻结，不能出款', NULL, NULL, '1', '0', 'D327968807900005', NULL, '1', '记账成功'," +
                " '交易成功', '0.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL," +
                " NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), NULL, '0.00', '0.07'," +
                " NULL, '0', '826100054518062', '12519750', NULL, '0.00', '0.00', '0.00', '0.00', '0', '0.00', '0');\n";
//        String nowDateStr = DateUtil.getLongCurrentDate();
//        String.format(orderSqlDemo, nowDateStr, nowDateStr, nowDateStr);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 999; i++){
            stringBuilder.append(orderSqlDemo);
        }
        System.out.println(stringBuilder);
    }
}
