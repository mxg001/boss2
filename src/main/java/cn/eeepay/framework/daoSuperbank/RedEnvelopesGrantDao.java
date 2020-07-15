package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/19/019.
 * 红包发放查询 dao
 * @author  liuks
 */
public interface RedEnvelopesGrantDao {

    /**
     *多条件分页查询
     */
    @SelectProvider(type=RedEnvelopesGrantDao.SqlProvider.class,method="selectAllList")
    @ResultType(RedEnvelopesGrant.class)
    List<RedEnvelopesGrant> selectAllByParam(@Param("reg")RedEnvelopesGrant reg,@Param("page")Page<RedEnvelopesGrant> page);

    @SelectProvider(type=RedEnvelopesGrantDao.SqlProvider.class,method="selectAllList")
    @ResultType(RedEnvelopesGrant.class)
    List<RedEnvelopesGrant> exportInfo(@Param("reg")RedEnvelopesGrant reg);

    @SelectProvider(type=RedEnvelopesGrantDao.SqlProvider.class,method="selectAllList")
    @ResultType(RedEnvelopesGrant.class)
    RedEnvelopesGrant sumCount(@Param("reg")RedEnvelopesGrant reg);


    @SelectProvider(type=RedEnvelopesGrantDao.SqlProvider.class,method="selectRedEnvelopesGrant")
    @ResultType(RedEnvelopesGrant.class)
    RedEnvelopesGrant selectRedEnvelopesGrantById(@Param("id")Long id);

    //查询红包图片
    @Select("select * from red_orders_imgs where red_order_id=#{id}")
    @ResultType(RedEnvelopesGrantImage.class)
    List<RedEnvelopesGrantImage> getImages(@Param("id")Long id);

    //查询红包评论
    @Select("select * from red_orders_discuss where red_order_id=#{id} ORDER BY create_date desc ")
    @ResultType(RedEnvelopesGrantDiscuss.class)
    List<RedEnvelopesGrantDiscuss> getRedEnvelopesGrantDiscuss(@Param("id")Long id,@Param("page")Page<RedEnvelopesGrantDiscuss> page);

    //查询红包操作记录
    @Select("select * from red_orders_option where red_order_id=#{id} ORDER BY create_date desc")
    @ResultType(RedEnvelopesGrantOption.class)
    List<RedEnvelopesGrantOption> selectRedEnvelopesGrantOption(@Param("id")Long id,@Param("page")Page<RedEnvelopesGrantOption> page);

    //修改图片状态
    @Update(
            "update red_orders_imgs set status=#{img.status} where id=#{img.id}"
    )
    int updateRedEnvelopesGrantImage(@Param("img")RedEnvelopesGrantImage img);

    //修改图片状态
    @Update(
            "update red_orders_imgs set status=#{img.status} where red_order_id=#{img.id} and status='0' "
    )
    int updateRedEnvelopesGrantImageAll(@Param("img")RedEnvelopesGrantImage img);

    //修改图片状态
    @Update(
            "update red_orders_extends set status='1' where red_order_id=#{id} "
    )
    int updateRemark(@Param("id")Long id);

    //修改图片状态
    @Update(
            "update red_orders set status_risk=#{statusRisk},status_recovery=#{statusRecovery}," +
                    "recovery_type=#{recoveryType} where id=#{id} "
    )
    int updateStatusRisk(RedEnvelopesGrant reg);

    //红包评论删除
    @Update(
            "update red_orders_discuss set status='1' where id=#{id} "
    )
    int deleteRedEnvelopesGrantDiscuss(Long id);

    @Select("select bus_type from red_orders where id = #{redOrderId}")
    String getBusType(@Param("redOrderId") Long redOrderId);


    class SqlProvider{

        public String selectRedEnvelopesGrant(final Map<String, Object> param){
            final Long id = (Long) param.get("id");
            String str=new SQL() {{
                SELECT(" reg.*, roe.push_address,roe.remark,oneUi.user_name as one_user_name," +
                        "twoUi.user_name as two_user_name,thrUi.user_name as thr_user_name," +
                        "fouUi.user_name as fou_user_name,roe.status as remark_status ");
                FROM("red_orders reg");
                LEFT_OUTER_JOIN(" red_orders_extends roe ON roe.red_order_id=reg.id ");
                LEFT_OUTER_JOIN(" user_info oneUi ON oneUi.user_code=reg.one_user_code ");
                LEFT_OUTER_JOIN(" user_info twoUi ON twoUi.user_code=reg.two_user_code ");
                LEFT_OUTER_JOIN(" user_info thrUi ON thrUi.user_code=reg.thr_user_code ");
                LEFT_OUTER_JOIN(" user_info fouUi ON fouUi.user_code=reg.fou_user_code ");


                WHERE("reg.id=#{id}");
            }}.toString();
            return str;
        }

        public String selectAllList(final Map<String, Object> param){
            final RedEnvelopesGrant reg = (RedEnvelopesGrant) param.get("reg");
            String str=new SQL() {{
                if(reg.getSumState()==0){
                    SELECT(" reg.* ");
                    SELECT("ui.user_name as pushRealName");
                }else if(reg.getSumState()==1){
                   SELECT(" sum(reg.push_amount) amount_count");
                   SELECT(" sum(reg.plate_profit) plate_profit_sum");
                }
                FROM("red_orders reg");
                LEFT_OUTER_JOIN("user_info ui on ui.user_code = reg.push_user_code");
                if(reg.getId()!=null){
                    WHERE(" reg.id=#{reg.id}");
                }
                if(reg.getCreateDateMin()!=null){
                    WHERE(" reg.create_date>=#{reg.createDateMin}");
                }
                if(reg.getCreateDateMax()!=null){
                    WHERE(" reg.create_date<=#{reg.createDateMax}");
                }
                if(StringUtils.isNotBlank(reg.getPushArea())){
                    WHERE(" reg.push_area=#{reg.pushArea}");
                }
                if(StringUtils.isNotBlank(reg.getPushType())){
                    WHERE(" reg.push_type=#{reg.pushType}");
                }
                if(StringUtils.isNotBlank(reg.getReceiveType())){
                    WHERE(" reg.receive_type=#{reg.receiveType}");
                }
                if(StringUtils.isNotBlank(reg.getBusType())){
                    WHERE(" reg.bus_type=#{reg.busType}");
                }
                if(reg.getOrgId()!=null){
                    WHERE(" reg.org_id=#{reg.orgId}");
                }
                if(StringUtils.isNotBlank(reg.getHasProfit())){
                    WHERE(" reg.has_profit=#{reg.hasProfit}");
                }
                if(reg.getConfId()!=null){
                    WHERE(" reg.conf_id=#{reg.confId}");
                }
                if(StringUtils.isNotBlank(reg.getPushUserCode())){
                    WHERE(" reg.push_user_code=#{reg.pushUserCode}");
                }
                if(StringUtils.isNotBlank(reg.getPushUserName())){
                    WHERE(" ui.user_name like concat(#{reg.pushUserName},'%')");
                }
                if(StringUtils.isNotBlank(reg.getPushUserPhone())){
                    WHERE(" reg.push_user_phone=#{reg.pushUserPhone}");
                }

                if(StringUtils.isNotBlank(reg.getDxUserCode())){
                    WHERE(" reg.dx_user_code=#{reg.dxUserCode}");
                }
                if(StringUtils.isNotBlank(reg.getDxUserName())){
                    WHERE(" reg.dx_user_name like concat(#{reg.dxUserName},'%')");
                }
                if(StringUtils.isNotBlank(reg.getDxUserPhone())){
                    WHERE(" reg.dx_user_phone=#{reg.dxUserPhone}");
                }
                if(StringUtils.isNotBlank(reg.getStatus())){
                    WHERE(" reg.status=#{reg.status}");
                }
                if(StringUtils.isNotBlank(reg.getStatusRisk())){
                    WHERE(" reg.status_risk=#{reg.statusRisk}");
                }
                if(StringUtils.isNotBlank(reg.getStatusRecovery())){
                    WHERE(" reg.status_recovery=#{reg.statusRecovery}");
                }
                if(StringUtils.isNotBlank(reg.getRecoveryType())){
                    WHERE(" reg.recovery_type=#{reg.recoveryType}");
                }
                if(StringUtils.isNotBlank(reg.getOrderNo())){
                    WHERE(" reg.order_no=#{reg.orderNo}");
                }
                if(StringUtils.isNotBlank(reg.getPayOrderNo())){
                    WHERE(" reg.pay_order_no=#{reg.payOrderNo}");
                }
                if(StringUtils.isNotBlank(reg.getPayType())){
                    WHERE(" reg.pay_type=#{reg.payType}");
                }
                ORDER_BY("reg.create_date desc");
            }}.toString();
            return str;
        }
    }

}
