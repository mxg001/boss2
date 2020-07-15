package cn.eeepay.framework.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.daoSuperbank.LotteryOrderDao;
import cn.eeepay.framework.daoSuperbank.SuperBankUserInfoDao;
import cn.eeepay.framework.model.LotteryOrder;
import cn.eeepay.framework.service.TranslateRow;
import cn.eeepay.framework.util.ExcelErrorMsgBean;
@Service
public class LotteryRowImpl implements TranslateRow<LotteryOrder> {
	private final static Logger log = LoggerFactory.getLogger(SuperBankServiceImpl.class);

    @Resource
    private LotteryOrderDao lotteryOrderDao;
    @Resource
    private SuperBankUserInfoDao userInfoDao;
    

    @Override
    public LotteryOrder translate(Row row, int index, List<ExcelErrorMsgBean> errors) {
    	LotteryOrder record = null;
    	 record = getShBankRecord(row, index, errors);
        return record;
    }

    /**
     * 获取彩票记录
     * @param bankSourceId
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private LotteryOrder getShBankRecord(Row row, int index
            , List<ExcelErrorMsgBean> errors) {
    	Map<Integer,String> temList = getRecordTemplate();
    	//List<Integer> canEmptyList = canEmptyFiledList();
    	/**valList存放解析的值*/
    	Map<Integer,String> lottery = new HashMap<Integer,String>();
    	for(int i = 0;i <= temList.size();i++){
    		String cellValue = null;
    		if(row.getCell(i) != null){
    	          row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
    	          cellValue = row.getCell(i).getStringCellValue();
    	     }
    		/*if(!canEmptyList.contains(i) && (StringUtils.isBlank(cellValue) || "null".equals(cellValue))){
    			errors.add(new ExcelErrorMsgBean(index, i+1, temList.get(i)));
    			return null;
    		}*/
			lottery.put(i, cellValue);
		}
		boolean redeemTimeValid = false;
		boolean betTimeValid = false;
		LotteryOrder record = new LotteryOrder();
		//ID
		if(lottery.get(1) == null){
			record.setOutId("");
		}else{
			record.setOutId(lottery.get(1));
		}
		//投注设备号
		if(lottery.get(2) == null){
			record.setDeviceNo("");
		}else{
			record.setDeviceNo(lottery.get(2));
		}
		//投注设备流水号
		if(lottery.get(3) == null){
			record.setDeviceJno("");
		}else{
			record.setDeviceJno(lottery.get(3));
		}
		//用户ID
		record.setOutUserId(lottery.get(4));
		//用户编号
		record.setUserCode(lottery.get(5));
		//商户号
		record.setMenchantNo(lottery.get(6));
		//商户名称
		record.setMenchantName(lottery.get(7));
		//订单号
		record.setOutOrderNo(lottery.get(8));
		//彩种
		record.setLotteryType(lottery.get(9));
		//投注期号
		record.setIssue(lottery.get(10));
		//投注时间
		if(lottery.get(11) != null && !"".equals(lottery.get(11)) && isValidDate(lottery.get(11))){
			record.setBetTime(lottery.get(11));
		}else{
			betTimeValid = true; //非法时间
			log.info("投注非法时间'"+lottery.get(11)+"'");
		}
		//兑奖时间
		if(!"".equals(lottery.get(12))){
			if(isValidDate(lottery.get(12))){
				record.setRedeemTime(lottery.get(12));
			}else{
				redeemTimeValid = true; //非法时间
				log.info("兑奖非法时间'"+lottery.get(12)+"'");
			}
		}else{
			record.setRedeemTime(null);
		}
		//购买状态
		record.setBuyStatus(lottery.get(13));
		//大奖标志
		record.setIsBigPrize(lottery.get(14));
		//兑奖标志
		record.setRedeemFlag(lottery.get(15));
		//消费e豆
		if(lottery.get(16) != null && !"".equals(lottery.get(16))){
			String consumee = (String)lottery.get(16);
			if(consumee.matches("^\\d+$")){
				record.setConsumeE(Integer.valueOf((String)lottery.get(16)));
			}else{
				record.setConsumeE(0);
			}
		}
		//中奖总金额
		record.setAwardAmount(lottery.get(17));
		
		record.setSportLottery("1");
		
		Integer userCount = null;

		if(record != null && (record.getDeviceNo() == null || "".equals(record.getDeviceNo())
				|| record.getDeviceJno() == null || "".equals(record.getDeviceJno()))){
			log.info("WARNNING:投注设备号或者投注设备流水号为空" + record + "~~~~");
			errors.add(new ExcelErrorMsgBean(index, 0, "5"));
			return null;

		}
		if(record != null  && (record.getConsumeE() ==null || record.getConsumeE() == 0)){
			log.info("WARNNING:消费E豆为0" + record + "~~~~");
			errors.add(new ExcelErrorMsgBean(index, 0, "5"));
			return null;

		}

		if(record != null  && !"出票成功".equals(record.getBuyStatus())){
			log.info("WARNNING:该订单购买状态不是出票成功" + record + "~~~~");
			errors.add(new ExcelErrorMsgBean(index, 0, "1"));
			return null;
		}
		String userCode = "";
		if(record != null && record.getUserCode() != null && !"".equals(record.getUserCode())){
        	/*int prefixCount = 3;
        	int subfixCount = 4;
        	if(record.getUserCode().length() > prefixCount + subfixCount){
        		userCode = restoreUserCode(record.getUserCode(), prefixCount, subfixCount);
        		userCount = userInfoDao.selectUserInfoByUserCode(userCode);
        	}*/
        	userCount = userInfoDao.selectUserInfoByUserCode(record.getUserCode());
        }
        if(userCount == null || userCount == 0){
        	log.info("WARNNING:超级银行家User Code 为" + record.getUserCode()+"的用户不存在~~~~");
        	errors.add(new ExcelErrorMsgBean(index, 0, "2"));
        	return null;
        }else{
        	record.setUserCode(record.getUserCode());
        }
        if(checkExists(record)){
        	log.info("WARNNING:超级银行家已存在记录" + record + "~~~~");
            errors.add(new ExcelErrorMsgBean(index, 0, "3"));
            return null;
        }
        if(betTimeValid || redeemTimeValid){
        	log.info("WARNNING:非法的时间格式" + record + "~~~~");
            errors.add(new ExcelErrorMsgBean(index, 0, "4"));
            return null;
        }

	    String orderNo = getOrderNo(record.getUserId()); //生成OrderNo
	    record.setOrderNo(orderNo);
	  //  record.setBatchNo(record.getBatchNo());
	    record.setAwardRequire("成功出票");
	    record.setMenchantNo(record.getMenchantNo()+" ");
		int lcount = lotteryOrderDao.saveLotteryOrder(record); //生成彩票记录
		if(lcount > 0){
			log.info("彩票订单设备号："+record.getDeviceJno()+"流水号为："+record.getDeviceNo() + "生成成功！");
		}
        
        return record;
    }
    
	/**
	 * 生成Order No
	 * @param memberId
	 * @return
	 *
	 **/
	public static String getOrderNo(String memberId){
		 String orderNo = "SU";
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		 Random ne=new Random();
	     int ram=ne.nextInt(9999-1000+1)+1000;
		 if(memberId != null){
			 orderNo += sdf.format(new Date()) + String.valueOf(ram) + memberId;
		 }else{
			 orderNo += sdf.format(new Date()) + String.valueOf(ram);
		 }
		 return orderNo;
	 }
    
    /**解析时间格式*/
    public static boolean isValidDate(String str) {  
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        try{  
            Date date = (Date)formatter.parse(str);  
            return str.equals(formatter.format(date));  
        }catch(Exception e){  
            return false;  
        }  
    }  
    
    private String restoreUserCode(String userCodeStr,int prefixCount,int subfixCount){
    	String userCode = "";
    	if(userCodeStr == null){
    		return userCode;
    	}
    	userCode = userCodeStr.substring(prefixCount, userCodeStr.length()-subfixCount);
    	log.info(userCode);
    	return userCode;
    }
    
    /**允许为空字段*/
    private static List<Integer> canEmptyFiledList(){
    	Integer[] indexs = new Integer[]{12,14,15};
    	return Arrays.asList(indexs);
    }
    
    /**excel模板*/
    private static Map<Integer,String> getRecordTemplate(){
    	Map<Integer,String> map = new HashMap<Integer,String>();
    	map.put(1, "id");
    	map.put(2, "投注设备号");
    	map.put(3, "投注设备流水号");
    	map.put(4, "用户ID");
    	map.put(5, "用户编号");
    	map.put(6, "商户号");
    	map.put(7, "商户名称");
    	map.put(8, "订单号");
    	map.put(9, "彩种");
    	map.put(10, "投注期号");
    	map.put(11, "投注时间");
    	map.put(12, "兑奖时间");
    	map.put(13, "购买状态");
    	map.put(14, "大奖标志");
    	map.put(15, "兑奖标志");
    	map.put(16, "消费e豆");
    	map.put(17, "中奖总金额");
    	return map;
    }
    
    private boolean checkExists(LotteryOrder record) {
        if(record == null){
            return  false;
        }
        int num = lotteryOrderDao.checkExists(record);
        if(num > 0){
            return true;
        }
        return false;
    }

}
