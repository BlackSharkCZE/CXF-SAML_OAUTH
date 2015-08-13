package org.madbit.soap;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 13.8.15<br/>
 * Time: 10:09<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class OAuthInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger logger = LoggerFactory.getLogger(OAuthInterceptor.class);

	private static final String AUTHORIZATION_TOKEN_HEADER = "Authorization";
	private ResourceServerTokenServices resourceServerTokenServices;


	public OAuthInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}

	public void handleMessage(Message message) throws Fault {
		logger.info("OAuthInterceptor: " + message);
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		final String authorizationHeader = request.getHeader(AUTHORIZATION_TOKEN_HEADER);

		if (authorizationHeader != null) {
			// Authorization HTTP header is present. Try validate oAuth token
			logger.debug("Authorization header is present. Try validate OAuth token");

			String parts[] = authorizationHeader.split(" ");
			if (parts[0].trim().equalsIgnoreCase("Bearer")) {
				try {
					final OAuth2Authentication oAuth2Authentication = resourceServerTokenServices.loadAuthentication(parts[1].trim());
					logger.debug("Token authentication: " + oAuth2Authentication);
				} catch (Exception e) {
					logger.error("oAuth token validation failed!", e);
					throw new Fault(e);
				}

			} else {
				// Invalid oAuth token. Expected Bearer
				logger.warn("Invalid token type. Require Bearer. Authorization: " + authorizationHeader);
				throw new Fault(new Exception("Invalid token. Require Bearer! Current token type: " + parts[0]));
			}

		} else {
			logger.debug("Authorization Header is not present. Execute WSS4JInInterceptor!");
			// oAuth token is not present in http headers. Delegate validation to WSS4J Interceptor
			final HashMap<String, Object> key = new HashMap<String, Object>() {{
				put("action","SAMLTokenSigned" ); // validate SAML token signature
			}};
			final WSS4JInInterceptor wss4JInInterceptor = new WSS4JInInterceptor(key);
			message.getInterceptorChain().add(wss4JInInterceptor);
		}

	}

	public ResourceServerTokenServices getResourceServerTokenServices() {
		return resourceServerTokenServices;
	}

	public void setResourceServerTokenServices(ResourceServerTokenServices resourceServerTokenServices) {
		this.resourceServerTokenServices = resourceServerTokenServices;
	}
}
