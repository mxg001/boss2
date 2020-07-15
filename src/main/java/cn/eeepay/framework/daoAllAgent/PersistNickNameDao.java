package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.PersistNickName;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface PersistNickNameDao {

    @SelectProvider(type=PersistNickNameDao.SqlProvider.class,method="selectPersistNickNameList")
    @ResultType(PersistNickName.class)
    List<PersistNickName> selectPersistNickNameList(@Param("info") PersistNickName info,@Param("page") Page<PersistNickName> page);

    @Insert("insert into pa_persist_nickname (sensitive_no,status,create_time,key_word,key_type) VALUES (nextval('persist_nickname_num'),'1',NOW(),#{info.keyWord},'1')")
    int insertPersistNickName(@Param("info") PersistNickName info);

    @Select("SELECT * FROM pa_persist_nickname where id=#{id}")
    @ResultType(PersistNickName.class)
    PersistNickName selectPersistNickNameById(@Param("id") Integer id);

    @Update("UPDATE pa_persist_nickname SET key_word=#{info.keyWord},modify_time=now() WHERE id=#{info.id}")
    int updatePersistNickName(@Param("info") PersistNickName info);

    @Delete("DELETE FROM pa_persist_nickname where id=#{id}")
    int deletePersistNickName(@Param("id") Integer id);

    @Select("SELECT * FROM pa_persist_nickname where key_word=#{keyWord}")
    @ResultType(PersistNickName.class)
    List<PersistNickName> selectPersistNickNameByKey(@Param("keyWord") String keyWord);

    class SqlProvider{

        public String selectPersistNickNameList(final Map<String, Object> param) {
            final PersistNickName info = (PersistNickName) param.get("info");
            SQL sql = new SQL(){{
                SELECT("pn.*");
                FROM("pa_persist_nickname pn");
            }};
            where(sql, info);
            return sql.toString();
        }

        public void where(SQL sql, PersistNickName info) {
            if(StringUtils.isNotBlank(info.getKeyWord())){
                sql.WHERE("pn.key_word like concat('%',#{info.keyWord},'%') ");
            }
            if(StringUtils.isNotBlank(info.getCreateTimeBegin())){
                sql.WHERE("pn.create_time >= #{info.createTimeBegin}");
            }
            if(StringUtils.isNotBlank(info.getCreateTimeEnd())){
                sql.WHERE("pn.create_time <= #{info.createTimeEnd}");
            }
        }
    }
}
