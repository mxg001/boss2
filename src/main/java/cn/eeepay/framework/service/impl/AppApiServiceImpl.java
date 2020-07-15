package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AppInfoDao;
import cn.eeepay.framework.model.MobileVerInfo;
import cn.eeepay.framework.service.AppApiService;
import cn.eeepay.framework.util.BossBaseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service("appApiService")
public class AppApiServiceImpl implements AppApiService {

    @Resource
    private AppInfoDao appInfoDao;

    /**
     * 根据当前版本获取需要更新的版本
     * @param currentVer
     * @return
     */
    @Override
    public MobileVerInfo getVersion(MobileVerInfo currentVer) {
        MobileVerInfo verInfo = appInfoDao.getVersion(currentVer);
        if(verInfo == null){
            throw new BossBaseException("找不到对应的版本信息");
        }
        //如果是IOS，当前版本在1.0.1及以下，都不返回（兼容旧版的判断）
        if(1 == currentVer.getPlatform()){
            if(compareVersion("1.0.2", currentVer.getVersion())){
                return null;
            }
        }
        return verInfo;
    }

    /**
     * 比较版本号大小
     * @param currentVer
     * @param version
     * @return
     */
    private boolean compareVersion(String version, String currentVer) {
        Long curLong = getVersionLong(currentVer);
        Long verLong = getVersionLong(version);
        return verLong > curLong;
    }

    /**
     * 根据版本号转换为Long
     * @param version
     * @return
     */
    private Long getVersionLong(String version) {
        Long one = 100L;
        Long two = 10000L;
        String[] verArr = version.split("\\.");
        System.out.println(verArr.length);
        Long verLong = Integer.parseInt(verArr[0]) * two
                + Integer.parseInt(verArr[1]) * one
                + Integer.parseInt(verArr[2]);
        return verLong;
    }

//    @Test
//    public void test1(){
//        Long aaa = getVersionLong("1.0.0");
//        System.out.println(aaa);
//    }
}
