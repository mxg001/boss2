package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateNoticeDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateNotice;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateNoticeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 公告service实现类
 */
@Service("exchangeActivateNoticeService")
public class ExchangeActivateNoticeServiceImpl implements ExchangeActivateNoticeService {

    @Resource
    private ExchangeActivateNoticeDao exchangeActivateNoticeDao;

    @Override
    public List<ExchangeActivateNotice> selectAllList(ExchangeActivateNotice notice, Page<ExchangeActivateNotice> page) {
        return exchangeActivateNoticeDao.selectAllList(notice,page);
    }

    @Override
    public int addNotice(ExchangeActivateNotice notice) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notice.setCreateUser(principal.getUsername());
        return exchangeActivateNoticeDao.addNotice(notice);
    }

    @Override
    public ExchangeActivateNotice getNotice(long id) {
        ExchangeActivateNotice notice =exchangeActivateNoticeDao.getNotice(id);
        if(notice!=null&&notice.getOemNoSet()!=null){
            String[] strs=notice.getOemNoSet().split(",");
            if(strs!=null&&strs.length>0){
                List<String> list=new ArrayList<String>();
                for(int i=0;i<strs.length;i++){
                    list.add(strs[i]);
                }
                notice.setOemNoList(list);
            }
        }
        return notice;
    }

    @Override
    public int updateNotice(ExchangeActivateNotice notice) {
        int num=exchangeActivateNoticeDao.updateNotice(notice);
        return num;
    }

    @Override
    public int updateNoticeState(long id, int state) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //状态:0删除1正常2待下发3置顶
        int num=0;
        if(state==0){//删除
            num=exchangeActivateNoticeDao.updateNoticeState(id,"0");
        }else if(state==1){//下发
            num=exchangeActivateNoticeDao.sendNotice(id,"1",principal.getUsername());
        }else if(state==2){//回收
            num=exchangeActivateNoticeDao.sendColseNotice(id,"2");
        }else if(state==3){//置顶
            num=exchangeActivateNoticeDao.updateNoticeState(id,"3");
        }else if(state==4){//取消置顶
            num=exchangeActivateNoticeDao.updateNoticeState(id,"1");
        }
        return num;
    }
}
