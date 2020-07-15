package cn.eeepay.boss.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.util.ResponseUtil;

/**
 * 文件下载
 * @author tans
 * @date 2017年8月21日 上午9:58:31
 */
@Controller
@RequestMapping(value="/downLoad")
public class DownLoadAction {
	
	private Logger log = LoggerFactory.getLogger(DownLoadAction.class);
	
	/**
	 * 批量新增模板
	 */
	@RequestMapping("/template")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("templateName") String templateName, @RequestParam("type") String type){
		log.info("下载模板，templateName={}，type={}", templateName, type);
		if(StringUtils.isBlank(templateName) || StringUtils.isBlank(type)){
			return null;
		}
		type = "." + type;
		String filePath = request.getServletContext().getRealPath("/")+File.separator+"template"+File.separator+templateName + type;
		log.info(filePath);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
  		switch (templateName) {
			case "invitePrizesAddAgent":
				templateName = "邀请有奖批量添加代理商";
				break;
			default:
//				templateName = sdf.format(new Date());
				break;
		}
  		ResponseUtil.download(response, filePath,templateName + type);
  		return null;
	} 

}
