package sk.anext.dev.inflate;

import org.apache.xml.security.utils.Base64;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Inflater;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 19.8.15<br/>
 * Time: 11:50<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class InflateTest {

	private static final String DEFLATED_ASSERTION_FILE = "/deflated_assertion.b64";

	@Test
	public void testInflate() throws Exception {

		final Path path = Paths.get(getClass().getResource(DEFLATED_ASSERTION_FILE).toURI());
		assertNotNull("Can not find file: " + DEFLATED_ASSERTION_FILE, path);

		final byte[] base64deflatedData = Files.readAllBytes(path);
		assertTrue("No bytes read from " + DEFLATED_ASSERTION_FILE, base64deflatedData.length > 0);

		final byte[] binAssertionDeflated = Base64.decode(base64deflatedData);
		assertTrue("Can not decode base64 deflated data! " + DEFLATED_ASSERTION_FILE, binAssertionDeflated.length > 0);


		Inflater inflater = new Inflater(true);
		inflater.setInput(binAssertionDeflated);

		final byte inflatedBytes[] = new byte[binAssertionDeflated.length * 2];
		final int inflate = inflater.inflate(inflatedBytes);

		final byte[] samlAssertion = new byte[inflate];
		System.arraycopy(inflatedBytes,0, samlAssertion,0, inflate);

		System.out.println(new String(samlAssertion));

	}
}
