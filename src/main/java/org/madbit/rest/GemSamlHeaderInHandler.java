package org.madbit.rest;

import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 19.8.15<br/>
 * Time: 11:07<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class GemSamlHeaderInHandler extends GemAbstractSamlBase64InHandler {

	private static final String SAML_AUTH = "SAML";

	public GemSamlHeaderInHandler() {
	}

	@Override
	public void filter(ContainerRequestContext context) {
		final
		Message message = JAXRSUtils.getCurrentMessage();
		final List<String> values = context.getHeaders().get(HttpHeaders.AUTHORIZATION);
		if (values == null || values.size() != 1 || !values.get(0).startsWith(SAML_AUTH)) {
			throwFault("Authorization header must be available and use SAML profile", null);
		}

		String[] parts = StringUtils.split(values.get(0), " ");
		if (parts.length != 2) {
			throwFault("Authorization header is malformed", null);
		}

		handleToken(message, parts[1]);
	}

}
