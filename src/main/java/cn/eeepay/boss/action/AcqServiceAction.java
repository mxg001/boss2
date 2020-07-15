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

import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.service.AcqServiceProService;

@Controller
@RequestMapping(value="/acqServiceAction")
public class AcqServiceAction {
	private static final Logger log = LoggerFactory.getLogger(AcqServiceAction.class);
	
	@Resource
	private AcqServiceProService acqServiceProService;
	
	/**
	 * 用于业务产品默认集群中
	 * @param ids
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectBoxAllInfo")
	@ResponseBody
	public Object selectBoxAllInfo(@RequestParam("ids")String ids){
		List<AcqService> list=null;
		try {
			list=acqServiceProService.selectBoxAllInfo(ids);
		} catch (Exception e) {
			log.error("报错！！",e);
		}
		return list;
	}
	
	/**
	 *用于收单商户中 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/selectBox")
	@ResponseBody
	public Object selectBox(@RequestParam("ids")String ids){
		List<AcqService> list=null;
		try {
			list=acqServiceProService.selectBox(ids);
		} catch (Exception e) {
			log.error("报错！！",e);
		}
		return list;
	}
}
