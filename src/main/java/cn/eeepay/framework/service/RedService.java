package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 超级银行家-红包
 * @author tans
 * @date 2018-1-17
 */
public interface RedService {

    /**
     * 红包业务管理
     * @param page
     * @return
     */
    List<RedControl> selectRedControl(Page<RedControl> page);

    /**
     * 修改红包业务开关状态
     * @param redControl
     * @return
     */
    Result updateRedOpen(RedControl redControl);

    /**
     * 修改红包业务是否开启组织控制
     * @param redControl
     * @return
     */
    int updateRedOrg(RedControl redControl);

    /**
     * 红包业务组织管理
     * @param redOrg
     * @param page
     * @return
     */
    List<RedOrg> selectRedOrg(RedOrg redOrg, Page<RedOrg> page);

    /**
     * 删除红包业务组织
     * @param id
     * @return
     */
    Result deleteRedOrg(String id);

    /**
     * 红包业务组织管理-批量导入
     * @param file
     * @param busCode
     * @return
     */
    Result importRedOrg(MultipartFile file, String busCode);

    /**
     * 新增红包业务组织
     * @param redOrg
     * @return
     */
    Result addRedOrg(RedOrg redOrg);

    Result redConfig(Map<String,Object> params,Page<Map<String,Object>> page);
    int  addRedConfig(Map<String,Object> params);

    Map<String,Object> redConfigInfo(String id);

    int  redConfigInfoUpdate(Map params);

    int updateRedStatus(String id, String status);

    /**
     * 检查组织是否已存在
     * @param redOrg
     * @return
     */
    boolean checkExists(RedOrg redOrg);

    /**
     * 修改个人发放红包配置
     * @param conf
     * @return
     */
    int updateRedUserConf(RedUserConf conf);

    /**
     * 获取个人发放红包配置
     * @return
     */
    RedUserConf getRedUserConf();

    Map<String,Object> redProductInfo(String type);

    int redProductInfoUpdate(Map params);

    RedLuckConf getLuckConf();

    Result updateLuckConf(RedLuckConf baseInfo);

    int configUseStatus(String id);

    List<RedAccountDetail> selectAccountDetailPage(RedAccountDetail baseInfo, Page<RedAccountDetail> page);
    
    List<RedAccountDetail> selectAccountDetailPageReload(RedAccountDetail baseInfo, Page<RedAccountDetail> page);
    
    List<ManorAccountDetail> selectManorAccountDetailPage(ManorAccountDetail baseInfo, Page<ManorAccountDetail> page);

    RedAccountInfo plateAccountInfo();
    
    RedAccountInfo plateAccountInfoReload(Long id);

    void exportPlateAccountDetail(HttpServletResponse response, RedAccountDetail detail);
    void exportRedAccountDetail(HttpServletResponse response, RedAccountDetail detail);

    void exportPlateAccountDetail(HttpServletResponse response, ManorAccountDetail detail);

    Result  openOem();

    RedControl  selectRedControlByPrimaryKey(Long id);
    RedAccountDetail findAccountDetailSum(RedAccountDetail baseInfo);

    RedAccountDetail selectAccountDetailSum(RedAccountDetail baseInfo);
    
    ManorAccountDetail selectManorAccountDetailSum(ManorAccountDetail baseInfo);

    String getSysPinfuChajuKey(String key);

	void exportManorTransactionRecore(HttpServletResponse response, ManorTransactionRecore detail);
	
	/**
	 * 查询红包业务组织管理
	 * @param redOrg
	 * @return
	 */
	List<RedOrg> selectRedOrgListAll(RedOrg redOrg);

	/**
	 * 查询红包业务组织分类排序
	 * @param redOrgSort
	 * @param page
	 */
	List<RedOrgSort> selectRedOrgSort(RedOrgSort redOrgSort, Page<RedOrgSort> page);

	/**
	 * 删除红包业务组织分类排序
	 * @param redOrgSort
	 * @return
	 */
	int deleteRedOrgSort(RedOrgSort redOrgSort);
	
	/**
	 * 新增红包业务组织分类排序
	 * @param redOrgSort
	 * @return
	 */
	int addRedOrgSort(RedOrgSort redOrgSort);
	
	/**
	 * 修改红包业务组织分类排序
	 * @param redOrgSort
	 * @return
	 */
	int editRedOrgSort(RedOrgSort redOrgSort);
	
	/**
	 * 	忽略型新增红包组织信息数据(存在则忽略,不存在则新增)
	 * @param redOrg
	 * @return
	 */
	int ignoreAddRedOrg(RedOrg redOrg);
	
	/**
	 * 
	 * @param file
	 * @param busCode
	 * @return
	 */
    Result importRedOrgSort(MultipartFile file, String busCode,Integer orgStatus);
}
