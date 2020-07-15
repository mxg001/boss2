package cn.eeepay.framework.service.pushManager;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.pushManager.PushManager;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PushManagerService {
    /***
     *  获取商户类型
     * @param apply
     * @return
     */
    List<Map<String, String>> getAppInfo(String apply);

    /***
     * 查询推送管理
     * @param page
     * @param pm
     */
    void selectPushManagerByCondition(Page<PushManager> page, PushManager pm);

    /***
     * 保存或者更新推送管理
     * @param pm
     * @param msgMap
     */
    void saveOrUpdatePushManager(PushManager pm, Map<String,Object> msgMap);

    /***
     * 删除推送管理
     * @param id
     */
    void delPushManagerById(Long id,Map<String, Object> msgMap);

    /***
     * 根据id获取推送内容
     * @param id
     * @return
     */
    PushManager getByPushManagerId(Long id,Map<String, Object> msgMap);

    /***
     * 从excel导入商户信息
     * @param file
     * @param pushId
     * @return
     */
    Map<String, Object> importPushManagerFromExcel(MultipartFile file,Long pushId) throws EncryptedDocumentException, InvalidFormatException, IOException;

    /***
     * 测试推送功能
     * @param merchantNo
     * @param pushId
     * @param msgMap
     */
    void previewPushManager(String merchantNo, Long pushId, Map<String, Object> msgMap);

    /***
     * 准备推送信息 实际上是更新推送时间 等待定时任务扫描进行推送
     * @param id
     * @param msgMap
     */
    void tuPush(Long id, Map<String, Object> msgMap);

    List<PushManager> getPushManager(PushManager pm);

    String getPushObjName(String pushObj);

    /***
     * 判断当前推送内容是否已经导入了商户信息
     * @param msgMap
     * @param pushId
     * @return
     */
    void checkCanPush(Map<String, Object> msgMap, String pushId);
}
