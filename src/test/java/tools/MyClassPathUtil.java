package tools;

import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：quanhz
 * @date ：Created in 2019/9/24 16:43
 */
public class MyClassPathUtil {

    @Test
    public void fun1(){
        String password = new Md5PasswordEncoder().encodePassword("123456", "18126328768");
        System.out.println("password = " + password);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        try {
            d1 = sdf.parse("2099-12-30");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("sdf.format(d1) = " + sdf.format(d1));
    }

    @Test
    public void fun2(){
        String s1 = "aaabb";
        String s2 = "aabbb";
        System.out.println(CheckPermutation(s1, s2));
    }

    public   boolean CheckPermutation(String s1, String s2) {
        if(s1.length()!=s2.length()){
            return false;
        }

        String s3 = new String(s1);
        for(int i=0;i<s3.length();i++){
            String s = String.valueOf(s3.charAt(i));
            if(s1.contains(s) && s2.contains(s)){
                s1 = s1.replace(s,"");
                s2 = s2.replace(s,"");
            }
        }

        return s1.equals(s2);

    }


    public boolean isSingle(String s) {

        char[] chars = s.toCharArray();
        for (char aChar : chars) {
            int indexOf = s.indexOf(aChar);
            int lastIndexOf = s.lastIndexOf(aChar);
            if(indexOf!=lastIndexOf){
                return false;
            }
        }


        return true;
    }

    public static void classPathConver(Set<String> paths) {
        String template = "<include name=\"XXX\"/>";
        ArrayList<String> currPaths = new ArrayList<>();
        if (paths != null && paths.size() > 0) {
            String currPath = "";
            for (String path : paths) {
                if (path.indexOf(".java") > -1) {
                    int beginIndex = path.lastIndexOf("\\java\\") + 5;
                    String tmpPath = path.substring(beginIndex + 1, path.length()).replace(".java", "*.class").replace("\\", "/");
                    currPath = template.replace("XXX", "WEB-INF/classes/" + tmpPath);
                    currPaths.add(currPath);
                } else {
                    if (path.lastIndexOf("\\webapp\\") > -1) {
                        int beginIndex = path.lastIndexOf("\\webapp\\") + 7;
                        currPath = path.substring(beginIndex + 1, path.length()).replace("\\", "/");
                        currPath = template.replace("XXX", currPath);
                        currPaths.add(currPath);
                    }

                }
                System.out.println(currPath);
            }
        }
    }

    public static void main(String[] args) {
        Set<String> paths = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/test/java/tools/convertPaths.txt"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                paths.add(line);
            }
            MyClassPathUtil.classPathConver(paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
