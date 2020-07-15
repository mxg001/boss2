package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditcardSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CreditcardSourceDao {

    @SelectProvider(type = SqlProvider.class, method = "selectList")
    @ResultType(CreditcardSource.class)
    List<CreditcardSource> selectList(@Param("baseInfo") CreditcardSource baseInfo, @Param("page") Page<CreditcardSource> page);

    @Select("select cs.* from creditcard_source cs")
    @ResultType(CreditcardSource.class)
    List<CreditcardSource> getAllBanks();
    
    @Select("select cs.* from creditcard_source cs where cs.id = #{id}")
    @ResultType(CreditcardSource.class)
    CreditcardSource selectDetail(@Param("id") Long id);

    @Select("select cs.* from creditcard_source cs where cs.bank_nick_name = #{bankNickName}")
    @ResultType(CreditcardSource.class)
    CreditcardSource selectByNick(@Param("bankNickName") String bankNickName);

    @Insert("insert into creditcard_source(code,bank_name,show_order,status,show_logo,h5_link," +
            "send_link,bank_bonus,bank_nick_name,source,slogan,card_bonus,first_brush_bonus," +
            "approve_status,auto_share_status,share_remark,special_position,special_image," +
            "create_time,update_by,rule_code,batch_status,card_active_status,access_methods," +
            "quick_card_status,special_label,apply_card_guide_img,send_api_link)" +
            " values " +
            "(#{code},#{bankName},#{showOrder},#{status},#{showLogo},#{h5Link}," +
            "#{sendLink},#{bankBonus},#{bankNickName},#{source},#{slogan},#{cardBonus},#{firstBrushBonus}," +
            "#{approveStatus},#{autoShareStatus},#{shareRemark},#{specialPosition},#{specialImage}," +
            "#{createTime},#{updateBy},#{ruleCode},#{batchStatus},#{cardActiveStatus},#{accessMethods},"+
            "#{quickCardStatus},#{specialLabel},#{applyCardGuideImg},#{sendApiLink})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(CreditcardSource info);

    @Update("update creditcard_source set bank_name=#{bankName},code=#{code},bank_nick_name=#{bankNickName}," +
            "show_logo=#{showLogo},source=#{source},show_order=#{showOrder},slogan=#{slogan}," +
            "card_bonus=#{cardBonus},first_brush_bonus=#{firstBrushBonus},approve_status=#{approveStatus}," +
            "auto_share_status=#{autoShareStatus},h5_link=#{h5Link},send_link=#{sendLink}," +
            "share_remark=#{shareRemark},special_position=#{specialPosition},special_image=#{specialImage}," +
            "update_time=#{updateTime},update_by=#{updateBy},rule_code=#{ruleCode},batch_status=#{batchStatus}," +
            "card_active_status=#{cardActiveStatus},access_methods=#{accessMethods},quick_card_status=#{quickCardStatus}," +
            "special_label=#{specialLabel}," +
            "apply_card_guide_img=#{applyCardGuideImg},send_api_link=#{sendApiLink}" +
            " where id = #{id}")
    int updateBank(CreditcardSource info);

    @Update("update creditcard_source set status = #{status} where id = #{id}")
    int updateBankStatus(CreditcardSource info);

    @Select("select count(1) from creditcard_source where code = #{code}")
    int selectCodeExists(@Param("code") String code);

    @Select("select count(1) from creditcard_source where code = #{code} and id <> #{id}")
    int selectCodeIdExists(@Param("code") String code, @Param("id") Long id);

    @Select("select count(1) from creditcard_source where show_order = #{showOrder}")
    int selectOrderExists(@Param("showOrder")String showOrder);

    @Select("select count(1) from creditcard_source where show_order = #{showOrder} and id <> #{id}")
    int selectOrderIdExists(@Param("showOrder") String showOrder, @Param("id") Long id);

    @Select("select count(1) from creditcard_source where special_position = #{specialPosition}")
    int selectSpecialExists(@Param("specialPosition")String specialPosition);

    @Select("select count(1) from creditcard_source where special_position = #{specialPosition} and id <> #{id}")
    int selectSpecialIdExists(@Param("specialPosition")String specialPosition, @Param("id") Long id);

    @Select("select count(1) from creditcard_source where rule_code = #{ruleCode}")
    int selectRuleCodeExists(@Param("ruleCode")String ruleCode);

    @Select("select count(1) from creditcard_source where rule_code = #{ruleCode} and id <> #{id}")
    int selectRuleCodeIdExists(@Param("ruleCode")String ruleCode, @Param("id")Long id);

    @Select("select count(1) from creditcard_source where bank_nick_name = #{bankNickName}")
    int selectNickExists(@Param("bankNickName")String bankNickName);

    @Select("select count(1) from creditcard_source where bank_nick_name = #{bankNickName} and id <> #{id}")
    int selectNickIdExists(@Param("bankNickName")String bankNickName, @Param("id")Long id);

    class SqlProvider{
        public String selectList(Map<String, Object> param){
            CreditcardSource baseInfo = (CreditcardSource) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("cs.*");
            sql.FROM("creditcard_source cs");
            if(baseInfo != null){
                if(StringUtils.isNotBlank(baseInfo.getCode())){
                    sql.WHERE("cs.code = #{baseInfo.code}");
                }
                if(StringUtils.isNotBlank(baseInfo.getBankName())){
                    baseInfo.setBankName(baseInfo.getBankName() + "%");
                    sql.WHERE("cs.bank_name like #{baseInfo.bankName}");
                }
                if(StringUtils.isNotBlank(baseInfo.getBankNickName())){
                    baseInfo.setBankNickName(baseInfo.getBankNickName() + "%");
                    sql.WHERE("cs.bank_nick_name like #{baseInfo.bankNickName}");
                }
                if(StringUtils.isNotBlank(baseInfo.getStatus())){
                    sql.WHERE("cs.status = #{baseInfo.status}");
                }
                if(baseInfo.getApproveStatus() != null){
                    sql.WHERE("cs.approve_status = #{baseInfo.approveStatus}");
                }
                if(baseInfo.getAutoShareStatus() != null){
                    sql.WHERE("cs.auto_share_status = #{baseInfo.autoShareStatus}");
                }
                if(baseInfo.getBatchStatus()!=null){
                    sql.WHERE("cs.batch_status = #{baseInfo.batchStatus}");
                }
                if(StringUtils.isNotBlank(baseInfo.getCardActiveStatus())){
                    sql.WHERE("cs.card_active_status = #{baseInfo.cardActiveStatus}");
                }
                if(StringUtils.isNotBlank(baseInfo.getAccessMethods())){
                    sql.WHERE("cs.access_methods = #{baseInfo.accessMethods}");
                }
            }
            sql.ORDER_BY("cs.show_order,cs.create_time desc");
            return sql.toString();
        }
    }
}
