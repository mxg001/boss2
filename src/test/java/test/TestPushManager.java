package test;

import cn.eeepay.boss.action.pushManager.PushManagerAction;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class TestPushManager {

    private static final Logger log = LoggerFactory.getLogger(TestPushManager.class);


    @Test
    public void fun6(){
        JSONObject jsonObject  = new JSONObject();
        jsonObject.put("username","hinzzz");
        jsonObject.put("password","https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=idea%E6%80%8E%E6%A0%B7%E5%85%8B%E9%9A%86maven%E5%B7%A5%E7%A8%8B%E5%88%B0githup&oq=idea%25E6%2580%258E%25E6%25A0%25B7%25E4%25B8%258A%25E4%25BC%25A0maven%25E5%25B7%25A5%25E7%25A8%258B%25E5%2588%25B0githup&rsv_pq=b61245fe004e36d0&rsv_t=9851MCRSZetgYVCyvdV1kVIj8l8w89kaoO7ye%2FWpyAzkPhOrx4cCiHsjgco&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=4053&rsv_sug3=74&rsv_sug1=31&rsv_sug7=000&rsv_sug2=0&rsv_sug4=4892&rsv_sug=1");

        Map<String,String> map = new HashMap<>();
        map.put("data",jsonObject.toJSONString());
        //HttpUtils.sendPostRequest("http://localhost:9999/sso/testRequestBody",map);



        String resp= null;
        JSONObject obj = new JSONObject();
        obj.put("name", "张三");
        obj.put("age", "18");
        String query = jsonObject.toString();
        log.info("发送到URL的报文为：");
        log.info(query);
        try {
            URL url = new URL("http://localhost:9999/sso/testRequestBody"); //url地址

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                os.write(query.getBytes("UTF-8"));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                log.info("返回来的报文："+sbf.toString());
                resp = sbf.toString();

            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JSONObject json = (JSONObject) JSON.parse(resp);
        }
    }

    @Test
    public void fun5(){
        List<Integer> list = new ArrayList<>();
        for (Integer integer : list) {
            System.out.println("integer = " + integer);
        }
    }


    @Test
    public void fun4(){
        String s = "";
        for (String s1 : s.split(",")) {
            System.out.println("s1 = " + s1);
        }
    }

    @Test
    public void fun3(){
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<24;i++){
            list.add(i);
        }
        //getList(5,list);

        getPageInfo(7,36);
    }

    private static List<Page<?>> getPageInfo(int sliceLength, int totalCounts) {
        List<Page<?>> mEndList=new ArrayList<>();
        if( totalCounts%sliceLength!=0) {
            for (int j = 0; j < totalCounts / sliceLength + 1; j++) {
                Page page = new Page();
                page.setPageNo(j+1);
                page.setPageSize(sliceLength);
                mEndList.add(page);
            }
        }else if(totalCounts%sliceLength==0){
            for (int j = 0; j < totalCounts / sliceLength; j++) {
                Page page = new Page();
                page.setPageNo(j+1);
                page.setPageSize(sliceLength);
                mEndList.add(page);
            }
        }
        for (int i = 0; i < mEndList.size(); i++) {
            System.out.println(mEndList.get(i).getPageNo()+"  "+mEndList.get(i).getPageSize());
        }
        return mEndList;
    }

    private static List<List<?>> getList(int sliceLength, List<?> mList) {
        List<List<?>> mEndList=new ArrayList<>();
        if( mList.size()%sliceLength!=0) {
            for (int j = 0; j < mList.size() / sliceLength + 1; j++) {
                if ((j * sliceLength + sliceLength) < mList.size()) {
                    mEndList.add(mList.subList(j * sliceLength, j * sliceLength + sliceLength));//0-3,4-7,8-11    j=0,j+3=3   j=j*3+1
                } else if ((j * sliceLength + sliceLength) > mList.size()) {
                    mEndList.add(mList.subList(j * sliceLength, mList.size()));
                } else if (mList.size() < sliceLength) {
                    mEndList.add(mList.subList(0, mList.size()));
                }
                System.out.println("j = " + j);
            }
        }else if(mList.size()%sliceLength==0){
            for (int j = 0; j < mList.size() / sliceLength; j++) {
                if ((j * sliceLength + sliceLength) <= mList.size()) {
                    mEndList.add(mList.subList(j * sliceLength, j * sliceLength + sliceLength));//0-3,4-7,8-11    j=0,j+3=3   j=j*3+1
                } else if ((j * sliceLength+ sliceLength) > mList.size()) {
                    mEndList.add(mList.subList(j * sliceLength, mList.size()));
                } else if (mList.size() < sliceLength) {
                    mEndList.add(mList.subList(0, mList.size()));
                }
                System.out.println("jjjj = " + j);
            }
        }
        for (int i = 0; i < mEndList.size(); i++) {
            System.out.println(mEndList.get(i).toString()+"");
        }
        return mEndList;
    }

    @Test
    public void fun(){
        remotePush(null,1,1,"测试推送标题","测试推送内容","http://www.baidu.com");
    }

    public void remotePush(List<?> list, Integer osType, Integer pushMode, String title, String content, String link){
        list = (List<MerchantInfo>)list;
        String mStr = "258121000001251,254111000001252,258121000001253";

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("merchantNoArray", mStr);
        requestMap.put("osType", osType+"");
        requestMap.put("pushMode", pushMode+"");
        requestMap.put("title", title);
        requestMap.put("content", content);
        String ext = "{\"link\":\""+link+"\"}";
        requestMap.put("ext", ext);
        String str = HttpUtils.sendPostRequest("http://192.168.4.20:8011/riskhandle/controlJPush", requestMap);
        System.out.println("result = " + str);
        JSONObject json = JSON.parseObject(str);
        json.getString("header").toString();
    }

    @Test
    public void fun2(){
        String str =  "{\"header\":{\"error\":\"成功\",\"errMsg\":\"发送成功\",\"succeed\":true},\"body\":{\"msg_id\":0}}";
        JSONObject json = JSON.parseObject(str);
        String body = json.getString("body");
        if(StringUtils.isNotEmpty(body)){
            JSONObject headerObj = JSON.parseObject(body);
            String msg_id = headerObj.getString("msg_id");
            System.out.println("msg_id = " + msg_id);
        }
        String header = json.getString("header");
        if(StringUtils.isNotEmpty(header)){
            JSONObject headerObj = JSON.parseObject(header);
            String succeed = headerObj.getString("succeed");
            System.out.println("succeed = " + succeed);
        }

    }
}
