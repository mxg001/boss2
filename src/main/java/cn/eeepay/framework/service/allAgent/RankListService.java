package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.RankList;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/6/006.
 * @author  liuks
 * 排行榜列表service
 */
public interface RankListService {

    List<RankList> selectAllList(RankList info, Page<RankList> page);

    List<RankList> importDetailSelect(RankList info);

    void importDetail(List<RankList> list, HttpServletResponse response) throws Exception;

    Map<String,Object> selectAllSum(RankList info, Page<RankList> page);
}
