package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ZqFileSyncRecord;
import org.apache.ibatis.annotations.Insert;

/**
 * @author tans
 * @date 2019/4/11 22:39
 */
public interface ZqFileSyncRecordDao {

    @Insert("insert into zq_file_sync_record" +
            " (batch_no,status,channel_code,merchant_no,bp_id,result_msg,operator,create_time)" +
            " values (#{batchNo},#{status},#{channelCode},#{merchantNo},#{bpId},#{resultMsg},#{operator},#{createTime})")
    int insert(ZqFileSyncRecord baseInfo);
}
