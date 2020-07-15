/**
 * 
 */
package cn.eeepay.framework.enums;

/**
 * @author yangxiaoshan
 * @date  2018年10月17日
 */
public enum OperType {

	/**
	 * 添加黑名单
	 */
	ADDBLACK(0),
	/**
	 * 关闭黑名单
	 */
	CLOSEBLACK(1),
	/**
	 * 打开黑名单
	 */
	OPENBLACK(2);
	
	private int type;
	
	private OperType(int type) {
	     this.type=type;
	}
	
	public int getOperType() {
		return type;
	}
	
	
}
