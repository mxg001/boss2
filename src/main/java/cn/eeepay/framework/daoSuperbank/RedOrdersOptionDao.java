package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.RedOrdersOption;
import org.apache.ibatis.annotations.Insert;

public interface RedOrdersOptionDao {

    @Insert("insert into red_orders_option(red_order_id,create_date,opt_user_name,opt_content," +
            "reason,remark) values(#{redOrderId},#{createDate},#{optUserName},#{optContent}," +
            "#{reason},#{remark})")
    int insert(RedOrdersOption redOrdersOption);

}
