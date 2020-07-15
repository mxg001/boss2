package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.PyIdentification;

public interface PyIdentificationDao {

	@Select("select * from py_identification where ident_name=#{name} and id_card=#{idCard} and account_no=#{accountNo}")
	@ResultType(PyIdentification.class)
	PyIdentification queryByCheckInfo(@Param("name")String name,@Param("idCard")String idCard,@Param("accountNo")String accountNo);
	
	int insert(PyIdentification pyIdentification);
}
