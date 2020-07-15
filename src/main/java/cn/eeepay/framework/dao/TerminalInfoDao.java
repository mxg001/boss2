package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.allAgent.TerInfo;
import cn.eeepay.framework.model.TerActivityCheck;
import cn.eeepay.framework.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface TerminalInfoDao {
	
	final Logger log = LoggerFactory.getLogger(TerminalInfoDao.class);
	
	//229tgh
	@Select("SELECT sys_name,sys_value FROM sys_dict WHERE sys_key = 'ACTIVITY_TYPE'")
	@ResultType(Map.class)
	List<Map<String, String>> selectAllActivityType();

    @Insert("insert into terminal_info(sn,PSAM_NO,open_status,type,CREATE_TIME,cashier_no,pos_type,activity_type,channel,model,activity_type_no)"
    		+ "values(#{record.sn},#{record.psamNo},#{record.openStatus},#{record.type},#{record.createTime},#{record.cashierNo},"
    		+ "#{record.posType},#{record.activityType},#{record.channel},#{record.model},#{record.activityTypeNo})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class) 
    int insert(@Param("record")TerminalInfo record);
    
    
    @Insert("insert into terminal_info(sn,PSAM_NO,open_status,type,CREATE_TIME,agent_no,agent_node,pos_type,activity_type,channel,model,activity_type_no)"
    		+ "values(#{record.sn},#{record.psamNo},#{record.openStatus},#{record.type},#{record.createTime}"
    		+ ",#{record.agentNo},#{record.agentNode},#{record.posType},#{record.activityType},#{record.channel},#{record.model},#{record.activityTypeNo})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class) 
    int insertSelective(@Param("record")TerminalInfo record);
    
    
    @Insert("insert into terminal_info(sn,PSAM_NO,open_status,type,CREATE_TIME,agent_no,agent_node,pos_type,merchant_no,cashier_no,bp_id,collection_code)"
    		+ "values(nextval('terminal_id_seq'),#{record.psamNo},#{record.openStatus},#{record.type},#{record.createTime}"
    		+ ",#{record.agentNo},#{record.agentNode},#{record.posType},#{record.merchantNo},#{record.cashierNo},#{record.bpId},#{record.collectionCode})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class) 
    int insertPsamInfo(@Param("record")TerminalInfo record);
    
    @Update("update terminal_info set sn=#{record.sn},PSAM_NO=#{record.psamNo},type=#{record.type}," +
            "pos_type=#{record.posType},activity_type=#{record.activityType},channel=#{record.channel},activity_type_no=#{record.activityTypeNo} where id=#{record.id}")
    int updateByPrimaryKey(@Param("record")TerminalInfo record);
    
    //解分/分配
    @Update("update terminal_info set agent_no=#{record.agentNo},agent_node=#{record.agentNode},open_status=#{record.openStatus} where id=#{record.id}")
    int solutionById(@Param("record")TerminalInfo record);
    
    //绑定
//    @Update("update terminal_info set merchant_no=#{record.merchantNo},open_status=#{record.openStatus},START_TIME=#{record.startTime},terminal_id=nextval('terminal_id_seq'),bp_id=#{record.bpId} where id=#{record.id}")
    @Update("update terminal_info set merchant_no=#{record.merchantNo},open_status=#{record.openStatus},START_TIME=#{record.startTime},bp_id=#{record.bpId} where id=#{record.id}")
    int bundlingById(@Param("record")TerminalInfo record);


//	@Update("<script>"
//			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
//			+ "         close=\"\" separator=\";\"> "
//			+ " update terminal_info "
//			+ " set merchant_no=#{item.merchantNo},"
//			+ " open_status=#{item.openStatus},"
//			+ " START_TIME=#{item.startTime},"
//			+ " bp_id=#{item.bpId}"
//			+ "   where SN=#{item.sn}"
//			+ "     </foreach> "
//			+ " </script>")
	@UpdateProvider(type = SqlProvider.class, method = "batchBundlingBySn")
	@ResultType(Integer.class)
	int batchBundlingBySn(@Param("list")List<TerminalInfo> list);
    
//    @Update("update terminal_info set open_status=#{openStatus},merchant_no=#{merchantNo},START_TIME=#{startTime},terminal_id=nextval('terminal_id_seq'),bp_id=#{bpId} where agent_no=#{agentNo} and sn=#{sn}")
    @Update("update terminal_info set open_status=#{openStatus},merchant_no=#{merchantNo},START_TIME=#{startTime},bp_id=#{bpId} where agent_no=#{agentNo} and sn=#{sn}")
   	int updateBundlingBySn(TerminalInfo terminalInfo);

	@Insert(
			"INSERT INTO push_record " +
					"(source_id,push_type,create_time) " +
					"VALUES " +
					"(#{info.sn},'terminal',NOW())"
	)
	int insertPushRecord(@Param("info")TerminalInfo terminalInfo);
    
    //解绑
//    @Update("update terminal_info set merchant_no=null,open_status=1,START_TIME=null,terminal_id=null,bp_id=null where id=#{id}")
    @Update("update terminal_info set merchant_no=null,open_status=1,START_TIME=null,bp_id=null where id=#{id}")
    int unbundlingById(@Param("id")Long id);
    
    @Update("update terminal_info set type=#{record.type},activity_type=#{record.activityType},activity_type_no=#{record.activityTypeNo} " +
			" where sn >= #{record.snStart} and sn <= #{record.snEnd}")
    int updateAllTerActivity(@Param("record")TerminalInfo record);

	@Select("select SN from terminal_info where open_status='2' and sn >= #{snStart} and sn <= #{snEnd}")
    @ResultType(TerminalInfo.class)
	List<TerminalInfo> selectAllTerActivity(TerminalInfo terInfo);
    
//    @Select("select ti.*,mi.merchant_name,bpd.bp_name,"
//    		+ "ai.agent_name,ai.agent_level from terminal_info ti "
//    		+ "left JOIN merchant_info mi on ti.merchant_no=mi.merchant_no "
//    		+ "left JOIN agent_info ai on ti.agent_no=ai.agent_no "
//    		+ "left JOIN business_product_define bpd on bpd.bp_id=ti.bp_id")
//    @ResultType(TerminalInfo.class)
//    List<TerminalInfo> selectAllInfo(@Param("page")Page<TerminalInfo> page);
    
    @Select("select tis.*,bpd.bp_name,hp.type_name from terminal_info tis "
    		+ "left JOIN business_product_define bpd on bpd.bp_id=tis.bp_id LEFT JOIN hardware_product hp on hp.hp_id = tis.type "
    		+ "where tis.merchant_no=#{merNo} and tis.bp_id=#{bpId}")
    @ResultType(TerminalInfo.class)
    List<TerminalInfo> selectAllInfoBymerNoAndBpId(@Param("merNo")String merNo,@Param("bpId")String bpId, @Param("page")Page<TerminalInfo> tiPage);
    
    @Select("select * from terminal_info where "
    		+ "sn=#{record.sn} or PSAM_NO=#{record.psamNo} limit 1")
    @ResultType(TerminalInfo.class)
    TerminalInfo selectBySameData(@Param("record")TerminalInfo record);
    
    @Select("select ti.*,mi.merchant_name,ai.agent_name,ai.agent_level,hp.type_name,hp.version_nu,ti.activity_type "
    		+ "from terminal_info ti "
    		+ "left JOIN merchant_info mi on ti.merchant_no=mi.merchant_no "
    		+ "left JOIN agent_info ai on ti.agent_no=ai.agent_no "
    		+ "left JOIN hardware_product hp on hp.hp_id=ti.type where ti.id=#{id}")
    @ResultType(TerminalInfo.class)
    TerminalInfo selectObjInfo(@Param("id") Long id);
    
    @SelectProvider(type=SqlProvider.class,method="selectByParam")
    @ResultType(TerminalInfo.class)
    List<TerminalInfo> selectByParam(@Param("page")Page<TerminalInfo> page,@Param("terminalInfo")TerminalInfo terminalInfo);

	@SelectProvider(type=SqlProvider.class,method="selectWithTACByCondition")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectWithTACByCondition(@Param("page") Page<TerminalInfo> page, @Param("info") TerminalInfo info, @Param("pns") String pns);

	/**
	 *机具表导出
	 */
	@SelectProvider(type=SqlProvider.class,method="selectByParam")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> importDetailSelect(@Param("terminalInfo")TerminalInfo terminalInfo);

	/**
	 * 机具活动考核列表导出查询
	 * @param info
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectWithTACByCondition")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> exportTerminalWithTAC(@Param("info") TerminalInfo info, @Param("pns") String pns);

    @SelectProvider(type=SqlProvider.class,method="selectByAddParam")
    @ResultType(TerminalInfo.class)
    List<TerminalInfo> selectByAddParam(@Param("terminalInfo")TerminalInfo terminalInfo);
    
    @InsertProvider(type=SqlProvider.class, method="insertBatch")
	int insertBatch(@Param("list")List<TerminalInfo> list);
    
    @SelectProvider(type=SqlProvider.class,method = "selectByIds")
    @ResultType(TerminalInfo.class)
    List<TerminalInfo> selectByIds(@Param("list")String[] list);
    
    @Select("select collection_code from terminal_info where open_status='2' and merchant_no=#{merchantNo}")
	String getReceivableCodeByMerchant(@Param("merchantNo")String merchantNo);
    
    @Select("select sn,merchant_no,agent_no,PSAM_NO,type from terminal_info where sn=#{sn}")
    TerminalInfo getBySn(@Param("sn")String sn);
    
    @UpdateProvider(type=SqlProvider.class, method="updateOpenStatusBatch")
   	int updateOpenStatusBatch(@Param("list")List<String> list,@Param("openStatus")String opentStatus);
    
//    @Select("select merchant_no from terminal_info where FIND_IN_SET(#{activityType},activity_type) and "
//    		+ " id=#{id}")
    @SelectProvider(type=SqlProvider.class, method="selectByIdAndActivity")
    @ResultType(TerminalInfo.class)
	TerminalInfo selectByIdAndActivity(@Param("id")Long id, @Param("activityTypeList")String[] activityTypeList);
    
    @Select("select ti.sn,bpd.bp_name from terminal_info ti inner join business_product_define bpd on bpd.bp_id=ti.bp_id"
    		+ " where ti.merchant_no=#{merchantNo}")
    @ResultType(TerminalInfo.class)
    List<TerminalInfo> getPageByMerchant(@Param("merchantNo")String merchantNo, @Param("page")Page<TerminalInfo> page);

    @Select("select * from terminal_info where id = #{id}")
    @ResultType(TerminalInfo.class)
    TerminalInfo selectById(@Param("id") Long id);

	@SelectProvider(type=SqlProvider.class,method="querySNList")
	@ResultType(TerInfo.class)
	List<TerInfo> querySNList(@Param("info") TerInfo info,@Param("page") Page<TerInfo> page);

	@SelectProvider(type=SqlProvider.class,method="querySNList")
	@ResultType(TerInfo.class)
	List<TerInfo> querySNByTerInfo(@Param("info") TerInfo info);

	@Select("select pti.sn,ti.type from pa_ter_info pti LEFT JOIN terminal_info ti on ti.SN=pti.sn where pti.order_no = #{orderNo}")
	@ResultType(TerInfo.class)
	List<TerInfo> queryShipMachineDetail(@Param("orderNo") String orderNo,@Param("page") Page<TerInfo> page);

	@Select("select pti.* from pa_ter_info pti LEFT JOIN terminal_info ti on ti.SN=pti.sn where ti.id = #{terId}")
	@ResultType(TerInfo.class)
	TerInfo selectAllAgentByTerminalId(@Param("terId") Long terId);

	@Select("select pti.* from pa_ter_info pti LEFT JOIN terminal_info ti on ti.SN=pti.sn where ti.SN = #{sn}")
	@ResultType(TerInfo.class)
	TerInfo selectAllAgentByTerminalSN(@Param("sn") String sn);

	@Update("update terminal_info set agent_no=null , agent_node=null , open_status=#{openStatus} where id=#{id}")
	int updateOpenStatus(@Param("id") Long id,@Param("openStatus")String openStatus);

	@Delete("delete from pa_ter_info where id=#{id}")
	int deleteAllAgentTerInfo(@Param("id") int id);

	@Update("update pa_ter_info set status=#{status},merchant_no=#{merchantNo} where id=#{id}")
	int updateAllAgentTerInfo(@Param("id") int id,@Param("status") int status,@Param("merchantNo") String merchantNo);

	/**
	 *根据sn查询机具
	 */
	@Select(
			"select id,SN,open_status,type,activity_type from terminal_info where SN=#{sn}"
	)
	TerminalInfo getTerminalInfoBySn(@Param("sn")String sn);

	/**
	 *根据SN更换机具type
	 */
	@Update(
			"update terminal_info set type=#{info.type},activity_type_no=#{info.activityTypeNo} where SN=#{info.sn}"
	)
    int updateTerminalType(@Param("info")TerminalInfo addInfo);

	@Select("select * from terminal_info where activity_type_no=#{activityTypeNo}")
	@ResultType(TerInfo.class)
	List<TerInfo> queryHlfBindSn(@Param("activityTypeNo") String activityTypeNo);
	@Update("<script>" +
			" update terminal_info set agent_no = #{agentNo},agent_node = #{agentNode},open_status = #{openStatus}" +
			" where sn in " +
			" <foreach collection='snList' item='item' open='(' close=')' separator=','>" +
			" #{item}" +
			" </foreach>" +
			" and open_status = '0'" +
			"</script>")
	int updateBatchIssued(@Param("snList") List<String> snList, @Param("agentNo") String agentNo,
						  @Param("agentNode") String agentNode, @Param("openStatus") String openStatus);

	@Select("<script>" +
			" select sn from terminal_info where sn &gt;= #{snStart} and sn &lt;= #{snEnd} " +
//			" and FIND_IN_SET(#{cjtActivityType}, activity_type) and open_status = #{openStatus}" +
			" and open_status = #{openStatus}" +
			" and type in" +
			" <foreach collection='hpIdList' item='item' open='(' close=')' separator=','>" +
			" #{item}" +
			"</foreach>" +
			"</script>")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectSnBy(@Param("snStart") String snStart, @Param("snEnd") String snEnd,
						  @Param("openStatus") String openStatus, @Param("hpIdList") List<Integer> hpIdList);

	@Update("update terminal_info set agent_no = #{agentNo},agent_node = #{agentNode},open_status = #{openStatus} " +
			" where sn >= #{snStart} and sn <= #{snEnd} and open_status = '0' ")
	int updateBatchIssuedSnRange(@Param("snStart") String snStart, @Param("snEnd") String snEnd,
								 @Param("agentNo") String agentNo, @Param("agentNode") String agentNode,
								 @Param("openStatus") String openStatus);

	@SelectProvider(type = SqlProvider.class, method = "selectCjtTerPage")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectCjtTerPage(@Param("baseInfo") TerminalInfo baseInfo, @Param("page") Page<TerminalInfo> page);


	@Insert("INSERT INTO terminal_operate " +
					"(agent_no,for_operater,oper_num,sn_array,oper_detail_type,oper_type,create_time) " +
					"SELECT agent_no,'平台','1',SN,#{oper_detail_type},#{oper_type},NOW() from terminal_info where id=#{id} ")
	int insertTerminalOperate(@Param("id")Long id,@Param("oper_detail_type")Integer oper_detail_type,@Param("oper_type")Integer oper_type);

	@Insert("replace INTO agent_terminal_operate " +
			"(agent_no,sn,oper_detail_type,oper_type,create_time) values " +
			"(#{agentNo},#{sn},#{oper_detail_type},#{oper_type},NOW())")
	int insertAgentRerminalOperate(@Param("agentNo")String agentNo,@Param("sn")String sn,@Param("oper_detail_type")Integer oper_detail_type,@Param("oper_type")Integer oper_type);

	@Insert("INSERT INTO `ter_activity_check`(`sn`,`due_days`,`check_time`) VALUES(#{tac.sn},#{tac.dueDays},#{tac.checkTime})")
	int insertOne(@Param("tac") TerActivityCheck tac);

	@Select("SELECT * FROM `ter_activity_check` WHERE sn=#{sn}")
	@ResultType(TerActivityCheck.class)
    TerActivityCheck selectTACBySn(@Param("sn") String sn);

	@Update("UPDATE `ter_activity_check` SET due_days=#{tac.dueDays} WHERE sn=#{tac.sn}")
	int updateDueDays(@Param("tac") TerActivityCheck tac);

	@Update("UPDATE ter_activity_check " +
			"SET due_days=due_days-1 " +
			"WHERE due_days <= 0 OR (due_days > 0 && `status`=0)")
    int updateDueDaysBatch();

	@Select("SELECT * FROM terminal_info WHERE sn=#{sn}")
	@ResultType(TerminalInfo.class)
	TerminalInfo selectBySn(@Param("sn") String sn);


	class SqlProvider{

		public String batchBundlingBySn(Map<String, Object> param){
			final List<TerminalInfo> list = (List<TerminalInfo>) param.get("list");
			StringBuilder sb = new StringBuilder();
			MessageFormat message = new MessageFormat("UPDATE terminal_info" +
					" SET merchant_no =#'{'list[{0}].merchantNo}, open_status =#'{'list[{0}].openStatus}, start_time =now(), bp_id =#'{'list[{0}].bpId}" +
					" WHERE sn =#'{'list[{0}].sn};");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			return sb.toString();
		}

    	public String selectByIds(Map<String, Object> param){
    		final String[] list = (String[]) param.get("list");
    			StringBuilder sb = new StringBuilder();
    			sb.append("select concat(\"NO.\",SN) SN, PSAM_NO from terminal_info where id in(");
    			MessageFormat message = new MessageFormat("#'{'list[{0}]},");
    			for(int i=0; i<list.length;i++){
    				sb.append(message.format(new Integer[]{i}));
    			}
    			sb.setLength(sb.length()-1);
        		sb.append(");");
    		return sb.toString();
    	}
    	
		public String selectByParam(Map<String,Object> param){
			final TerminalInfo terminalInfo=(TerminalInfo)param.get("terminalInfo");
			String sql = new SQL(){{
				SELECT("(select 1 from secret_key sk WHERE sk.device_id = ti.SN LIMIT 1 ) hasKey, ti.*," +
						"mi.merchant_name,ai.agent_name,ai.agent_level,bpd.bp_name,ai2.agent_name as one_agent_name ");
				SELECT("ats.ter_no,ats.union_mer_no,pti.user_code,aht.activity_type_name activityTypeNoName");
				FROM("terminal_info ti "
						+ "left JOIN merchant_info mi on ti.merchant_no=mi.merchant_no "
						+ "left JOIN agent_info ai on ti.agent_no=ai.agent_no "
						+ "left JOIN business_product_define bpd on bpd.bp_id=ti.bp_id "
						+ "LEFT JOIN agent_info  ai2 on ai.one_level_id =ai2.agent_no "
						+ "LEFT JOIN acq_terminal_store ats on ats.sn =ti.sn and acq_enname = 'YS_ZQ' "
						+ "LEFT JOIN pa_ter_info pti on pti.sn=ti.SN "
						+ "LEFT JOIN activity_hardware_type aht ON aht.activity_type_no=ti.activity_type_no "
				);

				if(StringUtils.isNotBlank(terminalInfo.getMerchantName())){
					WHERE("(mi.merchant_no= #{terminalInfo.merchantName} or mi.merchant_name = #{terminalInfo.merchantName})");
				}
				
				if(StringUtils.isNotBlank(terminalInfo.getChannel())){
					WHERE("ti.channel= #{terminalInfo.channel}");
				}
				
				if(StringUtils.isNotBlank(terminalInfo.getSnStart())&&StringUtils.isBlank(terminalInfo.getSnEnd())){
					WHERE("ti.sn like concat(#{terminalInfo.snStart},'%')");
				} else if(StringUtils.isBlank(terminalInfo.getSnStart())&&StringUtils.isNotBlank(terminalInfo.getSnEnd())){
					WHERE("ti.sn like concat('%',#{terminalInfo.snEnd},'%')");
				} else if(StringUtils.isNotBlank(terminalInfo.getSnStart())&&StringUtils.isNotBlank(terminalInfo.getSnEnd())){
					WHERE("ti.sn >= #{terminalInfo.snStart} and ti.sn <= #{terminalInfo.snEnd}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getBpId())){
					WHERE("bpd.bp_id = #{terminalInfo.bpId}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getType()) &&!StringUtils.equals("-1", terminalInfo.getType())){
					WHERE("ti.type = #{terminalInfo.type}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getOpenStatus()) &&!StringUtils.equals("-1", terminalInfo.getOpenStatus())){
					WHERE("ti.open_status =#{terminalInfo.openStatus}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getAgentNo()) && !"-1".equals(terminalInfo.getAgentNo())){
					if(StringUtils.isNotBlank(terminalInfo.getBool())){
						WHERE("ti.agent_node LIKE #{terminalInfo.bool}");
					}else{
						WHERE("ai.agent_no = #{terminalInfo.agentNo}");
					}
				}
				if(terminalInfo.getHasKey()!=null && terminalInfo.getHasKey()!=-1){
					if(terminalInfo.getHasKey() == 1){
						WHERE("1=1 AND EXISTS(SELECT 1 FROM secret_key sk2 WHERE sk2.device_id = ti.SN)");
					} else if(terminalInfo.getHasKey() == 0){
						WHERE("1=1 AND NOT EXISTS(SELECT 1 FROM secret_key sk2 WHERE sk2.device_id = ti.SN)");
					}
				}
				if(StringUtils.isNotBlank(terminalInfo.getPsamNo())&&StringUtils.isBlank(terminalInfo.getPsamNo1())){
					WHERE("ti.PSAM_NO like concat('%',#{terminalInfo.psamNo},'%')");
				} else if(StringUtils.isBlank(terminalInfo.getPsamNo())&&StringUtils.isNotBlank(terminalInfo.getPsamNo1())){
					WHERE("ti.PSAM_NO like concat('%',#{terminalInfo.psamNo1},'%')");
				} else if(StringUtils.isNotBlank(terminalInfo.getPsamNo())&&StringUtils.isNotBlank(terminalInfo.getPsamNo1())){
					WHERE("ti.PSAM_NO >= #{terminalInfo.psamNo} and ti.PSAM_NO <= #{terminalInfo.psamNo1}");
				}
				if(terminalInfo.getStartTimeBegin()!=null){
					WHERE("ti.START_TIME>=#{terminalInfo.startTimeBegin}");
				}
				if(terminalInfo.getStartTimeEnd()!=null){
					WHERE("ti.START_TIME<=#{terminalInfo.startTimeEnd}");
				}
				//20170116tgh
				if(StringUtils.isNotBlank(terminalInfo.getActivityType())){
					WHERE("FIND_IN_SET(#{terminalInfo.activityType},ti.activity_type)");
				}
				if(StringUtils.isNotBlank(terminalInfo.getTerNo())){
					WHERE("ats.ter_no = #{terminalInfo.terNo}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getUserCode())){
					WHERE("pti.user_code = #{terminalInfo.userCode}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getActivityTypeNo())){
					WHERE("ti.activity_type_no = #{terminalInfo.activityTypeNo}");
				}
			}}.toString();
			log.info("机具条件查询的sql：");
			log.info(sql);
			return sql;
		}


		public String selectWithTACByCondition(Map<String, Object> param){
			final TerminalInfo info = (TerminalInfo) param.get("info");
			final String pns = (String)param.get("pns");
			SQL sql = new SQL(){
				{
					SELECT("ti.*," +
							"hp.type_name," +
							"tac.due_days,due_days as dueDaysValue,tac.`status`,tac.`check_time`," +
							"bpd.bp_name," +
							"ai1.agent_no,ai1.agent_name,ai2.agent_no AS one_agent_no,ai2.agent_name AS one_agent_name," +
							"aht.activity_type_name activityTypeNoName");
					FROM("terminal_info ti");
					JOIN("ter_activity_check tac ON ti.sn=tac.sn");
					LEFT_OUTER_JOIN("agent_info ai1 ON ti.agent_no=ai1.agent_no");
					LEFT_OUTER_JOIN("agent_info ai2 ON ai1.one_level_id =ai2.agent_no");
					LEFT_OUTER_JOIN("hardware_product hp ON hp.hp_id=ti.type");
					LEFT_OUTER_JOIN("business_product_define bpd ON bpd.bp_id=ti.bp_id");
					LEFT_OUTER_JOIN("activity_hardware_type aht ON aht.activity_type_no=ti.activity_type_no");
					WHERE("hp.device_pn in " + pns);
					if(StringUtils.isNotBlank(info.getSnStart())&&StringUtils.isBlank(info.getSnEnd())){
						WHERE("ti.sn like concat(#{info.snStart},'%')");
					} else if(StringUtils.isBlank(info.getSnStart())&&StringUtils.isNotBlank(info.getSnEnd())){
						WHERE("ti.sn like concat('%',#{info.snEnd},'%')");
					} else if(StringUtils.isNotBlank(info.getSnStart())&&StringUtils.isNotBlank(info.getSnEnd())){
						WHERE("ti.sn >= #{info.snStart} and ti.sn <= #{info.snEnd}");
					}
					if(StringUtils.isNotBlank(info.getDaysStart())){
						WHERE("tac.due_days >= #{info.daysStart}");
					}
					if(StringUtils.isNotBlank(info.getDaysEnd())){
						WHERE("tac.due_days <= #{info.daysEnd}");
					}
					if(StringUtils.isNotBlank(info.getOneAgentNo())){
						WHERE("ai2.agent_no=#{info.oneAgentNo}");
					}
					if(StringUtils.isNotBlank(info.getAgentNo())){
						if("1".equals(info.getBool())){
							WHERE("ti.agent_node like #{info.agentNo}");
						}else {
							WHERE("ai1.agent_no = #{info.agentNo}");
						}
					}
					if(StringUtils.isNotBlank(info.getType())){
						WHERE("ti.type=#{info.type}");
					}
					if(StringUtils.isNotBlank(info.getBpId())){
						WHERE("ti.bp_id=#{info.bpId}");
					}
					if(StringUtils.isNotBlank(info.getActivityTypeNo())){
						WHERE("ti.activity_type_no=#{info.activityTypeNo}");
					}
					if(StringUtils.isNotBlank(info.getOpenStatus())){
						WHERE("ti.open_status=#{info.openStatus}");
					}
					if(StringUtils.isNotBlank(info.getStatus())){
						WHERE("tac.status=#{info.status}");
					}
					if(StringUtils.isNotBlank(info.getCheckStatus())){
						if("1".equals(info.getCheckStatus())){// 未达标
							WHERE("tac.due_days <= 0");
						}
						if("2".equals(info.getCheckStatus())){//考核中
							WHERE("tac.due_days > 0 and tac.status=0");
						}
						if("3".equals(info.getCheckStatus())){//已达标
							WHERE("tac.due_days > 0 and tac.status=1");
						}
					}
					if(info.getCheckTimeBegin()!=null){
						WHERE("tac.check_time>=#{info.checkTimeBegin}");
					}
					if(info.getCheckTimeEnd()!=null){
						WHERE("tac.check_time<=#{info.checkTimeEnd}");
					}
					if(StringUtils.isNotBlank(info.getOrder())){
						if("0".equals(info.getOrder())){
							ORDER_BY("tac.due_days desc");
						}
						if("1".equals(info.getOrder())) {
							ORDER_BY("tac.due_days asc");
						}
					}else {
						ORDER_BY("tac.check_time asc");
					}
				}
			};
			return sql.toString();
		}
		
		public String selectByAddParam(Map<String,Object> param){
			final TerminalInfo terminalInfo=(TerminalInfo)param.get("terminalInfo");
			return new SQL(){{
				StringBuilder sql=new StringBuilder("");
				if(StringUtils.isNotBlank(terminalInfo.getPsamNo())){
					if(sql.length()!=0){
						sql.append(" or ");
					}
					sql.append("PSAM_NO=#{terminalInfo.psamNo}");
				}
				if(StringUtils.isNotBlank(terminalInfo.getSn())){
					if(sql.length()!=0){
						sql.append(" or ");
					}
					sql.append("sn=#{terminalInfo.sn}");
				}
				
				SELECT(" id,sn,PSAM_NO");
				FROM("terminal_info ");
				if(sql.length()!=0){
					WHERE(sql.toString());
				}
				
			}}.toString();
		}
		
		public String insertBatch(Map<String, Object> param){
			final List<TerminalInfo> list = (List<TerminalInfo>) param.get("list");
			//TODO 如果是移公社的机具，要取移公社的序列
			String seq = "";
			if(list!=null&&list.size()>0){
				if("123".equals(list.get(0).getType())){
					seq = Constants.TERMINAL_NO_YGS_SEQ;
				} else {
					seq = Constants.TERMINAL_NO_SEQ;
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append("insert into terminal_info(sn,PSAM_NO,open_status,type,CREATE_TIME,cashier_no,pos_type,collection_code) values ");
			MessageFormat message = new MessageFormat("(nextval(''" + seq + "''),#'{'list[{0}].psamNo},#'{'list[{0}].openStatus},"
					+ "#'{'list[{0}].type},now(),#'{'list[{0}].cashierNo},#'{'list[{0}].posType},#'{'list[{0}].collectionCode}),");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		
		public String updateOpenStatusBatch(Map<String, Object> params){
			final List<String> list = (List<String>) params.get("list");
			StringBuilder sb = new StringBuilder("update terminal_info set agent_no=null , agent_node=null , open_status=#{openStatus} where id in(");
			MessageFormat message = new MessageFormat("#'{'list[{0}]},");
			for(int i=0; i< list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			sb.append(") and open_status='1'");
			return sb.toString();
		}
//		@Select("select merchant_no from terminal_info where FIND_IN_SET(#{activityType},activity_type) and "
//	    		+ " id=#{id}")
		public String selectByIdAndActivity(Map<String, Object> param){
			final String[] activityTypeList = (String[]) param.get("activityTypeList");
			String sql = new SQL(){{
				SELECT("merchant_no,activity_type");
				FROM("terminal_info");
				if(activityTypeList!=null && activityTypeList.length>0){
					StringBuilder sb = new StringBuilder();
					MessageFormat message = new MessageFormat(" FIND_IN_SET(#'{'activityTypeList[{0}]},activity_type)");
					for(int i=0; i<activityTypeList.length; i++){
						sb.append(message.format(new Integer[]{i}));
						sb.append(" or ");
					}
					sb.replace(sb.length()-3, sb.length(), "");
					WHERE("(" + sb.toString() + ")");
				}
				WHERE("id=#{id}");
			}}.toString();
			System.out.println(sql);
			return sql;
		}

		public String querySNList(final Map<String, Object> param) {
			final TerInfo info = (TerInfo) param.get("info");
			return new SQL(){{
				SELECT("ti.id,ti.sn,ti.type,ti.activity_type");
				FROM("terminal_info ti");
				WHERE("ti.open_status = 0 ");
				if(StringUtils.isNotBlank(info.getSnStart())&&StringUtils.isBlank(info.getSnEnd())){
					WHERE("ti.sn like concat(#{info.snStart},'%')");
				} else if(StringUtils.isBlank(info.getSnStart())&&StringUtils.isNotBlank(info.getSnEnd())){
					WHERE("ti.sn like concat('%',#{info.snEnd},'%')");
				} else if(StringUtils.isNotBlank(info.getSnStart())&&StringUtils.isNotBlank(info.getSnEnd())){
					WHERE("ti.sn >= #{info.snStart} and ti.sn <= #{info.snEnd}");
				}
				if(StringUtils.isNotBlank(info.getType()) &&!StringUtils.equals("-1", info.getType())){
					WHERE("ti.type = #{info.type}");
				}
			}}.toString();
		}

		public String selectCjtTerPage(Map<String, Object> param) {
			TerminalInfo baseInfo = (TerminalInfo) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("ti.sn, ti.type, ti.activity_type, hp.type_name");
			sql.FROM("terminal_info ti");
			sql.LEFT_OUTER_JOIN("hardware_product hp on hp.hp_id = ti.type");
			if(StringUtils.isNotEmpty(baseInfo.getSnStart())) {
				sql.WHERE("ti.sn >= #{baseInfo.snStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getSnEnd())) {
				sql.WHERE("ti.sn <= #{baseInfo.snEnd}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getType())) {
				sql.WHERE("FIND_IN_SET(ti.type,#{baseInfo.type})");
			}
			sql.WHERE("ti.open_status = '0'");
			sql.WHERE("not exists(select 1 from cjt_order_sn cos where cos.sn = ti.sn)");
			sql.ORDER_BY("ti.sn");
			return sql.toString();
		}
	}

    @Select("SELECT " + 
    //
    		"	count( * )  " + 
    		//
    		"FROM " + 
    		//
    		"	acq_terminal_store  " + 
    		//
    		"WHERE " + 
    		//
    		"	sn = #{sn}  " + 
    		//
    		"	AND (ter_no IS NULL or ter_no='') ")
	long countTerNoBySn(String sn);

    @Select("SELECT " + 
    //
    		"	count( * )  " + 
    		//
    		"FROM " + 
    		//
    		"	acq_terminal_store  " + 
    		//
    		"WHERE " + 
    		//
    		"	sn = #{sn}  ")
	long countBySn(String sn);

}