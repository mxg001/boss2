package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RankingRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface RankingRecordDao {

	 @SelectProvider(type=SqlProvider.class,method="selectRankingRecord")
	 @ResultType(RankingRecord.class)
	 List<RankingRecord> selectRankingRecord(@Param("baseInfo") RankingRecord baseInfo, @Param("page") Page<RankingRecord> page);
	 
	 @SelectProvider(type=SqlProvider.class,method="selectOrderSum")
	 @ResultType(RankingRecord.class)
	 RankingRecord getTotalInfo(@Param("baseInfo") RankingRecord baseInfo);
	 
	 @SelectProvider(type=SqlProvider.class,method="selectRankingRecordSum")
	 @ResultType(RankingRecord.class)
	 RankingRecord getRankingDetailSum(@Param("baseInfo") RankingRecord baseInfo);
	 
	 /**更新数据*/
	 @Update("update ranking_record set push_real_num=#{baseInfo.pushRealNum},push_real_amount=#{baseInfo.pushRealAmount},status=#{baseInfo.status} where id=#{baseInfo.id}")
	 int updRankingRecord(@Param("baseInfo") RankingRecord baseInfo);
	 
	 @Update("update ranking_record set push_num=#{baseInfo.pushNum} where id=#{baseInfo.id}")
	 int updRankingRecordPushNum(@Param("baseInfo") RankingRecord baseInfo);
	 
	class SqlProvider{
		 public String selectRankingRecord(Map<String,Object> param){
			 final RankingRecord info = (RankingRecord)param.get("baseInfo");
			 SQL sql = new SQL();
			 sql.SELECT("rr.id,rr.ranking_no,rr.batch_no,rr.rule_no,rr.ranking_name,rr.ranking_type,rr.org_id,rr.push_num,rr.push_total_amount,rr.push_real_num,rr.push_real_amount,rr.status,rr.create_date,rr.start_date,rr.end_date,oi.org_name,rr.data_type ");
			 whereSql(sql,info);
			 sql.ORDER_BY(" rr.create_date desc ");
			 return sql.toString();
		 }
	
		 public String selectOrderSum(Map<String,Object> param){
			 final RankingRecord info = (RankingRecord)param.get("baseInfo");
			 SQL sql = new SQL();
			 sql.SELECT("sum(rr.push_real_num) as totalPushPerson");
			 sql.SELECT("sum(rr.push_real_amount) as totalPushAmount");
			 whereSql(sql,info);
			 return sql.toString();
		 }
		 
		 public String selectRankingRecordSum(Map<String,Object> param){
			 final RankingRecord baseInfo = (RankingRecord)param.get("baseInfo");
			 SQL sql = new SQL();
			 
			 sql.SELECT("rr.ranking_no,rr.status as ranking_status,rr.rule_no,rr.ranking_name,rr.ranking_type,rr.batch_no,rr.push_num,rr.push_total_amount");
			 sql.SELECT("IFNULL(rr.push_real_num,0) as push_real_num,IFNULL(rr.push_real_amount,0) as push_real_amount,rr.start_date,rr.end_date,(select sum(user_total_amount) from ranking_record_detail where record_id=#{baseInfo.id}) as allUsersTotal");
			 sql.FROM(" ranking_record rr");
			 sql.WHERE(" rr.id=#{baseInfo.id}");
			 return sql.toString();
		 }
		 
		 public void whereSql(SQL sql, RankingRecord baseInfo){
			 sql.FROM("ranking_record rr");
			 
			 sql.LEFT_OUTER_JOIN("   org_info oi on rr.org_id=oi.org_id");

			 if(StringUtils.isNotBlank(baseInfo.getOrgId())){
				 sql.WHERE(" oi.org_id = #{baseInfo.orgId}");
			 }

			 if(StringUtils.isNotBlank(baseInfo.getRankingNo())){
				 sql.WHERE(" rr.ranking_no = #{baseInfo.rankingNo}");
			 }
			 if(StringUtils.isNotBlank(baseInfo.getRankingType())){
				 sql.WHERE(" rr.ranking_type = #{baseInfo.rankingType}");
			 }
			 if(StringUtils.isNotBlank(baseInfo.getStatus())){
				 sql.WHERE(" rr.status = #{baseInfo.status}");
			 }
			 if(StringUtils.isNotBlank(baseInfo.getRankingName())){
				 sql.WHERE(" rr.ranking_name = #{baseInfo.rankingName}");
			 }
			 if(StringUtils.isNotBlank(baseInfo.getRuleNo())){
				 sql.WHERE(" rr.rule_no = #{baseInfo.ruleNo}");
			 }
			 if(StringUtils.isNotBlank(baseInfo.getDataType())){
				 sql.WHERE(" rr.data_type = #{baseInfo.dataType}");
			 }
			 if(StringUtils.isNotBlank(baseInfo.getId())){
				 sql.WHERE(" rr.id = #{baseInfo.id}");
			 }
			 if(StringUtils.isNotBlank(baseInfo.getOrgId()) && !"-1".equals(baseInfo.getOrgId())){
				 sql.WHERE(" rr.org_id = #{baseInfo.orgId}");
			 }
		 }
	 }
	
	
}
