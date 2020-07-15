package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CardBins;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/23/023.
 * @author  liuks
 * 卡bin dao
 */
public interface CardBinsDao {

    @SelectProvider(type=CardBinsDao.SqlProvider.class,method="selectAllList")
    @ResultType(CardBins.class)
    List<CardBins> selectAllList(@Param("card") CardBins card,@Param("page") Page<CardBins> page);

    @SelectProvider(type=CardBinsDao.SqlProvider.class,method="selectAllList")
    @ResultType(CardBins.class)
    List<CardBins> exportInfo(@Param("card")CardBins card);

    @Insert(
            " INSERT INTO card_bins " +
                    "(card_no,state,card_type,card_bank,currency,card_style,remarks,create_time,create_id,create_name,business_type,card_num,card_digit) " +
                    " VALUES (#{card.cardNo},#{card.state},#{card.cardType},#{card.cardBank},#{card.currency}," +
                    "  #{card.cardStyle},#{card.remarks},NOW(),#{card.createId},#{card.createName},#{card.businessType},#{card.cardNum},#{card.cardDigit}) "
    )
    int insertCardBins(@Param("card")CardBins card);

    @Update(
            " update card_bins set card_no=#{card.cardNo},state=#{card.state},card_type=#{card.cardType}, " +
                    " card_bank=#{card.cardBank},currency=#{card.currency},card_style=#{card.cardStyle},business_type=#{card.businessType}, " +
                    " remarks=#{card.remarks},card_num=#{card.cardNum},card_digit=#{card.cardDigit} where id=#{card.id}"
    )
    int updateCardBins(@Param("card")CardBins card);

    @Select(
            "select * from card_bins where id=#{id}"
    )
    CardBins getCardBins(@Param("id")int id);

    @Delete(
            " delete from  card_bins where id=#{id}"
    )
    int deleteCardBins(@Param("id")int id);

    @Select(
            "select * from card_bins where card_no=#{cardNo} and business_type=#{businessType} and card_digit=#{cardDigit}"
    )
    CardBins getCardBinsByCardNo(@Param("cardNo")String cardNo,@Param("businessType")Integer businessType,@Param("cardDigit")Integer cardDigit);

    @Update(
            "update card_bins set state=#{state} where id=#{id} "
    )
    int openStateCardBins(@Param("id")int id, @Param("state")int state);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final CardBins card = (CardBins) param.get("card");
            String str= new SQL(){{
                SELECT(" * ");
                FROM(" card_bins ");
                if(StringUtils.isNotBlank(card.getCardNo())){
                    WHERE(" card_no like concat(#{card.cardNo},'%')");
                }
                if(card.getState()!=null){
                    WHERE(" state=#{card.state}");
                }
                if(card.getCardType()!=null){
                    WHERE(" card_type=#{card.cardType}");
                }
                if(card.getBusinessType()!=null){
                    WHERE(" business_type=#{card.businessType}");
                }
                if(card.getCurrency()!=null){
                    WHERE(" currency=#{card.currency}");
                }
                if(StringUtils.isNotBlank(card.getCardBank())){
                    WHERE(" card_bank=#{card.cardBank}");
                }
                if(card.getCreateTimeBegin() != null){
                    WHERE(" create_time >= #{card.createTimeBegin}");
                }
                if(card.getCreateTimeEnd() != null){
                    WHERE(" create_time <= #{card.createTimeEnd}");
                }
                ORDER_BY("create_time desc ");
            }}.toString();
            return str;
        }
    }
}
