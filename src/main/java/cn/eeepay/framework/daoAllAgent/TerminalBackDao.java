package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.TerminalBack;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface TerminalBackDao {

    @SelectProvider(type=SqlProvider.class,method="queryTerminalBackList")
    @ResultType(TerminalBack.class)
    List<TerminalBack> queryTerminalBackList(@Param("info") TerminalBack info, @Param("page") Page<TerminalBack> page);

    @Select("select * from pa_back_sn where order_no=#{orderNo}")
    @ResultType(Map.class)
    List<Map<String, Object>> queryTerminalBackSN(@Param("orderNo") String orderNo);

    class SqlProvider{

        public String queryTerminalBackList(final Map<String, Object> param) {
            final TerminalBack info = (TerminalBack) param.get("info");
            SQL sql = new SQL(){{
                SELECT("ptb.*,u1.one_user_code,u2.user_type receive_user_type,(SELECT count(1) from pa_back_sn pbs where ptb.order_no=pbs.order_no) count ");
                FROM("pa_terminal_back ptb");
                LEFT_OUTER_JOIN("pa_user_info u1 on u1.user_code=ptb.user_code");
                LEFT_OUTER_JOIN("pa_user_info u2 on u2.user_code=ptb.receive_user_code");
            }};
            where(sql, info);
            sql.ORDER_BY("ptb.create_time DESC");
            return sql.toString();
        }

        public void where(SQL sql, TerminalBack info) {
            if(StringUtils.isNotBlank(info.getOrderNo())){
                sql.WHERE("ptb.order_no = #{info.orderNo} ");
            }
            if(StringUtils.isNotBlank(info.getUserCode())){
                sql.WHERE("ptb.user_code = #{info.userCode} ");
            }
            if(StringUtils.isNotBlank(info.getReceiveUserCode())){
                sql.WHERE("ptb.receive_user_code = #{info.receiveUserCode} ");
            }
            if(StringUtils.isNotBlank(info.getOneUserCode())){
                sql.WHERE("u1.one_user_code = #{info.oneUserCode} ");
            }
            if(info.getReceiveUserType()!=null){
                sql.WHERE("u2.user_type = #{info.receiveUserType} ");
            }
            if(info.getStatus()!=null){
                sql.WHERE("ptb.status = #{info.status} ");
            }
            if(StringUtils.isNotBlank(info.getBackStartTime())){
                sql.WHERE("ptb.create_time  >= #{info.backStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getBackEndTime())){
                sql.WHERE("ptb.create_time  <= #{info.backEndTime} ");
            }
            if(StringUtils.isNotBlank(info.getReceiveStartTime())||StringUtils.isNotBlank(info.getReceiveEndTime())) {
                sql.WHERE("ptb.status = 1 ");
            }
            if (StringUtils.isNotBlank(info.getReceiveStartTime())) {
                sql.WHERE("ptb.last_update_time  >= #{info.receiveStartTime} ");
            }
            if (StringUtils.isNotBlank(info.getReceiveEndTime())) {
                sql.WHERE("ptb.last_update_time  <= #{info.receiveEndTime} ");
            }
        }
    }
}
