package cn.eeepay.framework.serviceImpl.sysUser;

import cn.eeepay.boss.action.exportManage.ExportManageAction;
import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.dao.sysUser.ExportManageDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExportManage;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.sysUser.ExportManageService;
import cn.eeepay.framework.serviceImpl.exportBigData.ExportBase;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("exportManageService")
public class ExportManageServiceImpl implements ExportManageService {

    private static final Logger log = LoggerFactory.getLogger(ExportManageServiceImpl.class);

    @Resource
    private ExportManageDao exportManageDao;
    @Resource
    private SysDictService sysDictService;


    @Override
    public boolean checkExportManageInfo(String md5Key,Map<String, Object> msg) {
        ExportManage exInfo=exportManageDao.checkExportManageInfo(md5Key);
        if(exInfo!=null){
            if("0".equals(exInfo.getStatus().toString())){
                msg.put("status", false);
                msg.put("msg", "该导出条件已在后台执行中,请稍后查询结果!");
                return true;
            }else if("1".equals(exInfo.getStatus().toString())||"2".equals(exInfo.getStatus().toString())){
                msg.put("status", false);
                msg.put("msg", "该导出条件已导出过!");
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ExportManage> selectAllList(ExportManage info, Page<ExportManage> page) {
        List<ExportManage> list=exportManageDao.selectAllList(info,page);
        return list;
    }

    @Override
    public int updateReadStatus(int id) {
        return exportManageDao.updateReadStatus(id);

    }

    @Override
    public ExportManage getExportManageInfoByID(int id) {
        return exportManageDao.getExportManageInfoByID(id);
    }

    @Override
    public void downloadFile(ExportManage info, HttpServletResponse response, Map<String, Object> msg) {
        OutputStream outStream =null;
        InputStream inStream=null;
        try {
            String fileUrl=info.getFileUrl();
            SysDict sysDict = sysDictService.getByKey(ExportBase.FILE_URL);
            if(fileUrl!=null&&!"".equals(fileUrl)&&sysDict!=null){
                String baseFolder=sysDict.getSysValue()+ File.separator+ ExportBase.FILENAME;
                String fileNameFormat = new String(info.getFileName().getBytes("UTF-8"),"ISO-8859-1");
                response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);

                inStream = new FileInputStream(new File(baseFolder+File.separator+fileUrl));
                outStream=response.getOutputStream();
                byte[] bs = new byte[1024]; //1K的数据缓冲
                int len;
                while ((len = inStream.read(bs)) != -1) {
                    outStream.write(bs, 0, len);
                }
                outStream.flush();
                //更新下载次数
                exportManageDao.updateDownloadNum(info.getId());
            }else{
                msg.put("status", false);
                msg.put("msg", "下载文档失败!");
            }
        }catch (Exception e){
            log.error("下载文档异常!",e);
            msg.put("status", false);
            msg.put("msg", "下载文档异常!");
        }finally {
            if(outStream!=null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inStream!=null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<ExportManage> getReadInfoList(String username) {
        return exportManageDao.getReadInfoList(username);
    }

    @Override
    public int deleteExportManage(int id) {
        return exportManageDao.deleteExportManage(id);
    }

    @Override
    public List<ExportManage> selectNeedDeleteList(Date date) {
        return exportManageDao.selectNeedDeleteList(date);
    }

}
