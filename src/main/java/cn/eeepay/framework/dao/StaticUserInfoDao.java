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
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.StaticUserInfo;

public interface StaticUserInfoDao {

	@Insert("Insert into static_user_info(mobilephone,user_name,remark,department) "
			+ "values(#{staticUserInfo.mobilephone},#{staticUserInfo.userName},"
			+ "#{staticUserInfo.remark},#{staticUserInfo.department})")
	int addInfo(@Param("staticUserInfo")StaticUserInfo staticUserInfo);
	
	@Delete("delete from static_user_info where static_user_id=#{id}")
	int deleteInfo(@Param("id")String id);
	
	@Select("select * from static_user_info where mobilephone=#{staticUserInfo.mobilephone}")
	@ResultType(StaticUserInfo.class)
	List<StaticUserInfo> findInfo(@Param("staticUserInfo")StaticUserInfo staticUserInfo);
	
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(StaticUserInfo.class)
	List<StaticUserInfo> selectAllInfo(@Param("page")Page<StaticUserInfo> page,@Param("staticUserInfo")StaticUserInfo staticUserInfo);
	
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(StaticUserInfo.class)
	List<StaticUserInfo> selectAllInfoImprot(@Param("staticUserInfo")StaticUserInfo staticUserInfo);
	
	 public class SqlProvider{
			public String selectAllInfo(Map<String,Object> param){
				final StaticUserInfo staticUserInfo=(StaticUserInfo)param.get("staticUserInfo");
				String sql = new SQL(){{
					String str="";
					if(staticUserInfo.getsTime()!=null&&staticUserInfo.geteTime()!=null){
						str+= " and ct.trans_time BETWEEN #{staticUserInfo.sTime} AND #{staticUserInfo.eTime}";
					}
					SELECT("su.*,a.*");
					FROM("(SELECT u.mobilephone as ssd, ct.merchant_no,sum(ct.trans_amount) as total_amt,"
							+ "count(ct.trans_amount) as total_cnt,"
							+ "sum(case ct.trans_status when 'SUCCESS' then ct.trans_amount else 0 end) as net_amt,"
							+ "sum(case ct.trans_status when 'SUCCESS' then 1 else 0 end) as net_cnt "
							+ "from static_user_info u,collective_trans_order ct "
							+ "WHERE u.mobilephone = ct.mobile_no "+str
							+ " GROUP BY u.mobilephone,ct.merchant_no) as a right join static_user_info su on a.ssd = su.mobilephone ");
					if(StringUtils.isNotBlank(staticUserInfo.getUserName())){
						WHERE("su.user_name=#{staticUserInfo.userName}");
					}
					if(StringUtils.isNotBlank(staticUserInfo.getMobilephone())){
						WHERE("su.mobilephone=#{staticUserInfo.mobilephone}");
					}
				}}.toString();
				System.out.println(sql);
				return sql;
			}
	 }
	
}
