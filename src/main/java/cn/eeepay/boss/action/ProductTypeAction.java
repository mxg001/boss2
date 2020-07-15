package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.OrgManagement;
import cn.eeepay.framework.model.exchange.ProductType;
import cn.eeepay.framework.model.exchange.PropertyConfig;
import cn.eeepay.framework.service.OrgManagementService;
import cn.eeepay.framework.service.ProductTypeService;
import cn.eeepay.framework.service.PropertyConfigService;
import cn.eeepay.framework.service.RandomNumberService;
import cn.eeepay.framework.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 超级兑产品类别Action
 */
@Controller
@RequestMapping(value = "/productType")
public class ProductTypeAction {

    private static final Logger log = LoggerFactory.getLogger(ProductTypeAction.class);
    @Resource
    private ProductTypeService productTypeService;

    @Resource
    private RandomNumberService randomNumberService;

    @Resource
    private PropertyConfigService propertyConfigService;

    @Resource
    private OrgManagementService orgManagementService;

    /**
     * 查询机构列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ProductType> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductType pt = JSONObject.parseObject(param, ProductType.class);
            productTypeService.selectAllList(pt, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询机构列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询机构列表失败!");
        }
        return msg;
    }

    /**
     * 新增获取字段默认值
     */
    @RequestMapping(value = "/getaddOrgDefault")
    @ResponseBody
    public Map<String,Object> getaddOrgDefault() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<PropertyConfig> list1=propertyConfigService.getOrgConfig("checkbox_config","pro_type_config");
            List<PropertyConfig> list2=propertyConfigService.getOrgConfig("file_config","pro_type_config");
            List<PropertyConfig> list3=propertyConfigService.getOrgConfig("text_config","pro_type_config");
            msg.put("status", true);
            msg.put("checkboxListDefault", list1);
            msg.put("fileListDefault", list2);
            msg.put("textListDefault", list3);
        } catch (Exception e){
            log.error("新增获取字段默认值失败!",e);
            msg.put("status", false);
            msg.put("msg", "新增获取字段默认值失败!");
        }
        return msg;
    }
    /**
     * 新增产品类别
     */
    @RequestMapping(value = "/addProductType")
    @ResponseBody
    @SystemLog(description = "新增产品类别",operCode="productType.addProductType")
    public Map<String,Object> addProductType(@RequestParam("info") String param,
                                             @RequestParam("checkboxList") String listStr1,
                                             @RequestParam("fileList") String listStr2,
                                             @RequestParam("textList") String listStr3) throws Exception{

        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductType pt = JSONObject.parseObject(param, ProductType.class);
            if(productTypeService.checkTypeName(pt)){
                OrgManagement org= orgManagementService.getOrgOne(pt.getOrgCode());
                msg.put("status", false);
                msg.put("msg","机构:"+org.getOrgName()+" 下产品类别名称已存在!");
                return msg;
            }
            List<PropertyConfig> checkboxList = JSONObject.parseArray(listStr1,PropertyConfig.class);
            List<PropertyConfig> fileList = JSONObject.parseArray(listStr2,PropertyConfig.class);
            List<PropertyConfig> textList = JSONObject.parseArray(listStr3,PropertyConfig.class);
            List<PropertyConfig> list=new ArrayList<PropertyConfig>();
            list.addAll(checkboxList);
            list.addAll(fileList);
            list.addAll(textList);
            String code=randomNumberService.getRandomNumber("TYPE","type_unique");
            pt.setTypeCode(code);
            int num=productTypeService.addProductType(pt,list);
            if(num>0){
                msg.put("msg","添加成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "新增产品类别失败!");
        } catch (Exception e){
            log.error("新增产品类别失败!",e);
            msg.put("status", false);
            msg.put("msg", "新增产品类别失败!");
        }
        return msg;
    }

    /**
     * 获取产品类别详情
     */
    @RequestMapping(value = "/getProductType")
    @ResponseBody
    public Map<String,Object> getProductType(@RequestParam("id") long  id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductType pt=productTypeService.getProductType(id);
            List<PropertyConfig> checkboxList=propertyConfigService.getTypeConfigAndValue("checkbox_config","pro_type_config",pt.getTypeCode());
            List<PropertyConfig> fileList=propertyConfigService.getTypeConfigAndValue("file_config","pro_type_config",pt.getTypeCode());
            List<PropertyConfig> textList=propertyConfigService.getTypeConfigAndValue("text_config","pro_type_config",pt.getTypeCode());
            if(fileList!=null&&fileList.size()>0){
                for(PropertyConfig pc:fileList){
                    if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                        pc.setConfigValue(CommonUtil.getImgUrlAgent(pc.getConfigValue()));
                    }
                }
            }
            msg.put("pt",pt);
            msg.put("checkboxListDefault", checkboxList);
            msg.put("fileListDefault", fileList);
            msg.put("textListDefault", textList);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取产品类别详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取产品类别详情失败!");
        }
        return msg;
    }
    /**
     * 编辑产品类别
     */
    @RequestMapping(value = "/editProductType")
    @ResponseBody
    @SystemLog(description = "编辑产品类别",operCode="productType.editProductType")
    public Map<String,Object> editProductType(@RequestParam("info") String param,
                                              @RequestParam("checkboxList") String listStr1,
                                              @RequestParam("fileList") String listStr2,
                                              @RequestParam("textList") String listStr3) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductType pt = JSONObject.parseObject(param, ProductType.class);
            if(productTypeService.checkTypeName(pt)){
                OrgManagement org= orgManagementService.getOrgOne(pt.getOrgCode());
                msg.put("status", false);
                msg.put("msg","机构:"+org.getOrgName()+" 下产品类别名称已存在!");
                return msg;
            }
            List<PropertyConfig> checkboxList = JSONObject.parseArray(listStr1,PropertyConfig.class);
            List<PropertyConfig> fileList = JSONObject.parseArray(listStr2,PropertyConfig.class);
            List<PropertyConfig> textList = JSONObject.parseArray(listStr3,PropertyConfig.class);
            if(pt.getId()!=null){
                List<PropertyConfig> list=new ArrayList<PropertyConfig>();
                list.addAll(checkboxList);
                list.addAll(textList);
                int num=productTypeService.editProductType(pt,list,fileList);
                if(num>0){
                    msg.put("msg","修改成功!");
                    msg.put("status", true);
                    return msg;
                }
            }
            msg.put("status", false);
            msg.put("msg", "修改产品类别失败!");
        } catch (Exception e){
            log.error("修改产品类别失败!",e);
            msg.put("status", false);
            msg.put("msg", "修改产品类别失败!");
        }
        return msg;
    }
    /**
     * 删除产品类别
     */
    @RequestMapping(value = "/deleteProductType")
    @ResponseBody
    @SystemLog(description = "删除产品类别",operCode="productType.deleteProductType")
    public Map<String,Object> deleteProductType(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            if(productTypeService.checkProduct(id)){
                msg.put("status", false);
                msg.put("msg", "该产品类别下存在产品,不能删除!");
                return msg;
            }
            //验证
            int num=productTypeService.deleteProductType(id);
            if(num>0){
                msg.put("msg","删除成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "删除产品类别失败!");
        } catch (Exception e){
            log.error("删除产品类别失败!",e);
            msg.put("status", false);
            msg.put("msg", "删除产品类别失败!");
        }
        return msg;
    }
    /**
     * 批量删除产品类别
     */
    @RequestMapping(value = "/deleteProductTypeBatch")
    @ResponseBody
    @SystemLog(description = "批量删除产品类别",operCode="productType.deleteProductTypeBatch")
    public Map<String,Object> deleteProductTypeBatch(@RequestParam("ids") String ids) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            productTypeService.deleteProductTypeBatch(ids,msg);
        } catch (Exception e){
            log.error("批量删除产品类别失败!",e);
            msg.put("status", false);
            msg.put("msg", "批量删除产品类别失败!");
        }
        return msg;
    }

    /**
     * 获取产品类别列表
     */
    @RequestMapping(value = "/getProductTypeList")
    @ResponseBody
    public Map<String,Object> getProductTypeList(@RequestParam("orgCode") String orgCode) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<ProductType> list=productTypeService.getProductTypeList(orgCode);
            msg.put("status", true);
            msg.put("list",list);
        } catch (Exception e){
            log.error("获取产品类别列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取产品类别列表失败!");
        }
        return msg;
    }

    /**
     * 获取产品类别新增的必填项
     */
    @RequestMapping(value = "/getAddProductType")
    @ResponseBody
    public Map<String,Object> getAddProductType(@RequestParam("typeCode") String  typeCode) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            Map<String,String> map=productTypeService.getAddProductType(typeCode);
            msg.put("mapConfig", map);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取产品类别新增的必填项失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取产品类别新增的必填项失败!");
        }
        return msg;
    }

    /**
     * 积分产品类别列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "积分产品类别列表",operCode="productType.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ProductType pt = JSONObject.parseObject(param, ProductType.class);
            List<ProductType> list=productTypeService.importDetailSelect(pt);
            productTypeService.importDetail(list,response);
        }catch (Exception e){
            log.error("积分产品类别列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "积分产品类别列表异常!");
        }
        return msg;
    }
}
