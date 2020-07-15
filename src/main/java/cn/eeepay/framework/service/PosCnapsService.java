package cn.eeepay.framework.service;

import cn.eeepay.framework.model.PosCnaps;

import java.util.List;

public interface PosCnapsService {

    List<PosCnaps> query(String bankName, String cityName);
}
