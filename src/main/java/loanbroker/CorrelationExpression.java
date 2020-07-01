package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

public class CorrelationExpression implements Expression {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		
		System.out.println("CorrelationExpression:");
		System.out.println(exchange.getIn().getBody(String.class));
		
		return (T) new String("Test");
	}

}
