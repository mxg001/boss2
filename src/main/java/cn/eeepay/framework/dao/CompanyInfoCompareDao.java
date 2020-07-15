package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.CompanyInfoCompare;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CompanyInfoCompareDao {


    @Select(
            " select * from company_info where company_name = #{companyName} order by create_time desc limit 1 "
    )
    List<CompanyInfoCompare> getCompanyInfoCompareOne(@Param("companyName") String companyName);
}
