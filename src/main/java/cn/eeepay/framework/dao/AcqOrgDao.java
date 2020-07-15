package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcpWhitelist;
import cn.eeepay.framework.model.AcqOrg;

public interface AcqOrgDao {

	@Select("select * from acq_org where id=#{id}")
	@ResultType(AcqOrg.class)
	AcqOrg selectByPrimaryKey(Integer id);

	@Update("update acq_org set settle_type=#{record.settleType},day_altered_time=#{record.dayAlteredTime},"
			+ "settle_account_id=#{record.settleAccountId},acq_trans_have_out=#{record.acqTransHaveOut},realtime_T0greaterT1=#{record.realtimeT0greatert1},"
			+ "acq_success_amount=#{record.acqSuccessAmount},phone=#{record.phone},acq_def_dayAmount=#{record.acqDefDayamount},valves_amount=#{record.valvesAmount}"
			+ ",dayAmount_T0greaterT1=#{record.dayamountT0greatert1}" + " where id=#{record.id}")
	int updateByPrimaryKey(@Param("record") AcqOrg record);

	// 收单机构白名单查询
	@Select("select * from acp_whitelist where acq_id=#{acqId}")
	@ResultType(AcpWhitelist.class)
	List<AcpWhitelist> selectAllWlInfo(@Param("acqId") int acqId);

	// 查询收单机构白名单是否存在
	@Select("select * from acp_whitelist where merchant_no=#{record.merchantNo} and acq_id=#{record.acqId}")
	@ResultType(AcpWhitelist.class)
	AcpWhitelist selectWlInfo(@Param("record") AcpWhitelist record);

	// 收单机构白名单删除
	@Delete("delete from acp_whitelist where id=#{id}")
	int deleteByWlid(@Param("id") int id);

	// 收单机构白名单新增
	@Insert("insert into acp_whitelist(merchant_no,acq_id,create_person) "
			+ "values(#{record.merchantNo},#{record.acqId},#{record.createPerson})")
	int insertWl(@Param("record") AcpWhitelist record);

	// 开通和关闭状态
	@Update("update acq_org set acq_status=#{record.acqStatus},acq_close_tips=#{record.acqCloseTips} where id=#{record.id}")
	int updateStatusByid(@Param("record") AcqOrg record);

	@SelectProvider(type = SqlProvider.class, method = "selectAllInfo")
	@ResultType(AcqOrg.class)
	List<AcqOrg> selectAllInfo(@Param("page") Page<AcqOrg> page, @Param("ao") AcqOrg ao);

	@Select("select * from acq_org")
	@ResultType(AcqOrg.class)
	List<AcqOrg> selectBoxAllInfo();

	/**
	 * 获取所有直清收单机构
	 * @return
	 */
	@Select("select * from acq_org where settle_type = 2")
	@ResultType(AcqOrg.class)
	List<AcqOrg> selectAllZqOrg();

	/**
	 * 通过收单机构名称获取id
	 * @return
	 */
	@Select("select id from acq_org where acq_name = #{acqName}")
	@ResultType(AcqOrg.class)
	AcqOrg selectInfoByName(@Param("acqName") String acqName);

	public class SqlProvider {

		public String selectAllInfo(Map<String, Object> param) {
			final AcqOrg ao = (AcqOrg) param.get("ao");
			return new SQL() {
				{
					SELECT("*");
					FROM("acq_org");
					if (ao.getId() != null) {
						WHERE(" id=#{ao.id}");
					}
					if (StringUtils.isNotBlank(ao.getAcqName())) {
						ao.setAcqName(ao.getAcqName() + "%");
						WHERE(" acq_name like #{ao.acqName}");
					}
					if (ao.getAcqStatus() != null && ao.getAcqStatus() != -1) {
						WHERE(" acq_status=#{ao.acqStatus}");
					}
				}
			}.toString();
		}
	}

	/**
	 * 收单机构新增 by sober
	 * 
	 * @param acqOrg
	 * @return
	 */
	@Insert("INSERT INTO acq_org (acq_name, acq_enname,HOST, PORT, acq_status, settle_type,"
			+ "day_altered_time,acq_trans_have_out,acq_success_amount,phone, acq_def_dayAmount, "
			+ "dayAmount_T0greaterT1, t0_advance_money, t0_own_money, valves_amount,t0_trans_advance_amount, "
			+ "create_person,settle_account_id,realtime_T0greaterT1) "
			+ "values(#{acqOrg.acqName},#{acqOrg.acqName},#{acqOrg.host},#{acqOrg.port},#{acqOrg.acqStatus},#{acqOrg.settleType},"
			+ "#{acqOrg.dayAlteredTime},#{acqOrg.acqTransHaveOut},#{acqOrg.acqSuccessAmount},#{acqOrg.phone},#{acqOrg.acqDefDayamount},"
			+ "#{acqOrg.dayamountT0greatert1},#{acqOrg.t0AdvanceMoney},#{acqOrg.t0OwnMoney},#{acqOrg.valvesAmount},#{acqOrg.t0TransAdvanceAmount},"
			+ "#{acqOrg.createPerson},#{acqOrg.settleAccountId},#{acqOrg.realtimeT0greatert1})")
	int addAcqOrg(@Param("acqOrg")AcqOrg acqOrg);
	@Update("update acq_org set channel_status=#{acqOrg.channelStatus} where id=#{acqOrg.id}")
	int updateChannelStatusByid(@Param("acqOrg")AcqOrg acqOrg);
}