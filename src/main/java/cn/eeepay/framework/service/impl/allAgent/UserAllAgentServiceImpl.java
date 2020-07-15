package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.UserAllAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.allAgent.CountSet;
import cn.eeepay.framework.model.allAgent.UserAllAgent;
import cn.eeepay.framework.model.allAgent.UserAllAgentCard;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.allAgent.UserAllAgentService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/7/12/012.
 */
@Service("userAllAgentService")
public class UserAllAgentServiceImpl implements UserAllAgentService {

    private static final Logger log = LoggerFactory.getLogger(UserAllAgentServiceImpl.class);

    @Resource
    private UserAllAgentDao userAllAgentDao;

    @Resource
    private AgentInfoService agentInfoService;

    @Override
    public List<UserAllAgent> selectAllList(UserAllAgent user, Page<UserAllAgent> page) {
        List<UserAllAgent> list=userAllAgentDao.selectAllList(user,page);
        UserAllAgent user2=new UserAllAgent();
        user2.setUserNode(user.getUserNode());
        user2.setUserCode(user.getUserCode());
        user2.setTransStartTime(user.getTransStartTime());
        user2.setTransEndTime(user.getTransEndTime());
        for (UserAllAgent u:list){

            u.setMobile(StringUtil.sensitiveInformationHandle(u.getMobile(),0));
            u.setIdCardNo(StringUtil.sensitiveInformationHandle(u.getIdCardNo(),1));

            user2.setUserCode(u.getUserCode());
            user2.setUserNode(u.getUserNode());
            u.setSumActivationMer(userAllAgentDao.selectSumActivationMer(u.getUserNode()));
            String sumMerTransSharestr=userAllAgentDao.selectSumMerTransShare(user2);
            String sumMerUserTransSharestr=userAllAgentDao.selectSumMerUserTransShare(user2);
            BigDecimal sumMerTransShare=StringUtil.isEmpty(sumMerTransSharestr)?BigDecimal.ZERO:new BigDecimal(sumMerTransSharestr);
            BigDecimal sumMerUserTransShare=StringUtil.isEmpty(sumMerUserTransSharestr)?BigDecimal.ZERO:new BigDecimal(sumMerUserTransSharestr);
            u.setSumMerTransShare(sumMerTransShare.toString());
            u.setSumMerUserTransShare(sumMerUserTransShare.subtract(sumMerTransShare).toString());
        }
        return list;
    }

    @Override
    public List<UserAllAgent> importDetailSelect(UserAllAgent user) {
        List<UserAllAgent> list=userAllAgentDao.importDetailSelect(user);
        UserAllAgent user2=new UserAllAgent();
        user2.setUserNode(user.getUserNode());
        user2.setUserCode(user.getUserCode());
        user2.setTransStartTime(user.getTransStartTime());
        user2.setTransEndTime(user.getTransEndTime());
        for (UserAllAgent u:list){

            u.setMobile(StringUtil.sensitiveInformationHandle(u.getMobile(),0));
            u.setIdCardNo(StringUtil.sensitiveInformationHandle(u.getIdCardNo(),1));

            user2.setUserCode(u.getUserCode());
            user2.setUserNode(u.getUserNode());
            u.setSumActivationMer(userAllAgentDao.selectSumActivationMer(u.getUserNode()));
            String sumMerTransSharestr=userAllAgentDao.selectSumMerTransShare(user2);
            String sumMerUserTransSharestr=userAllAgentDao.selectSumMerUserTransShare(user2);
            BigDecimal sumMerTransShare=StringUtil.isEmpty(sumMerTransSharestr)?BigDecimal.ZERO:new BigDecimal(sumMerTransSharestr);
            BigDecimal sumMerUserTransShare=StringUtil.isEmpty(sumMerUserTransSharestr)?BigDecimal.ZERO:new BigDecimal(sumMerUserTransSharestr);
            u.setSumMerTransShare(sumMerTransShare.toString());
            u.setSumMerUserTransShare(sumMerUserTransShare.subtract(sumMerTransShare).toString());
        }
        return list;
    }

    @Override
    public void importDetail(List<UserAllAgent> list, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "盟主管理列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("userCode",null);
            maps.put("agentNo",null);
            maps.put("realName",null);
            maps.put("nickName",null);
            maps.put("sumMer",null);
            maps.put("sumUser",null);
            maps.put("sumActivationMer",null);
            maps.put("sumMerTransShare",null);
            maps.put("sumMerUserTransShare",null);
            maps.put("mobile",null);
            maps.put("grade",null);
            maps.put("gradeStr",null);
            maps.put("gradeRate",null);
            maps.put("vipShareRatio",null);
            maps.put("shareRatio",null);
            maps.put("brandName",null);
            maps.put("parentId",null);
            maps.put("parentRealName",null);
            maps.put("twoUserCode",null);
            maps.put("oneUserCode",null);
            maps.put("idCardNo",null);
            maps.put("userType",null);
            maps.put("idCardNoState",null);
            maps.put("createTime",null);
            data.add(maps);
        }else{
            Map<String, String> idCardNoStateMap=new HashMap<String, String>();
            idCardNoStateMap.put("0","未完成认证");
            idCardNoStateMap.put("1","已完成认证");
            Map<String, String> userTypeMap=new HashMap<String, String>();
            userTypeMap.put("1","机构");
            userTypeMap.put("2","大盟主");
            userTypeMap.put("3","盟主");

            Map<String, String> gradeMap=new HashMap<String, String>();
            gradeMap.put("0","普通盟主");
            gradeMap.put("1","黄金盟主");
            gradeMap.put("2","铂金盟主");
            gradeMap.put("3","黑金盟主");
            gradeMap.put("4","钻石盟主");


            for (UserAllAgent or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("userCode",or.getUserCode()==null?"":or.getUserCode());
                maps.put("agentNo",or.getSelectAgentNo()==null?"":or.getSelectAgentNo());
                maps.put("realName",or.getRealName()==null?"":or.getRealName());
                maps.put("nickName",or.getNickName()==null?"":or.getNickName());
                maps.put("sumMer",or.getSumMer()==null?"":or.getSumMer().toString());
                maps.put("sumUser",or.getSumUser()==null?"":or.getSumUser().toString());
                maps.put("sumActivationMer",or.getSumActivationMer()==null?"":or.getSumActivationMer());
                maps.put("sumMerTransShare",or.getSumMerTransShare()==null?"":or.getSumMerTransShare());
                maps.put("sumMerUserTransShare",or.getSumMerUserTransShare()==null?"":or.getSumMerUserTransShare());
                maps.put("mobile",or.getMobile()==null?"":or.getMobile());
                maps.put("grade",gradeMap.get(or.getGrade()==null?"":or.getGrade().toString()));
                maps.put("gradeStr",or.getGradeStr()==null?"":or.getGradeStr());
                if(or.getGradeStr()!=null){
                    maps.put("gradeRate",or.getGradeStr().substring(or.getGradeStr().indexOf("万分之")+3,or.getGradeStr().length()-1));
                }else{
                    maps.put("gradeRate","");
                }
                maps.put("vipShareRatio",or.getVipShareRatio()==null?"":or.getVipShareRatio());
                maps.put("shareRatio",or.getShareRatio()==null?"":or.getShareRatio());
                maps.put("brandName",or.getBrandName()==null?"":or.getBrandName());
                maps.put("parentId",or.getParentId()==null?"":or.getParentId());
                maps.put("parentRealName",or.getParentRealName()==null?"":or.getParentRealName());
                maps.put("twoUserCode",or.getTwoUserCode()==null?"":or.getTwoUserCode());
                maps.put("oneUserCode",or.getOneUserCode()==null?"":or.getOneUserCode());
                maps.put("idCardNo",or.getIdCardNo()==null?"":or.getIdCardNo());
                maps.put("userType",userTypeMap.get(or.getUserType()==null?"":or.getUserType().toString()));
                maps.put("idCardNoState",idCardNoStateMap.get(or.getIdCardNoState()==null?"":or.getIdCardNoState().toString()));
                maps.put("createTime",or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"userCode","agentNo","realName","nickName","sumMer","sumUser",
                "sumActivationMer","sumMerTransShare","sumMerUserTransShare","mobile",
                "grade","gradeStr","gradeRate","vipShareRatio","shareRatio",
                "brandName","parentId","parentRealName","twoUserCode","oneUserCode","idCardNo",
                "userType","idCardNoState","createTime"
        };
        String[] colsName = new String[]{"盟主编号","代理商编号","盟主姓名","昵称","直营商户(家)","推广直属盟主数",
                "盟友商户激活数","直属商户交易量(元)","直属盟友团队交易量(元)","注册手机",
                "盟主身份","标准分润等级","标准分润比例(万分之)","VIP分润比例(万分之)","荣耀奖金分润比例",
                "所属品牌","上级盟主编号","上级盟主姓名","大盟主编号","机构编号","身份证号",
                "用户类型","是否认证","注册日期"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出盟主管理列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public UserAllAgent getUserAllAgent(int id) {
        UserAllAgent user=userAllAgentDao.getUserAllAgent(id);
        if(user!=null){
            List<UserAllAgentCard> cardList=userAllAgentDao.getUserCard(user.getUserCode(),1);
            if(cardList!=null&&cardList.size()>0){
                user.setCard(cardList.get(0));
            }
            getUserDetail(user);

            if(user.getOneAgentNo()!=null){
                AgentInfo agentInfo =agentInfoService.getAgentByNo(user.getOneAgentNo());
                if(agentInfo!=null){
                    user.setOneAgentName(agentInfo.getAgentName());
                }
            }
            if(user.getTwoAgentNo()!=null){
                AgentInfo agentInfo =agentInfoService.getAgentByNo(user.getTwoAgentNo());
                if(agentInfo!=null){
                    user.setTwoAgentName(agentInfo.getAgentName());
                }
            }
        }
        return user;
    }

    @Override
    public List<UserAllAgent> getOrgList(String brandCodes, int userType) {
        List<UserAllAgent> list=userAllAgentDao.getOrgList(brandCodes,userType);
        if(list!=null&&list.size()>0){
            for(UserAllAgent user:list){
                if(user.getOneAgentNo()!=null){
                    AgentInfo agentInfo =agentInfoService.getAgentByNo(user.getOneAgentNo());
                    if(agentInfo!=null){
                        user.setOneAgentName(agentInfo.getAgentName());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public CountSet selectAllSum(UserAllAgent user, Page<UserAllAgent> page) {
        return userAllAgentDao.selectAllSum(user,page);
    }

    private  void getUserListDetail(List<UserAllAgent> list){
        if(list!=null&&list.size()>0){
            for(UserAllAgent item:list){
                getUserDetail(item);
            }
        }
    }
    private void getUserDetail(UserAllAgent user){
        if(user!=null){
            user.setSumMer(userAllAgentDao.sumMer(user.getUserCode()));
            user.setSumUser(userAllAgentDao.sumUser(user.getUserCode()));
        }
    }

    @Override
    public int saveUserCardAllAgent(AgentInfo agent){
        int row=0;
        String userCode=userAllAgentDao.getUserAllAgentByAgentNo(agent.getAgentNo());
        if(userCode!=null&&!"".equals(userCode)){
            UserAllAgent userAllAgent=userAllAgentDao.selectUser(userCode);
            if(userAllAgent!=null){
                if(!userAllAgent.getMobile().equals(agent.getMobilephone())){
                    userAllAgent.setMobile(agent.getMobilephone());
                    userAllAgent.setPwd(new Md5PasswordEncoder().encodePassword("123456",agent.getMobilephone()));
                }
                userAllAgent.setRealName(agent.getAccountName());
                userAllAgent.setIdCardNo(agent.getIdCardNo());
                userAllAgentDao.updateUserAllAgent(userAllAgent);
            }
            UserAllAgentCard userCard=userAllAgentDao.getUserCardAllAgentByUserCode(userCode);
            if(userCard==null){
                userCard=new UserAllAgentCard();
                userCard.setUserCode(userCode);
                userCard.setBankName(agent.getBankName());
                userCard.setBankBranchName(agent.getBankName());
                userCard.setAccount(agent.getAccountNo());
                userCard.setMobile(agent.getMobilephone());
                userCard.setCnaps(agent.getCnapsNo());
                row=userAllAgentDao.insertUserCardAllAgent(userCard);
            }else{
                userCard.setId(userCard.getId());
                userCard.setUserCode(userCode);
                userCard.setBankName(agent.getBankName());
                userCard.setBankBranchName(agent.getBankName());
                userCard.setAccount(agent.getAccountNo());
                userCard.setMobile(agent.getMobilephone());
                userCard.setCnaps(agent.getCnapsNo());
                row=userAllAgentDao.updateUserCardAllAgent(userCard);
            }
        }
        return row;
    }

    @Override
    public int saveUserAllAgent(UserAllAgent user){
        int row=0;
        UserAllAgent oldUser=userAllAgentDao.selectUser(user.getUserCode());
        UserAllAgentCard card=user.getCard();
        if(oldUser.getUserType()!=3){
            throw new RuntimeException("只能修改盟主信息");
        }
        if(user.getRealName()==null||"".equals(user.getRealName())){
            throw new RuntimeException("姓名不能为空");
        }
        if(user.getMobile()==null||"".equals(user.getMobile())){
            throw new RuntimeException("注册手机不能为空");
        }else{
            if(!oldUser.getMobile().equals(user.getMobile())){
                user.setPwd(new Md5PasswordEncoder().encodePassword(user.getMobile().substring(user.getMobile().length()-6,user.getMobile().length()),user.getMobile()));
            }
        }
        if(user.getIdCardNo()==null||"".equals(user.getIdCardNo())){
            throw new RuntimeException("身份证号码不能为空");
        }
        if(card.getAccount()==null||"".equals(card.getAccount())){
            throw new RuntimeException("银行卡号不能为空");
        }
        if(card.getBankName()==null||"".equals(card.getBankName())){
            throw new RuntimeException("开户银行不能为空");
        }
        if(card.getAddress()==null||"".equals(card.getAddress())){
            throw new RuntimeException("开户地区不能为空");
        }
        if(card.getMobile()==null||"".equals(card.getMobile())){
            throw new RuntimeException("预留手机不能为空");
        }
        if(userAllAgentDao.getUserCardAllAgentCount(user)>0){
            throw new RuntimeException("注册手机或身份证号码已存在");
        }
        row=userAllAgentDao.updateUserAllAgentBySave(user);
        card.setUserCode(user.getUserCode());
        UserAllAgentCard userCard=userAllAgentDao.getUserCardAllAgentByUserCode(user.getUserCode());
        if(userCard==null){
            row=userAllAgentDao.insertUserCardAllAgentBySave(card);
        }else{
            card.setId(userCard.getId());
            row=userAllAgentDao.updateUserCardAllAgent(card);
        }
        return row;
    }

    @Override
    public List<UserAllAgent> selectDividedAdjustDetail(UserAllAgent user) {
        List<UserAllAgent> list=userAllAgentDao.selectDividedAdjustDetail(user);
        return list;
    }

    @Override
    public List<UserAllAgent> selectDividedAdjustDetailList(UserAllAgent user, Page<UserAllAgent> page){
        List<UserAllAgent> list=userAllAgentDao.selectDividedAdjustDetailList(user, page);
        return list;
    }

    @Override
    public void importDividedAdjustDetail(List<UserAllAgent> list, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "分润比例调整记录"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("userCode",null);
            maps.put("realName",null);
            maps.put("oneUserCode",null);
            maps.put("twoUserCode",null);
            maps.put("gradeRate",null);
            maps.put("shareRatio",null);
            maps.put("preRate",null);
            maps.put("afterRate",null);
            maps.put("createTime",null);
            data.add(maps);
        }else{
            for (UserAllAgent or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("userCode",or.getUserCode()==null?"":or.getUserCode());
                maps.put("realName",or.getRealName()==null?"":or.getRealName());
                maps.put("oneUserCode",or.getOneUserCode()==null?"":or.getOneUserCode());
                maps.put("twoUserCode",or.getTwoUserCode()==null?"":or.getTwoUserCode());
                maps.put("gradeRate",or.getGradeRate()==null?"":"Lv."+or.getGradeRate());
                maps.put("shareRatio",or.getShareRatio()==null?"":"万分之"+or.getShareRatio());
                maps.put("preRate",or.getPreRate()==null?"":"万分之"+or.getPreRate());
                maps.put("afterRate",or.getAfterRate()==null?"":"万分之"+or.getAfterRate());
                maps.put("createTime",or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"userCode","realName","oneUserCode","twoUserCode",
                "gradeRate","shareRatio","preRate","afterRate","createTime"};
        String[] colsName = new String[]{"盟主编号","盟主姓名","所属机构编号","所属大盟主编号",
                "当前分润等级","当前分润比例","调整前分润比例","调整后分润比例","调整日期"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出分润比例调整记录失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    public List<UserAllAgent> selectOneUserCodeList(){
        return userAllAgentDao.selectOneUserCodeList();
    }

    @Override
    public UserAllAgent getUserOne(String userCode) {
        return userAllAgentDao.getUserOne(userCode);
    }

    @Override
    public List<UserAllAgent> getUserByStr(String str,String level) {
        return userAllAgentDao.getUserByStr(str,level);
    }

    @Override
    public int resetPassword(String userCode) {
        UserAllAgent user=userAllAgentDao.selectUser(userCode);
        if(user!=null){
            user.setPwd(new Md5PasswordEncoder().encodePassword(user.getMobile().substring(user.getMobile().length()-6,user.getMobile().length()),user.getMobile()));
        }
        return userAllAgentDao.updateUserAllAgentBySave(user);
    }
}
