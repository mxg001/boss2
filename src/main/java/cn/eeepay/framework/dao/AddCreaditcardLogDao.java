package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.AddCreaditcardLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author tans
 * @date 2019/9/20 14:34
 */
public interface AddCreaditcardLogDao {

    @Select("select * from add_creaditcard_log where merchant_no = #{merchantNo}")
    @ResultType(AddCreaditcardLog.class)
    List<AddCreaditcardLog> selectMerchantCreditcard(@Param("merchantNo") String merchantNo);

    @Select("select * from add_creaditcard_log where id = #{id}")
    @ResultType(AddCreaditcardLog.class)
    AddCreaditcardLog selectMerchantCreditcardDetail(@Param("id") Long id);

    @Select("select * from add_creaditcard_log where merchant_no = #{merchantNo} order by create_time asc limit 1")
    @ResultType(AddCreaditcardLog.class)
    AddCreaditcardLog selectFirstMerchantCreditcard(@Param("merchantNo") String merchantNo);
}
