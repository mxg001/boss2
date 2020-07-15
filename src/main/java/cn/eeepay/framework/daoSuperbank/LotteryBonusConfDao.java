package cn.eeepay.framework.daoSuperbank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LotteryBonusConf;


public interface LotteryBonusConfDao {

	/**查询彩票奖励配置*/
	@SelectProvider(type = SqlProvider.class, method = "selectLotteryConfByPage")
	@ResultType(LotteryBonusConf.class)
	List<LotteryBonusConf> getLotteryBonusConf(@Param("lotteryConf") LotteryBonusConf lotteryConf,
			@Param("pager") Page<LotteryBonusConf> pager);
	
	 public class SqlProvider{
	     public String selectLotteryConfByPage(Map<String, Object> param){
	           StringBuffer sql = new StringBuffer("");
	           final LotteryBonusConf lotteryParam = (LotteryBonusConf) param.get("lotteryConf");
	           sql.append(("select lf.id,lf.org_id,oi.org_name,bonus_type,lf.bonus_request,lf.update_by,lf.update_date,lf.lottery_org_total_bonus,lf.company_bonus,lf.org_bonus,lf.profit_type"));
	           sql.append((" from lottery_bonus_conf lf left join org_info oi "));
	           sql.append((" on lf.org_id=oi.org_id "));
	          
	           if(lotteryParam != null && lotteryParam.getOrgId() != null && lotteryParam.getOrgId() != -1L){//组织id
		              sql.append(" where lf.org_id=#{lotteryConf.orgId}");
		       }
	            
	           sql.append(" order by lf.id desc ");
	           
	           System.out.println(sql.toString());
	           
	           return sql.toString();
	     }
    }

	 /**修改彩票奖励配置*/
	@Update("update lottery_bonus_conf set lottery_org_total_bonus=#{lotteryOrgTotalBonus},company_bonus=#{companyBonus},org_bonus=#{orgBonus},update_by=#{updateBy},update_date=sysdate() where id=#{id}")
	int updLotteryConf(LotteryBonusConf lotteryConf);
	
	
	/**新增彩票奖金配置*/
	@Insert("insert into lottery_bonus_conf " +
			"(org_id,bonus_type,bonus_request,lottery_org_total_bonus,company_bonus,org_bonus,update_by,update_date)" +
			" values" +
			" (#{orgId},#{bonusType},#{bonusRequest},#{lotteryOrgTotalBonus},#{companyBonus},#{orgBonus},#{updateBy},sysdate())")
	int saveLotteryConf(LotteryBonusConf lotteryConf);
	
	/**查询组织orgId的配置是否存在*/
	@Select("select count(1) from lottery_bonus_conf where org_id = #{orgId} ")
    @ResultType(Integer.class)
    int checkExists(@Param("orgId") Long orgId);
	
}
