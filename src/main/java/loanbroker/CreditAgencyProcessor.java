package loanbroker;

import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CreditAgencyProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String ssn = exchange.getIn().getHeader(Constants.PROPERTY_SSN, String.class);
		Random rand = new Random();
		int score = (int) (rand.nextDouble() * 600 + 300);
		int hlength = (int) (rand.nextDouble() * 19 + 1);

		exchange.getOut().setHeader(Constants.PROPERTY_SCORE, score);
		exchange.getOut().setHeader(Constants.PROPERTY_HISTORYLENGTH, hlength);
		exchange.getOut().setHeader(Constants.PROPERTY_SSN, ssn);
	}

}