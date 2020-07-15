package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayNotice;
import cn.eeepay.framework.service.CreditRepayNoticeService;
import cn.eeepay.framework.util.Constants;

/**
 * Created by Administrator on 2017/10/26/026.
 * @author liuks
 * 信用卡还款公告
 */
@Controller
@RequestMapping(value = "/creditRepayNotice")
public class CreditRepayNoticeAction {
    private static final Logger log = LoggerFactory.getLogger(CreditRepayNoticeAction.class);

    @Resource
    private CreditRepayNoticeService creditRepayNoticeService;

    /**
     * 查询公告列表
     * @param param
     * @param page
     * @return
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam.do")
    @ResponseBody
    public Page<CreditRepayNotice> selectByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<CreditRepayNotice> page) throws Exception{
        try{
            CreditRepayNotice notice = JSONObject.parseObject(param, CreditRepayNotice.class);
            creditRepayNoticeService.selectAllList(notice, page);
        } catch (Exception e){
            log.error("条件查询公告失败!",e);
        }
        return page;
    }

    /**
     * 查询公告详情
     * @param id 公告id
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectById/{id}")
    @ResponseBody
    public Map<String,Object> selectById(@PathVariable("id") int id) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
        try{
            msg = creditRepayNoticeService.selectById(id);
        } catch (Exception e){
            log.error("查询公告详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询公告详情失败!");
        }
        return msg;
    }

    /**
     * 保存公告
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveNotice.do")
    @SystemLog(description = "公告新增|修改",operCode="creditRepayNotice.save")
    @ResponseBody
    public Map<String, Object> saveCreditRepayNotice(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(param);
            CreditRepayNotice notice = json.getObject("notice", CreditRepayNotice.class);
            int ret = 0;
            if(notice.getId()<=0){
                notice.setNoticeNo(randomNoticeNo(8));
                ret = creditRepayNoticeService.insertCreditRepayNotice(notice);
                if (ret > 0) {
                    msg.put("status", true);
                    msg.put("msg", "新增公告成功!");
                }
            }else{
                ret= creditRepayNoticeService.updateCreditRepayNotice(notice);
                if (ret > 0) {
                    msg.put("status", true);
                    msg.put("msg", "修改公告成功!");
                }
            }

        } catch (Exception e) {
            log.error("添加 || 修改公告失败!", e);
            msg.put("status", false);
            msg.put("msg", "添加 || 修改公告失败!");
        }
        return msg;
    }

    /**
     * 随机产生编码
     * @return
     */
    private String randomNoticeNo(int length){
        String Str="CRN";
        //随机字符串的随机字符库
        //String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String KeyString = "0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        String reStr=Str+sb.toString();
        return reStr;
    }
    /**
     * 下发公告
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/issueNotice.do")
    @SystemLog(description = "公告下发",operCode="creditRepayNotice.issue")
    @ResponseBody
    public Map<String, Object> issueCreditRepayNotice(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(param);
            CreditRepayNotice notice = json.getObject("notice", CreditRepayNotice.class);
            int ret = 0;
            if(notice.getId()>0){
                ret = creditRepayNoticeService.issueCreditRepayNotice(notice);
                if (ret > 0) {
                    msg.put("status", true);
                    msg.put("msg", "下发公告成功");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "下发公告失败!");
            }

        } catch (Exception e) {
            log.error("下发公告失败!", e);
            msg.put("status", false);
            msg.put("msg", "下发公告失败!");
        }
        return msg;
    }

    /**
     * 逻辑删除公告
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteNotice/{id}")
    @SystemLog(description = "公告删除",operCode="creditRepayNotice.delete")
    @ResponseBody
    public Map<String, Object> deleteCreditRepayNotice(@PathVariable("id") int id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        if(id>0){
            CreditRepayNotice notice = new CreditRepayNotice();
            notice.setId(id);
            try {
                int ret = creditRepayNoticeService.deleteCreditRepayNotice(notice);
                if (ret > 0) {
                    msg.put("status", true);
                    msg.put("msg", "删除公告成功");
                }
            }catch (Exception e) {
                log.error("删除公告失败!", e);
                msg.put("status", false);
                msg.put("msg", "删除公告失败!");
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "删除公告失败!");
        }
        return msg;
    }

    /**
     * 收回公告
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/reclaimNotice/{id}")
    @SystemLog(description = "公告收回",operCode="creditRepayNotice.reclaim")
    @ResponseBody
    public Map<String, Object> reclaimCreditRepayNotice(@PathVariable("id") int id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        if(id>0){
            CreditRepayNotice notice = new CreditRepayNotice();
            notice.setId(id);
            try {
                int ret = creditRepayNoticeService.reclaimCreditRepayNotice(notice);
                if (ret > 0) {
                    msg.put("status", true);
                    msg.put("msg", "收回公告成功");
                }
            }catch (Exception e) {
                log.error("收回公告失败!", e);
                msg.put("status", false);
                msg.put("msg", "收回公告失败!");
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "收回公告失败!");
        }
        return msg;
    }
}
