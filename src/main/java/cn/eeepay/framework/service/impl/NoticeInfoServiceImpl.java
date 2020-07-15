package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.BusinessProductDefineDao;
import cn.eeepay.framework.dao.NoticeInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.NoticeInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.NoticeInfoService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;

@Service("noticeInfoService")
@Transactional
public class NoticeInfoServiceImpl implements NoticeInfoService {

	@Resource
	private NoticeInfoDao noticeInfoDao;
	
	@Resource
	private AgentInfoDao agentInfoDao;

	@Resource
	private BusinessProductDefineDao businessProductDefineDao;

	@Resource
	private SeqService seqService;

	@Override
	public int insert(Map<String, Object> data) {
		NoticeInfo notice = (NoticeInfo) data.get("notice");
		String isAll = (String) data.get("isAll");
		String agentBusiness = (String) data.get("agentBusiness");
		//所有广告新增都是默认业务消息
		notice.setMsgType(0);

		// 将选中业务产品的ID用逗号拼接成字符串
		List<BusinessProductDefine> products = (List<BusinessProductDefine>) data.get("products");
		if (products != null && products.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (BusinessProductDefine product : products) {
				sb.append(product.getBpId());
				sb.append(",");
			}
			notice.setBpId(sb.substring(0, sb.length()-1));//去掉最后一个“，”，截取字符串时，含首不含尾
		} else {
			notice.setBpId(null);
		}
		// 当下发对象为‘商户’时，清空‘代理商’下面的notice数据（receiveType）
		if ("1".equals(notice.getSysType())) {
			notice.setReceiveType(null);
			// 当 指定代理商的商户选择全部时，页面填写的代理商ID设置为"0"
			if ("0".equals(agentBusiness)) {
				notice.setAgentNo("0");
			}
		}
		//当下发对象为代理商时，清空商户下面notice相关数据
    	if("2".equals(notice.getSysType())){
    		//notice.setBpId(null);
    		notice.setAgentNo(null);
			notice.setReceiveType(isAll);
    	}
		//商户情况下初始化
		if(notice.getOemType()==null||"".equals(notice.getOemType())){
			notice.setOemType("0");
		}
    	Date date = new Date();
		notice.setCreateTime(date); // 当前时间为公告创建时间
		notice.setStatus("1"); // 公告状态：待下发
		// issued_org：下发组织：0移联,一级代理商id
		notice.setIssuedOrg("0");
		// 还有字段未赋值
		// login_user：当前登录用户
		return noticeInfoDao.insert(notice);
	}

	@Override
	public Map<String, Object> selectById(String id) {
		Map<String, Object> map = new HashMap<>();
		NoticeInfo notice = noticeInfoDao.selectById(id);
		if(notice != null){
			String agentNo = notice.getAgentNo();
			List<BusinessProductDefine> allProduct = null;

			if(agentNo != null && !"0".equals(agentNo)){
				allProduct = businessProductDefineDao.getProductsByAgent(agentNo);
			} else {
				allProduct = businessProductDefineDao.selectBpTeam();
			}
			String bpIds = notice.getBpId();
			if (bpIds == null) {
				//为空表示选择全部
				//map.put("products", allProduct);
			}else{
				String[] ids = bpIds.split(",");
				List<BusinessProductDefine> products = new ArrayList<>();
				for (String bpId : ids) {
					products.add(businessProductDefineDao.selectById(bpId));
				}
				map.put("products", products);
			}
			if(agentNo != null && !"0".equals(agentNo)){
				notice.setAgentName(agentInfoDao.selectNameById(agentNo));
			}
			if(notice.getAttachment() != null){
				String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, notice.getAttachment(), new Date(64063065600000L));
				notice.setAttachmentUrl(url);
			}
			if(notice.getMessageImg() != null){
				String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, notice.getMessageImg(), new Date(64063065600000L));
				notice.setMessageImgUrl(url);
			}
			if(notice.getTitleImg() != null){
				String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, notice.getTitleImg(), new Date(64063065600000L));
				notice.setTitleImgUrl(url);
			}
			map.put("allProduct", allProduct);
			map.put("notice", notice);
		}
		
		return map;
	}

	@Override
	public List<NoticeInfo> selectByParam(NoticeInfo notice, Page<NoticeInfo> page) {
		return noticeInfoDao.selectByParam(notice, page);
	}

	@Override
	public int deliverNotice(long id) {
		// 还有两个字段未赋值
		// login_user：当前登录用户
		// issued_org：下发组织：0移联,一级代理商id
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("loginUser", principal.getId().toString());
		return noticeInfoDao.deliverNotice(map);
	}

	@Override
	public int update(Map<String, Object> data) {
		NoticeInfo notice = (NoticeInfo) data.get("notice");
		String isAll = (String) data.get("isAll");
		String agentBusiness = (String) data.get("agentBusiness");

		//商户情况下初始化
		if(notice.getOemType()==null||"".equals(notice.getOemType())){
			notice.setOemType("0");
		}
		// 将选中业务产品的ID用逗号拼接成字符串
		List<BusinessProductDefine> products = (List<BusinessProductDefine>) data.get("products");
		if (products != null && products.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (BusinessProductDefine product : products) {
				sb.append(product.getBpId());
				sb.append(",");
			}
			notice.setBpId(sb.substring(0, sb.length()-1));//去掉最后一个“，”，截取字符串时，含首不含尾
		} else {
			notice.setBpId(null);
		}
		// 当下发对象为‘商户’时，清空‘代理商’下面的notice数据（receiveType）
		if ("1".equals(notice.getSysType())) {
			notice.setReceiveType(null);
			// 当 指定代理商的商户选择全部时，页面填写的代理商ID设置为"0"
			if ("0".equals(agentBusiness)) {
				notice.setAgentNo("0");
			}
		}
		//当下发对象为代理商时，清空商户下面notice相关数据
    	if("2".equals(notice.getSysType())){
//    		notice.setBpId(null);
    		notice.setAgentNo(null);
			notice.setReceiveType(isAll);
    	}
		return noticeInfoDao.update(notice);
	}

	@Override
	public Map<String, Object> selectInfoById(String id) {
		Map<String, Object> map = selectById(id);
		List<AgentInfo> allAgent = agentInfoDao.selectByLevelOne();
		map.put("allAgent", allAgent);
		return map;
	}

	@Override
	public Map<String, Object> selectLinkInfo() {
		Map<String, Object> map = new HashMap<>();
		List<AgentInfo> allAgent = agentInfoDao.selectByLevelOne();
		List<BusinessProductDefine> allProduct = businessProductDefineDao.selectBpTeam();
		map.put("allProduct", allProduct);
		map.put("allAgent", allAgent);
		return map;
	}

	@Override
	public int updateRecoverNotice(Integer id) {
		return noticeInfoDao.updateRecoverNotice(id);
	}

	@Override
	public int deleteRecoverNotice(Integer id) {
		return noticeInfoDao.deleteRecoverNotice(id);
	}

	@Override
	public int clearStrongNotice(String sysType) {
		return noticeInfoDao.clearStrongNotice(sysType);
	}

	@Override
	public int strongNotice(Integer id, Integer strong) {
		if(new Integer(1).equals(strong)){
			//置顶时候更新下发时间
			return noticeInfoDao.strongNotice(id,strong);
		}else{
			//取消置顶不更新下发时间
			return noticeInfoDao.strongNoticeTime(id,strong);
		}

	}
}
