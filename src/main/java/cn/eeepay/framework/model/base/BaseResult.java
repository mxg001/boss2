package cn.eeepay.framework.model.base;

import cn.eeepay.framework.util.StringUtil;

import java.io.Serializable;

public class BaseResult implements Serializable {

    private String msg;
    private boolean status;
    private Object data;

    private BaseResult(String msg, boolean status, Object data) {
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    public static BaseResult success(String msg){
        return success(msg,null);
    }

    public static BaseResult success(String msg,Object data){
        if(StringUtil.isEmpty(msg)){
            msg = "操作成功";
        }
        return new BaseResult(msg,true,data);
    }

    public static BaseResult success(){
        return success(null,null);
    }


    public static BaseResult error(String msg){
        if(StringUtil.isEmpty(msg)){
            msg = "系统发生未知异常";
        }
        return new BaseResult(msg,false,null);
    }

    public static BaseResult error(){
        return error(null);
    }


}
