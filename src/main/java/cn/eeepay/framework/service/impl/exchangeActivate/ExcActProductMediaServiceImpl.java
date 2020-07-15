package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExcActProductMediaDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.exchangeActivate.ExcActProductMedia;
import cn.eeepay.framework.model.exchangeActivate.MediaPage;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.exchangeActivate.ExcActProductMediaService;
import cn.eeepay.framework.service.exchangeActivate.HttpJfpdapiService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/10/11/011.
 * @author liuks
 */
@Service("excActProductMediaService")
public class ExcActProductMediaServiceImpl implements ExcActProductMediaService {

    private static final Logger log = LoggerFactory.getLogger(ExcActProductMediaServiceImpl.class);

    @Resource
    private ExcActProductMediaDao excActProductMediaDao;
    @Resource
    private HttpJfpdapiService httpJfpdapiService;
    @Resource
    private SysDictService sysDictService;

    @Override
    public List<ExcActProductMedia> selectAllList(ExcActProductMedia media, Page<ExcActProductMedia> page) {
        return excActProductMediaDao.selectAllList(media,page);
    }

    @Override
    public int deleteMedia(int id) {
        return excActProductMediaDao.deleteMedia(id);
    }

    @Override
    public void downloadFile(String name, String reName, HttpServletResponse response, Map<String, Object> msg) {
        OutputStream outStream =null;
        InputStream inStream=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        try {
            if(name!=null&&!"".equals(name)){
                String fileName =reName+sdf.format(new Date())+name.substring(name.lastIndexOf("."),name.length());//时间戳+文件后缀名
                String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
                response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);

                String fileUrl= CommonUtil.getImgUrlAgent(name);
                URL url = new URL(fileUrl);
                URLConnection conn = url.openConnection(); // 打开连接
                inStream = conn.getInputStream();
                outStream=response.getOutputStream();
                byte[] bs = new byte[1024]; //1K的数据缓冲
                int len;
                while ((len = inStream.read(bs)) != -1) {
                    outStream.write(bs, 0, len);
                }
                outStream.flush();
            }
        }catch (Exception e){
            log.error("下载文档异常!",e);
            msg.put("status", false);
            msg.put("msg", "下载文档异常!");
        }finally {
            if(outStream!=null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inStream!=null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ExcActProductMedia getProductMedia(int id) {
        return excActProductMediaDao.getProductMedia(id);
    }

    @Override
    public int addExcActProductMedia(ExcActProductMedia media,Map<String, Object> msg) throws Exception {
        if(HttpJfpdapiServiceImpl.ROUTE_NO.equals(media.getChannelNo())){
            String returnStr=httpJfpdapiService.httpMedia(media.getGoodTypeNo());
            media.setMessage(returnStr);
            //解析返回参数
            List<MediaPage> list=analysisStr(returnStr);

            if(list!=null&&list.size()>0){
                //下载资源 上传阿里云
                fileZip(list,media);
                int num=excActProductMediaDao.addProductMedia(media);
                msg.put("status", true);
                msg.put("msg", "下载成功!");
                return num;
            }else{
                msg.put("status", false);
                msg.put("msg", "下载失败,上游无数据返回!");
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "下载失败,该通道暂未开通下载服务!");
        }
        return 0;
    }

    /**
     * 解析返回参数，生成下载文件参数
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<MediaPage> analysisStr(String str) throws UnsupportedEncodingException {
        List<MediaPage> list=new ArrayList<MediaPage>();
        if(str!=null){
            JSONObject json = JSON.parseObject(str);
            if(json!=null&&json.get("code")!=null&&"200".equals(json.get("code").toString())
                &&json.get("enmsg")!=null&&"ok".equals(json.get("enmsg").toString())
                    ){
                if(json.get("data")!=null){
                    JSONObject dataJson=json.getJSONObject("data");
                    if(dataJson.get("tutorials")!=null){
                        JSONArray tutorialsJson=dataJson.getJSONArray("tutorials");
                        for(int i=0;i<tutorialsJson.size();i++){
                            JSONObject pageJson=tutorialsJson.getJSONObject(i);
                            int page=i+1;
                            if(pageJson.get("page")!=null){
                                page=pageJson.getIntValue("page");
                            }
                            if(pageJson.get("texts")!=null){
                                JSONArray textsJson=pageJson.getJSONArray("texts");
                                for(int k=0;k<textsJson.size();k++){
                                    JSONObject textJson=textsJson.getJSONObject(k);
                                    if(textJson.get("text")!=null){
                                        MediaPage info=new MediaPage();
                                        info.setName("教程的文字"+page+"_"+(k+1));
                                        info.setRemark(textJson.getString("text"));
                                        info.setType(1);
                                        list.add(info);
                                    }
                                }
                            }
                            if(pageJson.get("pics")!=null){
                                JSONArray picsJson=pageJson.getJSONArray("pics");
                                for(int j=0;j<picsJson.size();j++){
                                    JSONObject imgJson=picsJson.getJSONObject(j);
                                    if(imgJson.get("url")!=null){
                                        MediaPage info=new MediaPage();
                                        info.setName("图片教程"+page+"_"+(j+1));
                                        info.setRemark(imgJson.getString("url"));
                                        info.setType(0);
                                        list.add(info);
                                    }
                                }
                            }
                        }
                    }
                    if(dataJson.get("videos")!=null){
                        JSONArray videosJson=dataJson.getJSONArray("videos");
                        for(int h=0;h<videosJson.size();h++){
                            JSONObject vidJson=videosJson.getJSONObject(h);
                            if(vidJson.get("thumbnail")!=null){
                                MediaPage info=new MediaPage();
                                info.setName("视频的缩略图1_"+(h+1));
                                info.setRemark(vidJson.getString("thumbnail"));
                                info.setType(0);
                                list.add(info);
                            }
                            if(vidJson.get("url")!=null){
                                MediaPage info=new MediaPage();
                                info.setName("视频的网页地址1_"+(h+1));
                                info.setRemark(vidJson.getString("url"));
                                info.setType(1);
                                list.add(info);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private  void fileZip(List<MediaPage> list,ExcActProductMedia media){
        SysDict sysDict = sysDictService.getByKey("IMAGES_URL");
        if(sysDict!=null){
            String baseUrl=sysDict.getSysValue();
            String str= RandomNumber.mumberRandom("file",20,4);
            String mkdirs=baseUrl+ File.separator+str;//D:\image\file2223
            //母文件夹
            File mkdirsFile = new File(mkdirs);
            FileUtil.createFolder(mkdirsFile,0);

            //下载文件存放文件夹
            String downloadStr=mkdirs+ File.separator+"download";//D:\image\file2223\download
            File downloadFile = new File(downloadStr);
            FileUtil.createFolder(downloadFile,0);

            //网络下载文件到 下载文件夹
            if(downloadURL(downloadStr,list)){
                //打包文件
                String zipName= "zip_"+new Date().getTime()+"_"+new Random().nextInt(100000) +".zip";
                String zips=mkdirs+File.separator+zipName;//D:\image\file2223\zip_1122.zip
                File zipsFile = new File(zips);
                FileUtil.createFolder(zipsFile,1);

                //zip打包
                FileUtil.zipsFile(downloadFile,zipsFile);

                //上传zip到阿里云
                FileUtil.uploadALiyun(zipName,zipsFile);

                media.setGoodFile(zipName);
            }
            //删除临时文件
            FileUtil.delFolder(mkdirs);
        }else{
            log.error("导出积分兑换核销管理未配置数据字典图片目录!");
        }
    }

    /**
     * 网络下载文件资源存入本地临时文件夹
     */
    private boolean downloadURL(String baseFile,List<MediaPage> list){
        if(list!=null&&list.size()>0){
            //文本
            String txt=baseFile+File.separator+"教程文本.txt";
            int num=0;
            for(MediaPage info:list){
                num=num+downloadMediaPage(baseFile,txt,info);
            }
            if(num>0){
                return true;
            }
        }
        return false;
    }
    private int downloadMediaPage(String baseFile,String txtFile,MediaPage info){//D:\image\file2223\download
        if(info!=null){
            if(0==info.getType()){//文件
                String fileUrl=info.getRemark();
                String imgSuffix=fileUrl.substring(fileUrl.lastIndexOf("."),fileUrl.length());//文件后缀
                if(imgSuffix.indexOf("/")>0){
                    imgSuffix=".png";
                }
                return FileUtil.downloadImage(baseFile,info.getName(),info.getRemark(),imgSuffix);
            }else if(1==info.getType()){//文本
                return writeText(txtFile,info);
            }
        }
        return 0;
    }

    private  int writeText(String txtFile,MediaPage info){
        File file = new File(txtFile);
        FileUtil.createFolder(file,1);

        FileOutputStream fos=null;
        OutputStreamWriter os=null;
        BufferedWriter bw=null;
        try {
            fos=new FileOutputStream(file,true);
            os=new OutputStreamWriter(fos,"utf-8");
            bw=new BufferedWriter(os);
            bw.write(info.getName()+":"+info.getRemark()+"\r\n");
            bw.flush();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
