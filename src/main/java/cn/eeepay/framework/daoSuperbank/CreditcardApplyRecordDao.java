package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.CreditcardApplyRecord;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;


public interface CreditcardApplyRecordDao {

    @InsertProvider(type = SqlProvider.class, method = "insertBatch")
    int insertBatch(@Param("list")List<CreditcardApplyRecord> list);

    @Select("select count(1) from creditcard_apply_record where bank_code=#{bankCode}" +
            " and id_card_no=#{idCardNo} and user_name=#{userName} and new_account_status = #{newAccountStatus}")
    int checkExists(CreditcardApplyRecord record);

    @Select("select car.*,cs.rule_code,cs.bank_name from creditcard_apply_record car left join creditcard_source cs on cs.id = car.bank_code" +
            " where batch_no = #{batchNo}")
    List<CreditcardApplyRecord> selectListByBatchNo(String batchNo);

    @Update("update creditcard_apply_record set message = #{message},order_no = #{orderNo} where id = #{id}")
    int updateMessage(@Param("id") Long id, @Param("message") String message, @Param("orderNo") String orderNo);

    class SqlProvider{
        public String insertBatch(Map<String, Object> param){
            List<CreditcardApplyRecord> list = (List<CreditcardApplyRecord>) param.get("list");
            if(list == null || list.size() < 1){
                return "";
            }
            StringBuilder values = new StringBuilder();
            MessageFormat message = new MessageFormat("(#'{'list[{0}].bankCode},#'{'list[{0}].applyDate}" +
                    ", #'{'list[{0}].issueDate}, #'{'list[{0}].firstBrushDate},#'{'list[{0}].userName}"
                    + ", #'{'list[{0}].idCardNo}, #'{'list[{0}].newAccountStatus},#'{'list[{0}].cardNo}"
                    + ",#'{'list[{0}].createTime},#'{'list[{0}].operator},#'{'list[{0}].checkStatus},#'{'list[{0}].mobilephone}" +
                    ",#'{'list[{0}].batchNo},#'{'list[{0}].userCode}),");
            for(int i = 0; i < list.size(); i++){
                values.append(message.format(new Integer[]{i}));
            }
            final String valuesSql  = values.substring(1, values.length() - 2);//去掉最前面那个括号,和最后面的,)
            SQL sql = new SQL();
            sql.INSERT_INTO("creditcard_apply_record");
            sql.VALUES("bank_code,apply_date,issue_date,first_brush_date,user_name,id_card_no" +
                            ",new_account_status,card_no,create_time,operator,check_status,mobilephone,batch_no,user_code" ,valuesSql);
            return sql.toString();
        }
    }
}
