package cn.eeepay.boss.action.allAgent;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.allAgent.GoodAllAgent;
import cn.eeepay.framework.model.allAgent.GoodsPrice;
import cn.eeepay.framework.service.allAgent.GoodAllAgentService;
import cn.eeepay.framework.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Administrator on 2018/7/13/013.
 *
 * @author liuks
 * 商品 管理
 */
@Controller
@RequestMapping(value = "/goodAllAgent")
public class GoodAllAgentAction {

    private static final Logger log = LoggerFactory.getLogger(GoodAllAgentAction.class);

    @Resource
    private GoodAllAgentService goodAllAgentService;

    /**
     * 商品查询
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<GoodAllAgent> page) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            GoodAllAgent good = JSONObject.parseObject(param, GoodAllAgent.class);
            goodAllAgentService.selectAllList(good, page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("商品查询异常!", e);
            msg.put("status", false);
            msg.put("msg", "商品查询异常!");
        }
        return msg;
    }

    /**
     * 商品新增
     */
    @RequestMapping(value = "/addGood")
    @ResponseBody
    @SystemLog(description = "商品新增", operCode = "goodAllAgent.addGood")
    public Map<String, Object> addGood(@RequestParam("info") String info,
                                       @RequestParam("formatList") String formatList,
                                       @RequestParam("desc") String desc) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            GoodAllAgent good = JSONObject.parseObject(info, GoodAllAgent.class);
            List<GoodsPrice> pricesList=JSONObject.parseArray(formatList,GoodsPrice.class);
            good.setGoodsDesc(desc);
            if(good.getShipWay()==2){
                good.setShipper(3);
            }else{
                good.setShipper(0);
            }
            int num = goodAllAgentService.addGood(good,pricesList);
            if (num > 0) {
                msg.put("msg", "新增成功!");
                msg.put("status", true);
            } else {
                msg.put("msg", "新增失败!");
                msg.put("status", false);
            }
        } catch (Exception e) {
            log.error("商品新增异常!", e);
            msg.put("status", false);
            msg.put("msg", "商品新增异常!");
        }
        return msg;
    }


    /**
     * 商品详情
     */
    @RequestMapping(value = "/getGood")
    @ResponseBody
    public Map<String, Object> getGood(@RequestParam("id") int id) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            GoodAllAgent good = goodAllAgentService.getGood(id);
            List<GoodsPrice> goodsPrice=goodAllAgentService.getGoodsPrice(good.getGoodsCode());
            msg.put("good", good);
            msg.put("goodsPrice", goodsPrice);
            String detailImages = good.getDetailImgs();
            if(StringUtils.isNotBlank(detailImages)){
                String[] imgs = detailImages.split(",");
                List list = new ArrayList();
                Map imgsMap = new LinkedHashMap();
                for (String img: imgs) {
                    String imgUrl = CommonUtil.getImgUrlAgent(img);
                    list.add(imgUrl);
                    imgsMap.put(img, imgUrl);
                }
                if(list.size()>0){
                    msg.put("imgsUrl", list);
                    msg.put("imgsMap", imgsMap);
                }
            }
            msg.put("status", true);
        } catch (Exception e) {
            log.error("获取商品详情异常!", e);
            msg.put("status", false);
            msg.put("msg", "获取商品详情异常!");
        }
        return msg;
    }


    @RequestMapping(value = "/getGoodsCode")
    @ResponseBody
    public Map<String, Object> getGoodsCode(@RequestParam("goodsCode") String goodsCode) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            GoodAllAgent good = goodAllAgentService.getGoodsCode(goodsCode);
            msg.put("good", good);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("获取商品详情异常!", e);
            msg.put("status", false);
            msg.put("msg", "获取商品详情异常!");
        }
        return msg;
    }

    /**
     * 商品修改
     */
    @RequestMapping(value = "/saveGood")
    @ResponseBody
    @SystemLog(description = "商品修改", operCode = "goodAllAgent.saveGood")
    public Map<String, Object> saveGood(@RequestParam("info") String info,
                                        @RequestParam("formatList") String formatList,
                                        @RequestParam("deleteinfo") String deleteinfo,
                                        @RequestParam("desc") String desc) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            GoodAllAgent good = JSONObject.parseObject(info, GoodAllAgent.class);
            List<GoodsPrice> pricesList=JSONObject.parseArray(formatList,GoodsPrice.class);
            List<GoodsPrice> deleteList=JSONObject.parseArray(deleteinfo,GoodsPrice.class);
            good.setGoodsDesc(desc);
            if(good.getShipWay()==2){
                good.setShipper(3);
            }else{
                good.setShipper(0);
            }
            int num = goodAllAgentService.saveGood(good,pricesList,deleteList);
            if (num > 0) {
                msg.put("msg", "修改成功!");
                msg.put("status", true);
            } else {
                msg.put("msg", "修改失败!");
                msg.put("status", false);
            }
        } catch (Exception e) {
            log.error("商品修改异常!", e);
            msg.put("status", false);
            msg.put("msg", "商品修改异常!");
        }
        return msg;
    }

    /**
     * 商品上/下架
     */
    @RequestMapping(value = "/updateGood")
    @ResponseBody
    @SystemLog(description = "商品上/下架", operCode = "goodAllAgent.updateGood")
    public Map<String, Object> updateGood(@RequestParam("id") int id, @RequestParam("status") int status) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            int num = goodAllAgentService.updateGood(id, status);
            if (num > 0) {
                msg.put("msg", "操作成功!");
                msg.put("status", true);
            } else {
                msg.put("msg", "操作失败!");
                msg.put("status", false);
            }
        } catch (Exception e) {
            log.error("商品上/下架异常!", e);
            msg.put("status", false);
            msg.put("msg", "商品上/下架异常!");
        }
        return msg;
    }

    /**
     * 商品图片删除
     */
    @RequestMapping(value = "/deleteGoodImg")
    @ResponseBody
    @SystemLog(description = "商品图片删除", operCode = "goodAllAgent.saveGood")
    public Map<String, Object> deleteGoodImg(@RequestParam("id") int id, @RequestParam("status") int status) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            int num = goodAllAgentService.deleteGoodImg(id, status);
            if (num > 0) {
                msg.put("msg", "商品图片删除成功!");
                msg.put("status", true);
            } else {
                msg.put("msg", "商品图片删除失败!");
                msg.put("status", false);
            }
        } catch (Exception e) {
            log.error("商品图片删除异常!", e);
            msg.put("status", false);
            msg.put("msg", "商品图片删除异常!");
        }
        return msg;
    }

    /**
     * 商品查询
     */
    @RequestMapping(value = "/goodGroupQuery")
    @ResponseBody
    public Map<String, Object> goodGroupQuery(@RequestParam("info") String param, @ModelAttribute("page")
            Page<Map> page) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            goodAllAgentService.goodsGroupQuery(params, page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("商品查询异常!", e);
            msg.put("status", false);
            msg.put("msg", "商品查询异常!");
        }
        return msg;
    }

    /**
     * 新增商品分类
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addGoodsGroup")
    @ResponseBody
    public Map<String, Object> addGoodsGroup(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            params.put("op_user",getLoginUser().getRealName());
            goodAllAgentService.addGoodsGroup(params);
            msg.put("status", true);
            msg.put("msg", "新增商品分类成功!");
        } catch (Exception e) {
            log.error("新增商品分类异常!", e);
            msg.put("status", false);
            msg.put("msg", "新增商品分类异常!");
        }
        return msg;
    }

    /**
     * 修改商品分类
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateGoodsGroup")
    @ResponseBody
    public Map<String, Object> updateGoodsGroup(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            params.put("op_user",getLoginUser().getRealName());
            goodAllAgentService.updateGoodsGroup(params);
            msg.put("status", true);
            msg.put("msg", "修改商品分类成功!");
        } catch (Exception e) {
            log.error("修改商品分类异常!", e);
            msg.put("status", false);
            msg.put("msg", "修改商品分类异常!");
        }
        return msg;
    }
    /**
     * 修改商品名单类型
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateListType")
    @ResponseBody
    @SystemLog(description = "修改商品名单类型", operCode = "goodAllAgent.updateListType")
    public Map<String, Object> updateListType(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            params.put("op_user",getLoginUser().getRealName());
            goodAllAgentService.updateListType(params);
            msg.put("status", true);
            msg.put("msg", "修改商品名单类型成功!");
        } catch (Exception e) {
            log.error("修改商品名单类型异常!", e);
            msg.put("status", false);
            msg.put("msg", "修改商品名单类型异常!");
        }
        return msg;
    }

    /**
     * 删除商品分类
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteGoodsGroup")
    @ResponseBody
    public Map<String, Object> deleteGoodsGroup(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            params.put("op_user",getLoginUser().getRealName());
            int row = goodAllAgentService.deleteGoodsGroup(params);
            if(row==1){
                msg.put("status", true);
                msg.put("msg", "删除商品分类成功!");
            }else if(row==-9){
                msg.put("status", true);
                msg.put("msg", "该商品分类下存在商品，请移除后再删除!");
            }else{
                msg.put("status", true);
                msg.put("msg", "删除商品分类失败!");
            }

        } catch (Exception e) {
            log.error("修改商品分类异常!", e);
            msg.put("status", false);
            msg.put("msg", "修改商品分类异常!");
        }
        return msg;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public UserLoginInfo getLoginUser(){
        return (UserLoginInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 名单类型查询
     */
    @RequestMapping(value = "/goodsGroupQuery")
    @ResponseBody
    public Map<String, Object> goodsGroupQuery(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = new HashMap<String, Object>();
            params.put("brandCode",param);
            List<Map> list = goodAllAgentService.goodsGroupQuery(params,null);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("名单类型查询异常!", e);
            msg.put("status", false);
            msg.put("msg", "名单类型查询异常!");
        }
        return msg;
    }
}
