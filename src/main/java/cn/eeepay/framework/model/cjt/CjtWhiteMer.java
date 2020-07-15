package cn.eeepay.framework.model.cjt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 超级推商户白名单（已下架的商品，白名单用户仍能看到）表 cjt_white_mer
 * 
 * @author tans
 * @date 2019-05-29
 */
public class CjtWhiteMer
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 商户编号 */
	private String merchantNo;
	/** 白名单状态 0关闭 1开启 */
	private Integer status;
	/** 备注 */
	private String remark;
	/** 创建时间 */
	private Date createTime;
	/** 创建人 */
	private String creater;

	private String createTimeStart;
	private String createTimeEnd;
	private String merchantName;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setMerchantNo(String merchantNo) 
	{
		this.merchantNo = merchantNo;
	}

	public String getMerchantNo() 
	{
		return merchantNo;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Integer getStatus()
	{
		return status;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setCreater(String creater) 
	{
		this.creater = creater;
	}

	public String getCreater() 
	{
		return creater;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
