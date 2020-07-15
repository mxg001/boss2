package cn.eeepay.framework.dao.func;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.func.LoanFinancing;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface LoanFinancingDao {

    @SelectProvider(type= LoanFinancingDao.SqlProvider.class,method="selectAllList")
    @ResultType(LoanFinancing.class)
    List<LoanFinancing> selectAllList(@Param("info")LoanFinancing info,@Param("page") Page<LoanFinancing> page);

    @SelectProvider(type= LoanFinancingDao.SqlProvider.class,method="selectAllList")
    @ResultType(LoanFinancing.class)
    List<LoanFinancing> importList(@Param("info")LoanFinancing info);

    @Insert(
            "INSERT INTO loan_product_config " +
                    "(product_name,sort_no,status,product_link_url,log_img,create_time,operator) " +
                    " VALUES " +
                    "(#{info.productName},#{info.sortNo},0,#{info.productLinkUrl},#{info.logImg},now(),#{info.operator})"
    )
    int addLoanFinancing(@Param("info")LoanFinancing info);

    //更新数据
    @UpdateProvider(type= LoanFinancingDao.SqlProvider.class,method="updateLoanFinancing")
    @ResultType(Integer.class)
    int updateLoanFinancing(@Param("info")LoanFinancing info);

    @Update(
            "update loan_product_config set status=#{status},operator=#{operator} where id=#{id}"
    )
    int updateLoanFinancingStatus(@Param("id")int id, @Param("status")int status, @Param("operator")String operator);

    @Select(
            " select * from loan_product_config where id=#{id} "
    )
    LoanFinancing getLoanFinancing(@Param("id")int id);

    class SqlProvider{

        public String selectAllList(final Map<String, Object> param) {
            final LoanFinancing info = (LoanFinancing) param.get("info");
            return new SQL(){{
                SELECT(" * ");
                FROM("loan_product_config ");
                if(info.getId()!=null){
                    WHERE("id = #{info.id} ");
                }
                if(StringUtils.isNotBlank(info.getProductName())){
                    WHERE("product_name like concat(#{info.productName},'%') ");
                }
                if(info.getStatus()!=null){
                    WHERE("status = #{info.status} ");
                }
                if(info.getCreateTimeBegin() != null){
                    WHERE("create_time >= #{info.createTimeBegin}");
                }
                if(info.getCreateTimeEnd() != null){
                    WHERE("create_time <= #{info.createTimeEnd}");
                }
                ORDER_BY("sort_no asc,create_time desc");
            }}.toString();
        }

        public String updateLoanFinancing(final Map<String, Object> param) {
            final LoanFinancing info = (LoanFinancing) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" update loan_product_config ");
            sb.append("  set product_name=#{info.productName},sort_no=#{info.sortNo},operator=#{info.operator}, ");
            if(StringUtils.isNotBlank(info.getLogImg())){
                sb.append("log_img=#{info.logImg}, ");
            }
            sb.append("product_link_url=#{info.productLinkUrl} ");
            sb.append(" where id=#{info.id}");
            return sb.toString();
        }
    }
}
