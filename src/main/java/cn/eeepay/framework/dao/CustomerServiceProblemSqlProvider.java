package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.CustomerServiceProblem;
import org.apache.ibatis.jdbc.SQL;

public class CustomerServiceProblemSqlProvider {

    public String insertSelective(CustomerServiceProblem record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("customer_service_problem");
        
        if (record.getProblemId() != null) {
            sql.VALUES("problem_id", "#{problemId,jdbcType=INTEGER}");
        }
        
        if (record.getProblemType() != null) {
            sql.VALUES("problem_type", "#{problemType,jdbcType=VARCHAR}");
        }
        
        if (record.getProblemName() != null) {
            sql.VALUES("problem_name", "#{problemName,jdbcType=VARCHAR}");
        }
        
        if (record.getProblemContent() != null) {
            sql.VALUES("problem_content", "#{problemContent,jdbcType=VARCHAR}");
        }
        
        if (record.getAppScope() != null) {
            sql.VALUES("app_scope", "#{appScope,jdbcType=VARCHAR}");
        }
        
        if (record.getClicks() != null) {
            sql.VALUES("clicks", "#{clicks,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateUser() != null) {
            sql.VALUES("create_user", "#{createUser,jdbcType=VARCHAR}");
        }
        
        if (record.getUpdateUser() != null) {
            sql.VALUES("update_user", "#{updateUser,jdbcType=VARCHAR}");
        }
        
        if (record.getProblemStatus() != null) {
            sql.VALUES("problem_status", "#{problemStatus,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(CustomerServiceProblem record) {
        SQL sql = new SQL();
        sql.UPDATE("customer_service_problem");
        
        if (record.getProblemType() != null) {
            sql.SET("problem_type = #{problemType,jdbcType=VARCHAR}");
        }
        
        if (record.getProblemName() != null) {
            sql.SET("problem_name = #{problemName,jdbcType=VARCHAR}");
        }
        
        if (record.getProblemContent() != null) {
            sql.SET("problem_content = #{problemContent,jdbcType=VARCHAR}");
        }
        
        if (record.getAppScope() != null) {
            sql.SET("app_scope = #{appScope,jdbcType=VARCHAR}");
        }
        
        if (record.getClicks() != null) {
            sql.SET("clicks = #{clicks,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateUser() != null) {
            sql.SET("create_user = #{createUser,jdbcType=VARCHAR}");
        }
        
        if (record.getUpdateUser() != null) {
            sql.SET("update_user = #{updateUser,jdbcType=VARCHAR}");
        }
        
        if (record.getProblemStatus() != null) {
            sql.SET("problem_status = #{problemStatus,jdbcType=INTEGER}");
        }
        
        sql.WHERE("problem_id = #{problemId,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}