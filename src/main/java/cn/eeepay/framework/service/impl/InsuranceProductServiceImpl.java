package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.InsuranceCompanyDao;
import cn.eeepay.framework.daoSuperbank.InsuranceProductDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceCompany;
import cn.eeepay.framework.model.InsuranceProduct;
import cn.eeepay.framework.service.InsuranceProductService;
import cn.eeepay.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("insuranceProductService")
@Transactional
public class InsuranceProductServiceImpl implements InsuranceProductService {

    @Resource
    private InsuranceProductDao insuranceProductDao;
    @Resource
    private InsuranceCompanyDao insuranceCompanyDao;

    @Override
    public List<InsuranceProduct> selectList(InsuranceProduct baseInfo, Page<InsuranceProduct> page) {
        if(baseInfo.getCompanyNo()==-1){
            baseInfo.setCompanyNo(null);
        }
        List<InsuranceProduct> list=insuranceProductDao.selectList(baseInfo,page);
        for(InsuranceProduct insuranceProduct:list){
            filterModel(insuranceProduct);
        }
        return list;
    }

    @Override
    public InsuranceProduct selectDetail(Long id) {
        InsuranceProduct insuranceProduct = insuranceProductDao.selectDetail(id);
        filterModel(insuranceProduct);
        return insuranceProduct;
    }

    @Override
    public int addProduct(InsuranceProduct info) {
        info.setCreateBy(CommonUtil.getLoginUser().getUsername());
        info.setCreateDate(new Date());
        info.setStatus(0);
        return insuranceProductDao.insert(info);
    }

    @Override
    public int updateProduct(InsuranceProduct info) {
        info.setUpdateBy(CommonUtil.getLoginUser().getUsername());
        info.setUpdateDate(new Date());
        return insuranceProductDao.updateProduct(info);
    }

    @Override
    public int updateProductStatus(InsuranceProduct info)
    {
        info.setUpdateBy(CommonUtil.getLoginUser().getUsername());
        info.setUpdateDate(new Date());
        return insuranceProductDao.updateProductStatus(info);
    }

    @Override
    public boolean selectOrderExists(Integer showOrder) {
        return insuranceProductDao.selectOrderExists(showOrder)>0?true:false;
    }

    @Override
    public boolean selectOrderIdExists(Integer showOrder, Long productId) {
        return insuranceProductDao.selectOrderIdExists(showOrder,productId)>0?true:false;
    }

    @Override
    public boolean selectProductIdExists(String uppProductId) {
        return insuranceProductDao.selectProductIdExists(uppProductId)>0?true:false;
    }

    @Override
    public boolean selectProductIdExists(String uppProductId, Long productId) {
        return insuranceProductDao.selectProductAndIdExists(uppProductId,productId)>0?true:false;
    }

    @Override
    public boolean selectProductNameExists(String productName) {
        return insuranceProductDao.selectProductNameExists(productName)>0?true:false;
    }

    @Override
    public boolean selectProductNameIdExists(String productName, Long productId) {
        return insuranceProductDao.selectProductNameIdExists(productName,productId)>0?true:false;
    }

    @Override
    public List<InsuranceProduct> getListAll() {
        return insuranceProductDao.getListAll();
    }

    @Override
    public int deleteProduct(InsuranceProduct innsuranceProduct) {
        return insuranceProductDao.deleteProduct(innsuranceProduct.getProductId());
    }

    void filterModel(InsuranceProduct insuranceProduct){
        if(insuranceProduct.getCompanyNo()!=null){
            InsuranceCompany insuranceCompany = insuranceCompanyDao.selectByCompanyNo(insuranceProduct.getCompanyNo());
            insuranceProduct.setCompanyName(insuranceCompany.getCompanyName());
            insuranceProduct.setCompanyNickName(insuranceCompany.getCompanyNickName());

        }
        insuranceProduct.setStatusStr(getStatus().get(insuranceProduct.getStatus()));
        insuranceProduct.setProductTypeStr(getProductType().get(insuranceProduct.getProductType()));
        insuranceProduct.setBonusTypeStr(getBonusType().get(insuranceProduct.getBonusType()));
        insuranceProduct.setBonusSettleTimeStr(getBonusSettleTime().get(insuranceProduct.getBonusSettleTime()));
        insuranceProduct.setRecommendStatusStr(getRecommendStatus().get(insuranceProduct.getRecommendStatus()));
        if (StringUtils.isNotBlank(insuranceProduct.getProductImage())){
            insuranceProduct.setProductImageUrl(CommonUtil.getImgUrlAgent(insuranceProduct.getProductImage()));
        }
    }

    public Map<Integer, String> getStatus(){
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "关闭");
        map.put(1, "开启");
        return map;
    }

    public Map<Integer, String> getRecommendStatus(){
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "否");
        map.put(1, "是");
        return map;
    }
    public Map<Integer, String> getProductType(){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "意外险");
        map.put(2, "健康医疗险");
        map.put(3, "家财险");
        map.put(4, "医疗意外险");
        return map;
    }
    public Map<Integer, String> getBonusType(){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "固定奖金");
        map.put(2, "按比例发放");
        return map;
    }
    public Map<Integer, String> getBonusSettleTime(){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "实时");
        map.put(2, "周结");
        map.put(3, "月结");
        return map;
    }
}
