package sk.anext.dev;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.junit.Test;
import org.madbit.rest.SumRequest;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 20.8.15<br/>
 * Time: 15:18<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class RestClientTest {

	private final String OAUTH_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0NDAxMDYwOTIsImF1ZCI6InR5cGVzcmVwbyIsImlzcyI6Imh0dHA6XC9cL2loczEubWV0YWlzMi5hbnguc2s6ODA4MVwvbWV0YWlzaWFtXC8iLCJqdGkiOiJhMmZlNjA0Yi0xOTQ1LTQ0ZjYtYTM0ZC02M2Y2Yzc4MDUyYzUiLCJpYXQiOjE0NDAwNzcyOTJ9.NnJOw403KTiiJ8vMVX61dpytdqT2Q30n4ICODVirsITdBjDYNYXSgzLeXLi7N068abQ0GrD7XYhjAtZxVMvj29h3jconMp-xVdLjbBNUqe8sqK4OcS3VR8YqU-C2ZO_umdSLHtwQ9Tn_Ma8DbUnARh43fD_Huj9_vNPdxYNWmGg";

	private static final String SRC_DATA_FILE_JSON = "/META-INF/erl/request.json";
	private static final String REST_URL_PATH = "http://localhost:8080/rest/RestMyService/RestMyService";
	private static final String REST_METHOD = "/sum";


	@Test
	public void testRestClientTest() throws Exception {
		List<Object> providers = new ArrayList<>();
		final JSONProvider jsonProvider = new JSONProvider();
		jsonProvider.setDropCollectionWrapperElement(true);
		jsonProvider.setDropRootElement(true);
		jsonProvider.setSerializeAsArray(true);
		jsonProvider.setSupportUnwrapped(true);
		providers.add(jsonProvider);

		WebClient client = WebClient.create(REST_URL_PATH, providers);
		client = client.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + OAUTH_TOKEN)
				.path(REST_METHOD);

		final byte[] bytes = Files.readAllBytes(Paths.get(getClass().getResource(SRC_DATA_FILE_JSON).toURI()));
		assertTrue("Soubor s daty k odeslani nebyl nacteny", bytes.length > 0);
		SumRequest res = client.post(new String(bytes), SumRequest.class);
		assertNotNull(res);
		assertEquals(res.getSum().compareTo(10L), 0);

	}
}
