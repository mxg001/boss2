package cn.eeepay.framework.service.impl.cjt;

import cn.eeepay.framework.dao.HardwareProductDao;
import cn.eeepay.framework.dao.cjt.CjtGoodsDao;
import cn.eeepay.framework.dao.cjt.CjtTeamHardwareDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.cjt.CjtGoods;
import cn.eeepay.framework.service.cjt.CjtGoodsService;
import cn.eeepay.framework.service.impl.SeqService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超级推商品 服务层实现
 * @author tans
 * @date 2019-05-28
 */
@Service
public class CjtGoodsServiceImpl implements CjtGoodsService {

    @Resource
    private SeqService seqService;

    @Resource
    private CjtGoodsDao cjtGoodsDao;

    @Resource
    private HardwareProductDao hardwareProductDao;

    @Resource
    private CjtTeamHardwareDao cjtTeamHardwareDao;

    /**
     * 条件查询超级推商品
     * @param cjtGoods
     * @return
     */
    @Override
    public void selectCjtGoodsPage(Page<CjtGoods> page, CjtGoods cjtGoods) {
        cjtGoodsDao.selectCjtGoodsPage(page, cjtGoods);
        if(page != null && page.getResult() != null && page.getResult().size() > 0) {
            List<CjtGoods> list = page.getResult();
            for(CjtGoods item: list) {
                if(StringUtils.isNotEmpty(item.getMainImg())){
                    String imgUrl1 = item.getMainImg().split(",")[0];
                    item.setMainImgUrl1(CommonUtil.getImgUrlAgent(imgUrl1));
                }
                if(item.getCreateTime() != null){
                    item.setCreateTimeStr(DateUtil.getLongFormatDate(item.getCreateTime()));
                }
                if(item.getLastUpdateTime() != null){
                    item.setLastUpdateTimeStr(DateUtil.getLongFormatDate(item.getLastUpdateTime()));
                }
            }
        }
        return;
    }

    /**
     * 查询超级推商品
     * @param goodsCode
     * @return
     */
    @Override
    public CjtGoods selectCjtGoodsDetail(String goodsCode) {
        CjtGoods goods = cjtGoodsDao.selectCjtGoodsDetail(goodsCode);
        if(goods == null){
            throw new BossBaseException("找不到对应的商品");
        }
        String mainImg = goods.getMainImg();
        if(StringUtils.isNotEmpty(mainImg)){
            String[] imgArr = mainImg.split(",");
            if(imgArr.length >= 1){
                goods.setMainImgName1(imgArr[0]);
                goods.setMainImgUrl1(CommonUtil.getImgUrlAgent(imgArr[0]));
            }
            if(imgArr.length >= 2){
                goods.setMainImgName2(imgArr[1]);
                goods.setMainImgUrl2(CommonUtil.getImgUrlAgent(imgArr[1]));
            }
            if(imgArr.length >= 3){
                goods.setMainImgName3(imgArr[2]);
                goods.setMainImgUrl3(CommonUtil.getImgUrlAgent(imgArr[2]));
            }
        }
        String descImg = goods.getDescImg();
        if(StringUtils.isNotEmpty(descImg)){
            String[] descImgArr = descImg.split(",");
            StringBuffer sb = new StringBuffer();
            for(String img: descImgArr){
                sb.append(CommonUtil.getImgUrlAgent(img)).append(",");
            }
            goods.setDescImgUrl(sb.toString());
        }
        if(goods.getHpId() != null) {
            HardwareProduct hardwareProduct = hardwareProductDao.getHardwareProductByBpId(Long.valueOf(goods.getHpId()));
            if(hardwareProduct != null) {
                goods.setTypeName(hardwareProduct.getTypeName());
            }
        }
        return goods;
    }

    /**
     * 新增超级推商品
     * @param cjtGoods
     * @return
     */
    @Override
    public int insertCjtGoods(CjtGoods cjtGoods) {
        //对新增和修改的公共数据进行组合
        buildCommonGoodsData(cjtGoods);
        cjtGoods.setStatus("0");//默认未上架
        cjtGoods.setWhiteStatus("0");//默认非白名单
        cjtGoods.setCreater(CommonUtil.getLoginUserName());

        String goodsCode = seqService.createKey(CjtGoods.cjx_goods_code);
        cjtGoods.setGoodsCode(goodsCode);
        return cjtGoodsDao.insertCjtGoods(cjtGoods);
    }

    /**
     * 主要是对图片的处理
     * @param cjtGoods
     */
    private void buildCommonGoodsData(CjtGoods cjtGoods) {
        String mainImg1 = cjtGoods.getMainImgName1();
        if(StringUtils.isEmpty(mainImg1)) {
            throw new BossBaseException("主图不能为空");
        }
        String mainImg2 = cjtGoods.getMainImgName2();
        String mainImg3 = cjtGoods.getMainImgName3();

        StringBuffer mainSb = new StringBuffer();
        mainSb = buildImgName(mainSb, mainImg1);
        mainSb = buildImgName(mainSb, mainImg2);
        mainSb = buildImgName(mainSb, mainImg3);

        cjtGoods.setMainImg(mainSb.toString());
    }

    private StringBuffer buildImgName(StringBuffer sb, String imgName) {
        if(StringUtils.isNotEmpty(imgName)) {
            return sb.append(imgName).append(",");
        }
        return sb;
    }

    /**
     * 修改超级推商品
     * @param cjtGoods
     * @return
     */
    @Override
    public int updateCjtGoods(CjtGoods cjtGoods) {
        buildCommonGoodsData(cjtGoods);
        return cjtGoodsDao.updateCjtGoods(cjtGoods);
    }

    @Override
    public int updateStatus(CjtGoods cjtGoods) {
        return cjtGoodsDao.updateStatus(cjtGoods);
    }

    @Override
    public int updateWhiteStatus(CjtGoods cjtGoods) {
        return cjtGoodsDao.updateWhiteStatus(cjtGoods);
    }

    @Override
    public List<HardwareProduct> selectCjtHpList() {
        return cjtTeamHardwareDao.selectCjtHpList();
    }
}
