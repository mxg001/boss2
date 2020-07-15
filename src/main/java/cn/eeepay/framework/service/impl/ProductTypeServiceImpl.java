package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.ProductTypeDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.*;
import cn.eeepay.framework.service.ExchangeProductService;
import cn.eeepay.framework.service.ProductTypeService;
import cn.eeepay.framework.service.PropertyConfigService;
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
@Service("productTypeService")
public class ProductTypeServiceImpl implements ProductTypeService {

    private static final Logger log = LoggerFactory.getLogger(ProductTypeServiceImpl.class);

    @Resource
    private ProductTypeDao productTypeDao;

    @Resource
    private ExchangeProductService exchangeProductService;

    @Resource
    private PropertyConfigService propertyConfigService;

    @Override
    public List<ProductType> selectAllList(ProductType pt, Page<ProductType> page) {
        return productTypeDao.selectAllList(pt,page);
    }

    @Override
    public List<ProductType> importDetailSelect(ProductType pt) {
        return productTypeDao.importDetailSelect(pt);
    }

    @Override
    public int addProductType(ProductType pt,List<PropertyConfig> list) {
        int num=productTypeDao.addProductType(pt);
        if(num>0){
            if(list!=null&&list.size()>0){
                for(PropertyConfig pc:list){
                    pc.setConfigCode(pt.getTypeCode());
                    pc.setConfigValue(pc.getPropertyDefaultValue());
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        //不为空时存值
                        propertyConfigService.addTypeConfig(pc);
                    }
                }
            }
        }
        return num;
    }

    @Override
    public int deleteProductType(long id) {
        int num=productTypeDao.deleteProductType(id);
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
                    if(!checkProduct(Long.valueOf(id))){
                        num++;
                        productTypeDao.deleteProductType(Long.valueOf(id));
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
        ProductType pt=productTypeDao.getProductType(id);
        List<ExchangeProduct> list=exchangeProductService.getExchangeProductList(pt.getTypeCode());
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public int editProductType(ProductType pt,List<PropertyConfig> list,List<PropertyConfig> fileList) {
        int num=productTypeDao.editProductType(pt);
        if(num>0){
            if(list!=null&&list.size()>0){
                for(PropertyConfig pc:list){
                    PropertyConfig oldpc=propertyConfigService.updateTypeConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            pc.setConfigCode(pt.getTypeCode());
                            propertyConfigService.addTypeConfig(pc);
                        }else{
                            propertyConfigService.updateTypeConfig(pc);
                        }
                    }else{
                        if(oldpc!=null){
                            propertyConfigService.deleteTypeConfig(pc);
                        }
                    }
                }
            }
            if(fileList!=null&&fileList.size()>0){
                for(PropertyConfig pc:fileList){
                    PropertyConfig oldpc=propertyConfigService.updateTypeConfigSelect(pc);
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        if(oldpc==null){
                            pc.setConfigCode(pt.getTypeCode());
                            propertyConfigService.addTypeConfig(pc);
                        }else{
                            propertyConfigService.updateTypeConfig(pc);
                        }
                    }
                }
            }
        }
        return num;
    }

    @Override
    public ProductType getProductType(long id) {
        return productTypeDao.getProductType(id);
    }

    @Override
    public List<ProductType> getProductTypeList(String orgCode) {
        return productTypeDao.getProductTypeList(orgCode);
    }

    @Override
    public boolean checkTypeName(ProductType pt) {
        if(pt.getId()==null){//新增
            List<ProductType> oldList=productTypeDao.checkOrgName(pt);
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }else{
            List<ProductType> oldList=productTypeDao.checkOrgNameId(pt);
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public ProductType getProductOne(String typeCode) {
        List<ProductType> list=productTypeDao.getProductTypeCode(typeCode);
        if(list!=null&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean checkPrepaidCard(String typeCode) {
        List<PropertyConfig> checkboxList=propertyConfigService.getTypeConfigAndValue("checkbox_config","pro_type_config",typeCode);
        if(checkboxList!=null&&checkboxList.size()>0){
            for(PropertyConfig pc:checkboxList){
                if("need_receive_info".equals(pc.getPropertyCode())){//签收信息
                    if("1".equals(pc.getConfigValue())){
                        return  true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, String> getAddProductType(String typeCode) {
        List<PropertyConfig> checkboxList=propertyConfigService.getTypeConfigAndValue("checkbox_config","pro_type_config",typeCode);
        Map<String, String> map=new HashMap<String,String>();
        if(checkboxList!=null&&checkboxList.size()>0){
            for(PropertyConfig pc:checkboxList){
                map.put(pc.getPropertyCode(),pc.getConfigValue());
            }
        }
        return map;
    }

    @Override
    public void importDetail(List<ProductType> list, HttpServletResponse response) throws Exception {

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

            for (ProductType or : list) {
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
