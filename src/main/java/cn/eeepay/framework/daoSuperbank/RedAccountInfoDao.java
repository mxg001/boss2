package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ManorAccountDetail;
import cn.eeepay.framework.model.RedAccountDetail;
import cn.eeepay.framework.model.RedAccountInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RedAccountInfoDao {

    @Update("update red_account_info set total_amount=total_amount+#{notReceiveAmount}" +
            " where relation_id=#{relationId}")
    int updateAmount(@Param("relationId") Long relationId,
                     @Param("notReceiveAmount") BigDecimal notReceiveAmount);

    @Select("select * from red_account_info where relation_id=#{relationId}")
    @ResultType(RedAccountInfo.class)
    RedAccountInfo selectByRelationId(@Param("relationId") Long relationId);

    @Insert("insert into red_account_detail(red_account_id,account_code,create_date,type," +
            "trans_amount,red_order_id,remark,order_no,redmoney)" +
            "values(#{redAccountId},#{accountCode},#{createDate},#{type}," +
            "#{transAmount},#{redOrderId},#{remark},#{orderNo},#{redmoney})")
    int insertAccountDetail(RedAccountDetail accountDetail);

    @Select("select id,type,relation_id,account_code,total_amount from red_account_info where type=0 limit 1")
    RedAccountInfo plateAccountInfo();

    @Select("select id,type,relation_id,account_code,total_amount,user_code from red_account_info where id=#{id}")
    RedAccountInfo plateAccountInfoReload(@Param("id")Long id);
    
    @SelectProvider(type=SqlProvider.class, method="selectAccountDetailPage")
    @ResultType(RedAccountDetail.class)
    List<RedAccountDetail> selectAccountDetailPage(@Param("baseInfo") RedAccountDetail baseInfo, @Param("page")Page<RedAccountDetail> page);
   
    @SelectProvider(type=SqlProvider.class, method="selectManorAccountDetailPage")
    @ResultType(ManorAccountDetail.class)
    List<ManorAccountDetail> selectManorAccountDetailPage(@Param("baseInfo") ManorAccountDetail baseInfo, @Param("page")Page<ManorAccountDetail> page);

    @SelectProvider(type=SqlProvider.class, method="selectAccountDetailPage")
    @ResultType(RedAccountDetail.class)
    RedAccountDetail selectAccountDetailSum(@Param("baseInfo")RedAccountDetail baseInfo);
    
    @SelectProvider(type=SqlProvider.class, method="selectManorAccountDetailPage")
    @ResultType(ManorAccountDetail.class)
    ManorAccountDetail selectManorAccountDetailSum(@Param("baseInfo")ManorAccountDetail baseInfo);

    class SqlProvider{
        public String selectAccountDetailPage(Map<String, Object> param){
            RedAccountDetail baseInfo = (RedAccountDetail)param.get("baseInfo");
            SQL sql = new SQL();
            //查询类型，getSelectType 1表示汇总
            if(baseInfo != null && baseInfo.getSelectType() != null && baseInfo.getSelectType() == 1){
                if (baseInfo != null && baseInfo.getMethodType() != null && baseInfo.getMethodType() == 1) {
                    sql.SELECT("sum(case when rad.type = '0' then abs(rad.trans_amount) else 0 end) as m0," +
                            "sum(case when rad.type = '1' then abs(rad.trans_amount) else 0 end) as m1," +
                            "sum(case when rad.type = '2' then abs(rad.trans_amount) else 0 end) as m2," +
                            "sum(case when rad.type = '3' then abs(rad.trans_amount) else 0 end) as m3," +
                            "sum(case when rad.type = '4' then abs(rad.trans_amount) else 0 end) as m4," +
                            "sum(case when rad.type = '5' then abs(rad.trans_amount) else 0 end) as m5," +
                            "sum(case when rad.type = '6' then abs(rad.trans_amount) else 0 end) as m6," +
                            "sum(case when rad.type = '7' then abs(rad.trans_amount) else 0 end) as m7," +
                            "sum(case when rad.type = '8' then abs(rad.trans_amount) else 0 end) as m8," +
                            "sum(case when rad.type = '9' then abs(rad.trans_amount) else 0 end) as m9," +
                            "sum(case when rad.type = '10' then abs(rad.trans_amount) else 0 end) as m10," +
                            "sum(case when rad.type = '11' then abs(rad.trans_amount) else 0 end) as m11," +
                            "sum(case when rad.type = '12' then abs(rad.trans_amount) else 0 end) as m12," +
                            "sum(case when rad.type = '13' then abs(rad.trans_amount) else 0 end) as m13");

                }else{
                    sql.SELECT("sum(rad.trans_amount) as transAmountSum");
                }
            } else {
                if (baseInfo != null && baseInfo.getMethodType() != null && baseInfo.getMethodType() == 1){
                    sql.SELECT("rad.*,ro.bus_type,CASE WHEN ri.type = '1' THEN ri.relation_id WHEN ri.type = '2' THEN ri.user_code ELSE ''END relationId,ri.type as userType ");
                   /* sql.SELECT("rad.*,ro.bus_type,CASE WHEN ri.type = '1' THEN ri.relation_id WHEN ri.type = '2' THEN ri.user_code ELSE ''END relationId,ri.type as userType,CASE WHEN ri.type = '1' THEN oi.org_name WHEN ri.type = '2' THEN ui.user_name ELSE ''END userOrgName");*/
                }else {
                    sql.SELECT("rad.*,ro.bus_type");
                }
            }
            sql.FROM("red_account_detail rad");
            sql.LEFT_OUTER_JOIN("red_orders ro on ro.id=rad.red_order_id");
            /*共有条件------start*/
            if(baseInfo != null && baseInfo.getMethodType() != null && baseInfo.getMethodType() == 1){
                sql.LEFT_OUTER_JOIN("red_account_info ri on ri.id=rad.red_account_id");
            /*    sql.LEFT_OUTER_JOIN("org_info oi on ri.relation_id=oi.org_id");
                sql.LEFT_OUTER_JOIN("user_info ui on ri.relation_id=ui.user_id");*/
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                sql.WHERE("rad.create_date >= #{baseInfo.createDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                sql.WHERE("rad.create_date <= #{baseInfo.createDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getBusType()) ){
            	if (baseInfo.getBusType().equals("15")) {
            		  sql.WHERE("rad.type>=8");
				}else
					sql.WHERE("ro.bus_type=#{baseInfo.busType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderNo())){
                sql.WHERE("rad.order_no=#{baseInfo.orderNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getType())){
                sql.WHERE("rad.type=#{baseInfo.type}");
            }
            if(baseInfo.getRedOrderId()!=null){
                sql.WHERE("rad.red_order_id=#{baseInfo.redOrderId}");
            }
            /*共有条件------end*/
            if (baseInfo != null && baseInfo.getMethodType() != null && baseInfo.getMethodType() == 1){
                if(StringUtils.isNotBlank(baseInfo.getUserType())){
                    sql.WHERE("ri.type=#{baseInfo.userType}");
                }
                if (baseInfo.getUserType()!=null && baseInfo.getUserType().equals("2")) {
                    if(baseInfo.getRelationId()!=null) {
                        sql.WHERE("ri.user_code=#{baseInfo.relationId}");
                    }
                }else{
                    if(baseInfo.getRelationId()!=null) {
                        sql.WHERE("ri.relation_id=#{baseInfo.relationId}");
                    }
                }
                if(baseInfo.getId()!=null){
                    sql.WHERE("rad.id=#{baseInfo.id}");
                }

            }else{
                if(baseInfo.getRelationId()!=null){
                    sql.WHERE("ri.relation_id=#{baseInfo.relationId}");
                }
                sql.WHERE("rad.red_account_id=#{baseInfo.redAccountId}");
            }
            return sql.toString() + " ORDER BY rad.id DESC ,rad.create_date DESC ";
        }
        
        public String selectManorAccountDetailPage(Map<String, Object> param){
            ManorAccountDetail baseInfo = (ManorAccountDetail)param.get("baseInfo");
            SQL sql = new SQL();
            if(baseInfo != null && baseInfo.getSelectType() != null && baseInfo.getSelectType() == 1){
                sql.SELECT("sum(rad.total_amount) as transAmountSum");
            } else {
                if (baseInfo.getType()!=null && baseInfo.getType().equals("2")) {
                	 sql.SELECT("rad.*,ui.user_name,ui.user_code as tempId");
                }else{
                	 sql.SELECT("rad.*,oi.org_name as userName,rad.relation_id as tempId");
                }
            }
            
            sql.FROM("red_account_info rad");
            
            if (baseInfo.getType()!=null && baseInfo.getType().equals("2")) {
            	sql.LEFT_OUTER_JOIN("user_info ui on ui.user_code=rad.user_code");
			}else{
				sql.LEFT_OUTER_JOIN("org_info oi on oi.org_id=rad.relation_id");
			}
           
            if(StringUtils.isNotBlank(baseInfo.getType())){
                sql.WHERE("rad.type=#{baseInfo.type}");
            }
            if(StringUtils.isNotBlank(baseInfo.getFreeMoneyStart())){
                sql.WHERE("rad.total_amount>=#{baseInfo.freeMoneyStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getFreeMoneyEnd())){
                sql.WHERE("rad.total_amount<=#{baseInfo.freeMoneyEnd}");
            }

            if(baseInfo.getRelationId()!=null){
            	if (baseInfo.getType()!=null && baseInfo.getType().equals("2")) {
            		 sql.WHERE("rad.user_code=#{baseInfo.relationId}");
            	}else{
            		 sql.WHERE("rad.relation_id=#{baseInfo.relationId}");
            	}
            }
            return sql.toString() + " order by rad.id desc ";
        }
    }
	
}
