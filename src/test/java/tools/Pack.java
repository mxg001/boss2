package tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

/**
 * 打包工具
 */
public class Pack {
    String rn = "\n";
    String filesName = "";

    //远程分支1
    String branch1 = "remotes/origin/feature/20200521-99版活动后台功能开发";
    //远程 develop 分支
    String branch2 = "remotes/origin/develop";

    /**
     * 获取到两个分支的差异文件名
     */
    @Before
    public void diff() {
        //可以将结果输出至文件
        //git diff remotes/origin/feature/20200521-99版活动后台功能开发 remotes/origin/develop --name-only >>diff.txt
        String cmd = String.format("git diff %s %s --name-only", branch1, branch2);
        System.out.println(cmd);
        filesName = RuntimeUtil.execForStr(cmd);
    }

    /**
     * 获取两个分支的差异文件名，并生成相应的Ant文件列表
     */
    @Test
    public void buildAntInclude() {
        StringBuffer sb = new StringBuffer();
        for (String line : StringUtils.split(filesName, rn)) {
            //只处理src/main/下的文件
            if (line.contains("src/main/")) {
                String name = StringUtils.replace(line, "src/main/java/", "WEB-INF/classes/");
                name = StringUtils.replace(name, ".java", "*.class");
                name = StringUtils.replace(name, ".kt", "*.class");
                name = StringUtils.replace(name, "src/main/webapp/", "");
                sb.append(rn).append("<include name=\"").append(name).append("\"/>");
            }else{
                System.out.println(">>>>>>>>>>>>>>>>> " + line);
            }
        }
        System.err.println(sb);
    }

    /**
     * 直接将差异文件打包至文件夹中
     */
    @Test
    public void diffToDir() {
        String absolutePath = FileUtil.getAbsolutePath("");
        String basePath = StringUtils.replace(FileUtil.getParent(absolutePath, 2), "\\", "/") + "/";
        String compilePath = "target/classes";
        String staticPath = basePath + "src/main/webapp";
        String sourcePath = basePath + compilePath;
        String targetBasePath = basePath + "diff/";
        FileUtil.del(targetBasePath);
        TreeMap<String, String> pathMap = new TreeMap<>();

        for (String line : StringUtils.split(filesName, rn)) {
            //只处理src/main/下的文件
            if (line.contains("src/main/")) {
                String name = StringUtils.replace(line, "src/main/java/", compilePath+"/");
                name = StringUtils.replace(name, ".java", "");
                name = StringUtils.replace(name, ".kt", "");
                String key = basePath + name;
                key = StringUtils.replace(key, "\\", "/");
                pathMap.put(key, "");
            }
        }
        List<File> files = FileUtil.loopFiles(sourcePath);
        List<String> names = new ArrayList<>();
        for (File file : files) {
            String fileAbsolutePath = file.getAbsolutePath().replace("\\", "/");
            names.add(fileAbsolutePath);
        }
        Set<String> keys = pathMap.keySet();
        TreeMap<String, String> copy = new TreeMap<>();
        List<String> diffFiles = new ArrayList<>();
        for (String key : keys) {
            if (key.startsWith(sourcePath)) {
                for (String name : names) {
                    if (name.startsWith(key)) {
                        diffFiles.add(name);
                        copy.put(name, name.replace(sourcePath + "/", targetBasePath + "WEB-INF/"));
                    }
                }
            } else {
                diffFiles.add(key);
                copy.put(key, key.replace(staticPath + "/", targetBasePath));
            }
        }
        System.out.println("\n*******************************\n开始打包以下文件:");
        Set<Map.Entry<String, String>> entries = copy.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            FileUtil.copy(entry.getKey(), entry.getValue(), true);
            System.err.println(entry.getValue());
        }
        System.out.println("\n打包完成：" + targetBasePath);

    }
}
