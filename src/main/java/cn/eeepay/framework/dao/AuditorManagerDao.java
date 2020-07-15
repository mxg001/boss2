package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.AuditorCountInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.boss.action.AuditorManagerAction.AuditorRecord;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AuditorManagerInfo;

public interface AuditorManagerDao {

	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(AuditorManagerInfo.class)
	List<AuditorManagerInfo> selectByCondition(Page<AuditorManagerInfo> page, @Param("baseInfo")AuditorManagerInfo baseInfo);
	
	@SelectProvider(type=SqlProvider.class, method="getBpByAuditor")
	@ResultType(AuditorManagerInfo.class)
	List<AuditorManagerInfo> getBpByAuditor(@Param("auditorId")String auditorId);
	
	@Delete("delete from auditor_manager where auditor_id=#{auditorId}")
	int deleteByAuditor(String auditorId);

	@InsertProvider(type=SqlProvider.class, method="insertBatch")
	int insertBatch(List<AuditorManagerInfo> list);
	
	@Update("update auditor_manager set status=#{status} where id=#{id}")
	int updateStatus(AuditorManagerInfo info);
	
	@Delete("delete from auditor_manager where id=#{id}")
	int deleteData(Integer id);
	
	//审核人员记录查询
	@SelectProvider(type=SqlProvider.class, method="selecrAllInfoRecord")
	@ResultType(AuditorManagerInfo.class)
	List<AuditorManagerInfo> selecrAllInfoRecord(Page<AuditorManagerInfo> page,@Param("auditorId")String auditorId);
	
	@SelectProvider(type=SqlProvider.class, method="getRecordByUserList")
	@ResultType(AuditorCountInfo.class)
	List<AuditorCountInfo> getRecordByUserList(@Param("page")Page<AuditorCountInfo> page,@Param("info")AuditorCountInfo info);


	@SelectProvider(type=SqlProvider.class, method="getlogList")
	@ResultType(AuditorManagerInfo.class)
	List<AuditorManagerInfo> getlogList(Page<AuditorManagerInfo> page,@Param("info")AuditorCountInfo info);


	@Select("Select DISTINCT mbp.`status`,mbp.id as itemNo,ais.agent_name,els.create_time as auditorTime,"
			+ "bpd.bp_name,mis.one_agent_no,bsu.user_name,mis.merchant_no,mbp.auditor_id,mbp.bp_id  "
			+ " from merchant_business_product mbp "
			+ "JOIN examinations_log els on els.item_no=mbp.id and els.examine_type=1 "
			+ "LEFT JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
			+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
			+ "LEFT JOIN boss_shiro_user bsu on bsu.id=mbp.auditor_id "
			+ "LEFT JOIN business_product_define bpd on bpd.bp_id=mbp.bp_id "
			+ "where mbp.auditor_id=#{baseInfo.auditorId} and mbp.`status` in (4) and mbp.auditor_id is not null "
			+ "and EXISTS(select ddd.bp_id from auditor_manager ddd where mbp.bp_id=ddd.bp_id and ddd.auditor_id=#{baseInfo.auditorId} and ddd.`status`=1)"
			+ "and mbp.create_time>=#{baseInfo.sTime} and mbp.create_time<=#{baseInfo.eTime}")
	@ResultType(AuditorManagerInfo.class)
	List<AuditorManagerInfo> selecrAllInfoRecordInfoImprot(@Param("baseInfo")AuditorRecord baseInfo);

	//审核统计查询,20170921,mys
	@SelectProvider(type=SqlProvider.class, method="selectAllInfoRecord")
	@ResultType(AuditorCountInfo.class)
	List<AuditorCountInfo> selectAllInfoRecord(Page<AuditorCountInfo> page,@Param("info")AuditorCountInfo info);

	//审核统计导出,mays,20171110
	@SelectProvider(type=SqlProvider.class, method="exportInfoList")
	@ResultType(AuditorManagerInfo.class)
	List<AuditorManagerInfo> exportInfoList(@Param("info")AuditorCountInfo info);

	public class SqlProvider{
		public String selectByCondition(Map<String, Object> param){
			final AuditorManagerInfo baseInfo = (AuditorManagerInfo) param.get("baseInfo");
			return new SQL(){{
				SELECT("am.*,su.user_name,bpd.bp_name");
				FROM("auditor_manager am");
				LEFT_OUTER_JOIN("boss_shiro_user su on am.auditor_id=su.id");
				LEFT_OUTER_JOIN("business_product_define bpd on am.bp_id=bpd.bp_id");
				if(baseInfo != null && !"-1".equals(baseInfo.getBpId())){
					WHERE("am.bp_id=#{baseInfo.bpId}");
				}
				if(baseInfo != null && !"-1".equals(baseInfo.getAuditorId())){
					WHERE("am.auditor_id=#{baseInfo.auditorId}");
				}
				if(baseInfo != null && baseInfo.getStatus()!=2){
					WHERE("am.status=#{baseInfo.status}");
				}
			}}.toString();
		}

		public String getBpByAuditor(Map<String, Object> param){
			final String auditorId =  (String) param.get("auditorId");
			return new SQL(){{
				SELECT("am.*,bpd.bp_name");
				FROM("auditor_manager am");
				FROM("business_product_define bpd");
				WHERE("am.bp_id=bpd.bp_id");
				if(auditorId != null && !"-1".equals(auditorId)){
					WHERE("am.auditor_id=#{auditorId}");
				}
			}}.toString();
		}

		public String insertBatch(Map<String, Object> param){
			List<AuditorManagerInfo> list = (List<AuditorManagerInfo>) param.get("list");
			StringBuilder sb = new StringBuilder("insert into auditor_manager(auditor_id,bp_id,status) values ");
			MessageFormat message = new MessageFormat("(#'{'list[{0}].auditorId},#'{'list[{0}].bpId},#'{'list[{0}].status}),");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			System.out.println(sb.toString());
			return sb.toString();
		}

		//记录
		public String selecrAllInfoRecord(Map<String, Object> param){
			final String auditorId =  (String) param.get("auditorId");
			return new SQL(){{
				SELECT_DISTINCT("am.auditor_id,bsu.user_name");
				FROM("auditor_manager am");
				LEFT_OUTER_JOIN("business_product_define bpd on bpd.bp_id=am.bp_id");
				LEFT_OUTER_JOIN("boss_shiro_user bsu on bsu.id=am.auditor_id");
				if(StringUtils.isNotBlank(auditorId)){
					WHERE("am.auditor_id=#{auditorId}");
				}
			}}.toString();
		}

		//审核统计导出,mays,20171110
		public String exportInfo(Map<String, Object> param){
			final AuditorRecord info = (AuditorRecord) param.get("info");
			return new SQL(){{
				SELECT("mbp.id mbp_id,ai.agent_name,ai1.sale_name,bu.user_name,el.create_time,mi.merchant_no,bpd.bp_name");
				FROM("merchant_business_product mbp,examinations_log el,merchant_info mi,agent_info ai,"
						+ "agent_info ai1,boss_shiro_user bu,business_product_define bpd ");
				WHERE("mbp.merchant_no = mi.merchant_no AND mi.agent_no = ai.agent_no "
						+ "AND ai.one_level_id = ai1.agent_no AND el.operator = bu.id "
						+ "AND mbp.id = el.item_no AND mbp.bp_id = bpd.bp_id "
						+ "AND el.open_status = '1' AND el.operator <> '-1' and el.examine_type=1 AND mbp.status = '4' ");
				if (StringUtils.isNotBlank(info.getAuditorId())) {
					WHERE("mbp.auditor_id = #{info.auditorId}");
				}
				if (info.getsTime() != null && info.geteTime() != null) {
					WHERE("el.create_time BETWEEN #{info.sTime} AND #{info.eTime} ");
				}
				ORDER_BY("el.create_time");
			}}.toString();
		}

		public String getlogList(Map<String, Object> param){
			AuditorCountInfo info = (AuditorCountInfo) param.get("info");
			return getLogList(info,1);
		}

		public String exportInfoList(Map<String, Object> param){
			AuditorCountInfo info = (AuditorCountInfo) param.get("info");
			return getLogList(info,2);
		}

		private String getLogList(AuditorCountInfo info,int sta){
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append("  mbp.id as itemNo, ");
			if(sta==2){
				sb.append("  buser.user_name AS userName, ");
				sb.append("  mis.merchant_no, ");
				sb.append("  mis.sale_name, ");
				sb.append("  bpd.bp_name, ");
			}
			sb.append("  mis.merchant_name, ");
			sb.append("  el.open_status as auditorStatus, ");
			sb.append("  ais.agent_name, ");
			sb.append("  mbp.create_time, ");
			sb.append("  el.create_time as auditorTime, ");
			sb.append("  el.examination_opinions as describes ");
			sb.append(" from examinations_log el  ");
			sb.append("   INNER JOIN merchant_business_product mbp ON CAST(el.item_no AS SIGNED INTEGER)= mbp.id ");
			sb.append("   INNER JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no ");
			sb.append("   INNER JOIN agent_info ais on ais.agent_no=mis.agent_no ");
			if(sta==2){
				sb.append(" LEFT JOIN boss_shiro_user buser ON CAST(el.operator AS SIGNED INTEGER) = buser.id  ");
				sb.append(" LEFT JOIN business_product_define bpd ON CAST(el.bp_id AS SIGNED INTEGER) = bpd.bp_id ");
			}
			sb.append(" where 1=1 ");
			if (info.getAuditorId()!=null) {
				sb.append("   AND el.operator=CONCAT(#{info.auditorId},'')  ");
			}
			if(info.getsTime() != null){
				sb.append("   AND el.create_time >= #{info.sTime} ");
			}
			if(info.geteTime() != null){
				sb.append("   AND el.create_time <= #{info.eTime} ");
			}
			sb.append("       AND el.operator != '-1' ");
			sb.append("       AND el.examine_type = 1 ");
			return sb.toString();
		}
		public String selectAllInfoRecord(Map<String, Object> param){
			AuditorCountInfo info = (AuditorCountInfo) param.get("info");
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append("   buser.id, ");
			sb.append("   buser.user_name, ");
			sb.append("   IF(t1.allCount is null, 0, t1.allCount) allCount, ");
			sb.append("   IF(t1.successNum is null, 0, t1.successNum) successNum, ");
			sb.append("   IF(t1.successNum1 is null, 0, t1.successNum1) successNum1, ");
			sb.append("   IF(t1.successNum2 is null, 0, t1.successNum2) successNum2, ");
			sb.append("   IF(t1.successNum3 is null, 0, t1.successNum3) successNum3, ");
			sb.append("   IF(t1.failureNum is null, 0, t1.failureNum) failureNum, ");
			sb.append("   IF(t1.failureNum1 is null, 0, t1.failureNum1) failureNum1, ");
			sb.append("   IF(t1.failureNum2 is null, 0, t1.failureNum2) failureNum2, ");
			sb.append("   IF(t1.failureNum3 is null, 0, t1.failureNum3) failureNum3, ");
			sb.append("   IF(t2.notAudited is null, 0, t2.notAudited) notAudited, ");
			sb.append("   IF(t2.notAudited1 is null, 0, t2.notAudited1) notAudited1, ");
			sb.append("   IF(t2.notAudited2 is null, 0, t2.notAudited2) notAudited2, ");
			sb.append("   IF(t2.notAudited3 is null, 0, t2.notAudited3) notAudited3 ");

			sb.append(" FROM boss_shiro_user buser ");
			sb.append("   LEFT JOIN ( ");
			sb.append("       SELECT ");
			sb.append("         CAST(el.operator AS SIGNED INTEGER) operator, ");
			sb.append("         count(*) allCount, ");
			sb.append("         sum(IF(el.open_status = 1, 1, 0)) successNum, ");
			sb.append("         sum(IF(el.open_status = 1 and logExt.merchant_type='1', 1, 0)) successNum1, ");
			sb.append("         sum(IF(el.open_status = 1 and logExt.merchant_type='2', 1, 0)) successNum2, ");
			sb.append("         sum(IF(el.open_status = 1 and logExt.merchant_type='3', 1, 0)) successNum3, ");
			sb.append("         sum(IF(el.open_status = 2, 1, 0)) failureNum, ");
			sb.append("         sum(IF(el.open_status = 2 and logExt.merchant_type='1', 1, 0)) failureNum1, ");
			sb.append("         sum(IF(el.open_status = 2 and logExt.merchant_type='2', 1, 0)) failureNum2, ");
			sb.append("         sum(IF(el.open_status = 2 and logExt.merchant_type='3', 1, 0)) failureNum3 ");
			sb.append("      FROM examinations_log el FORCE INDEX (create_time) ");
			sb.append("        LEFT JOIN examinations_log_ext logExt ON logExt.examinations_log_id=el.id ");
			sb.append("      WHERE 1=1 ");
			if(info.getsTime() != null){
				sb.append("      AND el.create_time >= #{info.sTime} ");
			}
			if(info.geteTime() != null){
				sb.append("      AND el.create_time <= #{info.eTime} ");
			}
			if (info.getAuditorId()!=null) {
				sb.append("      AND el.operator = CONCAT(#{info.auditorId},'') ");
			}
			sb.append("          AND el.operator != '-1' ");
			sb.append("          AND el.examine_type = 1 ");
			sb.append("     GROUP BY el.operator ");
			sb.append("  ) t1 ON t1.operator = buser.id ");

			sb.append("  LEFT JOIN ( ");
			sb.append("      SELECT ");
			sb.append("        mbp.auditor_id operator, ");
			sb.append("        count(*) notAudited, ");
			sb.append("        sum(IF(mi.merchant_type='1', 1, 0)) notAudited1, ");
			sb.append("        sum(IF(mi.merchant_type='2', 1, 0)) notAudited2, ");
			sb.append("        sum(IF(mi.merchant_type='3', 1, 0)) notAudited3 ");
			sb.append("      FROM merchant_business_product mbp ");
			sb.append("        INNER JOIN merchant_info mi ON mi.merchant_no = mbp.merchant_no ");
			sb.append("      WHERE 1=1 ");
			if(info.getsTime() != null){
				sb.append("      AND mbp.create_time >= #{info.sTime} ");
			}
			if(info.geteTime() != null){
				sb.append("      AND mbp.create_time <= #{info.eTime} ");
			}
			if (info.getAuditorId()!=null) {
				sb.append("      AND mbp.auditor_id = #{info.auditorId} ");
			}
			sb.append("          AND mbp. STATUS = 2 ");
			sb.append("          AND mbp.auditor_id IS NOT NULL ");
			sb.append("     GROUP BY  mbp.auditor_id ");
			sb.append("  ) t2 ON t2.operator = buser.id ");

			sb.append("  WHERE ");
			sb.append("   ( t1.allCount IS NOT NULL ");
			sb.append("     OR t1.successNum IS NOT NULL ");
			sb.append("     OR t1.failureNum IS NOT NULL ");
			sb.append("     OR t2.notAudited IS NOT NULL");
			sb.append("   ) ");
			if(StringUtils.isNotBlank(info.getAuditorName())){
				sb.append(" and buser.user_name like CONCAT('%',#{info.auditorName},'%')");
			}
			return sb.toString();
		}
		public String getRecordByUserList(Map<String, Object> param){
			AuditorCountInfo info = (AuditorCountInfo) param.get("info");
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append("   busbpd.id, ");
			if (info.getAuditorId()!=null) {
				sb.append("       (select user_name from  boss_shiro_user where id=#{info.auditorId}) userName,   ");
			}
			sb.append("   busbpd.bpId, ");
			sb.append("   busbpd.bpName, ");
			sb.append("   IF(t1.allCount is null, 0, t1.allCount) allCount, ");
			sb.append("   IF(t1.successNum is null, 0, t1.successNum) successNum, ");
			sb.append("   IF(t1.successNum1 is null, 0, t1.successNum1) successNum1, ");
			sb.append("   IF(t1.successNum2 is null, 0, t1.successNum2) successNum2, ");
			sb.append("   IF(t1.successNum3 is null, 0, t1.successNum3) successNum3, ");
			sb.append("   IF(t1.failureNum is null, 0, t1.failureNum) failureNum, ");
			sb.append("   IF(t1.failureNum1 is null, 0, t1.failureNum1) failureNum1, ");
			sb.append("   IF(t1.failureNum2 is null, 0, t1.failureNum2) failureNum2, ");
			sb.append("   IF(t1.failureNum3 is null, 0, t1.failureNum3) failureNum3, ");
			sb.append("   IF(t2.notAudited is null, 0, t2.notAudited) notAudited, ");
			sb.append("   IF(t2.notAudited1 is null, 0, t2.notAudited1) notAudited1, ");
			sb.append("   IF(t2.notAudited2 is null, 0, t2.notAudited2) notAudited2, ");
			sb.append("   IF(t2.notAudited3 is null, 0, t2.notAudited3) notAudited3 ");
			sb.append(" FROM (");
			sb.append("     SELECT "+info.getAuditorId()+" AS id,");
			sb.append("           bpd.bp_id bpId, ");
			sb.append("           bpd.bp_name bpName ");
			sb.append("     FROM business_product_define bpd ");
			sb.append("     ) busbpd ");

			sb.append("   LEFT JOIN ( ");
			sb.append("       SELECT ");
			sb.append("         CAST(el.operator AS SIGNED INTEGER) operator, ");
			sb.append("         CAST(el.bp_id AS SIGNED INTEGER) bpId, ");
			sb.append("         count(*) allCount, ");
			sb.append("         sum(IF(el.open_status = 1, 1, 0)) successNum, ");
			sb.append("         sum(IF(el.open_status = 1 and logExt.merchant_type='1', 1, 0)) successNum1, ");
			sb.append("         sum(IF(el.open_status = 1 and logExt.merchant_type='2', 1, 0)) successNum2, ");
			sb.append("         sum(IF(el.open_status = 1 and logExt.merchant_type='3', 1, 0)) successNum3, ");
			sb.append("         sum(IF(el.open_status = 2, 1, 0)) failureNum, ");
			sb.append("         sum(IF(el.open_status = 2 and logExt.merchant_type='1', 1, 0)) failureNum1, ");
			sb.append("         sum(IF(el.open_status = 2 and logExt.merchant_type='2', 1, 0)) failureNum2, ");
			sb.append("         sum(IF(el.open_status = 2 and logExt.merchant_type='3', 1, 0)) failureNum3 ");
			sb.append("      FROM examinations_log el FORCE INDEX (create_time) ");
			sb.append("        LEFT JOIN examinations_log_ext logExt ON logExt.examinations_log_id=el.id ");
			sb.append("      WHERE 1=1 ");
			if(info.getsTime() != null){
				sb.append("      AND el.create_time >= #{info.sTime} ");
			}
			if(info.geteTime() != null){
				sb.append("      AND el.create_time <= #{info.eTime} ");
			}
			if (info.getAuditorId()!=null) {
				sb.append("      AND el.operator = CONCAT(#{info.auditorId},'') ");
			}
			sb.append("          AND el.operator != '-1' ");
			sb.append("          AND el.examine_type = 1 ");
			sb.append("      GROUP BY el.operator,el.bp_id ");
			sb.append(" ) t1 ON (t1.operator = busbpd.id and t1.bpId=busbpd.bpId) ");

			sb.append("  LEFT JOIN ( ");
			sb.append("      SELECT ");
			sb.append("        mbp.auditor_id operator, ");
			sb.append("        mbp.bp_id bpId, ");
			sb.append("        count(*) notAudited, ");
			sb.append("        sum(IF(mi.merchant_type='1', 1, 0)) notAudited1, ");
			sb.append("        sum(IF(mi.merchant_type='2', 1, 0)) notAudited2, ");
			sb.append("        sum(IF(mi.merchant_type='3', 1, 0)) notAudited3 ");
			sb.append("      FROM merchant_business_product mbp ");
			sb.append("        INNER JOIN merchant_info mi ON mi.merchant_no = mbp.merchant_no ");
			sb.append("      WHERE 1=1 ");
			if(info.getsTime() != null){
				sb.append("      AND mbp.create_time >= #{info.sTime} ");
			}
			if(info.geteTime() != null){
				sb.append("      AND mbp.create_time <= #{info.eTime} ");
			}
			if (info.getAuditorId()!=null) {
				sb.append("      AND mbp.auditor_id = #{info.auditorId} ");
			}
			sb.append("          AND mbp. STATUS = 2 ");
			sb.append("          AND mbp.auditor_id IS NOT NULL ");
			sb.append("     GROUP BY  mbp.auditor_id,mbp.bp_id ");
			sb.append(" ) t2 ON (t2.operator = busbpd.id and t2.bpId=busbpd.bpId) ");

			sb.append("  WHERE ");
			sb.append("   ( t1.allCount IS NOT NULL ");
			sb.append("     OR t1.successNum IS NOT NULL ");
			sb.append("     OR t1.failureNum IS NOT NULL ");
			sb.append("     OR t2.notAudited IS NOT NULL");
			sb.append("   ) ");
			return sb.toString();
		}
	}

}
