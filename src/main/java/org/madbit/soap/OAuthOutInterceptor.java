package org.madbit.soap;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 18.8.15<br/>
 * Time: 11:17<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class OAuthOutInterceptor extends AbstractPhaseInterceptor<Message> {

	public OAuthOutInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}


	@Override
	public void handleMessage(Message message) throws Fault {
		final Object append_timestamp = message.get("APPEND_TIMESTAMP");
		System.out.println(append_timestamp);
	}
}
