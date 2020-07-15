package cn.eeepay.boss.action;

import java.util.List;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.BusinessProductInfo;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.service.BusinessProductInfoService;
import cn.eeepay.framework.service.ServiceProService;

@Controller
@RequestMapping(value="businessProductInfoAction")
public class BusinessProductInfoAction {

	private static final Logger log = LoggerFactory.getLogger(BusinessProductInfoAction.class);
	
	@Resource
	private BusinessProductInfoService businessProductInfoService;
	
	@Resource
	private ServiceProService serviceProService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectBoxAllInfo")
	@ResponseBody
	public Object selectBoxAllInfo(@RequestParam("ids")String ids){
		List<BusinessProductInfo> list=null;
		List<ServiceInfo> list1=null;
		try {
			if(ids.equals("-1")){
				list1=serviceProService.selectServiceInfo();
				return list1;
			}
			list=businessProductInfoService.selectInfoByBpId(ids);
		} catch (Exception e) {
			log.error("报错！！",e);
		}
		return list;
	}
}
