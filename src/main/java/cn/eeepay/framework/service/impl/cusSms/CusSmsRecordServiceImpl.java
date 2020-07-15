package cn.eeepay.framework.service.impl.cusSms;

import cn.eeepay.framework.dao.cusSms.CusSmsRecordDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cusSms.CusSmsRecord;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.cusSms.CusSmsRecordService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("cusSmsRecordService")
public class CusSmsRecordServiceImpl implements CusSmsRecordService {

    private static final Logger log = LoggerFactory.getLogger(CusSmsRecordServiceImpl.class);

    @Resource
    private CusSmsRecordDao cusSmsRecordDao;
    @Resource
    private SysDictService sysDictService;

    @Override
    public List<CusSmsRecord> selectAllList(CusSmsRecord info, Page<CusSmsRecord> page) {
        List<CusSmsRecord> list=cusSmsRecordDao.selectAllList(info,page);
        if(page.getResult()!=null&&page.getResult().size()>0){
            for(CusSmsRecord item:page.getResult()){
                // 敏感信息屏蔽
                item.setMobilePhone(StringUtil.sensitiveInformationHandle(item.getMobilePhone(), 0));
            }
        }
        return list;
    }

    @Override
    public CusSmsRecord getCusSmsRecordInfo(int id, int sta) {
        CusSmsRecord info= cusSmsRecordDao.getCusSmsRecordInfo(id);
        if(1==sta) {
            // 敏感信息屏蔽
            info.setMobilePhone(StringUtil.sensitiveInformationHandle(info.getMobilePhone(), 0));
        }
        return info;
    }

    @Override
    public void exportInfo(CusSmsRecord info, HttpServletResponse response) throws Exception{
        List<CusSmsRecord> list=cusSmsRecordDao.importList(info);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "短信发送记录列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("templateId",null);
            maps.put("mobilePhone",null);
            maps.put("code",null);
            maps.put("merchantNo",null);
            maps.put("type",null);
            maps.put("operator",null);
            maps.put("createTime",null);
            maps.put("content",null);
            data.add(maps);
        }else{
            Map<String, String> typeMap=sysDictService.selectMapByKey("SMS_BUSINESS_TYPE");//业务类型

            for (CusSmsRecord item : list) {
                // 敏感信息屏蔽
                info.setMobilePhone(StringUtil.sensitiveInformationHandle(info.getMobilePhone(), 0));

                Map<String, String> maps = new HashMap<String, String>();
                maps.put("templateId",item.getTemplateId()==null?null:item.getTemplateId().toString());
                maps.put("mobilePhone",item.getMobilePhone()==null?"":item.getMobilePhone());
                maps.put("code",item.getCode()==null?"":item.getCode());
                maps.put("merchantNo",item.getMerchantNo()==null?"":item.getMerchantNo());
                maps.put("type",typeMap.get(item.getType()));
                maps.put("operator",item.getOperator()==null?"":item.getOperator());
                maps.put("createTime", item.getCreateTime()==null?"":sdf1.format(item.getCreateTime()));
                maps.put("content",item.getContent()==null?"":item.getContent());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"templateId","mobilePhone","code","merchantNo","type","operator","createTime","content"};
        String[] colsName = new String[]{"模板编号","手机号码","验证码","商户编号","业务类型","操作人","发送时间","发送内容"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("短信发送记录列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public int insertCusSmsRecordList(List<CusSmsRecord> list) {
        return cusSmsRecordDao.insertCusSmsRecordList(list);
    }
}
