package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/10.
 *
 * @author zja
 * 商品分类dao
 */
public interface GoodsGroupDao {
    @SelectProvider(type = SqlProvider.class, method = "goodsGroupQuery")
    @ResultType(Map.class)
    List<Map> goodsGroupQuery(Map params, Page<Map> page);

    @SelectProvider(type = SqlProvider.class, method = "goodsGroupQuery")
    @ResultType(Map.class)
    List<Map> goodsGroupQueryMap(Map params);

    @InsertProvider(type=SqlProvider.class, method="addGoodsGroup")
    int addGoodsGroup(Map params);

    @UpdateProvider(type=SqlProvider.class,method="updateGoodsGroup")
    int updateGoodsGroup(Map params);

    @DeleteProvider(type=SqlProvider.class,method="deleteGoodsGroup")
    int deleteGoodsGroup(Map params);

    @SelectProvider(type=SqlProvider.class,method="countGoods")
    Map countGoods(Map params);

    class SqlProvider {
        /**
         * 查询商户分类
         * @param params
         * @return
         */
        public String goodsGroupQuery(final Map<String, Object> params) {
            StringBuffer sb = new StringBuffer();
            sb.append("select g.group_code,g.group_name,g.create_time,g.brand_code,g.num,b.brand_name")
                    .append(" from pa_goods_group g")
                    .append(" inner join pa_brand b on g.brand_code = b.brand_code");
            sb.append(" where 1=1");
            if (StringUtils.isNotBlank((CharSequence) params.get("groupName"))) {
                sb.append(" and g.group_name = #{groupName}");
            }
            if (StringUtils.isNotBlank((CharSequence) params.get("brandCode"))) {
                sb.append(" and g.brand_code = #{brandCode}");
            }
            if (StringUtils.isNotBlank((CharSequence) params.get("createTimeBegin")) && StringUtils.isNotBlank((CharSequence) params.get("createTimeEnd")) ) {
                sb.append(" and g.create_time BETWEEN #{createTimeBegin} and #{createTimeEnd}");
            }
            sb.append(" GROUP BY g.brand_code,g.group_name ORDER BY g.id desc");
            return sb.toString();
        }

        public String addGoodsGroup(final Map<String, Object> param){
            Map params = (Map) param.get("params");
            StringBuffer sb = new StringBuffer("INSERT INTO pa_goods_group");
            sb.append(" (group_code, group_name, brand_code, create_time, op_user,num)")
                    .append(" VALUES (REPLACE(UUID(),'-',''),#{groupName},#{brandCode},now(),#{op_user},#{num}")
                    .append(")");
            return sb.toString();
        }

        public String countGoods(final Map<String, Object> param){
            return "select COUNT(g.id) cnt from pa_goods g where g.group_code = #{groupCode}";
        }

        public String updateGoodsGroup(final Map<String, Object> param){
            StringBuffer sb = new StringBuffer("UPDATE pa_goods_group SET group_name=#{groupName}, brand_code=#{brandCode}, op_user=#{op_user}, num=#{num} WHERE group_code=#{groupCode}");
            return sb.toString();
        }

        public String deleteGoodsGroup(final Map<String, Object> param){
            return "delete from pa_goods_group where group_code = #{groupCode}";
        }
    }
}
