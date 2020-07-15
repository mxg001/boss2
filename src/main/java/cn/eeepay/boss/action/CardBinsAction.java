package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CardBins;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.CardBinsService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/23/023.
 * @author  liuks
 * 卡bin管理
 */
@Controller
@RequestMapping(value = "/cardBins")
public class CardBinsAction {

    private static final Logger log = LoggerFactory.getLogger(CardBinsAction.class);

    @Resource
    private CardBinsService cardBinsService;

    /**
     * 卡bin管理列表
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<CardBins> page) throws Exception{
        Map<String, Object> msg = new HashMap<>();
        try{
            CardBins card = JSONObject.parseObject(param, CardBins.class);
            cardBinsService.selectAllList(card, page);
            msg.put("page",page);
            msg.put("status",true);
        } catch (Exception e){
            log.error("卡bin管理列表查询失败!",e);
            msg.put("status",false);
            msg.put("msg","卡bin管理列表查询失败");
        }
        return msg;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getCardBins")
    @ResponseBody
    public Map<String, Object> getCardBins(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg = new HashMap<>();
        try{
            CardBins card =cardBinsService.getCardBins(id);
            msg.put("card",card);
            msg.put("status",true);
        } catch (Exception e){
            log.error("获取卡bin信息失败!",e);
            msg.put("status",false);
            msg.put("msg","获取卡bin信息失败!");
        }
        return msg;
    }

    /**
     * 保存卡bin
     */
    @RequestMapping(value = "/saveCardBins")
    @SystemLog(description = "保存卡bin",operCode="cardBins.saveCardBins")
    @ResponseBody
    public Map<String, Object> saveCardBins(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            CardBins card = JSONObject.parseObject(param, CardBins.class);
            int ret = 0;
            CardBins oldCard =cardBinsService.getCardBinsByCardNo(card.getCardNo(),card.getBusinessType(),card.getCardDigit());
            if(oldCard!=null){
                msg.put("status", false);
                msg.put("msg", "新增卡bin失败,卡bin"+card.getCardNo()+"已存在!");
                return msg;
            }
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            card.setCreateId(principal.getId());
            card.setCreateName(principal.getUsername());
            card.setCardNum(card.getCardNo().length());
            ret = cardBinsService.insertCardBins(card);
            if (ret > 0) {
                msg.put("status", true);
                msg.put("msg", "新增卡bin成功!");
            }
        } catch (Exception e) {
            log.error("新增卡bin信息失败!",e);
            msg.put("status", false);
            msg.put("msg", "新增卡bin信息失败!");
        }
        return msg;
    }

    /**
     * 修改卡bin
     */
    @RequestMapping(value = "/updateCardBins")
    @SystemLog(description = "修改卡bin",operCode="cardBins.updateCardBins")
    @ResponseBody
    public Map<String, Object> updateCardBins(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            CardBins card = JSONObject.parseObject(param, CardBins.class);
            int ret = 0;
            CardBins oldCard =cardBinsService.getCardBinsByCardNo(card.getCardNo(),card.getBusinessType(),card.getCardDigit());
            if(oldCard!=null){
                if(card.getId().compareTo(oldCard.getId())!=0){
                    msg.put("status", false);
                    msg.put("msg", "修改卡bin失败,卡bin"+card.getCardNo()+"已存在!");
                    return msg;
                }
            }
            card.setCardNum(card.getCardNo().length());
            ret= cardBinsService.updateCardBins(card);
            if (ret > 0) {
                msg.put("status", true);
                msg.put("msg", "修改卡bin成功!");
            }
        } catch (Exception e) {
            log.error("改卡bin信息失败!",e);
            msg.put("status", false);
            msg.put("msg", "改卡bin信息失败!");
        }
        return msg;
    }

    /**
     *删除卡bin
     */
    @RequestMapping(value = "/deleteCardBins")
    @SystemLog(description = "删除卡bin信息",operCode="cardBins.deleteCardBins")
    @ResponseBody
    public Map<String, Object> deleteCardBins(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg = new HashMap<>();
        try{
            int num =cardBinsService.deleteCardBins(id);
            if(num>0){
                msg.put("msg","删除卡bin信息成功");
                msg.put("status",true);
            }else{
                msg.put("msg","删除卡bin信息失败");
                msg.put("status",false);
            }
        } catch (Exception e){
            log.error("删除卡bin信息失败,系统异常!",e);
            msg.put("msg","删除卡bin信息失败,系统异常!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     *批量删除卡bin
     */
    @RequestMapping(value = "/deleteBatchCardBins")
    @SystemLog(description = "批量删除卡bin信息",operCode="cardBins.deleteBatchCardBins")
    @ResponseBody
    public Map<String, Object> deleteBatchCardBins(@RequestParam("ids") String ids) throws Exception{
        Map<String, Object> msg = new HashMap<>();
        try{
            int num=0;
            if(ids!=null&&!"".equals(ids)){
                String[] strs=ids.split(",");
                if(strs!=null&&strs.length>0){
                    for(int i=0;i<strs.length;i++){
                        int sta =cardBinsService.deleteCardBins(Integer.valueOf(strs[i]));
                        if(sta>0){
                            num++;
                        }
                    }
                }
                msg.put("status",true);
                msg.put("msg","批量删除成功,选中数据"+strs.length+"条,成功删除"+num+"条.");
            }else{
                msg.put("status",false);
                msg.put("msg","批量删除失败,无数据");
            }
        } catch (Exception e){
            log.error("批量删除卡bin信息失败,系统异常!",e);
            msg.put("msg","批量删除卡bin信息失败,系统异常!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 导出卡bin管理列表
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/exportInfo")
    @SystemLog(description = "导出卡bin管理列表",operCode="cardBins.exportInfo")
    @ResponseBody
    public void exportInfo(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request) throws Exception{
        CardBins card = JSONObject.parseObject(param, CardBins.class);
        List<CardBins> list= cardBinsService.exportInfo(card);
        cardBinsService.exportCardBins(list,response);
    }

    /**
     * 批量新增模板
     */
    @RequestMapping("/downloadTemplate")
    public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"importCardBinsTemplate.xlsx";
        log.info(filePath);
        ResponseUtil.download(response, filePath,"卡bin导入模板.xlsx");
        return null;
    }

    /**
     * 批量导入
     */
    @RequestMapping(value="/importCardBins")
    @SystemLog(description = "导入卡bin管理列表",operCode="cardBins.importCardBins")
    @ResponseBody
    public Map<String, Object> importCardBins(@RequestParam("file") MultipartFile file,@RequestParam("businessType") Integer businessType){
        Map<String, Object> msg = new HashMap<>();
        try {
            if (!file.isEmpty()) {
                String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if(!format.equals(".xls") && !format.equals(".xlsx")){
                    msg.put("status", false);
                    msg.put("msg", "导入文件格式错误!");
                    return msg;
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "导入文件不能为空!");
                return msg;
            }
            msg = cardBinsService.importCardBins(file,businessType);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "导入失败");
            log.error("导入失败",e);
        }
        return msg;
    }


    /**
     *开启关闭卡bin
     */
    @RequestMapping(value = "/openStateCardBins")
    @SystemLog(description = "开启关闭卡bin",operCode="cardBins.openStateCardBins")
    @ResponseBody
    public Map<String, Object> openStateCardBins(@RequestParam("id") int id,@RequestParam("state") int state) throws Exception{
        Map<String, Object> msg = new HashMap<>();
        try{
            if(state>=0&&id>0){
                int num =cardBinsService.openStateCardBins(id,state);
                if(num>0){
                    msg.put("status",true);
                    msg.put("msg","操作成功!");
                }else{
                    msg.put("status",false);
                    msg.put("msg","操作失败!");
                }
            }else{
                msg.put("status",false);
                msg.put("msg","操作失败!");
            }
        } catch (Exception e){
            log.error("开启关闭卡bin操作失败,系统异常!",e);
            msg.put("msg","开启关闭卡bin操作失败,系统异常!");
            msg.put("status",false);
        }
        return msg;
    }
}
