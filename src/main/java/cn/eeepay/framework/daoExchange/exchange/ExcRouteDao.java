package cn.eeepay.framework.daoExchange.exchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExcRoute;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * bannerDao
 */
public interface ExcRouteDao {

    @SelectProvider(type=ExcRouteDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExcRoute.class)
    List<ExcRoute> selectAllList(@Param("route") ExcRoute route, @Param("page") Page<ExcRoute> page);

    @Insert(
            "INSERT INTO rdmp_pass_route " +
                    " (channel_no,channel_name,priority,channel_account_number," +
                    "  channel_password,remark,create_time) " +
                    " VALUES " +
                    " (#{route.channelNo},#{route.channelName},#{route.priority},#{route.channelAccountNumber}," +
                    "  #{route.channelPassword},#{route.remark},NOW() ) "
    )
    int addRoute(@Param("route") ExcRoute route);

    @Select(
            "select * from rdmp_pass_route where id=#{id}"
    )
    ExcRoute getRoute(@Param("id") int id);

    @Update(
            "update rdmp_pass_route set " +
                    " channel_no=#{route.channelNo},channel_name=#{route.channelName},priority=#{route.priority}, " +
                    " channel_account_number=#{route.channelAccountNumber},channel_password=#{route.channelPassword}, " +
                    " remark=#{route.remark} " +
                    " where id=#{route.id}"
    )
    int updateRoute(@Param("route") ExcRoute route);

    @Update(
            "update rdmp_pass_route set status=#{state} where id=#{id}"
    )
    int closeRoute(@Param("id") int id, @Param("state") String state);

    @Select(
            "select * from rdmp_pass_route where channel_no=#{route.channelNo}"
    )
    List<ExcRoute> getRouteList(@Param("route") ExcRoute route);

    @Select(
            "select * from rdmp_pass_route where channel_no=#{route.channelNo} and id!=#{route.id}"
    )
    List<ExcRoute> getRouteListById(@Param("route") ExcRoute route);


    @Select(
            "select * from rdmp_pass_route where channel_no=#{channelNo}"
    )
    ExcRoute getRouteByNo(@Param("channelNo") String channelNo);

    @Select(
            "select * from rdmp_pass_route "
    )
    List<ExcRoute> getRouteALLList();

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExcRoute route = (ExcRoute) param.get("route");
            return new SQL(){{
                SELECT("route.*");
                FROM("rdmp_pass_route route");
                if(StringUtils.isNotBlank(route.getChannelNo())){
                    WHERE("route.channel_no = #{route.channelNo} ");
                }
                if(StringUtils.isNotBlank(route.getChannelName())){
                    WHERE("route.channel_name like concat(#{route.channelName},'%') ");
                }
                if(route.getpId()!=null){
                    WHERE("route.channel_no in (select channel_no from rdmp_pass_good where p_id=#{route.pId}) ");
                }
                ORDER_BY("route.create_time desc");
            }}.toString();
        }
    }
}
