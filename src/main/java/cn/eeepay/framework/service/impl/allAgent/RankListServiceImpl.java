package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.RankListDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.RankList;
import cn.eeepay.framework.service.allAgent.RankListService;
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
 * Created by Administrator on 2018/12/6/006.
 * @author  liuks
 * 排行榜列表service 实现类
 */
@Service("rankListService")
public class RankListServiceImpl implements RankListService {

    private static final Logger log = LoggerFactory.getLogger(RankListServiceImpl.class);

    @Resource
    private RankListDao rankListDao;


    @Override
    public List<RankList> selectAllList(RankList info, Page<RankList> page) {
        return rankListDao.selectAllList(info,page);
    }

    @Override
    public List<RankList> importDetailSelect(RankList info) {
        return rankListDao.importDetailSelect(info);
    }

    @Override
    public void importDetail(List<RankList> list, HttpServletResponse response) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "排行榜奖金发放明细列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("countMonth",null);
            maps.put("rank",null);
            maps.put("rewardAmount",null);
            maps.put("realName",null);
            maps.put("nickName",null);
            maps.put("mobile",null);
            maps.put("userCode",null);
            maps.put("userType",null);
            maps.put("oneUserCode",null);
            maps.put("oneAgentNo",null);
            maps.put("entryStatus",null);
            maps.put("entryTime",null);
            maps.put("entryRemark",null);
            data.add(maps);
        }else{
            Map<String, String> userTypeMap=new HashMap<String, String>();
            userTypeMap.put("1","机构");
            userTypeMap.put("2","大盟主");
            userTypeMap.put("3","盟主");
            Map<String, String> entryStatusMap=new HashMap<String, String>();
            entryStatusMap.put("0","未入账");
            entryStatusMap.put("1","已入账");

            for (RankList or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("countMonth",or.getCountMonth()==null?"":or.getCountMonth());
                maps.put("rank",or.getRank()==null?"":or.getRank().toString());
                maps.put("rewardAmount",or.getRewardAmount()==null?"":or.getRewardAmount().toString());
                maps.put("realName",or.getRealName()==null?"":or.getRealName());
                maps.put("nickName",or.getNickName()==null?"":or.getNickName());
                maps.put("mobile",or.getMobile()==null?"":or.getMobile());
                maps.put("userCode",or.getUserCode()==null?"":or.getUserCode());
                maps.put("userType",userTypeMap.get(or.getUserType()==null?"":or.getUserType().toString()));
                maps.put("oneUserCode",or.getOneUserCode()==null?"":or.getOneUserCode());
                maps.put("oneAgentNo",or.getOneAgentNo()==null?"":or.getOneAgentNo());
                maps.put("entryStatus",entryStatusMap.get(or.getEntryStatus()==null?"":or.getEntryStatus().toString()));
                maps.put("entryTime",or.getEntryTime()==null?"":sdf1.format(or.getEntryTime()));
                maps.put("entryRemark",or.getEntryRemark()==null?"":or.getEntryRemark());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"countMonth","rank","rewardAmount","realName",
                "nickName","mobile","userCode","userType","oneUserCode","oneAgentNo","entryStatus",
                "entryTime","entryRemark"
        };
        String[] colsName = new String[]{"排行榜月份","排名","奖金(元)","盟主姓名",
                "盟主昵称","盟主手机","盟主编号","用户类型","所属机构编号","一级代理商编号","入账状态",
                "入账日期","入账信息"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出排行榜奖金发放明细列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public Map<String, Object> selectAllSum(RankList info, Page<RankList> page) {
        return rankListDao.selectAllSum(info,page);
    }

}
