package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateProduct;
import cn.eeepay.framework.model.exchangeActivate.ProductTypeActivate;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateOemService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateProductService;
import cn.eeepay.framework.service.exchangeActivate.ProductTypeActivateService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 产品实体
 */
@Controller
@RequestMapping(value = "/exchangeActivateProduct")
public class ExchangeActivateProductAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateProductAction.class);

    @Resource
    private ExchangeActivateProductService exchangeActivateProductService;

    @Resource
    private ProductTypeActivateService productTypeActivateService;

    @Resource
    private ExchangeActivateOemService exchangeActivateOemService;
    /**
     * 查询机构列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateProduct> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateProduct pro = JSONObject.parseObject(param, ExchangeActivateProduct.class);
            exchangeActivateProductService.selectAllList(pro, page);
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
     * 新增产品
     */
    @RequestMapping(value = "/addExchangeProduct")
    @ResponseBody
    @SystemLog(description = "新增产品",operCode="exchangeActivateProduct.addExchangeProduct")
    public Map<String,Object> addExchangeProduct(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateProduct pro = JSONObject.parseObject(param, ExchangeActivateProduct.class);
            if(exchangeActivateProductService.checkTypeName(pro)){
                ProductTypeActivate type=productTypeActivateService.getProductOne(pro.getTypeCode());
                msg.put("status", false);
                msg.put("msg","产品类别:"+type.getTypeName()+" 下产品名称已存在!");
                return msg;
            }
            int num=exchangeActivateProductService.addExchangeProduct(pro);
            if(num>0){
                msg.put("msg","添加成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "新增产品失败!");
        } catch (Exception e){
            log.error("新增产品失败!",e);
            msg.put("status", false);
            msg.put("msg", "新增产品失败!");
        }
        return msg;
    }

    /**
     * 产品详情
     */
    @RequestMapping(value = "/getExchangeProduct")
    @ResponseBody
    public Map<String,Object> getExchangeProduct(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateProduct pro =exchangeActivateProductService.getExchangeProduct(id);
            msg.put("pro",pro);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取产品详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取产品详情失败!");
        }
        return msg;
    }
    /**
     * 产品修改
     */
    @RequestMapping(value = "/updateExchangeProduct")
    @ResponseBody
    @SystemLog(description = "产品修改",operCode="exchangeActivateProduct.updateExchangeProduct")
    public Map<String,Object> updateExchangeProduct(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateProduct pro = JSONObject.parseObject(param, ExchangeActivateProduct.class);
            if(exchangeActivateProductService.checkTypeName(pro)){
                ProductTypeActivate type=productTypeActivateService.getProductOne(pro.getTypeCode());
                msg.put("status", false);
                msg.put("msg","产品类别:"+type.getTypeName()+" 下产品名称已存在!");
                return msg;
            }
            int num=exchangeActivateProductService.updateExchangeProduct(pro);
            if(num>0){
                msg.put("msg","修改成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "产品修改失败!");
        } catch (Exception e){
            log.error("产品修改失败!",e);
            msg.put("status", false);
            msg.put("msg", "产品修改失败!");
        }
        return msg;
    }
    /**
     * 产品删除
     */
    @RequestMapping(value = "/deleteExchangeProduct")
    @ResponseBody
    @SystemLog(description = "产品删除",operCode="exchangeActivateProduct.deleteExchangeProduct")
    public Map<String,Object> deleteExchangeProduct(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            //验证是否存在上架商品
            if(exchangeActivateOemService.checkProductOemShelve(id)){
                msg.put("msg","该产品已上架,不能删除!");
                msg.put("status", false);
                return msg;
            }
            int num =exchangeActivateProductService.deleteExchangeProduct(id);
            if(num>0){
                msg.put("msg","删除成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "产品删除失败!");
        } catch (Exception e){
            log.error("产品删除失败!",e);
            msg.put("status", false);
            msg.put("msg", "产品删除失败!");
        }
        return msg;
    }

    /**
     * 获取产品列表
     */
    @RequestMapping(value = "/getProductList")
    @ResponseBody
    public Map<String,Object> getProductTypeList(@RequestParam("name") String name,@RequestParam("typeCode") String typeCode) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<ExchangeActivateProduct> list=exchangeActivateProductService.getProductList(name,typeCode);
            msg.put("status", true);
            msg.put("list",list);
        } catch (Exception e){
            log.error("获取产品列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取产品列表失败!");
        }
        return msg;
    }

    /**
     * 获取产品列表
     * 通过名称，或者ID
     */
    @RequestMapping(value = "/productListSelect")
    @ResponseBody
    public Map<String,Object> productListSelect(@RequestParam("val") String val) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<ExchangeActivateProduct> list=exchangeActivateProductService.productListSelect(val);
            msg.put("status", true);
            msg.put("list",list);
        } catch (Exception e){
            log.error("获取产品列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取产品列表失败!");
        }
        return msg;
    }

    /**
     * 导出积分产品列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出积分产品列表",operCode="exchangeActivateProduct.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ExchangeActivateProduct pro = JSONObject.parseObject(param, ExchangeActivateProduct.class);
            List<ExchangeActivateProduct> list=exchangeActivateProductService.importDetailSelect(pro);
            exchangeActivateProductService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出积分产品列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出积分产品列表异常!");
        }
        return msg;
    }
}
