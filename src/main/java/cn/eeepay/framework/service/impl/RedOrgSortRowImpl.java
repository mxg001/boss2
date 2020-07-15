package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.daoSuperbank.OrgInfoDao;
import cn.eeepay.framework.daoSuperbank.RedOrgDao;
import cn.eeepay.framework.daoSuperbank.RedOrgSortDao;
import cn.eeepay.framework.daoSuperbank.SysOptionDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgInfo;
import cn.eeepay.framework.model.RedOrg;
import cn.eeepay.framework.model.RedOrgSort;
import cn.eeepay.framework.model.SysOption;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.RedService;
import cn.eeepay.framework.service.TranslateRow;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ExcelErrorMsgBean;
@Service
public class RedOrgSortRowImpl implements TranslateRow<RedOrgSort> {

    private ThreadLocal<String> busCodeThread = new ThreadLocal<>();//业务编号
    private ThreadLocal<Integer> orgStatusThread = new ThreadLocal<>();//是否开启组织控制
    
    @Resource
    private RedService redService;

    @Resource
    private OrgInfoDao orgInfoDao;

    @Resource
    private RedOrgDao redOrgDao;

	@Resource
    private SysOptionDao sysOptionDao;
    
    @Resource
    private RedOrgSortDao redOrgSortDao;
    @Override
    public RedOrgSort translate(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //1.获取组织ID、所属分类、排列顺序、操作
        //2.校验非空、操作只要新增、删除
        //3.校验组织ID和组织名称是否对应
        //4.根据对应的操作，insert、delete
        int orgIdColumn = 0;
        int categoryColumn = 1;
        int sortNumColumn = 2;
        int operateColumn = 3;
        
        String busCode = busCodeThread.get();
        Integer orgStatus = orgStatusThread.get();// 是否开启组织控制
        String orgId = String.valueOf(row.getCell(orgIdColumn));
        Long orgIdLong = Long.parseLong(orgId);
        if(StringUtils.isBlank(orgId)){
            errors.add(new ExcelErrorMsgBean(index, orgIdColumn+1, "组织ID不能为空"));
            return null;
        }else if(validExist(busCode,orgIdLong)){
            errors.add(new ExcelErrorMsgBean(index, orgIdColumn+1, "对应分类组织已存在"));
            return null;
        }else if(-1 != orgIdLong && !validOrgExist(orgIdLong,orgStatus,busCode)) {
        	errors.add(new ExcelErrorMsgBean(index, orgIdColumn+1, "组织ID不存在"));
            return null;
        }
        
        String category = String.valueOf(row.getCell(categoryColumn));
        if(StringUtils.isBlank(category)){
            errors.add(new ExcelErrorMsgBean(index, categoryColumn+1, "所属分类不能为空"));
            return null;
        }
        SysOption sysOption = new SysOption();
        sysOption.setName(category);
        List<SysOption> list = sysOptionDao.selectSysOption(sysOption);
        if(null == list || list.size() ==0){
            errors.add(new ExcelErrorMsgBean(index, categoryColumn+1, "找不到对应的所属分类"));
            return null;
        }else {
        	category = list.get(0).getCode();
        }
        
        String sortNum = String.valueOf(row.getCell(sortNumColumn));
        if(StringUtils.isBlank(sortNum)){
            errors.add(new ExcelErrorMsgBean(index, sortNumColumn+1, "排列顺序不能为空"));
            return null;
        }
        
        String operate = String.valueOf(row.getCell(operateColumn));
        if(StringUtils.isBlank(operate)){
            errors.add(new ExcelErrorMsgBean(index, operateColumn+1, "操作不能为空"));
            return null;
        }
        
        RedOrgSort redOrgSort = new RedOrgSort();
        redOrgSort.setBusCode(busCode);
        redOrgSort.setOrgId(orgIdLong);
        redOrgSort.setCategory(category);
        redOrgSort.setSortNum(Integer.parseInt(sortNum));
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        redOrgSort.setOperator(loginUser.getId());

        if("删除".equals(operate)){
        	redOrgSort.setOrgIds(orgId);
        	Integer count = redOrgSortDao.deleteRedOrgSort(redOrgSort);
        	if(!(count>0)) {
        		errors.add(new ExcelErrorMsgBean(index, orgIdColumn+1, "删除失败组织不存在"));
        		return null;
        	}
        } else {
            redService.addRedOrgSort(redOrgSort);
        }
        return redOrgSort;
    }

    public void setBusCode(String busCode){
        busCodeThread.set(busCode);
    }
    
    public void setOrgStatus(Integer orgStatus) {
		this.orgStatusThread.set(orgStatus);
	}

    /**
     *	验证组织分类是否已经存在
     * @param busCode
     * @param orgId
     * @return
     */
	private boolean validExist(String busCode,Long orgId) {
    	RedOrgSort queryParam = new RedOrgSort(busCode,orgId);
    	Page<RedOrgSort> page = new Page<>();
        List<RedOrgSort> list = redService.selectRedOrgSort(queryParam, page);
        return list.size() > 0;
    }
	
	/**
	 * 	判断组织ID是否存在[需要注意开启组织控制与不开启组织控制判断的数据来源不一样]
	 * @param orgIdLong
	 * @param orgStatus
	 * @param busCode
	 * @return
	 */
	private boolean validOrgExist(Long orgIdLong,Integer orgStatus,String busCode) {
		boolean result = false;
		
		if(1 == orgStatusThread.get()) {
			RedOrg redOrg = new RedOrg();
			redOrg.setBusCode(busCode);
			redOrg.setOrgId(orgIdLong);
			List<RedOrg> redOrgList= redOrgDao.selectRedOrgListAll(redOrg);
			result = redOrgList!=null && redOrgList.size()>0;
		}else {
			OrgInfo orgInfo = orgInfoDao.selectOrg(orgIdLong);
			result = null != orgInfo;
		}
		return result;
		
	}
}
