package test;

import cn.eeepay.framework.util.ExcelErrorMsgBean;
import cn.eeepay.framework.util.ExcelUtils;
import cn.eeepay.framework.service.TranslateRow;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMain {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\贷款结果导入通用模板-01.xlsx");
        List<ExcelErrorMsgBean> errors = new ArrayList<>();
        List<Map<String, String>> maps = ExcelUtils.parseWorkbook(new FileInputStream(file),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                errors, new TranslateRow<Map<String, String>>() {
                    @Override
                    public Map<String, String> translate(Row row, int index, List<ExcelErrorMsgBean> errors) {
                        Map<String, String> resultMap = new HashMap<>();
                        int line = 0;
                        resultMap.put("string1", row.getCell(line++).toString());
                        errors.add(new ExcelErrorMsgBean(index, line, "报错"));
                        resultMap.put("string2", row.getCell(line++).toString());
                        resultMap.put("string3", row.getCell(line++).toString());
                        resultMap.put("string4", row.getCell(line++).toString());
                        resultMap.put("string5", row.getCell(line++).toString());
                        resultMap.put("string6", row.getCell(line++).toString());
                        return resultMap;
                    }
                });
        System.out.println(errors.toString());
        System.out.println(maps);
    }

}
