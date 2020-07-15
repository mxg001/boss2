package cn.eeepay.boss.action.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.risk.DealTemplate;
import cn.eeepay.framework.service.risk.DealTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风控黑名单回复模板
 * @author MXG
 * create 2018/12/20
 */
@RequestMapping("dealTemplate")
@Controller
public class DealTemplateAction {

    @Resource
    private DealTemplateService templateService;

    private Logger log = LoggerFactory.getLogger(DealTemplateAction.class);

    /**
     * 分页查询列表
     * @param templateNo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectTemplateList")
    @ResponseBody
    public Map<String, Object> selectTemplateList(@RequestParam String templateNo,
                                                  @RequestParam(defaultValue = "1") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize){
        Map<String, Object> result = new HashMap<>();
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Page<DealTemplate> page = new Page<>(pageNo, pageSize);
            templateService.selectTemplateListWithPage(page, templateNo);
            result.put("status", true);
            result.put("page", page);
        } catch (Exception e) {
            result.put("status", false);
            result.put("msg", "查询失败");
            log.error("黑名单处理模板列表查询失败", e);
        }
        return result;
    }

    /**
     * 获取所有模板
     * @return
     */
    @RequestMapping("/selectAll")
    @ResponseBody
    public Map<String, Object> selectAll(){
        Map<String, Object> result = new HashMap<>();
        List<DealTemplate> templateList = templateService.selectAll();
        result.put("templateList", templateList);
        return result;
    }
    /**
     * 根据id查找
     * @param id
     * @return
     */
    @RequestMapping("/selectById")
    @ResponseBody
    public Map<String, Object> selectById(String id){
        Map<String, Object> result = new HashMap<>();
        try {
            DealTemplate template = templateService.selectById(id);
            result.put("status", true);
            result.put("template", template);
        } catch (Exception e) {
            result.put("status", false);
            log.error("根据id查找模板失败");
        }
        return result;
    }

    /**
     * 新增
     * @param template
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Map<String, Object> add(@RequestBody DealTemplate template){
        Map<String, Object> result = new HashMap<>();
        try {
            final UserLoginInfo principal =
                    (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            template.setCreator(principal.getUsername());
            templateService.add(template);
            result.put("status", true);
        } catch (Exception e) {
            result.put("status", false);
            log.error("黑名单模板添加失败");
        }
        return result;

    }

    /**
     * 修改
     * @param template
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Map<String, Object> edit(@RequestBody DealTemplate template){
        Map<String, Object> result = new HashMap<>();
        try {
            final UserLoginInfo principal =
                    (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            template.setCreator(principal.getUsername());
            templateService.update(template);
            result.put("status", true);
            result.put("msg", "修改成功");
        } catch (Exception e) {
            result.put("status", false);
            result.put("msg", "修改失败");
            log.error("黑名单模板修改失败");
        }
        return result;
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String id){
        Map<String, Object> result = new HashMap<>();
        try {
            templateService.delete(id);
            result.put("status", true);
            result.put("msg", "删除成功");
        } catch (Exception e) {
            result.put("status", false);
            result.put("msg", "删除失败");
            log.error("黑名单模板删除失败");
        }
        return result;
    }




}
