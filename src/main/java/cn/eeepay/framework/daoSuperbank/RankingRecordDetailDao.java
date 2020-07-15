package cn.eeepay.framework.daoSuperbank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RankingRecordDetail;

public interface RankingRecordDetailDao {

	 @SelectProvider(type=SqlProvider.class,method="selectRankingRecordDetail")
	 @ResultType(RankingRecordDetail.class)
	 List<RankingRecordDetail> selectRankingRecordDetail(@Param("recordId")String recordId,@Param("page")Page<RankingRecordDetail> page);
	 
	 @Update("update ranking_record_detail set status=#{baseInfo.status},remove_time=#{baseInfo.removeTime},remark=#{baseInfo.remark} where id=#{baseInfo.id}")
	 int updRankingDetail(@Param("baseInfo") RankingRecordDetail baseInfo);
	 
	 /**查询未发放的数据*/
	 @Select("select * from ranking_record_detail where is_rank=#{baseInfo.isRank} and status=#{baseInfo.status} and record_id=#{baseInfo.recordId}")
	 List<RankingRecordDetail> selectPushRecord(@Param("baseInfo") RankingRecordDetail baseInfo);
	 
	 @Update("update ranking_record_detail set status=1,push_time=NOW() where id=#{baseInfo.id}")
	 int updPushedRecord(@Param("baseInfo") RankingRecordDetail baseInfo);
	 
	 @Update("update ranking_record_detail set ranking_index=#{baseInfo.rankingIndex}  where id=#{baseInfo.id}")
	 int updSort(@Param("baseInfo") RankingRecordDetail baseInfo);
	 
	 /**批量更新*/
	 @UpdateProvider(type=SqlProvider.class,method="updateBatch")
	 @ResultType(Integer.class)
	 int updateBatch(@Param("params")Map<String,Object> params);
	 
	 @Select("select * from ranking_record_detail where id=#{id}")
	 RankingRecordDetail selectRankingById(@Param("id")String id);
	 
	 class SqlProvider{
		 public String selectRankingRecordDetail(Map<String,Object> param){
			 String recordId = (String)param.get("recordId");
			 SQL sql = new SQL();
			 
			 sql.SELECT("rd.id,rd.record_id,rr.ranking_no,rr.status as rankingStatus,rr.rule_no,rr.ranking_name,rr.ranking_type,rr.batch_no,rr.push_num,rr.push_total_amount");
			 sql.SELECT("IFNULL(rr.push_real_num,0) as pushRealNum,IFNULL(rr.push_real_amount,0) as pushRealAmount,rr.start_date,rr.end_date,(select sum(user_total_amount) from ranking_record_detail where record_id="+recordId+") as allUsersTotal,oi.org_name");
			 sql.SELECT("rd.user_name,rd.nick_name");
			 sql.SELECT("rd.user_code,rd.user_total_amount,rd.is_rank,rd.ranking_level,rd.ranking_amount,rd.status,rd.remove_time,rd.push_time,rd.remark,ui.phone,ui.toagent_date,rd.ranking_index ");
			 whereSql(sql,recordId);
			 sql.ORDER_BY(" rd.user_total_amount desc ");
			 sql.ORDER_BY(" rd.ranking_index asc ");
			 //sql.ORDER_BY(" ui.toagent_date ");   产生记录时已根据专员时间排序
			 return sql.toString();
		 }
		 
		 public void whereSql(SQL sql, String recordId){
			 sql.FROM("ranking_record_detail rd");
			 sql.LEFT_OUTER_JOIN("   ranking_record rr on rr.id=rd.record_id");
			 sql.LEFT_OUTER_JOIN("   org_info oi on rr.org_id=oi.org_id");
			 sql.LEFT_OUTER_JOIN("   user_info ui on rd.user_code=ui.user_code");
			 sql.WHERE(" rd.record_id="+recordId);
		 }
		 
		 public String updateBatch(Map<String,Object> param){
			 Map<String,Object> params = (Map<String,Object>)param.get("params");
			 List<Long> ids = (List<Long>)params.get("ids");
			 StringBuilder sql = new StringBuilder("update ranking_record_detail set is_rank=#{params.isRank},ranking_level=#{params.levelName} ,ranking_amount=#{params.rankingAmount} where id in(");
			 for(Long id :ids){
				 sql.append(id+",");
			 }
			 sql.delete(sql.length()-1, sql.length());
			 sql.append(")");
			 
			 return sql.toString();
		 }
	 }
}
