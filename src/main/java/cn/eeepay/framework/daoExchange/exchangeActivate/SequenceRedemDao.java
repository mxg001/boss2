package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.model.risk.SeqData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/9/30/030.
 * @author  liuks
 * 生产序列 对应redem库的unique_seq
 */
public interface SequenceRedemDao {

    @Insert(
            "INSERT INTO unique_seq (create_time) VALUES (NOW())"
    )
    @Options(useGeneratedKeys = true, keyProperty = "seqData.id")
    Long getSequence(@Param("seqData") SeqData seqData);
}
