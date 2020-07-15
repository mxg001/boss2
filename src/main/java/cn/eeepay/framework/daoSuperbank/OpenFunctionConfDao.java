package cn.eeepay.framework.daoSuperbank;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.OpenFunctionConf;

public interface OpenFunctionConfDao {
	
	 @Select("select id ,function_name,function_url from open_function_conf order by id ")
	 @ResultType(OpenFunctionConf.class)
     List<OpenFunctionConf> getList();
	 
	 @Select("select id ,function_name,function_url from open_function_conf where id = #{id}")
	 @ResultType(OpenFunctionConf.class)
     OpenFunctionConf getOpenFunctionConfById(@Param("id")Long id);
}
