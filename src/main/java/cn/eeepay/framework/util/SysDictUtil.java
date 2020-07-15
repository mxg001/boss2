package cn.eeepay.framework.util;

import cn.eeepay.framework.model.SysDict;

import java.util.List;

public class SysDictUtil {

    /**
     * 根据字典值返回字典名
     */
    public static String getSysNameByValue(List<SysDict> sysDictList, String value) {
        if (value != null) {
            for (SysDict sysDict : sysDictList) {
                if (value.equals(sysDict.getSysValue())) {
                    return sysDict.getSysName();
                }
            }
        }
        return null;

    }


}
