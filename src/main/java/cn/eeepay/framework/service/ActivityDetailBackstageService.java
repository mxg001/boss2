package cn.eeepay.framework.service;

import cn.eeepay.framework.model.ActivityDetailBackstage;

import java.util.List;

/**
 * Created by Administrator on 2018/1/30/030.
 * @author  liuks
 * 欢乐送,欢乐返活动延时核算清算 service
 */
public interface ActivityDetailBackstageService {

    int insertActivityDetailBackstage(ActivityDetailBackstage act);

    List<ActivityDetailBackstage>  getActivityDetailBackstage(int actId,String actState);

    void execute();

    String addJob();

    int countActivityDetailBackstage(String actState);
}
