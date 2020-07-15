package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.AwardParam;
import cn.eeepay.framework.model.allAgent.AwardParamLadder;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6/006.
 * @author liuks
 * 奖项参数设置service
 */
public interface AwardParamService {

    List<AwardParam> selectAllList(AwardParam oem, Page<AwardParam> page);

    int addAwardParam(AwardParam oem, List<AwardParamLadder> tradeList,List<AwardParamLadder> crownList,List<AwardParamLadder> vipList);

    AwardParam getAwardParam(int id);

    AwardParam getAwardParamOem(int id);

    int updateAwardParam(AwardParam oem,List<AwardParamLadder> tradeList,List<AwardParamLadder> crownList,List<AwardParamLadder> vipList);

    int updateAwardParamOem(AwardParam oem);

    boolean checkAwardParam(AwardParam oem);

    List<AwardParam> getOemList();

    int deleteOwnerImgs(int id,String ownerImgs);

    int deleteMerImgs(int id,String merImgs);

    int deleteLeaImgs(int id,String leaderboardBgi);
}
