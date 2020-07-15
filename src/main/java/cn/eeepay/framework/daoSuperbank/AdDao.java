package cn.eeepay.framework.daoSuperbank;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Ad;

/**
 * 超级银行家-广告管理表
 * @author Administrator
 *
 */
public interface AdDao {

	@SelectProvider(type = SqlProvider.class, method = "findAd")
	@ResultType(Ad.class)
	List<Ad> find(@Param("ad") Ad ad,@Param("page") Page<Ad> page);
	
	@Insert("insert into banner_info(id,apply_type,org_id,img_url,title,show_no,status,up_date," +
			"down_date,link,remark,postion_type)" +
			" values(#{id},#{applyType},#{orgId},#{imgUrl},#{title},#{showNo},#{status},#{upDate}," +
			"#{downDate},#{link},#{remark},#{postionType})")
	@ResultType(Long.class)
	long add(Ad ad);

	@Update("update banner_info set apply_type=#{applyType},org_id=#{orgId},img_url=#{imgUrl}," +
			"title=#{title},show_no=#{showNo},status=#{status},up_date=#{upDate},down_date=#{downDate}," +
			"link=#{link},remark=#{remark},postion_type=#{postionType}" +
			" where id = #{id}")
	@ResultType(Long.class)
	long upd(Ad ad);
	
	@Update("update banner_info set status=#{status} where id=#{id}")
	long on_off(Ad ad);
	
	@Delete("delete from banner_info where id=#{id}")
	long del(@Param("id") String id);
	
	@Select("select b.*, o.org_name " +
            " from banner_info b left join org_info o on o.org_id = b.org_id where b.id=#{id}")
	Ad detail(Ad ad);
	
	@Select("select key_value from sys_id where key_id='bannerManage'")
	long getSysId();
	@Update("update sys_id set key_value=#{newId} where key_id='bannerManage' and key_value=#{oldId}")
	int updSysId(@Param("newId") long newId,@Param("oldId") long oldId);
	
	public class SqlProvider{
	     public String findAd(Map<String, Object> param){
	           StringBuffer sql = new StringBuffer("");
	           final Ad ad = (Ad) param.get("ad");
	           sql.append(" select b.*,oi.org_name" +
                       " from banner_info b left join org_info oi on b.org_id = oi.org_id" +
                       " where 1=1 ");
	           if(ad != null){
	        	   if(ad.getOrgId()!=null&&ad.getOrgId() != -1L){
	        		   sql.append(" and b.org_id=#{ad.orgId}");
	        	   }
	        	   if(StringUtils.isNotBlank(ad.getPostionType()) ){
	        		   sql.append(" and b.postion_type=#{ad.postionType}");
	        	   }
	        	   if(ad.getTitle()!=null&&!"".equals(ad.getTitle())){
					   ad.setTitle(ad.getTitle() + "%");
	        		   sql.append(" and b.title like #{ad.title}");
	        	   }
	        	   if(ad.getId()!=null&&ad.getId()!=-1){
	        		   sql.append(" and b.id=#{ad.id}");
	        	   }
	        	   if(ad.getStatus()!=null&&!"-1".equals(ad.getStatus())){
	        		   sql.append(" and b.status=#{ad.status}");
	        	   }
	        	   if(StringUtils.isNotBlank(ad.getApplyType()) ){
					   sql.append(" and b.apply_type=#{ad.applyType}");
				   }
	           }
	           
	           sql.append(" order by b.id desc ");
	           
	           System.out.println("---------------------------"+sql.toString()+"---------------------------");
	           return sql.toString();
	     }

   }
}
