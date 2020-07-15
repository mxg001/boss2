package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.HlfHardware;
import cn.eeepay.framework.model.SysDict;

import java.util.List;
import java.util.Map;

public interface HardwareProductService {

	List<HardwareProduct> selectAllInfo(String item);

	HardwareProduct selectHardwareName(String hardWareId);
		

		public List<HardwareProduct> selectAllHardwareName();

		int addHard(HardwareProduct hardProduct);
		
		int updateHard(HardwareProduct hardProduct);

		List<HardwareProduct> selectByParams(String typeName, String versionNu);
		
		List<HardwareProduct> selectByParamsUpdate(String typeName, String versionNu,Long hpId);
		/**
		 * 查询业务产品的硬件种类机具
		 * @param bpId
		 * @return
		 */

		public List<HardwareProduct> selectByCondition(Page<HardwareProduct> page,HardwareProduct bpd);

		public Map<String, Object> queryHardById(String id);


	/**
	 * 查询业务产品的硬件种类机具
	 * @param bpId
	 * @return
	 */
	List<HardwareProduct> findHardWareBybpId(String bpId);

	/**
	 * 获取业务产品的硬件种类机具List
	 * @return
	 */
	List<HardwareProduct> getHardwareProduct();

	/**
	 * 获取业务产品的硬件种类机具Map
	 * @return
	 */
	Map<String,String> getHardwareProductMap();

	/**
	 *获取业务产品的硬件种类机具
	 */
	HardwareProduct getHardwareProductByBpId(Long bpId);

	/**
	 *获取业务产品的硬件种类机具
	 */
	HardwareProduct getHardwareProductByBpName(String bpName);

    List<HardwareProduct> selectAllHardwareProduct(List<SysDict> sysDicts);

	List<HardwareProduct> selectAllInfoByTeamId(String teamId);

	HlfHardware isHlfHardware(String sn);
}
