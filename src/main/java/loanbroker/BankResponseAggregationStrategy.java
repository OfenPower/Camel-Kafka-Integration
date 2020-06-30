package loanbroker;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class BankResponseAggregationStrategy implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		// the first time we only have the new exchange
		if (oldExchange == null) {
			return newExchange;
		}

//		Double oldQuote = oldExchange.getIn().getHeader(Constants.PROPERTY_RATE, Double.class);
//		Double newQuote = newExchange.getIn().getHeader(Constants.PROPERTY_RATE, Double.class);
//
//		// return the winner with the lowest rate
//		if (oldQuote.doubleValue() <= newQuote.doubleValue()) {
//			return oldExchange;
//		} else {
//			return newExchange;
//		}
		
		return null;
	}

}