package cn.eeepay.framework.dao.cusSms;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cusSms.CusSmsRecord;
import cn.eeepay.framework.model.importLog.ImportLogEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CusSmsRecordDao {

    @SelectProvider(type= CusSmsRecordDao.SqlProvider.class,method="selectAllList")
    @ResultType(CusSmsRecord.class)
    List<CusSmsRecord> selectAllList( @Param("info") CusSmsRecord info, @Param("page")  Page<CusSmsRecord> page);

    @SelectProvider(type= CusSmsRecordDao.SqlProvider.class,method="selectAllList")
    @ResultType(CusSmsRecord.class)
    List<CusSmsRecord> importList( @Param("info")CusSmsRecord info);

    @Select("select * from cus_sms_record where id=#{id}")
    CusSmsRecord getCusSmsRecordInfo(@Param("id")int id);

    @Insert(
            "<script> INSERT INTO cus_sms_record " +
                    " (template_id,mobile_phone,merchant_no,type,content,code, " +
                    "  status,operator,create_time) " +
                    " VALUES "+
                    "<foreach  collection=\"list\" item=\"info\" index=\"index\" separator=\",\" > " +
                    "(#{info.templateId},#{info.mobilePhone},#{info.merchantNo},#{info.type},#{info.content},#{info.code}," +
                    " '1',#{info.operator},now()) "+
                    " </foreach ></script>"
    )
    int insertCusSmsRecordList(@Param("list")List<CusSmsRecord> list);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final CusSmsRecord info = (CusSmsRecord) param.get("info");
            return new SQL(){{
                SELECT(" * ");
                FROM("cus_sms_record");
                if(info.getTemplateId()!=null){
                    WHERE(" template_id = #{info.templateId} ");
                }
                if(StringUtils.isNotBlank(info.getMobilePhone())){
                    WHERE("mobile_phone = #{info.mobilePhone} ");
                }
                if(StringUtils.isNotBlank(info.getMerchantNo())){
                    WHERE("merchant_no = #{info.merchantNo} ");
                }
                if(StringUtils.isNotBlank(info.getCode())){
                    WHERE("code = #{info.code} ");
                }
                if(StringUtils.isNotBlank(info.getType())){
                    WHERE("type = #{info.type} ");
                }
                if(info.getCreateTimeBegin() != null){
                    WHERE("create_time >= #{info.createTimeBegin}");
                }
                if(info.getCreateTimeEnd() != null){
                    WHERE("create_time <= #{info.createTimeEnd}");
                }
                ORDER_BY("create_time DESC ");
            }}.toString();
        }
    }
}
