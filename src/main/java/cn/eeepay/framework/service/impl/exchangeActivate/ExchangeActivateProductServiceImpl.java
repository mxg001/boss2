package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateProductDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateProduct;
import cn.eeepay.framework.model.exchangeActivate.ProductTypeActivate;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateOemService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateProductService;
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
@Service("exchangeActivateProductService")
public class ExchangeActivateProductServiceImpl implements ExchangeActivateProductService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateProductServiceImpl.class);

    @Resource
    private ExchangeActivateProductDao exchangeActivateProductDao;

    @Resource
    private ExchangeActivateOemService exchangeActivateOemService;

    @Override
    public List<ExchangeActivateProduct> selectAllList(ExchangeActivateProduct pro, Page<ExchangeActivateProduct> page) {
        return exchangeActivateProductDao.selectAllList(pro,page);
    }

    @Override
    public List<ExchangeActivateProduct> importDetailSelect(ExchangeActivateProduct pro) {
        return exchangeActivateProductDao.importDetailSelect(pro);
    }

    @Override
    public int addExchangeProduct(ExchangeActivateProduct pro) {
        return exchangeActivateProductDao.addExchangeProduct(pro);
    }

    @Override
    public List<ExchangeActivateProduct> getExchangeProductList(String typeCode) {
        return exchangeActivateProductDao.getExchangeProductList(typeCode);
    }

    @Override
    public ExchangeActivateProduct getExchangeProduct(long id) {
        return exchangeActivateProductDao.getExchangeProduct(id);
    }

    @Override
    public int updateExchangeProduct(ExchangeActivateProduct pro) {
        return exchangeActivateProductDao.updateExchangeProduct(pro);
    }

    @Override
    public int deleteExchangeProduct(long id) {
        //先删除下架的产品
        exchangeActivateOemService.deleteProductOemShelve(id);
        //逻辑删除
        int num =exchangeActivateProductDao.deleteExchangeProduct(id);
        return num;
    }

    @Override
    public boolean checkTypeName(ExchangeActivateProduct pro) {
        if(pro.getId()==null){//新增
            List<ExchangeActivateProduct> oldList=exchangeActivateProductDao.checkproductName(pro);
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }else{
            List<ExchangeActivateProduct> oldList=exchangeActivateProductDao.checkproductNameId(pro);
            if(oldList!=null&&oldList.size()>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ExchangeActivateProduct> getProductList(String name,String typeCode) {
        return exchangeActivateProductDao.getProductList(name,typeCode);
    }

    @Override
    public List<ExchangeActivateProduct> productListSelect(String val) {
        return exchangeActivateProductDao.productListSelect(val);
    }

    @Override
    public void importDetail(List<ExchangeActivateProduct> list, HttpServletResponse response) throws Exception {

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
            for (ExchangeActivateProduct or : list) {
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
