package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.MerchantMigrate;
import cn.eeepay.framework.model.MerchantMigrateInfo;
import cn.eeepay.framework.util.StringUtil;

/**
 * 
 * @author ws
 * @Date 2017年3月3日1:04:00
 * @see 商户迁移的所有数据库操作方法都将在这里
 *
 */
public interface MerchantMigrateDao {
	
	/**
	 * 根据商户编号获取商户详情
	 * @param agent_no 商户编号
	 * @return
	 */
	@Select("select * from agent_info where agent_no=#{agentNo}")
	@ResultType(AgentInfo.class)
	AgentInfo getAgentInfo(@Param("agentNo") String agent_no);
	
	/**
	 * 获取审核通过需要迁移的信息
	 * @return
	 */
	@Select("select * from merchant_migrate where check_status=2")
	@ResultType(MerchantMigrate.class)
	List<MerchantMigrate> migrate();
	
	/**
	 * 获取需要跨一级代理商且未迁移的迁移详细信息
	 * @param migrateId
	 * @return
	 */
	@Select("select * from merchant_migrate_info where migrate_id=#{mmId} and migrate_status=1 and modify_agent_no=1")
	@ResultType(MerchantMigrateInfo.class)
	List<MerchantMigrateInfo> getMigrateInfoByStatus(@Param("mmId") String migrateId);
	
	/**
	 * 获取商户迁移详细信息
	 * @param migrateId
	 * @return
	 */
	@Select("select * from merchant_migrate_info where migrate_id=#{mmId}")
	@ResultType(MerchantMigrateInfo.class)
	List<MerchantMigrateInfo> getAllmerchantMigrateInfo(@Param("mmId") String migrateId);
	
	/**
	 * 商户撤销
	 * @param id
	 * @return
	 */
	@Update("update merchant_migrate set check_status=5 where id=#{id} and (check_status=1 or check_status=2)")
	@ResultType(Integer.class)
	int cheXiaoMigrate(@Param("id") String id);
	
	/**
	 * 获取商户迁移子信息集合
	 * @param merchantMigrateId 商户迁移主表ID
	 * @return 返回商户迁移信息集合
	 */
	//@Select("select * from merchant_migrate_info where migrate_id=#{mmId}")
	@Select("SELECT a.agent_name one_agent_Name,b.agent_name node_agent_name,b.agent_level,m.* from merchant_migrate_info m"
			+ " INNER JOIN agent_info a on m.before_agentNo=a.agent_no INNER JOIN agent_info b on "
			+ "m.before_node_agentNo=b.agent_no WHERE m.migrate_id=#{mmId}")
	@ResultType(MerchantMigrateInfo.class)
	List<MerchantMigrateInfo> getMerchantMigrateInfoList(@Param("mmId") String merchantMigrateId, @Param("page") Page<MerchantMigrateInfo> page);
	
	/**
	 * 获取商户迁移详情
	 * @param id
	 * @return
	 */
	//@Select("select * from merchant_migrate where id=#{id}")
	@Select("SELECT a.agent_name oneAgentName,b.agent_name nodeAgentName,m.* from merchant_migrate m"
			+ " INNER JOIN agent_info a on m.agent_no=a.agent_no INNER JOIN agent_info b on m.node_agent_no=b.agent_no where m.id=#{id}")
	@ResultType(MerchantMigrate.class)
	MerchantMigrate getMerchantMigrateDetail(@Param("id") String id);
	
	/**
	 * 更新商户迁移状态
	 * @param checkStatus 状态 状态 1.待审核 2.审核通过 3.审核不通过 4.已迁移 5.已撤销 6.迁移失败 7.部分成功
	 * @param id 商户迁移ID编号
	 * @return
	 */
	@Update("update merchant_migrate set check_status=#{checkStatus} where id=#{id}")
	@ResultType(Integer.class)
	int updateMerchantMirgateCheckStatus(@Param("checkStatus") int checkStatus, @Param("id") int id);
	
	/**
	 * 修改商户迁移状态
	 * @param migrate_status 迁移状态 1.未迁移 2.已迁移 3.迁移失败 
	 * @param remark 备注
	 * @param id 商户迁移详情ID编号
	 * @return
	 */
	@Update("update merchant_migrate_info set migrate_status=#{migrate_status},remark=#{remark},migrate_time=now() where id=#{id}")
	@ResultType(Integer.class)
	int updateMerchantMigrateInfo(@Param("migrate_status") String migrate_status, @Param("remark") String remark, @Param("id") int id);
	
	/**
	 * 机具迁移
	 * @param SN 机具编号
	 * @param merchantNo 商户编号
	 * @param agentNo 所属代理商编号
	 * @param agentNode 所属代理商节点
	 * @return 受影响的行数
	 */
	@Update("update terminal_info set agent_no=#{agentNo},agent_node=#{agentNode} where merchant_no=#{merchantNo}")
	@ResultType(Integer.class)
	int updateMerchantTerminal(@Param("merchantNo") String merchantNo, @Param("agentNo") String agentNo, @Param("agentNode") String agentNode);
	
	/**
	 * 获取商户所有机具号
	 * @param merchantNo 商户编号
	 * @return 机具号集合
	 */
	@Select("select SN from terminal_info where merchant_no=#{merchantNo}")
	@ResultType(String.class)
	List<String> getMerchantTerminal(@Param("merchantNo") String merchantNo);
	
	
	/**
	 * 商户迁移-修改商户所属代理商、所属代理商节点
	 * @param agentNo 所属代理商编号
	 * @param oneAgentNo 一级代理商编号
	 * @param parentNode 所属代理商节点
	 * @param merchantNo 商户编号
	 * @return
	 */
	@Update("update merchant_info set agent_no=#{agentNo},parent_node=#{parentNode},one_agent_no=#{oneAgentNo} where merchant_no=#{merchantNo}")
	@ResultType(Integer.class)
	int modifyMerchant(@Param("agentNo") String agentNo, @Param("oneAgentNo") String oneAgentNo,@Param("parentNode") String parentNode, @Param("merchantNo") String merchantNo);
	
	/**
	 * 根据商户编号获取商户迁移信息
	 * @param merchantNo
	 * @return
	 */
	@Select("select * from merchant_migrate_info where merchant_no=#{merchantNo} and migrate_status=1")
	@ResultType(MerchantMigrateInfo.class)
	MerchantMigrateInfo findMerchantMigrateInfoByNo(@Param("merchantNo") String merchantNo);
	
	/**
	 * 获取商户使用的业务产品
	 * @param merchantNo 商户编号
	 * @return 业务产品ID集合
	 */
	@Select("select bp_id from merchant_business_product where merchant_no=#{merchantNo}")
	@ResultType(String.class)
	List<String> findMerchantBusProduct(@Param("merchantNo") String merchantNo);
	
	/**
	 * 
	 * @param agentNo
	 * @return
	 */
	@Select("select bp_id from agent_business_product where agent_no=#{agentNo} and status=1 order by bp_id")
	@ResultType(java.lang.String.class)
	public List<String> findAgentBusProduct(@Param("agentNo") String agentNo);
	
	/**
	 * 商户迁移组合查询
	 * @param param 查询条件
	 * @return Map集合
	 */
	@SelectProvider(type=MerchantMigrateDaoSQL.class,method="findParam")
	@ResultType(MerchantMigrate.class)
	public List<MerchantMigrate> findMerchantMigrate(@Param("page") Page<MerchantMigrate> page, @Param("mm") MerchantMigrate merchantMigrate);
	
	
	/**
	 * @see 商户迁移信息详情
	 * @param id 商户迁移详情ID编号
	 * @return Map
	 */
	@Insert("insert into merchant_migrate_info(migrate_id,merchant_no,before_agentNo,before_node_agentNo,modify_agent_no) values"
			+ "(#{mm.migrateId},#{mm.merchantNo},#{mm.beforeAgentNo},#{mm.beforeNodeAgentNo},#{mm.modifyAgentNo})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "mm.id", before = false, resultType = Integer.class)
	int addMirgrateInfo(@Param("mm") MerchantMigrateInfo param);
	
	
	/**
	 * @see 新增商户迁移信息
	 * @param merchantMigrate 商户迁移对象
	 * @return 受影响的行数
	 */
	@Insert("insert into merchant_migrate(oa_no,agent_no,node_agent_no,create_person,go_sn,file_name) values"
			+ "(#{mm.oaNo},#{mm.agentNo},#{mm.nodeAgentNo},#{mm.createPerson},#{mm.goSn},#{mm.fileName})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "mm.id", before = false ,resultType = Integer.class)
	int addMerchantMigrate(@Param("mm") MerchantMigrate mm);
	
	/**
	 * @see 商户迁移审核
	 * @param check_status 审核状态
	 * @param check_remark 审核描述
	 * @param check_person 审核人
	 * @param id 记录ID编号
	 * @return 受影响的行数
	 */
	@Update("update merchant_migrate set check_status=#{check_status},check_remark=#{check_remark},"
			+ "check_person=#{check_person},check_time=now() where id=#{id}")
	@ResultType(Integer.class)
	public int merchantMigrateCheck(@Param("check_status") String check_status, @Param("check_remark") String check_remark, @Param("check_person") String check_person,@Param("id") String id);
	
	/**
	 * @see 商户迁移信息变更
	 * @param merchantMigrate 商户迁移对象
	 * @return 受影响的行数
	 */
	@Select("update merchant_migrate set oa_no=#{mm.oa_no},agent_no=#{mm.agent_no},node_agent_no=#{mm.node_agent_no},"
			+ "create_person=#{create_person}，go_sn=#{mm.go_sn},file_name=#{mm.file_name} where id=#{mm.id}")
	public int modifyMerchantMigrate(@Param("mm") MerchantMigrate mm);
	
	/**
	 * 加载所有一级代理商
	 * @return
	 */
	@Select("select * from agent_info a where a.agent_level=1")
	@ResultType(AgentInfo.class)
	public List<AgentInfo> getAllAgentInfo();
	
	/**
	 *  and agent_node like #{agent_no}
	 * @see 加载一级代理商下所有子代理商
	 * @param agentNo 一级代理商编号
	 * @return Map集合 子代理商编号-代理商名称
	 */
	@Select("select * from agent_info where agent_node like concat('0-',#{agent_no},'-%')")
	@ResultType(AgentInfo.class)
	public List<AgentInfo> getNodeAgent(String agentNo);
	
	/**
	 * 主表在修改迁移状态为：撤销、审核失败、部分成功时，需要修改子表迁移状态
	 * @author tans
	 * @date 2017年4月6日 下午1:52:49
	 * @param mmId
	 * @param migrateStatus
	 * @return
	 */
	@Update("update merchant_migrate_info set migrate_status=#{migrateStatus} where migrate_id=#{mmId}")
	int updateInfoBy(@Param("mmId")String mmId, @Param("migrateStatus")int migrateStatus);
	/**
	 * ivan
	 * 更新商户参与的活动代理商节点
	 * @param merchantNo
	 * @param agentParentNode
	 */
	@Update("update activity_detail set agent_node=#{agentParentNode} where merchant_no=#{merchantNo}")
	void updateActivityAgentNode(@Param("merchantNo")String merchantNo, @Param("agentParentNode")String agentParentNode);

	/***
	 *
	 * @param merchantNos 商户编号
	 * @param agentNode 目标直属代理商
	 */
	@Update("update collective_trans_order set agent_node=#{agentNode} where merchant_no in (#{merchantNos}) ")
    void changeCollectiveTransOrder(@Param("merchantNos") String merchantNos, @Param("agentNode") String agentNode);

    /**
	 * 
	 * @author ws
	 * @date 2017年3月3日1:08:22
	 * @see  属于MerchantMigrateDao内部类,该内部类仅用于多条件组合的SQL语句
	 *
	 */
	public class MerchantMigrateDaoSQL{
		
		/**
		 * @see 商户迁移多条件组合查询SQL语句
		 * @param param 查询条件
		 * @return 基于倒序的多条件组合查询SQL语句
		 */
		public String findParam(Map<String, Object> param){
			final MerchantMigrate mm = (MerchantMigrate)param.get("mm");
			String sql = new SQL(){{
				SELECT(" a.agent_name oneAgentName,b.agent_name nodeAgentName,m.* ");
				FROM("merchant_migrate m");
				INNER_JOIN("agent_info a on m.agent_no=a.agent_no");
				INNER_JOIN("agent_info b on m.node_agent_no=b.agent_no");
				WHERE("1=1");
				if(StringUtil.isNotBlank(mm.getAgentNo())){
					WHERE("m.agent_no=#{mm.agentNo}");
				}
				if(StringUtil.isNotBlank(mm.getNodeAgentNo())){
					WHERE("m.node_agent_no=#{mm.nodeAgentNo}");
				}
				if(!StringUtil.isEmpty(mm.getCheckPerson())){
					WHERE("m.check_person=#{mm.checkPerson}");
				}
				if(!StringUtil.isEmpty(mm.getCreatePerson())){
					WHERE("m.create_person=#{mm.createPerson}");
				}
				if(null != mm.getsTime()){
					WHERE("m.create_time>=#{mm.sTime}");
				}
				if(null != mm.geteTime()){
					WHERE("m.create_time<=#{mm.eTime}");
				}
				if(!"-1".equals(mm.getCheckStatus())){
					WHERE("m.check_status=#{mm.checkStatus}");
				}
				if(!"".equals(mm.getOaNo())){
					WHERE("m.oa_no=#{mm.oaNo}");
				}
				ORDER_BY("m.create_time desc");
			}}.toString();
			System.out.println(sql);
			return sql;
		}
	}
}
