package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.LoanSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @desc superbank.loan_source
 * 贷款机构表
 * @author tans
 * @date 2017-12-5
 */
public interface LoanSourceDao {

    @Select("select * from loan_source order by convert(company_name using gbk)")
    @ResultType(LoanSource.class)
    List<LoanSource> getLoanList();

    @Select("select * from loan_source where id = #{id}")
    @ResultType(LoanSource.class)
    LoanSource selectDetail(@Param("id") Long id);
}
