package cn.eeepay.framework.service;

import cn.eeepay.framework.util.ExcelErrorMsgBean;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel 解析转化接口
 * Created by Administrator on 2017/5/24.
 */
public interface TranslateRow<T> {

    /**
     * 解析转化接口
     * @param row    Excel 行
     * @param index  第几行
     * @param errors 错误信息集合
     * @return 解析后的数据
     */
    T translate(Row row, int index, List<ExcelErrorMsgBean> errors);


}
