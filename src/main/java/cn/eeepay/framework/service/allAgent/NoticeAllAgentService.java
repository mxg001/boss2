package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.NoticeAllAgent;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 公告service
 */
public interface NoticeAllAgentService {

    List<NoticeAllAgent> selectAllList(NoticeAllAgent notice, Page<NoticeAllAgent> page);

    int addNotice(NoticeAllAgent notice);

    NoticeAllAgent getNotice(long id);

    int updateNotice(NoticeAllAgent notice);

    int updateNoticeState(long id, int state);

    int updateNoticeHome(long id,int homeStatus);

    int updateUserCodeSet(long id, String userCodeSet, Map<String, Object> msg);
}
