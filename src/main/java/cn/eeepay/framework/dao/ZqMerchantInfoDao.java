package cn.eeepay.framework.dao;

import cn.eeepay.boss.action.ZqMerInfoAction;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantsUpstream;
import cn.eeepay.framework.model.ZqMerchantInfo;
import cn.eeepay.framework.model.ZqMerchantLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/19.
 * liuks 修改 2017-11-15
 */
public interface ZqMerchantInfoDao {

    @Select("select * from zq_merchant_info where mbp_id=#{mbpId} and channel_code=#{channelCode}")
    @ResultType(ZqMerchantInfo.class)
    ZqMerchantInfo selectZqMerInfoBymbpIDAndChannel(@Param("mbpId") String mbpId, @Param("channelCode") String channelCode);

    @Insert("   insert into zq_merchant_info(merchant_no,sync_status,effective_status,channel_code,unionpay_mer_no,terminal_no,sync_remark,create_time,update_time,operator,report_status,regid,mbp_id) "
            + " values(#{zqMerInfo.merchantNo},#{zqMerInfo.syncStatus},#{zqMerInfo.effectiveStatus},#{zqMerInfo.channelCode},#{zqMerInfo.unionpayMerNo},#{zqMerInfo.terminalNo}," +
            "   #{zqMerInfo.syncRemark},#{zqMerInfo.createTime},#{zqMerInfo.updateTime},#{zqMerInfo.operator},#{zqMerInfo.reportStatus},#{zqMerInfo.regid},#{zqMerInfo.mbpId})")
    @Options(useGeneratedKeys = true, keyProperty = "zqMerInfo.id")
    int insertZqMerInfo(@Param("zqMerInfo") ZqMerchantInfo zqMerInfo);

    @Update("update zq_merchant_info set effective_status = '1' where channel_code = #{channelCode} and mbp_id=#{mbpId}")
    int updateCurrZqMerInfoEffStatus(@Param("mbpId") String mbpId, @Param("channelCode") String channelCode);

    @Update("update zq_merchant_info set effective_status = '0' where channel_code <> #{channelCode} and mbp_id=#{mbpId}")
    int updateOtherZqMerInfoEffStatus(@Param("mbpId") String mbpId, @Param("channelCode") String channelCode);

    @Update("update zq_merchant_info set sync_status=#{zqMerInfo.syncStatus},effective_status=#{zqMerInfo.effectiveStatus}, " +
            "unionpay_mer_no=#{zqMerInfo.unionpayMerNo},terminal_no=#{zqMerInfo.terminalNo},sync_remark=#{zqMerInfo.syncRemark},update_time=#{zqMerInfo.updateTime},operator=#{zqMerInfo.operator}, "+
            "regid=#{zqMerInfo.regid},mbp_id=#{zqMerInfo.mbpId} " +
            "where id = #{zqMerInfo.id} ")
    int updateZqMerInfo(@Param("zqMerInfo") ZqMerchantInfo zqMerInfo);

    @SelectProvider(type = SqlProvider.class, method = "selectAllZqMerInfo")
    @ResultType(ZqMerchantInfo.class)
    List<ZqMerchantInfo> selectAllZqMerInfoByPage(@Param("page") Page<ZqMerchantInfo> page, @Param("zqMerParams") ZqMerInfoAction.ZqMerParams zqMerParams);

    @SelectProvider(type = SqlProvider.class, method = "selectAllZqMerInfo")
    @ResultType(ZqMerchantInfo.class)
    List<ZqMerchantInfo> selectAllZqMerInfo(@Param("zqMerParams") ZqMerInfoAction.ZqMerParams zqMerParams);

    /*@Select("select zml.*,sis.service_name from zq_merchant_log zml "
            + "left join merchant_service msi on (msi.merchant_no=zml.merchant_no and msi.channel_code= zml.channel_code)"
            + "left join service_info sis on sis.service_id = msi.service_id "
            + "where msi.merchant_no=#{merchantNo} and msi.bp_id=#{bpId} ")*/
    @Select("SELECT DISTINCT log.id, log.channel_code,si.service_id,si.service_name,log.sync_requires,log.sync_status,log.sync_remark,log.create_time " +
            "FROM zq_merchant_log log " +
            "LEFT JOIN merchant_service ms ON log.merchant_no= ms.merchant_no " +
            "LEFT JOIN merchant_business_product mbp ON log.mbp_id = mbp.id AND ms.bp_id = mbp.bp_id " +
            "INNER JOIN zq_service_info zsi ON zsi.merchant_no = log.merchant_no AND zsi.mbp_id = log.mbp_id AND zsi.channel_code = log.channel_code " +
            "LEFT JOIN service_info si ON si.service_id = zsi.service_id " +
            "WHERE log.merchant_no =#{merchantNo} AND mbp.bp_id=#{bpId} " +
            "ORDER BY log.create_time ASC")
    @ResultType(ZqMerchantLog.class)
    List<ZqMerchantLog> selectZqMerLogsByMerAndMbpId(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);

    //针对WF_ZQ的同步记录查询
    @Select("SELECT * FROM zq_merchant_log WHERE channel_code = 'WF_ZQ' AND merchant_no = #{merchantNo}")
    @ResultType(ZqMerchantLog.class)
    List<ZqMerchantLog> selectZqMerLogsInWFZQ(@Param("merchantNo") String merchantNo);
    
   // createtime>=DATE_SUB(NOW(),INTERVAL 5 MINUTE);
    @Select("SELECT zq.merchant_no,mbp.bp_id FROM zq_merchant_info  zq ,merchant_business_product mbp WHERE zq.mbp_id = mbp.id AND zq.channel_code ='YS_ZQ' AND zq.report_status IS NULL and sync_status = '2' AND  zq.create_time >=DATE_SUB(NOW(),INTERVAL #{type} HOUR) ")
    @ResultType(ZqMerchantInfo.class)
    List<ZqMerchantInfo> selectYsSyncMer(String type);
    
    @Select("select * from zq_merchant_info where unionpay_mer_no = #{unionpayMerNo} ")
    @ResultType(ZqMerchantLog.class)
    ZqMerchantInfo selectByUnionpayMerNo(@Param("unionpayMerNo") String unionpayMerNo);


    @Select("select id from zq_merchant_info where unionpay_mer_no = #{unionpayMerNo} ")
    @ResultType(MerchantsUpstream.class)
    List<MerchantsUpstream> selectMerchantsUpstreamByUnionpayMerNo(@Param("unionpayMerNo") String unionpayMerNo);

    @SelectProvider(type = SqlProvider.class, method = "selectAllMerchantsUpstream")
    @ResultType(MerchantsUpstream.class)
    List<MerchantsUpstream> selectAllMerchantsUpstream(@Param("page") Page<MerchantsUpstream> page,@Param("list")List<String> list,@Param("start")int start);


    @SelectProvider(type = SqlProvider.class, method = "selectAllMerchantsUpstream")
    @ResultType(MerchantsUpstream.class)
    List<MerchantsUpstream> exportDetail(@Param("list")List<String> list,@Param("start")int start);

    @Select("select id,sync_status,report_status from zq_merchant_info" +
            " where merchant_no=#{merNo} and mbp_id=#{mbpId} and channel_code=#{acqEnname}" +
            " order by create_time desc limit 1")
    @ResultType(ZqMerchantInfo.class)
    ZqMerchantInfo selectByMerMbpAcq(@Param("merNo")String merNo,@Param("mbpId")String mbpId,@Param("acqEnname")String acqEnname);


    class SqlProvider {

        public String selectAllMerchantsUpstream(Map<String, Object> param){
            final List<String> list =(List<String>)param.get("list");
            final int start=Integer.valueOf(param.get("start").toString());
            return new SQL(){{
                SELECT("t1.id,t1.channel_code,t1.unionpay_mer_no,t1.mbp_id,t1.merchant_no,t2.merchant_name,t2.mobilephone,(@rowNum:=@rowNum+1) as rowNo");

                if(start>0){
                    FROM("zq_merchant_info t1 LEFT JOIN merchant_info t2 on t2.merchant_no=t1.merchant_no,(Select (@rowNum :="+start+") ) b ");
                }else{
                    FROM("zq_merchant_info t1 LEFT JOIN merchant_info t2 on t2.merchant_no=t1.merchant_no,(Select (@rowNum :=0) ) b ");
                }

                if(list!=null&&list.size()>0){
                    StringBuilder sb = new StringBuilder();
                    MessageFormat message = new MessageFormat("#'{'list[{0}]}");
                    for (int i = 0; i < list.size(); i++) {
                        sb.append(message.format(new Integer[]{i}));
                        sb.append(",");
                    }
                    sb.setLength(sb.length()-1);
                    WHERE(" t1.unionpay_mer_no in ("+sb.toString()+")");
                }
            }}.toString();
        }

        public String selectAllZqMerInfo(Map<String, Object> param) {
            final ZqMerInfoAction.ZqMerParams zqMerParams = (ZqMerInfoAction.ZqMerParams) param.get("zqMerParams");
            String sql = new SQL() {{
                SELECT(" DISTINCT zmi.*,mbp.id AS mbpId,ms.bp_id,ms.trade_type,mi.mobilephone as mi_mobilephone,mi.merchant_name," +
                        "ai.agent_name,bpd.bp_name,ui.user_name AS operatorName");
                FROM(" zq_merchant_info zmi ");
                LEFT_OUTER_JOIN(" merchant_business_product mbp ON ( mbp.merchant_no = zmi.merchant_no AND zmi.mbp_id = mbp.id )");
                LEFT_OUTER_JOIN(" merchant_service ms ON (mbp.merchant_no = ms.merchant_no AND zmi.channel_code = ms.channel_code AND mbp.bp_id = ms.bp_id ) ");
                LEFT_OUTER_JOIN(" merchant_info mi on (mi.merchant_no = zmi.merchant_no) ");
                LEFT_OUTER_JOIN(" agent_info ai on ai.agent_no = mi.agent_no ");
                LEFT_OUTER_JOIN(" business_product_define bpd on bpd.bp_id=mbp.bp_id ");
                LEFT_OUTER_JOIN(" user_info ui on zmi.operator=ui.id ");

                if(StringUtils.isNotBlank(zqMerParams.getAgentNo())){
                    WHERE("ai.agent_no = #{zqMerParams.agentNo}");
                }
                if(StringUtils.isNotBlank(zqMerParams.getUnionpayMerNo())){
                    WHERE("zmi.unionpay_mer_no = #{zqMerParams.unionpayMerNo}");
                }
                if(StringUtils.isNotBlank(zqMerParams.getMbpId())){
                    WHERE("mbp.id = #{zqMerParams.mbpId}");
                }
                if(StringUtils.isNotBlank(zqMerParams.getMerchantNo())){
                    WHERE("(mi.merchant_no = #{zqMerParams.merchantNo} or mi.merchant_name = #{zqMerParams.merchantNo})");
                }
                if(StringUtils.isNotBlank(zqMerParams.getChannelCode())){
                    WHERE("zmi.channel_code = #{zqMerParams.channelCode}");
                }
                if(StringUtils.isNotBlank(zqMerParams.getProductType())){
                    WHERE("bpd.bp_id = #{zqMerParams.productType}");
                }
                if(StringUtils.isNotBlank(zqMerParams.getMobilephone())){
                    WHERE("mi.mobilephone = #{zqMerParams.mobilephone}");
                }
                if(StringUtils.isNotBlank(zqMerParams.getAccountName())){
                    WHERE("mi.lawyer=#{zqMerParams.accountName}  ");
                }
                if(StringUtils.isNotBlank(zqMerParams.getCardId())){
                    WHERE("mi.id_card_no=#{zqMerParams.cardId}  ");
                }
                if(StringUtils.isNotBlank(zqMerParams.getTerminalNo())){
                    WHERE("zmi.terminal_no=#{zqMerParams.terminalNo}  ");
                }
                if(StringUtils.isNotBlank(zqMerParams.getTradeType())){
                    WHERE("ms.trade_type=#{zqMerParams.tradeType}");
                }
                if(StringUtils.isNotBlank(zqMerParams.getSyncStatus()) && "1".equals(zqMerParams.getTradeType())){
                    WHERE("zmi.effective_status='1' and zmi.sync_status = #{zqMerParams.syncStatus}");
                }
                if(zqMerParams.getsTime()!=null){
                    WHERE("zmi.create_time>=#{zqMerParams.sTime}");
                }
                if(zqMerParams.geteTime()!=null){
                    WHERE("zmi.create_time<=#{zqMerParams.eTime}");
                }

            }}.toString();
            return sql;
        }
    }
    @Select("select * from zq_merchant_info where regid =#{regid}")
    @ResultType(ZqMerchantInfo.class)
	ZqMerchantInfo selectByRegid(@Param("regid") String  regid);

    @Select("select unionpay_mer_no from zq_merchant_info where merchant_no =#{merNo} and mbp_id=#{bpId} and channel_code=#{channelCode}")
    @ResultType(ZqMerchantInfo.class)
    ZqMerchantInfo selectByMerNoAndBpId(@Param("merNo") String  merNo,@Param("bpId") String  bpId, @Param("channelCode") String channelCode);
}
