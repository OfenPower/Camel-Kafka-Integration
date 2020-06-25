package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ReplyProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String bankName = exchange.getIn().getHeader(Constants.PROPERTY_BANK, String.class);
		String ssn = exchange.getIn().getHeader(Constants.PROPERTY_SSN, String.class);
		Double rate = exchange.getIn().getHeader(Constants.PROPERTY_RATE, Double.class);

		String answer = "The best rate is [ssn:" + ssn + " bank:" + bankName + " rate:" + rate + "]";
		exchange.getOut().setBody(answer);
	}

}