package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CardBins;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/23/023.
 * @author  liuks
 * 卡bin service
 */
public interface CardBinsService {

    List<CardBins> selectAllList(CardBins card, Page<CardBins> page);

    int insertCardBins(CardBins card);

    int updateCardBins(CardBins card);

    CardBins getCardBins(int id);

    int deleteCardBins(int id);

    CardBins getCardBinsByCardNo(String cardNo,Integer businessType,Integer cardDigit);

    List<CardBins> exportInfo(CardBins card);

    Map<String,Object> importCardBins(MultipartFile file,Integer businessType) throws Exception;

    void exportCardBins(List<CardBins> list,HttpServletResponse response) throws Exception ;

    int openStateCardBins(int id, int state);
}
