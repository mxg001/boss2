package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.MerchantRequireHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2018/2/6/006.
 */
public interface MerchantRequireHistoryDao {

    @Select(
            "select * from merchant_require_history where merchant_no=#{merchantNo} and mri_id=#{mriId} and status=1 ORDER BY create_time desc LIMIT 10 "
    )
    List<MerchantRequireHistory> getMerchantRequireHistoryByMriId(@Param("merchantNo")String merchantNo, @Param("mriId")String mriId);

    //新增结算卡修改记录
    @Insert(
            "INSERT INTO merchant_require_history" +
                    "(merchant_no,mri_id,modify_type,history_content,new_content,batch_no,status,create_time,creator,remark)" +
                    " VALUES(#{mer.merchantNo},'3','2',#{mer.historyContent},#{mer.newContent},'BOSS','1',NOW(),#{mer.creator},'BOSS修改结算卡')"
    )
    int insertBankCard(@Param("mer")MerchantRequireHistory mer);

}
