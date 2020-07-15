package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.ZqFileSync;
import cn.eeepay.framework.db.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 报报备 数据层
 * 
 * @author tans
 * @date 2019-04-11
 */
public interface ZqFileSyncDao {
	
	/**
     * 查询报报备列表
     * 
     * @param zqFileSync 报报备信息
     * @return 报报备集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectPage")
	@ResultType(ZqFileSync.class)
	List<ZqFileSync> selectPage(@Param("page") Page<ZqFileSync> page,
									   @Param("baseInfo") ZqFileSync zqFileSync);

	@Insert("insert into zq_file_sync(batch_no,channel_code,file_name,file_url,inner_num,operator,create_time,status)" +
			" values (#{batchNo},#{channelCode},#{fileName},#{fileUrl},#{innerNum},#{operator},#{createTime},#{status})")
    int insert(ZqFileSync info);

	@Select("select * from zq_file_sync where status = #{status}")
	@ResultType(ZqFileSync.class)
	List<ZqFileSync> selectListByStatus(@Param("status") String status);

	@Update("update zq_file_sync set status = #{status} where batch_no = #{batchNo}")
	int updateStatus(@Param("batchNo")String batchNo, @Param("status")String status);

	@Select("select * from zq_file_sync where batch_no = #{batchNo}")
	@ResultType(ZqFileSync.class)
	ZqFileSync select(@Param("batchNo") String batchNo);

	class SqlProvider{
		public String selectPage(Map<String, Object> param){
			ZqFileSync baseInfo = (ZqFileSync) param.get("baseInfo");
			if(baseInfo == null){
				return null;
			}
			SQL sql = new SQL();
			sql.SELECT("zfs.*");
			sql.FROM("zq_file_sync zfs");
			if(StringUtils.isNotBlank(baseInfo.getBatchNo())){
				sql.WHERE("zfs.batch_no = #{baseInfo.batchNo}");
			}
			if(StringUtils.isNotBlank(baseInfo.getStatus())){
				sql.WHERE("zfs.status = #{baseInfo.status}");
			}
			if(StringUtils.isNotBlank(baseInfo.getChannelCode())){
				sql.WHERE("zfs.channel_code = #{baseInfo.channelCode}");
			}
			if(StringUtils.isNotBlank(baseInfo.getCreateTimeStart())){
				sql.WHERE("zfs.create_time >= #{baseInfo.createTimeStart}");
			}
			if(StringUtils.isNotBlank(baseInfo.getCreateTimeEnd())){
				sql.WHERE("zfs.create_time <= #{baseInfo.createTimeEnd}");
			}
			sql.ORDER_BY("zfs.create_time desc, zfs.id desc");
			return sql.toString();
		}
	}

	
}