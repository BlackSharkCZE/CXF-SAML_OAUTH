package org.madbit.rest;

import org.apache.cxf.interceptor.Fault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.List;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 13.8.15<br/>
 * Time: 14:25<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class OAuthRestFilter implements ContainerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(OAuthRestFilter.class);
	private static final String OAUTH_TOKEN_TYPE = "Bearer";

	private ResourceServerTokenServices resourceServerTokenServices;
	private GemSamlHeaderInHandler samlHeaderInHandler;


	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		final List<String> headers = containerRequestContext.getHeaders().get(HttpHeaders.AUTHORIZATION);
		if (headers != null && !headers.isEmpty() && headers.size() == 1) {
			final String parts[] = headers.get(0).split(" ");
			if (parts.length>=2 && parts[0].equalsIgnoreCase(OAUTH_TOKEN_TYPE)) {
				logger.info("Processing oAuth Token Verification.");
				final OAuth2Authentication oAuth2Authentication = resourceServerTokenServices.loadAuthentication(parts[1].trim());
				return;
			} else {
				if (parts.length>=2 && parts[0].equalsIgnoreCase("SAML")) {
					logger.info("Processing SAML Token Verification. Support deflate: " + samlHeaderInHandler.useDeflateEncoding());
					samlHeaderInHandler.filter(containerRequestContext);
					return;
				}
			}
		}
		throw new Fault(new Exception("Unauthorized! No valid SAML or oAuth token!"));
	}

	public ResourceServerTokenServices getResourceServerTokenServices() {
		return resourceServerTokenServices;
	}

	public void setResourceServerTokenServices(ResourceServerTokenServices resourceServerTokenServices) {
		this.resourceServerTokenServices = resourceServerTokenServices;
	}

	public GemSamlHeaderInHandler getSamlHeaderInHandler() {
		return samlHeaderInHandler;
	}

	public void setGemSamlHeaderInHandler(GemSamlHeaderInHandler samlHeaderInHandler) {
		this.samlHeaderInHandler = samlHeaderInHandler;
	}
}
