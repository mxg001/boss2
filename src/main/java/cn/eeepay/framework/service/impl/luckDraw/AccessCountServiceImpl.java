package cn.eeepay.framework.service.impl.luckDraw;

import cn.eeepay.framework.dao.luckDraw.AccessCountDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.AccessCount;
import cn.eeepay.framework.service.luckDraw.AccessCountService;
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
 * @author liuks
 * 访问统计service实现类
 */
@Service("accessCountService")
public class AccessCountServiceImpl implements AccessCountService {

    private static final Logger log = LoggerFactory.getLogger(AccessCountServiceImpl.class);

    @Resource
    private AccessCountDao  accessCountDao;

    @Override
    public List<AccessCount> selectAllList(AccessCount acc, Page<AccessCount> page) {
        return accessCountDao.selectAllList(acc,page);
    }

    @Override
    public List<AccessCount> importDetailSelect(AccessCount acc) {
        return accessCountDao.importDetailSelect(acc);
    }

    @Override
    public void importDetail(List<AccessCount> list, HttpServletResponse response) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd") ;
        String fileName = "抽奖数据统计列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("reqDate", null);
            maps.put("reqSole",null);
            maps.put("reqCount",null);
            maps.put("reqPerson",null);
            maps.put("reqMer",null);
            data.add(maps);
        }else{
            for (AccessCount or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",or.getId()+"");
                maps.put("reqDate", or.getReqDate()==null?"":sdf1.format(or.getReqDate()));
                maps.put("reqSole",or.getReqSole()==null?"":or.getReqSole()+"");
                maps.put("reqCount",or.getReqCount()==null?"":or.getReqCount()+"");
                maps.put("reqPerson",or.getReqPerson()==null?"":or.getReqPerson()+"");
                maps.put("reqMer",or.getReqMer()==null?"":or.getReqMer()+"");
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","reqDate","reqSole","reqCount","reqPerson","reqMer"};
        String[] colsName = new String[]{"ID","时间","独立访问","访问量","参与人次","参与商户"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出抽奖数据统计列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}
