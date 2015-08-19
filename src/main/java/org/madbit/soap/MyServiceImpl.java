package org.madbit.soap;


import cxf.client.gen.MyService;
import cxf.client.gen.SumRequest;
import cxf.client.gen.SumResponse;

public class MyServiceImpl implements MyService {

	public SumResponse sum(SumRequest parameters) {
		int sum = 0;
		for(Integer i: parameters.getAddend()){
			sum += i;
		}
		SumResponse response = new SumResponse();
		response.setSum(sum);
		return response;
	}

}
