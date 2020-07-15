package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.OutAccountService;
import cn.eeepay.framework.model.TimingProduce;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/11/011.
 *@author  liuks
 * 预警统计Dao
 */
public interface TimingProduceDao {
    /**
     *查询交易
     */
    @SelectProvider(type=TimingProduceDao.SqlProvider.class,method="getTimingTransactionCount")
    @ResultType(TimingProduce.class)
    List<TimingProduce> getTimingTransaction( @Param("queryaram") TimingProduce we);

    /**
     *查询出款
     */
    @SelectProvider(type=TimingProduceDao.SqlProvider.class,method="getPaymentServiceInTheSettlement")
    @ResultType(TimingProduce.class)
    List<TimingProduce> getPaymentServiceInTheSettlement( @Param("queryaram") TimingProduce we);

    /**
     * 查询所有出款服务
     */
    @Select("select outService.*,acqOrg.acq_name acq_org_name " +
            " from out_account_service outService " +
            "     INNER JOIN acq_org acqOrg ON acqOrg.id=outService.acq_org_id " +
            " where outService.out_account_status=1 ")
    @ResultType(OutAccountService.class)
    List<OutAccountService> getOutAccountServiceList();


    class SqlProvider{
        public String getTimingTransactionCount(Map<String, Object> param){
            final TimingProduce tp = (TimingProduce) param.get("queryaram");
            StringBuffer sb=new StringBuffer();
            sb.append(" select count(*) total,cto.acq_org_id,acqOrg.acq_name,acqOrg.acq_enname,cto.acq_service_id,acqService.service_name,cto.trans_status ");
            sb.append("   from collective_trans_order cto");
            sb.append("   INNER JOIN acq_org acqOrg ON acqOrg.id=cto.acq_org_id ");
            sb.append("   INNER JOIN acq_service acqService ON acqService.id=cto.acq_service_id ");
            sb.append("  where cto.trans_status!='SUCCESS' and cto.acq_org_id is not null ");
            if(tp.getStartTime()!=null){
                sb.append(" and cto.create_time >=#{queryaram.startTime} ");
            }
            if(tp.getEndTime()!=null){
                sb.append(" and cto.create_time <#{queryaram.endTime} ");
            }
            sb.append(" GROUP BY cto.acq_org_id,cto.acq_service_id,acqOrg.acq_name,acqOrg.acq_enname,acqService.service_name,cto.trans_status ");
            return sb.toString();
        }


        public String getPaymentServiceInTheSettlement(Map<String, Object> param){
            final TimingProduce tp = (TimingProduce) param.get("queryaram");
            StringBuffer sb=new StringBuffer();
            sb.append(" select count(*) total,settle.out_service_id,outser.service_name as out_service_name,outser.acq_org_id,acqOrg.acq_name,acqOrg.acq_enname ");
            sb.append(" from settle_transfer settle ");
            sb.append("  INNER JOIN out_account_service outser ON outser.id=settle.out_service_id ");
            sb.append("  INNER JOIN acq_org acqOrg ON acqOrg.id=outser.acq_org_id ");

            if("1".equals(tp.getPaymentStatus())){
                sb.append(" where  settle.status in ('1','6') and settle.out_service_id is not null ");//已提交,未知
            }else if("2".equals(tp.getPaymentStatus())){
                sb.append(" where  settle.status='5' and settle.out_service_id is not null ");//交易失败
            }
            if(tp.getStartTime()!=null){
                sb.append(" and settle.create_time >=#{queryaram.startTime} ");
            }
            if(tp.getEndTime()!=null){
                sb.append(" and settle.create_time <#{queryaram.endTime} ");
            }
            sb.append(" GROUP BY settle.out_service_id,outser.service_name,outser.acq_org_id,acqOrg.acq_name,acqOrg.acq_enname ");
            return sb.toString();
        }
    }
}
