package cn.eeepay.framework.daoSuperbank;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Notice;

/**
 * 超级银行家-公告表
 * @author Administrator
 *
 */
public interface NoticeDao {

	/**查询*/
	@SelectProvider(type = SqlProvider.class, method = "findNotice")
	@ResultType(Notice.class)
	List<Notice> find(@Param("notice") Notice searchCondi,@Param("pager") Page<Notice> pager);
	
	/**新增*/
	@Insert("insert into notice_info (notice_type,title,link,status,create_by,create_date,content," +
			"send_time,send_by_id,send_by_name,news_image,image_position,org_id,pop_switch,remark) " +
			"values(#{noticeType},#{title},#{link},#{status},#{createBy},#{createDate},#{content}," +
			"#{sendTime},#{sendById},#{sendByName},#{newsImage},#{imagePosition},#{orgId}," +
			"#{popSwitch},#{remark})")
	long add(Notice notice);
	
	/**修改*/
	@Update("update notice_info set title=#{title},link=#{link},status=#{status},content=#{content}," +
			"send_time=#{sendTime},send_by_id=#{sendById},send_by_name=#{sendByName}," +
			"news_image=#{newsImage},image_position=#{imagePosition},org_id=#{orgId}," +
			"pop_switch=#{popSwitch},remark=#{remark},update_by=#{updateBy},update_date=#{updateDate}" +
			" where id=#{id}")
	long upd(Notice notice);
	
	/**下发*/
	@Update("update notice_info set status=1,update_by=#{updateBy},update_date=sysdate(),send_time=sysdate() where id=#{id}")
	long release(Notice notice);
	
	/**详情*/
	@Select("select * from notice_info where id=#{id}")
	@ResultType(Notice.class)
	Notice detail(@Param("id") String id);

	@Update("update notice_info set status=#{status},send_by_id=#{sendById},send_by_name=#{sendByName}," +
			"send_time=#{sendTime} where id=#{id}")
	int sendNotice(Notice notice);

	@Delete("delete from notice_info where id=#{id}")
	int deleteNotice(Long id);

	@Update("update notice_info set pop_switch=#{popSwitch} where id = #{id}")
	int updateNoticePop(@Param("id") Long id, @Param("popSwitch") Integer popSwitch);

	public class SqlProvider{
	     public String findNotice(Map<String, Object> param){
	           StringBuffer sql = new StringBuffer("");
	           final Notice notice = (Notice) param.get("notice");
	           sql.append(" select * ");
	           sql.append(" from notice_info ni ");
	           sql.append(" where 1=1 ");
	          
	           if(notice != null ){
	              if(notice.getTitle()!=null&&!"".equals(notice.getTitle())){
	            	  sql.append(" and ni.title like '"+notice.getTitle()+"%'");
	              }
	              if(notice.getStartCreateTime()!=null&&!"".equals(notice.getStartCreateTime())){
	            	  sql.append(" and ni.create_date >= '"+notice.getStartCreateTime()+"'");
	              }
	              if(notice.getEndCreateTime()!=null && !"".equals(notice.getEndCreateTime())){
	            	  sql.append(" and ni.create_date <= '"+notice.getEndCreateTime()+"'");
	              }
	              if(notice.getStartReleaseTime()!=null&&!"".equals(notice.getStartReleaseTime())){
	            	  sql.append(" and ni.send_time >= '"+notice.getStartReleaseTime()+"'");
	              }
	              if(notice.getEndReleaseTime()!=null&&!"".equals(notice.getEndReleaseTime())){
	            	  sql.append(" and ni.send_time <= '"+notice.getEndReleaseTime()+"'");
	              }
	              if(notice.getStatus()!=null && !"".equals(notice.getStatus())&&!"-1".equals(notice.getStatus())){
	            	  sql.append(" and ni.status="+notice.getStatus());
	              }
	              if(StringUtils.isNotBlank(notice.getOrgId())&&!"-1".equals(notice.getOrgId())){
					  sql.append(" and find_in_set(" + notice.getOrgId() +",ni.org_id)");
				  }
				   if(notice.getPopSwitch() != null){
					   sql.append(" and ni.pop_switch = " + notice.getPopSwitch());
				   }
	           }
	           
	           sql.append(" order by ni.create_date desc ");
	           
	           System.out.println("-----------查询sql----------"+sql.toString());
	           
	           return sql.toString();
	     }
    }
}
