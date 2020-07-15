package cn.eeepay.framework.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 神策数据结构
 */
public class SensorsData {
    private static final Logger log = LoggerFactory.getLogger(SensorsData.class);

    /**
     * 详细说明请参考
     *
     * @see <a href="https://www.sensorsdata.cn/manual/data_schema.html#1-%E6%95%B0%E6%8D%AE%E6%95%B4%E4%BD%93%E6%A0%BC%E5%BC%8F">数据整体格式</a>
     */
    public enum Type {
        track("事件"), track_signup("关联新旧 id，当且仅当此时 original_id 为必须字段且有意义。"), profile_set("直接设置一个用户的 Profile，如果用户或者 Profile 的字段已存在，则覆盖，不存在则自动创建。"), profile_set_once("直接设置一个用户的 Profile。与 profile_set 接口不同的是，如果用户或者 Profile 的字段已存在，则这条记录会被忽略而不会覆盖已有数据，如果 Profile 不存在则会自动创建。因此，profile_set_once 比较适用于为用户设置首次激活时间、首次注册时间等只在首次设置时有效的属性。"), profile_increment("增加或减少一个用户的某个 Numeric 类型的 Profile，如果用户不存在则自动创建, Profile 不存在则默认为 0。"), profile_delete("删除一个用户的整个 Profile。"), profile_append("向某个用户的某个数组类型的 Profile 添加一个或者多个值。"), profile_unset("将某个用户的某些属性值设置为空。另外，为了与其它接口保持一致，在提交的数据上，属性的值请设置为非 null 的任何值，例如 true。");
        String remark;

        Type(String remark) {
            this.remark = remark;
        }

        public boolean equals(String name) {
            return this.name().equals(name);
        }

    }

    /*public static JSONObject sensorsQuery(String sql,int timeOut) {
        sql = URLUtil.encode(sql);
        String finalUrl = String.format("%s?token=%s&project=%s&q=%s&format=json", qURL, qToken, projectName,sql);
        log.info("res"+finalUrl);
        String res = HttpUtil.get(finalUrl, timeOut);
        log.info("res"+res);
        if(StringUtil.isBlank(res)){
            return null;
        }
        return JSONObject.parseObject(res);

    }*/

    @SerializedName("distinct_id")
    String distinctId;

    @SerializedName("original_id")
    String originalId;

    Long time;
    String type;
    String event;
    String project;
    Map properties;

    /**
     * 构建神策消息
     *
     * @param type       类型
     * @param event      事件名称
     * @param distinctId 唯一ID
     * @return
     */
    public static SensorsData builder(Type type, String event, String distinctId) {
        return new SensorsData().setType(type).setDistinctId(distinctId)
                .setEvent(event)
                .setTime(System.currentTimeMillis())
                .setProperties(new HashMap());
    }

    public String getDistinctId() {
        return distinctId;
    }

    public SensorsData setDistinctId(String distinctId) {
        this.distinctId = distinctId;
        return this;
    }

    public String getOriginalId() {
        return originalId;
    }

    public SensorsData setOriginalId(String originalId) {
        this.originalId = originalId;
        return this;
    }

    public Long getTime() {
        return time;
    }

    /**
     * 时间戳，毫秒
     *
     * @param time
     * @return
     */
    public SensorsData setTime(Long time) {
        this.time = time;
        return this;
    }

    public String getType() {
        return type;
    }

    public SensorsData setType(Type type) {
        this.type = type.name();
        return this;
    }

    public String getEvent() {
        return event;
    }

    public SensorsData setEvent(String event) {
        this.event = event;
        return this;
    }

    public String getProject() {
        return project;
    }

    public SensorsData setProject(String project) {
        this.project = project;
        return this;
    }

    public Map getProperties() {
        return properties;
    }

    public SensorsData setProperties(Map properties) {
        this.properties = properties;
        return this;
    }

    public SensorsData put(String key, Object val) {
        this.properties.put(key, val);
        return this;
    }

    /**
     * 存入键值对，当且仅当值不为空时才存入
     * <ol>
     * <li>不为null</li>
     * <li>Map，isNotEmpty</li>
     * <li>List，长度大于0</li>
     * <li>其它类型String.valueOf()后，isNotBlank</li>
     * </ol>
     *
     * @param key 键
     * @param val 值
     * @return
     */
    public SensorsData putNotBlank(String key, Object val) {
        if (null == val) {
            return this;
        }
        if (val instanceof List) {
            val = ((List) val).size() == 0 ? null : val;
        } else if (val instanceof Map) {
            val = StringUtil.isNotBlank((Map) val) ? null : val;
        }
        if (StringUtil.isNotBlank(val)) {
            this.properties.put(key, val);
        }
        return this;
    }

    private SensorsData() {
    }

    private static void http(String url, int timeOut, Object data) {
        String logTag = RandomUtil.simpleUUID();
        Map params = new HashMap(5);
        params.put("token", Constants.BEHAVIOUR_SERVER_TOKEN);
        params.put("project", Constants.BEHAVIOUR_SERVER_PROJECT);

        String dataType;
        if (data instanceof List) {
            dataType = "data_list";
        } else if (data instanceof SensorsData) {
            dataType = "data";
        } else {
            log.info("不支持的数据类型");
            return;
        }

        String json = Constants.gsonLight.toJson(data);
        log.info(String.format("[%s]Json:%s", logTag, json));
        String base64 = Base64.encode(json);
//        log.info(String.format("[%s]Base64:%s", logTag, base64));
        String urlEncode = null;
        try {
            urlEncode = URLEncoder.encode(base64, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        log.info(String.format("[%s]URLEncode:%s", logTag, urlEncode));

        params.put(dataType, urlEncode);

        log.info(String.format("[%s][%s][%s][%s]", logTag, timeOut, url, params));
        String res;
        boolean isPost = true;
        if(isPost){
            String urltemp = String.format("%s?token=%s&project=%s",url,Constants.BEHAVIOUR_SERVER_TOKEN,Constants.BEHAVIOUR_SERVER_PROJECT);
            String body = String.format("%s=%s", dataType, urlEncode);
            res = HttpUtil.post(urltemp, body, timeOut);
        }else{
            String finalUrl = String.format("%s?token=%s&project=%s&%s=%s", url, Constants.BEHAVIOUR_SERVER_TOKEN, Constants.BEHAVIOUR_SERVER_PROJECT, dataType, urlEncode);
            res = HttpUtil.get(finalUrl, timeOut);
        }
        log.info(String.format("[%s][%s][%s][%s]", logTag, timeOut, url, res));
    }

    /**
     * 推送数据
     */
    public void flush() {
        http(Constants.BEHAVIOUR_SERVER_URL, 1 * 60 * 1000, this);
    }

    /**
     * 批量推送数据至神策服务
     *
     * @param tracks
     */
    public static void flushList(List<SensorsData> tracks) {
        http(Constants.BEHAVIOUR_SERVER_URL, 1 * 60 * 1000, tracks);
    }

    public static void main(String[] args) {
        String id_card_no = "371426197708172847";
        int sex = Integer.valueOf(id_card_no.substring(16, 17));
        String gender = (sex & 1) == 1 ? "男" : "女";
        String age = String.format("%s年%s月%s日", id_card_no.substring(6, 10), id_card_no.substring(10, 12), id_card_no.substring(12, 14));
        System.out.println(gender + ":" + age);
    }

}
