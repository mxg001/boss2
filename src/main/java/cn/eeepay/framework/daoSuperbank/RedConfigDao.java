package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditCardBonus;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface RedConfigDao {

    @SelectProvider(type = RedConfigDao.SqlProvider.class, method = "redConfigQuery")
    @ResultType(List.class)
    List<Map<String,Object>> redConfigQuery(@Param("param" ) Map<String,Object> params,@Param("pager") Page<Map<String,Object>> pager);

    @Insert("insert into red_configure (bus_type,push_type,receive_type,push_area,push_org_id,remark,img_url,min_amount," +
            "max_amount,total_amount,red_number,allow_org_profit,create_time,update_time,operator,status,single_scale,service_id,oneladder_allmoney,oneladder_people_num,twoladder_allmoney,twoladder_people_num,manormoney_traderpremium) values (" +
            " #{param.bus_type},#{param.push_type},#{param.receive_type},#{param.push_area},#{param.push_org_id},#{param.remark}," +
            "#{param.img_url},#{param.min_amount},#{param.max_amount},#{param.total_amount},#{param.red_number}," +
            "#{param.allow_org_profit},now(),now(),#{param.operator},'0',#{param.single_scale}, #{param.service_id},#{param.oneladder_allmoney},#{param.oneladder_people_num},#{param.twoladder_allmoney},#{param.twoladder_people_num},#{param.manormoney_traderpremium} )")
    int insert(@Param("param")Map<String,Object> param);

    @Select(" select * from  red_configure where id = #{id}")
    @ResultType(Map.class)
    Map<String,Object> redConfigInfo( @Param("id") String id);

    @Update("update red_configure set bus_type = #{param.bus_type},push_type = #{param.push_type},receive_type = #{param.receive_type},push_area = #{param.push_area},push_org_id = #{param.push_org_id},remark = #{param.remark},\n" +
            "img_url =#{param.img_url},min_amount = #{param.min_amount},max_amount = #{param.max_amount},total_amount = #{param.total_amount},red_number = #{param.red_number},allow_org_profit = #{param.allow_org_profit},\n" +
            "update_time = now(),operator = #{param.operator},single_scale=#{param.single_scale},service_id=#{param.service_id},oneladder_allmoney=#{param.oneladder_allmoney},oneladder_people_num=#{param.oneladder_people_num},twoladder_allmoney=#{param.twoladder_allmoney},twoladder_people_num=#{param.twoladder_people_num},manormoney_traderpremium=#{param.manormoney_traderpremium} where id  = #{param.id}  ")
    int redConfigInfoUpdate(@Param("param") Map param);

    @Update("update red_configure set status = #{status} where id = #{id} ")
    int updateRedStatus(@Param("id") String id, @Param("status") String status);

    @Select(" select * from red_product_conf where conf_type = #{type}")
    Map<String,Object> redProductInfo(String type);

    @Update(" update red_product_conf set date_start = #{param.date_start},date_end = #{param.date_end},user_oneday_num = #{param.user_oneday_num},effective_time = #{param.effective_time},recovery_type = #{param.recovery_type}," +
            "update_time = now(),operator = #{param.operator} where conf_type = #{param.conf_type} ")
    int redProductInfoUpdate(@Param("param") Map params);

    @Select("select count(1) from red_orders where conf_id=#{id}")
    int configUseStatus(@Param("id") String id);

    @Select("select recovery_type from red_product_conf where conf_type=#{busType}")
    String getRecoveryType(@Param("busType") String busType);

    public class SqlProvider{

        public String redConfigQuery(Map<String,Object> param){

            StringBuffer sql = new StringBuffer("");

            final  Map<String,Object> params =(Map<String,Object>) param.get("param");
            sql.append("select * from red_configure where 1 =1 ");
            if(params.get("id")!=null && StringUtils.isNotBlank((String)params.get("id"))){
                sql.append(" and id = #{param.id}");
            }
            if(params.get("push_type")!=null && StringUtils.isNotBlank((String)params.get("push_type"))){
                sql.append(" and push_type = #{param.push_type}");
            }
            if(params.get("bus_type")!=null && StringUtils.isNotBlank((String)params.get("bus_type"))){
                sql.append(" and bus_type = #{param.bus_type}");
            }
            if(params.get("push_area")!=null && StringUtils.isNotBlank((String)params.get("push_area"))){
                sql.append(" and push_area = #{param.push_area}");
            }
            if(params.get("receive_type")!=null && StringUtils.isNotBlank((String)params.get("receive_type"))){
                sql.append(" and receive_type = #{param.receive_type}");
            }
            if(params.get("status")!=null && StringUtils.isNotBlank((String)params.get("status"))){
                sql.append(" and status = #{param.status}");
            }
            if(params.get("push_org_id")!=null && (Integer)params.get("push_org_id") != -1){
                sql.append(" and push_org_id = #{param.push_org_id}");
            }
            sql.append(" order by create_time desc");
            return sql.toString();
        }

    }

}
