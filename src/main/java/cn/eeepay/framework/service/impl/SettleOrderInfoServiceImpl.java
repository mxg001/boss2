package cn.eeepay.framework.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.SettleOrderInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SettleTransfer;
import cn.eeepay.framework.service.SettleOrderInfoService;

@Service("settleOrderInfoService")
@Transactional
public class SettleOrderInfoServiceImpl implements SettleOrderInfoService {

	@Resource
	private SettleOrderInfoDao settleOrderInfoDao;
	@Resource
	private TransInfoDao transInfoDao;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private SysDictDao sysDictDao;

	private static final Logger log = LoggerFactory.getLogger(SettleOrderInfoService.class);

	@Override
	public List<SettleOrderInfo> selectAllInfo(Page<SettleOrderInfo> page, SettleOrderInfo soi) {
		return settleOrderInfoDao.selectAllInfo(page, soi);
	}

	@Override
	public SettleOrderInfo selectInfo(String tranId) {
		return settleOrderInfoDao.selectInfo(tranId);
	}

	@Override
	public List<SettleTransfer> selectSettleInfo(String tranId) {
		return settleOrderInfoDao.selectSettleInfo(tranId);
	}

	@Override
	public String getTotalNumAndTotalMoney(SettleOrderInfo soi) {
		return settleOrderInfoDao.getTotalNumAndTotalMoney(soi);
	}

	@Override
	public List<SettleOrderInfo> selectOutDetailAllInfo(Page<SettleOrderInfo> page, SettleOrderInfo soi) {
		soi.setPageSize(page.getPageSize());
		soi.setPageFirst(page.getFirst());
		List<SettleOrderInfo> list=settleOrderInfoDao.selectOutDetailAllInfo(soi);
		page.setResult(list);
		return list;
	}

	@Override
	public Map<String, String> getOutDetailTotalMoney(SettleOrderInfo soi) {
		return settleOrderInfoDao.getOutDetailTotalMoney(soi);
	}

	@Override
	public List<SettleOrderInfo> exportOutDetailAllInfo(SettleOrderInfo soi) {
		return settleOrderInfoDao.exportOutDetailAllInfo(soi);
	}

	@Override
	public SettleTransfer selectOutSettleInfo(String id) {
		return settleOrderInfoDao.selectOutSettleInfo(id);
	}

	@Override
	public SettleOrderInfo selectOrderNoInfo(String orderNo) {
		return settleOrderInfoDao.selectOrderNoInfo(orderNo);
	}

	@Override
	public List<SettleOrderInfo> importAllInfo(SettleOrderInfo soi) {
		return settleOrderInfoDao.importAllInfo(soi);
	}

	@Resource
	private TransferService transferService;

	@Override
	public SettleOrderInfo getBySettleOrder(String settleOrder) {
		return settleOrderInfoDao.getBySettleOrder(settleOrder);
	}

	@Override
	public int updateSettleOrderStatus(String settleOrder, int i) {
		return settleOrderInfoDao.updateSettleOrderStatus(settleOrder,1);
	}

	@Override
	public String changeSettleStatus(String currSettleStatus, String oldSettleStatus,String changeSettleStatusReason, String[] ids,String operatorPerson,Integer orderOrigin) {
		String msg = "";
		int hasChanged = 0;

		try {
			if(ids!=null && ids.length>0){
				//判断订单状态是否已经被修改
				boolean checkCanChangeSettleStatusFlag = checkCanChangeSettleStatus(ids,orderOrigin);
				if(!checkCanChangeSettleStatusFlag){
					msg = "存在已变更结算状态的出款明细订单！";
					return msg;
				}
				//判断订单状态是否一致
				if(ids.length>1){
					Integer firstRecordStatus = null;
					if(orderOrigin== Constants.ORDER_ST_ORIGIN){
						firstRecordStatus = getSettleStatusByAccountSerialNo(ids[0]);
					}else if(orderOrigin== Constants.ORDER_CTO_ORIGIN){
						firstRecordStatus = getCTOStatusByOrderNo(ids[0]);
					}
					for(int i=1;i<ids.length;i++){
						if(orderOrigin== Constants.ORDER_ST_ORIGIN){
							if(firstRecordStatus!=getSettleStatusByAccountSerialNo(ids[i])){
								msg = "请选择结算状态一致的订单！";
								return msg;
							}
						}else if(orderOrigin== Constants.ORDER_CTO_ORIGIN){
							if(firstRecordStatus!=getCTOStatusByOrderNo(ids[i])){
								msg = "请选择结算状态一致的订单！";
								return msg;
							}
						}

					}
				}

				//开始变更状态并保存操作日志
				for (int i = 0; i < ids.length; i++) {
					int result = 0;
					if(orderOrigin== Constants.ORDER_ST_ORIGIN){
						result = settleOrderInfoDao.changeSettleStatus(currSettleStatus, changeSettleStatusReason, ids[i]);
					}else if(orderOrigin== Constants.ORDER_CTO_ORIGIN){
						result = settleOrderInfoDao.changeCTOStatus(currSettleStatus, changeSettleStatusReason, ids[i].toString());
					}
					if(result==1){
						hasChanged += result;
						//保存操作日志
						settleOrderInfoDao.saveChangeSettleStatusLog(currSettleStatus,oldSettleStatus,changeSettleStatusReason,ids[i],operatorPerson,orderOrigin);
					}
				}
			}else{
				msg = "订单号为空";
				return msg;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			log.debug("订单状态变更失败",e);
		}
		//msg  = "变更"+ids.length+"记录,"+hasChanged+"变更状态成功";
		msg = "变更结算状态成功！";
		return msg;
	}
	@Override
	public Integer getCTOStatusByOrderNo(String orderNo) {
		if(orderNo	!=null){
			return settleOrderInfoDao.getCTOStatusById(orderNo);
		}
		return -1;
	}


	@Override
	public boolean checkCanChangeSettleStatus(String[] id,Integer orderOrigin) {
		StringBuilder sb = new StringBuilder("");
		if(id!=null && id.length>0){
			for (String s : id) {
				sb.append("'").append(s).append("',");
			}
		}
		int result = settleOrderInfoDao.checkCanChangeSettleStatus(StringUtil.delLastChar(sb.toString()),orderOrigin);
		if(result>0){
			return false;
		}
		return true;
	}

	@Override
	public Integer getSettleStatusByAccountSerialNo(String accountSerialNo) {
		if(accountSerialNo!=null){
			return settleOrderInfoDao.getSettleStatusById(accountSerialNo);
		}
		return -1;
	}

	@Override
	public List<SettleOrderInfo> selectByAccountNos(Page<SettleOrderInfo> page, String[] accountNos) {
		return settleOrderInfoDao.selectByAccountNos(page, accountNos);
	}

	@Override
	public Map<String, String> selectTotalMoneyByAccountNos(String[] accountNos) {
		return settleOrderInfoDao.selectTotalMoneyByAccountNos(accountNos);
	}
}
