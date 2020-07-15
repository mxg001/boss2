package cn.eeepay.boss.action.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.allAgent.FirewallService;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2018/9/12.
 *
 * @author zja
 * 防火墙 管理
 */
@Controller
@RequestMapping(value = "/firewall")
public class FirewallAction {

    private static final Logger log = LoggerFactory.getLogger(FirewallAction.class);

    @Resource
    private FirewallService firewallService;

    /**
     * 名单类型查询
     */
    @RequestMapping(value = "/selectSysConfig")
    @ResponseBody
    public Map<String, Object> selectSysConfig(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            List<Map> list = firewallService.selectSysConfig(params);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("名单类型查询异常!", e);
            msg.put("status", false);
            msg.put("msg", "名单类型查询异常!");
        }
        return msg;
    }

    /**
     * 商品查询
     */
    @RequestMapping(value = "/selectRecordList")
    @ResponseBody
    public Map<String, Object> selectRecordList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<Map> page) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            firewallService.selectRecordList(params, page);
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
     * 新增防火墙
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addRecord")
    @ResponseBody
    public Map<String, Object> addRecord(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            Map args = new HashMap();
            args.put("list_type",params.get("list_type"));
            args.put("ctrl_biz_type",params.get("ctrl_biz_type"));
            args.put("ctrl_target_type",params.get("ctrl_target_type"));
            args.put("ctrl_target_code",params.get("ctrl_target_code"));
            List exist = firewallService.selectRecordList(args,null);
            if(exist==null || exist.size()==0){
                params.put("op_user",getLoginUser().getRealName());
                firewallService.addRecord(params);
                msg.put("status", true);
                msg.put("msg", "新增成功!");
            }else{
                msg.put("msg", "已存在，不能重复添加!");
            }
        } catch (Exception e) {
            log.error("新增异常!", e);
            msg.put("status", false);
            msg.put("msg", "新增异常!");
        }
        return msg;
    }

    /**
     * 修改防火墙
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateRecord")
    @ResponseBody
    public Map<String, Object> updateRecord(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            Map args = new HashMap();
            args.put("list_type",params.get("list_type"));
            args.put("ctrl_biz_type",params.get("ctrl_biz_type"));
            args.put("ctrl_target_type",params.get("ctrl_target_type"));
            args.put("ctrl_target_code",params.get("ctrl_target_code"));
            String firewall_code = (String) params.get("firewall_code");
            List<Map> exist = StringUtil.isNotBlank(params.get("able_status_switch")) ? null : firewallService.selectRecordList(args,null);
            if(
                    (exist==null || exist.size()==0)
                    || (exist.size()==1 && Objects.equals(firewall_code,exist.get(0).get("firewall_code")))
            ){
                //如果当前配置找不到重复数据,则允许修改;
                //如果当前配置找到有且仅有一条数据,且为自身,则允许修改;
                params.put("op_user",getLoginUser().getRealName());
                firewallService.updateRecord(params);
                msg.put("status", true);
                msg.put("msg", "修改成功!");
            }else{
                msg.put("msg", "已存在，不能重复添加!");
            }
        } catch (Exception e) {
            log.error("修改异常!", e);
            msg.put("status", false);
            msg.put("msg", "修改异常!");
        }
        return msg;
    }

    /**
     * 删除防火墙
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteRecord")
    @ResponseBody
    public Map<String, Object> deleteGoodsGroup(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map params = JSONObject.parseObject(param, Map.class);
            params.put("op_user",getLoginUser().getRealName());
            int row = firewallService.deleteRecord(params);
            if(row==1){
                msg.put("status", true);
                msg.put("msg", "删除成功!");
            }else{
                msg.put("status", true);
                msg.put("msg", "删除失败!");
            }

        } catch (Exception e) {
            log.error("修改异常!", e);
            msg.put("status", false);
            msg.put("msg", "修改异常!");
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
}
