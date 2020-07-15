package cn.eeepay.framework.model;

/**
 * 支行信息表
 */
public class PosCnaps {
    private Integer id;

    private Long cnapsNo;

    private String bankName;

    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCnapsNo() {
        return cnapsNo;
    }

    public void setCnapsNo(Long cnapsNo) {
        this.cnapsNo = cnapsNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}