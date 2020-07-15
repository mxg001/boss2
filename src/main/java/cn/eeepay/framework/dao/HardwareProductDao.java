package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.HlfHardware;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface HardwareProductDao {

	@Insert("insert into hardware_product (hp_id, type_name," + "model, version_nu, sale_starttime,"
			+ "sale_endtime, prod_starttime, prod_endtime" + "use_starttime, use_endtime, repa_starttime,"
			+ "repa_endtime, oem_mark, oem_id," + "manufacturer)"
			+ "values (#{hpId,jdbcType=VARCHAR}, #{typeName,jdbcType=VARCHAR},"
			+ "#{model,jdbcType=VARCHAR}, #{versionNu,jdbcType=VARCHAR}, #{saleStarttime,jdbcType=DATE},"
			+ "#{saleEndtime,jdbcType=DATE}, #{prodStarttime,jdbcType=DATE}, #{prodEndtime,jdbcType=DATE},"
			+ "#{useStarttime,jdbcType=DATE}, #{useEndtime,jdbcType=DATE}, #{repaStarttime,jdbcType=DATE}, "
			+ "#{repaEndtime,jdbcType=DATE}, #{oemMark,jdbcType=VARCHAR}, #{oemId,jdbcType=VARCHAR}, "
			+ "#{manufacturer,jdbcType=VARCHAR})")
	int insert(HardwareProduct record);

	@Select("select hp.*,ti.team_name as orgName from hardware_product hp LEFT JOIN team_info ti ON hp.org_id=ti.team_id order by CONVERT(type_name USING GBK) asc")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> selectAllInfo();

	@Select("select hp.*,ti.team_name as orgName " +
			"from hardware_product hp " +
			"LEFT JOIN team_info ti ON hp.org_id=ti.team_id " +
			"where hp_id like concat('%',#{item},'%') or type_name like concat('%',#{item},'%')")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> selectAllInfo2(@Param("item") String item);

	@Select(
			"select * from hardware_product where hp_id=#{bpId}"
	)
	HardwareProduct getHardwareProductByBpId(@Param("bpId")Long bpId);

	/**
	 *通过组合中文名查询硬件产品类型
	 */
	@Select(
			"select * from hardware_product where CONCAT(type_name,version_nu)=#{bpName}  LIMIT 1"
	)
	HardwareProduct getHardwareProductByBpName(@Param("bpName")String bpName);

	/**
	 * 根据ID，查询出类型、型号 by tans
	 * 
	 * @param hardWareId
	 * @return
	 */
	@Select("SELECT hp_id,type_name,version_nu,pos_type,facturer_code,model,secret_type FROM hardware_product WHERE hp_id=#{id}")
	@ResultType(HardwareProduct.class)
	HardwareProduct findHardwareName(@Param("id") String hardWareId);

	@Select("SELECT hp_id,type_name,version_nu,pos_type FROM hardware_product")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> findAllHardwareName();

	/**
	 * 硬件产品新增 by sober
	 * 
	 * @param hardwareProduct
	 * @return
	 */
	@Insert("INSERT INTO hardware_product (type_name,version_nu,facturer_code,manufacturer,model,org_id,secret_type,create_person,create_time,device_pn,team_entry_id ) "
			+ "VALUES (#{hardwareProduct.typeName},#{hardwareProduct.versionNu},#{hardwareProduct.facturerCode}," +
			"#{hardwareProduct.manufacturer},#{hardwareProduct.model},#{hardwareProduct.orgId},#{hardwareProduct.secretType}," +
			"#{hardwareProduct.createPerson},#{hardwareProduct.createTime},#{hardwareProduct.devicePn},#{hardwareProduct.teamEntryId})")
	int addHard(@Param("hardwareProduct") HardwareProduct hardwareProduct);

	
	
	@Insert("UPDATE hardware_product set type_name = #{hardwareProduct.typeName}, version_nu=#{hardwareProduct.versionNu}," +
			"facturer_code=#{hardwareProduct.facturerCode},"
			+ "manufacturer=#{hardwareProduct.manufacturer},model=#{hardwareProduct.model}," +
			"org_id=#{hardwareProduct.orgId},team_entry_id=#{hardwareProduct.teamEntryId}," +
			" secret_type = #{hardwareProduct.secretType},device_pn = #{hardwareProduct.devicePn} where hp_id = #{hardwareProduct.hpId}")
	int updateHard(@Param("hardwareProduct") HardwareProduct hardwareProduct);
	/**
	 * by sober 条件查询所添加的数据是否已经存在
	 * 
	 * @param typeName
	 * @param versionNu
	 * @return
	 */
	@Select("select hp_id, type_name, model," + "version_nu, sale_starttime,sale_endtime, prod_starttime, prod_endtime,"
			+ "use_starttime, use_endtime, repa_starttime,repa_endtime, oem_mark, oem_id,manufacturer,"
			+ "pos_type from hardware_product where type_name = #{typeName} and version_nu = #{versionNu} ")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> selectByParams(@Param("typeName") String typeName, @Param("versionNu") String versionNu);
	
	@Select("select hp_id, type_name, model," + "version_nu, sale_starttime,sale_endtime, prod_starttime, prod_endtime,"
			+ "use_starttime, use_endtime, repa_starttime,repa_endtime, oem_mark, oem_id,manufacturer,"
			+ "pos_type from hardware_product where type_name = #{typeName} and version_nu = #{versionNu} AND hp_id!=#{hpId}")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> selectByParamsUpdate(@Param("typeName") String typeName, @Param("versionNu") String versionNu,@Param("hpId") Long hpId);
	
	
	@Select("SELECT hp.hp_id ,hp.pos_type FROM hardware_product hp,business_product_hardware bph WHERE hp.hp_id = bph.hp_id AND bp_id = #{bpId}")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> findHardWareBybpId(@Param("bpId") String bpId);

	@SelectProvider(type = SqlProvider.class, method = "selectByCondition")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> selectByCondition(Page<HardwareProduct> page,@Param("hp") HardwareProduct hp);

	@Select("select hp.* from hardware_product hp " +
			" left join terminal_info ti on ti.type = hp.hp_id" +
			" where ti.sn = #{sn}")
	@ResultType(HardwareProduct.class)
    HardwareProduct selectBySn(@Param("sn") String sn);

	@SelectProvider(type = SqlProvider.class, method = "selectAllHardwareProduct")
	@ResultType(HardwareProduct.class)
    List<HardwareProduct> selectAllHardwareProduct(@Param("pns") String pns);

	@Select("select hp.*,ti.team_name as orgName from hardware_product hp LEFT JOIN team_info ti ON hp.org_id=ti.team_id where hp.org_id=#{teamId} order by CONVERT(type_name USING GBK) asc")
	@ResultType(HardwareProduct.class)
	List<HardwareProduct> selectAllInfoByTeamId(@Param("teamId") String teamId);


	@Select(
			"select harType.activity_type_no,harType.sub_type from terminal_info ter " +
					" INNER JOIN activity_hardware har ON (har.hard_id=ter.type and activity_code='009') " +
					" INNER JOIN activity_hardware_type harType ON harType.activity_type_no=har.activity_type_no" +
					" where ter.sn=#{sn}"
	)
	List<HlfHardware> isHlfHardware(@Param("sn")String sn);

	public class SqlProvider {
	public String selectByCondition(Map<String, Object> param) {
		final HardwareProduct hp = (HardwareProduct) param.get("hp");
		String sql = new SQL() {
			{
				SELECT("hp.*,ti.team_name as orgName");
				FROM("hardware_product hp LEFT JOIN team_info ti ON hp.org_id=ti.team_id");
				String typeName = hp.getTypeName();
				if (StringUtils.isNotBlank(typeName)) {
					hp.setTypeName(typeName+"%");
					WHERE("hp.type_name like #{hp.typeName}");
				}
				if (hp.getOrgId()!=null && hp.getOrgId()!=-1) {
					WHERE("hp.org_id = #{hp.orgId}");
				}
				if (hp.getSecretType()!=null) {
					WHERE("hp.secret_type = #{hp.secretType}");
				}
				if(hp.getCreateTimeBegin()!=null){
					WHERE("hp.create_time >=#{hp.createTimeBegin}");
				}
				if(hp.getCreateTimeEnd()!=null){
					WHERE("hp.create_time <=#{hp.createTimeEnd}");
				}
				if(hp.getCreatePerson()!=null){
					WHERE("hp.create_person like CONCAT('%',#{hp.createPerson},'%')");
				}


				if (StringUtils.isNotBlank(hp.getDevicePn())) {
					WHERE("hp.device_pn = #{hp.devicePn}");
				}
				if(!"".equals(hp.getDevicePn())){
					if(hp.getDevicePn()==null){
						WHERE("hp.device_pn is null ");
					}else{
						WHERE("hp.device_pn = #{hp.devicePn}");
					}

				}
//				if (StringUtils.isNotBlank(hp.getDevicePn())) {
//					WHERE("hp.device_pn = #{hp.devicePn}");
//				}

				ORDER_BY("hp.hp_id desc ");
			}
		}.toString();
		return sql;
	}

		public String selectAllHardwareProduct(Map<String, Object> param){
		    final String pns = (String)param.get("pns");
			SQL sql = new SQL(){
				{
					SELECT("hp_id,type_name,device_pn,version_nu");
					FROM("hardware_product");
					WHERE("device_pn IN " + pns);
				}
			};
			return sql.toString();
		}
	}
	
	@Select("SELECT * FROM hardware_product where hp_id=#{id}")
	@ResultType(HardwareProduct.class)
	HardwareProduct queryHardById(@Param("id")String id);

}