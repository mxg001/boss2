package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.RankList;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/6/006.
 * @author  liuks
 * 排行榜 list Dao
 */
public interface RankListDao {

    @SelectProvider(type=RankListDao.SqlProvider.class,method="selectAllList")
    @ResultType(RankList.class)
    List<RankList> selectAllList(@Param("info")RankList info,@Param("page") Page<RankList> page);

    @SelectProvider(type=RankListDao.SqlProvider.class,method="selectAllList")
    @ResultType(RankList.class)
    List<RankList> importDetailSelect(@Param("info")RankList info);

    @SelectProvider(type=RankListDao.SqlProvider.class,method="selectAllSum")
    @ResultType(Map.class)
    Map<String,Object> selectAllSum(@Param("info")RankList info,@Param("page")Page<RankList> page);


    class SqlProvider{

        public String selectAllList(final Map<String, Object> param) {
            return  getSelectSql(1,param);
        }
        public String selectAllSum(final Map<String, Object> param) {
            return  getSelectSql(2,param);
        }
        private String getSelectSql(int sta,Map<String, Object> param){
            RankList info = (RankList) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            if(sta==1){
                sb.append(" rankList.*, ");
                sb.append(" agentUser.agent_no oneAgentNo, ");
                sb.append(" userInfo.real_name,userInfo.nick_name,userInfo.mobile,userInfo.user_type,userInfo.one_user_code ");
            }else if(sta==2){
                sb.append(" sum(rankList.reward_amount) amountCount1, ");
                sb.append(" sum(IF(rankList.entry_status=1,rankList.reward_amount,0)) amountCount2, ");//已入账
                sb.append(" sum(IF(rankList.entry_status=0,rankList.reward_amount,0)) amountCount3 ");//未入账

            }
            sb.append(" from pa_month_rank_list rankList ");
            sb.append("  LEFT JOIN pa_user_info userInfo ON userInfo.user_code=rankList.user_code ");
            sb.append("  LEFT JOIN pa_agent_user agentUser ON agentUser.user_code=userInfo.one_user_code ");
            sb.append("where 1=1");
            if(StringUtils.isNotBlank(info.getCountMonth())){
                sb.append(" and rankList.count_month = #{info.countMonth} ");
            }
            if(StringUtils.isNotBlank(info.getUserCode())){
                sb.append(" and rankList.user_code = #{info.userCode} ");
            }
            if(StringUtils.isNotBlank(info.getMobile())){
                sb.append(" and userInfo.mobile = #{info.mobile} ");
            }
            if(StringUtils.isNotBlank(info.getOneAgentNo())){
                sb.append(" and agentUser.agent_no = #{info.oneAgentNo} ");
            }
            if(StringUtils.isNotBlank(info.getOneUserCode())){
                sb.append(" and userInfo.one_user_code = #{info.oneUserCode} ");
            }
            if(StringUtils.isNotBlank(info.getEntryStatus())){
                sb.append(" and rankList.entry_status = #{info.entryStatus} ");
            }else{
                sb.append(" and rankList.entry_status != 2 ");
            }
            if(StringUtils.isNotBlank(info.getNickName())){
                sb.append(" and userInfo.nick_name like concat(#{info.nickName},'%') ");
            }
            if(info.getEntryTimeBegin() != null){
                sb.append(" and rankList.entry_time >= #{info.entryTimeBegin}");
            }
            if(info.getEntryTimeEnd() != null){
                sb.append(" and rankList.entry_time <= #{info.entryTimeEnd}");
            }
            sb.append(" order by rankList.count_month DESC,rankList.rank ASC ");
            return sb.toString();
        }
    }

}
