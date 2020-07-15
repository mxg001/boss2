package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.util.StringUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/09/12.
 *
 * @author zja
 * 防火墙 dao
 */
public interface FirewallDao {

    @SelectProvider(type = FirewallDao.SqlProvider.class, method = "selectSysConfig")
    @ResultType(Map.class)
    List<Map> selectSysConfig(Map params);

    @SelectProvider(type = FirewallDao.SqlProvider.class, method = "selectRecordList")
    @ResultType(Map.class)
    List<Map> selectRecordList(Map params, Page<Map> page);

    @SelectProvider(type = FirewallDao.SqlProvider.class, method = "selectRecordList")
    @ResultType(Map.class)
    List<Map> selectRecordList2(Map params);

    @UpdateProvider(type = FirewallDao.SqlProvider.class, method = "addRecord")
    int addRecord(Map params);

    @UpdateProvider(type = FirewallDao.SqlProvider.class, method = "updateRecord")
    int updateRecord(Map params);

    @UpdateProvider(type = FirewallDao.SqlProvider.class, method = "deleteRecord")
    int deleteRecord(Map params);

    class SqlProvider {
        public String selectRecordList(final Map<String, Object> params) {
            StringBuffer column = new StringBuffer(" pf.firewall_code,pf.able_status,pf.ctrl_target_code,pf.create_time,pf.remark,pf.ctrl_biz_type,CASE WHEN pf.able_status='enable' then 1 ELSE 0 END able_status_switch");
            StringBuffer table = new StringBuffer(" from pa_firewall pf");
            StringBuffer where = new StringBuffer(" where 1=1");
            if (StringUtil.isNotBlank(params.get("firewall_code"))) {where.append(" and pf.firewall_code = #{firewall_code}");}
            if (StringUtil.isNotBlank(params.get("able_status"))) {where.append(" and pf.able_status = #{able_status}");}
            if (StringUtil.isNotBlank(params.get("ctrl_biz_type"))) {where.append(" and pf.ctrl_biz_type = #{ctrl_biz_type}");}
            if (StringUtil.isNotBlank(params.get("ctrl_target_code"))) {where.append(" and pf.ctrl_target_code = #{ctrl_target_code}");}
            if (StringUtil.isNotBlank(params.get("ctrl_target_type"))) {where.append(" and pf.ctrl_target_type = #{ctrl_target_type}");}
            if (StringUtil.isNotBlank(params.get("id"))) {where.append(" and pf.id = #{id}");}
            if (StringUtil.isNotBlank(params.get("list_type"))) {where.append(" and pf.list_type = #{list_type}");}
            if (StringUtil.isNotBlank(params.get("op_user"))) {where.append(" and pf.op_user = #{op_user}");}
            if (StringUtil.isNotBlank(params.get("remark"))) {where.append(" and pf.remark = #{remark}");}
            if (StringUtil.isNotBlank(params.get("createTimeBegin")) && StringUtil.isNotBlank(params.get("createTimeEnd"))) {
                where.append(" and pf.create_time BETWEEN #{createTimeBegin} and #{createTimeEnd}");
            }

            //关联用户进行查询
            if("user".equals(params.get("ctrl_target_type"))){
                table.append(" inner join pa_user_info pu on pu.user_code = pf.ctrl_target_code");
                column.append(",pu.real_name,pu.user_code");
                if (StringUtil.isNotBlank(params.get("agent_no"))) {where.append(" and pu.agent_no = #{agent_no}");}
                if (StringUtil.isNotBlank(params.get("agent_node"))) {where.append(" and pu.agent_node = #{agent_node}");}
                if (StringUtil.isNotBlank(params.get("brand_code"))) {where.append(" and pu.brand_code = #{brand_code}");}
                if (StringUtil.isNotBlank(params.get("creater"))) {where.append(" and pu.creater = #{creater}");}
                if (StringUtil.isNotBlank(params.get("pu_create_time"))) {where.append(" and pu.create_time = #{pu_create_time}");}
                if (StringUtil.isNotBlank(params.get("grade"))) {where.append(" and pu.grade = #{grade}");}
                if (StringUtil.isNotBlank(params.get("id"))) {where.append(" and pu.id = #{pu_id}");}
                if (StringUtil.isNotBlank(params.get("id_card_no"))) {where.append(" and pu.id_card_no = #{id_card_no}");}
                if (StringUtil.isNotBlank(params.get("is_acc"))) {where.append(" and pu.is_acc = #{is_acc}");}
                if (StringUtil.isNotBlank(params.get("mobile"))) {where.append(" and pu.mobile = #{mobile}");}
                if (StringUtil.isNotBlank(params.get("one_user_code"))) {where.append(" and pu.one_user_code = #{one_user_code}");}
                if (StringUtil.isNotBlank(params.get("other_profit_swith"))) {where.append(" and pu.other_profit_swith = #{other_profit_swith}");}
                if (StringUtil.isNotBlank(params.get("parent_id"))) {where.append(" and pu.parent_id = #{parent_id}");}
                if (StringUtil.isNotBlank(params.get("pwd"))) {where.append(" and pu.pwd = #{pwd}");}
                if (StringUtil.isNotBlank(params.get("real_name"))) {where.append(" and pu.real_name = #{real_name}");}
                if (StringUtil.isNotBlank(params.get("reg_type"))) {where.append(" and pu.reg_type = #{reg_type}");}
                if (StringUtil.isNotBlank(params.get("share_level"))) {where.append(" and pu.share_level = #{share_level}");}
                if (StringUtil.isNotBlank(params.get("status"))) {where.append(" and pu.status = #{status}");}
                if (StringUtil.isNotBlank(params.get("trans_profit_swith"))) {where.append(" and pu.trans_profit_swith = #{trans_profit_swith}");}
                if (StringUtil.isNotBlank(params.get("two_user_code"))) {where.append(" and pu.two_user_code = #{two_user_code}");}
                if (StringUtil.isNotBlank(params.get("update_time"))) {where.append(" and pu.update_time = #{update_time}");}
                if (StringUtil.isNotBlank(params.get("user_code"))) {where.append(" and pu.user_code = #{user_code}");}
                if (StringUtil.isNotBlank(params.get("user_level"))) {where.append(" and pu.user_level = #{user_level}");}
                if (StringUtil.isNotBlank(params.get("user_node"))) {where.append(" and pu.user_node = #{user_node}");}
                if (StringUtil.isNotBlank(params.get("user_type"))) {where.append(" and pu.user_type = #{user_type}");}
            }

            //关联商户进行查询
            if("mer".equals(params.get("ctrl_target_type"))) {
                table.append(" inner join pa_mer_info pm on pm.merchant_no = pf.ctrl_target_code");
                column.append(",pm.merchant_no,pm.mobile_phone");
                if (StringUtil.isNotBlank(params.get("agent_no"))) {where.append(" and pm.agent_no = #{agent_no}");}
                if (StringUtil.isNotBlank(params.get("agent_node"))) {where.append(" and pm.agent_node = #{agent_node}");}
                if (StringUtil.isNotBlank(params.get("bind_ter"))) {where.append(" and pm.bind_ter = #{bind_ter}");}
                if (StringUtil.isNotBlank(params.get("create_time"))) {where.append(" and pm.create_time = #{pm_create_time}");}
                if (StringUtil.isNotBlank(params.get("id"))) {where.append(" and pm.id = #{pm_id}");}
                if (StringUtil.isNotBlank(params.get("merchant_no"))) {where.append(" and pm.merchant_no = #{merchant_no}");}
                if (StringUtil.isNotBlank(params.get("mobile_phone"))) {where.append(" and pm.mobile_phone = #{mobile_phone}");}
                if (StringUtil.isNotBlank(params.get("status"))) {where.append(" and pm.status = #{pm_status}");}
                if (StringUtil.isNotBlank(params.get("user_code"))) {where.append(" and pm.user_code = #{user_code}");}
                if (StringUtil.isNotBlank(params.get("user_id"))) {where.append(" and pm.user_id = #{user_id}");}
                if (StringUtil.isNotBlank(params.get("user_node"))) {where.append(" and pm.user_node = #{user_node}");}
            }

            StringBuffer sql = new StringBuffer("select ");
            sql.append(column).append(table).append(where);
            sql.append(" order by pf.id desc");
            return sql.toString();
        }

        public String addRecord(final Map<String, Object> param) {
            StringBuffer sb = new StringBuffer("INSERT INTO pa_firewall");
            sb.append(" (firewall_code, list_type, ctrl_target_type, ctrl_target_code, ctrl_biz_type, able_status, remark, create_time, op_user)")
                    .append(" VALUES (REPLACE(UUID(),'-',''),#{list_type},#{ctrl_target_type},#{ctrl_target_code}")
                    .append(",#{ctrl_biz_type},#{able_status},#{remark},now(),#{op_user}")
                    .append(")");
            return sb.toString();
        }

        public String updateRecord(final Map<String, Object> param) {
            StringBuffer sb = new StringBuffer("UPDATE pa_firewall SET");
            sb.append(" op_user=#{op_user}")
                    .append(",ctrl_target_code=#{ctrl_target_code}")
                    .append(",able_status=#{able_status}")
                    .append(",ctrl_biz_type=#{ctrl_biz_type}")
                    .append(",remark=#{remark}")
                    .append(" WHERE firewall_code=#{firewall_code}");
            return sb.toString();
        }

        public String deleteRecord(final Map<String, Object> param) {
            return "delete from pa_firewall where firewall_code = #{firewall_code}";
        }

        public String countGoods(final Map<String, Object> param) {
            return "select COUNT(g.id) cnt from pa_goods g where g.group_code = #{groupCode}";
        }

        public String selectSysConfig(final Map<String, Object> params){
            StringBuffer sql = new StringBuffer("select * from pa_sys_dict d where d.status=1");
            if(StringUtil.isNotBlank(params.get("sys_key"))){sql.append(" and sys_key=#{sys_key}");}
            if(StringUtil.isNotBlank(params.get("sys_name"))){sql.append(" and sys_key=#{sys_name}");}
            if(StringUtil.isNotBlank(params.get("parent_id"))){sql.append(" and sys_key=#{parent_id}");}
            return sql.toString();
        }
    }
}
