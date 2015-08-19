package org.madbit.soap;

import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapActionInInterceptor;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.util.List;
import java.util.Map;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.8.15<br/>
 * Time: 13:57<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class RemoveSoapActionInInterceptor extends AbstractSoapInterceptor {

	public RemoveSoapActionInInterceptor() {
		super(Phase.READ);
		addBefore(SoapActionInInterceptor.class.getName());
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		if (message.getVersion() instanceof Soap11) {
			Map<String, List<String>> headers = CastUtils.cast((Map) message.get(Message.PROTOCOL_HEADERS));
			headers.remove("SOAPAction");
		}
	}

}
