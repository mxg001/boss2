package cn.eeepay.framework.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AddRequireItemDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.service.AddRequireItemService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;

@Service("addRequireItemService")
@Transactional
public class AddRequireItemServiceImpl implements AddRequireItemService {

	@Resource
	private AddRequireItemDao addRequireItemDao;
	
	@Resource
	private SeqService seqService;
	
	@Override
	public List<AddRequireItem> selectByName(Page<AddRequireItem> page,String itemName) {
		itemName=StringUtils.isBlank(itemName)?"%%":itemName+"%";
		return addRequireItemDao.selectByName(page,itemName);
	}

	@Override
	public AddRequireItem selectById(String id) {
		// TODO Auto-generated method stub
		AddRequireItem item = addRequireItemDao.selectById(id);
		if(item != null && item.getPhotoAddress()!=null){
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, item.getPhotoAddress(), new Date(64063065600000L));
			item.setPhotoAddressUrl(url);
		}
		return item;
	}

	@Override
	public int insertOrUpDate(AddRequireItem item) {
		// TODO Auto-generated method stub
		if(item==null){
			return 0;
		}
		if(item.getExampleType()!=3){
			item.setExample(null);
		}else{
			item.setPhotoAddress(null);
			item.setPhoto(null);
		}
		if(item.getItemId()!=null){
			return addRequireItemDao.update(item);
		}else{
			return addRequireItemDao.insert(item);
		}
	}
	@Override
	public AddRequireItem selectByMeriId(String meriId) {
		return addRequireItemDao.selectByMeriId(meriId);
	}

	@Override
	public AddRequireItem selectRequireName(String requireId) {
		return addRequireItemDao.selectRequireName(requireId);
	}

	@Override
	public List<AddRequireItem> selectAllRequireName() {
		return addRequireItemDao.selectAllRequireName();
	}

	@Override
	public List<AddRequireItem> queryItemNameList(Page<TerminalInfo> page) {
		return addRequireItemDao.queryItemNameList(page);
	}

	@Override
	public List<AddRequireItem> selectItemByBpId(String bpId) {
		return addRequireItemDao.selectItemByBpId(bpId);
	}
}
