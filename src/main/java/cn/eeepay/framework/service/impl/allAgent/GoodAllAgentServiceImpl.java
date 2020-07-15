package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.GoodAllAgentDao;
import cn.eeepay.framework.daoAllAgent.GoodsGroupDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.allAgent.CountSet;
import cn.eeepay.framework.model.allAgent.GoodAllAgent;
import cn.eeepay.framework.model.allAgent.GoodsPrice;
import cn.eeepay.framework.service.allAgent.GoodAllAgentService;
import cn.eeepay.framework.service.allAgent.RandomNumAllAgentService;
import cn.eeepay.framework.util.CommonUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author  liuks
 * 商品service实现
 */
@Service("goodAllAgentService")
public class GoodAllAgentServiceImpl implements GoodAllAgentService {

    @Resource
    private GoodAllAgentDao goodAllAgentDao;

    @Resource
    private GoodsGroupDao groupDao;

    @Resource
    private RandomNumAllAgentService randomNumAllAgentService;

    @Override
    public List<GoodAllAgent> selectAllList(GoodAllAgent good, Page<GoodAllAgent> page) {
        List<GoodAllAgent> list=goodAllAgentDao.selectAllList(good,page);
        getGoodImgUrlList(page.getResult());
        return list;
    }

    private void getGoodImgUrlList(List<GoodAllAgent> list){
        if(list!=null&&list.size()>0){
            for(GoodAllAgent good:list){
                getGoodImgUrl(good);
            }
        }
    }
    private void getGoodImgUrl(GoodAllAgent good){
        if(good!=null){
            if(good.getImg()!=null){
                good.setImg(CommonUtil.getImgUrlAgent(good.getImg()));
            }
            if(good.getImg2()!=null){
                good.setImg2(CommonUtil.getImgUrlAgent(good.getImg2()));
            }
            if(good.getImg3()!=null){
                good.setImg3(CommonUtil.getImgUrlAgent(good.getImg3()));
            }
        }
    }
    @Override
    public int addGood(GoodAllAgent good,List<GoodsPrice> pricesList) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String code=randomNumAllAgentService.getRandomNumber("SKU","good_code");
        good.setCreater(principal.getUsername());
        good.setGoodsCode(code);
        int i=goodAllAgentDao.addGood(good);
        for(GoodsPrice gp:pricesList){
            gp.setGoodsCode(code);
            goodAllAgentDao.addGoodsPrice(gp);
        }
        return i;
    }

    @Override
    public GoodAllAgent getGood(int id) {
        GoodAllAgent good=goodAllAgentDao.getGood(id);
        getGoodImgUrl(good);
        return  good;
    }

    @Override
    public GoodAllAgent getGoodsCode(String goodsCode) {
        GoodAllAgent good=goodAllAgentDao.getGoodsCode(goodsCode);
        getGoodImgUrl(good);
        return  good;
    }

    @Override
    public int saveGood(GoodAllAgent good,List<GoodsPrice> pricesList,List<GoodsPrice> deleteList) {
        int i=goodAllAgentDao.saveGood(good);
        for(GoodsPrice gp:pricesList){
            if(gp.getId()!=null){
                goodAllAgentDao.updateGoodsPrice(gp);
            }else{
                gp.setGoodsCode(good.getGoodsCode());
                goodAllAgentDao.addGoodsPrice(gp);
            }
        }
        for(GoodsPrice gp:deleteList){
            if(gp.getId()!=null){
                goodAllAgentDao.deleteGoodsPrice(gp.getId());
            }
        }
        return i;
    }

    @Override
    public List<GoodsPrice> getGoodsPrice(String  goodsCode){
        return goodAllAgentDao.getGoodsPrice(goodsCode);
    }

    @Override
    public int updateGood(int id, int status) {
        int num=goodAllAgentDao.updateGood(id,status);
        return num;
    }

    @Override
    public CountSet selectAllSum(GoodAllAgent good, Page<GoodAllAgent> page) {
        return goodAllAgentDao.selectAllSum(good,page);
    }

    @Override
    public int deleteGoodImg(int id, int status) {
        return goodAllAgentDao.deleteGoodImg(id,status);
    }


    @Override
    public List<Map> goodsGroupQuery(Map group, Page<Map> page) {
        return page == null ? groupDao.goodsGroupQueryMap(group) : groupDao.goodsGroupQuery(group, page);
    }

    @Override
    public int addGoodsGroup(Map group) {
        return groupDao.addGoodsGroup(group);
    }

    @Override
    public int updateGoodsGroup(Map group) {
        return groupDao.updateGoodsGroup(group);
    }

    @Override
    public int updateListType(Map group) {
        return goodAllAgentDao.updateListType(group);
    }

    @Override
    public int deleteGoodsGroup(Map params) {
        Map cnt = groupDao.countGoods(params);
        if (MapUtil.isNotEmpty(cnt) && (Long) cnt.get("cnt") > 0) {
            //如果当前分类下有商品，则提示不能删除
            return -9;
        }
        return groupDao.deleteGoodsGroup(params);
    }
}
