package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.ManorDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ManorService;
import cn.eeepay.framework.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级银行家-红包
 * 
 * @author tans
 * @date 2018-1-17
 */
@Service("manorService")
public class ManorServiceImpl implements ManorService {

	private Logger log = LoggerFactory.getLogger(ManorServiceImpl.class);

	@Resource
	private ManorDao manorDao;

	@Override
	public List<ManorMoney> selectManorMoneyPage(ManorMoney baseInfo, Page<ManorMoney> page) {
		List<ManorMoney> selectManorMoneyPage = manorDao.selectManorMoneyPage(baseInfo, page);
		for (ManorMoney manorMoney : selectManorMoneyPage) {
			if (StringUtils.isNotBlank(manorMoney.getNickName())) {
				try {
					manorMoney.setNickName(URLDecoder.decode(manorMoney.getNickName(), "utf-8"));
				} catch (Exception e) {
				}
			}
		}
		return selectManorMoneyPage;
	}

	@Override
	public ManorMainSum selectManorMoneySum(ManorMoney baseInfo) {
		return manorDao.selectManorMoneySum(baseInfo);
	}

	@Override
	public List<ManorMoney> selectManorMoneyDetlPage(ManorMoney baseInfo, Page<ManorMoney> page) {
		List<ManorMoney> selectManorMoneyDetlPage = manorDao.selectManorMoneyDetlPage(baseInfo, page);
		for (ManorMoney manorMoney : selectManorMoneyDetlPage) {
			if (StringUtils.isNotBlank(manorMoney.getNickName())) {
				try {
					manorMoney.setNickName(URLDecoder.decode(manorMoney.getNickName(), "utf-8"));
				} catch (Exception e) {
				}
			}
		}

		return selectManorMoneyDetlPage;
	}

	@Override
	public List<RedTerritoryBonusDetail> selectDailyBusinessPage(RedTerritoryBonusEveryday baseInfo, Page<RedTerritoryBonusDetail> page) {
		Page<RedTerritoryBonusEveryday> pageEveryday=new Page<>();
		BeanUtils.copyProperties(page,pageEveryday);
		List<RedTerritoryBonusEveryday> redTerritoryBonusEverydays = manorDao.selectDailyDividendPage(baseInfo, pageEveryday);
		List<RedTerritoryBonusDetail> details ;
		List<RedTerritoryBonusDetail> detailsAll = new ArrayList<>();
		if(redTerritoryBonusEverydays!=null&&redTerritoryBonusEverydays.size()>0) {
			List<Long> listEveryday = new ArrayList<>();
			for (RedTerritoryBonusEveryday everyday : redTerritoryBonusEverydays) {
				listEveryday.add(everyday.getId());
				RedTerritoryBonusDetail rec=new RedTerritoryBonusDetail();
				rec.setEverydayId(everyday.getId());
				BeanUtils.copyProperties(everyday,rec);
				detailsAll.add(rec);
			}
			List<RedTerritoryBonusDetail> redTerritoryBonusDetails = manorDao.selectDailyBusinessPage(listEveryday);
			Map<Long, List<RedTerritoryBonusDetail>> map;
			if (redTerritoryBonusDetails != null && redTerritoryBonusDetails.size() > 0) {
				map = new HashMap();
				List<RedTerritoryBonusDetail> redTerritoryBonusDetails1;
				for (RedTerritoryBonusDetail record : redTerritoryBonusDetails) {
					if (map.containsKey(record.getEverydayId())) {
						redTerritoryBonusDetails1 = map.get(record.getEverydayId());
					} else {
						redTerritoryBonusDetails1 = new ArrayList<>();
					}
					redTerritoryBonusDetails1.add(record);
					map.put(record.getEverydayId(), redTerritoryBonusDetails1);
				}
				details = new ArrayList<>();
				RedTerritoryBonusDetail record;
				for (Map.Entry<Long, List<RedTerritoryBonusDetail>> entry : map.entrySet()) {
					List<RedTerritoryBonusDetail> list = entry.getValue();
					record = list.get(0);
					for (RedTerritoryBonusDetail redTerritory : list) {
						if (redTerritory.getType() == 1) {
							record.setCreditCount(redTerritory.getBonusCount());
							record.setCreditBasicBonus(redTerritory.getBasicBonusAmount());
							record.setCreditBonusAmount(redTerritory.getBonusAmount());
						}
						if (redTerritory.getType() == 2) {
							record.setLoanCount(redTerritory.getBonusCount());
							record.setLoanBasicBonus(redTerritory.getBasicBonusAmount());
							record.setLoanBonusAmount(redTerritory.getBonusAmount());
						}
						if (redTerritory.getType() == 3) {
							record.setReceiptCount(redTerritory.getBonusCount());
							record.setReceiptBasicBonus(redTerritory.getBasicBonusAmount());
							record.setReceiptBonusAmount(redTerritory.getBonusAmount());
						}
						if (redTerritory.getType() == 4) {
							record.setInsuranceCount(redTerritory.getBonusCount());
							record.setInsuranceBasicBonus(redTerritory.getBasicBonusAmount());
							record.setInsuranceBonusAmount(redTerritory.getBonusAmount());
						}
						if (redTerritory.getType() == 5) {
							record.setBigDataCount(redTerritory.getBonusCount());
							record.setBigDataBasicBonus(redTerritory.getBasicBonusAmount());
							record.setBigDataBonusAmount(redTerritory.getBonusAmount());
						}
						if (redTerritory.getType() == 6) {
							record.setViolationCount(redTerritory.getBonusCount());
							record.setViolationBasicBonus(redTerritory.getBasicBonusAmount());
							record.setViolationBonusAmount(redTerritory.getBonusAmount());
						}
						if (redTerritory.getType() == 7) {
							record.setRepayCount(redTerritory.getBonusCount());
							record.setRepayBasicBonus(redTerritory.getBasicBonusAmount());
							record.setRepayBonusAmount(redTerritory.getBonusAmount());
						}
						if (redTerritory.getType() == 8) {
							record.setUpgradeCount(redTerritory.getBonusCount());
							record.setUpgradeBasicBonus(redTerritory.getBasicBonusAmount());
							record.setUpgradeBonusAmount(redTerritory.getBonusAmount());
						}
						if (redTerritory.getType() == 9) {
							record.setRedCount(redTerritory.getBonusCount());
							record.setRedBonus(redTerritory.getBasicBonusAmount());
							record.setRedAmount(redTerritory.getBonusAmount());
						}
					}
					details.add(record);
				}
				for (RedTerritoryBonusDetail rec : detailsAll) {
					for (RedTerritoryBonusDetail detail : details) {
						if (rec.getEverydayId() == detail.getEverydayId()) {
							rec.setCreditCount(detail.getCreditCount());
							rec.setCreditBasicBonus(detail.getCreditBasicBonus());
							rec.setCreditBonusAmount(detail.getCreditBonusAmount());
							rec.setLoanCount(detail.getLoanCount());
							rec.setLoanBasicBonus(detail.getLoanBasicBonus());
							rec.setLoanBonusAmount(detail.getLoanBonusAmount());
							rec.setReceiptCount(detail.getReceiptCount());
							rec.setReceiptBasicBonus(detail.getReceiptBasicBonus());
							rec.setReceiptBonusAmount(detail.getReceiptBonusAmount());
							rec.setInsuranceCount(detail.getInsuranceCount());
							rec.setInsuranceBasicBonus(detail.getInsuranceBasicBonus());
							rec.setInsuranceBonusAmount(detail.getInsuranceBonusAmount());
							rec.setBigDataCount(detail.getBigDataCount());
							rec.setBigDataBasicBonus(detail.getBigDataBasicBonus());
							rec.setBigDataBonusAmount(detail.getBigDataBonusAmount());
							rec.setViolationCount(detail.getViolationCount());
							rec.setViolationBasicBonus(detail.getViolationBasicBonus());
							rec.setViolationBonusAmount(detail.getViolationBonusAmount());
							rec.setRepayCount(detail.getRepayCount());
							rec.setRepayBasicBonus(detail.getRepayBasicBonus());
							rec.setRepayBonusAmount(detail.getRepayBonusAmount());
							rec.setUpgradeCount(detail.getUpgradeCount());
							rec.setUpgradeBasicBonus(detail.getUpgradeBasicBonus());
							rec.setUpgradeBonusAmount(detail.getUpgradeBonusAmount());
							rec.setRedCount(detail.getRedCount());
							rec.setRedBonus(detail.getRedBonus());
							rec.setRedAmount(detail.getRedAmount());
						}
					}
				}
			}
		}
		BeanUtils.copyProperties(pageEveryday,page);
		page.setResult(detailsAll);
		return detailsAll;
	}

	@Override
	public List<RedTerritoryBonusEveryday> selectDailyDividendPage(RedTerritoryBonusEveryday baseInfo, Page<RedTerritoryBonusEveryday> page) {
		List<RedTerritoryBonusEveryday> list=manorDao.selectDailyDividendPage(baseInfo,page);
		for(RedTerritoryBonusEveryday rec:list){
			rec.setPremiumTotalAmountConfStr((rec.getPremiumTotalAmountConf()==null?0:rec.getPremiumTotalAmountConf()).intValue()+"%");
		}
		return manorDao.selectDailyDividendPage(baseInfo,page);
	}

	@Override
	public ManorMainSum selectOrderSum(RedTerritoryBonusEveryday baseInfo) {
		return manorDao.selectOrderSum(baseInfo);
	}

	@Override
	public List<ManorTransactionRecore> selectManorTransactionRecorePage(ManorTransactionRecore baseInfo,
			Page<ManorTransactionRecore> page) {

		List<ManorTransactionRecore> selectManorTransactionRecorePage = manorDao
				.selectManorTransactionRecorePage(baseInfo, page);

		for (ManorTransactionRecore manorTransactionRecore : selectManorTransactionRecorePage) {

			if (StringUtils.isNotBlank(manorTransactionRecore.getNickName())) {
				try {
					manorTransactionRecore.setNickName(URLDecoder.decode(manorTransactionRecore.getNickName(), "utf-8"));
				} catch (Exception e) {
				}
			}

			if (manorTransactionRecore.getTradeFeeConf()!=null) {
				try {
					manorTransactionRecore.setTradeFeeConfStr(manorTransactionRecore.getTradeFeeConf()+"%");
				} catch (Exception e) {
				}
			}

			if (StringUtils.isNotBlank(manorTransactionRecore.getNewNickName())) {
				try {
					manorTransactionRecore.setNewNickName(URLDecoder.decode(manorTransactionRecore.getNewNickName(), "utf-8"));
				} catch (Exception e) {
				}
			}
			
			if (StringUtils.isNotBlank(manorTransactionRecore.getOldLordsUserCode()) && manorTransactionRecore.getOldLordsUserCode().equals("0")) {
				manorTransactionRecore.setUserName("系统");
				manorTransactionRecore.setNickName("系统");
				manorTransactionRecore.setOldPayPrice(new BigDecimal("200"));
				manorTransactionRecore.setPlateProfit(new BigDecimal("200"));
			}
			
		}

		return selectManorTransactionRecorePage;
	}

	@Override
	public ManorTransactionRecore selectManorTransactionRecorePageSum(ManorTransactionRecore baseInfo) {
		return manorDao.selectManorTransactionRecorePageSum(baseInfo);
	}

	@Override
	public ManorTransactionRecore selectManorTransactionRecorePageById(String id) {
		ManorTransactionRecore manorTransactionRecore = manorDao.selectManorTransactionRecorePageById(id);
		if(manorTransactionRecore.getTradeFeeConf()!=null){
			manorTransactionRecore.setTradeFeeConfStr(manorTransactionRecore.getTradeFeeConf()+"%");
		}
		return manorTransactionRecore;
	}

	@Override
	public List<ManorAdjustRecore> selectManorAdjustRecorePage(ManorAdjustRecore baseInfo,
			Page<ManorAdjustRecore> page) {

		List<ManorAdjustRecore> selectManorAdjustRecorePage = manorDao.selectManorAdjustRecorePage(baseInfo, page);

		for (ManorAdjustRecore manorAdjustRecore : selectManorAdjustRecorePage) {
			if (StringUtils.isNotBlank(manorAdjustRecore.getNickName())) {
				try {
					manorAdjustRecore.setNickName(URLDecoder.decode(manorAdjustRecore.getNickName(), "utf-8"));
				} catch (Exception e) {
				}
				
			}
			
			if (StringUtils.isNotBlank(manorAdjustRecore.getUserName())) {
			try {
				manorAdjustRecore.setUserName(URLDecoder.decode(manorAdjustRecore.getUserName(), "utf-8"));
			} catch (Exception e) {
			}
			}
		}

		return selectManorAdjustRecorePage;
	}

	@Override
	public List<ManorQuery> selectManorQueryPage(ManorQuery baseInfo, Page<ManorQuery> page) {
		List<ManorQuery> selectManorQueryPage = manorDao.selectManorQueryPage(baseInfo, page);

		for (ManorQuery manorQuery : selectManorQueryPage) {
			if (StringUtils.isNotBlank(manorQuery.getNickName())) {
				try {
					manorQuery.setNickName(URLDecoder.decode(manorQuery.getNickName(), "utf-8"));
				} catch (Exception e) {
				}
			}
			
			if (StringUtils.isNotBlank(manorQuery.getUserName())) {
				try {
					manorQuery.setUserName(URLDecoder.decode(manorQuery.getUserName(), "utf-8"));
				} catch (Exception e) {
				}
			}
			
			
			if (StringUtils.isNotBlank(manorQuery.getLordsUserCode()) && manorQuery.getLordsUserCode().equals("0")) {
				manorQuery.setUserName("系统");
				manorQuery.setNickName("系统");
			}
		}

		return selectManorQueryPage;
	}

	@Override
	public ManorManager selectManorManager() {
		ManorManager manorManager = manorDao.selectManorManager();
		if(manorManager!=null){
			manorManager.setBenchmarkBeginDateStr(DateUtils.formatDateTime(manorManager.getBenchmarkBeginDate()));
			manorManager.setBenchmarkEndDateStr(DateUtils.formatDateTime(manorManager.getBenchmarkEndDate()));
			manorManager.setFenhongBeginDateStr(DateUtils.formatDateTime(manorManager.getFenhongBeginDate()));
			manorManager.setFenhongEndDateStr(DateUtils.formatDateTime(manorManager.getFenhongEndDate()));
			manorManager.setPremiumBeginDateStr(DateUtils.formatDateTime(manorManager.getPremiumBeginDate()));
			manorManager.setPremiumEndDateStr(DateUtils.formatDateTime(manorManager.getPremiumEndDate()));
		}
		return manorManager;
	}

	@Override
	public void insertManorManager(ManorManager info) {
		manorDao.insertManorManager(info);
	}

	@Override
	public void updateManorManager(ManorManager info) {
		manorDao.updateManorManager(info);
	}

}
