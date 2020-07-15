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
import cn.eeepay.framework.model.LoanBonusConf;
import cn.eeepay.framework.model.LoanSource;
import cn.eeepay.framework.util.StringUtil;


public interface LoanCompanyDao {

	/**查询机构列表*/
	@Select("select id,company_name from loan_source")
    @ResultType(LoanSource.class)
	List<LoanSource> getLoanCompanies();

	/**查询贷款奖励配置*/
	@SelectProvider(type = SqlProvider.class, method = "selectLoanConfByPage")
	@ResultType(LoanBonusConf.class)
	List<LoanBonusConf> getLoanBonusConf(@Param("loanConf") LoanBonusConf loanConf,
			@Param("pager") Page<LoanBonusConf> pager);

	/**修改奖励配置*/
	@Update("update loan_bonus_conf set org_cost_loan=#{orgCostLoan},org_cost_reg=#{orgCostReg}," +
            "org_push_loan=#{orgPushLoan},org_push_reg=#{orgPushReg},update_by=#{updateBy}," +
            "update_date=sysdate(),company_bonus=#{companyBonus},org_bonus=#{orgBonus}" +
            " where id=#{id}")
	int updLoanConf(LoanBonusConf loanConf);

	/**修改贷款机构表*/
	@Update("update loan_source set loan_bonus=#{loanBonus},register_bonus=#{registerBonus} where id=#{sourceId}")
	int updLoanCompany(LoanBonusConf loanCompany);

	/**新增贷款机构*/
	@Insert("insert into loan_source (id,company_name,status,loan_bonus,register_bonus) values(#{id},#{companyName},'on',#{loanBonus},#{registerBonus})")
	int saveLoanCompany(@Param("id") long id,@Param("companyName") String companyName,@Param("loanBonus") String loanBonus,@Param("registerBonus") String registerBonus);


	@Select("select  id,org_id,source_id,org_cost_loan,org_cost_reg,update_by,update_date,org_push_loan,org_push_reg FROM loan_bonus_conf where id=#{id}")
	@ResultType(LoanBonusConf.class)
	LoanBonusConf selectByPrimaryKey(long id);


	@Select("select @@identity")
	long getInsertId();

	/**新增奖金配置*/
	@Insert("insert into loan_bonus_conf " +
			"(org_id,source_id,org_cost_loan,org_cost_reg,org_push_loan,org_push_reg,update_by,update_date,company_bonus,org_bonus,profit_type)" +
			" values" +
			"(#{orgId},#{sourceId},#{orgCostLoan},#{orgCostReg},#{orgPushLoan},#{orgPushReg},#{updateBy},sysdate(),#{companyBonus},#{orgBonus},#{profitType})")
	int saveLoanConf(LoanBonusConf loanConf);

	@Select("select key_value from sys_id where key_id='loan_manage'")
	long getSysId();
	@Update("update sys_id set key_value=#{newId} where key_id='loan_manage' and key_value=#{oldId}")
	int updSysId(@Param("newId") long newId,@Param("oldId") long oldId);

	@Select("select count(id) from loan_bonus_conf where source_id=#{sourceId} and org_id=#{orgId}")
	int isExist(@Param("sourceId") long sourceId,@Param("orgId") Long orgId);

	@Select("select lbc.* from loan_bonus_conf lbc left join org_info oi on oi.org_id = lbc.org_id where oi.v2_orgcode = #{v2Code}")
	List<LoanBonusConf> getDirectSalesConf(@Param("v2Code") String v2Code);
	
	class SqlProvider{
	     public String selectLoanConfByPage(Map<String, Object> param){
	           StringBuffer sql = new StringBuffer("");
	           final LoanBonusConf loanParam = (LoanBonusConf) param.get("loanConf");
	           sql.append("select lf.*,")
               .append("ls.company_name,ls.register_bonus,ls.loan_bonus,ls.profit_type,ls.reward_requirements,ls.loan_alias,oi.is_open")
	           .append(" from loan_bonus_conf lf")
               .append(" left join loan_source ls on ls.id = lf.source_id")
               .append(" left join org_info oi on oi.org_id = lf.org_id")
	           .append(" where 1=1");
	           if(loanParam != null && loanParam.getSourceId() != null && loanParam.getSourceId() != -1L){//贷款机构ID
	              sql.append(" and lf.source_id=#{loanConf.sourceId}");
	           }
	           if(loanParam != null && loanParam.getOrgId() != null && loanParam.getOrgId() != -1L){//组织id
		              sql.append(" and lf.org_id=#{loanConf.orgId}");
		       }
	           if(loanParam != null && loanParam.getIsOpen()!= null && "1".equals(loanParam.getIsOpen()) ){//外放组织:“是”筛选出是外放组织展现在列表中
	               sql.append(" and oi.is_open='1' ");
	            }
	           if(loanParam != null && loanParam.getIsOpen()!= null && "0".equals(loanParam.getIsOpen())){//外放组织:“否”筛选出不是外放组织展现在列表中
	               sql.append(" and (oi.is_open='0' or oi.is_open is null) ");
	            }
	           sql.append(" order by lf.id desc ");
	           System.out.println(sql.toString());
	           return sql.toString();
	     }
    }

}
