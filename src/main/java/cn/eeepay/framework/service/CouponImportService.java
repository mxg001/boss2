package cn.eeepay.framework.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by Administrator on 2019/6/5/005.
 * @author liuks
 * 新增券导入
 */
public interface CouponImportService {


    Map<String,Object> addCouponImport(String param,MultipartFile file) throws Exception;

    Map<String,Object> couponImportCard(String param, MultipartFile file) throws Exception;
}
