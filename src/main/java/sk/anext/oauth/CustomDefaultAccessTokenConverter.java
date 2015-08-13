package sk.anext.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.*;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 30.7.15<br/>
 * Time: 11:23<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class CustomDefaultAccessTokenConverter extends DefaultAccessTokenConverter {

	private UserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();

	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		String clientId = (String) map.get(CLIENT_ID);

		Map<String, String> parameters = new HashMap<>();
		parameters.put(CLIENT_ID, clientId);

		Set<String> scope = new LinkedHashSet<>(
				map.containsKey(SCOPE) ? (Collection<String>) Arrays.asList(map.get(SCOPE).toString().split(" ")) : Collections.<String>emptySet()
		);

		@SuppressWarnings("unchecked")
		Set<String> resourceIds = new LinkedHashSet<>(
				map.containsKey(AUD) ? (Collection<String>) map.get(AUD) : Collections.<String>emptySet()
		);

		Authentication user = userTokenConverter.extractAuthentication(map);
		OAuth2Request request = new OAuth2Request(parameters, clientId, null, true, scope, resourceIds, null, null, null);

		return new OAuth2Authentication(request, user);
	}

}
