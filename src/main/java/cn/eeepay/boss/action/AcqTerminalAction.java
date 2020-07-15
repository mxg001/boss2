package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqTerminal;
import cn.eeepay.framework.service.AcqMerchantService;
import cn.eeepay.framework.service.AcqTerminalService;

@Controller
@RequestMapping(value="/acqTerminalAction")
public class AcqTerminalAction {
	private static final Logger log = LoggerFactory.getLogger(AcqTerminalAction.class);
	
	
	@Resource
	private AcqTerminalService acqTerminalService;
	
	@Resource
	private AcqMerchantService acqMerchantService;
	/**
	 * 初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<AcqTerminal> page,@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			AcqTerminal act = JSON.parseObject(param,AcqTerminal.class);
			acqTerminalService.selectAllInfo(page,act);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 修改收单终端状态
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateTermianlStatus")
	@ResponseBody
	@SystemLog(description = "收单终端状态开关",operCode="orgTerminal.switch")
	public Object updateTermianlStatus(@RequestParam("info") String param)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			JSONObject json=JSON.parseObject(param);
			AcqTerminal act=new AcqTerminal();
			act.setId(json.getInteger("id"));
			act.setStatus(json.getInteger("status"));
			int i = acqTerminalService.updateStatusByid(act);
			if(i>0){
				jsonMap.put("bols", true);
			}else{
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByParam")
	@ResponseBody
	public Object selectByParam(@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(ids,Integer.class);
			AcqTerminal act = acqTerminalService.selectByPrimaryKey(id);
			jsonMap.put("result", act);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 删除终端
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteByid")
	@ResponseBody
	@SystemLog(description = "删除终端",operCode="orgTerminal.delete")
	public Object deleteById(@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			int id=JSON.parseObject(ids,Integer.class);
			int i = acqTerminalService.deleteByPrimaryKey(id);
			if(i>0){
				jsonMap.put("bols", true);
			}else{
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	/**
	 * 修改终端
	 * @param param
	 * @return
	 */
	@RequestMapping(value="updateLockedById")
	@ResponseBody
	@SystemLog(description = "修改收单机构终端",operCode="orgTerminal.update")
	public Object updateLockedById(@RequestParam("info")String param){
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			JSONObject json=JSON.parseObject(param);
			AcqTerminal act=new AcqTerminal();
			act.setId(json.getInteger("id"));
			act.setLocked(json.getInteger("locked"));
			int i = acqTerminalService.updateByPrimaryKey(act);
			if(i>0){
				jsonMap.put("bols", true);
			}else{
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("报错！！！",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 添加收单机构终端
	 * @param param
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value="/addTermianlInfo")
//	@ResponseBody
//	public Object addTermianlInfo(@RequestParam("info")String param)throws Exception{
//		Map<String, Object> jsonMap=new HashMap<String, Object>();
//		try {
//			AcqTerminal atl = JSON.parseObject(param,AcqTerminal.class);
//			if(acqMerchantService.selectById(atl.getAcqMerchantNo())!=null){
//				if(acqTerminalService.selectTerminalByTerNo(atl)!=null){
//					jsonMap.put("result", false);
//					jsonMap.put("msg", "终端已存在!!");
//				}else{
//					atl.setCreatePerson("thj");
//					int i = acqTerminalService.insert(atl);
//					if(i>0){
//						jsonMap.put("result", true);
//						jsonMap.put("msg", "新增终端成功！！");
//					}else{
//						jsonMap.put("result", false);
//						jsonMap.put("msg", "新增终端失败！！");
//					}
//				}
//			}else{
//				jsonMap.put("result", false);
//				jsonMap.put("msg", "商户不存在!!");
//			}
//			
//		} catch (Exception e) {
//			log.error("报错!!!",e);
//			jsonMap.put("result", false);
//		}
//		return jsonMap;
//	}
}
