package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Msg;

import java.util.List;
import java.util.Map;

public interface MsgService {

	/**
	 * 查询提示语列表
	 * @param page
	 * @param msg
	 */
	void selectMsgByCondition(Page<Msg> page, Msg msg);

    int insertMsg(Msg msg);

    Msg msgDetail(String id);

	int selectByReasonAndOrg(String reason, String sourceOrg,Integer id);

	/**
	 * 更新提示语逻辑 
	 * @param msg
	 * @param msgMap
	 */
	void updateMsg(Msg msg,Map msgMap);
	
	int updateMsg(Msg msg);

	/**
	 * 校验是否存在重复
	 * @param resMsg
	 * @param acqName
	 * @return
	 */
	String queryByResonAndAcqName(String resMsg, String acqName);

	/**
	 * 校验是否存在重复
	 * @param resMsg
	 * @param acqName
	 * @return
	 */
	String queryMsgByReason(String resMsg);

	/**
	 * 添加 提示语实体
	 * @param msg 实体
	 * @param msgMap 返回消息
	 */
	void addMsg(Msg msg,Map msgMap);

	int changeStatus(String id, String status);

    List<Msg> queryAgentTips();

	Msg queryMsgByMsgCode(String rollMsg);


/*	Map<String, Object> insertMsg(Msg parseObject);

	Map<String, Object> updateMsg(Msg parseObject);*/

}
