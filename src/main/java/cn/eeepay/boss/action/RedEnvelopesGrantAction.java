package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.RedEnvelopesGrantService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.Md5;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Administrator on 2018/1/18/018.
 * 红包发放查询 Action
 * @author liuks
 */
@Controller
@RequestMapping(value = "/redEnvelopesGrant")
public class RedEnvelopesGrantAction {
    private static final Logger log = LoggerFactory.getLogger(RedEnvelopesGrantAction.class);

    @Resource
    private RedEnvelopesGrantService redEnvelopesGrantService;

    /**
     * 红包发放查询列表
     */
    @RequestMapping(value = "/selectByParam")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<RedEnvelopesGrant> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RedEnvelopesGrant reg = JSONObject.parseObject(param, RedEnvelopesGrant.class);
            reg.setSumState(0);
            redEnvelopesGrantService.selectAllByParam(reg, page);
            reg.setSumState(1);
            RedEnvelopesGrant sunOrder= redEnvelopesGrantService.sumCount(reg);
            msg.put("page",page);
            msg.put("sunOrder",sunOrder);
            msg.put("status",true);
        } catch (Exception e){
            log.error("红包发放查询列表失败!",e);
            msg.put("msg","红包发放查询列表失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 红包评论查询
     */
    @RequestMapping(value = "/selectRedEnvelopesGrantDiscuss")
    @ResponseBody
    public Map<String, Object> selectRedEnvelopesGrantDiscuss(@RequestParam("id") Long id, @ModelAttribute("page")
            Page<RedEnvelopesGrantDiscuss> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            redEnvelopesGrantService.getRedEnvelopesGrantDiscuss(id, page);
            msg.put("page",page);
            msg.put("status",true);
        } catch (Exception e){
            log.error("红包评论查询失败!",e);
            msg.put("msg","红包评论查询失败!");
            msg.put("status",false);
        }
        return msg;
    }
    /**
     * 红包评论删除
     */
    @RequestMapping(value = "/deleteRedEnvelopesGrantDiscuss")
    @ResponseBody
    @SystemLog(description = "红包评论删除",operCode="redEnvelopesGrant.deleteRedEnvelopesGrantDiscuss")
    public Map<String, Object> deleteRedEnvelopesGrantDiscuss(@RequestBody RedEnvelopesGrantDiscuss baseInfo) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=redEnvelopesGrantService.deleteRedEnvelopesGrantDiscuss(baseInfo);
            if(num>0){
                msg.put("status",true);
                msg.put("msg","红包评论删除成功!");
                return msg;
            }
        } catch (Exception e){
            log.error("红包评论删除失败!",e);
            msg.put("msg","红包评论删除失败!");
            msg.put("status",false);
            return msg;
        }
        return msg;
    }
    /**
     * 红包图片屏蔽
     */
    @RequestMapping(value = "/updateRedEnvelopesGrantImage/{id}/{state}")
    @ResponseBody
    @SystemLog(description = "红包图片屏蔽/开启",operCode="redEnvelopesGrant.updateRedEnvelopesGrantImage")
    public Map<String, Object> updateRedEnvelopesGrantImage(@PathVariable("id") Long id,@PathVariable("state")Integer state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RedEnvelopesGrantImage img=new RedEnvelopesGrantImage();
            img.setId(id);
            img.setStatus(state+"");
            int num=redEnvelopesGrantService.updateRedEnvelopesGrantImage(img);
            if(num>0){
                msg.put("status",true);
                msg.put("msg","图片状态保存成功!");
                return msg;
            }
        } catch (Exception e){
            log.error("图片状态保存失败!",e);
            msg.put("msg","图片状态保存失败!");
            msg.put("status",false);
            return msg;
        }
        return msg;
    }
    /**
     * 红包图片屏蔽所有
     */
    @RequestMapping(value = "/updateRedEnvelopesGrantImageAll/{id}")
    @ResponseBody
    @SystemLog(description = "红包图片屏蔽所有",operCode="redEnvelopesGrant.updateRedEnvelopesGrantImageAll")
    public Map<String, Object> updateRedEnvelopesGrantImageAll(@PathVariable("id") Long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RedEnvelopesGrantImage img=new RedEnvelopesGrantImage();
            img.setId(id);
            img.setStatus("1");
            int num=redEnvelopesGrantService.updateRedEnvelopesGrantImageAll(img);
        } catch (Exception e){
            log.error("屏蔽所有红包图片失败!",e);
            msg.put("msg","屏蔽所有红包图片失败!");
            msg.put("status",false);
            return msg;
        }
        msg.put("status",true);
        msg.put("msg","屏蔽所有红包图片成功!");
        return msg;
    }
    /**
     * 屏蔽红包说明状态
     */
    @RequestMapping(value = "/updateRemark/{id}")
    @ResponseBody
    @SystemLog(description = "红包说明屏蔽",operCode="redEnvelopesGrant.updateRemark")
    public Map<String, Object> updateRemark(@PathVariable("id") Long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=redEnvelopesGrantService.updateRemark(id);
            if(num>0){
                msg.put("status",true);
                msg.put("msg","屏蔽红包说明成功!");
            }
        } catch (Exception e){
            log.error("屏蔽红包说明失败!",e);
            msg.put("msg","屏蔽红包说明失败!");
            msg.put("status",false);
            return msg;
        }
        return msg;
    }
    /**
     * 风控关闭红包
     */
    @RequestMapping(value = "/updateStatusRisk")
    @ResponseBody
    @SystemLog(description = "风控关闭红包",operCode="redEnvelopesGrant.updateStatusRisk")
    public Map<String, Object> updateStatusRisk(@RequestBody RedOrdersOption baseInfo) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num = redEnvelopesGrantService.updateStatusRisk(baseInfo);
            
            if(num>0){
            	//风控关闭调用接口   
            	//http://192.168.1.203:2111/superbank-mobile/order/closeRedOrder
            	//{"redOrderId":"1","sign":"wefe12312212212"}
	            if (StringUtils.isNotBlank(baseInfo.getStatus()) && baseInfo.getStatus().equals("1") 
	            		&& baseInfo.getRedOrderId() !=null) {
	            	String url = Constants.CLOSE_RED;
	            	String sign = Md5.md5Str(baseInfo.getRedOrderId() + "&" + Constants.SUPER_BANK_SECRET);
	        		Map<String,String> map = new HashMap<String,String>();
	        		map.put("redOrderId", baseInfo.getRedOrderId().toString());
	        		map.put("sign", sign);
	        		
	        		try {
	        			String rsStr = ClientInterface.httpPost2(url,map);
	        			if (!rsStr.contains("rsStr")) {
							log.error("风控红包关闭失败------------->redOrderId"+baseInfo.getRedOrderId());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
            	
                msg.put("status",true);
                msg.put("msg","操作成功!");
                return msg;
            }
        } catch (Exception e){
            log.error("风控关闭红包失败!",e);
            msg.put("msg","风控关闭红包失败!");
            msg.put("status",false);
        }
        return msg;
    }
    /**
     * 红包操作记录查询
     */
    @RequestMapping(value = "/selectRedEnvelopesGrantOption")
    @ResponseBody
    public Map<String, Object> selectRedEnvelopesGrantOption(@RequestParam("id") Long id, @ModelAttribute("page")
            Page<RedEnvelopesGrantOption> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            redEnvelopesGrantService.selectRedEnvelopesGrantOption(id, page);
            msg.put("page",page);
            msg.put("status",true);
        } catch (Exception e){
            log.error("红包操作记录查询!",e);
            msg.put("msg","红包操作记录查询!");
            msg.put("status",false);
        }
        return msg;
    }
    /**
     * 红包发放详情
     */
    @RequestMapping(value = "/selectById/{id}")
    @ResponseBody
    public Map<String,Object> selectById(@PathVariable("id") Long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RedEnvelopesGrant regDetail = redEnvelopesGrantService.selectRedEnvelopesGrantById(id);
            List<RedEnvelopesGrantImage> imageList=redEnvelopesGrantService.getImages(id);
            if(imageList!=null&&imageList.size()>0){
                for(RedEnvelopesGrantImage image:imageList){
                    image.setImgUrl(CommonUtil.getImgUrl(image.getImgUrl()));
                }
            }
            msg.put("regDetail",regDetail);//红包基本信息
            msg.put("imageList",imageList);//红包图片
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询红包发放详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询红包发放详情失败!");
        }
        return msg;
    }

    /**
     * 红包发放查询列表导出
     */
    @RequestMapping(value="/exportInfo")
    @ResponseBody
    public void exportInfo(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request) throws Exception{
        try {
            RedEnvelopesGrant reg = JSONObject.parseObject(param, RedEnvelopesGrant.class);
            reg.setSumState(0);
            List<RedEnvelopesGrant> list=redEnvelopesGrantService.exportInfo(reg);
            redEnvelopesGrantService.exportRedEnvelopesGrant(list,response);
        } catch (Exception e){
            log.error("红包发放查询列表导出异常", e);
        }
    }
}
