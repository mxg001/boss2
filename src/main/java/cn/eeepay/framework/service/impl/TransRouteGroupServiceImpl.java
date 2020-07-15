package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RouteGroupDao;
import cn.eeepay.framework.dao.TransRouteGroupDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TransRouteGroup;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.TransRouteGroupService;

@Service("transRouteGroupService")
@Transactional
public class TransRouteGroupServiceImpl implements TransRouteGroupService {

	@Resource
	private TransRouteGroupDao transRouteGroupDao;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(TransRouteGroup record) {
		return 0;
	}

	@Override
	public int insertSelective(TransRouteGroup record) {
		return 0;
	}

	@Override
	public TransRouteGroup selectByPrimaryKey(Integer id) {
		return transRouteGroupDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(TransRouteGroup record) {
		return 0;
	}

	@Override
	public int updateByPrimaryKey(TransRouteGroup record) {
		return 0;
	}

	@Override
	public List<TransRouteGroup> selectByParam(Page<TransRouteGroup> page, Map<String, Object> info) {
		return transRouteGroupDao.selectByParam(page, info);
	}

	@Resource
	private RouteGroupDao routeGroupDao;
	
	@Override
	public int transferMer(String groupCode, List<TransRouteGroup> list) {
		TransRouteGroup newGroup  = routeGroupDao.getGroupByCode(groupCode);
		if(newGroup==null){
			throw new RuntimeException("找不到对应的集群");
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		for(TransRouteGroup group: list){
			if(newGroup.getServiceType().intValue()!=group.getServiceType().intValue()){
				throw new RuntimeException("商户与集群的服务类型不一致");
			}
			group.setCreatePerson(principal.getId().toString());
		}
		
		//20170209 集群转移数量过多
		int k = 0;
		int arrSize = list.size()%1000==0?list.size()/1000:list.size()/1000+1;  
        for(int i=0;i<arrSize;i++) {
            List<TransRouteGroup>  sub = new ArrayList<TransRouteGroup>();  
            //把指定索引数据放入到list中  
            for(int j=i*1000;j<=1000*(i+1)-1;j++) {  
                if(j<=list.size()-1) { 
                    sub.add(list.get(j));  
                }
            } 
            //批量删除之前商户所在集群的记录
            transRouteGroupDao.deleteBatchByMer(sub);
            //批量添加，向新的集群里面录入商户 20170214
            k = transRouteGroupDao.transferMer(groupCode, sub);
        }
		return arrSize*1000+k;
	}

	@Override
	public List<TransRouteGroup> selectGroupByAcqMerchantNo(String acqMerchantNo) {
		return transRouteGroupDao.selectGroupByAcqMerchantNo(acqMerchantNo);
	}

	@Override
	public List<TransRouteGroup> selectMerNoByGroupCode(String groupCode) {
		return transRouteGroupDao.selectMerNoByGroupCode(groupCode);
	}

	@Override
	public List<TransRouteGroup> selectAcqMerNoByGroupCode(String GroupCode) {
		return transRouteGroupDao.selectAcqMerNoByGroupCode(GroupCode);
	}

}
