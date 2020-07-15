package test;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class PageTest {

    public static void main(String[] args){
        String str = "src/main/java/cn/eeepay/boss/action/api/SuperBankApiAction.java\n" +
                " src/main/java/cn/eeepay/framework/model/Result.java\n" +
                " src/main/java/cn/eeepay/framework/util/DateUtil.java\n" +
                " src/main/java/cn/eeepay/framework/util/SuperBankApiConstant.java\n" +
                " src/main/resources/security.xml\n" +
                " \n" +
                "  src/main/java/cn/eeepay/boss/action/api/SuperBankApiAction.java\n" +
                " src/main/java/cn/eeepay/framework/model/Result.java\n" +
                " src/main/java/cn/eeepay/framework/service/AgentOemService.java\n" +
                " src/main/java/cn/eeepay/framework/service/impl/AgentOemServiceImpl.java\n" +
                " \n" +
                "  src/main/java/cn/eeepay/boss/action/api/SuperBankApiAction.java\n" +
                " src/main/java/cn/eeepay/framework/service/SuperBankApiService.java\n" +
                " src/main/java/cn/eeepay/framework/service/impl/SuperBankApiServiceImpl.java\n" +
                " \n" +
                "  src/main/java/cn/eeepay/boss/action/api/SuperBankApiAction.java\n" +
                " src/main/java/cn/eeepay/framework/service/impl/SuperBankApiServiceImpl.java\n" +
                " \n" +
                "  src/main/java/cn/eeepay/framework/dao/SettleOrderInfoDao.java\n" +
                "  \n" +
                "   src/main/java/cn/eeepay/boss/action/api/SuperBankApiAction.java\n" +
                " src/main/java/cn/eeepay/framework/service/impl/SuperBankApiServiceImpl.java"
                ;
        convert(str);
    }

    public static String convert(String context){
        if(StringUtils.isBlank(context)){
            return context;
        }
        context = context.replaceAll("src/main/java", "<include name=\"WEB-INF/classes")
        .replaceAll("\\.java", "*.class\"/>")
        .replaceAll("src/main/webapp/", "<include name=\"")
        .replaceAll("src/main/resources", "<include name=\"WEB-INF/classes")
        .replaceAll("\\.html", ".html\"/>")
        .replaceAll("\\.xlsx", ".xlsx\"/>")
        .replaceAll("\\.js", ".js\"/>")
        .replaceAll("\\.xml", ".xml\"/>")
        .replaceAll("\\.css", ".css\"/>")
        .replaceAll("\"/>\"/>", "\"/>");

        String[] strArr = context.split("\n");

        if(strArr.length > 0){
            Set<String> setList = new HashSet<>();
            for (String str: strArr){
                str = str.replaceAll("\t", "");
                str = str.trim();
                boolean addStatus = setList.add(str);
                if(addStatus){
                    System.out.println(str);
                }
            }
        }

        return context;
    }
}
