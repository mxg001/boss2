package cn.eeepay.boss.action.capitalInsurance;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import cn.eeepay.framework.service.AgentInfoService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.model.capitalInsurance.SafeConfig;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.model.capitalInsurance.ShareReportTotal;
import cn.eeepay.framework.service.capitalInsurance.BillEneryService;
import cn.eeepay.framework.service.capitalInsurance.SafeConfigService;
import cn.eeepay.framework.service.capitalInsurance.ShareReporService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.RandomNumber;



/**
 * 分润月报表管理
 * @author ivan
 *
 */
@Controller
@RequestMapping(value = "/shareReporAction")
public class ShareReporAction {
	private static final Logger log = LoggerFactory.getLogger(ShareReporAction.class);
	@Autowired
	private ShareReporService shareReporService;
	@Autowired
	private BillEneryService billEneryService;
	@Resource
	private SafeConfigService safeConfigService;
	@Resource
	private AgentInfoService agentInfoService;
	/**
	 * 
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getAllInfo")
	@ResponseBody
	public Object getAllInfo(@ModelAttribute("page") Page<ShareReport> page,
			@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ShareReport tis = JSON.parseObject(param, ShareReport.class);
			shareReporService.queryAllInfo(tis, page);
			map.put("page", page);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}
	
	
	/**
	 * 查询总金额
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getTotalAmount")
	@ResponseBody
	public Object getTotalAmount(@RequestParam("info") String param) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ShareReport cto = JSON.parseObject(param, ShareReport.class);
			ShareReportTotal totalInfo = shareReporService.queryAmount(cto);
			map.put("totalInfo", totalInfo);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}
	/**
	 * 分润入账
	 * @param ids
	 * @return
	 */
	@RequestMapping("/batchAccount")
	@ResponseBody
	public Object batchAccount(@RequestParam("ids") String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Integer> idList = JSON.parseObject(ids, List.class);
			int succcessCount = 0;
			for (Integer id : idList) {
				//入账接口
				int count = shareReporService.insuranceProfitRecordAccount(id);
				if(count == 1){
					succcessCount++;
				}
			}
			if(succcessCount == idList.size()){
				//全部入账成功
				map.put("bols", true);
			}else{
				//部分成功
				map.put("bols", false);
				map.put("msg", "入账成功："+succcessCount+"笔，失败："+(idList.size()-succcessCount)+"笔");
				
			}
			return map;
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}
	
	/**
	 * 分润按月汇总
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/initShareReport")
	@ResponseBody
	public Object initShareReport(@RequestParam("reportMonth") String reportMonth,@RequestParam("proCode") String proCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Integer count = shareReporService.isInitShareReport(reportMonth);
			if(count > 0){
				map.put("bols", false);
				map.put("msg", "当前月份已经汇总,请重新选择月份");
				return map;
			}
			int year = Integer.valueOf(reportMonth.substring(0, 4));
			int month = Integer.valueOf(reportMonth.substring(4, 6));
			String sDate= DateUtil.getFirstDayOfMonth(year,month)+"00:00:00";
			String eDate = DateUtil.getLastDayOfMonth(year,month)+"23:59:59";
			List<ShareReport> entrys =  billEneryService.getInitReport(sDate,eDate);
			if(entrys == null || entrys.size()==0){
				map.put("bols", false);
				map.put("msg", "当月无可汇总对账数据");
				return map;
			}
			
			String batchNo =  RandomNumber.mumberRandom(new SimpleDateFormat("yyyyMMdd").format(new Date()),3,0);
			SafeConfig config = safeConfigService.getSafeConfigByProCode(proCode);
			BigDecimal sharerate  = config.getAgentShare();
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			for (ShareReport report : entrys) {
				report.setCreatePerson(principal.getRealName());
				report.setBillMonth(reportMonth);
				report.setBatchNo(batchNo);
				report.setShareRate(sharerate);
				BigDecimal totleAmount = report.getTotalAmount();
				BigDecimal shareAmount = totleAmount.divide(new BigDecimal("100")).multiply(sharerate);
				//超级盟主代理商分润比例为0
				AgentInfo agentInfo =agentInfoService.getAgentByNo(report.getOneAgentNo());
				if(agentInfo!=null&&"11".equals(agentInfo.getAgentType())){
					report.setShareRate(BigDecimal.ZERO);
					shareAmount=BigDecimal.ZERO;
				}
				report.setShareAmount(shareAmount);
				int result = shareReporService.initShareReport(report,sDate,eDate);
				if(result > 0){
					map.put("bols", true);
				}else{
					map.put("bols", false);
					map.put("msg", "汇总失败");
				}
			}
		} catch (Exception e) {
			log.error("汇总报错", e);
			map.put("bols", false);
			map.put("msg", "汇总报错");
		}
		return map;
	}
	/**
	 * 导出代理商分润月表列表
	 */
	@RequestMapping(value="/importDetail")
	@ResponseBody
	public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> msg=new HashMap<String,Object>();
		try {
			param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
			ShareReport tis = JSON.parseObject(param, ShareReport.class);
			List<ShareReport> list=shareReporService.importDetailSelect(tis);
			shareReporService.importDetail(list,response);
		}catch (Exception e){
			log.error("导出代理商分润月表列表异常!",e);
			msg.put("status", false);
			msg.put("msg", "导出代理商分润月表列表异常!");
		}
		return msg;
	}
}
