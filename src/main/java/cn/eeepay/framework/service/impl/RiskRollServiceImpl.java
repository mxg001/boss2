package cn.eeepay.framework.service.impl;

import java.util.*;

import javax.annotation.Resource;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.AutoTransferService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.eeepay.framework.dao.RiskRollDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.RiskRollService;
import cn.eeepay.framework.util.ClientInterface;

@Service("riskRollService")
@Transactional
public class RiskRollServiceImpl implements RiskRollService {

	private final static Logger log = LoggerFactory.getLogger(RiskRollServiceImpl.class);

	@Resource
	private RiskRollDao riskRollDao;

	@Resource
	private MerchantInfoService merchantInfoService;
	@Resource
	private AutoTransferService autoTransferService;

	@Override
	public List<RiskRoll> selectRollAllInfo(Page<RiskRoll> page, RiskRoll record) {
		return riskRollDao.selectRollAllInfo(page, record);
	}

	@Override
	public List<BlackOperLog> selectBlackLogs(Page<BlackOperLog> page, String rollId) {
		return riskRollDao.selectBlackLogs(page, rollId);
	}

	@Override
	public void insertBlackLog(BlackOperLog b){
		riskRollDao.insertBlackLog(b);
	}

	@Override
	public List<BlackOperLog> selectBlackLogsByMerNo(String merchantNo){
		return riskRollDao.selectBlackLogsByMerNo(merchantNo);
	}

	@Override
	public List<BlackOperLog> selectMoreTime(Page<BlackOperLog> page,String rId){
		return riskRollDao.selectMoreTime(page,rId);
	}

	@Override
	public List<BlackOperLog> selectMoreTime2(String rId){
		return riskRollDao.selectMoreTime2(rId);
	}

	@Override
	public RiskRoll checkRollByRollNo(String rollNo, Integer rollType, Integer rollBelong) {
		return riskRollDao.checkRollByRollNo(rollNo, rollType, rollBelong);
	}

	@Override
	public int insertRoll(RiskRoll record) {
		return riskRollDao.insertRoll(record);
	}

	public Map<String, Object> insertRollReLoad(RiskRoll record) {
		int i=0;
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("bols", false);
		map.put("msg", "新增失败");
		i = riskRollDao.insertRoll(record);
		if(i>0){
			map.put("msg", "新增成功");
			map.put("bols", true);
		}else{
			throw new RuntimeException("新增失败");
		}
		return map;
	}
	
	//组合rollNo
	public String rollNoCombination(int id,String no){
		List<RiskRoll> rlist = riskRollDao.selectRollInfo(id);
		int num[]=new int[rlist.size()];
		if(rlist.size()>=1){
			for (int i=0;i<rlist.size();i++) {
				if(id==1){
					String[] str=rlist.get(i).getRollNo().split("B");
					num[i]=Integer.valueOf(str[1]);
				}else{
					String[] str=rlist.get(i).getRollNo().split("H");
					num[i]=Integer.valueOf(str[1]);
				}

			}
			int max=0;
			for (int i = 0; i < num.length; i++) {
				if(max<num[i]){
					max=num[i];
				}
			}
			int[] shu=new int[]{10,100,1000,10000};
			String[] zi=new String[]{"000","00","0"};//H0001
			for (int i = 0; i < shu.length; i++) {
				if(max<shu[i]){
					max++;
					if(i>2){
						no+=max;
						break;
					}
					no+=zi[i]+max;
					break;
				}
			}
			if(riskRollDao.selectRollByRollNo(no)!=null){
				return "0";
			}
		}else{
			no+="0001";
		}
		return no;
	}
	@Override
	public List<RiskRollList> selectRollList(Page<RiskRollList> page, RiskRollList record) {
		return riskRollDao.selectRollList(page, record);
	}

	@Override
	public int insertRollList(RiskRollList record) {
		return riskRollDao.insertRollList(record);
	}

	@Override
	public RiskRoll selectRollDetail(int id) {
		return riskRollDao.selectRollDetail(id);
	}

	@Override
	public int deleteRollListInfo(int id,Map<String, Object> jsonMap) {
		// 查询出信息为记录
		RiskRoll oldInfo = riskRollDao.selectRollDetail(id);
		if(oldInfo!=null
				&&oldInfo.getRollStatus()!=null
				&&1==oldInfo.getRollStatus().intValue()){
			jsonMap.put("bols", false);
			jsonMap.put("msg", "请先关闭名单状态在进行删除操作!");
			return 0;
		}
		int num=riskRollDao.deleteRollListInfo(id);
		if (num > 0) {
			if(2==oldInfo.getRollBelong().intValue()){
				// 删除成功记录操作
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				BlackOperLog b = new BlackOperLog();
				b.setRollNo(oldInfo.getRollNo());
				b.setBlackType(String.valueOf(oldInfo.getRollType()));
				b.setCreateTime(new Date());
				b.setOperationType("删除黑名单");
				b.setCreateBy(principal.getRealName());
				b.setRemark(oldInfo.getRemark());
				insertBlackLog(b);
			}
			jsonMap.put("bols", true);
			jsonMap.put("msg", "删除成功");
		} else {
			jsonMap.put("bols", false);
			jsonMap.put("msg", "删除失败");
		}
		return num;
	}

	@Override
	public int deleteBatch(String idStr,Map<String, Object> jsonMap) {
		if(StringUtils.isNotBlank(idStr)){
			String[] strs=idStr.split(",");
			if(strs!=null&&strs.length>0){
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				int sucessNum=0;
				for (String str:strs) {
					int id=Integer.valueOf(str);
					RiskRoll oldInfo = riskRollDao.selectRollDetail(id);
					if(oldInfo.getRollStatus()==0){
						int num=riskRollDao.deleteRollListInfo(id);
						if(num>0){
							sucessNum++;
							if(2==oldInfo.getRollBelong().intValue()){
								// 删除成功记录操作
								BlackOperLog b = new BlackOperLog();
								b.setRollNo(oldInfo.getRollNo());
								b.setBlackType(String.valueOf(oldInfo.getRollType()));
								b.setCreateTime(new Date());
								b.setOperationType("删除黑名单");
								b.setCreateBy(principal.getRealName());
								b.setRemark(oldInfo.getRemark());
								insertBlackLog(b);
							}
						}
					}
				}
				jsonMap.put("bols", true);
				jsonMap.put("msg", "批量删除成功,选中"+strs.length+"条数据,成功删除"+sucessNum+"条数据");
				return 1;
			}
		}
		jsonMap.put("bols", false);
		jsonMap.put("msg", "批量删除失败!");
		return 0;
	}
	@Override
	public int updateRollStatus(RiskRoll record) {
		if(!"1".equals(record.getRollBelong()+"")){
			if("1".equals(record.getRollType()+"")||"4".equals(record.getRollType()+"")){
				MerchantInfo merInfo=merchantInfoService.selectMerExistByMerNo(record.getRollNo());
				if(merInfo!=null){
					RiskRoll recordSH=null;
					RiskRoll recordQB=null;
					if(record.getRollType().toString().equals("1")) {//黑名单类型：商户编号
						recordSH=record;
						recordQB=riskRollDao.findRiskRollByRollNoAndRollTypeAndBelongType(record.getRollNo(),4,2);
					}
					if(record.getRollType().toString().equals("4")) {//黑名单类型：钱包出账
						recordSH=riskRollDao.findRiskRollByRollNoAndRollTypeAndBelongType(record.getRollNo(),1,2);
						recordQB=record;
					}
					String nowRiskStatus=merInfo.getRiskStatus();
					String finalRiskStatus=null;
					String accountStatus=null;

					if(record.getRollStatus()==1){//风控开关由关闭改为开启
						if(record.getRollType().toString().equals("1")) {//黑名单类型：商户编号
							finalRiskStatus="3";
						}
						if(record.getRollType().toString().equals("4")) {//黑名单类型：钱包出账
							if(recordSH!=null&&recordSH.getRollStatus()==1){
								finalRiskStatus="3";
							}else{
								finalRiskStatus="2";
							}
						}
					}else{//风控开关由开启改为关闭
						if(record.getRollType().toString().equals("1")) {//黑名单类型：商户编号
							if(recordQB!=null&&recordQB.getRollStatus()==1){
								finalRiskStatus="2";
							}else{
								finalRiskStatus="1";
							}
						}
						if(record.getRollType().toString().equals("4")) {//黑名单类型：钱包出账
							if(recordSH!=null&&recordSH.getRollStatus()==1){
								finalRiskStatus="3";
							}else{
								finalRiskStatus="1";
							}
						}
					}
					if("1".equals(finalRiskStatus)){
						accountStatus="1";
					}else if("2".equals(finalRiskStatus)){
						accountStatus="3";
					}else if("3".equals(finalRiskStatus)){
						accountStatus="4";
					}
					if(finalRiskStatus!=null){
						if(!finalRiskStatus.equals(nowRiskStatus)){//不相同才修改

							final HashMap<String, Object> claims = new HashMap<String, Object>();
							claims.put("subjectNo", "224101001");
							claims.put("accountType", "M");
							claims.put("currencyNo", "1");
							claims.put("accountOwner", "000001");
							claims.put("selectType", "2");
							claims.put("accountNo", null);
							claims.put("cardNo", null);
							claims.put("userId", record.getRollNo());
							claims.put("accountStatus", accountStatus);
							String result = ClientInterface.transFrozenAccountNo(claims);//锁定账户
							if(StringUtils.isBlank(result)){
								return 0;
							}else{
								JSONObject json=JSON.parseObject(result);
								if((boolean)json.get("status")){
									int num=merchantInfoService.updateRiskStatusbyBlack(record.getRollNo().toString(), finalRiskStatus);
									if(num<=0){
										return 0;
									}
								}else{
									return 0;
								}
							}
						}
					}else{
						return 0;
					}
				}
			}
		}
		int i=riskRollDao.updateRollStatus(record);
		return i;
	}

	@Override
	public int updateRollInfo(RiskRoll record) {
		return riskRollDao.updateRollInfo(record);
	}

	@Override
	public RiskRoll selectRollByRollName(String rollName) {
		return riskRollDao.selectRollByRollName(rollName);
	}

	@Override
	public RiskRollList selectInfoByrollNoAndMerNo(RiskRollList record) {
		return riskRollDao.selectInfoByrollNoAndMerNo(record);
	}

	@Override
	public List<RiskRoll> selectRollByRollNo(String rollNo) {
		return riskRollDao.selectRollByRollNo(rollNo);
	}


	@Override
	public int updateOpenBatch(String idStr){
		int i = 0;
		String[] str=idStr.split(",");
		for(String s:str){
			RiskRoll record=riskRollDao.selectRollDetail(Integer.parseInt(s));
			if(record==null){
				log.info("============黑名单id:{}不存在",s);
				throw new RuntimeException("数据异常");
			}
			if(record.getRollStatus()==1){
				i+=1;
				log.info("============黑名单已经打开,id:{}",s);
				continue;
			}
			record.setRollStatus(1);
			record.setId(Integer.parseInt(s));
			try {
				i+=updateRollStatus(record);
				log.info("============黑名单批量打开成功,id:{}",s);
				// 打开黑名单时更新auto_transfer表
				String rollType = record.getRollType().toString();
				if("1".equals(rollType) || "4".equals(rollType)){
					// 商户黑名单：检索auto_transfer表中是否存在该商户且状态为待执行的任务，将状态改为再风控
					autoTransferService.updateByMerchantNoAndStatus(record.getRollNo(), "1");
				}
			}catch (Exception e){
				log.error("===========黑名单批量打开异常,id:{}",s,e);
			}
		}
		return i;
	}

	@Override
	public RiskRoll selectRollByRollNoAndType(String rollNo, Integer rollType, Integer rollBelong) {
		return riskRollDao.selectRollByRollNoAndType(rollNo, rollType, rollBelong);
	}

	@Override
	public String findBlacklist(String rollNo, String rollType, String rollBelong) {
		return riskRollDao.findBlacklist(rollNo, rollType, rollBelong);
	}

	@Override
	public List<RiskRollExport> selectRollAllInfoExport(RiskRoll record) {
		return riskRollDao.selectRollAllInfoExport(record);
	}

	@Override
	public RiskRoll findRiskRollByRollNoAndRollType(String rollNo, Integer rulesInstruction) {
		return riskRollDao.findRiskRollByRollNoAndRollType(rollNo, rulesInstruction);
	}

	@Override
	public List<RiskRoll> selectRollAll() {
		return riskRollDao.selectRollAll();
	}
}
