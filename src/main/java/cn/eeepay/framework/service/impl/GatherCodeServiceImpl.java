package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.GatherCodeDao;
import cn.eeepay.framework.dao.GatherExportRecordDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.GatherCode;
import cn.eeepay.framework.model.GatherExportRecord;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.GatherCodeService;

@Service("gatherCodeService")
@Transactional
public class GatherCodeServiceImpl implements GatherCodeService {
	
	@Resource
	private GatherCodeDao gatherCodeDao;
	
	@Resource
	private SeqService seqService;
	
	@Resource
	private SysDictDao sysDictDao;
	
	@Resource
	private GatherExportRecordDao gatherExportRecordDao;

	@Override
	public List<GatherCode> selectByParams(Page<GatherCode> page, GatherCode gatherCode) {
		List<GatherCode> list = gatherCodeDao.selectByParams(page,gatherCode);
		for(int i=0;i<list.size();i++){
			list.get(i).setNumber(i+1);
		}
		return list;
	}

	@Override
	public GatherCode gatherCodeDetail(String id) {
		return gatherCodeDao.gatherCodeDetail(id);
	}

	@Override
	public int insertBatch(GatherCode info) {
		List<GatherCode> list = new ArrayList<>();
		for(int i=0;i<info.getNumber();i++){
			GatherCode code = new GatherCode();
			code.setMaterialType(info.getMaterialType());
//			code.setSn(seqService.createKey("gather_code_seq"));
			code.setGatherCode("SK" + getStringRandom(8));
			list.add(code);
			if(i%300==0){
				gatherCodeDao.insertBatch(list);
				list.clear();
			}
		}
		if(list.size()>0)
			gatherCodeDao.insertBatch(list);
		return info.getNumber();
	}
	
	@Override
	public List<GatherCode> exportGatherCode(GatherCode info) {
		//向收款码导出记录表添加一条数据
		GatherExportRecord record = new GatherExportRecord();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		record.setOperator(principal.getId().toString());
		gatherExportRecordDao.insert(record);
		List<GatherCode> list = gatherCodeDao.selectByParams(new Page<GatherCode>(0, info.getNumber()), info);
		gatherCodeDao.updateStatusBatch(1,list,record.getId());
		return list;
	} 
	
	@Override
	public String gatherCodeUrl(String id) {
		GatherCode info = gatherCodeDao.gatherCodeDetail(id);
		SysDict sysDict = sysDictDao.getByKey("GATHER_CODE_IP");
		return "http://" + sysDict.getSysValue()+"/gather/gatherProcess?source=3&settleMent=0&gatherCode="+info.getGatherCode();
	}

	@Override
	public int updateGatherStatus(String id, int i) {
		List<GatherCode> list = new ArrayList<>();
		GatherCode info = new GatherCode();
		info.setId(Long.parseLong(id));
		list.add(info);
		return gatherCodeDao.updateStatusBatch(i,list,null);
	}
	
	//生成随机数字和字母,  
    public String getStringRandom(int length) {  
        String val = "";  
        Random random = new Random();  
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
//                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + 97);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }

	
}
