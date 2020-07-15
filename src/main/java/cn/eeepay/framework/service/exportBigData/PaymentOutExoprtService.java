package cn.eeepay.framework.service.exportBigData;

import javax.servlet.http.HttpSession;

public interface PaymentOutExoprtService {

    void export(String userName, String md5Key, String param, HttpSession session);

}
