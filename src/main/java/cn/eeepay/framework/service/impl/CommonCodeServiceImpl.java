package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.CommonCodeDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CommonCode;
import cn.eeepay.framework.service.CommonCodeService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/3/18 11:22
 */
@Service("CommonCodeService")
public class CommonCodeServiceImpl implements CommonCodeService {


    @Resource
    private CommonCodeDao commonCodeDao;

    @Resource
    private AgentInfoDao agentInfoDao;


    @Override
    public CommonCode queryById(Long id) {
        CommonCode  commonCode= commonCodeDao.queryById(id);
        if(commonCode!=null){
            commonCode.setCommonCodeUrl(CommonUtil.getImgUrlAgent(commonCode.getCommonCodeUrl()));
        }
        return commonCode;
    }

    @Override
    public List<CommonCode> query(CommonCode commonCode) {
        return commonCodeDao.query(commonCode);
    }

    @Override
    public int insert(CommonCode commonCode) {
        return commonCodeDao.insert(commonCode);
    }

    @Override
    public int update(CommonCode commonCode) {
        if(StringUtil.isBlank(commonCode.getCommonCodeUrl())){
            return 0;
        }else{
            return commonCodeDao.update(commonCode);
        }
    }

    @Override
    public int delById(Long id) {
        return commonCodeDao.delById(id);
    }

    @Override
    public List<CommonCode> queryAll(Page<CommonCode> page,String agentNo) {
        List<CommonCode> commonCodes = commonCodeDao.queryAll(page,agentNo);
        if(page.getResult()!=null && page.getResult().size()>0){
            for (CommonCode commonCode : page.getResult()) {
                commonCode.setCommonCodeUrl(CommonUtil.getImgUrlAgent(commonCode.getCommonCodeUrl()));
            }

        }
        return commonCodes;
    }

    @Override
    public String queryAgentNameByNo(String agentNo) {
        return agentInfoDao.queryAgentNameByNo(agentNo);
    }
}
