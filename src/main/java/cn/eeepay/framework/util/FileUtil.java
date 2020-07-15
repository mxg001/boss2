package cn.eeepay.framework.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/4/23/023.
 * @author liuks
 * 文件操作
 */
public class FileUtil {
    /**
     * 删除该路径下所有文件（文件夹和文件）
     * @param folderPath
     */
    public static  void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     *打包指定目录下的文件成zip
     * @param inFile 要打包的路径
     * @param zipsFile 打包成ZIP的路径
     */
    public static void zipsFile(File inFile,File zipsFile){
        //zip打包
        FileOutputStream fos = null;
        ZipOutputStream zos=null;
        try {
            fos = new FileOutputStream(zipsFile);
            zos= new ZipOutputStream(fos);
            zipFile(inFile, zos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(zos!=null){
                try {
                    zos.close();
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
    }
    //封装压缩文件的方法
    public static  void zipFile(File inputFile,ZipOutputStream zipoutputStream) {
        if(inputFile.exists()) { //判断文件是否存在
            if (inputFile.isFile()) {  //判断是否属于文件，还是文件夹
                FileInputStream fis=null;
                BufferedInputStream bis=null;
                try {
                    fis = new FileInputStream(inputFile);
                    bis = new BufferedInputStream(fis);
                    //将文件写入zip内，即将文件进行打包
                    ZipEntry ze = new ZipEntry(inputFile.getName()); //获取文件名
                    zipoutputStream.putNextEntry(ze);
                    //写入文件的方法，同上
                    byte [] b=new byte[1024];
                    long l=0;
                    while(l<inputFile.length()){
                        int j=bis.read(b,0,1024);
                        l+=j;
                        zipoutputStream.write(b,0,j);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //关闭输入输出流
                    if(bis!=null){
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fis!=null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {  //如果是文件夹，则使用穷举的方法获取文件，写入zip
                try {
                    File[] files = inputFile.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        zipFile(files[i], zipoutputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 从网络下载文件到本地
     * @param baseFile 根文件夹目录
     * @param fileName 文件名称（不带后缀格式）
     * @param fileUrl 网络文件路径（文件格式后缀）
     * @imgSuffix 文件后缀
     * @return
     */
    public static int downloadImage(String baseFile,String fileName,String fileUrl,String imgSuffix){

        OutputStream outStream =null;
        InputStream inStream=null;
        try {
            URL url = new URL(fileUrl);// 构造URL
            URLConnection conn = url.openConnection(); // 打开连接
            conn.setConnectTimeout(5 * 1000);  //超时响应时间为5秒
            //通过输入流获取图片数据
            inStream = conn.getInputStream();

            String img=baseFile+File.separator+fileName+imgSuffix;
            File imgFile = new File(img);
            createFolder(imgFile,1);

            outStream = new FileOutputStream(imgFile);
            byte[] bs = new byte[1024]; //1K的数据缓冲
            int len;
            while ((len = inStream.read(bs)) != -1) {
                outStream.write(bs, 0, len);
            }
            outStream.flush();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
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
        return 0;
    }

    /**
     * 将本地文件上传到阿里云
     * @param fileName 文件名
     * @param file 文件File
     */
    public  static void  uploadALiyun(String fileName,File file){
        FileInputStream in=null;
        try {
            in= new FileInputStream(file);
            ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_ATTCH_TUCKET, fileName,in);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 创建文件夹/文件
     * 0 文件夹 1文件
     * @param file
     */
    public static void createFolder(File file,int sta){
        if(!file.exists()){
            if(sta==0){
                file.mkdirs();
            }else{
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
