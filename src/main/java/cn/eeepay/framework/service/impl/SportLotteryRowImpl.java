package cn.eeepay.framework.service.impl;
/**体育彩票订单*/
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class SportLotteryRowImpl implements TranslateRow<LotteryOrder> {
	private final static Logger log = LoggerFactory.getLogger(SportLotteryRowImpl.class);

    @Resource
    private LotteryOrderDao lotteryOrderDao;
    @Resource
    private SuperBankUserInfoDao userInfoDao;
    
    private DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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
    	
    	/**valList存放解析的值*/
    	Map<Integer,String> lottery = new HashMap<Integer,String>();
    	
    	for(int i = 0;i <= temList.size();i++){
    		String cellValue = null;
    		if(row.getCell(i) != null){
    	         if(i==6){
    	        	 try{
    	        		 row.getCell(i).setCellType(Cell.CELL_TYPE_NUMERIC);
    	        		 Date buyTime = row.getCell(i).getDateCellValue();
    	        		 cellValue = formatter.format(buyTime);
    	        	 }catch(Exception e){
    	        		 cellValue = "";
    	        		 log.info("解析日期异常,行 " + index + " 列 "+ (i+1));
    	        	 }
    	         }else{
    	        	 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
       	          	 cellValue = row.getCell(i).getStringCellValue();
    	         }
    	     }
    		
			lottery.put(i, cellValue);
		}
		boolean betTimeValid = false;
		LotteryOrder record = new LotteryOrder();
		
		//用户编号
		if(lottery.get(1) == null){
			record.setOutUserId("");
		}else{
			record.setOutUserId(lottery.get(1));
		}
		//商户编号
		if(lottery.get(2) == null){
			record.setMenchantNo("");
		}else{
			record.setMenchantNo(lottery.get(2));
		}
		//商户名称
		if(lottery.get(3) == null){
			record.setMenchantName("");
		}else{
			record.setMenchantName(lottery.get(3));
		}
		//本家用户编号
		record.setUserCode(lottery.get(4));
		//订单编号
		record.setOutOrderNo(lottery.get(5));
		//购买时间
		if(lottery.get(6) != null && !"".equals(lottery.get(6)) && isValidDate(lottery.get(6))){
			record.setBetTime(lottery.get(6).replace("/", "-"));
		}else{
			betTimeValid = true; //非法时间
			log.info("购买时间非法'"+lottery.get(6)+"'");
		}
		//投注金额
		if(lottery.get(7) != null && !"".equals(lottery.get(7))){
			String consumee = (String)lottery.get(7);
			if(consumee.matches("^\\d+$")){
				record.setConsumeE(Integer.valueOf((String)lottery.get(7)) * 100);//转换成消费E豆
			}else{
				record.setConsumeE(0);
			}
		}
		//中奖金额
		record.setAwardAmount(lottery.get(8));
		
		//订单状态
		record.setBuyStatus(lottery.get(9));
		
		//中奖状态
		record.setRedeemFlag(lottery.get(10));
		
		record.setSportLottery("2");
		
		Integer userCount = null;

		
		if(record != null  && (record.getConsumeE() ==null || record.getConsumeE() == 0)){
			log.info("WARNNING:投注金额为0,订单状态不是出票成功" + record + "~~~~");
			errors.add(new ExcelErrorMsgBean(index, 0, "1"));
			return null;

		}

		if(record != null  && !"出票成功".equals(record.getBuyStatus())){
			log.info("WARNNING:该订单状态不是出票成功" + record + "~~~~");
			errors.add(new ExcelErrorMsgBean(index, 0, "1"));
			return null;
		}
		if(record != null && record.getUserCode() != null && !"".equals(record.getUserCode())){
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
        if(betTimeValid){
        	log.info("WARNNING:非法的时间格式" + record + "~~~~");
            errors.add(new ExcelErrorMsgBean(index, 0, "4"));
            return null;
        }

	    String orderNo = getOrderNo(record.getUserId()); //生成OrderNo
	    record.setOrderNo(orderNo);
	    record.setAwardRequire("成功出票");
	    record.setMenchantNo(record.getMenchantNo());
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
	public String getOrderNo(String memberId){
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
    public boolean isValidDate(String str) {  
        //DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try{  
            Date date = (Date)formatter.parse(str);  
            return str.equals(formatter.format(date));  
        }catch(Exception e){  
            return false;  
        }  
    }  
    
    /**excel模板*/
    private Map<Integer,String> getRecordTemplate(){
    	Map<Integer,String> map = new HashMap<Integer,String>();
    	map.put(1, "用户编号");
    	map.put(2, "商户编号");
    	map.put(3, "商户名称");
    	map.put(4, "商家用户编号");
    	map.put(5, "订单编号");
    	map.put(6, "购买时间");
    	map.put(7, "投注金额");
    	map.put(8, "中奖金额");
    	map.put(9, "订单状态");
    	map.put(10, "中奖状态");
    	return map;
    }
    
    private boolean checkExists(LotteryOrder record) {
        if(record == null){
            return  false;
        }
        int num = lotteryOrderDao.checkSportLotteryExists(record);
        if(num > 0){
            return true;
        }
        return false;
    }

}
