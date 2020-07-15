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
import cn.eeepay.framework.model.CreditCardBonus;
import cn.eeepay.framework.model.CreditcardSource;

public interface CreditCardBonusDao {

	@SelectProvider(type = SqlProvider.class, method = "selectCreditCardByPage")
	@ResultType(CreditCardBonus.class)
	List<CreditCardBonus> getCreditCardConf(@Param("creditCardConf") CreditCardBonus creditCardConf,
			@Param("pager") Page<CreditCardBonus> pager);
	
	 public class SqlProvider{

     public String selectCreditCardByPage(Map<String, Object> param){
           StringBuffer sql = new StringBuffer("");
           final CreditCardBonus creditCard = (CreditCardBonus) param.get("creditCardConf");
           sql.append(("select cf.org_id,oi.org_name,cs.BANK_NAME,cs.bank_nick_name,cs.BANK_BONUS,cs.card_bonus,cs.first_brush_bonus,oi.is_open,cf.* "));
           sql.append(" from creditcard_bonus_conf cf");
           sql.append(" left join org_info oi on cf.ORG_ID=oi.org_id ");
           sql.append(" left join creditcard_source cs on cf.SOURCE_ID=cs.ID");
           sql.append(" where 1=1"); 
           if(creditCard != null && creditCard.getSourceId() != null && creditCard.getSourceId() != -1L){
              sql.append(" and cf.source_id=#{creditCardConf.sourceId}");
           }
           if(creditCard != null && creditCard.getOrgId() != null && creditCard.getOrgId() != -1L){
               sql.append(" and cf.org_id=#{creditCardConf.orgId}");
            }
           if(creditCard != null && creditCard.getIsOpen()!= null && "1".equals(creditCard.getIsOpen()) ){//外放组织:筛选出是外放组织展现在列表中
               sql.append(" and oi.is_open='1' ");
            }
           if(creditCard != null && creditCard.getIsOpen()!= null && "0".equals(creditCard.getIsOpen())){//“否”筛选出不是外放组织展现在列表中
               sql.append(" and (oi.is_open='0' or oi.is_open is null) ");
            }
            
           sql.append(" order by cf.id desc ");
           
           return sql.toString();
        }
    }
	
	@Select("select id,bank_name,bank_nick_name,show_order,bank_bonus,card_bonus,first_brush_bonus from creditcard_source order by convert(bank_name using gbk)")
	@ResultType(CreditcardSource.class)
	List<CreditcardSource> allBanksList();
	
	@Update("update creditcard_bonus_conf set org_cost=#{orgCost},ORG_PUSH_COST=#{orgPushCost},is_onlyone=#{isOnlyone}," +
			" update_by=#{updateBy}, update_date=sysdate()," +
			"card_company_bonus=#{cardCompanyBonus},card_oem_bonus=#{cardOemBonus},first_company_bonus=#{firstCompanyBonus},first_oem_bonus=#{firstOemBonus}" +
			" where id=#{id}")
	int updCreditCardConf(CreditCardBonus creditCardConf);
	
	@Update("update creditcard_source set BANK_BONUS=#{bankBonus} where id=#{sourceId}")
	int updBankConf(CreditCardBonus creditCardConf);
	
	@Insert("insert into creditcard_source (id,bank_name,bank_bonus,status) values(#{id},#{bankName},#{bankBonus},'on')")
	int saveBank(@Param("id") long id,@Param("bankName") String bankName,@Param("bankBonus") String bankBonus);
	
	@Insert("insert into creditcard_bonus_conf (org_id,source_id,org_cost,org_push_cost,update_by,update_date,is_onlyone," +
			"card_company_bonus,card_oem_bonus,first_company_bonus,first_oem_bonus) " +
			"values(#{orgId},#{sourceId},#{orgCost},#{orgPushCost},#{updateBy},sysdate(),#{isOnlyone}," +
			"#{cardCompanyBonus},#{cardOemBonus},#{firstCompanyBonus},#{firstOemBonus})")
	int saveCreditCardConf(CreditCardBonus creditCardConf);
	
	@Select("select key_value from sys_id where key_id='creditcard_manage'")
	long getSysId();
	@Update("update sys_id set key_value=#{newId} where key_id='creditcard_manage' and key_value=#{oldId}")
	int updSysId(@Param("newId") long newId,@Param("oldId") long oldId);
	
	/**获取直营配置数据*/
	@Select("select cb.* from creditcard_bonus_conf cb where cb.org_id=(select oi.org_id from org_info oi where oi.v2_orgcode=#{orgCode})")
	List<CreditCardBonus> getDirectSalesConf(@Param("orgCode") String orgCode);
	
	@Select("select count(id) from creditcard_bonus_conf where source_id=#{sourceId}")
	int isExist(@Param("sourceId") long sourceId);
	
	@Select("select count(id) from creditcard_bonus_conf where source_id=#{sourceId} and org_id=#{orgId}")
	int isExistBankConfWithSiAndOrgId(@Param("sourceId") long sourceId,@Param("orgId") Long orgId);
	
}
