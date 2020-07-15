package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExcActRouteGoodDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActRoute;
import cn.eeepay.framework.model.exchangeActivate.ExcActRouteGood;
import cn.eeepay.framework.service.exchangeActivate.ExcActRouteGoodService;
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
 * Created by Administrator on 2018/9/30/030.
 */
@Service("excActRouteGoodService")
public class ExcActRouteGoodServiceImpl implements ExcActRouteGoodService {

    private static final Logger log = LoggerFactory.getLogger(ExcActRouteGoodServiceImpl.class);


    @Resource
    private ExcActRouteGoodDao excActRouteGoodDao;

    @Override
    public int addExcActRouteGood(ExcActRouteGood good) {
        return excActRouteGoodDao.addExcActRouteGood(good);
    }

    @Override
    public boolean checkRoute(ExcActRouteGood good) {
        List<ExcActRouteGood> list=null;
        if(good.getId()==null){
            list=excActRouteGoodDao.getGoodList(good);
        }else{
            list=excActRouteGoodDao.getGoodListById(good);
        }
        if(list!=null&&list.size()>0){
            return false;
        }
        return true;
    }

    @Override
    public List<ExcActRouteGood> selectAllList(ExcActRouteGood good, Page<ExcActRouteGood> page) {
        return excActRouteGoodDao.selectAllList(good,page);
    }

    @Override
    public ExcActRouteGood getRouteGood(int id) {
        return excActRouteGoodDao.getRouteGood(id);
    }

    @Override
    public int updateExcActRouteGood(ExcActRouteGood good) {
        return excActRouteGoodDao.updateExcActRouteGood(good);
    }

    @Override
    public int closeGood(int id, String state) {
        return excActRouteGoodDao.closeGood(id,state);
    }

    @Override
    public int closeGoodBatch(String ids, String state, Map<String, Object> msg) {
        if(ids!=null&&!"".equals(ids)){
            String[] strs=ids.split(",");
            if(strs!=null&&strs.length>0){
                int length=strs.length;
                int num=0;
                for(String id:strs){
                    excActRouteGoodDao.closeGood(Integer.parseInt(id),state);
                    num++;
                }
                msg.put("status", true);
                msg.put("msg", "批量操作成功,成功操作"+num+"条!");
                return 1;
            }
        }
        msg.put("status", false);
        msg.put("msg", "批量操作失败!");
        return 0;
    }

    @Override
    public int deleteGood(int id) {
        return excActRouteGoodDao.deleteGood(id);
    }

    @Override
    public int checkRouteGood(ExcActRouteGood good) {
        ExcActRouteGood checkRouteGood=excActRouteGoodDao.getCheckRouteGood(good);
        if(checkRouteGood!=null){
            return excActRouteGoodDao.updateCheckRouteGood(checkRouteGood);
        }
        return 0;
    }

    @Override
    public List<ExcActRouteGood> importDetailSelect(ExcActRouteGood good) {
        return excActRouteGoodDao.importDetailSelect(good);
    }

    @Override
    public void importDetail(List<ExcActRouteGood> list, HttpServletResponse response) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "核销渠道商品列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("channelNo",null);
            maps.put("channelName",null);
            maps.put("goodTypeNo",null);
            maps.put("channelGoodNo",null);
            maps.put("channelGoodName",null);
            maps.put("goodContentId",null);
            maps.put("pId",null);
            maps.put("pName",null);
            maps.put("status",null);
            maps.put("goodMode",null);
            maps.put("channelPrice",null);
            maps.put("createTime",null);
            data.add(maps);
        }else{
            Map<String, String> statusMap=new HashMap<String, String>();
            statusMap.put("0","关闭");
            statusMap.put("1","开启");

            Map<String, String> goodModeMap=new HashMap<String, String>();
            goodModeMap.put("1","文字报单");
            goodModeMap.put("2","图片报单");

            for (ExcActRouteGood or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()+"");
                maps.put("channelNo",or.getChannelNo()==null?"":or.getChannelNo());
                maps.put("channelName",or.getChannelName()==null?"":or.getChannelName());
                maps.put("goodTypeNo",or.getGoodTypeNo()==null?"":or.getGoodTypeNo());
                maps.put("channelGoodNo",or.getChannelGoodNo()==null?"":or.getChannelGoodNo());
                maps.put("channelGoodName",or.getChannelGoodName()==null?"":or.getChannelGoodName());
                maps.put("goodContentId",or.getGoodContentId()==null?"":or.getGoodContentId());
                maps.put("pId",or.getpId()==null?"":or.getpId()+"");
                maps.put("pName",or.getpName()==null?"":or.getpName());
                maps.put("status",statusMap.get(or.getStatus().toString()));
                maps.put("goodMode",goodModeMap.get(or.getGoodMode()));
                maps.put("channelPrice",or.getChannelPrice()==null?"":or.getChannelPrice().toString());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","channelNo","channelName","goodTypeNo","channelGoodNo",
                "channelGoodName","goodContentId","pId","pName","status","goodMode","channelPrice","createTime"
        };
        String[] colsName = new String[]{"ID","核销渠道编号","核销渠道名称","上游渠道ID","渠道商品编号",
                "渠道商品名称","商品内容ID","产品ID","产品名称","状态","核销方式","核销价格","创建时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出核销渠道商品列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}
