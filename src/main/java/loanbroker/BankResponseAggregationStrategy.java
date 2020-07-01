package loanbroker;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BankResponseAggregationStrategy implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		if (oldExchange == null) {
			obj.put("count", 1);
			newExchange.getIn().setBody(obj.toString());
			return newExchange;
		} else {
			try {
				int count = mapper.readTree(oldExchange.getIn().getBody(String.class)).get("count").asInt();
				obj.put("count", ++count);
				newExchange.getIn().setBody(obj.toString());
				return newExchange;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return null;
		
		
//		// the first time we only have the new exchange
//		if (oldExchange == null) {
//			return newExchange;
//		}
//		
//		return newExchange;
	}

	

}