package cn.eeepay.framework.service.risk;

import cn.eeepay.framework.model.risk.FileType;
import cn.eeepay.framework.model.risk.SurveyReply;

import java.util.List;

/**
 * Created by Administrator on 2018/9/13/013.
 * @author  liuks
 */
public interface SurveyReplyService {

    SurveyReply getMaxReply(String orderNo);

    List<SurveyReply> getReplyList(String orderNo);

    List<FileType> getFileList(String filesName);

    int updateNowSurveyReply(String orderNo,String status,String message);
}
