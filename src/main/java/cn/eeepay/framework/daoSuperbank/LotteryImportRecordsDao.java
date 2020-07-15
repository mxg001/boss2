package cn.eeepay.framework.daoSuperbank;

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

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LoanSource;
import cn.eeepay.framework.model.LotteryImportRecords;


public interface LotteryImportRecordsDao {

	/**查询彩票导入记录*/
	@SelectProvider(type = SqlProvider.class, method = "selectLotteryImportRecordsByPage")
	@ResultType(LotteryImportRecords.class)
	List<LotteryImportRecords> getLotteryImportRecords(@Param("lotteryRecord") LotteryImportRecords lotteryImportRecords,
			@Param("pager") Page<LotteryImportRecords> pager);
	
	 public class SqlProvider{
	     public String selectLotteryImportRecordsByPage(Map<String, Object> param){
	           StringBuffer sql = new StringBuffer("");
	           final LotteryImportRecords lotteryParam = (LotteryImportRecords) param.get("lotteryRecord");
	           sql.append(("select lr.id,lr.batch_no,lr.import_date,lr.file_name,lr.file_url,lr.status,lr.update_date,lr.update_by,lr.remark,lr.lottery_type "));
	           sql.append((" from LOTTERY_IMPORT_RECORDS lr where 1=1"));
	           
	           if(lotteryParam != null){
	        	   if(StringUtils.isNotBlank(lotteryParam.getStatus())){
			              sql.append(" and lr.status=#{lotteryRecord.status}");
			       }
	        	   
	                if(StringUtils.isNotBlank(lotteryParam.getImportDateStart())){
	                    sql.append(" and lr.import_date >= #{lotteryRecord.importDateStart}");
	                }
	                if(StringUtils.isNotBlank(lotteryParam.getImportDateEnd())){
	                    sql.append(" and lr.import_date <= #{lotteryRecord.importDateEnd}");
	                }
	                if(StringUtils.isNotBlank(lotteryParam.getBatchNo())){
			              sql.append(" and lr.batch_no like concat('%',#{lotteryRecord.batchNo},'%')");
			       }
	               if(StringUtils.isNotBlank(lotteryParam.getLotteryType())){
			              sql.append(" and lr.lottery_type = #{lotteryRecord.lotteryType}");
			       }
	            }
	            
	           sql.append(" order by lr.id desc ");
	           
	           System.out.println(sql.toString());
	           
	           return sql.toString();
	     }
    }

	
	/**新增彩票导入记录*/
	@Insert("insert into LOTTERY_IMPORT_RECORDS " +
			"(batch_no,import_date,file_name,file_url,status,update_by,update_date,lottery_type)" +
			" values" +
			"(#{batchNo},sysdate(),#{fileName},#{fileUrl},#{status},#{updateBy},sysdate(),#{lotteryType})")
	int saveLotteryImportRecords(LotteryImportRecords lotteryRecord);
	
	
	/** 删除状态为1的导入记录 */
	@Delete("delete from LOTTERY_IMPORT_RECORDS where id=#{id} and status = '1' ")
	int deleteLotteryImportRecord(@Param("id") Long id);
	
	
	/**获取导入记录状态为*/
    @Select("select id,batch_no,import_date,file_name,file_url,status,update_date,update_by,lottery_type from LOTTERY_IMPORT_RECORDS where status = '1' ")
    @ResultType(LotteryImportRecords.class)
    List<LotteryImportRecords> getLotteryListByStatus();
    
    /**修改导入记录的状态*/
    @Update("update LOTTERY_IMPORT_RECORDS set status=#{param.status},remark=#{param.remark},update_date=sysdate() where batch_no=#{param.batchNo} ")
    int updateLotteryListByBatchNo(@Param("param") Map param);
	
    /**根据ID获取导入记录*/
    @Select("select id,batch_no,import_date,file_name,file_url,status,update_date,update_by from LOTTERY_IMPORT_RECORDS where id = #{id} ")
    @ResultType(LotteryImportRecords.class)
    LotteryImportRecords getLotteryImportRecordById(@Param("id") String id);
	
}
