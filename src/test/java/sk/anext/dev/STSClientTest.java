package sk.anext.dev;

import cxf.client.gen.MyService_Service;
import cxf.client.gen.SumRequest;
import cxf.client.gen.SumResponse;
import org.apache.cxf.ws.security.trust.STSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.8.15<br/>
 * Time: 9:11<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(classes = CXFClientSecConfig.class)
public class STSClientTest {

	/*@Autowired
	private MyService service;*/

	@Autowired
	private MyService_Service service;

	@Autowired
	private STSClient stsClient;

	@Test
	public void testAutowired() throws Exception {
		assertNotNull(service);
		assertNotNull(stsClient);
	}

	@Test
	public void testSoapCall() throws Exception {
		final SumRequest parameters = new SumRequest();
		assertNotNull(parameters.getAddend());
		parameters.getAddend().clear();
		parameters.getAddend().addAll(Arrays.asList(1, 2, 3, 4));
		final SumResponse sum = service.getMyServiceSOAP().sum(parameters);
		assertEquals(10, sum.getSum());
	}

}
