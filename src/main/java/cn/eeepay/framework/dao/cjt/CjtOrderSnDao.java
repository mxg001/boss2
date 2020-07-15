package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.model.cjt.CjtOrderSn;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;


/**
 * @author tans
 * @date 2019/5/31 16:57
 */
public interface CjtOrderSnDao {

    @Select("select * from cjt_order_sn where sn = #{sn} limit 1")
    @ResultType(CjtOrderSn.class)
    CjtOrderSn selectBySn(String sn);

    @Insert("<script>" +
            "insert into cjt_order_sn(merchant_no,buy_order_no,sn,create_time) values " +
            "<foreach collection='snList' item='item' index='index'  close=';' separator=','> " +
            " (#{merchantNo},#{orderNo},#{item},#{currentDate})" +
            "</foreach> " +
            "</script>")
    int insertBatch(@Param("orderNo") String orderNo, @Param("merchantNo")String merchantNo,
                    @Param("currentDate")Date currentDate, @Param("snList")List<String> snList);


    @Select("select count(*) from cjt_order_sn where sn >= #{snStart} and sn <= #{snEnd}")
    @ResultType(Integer.class)
    Integer selectBySnStartAndEnd(@Param("snStart") String snStart, @Param("snEnd")String snEnd);

    @Select("select cos.*, hp.type_name as hpName from cjt_order_sn cos " +
            " inner join terminal_info ti on ti.sn = cos.sn" +
            " left join hardware_product hp on hp.hp_id = ti.type" +
            " where cos.buy_order_no = #{orderNo}")
    @ResultType(CjtOrderSn.class)
    List<CjtOrderSn> selectCjtOrderSnList(String orderNo);

    @Delete("delete from cjt_order_sn where sn = #{sn}")
    int deleteBySn(@Param("sn") String sn);
}
