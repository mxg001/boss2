package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.JoinTable;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface BusinessProductDefineDao {

	@Insert("INSERT INTO business_product_define(bp_id,bp_name,agent_show_name,sale_starttime,"
			+ "sale_endtime,proxy,bp_type,is_oem,team_id,own_bp_id,two_code,remark"
			+ ",bp_img,not_check,link,rely_hardware,link_product,allow_web_item,allow_individual_apply,effective_status,create_person,create_time) values "
			+ "(#{product.bpId},#{product.bpName},#{product.agentShowName},#{product.saleStarttime},"
			+ "#{product.saleEndtime},#{product.proxy},#{product.bpType},#{product.isOem},"
			+ "#{product.teamId},#{product.ownBpId},#{product.twoCode},#{product.remark},"
			+ "#{product.bpImg},#{product.notCheck},#{product.link},#{product.relyHardware},#{product.linkProduct},"
			+ "#{product.allowWebItem},#{product.allowIndividualApply},#{product.effectiveStatus},#{product.createPerson},#{product.createTime})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "product.bpId", before = false, resultType = Long.class)
	int insert(@Param("product") BusinessProductDefine product);

	@Select("select bpd.bp_id,bpd.team_id,bpd.bp_name,bpd.bp_type,bpg.group_no,bpd.allow_individual_apply"
			+ " from business_product_define bpd"
			+ " left join business_product_group bpg on bpg.bp_id=bpd.bp_id" +
            " where bpd.effective_status='1'"
			+ " order by case when bpg.group_no>'' then bpg.group_no else '999999' end, bpd.allow_individual_apply desc,convert(bpd.bp_name using gbk) asc")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectAllInfo();


	@Select("select bpd.bp_id,bpd.team_id,bpd.bp_name,bpd.bp_type,bpg.group_no,bpd.allow_individual_apply"
			+ " from business_product_define bpd"
			+ " left join business_product_group bpg on bpg.bp_id=bpd.bp_id" +
			" where bpd.effective_status='1' and bpd.team_id=#{teamId}"
			+ " order by case when bpg.group_no>'' then bpg.group_no else '999999' end, bpd.allow_individual_apply desc,convert(bpd.bp_name using gbk) asc")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectAllInfoByTeamId(@Param("teamId") String teamId);

	@Select("select bpd.bp_id,bpd.bp_name,bpd.bp_type,bpg.group_no,bpd.allow_individual_apply"
			+ " from business_product_define bpd"
			+ " left join business_product_group bpg on bpg.bp_id=bpd.bp_id " +
			" where (bpg.group_no in (SELECT group_no from business_product_group where bp_id=#{bpId}) or bpd.bp_id=#{bpId}) and bpd.effective_status='1'"
			+ " order by case when bpg.group_no>'' then bpg.group_no else '999999' end, bpd.allow_individual_apply desc,convert(bpd.bp_name using gbk)")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectAllInfoByBpId(Long bpId);

	@Select("select bpd.bp_id,bpd.bp_name,bpd.bp_type,bpg.group_no,bpd.allow_individual_apply"
			+ " from business_product_define bpd"
			+ " left join business_product_group bpg on bpg.bp_id=bpd.bp_id " +
			" where (bpd.bp_id=#{bpId} or bpd.bp_name like concat('%',#{bpId},'%')) and bpd.effective_status='1'"
			+ " order by case when bpg.group_no>'' then bpg.group_no else '999999' end, bpd.allow_individual_apply desc,convert(bpd.bp_name using gbk)")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectAllInfoByName(String bpId);

	@Select("select bpg.group_no,b1.*,b2.bp_name ownBpName,b3.bp_name linkProductName,t.team_name teamName from business_product_define b1 left join business_product_define "
			+ " b2 on b1.own_bp_id=b2.bp_id left join business_product_define b3 on b3.bp_id=b1.link_product left join team_info t on b1.team_id=t.team_id " +
			" left join business_product_group bpg on bpg.bp_id=b1.bp_id" +
			" where b1.bp_id=#{id}")
	@ResultType(BusinessProductDefine.class)
	BusinessProductDefine selectDetailById(@Param("id") String id);

	@SelectProvider(type = SqlProvider.class, method = "selectByParam")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectByParam(Page<BusinessProductDefine> page, @Param("bpd") BusinessProductDefine bpd);

	@Select("SELECT bp_id,bp_name FROM business_product_define WHERE team_id='100010' and rely_hardware=0 and effective_status='1' and bp_id <> #{id}")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> queryOtherProduct(@Param("id") String id);

	@Select("SELECT bp_id,bp_name FROM business_product_define WHERE team_id='200010' and rely_hardware=0 and effective_status='1' and bp_id <> #{id}")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> queryOtherOemProduct(String bpId);

	@Select("select * from business_product_define where bp_id=#{bpId}")
	@ResultType(BusinessProductDefine.class)
	BusinessProductDefine selectBybpId(@Param("bpId") String bpId);

	@Select("  SELECT bpd.* FROM business_product_define  bpd ,agent_business_product abp WHERE bpd.bp_id = abp.bp_id  AND  bpd.bp_id=#{bpId} AND abp.agent_no=#{agentNo} AND abp.status = '1' ")
	@ResultType(BusinessProductDefine.class)
	BusinessProductDefine selectBybpIdAndAgentNo(@Param("bpId") String bpId, @Param("agentNo") String agentNo);

	@Update("update business_product_define set bp_name=#{product.bpName},agent_show_name=#{product.agentShowName},"
			+ "sale_starttime=#{product.saleStarttime},sale_endtime=#{product.saleEndtime},"
			+ "proxy=#{product.proxy},bp_type=#{product.bpType},is_oem=#{product.isOem}"
			+ ",team_id=#{product.teamId},own_bp_id=#{product.ownBpId},"
			+ "two_code=#{product.twoCode},remark=#{product.remark},bp_img=#{product.bpImg},"
			+ "not_check=#{product.notCheck},link=#{product.link},rely_hardware=#{product.relyHardware},"
			+ "link_product=#{product.linkProduct},allow_web_item=#{product.allowWebItem},"
			+ "allow_individual_apply=#{product.allowIndividualApply} where bp_id=#{product.bpId}")
	int update(@Param("product") BusinessProductDefine product);

	@Select("select b.bp_id,b.bp_name,b.remark,t.team_name teamName from business_product_define b left join "
			+ "team_info t on b.team_id=t.team_id")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectBpTeam();

	@Select("select b.bp_id,b.bp_name,t.team_name teamName from business_product_define b left join "
			+ "team_info t on b.team_id=t.team_id where b.bp_Id=#{id}")
	@ResultType(BusinessProductDefine.class)
	BusinessProductDefine selectById(String id);

	/**
	 * 多条件查询业务产品列表
	 *
	 * @param map
	 * @return
	 */
	@SelectProvider(type = SqlProvider.class, method = "getProducesByCondition")
	@ResultType(JoinTable.class)
	List<JoinTable> getProducesByCondition(@Param("param") Map<String, Object> map);

	/**
	 * 查询代理商的业务产品
	 */
	@Select("SELECT abp.id id,bpd.bp_id key1,abp.status key2,bpd.bp_name key3,bpd.bp_type key5 FROM business_product_define bpd"
			+ " LEFT JOIN agent_business_product abp ON abp.bp_id=bpd.bp_id WHERE bpd.effective_status = '1' and abp.agent_no=#{agentNo}")
	@ResultType(JoinTable.class)
	List<JoinTable> getAgentProducts(@Param("agentNo") String agentNo);

	/**
	 * 根据代理商的ID，查询相关的业务产品
	 *
	 * @param id
	 * @return
	 */
	@Select("SELECT bpd.bp_id, bpd.bp_name,bpd.remark,team.team_name FROM business_product_define bpd"
			+ " LEFT JOIN agent_business_product abp ON abp.bp_id=bpd.bp_id LEFT JOIN team_info team"
			+ " ON bpd.team_id=team.team_id WHERE abp.agent_no=#{agentNo}")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> getProductsByAgent(String id);

	/**
	 * 根据服务类型，查询包含的业务产品
	 *
	 * @param serviceTypes
	 * @return
	 * @author tans
	 * @date 2017年3月15日 上午10:09:43
	 */
	@SelectProvider(type = SqlProvider.class, method = "getProductByServiceType")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> getProductByServiceType(@Param("serviceTypes") String[] serviceTypes);

	/**
	 * 查询业务产品
	 *
	 * @author mays
	 * @date 2017年12月19日
	 */
	@Select("SELECT DISTINCT bpd.bp_name,bpd.bp_id FROM business_product_define bpd,business_product_info bpi,service_info si "
			+ "WHERE si.service_status = 1 AND si.service_id = bpi.service_id AND bpi.bp_id = bpd.bp_id order by convert(bpd.bp_name using gbk)")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> getProduct();

	/**
	 * 根据组织，查询关联的业务产品以及群组号
	 *
	 * @param teamIdList
	 * @return
	 * @author tans
	 * @date 2017年3月15日 上午10:09:43
	 */
//	@SelectProvider(type=SqlProvider.class, method="getProductByTeam")
	@Select("<script>" +
			"select bpd.bp_id,bpd.bp_name,bpd.bp_type,bpg.group_no " +
			" from business_product_define bpd " +
			" left join business_product_group bpg on bpd.bp_id=bpg.bp_id " +
			" where bpd.effective_status = 1" +
			" <if test=\"teamIdList!=null and teamIdList.size > 0\">" +
			" and bpd.team_id in " +
			"<foreach  collection=\"teamIdList\" item=\"teamId\" index=\"index\" open=\"(\" close=\")\" separator=\",\" > " +
			" #{teamId} " +
			"</foreach >" +
			" </if>" +
			" order by convert(bpd.bp_name using gbk)" +
			"</script>")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> getProductByTeam(@Param("teamIdList") List<String> teamIdList);

	/**
	 * 查询组织内的其他业务产品
	 *
	 * @param teamId
	 * @param bpId
	 * @return
	 * @author tans
	 * @date 2017年8月24日 上午10:56:25
	 */
	@Select("SELECT bp_id,bp_name FROM business_product_define WHERE team_id=#{teamId} and rely_hardware=0 and bp_id <> #{bpId}" +
			" and effective_status= '1'")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> getTeamOtherBp(@Param("teamId") String teamId, @Param("bpId") String bpId);

	@Select("select si.service_id,si.service_name from business_product_info bpi" +
			" INNER JOIN service_info si on si.service_id = bpi.service_id" +
			" where bpi.bp_id = #{bpId} and si.effective_status = '1'")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> selectEffectiveServiceByBpId(@Param("bpId") Long bpId);

	@Select("select bpd.bp_id,bpd.bp_name from business_product_define bpd\n" +
			"INNER JOIN business_product_group bpg1 on bpg1.bp_id = bpd.bp_id\n" +
			"INNER JOIN business_product_group bpg2 on bpg2.group_no = bpg1.group_no\n" +
			"where bpg2.bp_id = #{bpId} and bpg1.bp_id <> #{bpId} and bpd.effective_status = '1'\n" +
			"and bpd.allow_individual_apply = '0'")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectEffectiveMemberProductByBpId(@Param("bpId") Long bpId);

	@Update("update business_product_define set effective_status = #{effectiveStatus} where bp_id = #{bpId}")
	int updateEffectiveStatus(BusinessProductDefine baseInfo);

	/**
	 * 查询业务产品对应的teamId
	 *
	 * @param ids
	 * @return
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectTeamIdsWithBpIds")
	@ResultType(String.class)
    List<String> selectTeamIdsWithBpIds(@Param("ids") List<String> ids);

	@SelectProvider(type = SqlProvider.class, method = "selectByParam")
	@ResultType(BusinessProductDefine.class)
    List<BusinessProductDefine> getExportQueryProduct(@Param("bpd") BusinessProductDefine bpd);

	@Select("SELECT bp_name FROM business_product_define WHERE bp_id=#{bpId}")
	String selectBpNameByBpId(@Param("bpId") String bpId);

	@Select("select * from business_product_define bpd left join agent_business_product abp on bpd.bp_id = abp.bp_id where abp.agent_no = #{agentNo} and bpd.team_id = #{teamId}")
	List<BusinessProductDefine> selectProdcuteByTeamIdAndAgentNo(@Param("agentNo") String agentNo, @Param("teamId") String teamId);

	public class SqlProvider {
		public String getProducesByCondition(Map<String, Object> map) {
			@SuppressWarnings("unchecked") final Map<String, Object> params = (Map<String, Object>) map.get("param");
			return new SQL() {{
				SELECT("bpd.bp_id key1,bpd.allow_individual_apply key7, bpd.bp_name key3,bpd.bp_type key5,bpg.group_no key6");
				FROM("business_product_define bpd");
				WHERE("bpd.effective_status = '1' ");
				if (params.get("agentNo") != null) {
					LEFT_OUTER_JOIN("agent_business_product abp ON abp.bp_id = bpd.bp_id "
							+ " AND abp.agent_no = #{param.agentNo}");
					ORDER_BY("abp.agent_no desc");
				}
				LEFT_OUTER_JOIN("team_info ti ON bpd.team_id=ti.team_id");
				LEFT_OUTER_JOIN("business_product_group bpg on bpg.bp_id=bpd.bp_id");
				if (StringUtil.isNotBlank(params.get("bpId"))) {
					WHERE("(bpg.group_no in (SELECT group_no from business_product_group where bp_id=#{param.bpId}) or bpd.bp_id=#{param.bpId})");
				}
				if (params.get("teamId") != null) {
					WHERE("bpd.team_id=#{param.teamId} ");
				}
				ORDER_BY("case when bpg.group_no>'' then bpg.group_no else '999999' end,"
						+ " bpd.allow_individual_apply desc");
			}}.toString();
		}


		public String selectTeamIdsWithBpIds(Map<String, Object> map) {
			final List<String> ids = (List<String>) map.get("ids");
			String sql = new SQL() {
				{
					SELECT("DISTINCT team_id");
					FROM("business_product_define");
					StringBuilder condition = new StringBuilder("bp_id IN (");
					for (int i = 0; i < ids.size(); i++) {
						condition.append("'" + ids.get(i) + "'");
						if (i != ids.size() - 1) {
							condition.append(",");
						}
					}
					condition.append(")");
					WHERE(condition.toString());
				}
			}.toString();
			return sql;
		}
//		public String getProductByTeam(Map<String, Object> param){
//			final String teamId = (String) param.get("teamId");
//			return new SQL(){{
//				SELECT("bpd.bp_id,bpd.bp_name,bpd.bp_type,bpg.group_no");
//				FROM("business_product_define bpd");
//				LEFT_OUTER_JOIN("business_product_group bpg on bpd.bp_id=bpg.bp_id");
//				if (StringUtils.isNotBlank(teamId)) {
//					String[] teamIdList = teamId.split(",");
//					WHERE("bpd.team_id=#{teamId} ");
//				}
//				WHERE("bpd.effective_status = 1");
//			}}.toString();
//		}

		public String selectByParam(Map<String, Object> param) {
			final BusinessProductDefine bpd = (BusinessProductDefine) param.get("bpd");
			String sql = new SQL() {
				{
					SELECT_DISTINCT("bpd3.bp_name linkProductName,bpd.rely_hardware,bpd.not_check,bpd.allow_web_item,bpd.link_product,bpd.proxy,bpd.sale_starttime,bpd.sale_endtime,bpd2.bp_name own_bp_name,bpd.allow_individual_apply,bpg.group_no,bpd.bp_id, bpd.bp_name,bpd.agent_show_name, bpd.bp_type, bpd.is_oem,bpd.effective_status,ti.team_name,bpd.create_person,bpd.create_time");
					FROM("business_product_define bpd LEFT JOIN team_info ti ON bpd.team_id=ti.team_id");
					LEFT_OUTER_JOIN("business_product_hardware bph ON bph.bp_id=bpd.bp_id");
					LEFT_OUTER_JOIN("business_product_group bpg on bpg.bp_id = bpd.bp_id");
					LEFT_OUTER_JOIN("business_product_define bpd2 on bpd2.bp_id = bpd.own_bp_id");
                    LEFT_OUTER_JOIN("business_product_define bpd3 on bpd3.bp_id=bpd.link_product");
					if (StringUtils.isNotBlank(bpd.getGroupNo())) {
						WHERE("bpg.group_no=#{bpd.groupNo}");
					}
					if (StringUtils.isNotBlank(bpd.getIsOem())) {
						WHERE("bpd.is_oem=#{bpd.isOem}");
					}
					if (bpd.getAllowIndividualApply() != null) {
						WHERE("bpd.allow_individual_apply=#{bpd.allowIndividualApply}");
					}

					String bpName = bpd.getBpName();
					if (StringUtils.isNotBlank(bpName)) {
						bpd.setBpName("%" + bpName + "%");
						WHERE("bpd.bp_name like #{bpd.bpName}");
					}
					String agentShowName = bpd.getAgentShowName();
					if (StringUtils.isNotBlank(agentShowName)) {
						bpd.setAgentShowName("%" + agentShowName + "%");
						WHERE("bpd.agent_show_name like #{bpd.agentShowName}");
					}
					String bpType = bpd.getBpType();
					if (StringUtils.isNotBlank(bpType)) {
						WHERE("bpd.bp_type=#{bpd.bpType}");
					}
					String teamId = bpd.getTeamId();
					if (StringUtils.isNotBlank(teamId)) {
						WHERE("ti.team_id = #{bpd.teamId}");
					}
					if (bpd != null && StringUtils.isNotBlank(bpd.getHpId())) {
						WHERE("(bph.hp_id = #{bpd.hpId} or bph.hp_id='0')");
					}
					if (bpd != null && bpd.getEffectiveStatus() != null) {
						WHERE("bpd.effective_status=#{bpd.effectiveStatus}");
					}
					if (bpd != null && StringUtils.isNotEmpty(bpd.getCreatePerson())) {
						WHERE("bpd.create_person like CONCAT('%',#{bpd.createPerson},'%')");
					}
					if (bpd != null && bpd.getCreateTimeBegin() != null) {
						WHERE("bpd.create_time >=#{bpd.createTimeBegin}");
					}

					if (bpd != null && bpd.getCreateTimeEnd() != null) {
						WHERE("bpd.create_time <=#{bpd.createTimeEnd}");
					}

					ORDER_BY("bpd.bp_id");
				}
			}.toString();
			return sql;
		}

		public String getProductByServiceType(Map<String, Object> map) {
			final String[] serviceTypes = (String[]) map.get("serviceTypes");
			String sql = new SQL() {{
				SELECT("bpd.bp_name,bpd.bp_id");
				FROM("business_product_define bpd, business_product_info bpi, service_info si");
				if (serviceTypes != null && serviceTypes.length > 0) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < serviceTypes.length; i++) {
						sb.append(serviceTypes[i]).append(",");
					}
					sb.setLength(sb.length() - 1);
					WHERE("si.service_type IN (" + sb + ")");
				}
				WHERE(" si.service_status=1 AND si.service_id = bpi.service_id"
						+ " AND bpi.bp_id = bpd.bp_id");
			}}.toString();
			System.out.println("根据服务类型，查询包含的业务产品的sql:" + sql);
			return sql;
		}
	}


}