package cn.eeepay.framework.service.impl.allAgent;


import cn.eeepay.framework.daoAllAgent.AwardParamDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.AwardParam;
import cn.eeepay.framework.model.allAgent.AwardParamDiamonds;
import cn.eeepay.framework.model.allAgent.AwardParamLadder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.allAgent.AwardParamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/6/006.
 * @author liuks
 * oem service实现类
 */
@Service("awardParamService")
public class AwardParamServiceImpl implements AwardParamService {

    @Resource
    private AwardParamDao awardParamDao;

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<AwardParam> selectAllList(AwardParam oem, Page<AwardParam> page) {
        return awardParamDao.selectAllList(oem,page);
    }

    @Override
    public int addAwardParam(AwardParam oem,List<AwardParamLadder> tradeList,
                             List<AwardParamLadder> crownList,
                             List<AwardParamLadder> vipList) {
        Map<String, String> oemMap=sysDictService.selectMapByKey("AGENT_OEM");//超级盟主组织
        oem.setBrandName(oemMap.get(oem.getBrandCode()));
        int num =awardParamDao.addAwardParam(oem);
        if(num>0){
            if(oem.getDiamonds()!=null){
                oem.getDiamonds().setBrandCode(oem.getBrandCode());
                awardParamDao.addDiamonds(oem.getDiamonds());
            }
            if(tradeList!=null&&tradeList.size()>0){
                for(AwardParamLadder item:tradeList){
                    awardParamDao.addLadder(item,oem.getBrandCode(),1);
                }
            }
            if(crownList!=null&&crownList.size()>0){
                for(AwardParamLadder item:crownList){
                    awardParamDao.addLadder(item,oem.getBrandCode(),2);
                }
            }
            if(vipList!=null&&vipList.size()>0){
                for(AwardParamLadder item:vipList){
                    awardParamDao.addLadder(item,oem.getBrandCode(),3);
                }
            }
        }
        return num;
    }

    @Override
    public AwardParam getAwardParam(int id) {
        AwardParam oem=awardParamDao.getAwardParamLittle(id);
        if(oem!=null&&oem.getBrandCode()!=null){
            AwardParamDiamonds diamonds=awardParamDao.getDiamonds(oem.getBrandCode());
            if(diamonds!=null){
                oem.setDiamonds(diamonds);
            }
            List<AwardParamLadder> tradeList=awardParamDao.getLadder(oem.getBrandCode(),1);
            if(tradeList!=null&&tradeList.size()>0){
                oem.setTradeList(tradeList);
            }
            List<AwardParamLadder> crownList=awardParamDao.getLadder(oem.getBrandCode(),2);
            if(crownList!=null&&crownList.size()>0){
                oem.setCrownList(crownList);
            }
            List<AwardParamLadder> vipList=awardParamDao.getLadder(oem.getBrandCode(),3);
            if(vipList!=null&&vipList.size()>0){
                oem.setVipList(vipList);
            }
        }
        return oem;
    }

    @Override
    public AwardParam getAwardParamOem(int id) {
        AwardParam oem=awardParamDao.getAwardParam(id);
        return oem;
    }

    @Override
    public int updateAwardParam(AwardParam oem, List<AwardParamLadder> tradeList,
                                List<AwardParamLadder> crownList,
                                List<AwardParamLadder> vipList) {
        Map<String, String> oemMap=sysDictService.selectMapByKey("AGENT_OEM");//超级盟主组织
        oem.setBrandName(oemMap.get(oem.getBrandCode()));
        AwardParam oldOem=awardParamDao.getAwardParam(oem.getId());
        int num =awardParamDao.updateAwardParam(oem);
        if(num>0){
            if(oem.getDiamonds()!=null){
                oem.getDiamonds().setBrandCode(oem.getBrandCode());
                awardParamDao.updateDiamonds(oem.getDiamonds());
            }
            if(tradeList!=null&&tradeList.size()>0){
                awardParamDao.deleteLadder(oldOem.getBrandCode(),1);
                for(AwardParamLadder item:tradeList){
                    awardParamDao.addLadder(item,oem.getBrandCode(),1);
                }
            }
            if(crownList!=null&&crownList.size()>0){
                awardParamDao.deleteLadder(oldOem.getBrandCode(),2);
                for(AwardParamLadder item:crownList){
                    awardParamDao.addLadder(item,oem.getBrandCode(),2);
                }
            }
            if(vipList!=null&&vipList.size()>0){
                awardParamDao.deleteLadder(oldOem.getBrandCode(),3);
                for(AwardParamLadder item:vipList){
                    awardParamDao.addLadder(item,oem.getBrandCode(),3);
                }
            }
        }
        return  num;
    }

    @Override
    public int updateAwardParamOem(AwardParam oem) {
        return awardParamDao.updateAwardParamOem(oem);
    }

    @Override
    public boolean checkAwardParam(AwardParam oem) {
        if(oem.getId()==null){
            List<AwardParam> list=awardParamDao.checkAwardParam(oem.getBrandCode());
            if(list!=null&&list.size()>0){
                return  true;
            }
        }else{
            List<AwardParam> list=awardParamDao.checkAwardParamId(oem.getBrandCode(),oem.getId());
            if(list!=null&&list.size()>0){
                return  true;
            }
        }
        return false;
    }

    @Override
    public List<AwardParam> getOemList() {
        return awardParamDao.getOemList();
    }

    @Override
    public int deleteOwnerImgs(int id,String ownerImgs){
        return awardParamDao.deleteOwnerImgs(id,ownerImgs);
    }

    @Override
    public int deleteMerImgs(int id,String merImgs){
        return awardParamDao.deleteMerImgs(id,merImgs);
    }

    @Override
    public int deleteLeaImgs(int id,String leaderboardBgi){
        return awardParamDao.deleteLeaImgs(id,leaderboardBgi);
    }
}
