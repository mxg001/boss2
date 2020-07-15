package tools;
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.eeepay.framework.service.AgentShareRuleJob;
import cn.eeepay.framework.service.OutAccountServiceRateJob;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
public class BaseTest extends AbstractJUnit4SpringContextTests {
//	@Resource
//	private Profit profit;
	
	@Resource
	private AgentShareRuleJob testTask;
	@Resource
	private OutAccountServiceRateJob outAccountServiceRateJob;
//	@Test
//	public void testProfie() throws Exception{
//		System.out.println(profit.profit("105", "183", 0, 2, new BigDecimal("30000"), new BigDecimal("1000")));
//	}
	
	@Test
	public void testReplaceAcqServiceRate() throws Exception{
		testTask.execute();
	}
	
	@Test
	public void testOutAccountServiceRateJob () throws Exception {
		outAccountServiceRateJob.execute();
	}
}
