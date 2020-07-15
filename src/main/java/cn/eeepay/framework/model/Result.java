package cn.eeepay.framework.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 服务器返回数据结果
 * tans
 * 2017-11-29
 */
public class Result implements Serializable {
    
	private static final long serialVersionUID = 3677878008034002428L;

	//是否处理成功
	private boolean status = false;
	
	//提示信息
	private String msg = "系统异常";
	
	//数据
	private Object data;

	//状态码
	private Integer code = 400;

	public static Result success(){
		return success("操作成功");
	}

	public static Result success(String msg){
		Result result = new Result();
		result.setStatus(true);
		if(StringUtils.isNotBlank(msg)){
			result.setMsg(msg);
		} else {
			result.setMsg("操作成功");
		}
		return result;
	}
	public static Result success(String msg,Object data){
		Result result = new Result();
		result.setStatus(true);
		result.setData(data);
		if(StringUtils.isNotBlank(msg)){
			result.setMsg(msg);
		} else {
			result.setMsg("操作成功");
		}
		return result;
	}

	public static Result fail(){
		return fail("操作失败");
	}

	public static Result fail(String msg){
		Result result = new Result();
		if(StringUtils.isNotBlank(msg)){
			result.setMsg(msg);
		} else {
			result.setMsg("操作失败");
		}
		return result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
