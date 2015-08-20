package org.madbit.rest;

import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.saml.AbstractSamlBase64InHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.InflaterInputStream;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 19.8.15<br/>
 * Time: 12:54<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public abstract class GemAbstractSamlBase64InHandler extends AbstractSamlBase64InHandler {

	protected void handleToken(Message message, String assertion) {
		// the assumption here is that saml:Assertion is directly available, however, it
		// may be contained inside saml:Response or saml:ArtifactResponse/saml:Response
		if (assertion == null) {
			throwFault("SAML assertion is not available", null);
		}

		try {
			byte[] deflatedToken = Base64Utility.decode(assertion);

			InputStream is = useDeflateEncoding()
					? fixInflate(deflatedToken)
					: new ByteArrayInputStream(deflatedToken);
			validateToken(message, is);
		} catch (Base64Exception ex) {
			throwFault("Base64 decoding has failed", ex);
		} catch (DataFormatException ex) {
			throwFault("Encoded assertion can not be inflated", ex);
		}
	}



	private InputStream fixInflate(byte[] binAssertionDeflated) throws DataFormatException {
		return new InflaterInputStream(new ByteArrayInputStream(binAssertionDeflated));
	}

}
