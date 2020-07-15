package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.PosCnaps;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PosCnapsDao {

    @Select("select cnaps_no ,bank_name  from pos_cnaps where bank_name like #{bankName} and bank_name like #{cityName}")
    @ResultType(PosCnaps.class)
    List<PosCnaps> query(@Param("bankName")String bankName , @Param("cityName")String cityName);
}
