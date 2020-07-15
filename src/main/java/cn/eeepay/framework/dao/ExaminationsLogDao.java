package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ExaminationsLog;
import java.util.List;

import org.apache.ibatis.annotations.*;

public interface ExaminationsLogDao {

    @Insert("insert into examinations_log(item_no,bp_id,open_status,"
    		+ "examination_opinions,operator,create_time,examine_type) "
    		+ "values(#{record.itemNo},#{record.bpId},#{record.openStatus},#{record.examinationOpinions},#{record.operator},#{record.createTime},#{record.examineType})")

    @Options(useGeneratedKeys = true, keyProperty = "record.id")
    int insert(@Param("record")ExaminationsLog record);

    @Select("select exl.*,bsu.real_name,uis.user_name as realName from examinations_log exl "
    		+ "LEFT JOIN boss_shiro_user bsu on exl.operator=bsu.id "
    		+ "LEFT JOIN user_info uis on exl.operator=uis.mobilephone "
    		+ "where exl.item_no=#{merchantId}")
    @ResultType(ExaminationsLog.class)
    List<ExaminationsLog> selectByMerchantId(@Param("merchantId")String merchantId);
    
    @Select("select * from examinations_log "
    		+ "where item_no=#{itemNo} and operator='-1' and examine_type=1 order by create_time desc limit 1")
    @ResultType(ExaminationsLog.class)
    ExaminationsLog selectByitemNo(@Param("itemNo")String itemNo);


    @Insert(
            " INSERT INTO examinations_log_ext" +
                    " (examinations_log_id,merchant_type,create_time) " +
                    " VALUES " +
                    " ( #{info.id},#{info.merchantType},NOW() ) "
    )
    int insertLogExt(@Param("info")ExaminationsLog record);
}