package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CommonCode;
import cn.eeepay.framework.service.CommonCodeService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/3/18 10:55
 */
@RestController
@RequestMapping("commonCode")
public class CommonCodeAction {


    private Logger log = LoggerFactory.getLogger(CommonCodeAction.class);
    @Resource
    private CommonCodeService commonCodeService;


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/queryAll")
    public Map<String, Object> queryAll(@Param("page") Page<CommonCode> page,@RequestBody CommonCode commonCode) {
        Map<String, Object> result;
        try {
            result = ResponseUtil.buildResponseMap(commonCodeService.queryAll(page,commonCode.getAgentNo()), page.getTotalCount());
        } catch (Exception e) {
            log.error("查询通用码异常", e);
            result = ResponseUtil.buildResponseMap(e);
        }
        return result;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/queryById")
    public Map<String, Object> queryById(@Param("id") Long id) {
        Map<String, Object> result;
        try {
            result = ResponseUtil.buildResponseMap(commonCodeService.queryById(id));
        } catch (Exception e) {
            log.error("查询通用码异常", e);
            result = ResponseUtil.buildResponseMap(e);
        }
        return result;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/queryAgentName")
    public Map<String, Object> queryAgentName(@Param("agentNo") String agentNo) {
        Map<String, Object> result;
        try {
            Map<String,String> data = new HashMap<>();
            data.put("agentName",commonCodeService.queryAgentNameByNo(agentNo));
            result = ResponseUtil.buildResponseMap(data);
        } catch (Exception e) {
            log.error("查询代理商名称异常", e);
            result = ResponseUtil.buildResponseMap(e);
        }
        return result;
    }



    @DataSource
    @RequestMapping("/addCommonCode")
    @SystemLog(operCode = "commonCode.addCommonCode",description = "通用码新增")
    public Map<String, Object> saveCommonCode(@RequestBody CommonCode commonCode) {
        Map<String, Object> result;
        try {
            List<CommonCode> commonCodes = commonCodeService.query(commonCode);
            if(commonCodes!=null && commonCodes.size()>0){
                result = new HashMap<>();
                result.put("status",false);
                result.put("msg","该代理商已添加");
            }else{
                result = ResponseUtil.buildResponseMap(commonCodeService.insert(commonCode));
                result.put("msg","添加通用码成功");
            }
        } catch (Exception e) {
            log.error("添加通用码异常", e);
            result = ResponseUtil.buildResponseMap(e);
        }
        return result;
    }


    @DataSource
    @RequestMapping("/updateCommonCode")
    @SystemLog(operCode = "commonCode.updateCommonCode",description = "通用码修改")
    public Map<String, Object> updateCommonCode(@RequestBody CommonCode commonCode) {
        Map<String, Object> result;
        try {
            result = ResponseUtil.buildResponseMap(commonCodeService.update(commonCode));
            result.put("msg","修改通用码成功");
        } catch (Exception e) {
            log.error("修改通用码异常", e);
            result = ResponseUtil.buildResponseMap(e);
        }
        return result;
    }

    @DataSource
    @RequestMapping("/delCommonCode")
    @SystemLog(operCode = "commonCode.delCommonCode",description = "通用码删除")
    public Map<String, Object> delCommonCodeById(@RequestParam("id") Long id) {
        Map<String, Object> result;
        try {
            result = ResponseUtil.buildResponseMap(commonCodeService.delById(id));
            result.put("msg","删除通用码成功");
        } catch (Exception e) {
            log.error("删除通用码异常", e);
            result = ResponseUtil.buildResponseMap(e);
        }
        return result;
    }
}
