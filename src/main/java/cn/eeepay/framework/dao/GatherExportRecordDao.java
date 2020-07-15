package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

import cn.eeepay.framework.model.GatherExportRecord;

public interface GatherExportRecordDao {

	@Insert("insert into gather_export_record(operator,create_time,update_time) values ("
			+ "#{record.operator},now(),now())")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Integer.class)  
	int insert(@Param("record")GatherExportRecord record);

}
