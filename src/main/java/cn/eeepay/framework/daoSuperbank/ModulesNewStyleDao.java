package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.ModulesNewStyles;
import cn.eeepay.framework.model.OrgInfoOpenConf;
import cn.eeepay.framework.model.OrgSourceConf;
import cn.eeepay.framework.util.DateUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ModulesNewStyleDao {
	/**查询三大模块新样式列表*/
	
	@Select("select * from modules_new_styles where org_id = #{orgId} and style = 1 order by `order` ")
    @ResultType(ModulesNewStyles.class)
	List<ModulesNewStyles> selectTutorModelByOrgId(@Param("orgId") Long orgId);

    @Select("select * from modules_new_styles where org_id = #{orgId} and style = 2 order by `order` ")
    @ResultType(ModulesNewStyles.class)
    List<ModulesNewStyles> selectBankModelByOrgId(@Param("orgId") Long orgId);


    /**插入三大活动模块*/
	@InsertProvider(type=SqlProvider.class, method="insertModuleBatch")
	int insertModuleBatch(@Param("list") List<ModulesNewStyles> list, @Param("orgId") Long orgId);
	
	class SqlProvider {
        public String insertModuleBatch(Map<String, Object> param) {
            List<ModulesNewStyles> list = (List<ModulesNewStyles>) param.get("list");
            if (list == null || list.size() < 1) {
                return "";
            }
            StringBuilder values = new StringBuilder();
            MessageFormat message = new MessageFormat("(#'{'orgId},#'{'list[{0}].modulesImages}" +
                    ", #'{'list[{0}].modulesMatchUrl}, #'{'list[{0}].style},#'{'list[{0}].order}),");
            for (int i = 0; i < list.size(); i++) {
                values.append(message.format(new Integer[]{i}));
            }
            final String valuesSql = values.substring(1, values.length() - 2);//去掉最前面那个括号,和最后面的,)
            SQL sql = new SQL();
            sql.INSERT_INTO("modules_new_styles");
            sql.VALUES("org_id,modules_images,modules_match_url,style,`order`", valuesSql);
            System.out.println(sql.toString());
            return sql.toString();
        }
    }

    @Delete("DELETE FROM modules_new_styles WHERE org_id=#{orgId}")
    void deleteModuleByOrgId(@Param("orgId") Long orgId);
}
