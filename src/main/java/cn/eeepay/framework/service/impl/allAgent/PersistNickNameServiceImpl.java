package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.PersistNickNameDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.PersistNickName;
import cn.eeepay.framework.service.allAgent.PersistNickNameService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("persistNickNameService")
public class PersistNickNameServiceImpl implements PersistNickNameService {
    private static final Logger log = LoggerFactory.getLogger(PersistNickNameServiceImpl.class);

    @Resource
    private PersistNickNameDao persistNickNameDao;

    public List<PersistNickName> selectPersistNickNameList(PersistNickName info, Page<PersistNickName> page){
        return persistNickNameDao.selectPersistNickNameList(info,page);
    }

    public int insertPersistNickName(PersistNickName info){
        int num=0;
        info.setKeyWord(info.getKeyWord().replace("，",","));
        String str[]=info.getKeyWord().split(",");
        for(int i=0;i<str.length;i++){
            if(StringUtil.isNotBlank(str[i])){
                PersistNickName persistNickName=new PersistNickName();
                persistNickName.setKeyWord(str[i]);
                List<PersistNickName> list=persistNickNameDao.selectPersistNickNameByKey(persistNickName.getKeyWord());
                if(list==null||list.size()==0){
                    num=persistNickNameDao.insertPersistNickName(persistNickName);
                }
            }
        }
        return num;
    }

    public int insertPersistNickNameAll(PersistNickName info){
        int num=0;
        List<PersistNickName> list=persistNickNameDao.selectPersistNickNameByKey(info.getKeyWord());
        if(list==null||list.size()==0){
            num=persistNickNameDao.insertPersistNickName(info);
        }
        return num;
    }

    public PersistNickName selectPersistNickNameById(Integer id){
        return persistNickNameDao.selectPersistNickNameById(id);
    }

    public int updatePersistNickName(PersistNickName info){
        return persistNickNameDao.updatePersistNickName(info);
    }

    public int deletePersistNickName(Integer id){
        return persistNickNameDao.deletePersistNickName(id);
    }

    @Override
    public void exportPersistNickName(PersistNickName info, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream ouputStream = null;

        Page<PersistNickName> page = new Page<>();
        page.setPageSize(Integer.MAX_VALUE);
        List<PersistNickName> list = persistNickNameDao.selectPersistNickNameList(info,page);
        String fileName = "保留昵称"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> map = null;

        for(PersistNickName item: list){
            map = new HashMap<>();
            map.put("keyWord", item.getKeyWord());
            map.put("createTime", item.getCreateTime()==null?"":sdfTime.format(item.getCreateTime()));
            data.add(map);
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"keyWord","createTime"};
        String[] colsName = new String[]{"保留昵称","添加日期"};
        try {
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出保留昵称失败,param:{}",JSONObject.toJSONString(info));
            e.printStackTrace();
        } finally {
            try {
                if(ouputStream!=null){
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
