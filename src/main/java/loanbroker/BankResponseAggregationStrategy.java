package loanbroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BankResponseAggregationStrategy implements AggregationStrategy, Predicate {

	/**
	 * @param oldExchange aggregation so far
	 * @param newExchange new incoming message
	 */
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {


		// aggregated message:
		/*
		{
			Bank Teil: das beste angebot nehmen
        	"monthlyPremiums" : 123.4,
        	"durationInMonths" : 1,
        	"grantedCredit" : 123.4,
        	"interestRatePerMonth" : 123.4,
			Aggregation Teil:
			"expectedResponseCount" : 1,
			"receivedResponseCount" : 1,
		}
		 */
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode newNode = mapper.readTree(newExchange.getIn().getBody(String.class));
			ObjectNode newObj = (ObjectNode)newNode;

			if (oldExchange == null)
			{
				newObj.put("expectedResponseCount",ExpectedResponseDatabase.getInstance().map.get(newNode.get("correlationId").asInt()));
				newObj.put("receivedResponseCount",1);
				newExchange.getIn().setBody(newObj.toString());
				return newExchange;
			}

			JsonNode oldNode = mapper.readTree(oldExchange.getIn().getBody(String.class));
			ObjectNode oldObj = (ObjectNode)oldNode;

			double oldInterest = oldObj.get("interestRatePerMonth").asDouble();
			double newInterest = newObj.get("interestRatePerMonth").asDouble();

			if(oldInterest <= newInterest){
				oldObj.put("receivedResponseCount",oldObj.get("receivedResponseCount").asInt() +1);
				oldObj.put("expectedResponseCount",ExpectedResponseDatabase.getInstance().map.get(newNode.get("correlationId").asInt()));
				newExchange.getIn().setBody(oldObj.toString());
			} else {
				newObj.put("receivedResponseCount",oldObj.get("receivedResponseCount").asInt() +1);
				newObj.put("expectedResponseCount",ExpectedResponseDatabase.getInstance().map.get(newNode.get("correlationId").asInt()));
				newExchange.getIn().setBody(newObj.toString());
			}
			return newExchange;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean matches(Exchange exchange) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode node = mapper.readTree(exchange.getIn().getBody(String.class));
			int expected = node.get("expectedResponseCount").asInt();
			int received = node.get("receivedResponseCount").asInt();
			return expected == received;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return true;
	}
}