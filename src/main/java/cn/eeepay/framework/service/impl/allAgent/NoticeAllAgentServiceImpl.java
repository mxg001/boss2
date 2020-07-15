package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.NoticeAllAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.allAgent.NoticeAllAgent;
import cn.eeepay.framework.model.allAgent.UserAllAgent;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.allAgent.NoticeAllAgentService;
import cn.eeepay.framework.service.allAgent.UserAllAgentService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 公告service实现类
 */
@Service("noticeAllAgentService")
public class NoticeAllAgentServiceImpl implements NoticeAllAgentService {
    private static final Logger log = LoggerFactory.getLogger(NoticeAllAgentServiceImpl.class);

    @Resource
    private NoticeAllAgentDao noticeAllAgentDao;
    @Resource
    private SysDictService sysDictService;

    @Resource
    private UserAllAgentService userAllAgentService;

    @Override
    public List<NoticeAllAgent> selectAllList(NoticeAllAgent notice, Page<NoticeAllAgent> page) {
        return noticeAllAgentDao.selectAllList(notice,page);
    }

    @Override
    public int addNotice(NoticeAllAgent notice) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notice.setCreateUser(principal.getUsername());
        return noticeAllAgentDao.addNotice(notice);
    }

    @Override
    public NoticeAllAgent getNotice(long id) {
        NoticeAllAgent notice =noticeAllAgentDao.getNotice(id);
        if(notice!=null){
            if(notice.getOemNoSet()!=null){
                notice.setOemNoList(analysisString(notice.getOemNoSet()));
            }
            if(notice.getOrgSet()!=null){
                notice.setOrgList(analysisString(notice.getOrgSet()));
            }
            if(notice.getImg()!=null&&!"".equals(notice.getImg())){
                notice.setImgUrl(CommonUtil.getImgUrlAgent(notice.getImg()));
            }
            if(notice.getHomeImg()!=null&&!"".equals(notice.getHomeImg())){
                notice.setHomeImgUrl(CommonUtil.getImgUrlAgent(notice.getHomeImg()));
            }
        }
        return notice;
    }
    private List<String> analysisString(String str){
        List<String> list=new ArrayList<String>();
        if(str!=null&&!"".equals(str)){
            String[] strs=str.split(",");
            if(strs!=null&&strs.length>0){
                for(int i=0;i<strs.length;i++){
                    list.add(strs[i]);
                }
            }
        }
        return list;
    }

    @Override
    public int updateNotice(NoticeAllAgent notice) {
        int num=noticeAllAgentDao.updateNotice(notice);
        return num;
    }

    @Override
    public int updateNoticeState(long id, int state) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //状态:0删除1正常2待下发3置顶
        int num=0;
        if(state==0){//删除
            num=noticeAllAgentDao.updateNoticeState(id,"0");
        }else if(state==1){//下发
            num=noticeAllAgentDao.sendNotice(id,"1",principal.getUsername());
            allAgentNoticePush(id);
        }else if(state==2){//回收
            num=noticeAllAgentDao.sendColseNotice(id,"2");
        }else if(state==3){//置顶
            num=noticeAllAgentDao.updateNoticeState(id,"3");
        }else if(state==4){//取消置顶
            num=noticeAllAgentDao.updateNoticeState(id,"1");
        }
        return num;
    }

    @Override
    public int updateNoticeHome(long id,int homeStatus){
        return noticeAllAgentDao.updateNoticeHome(id,homeStatus);
    }

    @Override
    public int updateUserCodeSet(long id, String userCodeSet, Map<String, Object> msg) {
        if(StringUtils.isNotBlank(userCodeSet)){
            String[] strs=userCodeSet.split(",");
            if(strs!=null&&strs.length>0){
                for(String str:strs){
                    UserAllAgent user=userAllAgentService.getUserOne(str);
                    if(user==null){
                        msg.put("status", false);
                        msg.put("msg", "预览公告操作失败,盟主编号:"+str+"不存在!");
                        return 0;
                    }
                }
                int num=noticeAllAgentDao.updateUserCodeSet(id,userCodeSet);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "预览公告操作成功,请登录APP查看!");
                    return 1;
                }
            }
        }else{
            int num=noticeAllAgentDao.updateUserCodeSet(id,null);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功,已清空预览效果!");
                return 1;
            }
        }
        msg.put("status", false);
        msg.put("msg", "预览公告操作失败!");
        return 0;
    }

    /**
     * 极光推送
     * @param id
     * @return
     */
    public String allAgentNoticePush(long id) {
        String url=sysDictService.getValueByKey("ALLAGENT_SERVICE_URL");
        url+="/activity/noticePush";
        Map<String, String> claims = new HashMap<>();
        claims.put("noticeId", id+"");//必传
        String accountMsg = ClientInterface.httpPost(url, claims);
        log.info("url:{}，noticeId:{}，response:{}",url,id,accountMsg);
        return accountMsg;
    }
}
