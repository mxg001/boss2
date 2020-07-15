package cn.eeepay.framework.service.impl.luckDraw;

import cn.eeepay.framework.dao.luckDraw.LuckDrawOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.LuckDrawEntry;
import cn.eeepay.framework.model.luckDraw.LuckDrawOrder;
import cn.eeepay.framework.service.luckDraw.LuckDrawOrderService;
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
 * Created by Administrator on 2018/11/7/007.
 * @author  liuks
 * 抽奖信息service实现类
 */
@Service("luckDrawOrderService")
public class LuckDrawOrderServiceImpl  implements LuckDrawOrderService {

    private static final Logger log = LoggerFactory.getLogger(LuckDrawOrderServiceImpl.class);

    @Resource
    private LuckDrawOrderDao luckDrawOrderDao;

    @Override
    public List<LuckDrawOrder> selectAllList(LuckDrawOrder order, Page<LuckDrawOrder> page) {
        List<LuckDrawOrder> list=luckDrawOrderDao.selectAllList(order,page);
        shieldedInformationList(page.getResult());
        return list;
    }

    //过滤敏感信息
    private void shieldedInformationList(List<LuckDrawOrder> list){
        if(list!=null&&list.size()>0){
            for(LuckDrawOrder item:list){
                shieldedInformation(item);
            }
        }
    }
    private void shieldedInformation(LuckDrawOrder order){
        if(order!=null&&order.getMobilephone()!=null){
            order.setMobilephone(order.getMobilephone().substring(0,3)+"****"+order.getMobilephone().substring(7,order.getMobilephone().length()));
        }
    }

    @Override
    public List<LuckDrawOrder> importDetailSelect(LuckDrawOrder order) {
        List<LuckDrawOrder> list=luckDrawOrderDao.importDetailSelect(order);
        shieldedInformationList(list);
        return list;
    }

    @Override
    public void importDetail(List<LuckDrawOrder> list, HttpServletResponse response) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "抽奖信息管理列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("seq",null);
            maps.put("awardDesc",null);
            maps.put("awardName",null);
            maps.put("activityName",null);
            maps.put("status",null);
            maps.put("mobilephone",null);
            maps.put("userCode",null);
            maps.put("userName",null);
            maps.put("playTime", null);
            data.add(maps);
        }else{
            Map<String, String> statusMap=new HashMap<String, String>();
            statusMap.put("1","未中奖");
            statusMap.put("2","已中奖");
            statusMap.put("3","已发奖");

            for (LuckDrawOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()+"");
                maps.put("seq",or.getSeq()==null?"":or.getSeq());
                maps.put("awardDesc",or.getAwardDesc()==null?"":or.getAwardDesc());
                maps.put("awardName",or.getAwardName()==null?"":or.getAwardName());
                maps.put("activityName",or.getActivityName()==null?"":or.getActivityName());
                maps.put("status",statusMap.get(or.getStatus()+""));
                maps.put("mobilephone",or.getMobilephone()==null?"":or.getMobilephone());
                maps.put("userCode",or.getUserCode()==null?"":or.getUserCode());
                maps.put("userName",or.getUserName()==null?"":or.getUserName());
                maps.put("playTime", or.getPlayTime()==null?"":sdf1.format(or.getPlayTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","seq","awardDesc","awardName","activityName","status","mobilephone",
                "userCode","userName","playTime"

        };
        String[] colsName = new String[]{"ID","奖品码","奖项说明","奖品名称","活动名称","状态","手机号",
                "用户ID","用户名称","抽奖时间"

        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出抽奖信息管理列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public Integer sumAwardsConfigId(LuckDrawOrder order) {
        return luckDrawOrderDao.sumAwardsConfigId(order);
    }

    @Override
    public LuckDrawOrder getLuckDrawOrder(int id) {
        LuckDrawOrder order=luckDrawOrderDao.getLuckDrawOrder(id);
        if(order!=null){
            shieldedInformation(order);
            List<LuckDrawEntry> list=luckDrawOrderDao.getLuckDrawEntry(id);
            if(list!=null&&list.size()>0){
                order.setEntryList(list);
            }
        }
        return order;
    }
}
