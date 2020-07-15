package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ProductTypeActivateDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateProduct;
import cn.eeepay.framework.model.exchangeActivate.ProductTypeActivate;
import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateProductService;
import cn.eeepay.framework.service.exchangeActivate.ProductTypeActivateService;
import cn.eeepay.framework.service.exchangeActivate.PropertyConfigActivateService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 *  超级兑 产品类别
 */
@Service("productTypeActivateService")
public class ProductTypeActivateServiceImpl implements ProductTypeActivateService {

    private static final Logger log = LoggerFactory.getLogger(ProductTypeActivateServiceImpl.class);

    @Resource
    private ProductTypeActivateDao productTypeActivateDao;

    @Resource
    private ExchangeActivateProductService exchangeActivateProductService;

    @Resource
    private PropertyConfigActivateService propertyConfigActivateService;

    @Override
    public List<ProductTypeActivate> selectAllList(ProductTypeActivate pt, Page<ProductTypeActivate> page) {
        return productTypeActivateDao.selectAllList(pt,page);
    }

    @Override
    public List<ProductTypeActivate> importDetailSelect(ProductTypeActivate pt) {
        return productTypeActivateDao.importDetailSelect(pt);
    }

    @Override
    public int addProductType(ProductTypeActivate pt,List<PropertyConfigActivate> list) {
        int num=productTypeActivateDao.addProductType(pt);
        if(num>0){
            if(list!=null&&list.size()>0){
                for(PropertyConfigActivate pc:list){
                    pc.setConfigCode(pt.getTypeCode());
                    pc.setConfigValue(pc.getPropertyDefaultValue());
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        //不为空时存值
                        propertyConfigActivateService.addTypeConfig(pc);
                    }
                }
            }
        }
        return num;
    }

    @Override
    public int deleteProductType(long id) {
        int num=productTypeActivateDao.deleteProductType(id);
        return num;
    }

    @Override
    public int deleteProductTypeBatch(String ids,Map<String, Object> msg) {
        if(ids!=null&&!"".equals(ids)){
            String[] strs=ids.split(",");
            if(strs!=null&&strs.length>0){
                int length=strs.length;
                int num=0;
                for(String id:strs){
                    //验证
                    if(!checkProduct(Long.parseLong(id))){
                        num++;
                        productTypeActivateDao.deleteProductType(Long.parseLong(id));
                    }
                }
                msg.put("status", true);
                msg.put("msg", "批量删除成功,选中"+length+"条数据,成功删除"+num+"条!");
                return 1;
            }
        }
        msg.put("status", false);
        msg.put("msg", "批量删除失败!");
        return 0;
    }
    /**
     * 验证该产品类别下是否含有产品
     */
    @Override
    public boolean checkProduct(long id){
        ProductTypeActivate pt=productTypeActivateDao.getProductType(id);
        List<ExchangeActivateProduct> list=exchangeActivateProductService.getExchangeProductList(pt.getTypeCode());
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public int editProductType(ProductTypeActivate pt,List<PropertyConfigActivate> list,List<PropertyConfigActivate> fileList) {
        int num=productTypeActivateDao.editProductType(pt);
        if(num>0){
            if(list!=null&&list.size()>0){
                for(PropertyConfigActivate pc:list){
                    PropertyConfigActivate oldpc=propertyConfigActivateService.updateTypeConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            pc.setConfigCode(pt.getTypeCode());
                            propertyConfigActivateService.addTypeConfig(pc);
                        }else{
                            propertyConfigActivateService.updateTypeConfig(pc);
                        }
                    }else{
                        if(oldpc!=null){
                            propertyConfigActivateService.deleteTypeConfig(pc);
                        }
                    }
                }
            }
            if(fileList!=null&&fileList.size()>0){
                for(PropertyConfigActivate pc:fileList){
                    PropertyConfigActivate oldpc=propertyConfigActivateService.updateTypeConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            pc.setConfigCode(pt.getTypeCode());
                            propertyConfigActivateService.addTypeConfig(pc);
                        }else{
                            propertyConfigActivateService.updateTypeConfig(pc);
                        }
                    }
                }
            }
        }
        return num;
    }

    @Override
    public ProductTypeActivate getProductType(long id) {
        return productTypeActivateDao.getProductType(id);
    }

    @Override
    public List<ProductTypeActivate> getProductTypeList(String orgCode) {
        return productTypeActivateDao.getProductTypeList(orgCode);
    }

    @Override
    public boolean checkTypeName(ProductTypeActivate pt) {
        if(pt.getId()==null){//新增
            List<ProductTypeActivate> oldList=productTypeActivateDao.checkOrgName(pt);
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }else{
            List<ProductTypeActivate> oldList=productTypeActivateDao.checkOrgNameId(pt);
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public ProductTypeActivate getProductOne(String typeCode) {
        List<ProductTypeActivate> list=productTypeActivateDao.getProductTypeCode(typeCode);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Map<String, String> getAddProductType(String typeCode) {
        List<PropertyConfigActivate> checkboxList=propertyConfigActivateService.getTypeConfigAndValue("checkbox_config","pro_type_config",typeCode);
        Map<String, String> map=new HashMap<String,String>();
        if(checkboxList!=null&&checkboxList.size()>0){
            for(PropertyConfigActivate pc:checkboxList){
                map.put(pc.getPropertyCode(),pc.getConfigValue());
            }
        }
        return map;
    }

    @Override
    public void importDetail(List<ProductTypeActivate> list, HttpServletResponse response) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "积分产品类别列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("typeCode",null);
            maps.put("orgName",null);
            maps.put("typeName",null);
            maps.put("videoUrl",null);
            maps.put("courseUrl",null);
            maps.put("bankUrl",null);
            maps.put("hint",null);
            maps.put("declaraType",null);
            data.add(maps);
        }else{
            Map<String, String> declaraTypeMap=new HashMap<String, String>();
            declaraTypeMap.put("1","微信报单");
            declaraTypeMap.put("2","自助报单");

            for (ProductTypeActivate or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()+"");
                maps.put("typeCode",or.getTypeCode()==null?"":or.getTypeCode());
                maps.put("orgName",or.getOrgName()==null?"":or.getOrgName());
                maps.put("typeName",or.getTypeName()==null?"":or.getTypeName());
                maps.put("videoUrl",or.getVideoUrl()==null?"":or.getVideoUrl());
                maps.put("courseUrl",or.getCourseUrl()==null?"":or.getCourseUrl());
                maps.put("bankUrl",or.getBankUrl()==null?"":or.getBankUrl());
                maps.put("hint",or.getHint()==null?"":or.getHint());
                maps.put("declaraType",declaraTypeMap.get(or.getDeclaraType()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","typeCode","orgName","typeName","videoUrl","courseUrl",
                "bankUrl","hint","declaraType"
        };
        String[] colsName = new String[]{"序号","编码","机构名称","产品类别名称","视频地址","兑换教程",
                "兑换入口","兑换入口跳转提示语","报单方式"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出积分产品类别列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}
