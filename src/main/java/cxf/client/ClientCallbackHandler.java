package cxf.client;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 14.8.15<br/>
 * Time: 13:59<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class ClientCallbackHandler implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("handle(Callback[] callbacks) >> " + (callbacks == null ? "NULL" : callbacks.length) );
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof WSPasswordCallback) {
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
				System.out.println("####: " + pc.getIdentifier());
				if ("iamtech".equals(pc.getIdentifier())) {
					pc.setPassword("changeit");
				} else {
					if ("sts".equals(pc.getIdentifier())) {
						pc.setPassword("changeit");
					} else {
						pc.setPassword("changeit");
					}
				}
			}
		}
	}

}
