package cn.eeepay.boss.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jpos.q2.qbean.BSH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditcardSource;
import cn.eeepay.framework.model.LoanSource;
import cn.eeepay.framework.model.OrgInfo;
import cn.eeepay.framework.model.OrgSourceConf;
import cn.eeepay.framework.model.OrgSourceConfigSumInfo;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.CreditcardSourceService;
import cn.eeepay.framework.service.OrgSourceConfigService;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;


/**
 * @author yanmj
 * @date 2018-11-02
 * @desc 超级银行家
 */
@Controller
@RequestMapping("/orgSourceConfig")
public class OrgSourceConfigAction {
	
	private final static Logger logger = LoggerFactory.getLogger(OrgSourceConfigAction.class);
	
	@Resource
	private CreditcardSourceService creditcardSourceService;
	@Resource
	private SuperBankService superBankService;
	@Resource
	private OrgSourceConfigService orgSourceConfigService;
	@Resource
	private SysDictService sysDictService;
	/**
	 * 查询信用卡列表
	 * @return
	 */
	@RequestMapping("/banksList")
	@ResponseBody
	public Result banksList() {
		Result result = new Result();
		try {
			List<CreditcardSource> creditcardSourcesList = creditcardSourceService.getAllBanks();
			result.setMsg("查询成功");
			result.setStatus(true);
			result.setData(creditcardSourcesList);
		} catch (Exception e) {
			logger.error("查询信用卡列表失败:",e);
			result.setMsg("查询信用卡列表失败");
		}
		return result;
	}
	
	/**
	 * 查询贷款机构列表
	 * @return
	 */
	@RequestMapping("/loansList")
	@ResponseBody
	public Result loansList() {
		Result result = new Result();
		try {
			List<LoanSource> creditcardSourcesList = superBankService.getLoanList();
			result.setMsg("查询成功");
			result.setStatus(true);
			result.setData(creditcardSourcesList);
		} catch (Exception e) {
			logger.error("查询贷款机构列表失败:",e);
			result.setMsg("查询贷款机构列表失败");
		}
		return result;
	}
	/**
	 * 组织配置
	 * @return
	 */
	@RequestMapping("/queryOrgSourcConfList")
	@ResponseBody
	public Result queryOrgSourcConfList(@RequestBody OrgSourceConf orgSourceConf,@RequestParam( defaultValue = "1")int pageNo,
            @RequestParam( defaultValue = "10")int pageSize){
		Result result = new Result();
		try {
			Page<OrgSourceConf> page = new Page<>(pageNo, pageSize);
			if(orgSourceConf == null){
				result.setStatus(false);
				result.setMsg("参数不能为空");
				return result;
			}
			if(!"1".equals(orgSourceConf.getType())&&!"2".equals(orgSourceConf.getType())){
				result.setMsg("参数有误");
				result.setStatus(false);
				return result;
			}
			//orgSourceConfigService.initConfig(orgSourceConf);
			orgSourceConfigService.getOrgSourceConfigPage(orgSourceConf, page);
			OrgSourceConfigSumInfo sumInfo = orgSourceConfigService.getOrgSourceConfigSumInfo(orgSourceConf);
			sumInfo.setOpenModel("单个链接外放");
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("sumInfo", sumInfo);
			map.put("page", page);
			result.setMsg("查询成功");
			result.setStatus(true);
			result.setData(map);
		} catch (Exception e) {
			result.setMsg("查询组织功能配置异常");
			logger.error("查询组织功能配置异常:",e);
		}
		return result;
	}
	
	@RequestMapping("/saveOrgSourceConfig")
	@ResponseBody
	public Result saveOrgSourceConfig(@RequestParam String data){
		Result result = new Result();
		if(data==null || "".equals(data)){
			result.setMsg("参数有误");
			return result;
		}
		try {
			data = URLDecoder.decode(data, "utf-8");
            JSONArray jsons = JSON.parseArray(data);

            result = checkOrderAndRefreshCache(jsons);
		} catch (Exception e) {
			result.setMsg("保存组织功能配置异常");
			logger.error("保存组织功能配置异常:",e);
		}
		return result;
	}
	
	private Result checkOrderAndRefreshCache(JSONArray jsons){
		Result result = new Result();
		Set<Integer> orderList = new HashSet<Integer>();
    	LinkedList<Long> idList = new LinkedList<Long>();
    	LinkedList<Long> noRecommendList = new LinkedList<>();
    	//保存已推荐数
    	int recommendCount = 0;
    	for (int i = 0; i < jsons.size(); i++) {
        	com.alibaba.fastjson.JSONObject  obj = jsons.getJSONObject(i);
			Long id = obj.getLong("id");
			Integer showOrder = obj.getInteger("showOrder");
			String recommend = obj.getString("isRecommend");
			idList.add(id);
			orderList.add(showOrder);
			if("1".equals(recommend)){
				recommendCount++;
			}
		}
        if (orderList.size()>0 && orderList.size() < jsons.size()) {
			result.setStatus(false);
			result.setMsg("保存的排序号有重复，请重填");
			return result;
		}
        OrgSourceConf conf = orgSourceConfigService.getOrgSourceById(idList.get(0));
		List<OrgSourceConf> recommendedOrgConf = orgSourceConfigService.getCountByRecommend(conf.getOrgId(),"1",conf.getApplication(),conf.getType());
		recommendCount = recommendCount + recommendedOrgConf.size();
		for (OrgSourceConf orgSourceConf:recommendedOrgConf) {
			for (Long id:idList){
				if((long)orgSourceConf.getId()==id){
					recommendCount--;
					break;
				}
			}
		}
		if(recommendCount>3){
			result.setStatus(false);
			result.setMsg("已推荐数超过3个,无法保存");
			return result;
		}
        for (Integer order : orderList) {
			Integer count = orgSourceConfigService.isExistOrder(conf.getOrgId(), order, conf.getApplication(), conf.getType(),idList);
			if (count != null && count > 0) {
				result.setStatus(false);
				result.setMsg("排序号 "+order+" 与其他数据有重复，请重填");
				return result;
			}
		}
		//修改组织配置
		int orgId = orgSourceConfigService.updateOrgSourceConfig(jsons);

        for (Long id : idList) {
        	OrgSourceConf conf1 = orgSourceConfigService.getOrgSourceById(id);
        	//刷新组织机构配置
        	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
        	String refreshSource = "";
        	if("1".equals(conf1.getType())){
        		refreshSource = Constants.REFRESH_CREDIT_SOURCE;
        	}else if("2".equals(conf1.getType())){
        		refreshSource = Constants.REFRESH_LOAN_SOURCE;
        	}
        	String back=ClientInterface.superBankPushLoanSource(String.valueOf(conf1.getSourceId()),sysDict.getSysValue()+refreshSource);
        	if(!StringUtils.isBlank(back)){
    			 JSONObject jsonObject = JSONObject.parseObject(back);
    			 if(jsonObject.containsValue("success")){
    			 result.setStatus(true);
    			 result.setMsg("操作成功");
    			 }else{
    				 result.setStatus(true);
    				 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
    			 }
    			 }else{
    				 result.setStatus(true);
    				 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
    			 }
        	break;
		}
        return result;
    }
	
	@RequestMapping("openOrClose")
	@ResponseBody
	public Result openOrClose(@RequestBody OrgSourceConf baseInfo){
		Result result = new Result();
		try {
			if(baseInfo==null||"".equals(baseInfo.getId())||
					(!"off".equals(baseInfo.getStatus())&&!"on".equals(baseInfo.getStatus()))){
				result.setStatus(false);
				result.setMsg("参数有误");
				return result;
			}
			boolean flag = true;
			OrgSourceConf conf1 = orgSourceConfigService.getOrgSourceById((long)baseInfo.getId());
			if("on".equals(baseInfo.getStatus())){
				flag = orgSourceConfigService.checkIsCanPutOn(conf1);
			}
			if(!flag){
				result.setStatus(false);
				if("1".equals(conf1.getType())){
					result.setMsg("信用卡银行管理该产品未上架，请先上架！");
				}else if("2".equals(conf1.getType())){
					result.setMsg("贷款机构管理该产品未上架，请先上架！");
				}
			}else{
				orgSourceConfigService.UpdateStatusById(baseInfo);
				//刷新组织机构配置
	        	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
	        	String refreshSource = "";
	        	if("1".equals(conf1.getType())){
	        		refreshSource = Constants.REFRESH_CREDIT_SOURCE;
	        	}else if("2".equals(conf1.getType())){
	        		refreshSource = Constants.REFRESH_LOAN_SOURCE;
	        	}
	        	String back=ClientInterface.superBankPushLoanSource(String.valueOf(conf1.getSourceId()),sysDict.getSysValue()+refreshSource);
	        	if(!StringUtils.isBlank(back)){
	    			 JSONObject jsonObject = JSONObject.parseObject(back);
	    			 if(jsonObject.containsValue("success")){
	    			 result.setStatus(true);
	    			 result.setMsg("操作成功");
	    			 }else{
	    				 result.setStatus(true);
	    				 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
	    			 }
    			 }else{
    				 result.setStatus(true);
    				 result.setMsg("修改成功 刷新缓存失败  请联系开发人员"); 
    			 }
			}
		} catch (Exception e) {
            e.printStackTrace();
            result.setMsg("状态有误");
        	result.setStatus(false);
        }
		return result;
	}
	
	/**批量上下架*/
    @RequestMapping("/batchOpenOrClose")
    @ResponseBody
    public Result batchOpenOrClose(@RequestParam("ids") String ids,@RequestParam("status") String status){
        Result result = new Result();
        try{
            String idArry[] = null;
            if(ids!=null && !"".equals(ids)){
                idArry = ids.split(",");
            }else{
            	result.setMsg("参数有误，修改失败");
            	result.setStatus(false);
                return result;
            }
            if(!"on".equals(status) && !"off".equals(status)){
            	result.setMsg("状态有误");
            	result.setStatus(false);
                return result;
            }
            boolean flag = true;
            if("on".equals(status)){
            	flag = orgSourceConfigService.checkIsCanBatchPutOn(idArry);
            }
            if (!flag) {
				result.setStatus(false);
				result.setMsg("存在未上架的产品");
			}else{
				orgSourceConfigService.batchUpd(idArry,status);
				//刷新组织机构配置
				for (String id : idArry) {
					OrgSourceConf conf1 = orgSourceConfigService.getOrgSourceById(Long.parseLong(id));
		        	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
		        	String refreshSource = "";
		        	if("1".equals(conf1.getType())){
		        		refreshSource = Constants.REFRESH_CREDIT_SOURCE;
		        	}else if("2".equals(conf1.getType())){
		        		refreshSource = Constants.REFRESH_LOAN_SOURCE;
		        	}
		        	String back=ClientInterface.superBankPushLoanSource(String.valueOf(conf1.getSourceId()),sysDict.getSysValue()+refreshSource);
		        	if(!StringUtils.isBlank(back)){
		    			 JSONObject jsonObject = JSONObject.parseObject(back);
		    			 if(jsonObject.containsValue("success")){
		    			 result.setStatus(true);
		    			 result.setMsg("操作成功");
		    			 }else{
		    				 result.setStatus(true);
		    				 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
		    			 }
	    			 }else{
	    				 result.setStatus(true);
	    				 result.setMsg("修改成功 刷新缓存失败  请联系开发人员"); 
	    			 }
		        break;
				}
			}
        }catch(Exception e){
            e.printStackTrace();
            result.setMsg("状态有误");
        	result.setStatus(false);
        }

        return result;
    }

}
