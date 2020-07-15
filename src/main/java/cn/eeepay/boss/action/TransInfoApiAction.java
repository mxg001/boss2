package cn.eeepay.boss.action;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import cn.eeepay.framework.model.TransInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.MerchantRequireItemService;
import cn.eeepay.framework.service.PosCardBinService;
import cn.eeepay.framework.service.RiskRollService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TransInfoFreezeNewLogService;
import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;

/**
 * 交易相关接口 api
 * @author zouruijin
 * zrj@eeepay.cn rjzou@qq.com
 * 2017年2月24日13:03:13
 *
 */
@Controller
@RequestMapping(value="/transInfoApiAction")
public class TransInfoApiAction {
	private static final Logger log = LoggerFactory.getLogger(TransInfoApiAction.class);
	
	@Resource
	private SysDictService sysDictService;
	
	@Resource
	private TransInfoService transInfoService;
	
	@Resource
	public AgentInfoService agentInfoService;
	
	@Resource
	public PosCardBinService posCardBinService;
	
	@Resource
	public TransInfoFreezeNewLogService transInfoFreezeNewLogService;
	
	@Resource
	private MerchantRequireItemService merchantRequireItemService;
	
	@Resource
	private RiskRollService riskRollService;

	/**
	 * 冻结交易
	 * @param token
	 * @return 
	 * @throws Exception
	 * @author transInfoAction/freezeTrans.do
	 */
	@RequestMapping("/freezeTrans.do")
    @ResponseBody
    public Map<String,Object> freezeTrans(@RequestParam String token) throws Exception{
		log.info("freezeTrans token:"+token);
    	Map<String,Object> msg=new HashMap<>();
    	msg.put("name", "冻结/解冻交易");
    	final String secret = Constants.ACCOUNT_API_SECURITY;
    	String acqEnname=null;
    	String orderNo=null;
    	String freezeStatus=null;
    	boolean isReturn = false;
    	try {
    		 final JWTVerifier verifier = new JWTVerifier(secret);
             final Map<String,Object> claims= verifier.verify(token);
             orderNo=(String)claims.get("transOrderNo");
             freezeStatus=(String)claims.get("freezeStatus");
             acqEnname=(String)claims.get("acqEnname");
//	         String jti = (String) claims.get("jti");
//	         long exp = Long.valueOf(claims.get("exp").toString());
//	         long iat = Long.valueOf(claims.get("iat").toString());
//	         String key = accountType+":"+userId+":"+jti;
//	         long expireTime = exp - iat;
//	         if(redisService.exists(key))
//	         {
//	         	 //返回 401
//	        	 throw new JWTVerifyException("Invalid Token");
//	         }
//	         else{
//	        	 redisService.insertSet(key, "1", expireTime);
//	         }
             for (Map.Entry<String, Object> entry : claims.entrySet()){
 	    		log.info("{}={}", new Object[]{entry.getKey(),entry.getValue()});
 	    	 }
		} catch (JWTVerifyException | InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException | IOException e) {
			log.debug(e.getMessage());
        	msg.put("status",false);
			msg.put("msg",e.getMessage());
			return msg;
		}
    	if (StringUtils.isBlank(orderNo)) {
			isReturn = true;
			msg.put("status", false);
			msg.put("msg", "orderNo参数不能为空");
			msg.put("timestamp", String.valueOf(new Date().getTime()));
			log.error(msg.toString());
		} else if (StringUtils.isBlank(freezeStatus)) {
			isReturn = true;
			msg.put("status", false);
			msg.put("msg", "freezeStatus参数不能为空");
			msg.put("timestamp", String.valueOf(new Date().getTime()));
			log.error(msg.toString());
		}
    	if (isReturn) {
			return msg;
		}
    	try {
    		int i = 0;
//    		if ("neweptok".equals(acqEnname)) {
//    			String acqReferenceNo = orderNo;
//    			TransInfo ti= transInfoService.findTransInfoByAcqReferenceNo(acqReferenceNo);
//    			if (ti != null) {
//    				orderNo = ti.getOrderNo();
//				}
//    			else{
//    				msg.put("status",true);
//         			msg.put("msg","订单号不存在");
//         			msg.put("timestamp",String.valueOf(DateUtil.dateToUnixTimestamp()));
//         			log.info(msg.toString());
//         			return msg;
//    			}
//    			
//			}
    		i = transInfoService.updateFreezeStatusByOrderNo(orderNo, freezeStatus);
    		if(i > 0){
    			msg.put("status",true);
     			msg.put("msg","冻结成功");
     			msg.put("timestamp",String.valueOf(DateUtil.dateToUnixTimestamp()));
     			log.info(msg.toString());
    		}
    		else{
    			msg.put("status",true);
     			msg.put("msg","订单号不存在");
     			msg.put("timestamp",String.valueOf(DateUtil.dateToUnixTimestamp()));
     			log.info(msg.toString());
    		}
		} catch (Exception e) {
			msg.put("status",false);
 			msg.put("msg",e.getMessage());
 			log.error(msg.toString());
		}
    	return msg;
	}
}
