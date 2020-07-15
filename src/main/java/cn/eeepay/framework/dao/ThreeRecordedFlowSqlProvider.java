package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ThreeRecordedFlow;
import org.apache.ibatis.jdbc.SQL;

public class ThreeRecordedFlowSqlProvider {

    public String insertSelective(ThreeRecordedFlow record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("three_recorded_flow");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getAgentNo() != null) {
            sql.VALUES("agent_no", "#{agentNo,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getActivteAmount() != null) {
            sql.VALUES("activte_amount", "#{activteAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getRecordedStatus() != null) {
            sql.VALUES("recorded_status", "#{recordedStatus,jdbcType=INTEGER}");
        }
        
        if (record.getRecordedSum() != null) {
            sql.VALUES("recorded_sum", "#{recordedSum,jdbcType=DECIMAL}");
        }
        
        if (record.getFromSerialNo() != null) {
            sql.VALUES("from_serial_no", "#{fromSerialNo,jdbcType=VARCHAR}");
        }
        
        if (record.getTransOrderNo() != null) {
            sql.VALUES("trans_order_no", "#{transOrderNo,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ThreeRecordedFlow record) {
        SQL sql = new SQL();
        sql.UPDATE("three_recorded_flow");
        
        if (record.getAgentNo() != null) {
            sql.SET("agent_no = #{agentNo,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getActivteAmount() != null) {
            sql.SET("activte_amount = #{activteAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getRecordedStatus() != null) {
            sql.SET("recorded_status = #{recordedStatus,jdbcType=INTEGER}");
        }
        
        if (record.getRecordedSum() != null) {
            sql.SET("recorded_sum = #{recordedSum,jdbcType=DECIMAL}");
        }
        
        if (record.getFromSerialNo() != null) {
            sql.SET("from_serial_no = #{fromSerialNo,jdbcType=VARCHAR}");
        }
        
        if (record.getTransOrderNo() != null) {
            sql.SET("trans_order_no = #{transOrderNo,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }
}