package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateHelpCenter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/11/011.
 * @author liuks
 * 帮助中心Dao
 */
public interface ExchangeActivateHelpCenterDao {

    @SelectProvider(type=ExchangeActivateHelpCenterDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateHelpCenter.class)
    List<ExchangeActivateHelpCenter> selectAllList(@Param("help") ExchangeActivateHelpCenter help, @Param("page") Page<ExchangeActivateHelpCenter> page);

    @Insert(
            "INSERT INTO yfb_help_center " +
                    " (title,link,category,model,sort,status,create_time,remark)" +
                    " VALUES " +
                    " (#{help.title},#{help.link},#{help.category},#{help.model},#{help.sort}," +
                    "  '1',NOW(),#{help.remark})"
    )
    int addHelpCenter(@Param("help") ExchangeActivateHelpCenter help);

    @Select(
            "select * from yfb_help_center where id=#{id}"
    )
    ExchangeActivateHelpCenter getHelpCenter(@Param("id") long id);

    @Update(
            "update yfb_help_center " +
                    " set title=#{help.title},link=#{help.link},category=#{help.category}," +
                    " model=#{help.model},sort=#{help.sort},remark=#{help.remark} " +
                    " where id=#{help.id}"
    )
    int updateHelpCenter(@Param("help") ExchangeActivateHelpCenter help);

    @Delete(
            "delete from yfb_help_center where id=#{id}"
    )
    int deleteHelpCenter(@Param("id") long id);

    @Update(
            "update yfb_help_center set status=#{state} where id=#{id}"
    )
    int updateHelpCenterState(@Param("id") long id, @Param("state") String state);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateHelpCenter help = (ExchangeActivateHelpCenter) param.get("help");
            String str=new SQL(){{
                SELECT("help.*");
                FROM("yfb_help_center help");
                if(StringUtils.isNotBlank(help.getTitle())){
                    WHERE("help.title like concat(#{help.title},'%') ");
                }
                if(StringUtils.isNotBlank(help.getCategory())){
                    WHERE("help.category = #{help.category} ");
                }
                if(StringUtils.isNotBlank(help.getModel())){
                    WHERE("help.model = #{help.model} ");
                }
                ORDER_BY("help.sort DESC");
                ORDER_BY("help.create_time DESC");
            }}.toString();
            return str;
        }
    }
}
