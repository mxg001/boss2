package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayNotice;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;


public interface CreditRepayNoticeDao {
    /**
     *根据通告id 查询通告
     */
    @Select({
        "select * from yfb_credit_repay_notice where id = #{id}"
    })
    @ResultType(CreditRepayNotice.class)
    CreditRepayNotice selectById(@Param("id")int id);


    /**
     * 新增通告
     * @param notice
     * @return
     */
    @Insert({
        "insert into yfb_credit_repay_notice(notice_no,title,link,status,create_time,modify_time,one_level_agent_no) " +
                " VALUES(#{notice.noticeNo},#{notice.title},#{notice.link},1,now(),now(),'default');"
    })
    int insertCreditRepayNotice(@Param("notice")CreditRepayNotice notice);


    /**
     * 修改通告
     * @param notice
     * @return
     */
    @Update({
        "update yfb_credit_repay_notice " +
                "set title=#{notice.title},link=#{notice.link},modify_time=NOW() " +
                "where id=#{notice.id}; "
    })
    int updateCreditRepayNotice(@Param("notice")CreditRepayNotice notice);


    /**
     * 下发通告
     * @param notice
     * @return
     */
    @Update({
            "update yfb_credit_repay_notice " +
                    "set status=#{notice.status},issued_time=NOW(),modify_time=NOW() " +
                    "where id=#{notice.id}; "
    })
    int issueCreditRepayNoticeStatus(@Param("notice")CreditRepayNotice notice);

    /**
     * 删除通告
     * @param notice
     * @return
     */
    @Update({
            "update yfb_credit_repay_notice " +
                    "set status=#{notice.status},modify_time=NOW() " +
                    "where id=#{notice.id}; "
    })
    int deleteCreditRepayNoticeStatus(@Param("notice")CreditRepayNotice notice);

    /**
     * 收回通告
     * @param notice
     * @return
     */
    @Update({
            "update yfb_credit_repay_notice " +
                    "set status=#{notice.status},issued_time=null,modify_time=NOW() " +
                    "where id=#{notice.id}; "
    })
    int reclaimCreditRepayNoticeStatus(@Param("notice")CreditRepayNotice notice);

    /**
     *根据条件动态查询列表
     */
    @SelectProvider(type=SqlProvider.class,method="selectAllList")
    @ResultType(CreditRepayNotice.class)
    List<CreditRepayNotice> selectAllList(@Param("queryaram")CreditRepayNotice info, Page<CreditRepayNotice> page);


     class SqlProvider {
        public String selectAllList(final Map<String, Object> param) {
            final CreditRepayNotice info = (CreditRepayNotice) param.get("queryaram");
            return new SQL() {
                {
                    SELECT("*");
                    FROM("yfb_credit_repay_notice");
                    if (StringUtils.isNotBlank(info.getTitle())) {
                        info.setTitle(info.getTitle()+"%");
                        WHERE("title like #{queryaram.title}");
                    }
                    if(info.getCreateTimeBegin() != null){
                        WHERE("create_time >= #{queryaram.createTimeBegin}");
                    }
                    if(info.getCreateTimeEnd() != null){
                        WHERE("create_time < #{queryaram.createTimeEnd}");
                    }
                    if(info.getIssuedTimeBegin() != null){
                        WHERE("issued_time >= #{queryaram.issuedTimeBegin}");
                    }
                    if(info.getIssuedTimeEnd() != null){
                        WHERE("issued_time < #{queryaram.issuedTimeEnd}");
                    }
                    if(info.getStatus()>0){
                        WHERE("status=#{queryaram.status}");
                    }else{
                        WHERE("status!=3");//删除的数据为3
                    }

                    ORDER_BY("IF (isnull(issued_time), 0, 1) ASC");
                    ORDER_BY("issued_time DESC");
                    ORDER_BY("create_time DESC");
                    ORDER_BY("id ASC");
                }
            }.toString();
        }
    }
}
