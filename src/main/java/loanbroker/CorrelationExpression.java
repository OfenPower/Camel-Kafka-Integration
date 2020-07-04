package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * CorrelationExpression fürs Aggregate, welche die "correltationId" einer Message
 * zurückliefert, damit der Aggregator die Eingangsmessages damit zuordnen kann
 */
public class CorrelationExpression implements Expression {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		
		String jsonString = exchange.getIn().getBody(String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = null;
		try {
			node = mapper.readTree(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		String id = node.get("correlationId").asText();
		return (T) id;
	}

}
