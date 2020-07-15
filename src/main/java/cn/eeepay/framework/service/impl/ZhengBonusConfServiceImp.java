package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgInfo;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.provider.ZhengBonusConfProvider;
import cn.eeepay.framework.rpc.entity.ZhengBonusConfEntity;
import cn.eeepay.framework.rpc.request.SelectZhengBonusConfReq;
import cn.eeepay.framework.rpc.response.BaseResponse;
import cn.eeepay.framework.rpc.response.SelectZhengBonusConfResp;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.ZhengBonusConfService;
import cn.eeepay.framework.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ZhengBonusConfServiceImp  implements ZhengBonusConfService{

	@Resource
	private ZhengBonusConfProvider zhengBonusConfProvider;

	@Resource
	private SuperBankService superBankService;

	@Override
	public int saveZhengBonusConf(ZhengBonusConfEntity entity) {
		int size=0;
		BaseResponse baseResponse = zhengBonusConfProvider.saveZhengBonusConf(entity);
		if(baseResponse.isStatus()){
			size=1;
		}
		return size;
	}

	@Override
	public List<ZhengBonusConfEntity> selectByOrgId(ZhengBonusConfEntity zhengBonusConfEntity,Page<ZhengBonusConfEntity> page) {
		SelectZhengBonusConfReq req=new SelectZhengBonusConfReq();
		if(-1==zhengBonusConfEntity.getOrgId().intValue()){
			zhengBonusConfEntity.setOrgId(null);
		}
		req.setZhengBonusConfEntity(zhengBonusConfEntity);
		req.setPageNo(page.getPageNo());
		req.setPageSize(page.getPageSize());
		SelectZhengBonusConfResp resp = zhengBonusConfProvider.selectByOrgId(req);
		List<ZhengBonusConfEntity> list=null;
	    list=(List<ZhengBonusConfEntity>) resp.getData();
		if(list!=null&&list.size()>0) {
			for (ZhengBonusConfEntity record : list) {
				List<OrgInfo> orgInfos = superBankService.getOrgInfoList();
				Long orgId = record.getOrgId();
				if(orgId.intValue()==0){
					record.setOrgName("默认");
				}
				for (OrgInfo orgInfo : orgInfos) {
					if (orgId.intValue() == orgInfo.getOrgId()) {
						record.setOrgName(orgInfo.getOrgName());
					}
				}
			}
		}
	    BeanUtils.copyProperties(resp, page);
	    page.setResult(list);
		return list;
	}

	@Override
	public Result updateBonusConfById(ZhengBonusConfEntity entity) {
		Result result=new Result();
		entity.setUpdateTime(new Date());
		UserLoginInfo loginUser = CommonUtil.getLoginUser();
		entity.setOperate(String.valueOf(loginUser.getId()));
		BaseResponse response = zhengBonusConfProvider.updateBonusConfById(entity);
		if(response.isStatus()){
			result.setStatus(true);
			result.setMsg("修改成功");
		}else{
			result.setStatus(false);
			result.setMsg(response.getMsg());
		}
		return result;
	}

}
