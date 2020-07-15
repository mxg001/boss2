package cn.eeepay.boss.action;

import cn.eeepay.boss.system.CommonConst;
import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/agentInfo")
public class AgentInfoAction {
	private static final Logger log = LoggerFactory.getLogger(AgentInfoAction.class);
	@Resource
	public AgentInfoService agentInfoService;
	@Resource
	public AgentShareTaskService agentShareTaskService;
	@Resource
	public PosCardBinService posCardBinService;
	@Resource
	private BusinessProductDefineService businessProductDefineService;
	@Resource
	private SysDictService sysDictService;
	
	/**
	 * 
	 * 存量代理商开户批量处理：（查询未开户的一级代理商，进行统一开户处理）
	 * @author Ivan
	 * @date   2017/03/28
	 */
	@RequestMapping(value = "/levelOneCreateAccByBacth")
	@ResponseBody
	public  Map<String,Object> levelOneCreateAgentAccount(){
		Map<String,Object> msg=new HashMap<>();
		StringBuffer errorMsg = new StringBuffer();
		int successCount = 0;
		int errorCount = 0;
	    //List<AgentInfo> list = agentInfoService.selectByLevelOne();
	    //优化查询
		List<AgentInfo> list = agentInfoService.selectLevelOneAgentWithNoAccount();
		for (AgentInfo agentInfo : list) {
				String agentNo = agentInfo.getAgentNo();
				int num = agentInfoService.levelOneCreateAcc(agentNo);
				if(num > 0){
					log.info(agentNo + "--->>>开户成功");
					System.out.println(agentNo + "--->>>开户成功");
					successCount++;
				} else {
					System.out.println(agentNo + "--->>>开户失败");
					log.info(agentNo + "--->>>开户失败");
					errorMsg.append(agentNo).append(" | ");
					errorCount++;
				}
		}
		if(errorCount > 0){
			String showMsg = "已成功数量："+successCount+"\n 失败数量："+errorCount+"\n 开户失败代理商ID："+errorMsg.toString();
			msg.put("msg",showMsg );
			log.info(showMsg);
			System.out.println(showMsg);
		}else{
			msg.put("msg", "开户成功!\n 已成功数量："+successCount);
			log.info("开户成功!\n 已成功数量："+successCount);
			System.out.println("开户成功!\n 已成功数量："+successCount);
		}
		msg.put("status", true);
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByLevelOne.do")
	public @ResponseBody Object selectByLevelOne() throws Exception {
		List<AgentInfo> list = null;
		try {
			list = agentInfoService.selectByLevelOne();
		} catch (Exception e) {
			log.error("查询所有一级代理商失败！");
		}
		return list;
	}
	
	//销售查询使用
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfoSale")
	public @ResponseBody Object selectAllInfoSale(HttpServletRequest request) throws Exception {
		List<AgentInfo> list = null;
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			boolean isSalesperson = (boolean) request.getSession().getAttribute(CommonConst.isSalesperson);
			list = agentInfoService.selectAllInfoSale(principal.getRealName(),isSalesperson);
		} catch (Exception e) {
			log.error("查询所有一级代理商失败！");
		}
		return list;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfo")
	public @ResponseBody Object selectAllInfo(@RequestParam(value="item",required=false)String item,HttpServletRequest request) throws Exception {
		List<AgentInfo> list = null;
		try {
			boolean isSalesperson = (boolean) request.getSession().getAttribute(CommonConst.isSalesperson);
			if(isSalesperson){
				final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				list = agentInfoService.selectAllAgentInfo(item,principal.getRealName(), 0);
			}else{
				list = agentInfoService.selectAllAgentInfo(item, 0);
			}
		} catch (Exception e) {
			log.error("查询所有代理商失败！",e);
		}
		return list;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllOneInfo")
	public @ResponseBody Object selectAllOneInfo(@RequestParam(value="item",required=false)String item) throws Exception {
		List<AgentInfo> list = null;
		try {
			list = agentInfoService.selectAllAgentInfo(item, 1);
		} catch (Exception e) {
			log.error("查询所有一级代理商失败！",e);
		}
		return list;
	}

	/**
	 * 获取某个代理商下的所有代理商
	 * @param item 模糊字段
	 * @param parentAgentNo 父级编号
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getAllAgentListByParent")
	public @ResponseBody Object getAllAgentListByParent(@RequestParam(value="item",required=false)String item,
												@RequestParam(value="parentAgentNo",required=false)String parentAgentNo) throws Exception {
		List<AgentInfo> list = null;
		try {
			list = agentInfoService.getAllAgentListByParent(item, parentAgentNo);
		} catch (Exception e) {
			log.error("查询所有代理商失败！",e);
		}
		return list;
	}

	/**
	 * 根据代理商的名称模糊查询一级代理商
	 * @param agentName
	 * @return agent_no,agent_name
	 * @throws Exception
	 * add by tans
	 * 备用，目前没用到
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectOneAgentByName")
	@ResponseBody
	public List<AgentInfo> selectOneAgentName(String agentName) throws Exception {
		List<AgentInfo> list = null;
		try {
			list = agentInfoService.selectOneAgentByName(agentName);
		} catch (Exception e) {
			log.error("查询一级代理商名称失败！");
		}
		return list;
	}

	/**
	 * 根据代理商编号查代理商信息和组织信息
	 * @param agentNo
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAgentTeamByAgentNo")
	@ResponseBody
	public Map<String, Object> selectAgentTeamByAgentNo(@RequestBody String agentNo)throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = null;
		String msg="";
		boolean status=true;
		try {
			result = agentInfoService.selectAgentTeamByAgentNo(agentNo);
			if(result==null){
				status=false;
				msg="不存在编号"+agentNo+"的代理商";
			}else if(!result.get("agentLevel").equals("1")){
				status=false;
				msg="编号"+agentNo+"的代理商不是一级代理商";
			}
		} catch (Exception e) {
			log.error("查询代理商组织信息失败！");
		}
		map.put("result",result);
		map.put("msg",msg);
		map.put("status",status);
		return map;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectProductById/{id}")
	@ResponseBody
	public List<BusinessProductDefine> selectProductById(@PathVariable("id") String id) throws Exception {
		List<BusinessProductDefine> list = null;
		try {
			list = agentInfoService.selectProductById(id);
		} catch (Exception e) {
			log.error("代理商关联查询业务产品失败！",e);
		}
		return list;
	}
	
	/**
	 * 通过组织查询该组织下所有的业务产品 
	 * @param teamId 
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getProductByTeamId/{id}")
	@ResponseBody
	public List<JoinTable> selectProductByTeamId(@PathVariable("id") Integer id) throws Exception {
		List<JoinTable> list = new ArrayList<>();
		try {
			if(id!=null)
				list = agentInfoService.selectProductByTeamId(id,null);
		} catch (Exception e) {
			log.error("代理商关联查询业务产品失败！",e);
		}
		return list;
	}
	
	/**
	 * 查询所有的业务产品对应的服务费率和服务额度
	 * @param [40,41]
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getAgentServices")
	@ResponseBody
	public Map<String,Object> getAgentServices(@RequestBody String params) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			JSONObject json = JSON.parseObject(params);
			List<Integer> bpIds = JSONArray.parseArray(json.get("bpIds").toString(),Integer.class);
			String agentNo = json.getString("agentNo");
			map.put("agentId", agentNo);
			map.put("bpIds",bpIds);
			map = agentInfoService.getAgentServices(map);
		} catch (Exception e) {
			log.error("查询代理商的服务信息失败！");
		}
		return map;
	}
	
	/**
	 * 保存添加的一级代理商
	 * @param {agentInfo:代理商基本信息,dpData:业务产品id数组,
	 * 			shareData:分润信息,rateData:费率信息,quotaData:额度信息,happyBackTypes:欢乐返子类型}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAgentInfo")
	@ResponseBody
	@SystemLog(description = "保存添加的一级代理商",operCode="agent.insert")
	public Map<String,Object> saveAgentInfo(@RequestBody String data) {
		Map<String,Object> result = new HashMap<>();
		try {
			log.info(data);
			JSONObject json=JSONObject.parseObject(data);
			//boss系统，新增代理商时，欢乐返设置改为选填，可以不用勾选，之前的必勾选欢乐返子类型限制去掉
			//20180628 rpc
			/*List<ActivityHardwareType> happyBackTypes = JSONArray.parseArray(json.getString("happyBackTypes"), ActivityHardwareType.class);
			if (happyBackTypes.size() == 0) {
				result.put("status", false);
				result.put("msg", "请勾选代理商参加的欢乐返活动");
				return result;
			}*/
			AgentInfo agent = agentInfoService.saveAgentInfo(json);
			String msg = "添加代理商成功";
			if(agent.getAgentNo() != null){
				//如果是智能盛POS，则需要通知core
				final List<BusinessProductDefine> businessProductDefines = businessProductDefineService.selectProdcuteByTeamIdAndAgentNo(agent.getAgentNo(), "100060");
				if(CollectionUtil.isNotEmpty(businessProductDefines)){
					notifyCoreOmeType(agent.getAgentNo());
				}
			}
			result.put("status", true);
			result.put("msg", msg);
		} catch (Exception e) {
			log.error("保存代理商信息异常！",e);
			result.put("status", false);
			String str=e.getMessage();
			if(e.getMessage()==null){
				result.put("msg", "代理商信息不完整");
				return result;
			}

			result.put("msg", str);
		}
		return result;
	}
	/**
	 * 查询代理商列表
	 * @param data
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryAgentInfoList")
	@ResponseBody
	public Page<AgentInfo> queryAgentInfoList(@RequestParam("baseInfo")String data,@Param("page")Page<AgentInfo> page) {
		try{
			@SuppressWarnings("unchecked")
			Map<String,Object> params=JSONObject.parseObject(data, Map.class);
			agentInfoService.queryAgentInfoList(params,page);
		}catch(Exception e){
			log.error("查询代理商异常！",e);
		}
		return page;
	}
	
	/**
	 * 销售查询代理商列表
	 * @param data
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryAgentInfoListSale")
	@ResponseBody
	public Page<AgentInfo> queryAgentInfoListSale(@RequestParam("baseInfo")String data,@Param("page")Page<AgentInfo> page) {
		try{
			@SuppressWarnings("unchecked")
			Map<String,Object> params=JSONObject.parseObject(data, Map.class);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			agentInfoService.queryAgentInfoListSale(principal.getRealName(),params,page);
		}catch(Exception e){
			log.error("查询代理商异常！",e);
		}
		return page;
	}
	
	/**
	 * 通过代理商下详情信息
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryAgentInfoDetail")
	@ResponseBody
	public Map<String,Object> queryAgentInfoDetail(@RequestBody String param) throws Exception {
		return getDetail(param,0);
	}

	/**
	 * 敏感信息获取
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getDataProcessing")
	@ResponseBody
	public Map<String,Object> getDataProcessing(@RequestBody String param) throws Exception{
		return getDetail(param,3);
	}

	/**
	 * 敏感信息获取(销售界面)
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/dataProcessingTwo")
	@ResponseBody
	public Map<String,Object> dataProcessingTwo(@RequestBody String param) throws Exception{
		return getDetail(param,4);
	}

	private Map<String,Object> getDetail(@RequestBody String param,int editState){
		Map<String,Object> map = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			String agentNo=json.getString("agentNo");
			Integer teamId=json.getInteger("teamId");
			if(StringUtils.isNotBlank(agentNo)&&teamId!=null){
				map =agentInfoService.queryAgentInfoDetail(agentNo,teamId,false);
				AgentInfo agentInfo = (AgentInfo) map.get("agentInfo");
				if(0==editState&&agentInfo!=null){
					agentInfo.setMobilephone(StringUtil.sensitiveInformationHandle(agentInfo.getMobilephone(),0));
					agentInfo.setIdCardNo(StringUtil.sensitiveInformationHandle(agentInfo.getIdCardNo(),1));
					agentInfo.setAccountNo(StringUtil.sensitiveInformationHandle(agentInfo.getAccountNo(),4));
				}
				if(4==editState&&agentInfo!=null&&agentInfo.getAgentLevel().intValue()!=1){
					agentInfo.setMobilephone(StringUtil.sensitiveInformationHandle(agentInfo.getMobilephone(),0));
					agentInfo.setIdCardNo(StringUtil.sensitiveInformationHandle(agentInfo.getIdCardNo(),1));
					agentInfo.setAccountNo(StringUtil.sensitiveInformationHandle(agentInfo.getAccountNo(),4));
				}
				map.put("agentInfo",agentInfo);
				map.put("status", true);
			}else{
				map.put("status", true);
				map.put("msg", "没有查询到代理商业务产品！");
			}
		} catch (Exception e) {
			map.put("status", false);
			map.put("msg", "代理商关联查询业务产品失败！");
			log.error("代理商关联查询业务产品失败！",e);
			e.printStackTrace();
		}
		return map;
	}


	/**
	 * 通过代理商下详情信息
	 * @param agent_no 
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryAgentInfoAudit")
	@ResponseBody
	public Map<String,Object> queryAgentInfoAudit(@RequestBody String param) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			String agentNo=json.getString("agentNo");
			Integer teamId=json.getInteger("teamId");
			if(StringUtils.isNotBlank(agentNo)&&teamId!=null){
				map =agentInfoService.queryAgentInfoAudit(agentNo,teamId,false);
				map.put("status", true);
			}else{
				map.put("status", true);
				map.put("msg", "没有查询到代理商业务产品！");
			}
		} catch (Exception e) {
			map.put("status", false);
			map.put("msg", "代理商关联查询业务产品失败！");
			log.error("代理商关联查询业务产品失败！",e);
		}
		return map;
	}
	
	/**
	 * 获取所有的分润信息
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getAllShare")
	@ResponseBody
	public List<AgentShareRule> getAllShare(@RequestBody String param) throws Exception{
		List<AgentShareRule> list = null;
		try {
			JSONObject json=JSONObject.parseObject(param);
			String agentNo=json.getString("agentNo");
			list = agentInfoService.getAllShare(agentNo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询所有分润信息失败");
		}
		return list;
	}
	
	/**
	 * 通过代理商下详情用于修改
	 * @param agent_no 
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/editAgentInfoDetail")
	@ResponseBody
	public Map<String,Object> editAgentInfoDetail(@RequestBody String param) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			String agentNo=json.getString("agentNo");
			Integer teamId=json.getInteger("teamId");
			if(StringUtils.isNotBlank(agentNo)&&teamId!=null){
				map =agentInfoService.queryAgentInfoDetail(agentNo,teamId,true);
				map.put("status", true);
			}else{
				map.put("status", true);
				map.put("msg", "没有查询到代理商业务产品！");  
			}
		} catch (Exception e) {
			map.put("status", false);
			map.put("msg", "代理商关联查询业务产品失败！");
			log.error("代理商关联查询业务产品失败！",e);
		}
		return map;
	}

	/**
	 * 所有的业务产品
	 * @param
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/editAgentInfoProducts")
	@ResponseBody
	public Map<String,Object> editAgentInfoProducts(@RequestBody String param) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			String agentNo=json.getString("agentNo");
			String bpId=json.getString("bpId");
			//查询业务产品
			List<JoinTable> bps=agentInfoService.getAgentProducts(agentNo);
			map.put("agentProducts",bps);
			map.put("parentProducts", agentInfoService.selectProductByAgentNoBpId(bpId,agentNo));
		} catch (Exception e) {
			log.error("代理商关联查询业务产品失败！",e);
		}
		return map;
	}
	
	/**
	 * 获取新的服务
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getNewAgentServices")
	@ResponseBody
	public Map<String,Object> getNewAgentServices(@RequestBody String params) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			map = agentInfoService.getNewAgentServices(JSONObject.parseObject(params,Map.class));
		} catch (Exception e) {
			log.error("查询代理商的服务信息失败！",e);
		}
		return map;
	}
	
	/**
	 * 添加新分润信息任务列表
	 */
	@RequestMapping(value = "/addNewShare")
	@ResponseBody
	@SystemLog(description = "添加代理商分润信息",operCode="agent.addNewShare")
	public Map<String,Object> addNewShare(@RequestBody String params) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "添加失败");
		try {
			AgentShareRuleTask share = JSONObject.parseObject(params,AgentShareRuleTask.class);
			Date nowDate = new Date();
			//如果填写的生效日期小于当前日期，则返回
			if(share.getEfficientDate().getTime()<nowDate.getTime()){
				msg.put("status", true);
				msg.put("msg", "新增分润失败,生效日期必须大于等于当前日期！");
				return msg;
			}
			msg =agentShareTaskService.insertAgentShareList(share);
		} catch (Exception e) {
			e.printStackTrace();
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "添加分润信息异常");
			else
				msg.put("msg", str);	
		}
		return msg;
	}
	/**
	 * 修改分润
	 */
	@RequestMapping(value = "/updateShare")
	@ResponseBody
	@SystemLog(description = "修改代理商分润",operCode="agent.updateShare")
	public Map<String,Object> updateShare(@RequestBody String params) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			AgentShareRuleTask share = JSONObject.parseObject(params,AgentShareRuleTask.class);
			Date nowDate = new Date();
			//如果填写的生效日期小于当前日期，则返回
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(share.getEfficientDate()));
			if(share.getEfficientDate().getTime()<nowDate.getTime()){
				msg.put("status", true);
				msg.put("msg", "修改分润失败,生效日期必须大于等于当前日期！");
				return msg;
			}
			msg = agentShareTaskService.updateAgentShareList(share);
		} catch (Exception e) {
			log.error("代理商分润修改失败========",e);
			e.printStackTrace();
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "修改分润信息异常");
			else
				msg.put("msg", str);	
		}
		return msg;
	}
	/**
	 * 删除新分润信息任务列表
	 * 已经用过的分润不能删除
	 */
	@RequestMapping(value = "/delNewShare")
	@ResponseBody
	@SystemLog(description = "删除代理商分润信息",operCode="agent.delNewShare")
	public Map<String,Object> delNewShare(@RequestBody Integer id) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			int count=agentShareTaskService.deleteAgentShareTask(id);
			if(count>0){
				map.put("status", true);
			}else{
				map.put("status", false);
				map.put("msg", "删除新的分润信息失败！");
			}
		} catch (Exception e) {
			log.error("查询代理商的服务信息失败！",e);
			map.put("status", false);
			map.put("msg", "系统异常！删除新的分润信息失败！");
		}
		return map;
	}
	/**
	 * 查询分润信息任务列表
	 */
	@RequestMapping(value = "/queryNewShareList")
	@ResponseBody
	public List<AgentShareRuleTask> queryNewShareList(@RequestBody Integer id) throws Exception {
		List<AgentShareRuleTask> list=new ArrayList<>();
		try {
			list=agentShareTaskService.getAgentShareRuleTask(id);
			
		} catch (Exception e) {
			log.error("查询分润信息任务列表异常！",e);
		}
		return list;
	}
	/**
	 * 开启/关闭代理商业务产品
	 */
	@RequestMapping(value = "/updateAgentProStatus")
	@ResponseBody
	@SystemLog(description = "开启/关闭代理商业务产品",operCode="agent.updateAgentProStatus")
	public Map<String,Object> updateAgentProStatus(@RequestBody String param) throws Exception {
		Map<String,Object> msg = new HashMap<String,Object>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try {
			Map<String,Object> map = JSONObject.parseObject(param,Map.class);
			msg = agentInfoService.updateAgentProStatus(map);
		} catch (Exception e) {
			log.error("开启/关闭代理商业务产品异常！",e);
			msg.put("status", false);
			String str=e.getMessage();
			if(e.getMessage()==null){
				msg.put("msg", "信息不完整");
				return msg;
			}
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "操作异常");
			else
				msg.put("msg", str);
		}
		return msg;
	}
	
	/**
	 * 修改代理商的状态，顺带修改user_info,user_entity_info
	 * 备用，目前的版本不控制这一状态
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/switchStatus.do")
	@ResponseBody
	public Map<String, Object> switchStatus (@RequestBody AgentInfo agent) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try{
			int num = agentInfoService.updateStatus(agent);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "操作失败");
			}
		} catch (Exception e){
			msg.put("status", false);
			log.error("Switch agent失败！",e);
		}
		return msg;
	}
	
	/**
	 * 修改代理商的分润状态
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/switchProfitStatus.do")
	@ResponseBody
	@SystemLog(description = "修改代理商的分润状态",operCode="agent.switchProfitStatus")
	public Map<String, Object> switchProfitStatus (@RequestBody AgentInfo agent) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try{
			msg = agentInfoService.updateProfitStatus(agent);
		} catch (Exception e){
			msg.put("status", false);
			log.error("Switch agent失败！",e);
		}
		return msg;
	}
	
	/**
	 * 
	 * @author tans
	 * @date 2017年5月22日 上午9:37:14
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/updateProgitSwitchBatch")
	@ResponseBody
	@SystemLog(description = "批量修改代理商的分润状态",operCode="agent.updateProgitSwitchBatch")
	public Map<String, Object> updateProgitSwitchBatch(@RequestBody String param){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try {
			JSONObject json = JSONObject.parseObject(param);
			List<String> agentNodeList = JSONObject.parseArray(json.getString("agentNodeList"), String.class);
			Integer switchStatus = json.getInteger("switchStatus");
			int num = agentInfoService.updateProgitSwitchBatch(agentNodeList,switchStatus);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}
		} catch (Exception e) {
			log.error("批量修改代理商分润状态失败，param:{}",param);
			log.error(e.toString());
		}
		return msg;
	}

	/**
	 * 修改代理商推广功能开关
	 * @author	mays
	 * @date	2017年8月11日 下午5:16:00
	 */
	@RequestMapping(value="/switchPromotionStatus.do")
	@ResponseBody
	@SystemLog(description = "修改代理商推广功能开关",operCode="agent.agentPromotion")
	public Map<String, Object> switchPromotionStatus (@RequestBody AgentInfo agent) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try{
			if (agent.getAgentLevel() != 1) {
				msg.put("msg", "只能控制一级代理商,该代理商不是一级代理商");
				return msg;
			}
			msg = agentInfoService.updatePromotionStatus(agent);
		} catch (Exception e){
			msg.put("status", false);
			log.error("修改代理商推广功能开关,失败！",e);
		}
		return msg;
	}


	/**
	 * 修改代理商欢乐返返现开关
	 * @author	rpc
	 * @date	2018年6月28日 16:38:00
	 */
	@RequestMapping(value="/switchCashBackStatus.do")
	@ResponseBody
	@SystemLog(description = "修改代理商欢乐返返现开关",operCode="agentinfo.switchCashBackStatus.do")
	public Map<String, Object> switchCashBackStatus (@RequestBody AgentInfo agent) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try{
			if (agent.getAgentLevel() != 1) {
				msg.put("msg", "只能控制一级代理商,该代理商不是一级代理商");
				return msg;
			}
			msg = agentInfoService.updateCashBackStatus(agent);
		} catch (Exception e){
			msg.put("status", false);
			log.error("修改代理商推广功能开关,失败！",e);
		}
		return msg;
	}

	@RequestMapping(value="/updatePromotionSwitchBatch")
	@ResponseBody
	@SystemLog(description = "批量修改代理商推广功能开关",operCode="agent.agentPromotion")
	public Map<String, Object> updatePromotionSwitchBatch(@RequestBody String param){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try {
			JSONObject json = JSONObject.parseObject(param);
			List<String> agentNodeList = JSONObject.parseArray(json.getString("agentNodeList"), String.class);
			Integer switchStatus = json.getInteger("switchStatus");
			int num = agentInfoService.updatePromotionSwitchBatch(agentNodeList,switchStatus);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}
		} catch (Exception e) {
			log.error("批量修改代理商推广功能开关失败，param:{}",param);
			log.error(e.toString());
		}
		return msg;
	}

	/**
	 * 删除代理商
	 */
	@RequestMapping(value = "/delAgent")
	@ResponseBody
	@SystemLog(description = "删除代理商",operCode="agent.delAgent")
	public Map<String,Object> delAgent( String agentNo, Integer teamId) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			String str=agentInfoService.delAgent(agentNo, teamId);
			msg.put("status", true);
			msg.put("msg", str);
		} catch (Exception e) {
			log.error("删除代理商异常！",e);
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "删除代理商信息异常");
			else
				msg.put("msg", str);
		}
		return msg;
	}
	
	/**
	 * 代理商开户
	 */
	@RequestMapping(value = "/openAccount")
	@ResponseBody
	@SystemLog(description = "代理商开户",operCode="agent.openAccount")
	public Map<String,Object> openAccount( String agentNo) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			System.out.println(agentNo);
			int num=agentInfoService.updateAgentAccount(agentNo);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "开户成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "开户失败");
			}
			
		} catch (Exception e) {
			log.error("开户异常！",e);
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "开户异常");
			else
				msg.put("msg", str);
		}
		return msg;
	}
	
	/**
	 * 存量代理商根据科目开户
	 */
	@RequestMapping(value = "/openAccountBatch")
	@ResponseBody
	@SystemLog(description = "批量代理商根据科目号开户",operCode="agent.openAccountBatch")
	public Map<String,Object> openOldAccount(@RequestBody String param) {
		Map<String,Object> msg=new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try {
			JSONObject json = JSONObject.parseObject(param);
			String subjectNo = json.getString("subjectNo");
			List<String> agentNoList = JSONObject.parseArray(json.getString("agentNoList"), String.class);
			if(agentNoList == null || agentNoList.size()>5000 || StringUtils.isBlank(subjectNo)){
				msg.put("msg", "参数异常");
				return msg;
			}
			msg = agentInfoService.openOldAccount(agentNoList,subjectNo);
		} catch (Exception e) {
			log.error("批量代理商根据科目号开户，param:{}",param);
			log.error(e.toString());
		}
		return msg;
	}
	
	/**
	 * 保存代理商修改
	 */
	@RequestMapping(value = "/updateAgent")
	@ResponseBody
	@SystemLog(description = "保存代理商修改",operCode="agent.edit")
	public Map<String,Object> updateAgent(@RequestBody String data) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			String str=agentInfoService.updateAgent(data);
			JSONObject json = JSONObject.parseObject(data);
			AgentInfo agent=JSONObject.parseObject(json.getString("agentInfo"), AgentInfo.class);
			if(agent.getAgentNo() != null){
				//如果是智能盛POS，则需要通知core
				final List<BusinessProductDefine> businessProductDefines = businessProductDefineService.selectProdcuteByTeamIdAndAgentNo(agent.getAgentNo(), "100060");
				if(CollectionUtil.isNotEmpty(businessProductDefines)){
					notifyCoreOmeType(agent.getAgentNo());
				}
			}
			msg.put("status", true);
			msg.put("msg", str);
		} catch (Exception e) {
			log.error("保存代理商修改异常！",e);
			log.error(e.getMessage());
			msg.put("status", false);
			String str=e.getMessage();
			if(e.getMessage()==null){
				msg.put("msg", "代理商信息不完整");
				return msg;
			}
			msg.put("msg", str);
		}
		return msg;
	}
	
	/**
	 * 保存代理商修改
	 */
	@RequestMapping(value = "/updateAgentShare")
	@ResponseBody
	@SystemLog(description = "代理商审核",operCode="agent.auditShare")
	public Map<String,Object> updateAgentShare(@RequestBody String data) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			int num =agentInfoService.updateAgentShare(data);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}else {
				msg.put("status", false);
				msg.put("msg", "操作失败");
			}
			
		} catch (Exception e) {
			log.error("审核代理商分润异常！",e);
			log.error(e.getMessage());
			msg.put("status", false);
			msg.put("msg", "审核代理商分润异常");
		}
		return msg;
	}
	
	@RequestMapping(value = "/queryAllShareTask")
	@ResponseBody
	public Page<AgentShareRuleTask> queryAllShareTask(@RequestParam("baseInfo")String data,@Param("page")Page<AgentShareRuleTask> page) throws Exception {
		try{
			AgentShareRuleTask params = JSONObject.parseObject(data, AgentShareRuleTask.class);
			agentShareTaskService.queryAllShareTask(params,page);
		}catch(Exception e){
			log.error("查询代理商异常！",e);
		}
		return page;
	}

	/**
	 * 查询欢乐返子类型
	 * @author	mays
	 * @date	2018年5月15日
	 */
	@RequestMapping(value = "/selectHappyBackType")
	@ResponseBody
	public Map<String, Object> selectHappyBackType() {
		Map<String, Object> msg = new HashMap<>();
		try {
			List<ActivityHardwareType> xhlfList = agentInfoService.selectHappyBackTypeBySubType("009","2");
			List<ActivityHardwareType> superList = agentInfoService.selectHappyBackTypeBySubType("009","3");
			msg.put("status", true);
			msg.put("xhlfList", xhlfList);
			msg.put("superList", superList);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("查询欢乐返子类型失败", e);
		}
		return msg;
	}

	/**
	 * 代理商管理获取欢乐返子类型
	 * @return
	 */
	@RequestMapping(value = "/selectHappyBackTypeWithTeamId")
	@ResponseBody
	public Map<String, Object> selectHappyBackTypeWithTeamId(@RequestParam("teamId")String teamId) {
		Map<String, Object> msg = new HashMap<>();
		List<String> ids = new ArrayList<>();
		if(StringUtils.isNotBlank(teamId)){
			String[] teadIds = teamId.split(",");
			ids = Arrays.asList(teadIds);
		}
		List<ActivityHardwareType> list;
		try {
			if(null != ids && ids.size() > 0){
				List<String> teamIds = businessProductDefineService.selectTeamIdsWithBpIds(ids);
				list = agentInfoService.selectHappyBackTypeWithTeamId(teamIds);
			}else {
				list = agentInfoService.selectHappyBackType();
			}
            //将新欢乐返和欢乐返分开
            List<ActivityHardwareType> activityHardwareTypeList = new ArrayList<>();
            if(list != null && list.size() >0) {
                for(ActivityHardwareType item: list) {
                    if(StringUtils.isNotEmpty(item.getSubType()) && "1".equals(item.getSubType())) {
                        activityHardwareTypeList.add(item);
                    }
                }
            }
			msg.put("status", true);
			msg.put("list", activityHardwareTypeList);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("查询欢乐返子类型失败", e);
		}
		return msg;
	}


	@RequestMapping(value = "/selectAgentRole")
	@ResponseBody
	public List<Map> selectAgentRole() {
		List<Map> list=null;
		try {
			list = agentInfoService.selectAgentShareRule();
		} catch (Exception e) {
			log.error("查询欢代理商权限", e);
		}
		return list;
	}

	/**
	 * 查询代理商列表
	 * @param data
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryAgentRoleOemList")
	@ResponseBody
	public Page<Map> queryAgentRoleOemList(@RequestParam("info")String data,@Param("page")Page<Map> page) {
		try{
			Map<String,Object> params=JSONObject.parseObject(data, Map.class);
			agentInfoService.queryAgentRoleOemList(params,page);
		}catch(Exception e){
			log.error("查询代理商异常！",e);
		}
		return page;
	}

	/**
	 * 保存代理商角色修改
	 */
	@RequestMapping(value = "/addAgentRoleOem")
	@ResponseBody
	@SystemLog(description = "新增代理商角色",operCode="agent.addAgentRoleOem")
	public Map<String,Object> addAgentRoleOem(@RequestParam("info")String data) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			Map<String,Object> params=JSONObject.parseObject(data, Map.class);
			Map map=agentInfoService.selectAgentRoleOem(params);
			if(map==null){
				agentInfoService.insertAgentRoleOem(params);
				msg.put("status", true);
				msg.put("msg", "新增代理商角色成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "代理商角色已存在");
			}
		} catch (Exception e) {
			log.error("新增代理商角色异常！",e);
			msg.put("status", false);
			msg.put("msg", "新增代理商角色异常");
		}
		return msg;
	}

	/**
	 * 保存代理商角色修改
	 */
	@RequestMapping(value = "/updateAgentRoleOem")
	@ResponseBody
	@SystemLog(description = "修改代理商角色",operCode="agent.updateAgentRoleOem")
	public Map<String,Object> updateAgentRoleOem(@RequestParam("info")String data) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			Map<String,Object> params=JSONObject.parseObject(data, Map.class);
			Map map=agentInfoService.selectAgentRoleOem(params);
			if(map==null){
				agentInfoService.updateAgentRoleOem(params);
				msg.put("status", true);
				msg.put("msg", "修改代理商角色成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "代理商角色已存在");
			}
		} catch (Exception e) {
			log.error("修改代理商角色异常！",e);
			msg.put("status", false);
			msg.put("msg", "修改代理商角色异常");
		}
		return msg;
	}

	/**
	 * 删除代理商角色
	 */
	@RequestMapping(value = "/deleteAgentRoleOem")
	@ResponseBody
	@SystemLog(description = "删除代理商角色",operCode="agent.deleteAgentRoleOem")
	public Map<String,Object> deleteAgentRoleOem(@RequestBody Integer id) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			if(agentInfoService.deleteAgentRoleOem(id)>0){
				msg.put("status", true);
				msg.put("msg", "删除代理商角色成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "删除代理商角色失败");
			}
		} catch (Exception e) {
			log.error("删除代理商角色异常！",e);
			msg.put("status", false);
			msg.put("msg", "删除代理商角色异常");
		}
		return msg;
	}

	/**
	 * 用户清单
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/userAgentRoleOem")
	@ResponseBody
	public List<Map> userAgentRoleOem(@RequestParam("info")String data) {
		List<Map> list=null;
		try{
			Map<String,Object> params=JSONObject.parseObject(data, Map.class);
			list=agentInfoService.selectUserAgentRoleOem(params);
		}catch(Exception e){
			log.error("用户清单异常！",e);
		}
		return list;
	}

	/**
	 * 查询一级代理商待审核分润
	 * @param agentInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/selectAgentShareCheckList")
	@ResponseBody
	public Result selectAgentShareCheckList(@RequestBody AgentInfo agentInfo,
										  @RequestParam(defaultValue = "1") int pageNo,
										  @RequestParam(defaultValue = "10") int pageSize){
		Result result = new Result();
		try {
			Page<AgentInfo> page = new Page<>(pageNo, pageSize);
			agentInfoService.selectAgentShareCheckList(page, agentInfo);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(page);
		} catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询一级代理商待审核分润异常", e);
		}
		return result;
	}

	@RequestMapping("/exportAgentShareCheck")
	public void exportAgentShareCheck(@RequestParam String param, HttpServletResponse response){
		try {
			AgentInfo info = JSON.parseObject(param,AgentInfo.class);
			agentInfoService.exportAgentShareCheck(info, response);
		} catch (Exception e) {
			log.info("导出代理商审核失败,参数:{}");
			log.info(e.toString());
		}
	}

	/**
	 * 查找联行行号
	 */
	@RequestMapping(value = "/getPoscnapsNo")
	@ResponseBody
	public Map<String,Object> getPoscnapsNo(@RequestParam("bankName") String bankName) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			String cnapsNo=posCardBinService.getPoscnapsNoByBankName(bankName);
			if(StringUtil.isNotBlank(cnapsNo)){
				msg.put("cnapsNo", cnapsNo);
				msg.put("status", true);
				msg.put("msg", "查询成功");
			}else {
				msg.put("status", false);
				msg.put("msg", "获取失败，请输入正确的开户行全称");
			}
		} catch (Exception e) {
			log.error("查找联行行号异常！",e);
			msg.put("status", false);
			msg.put("msg", "查找联行行号异常");
		}
		return msg;
	}

	public void notifyCoreOmeType(final String agentNo){
		new Thread(new Runnable() {
			@Override
			public void run() {
				SysDict sysDict = sysDictService.getByKey("CORE_SERVICE_URL");
				String accessUrl = sysDict.getSysValue() + "/zpos/refreshAgentInfo";
				String params = "agentNo=" + agentNo;
				final String result = HttpUtils.sendPost(accessUrl, params, "UTF-8");
				log.info("{}智能盛pos返回结果：{}",accessUrl,result);
			}
		}).start();
	}

}
