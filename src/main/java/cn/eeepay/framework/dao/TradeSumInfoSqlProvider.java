package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.TradeSumInfo;
import org.apache.ibatis.jdbc.SQL;

public class TradeSumInfoSqlProvider {

    public String insertSelective(TradeSumInfo record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("trade_sum_info");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getAgentNo() != null) {
            sql.VALUES("agent_no", "#{agentNo,jdbcType=VARCHAR}");
        }
        
        if (record.getBranch() != null) {
            sql.VALUES("branch", "#{branch,jdbcType=VARCHAR}");
        }
        
        if (record.getOneLevel() != null) {
            sql.VALUES("one_level", "#{oneLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getTwoLevel() != null) {
            sql.VALUES("two_level", "#{twoLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getThreeLevel() != null) {
            sql.VALUES("three_level", "#{threeLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getFourLevel() != null) {
            sql.VALUES("four_level", "#{fourLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getFiveLevel() != null) {
            sql.VALUES("five_level", "#{fiveLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getTradeSum() != null) {
            sql.VALUES("trade_sum", "#{tradeSum,jdbcType=DECIMAL}");
        }
        
        if (record.getMerSum() != null) {
            sql.VALUES("mer_sum", "#{merSum,jdbcType=INTEGER}");
        }
        
        if (record.getActivateSum() != null) {
            sql.VALUES("activate_sum", "#{activateSum,jdbcType=INTEGER}");
        }
        
        if (record.getMachinesStock() != null) {
            sql.VALUES("machines_stock", "#{machinesStock,jdbcType=INTEGER}");
        }
        
        if (record.getUnusedMachines() != null) {
            sql.VALUES("unused_machines", "#{unusedMachines,jdbcType=INTEGER}");
        }
        
        if (record.getExpiredNotActivated() != null) {
            sql.VALUES("expired_not_activated", "#{expiredNotActivated,jdbcType=INTEGER}");
        }
        
        if (record.getThreeIncome() != null) {
            sql.VALUES("three_income", "#{threeIncome,jdbcType=DECIMAL}");
        }
        
        if (record.getRecordedDate() != null) {
            sql.VALUES("recorded_date", "#{recordedDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getRecordedStatus() != null) {
            sql.VALUES("recorded_status", "#{recordedStatus,jdbcType=INTEGER}");
        }
        
        if (record.getTeamId() != null) {
            sql.VALUES("team_id", "#{teamId,jdbcType=VARCHAR}");
        }
        
        if (record.getIncomeCalc() != null) {
            sql.VALUES("income_calc", "#{incomeCalc,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(TradeSumInfo record) {
        SQL sql = new SQL();
        sql.UPDATE("trade_sum_info");
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getAgentNo() != null) {
            sql.SET("agent_no = #{agentNo,jdbcType=VARCHAR}");
        }
        
        if (record.getBranch() != null) {
            sql.SET("branch = #{branch,jdbcType=VARCHAR}");
        }
        
        if (record.getOneLevel() != null) {
            sql.SET("one_level = #{oneLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getTwoLevel() != null) {
            sql.SET("two_level = #{twoLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getThreeLevel() != null) {
            sql.SET("three_level = #{threeLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getFourLevel() != null) {
            sql.SET("four_level = #{fourLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getFiveLevel() != null) {
            sql.SET("five_level = #{fiveLevel,jdbcType=VARCHAR}");
        }
        
        if (record.getTradeSum() != null) {
            sql.SET("trade_sum = #{tradeSum,jdbcType=DECIMAL}");
        }
        
        if (record.getMerSum() != null) {
            sql.SET("mer_sum = #{merSum,jdbcType=INTEGER}");
        }
        
        if (record.getActivateSum() != null) {
            sql.SET("activate_sum = #{activateSum,jdbcType=INTEGER}");
        }
        
        if (record.getMachinesStock() != null) {
            sql.SET("machines_stock = #{machinesStock,jdbcType=INTEGER}");
        }
        
        if (record.getUnusedMachines() != null) {
            sql.SET("unused_machines = #{unusedMachines,jdbcType=INTEGER}");
        }
        
        if (record.getExpiredNotActivated() != null) {
            sql.SET("expired_not_activated = #{expiredNotActivated,jdbcType=INTEGER}");
        }
        
        if (record.getThreeIncome() != null) {
            sql.SET("three_income = #{threeIncome,jdbcType=DECIMAL}");
        }
        
        if (record.getRecordedDate() != null) {
            sql.SET("recorded_date = #{recordedDate,jdbcType=TIMESTAMP}");
        }
        
        if (record.getRecordedStatus() != null) {
            sql.SET("recorded_status = #{recordedStatus,jdbcType=INTEGER}");
        }
        
        if (record.getTeamId() != null) {
            sql.SET("team_id = #{teamId,jdbcType=VARCHAR}");
        }
        
        if (record.getIncomeCalc() != null) {
            sql.SET("income_calc = #{incomeCalc,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }
}