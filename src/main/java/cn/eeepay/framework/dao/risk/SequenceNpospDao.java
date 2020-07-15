package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.model.risk.SeqData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/9/10/010.
 * @author liuks
 */
public interface SequenceNpospDao {

    @Insert(
            "INSERT INTO unique_seq (create_time) VALUES (NOW())"
    )
    @Options(useGeneratedKeys = true, keyProperty = "seqData.id")
    Long getSequence(@Param("seqData") SeqData seqData);
}
