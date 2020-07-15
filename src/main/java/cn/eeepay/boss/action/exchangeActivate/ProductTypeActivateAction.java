package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrg;
import cn.eeepay.framework.model.exchangeActivate.ProductTypeActivate;
import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;
import cn.eeepay.framework.service.RandomNumberService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateOrgService;
import cn.eeepay.framework.service.exchangeActivate.ProductTypeActivateService;
import cn.eeepay.framework.service.exchangeActivate.PropertyConfigActivateService;
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
@RequestMapping(value = "/productTypeActivate")
public class ProductTypeActivateAction {

    private static final Logger log = LoggerFactory.getLogger(ProductTypeActivateAction.class);
    @Resource
    private ProductTypeActivateService productTypeActivateService;

    @Resource
    private RandomNumberService randomNumberService;

    @Resource
    private PropertyConfigActivateService propertyConfigActivateService;

    @Resource
    private ExchangeActivateOrgService exchangeActivateOrgService;

    /**
     * 查询机构列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ProductTypeActivate> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductTypeActivate pt = JSONObject.parseObject(param, ProductTypeActivate.class);
            productTypeActivateService.selectAllList(pt, page);
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
            List<PropertyConfigActivate> list1=propertyConfigActivateService.getOrgConfig("checkbox_config","pro_type_config");
            List<PropertyConfigActivate> list2=propertyConfigActivateService.getOrgConfig("file_config","pro_type_config");
            List<PropertyConfigActivate> list3=propertyConfigActivateService.getOrgConfig("text_config","pro_type_config");
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
    @SystemLog(description = "新增产品类别",operCode="productTypeActivate.addProductType")
    public Map<String,Object> addProductType(@RequestParam("info") String param,
                                             @RequestParam("checkboxList") String listStr1,
                                             @RequestParam("fileList") String listStr2,
                                             @RequestParam("textList") String listStr3) throws Exception{

        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductTypeActivate pt = JSONObject.parseObject(param, ProductTypeActivate.class);
            if(productTypeActivateService.checkTypeName(pt)){
                ExchangeActivateOrg org= exchangeActivateOrgService.getOrgOne(pt.getOrgCode());
                msg.put("status", false);
                msg.put("msg","机构:"+org.getOrgName()+" 下产品类别名称已存在!");
                return msg;
            }
            List<PropertyConfigActivate> checkboxList = JSONObject.parseArray(listStr1,PropertyConfigActivate.class);
            List<PropertyConfigActivate> fileList = JSONObject.parseArray(listStr2,PropertyConfigActivate.class);
            List<PropertyConfigActivate> textList = JSONObject.parseArray(listStr3,PropertyConfigActivate.class);
            List<PropertyConfigActivate> list=new ArrayList<PropertyConfigActivate>();
            list.addAll(checkboxList);
            list.addAll(fileList);
            list.addAll(textList);
            String code=randomNumberService.getRandomNumber("TYPE","activate_type_unique");
            pt.setTypeCode(code);
            int num=productTypeActivateService.addProductType(pt,list);
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
            ProductTypeActivate pt=productTypeActivateService.getProductType(id);
            List<PropertyConfigActivate> checkboxList=propertyConfigActivateService.getTypeConfigAndValue("checkbox_config","pro_type_config",pt.getTypeCode());
            List<PropertyConfigActivate> fileList=propertyConfigActivateService.getTypeConfigAndValue("file_config","pro_type_config",pt.getTypeCode());
            List<PropertyConfigActivate> textList=propertyConfigActivateService.getTypeConfigAndValue("text_config","pro_type_config",pt.getTypeCode());
            if(fileList!=null&&fileList.size()>0){
                for(PropertyConfigActivate pc:fileList){
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
    @SystemLog(description = "编辑产品类别",operCode="productTypeActivate.editProductType")
    public Map<String,Object> editProductType(@RequestParam("info") String param,
                                              @RequestParam("checkboxList") String listStr1,
                                              @RequestParam("fileList") String listStr2,
                                              @RequestParam("textList") String listStr3) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductTypeActivate pt = JSONObject.parseObject(param, ProductTypeActivate.class);
            if(productTypeActivateService.checkTypeName(pt)){
                ExchangeActivateOrg org= exchangeActivateOrgService.getOrgOne(pt.getOrgCode());
                msg.put("status", false);
                msg.put("msg","机构:"+org.getOrgName()+" 下产品类别名称已存在!");
                return msg;
            }
            List<PropertyConfigActivate> checkboxList = JSONObject.parseArray(listStr1,PropertyConfigActivate.class);
            List<PropertyConfigActivate> fileList = JSONObject.parseArray(listStr2,PropertyConfigActivate.class);
            List<PropertyConfigActivate> textList = JSONObject.parseArray(listStr3,PropertyConfigActivate.class);
            if(pt.getId()!=null){
                List<PropertyConfigActivate> list=new ArrayList<PropertyConfigActivate>();
                list.addAll(checkboxList);
                list.addAll(textList);
                int num=productTypeActivateService.editProductType(pt,list,fileList);
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
    @SystemLog(description = "删除产品类别",operCode="productTypeActivate.deleteProductType")
    public Map<String,Object> deleteProductType(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            if(productTypeActivateService.checkProduct(id)){
                msg.put("status", false);
                msg.put("msg", "该产品类别下存在产品,不能删除!");
                return msg;
            }
            //验证
            int num=productTypeActivateService.deleteProductType(id);
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
    @SystemLog(description = "批量删除产品类别",operCode="productTypeActivate.deleteProductTypeBatch")
    public Map<String,Object> deleteProductTypeBatch(@RequestParam("ids") String ids) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            productTypeActivateService.deleteProductTypeBatch(ids,msg);
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
            List<ProductTypeActivate> list=productTypeActivateService.getProductTypeList(orgCode);
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
            Map<String,String> map=productTypeActivateService.getAddProductType(typeCode);
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
    @SystemLog(description = "积分产品类别列表",operCode="productTypeActivate.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ProductTypeActivate pt = JSONObject.parseObject(param, ProductTypeActivate.class);
            List<ProductTypeActivate> list=productTypeActivateService.importDetailSelect(pt);
            productTypeActivateService.importDetail(list,response);
        }catch (Exception e){
            log.error("积分产品类别列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "积分产品类别列表异常!");
        }
        return msg;
    }
}
