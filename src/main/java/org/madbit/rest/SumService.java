package org.madbit.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 13.8.15<br/>
 * Time: 13:32<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
@Path("RestMyService")
public interface SumService {

	@POST
	@Path("sum")
	SumRequest calculateSum(SumRequest request);

	@GET
	@Path("empty")
	@Produces(MediaType.APPLICATION_JSON)
	SumRequest getEmpty();
}
