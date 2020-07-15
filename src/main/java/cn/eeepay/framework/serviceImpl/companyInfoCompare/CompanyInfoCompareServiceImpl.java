package cn.eeepay.framework.serviceImpl.companyInfoCompare;

import cn.eeepay.framework.dao.CompanyInfoCompareDao;
import cn.eeepay.framework.model.CompanyInfoCompare;
import cn.eeepay.framework.service.CompanyInfoCompareService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("companyInfoCompareService")
public class CompanyInfoCompareServiceImpl implements CompanyInfoCompareService {

    @Resource
    private CompanyInfoCompareDao companyInfoCompareDao;

    @Override
    public List<CompanyInfoCompare> getCompanyInfoCompareOne(String companyName,String licenseSocialCode,String legalName) {
        if(StringUtils.isNotBlank(companyName)){
            List<CompanyInfoCompare> list= companyInfoCompareDao.getCompanyInfoCompareOne(companyName);
            if(list!=null&&list.size()>0){
                for(CompanyInfoCompare info:list){
                    if("1".equals(info.getCompanyStatus())){
                        if(!licenseSocialCode.equalsIgnoreCase(info.getLicenseSocialCode())){
                            info.setReturnState(2);
                            info.setMsg("营业执照编号信息比对不一致!");
                            break;
                        }
                        if(!legalName.equals(info.getLegalName())){
                            info.setReturnState(2);
                            info.setMsg("法人姓名信息比对不一致!");
                            break;
                        }
                        info.setReturnState(1);
                        info.setMsg("营业执照名称、营业执照编号、法人姓名比对一致!");
                    }else{
                        info.setReturnState(2);
                        info.setMsg("营业执照名称信息比对不一致!");
                    }
                }
            }
            return list;
        }
        return null;
    }
}
