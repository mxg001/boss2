package cn.eeepay.boss.action.allAgent;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.AwardParam;
import cn.eeepay.framework.model.allAgent.AwardParamLadder;
import cn.eeepay.framework.service.allAgent.AwardParamService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/6/006.
 * @author  liuks
 * oem 奖项参数设置action
 */
@Controller
@RequestMapping(value = "/awardParam")
public class AwardParamAction {

    private static final Logger log = LoggerFactory.getLogger(AwardParamAction.class);

    @Resource
    private AwardParamService awardParamService;

    /**
     * 查询奖项参数设置
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<AwardParam> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardParam oem = JSONObject.parseObject(param, AwardParam.class);
            awardParamService.selectAllList(oem, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询奖项参数设置异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询奖项参数设置异常!");
        }
        return msg;
    }

    /**
     * 奖项参数设置新增
     */
    @RequestMapping(value = "/addAwardParam")
    @ResponseBody
    @SystemLog(description = "奖项参数设置新增",operCode="awardParam.addAwardParam")
    public Map<String,Object> addAwardParam(@RequestParam("info") String param,
                                            @RequestParam("tradeList") String listStr1,
                                            @RequestParam("crownList") String listStr2,
                                            @RequestParam("vipList") String listStr3) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardParam oem = JSONObject.parseObject(param, AwardParam.class);
            List<AwardParamLadder> tradeList = JSONObject.parseArray(listStr1,AwardParamLadder.class);
            List<AwardParamLadder> crownList = JSONObject.parseArray(listStr2,AwardParamLadder.class);
            List<AwardParamLadder> vipList = JSONObject.parseArray(listStr3,AwardParamLadder.class);
            if(awardParamService.checkAwardParam(oem)){
                msg.put("status", false);
                msg.put("msg", "该所属品牌商已存在!");
                return msg;
            }
            int num=awardParamService.addAwardParam(oem,tradeList,crownList,vipList);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "新增成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "新增失败!");
            }
        } catch (Exception e){
            log.error("奖项参数设置新增异常!",e);
            msg.put("status", false);
            msg.put("msg", "奖项参数设置新增异常!");
        }
        return msg;
    }

    /**
     * 查询奖项参数设置详情
     */
    @RequestMapping(value = "/getAwardParam")
    @ResponseBody
    public Map<String,Object> getAwardParam(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardParam oem =awardParamService.getAwardParam(id);
            msg.put("oem",oem);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询奖项参数设置详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询奖项参数设置详情异常!");
        }
        return msg;
    }

    /**
     * 查询奖项参数设置详情
     */
    @RequestMapping(value = "/getAwardParamOem")
    @ResponseBody
    public Map<String,Object> getAwardParamOem(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardParam oem =awardParamService.getAwardParamOem(id);
            if(oem.getAgentBgi()!=null&&!"".equals(oem.getAgentBgi())){
                oem.setAgentBgiUrl(CommonUtil.getImgUrlAgent(oem.getAgentBgi()));
            }
            if(oem.getMerBgi()!=null&&!"".equals(oem.getMerBgi())){
                oem.setMerBgiUrl(CommonUtil.getImgUrlAgent(oem.getMerBgi()));
            }
            if(oem.getOwnerImgs()!=null&&!"".equals(oem.getOwnerImgs())){
                String ownerImgs[]=oem.getOwnerImgs().split(",");
                Map<String,String> ownerImgsMap=new HashMap<String,String>();
                for(int i=0;i<ownerImgs.length;i++){
                    ownerImgsMap.put("ownerImg"+(i+1),CommonUtil.getImgUrlAgent(ownerImgs[i]));
                }
                oem.setOwnerImgsMap(ownerImgsMap);
            }
            if(oem.getOwnerImg()!=null&&!"".equals(oem.getOwnerImg())){
                oem.setOwnerImgUrl(CommonUtil.getImgUrlAgent(oem.getOwnerImg()));
            }
            if(oem.getMerImgs()!=null&&!"".equals(oem.getMerImgs())){
                String merImgs[]=oem.getMerImgs().split(",");
                Map<String,String> merImgsMap=new HashMap<String,String>();
                for(int i=0;i<merImgs.length;i++){
                    merImgsMap.put("merImg"+(i+1),CommonUtil.getImgUrlAgent(merImgs[i]));
                }
                oem.setMerImgsMap(merImgsMap);
            }
            if(oem.getMerImg()!=null&&!"".equals(oem.getMerImg())){
                oem.setMerImgUrl(CommonUtil.getImgUrlAgent(oem.getMerImg()));
            }
            if(oem.getLeaderboardBgi()!=null&&!"".equals(oem.getLeaderboardBgi())){
                String leaImgs[]=oem.getLeaderboardBgi().split(",");
                Map<String,String> leaImgsMap=new HashMap<String,String>();
                for(int i=0;i<leaImgs.length;i++){
                    leaImgsMap.put("leaImgs"+(i+1),CommonUtil.getImgUrlAgent(leaImgs[i]));
                }
                oem.setLeaImgsMap(leaImgsMap);
            }
            msg.put("oem",oem);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询奖项参数设置详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询奖项参数设置详情异常!");
        }
        return msg;
    }

    /**
     * 奖项参数设置修改
     */
    @RequestMapping(value = "/updateAwardParam")
    @ResponseBody
    @SystemLog(description = "奖项参数设置修改",operCode="awardParam.updateAwardParam")
    public Map<String,Object> updateAwardParam(@RequestParam("info") String param,
                                               @RequestParam("tradeList") String listStr1,
                                               @RequestParam("crownList") String listStr2,
                                               @RequestParam("vipList") String listStr3) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardParam oem = JSONObject.parseObject(param, AwardParam.class);
            List<AwardParamLadder> tradeList = JSONObject.parseArray(listStr1,AwardParamLadder.class);
            List<AwardParamLadder> crownList = JSONObject.parseArray(listStr2,AwardParamLadder.class);
            List<AwardParamLadder> vipList = JSONObject.parseArray(listStr3,AwardParamLadder.class);
            if(awardParamService.checkAwardParam(oem)){
                msg.put("status", false);
                msg.put("msg", "该所属品牌商已存在!");
                return msg;
            }
            int num=awardParamService.updateAwardParam(oem,tradeList,crownList,vipList);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "修改成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "修改失败!");
            }
        } catch (Exception e){
            log.error("奖项参数设置修改异常!",e);
            msg.put("status", false);
            msg.put("msg", "奖项参数设置修改异常!");
        }
        return msg;
    }

    /**
     * oem配置
     */
    @RequestMapping(value = "/updateAwardParamOem")
    @ResponseBody
    @SystemLog(description = "oem配置",operCode="awardParam.updateAwardParamOem")
    public Map<String,Object> updateAwardParamOem(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardParam oem = JSONObject.parseObject(param, AwardParam.class);
            int num=awardParamService.updateAwardParamOem(oem);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "oem配置成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "oem配置失败!");
            }
        } catch (Exception e){
            log.error("oem配置异常!",e);
            msg.put("status", false);
            msg.put("msg", "oem配置异常!");
        }
        return msg;
    }

    /**
     * 查询oemList
     */
    @RequestMapping(value = "/getOemList")
    @ResponseBody
    public Map<String,Object> getOemList() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<AwardParam> list=awardParamService.getOemList();
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询奖项参数设置列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询奖项参数设置列表异常!");
        }
        return msg;
    }

    /**
     * 图片删除
     */
    @RequestMapping(value = "/deleteOwnerImgs")
    @ResponseBody
    @SystemLog(description = "图片删除",operCode="awardParam.updateAwardParamOem")
    public Map<String,Object> deleteOwnerImgs(@RequestParam("id") int id,@RequestParam("ownerImgs") String ownerImgs) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num =awardParamService.deleteOwnerImgs(id,ownerImgs);
            if(num>0){
                msg.put("msg","图片删除成功!");
                msg.put("status", true);
            }else{
                msg.put("msg","图片删除失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("图片删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "图片删除异常!");
        }
        return msg;
    }

    /**
     * 图片删除
     */
    @RequestMapping(value = "/deleteMerImgs")
    @ResponseBody
    @SystemLog(description = "图片删除",operCode="awardParam.updateAwardParamOem")
    public Map<String,Object> deleteMerImgs(@RequestParam("id") int id,@RequestParam("merImgs") String merImgs) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num =awardParamService.deleteMerImgs(id,merImgs);
            if(num>0){
                msg.put("msg","图片删除成功!");
                msg.put("status", true);
            }else{
                msg.put("msg","图片删除失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("图片删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "图片删除异常!");
        }
        return msg;
    }

    /**
     * 图片删除
     */
    @RequestMapping(value = "/deleteLeaImgs")
    @ResponseBody
    @SystemLog(description = "图片删除",operCode="awardParam.updateAwardParamOem")
    public Map<String,Object> deleteLeaImgs(@RequestParam("id") int id,@RequestParam("leaderboardBgi") String leaderboardBgi) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num =awardParamService.deleteLeaImgs(id,leaderboardBgi);
            if(num>0){
                msg.put("msg","图片删除成功!");
                msg.put("status", true);
            }else{
                msg.put("msg","图片删除失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("图片删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "图片删除异常!");
        }
        return msg;
    }
}
