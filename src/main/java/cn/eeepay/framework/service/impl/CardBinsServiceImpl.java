package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CardBinsDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.CardBinsService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.CellUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/3/23/023.
 * @author  liuks
 * 卡bin service实现类
 */
@Service("cardBinsService")
@Transactional
public class CardBinsServiceImpl implements CardBinsService {

    private static final Logger log = LoggerFactory.getLogger(CardBinsServiceImpl.class);

    @Resource
    private CardBinsDao cardBinsDao;

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<CardBins> selectAllList(CardBins card, Page<CardBins> page) {
        return cardBinsDao.selectAllList(card,page);
    }

    @Override
    public int insertCardBins(CardBins card) {
        return cardBinsDao.insertCardBins(card);
    }

    @Override
    public int updateCardBins(CardBins card) {
        return cardBinsDao.updateCardBins(card);
    }

    @Override
    public CardBins getCardBins(int id) {
        return cardBinsDao.getCardBins(id);
    }

    @Override
    public int deleteCardBins(int id) {
        return cardBinsDao.deleteCardBins(id);
    }

    @Override
    public CardBins getCardBinsByCardNo(String cardNo,Integer businessType,Integer cardDigit) {
        return cardBinsDao.getCardBinsByCardNo(cardNo,businessType,cardDigit);
    }

    @Override
    public List<CardBins> exportInfo(CardBins card) {
        return cardBinsDao.exportInfo(card);
    }

    @Override
    public Map<String, Object> importCardBins(MultipartFile file,Integer businessType) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 遍历所有单元格，读取单元格
        int row_num = sheet.getLastRowNum();
        List<CardBins> list=new ArrayList<CardBins>();
        Map<String, Integer> stateMap=new HashMap<String, Integer>();
        stateMap.put("关闭",0);
        stateMap.put("打开",1);
        Map<String, Integer> cardTypeMap=new HashMap<String, Integer>();
        cardTypeMap.put("其他",0);
        cardTypeMap.put("贷记卡",1);
        cardTypeMap.put("借记卡",2);
        cardTypeMap.put("借贷合一卡",3);
        Map<String, String> currencyMap=sysDictService.selectMapByKey("cardBinsCurrency");
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> cardNoList=new ArrayList<String>();

        for (int i = 1; i <= row_num; i++) {
            Row row = sheet.getRow(i);
            String cardNo = CellUtil.getCellValue(row.getCell(0));//卡bin
            String cardDigit = CellUtil.getCellValue(row.getCell(1));//全卡号位数
            String state = CellUtil.getCellValue(row.getCell(2));//状态:0关闭,1打开
            String cardType = CellUtil.getCellValue(row.getCell(3));//卡种:0其他,1贷记卡,2借记卡
            String cardBank = CellUtil.getCellValue(row.getCell(4));//发卡行
            String cardStyle = CellUtil.getCellValue(row.getCell(5));//卡类型
            String currency = CellUtil.getCellValue(row.getCell(6));//交易币种:取数据字典值
            String remarks = CellUtil.getCellValue(row.getCell(7));//备注

            if(cardNo==null||"".equals(cardNo)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,卡bin为空!");
                return msg;
            }
            if(businessType!=1) {
                if (cardDigit == null || "".equals(cardDigit)) {
                    msg.put("status", false);
                    msg.put("msg", "导入失败,第" + (i + 1) + "行,全卡号位数为空!");
                    return msg;
                }
            }
            CardBins oldCardBins=getCardBinsByCardNo(cardNo,businessType,cardDigit == null?null:Integer.parseInt(cardDigit));
            if(oldCardBins!=null){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,卡bin已存在!");
                return msg;
            }
            if(cardNoList.contains(cardNo)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,卡bin "+cardNo+"重复!");
                return msg;
            }else{
                cardNoList.add(cardNo);
            }
            if(state==null||"".equals(state)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,状态为空!");
                return msg;
            }
            int stateInt=-1;
            if(stateMap.containsKey(state)){
                stateInt=stateMap.get(state);
            }else{
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,状态数据异常!");
                return msg;
            }

            if(cardType==null||"".equals(cardType)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,卡种为空!");
                return msg;
            }
            int cardTypeInt=-1;
            if(cardTypeMap.containsKey(cardType)){
                cardTypeInt=cardTypeMap.get(cardType);
            }else{
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,卡种数据异常!");
                return msg;
            }
            if(businessType!=2){
                if(cardBank==null||"".equals(cardBank)){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,发卡行为空!");
                    return msg;
                }
            }
            String currencyStr=null;
            if("其他".equals(currency)){
                currencyStr="0";
            }else{
                for(Map.Entry<String,String > entry:currencyMap.entrySet()){
                    if(entry.getValue().equals(currency)){
                        currencyStr=entry.getKey();
                        break;
                    }
                }
            }
            CardBins cardBins=new CardBins();
            cardBins.setBusinessType(businessType);
            cardBins.setCardNo(cardNo);
            cardBins.setState(stateInt);
            cardBins.setCardType(cardTypeInt);
            cardBins.setCardBank(cardBank);
            cardBins.setCardStyle(cardStyle);
            cardBins.setCurrency(currencyStr==null?null:Integer.valueOf(currencyStr));
            cardBins.setRemarks(remarks);
            cardBins.setCreateId(principal.getId());
            cardBins.setCreateName(principal.getUsername());
            cardBins.setCardNum(cardNo.length());
            cardBins.setCardDigit(cardDigit==null?null:Integer.parseInt(cardDigit));
            list.add(cardBins);
        }
        if(list.size()>0){
            for(CardBins cb:list){
                insertCardBins(cb);
            }
            msg.put("status", true);
            msg.put("msg", "导入成功!");
        }else{
            msg.put("status", false);
            msg.put("msg", "导入失败!");
        }
        return msg;
    }

    @Override
    public void exportCardBins(List<CardBins> list,HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "卡bin管理"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String, String> businessTypeMap=new HashMap<String, String>();
        businessTypeMap.put("1","境外卡");
        businessTypeMap.put("2","借贷合一卡bin白名单");
        Map<String, String> cardTypeMap=new HashMap<String, String>();
        cardTypeMap.put("0","其他");
        cardTypeMap.put("1","贷记卡");
        cardTypeMap.put("2","借记卡");
        cardTypeMap.put("3","借贷合一卡");
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("businessType",null);
            maps.put("cardNo",null);
            maps.put("cardDigit",null);
            maps.put("state",null);
            maps.put("cardType",null);
            maps.put("cardBank",null);
            maps.put("cardStyle",null);
            maps.put("currency",null);
            maps.put("remarks",null);
            maps.put("createTime",null);
            maps.put("createName",null);
            data.add(maps);
        }else{
            Map<String, String> cardBinsCurrencyMap=sysDictService.selectMapByKey("cardBinsCurrency");
            for (CardBins or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()==null?"":or.getId().toString());
                maps.put("businessType", StringUtils.trimToEmpty(businessTypeMap.get(or.getBusinessType()+"")));
                maps.put("cardNo",or.getCardNo()==null?"":or.getCardNo());
                maps.put("cardDigit",or.getCardDigit()==null?"":or.getCardDigit()+"");
                if(or.getState()!=null){
                    if(or.getState()==0){
                        maps.put("state","关闭");
                    }else if(or.getState()==1){
                        maps.put("state","打开");
                    }else{
                        maps.put("state",null);
                    }
                }else{
                    maps.put("state",null);
                }
                maps.put("cardType", StringUtils.trimToEmpty(cardTypeMap.get(or.getCardType()+"")));
                maps.put("cardBank",or.getCardBank()==null?"":or.getCardBank());
                maps.put("cardStyle",or.getCardBank()==null?"":or.getCardBank());
                if(or.getCurrency()!=null){
                    if(or.getCurrency()==0){
                        maps.put("currency","其他");
                    }else{
                        maps.put("currency",cardBinsCurrencyMap.get(or.getCurrency().toString()));
                    }
                }else{
                    maps.put("currency",null);
                }
                maps.put("remarks",or.getRemarks()==null?"":or.getRemarks());
                maps.put("createTime",or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("createName",or.getCreateName()==null?"":or.getCreateName());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","businessType","cardNo","cardDigit","state","cardType","cardBank","cardStyle",
                "currency","remarks","createTime","createName"
        };

        String[] colsName = new String[]{"编号","业务类型","卡bin/全卡号","全卡号位数","状态","卡种","发卡行","卡类型",
                "交易币种","备注","创建时间","创建人"
        };

        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出卡bin管理列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public int openStateCardBins(int id, int state) {
        return cardBinsDao.openStateCardBins(id,state);
    }
}
