package cn.eeepay.framework.service.impl.risk;

import cn.eeepay.framework.dao.risk.SurveyReplyDao;
import cn.eeepay.framework.model.risk.FileType;
import cn.eeepay.framework.model.risk.SurveyReply;
import cn.eeepay.framework.service.risk.SurveyReplyService;
import cn.eeepay.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/9/13/013.
 * @author  liuks
 */
@Service("surveyReplyService")
public class SurveyReplyServiceImpl implements SurveyReplyService {

    @Resource
    private SurveyReplyDao surveyReplyDao;


    @Override
    public SurveyReply getMaxReply(String orderNo) {
        SurveyReply reply=surveyReplyDao.getMaxReply(orderNo);
        getReplyFileList(reply);
        return reply;
    }

    @Override
    public List<SurveyReply> getReplyList(String orderNo) {
        List<SurveyReply> list=surveyReplyDao.getReplyList(orderNo);
        if(list!=null&&list.size()>0){
            for(SurveyReply reply:list){
                getReplyFileList(reply);
            }
        }
        return list;
    }

    private void getReplyFileList(SurveyReply reply){
        if(reply!=null){
            reply.setReplyFilesList(getFileList(reply.getReplyFilesName()));
        }
    }

    @Override
    public List<FileType> getFileList(String filesName){
        if(filesName!=null&&!"".equals(filesName)){
            String[] strs=filesName.split(",");
            if(strs!=null&&strs.length>0){
                List<FileType> list=new ArrayList<FileType>();
                for(String str:strs){
                    FileType fileType=checkImg(str);
                    if(fileType!=null){
                        list.add(fileType);
                    }
                }
                return list;
            }
        }
        return null;
    }
    /**
     *
     * @param name
     * 目前只识别JPG JPEG png
     */
    private FileType checkImg(String name){
        if(StringUtils.isNotBlank(name)){
            FileType fileType=new FileType();
            fileType.setName(name);
            fileType.setType("1");//默认文件
            String suffix=name.substring(name.lastIndexOf(".")+1,name.length());
            if(suffix!=null){
                suffix=suffix.toUpperCase();
                if("JPG".equals(suffix.toUpperCase())
                        ||"JPEG".equals(suffix.toUpperCase())
                        ||"PNG".equals(suffix.toUpperCase())){
                    fileType.setType("2");
                    fileType.setImgUrl(CommonUtil.getImgUrlAgent(name));
                }
            }
            return fileType;
        }
        return null;
    }

    @Override
    public int updateNowSurveyReply(String orderNo,String status,String message) {
        SurveyReply reply=surveyReplyDao.getMaxReply(orderNo);
        if(reply!=null){
            return surveyReplyDao.updateNowSurveyReply(reply.getId(),status,message);
        }
        return 0;
    }
}
