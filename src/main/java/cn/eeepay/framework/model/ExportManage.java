package cn.eeepay.framework.model;

import java.util.Date;

public class ExportManage {

    private Integer id;//id
    private String md5Key;//MD5条件加密值
    private Date createTime;//创建时间
    private String operator;//当前操作人
    private String remark;//备注
    private String filterRemark;//过滤条件转换显示
    private String filterStr;//过滤条件JSON字符串
    private Integer status;//状态:0 初始化,1 已完成
    private Integer readStatus;//读取状态:0 未读,1 已读
    private Integer downloadNum;//下载次数
    private String fileName;//文件名
    private String fileUrl;//文件存储路径
    private String msg;//错误描述

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFilterRemark() {
        return filterRemark;
    }

    public void setFilterRemark(String filterRemark) {
        this.filterRemark = filterRemark;
    }

    public String getFilterStr() {
        return filterStr;
    }

    public void setFilterStr(String filterStr) {
        this.filterStr = filterStr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public Integer getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(Integer downloadNum) {
        this.downloadNum = downloadNum;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
