package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

public class CorrelationExpression implements Expression {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		
		System.out.println("Correlation Expression:");
		System.out.println(exchange.getIn().getBody(String.class));
		System.out.println(exchange.getIn().getHeader("type"));
		System.out.println(exchange.getIn().getHeader("corrId"));
		
		return (T) new String("Test");
	}

}
