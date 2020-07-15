package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.OrderMainSum;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @Date 2017-12-1
 */
public interface OrderMainDao {

    @SelectProvider(type = SqlProvider.class, method = "selectOrderPage")
    @ResultType(OrderMain.class)
    List<OrderMain> selectOrderPage(@Param("baseInfo") OrderMain baseInfo, @Param("page") Page<OrderMain> page);

    @SelectProvider(type = SqlProvider.class, method = "selectOrderSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectOrderSum(@Param("baseInfo") OrderMain baseInfo);


    @Update("update order_main set proofreading_result = #{proofreadingResult}" +
            " where order_no = #{orderNo}")
    int updateProofreadingResult(OrderMain baseInfo);


    @Update("update order_main set loan_order_no = #{loanOrderNo},UPDATE_DATE=now(),status=#{status}" +
            " where order_no = #{orderNo}")
    int updateYsOrderResult(OrderMain baseInfo);

    @Select("select om.*,ui0.user_name," +
            "ui0.phone AS shareUserPhone," +
            "ui0.open_province,ui0.open_city,ui0.open_region,ui0.remark as userRemark," +
            "ui1.user_name AS oneUserName," +
            "ui2.user_name AS twoUserName," +
            "ui3.user_name AS thrUserName," +
            "ui4.user_name AS fouUserName," +
            "cs.bank_name,cs.bank_nick_name,cs.code as bankCode," +
            "ls.loan_alias," +
            " rtod.real_plat_profit,rtod.basic_bonus_amount,rtod.bonus_amount,rtbe.adjust_ratio,rtod.rate,rtod.rate_type,"+
            " rtbe.user_code as redUserCode,redUi.user_name as redUserName ,rtbe.territory_price,rtbe.territory_avg_price," +
            "rtbe.receive_time"+
            " from order_main om " +
           "  LEFT OUTER JOIN red_territory_order_detail rtod on rtod.order_no  = om.order_no"+
            "  LEFT OUTER JOIN red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id "+
            "  LEFT OUTER JOIN user_info redUi on redUi.user_code  = rtbe.user_code "+
            " LEFT OUTER JOIN user_info ui0 ON ui0.user_code = om.user_code" +
            " LEFT OUTER JOIN user_info ui1 ON ui1.user_code = om.one_user_code" +
            " LEFT OUTER JOIN user_info ui2 ON ui2.user_code = om.two_user_code" +
            " LEFT OUTER JOIN user_info ui3 ON ui3.user_code = om.thr_user_code" +
            " LEFT OUTER JOIN user_info ui4 ON ui4.user_code = om.fou_user_code" +
            " LEFT OUTER JOIN creditcard_source cs ON cs.id = om.bank_source_id" +
            " LEFT OUTER JOIN loan_source ls ON ls.id = om.loan_source_id" +
            " where om.order_no = #{orderNo}")
    @ResultType(OrderMain.class)
    OrderMain selectOrderDetail(@Param("orderNo") String orderNo);

    @Select("select om.*,ui0.user_name," +
            "ui0.phone AS shareUserPhone," +
            "ui0.open_province,ui0.open_city,ui0.open_region,ui0.remark as userRemark," +
            "ui1.user_name AS oneUserName," +
            "ui2.user_name AS twoUserName," +
            "ui3.user_name AS thrUserName," +
            "ui4.user_name AS fouUserName," +
            "cs.bank_name,cs.bank_nick_name,cs.code as bankCode," +
            "ls.loan_alias," +
            " rtod.real_plat_profit,rtod.basic_bonus_amount,rtod.bonus_amount,rtbe.adjust_ratio,rtod.rate,rtod.rate_type,"+
            " rtbe.user_code as redUserCode,redUi.user_name as redUserName ,rtbe.territory_price,rtbe.territory_avg_price," +
            "rtbe.receive_time,ro.id as redId,ro.push_amount as redAmount"+
            " from order_main om " +
            "  LEFT OUTER JOIN red_territory_order_detail rtod on rtod.order_no  = om.order_no"+
            "  LEFT OUTER JOIN red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id "+
            "  LEFT OUTER JOIN  red_orders ro on ro.order_no  = om.order_no "+
            "  LEFT OUTER JOIN user_info redUi on redUi.user_code  = rtbe.user_code "+
            " LEFT OUTER JOIN user_info ui0 ON ui0.user_code = om.user_code" +
            " LEFT OUTER JOIN user_info ui1 ON ui1.user_code = om.one_user_code" +
            " LEFT OUTER JOIN user_info ui2 ON ui2.user_code = om.two_user_code" +
            " LEFT OUTER JOIN user_info ui3 ON ui3.user_code = om.thr_user_code" +
            " LEFT OUTER JOIN user_info ui4 ON ui4.user_code = om.fou_user_code" +
            " LEFT OUTER JOIN creditcard_source cs ON cs.id = om.bank_source_id" +
            " LEFT OUTER JOIN loan_source ls ON ls.id = om.loan_source_id" +
            " where om.order_no = #{orderNo}")
    @ResultType(OrderMain.class)
    OrderMain selectRepayOrderDetail(@Param("orderNo") String orderNo);

    @Select("SELECT order_no,account_status" +
            " FROM" +
            " order_main" +
            " WHERE" +
//            " create_date <= #{createDateEnd} and create_date >= #{createDateStart} " +
//            " AND" +
            " order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " AND order_name LIKE #{orderName} AND order_type = #{orderType} and loan_source_id= #{loanSourceId}" +
            " and status=1 order by create_date desc LIMIT 1" )
    @ResultType(OrderMain.class)
    OrderMain selectYRDOrderExists(OrderMain orderMain);

    @Select("SELECT order_no,account_status,order_id_no,loan_name" +
            " FROM" +
            " order_main" +
            " WHERE" +
//            " create_date <= #{createDateEnd} and create_date >= #{createDateStart} " +
//            " AND " +
            " loan_source_id= #{loanSourceId}"+
            " and order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " AND order_name LIKE #{orderName} AND order_type = #{orderType}" +
//            " and status = '5'" +
            " order by create_date desc"
//            " limit 1"
    )
    @ResultType(OrderMain.class)
    List<OrderMain> selectYRDOrder(OrderMain orderMain);

    @Select("SELECT order_no,account_status,order_id_no,loan_name" +
            " FROM" +
            " order_main" +
            " WHERE" +
            " loan_source_id= #{loanSourceId}" +
//            " create_date <= #{createDateEnd} and create_date >= #{createDateStart} " +
//            " AND " +
            " and order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " AND order_name = #{orderName} AND order_type = #{orderType}" +
//            " and status = '5'" +
            " order by create_date desc"
//            " limit 1"
    )
    @ResultType(OrderMain.class)
    List<OrderMain> selectKKDOrder(OrderMain orderMain);

    @Select("SELECT order_no,account_status,order_id_no,loan_name" +
            " FROM" +
            " order_main" +
            " WHERE" +
            " loan_source_id= #{loanSourceId}" +
            " AND order_name = #{orderName} AND order_type = #{orderType}" +
            " and order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            "AND( loan_order_no IS NULL OR  loan_order_no !=#{loanOrderNo}) AND status != '5' " +
            " order by create_date desc"
    )
    @ResultType(OrderMain.class)
    List<OrderMain> selectQMGOrder(OrderMain orderMain);

    @Select("SELECT order_no,account_status,order_id_no,loan_name" +
            " FROM" +
            " order_main" +
            " WHERE" +
            " loan_source_id= #{loanSourceId}" +
            " AND order_name LIKE #{orderName} AND order_type = #{orderType}" +
            " and order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 3) = #{orderPhoneEnd}" +
            " order by create_date desc"
    )
    @ResultType(OrderMain.class)
    List<OrderMain> selectJfxlkOrder(OrderMain orderMain);

    @SelectProvider(type = SqlProvider.class, method = "selectJdbtOrder")
    @ResultType(OrderMain.class)
    List<OrderMain> selectJdbtOrder(@Param("baseInfo")OrderMain orderMain);

    @Select("SELECT COUNT(1) from order_main where loan_order_no=#{loanOrderNo}")
    int selectByLoanOrderNo(String loanOrderNo);

    @Select("SELECT order_no,account_status,order_id_no,loan_name" +
            " FROM" +
            " order_main" +
            " WHERE" +
            " loan_source_id= #{loanSourceId}" +
            " AND order_name = #{orderName} AND order_type = #{orderType}" +
            " order by create_date desc"
    )
    @ResultType(OrderMain.class)
    List<OrderMain> selectZAXYOrder(OrderMain orderMain);

    @Select("SELECT order_no,account_status,order_id_no,loan_name" +
            " FROM" +
            " order_main" +
            " WHERE" +
            " loan_source_id= #{loanSourceId}" +
            " and order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " AND order_name like #{orderNameStart} and length(order_name) = #{orderNameLength} and RIGHT(order_name, #{orderNameEndLength}) = #{orderNameEnd} " +
            "AND order_type = #{orderType}" +
            " order by create_date desc"
    )
    @ResultType(OrderMain.class)
    List<OrderMain> selectXEFOrder(OrderMain orderMain);

    @Select("SELECT order_no,account_status,order_id_no,loan_name" +
            " FROM" +
            " order_main" +
            " WHERE" +
            " loan_source_id= #{loanSourceId}" +
            " and order_phone = #{orderPhone}" +
            " AND order_name = #{orderName} AND order_type = #{orderType}" +
            " order by create_date desc"
    )
    @ResultType(OrderMain.class)
    List<OrderMain> selecQLGOrder(OrderMain orderMain);

    @Update("update order_main set status = #{status},batch_no=#{batchNo},loan_amount=#{loanAmount}" +
            ",update_by=#{updateBy}, update_date=#{updateDate} where order_no = #{orderNo}")
    int updateStatus(OrderMain orderMain);

    @Select("select * from order_main where batch_no=#{batchNo} and account_status<>'1' and status='5'")
    @ResultType(OrderMain.class)
    List<OrderMain> selectOrder(@Param("batchNo")String batchNo);

    @Update("update order_main set account_status = #{accountStatus}" +
            ",update_by=#{updateBy}, update_date=#{updateDate} " +
            " where order_no = #{orderNo} and status='5'")
    int updateAccountStatus(OrderMain order);

    @Select("select bank_bonus from creditcard_source where id=#{sourceId}")
    String getBankPushAmount(@Param("sourceId") Long sourceId);

    @Update("update order_main set profit_status = #{profitStatus} where batch_no = #{batchNo}")
    int updateProfitStatus(@Param("batchNo") String batchNo, @Param("profitStatus") String profitStatus);

    @Update("update order_main set status=9 " +
            " WHERE" +
//            " create_date <= #{createDateEnd} and create_date >= #{createDateStart}" +
//            " AND" +
            " order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " AND order_name LIKE #{orderName} AND order_type = #{orderType} and status = '1'" +
            " and loan_source_id= #{loanSourceId}")
    int updateExpired(OrderMain order);

    @Select("select order_no from order_main where order_type = #{orderType} and status = #{status}" +
            " and bank_name = #{bankName} and order_name = #{orderName} and RIGHT (order_id_no, 4) = #{orderIdNo}" +
            " order by create_date desc limit 1")
    @ResultType(OrderMain.class)
    OrderMain selectShBankOrderExistsSuccess(OrderMain order);

    @Select("select order_no from order_main where order_type = #{orderType} and status = #{status}" +
            " and bank_name = #{bankName} and order_name like #{orderName} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " order by create_date desc limit 1")
    @ResultType(OrderMain.class)
    OrderMain selectMsBankOrderExistsSuccess(OrderMain order);

    @Select("select order_no from order_main where order_type = #{orderType} and status = #{status}" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
            " and order_id_no like #{orderIdNo} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " order by create_date desc limit 1 ")
    @ResultType(OrderMain.class)
    OrderMain selectZxBankOrderExistsSuccess(OrderMain order);

    @Update("update order_main set status = #{status}, batch_no = #{batchNo} where order_type = #{orderType} and status <> 5" +
            " and bank_name = #{bankName} and order_name = #{orderName} and RIGHT (order_id_no, 4) = #{orderIdNo}" +
            " order by create_date desc limit 1 ")
    int updateShBankOrderSuccess(OrderMain order);

    @Update("update order_main set status = #{status} where order_type = #{orderType} and status <> '5'" +
            " and bank_name = #{bankName} and order_name = #{orderName} and RIGHT (order_id_no, 4) = #{orderIdNo}")
    int updateShBankOrderNotComplete(OrderMain order);

    @Update("update order_main set status = #{status}, batch_no = #{batchNo} where order_type = #{orderType} and status <> 5" +
            " and bank_name = #{bankName} and order_name like #{orderName} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " order by create_date desc limit 1 ")
    int updateMsBankOrderSuccess(OrderMain order);

    @Update("update order_main set status = #{status} where order_type = #{orderType} and status <> '5'" +
            " and bank_name = #{bankName} and order_name like #{orderName} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}")
    int updateMsBankOrderNotComplete(OrderMain order);

    @Update("update order_main set status = #{status}, batch_no = #{batchNo} where order_type = #{orderType} and status <> 5" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
            " and order_id_no like #{orderIdNo} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " order by create_date desc limit 1 ")
    int updateZxBankOrderSuccess(OrderMain order);

    @Update("update order_main set status = #{status} where order_type = #{orderType} and status <> '5'" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
            " and order_id_no like #{orderIdNo} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}")
    int updateZxBankOrderNotComplete(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where order_type = #{orderType} " +
            " and create_date <= #{createDate}" +
            " and bank_name = #{bankName}" +
            " and order_name like #{orderNameStart} and length(order_name) = #{orderNameLength} and RIGHT(order_name, #{orderNameEndLength}) = #{orderNameEnd}" +
            " and RIGHT (order_id_no, 4) = #{orderIdNo}" +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}"+
            " order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectShBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where order_type = #{orderType}" +
            " and bank_name = #{bankName} and order_name like #{orderName} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}"+
            " and create_date <= #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectMsBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where order_type = #{orderType} " +
            " and create_date <= #{createDate}" +
            " and bank_name = #{bankName}" +
            " and order_name like #{orderNameStart} and length(order_name) = #{orderNameLength} and RIGHT(order_name, #{orderNameEndLength}) = #{orderNameEnd}" +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}"+
            " order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectWzBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where order_type = #{orderType}" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
//            " and order_id_no like #{orderIdNo} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}"+
            " and create_date <= #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectZxBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where order_type = #{orderType}" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}"+
            " and RIGHT(LEFT(order_id_no,14),8)=#{orderIdNo}"+
            " and create_date <= #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectXyBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where order_type = #{orderType}" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
            " and RIGHT (order_id_no, 4) = #{orderIdNo}"+
            "and create_date <= #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectPaBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where order_type = #{orderType}" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
            " and RIGHT (order_id_no, 4) = #{orderIdNo}"+
            "and create_date <= #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectGfBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where status = '5'" +
            " and order_type = #{orderType}" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
            " and RIGHT (order_id_no, 4) = #{orderIdNo}"+
            " order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectPaBankBrushOrderExists(OrderMain order);

    @Update("update order_main set status = #{status}, batch_no = #{batchNo} where order_type = #{orderType} and status <> 5" +
            " and bank_name = #{bankName} and order_name like #{orderName}" +
            " and RIGHT (order_id_no, 4) = #{orderIdNo}"+
            " order by create_date desc limit 1 ")
    int updatePaBankOrderSuccess(OrderMain order);

    @Update("update order_main set status = #{status}, batch_no = #{batchNo} where order_type = #{orderType} and status <> 5" +
            " and bank_name = #{bankName} and order_name = #{orderName}" +
            " and RIGHT(LEFT(order_id_no,14),8)=#{orderIdNo}"+
            " order by create_date desc limit 1 ")
    int updateXyBankOrderSuccess(OrderMain order);

    @Update("<script>"
            + " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
            + "         close=\"\" separator=\";\"> "
            + " update order_main "
            + " set status=#{item.status},proofreading_method=#{item.proofreadingMethod},proofreading_result=#{item.proofreadingResult},"
            + " batch_no=#{item.batchNo}"
            + "   where order_no=#{item.orderNo}"
            + "     </foreach> "
            + " </script>")
    int updateOrderStatusBatch(@Param("list")List<OrderMain> list);

    @Update("<script>"
            + " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
            + "         close=\"\" separator=\";\"> "
            + " update order_main "
            + " set profit_status2=#{item.profitStatus2},proofreading_method=#{item.proofreadingMethod},proofreading_result=#{item.proofreadingResult},"
            + " batch_no2=#{item.batchNo2}"
            + "   where order_no=#{item.orderNo}"
            + "     </foreach> "
            + " </script>")
    int updateBrushStatusBatch(@Param("list")List<OrderMain> list);


    @Update("<script>"
            + " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
            + "         close=\"\" separator=\";\"> "
            + " update order_main "
            + " set status=#{item.status},batch_no=#{item.batchNo},loan_amount=#{item.loanAmount},"
            +"      update_by=#{item.updateBy}, update_date=#{item.updateDate}, loan_order_no=#{item.loanOrderNo}"
            + "   where order_no=#{item.orderNo}"
            + "     </foreach> "
            + " </script>")
    int updateLoanOrderBatch(@Param("list")List<OrderMain> list);

    @Update( "update order_main  set status=#{status},batch_no=#{batchNo},loan_amount=#{loanAmount},"+
             "update_by=#{updateBy}, update_date=#{updateDate}, loan_order_no=#{loanOrderNo}  where order_no=#{orderNo}")
    int updateLoanOrder(OrderMain orderMain);

    @Select("select count(1) from order_main where order_id_no = #{orderIdNo} and bank_name = #{bankName}" +
            " and status = '5'")
    @ResultType(Long.class)
    Long checkExistsSuccessOrder(OrderMain orderSuccess);

    @Select("select count(1) from order_main where order_id_no = #{orderIdNo} and loan_name = #{loanName}" +
            " and status = '5'")
    @ResultType(Long.class)
    Long checkExistsSuccessLoanOrder(OrderMain orderSuccess);

    @Select("select count(1) from order_main where order_id_no = #{orderIdNo} and bank_name = #{bankName}" +
            " and status = '5' and account_status2 = '1'")
    @ResultType(Long.class)
    Long checkExistsAccount2Order(OrderMain orderItem);

    @Select("select order_no,status,bank_name,order_id_no from order_main where order_type = #{orderType}" +
            " and bank_name = #{bankName}" +
            " and length(order_name) = #{orderNameLength} and RIGHT(order_name, #{orderNameEndLength}) = #{orderNameEnd}" +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}"+
            "  and create_date <= #{createDate}  order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectJtBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where user_code = #{userCode}" +
            " and order_type = #{orderType}" +
            " and bank_name = #{bankName} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " and order_name like #{orderName}"+
            " and create_date <= #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectZsBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where" +
            " order_type = #{orderType}" +
            " and bank_name = #{bankName} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " and order_name like #{orderName}"+
            " and create_date <= #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectJsBankOrderExists(OrderMain order);


    @Select("select order_no,status,bank_name,order_id_no from order_main where" +
            " order_type = #{orderType}" +
            " and bank_name = #{bankName} " +
            "  AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " and order_name like #{orderNameStart}"+
            " and  RIGHT (order_id_no, 4) = #{orderIdNo}"+
            " and create_date < #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectYsBankOrderExists(OrderMain order);

    @Select("select order_no,status,bank_name,order_id_no from order_main where" +
            " order_type = #{orderType}" +
            " and bank_name = #{bankName} " +
            " AND order_phone LIKE #{orderPhoneStart} AND RIGHT (order_phone, 4) = #{orderPhoneEnd}" +
            " and order_name = #{orderName}"+
            " and create_date <= #{createDate} order by create_date desc")
    @ResultType(OrderMain.class)
    List<OrderMain> selectHxBankOrderExists(OrderMain order);

    class SqlProvider{
        public String selectOrderPage(Map<String, Object> param){
            final OrderMain baseInfo = (OrderMain)param.get("baseInfo");
            SQL sql = new SQL(){{
                SELECT("om.order_no,om.org_id,om.org_name,om.order_type,om.user_code,om.pp_status,om.loan_order_no,ls.access_way");
                SELECT("om.status,om.create_date,om.total_bonus,om.share_user_code");
                SELECT("om.one_user_code,om.one_user_type,om.one_user_profit,om.org_profit");
                SELECT("om.plate_profit,om.account_status,om.pay_method,om.pay_date");
                SELECT("om.pay_channel,om.pay_order_no,om.pay_channel_no,om.price");
                SELECT("om.two_user_code,om.two_user_type,om.two_user_profit");
                SELECT("om.thr_user_code,om.thr_user_type,om.thr_user_profit");
                SELECT("om.fou_user_code,om.fou_user_type,om.fou_user_profit");
                SELECT("om.bank_source_id,om.bank_name,om.order_name,om.order_phone,om.order_id_no");
                SELECT("om.loan_source_id,om.loan_name,om.loan_amount,om.loan_push_pro");
                SELECT("om.receive_agent_id,om.receive_amount,om.repayment_agent_id,om.repayment_amount");
                SELECT("om.creditcard_bank_bonus,om.loan_bank_rate,om.loan_org_rate,om.loan_org_bonus,om.trans_rate");
                SELECT("om.refund_status,om.refund_date,om.refund_msg,om.complete_date");
                SELECT("om.repay_trans_channel,om.repay_trans_card_no,om.repay_trans_status,om.repay_transscucons");
                SELECT("om.repay_transfee,om.repay_transfee_add");
                SELECT("om.creditcard_bank_bonus2,om.total_bonus2");
                SELECT("om.one_user_profit2,om.two_user_profit2,om.thr_user_profit2,om.fou_user_profit2");
                SELECT("om.org_profit2,om.plate_profit2,om.account_status2,om.profit_status2");
                SELECT("om.pay_date2,om.batch_no,om.batch_no2,om.loan_type,om.profit_type ,om.proofreading_result,om.proofreading_method");
                SELECT("ui0.user_name,ui0.phone as shareUserPhone");
                SELECT("ui0.open_province,ui0.open_city,ui0.open_region,ui0.remark");
                SELECT("ui1.user_name as oneUserName,ui2.user_name as twoUserName");
                SELECT( "ui3.user_name as thrUserName,ui4.user_name as fouUserName");
                SELECT( "rtod.real_plat_profit ,rtod.rate_type,rtod.rate, rtod.basic_bonus_amount ,rtod.bonus_amount");
                SELECT( "rtbe.adjust_ratio,rtbe.user_code as redUserCode,redUi.user_name as redUserName");
                if("1".equals(baseInfo.getOrderType())){
                    SELECT( "uin.income_type as refundOrderNo");
                }
//                if("7".equals(baseInfo.getOrderType())){
//                    SELECT( "ro.id as recId,ro.push_amount as redAmount");
//                }
                SELECT( "cs.code as bankCode,cs.bank_name,cs.bank_nick_name,cs.batch_status");
                SELECT("ls.loan_alias");
            }};
            whereSql(sql, baseInfo);
            sql.ORDER_BY(" om.create_date desc");
            return sql.toString();
        }

        public String selectJdbtOrder(Map<String, Object> param){
            final OrderMain baseInfo = (OrderMain)param.get("baseInfo");
            SQL sql = new SQL(){{
                SELECT(" order_no,account_status,order_id_no,loan_name" );

            }};
            System.out.println(JSON.toJSON(baseInfo));
            sql.FROM("order_main ");
            sql.WHERE("loan_source_id = #{baseInfo.loanSourceId} and order_phone LIKE #{baseInfo.orderPhoneStart} AND RIGHT (order_phone, 4) = #{baseInfo.orderPhoneEnd}");
            sql.WHERE("length(order_name) = #{baseInfo.orderNameLength} and RIGHT(order_name, 1) = #{baseInfo.orderNameEnd}");
            if(StringUtils.isNotBlank(baseInfo.getOrderNameStart())){
                sql.WHERE("  order_name like #{baseInfo.orderNameStart}");
            }
                 sql.WHERE(" order_type = #{baseInfo.orderType} ");
                 sql.ORDER_BY("create_date");
                String s= sql.toString()+" desc";

             System.out.println(s);
            return s;
        }

        public String selectOrderSum(Map<String, Object> param){
            final OrderMain baseInfo = (OrderMain)param.get("baseInfo");
            SQL sql = new SQL(){{
                SELECT("sum(om.total_bonus) as totalBonusSum");
                SELECT("sum(om.plate_profit) as plateProfitSum");
                SELECT("sum(om.org_profit) as orgProfitSum");
                SELECT("sum(om.receive_amount) as receiveAmountSum");
                SELECT("sum(om.repayment_amount) as repayAmountSum");
                SELECT("sum(om.repayment_amount) as repaymentAmountSum");
                SELECT("sum(om.plate_profit2) as plateProfit2Sum");
                SELECT("sum(om.org_profit2) as orgProfit2Sum");
                SELECT("sum(om.creditcard_bank_bonus) as creditcardBankBonusSum");
                SELECT("sum(om.creditcard_bank_bonus2) as creditcardBankBonus2Sum");
                SELECT("sum(om.loan_amount) as loanAmountSum");
                SELECT("sum(om.price) as priceSum");
                SELECT("sum(rtod.real_plat_profit) as actualSum");
                SELECT("sum(rtod.bonus_amount) as territorySum");
                SELECT("sum(om.repay_transscucons) as repayTransscuconsSum");
//                if("7".equals(baseInfo.getOrderType())) {
//                    SELECT("sum(ro.push_amount) as redSum");
//                }
            }};
            whereSql(sql, baseInfo);
            return sql.toString();
        }

        public void whereSql(SQL sql, OrderMain baseInfo){
            sql.FROM("order_main om");
            sql.LEFT_OUTER_JOIN("  red_territory_order_detail rtod on rtod.order_no  = om.order_no");
//            if("7".equals(baseInfo.getOrderType())) {
//                sql.LEFT_OUTER_JOIN("  red_orders ro on ro.order_no  = om.order_no");
//            }
            sql.LEFT_OUTER_JOIN("  red_territory_bonus_everyday rtbe on rtbe.id  = rtod.bonus_everyday_id");
            sql.LEFT_OUTER_JOIN("  user_info redUi on redUi.user_code  = rtbe.user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui0 on ui0.user_code = om.user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui1 on ui1.user_code = om.one_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui2 on ui2.user_code = om.two_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui3 on ui3.user_code = om.thr_user_code");
            sql.LEFT_OUTER_JOIN("  user_info ui4 on ui4.user_code = om.fou_user_code");
            if("1".equals(baseInfo.getOrderType())){
                sql.LEFT_OUTER_JOIN("  user_income uin on uin.order_no = om.order_no");
            }
            sql.LEFT_OUTER_JOIN("  creditcard_source cs on cs.id = om.bank_source_id");
            sql.LEFT_OUTER_JOIN("  loan_source ls on ls.id = om.loan_source_id");
            if("1".equals(baseInfo.getOrderType()) && StringUtils.isNotBlank(baseInfo.getRefundOrderNo())){
                sql.WHERE("uin.income_type = #{baseInfo.refundOrderNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderType())){
            	baseInfo.setOrderTypeList(baseInfo.getOrderType().split(","));
            	if(baseInfo.getOrderTypeList().length > 1){
            		StringBuilder sb=new StringBuilder();
        			MessageFormat messageFormat = new MessageFormat("#'{'baseInfo.orderTypeList[{0}]}");
    				for (int i = 0; i < baseInfo.getOrderTypeList().length; i++) {
    					sb.append(messageFormat.format(new Integer[] { i }));
    					sb.append(",");
    				}
    				sb.setLength(sb.length() - 1);
                    sql.WHERE("om.order_type in (" + sb.toString() + ")");
            	} else {
            		sql.WHERE("om.order_type = #{baseInfo.orderType}");
            	}
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderNo())){
                sql.WHERE("om.order_no = #{baseInfo.orderNo}");
            }
            if(baseInfo.getAccessWay()!=0){
                sql.WHERE("ls.access_way = #{baseInfo.accessWay}");
            }
            if(StringUtils.isNotBlank(baseInfo.getStatus())){
                sql.WHERE("om.status = #{baseInfo.status}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                sql.WHERE("om.create_date >= #{baseInfo.createDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                sql.WHERE("om.create_date <= #{baseInfo.createDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayDateStart())){
                sql.WHERE("om.pay_date >= #{baseInfo.payDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayDateEnd())){
                sql.WHERE("om.pay_date <= #{baseInfo.payDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOneUserCode())){
                sql.WHERE("om.one_user_code = #{baseInfo.oneUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserCode())){
                sql.WHERE("om.share_user_code = #{baseInfo.shareUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getAccountStatus())){
                sql.WHERE("om.account_status = #{baseInfo.accountStatus}");
            }
            if(baseInfo.getOrgId() != null && -1 != baseInfo.getOrgId()){
                sql.WHERE("om.org_id = #{baseInfo.orgId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayOrderNo())){
                sql.WHERE("om.pay_order_no = #{baseInfo.payOrderNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderPhone())){
                sql.WHERE("om.order_phone = #{baseInfo.orderPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderIdNo())){
                sql.WHERE("om.order_id_no = #{baseInfo.orderIdNo}");
            }
            if(baseInfo.getBankSourceId() != null && -1 != baseInfo.getBankSourceId()){
                sql.WHERE("om.bank_source_id = #{baseInfo.bankSourceId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getReceiveAgentId())){
                sql.WHERE("om.receive_agent_id = #{baseInfo.receiveAgentId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRepaymentAgentId())){
                sql.WHERE("om.repayment_agent_id = #{baseInfo.repaymentAgentId}");
            }
            if(baseInfo.getLoanSourceId() != null && baseInfo.getLoanSourceId() != -1){
                sql.WHERE("om.loan_source_id = #{baseInfo.loanSourceId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserCode())){
                sql.WHERE("om.user_code = #{baseInfo.userCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderName())){
                sql.WHERE("om.order_name = #{baseInfo.orderName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRefundStatus())){
                sql.WHERE("om.refund_status = #{baseInfo.refundStatus}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayChannel())){
                sql.WHERE("om.pay_channel = #{baseInfo.payChannel}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayChannelNo())){
                sql.WHERE("om.pay_channel_no = #{baseInfo.payChannelNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRefundMsg())){
                sql.WHERE("om.refund_msg = #{baseInfo.refundMsg}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRefundDateStart())){
                sql.WHERE("om.refund_date >= #{baseInfo.refundDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRefundDateEnd())){
                sql.WHERE("om.refund_date <= #{baseInfo.refundDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserPhone())){
                sql.WHERE("ui0.phone = #{baseInfo.shareUserPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserName())){
                sql.WHERE("ui0.user_name = #{baseInfo.userName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOneUserCode())){
                sql.WHERE("om.one_user_code = #{baseInfo.oneUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOneUserName())){
                sql.WHERE("ui1.user_name = #{baseInfo.oneUserName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOneUserType())){
                sql.WHERE("om.one_user_type = #{baseInfo.oneUserType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getTwoUserCode())){
                sql.WHERE("om.two_user_code = #{baseInfo.twoUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getTwoUserName())){
                sql.WHERE("ui2.user_name = #{baseInfo.twoUserName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getTwoUserType())){
                sql.WHERE("om.two_user_type = #{baseInfo.twoUserType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getThrUserCode())){
                sql.WHERE("om.thr_user_code = #{baseInfo.thrUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getThrUserName())){
                sql.WHERE("ui3.user_name = #{baseInfo.thrUserName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getThrUserType())){
                sql.WHERE("om.thr_user_type = #{baseInfo.thrUserType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getFouUserCode())){
                sql.WHERE("om.fou_user_code = #{baseInfo.fouUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getFouUserName())){
                sql.WHERE("ui4.user_name = #{baseInfo.fouUserName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getFouUserType())){
                sql.WHERE("om.fou_user_type = #{baseInfo.fouUserType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRemark())){
                baseInfo.setRemark(baseInfo.getRemark() + "%");
                sql.WHERE("ui0.remark like #{baseInfo.remark}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenProvince()) && !"全部".equals(baseInfo.getOpenProvince())){
                sql.WHERE("ui0.open_province = #{baseInfo.openProvince}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenCity()) && !"全部".equals(baseInfo.getOpenCity())){
                sql.WHERE("ui0.open_city = #{baseInfo.openCity}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenRegion()) && !"全部".equals(baseInfo.getOpenRegion())){
                sql.WHERE("ui0.open_region = #{baseInfo.openRegion}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCompleteDateStart())){
                sql.WHERE("om.complete_date >= #{baseInfo.completeDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCompleteDateEnd())){
                sql.WHERE("om.complete_date <= #{baseInfo.completeDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRepayTransCardNo())){
                sql.WHERE("om.repay_trans_card_no = #{baseInfo.repayTransCardNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRepayTransStatus())){
                sql.WHERE("om.repay_trans_status = #{baseInfo.repayTransStatus}");
            }
            if(StringUtils.isNotBlank(baseInfo.getBankCode())){
                sql.WHERE("cs.code = #{baseInfo.bankCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getBankName())){
                baseInfo.setBankName(baseInfo.getBankName() + "%");
                sql.WHERE("cs.bank_name like #{baseInfo.bankName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getProofreadingMethod())){
                sql.WHERE("om.proofreading_method = #{baseInfo.proofreadingMethod}");
            }
            if(StringUtils.isNotBlank(baseInfo.getProofreadingResult())){
                sql.WHERE("om.proofreading_result = #{baseInfo.proofreadingResult}");
            }
            if(StringUtils.isNotBlank(baseInfo.getBankNickName())){
                baseInfo.setBankNickName(baseInfo.getBankNickName() + "%");
                sql.WHERE("cs.bank_nick_name like #{baseInfo.bankNickName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayDateStart())){
                sql.WHERE("om.pay_date >= #{baseInfo.payDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCompleteDateEnd())){
                sql.WHERE("om.pay_date <= #{baseInfo.payDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayDate2Start())){
                sql.WHERE("om.pay_date2 >= #{baseInfo.payDate2Start}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCompleteDateEnd())){
                sql.WHERE("om.pay_date2 <= #{baseInfo.payDate2End}");
            }
            if(StringUtils.isNotBlank(baseInfo.getAccountStatus2())){
                sql.WHERE("om.account_status2 = #{baseInfo.accountStatus2}");
            }
            if(StringUtils.isNotBlank(baseInfo.getProfitStatus2())){
                sql.WHERE("om.profit_status2 = #{baseInfo.profitStatus2}");
            }
            if(StringUtils.isNotBlank(baseInfo.getBatchNo())){
                sql.WHERE("om.batch_no = #{baseInfo.batchNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getProfitType())){
                sql.WHERE("om.profit_type = #{baseInfo.profitType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getLoanType())){
                sql.WHERE("om.loan_type = #{baseInfo.loanType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getBatchNo2())){
                sql.WHERE("om.batch_no2 = #{baseInfo.batchNo2}");
            }
            if(StringUtils.isNotBlank(baseInfo.getLoanOrderNo())){
                sql.WHERE("om.loan_order_no = #{baseInfo.loanOrderNo}");
            }
        }
    }

    /**新增订单*/
	@Insert("insert into order_main " +
			"(order_no,user_code,org_id,org_name,order_type,status,create_date,batch_no)" +
			" values" +
			" (#{orderNo},#{userCode},#{orgId},#{orgName},#{orderType},#{status},sysdate(),#{batchNo})")
	int saveOrderMain(OrderMain order);

}
