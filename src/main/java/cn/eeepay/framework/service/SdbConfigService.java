package cn.eeepay.framework.service;

import java.util.Map;


public interface SdbConfigService {

    Map getSdbConfig(int team_id);

    int updateSdbConfigImg(int team_id, String team_ad_url);

    int addSdbConfig(int team_id, String team_ad_url);

}
