package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.HappyBackActivityMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.HappyBackActivityMerchant;
import cn.eeepay.framework.service.HappyBackActivityMerchantService;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author tgh
 * @description 欢乐返活跃商户活动查询
 * @date 2019/8/20
 */
@Service
public class HappyBackActivityMerchantServiceImpl implements HappyBackActivityMerchantService {

	@Resource
	private HappyBackActivityMerchantDao happyBackActivityMerchantDao;

	@Resource
	private AgentInfoDao agentInfoDao;

	@Override
	public void selectHappyBackActivityMerchant(Page<HappyBackActivityMerchant> page,HappyBackActivityMerchant happyBackActivityMerchant) {
		happyBackActivityMerchantDao.selectHappyBackActivityMerchant(page,happyBackActivityMerchant);
		List<HappyBackActivityMerchant> list = page.getResult();
		for (HappyBackActivityMerchant item : list) {
			setAgentInfomation(item);
		}
	}

	@Override
	public Map<String, Object> countMoney(HappyBackActivityMerchant happyBackActivityMerchant) {
		Map<String, Object> map = new HashMap<>();
		map.put("rewardAmountTotalEd",happyBackActivityMerchantDao.countRewardAmount(happyBackActivityMerchant,"1"));//1:已奖励
		map.put("rewardAmountTotal",happyBackActivityMerchantDao.countRewardAmount(happyBackActivityMerchant,"0"));//0:待奖励
		map.put("deductAmountTotalEd",happyBackActivityMerchantDao.countDeductAmount(happyBackActivityMerchant,"1"));//1:已扣款
		map.put("deductAmountTotal",happyBackActivityMerchantDao.countDeductAmount(happyBackActivityMerchant,"3"));//3:待扣款
		map.put("deductAmountTotalAdvance",happyBackActivityMerchantDao.countDeductAmount(happyBackActivityMerchant,"2"));//2:发起预调账
		return map;
	}

	@Override
	public void exportExcel(HappyBackActivityMerchant happyBackActivityMerchant,HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<HappyBackActivityMerchant> list = happyBackActivityMerchantDao.exportExcel(happyBackActivityMerchant);
		String fileName = "欢乐返活跃商户活动" + sdf.format(new Date()) + ".xls" ;
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		Map<String, String> repeatStatusMap = new HashMap<>();
		repeatStatusMap.put("0", "否");
		repeatStatusMap.put("1", "是");
		List<Map<String, String>> data = new ArrayList<>() ;

		Map<String,String> deductStatusMap=new HashMap<String, String>();
		deductStatusMap.put("0","未扣款");
		deductStatusMap.put("1","已扣款");
		deductStatusMap.put("2","已发起预调账");
		deductStatusMap.put("3","待扣款");

		for(HappyBackActivityMerchant item: list){
			setAgentInfomation(item);
			Map<String, String> map = new HashMap<>();
			map.put("activeOrder", item.getActiveOrder());
			map.put("activeTime", item.getActiveTime() == null ? "" : sdfTime.format(item.getActiveTime()));
			map.put("targetStatus", "1".equals(item.getTargetStatus()) ? "已达标" : "未达标");
			map.put("rewardAmount", item.getRewardAmount() == null ? "0.00" : item.getRewardAmount().toString());
			map.put("rewardAccountStatus", item.getRewardAccountStatus()==null?"":"1".equals(item.getRewardAccountStatus()) ? "已入账" : "未入账");
			map.put("targetTime", item.getTargetTime() == null ? "" : sdfTime.format(item.getTargetTime()));
			map.put("rewardAccountTime", item.getRewardAccountTime() == null ? "" : sdfTime.format(item.getRewardAccountTime()));
			map.put("deductAmount", item.getDeductAmount() == null ? "0.00" : item.getDeductAmount().toString());
			map.put("deductStatus", deductStatusMap.get(item.getDeductStatus()));
			map.put("deductTime", item.getDeductTime() == null ? "" : sdfTime.format(item.getDeductTime()));
			map.put("repeatStatus", StringUtils.trimToEmpty(repeatStatusMap.get(item.getRepeatStatus()==null?"":item.getRepeatStatus())));
			map.put("activityTypeNo", item.getActivityTypeNo());
			map.put("merchantNo", item.getMerchantNo());
			map.put("teamName", item.getTeamName());
			map.put("teamEntryName", item.getTeamEntryName());
			map.put("hardId", item.getHardId()==null?"":item.getHardId().toString());
			map.put("agentName", item.getAgentName());
			map.put("agentNo", item.getAgentNo());
			map.put("oneAgentName", item.getOneAgentName());
			map.put("oneAgentNo", item.getOneAgentNo());
			data.add(map);
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"activeOrder","activeTime","targetStatus","rewardAmount","rewardAccountStatus",
				"targetTime","rewardAccountTime","deductAmount","deductStatus","deductTime","repeatStatus",
				"activityTypeNo","merchantNo","teamName","teamEntryName","hardId",
				"agentName","agentNo","oneAgentName","oneAgentNo"};
		String[] colsName = new String[]{"激活订单号","激活日期","奖励达标状态","奖励金额(元)","奖励入账状态","奖励达标日期","奖励入账日期",
				"扣款金额(元)","扣款状态","扣款/调账日期","是否重复注册",
				"欢乐返子类型编号","所属商户编号","所属组织","所属子组织","硬件产品ID",
				"所属代理商名称","所属代理商编号","所属一级代理商名称","所属一级代理商编号"};
		OutputStream ouputStream=null;
		try {
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}
	}

	/**
	 * 设置所属代理商和一级代理商编号/名称
	 * @param item
	 */
	private void setAgentInfomation(HappyBackActivityMerchant item) {
		String agentNode = item.getAgentNode();//所属代理商节点
		AgentInfo info = agentInfoDao.selectByAgentNode(agentNode);
		if(info!=null){
			String agentNo = info.getAgentNo();
			String agentName = info.getAgentName();
			item.setAgentNo(agentNo);
			item.setAgentName(agentName);
			item.setOneAgentNo(agentNo);
			item.setOneAgentName(agentName);
			if (info.getAgentLevel() != 1) {
				String oneLevelId = info.getOneLevelId();//一级代理商编号
				AgentInfo oneAgentInfo = agentInfoDao.selectByAgentNo(oneLevelId);
				item.setOneAgentNo(oneLevelId);
				item.setOneAgentName(oneAgentInfo.getAgentName());
			}
		}
	}
}
