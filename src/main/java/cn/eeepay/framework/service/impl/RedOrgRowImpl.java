package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.OrgInfoDao;
import cn.eeepay.framework.daoSuperbank.RedOrgDao;
import cn.eeepay.framework.model.OrgInfo;
import cn.eeepay.framework.model.RedOrg;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.RedService;
import cn.eeepay.framework.service.TranslateRow;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ExcelErrorMsgBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service
public class RedOrgRowImpl implements TranslateRow<RedOrg> {

    private ThreadLocal<String> busCodeThread = new ThreadLocal<>();//业务编号

    @Resource
    private RedService redService;

    @Resource
    private OrgInfoDao orgInfoDao;

    @Resource
    private RedOrgDao redOrgDao;

    @Override
    public RedOrg translate(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //1.获取组织ID、组织名称、操作
        //2.校验非空、操作只要新增、删除
        //3.校验组织ID和组织名称是否对应
        //4.根据对应的操作，insert、delete
        int orgIdLine = 0;
        int orgNameLine = 1;
        int operateLine = 2;
        String orgId = String.valueOf(row.getCell(orgIdLine));
        if(StringUtils.isBlank(orgId)){
            errors.add(new ExcelErrorMsgBean(index, orgIdLine, "组织ID不能为空"));
            return null;
        }
        Long orgIdLong = Long.parseLong(orgId);
        String orgName = String.valueOf(row.getCell(orgNameLine));
        if(StringUtils.isBlank(orgName)){
            errors.add(new ExcelErrorMsgBean(index, orgNameLine, "组织名称不能为空"));
            return null;
        }
        String operate = String.valueOf(row.getCell(operateLine));
        if(StringUtils.isBlank(operate)){
            errors.add(new ExcelErrorMsgBean(index, operateLine, "操作不能为空"));
            return null;
        }
        OrgInfo orgInfo = orgInfoDao.selectOrg(orgIdLong);
        if(orgInfo == null){
            errors.add(new ExcelErrorMsgBean(index, orgIdLine, "找不到对应的组织"));
            return null;
        }
        if(!orgInfo.getOrgName().equals(orgName)){
            errors.add(new ExcelErrorMsgBean(index, orgNameLine, "组织名称与组织ID不相符"));
            return null;
        }
        RedOrg redOrg = new RedOrg();
        redOrg.setBusCode(busCodeThread.get());
        redOrg.setOrgId(orgIdLong);
        redOrg.setOrgName(orgName);
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        redOrg.setOperator(loginUser.getId());
        Date date = new Date();
        if("删除".equals(operate)){
            redOrgDao.deleteRedOrg(redOrg);
        } else {
            if(redService.checkExists(redOrg)){
                errors.add(new ExcelErrorMsgBean(index, orgIdLine, "组织已存在"));
                return null;
            }
            redOrg.setCreateTime(date);
            redOrgDao.insert(redOrg);
        }
        return redOrg;
    }

    public void setBusCode(String busCode){
        busCodeThread.set(busCode);
    }
}
