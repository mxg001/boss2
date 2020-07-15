package cn.eeepay.framework.model.cjt;

/**
 * @author tans
 * @date 2019/6/3 15:53
 */
public class CjtTeamHardware {

    private Integer id;
    private Integer teamId;//组织ID
    private Integer hpId;//硬件产品类型ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getHpId() {
        return hpId;
    }

    public void setHpId(Integer hpId) {
        this.hpId = hpId;
    }
}
