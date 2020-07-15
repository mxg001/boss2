package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.job.UnSettleJob;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.service.SysDictService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysDictService")
@Transactional
public class SysDictServiceImpl implements SysDictService {

	private final Logger log = LoggerFactory.getLogger(SysDictServiceImpl.class);

	@Resource
	private RedisService redisService;

	@Resource
	private SysDictDao sysDictDao;

	@Override
	public List<SysDict> selectDicByCondition(SysDict dict, Page<SysDict> page) {
		return sysDictDao.selectDicByCondition(dict,page);
	}

	@Override
	public Map<String, Object> insert(SysDict info) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		if(info != null && "BOSS_UNIQUE".equals(info.getParentId())){
			int num = sysDictDao.checkUnique(info);
			if(num>0){
				msg.put("msg", "已存在唯一的数据字典");
				return msg;
			}
		}
		//如果是再次出款相关的数据字典，保存到redis
		if (insertReSettleRedis(info, msg)) return msg;
		int num = sysDictDao.insert(info);
		if(num > 0){
			msg.put("status", true);
			msg.put("msg", "操作成功");
		}
		return msg;
	}

	@Override
	public Map<String, Object> update(SysDict info) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		if(info != null && "BOSS_UNIQUE".equals(info.getParentId())){
			int num = sysDictDao.checkUnique(info);
			if(num>0){
				msg.put("msg", "已存在唯一的数据字典");
				return msg;
			}
		}
		//如果是再次出款相关的数据字典，保存到redis
		if (insertReSettleRedis(info, msg)) return msg;
		int num = sysDictDao.update(info);
		if(num > 0){
			msg.put("status", true);
			msg.put("msg", "操作成功");
		}
		return msg;
	}

	/**
	 * 如果是再次出款相关的数据字典，保存到redis
	 * @param info
	 * @param msg
	 * @return
	 */
	private boolean insertReSettleRedis(SysDict info, Map<String, Object> msg) {
		if(info!=null && info.getSysKey() != null) {
			String sysKey = info.getSysKey();
			if (sysKey.startsWith("RE_SETTLE_TASK")) {
				String time = info.getSysValue();//单位为：秒
				if ("RE_SETTLE_TASK_CHANNELS".equals(info.getSysKey())) {
					if (StringUtils.isBlank(time)) {
						msg.put("status", false);
						msg.put("msg", "值不能为空");
						return true;
					}
				}

				if (!"RE_SETTLE_TASK".equals(info.getSysKey())) {
					try {
						redisService.insertString(info.getSysKey(), time);
					} catch (Exception e) {
						log.error("修改字典存入缓存失败{}:{}", new String[]{info.getSysKey(), time});
					}
				}
			}
		}
		return false;
	}

	@Override
	public int delete(Integer id) {
		return sysDictDao.delete(id);
	}

	@Override
	public JSONObject selectDictAndChildren() {
		List<SysDict> list = sysDictDao.selectAllDict();
		JSONObject json=new JSONObject();
		if(list != null && list.size()>0){
			for(int i=0; i< list.size(); i++){
				SysDict dic=list.get(i);
				JSONArray array=null;
				if(json.containsKey(dic.getSysKey())){
					array=json.getJSONArray(dic.getSysKey());
				}else{
					array=new JSONArray();
					json.put(dic.getSysKey(), array);
				}
				JSONObject item=new JSONObject();
				item.put("text", dic.getSysName());
				if("INT".equals(dic.getType()))
					item.put("value", Integer.parseInt(dic.getSysValue()));
				else
					item.put("value", dic.getSysValue());
				array.add(item);
			}
		}
		return json;
	}

	@Override
	public SysDict getByKey(String string) {
		return sysDictDao.getByKey(string);
	}

	@Override
	public SysDict selectRestPwd() {
		return sysDictDao.selectRestPwd();
	}

	@Override
	public SysDict selectExistServiceLink(String serviceType, String string) {
		return sysDictDao.selectExistServiceLink(serviceType, string);
	}

	@Override
	public List<SysDict> selectByKey(String string) {
		return sysDictDao.selectListByKey(string);
	}

	@Override
	public Map<String, String> selectMapByKey(String key) {
		Map<String, String> sysDictMap = new HashMap<>();
		List<SysDict> sysDictList = sysDictDao.selectListByKey(key);
		if(sysDictList!=null && sysDictList.size()>0){
			for(SysDict sysDict: sysDictList){
				sysDictMap.put(sysDict.getSysValue(), sysDict.getSysName());
			}
		}
		return sysDictMap;
	}
	
	@Override
	public int updateSysValue(SysDict sysDict) {
		int num = sysDictDao.updateSysValue(sysDict);
		if(num > 1){
			throw new RuntimeException("updateSysValue一次只能修改一个唯一的数据字典");
		}
		return num;
	}

	@Override
	public String getValues(String parentId) {
		List<String> values = sysDictDao.getValueByparent(parentId);
		if(values != null && values.size() > 0){
			StringBuffer result = new StringBuffer();
			for(String value:values){
				result.append("'"+value+"',");
			}
			return result.substring(0, result.length()-1);
		}else
			return "''";
	}

	@Override
	public List<SysDict> getAcqMerchantList(String str) {
		return sysDictDao.getAcqMerchantList(str);
	}

	public String getValueByKey(String sysKey){
		return sysDictDao.getValueByKey(sysKey);
	}

	@Override
	public List<SysDict> selectByOnlyKey(String string) {
		return sysDictDao.selectByOnlyKey(string);
	}

	@Override
	public String getFirstValueByKey(String cjt_service_id) {
		return sysDictDao.getFirstValueByKey(cjt_service_id);
	}

	/**
	 * 通过数据字段key值返回map
	 * @param key 数据字段key
	 * @param order true  map{数据字段vaules:数据字段name}
	 *              false map{数据字段name:数据字段vaules}
	 * @return
	 */
	@Override
	public Map<String, String> selectMapKeyAndName(String key, boolean order) {
		Map<String, String> sysDictMap = new HashMap<>();
		List<SysDict> sysDictList = sysDictDao.selectListByKey(key);
		if(sysDictList!=null && sysDictList.size()>0){
			for(SysDict sysDict: sysDictList){
				if(order){
					sysDictMap.put(sysDict.getSysValue(), sysDict.getSysName());
				}else{
					sysDictMap.put(sysDict.getSysName() , sysDict.getSysValue());
				}

			}
		}
		return sysDictMap;
	}
}
