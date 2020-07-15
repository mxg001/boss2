package cn.eeepay.framework.exception;

/**
 * Created by Administrator on 2017/5/18.
 */
public class WorkOrderException extends RuntimeException {
    public WorkOrderException() {
        super();
    }

    public WorkOrderException(String message) {
        super(message);
    }

    public WorkOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkOrderException(Throwable cause) {
        super(cause);
    }

    protected WorkOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
