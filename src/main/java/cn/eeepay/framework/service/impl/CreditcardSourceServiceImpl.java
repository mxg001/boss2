package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.CreditcardSourceDao;
import cn.eeepay.framework.daoSuperbank.OrgSourceConfDao;
import cn.eeepay.framework.daoSuperbank.SequenceDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditcardSource;
import cn.eeepay.framework.model.OrgSourceConf;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.CreditcardSourceService;
import cn.eeepay.framework.service.OrgSourceConfigService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("creditcardSourceService")
@Transactional
public class CreditcardSourceServiceImpl implements CreditcardSourceService {

    private Logger log = LoggerFactory.getLogger(CreditcardSourceServiceImpl.class);

    @Resource
    private CreditcardSourceDao creditcardSourceDao;

    @Resource
    private SequenceDao sequenceDao;
    
    @Resource
	private OrgSourceConfigService orgSourceConfigService;
    
    @Resource
    private OrgSourceConfDao orgSourceConfDao;

    @Override
    public List<CreditcardSource> selectList(CreditcardSource baseInfo, Page<CreditcardSource> page) {
        creditcardSourceDao.selectList(baseInfo, page);
        List<CreditcardSource> list = page.getResult();
        if(list != null && list.size() > 0){
            for (CreditcardSource info: list){
            	/*OrgSourceConf conf = orgSourceConfDao.getBankersOrgSource(info.getId(),"1");
    			if(null!=conf&&!"".equals(conf.getShowOrder())){
    				info.setShowOrder(conf.getShowOrder());
    			}*/
                if(StringUtils.isNotBlank(info.getShowLogo())){
                    info.setShowLogoUrl(CommonUtil.getImgUrlAgent(info.getShowLogo()));
                }
                /*if(StringUtils.isNotBlank(info.getActivityImage())){
                    info.setActivityImageUrl(CommonUtil.getImgUrlAgent(info.getActivityImage()));
                }*/
                if(StringUtils.isNotBlank(info.getApplyCardGuideImg())){
                    info.setApplyCardGuideImgUrl(CommonUtil.getImgUrlAgent(info.getApplyCardGuideImg()));
                }
                if(StringUtils.isNotBlank(info.getStatus()) && "on".equals(info.getStatus())){
                    info.setStatusInt(1);
                } else {
                    info.setStatusInt(0);
                }
            }
        }
        page.setResult(list);
        return list;
    }

    @Override
    public CreditcardSource selectDetail(Long id) {
        CreditcardSource info = creditcardSourceDao.selectDetail(id);
        if(info != null){
            if(StringUtils.isNotBlank(info.getShowLogo())){
                info.setShowLogoUrl(CommonUtil.getImgUrlAgent(info.getShowLogo()));
            }
            if(StringUtils.isNotBlank(info.getSpecialImage())){
                info.setSpecialImageUrl(CommonUtil.getImgUrlAgent(info.getSpecialImage()));
            }
            if(StringUtils.isNotBlank(info.getH5Link())){
                info.setH5LinkUrl(CommonUtil.getImgUrlAgent(info.getH5Link()));
            }
            /*if(StringUtils.isNotBlank(info.getActivityImage())){
                info.setActivityImageUrl(CommonUtil.getImgUrlAgent(info.getActivityImage()));
            }*/
            if(StringUtils.isNotBlank(info.getApplyCardGuideImg())){
                info.setApplyCardGuideImgUrl(CommonUtil.getImgUrlAgent(info.getApplyCardGuideImg()));
            }
        }
        return info;
    }

    @Override
    public int addBank(CreditcardSource info) {
        //校验银行编码，必输，且唯一
        String bankCode = info.getCode();
        if(StringUtils.isBlank(bankCode)){
            throw new BossBaseException("银行编码不能为空");
        }
//        int checkCodeNum = creditcardSourceDao.selectCodeExists(bankCode);
//        if(checkCodeNum > 0){
//            throw new BossBaseException("银行编码已存在");
//        }
        //校验银行别称，必输，且唯一
        String bankNickName = info.getBankNickName();
        if(StringUtils.isBlank(bankNickName)){
            throw new BossBaseException("银行别称不能为空");
        }
        int checkNickNum = creditcardSourceDao.selectNickExists(bankNickName);
        if(checkNickNum > 0){
            throw new BossBaseException("银行别称已存在");
        }
        //校验显示顺序，必输，且唯一
        String order = info.getShowOrder();
        if(StringUtils.isBlank(order)){
            throw new BossBaseException("显示顺序不能为空");
        }
        int checkOrderNum = creditcardSourceDao.selectOrderExists(order);
        if(checkOrderNum > 0){
            throw new BossBaseException("显示顺序不能重复");
        }
        if(StringUtils.isBlank(info.getShowLogo())){
            throw new BossBaseException("LOGO不能为空");
        }
        if(StringUtils.isBlank(info.getSource())){
            throw new BossBaseException("来源不能为空");
        }
        if(StringUtils.isBlank(info.getSlogan())){
            throw new BossBaseException("宣传语不能为空");
        }
        if(StringUtils.isBlank(info.getCardBonus()+"")||"null".equals(info.getCardBonus()+"")){
            throw new BossBaseException("发卡奖金不能为空");
        }
        if(StringUtils.isBlank(info.getFirstBrushBonus()+"")||"null".equals(info.getFirstBrushBonus()+"")){
            throw new BossBaseException("首刷奖金不能为空");
        }
        if(StringUtils.isBlank(info.getApproveStatus()+"")||"null".equals(info.getApproveStatus()+"")){
            throw new BossBaseException("是否秒批不能为空");
        }
        if(StringUtils.isBlank(info.getAutoShareStatus()+"")||"null".equals(info.getAutoShareStatus()+"")){
            throw new BossBaseException("导入数据是否自动分润发卡奖金不能为空");
        }
        if(StringUtils.isBlank(info.getBatchStatus()+"")||"null".equals(info.getBatchStatus()+"")){
            throw new BossBaseException("查询是否秒结不能为空");
        }
        if(StringUtils.isBlank(info.getCardActiveStatus())){
            throw new BossBaseException("发卡奖金是否要求卡片激活不能为空");
        }
        if(StringUtils.isBlank(info.getAccessMethods())){
            throw new BossBaseException("接入方式不能为空");
        }
        if("1".equals(info.getAccessMethods())) {
        	if(StringUtils.isBlank(info.getSendLink())){
                throw new BossBaseException("申请H5链接不能为空");
            }
        }else if("0".equals(info.getAccessMethods())) {
        	if(StringUtils.isBlank(info.getSendApiLink())){
                throw new BossBaseException("申请API接口不能为空");
            }
        }
        if(StringUtils.isBlank(info.getApplyCardGuideImg())){
            throw new BossBaseException("办卡攻略图片不能为空");
        }
        
        
        //校验特别推荐位置，如果存在，则唯一
        String specialPosition = info.getSpecialPosition();
        if(StringUtils.isNotBlank(specialPosition)){
            int checkSpecialNum = creditcardSourceDao.selectSpecialExists(specialPosition);
            if(checkSpecialNum > 0){
                throw new BossBaseException("特别推荐位置不能重复");
            }
        }
        //导入匹配规则编码，如果存在，则唯一
        String ruleCode = info.getRuleCode();
        if(StringUtils.isNotBlank(ruleCode)){
            int checkRuleNum = creditcardSourceDao.selectRuleCodeExists(ruleCode);
            if(checkRuleNum > 0){
                throw new BossBaseException("导入匹配规则编码不能重复");
            }
        }
//        String code = sequenceDao.getValue("BANK_CODE");
//        info.setCode(code);
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        info.setUpdateBy(loginInfo.getId().toString());
        info.setCreateTime(new Date());
        info.setStatus("off");//默认不上架
        creditcardSourceDao.insert(info);
        //添加组织信用卡配置
        CreditcardSource creditcardSource = creditcardSourceDao.selectByNick(bankNickName);
        orgSourceConfigService.addOrgConf(creditcardSource.getId(),creditcardSource.getShowOrder(),"1");
        return info.getId().intValue();
    }

    @Override
    public int updateBank(CreditcardSource info) {
        Long id = info.getId();
        if(id == null){
            throw new BossBaseException("参数非法");
        }
        //校验银行编码，必输，且唯一
        String bankCode = info.getCode();
        if(StringUtils.isBlank(bankCode)){
            throw new BossBaseException("银行编码不能为空");
        }
//        int checkCodeNum = creditcardSourceDao.selectCodeIdExists(bankCode, id);
//        if(checkCodeNum > 0){
//            throw new BossBaseException("银行编码已存在");
//        }
        //校验银行别称，必输，且唯一
        String bankNickName = info.getBankNickName();
        if(StringUtils.isBlank(bankNickName)){
            throw new BossBaseException("银行别称不能为空");
        }
        int checkNickNum = creditcardSourceDao.selectNickIdExists(bankNickName, id);
        if(checkNickNum > 0){
            throw new BossBaseException("银行别称已存在");
        }
        //校验显示顺序，必输，且唯一
        String order = info.getShowOrder();
        /*if(StringUtils.isBlank(order)){
            throw new BossBaseException("显示顺序不能为空");
        }
        int checkOrderNum = creditcardSourceDao.selectOrderIdExists(order, id);
        if(checkOrderNum > 0){
            throw new BossBaseException("显示顺序不能重复");
        }*/
        if(StringUtils.isBlank(info.getShowLogo())){
            throw new BossBaseException("LOGO不能为空");
        }
        if(StringUtils.isBlank(info.getSource())){
            throw new BossBaseException("来源不能为空");
        }
        if(StringUtils.isBlank(info.getSlogan())){
            throw new BossBaseException("宣传语不能为空");
        }
        if(StringUtils.isBlank(info.getCardBonus()+"")||"null".equals(info.getCardBonus()+"")){
            throw new BossBaseException("发卡奖金不能为空");
        }
        if(StringUtils.isBlank(info.getFirstBrushBonus()+"")||"null".equals(info.getFirstBrushBonus()+"")){
            throw new BossBaseException("首刷奖金不能为空");
        }
        if(StringUtils.isBlank(info.getApproveStatus()+"")||"null".equals(info.getApproveStatus()+"")){
            throw new BossBaseException("是否秒批不能为空");
        }
        if(StringUtils.isBlank(info.getAutoShareStatus()+"")||"null".equals(info.getAutoShareStatus()+"")){
            throw new BossBaseException("导入数据是否自动分润发卡奖金不能为空");
        }
        if(StringUtils.isBlank(info.getBatchStatus()+"")||"null".equals(info.getBatchStatus()+"")){
            throw new BossBaseException("查询是否秒结不能为空");
        }
        if(StringUtils.isBlank(info.getCardActiveStatus())){
            throw new BossBaseException("发卡奖金是否要求卡片激活不能为空");
        }
        if(StringUtils.isBlank(info.getAccessMethods())){
            throw new BossBaseException("接入方式不能为空");
        }
        if("1".equals(info.getAccessMethods())) {
        	if(StringUtils.isBlank(info.getSendLink())){
                throw new BossBaseException("申请H5链接不能为空");
            }
        }else if("0".equals(info.getAccessMethods())) {
        	if(StringUtils.isBlank(info.getSendApiLink())){
                throw new BossBaseException("申请API接口不能为空");
            }
        }
        if(StringUtils.isBlank(info.getApplyCardGuideImg())){
            throw new BossBaseException("办卡攻略图片不能为空");
        }
        //校验特别推荐位置，如果存在，则唯一
        String specialPosition = info.getSpecialPosition();
        if(StringUtils.isNotBlank(specialPosition)){
            int checkSpecialNum = creditcardSourceDao.selectSpecialIdExists(specialPosition, id);
            if(checkSpecialNum > 0){
                throw new BossBaseException("特别推荐位置不能重复");
            }
        }
        //校验导入匹配规则编码，如果存在，则唯一
        String ruleCode = info.getRuleCode();
        if(StringUtils.isNotBlank(ruleCode)){
            int checkRuleNum = creditcardSourceDao.selectRuleCodeIdExists(ruleCode, id);
            if(checkRuleNum > 0){
                throw new BossBaseException("导入匹配规则编码不能重复");
            }
        }
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        info.setUpdateBy(loginInfo.getId().toString());
        info.setUpdateTime(new Date());
        return creditcardSourceDao.updateBank(info);
    }

    @Override
    public int updateBankStatus(CreditcardSource info) {
        int num =  creditcardSourceDao.updateBankStatus(info);
        /*if("off".equals(info.getStatus())){
        	orgSourceConfDao.updateStatusBySourceId("off", String.valueOf(info.getId()));
        }*/
        return num;
    }

	@Override
	public List<CreditcardSource> getAllBanks() {
		return creditcardSourceDao.getAllBanks();
	}
}
