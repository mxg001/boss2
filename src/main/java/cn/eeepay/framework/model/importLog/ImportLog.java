package cn.eeepay.framework.model.importLog;

import java.util.Date;

/**
 * Created by Administrator on 2018/11/13/013.
 * @author liuks
 * 导入处理日志类
 * 对应表 import_log
 */
public class ImportLog {

    private Integer id;
    private String  batchNo;//批次号
    private String  logSource;//导入入口 对应数据字典 LOG_SOURCE 值
    private String  msg;//总反馈处理结果
    private String operator;//操作人
    private Date createTime;//创建时间
    private String status;//导入状态

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getLogSource() {
        return logSource;
    }

    public void setLogSource(String logSource) {
        this.logSource = logSource;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
