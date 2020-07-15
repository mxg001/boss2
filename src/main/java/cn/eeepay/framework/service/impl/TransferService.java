package cn.eeepay.framework.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.util.Constants;

/**
 * @author 沙
 * T0 出款服务接口
 */
@Service("transferService")
public class TransferService {
	private static final Logger log = LoggerFactory.getLogger(TransferService.class);
	private static final ExecutorService threadpool = Executors.newFixedThreadPool(15);
	private static final Async async = Async.newInstance().use(threadpool);
	Queue<Future<Content>> queue = new LinkedList<Future<Content>>();
	public void sender(final List<String[]> param) throws Exception{
		queue.add(async.execute(Request.Post(Constants.NOWTRANSFER_HOST).body(setParam(param)), new FutureCallback<Content>() {
            @Override
            public void failed(final Exception ex) {
               log.error("出款服务失败，",ex);
            }

            @Override
            public void completed(final Content content) {
               log.error("出款服务完成，",content.toString());
               queue.remove();
            }

            @Override
            public void cancelled() {
            	log.error("出款服务取消，参数[{}]",param.toString());
            }
        }));
	}
	
	private UrlEncodedFormEntity setParam(List<String[]> param) throws UnsupportedEncodingException{
		List<NameValuePair> list=new ArrayList<NameValuePair>();
		for(String[] nameValue:param){
			list.add(new BasicNameValuePair(nameValue[0], nameValue[1]));
		}
		return new UrlEncodedFormEntity(list, "utf-8");
	}
}
