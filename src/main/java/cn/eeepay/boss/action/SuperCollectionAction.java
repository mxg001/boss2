package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.SuperCollection;
import cn.eeepay.framework.service.SuperCollectionService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18/018.
 * @author liuks
 * 超级还款设置
 */
@RestController
@RequestMapping(value = "/superCollection")
public class SuperCollectionAction {
    public static final String NUMBER="001";//超级还款设置编号

    public static final String SUPERPOS="SUPERPOS";//redis,累计交易量key

    public static final String STARTTIME="startTime";//SUPERPOS 对象中的 开始时间 000000

    public static final String ENDTIME="endTime";//SUPERPOS 对象中的 结束时间  235959

    public static final String TOTALQOUTA="totalQouta";//SUPERPOS 对象中的 总额度

    public static final String USEQOUTA="useQouta";//SUPERPOS 对象中的 已使用额度

    private static final Logger log = LoggerFactory.getLogger(SuperCollectionAction.class);

    @Resource
    private SuperCollectionService superCollectionService;

    @Resource
    private RedisTemplate redisTemplate;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectInfo")
    @ResponseBody
    public Map<String,Object> selectByNumber(){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            SuperCollection sc=superCollectionService.selectByNumber(SuperCollectionAction.NUMBER);
            //重redis里面获取值
            if(redisTemplate.opsForHash().hasKey(SuperCollectionAction.SUPERPOS,SuperCollectionAction.USEQOUTA)){
                Object obj=redisTemplate.opsForHash().get(SuperCollectionAction.SUPERPOS,SuperCollectionAction.USEQOUTA);
                //给个默认值,后续需要查询数据库统计
                msg.put("sumlines",obj==null?0:obj.toString());
            }else{
                msg.put("sumlines",0);
            }
            msg.put("info",sc);
            msg.put("status", true);
        } catch (Exception e){
            log.error("超级收款设置查询失败!",e);
            msg.put("status", false);
            msg.put("msg", "超级收款设置查询失败!");
        }
        return  msg;
    }

    @RequestMapping(value = "/saveInfo")
    @SystemLog(description = "修改超级收款设置",operCode="superCollection.saveInfo")
    @ResponseBody
    public Map<String, Object> saveSuperCollection(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try{
            SuperCollection sc = JSONObject.parseObject(param,SuperCollection.class);
            sc.setNumber(SuperCollectionAction.NUMBER);
            int ret=superCollectionService.saveSuperCollection(sc);
            if (ret > 0) {

                msg.put("status", true);
                msg.put("msg", "修改超级收款设置成功!");
            }
        } catch (Exception e) {
            log.error("修改超级收款设置失败!", e);
            msg.put("status", false);
            msg.put("msg", "修改超级收款设置失败!");
        }
        return msg;
    }

}
