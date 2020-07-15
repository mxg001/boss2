package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantServiceRateDao;
import cn.eeepay.framework.model.MerchantServiceRate;
import cn.eeepay.framework.service.MerchantServiceRateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("merchantServiceRateService")
@Transactional
public class MerchantServiceRateServiceImpl implements MerchantServiceRateService {
	
	private static final Pattern pattern=Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)$");
	private static final DecimalFormat format=new java.text.DecimalFormat("0.00");
	@Resource
	private MerchantServiceRateDao merchantServiceRateDao;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return merchantServiceRateDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(MerchantServiceRate record) {
		return merchantServiceRateDao.insert(record);
	}

	@Override
	public int updateByPrimaryKey(MerchantServiceRate record) {
		return merchantServiceRateDao.updateByPrimaryKey(record);
	}
	
	
	//用户商户费率修改查询
	public List<MerchantServiceRate> selectByMertId(MerchantServiceRate record) {
		return merchantServiceRateDao.selectByMertId(record);
	}

	//用户商户费率新增查询
	public MerchantServiceRate addSelectInfo(MerchantServiceRate record) {
		return merchantServiceRateDao.addSelectInfo(record);
	}

	@Override
	public String profitExpression(MerchantServiceRate rule) {
		if(rule==null||StringUtils.isEmpty(rule.getRateType())) return "";
		String profitExp=null;
		switch(rule.getRateType()){
			case "1": 
				profitExp=rule.getSingleNumAmount()==null?"":rule.getSingleNumAmount().toString();
				break;
			case "2":
				profitExp=rule.getRate()==null?"":rule.getRate().toString()+"%";
				break;
			case "3":
				profitExp=rule.getSafeLine()+"~"+rule.getRate()+"%~"+rule.getCapping();
				break;
			case "4":
				profitExp=rule.getRate()+"%+"+rule.getSingleNumAmount();	
				break;
			case "5":
				StringBuffer sb=new StringBuffer();
				sb.append(format.format(rule.getLadder1Rate())).append("%").append("<").append(format.format(rule.getLadder1Max()))
				  .append("<").append(format.format(rule.getLadder2Rate())).append("%");
				if(rule.getLadder2Max()!=null){
					sb.append("<").append(format.format(rule.getLadder2Max()))
					  .append("<").append(format.format(rule.getLadder3Rate())).append("%");
					if(rule.getLadder3Max()!=null){
						sb.append("<").append(format.format(rule.getLadder3Max()))
						  .append("<").append(format.format(rule.getLadder4Rate())).append("%");
						if(rule.getLadder4Max()!=null){
							sb.append("<").append(format.format(rule.getLadder4Max()));
						}
					}
				}
				profitExp=sb.toString();
				break;
			case "6":
				profitExp=format.format(rule.getRate())+"%~"+format.format(rule.getCapping());
			default : ;	
		}
		return profitExp;
	}

	@Override
	public MerchantServiceRate setMerchantServiceRate(MerchantServiceRate rate) {
		MerchantServiceRate rate1=new MerchantServiceRate();
		if(rate.getMerRate().indexOf("<")!=-1){
			if(!rate.getMerRate().matches("^(\\d+(\\.\\d+)?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d+)?)%)+"))
				throw new RuntimeException("服务费率设置错误！");
			String[] strs=rate.getMerRate().split("<");
			rate1.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
			rate1.setLadder1Max(new BigDecimal(strs[1]));
			rate1.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
			if(strs.length>3){
				rate1.setLadder2Max(new BigDecimal(strs[3]));
				rate1.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
				if(rate1.getLadder2Max().compareTo(rate1.getLadder1Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if(strs.length>5){
				rate1.setLadder3Max(new BigDecimal(strs[5]));
				rate1.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
				if(rate1.getLadder3Max().compareTo(rate1.getLadder2Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if(strs.length>7){
				rate1.setLadder4Max(new BigDecimal(strs[7]));
				if(rate1.getLadder4Max().compareTo(rate1.getLadder3Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			rate1.setRateType("5");
		}else if(rate.getMerRate().indexOf("+")!=-1){
			if(!rate.getMerRate().matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
				throw new RuntimeException("服务费率设置错误！");
			String[] temp=rate.getMerRate().split("%\\+");
			rate1.setRate(new BigDecimal(temp[0]));
			rate1.setSingleNumAmount(new BigDecimal(temp[1]));
			rate1.setRateType("4");
		}else if(rate.getMerRate().indexOf("~")!=-1){
			Matcher m = pattern.matcher(rate.getMerRate());
			while(m.find()){
				rate1.setSafeLine(new BigDecimal(m.group(1)));
				rate1.setRate(new BigDecimal(m.group(3)));
				rate1.setCapping(new BigDecimal(m.group(5)));
			}
			if(rate1.getSafeLine()==null||rate1.getCapping().compareTo(rate1.getSafeLine())<0)
				throw new RuntimeException("服务费率设置错误！");
			rate1.setRateType("3");
		}else if(rate.getMerRate().indexOf("%")!=-1){
			String str_=rate.getMerRate().substring(0, rate.getMerRate().indexOf("%"));
			if(str_.matches("\\d+(\\.\\d+)?")){
				rate1.setRate(new BigDecimal(str_));
			}else{
				throw new RuntimeException("服务费率设置错误！");
			}
			rate1.setRateType("2");
		}else {
			if(rate.getMerRate().matches("\\d+(\\.\\d+)?")){
				rate1.setSingleNumAmount(new BigDecimal(rate.getMerRate()));
			}else{
				throw new RuntimeException("服务费率设置错误！");
			}
			rate1.setRateType("1");
		}
		return rate1;
	}

	@Override
	public List<MerchantServiceRate> selectByMertIdAndSerivceId(String merId, String serId) {
		return merchantServiceRateDao.selectByMertIdAndSerivceId(merId,serId);
	}
	
}
