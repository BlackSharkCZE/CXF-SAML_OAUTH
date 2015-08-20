package org.madbit.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 13.8.15<br/>
 * Time: 13:33<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SumRequest {

	@XmlElement(name = "items")
	private List<Integer> items;
	@XmlElement(name = "sum")
	private Long sum = 0L;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SumRequest{");
		sb.append("items=").append(items);
		sb.append(", sum=").append(sum);
		sb.append('}');
		return sb.toString();
	}

	public List<Integer> getItems() {
		return items;
	}

	public void setItems(List<Integer> items) {
		this.items = items;
	}

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}
}
