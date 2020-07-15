package cn.eeepay.framework.model;

import java.io.Serializable;
import java.util.Date;

/**

 */
public class AgentOperLog implements Serializable{

	private static final long serialVersionUID = 8675520190226100422L;

	//日志编号
	private Integer id;

	//请求方法
	private String request_method;
	
	//请求参数
	private String request_params;
	
	//返回结果
	private String return_result;
	
	//方法描述
	private String method_desc;
	
	//请求ip
	private String oper_ip;

	//操作时间
	private Date oper_time;
	private String oper_start_time;
	private String oper_end_time;

	private String agent_no;
	
	private String agent_name;

	public Integer getId() {
		return id;
	}

	public AgentOperLog setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getRequest_method() {
		return request_method;
	}

	public AgentOperLog setRequest_method(String request_method) {
		this.request_method = request_method;
		return this;
	}

	public String getRequest_params() {
		return request_params;
	}

	public AgentOperLog setRequest_params(String request_params) {
		this.request_params = request_params;
		return this;
	}

	public String getReturn_result() {
		return return_result;
	}

	public AgentOperLog setReturn_result(String return_result) {
		this.return_result = return_result;
		return this;
	}

	public String getMethod_desc() {
		return method_desc;
	}

	public AgentOperLog setMethod_desc(String method_desc) {
		this.method_desc = method_desc;
		return this;
	}

	public String getOper_ip() {
		return oper_ip;
	}

	public AgentOperLog setOper_ip(String oper_ip) {
		this.oper_ip = oper_ip;
		return this;
	}

	public Date getOper_time() {
		return oper_time;
	}

	public AgentOperLog setOper_time(Date oper_time) {
		this.oper_time = oper_time;
		return this;
	}

	public String getAgent_no() {
		return agent_no;
	}

	public AgentOperLog setAgent_no(String agent_no) {
		this.agent_no = agent_no;
		return this;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public AgentOperLog setAgent_name(String agent_name) {
		this.agent_name = agent_name;
		return this;
	}

	public String getOper_start_time() {
		return oper_start_time;
	}

	public AgentOperLog setOper_start_time(String oper_start_time) {
		this.oper_start_time = oper_start_time;
		return this;
	}

	public String getOper_end_time() {
		return oper_end_time;
	}

	public AgentOperLog setOper_end_time(String oper_end_time) {
		this.oper_end_time = oper_end_time;
		return this;
	}
}