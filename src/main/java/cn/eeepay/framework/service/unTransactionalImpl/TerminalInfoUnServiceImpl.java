package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.TerminalInfoDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.importLog.ImportLog;
import cn.eeepay.framework.model.importLog.ImportLogEntry;
import cn.eeepay.framework.service.ActivityService;
import cn.eeepay.framework.service.HardwareProductService;
import cn.eeepay.framework.service.TerminalInfoUnService;
import cn.eeepay.framework.service.allAgent.RandomNumAllAgentService;
import cn.eeepay.framework.service.importLog.ImportLogService;
import cn.eeepay.framework.util.CellUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/11/16/016.
 * 机具导入
 */
@Service("terminalInfoUnServiceImpl")
public class TerminalInfoUnServiceImpl  implements TerminalInfoUnService {

    private static final Logger log = LoggerFactory.getLogger(TerminalInfoUnServiceImpl.class);

    @Resource
    private HardwareProductService hardwareProductService;

    @Resource
    private RandomNumAllAgentService randomNumAllAgentService;

    @Resource
    private ImportLogService importLogService;

    @Resource
    private TerminalInfoDao terminalInfoDao;
    @Resource
    private ActivityService activityService;

    private static final ExecutorService threadpool = Executors.newFixedThreadPool(5);


    @Override
    public Map<String, Object> importDiscount(MultipartFile file,HttpServletRequest request) throws Exception{
        log.info("机具导入------------------------------------");
        Map<String, Object> msg = new HashMap<>();
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        //读取第一个sheet
        final Sheet sheet = wb.getSheetAt(0);
        // 遍历所有单元格,读取单元格
        final int row_num = sheet.getLastRowNum();
        if(row_num<1){
            msg.put("status", false);
            msg.put("msg","导入的文件无数据!");
            return msg;
        }
        if(row_num>30000){
            msg.put("status", false);
            msg.put("msg","导入数据超过上限(30000条),请重新选择文件!");
            return msg;
        }
        final String batchNo=randomNumAllAgentService.getRandomNumberByData("DRU","nposp");
        request.getSession().setAttribute("batchNoTerminal",batchNo);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        threadpool.execute(
                new Runnable(){
                    @Override
                    public void run() {
                        log.info("机具导入线程开始------------------------------------");
                        List<TerminalInfo> list=new ArrayList<TerminalInfo>();//执行更新的
                        List<String> snList=new ArrayList<String>();//sn是否重复验证的
                        List<ImportLogEntry> logList=new ArrayList<ImportLogEntry>();//处理详情

                        for (int i = 1; i <= row_num; i++) {
                            Row row = sheet.getRow(i);
                            String sn1 = CellUtil.getCellValue(row.getCell(0));//机具SN号
                            String type = CellUtil.getCellValue(row.getCell(1));//硬件产品
                            String activityTypeNo = CellUtil.getCellValue(row.getCell(2));//硬件产品
                            String activityTypeName = CellUtil.getCellValue(row.getCell(3));//硬件产品

                            ImportLogEntry entry=new ImportLogEntry();
                            entry.setData1(sn1);
                            entry.setData2(type);
                            entry.setBatchNo(batchNo);

                            logList.add(entry);

                            if(sn1==null||"".equals(sn1)){
                                entry.setResult("机具SN号为空!");
                                continue;
                            }
                            String sn=sn1.split("\\.")[0];
                            entry.setData1(sn);

                            TerminalInfo info=terminalInfoDao.getTerminalInfoBySn(sn);
                            if(info==null){
                                entry.setResult("机具SN号不存在!");
                                continue;
                            }
                            if(snList.contains(sn)){
                                entry.setResult("机具SN号重复!");
                                continue;
                            }else{
                                snList.add(sn);
                            }
                            if(!"0".equals(info.getOpenStatus())&&!"1".equals(info.getOpenStatus())){
                                entry.setResult("该机具状态不符合!");
                                continue;
                            }

                            if(type==null||"".equals(type)){
                                entry.setResult("硬件产品类型为空!");
                                continue;
                            }
                            HardwareProduct hp=hardwareProductService.getHardwareProductByBpName(type);
                            if(hp==null){
                                entry.setResult("机具硬件产品不存在!");
                                continue;
                            }
                            Long typeId=hp.getHpId();
                            if(info.getType()!=null&&info.getType().equals(typeId.toString())){
                                entry.setResult("硬件产品与修改前的一致,无需修改!");
                                continue;
                            }
                            //验证欢乐返子类型
                            HlfHardware hlfHardware = new HlfHardware();
                            hlfHardware.setHardId(Long.valueOf(typeId));
                            hlfHardware.setActivityTypeNo(activityTypeNo);
                            HlfHardware hlf = null;
                            if(info.getActivityType().indexOf("7")>=0){
                                hlfHardware.setActivityCode("009");
                                hlf=activityService.selectHlfHardwareByHardId(hlfHardware);
                                if(hlf==null){
                                    entry.setResult("该硬件产品还没有欢乐返子类型，请先新建后重试");
                                    continue;
                                }
                                if(!hlf.getActivityTypeName().equals(activityTypeName)){
                                    entry.setResult("欢乐返子类型编号与名称不一致");
                                    continue;
                                }
                            }else if(info.getActivityType().indexOf("8")>=0){
                                hlfHardware.setActivityCode("008");
                                hlf=activityService.selectHlfHardwareByHardId(hlfHardware);
                                if(hlf==null){
                                    entry.setResult("该硬件产品还没有欢乐返子类型，请先新建后重试");
                                    continue;
                                }
                                if(!hlf.getActivityTypeName().equals(activityTypeName)){
                                    entry.setResult("欢乐返子类型编号与名称不一致");
                                    continue;
                                }
                            }else{
                                activityTypeNo="";
                            }

                            TerminalInfo addInfo=new TerminalInfo();
                            addInfo.setSn(sn);
                            addInfo.setType(typeId+"");
                            addInfo.setActivityTypeNo(activityTypeNo);
                            list.add(addInfo);
                            entry.setResult("成功!");
                        }

                        ImportLog log=new ImportLog();
                        log.setBatchNo(batchNo);
                        log.setOperator(principal.getUsername());
                        log.setMsg("处理批次号:"+batchNo+",导入"+row_num+"条,成功"+list.size()+"条,失败"+(row_num-list.size())+"条!");
                        log.setLogSource("1");

                        int num=importLogService.insertLog(log);
                        if(num>0){
                            if(logList!=null&&logList.size()>0){
                                importLogService.insertLogEntry(logList);
                            }
                            if(list!=null&&list.size()>0){
                                for(TerminalInfo addInfo:list){
                                    terminalInfoDao.updateTerminalType(addInfo);
                                }
                            }
                            importLogService.updateLog(batchNo);
                        }
                    }
                }
        );
        msg.put("status", true);
        msg.put("msg","导入数据中,请稍后查询结果!");
        return msg;
    }


    @Override
    public ImportLog getImportResult(String batchNo) {
        ImportLog log=importLogService.getImportLog(batchNo);
        if(log!=null&&"1".equals(log.getStatus())){
            return log;
        }
        return null;
    }


}
