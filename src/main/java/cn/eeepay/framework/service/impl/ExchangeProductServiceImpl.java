package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.ExchangeProductDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeProduct;
import cn.eeepay.framework.service.ExchangeOemService;
import cn.eeepay.framework.service.ExchangeProductService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 产品service实现类
 */
@Service("exchangeProductService")
public class ExchangeProductServiceImpl implements ExchangeProductService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeProductServiceImpl.class);

    @Resource
    private ExchangeProductDao exchangeProductDao;

    @Resource
    private ExchangeOemService exchangeOemService;

    @Override
    public List<ExchangeProduct> selectAllList(ExchangeProduct pro, Page<ExchangeProduct> page) {
        return exchangeProductDao.selectAllList(pro,page);
    }

    @Override
    public List<ExchangeProduct> importDetailSelect(ExchangeProduct pro) {
        return exchangeProductDao.importDetailSelect(pro);
    }

    @Override
    public int addExchangeProduct(ExchangeProduct pro) {
        return exchangeProductDao.addExchangeProduct(pro);
    }

    @Override
    public List<ExchangeProduct> getExchangeProductList(String typeCode) {
        return exchangeProductDao.getExchangeProductList(typeCode);
    }

    @Override
    public ExchangeProduct getExchangeProduct(long id) {
        return exchangeProductDao.getExchangeProduct(id);
    }

    @Override
    public int updateExchangeProduct(ExchangeProduct pro) {
        return exchangeProductDao.updateExchangeProduct(pro);
    }

    @Override
    public int deleteExchangeProduct(long id) {
        //先删除下架的产品
        exchangeOemService.deleteProductOemShelve(id);
        //逻辑删除
        int num =exchangeProductDao.deleteExchangeProduct(id);
        return num;
    }

    @Override
    public boolean checkTypeName(ExchangeProduct pro) {
        if(pro.getId()==null){//新增
            List<ExchangeProduct> oldList=exchangeProductDao.checkproductName(pro);
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }else{
            List<ExchangeProduct> oldList=exchangeProductDao.checkproductNameId(pro);
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ExchangeProduct> getProductList(String name,String typeCode) {
        return exchangeProductDao.getProductList(name,typeCode);
    }

    @Override
    public List<ExchangeProduct> productListSelect(String val) {
        return exchangeProductDao.productListSelect(val);
    }

    @Override
    public void importDetail(List<ExchangeProduct> list, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "积分产品列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("orgName",null);
            maps.put("typeName",null);
            maps.put("productName",null);
            maps.put("originalPrice",null);
            maps.put("excPoint",null);
            maps.put("excNum",null);
            maps.put("settleDay",null);
            maps.put("minDay",null);
            maps.put("excPrice",null);
            data.add(maps);
        }else{
            for (ExchangeProduct or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()+"");
                maps.put("orgName",or.getOrgName()==null?"":or.getOrgName());
                maps.put("typeName",or.getTypeName()==null?"":or.getTypeName());
                maps.put("productName",or.getProductName()==null?"":or.getProductName());
                maps.put("originalPrice",or.getOriginalPrice()==null?"":or.getOriginalPrice().toString());
                maps.put("excPoint",or.getExcPoint()==null?"":or.getExcPoint().toString());
                maps.put("excNum",or.getExcNum()==null?"":or.getExcNum().toString());
                maps.put("settleDay",or.getSettleDay()==null?"":or.getSettleDay().toString());
                maps.put("minDay",or.getMinDay()==null?"":or.getMinDay().toString());
                maps.put("excPrice",or.getExcPrice()==null?"":or.getExcPrice().toString());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","orgName","typeName","productName","originalPrice","excPoint",
                "excNum","settleDay","minDay","excPrice"
        };
        String[] colsName = new String[]{"序号","机构名称","产品类别","产品名称","券面价格","兑换积分",
                "兑换次数","结算周期(天)","最短有效时间(天)","兑换价格"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出积分产品列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}
