package cn.eeepay.framework.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.model.OutAccountServiceRate;
import cn.eeepay.framework.model.OutAccountServiceRateTask;

@Service
public class OutAccountServiceRateJob {
	@Autowired
	private OutAccountServiceService outAccountServiceService;
	
	public void execute() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
//		String dateStr = sdf.format(date);
		
		List<OutAccountServiceRateTask> list =  outAccountServiceService.findByEffective();
		
		OutAccountServiceRate temp = null;
		
		List<OutAccountServiceRate> rateList = new ArrayList<OutAccountServiceRate>();
		List<Integer> taskIdList = new ArrayList<Integer>();
		//放到正在执行的分润表
		for (OutAccountServiceRateTask rateTask : list) {
			//查询原rule
			temp = outAccountServiceService.getById(rateTask.getOutAccountServiceRateId());
			rateList.add(temp);
			taskIdList.add(rateTask.getId());
		}
		outAccountServiceService.updateByTaskBatch(list);
		outAccountServiceService.updateByRateBatch(rateList, taskIdList);
	}
}
