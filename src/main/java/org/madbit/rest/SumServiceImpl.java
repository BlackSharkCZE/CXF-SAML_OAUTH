package org.madbit.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 13.8.15<br/>
 * Time: 13:35<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class SumServiceImpl implements SumService {

	private static final Logger logger = LoggerFactory.getLogger(SumServiceImpl.class);

	@Override
	public SumRequest calculateSum(SumRequest request) {
		logger.info("********************************");
		logger.info("**** CALCULATE SUM EXECUTED ****");
		logger.info("********************************");
		if (request != null) {
			if (request.getItems() != null) {
				if (!request.getItems().isEmpty()) {
					for (Integer integer : request.getItems()) {
						request.setSum(request.getSum() + integer);
					}
				}
			}
		}
		return request;
	}

	@Override
	public SumRequest getEmpty() {
		return new SumRequest();
	}

}
