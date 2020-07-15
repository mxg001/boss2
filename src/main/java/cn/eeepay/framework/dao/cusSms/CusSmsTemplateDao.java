package cn.eeepay.framework.dao.cusSms;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.cusSms.CusSmsTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CusSmsTemplateDao {

    @SelectProvider(type= CusSmsTemplateDao.SqlProvider.class,method="selectAllList")
    @ResultType(CusSmsTemplate.class)
    List<CusSmsTemplate> selectAllList(@Param("info") CusSmsTemplate info, @Param("page") Page<CusSmsTemplate> page);

    @Insert(
            " INSERT INTO cus_sms_template " +
                    " (department,type,template,status,create_operator,last_update_operator,create_time,last_update_time) " +
                    "  VALUES " +
                    " (#{info.department},#{info.type},#{info.template},'1',#{info.createOperator},#{info.lastUpdateOperator},now(),now())"
    )
    int addSmsTemplate(@Param("info")CusSmsTemplate info);

    @Update(
            " update cus_sms_template set " +
                    " department=#{info.department},type=#{info.type},template=#{info.template},last_update_operator=#{info.lastUpdateOperator} " +
                    " where id=#{info.id}"
    )
    int editSmsTemplate(@Param("info")CusSmsTemplate info);

    @Delete(
            " delete from cus_sms_template where id=#{id}"
    )
    int deleteSmsTemplate(@Param("id")int id);

    @Select(
            " select * from cus_sms_template where id=#{id} "
    )
    CusSmsTemplate getSmsTemplateInfo(@Param("id")int id);

    //获取商户少量信息
    @Select(
            " select mobilephone,merchant_no from merchant_info where mobilephone=#{str}"
    )
    List<MerchantInfo> getMerchantInfoByPhone(@Param("str")String str);

    @Select(
            " select mobilephone,merchant_no from merchant_info where merchant_no=#{str}"
    )
    MerchantInfo getMerchantInfoByNo(@Param("str")String str);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final CusSmsTemplate info = (CusSmsTemplate) param.get("info");
            return new SQL(){{
                SELECT(" * ");
                FROM("cus_sms_template");
                if(info.getId()!=null){
                    WHERE(" id = #{info.id} ");
                }
                if(StringUtils.isNotBlank(info.getDepartment())){
                    WHERE("department = #{info.department} ");
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
