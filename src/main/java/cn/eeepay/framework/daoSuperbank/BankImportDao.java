package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BankImport;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface BankImportDao {

    @SelectProvider(type = SqlProvider.class, method = "selectList")
    @ResultType(BankImport.class)
    List<BankImport> selectList(@Param("baseInfo") BankImport baseInfo, @Param("page")Page<BankImport> page);

    @Insert("insert into bank_import(bank_code,bank_name,bank_nick_name,file_url,import_time," +
            "import_by,status,update_by,create_time,batch_no,bonus_type)" +
            "values(#{bankCode},#{bankName},#{bankNickName},#{fileUrl},#{importTime}," +
            "#{importBy},#{status},#{updateBy},#{createTime},#{batchNo},#{bonusType})")
    int insert(BankImport info);

    @Select("select * from bank_import where id = #{id}")
    BankImport selectDetail(Integer id);

    @Update("update bank_import set status = #{status} where id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    @Update("update bank_import set status = #{status},total_num=#{totalNum}," +
            "success_num=#{successNum},fail_num=#{failNum} where id = #{id}")
    int updateInfo(BankImport bankImport);

    class SqlProvider{
        public String selectList(Map<String, Object> param){
            BankImport baseInfo = (BankImport) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("bi.id,bi.batch_no,bi.batch_no,bi.file_url,bi.import_time,bi.import_by,bi.status");
            sql.SELECT("bi.total_num,bi.success_num,bi.fail_num,bi.bonus_type");
            sql.SELECT("cs.code as bankCode,cs.bank_name,cs.bank_nick_name");
            sql.FROM("bank_import bi");
            sql.LEFT_OUTER_JOIN("creditcard_source cs on cs.id = bi.bank_code");
            if(baseInfo != null){
                if(StringUtils.isNotBlank(baseInfo.getBankCode())){
                    sql.WHERE("bi.bank_code = #{baseInfo.bankCode}");
                }
                if(StringUtils.isNotBlank(baseInfo.getBankName())){
                    baseInfo.setBankName(baseInfo.getBankName() + "%");
                    sql.WHERE("bi.bank_name like #{baseInfo.bankName}");
                }
                if(StringUtils.isNotBlank(baseInfo.getBankNickName())){
                    baseInfo.setBankNickName(baseInfo.getBankNickName() + "%");
                    sql.WHERE("bi.bank_nick_name like #{baseInfo.bankNickName}");
                }
                if(StringUtils.isNotBlank(baseInfo.getImportTimeStart())){
                    sql.WHERE("bi.import_time >= #{baseInfo.importTimeStart}");
                }
                if(StringUtils.isNotBlank(baseInfo.getImportTimeEnd())){
                    sql.WHERE("bi.import_time <= #{baseInfo.importTimeEnd}");
                }
                if(StringUtils.isNotBlank(baseInfo.getStatus())){
                    sql.WHERE("bi.status = #{baseInfo.status}");
                }
                if(StringUtils.isNotBlank(baseInfo.getBonusType())){
                    sql.WHERE("bi.bonus_type = #{baseInfo.bonusType}");
                }
            }
            sql.ORDER_BY("bi.create_time desc");
            return sql.toString();
        }
    }
}
