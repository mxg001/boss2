package cn.eeepay.framework.daoCreditMgr;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmTaskInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CmTaskDao {

	/**
	 * 查询limit_add_bank_strategy里的所有银行名称
	 * @author	mays
	 * @date	2018年5月7日
	 */
	@Select("SELECT bankname text,bankname value FROM limit_add_bank_strategy GROUP BY bankname")
	List<Map<String, String>> selectBankName();

	/**
	 * 提额任务查询
	 * @author	mays
	 * @date	2018年5月7日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectTaskInfo")
	@ResultType(CmTaskInfo.class)
	List<CmTaskInfo> selectTaskInfo(@Param("page") Page<CmTaskInfo> page, @Param("info") CmTaskInfo info);

	@SelectProvider(type = SqlProvider.class, method = "selectTaskInfo")
	@ResultType(CmTaskInfo.class)
	List<CmTaskInfo> exportTaskInfo(@Param("info") CmTaskInfo info);

	/**
	 * 提额任务详情
	 * @author	mays
	 * @date	2018年5月8日
	 */
	@Select("SELECT latl.task_type,lata.task_title,latl.task_value,lata.data_type,lata.target_id,latl.card_health,"
			+ "latl.increase_possible,latl.start_time,latl.end_time FROM limit_add_task_list latl "
			+ "JOIN ( SELECT card_id,start_time,end_time FROM limit_add_task_list WHERE id = #{id} ) t "
			+ "ON t.card_id = latl.card_id AND t.start_time = latl.start_time AND t.end_time = latl.end_time "
			+ "LEFT JOIN limit_add_task_all lata ON lata.task_id = latl.task_type")
	@ResultType(CmTaskInfo.class)
	List<CmTaskInfo> queryTaskDetail(@Param("id") String id);

	public class SqlProvider {

		public String selectTaskInfo(Map<String, Object> param) {
			final CmTaskInfo info = (CmTaskInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("latl.id,cci.card_no,latl.bank_name,cci.user_no,cci.user_name,latl.card_health,latl.increase_possible,"
							+ "latl.task_have_complete/COUNT(task_type)*100 task_have_complete,latl.start_time,latl.end_time");
					FROM("limit_add_task_list latl");
					LEFT_OUTER_JOIN("cm_card_info cci ON cci.id = latl.card_id");
					if (StringUtils.isNotBlank(info.getCardNo())) {
						WHERE("cci.card_no = #{info.cardNo}");
					}
					if (StringUtils.isNotBlank(info.getUserName())) {
						WHERE("cci.user_name = #{info.userName}");
					}
					if (StringUtils.isNotBlank(info.getUserNo())) {
						WHERE("cci.user_no = #{info.userNo}");
					}
					if (StringUtils.isNotBlank(info.getBankName())) {
						WHERE("latl.bank_name = #{info.bankName}");
					}
					if (StringUtils.isNotBlank(info.getStartTime())) {
						WHERE("latl.start_time >= #{info.startTime}");
					}
					if (StringUtils.isNotBlank(info.getEndTime())) {
						WHERE("latl.end_time <= #{info.endTime}");
					}
					GROUP_BY("latl.card_id,latl.start_time,latl.end_time");
					ORDER_BY("cci.create_time desc");
				}
			};
			return sql.toString();
		}

	}

}