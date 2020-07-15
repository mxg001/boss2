package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.AcqService;

public interface AcqServiceProService {
	    
	    List<AcqService> selectBoxAllInfo(String acqId);
	    
	    List<AcqService> selectBox(String acqId);
	    
	    AcqService findServiceId(Integer id);
	    
	    /**
		 * 周期性关闭服务
		 */
		void periodicityClose();

		/**
		 * 根据收单服务Id判断该服务是否存在
		 * 
		 * @param serviceId
		 * @return
		 */
		boolean isExsitByServiceId(Integer serviceId);

		/**
		 * 根据服务Id获取服务名称
		 * 
		 * @param serviceId
		 * @return
		 */
		String getServiceNameByServiceId(Integer serviceId);
		
}
