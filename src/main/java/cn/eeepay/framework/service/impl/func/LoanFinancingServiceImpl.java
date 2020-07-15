package cn.eeepay.framework.service.impl.func;

import cn.eeepay.framework.dao.func.LoanFinancingDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.func.LoanFinancing;
import cn.eeepay.framework.service.func.LoanFinancingService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("loanFinancingService")
public class LoanFinancingServiceImpl implements LoanFinancingService {

    private static final Logger log = LoggerFactory.getLogger(LoanFinancingServiceImpl.class);

    @Resource
    private LoanFinancingDao loanFinancingDao;

    @Override
    public List<LoanFinancing> selectAllList(LoanFinancing info, Page<LoanFinancing> page) {
        List<LoanFinancing> list=loanFinancingDao.selectAllList(info,page);
        if(page.getResult()!=null&&page.getResult().size()>0){
            for(LoanFinancing item:page.getResult()){
                item.setLogImg(CommonUtil.getImgUrlAgent(item.getLogImg()));
            }
        }
        return list;
    }

    @Override
    public int addLoanFinancing(LoanFinancing info) {
        // 获取当前登录的人
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        return loanFinancingDao.addLoanFinancing(info);
    }

    @Override
    public int updateLoanFinancing(LoanFinancing info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        return loanFinancingDao.updateLoanFinancing(info);
    }

    @Override
    public int updateLoanFinancingStatus(int id, int status) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loanFinancingDao.updateLoanFinancingStatus(id,status,principal.getUsername());
    }

    @Override
    public LoanFinancing getLoanFinancing(int id) {
        LoanFinancing info=loanFinancingDao.getLoanFinancing(id);
        if(info!=null){
            info.setLogImg(CommonUtil.getImgUrlAgent(info.getLogImg()));
        }
        return info;
    }

    @Override
    public void importDetail(LoanFinancing info, HttpServletResponse response) throws Exception{
        List<LoanFinancing> list=loanFinancingDao.importList(info);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "贷款理财配置列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("productName",null);
            maps.put("sortNo",null);
            maps.put("status",null);
            maps.put("clickTimes",null);
            maps.put("createTime",null);
            maps.put("lastUpdateTime",null);
            maps.put("operator",null);
            data.add(maps);
        }else{
            Map<String, String> statusMap=new HashMap<String, String>();//上下架状态
            statusMap.put("0","下架");
            statusMap.put("1","上架");

            for (LoanFinancing item : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",item.getId()==null?"":item.getId().toString());
                maps.put("productName",item.getProductName());
                maps.put("sortNo",item.getSortNo()==null?"":item.getSortNo().toString());
                maps.put("status",statusMap.get(item.getStatus()+""));
                maps.put("clickTimes",item.getClickTimes()==null?"":item.getClickTimes().toString());
                maps.put("createTime", item.getCreateTime()==null?"":sdf1.format(item.getCreateTime()));
                maps.put("lastUpdateTime", item.getLastUpdateTime()==null?"":sdf1.format(item.getLastUpdateTime()));
                maps.put("operator",item.getOperator());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","productName","sortNo","status","clickTimes","createTime",
                "lastUpdateTime","operator"
        };
        String[] colsName = new String[]{"产品编号","产品名称","排序","上下架状态","点击次数","创建时间",
                "最后操作时间","操作人"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出贷款理财配置列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}
