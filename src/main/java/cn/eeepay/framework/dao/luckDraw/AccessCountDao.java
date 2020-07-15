package cn.eeepay.framework.dao.luckDraw;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.AccessCount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/7/007.
 * @author  liuks
 * 访问统计
 */
public interface AccessCountDao {

    @SelectProvider(type=AccessCountDao.SqlProvider.class,method="selectAllList")
    @ResultType(AccessCount.class)
    List<AccessCount> selectAllList(@Param("acc") AccessCount acc,@Param("page") Page<AccessCount> page);

    @SelectProvider(type=AccessCountDao.SqlProvider.class,method="selectAllList")
    @ResultType(AccessCount.class)
    List<AccessCount> importDetailSelect(@Param("acc")AccessCount acc);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final AccessCount acc = (AccessCount) param.get("acc");
            return new SQL(){{
                SELECT("luck.* ");
                FROM("luck_report luck");
                if(acc.getReqDateBegin() != null){
                    WHERE("luck.req_date >= #{acc.reqDateBegin}");
                }
                if(acc.getReqDateEnd() != null){
                    WHERE("luck.req_date <= #{acc.reqDateEnd}");
                }
                ORDER_BY("luck.req_date desc");
            }}.toString();
        }
    }
}
